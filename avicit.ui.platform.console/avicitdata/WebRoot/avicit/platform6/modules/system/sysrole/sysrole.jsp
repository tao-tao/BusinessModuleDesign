<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="avicit.platform6.api.session.SessionHelper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<%@ page import="java.util.*"%>
<%@ page import="avicit.platform6.core.spring.SpringFactory"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="sec" uri="/WEB-INF/tags/shiro.tld"%>
<html>
<head>
<%
String appId = SessionHelper.getApplicationId();
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>角色管理</title>
<base href="<%=ViewUtil.getRequestPath(request)%>">
<jsp:include page="/avicit/platform6/component/common/EasyUIJsInclude.jsp"></jsp:include>
<jsp:include page="/avicit/platform6/modules/system/commonpopup/commonSelectionHead.jsp"></jsp:include>

<script src="static/js/platform/component/common/exportData.js" type="text/javascript"></script>


<link href="avicit/platform6/modules/system/sysdept/css/icon.css" type="text/css" rel="stylesheet">
<script type="text/javascript">




var path="<%=ViewUtil.getRequestPath(request)%>";
var currTreeNode;
var editIndex = -1;
var validComboData={};
var roleTypeComboData ={};
var unableModifyComboData = {};
var appComboData ={};
var appid="<%=appId%>"; 
var newRowCount=0;

$(document).ready(function(){ 
	initComboData();
	$('#searchRoleDialog').dialog('close');
	$('#searchUserDialog').dialog('close');
	loadUserInfo(null);
	
	$('#searchGroupForm').find('input').on('keyup',function(e){
		if(e.keyCode == 13){
			searchRole();
		}
	});
	$('#searchUserForm').find('input').on('keyup',function(e){
		if(e.keyCode == 13){
			searchUser();
		}
	});
	
	
});


/**
 * 组织部门树单击事件
 */
function appTreeOnClickRow(row){
	currTreeNode=row;
	
	loadRoleInfo(row.id);
	loadUserInfo(null);
};



/**
 * 加载群组信息
 */
function loadRoleInfo(appId){
	
	$("#dgRole").datagrid("options").url ="platform/sysrole/getSysRoleListByPage.json";
	$('#dgRole').datagrid("reload", {filter_EQ_sysApplicationId: appId});
	$("#dgRole").datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');		
};

function loadSearchRoleInfo(appId, roleName, roleCode, roleGroup, roleType)
{
	$("#dgRole").datagrid("options").url ="platform/sysrole/getSysRoleListByPage.json";
	$('#dgRole').datagrid("reload", 
			{filter_EQ_sysApplicationId: appId, 
				filter_LIKE_roleName: roleName, 
				filter_LIKE_roleCode: roleCode, 
				filter_LIKE_roleGroup: roleGroup, 
				filter_LIKE_roleType: roleType}
	);
	$("#dgRole").datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');	
};


function dgRoleOnClickRow(index,rowData)
{
	newRowCount++;
	
	var myDatagrid=$('#dgRole');
	myDatagrid.datagrid('endEdit', editIndex);
	if(editIndex==-1)
	{
		myDatagrid.datagrid('beginEdit', index);  
		editIndex=index;
		
				
		
		// 有效无效的下拉框
		var ed = myDatagrid.datagrid('getEditor',{index:editIndex,field: 'validFlag'});
		$(ed.target).combobox('loadData', validComboData);
		
		// 系统角色的下拉框
		var ed = myDatagrid.datagrid('getEditor',{index:editIndex,field: 'roleType'});
		$(ed.target).combobox('loadData', roleTypeComboData);
		
		var ed = myDatagrid.datagrid('getEditor',{index:editIndex,field: 'usageModifier'});
		$(ed.target).combobox('loadData', unableModifyComboData);
		
		// 应用的下拉框
		var ed = myDatagrid.datagrid('getEditor',{index:editIndex,field: 'sysApplicationId'});
		$(ed.target).combobox('loadData', appComboData);
		
		//选择部门, 
		
		var ed = myDatagrid.datagrid('getEditor',{index: index, field: 'sysDeptName'});
		$(ed.target).attr('id', "receptDeptName"+newRowCount);
		$(ed.target).attr('disabled', true);
		
		var ed = myDatagrid.datagrid('getEditor',{index: index, field: 'sysDeptId'});
		$(ed.target).attr('id', "receptDeptId"+newRowCount);
		//$(ed.target).attr('disabled', true);
		
				
	   	var deptCommonSelector = new CommonSelector("dept","deptSelectCommonDialog","receptDeptId"+newRowCount,"receptDeptName"+newRowCount);
		deptCommonSelector.init(); //选择部门 
	   	
		// 获取群组中的用户列表
	    loadUserInfo(rowData.id);
	}
	else
	{
		$.messager.alert('提示','不能编辑，请确保上一条数据填写完整','warning');
	}
	
    
};

