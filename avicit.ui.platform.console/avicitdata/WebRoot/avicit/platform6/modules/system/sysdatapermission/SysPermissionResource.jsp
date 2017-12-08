<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>数据权限过滤配置</title>
<base href="<%=ViewUtil.getRequestPath(request)%>">
<jsp:include page="/avicit/platform6/component/common/EasyUIJsInclude.jsp"></jsp:include>
<jsp:include page="/avicit/platform6/modules/system/commonpopup/commonSelectionHead.jsp"></jsp:include>
<style type="text/css">
 .error {
 	color: red;
 	font-weight:700;
 }
</style>
</head>
<body class="easyui-layout">
	<!-- 左侧菜单信息 -->
	<div data-options="region:'west',split:true,title:'菜单信息'" style="width:250px;padding:0px;">
	 <div id="toolbar" class="datagrid-toolbar">
	 	<table width="100%">
	 		<tr>
	 			<td width="100%"><input type="text"  name="searchWord" id="searchWord"></input></td>
	 		</tr>
	 	</table>
	 </div>
	<ul id="memuTree" class="easyui-tree"
			data-options="
			url:'platform/sysPermissionTreeController/listMemuTreeById.json',
			loadFilter: function(data){
	            if (data.data){	
	                return data.data;
	            } else {
	                return data;
	            }
       		},
       		lines:true,
       		dataType:'json',
       		animate:true,
       		onBeforeExpand:myOnBeforeExpand,
       		onClick:memuTreeOnClickRow">数据加载中...
		</ul>
</div>
	<div data-options="region:'center',split:true,title:'权限资源'"
		style="padding: 0px;height:0; overflow:hidden;">
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'north',split:true,border:false"style="height: 550px">
				<div style="paddiong:0px;">
					<table width=100%>
						<tr>
							<td width="80"><div id="sqlLabel">资源过滤前置SQL</div></td>
							<td>
								<div style="position: relative;padding-right:5px;height: 78px">
									<textarea style="position: relative; overflow-y: hidden; width:100%; height:90%;" id="sql" readonly="readonly"></textarea>
								</div>
							</td>
						</tr>
					</table>
					<div id="toolbarImportResult">
						<table>
							<tr>
								<td><a class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="initResourceList();" href="javascript:void(0);">刷新资源</a></td>
								<td><a class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="saveResource();" href="javascript:void(0);">保存</a></td>
								<td><a class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="deleteResource();" href="javascript:void(0);">删除</a></td>
								<td><a class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="openSearchForm();" href="javascript:void(0);">查询</a></td>
								<td id="secret"><input id="secretCheck" type="checkbox"/></td>
								<td id="secretLable" valign="middle"><span>是否密级控制</span></td>
							</tr>
						</table>
					</div>
					<table id="dataprem"
						data-options="
							border: false,
							idField :'id',
							singleSelect: true,
							checkOnSelect: true,
							selectOnCheck: false,
							rownumbers: true,
							animate: true,
							collapsible: false,
							fitColumns: true,
							autoRowHeight: false,
							toolbar:'#toolbarImportResult',
							striped:true,
							onSelect:selectMainRow,
							url:''">
						<thead>
							<tr>
								<th data-options="field:'id', halign:'center',checkbox:true" width="220">id</th>
								<th data-options="field:'menuname', required:true,align:'center'" width="120">菜单名称</th>
								<th data-options="field:'datagrid',required:true,align:'center'" width="120">权限资源标识</th>
								<th data-options="field:'dataset',align:'center',align:'center'"  width="220">请求路径</th>
								<th data-options="field:'metadata',align:'center',align:'center'"  width="120">DataGrid标识</th>
								<th data-options="field:'status',align:'center',align:'center',formatter:formatStatus" width="120">权限状态</th>
								<th data-options="field:'sql',align:'center',align:'center',hidden:true" width="120">前置sql</th>
							</tr>
						</thead>
					</table>
	  			</div>
			</div>
			<div data-options="region:'south',split:true,border:false"style="height: 180px">
				<div style="padding:0px;height:50px;">
					<table id="datarole" class="easyui-datagrid"
							data-options=" 
								border: false,
								rownumbers: true,
								animate: true,
								collapsible: false,
								onClickCell:onClickCell,
								fitColumns: true,
								autoRowHeight: false,
								striped:true,
								singleSelect: true,
								checkOnSelect: false,
								selectOnCheck: false,
								url:''">
							<thead>
								<tr>
									<th data-options="field:'type',align:'center',hidden:true""  width="150">权限粒度</th>
									<th data-options="field:'typeName',required:true,align:'center'"  width="150">权限粒度</th>
									<th data-options="field:'accessRoleName',required:true,align:'center',styler:styleCell" width="120">角色</th>
									<th data-options="field:'accessDeptName',align:'center',styler:styleCell"  width="140">部门</th>
									<th data-options="field:'accessUserName',align:'center',styler:styleCell"  width="140">用户</th>
									<th data-options="field:'accessGroupName',align:'center',styler:styleCell"  width="140">群组</th>
									<th data-options="field:'accessPositionName',align:'center',styler:styleCell" width="180">岗位</th>
									<th data-options="field:'accessDept', halign:'center',hidden:true"  width="220">deptId</th>
									<th data-options="field:'accessRole', halign:'center',hidden:true"  width="220">roleId</th>
									<th data-options="field:'accessUser', halign:'center',hidden:true"  width="220">userId</th>
									<th data-options="field:'accessGroup', halign:'center',hidden:true"  width="220">groupId</th>
									<th data-options="field:'accessPosition', halign:'center',hidden:true"  width="220">positionId</th>
									<th data-options="field:'presql', halign:'center',hidden:true"  width="220">sql</th>
								</tr>
							</thead>
					</table>
			</div>  
			</div>
		</div>
	</div>
	<div id="searchDialog" data-options="iconCls:'icon-search',resizable:true,modal:false,buttons:'#searchBtns'" style="width: 600px;height:200px;display: none;">
		<form id="form1" method="post">
    		<table>
    			<tr>
    				<td>权限资源标识:</td><td><input class="easyui-validatebox" type="text" name="filter-LIKE-datagrid"></input></td>
    				<td>请求路径 :</td><td><input class="easyui-validatebox" type="text" name="filter-LIKE-dataset"></td>
    			</tr>
    			<tr>
    				<td>DataGrid标识:</td><td colspan="2"><input class="easyui-validatebox" type="text" name="filter-LIKE-metadata"></td>
    			</tr>
    		</table>
    	</form>
    	<div id="searchBtns">
    		<a class="easyui-linkbutton" plain="false" onclick="searchData();" href="javascript:void(0);">查询</a>
    		<a class="easyui-linkbutton" plain="false" onclick="clearData();" href="javascript:void(0);">清空</a>
    		<a class="easyui-linkbutton" plain="false" onclick="hideSearch();" href="javascript:void(0);">返回</a>
    	</div>
  </div>
