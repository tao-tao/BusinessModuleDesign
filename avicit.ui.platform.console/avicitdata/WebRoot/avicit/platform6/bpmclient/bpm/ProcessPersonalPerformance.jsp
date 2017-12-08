<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<%@page import="avicit.platform6.commons.utils.ComUtil"%>
<%@ page import="avicit.platform6.api.session.SessionHelper"%>
<html>
<head>
<%
	String path = request.getContextPath();
	String userId = SessionHelper.getLoginSysUserId(request);
	String startDate = ComUtil.Date2String(new Date());
	String endDate = ComUtil.Date2String(new Date());
%>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>流程个人绩效看板</title>
<base href="<%=ViewUtil.getRequestPath(request) %>">
<jsp:include page="/avicit/platform6/bpmclient/bpm/ProcessCommonJsInclude.jsp"></jsp:include>
<script src="static/js/platform/component/fusionchar/js/FusionCharts.js" type="text/javascript"></script>


</head>

<script type="text/javascript">
	//页面初始化
	$(function(){
		var data = "userId=<%=userId%>";
		ajaxRequest("POST",data,"platform/bpm/clientbpmdisplayaction/getProcessPersonalPerformance","json","getProcessPersonalPerformance");
	});
	
	//得到流程信息
	function getProcessPersonalPerformance(json){
		var strXML = "";
		for (var i = 0; i < json.rows.length; i++) {
			var obj = json.rows[i];
			if (i == 0) {
				$.each(obj,function(key,value){   
					var key01 = key.split("@")[0];
					var key02 = key.split("@")[1];
					var key03 = key.split("@")[2];
					
					strXML += "	<div style='width: 260px;margin-top:10px;cursor:pointer;' onclick='openProcessDetailPage(\""+key02+"\")'> " ;
					strXML += "		<div style='width:70px; float:left'>&nbsp;"+key01+"：</div> " ;
					strXML += "		<div class='Bar' style='width:180px;float:left'> " ;
					strXML += "			<div style='width: "+key03+";'> " ;
					strXML += "				<span>"+value+"</span> " ;
					strXML += "			</div> " ;
					strXML += "		</div> " ;
					strXML += "	</div> " ;
			    });   
			} else {
				$.each(obj,function(key,value){   
					var key01 = key.split("@")[0];
					var key02 = key.split("@")[1];
					var key03 = key.split("@")[2];
					
					strXML += "	<div style='width: 260px;padding-top:30px;'> " ;
					strXML += "		<div style='width:70px; float:left'>&nbsp;"+key01+"：</div> " ;
					strXML += "		<div class='Bar' style='width:180px;float:left;cursor:pointer;' onclick='openProcessDetailPage(\""+key02+"\")'> " ;
					strXML += "			<div style='width: "+key03+";'> " ;
					strXML += "				<span>"+value+"</span> " ;
					strXML += "			</div> " ;
					strXML += "		</div> " ;
					strXML += "	</div> " ;
			    });   
			}
		}
		document.getElementById("processPersonalPerformance").innerHTML = strXML;
	}
	
	function openProcessDetailPage(key) {
		if (key == "Start") {
			var url = "avicit/platform6/bpmclient/bpm/MyProcessInstanceList.jsp?tab="+key+"&startDate=<%=startDate%>&endDate=<%=endDate%>";
			top.addTab("我的流程",url,"dorado/client/skins/~current/common/icons.gif","MyProcessInstanceList"," -180px -140px");
		} else {
			var url = "avicit/platform6/bpmclient/bpm/ProcessWork.jsp?tab="+key+"&startDate=<%=startDate%>&endDate=<%=endDate%>";
			top.addTab("我的工作",url,"dorado/client/skins/~current/common/icons.gif","MyProcessInstanceList"," -180px -140px");
		}
	}
</script>
<body class="easyui-layout" fit="true">
	<div style="height:100%;overflow:auto;" >
		<div id="processPersonalPerformance" style="width: 260px;"></div>  
	</div>  
</body>
</html>