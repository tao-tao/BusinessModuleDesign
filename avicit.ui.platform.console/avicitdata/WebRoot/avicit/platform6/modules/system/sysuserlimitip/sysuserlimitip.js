/**
 * @author wangyi
 */

var userLimitCombo=null;
var ipLimitCombo=null;

var editIndex=-1;
var newRowCount=0;

var currentCodeBox;
var currentIpBox;
/**
 * 初始化combo数据
 */
$(function(){
	
	$.ajax({
		url: 'platform/sysUserRelationController/getComboData.json',
		data : {'lookupType' : 'PLATFORM_USER_LIMIT_IP_TYPE'},
		type :'POST',
		dataType :'json',
		success : function(r){
			if(r.lookup){
				ipLimitCombo=	r.lookup;
				
				$('#filter_EQ_limitTypeIpType').combobox('loadData', [{lookupCode:'', lookupName:'所有'}].concat( ipLimitCombo ));
			}
		}
	});
	
	$.ajax({
		url: 'platform/sysUserRelationController/getComboData.json',
		data : {'lookupType' : 'PLATFORM_USER_LIMTI_TYPE'},
		type :'POST',
		dataType :'json',
		success : function(r) {
			if(r.lookup){
				userLimitCombo  =	r.lookup;
				
				$('#filter_EQ_limitUserType').combobox('loadData', [{lookupCode:'', lookupName:'所有'}].concat(userLimitCombo));
			}
		}
	});
	
	$('#userLimitSearchDialog').dialog("close");
	
	$('#filter_LIKE_userLimitUserName').attr('disabled', true);
   	var commonSelector = new CommonSelector("user","userSelectCommonDialog", "filter_EQ_userLimitUserId" ,"filter_LIKE_userLimitUserName",null,null);
   	commonSelector.init(null,null,'n'); //选择人员  回填部门 */
	
});

$.extend($.fn.validatebox.defaults.rules, {
	ip: {// 验证IP地址
        validator: function (value) {
            return /^\d+\.\d+\.\d+\.\d+$/.test(value);
        },
        message: 'IP地址格式不正确'
    }
});



function formatIpLimit(value)
{
	if(value ==null ||value == ''){
		return '';
	}
	for(var i =0 ,length = ipLimitCombo.length; i<length;i++){
		if(ipLimitCombo[i].lookupCode == value){
			return ipLimitCombo[i].lookupName;
		}
	}
}

function formatUserLimit(value)
{
	if(value ==null ||value == ''){
		return '';
	}
	for(var i =0 ,length = userLimitCombo.length; i<length;i++){
		if(userLimitCombo[i].lookupCode == value){
			return userLimitCombo[i].lookupName;
		}
	}
}

