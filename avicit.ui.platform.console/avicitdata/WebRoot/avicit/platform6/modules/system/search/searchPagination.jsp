<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>


每页显示&nbsp;${result.rows}&nbsp;条记录&nbsp;共&nbsp;${result.totalPage}&nbsp;页&nbsp;当前第&nbsp;${result.page}&nbsp;页&nbsp;
    <c:if test="${result.totalPage!=1}">  
   					 <form method="post" action="platform/search/search.html"   style="margin:0px;display: inline;">
							 		<input id ="pageIndex" name="page" value="1"  style="width: 0px;display:none;"/>      
							 		<input id ="pageKeywords" name="keywords" value="${keyword}"  style="width: 0px;display:none;"/>       
							 		<input id ="rows" name="rows" value="5"  style="width: 0px;display:none;"/>       
									<input id = "previousPage" type="submit" value="首页"  class="next" />
					</form>
        <c:choose>  
            <c:when test="${result.page<=5}">  
                <c:forEach var="i" begin="2" end="${result.page}">  
                    <form method="post" action="platform/search/search.html"   style="margin:0px;display: inline;">
							 		<input id ="pageIndex" name="page" value="${i}"  style="width: 0px;display:none;"/>      
							 		<input id ="pageKeywords" name="keywords" value="${keyword}"  style="width: 0px;display:none;"/>       
							 		<input id ="rows" name="rows" value="5"  style="width: 0px;display:none;"/>       
									<input id = "previousPage" type="submit" value="[${i }]"  class="next" />
					</form>
                </c:forEach>  
            </c:when>  
            <c:otherwise>  
                ...&nbsp;  
                <c:forEach var="i" begin="${result.page-3}"  end="${result.page}">  
                    <form method="post" action="platform/search/search.html"   style="margin:0px;display: inline;">
							 		<input id ="pageIndex" name="page" value="${i}"  style="width: 0px;display:none;"/>      
							 		<input id ="pageKeywords" name="keywords" value="${keyword}"  style="width: 0px;display:none;"/>       
							 		<input id ="rows" name="rows" value="5"  style="width: 0px;display:none;"/>       
									<input id = "previousPage" type="submit" value="[${i }]"  class="next" />
					</form>
                </c:forEach>  
            </c:otherwise>  
        </c:choose>  
        
        
        <c:choose>  
            <c:when test="${result.page>=result.totalPage-4   || result.totalPage-4<=0}">  
                <c:forEach var="i" begin="${result.page+1}"  end="${result.totalPage}">  
                    <form method="post" action="platform/search/search.html"   style="margin:0px;display: inline;">
							 		<input id ="pageIndex" name="page" value="${i}"  style="width: 0px;display:none;"/>      
							 		<input id ="pageKeywords" name="keywords" value="${keyword}"  style="width: 0px;display:none;"/>       
							 		<input id ="rows" name="rows" value="5"  style="width: 0px;display:none;"/>       
									<input id = "previousPage" type="submit" value="[${i }]"  class="next" />
					</form>
                </c:forEach>  
            </c:when>  
            <c:otherwise>  
                <c:forEach var="i" begin="${result.page+1}"  end="${result.page+3}">  
                    <form method="post" action="platform/search/search.html"   style="margin:0px;display: inline;">
							 		<input id ="pageIndex" name="page" value="${i }"  style="width: 0px;display:none;"/>      
							 		<input id ="pageKeywords" name="keywords" value="${keyword}"  style="width: 0px;display:none;"/>       
							 		<input id ="rows" name="rows" value="5"  style="width: 0px;display:none;"/>       
									<input id = "previousPage" type="submit" value="[${i }]"  class="next" />
					</form>
                </c:forEach>  
                ...&nbsp;  
                    <form method="post" action="platform/search/search.html"   style="margin:0px;display: inline;">
							 		<input id ="pageIndex" name="page" value="${result.totalPage-1}"  style="width: 0px;display:none;"/>      
							 		<input id ="pageKeywords" name="keywords" value="${keyword}"  style="width: 0px;display:none;"/>       
							 		<input id ="rows" name="rows" value="5"  style="width: 0px;display:none;"/>       
									<input id = "previousPage" type="submit" value="[${result.totalPage}]"  class="next" />
					</form>
            </c:otherwise>  
        </c:choose>  
    </c:if>  
    
    				<form method="post" action="platform/search/search.html"   style="margin:0px;display: inline;">
							 		<input id ="pageIndex" name="page" value="${result.totalPage}"  style="width: 0px;display:none;"/>      
							 		<input id ="pageKeywords" name="keywords" value="${keyword}"  style="width: 0px;display:none;"/>       
							 		<input id ="rows" name="rows" value="5"  style="width: 0px;display:none;"/>       
									<input id = "previousPage" type="submit" value="末页"  class="next" />
					</form>
    
