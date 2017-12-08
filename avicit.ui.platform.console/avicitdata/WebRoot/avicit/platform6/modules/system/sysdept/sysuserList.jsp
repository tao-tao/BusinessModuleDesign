<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<%@taglib prefix="sec" uri="/WEB-INF/tags/shiro.tld"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户列表</title>
<base href="<%=ViewUtil.getRequestPath(request)%>">
<link href="avicit/platform6/modules/system/sysdept/css/icon.css" type="text/css" rel="stylesheet">
<jsp:include page="/avicit/platform6/component/common/EasyUIJsInclude.jsp"></jsp:include>
<jsp:include page="/avicit/platform6/modules/system/commonpopup/commonSelectionHead.jsp"></jsp:include>
<script src="static/js/platform/component/common/exportData.js" type="text/javascript"></script>
<script type="text/javascript">

var path="<%=ViewUtil.getRequestPath(request)%>";
var queryId="${param.id}";
var queryType="${param.type}";
var queryName="${param.name}";
/**
 * 供子页刷新用
 */
function flushData(){
	$("#dgUser").datagrid('reload');
	$("#dgUser").datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
}

/**
 * 加载用户数据
 */
function loadUserData(id,type,name){
	
	//if(id==queryId&&queryType==type) return;
	
	queryId=id;
	queryType=type;
	queryName=name;
	
	$("#selecDeptDialog").dialog('close');
	$("#queryUserDialog").dialog('close');
	
	$("#dgUser").datagrid("options").url ="platform/sysdept/sysDeptController/getUserDataByPage.json";
	$("#dgUser").datagrid('reload',{id:id,type:type});
	
	$("#dgUser").datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
}

/**
 * 格式化显示
 * @param value
 * @param row
 * @param index
 */
function formatLanguage(value,row,index){
	for (var i = 0; i < languageData.length; i++){
		if(value==languageData[i].languageCode){
		   return languageData[i].languageName;
		}
	}
	return value;
}



/**
 * 加载缓存
 */
function userLoadCache(){
	if(parent&&parent.loadCache){
		parent.loadCache();
	}else{
		loadAllCache();
	}
}

/**
 * 加载缓存
 */
function loadAllCache(){
	var win = $.messager.progress({
		title:'请等待',
		msg:'数据加载中...'
	});
	$.ajax({
        cache: true,
        type: "POST",
        url:'platform/sysdept/sysDeptController/reLoadCache.json',
        dataType:"json",
        error: function(request) {
        	$.messager.progress('close');
        	$.messager.alert('提示','操作失败，服务请求状态：'+request.status+' '+request.statusText+' 请检查服务是否可用！','warning');
        },
        success: function(data) {
        	$.messager.progress('close');
			if(data.result==0){
				$.messager.show({
					title : '提示',
					msg : '加载成功！',
					timeout:2000,  
			        showType:'slide'  
				});
			}else{
				$.messager.alert('提示',data.msg,'warning');
			}
        }
    });
}

/**
 * 重置密码
 */
function resetPassword(){
	var rows = $("#dgUser").datagrid('getChecked');
	var ids = [];
	if (rows.length > 0) {
		$.messager.confirm('请确认','您确定重置所选用户的密码？',function(b){
			if(b){
				for ( var i = 0, length = rows.length; i < length; i++) {
					ids.push(rows[i].ID);
				}
			   $.ajax({
				 url:'platform/sysdept/sysDeptController/resetPassword',
				 data : {ids : ids.join(',')},
				 type : 'post',
				 dataType : 'json',
				 success : function(data){
					 if(0==data.result){
						 $("#dgUser").datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
						 $.messager.show({title : '提示',msg : '操作成功'});
					 }else{
						 $.messager.alert('提示',data.msg,'warning');
					 }
				 }
			 });
			}
		});
	}else{
		$.messager.alert('提示',"请选择用户！",'warning');
	}
}

/**
 * 设置是否无效
 */
