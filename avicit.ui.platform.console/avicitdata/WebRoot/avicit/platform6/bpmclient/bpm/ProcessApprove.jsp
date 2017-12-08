<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<%
String entryId = request.getParameter("entryId");
String executionId = request.getParameter("executionId");
String taskId = request.getParameter("taskId");
String formId = request.getParameter("id");
String url = request.getParameter("url");

%>
<base href="<%=ViewUtil.getRequestPath(request) %>">
<script type="text/javascript">
var baseurl = '<%=request.getContextPath()%>';
</script>
<jsp:include page="/avicit/platform6/bpmclient/bpm/ProcessCommonJsInclude.jsp"></jsp:include>
<script src="static/js/platform/bpm/client/js/ToolBar.js" type="text/javascript"></script>
<script src="static/js/platform/bpm/client/js/ButtonProcessing.js" type="text/javascript"></script>
<script src="static/js/platform/bpm/client/js/Button.js" type="text/javascript"></script>

</head>
<script type="text/javascript">
function load(){
	var entryId = '<%=entryId%>';
	var executionId = '<%=executionId%>';
	var taskId = '<%=taskId%>';
	var formId = '<%=formId%>';
	var url = '<%=url%>';
	var toolbar = new ToolBar(entryId,executionId, taskId,'bpmToolBar', formId);
	$("#iframeId").attr("src",url);
}
</script>
<body onload="load();">
<div id="bpmToolBar"></div>
<iframe id="iframeId" width="100%" height="600" frameborder="0">
</iframe>
</body>
</html>
