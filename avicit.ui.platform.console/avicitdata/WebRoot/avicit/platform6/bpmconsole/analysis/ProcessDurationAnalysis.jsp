<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>流程实例信息</title>
<base href="<%=ViewUtil.getRequestPath(request) %>">
<jsp:include page="/avicit/platform6/bpmclient/bpm/ProcessCommonJsInclude.jsp"></jsp:include>
<script src="static/js/platform/component/fusionchar/js/FusionCharts.js" type="text/javascript"></script>
</head>

<script type="text/javascript">
var baseurl = '<%=request.getContextPath()%>';
var tabSelectedIndex = "0"; //那个标签被选中
/**
 * 页面请求入口
 */
$(function(){
	loadProcessTree();
	processAnalysisBegin();
	window.setTimeout("expand()", 500);
});
/**
 * 加载流程业务目录树
 */
function loadProcessTree(){
	$('#mytree').tree({   
		checkbox: false,   
		lines : true,
		method : 'post',
	    url:'platform/bpm/processAnalysisAction/getPrcessEntryTree.json?id=root',  
	    onBeforeExpand:function(node,param){  
	    	 $('#mytree').tree('options').url = "platform/bpm/processAnalysisAction/getPrcessEntryTree.json?id=" + node.id ;
	    },
	    onClick:function(node){
            clickTree(node);
      	}
	});  
}
/**
 * 点击树节点操作
 */
function clickTree(node) {
	$('#processDurationTop10_catalogId').val(node.id);
	$('#processStartInstanceTop10_catalogId').val(node.id);
	$('#taskActDurationTop10_catalogId').val(node.id);
	$('#autoActDurationTop10_catalogId').val(node.id);
	$('#processException_catalogId').val(node.id);
	expand();
	if(tabSelectedIndex=="0"){
		searchProcessDurationTop10();
	}else if(tabSelectedIndex=="1"){
		searchProcessStartInstanceTop10();
	}else if(tabSelectedIndex=="2"){
		searchTaskActDurationTop10();
	}else if(tabSelectedIndex=="3"){
		searchAutoActDurationTop10();
	}else if(tabSelectedIndex=="4"){
		searchProcessException();
	}
	
	
}
/**
 * 展开树节点操作
 */
function expand() {
	var node = $('#mytree').tree('getSelected');
	if(node){
		$('#mytree').tree('expand',node.target);
	}else{
		$('#mytree').tree('expandAll');
	}
}
/**
 * 流程统计分析入口
 */
function processAnalysisBegin(){
	$('#processAnalysis').tabs({
	    onSelect:function(title,index){
	       if(index == 0){
	    	   tabSelectedIndex = "0";
	    	   searchProcessDurationTop10();
	       }
		   if(index == 1){
			   tabSelectedIndex = "1";
			   searchProcessStartInstanceTop10();
			}
			if(index == 2){
				tabSelectedIndex = "2";
				searchTaskActDurationTop10();
			}
			if(index == 3){
				tabSelectedIndex = "3";
				searchAutoActDurationTop10();
			}
			if(index == 4){
				tabSelectedIndex = "4";
				searchProcessException();
			}
	    }   
	});
	searchProcessDurationTop10();
}
/**
 * 流程耗时统计查询
 */
function searchProcessDurationTop10(){
	var startDateBegin =$('#processDurationTop10_startDateBegin').datetimebox('getValue');
	var startDateEnd =$('#processDurationTop10_startDateEnd').datetimebox('getValue');
	var catalogId = $('#processDurationTop10_catalogId').val();
	var queryParas = "catalogId="+catalogId+"&startDateBegin="+startDateBegin+"&startDateEnd="+startDateEnd;
	getProcessDurationTop10(queryParas);
}
/**
 * 清空流程耗时查询条件
 */
function clearProcessDurationTop10(){
	 $('#processDurationTop10_catalogId').val('');
	 $('#processDurationTop10_startDateBegin').datebox("setValue","");
	 $('#processDurationTop10_startDateEnd').datebox("setValue","");
}
/**
 * 流程耗时统计ajax请求
 */
