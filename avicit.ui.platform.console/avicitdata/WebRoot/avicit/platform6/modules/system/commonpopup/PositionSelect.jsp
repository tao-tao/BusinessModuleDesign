<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<%@ taglib prefix="sec" uri="/WEB-INF/tags/shiro.tld" %>
<html>
<head>
<title>选择岗位</title>
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
	loadpositionList();
	$('#positionQueryText').searchbox({
        width: 200,
        searcher: function (value) {
            if (value == "请输入查询内容") {
                value = "";
            }
            try {
	            searchPositionFun(value);
	            //停止datagrid的编辑.
	            endEdit();
            } catch (e) {
            	
            }
            
        },
        prompt: "请输入查询内容"
    });
 });

function loadpositionList(){
	var dialogId = '<%=request.getParameter("dialogId")%>';
	var singleSelect = '<%=request.getParameter("singleSelect")%>';
	var isMultiple = singleSelect=='false'?false:true;
	var dataGridHeight = $(".easyui-layout").height()-32;
	$('#positionList').datagrid({
		url:'platform/commonSelection/commonSelectionController/getSysPosition?type=position',
		idField:'id',
		width: '100%',
	    nowrap: false,
	    striped: true,
	    autoRowHeight:false,
	   // singleSelect:isMultiple,
	    checkOnSelect:true,
	    remoteSort : false,
		height: dataGridHeight,
		fitColumns: true,
		//pagination:true,
		rownumbers:true,
		loadMsg:' 处理中，请稍候…',
		frozenColumns:[[
		                {field:'ck',checkbox:true}
		 ]],
		columns:[[
			{field:'positionName',title:'岗位名称',width:25,align:'left'},
			{field:'positionDesc',title:'岗位描述',width:80,align:'left',sortable:true}
		]],onLoadSuccess:function(data){
			if(data.rows.length>0){
				var historyPositionIds = '<%=request.getParameter("historyId")%>';
				setSelectedData(historyPositionIds,"positionList",",") ;
			}
		}
	});
	//设置分页控件   
	/**
	var p = $('#positionList').datagrid('getPager');
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
	return $('#positionList').datagrid('getChecked');
}
//查询
function searchPositionFun(queryKeyWord){
	if(queryKeyWord==null){
		$.messager.alert("操作提示", "请输入查询条件！","info");
		return ;
	}
	var queryParams = $('#positionList').datagrid('options').queryParams;  
    queryParams.positionKeyWord =queryKeyWord;
    $('#positionList').datagrid('options').queryParams=queryParams;        
    $("#positionList").datagrid('load'); 
}
//取消datagrid的全部勾选
function uncheckAllPosition(){
	 $("#positionList").datagrid('uncheckAll'); 
}
</script>
<body class="easyui-layout" fit="true">
<div region="center" border="false" style="overflow: hidden;">
         <div id="toolbar" class="datagrid-toolbar" style="display: block;">
				<table class="tableForm" id="searchForm" width='100%'>
					<tr>
						<td width="100px;"><input id="positionQueryText"></input>  </td>
					</tr>
				</table>
	     </div>
         <table id="positionList" style="overflow: auto;" ></table>
</div>

</body>
</html>