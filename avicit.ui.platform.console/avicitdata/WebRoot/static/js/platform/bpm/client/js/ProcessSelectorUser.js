/**
 * 条件值
 */
var conditions = {
		//分支
		isBranch : function(){
			if(dataJson.nextTask != null && dataJson.nextTask.length > 1){//分支
				return true;
			}else {//非分支
				return false;
			}
		},
		//选人方式
		isUserSelectTypeAuto : function(branchNo){
			if(typeof(branchNo) == 'undefined'){
				branchNo = 0;
			}
			if(dataJson.nextTask != null && dataJson.nextTask[branchNo].currentActivityAttr.userSelectType){
				if(dataJson.nextTask[branchNo].currentActivityAttr.userSelectType == 'auto'){
					return true;//自动选人方式
				} else {
					return false;
				}
			} else {
				return false;//手动选人方式
			}
		},
		//是否启用工作移交
		isWorkHandUser : function(branchNo){
			if(typeof(branchNo) == 'undefined'){
				branchNo = 0;
			}
			if(dataJson.nextTask != null && dataJson.nextTask[branchNo].currentActivityAttr.isWorkHandUser){
				if(dataJson.nextTask[branchNo].currentActivityAttr.isWorkHandUser == 'no'){
					return false;//启用
				} else {
					return true;//不启用
				}
			} else {//默认
				return true;//不启用
			}
		},
		//处理方式
		getDealType : function(branchNo){
			if(typeof(branchNo) == 'undefined'){
				branchNo = 0;
			}
			if(dataJson.nextTask != null && dataJson.nextTask[branchNo].currentActivityAttr.dealType){
				return dataJson.nextTask[branchNo].currentActivityAttr.dealType;
			}
			return "2";
		},
		//是否必须选人
		isMustUser : function(branchNo){
			if(typeof(branchNo) == 'undefined'){
				branchNo = 0;
			}
			if(dataJson.nextTask != null && dataJson.nextTask[branchNo].currentActivityAttr.isMustUser){
				if(dataJson.nextTask[branchNo].currentActivityAttr.isMustUser == 'no'){//必须选人
					return false;
				} else {
					return true;//必须选人
				}
			} else {
				return true;//默认值
			}
		},
		//是否启用密级
		isSecret : function(branchNo){
			if(typeof(branchNo) == 'undefined'){
				branchNo = 0;
			}
			if(dataJson.nextTask != null && dataJson.nextTask[branchNo].currentActivityAttr.isSecret){
				if(dataJson.nextTask[branchNo].currentActivityAttr.isMustUser == 'yes'){//启用
					return true;
				} else {
					return false;//不启用
				}
			} else {
				return false;//不启用
			}
		}, 
		//意见添写方式
		getIdeaType : function(){
			if(dataJson.currentActivityAttr.ideaType){
				return dataJson.currentActivityAttr.ideaType;
			}
			return "";
		},
		//是否强制表态
		isIdeaCompelManner : function(){
			if(dataJson.currentActivityAttr.ideaCompelManner){
				if(dataJson.currentActivityAttr.ideaCompelManner == 'yes'){//强制
					return true;
				} else {
					return false;//不强制
				}
			} else {
				return false;//不强制
			}
		},
		//是否显示选人框
		isSelectUser : function(branchNo){
			if(typeof(branchNo) == 'undefined'){
				branchNo = 0;
			}
			if(dataJson.nextTask != null && dataJson.nextTask[branchNo].currentActivityAttr.isSelectUser){
				if(dataJson.nextTask[branchNo].currentActivityAttr.isSelectUser == 'no'){//不显示
					return false;
				} else {
					return true;//显示
				}
			} else {
				return true;//显示
			}
		},
		/**
		 * 是否自动获取用户
		 */
		isAutoGetUser : function(branchNo){
			if(typeof(branchNo) == 'undefined'){
				branchNo = 0;
			}
			if(dataJson.nextTask != null && dataJson.nextTask[branchNo].currentActivityAttr.isAutoGetUser){
				if(dataJson.nextTask[branchNo].currentActivityAttr.isAutoGetUser == 'no'){//不显示
					return false;
				} else {
					return true;//是
				}
			} else {
				return false;
			}
		},
		//获取下节点类型
		getNextActivityType : function(){
			return dataJson.activityType;
		}
};

