<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="avicit.platform6.commons.utils.*"%>
<html>
<head>

	<base href="<%=ViewUtil.getRequestPath(request)%>">
	<script type="text/javascript" src="static/js/platform/component/commonword/Ntkoocx.js"></script>
	<title>模版编辑</title>
</head>
<%
	String path = request.getContextPath();
	if (path.equals("/")) { path = "";}
	String wordId = (String) request.getParameter("wordId");
	String strAuthType = (String) request.getHeader("Auth_Type");
%>
<body onload="doEdit();">
	<form id="" method="post" enctype ="multipart/form-data">
	<script type="text/javascript">
	var baseurl = '<%=request.getContextPath()%>';
		var TANGER_OCX_OBJ;
		
		//提交
		function doSave(){
			var formUrl = "<%=path%>/platform/bpm/bpmconsole/wordTempletAction/updateSysWordTempletBody?wordId=<%=wordId%>";
			document.forms[0].action = encodeURI(encodeURI(formUrl));
		    var result= TANGER_OCX_SaveDocUnprotect(<%=strAuthType%>);
		    window.returnValue=result; 
		    if(result != ""){
		       alert(result);
		    } else {
		    	alert("文件保存成功！");
			    doClose();
		    }
		}
		//关闭
		function doClose(){
			window.close();
		}
		function doEdit(){
			TANGER_OCX_OpenDoc('<%=wordId%>');
		}
		//打开模板
		function TANGER_OCX_OpenDoc(wordId) {
			TANGER_OCX_OBJ = document.all.item("TANGER_OCX");
			if(wordId != ""){	
				TANGER_OCX_OBJ.BeginOpenFromURL("<%=path%>/platform/bpm/bpmconsole/wordTempletAction/getSysWordTempletBody?wordId="+wordId);
			}else{
				alert("没有正文模板，请点击【上传】按钮添加正文模板！")
			}
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
			doSave();
		}
	</script> 
</form>
</body>
</html>

