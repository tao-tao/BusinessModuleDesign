//////////////////////////////////////////////////////////////
//基础功能
//////////////////////////////////////////////////////////////

//获取参数值
function fGetQuery(name){
	var sUrl = window.location.search.substr(1);
	var r = sUrl.match(new RegExp("(^|&)" + name + "=([^&]*)(&|$)"));
	return (r == null ? null : unescape(r[2]));
};

//获取#参数值
function fGetQueryHash(name){
	var sUrl = window.location.hash.substr(1);
	var r = sUrl.match(new RegExp("(^|&)" + name + "=([^&]*)(&|$)"));
	return (r == null ? null : unescape(r[2]));
};

//GetElementById
function $id(sId){
	return document.getElementById(sId);
};

//过滤帐号
function fTrim(str){
	return str.replace(/(^\s*)|(\s*$)/g, "").replace(/(^　*)|(　*$)/g, "");
};

//过滤手机号
function fParseMNum(sNum){
	var sTmpNum = fTrim(sNum);
	return /^0?(13|14|15|18)\d{9}$/.test(sTmpNum);
};

//自动截断对应域email地址
function fCheckAccount(sEmail){
	var sAccount = sEmail;
	var bAt;
	bAt = sAccount.indexOf("@" + gOption["sDomain"]) == -1;
	if(!bAt){
		var aAccountSplit;
		aAccountSplit=sEmail.split("@");
		sEmail=aAccountSplit[0];
	}
	return sEmail;
};


//获取Cookie
function fGetCookie(sName){
   var sSearch = sName + "=";
   if(document.cookie.length > 0){
      offset = document.cookie.indexOf(sSearch)
      if(offset != -1){
         offset += sSearch.length;
         end = document.cookie.indexOf(";", offset)
         if(end == -1) end = document.cookie.length;
         return unescape(document.cookie.substring(offset, end))
      }
      else return ""
   }
};

//设置Cookie
function fSetCookie(name, value, isForever, domain){
	var sDomain = ";domain=" + (domain || gOption["sCookieDomain"] );
	document.cookie = name + "=" + escape(value) + sDomain + (isForever?";expires="+  (new Date(2099,12,31)).toGMTString():"");
};

//绑定事件监听
function fEventListen(oElement, sName, fObserver, bUseCapture){
	bUseCapture = !!bUseCapture;
	if (oElement.addEventListener){
		oElement.addEventListener(sName, fObserver, bUseCapture);
	}else if(oElement.attachEvent){
		oElement.attachEvent('on' + sName, fObserver);
	}
};

//删除事件监听
function fEventUnlisten(oElement, sName, fObserver, bUseCapture){
	bUseCapture = !!bUseCapture;
	if(oElement.removeEventListener){
		oElement.removeEventListener(sName, fObserver, bUseCapture);
	}else if(oElement.detachEvent){
		oElement.detachEvent('on' + sName, fObserver);
	}
};

//限定范围随机数
function fRandom(nLength){
	return Math.floor(nLength * Math.random());
};

//url参数
function fUrlP(sName,sValue,bIsFirst){
	if(!arguments[2]){bIsFirst = false;}
	if(bIsFirst){		
		return sName + '=' + sValue;
	}else{
		return '&' + sName + '=' + sValue;
	}
};

//同步改变窗口大小与遮罩
function fResize(){
	var nBodyHeight = document.body.offsetHeight,
		nWindowHeight = document.documentElement.clientHeight,
		nResult;
	if(nBodyHeight > nWindowHeight){
		nResult = nBodyHeight;
	}else{
		nResult = nWindowHeight;
	}
	if($id("mask") == null){
		
	}else{
		$id("mask").style.height = nResult + "px"
	}
};

//////////////////////////////////////////////////////////////
//具体功能
//////////////////////////////////////////////////////////////