function addData()
{
	newRowCount++;	
	var myDatagrid=$('#dgUserLimit');
	
	myDatagrid.datagrid('endEdit',editIndex);
	if(editIndex != -1){
		$.messager.alert('提示','不能添加，请确保上一条数据填写完整','warning');
		return;
	}
	
	myDatagrid.datagrid('insertRow',{
		index: 0,
		row:{id:""}
		});	
	myDatagrid.datagrid('selectRow', 0).datagrid('beginEdit',0);
	editIndex=0;
	
	
	// IP类型的下拉框
	var ed = myDatagrid.datagrid('getEditor',{index: editIndex, field: 'limitTypeIpType'});
	//$(ed.target).combobox('loadData', ipLimitCombo);
	currentCodeBox=$(ed.target);
	
	// 这部分是由于onselect的副作用，造成了value的丢失和width的变化，而必须额外编写的程序
	var value=currentCodeBox.combobox('getValue');
	currentCodeBox.combobox('loadData', ipLimitCombo);
	currentCodeBox.combobox({onSelect:ipLimitSelect});
	currentCodeBox.combobox({width:currentCodeBox.parent().width()});
	currentCodeBox.combobox('setValue', value);
	
	var ed = myDatagrid.datagrid('getEditor',{index: editIndex, field: 'userLimitIpEnd'});
	currentIpBox = $(ed.target);
			
	if(value==0)
	{
		// ip点，只有ipfrom是可以编辑的，ipto设置无效
		currentIpBox.attr('disabled', true);
	}
	else
	{
		// ip段，ipto设置有效
		currentIpBox.attr('disabled', false);
	}
	
	// 用户访问类型的下拉框
	var ed = myDatagrid.datagrid('getEditor',{index: editIndex, field: 'limitUserType'});
	$(ed.target).combobox('loadData', userLimitCombo);
	
	//选择人员
	
	var ed = myDatagrid.datagrid('getEditor',{index: editIndex, field: 'userLimitUserName'});
	$(ed.target).attr('id', "receptUserName"+newRowCount);
	$(ed.target).attr('disabled', true);
	
	var ed = myDatagrid.datagrid('getEditor',{index: editIndex, field: 'userLimitUserId'});
	$(ed.target).attr('id', "receptUserId"+newRowCount);
	//$(ed.target).attr('disabled', true);  
	
   	var commonSelector = new CommonSelector("user","userSelectCommonDialog", "receptUserId"+newRowCount ,"receptUserName"+newRowCount,null,null);
   	commonSelector.init(null,null,'n'); //选择人员  回填部门 */
}

function dgUserLimitOnClickRow(index,rowData)
{
	newRowCount++;
	
	var myDatagrid=$('#dgUserLimit');
	
	myDatagrid.datagrid('endEdit', editIndex);
	if(editIndex==-1)
	{
		myDatagrid.datagrid('beginEdit', index);  
		editIndex=index;
		
		// IP类型的下拉框
		var ed = myDatagrid.datagrid('getEditor',{index: index, field: 'limitTypeIpType'});
		//$(ed.target).combobox('loadData', ipLimitCombo);
		currentCodeBox=$(ed.target);
		
		// 这部分是由于onselect的副作用，造成了value的丢失和width的变化，而必须额外编写的程序
		var value=currentCodeBox.combobox('getValue');
		currentCodeBox.combobox('loadData', ipLimitCombo);
		currentCodeBox.combobox({onSelect:ipLimitSelect});
		currentCodeBox.combobox({width:currentCodeBox.parent().width()});
		currentCodeBox.combobox('setValue', value);
		
		var ed = myDatagrid.datagrid('getEditor',{index: index, field: 'userLimitIpEnd'});
		currentIpBox = $(ed.target);
				
		if(value==0)
		{
			// ip点，只有ipfrom是可以编辑的，ipto设置无效
			currentIpBox.attr('disabled', true);
		}
		else
		{
			// ip段，ipto设置有效
			currentIpBox.attr('disabled', false);
		}
		
		// 用户访问类型的下拉框
		var ed = myDatagrid.datagrid('getEditor',{index: index, field: 'limitUserType'});
		$(ed.target).combobox('loadData', userLimitCombo);
		
		//选择人员
		
		var ed = myDatagrid.datagrid('getEditor',{index: index, field: 'userLimitUserName'});
		$(ed.target).attr('id', "receptUserName"+newRowCount);
		$(ed.target).attr('disabled', true);
		
		var ed = myDatagrid.datagrid('getEditor',{index: index, field: 'userLimitUserId'});
		$(ed.target).attr('id', "receptUserId"+newRowCount);
		//$(ed.target).attr('disabled', true);  
		
	   	var commonSelector = new CommonSelector("user","userSelectCommonDialog", "receptUserId"+newRowCount ,"receptUserName"+newRowCount,null,null);
	   	commonSelector.init(null,null,'n'); //选择人员  回填部门 */
	   	
	   		
		
	}
	else
	{
		$.messager.alert('提示','不能编辑，请确保上一条数据填写完整','warning');
	} 
	
    
}

