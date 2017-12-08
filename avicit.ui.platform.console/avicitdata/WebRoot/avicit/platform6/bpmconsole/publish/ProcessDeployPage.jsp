<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<html>
<head>
<%
	String deploymentId = request.getParameter("deploymentId");
	String processDefId = request.getParameter("processDefId");
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>流程配置页面</title>
<base href="<%=ViewUtil.getRequestPath(request) %>">
<jsp:include page="/avicit/platform6/bpmclient/bpm/ProcessCommonJsInclude.jsp"></jsp:include>
</head>
<script type="text/javascript">
var deploymentId = '<%=deploymentId%>';
var processDefId = '<%=processDefId%>';
var baseurl = '<%=request.getContextPath()%>';
var oldTaskDiv=null;
var globeTaskId = '';
//加载流程信息
function loadProcessImage(obj){
	ajaxRequest(
			"POST",
			"deploymentId=" + deploymentId + "&processDefId=" + processDefId + "&formType=" + obj,
			"platform/bpm/bpmPublishAction/getProcessImageCoordinate",
			"json", "draw");
}
function draw(obj){
	//画流程图
	$("#bpmImageDiv").html(obj.image);
	//初始化节点配置任务
	$('#pdActivityName').combobox('loadData',obj.data); 
}
$(function() {
$('#processNodes').tabs({
			onSelect : function(title, index) {
				if (index == 1) {
					loadData2();
				}
			}
		});
});
//初始化数据
function loadData(){
	loadProcessImage();
	globalProcessForm();
}
function loadData2(){
	var dataGridHeight = $(".easyui-layout").height()-75; 
	var lastIndex;
	$('#processForm').datagrid({
		toolbar:[{
            id:'btnadd',
            text:'添加',
            iconCls:'icon-add',
            handler:function(){
            	if(globeTaskId=='' || globeTaskId == null || globeTaskId==undefined){
            		$.messager.alert("操作提示", "请选择要导入资源的流程节点！","info"); 
            	}
                $('#processForm').datagrid('appendRow',{
                	pdActivityName:globeTaskId,
                	pdId:processDefId,
                	tag:'',
                	tagName:'',
                	accessibility:'1',
                	operability:'1'
                });
                lastIndex = $('#processForm').datagrid('getRows').length-1;
				$('#processForm').datagrid('selectRow', lastIndex);
				$('#processForm').datagrid('beginEdit', lastIndex);
        	}
        },'-',{
		    text:'保存',
		    iconCls:'icon-save',
		    handler:function(){
		    	endEdit();
		    	if($('#processForm').datagrid('getChanges').length){
		    		var updated = $('#processForm').datagrid('getChanges','updated');
		    		var inserted = $('#processForm').datagrid('getChanges','inserted');
		    		var deleted = $('#processForm').datagrid('getChanges','deleted');
		    		var effectRow = new Object();  
                    if (inserted.length) {  
                        effectRow["inserted"] = JSON.stringify(inserted);  
                    }  
                    if (deleted.length) {  
                        effectRow["deleted"] = JSON.stringify(deleted);  
                    }  
                    if (updated.length) {  
                        effectRow["updated"] = JSON.stringify(updated);  
                    }  
                    $.post("platform/bpm/bpmPublishAction/saveProcessNodeComponent.json", effectRow, function(rsp) {
                        if(rsp.flag=='success'){  
                        	$.messager.show({
    		    				title : '提示',
    		    				msg : "操作成功！"
    		    			}); 
                            $('#processForm').datagrid('acceptChanges');  
                        }  
                    }, "JSON").error(function() {  
                    	$.messager.show({
		    				title : '提示',
		    				msg : "操作失败！"
		    			});  
                    });  


		    	}
		    }
		}
        ,'-',{
		    text:'导入资源',
		    iconCls:'icon-redo',
		    handler:function(){
		    	if(globeTaskId=='' || globeTaskId == null || globeTaskId==undefined){
		    		$.messager.alert("操作提示", "请选择要导入资源的流程节点！","info"); 
		    		return;
		    	}
		    	inputUrl(globeTaskId,processDefId);
		    }
		}
        ,'-',{
		    text:'删除',
		    iconCls:'icon-remove',
		    handler:function(){
		    	var rows =$('#processForm').datagrid('getSelections');  
		    	if (rows == null || rows=='') {
					$.messager.alert("操作提示", "请您选择一条记录！","info"); 
					return;
				}
				for ( var i = 0; i < rows.length; i++) {
					var row = rows[i];
					if (row) {  
	                    var rowIndex = $('#processForm').datagrid('getRowIndex', row);  
	                    $('#processForm').datagrid('deleteRow', rowIndex);  
	                }  
				}
                
		    }
		},'-',{
		    text:'刷新缓存',
		    iconCls:'icon-reload',
		    handler:function(){
		    	refreshCache();
		    }
		}],
		url: 'platform/bpm/bpmPublishAction/findComponentFromUrl.json?pdActivityName=' + globeTaskId + '&pdId=' + processDefId,
	    nowrap: true,
	    autoRowHeight: false,
	    striped: true,
	    height:dataGridHeight,
	    singleSelect:false,
	    checkOnSelect:true,
	    collapsible:true,
	    remoteSort: false,
	    onBeforeLoad:function(){
			$(this).datagrid('rejectChanges');
		},
		onClickRow:function(rowIndex){
			if(lastIndex==0 && rowIndex==0){
				$('#processForm').datagrid('beginEdit', rowIndex);
			}
			if (lastIndex != rowIndex){
				$('#processForm').datagrid('endEdit', lastIndex);
				$('#processForm').datagrid('beginEdit', rowIndex);
			}
			lastIndex = rowIndex;
		},
	    pagination:false,
	    rownumbers:true,
	    columns:[[
				  {field:'id',hidden:true},
				  {field:'pdActivityName',hidden:true},
				  {field:'pdId',hidden:true},
				  {field:'op',title:'操作',width:25,align:'left',checkbox:true},
	              {field:'tag',title:'控件',width:200,editor:{type:'text'}},
	              {field:'tagName',title:'名称',width:260,rowspan:2,editor:{type:'text',required:true}},
	              {field:'accessibility',title:'读',width:80,rowspan:2,
	            	  editor:{type:'combobox',options:{data:[{'value':'1','text':'是'},{'value':'0','text':'否'}],height:20}},
	            	  formatter:function(value,rec){
		            		var temp = rec.accessibility;
		            		if(temp==1){
		            			return '是';
		            		}else{
		            			return '否';
		            		}
	            	  }
	              },
	              {field:'operability',title:'写',width:80,rowspan:2,
	              	editor:{type:'combobox',options:{data:[{'value':'1','text':'是'},{'value':'0','text':'否'}]}},
	              	formatter:function(value,rec){
	            		var temp = rec.operability;
	            		if(temp==1){
	            			return '是';
	            		}else{
	            			return '否';
	            		}
            	  }
	              }
	          ]]
	});
	function endEdit(){
		var rows = $('#processForm').datagrid('getRows');
		for(var i=0;i<rows.length;i++){
			$('#processForm').datagrid('endEdit',i);
		}
	}
}
function inputUrl(nodeId,processDefId){
	var url = getPath() + "/avicit/platform6/bpmconsole/publish/InputFormUrl.jsp";
	url += '?nodeId=' + nodeId +  '&processDefId=' + processDefId;
	var usd = new UserSelectDialog("inputuri", "500", "120",encodeURI(url), "请输入要导入的URL");
	var buttons = [{
		text : '确定',
		id : 'processSubimt',
		//iconCls : 'icon-submit',
		handler : function() {
			var frmId = $('#inputuri iframe').attr('id');
			var frm = document.getElementById(frmId).contentWindow;
			var formUrl = frm.submits();
			if(formUrl!='' && formUrl!='undefined' && formUrl!=null){
				easyuiMask();
				ajaxRequest(
						"POST",
						"formUrl=" + formUrl + "&pdId=" + processDefId + "&pdActivityName=" + nodeId,
						"platform/bpm/bpmPublishAction/findComponentFromUrl",
						"json", "backUrl");
				usd.close();
			}
		}
	}];
	usd.createButtonsInDialog(buttons);
	usd.show();
}
//选择流程表单 nodeId节点id processDefId流程定义id,type：global,activity,start
function selectForm(type){
	var url = getPath() + "/avicit/platform6/bpmconsole/publish/ProcessFormList.jsp";
	url += '?processDefId=' + processDefId;
	var usd = new UserSelectDialog("lcbdxzk", "700", "500",encodeURI(url), "流程表单选择框");
	var buttons = [{
		text : '确定',
		id : 'processSubimt',
		//iconCls : 'icon-submit',
		handler : function() {
			var frmId = $('#lcbdxzk iframe').attr('id');
			var frm = document.getElementById(frmId).contentWindow;
			var resultDatas = frm.getSelectedResultDataJson();
			if(type=='global'){
				$("#globalForm").form('load',
					{formCode:resultDatas.formCode,formUrl:resultDatas.formUrl,formName:resultDatas.formName,proxyPage:'N',formId:resultDatas.id,pdId:resultDatas.processDefId
				});
			}
			if(type=='activity'){
				$("#activityForm").form('load',
					{formUrl:resultDatas.formUrl,proxyPage:'N',formId:resultDatas.id,pdId:resultDatas.processDefId
				});
			}
			if(type=='start'){
				$("#startForm").form('load',
					{formCode:resultDatas.formCode,formUrl:resultDatas.formUrl,formName:resultDatas.formName,proxyPage:'N',formId:resultDatas.id,pdId:resultDatas.processDefId
				});
			}
			usd.close();
		}
	}];
	usd.createButtonsInDialog(buttons);
	usd.show();
}

