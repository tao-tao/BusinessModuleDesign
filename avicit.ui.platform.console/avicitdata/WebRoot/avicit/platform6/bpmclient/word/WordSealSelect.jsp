<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<html>
<head>
	<base href="<%=ViewUtil.getRequestPath(request)%>">
	<jsp:include page="/avicit/platform6/bpmclient/bpm/ProcessCommonJsInclude.jsp"></jsp:include>
	<title>印章列表</title>
</head>
<%
	String path = request.getContextPath();
	if (path.equals("/")) { path = "";}
%>
<body>
	<table id="wordSealTable"></table>
	<script type="text/javascript">
	var baseurl = '<%=request.getContextPath()%>';
		$(function(){
			$('#wordSealTable').datagrid({
				url: '<%=path%>/platform/bpm/clientbpmwordction/getWordSealList.json',
				width: '100%',
			    nowrap: false,
			    striped: true,
			    autoRowHeight:false,
			    singleSelect:true,
			    checkOnSelect:false,
				height: 200,
				fitColumns: true,
				pagination:false,
				rownumbers:true,
				loadMsg:' 处理中，请稍候…',
				columns:[[
					{field:'id',title:'',width:40,align:'left',hidden:true},
					{field:'sealName',title:'印章名称',width:40,align:'center',
						formatter:function(value,rec){
		  					return "<a href=\"javascript:window.doOpenWordSeal('"+rec.id+"');\">"+value+"</a>";
		  				}
					}
				]]
			});
		});
		
		function doOpenWordSeal(wordSealId) {
			parent.doWordSeal(wordSealId);
		}
	</script>
</body>
</html>