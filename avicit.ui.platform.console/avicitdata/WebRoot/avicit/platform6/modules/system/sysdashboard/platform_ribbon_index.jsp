<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@page import="avicit.platform6.api.session.SessionHelper"%>
<%@page import="avicit.platform6.modules.system.sysmenu.service.SysMenuService"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>

<%@page import="avicit.platform6.api.session.SessionParam"%>
<%@page import="avicit.platform6.api.session.SessionHelper"%>


<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<%@ page import="avicit.platform6.core.spring.SpringFactory"%>
<html>
<head>
<script type="text/javascript">
	var logoutTip = "您真的要注销系统吗?";
	var onFocusTip = "请输入检索内容...";
	var resetPorletTip = "确定要恢复默认设置吗?";
	var homePage = "系统首页";
	var messageIntervalTime = "30000";
	if (typeof (messageIntervalTime) == 'undefined') {
		messageIntervalTime = 30000;
	}
</script>

<title>系统首页</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<base href="<%=ViewUtil.getRequestPath(request) %>">


<link href="avicit/platform6/modules/system/css/pmoindex/css/pmo_blue.css" rel="stylesheet" type="text/css" />
<link href="avicit/platform6/modules/system/css/pmoindex/css/pmo_base.css" rel="stylesheet" type="text/css" />
<link href="avicit/platform6/modules/system/css/pmoindex/css/pmo_header.css" rel="stylesheet" type="text/css" />
<link href="avicit/platform6/modules/system/sysmessage/css/messageDialogCss.css" rel="stylesheet" type="text/css" />
<!--[if IE 6]>
	<script type="text/javascript"> 
		try { document.execCommand('BackgroundImageCache', false, true); } catch(e) {} 
	</script>
 <![endif]-->

<jsp:include page="/avicit/platform6/component/common/EasyUIJsInclude.jsp"></jsp:include>
<script type="text/javascript" src="avicit/platform6/modules/system/js/jQuery/jQuery-easydrag-1.5/jquery.hoverIntent.minified.js"></script>
<script type="text/javascript" src="avicit/platform6/modules/system/css/pmoindex/js/pmo.jquery.dcmegamenu.1.3.3.js"></script>
<script type="text/javascript" src="avicit/platform6/bpmclient/js/jQuery/cookie.jquery.js"></script>
<script type="text/javascript" src="avicit/platform6/modules/system/sysmessage/js/messageDialog.js"></script>
<script type="text/javascript" src="avicit/platform6/modules/system/sysquicksearch/js/quickSearch.js"></script>
<script type="text/javascript" src="avicit/platform6/modules/system/css/platform/js/oms_tree_index.js"></script>
<script type="text/javascript" src="avicit/platform6/modules/system/css/pmoindex/js/pmo.dashboard.index.js"></script>

<script type="text/javascript" src="avicit/platform6/modules/system/ribbonstyle/scripts/jquery.ribbon.buttom.js"></script>
<script type="text/javascript" src="avicit/platform6/modules/system/ribbonstyle/scripts/jquery.ribbon.menu.js"></script>
<link type="text/css" rel="stylesheet" href="avicit/platform6/modules/system/ribbonstyle/css/menu.css" >
<link type="text/css" rel="stylesheet" href="avicit/platform6/modules/system/ribbonstyle/css/buttom.css" >
<script type="text/javascript" src="avicit/platform6/modules/system/ribbonstyle/scripts/tab.js"></script>

<script type="text/javascript" src="avicit/cape/pmplan/taskinfo/CommonDialog.js"></script>

<script type="text/javascript" src="avicit/platform6/modules/system/sysdashboard/platform_ribbon_index.js"></script>
<style type="text/css">
#menu {
    top: 32px;
    right:5px;
    display: none;
    position:absolute;
    z-index:1000;
}
</style>
<script type="text/javascript">
	var baseUrl = "<%=ViewUtil.getRequestPath(request)%>";
