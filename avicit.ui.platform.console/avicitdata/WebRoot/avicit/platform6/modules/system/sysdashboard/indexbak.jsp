<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@page language="java"  pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@page import="avicit.platform6.core.spring.SpringFactory"%>
<%@page import="avicit.platform6.commons.utils.ViewUtil"%>
<%@page import="avicit.platform6.commons.utils.ProfileUtil"%>
<%@page import="avicit.platform6.modules.system.sysmenu.service.SysMenuService"%>
<%@page import="avicit.platform6.modules.system.sysmessage.service.SysMessageUtil"%>
<%@page import="avicit.platform6.core.session.SessionHelper"%>
<%@page import="avicit.platform6.modules.system.sysportal.service.SysPortalService"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@page import="avicit.platform6.core.api.system.syslocale.PlatformLocalesJSTL"%>

<%
	SysMenuService sysMenuService=SpringFactory.getBean(SysMenuService.class);
	SysPortalService sysPortalService = SpringFactory.getBean(SysPortalService.class);
    String indexElement = sysMenuService.getIndexElementVlign();
	String portalItems = sysMenuService.getDefaultSysFunction();
	String messageURL = sysMenuService.createTab4DefaultSysFunction("createTab:messageMgr;");
	String loginURL = sysMenuService.createTab4DefaultSysFunction("createTab:loginPortal;");
	int messageCount = SysMessageUtil.getUnreadedMessageTotal(SessionHelper.getLoginSysUserId());
 	String loginname = SessionHelper.getLoginSysUser().getName();
	String controlMessageDialog = ProfileUtil.getProfileValue("PLATFORM_V6_MESSAGE_DIALOG_SHOW"); //控制消息弹出窗口是否显示
	boolean isAdmin = sysPortalService.judgeSysRoleByCurrentUser();
	String quickSearch = sysMenuService.getQuickSearch();
%>
<html>
<head>
 
<script type="text/javascript">
<!--
	var logoutTip = "<%=PlatformLocalesJSTL.getBundleValue("platform.dashboard.index.logout.tip")%>";
	var onFocusTip = "<%=PlatformLocalesJSTL.getBundleValue("platform.search.onFocus.tip")%>";
	var resetPorletTip = "<%=PlatformLocalesJSTL.getBundleValue("platform.portlet.reset.tip")%>";
	var homePage = "<%=PlatformLocalesJSTL.getBundleValue("portal.index.homePage")%>";
	var messageIntervalTime = "<%=ProfileUtil.getProfileValue("PLATFORM_V6_MESSAGE_REQUEST_INTERVAL")%>";
	if(typeof(messageIntervalTime)=='undefined'){
		messageIntervalTime = 30000;
	}
//-->
</script>
<title><%=PlatformLocalesJSTL.getBundleValue("portal.index.homePage")%></title>
<base href="<%=ViewUtil.getRequestPath(request)%>">
<link rel="stylesheet" id="skins" type="text/css" href="avicit/platform6/component/js/jQuery/jquery-easyui-1.3.5/themes/default/easyui.css">
<link href="avicit/platform6/modules/system/css/platform/css/platform_blue.css" rel="stylesheet" type="text/css" />
<link href="avicit/platform6/modules/system/css/platform/css/platform_base.css" rel="stylesheet" type="text/css" />
<link href="avicit/platform6/modules/system/css/platform/css/platform_header.css" rel="stylesheet" type="text/css" />
<link href="avicit/platform6/modules/system/sysmessage/css/messageDialogCss.css" rel="stylesheet" type="text/css" />
<link href="avicit/platform6/component/js/jQuery/jquery-easyui-1.3.5/themes/icon.css" rel="stylesheet" type="text/css">

<script type="text/javascript" src="avicit/platform6/component/js/jQuery/jQuery-1.8.2/jquery-1.8.2.js"></script>
<script type="text/javascript" src="avicit/platform6/modules/system/js/jQuery/jQuery-easydrag-1.5/jquery.hoverIntent.minified.js"></script>
<script type="text/javascript" src="avicit/platform6/modules/system/css/platform/js/platform.jquery.dcmegamenu.1.3.3.js"></script>
<script type="text/javascript" src="avicit/platform6/component/js/jQuery/jquery-easyui-1.3.5/jquery.easyui.min.js"></script>
<script type="text/javascript" src="avicit/platform6/modules/system/css/platform/js/platform.dashboard.index.js"></script>
<script type="text/javascript" src="avicit/platform6/modules/system/sysmessage/js/messageDialog.js"></script>
<script type="text/javascript" src="avicit/platform6/modules/system/sysquicksearch/js/quickSearch.js"></script>
<script type="text/javascript">
var baseUrl = "<%=ViewUtil.getRequestPath(request)%>";
var messageCount = "<%=messageCount%>";
var doradoFlag = false;
var controlMessageDialog ="<%=controlMessageDialog%>";

