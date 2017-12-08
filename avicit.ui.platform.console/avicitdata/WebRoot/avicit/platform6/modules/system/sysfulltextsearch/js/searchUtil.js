
/**
 * 本文件中均为搜索使用的js工具函数，包括通用的基础或辅助性功能函数。
 */

// 为兼容IE6(不支持trim()), 特意写的函数, 第一个字母大写以作区分
try{
	String.prototype.Trim = function() { 
		return this.replace(/(^\s*)|(\s*$)/g, "");
	};
}catch(e){
	// do nothing
}

/* 专为IE6等写的函数 start， 根据class获取页面对象  start*/
var getElementsByClass = function(searchClass, node, tag) {
	var classElements = new Array();
	if (node == null)
		node = document;
	if (tag == null)
		tag = '*';
	var els = node.getElementsByTagName(tag);
	var elsLen = els.length;
	var pattern = new RegExp("(^|\\s)" + searchClass + "(\\s|$)");
	for (i = 0, j = 0; i < elsLen; i++) {
		if (pattern.test(els[i].className)) {
			classElements[j] = els[i];
			j++;
		}
	}
	return classElements;
};

function getClass(tagname, className) { //tagname指元素，className指class的值
	if (document.getElementsByClassName) { //判断浏览器是否支持getElementsByClassName，如果支持就直接的用
		return document.getElementsByClassName(className);
	} else { //当浏览器不支持getElementsByClassName的时候用下面的方法
		return getElementsByClass(className, null, null);
	}
};
/* 专为IE6等写的函数 start， 根据class获取页面对象  end*/

/***tableStyle("表格id","奇数行背景","偶数行背景","鼠标经过背景","点击后背景");***/  
function tableStyle(o, a, b, c, d) {
	var t = document.getElementById(o).getElementsByTagName("tr");
	if (null == t || 'undefined' == typeof (t))
		return;
	for ( var i = 0; i < t.length; i++) {
		t[i].style.backgroundColor = (t[i].sectionRowIndex % 2 == 0) ? a : b;
		t[i].onmouseout = function() {
			if (this.x != "1")
				this.style.backgroundColor = (this.sectionRowIndex % 2 == 0) ? a
						: b;
		}
		t[i].onmouseover = function() {
			if (this.x != "1")
				this.style.backgroundColor = c;
		}
		t[i].onclick = function() {
			if (this.x != "1")
				this.style.backgroundColor = d;
		}
	}
}

// 隐藏页面元素样式，使其和背景融为一体
function hideStyle(id) {
	var currButton = document.getElementById(id);
	if (null == currButton || 'undefined' == typeof (currButton))
		return;
	currButton.style.background = "transparent";
	currButton.style.border = "0px";
}

// 添加cookie, 全局生效，有效时间足够长 
function addCookie(key, value) {
	var ctuskytime = new Date();
	ctuskytime.setTime(ctuskytime.getTime()+24*60*60*1000);
    document.cookie = key + "=" + escape(value) + "; path=/" + ";expires=" + ctuskytime.toGMTString();
}

// 获取指定名称的cookie的值
function getCookie(objName) {  
         var arrStr = document.cookie.split("; ");
	     for(var i = 0;i < arrStr.length;i ++) {
	            var temp = arrStr[i].split("=");
	            if(temp[0] == objName) return unescape(temp[1]);
         }
 }

//隐藏页面元素,隐藏后不占用页面空间
function hideElement(id) {
	var elem = document.getElementById(id);
	if(null == elem || 'undefined' == typeof(elem) ) return;
	elem.style.display = "none";
}

// 显示页面元素
function showElement(id) {
	var elem = document.getElementById(id);
	if(null == elem || 'undefined' == typeof(elem) ) return;
	elem.style.display = "block";
}