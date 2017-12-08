var bannerHeight = 58;
var winWidth = 0;
var winHeight = 0;
var options = {
			speed: 'fast',   // slow  fast
//			event: 'click',
			effect: 'fade'  // slide  fade  注意最后一行不能有逗号 IE下不解析
	};
$(document).ready(function($){
	$('#mega-menu').dcMegaMenu(options,'0'); //'0'代表resize的标识 ,默认为0
});
// fade slide create tabs 要综合考虑，动态创建icon的css，不能在title上加入image的路径，不然后面关闭等事件不好用。
// 135是tabs 以及菜单条的高度  58 是logo的高度  tabName没有trim()方法
// if(icon == '' || icon == null)来判断是否显示当前的图片，没有则不显示，为的是流程以及搜索中不给该参数赋值，并且不显示。
/*获取高度值*/
function getHeight(){
   var rightHeight = document.body.clientHeight - 60;
   var Sys = {};
  var ua = navigator.userAgent.toLowerCase();
  try{
      var s;
			(s = ua.match(/msie ([\d.]+)/)) ? Sys.ie = s[1] :
			(s = ua.match(/firefox\/([\d.]+)/)) ? Sys.firefox = s[1] :
			(s = ua.match(/chrome\/([\d.]+)/)) ? Sys.chrome = s[1] :
			(s = ua.match(/opera.([\d.]+)/)) ? Sys.opera = s[1] :
			(s = ua.match(/version\/([\d.]+).*safari/)) ? Sys.safari = s[1] : 0;
	     
	     if(Sys.ie) rightHeight = document.body.clientHeight - 60;
	     if(Sys.firefox) rightHeight = document.body.clientHeight - 65;
	     if(Sys.chrome) rightHeight = document.body.clientHeight - 60;
	     if(Sys.opera) rightHeight = document.body.clientHeight - 207;
	     if(Sys.safari) rightHeight = document.body.clientHeight - 65;
	     //判断IE版本
	     if(Sys.ie && ua.indexOf('msie 8.0') > 1){
	     		rightHeight - 60;
	     }
  }catch(e){}
  return rightHeight;
}

function addTab(tabName,targetUrl,icon,menuCode,iconPosition){
	var initIframeHeight ;
	if($("#divBodyWidth").css("display") == "block"){
		initIframeHeight = getHeight() - bannerHeight;
	}else{
		initIframeHeight = getHeight();
	}
	var closeFlag = true;
	if(menuCode == "homePage"||menuCode == "portal"){
		closeFlag = false;
	}
	var randomIcon = parseInt(Math.random() * 100000);
	if ($('#tabs').tabs('exists',tabName)){
				$('#tabs').tabs('select', tabName);
				$("." + tabName).attr('src',targetUrl);
				closeMegaDrop();
		} else {
				if(targetUrl.indexOf('javascript') == -1 && targetUrl != ''){//判断当前的targetUrl是url时添加新的tab
					var currentTab ;
					if(icon == '' || icon == null){
						currentTab = $('#tabs').tabs('add',{
							title:tabName,
							content:"<iframe id=iframeBody class='" + tabName  + "' title='" + menuCode +"' style='overflow-y:auto;overflow-x:hidden;height:"+initIframeHeight+"px;width:100%;' src="+targetUrl+"  frameborder=0 ></iframe>",
							closable:closeFlag,
							onclick:closeMegaDrop()
						});
					}else{
						currentTab = $('#tabs').tabs('add',{
							title:tabName,
							iconCls : "_" + randomIcon+'tabIcon',
							content:"<iframe id=iframeBody class='" + tabName  + "' title='" + menuCode +"' style='overflow-y:auto;overflow-x:hidden;height:"+initIframeHeight+"px;width:100%;' src="+targetUrl+"  frameborder=0 ></iframe>",
							closable:closeFlag,
							onclick:closeMegaDrop()
						});
						$("._" + randomIcon + "tabIcon").css("background","url('" + icon + "') no-repeat").css("background-position",iconPosition);
					}
					
				}
		}
	if(menuCode != ''){
		$(".portlet").css("display","none");//非homePage,隐藏portlet设置
	} else {
		$(".portlet").css("display","");//homePage,显示portlet设置
	}
}
// 关闭当前megaDrop div的。
function closeMegaDrop(){
	$(".sub").hide(); 
	var $parentLi = $(".sub").parent().parent();
	if($parentLi.hasClass('mega-hover')){
		$parentLi.removeClass('mega-hover');
	}
}