<script type="text/javascript">
	var currTreeNode;
/**
 * 主表grid
 */
var myDatagrid;
var searchDialog;
$(document).ready(function() {
	initMemuSearch();
	myDatagrid = $('#dataprem').datagrid();
	searchDialog =$('#searchDialog').css('display','block').dialog({
		title:'查询'
	});
	searchForm = $('#form1').form();
	searchForm.find('input').on('keyup',function(e){
		if(e.keyCode == 13){
			searchData();
		}
	});
	searchDialog.dialog('close',true);
});

//打开查询框
function openSearchForm(){
	searchDialog.dialog('open',true);
};
//去后台查询
function searchData(){
	reloadResource(null,JSON.stringify(serializeObject(searchForm)));
};
//隐藏查询框
function hideSearch(){
	searchDialog.dialog('close',true);
};
/*清空查询条件*/
function clearData(){
	searchForm.find('input').val('');
};

function myOnBeforeExpand(row) {
	$("#memuTree").tree("options").url = "platform/sysPermissionTreeController/listMemuTreeById.json?id=" + row.id;
	return true;
};

/**
 * 菜单树单击事件
 */
function memuTreeOnClickRow(row) {
	$('#sql').attr("readonly",true).val('');
	resetResource();
	reloadResource(row);
};

/**
 *菜单查询
 **/
