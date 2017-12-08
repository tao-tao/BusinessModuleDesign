// datesplite那个处理Dom的也可以通过这种方式处理，你可以自己定义全新的组件。
dorado.beforeInit(function() {
	newViewObj = $extend(dorado.widget.View, {
		onReady : function() {
			$invokeSuper.call(this);
			
			//如何计算时间你们自己处理吧。
			var current = new Date().getTime();
			var $document = jQuery(document);
			$document.bind("keydown", "f8", function() {
				dorado.Debugger.log("当前页面加载时间 ：" + (current-_startTime)/1000+"秒");
				dorado.Debugger.show();
			});
		}
	})
	dorado.widget.View = newViewObj;
});
