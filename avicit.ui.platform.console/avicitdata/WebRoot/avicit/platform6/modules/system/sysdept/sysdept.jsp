<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<%@ page import="java.util.*"%>
<%@ page import="avicit.platform6.core.spring.SpringFactory"%>
<%@ page import="avicit.platform6.api.syslookup.SysLookupAPI"%>
<%@ page import="avicit.platform6.api.syslookup.dto.SysLookupSimpleVo"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="sec" uri="/WEB-INF/tags/shiro.tld"%>
<%
	SysLookupAPI upLoader = SpringFactory.getBean(SysLookupAPI.class);
	Collection<SysLookupSimpleVo> lookUpdeptType = upLoader.getLookUpListByType("PLATFORM_DEPT_TYPE");//修改平台，转换为通用代码，部门类型
	Collection<SysLookupSimpleVo> lookUpYesNoFlag = upLoader.getLookUpListByType("PLATFORM_YES_NO_FLAG");//修改平台，转换为通用代码，是否PMO
	Collection<SysLookupSimpleVo> lookUpValidFlag = upLoader.getLookUpListByType("PLATFORM_VALID_FLAG");//修改平台，转换为通用代码，是否有效
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>部门用户管理</title>
<base href="<%=ViewUtil.getRequestPath(request)%>">
<jsp:include page="/avicit/platform6/component/common/EasyUIJsInclude.jsp"></jsp:include>
<jsp:include page="/avicit/platform6/modules/system/commonpopup/commonSelectionHead.jsp"></jsp:include>
<link href="avicit/platform6/modules/system/sysdept/css/icon.css" type="text/css" rel="stylesheet">
<script type="text/javascript">
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
 * 部门树单击事件
 */
function deptTreeOnClickRow(row){
	currTreeNode=row;
	if(row.attributes.type=="dept"){
		$("#btEditDept").linkbutton('enable');
		$("#btDeleteDept").linkbutton('enable');
		$("#btLanguage").linkbutton('enable');
	}else{
		clearDeptForm();
		setFormReadOnly(true);
		setOperationEnable(false);
	}
	loadSelectTabInfo(null);
}


/**
 * 清空部门表单
 */
function clearDeptForm(){
	$('#deptEditForm').form('load',{
		ID:'',
		ORG_ID:'',
		PARENT_DEPT_ID:'',
		DEPT_CODE:'',
		DEPT_NAME:'',
		DEPT_ALIAS:'',
		PARENT_DEPT_NAME:'',
		POST:'',
		TEL:'',
		FAX:'',
		EMAIL:'',
		WORK_CALENDAR_ID:'',
		ORDER_BY:'',
		ATTRIBUTE_08:'',
		DEPT_PLACE:'',
		DEPT_TYPE:'',
		ATTRIBUTE_09:'',
		VALID_FLAG:'',
		DEPT_DESC:''
		});
}

/**
 * 加载部门信息
 */
function loadDeptInfo(deptId){
	$.ajax({
        cache: true,
        type: "POST",
        url:'platform/sysdept/sysDeptController/getDeptById.json?id='+deptId,
        dataType:"json",
        data:{id:deptId},
        error: function(request) {
        	$.messager.alert('提示','操作失败，服务请求状态：'+request.status+' '+request.statusText+' 请检查服务是否可用！','warning');
        },
        success: function(data) {
			if(data.result==0&&data.sysDept){
				$('#deptEditForm').form('load',data.sysDept);
			}else{
				$.messager.alert('提示',data.msg,'warning');
			}
        }
    });
}

/**懒加载tab页Iframe**/
function loadSelectTabInfo(title){
	
	if(!title){
		var tb = $('#deptUserTab').tabs('getSelected');
		var tabOptions=tb.panel('options');
		title=tabOptions.title;
	}
	
	if(title=="部门基本信息"){
		if(!currTreeNode||currTreeNode.attributes.type!="dept") return;
		loadDeptInfo(currTreeNode.id);
	}else if(title=="用户列表"){
		
		if(currTreeNode){
			var oldUrl=$("#iframeUserList").attr("src");
			if(oldUrl==""){
				var newUrl=path+"/avicit/platform6/modules/system/sysdept/sysuserList.jsp?id="+currTreeNode.id+"&type="+currTreeNode.attributes.type+"&name="+currTreeNode.text;
				$("#iframeUserList").attr("src",newUrl);
			}else{
				//函数调用
				var frm = document.getElementById("iframeUserList").contentWindow;
				frm.loadUserData(currTreeNode.id,currTreeNode.attributes.type,currTreeNode.text);
			}
		}else{
			var oldUrl=$("#iframeUserList").attr("src");
			if(oldUrl==""){
				var newUrl=path+"/avicit/platform6/modules/system/sysdept/sysuserList.jsp";
				$("#iframeUserList").attr("src",newUrl);
			}
		}

	}
}