function dgRoleOnAfterEdit(rowIndex, rowData, changes)
{
	//成功完成编辑，包括校验
	editIndex=-1;
};

function dgRoleOnLoadSuccess(data)
{
	if(editIndex != -1){
		$('#dgRole').datagrid('cancelEdit',editIndex);
		editIndex = -1;
	}
};


/**
 * 
**/
function initComboData(){
	$.ajax({
		url: 'platform/syslookuptype/getLookUpCode/PLATFORM_VALID_FLAG.json',
		type :'get',
		async:false,
		dataType :'json',
		success : function(r){
			if(r){
				validComboData =r;
			}
		}
	});
	
	$.ajax({
		url: 'platform/syslookuptype/getLookUpCode/PLATFORM_SYS_ROLETYPE.json',
		type :'get',
		async:false,
		dataType :'json',
		success : function(r){
			if(r){
				roleTypeComboData =r;
			}
		}
	});
	
	
	$.ajax({
		url: 'platform/syslookuptype/getLookUpCode/PLATFORM_USAGE_MODIFIER.json',
		type :'get',
		async:false,
		dataType :'json',
		success : function(r){
			if(r){
				unableModifyComboData =r;
			}
		}
	});
	
	$.ajax({
		url: 'platform/sysApps/querySysApplication.json',
		data :{},
		type :'POST',
		dataType :'json',
		success : function(r){
			if(r.data){
				appComboData =	r.data;
			}
		}
	}); 
	
	
};

function formatcombobox(value){
	
	
	if(value ==null ||value == ''){
		return '';
	}
	for(var i =0 ,length = validComboData.length; i<length;i++){
				
		if(validComboData[i].lookupCode == value){
						
			return validComboData[i].lookupName;
		}
	}
};
function formatcombobox2(value){
	if(value ==null ||value == ''){
		return '';
	}
	for(var i =0 ,length = roleTypeComboData.length; i<length;i++){
		if(roleTypeComboData[i].lookupCode == value){
			return roleTypeComboData[i].lookupName;
		}
	}
};

function formatcombobox4(value){
	if(value ==null ||value == ''){
		return '';
	}
	for(var i =0 ,length = unableModifyComboData.length; i<length;i++){
		if(unableModifyComboData[i].lookupCode == value){
			return unableModifyComboData[i].lookupName;
		}
	}
};

function formatcombobox3(value){
	if(value ==null ||value == ''){
		return '';
	}
	for(var i =0 ,length = appComboData.length; i<length;i++){
		if(appComboData[i].id == value){
			return appComboData[i].text;
		}
	}
};

/*增加一条记录*/

