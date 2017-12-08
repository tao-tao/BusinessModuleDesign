<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>

<script type="text/javascript" charset="utf-8">
	$(function() {
			$('#componentsDataGrid').datagrid({
				url:'platform/componentManagerController/findComponentResources.json',
			    rownumbers: true,
			    animate: false,
			    striped: true,
			    collapsible: false,
			    autoRowHeight: false,
			    singleSelect: true,
			    checkOnSelect:true,
			    selectOnCheck:false,
				iconcls: 'icon-save',
				//pagination: true,
				//pageSize: 10,
				//pageList: [10,20,30,40,50],
				fit: true,
				fitColumns: true,
				nowarp: true,
				border: false,
				idField: 'ID',
				frozenColumns:[[
				      {field:'ck',checkbox:true}
				 ]],
				toolbar: '#dataGridtoolbar',
				columns: [[
				           {
				        	   field:'ID', 
				        	   halign:'center',
				        	   hidden:true
				        	   ,fit:true
				           },
				           {
				        	   field:'ACCESSCONTROLID', 
				        	   halign:'center',
				        	   hidden:true,
				        	   width:  140,
				        	   editor :"{type:'text'}"
				        	   ,fit:true
				           },
				           {
				        	   title: '组件名称',
				        	   halign:'center',
				        	   field: 'KEY',
				        	   width:  50,
				        	   editor :"{type:'text'}"
				        	   ,fit:true
				           },
				           {
				        	   title: '组件标识 ',
				        	   halign:'center',
			                   field: 'VALUE',
			                   width: 260,
			                   sortable: true,
			                   editor :"{type:'text'}"
			                  ,fit:true
				           },
				           { 
				        	   title: '访问',
				        	   field: 'READGRANTED',
				        	   halign:'center',
				        	   width: 30,
				        	   formatter: comReadAuthFormat 
				        	   ,fit:true
				           },
				           {
				        		title:'编辑',
				        		field: 'WRITEGRANTED',
				        		halign:'center',
				        		width: 30,
				        		formatter: comWriteAuthFormat
				        		,fit:true
				           }
				         ]],
				         onClickCell:clickComponentCell
			});
		initComponentSearch();
	});
</script>

<div id="dataGridtoolbar" class="datagrid-toolbar" style="display: block;">
	<table class="toolbarTable">
		<tr>
     	  <sec:accesscontrollist hasPermission="3"><td >
				<a href="javascript:void(0);" class='easyui-menubutton' data-options="menu:'#compontOperator',iconCls:'icon-all-file'" >批量操作</a>
				<div id="compontOperator" style="width:150px;">
				<div id='readOkWriteOk' iconCls="icon-add" onclick="setComponentAuth('B','1','1',null);">访问允许，编辑允许</div>
				<div id='readOkWriteNot' iconCls="icon-all-file" onclick="setComponentAuth('B','1','0',null);"> 访问允许，编辑禁止</div>
				<div id='readNotWriteNot' iconCls="icon-all-file" onclick="setComponentAuth('B','0','0',null);"> 访问禁止，编辑禁止</div>
				<div id='compontBathchRemove' iconCls="icon-no" onclick="setComponentAuth('B','del','del',null);">批量移除</div>
			     </div>
			</td></sec:accesscontrollist>
			<sec:accesscontrollist hasPermission="3" domainObject="system_acl_add"><td style="display:none;"><a class="easyui-linkbutton"  iconCls="icon-add" plain="true" onclick="addData();" href="javascript:void(0);">添加</a></td></sec:accesscontrollist>
			<sec:accesscontrollist hasPermission="3" domainObject="system_acl_delete"><td><a class="easyui-linkbutton"  iconCls="icon-no" plain="true" onclick="deleteComponentResources();" href="javascript:void(0);">删除</a></td></sec:accesscontrollist>
			<sec:accesscontrollist hasPermission="3" domainObject="system_acl_refreshComnt"><td><a class="easyui-linkbutton" iconCls="icon-mini-refresh" plain="true" onclick="refreshComponent();" href="javascript:void(0);">重载页面组件</a></td></sec:accesscontrollist>
			<sec:accesscontrollist hasPermission="3" domainObject="system_acl_deleteAuth"><td><a class="easyui-linkbutton"  iconCls="icon-reload" plain="true" onclick="refreshAuthority();" href="javascript:void(0);">刷新组件授权</a></td></sec:accesscontrollist>
		</tr>
		<tr align="left" style="border:1px solid gray">
			<td  colspan="5"><input id="searchKeyWord" name="searchKeyWord"/></td>
		</tr>
	</table>
</div> 

<table id="componentsDataGrid"></table>