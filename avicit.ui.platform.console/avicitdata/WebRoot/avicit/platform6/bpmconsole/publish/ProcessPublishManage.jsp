<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>流程实例信息</title>
<base href="<%=ViewUtil.getRequestPath(request) %>">
<jsp:include page="/avicit/platform6/bpmclient/bpm/ProcessCommonJsInclude.jsp"></jsp:include>
<jsp:include page="/avicit/platform6/modules/system/commonpopup/commonSelectionHead.jsp"></jsp:include>
</head>

<script type="text/javascript">
var baseurl = '<%=request.getContextPath()%>';
/**
 * 初始化当前页面
 */

$(function(){
 	loadProcessTree();
 	loadProcessPublish();
 	window.setTimeout("expand()", 400);
 	
 	$('#searchForm').find('input').on('keyup',function(e){
		if(e.keyCode == 13){
			searchFun();
		}
	});
});
//绘制页面数据表格
function loadProcessPublish(){
	var dataGridHeight = $(".easyui-layout").height()-85;
	$('#processlist').datagrid({
		toolbar:[
					{
						text:'流程配置',
						iconCls:'icon-edit',
						handler:function(){
							setProcessDeploy();
						}
					}
					,'-',{
						text:'查看流程图',
						iconCls:'icon-search',
						handler:function(){
							lookUpTrackImage();
						}
				
					},'-',{
						text:'加载模板',
						iconCls:'icon-reload',
						handler:function(){
							reloadProcessTemplate();
						}
					}
					,'-',{
						text:'发布模板',
						iconCls:'icon-print',
						handler:function(){
							publishProcessTemplate();
						}
					},'-',{
						text:'删除模板',
						iconCls:'icon-no',
						handler:function(){
							isDelProcessTemplate();
						}
					
					},'-',{
						text:'启用模板',
						iconCls:'icon-redo',
						handler:function(){
							startOrSuspendProcessTemplate(1);
						}
					}
					,'-',{
						text:'停用模板',
						iconCls:'icon-undo',
						handler:function(){
							startOrSuspendProcessTemplate(0);
						}
					},'-',{
						text:'切换业务目录',
						iconCls:'icon-back',
						handler:function(){
							changeProcessCatalog();
						}
					},'-',{
						text:'意见配置',
						iconCls:'icon-add',
						handler:function(){
							setIdeaStyle();
						}
					},'-',{
						text:'启动权限设置',
						iconCls:'icon-edit',
						handler:function(){
							setProcessStartRight();
						}
					}],
		url: 'platform/bpm/bpmPublishAction/getPrcessPublishByPage.json?nodeType=catalog&id=-1',
		width: '100%',
	    nowrap: false,
	    striped: true,
	    autoRowHeight:false,
	    singleSelect:true,
	    checkOnSelect:true,
	    remoteSort : false,
		height: dataGridHeight,
		fitColumns: true,
		//sortName: 'startdate',  //排序字段,当表格初始化时候的排序字段
		//sortOrder: 'desc',    //定义排序顺序
		pagination:true,
		rownumbers:true,
		pageSize: 10,
		queryParams:{"":""},
		onLoadSuccess: function (data) {
             if (data.rows.length > 0) {
                 //调用mergeCellsByField()合并单元格
                 mergeCellsByField("processlist", "processName");
             }
        },
		loadMsg:' 处理中，请稍候…',
		columns:[[
			{field:'id',hidden:true},
			{field:'op',title:'操作',width:25,align:'left',checkbox:true},
			{title:'流程名称',field:'processName',width:80,align:'left'},
			{field:'version',title:'版本号',width:25,align:'left',
				formatter:function(value,rec){
					var temp = rec.processDefId;
					if(temp!=null && temp!=''){
						return temp.split('-')[1];
					}
				}
			},
			{field:'processDefId',title:'流程定义ID',width:50,align:'left'},
			
			{field:'deployId',title:'部署ID',width:50,align:'left'},
			{field:'deployDate',title:'部署时间',width:60,align:'left',
				formatter:function(value,rec){
  					var startdateMi=rec.deployDate;
  					if(startdateMi==undefined){
  						return;
  					}
  					var newDate=new Date(startdateMi);
  					return newDate.Format("yyyy-MM-dd hh:mm:ss");   
				}},
			{field:'deployer',title:'部署人',width:35,align:'left'},
			{field:'designer',title:'设计者',width:35,align:'left',
				formatter:function(value,rec){
  					var designers =rec.designer;
  					if(designers==undefined || designers=='null'){
  						return;
  					}else{
  						return designers;
  					}
				}},
			{field:'state',title:'流程状态',width:25,align:'left'},
			{field:'type',title:'流程类型',width:30,align:'left'}
		]]
	});
	//设置分页控件   
	var p = $('#processlist').datagrid('getPager');
	$(p).pagination({
	    pageSize: 10,//每页显示的记录条数，默认为10
	    pageList: [5,10,15,20,25,30],//可以设置每页记录条数的列表
	    beforePageText: '第',//页数文本框前显示的汉字
	    afterPageText: '页    共 {pages} 页',
	    displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
	});
}
//初始化流程业务树 
function loadProcessTree(){
	$('#mytree').tree({   
		checkbox: false,   
		lines : true,
		method : 'post',
	    url:'platform/bpm/bpmPublishAction/getPrcessPublishTree.json?nodeType=catalog&id=root',  
	    onBeforeExpand:function(node,param){  
	    	 $('#mytree').tree('options').url = "platform/bpm/bpmPublishAction/getPrcessPublishTree.json?nodeType=catalog&id=" + node.id ;
	    },
	    onClick:function(node){
            clickTree(node);
      	}
	});  
}
//点击树事件 
function clickTree(node) {
	expand();
	$('#processlist').datagrid({ url: 'platform/bpm/bpmPublishAction/getPrcessPublishByPage.json?nodeType=catalog&id=' + node.id } );
	$("#processlist").datagrid('reload'); 
}

