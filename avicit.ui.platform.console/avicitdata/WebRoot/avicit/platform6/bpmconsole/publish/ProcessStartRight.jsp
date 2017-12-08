<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<html>
<head>
<%
String deployment = request.getParameter("deployment");//流程部署id
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>流程实例信息</title>
<base href="<%=ViewUtil.getRequestPath(request) %>">
<jsp:include page="/avicit/platform6/bpmclient/bpm/ProcessCommonJsInclude.jsp"></jsp:include>
<script src="static/js/platform/bpm/client/js/ToolBar.js" type="text/javascript"></script>
<script src="static/js/platform/bpm/client/js/ButtonProcessing.js" type="text/javascript"></script>

</head>

<script type="text/javascript">
var baseurl = '<%=request.getContextPath()%>';
var targetType = "U";//用户类型
/**
 * 页面请求入口
 */
$(function(){
		$('#processAnalysis').tabs({
		    onSelect:function(title,index){
		       if(index == 0){
		    	   targetType = "U";
		    	   getProcessUserRight();
		       }
			   if(index == 1){
				   targetType = "D";
				   getProcessDeptRight();
				}
			   if(index == 2){
				   targetType = "R";
				   getProcessRoleRight();
				}
			   if(index == 3){
				   targetType = "G";
				   getProcessGroupRight();
				}
			   if(index == 4){
				   targetType = "P";
				   getProcessPositionRight();
				}
		    }   
		});
		getProcessUserRight();
});
//////////////////////////////////////////////////////////////////
/**
 * 获取流程用户权限 
 */
function getProcessUserRight(){
	var dataGridHeight = $(".easyui-layout").height() - 85;
	$('#processRightUser').datagrid({
		toolbar:[
				{
					text:'添加用户',
					iconCls:'icon-add',
					handler:function(){
						addProcessUser();
					}
				},'-',{
					text:'删除用户',
					iconCls:'icon-remove',
					handler:function(){
						deleteProcessUser();
					}
				},'-',{
					text:'刷新缓存',
					iconCls:'icon-reload',
					handler:function(){
						refreshCache();
					}
				}],
		url: 'platform/bpm/processAccessAction/getBpmAccessListByPage.json?deployment=<%=deployment%>&targetType='+targetType,
		width: '100%',
	    nowrap: false,
	    striped: true,
	    autoRowHeight:false,
	    singleSelect:false,
	    checkOnSelect:true,
	    remoteSort : false,
		height: dataGridHeight,
		fitColumns: true,
		sortName: 'dbid_',  //排序字段,当表格初始化时候的排序字段
		sortOrder: 'desc',    //定义排序顺序
		pagination:true,
		rownumbers:true,
		pageSize: 20,
		queryParams:{"":""},
		loadMsg:' 处理中，请稍候…',
		columns:[[
			{field:'dbid_',hidden:true},
			{field:'op',title:'操作',width:25,align:'left',checkbox:true},
			{field:'targetid_',title:'用户ID',width:50,align:'left'},
			{field:'targetName',title:'用户名称',width:50,align:'left'}
		]]
	});
	//设置分页控件   
	var p = $('#processRightUser').datagrid('getPager');
	$(p).pagination({
	    pageSize: 10,//每页显示的记录条数，默认为10
	    pageList: [5,10,15,20,25,30],//可以设置每页记录条数的列表
	    beforePageText: '第',//页数文本框前显示的汉字
	    afterPageText: '页    共 {pages} 页',
	    displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
	});
}
/**
 * 增加用户到流程启动权限
 */
