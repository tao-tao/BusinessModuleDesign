

function closeParentDialog(state){
	if(state == '1'){
		$.messager.alert('提示','登录失败 ！','info');
		parent.$("#sasnyuan_dialog_login").dialog('close');
	}else{
		parent.$("#sasnyuan_dialog_login").dialog('close');
	}
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

function mate(password)
{
	var digit=0;
	var smallChar=0;
	var bigChar=0;
	var other=0;
	
	for(var i=0;i<password.length;i++)
	{
		var c=password.charAt(i);
		
		if(c<='9' && c>='0')
			digit=1;
		else if(c<='z' && c>='a')
			smallChar=1;
		else if(c<='Z' && c>='A')
			bigChar=1;
		else
			other=1;
	}
	
	return digit+smallChar+bigChar+other;
	
}

function sanyuanlogin()
{
	
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
		url : 'platform/syspassword/sysPasswordController/changePassword?path='+'${sessionScope.SANYUANLOGIN_REDIRECT}',
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
				if(r.data=="success")
				{
					closeParentDialog("1");
				}
				else
				{
					$.messager.alert('提示', r.data, 'warning');
				}
			}
			else
			{
				$.messager.alert('提示','登录失败','warning');
			}
		}
	}); 
}












