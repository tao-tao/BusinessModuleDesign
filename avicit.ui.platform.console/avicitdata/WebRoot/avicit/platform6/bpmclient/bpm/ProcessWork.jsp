<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<%
	String userId = (String)session.getAttribute("userId");
	String tab = request.getParameter("tab");
	String startDate = request.getParameter("startDate");
	String endDate = request.getParameter("endDate");
%>
<base href="<%=ViewUtil.getRequestPath(request) %>">
<jsp:include page="/avicit/platform6/bpmclient/bpm/ProcessCommonJsInclude.jsp"></jsp:include>
</head>

<script type="text/javascript">
var userId = '<%=userId%>';
var baseurl = '<%=request.getContextPath()%>';

$(function(){
	$('#processWork').tabs({
	    onSelect:function(title,index){
			if(index == 0){//未办工作
				searchTodoFun();
			}
		    if(index == 1){//已办工作
		    	searchFinishedTodoFun();
		    }
	    }   
	});
	
	var tab = '<%=tab%>';
	if (tab != "undefind" && tab != "" && tab != "null") {
		if (tab == "Finish") {
			$("#FinishedTodo_endDateBegin").datebox("setValue","<%=startDate%>"); 
			$("#FinishedTodo_endDateEnd").datebox("setValue","<%=endDate%>"); 
		}
		if (tab == "Todo") $('#processWork').tabs("select" , 0);
		if (tab == "Finish") $('#processWork').tabs("select" , 1);
	}
});

function loadTodo(queryParams){
	$('#Todo_data').datagrid({   
	    height: 'auto',
	    nowrap: false,
	    striped: true,
	    url:'platform/bpm/clientbpmdisplayaction/gettodo.json',
	    autoRowHeight:false,
	    idField:'id',
	    singleSelect:false,//是否单选 
	    pagination:true,//分页控件
	    rownumbers:true,//行号
	    queryParams:queryParams,
	    loadMsg:' 处理中，请稍候…',
	    fitColumns: true,
	    columns:[[
	  			{field:'processDefName',title:'流程名称',width:50,align:'left',
	  				formatter:function(value,rec){
	  					if(value!=null&&value.length>10){
	  						value = value.substring(0,10)+"...";
	  					}
	  					return value;
	  				}},
	  			{field:'taskTitle',title:'标题',width:50,align:'left',
	  				formatter:function(value,rec){
	  					var processInstance = "'"+rec.processInstance+"'";
	  					var executionId = "'"+rec.executionId+"'";
	  					var dbid = "'"+rec.dbid+"'";
	  					var businessId = "'"+rec.businessId+"'";
	  					var url = "'"+rec.formResourceName+"'";
	  					var title = "'"+rec.taskTitle+"'";
	  					if(value!=null&&value.length>10){
	  						value = value.substring(0,10)+"...";
	  					}
	  					if(title!=null&&title.length>10){
	  						title = title.substring(0,10)+"...\'";
	  					}
	  					return '<a href="javascript:window.executeTask('+processInstance+','+executionId+','+dbid+','+businessId+','+url+','+title+')">'+value+'</a>';
	  				}},
	  			{field:'priority',title:'优先级',width:20,align:'center',
	  				formatter:function(value,rec){
	  					if(value == "2"){
	  						return '<img align="center" src="static/images/platform/bpm/client/images/highest.gif"/>';
	  					}else if(value == "1"){
	  						return '<img align="center" src="static/images/platform/bpm/client/images/high.gif"/>';
	  					}else{
	  						return '<img align="center" src="static/images/platform/bpm/client/images/normal.gif"/>';
	  					}
					}
				},
	  			{field:'taskSendUser',title:'发送人',width:20,align:'center'},
	  			{field:'taskSendDept',title:'发送部门',width:30,align:'center'},
	  			{field:'cTime',title:'发送时间',width:50,align:'center'},
	  			{field:'op',title:'操作',width:20,align:'center',
	  				formatter:function(value,rec){
	  					return '&nbsp;<img src="static/images/platform/bpm/client/images/signTask.gif" style="cursor:pointer" title="把待办任务标记完成" onclick=\"javascript:window.finishTask('+rec.dbid+')\" />&nbsp;&nbsp;&nbsp;&nbsp;<img src="static/images/platform/bpm/client/images/trackTask.gif" style="cursor:pointer" title="流程跟踪" onclick=\"javascript:window.trackBpm('+rec.processInstance+')\"/>';
					}
				}
	  			
	  		]]
	});
	//设置分页控件   
	var p = $('#Todo_data').datagrid('getPager');
	$(p).pagination({
	    pageSize: 10,//每页显示的记录条数，默认为10
	    pageList: [5,10,15],//可以设置每页记录条数的列表
	    beforePageText: '第',//页数文本框前显示的汉字
	    afterPageText: '页    共 {pages} 页',
	    displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
	});
}

