//dataTree 搜索 

/**
 * 搜索
 * searchContext : 搜索内容
 * searchContextCondition : 搜索条件
 * clearContextCondition : 清空条件
 * searchDataSetObject : dataSet
 */
var searchCount = 0;
var searchFirstRecord;
function searcher(searchContext,searchContextCondition,clearContextCondition,searchDataSetObject,searchDataTree){
	if(searchContext.length == 0){
		dorado.widget.NotifyTipManager.notify("请输入搜索内容!");
		return;	
	}
	if(typeof(clearContextCondition) == 'undefined'){
		dorado.MessageBox.alert("清空参数不允许为空!")
		return;
	}
	if(typeof(searchContextCondition) == 'undefined'){
		dorado.MessageBox.alert("搜索参数不允许为空!")
		return;
	}
	if(typeof(searchDataSetObject) == 'undefined'){
		dorado.MessageBox.alert("搜索对象(DataSet)参数不允许为空!")
		return;
	}
	clearContextCondition.each(function(obj){//清空
		for (var key in obj) {
			var result = searchDataSetObject.getData(obj[key], {
				firstResultOnly: false
			});
			var resultType;
			if (result == null) {
				resultType = "<null>";
			}else if (result instanceof dorado.Entity) {
				resultType = "dorado.Entity";
			}else if (result instanceof dorado.EntityList) {
				resultType = "dorado.EntityList";
			}else if (result instanceof Array) {
				resultType = "Array";
			}else {
				resultType = typeof result;
			}
			resultToNotSelect(result);
		}
	});
	searchContextCondition.each(function(obj){//选中
		for (var key in obj) {
			var result = searchDataSetObject.getData(obj[key], {
				firstResultOnly: false
			});
			var resultType;
			if (result == null) {
				resultType = "<null>";
			}else if (result instanceof dorado.Entity) {
				resultType = "dorado.Entity";
			}else if (result instanceof dorado.EntityList) {
				resultType = "dorado.EntityList";
			}else if (result instanceof Array) {
				resultType = "Array";
			}else {
				resultType = typeof result;
			}
			resultToSelect(result,searchDataTree);
		}
	});
	jQuery("#d_inputSearch_count").remove();
	jQuery("#d_inputSearch").append("<div id='d_inputSearch_count' style='height:100%;position:absolute;top:3px;right:15px;cursor:default;'>共" + searchCount + "个&nbsp;&nbsp;</div>");
}
//重置上次选中的结果，不选中
function resultToNotSelect(result) {
	 if (result instanceof dorado.Entity) {
		 searchCount = 0;
		 searchFirstRecord = '';
		result.set("searchSelectedFlag", false);
	}else if (result instanceof Array) {
		for (var i = 0; i < result.length; i++) {
			resultToNotSelect(result[i]);
		}
	}
}
//设置选中标识位
function resultToSelect(result,searchDataTree) {
	 if (result instanceof dorado.Entity) {
		 searchCount = searchCount + 1;
		 if((typeof(searchFirstRecord) == 'undefined' || searchFirstRecord == '') && typeof(searchDataTree) != 'undefined'){
			 searchFirstRecord = result;
			 searchDataTree.set("currentEntity",searchFirstRecord);
		 }
		 result.set("searchSelectedFlag", true);
		 repeatTreeNodeExpand(result);// tree expand
	}else if (result instanceof Array) {
		for (var i = 0; i < result.length; i++) {
			resultToSelect(result[i],searchDataTree);
		}
	}
}
//递归调用展开parent tree node
function repeatTreeNodeExpand(obj){
	if(obj instanceof dorado.Entity){
		if(typeof(obj._node) == 'undefined' ){
			var node = repeatTreeNodeExpand(obj.parent);
		} else {
			var node = obj._node;
			if (!node.get("expanded")) {
				node.expand();
				return node;
			}
		}
	} else if(obj instanceof dorado.EntityList){
		var node = repeatTreeNodeExpand(obj.parent);
		if(typeof(node)!= "undefined"){
			treeNodeExpand(node.get("nodes"));
		}
		
	}
}
//展开parent tree node
function treeNodeExpand(nodes){
	nodes.each(function(node){
		if(!node.get("expanded")){
			node.expand();
			return node;
		}
	});
}