function loadMessageData(){
	if(controlMessageDialog!="" && controlMessageDialog == "true"){
		loadMessage(baseUrl,messageIntervalTime,doradoFlag);
	}else{
		
	}
}
</script>
<!--[if IE 6]>
	<script type="text/javascript"> 
		try { document.execCommand('BackgroundImageCache', false, true); } catch(e) {} 
	</script>
<![endif]-->
</head>

<body class="easyui-layout"	onload="addTab('<%=PlatformLocalesJSTL.getBundleValue("portal.index.homePage")%>','platform/portlet/getContent?flag=','dorado/client/skins/~current/common/icons.gif','homePage','-0px -20px');loadMessageData();mainOfQuickSearch();">
	<div region="center" style="overflow: hidden;">
		<div id="divBodyWidth" style="height: 58px; position: relative; z-index: 5;">
			<div id="header">
				<div class="logo">
				<ul class="info" style="*width:auto;">
					<li>
						<div class="user"></div> 
						<%=loginname%>
					</li>
					<li>
						<div class="msgbg">
							<a href="javascript:void(0);" style="cursor:hand;" onclick="<%=messageURL%>"><%=messageCount%></a>
						</div> 
						<a href="javascript:void(0);" style="cursor:hand;" onclick="<%=messageURL%>"><%=PlatformLocalesJSTL.getBundleValue("portal.index.message")%></a>
					</li>
					<%=portalItems %>
				</ul>
				</div>
			</div>
		</div>
		<div class="blue" style="min-width:1003px;position:relative;z-index:4;">
			<ul id='mega-menu' class='mega-menu'>
				<%=indexElement%>
			</ul>
			<%=quickSearch%>
		</div>
		<div id="divBody">
			<div id="tabs" class="easyui-tabs" style='overflow: hidden;position:absolute;'></div>
		</div>
	</div>
	<div id="context_menu" class="easyui-menu" style="width:150px; display:none;">
		<div id="m-close" data-options="iconCls:'icon-close'"><%=PlatformLocalesJSTL.getBundleValue("portal.toolbar.close")%></div>
		<div class="menu-sep"></div>
		<div id="m-closeother" data-options="iconCls:'icon-close-other'"><%=PlatformLocalesJSTL.getBundleValue("portal.toolbar.closeOther")%></div>
		<div id="m-closeall" data-options="iconCls:'icon-close-all'"><%=PlatformLocalesJSTL.getBundleValue("portal.toolbar.closeAll")%></div>
		<div class="menu-sep"></div>
		<div id="m-refresh" data-options="iconCls:'icon-reload'"><%=PlatformLocalesJSTL.getBundleValue("portal.toolbar.refresh")%></div>
	</div>
	<div id="modify_dialog" title="<%=PlatformLocalesJSTL.getBundleValue("portal.modify.password")%>" style="background:#fff;padding:5px;width:500px;height:380px;display:none" >
	</div>
	<div id="menu">
	    <div id="tooltip_menu" >
	        <a href="javascript:void(0);" onclick="showDialog(true,'自定义个人布局',700,450,baseUrl + 'platform/portlet/getPortletConfig?isgloable=false&id=&layoutTemplateName=','saveLayout1()');return false;" ><img src='avicit/platform6/modules/system/sysdashboard/images/portlet_add.gif' border='0'>&nbsp;自定义个人布局</a>
	        <a href="javascript:void(0);" onclick="restDefault();return false;" class="menu_bottom"><img src='avicit/platform6/modules/system/sysdashboard/images/portlet_undo.gif' border='0'>&nbsp;<%=PlatformLocalesJSTL.getBundleValue("portal.reset.panel")%></a>
	    </div>
    </div>
	<div id="dialog" class="web_dialog">
	  <table style="width: 100%; border: 0px;" cellpadding="3" cellspacing="0">
	    <tr>
	      <td class="web_dialog_title"><span>Online Survey</span></td>
	      <td class="web_dialog_title align_right"><a href="#" id="btnClose">X</a></td>
	    </tr>
	    <tr>
	      <td colspan="2"><div id="context" style="width: 100%;"></div></td>
	    </tr>
	    <tr>
	      <td>&nbsp;</td>
	      <td align="right">
	      	<input type="button" id="save" value="<%=PlatformLocalesJSTL.getBundleValue("portal.conpnent.save")%>">
	      	<input type="button" id="cancel" onclick="hideDialog();return false;" value="<%=PlatformLocalesJSTL.getBundleValue("portal.conpnent.cancel")%>" >&nbsp;&nbsp;&nbsp;</td>
	    </tr>
	  </table>
	</div>
	<!-- 敏捷搜索提示框；默认隐藏，激发敏捷搜索后显示  -->
	<div id="agileSearchResult" class="agileSearchResults"></div>
</body>
</html>