function getProcessDurationTop10(queryParas){
	 ajaxRequest("POST",queryParas,"platform/bpm/processAnalysisAction/getProcessDurationTop10","json","getProcessDurationTop10Back");
}
/**
 * 流程耗时统计ajax请求回调
 */
function getProcessDurationTop10Back(json){
		var chart = new FusionCharts("static/js/platform/component/fusionchar/js/Column2D.swf", "ChartId", "100%", "380", "0", "0");
	    var strXML = "<chart palette='2' caption='流程平均耗时最长TOP10' xAxisName='流程名称' yAxisName='耗时（分钟）'  baseFont='微软雅黑'  showValues='1'  decimalPrecision='0' formatNumberScale='0'>";
	    var processDurationArray = json.rows;
		for (var i = 0; i < processDurationArray.length; i++) {
			var processDuration = processDurationArray[i];
		    strXML += "<set label='"+processDuration.pdName+"' value='"+processDuration.duration+"' link='JavaScript:openProcessInstancePage(\""+processDuration.pdId+"\")' />" ;
		}
        strXML += "</chart>";
        chart.setDataXML(strXML);
        chart.render("processDurationTop10Div");
}
/**
 * 流程耗时统计钻取
 */
function openProcessInstancePage(pdId){
	var url = getPath()+"/avicit/platform6/bpmconsole/analysis/ProcessInstanceList.jsp";
	var startDateBegin =$('#processDurationTop10_startDateBegin').datetimebox('getValue');
	var startDateEnd =$('#processDurationTop10_startDateEnd').datetimebox('getValue');
	url += "?pdId="+pdId+"&startDateBegin="+startDateBegin+"&startDateEnd="+startDateEnd;
	top.addTab(pdId+"流程实例",url,"dorado/client/skins/~current/common/icons.gif","ProcessInstanceList"," -0px -120px");
	//window.open(encodeURI(url),"流程实例信息","scrollbars=no,status=yes,resizable=no,top=0,left=0,width=700,height=400");
}
/**
 * 流程耗时统计
 */
function openProcessInstanceDurationList(){
	var url = getPath()+"/avicit/platform6/bpmconsole/analysis/ProcessInstanceDurationList.jsp";
	var catalogId = $('#processDurationTop10_catalogId').val();
	var startDateBegin =$('#processDurationTop10_startDateBegin').datetimebox('getValue');
	var startDateEnd =$('#processDurationTop10_startDateEnd').datetimebox('getValue');
	url += "?catalogId="+catalogId+"&startDateBegin="+startDateBegin+"&startDateEnd="+startDateEnd;
	top.addTab("流程耗时统计",url,"dorado/client/skins/~current/common/icons.gif","ProcessInstanceDurationList"," -0px -120px");
}

////////////////////////////////////////////////////////////////
/**
 * 流程启动最多统计查询
 */
function searchProcessStartInstanceTop10(){
	var startDateBegin =$('#processStartInstanceTop10_startDateBegin').datetimebox('getValue');
	var startDateEnd =$('#processStartInstanceTop10_startDateEnd').datetimebox('getValue');
	var catalogId = $('#processStartInstanceTop10_catalogId').val();
	var queryParas = "catalogId="+catalogId+"&startDateBegin="+startDateBegin+"&startDateEnd="+startDateEnd;
	getProcessStartInstanceTop10(queryParas);
}

/**
 * 流程启动最多统计ajax请求
 */
function getProcessStartInstanceTop10(queryParas){
	 ajaxRequest("POST",queryParas,"platform/bpm/processAnalysisAction/getProcessStartInstanceTop10","json","getProcessStartInstanceTop10Back");
}
/**
 * 流程耗时统计ajax请求回调
 */
