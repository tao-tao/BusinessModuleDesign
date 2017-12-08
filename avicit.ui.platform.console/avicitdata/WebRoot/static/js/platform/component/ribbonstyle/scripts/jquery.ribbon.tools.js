(function($){

	/**
	 * 用class名称加载
	 */
	$(function(){
		if($('.ribbon-tools').length > 0){
			$('.ribbon-tools').ribbonTools({});
		}
	});
	
	var toolsHeight = 0;
	var isClosed = false;
	var settings = null;
	/**
	 * 构造方法
	 */
	$.fn.ribbonTools = function(options, param){
		if (typeof options == 'string') {
			return $.fn.ribbonTools.methods[options](this, param);
		}
		options = options || {};
		settings = $.extend({}, $.fn.ribbonTools.defaults, $.fn.ribbonTools.parseOptions(this), options);
		toolsHeight = $(this).height();
		//init(this, settings);
		
		top.$('.spliter_up').css("display","block");
		top.$('.spliter_down').css("display","block");
	};
	
	/**
	 * 
	 */
	function init(target, options){
		var splitbar = $('<div class="ribbon-tools-close ribbon-tools-openClose"></div>').appendTo(target);
		var splitbutton = $('<div class="ribbon-tools-close-button"  title="返回" ></div>').prependTo(splitbar);
		//按钮增加事件
		splitbutton.bind('click', function(){
			if(isClosed == false){
				$(target).find('ul').animate({ height: "hide" });
				$(target).animate({ height: splitbutton.height() });
				splitbutton.attr('title', '打开');
				settings.onClose.call(target);
				isClosed = true;
			}else{
				$(target).find('ul').animate({ height: "show" });
				$(target).animate({ height: toolsHeight});
				splitbutton.attr('title', '返回');
				settings.onOpen.call(target);
				isClosed = false;
			}
	    });
	}
	
	$.fn.ribbonTools.methods = {
			
			hide: function(jq){
				return jq.each(function(){
					hide(this);
				});
			},
			show: function(jq){
				return jq.each(function(){
					show(this);
				});
			},
			change: function(jq){
				return jq.each(function(){
					change(this);
				});
			}
		};

	/**
	 * 默认参数
	 */
	$.fn.ribbonTools.defaults = {
		theme: 'windows7',
		isClosed: false,
		onOpen: function(){},
		onClose: function(){}
	};
	
	/**
	 * 获取参数
	 */
	$.fn.ribbonTools.parseOptions = function(target){
		var t = $(target);
		return $.extend({},  getFunc(target), {
			isClosed: (t.attr('isClosed') ? ((t.attr('isClosed') == 'false') ? false: true) : undefined)
		});
	};
	
	/**
	 * 获取事件
	 */
	function getFunc(target){
		var t = $(target);
		var options = {};
			
		var s = $.trim(t.attr('func'));
		if (s){
			if (s.substring(0, 1) != '{'){
				s = '{' + s + '}';
			}
			options = (new Function('return ' + s))();
		}
		return options;
	}
	
	function hide(target){
		$(target).animate({ height: "hide" });
		settings.onClose.call(target);
		isClosed = true;
	}
	
	function show(target){
		$(target).animate({ height: "show"});
		settings.onOpen.call(target);
		isClosed = false;
	}
	
	function change(target){
		if(isClosed == false){
			$(target).animate({ height: "hide" });
			settings.onClose.call(target);
			isClosed = true;
		}else{
			$(target).animate({ height: "show"});
			settings.onOpen.call(target);
			isClosed = false;
		}
	}
})(jQuery);
