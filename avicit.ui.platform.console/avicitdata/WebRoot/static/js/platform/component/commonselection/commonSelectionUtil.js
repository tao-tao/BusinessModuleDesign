/**
 * 创建CommonSelector  
 * @param selectType                  必填     选择人员-user 部门-dept 角色-role  群组-group  岗位-position
 * @param dialogDivId                 可选     选人对话框ID
 * @param targetIdEleId               必填     回写选择的数据需要用的页面元素Id
 * @param targetNameEleId             必填     回写选择的数据需要用的页面元素Id  
 * @param userDeptIdEleId             可选     回写用户ID所属部门需要用的页面元素Id（当selectType='user' 且需要回填部门时此项必填）
 * @param userDeptNameEleId           可选     回写用户所属部门名称需要用的页面元素Id（当selectType='user' 且需要回填部门时此项必填）
*  @param extParameter                可选     自定义页面初始化数据(字符串) 格式：“{参数1:参数值1,参数2:参数值2.....}”例如：'{orgId:"",deptId:""}'
 * @param singleSelect                可选     默认值 true  可填写值：true/false/-1/数字  当填写false/-1  表示选择多少项不受限制 当填写数字时将限定选择数量不大于填写的数字
 * @param splitChar                   可选     返回值分割符 默认逗号（,）
 * @param dialogTitle                 可选     dialog的标题
 * @param dialogWidth                 可选     默认值 600
 * @param dialogHeight                可选     默认值 460
 */
CommonSelector = function(selectType,dialogDivId,targetIdEleId,targetNameEleId,userDeptIdEleId,userDeptNameEleId,extParameter,singleSelect,splitChar,dialogTitle,dialogWidth,dialogHeight){
	this.imageObj = null;
	/**
	 * 使用场景：适用于不需要自动映射回写数据，需要使用函数回填数据情况（当需要自己通过函数回填数据，selectType，targetIdEleId必须填，其他可根据需要设置）
	 * isInitIcon   是否需要初始化图标
	 * callBackFunc 如果isInitIcon项填true callBackFunc项也填写 将不会自动向父页面回填选择的数据，需要自己写函数回填,
	 *   如果isInitIcon项填其他值则不初始化图标，需要自己写函数回填已选择数据到父页面
	 *multipleOrg 是否显示多个组织根节点 可选：1或不设置 代表当前登录人所在组织 n代表多组织 显示几个组织由项目开发人员在java代码中控制
	 *afterCloseCallFun  需要选人框关闭后做一些业务处理，开发人员自己将业务处理的函数名称以字符串传递即可
	 *displaySubDeptUser 传递“Y” 当部门节点处于checked状态是将该部门下人员与递归将该部门的子部门下人员取出 不填值或填除“Y”之外的其他任意值将只读取当前部门的直属人员
	 */
	this.init = function(isInitIcon,callBackFunc,multipleOrg,afterCloseCallFun,displaySubDeptUser){
		var iconPath ="/static/css/platform/themes/default/commonselection/icons_ext/selectorCommon.gif";
		var defaultTitle = "" ;
		if(selectType=='user'){
			//iconPath = "/avicit/platform6/component/css/commonselection/icons_ext/addUserDept.gif";
			defaultTitle = "选择用户" ;
		}else if(selectType=='dept'){
			//iconPath = "/avicit/platform6/component/css/commonselection/icons_ext/dept.gif";
			defaultTitle = "选择部门" ;
		}else if(selectType=='role'){
			//iconPath = "/avicit/platform6/component/css/commonselection/icons_ext/role.gif";
			defaultTitle = "选择角色" ;
		}else if(selectType=='group'){
			//iconPath = "/avicit/platform6/component/css/commonselection/icons_ext/group.png";
			defaultTitle = "选择群组" ;
		}else if(selectType=='position'){
			//iconPath = "/avicit/platform6/component/css/commonselection/icons_ext/postion.gif";
			defaultTitle = "选择岗位" ;
		}
		
		dialogDivId = dialogDivId || "_comframe_id_" + ( Math.random() + "" ).split(".")[1];
		if(dialogTitle==null || typeof(dialogTitle)=='undefined'){
			dialogTitle = defaultTitle ;
	    }
		if(dialogWidth==null || typeof(dialogWidth)=='undefined'){
	    	dialogWidth = 600 ;
	    }
		if(dialogHeight==null || typeof(dialogHeight)=='undefined'){
			dialogHeight=460;
	    }
		if(singleSelect==null || typeof(singleSelect)=='undefined'){
				singleSelect = true ;
		} 
	   
		if(splitChar==null || typeof(splitChar)=='undefined'){
			splitChar = ',' ;
		}
		if(extParameter==null || typeof(extParameter)=='undefined'){
			extParameter = '' ;
		}
		
		var context =getPath() ;
        if(isInitIcon==null || typeof(isInitIcon)=='undefined' || isInitIcon==true){
        	 //创建image元素
    		var imageObj = jQuery('<img >').attr('src', context+iconPath).attr('class', "input-right-icon");
    		imageObj.on("click",function(){
    			if(selectType=='user'){
    				selectUser(dialogDivId,targetIdEleId,targetNameEleId,userDeptIdEleId,userDeptNameEleId,dialogTitle,dialogWidth,dialogHeight,extParameter,singleSelect,splitChar,callBackFunc,afterCloseCallFun,multipleOrg,displaySubDeptUser);
    			}else if(selectType=='dept'){
    				selectDept(dialogDivId,targetIdEleId,targetNameEleId,dialogTitle,dialogWidth,dialogHeight,extParameter,singleSelect,splitChar,callBackFunc,afterCloseCallFun,multipleOrg,displaySubDeptUser);
    			}else if(selectType=='role'){
    				selectRole(dialogDivId,targetIdEleId,targetNameEleId,dialogTitle,dialogWidth,dialogHeight,extParameter,singleSelect,splitChar,callBackFunc,afterCloseCallFun,multipleOrg);
    			}else if(selectType=='group'){
    				selectGroup(dialogDivId,targetIdEleId,targetNameEleId,dialogTitle,dialogWidth,dialogHeight,extParameter,singleSelect,splitChar,callBackFunc,afterCloseCallFun,multipleOrg);
    			}else if(selectType=='position'){
    				selectPosition(dialogDivId,targetIdEleId,targetNameEleId,dialogTitle,dialogWidth,dialogHeight,extParameter,singleSelect,splitChar,callBackFunc,afterCloseCallFun,multipleOrg);
    			}
    		}); 
    	 var targetElement =  jQuery("#"+targetNameEleId);
    	 imageObj.on("hover",function(){
  			 targetElement.parent().addClass("selector-div-bg");
  			 targetElement.next().attr('src', "static/css/platform/themes/default/commonselection/icons_ext/selectorCommon.gif");
  		 });
    	 targetElement.addClass("selector-div-input");
    	 targetElement.css("width",targetElement.width()-16);
  		 targetElement.on("hover",function(){
  			 targetElement.parent().addClass("selector-div-bg");
  			 targetElement.next().attr('src', "static/css/platform/themes/default/commonselection/icons_ext/selectorCommon.gif");
  		 });
  		 
  		  targetElement.click(function(){
  			  //targetElement.next().css("display","block");
  		  }) ;
  		  
	  		//insertAfter(imageObj, targetElement);
	  		var divObj=jQuery('<div class="selector-div"></div>').appendTo(targetElement.parent());
	  		divObj.css("width",jQuery("#"+targetNameEleId).width()+18);
	  		targetElement.appendTo(divObj);
	  		imageObj.appendTo(divObj);
        }else{
        	if(selectType=='user'){
				selectUser(dialogDivId,targetIdEleId,targetNameEleId,userDeptIdEleId,userDeptNameEleId,dialogTitle,dialogWidth,dialogHeight,extParameter,singleSelect,splitChar,callBackFunc,afterCloseCallFun,multipleOrg,displaySubDeptUser);
			}else if(selectType=='dept'){
				selectDept(dialogDivId,targetIdEleId,targetNameEleId,dialogTitle,dialogWidth,dialogHeight,extParameter,singleSelect,splitChar,callBackFunc,afterCloseCallFun,multipleOrg,displaySubDeptUser);
			}else if(selectType=='role'){
				selectRole(dialogDivId,targetIdEleId,targetNameEleId,dialogTitle,dialogWidth,dialogHeight,extParameter,singleSelect,splitChar,callBackFunc,afterCloseCallFun,multipleOrg);
			}else if(selectType=='group'){
				selectGroup(dialogDivId,targetIdEleId,targetNameEleId,dialogTitle,dialogWidth,dialogHeight,extParameter,singleSelect,splitChar,callBackFunc,afterCloseCallFun,multipleOrg);
			}else if(selectType=='position'){
				selectPosition(dialogDivId,targetIdEleId,targetNameEleId,dialogTitle,dialogWidth,dialogHeight,extParameter,singleSelect,splitChar,callBackFunc,afterCloseCallFun,multipleOrg);
			}
        }
	};
};

function insertAfter(newElement, targetElement){ 
	  var parent = targetElement.parentNode; 
	  if (parent.lastChild == targetElement) { 
		  // 如果最后的节点是目标元素，则直接添加。因为默认是最后
		  parent.appendChild(newElement);
	  } else {
		//如果不是，则插入在目标元素的下一个兄弟节点 的前面，即目标元素的后面 。
		  parent.insertBefore(newElement, targetElement.nextSibling); 
	  }
};

/**
 * 选择人员
 * @param dialogDivId        必填    选人对话框ID
 * @param userIdEleId        必填    回写用户ID需要用的页面元素Id
 * @param userNameEleId  必填    回写用户姓名需要用的页面元素Id
 *  @param deptIdEleId       可选    回写用户ID所属部门需要用的页面元素Id
 * @param deptNameEleId  可选    回写用户所属部门名称需要用的页面元素Id
 * @param dialogTitle         可选    dialog的标题
 * @param dialogWidth       可选   默认值 550
 * @param dialogHeight      可选   默认值 460
 * @param singleSelect           可选  默认值 true  可填写值：true/false
 * @param splitChar            可选  返回值分割符 默认逗号（,） 
 */
function selectUser(dialogDivId,userIdEleId,userNameEleId,deptIdEleId,deptNameEleId,dialogTitle,dialogWidth,dialogHeight,extParameter,singleSelect,splitChar,callBackFunc,afterCloseCallFun,multipleOrg,displaySubDeptUser){
	    var historyUserId = "";
	    if(userIdEleId!=null && typeof(userIdEleId)!='undefined'){
		   historyUserId = document.getElementById(userIdEleId).value;
	    }
	    var queryParam='?singleSelect='+singleSelect +"&selectType=user&historyId="+historyUserId+"&extParameter="+extParameter ;
	    if(multipleOrg != null && typeof(multipleOrg) != 'undefined'){
	    	queryParam += "&multipleOrg="+multipleOrg ;
	    }
	    if(displaySubDeptUser != null && typeof(displaySubDeptUser) != 'undefined'){
	    	queryParam += "&displaySubDeptUser="+displaySubDeptUser ;
	    }
	    var reqUrl=getPath() + '/platform/commonSelection/commonSelectionController/getOrg'+queryParam;
	    if(afterCloseCallFun != null && typeof(afterCloseCallFun) != 'undefined'){
	    	afterCloseCallFun = afterCloseCallFun+"()";
	    }
	 	var userCommonDialog = new CommonSelectorDialog(dialogDivId,dialogWidth,dialogHeight,reqUrl,dialogTitle,afterCloseCallFun);
 		var buttons = [{
			 			text:'确定',
			 			id : 'UserProcessSubimt'+new Date().getTime(),
			 			//iconCls : 'icon-ok',
			 			handler:function(){
			 				var frmId = jQuery('#'+dialogDivId+' iframe').attr('id');
			 				var frm = document.getElementById(frmId).contentWindow;
			 				var resultData = frm.getSelectedResultDataJson();
			 				/**if(resultData.length==0){
			 					jQuery.messager.alert("操作提示", "请选择用户！","info");
		 						return ;
			 				}**/
			 				
			 				//选择数据提交前控制
			 				if(!submitLimitCheck(singleSelect,resultData.length,"user")){
			 					return ;
			 				}
			 				var funRetValue = null;
			 				if(callBackFunc!=null && callBackFunc!='undefined'){
			 					//zl
			 					//funRetValue = eval(callBackFunc + "(resultData,dialogDivId)");
			 					if(typeof(callBackFunc) === 'function'){
			 						funRetValue = callBackFunc(resultData,dialogDivId);
			 				    }else if(typeof(callBackFunc) ==='string'){
			 				    	funRetValue = eval(callBackFunc + "(resultData,dialogDivId)");
			 				    }else{
			 					   throw new Error("非法的回调函数！");
			 				    }
			 	   			}else{
			 	   			   //业务处理
				 				var userId="";
				 				var userName="";
				 				var deptId = "" ;
				 				var deptName = "" ;
				 				for(var i=0 ; i<resultData.length;i++){
				 					var userDept = resultData[i];
				 					  if(userId==""){
				  						 userId = userDept.userId;
				  					  }else{
				  						 userId +=splitChar+ userDept.userId;  
				  					  }
				  					 if(userName==""){
				  						userName = userDept.userName;
				  					  }else{
				  						 userName +=splitChar+ userDept.userName;  
				  					  }
				  					if(deptIdEleId != null && typeof(deptIdEleId) != 'undefined'){
				  						if(deptId==""){
				  	  						deptId = userDept.deptId;
				  	 					  }else{
				  	 						  deptId +=splitChar+ userDept.deptId;  
				  	 					  }
				  					}
				  					if(deptNameEleId != null && typeof(deptNameEleId) != 'undefined'){
				  						if(deptName==""){
				  	  						deptName = userDept.deptName;
				  	 					  }else{
				  	 						  deptName +=splitChar+ userDept.deptName;  
				  	 					  }
				  					}
				 				}
				 				jQuery("#"+userIdEleId).val(userId);
								jQuery("#"+userNameEleId).val(userName);
								if(deptIdEleId != null && typeof(deptIdEleId) != 'undefined'){
									jQuery("#"+deptIdEleId).val(deptId);
								}
								if(deptNameEleId != null && typeof(deptNameEleId) != 'undefined'){
									jQuery("#"+deptNameEleId).val(deptName);
								}
			 	   			}
			 				if(funRetValue==null || typeof(funRetValue)=="undefined" || funRetValue==true){
			 					userCommonDialog.close();
			 				}
		 			}
 	    	}
		];
 		userCommonDialog.createButtonsInDialog(buttons);
 		userCommonDialog.show();
 	};
