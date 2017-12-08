<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<%@ page import="avicit.platform6.api.quartz.dto.Job"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>定时器管理</title>
<base href="<%=ViewUtil.getRequestPath(request)%>">
<jsp:include page="/avicit/platform6/component/common/EasyUIJsInclude.jsp"></jsp:include>
<style type="text/css">
.icon-groupmenu, .icon-history, .icon-calendar, .icon-group{
	display:inline-block; 
	width:20px; 
	height:20px;
}

.icon-groupmenu{
	background:url('static/js/platform/component/jQuery/jquery-easyui-1.3.5/themes/gray/icon_extend/iconselectors.gif') no-repeat -60px -180px;
}

.icon-history{
	background:url('static/js/platform/component/jQuery/jquery-easyui-1.3.5/themes/gray/icon_extend/iconselectors.gif') no-repeat -140px -40px;
}

.icon-calendar{
	background:url('static/js/platform/component/jQuery/jquery-easyui-1.3.5/themes/gray/icon_extend/iconselectors.gif') no-repeat -100px -40px;
}

.icon-group{
	background:url('static/js/platform/component/jQuery/jquery-easyui-1.3.5/themes/gray/icon_extend/iconselectors.gif') no-repeat -40px -40px;
}
</style>
<script type="text/javascript">
/**
 * 初始化数据
 */
$(function(){
	/*初始化查询框*/
	var p = $('#searchButton').offset();
	$('#searchDialog').dialog({
		left: p.left,
		top: p.top + $('#searchButton').height()
	});
	var searchForm = $('#formSearch').form();
	searchForm.find('input').on('keyup',function(e){
		if(e.keyCode == 13){
			searchData();
		}
	});
});

/**
 * 打开查询框
 */
function openSearchForm(){
	$('#searchDialog').dialog('open',true);
}

/**
 * 隐藏查询框
 */
function hideSearch(){
	$('#searchDialog').dialog('close',true);
}

/**
 * 清空查询条件
 */
function clearData(){
	$('#formSearch').find('input').val('');
};

/**
 * 查询数据
 */
function searchData(){
	$('#datagrid1').datagrid('reload', serializeObject($('#formSearch').form()));
}

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
 * 启动定时任务
 */
