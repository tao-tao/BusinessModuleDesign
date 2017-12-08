<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<%@taglib prefix="sec" uri="/WEB-INF/tags/shiro.tld"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>demo</title>
<base href="<%=ViewUtil.getRequestPath(request)%>">
<jsp:include page="/avicit/platform6/component/common/EasyUIJsInclude.jsp"></jsp:include>
<script src="static/js/platform/component/common/exteasyui.js" type="text/javascript"></script>
</head>
<body class="easyui-layout" fit="true">
	<div data-options="region:'center'" style="background:#ffffff;">
		<div id="toolbarDemoBusinessTrip" class="datagrid-toolbar">
		 	<table>
		 		<tr>
		 		<sec:accesscontrollist hasPermission="3" domainObject="formdialog_demoBusinessTrip_button_add" permissionDes="添加">
					<td><a class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="demoBusinessTrip.insert();" href="javascript:void(0);">添加</a></td>
				</sec:accesscontrollist>
				<sec:accesscontrollist hasPermission="3" domainObject="formdialog_demoBusinessTrip_button_edit" permissionDes="编辑">
					<td><a class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="demoBusinessTrip.modify();" href="javascript:void(0);">编辑</a></td>
				</sec:accesscontrollist>
				<sec:accesscontrollist hasPermission="3" domainObject="formdialog_demoBusinessTrip_button_delete" permissionDes="删除">
					<td><a class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="demoBusinessTrip.del();" href="javascript:void(0);">删除</a></td>
				</sec:accesscontrollist>
				<sec:accesscontrollist hasPermission="3" domainObject="formdialog_demoBusinessTrip_button_query" permissionDes="查询">
					<td><a class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="demoBusinessTrip.openSearchForm();" href="javascript:void(0);">查询</a></td>
				</sec:accesscontrollist>
				</tr>
		 	</table>
	 	</div>
	 	<table id="dgDemoBusinessTrip"
			data-options="
				fit: true,
				border: false,
				rownumbers: true,
				animate: true,
				collapsible: false,
				fitColumns: true,
				autoRowHeight: false,
				toolbar:'#toolbarDemoBusinessTrip',
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
					<th data-options="field:'userId', halign:'center',formatter:formateHref" width="220">用户id</th><!-- ,hidden:true -->
					<th data-options="field:'userName',align:'center'" width="220">用户名称</th>
					<th data-options="field:'address',align:'center'" width="220">出差地址</th>
					<th data-options="field:'matter',align:'center'"  width="120">出差事由</th>
					<th data-options="field:'lendmoney',align:'center'"  width="120">出差借款</th>
					<th data-options="field:'traffic',align:'center'"  width="120">交通工具</th>
					<th data-options="field:'leavedate',align:'center',formatter:formateDate"  width="220">出差日期</th>
					<th data-options="field:'backdate',align:'center',formatter:formateDate"  width="220">返回日期</th>
					<th data-options="field:'summary',align:'center'"  width="220">出差总结</th>
					<th data-options="field:'allowance',align:'center'"  width="220">出差补助</th>
					
				</tr>
			</thead>
		</table>
	</div>
	<!--*****************************搜索*********************************  -->
	<div id="searchDialog" data-options="iconCls:'icon-search',resizable:true,modal:false,buttons:'#searchBtns'" style="width: 600px;height:200px;display: none;">
		<form id="form1">
    		<table style="padding-top: 10px;">
    			<tr>
    				<td>用户id:</td>
    				<td><input class="inputbox"  style="width: 151px;" type="text" name="userId"/></td>
    				<td>用户名称:</td>
    				<td><input class="inputbox" style="width: 151px;" type="text" name="userName"/></td>
    			</tr>
    			<tr>
    				<td>出差地址</td>
    				<td><input class="inputbox"  style="width: 151px;" type="text" name="address"/></td>
    				<td>出差事由</td>
    				<td><input class="inputbox" style="width: 151px;" type="text" name="matter"/></td>
    			</tr>
    		</table>
    	</form>
    	<div id="searchBtns">
    		<a class="easyui-linkbutton" iconCls="" plain="false" onclick="demoBusinessTrip.searchData();" href="javascript:void(0);">查询</a>
    		<a class="easyui-linkbutton" iconCls="" plain="false" onclick="demoBusinessTrip.clearData();" href="javascript:void(0);">清空</a>
    		<a class="easyui-linkbutton" iconCls="" plain="false" onclick="demoBusinessTrip.hideSearchForm();" href="javascript:void(0);">返回</a>
    	</div>
  </div>
	<script src="avicit/platform6/demo/demobusinesstrip/js/DemoBusinessTrip.js" type="text/javascript"></script>
	<script type="text/javascript">
		var demoBusinessTrip;
		$(function(){
			demoBusinessTrip= new DemoBusinessTrip('dgDemoBusinessTrip','${url}','searchDialog','form1');
		});
		function formateDate(value,row,index){
			return demoBusinessTrip.formate(value);
		}
		//demoBusinessTrip.detail(\''+row.id+'\')
		function formateHref(value,row,inde){
			return '<a href="javascript:void(0);" onclick="demoBusinessTrip.detail(\''+row.id+'\');">'+value+'</a>';
		}
	</script>
</body>
</html>