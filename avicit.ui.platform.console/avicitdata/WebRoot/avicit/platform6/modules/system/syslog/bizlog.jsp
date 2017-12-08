<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="avicit.platform6.commons.utils.ComUtil"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>日志审计信息</title>
<base href="<%=ComUtil.getRequestPath(request) %>">
<link href="avicit/platform6/modules/system/sysdept/css/icon.css" type="text/css" rel="stylesheet">
<jsp:include page="/avicit/platform6/component/common/EasyUIJsInclude.jsp"></jsp:include>
<jsp:include page="/avicit/platform6/modules/system/commonpopup/commonSelectionHead.jsp"></jsp:include>
<script src="static/js/platform/component/common/exportData.js" type="text/javascript"></script>
</head>

<script type="text/javascript">
var queryParams;
//查询
function searchFun(){
	queryParams = $('#sysLoglist').datagrid('options').queryParams;  
    queryParams.filter_LIKE_syslogOp =$('#syslogOp').combobox('getValue');
    //queryParams.filter_LIKE_syslogType =$('#syslogType').combobox('getValue');
    queryParams.filter_LIKE_syslogUsernameZh = $("#syslogUsernameZh").attr("value");
    queryParams.filter_LIKE_syslogModule = $("#syslogModule").attr("value");
    queryParams.filter_LIKE_syslogTitle = $("#syslogTitle").attr("value");
    queryParams.filter_GE_syslogTime = $('#startDateBegin').datetimebox('getValue');
    queryParams.filter_LE_syslogTime = $('#startDateEnd').datetimebox('getValue');
    $('#sysLoglist').datagrid('options').queryParams=queryParams;     
    $("#sysLoglist").datagrid('reload'); 
}
//查询选项重置
function clearFun(){
	$('#searchForm input').val('');
	$('#searchForm select').val('');
}

function formatDate(value)
{
	var newDate=new Date(value);
	return newDate.Format("yyyy-MM-dd hh:mm:ss");   
}
function cellTip(data){
	$('#sysLoglist').datagrid('doCellTip',   
	{   
		onlyShowInterrupt:false,   
		position:'bottom',
		tipStyler:{'backgroundColor':'#FFFFFF',boxShadow:'1px 1px 3px #292929'}
	}); 
}

/**
 * 导出日志(客户端数据)
 */
function exportClientData(){
	$.messager.confirm('确认','是否确认导出Excel文件?',function(r) {
        if (r) {
            //封装参数
            var columnFieldsOptions = getGridColumnFieldsOptions('sysLoglist');
            var dataGridFields = JSON.stringify(columnFieldsOptions[0]);
            var rows = $('#sysLoglist').datagrid('getRows');
            var datas = JSON.stringify(rows);
            var myParams = {
                dataGridFields: dataGridFields,//表头信息集合
                datas: datas,//数据集
                unContainFields:'id',
                hasRowNum : true,//默认为Y:代表第一列为序号
                sheetName: 'sheet1',//如果该参数为空，默认为导出的Excel文件名
                fileName: '平台日志导出数据'+ new Date().toString()//导出的Excel文件名
            };
            var url = "platform/sysLog/sysLogController/exportClient";
            var ep = new exportData("xlsExport","xlsExport",myParams,url);
            ep.excuteExport();
        }
       });
}
/**
 * 导出日志(服务端数据)
 */
function exportServerData(){
	$.messager.confirm('确认','是否确认导出Excel文件?',function(r) {
        if (r) {
            //封装参数
            var columnFieldsOptions = getGridColumnFieldsOptions('sysLoglist');
            var dataGridFields = JSON.stringify(columnFieldsOptions[0]);
            
            var expSearchParams = queryParams?queryParams:{};
            
            expSearchParams.dataGridFields=dataGridFields;
            expSearchParams.hasRowNum=true;
            expSearchParams.sheetName='sheet1';
            expSearchParams.unContainFields='id';//由于id没有chechbox，所以需要显示的过滤掉
            expSearchParams.fileName='平台日志导出数据'+ new Date().toString();
            
            
            //var url = "platform/commonExcelController/exportExcelClientData";
            var url = "platform/sysLog/sysLogController/exportServerBiz";
            var ep = new exportData("xlsExport","xlsExport",expSearchParams,url);
            ep.excuteExport();
        }
       });
}