//展开树
function expand() {
		var node = $('#mytree').tree('getSelected');
		if(node){
			$('#mytree').tree('expand',node.target);
		}else{
			$('#mytree').tree('expandAll');
		}
}

function changeProcessCatalog(){
	var data = $('#processlist').datagrid('getSelected');
	if (data == null || data=='') {
		$.messager.alert("操作提示", "请您选择一条记录！","info"); 
		return;
	}
	var deployId = data.deployId;
	var processDefId = data.processDefId;
	usd = new UserSelectDialog("processCatalogSelectDialog","300","395","avicit/platform6/bpmconsole/publish/ProcessCatalogSelect.jsp?deployId="+deployId+"&processDefId="+processDefId,"流程业务目录");
	usd.show();
}

/**
 * 设置流程的启动权限
 */
function setProcessStartRight(){
	
	var data = $('#processlist').datagrid('getSelected');
	if (data == null || data=='') {
		$.messager.alert("操作提示", "请您选择一条记录！","info"); 
		return;
	}
	var deployment = data.deployId;
	var url = getPath()+"/avicit/platform6/bpmconsole/publish/ProcessStartRight.jsp?deployment="+deployment;
	top.addTab("流程启动权限",url,"dorado/client/skins/~current/common/icons.gif","ProcessStartRight"," -0px -120px");
}
/**
 * 设置流程的意见
 */
function setIdeaStyle(){
	var data = $('#processlist').datagrid('getSelected');
	if (data == null || data=='') {
		$.messager.alert("操作提示", "请您选择一条记录！","info"); 
		return;
	}
	var pdId = data.processDefId;
	var url = getPath()+"/avicit/platform6/bpmconsole/ideastyle/IdeaStyleManager.jsp?pdId="+pdId;
	top.addTab("流程意见设置",url,"dorado/client/skins/~current/common/icons.gif","ProcessIdeaStyle"," -0px -120px");
}

