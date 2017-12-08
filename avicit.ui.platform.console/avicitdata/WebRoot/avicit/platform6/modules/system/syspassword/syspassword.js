/**
 * @author wangyi
 */

var dialog_first_row_tip = "提示：您为"; //\u63d0\u793a\uff1a\u60a8\u4e3a
var dialog_second_row_tip = "密码要求：您的密码"; //\u5bc6\u7801\u8981\u6c42\uff1a\u60a8\u7684\u5bc6\u7801
var dialog_third_row_tip = "最大长度不要超过"; //\u6700\u5927\u957f\u5ea6\u4e0d\u8981\u8d85\u8fc7
var dialog_fourth_row_tip = "最小长度不能小于"; //\u6700\u5c0f\u957f\u5ea6\u4e0d\u80fd\u5c0f\u4e8e
var dialog_five_row_tip = "其中大写字母、小写字母、数字、特殊字符至少要包含"; //\u5176\u4e2d\u5927\u5199\u5b57\u6bcd\u3001\u5c0f\u5199\u5b57\u6bcd\u3001\u6570\u5b57\u3001\u7279\u6b8a\u5b57\u7b26\u81f3\u5c11\u8981\u5305\u542b
var dialog_five_row_end_tip = ""; 
var dialog_six_row_begin_tip = "不能和前"; //\u4e0d\u80fd\u548c\u524d
var dialog_six_row_end_tip = "次密码重复"; //\u6b21\u5bc6\u7801\u91cd\u590d

var dialog_seven_row_begin_tip = "不能和旧密码有"; //\u4e0d\u80fd\u548c\u65e7\u5bc6\u7801\u6709
var dialog_seven_row_end_tip = "位（连续）重复"; //\u4f4d\uff08\u8fde\u7eed\uff09\u91cd\u590d

var dialog_eight_row_begin_tip = "下次请在"; //\u4e0b\u6b21\u8bf7\u5728
var dialog_eight_row_middle_tip = "天"; //\u5929
var dialog_eight_row_end_tip = "前修改密码"; //\u524d\u4fee\u6539\u5bc6\u7801

var password_length_tip = "位"; //\u4f4d

var shangeSelfPasswordVo; 

$(function(){

	$.ajax({
		url : 'platform/syspasswordtemplate/sysPasswordTemplateController/getChangeSelfPasswordVo',
		data : {
			
		},
		type : 'post',
		dataType : 'json',
		success : function(r) {
			
			if(r.result==0){
				
				//$.messager.alert('提示','首页菜单样式设置成功！重新登录即可生效。','warning');
				shangeSelfPasswordVo=r.data;
				
				generateTipContent();
			}
			else
			{
				$.messager.alert('提示','获取个人密码数据失败','warning');
			}
		}
	}); 
	
}); 

function generateTipContent()
{
	var tipContent = $("#tipContent");

	var maxlength = shangeSelfPasswordVo.maxlength;
	var intensity = shangeSelfPasswordVo.intensity;
	var distinctBefore = shangeSelfPasswordVo.distinctBefore;
	var minlength = shangeSelfPasswordVo.minlength;
	var difference = shangeSelfPasswordVo.difference;
	var oldPassword = shangeSelfPasswordVo.oldPassword;
	var secretLevelName = shangeSelfPasswordVo.secretLevelName;
	helpMessage = dialog_first_row_tip+"<font color='red'>"+secretLevelName+"</font><br/>";
	helpMessage+="　　　"+dialog_second_row_tip+"<br/>";
	//alert(maxlength == null);
	if(maxlength!="" && maxlength != null && maxlength != 'null'){
		helpMessage+="　　　"+dialog_third_row_tip+" <font color='red'>"+maxlength+"</font> "+password_length_tip+"<br/>";
	}
	if(minlength!="" && minlength != null){
		helpMessage+="　　　"+dialog_fourth_row_tip+" <font color='red'>"+minlength+"</font> "+password_length_tip+"<br/>";
	}
	if(intensity!="" && intensity != null){
		helpMessage+="　　　"+dialog_five_row_tip+" <font color='red'>"+intensity+"</font> "+dialog_five_row_end_tip+"<br/>	";
	}
	if(distinctBefore!="" && distinctBefore != null){
		helpMessage+="　　　"+dialog_six_row_begin_tip+" <font color='red'>"+distinctBefore+"</font> "+dialog_six_row_end_tip+"<br/>";
	}
	if(difference!="" && difference != null){
		helpMessage+="　　　"+dialog_seven_row_begin_tip+" <font color='red'>"+difference+"</font> "+dialog_seven_row_end_tip+"<br/>";
	}
	var noticeDateBeforeNum = null;

	if (null != shangeSelfPasswordVo.howLongModify && shangeSelfPasswordVo.howLongModify.length > 0) {
		noticeDateBeforeNum = shangeSelfPasswordVo.howLongModify; 
	} 
	if (null != noticeDateBeforeNum) {
		var dateStr = format(new Date().getTime()+noticeDateBeforeNum*24*60*60*1000);
		helpMessage+="　　　"+dialog_eight_row_begin_tip+" <font color='red'>"+dateStr+"</font>"+dialog_eight_row_end_tip+"<br/>";
	}
	helpMessage =helpMessage.substring(0,helpMessage.length-1);
	helpMessage+=".";
	tipContent.html(helpMessage);
	tipContent.css("margin-top:120px;width: 400px; overflow-x: hidden; overflow-y: auto; position: relative; left: 0px; bottom: 0px;");
	tipContent.attr("visible",true);
}

