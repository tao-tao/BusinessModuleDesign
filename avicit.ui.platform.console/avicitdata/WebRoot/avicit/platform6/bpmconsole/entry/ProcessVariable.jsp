<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<html>
<head>
<%
	String processInstanceId = request.getParameter("processInstanceId");
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>流程实例变量</title>
<base href="<%=ViewUtil.getRequestPath(request) %>">
<jsp:include page="/avicit/platform6/bpmclient/bpm/ProcessCommonJsInclude.jsp"></jsp:include>
</head>

<script type="text/javascript">
var baseurl = '<%=request.getContextPath()%>';
var processInstanceId = '<%=processInstanceId%>';
//初始化数据
function loadData(){
	$(function(){
		var lastIndex;
		$('#bpmVariable').datagrid({
			url: 'platform/bpm/bpmConsoleAction/getProcessEntryVariable.json?processInstanceId='+processInstanceId,
			width: '100%',
		    nowrap: false,
		    striped: true,
		    autoRowHeight:false,
		    singleSelect:true,
		    checkOnSelect:false,
		    remoteSort : false,
			height: 340,
			fitColumns: true,
			sortName: 'dbid',  //排序字段,当表格初始化时候的排序字段
			sortOrder: 'asc',    //定义排序顺序
			pagination:false,
			rownumbers:true,
			loadMsg:' 处理中，请稍候…',
			columns:[[
				{field:'dbid',hidden:true},
				{field:'key_',title:'参数名称',width:120,align:'left',sortable:true},
				{field:'class_',title:'参数类型',width:120,align:'left',sortable:true,
					formatter:function(value,rec){
						var type=rec.class_;
						if(type=='string'){
	  						return '字符串';
	  					}else if(type=='long' || type=='int' ){
	  						return '整型';
	  					}if(type=='blob'){
	  						return '二进制大对象';
	  					}else{
	  						return '其它类型';
	  					}
					}	
				},
				{field:'string_value_',title:'参数值',width:350,align:'left',sortable:true}
			]]
		});
	});
}

function trackback(obj){
	loadTrack();
}
function searchFun(){
	   var strName =  $("#strName").val();
	   var strValue =  $("#strValue").val();
	   var queryParams = $('#bpmVariable').datagrid('options').queryParams;  
	   queryParams.strName =strName;
	   queryParams.strValue = strValue;
	   $('#bpmVariable').datagrid('options').queryParams=queryParams;     
	   $("#bpmVariable").datagrid('load'); 
}

</script>
<body onload='loadData();'>
	<div id="toolbar" class="datagrid-toolbar"
		style="height: auto; display: block;">
			<table class="tableForm" id="searchForm" width='100%'>
				<tr>
					<td>参数名称：</td>
					<td colspan="2"><input name="strName" id="strName"
						editable="false" style="width: 100px;" />
					<td>参数值</td>
					<td colspan="2"><input name="strValue" id="strValue"
						editable="false" style="width: 100px;" />
					</td>
					<td style="width:50px"></td>
						<td colspan="2"><a class="easyui-linkbutton"   iconCls="icon-search" plain="true" onclick="searchFun();" href="javascript:void(0);">查询</a><a class="easyui-linkbutton" iconCls="icon-cancel" plain="true" onclick="clearFun();" href="javascript:void(0);">清空</a>
					</td>
				</tr>
			</table>
	</div>
	<table id="bpmVariable"></table>
</body>
</html>