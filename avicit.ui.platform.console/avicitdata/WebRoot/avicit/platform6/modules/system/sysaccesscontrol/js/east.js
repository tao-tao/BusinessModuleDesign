function clickComponentCell(rowIndex, field, value){
	switch(field){
	 	case 'READGRANTED':
	 		if(value == null || typeof(value) =='undefined'||value.length==0){
	 			setComponentAuth('S','1','1',rowIndex);
	 		}else if(value == '1'){
	 			setComponentAuth('S','0','',rowIndex);
	 		}else if(value == '0'){
	 			setComponentAuth('S','del','',rowIndex);
	 		}
	 		
	 		break ;
	 	case 'WRITEGRANTED':
	 		if(value == null || typeof(value) =='undefined'||value.length==0){
	 			setComponentAuth('S','1','1',rowIndex);
	 		}else if(value == '1'){
	 			setComponentAuth('S','','0',rowIndex);
	 		}else if(value == '0'){
	 			setComponentAuth('S','','del',rowIndex);
	 		}
	 		break ;
	 }
}

/**
 *查询
 */			
function initComponentSearch(){
	 $('#searchKeyWord').searchbox({
	        width: 200,
	        searcher: function (value) {
	        	var currResource = $("#tg").treegrid("getSelected");
				var resourceId = currResource==null?null:currResource.ID ;
	        	findComponentResources(resourceId,value);
	        },
	        prompt: "请输入组件名称或标识！"
	    });
}
// 访问权限formatter
function comReadAuthFormat(value, rowData, rowIndex) {
	if("1" == value)   
		return "<img src='avicit/platform6/modules/system/sysaccesscontrol/images/ok.png' title='允许访问' alt='允许访问' >";
	if("0" == value) 
		return "<img src='avicit/platform6/modules/system/sysaccesscontrol/images/no.gif' title='禁止访问' alt='禁止访问' >";
	
	return "<img src='avicit/platform6/modules/system/sysaccesscontrol/images/untitled.png' title='未设置权限' alt='未设置权限' >";	
}
//编辑权限formatter
function comWriteAuthFormat(value, rowData, rowIndex) {
	if("1" == value)   
		return "<img src='avicit/platform6/modules/system/sysaccesscontrol/images/ok.png' title='允许编辑' alt='允许编辑' >";
	if("0" == value) 
		return "<img src='avicit/platform6/modules/system/sysaccesscontrol/images/no.gif' title='禁止编辑' alt='禁止编辑' >";
	
	return "<img src='avicit/platform6/modules/system/sysaccesscontrol/images/untitled.png' title='未设置权限' alt='未设置权限' >";	
}
/**
 * 
 * @param currValue
 * @param rowData
 */
function setComponentAuth(authType,readStatus,writeStatus,rowIndex){
	var accessControlIds = "" ;
	var resourceIds      = "" ;
	//optType(S,B) accessControlIds readStatus writeStatus resourceIds targetType targetId
	var dgComp = $("#componentsDataGrid") ;
	if(authType == 'S'){
		var rows = dgComp.datagrid("getRows");
		var rowData = rows[rowIndex] ;
		var accessId = rowData.ACCESSCONTROLID;
		var resourceId = rowData.ID;
		rowData = null ;
		if(accessId  != null && accessId.length>0){
		  accessControlIds = accessId ;
		}else{
			resourceIds      = resourceId ;
		}
	}else {
		var rows =  dgComp.datagrid('getChecked');
		if(rows == null || rows.length==0){
			$.messager.alert('提示','请勾选若干组件后再进行此操作！','info');
			return ;
		}
		if(rows != null && rows.length>0){
			$.each(rows,function(i,selection){
				var currAccessControlId = selection.ACCESSCONTROLID ;
				if(currAccessControlId  != null && currAccessControlId.length>0){
					if(accessControlIds.length==0){
						accessControlIds = selection.ACCESSCONTROLID ;
					}else{
						accessControlIds += ","+selection.ACCESSCONTROLID ;
					}
				}else{
					if(resourceIds.length==0){
						resourceIds = selection.ID ;
					}else{
						resourceIds += ","+selection.ID ;
					}
				}
				
			});
		}
	}
	
	var requestParam = "";
	if(accessControlIds != null && accessControlIds.length > 0){
		requestParam += "accessControlIds="+accessControlIds ;
	}
    if(resourceIds != null && resourceIds.length > 0){
    	requestParam += "&resourceIds="+resourceIds ;
	}
	if(readStatus != null && readStatus.length > 0){
		requestParam += "&readStatus="+readStatus ;	
	}
	if(writeStatus != null && writeStatus.length > 0){
		requestParam += "&writeStatus="+writeStatus ;
	}
	if(TARGET_TYPE != null && TARGET_TYPE.length > 0){
		requestParam += "&targetType="+TARGET_TYPE ;
	}
	if(TARGET_ID != null && TARGET_ID.length > 0){
		requestParam += "&targetId="+TARGET_ID ;
	}
	$.ajax({
		url: "platform/componentManagerController/updateComponentResourceAuth.json",
		type: "POST",
		dataType: "json",
		data : requestParam ,
		success: function() {
			 var currResource = $("#tg").treegrid("getSelected");
			 var resourceId = currResource==null?null:currResource.ID ;
			 findComponentResources(resourceId,""); //重载组件
			 dgComp.datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
			$.messager.show({
				title : '提示',
				msg : "页面组件授权成功。",
				timeout: 2000,  
	            showType:'slide'
			});
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
            alert(XMLHttpRequest.status);
            alert(textStatus);
            alert(XMLHttpRequest.readyState);      
		}
	});
}
function refreshAuthority(){
	$.messager.confirm('加载资源权限信息', '确认要重新加载资源权限信息吗？', function(retValue){
		if (retValue){
			$.ajax({
				url: "platform/componentManagerController/refreshComponentAuthority.json",
				type: "POST",
				dataType: "json",
				success: function(msg) {
					$.messager.show({
						title : '提示',
						msg : "资源权限信息加载成功。",
						timeout: 2000,  
			            showType:'slide'
					});
				},
				error: function(XMLHttpRequest, textStatus, errorThrown) {
//                    alert(XMLHttpRequest.status);
//                    alert(textStatus);
//                    alert(XMLHttpRequest.readyState);
                }
			});
		}
	});
}

