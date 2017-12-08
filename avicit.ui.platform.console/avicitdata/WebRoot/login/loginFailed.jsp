<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<% 
 String host = request.getContextPath();
%>
<meta content="text/html; charset=utf-8" http-equiv="Content-Type">
<link href="../login/style/cmstop-error.css" type="text/css" rel="stylesheet">
<script type="text/javascript">
function returnLogin(){
	top.location.href="<%=host%>/login/login.jsp";
}

</script>
</head>
<body>
	<div class="main">
    <p class="title">非常抱歉，您无权访问该界面</p>
    <a href="#" class="btn" onclick="returnLogin();">返回网站首页</a>
	</div>
</body>
</html>
