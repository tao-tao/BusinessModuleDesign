<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>


<div id="comprehensiveTabControl" class="easyui-tabs" data-options="fit:true">
		<div title="角色" id = "roleTab" data-options="iconCls:'icon-role',fit:true">
			<div id="RoleToolbar" class="datagrid-toolbar" style="display: block;">
				<table class="tableForm" id="roleSearchForm" width='100%'>
					<tr>
						<td width=""><input id="roleQueryText"></input></td>
					</tr>
				</table>
			</div>
			<table id="roleList"></table>
		</div>
		
		<div title="用户" id = "userTab" data-options="iconCls:'icon-user',fit:true">
			<div id="toolbarUser" class="datagrid-toolbar" style="display: block;">
				<table class="tableForm" id="userSearchForm" width='100%'>
					<tr>
						<sec:accesscontrollist hasPermission="3"><td width=""><input id="userQueryText"></input></td></sec:accesscontrollist>
						<sec:accesscontrollist hasPermission="3"><td align="right" ><a class="easyui-linkbutton" iconCls='icon-org-dept' plain=true  onclick="authSelectDept();" style="width:80px;" href="javascript:void(0);">选择部门</a><input type="hidden" id="authSelectedDeptId"/></td></sec:accesscontrollist>
					</tr>
				</table>
			</div>
			<table id="dgUser" class="easyui-datagrid"
							data-options="
								fit: true,
								border: false,
								rownumbers: true,
								animate: true,
								collapsible: false,
								fitColumns: true,
								autoRowHeight: false,
								toolbar:'#toolbarUser',
								idField :'ID',
								singleSelect: true,
								checkOnSelect:true,
								pagination:true,
								pageSize:dataOptions.pageSize,
								pageList:dataOptions.pageList,
								striped:true,
								url:'platform/sysdept/sysDeptController/getUserDataByPage.json?_status=1&id=${param.id}&type=${param.type}'">
							<thead>
								<tr>
									<th data-options="field:'ID', halign:'center',hidden:'true',checkbox:true,fit:true" width="220">id</th>
									<th data-options="field:'NAME',required:true,align:'center',fit:true" editor="{type:'text'}" width="220">姓名</th>
									<th data-options="field:'LOGIN_NAME',align:'center',align:'center',fit:true" editor="{type:'text'}"  width="220">登录名</th>
									<th data-options="field:'SEX_NAME',align:'center',align:'center',fit:true" editor="{type:'text'}"  width="220">性别</th>
									<th data-options="field:'MOBILE',align:'center',align:'center',fit:true,hidden:true" editor="{type:'text'}"  width="220">手机</th>
									<th data-options="field:'DEPT_NAME',align:'center',align:'center',fit:true" editor="{type:'text'}"  width="220">部门</th>
								</tr>
							</thead>
					</table>
		</div>
		<div title="部门"  id = "deptTab" data-options="iconCls:'icon-dept',fit:true">
			<div id="deptToolbar" class="datagrid-toolbar" style="display: block;">
				<table class="tableForm" id="deptSearchForm" width='100%'>
					<tr>
						<td width=""><input id="deptQueryText"></input></td>
					</tr>
				</table>
			</div>
			<ul id="orgTree_dept" style="width: auto; overflow: auto;"></ul>
		</div>
		<div title="群组"  id = "groupTab" data-options="iconCls:'icon-group',fit:true">
			<div id="groupToolbar" class="datagrid-toolbar" style="display: block;">
				<table class="tableForm" id="GroupSearchForm" width='100%'>
					<tr>
						<td width=""><input id="groupQueryText"></input></td>
					</tr>
				</table>
			</div>
			<table id="groupList" ></table>
			
			
			
			
		</div>
		<div title="岗位"  id = "positionTab" data-options="iconCls:'icon-position',fit:true">
			<div id="positionToolbar" class="datagrid-toolbar" style="display: block;">
				<table class="tableForm" id="positionSearchForm" width='100%'>
					<tr>
						<td width=""><input id="positionQueryText"></input></td>
					</tr>
				</table>
			</div>
			<table id="positionList" ></table>
		</div>
		</div>