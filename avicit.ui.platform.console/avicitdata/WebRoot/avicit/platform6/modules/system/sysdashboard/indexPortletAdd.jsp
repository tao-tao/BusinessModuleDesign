<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="avicit.platform6.commons.utils.ViewUtil"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>

<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<base href="<%=ViewUtil.getRequestPath(request)%>">
<link href="static/js/platform/component/jQuery/jquery-easyui-1.3.5/themes/default/easyui.css" type="text/css" rel="stylesheet">
<link href="static/js/platform/component/jQuery/jquery-easyui-1.3.5/themes/default/icon.css" rel="stylesheet" type="text/css">
<link href="static/js/platform/component/jQuery/jquery-easyui-1.3.5/themes/default/avicit-easyui-extend-1.3.5.css" type="text/css" rel="stylesheet">
<style type="text/css">
body{
	font-family: Microsoft Yahei,sans-serif,Arial,Helvetica;
	font-size:14px;
	margin:10px 10px 10px 10px;
}
</style>
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
			url: baseUrl+'platform/IndexPortalController/getIndexPortletList.json?isgloable='+isgloable,
			data : {},
			type : 'post',
			dataType : 'json',
			success: function(result){
				if (result.flag == "success") {
					var listHtml="<table width='100%' border='0' cellpadding='0' cellspacing='0' id='indexMpmPortlet'>";
					var portletList = result.portletList;
					var portletids = getExistsPortletids();
					for (var i = 0; i < portletList.length; i++) {
						var display = true;
						var portlet = portletList[i];
						if (portletids != 'undefind' && portletids != null) {
							if (portletids.indexOf("@"+portlet.portletId+"@") > -1) {
								display = false;
							}
						} 
						if (display){
							listHtml +="<tr>";
							listHtml+="<td width='100%' align='left'>&nbsp;&nbsp;<input type='checkbox' name='_portlet' value='"+portlet.portletId+"'/>"+portlet.title+"</td>"; 
							listHtml +="</tr>";
						}
					}
					listHtml +="</table>";
					$("#addcheckbox").html(listHtml); 
// 					parent.parent.$("#_iframe_portletAddDialog").attr("height",($("#indexMpmPortlet").height()+80)+'px');
// 					parent.parent.$("#_iframe_portletAddDialog").attr("*height",($("#indexMpmPortlet").height()+80)+'px');
					parent.parent.$("#_iframe_portletAddDialog").css("height",($("#indexMpmPortlet").height()+80)+'px').css("_height",($("#indexMpmPortlet").height()+80)+'px');
				}
			},
			error : function(){
				//alert('portlet配置信息保存失败!');
			}
		});
	});
	function addPortletToIndex(){
		var ids="";
        $("input[name='_portlet']:checkbox").each(function(){ 
            if($(this).attr("checked")){
                ids += $(this).val()+","
            }
        });
        var iframeBody = parent.parent.$("#iframeBody")[0].contentWindow;
        iframeBody.addPortlet(ids);
        hideDialog();
	}
	function getExistsPortletids(){
		var portletids = "@";
		var iframeBody = parent.$("#iframeBody")[0].contentWindow;
		var portletRow = iframeBody.$("#portalContent .ui-portlet");
		$.each(portletRow,function(k,v){
			 var indexs = iframeBody.getPortletInfo(v.id);
	         $.each(indexs, function(k1, v1) {
	        	 portletids = portletids + k1 + "@";
	         });
		});
		return portletids;
	}
</script>
</head>
<body>
	<div id="addcheckbox"></div>
	<div id="toolbar" class="datagrid-toolbar datagrid-toolbar-extend" style="height:auto;display: block;position:relative;">
	<table class="tableForm"  width='100%'>
		<tr>	
		
			<td align="right" width="100%">
				<sec:accesscontrollist hasPermission="3" domainObject="formdialog_TestBpmBusinessTripAdd_button_saveForm" >
					<a title="保存" id="saveButton"  class="easyui-linkbutton" plain="false" onclick="addPortletToIndex();" href="javascript:void(0);">保存</a>
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
