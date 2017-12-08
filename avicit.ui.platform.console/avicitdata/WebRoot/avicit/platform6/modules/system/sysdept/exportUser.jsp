<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<%@taglib prefix="sec" uri="/WEB-INF/tags/shiro.tld"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户查询</title>
<base href="<%=ViewUtil.getRequestPath(request)%>">
	<jsp:include page="/avicit/platform6/component/common/EasyUIJsInclude.jsp"></jsp:include>
<script type="text/javascript">

var baseurl = '<%=request.getContextPath()%>';

jQuery(function(){
	
});

function submitData(){
	document.getElementById("xlsExportForm").submit();
}

/**
 * 关闭窗口
 */
function closeDialog(){
	parent.$("#queryUserDialog").dialog('close');
}
</script>
</head>

<body>

<form id="xlsExportForm" target="" name="xlsExportForm" action="platform/sysdept/sysDeptController/exportExcelUserData" method="post">
	<input type="hidden" id="head_NAME" name="head_NAME" value="姓名"/>
	<input type="hidden" id="head_LOGIN_NAME" name="head_LOGIN_NAME" value="登录名"/>
	<input type="hidden" id="head_SECRET_LEVEL_NAME" name="head_SECRET_LEVEL_NAME" value="密级"/>
	<input type="hidden" id="head_SEX_NAME"  name="head_SEX_NAME" value="姓别"/>
	<input type="hidden" id="head_MOBILE" name="head_MOBILE" value="电话"/>
</form>
</body>
</html>