function add0(m){
	return m<10?'0'+m:m ;
}

function format(timeStamp)
{
	//timeStamp是整数，否则要parseInt转换
	var time = new Date(timeStamp);
	var y = time.getFullYear();
	var m = time.getMonth()+1;
	var d = time.getDate()+1;
	var h = time.getHours()+1;
	var mm = time.getMinutes()+1;
	var s = time.getSeconds()+1;
	return y+'-'+add0(m)+'-'+add0(d)+' '+add0(h)+':'+add0(mm)+':'+add0(s);
}

function closeParentDialog(state){
	if(state == '1'){
		alert("密码修改成功，请用新密码重新登录！");
		parent.$("#modify_dialog").dialog('close');
	}else{
		parent.$("#modify_dialog").dialog('close');
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

function changePassword()
{
	
	var maxlength = shangeSelfPasswordVo.maxlength;
	var intensity = shangeSelfPasswordVo.intensity;
	var distinctBefore = shangeSelfPasswordVo.distinctBefore;
	var minlength = shangeSelfPasswordVo.minlength;
	var difference = shangeSelfPasswordVo.difference;
	var oldPassword = shangeSelfPasswordVo.oldPassword;
	var secretLevelName = shangeSelfPasswordVo.secretLevelName;
	
	var oldPassword = $("#oldPassword").val();
	var newPassword = $("#newPassword").val();
	var confirmPassword = $("#confirmPassword").val();
	
	if(!hasText(oldPassword)){
		$.messager.alert('提示',"原密码不能为空",'warning');
		return;
	}
	if(!hasText(newPassword)){
		$.messager.alert('提示',"新密码不能为空",'warning');
		return;
	}
	if(!hasText(confirmPassword)){
		$.messager.alert('提示',"确认密码不能为空",'warning');
		return;
	}
	if(confirmPassword != newPassword){
		$.messager.alert('提示',"确认密码和新密码不一致",'warning');
		return;
	}

	if(oldPassword == newPassword){
		$.messager.alert('提示',"新密码不能和原密码一致",'warning');
		return;
	}


	var level = 0;
	if(minlength!="" && minlength!=null){
		if(newPassword.length < minlength){
		  alert("新密码的长度不能小于"+minlength+"位");
		  return false;
	    }
	}else{
	}
	if(maxlength!="" && maxlength!=null){
		if(newPassword.length > maxlength&&newPassword.length<=100){
		   alert("新密码的长度不能大于"+maxlength+"位");
		   return false;
	    }
	}else{
	}

	level= mate(newPassword);
	if(intensity!="" && intensity !=null && level<intensity){
		 alert("新密码的密码强度要求为"+intensity+",而输入的密码强度为"+level+" .(密码强度其中包含数字，小写字母，大写字母，特殊字符<包含标点符号>，它们各为一级，最高为四级)");
		 return false;
	}
	
	$.ajax({
		url : 'platform/syspassword/sysPasswordController/changePassword',
		data : {
			oldPassword: oldPassword,
			newPassword: newPassword
		},
		type : 'post',
		dataType : 'json',
		success : function(r) {
			
			if(r.result==0)
			{
				if(r.data=="OK")
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
				$.messager.alert('提示','更改密码失败','warning');
			}
		}
	}); 
}












