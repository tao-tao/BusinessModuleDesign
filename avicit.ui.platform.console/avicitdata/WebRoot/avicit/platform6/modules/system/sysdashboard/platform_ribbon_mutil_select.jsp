<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<%@ page import="avicit.platform6.core.spring.SpringFactory"%>
<html>
	<head>
		<title>系统首页</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<base href="<%=ViewUtil.getRequestPath(request)%>">
		<script type="text/javascript">
			var baseurl = "<%=ViewUtil.getRequestPath(request)%>";
		</script>
		<jsp:include page="/avicit/platform6/component/common/EasyUIJsInclude.jsp"></jsp:include>
		<link href="avicit/platform6/component/js/jQuery/jquery-easyui-1.3.5/themes/gray/mpm_extend/mpm_icon.css" type="text/css" rel="stylesheet">
		<!--[if IE 6]>
		<script type="text/javascript">
		try { document.execCommand('BackgroundImageCache', false, true); } catch(e) {}
		</script>
		<![endif]-->

	<style type="text/css">
		a {margin-top:10px}
	</style>
	<script type="text/javascript">
	
	$(function(){
		$("#datagridLeft").datagrid("hideColumn","id");
		$("#datagridRight").datagrid("hideColumn","id");
		$("#datagridRight").datagrid("hideColumn","url");
		$("#datagridRight").datagrid("hideColumn","image");
		$("#datagridRight").datagrid("hideColumn","code");
		
		initForSearchMenu();
	});
	
	
	/**
	 *查询
	 **/
	function initForSearchMenu() {
		$('#search_input_menuname').searchbox({
			width : 282,
			searcher : function(value) {
				if (value == null || value == "") {
					$("#datagridLeft").datagrid("options").url = "platform/sysmenu/getLeftMenuList";
					$('#datagridLeft').datagrid('reload');
				} else {
					$("#datagridLeft").datagrid("options").url = "platform/sysmenu/getLeftMenuList";
					$('#datagridLeft').datagrid('load', {
						MENU_NAME : value
					});
				}
			},
			prompt : "请输入名称！"
		});
	}
	
	function moveElementRL(dir,rowData){
		var originalDatagrid = "#datagridRight";
		var targetDatagrid = "#datagridLeft";
		var dirStr = "右侧";
		if(dir == 0){
			var originalDatagrid = "#datagridLeft";
			var targetDatagrid = "#datagridRight";
			dirStr = "左侧";
		}
		
		if(dir == -1){
			var originalSelects = $(originalDatagrid).datagrid("getRows");
			for(var i=0; i<originalSelects.length;i++){
				var rowIndex = $(originalDatagrid).datagrid("getRowIndex",originalSelects[i]);
				$(targetDatagrid).datagrid("appendRow",originalSelects[i]);
			}
			//$(originalDatagrid).datagrid("loadData",{});
			$(originalDatagrid).datagrid('loadData',{total:0,rows:[]}); 
		}else{
			if(rowData != null && rowData != "undifined"){
				var originalSelect = rowData;
				var rowIndex = $(originalDatagrid).datagrid("getRowIndex",originalSelect);
				$(targetDatagrid).datagrid("appendRow",originalSelect);
				$(originalDatagrid).datagrid("deleteRow",rowIndex);
			}else{
				var originalSelects = $(originalDatagrid).datagrid("getSelections");
				if(originalSelects.length > 0){
					for(var i=0; i<originalSelects.length;i++){
						var rowIndex = $(originalDatagrid).datagrid("getRowIndex",originalSelects[i]);
						$(targetDatagrid).datagrid("appendRow",originalSelects[i]);
						$(originalDatagrid).datagrid("deleteRow",rowIndex);
					}
				}else{
					$.messager.alert("操作提示", "请选择"+dirStr+"需要移动的记录!!","warning");
				}
			}
			
			
		}
		
	}
	
	
	function moveElementUD(dir){
		//var row = $("#datagridRight").datagrid('getSelected');
		var rows = $("#datagridRight").datagrid('getSelections');
		if(rows.length == 0){
			$.messager.alert("操作提示", "请选择需要移动的记录!!","warning");
		}else if(rows.length > 1){
			$.messager.alert("操作提示", "最多只能选取一条记录移动!!","warning");
		}else{
		    var index = $("#datagridRight").datagrid('getRowIndex', rows[0]);
		    mysort(index, dir, 'datagridRight');
		}
	}
	 

	function mysort(index, type, gridname) {
	    if ("1" == type) {
	        if (index != 0) {
	            var toup = $('#' + gridname).datagrid('getData').rows[index];
	            var todown = $('#' + gridname).datagrid('getData').rows[index - 1];
	            $('#' + gridname).datagrid('getData').rows[index] = todown;
	            $('#' + gridname).datagrid('getData').rows[index - 1] = toup;
	            $('#' + gridname).datagrid('refreshRow', index);
	            $('#' + gridname).datagrid('refreshRow', index - 1);
	            $('#' + gridname).datagrid('selectRow', index - 1);
	            $('#' + gridname).datagrid('unselectRow', index);
	        }
	    } else if ("-1" == type) {
	        var rows = $('#' + gridname).datagrid('getRows').length;
	        if (index != rows - 1) {
	            var todown = $('#' + gridname).datagrid('getData').rows[index];
	            var toup = $('#' + gridname).datagrid('getData').rows[index + 1];
	            $('#' + gridname).datagrid('getData').rows[index + 1] = todown;
	            $('#' + gridname).datagrid('getData').rows[index] = toup;
	            $('#' + gridname).datagrid('refreshRow', index);
	            $('#' + gridname).datagrid('refreshRow', index + 1);
	            $('#' + gridname).datagrid('selectRow', index + 1);
	            $('#' + gridname).datagrid('unselectRow', index);
	        }
	    }
	}
	
	
	function onDblClickLeft(rowIndex, rowData){
		moveElementRL(0,rowData);
	}
	
	function onDblClickRight(rowIndex, rowData){
		moveElementRL(1,rowData);
	}
	
	
	function saveForm(){
		var dataVo = $("#datagridRight").datagrid('getRows');
		dataVo = JSON.stringify(dataVo);
		var param = {dataVo : dataVo};
		$.ajax({
			url : 'platform/sysmenu/setPersonalMenu',
			data : param,
			type : 'post',
			dataType : 'json',
			success : function(result) {
				if (result.flag == '0') {
					parent.saveMenuBack(result.personalMenu);
					parent.closeDailogMenu();
				} 
			}
		});
	}
	
	
	</script>
	</head>
	<body class="easyui-layout">
		<div id="toolbarMenu" region="north" style="margin-top:10px;margin-left:20px;height: 30px;border: 0px;"  >
			<input  id="search_input_menuname" name="search_input_menuname" title="【查询条件】菜单名称"></input>
		</div>
		<div region="center" border="false" align="center">
			<div align="center" style="border: medium;color: black;">
				<div style=" width: 282px; height:330px; float:left;margin-top:15px;margin-left:20px;">
				<table id="datagridLeft" class="easyui-datagrid" datapermission='datagridLeftManager'
				data-options="
				onDblClickRow:onDblClickLeft,
				rownumbers: true,
				animate: false,
				collapsible: false,
				fitColumns: true,
				autoRowHeight: true,
				singleSelect: false,
				pagination:false,
				pageSize: dataOptions.pageSize,
				pageList: dataOptions.pageList,
				striped:true,
				fit:true,
				loadMsg:'正在加载，请稍后...',
				url: 'platform/sysmenu/getLeftMenuList',
				method: 'post'">
				<thead>
					<tr>
						<sec:accesscontrollist hasPermission="3"
							domainObject="formdialog_datagridLeftManager_id">
							<th data-options="field:'id',width:100,align:'center'">id</th>
						</sec:accesscontrollist>
						<sec:accesscontrollist hasPermission="3"
							domainObject="formdialog_datagridLeftManager_name">
							<th data-options="field:'name',width:100,align:'center'">菜单名称</th>
						</sec:accesscontrollist>
					</tr>
				</thead>
			</table>
			</div>
			<div style="width: 90px;float:left;margin:0 5px;margin-top:15px;">
				 <a id="buttonMoveRight" class="easyui-linkbutton"   onclick="moveElementRL(0);" href="javascript:void(0);"><div style="width: 50px;">右移 </div></a>
				 <a id="buttonMoveLeft" class="easyui-linkbutton"   onclick="moveElementRL(1);" href="javascript:void(0);"><div style="width: 50px;">左移</div></a>
				 <a id="buttonMoveLeftAll" class="easyui-linkbutton"   onclick="moveElementRL(-1);" href="javascript:void(0);"><div style="width: 50px;">全部左移</div></a>
			</div>
			<div style="width: 282px; height:330px;float:left;margin-top:15px;">
				<table id="datagridRight" class="easyui-datagrid" datapermission='datagridRightManager'
					data-options="
					onDblClickRow:onDblClickRight,
					rownumbers: true,
					animate: false,
					collapsible: false,
					fitColumns: true,
					autoRowHeight: true,
					singleSelect: false,
					pagination:false,
					pageSize: dataOptions.pageSize,
					pageList: dataOptions.pageList,
					striped:true,
					fit:true,
					loadMsg:'正在加载，请稍后...',
					url: 'platform/sysmenu/getRightMenuList',
					method: 'post'">
					<thead>
						<tr>
							<sec:accesscontrollist hasPermission="3"
								domainObject="formdialog_datagridLeftManager_id">
								<th data-options="field:'id',width:100,align:'center'">id</th>
							</sec:accesscontrollist>
							<sec:accesscontrollist hasPermission="3"
								domainObject="formdialog_datagridLeftManager_name">
								<th data-options="field:'name',width:100,align:'center'">菜单名称</th>
							</sec:accesscontrollist>
							<sec:accesscontrollist hasPermission="3"
								domainObject="formdialog_datagridLeftManager_url">
								<th data-options="field:'url',width:100,align:'center'">路径</th>
							</sec:accesscontrollist>
							<sec:accesscontrollist hasPermission="3"
								domainObject="formdialog_datagridLeftManager_image">
								<th data-options="field:'image',width:100,align:'center'">图片</th>
							</sec:accesscontrollist>
							<sec:accesscontrollist hasPermission="3"
								domainObject="formdialog_datagridLeftManager_code">
								<th data-options="field:'code',width:100,align:'center'">代码</th>
							</sec:accesscontrollist>
						</tr>
					</thead>
				</table>
			</div>
			<div style="width: 90px; float:left; margin:0 5px;margin-top:15px;">
				 <a id="buttonMoveUp" class="easyui-linkbutton"   onclick="moveElementUD(1);" href="javascript:void(0);"><div style="width: 50px;">上移 </div></a>
				 <a id="buttonMoveDown" class="easyui-linkbutton"   onclick="moveElementUD(-1);" href="javascript:void(0);"><div style="width: 50px;">下移</div></a>
			</div>
			</div>
		</div>
		<div style="width:100%; fit:true;position:absolute; bottom:0px;background-color:#F2F2F3;" >
		    <div align="right">
						<a id="saveButton" class="easyui-linkbutton" onclick="saveForm();" href="javascript:void(0);">保存</a>
						<a class="easyui-linkbutton" onclick="parent.closeDailogMenu();" href="javascript:void(0);">返回</a>&nbsp;&nbsp;
			</div>
		</div>
	</body>
</html>
