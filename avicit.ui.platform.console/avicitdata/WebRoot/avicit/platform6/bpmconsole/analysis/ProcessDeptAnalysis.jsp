<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>部门维度</title>
	<base href="<%=ViewUtil.getRequestPath(request) %>">
<jsp:include page="/avicit/platform6/bpmclient/bpm/ProcessCommonJsInclude.jsp"></jsp:include>
<script src="static/js/platform/component/fusionchar/js/FusionCharts.js" type="text/javascript"></script>
</head>

	<script type="text/javascript">
		$(function(){
			$('#processDeptAnalysis').tabs({
			    onSelect:function(title,index){
					if(index == 0){//待办完成最多TOP10
						doQuery("TodoCompleteTop10");
					}
				    if(index == 1){//流程待办积压数量最多TOP10
				    	doQuery("TodoStockTop10");
				    }
				    if(index == 2){//流程待办平均办理时间TOP10
				    	doQuery("TodoCompleteAverageTimeTop10");
				    }
				    if(index == 3){//流程启动数量最多TOP10
						doQuery("StartNumTop10");
				    }
			    }   
			});
			doQuery("TodoCompleteTop10");
		});
		
		function getTodoCompleteTop10Back(json) {
			var chart = new FusionCharts("static/js/platform/component/fusionchar/js/Pie3D.swf", "ChartId", "100%", "400", "0", "0");
			var strXML  = "<chart palette='2' caption='待办完成最多TOP10' xAxisName='用户' yAxisName='待办完成量 (单位：条)' baseFont='微软雅黑'  showValues='1' decimals='0' formatNumberScale='0'>";
			for (var i = 0; i < json.rows.length; i++) {
				strXML += "	<set label='"+json.rows[i].name+"' value='"+json.rows[i].value+"' link='JavaScript:openUserAnalysisAll(\"TodoCompleteTop10\", \""+json.rows[i].id+"\")'/>" ;
			}
			strXML += "</chart>";
			chart.setDataXML(strXML);
			chart.render("todoCompleteTop10");
		}
		
		function getTodoStockTop10Back(json) {
        	var chart = new FusionCharts("static/js/platform/component/fusionchar/js/Pie3D.swf", "ChartId", "100%", "400", "0", "0");
			var strXML  = "<chart palette='2' caption='流程待办积压数量最多TOP10' xAxisName='用户' yAxisName='待办未办理量 (单位：条)' baseFont='微软雅黑'  showValues='1' decimals='0' formatNumberScale='0'>";
			for (var i = 0; i < json.rows.length; i++) {
				strXML += "	<set label='"+json.rows[i].name+"' value='"+json.rows[i].value+"' link='JavaScript:openUserAnalysisAll(\"TodoStockTop10\", \""+json.rows[i].id+"\")'/>" ;
			}
				strXML += "</chart>";
			chart.setDataXML(strXML);
			chart.render("todoStockTop10");
		}
		
		function getTodoCompleteAverageTimeTop10Back(json) {
        	var chart = new FusionCharts("static/js/platform/component/fusionchar/js/Pie3D.swf", "ChartId", "100%", "400", "0", "0");
			var strXML  = "<chart palette='2' caption='流程待办平均办理时间TOP10' xAxisName='用户' yAxisName='待办平均办理时间 (单位：分/条)' baseFont='微软雅黑'  showValues='1' decimals='0' formatNumberScale='0'>";
			for (var i = 0; i < json.rows.length; i++) {
				strXML += "	<set label='"+json.rows[i].name+"' value='"+json.rows[i].value+"' link='JavaScript:openUserAnalysisAll(\"TodoCompleteAverageTimeTop10\", \""+json.rows[i].id+"\")'/>" ;
			}
				strXML += "</chart>";
			chart.setDataXML(strXML);
			chart.render("todoCompleteAverageTimeTop10");
		}
		
		function getStartNumTop10Back(json) {
        	var chart = new FusionCharts("static/js/platform/component/fusionchar/js/Pie3D.swf", "ChartId", "100%", "400", "0", "0");
			var strXML  = "<chart palette='2' caption='流程启动数量最多TOP10' xAxisName='用户' yAxisName='流程启动量 (单位：条)' baseFont='微软雅黑' showValues='1' decimals='0' formatNumberScale='0'>";
			for (var i = 0; i < json.rows.length; i++) {
				strXML += "	<set label='"+json.rows[i].name+"' value='"+json.rows[i].value+"' link='JavaScript:openUserAnalysisAll(\"StartNumTop10\", \""+json.rows[i].id+"\")'/>" ;
			}
				strXML += "</chart>";
			chart.setDataXML(strXML);
			chart.render("startNumTop10");
		}
		
		//查询
		function doQuery(tab){
		    var startDate =$("#"+tab+"Search input[comboname=startDate]").datetimebox('getValue');
		    var endDate = $("#"+tab+"Search input[comboname=endDate]").datetimebox('getValue');
		    var data = "startDate="+startDate+"&endDate="+endDate+"&querySign=top10";
		    ajaxRequest("POST",data,"platform/bpm/bpmconsole/processDeptAnalysisAction/get"+tab,"json","get"+tab+"Back");
		}
		
		//查询全部
		function doQueryAll(tab){
			var startDate =$("#"+tab+"Search input[comboname=startDate]").datetimebox('getValue');
			var endDate =$("#"+tab+"Search input[comboname=endDate]").datetimebox('getValue');
		    var url  = getPath()+"/avicit/platform6/bpmconsole/analysis/ProcessDeptAnalysisAll.jsp";
			var title = "统计信息";
			if (tab == 'TodoCompleteTop10') {
				title = "部门待办完成数量";
			} else if (tab == 'TodoStockTop10') {
				title = "部门待办积压数量";
			} else if (tab == 'TodoCompleteAverageTimeTop10') {
				title = "部门待办平均办理时间";
			} else {
				title = "部门流程启动数量";
			}
				url += "?startDate="+startDate+"&endDate="+endDate+"&querySign=all"+"&tab="+tab+"&title="+title;
			top.addTab(title,url,"dorado/client/skins/~current/common/icons.gif","ProcessUserAnalysisAll"," -0px -120px");
		}
		
		//清空
		function doReset(id){
			$("#"+id+"SearchForm input").val('');
		}
		
		//用户维度分析
		function openUserAnalysisAll(tab, id) {
			var url  = getPath()+"/avicit/platform6/bpmconsole/analysis/ProcessUserAnalysisAll.jsp";
			var startDate =$("#"+tab+"Search input[comboname=startDate]").datetimebox('getValue');
			var endDate =$("#"+tab+"Search input[comboname=endDate]").datetimebox('getValue');
			var title = "统计信息";
			if (tab == 'TodoCompleteTop10') {
				title = "用户待办完成数量";
			} else if (tab == 'TodoStockTop10') {
				title = "用户待办积压数量";
			} else if (tab == 'TodoCompleteAverageTimeTop10') {
				title = "用户待办平均办理时间";
			} else {
				title = "用户流程启动数量";
			}
				url += "?startDate="+startDate+"&endDate="+endDate+"&querySign=all"+"&tab="+tab+"&title="+title+"&deptId="+id;
			top.addTab(title,url,"dorado/client/skins/~current/common/icons.gif","ProcessUserAnalysisAll"," -0px -120px");
		}
		
	</script>
	
	<body>
		<div class="easyui-tabs" id="processDeptAnalysis">
			<div title="流程待办完成最多TOP10" style="padding:10px;width:auto">
				<div id="TodoCompleteTop10Search" class="datagrid-toolbar" style="height: auto;display: block;">
					<fieldset>
						<legend>查询条件</legend>
						<table class="tableForm" id="TodoCompleteTop10SearchForm">
							<tr>
							    <td style="width:200px"></td>
								<td>开始日期(起)</td>
								<td colspan="2">
									<input name="startDate" id="startDate"  class="easyui-datebox" editable="false" style="width: 150px;" />
								</td>
								<td style="width:50px"></td>
								<td>结束日期(止)</td>
								<td colspan="2">
									<input name="endDate" id="endDate"  class="easyui-datebox" editable="false" style="width: 150px;" />
								</td>
								<td style="width:50px"></td>
								<td >
									<a class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="doQuery('TodoCompleteTop10');" href="javascript:void(0);">查询</a>
									<a class="easyui-linkbutton" iconCls="icon-cancel" plain="true" onclick="doReset('TodoCompleteTop10');" href="javascript:void(0);">清空</a>
								</td>
							</tr>
						</table>
					</fieldset>
				</div>
				<div id="todoCompleteTop10"></div>
				<div><img src="static/images/platform/common/icons/table.png" title="列表统计" onclick="doQueryAll('TodoCompleteTop10')" style="cursor:pointer"/></div>
			</div>
			<div title="流程待办积压数量最多TOP10" style="padding:10px;width:auto">
				<div id="TodoStockTop10Search" class="datagrid-toolbar" style="height: auto;display: block;">
					<fieldset>
						<legend>查询条件</legend>
						<table class="tableForm" id="TodoStockTop10SearchForm">
							<tr>
							    <td style="width:200px"></td>
								<td>开始日期(起)</td>
								<td colspan="2">
									<input name="startDate" id="startDate"  class="easyui-datebox" editable="false" style="width: 150px;" />
								</td>
								<td style="width:50px"></td>
								<td>结束日期(止)</td>
								<td colspan="2">
									<input name="endDate" id="endDate"  class="easyui-datebox" editable="false" style="width: 150px;" />
								</td>
								<td style="width:50px"></td>
								<td >
									<a class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="doQuery('TodoStockTop10');" href="javascript:void(0);">查询</a>
									<a class="easyui-linkbutton" iconCls="icon-cancel" plain="true" onclick="doReset('TodoStockTop10');" href="javascript:void(0);">清空</a>
								</td>
							</tr>
						</table>
					</fieldset>
				</div>
				<div id="todoStockTop10"></div>
				<div><img src="static/images/platform/common/icons/table.png" title="列表统计" onclick="doQueryAll('TodoStockTop10')" style="cursor:pointer"/></div>
			</div>
			<div title="流程待办平均办理时间TOP10" style="padding:10px;width:auto">
				<div id="TodoCompleteAverageTimeTop10Search" class="datagrid-toolbar" style="height: auto;display: block;">
					<fieldset>
						<legend>查询条件</legend>
						<table class="tableForm" id="TodoCompleteAverageTimeTop10SearchForm">
							<tr>
							    <td style="width:200px"></td>
								<td>开始日期(起)</td>
								<td colspan="2">
									<input name="startDate" id="startDate"  class="easyui-datebox" editable="false" style="width: 150px;" />
								</td>
								<td style="width:50px"></td>
								<td>结束日期(止)</td>
								<td colspan="2">
									<input name="endDate" id="endDate"  class="easyui-datebox" editable="false" style="width: 150px;" />
								</td>
								<td style="width:50px"></td>
								<td >
									<a class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="doQuery('TodoCompleteAverageTimeTop10');" href="javascript:void(0);">查询</a>
									<a class="easyui-linkbutton" iconCls="icon-cancel" plain="true" onclick="doReset('TodoCompleteAverageTimeTop10');" href="javascript:void(0);">清空</a>
								</td>
							</tr>
						</table>
					</fieldset>
				</div>
				<div id="todoCompleteAverageTimeTop10"></div>
				<div><img src="static/images/platform/common/icons/table.png" title="列表统计" onclick="doQueryAll('TodoCompleteAverageTimeTop10')" style="cursor:pointer"/></div>
			</div>
			<div title="流程启动数量最多TOP10" style="padding:10px;width:auto">
				<div id="StartNumTop10Search" class="datagrid-toolbar" style="height: auto;display: block;">
					<fieldset>
						<legend>查询条件</legend>
						<table class="tableForm" id="StartNumTop10SearchForm">
							<tr>
							    <td style="width:200px"></td>
								<td>开始日期(起)</td>
								<td colspan="2">
									<input name="startDate" id="startDate"  class="easyui-datebox" editable="false" style="width: 150px;" />
								</td>
								<td style="width:50px"></td>
								<td>结束日期(止)</td>
								<td colspan="2">
									<input name="endDate" id="endDate"  class="easyui-datebox" editable="false" style="width: 150px;" />
								</td>
								<td style="width:50px"></td>
								<td >
									<a class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="doQuery('StartNumTop10');" href="javascript:void(0);">查询</a>
									<a class="easyui-linkbutton" iconCls="icon-cancel" plain="true" onclick="doReset('StartNumTop10');" href="javascript:void(0);">清空</a>
								</td>
							</tr>
						</table>
					</fieldset>
				</div>
				<div id="startNumTop10"></div>
				<div><img src="static/images/platform/common/icons/table.png" title="列表统计" onclick="doQueryAll('StartNumTop10')" style="cursor:pointer"/></div>
			</div>
		</div>
	</body>
	
</html>