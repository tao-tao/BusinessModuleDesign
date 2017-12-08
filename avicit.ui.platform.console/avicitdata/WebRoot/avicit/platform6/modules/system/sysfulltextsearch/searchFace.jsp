<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@ page language="java"  pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@page import="avicit.platform6.commons.utils.ViewUtil"%>

<%-- 搜索引擎主体页面， 包含通用搜索框，搜索结果，分页跳转按钮等--%>
<html>
  <head>
  
    <title>搜索</title>
    <base href="<%=ViewUtil.getRequestPath(request)%>">                  
    
    <link href="avicit/platform6/modules/system/sysfulltextsearch/css/commonSearch.css" rel="stylesheet" type="text/css" /> <%-- 搜索的主体样式 --%>
    <link href="avicit/platform6/modules/system/sysfulltextsearch/css/fulltextSearchSuggest.css" rel="stylesheet" type="text/css" />     <%-- 热输入提示的样式 --%>
    <link href="avicit/platform6/modules/system/sysfulltextsearch/css/searchPagination.css" rel="stylesheet" type="text/css" />     <%-- 分页按钮的样式 --%>
     
    <script type="text/javascript" src="avicit/platform6/component/js/jQuery/jQuery-1.8.2/jquery-1.8.2.min.js"></script>
    <script type="text/javascript" src="avicit/platform6/component/js/jQuery/jquery-easyui-1.3.5/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="avicit/platform6/component/js/jQuery/jquery-easyui-1.3.5/easyloader.js"></script>
  </head>
  
  <body>
  <!--<body onload="focusKeywords(); "> -->
	<div id="out">  <!-- div out start  -->
		<div id="in"> <!-- div in start  -->
		
			    <!-- 控制搜索框样式，居左 start -->
				<div align="left" style="margin-top: 35px; padding-left: 150px" >
					 <%@include file="core/searchBox/searchBox.jsp"  %>
			    </div>
			    <!-- 控制搜索框样式  end -->	
			    
			    <!-- 搜索结果  -->
			    <div align="left" style="margin-top: 35px; padding-left: 150px" >
			    	<%@include file="core/searchResult/searchResult.jsp"  %>
			    </div>
			    			    
			    <!-- 加个脚注，纯属装饰，不然下面显得太空了 -->
			    <%@include file="accessory/searchFoot.jsp"  %> 
			    
		</div>  <!-- div in end  -->
	</div>  <!-- div out end  -->
  </body>
  
</html>
