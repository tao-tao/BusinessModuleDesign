<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>多语言设置</title>
	<base href="<%=ViewUtil.getRequestPath(request) %>">
	<jsp:include page="/avicit/platform6/component/common/EasyUIJsInclude.jsp"></jsp:include>
	<jsp:include page="/avicit/platform6/modules/system/commonpopup/commonSelectionHead.jsp"></jsp:include>
	
	<script type="text/javascript" charset="utf-8">
		
	
	
		var baseurl = '<%=request.getContextPath()%>';
		
        var languageCodeCombo={};
                
        var editIndex=-1;
        var newRowCount=0;
        
        var currentCodeBox=null;
        var currentNameBox=null;
        
		$(function() {
			
			$.ajax({
				url: 'platform/syslanguage/sysLanguageController/getLanguageCodes.json',
				data : {},
				type :'POST',
				dataType :'json',
				success : function(r){
					if(r.json){
						languageCodeCombo =	r.json;
						
						// 后期处理一下
						for(var i=0;i<languageCodeCombo.length;i++)
						{
							languageCodeCombo[i].comboText=languageCodeCombo[i].languageCode+"("+languageCodeCombo[i].languageName+")"
						}
					}
				}
			});
			
		});
		
		/*****************查询start****************/
		// 打开查询框
		
		
		
		function formatcombobox(value){
			if(value ==null ||value == ''){
				return '';
			}
			for(var i =0 ,length = languageCodeCombo.length; i<length;i++){
				if(languageCodeCombo[i].languageCode == value){
					return languageCodeCombo[i].languageCode;
				}
			}
		}
		
		function formatcombobox2(value){
			if(value ==null ||value == ''){
				return '';
			}
			for(var i =0 ,length = languageCodeCombo.length; i<length;i++){
				if(languageCodeCombo[i].languageCode == value){
					return languageCodeCombo[i].languageName;
				}
			}
		}
		
				
		function addData()
		{
			newRowCount++;
			
			var myDatagrid=$('#languageDataGrid');
			
			myDatagrid.datagrid('endEdit',editIndex);
			if(editIndex != -1){
				$.messager.alert('提示','不能添加，请确保上一条数据填写完整','warning');
				return;
			}
			
			myDatagrid.datagrid('insertRow',{
				index: 0,
				row:{id:"", isSystemDefault: '0'}
				});	
			myDatagrid.datagrid('selectRow', 0).datagrid('beginEdit',0);
			editIndex=0;
			
			// 语言代码的下拉框
			var ed = myDatagrid.datagrid('getEditor',{index: 0, field: 'languageCode'});
			currentCodeBox=$(ed.target);
			
			// 这部分是由于onselect的副作用，造成了value的丢失和width的变化，而必须额外编写的程序
			var value=currentCodeBox.combobox('getValue');
			currentCodeBox.combobox('loadData', languageCodeCombo);
			currentCodeBox.combobox({onSelect:languageCodeSelect});
			currentCodeBox.combobox({width:currentCodeBox.parent().width()});
			currentCodeBox.combobox('setValue', value);
			
			
			// 语言名称的文本框
			var ed = myDatagrid.datagrid('getEditor',{index: 0, field: 'languageName'});
			$(ed.target).attr('disabled', true);
			currentNameBox=$(ed.target);
			
			// 默认语言的文本框
			var ed = myDatagrid.datagrid('getEditor',{index: 0, field: 'isSystemDefault'});
			$(ed.target).attr('disabled', true);
			
		}
		
		function dgLanguageOnClickRow(index,rowData)
		{
			
			
			var myDatagrid=$('#languageDataGrid');
			
			myDatagrid.datagrid('endEdit', editIndex);
			if(editIndex==-1)
			{
				myDatagrid.datagrid('beginEdit', index);  
				editIndex=index;
				
				// 语言代码的下拉框
				var ed = myDatagrid.datagrid('getEditor',{index: index, field: 'languageCode'});
				currentCodeBox=$(ed.target);
				
				// 这部分是由于onselect的副作用，造成了value的丢失和width的变化，而必须额外编写的程序
				var value=currentCodeBox.combobox('getValue');
				currentCodeBox.combobox('loadData', languageCodeCombo);
				currentCodeBox.combobox({onSelect:languageCodeSelect});
				currentCodeBox.combobox({width:currentCodeBox.parent().width()});
				currentCodeBox.combobox('setValue', value);
				
				
				// 语言名称的文本框
				var ed = myDatagrid.datagrid('getEditor',{index: index, field: 'languageName'});
				$(ed.target).attr('disabled', true);
				currentNameBox=$(ed.target);
				
				// 默认语言的文本框
				var ed = myDatagrid.datagrid('getEditor',{index: index, field: 'isSystemDefault'});
				$(ed.target).attr('disabled', true);
				
			}
			else
			{
				$.messager.alert('提示','不能编辑，请确保上一条数据填写完整','warning');
			} 
			
		    
		}
		
		function languageCodeSelect(record)
		{
			if(currentCodeBox==null || currentNameBox==null)
				return;
			
			var value=currentCodeBox.combobox('getValue');
			currentNameBox.val(formatcombobox2(value)); 
						
		}

		function dgLanguageOnAfterEdit(rowIndex, rowData, changes)
		{
			//成功完成编辑，包括校验
			editIndex=-1; 
		} 

		function dgLanguageOnLoadSuccess(data)
		{
			if(editIndex != -1){
				$('#languageDataGrid').datagrid('cancelEdit',editIndex);
				editIndex = -1;
			} 
		}
		
		function saveData()
		{
			var myDatagrid=$('#languageDataGrid');
			myDatagrid.datagrid('endEdit',editIndex);
			
			if(editIndex!=-1)
			{
				$.messager.alert('提示','不能保存，请确保上一条数据填写完整','warning');
				return;
			}
			
			var rows = myDatagrid.datagrid('getChanges');
			var data =JSON.stringify(rows);
			if(rows.length > 0)
			{
				 $.ajax({
					 url:'platform/syslanguage/sysLanguageController/saveSysLanguages',
					 data : {datas : data},
					 type : 'post',
					 dataType : 'json',
					 success : function(r){
						if(r.result==0){
							 myDatagrid.datagrid('reload',{});
							 myDatagrid.datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
							 $.messager.show({
								 title : '提示',
								 msg : '保存成功'
							 });
						 }else{
							 $.messager.alert('提示','保存失败','warning');
						 } 
					 }
				 });
			 } 
		}
		
		function deleteData()
		{
			var myDatagrid=$('#languageDataGrid');
			
			var rows = myDatagrid.datagrid('getChecked');
			var data =JSON.stringify(rows);
			
			
			if (rows.length > 0) {
				$.messager.confirm('请确认', '您确定要删除当前所选的数据？', function(b) {
					if (b) {
						
						
						$.ajax({
							url : 'platform/syslanguage/sysLanguageController/deleteSysLanguages',
							data : {
								datas : data
							},
							type : 'post',
							dataType : 'json',
							success : function(r) {
								
								if(r.result==0){
									myDatagrid.datagrid('reload', {});//刷新当前页
									myDatagrid.datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
									editIndex = -1;
									$.messager.show({
										title : '提示',
										msg : '删除成功！'
									});
								}
								else
								{
									$.messager.alert('提示','删除失败','warning');
								}
							}
						}); 
					}
				});
			} else {
				$.messager.alert('提示', '请选择要删除的记录！', 'warning');
			}
		}
		
		function setDefaultLanguage()
		{
			var myDatagrid=$('#languageDataGrid');
			
			var rows = myDatagrid.datagrid('getChecked');
			var data =JSON.stringify(rows);
			
			if (rows.length == 1) {
				
				var id=rows[0].id;		
						
				$.ajax({
					url : 'platform/syslanguage/sysLanguageController/setDefaultLanguage',
					data : {
						id : id
					},
					type : 'post',
					dataType : 'json',
					success : function(r) {
						
						if(r.result==0){
							myDatagrid.datagrid('reload', {});//刷新当前页
							myDatagrid.datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
							editIndex = -1;
							$.messager.show({
								title : '提示',
								msg : '设置成功！'
							});
						}
						else
						{
							$.messager.alert('提示','设置失败','warning');
						}
					}
				}); 
					
			} else {
				$.messager.alert('提示', '请选择一个语言！', 'warning');
			}
		}
		
	function languageFilter(data)
	{
		
		for(var i=0;i<data.rows.length;i++)
		{
			if(data.rows[i].isSystemDefault=='1')
				data.rows[i].isSystemDefault='默认语言';
			else
				data.rows[i].isSystemDefault='';
		}
		
		return data;
		
	}
		
	</script>
