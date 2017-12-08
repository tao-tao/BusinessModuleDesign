<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<meta charset="UTF-8">
<title>修改任务</title>
<base href="<%=ViewUtil.getRequestPath(request)%>">
<jsp:include page="/avicit/platform6/component/common/EasyUIJsInclude.jsp"></jsp:include>
<script src="avicit/platform6/modules/system/quartz/subwindow/subwindow.js" type="text/javascript"></script>
<link href="avicit/platform6/modules/system/quartz/subwindow/subwindow.css" type="text/css" rel="stylesheet">
<script type="text/javascript">
/**
 * 初始化数据
 */
$(function(){
	var type = $('#type').combobox('getValue');
	if(type != null && type != ''){
		setTypeRemark(type);
	}
});

/**
 * 返回排除日期宽度
 * @returns
 */
function getCalendarWidth(){
	return parseInt($(window).width()*0.5);
}

/**
 * 返回排除日期高度
 * @returns
 */
function getHeight(){
	return parseInt(($(window).height()-180)*0.5);
}

/**
 * 修改定时任务
 */
function updateJob(){
	$('#updateJobButton').linkbutton('disable');
	
	//form验证
	var r = $('#formJob').form('validate');
	if(!r){
		$('#updateJobButton').linkbutton('enable');
		return false;
	}

	//获取基本信息
	var job =  $('#formJob').serializeArray();
	var jobJson = convertToJson(job);
	
	var name = jobJson.name;
	if(name.byteLength() > 50){
		$.messager.alert('提示','任务名称超过长度！','info');
		$('#updateJobButton').linkbutton('enable');
		return false;
	}
	
	var description = jobJson.description;
	if(description.byteLength() > 200){
		$.messager.alert('提示','描述超过长度！','info');
		$('#updateJobButton').linkbutton('enable');
		return false;
	}
	
	job = JSON.stringify(jobJson);
	
	//参数列表验证
	accept();
	var variables = $('#datagridVariables').datagrid('getRows');
	for(var i = 0; i < variables.length; i++){
		var name = $.trim(variables[i].name);
		var dataType = variables[i].dataType;
		var value = $.trim(variables[i].value);
		
		if (name == "" || dataType == "" || value == "") {
			$.messager.alert('提示','参数名称、类型和值不能为空，请检查。','info');
            $('#updateJobButton').linkbutton('enable');
            return false;
		}
		
		if(value.byteLength() > 100){
			$.messager.alert('提示','参数值超过长度！','info');
			$('#updateJobButton').linkbutton('enable');
			return false;
		}

		if (dataType == "Long" || dataType == "Integer") {
			if (isNaN(value) || String(parseInt(value)).length != value.length) {
	            $.messager.alert('提示','参数类型指定为整型或长整型，参数值不匹配，请检查。','info');
	            $('#updateJobButton').linkbutton('enable');
	            return false;
	        } 
		}else if(dataType == "Float"){
			if (isNaN(value) || String(parseFloat(value)).length != value.length) {
                $.messager.alert('提示','参数类型指定为浮点型，参数值不匹配，请检查。','info');
	            $('#updateJobButton').linkbutton('enable');
	            return false;
            } 
		}else if(dataType == "Date"){ 
			if (isDate(value) == false) {
                $.messager.alert('提示','参数类型指定为日期类型，参数值不匹配，请检查。日期格式为yyyyMMdd。','info');
	            $('#updateJobButton').linkbutton('enable');
	            return false;
            } 
		}
	}
	
	//获取排除日历ID
	var calendarChecked = $('#datagridCalendar').datagrid('getChecked');
	var idArray = new Array();
	for (var i = 0; i < calendarChecked.length; i++){
		idArray[i] = calendarChecked[i].id;
	}
	
	//组合成参数
	var param = {
		job: job,
		jobCalendarIds: idArray.join(','),
		variables: JSON.stringify(variables)
	};
	
	$.ajax({
		url : 'platform/jobMaintainController/updateJob',
		data : param,
		type : 'post',
		dataType : 'json',
		success : function(result) {
			if (result.flag == 'success') {
				parent.successUpdateJob();
			} else {
				$('#updateJobButton').linkbutton('enable');
				if(result.error){
					$.messager.show({title:'提示',msg :'修改定时任务失败！' + result.error});
				}else{
					$.messager.show({title:'提示',msg :'修改定时任务失败！'});
				}
			}
		}
	});
}

/**
 * 是否指定的日期格式,yyyyMMdd
 */