/**
 * 选择部门
 * @param dialogDivId        必填    选人对话框ID
 *  @param deptIdEleId       必填     回写用户ID所属部门需要用的页面元素Id
 * @param deptNameEleId  必填    回写用户所属部门名称需要用的页面元素Id
 * @param dialogTitle         可选    dialog的标题
 * @param dialogWidth       可选   默认值 550
 * @param dialogHeight      可选   默认值 460
 * @param singleSelect        可选  默认值 true  可填写值：true/false
 * @param splitChar            可选  返回值分割符 默认逗号（,） 
 */
function selectDept(dialogDivId,deptIdEleId,deptNameEleId,dialogTitle,dialogWidth,dialogHeight,extParameter,singleSelect,splitChar,callBackFunc,afterCloseCallFun,multipleOrg,displaySubDeptUser){
	    var historyDeptId = "";
	    if(deptIdEleId!=null && typeof(deptIdEleId)!='undefined'){
	    	historyDeptId = document.getElementById(deptIdEleId).value;
	    }
	    var queryParam='?singleSelect='+singleSelect +"&selectType=dept&historyId="+historyDeptId+"&extParameter="+extParameter ;
	    if(multipleOrg != null && typeof(multipleOrg) != 'undefined'){
	    	queryParam += "&multipleOrg="+multipleOrg ;
	    }
	    var deptUrl=getPath() + '/platform/commonSelection/commonSelectionController/getOrg'+queryParam;
	    if(afterCloseCallFun != null && typeof(afterCloseCallFun) != 'undefined'){
	    	afterCloseCallFun = afterCloseCallFun+"()";
	    }
	 	var deptCommonDialog = new CommonSelectorDialog(dialogDivId,dialogWidth,dialogHeight,deptUrl,dialogTitle,afterCloseCallFun);
 		var buttons = [
 		   {
	 			text:'确定',
	 			id : 'DeptprocessSubimt'+new Date().getTime(),
	 			//iconCls : 'icon-ok',
	 			handler:function(){
	 				var frmId = jQuery('#'+dialogDivId+' iframe').attr('id');
	 				var frm = document.getElementById(frmId).contentWindow;
	 				var resultData = frm.getSelectedResultDataJson();
	 				/**if(resultData.length==0){
	 					jQuery.messager.alert("操作提示", "请选择部门！","info");
 						return ;
	 				}**/
	 				
	 				//选择数据提交前控制
	 				if(!submitLimitCheck(singleSelect,resultData.length,"dept")){
	 					return ;
	 				}
	 			   var funRetValue = null;
	 			   if(callBackFunc!=null && callBackFunc!='undefined'){
	 				   //zl
	 				   if(typeof(callBackFunc) === 'function'){
	 					  funRetValue = callBackFunc(resultData,dialogDivId);
	 				   }else if(typeof(callBackFunc) ==='string'){
	 					  funRetValue = eval(callBackFunc + "(resultData,dialogDivId)");
	 				   }else{
	 					   throw new Error("非法的毁掉函数！");
	 				   }
	 				  
	 	   			}else{
		 				//业务处理
		 				var deptId = "" ;
		 				var deptName = "" ;
		 				for(var i=0 ; i<resultData.length;i++){
		 					var userDept = resultData[i];
		  					if(deptIdEleId != null && typeof(deptIdEleId) != 'undefined'){
		  						if(deptId==""){
		  	  						deptId = userDept.deptId;
		  	 					  }else{
		  	 						  deptId +=splitChar+ userDept.deptId;  
		  	 					  }
		  					}
		  					if(deptNameEleId != null && typeof(deptNameEleId) != 'undefined'){
		  						if(deptName==""){
		  	  						deptName = userDept.deptName;
		  	 					  }else{
		  	 						  deptName +=splitChar+ userDept.deptName;  
		  	 					  }
		  					}
		 				}
						if(deptIdEleId != null && typeof(deptIdEleId) != 'undefined'){
							jQuery("#"+deptIdEleId).val(deptId);
						}
						if(deptNameEleId != null && typeof(deptNameEleId) != 'undefined'){
							jQuery("#"+deptNameEleId).val(deptName);
						}
	 	   			}
	 			    if(funRetValue==null || typeof(funRetValue)=="undefined" || funRetValue==true){
	 			    	deptCommonDialog.close();
	 				}
 			}
 		}];
 		deptCommonDialog.createButtonsInDialog(buttons);
 		deptCommonDialog.show();
 	};
/**
 * 
 * @param dialogDivId
 * @param roleIdEleId
 * @param roleNameEleId
 * @param dialogTitle
 * @param dialogWidth
 * @param dialogHeight
 * @param singleSelect
 * @param splitChar
 */
function selectRole(dialogDivId,roleIdEleId,roleNameEleId,dialogTitle,dialogWidth,dialogHeight,extParameter,singleSelect,splitChar,callBackFunc,afterCloseCallFun,multipleOrg){
	  var historyRoleId = "";
	  if(roleIdEleId!=null && typeof(roleIdEleId)!='undefined'){
	    	historyRoleId = document.getElementById(roleIdEleId).value
	  }
	 var queryParam='?singleSelect='+singleSelect +"&dialogId="+dialogDivId+"&historyId="+historyRoleId+"&extParameter="+extParameter ;
	 if(multipleOrg != null && typeof(multipleOrg) != 'undefined'){
	    	queryParam += "&multipleOrg="+multipleOrg ;
	 }
	 var roleUrl=getPath() + '/avicit/platform6/modules/system/commonpopup/RoleSelect.jsp'+queryParam;
	 if(afterCloseCallFun != null && typeof(afterCloseCallFun) != 'undefined'){
	    	afterCloseCallFun = afterCloseCallFun+"()";
	 }
	var roleCommonDialog = new CommonSelectorDialog(dialogDivId,dialogWidth,dialogHeight,roleUrl,dialogTitle,afterCloseCallFun);
		var buttons = [{
 			text:'确定',
 			id : 'RoleProcessSubimt'+new Date().getTime(),
 			//iconCls : 'icon-ok',
 			handler:function(){
 				var frmId = jQuery('#'+dialogDivId+' iframe').attr('id');
 				var frm = document.getElementById(frmId).contentWindow;
 				var resultData = frm.getSelectedResultDataJson();
 				//选择数据提交前控制
 				if(!submitLimitCheck(singleSelect,resultData.length,"role")){
 					return ;
 				}
 				var funRetValue = null;
 				if(callBackFunc!=null && callBackFunc!='undefined'){
 					funRetValue = eval(callBackFunc + "(resultData,dialogDivId)");
 	   			}else{
	 				//业务处理
	 				var roleId = "" ;
	 				var roleName = "" ;
	 				for(var i=0 ; i<resultData.length;i++){
	 					var roles = resultData[i];
	  					if(roleIdEleId != null && typeof(roleIdEleId) != 'undefined'){
	  						if(roleId==""){
	  							roleId = roles.id;
	  	 					  }else{
	  	 						roleId +=splitChar+ roles.id;  
	  	 					  }
	  					}
	  					if(roleNameEleId != null && typeof(roleNameEleId) != 'undefined'){
	  						if(roleName==""){
	  							roleName = roles.roleName;
	  	 					  }else{
	  	 						roleName +=splitChar+ roles.roleName;  
	  	 					  }
	  					}
	 				}
					if(roleIdEleId != null && typeof(roleIdEleId) != 'undefined'){
						jQuery("#"+roleIdEleId).val(roleId);
					}
					if(roleNameEleId != null && typeof(roleNameEleId) != 'undefined'){
						jQuery("#"+roleNameEleId).val(roleName);
					}
 	   			}
 				if(funRetValue==null || typeof(funRetValue)=="undefined" || funRetValue==true){
 					roleCommonDialog.close();
	 			}
				
			}
		}];
		roleCommonDialog.createButtonsInDialog(buttons);
		roleCommonDialog.show();
		//var dialogIframeId = roleCommonDialog.getDialogIframeId();
		//createCommonQueryDialog(dialogDivId,dialogIframeId,"role");
};

/**
 * 
 * @param dialogDivId
 * @param groupEleId
 * @param groupNameEleId
 * @param dialogTitle
 * @param dialogWidth
 * @param dialogHeight
 * @param singleSelect
 * @param splitChar
 */
function selectGroup(dialogDivId,groupIdEleId,groupNameEleId,dialogTitle,dialogWidth,dialogHeight,extParameter,singleSelect,splitChar,callBackFunc,afterCloseCallFun,multipleOrg){
	var historyGroupId = "";
	if(groupIdEleId!=null && typeof(groupIdEleId)!='undefined'){
		historyGroupId = document.getElementById(groupIdEleId).value;
	}
	var queryParam='?singleSelect='+singleSelect +"&dialogId="+dialogDivId+"&historyId="+historyGroupId+"&extParameter="+extParameter ;
	if(multipleOrg != null && typeof(multipleOrg) != 'undefined'){
    	queryParam += "&multipleOrg="+multipleOrg ;
    }
	var groupUrl=getPath() + '/avicit/platform6/modules/system/commonpopup/GroupSelect.jsp'+queryParam;
	if(afterCloseCallFun != null && typeof(afterCloseCallFun) != 'undefined'){
	    	afterCloseCallFun = afterCloseCallFun+"()";
	}
	var groupCommonDialog = new CommonSelectorDialog(dialogDivId,dialogWidth,dialogHeight,groupUrl,dialogTitle,afterCloseCallFun);
		var buttons = [{
 			text:'确定',
 			id : 'GroupProcessSubimt'+new Date().getTime(),
 			//iconCls : 'icon-ok',
 			handler:function(){
 				var frmId = jQuery('#'+dialogDivId+' iframe').attr('id');
 				var frm = document.getElementById(frmId).contentWindow;
 				var resultData = frm.getSelectedResultDataJson();
 				//选择数据提交前控制
 				if(!submitLimitCheck(singleSelect,resultData.length,"group")){
 					return ;
 				}
 				var funRetValue = null;
 				if(callBackFunc!=null && callBackFunc!='undefined'){
 					//zl
 					//funRetValue = eval(callBackFunc + "(resultData,dialogDivId)");
 					if(typeof(callBackFunc) === 'function'){
 						funRetValue = callBackFunc(resultData,dialogDivId);
 				    }else if(typeof(callBackFunc) ==='string'){
 				    	funRetValue = eval(callBackFunc + "(resultData,dialogDivId)");
 				    }else{
 					   throw new Error("非法的回调函数！");
 				    }
 	   			}else{
	 				//业务处理
	 				var grouptId = "" ;
	 				var groupName = "" ;
	 				for(var i=0 ; i<resultData.length;i++){
	 					var groups = resultData[i];
	  					if(groupIdEleId != null && typeof(groupIdEleId) != 'undefined'){
	  						if(grouptId==""){
	  							grouptId = groups.id;
	  	 					  }else{
	  	 						grouptId +=splitChar+ groups.id;  
	  	 					  }
	  					}
	  					if(groupNameEleId != null && typeof(groupNameEleId) != 'undefined'){
	  						if(groupName==""){
	  							groupName = groups.groupName;
	  	 					  }else{
	  	 						groupName +=splitChar+ groups.groupName;  
	  	 					  }
	  					}
	 				}
					if(groupIdEleId != null && typeof(groupIdEleId) != 'undefined'){
						jQuery("#"+groupIdEleId).val(grouptId);
					}
					if(groupNameEleId != null && typeof(groupNameEleId) != 'undefined'){
						jQuery("#"+groupNameEleId).val(groupName);
					}
 	   			}
 				if(funRetValue==null || typeof(funRetValue)=="undefined" || funRetValue==true){
 					groupCommonDialog.close();
	 			}
			}
		}];
		groupCommonDialog.createButtonsInDialog(buttons);
		groupCommonDialog.show();
		//var groupIframeId = groupCommonDialog.getDialogIframeId();
		//createCommonQueryDialog(dialogDivId,groupIframeId,"group");
};
/**
 * 
 * @param dialogDivId
 * @param positionIdEleId
 * @param positionNameEleId
 * @param dialogTitle
 * @param dialogWidth
 * @param dialogHeight
 * @param singleSelect
 * @param splitChar
 */