</head>
<body>
	<div id="toolbar" >
		<table class="toolbarTable">
			<tr>
				<sec:accesscontrollist hasPermission="3" domainObject="system_use11_add"><td><a class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addData();" href="javascript:void(0);">添加</a></td></sec:accesscontrollist>
				<sec:accesscontrollist hasPermission="3" domainObject="system_user11_save"><td><a class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="saveData();" href="javascript:void(0);">保存</a></td></sec:accesscontrollist>
				<sec:accesscontrollist hasPermission="3" domainObject="system_user11_delete"><td><a class="easyui-linkbutton" iconCls="icon-no" plain="true" onclick="deleteData();" href="javascript:void(0);">删除</a></td></sec:accesscontrollist>
				<sec:accesscontrollist hasPermission="3" domainObject="system_user11_delete"><td><a class="easyui-linkbutton" iconCls="icon-setting" plain="true" onclick="setDefaultLanguage();" href="javascript:void(0);">设置默认语言</td></sec:accesscontrollist>
			</tr>
		</table>
	</div> 
	
	<table id="languageDataGrid" class="easyui-datagrid"
		data-options=" 
			fit: true,
			border: false,
			rownumbers: true,
			animate: true,
			collapsible: false,
			fitColumns: true,
			autoRowHeight: false,
			toolbar:'#toolbar',
			idField :'id',
			singleSelect: false,
			checkOnSelect: false,
			selectOnCheck: true,
			
			
			striped:true,
			
			url:'platform/syslanguage/sysLanguageController/getAll.json',
			
			loadFilter: languageFilter,
			
			onClickRow: dgLanguageOnClickRow,
			onAfterEdit: dgLanguageOnAfterEdit,
			onLoadSuccess: dgLanguageOnLoadSuccess
			">
		<thead>
			<tr>
				<th data-options="field:'id', halign:'center',checkbox:true" width="220">id</th>				
				<th data-options="field:'languageCode',halign:'center',align:'left', formatter: formatcombobox" editor="{type:'combobox',options:{required:true,editable:false,valueField:'languageCode',textField:'comboText'}}" width="220">语言代码</th>
				<th data-options="field:'languageName', halign:'center',align:'left'" editor="{type:'validatebox',options:{required:true}}"   width="220">语言名称</th>
								
								
				<th data-options="field:'isSystemDefault',halign:'center',align:'left'" editor="{type:'text'}" width="220">系统默认语言</th>
				
				
				
			</tr>
		</thead>
	</table>
	
	

</body>
</html>