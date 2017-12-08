<%@ page contentType="text/html; charset=UTF-8"%>
<html>
<head>
<title>注销页面</title>
</head>
<body topmargin="0" bottommargin="0" leftmargin="0" rightmargin="0">
	<%
		/*执行注销的过程*/
		//String loginurl = "login.jsp";
		//String loginurl = "http://222.avicit.org:8080/cas3/logout?service=http://222.avicit.org:8080/cas3/login";
		
		session.invalidate();
		
		out.println("ok........");
		
	%>
</body>
</html>

