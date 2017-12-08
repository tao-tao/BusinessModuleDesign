
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">

<%@page language="java"  pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@page import="avicit.platform6.commons.utils.ViewUtil"%>
<%@page import="avicit.platform6.api.sysshirolog.impl.AfterLoginSessionProcess"%>
<html>
<head>
 <meta http-equiv="X-UA-Compatible" content="chrome=1">
<%
	String isgloable = request.getParameter("isgloable");
	if(isgloable==null||isgloable.equals("")){
		isgloable = "false";
	}
	String sysPortletConfigId = request.getParameter("sysPortletConfigId");
	//add by xingc
	String skinColor = (String)request.getSession().getAttribute(AfterLoginSessionProcess.SESSION_CURRENT_USER_SKIN);
%>
<link rel="stylesheet" href="portal/lib/themes/flick/jquery-ui-1.10.4.custom.css" />
<link rel="stylesheet" href="portal/css/jquery.portlet.css?v=1.3.1" />
<!-- add by xingc-->
<link href="${pageContext.request.contextPath}/static/css/platform/themes/<%=skinColor %>/portlet/portlet.css" rel="stylesheet" type="text/css">

<script src="portal/lib/jquery-1.8.3.min.js" type="text/javascript"></script>
<script src="portal/lib/jquery-ui-1.10.4.custom.min.js" type="text/javascript"></script>
<script src="portal/script/jquery.portlet.js" type="text/javascript"></script>
<script type="text/javascript">
var baseUrl = "<%=ViewUtil.getRequestPath(request)%>";
//用于判断是个人首页还是首页布局模块中的首页
var isgloable = "<%=isgloable%>";
var xy="0:0";
var conX=0;//当前模型最大X坐标
var jsonList=[];//用于存储当前页面portlet位置信息
//获取portlet的位置信息,被首页调用
function getPortletInfo(id){
	 var indexs = $('#'+id).portlet('index');
	 return indexs;
}

//判断模版最大X坐标是否改变
function layoutChangeFlag(con){
	if (con!=conX)
		return true;
	else
		return false;
}

function max(array){
	return Math.max.apply(Math, array);
}

//布局更改时调用，从新计算小页坐标
function saveNewPortlet(portletList,layout,newConX){
	var portletStr = "";
	var x=0;
	var y=0;
	if(portletList!=null&&portletList.length!=null){
		//动态增加portlet
		for (var i = 0; i < portletList.length; i++) {
			var portlet = portletList[i];
			portletStr = portletStr + portlet.rowId+";"+portlet.portletId+";"+x+":"+y+"@";
			if (x!=newConX){
				x++;
			}else{
				x=0;
				y++;
			}
		}
		portletStr = portletStr.substring(0,portletStr.length-1);
	}
	$.ajax({ 
		url: '<%=ViewUtil.getRequestPath(request)%>platform/IndexPortalController/saveIndexPortlet.json',
		async : false,
		type: "POST",
		data : 'isgloable='+isgloable+'&sysPortletConfigId=<%=sysPortletConfigId%>&portlet=' + portletStr+'&layout='+layout,
		success: function(){
		},
		error : function(){
			//alert('portlet配置信息保存失败!');
		}
	});
}

