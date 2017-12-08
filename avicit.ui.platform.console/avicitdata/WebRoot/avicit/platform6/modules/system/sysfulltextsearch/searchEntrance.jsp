<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@ page language="java"  pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@page import="avicit.platform6.commons.utils.ViewUtil"%>

<%-- 搜索引擎入口页面， 包含一个通用搜索框，有版权声明等说明性信息--%>
<html>
  <head>
  
    <title>搜索入口</title>
    <base href="<%=ViewUtil.getRequestPath(request)%>">
    <link href="avicit/platform6/modules/system/sysfulltextsearch/css/commonSearch.css" rel="stylesheet" type="text/css" /> <%-- 搜索的主体样式 --%>
    <link href="avicit/platform6/modules/system/sysfulltextsearch/css/fulltextSearchSuggest.css" rel="stylesheet" type="text/css" />     <%-- 热输入提示的样式css  --%>
    
    <script type="text/javascript" src="avicit/platform6/component/js/jQuery/jQuery-1.8.2/jquery-1.8.2.min.js"></script>
    <script type="text/javascript" src="avicit/platform6/component/js/jQuery/jquery-easyui-1.3.5/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="avicit/platform6/component/js/jQuery/jquery-easyui-1.3.5/easyloader.js"></script>
  </head>
  
  <body>
	<div id="out">  <!-- div out start  -->
		<div id="in"> <!-- div in start  -->
			    <%-- 控制搜索框样式，居中 start --%>	
				<div align="center" style="margin-top: 35px;padding-left: 350px; " >
					 <%@include file="core/searchBox/searchBox.jsp"  %>
			    </div>
			    <%-- 控制搜索框样式  end --%>	 
			    
			    <br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>
			    
			    <!-- 版权声明  -->
			    <div align="center" style="margin-top: 35px" >
			    	<%@include file="accessory/searchCopyRight.jsp"  %> 
			    </div>  			    	
			    	    
		</div>  <!-- div in end  -->
	</div>  <!-- div out end  -->
  </body>
</html>
