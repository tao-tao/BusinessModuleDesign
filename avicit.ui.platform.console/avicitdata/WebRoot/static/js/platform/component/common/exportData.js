/**
 * 模拟ajax导出
 * 无弹出框,post提交无参数限制 并且是异步的
 * @param iframeId
 * @param formId
 * @param headData
 * @returns {exportUser}
 */
exportData = function(iframeId,formId,headData,actionUrl){
	
	var iframeName = "_iframe_" + iframeId;
	var formName = "_form_" + formId;
	
	if(typeof(headData) == 'undefined'|| null == headData){
		alert("导出头部不能为空！");
		return;
	}
	
	//设置是否显示遮罩Iframe
	if(typeof(iframeId) == 'undefined'|| iframeId == null){
		alert("iframeId不能为空！");
		return;
	}
	
	//设置是否显示遮罩Iframe
	if(typeof(formId) == 'undefined'|| formId == null){
		alert("formId不能为空！");
		return;
	}
	

	this.createDom = function(){
		
		//先销毁对象，再创建
		if(jQuery("#" + iframeName).length > 0 ){
			jQuery("#" + iframeName).remove();
			
		}
		
		//先销毁对象，再创建
		if(jQuery("#" + formName).length > 0 ){
			jQuery("#" + formName).remove();
		}
		
		if(jQuery("#" + iframeName).length == 0){
			var exportIframe = document.createElement("iframe"); 
			exportIframe.id = iframeName; 
			exportIframe.name = iframeName; 
			exportIframe.style.display = 'none'; 
			document.body.appendChild(exportIframe); 
			
			//创建form 
			var exportForm = document.createElement("form"); 
			exportForm.method = 'post'; 
			exportForm.action = actionUrl; 
			exportForm.name = formName; 
			exportForm.id = formName;
			exportForm.target = iframeName;
			document.body.appendChild(exportForm); 
		   
			for (var key in headData){
				 var headInput = document.createElement("input"); 
				 headInput.setAttribute("name",key); 
				 headInput.setAttribute("type","text"); 
				 if(typeof(headData[key])=='string'){
					 headInput.setAttribute("value",headData[key]);
				 }else{
					 var jsonStr=JSON.stringify(headData[key]);
					 headInput.setAttribute("value",jsonStr);
				 }
				 exportForm.appendChild(headInput); 
			}
		}
	};
	
	this.excuteExport = function(){
		document.getElementById(formName).submit();
	};
	
	this.createDom();
};

/**
 * 获取列的配置对象
 */
function getGridColumnFieldsOptions(table) {
    var allColsTitle = $("#" + table).datagrid("options").columns;
    return allColsTitle;
};