//查找页面上空余位置
function findPosition(){
	if (jsonList.length==0)return "";
//y轴循环
for (var j=0; true; j++){
	//x轴循环
	for (var i=0;i<=conX; i++){
	 	var flag = false;
	 	//如果当前位置上有portlet则进行下一位置循环，直到当前位置上没有portlet，返回当前位置
		 for (var k=0;k<jsonList.length;k++){
			if (jsonList[k].x==i&&jsonList[k].y==j){
				flag = true;
				break;
			 } 
			 
		 }
		 if (flag == false){
			 return i+':'+j;
		 }
	 }
}
}
//公共方法，增加portlet
function realAddPortlet(portlet,rowId){
	var x1 = portlet.xy.split(":")[1];
	var y1 = portlet.xy.split(":")[0];
	$('#'+rowId).portlet('option', 'add', {
        position:  {x: x1,y: y1},
        portlet: {
            attrs: {id: portlet.portletId},
            title: portlet.title,
            isClose: portlet.isClose,
            isShowTitle: portlet.isShowTitle,
            refreshTime: portlet.refreshTime,
            content: {
                style: {height: portlet.height},
                type: 'text',
                text: '<iframe src="'+portlet.url+'" width="100%" height="'+portlet.height+'" border="0" frameBorder="0" scrolling="auto" vspace="0" hspace="0" bordercolor="#000000"></iframe>'
            }
        }
    });
}
//添加首页应用，被首页调用
function addPortlet(ids){
	var idArray = ids.split(",");
	for(var i=0; i<idArray.length; i++){
		if(idArray[i]==null||idArray[i]==""){
			continue;
		}
		var indexs = $('#layout_row_1').portlet('index');

		//遇到相同id的先删除再添加进json数组，没有的直接添加
		 $.each(indexs, function(k, v) {
			 for (var i=0;i<jsonList.length;i++){
		        if (jsonList[i].id == k) {
		        	jsonList.splice(i, 1);
		           	break;
		        }
			 }
			jsonList.push({"x":v.x,"y":v.y,"id":k});
		}); 
		xy = findPosition();
		
		jsonList.push({"x":xy.split(":")[1],"y":xy.split(":")[0],"id":idArray[i]});
		$.ajax({
			url : '<%=ViewUtil.getRequestPath(request)%>platform/IndexPortalController/getPortletInfo.json?portletId='+idArray[i]+'&baseUrl='+baseUrl+'&xy='+xy,
			data : {},
			type : 'post',
			dataType : 'json',
			async:false,
			success : function(result) {
				if (result.flag == "success") {
					var portlet = result.portletModel;
					realAddPortlet(portlet,"layout_row_1");
				}
			}
		});
	}
}
//刷新页面
function reload(){
	window.location.reload();
}
//加载portlet
function loadPortlet(){
	$.ajax({
		url : baseUrl+'platform/IndexPortalController/getIndexPortlet.json',
		data : {'isgloable':'<%=isgloable%>','sysPortletConfigId':'<%=sysPortletConfigId%>','baseUrl':baseUrl},
		type : 'post',
		dataType : 'json',
		success : function(result) {
			if (result.flag == "success") {
				$("#layout").val(result.layout);
				var portletList = result.portletList;
				var layoutList = result.layoutList;
				var newConX = 0;
				var layoutId = "";
				if(layoutList!=null&&layoutList.length!=null){
					//动态画布局行div
					for (var i = 0; i < layoutList.length; i++) {
						var layout = layoutList[i];
						var rowWidthList = layout.columWidth;
						var columsArray = new Array();
						//初始化conX
						if (conX == 0 ){
							conX = rowWidthList.length-1;
						}
						//模版变化后conX
						newConX = rowWidthList.length-1;
						layoutId = layout.layoutId;
						for(var j=0; j<rowWidthList.length; j++){
							columsArray[j] = {width: rowWidthList[j],portlets: []};
						}
						if($("#"+layout.rowId).length==0){//判断是否已经存在
							$("#portalContent").append($("<div id='"+layout.rowId+"'></div>"));
						}
				        $('#'+layout.rowId).portlet({
				            sortable: true, //是否可以拖动
				            singleView: false, //单视图模式，不好用，禁用掉
				            columns: columsArray
				        });
					}
				}
				//判断conX是否变化，是则从新计算小页位置
				if (layoutChangeFlag(newConX)){
					conX = newConX;
					saveNewPortlet(portletList,layoutId,newConX);
				}else{
					conX = newConX;
					if(portletList!=null&&portletList.length!=null){
						//动态增加portlet
						for (var i = 0; i < portletList.length; i++) {
							var portlet = portletList[i];
							realAddPortlet(portlet,portlet.rowId);
						}
					}
				}
			}
		}
	});
}
$(function() {
	loadPortlet();
});
</script>
</head>
	<!--此处不能出现body,否则会引起首页空白处双击IE崩溃,原因不明？？？-->
	<div id='portalContent'></div>
	<input type="hidden" name="layout" id="layout">
</html>