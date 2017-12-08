<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Excel数据导入</title>
<base href="<%=ViewUtil.getRequestPath(request)%>">
<jsp:include page="/avicit/platform6/component/common/EasyUIJsInclude.jsp"></jsp:include>
</head>
<body class="easyui-layout">
	<div data-options="region:'center',split:true,border:false">
		<div id="toolbarExcel" class="datagrid-toolbar">
			<table border="0" cellspacing="1">
				<tr>
					<td><a class="easyui-linkbutton" iconCls="icon-excelimport" plain="true" onclick="downLoadTemplater();" href="javascript:void(0);">下载导入模版</a></td>
					<td><a class="easyui-linkbutton" iconCls="icon-setting" plain="true" onclick="importExcel();" href="javascript:void(0);">导入</a></td>
					<td><a class="easyui-linkbutton" iconCls="icon-mulwindow" plain="true" onclick="showResult();" href="javascript:void(0);">查看导入结果</a></td>
					<td><a class="easyui-linkbutton" iconCls="icon-undo" plain="true" onclick="back();" href="javascript:void(0);">返回</a></td>
				</tr>
			</table>
		</div>
		<form id="importExcel" style="padding-top:5px;" action="">
			<input type=hidden id="id" name ="id" value="${excelFileId}"/>
			<%-- <table border="0" cellspacing="1">
				<tr>
					<td align="right" style="padding-right:5px;">导入开始行</td>
					<td align="left"><input class="easyui-numberbox" data-options="min:1" type="text" name="initNo" id="initNo" value="1"/></td>
					<td algin="right" width="60">导入类型</td>
					<td>
						<c:forEach items="${type}" var="type">
							<input type="radio" id="type${type.lookupCode}" name="type" value="${type.lookupCode}"<c:if test="${type.lookupCode eq defOpt}">checked="true"</c:if> />
								<span><label for="type${type.lookupCode}">${type.lookupName}</label></span>
						</c:forEach>
					</td>
				</tr>
			</table> --%>
		</form>
		<form id="uploadExcel" style="padding-top:5px;" enctype="multipart/form-data" method="post">
			<table width="100%" border="0" cellspacing="1">
				<tr>
					<td align="right" width="80" style="padding-right:5px;">导入的文件</td>
					<td align="left" width="155"><input type=file name="impExcelFile" id="impExcelFile"/></td>
					<td><a class="easyui-linkbutton" iconCls="icon-upload" plain="false" name="upload" id="upload" onclick="uploadExcel();" href="javascript:void(0);">上传</a></td>
				</tr>
			</table>
		</form>
	</div>
<script type="text/javascript">
	var canImport;
	//查看导入结果
	function showResult(){
		var imp = new CommonDialog("importDataResult","700","350",'platform/excelImportResult/toManager',"查看导入结果",false,true,false);
		imp.show();
	}
	//关闭导入结果查看对话框
	function closeDataResult(){
		$("#importDataResult").dialog('close');
	}
	
	function back(){
		var f = parent&&parent.closeImportData;
		if(typeof(f)!=='undefined'){
			f();
		}
	}
	function downLoadTemplater(){
		var url = 'platform/excelImportController/templateDown?fileName=${fileName}.${ext}';
		var t = new DownLoad4URL('iframe','form',null,url);
		t.downLoad();
	}
	function importExcel(){
		if(canImport){
			$.messager.progress();	// 显示进度条
			$.ajax({
				url : '${path}',
				data : {datas : JSON.stringify(serializeObject($('#importExcel')))},
				async :true,
				type : 'post',
				dataType : 'json',
				success : function(result) {
					$.messager.progress('close');
					if (result.flag == "success") {
						$.messager.alert('提示',result.msg,'info');
					}else {
						var msg ='操作失败！';
						if(result.error){
							msg += '原因：'+result.error;
						}
						//showMessager(msg);
					}
				}
			});
			return;	
		}
		$.messager.alert('警告','请先上传文件再导入！','warning');
	}
	/*上传到服务器  */
	function uploadExcel(){
		if(document.getElementById("impExcelFile").value != ''){
			if(checkfiletype('impExcelFile')){
				$.messager.progress();	// 显示进度条
				$('#uploadExcel').form('submit', {
					url: 'platform/excelImportController/uploadExcel/${excelFileId}',
					success: function(r){
						$.messager.progress('close');	// 如果提交成功则隐藏进度条
						if(r){
							$.messager.alert('出错',r,'error');
							canImport=false;
						}else{
							$.messager.alert('提示','文件上传成功!','info');
							canImport=true;
						}
					}
				});
				return;
			}
		} else {
			$.messager.alert('警告','请选择要上传的excel文件!','warning');
			return;
		}
	}
	//检查上传类型
	function checkfiletype(id){
	    var fileName = document.getElementById(id).value;
	    //设置文件类型数组
	    var extArray =[".xls",".xlsx"];
	   	//获取文件名称
	   	while (fileName.indexOf("//") != -1)
	    	fileName = fileName.slice(fileName.indexOf("//") + 1);
	       	//获取文件扩展名
	       	var ext = fileName.slice(fileName.indexOf(".")).toLowerCase();
	   		//遍历文件类型
	       	var count = extArray.length;
	   		for (;count--;){
	     		if (extArray[count] == ext){ 
	       			return true;
	     		}
	   		}  
	   		$.messager.alert('错误','只能上传下列类型的文件: '  + extArray.join(" "),'error');
	   return false;  
	}
	
	/**
	 * 模拟ajax导出
	 * 无弹出框,post提交无参数限制
	 * @param iframeId
	 * @param formId
	 * @param headData
	 */
	 function DownLoad4URL(iframeId,formId,headData,actionUrl){
		 //设置是否显示遮罩Iframe
		 if(typeof(iframeId)!=='string'&&iframeId.trim()!==''){
			 throw new Error("iframeId不能为空！");
		 }
		 
		//设置是否显示遮罩Iframe
		if(typeof(formId)!=='string'&&formId.trim()!==''){
			throw new Error("formId不能为空！");
		}
		this.iframeName = "_iframe_" + iframeId;
		this.formName = "_form_" + formId;
		this.doc = document;//缓存全局对象，提高效率
		this.createDom.call(this,headData,actionUrl);
	};
	DownLoad4URL.prototype.downLoad=function(){
		this.doc.getElementById(this.formName).submit();
	};
	DownLoad4URL.prototype.createDom=function(headData,url){
		if(jQuery("#" + this.iframeName).length == 0){
			var exportIframe = this.doc.createElement("iframe"); 
			exportIframe.id = this.iframeName; 
			exportIframe.name = this.iframeName; 
			exportIframe.style.display = 'none'; 
			this.doc.body.appendChild(exportIframe); 
			//创建form 
			var exportForm = this.doc.createElement("form"); 
			exportForm.method = 'post'; 
			exportForm.action = url; 
			exportForm.name = this.formName; 
			exportForm.id = this.formName;
			exportForm.target = this.iframeName;
			this.doc.body.appendChild(exportForm); 
			if(headData&&typeof(headData)==='object'){
				for (var key in headData){
				 var headInput = this.doc.createElement("input"); 
				 headInput.setAttribute("name",key); 
				 headInput.setAttribute("type","text"); 
				 if(typeof(headData[key])=='string'){
					 headInput.setAttribute("value",headData[key]);
				 }else{
					 var jsonStr=JSON.stringify(headData[key]);
					 headInput.setAttribute("value",jsonStr);
				 }
				 exportForm.appendChild(headInput); 
				}
			}
		}
	};
</script>	
</body>
</html>