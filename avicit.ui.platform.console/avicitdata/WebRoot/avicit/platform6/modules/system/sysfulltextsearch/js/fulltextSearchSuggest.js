/**
 * 全文检索热输入提示的js,
 * cookie中和数据中的历史记录均按照时间的倒序排列，即越新的越靠前。
 */

/***********选择热输入提示框中的一条结果start***********/
var maxHistoryCount = 100;   // cookie中最多存的搜索历史记录数
var showHistoryCount = 20;   // 最多展现的搜索历史记录数


// 改变某条结果的背景色, 即选中某条结果
var suggestIndex = -1;
function changeBackGround(index) {

	var keywords = document.getElementById("keywords");
	if (null == keywords || 'undefined' == typeof (keywords))
		return;

	var suggest_ul = document.getElementById("suggest_ul");
	if (null == suggest_ul || 'undefined' == typeof (suggest_ul))
		return;

	var liList = suggest_ul.getElementsByTagName('li');
	if (null == liList || 'undefined' == typeof (liList) || 0 == liList.length)
		return;

	for (var i in liList) { // 把所有的背景变白
		if (parseInt(i) == i) { // 判断是否为整数
			liList[i].style.background = 'white';
		}
	}

	if (-1 != index) {
		liList[index].style.background = '#CDC9C9'; // 把当前选中的背景变灰
		keywords.value = liList[index].innerHTML;
	}
}

// 上下键事件处理函数，计算选中的位置下标，并改变背景色
function upOrDown(keyCode) {

	var suggest_ul = document.getElementById("suggest_ul");
	if (null == suggest_ul || 'undefined' == typeof (suggest_ul))
		return;

	var liList = suggest_ul.getElementsByTagName('li');
	if (null == liList || 'undefined' == typeof (liList) || 0 == liList.length)
		return;

	var showListLength = liList.length;

	if ('38' == keyCode) { // 上
		suggestIndex--;
		if (suggestIndex < -1)
			suggestIndex = showListLength - 1;
		changeBackGround(suggestIndex);
	} else if ('40' == keyCode) { // 下
		suggestIndex++;
		if (suggestIndex >= showListLength)
			suggestIndex = -1;
		changeBackGround(suggestIndex);
	}
}

/**************选择热输入提示框中的一条结果end**************/

/**************热输入提示框的显示和隐藏 start**************/

// 提示框有内容时, 显示热输入提示框, 供调用
function showSearchSuggest() {
	var suggest_ul = document.getElementById("suggest_ul");
	if (null == suggest_ul || 'undefined' == typeof (suggest_ul))
		return;

	var liList = suggest_ul.getElementsByTagName('li');
	if (0 == liList.length)
		return;

	suggest_ul.style.display = "block";
}

// 清除热输入提示框中的内容, 并且隐藏热输入提示框,供调用
function hideSearchSuggest() {
	suggestIndex = -1; // 每次隐藏都把选中位置标记的置为零
	var suggest_ul = document.getElementById("suggest_ul");
	if (null == suggest_ul || 'undefined' == typeof (suggest_ul))
		return;
	suggest_ul.innerHTML = '';
	suggest_ul.style.display = "none";
}

/**************热输入提示框的显示和隐藏 end**************/

/**************热输入提示框中内容的添加和删除 start**************/

// 清空热输入内容, 供调用
function cleanSuggest() {
	var suggest_ul = document.getElementById("suggest_ul");
	if (null == suggest_ul || 'undefined' == typeof (suggest_ul))
		return;
	suggest_ul.innerHTML = '';
}

// 在热输入内容头部添加一条热输入提示，供调用
function addSuggestOnHead(singleHotInput) {
	var suggest_ul = document.getElementById("suggest_ul");
	if (null == suggest_ul || 'undefined' == typeof (suggest_ul))
		return;

	var suggest = suggest_ul.innerHTML;
	suggest = "<li  onmouseout='mouseout(this)' onmouseover='mouseSelect(this)' onclick='mouseClick(this)'>"
			+ singleHotInput + "</li>" + suggest;

	suggest_ul.innerHTML = suggest;
}

