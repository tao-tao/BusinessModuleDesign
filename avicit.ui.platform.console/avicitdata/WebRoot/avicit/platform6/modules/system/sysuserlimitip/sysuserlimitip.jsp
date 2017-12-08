<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="avicit.platform6.commons.utils.ComUtil"%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户IP限制管理</title>
<base href="<%=ComUtil.getRequestPath(request) %>">
<link href="avicit/platform6/modules/system/sysdept/css/icon.css" type="text/css" rel="stylesheet">
<jsp:include page="/avicit/platform6/component/common/EasyUIJsInclude.jsp"></jsp:include>
<jsp:include page="/avicit/platform6/modules/system/commonpopup/commonSelectionHead.jsp"></jsp:include>
<script src="static/js/platform/component/common/exportData.js" type="text/javascript"></script>
</head>

<script src="avicit/platform6/modules/system/sysuserlimitip/sysuserlimitip.js" type="text/javascript"></script>

<body class="easyui-layout">
	<div id="toolbar" class="datagrid-toolbar" style="height:auto;display: block;">
		<sec:accesscontrollist   hasPermission="3" domainObject="pmTaskInfo_tabPower_button_btAddUserLimitIp" >
			<a id="btAddUserLimitIp"  class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addData();" href="javascript:void(0);">添加</a>
		</sec:accesscontrollist>
		
		
		<sec:accesscontrollist   hasPermission="3" domainObject="pmTaskInfo_tabPower_button_btSaveUserLimitIp" >
			<a id="btSaveUserLimitIp"  class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="saveData();" href="javascript:void(0);">保存</a>
		</sec:accesscontrollist>
		
		<sec:accesscontrollist   hasPermission="3" domainObject="pmTaskInfo_tabPower_button_btDeleteUserLimitIp" >
			<a id="btDeleteUserLimitIp" class="easyui-linkbutton" iconCls="icon-remove"  plain="true" onclick="deleteData();" href="javascript:void(0);">删除</a>
		</sec:accesscontrollist>
		
		<sec:accesscontrollist   hasPermission="3" domainObject="pmTaskInfo_tabPower_button_btSearchUserLimitIp" >
			<a id="btSearchUserLimitIp" class="easyui-linkbutton" iconCls="icon-search"  plain="true" onclick="showSearchDialog();" href="javascript:void(0);">查询</a>
		</sec:accesscontrollist>
		
		
	</div>
	
		
	<div data-options="region:'center',split:true,title:''" style="padding:0px;">	
		<!-- <table id="sysLoglist"></table> -->
		<table id="dgUserLimit" class="easyui-datagrid"
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
				
				striped:true,
				
				url: 'platform/sysuserlimitip/sysUserLimitIpController/getSysUserLimitIpListByPage.json',
				
				onClickRow: dgUserLimitOnClickRow,
				onAfterEdit: dgUserLimitOnAfterEdit,
				onLoadSuccess: dgUserLimitOnLoadSuccess
					
				">
			<thead>
				<tr>
					<th data-options="field:'id', halign:'center' ,checkbox:true" >id</th>
					<th data-options="field:'userLimitUserId',halign:'center',align:'left', hidden: true" editor="{type:'validatebox', options: {required: true}}" width="220">受限人ID</th>
					<th data-options="field:'userLimitUserName',halign:'center',align:'left'" editor="{type:'validatebox', options: {required: true}}" width="220">受限人</th>
					<th data-options="field:'limitTypeIpType',required:true,halign:'center',align:'left',formatter: formatIpLimit" editor="{type:'combobox',options:{required:true,panelHeight:'auto',editable:false,valueField:'lookupCode',textField:'lookupName'}}" width="220">IP类型</th>
					<th data-options="field:'limitUserType',required:true,halign:'center',align:'left', formatter: formatUserLimit" editor="{type:'combobox',options:{required:true,panelHeight:'auto',editable:false,valueField:'lookupCode',textField:'lookupName'}}" width="220">用户访问类型</th>
					<th data-options="field:'userLimitIpFrom',halign:'center',align:'left'" editor="{type:'validatebox', options:{required:true, validType: 'ip',tipPosition:'left'}}"  width="220">开始IP</th>
					<th data-options="field:'userLimitIpEnd',halign:'center',align:'left'" editor="{type:'validatebox', options:{required:true, validType: 'ip',tipPosition:'left'}}"  width="220">结束IP</th>
					
					
				</tr>
			</thead>
		</table>	
		
	</div>
	
	<div id="userLimitSearchDialog" class="easyui-dialog" data-options="iconCls:'icon-search',resizable:true,modal:false,buttons:'#userLimitsearchBtns'" 
			style="width: 600px;height:150px; visible: hidden" title="查询条件">
		<form id="userLimitSearchForm">
    		<input id="filter_EQ_userLimitUserId" type='text' name="filter_EQ_userLimitUserId" hidden="true"/>
    		
    		<table>
    			
    			<tr>
    				
    				<td>受限人:</td><td><input id="filter_LIKE_userLimitUserName" type='text' name="filter_LIKE_userLimitUserName" style="width: 150px"/></td>
    				<td>IP类型:</td><td><input id="filter_EQ_limitTypeIpType" class='easyui-combobox' data-options="panelHeight:'auto',editable:true, 
    					valueField:'lookupCode',textField:'lookupName'" name ="filter_EQ_limitTypeIpType"/></td>
    				
    			</tr>
    			<tr>
    				<td>访问类型:</td>
    				<td><input id="filter_EQ_limitUserType" class='easyui-combobox' data-options="panelHeight:'auto',editable:true, 
    					valueField:'lookupCode',textField:'lookupName'" name ="filter_EQ_limitUserType"/></td>
    			</tr>
    		</table>
    	</form>
    	<div id="userLimitsearchBtns">
    		<a class="easyui-linkbutton" plain="false" onclick="searchData();" href="javascript:void(0);">查询</a>
    		<a class="easyui-linkbutton" plain="false" onclick="clearSearchData();" href="javascript:void(0);">清空</a>
    		<a class="easyui-linkbutton" plain="false" onclick="hideSearchDialog();" href="javascript:void(0);">返回</a>
    	</div>
     </div>


</body>
</html>