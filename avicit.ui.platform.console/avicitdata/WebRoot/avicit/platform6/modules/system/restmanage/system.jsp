<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>系统列表</title>
<base href="<%=ViewUtil.getRequestPath(request)%>">
<jsp:include page="/avicit/platform6/component/common/EasyUIJsInclude.jsp"></jsp:include>
<jsp:include page="/avicit/platform6/modules/system/commonpopup/commonSelectionHead.jsp"></jsp:include>
</head>

<body class="easyui-layout"  fit="true">
<div data-options="region:'north',split:true,title:''" style="height: 300px; padding:0px;">
	<table id="dg"  class="easyui-datagrid"  url="platform/restmanage/system/list.json"  fit="true" toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="true" singleSelect="true" striped="true">
		<thead>
			<tr>
				<th data-options="field:'id', halign:'center',checkbox:true" width="50">id</th>
				<th data-options="field:'systemName',required:true,align:'center'"  width="100">系统名称</th>
				<th data-options="field:'orgName',required:true,align:'center'"  width="100">所属单位</th>
				<th data-options="field:'systemDesc',required:true,align:'center'"  width="100">系统描述</th>
				<th data-options="field:'status',required:true,align:'center'"  width="100"  formatter="formatStatus">系统状态</th>
			</tr>
		</thead>
	</table>
	</div>
	<!-- CRUD工具栏 -->
	<div id="toolbar">
		<table>
		<tr>
		<td><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newSys()">添加</a></td> 
		<td><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editSys()">编辑</a></td>  
		<td><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="deleteSys()">删除</a></td> 
		<td><input id="q_systemName" name="systemName" class="easyui-textbox"    value="请输入系统名称..."  onfocus="{this.value='';this.style.color='#000'}"   style="color:#808080"></td> 
		<td><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="querySys()">查询</a></td> 
		</tr>
		</table>
	</div>
	
	<!-- CU表单 -->
	<div id="dlg" class="easyui-dialog" style="width: 400px; height: 280px; padding: 10px 20px" closed="true" buttons="#dlg-buttons">
		<div class="ftitle">系统信息</div>
		<form id="fm" method="post" novalidate>
			<div class="fitem">
				<label>系统名称:</label> <input name="systemName" class="easyui-textbox" required="true">
			</div>
			<div class="fitem">
				<label>所属单位:</label> 
				<select id="orgId" name="orgId" class="easyui-combobox"   data-options="valueField:'id', textField:'orgName', width:166,editable:false,panelHeight:'auto'"></select>
			</div>
			<div class="fitem">
				<label>系统描述:</label> <input name="systemDesc" class="easyui-textbox" required="true">
			</div>
			<div class="fitem">
				<label>有效标识:</label> 
				<select name="status" class="easyui-combobox" data-options="width:166,editable:false,panelHeight:'auto'">
								<option value="1">有效</option>
								<option value="0">无效</option>
				</select>
			</div>
		</form>
	</div>
	<div id="dlg-buttons">
		<a href="javascript:void(0)" class="easyui-linkbutton" onclick="saveSys()" >保存</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" onclick="javascript:$('#dlg').dialog('close')" >返回</a>
	</div>
	
	
	<div data-options="region:'center',split:true,title:''" style="padding:0px;">
	<table id="dguser"  class="easyui-datagrid"  url="platform/restmanage/user/list.json"  fit="true" toolbar="#toolbaruser" pagination="true" rownumbers="true" fitColumns="true" singleSelect="true" striped="true">
		<thead>
			<tr>
				<th data-options="field:'id', halign:'center',checkbox:true" width="50">id</th>
				<th data-options="field:'userName',required:true,align:'center'"  width="100">用户名</th>
				<th data-options="field:'baseKey',required:true,align:'center'"  width="100">BASHKEY</th>
				<th data-options="field:'status',required:true,align:'center'"  width="50"  formatter="formatStatus">状态</th>
			</tr>
		</thead>
	</table>
	</div>
	
	<!-- CRUD工具栏 -->
	<div id="toolbaruser">
		<table>
		<tr>
		<td><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newUser()">添加</a></td> 
		<td><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editUser()">编辑</a></td>
		<td><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="deleteUser()">删除</a></td>
		<td><input id="q_userName" name="userName" class="easyui-textbox"  value="请输入用户名称..."  onfocus="{this.value='';this.style.color='#000'}"  style="color:#808080"></td>
		<td><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="queryUser()">查询</a></td>
		</tr>
		</table>
	</div>
	
	<!-- CU表单 -->
	<div id="dlguser" class="easyui-dialog" style="width: 400px; height: 400px; padding: 10px 20px" closed="true" buttons="#dlguser-buttons">
		<div class="ftitle">用户信息</div>
		<form id="fmuser" method="post" novalidate>
			<div class="fitem">
				<label>用户名称:</label> <input name="userName" class="easyui-textbox" required="true">
			</div>
			<div class="fitem">
				<label>用户密码:</label> <input name="userPassword"  type="password" class="easyui-textbox" required="true">
			</div>
			<div class="fitem">
				<label>所属单位:</label> 
				<select id="orgIduser" name="orgId" class="easyui-combobox"   data-options="valueField:'id', textField:'orgName', width:166,editable:false,panelHeight:'auto'"></select>
			</div>
			<div class="fitem">
				<label>所属系统:</label> 
				<select id="systemIduser" name="systemId" class="easyui-combobox" data-options="valueField:'id', textField:'systemName', width:166,editable:false,panelHeight:'auto'">
				</select>
			</div>
			<div class="fitem">
				<label>是否授权:</label> 
				<select name="type" class="easyui-combobox" data-options="width:166,editable:false,panelHeight:'auto'">
								<option value="1">有效</option>
								<option value="0">无效</option>
				</select>
			</div>
			<div class="fitem">
				<label>有效标识:</label> 
				<select name="status" class="easyui-combobox" data-options="width:166,editable:false,panelHeight:'auto'">
								<option value="1">有效</option>
								<option value="0">无效</option>
				</select>
			</div>
		</form>
	</div>
	<div id="dlguser-buttons">
		<a href="javascript:void(0)" class="easyui-linkbutton" onclick="saveUser()" >保存</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" onclick="javascript:$('#dlguser').dialog('close')" >返回</a>
	</div>
	
	
	
	
	
	
	
	
	</div>
	<!-- button js event -->
	<script type="text/javascript">
		function formatStatus(val,row){    
		    if (val ==1){    
		        return '有效';    
		    } else {    
		        return '无效';    
		    }    
		} 
		
		
		$('#dg').datagrid({
        	onClickRow:function(index,data)
        	{
        		var row=$('#dg').datagrid('getSelected');
        		var value=$('#q_userName').val();
        		if(value=='请输入用户名称...'){
        			value='';
        		}
        		if(row){
        			var systemId=row.id;
        			url = 'platform/restmanage/user/list.html';
    				$.post(url, {
    					systemId:systemId,
    					userName : value
    				}, function(result) {
    					$('#dguser').datagrid('loadData',result);
    				}, 'json');
        		}
        	}
        })
		
		
		
	
		var url;
		
		//System  js begin
		
		function querySys() {
			var value=$('#q_systemName').val();
			url = 'platform/restmanage/system/list.html';
			$.post(url, {
				systemName : value
			}, function(result) {
				$('#dg').datagrid('loadData',result);
			}, 'json');
		}
		
		function newSys() {
			$('#dlg').dialog('open').dialog('setTitle', '添加系统');
			$('#fm').form('clear');
			$.post('platform/restmanage/org/listAll.html', {
			}, function(result) {
				$("#orgId").combobox("loadData", result);
			}, 'json');
			url = 'platform/restmanage/system/add.html';
		}
		
		function editSys() {
			var row = $('#dg').datagrid('getSelected');
			if (row) {
				$('#dlg').dialog('open').dialog('setTitle', '编辑系统');
				$.post('platform/restmanage/org/listAll.html', {
				}, function(result) {
					$("#orgId").combobox("loadData", result);
				}, 'json');
				$('#fm').form('load', row);
				url = 'platform/restmanage/system/edit.html?id=' + row.id;
			}
		}
		
		function saveSys() {
			$('#fm').form('submit', {
				url : url,
				onSubmit : function() {
					return $(this).form('validate');
				},
				success : function(result) {
					var result = eval('(' + result + ')');
					if (result.errorMsg) {
						$.messager.show({
							title : 'Error',
							msg : result.errorMsg
						});
					} else {
						$('#dlg').dialog('close'); 
						$('#dg').datagrid('reload'); 
					}
				}
			});
		}
		
		function deleteSys() {
			url='platform/restmanage/system/delete.html';
			var row = $('#dg').datagrid('getSelected');
			if (row) {
				$.messager.confirm('',
						'确认删除吗?',
						function(r) {
							if (r) {
								$.post(url, {
									id : row.id
								}, function(result) {
									if (result.success) {
										$('#dg').datagrid('reload');
									} else {
										$.messager.show({
											title : 'Error',
											msg : result.errorMsg
										});
									}
								}, 'json');
							}
				});
			}
		}
		
		//User js begin
		
		$(function(){  
	        $('#orgIduser').combobox({  
	            onSelect:function(record){  
	                $.post('platform/restmanage/system/listByorg.json?orgId='+record.id+'&random='+Math.random(), {
	    			}, function(result) {
	    				$("#systemIduser").combobox("loadData", result).combobox('clear');
	    			}, 'json');
	            }  
	        });  
		});  
	
		function queryUser() {
			var value=$('#q_userName').val();
			url = 'platform/restmanage/user/list.html';
			$.post(url, {
				userName : value
			}, function(result) {
				$('#dguser').datagrid('loadData',result);
			}, 'json');
		}
		
		function newUser() {
			$('#dlguser').dialog('open').dialog('setTitle', '新用户');
			$('#fmuser').form('clear');
			$.post('platform/restmanage/org/listAll.html?&random='+Math.random(), {
			}, function(result) {
				$("#orgIduser").combobox("loadData", result).combobox('clear');
			}, 'json');
			url = 'platform/restmanage/user/add.html';
		}
		
		function editUser() {
			var row = $('#dguser').datagrid('getSelected');
			if (row) {
				$('#dlguser').dialog('open').dialog('setTitle', '编辑用户');
				$('#fmuser').form('clear');
				
				$.post('platform/restmanage/org/listAll.html?&random='+Math.random(), {
				}, function(result) {
					$("#orgIduser").combobox("loadData", result);
				}, 'json');
				
				$.post('platform/restmanage/system/listByorg.html?orgId='+row.orgId+'&random='+Math.random(), {
				}, function(result) {
					$("#systemIduser").combobox("loadData", result);
				}, 'json');
				
				$('#fmuser').form('load', row);
				url = 'platform/restmanage/user/edit.html?id=' + row.id;
			}
		}
		
		function saveUser() {
			$('#fmuser').form('submit', {
				url : url,
				onSubmit : function() {
					return $(this).form('validate');
				},
				success : function(result) {
					var result = eval('(' + result + ')');
					if (result.errorMsg) {
						$.messager.show({
							title : 'Error',
							msg : result.errorMsg
						});
					} else {
						$('#dlguser').dialog('close'); 
						$('#dguser').datagrid('reload'); 
					}
				}
			});
		}
		
		function deleteUser() {
			url='platform/restmanage/user/delete.html';
			var row = $('#dguser').datagrid('getSelected');
			if (row) {
				$.messager.confirm('',
						'确认删除吗?',
						function(r) {
							if (r) {
								$.post(url, {
									id : row.id
								}, function(result) {
									if (result.success) {
										$('#dguser').datagrid('reload');
									} else {
										$.messager.show({
											title : 'Error',
											msg : result.errorMsg
										});
									}
								}, 'json');
							}
				});
			}
		}
		
	</script>
	
	<style type="text/css">
#fm {
	margin: 0;
	padding: 10px 30px;
}

.ftitle {
	font-size: 14px;
	font-weight: bold;
	padding: 5px 0;
	margin-bottom: 10px;
	border-bottom: 1px solid #ccc;
}

.fitem {
	margin-bottom: 5px;
}

.fitem label {
	display: inline-block;
	width: 80px;
}

.fitem input {
	width: 160px;
}
</style>
</body>
</html>