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
      	},
	    onLoadSuccess:function(node,param){
	    	$('#mytree').tree('select',node.target);
	    	clickTree(node);
	    }
	});  
}
//点击树事件 
function clickTree(node) {
	expand();
	$('#name').val(node.text);
	$('#code').val(node.attributes.code);
	$('#remark').val(node.attributes.remark);
	$('#orderBy').val(node.attributes.orderBy);
	$('#dbid').val(node.id);
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
function addProcessCatalog(){
	var dbid = $('#dbid').val();
	if(dbid==null||dbid==""){
		$.messager.alert("操作提示", "请先选择左侧的一个业务目录!","info");
		return;
	}else{
		$('#parentId').val(dbid);
		$('#dbid').val("");
		$('#name').val("");
		$('#code').val("");
		$('#remark').val("");
		$('#orderBy').val("");
	}
}
function saveProcessCatalog(){
	var dbid = $('#dbid').val();
	var parentId = $('#parentId').val();
	var name = $('#name').val();
	if(name==null||name==""){
		$.messager.alert("操作提示", "请输入目录名称!","info");
		return;
	}
	if(dbid!=null&&dbid!=""){ //修改
		 var node = $('#mytree').tree('getSelected'); 
		 if (node) { 
			 node.text = $('#name').val();
			 node.attributes.code = $('#code').val();
			 node.attributes.remark = $('#remark').val();
			 node.attributes.orderBy = $('#orderBy').val();
		 }
		
		 var dataVo = $('#form1').serializeArray();
		 var dataJson = convertToJson(dataVo);
		 dataVo = JSON.stringify(dataJson);
		 ajaxRequest("POST","dataVo="+dataVo,"platform/bpm/bpmPublishAction/updateProcessCatalog","json","addProcessCatalogBack");
	}else if(parentId!=null&&parentId!=""){ //增加
		 var dataVo = $('#form1').serializeArray();
		 var dataJson = convertToJson(dataVo);
		 dataVo = JSON.stringify(dataJson);
		 ajaxRequest("POST","dataVo="+dataVo,"platform/bpm/bpmPublishAction/addProcessCatalog","json","addProcessCatalogBack");
	}else{
		$.messager.alert("操作提示", "请先选择左侧的一个业务目录!","info");
		return;
	}
}
function reload(){
	var node = $('#mytree').tree('getSelected'); 
	if (node) { 
		$('#mytree').tree('reload', node.target); 
	}
}
function addProcessCatalogBack(result){
	if(result.flag=="ok"){
		reload();
		$.messager.show({title : '提示',msg : "保存成功。"});
	}else{
		$.messager.show({title : '提示',msg : "保存失败。"});
	}
}

var _parentNode = null;
function deleteProcessCatalog(){
	var dbid = $('#dbid').val();
	if(dbid==null||dbid==""){
		$.messager.alert("操作提示", "请先选择左侧的一个业务目录!","info");
		return;
	}
	var node = $('#mytree').tree('getSelected'); 
	if (node) { 
		var children = $('#mytree').tree('getChildren', node.target);
		if(children&&children.length>0){
			$.messager.alert("操作提示", "该目录下有子目录，请先删除子目录!","info");
			return;
		}
	}
	
	$.messager.confirm("操作提示", "您确认要删除选定的数据吗？", function (data) {
        if (data) {
        	_parentNode = $('#mytree').tree('getParent', node.target);
        	easyuiMask();
        	ajaxRequest("POST","dbId="+dbid,"platform/bpm/bpmPublishAction/deleteProcessCatalog","json","deleteProcessCatalogBack");
        }
	 });
}

function  deleteProcessCatalogBack(result){
	if(result.flag=="0"){
		$.messager.show({title : '提示',msg : "删除成功。"});
	}else if(result.flag=="1"){
		$.messager.alert("操作提示", "不能删除，该业务目录下有流程定义模板。","info");
	}
	//加载父节点
	if(_parentNode){
		$('#mytree').tree('select',_parentNode.target);
		reload(); clickTree(_parentNode);
	}
    easyuiUnMask();
}
function refreshCache(){
	ajaxRequest("POST",null,"platform/bpm/bpmPublishAction/reLoadCache","json","afterRefreshCache");
}
function afterRefreshCache(json){
	if(json.success == true){
		 $.messager.show({title : '提示',msg : "刷新缓存成功!"});
	}else{
		 $.messager.show({title : '提示',msg : "刷新缓存失败!"});
	}
}
</script>
<body class="easyui-layout" fit="true">
<div data-options="region:'west',title:'流程业务目录管理',split:true,collapsible:false"  style="width:200px;overflow: auto;">
	<ul id="mytree"> </ul>  
</div>  
<div region="center" border="false" style="overflow: hidden;">
	<div id="toolbar" class="datagrid-toolbar" style="height:auto;display: block;">
			<table class="tableForm" id="searchForm" width='100%'>
				<tr>
					<td width="100px;"><a class="easyui-linkbutton"   iconCls="icon-add" plain="true" onclick="addProcessCatalog();" href="javascript:void(0);">添加子目录</a></td>
					<td width="70px;"><a class="easyui-linkbutton"   iconCls="icon-save" plain="true" onclick="saveProcessCatalog();" href="javascript:void(0);">保存</a></td>
					<td width="90px;"><a class="easyui-linkbutton"   iconCls="icon-reload" plain="true" onclick="refreshCache();" href="javascript:void(0);">刷新缓存</a></td>
					<td width="70px;"><a class="easyui-linkbutton"   iconCls="icon-remove" plain="true" onclick="deleteProcessCatalog();" href="javascript:void(0);">删除</a></td>
					<td > </td>
				</tr>
			</table>
	</div>
	<div>
		<form id="form1" method="post">
        <table class="tableForm" width="100%" border=0>
				<tr>
					<td >&nbsp;&nbsp;目录名称</td>
					<td ><input  class="easyui-validatebox"  id="name"  name="name" required="true" style="width:450px;"/></td>
					</td>
				</tr>
				<tr>
					<td>&nbsp;&nbsp;目录代码</td>
					<td ><input  class="easyui-validatebox"  id="code"  name="code"   style="width:450px;"/></td>
				</tr>
				<tr>
					<td>&nbsp;&nbsp;目录说明</td>
					<td ><input  class="easyui-validatebox"  id="remark"  name="remark"   style="width:450px;"/></td>
				</tr>
				<tr>
					<td >&nbsp;&nbsp;排序编号</td>
					<td>
					<input  class="easyui-validatebox"  id="orderBy"  name="orderBy"  style="width:450px;" />
					<input name="dbid" id="dbid"  style="display:none;" />
					<input name="parentId" id="parentId"  style="display:none;" />
					</td>
				</tr>
			</table>
		</form>
	</div>
</div>
</body>
</html>