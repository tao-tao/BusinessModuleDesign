/**
 * @author wangyi
 */

var editIndex=-1;
var editIndex2=-1;
var editIndex3=-1;
var newRowCount=0;

var menuStyle;

$(function(){ 
	$("#tt").tabs({
		width:$("#tt").parent().width(),
		height:$("#tt").parent().height()
	});

	$.ajax({
		url : 'platform/syscustomed/sysCustomedController/getSystermMainPageMenuStyle',
		data : {
			lookupType: "PLATFORM_MENU_STYLE"
			
		},
		type : 'post',
		dataType : 'json',
		success : function(r) {
			
			if(r.result==0){
				
				//$.messager.alert('提示','首页菜单样式设置成功！重新登录即可生效。','warning');
				menuStyle=r.data;
				
				generateMenuStyleTable(menuStyle);
			}
			else
			{
				$.messager.alert('提示','获取菜单风格列表失败','warning');
			}
		}
	}); 
	
}); 

function generateMenuStyleTable(menuStyle)
{
	var topTr=$('#menuStyle tr')[0];
	
	for(var i=0;i<menuStyle.length;i++)
	{
		var topTd=$("<td></td>");
		
		var table=$("<table style='MARGIN-RIGHT: auto; MARGIN-LEFT: auto;'></table>");
						
		var tr = $("<tr><td><img src='"+menuStyle[i].menuImageUrl+"'></img></td></tr>");
		
		var tr2;
		if(menuStyle[i].isDefault=='1')
			tr2 = $("<tr><td><input type='radio' checked='checked' name='layout' onclick='saveMenuStyle(\""+
				menuStyle[i].menuStyleUrl+"\");'>"+menuStyle[i].menuStyleName+"</input></td></tr>");
		else
			tr2 = $("<tr><td><input type='radio' name='layout' onclick='saveMenuStyle(\""+
					menuStyle[i].menuStyleUrl+"\");'>"+menuStyle[i].menuStyleName+"</input></td></tr>");
		
		tr.appendTo(table);
		tr2.appendTo(table);
		table.appendTo(topTd);
		topTd.appendTo(topTr);
	
	}
	
	var topTd=$("<td></td>");
	var table=$("<table style='MARGIN-RIGHT: auto; MARGIN-LEFT: auto;'></table>");
	var tr = $("<tr><td><input type='button' onclick='saveMenuStyle(null, true);' value='清空' /></td></tr>");
	tr.appendTo(table);
	table.appendTo(topTd);
	topTd.appendTo(topTr);
	
}


function dgGroupOnClickRow(index,rowData)
{
	newRowCount++;
	
	var myDatagrid=$('#dgGroup');
	myDatagrid.datagrid('endEdit', editIndex);
	if(editIndex==-1)
	{
		myDatagrid.datagrid('beginEdit', index);  
		editIndex=index;
		
		//选择用户,多选 
		
		var ed = myDatagrid.datagrid('getEditor',{index: editIndex, field: 'sysUserName'});
		$(ed.target).attr('id', "receptUserName"+newRowCount);
		$(ed.target).attr('disabled', true);
		
		var ed = myDatagrid.datagrid('getEditor',{index: editIndex, field: 'sysUserId'});
		$(ed.target).attr('id', "receptUserId"+newRowCount);
		//$(ed.target).attr('disabled', true);
		
		
	   	var commonSelector = new CommonSelector("user","userSelectCommonDialog", "receptUserId"+newRowCount ,"receptUserName"+newRowCount,null,null, null, false);
	   	commonSelector.init(null,null,'n'); //选择人员  回填部门 
	}
	else
	{
		$.messager.alert('提示','不能编辑，请确保上一条数据填写完整','warning');
	}
	
    
}

function dgGroupOnAfterEdit(rowIndex, rowData, changes)
{
	//成功完成编辑，包括校验
	editIndex=-1;
} 

function dgGroupOnLoadSuccess(data)
{
	if(editIndex != -1){
		$('#dgGroup').datagrid('cancelEdit',editIndex);
		editIndex = -1;
	}
}

/*增加一条记录*/

function addGroup()
{
	newRowCount++;	
	
	var myDatagrid=$('#dgGroup');
	myDatagrid.datagrid('endEdit',editIndex);
	if(editIndex != -1){
		$.messager.alert('提示','不能添加，请确保上一条数据填写完整','warning');
		return;
	}
	myDatagrid.datagrid('insertRow',{
		index: 0,
		row:{id: "", key: "PLATFORM_PERSONALGROUP"}
		});	
	myDatagrid.datagrid('selectRow', 0).datagrid('beginEdit',0);
	editIndex=0;
	
	//选择用户,多选 
	
	var ed = myDatagrid.datagrid('getEditor',{index: editIndex, field: 'sysUserName'});
	$(ed.target).attr('id', "receptUserName"+newRowCount);
	$(ed.target).attr('disabled', true);
	
	var ed = myDatagrid.datagrid('getEditor',{index: editIndex, field: 'sysUserId'});
	$(ed.target).attr('id', "receptUserId"+newRowCount);
	//$(ed.target).attr('disabled', true);
	
	
   	var commonSelector = new CommonSelector("user","userSelectCommonDialog", "receptUserId"+newRowCount ,"receptUserName"+newRowCount,null,null, null, false);
   	commonSelector.init(null,null,'n'); //选择人员  回填部门 
	
}

