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
	// 데이터베이스 연결 관련 변수 선언
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
	
	//게시글 입력 전에 해당 글의 그룹번호를 얻어오는 메소드
	public int getGroupId() throws BoardException
	{
		int board_groupId=1;
		try{
			connect();
			
			//board 시퀀스 수정해야함
			String sql = "select seq_group_id_article.nextval as board_groupId from dual";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if( rs.next() ){
				board_groupId = rs.getInt("board_groupId");
			}
			return board_groupId;
		}catch( Exception ex ){
			throw new BoardException("게시판 ) 게시글 입력 전에 그룹번호 얻어올 때  : " + ex.toString() );	
		} finally{
			disconnect();
		}		
	}
	
	//게시판에 글을 입력시 DB에 저장하는 메소드 
	//		( MC_board 테이블에 게시글을 입력하고 시퀀스로 지정된 게시글번호를 리턴
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
			throw new BoardException("게시판 ) DB에 입력시 오류  : " + ex.toString() );	
		} finally{
			disconnect();
		}
		
	}
	
	// 게시물 레코드를 검색하는 메소드
	// 순서번호(sequence_no)로 역순정렬
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
				throw new BoardException("게시판 ) DB에 목록 검색시 오류  : " + ex.toString() );	
			} finally{
				disconnect();
			}		
		}	

	//부모레코드의 자식레코드 중 마지막 레코드의 순서번호를 검색하는 메소드(제일 작은 번호값이 마지막값임)
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
			return str;	// 나중에 검색한 값을 리턴
		}catch( Exception ex ){
			throw new BoardException("게시판 ) 부모와 연관된 자식 레코드 중 마지막 순서번호 얻어오기  : " + ex.toString() );	
		} finally{
			disconnect();
		}			
	}
	
	//전체 레코드 수를 반환하는 메소드
	public int totalRecordCount() throws BoardException{
		try{
			connect();
			String sql = "select count(*) cnt from MC_board";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			rs.next();
			return rs.getInt("cnt");
		}catch( Exception ex ){
			throw new BoardException("게시판 ) 총 게시글 카운트 오류  : " + ex.toString() );
		} finally{
			disconnect();
		}		
	}		
	
}
