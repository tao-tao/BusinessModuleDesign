/**
 * 全局变量
 */
var framework = {
		user : {
			userId : "",
			userName : ""
		},
		form : {
			formId : "",
			formIds : "" //流程批量提交存储变量
		},
		bpm : {
			processDefineId : "",//当前流程文件定义Id
			processInstanceId : "", //当前流程实例DbId
			processExecutionId : "", //当前流程实例执行DbId
			processActivityName : ""//当前流程执行节点名称
		}
};

/**
 * 初始化Map
 */
var map = new Map();
/**
 * 根据formId取得流程提交信息
 * @param formId
 * @param isBatch
 */
function getProcessTaskParameter(formId){
	var bpmInfo = new Array();
	var url = "platform/bpm/clientbpmdisplayaction/getProcessDetailParameter";
	var contextPath = getPath();
	var urltranslated = contextPath + "/" + url;
	jQuery.ajax({
        type:"POST",
        async:false,
		data:"formId="+formId,
        url: urltranslated,  
        dataType:"json",
		context: document.body, 
        success: function(msg){
        	if(msg!=null){
        		if(msg.flag=='success'){
        			var entryId = msg.task.processInstance;
        			var executionId = msg.task.executionId;
        			var	taskId = msg.task.dbid;
        			bpmInfo[0] = entryId;
        			bpmInfo[1] = executionId;
        			bpmInfo[2] = taskId;
        			//return new ToolBar(entryId,executionId, taskId,'bpmToolBar', formId);
        		}else{
        			return null;
        		}
        	}
		},
		error: function(msg){
			return null;
		}
	}); 
	return bpmInfo;
}
/**
 * 流程批量工具栏
 */
ToolBarBatch = function (entryId,executionId,taskId,buttonDivId,formIds,needFormSave){
	this.entryId = entryId;
	this.executionId = executionId;
	this.taskId = taskId;
	this.buttonDiv = buttonDivId;
	this.formIds = formIds;
	this.buttonsArea = null;
	var _self = this;
	framework.form.formIds = formIds;
	this.buttonObj = new Button();
	this.canFormSave = false;
	this.isCurrentOperUser = 0;

	/**
	 * 初始化流程工具栏
	 */
	this.init = function(){
		this.buttonsArea = document.getElementById(this.buttonDiv);
		this.getButton(this.entryId,this.executionId,this.taskId);
	};
	/**
	 * 获取流程按钮
	 */
	this.getButton = function(entryId,executionId,taskId){
		var url = "platform/bpm/clientbpmdisplayaction/getoperateright";
		var contextPath = getPath();
		var urltranslated = contextPath + "/" + url;
		jQuery.ajax({
	        type:"POST",
			data:"processInstanceId="+entryId+"&executionId="+executionId+"&taskId="+taskId+"&isBatch=true",
	        url: urltranslated,  
	        dataType:"json",
			context: document.body, 
	        success: function(msg){
	        	if(msg!=null){
	        		if(msg.error!=null){ //失败
	        			//window.alert("Ajax操作时发生异常，地址为：" + urltranslated + "，异常信息为：" + msg.error);
	        			return;
	        		}else{
	        			_self.drawButton(msg);
	        		}
	        	}
			},
			error: function(msg){
				//window.alert("Ajax操作时发生异常，地址为：" + urltranslated + "，异常信息为：" + msg.responseText);
			}
    	}); 
	};
	/**
	 * 画流程按钮
	 */
	this.drawButton = function(json){
		this.isCurrentOperUser = json.isCurrentOperUser;
		var bpmContent = json.bpmContent;
		framework.bpm.processDefineId = bpmContent.processDefineId;
		framework.bpm.processInstanceId = bpmContent.processInstanceId;
		framework.bpm.processExecutionId = bpmContent.processExecutionId;
		framework.bpm.processActivityName = bpmContent.processActivityName;		
		var buttonArray = json.operateRight;
		var isBatch = json.isBatch;//批量提交标志 true 为批量，false不批量
		if(buttonArray == null){
			return ;
		}
		for (var i = 0; i < buttonArray.length; i++) {
			var button = buttonArray[i];
			if(isBatch){
				if(button.event == 'dosubmit'){
					button.event = 'dobactchsubmit'; //将单个提交转换成批量提交事件
				}else{
					continue;
				}
			}
			var data = {
					id : button.id,
					name : button.name,
					lable : button.lable,
					procinstDbid : button.procinstDbid,
					executionId : button.executionId,
					taskId : button.taskId,
					targetName : button.targetName,
					targetActivityName : button.targetActivityName,
					isAutoOpen : button.isAutoOpen,
					event : button.event
			};
			//改用了easyui的button
			//this.buttonObj.createButton(data);
			this.buttonObj.createEasyUIButton(data);
			if (!this.canFormSave && ButtonProcessing.ButtonName_FormSave == button.event) {
				this.canFormSave = true;
			}
		}
		//改用了easyui的button
		//this.buttonsArea.appendChild(this.buttonObj.getButtonDIV());
		//this.buttonObj.regButtonEvent();
		this.buttonObj.createEasyUIButtons(this.buttonsArea);
		//表单权限控制
		if (needFormSave != 'undefined' && needFormSave == true) {
			processFormAccess(entryId,executionId,this.isCurrentOperUser,this.canFormSave);
		}
	};
	this.init();
};
/**
 * 流程工具栏
 */
ToolBar = function (entryId,executionId,taskId,buttonDivId,formId,needFormSave){
	this.entryId = entryId;
	this.executionId = executionId;
	this.taskId = taskId;
	this.buttonDiv = buttonDivId;
	this.formId = formId;
	this.buttonsArea = null;
	var _self = this;
	this.buttonObj = new Button();
	framework.form.formId = formId;
	this.canFormSave = false;
	this.isCurrentOperUser = 0;
	/**
	 * 初始化流程工具栏
	 */
	this.init = function(){
		this.buttonsArea = document.getElementById(this.buttonDiv);
		this.getButton(this.entryId,this.executionId,this.taskId);
	};
	/**
	 * 获取流程按钮
	 */
	this.getButton = function(entryId,executionId,taskId){
		var url = "platform/bpm/clientbpmdisplayaction/getoperateright";
		var contextPath = getPath();
		var urltranslated = contextPath + "/" + url;
		jQuery.ajax({
	        type:"POST",
			data:"processInstanceId="+entryId+"&executionId="+executionId+"&taskId="+taskId,
	        url: urltranslated,  
	        dataType:"json",
			context: document.body, 
	        success: function(msg){
	        	if(msg!=null){
	        		if(msg.error!=null){ //失败
	        			//window.alert("Ajax操作时发生异常，地址为：" + urltranslated + "，异常信息为：" + msg.error);
	        			return;
	        		}else{
	        			_self.drawButton(msg);
	        		}
	        	}
			},
			error: function(msg){
				//window.alert("Ajax操作时发生异常，地址为：" + urltranslated + "，异常信息为：" + msg.responseText);
			}
    	}); 
	};
	/**
	 * 画流程按钮
	 */
	this.drawButton = function(json){
		this.isCurrentOperUser = json.isCurrentOperUser;
		var bpmContent = json.bpmContent;
		framework.bpm.processDefineId = bpmContent.processDefineId;
		framework.bpm.processInstanceId = bpmContent.processInstanceId;
		framework.bpm.processExecutionId = bpmContent.processExecutionId;
		framework.bpm.processActivityName = bpmContent.processActivityName;
		var buttonArray = json.operateRight;
		if(buttonArray == null){
			return ;
		}
		for (var i = 0; i < buttonArray.length; i++) {
			var button = buttonArray[i];
			//alert(button.event+","+button.id);
			var data = {
					id : button.id,
					name : button.name,
					lable : button.lable,
					procinstDbid : button.procinstDbid,
					executionId : button.executionId,
					taskId : button.taskId,
					targetName : button.targetName,
					targetActivityName : button.targetActivityName,
					isAutoOpen : button.isAutoOpen,
					orderBy : button.orderBy,
					event : button.event
			};
			//改用了easyui的button
			//this.buttonObj.createButton(data);
			this.buttonObj.createEasyUIButton(data);
			if (!this.canFormSave && ButtonProcessing.ButtonName_FormSave == button.event) {
				this.canFormSave = true;
			}
		}
		//改用了easyui的button
		//this.buttonsArea.appendChild(this.buttonObj.getButtonDIV());
		//this.buttonObj.regButtonEvent();
		this.buttonObj.createEasyUIButtons(this.buttonsArea);
		
		this.signTask(framework.bpm.processInstanceId,framework.bpm.processExecutionId,framework.bpm.processActivityName,bpmContent.hisTaskId);
		
		//表单权限控制
		if (needFormSave != 'undefined' && needFormSave == true) {
			processFormAccess(entryId,executionId,this.isCurrentOperUser,this.canFormSave);
		}
		
		//以下判断是否自动打开流程选人提交窗口
		var submitButton = $("div");
		if(submitButton!=null){
			for(var j=0; j<submitButton.length; j++){
				//alert(submitButton[j].id);
				if(submitButton[j]!=null&&submitButton[j].id!=null&&submitButton[j].id.indexOf("autoOpen_")!=-1){
					submitButton[j].click();
					break;
				}
			}
		}
	};
	

	//标记打开待办
	this.signTask = function(processInstanceId,processExecutionId,processActivityName,taskId){
		var url = "platform/bpm/clientbpmoperateaction/dosign";
		var contextPath = getPath();
		var urltranslated = contextPath + "/" + url;
		jQuery.ajax({
	        type:"POST",
			data:"processInstanceId="+processInstanceId+"&executionId="+processExecutionId+"&activityName="+processActivityName+"&taskId="+taskId,
	        url: urltranslated,  
	        dataType:"json",
			context: document.body, 
	        success: function(msg){
				
			},
			error: function(msg){
				//window.alert("Ajax操作时发生异常，地址为：" + urltranslated + "，异常信息为：" + msg.responseText);
			}
    	}); 
	};
	
	this.init();
};