function getProcessStartInstanceTop10Back(json){
		var chart = new FusionCharts("static/js/platform/component/fusionchar/js/Column2D.swf", "ChartId", "100%", "380", "0", "0");
	    var strXML = "<chart palette='2' caption='流程启动实例最多TOP10' xAxisName='流程名称' yAxisName='流 程 实 例 数'  baseFont='微软雅黑'  showValues='1'  decimalPrecision='0' formatNumberScale='0'>";
	    var processDurationArray = json.rows;
		for (var i = 0; i < processDurationArray.length; i++) {
			var processDuration = processDurationArray[i];
		    strXML += "<set label='"+processDuration.pdName+"' value='"+processDuration.instanceNum+"' link='JavaScript:openProcessStartInstancePage(\""+processDuration.pdId+"\")' />" ;
		}
        strXML += "</chart>";
        chart.setDataXML(strXML);
        chart.render("processStartInstanceTop10Div");
}
/**
 * 清空流程启动最多统计查询条件
 */
function clearProcessStartInstanceTop10(){
	 $('#processStartInstanceTop10_catalogId').val('');
	 $('#processStartInstanceTop10_startDateBegin').datebox("setValue","");
	 $('#processStartInstanceTop10_startDateEnd').datebox("setValue","");
}
/**
 * 流程启动最多统计钻取
 */
function openProcessStartInstancePage(pdId){
	var url = getPath()+"/avicit/platform6/bpmconsole/analysis/ProcessInstanceStartList.jsp";
	var startDateBegin =$('#processStartInstanceTop10_startDateBegin').datetimebox('getValue');
	var startDateEnd =$('#processStartInstanceTop10_startDateEnd').datetimebox('getValue');
	url += "?pdId="+pdId+"&startDateBegin="+startDateBegin+"&startDateEnd="+startDateEnd;
	top.addTab(pdId+"流程启动实例",url,"dorado/client/skins/~current/common/icons.gif","ProcessInstanceStartList"," -0px -120px");
}
/**
 * 流程启动最多列表统计
 */
function openProcessStartInstanceList(){
	var url = getPath()+"/avicit/platform6/bpmconsole/analysis/ProcessInstanceStartAllList.jsp";
	var catalogId = $('#processStartInstanceTop10_catalogId').val();
	var startDateBegin =$('#processStartInstanceTop10_startDateBegin').datetimebox('getValue');
	var startDateEnd =$('#processStartInstanceTop10_startDateEnd').datetimebox('getValue');
	url += "?catalogId="+catalogId+"&startDateBegin="+startDateBegin+"&startDateEnd="+startDateEnd;
	top.addTab("流程启动数量统计",url,"dorado/client/skins/~current/common/icons.gif","ProcessInstanceStartAllList"," -0px -120px");
}
/////////////////////////////////////////////////////////////////////////////////////////
/**
 * 流程人工活动耗时最长查询
 */
function searchTaskActDurationTop10(){
	var startDateBegin =$('#taskActDurationTop10_startDateBegin').datetimebox('getValue');
	var startDateEnd =$('#taskActDurationTop10_startDateEnd').datetimebox('getValue');
	var catalogId = $('#taskActDurationTop10_catalogId').val();
	var queryParas = "catalogId="+catalogId+"&startDateBegin="+startDateBegin+"&startDateEnd="+startDateEnd;
	getTaskActDurationTop10(queryParas);
}
/**
 * 清空流程人工活动耗时最长查询条件
 */
function clearTaskActDurationTop10(){
	 $('#taskActDurationTop10_catalogId').val('');
	 $('#taskActDurationTop10_startDateBegin').datebox("setValue","");
	 $('#taskActDurationTop10_startDateEnd').datebox("setValue","");
}
/**
 * 流程人工活动耗时最长ajax请求
 */
function getTaskActDurationTop10(queryParas){
	 ajaxRequest("POST",queryParas,"platform/bpm/processAnalysisAction/getTaskActDurationTop10","json","getTaskActDurationTop10Back");
}
/**
 * 流程人工活动耗时最长ajax请求回调
 */