function backChecked(obj) {
	easyuiUnMask();
	globalProcessForm();
	if (obj != null && obj.flag=='success') {
		$.messager.show({
			title : '提示',
			msg : "操作成功！"
		});
	}else{
		$.messager.show({
			title : '提示',
			msg : "操作失败！"
		}); 
	}
}
function backUrl(obj) {
	easyuiUnMask();
	if (obj != null && obj.flag=='success') {
		$('#processForm').datagrid('reload');
		$.messager.show({
			title : '提示',
			msg : "导入成功！"
		});
	}else{
		$.messager.show({
			title : '提示',
			msg : "操作失败！"
		}); 
	}
}
//全局 开始表单初始化
function globalProcessForm(){
	var globalUrl = 'platform/bpm/bpmPublishAction/getProcessDeployForm.json?type=global&pdId=' + processDefId + "&random=" + Math.random()*100;
	var startUrl = 'platform/bpm/bpmPublishAction/getProcessDeployForm.json?type=start&pdId=' + processDefId + "&random=" + Math.random()*100;
	$('#globalForm').form('load',globalUrl);	
	$('#startForm').form('load',startUrl);
}
//节点表单权限处理
function activityProcessForm(actValue){
	if(actValue=='act'){
		actValue = $('#pdActivityName').combobox('getValue');
	}
	if(actValue!=null && actValue!=''){
		var tempUrl = 'platform/bpm/bpmPublishAction/getProcessDeployForm.json?type=activity&pdId=' + processDefId + '&pdActivityName=' + actValue;
		tempUrl += "&random=" + Math.random()*100;
		var uri = 'platform/bpm/bpmPublishAction/findComponentFromUrl.json?pdActivityName=' + actValue + '&pdId=' + processDefId;
		uri += "&random=" + Math.random()*100;
		var tab = $('#processNodes').tabs('getSelected');   
		var index = $('#processNodes').tabs('getTabIndex',tab);
		if(index==0){
			$('#activityForm').form('load',tempUrl);
		}
		if(index==1){
			$('#processForm').datagrid('options').url = uri;
			$('#processForm').datagrid('reload');
		}
	}
}
//节点表单权限配置
function deployProcessNodeAccess(myself,nodeId,processDefId,nodeName){
	if(oldTaskDiv && oldTaskDiv!=null && oldTaskDiv!=undefined ){
		oldTaskDiv.style.filter="Alpha(Opacity=0)";
	}
	myself.style.filter="Alpha(Opacity=100)";
	oldTaskDiv=myself;
	activityProcessForm(nodeId);
	globeTaskId = nodeId;
}
//初始化按钮信息
$(function(){   
	$('#btnSubmitGlobal').bind('click',
			function() {
				$('#globalForm').form('submit', {   
		   			url:'platform/bpm/bpmPublishAction/saveProcessDeployForm?type=global&pdId=' + processDefId,   
		    		onSubmit: function(){   
		    			return $("#globalForm").form('validate');
		   			},   
		    		success:function(data){ 
		    			globalProcessForm();
		    			$.messager.show({
		    				title : '提示',
		    				msg : "操作成功！"
		    			}); 
		    		}   
			});  
	});  
	$('#btnSubmitActivity').bind('click',
			function() {
				$('#activityForm').form('submit', {   
		   			url:'platform/bpm/bpmPublishAction/saveProcessDeployForm?type=activity&pdId=' + processDefId,   
		    		onSubmit: function(){   
		    			var actValue = $('#pdActivityName').combobox('getValue');
		    			if(actValue=='请选择...' || ''==actValue || actValue=='undefined' || actValue==null){
		    				$.messager.alert("操作提示", "请选择节点名称！","info"); 
		    				return false;
		    			}
		    			return $("#activityForm").form('validate');
		   			},   
		    		success:function(data){   
		    			activityProcessForm();
		    			$.messager.show({
		    				title : '提示',
		    				msg : "操作成功！"
		    			});
		    		}   
			});  
	});  
	$('#btnSubmitStart').bind('click',
			function() {
				$('#startForm').form('submit', {   
		   			url:'platform/bpm/bpmPublishAction/saveProcessDeployForm?type=start&pdId=' + processDefId,   
		    		onSubmit: function(){   
		    			return $("#startForm").form('validate');
		   			},   
		    		success:function(data){   
		    			globalProcessForm();
		    			$.messager.show({
		    				title : '提示',
		    				msg : "操作成功！"
		    			});
		    		}   
				});  
	});  
	$('#btnDelete').bind('click',
			function() {
				//var tempId = $("#globalForm").form()[0].id.value;不用id删除了，用pdid跟表单类型一起删
				var tempId = $("#globalForm").form()[0].pdId.value;
				if(tempId!=null && tempId != '' && tempId != 'undefined'){
					$.messager.confirm("操作提示", "您确认要删除吗?", function(data) {
						if (data) {
							easyuiMask();
							ajaxRequest(
									"POST",
									"type=global&pdId=" + tempId,
									"platform/bpm/bpmPublishAction/deleteProcessDeployForm",
									"json", "backChecked");
						}
					});
				}
	});  
	$('#btnDeleteAct').bind('click',
			function() {
				//var tempId = $("#activityForm").form()[0].id.value;
				var tempId = $("#activityForm").form()[0].pdId.value;
				if(tempId!=null && tempId != '' && tempId != 'undefined'){
					$.messager.confirm("操作提示", "您确认要删除吗?", function(data) {
						if (data) {
							easyuiMask();
							ajaxRequest(
									"POST",
									"type=activity&pdId=" + tempId,
									"platform/bpm/bpmPublishAction/deleteProcessDeployForm",
									"json", "backChecked");
						}
					});
				}
	});  
	$('#btnDeleteStart').bind('click',
			function() {
				//var tempId = $("#startForm").form()[0].id.value;
				var tempId = $("#startForm").form()[0].pdId.value;
				if(tempId!=null && tempId != '' && tempId != 'undefined'){
					$.messager.confirm("操作提示", "您确认要删除吗?", function(data) {
						if (data) {
							easyuiMask();
							ajaxRequest(
									"POST",
									"type=start&pdId=" + tempId,
									"platform/bpm/bpmPublishAction/deleteProcessDeployForm",
									"json", "backChecked");
						}
					});
				}
	});
	$('#btnRefresh').bind('click',
			function() {
		ajaxRequest("POST",null,"platform/bpm/bpmPublishAction/reLoadTaskAttributeCache","json","afterRefreshCache");
	});  
	$('#btnSelectForm').bind('click',
			function() {
				selectForm('global');
	});  
	$('#btnSelectStartForm').bind('click',
			function() {
				selectForm('start');
	});  
	$('#btnSelectFormAct').bind('click',
			function() {
				selectForm('activity');
	});  
	
	//当选中节点是触发
	$('#pdActivityName').combobox({  
		onSelect:function(){
			activityProcessForm('act');
		}
	});  
	
}); 

