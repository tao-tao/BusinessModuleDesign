<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page import="avicit.platform6.modules.system.sysfulltextsearch.util.searchSysPath.SearchPathUtil" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div id="page"> 
    <c:if test="${!empty hasResult}">
			<c:if test="${pageIndex != '1'}"> 
						<input id ="previousPage" onclick="clickPagination(${previousPageIndex}, 5)" value='<上一页'  class="next" style="border: 0px solid #EBEBEB;background-color:#EBEBEB;vertical-align:middle;text-align:center;"/>
			</c:if>
			
			<c:forEach items="${indexList}" var="indexItem">	
					<c:if test="${pageIndex != indexItem}"> 
							 <input id ="${indexItem}" onclick="clickPagination(${indexItem}, 5)" value="${indexItem}"  class="pc" style="border: 0px solid #EBEBEB;background-color:#EBEBEB;vertical-align:middle;text-align:center;"/>
					</c:if>
					<c:if test="${pageIndex == indexItem}"> 	   
							 <input id ="${indexItem}" readonly="readonly" value="${indexItem}" style="border: 0px solid #EBEBEB;background-color:white;margin-left:20px;margin-right:25px;width:13px;vertical-align:middle;text-align:center;"/>						
					</c:if>
			</c:forEach>
			      	      
			<c:if test="${pageIndex != pageCount}"> 
					<input id = "nextPage"  onclick="clickPagination(${nextPageIndex}, 5)" value='下一页>'  class="next" style="border: 0px solid #EBEBEB;background-color:#EBEBEB;vertical-align:middle;text-align:center;"/>
			</c:if>
     </c:if> 
</div>

<script type="text/javascript">
<!--
    // 加载完成后再执行加载分页按钮的动态样式
	// paginationButton();
//-->
</script>

