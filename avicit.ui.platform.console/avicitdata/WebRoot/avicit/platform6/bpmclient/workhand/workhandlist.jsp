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
<title>工作移交</title>
<base href="<%=ViewUtil.getRequestPath(request) %>">
<jsp:include page="/avicit/platform6/bpmclient/bpm/ProcessCommonJsInclude.jsp"></jsp:include>

</head>

<script type="text/javascript">
var baseurl = '<%=request.getContextPath()%>';
/**
 * 初始化当前页面
 */
 
$(function(){
	
	loadWorkHand();
	
 });


function doworkHand() {
	$('#workhandDialog').dialog('open');
}

function loadWorkHand(){
	var dataGridHeight = $(".easyui-layout").height() - 60;
	$('#workhandlist').datagrid({
		toolbar:[
			{
			id : 'save',
			text:'工作移交',
			iconCls:'icon-add',
			handler:function(){
				//判断是否存在有效工作移交
				ajaxRequest("POST","","platform/bpm/clientbpmWorkHandAction/checkBeforeWorkHand","json","checkBeforeDoWorkhand");
			}
		},'-',{
			text:'注销移交',
			iconCls:'icon-remove',
			handler:function(){
				//判断是否存在待注销的工作移交
			   
				ajaxRequest("POST","","platform/bpm/clientbpmWorkHandAction/checkBeforeWorkHand","json","checkBeforeCancelWorkhand");
			}
		}],
		url: 'platform/bpm/clientbpmWorkHandAction/getSysWorkHandListByPage.json',
		width: '100%',
	    nowrap: false,
	    striped: true,
	    autoRowHeight:false,
	    singleSelect:true,
	    checkOnSelect:false,
	    remoteSort : false,
		height: dataGridHeight,
		fitColumns: true,
		sortName: 'handDate',  //排序字段,当表格初始化时候的排序字段
		sortOrder: 'asc',    //定义排序顺序
		pagination:true,
		rownumbers:true,
		queryParams:{"filter_EQ_workOwnerId":""},
		loadMsg:' 处理中，请稍候…',
		columns:[[
			{field:'workOwnerName',title:'移交人',width:25,align:'left',hidden:true},
			{field:'workOwnerDeptName',title:'移交部门',width:25,align:'left',sortable:true},
			{field:'receptUserName',title:'接受人',width:25,align:'left',sortable:true},
			{field:'receptDeptName',title:'接受人部门',width:25,align:'left',sortable:true},
			{field:'handReason',title:'移交原因',width:80,align:'left',sortable:true},
			{field:'handEffectiveDate',title:'生效日期',width:25,align:'left',sortable:true,
  				formatter:function(value,rec){
  					var handEffectiveDateMi=rec.handEffectiveDate;
  					var newDate=new Date(handEffectiveDateMi);
  					return newDate.Format("yyyy-MM-dd");   
				}},
			{field:'backDate',title:'预计返回日期',width:25,align:'left',editor:'text',
  				formatter:function(value,rec){
  					var backDateMi=rec.backDate;
  					var newDate=new Date(backDateMi);
  					return newDate.Format("yyyy-MM-dd");   
				}},
			{field:'handDate',title:'登记日期',width:25,align:'left',
	  				formatter:function(value,rec){
	  					var handDateMi=rec.handDate;
	  					var newDate=new Date(handDateMi);
	  					return newDate.Format("yyyy-MM-dd");   
					}},
			{field:'validFlag',title:'是否有效',width:15,align:'left',
		  				formatter:function(value,rec){
		  					var validFlag=rec.validFlag;
		  					if(validFlag=='1'){
		  						return "有效";
		  					}else{
		  						return "无效";
		  					}
						}}
		]]
	});
	//设置分页控件   
	var p = $('#workhandlist').datagrid('getPager');
	$(p).pagination({
	    pageSize: 10,//每页显示的记录条数，默认为10
	    pageList: [5,10,15,20,25,30],//可以设置每页记录条数的列表
	    beforePageText: '第',//页数文本框前显示的汉字
	    afterPageText: '页    共 {pages} 页',
	    displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
	});
}