function saveGroup()
{
	var myDatagrid=$('#dgGroup');
	myDatagrid.datagrid('endEdit',editIndex);
	
	if(editIndex!=-1)
	{
		$.messager.alert('提示','不能保存，请确保上一条数据填写完整','warning');
		return;
	}
	
	var rows = myDatagrid.datagrid('getChanges');
	for(var i = 0; i < rows.length; i++){
		var d = rows[i];
		if(d.sysGroupName.trim() == ""){
			$.messager.alert('提示','不能保存，请确保上一条数据填写完整','warning');
			return;
		}
	}
	var data =JSON.stringify(rows);
	if(rows.length > 0)
	{
		 $.ajax({
			 url:'platform/syscustomed/sysCustomedController/saveOrUpdatePersonalGroup',
			 data : {datas : data},
			 type : 'post',
			 dataType : 'json',
			 success : function(r){
				if(r.result==0){
					 myDatagrid.datagrid('reload');
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

/*删除选中数据*/
function deleteGroup(){
  
	var myDatagrid=$('#dgGroup');
	
	var rows = myDatagrid.datagrid('getChecked');
	var data =JSON.stringify(rows);
	
	if (rows.length > 0) {
		$.messager.confirm('请确认', '您确定要删除当前所选的数据？', function(b) {
			if (b) {
				
				
				$.ajax({
					url : 'platform/syscustomed/sysCustomedController/deletePersonalGroups',
					data : {
						datas : data
					},
					type : 'post',
					dataType : 'json',
					success : function(r) {
						
						if(r.result==0){
							myDatagrid.datagrid('reload');//刷新当前页
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

function dgApprovalOnClickRow(index,rowData)
{
	newRowCount++;
	
	var myDatagrid=$('#dgApproval');
	myDatagrid.datagrid('endEdit', editIndex2);
	if(editIndex2==-1)
	{
		myDatagrid.datagrid('beginEdit', index);  
		editIndex2=index;
		
		
	}
	else
	{
		$.messager.alert('提示','不能编辑，请确保上一条数据填写完整','warning');
	}
	
    
}

function dgApprovalOnAfterEdit(rowIndex, rowData, changes)
{
	//成功完成编辑，包括校验
	editIndex2=-1;
} 

function dgApprovalOnLoadSuccess(data)
{
	if(editIndex2 != -1){
		$('#dgApproval').datagrid('cancelEdit',editIndex);
		editIndex2 = -1;
	}
}

/*增加一条记录*/

function addApproval()
{
	newRowCount++;	
	
	var myDatagrid=$('#dgApproval');
	myDatagrid.datagrid('endEdit',editIndex2);
	if(editIndex2 != -1){
		$.messager.alert('提示','不能添加，请确保上一条数据填写完整','warning');
		return;
	}
	myDatagrid.datagrid('insertRow',{
		index: 0,
		row:{id: "", key: "PLATFORM_APPROVALOPTION", isMulti: '1', isDefault: '0'}
		});	
	myDatagrid.datagrid('selectRow', 0).datagrid('beginEdit',0);
	editIndex2=0;
		
}

function saveApproval()
{
	var myDatagrid=$('#dgApproval');
	myDatagrid.datagrid('endEdit',editIndex2);
	/**
	if(editIndex2!=-1)
	{
		$.messager.alert('提示','不能保存，请确保上一条数据填写完整','warning');
		return;
	}
	**/
	var rows = myDatagrid.datagrid('getChanges');
	for(var i = 0; i < rows.length; i++){
		var d = rows[i];
		if(d.value.trim() == ""){
			$.messager.alert('提示','不能保存，审批意见不能为空','warning');
			return;
		}else if(d.value.length > 200){
			$.messager.alert('提示','不能保存，审批意见内容过长,长度介于0-200之间','warning');
			return;
		}
	}
	var data =JSON.stringify(rows);
	if(rows.length > 0)
	{
		 $.ajax({
			 url:'platform/syscustomed/sysCustomedController/saveOrUpdateSysCustomed',
			 data : {datas : data},
			 type : 'post',
			 dataType : 'json',
			 success : function(r){
				if(r.result==0){
					 myDatagrid.datagrid('reload');
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

/*删除选中数据*/
function deleteApproval(){
  
	var myDatagrid=$('#dgApproval');
	
	var rows = myDatagrid.datagrid('getChecked');
	var ids = [];
	
	if (rows.length > 0) {
		$.messager.confirm('请确认', '您确定要删除当前所选的数据？', function(b) {
			if (b) {
				
				for(var i=0;i<rows.length;i++)
					ids.push(rows[i].id);
				
				$.ajax({
					url : 'platform/syscustomed/sysCustomedController/deleteSysCustomed',
					data : {
						ids : ids.join(',')
					},
					type : 'post',
					dataType : 'json',
					success : function(r) {
						
						if(r.result==0){
							myDatagrid.datagrid('reload');//刷新当前页
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

function dgOtherOnClickRow(index,rowData)
{
	newRowCount++;
	
	var myDatagrid=$('#dgOther');
	myDatagrid.datagrid('endEdit', editIndex3);
	if(editIndex3==-1)
	{
		myDatagrid.datagrid('beginEdit', index);  
		editIndex3=index;
		
		
	}
	else
	{
		$.messager.alert('提示','不能编辑，请确保上一条数据填写完整','warning');
	}
	
    
}

function dgOtherOnAfterEdit(rowIndex, rowData, changes)
{
	//成功完成编辑，包括校验
	editIndex3=-1;
} 

function dgOtherOnLoadSuccess(data)
{
	if(editIndex3 != -1){
		$('#dgOther').datagrid('cancelEdit',editIndex);
		editIndex3 = -1;
	}
}

/*增加一条记录*/

function addOther()
{
	newRowCount++;	
	
	var myDatagrid=$('#dgOther');
	myDatagrid.datagrid('endEdit',editIndex3);
	if(editIndex2 != -1){
		$.messager.alert('提示','不能添加，请确保上一条数据填写完整','warning');
		return;
	}
	myDatagrid.datagrid('insertRow',{
		index: 0,
		row:{id: "",  isMulti: '0', isDefault: '0'}
		});	
	myDatagrid.datagrid('selectRow', 0).datagrid('beginEdit',0);
	editIndex3=0;
		
}
function saveOther()
{
	var myDatagrid=$('#dgOther');
	myDatagrid.datagrid('endEdit',editIndex3);
	/**
	if(editIndex3!=-1)
	{
		$.messager.alert('提示','不能保存，请确保上一条数据填写完整','warning');
		return;
	}
	*/
	var rows = myDatagrid.datagrid('getChanges');
	
	for(var i = 0; i < rows.length; i++){
		var d = rows[i];
		if(d.key.trim() == "" || d.value.trim() == ""){
			$.messager.alert('提示','不能保存，属性名或属性值都不能为空','warning');
			return;
		}else if(d.key.length > 20){
			$.messager.alert('提示','不能保存，属性名内容过长,长度介于0-20之间','warning');
			return;
		}else if(d.value.length > 200){
			$.messager.alert('提示','不能保存，属性值内容过长,长度介于0-200之间','warning');
			return;
		}
	}
	
	var data =JSON.stringify(rows);
	if(rows.length > 0)
	{
		 $.ajax({
			 url:'platform/syscustomed/sysCustomedController/saveOrUpdateSysCustomed',
			 data : {datas : data},
			 type : 'post',
			 dataType : 'json',
			 success : function(r){
				if(r.result==0){
					 myDatagrid.datagrid('reload');
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

/*删除选中数据*/
function deleteOther(){
  
	var myDatagrid=$('#dgOther');
	
	var rows = myDatagrid.datagrid('getChecked');
	var ids = [];
	
	if (rows.length > 0) {
		$.messager.confirm('请确认', '您确定要删除当前所选的数据？', function(b) {
			if (b) {
				
				for(var i=0;i<rows.length;i++)
					ids.push(rows[i].id);
				
				$.ajax({
					url : 'platform/syscustomed/sysCustomedController/deleteSysCustomed',
					data : {
						ids : ids.join(',')
					},
					type : 'post',
					dataType : 'json',
					success : function(r) {
						
						if(r.result==0){
							myDatagrid.datagrid('reload');//刷新当前页
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

function saveMenuStyle(styleUrl, isClear)
{
	
	$.ajax({
		url : 'platform/syscustomed/sysCustomedController/saveCustomesMenuStyle',
		data : {
			key: "PLATFORM_MENU_STYLE",
			value: styleUrl,
			isMulti: "0",
			isDefault: "1"
		},
		type : 'post',
		dataType : 'json',
		success : function(r) {
			
			if(r.result==0){
				
				$.messager.alert('提示','首页菜单样式设置成功！重新登录即可生效。','warning');
				if(typeof isClear != 'undefined' && isClear == true){
					$("input[name='layout']").removeAttr("checked");
				}
			}
			else
			{
				$.messager.alert('提示','设置失败','warning');
			}
		}
	}); 
}
