function addProcessUser(){
	var usd = new UserSelectDialog('userSelectCommonDialog', 700, 400,
			getPath() + '/platform/user/bpmSelectUserAction/userSelectCommon?isMultiple=true',
			'选择人员部门');
	var buttons = [ {
		text : '确定',
		id : 'processSubimt',
		//iconCls : 'icon-submit',
		handler : function() {
			var frmId = $('#userSelectCommonDialog iframe').attr('id');
			var frm = document.getElementById(frmId).contentWindow;
			var resultDatas = frm.getSelectedResultDataJson();
			var userIds = "";
			for ( var i = 0; i < resultDatas.length; i++) {
				var resultData = resultDatas[i];
				//$("#receptUserId").val(resultData.userId);
				//$("#receptUserName").val(resultData.userName);
				userIds = userIds + resultData.userId + ";";
			}
			if(userIds!=null&&userIds!=""){
				userIds = userIds.substring(0,userIds.length-1);
			}else{
				$.messager.alert('提示', "请选择用户");
				return ;
			}
			var dataJson = {
				"targettype_" : "U",
				"targetid_" : userIds,
				"deployment_" : "<%=deployment%>",
				"access_" : "1",
				"type_" : "start"
			};
			var dataStr = JSON.stringify(dataJson);
			ajaxRequest("POST","dataVo="+dataStr,"platform/bpm/processAccessAction/addProcessRight","json","addProcessRightUserBack");
			usd.close();
		}
	} ];
	usd.createButtonsInDialog(buttons);
	usd.show();
}
function addProcessRightUserBack(result){
	if (result.flag!=null&&result.flag=="ok") {
		$.messager.show({title : '提示',msg : '保存成功'});
		$("#processRightUser").datagrid('reload'); 
	}else{
		$.messager.alert('提示', "保存失败");
	}
}
/**
 * 删除用户到流程启动权限
 */
function deleteProcessUser(){
	var datas = $('#processRightUser').datagrid('getSelections');
	if(datas == null){
		$.messager.alert("操作提示", "请您选择一条记录!","info");
		return;
	}
	var ids = '';
	for(var i=0;i<datas.length;i++){
		ids += datas[i].dbid_ + ',';
	}
	$.messager.confirm("操作提示", "您确认要删除选定的数据吗？", function (data) {
        if (data) {
        	easyuiMask();
        	ajaxRequest("POST","dbId="+ids,"platform/bpm/processAccessAction/deleteProcessRight","json","deleteProcessRightUserBack");
        }
	 });
}
function  deleteProcessRightUserBack(json){
    $.messager.show({title : '提示',msg : "删除成功!"});
    easyuiUnMask();
	$("#processRightUser").datagrid('reload'); 
}
//////////////////////////////////////////////////////////
/**
 * 获取流程部门权限 
 */
function getProcessDeptRight(){
	var dataGridHeight = $(".easyui-layout").height() - 85;
	$('#processRightDept').datagrid({
		toolbar:[
				{
					text:'添加部门',
					iconCls:'icon-add',
					handler:function(){
						addProcessDept();
					}
				},'-',{
					text:'删除部门',
					iconCls:'icon-remove',
					handler:function(){
						deleteProcessDept();
					}
				},'-',{
					text:'刷新缓存',
					iconCls:'icon-reload',
					handler:function(){
						refreshCache();
					}
				}],
		url: 'platform/bpm/processAccessAction/getBpmAccessListByPage.json?deployment=<%=deployment%>&targetType='+targetType,
		width: '100%',
	    nowrap: false,
	    striped: true,
	    autoRowHeight:false,
	    singleSelect:false,
	    checkOnSelect:true,
	    remoteSort : false,
		height: dataGridHeight,
		fitColumns: true,
		sortName: 'dbid_',  //排序字段,当表格初始化时候的排序字段
		sortOrder: 'desc',    //定义排序顺序
		pagination:true,
		rownumbers:true,
		pageSize: 20,
		queryParams:{"":""},
		loadMsg:' 处理中，请稍候…',
		columns:[[
			{field:'dbid_',hidden:true},
			{field:'op',title:'操作',width:25,align:'left',checkbox:true},
			{field:'targetid_',title:'部门ID',width:50,align:'left'},
			{field:'targetName',title:'部门名称',width:50,align:'left'}
		]]
	});
	//设置分页控件   
	var p = $('#processRightDept').datagrid('getPager');
	$(p).pagination({
	    pageSize: 10,//每页显示的记录条数，默认为10
	    pageList: [5,10,15,20,25,30],//可以设置每页记录条数的列表
	    beforePageText: '第',//页数文本框前显示的汉字
	    afterPageText: '页    共 {pages} 页',
	    displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
	});
}
/**
 * 增加部门到流程启动权限
 */