function setValidFlag(){
	var rows = $("#dgUser").datagrid('getChecked');
	var ids = [];
	if (rows.length > 0) {
		$.messager.confirm('请确认','您确定执行该操作？',function(b){
			if(b){
				for ( var i = 0, length = rows.length; i < length; i++) {
					ids.push(rows[i].ID);
				}
			   $.ajax({
				 url:'platform/sysdept/sysDeptController/setValidFlag',
				 data : {ids : ids.join(',')},
				 type : 'post',
				 dataType : 'json',
				 success : function(data){
					 if(0==data.result){
						 //loadUserData(queryId,queryType,queryName);
						 $("#dgUser").datagrid('reload');
						 $("#dgUser").datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
						 $.messager.show({title : '提示',msg : '操作成功'});
					 }else{
						 $.messager.alert('提示',data.msg,'warning');
					 }
				 }
			 });
			}
		});
	}else{
		$.messager.alert('提示',"请选择用户！",'warning');
	}
}

/**
 * 用户解锁
 */
function doUnLockSysUser(){
	var rows = $("#dgUser").datagrid('getChecked');
	var ids = [];
	if (rows.length > 0) {
		$.messager.confirm('请确认','您确定执行该操作？',function(b){
			if(b){
				for ( var i = 0, length = rows.length; i < length; i++) {
					ids.push(rows[i].ID);
				}
			   $.ajax({
				 url:'platform/sysdept/sysDeptController/doUnLockSysUser',
				 data : {ids : ids.join(',')},
				 type : 'post',
				 dataType : 'json',
				 success : function(data){
					 if(0==data.result){
						 $("#dgUser").datagrid('reload');
						 $("#dgUser").datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
						 $.messager.show({title : '提示',msg : '操作成功'});
					 }else{
						 $.messager.alert('提示',data.msg,'warning');
					 }
				 }
			 });
			}
		});
	}else{
		$.messager.alert('提示',"请选择用户！",'warning');
	}
}


/**
 * 移动用户部门
 */
function batchMoveDept(){
	var rows = $("#dgUser").datagrid('getChecked');
	if (rows.length > 0) {
		var usd = new CommonDialog("selecDeptDialog","350","400",path+"/avicit/platform6/modules/system/sysdept/selectDept.jsp","移动用户到部门",true,false,true,true,false,false);
		var buttons = [];
		buttons.push({
			text:'移动',
			id : 'saveButton',
			//iconCls : 'icon-add',
			handler:function(){
				var ifr = jQuery("#selecDeptDialog iframe")[0];
				var win = ifr.window || ifr.contentWindow;
				win.saveMoveDept(); // 调用iframe中的a函数
			}
		});
		usd.createButtonsInDialog(buttons);
		usd.show();
	}else{
		$.messager.alert('提示',"请选择用户！",'warning');
	}
}

/**
 * 保存批量移动部门
 */
function saveBatchMoveDept(deptId){
	if(""==deptId||null==deptId) $.messager.alert('提示',"获取部门为空！",'warning');
	var rows = $("#dgUser").datagrid('getChecked');
	var ids = [];
	var deptIds=[];
	if (rows.length > 0) {
		for ( var i = 0, length = rows.length; i < length; i++) {
			ids.push(rows[i].ID);
			deptIds.push(rows[i].DEPT_ID);
		}
		$.ajax({
	        cache: true,
	        type: "POST",
	        url:"platform/sysdept/sysDeptController/saveBatchMoveDept",
	        dataType:"json",
	        data:{userIds:ids.join(','),newDeptId:deptId,oldDeptIds:deptIds.join(',')},
	        error: function(request) {
	        	$.messager.alert('操作失败，服务请求状态：'+request.status+' '+request.statusText+' 请检查服务是否可用！');
	        },
	        success: function(data) {
				if(data.result==0){
					$("#dgUser").datagrid('reload',{});
					$("#dgUser").datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
					$("#selecDeptDialog").dialog('close');
					$.messager.show({
						title : '提示',
						msg : '操作成功！',
						timeout:2000,  
				        showType:'slide'  
					});
				}else{
					$.messager.alert('提示',data.msg,'warning');
				}
	        }
	    });
	}else{
		$.messager.alert('提示',"请选择用户！",'warning');
	}
}