//事件
var processSelectorUserEvent = {
		/**
		 * 获取分支数
		 * @returns
		 */
		getBranchLength : function(){
			if(dataJson.nextTask == null){
				return 0;
			}
			return dataJson.nextTask.length;
		}
};

var settings ;
$(function(){
	//下节点的属性值
	if(dataJson){
		if(dataJson.nextTask){
			if($("#workflowSelectorSource").length > 0 ){
				$("#workflowSelectorSource").append("<div id=\"selectorUserTabs\"></div>");
				var tabs = $("#selectorUserTabs").tabs({  
			        border:false,
			        fit : true ,
			        plain : true
			    });
				//创建选人区域
				var getDatas = new GetDatas();
				if(conditions.isBranch()){//多个分支时
					for(var i = 0 ; i < getDatas.getBranchLength() ; i++){
						var branchNo = i;//分支序号
						tabs.tabs('add',{ 
							id : 'selectorUserTab_' + branchNo,
						    title : getDatas.getNextActivityAlias(branchNo),
						    closable:false,
						    content : '<div class=\"easyui-layout\" fit=\"true\"><div region=\"center\" ><div id=\"selectorUserTab_' + branchNo + '_Content\"></div></div><div region=\"east\" split=\"true\" style=\"width:280px;\"><div id=\"selectorUserTabForTargetDataGrid_' + branchNo + '\"></div></div></div>',
						    iconCls : 'icon-branch'
						});
						var secondLayerTabs = $("#selectorUserTab_" + branchNo + '_Content').tabs({  
					        border:false,
					        fit : true ,
					        plain : true
					    });
						drawSecondLayerTabs(dataJson.nextTask[branchNo],secondLayerTabs,branchNo);//绘制第二层
						drawSecondLayerTabsForTargetDataGrid('selectorUserTabForTargetDataGrid_' + branchNo,branchNo);
					}
					//选中第一标签页
					tabs.tabs('select',0);
				} else { // 非分支
					//绘制目标dataGrid布局区域
					var branchNo = 0;
					drawSecondLayerTabs(dataJson.nextTask[0],tabs,branchNo);
					drawSecondLayerTabsForTargetDataGrid('selectorUserTabForTargetDataGrid_' + branchNo,branchNo);
					//选中第一标签页
					tabs.tabs('select',0);
				}
				
				setTimeout("processSelectUserComponentEvent.eventOfUserSelectType();" +
						"processSelectUserComponentEvent.eventOfIsDisplayUserSelect()",
						888);
				
				//自动获取用户
				if(type == 'dosubmit'){//只是正常提交时
					for(var i = 0 ; i < getDatas.getBranchLength() ; i++){
						if(conditions.isAutoGetUser(i)){
							getDatas.getAutoGetSysUsers(i);
						}
					}
				}
			}
		} else {
			hideUserSelectArea();
//			$.messager.alert('提示','服务端数据错误!','error');
		}
	}
	settings  = {
			saveToCookieKey : 'processSelectorUserCookie_' + outcome + '_'  + processInstanceId +  '_' + taskId + '_' + executionId
	};
	/*
	 * 流程选人和意见框是否显示控制
	 */
	if(type == 'dosubmit'){//正常提交
		var nextActivityType = conditions.getNextActivityType();
		if(nextActivityType){
			if(nextActivityType == 'sub-process'){//子流程
				if(!conditions.isMustUser()){ 
					hideUserSelectArea();
				}
			} else if(nextActivityType == 'foreach'){//分支
				if(!conditions.isMustUser()){ 
					hideUserSelectArea();
				}
			} else if(nextActivityType == "end"){//下一节点为流程流转结束(归档)
				hideUserSelectArea();
				if(conditions.getIdeaType() == 'cannot'){ //意见不能填写,自动提交
					var data = "taskId="+taskId+"&outcome="+outcome+"&formJson=";
					parent.ajaxRequest("POST",data,"platform/bpm/clientbpmoperateaction/dosubmit","json","submitBack");
				}
			} else if(nextActivityType == 'task'){
				//非分支  && 意见不能填写写 && 不显示选人框  && 单人处理方式 && 自动选择用户 
				if(!conditions.isBranch()){
					//如果配置为 不能填写意见&&不显示选人框&&选人方式自动
					if (conditions.getIdeaType() == 'cannot' && !conditions.isSelectUser() &&  conditions.isUserSelectTypeAuto()) {
						//setTimeout(function(){
							var data = "taskId="+taskId+"&outcome="+outcome+"&formJson=" + getSelectedResultDataJson();
							easyuiMask();
							parent.ajaxRequest("POST",data,"platform/bpm/clientbpmoperateaction/dosubmit","json","submitBack");
							parent.$('#userselectdialog').dialog('close');
							
						//},3000);
					} else {
						//意见不能填写
						if (conditions.getIdeaType() == 'cannot') {
							hideCommonIdearArea();
						}
						//不需要显示选人框
						if (!conditions.isSelectUser()) {
							hideUserSelectArea();
						}
					}
				}
			}
		}
	} else if(type == 'doretreattodraft'){ /*退回拟稿人*/
		hideUserSelectArea();
		//意见区域显示,
//		必须填写意见
	} else if(type=='doretreattoprev'){/*退回上一步*/
		//意见区域显示,
//		必须填写意见
		if(!conditions.isBranch()){ //当前有2个以上的分支在流转时,不隐藏选人区域
			hideUserSelectArea();
		}
	} else if(type == 'dotransmit'){//转发
		
	} else if(type == 'dosupersede'){//转办
		
	} else if(type == 'doadduser'){//增发
		
	} else if(type == 'doglobaljump'){//跳转
		
	} else if(type == 'doglobalreader'){//全局读者
		
	} else if(type == 'doglobaltransmit'){//全局转发
		
	} else if(type == 'dowithdrawassignee'){//节点内拿回
		
	} else if(type == 'dosupplement'){//补发
		
	} else if(type =='dowithdraw'){//拿回
		//var frm = document.getElementById('selectorUser').contentWindow;
		//setTimeout(function(){
			//var getDatas = new GetDatas();
			//processSelectUserComponentEvent.eventLoadDataToSelectDataGrid(getDatas.getUserList(0),0);
		//},1000);
		hideUserSelectArea();
		return;
	} else if(type == 'dowriteidea'){//填写意见
		hideUserSelectArea();
	}else if(type == 'dostepuserdefined'){
		processSelectUserComponentEvent.eventLoadDataToSelectDataGridByUserDefined(mySelectUser,0);
		hideCommonIdearArea();
	}
	//判断意见填写区域是否显示dataJson.nextTask
	if(type != 'doretreattodraft' && type != 'doretreattoprev' && type != 'dowriteidea' && dataJson.nextTask!=null &&  conditions.getIdeaType() == 'cannot'){
		hideCommonIdearArea();
	}
});
/**
 * 隐藏选人区域
 */