function isDate(dateString){
    if(dateString == ""){
    	return false;
    }
    var pattern = /^[1-9]\d{3}((0[1-9]{1})|(1[0-2]{1}))((0[1-9]{1})|([1-2]{1}\d{1})|(3[0-1]{1}))$/;
    if(!pattern.test(dateString)){
        return false;
    }
    return true;
 }

/**
 * 返回
 */
function doCancel(){
	parent.closeDialog();
}

/**
 * 选中排除日历
 */
function onLoadSuccessCalendar(data){
	var calendarIds = '${jobCalendarIds}';
	if(calendarIds != 'null' && calendarIds != ''){
		var calendarIdArray = calendarIds.split(',');
		for(var i = 0; i < calendarIdArray.length; i++){
			var existsId = calendarIdArray[i];
			 $('#datagridCalendar').datagrid('selectRecord', existsId);
		}
	}
}

/**
 * 改变任务类型时
 */
function onChangeType(newValue, oldValue){
	setTypeRemark(newValue);
	if(newValue == 'spring'){
		var usd = new CommonDialog("selectBeanDialog","550","300","avicit/platform6/modules/system/quartz/selectBean.jsp","选择Bean和函数",true,true,false);
		usd.show();
		
		/**
		 * 关闭显示历史页面
		 */
		closeSelectBeanDialog = function(result) {
			$('#program').val(result);
			usd.close();
		};
	}
}

/**
 * 设置类型说明
 */
function setTypeRemark(type){
	var typeRemark = "";
	if(type == 'spring'){
		typeRemark = "注册在Spring中的Bean#方法名,例如springBeanId#methodName";
	}else if(type == 'clazz'){
		typeRemark = "类的全路径#方法,例如com.demo.test.Test#sayHello";
	}else if(type == 'quartzClazz'){
		typeRemark = "类的全路径并且该类继承自org.quart.Job接口,例如com.demo.test.Test";
	}else if(type == 'sql'){
		typeRemark = "SQL语句";
	}else if(type == 'sp'){
		typeRemark = "存储过程名称";
	}
	$('#typeRemark').val(typeRemark);
}

/**
 * 格式化状态
 */
function formatterDataType(value, row, index){      
	var dataType = "";
	if(value == 'String'){
		dataType = "字符串";
	}else if(value == 'Integer'){
		dataType = "整型";
	}else if(value == 'Long'){
		dataType = "长整型";
	}else if(value == 'Float'){
		dataType = "浮点型";
	}else if(value == 'Date'){
		dataType = "日期类型";
	}
	return dataType;
}

/**
 * 选择日历，刷新其下的排除日期
 */
function onSelectCalendar(rowIndex, rowData){
	$('#datagridCalendarDate').datagrid('options').url = 'platform/jobCalendarController/loadJobCalendarDates.json';
	$('#datagridCalendarDate').datagrid('load',{
		jobCalenderId: rowData.id
	});
}

/**
 * 打开表达式页面
 */
function openSelectCron(){
	var usd = new CommonDialog("selectBeanDialog","650","490","avicit/platform6/modules/system/quartz/cronGenerator.jsp","选择执行时间",true,true,false);
	usd.show();
	
	/**
	 * 获取表达式(名称固定)
	 */
	getCronExpr = function(cronExpr) {
		return $('#cron').subwindow('getValue');
	};
	
	/**
	 * 成功后调用
	 */
	doSelectCronSuccess = function(cronExpr) {
		$('#cron').subwindow('setValue', cronExpr);
		usd.close();
	};
	
	/**
	 * 关闭页面
	 */
	 closeDialog = function(result) {
		usd.close();
	};
}

/**
 * ******************编辑单元格开始******************
 */
 
//当前编辑index
var editIndex = undefined;

/**
 * 是否正在编辑单元格，正在编辑的单元格修改并结束编辑
 * @returns {Boolean}
 */
function endEditing(){
	if (editIndex == undefined){
		return true;
	}
	if ($('#datagridVariables').datagrid('validateRow', editIndex)){
		$('#datagridVariables').datagrid('endEdit', editIndex);
		editIndex = undefined;
		return true;
	} else {
		return false;
	}
}

/**
 * 点击编辑单元格
 * @param index
 * @returns {Boolean}
 */
function onClickRow(index){
	if (editIndex != index){
		if (endEditing()){
			$('#datagridVariables').datagrid('selectRow', index).datagrid('beginEdit', index);
			editIndex = index;
		} else {
			$('#datagridVariables').datagrid('selectRow', editIndex);
		}
	}
}

/**
 *增加一行
 */
function append(){
    if (endEditing()){
        $('#datagridVariables').datagrid('appendRow',{});
        editIndex = $('#datagridVariables').datagrid('getRows').length-1;
        $('#datagridVariables').datagrid('selectRow', editIndex).datagrid('beginEdit', editIndex);
    }
}

