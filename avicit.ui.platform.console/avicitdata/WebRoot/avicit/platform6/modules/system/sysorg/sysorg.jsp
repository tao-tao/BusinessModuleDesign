<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<%@ page import="java.util.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="sec" uri="/WEB-INF/tags/shiro.tld"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>组织管理</title>
<base href="<%=ViewUtil.getRequestPath(request)%>">
<jsp:include page="/avicit/platform6/component/common/EasyUIJsInclude.jsp"></jsp:include>
<jsp:include page="/avicit/platform6/modules/system/commonpopup/commonSelectionHead.jsp"></jsp:include>
<link href="avicit/platform6/modules/system/sysdept/css/icon.css" type="text/css" rel="stylesheet">
<script type="text/javascript">

/* var orgView = $.extend({}, $.fn.datagrid.defaults.view, {
    renderRow: function(target, fields, frozen, rowIndex, rowData) {   
        var opts = $.data(target, "datagrid").options;   
        //用于拼接字符串的数组   
        var cc = [];   
        if(frozen && opts.rownumbers) {   
            //rowIndex从0开始，而行号显示的时候是从1开始，所以这里要加1.   
            var rowNumber = rowIndex + 1;   
            //如果分页的话，根据页码和每页记录数重新设置行号   
            if(opts.pagination) {   
                rowNumber += (opts.pageNumber - 1) * opts.pageSize;   
            }   
            cc.push("<td class=\"datagrid-td-rownumber\"><div class=\"datagrid-cell-rownumber\">" + rowNumber + "</div></td>");   
        }   
        
       
        
        for(var i = 0; i < fields.length; i++) {   
            var field = fields[i];   
            var col = $(target).datagrid("getColumnOption", field);   
            if(col) {   
                var value = rowData[field];   
                //获取用户定义的单元格样式，入参包括：单元格值，当前行数据，当前行索引(从0开始)   
                var style = col.styler ? (col.styler(value, rowData, rowIndex) || "") : "";   
                //如果是隐藏列直接设置display为none，否则设置为用户想要的样式   
                var styler = col.hidden ? "style=\"display:none;" + style + "\"" : (style ? "style=\"" + style + "\"" : "");   
                cc.push("<td field=\"" + field + "\" " + styler + ">");   
                //如果当前列是datagrid组件保留的ck列时，则忽略掉用户定义的样式，即styler属性对datagrid自带的ck列是不起作用的。   
                if(col.checkbox) {   
                    var styler = "";   
                } else {   
                    var styler = "";   
                    //设置文字对齐属性   
                    if(col.align) {   
                        styler += "text-align:" + col.align + ";";   
                    }   
                    //设置文字超出td宽时是否自动换行(设置为自动换行的话会撑高单元格)   
                    if(!opts.nowrap) {   
                        styler += "white-space:normal;height:auto;";   
                    } else {   
                        if(opts.autoRowHeight) {   
                            styler += "height:auto;";   
                        }   
                    }   
                }   
                //这个地方要特别注意，前面所拼接的styler属性并不是作用于td标签上，而是作用于td下的div标签上。   
                cc.push("<div style=\"" + styler + "\" ");   
                //如果是ck列，增加"datagrid-cell-check"样式类   
                if(col.checkbox) {   
                    cc.push("class=\"datagrid-cell-check ");   
                }   
                //如果是普通列，增加"datagrid-cell-check"样式类   
                else {   
                    cc.push("class=\"datagrid-cell " + col.cellClass);   
                }   
                cc.push("\">");   
                if(col.checkbox) {   
                    cc.push("<input type=\"checkbox\" name=\"" + field + "\" value=\"" + (value != undefined ? value : "") + "\"/>");   
                }   
                //普通列   
                else {   
                    if(col.formatter) {   
                        cc.push(col.formatter(value, rowData, rowIndex));   
                    }   
                    //这是最简单的简况了，将值直接放到td>div里面。   
                    else {
                    	
                    	if(field=='code')
                    	{
                    		if(rowData['type']=="org")
                    			value="<div><span class='icon-org'></span><span class='tree-title'>"+value+"</span></div>";
                    		else if(rowData['type']=="dept")
                    			value="<div><span class='icon-dept'></span><span class='tree-title'>"+value+"</span></div>";
                    	}
                    	else if(field=='validFlag')
                    	{
                    		if(value==1)
                    			value="<span class='icon-valid'></span>";
                    		else
                    			value="<span class='icon-invalid'></span>";	
                    	}
                    	
                        cc.push(value);   
                    }   
                }   
                cc.push("</div>");   
                cc.push("</td>");   
            }   
        }   
        
        // 添加详细的编辑列
        
        if(fields.length>0)
        {
	        cc.push("<td field='' ><div style='text-align:center;' class='datagrid-cell' ");
	        
	        if(rowData['type']=="org")
	        	cc.push(" onclick='javascript: showEditOrg(\""+ rowData['id'] +"\");'");
	        else if(rowData['type']=="dept")
	        	cc.push(" onclick='javascript: showEditDept(\""+ rowData['id'] +"\");'");
	               
	        cc.push("><span class='l-btn-left'><span class='icon-edit'></span></div></td>");
        }
        //返回单元格字符串，注意这个函数内部并未把字符串放到文档流中。   
        
        //alert(cc.join(""));
        
        return cc.join("");   
    }
});  */  


