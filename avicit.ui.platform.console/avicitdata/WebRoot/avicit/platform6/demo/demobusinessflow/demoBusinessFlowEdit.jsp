<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>编辑</title>
	<base href="<%=ViewUtil.getRequestPath(request) %>">
	<jsp:include page="/avicit/platform6/component/common/BpmJsInclude.jsp"></jsp:include>
</head>
<script type="text/javascript">
	/**
	 * 保存
	 */
	function saveForm(){
		var validateFlag = $('#formAdd').form('validate');
		if (validateFlag == false) {
			return;
		}
		$('#saveButton').linkbutton('disable');
		var dataVo = $('#formEdit').serializeArray();
		var dataJson = convertToJson(dataVo);
		dataVo = JSON.stringify(dataJson);
		$.ajax({
			url : 'platform/demobusinessflowController/update',
			data : {datas : dataVo},
			type : 'post',
			dataType : 'json',
			success : function(result) {
				if (result.flag == "success") {
					$.messager.show({
						title : '提示',
						msg : "操作成功。",
						timeout:2000,  
			            showType:'slide'  
					});
					if(parent != null){
						parent.reloadData();
					}
					closeForm();//关闭当前窗口
				}else {
					$.messager.show({
						title : '提示',
						msg : "操作失败。",
						timeout:2000,  
			            showType:'slide'  
					});
				}
			}
		});
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
<div region='center' border="false" >
<fieldset>
	<form id="formEdit" method="post" fit="true">
		<input name="id" type="hidden" value="${demoBusinessFlow.id}" />
		<input name="version" type="hidden" value="${demoBusinessFlow.version}" />
		<div class="formExtendBase">
			<sec:accesscontrollist hasPermission="3"
				domainObject="formdialog_demoBusinessFlowEdit_formEdit_userid">
				<div class="formUnit column2">
					<label class="labelbg">出差人ID</label>
					<div class="inputContainer">
						<input class="easyui-validatebox" title='出差人ID' type="text"
							name="userid"  id="userid" data-options="required:true"
							value="${demoBusinessFlow.userid}" style="width: 90%;"></input>
					</div>
				</div>
			</sec:accesscontrollist>
			<sec:accesscontrollist hasPermission="3"
				domainObject="formdialog_demoBusinessFlowEdit_formEdit_username">
				<div class="formUnit column2">
					<label class="labelbg">出差人姓名</label>
					<div class="inputContainer">
						<input class="easyui-validatebox" title='出差人姓名' type="text"
							name="username"  id="username" data-options="required:true"
							value="${demoBusinessFlow.username}" style="width: 90%;"></input>
					</div>
				</div>
			</sec:accesscontrollist>
			<sec:accesscontrollist hasPermission="3"
				domainObject="formdialog_demoBusinessFlowEdit_formEdit_address">
				<div class="formUnit column2">
					<label class="labelbg">出差地址</label>
					<div class="inputContainer">
						<input class="easyui-validatebox" title='出差地址' type="text"
							name="address" id="address" data-options="required:true"
							value="${demoBusinessFlow.address}" style="width: 90%;"></input>
					</div>
				</div>
			</sec:accesscontrollist>
			<sec:accesscontrollist hasPermission="3"
				domainObject="formdialog_demoBusinessFlowEdit_formEdit_matter">
				<div class="formUnit column2">
					<label class="labelbg">出差事由</label>
					<div class="inputContainer">
						<input class="easyui-validatebox" title='出差事由' type="text"
							name="matter" id="matter" data-options="required:true"
							value="${demoBusinessFlow.matter}" style="width: 90%;"></input>
					</div>
				</div>
			</sec:accesscontrollist>
			<sec:accesscontrollist hasPermission="3"
				domainObject="formdialog_demoBusinessFlowEdit_formEdit_lendmoney">
				<div class="formUnit column2">
					<label>出差借款</label>
					<div class="inputContainer">
						<input class="easyui-validatebox" title='出差借款' type="text"
							name="lendmoney"  id="lendmoney" 
							value="${demoBusinessFlow.lendmoney}" style="width: 90%;"></input>
					</div>
				</div>
			</sec:accesscontrollist>
			<sec:accesscontrollist hasPermission="3"
				domainObject="formdialog_demoBusinessFlowEdit_formEdit_traffic">
				<div class="formUnit column2">
					<label>交通工具</label>
					<div class="inputContainer">
						<input class="easyui-validatebox" title='交通工具' type="text"
							name="traffic" id="traffic" 
							value="${demoBusinessFlow.traffic}" style="width: 90%;"></input>
					</div>
				</div>
			</sec:accesscontrollist>
			<sec:accesscontrollist hasPermission="3"
				domainObject="formdialog_demoBusinessFlowEdit_formEdit_leavedate">
				<div class="formUnit column2">
					<label>离开日期</label>
					<div class="inputContainer">
						<input class="easyui-datetimebox" title='离开日期' type="text"
							name="leavedate" id="leavedate" data-options="required:true"
							value="${demoBusinessFlow.leavedate}" style="width: 200px;"></input>
					</div>
				</div>
			</sec:accesscontrollist>
			<sec:accesscontrollist hasPermission="3"
				domainObject="formdialog_demoBusinessFlowEdit_formEdit_backdate">
				<div class="formUnit column2">
					<label>返回日期</label>
					<div class="inputContainer">
						<input class="easyui-datetimebox" title='返回日期' type="text"
							name="backdate" id="backdate" data-options="required:true"
							value="${demoBusinessFlow.backdate}" style="width: 200px;"></input>
					</div>
				</div>
			</sec:accesscontrollist>
			<sec:accesscontrollist hasPermission="3"
				domainObject="formdialog_demoBusinessFlowEdit_formEdit_summary">
				<div class="formUnit column2">
					<label>出差总结</label>
					<div class="inputContainer">
						<input class="easyui-validatebox" title='出差总结' type="text"
							name="summary" id="summary"
							value="${demoBusinessFlow.summary}" style="width: 90%;"></input>
					</div>
				</div>
			</sec:accesscontrollist>
			<sec:accesscontrollist hasPermission="3"
				domainObject="formdialog_demoBusinessFlowEdit_formEdit_allowance">
				<div class="formUnit column2">
					<label>出差补助</label>
					<div class="inputContainer">
						<input class="easyui-validatebox" title='出差补助' type="text"
							name="allowance" id="allowance" 
							value="${demoBusinessFlow.allowance}" style="width: 90%;"></input>
					</div>
				</div>
			</sec:accesscontrollist>
		</div>
	</form>
</fieldset>
<!-- 附件 -->

</div>
<!-- 底部按钮 -->
<div id="toolbar" class="datagrid-toolbar datagrid-toolbar-extend" align="center" style="height:auto;">	
	<sec:accesscontrollist hasPermission="3" domainObject="formdialog_demoBusinessFlowEdit_button_saveForm" >
		<a title="保存" id="saveButton" class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="saveForm();" href="javascript:void(0);">保存</a>
	</sec:accesscontrollist>
	<sec:accesscontrollist hasPermission="3" domainObject="formdialog_demoBusinessFlowEdit_button_backForm" >
		<a title="返回" id="backButton" class="easyui-linkbutton" iconCls="icon-undo" plain="true" onclick="closeForm();" href="javascript:void(0);">返回</a>
	</sec:accesscontrollist>
</div>
</body>
</html>