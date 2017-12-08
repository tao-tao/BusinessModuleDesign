<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<html>
<head>
<%
	String deptId = request.getParameter("deptId");
	String positionId = request.getParameter("positionId");
	String userId = request.getParameter("userId");
	String state = request.getParameter("state");
	String orderBy = request.getParameter("orderBy");
	String startDate = request.getParameter("startDate");
	String endDate = request.getParameter("endDate");
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>流程待办信息</title>
<base href="<%=ViewUtil.getRequestPath(request) %>">
<jsp:include page="/avicit/platform6/bpmclient/bpm/ProcessCommonJsInclude.jsp"></jsp:include>
</head>

<script type="text/javascript">

$(function(){
	loadProcessEntry();
});

function loadProcessEntry(){
	var dataGridHeight = $(".easyui-layout").height();
	$('#processTodolist').datagrid({
		url: 'platform/bpm/bpmconsole/processUserAnalysisAction/getProcessTodoListByPage.json?deptId=<%=deptId%>&userId=<%=userId%>&positionId=<%=positionId%>&state=<%=state%>&orderBy=<%=orderBy%>&startDate=<%=startDate%>&endDate=<%=endDate%>',
		width: '100%',
	    nowrap: false,
	    striped: true,
	    autoRowHeight:false,
	    singleSelect:false,
	    checkOnSelect:false,
	    remoteSort : false,
		height: dataGridHeight,
		fitColumns: true,
		sortName: 'create_',  //排序字段,当表格初始化时候的排序字段
		sortOrder: 'desc',    //定义排序顺序
		pagination:true,
		pageSize:20,
		rownumbers:true,
		queryParams:{"":""},
		loadMsg:' 处理中，请稍候…',
		columns:[[
			{field:'dbid_',hidden:true},
			{field:'process_def_name_',title:'流程定义名称',width:50,align:'left'},
			{field:'procinst_',title:'流程实例ID',width:30,align:'left',sortable:true},
			{field:'task_title_',title:'流程待办标题',width:50,align:'left',sortable:true,
				formatter:function(value,rec){
  					var processInstance = "'"+rec.procinst_+"'";
  					return '<a href="javascript:window.trackBpm('+processInstance+')">'+value+'</a>';
  				}},
  			{field:'take_state_',title:'状态',width:40,align:'left',sortable:true,
  					formatter:function(value){
  						 if(value=='receive'){
  		                        return '流转中';
  		                    }else if(value=='done'){
  		                        return '结束';
  		                    }else if(value=='peruse'){
  		                        return '挂起';
  		                    }

  					}
  			},
			{field:'task_senduser_',title:'流程发送人',width:25,align:'left',sortable:true},
			{field:'task_senddept_',title:'发送部门',width:50,align:'left',sortable:true},
  			{field:'assigneeName',title:'流程接收人',width:25,align:'left',sortable:true},
			{field:'assigneeDeptName',title:'接收部门',width:50,align:'left',sortable:true},
			{field:'create_',title:'发送时间',width:50,align:'left',sortable:true,
  				formatter:function(value,rec){
  					var startdateMi=rec.create_;
  					if(startdateMi==undefined){
  						return;
  					}
  					var newDate=new Date(startdateMi);
  					return newDate.Format("yyyy-MM-dd hh:mm:ss");   
				}},
			{field:'end_',title:'完成时间',width:50,align:'left',editor:'text',
  				formatter:function(value,rec){
  					var endateMi=rec.end_;
  					if(endateMi==undefined){
  						return;
  					}
  					var newDate=new Date(endateMi);
  					return newDate.Format("yyyy-MM-dd hh:mm:ss");   
				}},
		    {field:'duration_',title:'耗时(分钟)',width:40,align:'left',sortable:true}
		]]
	});
	//设置分页控件   
	var p = $('#processTodolist').datagrid('getPager');
	$(p).pagination({
	    pageSize: 20,//每页显示的记录条数，默认为10
	    pageList: [5,10,15,20,25,30],//可以设置每页记录条数的列表
	    beforePageText: '第',//页数文本框前显示的汉字
	    afterPageText: '页    共 {pages} 页',
	    displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
	});
}

function trackBpm(processInstanceId){
	var url = getPath()+"/avicit/platform6/bpmclient/bpm/ProcessTrack.jsp";
	url += "?processInstanceId="+processInstanceId;
	window.open(encodeURI(url),"流程图","scrollbars=no,status=yes,resizable=no,top=0,left=0,width=700,height=400");
}
</script>
<body class="easyui-layout" fit="true">
<div region="center" border="false" style="overflow: hidden;">
	<table id="processTodolist"></table>
</div>
</body>
</html>