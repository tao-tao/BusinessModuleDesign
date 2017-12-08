<%@page import="avicit.platform6.api.sysshirolog.impl.AfterLoginSessionProcess"%>
<% 
String languageCode="zh_CN";
//String skinColor = "default";
//modify by xingc
String skinColor = (String)request.getSession().getAttribute(AfterLoginSessionProcess.SESSION_CURRENT_USER_SKIN);
%>
<meta http-equiv="X-UA-Compatible" content="IE=8">
<link href="static/js/platform/component/jQuery/jquery-easyui-1.3.5/themes/<%=skinColor%>/easyui.css" type="text/css" rel="stylesheet">
<link href="static/js/platform/component/jQuery/jquery-easyui-1.3.5/themes/<%=skinColor%>/avicit-easyui-extend-1.3.5.css" type="text/css" rel="stylesheet">
<link href="static/js/platform/component/jQuery/jquery-easyui-1.3.5/themes/<%=skinColor%>/icon.css" type="text/css" rel="stylesheet">
<link href="static/js/platform/component/jQuery/jquery-easyui-1.3.5/themes/<%=skinColor%>/page.css" type="text/css"  rel="stylesheet">
<link href="static/css/platform/themes/default/public/platform6.css" type="text/css"  rel="stylesheet">
<!--[if IE 6]>
	<script type="text/javascript"> 
		try { document.execCommand('BackgroundImageCache', false, true); } catch(e) {} 
	</script>
<![endif]-->
<script src="static/js/platform/component/common/json2.js" type="text/javascript"></script>
<script src="static/js/platform/component/jQuery/jQuery-1.8.2/jquery-1.8.2.min.js" type="text/javascript"></script>
<script src="static/js/platform/component/jQuery/jquery-easyui-1.3.5/jquery.easyui.min.js" type="text/javascript"></script>
<script src="static/js/platform/component/jQuery/jquery-easyui-1.3.5/datagrid-scrollview.js" type="text/javascript"></script>
<script src="static/js/platform/component/jQuery/jquery-easyui-1.3.5/datagrid-filter.js" type="text/javascript"></script>
<script src="static/js/platform/component/jQuery/jquery-easyui-1.3.5/locale/easyui-lang-<%=languageCode %>.js" type="text/javascript"></script>
<script src="static/js/platform/component/common/pageUtil.js" type="text/javascript"></script>
<script src="static/js/platform/component/common/Tools.js" type="text/javascript"></script>
<script src="static/js/platform/component/common/CommonDialog.js" type="text/javascript"></script>
<script src="static/js/platform/component/common/exteasyui.js" type="text/javascript"></script>
<script type="text/javascript">
document.onkeydown = check;  
function check(e) {  
    if (!e) var e = window.event;  
    if (e.keyCode) code = e.keyCode;  
    else if (e.which) code = e.which;  
    if (((event.keyCode == 8) &&                                                    //BackSpace   
         ((event.srcElement.type != "text" &&   
         event.srcElement.type != "textarea" &&   
         event.srcElement.type != "password") ||   
         event.srcElement.readOnly == true)) ||   
        ((event.ctrlKey) && ((event.keyCode == 78) || (event.keyCode == 82)) ) ||    //CtrlN,CtrlR   
        (event.keyCode == 116) ) {                                                   //F5   
        event.keyCode = 0;   
        event.returnValue = false;   
    }  
    return true;  
}  
</script>