<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>

<div id="treeGridToolbar" class="datagrid-toolbar" fit="true" style="height:auto;display: block;">
	<table class="toolbarTable" id="searchForm" width='100%'>
		<tr>
           <sec:accesscontrollist hasPermission="3"><td width="150px;">
				<a href="javascript:void(0);" class='easyui-menubutton' data-options="menu:'#menuOperator',iconCls:'icon-all-file'">批量操作</a>
				<div id="menuOperator" style="width:150px;">
					<div id='batchOk'     iconCls="icon-add"    onclick="changeBatchMenuAuth(1);">批量授权</div>
					<div id='batchFotbid' iconCls="icon-remove" onclick="changeBatchMenuAuth(0);">批量禁止</div>
					<div id='batchRemove' iconCls="icon-no"     onclick="changeBatchMenuAuth('del');">批量移除</div>
				</div>
			</td></sec:accesscontrollist>
			<sec:accesscontrollist hasPermission="3"><td align="right" width="100px;"><a class="easyui-linkbutton" iconCls="icon-reload" plain="true" onclick="refreshMenuSecurityCache();" style="width:120px;" href="javascript:void(0);">刷新授权缓存</a></td></sec:accesscontrollist>
		</tr>
		<!-- <tr align="left" style="border:1px solid gray">
			<td width="80px;"><input id="formSearch" name="search_NAME"></input></td>
			<td width="100px;"></td>
		</tr> -->
	</table>
</div>
<table id="tg" class="easyui-treegrid" 
		data-options="
			rownumbers: true,
			animate: false,
			collapsible: false,
			autoRowHeight: false,
			height: 400,
			url: 'platform/componentManagerController/getSysMenusByParentId.json',
			method: 'post',
			idField: 'ID',
			treeField: 'NAME',
		    fit: true,
			fitColumns: true,
			nowarp: true,
			border: false,
			toolbar: '#treeGridToolbar',
			onLoadSuccess: selectOneRow,
			onClickRow: onClickRow,
			columns: [[
		                   {
				        	   title: '名称',
				        	   field: 'NAME',
				        	   width:  150
				           },
				           { 
				        	   title: '访问权限',
				        	   field: 'REMARK',
				        	   formatter: remarkformat,
				        	   width: 40
				           }
				     ]]
		">
</table>