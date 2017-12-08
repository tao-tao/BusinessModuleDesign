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
		loadWordTemplet();
	});
	
	function loadWordTemplet(){
		var dataGridHeight = $(".easyui-layout").height() - 60;
		$('#ideaStyleTable').datagrid({
			toolbar:[
				{
				id : 'insertIdeaStyle',
				text:'添加',
				iconCls:'icon-add',
				handler:function(){
					insertIdeaStyle();
				}
			},'-',{
				id : 'deleteIdeaStyle',
				text:'删除',
				iconCls:'icon-remove',
				handler:function(){
					deleteIdeaStyle();
				}
			}],
			url: 'platform/bpm/bpmconsole/ideaStyleManagerAction/getBpmIdeaStyleList.json?pdId=<%=pdId%>',
			width: '100%',
		    nowrap: false,
		    striped: true,
		    autoRowHeight:false,
		    singleSelect:false,
		    checkOnSelect:false,
		    remoteSort : false,
			height: dataGridHeight,
			fitColumns: true,
			sortName: 'orderBy',  //排序字段,当表格初始化时候的排序字段
			sortOrder: 'asc',    //定义排序顺序
			pagination:false,
			rownumbers:true,
			queryParams:{"":""},
			loadMsg:' 处理中，请稍候…',
			columns:[[
			    {field:'id',hidden:true},
				{field:'op',title:'操作',width:25,align:'left',checkbox:true},
				{field:'title',title:'名称',width:80,align:'left',sortable:true},
				{field:'code',title:'代码',width:50,align:'left',sortable:true},
				{field:'activityName',title:'节点',width:200,align:'left',sortable:true},
				{field:'positionName',title:'岗位',width:200,align:'left',sortable:true},
				{field:'styleName',title:'样式',width:110,align:'left',sortable:true},
				{field:'type',title:'类型',width:40,align:'left',
					formatter:function(value,rec){
	 					var key = rec.type;
	 					if(key == '0'){
	 						return "自用类型";
	 					} else if (key == '1'){
	 						return "引用类型";
	 					}
	 				}
				},
				{field:'orderBy',title:'排序',width:15,align:'left',sortable:true}
			]]
		});
	}
	

	var usd;
	//新增
	function insertIdeaStyle(){		
		usd = new UserSelectDialog("processFormAddDialog","750","480","avicit/platform6/bpmconsole/ideastyle/IdeaStyleAdd.jsp?pdId=<%=pdId%>","新增");
		var buttons = [{
			text:'提交',
			id : 'processFormSubimt',
			//iconCls : 'icon-submit',
			handler:function(){
				 var frmId = $('#processFormAddDialog iframe').attr('id');
				 var frm = document.getElementById(frmId).contentWindow;
				 var dataVo = frm.$('#form1').serializeArray();
				 var dataJson =frm.convertToJson(dataVo);
				 dataVo = JSON.stringify(dataJson);
				 ajaxRequest("POST","dataVo="+dataVo,"platform/bpm/bpmconsole/ideaStyleManagerAction/addBpmIdeaStyle","json","insertIdeaStyleBack");
			}
		}];
		usd.createButtonsInDialog(buttons);
		usd.show();
	}
	function insertIdeaStyleBack(result){
		if(result.flag=="ok"){
			$('#ideaStyleTable').datagrid('reload');	
			$.messager.show({title : '提示',msg : "保存成功。"});
			usd.close();
			usd=null;
		}else{
			$.messager.show({title : '提示',msg : "保存失败。"});
		}
	}
	
	
	//删除
	function deleteIdeaStyle(){
		var datas = $('#ideaStyleTable').datagrid('getSelections');
		if(datas == null){
			$.messager.alert("操作提示", "请您选择一条记录!","info");
			return;
		}
		var ids = '';
		for(var i=0;i<datas.length;i++){
			ids += datas[i].id + ',';
		}
		$.messager.confirm("操作提示", "您确认要删除选定的数据吗？", function (data) {
	        if (data) {
	        	easyuiMask();
	        	ajaxRequest("POST","dbId="+ids,"platform/bpm/bpmconsole/ideaStyleManagerAction/deleteBpmIdeaStyle","json","deleteIdeaStyleBack");
	        }
		 });
	}
	function deleteIdeaStyleBack(json){
	    $.messager.show({title : '提示',msg : "删除成功!"});
	    easyuiUnMask();
		$("#ideaStyleTable").datagrid('reload'); 
	}
	
	
</script>
<body class="easyui-layout" fit="true">
	<div region="center" border="false" style="overflow: hidden;">
		<table id="ideaStyleTable"></table>
	</div>
</body>
</html>