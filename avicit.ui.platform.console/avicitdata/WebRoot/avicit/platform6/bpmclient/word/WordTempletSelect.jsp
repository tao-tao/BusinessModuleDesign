<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<html>
<head>
	<base href="<%=ViewUtil.getRequestPath(request) %>">
	<jsp:include page="/avicit/platform6/bpmclient/bpm/ProcessCommonJsInclude.jsp"></jsp:include>
	<title>正文模版</title>
</head>
<%
	String processInstanceId = request.getParameter("processInstanceId");
	String executionId = (String) request.getParameter("executionId");
	String taskId = (String) request.getParameter("taskId");
	String type = (String) request.getParameter("type");
%>
<script type="text/javascript">
var baseurl = '<%=request.getContextPath()%>';
var processInstanceId = '<%=processInstanceId%>';
var executionId = '<%=executionId%>';
var taskId = '<%=taskId%>';
var type = '<%=type%>';
function loadWordTemplet(){
	$(function(){
		$('#wordTemplet').datagrid({
			url: 'platform/bpm/clientbpmwordction/getWordTempletList.json?processInstanceId='+processInstanceId+'&executionId='+executionId+'&type='+type,
			width: '100%',
		    nowrap: false,
		    striped: true,
		    autoRowHeight:false,
		    singleSelect:true,
		    checkOnSelect:false,
			height: 340,
			fitColumns: true,
			pagination:false,
			rownumbers:true,
			loadMsg:' 处理中，请稍候…',
			columns:[[
				{field:'id',title:'',width:40,align:'left',hidden:true},
				{field:'wordName',title:'模版名称',width:40,align:'center',
					formatter:function(value,rec){
	  					return "<a href=\"javascript:window.doOpenTemplet('"+processInstanceId+"','"+executionId+"','"+rec.id+"','"+type+"');\">"+value+"</a>";
	  				}
				}
			]]
		});
	});
}

function doOpenTemplet(processInstanceId, executionId, templetId, type) {
	parent.openWordWindow(processInstanceId, executionId, templetId, type);
}

</script>
<body onload='loadWordTemplet();'>
<table id="wordTemplet"></table>
</body>
</html>