function showAddRole()
{
	newRowCount++;
	
	var myDatagrid=$('#dgRole');
	myDatagrid.datagrid('endEdit',editIndex);
	if(editIndex != -1){
		$.messager.alert('提示','不能添加，请确保上一条数据填写完整','warning');
		return;
	}
	myDatagrid.datagrid('insertRow',{
		index: 0,
		row:{id:"", roleType:"1", sysApplicationId: appid,
			validFlag: '1', orderBy: '0',usageModifier:'0'}
		});	
	myDatagrid.datagrid('selectRow', 0).datagrid('beginEdit',0);
	editIndex=0;
			
	// 有效无效的下拉框
	var ed = myDatagrid.datagrid('getEditor',{index:editIndex,field: 'validFlag'});
	$(ed.target).combobox('loadData', validComboData);
	
	// 系统角色的下拉框
	var ed = myDatagrid.datagrid('getEditor',{index:editIndex,field: 'roleType'});
	$(ed.target).combobox('loadData', roleTypeComboData);
	
	var ed = myDatagrid.datagrid('getEditor',{index:editIndex,field: 'usageModifier'});
	$(ed.target).combobox('loadData', unableModifyComboData);
	
	// 应用的下拉框
	var ed = myDatagrid.datagrid('getEditor',{index:editIndex,field: 'sysApplicationId'});
	$(ed.target).combobox('loadData', appComboData);
	
	//选择部门, 
	
	var ed = myDatagrid.datagrid('getEditor',{index: editIndex, field: 'sysDeptName'});
	$(ed.target).attr('id', "receptDeptName"+newRowCount);
	$(ed.target).attr('disabled', true);
	
	var ed = myDatagrid.datagrid('getEditor',{index: editIndex, field: 'sysDeptId'});
	$(ed.target).attr('id', "receptDeptId"+newRowCount);
	//$(ed.target).attr('disabled', true);
	
			
   	var deptCommonSelector = new CommonSelector("dept","deptSelectCommonDialog","receptDeptId"+newRowCount,"receptDeptName"+newRowCount);
	deptCommonSelector.init(); //选择部门 
	
	// 清空用户
	loadUserInfo(null);
	
};


function saveRole()
{
	var myDatagrid=$('#dgRole');
	myDatagrid.datagrid('endEdit',editIndex);
	
	if(editIndex!=-1)
	{
		$.messager.alert('提示','不能保存，请确保上一条数据填写完整','warning');
		return;
	}
	
	var rows = myDatagrid.datagrid('getChanges');
	
	var orderBycheck = false;
	for(var i=0;i<rows.length;i++) {
		if (rows[i].orderBy < 0) {
			orderBycheck = true;
		}
	}
	
	if (orderBycheck) {
		 $.messager.show({
			 title : '提示',
			 msg : '排序字段不能小于0'
		 });
		return;
	}
	
	
	var data =JSON.stringify(rows);
	
	if(rows.length > 0)
	{
		var reg =/\s/;
		for (var i=0;i<rows.length;i++){
			if(reg.test(rows[i].roleName)){
				$.messager.alert('提示',"角色名称含有空格字符，请检查！",'warning');
				return;
			}
			if(reg.test(rows[i].roleCode)){
				$.messager.alert('提示',"角色代码含有空格字符，请检查！",'warning');
				return;
			}
			if(reg.test(rows[i].roleGroup)){
				$.messager.alert('提示',"角色群组含有空格字符，请检查！",'warning');
				return;
			}
		}
		
		 $.ajax({
			 url:'platform/sysrole/saveSysRole.json',
			 data : {datas : data},
			 type : 'post',
			 dataType : 'json',
			 success : function(r){
				if(r.result==0){
					 loadRoleInfo(appid);
					 $.messager.show({
						 title : '提示',
						 msg : '保存成功'
					 });
				 }else{
					 $.messager.alert('提示',r.msg,'warning');
				 } 
			 }
		 });
	 } 
};

/*删除选中数据*/
function deleteRole(){
  
	var myDatagrid=$('#dgRole');
	
	var rows = myDatagrid.datagrid('getChecked');
	var data =JSON.stringify(rows);
	
	if (rows.length > 0) {
		$.messager.confirm('请确认', '您确定要删除当前所选的数据？', function(b) {
			if (b) {
				
				
				$.ajax({
					url : 'platform/sysrole/deleteSysRole.json',
					data : {
						datas : data
					},
					type : 'post',
					dataType : 'json',
					success : function(r) {
						
						if(r.result==0){
							loadRoleInfo(appid);
							loadUserInfo(null);
							editIndex = -1;
							$.messager.show({
								title : '提示',
								msg : '删除成功！'
							});
						}
						else
						{
							$.messager.alert('提示',r.msg,'warning');
						}
					}
				}); 
			}
		});
	} else {
		$.messager.alert('提示', '请选择要删除的记录！', 'warning');
	}
};

