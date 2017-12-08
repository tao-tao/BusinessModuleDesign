<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<html>
<head>
<%
	String waringType = request.getParameter("waringType");
	String isShowSendType01 = request.getParameter("isShowSendType01");//是否显示给处理人发邮件选项
	String begin =  request.getParameter("begin");
	String end = request.getParameter("end");
%>

	<base href="<%=ViewUtil.getRequestPath(request)%>">
<jsp:include page="/avicit/platform6/bpmclient/bpm/ProcessCommonJsInclude.jsp"></jsp:include>
	<title>流程预警</title>
	<style>
		body td{
			font-family: Microsoft Yahei,sans-serif,Arial,Helvetica;
			font-size: 12px;
			padding-left: 0.5em;
		}
	</style>
	<script type="text/javascript">
		$(function(){
		   init();
	    });
	   
	   	function init(){
		   ajaxRequest("POST","","platform/bpm/bpmconsole/processUserAnalysisAction/getProcessWaring?waringType=<%=waringType%>","json","initProcessWaring");
	   	}
	   
	   	function  initProcessWaring(retValue){
	    	var bpmWaringVo=retValue.obj;
	    	$("#dbid").attr("value",bpmWaringVo.dbid); 
	    	$("#value").attr("value",bpmWaringVo.value); 
	    	$("#otherMails").attr("value",bpmWaringVo.otherMails); 
	    	//$("#jobRule").attr("value",bpmWaringVo.jobRule); 
	    	//$("#jobClass").attr("value",bpmWaringVo.jobClass); 
	    	if (bpmWaringVo.sendType01 == "on") document.getElementById("sendType01").checked = true;
	    	if (bpmWaringVo.sendType02 == "on") document.getElementById("sendType02").checked = true;
	    }
	</script>
</head>

<body class="easyui-layout" fit="true">
	<div region="center" border="false" style="overflow: hidden;">
		<form id="processWaringForm" method="post">
        	<table class="tableForm" width="100%" border=0>
				<tr>
					<td>
						<input name="dbid"  id="dbid" style="display:none;" />
						<input name="waringType"  id="waringType" value="<%=waringType%>" style="display:none;" />
					</td>
				</tr>
	
				<tr>
					<td><h3><%=begin%><input name="value" id="value" class="easyui-validatebox" style="width:80px;"></input><%=end%></h3></td>
				</tr>


				<%
					if(isShowSendType01!=null&&isShowSendType01.equals("no")){//不显示		
					}else{
				%>
				<tr>
					<td><span><input name="sendType01" id="sendType01" type="checkbox">&nbsp;给流程处理人发邮件</span></td>
				</tr>
				<%	
					}
				%>

				<tr>
				    <td><span><input name="sendType02" id="sendType02" type="checkbox">&nbsp;给以下人员发邮件，多人以;分割</span></td>
				</tr>
				<tr>
					<td><textarea name="otherMails" id="otherMails" class="easyui-validatebox" style="height:60px;width:400px;"></textarea></td>
				</tr>
				
				<!-- 暂时隐藏掉自身的调度用系统自带调度
				<tr>
					<td>定时任务规则&nbsp;&nbsp;&nbsp;&nbsp;<input name="jobRule" id="jobRule" class="easyui-validatebox" style="width:312px;"></input></td>
				</tr>
				<tr>
					<td>定时任务处理类&nbsp;<input name="jobClass" id="jobClass" class="easyui-validatebox" style="width:312px;"></input></td>
				</tr>
				 -->
			</table>
		</form>
	</div>
</body>
</html>

