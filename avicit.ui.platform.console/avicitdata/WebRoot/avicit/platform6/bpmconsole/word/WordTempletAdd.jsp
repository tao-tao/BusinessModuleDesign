<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<html>
<head>
	<%
		String id = request.getParameter("id");
	%>
	
	<base href="<%=ViewUtil.getRequestPath(request)%>">
<jsp:include page="/avicit/platform6/bpmclient/bpm/ProcessCommonJsInclude.jsp"></jsp:include>
	<title>添加正文模板</title>
	<style>
		body td{
			font-family: Microsoft Yahei,sans-serif,Arial,Helvetica;
			font-size: 12px;
			padding-left: 0.5em;
		}
	</style>
	<script type="text/javascript">
	var baseurl = '<%=request.getContextPath()%>';
		$(function(){
			ajaxRequest("POST","id=<%=id%>","platform/bpm/bpmconsole/wordTempletAction/getWordTemplet","json","getWordTempletBack");
	    });
	   
	   	function getWordTempletBack(retValue){
	    	var wordTemplet=retValue.obj;
	    	$("#id").attr("value",wordTemplet.id); 
	    	$("#templetName").attr("value",wordTemplet.templetName);
	    	$('#templetType').combobox('setValue',wordTemplet.templetType); 
	    	$("#templetVersion").attr("value",wordTemplet.templetVersion); 
	    	$('#templetState').combobox('setValue',wordTemplet.templetState); 
	    	$("#orderBy").attr("value",wordTemplet.orderBy); 
	    	$("#flowKey").attr("value",wordTemplet.flowKey); 
	    	$("#version").attr("value",wordTemplet.version);
	    	$("#createdBy").attr("value",wordTemplet.createdBy); 
	    	$("#creationDate").datebox("setValue",new Date(wordTemplet.creationDate).Format("yyyy-MM-dd hh:mm:ss")); 
	    }
	</script>
</head>

<body class="easyui-layout" fit="true">
	<div region="center" border="false" style="overflow: hidden;">
		<form id="form1" method="post">
        	<table class="tableForm" width="100%" border=0>
				<tr>
					<td >模板名称</td>
					<td ><input id="templetName"  name="templetName" required="true" class="easyui-validatebox"/></td>
					<td >模板类型</td>
					<td >
						<select id="templetType" name="templetType" class="easyui-combobox" style="width:155px;" >
						  <option value="1">正文模板</option>
						  <option value="2">红头模板</option>
						</select>
					</td>
				</tr>
				<tr>
					<td >模板版本</td>
					<td ><input id="templetVersion"  name="templetVersion" required="true" class="easyui-validatebox"/></td>
					<td >模板状态</td>
					<td >
						<select id="templetState" name="templetState" class="easyui-combobox" style="width:155px;" >
						  <option value="Y">有效</option>
						  <option value="N">无效</option>
						</select>
					</td>
				</tr>
				<tr>
					<td >模板排序</td>
					<td ><input id="orderBy"  name="orderBy"/></td>
					<td ></td>
					<td >
						<input id="id"  name="id" style="display:none;"/>
						<input id="version"  name="version" style="display:none;"/>
						<input id="createdBy"  name="createdBy" style="display:none;"/>
						<input id="creationDate"  name="creationDate" style="display:none;"/>
					</td>
				</tr>
				<tr>
					<td >所属流程</td>
					<td colspan="3"> 
						<textarea name="flowKey" id="flowKey" class="easyui-validatebox" style="height:60px;width:480px;"></textarea>
					</td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>

