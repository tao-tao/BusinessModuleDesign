<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="avicit.platform6.core.api.system.syslocale.PlatformLocalesJSTL"%>

<!-- 没有对应的搜索结果时展现的提示信息，  start -->
<div id="noResultTip" style="margin-top: 35px" >  
	<p class="STYLE1" ><%=PlatformLocalesJSTL.getBundleValueforSearch("search.accessory.noResultTip.caption")%></p>
	
	<div id="noResultTip" style="margin-top: 50px" > 
		<p class="STYLE3" ><%=PlatformLocalesJSTL.getBundleValueforSearch("search.accessory.noResultTip.detail1")%>&nbsp;<font class="STYLE4">${searchKeywords}</font>&nbsp;<%=PlatformLocalesJSTL.getBundleValueforSearch("search.accessory.noResultTip.detail2")%></p>
		
		<p class="STYLE3" ><%=PlatformLocalesJSTL.getBundleValueforSearch("search.accessory.noResultTip.suggestCaption")%></p>
		<ul>
			<li class="STYLE3">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<%=PlatformLocalesJSTL.getBundleValueforSearch("search.accessory.noResultTip.suggest1")%>
			</li>
			<li class="STYLE3">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<%=PlatformLocalesJSTL.getBundleValueforSearch("search.accessory.noResultTip.suggest2")%>
			</li>
			<li class="STYLE3">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<%=PlatformLocalesJSTL.getBundleValueforSearch("search.accessory.noResultTip.suggest3")%>
			</li>
			<li class="STYLE3">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<%=PlatformLocalesJSTL.getBundleValueforSearch("search.accessory.noResultTip.suggest4")%>
			</li>
			<li class="STYLE3">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<%=PlatformLocalesJSTL.getBundleValueforSearch("search.accessory.noResultTip.suggest5")%>
			</li>
			<li class="STYLE3">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<%=PlatformLocalesJSTL.getBundleValueforSearch("search.accessory.noResultTip.suggest6")%>
			</li>
			
			<li> <%-- 没搜出结果的原因是没建立索引，或索引位置变化时， 页面给技术人员点提示信息，但此信息无需显式显示给普通用户。 --%>
				<font size="0.1" color="white">
					<c:if test="${isHasIndex == false}">
						no search index 
					</c:if>
				</font>
			</li>
						
		</ul>
	</div>
</div>

<!-- 没有对应的搜索结果时展现的提示信息， end -->