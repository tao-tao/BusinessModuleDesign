<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>个人设置</title>
<base href="<%=ViewUtil.getRequestPath(request) %>">
<link href="avicit/platform6/modules/system/sysdept/css/icon.css" type="text/css" rel="stylesheet">
<jsp:include page="/avicit/platform6/component/common/EasyUIJsInclude.jsp"></jsp:include>
<jsp:include page="/avicit/platform6/modules/system/commonpopup/commonSelectionHead.jsp"></jsp:include>
</head>

<script src="avicit/platform6/modules/system/syscustomed/syscustomed.js" type="text/javascript"></script>

<body class="easyui-layout" fit="true">


	<div data-options="region:'north',split:false,title:'菜单样式'" style="height: 250px; padding:0px;">	
		
		
		<table id="menuStyle" width="100%">
			 <tr>
			</tr> 
		</table>
		
	</div>
	
	
		
	<div data-options="region:'center',split:false,title:''" style="padding:0px;">	
		
		<div id="tt" fit="true" >   
		    <div title="个人群组" >   
		    	
		        <div id="toolbar" class="datagrid-toolbar" style="height:auto;display: block;">
						<a id="btAddSelfGroup"  class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addGroup();" href="javascript:void(0);">添加群组</a>
															
						<a id="btSaveSelfGroup"  class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="saveGroup();" href="javascript:void(0);">保存群组</a>
					
						<a id="btDeleteSelfGroup" class="easyui-linkbutton" iconCls="icon-remove"  plain="true" onclick="deleteGroup();" href="javascript:void(0);">删除群组</a>
										
					
				</div>
				
				<table id="dgGroup" class="easyui-datagrid"
					data-options=" 
						fit: true,
						border: false,
						rownumbers: false,
						animate: true,
						collapsible: false,
						fitColumns: true,
						autoRowHeight: false,
						toolbar:'#toolbar',
						idField :'id',
						singleSelect: false,
						checkOnSelect: false,
						selectOnCheck: true,
						
						pagination:true,
						pageSize:dataOptions.pageSize,
						pageList:dataOptions.pageList,
						
						striped:true,
						
						url: 'platform/syscustomed/sysCustomedController/getPersonalGroupListByPage',
						onClickRow: dgGroupOnClickRow,
						onAfterEdit: dgGroupOnAfterEdit,
						onLoadSuccess: dgGroupOnLoadSuccess
						
						">
					<thead>
						<tr>
							<th data-options="field:'id', halign:'center' ,checkbox:true" >id</th>
							<th data-options="field:'sysGroupId',halign:'center',align:'left', hidden: true" editor="{type:'text'}" width="220">群组ID</th>
							<th data-options="field:'sysGroupName',halign:'center',align:'left'" editor="{type:'validatebox', options:{required:true, validType: 'length[0,50]'}}" width="110">群组名称</th> 
							<th data-options="field:'sysUserId',halign:'center',align:'left', hidden: true" editor="{type:'text'}" width="220">群组成员ID</th>
							<th data-options="field:'sysUserName',halign:'center',align:'left'" editor="{type:'text'}" width="220">群组成员</th>
							
						</tr>
					</thead>
				</table>	   
		    </div>   
		    <div title="常用审批意见" >   
		        <div id="toolbar2" class="datagrid-toolbar" style="height:auto;display: block;">
						<a id="btAddApproval"  class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addApproval();" href="javascript:void(0);">添加意见</a>
															
						<a id="btSaveApproval"  class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="saveApproval();" href="javascript:void(0);">保存意见</a>
					
						<a id="btDeleteApproval" class="easyui-linkbutton" iconCls="icon-remove"  plain="true" onclick="deleteApproval();" href="javascript:void(0);">删除意见</a>
										
					
				</div>
				
				<table id="dgApproval" class="easyui-datagrid"
					data-options=" 
						fit: true,
						border: false,
						rownumbers: false,
						animate: true,
						collapsible: false,
						fitColumns: true,
						autoRowHeight: false,
						toolbar:'#toolbar2',
						idField :'id',
						singleSelect: false,
						checkOnSelect: false,
						selectOnCheck: true,
						
						pagination:true,
						pageSize:dataOptions.pageSize,
						pageList:dataOptions.pageList,
						
						striped:true,
						
						url: 'platform/syscustomed/sysCustomedController/getSysCustomedSettingByPage',
						queryParams: {filter_EQ_key:'PLATFORM_APPROVALOPTION'},
						onClickRow: dgApprovalOnClickRow,
						onAfterEdit: dgApprovalOnAfterEdit,
						onLoadSuccess: dgApprovalOnLoadSuccess
						
						">
					<thead>
						<tr>
							<th data-options="field:'id', halign:'center' ,checkbox:true" >id</th>
							<th data-options="field:'value',halign:'center',align:'left'" editor="{type:'validatebox', options:{}}" width="220">审批意见</th>
							
							
						</tr>
					</thead>
				</table>	 
		    </div>   
		    <div title="其他" >   
		        <div id="toolbar3" class="datagrid-toolbar" style="height:auto;display: block;">
						<a id="btAddOther"  class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addOther();" href="javascript:void(0);">添加行</a>
															
						<a id="btSaveOther"  class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="saveOther();" href="javascript:void(0);">保存</a>
					
						<a id="btDeleteOther" class="easyui-linkbutton" iconCls="icon-remove"  plain="true" onclick="deleteOther();" href="javascript:void(0);">删除</a>
										
					
				</div>
				
				<table id="dgOther" class="easyui-datagrid"
					data-options=" 
						fit: true,
						border: false,
						rownumbers: false,
						animate: true,
						collapsible: false,
						fitColumns: true,
						autoRowHeight: false,
						toolbar:'#toolbar3',
						idField :'id',
						singleSelect: false,
						checkOnSelect: false,
						selectOnCheck: true,
						
						pagination:true,
						pageSize:dataOptions.pageSize,
						pageList:dataOptions.pageList,
						
						striped:true,
						url: 'platform/syscustomed/sysCustomedController/getSysCustomedSettingByPage',
						
						queryParams: {filter_NOTIN_key: '\'PLATFORM_APPROVALOPTION\',\'PLATFORM_MENU_STYLE\',\'PLATFORM_PERSONALGROUP\''},
												
						onClickRow: dgOtherOnClickRow,
						onAfterEdit: dgOtherOnAfterEdit,
						onLoadSuccess: dgOtherOnLoadSuccess
						
						">
					<thead>
						<tr>
							<th data-options="field:'id', halign:'center' ,checkbox:true" >id</th>
							<th data-options="field:'key',halign:'center',align:'left'" editor="{type:'validatebox', options:{}}" width="220">属性名</th>
							<th data-options="field:'value',halign:'center',align:'left'" editor="{type:'validatebox', options:{}}" width="220">属性值</th>
							
							
						</tr>
					</thead>
				</table>  
		    </div>   
		</div>  
	</div>
	
	


</body>
</html>