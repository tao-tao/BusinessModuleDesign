<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>用户关系管理</title>
	<base href="<%=ViewUtil.getRequestPath(request) %>">
	<jsp:include page="/avicit/platform6/component/common/EasyUIJsInclude.jsp"></jsp:include>
	<jsp:include page="/avicit/platform6/modules/system/commonpopup/commonSelectionHead.jsp"></jsp:include>
	<script type="text/javascript" charset="utf-8">
		var baseurl = '<%=request.getContextPath()%>';
		var userRelationSearchDialog;
		var userRelationSearchForm;
        var validateCombo={};
        var relationCombo=${relationCombo};
        
        var editIndex=-1;
        var newRowCount=0;
        
		$(function() {
			
			$.ajax({
				url: 'platform/sysUserRelationController/getComboData.json',
				data : {'lookupType' : 'PLATFORM_VALID_FLAG'},
				type :'POST',
				dataType :'json',
				success : function(r){
					if(r.lookup){
						validateCombo =	r.lookup;
					}
				}
			});
			
			$.ajax({
				url: 'platform/sysUserRelationController/getComboData.json',
				data : {'lookupType' : 'PLATFORM_SYS_USER_RELATION_TYPE'},
				type :'POST',
				dataType :'json',
				async:'false',
				success : function(r) {
					if(r.lookup){
						//relationCombo =	r.lookup;
						var comboxData = [];
						for(var i = 0; i < relationCombo.length; i++){
							comboxData.push({"value":relationCombo[i].lookupCode,"text":relationCombo[i].lookupName});
						}
						$('#filter_EQ_userRelation').combobox('loadData', comboxData);
					}
				}
			});
			
			
			
			/*****************初始化查询框start****************/
						
			$('#userRelationSearchDialog').dialog('close');
			
			/*****************初始化查询框end****************/
			
		  	var commonSelector = new CommonSelector("user","userSelectCommonDialog", "filter_EQ_user1Id" ,"filter_LIKE_user1Name",null,null);
			commonSelector.init(null,null,'n'); //选择人员  回填部门 */
			
			var commonSelector = new CommonSelector("user","userSelectCommonDialog", "filter_EQ_user2Id" ,"filter_LIKE_user2Name",null,null);
			commonSelector.init(null,null,'n'); //选择人员  回填部门 */
			
		});
		
		/*****************查询start****************/
		// 打开查询框
		function openSearchDialog(){
			$('#userRelationSearchDialog').dialog('open', true);
		}

		// 清空查询条件
		function clearSearchData() {
			$('#userRelationSearchForm input').val('');
		}
		
		// 隐藏查询框
		function hideSearchDialog(){
			clearSearchData();
			$('#userRelationSearchDialog').dialog('close', true);
		}
		
		// 执行查询
		function userRelationSearchData() {
			//userRelationSearchDialog.datagrid('load', { param : JSON.stringify(serializeObject(userRelationSearchForm))});
			
			var user1Id=$('#filter_EQ_user1Id').val();
			var user2Id=$('#filter_EQ_user2Id').val();
			var relation=$('#filter_EQ_userRelation').combobox('getValue');
			
			$("#userRealtionDataGrid").datagrid("options").url ="platform/sysUserRelationController/getSysUserRelationVo.json";
			$('#userRealtionDataGrid').datagrid("reload", {user1Id: user1Id, user2Id: user2Id, relation: relation});
			$("#userRealtionDataGrid").datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');	
			
		}
		/*****************查询end****************/
		
		
		function formatcombobox(value){
			if(value ==null ||value == ''){
				return '';
			}
			for(var i =0 ,length = validateCombo.length; i<length;i++){
				if(validateCombo[i].lookupCode == value){
					return validateCombo[i].lookupName;
				}
			}
		}
		
		function formatcombobox2(value){
			if(value ==null ||value == ''){
				return '';
			}
			for(var i =0; i<relationCombo.length;i++){
				if(relationCombo[i].lookupCode == value){
					return relationCombo[i].lookupName;
				}
			}
		}
		
		function addData()
		{
			newRowCount++;
			
			var myDatagrid=$('#userRealtionDataGrid');
			
			myDatagrid.datagrid('endEdit',editIndex);
			if(editIndex != -1){
				$.messager.alert('提示','不能添加，请确保上一条数据填写完整','warning');
				return;
			}
			
			myDatagrid.datagrid('insertRow',{
				index: 0,
				row:{id:"", relation: "1", validFlag: "1"}
				});	
			myDatagrid.datagrid('selectRow', 0).datagrid('beginEdit',0);
			editIndex=0;
			
			
			// 有效无效的下拉框
			var ed = myDatagrid.datagrid('getEditor',{index: 0, field: 'validFlag'});
			$(ed.target).combobox('loadData', validateCombo);
			
			// 用户关系的下拉框
			var ed = myDatagrid.datagrid('getEditor',{index: 0, field: 'relation'});
			$(ed.target).combobox('loadData', relationCombo);
			
			//选择人员, 很奇怪，当设置attr('disabled')的时候，validatebox失效
			
			var ed = myDatagrid.datagrid('getEditor',{index: 0, field: 'user1Name'});
			$(ed.target).attr('id', "receptUserName"+newRowCount);
			$(ed.target).attr('disabled', true);
			
			var ed = myDatagrid.datagrid('getEditor',{index: 0, field: 'user1Id'});
			$(ed.target).attr('id', "receptUserId"+newRowCount);
			//$(ed.target).attr('disabled', true);
			
			
		   	var commonSelector = new CommonSelector("user","userSelectCommonDialog", "receptUserId"+newRowCount ,"receptUserName"+newRowCount,null,null);
		   	commonSelector.init(null,null,'n'); //选择人员  回填部门 
		   	
		   	var ed = myDatagrid.datagrid('getEditor',{index: 0, field: 'user2Name'});
			$(ed.target).attr('id', "receptUser2Name"+newRowCount);
			$(ed.target).attr('disabled', true);
			
			var ed = myDatagrid.datagrid('getEditor',{index: 0, field: 'user2Id'});
			$(ed.target).attr('id', "receptUser2Id"+newRowCount);
			//$(ed.target).attr('disabled', true);
			
		   	var commonSelector2 = new CommonSelector("user","userSelectCommonDialog2", "receptUser2Id"+newRowCount ,"receptUser2Name"+newRowCount,null,null);
		   	commonSelector2.init(null,null,'n'); //选择人员  回填部门  
		}
		
		function dgRelationOnClickRow(index,rowData)
		{
			newRowCount++;
			
			var myDatagrid=$('#userRealtionDataGrid');
			
			myDatagrid.datagrid('endEdit', editIndex);
			if(editIndex==-1)
			{
				myDatagrid.datagrid('beginEdit', index);  
				editIndex=index;
				
				// 有效无效的下拉框
				var ed = myDatagrid.datagrid('getEditor',{index: index, field: 'validFlag'});
				$(ed.target).combobox('loadData', validateCombo);
				
				// 用户关系的下拉框
				var ed = myDatagrid.datagrid('getEditor',{index: index, field: 'relation'});
				$(ed.target).combobox('loadData', relationCombo);
				
				//选择人员
				
				var ed = myDatagrid.datagrid('getEditor',{index: index, field: 'user1Name'});
				$(ed.target).attr('id', "receptUserName"+newRowCount);
				$(ed.target).attr('disabled', true);
				
				var ed = myDatagrid.datagrid('getEditor',{index: index, field: 'user1Id'});
				$(ed.target).attr('id', "receptUserId"+newRowCount);
				//$(ed.target).attr('disabled', true);  
				
			   	var commonSelector = new CommonSelector("user","userSelectCommonDialog", "receptUserId"+newRowCount ,"receptUserName"+newRowCount,null,null);
			   	commonSelector.init(null,null,'n'); //选择人员  回填部门 */
			   	
			   	var ed = myDatagrid.datagrid('getEditor',{index: index, field: 'user2Name'});
				$(ed.target).attr('id', "receptUser2Name"+newRowCount);
				$(ed.target).attr('disabled', true);
				
				var ed = myDatagrid.datagrid('getEditor',{index: index, field: 'user2Id'});
				$(ed.target).attr('id', "receptUser2Id"+newRowCount);
				//$(ed.target).attr('disabled', true); 
				
			   	var commonSelector2 = new CommonSelector("user","userSelectCommonDialog2", "receptUser2Id"+newRowCount ,"receptUser2Name"+newRowCount,null,null);
			   	commonSelector2.init(null,null,'n'); //选择人员  回填部门
				
				
			}
			else
			{
				$.messager.alert('提示','不能编辑，请确保上一条数据填写完整','warning');
			} 
			
		    
		}

		function dgRelationOnAfterEdit(rowIndex, rowData, changes)
		{
			//成功完成编辑，包括校验
			editIndex=-1; 
		} 

		function dgRelationOnLoadSuccess(data)
		{
			if(editIndex != -1){
				$('#userRealtionDataGrid').datagrid('cancelEdit',editIndex);
				editIndex = -1;
			} 
		}
		
		function saveData()
		{
			var myDatagrid=$('#userRealtionDataGrid');
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
					 url:'platform/sysUserRelationController/saveOrUpdateSysUserRelation',
					 data : {datas : data},
					 type : 'post',
					 async : false,
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
			var myDatagrid=$('#userRealtionDataGrid');
			
			var rows = myDatagrid.datagrid('getChecked');
			var ids=[];
			
			
			if (rows.length > 0) {
				$.messager.confirm('请确认', '您确定要删除当前所选的数据？', function(b) {
					if (b) {
						
						for(var i=0;i<rows.length;i++)
							ids.push(rows[i].id);
						
						$.ajax({
							url : 'platform/sysUserRelationController/deleteUserRelation',
							data : {
								ids : ids.join(",")
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
		
		function setValidFlag(flag)
		{
			var myDatagrid=$('#userRealtionDataGrid');
			
			var rows = myDatagrid.datagrid('getChecked');
			var ids=[];
			
			var msg="";
			if(flag==1)
				msg="确定要将这（些）行设为有效状态吗?";
			else
				msg="确定要将这（些）行设为无效状态吗?";
				
			if (rows.length > 0) {
				
				
				$.messager.confirm('请确认', msg , function(b) {
					if (b) {
						
						for(var i=0;i<rows.length;i++)
							ids.push(rows[i].id);
						
						$.ajax({
							url : 'platform/sysUserRelationController/changeValidFlag',
							data : {
								ids : ids.join(","),
								flag : flag
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
					}
				});
			} else {
				$.messager.alert('提示', '请选择要设置的记录！', 'warning');
			}
		}
		
		
		
	</script>
</head>
<body class="easyui-layout" fit="true">
 <div data-options="region:'center',split:true,border:false" style="padding:0px;overflow:hidden;">
	<div id="toolbar" >
		<table class="toolbarTable">
			<tr>
				<td><a class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addData();" href="javascript:void(0);">添加</a></td>
				<td><a class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="saveData();" href="javascript:void(0);">保存</a></td>
				<td><a class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="setValidFlag(1);" href="javascript:void(0);">设置为有效</a></td>
				<td><a class="easyui-linkbutton" iconCls="icon-close-all" plain="true" onclick="setValidFlag(0);" href="javascript:void(0);">设置为无效</a></td>
				<td><a class="easyui-linkbutton" iconCls="icon-no" plain="true" onclick="deleteData();" href="javascript:void(0);">删除</a></td>
				<td><a class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="openSearchDialog();" href="javascript:void(0);">查询</a></td>
			</tr>
		</table>
	</div> 
	
	<table id="userRealtionDataGrid" class="easyui-datagrid"
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
			singleSelect: true,
			checkOnSelect: true,
			selectOnCheck: false,
			
			pagination:true,
			pageSize:10,
			pageList:dataOptions.pageList,
			
			striped:true,
			
			url:'platform/sysUserRelationController/getSysUserRelationVo.json',
			
			onClickRow: dgRelationOnClickRow,
			onAfterEdit: dgRelationOnAfterEdit,
			onLoadSuccess: dgRelationOnLoadSuccess
			">
		<thead>
			<tr>
				<th data-options="field:'id', halign:'center',checkbox:true" width="220">id</th>
				<th data-options="field:'user1Id', halign:'center',align:'left', hidden: true" editor="{type:'validatebox',options:{required:true}}" width="220">用户1Id</th>
				<th data-options="field:'user1Name', halign:'center',align:'left'" editor="{type:'validatebox',options:{required:true}}"  width="220">用户1姓名</th>
				<th data-options="field:'relation',halign:'center',align:'left', formatter:formatcombobox2" editor="{type:'combobox',options:{required:true,panelHeight:'auto',editable:false,valueField:'lookupCode',textField:'lookupName'}}" width="220">用户关系</th>
				<th data-options="field:'user2Id', halign:'center',align:'left', hidden: true" editor="{type:'validatebox',options:{required:true}}" width="220">用户2Id</th>
				<th data-options="field:'user2Name', halign:'center',align:'left'" editor="{type:'validatebox',options:{required:true}}"   width="220">用户2姓名</th>
				<th data-options="field:'validFlag',halign:'center',align:'left', formatter:formatcombobox" editor="{type:'combobox',options:{required:true,panelHeight:'auto',editable:false,valueField:'lookupCode',textField:'lookupName'}}"  width="220">状态</th>
			</tr>
		</thead>
	</table>
	
	<div id="userRelationSearchDialog" class="easyui-dialog" data-options="iconCls:'icon-search',resizable:true,modal:false,buttons:'#userRelationsearchBtns'" 
			style="width: 430px;height:150px; visible: hidden" title="查询条件">
		<form id="userRelationSearchForm">
    		<input id="filter_EQ_user1Id" type='text' name="filter_EQ_user1Id" hidden="true" style="display: none"/>
    		<input id="filter_EQ_user2Id" type='text' name="filter_EQ_user2Id" hidden="true" style="display: none"/>
    		<table>
    			
    			<tr>
    				
    				<td>用户1:</td><td><input id="filter_LIKE_user1Name" type='text' name="filter_LIKE_user1Name" style="width: 150px"/></td>
    				
    				<td>用户2:</td><td><input id="filter_LIKE_user2Name" type='text' name="filter_LIKE_user2Name" style="width: 150px"/></td>
    			</tr>
    			<tr>
    				<td>用户关系:</td>
    				<td colspan="2">
    					<input id="filter_EQ_userRelation" class='easyui-combobox' data-options="panelHeight:'auto',editable:false"
    						name ="filter_EQ_userRelation" />
    				</td>
    			</tr>
    		</table>
    	</form>
    	<div id="userRelationsearchBtns">
    		<a class="easyui-linkbutton"  plain="false" onclick="userRelationSearchData();" href="javascript:void(0);">查询</a>
    		<a class="easyui-linkbutton" plain="false" onclick="clearSearchData();" href="javascript:void(0);">清空</a>
    		<a class="easyui-linkbutton"   plain="false" onclick="hideSearchDialog();" href="javascript:void(0);">返回</a>
    	</div>
     </div>
</div>
</body>
</html>