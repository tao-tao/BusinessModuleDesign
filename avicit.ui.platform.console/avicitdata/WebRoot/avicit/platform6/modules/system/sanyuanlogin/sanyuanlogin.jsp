<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改密码</title>
<base href="<%=ViewUtil.getRequestPath(request) %>">
<link href="avicit/platform6/modules/system/sysdept/css/icon.css" type="text/css" rel="stylesheet">
<jsp:include page="/avicit/platform6/component/common/EasyUIJsInclude.jsp"></jsp:include>
<jsp:include page="/avicit/platform6/modules/system/commonpopup/commonSelectionHead.jsp"></jsp:include>
</head>
<script src="avicit/platform6/modules/system/syspassword/syspassword.js" type="text/javascript"></script>
<script type="text/javascript">
var path = "<%=ViewUtil.getRequestPath(request)%>";
var sourceURL ="<%=request.getSession().getAttribute("SANYUANLOGIN_REDIRECT")%>";
  $(function(){
		  
		$('#sasnyuan_dialog_login').dialog({
			    title: '<font color=\"red\">你访问的模块受到安全保护，需要系统管理员，安全管理员，安全审计员同时登录才可以访问！</font>',
			    width: 580,
			    height: 480,
			    loadMsg:'数据加载中...',
			    closed: false,
			    cache: false,
			    modal: true
			});
		$('#sasnyuan_dialog_login').dialog('refresh', path+'avicit/platform6/modules/system/sanyuanlogin/sanyuanloginreal.jsp');
		  
		
});
  
  
  function login(){
		var systemlogin = $("#systemlogin").val();
		var systempassword = $("#systempassword").val();
		var safemanager = $("#safemanager").val();
		var safemanagerpassword = $("#safemanagerpassword").val();
		var safesheji = $("#safesheji").val();
		var safeshejipassword = $("#safeshejipassword").val();
		
		if(!hasText(systemlogin)){
			$.messager.alert('提示',"系统管理员不能为空",'warning');
			return;
		}
		if(!hasText(systempassword)){
			$.messager.alert('提示',"密码不能为空",'warning');
			return;
		}
		if(!hasText(safemanager)){
			$.messager.alert('提示',"安全管理员不能为空",'warning');
			return;
		}

		if(!hasText(safemanagerpassword)){
			$.messager.alert('提示',"密码不能为空",'warning');
			return;
		}
		if(!hasText(safesheji)){
			$.messager.alert('提示',"安全设计员不能为空",'warning');
			return;
		}
		if(!hasText(safeshejipassword)){
			$.messager.alert('提示',"密码不能为空",'warning');
			return;
		}
		
		$.ajax({
			url : 'platform/sanyuan/login?path='+sourceURL,
			data : {
				systemlogin: systemlogin,
				systempassword: systempassword,
				safemanager: safemanager,
				safemanagerpassword: safemanagerpassword,
				safesheji: safesheji,
				safeshejipassword: safeshejipassword
			},
			type : 'post',
			dataType : 'json',
			success : function(r) {
				
				if(r.result==0)
				{
					
					closeParentDialog("1");
					
					
				}
				else
				{
					$.messager.alert('提示','登录失败: ' +r.data,'warning');
				}
			}
		}); 
	}
	function hasText(str,trim){
		var tmp = str;
		if(null == tmp){
			return false;
		}
		if(trim){
			tmp = tmp.replace(/\s*/g,"");
		}
		if(0 == str.length){
			return false;
		}
		return true;
	}
	function closeParentDialog(state){
		if(state == '1'){
			$("#sasnyuan_dialog_login").dialog('close');
		}
		//location.href = path+"/avicit/platform6/modules/system/sysdashboard/index.jsp";
		location.href = path+'platform'+sourceURL;
	}
	function closeDialog(){
		
			$("#sasnyuan_dialog_login").dialog('close');
		
	}
</script>
<body>
	<div id="sasnyuan_dialog_login"  ></div>
</body>
</html>