function selectPosition(dialogDivId,positionIdEleId,positionNameEleId,dialogTitle,dialogWidth,dialogHeight,extParameter,singleSelect,splitChar,callBackFunc,afterCloseCallFun,multipleOrg){
	var historyPositionId = "";
	if(positionIdEleId!=null && typeof(positionIdEleId)!='undefined'){
		historyPositionId = document.getElementById(positionIdEleId).value;
	}
	var queryParam='?singleSelect='+singleSelect+"&dialogId="+dialogDivId+"&historyId="+historyPositionId+"&extParameter="+extParameter ;
	if(multipleOrg != null && typeof(multipleOrg) != 'undefined'){
    	queryParam += "&multipleOrg="+multipleOrg ;
    }
	var positionUrl=getPath() + '/avicit/platform6/modules/system/commonpopup/PositionSelect.jsp'+queryParam;
	if(afterCloseCallFun != null && typeof(afterCloseCallFun) != 'undefined'){
	    	afterCloseCallFun = afterCloseCallFun+"()";
	}
	var positionCommonDialog = new CommonSelectorDialog(dialogDivId,dialogWidth,dialogHeight,positionUrl,dialogTitle,afterCloseCallFun);
		var buttons = [{
 			text:'确定',
 			id : 'PositionProcessSubimt'+new Date().getTime(),
 			//iconCls : 'icon-ok',
 			handler:function(){
 				var frmId = jQuery('#'+dialogDivId+' iframe').attr('id');
 				var frm = document.getElementById(frmId).contentWindow;
 				var resultData = frm.getSelectedResultDataJson();
 				//选择数据提交前控制
 				if(!submitLimitCheck(singleSelect,resultData.length,"position")){
 					return ;
 				}
 				var funRetValue = null;
 				if(callBackFunc!=null && callBackFunc!='undefined'){
 					funRetValue = eval(callBackFunc + "(resultData,dialogDivId)");
 	   			}else{
	 				//业务处理
	 				var positionId = "" ;
	 				var positionName = "" ;
	 				for(var i=0 ; i<resultData.length;i++){
	 					var positions = resultData[i];
	  					if(positionIdEleId != null && typeof(positionIdEleId) != 'undefined'){
	  						if(positionId==""){
	  							positionId = positions.id;
	  	 					  }else{
	  	 						positionId +=splitChar+ positions.id;  
	  	 					  }
	  					}
	  					if(positionNameEleId != null && typeof(positionNameEleId) != 'undefined'){
	  						if(positionName==""){
	  							positionName = positions.positionName;
	  	 					  }else{
	  	 						positionName +=splitChar+ positions.positionName;  
	  	 					  }
	  					}
	 				}
					if(positionIdEleId != null && typeof(positionIdEleId) != 'undefined'){
						jQuery("#"+positionIdEleId).val(positionId);
					}
					if(positionNameEleId != null && typeof(positionNameEleId) != 'undefined'){
						jQuery("#"+positionNameEleId).val(positionName);
					}
 	   			}
 				if(funRetValue==null || typeof(funRetValue)=="undefined" || funRetValue==true){
 					positionCommonDialog.close();
	 			}
				
			}
		}];
		positionCommonDialog.createButtonsInDialog(buttons);
		positionCommonDialog.show();
		//var positionIframeId = positionCommonDialog.getDialogIframeId();
		//createCommonQueryDialog(dialogDivId,positionIframeId,"position");
};
/**
 * 取得datagrid中全部数据
  * @param dataGridId   
 */
function getAllResultData(dataGridId){
	return jQuery('#'+dataGridId).datagrid('getData').rows;
};
/**
 * 根据已知id值选中datagrid数据
 * @param historyId     带有分隔符号的id的字符串  必填
 * @param dataGridId                                          必填
 * @param splitChar      分隔符                             必填
 */
function setSelectedData(historyId,dataGridId,splitChar){
	if(historyId != null && typeof(historyId)!='undefined' && historyId.length>0){
		//var gridData = getAllResultData(dataGridId) ;
		//if(gridData != null && typeof(gridData)!='undefined' && gridData.length>0 ){
			var historyIdArr = historyId.split(splitChar);
			jQuery.each(historyIdArr,function(x,currentId){
				var rowIndex = jQuery('#'+dataGridId).datagrid('getRowIndex',currentId);
				if(rowIndex!=-1){
					jQuery('#'+dataGridId).datagrid('checkRow',rowIndex);
					//jQuery('#'+dataGridId).datagrid('selectRecord',currentId);
				}
			});
		//}
	}
};
function createCommonQueryDialog(dialogDivId,dialogIframeId,selectType){
	var queryUrl=getPath() + '/avicit/platform6/modules/system/commonpopup/commonSelectorQuery.jsp?selectType='+selectType ;
	var dialogTitle = "查询" ;
	if(selectType=='role'){
		dialogTitle = "角色查询" ;
	}else if(selectType=='group'){
		dialogTitle = "群组查询" ;
	}else if(selectType=='position'){
		dialogTitle = "岗位查询" ;
	}
	var dialogWidth="475";
	var dialogHeight="150" ;
	var queryCommonDialog = new CommonSelectorDialog("query"+dialogDivId,dialogWidth,dialogHeight,queryUrl,dialogTitle);
		var buttons = [{
 			text:'查询',
 			id : selectType+'Query'+new Date().getTime(),
 			iconCls : 'icon-search',
 			handler:function(){
 				var frm = document.getElementById(dialogIframeId).contentWindow;
 				var queryDialogIframeId = queryCommonDialog.getDialogIframeId();
 				var queryFrameWin = document.getElementById(queryDialogIframeId).contentWindow;
 				if(selectType=='role'){
 					 var roleName = queryFrameWin.document.getElementById("filter_LIKE_roleName").value;
 					var roleDesc = queryFrameWin.document.getElementById("filter_LIKE_desc").value;
 	 				 frm.searchRoleFun(roleName,roleDesc);
 				}else if(selectType=='group'){
 					var groupName =queryFrameWin.document.getElementById("filter_LIKE_groupName").value;
 					var groupDesc =queryFrameWin.document.getElementById("filter_LIKE_groupDesc").value;
 					frm.searchGroupFun(groupName,groupDesc);
 				}else if(selectType=='position'){
 					var positionName =queryFrameWin.document.getElementById("filter_LIKE_positionName").value;
 					var positionDesc =queryFrameWin.document.getElementById("filter_LIKE_positionDesc").value;
 					frm.searchPositionFun(positionName,positionDesc);
 				}
			}
		}];
		queryCommonDialog.createButtonsInDialog(buttons);
};
/****----------------------------------------------------------------------------------表单综合选择-----------------------------------------------------------------------------------------------****/
/**
 * 综合选择 
 * @param callBackFun                            必填  已选择数据回填函数
 * @param selectedDataMappingConfig  可选  父页面已选择数据回填映射设设置｛splitChar:",",user:{userId:"",userName:""},dept:{deptId:"",deptName:""},role:{roleId:"",roleName:""},group:{groupId:"",groupName:""},position:{positionId:"",positionName:""}}
* @param dialogDivId                             可选  动态创建dialog需要的div在父页面的唯一标记
 * @param dialogTitle                              可选  选择对话框标题 
 * @param dialogWidth                           可选  选择对话框宽
 * @param dialogHeight                          可选  选择对话框高
 * @param extParameter                         可选  扩展参数
 * @param splitChar                                可选  字符串的分隔符（默认，)
 * @returns {ComprehensiveSelector}
 */
ComprehensiveSelector = function(targetNameEleId,selectedDataMappingConfig,dialogDivId,dialogTitle,dialogWidth,dialogHeight,extParameter,splitChar){
	this.imageObj = null  ;
	/**
	 * 使用场景：适用于不需要自动映射回写数据，需要使用函数回填数据情况（当需要自己通过函数回填数据，selectType，targetIdEleId必须填，其他可根据需要设置）
	 * isInitIcon   是否需要初始化图标
	 * callBackFunc 如果isInitIcon项填true callBackFunc项也填写 将不会自动向父页面回填选择的数据，需要自己写函数回填,
	 *   如果isInitIcon项填其他值则不初始化图标，需要自己写函数回填已选择数据到父页面
	 *multipleOrg 是否显示多个组织根节点 可选：1或不设置 代表当前登录人所在组织 n代表多组织 显示几个组织由项目开发人员在java代码中控制
	 *afterCloseCallFun  需要选人框关闭后做一些业务处理，开发人员自己将业务处理的函数名称以字符串传递即可
	 *displaySubDeptUser 传递“Y” 当部门节点处于checked状态是将该部门下人员与递归将该部门的子部门下人员取出 不填值或填除“Y”之外的其他任意值将只读取当前部门的直属人员
	 */
	this.init = function(isInitIcon,callBackFun,multipleOrg,afterCloseCallFun,displaySubDeptUser){
		var iconPath = "/static/css/platform/themes/default/commonselection/icons_ext/selectorCommon.gif";
		var defaultTitle = "综合选择" ;
		dialogDivId = dialogDivId || "_comframe_id_" + ( Math.random() + "" ).split(".")[1];
		if(dialogTitle==null || typeof(dialogTitle)=='undefined'){
			dialogTitle = defaultTitle ;
	    }
		if(dialogWidth==null || typeof(dialogWidth)=='undefined'){
	    	dialogWidth = 770 ;
	    }
		if(dialogHeight==null || typeof(dialogHeight)=='undefined'){
			dialogHeight=460;
	    }
		if(splitChar==null || typeof(splitChar)=='undefined'){
			splitChar = ',' ;
		}
		if(extParameter==null || typeof(extParameter)=='undefined'){
			extParameter = '' ;
		}
		if(isInitIcon==null || typeof(isInitIcon)=='undefined' || isInitIcon==true){
		var context =getPath() ;
		 //创建image元素
		var imageObj = jQuery('<img >').attr('src', context+iconPath).attr('class', "input-right-icon");
		imageObj.addClass("input-right-icon-comp");
		imageObj.on("click",function(){
			selectDataComprehensively(targetNameEleId,callBackFun,dialogDivId,selectedDataMappingConfig,dialogTitle,dialogWidth,dialogHeight,extParameter,splitChar,multipleOrg,afterCloseCallFun,displaySubDeptUser);
		}); 
		var targetElement =  jQuery("#"+targetNameEleId);
		imageObj.on("hover",function(){
 			 targetElement.parent().addClass("selector-div-bg");
 			 targetElement.next().attr('src', "static/css/platform/themes/default/commonselection/icons_ext/selectorCommon.gif");
 		 });
		 targetElement.css("width",targetElement.width()-13);
		  targetElement.on("hover",function(){
			  targetElement.parent().addClass("selector-div-bg");
	  		  targetElement.next().attr('src', "static/css/platform/themes/default/commonselection/icons_ext/selectorCommon.gif");
			});
		 
		  targetElement.click(function(){
			  //targetElement.next().css("display","block");
		  }) ;
		  
		var divObj=jQuery('<div class="selector-div"></div>').appendTo(targetElement.parent());
		targetElement.addClass("selector-div-input");
		divObj.css("width",targetElement.width()+18);
		targetElement.appendTo(divObj);
		imageObj.appendTo(divObj);
	  }else{
		  selectDataComprehensively(targetNameEleId,callBackFun,dialogDivId,selectedDataMappingConfig,dialogTitle,dialogWidth,dialogHeight,extParameter,splitChar,multipleOrg,afterCloseCallFun,displaySubDeptUser); 
	  }
	};
};
/**
 * 综合选择dialog创建、button定义、回调函数设置
 * @param dialogDivId
 * @param callBackFun
 * @param selectedDataMappingConfig
 * @param dialogTitle
 * @param dialogWidth
 * @param dialogHeight
 * @param extParameter
 * @param singleSelect
 * @param splitChar
 */
