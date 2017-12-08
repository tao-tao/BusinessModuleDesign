<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>流程发布</title>
<base href="<%=ViewUtil.getRequestPath(request) %>">
<jsp:include page="/avicit/platform6/bpmclient/bpm/ProcessCommonJsInclude.jsp"></jsp:include>
</head>

<script type="text/javascript">
var baseurl = '<%=request.getContextPath()%>';
/**
 * 初始化当前页面
 */

 $(function(){
 	loadProcessTree();
 	loadProcessPublish();
 	window.setTimeout("expand()", 400);
 });
//绘制页面数据表格
function loadProcessPublish(){
	var dataGridHeight = $(".easyui-layout").height();
	$('#processlist').datagrid({	
		/**
		toolbar:[
					{
						text:'发布流程模板',
						iconCls:'icon-remove',
						handler:function(){
							publishProcessTemplate();
						}
					}],
		*/
		url: 'platform/bpm/bpmPublishAction/getPrcessNoPublishByPage.json?nodeType=catalog&id=-1',
		width: '100%',
	    nowrap: false,
	    striped: true,
	    autoRowHeight:false,
	    singleSelect:true,
	    checkOnSelect:true,
	    remoteSort : false,
		height: dataGridHeight,
		fitColumns: true,
		//sortName: 'startdate',  //排序字段,当表格初始化时候的排序字段
		//sortOrder: 'desc',    //定义排序顺序
		pagination:true,
		rownumbers:true,
		pageSize: 20,
		queryParams:{"":""},
		loadMsg:' 处理中，请稍候…',
		columns:[[
			{field:'id',hidden:true},
			{field:'op',title:'操作',width:25,align:'left',checkbox:true},
			{field:'processName',title:'模板名称',width:50,align:'left'},
			{field:'desc',title:'模板描述',width:50,align:'left'},
			{field:'designer',title:'设计者',width:50,align:'left'}
		]]
	});
	//设置分页控件   
	var p = $('#processlist').datagrid('getPager');
	$(p).pagination({
	    pageSize: 20,//每页显示的记录条数，默认为10
	    pageList: [5,10,15,20,25,30],//可以设置每页记录条数的列表
	    beforePageText: '第',//页数文本框前显示的汉字
	    afterPageText: '页    共 {pages} 页',
	    displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
	});
}
//初始化流程业务树 
function loadProcessTree(){
	$('#mytree').tree({   
		checkbox: false,   
		lines : true,
		method : 'post',
	    url:'platform/bpm/bpmPublishAction/getPrcessPublishTree.json?nodeType=catalog&id=root',  
	    onBeforeExpand:function(node,param){  
	    	 $('#mytree').tree('options').url = "platform/bpm/bpmPublishAction/getPrcessPublishTree.json?nodeType=catalog&id=" + node.id ;
	    },
	    onClick:function(node){
            clickTree(node);
      	}
	});  
}
//点击树事件 
function clickTree(node) {
	expand();
	$('#processlist').datagrid({ url: 'platform/bpm/bpmPublishAction/getPrcessNoPublishByPage.json?nodeType=catalog&id=' + node.id } );
	$("#processlist").datagrid('load'); 
}

//展开树
function expand() {
		var node = $('#mytree').tree('getSelected');
		if(node){
			$('#mytree').tree('expand',node.target);
		}else{
			$('#mytree').tree('expandAll');
		}
}
//发布流程模板
function publishProcessTemplate(){
	var data = $('#processlist').datagrid('getSelected');
	if (data == null || data=='') {
		$.messager.alert("操作提示", "请您选择一条记录！","info"); 
		return;
	}
	var id = data.id;
	$.messager.confirm("操作提示", "您确定要发布流程吗?", function(data) {
		if (data) {
			easyuiMask();
			ajaxRequest("POST", "id=" + id,
					"platform/bpm/bpmPublishAction/doDeployProcess", "json",
					"backChecked");
		}
	});
	
}

function backChecked(obj) {
	easyuiUnMask();
	if (obj != null && obj.success == true) {
		$.messager.show({
			title : '提示',
			msg : "操作成功！"
		});
		$('#processlist').datagrid('reload');	
	} else {
		$.messager.show({
			title : '提示',
			msg : "操作失败！"
		});
	}
}
</script>
<body class="easyui-layout" fit="true">
<div data-options="region:'west',title:'流程业务目录',split:true,collapsible:false"  style="width:180px;overflow: auto;">
	<ul id="mytree"> </ul>  
</div>  
<div region="center" border="false" style="overflow: hidden;">
	<table id="processlist"></table>
</div>
</div>
</body>
</html>