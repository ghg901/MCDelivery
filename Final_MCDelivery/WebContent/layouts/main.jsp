<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Insert title here</title>
</head>
<body>
	<!-- 페이지의 흐름제어를 page 속성값이 지정한 페이지로 이동 -->
	<!-- CONTENTPAGE="content.jsp" 값을 넘겨준다 -->
	<jsp:forward page="template.jsp">		
		<jsp:param name="CONTENTPAGE" value="content.jsp" />
	</jsp:forward>
</body>
</html>