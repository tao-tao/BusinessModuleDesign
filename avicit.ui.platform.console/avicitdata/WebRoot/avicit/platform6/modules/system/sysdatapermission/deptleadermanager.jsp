<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>跨部门领导选择</title>
<base href="<%=ViewUtil.getRequestPath(request)%>">
<jsp:include page="/avicit/platform6/component/common/EasyUIJsInclude.jsp"></jsp:include>
<jsp:include page="/avicit/platform6/modules/system/commonpopup/commonSelectionHead.jsp"></jsp:include>
</head>
<body class="easyui-layout">
	<div data-options="region:'center',split:true,border:false" style="padding:0px;overflow:hidden;height:0; overflow:hidden;">
		<div id="toolbar" class="datagrid-toolbar" style="display: block;">
				<table>  
					<tr>
						<td><a class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="insert();" href="javascript:void(0);">添加</a></td>
						<td><a class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="save();" href="javascript:void(0);">保存</a></td>
						<td><a class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="del();" href="javascript:void(0);">删除</a></td>
						<td><input id="deptLoaderQueryText"></input></td>
					</tr>
				</table>
	     </div>
		<table id="deptleader" class="easyui-datagrid" 
			data-options=" 
				fit: true,
				border: false,
				rownumbers: true,
				idField :'id',
				animate: true,
				collapsible: false,
				toolbar:'#toolbar',
				fitColumns: true,
				autoRowHeight: false,
				singleSelect: true,
				checkOnSelect: true,
				selectOnCheck: false,
				striped:true,
				url:'platform/sysPermissionLeaddeptController/getData',
				onClickCell:clickCell
				">
			<thead>
				<th data-options="field:'id',halign:'center',checkbox:true" width="220">id</th>
				<th data-options="field:'userId',halign:'center',hidden:true" width="220">userid</th>
				<th data-options="field:'userName',align:'center',halign:'center',styler:styler" width="100">员工姓名</th>
				<th data-options="field:'deptId',halign:'center',hidden:true" width="220">所管部门</th>
				<th data-options="field:'deptName',halign:'center',styler:styler" width="220">所管部门</th>
				<!-- <th data-options="field:'f',hidden:true" width="220"></th> -->
			</thead>
		</table>
	</div>
<script type="text/javascript">
var datagrid;
$(function(){
	$('#deptLoaderQueryText').searchbox({
        width: 200,
        searcher: function (value) {
            if (value == "请输入查询内容" || value=="," || value =="，" || value =="/" || value =="\\" || value =="." || value =="。") {
                value = "";
            }
            searchPositionFun(value);
        },
        prompt: "请输入查询内容"
    });
 });
//查询
function searchPositionFun(queryKeyWord){
	if(queryKeyWord==null){
		$.messager.alert("操作提示", "请输入查询条件！","info");
		return ;
	}
	var queryParams = $('#deptleader').datagrid('options').queryParams;  
    queryParams.KeyWord =queryKeyWord;
    $('#deptleader').datagrid('options').queryParams=queryParams;        
    $("#deptleader").datagrid('load'); 
}
function closeForm(){
	if(parent && parent.closeDeptLeader){
		parent.closeDeptLeader();
	}
}
function styler(){
	return 'cursor:pointer;';
}
//插入
function insert(){
	$("#deptleader").datagrid('insertRow',{
		index: 0,
		row:{id:"",userName:'请选择用户',deptName:'请选择部门'}
	}); 
}
//保存
save=function(){
	var accessRows = $("#deptleader").datagrid('getRows');
	var row = []; 
	var l = accessRows.length;
	for(;l--;){
		if(accessRows[l].f) row.push(accessRows[l]);
	}
	if(row.length === 0){
		$.messager.show({
			 title : '提示',
			 msg : '没有要保存的数据'
		 });
	}
	$.ajax({
		 url:'platform/sysPermissionLeaddeptController/leaddept/save',
		 data : {datas:JSON.stringify(row)},
		 type : 'post',
		 dataType : 'json',
		 success : function(r){
			 if (r.flag == "success"){
				 $("#deptleader").datagrid('load',{});
				 $("#deptleader").datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
				$.messager.show({
					 title : '提示',
					 msg : '保存成功！'
				 });
			 }else{
				 $.messager.show({
					 title : '提示',
					 msg : r.error
				});
			 } 
		 }
	 });
};
//删除
del=function(){
	var rows = $("#deptleader").datagrid('getChecked');
	var ids = [];
	var l =rows.length;
  	if(l > 0){
	  $.messager.confirm('请确认','您确定要删除当前所选的数据？',function(b){
		 if(b){
			 for(;l--;){
				 ids.push(rows[l].id);
			 }
			 $.ajax({
				 url:'platform/sysPermissionLeaddeptController/leaddept/delete',
				 data:	JSON.stringify(ids),
				 contentType : 'application/json',
				 type : 'post',
				 dataType : 'json',
				 success : function(r){
					 if (r.flag == "success") {
						 $("#deptleader").datagrid('load',{});
						 $("#deptleader").datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
						 $.messager.show({
							 title : '提示',
							 msg : '删除成功！'
						});
					}else{
						$.messager.show({
							 title : '提示',
							 msg : r.error
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
function loadSuccess(data){
	if(parent && parent.callDeptLeader){
		var rows =data.rows;
		var l =rows.length;
		var names = parent.callDeptLeader();
		var userName;
		var index;
		for(;l--;){
			userName=rows[l].userName;
			index =names.indexOf(userName);
			if(index !== -1){
				$('#deptleader').datagrid('checkRow',l);
			}
		}
	}
};
var clickCell = function(index, field,value){
	var comSelect;
	var nowRow=$('#deptleader').datagrid('getRows')[index];
	if(field ==='userName'){//员工
		comSelect = new GridCommonSelector("user",'deptleader',index,"userId",{targetId:'userId'},function(rowIndex,resultData){
			$('#deptleader').datagrid('updateRow',{
					index: index,
					row: {
						userId: resultData.userId,
						userName:resultData.userName,
						f:true
					}
				});
			},null,null,null,1,null,null,"n");
		comSelect.init(nowRow);
	}else if(field ==='deptName'){//部门
		comSelect = new GridCommonSelector("dept",'deptleader',index,"deptId",{targetId:'deptId'},function(rowIndex,resultData){
			$('#deptleader').datagrid('updateRow',{
					index: index,
					row: {
						deptId: resultData.deptId,
						deptName: resultData.deptName,
						f:true
					}
				});
			},null,null,null,-1,null,null,"n");//
		comSelect.init(nowRow);
	}
};
</script>
</body>
</html>