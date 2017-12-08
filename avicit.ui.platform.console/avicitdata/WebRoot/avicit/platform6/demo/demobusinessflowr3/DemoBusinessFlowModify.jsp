<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
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
.required-icon {
	maring:0px;
	padding:0px;
	width: 10px;
	height: 23px;
	overflow: hidden;
	display: inline-block;
	vertical-align:-4px;
	/* opacity: 0.6; */
/* 	filter: alpha(opacity=60); */
	background: url('avicit/platform6/modules/system/sysuser/view/required.gif') no-repeat center center;
}
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
			<input type="hidden" name="id" value='${demoBusinessFlowDTO.id}'/>
				<table width="100%" style="padding-top: 10px;">
					<tr>
						<th align="right">出差人ID:</th>
						<td>
							  <input title="用户ID" class="inputbox" style="width: 180px;" type="text" name="userid" id="userid" value='${demoBusinessFlowDTO.userid}'/>
						</td>
																	
						<th align="right">出差人姓名:</th>
						<td>
							  <input title="用户名称" class="inputbox" style="width: 180px;" type="text" name="username" id="username" value='${demoBusinessFlowDTO.username}'/>
						</td>
																	
					</tr>
					<tr>
						<th align="right">出差地址:</th>
						<td>
							  <input title="出差地址" class="inputbox" style="width: 180px;" type="text" name="address" id="address" value='${demoBusinessFlowDTO.address}'/>
						</td>
																	
						<th align="right">出差事由:</th>
						<td>
							  <input title="出差事由" class="inputbox" style="width: 180px;" type="text" name="matter" id="matter" value='${demoBusinessFlowDTO.matter}'/>
						</td>
																	
					</tr>
					<tr>
						<th align="right">出差借款:</th>
						<td>
							  <input title="出差借款" class="inputbox" style="width: 180px;" type="text" name="lendmoney" id="lendmoney" value='${demoBusinessFlowDTO.lendmoney}'/>
						</td>
																	
						<th align="right">交通工具:</th>
						<td>
							  <input title="交通工具" class="inputbox" style="width: 180px;" type="text" name="traffic" id="traffic" value='${demoBusinessFlowDTO.traffic}'/>
						</td>
																	
					</tr>
					<tr>
						<th align="right">出差日期:</th>
						<td>
							  <input title="出差日期" class="easyui-datebox" style="width: 180px;" type="text" name="leavedate" id="leavedate" value="<fmt:formatDate value='${demoBusinessFlowDTO.leavedate}' pattern='yyyy-MM-dd' />"/>
						</td>
																	
						<th align="right">返回日期:</th>
						<td>
							  <input title="返回日期" class="easyui-datebox" style="width: 180px;" type="text" name="backdate" id="backdate" value="<fmt:formatDate value='${demoBusinessFlowDTO.backdate}' pattern='yyyy-MM-dd' />"/>
						</td>
																	
					</tr>
					<tr>
						<th align="right">出差总结:</th>
						<td>
							  <input title="出差总结" class="inputbox" style="width: 180px;" type="text" name="summary" id="summary" value='${demoBusinessFlowDTO.summary}'/>
						</td>
																	
						<th align="right">出差补助:</th>
						<td>
							  <input title="出差补助" class="inputbox" style="width: 180px;" type="text" name="allowance" id="allowance" value='${demoBusinessFlowDTO.allowance}'/>
						</td>
																	
					</tr>
					<tr>
					</tr>
				</table>
		</form>
		<div id="toolbar" class="datagrid-toolbar datagrid-toolbar-extend">
			<table class="tableForm" border="0" cellspacing="1" width='100%'>
				<tr>	
					<td width="60%" align="right">
					<a title="保存" id="saveButton"  class="easyui-linkbutton" onclick="saveForm();" href="javascript:void(0);">保存</a>
					<a title="返回" id="returnButton"  class="easyui-linkbutton" onclick="closeForm();" href="javascript:void(0);">返回</a>
					</td>
				</tr>
			</table>
		</div>
	</div>
	<script type="text/javascript">
	$(function(){
		
	})
	function closeForm(){
		parent.demoBusinessFlow.closeDialog("#edit");
	}
	function saveForm(){
		parent.demoBusinessFlow.save(serializeObject($('#form')),"#edit");
	}
	</script>
</body>
</html>