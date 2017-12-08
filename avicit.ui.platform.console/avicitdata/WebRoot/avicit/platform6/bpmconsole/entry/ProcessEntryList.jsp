<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<html>
<head>
<%
	String path = request.getContextPath();
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>流程实例信息</title>
<base href="<%=ViewUtil.getRequestPath(request) %>">
<jsp:include page="/avicit/platform6/bpmclient/bpm/ProcessCommonJsInclude.jsp"></jsp:include>
<jsp:include page="/avicit/platform6/modules/system/commonpopup/commonSelectionHead.jsp"></jsp:include>
<script src="static/js/platform/bpm/ButtonProcessing.js" type="text/javascript"></script>
</head>

<script type="text/javascript">
var baseurl = '<%=request.getContextPath()%>';
var processDefinitionId = false;
/**
 * 初始化当前页面
 */
$(function(){
	loadProcessTree();
	loadProcessEntry();
	window.setTimeout("expand()", 400);
	
	$('#searchForm').find('input').on('keyup',function(e){
		if(e.keyCode == 13){
			searchFun();
		}
	});
});
//绘制页面数据表格
function loadProcessEntry(){
	var dataGridHeight = $(".easyui-layout").height() - 85;
	$('#processentrylist').datagrid({
		toolbar:[
				{
					text:'挂起',
					iconCls:'icon-undo',
					handler:function(){
						//将当前选择流程实例挂起
						checkIsSelect('suspend');
					}
				},'-',{
					text:'恢复',
					iconCls:'icon-back',
					handler:function(){
						//将当前选择流程实例恢复运行
						checkIsSelect('recover');
					}
				},'-',{
					text:'删除',
					iconCls:'icon-no',
					handler:function(){
						//将当前选择流程实例删除
						checkIsSelect('delete');
					}
				},'-',{
					text:'创建流程实例',
					iconCls:'icon-add',
					handler:function(){
						//新建流程实例
						if(processDefinitionId){
							var obj =	[{"dbid":processDefinitionId}];
							var url = getPath()+"/avicit/platform6/bpmconsole/entry/NewProcessEntry.jsp";
							url += "?processDefinitionId="+processDefinitionId;
							var usd = new UserSelectDialog("doStartProcess","500","350",encodeURI(url) ,"新启流程窗口");
							usd.show();
						}else{
						    $.messager.alert("操作提示", "请选择要启动的流程！","info"); 
						}
						
					}
				}],
		url: 'platform/bpm/bpmConsoleAction/getPrcessEntryListByPage.json?nodeType=catalog&id=-1',
		width: '100%',
	    nowrap: false,
	    striped: true,
	    autoRowHeight:false,
	    singleSelect:true,
	    checkOnSelect:true,
	    selectOnCheck: false,
	    remoteSort : false,
		height: dataGridHeight,
		fitColumns: true,
		sortName: 'startdate',  //排序字段,当表格初始化时候的排序字段
		sortOrder: 'desc',    //定义排序顺序
		pagination:true,
		rownumbers:true,
		pageSize: 10,
		queryParams:{"":""},
		loadMsg:' 处理中，请稍候…',
		columns:[[
			{field:'executionid',hidden:true},
			{field:'op',title:'操作',width:25,align:'left',checkbox:true},
			{field:'businessName',title:'业务目录',width:50,align:'left'},
			{field:'entryid',title:'流程实例ID',width:50,align:'left',sortable:true,
				formatter:function(value,rec){
  					var processInstance = "'"+rec.entryid+"'";
  					var state = "'"+rec.state+"'";
  					var id =  "'"+rec.executionid+"'";
  					var pdid =  "'"+rec.pdid+"'";
  					return '<a href="javascript:window.executeTask('+processInstance+','+ state + ',' + id + ',' + pdid +')">'+value+'</a>';
  				}},
			{field:'entryname',title:'流程实例名称',width:50,align:'left',sortable:true},
			{field:'definename',title:'流程定义名称',width:75,align:'left',sortable:true},
			{field:'state',title:'状态',width:25,align:'left',sortable:true,
				formatter:function(value){
					 if(value=='active'){
	                        return '流转中';
	                    }else if(value=='ended'){
	                        return '结束';
	                    }else if(value=='suspended'){
	                        return '挂起';
	                    }

				}
				},
			{field:'userid',title:'创建者',width:25,align:'left',sortable:true},
			{field:'startdate',title:'启动时间',width:50,align:'left',sortable:true,
  				formatter:function(value,rec){
  					var startdateMi=rec.startdate;
  					if(startdateMi==undefined){
  						return;
  					}
  					var newDate=new Date(startdateMi);
  					return newDate.Format("yyyy-MM-dd hh:mm:ss");   
				}},
			{field:'enddate',title:'结束时间',width:50,align:'left',editor:'text',
  				formatter:function(value,rec){
  					var endateMi=rec.enddate;
  					if(endateMi==undefined){
  						return;
  					}
  					var newDate=new Date(endateMi);
  					return newDate.Format("yyyy-MM-dd hh:mm:ss");   
				}}
		]]
	});
	//设置分页控件   
	var p = $('#processentrylist').datagrid('getPager');
	$(p).pagination({
	    pageSize: 10,//每页显示的记录条数，默认为10
	    pageList: [5,10,15,20,25,30],//可以设置每页记录条数的列表
	    beforePageText: '第',//页数文本框前显示的汉字
	    afterPageText: '页    共 {pages} 页',
	    displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
	});
}