function loadFinishedTodo(queryParams){
	$('#FinishedTodo_data').datagrid({
		height: 'auto',
	    nowrap: false,
	    striped: true,
	    url:'platform/bpm/clientbpmdisplayaction/getfinishtodo.json',
	    autoRowHeight:false,
	    idField:'id',
	    singleSelect:false,//是否单选 
	    pagination:true,//分页控件
	    rownumbers:true,//行号
	    queryParams:queryParams,
	    loadMsg:' 处理中，请稍候…',
	    fitColumns: true,
	    columns:[[
	  			{field:'processDefName',title:'流程名称',width:50,align:'left',
	  				formatter:function(value,rec){
	  					if(value!=null&&value.length>10){
	  						value = value.substring(0,10)+"...";
	  					}
	  					return value;
	  				}},
	  			{field:'taskTitle',title:'标题',width:50,align:'left',
	  				formatter:function(value,rec){
	  					var processInstance = "'"+rec.processInstance+"'";
	  					var executionId = "'"+rec.executionId+"'";
	  					var dbid = "'"+rec.dbid+"'";
	  					var businessId = "'"+rec.businessId+"'";
	  					var url = "'"+rec.formResourceName+"'";
	  					var title = "'"+rec.taskTitle+"'";
	  					if(value!=null&&value.length>10){
	  						value = value.substring(0,10)+"...";
	  					}
	  					if(title!=null&&title.length>10){
	  						title = title.substring(0,10)+"...";
	  					}
	  					return '<a href="javascript:window.executeTask('+processInstance+','+executionId+','+dbid+','+businessId+','+url+','+title+')">'+value+'</a>';
	  				}},
	  			{field:'priority',title:'优先级',width:20,align:'center',
	  				formatter:function(value,rec){
	  					if(value == "2"){
	  						return '<img align="center" src="static/images/platform/bpm/client/images/highest.gif"/>';
	  					}else if(value == "1"){
	  						return '<img align="center" src="static/images/platform/bpm/client/images/high.gif"/>';
	  					}else{
	  						return '<img align="center" src="static/images/platform/bpm/client/images/normal.gif"/>';
	  					}
					}
				},
	  			{field:'taskSendUser',title:'发送人',width:20,align:'center'},
	  			{field:'taskSendDept',title:'发送部门',width:30,align:'center'},
	  			{field:'cTime',title:'发送时间',width:50,align:'center'},
	  			{field:'op',title:'流程跟踪',width:20,align:'center',
	  				formatter:function(value,rec){
	  					return '&nbsp;<img src="static/images/platform/bpm/client/images/trackTask.gif" style="cursor:pointer" title="流程跟踪" onclick=\"javascript:window.trackBpm('+rec.processInstance+')\"/>';
					}
				}
	  			
	  		]]
	});
	//设置分页控件   
	var p = $('#FinishedTodo_data').datagrid('getPager');
	$(p).pagination({
	    pageSize: 10,//每页显示的记录条数，默认为10
	    pageList: [5,10,15],//可以设置每页记录条数的列表
	    beforePageText: '第',//页数文本框前显示的汉字
	    afterPageText: '页    共 {pages} 页',
	    displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
	    /*onBeforeRefresh:function(){  
	        $(this).pagination('loading');  
	        alert('before refresh');  
	        $(this).pagination('loaded');  
	    }*/  
	});
}

function executeTask(entryId,executionId,taskId,formId,url,title){
	if(url == null || url == ''){
		return ;
	}
	//debugger;
	var proxyPage = "N"; //是否做页面代理
	if(url!=null&&url.indexOf("proxyPage=Y")!=-1){//是否做页面代理
		proxyPage = "Y";
	}
	
	if (proxyPage != "Y") { //不明确指定用代理页面的，则通通跳转到自己页面
       if (url.indexOf("?") > 0) {
	    url += "&entryId=" + entryId;
	   }else{
	   	 url += "?entryId=" + entryId;
	   }
        url += "&id=" + formId;
		url += "&executionId=" + executionId;
		url += "&taskId=" + taskId;
       	try{
       		if(typeof(eval(top.addTab))=="function"){
       			top.addTab(title,encodeURI(url),"dorado/client/skins/~current/common/icons.gif","taskTodo"," -0px -120px");
       		}else{
       			window.open(url);
       		} 
       	}catch(e){}
		return; 
	 }
	//以下都是采用代理页面的avicit/platform6/bpmclient/bpm/ProcessApprove.jsp
    var redirectPath = "";
    redirectPath += "?id=" + formId;
    redirectPath += "&entryId=" + entryId;
    redirectPath += "&executionId=" + executionId;
	redirectPath += "&taskId=" + taskId;
    if (url.indexOf("?") > 0) {
        url += "&entryId=" + entryId;
		url += "&id=" + formId;
		url += "&executionId=" + executionId;
		url += "&taskId=" + taskId;
    }
    else {
        url += "?entryId=" + entryId;
		url += "&id=" + formId;
		url += "&executionId=" + executionId;
		url += "&taskId=" + taskId;
    }
    redirectPath += "&url=" + encodeURIComponent(url);
	try{
   		if(typeof(eval(top.addTab))=="function"){
   			redirectPath = "avicit/platform6/bpmclient/bpm/ProcessApprove.jsp"+redirectPath;
   			top.addTab(title,redirectPath,"dorado/client/skins/~current/common/icons.gif","taskTodo"," -0px -120px");
   		}else{
   			redirectPath = "ProcessApprove.jsp"+redirectPath;
   			window.open(redirectPath);
   		} 
   	}catch(e){}
	return ;
}
function finishTask(id){
	if(confirm("是否标识完成?")){
		ajaxRequest("POST","dbid="+id,"platform/bpm/clientbpmdisplayaction/finishtodo","json","backFinished");
	}
}
function backFinished(obj){
	if(obj != null && obj.mes == true){
		doTodo();
	}
}

