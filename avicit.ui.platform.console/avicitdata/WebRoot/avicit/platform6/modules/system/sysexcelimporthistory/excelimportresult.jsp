<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Excel数据导入</title>
<base href="<%=ViewUtil.getRequestPath(request)%>">
<jsp:include page="/avicit/platform6/component/common/EasyUIJsInclude.jsp"></jsp:include>
<script src="static/js/platform/component/common/exportData.js" type="text/javascript"></script>
</head>
<body class="easyui-layout">
	<div data-options="region:'center',split:true,border:false" style="padding:0px;overflow:hidden;">
		<div id="toolbarImportResult" >
			<fieldset>
				<legend>查询条件</legend>
				<form id="search">
				<table class="tableForm" id="searchForm" width='100%'>
					<tr>
						<td>导入表：</td>
						<td><input class="easyui-validatebox" name="filter-like-SYS_LOOKUP_NAME" id="syslookUpName"/></td>
						<td>导入人：</td>
						<td><input class="easyui-validatebox" name="filter-like-OPETATOR_PERSON" id="optPerson"/></td>
						<td>
							<a class="easyui-linkbutton" onclick="searchData();" href="javascript:void(0);">查询</a>
							<a class="easyui-linkbutton" onclick="clearData();" href="javascript:void(0);">重置</a>
						</td>
					</tr>
				</table>
				</form>
		</fieldset>
			<table>
				<tr>
					<td><a class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="deleteResult();" href="javascript:void(0);">删除</a></td>
					<td><a class="easyui-linkbutton" iconCls="icon-undo" plain="true" onclick="returnMain();" href="javascript:void(0);">返回</a></td>
				</tr>
			</table>
		</div>
		<table id="dgImportResult"
			data-options="
				fit: true,
				border: false,
				rownumbers: true,
				animate: true,
				collapsible: false,
				fitColumns: true,
				autoRowHeight: false,
				toolbar:'#toolbarImportResult',
				idField :'id',
				singleSelect: true,
				checkOnSelect: true,
				selectOnCheck: false,
				pagination:true,
				pageSize:10,
				pageList:[5,10],
				striped:true,
				url:'platform/excelImportResult/allImportResults.json'
			">
			<thead>
				<tr>
					<th data-options="field:'id', halign:'center',checkbox:true,width:100">id</th>
					<th data-options="field:'sysLookupName', halign:'center',align:'center',width:100">导入表</th>
					<th data-options="field:'totalCount', halign:'center',align:'center',width:60">总数</th>
					<th data-options="field:'successCount', halign:'center',align:'center',width:60">成功数</th>
					<th data-options="field:'failCount', halign:'center',align:'center',width:60">失败数</th>
					<th data-options="field:'opetatorPerson', halign:'center',align:'center',width:80">导入人</th>
					<th data-options="field:'creationDate', halign:'center',align:'center',width:120">导入时间</th>
					<th data-options="field:'fileUrl', halign:'center',align:'center',width:100,formatter:formatURL">下载</th>
				</tr>
			</thead>
			</table>  
		</table>  
	</div>
<script type="text/javascript">
	var dg;
	$(function(){
		dg=$('#dgImportResult').datagrid();
		$('#search').find('input').on('keyup',function(e){
			if(e.keyCode == 13){
				searchData();
			}
		});
	});
	//去后台查询
	function searchData(){
		dg.datagrid('load',{ param : JSON.stringify(serializeObject($('#search')))});
	}
	/*清空查询条件*/
	function clearData(){
		$('#syslookUpName').val('');
		$('#optPerson').val('');
		searchData();
	};
	function formatURL(value,row,index){
		if(!value)
			return "<span style='color:green'>导入成功</span>";
		return "<span style='color:red;cursor:pointer;'onclick='downLoadFile(\""+value+"\");'>失败【查看】</span>";
	}
	function downLoadFile(value){
		
		var url = "platform/excelImportController/downErroeFile?fileName="+value;
		var t = new DownLoad4URL('iframe','form',null,url);
		t.downLoad();
	
	}
	//删除导入记录
	function deleteResult(){
		var rows = dg.datagrid('getChecked');
		var ids = [];
		var l=rows.length;
		if (rows.length > 0) {
			$.messager.confirm('请确认','您确定要删除当前所选的数据？',
				function(b){
					if(b){
						for (;l--;) {
							ids.push(rows[l].id);
						}
						$.ajax({
							url : 'platform/excelImportResult/deleteImportResults',
							data : {
								ids : ids.join(',')
							},
							type : 'post',
							dataType : 'json',
							success : function(result) {
								if (result.flag == "success") {
									var dtTemp=dg.datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
									dtTemp.datagrid('reload');
									$.messager.show({
										title : '提示',
										msg : '删除成功',
										timeout:2000,  
								        showType:'slide'
									});
								}
							}
						});
					}
				});
		} else {
			$.messager.alert('提示', '请选择要删除的记录！', 'warning');
		}
	}
	//返回主页面
	function returnMain(){
		var f = parent&&parent.closeDataResult;
		if(typeof(f)!=='undefined'){
			f();
		}
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
		//先销毁对象，再创建
		if(jQuery("#" + this.iframeName).length > 0 ){
			jQuery("#" + this.iframeName).remove();
			
		}
		
		//先销毁对象，再创建
		if(jQuery("#" + this.formName).length > 0 ){
			jQuery("#" + this.formName).remove();
		}
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