<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<%
%>
<base href="<%=ViewUtil.getRequestPath(request) %>">
<jsp:include page="/avicit/platform6/bpmclient/bpm/ProcessCommonJsInclude.jsp"></jsp:include>
</head>
<script type="text/javascript">
var baseurl = '<%=request.getContextPath()%>';
var hideIndexs = new Array();
$(function(){
    $('#ss').searchbox({
        width: 200,
        searcher: function (value) {
            hideIndexs.length = 0;
            $("#processForm").datagrid('reload');
            if (value == "请输入查询内容") {
                value = "";
            }
            //停止datagrid的编辑.
            endEdit();
            var rows = $("#processForm").datagrid("getRows");
            var cols = $("#processForm").datagrid("options").columns[0];
            for (var i = rows.length - 1; i >= 0; i--) {
                var row = rows[i];
                var isMatch = false;
                for (var j = 0; j < cols.length; j++) {

                    var pValue = row[cols[j].field];
                    if (pValue == undefined) {
                        continue;
                    }
                    if (pValue.toString().indexOf(value) >= 0) {
                        isMatch = true;
                        break;
                    }
                }
                if (!isMatch){
                	hideIndexs.push(i);
                	$("#processForm").datagrid("refreshRow", i);
                }
            }
        },
        prompt: "请输入查询内容"
    });
    function endEdit() {
        var rows = $("#processForm").datagrid("getRows");
        for (var i = 0; i < rows.length; i++) {
            $("#processForm").datagrid("endEdit", i);
        }
    }
    var dataGridHeight = $(".easyui-layout").height() - 85;
	$('#processForm').datagrid({
		url: 'platform/bpm/bpmPublishAction/getProcessForms.json',
		nowrap: false,
	    striped: true,
	    autoRowHeight:false,
	    singleSelect: true,
		checkOnSelect: true,
		selectOnCheck: false,
	    remoteSort : false,
		height: dataGridHeight,
		fitColumns: true,
        rowStyler: function(index,row){
        	if(hideIndexs.length>0){
        		for(var i=0;i<hideIndexs.length;i++){
        			var temp = hideIndexs[i];
        			if(temp==index){
        				return "display:none";
        			}
        		}
        	}
    	},
        collapsible:true,
        remoteSort: false,
        pagination:false,
        rownumbers:true,
        columns:[[
				  {field:'id',hidden:true},
				  {field:'op',title:'操作',width:25,align:'left',checkbox:true},
				  {field:'formName',title:'表单名称',width:80,rowspan:2},
                  {field:'formCode',title:'表单代码',width:80,rowspan:2},
                  {field:'formUrl',title:'表单URL',width:300}
              ]]
	});
});


