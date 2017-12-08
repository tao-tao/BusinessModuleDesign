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
</head>
<body class="easyui-layout">
	<div data-options="region:'center',split:true,border:false" style="padding:0px;overflow:hidden;height:0; overflow:hidden;">
		<div id="toolbar" class="datagrid-toolbar" style="display: block;">
				<table class="tableForm" id="searchForm" width='100%'>
					<tr>
						<td width="100px;"><input id="deptLoaderQueryText"></input>  </td>
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
				fitColumns: true,
				autoRowHeight: false,
				singleSelect: true,
				checkOnSelect: true,
				selectOnCheck: false,
				striped:true,
				url:'platform/sysPermissionLeaddeptController/getData',
				onLoadSuccess:loadSuccess
				">
			<thead>
				<th data-options="field:'id',halign:'center',checkbox:true" width="220">id</th>
				<th data-options="field:'userId',halign:'center',hidden:true" width="220">userid</th>
				<th data-options="field:'userName',align:'center',halign:'center'" width="100">员工姓名</th>
				<th data-options="field:'deptName',halign:'center'" width="220">所管部门</th>
			</thead>
		</table>
	</div>
	<div id="toolbar" class="datagrid-toolbar datagrid-toolbar-extend">
		<table class="tableForm" border="0" cellspacing="1" width='100%'>
			<tr>	
				<td width="80%" align="right"><a title="确定" id="okButton"  class="easyui-linkbutton" iconCls="icon-save" plain="false" onclick="okForm();" href="javascript:void(0);">确定</a></td>
				<td align="left"><a title="返回" id="returnButton"  class="easyui-linkbutton" iconCls="icon-undo" plain="false" onclick="closeForm();" href="javascript:void(0);">返回</a></td>
			</tr>
		</table>
	</div>
<script type="text/javascript">
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
function okForm(){
	if(parent && parent.callBackUser){
		var rows = $('#deptleader').datagrid('getChecked');
		var l=rows.length;
		var userId =[];
		var userName=[];
		var row;
		for(;l--;){
			row =rows[l];
			userId.push(row.userId);
			userName.push(row.userName)
		}
		parent.callBackUser(userId.join(","),userName.join(","));
	}
}
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
}
</script>
</body>
</html>