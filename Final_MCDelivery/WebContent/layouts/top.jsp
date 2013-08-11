<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<%@ page import="Member.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Top Page</title>
<script type="text/javascript" src="../assets/js/checkLogin.js"></script>
<link rel="stylesheet" href="../assets/css/page.css" type="text/css">
<link rel="stylesheet" href="../assets/css/site.css" type="text/css">
</head>
<%
	request.setCharacterEncoding("EUC-KR");
	Object LoginID = session.getAttribute("LoginID");
%>
<body>
	<form method="post" name="top">
	
<%	
	MemberDao memberDao = MemberDao.getInstance();
	String inputID = "";
	String inputPW = "";
	
	//ID
	inputID = request.getParameter("inputID");
	if(inputID == null)
	{
		inputID = "";
	}
	
	inputPW = request.getParameter("inputPW");
	if(inputPW == null)
	{
		inputPW = "";
	}

	boolean isLogin = memberDao.setLogin(inputID, inputPW);
	
	if(!inputID.equals("") && !inputPW.equals(""))
	{
		if(isLogin){
			//set Session
			session.setAttribute("LoginID", inputID);
			response.sendRedirect("main.jsp");
			return;
		}
		else{
			response.sendRedirect("login.jsp");
		}
	}
%>
		<table width="100%">
			<tr>
				<td width="34%" rowspan="2">
					<img src="http://hotsun0428.cafe24.com/MC_Delivery/Images/logo.jpg"	height="100px" />
				</td>
				<td width="64%" height="56" align="right">
					<%if(LoginID == null){ %>
						<label>ID</label><input type="text" name="inputID">
						<label>PW</label><input type="password" name="inputPW">
						<input type="submit" value="로그인">
					<%}else{ %>
						<label><%=(String)LoginID %>님 환영합니다</label>
						<input type="submit" value="로그아웃">
					<%} %>
				</td>
			</tr>
			<tr>
				<td colspan="3" align="right">
					<a href="../member/join.jsp">회원가입</a>
					<a href="../member/mypage.jsp">마이페이지</a>
					<a href="../member/search.jsp">ID/PASSWORD찾기</a>
					<% if((String)LoginID=="admin@naver.com"){ %>
						<a href="../member/manage.jsp">Manage</a>
					<% } %>	
				</td>
			</tr>
		</table>
		<ul class="tabs">
			<li class="selected">
				<h3 class="tab-label">Menu</h3>
			</li>
			<li>
				<h3 class="tab-label">Order</h3>
			</li>
			<li>
				<h3 class="tab-label">Find Store</h3>
			</li>
			<li>
				<h3 class="tab-label">Notice</h3>
			</li>
		</ul>
	</form>
</body>
</html>