<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>默认首页配置</title>
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
					</tr>
				</table>
	     </div>
		<table id="portalconfig" class="easyui-datagrid" 
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
				url:'platform/portlet/portletconfig/getData.json',
				onClickCell:clickCell
				">
			<thead>
				<th data-options="field:'id',halign:'center',checkbox:true" width="220">id</th>
				<th data-options="field:'name',halign:'center'" editor="{type:'text'}" width="220">名称</th>
				<th data-options="field:'userid',halign:'center',hidden:true" width="220"></th>
				<th data-options="field:'userName',align:'center',halign:'center',styler:styler" width="100">创建者用户名</th>
				<th data-options="field:'roleId',halign:'center',hidden:true" width="320"></th>
				<th data-options="field:'layoutExtends',halign:'center',styler:styler" width="220">继承角色</th>
				<th data-options="field:'layout',halign:'center'" editor="{type:'text'}" width="220">布局</th>
				<th data-options="field:'orderBy',halign:'center'" editor="{type:'numberbox'}" width="50">优先级</th>
				<th data-options="field:'content',halign:'center',formatter:formatContent,styler:styler" width="100">配置内容</th>
				<th data-options="field:'f',hidden:true" width="220"></th>
			</thead>
		</table>
	</div>
<script type="text/javascript">
var datagrid;
$(function(){
 });
function styler(){
	return 'cursor:pointer;';
};
function formatContent(value,row,index){
	return "<span style='vertical-align:middle;font-color:green;cursor:pointer;line-height:1.5em;border-bottom:1px solid #eeeeee;'>配置</span>";
};
//插入
function insert(){
	endEditing();
	$("#portalconfig").datagrid('insertRow',{
		index: 0,
		row:{id:"",
				userid:'${sessionScope.CURRENT_LOGINUSER.id}',
				userName:'${sessionScope.CURRENT_LOGINUSER.name}',
				layoutExtends:'请选择角色',
				f:true,
				layout:'layout1.xml'}
	}); 
};
//保存
var save=function(){
	if(!this.endEditing()){
		return false;
	}
	var allRows = $("#portalconfig").datagrid('getRows');
	$.ajax({
		 url:'platform/portlet/portletconfig/save.json',
		 data : {datas:JSON.stringify(allRows)},
		 type : 'post',
		 dataType : 'json',
		 success : function(r){
			 if (r.flag == "success"){
				 $("#portalconfig").datagrid('load',{});
				 $("#portalconfig").datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
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
var del=function(){
	var rows = $("#portalconfig").datagrid('getChecked');
	var ids = [];
	var l =rows.length;
  	if(l > 0){
	  $.messager.confirm('请确认','您确定要删除当前所选的数据？',function(b){
		 if(b){
			 for(;l--;){
				 ids.push(rows[l].id);
			 }
			 $.ajax({
				 url:'platform/portlet/portletconfig/delte.json',
				 data:	JSON.stringify(ids),
				 contentType : 'application/json',
				 type : 'post',
				 dataType : 'json',
				 success : function(r){
					 if (r.flag == "success") {
						 $("#portalconfig").datagrid('load',{});
						 $("#portalconfig").datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
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
var editIndex = undefined;
var clickCell = function(index, field,value){
	var comSelect;
	var nowRow=$('#portalconfig').datagrid('getRows')[index];
	if(field ==='layoutExtends' && endEditing()){//员工
		comSelect = new GridCommonSelector("role",'portalconfig',index,"roleId",{targetId:'roleId'},function(rowIndex,resultData){
			var roleId =[],roleName=[];
			var l =resultData.length;
			for(;l--;){
				roleId.push(resultData[l].id);
				roleName.push(resultData[l].roleName);
			}
			
			$('#portalconfig').datagrid('updateRow',{
					index: index,
					row: {
						roleId: roleId.join(','),
						layoutExtends:roleName.join(','),
						f:true
					}
				});
			},null,null,null,false);
		comSelect.init(nowRow);
	}else if(field ==='content' &&endEditing()){
		if(!nowRow.id){
			alert("请保存再配置！");
			return;
		}
		window.open('<%=ViewUtil.getRequestPath(request)%>/avicit/platform6/modules/system/sysdashboard/indexAdmin.jsp?sysPortletConfigId='+nowRow.id+'&j='+ Math.random());
	}else if(endEditing()){
		$('#portalconfig').datagrid('selectRow', index).datagrid('editCell', {index:index,field:field});
		editIndex = index;
	}
};
//扩展easyui单元格编辑
$.extend($.fn.datagrid.methods, {
    editCell: function(jq,param){
        return jq.each(function(){
            var opts = $(this).datagrid('options');
            var fields = $(this).datagrid('getColumnFields',true).concat($(this).datagrid('getColumnFields'));
            for(var i=0; i<fields.length; i++){
                var col = $(this).datagrid('getColumnOption', fields[i]);
                col.editor1 = col.editor;
                if (fields[i] != param.field){
                    col.editor = null;
                }
            }
            $(this).datagrid('beginEdit', param.index);
            for(var i=0; i<fields.length; i++){
                var col = $(this).datagrid('getColumnOption', fields[i]);
                col.editor = col.editor1;
            }
        });
    }
});
var endEditing=function(){
    if (editIndex == undefined){return true}
   	$('#portalconfig').datagrid('endEdit', editIndex).datagrid('unselectRow',editIndex);
    editIndex = undefined;
    return true;
};
</script>
</body>
</html>