/**
 * 流程跟踪
 */
function doTrack(processInstanceId,executionId,taskId,outcome,targetActivityName){
	var clientWidth = document.body.clientWidth;
	var clientHeight = document.body.clientHeight;
	var url = getPath()+"/avicit/platform6/bpmclient/bpm/ProcessTrack.jsp";
	url += "?processInstanceId="+processInstanceId;
	//window.open(encodeURI(url));
	var usd = new UserSelectDialog("trackdialog",clientWidth,clientHeight,encodeURI(url) ,"流程跟踪窗口");
	usd.show();
}

/**
 * 设为关注
 * @param dialogId
 */
function doFocus(processInstanceId,executionId,taskId,outcome,targetActivityName){
		
	//ajaxRequest("POST","dbid="+taskId,"platform/bpm/clientbpmdisplayaction/setFocusedTask","json","backFinished");
	ajaxRequest("POST","dbid="+taskId,"platform/bpm/clientbpmoperateaction/setFocusedTask","json","backFinished");
	//window.alert("已经设为关注"+taskId);	
	
}
/**
 * 回调提示
 * @param obj
 */
function backFinished(obj){
	if(obj != null && obj.mes == false){
		alert("该任务此前已被关注！");
	}else if(obj != null && obj.mes == true){
		alert("已成功关注该任务！");
	}
}

function doclose(dialogId){
	if(dialogId){
		jQuery("#"+dialogId).window("close");
	}else{
		jQuery("#userselectdialog").window("close");
	}
}



/**
 * 数据存储
 * @param pId 流程实例Id
 * @param eId 当前节点Id
 * @param tId 任务Id
 * @param o outcome的Name
 * @param t 类型
 * @param targetActivityName 目标节点名称
 * @returns
 */
function DataObj(pId,eId,tId,o,t,targetActivityName){
	this.procinstDbid = pId;
	this.executionId = eId;
	this.taskId = tId;
	this.outcome = o;
	this.type = t;
	this.targetActivityName = targetActivityName;
}

/**
 * dataObj赋值 将参数对象装入Map中
 * @param procinstDbid 流程实例Id
 * @param executionId 当前节点Id
 * @param taskId 任务Id
 * @param outcome outcome的Name
 * @param type 类型
 */
function setDataObj(procinstDbid,executionId,taskId,outcome,type,targetActivityName){
	var obj = new DataObj(procinstDbid,executionId,taskId,outcome,type,targetActivityName);
	//debugger;
	map.put(type,obj);
}


function getFormData(){
	try{
		if(window.getFormDataJson){
			getFormDataJson();
		}
	}catch(e){}
}

/******************************************流程提交和流程的特殊操选择框确定事件*************************************************/


/**
 * 流程提交
 */
function dosubmit(procinstDbid,executionId,taskId,outcome,targetActivityName){
	//debugger;
	setDataObj(procinstDbid,executionId,taskId,outcome,"dosubmit",targetActivityName);
	var b = ButtonProcessing.ButtonPreviousProcessing("dosubmit",procinstDbid,executionId,framework.bpm.processActivityName,targetActivityName,framework.form.formId);
	getFormData();
	var para = "procinstDbid="+procinstDbid+"&executionId="+executionId+"&taskId="+taskId+"&outcome="+outcome+"&targetActivityName="+targetActivityName+"&type=dosubmit&doSubmitUrl=" + escape("platform/bpm/clientbpmoperateaction/dosubmit") + "&doSubmitCallEvent=submitBack";
	var usd = new UserSelectDialog("userselectdialog","650","450",getPath() + "/platform/user/bpmSelectUserAction/main?"+para,"流程提交处理窗口");
	var buttons = [{
		text:'提交',
		id : 'processSubimt',
		//iconCls : 'icon-submit',
		handler:function(){
			var frmId = $('#userselectdialog iframe').attr('id');
			var frm = document.getElementById(frmId).contentWindow;
			var seleUserJson = frm.getSelectedResultDataJson();
			if(typeof(seleUserJson) != 'undefined'){
				$('#processSubimt').linkbutton('disable');
				easyuiMask();
				var data = "taskId="+taskId+"&userJson=" + seleUserJson + "&outcome="+outcome+"&formJson="+framework.variable;
				ajaxRequest("POST",data,"platform/bpm/clientbpmoperateaction/dosubmit","json","submitBack")
			}
		}
	},{
		text:'暂存',
		id : 'tmpSave',
		//iconCls : 'icon-save',
		handler:function(){
			$('#tmpSave').linkbutton('disable');
			var frmId = $('#userselectdialog iframe').attr('id');
			var frm = document.getElementById(frmId).contentWindow;
			frm.tempSaveToCookie();
		}
	 }];
	if(b){
		usd.createButtonsInDialog(buttons);
		usd.show();
	}
}
function submitBack(obj){
	easyuiUnMask();
	doclose("userselectdialog");
	var dataObj = map.get("dosubmit");
	var b = ButtonProcessing.ButtonPostProcessing("dosubmit",dataObj.procinstDbid,dataObj.executionId,framework.bpm.processActivityName,dataObj.targetActivityName,framework.form.formId);
	if(b==true||b=="true"){
		//alert("提交成功");
		//window.close();
		window.location.reload();
	}
}
/**
 * 流程自动批量提交
 */
function doautosubmit(formIds,parameters,message){
	var data = "formIds="+formIds+"&idsTitles=" + parameters + "&message=" + message;
	ajaxRequest("POST",data,"platform/bpm/clientbpmoperateaction/doBatchAutoSubmit","json","submitBack2");
}
function submitBack2(obj){
	if(obj!=null && obj.flag=='success'){
		$.messager.show({
			title : '提示',
			msg : "操作成功！"
		});
	}
}
/**
 * 流程批量提交
 */
function dobactchsubmit(procinstDbid,executionId,taskId,outcome,targetActivityName){
	ButtonProcessing.ButtonPreviousProcessing("dobactchsubmit",procinstDbid,executionId,framework.bpm.processActivityName,targetActivityName,framework.form.formIds);
	var para = "procinstDbid="+procinstDbid+"&executionId="+executionId+"&taskId="+taskId+"&outcome="+outcome+"&targetActivityName="+targetActivityName+"&type=dosubmit&doSubmitUrl=" + escape("platform/bpm/clientbpmoperateaction/dobactchsubmit") + "&doSubmitCallEvent=batchSubmitBack";
	//var para = "procinstDbid="+procinstDbid+"&executionId="+executionId+"&taskId="+taskId+"&outcome="+outcome+"&targetActivityName="+targetActivityName+"&type=dobactchsubmit&doSubmitUrl=" + escape("platform/bpm/clientbpmoperateaction/dobactchsubmit") + "&doSubmitCallEvent=batchSubmitBack";
	var usd = new UserSelectDialog("userselectdialog","600","480",getPath() + "/platform/user/bpmSelectUserAction/main?"+para,"流程提交处理窗口");
	var buttons = [{
		text:'提交',
		id : 'processSubimt',
		//iconCls : 'icon-submit',
		handler:function(){
			var frmId = $('#userselectdialog iframe').attr('id');
			var frm = document.getElementById(frmId).contentWindow;
			var seleUserJson = frm.getSelectedResultDataJson();
			if(typeof(seleUserJson) != 'undefined'){
				$('#processSubimt').linkbutton('disable');
				easyuiMask();
				var data = "formIds="+framework.form.formIds+"&userJson=" + seleUserJson + "&outcome="+outcome+"&formJson="+framework.variable;
				ajaxRequest("POST",data,"platform/bpm/clientbpmoperateaction/dobatchsubmit","json","batchSubmitBack");
				usd.close();
			}
		}
	},{
		text:'暂存',
		id : 'tmpSave',
		handler:function(){
			$('#tmpSave').linkbutton('disable');
			var frmId = $('#userselectdialog iframe').attr('id');
			var frm = document.getElementById(frmId).contentWindow;
			frm.tempSaveToCookie();
		}
	 }];
	usd.createButtonsInDialog(buttons);
	usd.show();
}
function batchSubmitBack(obj){
	easyuiUnMask();
	if(obj!=null && obj.flag=='success'){
		$.messager.show({
			title : '提示',
			msg : "操作成功！"
		});
	}
	window.location.reload();
}
/**
 * 流程跳转
 */
