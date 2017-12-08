<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="avicit.platform6.commons.utils.*"%>
<html>
<head>

	<base href="<%=ViewUtil.getRequestPath(request)%>">
	<script type="text/javascript" src="static/js/platform/component/commonword/Ntkoocx.js"></script>
	<title>模版查看</title>
</head>
<%
	String path = request.getContextPath();
	if (path.equals("/")) { path = "";}
	String wordId = (String) request.getParameter("wordId");
	String strAuthType = (String) request.getHeader("Auth_Type");
	String wordTempletName = (String)request.getAttribute("wordTempletName");
	String wordTempletVersion = (String)request.getAttribute("wordTempletVersion");
	String wordTempletState = (String)request.getAttribute("wordTempletState");
	String wordSequence = (String)request.getAttribute("wordSequence");
	String formCode = (String)request.getAttribute("formCode");
	String wordTempletType = (String)request.getAttribute("wordTempletType");
%>
<body onload="doOpen();">
	<form id="" method="post" enctype ="multipart/form-data">
		<script type="text/javascript">
		var baseurl = '<%=request.getContextPath()%>';
			//提交
			function doSave(){
				var formUrl = "<%=path%>/module/system/sysword/wordTempletUpdate.action?state='1'&wordId="+'<%=wordId%>';
					formUrl += "&wordTempletName="+'<%=wordTempletName%>'+"&wordTempletVersion="+'<%=wordTempletVersion%>'+"&wordTempletState="+'<%=wordTempletState%>';
					formUrl += "&wordSequence="+'<%=wordSequence%>'+"&formCode="+'<%=formCode%>'+"&wordTempletType="+'<%=wordTempletType%>';
				document.forms[0].action = encodeURI(encodeURI(formUrl));
			    var result= TANGER_OCX_SaveDocUnprotect(<%=strAuthType%>);
			    window.returnValue=result; 
			    if(result != ""){
			       alert(result);
			    }
			    doClose();
			}
			
			function doOpen(){
				TANGER_OCX_OBJ = document.all.item("TANGER_OCX");
				TANGER_OCX_OpenDoc('<%=wordId%>');
			}
			
			//关闭
			function doClose(){
				window.close();
			}
			
			//打开模板
			function TANGER_OCX_OpenDoc(wordId){
				if(wordId != ""){	
					TANGER_OCX_OBJ.BeginOpenFromURL("<%=path%>/platform/bpm/bpmconsole/wordTempletAction/getSysWordTempletBody?wordId="+wordId);
				}else{
					alert("没有正文模板，请点击【上传】按钮添加正文模板！")
				}
			}
			
			//控制
			function ShowTitleBar() {
				TANGER_OCX_EnableFileNewMenu(false);
				TANGER_OCX_EnableFileOpenMenu(false);
				TANGER_OCX_EnableFilePropertiesMenu(false);
				TANGER_OCX_EnableFileCloseMenu(false);
				TANGER_OCX_EnableFileSaveMenu(false);
				TANGER_OCX_EnableFileSaveAsMenu(false);
				TANGER_OCX_EnableFilePrintPreviewMenu(false);
				TANGER_OCX_EnableFilePrintMenu(false);
				TANGER_OCX_EnableFilePageSetupMenu(false);
			}
		</script>
	
		<!-- 引用正文控件 -->
		<script src="static/js/platform/component/commonword/NtkoObjRead.js"></script>
	    <!-- 以下函数相应控件的两个事件:OnDocumentClosed,和OnDocumentOpened -->
	    <script language="JScript" for=TANGER_OCX event="OnDocumentClosed()">
		   	TANGER_OCX_OnDocumentClosed();
	 	</script>
		<script  language="JScript" for=TANGER_OCX event="OnDocumentOpened(TANGER_OCX_str,TANGER_OCX_obj)">
			TANGER_OCX_OnDocumentOpened(TANGER_OCX_str,TANGER_OCX_obj);
			ShowTitleBar();
		</script>
		<script language="JScript" for="TANGER_OCX" event="OnFileCommand(cmd,canceled)">
		</script> 
	</form>
</body>
</html>

