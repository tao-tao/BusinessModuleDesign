<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>添加日历</title>
<base href="<%=ViewUtil.getRequestPath(request)%>">
<jsp:include page="/avicit/platform6/component/common/EasyUIJsInclude.jsp"></jsp:include>
<link rel="stylesheet" type="text/css" href="avicit/platform6/modules/mdm/css/style.css">
</head>
<body class="easyui-layout" fit="true">
<div region="center" border="false">
   <form id="formCalendar" fit="true">
		<table class="table"  width="100%" height="80px" border="0" align="center">
			<tr>
				<th nowrap><span style="color:red">*</span>名称: </th>
				<td><input id="name" name="name" class="easyui-validatebox" style="width: 300px;"/></td>
			</tr>
			<tr>
				<td width="50px" align="center">描述: </td>
				<td><textarea id="description" name="description" style="width: 300px;height: 60px"  rows="" cols="" ></textarea></td>
			</tr>
		</table>
	</form>
</div>
</body>
</html>