function WorkHandback(obj){
	loadWorkHand();
}
var workhandDialog=null;
/**
 * 工作移交前检查是否存在未注销的工作移交的回调函数
 */
function  checkBeforeDoWorkhand(checkResult){
	if(checkResult.checkResult=='0'){
		var usd = new UserSelectDialog("workhandDialog","626","395","avicit/platform6/bpmclient/workhand/workhandAdd.jsp","工作移交");
		workhandDialog = usd;
		var buttons = [{
			text:'提交',
			id : 'processSubimt',
			//iconCls : 'icon-submit',
			handler:function(){
				 var frmId = $('#workhandDialog iframe').attr('id');
				 var frm = document.getElementById(frmId).contentWindow;
				 var dataVo = frm.$('#workhandForm').serializeArray();
				 var dataJson =frm.convertToJson(dataVo);
				 dataVo = JSON.stringify(dataJson);
				 ajaxRequest("POST","dataVo="+dataVo,"platform/bpm/clientbpmWorkHandAction/doWorkHand","json","doWorkHandBack");
			}
		}];
		usd.createButtonsInDialog(buttons);
		usd.show();
	}else{
		$.messager.alert("操作提示", "您还有未注销的工作移交，请注销后再进行移交操作！","info");
	}
}

/**
 * 工作移交完成后的回调函数
 */
 function doWorkHandBack(result){
	 try {
			if (result.success) {
				$('#workhandlist').datagrid('insertRow', {
					index : 0,
					row : result.obj
				});
			}
			//$.messager.alert("提示",result.msg+"","info");
			$.messager.show({
				title : '提示',
				msg : result.msg+""
			});
			workhandDialog.close();
			workhandDialog=null;
		} catch (e) {
			$.messager.alert('提示', result+"");
		}
	}

var  cancelWorkhandDialog=null;
/**
 * 注销工作移交前检查是否存在待注销的工作的回调函数
 */
 function  checkBeforeCancelWorkhand(checkResult){
			if(checkResult.checkResult=='1'){
				var usd = new UserSelectDialog("cancelWorkhandDialog","700","450","avicit/platform6/bpmclient/workhand/processWorkHandTask.jsp","工作移交注销");
				cancelWorkhandDialog = usd;
				var buttons = [{
					text:'提交',
					id : 'processSubimt',
					//iconCls : 'icon-submit',
					handler:function(){
						$.messager.confirm("操作提示", "您确认要注销选定的数据吗？", function (data) {
				            if (data) {
				            	var frmId = $('#cancelWorkhandDialog iframe').attr('id');
				 				var frm = document.getElementById(frmId).contentWindow;
				 				var rows =frm.$('#WorkHandTasklist').datagrid('getChecked');
				 				var deleteRowArr = [];
				 				if(rows.length > 0){
				 					for ( var i = 0; i < rows.length; i++) {
				 						deleteRowArr.push(rows[i].dbid+"@@"+rows[i].assignee);
									}
				 				}
				            	ajaxRequest("POST","deleteRows="+deleteRowArr.join(','),"platform/bpm/clientbpmWorkHandAction/deleteSysWorkHandPass","json","cancelWorkhandBack");
				            }else {
				            	usd.close();
				            }
				        });
					}
				}];
				usd.createButtonsInDialog(buttons);
				usd.show();	
			}else{
				$.messager.alert("操作提示", "没有可以注销的工作移交！","info");
			}
   }
/**
 * 工作注销回调函数
 */
