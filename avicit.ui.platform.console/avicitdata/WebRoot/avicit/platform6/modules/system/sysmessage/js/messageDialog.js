 var winCurrHeight=0;
 var hidTimer=null;
 var doradoFlag;
/* var currBaseUrl="";*/
 var move=false; 
       var Message = {
            set: function() {//最小化与恢复状态切换
                var sets= this.minbtn.status == 1 ? [0, 1, 'block', this.char[0], '最小化'] : [1, 0, 'none', this.char[1], '恢复'];
                this.minbtn.status = sets[0];
                this.win.style.borderBottomWidth = sets[1];
                this.content.style.display = sets[2];
                if(sets[0]=='1'){
                   this.win.style.height = "25px";
                }else{
               	   this.win.style.height =winCurrHeight;
                }
                this.minbtn.innerHTML = sets[3]
                this.minbtn.title = sets[4];
                this.win.style.top = this.getY().top;
            },
            close: function() {//关闭
                this.win.style.display = 'none';
                window.onscroll = null;
            },
            setOpacity: function(x) {//设置透明度
                var v = x >= 100 ? '' : 'Alpha(opacity=' + x + ')';
                this.win.style.visibility = x <= 0 ? 'hidden' : 'visible'; //IE有绝对或相对定位内容不随父透明度变化的bug
                this.win.style.filter = v;
                this.win.style.opacity = x / 100;
            },
            show: function() {//渐显
                clearInterval(this.timer2);
                var me = this, fx = this.fx(0, 100, 0.1), t = 0;
                this.timer2 = setInterval(function() {
                    t = fx();
                    me.setOpacity(t[0]);
                    if (t[1] == 0) { clearInterval(me.timer2) }
                }, 10);
            },
            fx: function(a, b, c) {//缓冲计算
                var cMath = Math[(a - b) > 0 ? "floor" : "ceil"], c = c || 0.1;
                return function() { return [a += cMath((b - a) * c), a - b] }
            },
            getY: function() {//计算移动坐标
                var d = document, b = document.body, e = document.documentElement;
                var s = Math.max(b.scrollTop, e.scrollTop);
                var h = /BackCompat/i.test(document.compatMode) ? b.clientHeight : e.clientHeight;
                var h2 = this.win.offsetHeight;
                return { foot: s + h + h2 + 2 + 'px', top: s + h - h2 - 2 + 'px' }
            },
            moveTo: function(y) {//移动动画
                clearInterval(this.timer);
                var me = this, a = parseInt(this.win.style.top) || 0;
                var fx = this.fx(a, parseInt(y));
                var t = 0;
                this.timer = setInterval(function() {
                    t = fx();
                    me.win.style.top = t[0] + 'px';
                    if (t[1] == 0) {
                        clearInterval(me.timer);
                        me.bind();
                    }
                }, 10);
            },
            bind: function() {//绑定窗口滚动条与大小变化事件
                var me = this, st, rt;
                window.onscroll = function() {
                    clearTimeout(st);
                    clearTimeout(me.timer2);
                    me.setOpacity(0);
                    st = setTimeout(function() {
                        me.win.style.top = me.getY().top;
                        me.show();
                    }, 6600);
                };
                window.onresize = function() {
                    clearTimeout(rt);
                    rt = setTimeout(function() { me.win.style.top = me.getY().top }, 100);
                }
            },
            init: function(sysMessageId,currBaseUrl) {//创建HTML 弹出窗口
                function $(id) { return document.getElementById(id) };
                this.win = document.createElement('div');
                //this.win.style.width ='380px';
                // this.win.style.height ='170px';
                winCurrHeight="185px";
                this.win.id = "msg_win";
                var unreadedIcon=currBaseUrl+"avicit/platform6/modules/system/sysmessage/icon/letter_readed.gif";
                this.win.innerHTML = '<div class="icos"><a style="border:none;" href="javascript:void(0)"  id="msg_readed" >'+
                	   '<img id="imageMessageButton" src="'+unreadedIcon+'" title="标记为已读" onclick="updateMessageStatus(\''+sysMessageId+'\',\'1\','+doradoFlag+',\''+currBaseUrl+'\')" style="border:0;margin-top:2px;"/></a>'+
                	   '<a href="javascript:void(0)" title="最小化" id="msg_min">0</a><a href="javascript:void(0)" title="关闭" id="msg_close">r</a></div>'+
                	   '<div id="msg_title" style="background: #62B4D8 url('+currBaseUrl+'avicit/platform6/modules/system/sysmessage/icon/dialogTitle.gif) no-repeat 0 center;">温馨提示</div><div id="msg_content"></div>';
                document.body.appendChild(this.win);
                var set = { minbtn: 'msg_min', closebtn: 'msg_close', title: 'msg_title', content: 'msg_content' };
                for (var Id in set) { this[Id] = $(set[Id]) };
                var me = this;
                this.minbtn.onclick = function() { me.set(); this.blur() };
                this.closebtn.onclick = function() { me.close() };
                this.char = navigator.userAgent.toLowerCase().indexOf('firefox') + 1 ? ['_', '::', '×'] : ['0', '2', 'r']; //FF不支持webdings字体
                this.minbtn.innerHTML = this.char[0];
                this.closebtn.innerHTML = this.char[2];
                setTimeout(function() {//初始化最先位置
                    me.win.style.display = 'block';
                    me.win.style.top = me.getY().foot;
                    me.moveTo(me.getY().top);
                }, 500);
                return this;
            }
        };
