function ButtonProcessing(){}

ButtonProcessing.ButtonName_Batchsubmit = "dobactchsubmit"; //流程批量提交
ButtonProcessing.ButtonName_Submit = "dosubmit"; //流程提交
ButtonProcessing.ButtonName_Jump = "doglobaljump"; //流程跳转
ButtonProcessing.ButtonName_Rejecttofirst = "doretreattodraft"; //流程退回开始节点
ButtonProcessing.ButtonName_Rejecttoprev = "doretreattoprev"; //流程退回上一节点
ButtonProcessing.ButtonName_Withdraw = "dowithdraw"; //流程拿回
ButtonProcessing.ButtonName_Innerwithdraw = "dowithdrawassignee"; //流程节点内拿回
ButtonProcessing.ButtonName_Transmit = "dotransmit"; //流程转发
ButtonProcessing.ButtonName_Supersede = "dosupersede"; //流程转办
ButtonProcessing.ButtonName_Supplement = "dosupplement"; //流程补发
ButtonProcessing.ButtonName_End = "doglobalend"; //流程结束
ButtonProcessing.ButtonName_Suspend = "doglobalsuspend"; //流程暂停
ButtonProcessing.ButtonName_Resume = "doglobalresume"; //流程恢复
ButtonProcessing.ButtonName_Idea = "doglobalidea"; //全局修改意见
ButtonProcessing.ButtonName_Adduser = "doadduser"; //增发
ButtonProcessing.ButtonName_Reader = "doglobalreader"; //读者
ButtonProcessing.ButtonName_GlobalTransmit = "doglobaltransmit"; //全局流程转发
ButtonProcessing.ButtonName_Track = "dotrack"; //流程跟踪
ButtonProcessing.ButtonName_WriteIdea = "dowriteidea"; //填写意见
ButtonProcessing.ButtonName_FormSave = "doformsave"; //保存表单
ButtonProcessing.ButtonName_WordCreate = "dowordcreate"; //创建正文
ButtonProcessing.ButtonName_WordEdit = "dowordedit"; //编辑正文
ButtonProcessing.ButtonName_WordRead = "dowordread"; //查看正文
ButtonProcessing.ButtonName_WordRedTemplet = "dowordredtemplet"; //查看正文
ButtonProcessing.ButtonName_FocusTask = "dofocus"; //关注任务
ButtonProcessing.ButtonName_userdefined = "dostepuserdefined"; //自定义流程处理人

/**
 * 按钮前处理
 */ 