function getTaskActDurationTop10Back(json){
		var chart = new FusionCharts("static/js/platform/component/fusionchar/js/Column2D.swf", "ChartId", "100%", "380", "0", "0");
	    var strXML = "<chart palette='2' caption='流程人工活动耗时最长TOP10' xAxisName='流程节点名称' yAxisName='耗时（分钟）'  baseFont='微软雅黑'  showValues='1'  decimalPrecision='2' formatNumberScale='0'>";
	    var processDurationArray = json.rows;
		for (var i = 0; i < processDurationArray.length; i++) {
			var bpmActinstConsoleV = processDurationArray[i];
			var x = bpmActinstConsoleV.pdname +"\n("+ bpmActinstConsoleV.activityname+")";
		    strXML += "<set label='"+x+"' value='"+bpmActinstConsoleV.duration+"' link='JavaScript:openTaskActDurationTop10Page(\""+bpmActinstConsoleV.pdid+"\",\""+bpmActinstConsoleV.activityname+"\")' />" ;
		}
        strXML += "</chart>";
        chart.setDataXML(strXML);
        chart.render("taskActDurationTop10Div");
}
/**
 * 流程人工活动耗时最长钻取
 */
function openTaskActDurationTop10Page(pdId,activityName){
	var url = getPath()+"/avicit/platform6/bpmconsole/analysis/TaskActDurationList.jsp";
	var startDateBegin =$('#taskActDurationTop10_startDateBegin').datetimebox('getValue');
	var startDateEnd =$('#taskActDurationTop10_startDateEnd').datetimebox('getValue');
	url += "?pdId="+pdId+"&activityName="+activityName+"&startDateBegin="+startDateBegin+"&startDateEnd="+startDateEnd;
	top.addTab(pdId+"人工活动耗时",url,"dorado/client/skins/~current/common/icons.gif","ProcessInstanceStartList"," -0px -120px");
}
//////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////
/**
 * 流程自动活动耗时最长查询
 */
function searchAutoActDurationTop10(){
	var startDateBegin =$('#autoActDurationTop10_startDateBegin').datetimebox('getValue');
	var startDateEnd =$('#autoActDurationTop10_startDateEnd').datetimebox('getValue');
	var catalogId = $('#autoActDurationTop10_catalogId').val();
	var queryParas = "catalogId="+catalogId+"&startDateBegin="+startDateBegin+"&startDateEnd="+startDateEnd;
	getAutoActDurationTop10(queryParas);
}
/**
 * 清空流程自动活动耗时最长查询条件
 */
function clearAutoActDurationTop10(){
	 $('#autoActDurationTop10_catalogId').val('');
	 $('#autoActDurationTop10_startDateBegin').datebox("setValue","");
	 $('#autoActDurationTop10_startDateEnd').datebox("setValue","");
}
/**
 * 流程自动活动耗时最长ajax请求
 */
function getAutoActDurationTop10(queryParas){
	 ajaxRequest("POST",queryParas,"platform/bpm/processAnalysisAction/getAutoActDurationTop10","json","getAutoActDurationTop10Back");
}
/**
 * 流程自动活动耗时最长ajax请求回调
 */
function getAutoActDurationTop10Back(json){
		var chart = new FusionCharts("static/js/platform/component/fusionchar/js/Column2D.swf", "ChartId", "100%", "380", "0", "0");
	    var strXML = "<chart palette='2' caption='流程自动活动耗时最长TOP10' xAxisName='流程节点名称' yAxisName='耗时（毫秒）'  baseFont='微软雅黑'  showValues='1'  decimalPrecision='0' formatNumberScale='0'>";
	    var processDurationArray = json.rows;
		for (var i = 0; i < processDurationArray.length; i++) {
			var bpmActinstConsoleV = processDurationArray[i];
			var x = bpmActinstConsoleV.pdname +"\n("+ bpmActinstConsoleV.activityname+")";
		    strXML += "<set label='"+x+"' value='"+bpmActinstConsoleV.duration+"' link='JavaScript:openAutoActDurationTop10Page(\""+bpmActinstConsoleV.pdid+"\",\""+bpmActinstConsoleV.activityname+"\")' />" ;
		}
        strXML += "</chart>";
        chart.setDataXML(strXML);
        chart.render("autoActDurationTop10Div");
}
/**
 * 流程自动活动耗时最长钻取
 */
