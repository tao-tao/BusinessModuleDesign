<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>添加</title>
<base href="<%=ViewUtil.getRequestPath(request)%>">
<jsp:include page="/avicit/platform6/component/common/EasyUIJsInclude.jsp"></jsp:include>
<script src="static/js/platform/component/common/exteasyui.js" type="text/javascript"></script>
</head>
<body class="easyui-layout" fit="true">
	<div data-options="region:'center',split:true,border:false" style="overflow:hidden;padding-bottom:35px;">
		<form id='form'>
			<fieldset style="height: 120px;margin-top: 5px;">
				<legend>基本信息</legend>
				<table width="100%" style="padding-top: 10px;">
					<tr>
						<th align="right">配置文件代码:</th>
						<td><span style="padding:0px;margin: 0px;">
								<span style="padding:0px;margin: 0px;width: 5px;display: inline-block;">
									<span class="required-icon"></span>
								</span>
							  	<input title="配置文件代码" class="inputbox" style="width: 180px;" type="text" name="profileOptionCode" id="profileOptionCode"/>
							</span></td>
						<th align="right">应用程序名称:</th>
						<td><select name="sysApplicationId" class="easyui-combobox" readonly='readonly' data-options="width:173,editable:false,panelHeight:'auto'">
							<c:forEach items="${appsList}" var="appsList">
								<%-- <option value="${appsList.id}">${appsList.applicationName}</option> --%>
								<option value="${appsList.id}" <c:if test="${appsList.id eq appId}">SELECTED</c:if>>${appsList.applicationName}</option>
							</c:forEach>
							</select>
						</td>
					</tr>
					<tr>
						<th align="right">配置文件名称:</th>
						<td><span style="padding:0px;margin: 0px;">
								<span style="padding:0px;margin: 0px;width: 5px;display: inline-block;">
									<span class="required-icon"></span>
								</span>
							  	<input title="配置文件名称" class="inputbox" style="width: 180px;" type="text" name="profileOptionName" id="profileOptionName"/>
							</span></td>
						<th align="right">是否可分配:</th>
						<td><select name="ynMultiValue" class="easyui-combobox" data-options="width:173,editable:false,panelHeight:'auto'">
							<c:forEach items="${mulValue}" var="mulValue">
								<option value="${mulValue.lookupCode}">${mulValue.lookupName}</option>
							</c:forEach>
							</select>
						</td>
					</tr>
					<tr>
						<th align="right">有效标识:</th>
						<td><div style="padding-left: 10px;"><select name="validFlag" class="easyui-combobox" data-options="width:183,editable:false,panelHeight:'auto'">
							<c:forEach items="${validFlag}" var="validFlag">
								<option value="${validFlag.lookupCode}">${validFlag.lookupName}</option>
							</c:forEach>
							</select></div>
						</td>
						<th align="right">使用级别 :</th>
						<td><select name="usageModifier" class="easyui-combobox" data-options="width:173,editable:false,panelHeight:'auto'">
							<c:forEach items="${usageModifier}" var="usageModifier">
								<option value="${usageModifier.lookupCode}">${usageModifier.lookupName}</option>
							</c:forEach>
							</select>
						</td>
					</tr>
				</table>
			</fieldset>
			<fieldset style="height: 80px;margin-top: 5px;">
				<legend>层次结构类型的访问控制</legend>
				<table style="padding-top: 10px;">
					<tr>
						<th>地点</th>
						<td style="width: 100px;"><input type="checkbox" name="siteEnabledFlag" value="Y" defaultChecked="N"></td>
						<th>应用产品</th>
						<td style="width: 100px;"><input type="checkbox" name="appEnabledFlag" value="Y" defaultChecked="N"></td>
						<th>角色</th>
						<td style="width: 100px;"><input type="checkbox" name="roleEnabledFlag" value="Y" defaultChecked="N"></td>
					</tr>
					<tr>
						<th>用户</th>
						<td style="width: 100px;"><input type="checkbox" name="userEnabledFlag" value="Y" defaultChecked="N"></td>
						<th align="right">部门</th>
						<td style="width: 100px;"><input type="checkbox" name="deptEnabledFlag" value="Y" defaultChecked="N"></td>
					</tr>
				</table>
			</fieldset>
			<fieldset style="margin-top: 5px;border-bottom-width: 0px;border-left-width: 0px;border-right-width: 0px;">
				<legend>用于配置文件选项的值列表SQL验证</legend>
				<div style="padding-top: 5px;"><textarea class="inputbox scrollbar" style="height:50px;width:98%;" rows="4" id="sqlValidation" name="sqlValidation">${sysUser.remark}</textarea></div>
				
			</fieldset>
		</form>
		<div id="toolbar" class="datagrid-toolbar datagrid-toolbar-extend">
			<table class="tableForm" border="0" cellspacing="1" width='100%'>
				<tr>	
					<td align="right" width="60%">
						<a title="保存" id="saveButton"  class="easyui-linkbutton" plain="false" onclick="saveForm();" href="javascript:void(0);">保存</a>
						<a title="返回" id="returnButton"  class="easyui-linkbutton"  plain="false" onclick="closeForm();" href="javascript:void(0);">返回</a>
					</td>
					
				</tr>
			</table>
		</div>
	</div>
	<script type="text/javascript">
	function closeForm(){
		parent.sysPro.closeDialog("#insert");
	}
	function saveForm(){
		var reg =/\s/;
		var profileOptionCode =$('#profileOptionCode').val();
		if(profileOptionCode.length ===  0||reg.test(profileOptionCode)){
			$.messager.alert('提示','配置文件代码不能为空，或含有空格字符！','warning');
			return;
		}
		if(profileOptionCode.length >100){
			$.messager.alert('提示',"配置文件代码不能太长！",'warning');
			return;
		}
		var profileOptionName =$('#profileOptionName').val();
		if(profileOptionName.length ===  0||reg.test(profileOptionName)){
			$.messager.alert('提示','配置文件名称不能为空，或含有空格字符！','warning');
			return;
		}
		if(profileOptionName.length >100){
			$.messager.alert('提示','配置文件名称不能太长！','warning');
			return;
		}
		parent.sysPro.save(serializeObject($('#form')),'${url}',"#insert");
	}
	</script>
</body>
</html>