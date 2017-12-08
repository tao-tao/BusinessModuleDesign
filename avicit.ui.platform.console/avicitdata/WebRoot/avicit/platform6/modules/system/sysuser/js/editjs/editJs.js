/**
 * @author zhanglei 编辑所用js
 */

function changeTabs(title,index,userId){
	//兼职部门
	if(index == 1){
		$("#divDept").css('height',document.body.clientHeight - 95);
		$('#saveButton').linkbutton('disable');
		if(!dgMapDept){
			dgMapDept = $('#dgMapDept').datagrid({
				queryParams : {id : userId},
				onClickCell: onClickCellDept
			});
		}
	}else if(index == 2){
		$("#divRole").css('height',document.body.clientHeight - 95);
		$('#saveButton').linkbutton('disable');
		if(!dgMapUserRole){
			$.ajax({
				url: 'platform/sysuser/getRoleCode.json',
				data :{},
				type :'POST',
				dataType :'json',
				success : function(r){
					if(r.roleType){
						roleType =	r.roleType;
					}
					if(r.validFlag){
						roleValFlag=r.validFlag;
					}
				}
			});
			dgMapUserRole = $('#dgMapUserRole').datagrid({
				queryParams : {userId : userId},
				onClickCell: onClickCellRole
			});
		}
	}else{
		$('#saveButton').linkbutton('enable');
	}
};
function closeForm(){
	if(parent != null){
		parent.flushData();
		parent.$closeEditIfram();
	}
};
function saveForm(){
	if ($('#formEditBaseInfo').form('validate') == false) {
		return;
	}
	$('#saveButton').linkbutton('disable');
	var dataVo = $('#formEditBaseInfo').serializeArray();
	var dataJson = convertToJson(dataVo);
	dataVo = JSON.stringify(dataJson);
	$.ajax({
		url : 'platform/sysuser/toSaveUser/2',
		data : {
			datas : JSON.stringify(serializeObject($('#formEditBaseInfo')))
		},
		type : 'post',
		dataType : 'json',
		success : function(result) {
			if (result.flag == "success") {
				showMessager("保存成功!");
				//保存完毕后刷新数据
				if(parent != null){
					parent.flushData();
				}
			}else {
				showMessager("保存失败!"+result.error);
			}
			$('#saveButton').linkbutton('enable');
		}
	});
};

/***************部门映射js********************/
$.extend($.fn.datagrid.methods, {
   editCell: function(jq,param){
       return jq.each(function(){
       var fields = $(this).datagrid('getColumnFields',true).concat($(this).datagrid('getColumnFields'));
       var l=fields.length;
       for(;l--;){
           var col = $(this).datagrid('getColumnOption', fields[l]);
           col.editor1 = col.editor;
           if (fields[l] != param.field && fields[l]!='deptId' && fields[l] !='positionId' ){
        		   col.editor = null; 
           }
       }
       $(this).datagrid('beginEdit', param.index);
       l=fields.length;
       for(;l--;){
          var col = $(this).datagrid('getColumnOption', fields[l]);
          col.editor = col.editor1;
       }
           });
       }
	
   });
//新增兼职部门
function addMapDept(){
	//首先结束编辑行
	endEditing();
	dgMapDept.datagrid('insertRow',{
		index: 0,
		row:{id:"",deptId:'',deptName:'',positionId:'',positionName:'',validFlag:0,isChiefDept:0,isManager:0}
		});	
};
/**
 *保存部门关系表
 */
function saveMapDept(id){
	var rows = dgMapDept.datagrid('getChanges');
	
	var l = rows.length;
	var length=l;
	var row;
	for(;l--;){
		row =rows[l];
		if(!row.deptName){
			$.messager.show({
				 title : '提示',
				 msg : '第'+(length-l)+'条数据中,部门名称不能为空!'
			 });
			return false;
		}
		if(!row.positionName){
			$.messager.show({
				 title : '提示',
				 msg : '第'+(length-l)+'条数据中,岗位名称不能为空!'
			 });
			return false;
		}
	}
	
	var data =JSON.stringify(rows);
	if(rows.length > 0){
		 $.ajax({
			 url:'platform/sysuser/saveMapDept',
			 data : {datas : data,userId:id},
			 type : 'post',
			 dataType : 'json',
			 success : function(r){
				 if (r.flag == "success"){
					 	reloadMapDept(id);
					 	if(r.deptId){
					 		$('#deptId').val(r.deptId);
						 	$('#deptName').val(r.deptName);
						 	$('#positonId').val(r.positonId);
						 	$('#positionName').val(r.positionName);
						 	if(r.isManager == "1"){
						 		$("input[name='isManager'][value=1]").attr("checked",true);  
						 	}else{
						 		$("input[name='isManager'][value=0]").attr("checked",true); 
						 	}	
					 	}
					 	showMessager("保存成功！");
				 }else{
					 if(r.error){
						 //$.messager.alert('提示','保存失败,'+r.error,'warning');
						 $.messager.alert('提示',r.error,'warning');
					 }else{
						 showMessager("保存失败，用户不存在!");
					 }
					 
				 } 
			 }
		 });
	 }else{
		 showMessager("没有要提交的数据！");
	 } 
	//var aa= dgMapDept.datagrid('validateRow', 0);
	//alert(aa);
	/* dgMapDept.datagrid('endEdit',0);
	var dd = dgMapDept.datagrid('getChanges');
	alert(dd.length);
	alert(dgMapDept.datagrid('getRows')[0].isManager); */
};
/**
 *删除用户部门关系记录
 */