function changeSkins(className){
	var skins = document.getElementById('skins');
	skins.href = '../themes/' + className + '/easyui.css'
}


function changeSkins(className){
	var skins = document.getElementById('skins');
	skins.href = '../themes/' + className + '/easyui.css'
}

$(function(){   
	//刷新    
	$("#m-refresh").click(function(){
		var currTab = $('#tabs').tabs('getSelected'); 
		//获取选中的标签项      
		var url = $(currTab.panel('options').content).attr('src');  
		var tabName = $(currTab.panel('options').content)[0].className; 
		$("." + tabName).attr('src',url);
    });
	//关闭所有
	 $("#m-closeall").click(function(){
		 $(".tabs li").each(function(i, n){ 
			 var title = $(n).text(); 
			 if(title != homePage){
				 $('#tabs').tabs('close',title);
			 }
			 });  
		 });
	//除当前之外关闭所有
	 $("#m-closeother").click(function(){      
		 var currTab = $('#tabs').tabs('getSelected');     
		 currTitle = currTab.panel('options').title;   
		 $(".tabs li").each(function(i, n){    
			 var title = $(n).text(); 
			 if(currTitle != title && title != homePage){ 
				 $('#tabs').tabs('close',title);   
			 }    
		});   
	});
	 //关闭当前
    $("#m-close").click(function(){     
		var currTab = $('#tabs').tabs('getSelected');   
		currTitle = currTab.panel('options').title; 
		if(currTitle != homePage){
			$('#tabs').tabs('close', currTitle);
		}
	}); 
    // tabs 标签页切换,隐藏/显fi
    $('#tabs').tabs({
		onSelect : function(title){
//			$('#tabs .tabs-header .tabs-wrap .tabs').css("width","100%");
			if(title == homePage){
				$(".portlet").css("display","");
			} else {
				$(".portlet").css("display","none");
			}
			return;
		}
    });
    
});

// 动态的与tab所选页签绑定。控制显示的权限
$(function(){     
	/*为选项卡绑定右键*/   
	$(".tabs li").live('contextmenu',function(e){  
		/* 选中当前触发事件的选项卡 */  
		var subtitle =$(this).text(); 
		var tabcount = $('#tabs').tabs('tabs').length;
		if(tabcount == 1){
			$("#m-closeother").attr({"disabled":"disabled"}).css({ "cursor": "default", "opacity": "0.4" }); 
			$("#m-closeall").attr({"disabled":"disabled"}).css({ "cursor": "default", "opacity": "0.4" }); 
			$("#m-close").attr({"disabled":"disabled"}).css({ "cursor": "default", "opacity": "0.4" }); 
		}else{
			if(tabcount == 2){
				$("#m-closeall").remove("disabled");
				$("#m-closeall").removeAttr("style");
				if(subtitle == homePage){
					if(typeof($("#m-close").attr("disabled"))== "undefined"){
						$("#m-close").attr({"disabled":"disabled"}).css({ "cursor": "default", "opacity": "0.4" });
					}
					$("#m-closeother").remove("disabled");
					$("#m-closeother").removeAttr("style");
				}else{
					$("#m-close").remove("disabled");
					$("#m-close").removeAttr("style");
					if(typeof($("#m-closeother").attr("disabled")) == 'undefined'){
						$("#m-closeother").attr({"disabled":"disabled"}).css({ "cursor": "default", "opacity": "0.4" });
					}
				}
			}else{
				$("#m-closeother").remove("disabled");
				$("#m-closeall").remove("disabled");
				$("#m-close").remove("disabled");
				$("#m-closeall").removeAttr("style");
				$("#m-close").removeAttr("style");
				$("#m-closeother").removeAttr("style");
				if(subtitle == homePage){
					if(typeof($("#m-close").attr("disabled"))== "undefined"){
						$("#m-close").attr({"disabled":"disabled"}).css({ "cursor": "default", "opacity": "0.4" });
					}
				}
			}
		}
		$('#tabs').tabs('select',subtitle);   
		//显示快捷菜单      
		$('#context_menu').menu('show', {    
	       left: e.pageX,          
	       top: e.pageY        
       });               
		return false;  
	});
	$("#navPopInfo .bdBox").hide();
});