/**
 * 查询
 */
function searchOpen(){
	var usd = new CommonDialog("queryUserDialog","700","300","platform/sysdept/sysDeptController/toQueryUserView","查询",false,false,false);
	usd.show();
}

var expSearchParams;

//去后台查询
function searchData(searchData){
	$("#dgUser").datagrid("options").url="platform/sysdept/sysDeptController/getUserDataByPage.json";
	searchData.id=queryId;
	searchData.type=queryType;
	expSearchParams=searchData;
	$('#dgUser').datagrid('load',searchData);
	$("#dgUser").datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
}

/**
 * 添加用户
 */
function insertUser(){
	var path="platform/sysuser/toAddJsp";
	if("dept"==queryType){
		path+="?deptId="+queryId+"&deptName="+queryName;
	}
	var usd = new CommonDialog("insertUserDialog","700","435",path,"添加用户",false,true,false);
	usd.show();
}

/**
 * 编辑用户
 */
function editUser(){
	var rows = $("#dgUser").datagrid('getChecked');
	if(rows.length<=0){
		$.messager.alert('提示',"请先择用户！",'warning');
		return;
	}
	if(rows.length>1){
		$.messager.alert('提示',"一次只能编辑一条记录！",'warning');
		return;
	}
	var path="platform/sysuser/toEditJsp?sysUserId="+rows[0].ID;
	var usd = new CommonDialog("editUserDialog","700","435",path,"编辑用户",false,true,false);
	usd.show();
}

/**
 * 关闭添加页
 */
$closeAddIfram = function(){
	$("#insertUserDialog").dialog('close');
};

/**
 * 关闭编辑页
 */
$closeEditIfram = function(){
	$("#editUserDialog").dialog('close');
};

/**
 * 导入数据从excel中
 */
function importUser(){
	var imp = new CommonDialog("importData","700","400",'platform/excelImportController/excelimport/importEmployeeInfo/xls',"Excel数据导入",false,true,false);
	imp.show();
}
function closeImportData(){
	$("#importData").dialog('close');
}

/**
 * 获取列的配置对象
 */
/* function getGridColumnFieldsOptions(table) {
    var allColsTitle = $("#" + table).datagrid("options").columns;
    return allColsTitle;
} */
/**
 * 导出用户(客户端数据)
 */
function exportClientData(){
	$.messager.confirm('确认','是否确认导出Excel文件?',function(r) {
        if (r) {
            //封装参数
            var columnFieldsOptions = getGridColumnFieldsOptions('dgUser');
            var dataGridFields = JSON.stringify(columnFieldsOptions[0]);
            var rows = $('#dgUser').datagrid('getRows');
            var datas = JSON.stringify(rows);
            var myParams = {
                dataGridFields: dataGridFields,//表头信息集合
                datas: datas,//数据集
                hasRowNum : true,//默认为Y:代表第一列为序号
                sheetName: 'sheet1',//如果该参数为空，默认为导出的Excel文件名
                unContainFields : 'STATUS_LABEL,EMAIL',//不需要导出的列，使用','分隔即可
                fileName: '平台用户导出数据'+ new Date().toString()//导出的Excel文件名
            };
            var url = "platform/sysuser/exportClient";
            var ep = new exportData("xlsExport","xlsExport",myParams,url);
            ep.excuteExport();
        }
       });
}
/**
 * 导出用户(服务端数据)
 */
