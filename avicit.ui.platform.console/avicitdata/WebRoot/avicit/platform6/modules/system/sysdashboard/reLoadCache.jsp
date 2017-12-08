<%@page import="avicit.platform6.core.spring.SpringFactory"%>
<%@page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="avicit.platform6.api.application.SysApplicationAPI"%>
<%@page import="avicit.platform6.api.syscustomed.SysCustomedAPI"%>
<%@page import="avicit.platform6.api.syslanguage.SysLanguageAPI"%>
<%@page import="avicit.platform6.api.syslookup.SysLookupAPI"%>
<%@page import="avicit.platform6.api.sysmenu.SysMenuAPI"%>
<%@page import="avicit.platform6.api.sysmessage.SysMessageAPI"%>
<%@page import="avicit.platform6.api.syspassword.SysPasswordAPI"%>
<%@page import="avicit.platform6.api.syspermissionresource.SysPermissionResourceAPI"%>
<%@page import="avicit.platform6.api.sysprofile.SysProfileAPI"%>
<%@page import="avicit.platform6.api.sysresource.SysResourceAPI"%>
<%@page import="avicit.platform6.api.sysuser.SysUserAPI"%>
<%@page import="avicit.platform6.api.sysuserlimitip.SysUserLimitIpAPI"%>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<%@page import="avicit.platform6.api.session.SessionHelper"%>
<%@page import="avicit.platform6.api.sysuser.SysRoleAPI"%>
<%
boolean isAdmin = false;
String path = request.getContextPath();
String basePath = ViewUtil.getRequestPath(request);

//因为配置到了菜单中，可以通过授权管理该界面，所有屏蔽掉对管理员的判断
//SysRoleAPI sysRoleAPI = SpringFactory.getBean(SysRoleAPI.class);
//if(!sysRoleAPI.isAdministrator(SessionHelper.getLoginName(request))){
//	isAdmin = false;//只允许【平台管理员】使用该界面。
//}else{
	isAdmin = true;
	SysApplicationAPI sysApplicationAPI = SpringFactory.getBean(SysApplicationAPI.class);
	SysCustomedAPI sysCustomedAPI = SpringFactory.getBean(SysCustomedAPI.class);
	SysLanguageAPI sysLanguageAPI = SpringFactory.getBean(SysLanguageAPI.class);
	SysLookupAPI sysLookupAPI = SpringFactory.getBean(SysLookupAPI.class);
	SysMenuAPI sysMenuAPI = SpringFactory.getBean(SysMenuAPI.class);
	SysMessageAPI sysMessageAPI = SpringFactory.getBean(SysMessageAPI.class);
	SysPasswordAPI sysPasswordAPI = SpringFactory.getBean(SysPasswordAPI.class);
	SysPermissionResourceAPI sysPermissionResourceAPI = SpringFactory.getBean(SysPermissionResourceAPI.class);
	SysProfileAPI sysProfileAPI = SpringFactory.getBean(SysProfileAPI.class);
	SysResourceAPI sysResourceAPI = SpringFactory.getBean(SysResourceAPI.class);
	SysUserAPI sysUserAPI = SpringFactory.getBean(SysUserAPI.class);
	SysUserLimitIpAPI sysUserLimitIpAPI = SpringFactory.getBean(SysUserLimitIpAPI.class);
	
	String flg = request.getParameter("_flg");
	if(flg != null){
		if(flg.equals("SysApplicationAPI")){
			sysApplicationAPI.reLoad();
			response.getWriter().write("<script type='text/javascript'>alert('刷新多应用缓存完成！');</script>");
		}else if(flg.equals("SysCustomedAPI")){
			sysCustomedAPI.reLoad();
			response.getWriter().write("<script type='text/javascript'>alert('刷新个人设置缓存完成！');</script>");
		}else if(flg.equals("SysLanguageAPI")){
			sysLanguageAPI.reLoad();
			response.getWriter().write("<script type='text/javascript'>alert('刷新多语言缓存完成！');</script>");
		}else if(flg.equals("SysLookupAPI")){
			sysLookupAPI.reLoad();
			response.getWriter().write("<script type='text/javascript'>alert('刷新通用代码缓存完成！');</script>");
		}else if(flg.equals("SysMenuAPI")){
			sysMenuAPI.reLoad();
			response.getWriter().write("<script type='text/javascript'>alert('刷新菜单缓存完成！');</script>");
		}else if(flg.equals("SysMessageAPI")){
			sysMessageAPI.reLoad();
			response.getWriter().write("<script type='text/javascript'>alert('刷新消息缓存完成！');</script>");
		}else if(flg.equals("SysPasswordAPI")){
			sysPasswordAPI.reLoad();
			response.getWriter().write("<script type='text/javascript'>alert('刷新密码缓存完成！');</script>");
		}else if(flg.equals("SysPermissionResourceAPI")){
			sysPermissionResourceAPI.reLoad();
			response.getWriter().write("<script type='text/javascript'>alert('刷新数据权限缓存完成！');</script>");
		}else if(flg.equals("SysProfileAPI")){
			sysProfileAPI.reLoad();
			response.getWriter().write("<script type='text/javascript'>alert('刷新系统参数缓存完成！');</script>");
		}else if(flg.equals("SysResourceAPI")){
			sysResourceAPI.reLoad();
			response.getWriter().write("<script type='text/javascript'>alert('刷新系统资源缓存完成！');</script>");
		}else if(flg.equals("SysUserAPI")){
			sysUserAPI.reLoad();
			response.getWriter().write("<script type='text/javascript'>alert('刷新大用户模块缓存完成！');</script>");
		}else if(flg.equals("SysUserLimitIpAPI")){
			sysUserLimitIpAPI.reLoad();
			response.getWriter().write("<script type='text/javascript'>alert('刷新IP限制缓存完成！');</script>");
		}else if(flg.equals("ALL")){
			sysApplicationAPI.reLoad();
			sysCustomedAPI.reLoad();
			sysLanguageAPI.reLoad();
			sysLookupAPI.reLoad();
			sysMenuAPI.reLoad();
			sysMessageAPI.reLoad();
			sysPasswordAPI.reLoad();
			sysPermissionResourceAPI.reLoad();
			sysProfileAPI.reLoad();
			sysResourceAPI.reLoad();
			sysUserAPI.reLoad();
			sysUserLimitIpAPI.reLoad();
			response.getWriter().write("<script type='text/javascript'>alert('刷新所有缓存完成！');</script>");
		}
		//response.sendRedirect(basePath+"avicit/platform6/modules/system/sysdashboard/reLoadCache.jsp");
	}
