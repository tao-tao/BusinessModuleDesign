// 显示搜索选项框
function showOrHideChoicePannel() {
	var choicePannel = document.getElementById("choicePannel");

	if (null == choicePannel || 'undefined' == typeof (choicePannel))
		return;
	if (null == choicePannel.style
			|| 'undefined' == typeof (choicePannel.style))
		return;
	if (null == choicePannel || 'undefined' == typeof (choicePannel))
		return;

	if (choicePannel.style.display == "block") {
		choicePannel.style.display = "none";
	} else {
		choicePannel.style.display = "block";
	}
};

// 显示 “被过滤”的 页面元素 
function showFilteredResult() {
	var tagName = getClass("span", "pageFilteredResult");
	for ( var i = 0; i < tagName.length; i++) {
		tagName[i].style.display = "block";
	}
};

// 隐藏“被过滤”的 页面元素 
function hideFilteredResult() {
	var tagName = getClass("span", "pageFilteredResult");
	for ( var i = 0; i < tagName.length; i++) {
		tagName[i].style.display = "none";
	}
};

// 显示文字样式
function showTextStyle(id) {
	var currButton = document.getElementById(id);
	if (null == currButton || 'undefined' == typeof (currButton))
		return;
	currButton.style.background = "#FFFFFF";
	currButton.style.border = "solid 1px";
};

// 隐藏样式，使其和背景融为一体 
function hideStyle(id) {
	var currButton = document.getElementById(id);
	if (null == currButton || 'undefined' == typeof (currButton))
		return;
	currButton.style.background = "transparent";
	currButton.style.border = "0px";
};

// 显示按钮的样式
function showButtonStyle(id) {
	var currButton = document.getElementById(id);
	if (null == currButton || 'undefined' == typeof (currButton))
		return;
	currButton.style.background = "#E6F6FD";
	currButton.style.border = "solid 1px";
};

// 隐藏页面元素
function hideElement(id) {
	var currElem = document.getElementById(id);
	if (null == currElem || 'undefined' == typeof (currElem))
		return;
	currElem.style.display = "none";
};

// 显示页面元素
function showElement(id) {
	var currElem = document.getElementById(id);
	if (null == currElem || 'undefined' == typeof (currElem))
		return;
	currElem.style.display = "block";
};

/******************************功能 tip提示信息隐藏与显示 start************************/

// change the opacity for different browsers
function changeOpac(opacity, id) {
	var obj = document.getElementById(id);
	if (obj) {
		var s = obj.style;
		s.opacity = (opacity / 100);
		s.MozOpacity = (opacity / 100);
		s.KhtmlOpacity = (opacity / 100);
		s.filter = "alpha(opacity=" + opacity + ")";
		s = null;
	}
};

function opacity(id, opacStart, opacEnd, millisec) {
	// speed for each frame
	var speed = Math.round(millisec / 100);
	var timer = 0;
	// determine the direction for the blending, if start and end are the same nothing happens
	if (opacStart > opacEnd) {
		for ( var i = opacStart; i >= opacEnd; i--) {
			setTimeout("changeOpac(" + i + ",'" + id + "')", (timer * speed));
			timer++;
		}
	} else if (opacStart < opacEnd) {
		for ( var i = opacStart; i <= opacEnd; i++) {
			setTimeout("changeOpac(" + i + ",'" + id + "')", (timer * speed));
			timer++;
		}
	}
};

function shiftOpacity(id, millisec) {
	// if an element is invisible, make it visible, else make it ivisible
	if (document.getElementById(id).style.opacity == 0) {
		opacity(id, 0, 100, millisec);
	} else {
		opacity(id, 100, 0, millisec);
	}
};

function blendimage(divid, imageid, imagefile, millisec) {
	var speed = Math.round(millisec / 100);
	var timer = 0;

	// set the current image as background
	document.getElementById(divid).style.backgroundImage = "url("
			+ document.getElementById(imageid).src + ")";

	// make image transparent
	changeOpac(0, imageid);

	// make new image
	document.getElementById(imageid).src = imagefile;

	// fade in image
	for ( var i = 0; i <= 100; i++) {
		setTimeout("changeOpac(" + i + ",'" + imageid + "')", (timer * speed));
		timer++;
	}
};

function currentOpac(id, opacEnd, millisec) {
	// standard opacity is 100
	var currentOpac = 100;

	// if the element has an opacity set, get it
	if (document.getElementById(id).style.opacity < 100) {
		currentOpac = document.getElementById(id).style.opacity * 100;
	}

	// call for the function that changes the opacity
	opacity(id, currentOpac, opacEnd, millisec);
};

function showTip(i, event) {
	showid = "tip" + i;
	var target = document.getElementById(showid);
	target.style.position = "absolute";
	if (navigator.appName != "Netscape") {
		event = window.event;
		event.srcElement.style.fontWeight = "700";
	} else {
		event.target.style.fontWeight = "700";
	}
	target.style.top = event.clientY - 100 + "px";
	target.style.left = event.clientX - 200 + "px";

	// 复制一个背景
	var bg = target.cloneNode(true);
	if (bg) {
		bg.id = "bg1";
		if (bg.style.backgroundColor.length == 0) {
			bg.style.backgroundColor = "#FFFFE1";
		}
		bg.style.filter = "alpha(opacity=0)";
		bg.style.opacity = 0;
		target.parentNode.appendChild(bg);

		opacity("bg1", 0, 90, 300);
		bg.style.display = "block";
	}

	target.style.display = "block";
};

function hiddenTip(i, event) {

	if (navigator.appName != "Netscape") {
		event = window.event;
		event.srcElement.style.fontWeight = "400";
	} else {
		event.target.style.fontWeight = "400";
	}

	hiddenid = "tip" + i;
	document.getElementById(hiddenid).style.display = "none";
	var bg = document.getElementById("bg1");
	if (bg) {
		bg.parentNode.removeChild(bg);
	}
};

/******************************功能 tip提示信息隐藏与显示 end************************/