function doglobaljump(processInstanceId,executionId,taskId,outcome,targetActivityName){
	var url = getPath()+"/avicit/platform6/bpmclient/bpm/ProcessGraph.jsp";
	// Added in 01-17
	url += "?processInstanceId="+processInstanceId+"&executionId="+executionId+"&taskId="+taskId+"&outcome="+outcome+"&type=doglobaljump";
	//window.open(encodeURI(url));
	var usd = new UserSelectDialog("doglobaljumpdialog","650","450",encodeURI(url) ,"流程跳转处理窗口");
	usd.show();
	
}
/**
 * 流程跳转选择目标节点后
 */
function dojumpActivitySelect(procinstDbid,executionId,taskId,outcome,targetActivityName){
	jQuery("#doglobaljumpdialog").window("close");
	setDataObj(procinstDbid,executionId,taskId,outcome,"doglobaljump",targetActivityName);
	ButtonProcessing.ButtonPreviousProcessing("doglobaljump",procinstDbid,executionId,framework.bpm.processActivityName,targetActivityName,framework.form.formId);
	getFormData();
	var para = "procinstDbid="+procinstDbid+"&executionId="+executionId+"&taskId="+taskId+"&outcome="+outcome+"&targetActivityName="+targetActivityName+"&type=doglobaljump&doSubmitUrl=" + escape("platform/bpm/clientbpmoperateaction/dojump") + "&doSubmitCallEvent=globaljumpBack";
	var usd = new UserSelectDialog("userselectdialog","650","480",getPath() + "/platform/user/bpmSelectUserAction/main?"+para,"流程跳转处理窗口");
	var buttons = [{
		text:'提交',
		id : 'processSubimt',
		//iconCls : 'icon-submit',
		handler:function(){
			var frmId = $('#userselectdialog iframe').attr('id');
			var frm = document.getElementById(frmId).contentWindow;
			var seleUserJson = frm.getSelectedResultDataJson();
			if(typeof(seleUserJson) != 'undefined'){
				$('#processSubimt').linkbutton('disable');
				easyuiMask();
				var data = "procinstDbid="+procinstDbid+"&executionId="+executionId+"&userJson="+seleUserJson+"&activityName="+targetActivityName;
				ajaxRequest("POST",data,"platform/bpm/clientbpmoperateaction/dojump","json","globaljumpBack");
			}
		}
	}];
	usd.createButtonsInDialog(buttons);
	usd.show();
}

/**
 * 跳转 回调
 */
function globaljumpBack(obj){
	var dataObj = map.get("doglobaljump");
	ButtonProcessing.ButtonPostProcessing("doglobaljump",dataObj.procinstDbid,dataObj.executionId,framework.bpm.processActivityName,dataObj.targetActivityName,framework.form.formId);
	//window.close();
	window.location.reload();
}



/**
 * 流程转发
 */
function dotransmit(procinstDbid,executionId,taskId,outcome,targetActivityName){
	setDataObj(procinstDbid,executionId,taskId,outcome,"dotransmit",targetActivityName);
	var b = ButtonProcessing.ButtonPreviousProcessing("dotransmit",procinstDbid,executionId,framework.bpm.processActivityName,targetActivityName,framework.form.formId);
	getFormData();
	var para = "procinstDbid="+procinstDbid+"&executionId="+executionId+"&taskId="+taskId+"&outcome="+outcome+"&targetActivityName="+targetActivityName+"&type=dotransmit&doSubmitUrl=" + escape("platform/bpm/clientbpmoperateaction/dotransmit") + "&doSubmitCallEvent=transmitBack";
	var usd = new UserSelectDialog("dotransmitdialog","650","480",getPath() + "/platform/user/bpmSelectUserAction/main?"+para,"流程转发处理窗口");
	var buttons = [{
		text:'提交',
		id : 'processSubimt',
		//iconCls : 'icon-submit',
		handler:function(){
			var frmId = $('#dotransmitdialog iframe').attr('id');
			var frm = document.getElementById(frmId).contentWindow;
			var seleUserJson = frm.getSelectedResultDataJson();
			if(typeof(seleUserJson) != 'undefined'){
				$('#processSubimt').linkbutton('disable');
				easyuiMask();
				var data = "procinstDbid="+procinstDbid+"&executionId="+executionId+"&userJson="+seleUserJson;
				ajaxRequest("POST",data,"platform/bpm/clientbpmoperateaction/dotransmit","json","transmitBack");
			}
		}
	}];
	if(b){
		usd.createButtonsInDialog(buttons);
		usd.show();
	}
}

/**
 * 流程转发
 */
function transmitBack(obj){
	var dataObj = map.get("dotransmit");
	ButtonProcessing.ButtonPostProcessing("dotransmit",dataObj.procinstDbid,dataObj.executionId,framework.bpm.processActivityName,dataObj.targetActivityName,framework.form.formId);
	//alert("转发成功");
	//window.close();
	window.location.reload();
}



/**
 * 流程转办
 */
function dosupersede(procinstDbid,executionId,taskId,outcome,targetActivityName){
	setDataObj(procinstDbid,executionId,taskId,outcome,"dosupersede",targetActivityName);
	var b = ButtonProcessing.ButtonPreviousProcessing("dosupersede",procinstDbid,executionId,framework.bpm.processActivityName,targetActivityName,framework.form.formId);
	getFormData();
	var para = "procinstDbid="+procinstDbid+"&executionId="+executionId+"&taskId="+taskId+"&outcome="+outcome+"&targetActivityName="+targetActivityName+"&type=dosupersede&doSubmitUrl=" + escape("platform/bpm/clientbpmoperateaction/dosupersede") + "&doSubmitCallEvent=supersedeBack";
	var usd = new UserSelectDialog("dosupersededialog","650","480",getPath() + "/platform/user/bpmSelectUserAction/main?"+para,"流程转办窗口");
	var buttons = [{
		text:'提交',
		id : 'processSubimt',
		//iconCls : 'icon-submit',
		handler:function(){
			var frmId = $('#dosupersededialog iframe').attr('id');
			var frm = document.getElementById(frmId).contentWindow;
			var seleUserJson = frm.getSelectedResultDataJson();
			if(typeof(seleUserJson) != 'undefined'){
				$('#processSubimt').linkbutton('disable');
				easyuiMask();
				var data = "executionId="+executionId+"&userJson="+seleUserJson+"&formJson="+framework.variable;
				ajaxRequest("POST",data,"platform/bpm/clientbpmoperateaction/dosupersede","json","supersedeBack");
			}
		}
	}];
	if(b){
		usd.createButtonsInDialog(buttons);
		usd.show();
	}
}
/**
 * 转办 回调 
 */
function supersedeBack(obj){
	var dataObj = map.get("dosupersede");
	ButtonProcessing.ButtonPostProcessing("dosupersede",dataObj.procinstDbid,dataObj.executionId,framework.bpm.processActivityName,dataObj.targetActivityName,framework.form.formId);
	//alert("转办成功");
	//window.close();
	window.location.reload();
}


/**
 * 退回开始节点df
 */