</script>
<!-- 自定义 Logo backgroundImage -->
<link href="avicit/cape/login/login.css" rel="stylesheet" type="text/css" />
</head>
<body class="easyui-layout" style="overflow-y:auto;" onload="addTab('系统首页','avicit/platform6/modules/system/sysdashboard/indexPortlet.jsp','dorado/client/skins/~current/common/icons.gif','homePage','-80px -140px');return false;">
	<div region="center" style="overflow: hidden;">
		<div id="tabs" class="easyui-tabs" data-options="tools:'#tab-tools',toolPosition:'left'" ></div>
		<div id="tab-tools" style="padding: 0px; margin: 0px;">
			<div class="ribbon-menu" >
				<a href="javascript:void(0);" class="ribbon-menu-button">&nbsp;</a>
				<!-- 菜单栏 -->
				<div class="ribbon-menus">
				<%
					SysMenuService sysMenuService = SpringFactory.getBean(SysMenuService.class);
					String menu = sysMenuService.getIndexElementRibbon("zh_CN");
					String loginUserName = "admin";
					String loginUserDept = "XX";
				%>
				<%= menu %>				
				</div>
			</div>
		</div>
	</div>
	<div id="context_menu" class="easyui-menu"
		style="width: 150px; display: none;">
		<div id="m-close">关闭</div>
		<div class="menu-sep"></div>
		<div id="m-closeother">关闭其它</div>
		<div id="m-closeall">关闭全部</div>
		<div class="menu-sep"></div>
		<div id="m-refresh">刷新</div>
	</div>
	<div id="modify_dialog" title="修改密码"
		style="background: #fff; padding: 5px; width: 500px; height: 380px; display: none">
	</div>
	
	
	
    <div id="em">
  	<br />
  	<table  width="100%">
    	<tr>
        	<td rowspan="2" align="center" width="40%">
            	<img src="avicit/platform6/modules/system/ribbonstyle/images/mpm/48.png" />
            </td>
            <td align="center" width="30%">姓名</td>
            <td align="center" width="30%"><%=loginUserName %></td>
        </tr>
        <tr>
            <td>部门</td>
            <td><%=loginUserDept %></td>
        </tr>
    </table>
    <br />
    <div id="splitLine"> </div>
    <br />
    <table  width="100%">
    	<tr>
        	<td align="center" width="40%">
            	<a onclick="javascript:openSubWindow_pwd();return false;" href="javascript:void(0);">修改密码</a>
            </td>
            <td align="center" width="30%">
            	<a onclick="addTab('个人设置','avicit/platform6/modules/system/syscustomed/view/sysCustomedSetting.d7','static/images/platform/index/images/icons.gif','customedSetting','-60px -20px');return false;" href="javascript:void(0);">设置</a>
            </td>
            <td align="center" width="30%">
            	<a onclick="javascript:showVersionDialog();return false;" href="javascript:void(0);">版本</a>
            </td>
        </tr>
    </table>
    <br />
    <div class="buttom">
    	<table  width="100%" style="padding-top:10px;">
            <tr>
                <td align="center" width="40%">
                </td>
                <td align="center" width="30%"></td>
                <td align="center" width="30%">
                	<a onclick="javascript:logout();;return false;" href="javascript:void(0);">退出</a>
                </td>
            </tr>
    </table>
    </div>
  </div>
  
	<div id="menu">
		<div id="tooltip_menu">
			<a href="javascript:void(0);"
				onclick="openPortletAdd();return false;"><img
				src='avicit/platform6/modules/system/sysdashboard/images/portlet_add.gif'
				border='0'>&nbsp;添加首页应用</a> <a href="javascript:void(0);"
				onclick="openLayoutPage();return false;"><img
				src='avicit/platform6/modules/system/sysdashboard/images/portlet_undo.gif'
				border='0'>&nbsp;设置页面布局</a> <a href="javascript:void(0);"
				onclick="savePortlet();return false;"><img
				src='avicit/platform6/modules/system/sysdashboard/images/portlet_all.gif'
				border='0'>&nbsp;保存页面设置</a>
		</div>
	</div>
	<div id="dialog" class="web_dialog">
		<table style="width: 100%; border: 0px;" cellpadding="3"
			cellspacing="0">
			<tr>
				<td class="web_dialog_title"><span>Online Survey</span></td>
				<td class="web_dialog_title align_right"><a href="#"
					id="btnClose">X</a></td>
			</tr>
			<tr>
				<td colspan="2"><div id="context" style="width: 100%;"></div></td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td align="right"><input type="button" id="save" value="保存">
					<input type="button" id="cancel"
					onclick="hideDialog();return false;" value="取消">&nbsp;&nbsp;&nbsp;</td>
			</tr>
		</table>
	</div>
	<!-- 敏捷搜索提示框；默认隐藏，激发敏捷搜索后显示  -->
	<div id="agileSearchResult" class="agileSearchResults"></div>
	<!-- 底部工具栏 -->
	<div class="ribbon-buttom" isClosed="false">
	 	<div class="ribbon-buttom-menu" id="ribbonButtomMenu">
	 	   <%
	 	   Map<String,Object> params = new HashMap<String,Object>();
	 	   params.put(SessionParam.currentLanguageCode,SessionHelper.getCurrentLanguageCode(request));
		   params.put(SessionParam.applicationId,SessionHelper.getApplicationId());
		   params.put(SessionParam.loginSysUserId,SessionHelper.getLoginSysUserId(request));
	 	   String personalMenu = sysMenuService.getPersonalMenu(params);
	 		%>
	 		<%=personalMenu %>
        </div>
	</div>
	<!--系统版本dialog  -->
	<div id="versionDialog"></div>
</body>

</html>