ButtonProcessing.ButtonPreviousProcessing = function (buttonName,entryId,executionId,currentActivityName,targetActivityName,formId){
	var _buttonName = buttonName || "";
	var _entryId = entryId || "";
	var _executionId = executionId || "";
	var _formId = formId || "";
	var _currentActivityName = currentActivityName || "";
	var _targetActivityName = targetActivityName || "";
	var b = true; //是否继续往下执行
	//流程提交前处理
	if(_buttonName == ButtonProcessing.ButtonName_Submit){
		if(window.beforeSubmit){
			b = beforeSubmit(_entryId,_executionId,_currentActivityName,_targetActivityName,_formId);
		}
	}
	
	//跳转前处理
	if(_buttonName == ButtonProcessing.ButtonName_Jump){
		if(window.beforeJump){
			b = beforeJump(_entryId,_executionId,_currentActivityName,_targetActivityName,_formId);
		}
	}
	
	//退回开始节点前处理
	if(_buttonName == ButtonProcessing.ButtonName_Rejecttofirst){
		if(window.beforeRejecttofirst){
			b = beforeRejecttofirst(_entryId,_executionId,_currentActivityName,_targetActivityName,_formId);
		}
	}
	
	//退回上一节点前处理
	if(_buttonName == ButtonProcessing.ButtonName_Rejecttoprev){
		if(window.beforeRejecttoprev){
			b = beforeRejecttoprev(_entryId,_executionId,_currentActivityName,_targetActivityName,_formId);
		}
	}
	
	//拿回前处理
	if(_buttonName == ButtonProcessing.ButtonName_Withdraw){
		if(window.beforeWithdraw){
			b = beforeWithdraw(_entryId,_executionId,_currentActivityName,_targetActivityName,_formId);
		}
	}
	
	//流程节点内拿回前处理
	if(_buttonName == ButtonProcessing.ButtonName_Innerwithdraw){
		if(window.beforeInnerwithdraw){
			b = beforeInnerwithdraw(_entryId,_executionId,_currentActivityName,_targetActivityName,_formId);
		}
	}
	
	//转发前处理
	if(_buttonName == ButtonProcessing.ButtonName_Transmit){
		if(window.beforeTransmit){
			b = beforeTransmit(_entryId,_executionId,_currentActivityName,_targetActivityName,_formId);
		}
	}
	
	//转办前处理
	if(_buttonName == ButtonProcessing.ButtonName_Supersede){
		if(window.beforeSupersede){
			b = beforeSupersede(_entryId,_executionId,_currentActivityName,_targetActivityName,_formId);
		}
	}
	
	//全局转发前处理
	if(_buttonName == ButtonProcessing.ButtonName_GlobalTransmit){
		if(window.beforeGlobalTransmit){
			b = beforeGlobalTransmit(_entryId,_executionId,_currentActivityName,_targetActivityName,_formId);
		}
	}
	
	//补发前处理
	if(_buttonName == ButtonProcessing.ButtonName_Supplement){
		if(window.beforeSupplement){
			b = beforeSupplement(_entryId,_executionId,_currentActivityName,_targetActivityName,_formId);
		}
	}
	
	//结束前处理
	if(_buttonName == ButtonProcessing.ButtonName_End){
		if(window.beforeEnd){
			b = beforeEnd(_entryId,_executionId,_currentActivityName,_targetActivityName,_formId);
		}
	}
	
	//暂停前处理
	if(_buttonName == ButtonProcessing.ButtonName_Suspend){
		if(window.beforeSuspend){
			b = beforeSuspend(_entryId,_executionId,_currentActivityName,_targetActivityName,_formId);
		}
	}
	
	//恢复前处理
	if(_buttonName == ButtonProcessing.ButtonName_Resume){
		if(window.beforeResume){
			b = beforeResume(_entryId,_executionId,_currentActivityName,_targetActivityName,_formId);
		}
	}

	//流程全局修改意见
	if(_buttonName == ButtonProcessing.ButtonName_Idea){
		if(window.beforeIdea){
			b = beforeIdea(_entryId,_executionId,_currentActivityName,_targetActivityName,_formId);
		}
	}
	
	//流程增发
	if(_buttonName == ButtonProcessing.ButtonName_Adduser){
		if(window.beforeAdduser){
			b = beforeAdduser(_entryId,_executionId,_currentActivityName,_targetActivityName,_formId);
		}
	}
	
	//流程读者
	if(_buttonName == ButtonProcessing.ButtonName_Reader){
		if(window.beforeReader){
			b = beforeReader(_entryId,_executionId,_currentActivityName,_targetActivityName,_formId);
		}
	}
	
	//全局转发
	if(_buttonName == ButtonProcessing.ButtonName_GlobalTransmit){
		if(window.beforeGlobalTransmit){
			b = beforeGlobalTransmit(_entryId,_executionId,_currentActivityName,_targetActivityName,_formId);
		}
	}
	
	//填写意见
	if(_buttonName == ButtonProcessing.ButtonName_WriteIdea){
		if(window.beforeWriteIdea){
			b = beforeWriteIdea(_entryId,_executionId,_currentActivityName,_targetActivityName,_formId);
		}
	}
	
	//保存表单
	if(_buttonName == ButtonProcessing.ButtonName_FormSave){
		if(window.beforeFormSave){
			b = beforeFormSave(_entryId,_executionId,_currentActivityName,_targetActivityName,_formId);
		}
	}
	
	//创建正文
	if(_buttonName == ButtonProcessing.ButtonName_WordCreate){
		if(window.beforeWordCreate){
			b = beforeWordCreate(_entryId,_executionId,_currentActivityName,_targetActivityName,_formId);
		}
	}
	
	//编辑正文
	if(_buttonName == ButtonProcessing.ButtonName_WordEdit){
		if(window.beforeWordEdit){
			b = beforeWordEdit(_entryId,_executionId,_currentActivityName,_targetActivityName,_formId);
		}
	}
	
	//查看正文
	if(_buttonName == ButtonProcessing.ButtonName_WordRead){
		if(window.beforeWordRead){
			b = beforeWordRead(_entryId,_executionId,_currentActivityName,_targetActivityName,_formId);
		}
	}
	
	//套红
	if(_buttonName == ButtonProcessing.ButtonName_WordRedTemplet){
		if(window.beforeWordRedTemplet){
			b = beforeWordRedTemplet(_entryId,_executionId,_currentActivityName,_targetActivityName,_formId);
		}
	}
	return b;
};

/**
 * 按钮后处理
 */ 