/**
 * 删除一行
 */
function removeit(){
    if (editIndex == undefined){
    	return;
    }
    $('#datagridVariables').datagrid('cancelEdit', editIndex).datagrid('deleteRow', editIndex);
    editIndex = undefined;
}

/**
 * 接受改变的值
 */
function accept(){
    if (endEditing()){
        $('#datagridVariables').datagrid('acceptChanges');
    }
}
/**
 * ******************编辑单元格结束******************
 */

</script>
</head>
<body class="easyui-layout" fit="true">
	<!-- 属性 -->
	<div data-options="region:'north'" split="false" style="height: 180px;border-left: 0;border-right: 0;border-top: 0;overflow: hidden;">
		<form id="formJob" fit="true">
			<input type="hidden" name="status" value="${job.status}">
			<input id="id" name="id" type="hidden" value="${job.id}"/>
			<div class="formExtendBase">
				<sec:accesscontrollist hasPermission="3" domainObject="jobMaintainUpdate_form_group">
				<div class="formUnit column2">
					<label class="labelbg">任务分组</label>
					<div class="inputContainer">
						<input style="width:210px;" id="group" name="group" title="任务分组" class="easyui-combobox" type="text" value="${job.group}"
						    data-options="required:true,editable:false,panelHeight:'auto',valueField:'name',textField:'description',url:'platform/jobMaintainController/loadGroups.json'" />
					</div>
				</div>
				</sec:accesscontrollist>
				<sec:accesscontrollist hasPermission="3" domainObject="jobMaintainUpdate_form_name">
				<div class="formUnit column2">
					<label class="labelbg">任务名称</label>
					<div class="inputContainer">
						<input style="width:90%;" id="name" name="name" title="任务名称" class="easyui-validatebox" type="text" data-options="required:true" value="${job.name}" />
					</div>
				</div>
				</sec:accesscontrollist>
				<sec:accesscontrollist hasPermission="3" domainObject="jobMaintainUpdate_form_type">
				<div class="formUnit column2">
					<label class="labelbg">任务类型</label>
					<div class="inputContainer">
						<input style="width:210px;" id="type" name="type" title="任务类型" class="easyui-combobox" value="${job.type}"
						    data-options="required:true,editable:false,panelHeight:'auto',
							onChange:onChangeType,valueField:'label',textField:'value',
							data: [{label: 'spring',value: 'SpringBean'},
							       {label: 'clazz',value: 'Java类'},
							       {label: 'quartzClazz',value: 'Quartz Job'},
							       {label: 'sql',value: 'Sql语句'},
							       {label: 'sp',value: '存储过程'}]" /> 
					</div>
				</div>
				</sec:accesscontrollist>
				<sec:accesscontrollist hasPermission="3" domainObject="jobMaintainUpdate_form_cron">
				<div class="formUnit column2">
					<label class="labelbg">表达式</label>
					<div class="inputContainer input-readonly">
						<input style="width:213px;" id="cron" name="cron" title="表达式" class="easyui-subwindow" type="text" data-options="required:true,onClick:openSelectCron" value="${job.cron}"/>
					</div>
				</div>
				</sec:accesscontrollist>
				<sec:accesscontrollist hasPermission="3" domainObject="jobMaintainUpdate_form_typeRemark">
				<div class="formUnit column1">
					<label>类型说明</label>
					<div class="inputContainer input-readonly">
						<input style="width:90%;" id="typeRemark" name="" title="类型说明" class="easyui-validatebox" type="text" data-options="required:false" value="" readonly="readonly"/>
					</div>
				</div>
				</sec:accesscontrollist>
				<sec:accesscontrollist hasPermission="3" domainObject="jobMaintainUpdate_form_program">
				<div class="formUnit column1">
					<label class="labelbg">执行程序</label>
					<div class="inputContainer">
						<input style="width:90%;" id="program" name="program" title="执行程序" class="easyui-validatebox" type="text" data-options="required:true" value="${job.program}" />
					</div>
				</div>
				</sec:accesscontrollist>
				<sec:accesscontrollist hasPermission="3" domainObject="jobMaintainUpdate_form_description">
				<div class="formUnit column1">
					<label>描述</label>
					<div class="inputContainer" >
						<input style="width:90%;" id="description" name="description" title="描述" class="easyui-validatebox" type="text" data-options="required:false" value="${job.description}" />
					</div>
				</div>
				</sec:accesscontrollist>
			</div>
		</form>
	</div>
	<!-- 参数列表 -->
	<div data-options="region:'center'" split="false" title="参数列表" collapsible="false" style="border-left: 0;border-right: 0;border-top: 0;">
		<table id="datagridVariables" class="easyui-datagrid" 
			data-options="
				fit: true,
				border:false,
				rownumbers: true,
				animate: true,
				collapsible: false,
				fitColumns: true,
				autoRowHeight: false,
				singleSelect: true,
				toolbar:'#toolbarParam',
				method: 'post',
				url: 'platform/jobMaintainController/findJobVariables.json?jobId=${job.id}',
				onClickRow: onClickRow
			">
			<thead>
				<tr>
					<th data-options="field:'name', halign:'center', align:'left',editor:{type:'validatebox'}" width="100">参数名称</th>
					<th data-options="field:'dataType', halign:'center', align:'left',formatter:formatterDataType,
	                        editor:{
	                            type:'combobox',
	                            options:{
	                                editable:false,
	                                panelHeight:'auto',
	                                valueField:'label',
	                                textField:'value',
	                                data: [{label: 'String',value: '字符串'},
							               {label: 'Integer',value: '整型'},
							               {label: 'Long',value: '长整型'},
							               {label: 'Float',value: '浮点型'},
							               {label: 'Date',value: '日期类型'}]
	                            }
	                        }" width="100">数据类型</th>
					<th data-options="field:'value', halign:'center', align:'left',editor:{type:'validatebox'}" width="100">参数值</th>
				</tr>
			</thead>
		</table>
		<div id="toolbarParam" class="datagrid-toolbar" style="height:auto;display: block;padding:0 0 0 10px;">
			<a class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="append();" href="javascript:void(0);">增加</a>
			<a class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="removeit();" href="javascript:void(0);">删除</a>
		</div>
	</div>	
	<!-- 排除日期 -->
	<div data-options="region:'south',height:getHeight()" split="false" title="排除日期" collapsible="false" style="border-left: 0;border-right: 0;border-bottom: 0;">
		<div class="easyui-layout" fit="true">
			<!-- 日历名称 -->
			<div data-options="region:'center',split:true" style="border-left: 0;border-bottom: 0;border-top: 0;">
				<table id="datagridCalendar" class="easyui-datagrid" 
					data-options="
						fit: true,
						border:false,
						rownumbers: true,
						animate: true,
						collapsible: false,
						fitColumns: true,
						autoRowHeight: false,
						singleSelect: false,
						idField: 'id',
						method: 'post',
						url: 'platform/jobCalendarController/loadJobCalendars.json',
						onSelect: onSelectCalendar,
						onLoadSuccess: onLoadSuccessCalendar
					">
					<thead>
						<tr>
							<th data-options="field:'checkbox', checkbox: true, halign:'center', align:'center'" width="30"></th>
							<th data-options="field:'name', halign:'center', align:'left'" width="100">日历名称</th>
						</tr>
					</thead>
				</table>
			</div>
			<!-- 日历描述 -->
			<div data-options="region:'east',split:true,width: getCalendarWidth()" style="border-right: 0;border-bottom: 0;border-top: 0;">
				<table id="datagridCalendarDate" class="easyui-datagrid" 
					data-options="
						fit: true,
						border:false,
						rownumbers: true,
						animate: true,
						collapsible: false,
						fitColumns: true,
						autoRowHeight: false,
						singleSelect: true,
						method: 'post'
					">
					<thead>
						<tr>
							<th data-options="field:'excludedDate', halign:'center', align:'left',formatter:formatColumnDate" width="100">排除的日期</th>
							<th data-options="field:'description', halign:'center', align:'left'" width="100">描述</th>
						</tr>
					</thead>
				</table>
			</div>
			<!-- 按钮  -->
			<div data-options="region:'south'" split="false" style="height:30px;text-align: center;padding:3px 0 0 0;overflow:hidden;border-left: 0;border-right: 0;border-bottom: 0;">
				<!-- <a class="easyui-linkbutton" iconCls="icon-ok" id="updateJobButton" onclick="updateJob();" href="javascript:void(0);">完成</a>
				<a class="easyui-linkbutton" iconCls="icon-no" onclick="doCancel();" href="javascript:void(0);">取消</a> -->
				<table class="tableForm" border="0" cellspacing="1" width='100%'>
					<tr>	
						<td width="60%" align="right"><a title="保存" id="saveButton"  class="easyui-linkbutton" plain="false" onclick="updateJob();" href="javascript:void(0);">保存</a>
						<a title="返回" id="returnButton"  class="easyui-linkbutton" plain="false" onclick="doCancel();" href="javascript:void(0);">返回</a>
						</td>
					</tr>
				</table>
			</div>
		</div>
	</div>
</body>
</html>