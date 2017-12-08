<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>系统代码维护</title>
<base href="<%=ViewUtil.getRequestPath(request)%>">
<jsp:include page="/avicit/platform6/component/common/EasyUIJsInclude.jsp"></jsp:include>
</head>
<body class="easyui-layout" fit="true">
	<div data-options="region:'center',title:'通用代码列表'" style="background:#ffffff;height:0px;padding:0px;overflow:hidden;">
		<div id="toolbarSysLookupType" class="datagrid-toolbar">
		 	<table>
		 		<tr>
					<td><a class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="sysLookupType.insert();" href="javascript:void(0);">添加</a></td>
					<td><a class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="sysLookupType.modify();" href="javascript:void(0);">编辑</a></td>
					<td><a class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="sysLookupType.del();" href="javascript:void(0);">删除</a></td>
					<td><a class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="sysLookupType.openSearchForm();" href="javascript:void(0);">查询</a></td>
				</tr>
		 	</table>
	 	</div>
	 	<table id="dgLookupType"
			data-options="
				fit: true,
				border: false,
				rownumbers: true,
				animate: true,
				collapsible: false,
				fitColumns: true,
				autoRowHeight: false,
				toolbar:'#toolbarSysLookupType',
				idField :'id',
				singleSelect: true,
				checkOnSelect: true,
				selectOnCheck: false,
				pagination:true,
				pageSize:dataOptions.pageSize,
				pageList:dataOptions.pageList,
				striped:true">
			<thead>
				<tr>
					<th data-options="field:'id', halign:'center',checkbox:true" width="220">id</th>
					<th data-options="field:'sid', halign:'center',hidden:true" width="220">sid</th><!-- ,hidden:true -->
					<th data-options="field:'lookupType', required:true,align:'center'" width="220">系统代码类型</th>
					<th data-options="field:'lookupTypeName',required:true,align:'center'" width="220">系统代码类型名称</th>
					<th data-options="field:'systemFlag',hidden:true,align:'center',align:'center',formatter:formatLevel"  width="120">级别</th>
					<th data-options="field:'usageModifier',align:'center',align:'center',formatter:formatModifier"  width="120">使用级别</th>
					<th data-options="field:'validFlag',align:'center',align:'center',formatter:formatValid"  width="120">有效标识</th>
					<th data-options="field:'description',align:'center',align:'center'"  width="220">描述</th>
				</tr>
			</thead>
		</table>
	</div>
	<div data-options="region:'east',title:'通用代码值'" style="width:550px;background:#f5fafe;">
		<div id="toolbarSysLookup" class="datagrid-toolbar">
		 	<table>
		 		<tr>
					<td><a class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="sysLookup.insert();" href="javascript:void(0);">添加</a></td>
					<td><a class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="sysLookup.save(sysLookupType._selectId);" href="javascript:void(0);">保存</a></td>
					<td><a class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="sysLookup.del();" href="javascript:void(0);">删除</a></td>
					<!-- <td><a class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="sysLookup.openLanguageForm();" href="javascript:void(0);">语言设置</a></td> -->
				</tr>
		 	</table>
	 	</div>
		<table id='dgLookup' class="easyui-datagrid"
       	 		data-options="
        		fit: true,
				border: false,
				rownumbers: true,
				animate: true,
				collapsible: false,
				fitColumns: true,
				autoRowHeight: false,
				idField :'id',
				toolbar:'#toolbarSysLookup',
				singleSelect: true,
				checkOnSelect: true,
				selectOnCheck: false,
				method:'get',
				striped:true">
		    <thead>   
		        <tr> 
		        	<th data-options="field:'id', halign:'center',checkbox:true" width="220">id</th>
		        	<th data-options="field:'sid', halign:'center',hidden:true" width="220">sid</th>
		        	<th data-options="field:'lookupName',align:'center'" editor="{type:'text'}" width="320"><span style="color:red;">*</span>系统代码名称</th>
		        	<th data-options="field:'lookupCode',align:'center'" editor="{type:'text'}" width="220"><span style="color:red;">*</span>系统代码值</th>
					<th data-options="field:'validFlag',align:'center',formatter:formatLookupValid" 
						editor="{type:'combobox',options:{panelHeight:'auto',editable:false,valueField:'lookupCode',textField:'lookupName',onHidePanel:function(){sysLookup.endEditing();}}}" width="220">有效标识</th>
					<th data-options="field:'displayOrder',align:'center'" editor="{type:'numberbox'}" width="120"><span style="color:red;">*</span>排序</th>
					<th data-options="field:'description',align:'center'" editor="{type:'text'}" width="220">描述</th>
		        </tr>   
		    </thead>   
		</table>  
	</div>
	<!--*****************************搜索*********************************  -->
	<div id="searchDialog" data-options="iconCls:'icon-search',resizable:true,modal:false,buttons:'#searchBtns'" style="width: 600px;height:200px;display: none;">
		<form id="form1">
    		<table style="padding-top: 10px;">
    			<tr>
    				<td>系统代码类型:</td>
    				<td><input class="easyui-validatebox"  style="width: 151px;" type="text" name="filter-LIKE-LOOKUP_TYPE"/></td>
    				<td>系统代码类型名称:</td>
    				<td><input class="easyui-validatebox" style="width: 151px;" type="text" name="filter-LIKE-LOOKUP_TYPE_NAME"/></td>
    			</tr>
    			<tr>
    				<td>有效标识:</td>
    				<!-- <td><input class="easyui-validatebox" type="text" name="filter-LIKE-VALID_FLAG"/></td> -->
    				<td>
    					<div style="padding-left: 0px;"><select name="filter-EQ-VALID_FLAG" class="easyui-combobox" data-options="width:152,editable:false,panelHeight:'auto'">
							<c:forEach items="${validFlag}" var="validFlag">
								<option value="${validFlag.lookupCode}">${validFlag.lookupName}</option>
							</c:forEach>
							</select>
						</div>
    				</td>
    			</tr>
    		</table>
    	</form>
    	<div id="searchBtns">
    		<a class="easyui-linkbutton" plain="false" onclick="sysLookupType.searchData();" href="javascript:void(0);">查询</a>
    		<a class="easyui-linkbutton" plain="false" onclick="sysLookupType.clearData();" href="javascript:void(0);">清空</a>
    		<a class="easyui-linkbutton" plain="false" onclick="sysLookupType.hideSearchForm();" href="javascript:void(0);">返回</a>
    	</div>
  </div>
	<script src="avicit/platform6/modules/system/syslookuptype/js/SysLookupType.js" type="text/javascript"></script>
	<script src="avicit/platform6/modules/system/syslookuptype/js/SysLookup.js" type="text/javascript"></script>
	<script type="text/javascript">
 		var sysLookupType;
		var sysLookup;
		$(function(){
			sysLookupType= new SysLookupType('dgLookupType','${url}','searchDialog','form1');
			sysLookupType.setOnLoadSuccess(function(){
				sysLookup = new SysLookup('dgLookup','${url}'); 
			});
			sysLookupType.setOnSelectRow(function(rowIndex, rowData,id){
				sysLookup.loadById(id);  
			});
			
			sysLookupType.init();
		});
		function reload1(){
			sysLookupType.reLoad();
		};
		function reload2(){
			sysLookup.reLoad();
		};
		function closeL(){
			//'#chooseL'
			sysLookupType.closeLdialog('#chooseL');
		};
		
		
		//格式化信息
		function formatValid(value){
			return sysLookupType.formatValid(value);
		};
		//格式化信息
		function formatLevel(value){
			return sysLookupType.formatLevel(value);
		};
		
		function formatModifier(value){
			return sysLookupType.formatModifier(value);
		};
		//格式化lookup
		function formatLookupValid(value){
			return sysLookup.formatValid(value);
		};
		/* function formatLevel(value){
			return sysProVal.formatLevelCode(value);
		} */
	</script>
</body>
</html>