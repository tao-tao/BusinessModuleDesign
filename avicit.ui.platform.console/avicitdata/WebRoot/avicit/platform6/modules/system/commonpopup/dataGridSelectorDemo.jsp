<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>   
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<%@ taglib prefix="sec" uri="/WEB-INF/tags/shiro.tld" %>
<html >
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title></title>
<base href="<%=ViewUtil.getRequestPath(request) %>">
<jsp:include page="/avicit/platform6/component/common/EasyUIJsInclude.jsp"></jsp:include>
<jsp:include page="/avicit/platform6/modules/system/commonpopup/commonSelectionHead.jsp"></jsp:include>
	<script type="text/javascript">
		var myDatagrid; 
		var searchDialog;
		var searchForm;
		var editIndex = undefined;
		
		$(function(){
			/*初始化datagrid*/
			myDatagrid =$('#dgTestPermission').datagrid({
				onBeforeEdit : function(rowIndex, rowData){
					
				},
				onClickCell:function(rowIndex, field, value){
					/**
					if(field == "bzPeople") {   
						   var userSelect = new GridCommonSelector("user","dgTestPermission",rowIndex,"bzPeople",{userId:'isNeedCar',userName:'bzPeople'});//
						   var rows = $('#dgTestPermission').datagrid("getRows");
						   userSelect.init(rows[rowIndex]);
					   } 
			
					 
					   if(field == "bzLocation") {   
						   var userSelect = new GridCommonSelector("dept","dgTestPermission",rowIndex,"bzLocation",{deptId:'bzByAirplane',deptName:'bzLocation'});
						   var rows = $('#dgTestPermission').datagrid("getRows");
						   userSelect.init(rows[rowIndex]);
					   }
					   if(field == "bzDays") {   
						   var userSelect = new GridCommonSelector("group","dgTestPermission",rowIndex,"bzDays",{groupId:'bzByTrain',groupName:'bzDays'});
						   var rows = $('#dgTestPermission').datagrid("getRows");
						   userSelect.init(rows[rowIndex]);
					   }
					   if(field == "bzNeedMoneyQty") {   
						   var userSelect = new GridCommonSelector("role","dgTestPermission",rowIndex,"bzNeedMoneyQty",{roleId:'bzResult',roleName:'bzNeedMoneyQty'});
						   var rows = $('#dgTestPermission').datagrid("getRows");
						   userSelect.init(rows[rowIndex]);
					   }
					   if(field == "bzComments") {   
						   var userSelect = new GridCommonSelector("position","dgTestPermission",rowIndex,"bzComments",{positionId:'secretLevel',positionName:'bzComments'});
						   var rows = $('#dgTestPermission').datagrid("getRows");
						   userSelect.init(rows[rowIndex]);
					   }  **/
				},
				onAfterEdit : function(rowIndex, rowData, changes){
					editIndex = -1;
				},
				onClickRow : function(rowIndex,rowData){
				 if(editIndex != undefined){
						myDatagrid.datagrid('endEdit',editIndex);
					}
					myDatagrid.datagrid('selectRow', rowIndex).datagrid('beginEdit',rowIndex);
					editIndex = rowIndex;
				}
			});
			/*初始化查询框*/
			searchDialog =$('#searchDialog').dialog({
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
		}
		//去后台查询
		function searchData(){
			myDatagrid.datagrid('load',{ param : JSON.stringify(serializeObject(searchForm))});
		}
		//隐藏查询框
		function hideSearch(){
			searchDialog.dialog('close',true);
		}
		/*清空查询条件*/
		function clearData(){
			searchForm.find('input').val('');
		};

		/*增加一条记录*/
		function addData(){
			if(editIndex != undefined){
				 myDatagrid.datagrid('endEdit',editIndex);
			} 
			myDatagrid.datagrid('insertRow',{
				index: 0,
				row:{id:""}
				});
			
			myDatagrid.datagrid('selectRow', 0).datagrid('beginEdit',0);
			editIndex=0;
		}
		
		function saveData(){
			
			var ed = myDatagrid.datagrid('getEditors', 0);
			//console.info(ed);
			myDatagrid.datagrid('endEdit',editIndex);
			var rows = myDatagrid.datagrid('getChanges');
			var data =JSON.stringify(rows);
			 if(rows.length > 0){
						 $.ajax({
							 url:'',
							 data : {datas : data},
							 type : 'post',
							 dataType : 'json',
							 success : function(r){
								 if(r.result){
									 myDatagrid.datagrid('reload',{});
									 myDatagrid.datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
									 $.messager.show({
										 title : '提示',
										 msg : '保存成功'
									 });
								 }else{
									 $.message.alert('提示','保存失败','warning');
								 }
								 
							 }
						 });
					 } 
			
		}
		/*删除选中数据*/
		function deleteData(){
		  var rows = myDatagrid.datagrid('getSelections');
		  var ids = [];
		  if(rows.length > 0){
			  $.messager.confirm('请确认','您确定要删除当前所选的数据？',function(b){
				 if(b){
					 for(var i =0,length = rows.length; i<length;i++){
						 ids.push(rows[i].id);
					 }
					 $.ajax({
						 url:'',
						 data:{
							 ids : ids.join(',')
						 },
						 type : 'post',
						 dataType : 'json',
						 success : function(r){
							 myDatagrid.datagrid('reload',{});//刷新当前页
							 myDatagrid.datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
							 $.messager.show({
								 title : '提示',
								 msg : '删除成功！'
							 });
						 }
					 });
				 } 
			  });
		  }else{
			  $.message.alert('提示','请选择要删除的记录！','warning');
		  }
		}
	
		
		$.extend($.fn.datagrid.defaults.editors, {
			datebox : {
				init : function(container, options) {
					var input = $('<input type="text">').appendTo(container);
					input.datebox(options);
					return input;
				},
				destroy : function(target) {
					$(target).datebox('destroy');
				},
				getValue : function(target) {
					return $(target).datebox('getValue');
				},
				setValue : function(target, value) {
					$(target).datebox('setValue', formatDatebox(value));
				},
				resize : function(target, width) {
					$(target).datebox('resize', width);
				}
			}
		});

		function formatDatebox(value) {
			if (value == null || value == '') {
				return '';
			}
			var dt;
			if (value instanceof Date) {
				dt = value;
			} else {
				dt = new Date(value);
				if (isNaN(dt)) {
					//标红的这段是关键代码，将那个长字符串的日期值转换成正常的JS日期格式
					value = value.replace(/\/Date\((-?\d+)\)\//, '$1');
					dt = new Date();
					dt.setTime(value);
				}
			}
			return dt.format("yyyy-MM-dd"); //这里用到一个javascript的Date类型的拓展方法，这个是自己添加的拓展方法，在后面的步骤3定义
		}

		function parserDate(s) {
			var t = Date.parse(s);
			if (!isNaN(t)) {
				return new Date(t);
			} else {
				return new Date();
			}
		}
		function userCallBack(userObj){
			var msg = "   用户ID："+userObj.userId +"\r\n    用户名："+userObj.userName ;
			$.messager.alert('提示',msg,'info');
		}
	</script>
</head>
<body id="myBody" class="easyui-layout" fit="true">
    <div id="toolbar" >
		<table class="toolbarTable">
			<tr>
				<sec:accesscontrollist hasPermission="3" domainObject="system_use11_add"><td><a class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addData();" href="javascript:void(0);">添加</a></td></sec:accesscontrollist>
				<sec:accesscontrollist hasPermission="3" domainObject="system_user11_save"><td><a class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="saveData();" href="javascript:void(0);">保存</a></td></sec:accesscontrollist>
				<sec:accesscontrollist hasPermission="3" domainObject="system_user11_delete"><td><a class="easyui-linkbutton" iconCls="icon-no" plain="true" onclick="deleteData();" href="javascript:void(0);">删除</a></td></sec:accesscontrollist>
				<sec:accesscontrollist hasPermission="3" domainObject="system_user11_search"><td><a class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="openSearchForm();" href="javascript:void(0);">查询</a></td></sec:accesscontrollist>
			</tr>
		</table>
	</div> 
    <div data-options="region:'center',title:'',border:'false'" style="padding:0px;">
    	<table id="dgTestPermission"
			data-options="
				rownumbers: true,
				animate: true,
				collapsible: false,
				fitColumns: true,
				autoRowHeight: false,
				checkOnSelect:true,
				url: 'platform/testpermission/getAllData.json',
				method: 'post',
				fit : true,
				idField :'id',
				toolbar:'#toolbar',
				pagination:true,
				pageSize:10,
				pageList:[10,20,30,50,100],
				striped:true
			">
		<thead>
			<tr>
				<th data-options="field:'id', halign:'center',checkbox:true" width="220">ID</th>
				<th data-options='field:"bzPeople", halign:"center"' width='220' editor='{type:"CommonSelector",options:{selectType:"user",width:220,dataGridId:"dgTestPermission",dialogShowField:"bzPeople",userId:"isNeedCar",userName:"bzPeople",selectCount:false,extParameter:"{\"secretL\":9}"}}'>选人</th>
				<th data-options="field:'bzLocation', halign:'center',editor:{type:'text'}" width="220" editor="{type:'CommonSelector',options:{selectType:'dept',width:220,dataGridId:'dgTestPermission',dialogShowField:'bzLocation',deptId:'bzByAirplane',deptName:'bzLocation',selectCount:2}}">选部门</th>
				<th data-options="field:'bzDays', halign:'center',editor:{type:'text'}" width="220" editor="{type:'CommonSelector',options:{selectType:'group',width:220,dataGridId:'dgTestPermission',dialogShowField:'bzDays',groupId:'bzByTrain',groupName:'bzDays',selectCount:1}}">选群组</th>
				<th data-options="field:'bzNeedMoneyQty', halign:'center',editor:{type:'text'}"width="220" editor="{type:'CommonSelector',options:{selectType:'role',width:220,dataGridId:'dgTestPermission',dialogShowField:'bzNeedMoneyQty',roleId:'bzResult',roleName:'bzNeedMoneyQty',selectCount:-1}}">选角色</th>
				<th data-options="field:'bzComments', halign:'center',editor:{type:'text'}" width="220" editor="{type:'CommonSelector',options:{selectType:'position',width:220,dataGridId:'dgTestPermission',dialogShowField:'bzComments',positionId:'secretLevel',positionName:'bzComments',selectCount:10}}">选岗位</th>
				<th data-options="field:'isNeedCar', halign:'center',editor:{type:'text'}" width="220">人员ID</th>
				<th data-options="field:'bzByAirplane', halign:'center',editor:{type:'text'}"width="220">部门ID</th>
				<th data-options="field:'bzByTrain', halign:'center',editor:{type:'text'}" width="220">群组ID</th>
				<th data-options="field:'bzResult', halign:'center',editor:{type:'text'}" width="220">角色ID</th>
				<th data-options="field:'secretLevel', halign:'center',editor:{type:'text'}" width="220">岗位ID</th>
			
			</tr>
		</thead>
	</table>
    </div>   
	<div id="searchDialog" class="easyui-dialog" data-options="iconCls:'icon-search',resizable:true,modal:false,buttons:'#searchBtns'" style="width: 600px;height:400px;">
		<form id="form1">
    		<table>
    			<tr>
    				<td>出差人:</td><td><input type='text' name="filter-like-BZ_PEOPLE"/></td>
    				<td>出差地点:</td><td><input type='text' name="bzLocation" /></td>
    			</tr>
    			<tr>
    				<td>出差天数:</td><td colspan="2"><input type='text' name ="bzDays" /></td>
    			</tr>
    		</table>
    	</form>
    	<div id="searchBtns">
    		<a class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="searchData();" href="javascript:void(0);">查询</a>
    		<a class="easyui-linkbutton" iconCls="icon-cancel" plain="true" onclick="clearData();" href="javascript:void(0);">清空</a>
    		<a class="easyui-linkbutton" iconCls="icon-back" plain="true" onclick="hideSearch();" href="javascript:void(0);">返回</a>
    	</div>
  </div>
</body>
</html>