function showSearchRole()
{
	$('#searchRoleDialog').dialog('open');
		
	$('#filter_EQ_roleType').combobox('loadData', [{lookupCode: '', lookupName:'所有'}].concat(roleTypeComboData));
	//$('#filter_EQ_g_valid_flag').combobox('loadData', comboData);
};

function hideSearchRole()
{
	$('#searchRoleDialog').dialog('close');
};

function searchRole()
{
	
	var roleName= $('#filter_LIKE_roleName').val();
	var roleCode= $('#filter_LIKE_roleCode').val();
	var roleGroup= $('#filter_LIKE_roleGroup').val();
	var roleType= $('#filter_EQ_roleType').combobox('getValue');
	
	expSearchParams={filter_LIKE_roleName: roleName, filter_LIKE_roleCode: roleCode, filter_LIKE_roleGroup: roleGroup, filter_EQ_roleType: roleType};
	
	//alert(searchName+","+validFlag);
	
	loadSearchRoleInfo(appid, roleName, roleCode, roleGroup, roleType);
};

function clearRole()
{
	$('#filter_LIKE_roleName').val('');
	$('#filter_LIKE_roleCode').val('');
	$('#filter_LIKE_roleGroup').val('');
	$('#filter_EQ_roleType').combobox('select', '');
};


function setLanguage(){
	var rows = $('#dgGroup').datagrid('getChecked');
	
	if (rows.length == 1) {
				
		var usd = new CommonDialog("groupTlSelectDialog","800","400","platform/sysgroup/sysGroupController/toLanguageListView?id="+rows[0].id,"设置多语言",null,null,false);
		usd.show();
		
	}
	else
	{
		$.messager.alert('提示',"请选择一个角色！",'warning');
	}
	
};

function loadUserInfo(roleId)
{
	//filter_EQ_ug_groupId
	
	if(roleId==null)
	{
		
		$('#dgUser').datagrid("loadData", []);
		$("#dgUser").datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
	}
	else
	{
		$("#dgUser").datagrid("options").url ="platform/sysrole/getUerListByRoleId.json";
		$('#dgUser').datagrid("reload", {filter_EQ_ur_sysRoleId: roleId});
		$("#dgUser").datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');	
	}
}
function showAddUser(){
	var row=$('#dgRole').datagrid('getChecked');
	if (row.length==0){
		$.messager.alert('提示','请选择一个角色','warning');
		return;
	}else if(row.length>1){
		$.messager.alert('提示','只能选择一个角色','warning');
		return;
	}
	var comSelector = new CommonSelector("user","userSelectCommonDialog","deptId","deptName",null,null,null,false,null,null,600,400);
	comSelector.init(false,'selectUserDialogCallBack','n'); //选择人员  回填部门 */
	
};

function selectUserDialogCallBack(data){
	var row=$('#dgRole').datagrid('getChecked');
	var roleId=row[0].id;
	var ids = [];
	var l =data.length;
	if (l>0){
	for(;l--;){
		 ids.push(data[l].userId);
	 }
	$.ajax({
        cache: true,
        type: "POST",
        url:'platform/sysrole/saveUserRole.json',
        dataType:"json",
        data: {roleIds: roleId, ids: ids.join(',')},
        error: function(request) {
        	$.messager.alert('提示','操作失败，服务请求状态：'+request.status+' '+request.statusText+' 请检查服务是否可用！','warning');
        },
        success: function(data) {
			if(data.result==0){
				
				loadUserInfo(roleId);
				
				$.messager.show({
					title : '提示',
					msg : '添加成功,已存在用户不会被重新添加！'
				});
			}else{
				$.messager.alert('提示',data.msg,'warning');
			}
        }
    }); 
	}
};
	
