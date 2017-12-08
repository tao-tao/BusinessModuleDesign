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
		url: 'platform/bpm/processAnalysisAction/getProcessDurationList.json?catalogId=<%=catalogId%>&state=ended&startDateBegin=<%=startDateBegin%>&startDateEnd=<%=startDateEnd%>',
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
			{field:'pdName',title:'流程定义名称',width:50,align:'left',
				formatter:function(value,rec){
					return '<a href="javascript:openProcessInstancePage(\''+rec.pdId+'\')" title="查看详情">'+value+'</a>';
				}
			},
			{field:'instanceNum',title:'完成的流程实例数',width:30,align:'left',sortable:true},
			{field:'duration',title:'平均耗时（分钟）',width:60,align:'left',sortable:true}
		]]
	});
}
/**
 * 流程耗时统计钻取
 */
function openProcessInstancePage(pdId){
	var url = getPath()+"/avicit/platform6/bpmconsole/analysis/ProcessInstanceList.jsp";
	url += "?pdId="+pdId+"&startDateBegin=<%=startDateBegin%>&startDateEnd=<%=startDateEnd%>";
	top.addTab(pdId+"流程实例",url,"dorado/client/skins/~current/common/icons.gif","ProcessInstanceList"," -0px -120px");
	//window.open(encodeURI(url),"流程实例信息","scrollbars=no,status=yes,resizable=no,top=0,left=0,width=700,height=400");
}
</script>
<body class="easyui-layout" fit="true">
<div region="center" border="false" style="overflow: hidden;">
<table id="processentrylist"></table>
</div>
</body>
</html>