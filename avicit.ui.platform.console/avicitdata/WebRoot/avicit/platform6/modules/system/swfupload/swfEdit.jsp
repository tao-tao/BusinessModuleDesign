<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="avicit.platform6.commons.utils.ComUtil"%>
<html>
	<head>
		<title>编辑页面示例</title>
		<base href="<%=ComUtil.getRequestPath(request)%>">
		<link href="static/css/platform/themes/default/index/style/platform_blue.css" rel="stylesheet" type="text/css">
		<script type="text/javascript" src="static/js/platform/component/jQuery/jQuery-1.8.2/jquery-1.8.2.js"></script>
	</head>
	<%
		//此处生成业务对象ID
		//String id = ComUtil.getId();
		String id = "8a58ab2a485e5c9d01485e5f29fd0001";
	%>
	<script>
	function afterUploadEvent(){
		alert("上传成功！");
		location.href = "<%=ComUtil.getRequestPath(request)%>"+"avicit/platform6/modules/system/swfupload/swfEdit.jsp"
		//上传后事件
	} 
	</script>
	<body scroll="no">
		<form action="" >
			<!-- 在业务表单中把ID做为隐藏域传到后台，后台不能再重新生成ID,否则业务数据和附件对不上号 -->
			<input type="hidden" name="id" value="<%= id %>" />
		</form>
		<!-- 
			参数说明:
			file_size_limit:附件大小限制，可以带单位，合法的单位有:B、KB、MB、GB，如果省略单位，则默认为KB。该属性为0时，表示不限制文件的大小。
			file_types:允许上传的文件类型，当有多个类型时使用分号隔开，比如：*.jpg;*.png ,允许所有类型时请使用 *.*
			file_upload_limit:上传附件的数量限制，0表示不限制。
			save_type:  true:保存到数据库,false表示保存到硬盘,selfDefined表示由用户自己处理
			form_id:业务表ID,新增页面需要通过ConUtil.getId()先生成好ID！
			form_code:业务表的表名
			form_field:后加的，不知道干什么用的
			allowAdd:是否有上传附件权限，false-无  true-有
			allowDel:是否有删除已存附件权限，false-无  true-有
			cleanOnExit:新增页面设置为true，不保存数据（以页面中saveSuccess标志判断）直接退出时，会清除附件；编辑页面可设置为false,退出时不会清空附件
			file_category(可选):附件分类通用代码，大小写敏感,可以为空。一般不需要
			secret_level（可选）:附件密级通用代码，大小写敏感,可以为空。此字段会和用户密级进行比较，只能上传或查看低于用户密级的附件。
		-->
		<jsp:include page="/avicit/platform6/modules/system/swfupload/swfEditInclude.jsp">
			<jsp:param name="file_size_limit" value="500 MB" />
			<jsp:param name="file_types" value="*.*" />
			<jsp:param name="file_upload_limit" value="10" />
			<jsp:param name="save_type" value="true" /> 
			<jsp:param name="form_id" value="<%= id %>" />
			<jsp:param name="form_code" value="pm_project" />
			<jsp:param name="form_field" value="" />
			<jsp:param name="allowAdd" value="true" />
			<jsp:param name="allowDel" value="true" />
			<jsp:param name="cleanOnExit" value="true" />
			<jsp:param name="secret_level" value="PLATFORM_FILE_SECRET_LEVEL" />
		</jsp:include>
	</body>
</html>