function trackBpm(processInstanceId){
	var url = getPath()+"/avicit/platform6/bpmclient/bpm/ProcessTrack.jsp";
	url += "?processInstanceId="+processInstanceId;
	window.open(encodeURI(url),"流程图","scrollbars=no,status=yes,resizable=no,top=0,left=0,width=700,height=400");
}

//查询未办
function searchTodoFun(){
	var queryParams = {
		userId : userId,
		taskFinished : '0',
		processDefName : $('#Todo_processDefName').attr('value'),
		taskTitle : $('#Todo_taskTitle').attr('value')
	};
	loadTodo(queryParams); 
}

//查询已办
function searchFinishedTodoFun(){
	var queryParams = {
			userId : userId,
			taskFinished : '1',
			processDefName : $('#FinishedTodo_processDefName').attr('value'),
			taskTitle : $('#FinishedTodo_taskTitle').attr('value'),
			endDateBegin : $("#FinishedTodo_endDateBegin").datetimebox('getValue'),
			endDateEnd : $("#FinishedTodo_endDateEnd").datetimebox('getValue')
		};
	loadFinishedTodo(queryParams); 
}

function clearTodoFun(){
	$('#Todo_searchForm input').val('');
	$('#Todo_searchForm select').val('');
}

function clearFinishedTodoFun(){
	$('#FinishedTodo_searchForm input').val('');
	$('#FinishedTodo_searchForm select').val('');
}

</script>
<body class="easyui-layout" fit="true">
	<div  id="processWork" style="overflow: hidden;height:0px;">
		<div title="未办工作" style="padding:3px;width:auto;">
			<div id="Todo_toolbar" class="datagrid-toolbar" style="height: auto;">
				<fieldset>
					<legend>查询条件</legend>
					<table class="tableForm" id="Todo_searchForm">
						<tr>
							<td>流程名称</td>
							<td colspan="2"><input id="Todo_processDefName" name="Todo_processDefName"  class="easyui-inputbox" style="width: 150px;" /></td>
							<td style="width:50px"></td>
							
							<td>标题</td>
							<td colspan="2"><input id="Todo_taskTitle" name="Todo_taskTitle"  class="easyui-inputbox" style="width: 150px;" /></td>
							<td style="width:50px"></td>
							
							<td ><a class="easyui-linkbutton"  iconCls="icon-search" plain="true" onclick="searchTodoFun();" href="javascript:void(0);">查询</a><a class="easyui-linkbutton" iconCls="icon-cancel" plain="true" onclick="clearTodoFun();" href="javascript:void(0);">清空</a>
							</td>
						</tr>
					</table>
				</fieldset>
			</div>
			<table id="Todo_data"></table>
		</div>
		<div title="已办工作" style="padding:3px;width:auto">
			<div id="FinishedTodo_toolbar" class="datagrid-toolbar" style="height: auto;">
				<fieldset>
					<legend>查询条件</legend>
					<table class="tableForm" id="FinishedTodo_searchForm">
						<tr>
							<td>流程名称</td>
							<td colspan="2"><input id="FinishedTodo_processDefName" name="FinishedTodo_processDefName"  class="easyui-inputbox" style="width: 150px;" /></td>
							<td style="width:50px"></td>
							
							<td>标题</td>
							<td colspan="2"><input id="FinishedTodo_taskTitle" name="FinishedTodo_taskTitle"  class="easyui-inputbox" style="width: 150px;" /></td>
							<td style="width:50px"></td>
							
							<td>完成日期(起)</td>
							<td colspan="2">
								<input name="FinishedTodo_endDateBegin" id="FinishedTodo_endDateBegin"  class="easyui-datebox" editable="false" style="width: 150px;" />
							</td>
							<td style="width:50px"></td>
							<td>完成日期(止)</td>
							<td colspan="2">
								<input name="FinishedTodo_endDateEnd" id="FinishedTodo_endDateEnd"  class="easyui-datebox" editable="false" style="width: 150px;" />
							</td>
							<td style="width:50px"></td>
							
							<td ><a class="easyui-linkbutton"  iconCls="icon-search" plain="true" onclick="searchFinishedTodoFun();" href="javascript:void(0);">查询</a><a class="easyui-linkbutton" iconCls="icon-cancel" plain="true" onclick="clearFinishedTodoFun();" href="javascript:void(0);">清空</a>
							</td>
						</tr>
					</table>
				</fieldset>
			</div>
			<table id="FinishedTodo_data"></table>
		</div>
	</div>
<body>
</html>