//}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>My JSP 'reLoadCache.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<style type="text/css">
		button{
			height:25px;
			width:150px;
			color: #606060;
			border: solid 1px #b7b7b7;
			background: #fff;
			background: -webkit-gradient(linear, left top, left bottom, from(#fff), to(#ededed));
			background: -moz-linear-gradient(top,  #fff,  #ededed);
			filter:  progid:DXImageTransform.Microsoft.gradient(startColorstr='#ffffff', endColorstr='#ededed');
		}
	</style>
  </head>
  
  <body>
  	<%if(isAdmin){ %>
  		<table>
  			<tr>
  				<td>
  					<button onclick="reLoadCache('SysApplicationAPI')">刷新多应用缓存</button>
  				</td>
  				<td>
  					<button onclick="reLoadCache('SysCustomedAPI')">刷新个人设置缓存</button>
  				</td>
  				<td>
  					<button onclick="reLoadCache('SysLanguageAPI')">刷新多语言缓存</button>
  				</td>
  				<td>
  					<button onclick="reLoadCache('SysLookupAPI')">刷新通用代码缓存</button>
  				</td>
  			</tr>
  			<tr>
  				<td>
  					<button onclick="reLoadCache('SysMenuAPI')">刷新菜单缓存</button>
  				</td>
  				<td>
  					<button onclick="reLoadCache('SysMessageAPI')">刷新消息缓存</button>
  				</td>
  				<td>
  					<button onclick="reLoadCache('SysPasswordAPI')">刷新密码缓存</button>
  				</td>
  				<td>
  					<button onclick="reLoadCache('SysPermissionResourceAPI')">刷新数据权限缓存</button>
  				</td>
  			</tr>
  			<tr>
  				<td>
  					<button onclick="reLoadCache('SysProfileAPI')">刷新系统参数缓存</button>
  				</td>
  				<td>
  					<button onclick="reLoadCache('SysResourceAPI')">刷新系统资源缓存</button>
  				</td>
  				<td>
  					<button onclick="reLoadCache('SysUserAPI')">刷新大用户模块缓存</button>
  				</td>
  				<td>
  					<button onclick="reLoadCache('SysUserLimitIpAPI')">刷新IP限制缓存</button>
  				</td>
  			</tr>
  			<tr>
  				<td>
  					<button onclick="reLoadCache('ALL')">刷新所有缓存</button>
  				</td>
  				<td>
  				</td>
  				<td>
  				</td>
  				<td>
  				</td>
  			</tr>
  		</table>
	    
	    <script type="text/javascript">
	    	function reLoadCache(flg){
	    		location = "<%=basePath%>avicit/platform6/modules/system/sysdashboard/reLoadCache.jsp?_flg="+flg;
	    	}
	    	
	    </script>
    <%}else{ %>
    	<p>无管理员权限！只允许【平台管理员】使用该界面！</p>
    <%} %>
  </body>
</html>