/**
 * 修改当前消息的状态 
 * @param currBaseUrl      该值从在调用页面用 avicit.platform6.commons.utils.ViewUtil.getRequestPath(request)获得
 * @param sysMessageId     待修改消息的Id
 */
function updateMessageStatus(sysMessageId,sysMessageStatus,doradoFlag,currBaseUrl){
	$.ajax({
	     type: "POST",//使用post方法访问后台
	     dataType: "json",//返回json格式的数据
	     url: currBaseUrl+"platform/modules/system/sysmessage/action/updateSysMessageReadStatus.json",//要访问的后台地址
	     data:"sysMessageId="+sysMessageId+"&sysMessageStatus="+sysMessageStatus,
	     async : false,
	     complete :function(){},
	     success: function(msg){//msg为返回的数据，在这里做数据绑定
	    	 var currImageMessageButton=document.getElementById("imageMessageButton");
	    	 currImageMessageButton.src=currBaseUrl+"avicit/platform6/modules/system/sysmessage/icon/letter_open.gif";
	    	 currImageMessageButton.title="消息已经标记为已读,将不在进行提醒!";
	    	 currImageMessageButton.onclick=function(){
	    	 }
	    	 var msgPop=document.getElementById("msg_win");
    		 msgPop.style.top="690px";
	     }
	   });
	if(doradoFlag){
		
	}else{
		refreshMessageCount();
	}
}

/**
 * 初始化右下角弹出窗口 读取未阅读消息 进行提示  
 * @param baseUrl            该值从在调用页面用 avicit.platform6.commons.utils.ViewUtil.getRequestPath(request)获得
 * @param refreshFrequency   定时检测未读消息的频率  该值在配置管理里面进行设置  key为PLATFORM_V6_MESSAGE_REQUEST_INTERVAL
 * @param isDorado           调用页面是不是dorado7开发的页面 是-true 不是-false
 */
function loadMessage(baseUrl,refreshFrequency,isDorado){
	if(typeof(doradoFlag)=='undefined'){
		doradoFlag = true;
	}
	//循环执行
	var timeId=0;
	try{
		timeId=setInterval(function(){
			 $.ajax({
			     type: "POST",//使用post方法访问后台
			     dataType: "json",//返回json格式的数据
			     async : false,
			     url: baseUrl+"platform/modules/system/sysmessage/action/getLatestMessageByReceiveUserId.json",//要访问的后台地址
			     error:function(XMLHttpRequest,textStatus,errorThrown){
			    	 if(textStatus=="error"){
			    		 clearInterval(timeId);
			    		 var errorTimer=setTimeout("loadMessage('"+baseUrl+"',"+refreshFrequency+","+isDorado+")",refreshFrequency*10);
			    	 }
			     },
			     success: function(msg){//msg为返回的数据，在这里做数据绑定
			    	 $.each(msg, function(i,entity){
			    		if(i=="sysMessage" && entity!=null){
			    			//右下角弹出窗口显示内容拼装从此开始
			    			var titile=entity.title;  //取得消息的标题  格式化标题 标题在弹出框的最多占用两行位置  超出将被截取处理
			    			var content=entity.content;////取得消息的正文  格式化正文 正文在弹出框的最多占用六行位置  超出将被截取处理
			    			if(titile!=null && content!=null){
				    			var tempTitle="";
				    			var messageId=entity.id;
				    			var messageUrl=entity.urlAddress;
				    			var urlType=entity.urlType;
				    			if(messageUrl==null){
				    				if(titile.length>23){//   
					    				tempTitle=titile.substring(0,23)+"...";
					    			}else{
					    				tempTitle=titile;
					    			}
				    			}else{
				    				if(titile.length>23){//   
					    				tempTitle="<a href='javascript:void(0)' style='cursor:pointer;' onclick=\"createMessageTab(\'"+titile+"\',\'"+messageUrl+"\',\'"+urlType+"\',\'"+messageId+"\',"+isDorado+",\'"+baseUrl+"\');\">"+titile.substring(0,23)+"..."+"</a>";
					    			}else{
					    				tempTitle="<a href='javascript:void(0)' style='cursor:pointer;' onclick=\"createMessageTab(\'"+titile+"\',\'"+messageUrl+"\',\'"+urlType+"\',\'"+messageId+"\',"+isDorado+",\'"+baseUrl+"\');\">"+titile+"</a>";
					    			}
				    			}
				    		/*	currBaseUrl="";
				    			currBaseUrl=baseUrl;*/
	                            var sysMessageId=entity.id;
				    			var contentDesc="<div>&nbsp;&nbsp;&nbsp;&nbsp;<font>"+content+"</font></div>";
				    			var currMsg=document.getElementById("msg_win");
					    		if(currMsg!=null){
					    			document.body.removeChild(currMsg);
					    		}
					    		//初始化右下角弹出窗口
				    			Message.init(sysMessageId,baseUrl);
				    			//设置标题
				    			if(titile.length>21){
				    				Message.title.title=titile;
				    			}
				    			Message.title.innerHTML =tempTitle;
				    			//设置对话框显示的内容
				    			Message.content.innerHTML = contentDesc;
				    			
				    			setTimeout("hidMessageWindow()",refreshFrequency*0.8);
				    			
//				    			$('#msg_win').draggable({  
//				    				   handle:'#msg_title'  
//				    			}); 
				    			var msgPop=document.getElementById("msg_win");
				    			msgPop.onmouseover=function(){
				    				   clearInterval(hidTimer);
				    			}
			    			}
			    		}
			    	 })
			     }
			   });
	    },refreshFrequency); 
	}catch(exception){
		clearInterval(timeId);
		var runErrorTimer=setTimeout("loadMessage('"+baseUrl+"',"+refreshFrequency+","+isDorado+")",refreshFrequency*10);
	}
}
/**
 * 在主页的tab工作区中动态创建tab
 * @param title       tab的标题
 * @param messageUrl  tab中引用页面的地址
 * @param messageId   tab的唯一标识
 * @param isDorado    主页是否是dorado7开发的页面
 */
