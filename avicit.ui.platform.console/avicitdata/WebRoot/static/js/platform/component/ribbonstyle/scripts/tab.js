function addTabAndClose(name,url,img,code,position){
	if(url == "null"){
		return;
	}
	addTabfromPersonalMenu(name,url,img,code,position);
	$('.ribbon-menus').css('display', 'none');
}


function addTabfromPersonalMenu(name,url,img,code,position){
	
	var tabs = $('#tabs').tabs("tabs")
	var len = tabs.length;
	if (!$('#tabs').tabs('exists',name) && len >= 6){
		$.messager.alert("操作提示", "页签最多只能有6个!!","warning");
		return;
	}
	addTab(name,url,img,code,position);
}