// 把banner显示与隐藏
$(function(){    
	$('#arrowUpAndDown').click(function(){
		var isShow = $('#arrowUpAndDown').hasClass('arrowUp');
	    if(isShow == false){
		   $("#arrowUpAndDown").removeClass('arrowDown');
		   $("#arrowUpAndDown").addClass('arrowUp');
		   $("#divBodyWidth").show();
		   recoverWindowCallaspe();
	    }else{
		   $("#arrowUpAndDown").removeClass('arrowUp');
		   $("#arrowUpAndDown").addClass('arrowDown');
		   $("#divBodyWidth").hide();
		   reSizeWindowCallaspe();
	    }
	   
	   });
	});


// 注销
function logout() {
	if(confirm(logoutTip)) {
		window.location = baseUrl+'platform/user/logout';
	} 
}

function showPersionConfig(){
	$("#navPopInfo .bdBox").show();
}

function openSubWindow_pwd(){
	$('#modify_dialog').show();
	$('#modify_dialog').dialog();
	$('div[id=modify_dialog]').html($("<iframe name='applyFrame' id='applyFrame' scrolling='yes' frameborder='0'  src='"+baseUrl+"platform/syspassword/sysPasswordController/toPassword' style='width:100%;height:91%;'></iframe>"));
	$('#modify_dialog').dialog('open');
}

function showVersion(){	
	$('#versionDialog').show();
	$('#versionDialog').dialog();
	$('div[id=versionDialog]').html($("<iframe name='applyFrame' id='applyFrame' scrolling='yes' frameborder='0'  src='"+baseUrl+"platform/systemversion/toVesionAlert' style='width:100%;height:100%;'></iframe>"));
	$('#versionDialog').dialog('open');
}

$(function(){    
$('#navPopInfo').hover(function(){
	$("#navPopInfo .bdBox").show();
   },
function(){
	$("#navPopInfo .bdBox").hide();
   });
});

function reSizeWindow(){
	   //获取窗口宽度
	   if (window.innerWidth){
		   winWidth = window.innerWidth;
	   } else {
		   if ((document.body) && (document.body.clientWidth)){
			   winWidth = document.body.clientWidth;
		   }
	   }
	   //获取窗口高度
	   if (window.innerHeight){
		   winHeight = window.innerHeight;
	   } else {
		   if ((document.body) && (document.body.clientHeight)){}
		   winHeight = document.body.clientHeight;
	   }
	   //通过深入Document内部对body进行检测，获取窗口大小
	   if (document.documentElement  && document.documentElement.clientHeight && document.documentElement.clientWidth){
		   winHeight = document.documentElement.clientHeight;
		   winWidth = document.documentElement.clientWidth;
	   }
	   
	   $("#tabs").height(winHeight-90);
       //  $("#iframeBody").height(winHeight-100);
	   //  必须采用该方法，解决当浏览器变化之后，iframe以及tab高度自适应。由于iframebody的id都一致，所以不能通过id来实现。
	   //  用className来实现。遍历全部
	   $(".tabs li").each(function(i, n){ 
			 var title = $(n).text(); 
			 $('.'+title).height(winHeight-100);  
			 $('.'+title).width(winWidth); 
			 // 结合easyui中的样式，需要把panel-body panel-body-noheader中的宽度也要实现自适应的,当小窗口打开时，再最大化，可以自适应。
			 $('.panel-body-noborder').width(winWidth);
		 });
	   $("#divBodyWidth").width(winWidth);
	   $("#tabs").width(winWidth);
	   $(".tabs-wrap").width(winWidth);
}



