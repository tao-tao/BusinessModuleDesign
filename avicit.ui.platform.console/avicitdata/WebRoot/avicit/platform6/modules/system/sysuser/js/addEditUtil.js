/**
 * @author zhanglei 添加页和编辑页公用的js
 */


//通用消息弹出框
function showMessager(msg){
	var scroll =-document.body.scrollTop-document.documentElement.scrollTop;
	/*var isIE=!!window.ActiveXObject;
	var isIE6=isIE&&!window.XMLHttpRequest;
	//alert(document.documentElement.scrollTop);
	if(isIE6){
		//scroll+=129;
	}*/
	/*alert(document.body.scrollTop);
	alert(document.documentElement.scrollTop);
	alert(-document.body.scrollTop-document.documentElement.scrollTop+19);*/
	$.messager.show({
		title : '提示',
		msg : msg,
		timeout:2000,  
        showType:'slide',
        style:{
        	left:'',
        	right:0,
        	top:'',
            bottom:scroll
        }  
	});
}

//选择岗位
function mySelectPos(){
	var userSelector = new CommonSelector("position","positionSelectCommonDialog","positionId","positionName",null,null,null,null,null,null,600,400);
	userSelector.init(false,"selectPosDialogCallBack");
}
//岗位选择回调
function selectPosDialogCallBack(data){
	$('#positionId').val(data[0].id);
	$('#positionName').val(data[0].positionName);
}
//选择角色
function mySelectRole(){
	var userSelector = new CommonSelector("role","roleSelectCommonDialog","ruleId","ruleName",null,null,false,null,null,null,600,400);
	userSelector.init(false,"selectRoleDialogCallBack");
}
//角色选择回调
function selectRoleDialogCallBack(data){
	$('#ruleId').val(data[0].id);
	$('#ruleName').val(data[0].roleName);
}
//选择部门
function mySelectDept(){
	var deptSelector = new CommonSelector("dept","deptSelectCommonDialog","deptId","deptName",null,null,null,null,null,null,600,400);
	deptSelector.init(false,"selectDeptDialogCallBack",'n');
}
//部门选择回调
function selectDeptDialogCallBack(data){
	$('#deptId').val(data[0].deptId);
	$('#deptName').val(data[0].deptName);
}
//打开用户头像选择
function chooseUserPhoto(){
	 $('#addUserPhotoDialog').dialog('open',true);
}
//打开用户签名选择
function chooseUserSign(){
	 $('#addUserSignDialog').dialog('open',true);
}
//关闭用户头像上传框
function closeUpLoadUserPhoto(){
	 $('#addUserPhotoDialog').dialog('close',true);
}
//关闭签名上传框
function closeUpLoadUserSign(){
	$('#addUserSignDialog').dialog('close',true);
}
//上传头像
function upLoadUserPhoto(userId){
	userId = userId||sysUserId;
	if(document.getElementById("sysUserPhoto").value != ''){
		if(checkfiletype('sysUserPhoto')){
			$.messager.progress();	// 显示进度条
			$('#uploadForm').form('submit', {
				url: 'platform/sysuser/photo/upload/headerphoto?userId='+userId,
				success: function(){
					$.messager.progress('close');	// 如果提交成功则隐藏进度条
					$.messager.alert('提示','头像文件上传成功!','info',function(r){
						$('#addUserPhotoDialog').dialog('close',true);
						document.getElementById("sysUserHeadPhotoImg").src="platform/sysuser/photo/upload/headerphoto?sysUserId="+userId+"&o=" + Math.random();
					});
				}
			});
			return;
		}
	} else {
		$.messager.alert('警告','请选择要上传的头像文件!','warning');
		return;
	}
}
//上传签名
function upLoadUserSign(userId){
	userId = userId||sysUserId;
	if(document.getElementById("sysUserSign").value != ''){
		if(checkfiletype('sysUserSign')){
			$.messager.progress();	// 显示进度条
			$('#uploadPersionSignForm').form('submit', {
				url: 'platform/sysuser/photo/upload/signphoto?userId='+userId,
				success: function(){
					$.messager.progress('close');	// 如果提交成功则隐藏进度条
					$.messager.alert('提示','签名文件上传成功!','info',function(r){
						$('#addUserSignDialog').dialog('close',true);
						document.getElementById("sysUserSignPhotoImg").src="platform/sysuser/photo/upload/signphoto?sysUserId="+userId+"&o=" + Math.random();
					});
				}
			});
			return;
		}
	} else {
		$.messager.alert('警告','请选择要上传的头像文件!','warning');
		return;
	}
}

//检查上传类型
function checkfiletype(id){
    var fileName = document.getElementById(id).value;
    //设置文件类型数组
    var extArray =[".jpg",".png",".gif",".bmp"];
   	//获取文件名称
   	while (fileName.indexOf("//") != -1)
    	fileName = fileName.slice(fileName.indexOf("//") + 1);
       	//获取文件扩展名
       	var ext = fileName.slice(fileName.indexOf(".")).toLowerCase();
   		//遍历文件类型
       	var count = extArray.length;
   		for (;count--;){
     		if (extArray[count] == ext){ 
       			return true;
     		}
   		}  
   		$.messager.alert('错误','只能上传下列类型的文件: '  + extArray.join(" "),'error');
   return false;  
}
//进入用户代码框验证规则
function checkInUserCode(myObjin){
	$(myObjin).validatebox({ 
		    validType : "def['userCode']"
	 });
}
//离开用户代码框验证规则
function checkUserCode(myObj,userId){
	$(myObj).validatebox({ 
	    validType : "unique['platform/sysuser/checkUserCode?sysUserId="+userId+"','userCode']"
	});
	
}
//进入用户登录名框验证规则
function checkInLoginName(myObjin){
	$(myObjin).validatebox({ 
		    validType : "def['loginName']"
	 });
}
//离开用户登录名框验证规则
function checkLoginName(myObj,userId){
	$(myObj).validatebox({ 
	    validType : "unique['platform/sysuser/checkLoginName?sysUserId="+userId+"','loginName']"
	});
	
}
//进入集团统一代码框验证规则
function checkInUnitCode(myObjin){
	$(myObjin).validatebox({ 
		    validType : "def['unitCode']"
	 });
}
//离开集团统一代码框验证规则
function checkUnitCode(myObj,userId){
	$(myObj).validatebox({ 
	    validType : "unique['platform/sysuser/checkUnitCode?sysUserId="+userId+"','unitCode']"
	});
}
	
