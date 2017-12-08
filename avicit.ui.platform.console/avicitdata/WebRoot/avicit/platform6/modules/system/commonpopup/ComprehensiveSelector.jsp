<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<%@ taglib prefix="sec" uri="/WEB-INF/tags/shiro.tld" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>综合选择</title>
<base href="<%=ViewUtil.getRequestPath(request) %>">
<jsp:include page="/avicit/platform6/component/common/EasyUIJsInclude.jsp"></jsp:include>
<jsp:include page="/avicit/platform6/modules/system/commonpopup/commonSelectionHead.jsp"></jsp:include>
<script
	src="static/js/platform/component/commonselection/ComprehensiveSelector.js"
	type="text/javascript"></script>
<%
	//页面过来接收参数
	String showTab = request.getParameter("showTab");
	String extParameter = request.getParameter("extParameter");
	String multipleOrg = request.getParameter("multipleOrg");
	String displaySubDeptUser = request.getParameter("displaySubDeptUser");
%>
<script type="text/javascript">
    var multipleOrg_req = '<%=multipleOrg%>' ;
	/**
	 * 初始化当前页面
	 */
		$(function() {
			var dataGridHeight = $(".easyui-layout").height();
			$("#ButtonDiv").css("height", dataGridHeight - 5);
			$("#ButtonDiv").css("padding-top", dataGridHeight / 2 - 30);
			var showTab ="<%=showTab%>";
			var mappingConfigJson = null ;
			if(showTab!=null && typeof(showTab)!='undefined' && showTab.length>0){
				mappingConfigJson = parent.document.getElementById(showTab).value;
			}
			var json = JSON.parse(mappingConfigJson);
			loadTargetDataGridTable(dataGridHeight-5);
			initTabContainer(dataGridHeight,json);
			if(json!=null && typeof(json)!='undefined'){
				var userShow=json.user.tabShow;
				if(userShow!=null && typeof(userShow)!='undefined'){
					if(userShow=='1'){
						$('#comprehensiveTabControl').tabs("select","用户");
					}else{
						//$("#userTab").css("display","none");
						$('#comprehensiveTabControl').tabs("close","用户");
					}
				}
				var deptShow=json.dept.tabShow;
		        if(deptShow!=null && typeof(deptShow)!='undefined'){
		        	if(deptShow=='1'){
		        		$('#comprehensiveTabControl').tabs("select","部门");
					}else{
						//$("#deptTab").css("display","none");
						$('#comprehensiveTabControl').tabs("close","部门");
					}
				} 
				var roleShow=json.role.tabShow;
		        if(roleShow!=null && typeof(roleShow)!='undefined'){
		        	if(roleShow=='1'){
		        		$('#comprehensiveTabControl').tabs("select","角色");
					}else{
						//$("#roleTab").css("display","none");
						$('#comprehensiveTabControl').tabs("close","角色");
					}
				}
				var groupShow=json.group.tabShow;
		        if(groupShow!=null && typeof(groupShow)!='undefined'){
		        	if(groupShow=='1'){
		        		$('#comprehensiveTabControl').tabs("select","群组");
					}else{
						//$("#groupTab").css("display","none");
						$('#comprehensiveTabControl').tabs("close","群组");
					}
		        	
				}
				var positionShow=json.position.tabShow;
		        if(positionShow!=null && typeof(positionShow)!='undefined'){
		        	if(positionShow=='1'){
		        		$('#comprehensiveTabControl').tabs("select","岗位");
					}else{
						$('#comprehensiveTabControl').tabs("close","岗位");
					}
				}
		        displayParentPageSelectedData(json) ;
			}else{
				$("#userTab").css("display","block");
				$("#deptTab").css("display","block");
				$("#roleTab").css("display","block");
				$("#groupTab").css("display","block");
				$("#positionTab").css("display","block");
			}
		});
	
	/**
	 *获得已选择的数据
	 */
	function getSelectedResultDataJson(){
		return $('#selectTargetDataGrid').datagrid('getChecked');
	}
</script>
</head>
<body class="easyui-layout" fit="true">
	<div region="west" style="overflow: hidden; float: left;">
		<div id="comprehensiveTabControl" class="easyui-tabs"
			style="width: 350px">
			<div title="用户" style="padding: 10px; width: auto" id = "userTab" style = "display:block;" 
				data-options="iconCls:'icon-user',closable:false">
				<div id="toolbar" class="datagrid-toolbar" style="display: block;">
					<table class="tableForm" id="userSearchForm" width='100%'>
						<tr>
							<td width=""><input id="userQueryText"></input></td>
						</tr>
					</table>
				</div>
				<ul id="orgTree_user" style="width: auto; overflow: auto;"></ul>
			</div>
			<div title="部门" style="padding: 10px; width: auto" id = "deptTab" style = "display:block;" 
				data-options="iconCls:'icon-dept',closable:false">
				<div id="toolbar" class="datagrid-toolbar" style="display: block;">
					<table class="tableForm" id="deptSearchForm" width='100%'>
						<tr>
							<td width=""><input id="deptQueryText"></input></td>
						</tr>
					</table>
				</div>
				<ul id="orgTree_dept" style="width: auto; overflow: auto;"></ul>
			</div>
			<div title="角色" style="padding: 10px; width: auto" id = "roleTab" style = "display:block;" 
				data-options="iconCls:'icon-role',closable:false">
				<div id="toolbar" class="datagrid-toolbar" style="display: block;">
					<table class="tableForm" id="roleSearchForm" width='100%'>
						<tr>
							<td width=""><input id="roleQueryText"></input></td>
						</tr>
					</table>
				</div>
				<table id="roleList" style="overflow: auto;"  ></table>
			</div>
			<div title="群组" style="padding: 10px; width: auto" id = "groupTab" style = "display:block;" 
				data-options="iconCls:'icon-group',closable:false">
				<div id="toolbar" class="datagrid-toolbar" style="display: block;">
					<table class="tableForm" id="GroupSearchForm" width='100%'>
						<tr>
							<td width=""><input id="groupQueryText"></input></td>
						</tr>
					</table>
				</div>
				<table id="groupList" style="overflow: auto;"></table>
			</div>
			<div title="岗位" style="padding: 10px; width: auto" id = "positionTab" style = "display:block;" 
				data-options="iconCls:'icon-position',closable:false">
				<div id="toolbar" class="datagrid-toolbar" style="display: block;">
					<table class="tableForm" id="positionSearchForm" width='100%'>
						<tr>
							<td width=""><input id="positionQueryText"></input></td>
						</tr>
					</table>
				</div>
				<table id="positionList" style="overflow: auto;"></table>
			</div>
		</div>
	</div>
	<div region="center" border="false" id="ButtonDiv"  style="overflow: hidden; width:100px;float: left;">
		<table border="0" align="center" >
			<tr>
				<td><input type="button" value="添加>>"   id="buttonAddSelectedData"
					style="cursor: pointer; width: 65px;align:center;"
					onclick="addSelectDataToGrid();" /><br /> <br /> <input  id="buttonRemoveSelectedData"
					type="button" value="<<删除" style="cursor: pointer; width: 65px; align:center;"
					onclick="removeGridAllSelectedData('selectTargetDataGrid');" /><br /></td>
			</tr>
		</table>
	</div>
   <div region="east" border="false" style="overflow: auto;float:left;">
       <div id="selectTargetDataGrid" style="overflow-y: auto;width:320px;" ></div>
</div>
</body>
</html>