function reSizeWindowAfter(){
	   //获取窗口宽度
	   if (window.innerWidth){
		   winWidth = window.innerWidth;
	   } else {
		   if ((document.body) && (document.body.clientWidth)){
			   winWidth = document.body.clientWidth;
		   }
	   }
	   //获取窗口高度
	   if (window.innerHeight){
		   winHeight = window.innerHeight;
	   } else {
		   if ((document.body) && (document.body.clientHeight)){}
		   winHeight = document.body.clientHeight;
	   }
	   //通过深入Document内部对body进行检测，获取窗口大小
	   if (document.documentElement  && document.documentElement.clientHeight && document.documentElement.clientWidth){
		   winHeight = document.documentElement.clientHeight;
		   winWidth = document.documentElement.clientWidth;
	   }
	   try{
		   $.fn.dcMegaMenu(options,'1'); //'1',resize窗口的标识为真
	   }catch(e){
		   alert(e);
	   }
	   
	   $("#tabs").height(winHeight-90);
	   //  必须采用该方法，解决当浏览器变化之后，iframe以及tab高度自适应。由于iframebody的id都一致，所以不能通过id来实现。
	   //  用className来实现。遍历全部
	   $(".tabs li").each(function(i, n){ 
			 var title = $(n).text(); 
			 $('.'+title).height(winHeight-100);  
			 $('.'+title).width(winWidth); 
			 // 结合easyui中的样式，需要把panel-body panel-body-noheader中的宽度也要实现自适应的,当小窗口打开时，再最大化，可以自适应。
			 $('.panel-body-noborder').width(winWidth);
		 });
	   $("#divBodyWidth").width(winWidth);
	   $("#tabs").width(winWidth);
	   $(".tabs-wrap").width(winWidth);
	   resizeSearchDiv(winWidth-180);
}

function recoverWindowCallaspe(){
	   resizeSearchDiv();
	   $("#tabs").height($("#tabs").height()-bannerHeight);
	   $(".tabs li").each(function(i, n){ 
			 var title = $(n).text(); 
			 $('.'+title).height($('.'+title).height()-bannerHeight);   
		 });
}

// 参数58为图logo的高度，当logo发生变化时，还需要把高度的值传过来
function reSizeWindowCallaspe(){
	resizeSearchDiv();
	   $("#tabs").height($("#tabs").height()+bannerHeight);
	   $(".tabs li").each(function(i, n){ 
			 var title = $(n).text(); 
			 $('.'+title).height($('.'+title).height()+bannerHeight);   
		 });
}


$(function(){
	 //调用函数，获取数值
	if(messageCount > 0){
		$(".msgbg").css("display","block");
	}
	reSizeWindow();
	window.onresize=reSizeWindowAfter;
});

//全文搜索的focus与blur事件
function onFocusEvent(obj){
    if(obj.value==onFocusTip){
            obj.value="";
     }
  }
function onBlurEvent(obj){
    if(obj.value==""){
      obj.value=onFocusTip;
    }
}

//10S刷新消息一次，10000
$(document).ready(function(){  
	if(typeof messageIntervalTime == 'undefined' || messageIntervalTime == null){
		messageIntervalTime = 30000;
	}
	refreshMessageCount();
   setInterval(function(){
	   refreshMessageCount();
   }, messageIntervalTime);
});  

function gcPlatform(){
	if($.browser.msie){
		 CollectGarbage();
   }
}


// 敏捷搜索框用到的方法
function resizeSearchDiv(searchBoxLeftPositionValue){
	var serachLeftPosition;
	var searchBox = $("#subjectSearch");
	if(searchBox.position()!=null){
		if(typeof(searchBoxLeftPositionValue) == 'undefined'){
			serachLeftPosition = searchBox.position().left;
		}else{
			serachLeftPosition = searchBoxLeftPositionValue;
		}
		var top = searchBox.position().top + searchBox.height();
		var left = serachLeftPosition  + searchBox.width() - $(".agileSearchResults").width() ;
		// 显示banner 58 0
		if($("#divBodyWidth").css("display") == "block"){
			$(".agileSearchResults").css("top", top+bannerHeight);
			$(".agileSearchResults").height($(".agileSearchResults").height()-bannerHeight);
			$(".agileSearchResults").css("left", left);
		}else{
			$(".agileSearchResults").css("top", top);
			$(".agileSearchResults").height($(".agileSearchResults").height());
			$(".agileSearchResults").css("left", left);
		}
	}
}

