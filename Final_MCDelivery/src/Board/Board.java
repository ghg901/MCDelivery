package Board;

public class Board {
	private int board_id;				//글 번호
	private String board_title;			//글 제목
	private String board_content;		//글 내용
	private int board_password;			//글 비밀번호
	private String board_date;			//글 작성일자
	private String board_writer;		//글 작성자
	private String board_header;		//글 말머리
	private int board_read_count;		//글 조회수
	private int board_groupId;			//그룹 번호
	private String board_sequence_no;	//시퀀스 번호
	
	
	public Board() {
	}
	
	public Board(int board_id, String board_title, String board_content, 
					int board_password, String board_date, String board_writer, 
					String board_header, int board_read_count, 
					int board_groupId, String board_sequence_no) {
		this.board_id = board_id;
		this.board_title = board_title;
		this.board_content = board_content;
		this.board_password = board_password;
		this.board_date = board_date;
		this.board_writer = board_writer;
		this.board_header = board_header;
		this.board_read_count = board_read_count;
		this.board_groupId = board_groupId;
		this.board_sequence_no = board_sequence_no;
	}
	
	public int getBoard_id() {
		return board_id;
	}
	public void setBoard_id(int board_id) {
		this.board_id = board_id;
	}
	public String getBoard_title() {
		return board_title;
	}
	public void setBoard_title(String board_title) {
		this.board_title = board_title;
	}
	public String getBoard_content() {
		return board_content;
	}
	public void setBoard_content(String board_content) {
		this.board_content = board_content;
	}
	public int getBoard_password() {
		return board_password;
	}
	public void setBoard_password(int board_password) {
		this.board_password = board_password;
	}
	public String getBoard_date() {
		return board_date;
	}
	public void setBoard_date(String board_date) {
		this.board_date = board_date;
	}
	public String getBoard_writer() {
		return board_writer;
	}
	public void setBoard_writer(String board_writer) {
		this.board_writer = board_writer;
	}
	public String getBoard_header() {
		return board_header;
	}
	public void setBoard_header(String board_header) {
		this.board_header = board_header;
	}
	public int getBoard_read_count() {
		return board_read_count;
	}
	public void setBoard_read_count(int board_read_count) {
		this.board_read_count = board_read_count;
	}
	public int getBoard_groupId() {
		return board_groupId;
	}
	public void setBoard_groupId(int board_groupId) {
		this.board_groupId = board_groupId;
	}
	public String getBoard_sequence_no() {
		return board_sequence_no;
	}
	public void setBoard_sequence_no(String board_sequence_no) {
		this.board_sequence_no = board_sequence_no;
	}
	
	//게시물의 시퀀스번호에 따른 레벨 반환
	public int getLevel()
	{
		if( board_sequence_no == null			)	{ return -1; }
		if( board_sequence_no.length() != 16 	) 	{ return -1; }
		if( board_sequence_no.endsWith("999999")) 	{ return 0; }
		if( board_sequence_no.endsWith("9999")	) 	{ return 1; }
		if( board_sequence_no.endsWith("99")	) 	{ return 2; }
		return 3;
	}
}