function addProcessDept(){
	var usd = new UserSelectDialog('userSelectCommonDialog', 700, 400,
			getPath() + '/platform/user/bpmSelectUserAction/deptSelectCommon?isMultiple=true',
			'选择部门');
	var buttons = [ {
		text : '确定',
		id : 'processSubimt',
		//iconCls : 'icon-submit',
		handler : function() {
			var frmId = $('#userSelectCommonDialog iframe').attr('id');
			var frm = document.getElementById(frmId).contentWindow;
			var resultDatas = frm.getSelectedResultDataJson();
			var ids = "";
			for ( var i = 0; i < resultDatas.length; i++) {
				var resultData = resultDatas[i];
				ids = ids + resultData.deptId + ";";
			}
			if(ids!=null&&ids!=""){
				ids = ids.substring(0,ids.length-1);
			}else{
				$.messager.alert('提示', "请选择部门");
				return ;
			}
			var dataJson = {
				"targettype_" : "D",
				"targetid_" : ids,
				"deployment_" : "<%=deployment%>",
				"access_" : "1",
				"type_" : "start"
			};
			var dataStr = JSON.stringify(dataJson);
			ajaxRequest("POST","dataVo="+dataStr,"platform/bpm/processAccessAction/addProcessRight","json","addProcessRightDeptBack");
			usd.close();
		}
	} ];
	usd.createButtonsInDialog(buttons);
	usd.show();
}
function addProcessRightDeptBack(result){
	if (result.flag!=null&&result.flag=="ok") {
		$.messager.show({title : '提示',msg : '保存成功'});
		$("#processRightDept").datagrid('reload'); 
	}else{
		$.messager.alert('提示', "保存失败");
	}
}
/**
 * 删除用户到流程启动权限
 */
function deleteProcessDept(){
	var datas = $('#processRightDept').datagrid('getSelections');
	if(datas == null){
		$.messager.alert("操作提示", "请您选择一条记录!","info");
		return;
	}
	var ids = '';
	for(var i=0;i<datas.length;i++){
		ids += datas[i].dbid_ + ',';
	}
	$.messager.confirm("操作提示", "您确认要删除选定的数据吗？", function (data) {
        if (data) {
        	easyuiMask();
        	ajaxRequest("POST","dbId="+ids,"platform/bpm/processAccessAction/deleteProcessRight","json","deleteProcessDeptRightBack");
        }
	 });
}
function  deleteProcessDeptRightBack(json){
    $.messager.show({title : '提示',msg : "删除成功!"});
    easyuiUnMask();
	$("#processRightDept").datagrid('reload'); 
}
//////////////////////////////////////////////////////////
/**
 * 获取流程角色权限 
 */
function getProcessRoleRight(){
	var dataGridHeight = $(".easyui-layout").height() - 85;
	$('#processRightRole').datagrid({
		toolbar:[
				{
					text:'添加角色',
					iconCls:'icon-add',
					handler:function(){
						addProcessRole();
					}
				},'-',{
					text:'删除角色',
					iconCls:'icon-remove',
					handler:function(){
						deleteProcessRole();
					}
				},'-',{
					text:'刷新缓存',
					iconCls:'icon-reload',
					handler:function(){
						refreshCache();
					}
				}],
		url: 'platform/bpm/processAccessAction/getBpmAccessListByPage.json?deployment=<%=deployment%>&targetType='+targetType,
		width: '100%',
	    nowrap: false,
	    striped: true,
	    autoRowHeight:false,
	    singleSelect:false,
	    checkOnSelect:true,
	    remoteSort : false,
		height: dataGridHeight,
		fitColumns: true,
		sortName: 'dbid_',  //排序字段,当表格初始化时候的排序字段
		sortOrder: 'desc',    //定义排序顺序
		pagination:true,
		rownumbers:true,
		pageSize: 20,
		queryParams:{"":""},
		loadMsg:' 处理中，请稍候…',
		columns:[[
			{field:'dbid_',hidden:true},
			{field:'op',title:'操作',width:25,align:'left',checkbox:true},
			{field:'targetid_',title:'角色ID',width:50,align:'left'},
			{field:'targetName',title:'角色名称',width:50,align:'left'}
		]]
	});
	//设置分页控件   
	var p = $('#processRightRole').datagrid('getPager');
	$(p).pagination({
	    pageSize: 10,//每页显示的记录条数，默认为10
	    pageList: [5,10,15,20,25,30],//可以设置每页记录条数的列表
	    beforePageText: '第',//页数文本框前显示的汉字
	    afterPageText: '页    共 {pages} 页',
	    displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
	});
}
/**
 * 增加角色到流程启动权限
 */
