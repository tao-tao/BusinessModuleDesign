<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<html>
<head>
<%
	String catalogId = request.getParameter("catalogId");
	String startDateBegin = request.getParameter("startDateBegin");
	String startDateEnd = request.getParameter("startDateEnd");
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>流程实例信息</title>
<base href="<%=ViewUtil.getRequestPath(request) %>">
<jsp:include page="/avicit/platform6/bpmclient/bpm/ProcessCommonJsInclude.jsp"></jsp:include>
</head>

<script type="text/javascript">
var baseurl = '<%=request.getContextPath()%>';

$(function(){
	loadProcessEntry();
});

function loadProcessEntry(){
	var dataGridHeight = $(".easyui-layout").height();
	$('#processentrylist').datagrid({
		url: 'platform/bpm/processAnalysisAction/getProcessInstanceStartAllList.json?catalogId=<%=catalogId%>&state=ended&startDateBegin=<%=startDateBegin%>&startDateEnd=<%=startDateEnd%>',
		width: '100%',
	    nowrap: false,
	    striped: true,
	    autoRowHeight:false,
	    singleSelect:false,
	    checkOnSelect:false,
	    remoteSort : false,
		height: dataGridHeight,
		fitColumns: true,
		pagination:false,
		rownumbers:true,
		queryParams:{"":""},
		loadMsg:' 处理中，请稍候…',
		columns:[[
			{field:'pdId',title:'流程定义ID',width:50,align:'center'},
			{field:'pdName',title:'流程定义名称',width:50,align:'left'},
			{field:'instanceNum',title:'启动的流程实例数',width:30,align:'left',sortable:true}
		]]
	});
}

</script>
<body class="easyui-layout" fit="true">
<div region="center" border="false" style="overflow: hidden;">
<table id="processentrylist"></table>
</div>
</body>
</html>