</script>
<body class="easyui-layout" fit="true">


	<!-- <div data-options="region:'north',split:true,title:''" style="height: 150px; padding:0px;"> -->
	<!-- <div region="center" border="false" style="overflow: hidden;"> -->
		<div id="toolbar" class="datagrid-toolbar" style="height:auto;display: block;">
			<fieldset>
				<legend>查询条件</legend>
				<table class="tableForm" id="searchForm" width='100%'>
					<tr>
						<td >操作人：</td>
						<td colspan="2"><input name="syslogUsernameZh" id="syslogUsernameZh" editable="false" style="width: 150px;" />
						<td>操作类型：</td>
						<td colspan="2"><input name="syslogOp" id="syslogOp" class="easyui-combobox" style="width: 150px;" data-options="panelHeight:'auto',editable:false,valueField: 'label',textField: 'value',data: [{label: 'insert',value: '插入'},{label: 'delete',value: '删除'},{label: 'update',value: '修改'},{label: 'select',value: '查看'}]"/>
						</td>
 
						<td>模块名称：</td>
						<td colspan="1"><input name="syslogModule" id="syslogModule" editable="false" style="width: 150px;" />
					</tr>
					
					<tr>
						<td>操作时间从：</td>
						<td colspan="2"><input name="startDateBegin" id="startDateBegin" class="easyui-datetimebox" editable="false" style="width:150px;" />
						</td>
						<td>操作时间(至)：</td>
						<td colspan="2"><input name="startDateEnd" id="startDateEnd"  class="easyui-datetimebox" editable="false" style="width: 150px;" />
						</td>
						<td>日志内容：</td>
						<td colspan="2"><input name="syslogTitle" id="syslogTitle"  editable="false" style="width: 150px;" />
						</td>
						
					</tr>
					<tr>
					<td colspan="2">
					</td>
					<td colspan="2">
					</td>
					<td colspan="2"><a class="easyui-linkbutton"   iconCls="icon-search" plain="false" onclick="searchFun();" href="javascript:void(0);">查询</a><a class="easyui-linkbutton" iconCls="icon-cancel" plain="false" onclick="clearFun();" href="javascript:void(0);">清空</a>
					</td>
					<td colspan="2">
					</td>
					</tr>
					
				</table>
			</fieldset>
			<table >
				<tr>
					<td><a class="easyui-menubutton"  data-options="menu:'#export',iconCls:'icon-export'" href="javascript:void(0);">导出日志</a></td>
				</tr>
			</table>
			<div id="export" style="width:150px;">
				<div data-options="iconCls:'icon-excel'" onclick="exportClientData();">Excel导出(客户端)</div>
				<div data-options="iconCls:'icon-excel'" onclick="exportServerData();">Excel导出(服务器端)</div>
			</div>
		</div>
	<!-- </div> -->
		
	<div data-options="region:'center',split:true,title:''" style="padding:0px;">	
		<!-- <table id="sysLoglist"></table> -->
		<table id="sysLoglist" class="easyui-datagrid"
			data-options=" 
				fit: true,
				border: false,
				rownumbers: false,
				animate: true,
				collapsible: false,
				fitColumns: true,
				autoRowHeight: false,
				toolbar:'#toolbar',
				idField :'id',
				singleSelect: true,
				checkOnSelect: true,
				selectOnCheck: false,
				
				pagination:true,
				pageSize:dataOptions.pageSize,
				pageList:dataOptions.pageList,
				onLoadSuccess:cellTip,
				striped:true,
				
				url: 'platform/sysLog/sysLogController/getBizLogDataByPage.json?',
				queryParams:{'':''}		
				">
			<thead>
				<tr>
					<th data-options="field:'id', hidden:true" >id</th>
					
					<th data-options="field:'syslogUsernameZh',halign:'center',align:'left'" editor="{type:'text'}" width="100">操作人</th>
					<th data-options="field:'syslogIp',required:true,halign:'center',align:'left',sortable:true" editor="{type:'text'}" width="150">操作人IP</th>
					<th data-options="field:'syslogTime',required:true,halign:'center',align:'left', formatter: formatDate,sortable:true" editor="{type:'text'}" width="220">操作时间</th>
					<th data-options="field:'syslogModule',halign:'center',align:'left',sortable:true" editor="{type:'text'}"  width="150">模块名称</th>
					
					<th data-options="field:'syslogTitle',halign:'center',align:'left',sortable:true" editor="{type:'text'}"  width="600">日志内容</th>
					<th data-options="field:'syslogOp',halign:'center',align:'left',sortable:true" editor="{type:'text'}"  width="100">操作类型</th>
					
					
					<th data-options="field:'syslogType',halign:'center',align:'left'" editor="{type:'text'}"  width="150">日志类型</th>
					
				</tr>
			</thead>
		</table>	
		
	</div>


</body>
</html>