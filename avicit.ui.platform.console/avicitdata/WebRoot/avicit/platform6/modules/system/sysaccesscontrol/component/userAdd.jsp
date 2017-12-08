<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>集中授权管理</title>
		<base href="<%=ViewUtil.getRequestPath(request) %>">
		<jsp:include page="/avicit/platform6/component/common/EasyUIJsInclude.jsp"></jsp:include>
		<jsp:include page="/avicit/platform6/modules/system/commonpopup/commonSelectionHead.jsp"></jsp:include>
		<script src="static/js/platform/component/common/exteasyui.js" type="text/javascript"></script>
		<script type="text/javascript">
		$(function(){
			$('#userQueryText')
			.searchbox(
					{
						width : 200,
						searcher : function(
								value) {
							if (value == "请输入查询内容") {
								value = "";
							}
							  if(value.length==0){
								  functionAction = true ;
							  }else{
								  functionAction = false ;
							  }
							  searchData() ;
						},
						prompt : "请输入查询内容"
					});
		});
		
		function searchData(){
			$("#dgUser").datagrid("options").url="platform/centralizedAuthorizationController/getUserDataByPage.json";
			var type = "" ;
			var depeId="";
			if(depeId!= null && depeId.length>0){
				type="dept";
			}
			var queryKeyWord = $('#userQueryText').searchbox("getValue");
			var searchData = {
					search_USER_NAME:queryKeyWord,
					search_LOGIN_NAME:queryKeyWord
			} ;
			searchData.id=depeId;
			searchData.type=type;
			$('#dgUser').datagrid('load',searchData);
			$("#dgUser").datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
		}
		
		function saveUser(){
			var rows = $('#dgUser').datagrid('getChecked');
			var ids = [];
			var l =rows.length;
		  	if(l > 0){
			  $.messager.confirm('请确认','您确定要授权当前所选的用户？',function(b){
				 if(b){
					 for(;l--;){
						 ids.push(rows[l].ID);
					 }
					 $.ajax({
						 url:'platform/sysAccessControlController/insertComponentResourceAuth/U/'+parent.curComId+'.json',
						 data:	JSON.stringify(ids),
						 contentType : 'application/json',
						 type : 'post',
						 dataType : 'json',
						 success : function(r){
							 if (r.flag == "success") {
								 parent.reloadTabData(parent.currTabIndex);
								 parent.$closeAddUserDialog();
								 $.messager.show({
									 title : '提示',
									 msg : '操作成功！'
								});
							}else{
								$.messager.show({
									 title : '提示',
									 msg : r.msg
								});
							}
						 }
					 });
				 } 
			  });
		  	}else{
			  $.messager.alert('提示','请选择要删除的记录！','warning');
		  	}
		}
		function backWin(){
			window.parent.$("#insertUserDialog").dialog('close');
		}
		</script>
	</head>
<body class="easyui-layout" fit="true">
<div region="center">
<div id="toolbarTableMap" class="datagrid-toolbar">
		 	<table>
		 		<tr>
						<td><input id="userQueryText"><sec:accesscontrollist hasPermission="3"></input></sec:accesscontrollist></td>
					</tr>
		 	</table>
	 	</div>
	 	<table id="dgUser" class="easyui-datagrid" datapermission="sysSyncTableMapgrid"
							data-options=" 
								fit: true,
								border: false,
								rownumbers: true,
								animate: true,
								collapsible: false,
								fitColumns: true,
								autoRowHeight: false,
								toolbar:'#toolbarTableMap',
								idField :'ID',
								queryParams : {id:'',type:''},
								singleSelect: true,
								checkOnSelect: true,
								selectOnCheck: false,
								pagination:true,
								pageSize:dataOptions.pageSize,
								pageList:dataOptions.pageList,
								striped:true,
								url:'platform/sysdept/sysDeptController/getUserDataByPage.json'">
							<thead>
								<tr>
									<th data-options="field:'ID', halign:'center',checkbox:true,fit:true" width="220">id</th>
									<th data-options="field:'NAME',required:true,align:'center',fit:true" editor="{type:'text'}" width="220">姓名</th>
									<th data-options="field:'LOGIN_NAME',align:'center',align:'center',fit:true" editor="{type:'text'}"  width="220">登录名</th>
									<th data-options="field:'SEX_NAME',align:'center',align:'center',fit:true" editor="{type:'text'}"  width="220">性别</th>
									<th data-options="field:'MOBILE',align:'center',align:'center',fit:true,hidden:true" editor="{type:'text'}"  width="220">手机</th>
									<th data-options="field:'DEPT_NAME',align:'center',align:'center',fit:true" editor="{type:'text'}"  width="220">部门</th>
								</tr>
							</thead>
		</table>
		</div>
	<div region="south" class="datagrid-toolbar">
		<table width="100%">
			<tr>
				<td style="float:right">
					<a class="easyui-linkbutton" onclick="saveUser()" href="javascript:void(0);">确定</a>
					<a class="easyui-linkbutton" onclick="backWin();" href="javascript:void(0);">返回</a>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>