function ipLimitSelect(record)
{
	
	var value=currentCodeBox.combobox('getValue');
	//alert(value);
	if(value==0)
	{
		// ip点，只有ipfrom是可以编辑的，ipto设置无效
		currentIpBox.attr('disabled', true);
	}
	else
	{
		// ip段，ipto设置有效
		currentIpBox.attr('disabled', false);
	}
}

function dgUserLimitOnAfterEdit(rowIndex, rowData, changes)
{
	//成功完成编辑，包括校验
	editIndex=-1; 
} 

function dgUserLimitOnLoadSuccess(data)
{
	if(editIndex != -1){
		$('#dgUserLimit').datagrid('cancelEdit',editIndex);
		editIndex = -1;
	} 
}

function saveData()
{
	var myDatagrid=$('#dgUserLimit');
	myDatagrid.datagrid('endEdit',editIndex);
	
	if(editIndex!=-1)
	{
		$.messager.alert('提示','不能保存，请确保上一条数据填写完整','warning');
		return;
	}
	
	var rows = myDatagrid.datagrid('getChanges');
	var data =JSON.stringify(rows);
	if(rows.length > 0)
	{
		 $.ajax({
			 url:'platform/sysuserlimitip/sysUserLimitIpController/saveOrUpdateSysUserLimitIps',
			 data : {datas : data},
			 type : 'post',
			 dataType : 'json',
			 success : function(r){
				if(r.result==0){
					 myDatagrid.datagrid('reload',{});
					 myDatagrid.datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
					 $.messager.show({
						 title : '提示',
						 msg : '保存成功'
					 });
				 }else{
					 $.messager.alert('提示','保存失败','warning');
				 } 
			 }
		 });
	 } 
}

function deleteData()
{
	var myDatagrid=$('#dgUserLimit');
	
	var rows = myDatagrid.datagrid('getChecked');
	var ids=[];
	
	
	if (rows.length > 0) {
		$.messager.confirm('请确认', '您确定要删除当前所选的数据？', function(b) {
			if (b) {
				
				for(var i=0;i<rows.length;i++)
					ids.push(rows[i].id);
				
				$.ajax({
					url : 'platform/sysuserlimitip/sysUserLimitIpController/deleteSysUserLimitIps',
					data : {
						ids : ids.join(",")
					},
					type : 'post',
					dataType : 'json',
					success : function(r) {
						
						if(r.result==0){
							myDatagrid.datagrid('reload', {});//刷新当前页
							myDatagrid.datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
							editIndex = -1;
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

function showSearchDialog()
{
	$('#userLimitSearchDialog').dialog("open");
	
	
	
}


function hideSearchDialog()
{
	$('#userLimitSearchDialog').dialog('close');
}

function searchData()
{
	
		
	var userLimitUserId= $('#filter_EQ_userLimitUserId').val();
	var limitTypeIpType= $('#filter_EQ_limitTypeIpType').combobox('getValue');
	var limitUserType= $('#filter_EQ_limitUserType').combobox('getValue');
	
	//alert(searchName+","+validFlag);
	
	loadSearchInfo(userLimitUserId, limitTypeIpType, limitUserType);
}

function clearSearchData()
{
	$('#filter_EQ_userLimitUserId').val('');
	$('#filter_LIKE_userLimitUserName').val('');
	$('#filter_EQ_limitTypeIpType').combobox('setValue', '');
	$('#filter_EQ_limitUserType').combobox('setValue', '');
	
	
}

function loadSearchInfo(userLimitUserId, limitTypeIpType, limitUserType)
{
	
	$("#dgUserLimit").datagrid("options").url ="platform/sysuserlimitip/sysUserLimitIpController/getSysUserLimitIpListByPage.json";
	$('#dgUserLimit').datagrid("reload", {filter_EQ_userLimitUserId: userLimitUserId, filter_EQ_limitTypeIpType: limitTypeIpType, filter_EQ_limitUserType: limitUserType});
	$("#dgUserLimit").datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');	
	
}