function deleteUser()
{
	
	if(editIndex==-1)
	{
		$.messager.alert('提示','请选择一个角色','warning');
		return;
	}
	
	var row=$('#dgRole').datagrid('getRows')[editIndex];
	var roleId=row.id;
	
		
	var rows = $('#dgUser').datagrid('getChecked');
	var data =JSON.stringify(rows);
	
	if (rows.length > 0) {
		$.messager.confirm('请确认', '您确定要删除当前所选的数据？', function(b) {
			if (b) {
								
				$.ajax({
					url : 'platform/sysrole/deleteUserRole.json',
					data : {
						datas : data, roleId: roleId
					},
					type : 'post',
					dataType : 'json',
					success : function(r) {
						
						if(r.result==0){
							
							loadUserInfo(roleId);
							
							$.messager.show({
								title : '提示',
								msg : '删除成功！'
							});
						}
						else
						{
							$.messager.alert('提示',r.msg,'warning');
						}
					}
				}); 
			}
		});
	} else {
		$.messager.alert('提示', '请选择要删除的记录！', 'warning');
	}
};

function showSearchUser()
{
	$('#filter_EQ_u_status').combobox('loadData', [{lookupCode: '', lookupName:'所有'}].concat(validComboData));
	$('#searchUserDialog').dialog("open");
};


function hideSearchUser()
{
	$('#searchUserDialog').dialog('close');
};

function searchUser()
{
	if(editIndex==-1)
	{
		$.messager.alert('提示','请选择一个角色','warning');
		return;
	}
	
	var row=$('#dgRole').datagrid('getRows')[editIndex];
	var roleId=row.id;
	
	
	var name= $('#filter_LIKE_u_name').val();
	var loginName= $('#filter_LIKE_u_loginName').val();
	var status= $('#filter_EQ_u_status').combobox('getValue');
	//alert(searchName+","+validFlag);
	
	loadSearchUserInfo(roleId, name, loginName,status);
};

function clearUser()
{
	$('#filter_LIKE_u_name').val('');
	$('#filter_LIKE_u_loginName').val('');
	$('#filter_EQ_u_status').combobox('select', '');
};

function loadSearchUserInfo(roleId, name, loginName,status)
{
	
	$("#dgUser").datagrid("options").url ="platform/sysrole/getUerListByRoleId.json";
	$('#dgUser').datagrid("reload", {filter_EQ_ur_sysRoleId: roleId,filter_EQ_u_status: status, filter_LIKE_u_name: name, filter_LIKE_u_loginName: loginName});
	$("#dgUser").datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');	
	
	
};



var expSearchParams={};
 
function importRole()
{
	var imp = new CommonDialog("importData","700","400",'platform/excelImportController/excelimport/importRole/xls',"Excel数据导入",false,true,false);
	imp.show();
}


function closeImportData(){
	$("#importData").dialog('close');
}

/**
 * 导出岗位(客户端数据)
 */
function exportClientData(){
	$.messager.confirm('确认','是否确认导出Excel文件?',function(r) {
        if (r) {
            //封装参数
            var columnFieldsOptions = getGridColumnFieldsOptions('dgRole');
            var dataGridFields = JSON.stringify(columnFieldsOptions[0]);
            var rows = $('#dgRole').datagrid('getRows');
            var datas = JSON.stringify(rows);
            var myParams = {
                dataGridFields: dataGridFields,//表头信息集合
                datas: datas,//数据集
                hasRowNum : true,//默认为Y:代表第一列为序号
                sheetName: 'sheet1',//如果该参数为空，默认为导出的Excel文件名
                unContainFields : 'id,roleGroup,sysDeptId,sysApplicationId,roleType',
                fileName: '平台角色导出数据'+ new Date().toString()//导出的Excel文件名
            };
            //var url = "platform/commonExcelController/exportExcelClientData";
            var url = "platform/sysrole/exportClient";
            var ep = new exportData("xlsExport","xlsExport",myParams,url);
            ep.excuteExport();
        }
       });
}
/**
 * 导出岗位(服务端数据)
 */
