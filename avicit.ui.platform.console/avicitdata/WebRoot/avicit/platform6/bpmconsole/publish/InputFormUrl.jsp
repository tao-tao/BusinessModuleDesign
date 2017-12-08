<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<html>
<head>
<%
	String nodeId = request.getParameter("nodeId");
	String processDefId = request.getParameter("processDefId");
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<base href="<%=ViewUtil.getRequestPath(request) %>">
<jsp:include page="/avicit/platform6/bpmclient/bpm/ProcessCommonJsInclude.jsp"></jsp:include>
</head>
<script type="text/javascript">
var baseurl = '<%=request.getContextPath()%>';
function submits() {
	$('#myforms').form('submit', {   
	onSubmit: function(){   
		return $("#myforms").form('validate');
	}  
	}); 
	var myurl = $('#myforms').form()[0].formUrl.value;
	return myurl;
}
 
</script>
<body class="easyui-layout" fit="true">
					<form id="myforms" method="post" action="">
						<fieldset style="height:50px;" >
							<legend >请输入要导入的表单URL</legend>
							<label><input class="easyui-validatebox"  type="text" name="formUrl" id="formUrl"  required="true" style="width:360px;"/></label>
						</fieldset>
					</form>
</body>
</html>