function startJob(){
	var selected = $('#datagrid1').datagrid('getSelected');
	if(selected == null){
		$.messager.show({title:'提示',msg :'请先选择一条数据！'});
		return false;
	}
	var id = selected.id;
	$.messager.confirm('确认', '您正的要开始这一任务吗?', function(r){
        if (r){
        	var param = {
        		id: id
        	};
        	
			$.ajax({
				url : 'platform/jobScheduleController/executeStartJob',
				data : param,
				type : 'post',
				dataType : 'json',
				success : function(result) {
					if (result.flag == 'success') {
						$("#datagrid1").datagrid('reload'); 
						$.messager.show({title:'提示',msg :'操作成功！'});
					} else {
						if (result.idError == 'true') {
							$.messager.show({title:'提示',msg :'必须指定调度的任务编号！'});
						}else if (result.jobRunning == 'true') {
							$.messager.show({title:'提示',msg :'当前任务已处于开始状态！'});
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
function stopJob(){
	var selected = $('#datagrid1').datagrid('getSelected');
	if(selected == null){
		$.messager.show({title:'提示',msg :'请先选择一条数据！'});
		return false;
	}
	var id = selected.id;
	$.messager.confirm('确认', '您正的要停止这一任务吗?', function(r){
        if (r){
        	var param = {
        		id: id
        	};
        	
			$.ajax({
				url : 'platform/jobScheduleController/executeStopJob',
				data : param,
				type : 'post',
				dataType : 'json',
				success : function(result) {
					if (result.flag == 'success') {
						$("#datagrid1").datagrid('reload'); 
						$.messager.show({title:'提示',msg :'操作成功！'});
					} else {
						if (result.idError == 'true') {
							$.messager.show({title:'提示',msg :'必须指定调度的任务编号！'});
						}else if (result.jobStoped == 'true') {
							$.messager.show({title:'提示',msg :'当前任务已处于停止状态！'});
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
function runRightNow(){
	var selected = $('#datagrid1').datagrid('getSelected');
	if(selected == null){
		$.messager.show({title:'提示',msg :'请先选择一条数据！'});
		return false;
	}
	var id = selected.id;
	$.messager.confirm('确认', '您正的要立即执行这一任务吗?', function(r){
        if (r){
        	var param = {
        		id: id
        	};
        	
			$.ajax({
				url : 'platform/jobScheduleController/executeRunRightNow',
				data : param,
				type : 'post',
				dataType : 'json',
				success : function(result) {
					if (result.flag == 'success') {
						$("#datagrid1").datagrid('reload'); 
						$.messager.show({title:'提示',msg :'操作成功！'});
					} else {
						if (result.idError == 'true') {
							$.messager.show({title:'提示',msg :'必须指定调度的任务编号！'});
						}else if (result.JobExecutorIsNull == 'true') {
							$.messager.show({title:'提示',msg :'必须配置JobExecutor！'});
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
 * 显示历史
 */
function showHistory(){
	var selected = $('#datagrid1').datagrid('getSelected');
	if(selected == null){
		$.messager.show({title:'提示',msg :'请先选择一条数据！'});
		return false;
	}
	
	var id = selected.id;
	var usd = new CommonDialog("showHistoryDialog","700","400","avicit/platform6/modules/system/quartz/jobHistoryManager.jsp?jobId="+id,"显示历史",true,true,false,false,true);
	usd.show();
	
	/**
	 * 关闭页面
	 */
	closeDialog = function() {
		usd.close();
	};
}

/**
 * 配置日历
 */
function showCalendar(){
	var usd = new CommonDialog("showCalendarDialog","700","400","avicit/platform6/modules/system/quartz/jobCalendarMaintainManager.jsp","配置日历",true,true,false,false,true);
	usd.show();
	
	/**
	 * 关闭页面
	 */
	closeDialog = function() {
		usd.close();
	};
}

/**
 * 组任务查看
 */
function showGroupResolve(){
	var usd = new CommonDialog("showGroupResolveDialog","650","400","avicit/platform6/modules/system/quartz/groupResolve.jsp","组任务操作",true,true,false);
	usd.show();
	
	/**
	 * 关闭页面
	 */
	closeDialog = function() {
		usd.close();
	};
}

/**
 * 添加任务
 */
function addJob(){
	var clientWidth = document.body.clientWidth;
	var clientHeight = document.body.clientHeight;

	var usd = new CommonDialog("addJobDialog",clientWidth,clientHeight,"avicit/platform6/modules/system/quartz/jobMaintainAdd.jsp","添加任务",false,true,false);
	          
	usd.show();
	
	/**
	 * 成功添加任务后执行
	 */
	successAddJob = function() {
		$("#datagrid1").datagrid('reload');
		$.messager.show({title:'提示',msg :'添加定时任务成功！'});
		usd.close();
	};
	
	/**
	 * 关闭页面
	 */
	closeDialog = function() {
		usd.close();
	};
}

/**
 * 修改任务
 */
function updateJob(){
	var selected = $('#datagrid1').datagrid('getSelected');
	if(selected == null){
		$.messager.show({title:'提示',msg :'请先选择一条数据！'});
		return false;
	}
	
	var id = selected.id;
	var clientWidth = document.body.clientWidth;
	var clientHeight = document.body.clientHeight;
	var usd = new CommonDialog("updateJobDialog",clientWidth,clientHeight,"platform/jobMaintainController/toUpdateJobJsp?id=" + id,"修改任务",true,true,false);
	usd.show();
	
	/**
	 * 成功修改任务后执行
	 */
	successUpdateJob = function() {
		$("#datagrid1").datagrid('reload');
		$.messager.show({title:'提示',msg :'修改定时任务成功！'});
		usd.close();
	};
	
	/**
	 * 关闭页面
	 */
	closeDialog = function() {
		usd.close();
	};
}

/**
 * 删除定时任务
 */
function deleteJob(){
	var selected = $('#datagrid1').datagrid('getSelected');
	if(selected == null){
		$.messager.show({title:'提示',msg :'请先选择一条数据！'});
		return false;
	}

	$.messager.confirm('确认', '您确认删除吗?', function(r){
        if (r){
        	var param = {
        		id: selected.id
        	};
        	
			$.ajax({
				url : 'platform/jobMaintainController/deleteJob',
				data : param,
				type : 'post',
				dataType : 'json',
				success : function(result) {
					if (result.flag == 'success') {
						$("#datagrid1").datagrid('uncheckAll'); 
						$("#datagrid1").datagrid('reload'); 
						$.messager.show({title:'提示',msg :'删除定时任务成功！'});
					} else {
						$.messager.show({title:'提示',msg :'删除定时任务失败！'});
					}
				}
			});
        }
    });	
}
</script>
</head>
<body class="easyui-layout" fit="true">
<div region="north" border="false">
	<div id="toolbar" class="datagrid-toolbar" style="height:auto;display: block;">
		<div style="padding:0 0 0 2px;">
			<a id="addButton" href="javascript:void(0)" onclick="addJob()" class="easyui-linkbutton" data-options="iconCls:'icon-add'" plain="true">添加</a>
			<a id="editButton" href="javascript:void(0)" onclick="updateJob()" class="easyui-linkbutton" data-options="iconCls:'icon-edit'" plain="true">编辑</a>
			<a id="scheduleButton" href="javascript:void(0)"  class="easyui-menubutton" data-options="menu:'#scheduleButtons',iconCls:'icon-groupmenu'">调度</a>   
			<div id="scheduleButtons" style="width:100px;">   
			    <div data-options="iconCls:'icon-add'" onclick="startJob()">开始</div> 
			    <div data-options="iconCls:'icon-remove'" onclick="stopJob()">停止</div>
			    <div data-options="iconCls:'icon-valid'" onclick="runRightNow()">立即执行</div>
			</div>
			<a id="showHistoryButton" href="javascript:void(0)" onclick="showHistory()" class="easyui-linkbutton" data-options="iconCls:'icon-history'" plain="true">显示历史</a>
			<a id="showCalendarButton" href="javascript:void(0)" onclick="showCalendar()" class="easyui-linkbutton" data-options="iconCls:'icon-calendar'" plain="true">配置日历</a>
			<a id="groupButton" href="javascript:void(0)" onclick="showGroupResolve()" class="easyui-linkbutton" data-options="iconCls:'icon-group'" plain="true">组任务查看</a>
			<span class="datagrid-btn-separator" style="float:none; display: inline-block;height: 20px;"></span>
			<a id="deleteButton" href="javascript:void(0)" onclick="deleteJob()" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" plain="true">删除</a>
			<a id="searchButton" href="javascript:void(0)" onclick="openSearchForm()" class="easyui-linkbutton" data-options="iconCls:'icon-search'" plain="true">查询</a>
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
				url:'platform/jobMaintainController/loadJobs.json',
				idField: 'id',
				singleSelect: true,
				checkOnSelect: true,
				selectOnCheck: true,
				pagination:true,
				pageSize:dataOptions.pageSize,
				pageList:dataOptions.pageList
			">
		<thead>
			<tr>
				<sec:accesscontrollist hasPermission="3" domainObject="jobMaintainManager_gridform_group">
					<th data-options="field:'group', halign:'center', align:'left',formatter:formatterGroup" width="100">任务分组</th>
				</sec:accesscontrollist>
				<sec:accesscontrollist hasPermission="3" domainObject="jobMaintainManager_gridform_name">
					<th data-options="field:'name', halign:'center', align:'left'" width="120">名称</th>
				</sec:accesscontrollist>
				<sec:accesscontrollist hasPermission="3" domainObject="jobMaintainManager_gridform_type">
					<th data-options="field:'type', halign:'center', align:'left',formatter:formatterType" width="70">任务类型</th>
				</sec:accesscontrollist>
				<sec:accesscontrollist hasPermission="3" domainObject="jobMaintainManager_gridform_status">
					<th data-options="field:'status', halign:'center', align:'center',formatter:formatterStatus" width="60">状态</th>
				</sec:accesscontrollist>
				<sec:accesscontrollist hasPermission="3" domainObject="jobMaintainManager_gridform_lastState">
					<th data-options="field:'lastState', halign:'center', align:'center',formatter:formatterLastState" width="80">最后一次执行状态</th>
				</sec:accesscontrollist>
				<sec:accesscontrollist hasPermission="3" domainObject="jobMaintainManager_gridform_program">
					<th data-options="field:'program', halign:'center', align:'left'" width="200">程序</th>
				</sec:accesscontrollist>
				<sec:accesscontrollist hasPermission="3" domainObject="jobMaintainManager_gridform_cron">
					<th data-options="field:'cron', halign:'center', align:'left'" width="150">表达式</th>
				</sec:accesscontrollist>
				<sec:accesscontrollist hasPermission="3" domainObject="jobMaintainManager_gridform_description">
					<th data-options="field:'description', halign:'center', align:'left'" width="200">描述</th>
				</sec:accesscontrollist>
			</tr>
		</thead>
	</table>
</div>
<div id="searchDialog" class="easyui-dialog" data-options="title:'查询条件',closed:true,iconCls:'icon-search',resizable:true,modal:false,buttons:'#searchBtns'" style="width: 450px;height:170px;">
	<form id="formSearch">
		<table class="tableForm" width="98%" height="70px">
			<tr>
				<td width="60px;" align="right">名称</td>
				<td width="150px;" colspan="3">
					<input id="searchName" name="searchName" type="text" style="width: 330px"/>
				</td>
			</tr>
			<tr>
				<td align="right">所属分组</td>
				<td width="150px;">
					<input style="width: 150px" id="searchGroup" name="searchGroup" class="easyui-combobox" 
					    data-options="
					        editable:false,
					        panelHeight:'auto',
					        valueField:'name',
					        textField:'description',
					        url:'platform/jobMaintainController/loadGroups.json'" />
				</td>
				<td width="50px;" align="right">状态</td>
				<td>
					<input style="width:125px;" id="searchStatus" name="searchStatus" class="easyui-combobox" 
						 data-options="
						    editable:false,
						    panelHeight:'auto',
							valueField:'label',
							textField:'value',
							data: [{label: 'C',value: '启动中'},
							       {label: 'R',value: '运行中'},
							       {label: 'D',value: '暂停中'},
							       {label: 'S',value: '已暂停'}]" />  
				</td>
			</tr>
		</table>
	</form>	
   	<div id="searchBtns">
   		<a class="easyui-linkbutton"  plain="false" onclick="searchData();" href="javascript:void(0);">查询</a>
   		<a class="easyui-linkbutton" plain="false" onclick="clearData();" href="javascript:void(0);">清空</a>
   		<a class="easyui-linkbutton" plain="false" onclick="hideSearch();" href="javascript:void(0);">返回</a>
   	</div>
</div>
</body>
</html>
