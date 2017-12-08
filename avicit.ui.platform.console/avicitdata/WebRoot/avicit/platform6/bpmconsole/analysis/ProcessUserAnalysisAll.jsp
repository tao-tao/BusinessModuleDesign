<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<html>
<head>
<%
	String deptId = request.getParameter("deptId");
	String positionId = request.getParameter("positionId");
	String startDate = request.getParameter("startDate");
	String endDate = request.getParameter("endDate");
	String querySign = request.getParameter("querySign");
	String tab = request.getParameter("tab");
	String title = request.getParameter("title");
	if ("TodoCompleteTop10".equals(tab)) {
		title = "用户待办完成数量";
	} else if ("TodoStockTop10".equals(tab)) {
		title = "用户待办积压数量";
	} else if ("TodoCompleteAverageTimeTop10".equals(tab)) {
		title = "用户待办平均办理时间";
	} else {
		title = "用户流程启动数量";
	}
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>流程信息统计</title>
<base href="<%=ViewUtil.getRequestPath(request) %>">
<jsp:include page="/avicit/platform6/bpmclient/bpm/ProcessCommonJsInclude.jsp"></jsp:include>
</head>

<script type="text/javascript">

$(function(){
	loadProcessEntry();
});

function loadProcessEntry(){
	var dataGridHeight = $(".easyui-layout").height();
	$('#processUserAnalysisAll').datagrid({
		url: 'platform/bpm/bpmconsole/processUserAnalysisAction/get<%=tab%>.json?startDate=<%=startDate%>&endDate=<%=endDate%>&querySign=<%=querySign%>&deptId=<%=deptId%>&positionId=<%=positionId%>',
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
			{field:'name',title:'用户名',width:50,align:'left'},
			{field:'value',title:'<%=title%>',width:50,align:'left',sortable:true},
			{field:'id',title:'查看详情',width:50,align:'center',
  				formatter:function(value,rec){
  					return '<img onclick="javascript:openDetailPage(\''+value+'\')" title="查看详情" src="static/images/platform/bpm/client/images/trackTask.gif" style="cursor:pointer"/>';
				}
			}
		]]
	});
}

//详细信息
function openDetailPage(id) {
	var tab = '<%=tab%>';
	if (tab == 'StartNumTop10') {
		openProcessUserStartPage(tab, id);
	} else {
		openProcessTodoPage(tab, id);
	}
}

//打开待办分析详细页
function openProcessTodoPage(tab, id) {
	var url = getPath()+"/avicit/platform6/bpmconsole/analysis/ProcessTodoList.jsp";
	var state = ''; var orderBy = '';
	if (tab == 'TodoCompleteTop10') {
		state = 'done'; orderBy = 'end_';
	} else if (tab == 'TodoStockTop10') {
		state = 'receive'; orderBy = 'create_';
	} else if (tab == 'TodoCompleteAverageTimeTop10') {
		state = 'done'; orderBy = 'end_';
	}
	url += "?state="+state+"&orderBy="+orderBy+"&userId="+id+"&startDate=<%=startDate%>&endDate=<%=endDate%>&deptId=<%=deptId%>&positionId=<%=positionId%>";
	top.addTab("待办信息",url,"dorado/client/skins/~current/common/icons.gif","ProcessTodoList"," -0px -120px");
}

//流程启动实例信息
function openProcessUserStartPage(tab, id) {
	var url  = getPath()+"/avicit/platform6/bpmconsole/analysis/ProcessInstanceStartList.jsp";
		url += "?userId="+id+"&startDateBegin=<%=startDate%>&startDateEnd=<%=endDate%>&deptId=<%=deptId%>&positionId=<%=positionId%>";
	top.addTab("流程启动实例",url,"dorado/client/skins/~current/common/icons.gif","ProcessInstanceStartList"," -0px -120px");
}
</script>
<body class="easyui-layout" fit="true">
<div region="center" border="false" style="overflow: hidden;">
	<table id="processUserAnalysisAll"></table>
</div>
</body>
</html>