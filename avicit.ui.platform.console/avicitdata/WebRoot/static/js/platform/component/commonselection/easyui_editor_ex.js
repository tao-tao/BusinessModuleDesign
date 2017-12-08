(function($) { 
	$.extend($.fn.datagrid.defaults.editors, {   
	    CommonSelector: {   
	        init: function(container, options){   
	        var rowIndex = container[0].parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.getAttribute("datagrid-row-index");
	        var selectType = options.selectType ; 
	        var dataGridId = options.dataGridId ; 
	        var dialogShowField = options.dialogShowField ; 
	        var colWidth = options.width ; 
	        var callBackFun = options.callBackFun;
	        var extParameter = options.extParameter;
	        var selectLimit = options.selectCount ;
	        var dialogWidth = options.dialogWidth;
	        var dialogHeight = options.dialogHeight;
	        var splitChar = options.splitChar;
	        var afterCloseFun = options.afterCloseFun;
	        var multipleOrg = options.multipleOrg;
	        var displaySubDeptUser = options.displaySubDeptUser;
	        var required = options.required;
	        var contextPath = getPath();
	        var iconPath =contextPath + "/static/css/platform/themes/default/commonselection/icons_ext/selectorCommon_16x20.gif" ;
	        var targetId = "" ;
	        var targetName = "" ;
	        	if(selectType=='user'){
	        		//iconPath = contextPath +"/avicit/platform6/component/css/commonselection/icons_ext/addUserDept.gif";
	        		targetId = options.userId ;
	                targetName = options.userName ;
	        	}else if(selectType=='dept'){
	        		//iconPath =contextPath + "/avicit/platform6/component/css/commonselection/icons_ext/dept.gif";
	        		targetId = options.deptId ;
	                targetName = options.deptName ;
	        	}else if (selectType=='role'){
	        		//iconPath = contextPath +"/avicit/platform6/component/css/commonselection/icons_ext/role.gif";
	        		targetId = options.roleId ;
	                targetName = options.roleName ;
	        	}else if (selectType=='group'){
	        		//iconPath = contextPath +"/avicit/platform6/component/css/commonselection/icons_ext/group.png";
	        		targetId = options.groupId ;
	                targetName = options.groupName ;
	        	}else if (selectType=='position'){
	        		//iconPath =contextPath + "/avicit/platform6/component/css/commonselection/icons_ext/postion.gif";
	        		targetId = options.positionId ;
	                targetName = options.positionName ;
	        	}
	            var pxIndex = colWidth+"".indexOf("px");
	            var currWidth = "" ;
	            if(pxIndex!=-1){
	            	currWidth = colWidth+"".substring(0,pxIndex);          	
	            }
	        	if(jQuery.isNumeric(currWidth)){
	        		
	        	}else{
	        		jQuery.messager.alert("提示信息","必须指定editor的options的width值,width值为field所在th的width!","warning");
	        	}
	        	//if(callBackFun != null && typeof(callBackFun)!='undefined'){
	        	//	callBackFun ="'"+callBackFun+"'";
	        	//}
	        	//if(afterCloseFun != null && typeof(afterCloseFun)!='undefined'){
	        	//	afterCloseFun ="'"+afterCloseFun+"'";
	        	//}
	        	extParameter = extParameter || '';
	        //	var funcParam = "'"+selectType+"','"+dataGridId+"',"+rowIndex+",'"+dialogShowField+"','"+targetId+"','"+targetName+"',"+callBackFun+",'"+extParameter+"',"+selectLimit+","+dialogWidth+","+dialogHeight+","+splitChar+","+afterCloseFun+",'"+multipleOrg+"','"+displaySubDeptUser+"'" ;
	        //	var img ='<img src="'+iconPath+'" class="datagrid-row-editor-icon" onClick="openCommonSelector('+funcParam+');">';//openCommonSelector(this,'+funcParam+');
	        //	var divObj = $('<div  class="datagrid-row-editor-container"><input type="text" onClick="changeDsiaplay(this,\'inline\');" class="datagrid-editable-input" readonly="readonly" name ="commonSelector_datagrid-row-editor">'+img+'</div>');   
	        //	divObj.appendTo(container);
	        //	divObj.children("input").validatebox(options);
	         //   return divObj;
	        	//var funcParam = "'"+selectType+"','"+dataGridId+"',"+rowIndex+",'"+dialogShowField+"','"+targetId+"','"+targetName+"',"+callBackFun+",'"+extParameter+"',"+selectLimit+","+dialogWidth+","+dialogHeight+","+splitChar+","+afterCloseFun+",'"+multipleOrg+"','"+displaySubDeptUser+"'" ;
	        	var img = $('<img src="'+iconPath+'" class="datagrid-row-editor-icon"/>');//openCommonSelector(this,'+funcParam+');
	        	var divObj = $('<div  class="datagrid-row-editor-container"><input type="text" onClick="changeDsiaplay(this,\'inline\');" class="datagrid-editable-input" readonly="readonly" name ="commonSelector_datagrid-row-editor"></div>');
	        	divObj.append(img);
	        	divObj.appendTo(container);
	        	img.on("click",function(){
	        		openCommonSelector(selectType,dataGridId,Number(rowIndex),dialogShowField,targetId,targetName,callBackFun,extParameter,selectLimit,dialogWidth,dialogHeight,splitChar,afterCloseFun,multipleOrg,displaySubDeptUser);
	        	});
	        //	divObj.children("input").validatebox(options);
	            return divObj;
	        },   
	        getValue: function(target){   
	            return $(target[0].childNodes[0]).val();   
	        },   
	        setValue: function(target, value){   
	            $(target[0].childNodes[0]).val(value);   
	        },   
	        resize: function(target, width){   
	            var input = $(target);   
	            if ($.boxModel == true){   
	                input.width(width - (input.outerWidth() - input.width()));   
	            } else {   
	                input.width(width);   
	                target[0].childNodes[0].style.width=width-6+"px";
	            }   
	        }   
	     }
	});
	$.extend($.fn.validatebox.methods, {  
		remove: function(jq, newposition){  
			return jq.each(function(){  
				$(this).removeClass("validatebox-text validatebox-invalid").unbind('focus.validatebox').unbind('blur.validatebox');
			});  
		},
		reduce: function(jq, newposition){  
			return jq.each(function(){  
			   var opt = $(this).data().validatebox.options;
			   $(this).addClass("validatebox-text").validatebox(opt);
			});  
		}	
	}); 
	$.extend($.fn.validatebox.defaults.rules, {
		customValidate: {
	        validator: function(value,param){
	            var rt= param[0](value);//调用函数
		    param[1]=rt;//设置显示的信息
	            return rt ==null;//如果无返回信息,说明校验通过
	        },
	        message: '{1}' //显示校验错误信息
	    }
	});
	$.extend($.fn.datagrid.methods, {
	    addEditor : function(jq, param) {
	    	debugger;
	        if (param instanceof Array) {
	            $.each(param, function(index, item) {
	                var e = $(jq).datagrid('getColumnOption', item.field);
	                e.editor = item.editor;
	            });
	        } else {
	            var e = $(jq).datagrid('getColumnOption', param.field);
	            e.editor = param.editor;
	        }
	    },
	    removeEditor : function(jq, param) {
	        if (param instanceof Array) {
	            $.each(param, function(index, item) {
	                var e = $(jq).datagrid('getColumnOption', item);
	                e.editor = {};
	            });
	        } else {
	            var e = $(jq).datagrid('getColumnOption', param);
	            e.editor = {};
	        }
	    }
	});
})(jQuery);
/**
 * 控制input右侧img的显隐
 * @param currObj
 * @param status   block/none
 */
