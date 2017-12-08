<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<html>
<head>
	<base href="<%=ViewUtil.getRequestPath(request)%>">
	<jsp:include page="/avicit/platform6/bpmclient/bpm/ProcessCommonJsInclude.jsp"></jsp:include>
	<title>创建正文</title>
</head>
<%
	String path = request.getContextPath();
	if (path.equals("/")) { path = "";}
	String strAuthType = (String) request.getHeader("Auth_Type");
	String processInstanceId = (String) request.getParameter("processInstanceId");
	String executionId = (String) request.getParameter("executionId");
	String templetId = (String) request.getParameter("templetId");
%>
<body onload="doOpen();">
	<form id="" method="post" enctype ="multipart/form-data">
	<script type="text/javascript">
	    var baseurl = '<%=request.getContextPath()%>';
		var isSave = true;
		//提交
		function doSave(){
			var formUrl = "<%=path%>/platform/bpm/clientbpmwordction/insertword?processInstanceId=<%=processInstanceId%>";
			document.forms[0].action = encodeURI(formUrl);
		    var result= TANGER_OCX_SaveDocUnprotect(<%=strAuthType%>);
		    window.returnValue=result; 
		    if(result != ""){
		       alert(result);
		    } else {
			    doClose();
		    }
		}
		
		//打开正文
		function doOpen(){
			var url = "<%=path%>/platform/bpm/clientbpmwordction/getWordRight";
			jQuery.ajax({
		        type:"POST",
				data:"processInstanceId=<%=processInstanceId%>&executionId=<%=executionId%>",
		        url: url,  
		        dataType:"json",
				context: document.body, 
		        success: function(msg){
		        	if(msg!=null){
		        		if(msg.error!=null){ //失败
		        			window.alert("Ajax操作时发生异常，地址为：" + url + "，异常信息为：" + msg.error);
		        			return;
		        		}else{
		        			getWordRight(msg.docRight);//得到正文权限
		        			TANGER_OCX_OpenDoc("<%=templetId%>");
		        		}
		        	}
				},
				error: function(msg){
					window.alert("Ajax操作时发生异常，地址为：" + url + "，异常信息为：" + msg.responseText);
				}
	    	}); 
		}
		
		//打开模板
		function TANGER_OCX_OpenDoc(templetId) {
			TANGER_OCX_OBJ = document.all.item("TANGER_OCX");
			if(templetId != ""){
				TANGER_OCX_OBJ.BeginOpenFromURL("<%=path%>/platform/bpm/clientbpmwordction/getWordTemplet?templetId="+templetId);
			}else{
				alert("没有正文模板，请点击【上传】按钮添加正文模板！");
			}
		}
		
		//页面关闭前事件
		window.onbeforeunload = function (){
			if(isSave && window.confirm("需要保存文件吗？")) {
				doSave();
	        }
		};
		
		//关闭
		function doClose(){
			alert("文件保存成功！");
			isSave = false;
			window.close();
		}
		
		//得到正文拥有的权限
		var userName = "";//文档处理人
		var wordRevisions = false;//编辑时留痕
		var wordShowRevisions = false;//显示留痕
		function getWordRight(docRight) {
			if (docRight.userName) userName = docRight.userName;
			if (docRight.wordRevisions) wordRevisions = true;
			if (docRight.wordShowRevisions) wordShowRevisions = true;
		}
		
		//控制
		function ShowTitleBar() {
			TANGER_OCX_EnableFileNewMenu(false);
			TANGER_OCX_EnableFileOpenMenu(true);
			TANGER_OCX_EnableFilePropertiesMenu(true);
			TANGER_OCX_EnableFileCloseMenu(false);
			TANGER_OCX_EnableFileSaveMenu(true);
			TANGER_OCX_EnableFileSaveAsMenu(true);
			TANGER_OCX_EnableFilePrintPreviewMenu(true);
			TANGER_OCX_EnableFilePrintMenu(true);
			TANGER_OCX_EnableFilePageSetupMenu(true);
		}
	</script>

	<!-- 引用正文控件 -->
	<script src="static/js/platform/component/commonword/NtkoObjEdit.js"></script>
    <!-- 以下函数相应控件的两个事件:OnDocumentClosed,和OnDocumentOpened -->
    <script language="JScript" for=TANGER_OCX event="OnDocumentClosed()">
	   	TANGER_OCX_OnDocumentClosed();
 	</script>
	<script  language="JScript" for=TANGER_OCX event="OnDocumentOpened(TANGER_OCX_str,TANGER_OCX_obj)">
		TANGER_OCX_OnDocumentOpened(TANGER_OCX_str,TANGER_OCX_obj);
		ShowTitleBar();
	</script>
	<script language="JScript" for="TANGER_OCX" event="OnFileCommand(cmd,canceled)">
		if (cmd == 3) {
			document.all("TANGER_OCX").CancelLastCommand = true;//取消缺省的操作
			doSave();
		}
	</script> 
</form>
</body>
</html>