function cancelWorkhandBack(retResult){
	if(retResult.result=='1'){
		var queryParams = $('#workhandlist').datagrid('options').queryParams;  
        queryParams.filter_EQ_validFlag =$('#filter_EQ_validFlag').val(); 
        queryParams.filter_EQ_handEffectiveDate =$('#filter_EQ_handEffectiveDate').val(); 
        $('#workhandlist').datagrid('options').queryParams=queryParams;        
        $("#workhandlist").datagrid('reload'); 
        $.messager.show({
			title : '提示',
			msg : "注销成功!"
		});
		//$.messager.alert("操作提示", "注销成功!","info");
		cancelWorkhandDialog.close();
		cancelWorkhandDialog=null;
	}else{
		$.messager.alert("操作提示", "注销失败!","info");
	}
}
/**
 *选择人员及部门
 */
function selectUserDept(){
		var usd = new UserSelectDialog('innerAddressDialog',700,400,getPath() + '/platform/user/bpmSelectUserAction/userSelectCommon?isMultiple=true','选择人员部门');
		var buttons = [{
			text:'确定',
			id : 'processSubimt',
			//iconCls : 'icon-submit',
			handler:function(){
				var frmId = $('#innerAddressDialog iframe').attr('id');
				var frm = document.getElementById(frmId).contentWindow;
				var resultDatas = frm.getSelectedResultDataJson();
				var frmResultId = $('#workhandDialog iframe').attr('id');
				var frmResult = document.getElementById(frmResultId).contentWindow;
				for(var i = 0 ; i < resultDatas.length ; i++){
					var resultData = resultDatas[i];
					frmResult.$("#receptUserId").val(resultData.userId);
					frmResult.$("#receptUserName").val(resultData.userName);
					frmResult.$("#receptDeptId").val(resultData.deptId);
					frmResult.$("#receptDeptName").val(resultData.deptName);
				}
				usd.close();
			}
		}];
		usd.createButtonsInDialog(buttons);
		usd.show();
	}
//查询
function searchFun(){
	var queryParams = $('#workhandlist').datagrid('options').queryParams;  
    queryParams.filter_EQ_validFlag =$('#toolbar select[comboname=filter_EQ_validFlag]').combobox('getValue');
    queryParams.filter_LE_handEffectiveDate =$('#toolbar input[comboname=filter_LE_handEffectiveDate]').datetimebox('getValue');
    queryParams.filter_GE_handEffectiveDate =$('#toolbar input[comboname=filter_GE_handEffectiveDate]').datetimebox('getValue');
    $('#workhandlist').datagrid('options').queryParams=queryParams;        
    $("#workhandlist").datagrid('load'); 
}
function clearFun(){
	$('#searchForm input').val('');
	$('#searchForm select').val('');
}
</script>
<body class="easyui-layout" fit="true">
<div region="center" border="false" style="overflow: hidden;">
		<div id="toolbar" class="datagrid-toolbar" style="height: auto;display: block;">
			<fieldset>
				<legend>查询条件</legend>
				<table class="tableForm" id="searchForm">
					<tr>
					    <td style="width:50px"></td>
						<td>生效日期(起)</td>
						<td colspan="2"><input name="filter_GE_handEffectiveDate" id="filter_GE_handEffectiveDate"  class="easyui-datebox" editable="false" style="width: 150px;" />
						</td>
						<td style="width:50px"></td>
						<td>生效日期(止)</td>
						<td colspan="2"><input name="filter_LE_handEffectiveDate" id="filter_LE_handEffectiveDate"  class="easyui-datebox" editable="false" style="width: 150px;" />
						</td>
						<td style="width:50px"></td>
						<td>是否有效</td>
						<td ><select name="filter_EQ_validFlag" id="filter_EQ_validFlag" class="easyui-combobox" style="width: 156px;" >
						       <option value=""></option>
						       <option value="1">有效</option>
							   <option value="0">无效</option>
						     </select>
						</td>
						<td style="width:50px"></td>
						<td ><a class="easyui-linkbutton"   iconCls="icon-search" plain="true" onclick="searchFun();" href="javascript:void(0);">查询</a><a class="easyui-linkbutton" iconCls="icon-cancel" plain="true" onclick="clearFun();" href="javascript:void(0);">清空</a>
						</td>
					</tr>
				</table>
			</fieldset>
		</div>
		<table id="workhandlist"></table>
</div>

</body>
</html>