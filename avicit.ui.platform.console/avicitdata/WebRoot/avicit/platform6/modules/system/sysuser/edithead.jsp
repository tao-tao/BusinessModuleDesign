<%@ page import="avicit.platform6.api.session.SessionHelper"%>
<%@ page import="avicit.platform6.core.spring.SpringFactory"%>
<% 
//String languageCode=avicit.platform6.core.session.SessionHelper.getCurrentUserLanguageCode();
//String skinColor = "default"; //gray

String languageCode=SessionHelper.getCurrentUserLanguageCode(request);

String skinColor = "gray";
%>
<link href="static/js/platform/component/jQuery/jquery-easyui-1.3.5/themes/<%=skinColor%>/easyui.css" type="text/css" rel="stylesheet">
<link href="static/js/platform/component/jQuery/jquery-easyui-1.3.5/themes/<%=skinColor%>/icon.css"  type="text/css" rel="stylesheet">
<link href="static/js/platform/component/jQuery/jquery-easyui-1.3.5/themes/<%=skinColor%>/page.css"  type="text/css"  rel="stylesheet">
<link href="static/css/platform/themes/default/commonselection/icon_ext.css" rel="stylesheet" type="text/css">
<link href="static/css/platform/themes/default/commonselection/easyui_editor_ex.css"  rel="stylesheet" type="text/css">
<!--[if IE 6]>
	<script type="text/javascript"> 
		try { document.execCommand('BackgroundImageCache', false, true); } catch(e) {} 
	</script>
<![endif]-->

<script src="static/js/platform/component/jQuery/jQuery-1.8.2/jquery-1.8.2.min.js" type="text/javascript"></script>
<script src="static/js/platform/component/jQuery/jquery-easyui-1.3.5/jquery.easyui.min.js" type="text/javascript"></script>
<script src="static/js/platform/component/jQuery/jquery-easyui-1.3.5/locale/easyui-lang-<%=languageCode %>.js" type="text/javascript"></script>
<script src="static/js/platform/component/common/pageUtil.js" type="text/javascript"></script>
<script src="static/js/platform/component/common/Tools.js" type="text/javascript"></script>