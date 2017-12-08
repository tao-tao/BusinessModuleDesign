<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@ page language="java"  pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@page import="avicit.platform6.commons.utils.ViewUtil"%>
<html>
<head>

<title>迷你搜索入口</title>
<base href="<%=ViewUtil.getRequestPath(request)%>">
<link href="avicit/platform6/modules/system/sysfulltextsearch/css/miniSearchEntrance.css" rel="stylesheet" type="text/css" />

</head>

<body>		
				<!-- 控制搜索框样式，居中 start -->
				<div align="center" style="margin-top: 75px" >
					 <%@include file="core/searchBox/miniSearchBox.jsp"  %>
			    </div>
			    <!-- 控制搜索框样式  end -->	
			    
			    <!-- 版权声明  -->
			    <div align="center" style="margin-top: 35px" >
			    	<%@include file="accessory/searchCopyRight.jsp"  %> 
			    </div>  				    
</body>
</html>