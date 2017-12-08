<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>多应用管理</title>
<base href="<%=ViewUtil.getRequestPath(request)%>">
<jsp:include page="/avicit/platform6/component/common/EasyUIJsInclude.jsp"></jsp:include>
</head>
<body class="easyui-layout" fit="true">
	<div region="center" border="false" style="height:0px;padding:0px;overflow:hidden;">
		<div id="toolbar" class="datagrid-toolbar" style="height:auto;display: block;">
			<fieldset>
				<legend>查询条件</legend>
					<form id="searchForm">
						<table class="tableForm" id="searchForm" width='100%'>
							<tr>
								<td align="right">应用名称：</td>
								<td><div><input name="filter-like-APPLICATION_NAME" id="filter-like-APPLICATION_NAME" class="easyui-validatebox" editable="false" style="width:99%;"/></div>
								</td>
								<td align="right">应用编码：</td>
								<td><div><input name="filter-like-APPLICATION_CODE" id="filter-like-APPLICATION_CODE" class="easyui-validatebox" editable="false" style="width:99%"/></div>
								</td>
							</tr>
							<tr>
								<td colspan="2">
								 <div style="float: right;"><a class="easyui-linkbutton" style="width: 60px;" iconCls="icon-search" plain="true" onclick="sysApp.search();" href="javascript:void(0);">查询</a></div>
								</td>
								<td colspan="2">
								<div><a class="easyui-linkbutton" style="width: 60px;" iconCls="icon-cancel" plain="true" onclick="sysApp.reset();" href="javascript:void(0);">清空</a></div>
								</td>
							</tr>
						</table>
					</form>
			</fieldset>
		</div>
		<div id="toolbarSysApp">
			<td><a class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="sysApp.insert();" href="javascript:void(0);">添加</a></td>
			<td><a class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="sysApp.edit();" href="javascript:void(0);">编辑</a></td>
			<td><a class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="sysApp.save();" href="javascript:void(0);">保存</a></td>
			<td><a class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="sysApp.del();" href="javascript:void(0);">删除</a></td>
		</div>
		<table id="sysAppList"
			data-options=" 
						fit: true,
						border: false,
						rownumbers: true,
						animate: true,
						collapsible: false,
						fitColumns: true,
						autoRowHeight: false,
						toolbar:'#toolbarSysApp',
						idField :'id',
						singleSelect: true,
						checkOnSelect: true,
						selectOnCheck: false,
						striped:true">
			<thead>
				<tr>
					<th data-options="field:'id', halign:'center',checkbox:true" width="220">id</th>
					<th data-options="field:'applicationName',required:true,align:'center'" editor="{type:'validatebox',options:{required:true}}" width="120"><font color="red">*</font>应用名称</th>
					<th data-options="field:'applicationCode',required:true,align:'center'" editor="{type:'validatebox',options:{required:true}}"><font color="red">*</font>应用编码</th>
					<th data-options="field:'basepath',align:'left'" editor="{type:'text'}" width="120">应用地址</th>
					<th data-options="field:'runState',formatter:format,align:'center',align:'center'" editor="{type:'combobox',options:{panelHeight:'auto',editable:false,valueField:'lookupCode',textField:'lookupName'}}"  width="60">状态</th>
					<th data-options="field:'description',align:'center',align:'left'" editor="{type:'text'}"  width="220">描述</th>
				</tr>
			</thead>
		</table>
	</div>
	<script src="avicit/platform6/modules/system/sysapplication/js/SysApplication.js" type="text/javascript"></script>
	<script type="text/javascript">
		var sysApp;
		$(function(){
			sysApp= new SysApplication('${dId}','sysAppList','searchForm','${url}');
		});
		function format(value){
			return sysApp.format(value);
		}
	</script>
</body>
</html>