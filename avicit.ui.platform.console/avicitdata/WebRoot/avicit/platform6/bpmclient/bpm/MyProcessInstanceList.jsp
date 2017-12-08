<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<html>
<head>
<%
	String tab = request.getParameter("tab");
	String startDate = request.getParameter("startDate");
	String endDate = request.getParameter("endDate");
%>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>流程实例信息</title>
<base href="<%=ViewUtil.getRequestPath(request) %>">
<jsp:include page="/avicit/platform6/bpmclient/bpm/ProcessCommonJsInclude.jsp"></jsp:include>
<script src="static/js/platform/component/fusionchar/js/FusionCharts.js" type="text/javascript"></script>
</head>

<script type="text/javascript">
var baseurl = '<%=request.getContextPath()%>';
var tabSelectedIndex = "0"; //那个标签被选中
/**
 * 页面请求入口
 */
$(function(){
	loadProcessTree();
	myProcessInstanceBegin();
	init();
	window.setTimeout("expand()", 500);
});
/**
 * 初始化页面参数
 */
function init() {
	var tab = '<%=tab%>';
	if (tab != "undefind" && tab != "" && tab != "null") {
		if (tab != "Todo") {
			$("#instance"+tab+"_startDateBegin").datebox("setValue","<%=startDate%>"); 
			$("#instance"+tab+"_startDateEnd").datebox("setValue","<%=endDate%>"); 
		}
		if (tab == "Start") $('#myProcessTab').tabs("select" , 0);
		if (tab == "Todo") $('#myProcessTab').tabs("select" , 1);
		if (tab == "Finish") $('#myProcessTab').tabs("select" , 2);
	}
}
/**
 * 加载流程业务目录树
 */
function loadProcessTree(){
	$('#mytree').tree({   
		checkbox: false,   
		lines : true,
		method : 'post',
		animate:true,
	    url:'platform/bpm/clientbpmdisplayaction/getProcessStartTree.json?id=root',  
	    onBeforeExpand:function(node,param){
	    	$('#mytree').tree('options').url = "platform/bpm/clientbpmdisplayaction/getProcessStartTree.json?id=" + node.id ;
	    },
	    onClick:function(node){
            clickTree(node);
      	}
	});  
}
/**
 * 点击树节点操作
 */
function clickTree(node) {
	expand();
	var nodeType = node.attributes.nodeType;
	if(nodeType!=null&&nodeType=="catalog"){
		$('#instanceStart_catalogId').val(node.id);
		$('#instanceStart_pdId').val("");
		$('#instanceTodo_catalogId').val(node.id);
		$('#instanceTodo_pdId').val("");
		$('#instanceFinish_catalogId').val(node.id);
		$('#instanceFinish_pdId').val("");
	}else if(nodeType!=null&&nodeType=="process"){
		$('#instanceStart_catalogId').val("");
		$('#instanceStart_pdId').val(node.id);
		$('#instanceTodo_catalogId').val("");
		$('#instanceTodo_pdId').val(node.id);
		$('#instanceFinish_catalogId').val(node.id);
		$('#instanceFinish_pdId').val("");
	}

	if(tabSelectedIndex=="0"){
		searchInstanceStart();
	}else if(tabSelectedIndex=="1"){
		searchInstanceTodo();
	}else if(tabSelectedIndex=="2"){
		searchInstanceFinish();
	}else if(tabSelectedIndex=="3"){
		//searchInstanceFinish();
	}
}
/**
 * 展开树节点操作
 */
function expand() {
	var node = $('#mytree').tree('getSelected');
	if(node){
		$('#mytree').tree('expand',node.target);
	}else{
		$('#mytree').tree('expandAll');
	}
}
/**
 * 入口
 */
function myProcessInstanceBegin(){
	$('#myProcessTab').tabs({
	    onSelect:function(title,index){
	       if(index == 0){
	    	   tabSelectedIndex = "0";
	    	   searchInstanceStart();
	       }
		   if(index == 1){
			   tabSelectedIndex = "1";
			   searchInstanceTodo();
			}
			if(index == 2){
				tabSelectedIndex = "2";
				searchInstanceFinish();
			}
			if(index == 3){
				tabSelectedIndex = "3";
				//searchAutoActDurationTop10();
			}
	    }   
	});
}
/////////////////////////////////////////////////////////////////////////////////
/**
 * 我启动的流程实例查询
 */
