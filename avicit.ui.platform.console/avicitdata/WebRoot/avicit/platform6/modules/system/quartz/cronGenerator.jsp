<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>选择执行时间</title>
<base href="<%=ViewUtil.getRequestPath(request)%>">
<jsp:include page="/avicit/platform6/component/common/EasyUIJsInclude.jsp"></jsp:include>
<script src="avicit/platform6/modules/system/quartz/cronExpr.js" type="text/javascript"></script>
<script type="text/javascript">
/**
 * 初始化数据
 */
$(function(){
	initCronExprSelect();
	if(parent.getCronExpr()){
		var cron = parent.getCronExpr();
		if(cron){
			$('#cronExpr').val(cron);
			parseCronExpr();
		}
	}
	
});

/**
 * 确定
 */
function doSubmit(){
	$('#submitButton').linkbutton('disable');
	
	var cronExpr = $('#cronExpr').val();
	cronExpr = $.trim(cronExpr);
	if (cronExpr == "") {
		$.messager.alert('提示','Cron表达式不能为空!','info');
		$('#submitButton').linkbutton('enable');
        return false;
	}
	//组合成参数
	var param = {
		cronExpr: cronExpr
	};
	
	$.ajax({
		url : 'platform/cronGeneratorController/isValidCronExpression',
		data : param,
		type : 'post',
		dataType : 'json',
		success : function(result) {
			if (result.flag == 'success') {
				parent.doSelectCronSuccess(cronExpr);
			} else {
				$('#submitButton').linkbutton('enable');
				var errorMsg = result.errorMsg;
				if(errorMsg && errorMsg != ''){
					$.messager.alert('提示', errorMsg,'error');
				}else{
					$.messager.alert('提示','验证错误！','error');
				}
			}
		}
	});
}

/**
 * 返回
 */
function doCancel(){
	parent.closeDialog();
}

/**
 * 生成表达式
 */
function generateCronExpr(){
	var cron = new CronExprObject();
	var records = cron.cronExpression();
	records = JSON.stringify(records);
	//组合成参数
	var param = {
		cronExpr: '',
		startTime: '',
		records: records
	};
	
	$.ajax({
		url : 'platform/cronGeneratorController/executeGenerateCronExpression',
		data : param,
		type : 'post',
		dataType : 'json',
		success : function(result) {
			if (result.flag == 'success') {
				$('#cronExpr').val(result.cronExpr);
				$('#startTime').datetimebox('setValue', result.startTime);
				var executionTimes = result.executionTimes;
				executionTimes = executionTimes.replace(/;/g, "\n");
				$('#executionTimes').val(executionTimes);
			} else {
				var errorMsg = result.errorMsg;
				if(errorMsg && errorMsg != ''){
					$.messager.alert('提示',errorMsg,'error');
				}else{
					$.messager.alert('提示','执行错误！','error');
				}
			}
		}
	});
}

/**
 * 解析表达式
 */
function parseCronExpr(){
	var cronExpr = $('#cronExpr').val();
	cronExpr = $.trim(cronExpr);
	if (cronExpr == "") {
		$.messager.alert('提示','Cron表达式不能为空!','info');
        return false;
	}
	
	var cron = new CronExprObject();
	
	if(cron.parseCronToUI(cronExpr)){
		cron.parseSecondToUI();
		cron.parseMinuteToUI();
		cron.parseHourToUI();
		cron.parseDayToUI();
		cron.parseMonthToUI();
		cron.parseWeekToUI();
	}else{
		$.messager.alert('提示','表达式解析不成功，请点击验证按钮确认表达式是否格式正确','info');
	}
}

/**
 * 验证表达式
 */
