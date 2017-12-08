<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<html>
<head>

<%
	String type = request.getParameter("type");
	String processInstanceId = request.getParameter("processInstanceId");
	String executionId = request.getParameter("executionId");
	String taskId = request.getParameter("taskId");
	String outcome = request.getParameter("outcome");
	String mes = "";
	if(type != null && type.equals("doglobaljump")){
		mes = "在有多个并发节点的情况下，请您先选择一个流程跳转的起始节点（点击红框标记的节点），然后再选择一个目标节点后执行流程跳转操作。";
	}
	if(type != null && type.equals("doglobalreader")){
		mes = "请您先选择一个要增加流程读者的节点，然后执行增加流程读者操作。";
	}
	if(type != null && type.equals("doglobaltransmit")){
		mes = "请您先选择一个要转发流程的节点，然后执行流程转发操作。";
	}
	if(type!=null && type.equals("dosupplement")){
		mes = "在有多个并发节点的情况下，请您先选择一个流程补发的节点（点击红框标记的节点），然后执行流程补发操作。";
	}
	if(type!=null && type.equals("dowithdraw")){
		mes = "在有多个并发节点的情况下，请您先选择一个流程拿回的节点（点击红框标记的节点），然后执行流程拿回操作。";
	}
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>流程跳转</title>
<base href="<%=ViewUtil.getRequestPath(request) %>">
<jsp:include page="/avicit/platform6/bpmclient/bpm/ProcessCommonJsInclude.jsp"></jsp:include>
</head>

<script type="text/javascript">
var baseurl = '<%=request.getContextPath()%>';
var type = '<%=type%>';
var processInstanceId = '<%=processInstanceId%>';
var taskId = '<%=taskId%>';
var executionId = '<%=executionId%>';
var curExecutionId = '<%=executionId%>';
var outcome = '<%=outcome%>';

$(function(){
	var div = $("#bpmImageDiv").width("100%").height("340").css({overflow:"auto",zindex:"300",position:"relative"});
	var img = $("#img").attr("src", "platform/bpm/clientbpmdisplayaction/getfiguretrack.gif?processInstanceId="+processInstanceId);
	img.appendTo(div);
	ajaxRequest("POST","processInstanceId="+processInstanceId,"platform/bpm/clientbpmdisplayaction/getcoordinate","json","draw");
	ajaxRequest("POST","processInstanceId="+processInstanceId,"platform/bpm/clientbpmdisplayaction/getforeachexecution","json","drawlayer");
});


var exeNameMap = new Map();

function drawlayer(obj){
	var exes = obj.foreachexe;
	if(exes == null){
		return ;
	}
	for (var name in exes){
		var eidmap = exes[name];
		var exeIdMap = new Map();
		for (var id in eidmap){
			var users = eidmap[id];
			exeIdMap.put(id,users);
		}
		exeNameMap.put(name,exeIdMap);
	}
}

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
		var isCurrent = activity.isCurrent;
		var executionId = activity.executionId;
		var isAlone = activity.executionAlone;
		var sameActName = activity.sameActName;

		if(isCurrent == "true"){
			arrayObj[arrayObj.length]=executionId;
			$("#bpmImageDiv").append("<div id=\""+executionId+"\" onclick=\"clickNode(this,'"+isCurrent+"','"+activityName+"','"+activityAlias+"','"+executionId+"','"+isAlone+"','"+sameActName+"',event);\" style='background:url(static/images/platform/bpm/client/images/tick2.png) no-repeat center;cursor:pointer;z-index:400;position:absolute;border:3px solid red;left:"+x+"px;top:"+y+"px;width:"+w+"px;height:"+h+"px;'></div>");
		}else{
			$("#bpmImageDiv").append("<div onclick=\"clickNode(this,'"+isCurrent+"','"+activityName+"','"+activityAlias+"','"+executionId+"','"+isAlone+"','"+sameActName+"');\" style='background:url(static/images/platform/bpm/client/images/tick2.png) no-repeat center;cursor:pointer;z-index:400;position:absolute;left:"+x+"px;top:"+y+"px;width:"+w+"px;height:"+h+"px;'></div>");
		}
		//只有一个当前节点时候补发操作和拿回操作自动处理
		
		if(isAlone && isCurrent == "true"){
			if(type == 'dosupplement'){
				parent.dosupplementActivitySelect(processInstanceId,executionId,taskId,outcome,activityName);
			} else if(type == 'dowithdraw'){
				parent.dowithdrawActivitySelect(processInstanceId,executionId,taskId,outcome,activityName);
			}
		}
	}
}

