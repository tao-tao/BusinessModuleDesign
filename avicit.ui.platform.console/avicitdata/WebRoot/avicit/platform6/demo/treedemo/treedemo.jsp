<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>treedemo</title>
<base href="<%=ViewUtil.getRequestPath(request)%>">
<jsp:include page="/avicit/platform6/component/common/EasyUIJsInclude.jsp"></jsp:include>

</head>
<body class="easyui-layout" fit="true">
<div data-options="region:'west',split:true,title:''" style="width:250px;padding:0px;">
	 <div id="toolbar" class="datagrid-toolbar">
	 	<table width="100%">
	 		<tr>
	 			<td width="100%"><input  type="text"  name="searchWord" id="searchWord"></input></td>
	 		</tr>
	 	</table>
	 </div>
	<ul id="menu">正在加载数据...</ul>
</div>
<div data-options="region:'center',split:true,title:'操作'" style="padding:0px;overflow:auto;">
	<div id="toolbarImportResult" class='datagrid-toolbar'>
		<table>
			<tr>
				<td><a class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="treeDemo.insert();" href="javascript:void(0);">添加平级节点</a></td>
				<td><a class="easyui-linkbutton" iconCls="icon-add_other" plain="true" onclick="treeDemo.insertsub();" href="javascript:void(0);">添加子节点</a></td>
				<td><a class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="treeDemo.modify();" href="javascript:void(0);">编辑</a></td>
				<td><a class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="treeDemo.del();" href="javascript:void(0);">删除</a></td>
			</tr>
		</table>
	</div>
</div>
<script src="avicit/platform6/demo/treedemo/js/TreeDemo.js" type="text/javascript"></script>
<script type="text/javascript">
	var treeDemo;
	$(function(){
		treeDemo = new TreeDemo('menu','${url}','searchWord');
	});
</script>
</body>
</html>