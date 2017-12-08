<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>

<title>AVICIT WORKFLOW 选人</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<base href="<%=ViewUtil.getRequestPath(request) %>">
<jsp:include page="/avicit/platform6/bpmclient/bpm/ProcessUserDialogJsInclude.jsp"></jsp:include>
<script src="static/js/platform/bpm/cookie.jquery.js" type="text/javascript"></script>
<script src="static/js/platform/component/jQuery/jquery-easyui-1.3.5/plugins/jquery.toolbar.js" type="text/javascript"></script>
<script src="static/js/platform/component/jQuery/jquery-easyui-1.3.5/extend/easyui.layout.extend.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="static/js/platform/component/jQuery/jQuery-liveSearch/themes/jquery.liveSearch.css">
<script src="static/js/platform/component/jQuery/jQuery-liveSearch/jquery.liveSearch.js" type="text/javascript"></script>
<script src="static/js/platform/bpm/client/js/ProcessSelectorUser.js" type="text/javascript"></script>
<script src="static/js/platform/bpm/client/js/ProcessSelectorUserComponent.js" type="text/javascript"></script>
<script src="static/js/platform/bpm/client/js/ProcessSelectorCommonIdearComponent.js" type="text/javascript"></script>

<script type="text/javascript">
var baseurl = '<%=request.getContextPath()%>';
var dataJson = ${taskUserSelect};
var processInstanceId = '${processInstanceId}';
var executionId = '${executionId}';
var outcome = '${outcome}';
var type = '${type}';
var mySelectUser  = ${mySelectUser};
var taskId = '${taskId}';
var targetActivityName = '${targetActivityName}';
var orgId = '${orgId}';
var doSubmitUrl = '${doSubmitUrl}';
var doSubmitCallEvent = '${doSubmitCallEvent}';
</script>
</head>
<body>
<!-- 
	<div style="width:100%;height:275px;" id="topFrame">
		<iframe name="selectorUser" id="selectorUser" width="100%" height="100%" id="selectorUser" marginwidth="0" marginheight="0" src="${selectWebPath}"  scrolling="no" frameborder="0"></iframe>
	</div> 
	<div style="width:100%;height:135px;" id="bottomFrame">
		<iframe name="commonIdear" id="commonIdear" width="100%" height="100%" id="commonIdear"  marginwidth="0" marginheight="0" src="${commonIdearWebPath}"  scrolling="no" frameborder="0"></iframe> 
	</div>
	-->

   <div class="easyui-layout" fit="true" id='selectorUserlayout'>
   		<div region="center" id="workflowSelectorSource"></div>
   		<div region="south" split="true" style="height:125px;" id='ideaCompel' ><div id="toolbar"></div><textarea id="textAreaIdeas" style="width:98.5%;height:75px;"></textarea></div>
   </div>
   <!-- 加载常用意见 -->
   <div id="shareit-box">
		<div id="shareit-header"></div>
		<div id="shareit-body">
			<div id="shareit-blank"></div>
			<div id="shareit-url"></div>
		</div>
	</div>
   <!-- 强制表态需要 -->
   <input type="hidden" id="ideaCompelManner" name="ideaCompelManner" value='' />
</body>
<script>
	if(dataJson.nextTask != null && dataJson.nextTask.length == 1){
		$("#selectorUserlayout").append("<div region=\"east\" split=\"true\" style=\"width:300px;\" id='selectedResultCompel'><div id=\"selectorUserTabForTargetDataGrid_0\"></div></div>");
	}
</script>

</html>
	