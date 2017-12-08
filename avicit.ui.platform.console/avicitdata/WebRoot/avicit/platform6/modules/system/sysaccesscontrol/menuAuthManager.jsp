<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>菜单授权管理</title>
		<base href="<%=ViewUtil.getRequestPath(request) %>">
		<jsp:include page="/avicit/platform6/component/common/EasyUIJsInclude.jsp"></jsp:include>
		<jsp:include page="/avicit/platform6/modules/system/commonpopup/commonSelectionHead.jsp"></jsp:include>
		<script src="avicit/platform6/modules/system/sysaccesscontrol/js/menu/center.js" type="text/javascript"></script>
		<script src="avicit/platform6/modules/system/sysaccesscontrol/js/menu/menutree.js" type="text/javascript"></script>
		<script type="text/javascript" charset="utf-8">
		var currentMenuId="root";
		var curComId="none";
		var comDatagrid;
		var currTabIndex=0;
		var TARGET_TYPE = "R"; // 目标类型； 角色(R)，用户(U)，部门(D)，群组(G)，岗位(P)。
		window.TARGET_ID = ""; // 目标ID
		$(function(){
			initMemuSearch();
			initTabContainer();
		});
		function myOnBeforeExpand(row) {
			$("#memuTree").tree("options").url = "platform/sysPermissionTreeController/listMemuTreeById.json?id=" + row.id;
			return true;
		}
		function clickTreeRow(row){
			currentMenuId = row.id;
			reloadTabData(currTabIndex);
		}
		

		function reloadTabData(index){
			if(index==0){
				loadRole(currentMenuId);
			}else if(index==1){
				loadUser(currentMenuId);
			}else if(index==2){
				loadDept(currentMenuId);
			}else if(index==3){
				loadGroup(currentMenuId);
			}else if(index==4){
				loadPosition(currentMenuId);
			}
		}
		/**
		 *菜单查询
		 **/
		function initMemuSearch() {
			$('#searchWord').searchbox({
				width : 200,
				searcher : function(value) {
					var path = "platform/sysPermissionTreeController/searchMemu.json";
					if (value == null || value == "") {
						path = "platform/sysPermissionTreeController/listMemuTreeById.json";
					}
					$.ajax({
						cache : true,
						type : "POST",
						url : path,
						dataType : "json",
						data : {
							search_text : value
						},
						async : false,
						error : function(request) {
							alert('操作失败，服务请求状态：' + request.status
									+ ' ' + request.statusText
									+ ' 请检查服务是否可用！');
						},
						success : function(data) {
							if (data.result == 0) {
								$('#memuTree').tree('loadData',
										data.data);
							} else {
								$.messager.alert('提示', data.msg,
										'warning');
							}
						}
					});
				},
				prompt : "请输入菜单名称！"
			});
		}
		
		
		
		</script>
	</head>
	
	<body class="easyui-layout" fit="true">
		<div data-options="region:'west',title:' 菜单资源信息',split:true,iconCls:'icon-save'" style="width:400px;background:#f5fafe;overflow-y:hidden;">
				<%@ include file ="menu/menutree.jsp"%>
		</div> 
		<div data-options="region:'center',iconCls:'icon-search',title:'权限设置'" style="background:#ffffff; height:0; overflow:hidden; font-size:0;">
			 <%@ include file ="menu/center.jsp"%>
		</div>
		
	
	</body>
</html>