function exportServerData(){
	$.messager.confirm('确认','是否确认导出Excel文件?',function(r) {
        if (r) {
            //封装参数
            var columnFieldsOptions = getGridColumnFieldsOptions('dgRole');
            var dataGridFields = JSON.stringify(columnFieldsOptions[0]);
            expSearchParams = expSearchParams?expSearchParams:{};
     
            expSearchParams.dataGridFields=dataGridFields;
            expSearchParams.hasRowNum=true;
            expSearchParams.sheetName='sheet1';
            expSearchParams.fileName='平台角色导出数据'+ new Date().toString();
            expSearchParams.unContainFields='id,roleGroup,sysDeptId,sysApplicationId,roleType';
            
            expSearchParams.filter_EQ_sysApplicationId=appid;
            var roleName= $('#filter_LIKE_roleName').val();
        	var roleCode= $('#filter_LIKE_roleCode').val();
        	var roleType= $('#filter_EQ_roleType').combobox('getValue');
        	
        	expSearchParams.filter_LIKE_roleName=roleName;
        	expSearchParams.filter_LIKE_roleCode=roleCode;
        	expSearchParams.filter_EQ_roleType=roleType;
        	
            //var url = "platform/commonExcelController/exportExcelClientData";
            var url = "platform/sysrole/exportServer";

            var ep = new exportData("xlsExport","xlsExport",expSearchParams,url);
            ep.excuteExport();
        }
       });
}

</script>
</head>