var path="<%=ViewUtil.getRequestPath(request)%>";
var currTreeNode;

$(document).ready(function(){ 
	initForSearch();
});

function myOnBeforeExpand(row){
	$("#deptTree").tree("options").url = "platform/sysdept/sysDeptController/getChildOrgDeptById.json?type="+row.attributes.type;
	return true;
}




/**
 * 组织部门树单击事件
 */
function deptTreeOnClickRow(row){
	currTreeNode=row;
	if(row.attributes.type=="dept"){
		
		$("#btAddOrg").hide();
		
	}else{
		
		
		$("#btAddOrg").show();
	}
	loadSelectTabInfo(null);
}

function appendStatusIcon(data)
{
	alert(data.rows.length);
	
	for(var i=0;i<data.rows.length;i++)
	{
		data.rows[i].code="<span class='icon-org'></span>"+data.rows[i].code;
	}
}


/**
 * 加载部门信息
 */
function loadOrgDeptInfo(parentId, orgId){
	
	$("#dgOrgDept").datagrid("options").url ="platform/sysorg/sysOrgController/getSysOrgDeptList.json";
	
	if(parentId=='root' && orgId=='root')
		$('#dgOrgDept').datagrid("reload",{});
	else 
		$('#dgOrgDept').datagrid("reload", {parentId: parentId, orgId: orgId});
	
	$("#dgOrgDept").datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');		
}

/**懒加载tab页Iframe**/
function loadSelectTabInfo(title){
	
	
	if(!currTreeNode) 
		currTreeNode=$('#deptTree').tree('getRoot');
	
	if(currTreeNode.attributes.type=="dept")
	{
		var parentId=currTreeNode.id;
		var orgId=currTreeNode.attributes.ORG_ID;
		
		loadOrgDeptInfo(parentId, orgId);
	}
	else if(currTreeNode.attributes.type=="org")
	{
		var parentId=currTreeNode.id;
		var orgId=currTreeNode.id;
					
		loadOrgDeptInfo(parentId, orgId);
	}
}


/**
 *查询
**/
function initForSearch(){
 $('#searchWord').searchbox({
	 	width: 200,
        searcher: function (value) {
        	var path="platform/sysorg/sysOrgController/searchOrg";
        	if(value==null||value==""){
        		path="platform/sysdept/sysDeptController/getChildOrgDeptById";
        	}
        	$.ajax({
                cache: true,
                type: "POST",
                url:path,
                dataType:"json",
                data:{search_text:value},
                async: false,
                error: function(request) {
                	alert('操作失败，服务请求状态：'+request.status+' '+request.statusText+' 请检查服务是否可用！');
                },
                success: function(data) {
					if(data.result==0){
						//$('#deptEditForm')[0].reset();
						
						
						$('#deptTree').tree('loadData',data.data);
					}else{
						$.messager.alert('提示',data.msg,'warning');
					}
                }
            });
        },
        prompt: "请输入组织或部门名称！"
    });
}







/**
 * 刷新当前节点
 */