/**
* 根据字段动态合并单元格
* 参数 tableID 要合并table的id
* 参数 colList 要合并的列,用逗号分隔(例如："name,department,office");
*/
function mergeCellsByField(tableID, colList) {
    var ColArray = colList.split(",");
    var tTable = $("#" + tableID);
    var TableRowCnts = tTable.datagrid("getRows").length;
    var tmpA;
    var tmpB;
    var PerTxt = "";
    var CurTxt = "";
    var alertStr = "";
    for (j = ColArray.length - 1; j >= 0; j--) {
        PerTxt = "";
        tmpA = 1;
        tmpB = 0;

        for (i = 0; i <= TableRowCnts; i++) {
            if (i == TableRowCnts) {
                CurTxt = "";
            }
            else {
                CurTxt = tTable.datagrid("getRows")[i][ColArray[j]];
            }
            if (PerTxt == CurTxt) {
                tmpA += 1;
            }
            else {
                tmpB += tmpA;
                
                tTable.datagrid("mergeCells", {
                    index: i - tmpA,
                    field: ColArray[j],//合并字段
                    rowspan: tmpA,
                    colspan: null
                });
                tTable.datagrid("mergeCells", { //根据ColArray[j]进行合并
                    index: i - tmpA,
                    field: "Ideparture",
                    rowspan: tmpA,
                    colspan: null
                });
                tmpA = 1;
            }
            PerTxt = CurTxt;
        }
    }
}
//查看流程图
function lookUpTrackImage() {
	var data = $('#processlist').datagrid('getSelected');
	if (data == null || data=='') {
		$.messager.alert("操作提示", "请您选择一条记录！","info"); 
		return;
	}
	var deploymentId = data.deployId;
	var url = getPath() + "/platform/bpm/bpmPublishAction/getProcessImageTrack.gif";
	url += "?processInstanceId=" + deploymentId;
	var usd = new UserSelectDialog("lct", "700", "500",encodeURI(url), "流程图");
	usd.show();
}
//发布模板
function publishProcessTemplate() {
	var buttons = [];
	buttons.push({
		text:'发布流程模板',
		id : 'addTemplate',
		//iconCls : 'icon-add',
		handler:function(){
			var ifr = jQuery("#fbmb iframe")[0];
			var win = ifr.window || ifr.contentWindow;
			win.publishProcessTemplate(); // 调用iframe中的a函数
		}
	});
	var url = getPath() + "/avicit/platform6/bpmconsole/publish/ProcessPublishPage.jsp";
	var usd = new UserSelectDialog("fbmb", "700", "500",encodeURI(url), "发布流程模板", null, buttons);
	usd.show();
}
//加载模板
function reloadProcessTemplate(){
	var data = $('#processlist').datagrid('getSelected');
	if (data == null || data=='') {
		$.messager.alert("操作提示", "请您选择一条记录！","info"); 
		return;
	}
	var deploymentId = data.deployId;
	$.messager.confirm("操作提示", "您确定要加载流程吗?", function(data) {
		if (data) {
			easyuiMask();
			ajaxRequest("POST", "deploymentId=" + deploymentId,
					"platform/bpm/bpmPublishAction/reloadProcessDefinition", "json",
					"backChecked");
		}
	});
	
}
//判断是否能删除流程模板
function isDelProcessTemplate(){
	var data = $('#processlist').datagrid('getSelected');
	if (data == null || data=='') {
		$.messager.alert("操作提示", "请您选择一条记录！","info"); 
		return;
	}
	var processDefId = data.processDefId;
	var deploymentId = data.deployId;
	easyuiMask();
	ajaxRequest("POST", "processDefId=" + processDefId + "&deploymentId=" + deploymentId,"platform/bpm/bpmPublishAction/isDeleteProcessDefinitionById", "json","backDel");
}

function backDel(obj){
	if (obj != null && obj.flag == true) {
		var deploymentId = obj.deploymentId; 
		$.messager.confirm("操作提示", "您确定要删除吗?", function(data) {
			if (data) {
				ajaxRequest("POST", "deploymentId=" + deploymentId,"platform/bpm/bpmPublishAction/deleteProcessDefinitionById", "json","backChecked");
			}
			easyuiUnMask();
		});
	} else {
		easyuiUnMask();
		$.messager.alert("操作提示","请先删除该流程模板的流程实例后，再删除模板！", "info");
	}
}

//启动或者停用流程模板 isFlag 表示是启用还是停用 1表示停用  0表示挂起
function startOrSuspendProcessTemplate(isFlag){
	var data = $('#processlist').datagrid('getSelected');
	if (data == null || data=='') {
		$.messager.alert("操作提示", "请您选择一条记录！","info"); 
		return;
	}
	var deploymentId = data.deployId;
	
	$.messager.confirm("操作提示", "您确定要执行吗?", function(data) {
		if (data) {
			easyuiMask();
			ajaxRequest("POST", "deploymentId=" + deploymentId + "&isFlag=" + isFlag,"platform/bpm/bpmPublishAction/startOrSuspendedProcessTemplate", "json","backChecked");
		}
	});
	
}
function backChecked(obj) {
	easyuiUnMask();
	if (obj != null && obj.success == true) {
		$.messager.show({
			title : '提示',
			msg : "操作成功！"
		});
		$('#processlist').datagrid('reload');
	} else {
		$.messager.show({
			title : '提示',
			msg : "操作失败！"
		});
	}
}
/**
 * 设置流程配置
 */
function setProcessDeploy(){
	var data = $('#processlist').datagrid('getSelected');
	if (data == null || data=='') {
		$.messager.alert("操作提示", "请您选择一条记录！","info"); 
		return;
	}
	var deploymentId = data.deployId;
	var processDefId = data.processDefId;
	var url = getPath()+"/avicit/platform6/bpmconsole/publish/ProcessDeployPage.jsp?deploymentId="+deploymentId+"&processDefId="+processDefId;
	top.addTab("流程配置管理",url,"dorado/client/skins/~current/common/icons.gif","ProcessStartRight"," -0px -120px");
}

