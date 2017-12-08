<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<%
	String userId = (String)session.getAttribute("userId");
%>
<base href="<%=ViewUtil.getRequestPath(request) %>">
<jsp:include page="/avicit/platform6/bpmclient/bpm/ProcessCommonJsInclude.jsp"></jsp:include>
<script src="static/js/platform/bpm/client/js/ToolBar.js" type="text/javascript"></script>

</head>

<script type="text/javascript">
var baseurl = '<%=request.getContextPath()%>';
var userId = '<%=userId%>';
$(function(){
	getProcessStartList();
});
/**
 * 获取该用户可以启动的流程列表
 */
function getProcessStartList(){
	ajaxRequest("POST","userId="+userId,"platform/bpm/clientbpmdisplayaction/getProcessStartList","json","getProcessStartListBack");
}
function getProcessStartListBack(obj){
	var data = new Array();
	var dataCount = 0;
	data[dataCount++] = "<table width='100%'  border='0' cellspacing='0' cellpadding='0'>";
	if(obj!=null&&obj.list!=null){
		var  pdArray = obj.list;
		for (var i = 0; i < pdArray.length; i++) {
			var pd = pdArray[i];
			data[dataCount++] = "<tr height='20'>";
			data[dataCount++] = "<td align='left'  width='100%' onclick='startProcess(\""+pd.dbid+"\",\""+pd.name+"\",\""+pd.formUrl+"\")' style='cursor:pointer'>&nbsp;"+pd.name+"</td>";
			data[dataCount++] = "</tr>";
		}
	}
	data[dataCount++] = "</table>";
	$('#processStartListDiv').html(data.join("")); 
}
function startProcess(pdId,pdName,formUrl){
	if(formUrl==null||formUrl==""){
		alert("启动失败，请先配置流程的启动表单。");
		return;
	}
	if (formUrl!=null&&formUrl.indexOf("?") > 0) {
		formUrl += "&pdId=" + pdId;
	}else{
		formUrl += "?pdId=" + pdId;
	}
	if(typeof(eval(top.addTab))=="function"){
		top.addTab(pdName,formUrl,"dorado/client/skins/~current/common/icons.gif","processFormStart"," -0px -120px");
	}
	
}
</script>
<body>
<div id="processStartListDiv"></div>
<body>
</html>