function updateCurrentNode(data){
	
	flushCurrentChildNodes();
	loadSelectTabInfo(null);
	
	$.messager.show({
		title : '提示',
		msg : '操作成功。',
		timeout:2000,  
        showType:'slide'  
	});
}

/**
 * 添加子节点
 */
function addChildNode(data){
		
	flushCurrentChildNodes();
	loadSelectTabInfo(null);
	
			
	$.messager.show({
		title : '提示',
		msg : '操作成功。',
		timeout:2000,  
        showType:'slide'  
	});
}


/**
 * 刷新子节点
 */


function flushCurrentChildNodes(){
	var node = currTreeNode;
	if(node){
		if(node.id=='root')
			$("#deptTree").tree('reload',null);
		else
			$("#deptTree").tree('reload',node.target);  //重新加载targer的子节点
	}
	else
	{
		$("#deptTree").tree('reload',null);
	}
}



/**
 * 加载缓存
 */
function loadCache(){
	
	var win = $.messager.progress({
		title:'请等待',
		msg:'数据加载中...'
	});
	
	$.ajax({
        cache: true,
        type: "POST",
        url:'platform/sysdept/sysDeptController/reLoadCache.json',
        dataType:"json",
        error: function(request) {
        	$.messager.progress('close');
        	$.messager.alert('提示','操作失败，服务请求状态：'+request.status+' '+request.statusText+' 请检查服务是否可用！','warning');
        },
        success: function(data) {
        	$.messager.progress('close');
			if(data.result==0){
				$.messager.show({
					title : '提示',
					msg : '加载成功！',
					timeout:2000,  
			        showType:'slide'  
				});
			}else{
				$.messager.alert('提示',data.msg,'warning');
			}
        }
    });
}

function setLanguage(){
	var rows = $('#dgOrgDept').datagrid('getChecked');
	
	if (rows.length == 1) {
		
		if(rows[0].type=="dept")
		{
			var usd = new CommonDialog("deptTlSelectDialog","800","400","platform/sysdept/sysDeptController/toLanguageListView?id="+rows[0].id,"设置多语言",null,null,false);
			usd.show();
		}
		else if(rows[0].type=="org")
		{
			var usd = new CommonDialog("orgTlSelectDialog","800","400","platform/sysorg/sysOrgController/toLanguageListView?id="+rows[0].id,"设置多语言",null,null,false);
			usd.show();
		}
	}
	else
	{
		$.messager.alert('提示',"请选择一个组织或部门！",'warning');
	}
	

	
}

function onTabSelect(title,index){
	loadSelectTabInfo(title);
}

/* 
	删除组织和部门

 */
function deleteOrgDepts()
{
	deleteMsg="";
	
	var rows = $('#dgOrgDept').datagrid('getChecked');
	//alert(rows.length);
	
	var ids = [];
	var types= [];
	var l=rows.length;
	if (rows.length > 0) {
		$.messager.confirm('请确认','您确定要删除当前所选的数据？',
			function(b){
				if(b){
					
					
					for (;l--;) {
						ids.push(rows[l].id);
						types.push(rows[l].type);
						
					}
					
					var idsStr=ids.join('@');
					var typesStr=types.join('@');
					
					$.ajax({
						 url:'platform/sysorg/sysOrgController/delete.json',
						 data : {ids : idsStr, types: typesStr},
						 type : 'post',
						 dataType : 'json',
						 success : function(data){
							 if(0==data.result){
								 $.messager.show({title : '提示',msg : '删除成功'});
								 flushCurrentChildNodes();
								 loadSelectTabInfo(null);
							 }else{
								 $.messager.alert('提示',data.msg,'warning');
							 }
							 
							
							
						 }
					 });
					
					
				}
			});
	} else {
		$.messager.alert('提示', '请选择要删除的记录！', 'warning');
	}
}






