<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<%@ page import="avicit.platform6.api.quartz.dto.Job"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>组任务操作</title>
<base href="<%=ViewUtil.getRequestPath(request)%>">
<jsp:include page="/avicit/platform6/component/common/EasyUIJsInclude.jsp"></jsp:include>
<script type="text/javascript">
/**
 * 初始化数据
 */
$(function(){

});

/**
 * 格式化任务分组
 */
function formatterGroup(value, row, index){
	var groupName = "";
	if(value == '<%=Job.DEFAULT_GROUP %>'){
		groupName = "默认组";
	}else if(value == '<%=Job.DEFAULT_SYSTEM_JOB_GROUP %>'){
		groupName = "系统后台自动任务";
	}
	return groupName;
}

/**
 * 格式化任务类型
 */
function formatterType(value, row, index){
	var typeName = "";
	if(value == 'spring'){
		typeName = "SpringBean";
	}else if(value == 'clazz'){
		typeName = "Java类";
	}else if(value == 'quartzClazz'){
		typeName = "集成自Job接口的Java类";
	}else if(value == 'sql'){
		typeName = "Sql语句";
	}else if(value == 'sp'){
		typeName = "存储过程";
	}
	return typeName;
}

/**
 * 格式化状态
 */
function formatterStatus(value, row, index){
	var statusName = "";
	if(value == 'C'){
		statusName = "启动中";
	}else if(value == 'R'){
		statusName = "运行中";
	}else if(value == 'D'){
		statusName = "暂停中";
	}else if(value == 'S'){
		statusName = "已暂停";
	}
	return statusName;
}

/**
 * 格式化最后一次执行状态
 */
function formatterLastState(value, row, index){
	var lastStateName = "";
	if(value == 'S'){
		lastStateName = "成功";
	}else if(value == 'F'){
		lastStateName = "失败";
	}
	return lastStateName;
}

/**
 * 选择组，刷新其下的定时任务
 */
function onSelectGroup(rowIndex, rowData){
	$('#datagridGroupJob').datagrid('options').url = 'platform/jobMaintainController/loadJobsByGroup.json';
	$('#datagridGroupJob').datagrid('load',{
		group: rowData.name
	});
}

/**
 * 启动定时任务
 */
function startGroupJob(){
	var selected = $('#datagridGroup').datagrid('getSelected');
	if(selected == null){
		$.messager.show({title:'提示',msg :'请先选择一条数据！'});
		return false;
	}
	$.messager.confirm('确认', '确定启动该组中尚未启动的所有任务?', function(r){
        if (r){
        	var param = {
        		group: selected.name
        	};
        	
			$.ajax({
				url : 'platform/jobScheduleController/executeStartGroupJob',
				data : param,
				type : 'post',
				dataType : 'json',
				success : function(result) {
					if (result.flag == 'success') {
						$("#datagridGroupJob").datagrid('reload');
						parent.$("#datagrid1").datagrid('reload');
						$.messager.show({title:'提示',msg :'操作成功！'});
					} else {
						if (result.idError == 'true') {
							$.messager.show({title:'提示',msg :'必须指定调度的任务编号！'});
						}else if (result.jobsAllRunning == 'true') {
							$.messager.show({title:'提示',msg :'当前组中的所有任务都处于运行状态！'});
						}else if (result.jobsEmpty == 'true') {
							$.messager.show({title:'提示',msg :'当前组中的没有任何任务！'});
						}else{
							$.messager.show({title:'提示',msg :'操作失败！'});
						}
					}
				}
			});
        }
    });	
}

/**
 * 停止定时任务
 */
