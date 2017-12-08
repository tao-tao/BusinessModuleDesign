/**
 * 搜索引擎配置所用的js
 */
function showButtonStyle(id) {
	var currButton = document.getElementById(id);
	if (null == currButton || 'undefined' == typeof (currButton))
		return;
	currButton.style.background = "#E6F6FD";
	currButton.style.border = "solid 1px";
}

function showTextStyle(id) {
	var currButton = document.getElementById(id);
	if (null == currButton || 'undefined' == typeof (currButton))
		return;
	currButton.style.background = "#FFFFFF";
	currButton.style.border = "solid 1px";
}

function showSearchWaitTip() {
	var searchWaitTipBox = document.getElementById("searchWaitTipBox");
	if (null == searchWaitTipBox || 'undefined' == typeof (searchWaitTipBox))
		return;
	searchWaitTipBox.style.display = "block";
}

var msg = "操作正在进行中......";
var seq = 0;
function showTipWords() {
	len = msg.length;
	var searchWaitTipBox = document.getElementById("searchWaitTipWords");
	searchWaitTipBox.innerHTML = msg.substring(0, seq++);
	if (seq >= len)
		seq = 0;
	window.setTimeout("showTipWords();", 789);
}

function confirmOperator(mes) {
	if (confirm(mes)) {
		showSearchWaitTip();
		showTipWords();
		return true;
	} else {
		return false;
	}
}