// 在热输入内容尾部添加一条热输入提示，供调用
function addSuggestOnTail(singleHotInput) {
	var suggest_ul = document.getElementById("suggest_ul");
	if (null == suggest_ul || 'undefined' == typeof (suggest_ul))
		return;

	var suggest = suggest_ul.innerHTML;
	suggest = suggest
			+ "<li  onmouseout='mouseout(this)' onmouseover='mouseSelect(this)' onclick='mouseClick(this)'>"
			+ singleHotInput + "</li>";

	suggest_ul.innerHTML = suggest;
}
/***********热输入提示框中内容的添加和删除 end***********/

/********浏览器热输入列表及cookie操作 start********/

// 把用户的热输入存入cookie
function addHotInputInCookie(hotInput) {
	
	var ctuskytime = new Date();
	ctuskytime.setTime(ctuskytime.getTime()+24*60*60*1000);
	
	var hotInputInCookies = getCookie('searchHotInput');
	if (null != hotInputInCookies && 'undefined' != typeof (hotInputInCookies)) { // 本来存在该cookie，追加一个在最前面
		hotInputInCookies = hotInput + "*" + hotInputInCookies;
		document.cookie = "searchHotInput=" + escape(hotInputInCookies)
				+ "; path=/" + ";expires=" + ctuskytime.toGMTString();
	} else { // 本来不存在该cookie，加一个cookie
		document.cookie = "searchHotInput=" + escape(hotInput) + "; path=/" + ";expires=" + ctuskytime.toGMTString();
	}
}

// 浏览器热输入列表中添加一条热输入，供调用
function addSuggestInFrontList(singleHotInput) {
	suggestFrontList.unshift(singleHotInput);
}

// 清空浏览器热输入列表，供调用
function clearSuggestFrontList() {
	suggestFrontList.length = 0;
}

// 点击搜索时，如果该输入没有在浏览器热输入列表中中，将该搜索输入作为该用户的热输入存在浏览器热输入列表中，同时存入cookie中
function addHotInputWhenSearching(hotInput) {
	for ( var i = 0; i < suggestFrontList.length; i++) {
		if (suggestFrontList[i] == hotInput)
			return;
	}
	
	if(suggestFrontList.length >= maxHistoryCount) {    // 超过需要存储的最大历史记录条数，把最早的那条删掉
		
		var ctuskytime = new Date();
		ctuskytime.setTime(ctuskytime.getTime()+24*60*60*1000);
		
		suggestFrontList.pop();
		var oldHotInputInCookies = getCookie('searchHotInput');
		var position = oldHotInputInCookies.lastIndexOf("*");
		var newHotInputInCookies = oldHotInputInCookies.substring(0, position);
		document.cookie = "searchHotInput=" + escape(newHotInputInCookies) + "; path=/" + ";expires=" + ctuskytime.toGMTString();
	} 
	
	addSuggestInFrontList(hotInput);
	addHotInputInCookie(hotInput);
}
/****************浏览器热输入列表 end****************/

// 将浏览器热输入列表中前缀符合要求的加入到热输入提示框的内容中
function addFrontHotInputForShow(hotInput, suggestBackList) {
	if (null == hotInput || 0 == hotInput.toString().Trim().length
			|| 'undefined' == typeof (hotInput))
		return;

	for ( var i = 0; i < suggestFrontList.length; i++) {
		var cadidateSuggest = suggestFrontList[i].toString().Trim();
		if (0 == cadidateSuggest.indexOf(hotInput)) {
			var reduplicateFlag = false; // 默认前后台返回的结果没有重复
			for ( var j in suggestBackList) {
				if (cadidateSuggest == suggestBackList[j])
					reduplicateFlag = true; // 出现重复了 
			}
			if (!reduplicateFlag)
				addSuggestOnHead(cadidateSuggest); // 如果和后台返回的结果没有重复, 则添加到要显示的列表中;
		}
	}
}

