/* Demo Note:  This demo uses a FileProgress class that handles the UI for displaying the file name and percent complete.
The FileProgress class is not part of SWFUpload.
*/


/* **********************
   Event Handlers
   These are my custom event handlers to make my
   web application behave the way I went when SWFUpload
   completes different tasks.  These aren't part of the SWFUpload
   package.  They are part of my application.  Without these none
   of the actions SWFUpload makes will show up in my application.
   ********************** */

function swfUploadPreLoad() {
	var self = this;
	var loading = function () {
		document.getElementById("divLoadingContent").style.display = "";

		var longLoad = function () {
			document.getElementById("divLoadingContent").style.display = "none";
			document.getElementById("divLongLoading").style.display = "";
		};
		this.customSettings.loadingTimeout = setTimeout(function () {
				longLoad.call(self)
			},
			15 * 1000
		);
	};
	
	this.customSettings.loadingTimeout = setTimeout(function () {
			loading.call(self);
		},
		1*1000
	);
}
   
function swfUploadLoadFailed() {
	clearTimeout(this.customSettings.loadingTimeout);
	document.getElementById("divLoadingContent").style.display = "none";
	document.getElementById("divLongLoading").style.display = "none";
	document.getElementById("divAlternateContent").style.display = "";
}
   
   
function fileQueued(file) {
	try {
		FileProgress(file, this.customSettings.myFileListTarget,this,this.customSettings.selectHtml,this.customSettings.userName);
	} catch (ex) {
		this.debug(ex);
	}
}

function fileQueueError(file, errorCode, message) {
	try {
		
		if (errorCode === SWFUpload.QUEUE_ERROR.QUEUE_LIMIT_EXCEEDED) {
			alert("超过附件数量限制。  " +  (message > 1 ? "您仅能添加 " +  message + " 个文件。" : "您不能添加更多的文件。"));
			return;
		}
		
		FileProgress(file, this.customSettings.myFileListTarget,this);
		var tr = document.getElementById(file.id);
		tr.style.color="red";
		var bar = document.getElementById(file.id+"_bar");
		var errInfo = "添加失败";

		switch (errorCode) {
		case SWFUpload.QUEUE_ERROR.FILE_EXCEEDS_SIZE_LIMIT:
			//progress.setStatus("File is too big.");
			errInfo = errInfo + ":文件太大";
			this.debug("Error Code: File too big, File name: " + file.name + ", File size: " + file.size + ", Message: " + message);
			break;
		case SWFUpload.QUEUE_ERROR.ZERO_BYTE_FILE:
			//progress.setStatus("Cannot upload Zero Byte files.");
			errInfo = errInfo + ":0字节文件";
			this.debug("Error Code: Zero byte file, File name: " + file.name + ", File size: " + file.size + ", Message: " + message);
			break;
		case SWFUpload.QUEUE_ERROR.INVALID_FILETYPE:
			//progress.setStatus("Invalid File Type.");
			errInfo = errInfo + ":文件类型错误";
			this.debug("Error Code: Invalid File Type, File name: " + file.name + ", File size: " + file.size + ", Message: " + message);
			break;
		default:
			if (file !== null) {
				//progress.setStatus("Unhandled Error");
				errInfo = errInfo + ":系统未知错误";
			}
			this.debug("Error Code: " + errorCode + ", File name: " + file.name + ", File size: " + file.size + ", Message: " + message);
			break;
		}
		
		bar.parentNode.innerHTML="<center>"+errInfo+"</center>";
		var delObject = document.getElementById(file.id+"_del");
		delObject.parentNode.innerHTML="&nbsp;";
		swfUploadInstance.cancelUpload(file.id);
	} catch (ex) {
        this.debug(ex);
    }
}

function fileDialogComplete(numFilesSelected, numFilesQueued) {
	try {
		if (numFilesSelected > 0) {
			//document.getElementById(this.customSettings.cancelButtonId).disabled = false;
		}
		
		/* I want auto start the upload and I can do that here */
		//add by stephen
		if(this.settings.auto_upload){//是否要上传
			this.startUpload();
		}
	} catch (ex)  {
        this.debug(ex);
	}
}

