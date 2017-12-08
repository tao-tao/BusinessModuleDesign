<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>

<title>AVICIT WORKFLOW 选人</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<base href="<%=ViewUtil.getRequestPath(request) %>">
<jsp:include page="/avicit/platform6/bpmclient/bpm/ProcessUserDialogJsInclude.jsp"></jsp:include>
<script src="static/js/platform/bpm/client/js/ProcessWorkHand.js" type="text/javascript"></script>
<script type="text/javascript">
var baseurl = '<%=request.getContextPath()%>';
var workhandJson = ${workhandJson};
</script>
</head>
<body>
	<div id="workhandDataGrid" fit="true"></div> 
</body>
</html>
