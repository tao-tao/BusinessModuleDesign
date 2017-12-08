<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>选择Bean和函数</title>
<base href="<%=ViewUtil.getRequestPath(request)%>">
<jsp:include page="/avicit/platform6/component/common/EasyUIJsInclude.jsp"></jsp:include>
<script type="text/javascript">
/**
 * 初始化数据
 */
$(function(){
	$('#datagridBean').datagrid('enableFilter');
	$('#datagridBeanMethod').datagrid('enableFilter');
});

/**
 * 选择bean，刷新其下的函数
 */
function onSelectBean(rowIndex, rowData){
	$('#datagridBeanMethod').datagrid('options').url = 'platform/beanMethodSelectorController/loadMethods.json';
	$('#datagridBeanMethod').datagrid('load',{
		beanName: rowData.name
	});
}

/**
 * 双击选择函数
 */
function selectBeanMethod(rowIndex, rowData){
	var beanRecord = $('#datagridBean').datagrid('getSelected');
	var result = beanRecord.name + "#" + rowData.name;
	parent.closeSelectBeanDialog(result);
}
</script>
</head>
<body class="easyui-layout" fit="true">
<div region="north" border="false">
	<div id="toolbar" class="datagrid-toolbar" style="height:20px;overflow: hidden;">
		<div style="padding:0 0 0 2px;">
			双击具体的函数名称
		</div>
	</div>
</div>
<div region="center" border="false">
	<table id="datagridBean" class="easyui-datagrid" 
			data-options="
				fit: true,
				rownumbers: true,
				animate: true,
				collapsible: false,
				fitColumns: true,
				autoRowHeight: true,
				singleSelect: true,
				method: 'post',
				url:'platform/beanMethodSelectorController/loadBeans.json',
				onSelect: onSelectBean
			">
		<thead>
			<tr>
				<th data-options="field:'name', halign:'center', align:'left'" width="100">Bean名称</th>
			</tr>
		</thead>
	</table>
</div>
<div data-options="region:'east',split:true" style="width:200px;border-right: 0;border-bottom: 0;border-top: 0;">
	<table id="datagridBeanMethod" class="easyui-datagrid" 
			data-options="
				fit: true,
				rownumbers: true,
				animate: true,
				collapsible: false,
				fitColumns: true,
				autoRowHeight: true,
				method: 'post',
				singleSelect: true,
				onDblClickRow: selectBeanMethod
			">
		<thead>
			<tr>
				<th data-options="field:'name', halign:'center', align:'left'" width="100">函数名</th>
			</tr>
		</thead>
	</table>
</div>
</body>
</html>
