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
<title>流程意见</title>
<base href="<%=ViewUtil.getRequestPath(request) %>">
<jsp:include page="/avicit/platform6/bpmclient/bpm/ProcessCommonJsInclude.jsp"></jsp:include>
</head>

<script type="text/javascript">
var baseurl = '<%=request.getContextPath()%>';
var processInstanceId = '<%=processInstanceId%>';
function loadTrack(){
	$(function(){
		var lastIndex;
		$('#idea').datagrid({
			toolbar:[
			  /**  {
				text:'删除',
				iconCls:'icon-remove',
				handler:function(){
					var row = $('#idea').datagrid('getSelected');
					if (row){
						var index = $('#idea').datagrid('getRowIndex', row);
						alert(row.currentActiveLabel);
						$('#idea').datagrid('deleteRow', index);
					}
				}
			},'-',
			**/
				{
				id : 'save',
				text:'保存',
				iconCls:'icon-save',
				handler:function(){
					var array = new Array();
					var rows = $('#idea').datagrid('getChanges');
					if(rows != null){
						for (var row in rows){
							array.push(rows[row]);
						}
					}
					$('#idea').datagrid('acceptChanges');
					var currRow = $("#idea").datagrid("getSelected");
					if(currRow != null){
						array.push(currRow);
					}
					var trackstr = JSON.stringify(array);
					ajaxRequest("POST","trackJson="+trackstr,"platform/bpm/clientbpmdisplayaction/dotrack","json","trackback");
				}
			},'-',{
				text:'撤销',
				iconCls:'icon-undo',
				handler:function(){
					$('#idea').datagrid('rejectChanges');
				}
			}],
			url: 'platform/bpm/clientbpmdisplayaction/getcharactertrack.json?processInstanceId='+processInstanceId,
			width: '100%',
		    nowrap: false,
		    striped: true,
		    autoRowHeight:false,
		    singleSelect:true,
		    checkOnSelect:false,
		    remoteSort : false,
			height: 340,
			fitColumns: true,
			sortName: 'intoTime',  //排序字段,当表格初始化时候的排序字段
			sortOrder: 'asc',    //定义排序顺序
			pagination:true,
			rownumbers:true,
			loadMsg:' 处理中，请稍候…',
			columns:[[
				{field:'dbid',title:'',width:40,align:'left',hidden:true},
				{field:'currentActiveLabel',title:'当前节点',width:40,align:'left',sortable:true},
				{field:'preActiveLabel',title:'上一节点',width:40,align:'left',sortable:true},
				{field:'assigneeName',title:'当前人',width:40,align:'left',sortable:true},
				{field:'assigneeDeptName',title:'当前部门',width:50,align:'left',sortable:true},
				{field:'iTime',title:'创建时间',width:60,align:'center',sortable:true},
				{field:'message',title:'意见',width:80,align:'left',editor:'text'},
				{field:'sendType',title:'发送类型',width:30,align:'left'},
				{field:'intoTime',hidden:true}
			]],
			onClickCell:function(rowIndex,field,value){
				if(field == 'message'){
					if (lastIndex != rowIndex){
						$('#idea').datagrid('endEdit', lastIndex);
						$('#idea').datagrid('beginEdit', rowIndex);
					}
					lastIndex = rowIndex;
				}
			}
		});
		//设置分页控件   
		var p = $('#idea').datagrid('getPager');
		$(p).pagination({
		    pageSize: 10,//每页显示的记录条数，默认为10
		    pageList: [5,10,15],//可以设置每页记录条数的列表
		    beforePageText: '第',//页数文本框前显示的汉字
		    afterPageText: '页    共 {pages} 页',
		    displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
		});
	});
}

function trackback(obj){
	loadTrack();
}

</script>
<body onload='loadTrack();'>
<table id="idea"></table>
</body>
</html>