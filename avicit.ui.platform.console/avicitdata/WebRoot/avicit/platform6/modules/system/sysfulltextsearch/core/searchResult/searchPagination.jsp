<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>

<%@ page import="avicit.platform6.modules.system.sysfulltextsearch.util.searchSysPath.SearchPathUtil" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="avicit.platform6.core.api.system.syslocale.PlatformLocalesJSTL"%>

<%String hasResult2 = (String)request.getParameter("hasResult"); %>
<% if(hasResult2.startsWith("1")) { %>                  <%--有结果才显示分页页码--%>

<c:if test="${pageination == 'pageination'}">           <%--分页情况下,才显示分页页码--%>

	    <div id="page"> 
	    
					<c:if test="${pageIndex != '1'}"> 
			                <form method="post" action="<c:url value="<%= SearchPathUtil.SEARCH_SEARCH_ALL_INDEX_BYPAGE_ACTION %>"/>" style="margin:0px;display: inline;">
							 		<input id ="pageIndex" name="pageIndex" value="${previousPageIndex}"  style="width: 0px;display:none;"/>        <!--  传值用的隐藏控件 -->
							 		<input id ="pageKeywords" name="keywords" value="${searchKeywords}"  style="width: 0px;display:none;"/>        <!--  传值用的隐藏控件 -->
							 		<input id ="pageSize" name="pageSize" value="5"  style="width: 0px;display:none;"/>        <!--  传值用的隐藏控件 -->
									<input id = "previousPage" type="submit" value='<%=PlatformLocalesJSTL.getBundleValueforSearch("search.pageination.previous")%>'  class="next" /> <%--onmouseover="showPageButtonStyle('previousPage')" onmouseout="hidePageButtonStyle('previousPage')" --%>  
									                                                                        <%-- <上一页 --%>
							</form>
					</c:if>
					
					<c:forEach items="${indexList}" var="indexItem">	
							<c:if test="${pageIndex != indexItem}"> 
									<form method="post" action="<c:url value="<%= SearchPathUtil.SEARCH_SEARCH_ALL_INDEX_BYPAGE_ACTION %>"/>" style="margin:0px;display: inline;">
									 		<input id ="pageIndex" name="pageIndex" value="${indexItem}"  style="width: 0px;display:none;"/>        <!--  传值用的隐藏控件 -->
									 		<input id ="pageKeywords" name="keywords" value="${searchKeywords}"  style="width: 0px;display:none;"/>        <!--  传值用的隐藏控件 -->
									 		<input id ="pageSize" name="pageSize" value="5"  style="width: 0px;display:none;"/>        <!--  传值用的隐藏控件 -->
											<input id ="${indexItem}" type="submit" value="${indexItem}"  class="pc"/> <%--onmouseover="showPageButtonStyle('${indexItem}')" onmouseout="hidePageButtonStyle('${indexItem}')" --%> 
									</form>	
							</c:if>
							<c:if test="${pageIndex == indexItem}"> 
											<input id ="${indexItem}" type="submit" value="${indexItem}" style="border: 0; background: transparent;width:35px;height: 22px; "/>
							</c:if>
					</c:forEach>
			         
			        
			         <c:if test="${pageIndex != pageCount}"> 
								<form method="post" action="<c:url value="<%= SearchPathUtil.SEARCH_SEARCH_ALL_INDEX_BYPAGE_ACTION %>"/>" style="margin:0px;display: inline;">
								 		<input id ="pageIndex" name="pageIndex" value="${nextPageIndex}"  style="width: 0px;display:none;"/>        <!--  传值用的隐藏控件 -->
								 		<input id ="pageKeywords" name="keywords" value="${searchKeywords}"  style="width: 0px;display:none;"/>        <!--  传值用的隐藏控件 -->
								 		<input id ="pageSize" name="pageSize" value="5"  style="width: 0px;display:none;"/>        <!--  传值用的隐藏控件 -->
										<input id = "nextPage" type="submit" value='<%=PlatformLocalesJSTL.getBundleValueforSearch("search.pageination.next")%>'  class="next"/> <%--onmouseover="showPageButtonStyle('nextPage')" onmouseout="hidePageButtonStyle('nextPage')"--%> 
										                                                                       <%--下一页>--%>
								</form>
					</c:if>
	
		</div>
</c:if>

<% } %>