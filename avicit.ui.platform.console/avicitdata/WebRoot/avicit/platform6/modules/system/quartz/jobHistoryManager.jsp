<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>显示历史</title>
<base href="<%=ViewUtil.getRequestPath(request)%>">
<jsp:include page="/avicit/platform6/component/common/EasyUIJsInclude.jsp"></jsp:include>
<script type="text/javascript">
/**
 * 格式化结束状态
 */
function formatterEndStatus(value, row, index){
	var endStatus = "";
	if(value == 'S'){
		endStatus = "成功";
	}else if(value == 'F'){
		endStatus = "失败";
	}
	return endStatus;
}

/**
 * 返回
 */
function doBack(){
	parent.closeDialog();
}

/**
 * 删除所有历史
 */
function deleteHistroy(){
	$.messager.confirm('确认', '您确认删除吗?', function(r){
        if (r){
        	var param = {
        		jobId: $('#jobId').val()
        	};
        	
			$.ajax({
				url : 'platform/jobHistoryController/executeDeleteJobHistories',
				data : param,
				type : 'post',
				dataType : 'json',
				success : function(result) {
					if (result.flag == 'success') {
						$("#datagrid1").datagrid('reload'); 
						$.messager.show({title:'提示',msg :'删除所有历史成功！'});
					} else {
						$.messager.show({title:'提示',msg :'删除所有历史失败！'});
					}
				}
			});
        }
    });	
}
</script>
</head>
<body class="easyui-layout" fit="true">
<input type="hidden" id="jobId" value="${param.jobId}">
<div region="north" border="false">
	<div id="toolbar" class="datagrid-toolbar" style="height:auto;display: block;">
		<div style="padding:0 0 0 2px;">
			<a id="backButton" href="javascript:void(0)" onclick="doBack()" class="easyui-linkbutton" data-options="iconCls:'icon-back'" plain="true">返回</a>
			<a id="deleteButton" href="javascript:void(0)" onclick="deleteHistroy()" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" plain="true">删除所有</a>
		</div>
	</div>
</div>
<div region="center" border="false">
	<table id="datagrid1" class="easyui-datagrid" 
			data-options="
				fit: true,
				rownumbers: true,
				animate: true,
				collapsible: false,
				fitColumns: true,
				autoRowHeight: true,
				method: 'post',
				url:'platform/jobHistoryController/loadJobHistories.json?jobId=${param.jobId}',
				idField: 'id',
				singleSelect: true,
				pagination:true,
				pageSize:dataOptions.pageSize,
				pageList:dataOptions.pageList
			">
		<thead>
			<tr>
				<sec:accesscontrollist hasPermission="3" domainObject="jobHistoryManager_gridform_startDate">
					<th data-options="field:'startDate', halign:'center', align:'center',formatter:formatColumnTime" width="100">开始时间</th>
				</sec:accesscontrollist>
				<sec:accesscontrollist hasPermission="3" domainObject="jobHistoryManager_gridform_endDate">
					<th data-options="field:'endDate', halign:'center', align:'center',formatter:formatColumnTime" width="100">结束时间</th>
				</sec:accesscontrollist>
				<sec:accesscontrollist hasPermission="3" domainObject="jobHistoryManager_gridform_endStatus">
					<th data-options="field:'endStatus', halign:'center', align:'center',formatter:formatterEndStatus" width="50">结束状态</th>
				</sec:accesscontrollist>
				<sec:accesscontrollist hasPermission="3" domainObject="jobHistoryManager_gridform_message">
					<th data-options="field:'message', halign:'center', align:'left'" width="500">信息</th>
				</sec:accesscontrollist>
			</tr>
		</thead>
	</table>
</div>
</body>
</html>