function addProcessRole(){
	var usd = new UserSelectDialog('userSelectCommonDialog', 700, 400,
			getPath() + '/platform/user/bpmSelectUserAction/roleSelectCommon?isMultiple=true',
			'选择角色');
	var buttons = [ {
		text : '确定',
		id : 'processSubimt',
		//iconCls : 'icon-submit',
		handler : function() {
			var frmId = $('#userSelectCommonDialog iframe').attr('id');
			var frm = document.getElementById(frmId).contentWindow;
			var resultDatas = frm.getSelectedResultDataJson();
			var ids = "";
			for ( var i = 0; i < resultDatas.length; i++) {
				var resultData = resultDatas[i];
				ids = ids + resultData.roleId + ";";
			}
			if(ids!=null&&ids!=""){
				ids = ids.substring(0,ids.length-1);
			}else{
				$.messager.alert('提示', "请选择角色");
				return ;
			}
			var dataJson = {
				"targettype_" : "R",
				"targetid_" : ids,
				"deployment_" : "<%=deployment%>",
				"access_" : "1",
				"type_" : "start"
			};
			var dataStr = JSON.stringify(dataJson);
			ajaxRequest("POST","dataVo="+dataStr,"platform/bpm/processAccessAction/addProcessRight","json","addProcessRightRoleBack");
			usd.close();
		}
	} ];
	usd.createButtonsInDialog(buttons);
	usd.show();
}
function addProcessRightRoleBack(result){
	if (result.flag!=null&&result.flag=="ok") {
		$.messager.show({title : '提示',msg : '保存成功'});
		$("#processRightRole").datagrid('reload'); 
	}else{
		$.messager.alert('提示', "保存失败");
	}
}
/**
 * 删除用户到流程启动权限
 */
function deleteProcessRole(){
	var datas = $('#processRightRole').datagrid('getSelections');
	if(datas == null){
		$.messager.alert("操作提示", "请您选择一条记录!","info");
		return;
	}
	var ids = '';
	for(var i=0;i<datas.length;i++){
		ids += datas[i].dbid_ + ',';
	}
	$.messager.confirm("操作提示", "您确认要删除选定的数据吗？", function (data) {
        if (data) {
        	easyuiMask();
        	ajaxRequest("POST","dbId="+ids,"platform/bpm/processAccessAction/deleteProcessRight","json","deleteProcessRoleRightBack");
        }
	 });
}
function  deleteProcessRoleRightBack(json){
    $.messager.show({title : '提示',msg : "删除成功!"});
    easyuiUnMask();
	$("#processRightRole").datagrid('reload'); 
}
//////////////////////////////////////////////////////////
/**
 * 获取流程群组权限 
 */
function getProcessGroupRight(){
	var dataGridHeight = $(".easyui-layout").height() - 85;
	$('#processRightGroup').datagrid({
		toolbar:[
				{
					text:'添加群组',
					iconCls:'icon-add',
					handler:function(){
						addProcessGroup();
					}
				},'-',{
					text:'删除群组',
					iconCls:'icon-remove',
					handler:function(){
						deleteProcessGroup();
					}
				},'-',{
					text:'刷新缓存',
					iconCls:'icon-reload',
					handler:function(){
						refreshCache();
					}
				}],
		url: 'platform/bpm/processAccessAction/getBpmAccessListByPage.json?deployment=<%=deployment%>&targetType='+targetType,
		width: '100%',
	    nowrap: false,
	    striped: true,
	    autoRowHeight:false,
	    singleSelect:false,
	    checkOnSelect:true,
	    remoteSort : false,
		height: dataGridHeight,
		fitColumns: true,
		sortName: 'dbid_',  //排序字段,当表格初始化时候的排序字段
		sortOrder: 'desc',    //定义排序顺序
		pagination:true,
		rownumbers:true,
		pageSize: 20,
		queryParams:{"":""},
		loadMsg:' 处理中，请稍候…',
		columns:[[
			{field:'dbid_',hidden:true},
			{field:'op',title:'操作',width:25,align:'left',checkbox:true},
			{field:'targetid_',title:'群组ID',width:50,align:'left'},
			{field:'targetName',title:'群组名称',width:50,align:'left'}
		]]
	});
	//设置分页控件   
	var p = $('#processRightGroup').datagrid('getPager');
	$(p).pagination({
	    pageSize: 10,//每页显示的记录条数，默认为10
	    pageList: [5,10,15,20,25,30],//可以设置每页记录条数的列表
	    beforePageText: '第',//页数文本框前显示的汉字
	    afterPageText: '页    共 {pages} 页',
	    displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
	});
}
/**
 * 增加群组到流程启动权限
 */
