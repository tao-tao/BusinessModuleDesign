<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>工作移交</title>
<base href="<%=ViewUtil.getRequestPath(request) %>">
<jsp:include page="/avicit/platform6/bpmclient/bpm/ProcessCommonJsInclude.jsp"></jsp:include>
</head>

<script type="text/javascript">
var baseurl = '<%=request.getContextPath()%>';
$(function(){
	loadWorkHandTasklist();
});
function loadWorkHandTasklist(){
	$(function(){
		var dataGridHeight = $(".easyui-layout").height();
		$('#WorkHandTasklist').datagrid({
			url: 'platform/bpm/clientbpmWorkHandAction/getWorkHandTask.json',
			width: '100%',
		    nowrap: false,
		    striped: true,
		    autoRowHeight:false,
		    singleSelect:false,
		    checkOnSelect:false,
		    remoteSort : false,
			height: dataGridHeight,
			fitColumns: true,
			sortName: 'taskTitle',  //排序字段,当表格初始化时候的排序字段
			sortOrder: 'asc',    //定义排序顺序
			pagination:false,
			rownumbers:true,
			loadMsg:' 处理中，请稍候…',
			columns:[[
			    {field : 'dbid',title : '',width :15,checkbox : true},
                {field:'taskTitle',title:'标题',width:80,align:'left'},
				{field:'processDefName',title:'流程名称',width:40,align:'left'},
				{field:'taskSendUser',title:'发送人',width:25,align:'left',sortable:true},
				{field:'taskSendDept',title:'发送部门',width:25,align:'left',sortable:true},
				{field:'assigneeName',title:'处理人',width:25,align:'left'},
				{field:'assignee',title:'',width:25,align:'left',hidden:true}
			]]
		});
		//设置分页控件   
		var p = $('#WorkHandTasklist').datagrid('getPager');
		$(p).pagination({
		    pageSize: 10,//每页显示的记录条数，默认为10
		    pageList: [5,10,15],//可以设置每页记录条数的列表
		    beforePageText: '第',//页数文本框前显示的汉字
		    afterPageText: '页    共 {pages} 页',
		    displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
		});
	});
}

</script>
<body  class="easyui-layout" fit="true">
<div region="center" border="false" style="overflow: hidden;">
		<table id="WorkHandTasklist"></table>
</div>
</body>
</html>