function hideUserSelectArea(){
	//alert('hideUserSelectArea');
	var tempheight = $('#selectorUserlayout').height();
	$('#selectorUserlayout').layout("panel", "south").panel("resize",{height:tempheight});
	$('#selectorUserlayout').layout("resize");
	$('#textAreaIdeas').css('height','300px');
	$('#workflowSelectorSource').css('height','0px');
	$('#selectedResultCompel').css('height','0px');
}
/**
 * 隐藏意见区域
 */
function hideCommonIdearArea(){
	$('#selectorUserlayout').layout("panel", "south").panel("resize",{height:1});
	$('#selectorUserlayout').layout("resize");
}
/**
 * 获取 select user 结果
 */
function getSelectedResultDataJson(){
	//是否填写意见判断
	if(!doIdeaCheck()){ 
		return;
	}
	var selectedUsers  = getSelectedUserDataJson(); //选人结果
	//判断是否必须选人
	if(!doUserSelectCheck(selectedUsers)){ 
		return;
	}
	if(type == 'dosubmit'){//正常提交下,才对强制表态进行判断
		//判断是否强制表态
		if(!doIdearCompelMannerCheck()){ 
			return;
		}
	}
	var returnValue;
	//判断是否允许工作移交 && 是否最后一个处理人
	if(dataJson.lastDealUser && conditions.isWorkHandUser()){
		returnValue = getWorkHandUsersList();
	} else {
		returnValue =  putTogetherSelectedResultDataJson(null);
	}
	return returnValue;
}
/**
 * 获取已选中的用户
 * @returns json
 */