function initMemuSearch() {
	$('#searchWord').searchbox({
		width : 200,
		searcher : function(value) {
			var path = "platform/sysPermissionTreeController/searchMemu.json";
			if (value == null || value == "") {
				path = "platform/sysPermissionTreeController/listMemuTreeById.json";
			}
			$.ajax({
				cache : true,
				type : "POST",
				url : path,
				dataType : "json",
				data : {
					search_text : value
				},
				async : false,
				error : function(request) {
					alert('操作失败，服务请求状态：' + request.status
							+ ' ' + request.statusText
							+ ' 请检查服务是否可用！');
				},
				success : function(data) {
					if (data.result == 0) {
						$('#memuTree').tree('loadData',
								data.data);
					} else {
						$.messager.alert('提示', data.msg,
								'warning');
					}
				}
			});
		},
		prompt : "请输入菜单名称！"
	});
};
var tempIndex="";
var tempfieldName="";
var tempfieldId="";
var tempvalue = "";
var tempvalueid = "";
var deptLeader;
function onClickCell(rowIndex, field, value){
	tempIndex  = rowIndex;
	tempfieldName = field;
	tempfieldId = "deptId";
	tempvalue = value==null?"":value+",";
	var type  = $('#datarole').datagrid('getData').rows[tempIndex].type==null?"":$('#datarole').datagrid('getData').rows[tempIndex].type;
	if(type=="2"){
		if(field=="accessUserName"){
			deptLeader =value;
			openDeptLeaderDialog();
		}
		return;
	}
	if(field=="accessDeptName"){
		showDept(rowIndex, field, value);
		return;
	}
	if(field=="accessRoleName"){
		showRole(rowIndex, field, value);
		return;
	}
	if(field=="accessUserName"){
		showUser(rowIndex, field, value);
		return;
	}
	if(field=="accessGroupName"){
		showgroup(rowIndex, field, value);
		return;
	}
	if(field=="accessPositionName"){
		showPosition(rowIndex, field, value);
		return;
	}
	/* if(field=="presqlName"){
		showDept(rowIndex, field, value);
	} */
};

