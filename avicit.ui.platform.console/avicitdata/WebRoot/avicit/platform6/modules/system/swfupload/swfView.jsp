<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="avicit.platform6.commons.utils.ComUtil"%>
<html>
	<head>
		<title>查看页面示例</title>
		<base href="<%=ComUtil.getRequestPath(request)%>">
		<link href="static/css/platform/themes/default/index/style/platform_blue.css" rel="stylesheet" type="text/css">
		<script type="text/javascript" src="static/js/platform/component/jQuery/jQuery-1.8.2/jquery-1.8.2.js"></script>
	</head>
	<script>
	//点击下载调用的js
	function downloadFile(fileId,formId,formTable,saveType){
		window.open("<%=request.getContextPath()%>/platform/swfUploadController/doDownload?fileuploadBusinessId="+formId+"&fileuploadBusinessTableName="+formTable+"&fileuploadIsSaveToDatabase="+saveType+"&fileId="+fileId,"_blank");
	}
	
	//点击浏览调用的js
	function viewFile(fileId,formId,formTable,saveType){
		window.open("<%=request.getContextPath()%>/platform/swfUploadController/doDownload?fileuploadBusinessId="+formId+"&fileuploadBusinessTableName="+formTable+"&fileuploadIsSaveToDatabase="+saveType+"&fileId="+fileId,"_blank");
	}
	
	//点击打印调用的js
	function printFile(fileId,formId,formTable,saveType){
		window.open("<%=request.getContextPath()%>/platform/swfUploadController/doDownload?fileuploadBusinessId="+formId+"&fileuploadBusinessTableName="+formTable+"&fileuploadIsSaveToDatabase="+saveType+"&fileId="+fileId,"_blank");
	}
	</script>
	
	<body scroll="no">
	<!-- 
		参数说明:
		form_id:业务表ID
		save_type:true表示保存在硬盘，false表示保存在磁盘，other表示其他存储方式
		file_category(可选):附件分类通用代码，大小写敏感,可以为空。
		secret_level（可选）:附件密级通用代码，大小写敏感,可以为空。此字段会和用户密级进行比较，只能上传或查看低于用户密级的附件。
	-->
		<jsp:include page="/avicit/platform6/modules/system/swfupload/swfViewInclude.jsp">
			<jsp:param name="form_id" value="8a58bc27443e47b201443f288433000d" />
			<jsp:param name="save_type" value="true" />
			<jsp:param name="file_category" value="PLATFORM_SEX" />
			<jsp:param name="secret_level" value="PLATFORM_FILE_SECRET_LEVEL" />
		</jsp:include>
	</body>
</html>