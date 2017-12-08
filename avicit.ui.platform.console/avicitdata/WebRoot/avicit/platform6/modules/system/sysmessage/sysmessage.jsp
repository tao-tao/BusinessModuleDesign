<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="avicit.platform6.commons.utils.ComUtil"%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>消息管理</title>
<base href="<%=ComUtil.getRequestPath(request) %>">
<link href="avicit/platform6/modules/system/sysdept/css/icon.css" type="text/css" rel="stylesheet">
<jsp:include page="/avicit/platform6/component/common/EasyUIJsInclude.jsp"></jsp:include>
<jsp:include page="/avicit/platform6/modules/system/commonpopup/commonSelectionHead.jsp"></jsp:include>
<script src="static/js/platform/component/common/exportData.js" type="text/javascript"></script>
</head>

<script src="avicit/platform6/modules/system/sysmessage/sysmessage.js" type="text/javascript"></script>

<body class="easyui-layout" fit="true">


	
	<div id="toolbar" class="datagrid-toolbar" style="height:auto;display: block;">
		<sec:accesscontrollist   hasPermission="3" domainObject="pmTaskInfo_tabPower_button_btAddMessage" >
			<a id="btAddMessage"  class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="showSendMessageDialog();" href="javascript:void(0);">发送消息</a>
		</sec:accesscontrollist>
		
		
		<sec:accesscontrollist   hasPermission="3" domainObject="pmTaskInfo_tabPower_button_btRead" >
			<a id="btRead"  class="easyui-linkbutton" iconCls="icon-setting" plain="true" onclick="makeRead(1);" href="javascript:void(0);">标记为已读</a>
		</sec:accesscontrollist>
		
		<sec:accesscontrollist   hasPermission="3" domainObject="pmTaskInfo_tabPower_button_btUnRead" >
			<a id="btUnRead"  class="easyui-linkbutton" iconCls="icon-setting" plain="true" onclick="makeRead(0);" href="javascript:void(0);">标记为未读</a>
		</sec:accesscontrollist>
		
		<sec:accesscontrollist   hasPermission="3" domainObject="pmTaskInfo_tabPower_button_btDeleteMessage" >
			<a id="btDeleteMessage" class="easyui-linkbutton" iconCls="icon-remove"  plain="true" onclick="deleteData();" href="javascript:void(0);">删除</a>
		</sec:accesscontrollist>
		
		<sec:accesscontrollist   hasPermission="3" domainObject="pmTaskInfo_tabPower_button_btSearchMessage" >
			<a id="btSearchMessage" class="easyui-linkbutton" iconCls="icon-search"  plain="true" onclick="showSearchDialog();" href="javascript:void(0);">查询</a>
		</sec:accesscontrollist>
		
		
	</div>
	
		
	<div data-options="region:'center',split:true,title:''" style="padding:0px;">	
		<!-- <table id="sysLoglist"></table> -->
		<table id="dgMessage" class="easyui-datagrid"
			data-options=" 
				fit: true,
				border: false,
				rownumbers: false,
				animate: true,
				collapsible: false,
				fitColumns: true,
				autoRowHeight: false,
				toolbar:'#toolbar',
				idField :'id',
				singleSelect: false,
				checkOnSelect: false,
				selectOnCheck: true,
				
				pagination:true,
				pageSize:dataOptions.pageSize,
				pageList:dataOptions.pageList,
				
				striped:true,
				
				url: 'platform/sysmessage/sysMessageController/getSysMessageListByPage.json',
				queryParams: {filter_EQ_m_is_readed: '0', filter_EQ_m_recv_user: 'recv'},
				onClickCell: dgMessageOnClickCell,
				
				view: messageView
				
					
				">
			<thead>
				<tr>
					<th data-options="field:'id', halign:'center' ,checkbox:true" >id</th>
					
					<th data-options="field:'title',halign:'center',align:'left'" editor="{type:'text'}" width="220">消息标题</th>
					<th data-options="field:'content',halign:'center',align:'left'" editor="{type:'text'}" width="220">消息内容</th>
					<th data-options="field:'isReaded',required:true,halign:'center',align:'left', formatter: formatRead" editor="{type:'combobox',options:{required:true,panelHeight:'auto',editable:false,valueField:'lookupCode',textField:'lookupName'}}" width="220">是否已读</th>
					
					<th data-options="field:'urlAddress',halign:'center',align:'left'" editor="{type:'text'}" width="220">转向地址</th>
					<th data-options="field:'sendUserName',halign:'center',align:'left'" editor="{type:'text'}" width="220">发送人</th>
					<th data-options="field:'sendDeptName',halign:'center',align:'left'" editor="{type:'text'}" width="220">发送人部门</th>
					
					<th data-options="field:'sendDate',required:true,halign:'center',align:'left', formatter: formatDate" editor="{type:'text'}" width="220">发送时间</th>
					<th data-options="field:'recvDate',required:true,halign:'center',align:'left', formatter: formatDate" editor="{type:'text'}" width="220">接收时间</th>
					
				</tr>
			</thead>
		</table>	
		
	</div>
	
	<div id="messageSearchDialog" class="easyui-dialog" data-options="iconCls:'icon-search',resizable:true,modal:false,buttons:'#messagesearchBtns'" 
			style="width: 600px;height:150px; visible: hidden" title="查询条件">
		<form id="messageSearchForm">
    		<input id="filter_EQ_userLimitUserId" type='text' name="filter_EQ_userLimitUserId" style="display: none"/>
    		
    		<table>
    			
    			<tr>
    				
    				<td>消息标题:</td><td><input id="filter_LIKE_m_title" type='text' name="filter_LIKE_m_title" style="width: 150px"/></td>
    				<td>是否已读:</td><td><input id="filter_EQ_m_is_readed" class='easyui-combobox' data-options="panelHeight:'auto',editable:false, 
    					valueField:'lookupCode',textField:'lookupName'" name ="filter_EQ_m_is_readed" style="width: 150px"/></td>
    				
    			</tr>
    			
    		</table>
    	</form>
    	<div id="messagesearchBtns">
    		<a class="easyui-linkbutton" plain="false" onclick="searchData();" href="javascript:void(0);">查询</a>
    		<a class="easyui-linkbutton" plain="false" onclick="clearSearchData();" href="javascript:void(0);">清空</a>
    		<a class="easyui-linkbutton" plain="false" onclick="hideSearchDialog();" href="javascript:void(0);">返回</a>
    	</div>
     </div>
     
     <div id="messageContentDialog" class="easyui-dialog" data-options="iconCls:'icon-search',resizable:true,modal:true" 
			style="width: 600px;height:150px; visible: hidden" title="查看消息内容">
			
			<textarea id="messageContent" style="width: 98%; height: 93%; resize: none" disabled="true"></textarea>
			
	</div>
	
	<div id="sendMessageDialog" class="easyui-dialog" data-options="iconCls:'icon-search',resizable:true,modal:true,buttons:'#sendMessageBtns'" 
			style="width: 600px;height:300px; visible: hidden" title="发送消息">
		<form id="sendMessageForm">
    		<input id="recvUser"  name="recvUser" style="display: none"/>
    		
    		<table>
    			
    			<tr>
    				<td>消息标题:</td><td><input id="title"  name="title" style="width: 500px"/></td>
    			</tr>
    			
    			<tr>
    				<td>内容:</td><td><textarea  id="content" name="content"
    					 style="width: 500px; height: 100px; resize: none"></textarea></td>
    			</tr>
    			
    			<tr>
    				<td>转向地址:</td><td><input id="urlAddress"  name="urlAddress" style="width: 500px"/></td>
    			</tr>
    			
    			<tr>
    				<td>接收人:</td><td><input id="recvUserName"  name="recvUserName" style="width: 500px"/></td>
    			</tr>
    			
    			<tr style="display:none">
    				<td>是否已读:</td><td><input id="isReaded" class='easyui-combobox' data-options="panelHeight:'auto',editable:false, 
    					valueField:'lookupCode',textField:'lookupName'" name ="isReaded" style="width: 500px"/></td>
    			</tr>
    			
    		</table>
    	</form>
    	<div id="sendMessageBtns">
    		<a class="easyui-linkbutton" onclick="saveData();" href="javascript:void(0);">发送</a>
    		<a class="easyui-linkbutton" onclick="clearSendMessageData();" href="javascript:void(0);">清空</a>
    		<a class="easyui-linkbutton" onclick="hideSendMessageDialog();" href="javascript:void(0);">返回</a>
    	</div>
     </div>


</body>
</html>