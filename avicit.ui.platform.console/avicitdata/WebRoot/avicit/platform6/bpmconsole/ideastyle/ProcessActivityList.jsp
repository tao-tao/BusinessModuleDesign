<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<%
	String path = request.getContextPath();
	String pdId = request.getParameter("pdId");
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>流程意见配置管理</title>
<base href="<%=ViewUtil.getRequestPath(request) %>">
<jsp:include page="/avicit/platform6/bpmclient/bpm/ProcessCommonJsInclude.jsp"></jsp:include>

</head>

<script type="text/javascript"> 
var baseurl = '<%=request.getContextPath()%>';
	$(function(){
		loadList();
	});
	
	function loadList(){
		var dataGridHeight = $(".easyui-layout").height();
		$('#processActivityTable').datagrid({
			url: 'platform/bpm/bpmconsole/ideaStyleManagerAction/getActivityList.json?pdId=<%=pdId%>',
			width: '100%',
		    nowrap: false,
		    striped: true,
		    autoRowHeight:false,
		    singleSelect:false,
		    checkOnSelect:false,
		    remoteSort : false,
			height: dataGridHeight,
			fitColumns: true,
			//sortName: 'orderBy',  //排序字段,当表格初始化时候的排序字段
			//sortOrder: 'asc',    //定义排序顺序
			pagination:false,
			rownumbers:true,
			queryParams:{"":""},
			loadMsg:' 处理中，请稍候…',
			columns:[[
			    {field:'id',hidden:true},
				{field:'op',title:'操作',width:25,align:'left',checkbox:true},
				{field:'activityAlias',title:'节点名称',width:80,align:'left',sortable:true},
				{field:'activityName',title:'节点代码',width:50,align:'left',sortable:true}
			]]
		});
	}
	
	function getSelectedResultDataJson(){
		return $('#processActivityTable').datagrid('getChecked');
	}
</script>
<body class="easyui-layout" fit="true">
	<div region="center" border="false" style="overflow: hidden;">
		<table id="processActivityTable"></table>
	</div>
</body>
</html>