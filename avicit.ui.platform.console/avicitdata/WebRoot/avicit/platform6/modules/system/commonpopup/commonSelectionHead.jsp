<%@page import="avicit.platform6.api.sysshirolog.impl.AfterLoginSessionProcess"%>
<% 
//modify by xingc
String skinColor = (String)request.getSession().getAttribute(AfterLoginSessionProcess.SESSION_CURRENT_USER_SKIN);
%>
<link href="static/css/platform/themes/<%=skinColor%>/commonselection/icon_ext.css" rel="stylesheet" type="text/css">
<link href="static/css/platform/themes/<%=skinColor%>/commonselection/easyui_editor_ex.css"  rel="stylesheet" type="text/css">
<script src="static/js/platform/component/commonselection/CommonSelectorDialog.js"  type="text/javascript" ></script>
<script src="static/js/platform/component/commonselection/commonSelectionUtil.js"  type="text/javascript" ></script>
<script src="static/js/platform/component/commonselection/ComprehensiveSelector.js"  type="text/javascript" ></script>
<script src="static/js/platform/component/commonselection/easyui_editor_ex.js"  type="text/javascript" ></script>

