<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<%@taglib prefix="sec" uri="/WEB-INF/tags/shiro.tld"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>语言列表</title>
<base href="<%=ViewUtil.getRequestPath(request)%>">
<jsp:include page="/avicit/platform6/component/common/EasyUIJsInclude.jsp"></jsp:include>
<jsp:include page="/avicit/platform6/modules/system/commonpopup/commonSelectionHead.jsp"></jsp:include>
<link href="avicit/platform6/modules/system/sysdept/css/icon.css" type="text/css" rel="stylesheet">
<script type="text/javascript">
var editIndex = undefined;
var languageData=${languageStr};

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


$.extend($.fn.datagrid.methods, {
    editCell: function(jq,param){
        return jq.each(function(){
            var opts = $(this).datagrid('options');
            var fields = $(this).datagrid('getColumnFields',true).concat($(this).datagrid('getColumnFields'));
            for(var i=0; i<fields.length; i++){
         	   //console
                var col = $(this).datagrid('getColumnOption', fields[i]);
                col.editor1 = col.editor;
                if (fields[i] != param.field){
             	   var fieldOpts= $(this).datagrid('getColumnOption',fields[i]);
             	   if(!fieldOpts.hidden || fieldOpts.hidden == false){
             		   col.editor = null;
             	   }   
             	   //}
                   
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



function endEditing(){
    if (editIndex == undefined){return true;}
   	$("#dgDeptTl").datagrid('endEdit', editIndex);
    editIndex = undefined;
    return true;
}

function onClickCell(index, field){

    if (endEditing()){
    	$("#dgDeptTl").datagrid('selectRow', index).datagrid('editCell', {index:index,field:field});
        editIndex = index;
    }
}

function insertDeptTl(){
	
	endEditing();
	
	var rows = $("#dgDeptTl").datagrid('getChanges');
	if(rows.length>0){
		$.messager.alert('提示',"请先保存修改数据！",'warning');
		return;
	}
	
	var row = {id:''};
	
	$("#dgDeptTl").datagrid('insertRow',{
		index: 0,
		row:row
		});
}


function saveDeptTl(){
	endEditing();
	var rows = $("#dgDeptTl").datagrid('getChanges');
	var data =JSON.stringify(rows);
	if(rows.length > 0){
		 $.ajax({
			 url:'platform/sysgroup/sysGroupController/saveLanguageSet.json',
			 data : {datas : data, groupId:'${groupId}'},
			 type : 'post',
			 dataType : 'json',
			 success : function(data){
				 if(0==data.result){
					 $("#dgDeptTl").datagrid('reload',{});
					 $("#dgDeptTl").datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
					 $.messager.show({
						 title : '提示',
						 msg : '保存成功'
					 });
				 }else{
					 $.messager.alert('提示',data.msg,'warning');
				 }
			 }
		 });
	}else{
		$.messager.show({
			 title : '提示',
			 msg : '没有数据需要保存！'
		 });
	}
}

function deleteDeptTl(){
	
	var rows = $("#dgDeptTl").datagrid('getChecked');
	var ids = [];
	if (rows.length > 0) {
		var data=$('#dgDeptTl').datagrid('getRows');
		if(rows.length==data.length){
			$.messager.alert('提示',"至少需要一种语言！",'warning');
			return;
		}
		
		$.messager.confirm('请确认','您确定要删除当前所选的数据？',function(b){
			if(b){
				for ( var i = 0, length = rows.length; i < length; i++) {
					
					if(rows[i].id){
						ids.push(rows[i].id);
					}else{
						var rowIndex=$('#dgDeptTl').datagrid("getRowIndex",rows[i]);
						$('#dgDeptTl').datagrid("deleteRow",rowIndex);
					}
				}
				
				if(ids.length<=0) return;
				
			   $.ajax({
				 url:'platform/sysgroup/sysGroupController/deleteSysGroupTl.json',
				 data : {
						ids : ids.join(',')
					},
				 type : 'post',
				 dataType : 'json',
				 success : function(data){
					 if(0==data.result){
						 $("#dgDeptTl").datagrid('reload',{});
						 $("#dgDeptTl").datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
						 $.messager.show({
							 title : '提示',
							 msg : '操作成功'
						 });
					 }else{
						 $.messager.alert('提示',data.msg,'warning');
					 }
				 }
			 });
			}
		});
	}else{
		$.messager.alert('提示',"请选择数据！",'warning');
	}
}
</script>
</head>

<body class="easyui-layout" fit="true">
<div data-options="region:'center',title:''" style="padding:0px;">
	<div id="toolbarLanguage" >
						<table >
							<tr>
								<td><a class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="insertDeptTl();" href="javascript:void(0);">添加</a></td>
								<td><a class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="deleteDeptTl();" href="javascript:void(0);">删除</a></td>
								<td><a class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="saveDeptTl();" href="javascript:void(0);">保存</a></td>
							</tr>
						</table>
					</div> 
					<table id="dgDeptTl" class="easyui-datagrid"
							data-options="
								rownumbers: true,
								animate: true,
								collapsible: false,
								fitColumns: true,
								autoRowHeight: false,
								checkOnSelect:true,
								selectOnCheck:true,
								url: 'platform/sysgroup/sysGroupController/getSysGroupTlByParentId.json?id=${groupId}',
								method: 'post',
								fit : true,
								striped : false,
								idField :'id',
								toolbar:'toolbarLanguage',
								onClickCell:onClickCell,
								onAfterEdit:endEditing
								" >
							<thead>
								<tr>
									<th data-options="field:'sysGroupId', align:'center',align:'center',hidden:true" editor="{type:'text'}"></th>
									<th data-options="field:'id', halign:'center',checkbox:true" width="220">id</th>
									<th data-options="field:'sysLanguageCode',required:true,halign:'center',formatter:formatLanguage,align:'center',editor:{type:'combobox',options:{valueField:'languageCode',editable:false,textField:'languageName',data:languageData,panelHeight: 75}}"  width="220">语言名称</th>
									<th data-options="field:'groupName',required:true,align:'center'" editor="{type:'text'}" width="220">群组名称</th>
									<th data-options="field:'groupDesc',align:'center',align:'center'" editor="{type:'text'}"  width="220">群组描述</th>
								</tr>
							</thead>
					</table>
</div>
</body>
</html>