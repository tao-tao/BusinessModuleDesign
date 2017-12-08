<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">

<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page import="avicit.platform6.modules.system.sysfulltextsearch.util.searchSysPath.SearchPathUtil" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="avicit.platform6.core.api.system.syslocale.PlatformLocalesJSTL"%>

<%
		String fullTextSearchPath = SearchPathUtil.deletePrefix(SearchPathUtil.SEARCH_SEARCH_ALL_INDEX_BYPAGE_ACTION) + "?keywords=";
%>

<script type="text/javascript" src="avicit/platform6/modules/system/sysfulltextsearch/js/searchUtil.js"></script>
<script type="text/javascript" src="avicit/platform6/modules/system/sysfulltextsearch/js/searchCommon.js"></script>
<script type="text/javascript" src="avicit/platform6/modules/system/sysfulltextsearch/js/miniSearch.js"></script>

<%-- 可被别的页面包含，引用；任何jsp页面include本页面后修改一下路径， 便具备了全文搜索功能   --%>
<form id="sysMiniSearchForm" name="sysMiniSearchForm" >  <%-- 这里是js调用到后台,  加form只是为了支持回车 --%>
			<span class="s_ipt_wr">
				<input type="text" value="${searchKeywords}" name="keywords" id="keywords" name="wd" id="miniKw" maxlength="100" class="s_ipt"/>
			</span>
			<span class="s_btn_wr">
				<input type="submit" value='<%=PlatformLocalesJSTL.getBundleValueforSearch("search.button.text")%>' id="miniSu" class="s_btn" onclick="miniSearchAction('<%=fullTextSearchPath%>');"/> <br/><br/><br/><br/><br/>
			                                                        <%--搜索--%>
			</span>
</form>