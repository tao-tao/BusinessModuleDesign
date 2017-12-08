/**
 * 利用jquery.parseJson解析str,返回一个json对象
 * @param {Object} str
 */
$parseJson = function (str){
	//回填处理
	var json ;
	try{
		json = jQuery.parseJSON(str);
	}catch(e){
		var regExp = /(\u0027+)/g;
		json = replaceReg(str,"\"",regExp);
		try{
			json = jQuery.parseJSON(targetProperty);
		}catch(e1){
			try{
		       var r, regExp;
			   var ss = str;
			   regExp = /(\u007B+)/g; // 替换'{'
			   r = replaceReg(ss, "{\"",regExp);
			   regExp = /(\u007D+)/g; // 替换'}'
			   r = replaceReg(r, "\"}",regExp); 
			   regExp = /(\u003A+)/g; // 替换':'
			   r = replaceReg(r, "\":\"",regExp); 
			   json = jQuery.parseJSON(r);
			}catch(e2){
				dorado.MessageBox.alert(e2);
				return null;
			}
		}
	}
	return json;
}
/**
 * 
 * @param {Object} str 交换字符串
 * @param {Object} regEp 表达式
 */
function replaceReg(str,char,regExp){
	  return str.replace(regExp, char);   //交换
  }
  
 /**
 * 利用dataPath获取历史的选取记录实体
 * @param {Object} dataSetObject 左边的dataSet
 * @param {Object} selectionRecordDataPath 数据路径
 * @param {Object} selectionRecordDataSet 已选的dataSet
 */
$setHistorySelectionRecordEntity = function (dataSetObject,selectionRecordDataPath,selectionRecordDataSet){
	var result = dataSetObject.getData(selectionRecordDataPath, {firstResultOnly: false});
	var resultType;
	if (result == null) {
		resultType = "<null>";
	}else if (result[0] instanceof dorado.Entity) {
		selectionRecordDataSet.insert(result[0].toJSON());
	}
}
/**
 * 设置dialog样式
 */
$setFormUISelectionDialog = function(dialog,caption,height,width,icon){
	if(caption != null && caption != ''){
		dialog.set("caption",caption);
	}
	if(height != null && height != ''){
		dialog.set("height",height);
	}
	if(width != null && width != ''){
		dialog.set("width",width);
	}
	if(icon != null && icon){
		dialog.set("icon",icon);
	}
}
/***
 * 设置回填
 */
$setSelectionRecord = function(dialog,targetProperty,targetDataSet,selectedDatas,isMultiple){
	var eventFlag = $callOnSelectedValueClick(dialog,selectedDatas);
	if(eventFlag){
		return;
	}
	if(targetProperty != null && typeof(targetDataSet) != 'undefined'){
		targetProperty.each(function(obj){
			for (var key in obj) {
				//key:obj[key]
				var selectedContext = '';
				// selected value
				selectedDatas.each(function(selectedEntity){
					selectedContext += selectedEntity.get(key) + ',';
				});
				if(selectedContext.length > 1){
					selectedContext = selectedContext.substr(selectedContext,selectedContext.length -1);
				}
				//set target dataset element value
				try{
					targetDataSet.getData().current.set(obj[key],selectedContext);
				}catch(e){
					targetDataSet.getData().set(obj[key],selectedContext);
				}
			}
		});
	} else {
		var editor = dialog.get("userData"); // 取得对应的编辑框 
		var selectedContext = '';
		if( selectedDatas instanceof Array|| selectedDatas instanceof dorado.EntityList){
			selectedDatas.each(function(selectedEntity){
				selectedContext += selectedEntity.get("id") + ',';
			});
			if(selectedContext.length > 1){
				selectedContext = selectedContext.substr(selectedContext,selectedContext.length -1);
			}
		} else if(selectedDatas instanceof dorado.Entity) {
			selectedContext += selectedDatas.get("id");
		}
		editor.set("text", selectedContext);
	}
}
/**
 * 调用虚拟事件
 */
$callOnSelectedValueClick = function(dialog,selectedDatas){
	//调用虚拟事件
	var contanier = dialog.findParent(dorado.widget.Container);
	return contanier.fireEvent("onSelectedValueClick", dialog, selectedDatas);
}
/**
 * 清空已选到的记录
 */
$clearHistorySelectedRecordEntity = function(selectedDataSetObject){
	selectedDataSetObject.clear();
}
/**
 * 刷新dataSet
 * view.get("#sysOrgDataSet").flush();
 */
$refreshDataSet = function(uiComponentDataSet){
	uiComponentDataSet.flush();
}

