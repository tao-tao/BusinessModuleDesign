<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ page import="avicit.platform6.api.session.dto.SecurityUser"%>
<%@ page import="avicit.platform6.api.session.dto.SecurityMenu"%>
<%@ page import="avicit.platform6.core.spring.SpringFactory"%>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<%@ page import="avicit.platform6.modules.system.sysmessage.service.SysMessageUtil"%>
<%@ page import="avicit.platform6.api.session.SessionHelper"%>
<%@ page import="avicit.platform6.api.sysprofile.SysProfileAPI"%>
<%@ page import="avicit.platform6.api.sysmenu.impl.SysMenuAPImpl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@page import="avicit.platform6.core.locale.PlatformLocalesJSTL"%>
<%@page import="avicit.platform6.api.session.dto.SecurityUser"%>
<%@page import="avicit.platform6.api.sysshirolog.impl.AfterLoginSessionProcess"%>
<%
	SysMenuAPImpl sysMenuAPI = SpringFactory.getBean(SysMenuAPImpl.class);
	SysProfileAPI sysProfileAPI = SpringFactory.getBean("sysProfileAPI");
	SecurityUser user = SessionHelper.getSecurityUser(request);
	
	SecurityMenu menu = new SecurityMenu();
	menu.reflashMenu(SessionHelper.getApplicationId());
	//SysPortalService sysPortalService = SpringFactory.getBean(SysPortalService.class);
    String indexElement = sysMenuAPI.getIndexElementVlign(SessionHelper.getCurrentLanguageCode(request), user, menu,SessionHelper.getApplicationId());
    
	request.getSession().setAttribute("CURRENT_LOGINUSER_SECURITY", user);
	String portalItems = sysMenuAPI.getDefaultSysFunction(SessionHelper.getApplicationId(),SessionHelper.getCurrentLanguageCode(request));
	String loginname =user.getUser().getName();
	String messageURL = sysMenuAPI.createTab4DefaultSysFunction("createTab:messageMgr;",SessionHelper.getApplicationId(),SessionHelper.getCurrentLanguageCode(request));
 	String fulltextSearchPath = "";//SearchPathUtil.deletePrefix(SearchPathUtil.SEARCH_SEARCH_ALL_INDEX_BYPAGE_ACTION);
	String agileSearchPath = "";//SearchPathUtil.deletePrefix(SearchPathUtil.AGILE_SEARCH_ACTION);
	String controlMessageDialog = sysProfileAPI.getProfileValueByCode("PLATFORM_V6_MESSAGE_DIALOG_SHOW"); //控制消息弹出窗口是否显示
	String messageCount = "";
	boolean isAdmin = true;//sysPortalService.judgeSysRoleByCurrentUser();
	String quickSearch = sysMenuAPI.getQuickSearch(SessionHelper.getLocaleByUser(request));
	//add by xingc
	String skinColor = (String)request.getSession().getAttribute(AfterLoginSessionProcess.SESSION_CURRENT_USER_SKIN);
	String skinSwitch = (String)request.getSession(false).getAttribute(AfterLoginSessionProcess.SESSION_CURRENT_SKIN_SWITCH);
	skinColor = "default";
%>
<html>
<head>
 <meta http-equiv="X-UA-Compatible" content="IE=8">
<script type="text/javascript">
	var logoutTip = "<%=PlatformLocalesJSTL.getBundleValue("platform.dashboard.index.logout.tip")%>";
	var onFocusTip = "<%=PlatformLocalesJSTL.getBundleValue("platform.search.onFocus.tip")%>";
	var resetPorletTip = "<%=PlatformLocalesJSTL.getBundleValue("platform.portlet.reset.tip")%>";
	var homePage = "<%=PlatformLocalesJSTL.getBundleValue("portal.index.homePage")%>";
	var messageIntervalTime = "<%=sysProfileAPI.getProfileValueByCode("PLATFORM_V6_MESSAGE_REQUEST_INTERVAL")%>";
	if(typeof(messageIntervalTime)=='undefined'){
		messageIntervalTime = 30000;
	}
	var skinSwitch = "<%=skinSwitch %>";
</script>

<title><%=PlatformLocalesJSTL.getBundleValue("portal.index.homePage")%></title>
<base href="<%=ViewUtil.getRequestPath(request)%>">

