<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@page import="avicit.platform6.core.spring.SpringFactory"%>
<html>
<head>

<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="avicit.platform6.commons.utils.ViewUtil"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@page import="avicit.platform6.core.locale.PlatformLocalesJSTL"%>
<%
/* Object o = SpringFactory.getBean("dddd"); */
	String host = request.getContextPath();
	String needCheckLock = (String) session.getAttribute("needCheckLock");
	String unvalidatUser = (String) session.getAttribute("unValidate");//无效用户，add by zl
	String loginName = (String) session.getAttribute("loginName");
	boolean needCheckLockFlag = false;
	if(needCheckLock != null && needCheckLock == "1"){
		needCheckLockFlag = true;
	}
	/* 判断是否存在登录失败的情况 */
	String exception_msg_ = (String) session.getAttribute("exception_msg_");
	if (null != exception_msg_&& "Bad credentials".equals(exception_msg_)) {
		exception_msg_ = PlatformLocalesJSTL.getBundleValue("login.error.tip");
	}
	if(null != exception_msg_&& "User is disabled".equals(exception_msg_)){
		if(null != unvalidatUser &&"1".equals(unvalidatUser)){
			exception_msg_ = PlatformLocalesJSTL.getBundleValue("login.error.tip");
		}else{
			exception_msg_ = PlatformLocalesJSTL.getBundleValue("login.error.userlock.tip");	
		}
		
	}
%>