function openAutoActDurationTop10Page(pdId,activityName){
	var url = getPath()+"/avicit/platform6/bpmconsole/analysis/AutoActDurationList.jsp";
	var startDateBegin =$('#autoActDurationTop10_startDateBegin').datetimebox('getValue');
	var startDateEnd =$('#autoActDurationTop10_startDateEnd').datetimebox('getValue');
	url += "?pdId="+pdId+"&activityName="+activityName+"&startDateBegin="+startDateBegin+"&startDateEnd="+startDateEnd;
	top.addTab(pdId+"流程活动耗时",url,"dorado/client/skins/~current/common/icons.gif","ProcessInstanceStartList"," -0px -120px");
}
/////////////////////////////////////////////////////////////////////////////////////////
/**
 * 查询流程异常
 */
function searchProcessException(){
	var startDateBegin =$('#processException_startDateBegin').datetimebox('getValue');
	var startDateEnd =$('#processException_startDateEnd').datetimebox('getValue');
	var catalogId = $('#processException_catalogId').val();
	var queryParas = "catalogId="+catalogId+"&startDateBegin="+startDateBegin+"&startDateEnd="+startDateEnd;
	getProcessException(queryParas);
}
/**
 * 清空流程异常查询条件
 */
function clearProcessException(){
	 $('#processException_catalogId').val('');
	 $('#processException_startDateEnd').datebox("setValue","");
	 $('#autoActDurationTop10_startDateEnd').datebox("setValue","");
}
/**
 * 流程异常查询ajax请求
 */
function getProcessException(queryParas){
	var dataGridHeight = $(".easyui-layout").height()-140;
	$('#processExceptionTable').datagrid({
		toolbar:[{
						text:'删除',
						iconCls:'icon-no',
						handler:function(){
							deleteExceptionInfo();
						}
				}],
		url: 'platform/bpm/processAnalysisAction/getProcessExceptionListByPage.json?'+queryParas,
		width: '100%',
	    nowrap: false,
	    striped: true,
	    autoRowHeight:false,
	    singleSelect:false,
	    checkOnSelect:true,
	    remoteSort : false,
		height: dataGridHeight,
		fitColumns: true,
		sortName: 'time_',  //排序字段,当表格初始化时候的排序字段
		sortOrder: 'desc',    //定义排序顺序
		pagination:true,
		pageSize:20,
		rownumbers:true,
		queryParams:{"":""},
		loadMsg:' 处理中，请稍候…',
		columns:[[
					{field:'dbid_',hidden:true},
					{field:'op',title:'操作',width:25,align:'left',checkbox:true},
					{field:'pdname_',title:'流程名称',width:80,align:'left'},
					{field:'instanceid_',title:'异常实例ID',width:30,align:'left',sortable:true},
					{field:'activityid_',title:'异常节点ID',width:30,align:'left',sortable:true},
					{field:'activityname_',title:'异常节点名称',width:60,align:'left',sortable:true},
					{field:'username_',title:'操作人姓名',width:30,align:'left',sortable:true},
					{field:'deptname_',title:'操作人部门',width:60,align:'left',sortable:true},
					{field:'time_',title:'异常产生时间',width:50,align:'left',sortable:true,
		  				formatter:function(value,rec){
		  					var startdateMi=rec.time_;
		  					if(startdateMi==undefined){
		  						return;
		  					}
		  					var newDate=new Date(startdateMi);
		  					return newDate.Format("yyyy-MM-dd hh:mm:ss");   
					}},
					{field:'op2',title:'查看异常信息',width:30,align:'center',sortable:true,
			  				formatter:function(value,rec){
			  					return '<img src="static/images/platform/bpm/client/images/trackTask.gif" style="cursor:pointer"  onclick="javascript:window.lookExceptionInfo(\''+rec.dbid_+'\')"/>';
					}}
				]]
	});
	//设置分页控件   
	var p = $('#processExceptionTable').datagrid('getPager');
	$(p).pagination({
	    pageSize: 20,//每页显示的记录条数，默认为10
	    pageList: [5,10,15,20,25,30],//可以设置每页记录条数的列表
	    beforePageText: '第',//页数文本框前显示的汉字
	    afterPageText: '页    共 {pages} 页',
	    displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
	});
}
/**
 * 查看异常信息
 */
