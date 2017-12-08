<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@page import="avicit.platform6.commons.utils.ViewUtil"%>
<%@ page import="avicit.platform6.modules.system.sysfulltextsearch.util.searchSysPath.SearchPathUtil" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
	<head>
	
	<title>搜索配置</title>
    <base href="<%=ViewUtil.getRequestPath(request)%>">
    <link href="avicit/platform6/modules/system/sysfulltextsearch/css/searchConfig.css" rel="stylesheet" type="text/css" /> <%-- 搜索的主体样式 --%>
	
	<script type="text/javascript" src="avicit/platform6/modules/system/sysfulltextsearch/js/searchUtil.js"></script>
	<script type="text/javascript" src="avicit/platform6/modules/system/sysfulltextsearch/js/searchConfig.js"></script>  <%-- 搜索引擎配置的 js方法--%>

	</head>

		<body onload= "tableStyle('searchEngineConfigTable', '#ffffff', '#f5fafe', '#EFEFEF', '#9FF1AF'); tableStyle('searchEngineOperateTable', '#ffffff', '#f5fafe', '#EFEFEF', '#9FF1AF');" > 
		
		<!-- 操作等待提示框；默认隐藏，动作执行过程中(比如创建索引过程中) 显示  -->
	    <div id="searchWaitTipBox" class="searchWaitTipBox" align="center" >
	    	<div id="searchWaitTipWords" style="padding-top: 40px;font-size: 4">
	    
	    	</div>
	    </div>
	     
		<%-- 搜索引擎参数配置 --%>
		<%@include file="../core/searchConfig/searchConfigPara.jsp"  %>
		
		<%-- 搜索引擎操作--%>
		 <%@include file="../core/searchConfig/searchConfigOperator.jsp"  %>
		 
		</body>
</html>