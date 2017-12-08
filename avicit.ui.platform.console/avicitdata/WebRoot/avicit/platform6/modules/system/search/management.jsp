<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>系统列表</title>
<base href="<%=ViewUtil.getRequestPath(request)%>">
<jsp:include page="/avicit/platform6/component/common/EasyUIJsInclude.jsp"></jsp:include>
<jsp:include page="/avicit/platform6/modules/system/commonpopup/commonSelectionHead.jsp"></jsp:include>
</head>

<body class="easyui-layout"  fit="true">
<div data-options="region:'center',split:true,border:false" style="padding:0px;overflow:hidden;">
	<table id="dg"  class="easyui-datagrid"  url="platform/search/list.json" toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="true" singleSelect="true" striped="true">
		<thead>
			<tr>
				<th data-options="field:'id', halign:'center',checkbox:true" width="20">id</th>
				<th data-options="field:'datasourceName',required:true,align:'center'"  width="20">数据源名称</th>
				<th data-options="field:'displayName',required:true,align:'center'"  width="20">显示名称</th>
				<th data-options="field:'type',required:true,align:'center'"  width="20" formatter="formatType">数据源类型</th>
				<th data-options="field:'datasourceDesc',required:true,align:'center'"  width="70">简要描述</th>
				<th data-options="field:'sqlStatement',required:true,align:'center'"  width="70">SQL</th>
				<th data-options="field:'status',required:true,align:'center'"  width="20" formatter="formatStatus">是否有效</th>
			</tr>
		</thead>
	</table>
	<!-- CRUD工具栏 -->
	<div id="toolbar">
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newDb()">添加数据库数据源</a> 
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newDoc()">添加文档数据源</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editDs()">编辑</a> 
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="setOk()">设置为有效</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="setCancel()">设置为无效</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="buildIndex()">重建索引</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="deleteDs()">删除</a>
	</div>
	
	<!-- CU表单 -->
	<div id="dlg" class="easyui-dialog" style="width: 500px; height: 760px; padding: 10px 20px" closed="true" buttons="#dlg-buttons">
		<form id="fm" method="post" novalidate>
				<div class="fitem"><label>数据源名称:</label> <input name="datasourceName" class="easyui-textbox" required="true"></div>
				<div class="fitem"><label>显示名称:</label> <input name="displayName" class="easyui-textbox" required="true"></div>
			
			<div class="fitem">
				<label>数据库连接:</label> 
				<select id="orgId" name="connectionFk" class="easyui-combobox"   data-options="valueField:'id', textField:'connectionName', width:166,editable:false,panelHeight:'auto'"></select>
			</div>	
				
			<div class="fitem">
				<label>权限控制:</label> 
				<select id="authId" name="sysroleFk" class="easyui-combobox"   data-options="valueField:'id', textField:'roleName', width:166,editable:false,panelHeight:'auto'"></select>
			</div>		
				
				
			<div class="fitem">
				<label>有效标识:</label> 
				<select name="status" class="easyui-combobox" data-options="width:166,editable:false,panelHeight:'auto'">
								<option value="1">有效</option>
								<option value="0">无效</option>
				</select>
			</div>
				<div class="fitem"><label>简要描述:</label> <input name="datasourceDesc" class="easyui-textbox"  data-options="multiline:true" style="height:60px" required="true"></div>
				<div class="fitem"><label>SQL语句:</label> <input name="sqlStatement" class="easyui-textbox"  data-options="multiline:true" style="height:60px" required="true"></div>
				<div class="fitem"><label>展示路径:</label> <input name="displayUrl" class="easyui-textbox"  data-options="multiline:true" style="height:60px" required="true"></div>
		</form>
	</div>
	<div id="dlg-buttons">
		<a href="javascript:void(0)" class="easyui-linkbutton" onclick="saveDb()" >保存</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" onclick="javascript:$('#dlg').dialog('close')" >返回</a>
	</div>
	
	
	
	
	
	<!-- CU表单 -->
	<div id="dlgdoc" class="easyui-dialog" style="width: 450px; height: 460px; padding: 10px 20px" closed="true" buttons="#dlgdoc-buttons">
		<form id="fmdoc" method="post" novalidate>
				<div class="fitem"><label>数据源名称:</label> <input name="datasourceName" class="easyui-textbox" required="true"></div>
				<div class="fitem"><label>显示名称:</label> <input name="displayName" class="easyui-textbox" required="true"></div>
				
			<div class="fitem">
				<label>权限控制:</label> 
				<select id="authdocId" name="sysroleFk" class="easyui-combobox"   data-options="valueField:'id', textField:'roleName', width:166,editable:false,panelHeight:'auto'"></select>
			</div>		
				
				
			<div class="fitem">
				<label>有效标识:</label> 
				<select name="status" class="easyui-combobox" data-options="width:166,editable:false,panelHeight:'auto'">
								<option value="1">有效</option>
								<option value="0">无效</option>
				</select>
			</div>
				<div class="fitem"><label>简要描述:</label> <input name="datasourceDesc" class="easyui-textbox"  data-options="multiline:true" style="height:60px" required="true"></div>
				<div class="fitem"><label>文档路径:</label> <input name="docPath" class="easyui-textbox"  data-options="multiline:true" style="height:60px" required="true"></div>
		</form>
	</div>
	<div id="dlgdoc-buttons">
		<a href="javascript:void(0)" class="easyui-linkbutton" onclick="saveDoc()" >保存</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" onclick="javascript:$('#dlgdoc').dialog('close')" >返回</a>
	</div>
	
	<br>
	
	 <div class="easyui-panel" title="字段设置" style="height:400px;padding:10px;">
			<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'west'" style="width:500px;padding:10px">
					<table id="dguser"  class="easyui-datagrid"  url="" toolbar="" pagination="false" rownumbers="true" fitColumns="true" singleSelect="true">
						<thead>
							<tr>
								<th data-options="field:'id', halign:'center',checkbox:true" width="50">id</th>
								<th data-options="field:'columnName',required:true,align:'center'"  width="100">字段名</th>
								<th data-options="field:'columnType',required:true,align:'center'"  width="100" >字段类型</th>
							</tr>
						</thead>
					</table>
			
			</div>
			<div data-options="region:'east'" style="width:700px;padding:10px">
				  <table id="dgindex"  class="easyui-datagrid"  url="" toolbar="" pagination="false" rownumbers="true" fitColumns="true" singleSelect="true">
					<thead>
						<tr>
							<th data-options="field:'id', halign:'center',checkbox:true" width="50">id</th>
							<th data-options="field:'columnName',required:true,align:'center'"  width="150">字段名</th>
							<th data-options="field:'columnType',required:true,align:'center'"  width="100">字段类型</th>
							<th data-options="field:'indexed',required:true,align:'center'"  width="50">是否索引</th>
							<th data-options="field:'stored',required:true,align:'center'"  width="50" >是否存储</th>
							<th data-options="field:'belong',required:true,align:'center'"  width="50" >归属</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>
	
	
	</div>
	<!-- button js event -->
	<script type="text/javascript">
	
	var op_indexed = [
	    		    {indexedid:'1',indexedname:'是'},
	    		    {indexedid:'0',indexedname:'否'}
	    		];
	
	var op_sorted = [
	    		    {sortedid:'1',sortedname:'是'},
	    		    {sortedid:'0',sortedname:'否'}
	    		];
	
	var op_belong = [
		    		    {belongid:'0',belongname:'无'},
		    		    {belongid:'1',belongname:'标题'},
		    		    {belongid:'2',belongname:'正文'}
		    		];
	
	$(function(){
		$('#dgindex').datagrid({
			columns:[[
				{field:'id',checkbox:true},
				{field:'columnName',title:'字段名称',width:60},
				{field:'indexed',title:'是否索引',width:100,
					formatter:function(value){
						for(var i=0; i<op_indexed.length; i++){
							if (op_indexed[i].indexedid == value) return op_indexed[i].indexedname;
						}
						return value;
					},
					editor:{
						type:'combobox',
						options:{
							valueField:'indexedid',
							textField:'indexedname',
							data:op_indexed,
							required:true
						}
					}
				},
				{field:'stored',title:'是否存储',width:100,
					formatter:function(value){
						for(var i=0; i<op_sorted.length; i++){
							if (op_sorted[i].sortedid == value) return op_sorted[i].sortedname;
						}
						return value;
					},
					editor:{
						type:'combobox',
						options:{
							valueField:'sortedid',
							textField:'sortedname',
							data:op_sorted,
							required:true
						}
					}
				},
				{field:'belong',title:'归属',width:100,
					formatter:function(value){
						for(var i=0; i<op_belong.length; i++){
							if (op_belong[i].belongid == value) return op_belong[i].belongname;
						}
						return value;
					},
					editor:{
						type:'combobox',
						options:{
							valueField:'belongid',
							textField:'belongname',
							data:op_belong,
							required:true
						}
					}
				},
				{field:'action',title:'操作',width:80,align:'center',
					formatter:function(value,row,index){
						if (row.editing){
							var s = "<a href='javascript:void(0)'  onclick=saverow('"+row.id+"',"+index+")>保存</a> ";
							var c = '<a href="javascript:void(0)" onclick="cancelrow(this)">放弃</a>';
							return s+c;
						} else {
							var e = '<a href="javascript:void(0)" onclick="editrow(this)">编辑</a> ';
							var d = "<a href='javascript:void(0)' onclick=deleterow('"+row.id+"',"+index+")>删除</a>";
							return e+d;
						}
					}
				}
			]],
			onBeforeEdit:function(index,row){
				row.editing = true;
				updateActions(index);
			},
			onAfterEdit:function(index,row){
				row.editing = false;
				updateActions(index);
			},
			onCancelEdit:function(index,row){
				row.editing = false;
				updateActions(index);
			}
		});
	});
	function updateActions(index){
		$('#dgindex').datagrid('updateRow',{
			index: index,
			row:{}
		});
	}
	function getRowIndex(target){
		var tr = $(target).closest('tr.datagrid-row');
		return parseInt(tr.attr('datagrid-row-index'));
	}
	function editrow(target){
		$('#dgindex').datagrid('beginEdit', getRowIndex(target));
	}
	function deleterow(id_,index_){
		if(id_=="undefined"){
			$('#dgindex').datagrid('deleteRow', index_);
		}else{
		
				url='platform/search/delete_detail.html';
				$.messager.confirm('','确认删除吗?',function(r){
					if (r){
						$.post(url, {
							id : id_
						}, function(result) {
							if (result.success) {
								$('#dgindex').datagrid('deleteRow', index_);
								url = 'platform/search/listDetails.html';
			    				$.post(url, {
			    					id:mainId
			    				}, function(result) {
			    					$('#dgindex').datagrid('loadData',result);
			    				}, 'json');
							} else {
								$.messager.show({
									title : 'Error',
									msg : result.errorMsg
								});
							}
						}, 'json');
					}
				});
		}
	}
	
	function saverow(id_,index_){
		$('#dgindex').datagrid('endEdit', index_);
		url='platform/search/save_detail.html';
		var row=$('#dg').datagrid('getSelected');
		var mainId=row.id;
			var temp=$('#dgindex').datagrid('getRows');
			var rowdata=temp[index_];
			if (rowdata){
				if(id_=="undefined"){
					$.post(url, {
						columnName : rowdata.columnName,
						indexed : rowdata.indexed,
						maindataId : mainId,
						stored : rowdata.stored,
						belong:rowdata.belong
					}, function(result) {
						if (result.success) {
							 $.messager.show({title:'提示',msg:'保存成功!',timeout:5000,showType:'slide'});
						} else {
							$.messager.show({title : 'Error',msg : result.errorMsg});
						}
					}, 'json');
					
				}else{
					$.post(url, {
						id : id_,
						columnName : rowdata.columnName,
						indexed : rowdata.indexed,
						maindataId : mainId,
						stored : rowdata.stored,
						belong:rowdata.belong
					}, function(result) {
						if (result.success) {
							 $.messager.show({title:'提示',msg:'保存成功!',timeout:5000,showType:'slide'});
						} else {
							$.messager.show({title : 'Error',msg : result.errorMsg});
						}
					}, 'json');
					
				}
			}
		
		$('#dgindex').datagrid('endEdit', index_);
	}
	function cancelrow(target){
		$('#dgindex').datagrid('cancelEdit', getRowIndex(target));
	}

	
	
		function formatStatus(val,row){    
		    if (val ==1){    
		        return '有效';    
		    } else {    
		        return '无效';    
		    }    
		} 
		
		function formatDetail(val,row){    
		    if (val ==1){    
		        return '是';    
		    } else {    
		        return '否';    
		    }    
		}
		
		function formatType(val,row){    
		    if (val ==1){    
		        return '数据库数据源';    
		    } else {    
		        return '文档数据源';    
		    }    
		}
		
		
		$('#dg').datagrid({
        	onClickRow:function(index,data)
        	{
        		var row=$('#dg').datagrid('getSelected');
        		if(row.type==1){
        			url = 'platform/search/listDetails.html';
    				$.post(url, {
    					id:row.id
    				}, function(result) {
    					$('#dgindex').datagrid('loadData',result);
    				}, 'json');
    				
    				url = 'platform/search/listColumns.html';
    				$.post(url, {
    					id:row.id
    				}, function(result) {
    					$('#dguser').datagrid('loadData',result);
    				}, 'json');
    				
        		}else{
        			
        			$('#dgindex').datagrid('loadData',{total: 0, rows: [] });
        			$('#dguser').datagrid('loadData',{total: 0, rows: [] });
        			
        		}
        			
        	}
        })
        
        
        $('#dguser').datagrid({
        	onDblClickRow:function(index,data)
        	{
        		var row=$('#dguser').datagrid('getSelected');
        		if(row){
        			var temp=$('#dgindex').datagrid('getRows');
        			var flag=true;
        			for(var i=0;i<temp.length;i++){
        				if(temp[i].columnName==row.columnName){
        					$.messager.alert('警告',row.columnName+'已存在!','error');
        					flag=false;
        				}
        			}
        			
        			if(flag){
        				$('#dgindex').datagrid('appendRow',{
            				columnName: row.columnName,
            				columnType:row.columnType,
            				indexed: '0',
            				stored:'0',
            				belong:'0'
            			});
        			}
        			
        		}
        	}
        })
        
        
        
	
		var url;
		
		function newDb() {
			$('#dlg').dialog('open').dialog('setTitle', '新数据库数据源');
			
			$.post('platform/search/connection/listAll.html', {
			}, function(result) {
				$("#orgId").combobox("loadData", result);
			}, 'json');
			
			$.post('platform/search/connection/listAuth.html', {
			}, function(result) {
				$("#authId").combobox("loadData", result);
			}, 'json');
			
			$('#fm').form('clear');
			url = 'platform/search/adddb.html';
		}
		
		
		function newDoc() {
			$('#dlgdoc').dialog('open').dialog('setTitle', '新文档数据源');
			
			$.post('platform/search/connection/listAuth.html', {
			}, function(result) {
				$("#authdocId").combobox("loadData", result);
			}, 'json');
			
			$('#fmdoc').form('clear');
			url = 'platform/search/adddoc.html';
		}
		
		
		function editDs() {
			var row = $('#dg').datagrid('getSelected');
			if (row) {
				if(row.type=='1'){
					$('#dlg').dialog('open').dialog('setTitle', '编辑数据库数据源');
					$.post('platform/search/connection/listAll.html', {
					}, function(result) {
						$("#orgId").combobox("loadData", result);
					}, 'json');
					
					$.post('platform/search/connection/listAuth.html', {
					}, function(result) {
						$("#authId").combobox("loadData", result);
					}, 'json');
					
					$('#fm').form('clear');
					$('#fm').form('load', row);
					url = 'platform/search/editdb.html?id=' + row.id;
				}
				
				if(row.type=='0'){
					$('#dlgdoc').dialog('open').dialog('setTitle', '编辑文档数据源');
					
					$.post('platform/search/connection/listAuth.html', {
					}, function(result) {
						$("#authdocId").combobox("loadData", result);
					}, 'json');
					
					$('#fmdoc').form('clear');
					$('#fmdoc').form('load', row);
					url = 'platform/search/editdoc.html?id=' + row.id;
				}
			}
		}
		
		
		function setOk() {
			url='platform/search/set.html';
			var row = $('#dg').datagrid('getSelected');
			if (row) {
				$.messager.confirm('',
						'确认设置吗?',
						function(r) {
							if (r) {
								$.post(url, {
									id : row.id,
									status:'1'
								}, function(result) {
									if (result.success) {
										$('#dg').datagrid('reload');
									} else {
										$.messager.show({
											title : 'Error',
											msg : result.errorMsg
										});
									}
								}, 'json');
							}
				});
			}
		}
		
		
		function setCancel() {
			url='platform/search/set.html';
			var row = $('#dg').datagrid('getSelected');
			if (row) {
				$.messager.confirm('',
						'确认设置吗?',
						function(r) {
							if (r) {
								$.post(url, {
									id : row.id,
									status:'0'
								}, function(result) {
									if (result.success) {
										$('#dg').datagrid('reload');
									} else {
										$.messager.show({
											title : 'Error',
											msg : result.errorMsg
										});
									}
								}, 'json');
							}
				});
			}
		}
		
		
		function saveDb() {
			$('#fm').form('submit', {
				url : url,
				onSubmit : function() {
					return $(this).form('validate');
				},
				success : function(result) {
					var result = eval('(' + result + ')');
					if (result.errorMsg) {
						$.messager.show({
							title : 'Error',
							msg : result.errorMsg
						});
					} else {
						$('#dlg').dialog('close'); 
						$('#dg').datagrid('reload'); 
					}
				}
			});
		}
		
		
		function saveDoc() {
			$('#fmdoc').form('submit', {
				url : url,
				onSubmit : function() {
					return $(this).form('validate');
				},
				success : function(result) {
					var result = eval('(' + result + ')');
					if (result.errorMsg) {
						$.messager.show({
							title : 'Error',
							msg : result.errorMsg
						});
					} else {
						$('#dlgdoc').dialog('close'); 
						$('#dg').datagrid('reload'); 
					}
				}
			});
		}
		
		function deleteDs() {
			url='platform/search/delete.html';
			var row = $('#dg').datagrid('getSelected');
			if (row) {
				$.messager.confirm('',
						'确认删除吗?',
						function(r) {
							if (r) {
								$.post(url, {
									id : row.id
								}, function(result) {
									if (result.success) {
										$('#dg').datagrid('reload');
									} else {
										$.messager.show({
											title : 'Error',
											msg : result.errorMsg
										});
									}
								}, 'json');
							}
				});
			}
		}
		
		
		
		function buildIndex() {
			url='platform/search/bulidIndex.html';
				$.messager.confirm('',
						'确认重建索引吗?',
						function(r) {
							if (r) {
								$.post(url, {}, function(result) {
									if (result.success) {
										 $.messager.show({title:'提示',msg:'重建成功!耗时'+result.msg+'ms',timeout:5000,showType:'slide'});
									} else {
										$.messager.show({title : 'Error',msg : result.errorMsg});
									}
								}, 'json');
							}
				});
		}
		
		
	</script>
	
	<style type="text/css">
#fm {
	margin: 0;
	padding: 10px 30px;
}

.ftitle {
	font-size: 14px;
	font-weight: bold;
	padding: 5px 0;
	margin-bottom: 10px;
	border-bottom: 1px solid #ccc;
}

.fitem {
	margin-bottom: 5px;
}

.fitem label {
	display: inline-block;
	width: 80px;
}

.fitem input {
	width: 260px;
}
</style>
</body>
</html>