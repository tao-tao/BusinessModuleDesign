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
var userId = '<%=userId%>';
var baseurl = '<%=request.getContextPath()%>';

<!--
var s;
var Sys = {};
var ua = navigator.userAgent.toLowerCase();
(s = ua.match(/msie ([\d.]+)/)) ? Sys.ie = s[1] :
(s = ua.match(/firefox\/([\d.]+)/)) ? Sys.firefox = s[1] :
(s = ua.match(/chrome\/([\d.]+)/)) ? Sys.chrome = s[1] :
(s = ua.match(/opera.([\d.]+)/)) ? Sys.opera = s[1] :
(s = ua.match(/version\/([\d.]+).*safari/)) ? Sys.safari = s[1] : 0;
var isIE6 = false;
//判断IE版本
if(Sys.ie && ua.indexOf('msie 6.0') > 1){
	isIE6 = true;
}
if(Sys.ie && ua.indexOf('msie 7.0') > 1){
	isIE6 = true;
}

function doTodo(){
	var dataGridHeight = $(".easyui-layout").height();
	if(dataGridHeight==null||dataGridHeight==0){
		dataGridHeight = 300;
	}
	$('#todo_data').datagrid({  
	    height: dataGridHeight,
	    nowrap: false,
	    striped: true,
	    url:'platform/bpm/clientbpmdisplayaction/getFocusedTask.json?userId='+userId,
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
	  					//if(value!=null&&value.length>10){
	  						//value = value.substring(0,10)+"...";
	  					//}
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
	  					return '&nbsp;<img src="static/images/platform/bpm/client/images/signTask.gif" style="cursor:pointer" title="取消关注" onclick=\"javascript:window.cancelFocusedTask('+rec.dbid+')\" />&nbsp;&nbsp;&nbsp;&nbsp;<img src="static/images/platform/bpm/client/images/trackTask.gif" style="cursor:pointer" title="流程跟踪" onclick=\"javascript:window.trackBpm('+rec.processInstance+')\"/>';
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
	 url += "?id=" + formId;
	 try{
     	window.open(getPath()+"/"+url);
       	}catch(e){}
		return; 
}

function cancelFocusedTask(id){
	if(confirm("是否取消关注?")){
		ajaxRequest("POST","dbid="+id,"platform/bpm/clientbpmoperateaction/cancelFocusedTask","json","backFinished");
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
	if(isIE6){
		$('#todo_data').datagrid('options').url = 'platform/bpm/clientbpmdisplayaction/getFocusedTask.json?userId='+userId;
		$('#todo_data').datagrid('resize',{width:600,height:300});
		$("#todo_data").datagrid('reload'); 
	}
});
</script>
<body class="easyui-layout" fit="true">
	<div region="center" style="padding:0px;" border="false">
		<table id="todo_data"></table>
	</div>
<body>
</html>