function addProcessGroup(){
	var usd = new UserSelectDialog('userSelectCommonDialog', 700, 400,
			getPath() + '/platform/user/bpmSelectUserAction/groupSelectCommon?isMultiple=true',
			'选择群组');
	var buttons = [ {
		text : '确定',
		id : 'processSubimt',
		//iconCls : 'icon-submit',
		handler : function() {
			var frmId = $('#userSelectCommonDialog iframe').attr('id');
			var frm = document.getElementById(frmId).contentWindow;
			var resultDatas = frm.getSelectedResultDataJson();
			var ids = "";
			for ( var i = 0; i < resultDatas.length; i++) {
				var resultData = resultDatas[i];
				ids = ids + resultData.groupId + ";";
			}
			if(ids!=null&&ids!=""){
				ids = ids.substring(0,ids.length-1);
			}else{
				$.messager.alert('提示', "请选择群组");
				return ;
			}
			var dataJson = {
				"targettype_" : "G",
				"targetid_" : ids,
				"deployment_" : "<%=deployment%>",
				"access_" : "1",
				"type_" : "start"
			};
			var dataStr = JSON.stringify(dataJson);
			ajaxRequest("POST","dataVo="+dataStr,"platform/bpm/processAccessAction/addProcessRight","json","addProcessRightGroupBack");
			usd.close();
		}
	} ];
	usd.createButtonsInDialog(buttons);
	usd.show();
}
function addProcessRightGroupBack(result){
	if (result.flag!=null&&result.flag=="ok") {
		$.messager.show({title : '提示',msg : '保存成功'});
		$("#processRightGroup").datagrid('reload'); 
	}else{
		$.messager.alert('提示', "保存失败");
	}
}
/**
 * 删除用户到流程启动权限
 */
function deleteProcessGroup(){
	var datas = $('#processRightGroup').datagrid('getSelections');
	if(datas == null){
		$.messager.alert("操作提示", "请您选择一条记录!","info");
		return;
	}
	var ids = '';
	for(var i=0;i<datas.length;i++){
		ids += datas[i].dbid_ + ',';
	}
	$.messager.confirm("操作提示", "您确认要删除选定的数据吗？", function (data) {
        if (data) {
        	easyuiMask();
        	ajaxRequest("POST","dbId="+ids,"platform/bpm/processAccessAction/deleteProcessRight","json","deleteProcessGroupRightBack");
        }
	 });
}
function  deleteProcessGroupRightBack(json){
    $.messager.show({title : '提示',msg : "删除成功!"});
    easyuiUnMask();
	$("#processRightGroup").datagrid('reload'); 
}
//////////////////////////////////////////////////////////
/**
 * 获取流程岗位权限 
 */
function getProcessPositionRight(){
	var dataGridHeight = $(".easyui-layout").height() - 85;
	$('#processRightPosition').datagrid({
		toolbar:[
				{
					text:'添加岗位',
					iconCls:'icon-add',
					handler:function(){
						addProcessPosition();
					}
				},'-',{
					text:'删除岗位',
					iconCls:'icon-remove',
					handler:function(){
						deleteProcessPosition();
					}
				},'-',{
					text:'刷新缓存',
					iconCls:'icon-reload',
					handler:function(){
						refreshCache();
					}
				}],
		url: 'platform/bpm/processAccessAction/getBpmAccessListByPage.json?deployment=<%=deployment%>&targetType='+targetType,
		width: '100%',
	    nowrap: false,
	    striped: true,
	    autoRowHeight:false,
	    singleSelect:false,
	    checkOnSelect:true,
	    remoteSort : false,
		height: dataGridHeight,
		fitColumns: true,
		sortName: 'dbid_',  //排序字段,当表格初始化时候的排序字段
		sortOrder: 'desc',    //定义排序顺序
		pagination:true,
		rownumbers:true,
		pageSize: 20,
		queryParams:{"":""},
		loadMsg:' 处理中，请稍候…',
		columns:[[
			{field:'dbid_',hidden:true},
			{field:'op',title:'操作',width:25,align:'left',checkbox:true},
			{field:'targetid_',title:'岗位ID',width:50,align:'left'},
			{field:'targetName',title:'岗位名称',width:50,align:'left'}
		]]
	});
	//设置分页控件   
	var p = $('#processRightPosition').datagrid('getPager');
	$(p).pagination({
	    pageSize: 10,//每页显示的记录条数，默认为10
	    pageList: [5,10,15,20,25,30],//可以设置每页记录条数的列表
	    beforePageText: '第',//页数文本框前显示的汉字
	    afterPageText: '页    共 {pages} 页',
	    displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
	});
}
/**
 * 增加岗位到流程启动权限
 */
