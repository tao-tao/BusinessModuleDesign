
UserSelectDialog = function(id,width,height,content,title,model,buttons){
	id = id || "_comframe_id_" + ( Math.random() + "" ).split(".")[1];
	var iframeId = "_iframe_" + id;
	width = width || "500";
	height = height || "500";
	title = title || "选人窗口";
	/**
	 * 显示选人对话框
	 */
	this.createDom = function(){
		if(jQuery("#" + id).length == 0 ){
			if(content.indexOf('<') == -1){
				if(content.indexOf('?') == -1){//判断content(url)是否有参数
					content + '?j='+Math.random();
				} else {
					content + '&j='+Math.random();
				}
				jQuery(document.body).append("<div id='" + id + "'><iframe frameborder='0' scrolling='no'></iframe></div>");
				var iframe = jQuery("#" + id + " iframe");
				iframe.attr("id",iframeId);
				iframe.attr("src",content);
				iframe.attr("height","99%");
				iframe.attr("width","100%");
				iframe.attr("frameborder","0");
				iframe.attr("scrolling","auto");
			} else {
				jQuery(document.body).append("<div id='" + id + "'>" + content + "</div>");
			}
		} else {
			jQuery("#" + id).dialog('open');
		}
		
	};
	if(typeof buttons == "undefined" || buttons == null){
		buttons = [];
	}
	buttons.push({
		text:'返回',
		id : 'close',
		//iconCls : 'icon-close',
		handler:function(){
			jQuery('#' + id).dialog('close');
		}
	});
	this.createDialog = function(){
		jQuery('#' + id).dialog({
			closed: true,
			modal : true,
			title: title,
			resizable:true,
			width: width,  
		    height: height,
		    collapsible : true,
		    buttons:buttons,
			onClose : function(){
				try{
					$('#processSubimt').linkbutton('enable');
			    	$('#close').linkbutton('enable');
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
		
	};
	this.createButtonsInDialog = function(buttons){
		var items = buttons;
		var buttonbar = jQuery('#' + id).children("div.dialog-button");
        for(var i = 0;i < items.length;i++){
            var item = items[i];
            var btn=jQuery("<a  href=\"javascript:void(0)\" ></a>");
            btn[0].onclick = eval(item.handler||function(){});
            jQuery(btn).prependTo(buttonbar).linkbutton(jQuery.extend({},item,{plain:false}));
        }
        buttonbar = null;
	};
	this.close = function(){
		jQuery('#' + id).dialog('close');
	};
	this.createDom();
	this.createDialog();
	this.show = function(){
		jQuery('#' + id).dialog('open');
	};
};
//扩展easyui dialog对话框组件
//动态添加/移除button,
// modify by liub @cause:dorado7重定向了原生态的闭包结构，目前不在调用
jQuery(function(){
	jQuery.extend(jQuery.fn.dialog.methods, { 
	    addButtonsItem: function(jq, items){ 
	        return jq.each(function(){ 
	            var buttonbar = jQuery(this).children("div.dialog-button");
	            for(var i = 0;i < items.length;i++){
	                var item = items[i];
	                var btn=jQuery("<a href=\"javascript:void(0)\"></a>");
	                btn[0].onclick = eval(item.handler||function(){});
	                jQuery(btn).prependTo(buttonbar).linkbutton(jQuery.extend({},item,{plain:false}));
	            }
	            buttonbar = null;
	        }); 
	    },
	    removeButtonsItem: function(jq, param){ 
	        return jq.each(function(){ 
	            var btns = jQuery(this).children("div.dialog-button").children("a");
	            var cbtn = null;
	            if(typeof param == "number"){
	                cbtn = btns.eq(param);
	            }else if(typeof param == "string"){
	                var text = null;
	                btns.each(function(){
	                    text = jQuery(this).data().linkbutton.options.text;
	                    if(text == param){
	                        cbtn = jQuery(this);
	                        text = null;
	                        return;
	                    }
	                });
	            }
	            if(cbtn){
	                var prev = cbtn.prev()[0];
	                var next = cbtn.next()[0];
	                if(prev && next && prev.nodeName == "DIV" && prev.nodeName == next.nodeName){
	                    jQuery(prev).remove();
	                }else if(next && next.nodeName == "DIV"){
	                    jQuery(next).remove();
	                }else if(prev && prev.nodeName == "DIV"){
	                    jQuery(prev).remove();
	                }
	                cbtn.remove(); 
	                cbtn= null;
	            }                      
	        }); 
	    }              
	});
});

