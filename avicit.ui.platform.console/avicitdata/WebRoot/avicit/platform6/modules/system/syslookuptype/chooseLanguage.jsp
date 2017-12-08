<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>多语言设置</title>
<base href="<%=ViewUtil.getRequestPath(request)%>">
<jsp:include page="/avicit/platform6/component/common/EasyUIJsInclude.jsp"></jsp:include>
</head>
<body class="easyui-layout" fit="true">
	<div data-options="region:'center'" style="background:#ffffff;">
		<div id="toolbarLangauge" class="datagrid-toolbar">
		 	<table>
		 		<tr>
					<td><a class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="insert();" href="javascript:void(0);">添加</a></td>
					<td><a class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="language.save();" href="javascript:void(0);">保存</a></td>
					<td><a class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="language.del();" href="javascript:void(0);">删除</a></td>
					<td><a class="easyui-linkbutton" iconCls="icon-back" plain="true" onclick="back();" href="javascript:void(0);">返回</a></td>
				</tr>
		 	</table>
	 	</div>
	 	<table id="dgLanguage"
			data-options="
				fit: true,
				border: false,
				rownumbers: true,
				animate: true,
				collapsible: false,
				fitColumns: true,
				autoRowHeight: false,
				toolbar:'#toolbarLangauge',
				idField :'id',
				singleSelect: true,
				checkOnSelect: true,
				selectOnCheck: false,
				striped:true">
			<thead>
				<tr>
					<th data-options="field:'id', halign:'center',checkbox:true" width="220">id</th>
					<th data-options="field:'sysLanguageCode', required:true,align:'center',formatter:formateLanguage" 
					    editor="{type:'combobox',options:{panelHeight:'auto',editable:false,valueField:'code',textField:'name',onHidePanel:function(){return language.endEditing();}}}" width="220"><span style="color:red;">*</span>语言名称</th>
					<th data-options="field:'${name}',required:true,align:'center'" editor="{type:'text'}" width="220"><span style="color:red;">*</span>${display}</th>
					<th data-options="field:'description',align:'center',align:'center'" editor="{type:'text'}" width="220">描述</th>
				</tr>
			</thead>
		</table>
	</div>
	<script src="avicit/platform6/modules/system/syslookuptype/js/Language.js" type="text/javascript"></script>
	<script type="text/javascript">
		var language;
		function insert(){//神知道为啥要这样?
			language.insert();
		}
		$(function(){
			language= new Language('dgLanguage','${url}');
			if(${flag}){
				language.setSaveCallBack(parent.reload1);
			}else{
				language.setSaveCallBack(parent.reload2);
			}
		});
		function formateLanguage(value){
			return language.formateLanguage(value);
		}
		function back(){
			parent.closeL();
		}
	</script>
</body>
</html>