function getSelectedUserDataJson(){
	//var frm = document.getElementById('selectorUser').contentWindow;
	if(dataJson){
		if(dataJson.nextTask){
			if(conditions.isBranch()){//分支时https://github.com/login?return_to=%2Fdouglascrockford%2FJSON-js
				try{
					var selectedUsers = new Array();
					for(var i = 0 ; i < dataJson.nextTask.length ; i++){
						var branchNo = i;//分支序号
						var dataSetSelectedUser =  $('#selectorUserTabForTargetDataGrid_' + branchNo).datagrid('getData');
						if(dataSetSelectedUser.rows.length == 0){
							selectedUsers.push(new Array());
						} else {
							selectedUsers.push(dataSetSelectedUser.rows);
						}
					}
					return selectedUsers;
				}catch(e){
					return null;
				}
			} else {//
				try{
					var dataSetSelectedUser =  $('#selectorUserTabForTargetDataGrid_0').datagrid('getData');
					var selectedUser = dataSetSelectedUser.rows;
					var selectedUsers  = JSON.stringify(selectedUser); //选人结果
					return selectedUsers;
				}catch(e){
					return null;
				}
			}
		}
	}
}
/**
 * 流程提交
 * workhandUsers 工作移交人
 */
function putTogetherSelectedResultDataJson(workhandUsers){
	//var commonIdearFrm = document.getElementById("commonIdear").contentWindow;
	//var selectUserFrm = document.getElementById("selectorUser").contentWindow;
	var selectedUsers  = getSelectedUserDataJson(); //选人结果
	if(typeof(selectedUsers) != 'undefined'){
		try{
			selectedUsers = JSON.parse(selectedUsers);
		}catch(e){}
	} else {
		selectedUsers = null;
	}
//	alert(selectedUsers);
	var ideaValue = $('#textAreaIdeas').val();
	if(typeof(ideaValue) == 'undefined'){
		ideaValue = '';
	}
	var selectedUserAndIdea;
	if (conditions.isBranch()) {
//		var users = [{selectedUsers: selectedUsers,targetActivityName:targetActivityName,outcome:outcome,workhandUsers:workhandUsers},{selectedUsers: selectedUsers,targetActivityName:targetActivityName,outcome:outcome,workhandUsers:workhandUsers}]
		var users = new Array();
		for(var i = 0 ; i < dataJson.nextTask.length ; i++){
			var branchNo = i;//分支序号
			var dataSetSelectedUser =  $('#selectorUserTabForTargetDataGrid_' + branchNo).datagrid('getData');
			var branch = {
					selectedUsers : dataSetSelectedUser.rows,
					targetActivityName : dataJson.nextTask[branchNo].currentActivityAttr.activityName,
					outcome : outcome,
					workhandUsers : workhandUsers
			};
			users.push(branch);
		}
	 	selectedUserAndIdea = {
    		users: users,
    		compelManner : $('#ideaCompelManner').val(),
    		idea: ideaValue,
			outcome: outcome
		}; 
	}else{
		selectedUserAndIdea = {
			users : [{selectedUsers: selectedUsers,targetActivityName:targetActivityName,outcome:outcome,workhandUsers:workhandUsers}],
    		idea: ideaValue,
    		compelManner : $('#ideaCompelManner').val(),
			outcome: outcome
		};
	}

	//转换为字符串
	var jsonString = JSON.stringify(selectedUserAndIdea);
	return jsonString;
}
/**
 * 判断是否必须选人
 * @returns {Boolean}
 */