function showDept(rowIndex, field, value){
	
	var userSelect = new GridCommonSelector("dept","datarole",rowIndex,"dept",{targetId:'accessDept'},"selectDialogCallBack",null,null,null,false,null,null,"n");//
	 var rows = $('#datarole').datagrid("getRows");
	 userSelect.init(rows[rowIndex]);
	/* var deptSelector = new CommonSelector("dept","deptSelectCommonDialog","ACCESS_DEPT",null,null,null,null,false,null,null,600,400);
	deptSelector.init(false,"selectDialogCallBack",'1'); */
};
function showUser(rowIndex, field, value){
	var userSelect = new GridCommonSelector("user","datarole",rowIndex,"dept",{targetId:'accessUser'},"selectDialogCallBack",null,null,null,false,null,null,"n");//
	 var rows = $('#datarole').datagrid("getRows");
	 userSelect.init(rows[rowIndex]);
};
function showRole(rowIndex, field, value){
	var userSelect = new GridCommonSelector("role","datarole",rowIndex,"dept",{targetId:'accessRole'},"selectDialogCallBack",null,null,null,false);//
	 var rows = $('#datarole').datagrid("getRows");
	 userSelect.init(rows[rowIndex]);
};
function showgroup(rowIndex, field, value){
	var userSelect = new GridCommonSelector("group","datarole",rowIndex,"dept",{targetId:'accessGroup'},"selectDialogCallBack",null,null,null,false);//
	 var rows = $('#datarole').datagrid("getRows");
	 userSelect.init(rows[rowIndex]);
};
function showPosition(rowIndex, field, value){
	var userSelect = new GridCommonSelector("position","datarole",rowIndex,"dept",{targetId:'accessPosition'},"selectDialogCallBack",null,null,null,false);//
	 var rows = $('#datarole').datagrid("getRows");
	 userSelect.init(rows[rowIndex]);
};
function showSql(rowIndex, field, value){
	var userSelect = new GridCommonSelector("dept","datarole",rowIndex,"dept",{targetId:'accessDept'},"selectDialogCallBack",null,null,null,false);//
	 var rows = $('#datarole').datagrid("getRows");
	 userSelect.init(rows[rowIndex]);
};
//部门选择回调
function selectDialogCallBack(rowIndex,data,dialogDivId){
	var callId = [];
	var callName = [];
	
	if(tempfieldName=="accessDeptName"){
		$('#datarole').datagrid('updateRow',{
			index: tempIndex,
			row: {
				accessDeptName: data.deptName,
				accessDept:data.deptId
			}
		});
	}
	if(tempfieldName=="accessRoleName"){
		//var temproleid="";
		//var temprolename="";
		//console.info(data);
		 for(var i=0 ; i<data.length;i++){
			 callId.push(data[i].id);
			 callName.push(data[i].roleName);
			/* if(!(tempvalue.indexOf(data[i].roleName)!=-1)){
				if(i==0){
					tempvalue =  tempvalue+data[i].roleName;
					temproleid = temproleid+data[i].id;
				}else{
					tempvalue =  tempvalue+","+data[i].roleName;
					temproleid = temproleid+","+data[i].id;
				}
			} */
		}
		$('#datarole').datagrid('updateRow',{
			index: tempIndex,
			row: {
				accessRoleName: callName.join(','),
				accessRole:callId.join(',')
			}
		});
	}
	if(tempfieldName=="accessUserName"){
		$('#datarole').datagrid('updateRow',{
			index: tempIndex,
			row: {
				accessUserName: data.userName,
				accessUser:data.userId
				
			}
		});
	}
	if(tempfieldName=="accessGroupName"){
		$('#datarole').datagrid('updateRow',{
			index: tempIndex,
			row: {
				accessGroupName: data.groupName,
				accessGroup:data.groupId
			}
		});
	}
	if(tempfieldName=="accessPositionName"){
		$('#datarole').datagrid('updateRow',{
			index: tempIndex,
			row: {
				accessPositionName: data.positionName,
				accessPosition:data.positionId
			}
		});
	}
	/* if(tempfieldName=="PRESQL_NAME"){
		$('#datarole').datagrid('updateRow',{
			index: tempIndex,
			row: {
				PRESQL_NAME: tempvalue,
				PRESQL:tempvalueId
				
			}
		});
	} */

};
var isOk='';
//验证前置sql的正确性
function checkSQL(sql){
	var result=true;
	$.ajax({
		 async: false, 
		 url:'platform/sysPermissionResourceController/check',
		 data : {sql:sql},
		 type : 'post',
		 dataType : 'json',
		 success : function(r){
			 if(r.flag==='success'){
				 result = true;
				 $('#sql').tooltip('destroy');
				 if(isOk){
					 clearInterval(isOk);
					 $('#sqlLabel').removeClass('error');
				 }
			 }else{
				 result = false;
				 isOk=setInterval('showError(true)', 1000);
				 $('#sql').tooltip({
						trackMouse:true,
						position: 'right',
						content: '<span style="color:red">'+r.msg+'</span>',    
						onShow: function(){        
							$(this).tooltip('tip').css({            
								color: '#000',
								borderColor: '#CC9933',
								backgroundColor: '#FFFFCC'      
							});    
					}});
			 }
		 }
	 });
	return result;
};
var errorFlag;
//交替变换错误信息
function showError(b){
	if(errorFlag){
		$('#sqlLabel').addClass('error');
		errorFlag= false;
	}else{
		$('#sqlLabel').removeClass('error');
		errorFlag=true;
	}
};
function saveResource(){
	var totalSql = $('#sql').val();
	if(!checkSQL(totalSql)){
		return false;
	}
	var selectRow = myDatagrid.datagrid('getSelected');
	var data =JSON.stringify(selectRow);
	var accessRows = $('#datarole').datagrid('getRows');
	var accessData =JSON.stringify(accessRows);
	
	/**
	组装参数 获得sql参数和是否启用密级等级
	**/
	var sqlvalue = document.getElementById("sql").value;
	//alert($('#secretCheck').attr('checked'));
	 $.ajax({
		 url:'platform/sysPermissionResourceController/saveResource',
		 data : {data : data,
			 accessdata:accessData,
			 presql:sqlvalue,
			 secret:$('#secretCheck').attr('checked')},
		 type : 'post',
		 dataType : 'json',
		 success : function(r){
			 if(r.result==0){
				 //myDatagrid.datagrid('reload',{});
				 $.messager.show({
					 title : '提示',
					 msg : '保存成功'
				 });
			 }else{
				 $.messager.alert('提示',r.msg,'warning');
			 }
			 
		 }
	 });
	
};
/*删除选中数据*/
function deleteResource(){
  var rows = myDatagrid.datagrid('getChecked');
  var ids = [];
  if(rows.length > 0){
	  $.messager.confirm('请确认','您确定要删除当前所选的数据？',function(b){
		 if(b){
			 for(var i =0,length = rows.length; i<length;i++){
				 ids.push(rows[i].id);
			 }
			 $.ajax({
				 url:'platform/sysPermissionResourceController/deleteResource',
				 data:{
					 ids : ids.join(',')
				 },
				 type : 'post',
				 dataType : 'json',
				 success : function(r){
					 if(r.flag==='success'){
						 reloadAccess({id:''});
						 reloadResource();
						 resetResource();
						 $.messager.show({
							 title : '提示',
							 msg : '删除成功！'
						 });
					 }else{
						 $.messager.show({
							 title : '提示',
							 msg : '删除失败！'
						 });
					 }
				 }
			 });
		 } 
	  });
  }else{
	  $.messager.alert('提示','请选择要删除的记录！','warning');
  }
};
/**
 * 点击行出发事件
 */
