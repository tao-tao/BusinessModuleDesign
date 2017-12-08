<%@ page contentType="text/html; charset=UTF-8"%>
<!-- 格尔安全认证网关登录页面 -->
<%@ page import="java.net.URLEncoder"%>
<%@ page import="java.net.URLDecoder"%>
<%@page import="org.apache.log4j.Logger"%>
<%
	Logger logger = Logger.getLogger(this.getClass());
	// FXIME 以后能把登录和注销相关代码进行一下重构，现在有点乱
	// 登录页

	String path = request.getContextPath();
	String loginJsp ="/login/login.jsp";
	// 集成认证成功后跳转登录action
    Cookie[] cookies = request.getCookies();
	if (cookies == null) {
		cookies = new Cookie[0];
	} 
	String loginAction = null;
	for (Cookie ck : cookies) {
		logger.info(ck.getName() + " = " + ck.getValue());
		if ("KOAL_CERT_CN".equals(ck.getName())) {
			String username = new String(URLDecoder.decode(ck.getValue(), "ISO-8859-1").getBytes("ISO-8859-1"), "GBK");
			
			/**
			*  此处不再对用户名进行md5加密 马义 2010年10月21日
			loginAction =path+ "/platform/user/caslogin?username_=" +username;
			*/
			//username = MD5Utils.getMD5DigestHex(URLEncoder.encode(username, "UTF-8")).toLowerCase();
			// 因为这里的用户名已经MD5过，所以可以直接在url中进行传递
			loginAction =path+ "/platform/user/caslogin?username_=" + URLEncoder.encode(username, "UTF-8");
			break;
		}
	}
	if (loginAction != null) {
		response.sendRedirect(response.encodeRedirectURL(loginAction));
	} else {
		request.getRequestDispatcher(loginJsp).forward(request, response);
	}
%>
