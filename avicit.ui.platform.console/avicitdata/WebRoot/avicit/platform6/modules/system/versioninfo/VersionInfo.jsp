<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<html>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>系统版本信息</title>
<base href="<%=ViewUtil.getRequestPath(request) %>">
<link href="avicit/platform6/modules/system/sysdept/css/icon.css" type="text/css" rel="stylesheet">
<jsp:include page="/avicit/platform6/component/common/EasyUIJsInclude.jsp"></jsp:include>
<jsp:include page="/avicit/platform6/modules/system/commonpopup/commonSelectionHead.jsp"></jsp:include>
</head>
<script type="text/javascript">
var baseurl = '<%=request.getContextPath()%>';
$(function(){
    var dataGridHeight = $(".easyui-layout").height() - 85;
	$('#versionForm').datagrid({
		url: 'platform/systemversion/getJarInfo.json',
		nowrap: false,
	    striped: true,
	    autoRowHeight:false,
	    singleSelect:false,
	    checkOnSelect:true,
	    remoteSort : false,
		height: dataGridHeight,
		fitColumns: true,
        collapsible:true,
        remoteSort: false,
        pagination:false,
        rownumbers:true,
        columns:[[
				  {field:'implementationTitle',title:'组件名称',width:300},
                  {field:'implementationVersion',title:'组件版本',width:100},
                  {field:'builtAt',title:'构建日期',width:300}
              ]]
	});
});

</script>
<body class="easyui-layout">
<div region="center" border="false" style="overflow: hidden;">
	<table id="versionForm"></table>
</div>  

</body>
</html>