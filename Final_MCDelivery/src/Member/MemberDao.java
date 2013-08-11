package Member;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class MemberDao {
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
	private MemberDao() {
		try {
			Class.forName(Driver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Share Driver Instance
	static MemberDao instance;

	static public MemberDao getInstance() {
		if (instance == null) {
			instance = new MemberDao();
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
				System.out.println("disconnect success");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (stmt != null) {
			try {
				stmt.close();
				System.out.println("disconnect success");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (conn != null) {
			try {
				conn.close();
				System.out.println("disconnect success");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (rs != null) {
			try {
				rs.close();
				System.out.println("disconnect success");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean setLogin(String id, String pw){
		boolean success = false;		
		connect();
		String sql = "";
		try{
			stmt = conn.createStatement();
			ResultSet rs = null;
			
			sql =	" select * from mc_member ";
			sql+= 	" where ";
			sql+= 	" member_email='"+ id +"' ";
			sql+= 	" and member_password='"+ pw+"' ";			
			
			rs = stmt.executeQuery(sql);
			
			if(rs.next()){
				success = true;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			disconnect();
		}
		return success;
	}
}