function searchInstanceStart(){
	var startDateBegin =$('#instanceStart_startDateBegin').datetimebox('getValue');
	var startDateEnd =$('#instanceStart_startDateEnd').datetimebox('getValue');
	var catalogId = $('#instanceStart_catalogId').val();
	var pdId = $('#instanceStart_pdId').val();
	var queryParas = "catalogId="+catalogId+"&startDateBegin="+startDateBegin+"&startDateEnd="+startDateEnd+"&pdId="+pdId;
	getInstanceStart(queryParas);
}
/**
 * 清空我启动的流程实例查询条件
 */
function clearInstanceStart(){
	 $('#instanceStart_catalogId').val('');
	 $('#instanceStart_startDateBegin').datebox("setValue","");
	 $('#instanceStart_startDateEnd').datebox("setValue","");
	 $('#instanceStart_pdId').val('');
}
/**
 * 我启动的流程实例查询ajax请求
 */
function getInstanceStart(queryParas){
	var dataGridHeight = $(".easyui-layout").height()-120;
	$('#instanceStartDiv').datagrid({
		url: 'platform/bpm/clientbpmdisplayaction/getProcessInstanceListByPage.json?'+queryParas,
		width: '100%',
	    nowrap: false,
	    striped: true,
	    autoRowHeight:false,
	    singleSelect:false,
	    checkOnSelect:false,
	    remoteSort : false,
		height: dataGridHeight,
		fitColumns: true,
		sortName: 'startdate',  //排序字段,当表格初始化时候的排序字段
		sortOrder: 'desc',    //定义排序顺序
		pagination:true,
		pageSize:20,
		rownumbers:true,
		queryParams:{"":""},
		loadMsg:' 处理中，请稍候…',
		columns:[[
			{field:'id',hidden:true},
			{field:'definename',title:'流程定义名称',width:60,align:'left',sortable:true},
			{field:'entryname',title:'流程实例名称',width:50,align:'left',sortable:true,
				formatter:function(value,rec){
  					var processInstance = "'"+rec.entryid+"'";
  					var state = "'"+rec.state+"'";
  					var id =  "'"+rec.id+"'";
  					return '<a href="javascript:window.trackBpm('+processInstance+')">'+value+'</a>';
  				}},
  			{field:'state',title:'状态',width:40,align:'left',sortable:true,
  					formatter:function(value){
  						 if(value=='active'){
  		                        return '流转中';
  		                    }else if(value=='ended'){
  		                        return '结束';
  		                    }else if(value=='suspended'){
  		                        return '挂起';
  		                    }

  					}
  			},
			{field:'userid',title:'创建者',width:25,align:'left',sortable:true},
			{field:'deptid',title:'创建部门',width:50,align:'left',sortable:true},
			{field:'startdate',title:'启动时间',width:50,align:'left',sortable:true,
  				formatter:function(value,rec){
  					var startdateMi=rec.startdate;
  					if(startdateMi==undefined){
  						return;
  					}
  					var newDate=new Date(startdateMi);
  					return newDate.Format("yyyy-MM-dd hh:mm:ss");   
				}},
			{field:'enddate',title:'结束时间',width:50,align:'left',editor:'text',
  				formatter:function(value,rec){
  					var endateMi=rec.enddate;
  					if(endateMi==undefined){
  						return;
  					}
  					var newDate=new Date(endateMi);
  					return newDate.Format("yyyy-MM-dd hh:mm:ss");   
				}}
		]]
	});
	//设置分页控件   
	var p = $('#instanceStartDiv').datagrid('getPager');
	$(p).pagination({
	    pageSize: 20,//每页显示的记录条数，默认为10
	    pageList: [5,10,15,20,25,30],//可以设置每页记录条数的列表
	    beforePageText: '第',//页数文本框前显示的汉字
	    afterPageText: '页    共 {pages} 页',
	    displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
	});
}
/**
 * 流程跟踪
 */