function lookExceptionInfo(dbid_){
	 ajaxRequest("POST","dbId="+dbid_,"platform/bpm/processAnalysisAction/getProcessExceptionById","json","getProcessExceptionByIdBack");
	
}
function  getProcessExceptionByIdBack(json){
	$('#processExceptionInfoDiv').html(json.rows.exception_);
	$('#processExceptionInfoDiv').dialog({   
	    modal:true  
	});  
}
/**
 * 删除异常信息
 */
function deleteExceptionInfo(){
	var datas = $('#processExceptionTable').datagrid('getSelections');
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
        	ajaxRequest("POST","dbId="+ids,"platform/bpm/processAnalysisAction/deleteProcessException","json","deleteProcessExceptionBack");
        }
	 });
}
function  deleteProcessExceptionBack(json){
    $.messager.show({title : '提示',msg : "删除成功!"});
    easyuiUnMask();
	$("#processExceptionTable").datagrid('reload'); 
}

var processWaringDialog;
//添加流程预警
function doAddProcessWaring(id) {
	var para = "";
	if(id!=null&&id=="TaskActDurationTop10"){
		 para = "?isShowSendType01=no&waringType="+id+"&begin=流程人工活动耗时大于&end=分钟";
	}else if(id=="AutoActDurationTop10"){
		 para = "?isShowSendType01=no&waringType="+id+"&begin=流程自动活动耗时大于&end=毫秒";
	}else if(id=="ProcessException"){
		 para = "?isShowSendType01=no&waringType="+id+"&begin=流程异常条数大于&end=条";
	}
	
	var usd = new UserSelectDialog("processWaringDialog","450","250",getPath()+"/avicit/platform6/bpmconsole/analysis/ProcessWaringAdd.jsp"+encodeURI(para),"流程预警");
	processWaringDialog = usd;
	var buttons = [{
		text:'提交',
		id : 'processSubimt',
		//iconCls : 'icon-submit',
		handler:function(){
			 var frmId = $('#processWaringDialog iframe').attr('id');
			 var frm = document.getElementById(frmId).contentWindow;
			 var dataVo = frm.$('#processWaringForm').serializeArray();
			 var dataJson =frm.convertToJson(dataVo);
			 	 dataVo = JSON.stringify(dataJson);
			 ajaxRequest("POST","dataVo="+dataVo,"platform/bpm/bpmconsole/processUserAnalysisAction/insertProcessWaring","json","insertProcessWaringBack");
		}
	}];
	usd.createButtonsInDialog(buttons);
	usd.show();
}

//添加流程预警返回函数
function insertProcessWaringBack(result){
	try {
		if (result.success) {
			$.messager.show({
				title : '提示',
				msg : result.msg
			});
			processWaringDialog.close();
		} else {
			$.messager.alert('提示', result.msg);
		}
	} catch (e) {
		$.messager.alert('提示', result.msg);
	}
}

</script>
<body class="easyui-layout" fit="true">
<div data-options="region:'west',title:'流程业务目录',split:false" style="width:200px;overflow: auto;">
	<ul id="mytree"></ul>  