function addProcessPosition(){
	var usd = new UserSelectDialog('userSelectCommonDialog', 700, 400,
			getPath() + '/platform/user/bpmSelectUserAction/positionSelectCommon?isMultiple=true',
			'选择岗位');
	var buttons = [ {
		text : '确定',
		id : 'processSubimt',
		//iconCls : 'icon-submit',
		handler : function() {
			var frmId = $('#userSelectCommonDialog iframe').attr('id');
			var frm = document.getElementById(frmId).contentWindow;
			var resultDatas = frm.getSelectedResultDataJson();
			var ids = "";
			for ( var i = 0; i < resultDatas.length; i++) {
				var resultData = resultDatas[i];
				ids = ids + resultData.positionId + ";";
			}
			if(ids!=null&&ids!=""){
				ids = ids.substring(0,ids.length-1);
			}else{
				$.messager.alert('提示', "请选择岗位");
				return ;
			}
			var dataJson = {
				"targettype_" : "P",
				"targetid_" : ids,
				"deployment_" : "<%=deployment%>",
				"access_" : "1",
				"type_" : "start"
			};
			var dataStr = JSON.stringify(dataJson);
			ajaxRequest("POST","dataVo="+dataStr,"platform/bpm/processAccessAction/addProcessRight","json","addProcessRightPositionBack");
			usd.close();
		}
	} ];
	usd.createButtonsInDialog(buttons);
	usd.show();
}
function addProcessRightPositionBack(result){
	if (result.flag!=null&&result.flag=="ok") {
		$.messager.show({title : '提示',msg : '保存成功'});
		$("#processRightPosition").datagrid('reload'); 
	}else{
		$.messager.alert('提示', "保存失败");
	}
}
/**
 * 删除岗位到流程启动权限
 */
function deleteProcessPosition(){
	var datas = $('#processRightPosition').datagrid('getSelections');
	if(datas == null){
		$.messager.alert("操作提示", "请您选择一条记录!","info");
		return;
	}
	var ids = '';
	for(var i=0;i<datas.length;i++){
		ids += datas[i].dbid_ + ',';
	}
	$.messager.confirm("操作提示", "您确认要删除选定的数据吗？", function (data) {
        if (data) {
        	easyuiMask();
        	ajaxRequest("POST","dbId="+ids,"platform/bpm/processAccessAction/deleteProcessRight","json","deleteProcessPositionRightBack");
        }
	 });
}
function  deleteProcessPositionRightBack(json){
    $.messager.show({title : '提示',msg : "删除成功!"});
    easyuiUnMask();
	$("#processRightPosition").datagrid('reload'); 
}
function refreshCache(){
	ajaxRequest("POST","flowId=<%=deployment%>","platform/bpm/processAccessAction/reLoadCache","json","afterRefreshCache");
}
function afterRefreshCache(json){
	if(json.success == true){
		 $.messager.show({title : '提示',msg : "刷新缓存成功!"});
	}else{
		 $.messager.show({title : '提示',msg : "刷新缓存失败!"});
	}
}
</script>
<body class="easyui-layout" fit="true"> 
<div region="center" border="false" style="overflow: hidden;">
	<div class="easyui-tabs" id="processAnalysis">
    	<div title="用户" style="padding:10px;width:auto">
    		<table id="processRightUser"></table>	
    	</div>
    	 <div title="部门" style="padding:10px;width:auto">	
    	 <table id="processRightDept"></table>
    	</div>
    	<div title="角色" style="padding:10px;width:auto">	
    	<table id="processRightRole"></table>
    	</div>
    	<div title="群组" style="padding:10px;width:auto">	
    	<table id="processRightGroup"></table>
    	</div>
    	<div title="岗位" style="padding:10px;width:auto">
    	<table id="processRightPosition"></table>	
    	</div>
	</div>
</div>
</body>
</html>