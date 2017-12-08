<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<html>
<head>
	<base href="<%=ViewUtil.getRequestPath(request)%>">
	<jsp:include page="/avicit/platform6/bpmclient/bpm/ProcessCommonJsInclude.jsp"></jsp:include>
	<title>编辑正文</title>
</head>
<%
	String path = request.getContextPath();
	if (path.equals("/")) { path = "";}
	String strAuthType = (String) request.getHeader("Auth_Type");
	String processInstanceId = (String) request.getParameter("processInstanceId");
	String executionId = (String) request.getParameter("executionId");
%>
<body onload="doOpen();">
	<form id="" method="post" enctype ="multipart/form-data">
	<script type="text/javascript">
	var baseurl = '<%=request.getContextPath()%>';
		var isSave = true;
		//提交
		function doSave(){
			var formUrl = "<%=path%>/platform/bpm/clientbpmwordction/updateword?processInstanceId=<%=processInstanceId%>";
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
							TANGER_OCX_OpenDoc("<%=processInstanceId%>");//打开正文
		        			getWordRight(msg.docRight);//得到正文权限
		        		}
		        	}
				},
				error: function(msg){
					window.alert("Ajax操作时发生异常，地址为：" + url + "，异常信息为：" + msg.responseText);
				}
	    	}); 
		}
		
		//打开模板
		function TANGER_OCX_OpenDoc(processInstanceId) {
			TANGER_OCX_OBJ = document.all.item("TANGER_OCX");
			if(processInstanceId != ""){	
				TANGER_OCX_OBJ.BeginOpenFromURL("<%=path%>/platform/bpm/clientbpmwordction/getword?processInstanceId=<%=processInstanceId%>&openType=wordEdit");
			}else{
				alert("没有正文模板，请添加正文模板！");
			}
		}
		
		//页面关闭前事件
		window.onbeforeunload = function (){
			if(isSave) {
				if (window.confirm("需要保存文件吗？")) {
					doSave();
				} else {
					var formUrl = "<%=path%>/platform/bpm/clientbpmwordction/updatewordlockuser?processInstanceId=<%=processInstanceId%>";
					document.forms[0].action = encodeURI(formUrl);
					TANGER_OCX_SaveDocUnprotect(<%=strAuthType%>);
				}
	        }
		};
		
		//关闭
		function doClose(){
			alert("文件保存成功！");
			isSave = false;
			window.close();
		}
		
		//得到正文拥有的权限
		var lockUser = "";//文档锁定人
		var userName = "";//文档处理人
		var wordRevisions = false;//编辑时留痕
		var wordShowRevisions = false;//显示留痕
		var wordSeal = false;//加盖公章
		var wordFieldArray = "";//同步域
		function getWordRight(docRight) {
			if (docRight.lockUser) lockUser = docRight.lockUser;
			if (docRight.userName) userName = docRight.userName;
			if (docRight.wordRevisions) wordRevisions = true;
			if (docRight.wordShowRevisions) wordShowRevisions = true;
			if (docRight.wordSeal) wordSeal = true;
			if (docRight.wordFieldName) wordFieldArray = docRight.wordFieldName;
		}
		
		//控制
		function ShowTitleBar() {
			if (lockUser != "nobody") {
				TANGER_OCX_OBJ.Toolbars = false;
				TANGER_OCX_EnableFileSaveMenu(false);
				isSave = false;
				alert("文档正在被其他用户编辑中，请稍后再试。");
			} else {
				TANGER_OCX_EnableFileNewMenu(false);
				TANGER_OCX_EnableFileOpenMenu(false);
				TANGER_OCX_EnableFilePropertiesMenu(false);
				TANGER_OCX_EnableFileCloseMenu(false);
				TANGER_OCX_EnableFilePageSetupMenu(false);
				TANGER_OCX_EnableFileSaveMenu(true);
				initCustomMenus();//初始化自定义按钮
			}
			TANGER_OCX_EnableFileSaveAsMenu(true);
			TANGER_OCX_EnableFilePrintPreviewMenu(true);
			TANGER_OCX_EnableFilePrintMenu(true);
			
			//显示留痕
			TANGER_OCX_ShowRevisions(wordShowRevisions);
		
			//编辑时留痕
			TrackRevisions(wordRevisions); 	
			if (wordRevisions == true && userName != "") TANGER_OCX_SetDocUser(userName);
		}
		
		//初始化自定义按钮
		function initCustomMenus() {
			if (wordShowRevisions) {
				TANGER_OCX_OBJ.AddCustomButtonOnMenu(0,"显示留痕",true);
				TANGER_OCX_OBJ.AddCustomButtonOnMenu(1,"显示清稿",true);
			}
			if (wordSeal) TANGER_OCX_OBJ.AddCustomButtonOnMenu(2,"加盖公章",true);
		}
		
		//得到公章列表
		function getWordSealList() {
			var url = '<%=path%>/avicit/platform6/bpmclient/word/WordSealSelect.jsp';
			var usd = new UserSelectDialog("worddialog","400","300",encodeURI(url) ,"印章列表");
				usd.show();
		}
		
		//加盖公章
		function doWordSeal(wordSealId) {
			$('#worddialog').dialog('close');
			if (wordSealId != null && wordSealId != "close") {
				AddSecSignFromURL("<%=path%>/platform/bpm/clientbpmwordction/getWordSeal?wordSealId="+wordSealId);
			}
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
		JsonToBookMark(wordFieldArray); ShowTitleBar();
	</script>
	<script language="JScript" for="TANGER_OCX" event="OnFileCommand(cmd,canceled)">
		if (cmd == 3) {
			document.all("TANGER_OCX").CancelLastCommand = true;//取消缺省的操作
			doSave();
		}
	</script>
	<script language="JScript" for="TANGER_OCX" event="OnCustomButtonOnMenuCmd(btnPos,btnCaption,btnCmdid)">
		//事件处理代码中可以直接引用控件属性或者方法，可以省略控件对象前缀
		if(btnCmdid == 0) {//"显示留痕"
			TANGER_OCX_ShowRevisions(true);
		} else if(btnCmdid == 1) {//"显示清稿"
			TANGER_OCX_ShowRevisions(false);
		} else if(btnCmdid == 2) {//"加盖公章"
			getWordSealList();
		}
	</script>

</form>
</body>
</html>

