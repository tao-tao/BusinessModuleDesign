/**
 * add by liuwei
 * 判断当前IE版本
 * @returns
 */
var ie = (function(){
    var undef, v = 3, div = document.createElement('div'), all = div.getElementsByTagName('i');
    while (div.innerHTML = '<!--[if gt IE ' + (++v) + ']><i></i><![endif]-->', all[0]);
    return v > 4 ? v : undef;
}());

/**
 * add by liuwei
 * 针对panel window dialog三个组件调整大小时会超出父级元素的修正
 * 如果父级元素的overflow属性为hidden，则修复上下左右个方向
 * 如果父级元素的overflow属性为非hidden，则只修复上左两个方向
 * @param width
 * @param height
 * @returns
 */
var easyuiPanelOnResize = function(width, height){
	if(!$.data(this,'window') && !$.data(this,'dialog')) return;
	if(ie===8){
		var data = $.data(this, "window") || $.data(this, "dialog");
		if(data.pmask){
			var masks = data.window.nextAll('.window-proxy-mask');
			if(masks.length > 1){
				$(masks[1]).remove();
        		masks[1] = null;
			}
		}
	}
	if($(this).panel('options').maximized==true){
		$(this).panel('options').fit=false;
	}
    $(this).panel('options').reSizing = true;
	if(!$(this).panel('options').reSizeNum){
		$(this).panel('options').reSizeNum = 1;
	}else{
		$(this).panel('options').reSizeNum++;
	}		
    var parentObj = $(this).panel('panel').parent();
    var left = $(this).panel('panel').position().left;
    var top = $(this).panel('panel').position().top;
    
    if ($(this).panel('panel').offset().left < 0) {
        $(this).panel('move', {
            left: 0
        });
    }
    if ($(this).panel('panel').offset().top < 0) {
        $(this).panel('move', {
            top: 0
        });
    }
    
    if (left < 0) {
        $(this).panel('move', {
            left: 0
        }).panel('resize', {
            width: width + left
        });
    }
    if (top < 0) {
        $(this).panel('move', {
            top: 0
        }).panel('resize', {
            height: height + top
        });
    }
    if (parentObj.css("overflow") == "hidden") {
		var inline = $.data(this, "window").options.inline;
				if(inline==false){
					parentObj = $(window);
				}
 
                if ((width + left > parentObj.width()) && $(this).panel('options').reSizeNum > 1) {
            $(this).panel('resize', {
                width: parentObj.width() - left
            });
        }
        
        if ((height + top > parentObj.height()) && $(this).panel('options').reSizeNum > 1) {
            $(this).panel('resize', {
                height: parentObj.height() - top
            });
        }  
    }
    $(this).panel('options').reSizing = false;
};

/**
 * add by liuwei
 * 针对panel window dialog三个组件拖动时会超出父级元素的修正
 * 如果父级元素的overflow属性为hidden，则修复上下左右个方向
 * 如果父级元素的overflow属性为非hidden，则只修复上左两个方向
 * @param left
 * @param top
 * @returns
 */
var easyuiPanelOnMove = function(left, top){
    //alert(left+" "+top);
	if ($(this).panel('options').reSizing) 
        return;
    var parentObj = $(this).panel('panel').parent();
    var width = $(this).panel('options').width;
    var height = $(this).panel('options').height;
   // var right = left + width;
   // var buttom = top + height;
   // var parentWidth = parentObj.width();
   // var parentHeight = parentObj.height();
    
    if (left < 0) {
        $(this).panel('move', {
            left: 0
        });
    }
    if (top < 0) {
        $(this).panel('move', {
            top: 0
        });
    }
    
    if (parentObj.css("overflow") == "hidden") {
    	if(typeof($.data(this, "window")) != 'undefined'){
    		var inline = $.data(this, "window").options.inline;
    		if(inline==false){
    			parentObj = $(window);
    		}
            
    		if (left > parentObj.width() - width) {
                if(left<=0)
                {
                	$(this).panel({width:parentObj.width() });
                }
                else
                {
	            	$(this).panel('move', {
	                    "left": parentObj.width() - width
	                });
            	}
            }
            if (top > parentObj.height() - height) {
            	if(top<=0)
                {
                	$(this).panel({height: parentObj.height()}) ;
                    
                }
                else
                {
	            	$(this).panel('move', {
	                    "top": parentObj.height() - height
	                });
                }
            }
    	}
    }
};
// 重写默认的方法
$.fn.panel.defaults.onResize = easyuiPanelOnResize;
$.fn.window.defaults.onResize = easyuiPanelOnResize;
$.fn.dialog.defaults.onResize = easyuiPanelOnResize;
$.fn.window.defaults.onMove = easyuiPanelOnMove;
$.fn.panel.defaults.onMove = easyuiPanelOnMove;
$.fn.dialog.defaults.onMove = easyuiPanelOnMove;