//查询
function searchFun(){
	var queryParams = $('#processentrylist').datagrid('options').queryParams;  
    queryParams.state =$('#toolbar select[comboname=state]').combobox('getValue');
    queryParams.entryid = $("#entryid").attr("value");
    queryParams.entryname = $("#entryname").attr("value");
    queryParams.definename = $("#definename").attr("value");
    queryParams.startDateBegin = $('#startDateBegin').datetimebox('getValue');
    queryParams.startDateEnd = $('#startDateEnd').datetimebox('getValue');
    queryParams.userId = $("#receptUserId").val();
    $('#processentrylist').datagrid('options').queryParams=queryParams;     
    $("#processentrylist").datagrid('reload'); 
}
//查询选项重置
function clearFun(){
	$('#searchForm input').val('');
	$('#searchForm select').val('');
}
//查看流程实例详细信息
function executeTask(entryId,state,id,pdid){
	var url = "avicit/platform6/bpmconsole/entry/ProcessConsoleTrack.jsp";
	url += "?processInstanceId="+entryId + "&state=" + state  + "&id=" + id + "&pdid=" + pdid ;
	try{
   		if(typeof(eval(top.addTab))=="function"){
   			top.addTab("流程实例信息",encodeURI(url),"dorado/client/skins/~current/common/icons.gif","taskProcess"," -0px -120px");
   		}else{
   			window.open(url);
   		} 
   	}catch(e){}
}
//初始化流程业务树 
function loadProcessTree(){
	$('#mytree').tree({   
		checkbox: false,   
		lines : true,
		method : 'post',
	    url:'platform/bpm/bpmConsoleAction/getPrcessEntryTree.json?nodeType=catalog&id=root',  
	    onBeforeExpand:function(node,param){  
	    	 $('#mytree').tree('options').url = "platform/bpm/bpmConsoleAction/getPrcessEntryTree.json?nodeType=catalog&id=" + node.id ;
	    },
	    onClick:function(node){
            clickTree(node);
      	}
	});  
}
//点击树事件 
function clickTree(node) {
	expand();
	var nodeType = node.attributes.nodeType;
	//处理启动流程时判断变量
	if(nodeType == 'process'){
		var keyId = node.attributes.keyId;
		processDefinitionId = keyId + "-1";
	}
	if(nodeType == 'subprocess'){
		processDefinitionId = node.id;
	}
	if(nodeType == 'catalog'){
		$('#processentrylist').datagrid({ url: 'platform/bpm/bpmConsoleAction/getPrcessEntryListByPage.json?nodeType=catalog&id=' + node.id } );
	}else if(nodeType == 'process')
	{
		$('#processentrylist').datagrid({ url: 'platform/bpm/bpmConsoleAction/getPrcessEntryListByPage.json?nodeType=process&id=' + node.id } );
	}else{
		$('#processentrylist').datagrid({ url: 'platform/bpm/bpmConsoleAction/getPrcessEntryListByPage.json?nodeType=subprocess&id=' + node.id } );
	}
	var queryParams = $('#processentrylist').datagrid('options').queryParams;  
    $('#processentrylist').datagrid('options').queryParams=queryParams;  
	$("#processentrylist").datagrid('reload'); 
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

	function checkBeforeSuspend(id) {
		$.messager.confirm("操作提示", "您确认要挂起流程实例吗?", function(data) {
			if (data) {
				easyuiMask();
				ajaxRequest("POST", "processInstanceId=" + id,
						"platform/bpm/bpmConsoleAction/supProcessEntry",
						"json", "backFinished");
			}
		});
	}

	function checkBeforeRecover(id) {
		$.messager.confirm("操作提示", "您确认要恢复流程实例吗?", function(data) {
			if (data) {
				easyuiMask();
				ajaxRequest("POST", "processInstanceId=" + id,
						"platform/bpm/bpmConsoleAction/recoverProcessEntry",
						"json", "backFinished");
			}
		});
	}
	function checkBeforeDelete(id) {
		$.messager.confirm("操作提示", "您确认要删除流程实例吗?", function(data) {
			if (data) {
				easyuiMask();
				ajaxRequest("POST", "processInstanceId=" + id,
						"platform/bpm/bpmConsoleAction/deleteProcessEntry",
						"json", "backFinished");
			}
		});
	}
	//验证是否选择有效的条目
	function checkIsSelect(type) {
		//var datas = $('#processentrylist').datagrid('getSelections');
		var datas = $('#processentrylist').datagrid('getChecked');
		if (datas == null || datas=='') {
			$.messager.alert("操作提示", "请您选择一条记录！","info"); 
			return;
		}
		for ( var i = 0; i < datas.length; i++) {
			var temp = datas[i].state;
			if ('delete' != type) {
				if (temp == 'ended') {
					$.messager.alert("操作提示", "请选择流转中的流程！","info"); 
					return;
				}
			}
		}
		var entryIds = '';
		;
		var ids = '';
		for ( var i = 0; i < datas.length; i++) {
			entryIds += datas[i].entryid + ',';
			ids += datas[i].executionid + ',';
		}
		if ('suspend' == type) {
			checkBeforeSuspend(entryIds);
		}
		if ('recover' == type) {
			checkBeforeRecover(entryIds);
		}
		if ('delete' == type) {
			checkBeforeDelete(ids);
		}
	}
	function backFinished(obj) {
		easyuiUnMask();
		if (obj != null && obj.success == true) {
			$.messager.show({
				title : '提示',
				msg : "操作成功！"
			});
			//显示第一页数据
			//alert($("#processentrylist").datagrid("options").pageNumber);
			//alert($("#processentrylist").datagrid('getPager').pagination.defaults.pageNumber);//
			$("#processentrylist").datagrid('reload');
		} else {
			$.messager.show({
				title : '提示',
				msg : "操作失败！"
			});
		}
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
		var comSelector = new CommonSelector("user","userSelectCommonDialog","receptUserId","receptUserName",null,null,null,null,null,null,600,400);
		comSelector.init(false,'selectUserDialogCallBack','n'); //选择人员  回填部门 */
	}
	function selectUserDialogCallBack(data){
		$("#receptUserId").val(data[0].userId);
		$("#receptUserName").val(data[0].userName);
	}
</script>
<body class="easyui-layout" fit="true">


<div data-options="region:'west',title:'流程实例监控',split:true,collapsible:false"  style="width:200px;overflow: auto;">
	<ul id="mytree"> </ul>  
</div>  
<div region="center" border="false" style="overflow: hidden;">
		<div id="toolbar" class="datagrid-toolbar" style="height:auto;display: block;">
			<fieldset>
				<legend>查询条件</legend>
				<table class="tableForm" id="searchForm" width='100%'>
					<tr>
						<td >流程实例ID：</td>
						<td colspan="2"><input name="entryid" id="entryid" editable="false" style="width: 150px;" />
						<td>流程实例名称：</td>
						<td colspan="2"><input name="entryname" id="entryname"  editable="false" style="width: 150px;" />
						</td>
						<td>流程定义名称：</td>
						<td colspan="2"><input name="definename" id="definename"  editable="false" style="width: 150px;" />
						</td>
						<td>状态：</td>
						<td colspan="2"><select name="state" id="state" class="easyui-combobox" style="width: 150px;" >
						       <option value="">全部</option>
						       <option value="active">流转中</option>
						       <option value="suspended">挂起</option>
							   <option value="ended">结束</option>
						     </select>
						</td>
					</tr>
					
					<tr>
						<td>创建者：</td>
						<td colspan="2"><input name="receptUserId" id="receptUserId"  style="display:none;" /><input class="easyui-validatebox"  name="receptUserName"   id="receptUserName" required="true"></input>
					<img  src="static/images/platform/bpm/images/addUserDept.gif" title="选择人员部门" onclick="selectUserDept();" style="align:center;"/>
						</td>
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
		<table id="processentrylist"></table>
</div>
</div>
</body>
</html>