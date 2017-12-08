<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<html>
<head>
<%
	String pdId = request.getParameter("pdId");
	String activityName = request.getParameter("activityName");
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
	loadTaskActDuration();
});

function loadTaskActDuration(){
	var dataGridHeight = $(".easyui-layout").height();
	$('#processentrylistTable').datagrid({
		url: 'platform/bpm/processAnalysisAction/getTaskActDurationListByPage.json?pdId=<%=pdId%>&activityName=<%=activityName%>&activityType=task&startDateBegin=<%=startDateBegin%>&startDateEnd=<%=startDateEnd%>',
		width: '100%',
	    nowrap: false,
	    striped: true,
	    autoRowHeight:false,
	    singleSelect:false,
	    checkOnSelect:false,
	    remoteSort : false,
		height: dataGridHeight,
		fitColumns: true,
		sortName: 'duration',  //排序字段,当表格初始化时候的排序字段
		sortOrder: 'desc',    //定义排序顺序
		pagination:true,
		pageSize:20,
		rownumbers:true,
		queryParams:{"":""},
		loadMsg:' 处理中，请稍候…',
		columns:[[
			{field:'pdname',title:'流程名称',width:50,align:'left'},
			{field:'entryid',title:'流程实例ID',width:30,align:'left',sortable:true},
			{field:'activityname',title:'流程活动ID',width:60,align:'left',sortable:true},
			{field:'activityalias',title:'流程活动名称',width:60,align:'left',sortable:true},
			{field:'starttime',title:'启动时间',width:50,align:'left',sortable:true,
  				formatter:function(value,rec){
  					var startdateMi=rec.starttime;
  					if(startdateMi==undefined){
  						return;
  					}
  					var newDate=new Date(startdateMi);
  					return newDate.Format("yyyy-MM-dd hh:mm:ss");   
				}},
			{field:'endtime',title:'结束时间',width:50,align:'left',editor:'text',
  				formatter:function(value,rec){
  					var endateMi=rec.endtime;
  					if(endateMi==undefined){
  						return;
  					}
  					var newDate=new Date(endateMi);
  					return newDate.Format("yyyy-MM-dd hh:mm:ss");   
				}},
		    {field:'duration',title:'耗时(分钟)',width:40,align:'left',sortable:true}
		]]
	});
	//设置分页控件   
	var p = $('#processentrylistTable').datagrid('getPager');
	$(p).pagination({
	    pageSize: 20,//每页显示的记录条数，默认为10
	    pageList: [5,10,15,20,25,30],//可以设置每页记录条数的列表
	    beforePageText: '第',//页数文本框前显示的汉字
	    afterPageText: '页    共 {pages} 页',
	    displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
	});
}


</script>
<body class="easyui-layout" fit="true">
<div region="center" border="false" style="overflow: hidden;">
<table id="processentrylistTable"></table>
</div>
</body>
</html>