function changeDsiaplay(currObj,status){
	var w = currObj.parentNode.style.width;
	var index = w.indexOf("px");
	if(index!=-1){
		w = w.substring(0,index);
	}
	if(currObj.tagName=='INPUT' || currObj.tagName=='input'){
		currObj.style.width=(parseInt(w)-22)+"px";
		currObj.parentNode.lastChild.style.display=status;
	}else{
		currObj.childNodes[1].style.display=status;
	}
};
/**
 * 
 * @param newElement      需要插入的元素对象
 * @param targetElement   参照对象
 */
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
 * 
 * @param selectType   user/dept/position/role/group
 * @param dataGridId   页面datagrid的id
 * @param rowIndex     当前编辑行的rowIndex
 * @param dialogShowField        datagrid的column中触发打开选择框的field
 * @param fieldMappingConfig   选择数据映射对象
 */
function openCommonSelector(selectType,dataGridId,rowIndex,dialogShowField,targetId,targetName,callBackFun,extParameter,selectLimit,dialogWidth,dialogHeight,splitChar,afterCloseCallFun,multipleOrg,displaySubDeptUser){
	var commonSelector = new GridCommonSelector(selectType,dataGridId,rowIndex,dialogShowField,{targetId:targetId,targetName:targetName},callBackFun,extParameter,dialogWidth,dialogHeight,selectLimit,splitChar,afterCloseCallFun,multipleOrg,displaySubDeptUser);//
    var rows = $('#'+dataGridId).datagrid("getRows");
	commonSelector.init(rows[rowIndex]);
};
