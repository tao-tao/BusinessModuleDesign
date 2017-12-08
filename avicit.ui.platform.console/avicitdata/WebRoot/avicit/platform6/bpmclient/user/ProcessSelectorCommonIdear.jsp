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
<script src="static/js/platform/bpm/cookie.jquery.js" type="text/javascript"></script>
<script src="static/js/platform/bpm/ProcessSelectorCommonIdearComponent.js" type="text/javascript"></script>

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
   		<div id="selectorUserIdeaLayout" region="center" split="true">
   			<div id="toolbar"></div>
   			<textarea id="textAreaIdeas" style="width:98.5%;height:99%;"></textarea>
   		</div>
   </div>
   
   <div id="shareit-box">
		<div id="shareit-header"></div>
		<div id="shareit-body">
			<div id="shareit-blank"></div>
			<div id="shareit-url"></div>
		</div>
	</div>
	<input type="hidden" id="ideaCompelManner" name="ideaCompelManner" value='' />
</body>
</html>
