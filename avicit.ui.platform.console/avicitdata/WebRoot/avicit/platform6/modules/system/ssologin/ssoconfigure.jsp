<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<%@ page import="avicit.platform6.api.syssso.loder.SsoPropsLoader"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>sso配置</title>
<base href="<%=ViewUtil.getRequestPath(request) %>">
<jsp:include page="/avicit/platform6/component/common/EasyUIJsInclude.jsp"></jsp:include>
<style type="text/css">
.requiedbox {
	display: inline-block;
	white-space: nowrap;
	margin: 0;
	padding: 0;
	/* border-width: 1px;
	border-style: solid; */
	overflow: hidden;
}
.selectbox {
	display: inline-block;
	white-space: nowrap;
	margin: 0;
	padding: 0;
	border-width: 1px;
	border-style: solid;
	overflow: hidden;
}
.selectbox .selectbox-text {
	font-size: 12px;
	border: 0;
	margin: 0;
	padding: 0;
	line-height: 20px;
	height: 20px;
	 *margin-top: -1px;
	 *height: 18px;
	 *line-height: 18px;
	_height: 18px;
	_line-height: 18px;
	vertical-align: baseline;
}
.selectbox-button {
	width: 18px;
	height: 20px;
	overflow: hidden;
	display: inline-block;
	vertical-align: top;
	cursor: pointer;
	/* opacity: 0.6;
	filter: alpha(opacity=60); */
}
.selectbox-button-hover {
	/* opacity: 1.0;
	filter: alpha(opacity=100); */
}
.selectbox {
	border-color: #95B8E7;
	background-color: #fff;
}
.selectbox-button {
	background: url('static/css/platform/themes/default/commonselection/icons_ext/selectorCommon.gif') no-repeat center center;
}
.required-icon {
	maring:0px;
	padding:0px;
	width: 10px;
	height: 23px;
	overflow: hidden;
	display: inline-block;
	vertical-align: top;
	/* opacity: 0.6; */
/* 	filter: alpha(opacity=60); */
	background: url('static/css/platform/themes/default/public/images/required.gif') no-repeat center center;
}
.datagrid-toolbar-extend {
	height: auto;
	padding: 1px 0;
	border-width: 0 0 1px 0;
	border-style: solid;
	position: absolute;
	bottom: 0px;
	left: 0px;
	width: 100%;
}
.numberBox_extend{
	border:1px solid #0066FF;height:18px;
}
.inputbox{
	background-color: #fff;
	border: 1px solid #95b8e7;
	color: #000;
	height: 18px;
}
.easyui-numberbox{
	border: 1px solid #95b8e7;
}
.fieldset{
	font-size: 12px;
	font-family: "Microsoft YaHei";
	color: #444;
	line-height:20px;
	width: 95%;
}
</style>
</head>
<body  class="easyui-layout" fit="true" onload="init();">
	 <div class="easyui-panel" title=""  style="height:500px;width:1440px;padding:10px;"/>
	 <div id="" style="margin-top: -10px;margin-left: -10px; width: 100%;"class="datagrid-toolbar">
					<table width="">
							<td><a title="保存" id="saveButton"  class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="saveForm();" href="javascript:void(0);">保存</a></td>
							<td><a title="取消" id="returnButton"  class="easyui-linkbutton" iconCls="icon-undo" plain="true" onclick="resetForm();" href="javascript:void(0);">取消</a></td>
					</table>	
		</div>
	<div data-options="region:'center',split:true,border:false" style="overflow:hidden;padding-bottom:35px;">
				<form id="formssologin" method="post">
					<table width="">
						<tr>
							<td width="" align="">
								<label>SSO登录URL</label>
							</td>
							<td align="left" width="">
								<span class="requiedbox" style="width: 468px;">
									<span>
										<span class="required-icon"></span>
									</span>
								  	<input style="width: 408px" title="sso登录url" class="easyui-validatebox" type="text" name="ssoLoginUrl" id="ssoLoginUrl" value="${ssoLoginUrl}"data-options="required:true"/>
								</span>
							</td>
						</tr>
						<tr height="15px"></tr>
						<tr>
							<td width="" align="">
								<label>SSO退出URL</label>
							</td>
							<td align="left" width="170">
								<span class="requiedbox" style="width: 468px; height: 23px;">
										<span>
											<span class="required-icon"></span>
										</span>
								  	<input style="width: 408px" title="SSO退出URL" class="easyui-validatebox" type="text" name="ssoLogoutUrl" id="ssoLogoutUrl" value="${ssoLogoutUrl}" data-options="required:true" />
								</span>
							</td>
						</tr>
						<tr height="15px"></tr>
						<tr>
							<td width="" align="">
								<label>当前应用服务器名称</label>
							</td>
							<td align="" width="">
								<span class="requiedbox" style="width: 468px; height: 23px;">
									<span>
										<span class="required-icon"></span>
									</span>
								  	<input style="width: 408px" title="当前应用服务器名称" class="easyui-validatebox" type="text" name="serverName" id="serverName" value="${serverName}" data-options="required:true"/>
								</span>
							</td>
						</tr>
						<tr height="15px"></tr>
						<tr>
							<td width="" align="">
								<label>当前应用登录URL</label>
							</td>
							<td align="" width="">
							<span class="requiedbox" style="width: 468px; height: 23px;">
										<span>
											<span class="required-icon"></span>
										</span>
								  	<input style="width: 408px" title="当前应用登录URL" class="easyui-validatebox" type="text" name="serviceUrl" id="serviceUrl" value="${serviceUrl}" data-options="required:true"/>
								  	</span>
							</td>
						</tr>
						<tr height="15px"></tr>
						<tr>
							<td width="" align="">
								<label>SSO服务器</label>
							</td>
							<td align="" width="">
							<span class="requiedbox" style="width: 468px; height: 23px;">
										<span>
											<span class="required-icon"></span>
										</span>
								  	<input style="width: 408px" title="ssoServerUrl" class="easyui-validatebox" type="text" name="ssoServerUrl" id="ssoServerUrl" value="${ssoServerUrl}" data-options="required:true"/>
								  	</span>
							</td>
						</tr>
						<tr height="15px"></tr>
						<tr>
						<td width="" align="">
								<label>SSO登录启用</label>
							</td>
							<td width="" align="" width="">
							<span class="requiedbox" style="width: 268px; height: 23px;">
										<span>
											<span class="required-icon"></span>
										</span>
										<input width="90%" title="ssoEnabled" class="easyui-numberbox" type="checkbox" name="ssoEnabled" id="ssoEnabled"/>
								  	</span>
								
							</td>
						</tr >
						<tr>
						
					</tr>
					</table>
				</form>
			
	</div>
		
	</div>
<script type="text/javascript">
/**
 * 
 */
function saveForm(){
	
	if ($('#formssologin').form('validate') == false) {
		return;
	}
	var dataVo = $('#formssologin').serializeArray();
	var dataJson = convertToJson(dataVo);
	dataVo = JSON.stringify(dataJson);
	$.ajax({
		url : 'platform/ssoUpdate/saveorupdate',
		data : {
			datas : JSON.stringify(serializeObject($('#formssologin')))
		},
		type : 'post',
		dataType : 'json',
		success : function(result) {
			if (result.flag == "success") {
				showMessager("配置成功");
			}else {
				
				showMessager("配置失败");
			}
		}
	});
}
function resetForm(){
	$('#formssologin').form('clear');
	
}
/**
 * 
 */
function showMessager(msg){
	var scroll =-document.body.scrollTop-document.documentElement.scrollTop;
	$.messager.show({
		title : '提示',
		msg : msg,
		timeout:2000,  
        showType:'slide',
        style:{
        	left:'',
        	right:0,
        	top:'',
            bottom:scroll
        }  
	});
}
function init(){
	var falg = ${ssoEnabled};
	if(falg){
		document.getElementById("ssoEnabled").checked = true;
	}
}
</script>
</body>
</html>