function showEditDept(id)
{
	var usd = new CommonDialog("formEditDialog","800","500",path+"avicit/platform6/modules/system/sysorg/editDeptForm.jsp?id="+id,"编辑部门");
	var buttons = [{
		text:'提交',
		id : 'formSubimt',
		//iconCls : 'icon-submit',
		handler:function(){
			 var frmId = $('#formEditDialog iframe').attr('id');
			 var frm = document.getElementById(frmId).contentWindow;
			 if(!frm.$('#deptEditForm').form("validate")){
				 $.messager.alert('提示','请完善表单中的信息','warning');
				 return;
			 }
			 $.ajax({
			        cache: true,
			        type: "POST",
			        url:'platform/sysdept/sysDeptController/saveDept',
			        dataType:"json",
			        data:frm.$('#deptEditForm').serialize(),
			        error: function(request) {
			        	$.messager.alert('提示','操作失败，服务请求状态：'+request.status+' '+request.statusText+' 请检查服务是否可用！','warning');
			        },
			        success: function(data) {
						if(data.result==0){
							if(data.opType=="update"){
								updateCurrentNode(data.sysDept);	
							}else if(data.opType="insert"){
								addChildNode(data.sysDept);
															
							}
							
							
							usd.close();
						}else{
							$.messager.alert('提示',data.msg,'warning');
						}
			        }
			    });
		}
	}];
	usd.createButtonsInDialog(buttons);
	usd.show();
}

function showAddDept()
{
	var selectNode = currTreeNode;
	if(!selectNode || selectNode.id=="root"){
		$.messager.alert('提示',"请选择组织或部门！",'warning');
		return;
	}
	var usd = new CommonDialog("formAddDialog","800","500",path+"avicit/platform6/modules/system/sysorg/addDeptForm.jsp","添加部门");
	var buttons = [{
		text:'提交',
		id : 'formSubimt',
		//iconCls : 'icon-submit',
		handler:function(){
			 var frmId = $('#formAddDialog iframe').attr('id');
			 var frm = document.getElementById(frmId).contentWindow;
			 if(!frm.$('#deptEditForm').form("validate")){
				 $.messager.alert('提示','请完善表单中的信息','warning');
				 return;
			 }
			 $.ajax({
			        cache: true,
			        type: "POST",
			        url:'platform/sysdept/sysDeptController/saveDept',
			        dataType:"json",
			        data:frm.$('#deptEditForm').serialize(),
			        error: function(request) {
			        	$.messager.alert('提示','操作失败，服务请求状态：'+request.status+' '+request.statusText+' 请检查服务是否可用！','warning');
			        },
			        success: function(data) {
						if(data.result==0){
							if(data.opType=="update"){
								updateCurrentNode(data.sysDept);	
							}else if(data.opType="insert"){
								addChildNode(data.sysDept);
															
							}
														
							usd.close();
						}else{
							$.messager.alert('提示',data.msg,'warning');
						}
			        }
			    });
		}
	}];
	usd.createButtonsInDialog(buttons);
	usd.show();
}

function showAddOrg()
{
	var selectNode = currTreeNode;
	if(!selectNode){
		$.messager.alert('提示',"请选择组织！",'warning');
		return;
	}
	var usd = new CommonDialog("formAddDialog","800","500",path+"avicit/platform6/modules/system/sysorg/addOrgForm.jsp","添加组织");
	var buttons = [{
		text:'提交',
		id : 'formSubimt',
		//iconCls : 'icon-submit',
		handler:function(){
			 var frmId = $('#formAddDialog iframe').attr('id');
			 var frm = document.getElementById(frmId).contentWindow;
			 
			 // validate the form
			 var orgForm=frm.$('#orgAddForm');
			 if(!orgForm.form('validate'))
			 {
				 $.messager.alert('提示','请完善表单中的信息','warning');
				 return;
			 }
			 
			 $.ajax({
			        cache: true,
			        type: "POST",
			        url:'platform/sysorg/sysOrgController/saveSysOrg',
			        dataType:"json",
			        data:frm.$('#orgAddForm').serialize(),
			        error: function(request) {
			        	$.messager.alert('提示','操作失败，服务请求状态：'+request.status+' '+request.statusText+' 请检查服务是否可用！','warning');
			        },
			        success: function(data) {
						if(data.result==0){
							if(data.opType=="update"){
								updateCurrentNode(data.sysOrg);	
							}else if(data.opType="insert"){
								addChildNode(data.sysOrg);
															
							}
														
							usd.close();
						}else{
							$.messager.alert('提示',data.msg,'warning');
						}
			        }
			    });
		}
	}];
	usd.createButtonsInDialog(buttons);
	usd.show();
}

