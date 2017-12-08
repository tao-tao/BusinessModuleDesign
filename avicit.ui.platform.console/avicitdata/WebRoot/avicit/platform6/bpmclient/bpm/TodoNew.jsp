<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="avicit.platform6.api.sysshirolog.impl.AfterLoginSessionProcess"%>      
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<%
	String userId = (String)session.getAttribute("userId");
	String skinColor = (String)request.getSession().getAttribute(AfterLoginSessionProcess.SESSION_CURRENT_USER_SKIN);
%>
<base href="<%=ViewUtil.getRequestPath(request) %>">
<jsp:include page="/avicit/platform6/bpmclient/bpm/ProcessCommonJsInclude.jsp"></jsp:include>
<script src="static/js/platform/bpm/client/js/ToolBar.js" type="text/javascript"></script>
<link type="text/css" rel="stylesheet" href="static/css/platform/themes/<%=skinColor %>/todo/todo.css" >
</head>
<script type="text/javascript">
var page = '1';
function executeTask(entryId,executionId,taskId,formId,url,title,taskType){
	if(url == null || url == ''){
		return ;
	}
	if(taskType == '1') {
		finishTaskNew(taskId);
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
		window.location.reload();
	}
}
function finishTaskNew(id){
	if(id != null){
		ajaxRequest("POST","dbid="+id,"platform/bpm/clientbpmdisplayaction/finishtodo","json","backFinished");
	}
}
function trackBpm(processInstanceId){
	var url = getPath()+"/avicit/platform6/bpmclient/bpm/ProcessTrack.jsp";
	url += "?processInstanceId="+processInstanceId;
	//window.open(encodeURI(url),"流程图","scrollbars=no,status=yes,resizable=no,top=0,left=0,width=700,height=400");
	top.addTab("流程图",encodeURI(url),"dorado/client/skins/~current/common/icons.gif","taskTodo"," -0px -120px");
	//var usd = new UserSelectDialog("trackdialog","500","400",encodeURI(url) ,"流程跟踪窗口");
	//usd.show();
}
function go(num) {  
	if(num<=0) { 
    	alert('当前已是第一页'); 
      	return; 
    } 
    if(num>${maxPage}) { 
      	alert('当前已是最后一页'); 
      	return; 
    }
    var url = window.location; 
    var pos = String(url).indexOf("page");    //查看是否存在pageNum页数参数 
    if(pos=="-1") { 
    	window.location.replace(url+'?page='+num) ;   //不存在则添加,值为所点击的页数 
	} else { 
    	var ui = String(url).substring(0,pos);           
   		window.location.replace(ui+'page='+num);      //存在,则刷新pageNum参数值 
	} 
	
} 
function jump() {  
	var jumPage = $('#jumPage').val();
	if(jumPage==''||jumPage<1){
		jumPage = 1;
	}else if(jumPage>${maxPage}) { 
		jumPage = ${maxPage};
	}
	var url = window.location; 
  	 	var pos = String(url).indexOf("page");    //查看是否存在pageNum页数参数 
   	if(pos=="-1") { 
   		window.location.replace(url+'?page='+jumPage) ;   //不存在则添加,值为所点击的页数 
	} else { 
   		var ui = String(url).substring(0,pos);           
  			window.location.replace(ui+'page='+jumPage);      //存在,则刷新pageNum参数值 
	} 
	
}

</script>
<body>
<div class="pt-pt-cont">
<table  class="p-table4">
	<thead>
		<tr>
			<td align='center' nowrap>标题</td>
			<td align='center' nowrap>流程名称</td>
			<td align='center' nowrap>优先级</td>
			<td align='center' nowrap>发送人</td>
			<td align='center' nowrap>发送部门</td>
			<td align='center' nowrap>发送时间</td>
			<td align='center' nowrap>操作</td>
		</tr>
	</thead>
