<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>

<div id="toolbarTableMap" class="datagrid-toolbar">
		 	<table>
		 		<tr>
					<td><a class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="comDatagrid.insert()" href="javascript:void(0);">添加</a></td>
					<td><a class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="comDatagrid.save(currentMenuId)" href="javascript:void(0);">保存</a></td>
					<td><a class="easyui-linkbutton" iconCls="icon-reload" plain="true" onclick="comDatagrid.refreshComponent()" href="javascript:void(0);">重载页面组件</a></td>
					<td><a class="easyui-linkbutton" iconCls="icon-reload" plain="true" onclick="comDatagrid.refreshCom()" href="javascript:void(0);">刷新权限信息</a></td>
					<td><a class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="comDatagrid.del()" href="javascript:void(0);">删除</a></td>
					
					
				</tr>
		 	</table>
	 	</div>
	 	<table id="dgCom" class="easyui-datagrid" datapermission="sysSyncTableMapgrid"
							data-options=" 
								fit: true,
								border: false,
								rownumbers: true,
								animate: true,
								collapsible: false,
								fitColumns: true,
								autoRowHeight: false,
								toolbar:'#toolbarTableMap',
								idField :'id',
								singleSelect: true,
								checkOnSelect: true,
								selectOnCheck: false,
								pagination:true,
								pageSize:dataOptions.pageSize,
								pageList:dataOptions.pageList,
								striped:true,
								queryParams:{currentMenuId:currentMenuId},
								url:'platform/componentManagerController/getComponentListByPage.json',
								
								onSelect: function(rowIndex, rowData){
								    if (rowData.id!=null&&rowData.id!=''){
										curComId = rowData.id;
									}else{
										curComId = 'none';
									}
									reloadTabData(currTabIndex);
								},
								onLoadSuccess: function(data){
									if (data.rows.length != 0) {
										$('#dgCom').datagrid('selectRow', 0);
									}else{
										curComId='none';
										reloadTabData(currTabIndex);
									}
								}">
							<thead>
								<tr>
									<th data-options="field:'id', halign:'center',checkbox:true" width="220">id</th>
									<th data-options="field:'key',required:true,align:'center'" editor="{type:'text'}" width="220">组件名称</th>
									<th data-options="field:'value',required:true,align:'center'" editor="{type:'text'}" width="220">组件标识</th>
									<th data-options="field:'desc',required:true,align:'center'" editor="{type:'text'}" width="220">说明</th>
								</tr>
							</thead>
		</table>