</div>  
<div region="center" border="false" style="overflow: hidden;">
		<div class="easyui-tabs" id="processAnalysis">
			<div title="流程平均耗时最长TOP10" style="padding:10px;width:auto">
				<div class="datagrid-toolbar" style="height:auto;display: block;">
				<fieldset>
					<legend>查询条件</legend>
					<table class="tableForm" width='100%' align="left">
						<tr>
							<td style="width:70px" align="left">&nbsp;开始时间：</td>
							<td style="width:160px" align="left"><input name="startDateBegin" id="processDurationTop10_startDateBegin" class="easyui-datebox" editable="false" style="width:150px;" /></td>
							<td style="width:70px" align="left">&nbsp;结束时间：</td>
							<td style="width:160px" align="left"><input name="startDateEnd" id="processDurationTop10_startDateEnd"  class="easyui-datebox" editable="false" style="width: 150px;" /></td>
							<td  align="left">
							<input  type="hidden"  name="catalogId" id="processDurationTop10_catalogId"     />
							<a class="easyui-linkbutton"   iconCls="icon-search" plain="true" onclick="searchProcessDurationTop10();" href="javascript:void(0);">查询</a><a class="easyui-linkbutton" iconCls="icon-cancel" plain="true" onclick="clearProcessDurationTop10();" href="javascript:void(0);">清空</a>
							</td>
						</tr>
					</table>
				</fieldset>
				</div>
				<br>
				<div id="processDurationTop10Div"></div>
				<div><img src="static/images/platform/common/icons/table.png" title="列表统计" onclick="openProcessInstanceDurationList()" style="cursor:pointer"/></div>
			</div>
			<div title="流程启动实例最多TOP10" style="padding:10px;width:auto">
				<div class="datagrid-toolbar" style="height:auto;display: block;">
					<fieldset>
					<legend>查询条件</legend>
					<table class="tableForm"  width='100%' align="left">
						<tr>
							<td style="width:70px" align="left">&nbsp;开始时间：</td>
							<td style="width:160px" align="left"><input name="startDateBegin" id="processStartInstanceTop10_startDateBegin" class="easyui-datebox" editable="false" style="width:150px;" /></td>
							<td style="width:70px" align="left">&nbsp;结束时间：</td>
							<td style="width:160px" align="left"><input name="startDateEnd" id="processStartInstanceTop10_startDateEnd"  class="easyui-datebox" editable="false" style="width: 150px;" /></td>
							<td  align="left">
							<input  type="hidden"  name="catalogId" id="processStartInstanceTop10_catalogId"     />
							<a class="easyui-linkbutton"   iconCls="icon-search" plain="true" onclick="searchProcessStartInstanceTop10();" href="javascript:void(0);">查询</a><a class="easyui-linkbutton" iconCls="icon-cancel" plain="true" onclick="clearProcessStartInstanceTop10();" href="javascript:void(0);">清空</a>
							</td>
						</tr>
					</table>
					</fieldset>
				</div>
				<br>
				<div id="processStartInstanceTop10Div"></div>
				<div><img src="static/images/platform/common/icons/table.png" title="列表统计" onclick="openProcessStartInstanceList()" style="cursor:pointer"/></div>
			</div>
			<div title="流程人工活动耗时最长TOP10" style="padding:10px;width:auto">
				<div class="datagrid-toolbar" style="height:auto;display: block;">
					<fieldset>
					<legend>查询条件</legend>
					<table class="tableForm"  width='100%' align="left">
						<tr>
							<td style="width:70px" align="left">&nbsp;开始时间：</td>
							<td style="width:160px" align="left"><input name="startDateBegin" id="taskActDurationTop10_startDateBegin" class="easyui-datebox" editable="false" style="width:150px;" /></td>
							<td style="width:70px" align="left">&nbsp;结束时间：</td>
							<td style="width:160px" align="left"><input name="startDateEnd" id="taskActDurationTop10_startDateEnd"  class="easyui-datebox" editable="false" style="width: 150px;" /></td>
							<td  align="left">
							<input  type="hidden"  name="catalogId" id="taskActDurationTop10_catalogId"     />
							<a class="easyui-linkbutton"   iconCls="icon-search" plain="true" onclick="searchTaskActDurationTop10();" href="javascript:void(0);">查询</a><a class="easyui-linkbutton" iconCls="icon-cancel" plain="true" onclick="clearTaskActDurationTop10();" href="javascript:void(0);">清空</a>
							</td>
						</tr>
					</table>
					</fieldset>
				</div>
				<br>
				<div id="taskActDurationTop10Div"></div>
				<div>
					<img src="static/images/platform/common/performance_warn.gif" title="流程预警" onclick="doAddProcessWaring('TaskActDurationTop10')" style="cursor:pointer"/>
				</div>
			</div>
			<div title="流程自动活动耗时最长TOP10" style="padding:10px;width:auto">
				<div class="datagrid-toolbar" style="height:auto;display: block;">
					<fieldset>
					<legend>查询条件</legend>
					<table class="tableForm"  width='100%' align="left">
						<tr>
							<td style="width:70px" align="left">&nbsp;开始时间：</td>
							<td style="width:160px" align="left"><input name="startDateBegin" id="autoActDurationTop10_startDateBegin" class="easyui-datebox" editable="false" style="width:150px;" /></td>
							<td style="width:70px" align="left">&nbsp;结束时间：</td>
							<td style="width:160px" align="left"><input name="startDateEnd" id="autoActDurationTop10_startDateEnd"  class="easyui-datebox" editable="false" style="width: 150px;" /></td>
							<td  align="left">
							<input  type="hidden"  name="catalogId" id="autoActDurationTop10_catalogId"     />
							<a class="easyui-linkbutton"   iconCls="icon-search" plain="true" onclick="searchAutoActDurationTop10();" href="javascript:void(0);">查询</a><a class="easyui-linkbutton" iconCls="icon-cancel" plain="true" onclick="clearAutoActDurationTop10();" href="javascript:void(0);">清空</a>
							</td>
						</tr>
					</table>
					</fieldset>
				</div>
				<br>
				<div id="autoActDurationTop10Div"></div>
				<div>
					<img src="static/images/platform/common/performance_warn.gif" title="流程预警" onclick="doAddProcessWaring('AutoActDurationTop10')" style="cursor:pointer"/>
				</div>
			</div>
    		<div title="流程异常统计" style="padding:10px;width:auto">
    			<div class="datagrid-toolbar" style="height:auto;display: block;">
					<fieldset>
					<legend>查询条件</legend>
					<table class="tableForm"  width='100%' align="left">
						<tr>
							<td style="width:70px" align="left">&nbsp;开始时间：</td>
							<td style="width:160px" align="left"><input name="startDateBegin" id="processException_startDateBegin" class="easyui-datebox" editable="false" style="width:150px;" /></td>
							<td style="width:70px" align="left">&nbsp;结束时间：</td>
							<td style="width:160px" align="left"><input name="startDateEnd" id="processException_startDateEnd"  class="easyui-datebox" editable="false" style="width: 150px;" /></td>
							<td  align="left">
							<input  type="hidden"  name="catalogId" id="processException_catalogId"     />
							<a class="easyui-linkbutton"   iconCls="icon-search" plain="true" onclick="searchProcessException();" href="javascript:void(0);">查询</a><a class="easyui-linkbutton" iconCls="icon-cancel" plain="true" onclick="clearProcessException();" href="javascript:void(0);">清空</a>
							</td>
						</tr>
					</table>
					</fieldset>
				</div>
				<br>
    			<div><table id="processExceptionTable"></table></div>
    			 <div>
					<img src="static/images/platform/common/performance_warn.gif" title="流程预警" onclick="doAddProcessWaring('ProcessException')" style="cursor:pointer"/>
				</div>
    			<div id="processExceptionInfoDiv" title="异常详细信息" style="width:700px;height:400px;"></div> 

    		</div>
	</div>
</div>

</body>
</html>