<tbody>
	<c:forEach items="${rows}" var="todo" varStatus="vs"> 
		<c:choose>
			<c:when test="${vs.count % 2 == 0}">
				<tr class="odd"> 
					<td align='center' nowrap><a href="javascript:window.executeTask('${todo.processInstance }','${todo.executionId }','${todo.dbid }','${todo.businessId }','${todo.formResourceName }','${todo.taskTitle }','${todo.taskType}')">${todo.taskTitle }</a></td> 
					<td align='center' nowrap> ${todo.processDefName }</td> 
					<c:if test="${todo.priority==2}">
		        		<td align='center' nowrap><img align="center" src="static/images/platform/bpm/client/images/highest.gif"/></td> 
		        	</c:if>
					<c:if test="${todo.priority==1}">
		        		<td align='center' nowrap><img align="center" src="static/images/platform/bpm/client/images/high.gif"/></td> 
		        	</c:if>
		        	<c:if test="${todo.priority==0 || todo.priority==null || todo.priority==''}">
		        		<td align='center' nowrap><img align="center" src="static/images/platform/bpm/client/images/normal.gif"/></td> 
		        	</c:if>
		        	<td align='center' nowrap>${todo.taskSendUser }</td> 
		        	<td align='center' nowrap>${todo.taskSendDept }</td> 
		        	<td align='center' nowrap>${todo.cTime }</td> 
		        	<td align='center' nowrap><img src='static/images/platform/bpm/client/images/signTask.gif' style='cursor:pointer' title='把待办任务标记完成' onclick="javascript:window.finishTask('${todo.dbid }')" />&nbsp;&nbsp;&nbsp;&nbsp;<img src="static/images/platform/bpm/client/images/trackTask.gif" style="cursor:pointer" title="流程跟踪" onclick="javascript:window.trackBpm('${todo.processInstance }')"/></td> 
				</tr> 
			</c:when>
			<c:otherwise> 
				<tr class="even"> 
					<td align='center' nowrap><a href="javascript:window.executeTask('${todo.processInstance }','${todo.executionId }','${todo.dbid }','${todo.businessId }','${todo.formResourceName }','${todo.taskTitle }','${todo.taskType}')">${todo.taskTitle }</a></td> 
					<td align='center' nowrap>${todo.processDefName }</td> 
					
					<c:if test="${todo.priority==2}">
		        		<td align='center' nowrap><img align="center" src="static/images/platform/bpm/client/images/highest.gif"/></td> 
		        	</c:if>
					<c:if test="${todo.priority==1}">
		        		<td align='center' nowrap><img align="center" src="static/images/platform/bpm/client/images/high.gif"/></td> 
		        	</c:if>
		        	<c:if test="${todo.priority==0 || todo.priority==null || todo.priority==''}">
		        		<td align='center' nowrap><img align="center" src="static/images/platform/bpm/client/images/normal.gif"/></td> 
		        	</c:if>
		        	<td align='center' nowrap>${todo.taskSendUser }</td> 
		        	<td align='center' nowrap>${todo.taskSendDept }</td> 
		        	<td align='center' nowrap>${todo.cTime }</td> 
		        	<td align='center' nowrap><img src='static/images/platform/bpm/client/images/signTask.gif' style='cursor:pointer' title='把待办任务标记完成' onclick="javascript:window.finishTask('${todo.dbid }')" />&nbsp;&nbsp;&nbsp;&nbsp;<img src="static/images/platform/bpm/client/images/trackTask.gif" style="cursor:pointer" title="流程跟踪" onclick="javascript:window.trackBpm('${todo.processInstance }')"/></td>
				</tr> 
			</c:otherwise>
		</c:choose> 
	</c:forEach> 
</tbody>
</table>

<div class="list-pageNav">
	<c:if test="${maxPage>1}">
	
	<span class="pageNav-page">
		<a href="javascript:window.go(1)">首页</a>
		<a href="javascript:window.go('${pageNo-1}')">上页</a>
		<span class="jumpPage">第 <input type="text" style="height:8" id='jumPage' value="${pageNo}" onkeypress="if(event.keyCode==13){jump();return false;}"/> 页/共${maxPage}页(${total}条记录)</span>
		<a href="javascript:window.go('${pageNo+1}')">下页</a>
		<a href="javascript:window.go('${maxPage}')">末页</a>
		</span>
	</span>
	</span>
	</c:if>
</div>
</div>
</body>
</html>
