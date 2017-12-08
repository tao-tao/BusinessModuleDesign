/**
 * 模拟ajax导出
 * 无弹出框,post提交无参数限制
 * @param iframeId
 * @param formId
 * @param headData
 * @returns {exportUser}
 */
exportUser = function(iframeId,formId,headData,actionUrl){
	
	//alert(1);
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
			
			for (var key in headData)
			{
				 alert(key); 
				 alert(headData[key]);
				 var headInput = document.createElement("input"); 
				 headInput.setAttribute("name",key); 
				 headInput.setAttribute("type","text"); 
				 headInput.setAttribute("value",headData[key]); 
				 exportForm.appendChild(headInput); 
			}
		}
	};
	
	this.excuteExport = function(){
		alert(2);
		document.getElementById(formName).submit();
	};
	
	this.createDom();
};