function doretreattodraft(procinstDbid,executionId,taskId,outcome,targetActivityName){
	setDataObj(procinstDbid,executionId,taskId,outcome,"doretreattodraft",targetActivityName);
	var b = ButtonProcessing.ButtonPreviousProcessing("doretreattodraft",procinstDbid,executionId,framework.bpm.processActivityName,targetActivityName,framework.form.formId);
	getFormData();
	var para = "procinstDbid="+procinstDbid+"&executionId="+executionId+"&taskId="+taskId+"&outcome="+outcome+"&targetActivityName="+targetActivityName+"&type=doretreattodraft&doSubmitUrl=" + escape("platform/bpm/clientbpmoperateaction/dobacktofirst") + "&doSubmitCallEvent=backToFirstBack";
	var usd = new UserSelectDialog("doretreattodraftdialog","650","480",getPath() + "/platform/user/bpmSelectUserAction/main?"+para,"流程退回开始节点");
	var buttons = [{
		text:'提交',
		id : 'processSubimt',
		//iconCls : 'icon-submit',
		handler:function(){
			var frmId = $('#doretreattodraftdialog iframe').attr('id');
			var frm = document.getElementById(frmId).contentWindow;
			var seleUserJson = frm.getSelectedResultDataJson();
			if(typeof(seleUserJson) != 'undefined'){
				$('#processSubimt').linkbutton('disable');
				easyuiMask();
				var data = "executionId="+executionId+"&userJson="+seleUserJson;
				ajaxRequest("POST",data,"platform/bpm/clientbpmoperateaction/dobacktofirst","","backToFirstBack");
			}
		}
	}];
	if(b){
		usd.createButtonsInDialog(buttons);
		usd.show();
	}
}

/**
 * 退回开始节点 回调
 */
function backToFirstBack(obj){
	var dataObj = map.get("doretreattodraft");
	doclose("doretreattodraftdialog");
	var b = ButtonProcessing.ButtonPostProcessing("doretreattodraft",dataObj.procinstDbid,dataObj.executionId,framework.bpm.processActivityName,dataObj.targetActivityName,framework.form.formId);
	if(b==true||b=="true"){
		//alert("退回成功");
		//window.close();
		window.location.reload();
	}

}


/**
 * 退回上一节点
 */
function doretreattoprev(procinstDbid,executionId,taskId,outcome,targetActivityName){
	setDataObj(procinstDbid,executionId,taskId,outcome,"doretreattoprev",targetActivityName);
	var b = ButtonProcessing.ButtonPreviousProcessing("doretreattoprev",procinstDbid,executionId,framework.bpm.processActivityName,targetActivityName,framework.form.formId);
	getFormData();
	var para = "procinstDbid="+procinstDbid+"&executionId="+executionId+"&taskId="+taskId+"&outcome="+outcome+"&targetActivityName="+targetActivityName+"&type=doretreattoprev&doSubmitUrl=" + escape("platform/bpm/clientbpmoperateaction/dobacktoprev") + "&doSubmitCallEvent=backToPrevBack";
	var usd = new UserSelectDialog("doretreattoprevdialog","650","480",getPath() + "/platform/user/bpmSelectUserAction/main?"+para,"退回上一步");
	var buttons = [{
		text:'提交',
		id : 'processSubimt',
		//iconCls : 'icon-submit',
		handler:function(){
			var frmId = $('#doretreattoprevdialog iframe').attr('id');
			var frm = document.getElementById(frmId).contentWindow;
			var seleUserJson = frm.getSelectedResultDataJson();
			if(typeof(seleUserJson) != 'undefined'){
				$('#processSubimt').linkbutton('disable');
				easyuiMask();
				var data = "executionId="+executionId+"&userJson="+seleUserJson;
				ajaxRequest("POST",data,"platform/bpm/clientbpmoperateaction/dobacktoprev","","backToPrevBack");
			}
		}
	}];
	if(b){
		usd.createButtonsInDialog(buttons);
		usd.show();
	}
}

/**
 * 退回上一节点 回调
 */
function backToPrevBack(obj){
	var dataObj = map.get("doretreattoprev");
	doclose("doretreattoprevdialog");
	var b = ButtonProcessing.ButtonPostProcessing("doretreattoprev",dataObj.procinstDbid,dataObj.executionId,framework.bpm.processActivityName,dataObj.targetActivityName,framework.form.formId);
	if(b==true||b=="true"){
		//alert("退回成功");
		//window.close();
		window.location.reload();
	}
}


/**
 * 流程补发
 */
function dosupplement(processInstanceId,executionId,taskId,outcome,targetActivityName){
	//alert(para);
	var url = getPath()+"/avicit/platform6/bpmclient/bpm/ProcessGraph.jsp";
	url += "?processInstanceId="+processInstanceId+"&executionId="+executionId+"&taskId="+taskId+"&outcome="+outcome+"&type=dosupplement&random="+Math.random();
	//window.open(encodeURI(url));
	var usd = new UserSelectDialog("dosupplementdialog","650","400",encodeURI(url) ,"流程补发处理窗口");
	usd.show();
	
}
/**
 * 流程补发选择目标节点后
 */
function dosupplementActivitySelect(procinstDbid,executionId,taskId,outcome,targetActivityName){
	jQuery("#dosupplementdialog").window("close");
	setDataObj(procinstDbid,executionId,taskId,outcome,"dosupplement",targetActivityName);
	var b = ButtonProcessing.ButtonPreviousProcessing("dosupplement",procinstDbid,executionId,framework.bpm.processActivityName,targetActivityName,framework.form.formId);
	getFormData();
	var para = "procinstDbid="+procinstDbid+"&executionId="+executionId+"&taskId="+taskId+"&outcome="+outcome+"&targetActivityName="+targetActivityName+"&type=dosupplement&doSubmitUrl=" + escape("platform/bpm/clientbpmoperateaction/dosupplementassignee") + "&doSubmitCallEvent=supplementBack&random="+Math.random();
	var usd = new UserSelectDialog("userselectdialog","650","480",getPath() + "/platform/user/bpmSelectUserAction/main?"+para,"流程补发处理窗口");
	var buttons = [{
		text:'提交',
		id : 'processSubimt',
		//iconCls : 'icon-submit',
		handler:function(){
			var frmId = $('#userselectdialog iframe').attr('id');
			var frm = document.getElementById(frmId).contentWindow;
			var seleUserJson = frm.getSelectedResultDataJson();
			if(typeof(seleUserJson) != 'undefined'){
				$('#processSubimt').linkbutton('disable');
				easyuiMask();
				var data = "executionId="+executionId+"&userJson="+seleUserJson;
				ajaxRequest("POST",data,"platform/bpm/clientbpmoperateaction/dosupplementassignee","json","supplementBack");
			}
		}
	}];
	if(b){
		usd.createButtonsInDialog(buttons);
		usd.show();
	}

}

/**
 * 流程增发补发回调
 */
function supplementBack(obj){
	var dataObj = map.get("dosupplement");
	ButtonProcessing.ButtonPostProcessing("dosupplement",dataObj.procinstDbid,dataObj.executionId,framework.bpm.processActivityName,dataObj.targetActivityName,framework.form.formId);
	//window.close();
	window.location.reload();
}


/**
 * 流程拿回
 */
function dowithdraw(processInstanceId,executionId,taskId,outcome,targetActivityName){
	ButtonProcessing.ButtonPreviousProcessing("dowithdraw",processInstanceId,executionId,framework.bpm.processActivityName,targetActivityName,framework.form.formId);
	getFormData();
	var url = getPath()+"/avicit/platform6/bpmclient/bpm/ProcessGraph.jsp";
	url += "?processInstanceId="+processInstanceId+"&executionId="+executionId+"&taskId="+taskId+"&outcome="+outcome+"&targetActivityName="+targetActivityName+"&type=dowithdraw";
	var usd = new UserSelectDialog("dowithdrawdialog","650","400",encodeURI(url) ,"流程拿回窗口");
	usd.show();
}

/**
 * 流程拿回选择目标节点后
 */
function dowithdrawActivitySelect(procinstDbid,executionId,taskId,outcome,targetActivityName){
	jQuery("#dowithdrawdialog").window("close");
	setDataObj(procinstDbid,executionId,taskId,outcome,"dowithdraw",targetActivityName);
	var b = ButtonProcessing.ButtonPreviousProcessing("dowithdraw",procinstDbid,executionId,framework.bpm.processActivityName,targetActivityName,framework.form.formId);
	getFormData();
	var para = "procinstDbid="+procinstDbid+"&executionId="+executionId+"&taskId="+taskId+"&outcome="+outcome+"&targetActivityName="+targetActivityName+"&type=dowithdraw&doSubmitUrl=" + escape("platform/bpm/clientbpmoperateaction/dowithdrawcurract") +  "&doSubmitCallEvent=withdrawBack&random="+Math.random();
	var usd = new UserSelectDialog("userselectdialog_" ,"650","480",getPath() + "/platform/user/bpmSelectUserAction/main?"+para,"流程拿回处理窗口");
	var buttons = [{
		text:'提交',
		id : 'processSubimt',
		//iconCls : 'icon-submit',
		handler:function(){
			var frmId = $('#userselectdialog_ iframe').attr('id');
			var frm = document.getElementById(frmId).contentWindow;
			var seleUserJson = frm.getSelectedResultDataJson();
			if(typeof(seleUserJson) != 'undefined'){
				$('#processSubimt').linkbutton('disable');
				easyuiMask();
				var data = "executionId="+executionId+"&userJson="+seleUserJson;
				ajaxRequest("POST",data,"platform/bpm/clientbpmoperateaction/dowithdrawcurract","json","withdrawBack");
			}
		}
	}];
	if(b){
		usd.createButtonsInDialog(buttons);
		usd.show();
	}

}


