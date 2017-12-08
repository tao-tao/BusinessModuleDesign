<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@page import="avicit.platform6.core.api.system.syslocale.PlatformLocalesJSTL"%>

<!-- 本页无功能性代码； 加个脚注，纯属装饰，不然下面显得太空了 -->
<div id="foot" style="position: absolute; bottom: 0">     
	&copy; 2013 @avicit&nbsp;<span><%=PlatformLocalesJSTL.getBundleValueforSearch("search.accessory.searchFoot")%></span>
	<%--此内容系平台根据您的指令自动搜索的结果，搜索数据依赖于索引的同步与更新，并且依据搜索人的密级和权限等做了过滤。 --%>
</div>