function refreshMessageCount(){
	$.ajax({
	     type: "GET",//使用GET方法访问后台
	     url: baseUrl+"platform/sysmessage/sysMessageController/getSysMessageCount?_dc="+new Date().getTime(),//要访问的后台地址
	     async : false,
	     success: function(msg){//msg为返回的数据，在这里做数据绑定
	    	if(parseInt(msg) > 0){
	    		$(".msgbg").css("display","block");
	    		$(".msgbg").find("a").text(msg);
//	    		$(".msgbg").find("a").get(0).innerText= msg;  这两种方法都可以
	    	}else{
	    		$(".msgbg").css("display","none");
	    	}
	     }, 
	     error : function(msg){
		   }
	   });
}
/**
 * 保存选择的portlet
 */
function savePortlet(){
	$('#portletConfigIframe')[0].contentWindow.$saveConfigPersonPortlet();
}
/**
 * 恢复出厂设置
 */
function restDefault(){
	if(confirm(resetPorletTip)) {
		$.ajax({
		    url : baseUrl + "platform/portlet/restDefault",
		    method : "GET",
		    async: false,
		    success: function(msg){
		    	
		   }
		});
		refreshPortlet($("#iframeBody")[0].contentWindow.flag);
    }
	$("#menu").hide();
}
/**
 * 设置全局portlet
 */
function setGlobalPortlet(){
	$("#iframeBody").attr('src',baseUrl + 'platform/portlet/getContent?flag=global');
	$("#menu").hide();
	return false;
}
/**
 * 保存选择的布局
 */
function saveLayout(){
	var checkedValue = '';
	var radios = $('#portletConfigIframe')[0].contentWindow.document.getElementsByName('layout');
	for(i=0;i<radios.length;i++){
		if(radios[i].checked){
			checkedValue = radios[i].value;
		}
	}
	$.ajax({
	    url : baseUrl + "platform/portlet/saveSelectLayout?flag=" + $('#portletConfigIframe')[0].contentWindow.flag,
	    method : "GET",
	    dataType : "text",
	    async: false,
		data : "layoutValue=" + checkedValue,
	    success: function(msg){
	    	
	   }
	});
	hideDialog();
	refreshPortlet();
}
function saveLayout1(){
	var frm = $('#portletConfigIframe')[0].contentWindow;
	var configResultXml = frm.saveConfigResultXml();
	//获取当前操作的数据行记录
	alert('布局设置成功!')
	hideDialog();
	refreshPortlet();
}
function hideDialog(){
	$.mask("close");
    $("#overlay").hide();
    $("#save").unbind("click");
    $("#dialog").fadeOut(100);
}
function refreshPortlet(flag){
	if(typeof(flag) == 'undefined'){
		flag = '';
	}
	$("#iframeBody").attr('src',baseUrl + 'platform/portlet/getContent?flag=' + flag);
	return false;
}
//设置portlet
function setPortlet(){
	$('#setPortlet_dialog').dialog('open');
	return false;
}
function showDialog(modal,title,w,h,iframeSrc,saveClickEvent){
	$.mask();
    $("#overlay").show();
    //指定title
    if(typeof(title) != 'undefined'){
    	$(".web_dialog_title span").text(title);
    }
    if(typeof(title) != 'saveClickEvent'){
    	$("#save").bind("click",function(){
    		eval(saveClickEvent);
    	});
    }
    var left = (screen.availWidth - w -30) / 2;
	var top = (screen.availHeight - h - 30) / 2;
	$("#dialog").css({"width": w,"height": h,top: top,left: left});
	$("#dialog #context").css({"height": h - 75 });
	//设置显示内容
	if(typeof(iframeSrc) != 'undefined'){
		var flag = $("#iframeBody")[0].contentWindow.flag;
		$("#dialog #context").html("<iframe id='portletConfigIframe' name='portletConfigIframe' src='" + iframeSrc + "' width='100%' height='" + (h - 75) + "px' frameborder='0' marginheight='0' marginwidth='0' scrolling='auto'></iframe>")
	} else {
		$("#dialog #context").html('<span></span>');
	}
    $("#dialog").fadeIn(300);
    if(modal){
       $("#overlay").unbind("click");
    }else{
       $("#overlay").click(function (e){
          hideDialog();
       });
    }
    var obj = document.getElementById('dialog');
    rDrag.init(obj);
    $("#menu").hide();
 }