function validateCron(){
	var cronExpr = $('#cronExpr').val();
	cronExpr = $.trim(cronExpr);
	if (cronExpr == "") {
		$.messager.alert('提示','Cron表达式不能为空!','info');
        return false;
	}
	//组合成参数
	var param = {
		cronExpr: cronExpr
	};
	
	$.ajax({
		url : 'platform/cronGeneratorController/isValidCronExpression',
		data : param,
		type : 'post',
		dataType : 'json',
		success : function(result) {
			if (result.flag == 'success') {
				$.messager.show({title:'提示',msg :'验证正确！'});
			} else {
				var errorMsg = result.errorMsg;
				if(errorMsg && errorMsg != ''){
					$.messager.alert('提示', errorMsg,'error');
				}else{
					$.messager.alert('提示','验证错误！','error');
				}
			}
		}
	});
}

/**
 * 预演执行时间
 */
function showRunTime(){
	var cronExpr = $('#cronExpr').val();
	cronExpr = $.trim(cronExpr);
	if (cronExpr == "") {
		$.messager.alert('提示','Cron表达式不能为空!','info');
        return false;
	}
	var startTime = $('#startTime').val();
	
	//组合成参数
	var param = {
		cronExpr: cronExpr,
		startTime: startTime
	};
	
	$.ajax({
		url : 'platform/cronGeneratorController/executeGenerateCronExpression',
		data : param,
		type : 'post',
		dataType : 'json',
		success : function(result) {
			if (result.flag == 'success') {
				$('#cronExpr').val(result.cronExpr);
				$('#startTime').datetimebox('setValue', result.startTime);
				var executionTimes = result.executionTimes;
				executionTimes = executionTimes.replace(/;/g, "\n");
				$('#executionTimes').val(executionTimes);
			} else {
				var errorMsg = result.errorMsg;
				if(errorMsg && errorMsg != ''){
					$.messager.alert('提示',errorMsg,'error');
				}else{
					$.messager.alert('提示','执行错误！','error');
				}
			}
		}
	});
}
</script>
</head>
<body class="easyui-layout" fit="true">
<div region="north" border="false" style="height: 160px;">
 	<div id="tab-tools">
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-reload'" onclick="generateCronExpr()">生成表达式</a>
    </div>
	<div class="easyui-tabs" fit="true" tabWidth="50" data-options="tools:'#tab-tools'" >   
	    <div title="月">   
	        <table width="100%" border="0">
				<tr height="35px">
					<td colspan="2" style="padding-left: 10px;">
						<input type="radio" name="radioMonth" id="monthLoop" value="loop" checked="checked" />每月
					</td>
				</tr>
				<tr height="25px">
					<td width="80px" style="padding-left: 10px;">
						<input type="radio" name="radioMonth" id="monthFix" value="fix" />指定
					</td>
					<td>
						<div style="float: left;width: 75px"><input type="checkbox" name="month" value="1"/>1月</div>
						<div style="float: left;width: 75px"><input type="checkbox" name="month" value="2"/>2月</div>
						<div style="float: left;width: 75px"><input type="checkbox" name="month" value="3"/>3月</div>
						<div style="float: left;width: 75px"><input type="checkbox" name="month" value="4"/>4月</div>
						<div style="float: left;width: 75px"><input type="checkbox" name="month" value="5"/>5月</div>
						<div style="float: left;width: 75px"><input type="checkbox" name="month" value="6"/>6月</div>
					</td>
				</tr>
				<tr height="25px">
					<td></td>
					<td>		
						<div style="float: left;width: 75px"><input type="checkbox" name="month" value="7"/>7月</div>
						<div style="float: left;width: 75px"><input type="checkbox" name="month" value="8"/>8月</div>
						<div style="float: left;width: 75px"><input type="checkbox" name="month" value="9"/>9月</div>
						<div style="float: left;width: 75px"><input type="checkbox" name="month" value="10"/>10月</div>
						<div style="float: left;width: 75px"><input type="checkbox" name="month" value="11"/>11月</div>
						<div style="float: left;width: 75px"><input type="checkbox" name="month" value="12"/>12月</div>
					</td>
				</tr>
			</table>
	    </div>
	    <div title="周">   
	        <table width="100%" border="0">
				<tr height="30px">
					<td colspan="2" style="padding-left: 10px;"><input type="checkbox" id="useWeek" name="useWeek" value="yes" />使用星期</td>
				</tr>
				<tr height="30px">
					<td colspan="2" style="padding-left: 20px;"><input type="radio" name="radioWeek" id="weekLoop" value="loop" checked="checked" />每周</td>
				</tr>
				<tr>
					<td valign="top" style="padding-left: 20px;">
						<input type="radio" name="radioWeek" id="weekFix" value="fix" />指定
					</td>
					<td valign="top">
						<div style="float: left;width: 75px"><input type="checkbox" name="week" value="1">星期日</div>
						<div style="float: left;width: 75px"><input type="checkbox" name="week" value="2">星期一</div>
						<div style="float: left;width: 75px"><input type="checkbox" name="week" value="3">星期二</div>
						<div style="float: left;width: 75px"><input type="checkbox" name="week" value="4">星期三</div>
						<div style="float: left;width: 75px"><input type="checkbox" name="week" value="5">星期四</div>
						<div style="float: left;width: 75px"><input type="checkbox" name="week" value="6">星期五</div>
						<div style="float: left;width: 75px"><input type="checkbox" name="week" value="7">星期六</div>
					</td>
				</tr>
			</table>
	    </div>
	    <div title="天">   
	        <table width="100%" border="0">
				<tr height="30px">
					<td colspan="2" style="padding-left: 10px;">
						<input type="radio" name="radioDay" id="dayLoop" value="loop" checked="checked" />每天
					</td>
				</tr>
				<tr height="25px">
					<td width="80px" style="padding-left: 10px;">
						<input type="radio" name="radioDay" id="dayFix" value="fix" />指定
					</td>
					<td>
						<div style="float: left;width: 40px"><input type="checkbox" name="day" value="1"/>1</div>
						<div style="float: left;width: 40px"><input type="checkbox" name="day" value="2"/>2</div>
						<div style="float: left;width: 40px"><input type="checkbox" name="day" value="3"/>3</div>
						<div style="float: left;width: 40px"><input type="checkbox" name="day" value="4"/>4</div>
						<div style="float: left;width: 40px"><input type="checkbox" name="day" value="5"/>5</div>
						<div style="float: left;width: 40px"><input type="checkbox" name="day" value="6"/>6</div>
						<div style="float: left;width: 40px"><input type="checkbox" name="day" value="7"/>7</div>
						<div style="float: left;width: 40px"><input type="checkbox" name="day" value="8"/>8</div>
						<div style="float: left;width: 40px"><input type="checkbox" name="day" value="9"/>9</div>
						<div style="float: left;width: 40px"><input type="checkbox" name="day" value="10"/>10</div>
						<div style="float: left;width: 40px"><input type="checkbox" name="day" value="11"/>11</div>
						<div style="float: left;width: 40px"><input type="checkbox" name="day" value="12"/>12</div>
					</td>
				</tr>
				<tr height="25px">
					<td></td>
					<td>		
						<div style="float: left;width: 40px"><input type="checkbox" name="day" value="13"/>13</div>
						<div style="float: left;width: 40px"><input type="checkbox" name="day" value="14"/>14</div>
						<div style="float: left;width: 40px"><input type="checkbox" name="day" value="15"/>15</div>
						<div style="float: left;width: 40px"><input type="checkbox" name="day" value="16"/>16</div>
						<div style="float: left;width: 40px"><input type="checkbox" name="day" value="17"/>17</div>
						<div style="float: left;width: 40px"><input type="checkbox" name="day" value="18"/>18</div>
						<div style="float: left;width: 40px"><input type="checkbox" name="day" value="19"/>19</div>
						<div style="float: left;width: 40px"><input type="checkbox" name="day" value="20"/>20</div>
						<div style="float: left;width: 40px"><input type="checkbox" name="day" value="21"/>21</div>
						<div style="float: left;width: 40px"><input type="checkbox" name="day" value="22"/>22</div>
						<div style="float: left;width: 40px"><input type="checkbox" name="day" value="23"/>23</div>
						<div style="float: left;width: 40px"><input type="checkbox" name="day" value="24"/>24</div>
					</td>
				</tr>
				<tr height="25px">
					<td></td>
					<td>		
						<div style="float: left;width: 40px"><input type="checkbox" name="day" value="25"/>25</div>
						<div style="float: left;width: 40px"><input type="checkbox" name="day" value="26"/>26</div>
						<div style="float: left;width: 40px"><input type="checkbox" name="day" value="27"/>27</div>
						<div style="float: left;width: 40px"><input type="checkbox" name="day" value="28"/>28</div>
						<div style="float: left;width: 40px"><input type="checkbox" name="day" value="29"/>29</div>
						<div style="float: left;width: 40px"><input type="checkbox" name="day" value="30"/>30</div>
						<div style="float: left;width: 40px"><input type="checkbox" name="day" value="31"/>31</div>
					</td>
				</tr>
			</table>
	    </div>
	    <div title="时">   
	        <table width="100%" border="0">
				<tr height="30px">
					<td colspan="2" style="padding-left: 10px;">
						<input type="radio" name="radioHour" id="hourLoop" value="loop" checked="checked" />每小时
					</td>
				</tr>
				<tr height="25px">
					<td width="80px" style="padding-left: 10px;">
						<input type="radio" name="radioHour" id="hourFix" value="fix" />指定
					</td>
					<td>
						<div style="float: left;width: 50px"><input type="checkbox" name="hour" value="0"/>0</div>
						<div style="float: left;width: 50px"><input type="checkbox" name="hour" value="1"/>1</div>
						<div style="float: left;width: 50px"><input type="checkbox" name="hour" value="2"/>2</div>
						<div style="float: left;width: 50px"><input type="checkbox" name="hour" value="3"/>3</div>
						<div style="float: left;width: 50px"><input type="checkbox" name="hour" value="4"/>4</div>
						<div style="float: left;width: 50px"><input type="checkbox" name="hour" value="5"/>5</div>
						<div style="float: left;width: 50px"><input type="checkbox" name="hour" value="6"/>6</div>
						<div style="float: left;width: 50px"><input type="checkbox" name="hour" value="7"/>7</div>
					</td>
				</tr>
				<tr height="25px">
					<td></td>
					<td>
						<div style="float: left;width: 50px"><input type="checkbox" name="hour" value="8"/>8</div>
						<div style="float: left;width: 50px"><input type="checkbox" name="hour" value="9"/>9</div>
						<div style="float: left;width: 50px"><input type="checkbox" name="hour" value="10"/>10</div>
						<div style="float: left;width: 50px"><input type="checkbox" name="hour" value="11"/>11</div>
						<div style="float: left;width: 50px"><input type="checkbox" name="hour" value="12"/>12</div>
						<div style="float: left;width: 50px"><input type="checkbox" name="hour" value="13"/>13</div>
						<div style="float: left;width: 50px"><input type="checkbox" name="hour" value="14"/>14</div>
						<div style="float: left;width: 50px"><input type="checkbox" name="hour" value="15"/>15</div>
					</td>
				</tr>
				<tr height="25px">
					<td></td>
					<td>
						<div style="float: left;width: 50px"><input type="checkbox" name="hour" value="16"/>16</div>
						<div style="float: left;width: 50px"><input type="checkbox" name="hour" value="17"/>17</div>
						<div style="float: left;width: 50px"><input type="checkbox" name="hour" value="18"/>18</div>
						<div style="float: left;width: 50px"><input type="checkbox" name="hour" value="19"/>19</div>
						<div style="float: left;width: 50px"><input type="checkbox" name="hour" value="20"/>20</div>
						<div style="float: left;width: 50px"><input type="checkbox" name="hour" value="21"/>21</div>
						<div style="float: left;width: 50px"><input type="checkbox" name="hour" value="22"/>22</div>
						<div style="float: left;width: 50px"><input type="checkbox" name="hour" value="23"/>23</div>
					</td>
				</tr>
			</table>
	    </div>
	    <div title="分">   
	        <table width="100%" border="0">
				<tr height="25px">
					<td style="padding-left: 10px;">
						<input type="radio" name="radioMinute" id="minuteLoop" value="loop" checked="checked" />循环
					</td>
					<td>
						从第&nbsp;<input type="text" name="beginMinute" id="beginMinute" size="2" value="0">&nbsp;分钟开始,每隔&nbsp;<input type="text" name="sepMinute" id="sepMinute" size="2" value="6">&nbsp;分钟执行.
					</td>
				</tr>
				<tr height="20px">
					<td width="70px" style="padding-left: 10px;">
						<input type="radio" name="radioMinute" id="minuteFix" value="fix" />指定
					</td>
					<td>
						<div style="float: left;width: 35px"><input type="checkbox" name="minute" value="0"/>0</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="minute" value="1"/>1</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="minute" value="2"/>2</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="minute" value="3"/>3</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="minute" value="4"/>4</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="minute" value="5"/>5</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="minute" value="6"/>6</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="minute" value="7"/>7</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="minute" value="8"/>8</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="minute" value="9"/>9</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="minute" value="10"/>10</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="minute" value="11"/>11</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="minute" value="12"/>12</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="minute" value="13"/>13</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="minute" value="14"/>14</div>
					</td>
				</tr>
				<tr height="20px">
					<td></td>
					<td>
						<div style="float: left;width: 35px"><input type="checkbox" name="minute" value="15"/>15</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="minute" value="16"/>16</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="minute" value="17"/>17</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="minute" value="18"/>18</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="minute" value="19"/>19</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="minute" value="20"/>20</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="minute" value="21"/>21</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="minute" value="22"/>22</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="minute" value="23"/>23</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="minute" value="24"/>24</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="minute" value="25"/>25</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="minute" value="26"/>26</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="minute" value="27"/>27</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="minute" value="28"/>28</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="minute" value="29"/>29</div>
					</td>
				</tr>
				<tr height="20px">
					<td></td>
					<td>
						<div style="float: left;width: 35px"><input type="checkbox" name="minute" value="30"/>30</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="minute" value="31"/>31</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="minute" value="32"/>32</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="minute" value="33"/>33</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="minute" value="34"/>34</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="minute" value="35"/>35</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="minute" value="36"/>36</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="minute" value="37"/>37</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="minute" value="38"/>38</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="minute" value="39"/>39</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="minute" value="40"/>40</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="minute" value="41"/>41</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="minute" value="42"/>42</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="minute" value="43"/>43</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="minute" value="44"/>44</div>
					</td>
				</tr>
				<tr height="20px">
					<td></td>
					<td>
						<div style="float: left;width: 35px"><input type="checkbox" name="minute" value="45"/>45</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="minute" value="46"/>46</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="minute" value="47"/>47</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="minute" value="48"/>48</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="minute" value="49"/>49</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="minute" value="50"/>50</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="minute" value="51"/>51</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="minute" value="52"/>52</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="minute" value="53"/>53</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="minute" value="54"/>54</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="minute" value="55"/>55</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="minute" value="56"/>56</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="minute" value="57"/>57</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="minute" value="58"/>58</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="minute" value="59"/>59</div>
					</td>
				</tr>
			</table>
	    </div>
	    <div title="秒" selected="true">   
	        <table width="100%" border="0">
				<tr height="25px">
					<td style="padding-left: 10px;">
						<input type="radio" name="radioSecond" id="secondLoop" value="loop" checked="checked" />循环
					</td>
					<td>
						从第&nbsp;<input type="text" name="beginSecond" id="beginSecond" size="2" value="0">&nbsp;秒开始,每隔&nbsp;<input type="text" name="sepSecond" id="sepSecond" size="2" value="5">&nbsp;秒执行.
					</td>
				</tr>
				<tr height="20px">
					<td width="70px" style="padding-left: 10px;">
						<input type="radio" name="radioSecond" id="secondFix" value="fix" />指定
					</td>
					<td>
						<div style="float: left;width: 35px"><input type="checkbox" name="second" value="0"/>0</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="second" value="1"/>1</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="second" value="2"/>2</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="second" value="3"/>3</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="second" value="4"/>4</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="second" value="5"/>5</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="second" value="6"/>6</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="second" value="7"/>7</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="second" value="8"/>8</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="second" value="9"/>9</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="second" value="10"/>10</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="second" value="11"/>11</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="second" value="12"/>12</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="second" value="13"/>13</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="second" value="14"/>14</div>
					</td>
				</tr>
				<tr height="20px">
					<td></td>
					<td>
						<div style="float: left;width: 35px"><input type="checkbox" name="second" value="15"/>15</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="second" value="16"/>16</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="second" value="17"/>17</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="second" value="18"/>18</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="second" value="19"/>19</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="second" value="20"/>20</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="second" value="21"/>21</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="second" value="22"/>22</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="second" value="23"/>23</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="second" value="24"/>24</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="second" value="25"/>25</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="second" value="26"/>26</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="second" value="27"/>27</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="second" value="28"/>28</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="second" value="29"/>29</div>
					</td>
				</tr>
				<tr height="20px">
					<td></td>
					<td>
						<div style="float: left;width: 35px"><input type="checkbox" name="second" value="30"/>30</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="second" value="31"/>31</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="second" value="32"/>32</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="second" value="33"/>33</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="second" value="34"/>34</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="second" value="35"/>35</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="second" value="36"/>36</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="second" value="37"/>37</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="second" value="38"/>38</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="second" value="39"/>39</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="second" value="40"/>40</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="second" value="41"/>41</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="second" value="42"/>42</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="second" value="43"/>43</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="second" value="44"/>44</div>
					</td>
				</tr>
				<tr height="20px">
					<td></td>
					<td>
						<div style="float: left;width: 35px"><input type="checkbox" name="second" value="45"/>45</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="second" value="46"/>46</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="second" value="47"/>47</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="second" value="48"/>48</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="second" value="49"/>49</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="second" value="50"/>50</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="second" value="51"/>51</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="second" value="52"/>52</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="second" value="53"/>53</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="second" value="54"/>54</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="second" value="55"/>55</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="second" value="56"/>56</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="second" value="57"/>57</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="second" value="58"/>58</div>
						<div style="float: left;width: 35px"><input type="checkbox" name="second" value="59"/>59</div>
					</td>
				</tr>
			</table>
	    </div>  
	</div> 
