<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="sec" uri="/WEB-INF/tags/shiro.tld"%>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>出差流程表</title>
	<base href="<%=ViewUtil.getRequestPath(request)%>">
	<jsp:include page="/avicit/platform6/component/common/BpmJsInclude.jsp"></jsp:include>
	<script src="avicit/platform6/component/js/common/exteasyui.js" type="text/javascript"></script>
	<script type="text/javascript">
	var searchDialog;
	var searchForm;
	var myDatagrid;
	
	$(function() {
		myDatagrid = $('#demoBusinessFlowDataGrid');
		
		/*初始化查询框*/
		var p = $('#searchButton').offset();
		searchDialog = $('#searchDialog').dialog({
			left: p.left,
			top: p.top + $('#searchButton').height()
		});
		searchForm = $('#formSearchDialog').form();
		$('#formSearchDialog input').on('keyup',function(e){
			if(e.keyCode == '13'){
				searchData();
			}
		});
		setBpmMenuState($('#bpmState').val(),$('#bpmType').val());//设置选中背景色
	});

	//关联流程查询 
	function initWorkFlow(state, type) {
		setBpmMenuState(state,type);
		$('#bpmState').val(state);
		$('#bpmType').val(type);
		reloadData();
	}
	
	//打开查询框
	function openSearchForm() {
		searchDialog.dialog("open", true);
	}
	
	//加载数据
	function reloadData() {
		myDatagrid.datagrid('reload', {
			param : JSON.stringify(serializeObject(searchForm,true)), //true表示忽略表单空格，查询时一定写true 20140111
			bpmState : $('#bpmState').val(),
			bpmType : $('#bpmType').val()
		});
	}
	
	/*清空查询条件*/
	function clearData() {
		searchForm.find('input').val('');
	};

	//隐藏查询框
	function goBack() {
		searchDialog.dialog('close', true);
	}

	/**
	 *增加记录
	 **/
	function addForm() {
		var usd = new CommonDialog("FormAddDialog", "900", "450", "avicit/platform6/demo/demobusinessflow/demoBusinessFlowAdd.jsp", "添加", false, true, false,false,true);
		usd.show();
		
		closeDialog = function() {
			usd.close();
		};
	}

	/**
	 * 修改记录
	 */
	function editForm() {
		var row = myDatagrid.datagrid('getSelections');
		if (row.length == 1) {
			var id = row[0].id;
			var usdEdit = new CommonDialog("FormEditDialog", "900", "450","platform/demobusinessflowController/toEditJsp?id=" + id, "编辑", false, true, false,false,true);
			usdEdit.show();
			
			closeDialog = function() {
				usdEdit.close();
			};
		} else {
			if (row != null && row.length < 1) {
				$.messager.alert('提示', '请选择编辑的数据!', 'warning');
			} else {
				$.messager.alert('提示', '请选择一条数据进行编辑!', 'warning');
			}
		}
	}

	/**
	 *删除记录
	 */
	function deleteForm() {
		var rows = myDatagrid.datagrid('getSelections');
		var ids = [];
		if (rows.length > 0) {
			$.messager.confirm('请确认','您确定要删除当前所选的数据？',function(b) {
				if (b) {
					for ( var i = 0, length = rows.length; i < length; i++) {
						ids.push(rows[i].id);
					}
					$.ajax({
							url : 'platform/demobusinessflowController/delete',
							data : {ids : ids.join(',')},
							type : 'post',
							dataType : 'json',
							success : function(result) {
								if (result.flag == "success") {
									reloadData();
									$.messager.show({title : '提示',msg : "删除成功!",timeout:2000, showType:'slide'});
								}
							}
						});
					}
			});
		} else {
			$.messager.alert('提示', '请选择要删除的记录！', 'warning');
		}
	}
	
	/**
	 * 格式化出差人
	 * @param value
	 * @param row
	 * @param index
	 * @returns {String}
	 */
	function formatHref(value, row, index){
		return "<a href=javascript:window.executeTask('" + row.id + "','"+value+"');>" + value + "</a>";
	}
	
	/**
	 * 打开详细信息
	 * @param id
	 * @param value
	 */
	function executeTask(id, value){
		var url = "${pageContext.request.contextPath}/avicit/platform6/demo/demobusinessflow/demoBusinessFlowDetail.jsp?id=" + id;
		if(typeof(top.addTab) != 'undefined'){
			top.addTab(value, url);
		}else{
			window.open(url);
		}
	}
