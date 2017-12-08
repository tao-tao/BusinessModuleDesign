<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<%@ page import="java.util.*"%>
<%@ page import="avicit.platform6.core.spring.SpringFactory"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="sec" uri="/WEB-INF/tags/shiro.tld"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>角色添加用户</title>
<base href="<%=ViewUtil.getRequestPath(request)%>">
<jsp:include page="/avicit/platform6/component/common/EasyUIJsInclude.jsp"></jsp:include>
<jsp:include page="/avicit/platform6/modules/system/commonpopup/commonSelectionHead.jsp"></jsp:include>
<script src="avicit/platform6/component/js/common/CommonDialog.js" type="text/javascript"></script>
<link href="avicit/platform6/modules/system/sysdept/css/icon.css" type="text/css" rel="stylesheet">
<script type="text/javascript">


var path="<%=ViewUtil.getRequestPath(request)%>";
var comboData={};
var userTypeComboData={};


$(document).ready(function(){ 
	initComboData();
	loadUserInfo();
});

/**
 * 
**/
function initComboData(){
	$.ajax({
		url: 'platform/sysrole/getComboData.json',
		data : {'lookupType' : 'PLATFORM_VALID_FLAG'},
		type :'POST',
		dataType :'json',
		success : function(r){
			if(r.lookup){
				comboData =	r.lookup;
				
				$('#filter_EQ_status').combobox('loadData', [{lookupCode:'', lookupName: '所有'}].concat(comboData));
			}
		}
	});
	
	$.ajax({
		url: 'platform/sysrole/getComboData.json',
		data :{'lookupType' : 'PLATFORM_USER_TYPE'},
		type :'POST',
		dataType :'json',
		success : function(r){
			if(r.lookup){
				userTypeComboData =	r.lookup;
				
				
			}
		}
	}); 
}

function loadUserInfo(name, loginName, no, validFlag)
{
	$("#dgUser").datagrid("options").url ="platform/sysrole/getUserListByCondition.json";
	$('#dgUser').datagrid("reload", {filter_LIKE_name: name, filter_LIKE_loginName:loginName, filter_LIKE_no:no, filter_LIKE_status: validFlag} );
	$("#dgUser").datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');	
}

function formatcombobox(value){
	if(value ==null ||value == ''){
		return '';
	}
	for(var i =0 ,length = comboData.length; i<length;i++){
		if(comboData[i].lookupCode == value){
			return comboData[i].lookupName;
		}
	}
}

function formatUserTypeCombobox(value){
	if(value ==null ||value == ''){
		return '';
	}
	for(var i =0 ,length = userTypeComboData.length; i<length;i++){
		if(userTypeComboData[i].lookupCode == value){
			return userTypeComboData[i].lookupName;
		}
	}
}

function clearUser()
{
	$('#filter_LIKE_name').val('');
	$('#filter_LIKE_loginName').val('');
	$('#filter_LIKE_no').val('');
	$('#filter_EQ_status').combobox('select', '');
}

function searchUser()
{
	var name=$('#filter_LIKE_name').val();
	var loginName=$('#filter_LIKE_loginName').val();
	var no=$('#filter_LIKE_no').val();	
	var validFlag= $('#filter_EQ_status').combobox('getValue');
	
	//alert(name+","+loginName+","+no+","+validFlag);
	
	loadUserInfo(name, loginName, no, validFlag);
}



</script>
</head>

<body class="easyui-layout" fit="true">


		
	<div data-options="region:'north',split:false,title:''" style="height: 90px; padding:0px;">	
	
		<div id="searchUser" class="easyui-panel " data-options="iconCls:'icon-search'" 
			style=" visible: hidden" title="查询">
			
			
			<div style="TEXT-ALIGN: center; ">
				<form id="searchUserForm">
		    		<table style=" MARGIN-RIGHT: auto; MARGIN-LEFT: auto;">
		    			<tr>
		    				<td>用户名:</td><td><input type='text' name="filter_LIKE_name" id="filter_LIKE_name" style="width:100px"/></td>
		    				<td>登录名:</td><td><input type='text' name="filter_LIKE_loginName" id="filter_LIKE_loginName" style="width:100px"/></td>
		    				<td>用户编号:</td><td><input type='text' name="filter_LIKE_no" id="filter_LIKE_no" style="width:100px"/></td>
		    				<td>是否有效:</td><td><input id="filter_EQ_status" class="easyui-combobox" name="filter_EQ_status" style="width:100px" 
	    						 data-options="panelHeight:'auto', editable:false,valueField:'lookupCode',textField:'lookupName', data: [{lookupCode:'1', lookupName: 'aaa'}]" /> </td>
		    				
		    			</tr>
		    			
		    		</table>
		    	</form>
		    </div>	
		    
		    <div style="TEXT-ALIGN: center; ">
		    
		    	<div  id="searchBtns" style=" MARGIN-RIGHT: auto; MARGIN-LEFT: auto;">
		    		<a class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="searchUser();" href="javascript:void(0);">查询</a>
		    		<a class="easyui-linkbutton" iconCls="icon-cancel" plain="true" onclick="clearUser();" href="javascript:void(0);">清空</a>
		    		
		    	</div>
	    	</div>
	    </div>
	</div>
	
	<div data-options="region:'center',split:false,title:''" style="height:0; overflow:hidden; font-size:0;">				
		<table id="dgUser" class="easyui-datagrid"
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
				singleSelect: false,
				checkOnSelect: false,
				selectOnCheck: true,
				
				pagination:true,
				pageSize:dataOptions.pageSize,
				pageList:dataOptions.pageList,
				
				striped:true,
				url: 'platform/sysuser/getUserListByCondition.json'	
				">
			<thead>
				<tr>
					<th data-options="field:'id', halign:'center',checkbox:true" width="220">id</th>
					
					<th data-options="field:'name',required:true,halign:'center',align:'left'" editor="{type:'text'}" width="220">用户名</th>
					<th data-options="field:'nameEn',required:true,halign:'center',align:'left'" editor="{type:'text'}" width="220">英文名</th>
					<th data-options="field:'no',required:true,halign:'center',align:'left'" editor="{type:'text'}" width="220">用户编号</th>
					<th data-options="field:'loginName',halign:'center',align:'left'" editor="{type:'text'}"  width="220">登录名</th>
					<th data-options="field:'type',halign:'center',align:'left', formatter:formatUserTypeCombobox" editor="{type:'combobox', options:{required:true,panelHeight:'auto',editable:false,valueField:'lookupCode',textField:'lookupName'}}"  width="220">类型</th>
					<th data-options="field:'status',halign:'center',align:'left', formatter:formatcombobox" editor="{type:'combobox', options:{required:true,panelHeight:'auto',editable:false,valueField:'lookupCode',textField:'lookupName'}}"  width="220">状态</th>
					<th data-options="field:'remark',halign:'center',align:'left'" editor="{type:'text'}"  width="220">描述</th>
					
				</tr>
			</thead>
		</table>	
	</div>
		
		
	

</body>
</html>