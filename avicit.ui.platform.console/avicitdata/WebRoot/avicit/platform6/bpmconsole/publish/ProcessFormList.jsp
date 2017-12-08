<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<%
	String processDefId = request.getParameter("processDefId");
%>
<base href="<%=ViewUtil.getRequestPath(request) %>">
<jsp:include page="/avicit/platform6/bpmclient/bpm/ProcessCommonJsInclude.jsp"></jsp:include>
</head>
<script type="text/javascript">
var baseurl = '<%=request.getContextPath()%>';
var processDefId  = '<%=processDefId%>';
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
	
	$('#processForm').datagrid({
		url: 'platform/bpm/bpmPublishAction/getProcessForms.json',
		width: '100%',
        nowrap: true,
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
        autoRowHeight: false,
        singleSelect:true,
	    checkOnSelect:true,
        striped: true,
        collapsible:true,
        remoteSort: false,
        pagination:false,
        rownumbers:true,
        columns:[[
				  {field:'id',hidden:true},
				  {field:'nodeId',hidden:true},
				  {field:'processDefId',title:'操作',hidden:true},
				  {field:'op',title:'操作',width:25,align:'left',checkbox:true},
				  {field:'formName',title:'表单名称',width:150,rowspan:2},
                  {field:'formUrl',title:'表单URL',width:250},
                  {field:'formCode',title:'表单编码',width:80,rowspan:2},
                  {field:'formType',title:'表单类型',width:80,rowspan:2}
              ]]
	});
});
function getSelectedResultDataJson(){
	var datas = $('#processForm').datagrid('getSelected');
	if (datas == null || datas=='' || datas == 'null') {
		$.messager.alert("操作提示", "请您选择一条记录！","info"); 
		return;
	}else{
		if(processDefId!=null && processDefId!='' && processDefId!='undefinded'){
			datas.processDefId=processDefId;
		}
		return datas;
	}
	
}
function backChecked(obj) {
	easyuiUnMask();
	if (obj != null && obj.success == true) {
		$.messager.show({
			title : '提示',
			msg : "操作成功！"
		});
		$('#processForm').datagrid('reload');	
	} else {
		$.messager.show({
			title : '提示',
			msg : "操作失败！"
		});
	}
}
</script>
<body class="easyui-layout" fit="true">
<div data-options="region:'center',title:''" style="padding:5px;">
	<input id="ss"></input>  
	<table id="processForm"></table>
</div>  

</body>
</html>