function refreshCache(){
	ajaxRequest("POST",null,"platform/bpm/bpmPublishAction/reLoadFormSecurityCache","json","afterRefreshCache");
}
function afterRefreshCache(json){
	if(json.success == true){
		 $.messager.show({title : '提示',msg : "刷新缓存成功!"});
	}else{
		 $.messager.show({title : '提示',msg : "刷新缓存失败!"});
	}
}
</script>
<body class="easyui-layout"  fit="true" onload='loadData()'>
	<div data-options="region:'west',title:'流程表单节点配置',split:true"
		style="width:580px; overflow: auto;">
			<div id="bpmImageDiv"></div>
	</div>
	<div data-options="region:'center',title:''" style="padding: 5px;">
		<div class="easyui-tabs" id="processNodes">
			<div title="表单配置" class="datagrid-toolbar" style="padding:5px; width:50%;height:auto">
				<div>
					<form id="globalForm" method="post" action="">
						<fieldset style="height:auto;" >
						<legend >全局表单配置</legend>
							<input type="hidden" name="pdId" id="pdId" />
							<input type="hidden" name="id" id="id" />
							<input type="hidden" name="formId" id="formId" />
							<table>
							<tr>
								<td  width='90px'>表单URL：</td>
								<td colspan=3><input class="easyui-validatebox"  type="text" name="formUrl" id="formUrl" required="true" readonly='true' style="width:460px;"/></td>
								<td align='left' width='75px'><a id="btnSelectForm" class="easyui-linkbutton">选择..</a> </td>
							</tr>
							<tr>
								<td width='90px'>是否代理：</td>
								<td>
								<select id="proxyPage" class="easyui-combobox" name="proxyPage" style="width:50px;" >  
    								<option value="N">否</option>  
    								<option value="Y">是</option>  
								</select>  
								</td>
								<td width='90px'>表单名称：</td>
								<td><input type="text" name="formName" id="formName" align=left readonly='true' ></td>
								<td></td>
							</tr>
							<tr>
								<td colspan=5 align='center'>	
									<a id="btnSubmitGlobal" class="easyui-linkbutton" >保存</a> 
									<a id="btnDelete" class="easyui-linkbutton" >删除</a>
									<a id="btnRefresh" class="easyui-linkbutton" >刷新缓存</a>
								</td>
							</tr>
							</table>
						</fieldset>
					</form>
				</div>
				<div>
					<form id="startForm" method="post" action="" style="height: auto; display: block;">
						<fieldset style="height:auto;">
							<legend>流程启动表单</legend>
							<input type="hidden" name="pdId" id="pdId" />
							<input type="hidden" name="id" id="id" />
							<input type="hidden" name="formId" id="formId" />
							<table>
								<tr>
									<td width='90px'>表单URL</td>
									<td ><input class="easyui-validatebox" type="text" name="formUrl" id="formUrl"  required="true" style="width:460px;"/></td><td align='left' width='75px'><a id="btnSelectStartForm" class="easyui-linkbutton">选择..</a></td>
								</tr>
								<tr>
									<td width='90px'>是否代理：</td>
									<td>
										<select id="proxyPage" class="easyui-combobox" name="proxyPage" style="width:50px;" >  
    										<option value="N">否</option>  
    										<option value="Y">是</option>  
										</select>  
									</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td colspan=3 align='center'>
										<a id="btnSubmitStart" class="easyui-linkbutton">保存</a> 
										<a id="btnDeleteStart" class="easyui-linkbutton">删除</a>
									</td>
								</tr>
							</table>	
						</fieldset>
					</form>
				</div>
				<div>
				<form id="activityForm" method="post" action="" style="height: auto; display: block;">
					<fieldset style="height:auto;">
						<legend>节点配置表单</legend>
							<input type="hidden" name="pdId" id="pdId" />
							<input type="hidden" name="id" id="id" />
							<input type="hidden" name="formId" id="formId" />
							<table>
								<tr>
									<td width='90px'>表单URL</td>
									<td colspan=3><input class="easyui-validatebox" type="text" name="formUrl" id="formUrl"  required="true" style="width:460px;"/></td>
									<td align='left' width='75px'><a id="btnSelectFormAct" class="easyui-linkbutton">选择..</a> </td>
								</tr>
								<tr>
									<td width='90px'>Url参数:</td>
									<td colspan=3><input type="text" name="remark" id="remark"  required="true" style="width:460px;"/></td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td width='90px'>是否代理：</td>
									<td>
										<select id="proxyPage" class="easyui-combobox" name="proxyPage" style="width:50px;" >  
    										<option value="N">否</option>  
    										<option value="Y">是</option>  
										</select>  
									</td>
									<td width='90px'>节点名称：</td>
									<td>
										<input class="easyui-combobox" id="pdActivityName" name="pdActivityName" value='请选择...' valueField="actId"  textField="alias" style="width:80%;"/>
									</td>
									<td></td>
								</tr>
								<tr>
									<td colspan=5 align='center'>
										<a id="btnSubmitActivity" class="easyui-linkbutton" >保存</a> 
										<a id="btnDeleteAct" class="easyui-linkbutton" >删除</a>
									</td>
								</tr>
							</table>
					</fieldset>
					</form>
				</div>
			</div>
			<div title="权限配置" style="padding: 10px; width: auto">
				<div> <table id="processForm"></table> </div>
			</div>
		</div>
	</div>
</body>
</html>