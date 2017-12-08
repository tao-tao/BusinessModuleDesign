<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<html>
<head>
<%
	String processDefinitionId = request.getParameter("processDefinitionId");
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新建流程实例</title>
<base href="<%=ViewUtil.getRequestPath(request) %>">
<jsp:include page="/avicit/platform6/bpmclient/bpm/ProcessCommonJsInclude.jsp"></jsp:include>
</head>

<script type="text/javascript">
var baseurl = '<%=request.getContextPath()%>';
var processDefinitionId = '<%=processDefinitionId%>';

function initComboBox(datas){
	$('#proceeForms').combobox({   
 		width:200,
 		data:datas,
   		valueField:"formCode",   
    	textField:"formName"  
	});  
}
//初始化数据
function loadData(){
	//加载表单数据
	addProcessFormDatas();
	//注册按钮事件
	loadSubmit();
	$(function(){
		var lastIndex;
		$('#proceevargrid').datagrid({
            width:450,
            height:350,
            nowrap: true,
            autoRowHeight: false,
            striped: true,
            collapsible:true,
            remoteSort: false,
            columns:[[
                {field:'varName',title:'变量名称',width:100,editor:'text'},
                {field:'varType',title:'变量类型',width:120,rowspan:2,sortable:true,
                	 editor:{type:'combobox',options:{data:[{'value':'string','text':'字符串'},{'value':'int','text':'整型'},
                	                                        {'value':'Long','text':'长整型'},{'value':'Float','text':'浮点型'},{'value':'date','text':'日期型'}]}}
                },
                {field:'varValue',title:'变量值',width:180,rowspan:2,editor:'text'}
            ]],
            onBeforeLoad:function(){
				$(this).datagrid('rejectChanges');
			},
			onClickRow:function(rowIndex){
				if (lastIndex != rowIndex){
					$('#proceevargrid').datagrid('endEdit', lastIndex);
					$('#proceevargrid').datagrid('beginEdit', rowIndex);
				}
				lastIndex = rowIndex;
			},
            pagination:false,
            rownumbers:true,
            toolbar:[{
                id:'btnadd',
                text:'添加',
                iconCls:'icon-add',
                handler:function(){
                    $('#proceevargrid').datagrid('appendRow',{
                    	varName: '',
                    	varType: '',
                    	varValue: ''
                    });
                    lastIndex = $('#proceevargrid').datagrid('getRows').length-1;
					$('#proceevargrid').datagrid('selectRow', lastIndex);
					$('#proceevargrid').datagrid('beginEdit', lastIndex);
            	}
            }
            ,'-',{
    		    text:'删除',
    		    iconCls:'icon-remove',
    		    handler:function(){
    			var row = $('#proceevargrid').datagrid('getSelected');
    			if (row){
    			    var index = $('#proceevargrid').datagrid('getRowIndex', row);
    			    $('#proceevargrid').datagrid('deleteRow', index);
    			}
    		    }
    		}]
		
		});
		 
	});
}


function backFinished(obj){
	if(obj != null && obj.success == true){
		$.messager.show({
			title : '提示',
			msg : "操作成功！"
		});
	}else{
		$.messager.show({
			title : '提示',
			msg : "操作失败！"
		});
	}
}
//加载表单数据
function addProcessFormDatas(){
	ajaxRequest("GET","processDefinitionId="+processDefinitionId,"platform/bpm/bpmConsoleAction/getProcessforms","json","backResult");
}
//回调函数，将数据传给下拉菜单
function backResult(obj){
	initComboBox(obj.datas);
}
function loadSubmit(){
	$('#btn1').bind('click', function(){
		var myselect = $('#proceeForms').combobox('getValue');
		//接受所有修改
		$('#proceevargrid').datagrid('acceptChanges');
		var gridDatas = $('#proceevargrid').datagrid('getRows');
		var variables = '';
		for(var i=0;i<gridDatas.length;i++){
			var varName =  gridDatas[i].varName;
			var varType =  gridDatas[i].varType;
			var varValue =  gridDatas[i].varValue;
			if(varName=='' || varType == '' || varValue == '' || varName==null || varType == null || varValue == null){
				$.messager.alert("操作提示","增加的变量属性填写不规范!", "info");
				return;
			}
			variables += varName + '@@@' + varType + '@@@' + varValue + ';';
		}
		if(myselect!="请选择..."){
			ajaxRequest("POST","processDefinitionId="+processDefinitionId+"&formCode="+myselect+"&variables="+variables,"platform/bpm/bpmConsoleAction/doStartNewProcessEntry","json","backFinished");
		}else{
			$.messager.alert("操作提示","请选择表单...", "info");
		}
		
	});
}
</script>
<body onload='loadData();'>
	<div style="padding:10px;width:auto">
	<table class="easyui-tables">
		<tr>
			<td>流程表单名称：&nbsp;&nbsp;</td>
			<td><input id="proceeForms" name="proceeForms" value='请选择...' /></td>
			<td><a id="btn1" class="easyui-linkbutton" href="javascript:void(0);">启动流程</a> </td>
		</tr>
	</table>
	
</div>
	<table id="proceevargrid"></table>
</body>
</html>