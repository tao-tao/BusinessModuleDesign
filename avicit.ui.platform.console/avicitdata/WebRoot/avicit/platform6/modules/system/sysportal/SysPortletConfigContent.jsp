<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<%@page import="avicit.platform6.core.session.SessionHelper"%>
<%
 	String loginUserName = SessionHelper.getLoginSysUser().getName();
 	String loginUserId = SessionHelper.getLoginSysUser().getId();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>

<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<base href="<%=ViewUtil.getRequestPath(request)%>">
<jsp:include page="/avicit/platform6/component/common/EasyUIJsInclude.jsp"></jsp:include>
<style type="text/css">
	body{
		font-family: Microsoft Yahei,sans-serif,Arial,Helvetica;
		font-size:12px;
		margin:0px 0px 0px 0px;
	}
</style>
<script type="text/javascript">
	var id = '${id}';
	var isgloable = '${isgloable}';
	var layoutTemplateName = '${layoutTemplateName}';
	
	function addPortletFirst(obj){
		var portletId = '';
		var columnId = $(obj).parent().attr('id');
		if(typeof(columnId) == 'undefined'){
			columnId = $(obj).parent().parent().parent().parent().parent().attr('id');
		}
		try{
			parent.parent.openSelectPorletLayoutConfigDialog(portletId,columnId);
		}catch(e){
			parent.openSelectPorletLayoutConfigDialog(portletId,columnId);
		}
	}
	function addPortletSecend(obj){
		var portletId = '';
		var columnId = $(obj).parent().parent().attr('id');
		if(typeof(columnId) == 'undefined'){
			columnId = $(obj).parent().parent().parent().parent().parent().attr('id');
		}
		try{
			parent.parent.openSelectPorletLayoutConfigDialog(portletId,columnId);
		}catch(e){
			parent.openSelectPorletLayoutConfigDialog(portletId,columnId);
		}
	}
	function removeAllPortlet(obj){
		var columnId = $(obj).parent().parent().attr('id');
		var flag = window.confirm("确定要移除全部的portlet吗?");
		if (flag) {
			var html = "<img style=\"cursor:pointer;\"  onClick=\"addPortletFirst(this);return false;\" src='avicit/platform6/modules/system/sysportal/portalstyle/portlet_config_add.gif' border='0'>";
			$('#' + columnId).html(html);
		}
	}
	function backOperate(value,valueText,columnId){
		var portletContentHtml = '';
		if($(".portletContent_" + columnId).length > 0){
			portletContentHtml = $(".portletContent_" + columnId).html();
		}
		var html = $('#' + columnId).html();
		if(html.indexOf("<img") == 0){
			//$('#' + columnId).html('<table width=100% id=column_inner_' + columnId + '><tbody></tbody></table>');
		}
		var toolbar = "<div style=\"width:100%;background:#f0f0f0; text-align:left;\">" + 
					"<img title=\"继续添加portlet\" style=\"cursor:pointer;\"  onClick=\"addPortletSecend(this);return false;\" src='avicit/platform6/modules/system/sysportal/portalstyle/portlet_config_add1.gif' border='0'>添加" +
					"&nbsp;&nbsp;&nbsp;<img title=\"全部清空\" style=\"cursor:pointer;\"  onClick=\"removeAllPortlet(this);return false;\" src='avicit/platform6/modules/system/sysportal/portalstyle/portlet_config_remove1.gif' border='0'>全部删除" +
					"</div>";
		var html = "<div style=\"overflow-y:auto;height:100px\"><table width=\"100%\" border=0 class=\"portletContent_" + columnId + "\">";
		html += portletContentHtml;
		var backValues = value.split('|');
		var backValueTexts = valueText.split('|');
		for(var i = 0 ; i < backValues.length ; i++){
			if(backValues[i] != ''){
				html += "<tr><td width=\"70%\" align=\"right\" portletId=\"" + backValues[i] + "\">" + backValueTexts[i] + "</td><td width=\"30%\" align=\"center\"><img style=\"cursor:pointer;\"  onClick=\"removePortlet(this,'" + columnId + "');return false;\" src='avicit/platform6/modules/system/sysportal/portalstyle/portlet_config_remove1.gif' border='0'></td></tr>";
			}
		}
		html += "</table></div>";
		//$('#' + columnId).attr('portletId',value);
		$('#' + columnId).html(toolbar + html);
	}
	function removePortlet(obj,columnId){
		var flag = window.confirm("确定要移除该portlet吗?");
		if (flag) {
			//var html = "<img style=\"cursor:pointer;\"  onClick=\"addPortlet(this);return false;\" src='avicit/platform6/modules/system/sysportal/portalstyle/portlet_config_add.gif' border='0'>";
			var columnHtmlObj = $(obj).parent().parent().parent();
			$(obj).parent().parent().remove();
			if($(columnHtmlObj).find("tr").length == 0){
				var html = "<img style=\"cursor:pointer;\"  onClick=\"addPortletFirst(this);return false;\" src='avicit/platform6/modules/system/sysportal/portalstyle/portlet_config_add.gif' border='0'>";
				$('#' + columnId).html(html);
			}
		}
	}
	function saveConfigResultXml(){
		var portletResult = getConfigColumnPortlet();
		$.ajax({ 
			url: '${saveConfigXmlPath}', 
			async : false,
			type: "POST",
			data : 'isgloable=' + isgloable + '&layoutTemplateName=' + layoutTemplateName + '&id='+ id + '&result=' + portletResult,
			success: function(){
					
			},
			error : function(){
				//alert('portlet配置信息保存失败!');
			}
		});
	}
	
	function getConfigColumnPortlet(){
		var resulst = new Array();
		$('.columns').each(function(){
			var columns = new Array();
			$(this).find('td').each(function(){
				var columnId = $(this).attr('id');
				if(typeof(columnId) != 'undefined'){
					var column = new Array();
					$('.portletContent_' + columnId).find('td').each(function(){
						var portletId = $(this).attr('portletId');
						if(typeof(portletId) != 'undefined'){
							column.push({
								'portlet' : portletId
							});
						}
					});
					if(column.length > 0){
						columns.push({
							'id'  :columnId,
							'column' : column
						});
					} else {
						columns.push({
							'id'  : columnId,
							'column' : []
						});
					}
				}
			});
			resulst.push({
					'columns' : columns 
			});
		});
		return JSON.stringify({
			'configResulst' : resulst
		});
	}
</script>
</head>
<body id='layout'>
	${layoutConfigContentHtml}
	<input type='hidden' name='layoutConfigContentResult' id='layoutConfigContentResult'>
</body>
</html>
