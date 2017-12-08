// 全局变量，用来控制对DIV UL 追加IFRAME，可以让其在XGANT进行正常显示
var useObjectShim = true;
(function($){
	setTimeout(function(){
		$('#tabs .tabs-header .tabs-wrap').append("<div class='spliter_up'></div>");
		$('#tabs .tabs-header .tabs-wrap').append("<div class='spliter_down'></div>");
		$.ajax({
			url : 'platform/sysmenu/getLoginUserName',
			data : {},
			type : 'post',
			dataType : 'json',
			success : function(result) {
				$('#tabs .tabs-header .tabs-wrap').append("<div class='personal_set'><img alt=\"\" src=\"avicit/platform6/modules/system/ribbonstyle/images/mpm/20.png\"></img>&nbsp;"+result.userName+"</div>");
				$(".personal_set").bind('mouseenter',function(){
					$("#em").show();
				});
				
				
				$(".personal_set").bind('mouseout',function(){
					setTimeout(function(){$("#em").hide();},800);
				});
				
				
				$("#em").bind('mouseenter',function(){
					$(".personal_set").unbind('mouseout');
				}).bind('mouseleave',function(){
					$("#em").hide();
				});
			}
		});
		
		
		
		//$('#tabs .tabs-header .tabs-wrap').append("<div class='out'><a onclick=\"javascript:logout();return false;\" href=\"javascript:void(0);\">退出</a></div>");
		//$(".spliter").css("display","none");
		
		$(".spliter_up").click(function(){
			var selected = $('#tabs').tabs("getSelected");
			var flag =  $(selected).find('iframe')[0].contentWindow;
			flag.$('.ribbon-tools').ribbonTools('hide');
			//$(".spliter_up").css("visibility","collapse");
			$(".spliter_up").css("visibility","hidden");
			$(".spliter_down").css("visibility","visible");
       });
		$(".spliter_down").click(function(){
			var selected = $('#tabs').tabs("getSelected");
			var flag =  $(selected).find('iframe')[0].contentWindow;
			flag.$('.ribbon-tools').ribbonTools('show');
			$(".spliter_up").css("visibility","visible");
			$(".spliter_down").css("visibility","hidden");
       });
		
		
	},500);
	
	/**
	 * 用class名称加载
	 */
	$(function(){
		if($('.ribbon-menu').length > 0){
//			var windowH = document.documentElement.clientHeight;
//			var objH = $('.ribbon-menu').children("div").height();
//			if(objH>windowH){alert(111);
//				$('.ribbon-menu').children("div").height(windowH-100);
//				$('.ribbon-menu').children("div").css("overflow-y","auto");
//				$('.ribbon-menu').children("div").css("width","160");
//			}
			$('.ribbon-menu').ribbonMenu({});
		}
	});
	
	/**
	 * 构造方法
	 */
	$.fn.ribbonMenu = function(settings){
		settings = settings || {};
		settings = $.extend({}, $.fn.ribbonMenu.defaults, $.fn.ribbonMenu.parseOptions(this), settings);
		init(this);
	};
	
	/**
	 * 初始化
	 */
	function init(target){
		var t = $(target);
		t.addClass('ribbon-menu-hidden').css('visibility', 'hidden');
		var menu = $('<div class="ribbon-menu"></div>').appendTo('body');
		var x = t.parent().offset().top; 
		var y = t.parent().offset().left; 
		menu.css('position', 'absolute');
		menu.css('z-index', '9999');
		menu.css('top', x + 'px');
		menu.css('left', y + 'px');
		t.contents().appendTo(menu);
		$.each(menu.find('li'), function(i,val){
			if($(this).find('ul').length > 0){
				$(this).find('> a').addClass('arrow');
			}
		});

		//按钮增加事件--主菜单
		menu.bind('mouseenter', function(){
			// 以下为刘伟扩展，遮挡XGANT START 外围公用的，可以不用销毁。
			var ul = $('.ribbon-menus').find('> ul');
			
			if(ul.length > 0){
				if (useObjectShim) {
					var rubbionFirstIframe = $('.ribbon-menus').find('> iframe');
					if (rubbionFirstIframe.length>0) {
						
					}else {
						
						$('.ribbon-menus').find('> ul').addClass('rubbionFirstIframe');
						$(".rubbionFirstIframe").after("<iframe class='supportObjectButtomRubbion' frameborder='no' style='*width:150px;*height:300px;width:100%;height:100%;top:" + ul.position().top + "px;'> </iframe>");
					}
				}else{
					
				}
			}
			// 以上为刘伟扩展，遮挡XGANT END
			
			$('.ribbon-menus').css('display', 'block');
	    }); 
		
		//按钮增加事件--主菜单
		menu.bind('mouseleave', function(){
			$('.ribbon-menus').css('display', 'none');
	    }); 
		
		//按钮增加事件--子菜单
		menu.find('li').bind('mouseenter', function(){
			// 以下为刘伟扩展，遮挡XGANT START 私有的，添加一个IFRAME，用局部变量的思想 START。
			
			var ul = $(this).find('> ul');
			if(ul.length > 0){
				if (useObjectShim) {
					var rubbionIframe = $(this).parent().find('> iframe');
					if (rubbionIframe.length>0) {
					}else {
						ul.css('top', $(this).position().top + 'px');
						ul.css('display', 'block');
						$(this).find('> ul').addClass('rubbionIframe', this);
						$(".rubbionIframe", this).after("<iframe class='supportObjectButtomRubbion' frameborder='no' style='*width:300px;*height:400px;width:" + $(".rubbionIframe", this).width() + "px;height:" + $(".rubbionIframe", this).height() + "px;left:" + ($(this).position().left + $(".rubbionIframe", this).width()) + "px;top:" + $(this).position().top + "px;'> </iframe>");
					}
				}else{
					ul.css('top', $(this).position().top + 'px');
					ul.css('display', 'block');
				}
			}
	    }); 
		
		//按钮增加事件--子菜单
		menu.find('li').bind('mouseleave', function(){
			
			$(this).find('> ul').css('display', 'none');
			// 以下为刘伟扩展，遮挡XGANT START 私有的，必须要销毁，用局部变量的思想 START。
			if (useObjectShim) {
				var rubbionIframe = $(this).find('> iframe');
				if (rubbionIframe.length > 0) {
					$(this).find('> ul').removeClass('rubbionIframe', this);
					$(".supportObjectButtomRubbion",this).remove();
				}else {
				
				}
			}
	    }); 
		
		//按钮增加事件
		/*menu.find('li').bind('click', function(){
			$('.ribbon-menus').css('display', 'none');
	    });*/
	}

	/**
	 * 默认参数
	 */
	$.fn.ribbonMenu.defaults = {
		
	};
	
	/**
	 * 获取参数
	 */
	$.fn.ribbonMenu.parseOptions = function(target){
		var t = $(target);
		return $.extend({},  getFunc(target), {
			
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
})(jQuery);