function selectDataComprehensively(targetNameEleId,callBackFun,dialogDivId,selectedDataMappingConfig,dialogTitle,dialogWidth,dialogHeight,extParameter,splitChar,multipleOrg,afterCloseCallFun,displaySubDeptUser){
	var showTab = "" ;
	if(selectedDataMappingConfig != null && selectedDataMappingConfig!='undefined'){
		var mappingConfigObjId = "ComplensiveMappingConfig"+new Date().getTime() ;
		var mappingConfigObj = jQuery('<input type="hidden" >').attr('id',mappingConfigObjId);
		mappingConfigObj.appendTo(jQuery("body"));
		var mappingSplitChar = selectedDataMappingConfig.splitChar ;
		if(mappingSplitChar!=null && typeof(mappingSplitChar)!='undefined'){
			splitChar = mappingSplitChar ;
		}
		$("#"+mappingConfigObjId).val(json2str(selectedDataMappingConfig));
	}
	var queryParam="?extParameter="+extParameter+"&showTab="+mappingConfigObjId ;
	if(multipleOrg != null && typeof(multipleOrg) != 'undefined'){
    	queryParam += "&multipleOrg="+multipleOrg ;
    }
	if(displaySubDeptUser != null && typeof(displaySubDeptUser) != 'undefined'){
    	queryParam += "&displaySubDeptUser="+displaySubDeptUser ;
    }
	var positionUrl=getPath() + '/avicit/platform6/modules/system/commonpopup/ComprehensiveSelector.jsp'+queryParam;
	var comprehensiveDialog = new CommonSelectorDialog(dialogDivId,dialogWidth,dialogHeight,positionUrl,dialogTitle,null,true,true);
		var buttons = [{
			text:'确定',
			id : 'comprehensiveSelectSubimt'+new Date().getTime(),
			//iconCls : 'icon-ok',
			handler:function(){
				var frmId = jQuery('#'+dialogDivId+' iframe').attr('id');
				var frm = document.getElementById(frmId).contentWindow;
				var resultData = frm.getSelectedResultDataJson();
				//业务处理
				var userId = "" ;
				var userName = "" ;
				var deptId = "" ;
				var deptName = "" ;
				var roleId = "" ;
				var roleName = "" ;
				var groupId = "" ;
				var groupName = "" ;
				var positionId = "" ;
				var positionName = "" ;
				var userCount=0;
				var deptCount=0;
				var roleCount=0;
				var groupCount=0;
				var positionCount=0;
				var tipMsg = "" ;
				for(var i=0 ; i<resultData.length;i++){
					var currData = resultData[i];
					var type = currData.type;
					var id =currData.id;
					var name = currData.name;
					if(type=='user'){
						if(userId==""){
							userId = id;
 	 					  }else{
 	 						userId +=splitChar+ id;
 	 					  }
 						if(userName==""){
 							userName = name;
 	 					  }else{
 	 						userName +=splitChar+ name;  
 	 					  }
 						userCount=userCount+1;
					}else if(type=='dept'){
						if(deptId==""){
							deptId = id;
 	 					  }else{
 	 						deptId +=splitChar+ id;
 	 					  }
 						if(deptName==""){
 							deptName = name;
 	 					  }else{
 	 						deptName +=splitChar+ name;  
 	 					  }
 						deptCount=deptCount+1;
					}else if(type=='role'){
						if(roleId==""){
							roleId = id;
 	 					  }else{
 	 						roleId +=splitChar+ id;
 	 					  }
 						if(roleName==""){
 							roleName = name;
 	 					  }else{
 	 						roleName +=splitChar+ name;  
 	 					  }
 						roleCount=roleCount+1;
					}else if(type=='group'){
						if(groupId==""){
							groupId = id;
 	 					  }else{
 	 						groupId +=splitChar+ id;
 	 					  }
 						if(groupName==""){
 							groupName = name;
 	 					  }else{
 	 						groupName +=splitChar+ name;  
 	 					  }
 						groupCount=groupCount+1;
					}else if(type=='position'){
						if(positionId==""){
 							positionId = id;
 	 					  }else{
 	 						positionId +=splitChar+ id;  
 	 					  }
 						if(positionName==""){
 							positionName = name;
 	 					  }else{
 	 						positionName +=splitChar+ name;  
 	 					  }
 						positionCount=positionCount+1;
					}
		    }
			if(selectedDataMappingConfig != null && selectedDataMappingConfig!='undefined'){
					  var msgHeader = "" ;
					  //选择数据提交前控制
					  var userLimit = selectedDataMappingConfig.user.selectCount ;
					  if(userCount>0 && userLimit!=null && userLimit!="undefined" && userLimit>0){
						  if(userCount>selectedDataMappingConfig.user.selectCount){
							     msgHeader = "用户只能选择" ;
							     if(tipMsg.length==0){
							    	 tipMsg=msgHeader+userLimit+"个";
							     }else{
							    	 tipMsg=tipMsg+"<br>"+msgHeader+userLimit+"个";
							     }
							     userId = "" ;
								 userName = "" ;
								 userCount = 0 ;
				 		  }
					  }
		 			  
		 			//选择数据提交前控制
					  var deptLimit = selectedDataMappingConfig.dept.selectCount ;
					  if(deptCount>0 && deptLimit!=null && deptLimit!="undefined" && deptLimit>0 ){
						  if(deptCount>selectedDataMappingConfig.dept.selectCount){
							    msgHeader = "部门只能选择" ;
							    if(tipMsg.length==0){
							    	 tipMsg=msgHeader+deptLimit+"个";
							     }else{
							    	 tipMsg+="<br>"+msgHeader+deptLimit+"个";
							     }
							     deptId = "" ;
								 deptName = "" ;
								 deptCount = 0 ;
				 		  }
					  }
		 			  
		 			//选择数据提交前控制
					  var roleLimit = selectedDataMappingConfig.role.selectCount ;
					  if(roleCount>0 && roleLimit!=null && roleLimit!="undefined" && roleLimit>0 ){
						  if(roleCount>selectedDataMappingConfig.role.selectCount){
							    msgHeader = "角色只能选择" ;
							    if(tipMsg.length==0){
							    	 tipMsg=msgHeader+roleLimit+"个";
							     }else{
							    	 tipMsg=tipMsg+"<br>"+msgHeader+roleLimit+"个";
							     }
							    roleId = "" ;
							    roleName = "" ;
							    roleCount = 0 ;
				 		  }
					  }
					 
		 			//选择数据提交前控制
		 			 var groupLimit = selectedDataMappingConfig.group.selectCount ;
					  if(groupCount>0 && groupLimit!=null && groupLimit!="undefined" && groupLimit>0 ){
						  if(groupCount>selectedDataMappingConfig.group.selectCount){
							    msgHeader = "群组只能选择" ;
							    if(tipMsg.length==0){
							    	 tipMsg=msgHeader+groupLimit+"个";
							     }else{
							    	 tipMsg=tipMsg+"<br>"+msgHeader+groupLimit+"个";
							     }
							    grouptId = "" ;
							    groupName = "" ;
							    groupCount = 0 ;
				 		  }
					  }
		 			//选择数据提交前控制
					  var positionLimit = selectedDataMappingConfig.position.selectCount ;
					  if(positionCount>0 && positionLimit!=null && positionLimit!="undefined" && positionLimit>0){
						  if(positionCount>selectedDataMappingConfig.position.selectCount){
							    msgHeader = "岗位只能选择" ;
							    if(tipMsg.length==0){
							    	 tipMsg=msgHeader+positionLimit+"个";
							     }else{
							    	 tipMsg=tipMsg+"<br>"+msgHeader+positionLimit+"个";
							     }
							    positionId = "" ;
							    positionName = "" ;
							    positionCount = 0 ;
				 		  }
					  }
		 			
		   	       }
			   if(tipMsg.length>0){
	 					jQuery.messager.alert("操作提示", tipMsg,"info");
	 					tipMsg = "" ;
	 					return;
	 		    }
		 		var retValue = {
						"user":{"userId":userId,"userName":userName},
						"dept":{"deptId":deptId,"deptName":deptName},
						"role":{"roleId":roleId,"roleName":roleName},
						"group":{"groupId":groupId,"groupName":groupName},
						"position":{"positionId":positionId,"positionName":positionName}
				 } ;
		 		 var funRetValue = null;
				 if(callBackFun!=null && callBackFun!='undefined'){
					 //zl
					 //funRetValue = eval(callBackFun + "(retValue,dialogDivId)");
					 if(typeof(callBackFun) === 'function'){
						 funRetValue = callBackFun(retValue,dialogDivId);
	 				    }else if(typeof(callBackFun) ==='string'){
	 				    	funRetValue = eval(callBackFun + "(retValue,dialogDivId)");
	 				    }else{
	 					   throw new Error("非法的回调函数！");
	 				    }
		   		 }else{
		   			 if(selectedDataMappingConfig != null && selectedDataMappingConfig!='undefined'){
		   				//addParentSelectedDataToGrid(selectedDataMappingConfig) ;
		   				var selectedUser = selectedDataMappingConfig.user;
		   				var selectedDept = selectedDataMappingConfig.dept;
		   				var selectedRole = selectedDataMappingConfig.role;
		   				var selectedGroup = selectedDataMappingConfig.group;
		   				var selectedPosition = selectedDataMappingConfig.position;
		   				 if(selectedUser != null && typeof(selectedUser) != 'undefined'){
		   					var userIdStr = selectedUser.userId ;
		   					var userNameStr =  selectedUser.userName ;
		   					if(userIdStr != null && typeof(userIdStr) != 'undefined'){
		   					   $("#"+userIdStr).val(userId);
		   					}
		   					if(userNameStr != null && typeof(userNameStr) != 'undefined'){
		   						$("#"+userNameStr).val(userName);
		   					}
		   					
		   				 }
		   				 if(selectedDept != null && typeof(selectedDept) != 'undefined'){
		   					var detpIdStr = selectedDept.deptId ;
		   					var detpNameStr =  selectedDept.deptName ; 
		   					if(detpIdStr != null && typeof(detpIdStr) != 'undefined'){
		   						$("#"+detpIdStr).val(deptId);
		   					}
		   					if(detpNameStr != null && typeof(detpNameStr) != 'undefined'){
		   						$("#"+detpNameStr).val(deptName);
		   					}
		   				 }
		   				 if(selectedRole != null && typeof(selectedRole) != 'undefined'){
		   					var roleIdStr = selectedRole.roleId ;
		   					var roleNameStr =  selectedRole.roleName ;
		   					if(roleIdStr != null && typeof(roleIdStr) != 'undefined'){
		   						$("#"+roleIdStr).val(roleId);
		   					}
		   					if(roleNameStr != null && typeof(roleNameStr) != 'undefined'){
		   						$("#"+roleNameStr).val(roleName);
		   					}
		   				 }
		   				 if(selectedGroup != null && typeof(selectedGroup) != 'undefined'){
		   					var groupIdStr = selectedGroup.groupId ;
		   					var groupNameStr =  selectedGroup.groupName ; 
		   					if(groupIdStr != null && typeof(groupIdStr) != 'undefined'){
		   						$("#"+groupIdStr).val(groupId);
		   					}
		   					if(groupNameStr != null && typeof(groupNameStr) != 'undefined'){
		   						$("#"+groupNameStr).val(groupName);
		   					}
		   				 }
		   				 if(selectedPosition != null && typeof(selectedPosition) != 'undefined'){
		   					var positionIdStr = selectedPosition.positionId ;
		   					var positionNameStr =  selectedPosition.positionName ;
		   					if(positionIdStr != null && typeof(positionIdStr) != 'undefined'){
		   						$("#"+positionIdStr).val(positionId);
		   					}
		   					if(positionNameStr != null && typeof(positionNameStr) != 'undefined'){
		   						$("#"+positionNameStr).val(positionName);
		   					}
		   				 }
		   			 }
		   		}
				 if(funRetValue==null || typeof(funRetValue)=="undefined" || funRetValue==true){
				   comprehensiveDialog.close();
				   if(afterCloseCallFun != null && afterCloseCallFun != 'undefined'){
					   //zl 
					   //eval(afterCloseCallFun+ "(retValue,dialogDivId)");
					   if(typeof(afterCloseCallFun) === 'function'){
							afterCloseCallFun(retValue,dialogDivId);
	 				    }else if(typeof(afterCloseCallFun) ==='string'){
	 				    	eval(afterCloseCallFun + "(rowIndex,retValue,dialogDivId)");
	 				    }else{
	 					   throw new Error("非法的回调函数！");
	 				    }
						 
					}
				 }
	 }
  }];
		comprehensiveDialog.createButtonsInDialog(buttons);
		comprehensiveDialog.show();
};
/**----------------------------------------------------------------------datagrid选择--------------------------------------------------------------------------------------------------------------------------------------------------**/
/**
  * 创建GridCommonSelector  
 * @param selectType                  必填     选择人员-user 部门-dept 角色-role  群组-group  岗位-position
 * @param dataGridId                  必填     弹出选择框所在datagrid的ID
 * @param rowIndex                    必填     回写选择的数据需要用的页面元素Id
 * @param selectDialogShowFieldName   必填     回写选择的数据需要用的页面元素Id  
 * @param filedMappingConfig          可选     回写用户ID所属部门需要用的页面元素Id（当selectType='user' 且需要回填部门时此项必填）
 * @param callBackFun                 可选     回写用户所属部门名称需要用的页面元素Id（当selectType='user' 且需要回填部门时此项必填）
 * @param extParameter                可选     自定义页面初始化数据(字符串) 格式：“{参数1:参数值1,参数2:参数值2.....}”例如：‘{orgId:"",deptId:""}’
 * @param dialogWidth                 可选     默认值 600
 * @param dialogHeight                可选     默认值 460
 * @param singleSelect                可选     默认值 true  可填写值：true/false/-1/数字  当填写false/-1  表示选择多少项不受限制 当填写数字时将限定选择数量不大于填写的数字
 * @param splitChar                   可选     返回值分割符 默认逗号（,）
 * @param multipleOrg                 是否显示多个组织根节点 可选：1或不设置 代表当前登录人所在组织 n代表多组织 显示几个组织由项目开发人员在java代码中控制
 * @param afterCloseCallFun           需要选人框关闭后做一些业务处理，开发人员自己将业务处理的函数名称以字符串传递即可
 * @param displaySubDeptUser          传递“Y” 当部门节点处于checked状态是将该部门下人员与递归将该部门的子部门下人员取出 不填值或填除“Y”之外的其他任意值将只读取当前部门的直属人员
 */
