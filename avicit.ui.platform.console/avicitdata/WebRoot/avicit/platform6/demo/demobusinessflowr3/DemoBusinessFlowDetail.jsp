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
			url : 'platform/demoBusinessFlow/toDetailJsp.json',
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
			url : 'platform/demoBusinessFlow/operation/save',
			data : {
				data : dataVo
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
	
</script>
<body class="easyui-layout" fit="true">
<div region='center' border="false" style="overflow: hidden;">
	<!-- 流程按钮区域开始 -->
	<div class=datagrid-toolbar>
			<div id=bpmToolBar></div>
			<!-- 自定义按钮放到这里 -->
			<a class="easyui-linkbutton" iconCls="icon-undo" plain="true" onclick="doBack();" href="javascript:void(0);">返回</a>
	</div>
	<!-- 流程按钮区域结束 -->
<fieldset> 

<form id='formDetail'>
			<input type="hidden" name="id"/>
			
				<table width="100%" style="padding-top: 10px;">
					<tr>
						<th align="right"><label >出差人ID</label></th>
						<td>
							  <input title="出差人ID" class="inputbox" style="width: 180px;" type="text" name="userid" id="userid" disabled/>
						</td>
																	
						<th align="right"><label >出差人姓名</label></th>
						<td>
							  <input title="出差人姓名" class="inputbox" style="width: 180px;" type="text" name="username" id="username" disabled/>
						</td>
																	
					</tr>
					<tr>
						<th align="right"><label >出差地址</label></th>
						<td>
							  <input title="出差地址" class="inputbox" style="width: 180px;" type="text" name="address" id="address" disabled/>
						</td>
																	
						<th align="right"><label >出差事由</label></th>
						<td>
							  <input title="出差事由" class="inputbox" style="width: 180px;" type="text" name="matter" id="matter" disabled/>
						</td>
																	
					</tr>
					<tr>
						<th align="right"><label >出差借款</label></th>
						<td>
							  <input title="出差借款" class="inputbox" style="width: 180px;" type="text" name="lendmoney" id="lendmoney" disabled/>
						</td>
																	
						<th align="right"><label >交通工具</label></th>
						<td>
							  <input title="交通工具" class="inputbox" style="width: 180px;" type="text" name="traffic" id="traffic" disabled/>
						</td>
																	
					</tr>
					<tr>
						<th align="right"><label >出差日期</label></th>
						<td>
							  <input title="出差日期" class="easyui-datebox" style="width: 180px;" type="text" name="leavedate" id="leavedate" disabled/>
						</td>
																	
						<th align="right"><label >返回日期</label></th>
						<td>
							  <input title="返回日期" class="easyui-datebox" style="width: 180px;" type="text" name="backdate" id="backdate" disabled/>
						</td>
																	
					</tr>
					<tr>
						<th align="right"><label >出差总结</label></th>
						<td>
							  <input title="出差总结" class="inputbox" style="width: 180px;" type="text" name="summary" id="summary" disabled/>
						</td>
																	
						<th align="right"><label >出差补助</label></th>
						<td>
							  <input title="出差补助" class="inputbox" style="width: 180px;" type="text" name="allowance" id="allowance" disabled/>
						</td>
																	
					</tr>
					<tr>
					</tr>
				</table>
				 
				 <!-- 
				 <div class="formExtendBase">
					<div class="formUnit column2">
						<label >出差人ID</label>
						<div class="inputContainer">
							<input class="easyui-validatebox" title='出差人ID' type="text"
								name="userid" id="userid" data-options="" style="width: 90%;" readonly disabled></input>
						</div>
					</div>
					<div class="formUnit column2">
						<label >出差人姓名</label>
						<div class="inputContainer">
							<input class="easyui-validatebox" title='出差人姓名' type="text"
								name="username" id="username" data-options="" style="width: 90%;" readonly disabled></input>
						</div>
					</div>
					<div class="formUnit column2">
						<label >出差地址</label>
						<div class="inputContainer">
							<input class="easyui-validatebox" title='出差地址' type="text"
								name="address" id="address" data-options="" style="width: 90%;" readonly disabled></input>
						</div>
					</div>
					<div class="formUnit column2">
						<label >出差事由</label>
						<div class="inputContainer">
							<input class="easyui-validatebox" title='出差事由' type="text"
								name="matter" id="matter" data-options="" style="width: 90%;" readonly disabled></input>
						</div>
					</div>
					<div class="formUnit column2">
						<label>出差借款</label>
						<div class="inputContainer">
							<input class="easyui-validatebox" title='出差借款' type="text"
								name="lendmoney" id="lendmoney"  style="width: 90%;" readonly disabled></input>
						</div>
					</div>
					<div class="formUnit column2">
						<label>交通工具</label>
						<div class="inputContainer">
						
							<input class="easyui-validatebox" title='交通工具' type="text"
								name="traffic" id="traffic"  style="width: 90%;" readonly disabled></input>
						</div>
					</div>
					<div class="formUnit column2">
						<label>出差日期</label>
						<div class="inputContainer">
							<input class="easyui-datebox" title='离开日期'
								name="leavedate" id="leavedate" data-options="required:false" style="width: 200px;" disabled></input>
						</div>
					</div>
					<div class="formUnit column2">
						<label>返回日期</label>
						<div class="inputContainer">
							<input class="easyui-datebox" title='返回日期' 
								name="backdate" id="backdate" data-options="required:false" style="width: 200px;" disabled></input>
						</div>
					</div>
					<div class="formUnit column2">
						<label>出差总结</label>
						<div class="inputContainer">
							<input class="easyui-validatebox" title='出差总结' type="text"
								name="summary" id="summary"   style="width: 90%;" readonly disabled></input>
						</div>
					</div>
					<div class="formUnit column2">
						<label>出差补助</label>
						<div class="inputContainer">
							<input class="easyui-validatebox" title='出差补助' type="text"
								name="allowance" id="allowance"  style="width: 90%;" readonly disabled></input>
						</div>
					</div>
			</div>
			 -->
		</form>
	</fieldset>
	<div id="idea"></div>
</div>
</body>
</html>