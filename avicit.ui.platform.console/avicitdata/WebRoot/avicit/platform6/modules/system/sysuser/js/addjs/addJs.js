/**
 * @author zhanglei 添加所用js
 */
function saveForm(){
	/* var dialogId = "${dialogId}"; //当前对话框的ID */
	if ($('#formAddBaseInfo').form('validate') == false) {
		return;
	}
	var loginName =$('#loginName').val();
	if(loginName.length ===  0){
		showMessager("登录名不能为空！");
		return;
	}else if(loginName.length >20){
		showMessager("登录名不能太长！");
		return;
	}
	var no =$('#no').val();
	if(no.length >20){
		showMessager("用户编号不能太长！");
		return;
	}
	var name =$('#name').val();
	if(name.length >20){
		showMessager("用户名称不能太长！");
		return;
	}
	//$('#saveButton').linkbutton('disable');
	var dataVo = $('#formAddBaseInfo').serializeArray();
	var dataJson = convertToJson(dataVo);
	dataVo = JSON.stringify(dataJson);
	$.ajax({
		url : 'platform/sysuser/toSaveUser/1',
		data : {
			datas : JSON.stringify(serializeObject($('#formAddBaseInfo')))
		},
		type : 'post',
		dataType : 'json',
		success : function(result) {
			if (result.flag == "success") {
				if(parent!=null){
					parent.flushData();
					parent.$closeAddIfram();
				}
			}else {
				var msg ='操作失败！';
				if(result.error){
					msg += '原因：'+result.error;
				}
				showMessager(msg);
			}
		}
	});
}
//保存并且继续添加
function saveAddForm(){
	if ($('#formAddBaseInfo').form('validate') == false) {
		return;
	}
	//$('#saveButton').linkbutton('disable');
	var dataVo = $('#formAddBaseInfo').serializeArray();
	var dataJson = convertToJson(dataVo);
	dataVo = JSON.stringify(dataJson);
	$.ajax({
		url : 'platform/sysuser/toSaveUser/3',
		data : {
			datas : JSON.stringify(serializeObject($('#formAddBaseInfo')))
		},
		type : 'post',
		dataType : 'json',
		success : function(result) {
			if (result.flag == "success") {
				var newId = result.newId;
				//alert(newId);
				var doc = document;
				doc.getElementById('signPhoto_sysUserId').value=newId;
				doc.getElementById('headerPhoto_sysUserId').value=newId;
				doc.getElementById('unitCode').value='';
				doc.getElementById('orderBy').value='';
				doc.getElementById('homeTel').value='';
				doc.getElementById('homeZip').value='';
				doc.getElementById('homeAddress').value='';
				doc.getElementById('roomNo').value='';
				doc.getElementById('workSpace').value='';
				doc.getElementById('email').value='';
				doc.getElementById('fax').value='';
				doc.getElementById('officeTel').value='';
				doc.getElementById('mobile').value='';
				doc.getElementById('education').value='';
				doc.getElementById('birthAddress').value='';
				doc.getElementById('title').value='';
//				doc.getElementById('ruleName').value='';
//				doc.getElementById('ruleId').value='';
//				doc.getElementById('positionName').value='';
//				doc.getElementById('positionId').value='';
//				doc.getElementById('deptName').value='';
//				doc.getElementById('deptId').value='';
				doc.getElementById('nameEn').value='';
				doc.getElementById('name').value='';
				doc.getElementById('loginName').value='';
				doc.getElementById('no').value='';
				doc.getElementById('id').value=newId;
				doc.getElementById("remark").InnerText = ""; 
				doc.getElementById('sysUserHeadPhotoImg').src='avicit/platform6/modules/system/sysuser/userPhoto.gif';
				doc.getElementById('sysUserSignPhotoImg').src='avicit/platform6/modules/system/sysuser/userSign.gif';
				parent.flushData();
			}else {
				var msg ='操作失败！';
				if(result.error){
					msg += '原因：'+result.error;
				}
				showMessager(msg);
			}
		}
	});
}
function closeForm(){
	if(parent != null){
		parent.$closeAddIfram();
	}
}