/**
 * 拿回 回调
 */
function withdrawBack(obj){
	var dataObj = map.get("dowithdraw");
	doclose("userselectdialog_");
	ButtonProcessing.ButtonPostProcessing("dowithdraw",dataObj.procinstDbid,dataObj.executionId,framework.bpm.processActivityName,dataObj.targetActivityName,framework.form.formId);
//	if(b==true||b=="true"){
		//alert("拿回成功");
		//window.close();
		window.location.reload();
//	}
}


/**
 * 流程节点内拿回操作
 */
function dowithdrawassignee(procinstDbid,executionId,taskId,outcome,targetActivityName){
	setDataObj(procinstDbid,executionId,taskId,outcome,"dowithdrawassignee",targetActivityName);
	var b = ButtonProcessing.ButtonPreviousProcessing("dowithdrawassignee",procinstDbid,executionId,framework.bpm.processActivityName,targetActivityName,framework.form.formId);
	//获取表单data的json
	getFormData();
	var para = "procinstDbid="+procinstDbid+"&executionId="+executionId+"&taskId="+taskId+"&outcome="+outcome+"&targetActivityName="+targetActivityName+"&type=dowithdrawassignee&doSubmitUrl=" + escape("platform/bpm/clientbpmoperateaction/dowithdrawactassignee") +  "&doSubmitCallEvent=withdrawassigneeBack&random="+Math.random();
	var usd = new UserSelectDialog("dowithdrawassigneedialog","650","480",getPath() + "/platform/user/bpmSelectUserAction/main?"+para,"流程节点内拿回处理窗口");
	var buttons = [{
		text:'提交',
		id : 'processSubimt',
		//iconCls : 'icon-submit',
		handler:function(){
			var frmId = $('#dowithdrawassigneedialog iframe').attr('id');
			var frm = document.getElementById(frmId).contentWindow;
			var seleUserJson = frm.getSelectedResultDataJson();
			if(typeof(seleUserJson) != 'undefined'){
				$('#processSubimt').linkbutton('disable');
				easyuiMask();
				var data = "executionId="+executionId+"&userJson="+seleUserJson;
				ajaxRequest("POST",data,"platform/bpm/clientbpmoperateaction/dowithdrawactassignee","json","withdrawassigneeBack");
			}
		}
	}];
	if(b){
		usd.createButtonsInDialog(buttons);
		usd.show();
	}
}

/**
 * 节点内拿回 回调
 */
function withdrawassigneeBack(obj){
	var dataObj = map.get("dowithdrawassignee");
	ButtonProcessing.ButtonPostProcessing("doinnerwithdraw",dataObj.procinstDbid,dataObj.executionId,framework.bpm.processActivityName,dataObj.targetActivityName,framework.form.formId);
	//alert("拿回成功");
	//window.close();
	window.location.reload();
}


/**
 * 全局转发
 */
function doglobaltransmit(processInstanceId,executionId,taskId,outcome,targetActivityName){
	//alert(para);
	var url = getPath()+"/avicit/platform6/bpmclient/bpm/ProcessGraph.jsp";
	url += "?processInstanceId="+processInstanceId+"&executionId="+executionId+"&taskId="+taskId+"&outcome="+outcome+"&targetActivityName="+targetActivityName+"&type=doglobaltransmit";
	//window.open(encodeURI(url));
	var usd = new UserSelectDialog("doglobalreaderdialog","650","450",encodeURI(url) ,"流程读者处理窗口");
	usd.show();
}
/**
 * 全局转发选择目标节点后
 * @param activityName
 */
function doglobaltransmitActivitySelect(processInstanceId,executionId,taskId,outcome,targetActivityName){
	jQuery("#doglobaltransmit").window("close");
	setDataObj(processInstanceId,executionId,taskId,outcome,"doglobaltransmit",targetActivityName);
	var b = ButtonProcessing.ButtonPreviousProcessing("doglobaltransmit",processInstanceId,executionId,framework.bpm.processActivityName,targetActivityName,framework.form.formId);
	//获取表单data的json
	getFormData();
	var para = "procinstDbid="+processInstanceId+"&executionId="+executionId+"&taskId="+taskId+"&outcome="+outcome+"&targetActivityName="+targetActivityName+"&type=doglobaltransmit&doSubmitUrl=" + escape("platform/bpm/clientbpmoperateaction/doglobaltransmit") + "&doSubmitCallEvent=globaltransmitBack";
	var usd = new UserSelectDialog("userselectdialog","650","480",getPath() + "/platform/user/bpmSelectUserAction/main?"+para,"流程全局转发处理窗口");
	var buttons = [{
		text:'提交',
		id : 'processSubimt',
		//iconCls : 'icon-submit',
		handler:function(){
			var frmId = $('#userselectdialog iframe').attr('id');
			var frm = document.getElementById(frmId).contentWindow;
			var seleUserJson = frm.getSelectedResultDataJson();
			if(typeof(seleUserJson) != 'undefined'){
				$('#processSubimt').linkbutton('disable');
				easyuiMask();
				var data = "executionId="+executionId+"&userJson="+seleUserJson;
				ajaxRequest("POST",data,"platform/bpm/clientbpmoperateaction/doglobaltransmit","json","globaltransmitBack");
			}
		}
	}];
	if(b){
		usd.createButtonsInDialog(buttons);
		usd.show();
	}
}
/**
 * 全局转发
 */
function globaltransmitBack(obj){
	var dataObj = map.get("doglobaltransmit");
	ButtonProcessing.ButtonPostProcessing("doglobaltransmit",dataObj.procinstDbid,dataObj.executionId,framework.bpm.processActivityName,dataObj.targetActivityName,framework.form.formId);
	//window.close();
	window.location.reload();
}


/**
 * 流程读者
 */
function doglobalreader(processInstanceId,executionId,taskId,outcome,targetActivityName){
	//alert(para);
	var url = getPath()+"/avicit/platform6/bpmclient/bpm/ProcessGraph.jsp";
	url += "?processInstanceId="+processInstanceId+"&executionId="+executionId+"&taskId="+taskId+"&outcome="+outcome+"&targetActivityName="+targetActivityName+"&type=doglobalreader";
	//window.open(encodeURI(url));
	var usd = new UserSelectDialog("doglobalreaderdialog","650","450",encodeURI(url) ,"流程读者处理窗口");
	usd.show();
}
/**
 * 流程读者选择目标节点后
 * @param activityName
 */
function doreaderActivitySelect(procinstDbid,executionId,taskId,outcome,targetActivityName){
	//alert('读者选择目标节点');
	jQuery("#doglobalreaderdialog").window("close");
	setDataObj(procinstDbid,executionId,taskId,outcome,"doglobalreader",targetActivityName);
	var b = ButtonProcessing.ButtonPreviousProcessing("doglobalreader",procinstDbid,executionId,framework.bpm.processActivityName,targetActivityName,framework.form.formId);
	//获取表单data的json
	getFormData();
	var para = "procinstDbid="+procinstDbid+"&executionId="+executionId+"&taskId="+taskId+"&outcome="+outcome+"&targetActivityName="+targetActivityName+"&type=doglobalreader&doSubmitUrl=" + escape("platform/bpm/clientbpmoperateaction/doreader") +  "&doSubmitCallEvent=globalreaderBack&random="+Math.random();
	var usd = new UserSelectDialog("userselectdialog","650","480",getPath() + "/platform/user/bpmSelectUserAction/main?"+para,"流程处理窗口");
	var buttons = [{
		text:'提交',
		id : 'processSubimt',
		//iconCls : 'icon-submit',
		handler:function(){
			var frmId = $('#userselectdialog iframe').attr('id');
			var frm = document.getElementById(frmId).contentWindow;
			var seleUserJson = frm.getSelectedResultDataJson();
			if(typeof(seleUserJson) != 'undefined'){
				$('#processSubimt').linkbutton('disable');
				easyuiMask();
				var data = "processInstanceId="+procinstDbid+"&executionId="+executionId+"&activityName="+targetActivityName+"&userJson=" + seleUserJson;
				ajaxRequest("POST",data,"platform/bpm/clientbpmoperateaction/doreader","json","globalreaderBack");
			}
		}
	}];
	if(b){
		usd.createButtonsInDialog(buttons);
		usd.show();
	}
}