function showEditOrg(id)
{
	var usd = new CommonDialog("formEditDialog","800","400",path+"avicit/platform6/modules/system/sysorg/editOrgForm.jsp?id="+id,"编辑组织");
	var buttons = [{
		text:'提交',
		id : 'formSubimt',
		//iconCls : 'icon-submit',
		handler:function(){
			 var frmId = $('#formEditDialog iframe').attr('id');
			 var frm = document.getElementById(frmId).contentWindow;
			 
			// validate the form
			 var orgForm=frm.$('#orgEditForm');
			 if(!orgForm.form('validate'))
			 {
				 $.messager.alert('提示','请完善表单中的信息','warning');
				 return;
			 }
			 
			 $.ajax({
			        cache: true,
			        type: "POST",
			        url:'platform/sysorg/sysOrgController/saveSysOrg',
			        dataType:"json",
			        data:frm.$('#orgEditForm').serialize(),
			        error: function(request) {
			        	$.messager.alert('提示','操作失败，服务请求状态：'+request.status+' '+request.statusText+' 请检查服务是否可用！','warning');
			        },
			        success: function(data) {
						if(data.result==0){
							if(data.opType=="update"){
								updateCurrentNode(data.sysOrg);	
							}else if(data.opType="insert"){
								addChildNode(data.sysOrg);
															
							}
														
							usd.close();
						}else{
							$.messager.alert('提示',data.msg,'warning');
						}
			        }
			    });
		}
	}];
	usd.createButtonsInDialog(buttons);
	usd.show();
}


function setValidFlag(){
	var rows = $("#dgOrgDept").datagrid('getChecked');
	var ids = [];
	if (rows.length == 1) {
		$.messager.confirm('请确认','您确定执行该操作？',function(b){
			if(b){
				var rowData = rows[0];
			   $.ajax({
				 url:'platform/sysorg/sysOrgController/setValidFlag',
				 data : {id : rows[0].id, type: rows[0].type},
				 type : 'post',
				 dataType : 'json',
				 success : function(data){
					 if(0==data.result){
						 //loadUserData(queryId,queryType,queryName);
						 $("#dgOrgDept").datagrid('reload');
						 $("#dgOrgDept").datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
						 $.messager.show({title : '提示',msg : '操作成功'});
						 //是否将部门下的人员置为无效
						 if(rowData.type == "dept" && rowData.validFlag == "1"){
							 $.messager.confirm('请确认','是否将部门下的人员置为无效？',function(_flg){
								 if(_flg){
									 $.ajax({
										 url:'platform/sysorg/sysOrgController/setUserValidByDept',
										 data : {id : rowData.id},
										 type : 'post',
										 dataType : 'json',
										 success : function(_data){
											 if(0==_data.result){
												 $.messager.show({title : '提示',msg : '操作成功'});
											 }else{
												 $.messager.alert('提示',_data.msg,'warning');
											 }
										 }
									 });
								 }
							 });
						 }
					 }else{
						 $.messager.alert('提示',data.msg,'warning');
					 }
				 }
			 });
			}
		});
	}else{
		$.messager.alert('提示',"请选择一个组织或部门！",'warning');
	}
};

function importOrg()
{
	var imp = new CommonDialog("importData","700","400",'platform/excelImportController/excelimport/importOrgInfo/xls',"Excel数据导入",false,true,false);
	imp.show();
};

function importDept()
{
	var imp = new CommonDialog("importData","700","400",'platform/excelImportController/excelimport/importDeptInfo/xls',"Excel数据导入",false,true,false);
	imp.show();
};


function closeImportData(){
	$("#importData").dialog('close');
};

var _defaultOpenRoot_flg = false;
function defaultOpenRoot(node, data){
	if(_defaultOpenRoot_flg){
		return;
	}
	$("#deptTree").tree("expandAll");
	_defaultOpenRoot_flg = true;
};

function formatStatus(value,rowData,rowIndex){
	if(value==1)
		return "<span class='icon-valid'></span>";
	else
		return "<span class='icon-invalid'></span>";	
};