/**
 *查询
**/
function initForSearch(){
 $('#searchWord').searchbox({
	 	width: 200,
        searcher: function (value) {
        	var path="platform/sysdept/sysDeptController/searchDept.json";
        	if(value==null||value==""){
        		path="platform/sysdept/sysDeptController/getChildOrgDeptById.json";
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
						$('#deptEditForm')[0].reset();
						setFormReadOnly(true);
						setOperationEnable(false);
						$('#deptTree').tree('loadData',data.data);
					}else{
						$.messager.alert('提示',data.msg,'warning');
					}
                }
            });
        },
        prompt: "请输入部门名称！"
    });
}


/**
 * 设置表单是否只读
 */
function setFormReadOnly(isReadOnly){
	
	if(!isReadOnly){
		var selectNode = $('#deptTree').tree('getSelected');	
		if(!selectNode){
			$.messager.alert('提示',"请选择部门！",'warning');
		}
		
		$("form input[name!='PARENT_DEPT_NAME']").attr("disabled",false);
		$('form textarea').attr("disabled",false);
		$("#btSaveDept").linkbutton('enable');
	}else{
		$("form input[name!='PARENT_DEPT_NAME']").attr("disabled",true);
		$('form textarea').attr("disabled",true);
	}
}

/**
 * 设置操作 
 */
function setOperationEnable(isEnable){
	
	if(isEnable){
		$("#btEditDept").linkbutton('enable');
		$("#btSaveDept").linkbutton('enable');
		$("#btDeleteDept").linkbutton('enable');		
	}else{
		$("#btEditDept").linkbutton('disable');
		$("#btSaveDept").linkbutton('disable');
		$("#btDeleteDept").linkbutton('disable');
		$("#btLanguage").linkbutton('disable');
	}

}


/**
 * 刷新当前节点
 */