<body class="easyui-layout" fit="true">
		<div data-options="region:'north',split:true,title:''" style="height: 300px; padding:0px;">
		
			<div id="toolbarRole" class="datagrid-toolbar">
				<sec:accesscontrollist   hasPermission="3" domainObject="sysRole_tabPower_button_btAddRole" >
					<a id="btAddRole"  class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="showAddRole();" href="javascript:void(0);">添加</a>
				</sec:accesscontrollist>
				
				
				<sec:accesscontrollist   hasPermission="3" domainObject="sysRole_tabPower_button_btSaveRole" >
					<a id="btSaveRole"  class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="saveRole();" href="javascript:void(0);">保存</a>
				</sec:accesscontrollist>
				
				<td><a id="btImportRole" class="easyui-linkbutton" iconCls="icon-import"  plain="true" onclick="importRole();" href="javascript:void(0);">导入角色</a></td>
				 
				<sec:accesscontrollist   hasPermission="3" domainObject="sysRole_tabPower_button_btImportRole" >
					<a class="easyui-menubutton"  data-options="menu:'#export',iconCls:'icon-export'" href="javascript:void(0);">导出角色</a>
					<div id="export" style="width:150px;">
						<div data-options="iconCls:'icon-excel'" onclick="exportClientData();">Excel导出(客户端)</div>
						<div data-options="iconCls:'icon-excel'" onclick="exportServerData();">Excel导出(服务器端)</div>
					</div>
				</sec:accesscontrollist>
				
				<sec:accesscontrollist   hasPermission="3" domainObject="sysRole_tabPower_button_btDeleteRole" >
					<a id="btDeleteRole" class="easyui-linkbutton" iconCls="icon-remove"  plain="true" onclick="deleteRole();" href="javascript:void(0);">删除</a>
				</sec:accesscontrollist>
				
				<sec:accesscontrollist   hasPermission="3" domainObject="sysRole_tabPower_button_btSearchRole" >
					<a id="btSearchRole" class="easyui-linkbutton" iconCls="icon-search"  plain="true" onclick="showSearchRole();" href="javascript:void(0);">查询</a>
				</sec:accesscontrollist>
				
			</div>
			
			
					
			<table id="dgRole" class="easyui-datagrid"
				data-options=" 
					fit: true,
					border: false,
					rownumbers: true,
					animate: true,
					collapsible: false,
					fitColumns: true,
					autoRowHeight: false,
					toolbar:'#toolbarRole',
					idField :'id',
					singleSelect: true,
					checkOnSelect: true,
					selectOnCheck: false,
					url: 'platform/sysrole/getSysRoleListByPage.json',
					queryParams:{filter_EQ_sysApplicationId: '<%=appId%>'},
					pagination:true,
					pageSize:10,
					pageList:dataOptions.pageList,
					
					striped:true,
					
					onClickRow: dgRoleOnClickRow,
					onAfterEdit: dgRoleOnAfterEdit,
					onLoadSuccess: dgRoleOnLoadSuccess
					">
				<thead>
					<tr>
						<th data-options="field:'id', halign:'center',checkbox:true" width="220">id</th>
						<th data-options="field:'roleName', halign:'center',align:'left'" editor="{type:'validatebox',options:{required:true, validType: 'length[0,50]'}}" width="220"><font color="red">*</font>角色名称</th>
						<th data-options="field:'roleCode', halign:'center',align:'left'" editor="{type:'validatebox',options:{required:true, validType: 'length[0,50]'}}" width="220"><font color="red">*</font>角色编码</th>
						<th data-options="field:'roleGroup', hidden:true,halign:'center',align:'left'" editor="{type:'validatebox',options:{ validType: 'length[0,50]'}}" width="220">角色群组</th>
						<th data-options="field:'sysDeptId', halign:'center',align:'left', hidden: true" editor="{type:'text'}"" width="220">所属部门ID</th>
						<th data-options="field:'sysDeptName', halign:'center',align:'left'" editor="{type:'validatebox',options:{validType: 'length[0,100]'}}"" width="220">所属部门</th>
						<th data-options="field:'roleType', hidden:true,halign:'center',align:'left', formatter: formatcombobox2" editor="{type:'combobox',options:{required:true,panelHeight:'auto',editable:false,valueField:'lookupCode',textField:'lookupName'}}" width="220">角色类型</th>
						<th data-options="field:'usageModifier', halign:'center',align:'left', formatter: formatcombobox4" editor="{type:'combobox',options:{required:true,panelHeight:'auto',editable:false,valueField:'lookupCode',textField:'lookupName'}}" width="220">应用范围</th>
						<th data-options="field:'sysApplicationId',hidden:true,required:true,halign:'center',align:'left', formatter: formatcombobox3" editor="{type:'combobox',options:{required:true,panelHeight:'auto',editable:false,valueField:'id',textField:'text'}}" width="220">所属应用</th>
						<th data-options="field:'desc',halign:'center',align:'left'" editor="{type:'validatebox',options:{validType: 'length[0,240]'}}"  width="220">描述</th>
						<th data-options="field:'orderBy',halign:'center',align:'left'" editor="{type:'numberbox',options:{required:true, validType: 'length[0,10]'}}"  width="220"><font color="red">*</font>排序编号</th>
						<th data-options="field:'validFlag',halign:'center',align:'left', formatter: formatcombobox" editor="{type:'combobox',options:{required:true,panelHeight:'auto',editable:false,valueField:'lookupCode',textField:'lookupName'}}"  width="220">状态</th>
					</tr>
				</thead>
			</table>		
				
		</div>	
		
		<div id="searchRoleDialog" class="easyui-dialog" data-options="iconCls:'icon-search',resizable:true,modal:false,buttons:'#searchBtns'" 
			style="width: 600px;height:150px; visible: hidden" title="搜索角色">
			<form id="searchGroupForm">
	    		<table>
	    			<tr>
	    				<td>角色名称:</td><td><input type='text' name="filter_LIKE_roleName" id="filter_LIKE_roleName"/></td>
	    				<td>角色代码:</td><td><input type='text' name="filter_LIKE_roleCode" id="filter_LIKE_roleCode"/></td>
	    			</tr>
	    			
	    			<tr>
	    				<td>角色群组:</td><td><input type='text' name="filter_LIKE_roleGroup" id="filter_LIKE_roleGroup"/></td>
	    				<td>角色类型:</td><td><input id="filter_EQ_roleType" class="easyui-combobox" name="filter_EQ_roleType"  
    						 data-options="panelHeight:'auto', editable:false,valueField:'lookupCode',textField:'lookupName'" /> </td>
	    				
	    			</tr>
	    			
	    		</table>
	    	</form>
	    	<div id="searchBtns">
	    		<a class="easyui-linkbutton" onclick="searchRole();" href="javascript:void(0);">查询</a>
	    		<a class="easyui-linkbutton" onclick="clearRole();" href="javascript:void(0);">清空</a>
	    		<a class="easyui-linkbutton" onclick="hideSearchRole();" href="javascript:void(0);">返回</a>
	    	</div>
	    </div>
		
		<div data-options="region:'center',split:true,title:''" style="padding:0px;">	
		
			<div id="toolbarUser" class="datagrid-toolbar">
				<sec:accesscontrollist   hasPermission="3" domainObject="sysRole_tabPower_button_btAddUser" >
					<a id="btAddUser"  class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="showAddUser();" href="javascript:void(0);">添加用户</a>
				</sec:accesscontrollist>
				
				
				<sec:accesscontrollist   hasPermission="3" domainObject="sysRole_tabPower_button_btDeleteUser" >
					<a id="btDeleteUser" class="easyui-linkbutton" iconCls="icon-remove"  plain="true" onclick="deleteUser();" href="javascript:void(0);">删除用户</a>
				</sec:accesscontrollist>
				
				<sec:accesscontrollist   hasPermission="3" domainObject="sysRole_tabPower_button_btSearchUser" >
					<a id="btSearchUser" class="easyui-linkbutton" iconCls="icon-search"  plain="true" onclick="showSearchUser();" href="javascript:void(0);">查询用户</a>
				</sec:accesscontrollist>
				
				
			</div>
					
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
					singleSelect: true,
					checkOnSelect: true,
					selectOnCheck: false,
					
					pagination:true,
					pageSize:dataOptions.pageSize,
					pageList:dataOptions.pageList,
					
					striped:true
							
					">
				<thead>
					<tr>
						<th data-options="field:'id', halign:'center',checkbox:true" width="220">id</th>
						
						<th data-options="field:'no',required:true,halign:'center',align:'left'" editor="{type:'text'}" width="220">用户编号</th>
						<th data-options="field:'name',required:true,halign:'center',align:'left'" editor="{type:'text'}" width="220">用户名</th>
						<th data-options="field:'loginName',halign:'center',align:'left'" editor="{type:'text'}"  width="220">登录名</th>
						
						<th data-options="field:'deptCode',halign:'center',align:'left'" editor="{type:'text'}"  width="220">部门编号</th>
						<th data-options="field:'deptName',halign:'center',align:'left'" editor="{type:'text'}"  width="220">所属部门</th>
						
						
						<th data-options="field:'status',halign:'center',align:'left', formatter: formatcombobox" editor="{type:'combobox',options:{required:true,panelHeight:'auto',editable:false,valueField:'lookupCode',textField:'lookupName'}}"  width="220">状态</th>
						<th data-options="field:'remark',halign:'center',align:'left'" editor="{type:'text'}"  width="220">描述</th>
						
					</tr>
				</thead>
			</table>	
		</div>
		<input id="deptId" style="display:none"></input>
							<input id="deptName" style="display:none"></input>
		<div id="searchUserDialog" class="easyui-dialog" data-options="iconCls:'icon-search',resizable:true,modal:false,buttons:'#searchUserBtns'" 
			style="width: 500px;height:150px; visible: hidden" title="搜索用户">
			<form id="searchUserForm">
	    		<table>
	    			<tr style="height:5px;">
	    			</tr>
	    			<tr>
	    				<td>用户名:</td><td><input type='text' name="filter_LIKE_name" id="filter_LIKE_u_name"/></td>
	    				<td>登录名:</td><td><input type='text' name="filter_LIKE_loginName" id="filter_LIKE_u_loginName"/></td>
	    				
	    			</tr>
	    			<tr style="height:5px;">
	    			</tr>
	    			<tr>
	    				<td>状态:</td><td><input id="filter_EQ_u_status" class="easyui-combobox" name="filter_EQ_u_status"  
    						 data-options="panelHeight:'auto', editable:false,valueField:'lookupCode',textField:'lookupName', data: [{lookupCode:'1', lookupName: 'aaa'}]" /> </td>
	    				
	    			</tr>
	    		</table>
	    	</form>
	    	<div id="searchUserBtns">
	    		<a class="easyui-linkbutton" plain="false" onclick="searchUser();" href="javascript:void(0);">查询</a>
	    		<a class="easyui-linkbutton" plain="false" onclick="clearUser();" href="javascript:void(0);">清空</a>
	    		<a class="easyui-linkbutton" plain="false" onclick="hideSearchUser();" href="javascript:void(0);">返回</a>
	    	</div>
	    </div>
	   

</body>
</html>