GridCommonSelector = function(selectType,dataGridId,rowIndex,selectDialogShowFieldName,filedMappingConfig,callBackFun,extParameter,dialogWidth,dialogHeight,singleSelect,splitChar,afterCloseCallFun,multipleOrg,displaySubDeptUser){
	      this.init = function(rowData){
	    	if(dataGridId != null && rowIndex!=null && selectDialogShowFieldName!=null){
	                    	var defaultTitle = "" ;
	                		if(selectType=='user'){
	                			defaultTitle = "选择人员" ;
	                		}else if(selectType=='dept'){
	                			defaultTitle = "选择部门" ;
	                		}else if(selectType=='role'){
	                			defaultTitle = "选择角色" ;
	                		}else if(selectType=='group'){
	                			defaultTitle = "选择群组" ;
	                		}else if(selectType=='position'){
	                			defaultTitle = "选择岗位" ;
	                		}else{
	                			defaultTitle = "综合选择" ;
	                		}
	                		var dialogTitle = defaultTitle ;
	                		var dialogDivId = "_comframe_id_" + ( Math.random() + "" ).split(".")[1];
	                		
	                		if(dialogWidth==null || typeof(dialogWidth)=='undefined'){
	                	    	dialogWidth = 600 ;
	                	    }
	                		if(dialogHeight==null || typeof(dialogHeight)=='undefined'){
	                			dialogHeight=460;
	                	    }
	                		if(singleSelect==null || typeof(singleSelect)=='undefined'){
	                			singleSelect = true ;
	                		} 
	                		if(splitChar==null || typeof(splitChar)=='undefined'){
	                			splitChar = ',' ;
	                		}
	                		if(extParameter==null || typeof(extParameter)=='undefined'){
	                			extParameter = '' ;
	                		}
	                    	if(selectType=='user'){
	                    	   gridSelectUser(rowIndex,dataGridId,filedMappingConfig,rowData,callBackFun,dialogDivId,dialogTitle,dialogWidth,dialogHeight,extParameter,singleSelect,splitChar,afterCloseCallFun,multipleOrg,displaySubDeptUser);
	            			}else if(selectType=='dept'){
	            				gridSelectDept(rowIndex,dataGridId,filedMappingConfig,rowData,callBackFun,dialogDivId,dialogTitle,dialogWidth,dialogHeight,extParameter,singleSelect,splitChar,afterCloseCallFun,multipleOrg,displaySubDeptUser);
	            			}else if(selectType=='role'){
	            				gridSelectRole(rowIndex,dataGridId,filedMappingConfig,rowData,callBackFun,dialogDivId,dialogTitle,dialogWidth,dialogHeight,extParameter,singleSelect,splitChar,afterCloseCallFun,multipleOrg);
	            			}else if(selectType=='group'){
	            				gridSelectGroup(rowIndex,dataGridId,filedMappingConfig,rowData,callBackFun,dialogDivId,dialogTitle,dialogWidth,dialogHeight,extParameter,singleSelect,splitChar,afterCloseCallFun,multipleOrg);
	            			}else if(selectType=='position'){
	            				gridSelectPosition(rowIndex,dataGridId,filedMappingConfig,rowData,callBackFun,dialogDivId,dialogTitle,dialogWidth,dialogHeight,extParameter,singleSelect,splitChar,afterCloseCallFun,multipleOrg);
	            			}else{
	            				selectComprehensiveData(rowIndex,dataGridId,callBackFun,rowData,dialogDivId,dialogTitle,dialogWidth,dialogHeight,extParameter,singleSelect,splitChar,afterCloseCallFun,multipleOrg,displaySubDeptUser);
	            			}
	            }  
	      }
};
/**
 * 选择人员
 * @param rowIndex          必填    datagrid当前行index
 * @param dataGridId        必填    当前操作的datagrid的ID
 * @param fieldMapping    必填    字段映射
 * @param rowData           必填    字段映射
 * @param dialogTitle         可选    dialog的标题
 * @param dialogWidth       可选   默认值 550
 * @param dialogHeight      可选   默认值 460
 * @param singleSelect        可选  默认值 true  可填写值：true/false
 * @param splitChar            可选  返回值分割符 默认逗号（,） 
 */
function gridSelectUser(rowIndex,dataGridId,fieldMapping,rowData,callBackFun,dialogDivId,dialogTitle,dialogWidth,dialogHeight,extParameter,singleSelect,splitChar,afterCloseCallFun,multipleOrg,displaySubDeptUser){
	    var fieldMapingJson = fieldMapping;
	    var rowUserIdProperty = fieldMapingJson.targetId;
	    var rowUserIdPropertyValue ="";
	    if(null !=rowData && typeof(rowData)!='undefined'){
	    	rowUserIdPropertyValue = rowData[rowUserIdProperty] ;
	    }
	    var queryParam='?singleSelect='+singleSelect +"&selectType=user&historyId="+rowUserIdPropertyValue+"&extParameter="+extParameter ;
	    if(multipleOrg != null && typeof(multipleOrg) != 'undefined'){
	    	queryParam += "&multipleOrg="+multipleOrg ;
	    }
	    if(displaySubDeptUser != null && typeof(displaySubDeptUser) != 'undefined'){
	    	queryParam += "&displaySubDeptUser="+displaySubDeptUser ;
	    }
	    var reqUrl=getPath() + '/platform/commonSelection/commonSelectionController/getOrg'+queryParam;
	    if(afterCloseCallFun != null && typeof(afterCloseCallFun) != 'undefined'){
	    	afterCloseCallFun = afterCloseCallFun+"("+rowIndex+")";
	    }
	    var gridSelectUserDialog = new CommonSelectorDialog(dialogDivId,dialogWidth,dialogHeight,reqUrl,dialogTitle,afterCloseCallFun,true,true);
 		var buttons = [{
			 			text:'确定',
			 			id : 'GridUserSelectSubimt'+new Date().getTime(),
			 			//iconCls : 'icon-ok',
			 			handler:function(){
			 				var frmId = jQuery('#'+dialogDivId+' iframe').attr('id');
			 				var frm = document.getElementById(frmId).contentWindow;
			 				var resultData = frm.getSelectedResultDataJson();
			 				//选择数据提交前控制
			 				if(!submitLimitCheck(singleSelect,resultData.length,"user")){
			 					return ;
			 				}
			 					//业务处理
				 				var userId="";
				 				var userName="";
				 				var deptId = "" ;
				 				var deptName = "" ;
				 				for(var i=0 ; i<resultData.length;i++){
				 					var userDept = resultData[i];
				 					  if(userId==""){
				  						 userId = userDept.userId;
				  					  }else{
				  						 userId +=splitChar+ userDept.userId;  
				  					  }
				  					 if(userName==""){
				  						userName = userDept.userName;
				  					  }else{
				  						 userName +=splitChar+ userDept.userName;  
				  					  }
				  					 var deptIdProperty = fieldMapingJson.deptId;
				  					if(deptIdProperty != null && typeof(deptIdProperty) != 'undefined'){
				  						if(deptId==""){
				  	  						deptId = userDept.deptId;
				  	 					  }else{
				  	 						  deptId +=splitChar+ userDept.deptId;  
				  	 					  }
				  						 if(deptName==""){
				  	  						deptName = userDept.deptName;
				  	 					  }else{
				  	 						  deptName +=splitChar+ userDept.deptName;  
				  	 					  }
				  					}
				 				}
				 				var retValue = {
			    						"userId": userId,
			    						"userName": userName,
			    						"deptId":deptId,
			    						"deptName":deptName
			    					} ;
				 				var funRetValue = null;
				 				if(callBackFun!=null && callBackFun!='undefined'){
				 					//zl
				 					//funRetValue = eval(callBackFun + "(rowIndex,retValue,dialogDivId)");
				 					if(typeof(callBackFun) === 'function'){
			 							funRetValue = callBackFun(rowIndex,retValue,dialogDivId);
				 				    }else if(typeof(callBackFun) ==='string'){
				 				    	funRetValue = eval(callBackFun + "(rowIndex,retValue,dialogDivId)");
				 				    }else{
				 					   throw new Error("非法的回调函数！");
				 				    }
				    			}else{
				    				 var rowUserNameProperty = fieldMapingJson.targetName;
				    				//var retInfo =eval('[{"'+rowUserIdProperty+'":"'+userId+'","'+rowUserNameProperty+'":"'+userName+'"}]');
				    				 var dataGridObj = jQuery('#'+dataGridId) ;
				    				 /** dataGridObj.datagrid("selectRow",rowIndex);
				    				 var row  = dataGridObj.datagrid("getSelected");
				    				row[rowUserIdProperty]= userId;
				    				row[rowUserNameProperty]= userName;
				    				dataGridObj.datagrid('updateRow',{
				    					index: rowIndex,
				    					row: row
				    				});*/
				    				dataGridObj.datagrid('beginEdit', rowIndex);
				    				var edUserId = dataGridObj.datagrid('getEditor', {index:rowIndex,field:rowUserIdProperty});
				    				if(edUserId == null || typeof(edUserId) =='undefined' ){
				    					edUserId = addEditor(dataGridId,rowUserIdProperty,rowIndex,'text');
				    				}
				    				$(edUserId.target).val(userId);
				    				var edUserName = dataGridObj.datagrid('getEditor', {index:rowIndex,field:rowUserNameProperty});
				    				$(edUserName.target).children("input").val(userName);
				    				dataGridObj.datagrid('endEdit', rowIndex);
				    			}
				 				if(funRetValue==null || typeof(funRetValue)=="undefined" || funRetValue==true){
				 				  gridSelectUserDialog.close();
				 				}
		 			}
 	    	}
		];
 		gridSelectUserDialog.createButtonsInDialog(buttons);
 		gridSelectUserDialog.show();
 	};