function trackBpm(processInstanceId){
	var url = getPath()+"/avicit/platform6/bpmclient/bpm/ProcessTrack.jsp";
	url += "?processInstanceId="+processInstanceId;
	window.open(encodeURI(url),"流程图","scrollbars=no,status=yes,resizable=no,top=0,left=0,width=700,height=400");
}
//////////////////////////////////////////////////////////////////////////////
/**
 * 我处理中的流程实例查询
 */
function searchInstanceTodo(){
	var startDateBegin =$('#instanceTodo_startDateBegin').datetimebox('getValue');
	var startDateEnd =$('#instanceTodo_startDateEnd').datetimebox('getValue');
	var catalogId = $('#instanceTodo_catalogId').val();
	var pdId = $('#instanceTodo_pdId').val();
	var queryParas = "catalogId="+catalogId+"&startDateBegin="+startDateBegin+"&startDateEnd="+startDateEnd+"&pdId="+pdId;
	getInstanceTodo(queryParas);
}
/**
 * 清空我处理中的流程实例查询条件
 */
function clearInstanceTodo(){
	 $('#instanceTodo_catalogId').val('');
	 $('#instanceTodo_startDateBegin').datebox("setValue","");
	 $('#instanceTodo_startDateEnd').datebox("setValue","");
	 $('#instanceTodo_pdId').val('');
}
/**
 * 我处理中的流程实例查询ajax请求
 */
function getInstanceTodo(queryParas){
	var dataGridHeight = $(".easyui-layout").height()-120;
	$('#instanceTodoDiv').datagrid({
		url: 'platform/bpm/clientbpmdisplayaction/getProcessInstanceTodoListByPage.json?isFinished=0&'+queryParas,
		width: '100%',
	    nowrap: false,
	    striped: true,
	    autoRowHeight:false,
	    singleSelect:false,
	    checkOnSelect:false,
	    remoteSort : false,
		height: dataGridHeight,
		fitColumns: true,
		sortName: 'startdate',  //排序字段,当表格初始化时候的排序字段
		sortOrder: 'desc',    //定义排序顺序
		pagination:true,
		pageSize:20,
		rownumbers:true,
		queryParams:{"":""},
		loadMsg:' 处理中，请稍候…',
		columns:[[
			{field:'id',hidden:true},
			{field:'definename',title:'流程定义名称',width:60,align:'left',sortable:true},
			{field:'entryname',title:'流程实例名称',width:50,align:'left',sortable:true,
				formatter:function(value,rec){
  					var processInstance = "'"+rec.entryid+"'";
  					var state = "'"+rec.state+"'";
  					var id =  "'"+rec.id+"'";
  					return '<a href="javascript:window.trackBpm('+processInstance+')">'+value+'</a>';
  				}},
  			{field:'state',title:'状态',width:40,align:'left',sortable:true,
  					formatter:function(value){
  						 if(value=='active'){
  		                        return '流转中';
  		                    }else if(value=='ended'){
  		                        return '结束';
  		                    }else if(value=='suspended'){
  		                        return '挂起';
  		                    }

  					}
  			},
			{field:'userid',title:'创建者',width:25,align:'left',sortable:true},
			{field:'deptid',title:'创建部门',width:50,align:'left',sortable:true},
			{field:'startdate',title:'启动时间',width:50,align:'left',sortable:true,
  				formatter:function(value,rec){
  					var startdateMi=rec.startdate;
  					if(startdateMi==undefined){
  						return;
  					}
  					var newDate=new Date(startdateMi);
  					return newDate.Format("yyyy-MM-dd hh:mm:ss");   
				}},
			{field:'enddate',title:'结束时间',width:50,align:'left',editor:'text',
  				formatter:function(value,rec){
  					var endateMi=rec.enddate;
  					if(endateMi==undefined){
  						return;
  					}
  					var newDate=new Date(endateMi);
  					return newDate.Format("yyyy-MM-dd hh:mm:ss");   
				}}
		]]
	});
	//设置分页控件   
	var p = $('#instanceTodoDiv').datagrid('getPager');
	$(p).pagination({
	    pageSize: 20,//每页显示的记录条数，默认为10
	    pageList: [5,10,15,20,25,30],//可以设置每页记录条数的列表
	    beforePageText: '第',//页数文本框前显示的汉字
	    afterPageText: '页    共 {pages} 页',
	    displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
	});
}
//////////////////////////////////////////////////////////////////////////////
/**
 * 我经办过流程实例查询
 */
