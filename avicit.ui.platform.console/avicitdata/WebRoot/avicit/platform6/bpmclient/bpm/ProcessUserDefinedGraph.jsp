<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<html>
<head>

<%
	String processInstanceId = request.getParameter("processInstanceId");
	String executionId = request.getParameter("executionId");
	String taskId = request.getParameter("taskId");
	String outcome = request.getParameter("outcome");
	String targetActivityName = request.getParameter("targetActivityName");
	String mes = "";
		mes = "请您选择一个要定义流程审批人的节点，选择审批人然后点击“提交”。";
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>流程自定义审批人</title>
<base href="<%=ViewUtil.getRequestPath(request) %>">
<jsp:include page="/avicit/platform6/bpmclient/bpm/ProcessCommonJsInclude.jsp"></jsp:include>
</head>
<script type="text/javascript">
var baseurl = '<%=request.getContextPath()%>';
var processInstanceId = '<%=processInstanceId%>';
var taskId = '<%=taskId%>';
var executionId = '<%=executionId%>';
var outcome = '<%=outcome%>';
var targetActivityName = '<%=targetActivityName%>';
var globalStepObject;
var stepMap = new Map();

$(function(){
	var div = $("#bpmImageDiv").width("100%").height("400").css({overflow:"auto",zindex:"360",position:"relative"});
	var img = $("#img").attr("src", "platform/bpm/clientbpmdisplayaction/getfiguretrack.gif?processInstanceId="+processInstanceId);
	img.appendTo(div);
	var data = 'processInstanceId=' + processInstanceId + '&type=search';
	ajaxRequest("POST",data,"platform/bpm/clientbpmoperateaction/douserdefined","json","drawlayer");
});

var arrayObj = new Array();
function draw(obj){
	var activityMap = obj.activity;
	if(activityMap == null){
		return ;
	}
	for(var key in activityMap){
		var activity = activityMap[key];
		var x = activity.left;
		var y = activity.top;
		var h = activity.height;
		var w = activity.width;
		var activityAlias = activity.activityAlias;
		var activityName = activity.activityName;
		var executionId = activity.executionId;
		arrayObj[arrayObj.length]=executionId;
		
		var tempActivityName = stepMap.get(activityName);
		if(tempActivityName!=null && tempActivityName!='undefined' && tempActivityName!=''){
			$("#bpmImageDiv").append("<div onclick=\"clickNode(this,'"+ activityName+"','"+activityAlias+"','"+executionId+"');\" style='background:url(static/images/platform/bpm/client/images/tick2.png) no-repeat center;cursor:pointer;z-index:400;position:absolute;border:3px solid blue;left:"+x+"px;top:"+y+"px;width:"+w+"px;height:"+h+"px;'></div>");
		}else{
			$("#bpmImageDiv").append("<div onclick=\"clickNode(this,'"+ activityName+"','"+activityAlias+"','"+executionId+"');\" style='background:url(static/images/platform/bpm/client/images/tick2.png) no-repeat center;cursor:pointer;z-index:400;position:absolute;left:"+x+"px;top:"+y+"px;width:"+w+"px;height:"+h+"px;'></div>");
		}
	}
}

var bc = false;
var target = "";

function clickNode(obj,activityName,activityAlias,executionId){
	globalStepObject = obj;
	var data = 'processInstanceId=' + processInstanceId + '&activityName=' + activityName +'&type=search';
	ajaxRequest("POST",data,"platform/bpm/clientbpmoperateaction/douserdefined","json","back");
}
function back(obj){
	if(obj!=null){
		var mySelectUser = obj.selectUser;
		if(mySelectUser==null || mySelectUser=='undefined'){
			mySelectUser = "";
		}
		douserdefined(processInstanceId,executionId,taskId,outcome,targetActivityName,obj.activityName,mySelectUser);
	}
}
/**
 * 自定义流程审批人选择目标节点后
 */
function douserdefined(procinstDbid,executionId,taskId,outcome,targetActivityName,activityName,mySelectUser){
	var para = "procinstDbid="+procinstDbid+"&executionId="+executionId+"&taskId="+taskId+"&outcome="+outcome+"&targetActivityName="+targetActivityName+"&mySelectUser="+ mySelectUser +"&type=dostepuserdefined&doSubmitUrl=" + escape("platform/bpm/clientbpmoperateaction/douserdefined") +  "&doSubmitCallEvent=backFinished&random="+Math.random();
	var usd = new UserSelectDialog("doadduserdialog","650","500",getPath() + "/platform/user/bpmSelectUserAction/main?"+encodeURI(para),"流程自定义审批选人窗口");
	var buttons = [{
		text:'提交',
		id : 'processSubimt',
		//iconCls : 'icon-submit',
		handler:function(){
			var frmId = $('#doadduserdialog iframe').attr('id');
			var frm = document.getElementById(frmId).contentWindow;
			var seleUserJson = frm.getSelectedResultDataJson();
			if(typeof(seleUserJson) != 'undefined'){
				var data = 'processInstanceId=' + processInstanceId + '&selectUserIds=' + seleUserJson +  '&activityName=' + activityName;
				ajaxRequest("POST",encodeURI(data),"platform/bpm/clientbpmoperateaction/douserdefined","json","backFinished");
				usd.close();
			}
		}
	}];
	usd.createButtonsInDialog(buttons);
	usd.show();
}

function backFinished(obj){
	if(obj != null){
		if(obj.isHaveUser=='true'){
			if(globalStepObject!=null){
				globalStepObject.style.border="3px solid blue";
			}
		}else{
			if(globalStepObject!=null){
				globalStepObject.style.border="3px solid white";
			}
		}
		$.messager.show({
			title : '提示',
			msg : obj.flag
		});
	}
}

function drawlayer(obj){
	if(obj != null){
		bpmSelectPersonList = obj.bpmSelectPersonList;
		for(var i=0;i<bpmSelectPersonList.length;i++){
			var stepId = bpmSelectPersonList[i].stepId;
			var selectUserId = bpmSelectPersonList[i].selectUserId;
			stepMap.put(stepId,selectUserId);
		}
	}
	ajaxRequest("POST","processInstanceId="+processInstanceId,"platform/bpm/clientbpmdisplayaction/getcoordinate","json","draw")
}
</script>
<body style="margin: 0px; overflow: hidden;">
	<div id="tip"
		style="position: relative; padding-left: 24px; background-color: #fdfcc2; border: solid 2px #ffe4c4; font-family: Microsoft YaHei; FONT-SIZE: 12px">
		<span
			style="margin: 0px 12px 0px 2px; width: 16px; height: 16px; left: 0px; top: 0px; position: absolute; background-image: url(static/images/platform/bpm/client/images/icon-info-small.gif)">
		</span> 
		<span><%=mes%></span>
	</div>
	<div id="bpmImageDiv">
	<img id="img"/>
</div>
</body>
</html>