/**
 * dialog 拖动
 */
var rDrag = {
		 o:null,
		 init:function(o){
			 o.onmousedown = this.start;
		 },
		 start:function(e){
		  var o;
		  e = rDrag.fixEvent(e);
		               e.preventDefault && e.preventDefault();
		               rDrag.o = o = this;
		  o.x = e.clientX - rDrag.o.offsetLeft;
		                o.y = e.clientY - rDrag.o.offsetTop;
		  document.onmousemove = rDrag.move;
		  document.onmouseup = rDrag.end;
		 },
		 move:function(e){
		  e = rDrag.fixEvent(e);
		  var oLeft,oTop;
		  oLeft = e.clientX - rDrag.o.x;
		  oTop = e.clientY - rDrag.o.y;
		  rDrag.o.style.left = oLeft + 'px';
		  rDrag.o.style.top = oTop + 'px';
		 },
		 end:function(e){
		  e = rDrag.fixEvent(e);
		  rDrag.o = document.onmousemove = document.onmouseup = null;
		 },
		 fixEvent: function(e){
		        if (!e) {
		            e = window.event;
		            e.target = e.srcElement;
		            e.layerX = e.offsetX;
		            e.layerY = e.offsetY;
		        }
		        return e;
		    }
		}
/**
 * 初始化portlet icon
 * liub
 */
$(function(){
	setTimeout(function(){
//		$('#tabs .tabs-header .tabs-wrap').css("width","100%");
		$('#tabs .tabs-header .tabs-wrap .tabs').css("float","left");
		$('#tabs .tabs-header .tabs-wrap').append("<div class='portlet'></div>");
		$(".portlet").click(function(){
             $("#menu").show();
        });
		$("#portlet").bind('mouseout',function(){
			setTimeout(function(){$("#menu").hide();},800);
		});
		$("#tooltip_menu").bind('mouseenter',function(){
			$("#portlet").unbind('mouseout');
		}).bind('mouseleave',function(){
			$("#menu").hide();
		});
	},500);
	// 设置鼠标经过样式
	$("#menu #tooltip_menu a").mouseover(function(){
		$(this).addClass('mouseover');
	}).mouseout(function(){
		$(this).removeClass('mouseover');
	});
	var maskStackCount = 0;//遮罩
	$.mask = function(method){
		if(typeof method == "undefined"){
			method="open";
		}
		if (method == "open") {
			if (maskStackCount == 0) {
				var mask = $("<div id='window-mask' class='window-mask' style='display:none'></div>").appendTo("body");
				mask.css({
					width: $(window).width() + "px",
					height: $(window).height() + "px"
				}).fadeIn(function(){
					$(this).css("filter","alpha(opacity=60)");
				});
				$(window).bind("resize.mask", function(){
					mask.css({
						width: $(window).width() + "px",
						height: $(window).height() + "px"
					});
				});
			}
			maskStackCount++;
		}else if(method == "close"){
			maskStackCount--;
			if(maskStackCount == 0){
				$("#window-mask").fadeOut(function(){
					$("#window-mask").remove();
				});
				$(window).unbind("resize.mask");
			}
		}
		
	 };
     $("#btnClose").click(function (e){
		 $.mask("close");
         hideDialog();
         e.preventDefault();
     });
});