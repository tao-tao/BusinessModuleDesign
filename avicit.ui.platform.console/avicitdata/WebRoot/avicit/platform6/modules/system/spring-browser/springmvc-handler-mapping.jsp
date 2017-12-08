<%@page import="org.springframework.stereotype.Controller"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Map"%>
<%@page import="org.springframework.web.servlet.mvc.annotation.DefaultAnnotationHandlerMapping"%>
<%@page import="avicit.platform6.core.spring.SpringMVCFactory"%>
<%@page import="org.springframework.beans.factory.BeanFactoryUtils" %>
<%@page import="org.springframework.web.servlet.HandlerMapping" %>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	ApplicationContext context = SpringMVCFactory.getApplicationContext();
	Map<String, HandlerMapping> allRequestMappings = BeanFactoryUtils.beansOfTypeIncludingAncestors(context,HandlerMapping.class, true, false);

	System.out.println(allRequestMappings.size());

	int ddd = context.getBeanDefinitionCount();
	System.out.println(ddd);
	DefaultAnnotationHandlerMapping mo = SpringMVCFactory.getBean(DefaultAnnotationHandlerMapping.class);
	Map handlerMap = mo.getHandlerMap();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Spring MVC Handler Mapping</title>
</head>
<body>
	<h3>Spring MVC Handler Mapping for <%=context.getDisplayName() %></h3>
	<table width="100%" border="1px">
		<tr>
			<td>URL</td>
			<td>Object</td>
		</tr>
		<%
			for(Iterator it = handlerMap.entrySet().iterator(); it.hasNext();){
				Map.Entry entry = (Map.Entry)it.next();
				String s = (String)entry.getKey();
				Object o = entry.getValue();
				out.println("<tr>");
				out.println("<td>");
				out.println(s);
				out.println("</td>");
				out.println("<td>");
				out.println(o);
				out.println("</td>");
				out.println("</tr>");
			}
		%>
	</table>
</body>
</html>