<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="avicit.platform6.commons.utils.ViewUtil"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>

<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<base href="<%=ViewUtil.getRequestPath(request) %>">

<link href="static/js/platform/component/jQuery/jquery-easyui-1.3.5/themes/default/easyui.css" type="text/css" rel="stylesheet">
<link href="static/js/platform/component/jQuery/jquery-easyui-1.3.5/themes/default/icon.css" rel="stylesheet" type="text/css">
<link href="static/js/platform/component/jQuery/jquery-easyui-1.3.5/themes/default/avicit-easyui-extend-1.3.5.css" type="text/css" rel="stylesheet">
<link href="static/js/platform/component/jQuery/jquery-easyui-1.3.5/themes/default/page.css" type="text/css"  rel="stylesheet">

<script src="static/js/platform/component/common/json2.js" type="text/javascript"></script>
<script src="static/js/platform/component/jQuery/jQuery-1.8.2/jquery-1.8.2.min.js" type="text/javascript"></script>
<script src="static/js/platform/component/jQuery/jquery-easyui-1.3.5/jquery.easyui.min.js" type="text/javascript"></script>
<script src="static/js/platform/component/jQuery/jquery-easyui-1.3.5/datagrid-scrollview.js" type="text/javascript"></script>
<script src="static/js/platform/component/jQuery/jquery-easyui-1.3.5/locale/easyui-lang-zh_CN.js" type="text/javascript"></script>
<script src="static/js/platform/component/common/pageUtil.js" type="text/javascript"></script>
<script src="static/js/platform/component/common/Tools.js" type="text/javascript"></script>
<script src="static/js/platform/component/common/CommonDialog.js" type="text/javascript"></script>
<%
String dialogId = request.getParameter("dialogId");
String isgloable = request.getParameter("isgloable");
String sysPortletConfigId = request.getParameter("sysPortletConfigId");
%>
<script type="text/javascript">
var baseUrl = "<%=ViewUtil.getRequestPath(request)%>";
	var dialogId = "<%=dialogId%>";
	var isgloable = "<%=isgloable%>";

	function hideDialog(){
		if(parent!=null&&parent.$('#' + dialogId)!=null){
			parent.$('#' + dialogId).dialog('close');//关闭当前窗口
		}
	}
	
	
	
	$(function() {
		$.ajax({ 
			async:true,
			url: baseUrl+'platform/IndexPortalController/getPortletConfig.json?isgloable='+isgloable,
			data : {},
			type : 'post',
			dataType : 'json',
			success: function(result){
				$("#layoutContent").html(result.layoutContent);
			},
			error : function(){
				//alert('portlet配置信息保存失败!');
			}
		});
	});
	function saveLayout(){
		var layoutKey=$('input:radio[name="layoutId"]:checked').val();
		if(layoutKey==null||layoutKey==""){
			alert("请您选择一个模板");return;
		}
		$.ajax({ 
			url: baseUrl+'platform/IndexPortalController/saveIndexLayout.json',
			async : false,
			type: "POST",
			data : 'isgloable='+isgloable+'&layoutKey=' + layoutKey+'&sysPortletConfigId=<%=sysPortletConfigId%>',
			success: function(){
				var iframeBody = parent.parent.$("#iframeBody")[0].contentWindow;
		        iframeBody.loadPortlet();
				//获取当前操作的数据行记录
				$.messager.alert('提示','布局设置成功!','info',function(){
				//alert('布局设置成功!')
				//parent.parent.location.reload();
					hideDialog();
					iframeBody.reload();
				});
			},
			error : function(){
				//alert('portlet配置信息保存失败!');
			}
		});
	}
</script>
</head>
<body >
	<div id="layoutContent">
		
	</div>
	<div id="toolbar" class="datagrid-toolbar datagrid-toolbar-extend" style="height:auto;display: block;">
	<table class="tableForm"  width='100%' border="0" cellspacing="1" >
		<tr>	
		
		
			<td align="right" width="100%">
				<sec:accesscontrollist hasPermission="3" domainObject="formdialog_TestBpmBusinessTripAdd_button_saveForm" >
					<a title="保存" id="saveButton"  class="easyui-linkbutton" plain="false" onclick="saveLayout();" href="javascript:void(0);">保存</a>
				</sec:accesscontrollist>
				<sec:accesscontrollist hasPermission="3" domainObject="formdialog_TestBpmBusinessTripAdd_button_backForm" >
					<a title="返回" id="returnButton"  class="easyui-linkbutton"  plain="false" onclick="hideDialog();" href="javascript:void(0);">返回</a>
				</sec:accesscontrollist>
			</td>
			
			<td > </td>
		</tr>
	</table>
	</div>
</body>
</html>
