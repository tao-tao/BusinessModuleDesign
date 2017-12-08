<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<html>
<head>
<%
	String startDate = request.getParameter("startDate");
	String endDate = request.getParameter("endDate");
	String querySign = request.getParameter("querySign");
	String tab = request.getParameter("tab");
	String title = request.getParameter("title");
	if ("TodoCompleteTop10".equals(tab)) {
		title = "流程待办完成数量";
	} else if ("TodoStockTop10".equals(tab)) {
		title = "流程待办积压数量";
	} else if ("TodoCompleteAverageTimeTop10".equals(tab)) {
		title = "流程待办平均办理时间";
	} else {
		title = "流程启动数量";
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
	$('#processDeptAnalysisAll').datagrid({
		url: 'platform/bpm/bpmconsole/processDeptAnalysisAction/get<%=tab%>.json?startDate=<%=startDate%>&endDate=<%=endDate%>&querySign=<%=querySign%>',
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
			{field:'name',title:'部门名',width:50,align:'left'},
			{field:'value',title:'<%=title%>',width:50,align:'left',sortable:true},
			{field:'id',title:'查看详情',width:50,align:'center',
  				formatter:function(value,rec){
  					return '<img onclick="javascript:openUserAnalysisAll(\''+value+'\')" title="查看详情" src="static/images/platform/bpm/client/images/trackTask.gif" style="cursor:pointer"/>';
				}
			}
		]]
	});
}

//用户维度分析
function openUserAnalysisAll(id) {
	var tab = '<%=tab%>';
	var url  = getPath()+"/avicit/platform6/bpmconsole/analysis/ProcessUserAnalysisAll.jsp";
	var title = "统计信息";
	if (tab == 'TodoCompleteTop10') {
		title = "用户待办完成数量";
	} else if (tab == 'TodoStockTop10') {
		title = "用户待办积压数量";
	} else if (tab == 'TodoCompleteAverageTimeTop10') {
		title = "用户待办平均办理时间";
	} else {
		title = "用户流程启动数量";
	}
		url += "?startDate=<%=startDate%>&endDate=<%=endDate%>&querySign=all&tab="+tab+"&title="+title+"&deptId="+id;
	top.addTab(title,url,"dorado/client/skins/~current/common/icons.gif","ProcessUserAnalysisAll"," -0px -120px");
}

</script>
<body class="easyui-layout" fit="true">
<div region="center" border="false" style="overflow: hidden;">
	<table id="processDeptAnalysisAll"></table>
</div>
</div>
</body>
</html>