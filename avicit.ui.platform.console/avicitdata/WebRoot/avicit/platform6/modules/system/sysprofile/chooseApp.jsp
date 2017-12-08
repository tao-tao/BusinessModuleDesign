<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<base href="<%=ViewUtil.getRequestPath(request)%>">
<jsp:include page="/avicit/platform6/component/common/EasyUIJsInclude.jsp"></jsp:include>
</head>
<body class="easyui-layout">
	<div region="center" border="false" style="overflow: hidden;">
		<table id="sysAppList" class="easyui-datagrid"
			data-options=" 
						fit: true,
						border: false,
						rownumbers: true,
						animate: true,
						collapsible: false,
						fitColumns: true,
						autoRowHeight: false,
						idField :'id',
						singleSelect: true,
						checkOnSelect: true,
						selectOnCheck: false,
						url:'platform/sysApps/allSysApps/1234567.json',
						onDblClickRow:onSure,
						onLoadSuccess:init,
						striped:true">
			<thead>
				<tr>
					<th data-options="field:'id', halign:'center',checkbox:true" width="220">id</th>
					<th data-options="field:'applicationName',align:'center'" width="120">应用名称</th>
					<!-- <th data-options="field:'applicationCode',align:'center'">应用编码</th> -->
					<th data-options="field:'basepath',align:'left'"  width="120">应用地址</th>
					<th data-options="field:'description',align:'center',align:'left'" width="220">描述</th>
				</tr>
			</thead>
		</table>
		<div id="toolbar" class="datagrid-toolbar datagrid-toolbar-extend">
			<table class="tableForm" border="0" cellspacing="1" width='100%'>
				<tr>	
					<td align="right" width="60%">
						<a title="确定" id="saveButton"  class="easyui-linkbutton" plain="false" onclick="buttonSure();" href="javascript:void(0);">确定</a>
						<a title="返回" id="returnButton"  class="easyui-linkbutton"  plain="false" onclick="back();" href="javascript:void(0);">返回</a>
					</td>
				</tr>
			</table>
		</div>
	</div>
	<script type="text/javascript">
		function init(){
			var rows = $('#sysAppList').datagrid('getRows');
			var l = rows.length;
			var id='${sId}';
			for(;l--;){
				if(rows[l].id == id){
					$('#sysAppList').datagrid('selectRow',l);
					return true;
				}
			}
		}
		function onSure(rowIndex, rowData){
			parent.sysProVal.sureApp(${index},rowData);
			
		}
		function buttonSure(){
			var rows = $('#sysAppList').datagrid('getChecked');
			if(rows.length > 1){
				$.messager.alert('提示','只能选择一条数据！','warning');
				return false;
			}
			parent.sysProVal.sureApp(${index},rows[0]);
		}
		function back(){
			parent.sysProVal.closeApp('#app');
		}
	</script>
</body>
</html>