<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新建流程实例</title>
<base href="<%=ViewUtil.getRequestPath(request) %>">
<script src="static/js/platform/component/common/Tools.js" type="text/javascript"></script>
<script src="static/js/platform/bpm/client/js/wz_jsgraphics.js" type="text/javascript"></script>
<script src="static/js/platform/component/jQuery/jQuery-1.8.2/jquery-1.8.2.min.js" type="text/javascript"></script>
<script src="static/js/platform/component/jQuery/jquery-easyui-1.3.5/jquery.easyui.min.js" type="text/javascript"></script>
<link href="static/js/platform/component/jQuery/jquery-easyui-1.3.5/themes/gray/easyui.css" type="text/css" rel="stylesheet">
<link href="static/js/platform/component/jQuery/jquery-easyui-1.3.5/themes/gray/icon.css" rel="stylesheet" type="text/css">

</head>

<script type="text/javascript">
var baseurl = '<%=request.getContextPath()%>';
var currentPdid = parent.pdid;
//初始化数据
function loadData(){
	loadSubmit();
	$(function(){
		$('#proceeForms').combobox({   
			width:200,
	 		data:parent.myobj,
	   		valueField:'stringval_',   
	    	textField:'objname_'
		});  
	});
}
function backFinished(obj){
	easyuiUnMask();
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
function loadSubmit(){
	$('#btn1').bind('click', function(){
		var myselect = $('#proceeForms').combobox('getValue');
		if(myselect == currentPdid){
			$.messager.alert("不能选择相同的版本!", "info");
			return;
		}
		if(myselect.indexOf('-')!=-1){
			//修改本页的pdid
			currentPdid = myselect;
			easyuiMask();
			ajaxRequest("POST","pdid="+myselect + "&processInstanceId=" + parent.processInstanceId,"platform/bpm/bpmConsoleAction/changeProcessVersion","json","backFinished");
		}
		
	});
}

</script>
<body onload="loadData()">
<div style="padding:10px;width:auto">
	<table class="easyui-tables">
		<tr>
			<td>流程版本：&nbsp;&nbsp;</td>
			<td><input id="proceeForms" name="proceeForms" value='请选择...' /></td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;<a id="btn1" class="easyui-linkbutton">提     交</a> </td>
		</tr>
	</table>
	
</div>
			
	
</body>
</html>