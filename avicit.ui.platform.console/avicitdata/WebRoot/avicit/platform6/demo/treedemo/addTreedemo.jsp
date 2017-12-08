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
	<div data-options="region:'center',split:true,border:false" style="padding-bottom:32px;overflow: auto;">
		<form id='form' class='form'> 
		<input type="hidden" name='id' id='id' value=''/>
		<table class="table" width="100%" style="padding-top: 4px;">
			<tr>
				<th align="right" width="85px">编码</th>
				<td>
					<input title="编码" class="inputbox"  type="text" name="code" id="code" value=''/>
				</td>
			</tr>
			<tr>
				<th align="right" width="85px">名称</th>
				<td>
					<input title="名称" class="inputbox"  type="text" name="name" id="name" value=''/>
				</td>
			</tr>
		</table>
	</form>
	<div id="toolbar" class="datagrid-toolbar datagrid-toolbar-extend">
			<table class="tableForm" border="0" cellspacing="1" width='100%'>
				<tr>	
					<td width="60%" align="right"><a title="保存" id="saveButton"  class="easyui-linkbutton" plain="false" onclick="saveForm();" href="javascript:void(0);">保存</a>
					<a title="返回" id="returnButton"  class="easyui-linkbutton" plain="false" onclick="closeForm();" href="javascript:void(0);">返回</a>
					</td>
				</tr>
			</table>
		</div>
	</div>
	<script type="text/javascript">
		
		function closeForm(){
			parent.treeDemo.closeDialog("#insert");
		}
		function saveForm(){
			parent.treeDemo.save(serializeObject($('#form')),'${url}','#insert','${id}');
		}
	</script>
</body>
</html>