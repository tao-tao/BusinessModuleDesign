var bannerHeight = 45;
var winWidth = 0;
var winHeight = 0;
var options = {
			speed: 'fast',   // slow  fast
			effect: 'fade',  // slide  fade
//			event: 'click',
			fullWidth: false  // true false
	};
$(document).ready(function($){
	$('#mega-menu').dcMegaMenu(options);
});

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


// fade slide create tabs 要综合考虑，动态创建icon的css，不能在title上加入image的路径，不然后面关闭等事件不好用。
function addTab(tabName,targetUrl,icon,menuCode,iconPosition){
	var initIframeHeight ;
	if($("#divBodyWidth").css("display") == "block"){
		initIframeHeight = getHeight()-bannerHeight;
	}else{
		initIframeHeight = getHeight();
	}
	var closeFlag = true;
	if(tabName == "系统首页"){
		closeFlag = false
	}
	var randomIcon = parseInt(Math.random() * 100000);
	if ($('#tabs').tabs('exists',tabName)){
				$('#tabs').tabs('select', tabName);
				$("." + tabName).attr('src',targetUrl);
				closeMegaDrop();
		} else {
				if(targetUrl != '' && targetUrl.indexOf('javascript') == -1){//判断当前的targetUrl是url时添加新的tab
					var currentTab = $('#tabs').tabs('add',{
						title:tabName,
						iconCls : "_" + randomIcon+'tabIcon',
						content:"<iframe id=iframeBody class='" + tabName  + "' style='overflow:hidden; height:"+initIframeHeight+"px;width:100%' src="+targetUrl+"  frameborder=0></iframe>",
						closable:closeFlag,
						onclick:closeMegaDrop()
					});
					$("._" + randomIcon + "tabIcon").css("background","url('" + icon + "') no-repeat").css("background-position",iconPosition);
				}
		}
}
// 关闭当前megaDrop div的。
function closeMegaDrop(){
	$(".sub").hide();
	$(".neck").hide(); 
	var $parentLi = $(".sub").parent().parent().parent();
	if($parentLi.hasClass('mega-hover')){
		$parentLi.removeClass('mega-hover');
	}
	if($(".supportObjectButtom").length>0){
		$(".supportObjectButtom").remove();
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

//目前还不支持动态变化
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
			if(title == homePage){
				$(".portlet").css("display","");
			} else {
				$(".portlet").css("display","none");
			}
			return;
		}
    });
    
});

//动态的与tab所选页签绑定。控制显示的权限
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
		if($('#m-refresh').find("iframe").length>0){
		}else{
			$('#m-refresh').after("<iframe class='supportObjectButtom' frameborder='no' style='*width:"+($('#context_menu').width()+100)+"px;*height:"+($('#context_menu').height()+100)+"px;'> </iframe>");
		}
		return false;  
	});
	$("#navPopInfo .bdBox").hide();
});

// 把banner显示与隐藏
$(function(){    
	$('#arrowUpAndDown').click(function(){
		   var image = ($("#arrowUpAndDown").attr("src")).indexOf("arrowup01.gif");
		   if(image == -1){
			   $("#arrowUpAndDown").attr("src","static/css/platform/themes/default/index/style/images/arrowup01.gif");
			   $("#divBodyWidth").show();
			   recoverWindowCallaspe();
		   }else{
			   $("#arrowUpAndDown").attr("src","static/css/platform/themes/default/index/style/images/arrowdown01.gif");
			   $("#divBodyWidth").hide();
			   reSizeWindowCallaspe();
		   }
		   
	   });
	});

function recoverWindowCallaspe(){
	$('.tabs-panels').height(($('.tabs-panels').height())-bannerHeight);
	$('.panel-body-noborder').height(($('.panel-body-noborder').height())-bannerHeight);
	   $("#tabs").height($("#tabs").height()-bannerHeight);
	   $(".tabs li").each(function(i, n){ 
			 var title = $(n).text(); 
			 $('.'+title).height($('.'+title).height()-bannerHeight);   
		 });
}

