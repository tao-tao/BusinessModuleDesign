<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>流程实例信息</title>
<base href="<%=ViewUtil.getRequestPath(request) %>">
<jsp:include page="/avicit/platform6/bpmclient/bpm/ProcessCommonJsInclude.jsp"></jsp:include>
</head>
<%
	String deployId = request.getParameter("deployId");
	String processDefId = request.getParameter("processDefId");
%>
<script type="text/javascript">
var baseurl = '<%=request.getContextPath()%>';
$(function(){
 	loadProcessTree();
 	window.setTimeout("expand()", 400);
});

//初始化流程业务树 
function loadProcessTree(){
	$('#mytree').tree({   
		checkbox: false,   
		lines : true,
		method : 'post',
	    url:'platform/bpm/bpmPublishAction/getPrcessPublishTree.json?nodeType=catalog&id=root',  
	    onBeforeExpand:function(node,param){  
	    	 $('#mytree').tree('options').url = "platform/bpm/bpmPublishAction/getPrcessPublishTree.json?nodeType=catalog&id=" + node.id ;
	    },
	    onClick:function(node){
            clickTree(node);
      	}
	});  
}
//点击树事件 
function clickTree(node) {
	expand();
	var dbid = node.id;
	var name = node.text;
	if(dbid==null||dbid==""){
		$.messager.alert("操作提示", "请先选择一个业务目录!","info");
		return;
	}
	$.messager.confirm("操作提示", "您确认要切换选定的业务目录吗？", function (data) {
        if (data) {
        	easyuiMask();
        	ajaxRequest("POST","dbId="+dbid+"&processDefId=<%=processDefId%>","platform/bpm/bpmPublishAction/changeProcessCatalog","json","changeProcessCatalogBack");
        }
	 });
}

function  changeProcessCatalogBack(result){
	if(result.flag=="ok"){
		parent.$("#processlist").datagrid('reload'); 
		$.messager.show({title : '提示',msg : "成功。"});
	}else if(result.flag=="error"){
		$.messager.show({title : '提示',msg : "失败。"});
	}
    easyuiUnMask();
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

</script>
<body class="easyui-layout" fit="true">
<div data-options="region:'center'"  style="width:200px;overflow: auto;">
	<ul id="mytree"> </ul>  
</div>  

</body>
</html>