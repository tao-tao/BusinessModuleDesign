<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<%@taglib prefix="sec" uri="/WEB-INF/tags/shiro.tld"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>选择部门</title>
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
						$('#deptTree').tree('loadData',data.data);
					}else{
						alert("查询失败："+data.msg);
					}
                }
            });
        },
        prompt: "请输入部门名称！"
    });
}


/**
 * 选择用户部门
 */
function saveMoveDept(){
	var selectNode = $('#deptTree').tree('getSelected');
	if (selectNode&&"dept"==selectNode.attributes.type){
			parent.saveSelectedDeptId(selectNode.id);	
	}else{
		$.messager.alert('提示',"请选择部门！",'warning');
	}
}

/**
 * 关闭
 */
function closeDialog(){
	parent.$("#authSelectDeptDialog").dialog('close');
}
</script>
</head>

<body class="easyui-layout" style="overflow:hidden;">

<div data-options="region:'center',split:true,title:''" style="padding:0px;" >
	 <div id="toolbar" >
	 	<table width="100%">
	 		<tr>
	 			<td width="100%"><input  type="text"  name="searchWord" id="searchWord"></input></td>
	 		</tr>
	 	</table>
	 </div>
	<ul id="deptTree" class="easyui-tree" data-options="
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
       		animate:true	" style="overflow-x:hidden;">数据加载中...</ul>
</div>

						
<div region="south" border="false">
	<div id="toolbar" class="datagrid-toolbar" style="height:auto;display: block;">
	<table class="tableForm"  width='100%'>
		<tr>	
		<td style="float:right">
			<sec:accesscontrollist hasPermission="3" domainObject="formdialog_SysLookupTypeBakAdd_button_saveForm" >
				<a  title="确定" id="saveButton"  class="easyui-linkbutton" plain="false" onclick="saveMoveDept();" href="javascript:void(0);">确定</a>
			</sec:accesscontrollist>
			
			
			<sec:accesscontrollist hasPermission="3" domainObject="formdialog_SysLookupTypeBakAdd_button_backForm" >
				<a  title="返回" id="backButton"  class="easyui-linkbutton" plain="false" onclick="closeDialog();" href="javascript:void(0);">返回</a>
			</sec:accesscontrollist>
		</td>
		</tr>
	</table>
  </div>
</div>
</body>
</html>