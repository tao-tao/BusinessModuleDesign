/**
 * 本文件中为高级搜索主体的js函数，包括分页相关的js函数和结果展现使用的js函数等。
 * 需要引入searchUtil.js, kmAdvancedSearchFace.js
 * @author wangjia
 */

// 分页按钮的样式变化
function paginationButton() {
	var pc = getElementsByClass("pc");
	if(!pc) return;
	for ( var i = 0; i < pc.length; i++) {
		pc[i].onmouseover = function() {
			this.style.backgroundColor = "gray";
			this.style.border="0 solid #EBEBEB";
		};
		pc[i].onmouseout = function() {
			this.style.backgroundColor = "#EBEBEB";
			this.style.border="0 solid #EBEBEB";
		};
		pc[i].onmousedown = function() {
			this.style.backgroundColor = "green";
			this.style.border="0 solid #EBEBEB";
		};
	}
}

// 点击分页按钮后的事件,执行分页操作
function clickPagination(pageIndex, pagesize) {
	try{
		parent.document.getElementById("pageIndex").value = pageIndex;
		parent.document.getElementById("pageSize").value = pagesize;
		parent.doAdvancedSearch();
	} catch(e) {
		alert(e.toString());
		document.sysAdvancedSearchForm.sumbit();
	}
}

var resultIndex = 1;

//打开搜索结果页面
function showResultOnTip(title, path) {
	try {
		parent.parent.addTab(title + (resultIndex++), path, '搜索结果', '0px 0px');
	} catch (e) {
		// 移植后可能会有异常, 不能用addTab方法;在新窗口上打开详情页
		window.open(path, "_blank");
	}
}