<!-- modify by xingc-->
<link href="static/css/platform/themes/<%=skinColor %>/index/style/easyui.css" rel="stylesheet" id="skins" type="text/css" />
<link href="static/css/platform/themes/<%=skinColor %>/pmo_index/style/pmo_base.css" rel="stylesheet" type="text/css" />
<link href="static/css/platform/themes/<%=skinColor %>/pmo_index/style/pmo_title.css" rel="stylesheet" type="text/css" />
<link href="static/css/platform/themes/<%=skinColor %>/pmo_index/style/pmo_header.css" rel="stylesheet" type="text/css" />
<link href="static/css/platform/themes/<%=skinColor %>/sysmessage/style/messageDialogCss.css" rel="stylesheet" type="text/css" />

 <!--[if IE 6]>
	<script type="text/javascript"> 
		try { document.execCommand('BackgroundImageCache', false, true); } catch(e) {} 
	</script>
 <![endif]-->
 
<script type="text/javascript" src="static/js/platform/component/jQuery/jQuery-1.8.2/jquery-1.8.2.js"></script>
<script type="text/javascript" src="static/js/platform/index/js/jQuery/jQuery-easydrag-1.5/jquery.hoverIntent.minified.js"></script>
<script type="text/javascript" src="static/js/platform/index/js/jQuery/jQuery-easyui-1.2.6/jquery.easyui.min.js"></script>
<script type="text/javascript" src="static/js/platform/bpm/client/js/cookie.jquery.js"></script>
<script type="text/javascript" src="static/js/platform/sysmessage/js/messageDialog.js"></script>
<script type="text/javascript" src="static/js/platform/search/js/quickSearch.js"></script>
<script type="text/javascript" src="static/js/platform/component/common/CommonDialog.js"></script>
<script type="text/javascript" src="static/js/platform/index/js/pmo.jquery.dcmegamenu.1.3.3.js"></script>
<script type="text/javascript" src="static/js/platform/index/js/pmo.dashboard.index.js"></script>
<script type="text/javascript" src="static/js/platform/index/js/skin.js"></script>
<script type="text/javascript">

	var doradoFlag = false;
	var baseUrl = "<%=ViewUtil.getRequestPath(request)%>";
	var messageCount = "<%=messageCount%>";
	var controlMessageDialog ="<%=controlMessageDialog%>";
	var messageIntervalTime = "<%=sysProfileAPI.getProfileValueByCode("PLATFORM_V6_MESSAGE_REQUEST_INTERVAL")%>";
	if(typeof(messageIntervalTime)=='undefined'){
		messageIntervalTime = 30000;
	}
	var fulltextSearchPath = "<%= fulltextSearchPath %>";
	var agileSearchPath = "<%= agileSearchPath %>";
	function loadMessageData(){
		if(controlMessageDialog!="" && controlMessageDialog == "true"){
			loadMessage(baseUrl,messageIntervalTime,doradoFlag);
		}else{
			
		}
	}
	
	function openLayoutPage() {
		var dialogId = "layoutDialog";
		var usd = new CommonDialog(dialogId, "450", "450",
				baseUrl + 'avicit/platform6/modules/system/sysdashboard/indexPortletConfig.jsp?isgloable=false&dialogId='+dialogId, "设置页面布局", false, true, false);
		usd.show();
	}
	function openPortletAdd() {
		var dialogId = "portletAddDialog";
		var usd = new CommonDialog(dialogId, "450", "450",
				baseUrl + 'avicit/platform6/modules/system/sysdashboard/indexMpmPortletAdd.jsp?isgloable=false&dialogId='+dialogId, "添加首页应用", false, true, false);
		usd.show();
	}
	
	function savePortlet(){
		var portlet = "";
		var iframeBody = $("#iframeBody")[0].contentWindow;
		var portletRow = iframeBody.$("#portalContent .ui-portlet");
		var layout = iframeBody.$("#layout").val();
		$.each(portletRow,function(k,v){
			 var indexs = iframeBody.getPortletInfo(v.id);
	         $.each(indexs, function(k1, v1) {
	        	 portlet = portlet + v.id+";"+k1+";"+v1.x+":"+v1.y+"@";
	         });
		});

		if(portlet!=null&&portlet!=""){
			portlet = portlet.substring(0,portlet.length-1);
			$.ajax({ 
				url: baseUrl+'platform/IndexPortalController/saveIndexPortlet',
				async : false,
				type: "POST",
				data : 'isgloable=false&portlet=' + portlet+'&layout='+layout,
				success: function(){
					//获取当前操作的数据行记录
					alert('保存页面设置成功!')
					hideDialog();
					window.location.reload();
					//refreshPortlet();	
				},
				error : function(){
					//alert('portlet配置信息保存失败!');
				}
			});
		}

	}
	