</script>
</head>
<body class="easyui-layout" fit="true">
<div data-options="region:'center',border:false" style="height:0; overflow:hidden; font-size:0;">
	<table id="demoBusinessFlowDataGrid" class="easyui-datagrid"
		data-options="
			rownumbers: true,
			toolbar:'#toolbar',
			animate: true,
			collapsible: false,
			fitColumns: true,
			autoRowHeight: false,
			singleSelect: false,
			pagination:true,
			pageSize: dataOptions.pageSize,
			fit:true,
			pageList:dataOptions.pageList,
			url: 'platform/demobusinessflowController/queryDemoBusinessFlowBpm.json',
			method: 'post',
			queryParams: {
				bpmState: $('#bpmState').val(),
				bpmType: $('#bpmType').val()
			}
		">
		<thead>
			<tr>
				<sec:accesscontrollist hasPermission="3" domainObject="formdialog_TestBpmBusinessTripMaganer_id">
					<th data-options="field:'id', halign:'center',checkbox:true" width="220px" title="主键">主键</th>
				</sec:accesscontrollist>
				<sec:accesscontrollist hasPermission="3" domainObject="formdialog_TestBpmBusinessTripMaganer_userid">
					<th data-options="field:'userid', halign:'center',editor:{type:'text'},formatter:formatHref" width="220px" title="出差人ID">出差人ID</th>
				</sec:accesscontrollist>
				<sec:accesscontrollist hasPermission="3" domainObject="formdialog_TestBpmBusinessTripMaganer_username">
					<th data-options="field:'username', halign:'center',editor:{type:'text'}" width="220px" title="出差人姓名">出差人姓名</th>
				</sec:accesscontrollist>
				<sec:accesscontrollist hasPermission="3" domainObject="formdialog_TestBpmBusinessTripMaganer_address">
					<th data-options="field:'address', halign:'center',editor:{type:'text'}" width="220px" title="出差地址"> 出差地址</th>
				</sec:accesscontrollist>
				<sec:accesscontrollist hasPermission="3" domainObject="formdialog_TestBpmBusinessTripMaganer_matter">
					<th data-options="field:'matter', halign:'center',editor:{type:'text'}" width="220px" title="出差事由">出差事由</th>
				</sec:accesscontrollist>
				<sec:accesscontrollist hasPermission="3" domainObject="formdialog_TestBpmBusinessTripMaganer_lendmoney">
					<th data-options="field:'lendmoney', halign:'center',editor:{type:'text'}" width="220px" title="出差借款">出差借款</th>
				</sec:accesscontrollist>
				<sec:accesscontrollist hasPermission="3" domainObject="formdialog_TestBpmBusinessTripMaganer_traffic">
					<th data-options="field:'traffic', halign:'center',editor:{type:'text'}" width="220px" title="出差借款">出差借款</th>
				</sec:accesscontrollist>
				<sec:accesscontrollist hasPermission="3" domainObject="formdialog_TestBpmBusinessTripMaganer_leavedate">
					<th data-options="field:'leavedate', halign:'center',formatter:formatDatebox,editor:{type:'text'}" width="220px" title="离开日期">离开日期</th>
				</sec:accesscontrollist>
				<sec:accesscontrollist hasPermission="3" domainObject="formdialog_TestBpmBusinessTripMaganer_backdate">
					<th data-options="field:'backdate', halign:'center',formatter:formatDatebox,editor:{type:'text'}" width="220px" title="返回日期">返回日期</th>
				</sec:accesscontrollist>
				<sec:accesscontrollist hasPermission="3" domainObject="formdialog_TestBpmBusinessTripMaganer_summary">
					<th data-options="field:'summary', halign:'center',editor:{type:'text'}" width="220px" title="出差总结">出差总结</th>
				</sec:accesscontrollist>
				<sec:accesscontrollist hasPermission="3" domainObject="formdialog_TestBpmBusinessTripMaganer_allowance">
					<th data-options="field:'allowance', halign:'center',editor:{type:'text'}" width="220px" title="出差补助">出差补助</th>
				</sec:accesscontrollist>
				<sec:accesscontrollist hasPermission="3" domainObject="formdialog_TestBpmBusinessTripMaganer_activityalias_">
					<th data-options="field:'activityalias_', halign:'center'" width="220px" title="流程当前步骤">流程当前步骤</th>
				</sec:accesscontrollist>
				<sec:accesscontrollist hasPermission="3" domainObject="formdialog_TestBpmBusinessTripMaganer_bussinessstate_">
					<th data-options="field:'bussinessstate_', halign:'center'" width="220px" title="流程状态">流程状态</th>
				</sec:accesscontrollist>
			</tr>
		</thead>
	</table>
	<div id="toolbar" class="datagrid-toolbar" style="height:auto;display: block;">
		<table class="tableForm" id="menuTable" width='100%'>
			<tr>	
			<sec:accesscontrollist   hasPermission="3" domainObject="formdialog_TestBpmBusinessTripMaganer_button_bpmAll" >
					<td width="105px;">
						<a href="javascript:void(0);" id="allMenu" name="bpm_all_menu" class='easyui-menubutton' data-options="menu:'#allmm',iconCls:'icon-all-file'">全部文件</a>
						<div id="allmm" style="width:105px;">
							<div id='all_start' name="bpm_all_start" onclick="initWorkFlow('start','all')">拟稿中</div>
							<div id='all_active' name="bpm_all_active" onclick="initWorkFlow('active','all')">流转中</div>
							<div id='all_ended' name="bpm_all_ended" onclick="initWorkFlow('ended','all')">已完成</div>
							<div id='all_all' name="bpm_all_all" onclick="initWorkFlow('all','all')">全部文件</div>
						</div>
					</td>
				</sec:accesscontrollist>
				<sec:accesscontrollist   hasPermission="3" domainObject="formdialog_TestBpmBusinessTripMaganer_button_bpmMy" >
					<td width="105px;">
						<a href="javascript:void(0);" id="myMenu" name="bpm_my_menu" class='easyui-menubutton' data-options="menu:'#mymm',iconCls:'icon-my-file'">我的文件</a>
						<div id="mymm" style="width:105px;">
							<div id='my_start' name="bpm_my_start"  onclick="initWorkFlow('start','my')">拟稿中</div>
							<div id='my_active' name="bpm_my_active" onclick="initWorkFlow('active','my')">流转中</div>
							<div id='my_ended' name="bpm_my_ended" onclick="initWorkFlow('ended','my')">已完成</div>
							<div id='my_all' name="bpm_my_all" onclick="initWorkFlow('all','my')">我的全部</div>
						</div>
					</td>
				</sec:accesscontrollist>
				<sec:accesscontrollist hasPermission="3" domainObject="formdialog_TestBpmBusinessTripMaganer_button_addForm" >
					<td width="65px;"><a id="addButton" title="添加" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addForm();" href="javascript:void(0);">添加</a></td>
				</sec:accesscontrollist>
				
				<sec:accesscontrollist hasPermission="3" domainObject="formdialog_TestBpmBusinessTripMaganer_button_editForm" >
					<td width="65px;"><a title="编辑" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editForm();" href="javascript:void(0);">编辑</a></td>
				</sec:accesscontrollist>
				
				<sec:accesscontrollist hasPermission="3" domainObject="formdialog_TestBpmBusinessTripMaganer_button_deleteForm" >
					<td width="65px;"><a title="删除" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="deleteForm();" href="javascript:void(0);">删除</a></td>
				</sec:accesscontrollist>
				
				<sec:accesscontrollist hasPermission="3" domainObject="formdialog_TestBpmBusinessTripMaganer_button_openSearchForm" >
					<td><a title="查询" class="easyui-linkbutton" id="searchButton" iconCls="icon-search" plain="true" onclick="openSearchForm();" href="javascript:void(0);">查询</a></td>
				</sec:accesscontrollist>
				<td > </td>
			</tr>
		</table>
	</div>