//fFQ
function fFQ(){
	var sFqLf = fGetQuery("fq");
	var bEnableFQ = (/^[0-9]/).test(sFqLf);
	var sFQuid = fGetQuery("uid");
	var bTestMail = (new RegExp("(@"+ gOption["sDomain"] +")$")).test(sFQuid);
	if(bEnableFQ && bTestMail){
		var nFQrandom = (new Date()).getTime();
		fSetCookie("fq",sFqLf+"_"+nFQrandom,false);
		var oImg = document.createElement("img");
		var sImgUrl = "http://count.mail.163.com/beacon/login.gif?uid=" + sFQuid + "&fq=" + nFQrandom + "&lf=" + fGetQuery("fq");
		oImg.setAttribute("src", sImgUrl);
		oImg.setAttribute("alt", "");
		oImg.style.display = "none";
		document.body.appendChild(oImg);
	}
};

//设置starttime的Cookie
function fStartTime(){
	var sSt = fGetCookie("starttime");
	if( sSt == "" ){
		sSt = (new Date()).getTime();
		fSetCookie("starttime",sSt,false);
	}
};

//处理nts_mail_user的Cookie
var gUserInfo = {
	"username" : null,
	"style"    : null,
	"safe"     : null
};
var gVisitorCookie = (function(){
	var _fGetNtsMailUser = function(){
		var sUserInfo = fGetCookie("nts_mail_user");
		if( sUserInfo != undefined ){
			var aTmp = sUserInfo.split(":");			
			if( aTmp.length == 3 ){
				gUserInfo["username"] = aTmp[0];
				gUserInfo["style"] = aTmp[1];
				gUserInfo["safe"] = aTmp[2];
			}
		}
		return;
	},
	_fSetNtsMailUser = function(){
		var sUserInfo = gUserInfo.username + ":" + gUserInfo.style + ":" + gUserInfo.safe;
		_fSetNtsMailCookie("nts_mail_user",sUserInfo,true);
		return;
	},
	_fSetNtsMailCookie = function(name, value, isForever, domain){
		var sDomain = ";domain=" + (domain || gOption["sCookieDomain"] );
		document.cookie = name + "=" + value + sDomain + (isForever?";expires="+  (new Date(2099,12,31)).toGMTString():"");
	};
	return {
		"init" : function(){
			_fGetNtsMailUser();
			return this;
		},
		"saveInfo" : function(){
			_fSetNtsMailUser();
		},
		"loadInfo" : function(){
			_fGetNtsMailUser();
		}
	};
})().init();

function loginRequest(jsonp){
	rnd = getRnd();
	c_url = "http://reg.163.com/services/httpLoginExchgKey?rnd="+rnd;
	c_url += "&jsonp="+jsonp;
	fGetScript(c_url);
};

function getRnd(){
	var timestamp = new Date().getTime();
	var rnd = base64encode(utf16to8("\n" + timestamp));
	return rnd;
};

// documentReady事件支持
var DOMContentLoaded;
var DOMREADY = function(o){
	var DOMREADY = {
		isReady		:	false,
		ready		:	o,
		bindReady	:	function(){
			try{
				if ( document.readyState === "complete" ){
					DOMREADY.isReady = true;
					return setTimeout( DOMREADY.ready, 1 );
				}
				if ( document.addEventListener ){
					document.addEventListener( "DOMContentLoaded", DOMContentLoaded, false );
				}else if( document.attachEvent ){
					document.attachEvent( "onreadystatechange", DOMContentLoaded );
					var toplevel = false;
					try {
						toplevel = window.frameElement == null;
					} catch(e) {}
					if( document.documentElement.doScroll && toplevel ){
						doScrollCheck();
					}
				}
			}catch(e){}
		}
	};
	if( document.addEventListener ){
		DOMContentLoaded = function(){
			document.removeEventListener( "DOMContentLoaded", DOMContentLoaded, false );
			DOMREADY.ready();
		};

	}else if ( document.attachEvent ){
		DOMContentLoaded = function(){
			if ( document.readyState === "complete" ) {
				document.detachEvent( "onreadystatechange", DOMContentLoaded );
				if( DOMREADY.isReady ){
					return;
				}else{
					DOMREADY.isReady = true;
					DOMREADY.ready();
				}
			}
		};
	}
	function doScrollCheck(){
		if( DOMREADY.isReady ){
			return;
		}
		try {
			document.documentElement.doScroll("left");
		}catch(e){
			setTimeout( doScrollCheck, 1);
			return;
		}
		DOMREADY.isReady = true;
		DOMREADY.ready();
	}
	DOMREADY.bindReady();
};