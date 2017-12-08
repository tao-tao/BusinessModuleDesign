<%@page import="avicit.platform6.bpmclient.dto.sys.HistoryTaskVo"%>
<%@page import="avicit.platform6.bpmclient.dto.sys.HistoryTask"%>
<%@page import="avicit.platform6.bpmclient.bpm.service.BpmDisplayService"%>
<%@page import="avicit.platform6.bpm.api.BpmToolService"%>
<%@page import="avicit.platform6.api.session.SessionHelper"%>
<%@page import="avicit.platform6.bpmclient.dto.sys.HistoryTask"%>
<%@page import="avicit.platform6.bpm.api.op.DocRightDefinition"%>
<%@page import="avicit.platform6.modules.system.sysfileupload.service.SwfUploadService"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="avicit.platform6.core.spring.SpringFactory"%>
<%@page import="avicit.platform6.commons.utils.ComUtil"%>
<%@page import="java.util.*"%>


<%
	String file_size_limit = ComUtil.replaceNull2Space(request.getParameter("file_size_limit"));
	String file_types = ComUtil.replaceNull2Space(request.getParameter("file_types"));
	String file_upload_limit = ComUtil.replaceNull2Space(request.getParameter("file_upload_limit"));
	String save_type = ComUtil.replaceNull2Space(request.getParameter("save_type"));
	String form_id = ComUtil.replaceNull2Space(request.getParameter("form_id"));
	String form_code = ComUtil.replaceNull2Space(request.getParameter("form_code"));
	String form_field = ComUtil.replaceNull2Space(request.getParameter("form_field"));
	String allowAdd = ComUtil.replaceNull2Space(request.getParameter("allowAdd"));
	String allowDel = ComUtil.replaceNull2Space(request.getParameter("allowDel"));
	
	String entryId = ComUtil.replaceNull2Space(request.getParameter("entryId"));
	String executionId = ComUtil.replaceNull2Space(request.getParameter("executionId"));
	
	String cleanOnExit = ComUtil.replaceNull2Space(request.getParameter("cleanOnExit"));
	String file_category = ComUtil.replaceNull2Space(request.getParameter("file_category"));
	String secret_level = ComUtil.replaceNull2Space(request.getParameter("secret_level"));
	SwfUploadService swfUploadService = (SwfUploadService) SpringFactory.getBean(SwfUploadService.class);
	if("process".equals(allowAdd)){
		BpmToolService bpmToolService = (BpmToolService) SpringFactory.getBean(BpmToolService.class);
		BpmDisplayService bpmDisplayService = (BpmDisplayService) SpringFactory.getBean(BpmDisplayService.class);
		String states = bpmToolService.getProcessStateByFormId(form_id);
		if(states==null || "".equals(states) || "结束".equals(states)){
			allowDel = "false";
			allowAdd = "false";
		}else{
			if(entryId==null || "".equals(entryId)){
				String userId = SessionHelper.getLoginSysUserId(request);
				List<HistoryTaskVo> taskList = bpmDisplayService.getProcessDetailParameter(form_id,userId);
				for(HistoryTaskVo h:taskList){
					entryId = h.getProcessInstance() + "";
					executionId = h.getExecutionId();
					break;
				}
			}
			Map<String,Object> map = bpmToolService.getDocRightDefinitionByExecutionId(entryId,executionId);
			List<DocRightDefinition> list = (List<DocRightDefinition>) map.get("docRight");
			for(DocRightDefinition  dr : list){
				String type = dr.getType();
				if("attachCreate".equals(type)){
					allowDel = "true";
					allowAdd = "true";
				}
				if("attachEdit".equals(type)){
					
				}
				if("attachPrint".equals(type)){
					
				}
			}
		}
	}
	
	
	
	Map<String,String> para = new HashMap<String,String>();
	para.put("formId", form_id);
	para.put("allowDel", allowDel);
	para.put("secretLevel", secret_level);
	para.put("fileCategory", file_category);
	
	Map<String,String> result = swfUploadService.getAttachHtml(para,SessionHelper.getLoginSysUser(request));
	String attachHtml = result.get("html");
	String fileCategory = result.get("fileCategory");
	String fileSecret = result.get("fileSecret");