function doUserSelectCheck(selectedUsers){
	if(conditions.isBranch()){ //分支 选人判断
		if(typeof(selectedUsers) != 'undefined' && selectedUsers instanceof Array){
			var getDatas = new GetDatas();
			
			//拼接必填用户的分支页签字符串
			var mustUserTabs = "";
			for(var i = 0 ; i < getDatas.getBranchLength() ; i++){
				if(conditions.isMustUser(i)){ 
					mustUserTabs += "【"+dataJson.nextTask[i].currentActivityAttr.activityAlias+"】"
				}
			}
			
			for(var i = 0 ; i < getDatas.getBranchLength() ; i++){
				var selectedUser = selectedUsers[i];
				if(selectedUser){
					if(!isSelectUserCheck(selectedUser,i,mustUserTabs)){
						return false;
					}
				}
			}
			return true;
		} else {
			$.messager.alert('提示','请选择流程处理人。','error');
			return false;
		}
	} else { //非分支
		if (typeof(type) != 'undefined' && type != null && type == "dosubmit") { //正常提交
			if(dataJson.lastDealUser){//最后一个处理人
				var nextActivityType = dataJson.activityType; //下一节点是否task节点
				if (typeof(nextActivityType) != 'undefined' && nextActivityType != null && nextActivityType == "task") {
					return isSelectUserCheck(selectedUsers);
				}else if (nextActivityType != null && nextActivityType == "sub-process") { //子流程
					var nextIsMustUser = dataJson.nextTask[0].currentActivityAttr.isMustUser;
					if(nextIsMustUser!=null && nextIsMustUser=="yes"){ 
						return isSelectUserCheck(selectedUsers);
					}
				}else if (nextActivityType != null && nextActivityType == "foreach") { //循环节点
					var nextIsMustUser = dataJson.nextTask[0].currentActivityAttr.isMustUser;
					if(nextIsMustUser != null && nextIsMustUser=="yes"){ 
						return isSelectUserCheck(selectedUsers);
					}
				}
			} else {//不是最后一个处理人
				return true;
			}
		}else if(typeof(type) != 'undefined' && type !=null && (type == "doretreattoprev" || type == "dowithdraw")){//退回上一步,拿回流程
			var curExecutionNum = dataJson.curExecutionNum;
			if(typeof(curExecutionNum) != 'undefined' && curExecutionNum != null && parseInt(curExecutionNum) == 1){ //分支情况下，最后一个拿回时
				if (selectedUsers == null || selectedUsers == "" || selectedUsers == "[]") {
					$.messager.alert('提示','请选择流程处理人。','error');
	            	return false;
	         	}
			}
		}else if(typeof(type) != 'undefined' && type != null && ( type == "dotransmit" || type == "dosupersede" || type == "doadduser" || type == "doglobaljump" || type == "doglobalreader" || type == "doglobaltransmit" || type == "doretreattodraft" || type == "dosupplement")){//转发，转办,增发,跳转, 全局读者，全局转发,退回拟稿人,补发
		 	if (selectedUsers == null || selectedUsers == "" || selectedUsers == "[]") {
	        	$.messager.alert('提示','请选择流程处理人。','error');
	            return false;
	         }
		}
	}
	return true;
}
/**
 * 是否必须选人
 */