function exportServerData(){
	$.messager.confirm('确认','是否确认导出Excel文件?',function(r) {
        if (r) {
            //封装参数
            var columnFieldsOptions = getGridColumnFieldsOptions('dgUser');
            var dataGridFields = JSON.stringify(columnFieldsOptions[0]);
            
            expSearchParams = expSearchParams?expSearchParams:{};
            
            expSearchParams.dataGridFields=dataGridFields;
            expSearchParams.hasRowNum=true;
            expSearchParams.sheetName='sheet1';
            expSearchParams.unContainFields='STATUS_LABEL,EMAIL';
            expSearchParams.fileName='平台用户导出数据'+ new Date().toString();
            
            expSearchParams.id =queryId;
            expSearchParams.type=queryType;
            
            //var url = "platform/commonExcelController/exportExcelClientData";
            var url = "platform/sysuser/exportServer";
            var ep = new exportData("xlsExport","xlsExport",expSearchParams,url);
            ep.excuteExport();
        }
       });
}

/**
 * 导出pdf
 */
/* function exportUserPdfData(){
	var expParams={};
	$.extend(expParams,exportHead,expSearchParams);
	var url="platform/sysdept/sysDeptController/exportPdfUserData";
	var ep=new exportData("pdfExport","pdfExport",expParams,url);
	ep.excuteExport();
} */
/**
 * 删除用户
 */