/**
 * 流程读者
 */
function globalreaderBack(obj){
	var dataObj = map.get("doglobalreader");
	ButtonProcessing.ButtonPostProcessing("doglobalreader",dataObj.procinstDbid,dataObj.executionId,framework.bpm.processActivityName,dataObj.targetActivityName,framework.form.formId);
	//window.close();
	window.location.reload();
}


/**
 *  增发
 */
function doadduser(procinstDbid,executionId,taskId,outcome,targetActivityName){
	setDataObj(procinstDbid,executionId,taskId,outcome,"doadduser",targetActivityName);
	var b = ButtonProcessing.ButtonPreviousProcessing("doadduser",procinstDbid,executionId,framework.bpm.processActivityName,targetActivityName,framework.form.formId);
	getFormData();
	var para = "procinstDbid="+procinstDbid+"&executionId="+executionId+"&taskId="+taskId+"&outcome="+outcome+"&targetActivityName="+targetActivityName+"&type=doadduser&doSubmitUrl=" + escape("platform/bpm/clientbpmoperateaction/dosupplementassignee") +  "&doSubmitCallEvent=adduserBack&random="+Math.random();
	var usd = new UserSelectDialog("doadduserdialog","650","480",getPath() + "/platform/user/bpmSelectUserAction/main?"+para,"流程增发窗口");
	var buttons = [{
		text:'提交',
		id : 'processSubimt',
		//iconCls : 'icon-submit',
		handler:function(){
			var frmId = $('#doadduserdialog iframe').attr('id');
			var frm = document.getElementById(frmId).contentWindow;
			var seleUserJson = frm.getSelectedResultDataJson();
			if(typeof(seleUserJson) != 'undefined'){
				$('#processSubimt').linkbutton('disable');
				easyuiMask();
				var data = "executionId="+executionId+"&userJson="+seleUserJson;
				ajaxRequest("POST",data,"platform/bpm/clientbpmoperateaction/dosupplementassignee","json","adduserBack");
			}
		}
	}];
	if(b){
		usd.createButtonsInDialog(buttons);
		usd.show();
	}
}

function adduserBack(obj){
	var dataObj = map.get("doadduser");
	ButtonProcessing.ButtonPostProcessing("doadduser",dataObj.procinstDbid,dataObj.executionId,framework.bpm.processActivityName,dataObj.targetActivityName,framework.form.formId);
	//alert("增发成功");
	//window.close();
	window.location.reload();
}

/**
 * 填写意见
 */
function dowriteidea(procinstDbid,executionId,taskId,outcome,targetActivityName){
	setDataObj(procinstDbid,executionId,taskId,outcome,"dowriteidea",targetActivityName);
	var para = "procinstDbid="+procinstDbid+"&executionId="+executionId+"&taskId="+taskId+"&outcome="+outcome+"&targetActivityName="+targetActivityName+"&type=dowriteidea&doSubmitUrl=" + escape("platform/bpm/clientbpmoperateaction/dowriteidea") +  "&doSubmitCallEvent=writeideaBack&random="+Math.random();
	var usd = new UserSelectDialog("userselectdialog","650","480",getPath() + "/platform/user/bpmSelectUserAction/main?"+para,"流程处理窗口");
	var buttons = [{
		text:'提交',
		id : 'processSubimt',
		//iconCls : 'icon-submit',
		handler:function(){
			var frmId = $('#userselectdialog iframe').attr('id');
			var frm = document.getElementById(frmId).contentWindow;
			var seleUserJson = frm.getSelectedResultDataJson();
			if(typeof(seleUserJson) != 'undefined'){
				$('#processSubimt').linkbutton('disable');
				easyuiMask();
				var data = "procinstDbid="+procinstDbid+"&taskId="+taskId+"&userJson="+seleUserJson;
				ajaxRequest("POST",data,"platform/bpm/clientbpmoperateaction/dowriteidea","json","writeideaBack");
			}
		}
	}];
	usd.createButtonsInDialog(buttons);
	usd.show();
}

/**
 * 填写意见回调
 * @param obj
 */
function writeideaBack(obj){
	var dataObj = map.get("dowriteidea");
	ButtonProcessing.ButtonPostProcessing("dowriteidea",dataObj.procinstDbid,dataObj.executionId,framework.bpm.processActivityName,dataObj.targetActivityName,framework.form.formId);
	//window.close();
	window.location.reload();
}


/**
 * 流程结束
 */
function doglobalend(procinstDbid,executionId,taskId,outcome,targetActivityName){
	var b = confirm("流程一旦结束将无法恢复，您确定结束该流程吗？");
	if(b){
		setDataObj(procinstDbid,executionId,taskId,"","doglobalend");
		var b = ButtonProcessing.ButtonPreviousProcessing("doglobalend",procinstDbid,executionId,framework.bpm.processActivityName,targetActivityName,framework.form.formId);
		var data = "executionId="+executionId;
		var url = "platform/bpm/clientbpmoperateaction/doend";
		var event = "endBack";
		if(b){
			ajaxRequest("POST",data,url,"json",event);
		}
	}
}
/**
 * 流程完成回调
 */
function endBack(obj){
	var dataObj = map.get("doglobalend");
	ButtonProcessing.ButtonPostProcessing("doglobalend",dataObj.procinstDbid,dataObj.executionId,framework.bpm.processActivityName,dataObj.targetActivityName,framework.form.formId);
	//alert("流程完成成功");
	//window.close();
	window.location.reload();
}

/**
 * 流程暂停
 */
function doglobalsuspend(procinstDbid,executionId,taskId,outcome,targetActivityName){
	var b = confirm("您确定暂停该流程吗？");
	if(b){
		setDataObj(procinstDbid,executionId,taskId,"","doglobalsuspend");		
		var b = ButtonProcessing.ButtonPreviousProcessing("doglobalsuspend",procinstDbid,executionId,framework.bpm.processActivityName,targetActivityName,framework.form.formId);
		var data = "processInstanceId="+procinstDbid;
		var url = "platform/bpm/clientbpmoperateaction/dosuspend";
		var event = "suspendBack";
		if(b){
			ajaxRequest("POST",data,url,"json",event);
		}
	}
}
/**
 * 流程暂停回调
 */
function suspendBack(obj){
	var dataObj = map.get("doglobalsuspend");
	ButtonProcessing.ButtonPostProcessing("doglobalsuspend",dataObj.procinstDbid,dataObj.executionId,framework.bpm.processActivityName,dataObj.targetActivityName,framework.form.formId);
	//alert("流程暂停成功");
	//window.close();
	window.location.reload();
}

/**
 * 流程恢复
 */
function doglobalresume(procinstDbid,executionId,taskId,outcome,targetActivityName){
	var b = confirm("您确定恢复该流程吗？");
	if(b){
		setDataObj(procinstDbid,executionId,taskId,"","doglobalresume");
		var b = ButtonProcessing.ButtonPreviousProcessing("doglobalresume",procinstDbid,executionId,framework.bpm.processActivityName,targetActivityName,framework.form.formId);
		var data = "processInstanceId="+procinstDbid;
		var url = "platform/bpm/clientbpmoperateaction/doresume";
		var event = "resumeBack";
		if(b){
			ajaxRequest("POST",data,url,"json",event);
		}
	}
}
/**
 * 流程恢复回调
 */
function resumeBack(obj){
	var dataObj = map.get("doglobalresume");
	ButtonProcessing.ButtonPostProcessing("doglobalresume",dataObj.procinstDbid,dataObj.executionId,framework.bpm.processActivityName,dataObj.targetActivityName,framework.form.formId);
	//window.close();
	window.location.reload();
}


/**
 * 修改意见
 */