/**
 * 选择部门
 * @param rowIndex          必填    datagrid当前行index
  * @param dataGridId        必填    当前操作的datagrid的ID
 * @param fieldMapping    必填    字段映射
 * @param rowData           当前操作行对应的数据
 * @param dialogTitle         可选    dialog的标题
 * @param dialogWidth       可选   默认值 550
 * @param dialogHeight      可选   默认值 460
 * @param singleSelect        可选  默认值 true  可填写值：true/false
 * @param splitChar            可选  返回值分割符 默认逗号（,） 
 */
function gridSelectDept(rowIndex,dataGridId,fieldMapping,rowData,callBackFun,dialogDivId,dialogTitle,dialogWidth,dialogHeight,extParameter,singleSelect,splitChar,afterCloseCallFun,multipleOrg,displaySubDeptUser){
	    var fieldMapingJson = fieldMapping;
	    var rowDeptIdProperty = fieldMapingJson.targetId;
	    var rowDeptIdPropertyValue = "" ;
	    if(null !=rowData && typeof(rowData)!='undefined'){
	    	rowDeptIdPropertyValue = rowData[rowDeptIdProperty] ;
	    }
	    var queryParam='?singleSelect='+singleSelect +"&selectType=dept&historyId="+rowDeptIdPropertyValue+"&extParameter="+extParameter ;
	    if(multipleOrg != null && typeof(multipleOrg) != 'undefined'){
	    	queryParam += "&multipleOrg="+multipleOrg ;
	    }
	    var deptUrl=getPath() + '/platform/commonSelection/commonSelectionController/getOrg'+queryParam;
	    if(afterCloseCallFun != null && typeof(afterCloseCallFun) != 'undefined'){
	    	afterCloseCallFun = afterCloseCallFun+"("+rowIndex+")";
	    }
	 	var deptCommonDialog = new CommonSelectorDialog(dialogDivId,dialogWidth,dialogHeight,deptUrl,dialogTitle,afterCloseCallFun,true,true);
 		var buttons = [
 		   {
	 			text:'确定',
	 			id : 'gridDeptSelectSubimt'+new Date().getTime(),
	 			//iconCls : 'icon-ok',
	 			handler:function(){
	 				var frmId = jQuery('#'+dialogDivId+' iframe').attr('id');
	 				var frm = document.getElementById(frmId).contentWindow;
	 				var resultData = frm.getSelectedResultDataJson();
	 				//选择数据提交前控制
	 				if(!submitLimitCheck(singleSelect,resultData.length,"dept")){
	 					return ;
	 				}
	 				//业务处理
	 				var deptId = "" ;
	 				var deptName = "" ;
	 				var deptIdProperty = fieldMapingJson.targetId;
	 				for(var i=0 ; i<resultData.length;i++){
	 					var userDept = resultData[i];
	  					if(deptIdProperty != null && typeof(deptIdProperty) != 'undefined'){
	  						if(deptId==""){
	  	  						deptId = userDept.deptId;
	  	 					  }else{
	  	 						  deptId +=splitChar+ userDept.deptId;  
	  	 					  }
	  						if(deptName==""){
	  	  						deptName = userDept.deptName;
	  	 					  }else{
	  	 						  deptName +=splitChar+ userDept.deptName;  
	  	 					  }
	  					}
	 				}
	 				var retValue = {
    						"deptId":deptId,
    						"deptName":deptName
    					} ;
	 				var funRetValue = null;
	 				if(callBackFun!=null && callBackFun!='undefined'){
	 					if(typeof(callBackFun) === 'function'){
 							funRetValue = callBackFun(rowIndex,retValue,dialogDivId);
	 				    }else if(typeof(callBackFun) ==='string'){
	 				    	funRetValue = eval(callBackFun + "(rowIndex,retValue,dialogDivId)");
	 				    }else{
	 					   throw new Error("非法的回调函数！");
	 				    }
	    			}else{
	    				 var rowDeptNameProperty = fieldMapingJson.targetName;
	    				 var dataGridObj = jQuery('#'+dataGridId) ;
	    				/** dataGridObj.datagrid("selectRow",rowIndex);
		    			 var row = dataGridObj.datagrid("getSelected");
		    			 row[rowDeptIdProperty]= deptId;
		    			 row[rowDeptNameProperty]= deptName;
		    			 dataGridObj.datagrid('beginEdit', rowIndex);
		    			 dataGridObj.datagrid('updateRow',{
		    					index: rowIndex,
		    					row: row
		    			 });**/
		    			 dataGridObj.datagrid('beginEdit', rowIndex);
		    			 var edDeptId = dataGridObj.datagrid('getEditor', {index:rowIndex,field:rowDeptIdProperty});
		    			 if(edDeptId == null || typeof(edDeptId) =='undefined' ){
		    				 edDeptId = addEditor(dataGridId,rowDeptIdProperty,rowIndex,'text');
		    			 }
		    			 $(edDeptId.target).val(deptId);
		    			 var edDeptName = dataGridObj.datagrid('getEditor', {index:rowIndex,field:rowDeptNameProperty});
		    			 $(edDeptName.target).children("input").val(deptName);
		    			 //$(edDeptName.target).CommonSelector('setValue',deptName);
		    			 
		    			 dataGridObj.datagrid('endEdit', rowIndex);
	    			}
	 				if(funRetValue==null || typeof(funRetValue)=="undefined" || funRetValue==true){
					    deptCommonDialog.close();
	 				}
 			}
 		}];
 		deptCommonDialog.createButtonsInDialog(buttons);
 		deptCommonDialog.show();
 	};
/**
 * 
* @param rowIndex           必填    datagrid当前行index
  * @param dataGridId        必填    当前操作的datagrid的ID
 * @param fieldMapping    必填    字段映射
 * @param rowData            必填    字段映射
 * @param dialogTitle
 * @param dialogWidth
 * @param dialogHeight
 * @param singleSelect
 * @param splitChar
 */
function gridSelectRole(rowIndex,dataGridId,fieldMapping,rowData,callBackFun,dialogDivId,dialogTitle,dialogWidth,dialogHeight,extParameter,singleSelect,splitChar,afterCloseCallFun,multipleOrg){
	  var fieldMapingJson = fieldMapping;
	  var rowRoleIdProperty = fieldMapingJson.targetId;
	  var rowRoleIdPropertyValue ="";
	  if(null !=rowData && typeof(rowData)!='undefined'){
	    	rowRoleIdPropertyValue = rowData[rowRoleIdProperty] ;
	   }
	var queryParam='?singleSelect='+singleSelect +"&historyId="+rowRoleIdPropertyValue+"&extParameter="+extParameter ;
	if(multipleOrg != null && typeof(multipleOrg) != 'undefined'){
    	queryParam += "&multipleOrg="+multipleOrg ;
    }
	var roleUrl=getPath() + '/avicit/platform6/modules/system/commonpopup/RoleSelect.jsp'+queryParam;
	if(afterCloseCallFun != null && typeof(afterCloseCallFun) != 'undefined'){
	    afterCloseCallFun = afterCloseCallFun+"("+rowIndex+")";
	}
	 var roleCommonDialog = new CommonSelectorDialog(dialogDivId,dialogWidth,dialogHeight,roleUrl,dialogTitle,afterCloseCallFun,true,true);
		var buttons = [{
 			text:'确定',
 			id : 'GridRoleSelectSubimt'+new Date().getTime(),
 			//iconCls : 'icon-ok',
 			handler:function(){
 				var frmId = jQuery('#'+dialogDivId+' iframe').attr('id');
 				var frm = document.getElementById(frmId).contentWindow;
 				var resultData = frm.getSelectedResultDataJson();
 				//选择数据提交前控制
 				if(!submitLimitCheck(singleSelect,resultData.length,"role")){
 					return ;
 				}
 				//业务处理
 				var roleId = "" ;
 				var roleName = "" ;
 				for(var i=0 ; i<resultData.length;i++){
 					var roles = resultData[i];
  					if(rowRoleIdProperty != null && rowRoleIdProperty != 'undefined'){
  						if(roleId==""){
  							roleId = roles.id;
  	 					  }else{
  	 						roleId +=splitChar+ roles.id;  
  	 					  }
  						if(roleName==""){
  							roleName = roles.roleName;
  	 					  }else{
  	 						roleName +=splitChar+ roles.roleName;  
  	 					  }
  					}
 				}
 				//var retValue = "{\"roleId\":\""+roleId+"\",\"roleName\":\""+roleName+"\"}";
 				var retValue = {"roleId":roleId,"roleName":roleName};
 				var funRetValue = null ;
 				if(callBackFun!=null && callBackFun!='undefined'){
 					//zl
 					//funRetValue = eval(callBackFun + "(rowIndex,resultData,dialogDivId)");
 					if(typeof(callBackFun) === 'function'){
 						funRetValue = callBackFun(rowIndex,resultData,dialogDivId);
 				    }else if(typeof(callBackFun) ==='string'){
 				    	funRetValue = eval(callBackFun + "(rowIndex,resultData,dialogDivId)");
 				    }else{
 					   throw new Error("非法的回调函数！");
 				    }
    			}else{
    				 var rowRoleNameProperty = fieldMapingJson.targetName;
    				 var dataGridObj = jQuery('#'+dataGridId) ;
    				 /**var row = dataGridObj.datagrid("selectRow",rowIndex);
	    			 row[rowRoleIdProperty]= roleId;
	    			 row[rowRoleNameProperty]= roleName;
	    			 dataGridObj.datagrid('beginEdit', rowIndex);
	    			 dataGridObj.datagrid('updateRow',{
	    					index: rowIndex,
	    					row: row
	    			 });**/
	    			 dataGridObj.datagrid('beginEdit', rowIndex);
	    			 var edRoleId = dataGridObj.datagrid('getEditor', {index:rowIndex,field:rowRoleIdProperty});
	    			 if(edRoleId == null || typeof(edRoleId) =='undefined' ){
	    				 edRoleId = addEditor(dataGridId,rowRoleIdProperty,rowIndex,'text');
	    			 }
	    			 $(edRoleId.target).val(roleId);
	    			 var edRoleName = dataGridObj.datagrid('getEditor', {index:rowIndex,field:rowRoleNameProperty});
	    			 $(edRoleName.target).children("input").val(roleName);
	    			 dataGridObj.datagrid('endEdit', rowIndex);
    			}
 				if(funRetValue==null || typeof(funRetValue)=="undefined" || funRetValue==true){
				   roleCommonDialog.close();
 				}
			}
		}];
		roleCommonDialog.createButtonsInDialog(buttons);
		roleCommonDialog.show();
};

/**
 * 
 * @param rowIndex          必填    datagrid当前行index
  * @param dataGridId        必填    当前操作的datagrid的ID
 * @param fieldMapping    必填    字段映射
 *@param rowData 
 * @param dialogTitle
 * @param dialogWidth
 * @param dialogHeight
 * @param singleSelect
 * @param splitChar
 */