function deleteMapDept(userId) {
	var rows = dgMapDept.datagrid('getChecked');
	var ids = [];
	var l=rows.length;
	if (rows.length > 0) {
		$.messager.confirm('请确认','您确定要删除当前所选的数据？',
			function(b){
				if(b){
					for (;l--;) {
						ids.push(rows[l].id);
					}
					$.ajax({
						url : 'platform/sysuser/deleteMapDept',
						data : {
							ids : ids.join(',')
						},
						type : 'post',
						dataType : 'json',
						success : function(result) {
							if (result.flag == "success") {
								reloadMapDept(userId);
								showMessager("删除成功!");
							}
						}
					});
				}
			});
	} else {
		$.messager.alert('提示', '请选择要删除的记录！', 'warning');
	}
};
/**
 *重新刷新用户部门关系映射数据
 */
function reloadMapDept(userId){
 	dgMapDept.datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
 	dgMapDept.datagrid('reload',{id : userId});
};
//结束部门编辑
function endEditing(){
    if (editIndex == undefined){return true;}
    //if (dgMapDept.datagrid('validateRow', editIndex)){
   	 dgMapDept.datagrid('endEdit', editIndex);
        editIndex = undefined;
        return true;
    //} else {
      //  return false;
    //}
};
/*部门选择后回调*/
function callBackDept(index){
	dgMapDept.datagrid('beginEdit', index);
	var ed = dgMapDept.datagrid('getEditor', {index:index,field:'validFlag'});
	$(ed.target).val(1);
	dgMapDept.datagrid('endEdit', index);
};
//更改是否主管
function changeMgr(index,field,value){
	 	endEditing();
		var row = dgMapDept.datagrid('getRows')[index];
		var value =(parseInt(row.isManager)+1)&1;
		dgMapDept.datagrid('beginEdit', index);
		var ed = dgMapDept.datagrid('getEditor', {index:index,field:'isManager'});
		$(ed.target).val(value);
		dgMapDept.datagrid('endEdit', index);
};
//更改是否主部门
function changeIsChiefDept(index,field,value){
   endEditing();
	var rows =dgMapDept.datagrid('getRows');
	var count = rows.length;
	var valueNew = (parseInt(value)+1)&1;
	for(;count--;){
		if(rows[count].isChiefDept == 1){
			dgMapDept.datagrid('beginEdit', count);
			var ed = dgMapDept.datagrid('getEditor', {index:count,field:field});
			$(ed.target).val(0);
			dgMapDept.datagrid('endEdit', count);
			break;
		}
	} 
	dgMapDept.datagrid('beginEdit', index);
	var ed = dgMapDept.datagrid('getEditor', {index:index,field:'isChiefDept'});
	$(ed.target).val(valueNew);
	dgMapDept.datagrid('endEdit', index);
};
//编辑岗位和部门
function editDeptPos(index, field,value){
	 if (endEditing()){
   	 dgMapDept.datagrid('selectRow', index).datagrid('editCell', {index:index,field:field});
   	 editIndex = index;
	 }
};
function onClickCellDept(index, field,value){
	 switch(field){
	 	case 'isManager':
	 		changeMgr(index,field,value);
	 		return false;
	 	case 'validFlag' :
	 		return false;
	 	case 'isChiefDept':
	 		 changeIsChiefDept(index,field,value);
	 		 break;
	 	default :
	 		editDeptPos(index, field,value);
	 }
};
/*格式化datagrid 是否主管 */
function formateManeger(value,row,index){
	var flag = row.isManager == 0?'':'checked="true"';
	return '<input type="checkbox" '+flag+'/>';
};
/*格式化datagrid 是否主部门*/
function formateChiefDept(value,row,index){
	var flag = row.isChiefDept == 0?'':'checked="true"';
	return '<input type="checkbox"'+flag+'/>';
};
/*格式化datagrid有效标识*/
function formateValidFlag(value,row,index){
	return value == 1?'有效':'无效';
};
/***************角色映射js********************/
/*格式化角色类型*/
function formateRoleType(value,row,index){
	var l=roleType.length;
	for(;l--;){
		if(roleType[l].lookupCode == value){
			return roleType[l].lookupName;
		}
	}
};
//格式化角色有效
function formateRoleFlag(value,row,index){
	var l=roleValFlag.length;
	for(;l--;){
		if(roleValFlag[l].lookupCode == value){
			return roleValFlag[l].lookupName;
		}
	}
};
//新增兼职角色
function addMapRole(){
	//首先结束编辑行
	endRoleEditing();
	dgMapUserRole.datagrid('insertRow',{
		index: 0,
		row:{id:+new Date(),roleId:'',roleName:'',roleName:'',validFlag:''}
		});	
};
//保存用户角色关系
function saveMapUserRole(userId){
	var rows = dgMapUserRole.datagrid('getChanges');
	var ids = [];
	var count = rows.length;
	if(count > 0){
		/* for (;count--;) {
			ids.push(rows[count].sysRoleId);
		 }*/
		 $.ajax({
			 url:'platform/sysuser/saveMapRole',
			 data : {datas : JSON.stringify(rows),userId:userId},
			 type : 'post',
			 dataType : 'json',
			 success : function(r){
				 if (r.flag == "success"){
					 	reloadMapRole(userId);
					 	showMessager("保存成功！");
				 }else{
					 if(r.error){
						 $.messager.alert('提示','保存失败,'+r.error,'warning');
					 }else{
						 showMessager("保存失败，用户不存在！");
					 }
					 
				 }
			 }
		 });
	 }else{
		 showMessager("没有要提交的数据！");
	 } 
};
/*角色选择后回调*/
function callBackRole(index,data){
	dgMapUserRole.datagrid('beginEdit', index);
	var ed = dgMapUserRole.datagrid('getEditor', {index:index,field:'roleName'});
	$(ed.target).children("input").val(data[0].roleName);
	dgMapUserRole.datagrid('endEdit', index);//此处必须结束编辑并且重新开启，因为选人框是oncellclick点击开的，其他cell的editor为null
	dgMapUserRole.datagrid('beginEdit', index);
	ed = dgMapUserRole.datagrid('getEditor', {index:index,field:'roleType'});
	$(ed.target).val(data[0].roleType);
	ed = dgMapUserRole.datagrid('getEditor', {index:index,field:'sysRoleId'});
	$(ed.target).val(data[0].id);
	ed = dgMapUserRole.datagrid('getEditor', {index:index,field:'validFlag'});
	$(ed.target).val(data[0].validFlag);
	dgMapUserRole.datagrid('endEdit', index);
};
/*重新刷新用户角色关系数据*/
function reloadMapRole(userId){
	dgMapUserRole.datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
	dgMapUserRole.datagrid('reload',{userId : userId});
};
/*删除用户角色关系记录*/
function deleteMapUserRole(userId){
	var rows = dgMapUserRole.datagrid('getChecked');
	var total = dgMapUserRole.datagrid('getRows').length;
	var count=rows.length;
	if(count == total){
		$.messager.alert('提示', '至少保留一条用户角色信息', 'warning');
		return false;
	}
	var ids = [];
	if (count > 0) {
		$.messager.confirm('请确认','您确定要删除当前所选的数据？',
			function(b){
				if(b){
					for(;count--;) {
						ids.push(rows[count].sysRoleId);
					}
					$.ajax({
						url : 'platform/sysuser/deleteMapRole',
						data : {
							ids : ids.join(','),userId:userId
						},
						type : 'post',
						dataType : 'json',
						success : function(result) {
							 if (result.flag == "success") {
								reloadMapRole(userId);
								showMessager("删除成功!");
							}else{
								showMessager("保存失败,"+result.error);
							}
						}
					});
				}
			});
	} else {
		$.messager.alert('提示', '请选择要删除的记录！', 'warning');
	}
};
//结束角色编辑
function endRoleEditing(){
    if (editIndexRole == undefined){return true;}
   	 dgMapUserRole.datagrid('endEdit', editIndexRole);
   	 editIndexRole = undefined;
       return true;
};
//角色单元格点击
function onClickCellRole(index,field){
	 if(field == 'roleType' || field == 'validFlag'){
		 return false;
	 }
    if (endRoleEditing()){
   	 dgMapUserRole.datagrid('selectRow', index).datagrid('editCell', {index:index,field:field});
   	 editIndexRole = index;
    }
};