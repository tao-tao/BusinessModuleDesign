<%@ page language="java"  pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@page import="avicit.platform6.commons.utils.ViewUtil"%>
<html>
  <head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>搜索入口</title>
    <base href="<%=ViewUtil.getRequestPath(request)%>">
    <link href="avicit/platform6/modules/system/search/css/commonSearch.css" rel="stylesheet" type="text/css" /> <%-- 搜索的主体样式 --%>
    <link href="avicit/platform6/modules/system/search/css/fulltextSearchSuggest.css" rel="stylesheet" type="text/css" />     <%-- 热输入提示的样式css  --%>
    <link href="avicit/platform6/modules/system/search/css/advancedSearchResult.css" rel="stylesheet" type="text/css" />       <%-- 搜索结果的样式 --%>                    
	<link href="avicit/platform6/modules/system/search/css/advancedSearchPagination.css" rel="stylesheet" type="text/css" />   <%-- 分页按钮相关的样式 --%>
    
    <script type="text/javascript" src="avicit/platform6/component/js/jQuery/jQuery-1.8.2/jquery-1.8.2.min.js"></script>
    <script type="text/javascript" src="avicit/platform6/component/js/jQuery/jquery-easyui-1.3.5/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="avicit/platform6/component/js/jQuery/jquery-easyui-1.3.5/easyloader.js"></script>
    
    <script type="text/javascript">
  </script>
    
  </head>
  
  
  
  <body>
	<div id="out"> 
		<div id="in"> 
			    <%-- 控制搜索框样式，居中 start --%>	
				<div align="center" style="margin-top: 10px;padding-left: 100px; " >
				<form name="sysFullTextSearchForm" method="post" action="platform/search/search.html"  >  
					<span class="s_ipt_wr">
						<input id="keywords" type="text" value="${keyword}" name="keywords" maxlength="100" class="s_ipt" autocomplete="off"/>
					</span>
					<span class="s_btn_wr">
						<input type="submit" value="搜索"   id="su" style="background: url(avicit/platform6/modules/system/sysfulltextsearch/images/searchBox/sousbg.png/i-1.0.0.png) repeat scroll 0 0 #DDDDDD;" class="s_btn"/> <%--搜索--%>
					</span>
				</form>
			    </div>
			    
			    <c:if test="${error=='1'}">
			    		<%@include file="errorTip.jsp"  %> 
			    </c:if>
			    
			    
			    <c:if test="${result.result !=null}">
				    <c:forEach var="item" items="${result.result}" varStatus="status">
	       					<h4>${item.displayName}:
			       					<c:if test="${item.dataType eq '1'}">
		                					<a href="${item.displayUrl}"  >${item.title}</a>
		                			</c:if>  
		                			<c:if test="${item.dataType eq '2'}">
		                					<a href="platform/search/download/${item.id}.html"  >${item.title}</a>
		                			</c:if>  
	       					</h4>
	        				<h5>摘要：${item.content}</h5>
					</c:forEach>
					<c:choose>  
                			<c:when test="${result.result== null || fn:length(result.result) == 0}">
                					<%@include file="noResultTip.jsp"  %> 
                			</c:when>  
                			<c:otherwise>
                					<%@include file="searchPagination.jsp"  %> 
                			</c:otherwise>  
            		</c:choose>  
				</c:if>
			    	    
		</div>
	</div>  
  </body>
</html>