</div>
<div region="center" border="false">
	<form id="formSelectCron">	
		<table width="600px" border="0">
			<tr height="30px">
				<td width="120px" align="right">Cron表达式</td>
				<td width="150px"><input id="cronExpr" name="cronExpr" class="easyui-validatebox" required="true" style="width: 150px" value=""/></td>
				<td width="150px"><a class="easyui-linkbutton" iconCls="icon-valid" onclick="validateCron();" href="javascript:void(0);"><div style="width: 72px;">验证表达式</div></a></td>
				<td width="150px"><a class="easyui-linkbutton" iconCls="icon-ok" onclick="parseCronExpr();" href="javascript:void(0);">解析表达式</a></td>
			</tr>
			<tr height="30px">
				<td align="right">开始时间</td>
				<td><input id="startTime" name="startTime" class="easyui-datetimebox" style="width: 150px;"/></td>
				<td colspan="2"><a class="easyui-linkbutton" iconCls="icon-undo" onclick="showRunTime();" href="javascript:void(0);">预演执行时间</a></td>
			</tr>
			<tr height="30px">
				<td align="right">执行时间</td>
				<td colspan="3"><textarea id="executionTimes" name="executionTimes" readonly="readonly" style="width: 430px;height: 120px" ></textarea></td>
			</tr>
		</table>
	</form>
</div>
<!-- 按钮  -->
<div data-options="region:'south'" split="false" style="height:30px;text-align: center;padding:3px 0 0 0;overflow:hidden;border: 0">
	<a class="easyui-linkbutton" iconCls="icon-ok" id="submitButton" onclick="doSubmit();" href="javascript:void(0);">确定</a>
	<a class="easyui-linkbutton" iconCls="icon-no" onclick="doCancel();" href="javascript:void(0);">取消</a>
</div>
</body>
</html>
