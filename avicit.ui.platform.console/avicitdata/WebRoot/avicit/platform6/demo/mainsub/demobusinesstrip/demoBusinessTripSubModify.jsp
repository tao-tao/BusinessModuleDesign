<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>修改</title>
<base href="<%=ViewUtil.getRequestPath(request)%>">
<jsp:include page="/avicit/platform6/component/common/EasyUIJsInclude.jsp"></jsp:include>
<script src="static/js/platform/component/common/exteasyui.js" type="text/javascript"></script>
<style type="text/css">
.inputbox{
	background-color: #fff;
	border: 1px solid #95b8e7;
	color: #000;
	height: 18px;
}
</style>
</head>
<body class="easyui-layout" fit="true">
	<div data-options="region:'center',split:true,border:false" style="overflow:hidden;padding-bottom:35px;">
		<form id='form'>
			<input type="hidden" name="id" value='${demoBusinessTripSubDTO.id}'/>
			<input type="hidden" name="parentId" value='${demoBusinessTripSubDTO.parentId}'/>
				<table width="100%" style="padding-top: 10px;">
					<tr>
						<th align="right">出差地点:</th>
						<td>
							  <input title="出差地点" class="inputbox" style="width: 180px;" type="text" name="address" id="address" value='${demoBusinessTripSubDTO.address}'/>
						</td>
						<th align="right">出差内容:</th>
						<td>
							 <input title="出差内容" class="inputbox" style="width: 180px;" type="text" name="content" id="content" value='${demoBusinessTripSubDTO.content}'/>
						</td>
					</tr>
					<tr>
						<th align="right">出差借款:</th>
						<td>
							  <input title="出差借款" class="easyui-numberbox" style="width: 180px;" type="text" name="lendmoney" id="lendmoney" value='${demoBusinessTripSubDTO.lendmoney}'/>
						</td>
					</tr>
				</table>
		</form>
		<div id="toolbar" class="datagrid-toolbar datagrid-toolbar-extend">
			<table class="tableForm" border="0" cellspacing="1" width='100%'>
				<tr>	
					<td width="50%" align="right">
						<a title="保存" id="saveButton" class="easyui-linkbutton" onclick="saveForm();" href="javascript:void(0);">保存</a>
						<a title="返回" id="returnButton" class="easyui-linkbutton" onclick="closeForm();" href="javascript:void(0);">返回</a>
					</td>
				</tr>
			</table>
		</div>
	</div>
	<script type="text/javascript">
	function closeForm(){
		parent.demoBusinessTripSub.closeDialog("#edit");
	};
	function saveForm(){
		parent.demoBusinessTripSub.save(serializeObject($('#form')),"#edit");
	};
	</script>
</body>
</html>