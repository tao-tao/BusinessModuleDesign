<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<%@ taglib prefix="sec" uri="/WEB-INF/tags/shiro.tld" %>
<html>
<head>
<title>选择群组</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<base href="<%=ViewUtil.getRequestPath(request) %>">
<jsp:include page="/avicit/platform6/component/common/EasyUIJsInclude.jsp"></jsp:include>
<jsp:include page="/avicit/platform6/modules/system/commonpopup/commonSelectionHead.jsp"></jsp:include>
</head>

<script type="text/javascript">
/**
 * 初始化当前页面
 */
 
$(function(){
	loadgroupList();
	$('#groupQueryText').searchbox({
        width: 200,
        searcher: function (value) {
            if (value == "请输入查询内容") {
                value = "";
            }
            searchGroupFun(value);
            //停止datagrid的编辑.
            endEdit();
            
        },
        prompt: "请输入查询内容"
    });
 });

function loadgroupList(){
	var dialogId = '<%=request.getParameter("dialogId")%>';
	var singleSelect = '<%=request.getParameter("singleSelect")%>';
	var isMultiple = singleSelect=='false'?false:true;
	var dataGridHeight = $(".easyui-layout").height()-32;
	$('#groupList').datagrid({
		url:'platform/commonSelection/commonSelectionController/getSysGroup?type=group',
		idField:'id',
		width: '100%',
	    nowrap: false,
	    striped: true,
	    autoRowHeight:false,
	//   singleSelect:isMultiple,
	    checkOnSelect:true,
	    remoteSort : false,
		height: dataGridHeight,
		fitColumns: true,
	//	pagination:true,
		rownumbers:true,
		loadMsg:' 处理中，请稍候…',
		frozenColumns:[[
		                {field:'ck',checkbox:true}
		 ]],
		columns:[[
			{field:'groupName',title:'群组名称',width:25,align:'left'},
			{field:'groupDesc',title:'群组描述',width:80,align:'left',sortable:true}
		]],onLoadSuccess:function(data){
			if(data.rows.length>0){
				var historyGroupIds = '<%=request.getParameter("historyId")%>';
				setSelectedData(historyGroupIds,"groupList",",") ;
			}
		}
	});
	//设置分页控件  
	/**
	var p = $('#groupList').datagrid('getPager');
	$(p).pagination({
	    pageSize: 10,//每页显示的记录条数，默认为10
	    pageList: [5,10,15,20,25,30],//可以设置每页记录条数的列表
	    beforePageText: '第',//页数文本框前显示的汉字
	    afterPageText: '页    共 {pages} 页',
	    displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
	});**/
}
/**
 * 取得datagrid中已选择全部数据
 */
function getSelectedResultDataJson(){
	return $('#groupList').datagrid('getChecked');
}
//查询
function searchGroupFun(queryKeyWord){
	if(queryKeyWord==null){
		$.messager.alert("操作提示", "请输入查询条件！","info");
		return ;
	}
	var queryParams = $('#groupList').datagrid('options').queryParams;  
    queryParams.queryKeyWord =queryKeyWord;
    $('#groupList').datagrid('options').queryParams=queryParams;        
    $("#groupList").datagrid('load'); 
}
//取消datagrid的全部勾选
function uncheckAllCheckedGroup(){
	 $("#groupList").datagrid('uncheckAll'); 
}
</script>
<body class="easyui-layout" fit="true">
<div region="center" border="false" style="overflow: hidden;">
           <div id="toolbar" class="datagrid-toolbar" style="display: block;">
				<table class="tableForm" id="searchForm" width='100%'>
					<tr>
						<td width="100px;"><input id="groupQueryText"></input>  </td>
					</tr>
				</table>
	     </div>
         <table id="groupList" style="overflow: auto;"></table>
</div>

</body>
</html>