function onClickRow(rowNum, record) {
	var rows = $("#dataprem").datagrid('getChecked');
	$.ajax({
		cache : true,
		type : "POST",
		url : 'platform/sysPermissionTreeController/listAccess.json?id'+ rows[0].id,
		dataType : "json",
		data : {
			id : rows[0].id
		},
		error : function(request) {
			$.messager.alert('提示', '操作失败，服务请求状态：' + request.status + ' '+ request.statusText + ' 请检查服务是否可用！', 'warning');
		},
		success : function(data) {
			if (data.result == 0 && data.data) {
				$('#datarole').datagrid('loadData', data.data);
			} else {
				$.messager.alert('提示', data.msg, 'warning');
			}
		}
	});
};
//刷新权限资源
function initResourceList(){
	var node =$("#memuTree").tree("getSelected");
	if(node){
		if(node.id ==='root'){
			 alert("请不要选择菜单跟节点！");
			 return false;
		 }
		 $.messager.confirm('请确认','您确定要刷新当前权限资源？',function(b){
			 if(b){
				 $.ajax({
					 url:'platform/sysPermissionTreeController/refreshResource',
					 data:{
						 id:node.id
					 },
					 type:'post',
					 dataType:'json',
					 success:function(r){
						 if(r.flag ==="success"){
							 reloadResource(node);
							 $.messager.show({
								 title : '提示',
								 msg : '刷新成功！'
							 });
						 }else{
							 $.messager.alert('提示', '操作失败，'+r.msg, 'warning');
						 }
						 
					 }
				 });
			 }
			
		 });
	}else{
		alert("请选择一菜单！");
	}
};
//重置主表数据状态（选中，check，unchek等状态）
function resetResource(){
	myDatagrid.datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
};
//刷新主表grid 
function reloadResource(nodeinf,param){
	var node = nodeinf || $("#memuTree").tree("getSelected");
	if(!node){
		return false;
	}
	var vparam =param||{};
	$.ajax({
		cache : true,
		type : "POST",
		url : 'platform/sysPermissionTreeController/listResource.json',
		dataType : "json",
		data : {
			id : node.id,
			param :vparam
		},
		error : function(request) {
			$.messager.alert('提示', '操作失败，服务请求状态：' + request.status + ' '+ request.statusText + ' 请检查服务是否可用！', 'warning');
		},
		success : function(data) {
			var grid = $('#dataprem');
			if (data.result == 0 && data.data) {
				grid.datagrid('loadData', data.data);
				if(grid.datagrid('getRows').length>0){
					grid.datagrid('selectRow',0);
				}else{
					reloadAccess({id:''});
				}
			} else {
				$.messager.alert('提示', data.msg, 'warning');
			} 
		}
	});
};
//刷新子表grid
function reloadAccess(rowData){
	var row = rowData||$("#dataprem").datagrid('getSelected');
	$.ajax({
		cache : true,
		type : "POST",
		url : 'platform/sysPermissionTreeController/listAccess.json?id'+ row.id,
		dataType : "json",
		data : {
			id : row.id
		},
		error : function(request) {
			$.messager.alert('提示', '操作失败，服务请求状态：' + request.status + ' '+ request.statusText + ' 请检查服务是否可用！', 'warning');
		},
		success : function(data) {
			if (data.result == 0 && data.data) {
				$('#datarole').datagrid('loadData', data.data);
			} else {
				$.messager.alert('提示', data.msg, 'warning');
			}
		}
	});
};
//主表选中事件
function selectMainRow(rowIndex, rowData){
	$('#sql').attr("readonly",false).val(rowData.sql);
	if(rowData.haveSecret==0){
		$('#secret').css('visibility',"hidden");
		$('#secretLable').css('visibility',"hidden");
	}else{
		$('#secret').css('visibility',"visible");
		$('#secretLable').css('visibility',"visible");
		if(rowData.isSecret==1){
			$('#secretCheck').attr('checked',"checked");
		}
	}
	reloadAccess(rowData);
};
function formatStatus(value,row,index){
	return value==='0'?'有效':'无效';
	
};

function styleCell(value,row,index){
	if(row.type ==2 && this.field !=="accessUserName"){
		return "cursor:not-allowed;";
	}else{
		return "cursor:pointer;";
	}
};

function openDeptLeaderDialog(){
	var usd = new CommonDialog("deptLeaderDialog","600","300","avicit/platform6/modules/system/sysdatapermission/deptleader.jsp","选择部门领导",false,false,false);
	usd.show();
};
function closeDeptLeader(){
	$("#deptLeaderDialog").dialog('close');
};
//回填数据
function callBackUser(userId,userName){
	$('#datarole').datagrid('updateRow',{
		index: tempIndex,
		row: {
			accessUser: userId,
			accessUserName:userName
		}
	});
	closeDeptLeader();
};
function callDeptLeader(){
	return deptLeader;
};

</script>	
</body>
</html>