var usd;
function addProcessForm(){
	usd = new UserSelectDialog("processFormAddDialog","626","395","avicit/platform6/bpmconsole/publish/ProcessFormAdd.jsp","流程表单");
	var buttons = [{
		text:'提交',
		id : 'processFormSubimt',
		//iconCls : 'icon-submit',
		handler:function(){
			 var frmId = $('#processFormAddDialog iframe').attr('id');
			 var frm = document.getElementById(frmId).contentWindow;
			 var dataVo = frm.$('#form1').serializeArray();
			 var _code = frm.$('#formCode').val().trim();
			 var _name = frm.$('#formName').val().trim();
			 var _url = frm.$('#formUrl').val().trim();
			 if(_code == "" ||  _name == "" || _url == ""){
				 $.messager.show({title : '提示',msg : "【表单代码】【表单名称】【表单URL】为必填项。"});
				 return;
			 }
			 var dataJson =frm.convertToJson(dataVo);
			 dataVo = JSON.stringify(dataJson);
			 ajaxRequest("POST","dataVo="+dataVo,"platform/bpm/bpmPublishAction/addProcessForm","json","addProcessFormBack");
		}
	}];
	usd.createButtonsInDialog(buttons);
	usd.show();
}
function addProcessFormBack(result){
	if(result.flag=="ok"){
		$('#processForm').datagrid('reload');	
		$.messager.show({title : '提示',msg : "保存成功。"});
		usd.close();
		usd=null;
	}else{
		$.messager.show({title : '提示',msg : "保存失败。"});
	}
}
function editProcessForm(){
	var rows = $("#processForm").datagrid('getChecked');
	if(rows.length<=0){
		$.messager.alert('提示',"请先择表单！",'warning');
		return;
	}
	if(rows.length>1){
		$.messager.alert('提示',"一次只能编辑一条记录！",'warning');
		return;
	}
	usd = new UserSelectDialog("processFormEditDialog","626","395","platform/bpm/bpmPublishAction/toProcessFormEdit?id="+rows[0].id,"流程表单");
	var buttons = [{
		text:'提交',
		id : 'processFormSubimt',
		//iconCls : 'icon-submit',
		handler:function(){
			 var frmId = $('#processFormEditDialog iframe').attr('id');
			 var frm = document.getElementById(frmId).contentWindow;
			 var dataVo = frm.$('#form1').serializeArray();
			 var _code = frm.$('#formCode').val().trim();
			 var _name = frm.$('#formName').val().trim();
			 var _url = frm.$('#formUrl').val().trim();
			 if(_code == "" ||  _name == "" || _url == ""){
				 $.messager.show({title : '提示',msg : "【表单代码】【表单名称】【表单URL】为必填项。"});
				 return;
			 }
			 var dataJson = frm.convertToJson(dataVo);
			 dataVo = JSON.stringify(dataJson);
			 ajaxRequest("POST","dataVo="+dataVo,"platform/bpm/bpmPublishAction/updateProcessForm","json","editProcessFormBack");
		}
	}];
	usd.createButtonsInDialog(buttons);
	usd.show();
}
function editProcessFormBack(result){
	if(result.flag=="ok"){
		$('#processForm').datagrid('reload');	
		$.messager.show({title : '提示',msg : "保存成功。"});
		usd.close();
		usd=null;
	}else{
		$.messager.show({title : '提示',msg : "保存失败。"});
	}
}
/**
 * 删除异常信息
 */
function deleteProcessForm(){
	var datas = $('#processForm').datagrid('getChecked');
	if(datas == null || datas.length <=0 ){
		$.messager.alert("操作提示", "请您选择一条记录!","info");
		return;
	}
	var ids = '';
	for(var i=0;i<datas.length;i++){
		ids += datas[i].id + ',';
	}
	$.messager.confirm("操作提示", "您确认要删除选定的数据吗？", function (data) {
        if (data) {
        	easyuiMask();
        	ajaxRequest("POST","dbId="+ids,"platform/bpm/bpmPublishAction/deleteProcessForm","json","deleteProcessFormBack");
        }
	 });
}
function  deleteProcessFormBack(json){
    $.messager.show({title : '提示',msg : "删除成功!"});
    easyuiUnMask();
	$("#processForm").datagrid('reload'); 
}
function refreshCache(){
	ajaxRequest("POST",null,"platform/bpm/bpmPublishAction/reLoadFormsCache","json","afterRefreshCache");
}
function afterRefreshCache(json){
	if(json.success == true){
		 $.messager.show({title : '提示',msg : "刷新缓存成功!"});
	}else{
		 $.messager.show({title : '提示',msg : "刷新缓存失败!"});
	}
}
</script>
<body class="easyui-layout" fit="true">
<div region="center" border="false" style="overflow: hidden;">
	<div id="toolbar" class="datagrid-toolbar" style="height:auto;display: block;">

			<table class="tableForm" id="searchForm" width='100%'>
				<tr>
					<td width="70px;"><a class="easyui-linkbutton"   iconCls="icon-add" plain="true" onclick="addProcessForm();" href="javascript:void(0);">添加</a></td>
					<td width="70px;"><a class="easyui-linkbutton"   iconCls="icon-edit" plain="true" onclick="editProcessForm();" href="javascript:void(0);">编辑</a></td>
					<td width="90px;"><a class="easyui-linkbutton"   iconCls="icon-reload" plain="true" onclick="refreshCache();" href="javascript:void(0);">刷新缓存</a></td>
					<td width="70px;"><a class="easyui-linkbutton"   iconCls="icon-remove" plain="true" onclick="deleteProcessForm();" href="javascript:void(0);">删除</a></td>
					<td width="100px;"><input id="ss"></input>  </td>
					<td > </td>
				</tr>
			</table>

	</div>
	<table id="processForm"></table>
</div>  

</body>
</html>