%>
<script type="text/javascript">
var baseurl = "<%=request.getContextPath()%>";
var file_size_limit = "<%=file_size_limit%>";
var file_types = "<%=file_types%>";
var file_upload_limit = "<%=file_upload_limit%>";
var save_type = "<%=save_type%>";
var form_id = "<%=form_id%>";
var form_code = "<%=form_code%>";
var form_field = "<%=form_field%>";
var allowAdd = "<%=allowAdd%>";
var allowDel = "<%=allowDel%>";
var cleanOnExit = "<%=cleanOnExit%>";
var file_category = "<%=file_category%>";
var secret_level = "<%=secret_level%>";
fileCategory = eval('('+'<%=fileCategory%>'+')');
fileSecret = eval('('+'<%=fileSecret%>'+')');
</script>
<link href="avicit/platform6/modules/system/swfupload/swf/css/default.css"
	rel="stylesheet" type="text/css" />
<script type="text/javascript"
	src="avicit/platform6/modules/system/swfupload/swf/js/swfupload.js"></script>
<script type="text/javascript"
	src="avicit/platform6/modules/system/swfupload/swf/js/swfupload.swfobject.js"></script>
<script type="text/javascript"
	src="avicit/platform6/modules/system/swfupload/swf/js/swfupload.queue.js"></script>
<script type="text/javascript"
	src="avicit/platform6/modules/system/swfupload/swf/js/fileprogress.js"></script>
<script type="text/javascript"
	src="avicit/platform6/modules/system/swfupload/swf/js/handlers.js"></script>
<script type="text/javascript"
	src="avicit/platform6/modules/system/swfupload/swf/js/myswfupload.js"></script>
<script type="text/javascript"
	src="avicit/platform6/modules/system/attachment/jquerymutilupload/jquery.MultiFile.Edit.js"></script>
<script type="text/javascript">
	var baseurl = "<%=request.getContextPath()%>";
	function swfUploadLoaded() {
		clearTimeout(this.customSettings.loadingTimeout);
		document.getElementById("divLoadingContent").style.display = "none";
		document.getElementById("divLongLoading").style.display = "none";
		document.getElementById("divAlternateContent").style.display = "none";
		var stats = swfu.getStats();
		stats.successful_uploads = <%=result.get("fileSize")%>;
		swfu.setStats(stats);
	}

	function upload() {
		if (swfu.getStats().files_queued > 0) {
			swfu.startUpload();
		}
	}

	function deleteFile(fileId, fileName, obj) {
		//进行删除确认
		if (confirm("您要删除选中的附件吗?")) {
			obj.innerText = "删除中...";
			//修改删除链接
			$.ajax({
						type : 'GET',
						url : "platform/swfUploadController/doDelete",
						data : '&pkId=' + fileId + "&fileuploadBusinessId="
								+ form_id + "&fileuploadBusinessTableName="
								+ form_code + "&fileuploadIsSaveToDatabase="
								+ save_type + "&fileName=",
						success : function() {
							var stats = swfu.getStats();
							stats.successful_uploads--;
							swfu.setStats(stats);
							obj.parentNode.parentNode.parentNode.parentNode
									.removeChild(obj.parentNode.parentNode.parentNode);
							document.getElementById("shengyu").innerHTML = (file_upload_limit - stats.successful_uploads);
						}
					});
		}
	}

	//小心使用，也可不用此方法，而是去手工删除附件
	//保存后务必将此修改为true，否则退出页面时附件会被删除，默认为false
	var saveSuccess = true;
	window.onbeforeunload = function() {
		//如果是新增页面，且未保存数据，那么关闭页面时调用清除附件的方法
		if (cleanOnExit == "true" && !saveSuccess) {
			clearFiles();
		}
	};

	/**
	 *清除此formID下所有的附件、
	 */
	function clearFiles() {
		$.ajax({
			type : 'GET',
			url : "platform/swfUploadController/doDeleteByFormId",
			data : "&fileuploadBusinessId=" + form_id,
			success : function() {
			}
		});
	}
	
	function downloadFile(fileId,formId,formTable,saveType){
		window.open("<%=request.getContextPath()%>/platform/swfUploadController/doDownload?fileuploadBusinessId="+formId+"&fileuploadBusinessTableName="+formTable+"&fileuploadIsSaveToDatabase="+saveType+"&fileId="+fileId,"_blank");
	}