function gridSelectGroup(rowIndex,dataGridId,fieldMapping,rowData,callBackFun,dialogDivId,dialogTitle,dialogWidth,dialogHeight,extParameter,singleSelect,splitChar,afterCloseCallFun,multipleOrg){
	 var fieldMapingJson = fieldMapping;
	  var rowGroupIdProperty = fieldMapingJson.targetId;
	  var rowGroupIdPropertyValue ="";
	  if(null !=rowData && typeof(rowData)!='undefined'){
		  rowGroupIdPropertyValue = rowData[rowGroupIdProperty] ;
	   }
	var queryParam='?singleSelect='+singleSelect +"&historyId="+rowGroupIdPropertyValue+"&extParameter="+extParameter ;
	if(multipleOrg != null && typeof(multipleOrg) != 'undefined'){
    	queryParam += "&multipleOrg="+multipleOrg ;
    }
	var groupUrl=getPath() + '/avicit/platform6/modules/system/commonpopup/GroupSelect.jsp'+queryParam;
	if(afterCloseCallFun != null && typeof(afterCloseCallFun) != 'undefined'){
	    	afterCloseCallFun = afterCloseCallFun+"("+rowIndex+")";
	}
	var groupCommonDialog = new CommonSelectorDialog(dialogDivId,dialogWidth,dialogHeight,groupUrl,dialogTitle,afterCloseCallFun,true,true);
		var buttons = [{
 			text:'确定',
 			id : 'GridGroupSelectSubimt'+new Date().getTime(),
 			//iconCls : 'icon-ok',
 			handler:function(){
 				var frmId = jQuery('#'+dialogDivId+' iframe').attr('id');
 				var frm = document.getElementById(frmId).contentWindow;
 				var resultData = frm.getSelectedResultDataJson();
 				//选择数据提交前控制
 				if(!submitLimitCheck(singleSelect,resultData.length,"group")){
 					return ;
 				}
 				//业务处理
 				var groupId = "" ;
 				var groupName = "" ;
 				for(var i=0 ; i<resultData.length;i++){
 					var groups = resultData[i];
  					if(rowGroupIdProperty != null && typeof(rowGroupIdProperty) != 'undefined'){
  						if(groupId==""){
  							groupId = groups.id;
  	 					  }else{
  	 						groupId +=splitChar+ groups.id;  
  	 					  }
  						if(groupName==""){
  							groupName = groups.groupName;
  	 					  }else{
  	 						groupName +=splitChar+ groups.groupName;  
  	 					  }
  					}
 				}
 				var retValue = {
 						"groupId":groupId,
 						"groupName":groupName
					} ;
 				var funRetValue = null ;
 				if(callBackFun!=null && callBackFun!='undefined'){
 					//zl
 					//funRetValue = eval(callBackFun + "(rowIndex,retValue,dialogDivId)");
 					if(typeof(callBackFun) === 'function'){
 						funRetValue = callBackFun(rowIndex,retValue,dialogDivId);
 				    }else if(typeof(callBackFun) ==='string'){
 				    	funRetValue = eval(callBackFun + "(rowIndex,retValue,dialogDivId)");
 				    }else{
 					   throw new Error("非法的回调函数！");
 				    }
    			}else{
    				var rowGroupNameProperty = fieldMapingJson.targetName;
    				var dataGridObj = jQuery('#'+dataGridId) ;
    				dataGridObj.datagrid("selectRow",rowIndex);
    				/*var row  = dataGridObj.datagrid("getSelected");
    				row[rowGroupIdProperty]= groupId;
    				row[rowGroupNameProperty]= groupName;
    				dataGridObj.datagrid('beginEdit', rowIndex);
    				dataGridObj.datagrid('updateRow',{
    					index: rowIndex,
    					row: row
    				});*/
    				dataGridObj.datagrid('beginEdit', rowIndex);
	    			var edGroupId = dataGridObj.datagrid('getEditor', {index:rowIndex,field:rowGroupIdProperty});
	    			if(edGroupId == null || typeof(edGroupId) =='undefined' ){
	    				edGroupId = addEditor(dataGridId,rowGroupIdProperty,rowIndex,'text');
	    			}
	    			$(edGroupId.target).val(groupId);
	    			var edGroupName = dataGridObj.datagrid('getEditor', {index:rowIndex,field:rowGroupNameProperty});
	    			$(edGroupName.target).children("input").val(groupName);
	    			dataGridObj.datagrid('endEdit', rowIndex);
    			}
 				if(funRetValue==null || typeof(funRetValue)=="undefined" || funRetValue==true){
				    groupCommonDialog.close();
 				}
			}
		}];
		groupCommonDialog.createButtonsInDialog(buttons);
		groupCommonDialog.show();
};
/**
 * 
 * @param rowIndex          必填    datagrid当前行index
 * @param dataGridId        必填    当前操作的datagrid的ID
 * @param fieldMapping    必填    字段映射
 * @param dialogTitle
 * @param dialogWidth
 * @param dialogHeight
 * @param singleSelect
 * @param splitChar
 */
function gridSelectPosition(rowIndex,dataGridId,fieldMapping,rowData,callBackFun,dialogDivId,dialogTitle,dialogWidth,dialogHeight,extParameter,singleSelect,splitChar,afterCloseCallFun,multipleOrg){
	 var fieldMapingJson = fieldMapping;
	  var rowPositionIdProperty = fieldMapingJson.targetId;
	  var rowPositionIdPropertyValue ="";
	  if(null !=rowData && typeof(rowData)!='undefined'){
		  rowPositionIdPropertyValue = rowData[rowPositionIdProperty] ;
	   }
	var queryParam='?singleSelect='+singleSelect+"&historyId="+rowPositionIdPropertyValue+"&extParameter="+extParameter ;
	if(multipleOrg != null && typeof(multipleOrg) != 'undefined'){
    	queryParam += "&multipleOrg="+multipleOrg ;
    }
	var positionUrl=getPath() + '/avicit/platform6/modules/system/commonpopup/PositionSelect.jsp'+queryParam;
	if(afterCloseCallFun != null && typeof(afterCloseCallFun) != 'undefined'){
		//zl
    	afterCloseCallFun = afterCloseCallFun+"("+rowIndex+")";
//		if(typeof(afterCloseCallFun) === 'function'){
//			afterCloseCallFun = afterCloseCallFun(rowIndex);
//	    }else if(typeof(afterCloseCallFun) ==='string'){
//	    	afterCloseCallFun = eval(afterCloseCallFun + "(rowIndex)");
//	    }else{
//		   throw new Error("非法的回调函数！");
//	    }
	}
	var positionCommonDialog = new CommonSelectorDialog(dialogDivId,dialogWidth,dialogHeight,positionUrl,dialogTitle,afterCloseCallFun,true,true);
		var buttons = [{
 			text:'确定',
 			id : 'GridPositionSelectSubimt'+new Date().getTime(),
 			//iconCls : 'icon-ok',
 			handler:function(){
 				var frmId = jQuery('#'+dialogDivId+' iframe').attr('id');
 				var frm = document.getElementById(frmId).contentWindow;
 				var resultData = frm.getSelectedResultDataJson();
 				//选择数据提交前控制
 				if(!submitLimitCheck(singleSelect,resultData.length,"position")){
 					return ;
 				}
 				//业务处理
 				var positionId = "" ;
 				var positionName = "" ;
 				for(var i=0 ; i<resultData.length;i++){
 					var positions = resultData[i];
  					if(rowPositionIdProperty != null && typeof(rowPositionIdProperty) != 'undefined'){
  						if(positionId==""){
  							positionId = positions.id;
  	 					  }else{
  	 						positionId +=splitChar+ positions.id;  
  	 					  }
  						if(positionName==""){
  							positionName = positions.positionName;
  	 					  }else{
  	 						positionName +=splitChar+ positions.positionName;  
  	 					  }
  					}
 				}
 				var retValue = {
 						"positionId":positionId,
 						"positionName":positionName
					} ;
 				var funRetValue = null ;
 				if(callBackFun!=null && callBackFun!='undefined'){
 					//zl
 					//funRetValue = eval(callBackFun + "(rowIndex,retValue,dialogDivId)");
 					if(typeof(callBackFun) === 'function'){
 						funRetValue = callBackFun(rowIndex,retValue,dialogDivId);
 				    }else if(typeof(callBackFun) ==='string'){
 				    	funRetValue = eval(callBackFun + "(rowIndex,retValue,dialogDivId)");
 				    }else{
 					   throw new Error("非法的回调函数！");
 				    }
    			}else{
    				var rowPositionNameProperty = fieldMapingJson.targetName;
    				var dataGridObj = jQuery('#'+dataGridId) ;
    				dataGridObj.datagrid("selectRow",rowIndex);
    				/**var row  = dataGridObj.datagrid("getSelected");
    				row[rowPositionIdProperty]= positionId;
    				row[rowPositionNameProperty]= positionName;
    				dataGridObj.datagrid('beginEdit', rowIndex);
    				dataGridObj.datagrid('updateRow',{
    					index: rowIndex,
    					row: row
    				});**/
    				 dataGridObj.datagrid('beginEdit', rowIndex);
	    			 var edPositionId = dataGridObj.datagrid('getEditor', {index:rowIndex,field:rowPositionIdProperty});
	    			 if(edPositionId == null || typeof(edPositionId) =='undefined' ){
	    				 edPositionId = addEditor(dataGridId,rowPositionIdProperty,rowIndex,'text');
		    		 }
	    			 $(edPositionId.target).val(positionId);
	    			 var edPositionName = dataGridObj.datagrid('getEditor', {index:rowIndex,field:rowPositionNameProperty});
	    			 $(edPositionName.target).children("input").val(positionName);
	    			 dataGridObj.datagrid('endEdit', rowIndex);
    				
    			}
 				if(funRetValue==null || typeof(funRetValue)=="undefined" || funRetValue==true){
			    	positionCommonDialog.close();
 				}
			}
		}];
		positionCommonDialog.createButtonsInDialog(buttons);
		positionCommonDialog.show();
};
/**
 * grid综合选择
 * @param rowIndex
 * @param dataGridId
 * @param callBackFun
 * @param rowData
 * @param dialogDivId
 * @param dialogTitle
 * @param dialogWidth
 * @param dialogHeight
 * @param extParameter
 * @param singleSelect
 * @param splitChar
 * @param afterCloseCallFun
 * @param multipleOrg
 */
