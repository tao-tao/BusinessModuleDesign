<%@ page contentType="text/html; charset=GBK"%>
<!-- CAS安全认证网关登录页面 -->
<%@ page import="java.net.URLEncoder"%>
<%@ page import="java.net.URLDecoder"%>
<%

			String loginAction = null;
	
			String path = request.getContextPath();

			String username = (String)request.getSession().getAttribute("LOGIN");
			java.security.Principal principal = request.getUserPrincipal();
			if(principal!=null){
				username= principal.getName();
			}
			loginAction =path+ "/platform/user/caslogin?username_=" +username;
			String sURI=request.getParameter("sourceURI");
			if(sURI!=null&&!"".equals(sURI)){
				loginAction+="&sourceURI="+sURI;
			}
			String sourceQueryString=request.getParameter("sourceQueryString");
			
			if(sourceQueryString!=null && !"".equals(sourceQueryString)){
				loginAction+="&sourceQueryString="+sourceQueryString;
			}

%>
    <script type="text/javascript">
    window.location.href="<%=loginAction%>";
	</script>
