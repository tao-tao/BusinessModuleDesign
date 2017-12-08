<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="avicit.platform6.commons.utils.ComUtil"%>
<%@page import="avicit.platform6.core.session.SessionHelper"%>
<%
 	String loginUserName = SessionHelper.getLoginSysUser().getName();
 	String loginUserId = SessionHelper.getLoginSysUser().getId();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>

<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<base href="<%=ComUtil.getRequestPath(request) %>">
<script src="avicit/platform6/bpmclient/js/jQuery/jQuery-1.8.2/jquery-1.8.2.min.js" type="text/javascript"></script>
<script src="avicit/platform6/component/js/jQuery/jquery-easyui-1.3.5/jquery.easyui.min.js" type="text/javascript"></script>
<link href="avicit/platform6/component/js/jQuery/jquery-easyui-1.3.5/themes/gray/easyui.css" type="text/css" rel="stylesheet">
<!-- <script src="avicit/platform6/modules/system/sysportal/js/sysPortletConsole.js" type="text/javascript"></script> -->
<!-- end jquery/jquery easyui javascript -->
<style type="text/css">
	body{
		font-family: Microsoft Yahei,sans-serif,Arial,Helvetica;
		font-size:12px;
		margin:0px 0px 0px 0px;
	}
</style>
<script type="text/javascript">
	var portletId = '${portletId}';
	var columnId = '${columnId}';
	
	function getCheckboxValue(){
		var v = "";
		var r = document.getElementsByName('webComponentCheckBoxGroup');
		if( typeof r.length =='undefined'){
			v=r.value;
		}else{
			for ( var i = 0; i < r.length; i++) {
				if(r[i].checked){
					if(v==""){
						v=r[i].value;
					}else {
						v= v +'|'+r[i].value; 
					}
				}
			}
		}
		return v;
	} 
	function getCheckboxText(checkValues){
		var v = "";
		var backValues = checkValues.split('|');
		for(var i = 0 ; i < backValues.length ; i++){
			if(v==""){
				v = $('#' + backValues[i]).text();
			}else {
				v= v +'|'+ $('#' + backValues[i]).text(); 
			}
		}
		return v;
	}
</script>
</head>
<body class="easyui-layout" id='layout'>
	<table width="100%"><tr><td align="center">
		<table width="80%">
			${webComponent}
	    </table>
	</td></tr></table>
</body>
</html>