var bc = false;
var target = "";
var p_obj = "";
var p_isCurrent = "";
var p_activityName = "";
var p_activityAlias = "";
var p_executionId = "";
var p_isAlone = "";
var p_sameActName = "";
var p_event = "";

function clickNode(obj,isCurrent,activityName,activityAlias,executionId,isAlone,sameActName,event){
	p_obj = obj;
	p_isCurrent = isCurrent;
	p_activityName = activityName;
	p_activityAlias = activityAlias;
	p_executionId = executionId;
	p_isAlone = isAlone;
	p_sameActName = sameActName;
	p_event = event;
	
	if(isCurrent == "true" && sameActName == "false"){
		if(type == 'dosupplement'){
			dosupplement(executionId,activityName);
		}
		if(type == 'doglobaljump'){
			dojump(obj,isCurrent,activityName,activityAlias,executionId,isAlone);
		}
		if(type == 'dowithdraw'){
			dowithdraw();
		}
	}
	
	if(isCurrent == "true" && sameActName == "true"){
		
		if(type == 'dosupplement'){
			var eidmap = exeNameMap.get(activityName);
			drawTip(eidmap);
		}
		if(type == 'dowithdraw'){
			var eidmap = exeNameMap.get(activityName);
			drawTip(eidmap);
		}
		if(type == 'doglobaljump'){
			var eidmap = exeNameMap.get(activityName);
			drawTip(eidmap);
		}
	}
	
	if(isCurrent == "false" && sameActName == "false"){
		if(type == 'doglobaljump'){
			dojump(obj,isCurrent,activityName,activityAlias,executionId,isAlone);
		}
	}
	
	if(type == 'doglobalreader'){
		doreader(isCurrent,activityName,activityAlias);
	}

	if(type == 'doglobaltransmit'){
		dotransmit(isCurrent,activityName,activityAlias);
	}
}


function drawTip(eidmap){
	var keyts = eidmap.keys();
	$('#bpmImageDiv').append("<div id='div_tip' style='z-index:500px;background-color:#fdfcc2;position: absolute'></div>");
	var divObj = $("#div_tip").show();
	var tab = "";
	tab += "<table style=border-collapse:collapse;margin:3px 3px 3px 3px; cellpadding=3 cellspacing=3 border=1 width=350 >";
	tab += "<th width=40>分支</th>";
	tab += "<th>接收人</th>";
	tab += "<th width=50>操作</th>";
	tab += "<tr>";
	var num = 0;
	for (var k in keyts){
		var key = keyts[k];
		var value =	eidmap.get(keyts[k]);
		var uids = "";
		var unames = "";
		var b = 0;
		for (var u in value){
			var id = value[u].id;
			var name = value[u].name;
			b++;
			uids +=id+","; 
			unames +=name+","; 
			if(b>4){
				unames +="....";
				break;
			}
		}
		num++;
		tab += "<tr><td>分支"+replaceNull2Space(num)+"</td>";
		tab += "<td>"+unames+"</td>";
		tab += "<td><input type=submit  value=确定 id="+key+" onclick=clickExecutionSelect(this) /></td></tr>";
		if(key == curExecutionId){
			num++;
			//alert(key+"--&&&&&&999");
			dymanicSelect(key);
			return;
		}
	}
	tab +="</table>";
	divObj.css({left:getPointerX(p_event)+"px",top:getPointerY(p_event)+"px"});
	divObj.html(tab);
	if(startNodes.length>0){
		parent.dojumpActivitySelect(processInstanceId,curExecutionId,taskId,outcome,p_activityName);
	}else{
		//foreach:当前用户不能跳转不属于自己的execution
		//window.alert("");
	}
	//给出提示：请到待办处执行流程跳转操作
	//if(type == 'doglobaljump'){
		//alert(p_executionId+"$$"+p_activityName+"$$"+p_activityAlias);
		//dojump(p_obj,p_isCurrent,p_activityName,p_activityAlias,p_executionId,p_isAlone);
	//}
}


function dymanicSelect(executionId){
	
	if(type == 'doglobaljump'){
		dojump(p_obj,p_isCurrent,p_activityName,p_activityAlias,executionId,p_isAlone);
	}
	if(type == 'dosupplement'){
		parent.dosupplementActivitySelect(processInstanceId,executionId,taskId,outcome,t.name);
	}
	if(type == 'dowithdraw'){
		parent.dowithdrawActivitySelect(processInstanceId,executionId,taskId,outcome,t.name);
	}

}

function clickExecutionSelect(t){
	
	if(type == 'doglobaljump'){
		dojump(p_obj,p_isCurrent,p_activityName,p_activityAlias,t.id,p_isAlone);
	}
	if(type == 'dosupplement'){
		parent.dosupplementActivitySelect(processInstanceId,t.id,taskId,outcome,t.name);
	}
	if(type == 'dowithdraw'){
		parent.dowithdrawActivitySelect(processInstanceId,t.id,taskId,outcome,t.name);
	}
	
	$("#div_tip").hide();
}

