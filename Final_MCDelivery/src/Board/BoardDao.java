package Board;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BoardDao {
	// �����ͺ��̽� ���� ���� ���� ����
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private Statement stmt = null;
	private ResultSet rs = null;
	private String Driver = "oracle.driver.OracleDriver";
	private String URI = "jdbc:oracle:thin:@oracle.hotsun0428.cafe24.com:1521:orcl";
	private String ID = "hotsun0428";
	private String PW = "rudah0428";

	// Initialization
	private BoardDao() {
		try {
			Class.forName(Driver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Share Driver Instance
	static BoardDao instance;

	static public BoardDao getInstance() {
		if (instance == null) {
			instance = new BoardDao();
		}
		return instance;
	}

	// Connection
	public void connect() {
		try {
			conn = DriverManager.getConnection(URI, ID, PW);
			System.out.println("connection success");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("connection fail");
		}
	}

	// Disconnection
	public void disconnect() {
		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (stmt != null) {
			try {
				stmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (conn != null) {
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (rs != null) {
			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	//�Խñ� �Է� ���� �ش� ���� �׷��ȣ�� ������ �޼ҵ�
	public int getGroupId() throws BoardException
	{
		int board_groupId=1;
		try{
			connect();
			
			//board ������ �����ؾ���
			String sql = "select seq_group_id_article.nextval as board_groupId from dual";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if( rs.next() ){
				board_groupId = rs.getInt("board_groupId");
			}
			return board_groupId;
		}catch( Exception ex ){
			throw new BoardException("�Խ��� ) �Խñ� �Է� ���� �׷��ȣ ���� ��  : " + ex.toString() );	
		} finally{
			disconnect();
		}		
	}
	
	//�Խ��ǿ� ���� �Է½� DB�� �����ϴ� �޼ҵ� 
	//		( MC_board ���̺� �Խñ��� �Է��ϰ� �������� ������ �Խñ۹�ȣ�� ����
	public int insert( Board board ) throws BoardException
	{
		try{
			connect();
			String sql= "insert into MC_board values(?,?,?,?,sysdate,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, board.getBoard_id());
			pstmt.setString(2, board.getBoard_title());
			pstmt.setString(3, board.getBoard_content());
			pstmt.setInt(4, board.getBoard_password());
			pstmt.setString(5, board.getBoard_writer());
			pstmt.setString(6, board.getBoard_header());
			pstmt.setInt(7, board.getBoard_read_count());
			pstmt.setInt(8, board.getBoard_groupId());
			pstmt.setString(9, board.getBoard_sequence_no());
			
			int cnt = pstmt.executeUpdate();
			
			if(cnt>0){
				sql = "select board_id from MC_board where board_writer=? and board_title=?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, board.getBoard_writer());
				pstmt.setString(2, board.getBoard_title());
				rs = pstmt.executeQuery();
	
				rs.next();
				System.out.println(rs.getInt("board_id"));
				return rs.getInt("board_id");
			}
			else return -1;
		
		}catch( Exception ex ){
			throw new BoardException("�Խ��� ) DB�� �Է½� ����  : " + ex.toString() );	
		} finally{
			disconnect();
		}
		
	}
	
	// �Խù� ���ڵ带 �˻��ϴ� �޼ҵ�
	// ������ȣ(sequence_no)�� ��������
		public List<Board> selectList(int firstRow, int endRow) throws BoardException
		{
			PreparedStatement ps = null;
			ResultSet rs = null;
			List<Board> mList = new ArrayList<Board>();
			boolean isEmpty = true;
			
			try{
				connect();
				String sql = "select * from MC_board order by board_sequence_no desc";
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				
				for(int i=0; i<firstRow; i++) rs.next();
				
				while(rs.next()){
					isEmpty = false;
					int board_id = rs.getInt("board_id");
					String board_title = rs.getString("board_title");
					String board_content = rs.getString("board_content");
					int board_password = rs.getInt("board_password");
					String board_date = rs.getString("board_date");
					String board_writer = rs.getString("board_writer");
					String board_header = rs.getString("board_header");
					int board_read_count = rs.getInt("board_read_count");
					int board_group_id = rs.getInt("board_group_id");
					String board_sequence_no = rs.getString("board_sequence_no");
					
					Board board = new Board(board_id, board_title, board_content,
											board_password, board_date, board_writer, board_header,
											board_read_count, board_group_id, board_sequence_no);
					mList.add(board);
					firstRow++;
					if(firstRow==endRow) break;
				}
				
				if( isEmpty ) return Collections.emptyList();
				
				return mList;
			}catch( Exception ex ){
				throw new BoardException("�Խ��� ) DB�� ��� �˻��� ����  : " + ex.toString() );	
			} finally{
				disconnect();
			}		
		}	

	//�θ��ڵ��� �ڽķ��ڵ� �� ������ ���ڵ��� ������ȣ�� �˻��ϴ� �޼ҵ�(���� ���� ��ȣ���� ����������)
	public String selectLastSequenceNumber( String maxSeqNum, String minSeqNum ) throws BoardException
	{
		try{
			connect();
			String sql = "select board_sequence_no from MC_board where board_sequence_no<=? and board_sequence_no>=?";
			String str=null;
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, maxSeqNum);
			pstmt.setString(2, minSeqNum);
			rs = pstmt.executeQuery();
			while(rs.next()){
				str = rs.getString("board_sequence_no");
			}
			return str;	// ���߿� �˻��� ���� ����
		}catch( Exception ex ){
			throw new BoardException("�Խ��� ) �θ�� ������ �ڽ� ���ڵ� �� ������ ������ȣ ������  : " + ex.toString() );	
		} finally{
			disconnect();
		}			
	}
	
	//��ü ���ڵ� ���� ��ȯ�ϴ� �޼ҵ�
	public int totalRecordCount() throws BoardException{
		try{
			connect();
			String sql = "select count(*) cnt from MC_board";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			rs.next();
			return rs.getInt("cnt");
		}catch( Exception ex ){
			throw new BoardException("�Խ��� ) �� �Խñ� ī��Ʈ ����  : " + ex.toString() );
		} finally{
			disconnect();
		}		
	}		
	
}
