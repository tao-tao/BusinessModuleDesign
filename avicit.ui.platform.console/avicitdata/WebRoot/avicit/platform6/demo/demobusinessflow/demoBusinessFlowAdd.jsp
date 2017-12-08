<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>添加</title>
	<base href="<%=ViewUtil.getRequestPath(request) %>">
	<jsp:include page="/avicit/platform6/component/common/BpmJsInclude.jsp"></jsp:include>
</head>
<script type="text/javascript">
	var formCode = "TestBpmBusinessTrip"; //表单的code，启动流程时需要
	
	/**
	 * 保存并启动流程
	 */
	function saveFormAndStartFlow(){
		var validateFlag = $('#formAdd').form('validate');
		if (validateFlag == false) {
			return;
		}
		$('#saveButton').linkbutton('disable');
		var dataVo = $('#formAdd').serializeArray();
		var dataJson = convertToJson(dataVo);
		dataVo = JSON.stringify(dataJson);
		//打开流程选择对话框
		var processDef = new StartProcessByFormCode();
		processDef.SetFormCode(formCode);
		StartProcessByFormCode.prototype.doStart = function(pdId) {
			/* 将表单代码、业务数据、流程定义ID提交到Java端 */
			$.ajax({
				url : 'platform/demobusinessflowController/saveAndStartProcess',
				data :{
					processDefId:pdId,
					formCode:formCode,
					datas:dataVo
				},
				type : 'post',
				dataType : 'json',
				success : function(result) {
					if (result.flag == "success") {
						$.messager.show({title : '提示',msg : "操作成功。"});
						if(parent != null){
							parent.reloadData();
						}
						var processInstanceId = result.bp.processInstanceId; //流程实例ID
						var taskUrl = result.bp.taskUrl; //待办URL
						//打开待办审批页面
						//parent.window.location.replace(getPath()+"/"+taskUrl);
						closeForm();//关闭当前窗口
					}else {
						$.messager.show({title : '提示', msg : "操作失败。"});
					}
				}
			});
		};
		processDef.start();
	}
	
	/**
	 * 关闭本页面
	 */
	function closeForm(){
		if(parent != null && typeof(parent.closeDialog) != "undefined"){
			parent.closeDialog();
		}
	}
</script>
<body class="easyui-layout" fit="true">
<div region="center" border="false">
<fieldset>
	<form id="formAdd" method="post">
		<div class="formExtendBase">
			<sec:accesscontrollist hasPermission="3"
				domainObject="formdialog_demoBusinessFlowAdd_formAdd_userid">
				<div class="formUnit column2">
					<label class="labelbg">出差人ID</label>
					<div class="inputContainer">
						<input class="easyui-validatebox" title='出差人ID' type="text" style="width:90%;"
							name="userid" id="userid" data-options="required:true" ></input>
					</div>
				</div>
			</sec:accesscontrollist>
			<sec:accesscontrollist hasPermission="3"
				domainObject="formdialog_demoBusinessFlowAdd_formAdd_username">
				<div class="formUnit column2">
					<label class="labelbg">出差人姓名</label>
					<div class="inputContainer">
						<input class="easyui-validatebox" title='出差人姓名' type="text" style="width:90%;"
							name="username" id="username" data-options="required:true"></input>
					</div>
				</div>
			</sec:accesscontrollist>
			<sec:accesscontrollist hasPermission="3"
				domainObject="formdialog_demoBusinessFlowAdd_formAdd_address">
				<div class="formUnit column2">
					<label class="labelbg">出差地址</label>
					<div class="inputContainer">
						<input class="easyui-validatebox" title='出差地址' type="text" style="width:90%;"
							name="address" id="address" data-options="required:true"></input>
					</div>
				</div>
			</sec:accesscontrollist>
			<sec:accesscontrollist hasPermission="3"
				domainObject="formdialog_demoBusinessFlowAdd_formAdd_matter">
				<div class="formUnit column2">
					<label class="labelbg">出差事由</label>
					<div class="inputContainer">
						<input class="easyui-validatebox" title='出差事由' type="text" style="width:90%;"
							name="matter" id="matter" data-options="required:true"></input>
					</div>
				</div>
			</sec:accesscontrollist>
			<sec:accesscontrollist hasPermission="3"
				domainObject="formdialog_demoBusinessFlowAdd_formAdd_lendmoney">
				<div class="formUnit column2">
					<label>出差借款</label>
					<div class="inputContainer">
						<input class="easyui-validatebox" title='出差借款' type="text" style="width:90%;"
							name="lendmoney" id="lendmoney"></input>
					</div>
				</div>
			</sec:accesscontrollist>
			<sec:accesscontrollist hasPermission="3"
				domainObject="formdialog_demoBusinessFlowAdd_formAdd_traffic">
				<div class="formUnit column2">
					<label>交通工具</label>
					<div class="inputContainer">
						<input class="easyui-validatebox" title='交通工具' type="text" style="width:90%;"
							name="traffic" id="traffic"></input>
					</div>
				</div>
			</sec:accesscontrollist>
			<sec:accesscontrollist hasPermission="3"
				domainObject="formdialog_demoBusinessFlowAdd_formAdd_leavedate">
				<div class="formUnit column2">
					<label>离开日期</label>
					<div class="inputContainer">
						<input class="easyui-datetimebox" title='离开日期' type="text" 
							name="leavedate" id="leavedate" data-options="required:false"></input>
					</div>
				</div>
			</sec:accesscontrollist>
			<sec:accesscontrollist hasPermission="3"
				domainObject="formdialog_demoBusinessFlowAdd_formAdd_backdate">
				<div class="formUnit column2">
					<label>返回日期</label>
					<div class="inputContainer">
						<input class="easyui-datetimebox" title='返回日期' type="text" 
							name="backdate" id="backdate" data-options="required:false"></input>
					</div>
				</div>
			</sec:accesscontrollist>
			<sec:accesscontrollist hasPermission="3"
				domainObject="formdialog_demoBusinessFlowAdd_formAdd_summary">
				<div class="formUnit column2">
					<label>出差总结</label>
					<div class="inputContainer">
						<input class="easyui-validatebox" title='出差总结' type="text" style="width:90%;" 
							name="summary" id="summary"></input>
					</div>
				</div>
			</sec:accesscontrollist>
			<sec:accesscontrollist hasPermission="3"
				domainObject="formdialog_demoBusinessFlowAdd_formAdd_allowance">
				<div class="formUnit column2">
					<label>出差补助</label>
					<div class="inputContainer">
						<input class="easyui-validatebox" title='出差补助' type="text" style="width:90%;"
							name="allowance" id="allowance" ></input>
					</div>
				</div>
			</sec:accesscontrollist>
		</div>
	</form>
</fieldset>
</div>
<!-- 底部按钮 -->
<div id="toolbar" class="datagrid-toolbar datagrid-toolbar-extend" align="center" style="height:auto;">
	<sec:accesscontrollist hasPermission="3" domainObject="formdialog_demoBusinessFlowAdd_button_saveForm" >
		<a title="保存" id="saveButton" class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="saveFormAndStartFlow();" href="javascript:void(0);">保存并启动流程</a>
	</sec:accesscontrollist>
	<sec:accesscontrollist hasPermission="3" domainObject="formdialog_demoBusinessFlowAdd_button_backForm" >
		<a title="返回" id="backButton" class="easyui-linkbutton" iconCls="icon-undo" plain="true" onclick="closeForm();" href="javascript:void(0);">返回</a>
	</sec:accesscontrollist>
</div>
</body>
</html>