//查询
function searchFun(){
	var queryParams = $('#processlist').datagrid('options').queryParams;  
    queryParams.processDefId = $("#processDefId").attr("value");
    queryParams.processName = $("#processName").attr("value");
    queryParams.startDateBegin = $('#startDateBegin').datetimebox('getValue');
    queryParams.startDateEnd = $('#startDateEnd').datetimebox('getValue');
    queryParams.userId = $("#receptUserId").val();
    $('#processlist').datagrid('options').queryParams=queryParams;     
    $("#processlist").datagrid('reload'); 
}
//查询选项重置
function clearFun(){
	$('#searchForm input').val('');
	$('#searchForm select').val('');
}

/**
 *选择人员及部门
 */
function selectUserDept() {
	/**
	var usd = new UserSelectDialog('userSelectCommonDialog', 700, 400,
			getPath() + '/platform/user/bpmSelectUserAction/userSelectCommon',
			'选择人员部门');
	var buttons = [ {
		text : '确定',
		id : 'processSubimt',
		//iconCls : 'icon-submit',
		handler : function() {
			var frmId = $('#userSelectCommonDialog iframe').attr('id');
			var frm = document.getElementById(frmId).contentWindow;
			var resultDatas = frm.getSelectedResultDataJson();

			for ( var i = 0; i < resultDatas.length; i++) {
				var resultData = resultDatas[i];
				$("#receptUserId").val(resultData.userId);
				$("#receptUserName").val(resultData.userName);
			}
			usd.close();
		}
	} ];
	usd.createButtonsInDialog(buttons);
	usd.show();
	*/
	
	
	/**
	var usd = new CommonDialog("userAddDialog","800","500",getPath()+"/avicit/platform6/modules/system/sysgroup/addUser.jsp","选择用户");
	var buttons = [{
		text:'确定',
		id : 'formSubimt',
		//iconCls : 'icon-submit',
		handler:function(){
			 var frmId = $('#userAddDialog iframe').attr('id');
			 var frm = document.getElementById(frmId).contentWindow;
			 var myDatagrid=frm.$('#dgUser');
			 var rows = myDatagrid.datagrid('getChecked');
			 if(rows.length==0)
			 {
				 $.messager.alert('提示',"请选择用户",'warning');
				 return;
			 }else{
				for ( var i = 0; i < rows.length; i++) {
					var resultData = rows[i];
					$("#receptUserId").val(resultData.id);
					$("#receptUserName").val(resultData.name);
				}
				usd.close();
			 }
		}
	}];
	usd.createButtonsInDialog(buttons);
	usd.show();
	*/
	var comSelector = new CommonSelector("user","userSelectCommonDialog","receptUserId","receptUserName",null,null,null,null,null,null,600,400);
	comSelector.init(false,'selectUserDialogCallBack','n'); //选择人员  回填部门 */
	
}
function selectUserDialogCallBack(data){
	$("#receptUserId").val(data[0].userId);
	$("#receptUserName").val(data[0].userName);
}
</script>
<body class="easyui-layout" fit="true">
<div data-options="region:'west',title:'流程发布管理',split:true,collapsible:false"  style="width:200px;overflow: auto;">
	<ul id="mytree"> </ul>  
</div>  
<div region="center" border="false" style="overflow: hidden;">
<div id="toolbar" class="datagrid-toolbar" style="height:auto;display: block;">
			<fieldset>
				<legend>查询条件</legend>
				<table class="tableForm" id="searchForm" width='100%'>
					<tr>
						<td>流程模板名称：</td>
						<td colspan="2"><input name="processName" id="processName"  editable="false" style="width: 150px;" />
						</td>
						<td>流程定义ID：</td>
						<td colspan="2"><input name="processDefId" id="processDefId"  editable="false" style="width: 150px;" />
						</td>
						<td>部署人：</td>
						<td colspan="2"><input name="receptUserId" id="receptUserId"  style="display:none;" /><input class="easyui-validatebox"  name="receptUserName"   id="receptUserName" required="true"></input>
						<img  src="static/images/platform/bpm/images/addUserDept.gif" title="选择人员部门" onclick="selectUserDept();" style="align:center;"/>
						</td>
					</tr>
					<tr>
						<td>开始时间：</td>
						<td colspan="2"><input name="startDateBegin" id="startDateBegin" class="easyui-datebox" editable="false" style="width:150px;" />
						</td>
						<td>结束时间：</td>
						<td colspan="2"><input name="startDateEnd" id="startDateEnd"  class="easyui-datebox" editable="false" style="width: 150px;" />
						</td>
						<td style="width:50px"></td>
						<td colspan="2"><a class="easyui-linkbutton"   iconCls="icon-search" plain="true" onclick="searchFun();" href="javascript:void(0);">查询</a><a class="easyui-linkbutton" iconCls="icon-cancel" plain="true" onclick="clearFun();" href="javascript:void(0);">清空</a>
						</td>
					</tr>
				</table>
			</fieldset>
		</div>
	<table id="processlist"></table>
</div>
</div>
</body>
</html>