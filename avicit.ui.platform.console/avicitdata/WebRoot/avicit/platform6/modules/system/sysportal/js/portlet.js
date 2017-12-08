var colorClasses = new Array('color-white','color-yellow', 'color-red', 'color-blue', 'color-gray', 'color-orange', 'color-green');
var config = {
	isDrag : false	
};
	$(function() {
		var sortableContent;
		//$(".column")
		if(!config.isDrag){
			$(".portlet-header").css('cursor','');
		}
		if(config.isDrag){
			eventOnMouseDrag = function(){
				$(".column").sortable({
//					cursorAt: 'top',
					grid: [50, 20],
					revert : true,
					tolerance : 'pointer',
					connectWith: ".column",
					helper: 'clone',
					forcePlaceholderSize: true,
					stop : function(e,ui){
//						return false;
						ui.item.find(".portlet-content").html(sortableContent);
						$savePreferences();
						$(".column").sortable('destroy');
					},
					start : function(e,ui){
						var content = ui.helper.find(".portlet-content");
						sortableContent = content.html();
						content.html("");
					}
				});
			};
		}
		$(".portlet" ).addClass("ui-widget ui-widget-content ui-helper-clearfix ui-corner-all" )
			.find(".portlet-header" ).end()
			.find(".portlet-content" );
		
		if(config.isDrag){// add close
			$('<a href="#" class="remove">CLOSE</a>').mousedown(function (e) {
	            e.stopPropagation();    
	        }).click(function () {
	            if(confirm('确定要移除该小组件吗?')) {
	            	$(this).parents(".portlet").animate({
	                    opacity: 0    
	                },function () {
	                    $(this).wrap('<div/>').parent().slideUp(function () {
	                        $(this).remove();
	                        $savePreferences();
	                    });
	                });
	            }
	            return false;
	        }).appendTo($.find(".portlet-header"));
		}//end add close
		
		//add edit
		$('<a href="#" class="edit">EDIT</a>').mousedown(function (e) {
            e.stopPropagation();    
        }).toggle(function (){
           $(this).parents(".portlet").find('.edit-box').show().find('input').val($(this).parents(".portlet").find("h3").text());
           $(this).parents(".portlet").find('.edit-box').show().find('input').focus();
           $(this).attr('class','editToggle');
           return false;
       },function () {
    	   $(this).attr('class','edit');
           $(this).css({backgroundPosition: '', width: '15px'}).parents(".portlet").find('.edit-box').hide();
           return false;
      }).appendTo($.find(".portlet-header"));
		
      $('<div class="edit-box" style="display:none;"/>')
          .append('<ul><li class="item"><label>标题 :</label><input value=""/><span title="恢复默认标题">&nbsp;&nbsp;&nbsp;&nbsp;</span></li>')
          .append((function(){
              var colorList = '<li class="item"><label>外观 :</label><ul class="colors">';
              for(var i = 0 ; i < colorClasses.length ; i++){
            	  colorList += '<li class="' + colorClasses[i] + '"/>';
              }
             return colorList + '</ul>';
       })()).append('</ul>').insertAfter($(".portlet-header",this));
      // set person config
		$('.edit-box').each(function () {
            $('input',this).keyup(function () {
                $(this).parents(".portlet").find('h3').text( $(this).val().length > 20 ? $(this).val().substr(0,20)+'...' : $(this).val());
                $savePreferences();
            });
            //恢复默认标题
             $('ul li.item span',this).click(function(){
            	var that = this;
             	var portletId = $(this).parents('.portlet').attr('id');
             	$.ajax({
        		    url : path + "platform/portlet/reDefaultTitle.json",
        		    method : "GET",
        			data : "portletId=" + portletId,
        		    success: function(msg){
        		    	$(that).parents('.portlet').find('h3').text(msg);
        		    	$(that).parents('.portlet').find('input').val(msg);
        		    	$savePreferences();
        		   }
        		});
             });
             //选择portlet外观 color
            $('ul.colors li',this).click(function () {
                var  className = $(this).parents(".portlet").find(".portlet-header").attr('class');
                if (className) {
                	var colorStylePattern = /\bcolor-[\w]{1,}\b/;
                	$(this).parents(".portlet").find(".portlet-header").removeClass(className).addClass("portlet-header " + $(this).attr('class').match(colorStylePattern)[0]);
                	var borderStyle = $(this).parents(".portlet").find(".portlet-header").css('background-color');
                	if(borderStyle == 'rgb(255, 255, 255)' || borderStyle == 'transparent' || borderStyle == '#ffffff' || borderStyle == 'rgba(0, 0, 0, 0)'){
                		$(this).parents(".portlet").css('border-color','#999999');
                	} else {
                		$(this).parents(".portlet").css('border-color',borderStyle);
                	}
                	$savePreferences();
                    return false;
                }
            });
        });
		// end set person config
		$(".column" ).disableSelection();
		//end and edit 
		
		if(config.isDrag){// add collapse
			 $('<a href="#" class="collapse">COLLAPSE</a>').mousedown(function (e) {
	               /* STOP event bubbling */
	               e.stopPropagation();    
	         }).click(function(){
	        	 $(this).parents(".portlet").toggleClass('collapsed');
	         	 $savePreferences();
	            return false;    
	         }).appendTo($.find(".portlet-header"));
		}// end add collapse
		
		 // add refresh
		$('<a href="#" class="refresh">REFRESH</a>').mousedown(function (e) {
             /* STOP event bubbling */
             e.stopPropagation();    
       }).click(function(){
    	   var iframe = $(this).parents(".portlet").find('iframe')
    	   iframe.attr('src',iframe.attr('src'));
           return false;    
       }).appendTo($.find(".portlet-header"));
		 //end add refresh
		 
		
		//save 
		$savePreferences = function () {
	        var sortResultString = '';
	        /* Assemble the history string */
	        $(".column").each(function(i){
	        	sortResultString += (i == 0) ? '' : '|';
	            $(".portlet",this).each(function(i){
	            	sortResultString += (i == 0) ? '' : ';';
	                /* ID of widget: */
	                sortResultString += $(this).attr('id') + ',';
	                /* Color of widget (color classes) */
	                if($(this).find(".portlet-header").length > 0){
	                	sortResultString += $(this).find(".portlet-header").attr('class').match(/\bcolor-[\w]{1,}\b/) + ',';
		                /* Title of widget (replaced used characters) */
		                sortResultString += $('h3:eq(0)',this).text().replace(/\|/g,'[-PIPE-]').replace(/,/g,'[-COMMA-]') + ',';
		                /* Collapsed/not collapsed widget? : */
		                sortResultString += $(".portlet-content",this).css('display') == 'none' ? 'collapsed' : 'not-collapsed';
	                }
	            });
	        });
	        //alert(sortResultString);
	        saveSortWidgets(sortResultString);
	    }
		
		/* Read history: */
        if(!historyLayoutContent) {
        	$(".column").css({visibility:'visible'});
            return;
        } else {
        	/* For each column */
            if(historyLayoutContent.indexOf(":") == -1){
            	 $(".column").each(function(i){
            		 var thisColumn = $(this);
                     var tmp = historyLayoutContent.split('|')[i];
                     if(!tmp){
                     	return;
                     }
                     var widgetData = tmp.split(';');
                     if(typeof(widgetData) != 'undefined' && widgetData != ''){
                    	 $(widgetData).each(function(){
                    		 if(this.length){
                    			 var thisWidgetData = this.split(',');
                    			 var clonedWidget = $('#' + thisWidgetData[0]);
                    			 if(typeof(thisWidgetData[1]) != 'null'){
                                 	var className = clonedWidget.find(".portlet-header").attr('class');
                                     if(className) {
                                    	 clonedWidget.find(".portlet-header").removeClass(className).addClass(className + " " + thisWidgetData[1]);
                                    	 var borderStyle = clonedWidget.find(".portlet-header").css('background-color');
                                     	 if(borderStyle){
                                     		if(borderStyle == 'rgb(255, 255, 255)' || borderStyle == 'transparent' || borderStyle == '#ffffff' || borderStyle == 'rgba(0, 0, 0, 0)'){
                                         		clonedWidget.css('border-color','#999999');
                                         	} else {
                                         		clonedWidget.css('border-color',borderStyle);
                                         	}
                                     	 }
                                     }
                                 }
                    			 if($(clonedWidget).find('h3:eq(0)').length > 0){
                    				 if(typeof(thisWidgetData[2]) != 'undefined'){
                    					 $(clonedWidget).find('h3:eq(0)').html(thisWidgetData[2].replace(/\[-PIPE-\]/g,'|').replace(/\[-COMMA-\]/g,','));//替换portlet标题
                    				 }
                                     if(typeof(thisWidgetData[3]) != 'undefined' && thisWidgetData[3]==='collapsed') {
                                         $(clonedWidget).addClass('collapsed');
                                     }
                    			 }
//                                 $('#' + thisWidgetData[0]).remove();
                                 $(thisColumn).append(clonedWidget);
                    		 }
                    	 });
                     }
            	});
            }
        }
	});
	$(document).ready(function(){
		document.getElementById('loading').style.display='none';
		document.getElementById('mainPanel').style.display='';
		
		//如果是ie mise 8.0 重新加载一下指定的url
		 if(getBrowType() == 'ie msie 8.0'){
			 $(".portlet").each(function(){
				 var iframe = $(this).find(".portlet-content iframe");
				 var iframeSrc = iframe.attr('src');
				 iframe.attr('src',iframeSrc);
			 });
		 }
	});
	/**
	 * 保存拖动排序后结果
	 * @param sortResultString
	 */
	function saveSortWidgets(sortResultString){
		$.ajax({
		    url : path + "platform/portlet/saveSortResult.json",
		    method : "POST",
			data : "sortResult=" + encodeURI(encodeURI(sortResultString)) + "&flag=" + flag,
		    success: function(msg){
		    	
		   }
		});
	}
	function openUrlEvent(title,url){
		openContent(title,url);
	}
	/**
	 * 打开portlet
	 * @param url
	 */
	function openContent(title,url){
		try{
			parent.parent.addTab(title,url);
		}catch(e){}
		//pmo调用两层
		try{
			parent.addTab(title,url);
		}catch(e){
			window.open(url);
		}
		return;
	}
	/**
	 * 恢复默认标题
	 * @param obj
	 */
	function reDefaultTitle(obj){
		return false;
	}
	
   function getBrowType(){
	  var Sys = {};
	  var ua = navigator.userAgent.toLowerCase();
	  try{
	      var s;
		  (s = ua.match(/msie ([\d.]+)/)) ? Sys.ie = s[1] :
		  (s = ua.match(/firefox\/([\d.]+)/)) ? Sys.firefox = s[1] :
		  (s = ua.match(/chrome\/([\d.]+)/)) ? Sys.chrome = s[1] :
		  (s = ua.match(/opera.([\d.]+)/)) ? Sys.opera = s[1] :
		  (s = ua.match(/version\/([\d.]+).*safari/)) ? Sys.safari = s[1] : 0;
		  if(Sys.ie){
			  if(Sys.ie && ua.indexOf('msie 8.0') > 1){
	     			return 'ie msie 8.0';
			  }
			  return 'ie';
		  }
		  if(Sys.firefox){
			  return "ff";
		  }
		  if(Sys.chrome){
			  return  "chrome";
		  }
		  if(Sys.opera){
			 return "safari"; 
		  }
	  }catch(e){}
	  return "";
  }

	