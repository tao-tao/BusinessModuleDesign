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
	var currentObject; 
	var flag = false;
	var i = 0;
	var subDivWidth = 0;
	var positonLeft = 0;
	//define the defaults for the plugin and how to call it	
	$.fn.dcMegaMenu = function(options){
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
		// 璁剧疆涓�叏灞�彉閲�
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
				var neck = $('.neck',this);
				// 娣诲姞鍔ㄦ�鏀瑰彉div浣嶇疆浜嬩欢  20120911
//				var subDivWidth = 0;
//				var positonLeft = 0;
				var posObject = $('.dc-mega',this);
				if(posObject.position() !=null){
					positonLeft = posObject.position().left;
				}
				if(subNav.outerWidth()!=null){
					subDivWidth = subNav.outerWidth();
				}
				var browserWidth = getWindowWidth();
				if(positonLeft+subDivWidth>browserWidth){
					beforeOpenEvent(subNav.parent().parent(),browserWidth-subDivWidth);
				}else{
					beforeOpenEvent(subNav.parent().parent(),positonLeft);
				}
				// end娣诲姞鍔ㄦ�鏀瑰彉div浣嶇疆浜嬩欢   20120911 style='*width:"+(subDivWidth+17)+"px;*height:"+subNav.outerHeight()+"px;'
				if(subNav.parent().find(".supportObjectButtom").length >0){
					$(".supportObjectButtom",this).remove();
					subNav.after("<iframe class='supportObjectButtom' frameborder='no' style='*width:"+(subDivWidth+17)+"px;*height:"+subNav.outerHeight()+"px;'> </iframe>");
				}else{
					subNav.after("<iframe class='supportObjectButtom' frameborder='no' style='*width:"+(subDivWidth+17)+"px;*height:"+subNav.outerHeight()+"px;'> </iframe>");
				}
				$(this).addClass('mega-hover');
				if(defaults.effect == 'fade'){
					$(subNav).fadeIn(defaults.speed);
					$(neck).fadeIn(defaults.speed);
				}
				if(defaults.effect == 'slide'){
					$(subNav).show(defaults.speed);
					$(neck).show(defaults.speed);
				}
				// beforeOpen callback;
				defaults.beforeOpen.call(this);
			}
			 
			function beforeOpenEvent(subCurrentObject,newPosition){
			 		subCurrentObject.css("left",(newPosition-getleftMargin())+'px');
			 		subCurrentObject.css("marginLeft",'0px');
			}
			
			// 娣诲姞浜唍eck鐨剆how浜嬩欢涓巗ub鍚屾 骞垮畨椤圭洰 
			function megaAction(obj){
				var neck = $('.neck',obj);
				var subNav = $('.sub',obj);
//				var subDivWidth = 0;
//				var positonLeft = 0;
				// 娣诲姞鍔ㄦ�鏀瑰彉div浣嶇疆浜嬩欢  20120911
				var posObject = $('.dc-mega',obj);
				if(posObject.position() !=null){
					positonLeft = posObject.position().left;
				}
				if(subNav.outerWidth()!=null){
					subDivWidth = subNav.outerWidth();
				}
				var browserWidth = getWindowWidth();
				if(positonLeft+subDivWidth>browserWidth){
					beforeOpenEvent(subNav.parent().parent(),browserWidth-subDivWidth);
				}else{
					beforeOpenEvent(subNav.parent().parent(),positonLeft);
				}
				subNav.after("<iframe class='supportObjectButtom' frameborder='no' style='*width:"+(subDivWidth+17)+"px;*height:"+subNav.outerHeight()+"px;'> </iframe>");
				// end娣诲姞鍔ㄦ�鏀瑰彉div浣嶇疆浜嬩欢   20120911
				$(obj).addClass('mega-hover');
				if(defaults.effect == 'fade'){
					$(subNav).fadeIn(defaults.speed);
					$(neck).fadeIn(defaults.speed);
				}
				if(defaults.effect == 'slide'){
					$(subNav).show(defaults.speed);
					$(neck).show(defaults.speed);
				}
				// beforeOpen callback;
				defaults.beforeOpen.call(this);
			}
			
			// 娣诲姞浜唍eck鐨刪idden浜嬩欢涓巗ub鍚屾 骞垮畨椤圭洰
			function megaOut(){
				var neck = $('.neck',this);
				var subNav = $('.sub',this);
				if($(".supportObjectButtom",this).length>0){
					$(".supportObjectButtom",this).remove();
				}
				$(this).removeClass('mega-hover');
				$(subNav).hide();
				$(neck).hide();
				// beforeClose callback;
				defaults.beforeClose.call(this);
			}
			function megaActionClose(obj){
				var subNav = $('.sub',obj);
				var neck = $('.neck',obj);
				if($(".supportObjectButtom",obj).length>0){
					$(".supportObjectButtom",obj).remove();
				}
				$(obj).removeClass('mega-hover');
				$(subNav).hide();
				$(neck).hide();
				// beforeClose callback;
				defaults.beforeClose.call(this);
			}
			function megaReset(){
				if($(".supportObjectButtom",$dcMegaMenuObj).length>0){
					$(".supportObjectButtom",$dcMegaMenuObj).remove();
				}
				$('li',$dcMegaMenuObj).removeClass('mega-hover');
				$('.sub',$dcMegaMenuObj).hide();
				$('.neck',$dcMegaMenuObj).hide();
			}

			function megaSetup(){
				$arrow = '<span class="dc-mega-icon"></span>';
				var clParentLi = clParent+'-li';
				var menuWidth = getWindowWidth();//$dcMegaMenuObj.outerWidth();
				if($('.sub-container-parent').length){
					$('.neck').remove();
				}else{
					$('.sub-container').wrap("<div class='sub-container-parent'></div>");
				}
				
				if($('.sub-container').length){
					$('.sub-container').before('<div class="neck"></div>');
				}else{
					$('.sub-container').before('<div class="neck"></div>');
				}
				$('> li',$dcMegaMenuObj).each(function(){
					//Set Width of sub
					a = 1;
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
							//$('.sub').after("<iframe class='supportObjectButtom'> </iframe>");
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
								//var subLeft = pl - ml;
								// If Left Position Is Negative Set To Left Margin
								
								 params = {left: (pl-41)+'px', marginLeft: -ml+'px'};
								
								// 濡傛灉褰撳墠鐨刣iv鍔犱笂pos澶т簬娴忚鍣ㄥ疄闄呯殑瀹藉害锛屽垯浠ユ渶鍙宠竟鐨勪负涓汇�
								// subw寮瑰嚭灞傜殑瀹藉害  pl涓� menuWidth
								if(subw+pl>menuWidth){
									params = {marginLeft:123,left:menuWidth-subw+'px'};
								}
							}
//							$('.'+clContainer,this).css(params);
							// Calculate Row Height
							$mainSub.hide();
					
						} else {
							var pos = $(this).position();
							pl = pos.left;
							var subw = $mainSub.outerWidth();
							if(subw+pl>menuWidth){
								$('.'+clContainer,this).addClass('non-mega').css('left',menuWidth-subw+'px').css('marginLeft',0);
							}else{
								$('.'+clContainer,this).addClass('non-mega').css('left',(pl+1)+'px');
								$('.sub-container-parent',this).addClass('non-mega').css('left',(pl+1)+'px');
							}
							//$('.sub').after("<iframe class='supportObjectButtom'> </iframe>");
						}
					}
				});
				// Set position of mega dropdown to bottom of main menu
				var menuHeight = $('> li > a',$dcMegaMenuObj).outerHeight(true);
				//$('.'+clContainer,$dcMegaMenuObj).css({top: menuHeight+'px'}).css('z-index','1000');
				$('.sub-container-parent').css({top: '27px'}).css('z-index','1003');
				//.css('left',$('.'+clContainer).css('left')).css('marginLeft',$('.'+clContainer).css('margin-left'));

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
						if($parentLi.hasClass('mega-hover')){
							++i;
							if(i == 2 && flag == false){
								megaActionClose($parentLi);
							}
							if(i==2){
								i = 0;
								flag = false;
							}
						} else {
							++i;
							megaAction($parentLi);
							flag = true;
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

// 璁＄畻娴忚鍣ㄧ殑瀹藉害銆備笌褰撳墠鐨刣iv寮瑰嚭灞傜殑瀹藉害杩涜姣旇緝銆�
function getWindowWidth(){
	var winWidth = 0;
	if (window.innerWidth){
		   winWidth = window.innerWidth;
	} else {
		   if ((document.body) && (document.body.clientWidth)){
			   winWidth = document.body.clientWidth;
		   }
	}
	//閫氳繃娣卞叆Document鍐呴儴瀵筨ody杩涜妫�祴锛岃幏鍙栫獥鍙ｅぇ灏�
	if (document.documentElement && document.documentElement.clientWidth){
		   winWidth = document.documentElement.clientWidth;
	}
	return winWidth;
}


function getleftMargin(){
	   var leftMargin = 0;
	   var Sys = {};
	  var ua = navigator.userAgent.toLowerCase();
	  try{
	      var s;
				(s = ua.match(/msie ([\d.]+)/)) ? Sys.ie = s[1] :
				(s = ua.match(/firefox\/([\d.]+)/)) ? Sys.firefox = s[1] :
				(s = ua.match(/chrome\/([\d.]+)/)) ? Sys.chrome = s[1] :
				(s = ua.match(/opera.([\d.]+)/)) ? Sys.opera = s[1] :
				(s = ua.match(/version\/([\d.]+).*safari/)) ? Sys.safari = s[1] : 0;
		     
		     if(Sys.ie) leftMargin = 1;
		     if(Sys.firefox) leftMargin = 2;
		     if(Sys.chrome) leftMargin = 2;
		     if(Sys.opera) rightHeight = 2;
		     if(Sys.safari) rightHeight = 2;
		     
		     //鍒ゆ柇IE鐗堟湰
		     if(Sys.ie && ua.indexOf('msie 6.0') > 1){
		    	 leftMargin = 2;
		     }
	  }catch(e){}
	  return leftMargin;
	}