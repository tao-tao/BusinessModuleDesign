<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>平台版本</title>
<base href="<%=ViewUtil.getRequestPath(request) %>">
<link href="avicit/platform6/modules/system/sysdept/css/icon.css" type="text/css" rel="stylesheet">
<jsp:include page="/avicit/platform6/component/common/EasyUIJsInclude.jsp"></jsp:include>
<jsp:include page="/avicit/platform6/modules/system/commonpopup/commonSelectionHead.jsp"></jsp:include>
</head>
<script type="text/javascript">
var baseurl = '<%=request.getContextPath()%>';
$(function(){
		$.ajax({
        cache: false,
        type: "get",
        url:'platform/systemversion/getCoreJarInfo.json',
        dataType:"json",
        async: false,
        error: function(request) {
        	throw new Error('操作失败，服务请求状态：'+request.status+' '+request.statusText+' 请检查服务是否可用！');
        },
        success: function(data) {
			$('#system').text(data.pv.implementationVersion);
			$('#buildDate').text(data.pv.builtAt);
			
        }
    });
});

</script>
<body class="easyui-layout">
		<div id="loginForm" align="center" style="height: 100%;width:100%,vertical-align:middle;">
			<table border="0" bgcolor="#EFEFEF" vertical-align:middle;" height="100%" width="100%">
				<tr>
					<td  colspan="2">
					</td>
				</tr>
				<tr>
					<td  colspan="2">
					</td>
				</tr>
				<tr>
					<td  colspan="2">
					</td>
				</tr>
				<tr>
					<td  colspan="2">
					</td>
				</tr>
				<tr>
					<td  colspan="2">
					</td>
				</tr>
				<tr>
					<td style="width: 150px;" align="right">平台版本：</td>
					<td id="system" align="left"></td>
				</tr>
				<tr>
					<td  colspan="2">
					</td>
				</tr>

				<tr>
					<td style="width: 90px;" align="right">构建日期：</td>
					<td id="buildDate" align="left"></td>
				</tr>
				<tr></tr>
				<tr>
					<td  colspan="2">
					</td>
				</tr>

				<tr>
					<td  style="width: 90px;" align="right">
						版权所有：
					</td>
					<td  align="left">
						金航数码科技有限责任公司
					</td>
				</tr>
				<tr>
					<td  colspan="2">
					</td>
				</tr>
				<tr>
					<td  colspan="2">
					</td>
				</tr>
				<tr>
					<td  colspan="2">
					</td>
				</tr>
				<tr>
					<td  colspan="2">
					</td>
				</tr>
				<tr>
					<td  colspan="2">
					</td>
				</tr>
			</table>
		</div>
</body>
</html>