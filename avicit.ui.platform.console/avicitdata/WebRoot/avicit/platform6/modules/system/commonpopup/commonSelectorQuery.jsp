<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<%@ taglib prefix="sec" uri="/WEB-INF/tags/shiro.tld" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>通用查询</title>
<base href="<%=ViewUtil.getRequestPath(request) %>">
<jsp:include page="/avicit/platform6/component/common/EasyUIJsInclude.jsp"></jsp:include>
<jsp:include page="/avicit/platform6/modules/system/commonpopup/commonSelectionHead.jsp"></jsp:include>
<script type="text/javascript">
	/**
	 * 初始化当前页面
	 */
	$(function() {
		var selectType = '<%=request.getParameter("selectType")%>';
		if(selectType=='role'){
			document.getElementById("RoleQueryForm").style.display="block";
			document.getElementById("GroupQueryForm").style.display="none";
			document.getElementById("PositionQueryForm").style.display="none";
		}else if(selectType=='group'){
			document.getElementById("RoleQueryForm").style.display="none";
			document.getElementById("GroupQueryForm").style.display="block";
			document.getElementById("PositionQueryForm").style.display="none";
		}if(selectType=='position'){
			document.getElementById("RoleQueryForm").style.display="none";
			document.getElementById("GroupQueryForm").style.display="none";
			document.getElementById("PositionQueryForm").style.display="block";
		}
	});
  //清空角色查询条件
  function clearRoleCondition(){
		$('#RoleQueryForm input').val('');
  }
 //清空群组查询条件
  function clearGroupCondition(){
	  $('#GroupQueryForm input').val('');
  }
 //清空岗位查询条件
  function clearPositionCondition(){
	  $('#PositionQueryForm input').val('');
  }
</script>
</head>
<body class="easyui-layout" fit="true">
	<div region="center" border="false" style="overflow: hidden;">
		<form id="RoleQueryForm">
			<table class="tableForm">
			   <tr><td></td></tr>
			    <tr><td></td></tr>
			    <tr><td></td></tr>
			    <tr><td></td></tr>
			     <tr><td></td></tr>
			    <tr><td></td></tr>
			    <tr><td></td></tr>
				<tr>
					<td>角色名称</td>
					<td><input class="easyui-validatebox"
						name="filter_LIKE_roleName" id="filter_LIKE_roleName" /></input></td>
					<td>角色描述</td>
					<td><input class="easyui-validatebox" name="filter_LIKE_desc"
						id="filter_LIKE_desc" /></td>
				</tr>
			</table>
		</form>
		<form id="GroupQueryForm">
			<table class="tableForm">
			    <tr><td></td></tr>
			    <tr><td></td></tr>
			    <tr><td></td></tr>
			    <tr><td></td></tr>
			     <tr><td></td></tr>
			    <tr><td></td></tr>
			    <tr><td></td></tr>
				<tr>
					<td>群组名称</td>
					<td><input class="easyui-validatebox"
						name="filter_LIKE_groupName" id="filter_LIKE_groupName" /></input></td>
					<td>群组描述</td>
					<td><input class="easyui-validatebox" name="filter_LIKE_groupDesc"
						id="filter_LIKE_groupDesc" /></td>
				</tr>
			</table>
		</form>
		<form id="PositionQueryForm">
			<table class="tableForm">
			    <tr><td></td></tr>
			    <tr><td></td></tr>
			    <tr><td></td></tr>
			    <tr><td></td></tr>
			     <tr><td></td></tr>
			    <tr><td></td></tr>
			    <tr><td></td></tr>
				<tr>
					<td>岗位名称</td>
					<td><input class="easyui-validatebox"
						name="filter_LIKE_positionName" id="filter_LIKE_positionName" /></input></td>
					<td>岗位描述</td>
					<td><input class="easyui-validatebox" name="filter_LIKE_positionDesc"
						id="filter_LIKE_positionDesc" /></td>
				</tr>
			</table>
		</form>
	</div>

</body>
</html>