</script>
</head>
<body class="easyui-layout"	onload="addTab('<%=PlatformLocalesJSTL.getBundleValue("portal.index.homePage")%>','avicit/platform6/modules/system/sysdashboard/indexPortlet.jsp','static/images/platform/index/images/icons.gif','','-0px -20px');loadMessageData();mainOfQuickSearch();">
	<div region="center" style="overflow: hidden;">
		<div id="divBodyWidth" style="height: 44px;position: relative; z-index: 1;">
			<div id="header">
				<div class="logo"></div>
				<!--IE7，IE6都有这个问题，必须给出width -->
				<ul class="info" style="*width:auto;">
					<!-- <li style="margin-top: 10px;">
						<div title="灰色" style="cursor:pointer;float:left;overflow:hidden;width:9px; height:9px;margin-left:3px;margin-right:3px;background-color:#798da5; border:1px solid white;" onclick="switchSkin('gray');"></div>
				        <div title="蓝色" style="cursor:pointer;float:left;overflow:hidden;width:9px; height:9px;margin-left:3px;margin-right:3px;background-color:#287ce7; border:1px solid white;" onclick="switchSkin('blue');"></div>
				        <div title="默认" style="cursor:pointer;float:left;overflow:hidden;width:9px; height:9px;margin-left:3px;margin-right:3px;background-color:#287ce7; border:1px solid white;" onclick="switchSkin('default');"></div>
					</li> -->
					<li>
						<div class="user"></div> 
						<%=loginname%>
					</li>
					<li>
						<div class="msgbg">
							<a href="javascript:void(0);" style="cursor:hand;"" onclick="<%=messageURL%>"><%=messageCount%></a>
						</div> 
						<a href="javascript:void(0);" style="cursor:hand;" onclick="<%=messageURL%>"><%=PlatformLocalesJSTL.getBundleValue("portal.index.message")%></a>
					</li>
					<%=portalItems %>
				 </ul>
			</div>	
		</div>
		<!-- modify by xingc-->
		<div class="blue" style="min-width: 1003px;position:relative;z-index:4;">
			<ul id='mega-menu' class='mega-menu'>
				<%=indexElement%>
			</ul>
			<%=quickSearch%>
		</div>
		<div id="divBody">
			<div id="tabs" class="easyui-tabs"  style='overflow: hidden;position:absolute;'></div>
		</div>
	</div>
	<div id="context_menu" class="easyui-menu" style="width:150px;display:none;">
		<div id="m-close" ><%=PlatformLocalesJSTL.getBundleValue("portal.toolbar.close")%></div>
		<div class="menu-sep"></div>
		<div id="m-closeother"><%=PlatformLocalesJSTL.getBundleValue("portal.toolbar.closeOther")%></div>
		<div id="m-closeall"><%=PlatformLocalesJSTL.getBundleValue("portal.toolbar.closeAll")%></div>
		<div class="menu-sep"></div>
		<div id="m-refresh"><%=PlatformLocalesJSTL.getBundleValue("portal.toolbar.refresh")%></div>
	</div>
	<div id="modify_dialog" title="<%=PlatformLocalesJSTL.getBundleValue("portal.modify.password")%>" style="background:#fff;padding:5px;width:500px;height:380px;display:none" >
	</div>
	<div id="menu">
	    <div id="tooltip_menu" >
	        <a href="javascript:void(0);" onclick="openPortletAdd();return false;" ><img src='avicit/platform6/modules/system/sysdashboard/images/portlet_add.gif' border='0'>&nbsp;添加首页应用</a>
	        <a href="javascript:void(0);" onclick="openLayoutPage();return false;" ><img src='avicit/platform6/modules/system/sysdashboard/images/portlet_undo.gif' border='0'>&nbsp;设置页面布局</a>
	        <a href="javascript:void(0);" onclick="savePortlet();return false;" ><img src='avicit/platform6/modules/system/sysdashboard/images/portlet_all.gif' border='0'>&nbsp;保存页面设置</a>
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