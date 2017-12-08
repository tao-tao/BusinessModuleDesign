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
		<table id="sysSite" class="easyui-datagrid"
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
						onDblClickRow:onSure,
						striped:true">
			<thead>
				<tr>
					<th data-options="field:'id', halign:'center',checkbox:true" width="220">id</th>
					<th data-options="field:'code',align:'center'" width="120">代码</th>
					<th data-options="field:'name',align:'left'"  width="120">名称</th>
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
		$(function(){
			$('#sysSite').datagrid('loadData',[{
				id:1,
				code:'Site',
				name:'地点'
			}]);
		});
		function onSure(rowIndex, rowData){
			parent.sysProVal.sureSite(${index},rowData);
		}
		function buttonSure(){
			var rows = $('#sysSite').datagrid('getChecked');
			if(rows.length > 1){
				$.messager.alert('提示','只能选择一条数据！','warning');
				return false;
			}
			parent.sysProVal.sureSite(${index},rows[0]);
		}
		function back(){
			parent.sysProVal.closeSite('#site');
		}
	</script>
</body>
</html>