function updateCurrentNode(data){
	
	var selectNode = $('#deptTree').tree('getSelected');
	$('#deptTree').tree('update',{
		target: selectNode.target,
		text:data.DEPT_NAME,
		id:data.ID,
		attributes:data
	});
	
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
	var selectNode = $('#deptTree').tree('getSelected');
	
	$('#deptTree').tree('append',{
		parent: selectNode.target,
		data:[
		      {
		    	text:data.DEPT_NAME,
		  		id:data.ID,
		  		attributes:data,
		  		iconCls:'icon-dept'
		      }
		     ]
	});
	
	var newNode = $('#deptTree').tree('find',data.ID);
	$('#deptTree').tree('select',newNode.target);
	$('#deptEditForm').form('load',data);
	
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
function flushChildNodes(){
	var node = $('#deptTree').tree('getSelected');
	if(node){
		$("#deptTree").tree('reload',node.target);  //重新加载targer的子节点
	}
}

/**
 * 保存部门
 */
function saveDept(){
	
	var vResult=$('#deptEditForm').form('validate');
	if(!vResult) return false;
	
	$.ajax({
        cache: true,
        type: "POST",
        url:'platform/sysdept/sysDeptController/saveDept',
        dataType:"json",
        data:$('#deptEditForm').serialize(),
        error: function(request) {
        	$.messager.alert('提示','操作失败，服务请求状态：'+request.status+' '+request.statusText+' 请检查服务是否可用！','warning');
        },
        success: function(data) {
			if(data.result==0){
				if(data.opType=="update"){
					updateCurrentNode(data.sysDept);	
				}else if(data.opType="insert"){
					addChildNode(data.sysDept);
					//flushChildNodes();
				}
			}else{
				$.messager.alert('提示',data.msg,'warning');
			}
        }
    });
}

/**
 * 添加部门
 */
function addDept(){
	
	var selectNode = $('#deptTree').tree('getSelected');
	if(!selectNode||selectNode.id=="root"){
		$.messager.alert('提示',"请选择组织或部门！",'warning');
		return;
	}
	
	clearDeptForm();
	
	var parentName;
	var parentId;
	var orgId;
	if(selectNode.attributes.type=="dept"){
		parentId=selectNode.id;
		parentName=selectNode.attributes.DEPT_NAME;
		orgId=selectNode.attributes.ORG_ID;
	}else if(selectNode.attributes.type=="org"){
		parentName=selectNode.attributes.ORG_NAME;
		parentId="-1";
		orgId=selectNode.id;
	}else{
		parentName=selectNode.text;
	}
	
	$('#deptEditForm').form('load',{
		PARENT_DEPT_ID:parentId,
		PARENT_DEPT_NAME:parentName,
		DEPT_TYPE:'1',
		VALID_FLAG:'1',
		ATTRIBUTE_09:'0',
		PARENT_TYPE:selectNode.attributes.type,
		ORG_ID:orgId
		});
	
	setFormReadOnly(false);
	setOperationEnable(true);
	
	$("#DEPT_CODE:focus");
	
	flushChildNodes();
}

/**
 * 加载缓存
 */
/* function loadCache(){
	
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
} */

function setLanguage(){
	var selectNode = $('#deptTree').tree('getSelected');
	if(!selectNode||selectNode.attributes.type!="dept"){
		$.messager.alert('提示',"请选择部门！",'warning');
		return;
	}

	var usd = new CommonDialog("deptTlSelectDialog","800","400","platform/sysdept/sysDeptController/toLanguageListView?id="+selectNode.id,"设置多语言",null,null,false);
	usd.show();
}

function onTabSelect(title,index){
	loadSelectTabInfo(title);
}

/**
 * 删除部门
 */
function deleteDept(){
	
	var selectNode = $('#deptTree').tree('getSelected');
	if(!selectNode||selectNode.attributes.type!="dept"){
		$.messager.alert('提示',"请选择要删除的部门！",'warning');
		return;
	}
	
	$.ajax({
        cache: true,
        type: "POST",
        url:'platform/sysdept/sysDeptController/deleteDept.json',
        dataType:"json",
        data:{id:selectNode.id},
        error: function(request) {
        	$.messager.alert('提示','操作失败，服务请求状态：'+request.status+' '+request.statusText+' 请检查服务是否可用！','warning');
        },
        success: function(data) {
			if(data.result==0){
				
				clearDeptForm();
				
				var parentNode=$('#deptTree').tree('getParent',selectNode.target);
				if(parentNode){
					$('#deptTree').tree('reload',parentNode.target);
				}
				
				$.messager.show({
					title : '提示',
					msg : '操作成功！',
					timeout:2000,  
			        showType:'slide'  
				});
			}else{
				$.messager.alert('提示',data.msg,'warning');
			}
        }
    });
}

var _defaultOpenRoot_flg = false;
function defaultOpenRoot(node, data){
	if(_defaultOpenRoot_flg){
		return;
	}
	$("#deptTree").tree("expandAll");
	_defaultOpenRoot_flg = true;
}
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
	<div id="deptUserTab" class="easyui-tabs" data-options="fit:true,plain:true,tabPosition:'top',toolPosition:'right',onSelect:onTabSelect" style="padding:1px"  >
		<div id="tabDept" iconCls="icon-dept" title="部门基本信息" class="panel-noscroll">
			<div id="toolbar" class="datagrid-toolbar">
				<sec:accesscontrollist   hasPermission="3" domainObject="pmTaskInfo_tabPower_button_btAddDept" >
					<a id="btAddDept"  class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addDept();" href="javascript:void(0);">添加</a>
				</sec:accesscontrollist>
				<sec:accesscontrollist   hasPermission="3" domainObject="pmTaskInfo_tabPower_button_btEditDept" >
					<a id="btEditDept"  class="easyui-linkbutton" iconCls="icon-edit" disabled="disabled" plain="true" onclick="setFormReadOnly(false);" href="javascript:void(0);">编辑</a>
				</sec:accesscontrollist>
				<sec:accesscontrollist   hasPermission="3" domainObject="pmTaskInfo_tabPower_button_btSaveDept" >
					<a id="btSaveDept"  class="easyui-linkbutton" iconCls="icon-save" disabled="disabled" plain="true" onclick="saveDept();" href="javascript:void(0);">保存</a>
				</sec:accesscontrollist>
				
				<sec:accesscontrollist   hasPermission="3" domainObject="pmTaskInfo_tabPower_button_btDeleteDept" >
					<a id="btDeleteDept" class="easyui-linkbutton" iconCls="icon-remove" disabled="disabled" plain="true" onclick="deleteDept();" href="javascript:void(0);">删除</a>
				</sec:accesscontrollist>
				
				<%-- <sec:accesscontrollist   hasPermission="3" domainObject="pmTaskInfo_tabPower_button_btLoadCache" >
					<a id="btLanguage" class="easyui-linkbutton" disabled="disabled" iconCls="icon-my-file"  plain="true" onclick="setLanguage();" href="javascript:void(0);">多语言设置</a>
				</sec:accesscontrollist> --%>
				
			</div>
			
			<form id="deptEditForm" name="deptEditForm" method="post">
				<input type="hidden"  name="ORG_ID" id="ORG_ID"></input>
				<input type="hidden"  name="ID" id="ID"></input>
				<input type="hidden"  name="PARENT_DEPT_ID" id="PARENT_DEPT_ID"></input>
				<input type="hidden"  name="PARENT_TYPE" id="PARENT_TYPE"></input>
				<div class="formExtendBase">
					<sec:accesscontrollist hasPermission="3" domainObject="system_sysdept_deptEditForm_DEPT_CODE">
						<div class="formUnit column2">
							<label class="labelbg">部门代码：</label>
							<div class="inputContainer">
								<input class="easyui-validatebox" disabled="disabled"  data-options="required:true,validType:length[0,100]" type="text" name="DEPT_CODE" id="DEPT_CODE" ></input>
							</div>
						</div>
					</sec:accesscontrollist>
					
					<sec:accesscontrollist hasPermission="3" domainObject="system_sysdept_deptEditForm_DEPT_NAME">
						<div class="formUnit column2">
							<label class="labelbg">部门名称：</label>
							<div class="inputContainer">
								<input class="easyui-validatebox" disabled="disabled"  data-options="required:true" type="text" name="DEPT_NAME" id="DEPT_NAME" ></input>
							</div>
						</div>
					</sec:accesscontrollist>
					
					<sec:accesscontrollist hasPermission="3" domainObject="system_sysdept_deptEditForm_DEPT_ALIAS">
						<div class="formUnit column2">
							<label>部门别名：</label>
							<div class="inputContainer">
								<input class="easyui-validatebox" disabled="disabled" type="text" name="DEPT_ALIAS" id="DEPT_ALIAS" ></input>
							</div>
						</div>
					</sec:accesscontrollist>
					
					<sec:accesscontrollist hasPermission="3" domainObject="system_sysdept_deptEditForm_PARENT_DEPT_NAME">
						<div class="formUnit column2">
							<label>上级部门：</label>
							<div class="inputContainer">
								<input class="easyui-validatebox" disabled="disabled" type="text" name="PARENT_DEPT_NAME" id="PARENT_DEPT_NAME" ></input>
							</div>
						</div>
					</sec:accesscontrollist>
					
					<sec:accesscontrollist hasPermission="3" domainObject="system_sysdept_deptEditForm_POST">
						<div class="formUnit column2">
							<label>邮编：</label>
							<div class="inputContainer">
								<input class="easyui-validatebox" disabled="disabled" type="text" name="POST" id="POST" ></input>
							</div>
						</div>
					</sec:accesscontrollist>
					
					<sec:accesscontrollist hasPermission="3" domainObject="system_sysdept_deptEditForm_TEL">
						<div class="formUnit column2">
							<label >电话：</label>
							<div class="inputContainer">
								<input class="easyui-validatebox" disabled="disabled" type="text" name="TEL" id="TEL" ></input>
							</div>
						</div>
					</sec:accesscontrollist>
					
					<sec:accesscontrollist hasPermission="3" domainObject="system_sysdept_deptEditForm_FAX">
						<div class="formUnit column2">
							<label >传真：</label>
							<div class="inputContainer">
								<input class="easyui-validatebox" disabled="disabled" type="text" name="FAX" id="FAX" ></input>
							</div>
						</div>
					</sec:accesscontrollist>
					
					<sec:accesscontrollist hasPermission="3" domainObject="pmproject_pmProjectForm_editForm_EMAIL">
						<div class="formUnit column2">
							<label >邮件：</label>
							<div class="inputContainer">
								<input class="easyui-validatebox" disabled="disabled" data-options="validType:'email'" type="text" name="EMAIL" id="EMAIL" ></input>
							</div>
						</div>
					</sec:accesscontrollist>
					
					<sec:accesscontrollist hasPermission="3" domainObject="pmproject_pmProjectForm_editForm_WORK_CALENDAR_ID">
						<div class="formUnit column2">
							<label>工作日历：</label>
							<div class="inputContainer">
								<input class="easyui-validatebox" disabled="disabled" type="text" name="WORK_CALENDAR_ID" id="WORK_CALENDAR_ID" ></input>
							</div>
						</div>
					</sec:accesscontrollist>
					
					<sec:accesscontrollist hasPermission="3" domainObject="pmproject_pmProjectForm_editForm_ORDER_BY">
						<div class="formUnit column2">
							<label>排序编号：</label>
							<div class="inputContainer">
								<input class="easyui-numberbox" disabled="disabled" data-options="max:9999999999" type="text" name="ORDER_BY" id="ORDER_BY" ></input>
							</div>
						</div>
					</sec:accesscontrollist>
					
					<sec:accesscontrollist hasPermission="3" domainObject="pmproject_pmProjectForm_editForm_ATTRIBUTE_08">
						<div class="formUnit column2">
							<label>部门级别：</label>
							<div class="inputContainer">
								<input class="easyui-numberbox" disabled="disabled" data-options="max:9"  type="text"  name="ATTRIBUTE_08" id="ATTRIBUTE_08" ></input>
							</div>
						</div>
					</sec:accesscontrollist>
					
					<sec:accesscontrollist hasPermission="3" domainObject="pmproject_pmProjectForm_editForm_DEPT_PLACE">
						<div class="formUnit column2">
							<label>工作地点：</label>
							<div class="inputContainer">
								<input class="easyui-validatebox" disabled="disabled"  type="text"  name="DEPT_PLACE" id="DEPT_PLACE" ></input>
							</div>
						</div>
					</sec:accesscontrollist>
					
					<sec:accesscontrollist hasPermission="3" domainObject="pmproject_pmProjectForm_editForm_DEPT_TYPE">
						<div class="formUnit column2">
							<label class="labelbg">部门类型：</label>
							<div class="inputContainer">
								<c:forEach items="<%=lookUpdeptType%>" var="data">
									<input type="radio" name="DEPT_TYPE" disabled="disabled" id="DEPT_TYPE" value="${data.lookupCode}" style="width: 20px"/>${data.lookupName}
								</c:forEach>
							</div>
						</div>
					</sec:accesscontrollist>
					
					<%-- <sec:accesscontrollist hasPermission="3" domainObject="pmproject_pmProjectForm_editForm_ATTRIBUTE_09">
						<div class="formUnit column2">
							<label>是否PMO：</label>
							<div class="inputContainer">
								<c:forEach items="<%=lookUpYesNoFlag%>" var="data">
									<input type="radio" name="ATTRIBUTE_09" disabled="disabled" id="ATTRIBUTE_09" value="${data.lookupCode}" style="width: 20px"/>${data.lookupName}
								</c:forEach>
							</div>
						</div>
					</sec:accesscontrollist> --%>
					
					<sec:accesscontrollist hasPermission="3" domainObject="pmproject_pmProjectForm_editForm_VALID_FLAG">
						<div class="formUnit column2">
							<label class="labelbg">状态：</label>
							<div class="inputContainer">
								<c:forEach items="<%=lookUpValidFlag%>" var="data">
									<input type="radio" name="VALID_FLAG" disabled="disabled" id="VALID_FLAG" value="${data.lookupCode}" style="width: 20px"/>${data.lookupName}
								</c:forEach>
							</div>
						</div>
					</sec:accesscontrollist>
					
					<sec:accesscontrollist hasPermission="3" domainObject="pmproject_pmProjectForm_editForm_DEPT_DESC">
						<div class="formUnit column1" style="height:80px">
							<label>描述 ：</label>
							<div class="inputContainer">
								<textarea  style="height:60px;width: 90%" disabled="disabled"  name="DEPT_DESC" id="DEPT_DESC"></textarea>
							</div>
						</div>
					</sec:accesscontrollist>
					
					<div class="formUnit column1" style="height:40px">
							<label><div class="icon-tip"></div></label>
							<div class="inputContainer">
								<div><span class="icon-org"></span>代表组织 &nbsp;<span class="icon-dept"></span>代表部门</div>
							</div>
					</div>
				</div>
			</form>
		</div>
		<div id="tabUserList" iconCls="icon-org-user" title="用户列表" class="panel-noscroll">
			<iframe id="iframeUserList" name="iframeUserList" scrolling="no" frameborder="0" src="" style="width:100%;height:100%;"></iframe>
		</div>
	</div>
</div>
</body>
</html>