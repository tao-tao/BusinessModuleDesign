<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="avicit.platform6.commons.utils.ViewUtil"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@page import="avicit.platform6.core.api.system.syslocale.PlatformLocalesJSTL"%>
<%
	String host = request.getContextPath();
	String needCheckLock = (String) session.getAttribute("needCheckLock");
	String loginName = (String) session.getAttribute("loginName");
	boolean needCheckLockFlag = false;
	if(needCheckLock != null && needCheckLock == "1"){
		needCheckLockFlag = true;
	}
/* 判断是否存在登录失败的情况 */
String exception_msg_ = (String) session
		.getAttribute("exception_msg_");
/* 将英文的Bad credentials转换为中文 */
if (null != exception_msg_
		&& "Bad credentials".equals(exception_msg_)) {
	exception_msg_ = PlatformLocalesJSTL.getBundleValue("login.error.tip");
}
if(null != exception_msg_ && "User is disabled".equals(exception_msg_)){
	exception_msg_ = PlatformLocalesJSTL.getBundleValue("login.error.userlock.tip");
}
%>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="chrome=1">
<title>企业项目管理系统</title>
<meta content="text/html; charset=utf-8" http-equiv="Content-Type">
<meta name="description" content="">
<meta name="keywords" content="登录">
<link rel="stylesheet" id="skins" type="text/css" href="../avicit/platform6/modules/system/css/easyui.css">
<link href="style/pmo.login.style.css" type="text/css" rel="stylesheet">
<link rel="icon" href="img/favicon.png" type="image/x-icon">
<link rel="shortcut icon" href="img/favicon.png" type="image/x-icon">
<script src="../avicit/platform6/component/js/jQuery/jQuery-1.8.2/jquery-1.8.2.js" type="text/javascript"></script>
<script src="../avicit/platform6/component/js/jQuery/jquery-easyui-1.3.5/jquery.easyui.min.js" type="text/javascript"></script>
<script type="text/javascript" src="img/base.js"></script>
<script type="text/javascript">
var path = "<%=ViewUtil.getRequestPath(request)%>";
  $(function(){
	  var needCheckLock = "<%=needCheckLock%>";
	  var  needCheckLockFlag = false;
	  if(needCheckLock != null && needCheckLock == "1"){
	  	needCheckLockFlag = true;
	  }
		if(needCheckLockFlag){
			var loginName = "<%=loginName%>";
			$('#modify_dialog_login').show();
			$('#modify_dialog_login').dialog({
				 content : "<iframe name='applyFrame' id='applyFrameModifyLogin' scrolling='yes' frameborder='0'  style='width:100%;height:91%;'></iframe>"
			});
			document.getElementById('applyFrameModifyLogin').src = path + "avicit/platform6/modules/system/syspassword/view/ChangePasswordLogin.d7?loginName=" + loginName + "&j=" + Math.random(); 
			$('#modify_dialog_login').dialog('open');
	}
});
</script>
</head>
<body>
  <div class="container" >
	  <div class="loginForm" >
				<form action="<%=host%>/security_check_" method="post" style="height: 20px;" id="formSubmit">
					<table style = 'margin-top:-10px;'>
						<tr>
							<td>
								<input name="username_" class="ml-70" tabindex="1" type="text" size="20" id="uname" focucmsg="用户名" onkeydown="pressKeyDown(event);">
							</td>
							<td>
								<input name="Password1" class="ml-30" id="showPwd" size="20" tabindex="2" focucmsg="密码" type="text" onkeydown="pressKeyDown(event);"> 
								<input name="password_" class="ml-30 displaynone" id="password" size="20" tabindex="2" type="password" onkeydown="pressKeyDown(event);">
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<div class="forgetPwdLine">
								<%
								if (exception_msg_ != null && exception_msg_ != "") {
									out.println("<font color=\"red\">"+ PlatformLocalesJSTL.getBundleValue("login.error.info")+":" + exception_msg_
											+ "</font>");
								}
								%>
								</div>
								<div id="modify_dialog_login"  title="<%=PlatformLocalesJSTL.getBundleValue("portal.modify.password")%>" style="background:#fff;padding:5px;width:500px;height:400px;display:none" ></div>
							</td>
						</tr>
						
						<tr>
							<td>
							</td>
							
							<td>
								<input name="ButtonLogin" class="btn" type="button" onclick="submit()" value="" style="margin-left: 120px;margin-top:15px;">
							</td>
						</tr>
					</table>
				</form>
	</div>
</div>
	<script type="text/javascript">
		////////////////////////////////////////////////////////////////////////////////////////////
		//设置垂直居中
		function fBodyVericalAlign(){
			var nBodyHeight = 572;
			var nClientHeight = document.documentElement.clientHeight;
			if(nClientHeight >= nBodyHeight + 2){
				var nDis = (nClientHeight - nBodyHeight)/2;
				document.body.style.paddingTop = nDis + 'px';
			}else{
				document.body.style.paddingTop = '0px';
			}
		}
		fBodyVericalAlign();

		//onresize事件
		fEventListen(window,'resize',fResize);
		fEventListen(window,'resize',fBodyVericalAlign);
	 	
		$(document).ready(function() {
			$("#uname").each(function() {
				$(this).val($(this).attr("focucmsg"));
				$(this).val($(this).attr("focucmsg")).css("color", "#979393");
				$(this).focus(function() {
					if ($(this).val() == $(this).attr("focucmsg")) {
						$(this).val('');
						$(this).val('').css("color", "#000");
					}
				});
				$(this).blur(function() {
					if (!$(this).val()) {
						$(this).val($(this).attr("focucmsg"));
						$(this).val($(this).attr("focucmsg")).css("color", "#979393");
					}

				});
			});

			$("#showPwd").each(function() {
				$("#password").hide();
				$(this).val($(this).attr("focucmsg"));
				$(this).val($(this).attr("focucmsg")).css("color", "#979393");
				$(this).focus(function() {
					if ($(this).val() == $(this).attr("focucmsg")) {

						$("#showPwd").hide();
						$("#password").show().focus();

					}
				});
				$("#password").blur(function() {
					if ($(this).val() == '') {

						$("#showPwd").show();
						$("#password").hide();

					}
				});
			});
		});
		
		function submit()
		{
			confirm();
		}
		
		function confirm(){
			document.getElementById("formSubmit").submit();
		}
		
		function pressKeyDown(event){
			if(event.keyCode==13){
				confirm();
				return false;
			}
			return true;
		}
	</script>
</body>
</html>