function dosupplement(executionId,activityName){
	parent.dosupplementActivitySelect(processInstanceId,executionId,taskId,outcome,activityName);
}

function dowithdraw(){
	parent.dowithdrawActivitySelect(processInstanceId,p_executionId,taskId,outcome,p_activityName);
}

function dotransmit(isCurrent,activityName,activityAlias){
	var b = confirm("您选择了【"+activityAlias+"】为流程转发的目标节点，确定吗？");
	if(b){
		parent.doglobaltransmitActivitySelect(processInstanceId,executionId,taskId,outcome,activityName);
	}
}

function doreader(isCurrent,activityName,activityAlias){
	if(isCurrent==true||isCurrent=="true"){
		alert("流程未流程转节点不能作为流程读者的目标节点，请您重新选择。");
		return;
	}
	var b = confirm("您选择了【"+activityAlias+"】为流程读者的目标节点，确定吗？");
	if(b){
		parent.doreaderActivitySelect(processInstanceId,executionId,taskId,outcome,activityName);
	}
}



var startExecutionId = "";
var boolFlag = "";
var startNodes = new Array();

function validateDestActivity(processInstanceId,executionId,taskId,outcome,activityName){
	var data = "procinstDbid="+processInstanceId+"&executionId="+executionId+"&activityName="+activityName;
	return isMatchable("POST",data,"platform/bpm/clientbpmoperateaction/validateDestActivity","json","validateCallBack");
}
function validateCallBack(obj){
	//alert(obj.flag);
	if(obj!=null && obj.flag=="failed"){
		//boolFlag = "false";
	}
}
function dojump(obj,isCurrent,activityName,activityAlias,executionId,isAlone){
	//只有一个当前节点，默认是开始节点
	if(isAlone == "true"){
		if(isCurrent=="true"){
			obj.style.border="3px solid blue";
			//Added in 01-15
			startNodes.push(obj);
			return ;
		}
	}else{
		//Added in 01-20
		//foreach+group:admin用户从task2跳转 到task3后，保证dingrc用户也可以task2跳转到task3
		if(isCurrent=="true" && startNodes.length==0){
			//task3-->task2的跳转情况进行限制
			//if(startNodes.length>0){
			//	alert("无法跳转到所选节点，请您重新选择。");
			//	return;
			//}
				
			var b = confirm("您确定选择【"+activityAlias+"】为跳转的起始节点吗？");
			if(b){
				bc = true;
				target = obj;
				startExecutionId = executionId;
				obj.style.border="3px solid blue";
				startNodes.push(obj);
			}
			for (index in arrayObj){
				if(arrayObj[index] != executionId){
					$("#"+arrayObj[index]).css({border:"3px solid red"});
				}
			}
			return;
		}else{
			if(bc == false){
				alert("请您先选择一个当前节点作为流程跳转的起始节点。");
				return;
			}
		}
	}
	//Added in 01-16
	var rst;
	if(startExecutionId != ""){
		rst = validateDestActivity(processInstanceId,startExecutionId,taskId,outcome,activityName);
	}else{
		rst = validateDestActivity(processInstanceId,executionId,taskId,outcome,activityName);
	}
	if(rst.flag=="failed"){
		alert("无法跳转到所选节点，请您重新选择。");
		return;
	}
	var b = confirm("流程将跳转到【"+activityAlias+"】节点，您确定吗？");
	if(b){
		//分支跳转
		if(startExecutionId != ""){
			executionId = startExecutionId;
		}
		parent.dojumpActivitySelect(processInstanceId,executionId,taskId,outcome,activityName);
	}
}
function isMatchable(type, data, url, dataType, event) {
	var contextPath = getPath();
	var urltranslated = contextPath + "/" + url;
	var result = "";
	if(dataType != ''){
		jQuery.ajax({
			type : type,
			data : data,
			url : urltranslated,
			async: false,
			dataType : dataType,
			success : function(msg) {
				if(msg!=null&&msg.error!=null){ //失败
	    			window.alert("Ajax操作时发生异常，地址为：" + urltranslated + "，异常信息为：" + msg.error);
	    			return;
	    		}else{
	    			if(event!=null){
	    				eval(event + "(msg)");
	    			}
	    			result = msg;
	    		}
			},
			error: function(msg){
			//	window.alert("Ajax操作时发生异常，地址为：" + urltranslated + "，异常信息为：" + msg.responseText);
			}
		});
	}
	return result;
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