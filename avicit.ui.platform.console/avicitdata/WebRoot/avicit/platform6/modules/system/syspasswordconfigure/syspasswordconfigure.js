var templateState={};
var sysuser_level={};//用户密级
var editIndex=-1;
$(function() {
	$.ajax({
		url: 'platform/syslookuptype/getLookUpCode/PLATFORM_SYS_TEMPLATE_STATE',
		type :'get',
		dataType :'json',
		success : function(r){
			if(r){
				templateState =	r;
			}
		}
	});
	$.ajax({
		url: 'platform/syslookuptype/getLookUpCode/PLATFORM_USER_SECRET_LEVEL',
		type :'get',
		async:false,
		dataType :'json',
		success : function(r){
			if(r){
				sysuser_level =r;
			}
		}
	});
	$('#userLevelCode').combobox('loadData',sysuser_level);  


	
	$('#passwordDialog').dialog('close');
}); 
//加载成功选择第一条数据
function loadSuccess(data){
	if(data.total>0){
		$('#dgPassword').datagrid('selectRow',0);
		var row = $('#dgPassword').datagrid('getSelected');
		dgPasswordOnClickRow(null,row);
	}
};
function formatUserLevelCode(value){
	if(value ==null ||value == '') return '';
	var l=sysuser_level.length;
	for(;l--;){
		if(sysuser_level[l].lookupCode == value){
			return sysuser_level[l].lookupName;
		}
	}
};

function dgPasswordOnClickRow(index,rowData){
	$('#dgPasswordTemplate').datagrid('options').url="platform/syspasswordtemplate/sysPasswordTemplateController/querySysPasswordTemplet.json";
	$('#dgPasswordTemplate').datagrid('reload', {filter_EQ_templetType: rowData.id});
	$('#dgPasswordTemplate').datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
}

function formatAvailable(value)
{
	if(value ==null ||value == ''){
		return '';
	}
	for(var i =0 ,length = templateState.length; i<length;i++){
		if(templateState[i].lookupCode == value){
			return templateState[i].lookupName;
		}
	}
}

function dgPasswordTemplateOnClickRow(index,rowData)
{
		
	var myDatagrid=$('#dgPasswordTemplate');
	myDatagrid.datagrid('endEdit', editIndex);
	if(editIndex==-1)
	{
		myDatagrid.datagrid('beginEdit', index);  
		editIndex=index;
		
		//选择用户,多选 
		
		var ed = myDatagrid.datagrid('getEditor',{index: index, field: 'templetKey'});
		$(ed.target).attr('disabled', true);
		
		var ed = myDatagrid.datagrid('getEditor',{index: index, field: 'templetKeyDesc'});
		$(ed.target).attr('disabled', true);
		
				
		// 有效无效的下拉框
		var ed = myDatagrid.datagrid('getEditor',{index: index,field: 'templetState'});
		$(ed.target).combobox('loadData', templateState);
				
	   	
	}
	else
	{
		$.messager.alert('提示','不能编辑，请确保上一条数据填写完整','warning');
	}
	
    
}

function dgPasswordTemplateOnAfterEdit(rowIndex, rowData, changes)
{
	//成功完成编辑，包括校验
	editIndex=-1;
} 

function dgPasswordTemplateOnLoadSuccess(data)
{
	if(editIndex != -1){
		$('#dgPasswordTemplate').datagrid('cancelEdit',editIndex);
		editIndex = -1;
	}
}

function addData()
{
	$('#id').val('');
	$('#applicationId').val('');
	$('#key').val('');
	$('#code').val('');
	$('#userLevelCode').val('');
	$('#passwordDialog').dialog({title: '添加'});
	$('#passwordDialog').dialog('open');
}

function editData()
{
	var myDatagrid=$('#dgPassword');
	var rows = myDatagrid.datagrid('getChecked');
	
	if(rows.length!=1)
	{
		$.messager.alert('提示','请选择一个数据','warning');
		return;
	}
	
	$('#id').val(rows[0].id);
	$('#applicationId').val(rows[0].sysApplicationId);
	$('#key').val(rows[0].key);
	$('#code').val(rows[0].code);
	$('#userLevelCode').combobox('setValue', rows[0].userLevelCode);

	$('#passwordDialog').dialog({title: '编辑'});
	$('#passwordDialog').dialog('open');
}

function hideDialog()
{
	$('#passwordDialog').dialog('close');
}

function saveData()
{
	var key =$('#key').val();
	if(key.length ===  0){
		alert("密码模版名称不能为空");
		return;
	}
	if(key.length >100){
		alert("密码模版名称不能太长！");
		return;
	}
	var code = $('#code').val();
	if(code.length ===  0){
		alert("密码模版标识不能为空！");
		return;
	}
	if(code.length >100){
		alert("密码模版标识不能太长！");
		return;
	}
	$.ajax({
		 url:'platform/syspasswordtemplate/sysPasswordTemplateController/saveSysPasswordTempletLevel',
		 data : {data :JSON.stringify(serializeObject($('#passwordEditForm')))},
		 type : 'post',
		 dataType : 'json',
		 success : function(r){
			if(r.result==0){
				$('#dgPassword').datagrid('reload');
				$('#dgPassword').datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
				
				 $.messager.show({
					 title : '提示',
					 msg : '保存成功'
				 });
				 
				 hideDialog();
			 }else{
				 $.messager.alert('提示',r.msg,'warning');
			 } 
		 }
	 });
}

/*删除选中数据*/
function deleteData(){
  
	var myDatagrid=$('#dgPassword');
	
	var rows = myDatagrid.datagrid('getChecked');
	var ids=[];
	
	if (rows.length > 0) {
		$.messager.confirm('请确认', '您确定要删除当前所选的数据？', function(b) {
			if (b) {
				
				for(var i=0;i<rows.length;i++)
					ids.push(rows[i].id);
				
				$.ajax({
					url : 'platform/syspasswordtemplate/sysPasswordTemplateController/deleteSysPasswordTempletLevel',
					data : {
						ids : ids.join(',')
					},
					type : 'post',
					dataType : 'json',
					success : function(r) {
						
						if(r.result==0){
							
							$('#dgPassword').datagrid('reload');
							$('#dgPassword').datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
							
							$('#dgPasswordTemplate').datagrid('loadData',[]);
							
							$.messager.show({
								title : '提示',
								msg : '删除成功！'
							});
						}
						else
						{
							$.messager.alert('提示','删除失败','warning');
						}
					}
				}); 
			}
		});
	} else {
		$.messager.alert('提示', '请选择要删除的记录！', 'warning');
	}
}

function savePasswordTemplate()
{
	var myDatagrid=$('#dgPasswordTemplate');
	myDatagrid.datagrid('endEdit',editIndex);
	
	if(editIndex!=-1)
	{
		$.messager.alert('提示','不能保存，请确保上一条数据填写完整','warning');
		return;
	}
	
	var rows = myDatagrid.datagrid('getChanges');
	var data =JSON.stringify(rows);
	
	$.ajax({
		 url:'platform/syspasswordtemplate/sysPasswordTemplateController/saveSysPasswordTemplet',
		 data : { datas: data },
		 type : 'post',
		 dataType : 'json',
		 success : function(r){
			if(r.result==0){
				 
				$('#dgPasswordTemplate').datagrid('reload');
				$('#dgPasswordTemplate').datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
				
				 $.messager.show({
					 title : '提示',
					 msg : '保存成功'
				 });
				 
				 
			 }else{
				 $.messager.alert('提示',r.msg,'warning');
			 } 
		 }
	 });
}




































