/**
 * 本文件中均搜索主题的js函数。
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

/** *执行搜索请求前检查输入框是否为空 start** */
var resultIndex = 1;
function checkKeyWordsInput() {
	if (document.sysFullTextSearchForm.keywords.value.Trim().length == 0) {
		try{      
			  $.messager.alert('提示', '搜索输入框不能为空!', 'info');
		}catch(e) {
			alert("搜索输入框不能为空!");
		}
		document.sysFullTextSearchForm.keywords.focus();
		return false;
	} else {
		addHotInputWhenSearching(document.sysFullTextSearchForm.keywords.value);
		return true;
	}
}

function checkKeyWordsInputInChoice(keywordId) {
	try {
		var keywordElement = document.getElementById(keywordId);
		keywordElement.value = document.sysFullTextSearchForm.keywords.value
				.Trim();
		return checkKeyWordsInput();
	} catch (e) {
		return false;
	}
}
/** *执行搜索请求前检查输入框是否为空 end** */

// 默认光标在搜索框
function focusKeywords() { // 解决IE下加载出错问题, 设个延时
	// document.sysFullTextSearchForm.keywords.focus();
	setTimeout(function() {
		document.getElementById('keywords').focus();
	}, 500);
}

// 打开搜索结果页面
function showResultOnTip(title, path) {
	try {
		parent.addTab(title + (resultIndex++), path, '搜索结果', '0px 0px');
	} catch (e) {
		// 移植后可能会有异常, 不能用addTab方法;在新窗口上打开详情页
		window.open(path, "_blank");
	}
}

focusKeywords(); paginationButton();