function doglobalidea(processInstanceId,executionId,taskId,outcome,targetActivityName){
	setDataObj(processInstanceId,executionId,taskId,outcome,"doglobalidea",targetActivityName);
	ButtonProcessing.ButtonPreviousProcessing("doglobalidea",processInstanceId,executionId,framework.bpm.processActivityName,targetActivityName,framework.form.formId);
	getFormData();
	var url = getPath()+"/avicit/platform6/bpmclient/bpm/ProcessIdea.jsp";
	url += "?processInstanceId="+processInstanceId;
	//window.open(encodeURI(url));
	var usd = new UserSelectDialog("doglobalideadialog","800","400",encodeURI(url) ,"流程意见修改窗口");
	usd.show();
}
/**
 * 保存表单接口
 */
function doformsave(processInstanceId,executionId,taskId,outcome,targetActivityName){
	ButtonProcessing.ButtonPreviousProcessing("doformsave",processInstanceId,executionId,framework.bpm.processActivityName,targetActivityName,framework.form.formId);
	if (window.saveFormData) {
		window.saveFormData(processInstanceId,executionId);
	}
}
/**
 * 创建正文
 */
function dowordcreate(processInstanceId,executionId,taskId,outcome,targetActivityName){
	ButtonProcessing.ButtonPreviousProcessing("dowordcreate",processInstanceId,executionId,framework.bpm.processActivityName,targetActivityName,framework.form.formId);
	getFormData();
	var url = getPath()+"/avicit/platform6/bpmclient/word/WordTempletSelect.jsp";
		url += "?processInstanceId="+processInstanceId+"&executionId="+executionId+"&type=wordCreate";
	var usd = new UserSelectDialog("worddialog","400","450",encodeURI(url) ,"正文模版");
	usd.show();
}

function openWordWindow(processInstanceId, executionId, templetId, type){
	$('#worddialog').dialog('close');
	var url = "";
	if (type == "wordCreate") {
		url = getPath()+"/avicit/platform6/bpmclient/word/WordCreate.jsp";
		url += "?processInstanceId="+processInstanceId+"&executionId="+executionId+"&templetId="+templetId;
	} else if (type == "wordRedTemplet"){
		url = getPath()+"/avicit/platform6/bpmclient/word/WordRedTemplet.jsp";
		url += "?processInstanceId="+processInstanceId+"&executionId="+executionId+"&templetId="+templetId;
	} else {
		alert("操作类型不能为空！");
	}
	if (null != url && url.length > 1) {
		var sOrnaments = "dialogHeight:"+window.screen.availHeight+"px;dialogWidth:"+window.screen.availWidth+"px;center:yes;status:0;help:0;edge:raised;scroll:no";
		var vFreeArgument = new Array(); vFreeArgument[0] = window;
		window.showModalDialog(url,vFreeArgument,sOrnaments);
		window.location.reload();
	}
}
/**
 * 编辑正文
 */
function dowordedit(processInstanceId,executionId,taskId,outcome,targetActivityName){
	ButtonProcessing.ButtonPreviousProcessing("dowordedit",processInstanceId,executionId,framework.bpm.processActivityName,targetActivityName,framework.form.formId);
	getFormData();
	var url = getPath()+"/avicit/platform6/bpmclient/word/WordEdit.jsp";
		url += "?processInstanceId="+processInstanceId+"&executionId="+executionId;
	var sOrnaments = "dialogHeight:"+window.screen.availHeight+"px;dialogWidth:"+window.screen.availWidth+"px;center:yes;status:0;help:0;edge:raised;scroll:no";
	var vFreeArgument = new Array(); vFreeArgument[0] = window;
	window.showModalDialog(url,vFreeArgument,sOrnaments);
}

/**
 * 查看正文
 */
function dowordread(processInstanceId,executionId,taskId,outcome,targetActivityName){
	ButtonProcessing.ButtonPreviousProcessing("dowordread",processInstanceId,executionId,framework.bpm.processActivityName,targetActivityName,framework.form.formId);
	getFormData();
	var url = getPath()+"/avicit/platform6/bpmclient/word/WordRead.jsp";
		url += "?processInstanceId="+processInstanceId+"&executionId="+executionId;
	var sOrnaments = "dialogHeight:"+window.screen.availHeight+"px;dialogWidth:"+window.screen.availWidth+"px;center:yes;status:0;help:0;edge:raised;scroll:no";
	var vFreeArgument = new Array(); vFreeArgument[0] = window;
	window.showModalDialog(url,vFreeArgument,sOrnaments);
}

/**
 * 套红
 */
function dowordredtemplet(processInstanceId,executionId,taskId,outcome,targetActivityName){
	ButtonProcessing.ButtonPreviousProcessing("dowordredtemplet",processInstanceId,executionId,framework.bpm.processActivityName,targetActivityName,framework.form.formId);
	getFormData();
	var url = getPath()+"/avicit/platform6/bpmclient/word/WordTempletSelect.jsp";
		url += "?processInstanceId="+processInstanceId+"&executionId="+executionId+"&type=wordRedTemplet";
	var usd = new UserSelectDialog("worddialog","400","450",encodeURI(url) ,"红头模版");
	usd.show();
}
/**
 * 自定义流程审批人
 */

function dostepuserdefined(processInstanceId,executionId,taskId,outcome,targetActivityName){
	var url = getPath()+"/avicit/platform6/bpmclient/bpm/ProcessUserDefinedGraph.jsp";
	//url += "?processInstanceId="+processInstanceId+"&taskId="+taskId+"&outcome="+outcome;
	url += "?processInstanceId="+processInstanceId+"&executionId="+executionId+"&taskId="+taskId+"&outcome="+outcome+"&targetActivityName="+targetActivityName;
	top.addTab("自定义审批人",encodeURI(url),"dorado/client/skins/~current/common/icons.gif","taskTodo"," -0px -120px");
	//var usd = new UserSelectDialog("douserdefineddialog","700","450",encodeURI(url) ,"自定义流程审批人");
	//usd.show();
}

/*******************************************启动流程弹出选择流程框*************************************************/

//function start(obj){
//	if(obj != null && obj.length == 1){
//		var processDefinitionId = obj[0].dbid;
//		new StartProcessByFormCode().doStart(processDefinitionId);
//	}else{
//		US_GIALOG =	new StartProcessByFormCode().openSelectProcessDialog();
//	}
//}

function sendUrl(processInstanceId){
	var data = "processInstanceId="+processInstanceId;
	ajaxRequest("POST",data,"platform/bpm/clientbpmdisplayaction/getprocessurl","json","back1");
}
function back1(obj){
	if(US_GIALOG != ""){
		US_GIALOG.close();
	}
	//window.open(getPath()+"/"+FormProxyPage.url+obj.url,"代理页面","scrollbars=no,status=yes,resizable=no,top=0,left=0,width="+(screen.availWidth-10)+",height="+(screen.availHeight-30));
	//window.open(getPath()+"/"+FormProxyPage.url+obj.url);
	var url = obj.url;
	//debugger;
	var proxyPage = "N"; //是否做页面代理
	if(url.indexOf('&url=') > -1){
		var u = decodeURIComponent(url.substr(url.indexOf('&url='),url.length));
		if(u!=null&&u.indexOf("proxyPage=Y")!=-1){//是否做页面代理
			proxyPage = "Y";
		}
	}
	
    if (proxyPage != "Y") { //不明确指定用代理页面的，则通通跳转到自己页面
    	//var winurl = getPath()+"/avicit/platform6/bpmclient/bpm/Button.jsp?entryId="+entryId+"&executionId="+executionId+"&taskId="+taskId+"&formId="+formId;
    	objWin__ = window.open(url,"操作列表","scrollbars=no,status=yes,resizable=no,top=0,left=0,width="+(screen.availWidth-10)+",height="+(screen.availHeight-30));
    	return ;
    }
    //以下都是采用代理页面的
    var redirectPath = getPath()+"/avicit/platform6/bpmclient/bpm/ProcessApprove.jsp" + url;
    /*
     * 如果URL中已经包含?,比如iframe?url=pmo/bowf/projnotice/process,则在后面用 & 拼接参数，否则用?拼接参数
     */
    //window.open(redirectPath);
     objWin__ = window.open(redirectPath,"代理页面","scrollbars=no,status=yes,resizable=no,top=0,left=0,width="+(window.screen.availWidth-10)+",height="+(window.screen.availHeight-30));
	return ;
	
}
/**
 * 表单对应流程选择并启动流程(窗体调用函数)
 */
