<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<%@taglib prefix="sec" uri="/WEB-INF/tags/shiro.tld"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>出差流程示例</title>
<base href="<%=ViewUtil.getRequestPath(request)%>">
<jsp:include page="/avicit/platform6/component/common/EasyUIJsInclude.jsp"></jsp:include>
<script src="static/js/platform/component/dialog/UserSelectDialog.js" type="text/javascript"></script>
<script src="static/js/platform/bpm/client/js/ToolBar.js" type="text/javascript"></script>
<script src="static/js/platform/component/common/exteasyui.js" type="text/javascript"></script>
</head>
<body class="easyui-layout" fit="true">
	<div data-options="region:'center'" style="background:#ffffff;">
		<div id="toolbarDemoBusinessFlow" class="datagrid-toolbar">
		 	<table>
		 		<tr>
		 		<sec:accesscontrollist hasPermission="3" domainObject="formdialog_demoBusinessFlowR3_button_bpmAll" permissionDes="全部文件">
					<td width="105px;">
						<a href="javascript:void(0);" id="allMenu" name="bpm_all_menu" class='easyui-menubutton' data-options="menu:'#allmm',iconCls:'icon-all-file'">全部文件</a>
						<div id="allmm" style="width:105px;">
							<div id='all_start' name="bpm_all_start" onclick="demoBusinessFlow.initWorkFlow('start','all')">拟稿中</div>
							<div id='all_active' name="bpm_all_active" onclick="demoBusinessFlow.initWorkFlow('active','all')">流转中</div>
							<div id='all_ended' name="bpm_all_ended" onclick="demoBusinessFlow.initWorkFlow('ended','all')">已完成</div>
							<div id='all_all' name="bpm_all_all" onclick="demoBusinessFlow.initWorkFlow('all','all')">全部文件</div>
						</div>
					</td>
				</sec:accesscontrollist>
				<sec:accesscontrollist hasPermission="3" domainObject="formdialog_demoBusinessFlowR3_button_bpmMyFile" permissionDes="我的文件">
					<td width="105px;">
						<a href="javascript:void(0);" id="myMenu" name="bpm_my_menu" class='easyui-menubutton' data-options="menu:'#mymm',iconCls:'icon-my-file'">我的全部</a>
						<div id="mymm" style="width:105px;">
							<div id='my_start' name="bpm_my_start"  onclick="demoBusinessFlow.initWorkFlow('start','my')">我的拟稿</div>
							<div id='my_active' name="bpm_my_active" onclick="demoBusinessFlow.initWorkFlow('active','my')">我的流转</div>
							<div id='my_ended' name="bpm_my_ended" onclick="demoBusinessFlow.initWorkFlow('ended','my')">我的完成</div>
							<div id='my_all' name="bpm_my_all" onclick="demoBusinessFlow.initWorkFlow('all','my')">我的全部</div>
						</div>
					</td>
				</sec:accesscontrollist>
				<sec:accesscontrollist hasPermission="3" domainObject="formdialog_demoBusinessFlowR3_button_add" permissionDes="添加">
					<td><a class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="demoBusinessFlow.insert();" href="javascript:void(0);">添加</a></td>
				</sec:accesscontrollist>
				<sec:accesscontrollist hasPermission="3" domainObject="formdialog_demoBusinessFlowR3_button_edit" permissionDes="编辑">
					<td><a class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="demoBusinessFlow.modify();" href="javascript:void(0);">编辑</a></td>
				</sec:accesscontrollist>
				<sec:accesscontrollist hasPermission="3" domainObject="formdialog_demoBusinessFlowR3_button_delete" permissionDes="删除">
					<td id="tool_del_td"><a class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="demoBusinessFlow.del();" href="javascript:void(0);">删除</a></td>
				</sec:accesscontrollist>
				<sec:accesscontrollist hasPermission="3" domainObject="formdialog_demoBusinessFlowR3_button_query" permissionDes="查询">
					<td><a class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="demoBusinessFlow.openSearchForm();" href="javascript:void(0);">查询</a></td>
				</sec:accesscontrollist>
				
				<sec:accesscontrollist hasPermission="3" domainObject="formdialog_demoBusinessFlowR3_button_doBatchStart" permissionDes="批量启动流程测试">
					<td><a class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="doBatchStart();" href="javascript:void(0);">批量启动流程</a></td>
				</sec:accesscontrollist>
				
				</tr>
		 	</table>
	 	</div>
	 	<table id="dgDemoBusinessFlow"
			data-options="
				fit: true,
				border: false,
				rownumbers: true,
				animate: true,
				collapsible: false,
				fitColumns: true,
				autoRowHeight: false,
				toolbar:'#toolbarDemoBusinessFlow',
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
					<th data-options="field:'id', halign:'center',checkbox:true" width="220">主键</th>
					<th data-options="field:'userid', halign:'center',formatter:formateHref" width="220">出差人ID</th>
					<th data-options="field:'username', halign:'center'" width="220">出差人姓名</th>
					<th data-options="field:'address', halign:'center'" width="220">出差地址</th>
					<th data-options="field:'matter', halign:'center'" width="220">出差事由</th>
					<th data-options="field:'lendmoney', halign:'center'" width="220">出差借款</th>
					<th data-options="field:'traffic', halign:'center'" width="220">交通工具</th>
					<th data-options="field:'leavedate', halign:'center',formatter:formateDate" width="220">出差日期</th>
					<th data-options="field:'backdate', halign:'center',formatter:formateDate" width="220">返回日期</th>
					<th data-options="field:'summary', halign:'center'" width="220">出差总结</th>
					<th data-options="field:'allowance', halign:'center'" width="220">出差补助</th>
					<th data-options="field:'activityalias_', halign:'center'" width="220px">流程当前步骤</th>
					<th data-options="field:'businessstate_', halign:'center'" width="220px">流程状态</th>
				</tr>
			</thead>
		</table>
	</div>
	<!--*****************************搜索*********************************  -->
	<div id="searchDialog" data-options="iconCls:'icon-search',resizable:true,modal:false,buttons:'#searchBtns'" style="width: 600px;height:200px;display: none;">
		<form id="form1">
    		<table style="padding-top: 10px;">
					<tr>
													
							<td>出差人姓名:</td>
	    					<td><input class="inputbox"  style="width: 151px;" type="text" name="username"/></td>
													
							<td>出差借款:</td>
	    					<td><input class="inputbox"  style="width: 151px;" type="text" name="lendmoney"/></td>
					</tr>
					<tr>
													
							<td>交通工具:</td>
	    					<td><input class="inputbox"  style="width: 151px;" type="text" name="traffic"/></td>
													
							<td>出差日期:</td>
	    					<td><input class="inputbox"  style="width: 151px;" type="text" name="leavedate"/></td>
					</tr>
					<tr>
					</tr>
    		</table>
    		<input type="hidden" name="bpmState"  id="bpmState" value="all"></input>
			<input type="hidden" name="bpmType"  id="bpmType" value="my"></input>
    	</form>
    	<div id="searchBtns">
    		<a class="easyui-linkbutton" onclick="demoBusinessFlow.searchData();" href="javascript:void(0);">查询</a>
    		<a class="easyui-linkbutton" onclick="demoBusinessFlow.clearData();" href="javascript:void(0);">清空</a>
    		<a class="easyui-linkbutton" onclick="demoBusinessFlow.hideSearchForm();" href="javascript:void(0);">返回</a>
    	</div>
  </div>
  
	<script src=" avicit/platform6/demo/demobusinessflowr3/js/DemoBusinessFlow.js" type="text/javascript"></script>
	<script type="text/javascript">
		var demoBusinessFlow;
		$(function(){
			demoBusinessFlow= new DemoBusinessFlow('dgDemoBusinessFlow','${url}','searchDialog','form1');
		});
		function formateDate(value,row,index){
			return demoBusinessFlow.formate(value);
		}
		//demoBusinessFlow.detail(\''+row.id+'\')
		function formateHref(value,row,inde){
			return "<a href='javascript:void(0);' onclick='demoBusinessFlow.detail(\""+row.id+"\",\""+value+"\");'>"+value+"</a>";
		}
		
		function doBatchStart(){
			if(confirm("确定批量启动50个流程吗？")){
				MaskUtil.mask("正在创建业务数据...");//添加遮挡
				//先去创建业务数据，返回业务数据json字符串，批量启动流程时。将业务数据json字符串传递给流程
				$.ajax({
					url : "<%=request.getContextPath()%>/platform/demoBusinessFlow/batchInsert",
					type : 'get',
					dataType : 'json',
					success : function(data){
						MaskUtil.unmask();//去掉遮挡
						if(data != null && data.error != null){
							$.messager.show({title : '提示', msg : data.error});
						}else{
							$.messager.show({title : '提示', msg : '业务数据创建完成，请选择流程启动！'});
							var processDef = new StartProcessByFormCode();//定义流程选择窗口
							processDef.SetFormCode("demoBusinessFlowR3");//指定formCode
							StartProcessByFormCode.prototype.doStart = function(pdId) {
								MaskUtil.mask("正在批量启动流程...");//添加遮挡
								//调用 doBatchstart接口，将表单代码、业务数据、流程定义ID提交到Java端 
								$.ajax({
									url : "<%=request.getContextPath()%>/platform/bpm/clientbpmoperateaction/doBatchstart",
									data : {processDefId : pdId, formCode : "demoBusinessFlowR3", jsonString : JSON.stringify(data.result)},
									type : 'post',
									dataType : 'json',
									success : function(result){
										MaskUtil.unmask();//去掉遮挡
										if(result != null && result.error != null){
											$.messager.show({title : '提示', msg : result.error});
										}else{
											demoBusinessFlow.reLoad();//刷新当前列表
											$.messager.show({title : '提示', msg : '批量启动流程完毕！'});
										}
									}
								});
							};
							processDef.start();
						}
					}
				});
			}
		}
	</script>
</body>
</html>