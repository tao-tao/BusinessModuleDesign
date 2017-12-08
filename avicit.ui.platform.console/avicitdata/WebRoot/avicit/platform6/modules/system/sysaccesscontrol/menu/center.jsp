<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>


<div id="comprehensiveTabControl" class="easyui-tabs" data-options="fit:true">
		<div title="角色" id = "roleTab" data-options="iconCls:'icon-role',fit:true">
			<div id="RoleToolbar" class="datagrid-toolbar" style="display: block;">
				<table class="tableForm" id="roleSearchForm" width='100%'>
					<tr>
						<td >
							<input id="roleQueryText"></input>
							<a class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="insertRole();" href="javascript:void(0);">添加</a>
							
							<a href="javascript:void(0);" class='easyui-menubutton' data-options="menu:'#compontOperator1',iconCls:'icon-all-file'" >批量操作</a>
							<div id="compontOperator1">
								<div id='readOkWriteOk1' iconCls="icon-add" onclick="setComponentAuth('1','1',null);">访问允许，编辑允许</div>
								<div id='readOkWriteNot1' iconCls="icon-all-file" onclick="setComponentAuth('1','0',null);"> 访问允许，编辑禁止</div>
								<div id='readNotWriteNot1' iconCls="icon-all-file" onclick="setComponentAuth('0','0',null);"> 访问禁止，编辑禁止</div>
						    </div>
						    <a class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="delResources();" href="javascript:void(0);">删除</a>
						</td>
					</tr>
				</table>
			</div>
			<table id="roleList"></table>
		</div>
		
		<div title="用户" id = "userTab" data-options="iconCls:'icon-user',fit:true">
			<div id="toolbarUser" class="datagrid-toolbar" style="display: block;">
				<table class="tableForm" id="userSearchForm" width='100%'>
					<tr>
						<td>
							<input id="userQueryText"></input>
							<a class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="insertUser();" href="javascript:void(0);">添加</a>
							<input type="hidden" id="userId">
							<input type="hidden" id="userName">
							<a href="javascript:void(0);" class='easyui-menubutton' data-options="menu:'#compontOperator2',iconCls:'icon-all-file'" >批量操作</a>
							<div id="compontOperator2" style="width:150px;">
								<div id='readOkWriteOk2' iconCls="icon-add" onclick="setComponentAuth('1','1',null);">访问允许，编辑允许</div>
								<div id='readOkWriteNot2' iconCls="icon-all-file" onclick="setComponentAuth('1','0',null);"> 访问允许，编辑禁止</div>
								<div id='readNotWriteNot2' iconCls="icon-all-file" onclick="setComponentAuth('0','0',null);"> 访问禁止，编辑禁止</div>
						    </div>
						    <a class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="delResources();" href="javascript:void(0);">删除</a>
						</td>
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
								checkOnSelect: true,
								selectOnCheck: false,
								onClickCell:clickComponentCell,
								pagination:true,
								pageSize:dataOptions.pageSize,
								pageList:dataOptions.pageList,
								striped:true
								">
							<thead>
								<tr>
									<th data-options="field:'ID', halign:'center',checkbox:true,fit:true" width="220">id</th>
									<th data-options="field:'NAME',required:true,align:'center',fit:true" editor="{type:'text'}" width="220">姓名</th>
									<th data-options="field:'LOGIN_NAME',align:'center',align:'center',fit:true" editor="{type:'text'}"  width="220">登录名</th>
									<!-- <th data-options="field:'SEX_NAME',align:'center',align:'center',fit:true" editor="{type:'text'}"  width="220">性别</th>-->
									<th data-options="field:'MOBILE',align:'center',align:'center',fit:true,hidden:true" editor="{type:'text'}"  width="220">手机</th>
									<th data-options="field:'DEPT_NAME',align:'center',align:'center',fit:true" editor="{type:'text'}"  width="220">部门</th>
									<th data-options="field:'ACCESSIBILITY',align:'center',align:'center',fit:true,formatter:comReadAuthFormat" editor="{type:'text'}"  width="220">访问权限</th>
									<th data-options="field:'OPERABILITY',align:'center',align:'center',fit:true,formatter:comWriteAuthFormat" editor="{type:'text'}"  width="220">编辑权限</th>
								</tr>
							</thead>
					</table>
		</div>
		<div title="部门"  id = "deptTab" data-options="iconCls:'icon-dept',fit:true">
			<div id="deptToolbar" class="datagrid-toolbar" style="display: block;">
				<table class="tableForm" id="deptSearchForm" width='100%'>
					<tr>
						<td width="">
							<input id="deptQueryText"></input>
							<input id="deptId" style="display:none"></input>
							<input id="deptName" style="display:none"></input>
							<a class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="mySelectDept();" href="javascript:void(0);">添加</a>
							
							<a href="javascript:void(0);" class='easyui-menubutton' data-options="menu:'#compontOperator3',iconCls:'icon-all-file'" >批量操作</a>
							<div id="compontOperator3" style="width:150px;">
								<div id='readOkWriteOk3' iconCls="icon-add" onclick="setComponentAuth('1','1',null);">访问允许，编辑允许</div>
								<div id='readOkWriteNot3' iconCls="icon-all-file" onclick="setComponentAuth('1','0',null);"> 访问允许，编辑禁止</div>
								<div id='readNotWriteNot3' iconCls="icon-all-file" onclick="setComponentAuth('0','0',null);"> 访问禁止，编辑禁止</div>
						    </div>
						    <a class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="delResources();" href="javascript:void(0);">删除</a>
						</td>
					</tr>
				</table>
			</div>
			<ul id="orgTree_dept" style="width: auto; overflow: auto;"></ul>
		</div>
		<div title="群组"  id = "groupTab" data-options="iconCls:'icon-group',fit:true">
			<div id="groupToolbar" class="datagrid-toolbar" style="display: block;">
				<table class="tableForm" id="GroupSearchForm" width='100%'>
					<tr>
						<td width="">
							<input id="groupQueryText"></input>
							<a class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="insertGroup();" href="javascript:void(0);">添加</a>
							
							<a href="javascript:void(0);" class='easyui-menubutton' data-options="menu:'#compontOperator4',iconCls:'icon-all-file'" >批量操作</a>
							<div id="compontOperator4" style="width:150px;">
								<div id='readOkWriteOk4' iconCls="icon-add" onclick="setComponentAuth('1','1',null);">访问允许，编辑允许</div>
								<div id='readOkWriteNot4' iconCls="icon-all-file" onclick="setComponentAuth('1','0',null);"> 访问允许，编辑禁止</div>
								<div id='readNotWriteNot4' iconCls="icon-all-file" onclick="setComponentAuth('0','0',null);"> 访问禁止，编辑禁止</div>
						    </div>
						    <a class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="delResources();" href="javascript:void(0);">删除</a>
						</td>
					</tr>
				</table>
			</div>
			<table id="groupList" ></table>
			
			
			
			
		</div>
		<div title="岗位"  id = "positionTab" data-options="iconCls:'icon-position',fit:true">
			<div id="positionToolbar" class="datagrid-toolbar" style="display: block;">
				<table class="tableForm" id="positionSearchForm" width='100%'>
					<tr>
						<td width="">
							<input id="positionQueryText"></input>
							<a class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="insertPosition();" href="javascript:void(0);">添加</a>
							
							<a href="javascript:void(0);" class='easyui-menubutton' data-options="menu:'#compontOperator5',iconCls:'icon-all-file'" >批量操作</a>
							<div id="compontOperator5" style="width:150px;">
								<div id='readOkWriteOk5' iconCls="icon-add" onclick="setComponentAuth('1','1',null);">访问允许，编辑允许</div>
								<div id='readOkWriteNot5' iconCls="icon-all-file" onclick="setComponentAuth('1','0',null);"> 访问允许，编辑禁止</div>
								<div id='readNotWriteNot5' iconCls="icon-all-file" onclick="setComponentAuth('0','0',null);"> 访问禁止，编辑禁止</div>
						    </div>
						    <a class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="delResources();" href="javascript:void(0);">删除</a>
						</td>
					</tr>
				</table>
			</div>
			<table id="positionList" ></table>
		</div>
		</div>