ButtonProcessing.ButtonPostProcessing = function (buttonName,entryId,executionId,currentActivityName,targetActivityName,formId){
	
	var _buttonName = buttonName || "";
	var _entryId = entryId || "";
	var _executionId = executionId || "";
	var _formId = formId || "";
	var _currentActivityName = currentActivityName || "";
	var _targetActivityName = targetActivityName || "";
	var b = true; //是否继续往下执行
	//流程提交后处理
	if(_buttonName == this.ButtonName_Submit){
		if(window.afterSubmit){
			b = afterSubmit(_entryId,_executionId,_currentActivityName,_targetActivityName,_formId);
		}
	}
	
	//跳转后处理
	if(_buttonName == this.ButtonName_Jump){
		if(window.afterJump){
			b = afterJump(_entryId,_executionId,_currentActivityName,_targetActivityName,_formId);
		}
	}
	
	//退回开始节点后处理
	if(_buttonName == ButtonProcessing.ButtonName_Rejecttofirst){
		if(window.afterRejecttofirst){
			b = afterRejecttofirst(_entryId,_executionId,_currentActivityName,_targetActivityName,_formId);
		}
	}
	
	//退回上一节点后处理
	if(_buttonName == ButtonProcessing.ButtonName_Rejecttoprev){
		if(window.alterRejecttoprev){
			b = alterRejecttoprev(_entryId,_executionId,_currentActivityName,_targetActivityName,_formId);
		}
	}
	
	//拿回后处理
	if(_buttonName == ButtonProcessing.ButtonName_Withdraw){
		if(window.afterWithdraw){
			b = afterWithdraw(_entryId,_executionId,_currentActivityName,_targetActivityName,_formId);
		}
	}
	
	//流程节点内拿回后处理
	if(_buttonName == ButtonProcessing.ButtonName_Innerwithdraw){
		if(window.afterInnerwithdraw){
			b = afterInnerwithdraw(_entryId,_executionId,_currentActivityName,_targetActivityName,_formId);
		}
	}
	
	//转发后处理
	if(_buttonName == ButtonProcessing.ButtonName_Transmit){
		if(window.alterTransmit){
			b = afterTransmit(_entryId,_executionId,_currentActivityName,_targetActivityName,_formId);
		}
	}
	
	//全局转发后处理
	if(_buttonName == ButtonProcessing.ButtonName_GlobalTransmit){
		if(window.afterGlobalTransmit){
			b = afterGlobalTransmit(_entryId,_executionId,_currentActivityName,_targetActivityName,_formId);
		}
	}
	
	//转办后处理
	if(_buttonName == ButtonProcessing.ButtonName_supersede){
		if(window.afterSupersede){
			b = afterSupersede(_entryId,_executionId,_currentActivityName,_targetActivityName,_formId);
		}
	}
	
	//补发后处理
	if(_buttonName == ButtonProcessing.ButtonName_Supplement){
		if(window.afterSupplement){
			b = afterSupplement(_entryId,_executionId,_currentActivityName,_targetActivityName,_formId);
		}
	}
	
	//结束后处理
	if(_buttonName == ButtonProcessing.ButtonName_End){
		if(window.afterEnd){
			b = afterEnd(_entryId,_executionId,_currentActivityName,_targetActivityName,_formId);
		}
	}
	
	//暂停后处理
	if(_buttonName == ButtonProcessing.ButtonName_Suspend){
		if(window.afterSuspend){
			b = afterSuspend(_entryId,_executionId,_currentActivityName,_targetActivityName,_formId);
		}
	}
	
	//恢复后处理
	if(_buttonName == ButtonProcessing.ButtonName_Resume){
		if(window.afterResume){
			b = afterResume(_entryId,_executionId,_currentActivityName,_targetActivityName,_formId);
		}
	}
	
	//流程全局修改意见
	if(_buttonName == ButtonProcessing.ButtonName_Idea){
		if(window.afterIdea){
			b = afterIdea(_entryId,_executionId,_currentActivityName,_targetActivityName,_formId);
		}
	}
	//流程增发
	if(_buttonName == ButtonProcessing.ButtonName_Adduser){
		if(window.afterAdduser){
			b = afterAdduser(_entryId,_executionId,_currentActivityName,_targetActivityName,_formId);
		}
	}
	
	//流程读者
	if(_buttonName == ButtonProcessing.ButtonName_Reader){
		if(window.afterReader){
			b = afterReader(_entryId,_executionId,_currentActivityName,_targetActivityName,_formId);
		}
	}
	
	//全局转发
	if(_buttonName == ButtonProcessing.ButtonName_GlobalTransmit){
		if(window.afterGlobalTransmit){
			b = afterGlobalTransmit(_entryId,_executionId,_currentActivityName,_targetActivityName,_formId);
		}
	}
	
	//填写意见
	if(_buttonName == ButtonProcessing.ButtonName_WriteIdea){
		if(window.afterWriteIdea){
			b = afterWriteIdea(_entryId,_executionId,_currentActivityName,_targetActivityName,_formId);
		}
	}

	//保存表单
	if(_buttonName == ButtonProcessing.ButtonName_FormSave){
		if(window.afterFormSave){
			b = afterFormSave(_entryId,_executionId,_currentActivityName,_targetActivityName,_formId);
		}
	}
	
	//创建正文
	if(_buttonName == ButtonProcessing.ButtonName_WordCreate){
		if(window.afterWordCreate){
			b = afterWordCreate(_entryId,_executionId,_currentActivityName,_targetActivityName,_formId);
		}
	}
	
	//编辑正文
	if(_buttonName == ButtonProcessing.ButtonName_WordEdit){
		if(window.afterWordEdit){
			b = afterWordEdit(_entryId,_executionId,_currentActivityName,_targetActivityName,_formId);
		}
	}
	
	//查看正文
	if(_buttonName == ButtonProcessing.ButtonName_WordRead){
		if(window.afterWordRead){
			b = afterWordRead(_entryId,_executionId,_currentActivityName,_targetActivityName,_formId);
		}
	}
	
	//套红
	if(_buttonName == ButtonProcessing.ButtonName_WordRedTemplet){
		if(window.afterWordRedTemplet){
			b = afterWordRedTemplet(_entryId,_executionId,_currentActivityName,_targetActivityName,_formId);
		}
	}
	return b;
}; 