var FORMCODE = "";
var US_GIALOG = "";
var __processList;
var StartProcessByFormCode = function(){
	this.formCode = "";
	this.formData = "";
	var _self = this;
	StartProcessByFormCode.prototype.SetFormCode = function(_formCode){
		this.formCode = _formCode;
		FORMCODE = _formCode;
	};
	StartProcessByFormCode.prototype.SetFormData = function(_formData){
		this.formData = _formData;
	};
	this.start = function (){
		//var paras = "formCode="+this.formCode+"&formData="+this.formData;
		//ajaxRequest("POST",paras,"platform/bpm/clientbpmdisplayaction/getprocessbyformcode","json","this.startBack");
		var url = "platform/bpm/clientbpmdisplayaction/getprocessbyformcode";
		var contextPath = getPath();
		var urltranslated = contextPath + "/" + url;
		jQuery.ajax({
	        type:"POST",
			data:"formCode="+this.formCode+"&formData="+this.formData,
	        url: urltranslated,  
	        dataType:"json",
	        async:false,
			context: document.body, 
	        success: function(obj){
	        	if(obj != null && obj.length == 1){
	    			var processDefinitionId = obj[0].dbid;
	    			new StartProcessByFormCode().doStart(processDefinitionId);
	    		}else{
	    			_self.openSelectProcessDialog(obj);
	    		}
			},
			error: function(msg){}
		}); 	
	};
	
	this.openSelectProcessDialog = function(obj){
		__processList = obj; //付给页面变量为选流程页面使用
		var dialogUrl=getPath()+"/avicit/platform6/bpmclient/bpm/ProcessDefinitionList.jsp";
		dialogUrl += "?formCode="+this.formCode;
		var usd = new UserSelectDialog("startProcessByFormCodedialog","400","400",encodeURI(dialogUrl) ,"流程定义选择");
		var buttons = [{
 			text:'提交',
 			id : 'tj',
 			//iconCls : 'icon-submit',
 			handler:function(){
 				var frm = document.getElementById("_iframe_startProcessByFormCodedialog").contentWindow;
 				var processDefInfo = frm.$('#Process_Definition_data').datagrid('getSelected');
 				if(processDefInfo == null){
 					return ;
 				}
 				var processDefinitionId = processDefInfo.dbid;
 				var processName = processDefInfo.name;
 				//请求操作
 				if(confirm('你确定要启动['+processName+']流程吗?')){
 					new StartProcessByFormCode().doStart(processDefinitionId);
 					usd.close();
 				}
 			}
 		}];
		usd.createButtonsInDialog(buttons);
		usd.show();
		return usd;
	};
};
/**
 * 设置我的流程和全部流程按钮选中
 * @param type
 * @param divId
 */
function setBpmMenuState(state, type){
	var text = $('#'+type+"_"+state).text();
	if(type!=null&&type=="all"){
		$('#allMenu').css('background-color','#B2DFEE'); 
		$('#myMenu').css('background-color','#F2F2F2');
		$('#myMenu').menubutton({text:'我的全部'}); 
		$('#allMenu').menubutton({text:text}); 
	}else if(type!=null&&type=="my"){
		$('#myMenu').css('background-color','#B2DFEE'); 
		$('#allMenu').css('background-color','#F2F2F2');
		$('#allMenu').menubutton({text:'全部文件'}); 
		$('#myMenu').menubutton({text:text}); 
	}
	$("div[name^='bpm_']").removeClass('menu_selected');
	$('#'+type+"_"+state).addClass('menu_selected');
	
	if(state == "start" && type == "my"){
		$("#tool_del_td").show();
		$("#tool_edit_td").show();
		
	}else{
		jQuery.ajax({
	        type:"POST",
	        url: "platform/bpm/clientbpmdisplayaction/isAdmin",  
	        dataType:"json",
	        async:true,
	        success: function(obj){
	        	if(obj != null && obj.isAdmin == "true"){
	        		$("#tool_del_td").show();
	        		$("#tool_edit_td").show();
	    		}else{
	    			$("#tool_del_td").hide();
	    			$("#tool_edit_td").hide();
	    		}
			},
			error: function(msg){
				$("#tool_del_td").hide();
				$("#tool_edit_td").hide();
			}
		}); 
		
	}
}

//初始化流程按钮及意见
function initBpmInfo(entryId,executionId,taskId,formId){
	if(entryId==null||entryId==""||entryId=='null'){
		var bpmInfo = getProcessTaskParameter(formId);
		if(bpmInfo!=null){
			entryId = bpmInfo[0];
			executionId = bpmInfo[1];
			taskId = bpmInfo[2];
		}
	}
	if(entryId!=null&&entryId!=""){
		//画流程按钮
		var toolbar = new ToolBar(entryId,executionId, taskId, "bpmToolBar", formId);
		//画流审批意见
		new IdeaManage.getIdea(entryId);
	}
}

//初始化流程按钮及意见带流程表单控制
function initBpmInfoAndFormAccess(entryId,executionId,taskId,formId){
	if(entryId==null||entryId==""||entryId=='null'){
		var bpmInfo = getProcessTaskParameter(formId);
		if(bpmInfo!=null){
			entryId = bpmInfo[0];
			executionId = bpmInfo[1];
			taskId = bpmInfo[2];
		}
	}
	if(entryId!=null&&entryId!=""){
		//画流程按钮
		var toolbar = new ToolBar(entryId,executionId, taskId, "bpmToolBar", formId, true);
		//画流审批意见
		new IdeaManage.getIdea(entryId);
		//流程表单权限控制(移动到ToolBar按钮中进行判断)
		//processFormAccess(entryId,executionId);
	}
}
/**
 * 获取流程表单权限
 * @param entryId
 * @param executionId
 */
function processFormAccess(entryId,executionId,isCurrentOperUser,canFormSave){
	var url = "platform/bpm/bpmPublishAction/getProcessJspFormSecret";
	var contextPath = getPath();
	var urltranslated = contextPath + "/" + url;
	jQuery.ajax({
        type:"POST",
		data:"entryId="+entryId+"&executionId="+executionId,
        url: urltranslated,  
        dataType:"json",
        async:false,
        success: function(obj){
        	if(obj!=null && obj.fomSecurityList != null && obj.fomSecurityList.length>0){
        		//进行表单权限控制
        		for(var i=0;i<obj.fomSecurityList.length;i++){
        			var tag = obj.fomSecurityList[i].tag;
        			var tagName = obj.fomSecurityList[i].tagName;
        			var	accessibility = 1;
        			if (isCurrentOperUser == 1) {
        				accessibility = obj.fomSecurityList[i].accessibility;//0表示隐藏
        			}
        			var	operability = obj.fomSecurityList[i].operability;//0表示只读
        			if (canFormSave == false) {//如果不能保存,则所有样式都是只读
        				operability = "0";
        			}
        			formControl(tag,tagName,accessibility,operability);
        		}
        	}
        	setTimeout('checkAfterInitBpmInfoAndFormAccessFunction()', 800);
		},
		error: function(msg){}
	}); 	
}

function checkAfterInitBpmInfoAndFormAccessFunction() {
	//执行页面后处理函数
	if (window.afterInitBpmInfoAndFormAccess) {
		afterInitBpmInfoAndFormAccess();
	}
}

/**
 * 控制jsp权限
 * @param tag
 * @param tagName
 * @param accessibility
 * @param operability
 */
function formControl(tag,tagName,accessibility,operability){
	/**
	if(operability==0){
		var isDate =  $('#' + tag).hasClass("easyui-datebox");
		if(isDate){
			$('#' + tag).datebox({disabled: true});
		}else{
			$('#' + tag).attr("disabled","disabled");
			$('#' + tag).attr("readonly","readonly");
		}
	}
	*/
	if(operability==1){
		var isDate =  $('#' + tag).hasClass("easyui-datebox");
		if(isDate){
			$('#' + tag).datebox({disabled: false});
		}else{
			$('#' + tag).removeAttr("disabled");
			$('#' + tag).removeAttr("readonly");
		}
	}
	
	if(accessibility==0){
		//控制日期类型
		var isDate =  $('#' + tag).hasClass("easyui-datebox");
		if(isDate){
			$('#' + tag).datebox("destroy");
		}else{
			$('#' + tag).hide();
		}
		$("label").each(function(){
			   var text = $(this).text();
			   if(text == tagName ){
				   $(this).css("display", "none");
				   $.parser.parse(this);
				   return;
			   }
			});
	}
	
}

/**
 * 通过taskId获取任务对象
 * @param taskId 任务id
 * @return
 */
function getTaskByTaskId(taskId){
	var url = "platform/bpm/clientbpmdisplayaction/getTaskByTaskId";
	var contextPath = getPath();
	var urltranslated = contextPath + "/" + url;
	jQuery.ajax({
        type:"POST",
		data:"taskId="+taskId,
        url: urltranslated,  
        dataType:"json",
        async:true,
        success: function(task){
        	return task;
		},
		error: function(msg){
		}
	}); 	
}