function uploadStart(file) {
	try {
		/* I don't want to do any file validation or anything,  I'll just update the UI and
		return true to indicate that the upload should start.
		It's important to update the UI here because in Linux no uploadProgress events are called. The best
		we can do is say we are uploading.
		 */
		var progress = new FileProgress(file, this.customSettings.progressTarget);
		progress.setStatus("Uploading...");
		progress.toggleCancel(true, this);
	}
	catch (ex) {}
	
	return true;
}

function uploadProgress(file, bytesLoaded, bytesTotal) {
	try {
		var percent = Math.ceil((bytesLoaded / bytesTotal) * 100);
		var bar = document.getElementById(file.id+"_bar");
		bar.style.width=percent+"%";
		var per = document.getElementById(file.id+"_per");
		per.innerHTML = "("+percent+"%)";
		var tr = document.getElementById(file.id);
	} catch (ex) {
		this.debug(ex);
	}
}

function uploadSuccess(file, serverData) {
	var isSuccess = (serverData.indexOf("successed")!=-1?true:false);
	try {
		if(isSuccess){
			var icon = au.countImage(file.name);
			var tr = document.getElementById(file.id);
			tr.style.color="green";
			var attId = serverData.substring("successed".length,serverData.indexOf("<"));
			//dingrc  增加afterUploadGetFileId回调方法传fileid回去
			if(typeof(afterUploadGetFileId)=="function"){
				afterUploadGetFileId(attId);
			}
           // document.getElementById(file.id+"_id").value =attId; 
			var bar = document.getElementById(file.id+"_id");
			bar.parentNode.innerHTML= "<INPUT TYPE='hidden' id= '"+file.id+"_id'"+" NAME='attName' value='"+file.name+"'><img src='"+icon+"' class='new-ico-file'/>"
				+"<a href='platform/swfUploadController/doDownload?fileuploadBusinessId="+attId+"&fileuploadBusinessTableName="+form_code+"&fileuploadIsSaveToDatabase="+save_type+"&fileId="+attId+"'  class='link-black'>"+file.name+"</a>";
			var bar = document.getElementById(file.id+"_bar");
			bar.parentNode.innerHTML="<center>上传成功</center>";
			var delObject = document.getElementById(file.id+"_del");
			delObject.parentNode.innerHTML="<span id='lb"+attId+"'><label  class='link-red' onclick='deleteFile(\""+attId+"\",\""+file.name+"\",this)'>删除</label></span>";
			document.getElementById(this.customSettings.myFileListTarget+"Count").innerHTML=this.getStats().files_queued;
			document.getElementById("shengyu").innerHTML = (file_upload_limit - swfu.getStats().successful_uploads);
			
			if(file_category!=""){
				var sc = document.getElementById("category_"+file.id);
				var tsc = sc.options[sc.options.selectedIndex].text;
				sc.parentNode.innerHTML=tsc;
			}
			if(secret_level!=""){
				var lc = document.getElementById("secret_"+file.id);
				var tlc = lc.options[lc.options.selectedIndex].text;
				lc.parentNode.innerHTML=tlc;
			}
			
			
		}else{
			var tr = document.getElementById(file.id);
			tr.style.color="red";
			var bar = document.getElementById(file.id+"_bar");
			bar.parentNode.innerHTML="<center>上传失败</center>";
			var delObject = document.getElementById(file.id+"_del");
			delObject.parentNode.innerHTML="&nbsp;";
		}
	} catch (ex) {
		this.debug(ex);
	}
	/*
	try {
		var progress = new FileProgress(file, this.customSettings.progressTarget);
		progress.setComplete();
		progress.setStatus("Complete.");
		progress.toggleCancel(false);

	} catch (ex) {
		this.debug(ex);
	}
	*/
}