</div>
<!-- 查询页面 -->
<div id="searchDialog" class="easyui-dialog" data-options="title:'查询',closed:true,iconCls:'icon-search',resizable:true,modal:false,buttons:'#searchBtns'" style="width:700px;height:200px;">
	<form id="formSearchDialog" method="post">
		<div class="formExtendBase">
			<div class="formUnit column2">
				<label>出差人姓名</label>
				<div class="inputContainer">
					<input class="easyui-validatebox" style="width: 90%" name="filter-LIKE-username"></input>
				</div>
			</div>
			<div class="formUnit column2">
				<label>出差借款</label>
				<div class="inputContainer">
					<input class="easyui-validatebox" style="width: 90%" name="filter-LIKE-lendmoney"></input>
				</div>
			</div>
			<div class="formUnit column2">
				<label>交通工具</label>
				<div class="inputContainer">
					<input class="easyui-validatebox" style="width: 90%" name="filter-LIKE-traffic"></input>
				</div>
			</div>
			<div class="formUnit column2">
				<label>离开日期</label>
				<div class="inputContainer">
					<input class="easyui-validatebox" style="width: 90%" name="filter-LIKE-leavedate"></input>
				</div>
			</div>
		</div>
		<div id="searchBtns">
			<a class="easyui-linkbutton" iconCls="icon-search" plain="true"
				onclick="reloadData();" href="javascript:void(0);">查询</a>
			<a class="easyui-linkbutton" iconCls="icon-cancel" plain="true"
				onclick="clearData();" href="javascript:void(0);">清空</a>
			<a class="easyui-linkbutton" iconCls="icon-back" plain="true"
				onclick="goBack();" href="javascript:void(0);">返回</a>
		</div>
	</form>
	<form id="formSearchBpm">
		<input type="hidden" name="bpmState"  id="bpmState" value="all"></input>
		<input type="hidden" name="bpmType"  id="bpmType" value="my"></input>
	</form>
</div>
</body>
</html>