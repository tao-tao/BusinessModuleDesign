<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@ page language="java"  pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>

<%-- 搜索引擎高级检索主体页面， 包含高级搜索框，搜索结果，分页跳转按钮等--%>
<html>
  <head>
  
    	<title>高级检索</title>
    	<base href="<%=ViewUtil.getRequestPath(request)%>">                  
    	<jsp:include page="/avicit/platform6/component/common/EasyUIJsInclude.jsp"></jsp:include>
    		
    	<link href="avicit/platform6/modules/system/sysfulltextsearch/css/advancedSearchFace.css" rel="stylesheet" type="text/css" />     
    	<link href="avicit/platform6/modules/system/sysfulltextsearch/css/advancedSearchBox.css" rel="stylesheet" type="text/css" />  
    	           	  	
    	<%-- 放在页面后面，页面主体加载完毕后执行 --%>
        <script type="text/javascript">
    		var advancedSearchResult = null;
			var advancedSearchResultWin = null;
			var sysAdvancedSearchForm = null;		
		</script>
  </head>
  
  <body>
        <div id="index-head" style="background-color: #F7F7F7">
        	<div class="headsearch">
        	    <div class="headslogo" style="background:none;margin-top:15px;margin-bottom:20px">
        	    	<img src="avicit/platform6/modules/system/sysfulltextsearch/images/searchFace/advancedSearchHeader.jpg">
        	    </div>
        	    <%@include file="core/searchBox/advancedSearchBox.jsp"%>
        	</div>
        </div>
        
	    <iframe id="advancedSearchResult" 
	            name="advancedSearchResult"
	            width="100%" 
	            height="750"
	            scrolling="auto"
		        frameborder="0"
		        src="avicit/platform6/modules/system/sysfulltextsearch/core/searchResult/advancedSearchResult.jsp">
	    </iframe>

	    <%-- 统一的尾部 --%>
        <div id="zhsh_foot">&copy; 2013 金航数码科技有限责任公司</div>
        
        <%-- 放在页面后面，页面主体加载完毕后执行 --%>
        <script type="text/javascript">
    		advancedSearchResult = document.getElementById("advancedSearchResult");
			advancedSearchResultWin = advancedSearchResult.contentWindow;
			sysAdvancedSearchForm = null;
			
			// 兼容Firefox/Opera/Safari/IE的处理方式,等ifram内的表单加载完成后再获取表单对象
			advancedSearchResult.onload = advancedSearchResult.onreadystatechange = function() {
			     if (this.readyState && this.readyState != 'complete')   
			    	 return;        
			     else{
			    	 sysAdvancedSearchForm = advancedSearchResultWin.document.getElementById("sysAdvancedSearchForm");
			     }
			};
		</script>
  </body>
  
</html>
