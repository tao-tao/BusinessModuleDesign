<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<% 
 String host = request.getContextPath();
%>
<script type="text/javascript">
	top.location.href="<%=host%>/login/login.jsp";
</script>
</head>
</html>
