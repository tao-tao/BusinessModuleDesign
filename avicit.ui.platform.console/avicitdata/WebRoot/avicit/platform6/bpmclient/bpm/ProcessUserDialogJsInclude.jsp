<%@page import="avicit.platform6.api.sysshirolog.impl.AfterLoginSessionProcess"%>
<% 
String languageCode="zh_CN";
//modify by xingc
String skinColor = (String)request.getSession().getAttribute(AfterLoginSessionProcess.SESSION_CURRENT_USER_SKIN);
%>
<link rel="stylesheet" type="text/css" href="static/js/platform/component/jQuery/jquery-easyui-1.3.5/themes/<%=skinColor%>/easyui.css">
<link rel="stylesheet" type="text/css" href="static/js/platform/component/jQuery/jquery-easyui-1.3.5/themes/<%=skinColor%>/icon.css">
<link type="text/css" rel="stylesheet" href="static/css/platform/bpm/client/css/ProcessSelectorUserStyle.css" >
<script src="static/js/platform/component/common/json2.js" type="text/javascript"></script>
<script src="static/js/platform/component/jQuery/jQuery-1.8.2/jquery-1.8.2.min.js" type="text/javascript"></script>
<script src="static/js/platform/component/jQuery/jquery-easyui-1.3.5/jquery.easyui.min.js" type="text/javascript"></script>
<script src="static/js/platform/component/common/Tools.js" type="text/javascript"></script>
<script src="static/js/platform/component/dialog/UserSelectDialog.js" type="text/javascript"></script>
