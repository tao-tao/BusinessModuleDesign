$(function(){
	$("#tg").treegrid({
		onClickCell:function(field,row){
			if(field=='NAME'){
				var urlId = row.ID ;
				if(TARGET_ID==null || TARGET_ID.length==0){
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
				}
				findComponentResources(urlId);
			}
		}
		
	});
});

// 刷新菜单授权缓存
function refreshMenuSecurityCache() {
	
	$.messager.confirm('系统', '真的要刷新菜单资源的权限信息吗？', function(r){
		if (r)
			$.ajax({
				url: "platform/componentManagerController/refreshComponentAuthority.json",
				type: "POST",
				dataType: "json",
				success: function() {
					$.messager.show({
						title : '提示',
						msg : "刷新菜单授权缓存成功。",
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
	});
	
} 

// checkBox 选中 
function checkboxOnclick(ct) {

	var currCheckbox=$(ct);
	var currNodeId=currCheckbox.parent().parent().parent().parent().attr("node-id");
	var parentNode=currCheckbox.parent().parent().parent().parent();
	
	var status = false;  
       if(currCheckbox.hasClass("tree-checkbox1"))  status = true;

       changCheck(status,currCheckbox);
       
       //选子节点
       var childNode=parentNode.next();
       if(!childNode.attr("node-id")){
        parentNode.next().find(".tree-checkbox").each(function(){
        	changCheck(status,$(this));
        })
       }
}

function changCheck(status,target){
	if(status){
		target.removeClass("tree-checkbox0 tree-checkbox1");
		target.addClass("tree-checkbox0");
	}else{
		target.removeClass("tree-checkbox0 tree-checkbox1");
		target.addClass("tree-checkbox1");
	}
}

// 树节点的formatter
function formatCheckBox(value, rowData, rowIndex){
   	if(rowData.checked){
   		return "<span class='tree-checkbox tree-checkbox1' onclick='checkboxOnclick(this);'></span>" + value;
   	}else{
   		return "<span class='tree-checkbox tree-checkbox0' onclick='checkboxOnclick(this);'></span>" + value;
   	}
}

// 点击一行，记录当前行的ID
function onClickRow() {
	var currMenu = $("#tg").treegrid('getSelected'); 
	if(!currMenu || !currMenu.ID) {
		window.MENU_ID = "";
		return;
	}
	window.MENU_ID = currMenu.ID; if(!window.MENU_ID) return;
	window.MENU_EVEL = $("#tg").treegrid('getLevel', window.MENU_ID); 
}

// 加载完树之后，默认选中一行
function selectOneRow() {
	if(!window.MENU_ID) return;
	try{
		//$("#tg").treegrid('expandAll', window.MENU_ID); // 批量修改后展开下一级效果更好，但影响性能。
		if("1" == window.MENU_ID)
			$("#tg").treegrid('expandAll', window.MENU_ID); 
		
		$("#tg").treegrid('select', window.MENU_ID);
	} catch(e) {

	}
}

// 权限formatter
function remarkformat(value, rowData, rowIndex) {
	
	if("1" == value) 
		return "<img src='avicit/platform6/modules/system/sysaccesscontrol/images/ok.png' title='允许访问' onclick='window.changeSingleMenuAuth(this);' alt=" + rowData.ID +">";
	if("0" == value) 
		return "<img src='avicit/platform6/modules/system/sysaccesscontrol/images/no.gif' title='禁止访问' onclick='window.changeSingleMenuAuth(this);' alt=" + rowData.ID +">";

	return "<img src='avicit/platform6/modules/system/sysaccesscontrol/images/untitled.png' title='未设置权限' onclick='window.changeSingleMenuAuth(this);' alt=" + rowData.ID +">";	
}

/**
 *查询
 */			
function initForSearch(){
	 $('#formSearch').searchbox({
	        width: 200,
	        searcher: function (value) {
	        	if(null == value||"" == value) {
					$("#tg").treegrid("options").url = "platform/componentManagerController/getSysMenusByParentId.json";
					$('#tg').treegrid('load',{
						 targetType: window.TARGET_TYPE,
						 targetId: window.TARGET_ID,
						 level: window.MENU_EVEL
					});
	        	} else {
					$("#tg").treegrid("options").url = "platform/componentManagerController/searchMenu.json";
					$('#tg').treegrid('load', {
						 search_name: value,
						 targetType: window.TARGET_TYPE,
						 targetId: window.TARGET_ID,
						 level: window.MENU_EVEL
					});
	        	};
	        },
	        prompt: "请输入菜单名称！"
	    });
}

$(document).ready(function() { 
	 initForSearch();
});

// west点击，选中不同权限主体时，激发的事件
window.loadAuthInfoData = function() {
	$("#tg").treegrid("options").url = "platform/componentManagerController/getSysMenusByParentId.json";
	$('#tg').treegrid('load', {
		 targetType: window.TARGET_TYPE,
		 targetId: window.TARGET_ID,
		 level: window.MENU_EVEL
	});
};

//改变单个菜单的权限状态
window.changeSingleMenuAuth = function(element) {
	//var currMenu = $("#tg").treegrid("getSelected");  // 可能点的那一行不是选中的那一行
	var currMenuId = element.alt;
	if(!currMenuId)
		return;
	$("#tg").treegrid("select", currMenuId);
	var currMenu = $("#tg").treegrid("getSelected"); 
	if(!currMenu)
		return;
	if(!window.window.TARGET_TYPE || !window.TARGET_ID) {
		$.messager.alert('系统','请选择被授权的对象！','info');
		return;
	}
	var parentId = currMenu._parentId;
	window.MENU_ID = currMenuId;
	
	var OPER_TYPE = "";
	var currRemark = currMenu.REMARK; 
	if("0" == currRemark)       // “禁止” 变  “没有”
		OPER_TYPE = "del";
	else if("1" == currRemark)  // “允许” 变  “禁止”
		OPER_TYPE = "0";
	else                        // “没有” 变  “允许”
		OPER_TYPE = "1";
	
	$.messager.confirm('系统', '确认要修改此项权限吗？', function(r) {
		if(r) {
			$.ajax({
				url: "platform/componentManagerController/saveSingleSetAuth.json",
				type: "POST",
				dataType: "json",
				data: {
					targetType: window.TARGET_TYPE,
					targerId: window.TARGET_ID,
					menuId: currMenuId,
					operType: OPER_TYPE
				},
				success: function(result) {
					if(!result || "success" != result.flag ) return;  // 操作失败，如没有对应的参数或库里没有对应的数据等
					/*
					 * 先看当前节点的父结点是否为第一次选中，如果是，则刷新父结点，如果父结点已经被选中，则只刷新当前结点。
					 * 
					 */
					if(result.returnP){
						$("#tg").treegrid('reload',parentId);
					}else{
						if(parentId){
							$("#tg").treegrid('reload', parentId);
						}else{
							$("#tg").treegrid('reload',currMenuId);
						}
					}
					
					/*
					if(parentId){
						$("#tg").treegrid('reload', parentId);
					}else{
						$("#tg").treegrid('reload',currMenuId);
					}
					*/
					$.messager.show({
						title: '提示',
						msg: "授权修改成功。",
						timeout: 2000,  
			            showType: 'slide'
					});								
				},
				error: function(XMLHttpRequest, textStatus, errorThrown) {
		            alert(XMLHttpRequest.status);
		            alert(textStatus);
		            alert(XMLHttpRequest.readyState);      
				}
			});	
		}
	});
	
};

// 批量改变菜单的权限状态（选中菜单树上的一个根，将根及全部子节点的权限改变）
window.changeBatchMenuAuth = function(operType) {
	
	if(!window.window.TARGET_TYPE || !window.TARGET_ID) {
		$.messager.alert('系统','请选择被授权的对象！','info');
		return;
	}
	
	var currMenu = $("#tg").treegrid("getSelected"); 
	if(!currMenu) {
		$.messager.alert('系统','请选择菜单！','info');
		return;
	};
	var currMenuId = currMenu.ID; 
	
	$.messager.confirm('系统', '确认要批量修改所有子结点的权限吗？', function(r) {
		if(r) {
					$.ajax({
						url: "platform/componentManagerController/saveBatchSetAuth.json",
						type: "POST",
						dataType: "json",
						data: {
							targetType: window.TARGET_TYPE,
							targerId: window.TARGET_ID,
							menuId: currMenuId,
							operType: operType
						},
						success: function(result) {
							if(!result || "success" != result.flag ) return;  // 操作失败，如没有对应的参数或库里没有对应的数据等
						    
							$("#tg").treegrid("select", currMenuId);
							var currMenu = $("#tg").treegrid("getSelected"); if(!currMenu) return;
							var parentId = currMenu._parentId;

							if(parentId) {
								$("#tg").treegrid('reload', parentId);	
							}else
								$("#tg").treegrid('reload');
												
							$.messager.show({
								title: '提示',
								msg: "授权批量修改成功。",
								timeout: 2000,  
					            showType: 'slide'
							});								
						},
						error: function(XMLHttpRequest, textStatus, errorThrown) {
				            alert(XMLHttpRequest.status);
				            alert(textStatus);
				            alert(XMLHttpRequest.readyState);      
						}
					});	
		};
	});
};

/**
 * 通过ajax加载后台的针对当前授权实体的所有权限信息
window.loadAuthInfoData = function() {
	if(!window.TARGET_TYPE || !window.TARGET_ID) return;

	var url = window.AUTH_INFO_URL + "?targetType=" + window.TARGET_TYPE + "&targetId=" + window.TARGET_ID;
	jQuery.post(url, function(data) {
		window.AUTH_DATA = data;
		window.redrawAuthIcon();
	});
};

window.redrawAuthIcon = function() {
	if(null == window.AUTH_DATA || "undefined" == typeof(window.AUTH_DATA)) return; 
	if(null == window.AUTH_DATA.menus || "undefined" == typeof(window.AUTH_DATA.menus)) return; 
	if(window.REFLASH_TYPE != "" && window.REFLASH_TYPE != "MENU") return;

	try {
		var root = $('#tg').treegrid('getRoot');
		window.recDraw(root);	
	}catch (err) {
		alert(err);
		//$.message.alert('Error', err);	
	}
};


window.recDraw = function(node){
	if (!node) return;
	$(window.AUTH_DATA.menus).each(function(index){
		if(node.id == $(this).id ) {
			node.REMARK = this.accessibility;
			$('#tg').treegrid('refresh', node.id);
		}
	});

	var children = $('#tg').treegrid('getChildren', node.id);
	children.each(function(){
		window.recDraw();
	});
	
};
*/

/**
 * 按菜单资源id加载组件信息
 */
function findComponentResources(urlId,queryKeyWord){
	var componentsDataGrid = $('#componentsDataGrid') ;
	var queryParams = componentsDataGrid.datagrid('options').queryParams; 
	if(urlId == null || typeof(urlId) == 'undefined'){
		urlId = '1' ;
	}
    queryParams.urlId =urlId;
    queryParams.targetType =TARGET_TYPE;
    queryParams.targetId =TARGET_ID;
    queryParams.queryKeyWord =queryKeyWord;
    componentsDataGrid.datagrid('options').queryParams=queryParams;        
    componentsDataGrid.datagrid('load');
    $(".searchbox-text:eq(2)").val("");
}
