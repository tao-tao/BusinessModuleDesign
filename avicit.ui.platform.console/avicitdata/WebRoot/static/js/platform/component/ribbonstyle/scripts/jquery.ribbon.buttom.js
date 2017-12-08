(function($){
	var buttomHeight = 0;
	var isClosed = true;
	/**
	 * 用class名称加载
	 */
	$(function(){
		
		if($('.ribbon-buttom').length > 0){
			$('.ribbon-buttom').ribbonButtom({});
		}
	});
	
	
	
	/**
	 * 构造方法
	 */
	$.fn.ribbonButtom = function(settings){
		settings = settings || {};
		settings = $.extend({}, $.fn.ribbonButtom.defaults, $.fn.ribbonButtom.parseOptions(this), settings);
		//设置参数
		
		if(settings.isClosed == true){
			isClosed = true;
		}else{
			isClosed = false;
		}
		buttomHeight = $(this).height();
		
		init(this);
	};
	
	/**
	 * 初始化
	 */
	function init(target){
		$(target).addClass('ribbon-buttom-openClose');
		//var splitbar = $('<div class="ribbon-buttom-splitbar"></div>').prependTo(target);
		var targetHeight = $(target).height();
		var splitbuttonHeight = 0;
		var splitbutton;
		if(isClosed == true){
			splitbutton = $('<div class="ribbon-buttom-splitbutton"></div>').prependTo('body');
			splitbuttonHeight = splitbutton.height();
			$(target).css('height',splitbuttonHeight - targetHeight + 'px');
		}else{
			splitbutton = $('<div class="ribbon-buttom-splitbutton"></div>').prependTo('body');
			splitbuttonHeight = splitbutton.height();
		}
		//按钮增加事件
		splitbutton.bind('click', function(){
			
			if(isClosed == false){
				$(target).animate({ height: 'hide'});
				splitbutton.animate({ height: (splitbuttonHeight - targetHeight)});
				splitbutton.css("background-image","url(avicit/platform6/modules/system/ribbonstyle/images/mpm/up_personal1.png)");
				isClosed = true;
			}else{
				$(target).animate({ height: 'show'});
				splitbutton.animate({ height: splitbuttonHeight});
				splitbutton.css("background-image","url(avicit/platform6/modules/system/ribbonstyle/images/mpm/down_personal1.png)");
				//splitbutton.removeClass('invert');
				isClosed = false;
			}
	    });
		
		
		$(target).bind('click',function(){
			$(target).animate({ height: 'hide'});
			splitbutton.animate({ height: (splitbuttonHeight - targetHeight)});
			splitbutton.css("background-image","url(avicit/platform6/modules/system/ribbonstyle/images/mpm/up_personal1.png)");
			isClosed = true;
		});
		
		
		setTimeout(function(){
			$(target).animate({ height: 'hide'});
			splitbutton.animate({ height: (splitbuttonHeight - targetHeight)});
			splitbutton.css("background-image","url(avicit/platform6/modules/system/ribbonstyle/images/mpm/up_personal1.png)");
			isClosed = true;
		},5000);
	}

	/**
	 * 默认参数
	 */
	$.fn.ribbonButtom.defaults = {
		isClosed: true
	};
	
	/**
	 * 获取参数
	 */
	$.fn.ribbonButtom.parseOptions = function(target){
		var t = $(target);
		return $.extend({}, {
			isClosed: (t.attr('isClosed') ? ((t.attr('isClosed') == 'false') ? false: true) : undefined)
		});
		
	};
})(jQuery);
