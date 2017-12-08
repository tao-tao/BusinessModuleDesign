<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.Enumeration"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=8">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>登录转向</title>

<%
	 String indexPage = session.getAttribute("LOGINSUCCESSNEXTURL_").toString();
	indexPage =request.getContextPath()+indexPage;
	
%>
<script language="javascript">
	location.href = "<%=indexPage%>";
</script>
</head>
<body>
</body>
</html>