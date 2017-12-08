<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
	String entryId = request.getParameter("entryId");
	String executionId = request.getParameter("executionId");
	String taskId = request.getParameter("taskId");
	String formId = request.getParameter("id");
	String skinColor = "gray";
%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>详细</title>
	<base href="<%=ViewUtil.getRequestPath(request) %>">
	<jsp:include page="/avicit/platform6/component/common/BpmJsInclude.jsp"></jsp:include>
</head>
<script type="text/javascript">
	var baseurl = '<%=request.getContextPath()%>';
	var entryId = "<%=entryId%>";
	var executionId = "<%=executionId%>";
	var taskId = "<%=taskId%>";
	var formId = "<%=formId%>"; 
	
	//初始化页面值
	function initFormData(){
		$.ajax({
			url : 'platform/demobusinessflowController/toDetailJsp.json',
			data : {
				id : formId
			},
			type : 'post',
			dataType : 'json',
			success : function(result) {
				if (result.flag == "success") {
					//进行时间转化
					result.demoBusinessFlow.leavedate = formatDatebox(result.demoBusinessFlow.leavedate);
					result.demoBusinessFlow.backdate = formatDatebox(result.demoBusinessFlow.backdate);
					$("#formDetail").form('load',result.demoBusinessFlow);
				} else {
					$.messager.show({
						title : '提示',
						msg : "数据加载失败。"
					});
				}
			}
		});
	}

	/**
	 * 保存表单方法
	 * @param processInstanceId
	 * @param executionId
	 */
	window.saveFormData = function(processInstanceId,executionId){
		var dataVo = $('#formDetail').serializeArray();
		var dataJson = convertToJson(dataVo);
		dataVo = JSON.stringify(dataJson);
		$.ajax({
			url : 'platform/demobusinessflowController/update',
			data : {
				datas : dataVo
			},
			type : 'post',
			dataType : 'json',
			success : function(result) {
				if (result.flag == "success") {
					initFormData();
					$.messager.show({
						title : '提示',
						msg : "操作成功。"
					});
				} else {
					$.messager.show({
						title : '提示',
						msg : "操作失败。"
					});
				}
			}
		});
	};
	//返回
	function doBack(){
		if(parent!=null&&parent.$('#tabs')!=null){
			var currTab = parent.$('#tabs').tabs('getSelected');
			var currTitle = currTab.panel('options').title; 
			parent.$('#tabs').tabs('close',currTitle);
		}
	}
	//页面加载完成后入口
	$(function(){
		initFormData();
		//不控制表单权限
 		//initBpmInfo(entryId,executionId,taskId,formId);
		//控制表单权限用这个
		initBpmInfoAndFormAccess(entryId,executionId,taskId,formId);
	});
	
	//表单权限后处理函数
	function afterInitBpmInfoAndFormAccess() {
		//alert("执行表单权限后处理函数");
	};
