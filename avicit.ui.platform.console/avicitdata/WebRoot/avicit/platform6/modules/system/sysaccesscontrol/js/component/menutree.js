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
	if(!currentMenuId) return;
	try{
		$("#memuTree").treegrid('select', currentMenuId);
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