<title><%=PlatformLocalesJSTL.getBundleValue("login.title")%></title>
<meta content="text/html; charset=utf-8" http-equiv="Content-Type">
<meta name="description" content="">
<meta name="keywords" content="登录">
<link href="../static/css/platform/themes/default/login/style/style.css" type="text/css" rel="stylesheet">
<link rel="stylesheet" id="skins" type="text/css" href="../static/css/platform/themes/default/login/style/easyui.css">
<script src="../static/js/platform/component/jQuery/jQuery-1.8.2/jquery-1.8.2.js" type="text/javascript"></script>
<script src="../static/js/platform/component/jQuery/jquery-easyui-1.3.5/jquery.easyui.min.js" type="text/javascript"></script>
<script type="text/javascript" src="../static/js/platform/login/js/base.js"></script>
<script type="text/javascript">
var path = "<%=ViewUtil.getRequestPath(request)%>";
  $(function(){
	  var needCheckLock = "<%=needCheckLock%>";
	  var  needCheckLockFlag = false;
	  var firstLoginFalg = "<%=exception_msg_%>";
	  if(firstLoginFalg != "null" && firstLoginFalg.indexOf("首次登录")!=-1){
		  
		  $('#modify_dialog_login').dialog({
			    title: firstLoginFalg,
			    width: 400,
			    height: 400,
			    loadMsg:'数据加载中...',
			    closed: false,
			    cache: false,
			    modal: true
			});
		$('#modify_dialog_login').dialog('refresh', path+'avicit/platform6/modules/system/syspassword/syspassword.jsp');
		  
		
		  }
	  
	
});
</script>
</head>
<body>
	<div class="header" style="left: 0px; top: 0px">
		<h1 class="headerLogo">
			<a title="走近AVICIT." href="#"><img alt="AVICIT." src="../static/css/platform/themes/default/login/style/img/logo.png" align="middle"></a>
		</h1>
		<a class="headerIntro" title="你的专业信息化支撑伙伴" href="#"><span class="unvisi">你的专业信息化支撑伙伴</span></a>
		<div class="headerNav">
			<a href="#">帮助</a>
		</div>
	</div>
	<div id="mainBg" class="main">
		<div id="mainCnt" class="main-inner">
			<div id="theme">
				<div id="themeArea">
					<div id="themeAreaInner"></div>
				</div>
			</div>
			<div id="loginBlock" class="login">
				<div class="loginFunc"></div>
				<form action="<%=host%>/platform/user/login" method="post" style="height: 278px">
					<input id="username_" name="username_" class="userinput" tabindex="1" type="text" size="20" focucmsg="<%=PlatformLocalesJSTL.getBundleValue("login.username.tip.name")%>"></input>
					<input name="Password1" class="userinput" id="showPwd" size="20" tabindex="2" focucmsg="<%=PlatformLocalesJSTL.getBundleValue("login.password.tip.name")%>" type="text"></input>
					<input name="password_" class="userinput displaynone" id="password" size="20"  tabindex="2" type="password">
					<input name="Button1" class="g-button-submit" type="submit" value="<%=PlatformLocalesJSTL.getBundleValue("login.confirm.button.name")%>" />
					<div class="forgetPwdLine">
						<%
							if (exception_msg_ != null && exception_msg_ != "") {
								out.println("<font color=\"red\">"+ PlatformLocalesJSTL.getBundleValue("login.error.info")+":" + exception_msg_
										+ "</font>");
							}
						%>
					</div>
					<div id="browseVersion" class="forgetPwdLine">
						<%
							out.println("<font color=\"red\">"+ PlatformLocalesJSTL.getBundleValue("login.IE6.Version.info") + "</font>");
						%>
					</div>
					<div id="modify_dialog_login"  ></div>
				</form>
				<div class="loginCopyright"></div>
			</div>
		</div>
	</div>
	
	<div id="footer" class="footer">
		<div class="footer-inner">
			<a class="footerLogo" href="#" ><img alt="avicit" src="../static/css/platform/themes/default/login/style/img/netease_logo.gif"></a>
			<div class="footerNav">
				<a style="margin-right: 26px;" href="#">服务</a>|<span class="copyright">金航数码公司版权所有</span>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		////////////////////////////////////////////////////////////////////////////////////////////
		//加载随机主题
		(function() {
			var aTheme = [
			//tree - nRandom = 0
			{
				'bgSrc' : '../static/css/platform/themes/default/login/style/img/login01bg.jpg',
				'bgCnt' : '../static/css/platform/themes/default/login/style/img/login01.jpg',
				'bgTitle' : ''
			},
			//mother - nRandom = 1
			{
				'bgSrc' : '../static/css/platform/themes/default/login/style/img/login02bg.jpg',
				'bgCnt' : '../static/css/platform/themes/default/login/style/img/login02.jpg'
			},
			//lofter - nRandom = 2
			{
				'bgSrc' : '../static/css/platform/themes/default/login/style/img/login03bg.jpg',
				'bgCnt' : '../static/css/platform/themes/default/login/style/img/login03.jpg'
			},
			//young - nRandom = 3
			{
				'bgSrc' : '../static/css/platform/themes/default/login/style/img/pmo01bg.jpg',
				'bgCnt' : '../static/css/platform/themes/default/login/style/img/pmo01.jpg'
			},
			//fatmelon - nRandom = 4
			{
				'bgSrc' : '../static/css/platform/themes/default/login/style/img/pmo02bg.jpg',
				'bgCnt' : '../static/css/platform/themes/default/login/style/img/pmo02.jpg'
			} ], oBg = $id("mainBg"), oCnt = $id("mainCnt");
			//oTarTxt = $id("themeTxt"),
			//nRandom = fRandom(aTheme.length);
			//母亲节图40%、冬瓜图30%、五月青年节图20% 、LOFTER图10%。
			var nForRandom = fRandom(10);
			var nRandom = 0;
			if (nForRandom == 0) {
				nRandom = 1; //LOFTER图10%
			}
			if (nForRandom >= 1 && nForRandom <= 3) {
				nRandom = 2; //五月青年节图20%
			}
			if (nForRandom >= 4 && nForRandom <= 6) {
				nRandom = 3; //冬瓜图30%
			}
			if (nForRandom >= 7 && nForRandom <= 9) {
				nRandom = 4; //母亲节图40%
			}
			var oRandom = aTheme[nRandom];
			oCnt.style.backgroundImage = 'url(' + oRandom.bgCnt + ')';
			oCnt.style.backgroundRepeat = 'no-repeat';
			oCnt.style.backgroundPosition = 'center top';
			oBg.style.backgroundImage = 'url(' + oRandom.bgSrc + ')';
			oBg.style.backgroundRepeat = 'repeat-x';
		})();
		////////////////////////////////////////////////////////////////////////////////////////////
		//设置垂直居中
		function fBodyVericalAlign() {
			var nBodyHeight = 572;
			var nClientHeight = document.documentElement.clientHeight;
			if (nClientHeight >= nBodyHeight + 2) {
				var nDis = (nClientHeight - nBodyHeight) / 2;
				document.body.style.paddingTop = nDis + 'px';
			} else {
				document.body.style.paddingTop = '0px';
			}
		}
		fBodyVericalAlign();

		//onresize事件
		fEventListen(window, 'resize', fResize);
		fEventListen(window, 'resize', fBodyVericalAlign);

		$(document).ready(function() {
		$("#username_").each(function() {
			$(this).val($(this).attr("focucmsg"));
			$(this).val($(this).attr("focucmsg")).css("color", "#979393");
			$(this).focus(function() {
				if ($(this).val() == $(this).attr("focucmsg")) {
					$(this).val('');
					$(this).val('').css("color","#000");
				}
			});
			$(this).blur(function() {
				if (!$(this).val()) {
					$(this).val($(this).attr("focucmsg"));
					$(this).val($(this).attr("focucmsg")).css("color","#979393");
				}});
		});

		$("#showPwd").each(function() {
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
			}}); 
		});
	});
	</script>
<script type="text/javascript">
<!--
var s;
var Sys = {};
var ua = navigator.userAgent.toLowerCase();
(s = ua.match(/msie ([\d.]+)/)) ? Sys.ie = s[1] :
(s = ua.match(/firefox\/([\d.]+)/)) ? Sys.firefox = s[1] :
(s = ua.match(/chrome\/([\d.]+)/)) ? Sys.chrome = s[1] :
(s = ua.match(/opera.([\d.]+)/)) ? Sys.opera = s[1] :
(s = ua.match(/version\/([\d.]+).*safari/)) ? Sys.safari = s[1] : 0;
//判断IE版本
if(Sys.ie && ua.indexOf('msie 6.0') > 1){
	$('#browseVersion').css("display","block");
}else{
	$('#browseVersion').css("display","none");
}
//-->
</script>
</body>
</html>