function selectComprehensiveData(rowIndex,dataGridId,callBackFun,rowData,dialogDivId,dialogTitle,dialogWidth,dialogHeight,extParameter,singleSelect,splitChar,afterCloseCallFun,multipleOrg,displaySubDeptUser,displaySubDeptUser){
	var queryParam='?singleSelect='+singleSelect+"&extParameter="+extParameter ;
	if(multipleOrg != null && typeof(multipleOrg) != 'undefined'){
    	queryParam += "&multipleOrg="+multipleOrg ;
    }
	if(displaySubDeptUser != null && typeof(displaySubDeptUser) != 'undefined'){
    	queryParam += "&displaySubDeptUser="+displaySubDeptUser ;
    }
	
	var positionUrl=getPath() + '/avicit/platform6/modules/system/commonpopup/ComprehensiveSelector.jsp'+queryParam;
	var comprehensiveDialog = new CommonSelectorDialog(dialogDivId,dialogWidth,dialogHeight,positionUrl,dialogTitle,null,true,true);
		var buttons = [{
			text:'确定',
			id : 'GridComprehensiveSelectSubimt'+new Date().getTime(),
			//iconCls : 'icon-ok',
			handler:function(){
				var frmId = jQuery('#'+dialogDivId+' iframe').attr('id');
				var frm = document.getElementById(frmId).contentWindow;
				var resultData = frm.getSelectedResultDataJson();
				//业务处理
				var userId = "" ;
				var userName = "" ;
				var deptId = "" ;
				var deptName = "" ;
				var roleId = "" ;
				var roleName = "" ;
				var groupId = "" ;
				var groupName = "" ;
				var positionId = "" ;
				var positionName = "" ;
				var userCount=0;
				var deptCount=0;
				var roleCount=0;
				var groupCount=0;
				var positionCount=0;
				for(var i=0 ; i<resultData.length;i++){
					var currData = resultData[i];
					var type = currData.type;
					var id =currData.id;
					var name = currData.name;
					if(type=='user'){
						if(userId==""){
							userId = id;
 	 					  }else{
 	 						userId +=splitChar+ id;
 	 					  }
 						if(userName==""){
 							userName = name;
 	 					  }else{
 	 						userName +=splitChar+ name;  
 	 					  }
 						userCount=userCount+1;
					}else if(type=='dept'){
						if(deptId==""){
							deptId = id;
 	 					  }else{
 	 						deptId +=splitChar+ id;
 	 					  }
 						if(deptName==""){
 							deptName = name;
 	 					  }else{
 	 						deptName +=splitChar+ name;  
 	 					  }
 						deptCount=deptCount+1;
					}else if(type=='role'){
						if(roleId==""){
							roleId = id;
 	 					  }else{
 	 						roleId +=splitChar+ id;
 	 					  }
 						if(roleName==""){
 							roleName = name;
 	 					  }else{
 	 						roleName +=splitChar+ name;  
 	 					  }
 						roleCount=roleCount+1;
					}else if(type=='group'){
						if(groupId==""){
							groupId = id;
 	 					  }else{
 	 						groupId +=splitChar+ id;
 	 					  }
 						if(groupName==""){
 							groupName = name;
 	 					  }else{
 	 						groupName +=splitChar+ name;  
 	 					  }
 						groupCount=groupCount+1;
					}else if(type=='position'){
						if(positionId==""){
 							positionId = id;
 	 					  }else{
 	 						positionId +=splitChar+ id;  
 	 					  }
 						if(positionName==""){
 							positionName = name;
 	 					  }else{
 	 						positionName +=splitChar+ name;  
 	 					  }
 						positionCount=positionCount+1;
					}
		  }
				 var tipMsg = "" ;
				  if(selectedDataMappingConfig != null && selectedDataMappingConfig!='undefined'){
					  var msgHeader = "" ;
					  //选择数据提交前控制
					  var userLimit = selectedDataMappingConfig.user.selectCount ;
					  if(userLimit!=null && userLimit!="undefined"){
						  if(userCount>selectedDataMappingConfig.user.selectCount){
							     msgHeader = "用户只能选择" ;
							     if(tipMsg.length==0){
							    	 tipMsg=msgHeader+userLimit+"个";
							     }else{
							    	 tipMsg="<br>"+msgHeader+userLimit+"个";
							     }
							     userId = "" ;
								 userName = "" ;
								 userCount = 0 ;
				 		  }
					  }
		 			  
		 			//选择数据提交前控制
					  var deptLimit = selectedDataMappingConfig.dept.selectCount ;
					  if(deptLimit!=null && deptLimit!="undefined"){
						  if(deptCount>selectedDataMappingConfig.dept.selectCount){
							    msgHeader = "部门只能选择" ;
							    if(tipMsg.length==0){
							    	 tipMsg=msgHeader+deptLimit+"个";
							     }else{
							    	 tipMsg="<br>"+msgHeader+deptLimit+"个";
							     }
							     deptId = "" ;
								 deptName = "" ;
								 deptCount = 0 ;
				 		  }
					  }
		 			  
		 			//选择数据提交前控制
					  var roleLimit = selectedDataMappingConfig.role.selectCount ;
					  if(roleLimit!=null && roleLimit!="undefined"){
						  if(roleCount>selectedDataMappingConfig.role.selectCount){
							    msgHeader = "角色只能选择" ;
							    if(tipMsg.length==0){
							    	 tipMsg=msgHeader+roleLimit+"个";
							     }else{
							    	 tipMsg="<br>"+msgHeader+roleLimit+"个";
							     }
							    roleId = "" ;
							    roleName = "" ;
							    roleCount = 0 ;
				 		  }
					  }
					 
		 			//选择数据提交前控制
		 			 var groupLimit = selectedDataMappingConfig.group.selectCount ;
					  if(groupLimit!=null && groupLimit!="undefined"){
						  if(groupCount>selectedDataMappingConfig.group.selectCount){
							    msgHeader = "群组只能选择" ;
							    if(tipMsg.length==0){
							    	 tipMsg=msgHeader+groupLimit+"个";
							     }else{
							    	 tipMsg="<br>"+msgHeader+groupLimit+"个";
							     }
							    grouptId = "" ;
							    groupName = "" ;
							    groupCount = 0 ;
				 		  }
					  }
		 			//选择数据提交前控制
					  var positionLimit = selectedDataMappingConfig.position.selectCount ;
					  if(positionLimit!=null && positionLimit!="undefined"){
						  if(positionCount>selectedDataMappingConfig.position.selectCount){
							    msgHeader = "岗位只能选择" ;
							    if(tipMsg.length==0){
							    	 tipMsg=msgHeader+positionLimit+"个";
							     }else{
							    	 tipMsg="<br>"+msgHeader+positionLimit+"个";
							     }
							    positionId = "" ;
							    positionName = "" ;
							    positionCount = 0 ;
				 		  }
					  }
		 			 if(tipMsg.length>0){
		 					jQuery.messager.alert("操作提示", tipMsg,"info");
		 					tipMsg = "" ;
		 					return;
		 		      }
		   	      }
			var retValue = {
					"user":{"userId":userId,"userName":userName},
					"dept":{"deptId":deptId,"deptName":deptName},
					"role":{"roleId":roleId,"roleName":roleName},
					"group":{"groupId":groupId,"groupName":groupName},
					"position":{"positionId":positionId,"positionName":positionName}
				} ;
		var funRetValue = null ;
		if(callBackFun!=null && callBackFun!='undefined'){
			//zl
			//funRetValue = eval(callBackFun + "(rowIndex,retValue,dialogDivId)");
			if(typeof(callBackFun) === 'function'){
				funRetValue = callBackFun(rowIndex,retValue,dialogDivId);
		    }else if(typeof(callBackFun) ==='string'){
		    	funRetValue = eval(callBackFun + "(rowIndex,retValue,dialogDivId)");
		    }else{
			   throw new Error("非法的回调函数！");
		    }
		}else{
			
		}
		if(funRetValue==null || typeof(funRetValue)=="undefined" || funRetValue==true){
	    	comprehensiveDialog.close();
	    	if(afterCloseCallFun != null && afterCloseCallFun != 'undefined'){
				//zl 
	    		//eval(afterCloseCallFun+ "(retValue,dialogDivId)");
				 if(typeof(afterCloseCallFun) === 'function'){
					 afterCloseCallFun(retValue,dialogDivId);
				 }else if(typeof(afterCloseCallFun) ==='string'){
			     	eval(afterCloseCallFun + "(retValue,dialogDivId)");
			     }else{
				   throw new Error("非法的回调函数！");
			     }
			}
		}
	 }
  }];
		comprehensiveDialog.createButtonsInDialog(buttons);
		comprehensiveDialog.show();
};
/**
 * dialog对话框onclose中调用
 * @param dialogShowFieldEleId
 */
function changeSelectorIconStatus(dialogShowFieldEleId){
	var dialogShowFieldEle = jQuery("#"+dialogShowFieldEleId);
	var selectIconObj = dialogShowFieldEle.next();
	selectIconObj.css("display","none");
};
/**
 * 将综合选择父页面的数据回带到dialog的grid
 * @param type
 * @param selectedData
 * @param splitChar
 */
function addParentSelectedDataToGrid(selectedDataMappingConfig){
	var currSplitChar =  selectedDataMappingConfig.splitChar;
	var selectedUser = selectedDataMappingConfig.user;
	var selectedDept = selectedDataMappingConfig.dept;
	var selectedRole = selectedDataMappingConfig.role;
	var selectedGroup = selectedDataMappingConfig.group;
	var selectedPosition = selectedDataMappingConfig.position;
	if(currSplitChar == null && currSplitChar == 'undefined'){
		currSplitChar = ",";
	}
	
	 var allDataArr = new Array();
	 if(selectedUser !=null && selectedUser!='undefined'){
		 var userIdArr = jQuery("#"+selectedUser.userId).val().split(currSplitChar); 
		 var userNameArr = jQuery("#"+selectedUser.userName).val().split(currSplitChar); 
		 if(userIdArr.length>0 && userNameArr.length>0 && userIdArr.length == userNameArr.length){
			 for(var i = 0 ; i < userIdArr.length ; i ++ ){
				 var selectionData = {"id":userIdArr[i],"name":userNameArr[i],type:"user",typeName:"用户"};
				 allDataArr.push(selectionData);
			 }
		 } 
	 }else if(selectedDept !=null && selectedDept!='undefined'){
		 var deptIdArr = jQuery("#"+selectedDept.deptrId).val().split(currSplitChar);  
		 var deptNameArr = jQuery("#"+selectedDept.deptName).val().split(currSplitChar); 
		 if(deptIdArr.length>0 && deptNameArr.length>0 && deptIdArr.length == deptNameArr.length){
			 for(var j = 0 ; j < deptIdArr.length ; j ++ ){
				 var deptData = {"id":deptIdArr[j],"name":deptNameArr[j],type:"dept",typeName:"部门"};
				 allDataArr.push(deptData);
			 }
		 } 
	 }else if(selectedRole !=null && selectedRole!='undefined'){
		 var roleIdArr = jQuery("#"+selectedRole.roleId).val().split(currSplitChar) ;
		 var roleNameArr = jQuery("#"+selectedRole.roleName).val().split(currSplitChar) ;
		 if(roleIdArr.length>0 && roleNameArr.length>0 && roleIdArr.length == roleNameArr.length){
			 for(var k = 0 ; k< roleIdArr.length ; k ++ ){
				 var roleData = {"id":roleIdArr[k],"name":roleNameArr[k],type:"role",typeName:"角色"};
				 allDataArr.push(roleData);
			 }
		 } 
	 }else if(selectedGroup !=null && selectedGroup!='undefined'){
		 var groupIdArr = jQuery("#"+selectedGroup.groupId).val().split(currSplitChar) ;
		 var groupNameArr = jQuery("#"+selectedGroup.groupName).val().split(currSplitChar) ;
		 if(groupIdArr.length>0 && groupNameArr.length>0 && groupIdArr.length == groupNameArr.length){
			 for(var m = 0 ; m < groupIdArr.length ; m ++ ){
				 var groupData = {"id":groupIdArr[m],"name":groupNameArr[m],type:"group",typeName:"群组"};
				 allDataArr.push(groupData);
			 }
		 } 
	 }else if(selectedPosition !=null && selectedPosition!='undefined'){
		 var positionIdArr = jQuery("#"+selectedPosition.positionId).val().split(currSplitChar) ;
		 var positionNameArr = jQuery("#"+selectedPosition.positionName).val().split(currSplitChar) ;
		 if(positionIdArr.length>0 && positionNameArr.length>0 && positionIdArr.length == positionNameArr.length){
			 for(var i = 0 ; i < positionIdArr.length ; i ++ ){
				 var positionData = {"id":positionIdArr[i],"name":positionNameArr[i],type:"position",typeName:"岗位"};
				 allDataArr.push(positionData);
			 }
		 } 
	 }
	var  historySelectedData ={
				total : allDataArr.length,
				rows : allDataArr
	 };
	$('#selectTargetDataGrid').datagrid('loadData',historySelectedData);
};

function removeNodeNameParent(nodeName){
	var currNodeName="";
	if(nodeName!=null && nodeName!='undefined'){
		var index1 = nodeName.indexOf(">");
		var endIndex=nodeName.indexOf("</a>");
		if(index1!=-1 && endIndex!=-1){
			currNodeName=nodeName.substring(index1+1,endIndex);
		}else{
			currNodeName = nodeName ;
		}
	}else{
		currNodeName = nodeName ;
	}
	return currNodeName ;
};
/**
 * 通用选择确认提交前检验
 * @param singleSelect    页面传递的控制提交个数的参数
 * @param selectDataList  已选择的数据集
 */
function submitLimitCheck(singleSelect,selectDataCount,selectType){
	var defaultTitle = "数据" ;
	if(selectType=='user'){
		defaultTitle = "用户" ;
	}else if(selectType=='dept'){
		defaultTitle = "部门" ;
	}else if(selectType=='role'){
		defaultTitle = "角色" ;
	}else if(selectType=='group'){
		defaultTitle = "群组" ;
	}else if(selectType=='position'){
		defaultTitle = "岗位" ;
	}
	
	if(singleSelect!=true && singleSelect!=false && !isNaN(singleSelect)){
		 if(singleSelect!='-1' && selectDataCount>singleSelect){
			jQuery.messager.alert("操作提示", "只能选择"+singleSelect+"个"+defaultTitle+"！","info");
			return false;
		 }else{
			 return true; 
		 }
	}else{
		if(singleSelect && selectDataCount>1){
			jQuery.messager.alert("操作提示", "只能选择一个"+defaultTitle+"！","info");
			return false;
        }else{
        	return true; 
        }
	}
	
};
/**
 * 动态添加Editor并返回Editor对象
 * @param dataGridId  
 * @param fieldName  需要添加editor的字段名称
 * @param rowIndex   当前行索引
 * @returns
 */
function addEditor(dataGridId,fieldName,rowIndex,editorType){
	var currDataGrid = $("#"+dataGridId) ;
	currDataGrid.datagrid('addEditor', {
        field : fieldName,
        editor : {
            type :editorType ,
            options : {
            }
        }
    });
	var edUserIdED = currDataGrid.datagrid('getEditor', {index:rowIndex,field:fieldName});
	return edUserIdED ;
};

/**
 * 关闭通用选择对话框
 * @param dialogId 通用选择对话框ID
 */
function closeCommonSelectorDialog(dialogId){
	jQuery('#' + dialogId).dialog('close');
};

function json2str(o) {
   var arr = [];
   var fmt = function(s) {
       if (typeof s == 'object' && s != null) return json2str(s);
      return /^(string|number)$/.test(typeof s) ? "\"" + s + "\"" : s;
     }
    for (var i in o) arr.push("\"" + i + "\":" + fmt(o[i]));
    return '{' + arr.join(',') + '}';
};