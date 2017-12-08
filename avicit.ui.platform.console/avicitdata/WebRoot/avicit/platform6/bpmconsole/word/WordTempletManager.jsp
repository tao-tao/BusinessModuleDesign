<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<html>
<head>

<%
	String path = request.getContextPath();
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>流程正文综合管理</title>
<base href="<%=ViewUtil.getRequestPath(request) %>">
<jsp:include page="/avicit/platform6/bpmclient/bpm/ProcessCommonJsInclude.jsp"></jsp:include>

</head>

<script type="text/javascript">
var baseurl = '<%=request.getContextPath()%>';
	var userAgent = navigator.userAgent.toLowerCase(); 
	var isFireFox=/mozilla/.test(userAgent) && !/(compatible|webkit)/.test(userAgent); 
	if(isFireFox) { 
	    window.showModelessDialog=function (url) 
	    { 
	        var windowName=(arguments[1]==null?"":arguments[1].toString()); 
	        var feature=(arguments[2]==null?"":arguments[2].toString()); 
	        var OpenedWindow=window.open(url,windowName,feature); 
	        window.addEventListener('click',function (){OpenedWindow.focus();},false); 
	        return OpenedWindow; 
	    } 
	} else { 
	    //子窗口中调用父窗口 
	    //IE中用window.parent.document 
	    //FF中用window.opener.document 
	    //下面的代码将 作用于IE '重载' window.showModelessDialog 方法 统一用 window.opener访问父窗口 
	    var originFn=window.showModelessDialog; 
	    window.showModelessDialog=function (url) 
	    { 
	        var OpenedWindow= originFn(url,arguments[1],arguments[2]); 
	        OpenedWindow.opener=window; 
	    } 
	} 

 
	$(function(){
		loadWordTemplet();
		
		$('#searchForm').find('input').on('keyup',function(e){
			if(e.keyCode == 13){
				searchFun();
			}
		});
	});
	
	function loadWordTemplet(){
		var dataGridHeight = $(".easyui-layout").height() - 60;
		$('#wordTempletlist').datagrid({
			toolbar:[
				{
				id : 'insertWordTemplet',
				text:'添加',
				iconCls:'icon-add',
				handler:function(){
					insertWordTemplet();
				}
			},'-',{
				text:'编辑',
				iconCls:'icon-edit',
				handler:function(){
					updateWordTemplet();
				}
			},'-',{
				text:'删除',
				iconCls:'icon-remove',
				handler:function(){
					deleteWordTemplet();
				}
			}],
			url: 'platform/bpm/bpmconsole/wordTempletAction/getWordTempletListByPage.json',
			width: '100%',
		    nowrap: false,
		    striped: true,
		    autoRowHeight:false,
		    singleSelect:true,
		    checkOnSelect:true,
		    selectOnCheck: false,
		    remoteSort : false,
			height: dataGridHeight,
			fitColumns: true,
			sortName: 'templetName',  //排序字段,当表格初始化时候的排序字段
			sortOrder: 'asc',    //定义排序顺序
			pagination:true,
			rownumbers:true,
			queryParams:{"filter_EQ_id":""},
			loadMsg:' 处理中，请稍候…',
			columns:[[
				{field:'op',title:'操作',width:25,align:'left',checkbox:true},
				{field:'templetName',title:'模板名称',width:15,align:'left',sortable:true},
				{field:'templetType',title:'模板类型',width:15,align:'left',
					formatter:function(value,rec){
	 					var key = rec.templetType;
	 					if(key == '1'){
	 						return "正文模板";
	 					} else if (key == '2'){
	 						return "红头模板";
	 					}
	 				}
				},
				{field:'templetVersion',title:'模板版本',width:15,align:'left',sortable:true},
				{field:'templetState',title:'模板状态',width:15,align:'left',
	 				formatter:function(value,rec){
	 					var key = rec.templetState;
	 					if(key == 'Y'){
	 						return "有效";
	 					} else {
	 						return "无效";
	 					}
	 				}
				},
				{field:'id',title:'查看模板',width:15,align:'center',
	  				formatter:function(value,rec){
	  					return '<img onclick="javascript:readWordTemplet(\''+rec.id+'\')" title="查看模板" src="static/images/platform/bpm/client/images/trackTask.gif" style="cursor:pointer"/>';
					}
				},
				{field:'id01',title:'编辑模板',width:15,align:'center',
	  				formatter:function(value,rec){
	  					return '<img onclick="javascript:editWordTemplet(\''+rec.id+'\')" title="编辑模板" src="static/images/platform/bpm/client/images/trackTask.gif" style="cursor:pointer"/>';
					}
				}
			]]
		});
		//设置分页控件   
		var p = $('#wordTempletlist').datagrid('getPager');
		$(p).pagination({
		    pageSize: 10,//每页显示的记录条数，默认为10
		    pageList: [5,10,15,20,25,30],//可以设置每页记录条数的列表
		    beforePageText: '第',//页数文本框前显示的汉字
		    afterPageText: '页    共 {pages} 页',
		    displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
		});
	}
	
	//查询
	function searchFun(){
		var queryParams = $('#wordTempletlist').datagrid('options').queryParams; 
		queryParams.filter_LIKE_templetName = $('#filter_LIKE_templetName').attr('value');
	    queryParams.filter_EQ_templetType =$('#toolbar select[comboname=filter_EQ_templetType]').combobox('getValue');
	    queryParams.filter_EQ_templetState =$('#toolbar select[comboname=filter_EQ_templetState]').combobox('getValue');
	    $('#wordTempletlist').datagrid('options').queryParams=queryParams;        
	    $("#wordTempletlist").datagrid('load'); 
	}
	
	function clearFun(){
		$('#searchForm input').val('');
		$('#searchForm select').val('');
	}
	
	var width = screen.availWidth;
	var height = screen.availHeight;
	//查看模板
	function readWordTemplet(id) {
		var url = getPath()+"/avicit/platform6/bpmconsole/word/WordTempletRead.jsp?wordId="+id;
		try{
			var arguments = "dialogWidth="+width+"px,dialogHeight="+height+"px";
			window.showModalDialog(url, '', arguments);
		}catch(e){
			var arguments = "width="+width+"px,height="+height+"px";
			window.showModelessDialog(url,'',arguments);	
		}
	}
	
	//编辑模板
	function editWordTemplet(id) {
		var url = getPath()+"/avicit/platform6/bpmconsole/word/WordTempletEdit.jsp?wordId="+id;
		try{
			var arguments = "dialogWidth="+width+"px,dialogHeight="+height+"px";
			window.showModalDialog(url, '', arguments);
		}catch(e){
			var arguments = "width="+width+"px,height="+height+"px";
			window.showModelessDialog(url,'',arguments);	
		}
	}
	
	//上传模板
	function updateWordTemplet(id) {
		//window.showDialog("../WordTempletRead.jsp?wordId="+id);
	}
	
	var usd;
	//新增
	function insertWordTemplet(){
		usd = new UserSelectDialog("insertWordTempletDialog","650","300","avicit/platform6/bpmconsole/word/WordTempletAdd.jsp","新增模板");
		var buttons = [{
			text:'提交',
			id : 'wordTempletSubimt',
			//iconCls : 'icon-submit',
			handler:function(){
				 var frmId = $('#insertWordTempletDialog iframe').attr('id');
				 var frm = document.getElementById(frmId).contentWindow;
				 var dataVo = frm.$('#form1').serializeArray();
				 var dataJson =frm.convertToJson(dataVo);
				 dataVo = JSON.stringify(dataJson);
				 ajaxRequest("POST","dataVo="+dataVo,"platform/bpm/bpmconsole/wordTempletAction/insertWordTemplet","json","insertWordTempletBack");
			}
		}];
		usd.createButtonsInDialog(buttons);
		usd.show();
	}
	function insertWordTempletBack(result){
		if(result.success==true){
			$('#wordTempletlist').datagrid('reload');	
			$.messager.show({title : '提示',msg : "保存成功。"});
			usd.close();
			usd=null;
		}else{
			$.messager.show({title : '提示',msg : "保存失败。"});
		}
	}
	
	//更新
	function updateWordTemplet(){
		var datas = $('#wordTempletlist').datagrid('getSelections');
		if(datas == null){
			$.messager.alert("操作提示", "请您选择一条记录!","info");
			return;
		}
		if(datas.length > 1){
			$.messager.alert("操作提示", "一次只能编辑一条记录!","info");
			return;
		}
		var id = datas[0].id;
		usd = new UserSelectDialog("insertWordTempletDialog","650","300","avicit/platform6/bpmconsole/word/WordTempletAdd.jsp?id="+id,"编辑模板");
		var buttons = [{
			text:'提交',
			id : 'wordTempletSubimt',
			//iconCls : 'icon-submit',
			handler:function(){
				 var frmId = $('#insertWordTempletDialog iframe').attr('id');
				 var frm = document.getElementById(frmId).contentWindow;
				 var dataVo = frm.$('#form1').serializeArray();
				 var dataJson =frm.convertToJson(dataVo);
				 dataVo = JSON.stringify(dataJson);
				 ajaxRequest("POST","dataVo="+dataVo,"platform/bpm/bpmconsole/wordTempletAction/insertWordTemplet","json","insertWordTempletBack");
			}
		}];
		usd.createButtonsInDialog(buttons);
		usd.show();
	}
	function updateWordTempletBack(result){
		if(result.success==true){
			$('#wordTempletlist').datagrid('reload');	
			$.messager.show({title : '提示',msg : "保存成功。"});
			usd.close();
			usd=null;
		}else{
			$.messager.show({title : '提示',msg : "保存失败。"});
		}
	}
	
	//删除
	function deleteWordTemplet(){
		//var datas = $('#wordTempletlist').datagrid('getSelections');
		var datas = $('#wordTempletlist').datagrid('getChecked');
		if(datas == null){
			$.messager.alert("操作提示", "请您选择一条记录!","info");
			return;
		}
		var ids = '';
		for(var i=0;i<datas.length;i++){
			ids += datas[i].id + ';';
		}
		$.messager.confirm("操作提示", "您确认要删除选定的数据吗？", function (data) {
	        if (data) {
	        	easyuiMask();
	        	ajaxRequest("POST","id="+ids,"platform/bpm/bpmconsole/wordTempletAction/deleteWordTemplet","json","deleteWordTempletBack");
	        }
		 });
	}
	function deleteWordTempletBack(json){
	    $.messager.show({title : '提示',msg : "删除成功!"});
	    easyuiUnMask();
		$("#wordTempletlist").datagrid('reload'); 
	}
	
	