//参数58为图logo的高度，当logo发生变化时，还需要把高度的值传过来  tabs-panels
function reSizeWindowCallaspe(){
	$('.tabs-panels').height(($('.tabs-panels').height())+bannerHeight);
	$('.panel-body-noborder').height(($('.panel-body-noborder').height())+bannerHeight);
	   $("#tabs").height($("#tabs").height()+bannerHeight);
	   $(".tabs li").each(function(i, n){ 
			 var title = $(n).text(); 
			 $('.'+title).height($('.'+title).height()+bannerHeight);   
		 });
}

// 注销
function logout() {
	if(confirm('您真的要注消系统吗?')) {
		//location.href=baseUrl+'logout';
		window.open(baseUrl+'logout' ,'_self');
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

$(function(){    
	$('#navPopInfo').hover(function(){
		$("#navPopInfo .bdBox").show();
	 },
	function(){
		$("#navPopInfo .bdBox").hide();
	 });
});
// reSizeWindow与$('#mega-menu').dcMegaMenu(options);共同来控制带脖子的div的生成与销毁
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
	   try{
		   $.fn.dcMegaMenu(options);
	   }catch(e){
		   alert(e);
	   }
	   $("#tabs").height(winHeight-70);
	   $(".tabs li").each(function(i, n){ 
			 var title = $(n).text(); 
			 $('.'+title).height(winHeight-120);  
			 $('.'+title).width(winWidth); 
			 // 结合easyui中的样式，需要把panel-body panel-body-noheader中的宽度也要实现自适应的,当小窗口打开时，再最大化，可以自适应。
			 $('.panel-body-noborder').width(winWidth);
		 });
	   $("#divBodyWidth").width(winWidth);
	   $("#tabs").width(winWidth);
	   $(".tabs-panels").width(winWidth);
	   $(".panel").width(winWidth);
	   $(".tabs-wrap").width(winWidth);
}

// reSizeWindowAfter只是控制其它窗口的改变，不包括 $.fn.dcMegaMenu(options);
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
	   $("#tabs").height(winHeight-70);
	   $(".tabs li").each(function(i, n){ 
			 var title = $(n).text(); 
			 $('.'+title).height(winHeight-120);  
			 $('.'+title).width(winWidth); 
			 // 结合easyui中的样式，需要把panel-body panel-body-noheader中的宽度也要实现自适应的,当小窗口打开时，再最大化，可以自适应。
			 $('.panel-body-noborder').width(winWidth);
		 });
	   $("#divBodyWidth").width(winWidth);
	   $("#tabs").width(winWidth);
	   $(".tabs-panels").width(winWidth);
	   $(".panel").width(winWidth);
	   $(".tabs-wrap").width(winWidth);
}

$(function(){
	if(messageCount > 0){
		$(".msgbg").css("display","block");
	}
	 //调用函数，获取数值
	reSizeWindow();
	window.onresize=reSizeWindowAfter;
});

// 全文搜索的focus与blur事件
function onFocusEvent(obj){
    if(obj.value=="全文搜索..."){
            obj.value="";
     }
  }
function onBlurEvent(obj){
    if(obj.value==""){
      obj.value="全文搜索...";
    }
}
// messageIntervalTime秒新消息一次，从配置文件中获取
$(document).ready(function(){    
	   setInterval(function(){
		   refreshMessageCount();
	   }, messageIntervalTime);
});

function gcPlatform(){
	if($.browser.msie){
		 CollectGarbage();
   }
}
function refreshMessageCount(){
	$.ajax({
	     type: "GET",//使用GET方法访问后台
	     url: baseUrl+"platform/modules/system/sysmessage/action/getSysMessageCount",//要访问的后台地址
	     async: false,
	     success: function(msg){//msg为返回的数据，在这里做数据绑定
	    	if(parseInt(msg) > 0){
	    		$(".msgbg").css("display","block");
	    		$(".msgbg").find("a").text(msg);
	    	}else{
	    		$(".msgbg").css("display","none");
	    	}
	     }, 
	     error : function(msg){
		   }
	   });
}

//全文搜索的focus与blur事件
function onFocusEvent(obj){
    if(obj.value=="请输入检索内容..."){
            obj.value="";
     }
  }
function onBlurEvent(obj){
    if(obj.value==""){
      obj.value="请输入检索内容...";
    }
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