function createMessageTab(title,messageUrl,urlType,messageId,isDorado,currBaseUrl){
	if(isDorado){
		if(messageUrl.substr(0,4)=="http"){
			if(urlType!=null && urlType=='0'){
				eval(messageUrl);
			}else{
				createMessageTab_({tab:{path:messageUrl,name:title},refreshOld:true});
			}
		}else{
			if(urlType!=null && urlType=='0'){
				eval(messageUrl);
			}else{
				createMessageTab_({tab:{path:currBaseUrl+messageUrl,name:title},refreshOld:true});
			}
		}
	}else{
		if(urlType == 0){
			// 不执行最下面的那个updateMessageStatus，所以放在了这里。
			updateMessageStatus(messageId,'1',isDorado,currBaseUrl);
			eval(messageUrl);
		}else{
			addTab(title,messageUrl,'avicit/platform6/modules/system/css/images/folder_lightbulb.png',messageId,'0px 0px');
		}
	}
	updateMessageStatus(messageId,'1',isDorado,currBaseUrl);
}
/**
 * 隐藏右下角窗口 启动函数
 */
function hidMessageWindow(){
   hidTimer=setInterval("changeMessageWindowHeight()",20);//调用changeMessageWindowHeight(),每0.02秒向下移动一次 
   var msgPop=document.getElementById("msg_win");
   msgPop.onmouseout=function(){
	  // hidTimer=setInterval("changeMessageWindowHeight()",100);//调用changeMessageWindowHeight(),每0.1秒向下移动一次
   }
} 
/**
 * 不断改变右下角窗口的高度 当仅剩下标题栏高度时停止
 */
function changeMessageWindowHeight() { 
    var msgPop=document.getElementById("msg_win");
    var popH=parseInt(msgPop.offsetHeight);//用parseInt将对象的高度转化为数字
    var currTOP=parseInt(msgPop.style.top);
    if(currTOP<=652){
    	//msgPop.style.height=(popH-2).toString()+"px";
    	msgPop.style.top=(parseInt(msgPop.style.top)+2).toString()+"px";
    }else{//否则  
        clearInterval(hidTimer);//取消调用 
    } 
} 

/**
 * 创建展示详细信息对话框
 */
function createDialog(){
		var currMsg=document.getElementById("messageDetail_dialog");
    	if(currMsg!=null){
    		return;
    	}
		var dialog=document.createElement('div');
        dialog.id = "messageDetail_dialog";
        dialog.title="消息查看";
        dialog.style="background:#fff;padding:5px;width:500px;height:300px;display:none;"
        // document.body.appendChild(dialog);
        document.body.insertBefore(dialog,document.getElementById("msg_win"));
}
/**
 * 打开消息展示详细信息对话框
 * @param baseUrl       应用上下文路径
 * @param openFilePath  打开文件的路径
 */
function openMessageDetailDialog(baseUrl,openFilePath){
	createDialog();
	$('#messageDetail_dialog').show();
	$('#messageDetail_dialog').dialog();
	$('div[id=messageDetail_dialog]').html($("<iframe name='messageDetailFrame' id='messageDetailFrame' scrolling='yes' frameborder='0'  src='"+baseUrl+openFilePath+"' style='width:100%;height:92%;'></iframe>"));
	$('#messageDetail_dialog').dialog('open');
}
