<?xml version="1.0" encoding="UTF-8" ?>
<%@page import="org.springframework.web.servlet.mvc.annotation.DefaultAnnotationHandlerMapping"%>
<%@page import="java.util.Date"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="avicit.platform6.core.spring.SpringMVCFactory"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="avicit.platform6.core.spring.SpringFactory"%>
<%@page import="com.alibaba.druid.pool.DruidDataSource" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="org.springframework.beans.factory.config.BeanDefinition"%>
<%@page import="java.util.Arrays"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
	java.util.Map<String, BeanDefinition> beanMap = null;
	ApplicationContext appContext = null;
	/* 检测是否是Spring MVC的 bean 工厂 */
	String isSpringMVCFactory = request.getParameter("isSpringMVC");
	if (StringUtils.isNotBlank(isSpringMVCFactory)) {
		beanMap = SpringMVCFactory.getBeanDefinitions();
		appContext = SpringMVCFactory.getApplicationContext();
	} else {
		beanMap = SpringFactory.getBeanDefinitions();
		appContext = SpringFactory.getApplicationContext();
	}

	/* 读取数据库连接池的最大连接数和当前活动连接数 */
	DruidDataSource basicDataSource = SpringFactory.getBean("defaultDataSource");
	int maxActive =basicDataSource.getMaxActive();
	int curActive = basicDataSource.getActiveCount();
	
	
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Spring Beans Browser</title>
</head>
<body>
	<center>
		<h3>
			Spring 容器中 Bean 定义信息 (共
			<%=beanMap.size()%>
			个Bean)
		</h3>
		<center>
			<%
				out.println("数据库连接池最大连接数量：" + maxActive + "；当前活动的数量：" + curActive);
			%>
		</center>
		<table width="100%" border="1px">
			<tr>
				<td>Spring ApplicationContext ID</td>
				<td>Spring ApplicationContext DisplayName</td>
				<td>Spring ApplicationContext Parent</td>
				<td>Spring ApplicationContext StartDate</td>
				<td>ClassName</td>
			</tr>
			<tr>
				<td><%=appContext.getId()%></td>
				<td><%=appContext.getDisplayName()%></td>
				<td><%=appContext.getParent()%></td>
				<td><%=new Date(appContext.getStartupDate())%></td>
				<td><%=appContext.getClass().getName()%></td>
			</tr>
		</table>
	</center>
	<%--		<div><button id="refreshSpringFactory" title="关闭当前的Spring 容器，重新启动Spring 容器" onclick="javascript:refreshSpringFactory();">重新启动Spring容器</button></div>--%>

	<table border="1px">
		<tr>
			<td width="10%">ID</td>
			<td>class</td>
			<td>scope</td>
			<td nowrap="nowrap">配置是否正确?</td>
			<td>configLocation</td>
		</tr>
		<%
			Object[] beanNames = beanMap.keySet().toArray();
			Arrays.sort(beanNames);
			for (Object o : beanNames) {
				String beanName = (String) o;
				BeanDefinition beanDef = beanMap.get(beanName);

				out.println("<tr>");

				out.println("<td><a href=\"spring-bean-properties.jsp?beanName=" + beanName + "\">" + beanName + "</a></td>");
				out.println("<td>" + beanDef.getBeanClassName() + "</td>");
				out.println("<td>" + beanDef.getScope() + "</td>");

				//bean配置是否正确？ Bean名称为*Action的bean必须定义为原型模式
				boolean isCorrect = beanName.indexOf("Action") > 0 ? beanDef.getScope().equalsIgnoreCase("prototype") : true;
				if (isCorrect) {
					out.println("<td>" + isCorrect + "</td>");
				} else {
					out.println("<td style=\"background-color: red;\"><strong>" + isCorrect + "</strong></td>");
				}
				out.println("<td>" + beanDef.getResourceDescription() + "</td>");

				out.println("</tr>");
			}
		%>
	</table>
</body>
</html>