function searchInstanceFinish(){
	var startDateBegin =$('#instanceFinish_startDateBegin').datetimebox('getValue');
	var startDateEnd =$('#instanceFinish_startDateEnd').datetimebox('getValue');
	var catalogId = $('#instanceFinish_catalogId').val();
	var pdId = $('#instanceFinish_pdId').val();
	var queryParas = "catalogId="+catalogId+"&startDateBegin="+startDateBegin+"&startDateEnd="+startDateEnd+"&pdId="+pdId;
	getInstanceFinish(queryParas);
}
/**
 * 清空我经办过流程实例条件
 */
function clearInstanceFinish(){
	 $('#instanceFinish_catalogId').val('');
	 $('#instanceFinish_startDateBegin').datebox("setValue","");
	 $('#instanceFinish_startDateEnd').datebox("setValue","");
	 $('#instanceFinish_pdId').val('');
}
/**
 * 我经办过流程实例查询ajax请求
 */
function getInstanceFinish(queryParas){
	var dataGridHeight = $(".easyui-layout").height()-120;
	$('#instanceFinishDiv').datagrid({
		url: 'platform/bpm/clientbpmdisplayaction/getProcessInstanceTodoListByPage.json?isFinished=1&'+queryParas,
		width: '100%',
	    nowrap: false,
	    striped: true,
	    autoRowHeight:false,
	    singleSelect:false,
	    checkOnSelect:false,
	    remoteSort : false,
		height: dataGridHeight,
		fitColumns: true,
		sortName: 'startdate',  //排序字段,当表格初始化时候的排序字段
		sortOrder: 'desc',    //定义排序顺序
		pagination:true,
		pageSize:20,
		rownumbers:true,
		queryParams:{"":""},
		loadMsg:' 处理中，请稍候…',
		columns:[[
			{field:'id',hidden:true},
			{field:'definename',title:'流程定义名称',width:60,align:'left',sortable:true},
			{field:'entryname',title:'流程实例名称',width:50,align:'left',sortable:true,
				formatter:function(value,rec){
  					var processInstance = "'"+rec.entryid+"'";
  					var state = "'"+rec.state+"'";
  					var id =  "'"+rec.id+"'";
  					return '<a href="javascript:window.trackBpm('+processInstance+')">'+value+'</a>';
  				}},
  			{field:'state',title:'状态',width:40,align:'left',sortable:true,
  					formatter:function(value){
  						 if(value=='active'){
  		                        return '流转中';
  		                    }else if(value=='ended'){
  		                        return '结束';
  		                    }else if(value=='suspended'){
  		                        return '挂起';
  		                    }

  					}
  			},
			{field:'userid',title:'创建者',width:25,align:'left',sortable:true},
			{field:'deptid',title:'创建部门',width:50,align:'left',sortable:true},
			{field:'startdate',title:'启动时间',width:50,align:'left',sortable:true,
  				formatter:function(value,rec){
  					var startdateMi=rec.startdate;
  					if(startdateMi==undefined){
  						return;
  					}
  					var newDate=new Date(startdateMi);
  					return newDate.Format("yyyy-MM-dd hh:mm:ss");   
				}},
			{field:'enddate',title:'结束时间',width:50,align:'left',editor:'text',
  				formatter:function(value,rec){
  					var endateMi=rec.enddate;
  					if(endateMi==undefined){
  						return;
  					}
  					var newDate=new Date(endateMi);
  					return newDate.Format("yyyy-MM-dd hh:mm:ss");   
				}}
		]]
	});
	//设置分页控件   
	var p = $('#instanceFinishDiv').datagrid('getPager');
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
<div data-options="region:'west',title:'流程业务目录',split:false" style="width:200px;overflow: auto;">
	<ul id="mytree"></ul>  
</div>  
<div region="center" border="false" style="overflow: hidden;height:0px;">
		<div  id="myProcessTab">
			<div title="我发起的流程" style="padding:10px;width:auto">
				<div class="datagrid-toolbar" style="height:auto;display: block;">
				<fieldset>
					<legend>查询条件</legend>
					<table class="tableForm" width='100%' align="left">
						<tr>
							<td style="width:70px" align="left">&nbsp;开始时间：</td>
							<td style="width:160px" align="left"><input name="startDateBegin" id="instanceStart_startDateBegin" class="easyui-datebox" editable="false" style="width:150px;" /></td>
							<td style="width:70px" align="left">&nbsp;结束时间：</td>
							<td style="width:160px" align="left"><input name="startDateEnd" id="instanceStart_startDateEnd"  class="easyui-datebox" editable="false" style="width: 150px;" /></td>
							<td  align="left">
							<input  type="hidden"  name="catalogId" id="instanceStart_catalogId"     />
							<input  type="hidden"  name="pdId" id="instanceStart_pdId"     />
							<a class="easyui-linkbutton"   iconCls="icon-search" plain="true" onclick="searchInstanceStart();" href="javascript:void(0);">查询</a><a class="easyui-linkbutton" iconCls="icon-cancel" plain="true" onclick="clearInstanceStart();" href="javascript:void(0);">清空</a>
							</td>
						</tr>
					</table>
				</fieldset>
				</div>
				<br>
				<table id="instanceStartDiv"></table>
			</div>
			<div title="我处理中的流程" style="padding:10px;width:auto">
				<div class="datagrid-toolbar" style="height:auto;display: block;">
				<fieldset>
					<legend>查询条件</legend>
					<table class="tableForm" width='100%' align="left">
						<tr>
							<td style="width:70px" align="left">&nbsp;开始时间：</td>
							<td style="width:160px" align="left"><input name="startDateBegin" id="instanceTodo_startDateBegin" class="easyui-datebox" editable="false" style="width:150px;" /></td>
							<td style="width:70px" align="left">&nbsp;结束时间：</td>
							<td style="width:160px" align="left"><input name="startDateEnd" id="instanceTodo_startDateEnd"  class="easyui-datebox" editable="false" style="width: 150px;" /></td>
							<td  align="left">
							<input  type="hidden"  name="catalogId" id="instanceTodo_catalogId"     />
							<input  type="hidden"  name="pdId" id="instanceTodo_pdId"     />
							<a class="easyui-linkbutton"   iconCls="icon-search" plain="true" onclick="searchInstanceTodo();" href="javascript:void(0);">查询</a><a class="easyui-linkbutton" iconCls="icon-cancel" plain="true" onclick="clearInstanceTodo();" href="javascript:void(0);">清空</a>
							</td>
						</tr>
					</table>
				</fieldset>
				</div>
				<br>
				<table id="instanceTodoDiv"></table>
			</div>
			<div title="我经办过的流程" style="padding:10px;width:auto">
				<div class="datagrid-toolbar" style="height:auto;display: block;">
				<fieldset>
					<legend>查询条件</legend>
					<table class="tableForm" width='100%' align="left">
						<tr>
							<td style="width:70px" align="left">&nbsp;开始时间：</td>
							<td style="width:160px" align="left"><input name="startDateBegin" id="instanceFinish_startDateBegin" class="easyui-datebox" editable="false" style="width:150px;" /></td>
							<td style="width:70px" align="left">&nbsp;结束时间：</td>
							<td style="width:160px" align="left"><input name="startDateEnd" id="instanceFinish_startDateEnd"  class="easyui-datebox" editable="false" style="width: 150px;" /></td>
							<td  align="left">
							<input  type="hidden"  name="catalogId" id="instanceFinish_catalogId"     />
							<input  type="hidden"  name="pdId" id="instanceFinish_pdId"     />
							<a class="easyui-linkbutton"   iconCls="icon-search" plain="true" onclick="searchInstanceFinish();" href="javascript:void(0);">查询</a><a class="easyui-linkbutton" iconCls="icon-cancel" plain="true" onclick="clearInstanceFinish();" href="javascript:void(0);">清空</a>
							</td>
						</tr>
					</table>
				</fieldset>
				</div>
				<br>
				<table id="instanceFinishDiv"></table>
			</div>
	</div>
</div>
</body>
</html>