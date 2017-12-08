<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<html>
<head>
	<base href="<%=ViewUtil.getRequestPath(request)%>">
<jsp:include page="/avicit/platform6/bpmclient/bpm/ProcessCommonJsInclude.jsp"></jsp:include>
	<title>添加流程表单</title>
	<style>
		body td{
			font-family: Microsoft Yahei,sans-serif,Arial,Helvetica;
			font-size: 12px;
			padding-left: 0.5em;
		}
	</style>
</head>

<body class="easyui-layout" fit="true">
     <div region="center" border="false" style="overflow: hidden;">
       <form id="form1" method="post">
        <table class="tableForm" width="100%" border=0>
				<tr>
					<td >表单代码</td>
					<td ><input  class="easyui-validatebox"  id="formCode"  name="formCode" required="true" style="width:450px;" value="${bpmForms.formCode }"/></td>
					</td>
				</tr>
				<tr>
					<td>表单名称</td>
					<td ><input  class="easyui-validatebox"  id="formName"  name="formName"  required="true" style="width:450px;" value="${bpmForms.formName }"/></td>
				</tr>
				<tr>
					<td >表单URL</td>
					<td>
					<input  class="easyui-validatebox"  id="formUrl"  name="formUrl"  required="true" style="width:450px;" value="${bpmForms.formUrl }"/>
					<input name="id" id="id"  style="display:none;" value="${bpmForms.id }" />
					</td>
				</tr>
			</table>
		</form>
	</div>
	
</body>
</html>

