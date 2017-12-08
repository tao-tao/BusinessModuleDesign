<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<html>
<head>
<%
	String path = request.getContextPath();
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>流程实例信息</title>
<base href="<%=ViewUtil.getRequestPath(request) %>">
<jsp:include page="/avicit/platform6/bpmclient/bpm/ProcessCommonJsInclude.jsp"></jsp:include>
</head>

<script type="text/javascript">
var baseurl = '<%=request.getContextPath()%>';
/**
 * 初始化当前页面
 */
$(function(){
	loadProcessTree();
	window.setTimeout("expand()", 600);
});

//初始化流程业务树 
function loadProcessTree(){
	$('#mytree').tree({   
		checkbox: false,   
		lines : true,
		method : 'post',
		animate:true,
	    url:'platform/bpm/clientbpmdisplayaction/getProcessStartTree.json?id=root',  
	    onBeforeExpand:function(node,param){
	    	$('#mytree').tree('options').url = "platform/bpm/clientbpmdisplayaction/getProcessStartTree.json?id=" + node.id ;
	    },
	    onClick:function(node){
            clickTree(node);
      	}
	});  
}
//点击树事件 
function clickTree(node) {
	expand();
	var nodeType = node.attributes.nodeType;
	if(nodeType!=null&&nodeType=="process"){
		var formUrl = node.attributes.formUrl;
		startProcess(node.id,node.text,formUrl);
	}
}

//展开树
function expand() {
		var node = $('#mytree').tree('getSelected');
		if(node){
			$('#mytree').tree('expand',node.target);
		}else{
			$('#mytree').tree('expandAll');
		}
}
function startProcess(pdId,pdName,formUrl){
	var paras = "pdId="+pdId+"&pdName="+pdName;
	ajaxRequest("POST",paras,"platform/bpm/clientbpmdisplayaction/getProcessStartUrl","json","startProcessBack");
}
function startProcessBack(obj){
	var pdId = obj.pdId;
	var pdName = obj.pdName;
	var formUrl = obj.formUrl;
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
<body class="easyui-layout" fit="true">
<div style="height:100%;overflow:auto;" >
	<ul id="mytree"> </ul>  
</div>  

</body>
</html>