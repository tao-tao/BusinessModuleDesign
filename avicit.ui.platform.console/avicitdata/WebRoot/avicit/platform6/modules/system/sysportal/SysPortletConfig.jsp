<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="avicit.platform6.core.session.SessionHelper"%>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<%
 	String loginUserName = SessionHelper.getLoginSysUser().getName();
 	String loginUserId = SessionHelper.getLoginSysUser().getId();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>

<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<base href="<%=ViewUtil.getRequestPath(request)%>">
<jsp:include page="/avicit/platform6/component/common/EasyUIJsInclude.jsp"></jsp:include>
<style type="text/css">
body{
	font-family: Microsoft Yahei,sans-serif,Arial,Helvetica;
	font-size:12px;
	margin:0px 0px 0px 0px;
}
.web_dialog {
	display: none;
	position: absolute;
	width: 380px;
	height: 200px;
	top: 50%;
	left: 50%;
	background-color: #ffffff;
	border: 2px solid #336699;
	padding: 0px;
	z-index: 102;
	font-family: Verdana;
	font-size: 10pt;
	border-radius: 4px 4px 4px 4px;
	-moz-box-shadow: 2px 2px 10px #06C;
	-webkit-box-shadow: 2px 2px 10px #06C;
	box-shadow: 2px 2px 10px #06C;
	filter: progid:DXImageTransform.Microsoft.Shadow(color='#bdbdbd',Direction=135, Strength=5 );
	border: 1px solid #E7E7E7;
}
.web_dialog #cancel{
	cursor: pointer;
}
.web_dialog #save{
	background-color: #336699;
    background-image: -moz-linear-gradient(center top , #6c8aa8, #0e81f3);
    border-radius: 4px 4px 4px 4px;
    box-shadow: 0 5px 15px 0 rgba(0, 0, 0, 0.2);
    border: 1px solid #113860;
    color: #FFFFFF;
    cursor: pointer;
    height: 30px;
    margin-right: 3px;
    text-shadow: 0 1px rgba(0, 0, 0, 0.1);
    width:80px;
}
.web_dialog #cancel{
	background-color: #336699;
    background-image: -moz-linear-gradient(center top , #0e81f3, #6c8aa8);
    border-radius: 4px 4px 4px 4px;
    box-shadow: 0 5px 15px 0 rgba(0, 0, 0, 0.2);
    border: 1px solid #113860;
    color: #FFFFFF;
    cursor: pointer;
    height: 30px;
    margin-right: 2px;
    text-shadow: 0 1px rgba(0, 0, 0, 0.1);
    width:80px;
}
.web_dialog_title {
	border-bottom: solid 1px #336699;
	padding: 4px;
	font-weight:bold;
	cursor: move;
}
.web_dialog_title a {
	color: #336699;
	padding-right:4px;
	text-decoration: none;
}
.web_dialog_title a:hover {
	color: #336699;
	padding-right:4px;
	text-decoration: none;
}
.align_right {
	text-align: right;
}
.window-mask{
	background:#FFFFFF;
	position:absolute;
	width:100%;
	top:0px;
	left:0px;
	opacity:0.6; 
	filter:alpha(opacity=60);
	z-index:2;
}
.layoutContentConfig{
	position : relative;
}
.defaultSelected{
	height : 16px;
	background: url('avicit/platform6/modules/system/sysportal/portalstyle/green_yes.gif') no-repeat;
}
</style>
<script type="text/javascript">
	var contextPath = "${contextPath}";
	var isgloable = "${isgloable}";
	var loginUserName = '<%=loginUserName%>';
	var loginUserId = '<%=loginUserId%>';
	var layoutTemplateName = '${layoutTemplateName}';
	var recordId = '${id}';
	
	$(function(){
		$(".layoutContentConfig").bind({
			mouseover:function(){
				$(this).css("background-color","#f0f0f0");
			},  
			mouseout:function(){
				$(this).css("background-color","#ffffff");
			},
			click : function(){
				$('.layoutContentConfig').css("background-color","#ffffff").bind('mouseout');
				$(this).css("background-color","#f0f0f0");
				$(this).unbind('mouseout');
				$('#layoutTemplateNameHidden').val($(this).attr('layoutTemplateNameValue'));
				$('#layoutConfigContentIframe').attr('src',contextPath + '/platform/portlet/getPortletConfigContent?isgloable=' + isgloable + '&id=' + recordId + '&layoutTemplateName=' + $(this).attr('layoutTemplateNameValue'));
			}
		});
		
	     $("#btnClose").click(function (e){
			 $.mask("close");
	         hideDialog();
	         e.preventDefault();
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
	});
	
	function backOperate(value,valueText,columnId){
		var frm = document.getElementById('layoutConfigContentIframe').contentWindow;
		frm.backOperate(value,valueText,columnId);
		return false;
	}
	function saveConfigResultXml(){
		var frm = document.getElementById('layoutConfigContentIframe').contentWindow;
		return frm.saveConfigResultXml();
	}
	$(function(){
		//var layoutConfigContentArg = encodeURI(JSON.stringify(layoutConfigContent));
		$('#layoutConfigContentIframe').attr('src','${layoutConfigContentIframePatch}');
	});
	function openSelectPorletLayoutConfigDialog(portletId,columnId){
		showDialog(true,'添加页面内容',400,300,contextPath + '/platform/portlet/getSelectPortletConfigWebComponent?portletId=&columnId=' +columnId ,'saveAddLayout()');
		return false;
	}
	function saveAddLayout(){
		var frm = $('#portletConfigIframe')[0].contentWindow;
		var columnId = frm.columnId;
		var checkedValue = frm.getCheckboxValue();
		var checkedText = frm.getCheckboxText(checkedValue);
		//回填处理
		backOperate(checkedValue,checkedText,columnId)
		//回填处理end
		hideDialog();
	}
	function hideDialog(){
		$.mask("close");
	    $("#overlay").hide();
	    $("#save").unbind("click");
	    $("#dialog").fadeOut(100);
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
	    var left = 200;
		var top = 50;
		$("#dialog").css({"width": w,"height": h,top: top,left: left});
		$("#dialog #context").css({"height": h - 75 });
		//设置显示内容
		if(typeof(iframeSrc) != 'undefined'){
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
</script>
</head>
<body class="easyui-layout" id='layout'>
	<div data-options="region:'west',split:true" style="width:200px;padding:10px;">
		${layoutContent}
	</div>
	<div data-options="region:'center'" style="height:700px;border:1px solid #ffffff"><iframe scrolling="yes" frameborder="0" width="100%" height="350px" id="layoutConfigContentIframe" name="layoutConfigContentIframe" ></iframe></div>
	<div id="dialog" class="web_dialog">
	  <table style="width: 100%; border: 0px;" cellpadding="3" cellspacing="0">
	    <tr>
	      <td class="web_dialog_title"><span>Online Survey</span></td>
	      <td class="web_dialog_title align_right"><a href="#" id="btnClose">X</a></td>
	    </tr>
	    <tr>
	      <td colspan="2"><div id="context" style="width: 100%;"></div></td>
	    </tr>
	    <tr>
	      <td>&nbsp;</td>
	      <td align="right">
	      	<input type="button" id="save" value="保存">
	      	<input type="button" id="cancel" onclick="hideDialog();return false;" value="取消" >&nbsp;&nbsp;&nbsp;</td>
	    </tr>
	  </table>
	</div>
	<input type='hidden' name='layoutConfigContentResult' id='layoutConfigContentResult' />
	<input type='hidden' name='layoutTemplateNameHidden' id='layoutTemplateNameHidden' value='${layoutTemplateName}'/>
</body>
</html>