function uploadError(file, errorCode, message) {
	try {
		var progress = new FileProgress(file, this.customSettings.progressTarget);
		progress.setError();
		progress.toggleCancel(false);

		switch (errorCode) {
		case SWFUpload.UPLOAD_ERROR.HTTP_ERROR:
			progress.setStatus("上传错误: " + message);
			this.debug("Error Code: HTTP Error, File name: " + file.name + ", Message: " + message);
			break;
		case SWFUpload.UPLOAD_ERROR.MISSING_UPLOAD_URL:
			progress.setStatus("Configuration Error");
			this.debug("Error Code: No backend file, File name: " + file.name + ", Message: " + message);
			break;
		case SWFUpload.UPLOAD_ERROR.UPLOAD_FAILED:
			progress.setStatus("上传失败");
			this.debug("Error Code: Upload Failed, File name: " + file.name + ", File size: " + file.size + ", Message: " + message);
			break;
		case SWFUpload.UPLOAD_ERROR.IO_ERROR:
			progress.setStatus("Server (IO) Error");
			this.debug("Error Code: IO Error, File name: " + file.name + ", Message: " + message);
			break;
		case SWFUpload.UPLOAD_ERROR.SECURITY_ERROR:
			progress.setStatus("安全性错误");
			this.debug("Error Code: Security Error, File name: " + file.name + ", Message: " + message);
			break;
		case SWFUpload.UPLOAD_ERROR.UPLOAD_LIMIT_EXCEEDED:
			progress.setStatus("超过上传限制");
			this.debug("Error Code: Upload Limit Exceeded, File name: " + file.name + ", File size: " + file.size + ", Message: " + message);
			break;
		case SWFUpload.UPLOAD_ERROR.SPECIFIED_FILE_ID_NOT_FOUND:
			progress.setStatus("文件没有找到");
			this.debug("Error Code: The file was not found, File name: " + file.name + ", File size: " + file.size + ", Message: " + message);
			break;
		case SWFUpload.UPLOAD_ERROR.FILE_VALIDATION_FAILED:
			progress.setStatus("验证失败，上传跳过");
			this.debug("Error Code: File Validation Failed, File name: " + file.name + ", File size: " + file.size + ", Message: " + message);
			break;
		case SWFUpload.UPLOAD_ERROR.FILE_CANCELLED:
			if (this.getStats().files_queued === 0) {
				//document.getElementById(this.customSettings.cancelButtonId).disabled = true;
			}
			progress.setStatus("取消");
			progress.setCancelled();
			break;
		case SWFUpload.UPLOAD_ERROR.UPLOAD_STOPPED:
			progress.setStatus("停止");
			break;
		default:
			progress.setStatus("未知错误: " + error_code);
			this.debug("Error Code: " + errorCode + ", File name: " + file.name + ", File size: " + file.size + ", Message: " + message);
			break;
		}
	} catch (ex) {
        this.debug(ex);
    }
}

function uploadComplete(file) {
	if (this.getStats().files_queued === 0) {
		//document.getElementById(this.customSettings.cancelButtonId).disabled = true;
		if(typeof(afterUploadEvent)=="function"){
			//dingrc  增加file参数  file.name可以拿到文件名称
			afterUploadEvent(file);
		}
	}
}

// This event comes from the Queue Plugin
function queueComplete(numFilesUploaded) {
	document.getElementById(this.customSettings.myFileListTarget+"SuccessUploadCount").innerHTML=this.getStats().successful_uploads;//add by stephen
	/* comment by stephen
	var status = document.getElementById("divStatus");
	status.innerHTML = numFilesUploaded + " file" + (numFilesUploaded === 1 ? "" : "s") + " uploaded.";
	*/
}

//add by stephen 
//version 2.2 why remove follow function, i must add again.
var _K = 1024;
var _M = _K*1024;
function getNiceFileSize(bitnum){
	if(bitnum<_M){
		if(bitnum<_K){
			return bitnum+'B';
		}else{
			return Math.ceil(bitnum/_K)+'K';
		}
		
	}else{
		return Math.ceil(100*bitnum/_M)/100+'M';
	}
}

