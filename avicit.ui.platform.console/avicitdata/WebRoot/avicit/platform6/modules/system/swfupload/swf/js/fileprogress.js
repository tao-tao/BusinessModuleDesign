// Constructor
// file is a SWFUpload file object
// targetID is the HTML element id attribute that the FileProgress HTML structure will be added to.
// Instantiating a new FileProgress object with an existing file will reuse/update the existing DOM elements
/*
*	选择文件后执行的方法.
*	@param file 选择的文件对象，已经经过flash控件处理过了
*	@param fileListID WEB界面显示已选文件信息的位置区域
*	@param swfUploadInstance flash控件对象
*	@reference [handler.js].fileQueued(file)
*	@reference [handler.js].fileQueueError(file)
*/
function FileProgress(file,fileListID,swfUploadInstance,selectHtml,userName) {
	var i = 0;//增加变量，修改平台上传附件时添加文件给表格增加斑马线 chenyg 2014-05-28
	var sameFlag = false;
	var files = document.getElementsByName("attName");
	for(var i=0;i<files.length;i++){
		//alert(files[i].value);
		if(files[i].value == file.name){
			sameFlag = true;
			break;
		}
	}
	if(sameFlag){
		swfUploadInstance.cancelUpload(file.id);
		alert("【"+file.name+"】存在同名附件，无法添加。");
		return;
	}
	if(!document.getElementById(file.id)){
		document.getElementById(fileListID+"Count").innerHTML=swfUploadInstance.getStats().files_queued;
		var tb = document.getElementById(fileListID);
		var tr = tb.insertRow(-1);
		tr.setAttribute("id",file.id);
		if(i%2==0){//修改平台上传附件，增加表格斑马线 chenyg 2014-05-28
			tr.className='datagrid-cell';
		}else{
			tr.className='datagrid-cello';
		}
		var td = tr.insertCell(-1);td.className='uploadTD';td.width="10%";td.align="left";
		var icon = au.countImage(file.name);
		td.innerHTML="<INPUT TYPE='hidden' id= '"+file.id+"_id'"+" name = 'attName' value='"+file.name+"'/><img src='"+icon+"' class='new-ico-file'/>"+file.name;

		/**
		var td = tr.insertCell(-1);td.className='uploadTD';td.align="center";
		var now = new Date();
		td.innerHTML= ((now.getYear() < 1900) ? (1900 + now.getYear()):now.getYear())+"-"+(now.getMonth()<9?"0"+(now.getMonth()+1):(now.getMonth()+1)) +"-"+((now.getDate()<10)?"0"+now.getDate():now.getDate());
	**/
		//alert(file.id);
		if(file_category!=""){
			var td = tr.insertCell(-1);td.className='uploadTD';td.align="center";td.width="10%";
			td.innerHTML= genfileCategory(file.id,file_category);
			setFilePara(file.id,"category",document.getElementById("category_"+file.id));
		}
		if(secret_level!=""){
			var td = tr.insertCell(-1);td.className='uploadTD';td.align="center";td.width="10%";
			td.innerHTML= genSecretSelect(file.id,secret_level);
			setFilePara(file.id,"secret",document.getElementById("secret_"+file.id));
		}
		
		td = tr.insertCell(-1);td.className='uploadTD';td.align="center";td.width="10%";
		if(file.size){
			td.innerHTML=getNiceFileSize(file.size);
		}else{
			td.innerHTML="0B";
		}
		
		td = tr.insertCell(-1);td.className='uploadTD';td.width="10%";
		td.innerHTML="<center><span id="+file.id+"_per>等待上传</span></center><span id="+file.id+"_bar class=uploadBar></span>";
		td = tr.insertCell(-1);td.className='uploadTD';td.align="center";td.width="10%";
		td.innerHTML="<span id="+file.id+"_del  class='link-red''>取消</span>";
		var delObject = document.getElementById(file.id+"_del");
		delObject.onclick = function () {
			swfUploadInstance.cancelUpload(file.id);
			var tr = document.getElementById(file.id);
			tr.style.color="red";
			var bar = document.getElementById(file.id+"_bar");
			var errInfo = "<center>已取消</center>";
			bar.parentNode.innerHTML=errInfo;
			var delObject = document.getElementById(file.id+"_del");
			delObject.parentNode.innerHTML="&nbsp;";
			document.getElementById(fileListID+"Count").innerHTML=swfUploadInstance.getStats().files_queued;
		};
		//alert(tb.innerHTML);
	}
	i++;
}
FileProgress.prototype.setProgress = function (percentage) {
	this.fileProgressElement.className = "progressContainer green";
	this.fileProgressElement.childNodes[3].className = "progressBarInProgress";
	this.fileProgressElement.childNodes[3].style.width = percentage + "%";
};
FileProgress.prototype.setComplete = function () {
	this.fileProgressElement.className = "progressContainer blue";
	this.fileProgressElement.childNodes[3].className = "progressBarComplete";
	this.fileProgressElement.childNodes[3].style.width = "";
	/* comment by stephen
	var oSelf = this;
	setTimeout(function () {
		oSelf.disappear();
	}, 10000);
	*/
};
FileProgress.prototype.setError = function () {
	this.fileProgressElement.className = "progressContainer red";
	this.fileProgressElement.childNodes[3].className = "progressBarError";
	this.fileProgressElement.childNodes[3].style.width = "";
	
	/* comment by stephen
	var oSelf = this;
	setTimeout(function () {
		oSelf.disappear();
	}, 5000);
	*/
};
FileProgress.prototype.setCancelled = function () {
	this.fileProgressElement.className = "progressContainer";
	this.fileProgressElement.childNodes[3].className = "progressBarError";
	this.fileProgressElement.childNodes[3].style.width = "";
	
	/* comment by stephen
	var oSelf = this;
	setTimeout(function () {
		oSelf.disappear();
	}, 2000);
	*/
};
FileProgress.prototype.setStatus = function (status) {
	this.fileProgressElement.childNodes[2].innerHTML = status;
};