function isSelectUserCheck(selectedUsers,branchNo,mustUserTabs){
	if(typeof(branchNo) == 'undefined'){
		branchNo = 0;
	}
	if(conditions.isBranch()){//分支判断
		if(conditions.isMustUser(branchNo)){//判断是否必须选人
			var curDealType = conditions.getDealType(branchNo);
			if (curDealType == "1" || curDealType == "3") { //单人处理1，多人任意3
				//alert('单人处理1，多人任意3 selectedUsers = ' + selectedUsers);
				if (selectedUsers == null || selectedUsers == "" || selectedUsers == "[]") {
					if (typeof(mustUserTabs) != 'undefined') {
						$.messager.alert('提示',mustUserTabs+'必须选人。','error');	
					} else {
						$.messager.alert('提示','请选择流程处理人。','error');
					}
					return false;
				}
			}else if (curDealType == "2" || curDealType == "4") { //多人顺序2，多人并行4
				var curIsLastDealUser = dataJson.lastDealUser;
				if (curIsLastDealUser != null && (curIsLastDealUser == true || curIsLastDealUser == "true")) { //该人是最后1个处理人
					if (selectedUsers == null || selectedUsers == "" || selectedUsers == "[]") {
						if (typeof(mustUserTabs) != 'undefined') {
							$.messager.alert('提示',mustUserTabs+'必须选人。','error');	
						} else {
							$.messager.alert('提示','请选择流程处理人。','error');
						}
						return false;
					}
				}
			}
		}
	} else {//非分支
		var curDealType = conditions.getDealType(branchNo);
		if (curDealType == "1" || curDealType == "3") { //单人处理1，多人任意3
			//alert('单人处理1，多人任意3 selectedUsers = ' + selectedUsers);
			if (selectedUsers == null || selectedUsers == "" || selectedUsers == "[]") {
				$.messager.alert('提示','请选择流程处理人。','error');
				return false;
			}
		}else if (curDealType == "2" || curDealType == "4") { //多人顺序2，多人并行4
			//alert('curDealType == "2/3" selectedUsers = ' + selectedUsers);
			var curIsLastDealUser = dataJson.lastDealUser;
			if (curIsLastDealUser != null && (curIsLastDealUser == true || curIsLastDealUser == "true")) { //该人是最后1个处理人
				if (selectedUsers == null || selectedUsers == "" || selectedUsers == "[]") {
					$.messager.alert('提示','请选择流程处理人。','error');
					return false;
				}
			}
		}
	}
	return true;
}
/**
 * 是否填写意见判断
 * @returns {Boolean}
 */
function doIdeaCheck(){
	//var frm = document.getElementById("commonIdear").contentWindow;
	var curIdeaType = conditions.getIdeaType(); //意见处理类型
	var splitFlag = dataJson.currentActivityAttr.splitFlag; //是否是分支调用，true表示是，false表示否
	if (typeof(type) == 'undefined' || type != null && type == "dosubmit") { //正常提交
		if (typeof(splitFlag) == 'undefined' || splitFlag == null || splitFlag == "false" || splitFlag== false) { //非分支情况下
			var ideaValue = $('#textAreaIdeas').val();
  			if (curIdeaType == "must" && (typeof(ideaValue) == 'undefined' || ideaValue == null || ideaValue == '')) {
  				$.messager.alert('提示','请填写意见后再提交!','error');
        		return false;
			}
		}
	}else if(typeof(type) != 'undefined' && type!=null && type == "dowithdraw" ){//流程拿回
		//可填可不填
		return true;
	}else if(typeof(type) != 'undefined' && type !=null  && (type == "dotransmit" || type == "dosupersede" || type == "doadduser" || type == "doglobaljump" || type == "doglobalreader" || type == "doglobaltransmit" || type == "dosupplement")){//转发，转办,增发,跳转, 全局读者，全局转发,补发
	 	//可填可不填 
		return true;
	} else if(typeof(type) != 'undefined' && type!=null && type == 'doretreattodraft' || type == 'doretreattoprev'){//退回拟稿人,退回上一步,必须填写意见
		var ideaValue = $('#textAreaIdeas').val();
		if (typeof(ideaValue) == 'undefined' || ideaValue == null || ideaValue == '') {
	    	$.messager.alert('提示','请填写意见后再提交!','error');
	    	return false;
		}
	}
	return true;
}
/**
 * 是否强强制表态
 * @returns {Boolean}
 */