</script>
<body class="easyui-layout" fit="true">
<div  region='center' border="false" style="overflow: hidden;">
<!-- 流程按钮区域开始 -->
<DIV class=toolbar-left-wrap><DIV class=toolbar-left>
<DIV id=bpmToolBar></DIV>
<!-- 自定义按钮放到这里 -->
<DIV id="buttonBack_c"  onclick="doBack()"  title="返回" class="bbtnContainer" onmouseup="onButtonUp(this);"  onmouseover="onButtonOver(this);"  onmouseout="onButtonOut(this);" onmousedown="onButtonDown(this);">
<DIV id="buttonBack_l" class="bbtn-left"></DIV>
<DIV id="buttonBack_t" class="bbtn"><IMG style="border:0px;vertical-align:text-top;" src="static/js/platform/component/jQuery/jquery-easyui-1.3.5/themes/<%=skinColor%>/icons/undo.png">&nbsp;返回</DIV>
<DIV id="buttonBack_r" class="bbtn-right"></DIV></DIV>
</DIV></DIV>
<!-- 流程按钮区域结束 -->
<fieldset> 
   <form id="formDetail" method="post">
    <div class="formExtendBase">
				<sec:accesscontrollist hasPermission="3"
					domainObject="formdialog_demoBusinessFlowDetail_formAdd_userid">
					<div class="formUnit column2">
						<label class="labelbg">出差人ID</label>
						<div class="inputContainer">
							<input class="easyui-validatebox" title='出差人ID' type="text"
								name="userid" id="userid" data-options="required:true" style="width: 90%;"></input>
						</div>
					</div>
				</sec:accesscontrollist>
				<sec:accesscontrollist hasPermission="3"
					domainObject="formdialog_demoBusinessFlowDetail_formAdd_username">
					<div class="formUnit column2">
						<label class="labelbg">出差人姓名</label>
						<div class="inputContainer">
							<input class="easyui-validatebox" title='出差人姓名' type="text"
								name="username" id="username" data-options="required:true" style="width: 90%;"></input>
						</div>
					</div>
				</sec:accesscontrollist>
				<sec:accesscontrollist hasPermission="3"
					domainObject="formdialog_demoBusinessFlowDetail_formAdd_address">
					<div class="formUnit column2">
						<label class="labelbg">出差地址</label>
						<div class="inputContainer">
							<input class="easyui-validatebox" title='出差地址' type="text"
								name="address" id="address" data-options="required:true" style="width: 90%;"></input>
						</div>
					</div>
				</sec:accesscontrollist>
				<sec:accesscontrollist hasPermission="3"
					domainObject="formdialog_demoBusinessFlowDetail_formAdd_matter">
					<div class="formUnit column2">
						<label class="labelbg">出差事由</label>
						<div class="inputContainer">
							<input class="easyui-validatebox" title='出差事由' type="text"
								name="matter" id="matter" data-options="required:true" style="width: 90%;"></input>
						</div>
					</div>
				</sec:accesscontrollist>
				<sec:accesscontrollist hasPermission="3"
					domainObject="formdialog_demoBusinessFlowDetail_formAdd_lendmoney">
					<div class="formUnit column2">
						<label>出差借款</label>
						<div class="inputContainer">
							<input class="easyui-validatebox" title='出差借款' type="text"
								name="lendmoney" id="lendmoney"  style="width: 90%;"></input>
						</div>
					</div>
				</sec:accesscontrollist>
				<sec:accesscontrollist hasPermission="3"
					domainObject="formdialog_demoBusinessFlowDetail_formAdd_traffic">
					<div class="formUnit column2">
						<label>交通工具</label>
						<div class="inputContainer">
						
							<input class="easyui-validatebox" title='交通工具' type="text"
								name="traffic" id="traffic"  style="width: 90%;"></input>
						</div>
					</div>
				</sec:accesscontrollist>
				<sec:accesscontrollist hasPermission="3"
					domainObject="formdialog_demoBusinessFlowDetail_formAdd_leavedate">
					<div class="formUnit column2">
						<label>离开日期</label>
						<div class="inputContainer">
							<input class="easyui-datebox" title='离开日期'
								name="leavedate" id="leavedate" data-options="required:false" style="width: 200px;"></input>
						</div>
					</div>
				</sec:accesscontrollist>
				<sec:accesscontrollist hasPermission="3"
					domainObject="formdialog_demoBusinessFlowDetail_formAdd_backdate">
					<div class="formUnit column2">
						<label>返回日期</label>
						<div class="inputContainer">
							<input class="easyui-datebox" title='返回日期' 
								name="backdate" id="backdate" data-options="required:false" style="width: 200px;"></input>
						</div>
					</div>
				</sec:accesscontrollist>
				<sec:accesscontrollist hasPermission="3"
					domainObject="formdialog_demoBusinessFlowDetail_formAdd_summary">
					<div class="formUnit column2">
						<label>出差总结</label>
						<div class="inputContainer">
							<input class="easyui-validatebox" title='出差总结' type="text"
								name="summary" id="summary"   style="width: 90%;"></input>
						</div>
					</div>
				</sec:accesscontrollist>
				<sec:accesscontrollist hasPermission="3"
					domainObject="formdialog_demoBusinessFlowDetail_formAdd_allowance">
					<div class="formUnit column2">
						<label>出差补助</label>
						<div class="inputContainer">
							<input class="easyui-validatebox" title='出差补助' type="text"
								name="allowance" id="allowance"  style="width: 90%;"></input>
						</div>
					</div>
				</sec:accesscontrollist>
		</div>
	</form>
	</fieldset>
	<div id="idea"></div>
</div>
</body>
</html>