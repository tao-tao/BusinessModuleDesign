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
			$('#roleQueryText').searchbox({
				width : 200,
				searcher : function(value) {
					if (value == "请输入查询内容") {
						value = "";
					}
					searchRoleFun(value);
					//停止datagrid的编辑.
				},
				prompt : "请输入查询内容"
			});
		});
		
		function searchRoleFun(queryKeyWord){
			if(queryKeyWord==null){
				$.messager.alert("操作提示", "请输入查询条件！","info");
				return ;
		    }
			var queryParams = $('#dgRole').datagrid('options').queryParams;  
		    queryParams.roleQueryKey =queryKeyWord;
		    $('#dgRole').datagrid('options').queryParams=queryParams;        
		    $("#dgRole").datagrid('load'); 
		}
		
		function saveUser(){
			var rows = $('#dgRole').datagrid('getChecked');
			var ids = [];
			var l =rows.length;
		  	if(l > 0){
			  $.messager.confirm('请确认','您确定要授权当前所选的角色？',function(b){
				 if(b){
					 for(;l--;){
						 ids.push(rows[l].id);
					 }
					 $.ajax({
						 url:'platform/sysAccessControlController/insertComponentResourceAuth/R/'+parent.curComId+'.json',
						 data:	JSON.stringify(ids),
						 contentType : 'application/json',
						 type : 'post',
						 dataType : 'json',
						 success : function(r){
							 if (r.flag == "success") {
								 parent.reloadTabData(parent.currTabIndex);
								 parent.$closeAddRoleDialog();
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
			window.parent.$("#dept").dialog('close');
		}
		</script>
	</head>
<body class="easyui-layout" fit="true">
<div region="center">
<div id="toolbarTableMap" class="datagrid-toolbar">
		 	<table>
		 		<tr>
						<td><input id="roleQueryText"><sec:accesscontrollist hasPermission="3"></input></sec:accesscontrollist></td>
					</tr>
		 	</table>
	 	</div>
	 	<table id="dgRole" class="easyui-treegrid" datapermission="sysSyncTableMapgrid"
							data-options=" 
								rownumbers: true,
								animate: false,
								collapsible: false,
								autoRowHeight: false,
								height: 400,
								url: 'platform/centralizedAuthorizationController/getOrgDepts.json?multipleOrg=n&selectType=dept_Comprehensive',
								method: 'post',
								idField: 'ID',
								treeField: 'NAME',
							    fit: true,
								fitColumns: true,
								nowarp: true,
								border: false,
								toolbar: '#toolbarTableMap'">
							<thead>
								<tr>
									<th data-options="field:'ID', halign:'center',checkbox:true,fit:true" width="220">id</th>
									<th data-options="field:'NAME',required:true,align:'center',fit:true" editor="{type:'text'}" width="220">角色名称</th>
									
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