function formatCode(value,rowData,rowIndex){
	if(rowData['type']=="org")
		return "<div><span class='icon-org'></span><span class='tree-title'>"+value+"</span></div>";
	else if(rowData['type']=="dept")
		return "<div><span class='icon-dept'></span><span class='tree-title'>"+value+"</span></div>";
};
function formatEdit(value,rowData,rowIndex){
	 if(rowData['type']=="org"){
		 return "<span class='icon-edit' onclick='javascript: showEditOrg(\""+ rowData.id +"\");'></span>";
	 }else{
		 return "<span class='icon-edit' onclick='javascript: showEditDept(\""+ rowData.id +"\");'></span>";
	 }
};
</script>
</head>

<body class="easyui-layout" fit="true">
<div data-options="region:'west',split:true,title:''" style="width:250px;padding:0px;">
	 <div id="toolbar" class="datagrid-toolbar">
	 	<table width="100%">
	 		<tr>
	 			<td width="100%"><input  type="text"  name="searchWord" id="searchWord"></input></td>
	 		</tr>
	 	</table>
	 </div>
	<ul id="deptTree" class="easyui-tree" data-options="
			onLoadSuccess:defaultOpenRoot,
			url:'platform/sysdept/sysDeptController/getChildOrgDeptById.json',
			loadFilter: function(data){
	            if (data.data){	
	                return data.data;
	            } else {
	                return data;
	            }
       		},
       		lines:true,
       		dataType:'json',
       		animate:true,
       		onBeforeExpand:myOnBeforeExpand,
       		onClick:deptTreeOnClickRow">数据加载中...</ul>
</div>
<div data-options="region:'center',split:true,title:''" style="padding:0px;">
	
	<div id="toolbarOrg" class="datagrid-toolbar">
			<a id="btAddOrg"  class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="showAddOrg();" href="javascript:void(0);">添加组织</a>
			<a id="btAddDept"  class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="showAddDept();" href="javascript:void(0);">添加部门</a>
			<a id="btSetValid" class="easyui-linkbutton" iconCls="icon-my-file"  plain="true" onclick="setValidFlag();" href="javascript:void(0);">有(无)效设置</a>
			<!-- <a id="btLanguage" class="easyui-linkbutton" iconCls="icon-my-file"  plain="true" onclick="setLanguage();" href="javascript:void(0);">多语言设置</a> -->
			<a id="btImportOrg" class="easyui-linkbutton" iconCls="icon-import"  plain="true" onclick="importOrg();" href="javascript:void(0);">组织导入</a>
			<a id="btImportDept" class="easyui-linkbutton" iconCls="icon-import"  plain="true" onclick="importDept();" href="javascript:void(0);">部门导入</a>
			<a id="btDeleteOrgDept" class="easyui-linkbutton" iconCls="icon-remove"  plain="true" onclick="deleteOrgDepts();" href="javascript:void(0);">删除</a>
		
	</div>
	
	<table id="dgOrgDept" class="easyui-datagrid"
		data-options=" 
			fit: true,
			border: false,
			rownumbers: true,
			animate: true,
			collapsible: false,
			fitColumns: true,
			autoRowHeight: false,
			toolbar:'#toolbarOrg',
			idField :'id',
			singleSelect: true,
			checkOnSelect: true,
			selectOnCheck: false,
			striped:true,
			url:'platform/sysorg/sysOrgController/getSysOrgDeptList.json'
			">
		<thead>
			<tr>
				<th data-options="field:'id', halign:'center',checkbox:true" width="220">id</th>
				<th data-options="field:'validFlag', halign:'center',align:'center',formatter:formatStatus" >状态</th>
				<th data-options="field:'code',required:true,halign:'center',align:'left',formatter:formatCode" width="220">代码</th>
				<th data-options="field:'name',required:true,halign:'center',align:'center'" width="220">名称</th>
				<th data-options="field:'tel',required:true,halign:'center',align:'left'" width="220">电话</th>
				<th data-options="field:'fax',halign:'center',align:'left'" width="220">传真</th>
				<th data-options="field:'email',halign:'center',align:'left'" width="220">邮箱</th>
				<th data-options="field:'place',halign:'center',align:'left'"  width="220">办公地点</th>
				<th data-options="field:'orderBy',halign:'center',align:'left'" width="220">排序</th>
				<th data-options="field:'_d',halign:'center',align:'center',formatter:formatEdit"  width="60">编辑</th>
			</tr>
		</thead>
	</table>
</div>
</body>
</html>