function refreshComponent(){
	//当前所选URL的id
	var currResource = $("#tg").treegrid("getSelected");
	var resourceId = currResource==null?null:currResource.ID ;
	if(!resourceId){
		$.messager.alert('提示','请选择一个菜单！','info');
		return ;
	}
	$.ajax({
		url: "platform/componentManagerController/refreshComponent4Page.json?resourceId="+resourceId,
		type: "POST",
		dataType: "json",
		data : resourceId,
		success: function(msg) {
		  if(msg.flag=='success'){
			  findComponentResources(resourceId,""); //重载组件
			  $.messager.show({
					title : '提示',
					msg : "重载页面组件成功。",
					timeout: 2000,  
		            showType:'slide'
				});  
		  }else if(msg.flag=='failure'){
			 if(msg.error){
				 $.messager.show({
						title : '提示',
						msg : msg.error,
						timeout: 2000,  
			            showType:'slide'
					});  
			 }
		  }
			
		},error: function(XMLHttpRequest, textStatus, errorThrown) {
//            alert(XMLHttpRequest.status);
//            alert(textStatus);
//            alert(XMLHttpRequest.readyState);
        }
	});
}

function deleteComponentResources(){
	/**if(TARGET_ID==null || TARGET_ID.length==0){
		var tipMsg = "" ;
		if(TARGET_TYPE=='R'){
			tipMsg = "角色" ;
		}else if(TARGET_TYPE=='U'){
			tipMsg = "用户" ;
		}if(TARGET_TYPE=='D'){
			tipMsg = "部门" ;
		}if(TARGET_TYPE=='G'){
			tipMsg = "群组" ;
		}if(TARGET_TYPE=='P'){
			tipMsg = "岗位" ;
		}
		$.messager.alert('提示','请选择一个'+tipMsg+'！','info');
		return ;
	}**/
	var rows =  $("#componentsDataGrid").datagrid('getChecked');
	var deleteIds = "" ;
	if(rows != null && rows.length>0){
		$.each(rows,function(i,selection){
			if(deleteIds.length==0){
				deleteIds = selection.ID ;
			}else{
				deleteIds += ","+selection.ID ;
			}
		});
	}
	if( deleteIds!= null && deleteIds.length > 0 ){
		$.messager.confirm('删除组件', '确认要删除选择的组件及相关的权限吗？', function(retValue){
		 if(retValue){
			$.ajax({
				url: "platform/componentManagerController/deleteComponentResources.json",
				type: "POST",
				dataType: "json",
				data : "deleteIds="+deleteIds,
				success: function(msg) {
				  if(msg.flag=='success'){
					  var currResource = $("#tg").treegrid("getSelected");
					  var resourceId = currResource==null?null:currResource.ID ;
					  $("#componentsDataGrid").datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
					  findComponentResources(resourceId,""); //重载组件
					  $.messager.show({
							title : '提示',
							msg : "组件删除成功。",
							timeout: 2000,  
				            showType:'slide'
						});  
				  }else if(msg.flag=='failure'){
					 if(msg.error){
						 $.messager.show({
								title : '提示',
								msg : msg.error,
								timeout: 2000,  
					            showType:'slide'
							});  
					 }
				  }
				}
			});
	   }
	 });
	}else{
		$.messager.alert('提示','请选择需要删除的组件！','info');
		return ;
	}
}