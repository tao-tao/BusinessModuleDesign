<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="avicit.platform6.commons.utils.ComUtil"%>
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<param name="wmode" value="transparent">
<title>历史性能信息</title>
<base href="<%=ComUtil.getRequestPath(request)%>">
<script src="static/js/platform/component/fusionchar/js/FusionCharts.js" type="text/javascript"></script>
<script src="avicit/platform6/component/js/jQuery/jQuery-1.8.2/jquery-1.8.2.js" type="text/javascript"></script>
</head>

<script type="text/javascript">
function showChart(){
	var data = "id=1";
	jQuery.ajax({type : 'POST',
		data : data,
		url : 'platform/console/dynaPropertyController/getChartData?para=<%= request.getParameter("para")%>',
		dataType : "text",
		success : function(msg) {
			var chart = new FusionCharts("avicit/platform6/modules/system/fusionchar/charts/StackedColumn3DLineDY.swf?ChartNoDataText=没有数据!","ChartId","90%","90%");
			chart.setDataXML(msg);	
			chart.render("chartdiv");
		}});

	}

</script>

<body onload="showChart()">
	<div id="chartdiv" align="center">请稍候...</div>
</body>
<script>

</script>
</html>