</script>
<body class="easyui-layout" fit="true">
	<div region="center" border="false" style="overflow: hidden;">
		<div id="toolbar" class="datagrid-toolbar" style="height: auto;display: block;">
			<fieldset>
				<legend>查询条件</legend>
				<table class="tableForm" id="searchForm">
					<tr>
					    <td style="width:50px"></td>
						<td>模板名称</td>
						<td colspan="2"><input id="filter_LIKE_templetName" name="filter_LIKE_templetName"  class="easyui-inputbox" style="width: 150px;" /></td>
						<td style="width:50px"></td>
						
						<td>模板类型</td>
						<td colspan="2">
							<select id="filter_EQ_templetType" name="filter_EQ_templetType" class="easyui-combobox" style="width:155px;" >
						  		<option value="1">正文模板</option>
						  		<option value="2">红头模板</option>
							</select>
						</td>
						<td style="width:50px"></td>
						
						<td>模板状态</td>
						<td >
							<select id="filter_EQ_templetState" name="filter_EQ_templetState" class="easyui-combobox" style="width:155px;" >
						  		<option value="Y">有效</option>
						  		<option value="N">无效</option>
							</select>
						</td>
						<td style="width:50px"></td>
						<td ><a class="easyui-linkbutton"   iconCls="icon-search" plain="true" onclick="searchFun();" href="javascript:void(0);">查询</a><a class="easyui-linkbutton" iconCls="icon-cancel" plain="true" onclick="clearFun();" href="javascript:void(0);">清空</a>
						</td>
					</tr>
				</table>
			</fieldset>
		</div>
		<table id="wordTempletlist"></table>
	</div>
</body>
</html>