// Show/Hide the cancel button
FileProgress.prototype.toggleCancel = function (show, swfUploadInstance) {
	this.fileProgressElement.childNodes[0].style.visibility = show ? "visible" : "hidden";
	if (swfUploadInstance) {
		var fileID = this.fileProgressID;
		this.fileProgressElement.childNodes[0].onclick = function () {
			swfUploadInstance.cancelUpload(fileID);
			return false;
		};
	}
};

// Fades out and clips away the FileProgress box.
FileProgress.prototype.disappear = function () {

	var reduceOpacityBy = 15;
	var reduceHeightBy = 4;
	var rate = 30;	// 15 fps

	if (this.opacity > 0) {
		this.opacity -= reduceOpacityBy;
		if (this.opacity < 0) {
			this.opacity = 0;
		}

		if (this.fileProgressWrapper.filters) {
			try {
				this.fileProgressWrapper.filters.item("DXImageTransform.Microsoft.Alpha").opacity = this.opacity;
			} catch (e) {
				// If it is not set initially, the browser will throw an error.  This will set it if it is not set yet.
				this.fileProgressWrapper.style.filter = "progid:DXImageTransform.Microsoft.Alpha(opacity=" + this.opacity + ")";
			}
		} else {
			this.fileProgressWrapper.style.opacity = this.opacity / 100;
		}
	}

	if (this.height > 0) {
		this.height -= reduceHeightBy;
		if (this.height < 0) {
			this.height = 0;
		}

		this.fileProgressWrapper.style.height = this.height + "px";
	}

	if (this.height > 0 || this.opacity > 0) {
		var oSelf = this;
		setTimeout(function () {
			oSelf.disappear();
		}, rate);
	} else {
		this.fileProgressWrapper.style.display = "none";
	}
};
genSecretSelect = function(fileId,secret_level){
	var selStr = "";
	var fileSecret = {};
	$.ajax({
		 url:'platform/syslookuptype/getLookUpCode/'+secret_level+'.json',
		 type : 'get',
		 async : false,
		 dataType : 'json',
		 success : function(r){
			 fileSecret=r;
		 }
	 });
	if(typeof(fileSecret)!="undefined"){
		selStr = "<select id='secret_"+fileId+"'  name='secret_"+fileId+"' onchange='javascript:setFilePara(\""+fileId+"\",\"secret\",this)'>";
		for(var i=0;i<fileSecret.length;i++){
			selStr+="<option value='"+fileSecret[i].lookupCode+"'>"+fileSecret[i].lookupName+"</option>";
		}
		selStr +="</select>";
	}
	return selStr;
};
genfileCategory = function(fileId,file_Category){
	var selStr ="";
	var fileCategory = {};
	$.ajax({
		 url:'platform/syslookuptype/getLookUpCode/'+file_Category+'.json',
		 type : 'get',
		 async : false,
		 dataType : 'json',
		 success : function(r){
			 fileSecret=r;
		 }
	 });
	if(typeof(fileCategory)!="undefined"){
		selStr = "<select id='category_"+fileId+"' name='category_"+fileId+"' onchange='javascript:setFilePara(\""+fileId+"\",\"category\",this)'>";
		for(var i=0;i<fileCategory.length;i++){
			selStr+="<option value='"+fileCategory[i].lookupCode+"'>"+fileCategory[i].lookupName+"</option>";
		}
		selStr +="</select>";
	}
	return selStr;
};
setFilePara = function(fileId,paraName,obj){
	swfu.addFileParam(fileId , paraName, obj.options[obj.options.selectedIndex].value);
};