function stopGroupJob(){
	var selected = $('#datagridGroup').datagrid('getSelected');
	if(selected == null){
		$.messager.show({title:'提示',msg :'请先选择一条数据！'});
		return false;
	}
	$.messager.confirm('确认', '确定要停止该组中的所有任务?', function(r){
        if (r){
        	var param = {
        		group: selected.name
        	};
        	
			$.ajax({
				url : 'platform/jobScheduleController/executeStopGroupJob',
				data : param,
				type : 'post',
				dataType : 'json',
				success : function(result) {
					if (result.flag == 'success') {
						$("#datagridGroupJob").datagrid('reload');
						parent.$("#datagrid1").datagrid('reload');
						$.messager.show({title:'提示',msg :'操作成功！'});
					} else {
						if (result.idError == 'true') {
							$.messager.show({title:'提示',msg :'必须指定调度的任务编号！'});
						}else if (result.jobsAllStoped == 'true') {
							$.messager.show({title:'提示',msg :'当前组中的所有任务都处于停止状态！'});
						}else if (result.jobsEmpty == 'true') {
							$.messager.show({title:'提示',msg :'当前组中的没有任何任务！'});
						}else{
							$.messager.show({title:'提示',msg :'操作失败！'});
						}
					}
				}
			});
        }
    });	
}

/**
 * 立即执行定时任务
 */
function runRightNowGroup(){
	var selected = $('#datagridGroup').datagrid('getSelected');
	if(selected == null){
		$.messager.show({title:'提示',msg :'请先选择一条数据！'});
		return false;
	}
	$.messager.confirm('确认', '确定要立即执行该组中的所有任务?', function(r){
        if (r){
        	var param = {
        		group: selected.name
        	};
        	
			$.ajax({
				url : 'platform/jobScheduleController/executeImmediate',
				data : param,
				type : 'post',
				dataType : 'json',
				success : function(result) {
					if (result.flag == 'success') {
						$("#datagridGroupJob").datagrid('reload'); 
						parent.$("#datagrid1").datagrid('reload');
						$.messager.show({title:'提示',msg :'操作成功！'});
					} else {
						if (result.idError == 'true') {
							$.messager.show({title:'提示',msg :'必须指定调度的任务编号！'});
						}else if (result.JobExecutorIsNull == 'true') {
							$.messager.show({title:'提示',msg :'必须配置JobExecutor！'});
						}else if (result.jobsEmpty == 'true') {
							$.messager.show({title:'提示',msg :'当前组中的没有任何任务！'});
						}else{
							$.messager.show({title:'提示',msg :'操作失败！'});
						}
					}
				}
			});
        }
    });	
}
</script>
</head>
<body class="easyui-layout" fit="true">
<div region="north" border="false" style="height: 160px;">
	<table id="datagridGroup" class="easyui-datagrid" 
			data-options="
				fit: true,
				rownumbers: true,
				animate: true,
				collapsible: false,
				fitColumns: true,
				autoRowHeight: true,
				idField: 'name',
				singleSelect: true,
				toolbar: '#toolbar',
				method: 'post',
				url:'platform/jobMaintainController/loadGroupsRows.json',
				onSelect: onSelectGroup
			">
		<thead>
			<tr>
				<th data-options="field:'description', halign:'center', align:'left'" width="100">组描述</th>
			</tr>
		</thead>
	</table>
	<div id="toolbar" class="datagrid-toolbar" style="height:27px;overflow: hidden;">
		<div style="padding:0 0 0 2px;">
			<a href="javascript:void(0)" onclick="startGroupJob()" class="easyui-linkbutton" data-options="iconCls:'icon-add'" plain="true">开始</a>
			<a href="javascript:void(0)" onclick="stopGroupJob()" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" plain="true">停止</a>
			<a href="javascript:void(0)" onclick="runRightNowGroup()" class="easyui-linkbutton" data-options="iconCls:'icon-valid'" plain="true">立即执行</a>
		</div>
	</div>
</div>
<div region="center" border="false">
	<table id="datagridGroupJob" class="easyui-datagrid" 
			data-options="
				fit: true,
				rownumbers: true,
				animate: true,
				collapsible: false,
				fitColumns: true,
				autoRowHeight: true,
				method: 'post',
				idField: 'id',
				singleSelect: true
			">
		<thead>
			<tr>
				<th data-options="field:'name', halign:'center', align:'left'" width="100">名称</th>
				<th data-options="field:'type', halign:'center', align:'left',formatter:formatterType" width="80">任务类型</th>
				<th data-options="field:'program', halign:'center', align:'left'" width="120">程序</th>
				<th data-options="field:'cron', halign:'center', align:'left'" width="100">表达式</th>
				<th data-options="field:'status', halign:'center', align:'center',formatter:formatterStatus" width="60">状态</th>
			</tr>
		</thead>
	</table>
</div>
</body>
</html>
