/**
 * 创建通用选择对话框
 * @param id            可选 创建dialog需要用的div的id
 * @param width      可选 dialog的宽
 * @param height     可选 dialog的高
 * @param content   必填 dialog需要显示的内容（url或文本内容）
 * @param title         可选 dialog的标题
 * @param closable   可选 是否显示dialog的toolbar的关闭按钮
 * @param modal      可选 是否设置额为模态
 */
CommonSelectorDialog= function(id,width,height,content,title,onCloseFunction,closable,modal,useDefaultButton){
	id = id || "_comframe_id_" + ( Math.random() + "" ).split(".")[1];
	var iframeId = "_iframe_" + id;
	width = width || "500";
	height = height || "500";
	title = title || "选人窗口";
	if(closable == null || closable == ''){
		closable = true;
	}
	
	if(typeof(modal) == 'undefined'||modal == null || modal == ''){
		modal = true;
	}
	
	if(typeof(useDefaultButton) == 'undefined'|| useDefaultButton == null){
		useDefaultButton = true;
	}
	/**
	 * 显示对话框
	 */
	this.createDom = function(){
		if(jQuery("#" + id).length == 0 ){
			if(content.indexOf('<') == -1){
				if(content.indexOf('?') == -1){//判断content(url)是否有参数
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
			jQuery("#" + id).dialog('open');
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
			closed: true,
			title: title,
			resizable:true,
			width: width,  
		    height: height,
		    modal:modal,
		    maximizable:false,
		    minimizable:false,
		    collapsible : true,
		    buttons:closeButtons,
			onClose : function(){
				if(onCloseFunction != null && onCloseFunction != 'undefined'){
					 eval(onCloseFunction);
				}
				jQuery('#' + id).dialog('destroy');
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
			    maximizable:false,
			    resizable:true,
			    minimizable:false,
			    collapsible : true,
				onClose : function(){
					if(onCloseFunction != null && onCloseFunction != 'undefined'){
						 eval(onCloseFunction);
					}
					jQuery('#' + id).dialog('destroy');
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
		$(".platform6BackgroundIframe").remove();
		jQuery('#' + id).dialog('close');
	};
	this.getDialogDivId=function(){
		return id ;
	};
	this.getDialogIframeId=function(){
		return iframeId ;
	};
	this.createDom();
	this.createDialog();
	this.show = function(){
		jQuery('#' + id).dialog('open');
		var iframe = jQuery("#" + id + " iframe");
		iframe.attr("src",content);
		if (modal==true){
			var windowMask = $(".window-mask");
			windowMask.append("<iframe class='platform6BackgroundIframe' frameborder='no'> </iframe>");
		}else {
			iframe.parent().parent().parent().after("<iframe class='platform6BackgroundIframe' frameborder='no'> </iframe>");
		}
	};
};


