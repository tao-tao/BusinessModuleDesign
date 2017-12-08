/*
 * DC Mega Menu - jQuery mega menu
 * Copyright (c) 2011 Design Chemical
 *
 * Dual licensed under the MIT and GPL licenses:
 * 	http://www.opensource.org/licenses/mit-license.php
 * 	http://www.gnu.org/licenses/gpl.html
 *
 */
(function($){
	var currentObject ; 
	var subDivWidth;
	var positonLeft;
	//define the defaults for the plugin and how to call it	
	$.fn.dcMegaMenu = function(options,resizeFlag){
		//set default options  
		var defaults = {
			classParent: 'dc-mega',
			classContainer: 'sub-container',
			classSubParent: 'mega-hdr',
			classSubLink: 'mega-hdr',
			classWidget: 'dc-extra',
			rowItems: 3,
			speed: 'fast',
			effect: 'fade',
			event: 'hover',
			fullWidth: false,
			onLoad : function(){},
            beforeOpen : function(){},
			beforeClose: function(){}
		};

		//call in the default otions
		var options = $.extend(defaults, options);
		var $dcMegaMenuObj = this;
		// 设置一全局变量
		if(typeof(currentObject) == 'undefined'){
			currentObject = this;
		} else {
			$dcMegaMenuObj = currentObject;
		}
		//act upon the element that is passed into the design    
		return $dcMegaMenuObj.each(function(options){

			var clSubParent = defaults.classSubParent;
			var clSubLink = defaults.classSubLink;
			var clParent = defaults.classParent;
			var clContainer = defaults.classContainer;
			var clWidget = defaults.classWidget;	
			
			megaSetup();
			
			function megaOver(){
				var subNav = $('.sub',this);
				var posObject = $('.dc-mega',this);
				if(posObject.position() !=null){
					positonLeft = posObject.position().left;
				}
				if(subNav.outerWidth()!=null){
					subDivWidth = subNav.outerWidth();
				}
				// 设置成全局变量，此方法是当浏览器的大小变化时，会自动改变弹出层的位置。时时要改变。
				// 自定义扩展了一个方法，beforeOpenEvent
				var browserWidth = getWindowWidth();
				if(positonLeft+subDivWidth>browserWidth){
					beforeOpenEvent(subNav.parent(),browserWidth-subDivWidth);
				}else{
					beforeOpenEvent(subNav.parent(),positonLeft);
				}
				$(this).addClass('mega-hover');
				if(defaults.effect == 'fade'){
					$(subNav).fadeIn(defaults.speed);
				}
				if(defaults.effect == 'slide'){
					$(subNav).show(defaults.speed);
				}
			}
			
			
			function beforeOpenEvent(subCurrentObject,newPosition){
			 	if(resizeFlag =='1'){
			 		subCurrentObject.css("left",(newPosition-3)+'px');
			 		subCurrentObject.css("marginLeft",'0px');
            	}
			}
			// 为了能兼容onclick事件，当浏览器的大小发生变化时,会全部执行。与megaOver方法不同的是，onclick事件如果传resizeFlag过来，会执行N多次，为了提高效率，帮采用此方法
			function beforeOpenEventAll(subCurrentObject,newPosition){
			 		subCurrentObject.css("left",(newPosition-3)+'px');
			 		subCurrentObject.css("marginLeft",'0px');
			}
			function megaAction(obj){
				var subNav = $('.sub',obj);
				var posObject = $('.dc-mega',obj);
				if(posObject.position() !=null){
					positonLeft = posObject.position().left;
				}
				if(subNav.outerWidth()!=null){
					subDivWidth = subNav.outerWidth();
				}
				// 设置成全局变量，此方法是当浏览器的大小变化时，会自动改变弹出层的位置。时时要改变。
				// 自定义扩展了一个方法，beforeOpenEvent
				var browserWidth = getWindowWidth();
				if(positonLeft+subDivWidth>browserWidth){
					beforeOpenEventAll(subNav.parent(),browserWidth-subDivWidth);
				}else{
					beforeOpenEventAll(subNav.parent(),positonLeft);
				}
				
				$(obj).addClass('mega-hover');
				if(defaults.effect == 'fade'){
					$(subNav).fadeIn(defaults.speed);
				}
				if(defaults.effect == 'slide'){
					$(subNav).show(defaults.speed);
				}
				// beforeOpen callback;
				defaults.beforeOpen.call(this);
			}
			function megaOut(){
				var subNav = $('.sub',this);
				$(this).removeClass('mega-hover');
				$(subNav).hide();
				// beforeClose callback;
				defaults.beforeClose.call(this);
			}
			function megaActionClose(obj){
				var subNav = $('.sub',obj);
				$(obj).removeClass('mega-hover');
				$(subNav).hide();
				// beforeClose callback;
				defaults.beforeClose.call(this);
			}
			function megaReset(){
				$('li',$dcMegaMenuObj).removeClass('mega-hover');
				$('.sub',$dcMegaMenuObj).hide();
			}

			function megaSetup(){
				$arrow = '<span class="dc-mega-icon"></span>';
				var clParentLi = clParent+'-li';
				var menuWidth = getWindowWidth();//$dcMegaMenuObj.outerWidth();
				$('> li',$dcMegaMenuObj).each(function(obj){
					//Set Width of sub
					var $mainSub = $('> ul',this);
					var $primaryLink = $('> a',this);
					if($mainSub.length){
						$primaryLink.addClass(clParent).append($arrow);
						$mainSub.addClass('sub').wrap('<div class="'+clContainer+'" />');
						var pos = $(this).position();
						pl = pos.left;
						if($('ul',$mainSub).length){
							$(this).addClass(clParentLi);
							$('.'+clContainer,this).addClass('mega');
//							$('.sub').after("<iframe class='supportObjectButtom'> </iframe>");
							$('> li',$mainSub).each(function(){
								if(!$(this).hasClass(clWidget)){
									$(this).addClass('mega-unit');
									if($('> ul',this).length){
										$(this).addClass(clSubParent);
										$('> a',this).addClass(clSubParent+'-a');
									} else {
										$(this).addClass(clSubLink);
										$('> a',this).addClass(clSubLink+'-a');
									}
								}
							});
							// Get Sub Dimensions & Set Row Height
							$mainSub.show();
							// Get Position of Parent Item
							var pw = $(this).width();
							var pr = pl + pw;
							// Check available right margin
							var mr = menuWidth - pr;
							// // Calc Width of Sub Menu
							var subw = $mainSub.outerWidth();
							var totw = $mainSub.parent('.'+clContainer).outerWidth();
							var cpad = totw - subw;
							if(defaults.fullWidth == true){
								var fw = menuWidth - cpad;
								$mainSub.parent('.'+clContainer).css({width: fw+'px'});
								$dcMegaMenuObj.addClass('full-width');
							}
							var iw = $('.mega-unit',$mainSub).outerWidth(true);
							var rowItems = $('.row:eq(0) .mega-unit',$mainSub).length;
							var inneriw = iw * rowItems;
							var totiw =inneriw+ cpad;
							// Calc Required Left Margin incl additional required for right align
							if(defaults.fullWidth == true){
								params = {left: 0};
							} else {
								var ml = mr < ml ? ml + ml - mr : (totiw - pw)/2;
								var subLeft = pl - ml;
								// If Left Position Is Negative Set To Left Margin
								var params = {left: (pl)+'px', marginLeft: -2+'px'};
								
								// 如果当前的div加上pos大于浏览器实际的宽度，则以最右边的为主。
								// subw弹出层的宽度  pl为  menuWidth
								if(subw+pl>menuWidth){
									var params = {marginLeft:0,left:menuWidth-subw+'px'};
								}
							}
							$('.'+clContainer,this).css(params);
							
							$mainSub.hide();
					
						} else {
							var pos = $(this).position();
							pl = pos.left;
							var subw = $mainSub.outerWidth();
							if(subw+pl>menuWidth){
								$('.'+clContainer,this).addClass('non-mega').css('left',menuWidth-subw+'px').css('marginLeft',0);
							}else{
								$('.'+clContainer,this).addClass('non-mega').css('left',pl+'px');
							}
//							$('.sub').after("<iframe class='supportObjectButtom'> </iframe>");
						}
					}
				});
				// Set position of mega dropdown to bottom of main menu
				var menuHeight = $('> li > a',$dcMegaMenuObj).outerHeight(true);
				$('.'+clContainer,$dcMegaMenuObj).css({top: menuHeight+'px'}).css('z-index','1000');
				
				if(defaults.event == 'hover'){
					// HoverIntent Configuration
					var config = {
						sensitivity: 2,
						interval: 100,
						over: megaOver,
						timeout: 400,
						out: megaOut
					};
					$('li',$dcMegaMenuObj).hoverIntent(config);
				}
				
				if(defaults.event == 'click'){
					
					$('body').mouseup(function(e){
						if(!$(e.target).parents('.mega-hover').length){
							megaReset();
						}
					});
					$('> li > a.'+clParent,$dcMegaMenuObj).click(function(e){
						var $parentLi = $(this).parent();
							if(resizeFlag == '0'){
								if($parentLi.hasClass('mega-hover')){
									megaActionClose($parentLi);
								} else {
									megaAction($parentLi);
								}
						}else{
							
						}
						e.preventDefault();
					});
				}
				// onLoad callback;
				defaults.onLoad.call(this);
			}
		});
	};
})(jQuery);

// 计算浏览器的宽度。与当前的div弹出层的宽度进行比较。
function getWindowWidth(){
	var winWidth = 0;
	if (window.innerWidth){
		   winWidth = window.innerWidth;
	} else {
		   if ((document.body) && (document.body.clientWidth)){
			   winWidth = document.body.clientWidth;
		   }
	}
	//通过深入Document内部对body进行检测，获取窗口大小
	if (document.documentElement && document.documentElement.clientWidth){
		   winWidth = document.documentElement.clientWidth;
	}
	return winWidth;
}
