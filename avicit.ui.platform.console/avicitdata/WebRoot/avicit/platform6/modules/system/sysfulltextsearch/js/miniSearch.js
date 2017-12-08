/**
 * 本文件中均为迷你搜索页使用的js函数。
 */

// 将搜索结果展现在tip上，如果不再平台页面的环境中，则重新打开一个页面作为搜索结果页
function showResultOnTipForMini(title, path) {
		try{
			parent.parent.addTab("搜索: "+title,  path,  '搜索列表',  '0px 0px');         // 调用父亲的父亲的js方法
		} catch(e) {
		    // 移植后可能会有异常, 不能用addTab方法;在新窗口上打开搜索列表页。
			window.open(path, "_blank");
		}
}	

// 验证迷你搜索框是否为空，不为空时传请求给后台。
function miniSearchAction(fullTextSearchPath) {
	var keywords = document.sysMiniSearchForm.keywords.value.Trim();
	if(keywords.length==0) {
		alert("搜索输入框不能为空!");
		return;
    }
	
	showResultOnTipForMini(keywords,  fullTextSearchPath + keywords  );
}