function deleteUser(){
	var rows = $('#dgUser').datagrid('getChecked');
	var ids = [];
	var deptIds = [];
	var l=rows.length;
	if (rows.length > 0) {
		$.messager.confirm('请确认','您确定要删除当前所选的数据？',
			function(b){
				if(b){
					for (;l--;) {
						ids.push(rows[l].ID);
						deptIds.push(rows[l].DEPT_ID);
					}
					$.ajax({
						url : 'platform/sysuser/deleteSysUser',
						data : {ids : ids.join(','), deptIds : deptIds.join(',')},
						type : 'post',
						dataType : 'json',
						success : function(result) {
							if (result.flag == "success") {
								if(result.toolTip){
									$.messager.alert('提示',result.toolTip+"已是系统资源，请先删除系统资源！",'warning');
								}else{
									$("#dgUser").datagrid('reload');
									$("#dgUser").datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
									$.messager.show({
										title : '提示',
										msg : '操作成功！',
										timeout:2000,  
								        showType:'slide'  
									});
								}
							}else{
								$.messager.alert('提示',result.msg,'warning');
							}
						}
					});
				}
			});
	} else {
		$.messager.alert('提示', '请选择要删除的记录！', 'warning');
	}
}
</script>
</head>
<body class="easyui-layout" fit="true">
<div data-options="region:'center',split:true,border:false" style="padding:0px;overflow:hidden;">
	<div id="toolbarUser" >
						<table >
							<tr>
							<sec:accesscontrollist hasPermission="3" domainObject="sysuser_toolbar_insertUser" permissionDes="添加">
								<td><a class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="insertUser();" href="javascript:void(0);">添加</a></td>
									</sec:accesscontrollist>
								
								<sec:accesscontrollist hasPermission="3" domainObject="sysuser_toolbar_editUser" permissionDes="编辑">
								<td><a class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editUser();" href="javascript:void(0);">编辑</a></td>
								</sec:accesscontrollist>
								
								<sec:accesscontrollist hasPermission="3" domainObject="sysuser_toolbar_resetPassword" permissionDes="恢复密码">
								<td><a class="easyui-linkbutton" iconCls="icon-tools" plain="true" onclick="resetPassword();" href="javascript:void(0);">恢复密码</a></td>
								</sec:accesscontrollist>
								
								<td><a class="easyui-linkbutton" iconCls="icon-setting" plain="true" onclick="setValidFlag();" href="javascript:void(0);">有(无)效设置</a></td>
								
								<sec:accesscontrollist hasPermission="3" domainObject="sysuser_toolbar_doUnLockSysUser" permissionDes="解锁">
								<td><a class="easyui-linkbutton" iconCls="icon-setting" plain="true" onclick="doUnLockSysUser();" href="javascript:void(0);">解锁</a></td>
								</sec:accesscontrollist>
								
								<td><a class="easyui-linkbutton" iconCls="icon-import" plain="true" onclick="importUser();" href="javascript:void(0);">导入用户</a></td>
								
								<td><a class="easyui-menubutton"  data-options="menu:'#export',iconCls:'icon-export'" href="javascript:void(0);">导出用户</a></td>
							<!-- 	<td><a class="easyui-linkbutton" iconCls="icon-reload" plain="true" onclick="userLoadCache();" href="javascript:void(0);">加载缓存 </a></td> -->
								<td><a id="btn-tools" href="#" class="easyui-menubutton" data-options="menu:'#tools',iconCls:'icon-tools'">工具</a></td>
								
								<sec:accesscontrollist hasPermission="3" domainObject="sysuser_toolbar_deleteUser" permissionDes="删除">
								<td><a class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="deleteUser();" href="javascript:void(0);">删除</a></td>
								</sec:accesscontrollist>
								
								<sec:accesscontrollist hasPermission="3" domainObject="sysuser_toolbar_searchOpen" permissionDes="查询">
								<td><a class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="searchOpen();" href="javascript:void(0);">查询</a></td>
								</sec:accesscontrollist>
								
							</tr>
						</table>
					</div>
					<div id="tools" style="width:150px;">
						<div onclick="batchMoveDept();">移动部门</div>
					</div>
					<div id="export" style="width:150px;">
						<div data-options="iconCls:'icon-excel'" onclick="exportClientData();">Excel导出(客户端)</div>
						<div data-options="iconCls:'icon-excel'" onclick="exportServerData();">Excel导出(服务器端)</div>
						<!-- <div data-options="iconCls:'icon-pdf'" onclick="exportUserPdfData();">PDF导出</div>  -->
					</div>
					<table id="dgUser" class="easyui-datagrid" datapermission='sysUser'
							data-options=" 
								fit: true,
								border: false,
								rownumbers: true,
								animate: true,
								collapsible: false,
								fitColumns: true,
								autoRowHeight: false,
								toolbar:'#toolbarUser',
								idField :'ID',
								singleSelect: true,
								checkOnSelect: true,
								selectOnCheck: false,
								pagination:true,
								pageSize:dataOptions.pageSize,
								pageList:dataOptions.pageList,
								striped:true,
								url:'platform/sysdept/sysDeptController/getUserDataByPage.json?id=${param.id}&type=${param.type}'
								">
							<thead>
								<tr>
									<th data-options="field:'ID', halign:'center',checkbox:true" width="220">id</th>
									<th data-options="field:'STATUS_LABEL',required:true,align:'center'" editor="{type:'text'}">状态</th>
									
									<th data-options="field:'NO',required:true,align:'center'" editor="{type:'text'}" width="220">用户编号</th>
									
									<th data-options="field:'NAME',required:true,align:'center'" editor="{type:'text'}" width="300">姓名</th>
									
									<th data-options="field:'LOGIN_NAME',align:'center',align:'center'" editor="{type:'text'}"  width="300">登录名</th>
									
									<th data-options="field:'SECRET_LEVEL_NAME',align:'center',align:'center'" editor="{type:'text'}"  width="220">密级</th>
									
									<th data-options="field:'SEX_NAME',align:'center',align:'center'" editor="{type:'text'}"  width="80">性别</th>
									<th data-options="field:'MOBILE',align:'center',align:'center'" editor="{type:'text'}"  width="220">手机</th>
									<th data-options="field:'OFFICE_TEL',align:'center',align:'center'" editor="{type:'text'}"  width="220">办公电话</th>
									<th data-options="field:'EMAIL',align:'center',align:'center'" editor="{type:'text'}"  width="220">邮件</th>
									<th data-options="field:'IS_MANAGER_NAME',align:'center',align:'center'" editor="{type:'text'}"  width="140">是否主管</th>
									<th data-options="field:'ORDER_BY',align:'center',align:'center'" editor="{type:'text'}"  width="80">排序</th>
									<th data-options="field:'DEPT_NAME',align:'center',align:'center'" editor="{type:'text'}"  width="220">部门</th>
									<th data-options="field:'POSITION_NAME',align:'center',align:'center'" editor="{type:'text'}"  width="220">岗位</th>
								</tr>
							</thead>
					</table>
</div>
</body>
</html>