function doIdearCompelMannerCheck(){
	if($('#bottomFrame').css('height') == '0px'){//如果意见不能填写,不再继续判断强制表态
		return true;
	}
	//var frm = document.getElementById("commonIdear").contentWindow;
	var idearCompelMannerValue = $('#ideaCompelManner').val();
	if(conditions.isIdeaCompelManner() && idearCompelMannerValue == ''){
		$.messager.alert('提示','强制表态不允许为空!','error');
		return false;
	}
	return true;
}
/**
 * 获取工作移交usersList
 * @returns {String}
 */
function getWorkHandUsersList(){
	var selectedUsers = getSelectedUserDataJson();
	if(typeof(selectedUsers) == 'undefined' || selectedUsers == '[]'){
		selectedUsers = '';
	}
	if(selectedUsers instanceof Array){
		var selectedUsersArray = new Array();
		for(var i = 0 ; i < selectedUsers.length ; i++){
			selectedUsersArray.push(selectedUsers[i][0]);
		}
		var selectedUsers = JSON.stringify(selectedUsersArray);
	}
	var returnValue;
	if(selectedUsers != '' && typeof(selectedUsers) != ""){
		$.ajax({
			   type: "GET",
			   url: getPath() + '/platform/user/bpmSelectUserAction/getWorkHandUsers',
			   async: false,
			   data: 'userList=' + selectedUsers,
			   dataType: 'json',
			   success: function(msg){
			     if(msg.workHandUsersList){
			    	$('#processSubimt').linkbutton('disable');
			    	$('#close').linkbutton('disable');
			    	var usd = new UserSelectDialog('workflowWorkhandDialog','550','300',getPath() + '/platform/user/bpmSelectUserAction/getWorkHandSelectUser?userList=' + encodeURI(encodeURI(JSON.stringify(msg.workHandUsersList))),'工作移交选人窗口');
			 		var buttons = [{
			 			text:'提交',
			 			id : 'workhandSubmit',
			 			//iconCls : 'icon-submit',
			 			handler:function(){
			 				$('#workhandSubmit').linkbutton('disable');
			 				easyuiMask();
			 				var frmId = $('#workflowWorkhandDialog iframe').attr('id');
			 				var frm = document.getElementById(frmId).contentWindow;
			 				var workhandUsers = frm.$('#workhandDataGrid').datagrid('getData').rows;
			 				returnValue =  putTogetherSelectedResultDataJson(workhandUsers);
			 				var seleUserJson = returnValue;
			 				var data = "taskId=" + taskId + "&outcome=" + outcome + "&executionId="+executionId+"&userJson="+seleUserJson;
			 				ajaxRequest("POST",data,doSubmitUrl,"json","parent." + doSubmitCallEvent);
			 				usd.close();
			 			}
			 		}];
			 		usd.createButtonsInDialog(buttons);
			 		usd.show(); 
			     } else {
			    	 returnValue =  putTogetherSelectedResultDataJson(null);
			     }
			   },
			   error : function(msg){
				   $.messager.alert('错误',msg,'error');
				   return ;
			   }
		});
	}else {
		returnValue =  putTogetherSelectedResultDataJson(null);
    }
	return returnValue;
}
/**
 * 临时保存到cookie
 */
function tempSaveToCookie(){
	//常用意见保存到cookie
	//var frm = document.getElementById("commonIdear").contentWindow;
	var ideasValue = $("#textAreaIdeas").val();
	saveToCookieTip();
	$.cookie(settings.saveToCookieKey,ideasValue,{expires: 10});
}