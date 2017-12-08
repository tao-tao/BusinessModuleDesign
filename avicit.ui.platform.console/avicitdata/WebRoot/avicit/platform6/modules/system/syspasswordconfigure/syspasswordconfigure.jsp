<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<%@ page import="java.util.*"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec" uri="/WEB-INF/tags/shiro.tld"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>密码配置管理</title>
<base href="<%=ViewUtil.getRequestPath(request)%>">
<jsp:include page="/avicit/platform6/component/common/EasyUIJsInclude.jsp"></jsp:include>
<jsp:include page="/avicit/platform6/modules/system/commonpopup/commonSelectionHead.jsp"></jsp:include>
<script src="avicit/platform6/component/js/common/exportData.js" type="text/javascript"></script>
<link href="avicit/platform6/modules/system/sysdept/css/icon.css" type="text/css" rel="stylesheet">
<script src="avicit/platform6/modules/system/syspasswordconfigure/syspasswordconfigure.js" type="text/javascript"></script>
</head>

<body class="easyui-layout" fit="true">

		<div data-options="region:'north',split:true,title:'密码配置管理'" style="height: 250px; padding:0px;">
		
			<div id="toolbarPassword" class="datagrid-toolbar">
					<a id="btAddPassword"  class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addData();" href="javascript:void(0);">添加</a>
					<a id="btEditPassword"  class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editData();" href="javascript:void(0);">编辑</a>
					<a id="btDeletePassword" class="easyui-linkbutton" iconCls="icon-remove"  plain="true" onclick="deleteData();" href="javascript:void(0);">删除</a>
			</div>
			<table id="dgPassword" class="easyui-datagrid"
				data-options=" 
					fit: true,
					border: false,
					rownumbers: true,
					animate: true,
					collapsible: false,
					fitColumns: true,
					autoRowHeight: false,
					toolbar:'#toolbarPassword',
					idField :'id',
					singleSelect: true,
					checkOnSelect: true,
					selectOnCheck: false,
					pagination:true,
					pageSize:10,
					pageList:dataOptions.pageList,
					striped:true,
					onLoadSuccess:loadSuccess,
					url: 'platform/syspasswordtemplate/sysPasswordTemplateController/querySysPasswordTempletLevel.json',
					onClickRow: dgPasswordOnClickRow
					">
				<thead>
					<tr>
						<th data-options="field:'id', halign:'center',checkbox:true" width="220">id</th>
						<th data-options="field:'key', halign:'center',align:'left'" editor="{type:'text'}" width="220">密码模板名称</th>
						<th data-options="field:'code', halign:'center',align:'left'" editor="{type:'text'}" width="220">密码模板标识</th>
						<th data-options="field:'userLevelCode', halign:'center',align:'left',formatter:formatUserLevelCode" editor="{type:'text'}" width="220">用户密级</th>
					</tr>
				</thead>
			</table>		
		</div>	
		
		<div id="passwordDialog" class="easyui-dialog" data-options="iconCls:'icon-search',resizable:true,modal:false,buttons:'#searchBtns'" 
			style="width: 600px;height:200px; visible: hidden" title="添加">
			<form id="passwordEditForm" name="passwordEditForm" method="post" >
				<input type="hidden"   name="id" id="id" ></input>
				<div class="formExtendBase" >
					<div class="formUnit column1">
						<label class="labelbg">密码模板名称：</label>
						<div class="inputContainer">
							<input class="easyui-validatebox"  name="key" id="key" style="width:80%"></input>
						</div>
					</div>
					<div class="formUnit column1">
						<label class="labelbg">密码模板标识：</label>
						<div class="inputContainer">
							<input class="easyui-validatebox"  name="code" id="code" style="width:80%"></input>
						</div>
					</div>
					<div class="formUnit column1">
						<label class="labelbg">用户密级：</label>
						<div class="inputContainer">
							<input class="easyui-combobox" data-options="panelHeight:'auto',editable:false,width:352,valueField:'lookupCode',textField:'lookupName'" name="userLevelCode" id="userLevelCode" style="width:80%"></input>
						</div>
					</div>
				</div>
			</form>
	    	<div id="searchBtns">
	    		<a class="easyui-linkbutton" plain="false" onclick="saveData();" href="javascript:void(0);">确认</a>
	    		<a class="easyui-linkbutton" plain="false" onclick="hideDialog();" href="javascript:void(0);">取消</a>
	    	</div>
	    </div>
		<div data-options="region:'center',split:true,title:''" style="padding:0px;">	
		
			<div id="toolbarUser" class="datagrid-toolbar">
				<a id="btSearchUser" class="easyui-linkbutton" iconCls="icon-save"  plain="true" onclick="savePasswordTemplate();" href="javascript:void(0);">保存</a>
			</div>
					
			<table id="dgPasswordTemplate" class="easyui-datagrid"
				data-options=" 
					fit: true,
					border: false,
					rownumbers: true,
					animate: true,
					collapsible: false,
					fitColumns: true,
					autoRowHeight: false,
					toolbar:'#toolbarUser',
					idField :'id',
					singleSelect: true,
					checkOnSelect: true,
					selectOnCheck: false,
					
					pagination:true,
					pageSize:dataOptions.pageSize,
					pageList:dataOptions.pageList,
					
					striped:true,
					onClickRow: dgPasswordTemplateOnClickRow,
					onAfterEdit: dgPasswordTemplateOnAfterEdit,
					onLoadSuccess: dgPasswordTemplateOnLoadSuccess
							
					">
				<thead>
					<tr>
						<th data-options="field:'id', halign:'center',checkbox:true" width="220">id</th>
						<th data-options="field:'templetKey',required:true,halign:'center',align:'left'" editor="{type:'text'}" width="220">密码规则</th>
						<th data-options="field:'templetKeyDesc',required:true,halign:'center',align:'left'" editor="{type:'text'}" width="220">密码规则描述</th>
						<th data-options="field:'templetValue',required:true,halign:'center',align:'left'" editor="{type:'numberbox', options: {max: 99999999}}" width="220">密码规则值</th>
						<th data-options="field:'templetState',halign:'center',align:'left', formatter: formatAvailable" editor="{type:'combobox',options:{required:true,panelHeight:'auto',editable:false,valueField:'lookupCode',textField:'lookupName'}}"  width="220">是否可用</th>
					</tr>
				</thead>
			</table>	
		</div>
</body>
</html>