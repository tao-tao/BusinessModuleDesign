<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>详情</title>
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
			<input type="hidden" name="id" value='${demoBusinessTripMainDTO.id}'/>
				<table width="100%" style="padding-top: 10px;">
					<tr>
						<th align="right">用户id:</th>
						<td>
							  <input title="用户id" class="inputbox" style="width: 180px;" type="text" name="userId" id="userId" readonly="readonly" value='${demoBusinessTripMainDTO.userId}'/>
						</td>
						<th align="right">用户名称 :</th>
						<td>
							 <input title="用户名称" class="inputbox" style="width: 180px;" type="text" name="userName" id="userName" readonly="readonly" value='${demoBusinessTripMainDTO.userName}'/>
						</td>
					</tr>
					<tr>
						<th align="right">出差地址:</th>
						<td>
							  <input title="出差地址" class="inputbox" style="width: 180px;" type="text" name="address" id="address" readonly="readonly" value='${demoBusinessTripMainDTO.address}'/>
						</td>
						<th align="right">交通工具:</th>
						<td>
							 <input title="交通工具" class="inputbox" style="width: 180px;" type="text" name="traffic" id="traffic" readonly="readonly" value='${demoBusinessTripMainDTO.traffic}'/>
						</td>
					</tr>
					<tr>
						<th align="right">出差日期:</th>
						<td>
							  <input title="出差日期" class="easyui-datebox" style="width: 180px;" type="text" readonly="readonly" name="leavedate" id="leavedate"/>
						</td>
						<th align="right">返回日期:</th>
						<td>
							 <input title="返回日期" class="easyui-datebox" style="width: 180px;" type="text"readonly="readonly" name="backdate" id="backdate"/>
						</td>
					</tr>
					<tr>
						<th align="right">返回补助:</th>
						<td>
							 <input title="返回补助" class="easyui-numberbox" style="width: 180px;" type="text" name="allowance" id="allowance" readonly="readonly" value='${demoBusinessTripMainDTO.allowance}'/>
						</td>
					</tr>
				</table>
		</form>
		<!-- <div id="toolbar" class="datagrid-toolbar datagrid-toolbar-extend">
			<table class="tableForm" border="0" cellspacing="1" width='100%'>
				<tr>	
					<td width="50%" align="right"><a title="保存" id="saveButton"  class="easyui-linkbutton" iconCls="icon-save" plain="false" onclick="saveForm();" href="javascript:void(0);">保存</a></td>
					<td align="left"><a title="返回" id="returnButton"  class="easyui-linkbutton" iconCls="icon-undo" plain="false" onclick="closeForm();" href="javascript:void(0);">返回</a></td>
				</tr>
			</table>
		</div> -->
	</div>
	<script type="text/javascript">
	$(function(){
		var leavedate ='${demoBusinessTripMainDTO.leavedate}';
		if(leavedate){
			$('#leavedate').datebox('setValue',new Date(leavedate).format("yyyy-MM-dd"));
		}
		var backdate='${demoBusinessTripMainDTO.backdate}';
		if(backdate){
			$('#backdate').datebox('setValue',new Date(backdate).format("yyyy-MM-dd"));
		}
	})
	</script>
</body>
</html>