</script>
<form id="swfform" name="swfform" method="post"
	enctype="multipart/form-data">
	<table width="95%" cellspacing="1" cellpadding="1" border="0"
		align="center">
		<tr>
			<td>
				<div id="uploadDiv">
					<table cellpadding="1" cellspacing="1">
						<tr style="font-size: 12px; font-weight: normal; color: aaaaaa;">
							<td rowspan="2"><span id="spanButtonPlaceholder"></span></td>
							<td><input id="btnUpload" type="button" onclick="upload();"
								value="上传" class="btn-upload"></td>
						</tr>
					</table>
				</div>
			</td>
		</tr>
		<tr>
			<td>
				<TABLE id="idFileList" width="100%" border="0" cellpadding="1"
					cellspacing="1">
					<tr class="newUploadTitle">
							<th align="center" nowrap>
								附件名称
							</th>
							<% if(file_category!=null && !file_category.equals("")) {%>
							<th align="center" nowrap>
								附件类型
							</th>
							<%} %>
							<% if(secret_level!=null && !secret_level.equals("")) {%>
							<th align="center" nowrap>
								附件密级
							</th>
							<%} %>
							<th align="center" nowrap>
								附件大小
							</th>
							<th align="center" nowrap>
								状态
							</th>
							<th  align="center" nowrap>
								操作
							</th>
						</tr>
					<%=attachHtml%>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<div id='isDisplayNum'>
				<%
					if(file_upload_limit!=null && Integer.parseInt(file_upload_limit)>0){
				%> (最多<%=file_upload_limit%>个附件,您还可以上传<span id="shengyu"><%=(Integer.parseInt(file_upload_limit)-Integer.parseInt(result.get("fileSize").toString()))%></span>个附件)
				<%
					}
				%>
				</div>
			</td>
		</tr>
	</table>
</form>

<div id="uploadCountDiv" style="padding-top: 5px; display: none;">
	<div style="display: none">
		</span>等待上传 <span id="idFileListCount">0</span> 个 ，已上传 <span
			id="idFileListSuccessUploadCount">0</span> 个。&nbsp;&nbsp;
	</div>
	<div id="divSWFUploadUI" style="visibility: hidden;"></div>
	<noscript
		style="display: block; margin: 10px 25px; padding: 10px 15px;">
		很抱歉，附件上传界面无法载入，请将浏览器设置成支持JavaScript。</noscript>
	<div id="divLoadingContent" class="content"
		style="background-color: #FFFF66; border-top: solid 4px #FF9966; border-bottom: solid 4px #FF9966; margin: 10px 25px; padding: 10px 15px; display: none;">
		附件上传界面正在载入，请稍后...</div>
	<div id="divLongLoading" class="content"
		style="background-color: #FFFF66; border-top: solid 4px #FF9966; border-bottom: solid 4px #FF9966; margin: 10px 25px; padding: 10px 15px; display: none;">
		附件上传界面载入失败，请确保浏览器已经开启对JavaScript的支持，并且已经安装可以工作的Flash插件版本。</div>
	<div id="divAlternateContent" class="content"
		style="background-color: #FFFF66; border-top: solid 4px #FF9966; border-bottom: solid 4px #FF9966; margin: 10px 25px; padding: 10px 15px; display: none;">
		很抱歉，附件上传界面无法载入，请安装或者升级您的Flash插件</div>
</div>
<script>
	if (allowAdd == "true") {
		document.getElementById("uploadDiv").style.display = "block";
		document.getElementById("uploadCountDiv").style.display = "block";
		document.getElementById("btnUpload").style.display = "block";
	} else {
		document.getElementById("uploadDiv").style.display = "none";
		document.getElementById("uploadCountDiv").style.display = "none";
		document.getElementById("btnUpload").style.display = "none";
		document.getElementById("isDisplayNum").style.display = "none";
		
	}
</script>