CommonDialog = function(id,width,height,content,title,closable,modal,useDefaultButton,appendIframe,isMaximized,maximizable){
	id = id || "_comframe_id_" + ( Math.random() + "" ).split(".")[1];
	var iframeId = "_iframe_" + id;
	width = width || "500";
	height = height || "500";
	title = title || "选人窗口";
	if(typeof(closable) == 'undefined'||closable == null){
		closable = true;
	}
	
	if(typeof(modal) == 'undefined'||modal == null){
		modal = true;
	}
	
	if(typeof(useDefaultButton) == 'undefined'|| useDefaultButton == null){
		useDefaultButton = true;
	}
	
	//设置是否显示遮罩Iframe
	if(typeof(appendIframe) == 'undefined'|| appendIframe == null){
		appendIframe = false;
	}
	
	if(typeof(isMaximized) == 'undefined'|| isMaximized == null){
		isMaximized = false;
	}
	
	if(typeof(maximizable) == 'undefined'|| maximizable == null){
		maximizable = true;
	}
	/**
	 * 显示对话框
	 */
	this.createDom = function(){
		// START modal 为false时，重复打开CommonDialog，CommonDialog会变大。先关闭已经打开的，再打开新的。
		if(jQuery("#" + id).length > 0 ){
			jQuery('#' + id).dialog('close');
		}
		// END
		
		if(jQuery("#" + id).length == 0 ){
			if(content.indexOf('<') == -1){
				if(content.indexOf('?') == -1){//判断content(url)是否有参�?
					content + '?j='+Math.random();
				} else {
					content + '&j='+Math.random();
				}
				var iframe = jQuery("#" + id+" iframe");
				if(typeof(iframe.attr("id"))=='undefined'){
					jQuery(document.body).append("<div id='" + id + "'><iframe frameborder='0' scrolling='no'></iframe></div>");
					iframe = jQuery("#" + id + " iframe");
					iframe.attr("id",iframeId);
					iframe.attr("height","99%");
					iframe.attr("width","100%");
					iframe.attr("frameborder","0");
					iframe.attr("scrolling","auto");
				}else{
				}
			} else {
				jQuery(document.body).append("<div id='" + id + "'>" + content + "</div>");
			}
		} else {
			//jQuery("#" + id).dialog('open');
		}
		
	};
	this.createDialog = function(){
		
		if(useDefaultButton){
			var closeButtons=[{
				text:'返回',
				id : 'closeButton',
				//iconCls : 'icon-no',
				handler:function(){
					jQuery('#' + id).dialog('close');
				}
			}];
			jQuery('#' + id).dialog({
				closable: closable,
				title: title,
				resizable:true,
				width: width,  
			    height: height,
			    modal:modal,
			    maximizable:maximizable,
			    minimizable:false,
			    maximized:isMaximized,
			    collapsible : true,
			    inline:true,
			    shadow: false,
			    buttons:closeButtons,
				onClose : function(){
//					try{
//					}catch(e){}
					var iframe = jQuery("#" + iframeId);
			        if(iframe){ 
			        	try{ 
							iframe.attr("src","about:blank");
			        		iframe.contentWindow.document.write(''); 
			        		iframe.contentWindow.document.clear(); 
			        	}catch(e){}; 
			        } 
					jQuery("#" + id).remove();
				}
			});
		}else{
			jQuery('#' + id).dialog({
				closed: true,
				title: title,
				resizable:true,
				width: width,  
			    height: height,
			    modal:modal,
			    maximizable:true,
			    resizable:true,
			    minimizable:false,
			    maximized:isMaximized,
			    collapsible : true,
			    inline:true,
			    shadow: false,
				onClose : function(){
					try{
					}catch(e){}
					var iframe = jQuery("#" + iframeId);
			        if(iframe){ 
			        	try{ 
							iframe.attr("src","about:blank");
			        		iframe.contentWindow.document.write(''); 
			        		iframe.contentWindow.document.clear(); 
			        	}catch(e){}; 
			        } 
					jQuery("#" + id).remove();
				}
			});
		}
	};
	this.createButtonsInDialog = function(buttons){
		var items = buttons;
		var buttonbar = jQuery('#' + id).children("div.dialog-button");
        for(var i = 0;i < items.length;i++){
            var item = items[i];
            var btn=jQuery("<a href=\"javascript:void(0)\"></a>");
            btn[0].onclick = eval(item.handler||function(){});
            jQuery(btn).prependTo(buttonbar).linkbutton(jQuery.extend({},item,{plain:false}));
        }
        buttonbar = null;
	};
	this.close = function(){
		/*
               if (appendIframe == true){
		$(".platform6BackgroundIframe").remove();
                }
         */
		jQuery('#' + id).dialog('close');
	};
	this.createDom();
	this.createDialog();
	this.show = function(){
		jQuery('#' + id).dialog('open');
		var iframe = jQuery("#" + id + " iframe");
		iframe.attr("src",content);
		/*并不是所有的页面都需要追加iframe,如果加上会影响拖拽，如果想用commondialog遮盖甘特图的话，可用avicit/cape/pmplan/taskinfo/CommonDialog.js
		if (appendIframe == true){
			var windowMask = $(".window-mask");
			if (windowMask.length > 0){
				windowMask.append("<iframe class='platform6BackgroundIframe' frameborder='no'> </iframe>");
			}
		}
		*/
	};
	
};