// 设置热输入提示框的内容, 显示提示框(如果内容不为空), 初始化选中的热输入index。
function showSuggest() {

	cleanSuggest(); // 清空上次的内容

	var suggestBackList;
	var hotInputPrefix = document.sysFullTextSearchForm.keywords.value.Trim();

	$.ajax({
		type : "POST", // 使用post方法访问后台
		dataType : "json", // 返回json格式的数据
		url : fulltextSuggestPath + ".json", // 要访问的后台地址
		data : "hotInputPrefix=" + hotInputPrefix,
		complete : function() {
		}, // dothing here 

		/* 调试: ajax错误的提示信息
		error: function(XMLHttpRequest, textStatus, errorThrown) {
		    alert(XMLHttpRequest.status);
		    alert(XMLHttpRequest.readyState);
		    alert(textStatus);
		},
		 */

		success : function(result) { // result为返回的数据，在这里由SpringMVC返回包含热输入提示结果集合的List<String>

			if (null != result && "undefined" != typeof (result)
					&& null != result.stringList
					&& "undefined" != typeof (result.stringList)
					&& 0 != result.stringList.length) {

				suggestBackList = result.stringList; // 后台返回的热输入结果

				for ( var i in suggestBackList)
					addSuggestOnTail(suggestBackList[i]); // 把后台返回的结果逐个添加到热输入提示框的内容中,  即所有用户公共的搜索热输入, 排序在后
			}

			addFrontHotInputForShow(hotInputPrefix, suggestBackList); // 添加浏览器端存的热输入, 即某个用户个人的搜索热输入, 排序在前。
			showSearchSuggest(); //  显示热输入提示框

		}
	});
}

// 延时执行ajax和显示热输入提示框
var timer;
function timerShowSuggest() {
	if (timer)
		window.clearTimeout(timer);
	timer = window.setTimeout("showSuggest()", 300);
}

// 主功能: 文字内容变化时, 根据按键激发事件
function prepareToShowSuggest() {

	var keywords = $('#keywords');

	keywords
			.bind('keyup',
					function(event) {
						if ("13" == event.keyCode) { // 回车事件,  执行全文检索, 这里不用作处理
							// do nothing here, code of fulltext search service is in other place. 
						} else if ("38" == event.keyCode
								|| "40" == event.keyCode) { //上下键, 通过点击上下键选中一条结果, 并改变搜索框显示的内容
							upOrDown(event.keyCode);
						} else if ("37" == event.keyCode
								|| "39" == event.keyCode) {
							// 左右键, do nothing.
						} else { // 如果有结果, 将搜索框的内容显示出来    					  
							if (document.sysFullTextSearchForm.keywords.value
									.Trim().length == 0) { // 搜索框文字内容为空, 隐藏热输入提示框
								hideSearchSuggest();
							} else {
								hideSearchSuggest(); // 先隐藏热输入提示框
								timerShowSuggest(); // 将搜索框的内容显示出来   
							}
						}
					});
}

/****************** 鼠标事件start ******************/
// mouseOn事件：选中标签置为-1 鼠标经过时，鼠标上的这条数据处于被选中状态；其他数据处于未选中状态
function mouseSelect(currentLi) {
	suggestIndex = -1; // 把选中的位置标记置为-1, （清除上下键操作后的结结果）
	changeBackGround(-1); // 把所有结果置位非选中的状态。
	currentLi.style.background = '#CDC9C9'; // 把当前选中的背景变灰
}

function mouseout(currentLi) {
	currentLi.style.background = 'white';
}

function mouseClick(currentLi) {
	var keywords = document.getElementById("keywords");
	if (null == keywords || 'undefined' == typeof (keywords))
		return;

	keywords.value = currentLi.innerHTML; // 当前鼠标点击的热输入数据放入搜索框
	hideSearchSuggest(); // 隐藏热输入提示框
	document.sysFullTextSearchForm.submit(); //  提交全文检索请求
}
/****************** 鼠标事件end *******************/

// 展现当前浏览器若干条搜索历史记录，showCount为需要展现的条数
function getSearchHistoryFormCookie(showCount) {
	
	if(!showCount) showCount = showHistoryCount;
	if(!suggestFrontList) return null; 
	
	var historyArray = new Array();
	for(var i = 0; i < suggestFrontList.length; i++) {
		if(i >= (showCount - 1)) break;
		historyArray.push(suggestFrontList[i]);
	}
	
	return historyArray;
}

//清除搜索历史记录
function clearSearchHistory() {
	var ctuskytime = new Date();
	ctuskytime.setTime(ctuskytime.getTime());
	document.cookie = "searchHotInput=" + "" + "; path=/" + ";expires=" + ctuskytime.toGMTString();
	
	if(suggestFrontList) {
		suggestFrontList = [];
	}
}