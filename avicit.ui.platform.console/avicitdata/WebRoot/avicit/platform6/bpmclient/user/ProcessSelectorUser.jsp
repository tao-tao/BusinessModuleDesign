<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>

<title>AVICIT WORKFLOW 选人</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<base href="<%=ViewUtil.getRequestPath(request) %>">
<jsp:include page="/avicit/platform6/bpmclient/bpm/ProcessUserDialogJsInclude.jsp"></jsp:include>
<script src="static/js/platform/component/jQuery/jquery-easyui-1.3.5/plugins/jquery.toolbar.js" type="text/javascript"></script>
<script src="static/js/platform/component/jQuery/jquery-easyui-1.3.5/extend/easyui.layout.extend.js" type="text/javascript"></script>

<link rel="stylesheet" type="text/css" href="static/js/platform/component/jQuery/jQuery-liveSearch/themes/jquery.liveSearch.css">
<script src="static/js/platform/component/jQuery/jQuery-liveSearch/jquery.liveSearch.js" type="text/javascript"></script>
<script src="static/js/platform/bpm/client/js/ProcessSelectorUserComponent.js" type="text/javascript"></script>
<script type="text/javascript">
var baseurl = '<%=request.getContextPath()%>';
var dataJson = ${taskUserSelect};
var processInstanceId = '${processInstanceId}';
var executionId = '${executionId}';
var outcome = '${outcome}';
var type = '${type}';
var taskId = '${taskId}';
var targetActivityName = '${targetActivityName}';
var orgId = '${orgId}';
</script>
</head>
<body>
   <div class="easyui-layout" fit="true" id='selectorUserlayout'>
   		<div region="center" id="workflowSelectorSource"></div>
   </div>
</body>
<script>
	if(dataJson.nextTask != null && dataJson.nextTask.length == 1){
		//$("#selectorUserlayout").append("<div region=\"east\" split=\"true\" style=\"width:300px;\"><div id=\"selectorUserTabForTargetDataGrid_0\"></div></div>");
	}
</script>
</html>
