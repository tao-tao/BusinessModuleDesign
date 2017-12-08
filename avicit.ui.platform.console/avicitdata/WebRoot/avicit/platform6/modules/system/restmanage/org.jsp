<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>单位列表</title>
<base href="<%=ViewUtil.getRequestPath(request)%>">
<jsp:include page="/avicit/platform6/component/common/EasyUIJsInclude.jsp"></jsp:include>
<jsp:include page="/avicit/platform6/modules/system/commonpopup/commonSelectionHead.jsp"></jsp:include>
</head>
<body class="easyui-layout"  fit="true">
<div data-options="region:'center',split:true,border:false" style="padding:0px;overflow:hidden;">
	<table id="dg"  class="easyui-datagrid"  url="platform/restmanage/org/list.json" fit="true" toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="true" singleSelect="true" autoRowHeight="false" striped="true">
		<thead>
			<tr>
				<th data-options="field:'id', halign:'center',checkbox:true" width="50">id</th>
				<th data-options="field:'orgName',required:true,align:'center'"  width="100">单位名</th>
			</tr>
		</thead>
	</table>
	<!-- CRUD工具栏 -->
	<div id="toolbar">
		<table>
		<tr>
		<td><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newUser()">添加</a></td>
		<td><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editUser()">编辑</a></td>
		<td><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="deleteUser()">删除</a></td>
		<td><input id="q_orgName" name="orgName" class="easyui-textbox"  value="请输入单位名称..."  onfocus="{this.value='';this.style.color='#000'}"  style="color:#808080"></td>
		<td><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="queryUser()">查询</a></td>
		</tr>
		</table>
	</div>
	
	<!-- CU表单 -->
	<div id="dlg" class="easyui-dialog" style="width: 400px; height: 280px; padding: 10px 20px" closed="true" buttons="#dlg-buttons">
		<div class="ftitle">单位信息</div>
		<form id="fm" method="post" novalidate>
			<div class="fitem">
				<label>单位名称:</label> <input name="orgName" class="easyui-textbox" required="true">
			</div>
		</form>
	</div>
	<div id="dlg-buttons">
		<a href="javascript:void(0)" class="easyui-linkbutton"  onclick="saveUser()" >保存</a>
		<a href="javascript:void(0)" class="easyui-linkbutton"  onclick="javascript:$('#dlg').dialog('close')" >返回</a>
	</div>
	</div>
	
	<!-- button js event -->
	<script type="text/javascript">
		var baseurl = '<%=request.getContextPath()%>';
		var url;
		
		function queryUser() {
			var value=$('#q_orgName').val();
			if(value=='请输入单位名称...'){
    			value='';
    		}
			url = 'platform/restmanage/org/list.json';
			$.post(url, {
				orgName : value
			}, function(result) {
				$('#dg').datagrid('loadData',result);
			}, 'json');
		}
		
		function newUser() {
			$('#dlg').dialog('open').dialog('setTitle', '添加单位');
			$('#fm').form('clear');
			url = 'platform/restmanage/org/add.json';
		}
		
		function editUser() {
			var row = $('#dg').datagrid('getSelected');
			if (row) {
				$('#dlg').dialog('open').dialog('setTitle', '编辑单位');
				$('#fm').form('load', row);
				url = 'platform/restmanage/org/edit.json?id=' + row.id;
			}
		}
		
		function saveUser() {
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
		
		function deleteUser() {
			url='platform/restmanage/org/delete.json';
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