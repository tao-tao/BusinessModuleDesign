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
%>
<base href="<%=ViewUtil.getRequestPath(request) %>">
<jsp:include page="/avicit/platform6/bpmclient/bpm/ProcessCommonJsInclude.jsp"></jsp:include>
</head>

<script type="text/javascript">
var baseurl = '<%=request.getContextPath()%>';
var userId = '<%=userId%>';
function doTodo(){
	$('#todo_data').datagrid({   
	    height: 'auto',
	    nowrap: false,
	    striped: true,
	    url:'platform/bpm/clientbpmdisplayaction/getfinishtodo.json?userId='+userId+'&taskFinished=1',
	    autoRowHeight:false,
	    idField:'id',
	    singleSelect:false,//是否单选 
	    pagination:true,//分页控件
	    rownumbers:true,//行号
	    loadMsg:' 处理中，请稍候…',
	    //frozenColumns:[[
	    //    {field:'id',checkbox:true}
	    //]],
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
	var p = $('#todo_data').datagrid('getPager');
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
	//var usd = new UserSelectDialog("trackdialog","500","400",encodeURI(url) ,"流程跟踪窗口");
	//usd.show();
}
$(function(){
	doTodo();
});
</script>
<body>
<table id="todo_data"></table>
<body>
</html>
