<%@page import="java.lang.reflect.Modifier"%>
<%@page import="java.lang.reflect.Method"%>
<%@page import="org.springframework.util.ReflectionUtils"%>
<%@page import="java.util.Date"%>
<%@page import="avicit.platform6.api.session.SessionHelper"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Enumeration"%>
<%@page import="java.util.Map"%>
<%@page import="org.apache.commons.beanutils.BeanUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Current Session Info</title>
</head>
<%
	Map<String, Object> map1 = BeanUtils.describe(session);

	map1.put("lastAccessedTime1", new Date(session.getLastAccessedTime()));
	map1.put("creationTime1", new Date(session.getCreationTime()));

	Enumeration<?> e = session.getAttributeNames();
	Map<String, Object> mapAttribute = new HashMap<String, Object>();
	while (e.hasMoreElements()) {
		String name = (String) e.nextElement();
		Object value = session.getAttribute(name);
		mapAttribute.put(name, value);
	}

	Map<String, Object> map2 = new HashMap<String, Object>();

	Method[] methods = ReflectionUtils.getAllDeclaredMethods(SessionHelper.class);
	for (Method m : methods) {
		String methodName = m.getName();
		if (m.getName().startsWith("get") && !m.getName().equals("getClass")) {
			Object value = ReflectionUtils.invokeMethod(m, null);
			map2.put(methodName, value);

		}
	}
	/*
	map2.put("getApplicationId", SessionHelper.getApplicationId());
	map2.put("getClientIp", SessionHelper.getClientIp());
	map2.put("getCurrentLanguageCode", SessionHelper.getCurrentLanguageCode());
	map2.put("getLoginName", SessionHelper.getLoginName());
	map2.put("getLoginSysUser", SessionHelper.getLoginSysUser());
	map2.put("getLoginSysUserId", SessionHelper.getLoginSysUserId());
	map2.put("getRequest", SessionHelper.getRequest());
	map2.put("getSystemDefaultLanguageCode", SessionHelper.getSystemDefaultLanguageCode());
	 */
%>
<body>
	<table width="100%" border="1">
		<caption>Current Session Info</caption>
		<tr>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
		<%
			for (Iterator<?> it = map1.keySet().iterator(); it.hasNext();) {
				Object key = it.next();
				Object value = map1.get(key);
		%>
		<tr>
			<td>&nbsp;<%=key%></td>
			<td>&nbsp;<%=value%></td>
		</tr>
		<%
			}
		%>
		<tr>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
	</table>
	<p />
	<table width="100%" border="1">
		<caption>Current Session Attributes</caption>
		<tr>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
		<%
			for (Iterator<?> it = mapAttribute.keySet().iterator(); it.hasNext();) {
				Object key = it.next();
				Object value = mapAttribute.get(key);
		%>
		<tr>
			<td>&nbsp;<%=key%></td>
			<td>&nbsp;<%=value%></td>
		</tr>
		<%
			}
		%>
		<tr>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
		<%
			for (Iterator<?> it = map2.keySet().iterator(); it.hasNext();) {
				Object key = it.next();
				Object value = map2.get(key);
		%>
		<tr>
			<td>&nbsp;<%=key%></td>
			<td>&nbsp;<%=value%></td>
		</tr>
		<%
			}
		%>
	</table>
</body>
</html>