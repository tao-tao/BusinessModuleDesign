<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<%@ taglib prefix="sec" uri="/WEB-INF/tags/shiro.tld" %>
<html>
<head>
<title>选择部门</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<base href="<%=ViewUtil.getRequestPath(request) %>">
<jsp:include page="/avicit/platform6/component/common/EasyUIJsInclude.jsp"></jsp:include>
<jsp:include page="/avicit/platform6/modules/system/commonpopup/commonSelectionHead.jsp"></jsp:include>
<script>
var baseurl = '<%=request.getContextPath()%>';
var orgDatajson = ${orgDatajson};
var historyData = ${historyData};
var singleSelect = '${singleSelect}';
var multipleOrg = '${multipleOrg}' ;	
$(function(){
	$('#deptQueryText').searchbox({
        width: 240,
        searcher: function (value) {
            if (value == "请输入查询的部门信息") {
                value = "";
            }
            var param="selectType=dept&deptKeyWord="+value;
            if(multipleOrg != null && typeof(multipleOrg)!='undefined'){
            	param +="&multipleOrg="+multipleOrg;
            }
           if(value.length==0){
           	  ajaxRequest("POST",param,"platform/commonSelection/commonSelectionController/getOrgQueryResult","json","loadDeptTreeDefaultQueryResult");
           }else{
        	   ajaxRequest("POST",param,"platform/commonSelection/commonSelectionController/getOrgQueryResult","json","loadDeptTreeQueryResult");
           }
        },
        prompt: "请输入查询的部门信息"
    });
	loadDeptTree() ;
	//window.setTimeout("expand()", 200);
	var dataGridHeight = $(".easyui-layout").height();
	$("#orgTree_dept").css("height",dataGridHeight-40);
	loadSelectedTable(dataGridHeight);
	$("#ButtonDiv").css("height",dataGridHeight-5);
 	$("#ButtonDiv").css("padding-top",dataGridHeight/2-30);
 	if(typeof(historyData)=='undefined'){
 		historyData ={
 				total : 0,
 				rows : new Array()
 	     };
 	}
    loadSelectGridDatas(historyData);
 });
 
/**
 * 树查询回调函数（查询关键字为空时调用）
 */
function loadDeptTreeDefaultQueryResult(queryResult){
	 var queryJson =eval(queryResult);
	 $('#orgTree_dept').tree("loadData",queryJson);
}
/**
 * 树查询回调函数
 */
 function loadDeptTreeQueryResult(queryResult){
	 var queryJson =eval(queryResult);
	 $('#orgTree_dept').tree("loadData",queryJson);
	 $('#orgTree_dept').tree('expandAll');
 }
/**
 * 加载组织树
 */
function loadDeptTree(){
	    var orgData =   eval(orgDatajson);
		 $('#orgTree_dept').tree({
	 		 checkbox : true,
	 		 lines : true,
	 		 method : 'post',
	 		data: orgData,
	 		onCheck :function(node, checked){
	 			var nodeType = node.attributes.nodeType ;
	 			if(nodeType=='dept'){
	 				var currNodeName = removeNodeNameParent(node.text) ;
		 			var selectionData = {"deptId":node.id,"deptName":currNodeName};
	 				if(checked){
	 					var rowIndex_ = getDataGridRowIndex(node.id);
	 					if(rowIndex_==-1){
	 						$('#selectDeptTargetDataGrid').datagrid('appendRow',selectionData);
	 					}
	 				//	$('#selectDeptTargetDataGrid').datagrid('selectRecord',node.id);
	 					var rowIndex = getDataGridRowIndex(node.id);
			 		     $('#selectDeptTargetDataGrid').datagrid('checkRow',rowIndex);
	 				}else{
	 					var rowIndex = getDataGridRowIndex(node.id);
	 					if(rowIndex!=-1){
	 						$('#selectDeptTargetDataGrid').datagrid('deleteRow',rowIndex);
	 					}
	 				}
	 			}else if(nodeType=='org'){
		 					var param = "checked="+checked ;
		 					//ajaxRequest("POST",param,"platform/commonSelection/commonSelectionController/getAllSysDeptByDeptId","json","displayAllDepts");
	 			}else{
	 				if(nodeType=='org'){
	 					//$('#orgTree_dept').tree('uncheck',node.target) ;
	 					return false ;
	 				}
	 			}
	 		},
	 		 onBeforeExpand:function(node,param){
	 			var beforeExpandUrl = 'platform/commonSelection/commonSelectionController/getDepts';
				var para = node.attributes.para;
				if(typeof(para) == 'undefined'){
					para = '';
				}
				$('#orgTree_dept').tree('options').url = beforeExpandUrl + '?nodeType='+node.attributes.nodeType +'&deptId='+node.id+'&selectType=dept&param=' + para;
		        
	 		 },onLoadSuccess:function(node,data){
	 			
	 		 },onDblClick:function(node){
	 			var nodeType = node.attributes.nodeType ;
	 			if(nodeType=='dept'){
	 				var currNodeName = removeNodeNameParent(node.text) ;
		 			var selectionData = {"deptId":node.id,"deptName":currNodeName};
		 			var rowIndex_ = getDataGridRowIndex(node.id);
 					if(rowIndex_==-1){
 						$('#selectDeptTargetDataGrid').datagrid('appendRow',selectionData);
 					}
 					$('#selectDeptTargetDataGrid').datagrid('selectRecord',node.id);
 					$('#orgTree_dept').tree('check',node.target) ;
	 			}
	 		 }
	 	 });
}
/**
 * 初始化datagrid
 */
function loadSelectedTable(dataGridHeight){
	 $('#selectDeptTargetDataGrid').datagrid({
			fitColumns: true,
			remoteSort: false,
			nowrap:false,
			idField:'deptId',
			loadMsg:"数据加载中.....",
		//	singleSelect:singleSelect,
			rownumbers:true,
			height : dataGridHeight,
			autoRowHeight: false,
			striped: true,
			collapsible:true,
			frozenColumns:[[
             {field:'ck',checkbox:true}
			]],
			checkOnSelect : true,
			selectOnCheck : true,
			onCheck : function(rowIndex,rowData){
			},
			columns:[[
				{field:'deptId',title:'部门ID',width:120,align:'center',hidden : true},
				{field:'deptName',title:'部门名称',width:150,align:'center'}
			]],onLoadSuccess:function(data){
					if(data.rows.length>0){
						$('#selectDeptTargetDataGrid').datagrid("checkAll");				
					}
			},onDblClickRow:function(rowIndex, rowData){
				$('#selectDeptTargetDataGrid').datagrid('deleteRow',rowIndex);
				var nodes = $('#orgTree_dept').tree('getChecked');
				if(null != nodes && typeof(nodes)!='undefined' && nodes.length>0){
				for(var j = 0; j< nodes.length; j++){
					if(nodes[j].id==rowData.deptId){
						$('#orgTree_dept').tree('uncheck',nodes[j].target); 
						break ;
					}
				}
			  }
			}
		});
}
/**
 * 打开页面时将已选择的数据带回datagrid显示
 */
function loadSelectGridDatas(datas){
	$('#selectDeptTargetDataGrid').datagrid('loadData',datas);
}
//展开树
function expand() {
		var node = $('#orgTree_dept').tree('getSelected');
		if(node){
			$('#orgTree_dept').tree('expand',node.target);
		}else{
			$('#orgTree_dept').tree('expandAll');
		}
}

/**
 * 移除所有选中的部门（包含datagrid和tree）
 */
function removeAllSelectedDeptClick(){
	removeGridAllSelectedData();
	var nodes = $('#orgTree_dept').tree('getChecked');
	for(var i = 0; i < nodes.length; i++){
		if(getRecordIndexInTargetDataGrid(nodes[i].id) == -1){
			$('#orgTree_dept').tree('uncheck',nodes[i].target); 		
		}
	}
}

/**
 * 移除全部部门
 */
function removeAllDeptClick(){
	$('#selectDeptTargetDataGrid').datagrid('clearChecked');
	$('#selectDeptTargetDataGrid').datagrid('loadData',[]);
	var nodes = $('#orgTree_dept').tree('getChecked');
	for(var j = 0; j< nodes.length; j++){
		$('#orgTree_dept').tree('uncheck',nodes[j].target); 		
	}
}
/**
 * 移除dataGrid所有选择的数据
 */
function removeGridAllSelectedData(){
	var dataGridData = $('#selectDeptTargetDataGrid').datagrid('getChecked');
	var checkDataSize=dataGridData.length;
	if(checkDataSize>0){
		$('#selectDeptTargetDataGrid').datagrid('deleteRow',getRecordIndexInTargetDataGrid(dataGridData[0].deptId));
		var currDataGridData=$('#selectDeptTargetDataGrid').datagrid('getChecked');
		if(currDataGridData.length>=1){
			removeGridAllSelectedData();
		}
	}
}
/**
根据idField的值取得该行数据在datagrid中的rowIndex
**/
function getRecordIndexInTargetDataGrid(userId){
	return getDataGridRowIndex(userId);
}
/**
 * 根据idField的值取得该行数据在datagrid中的rowIndex
 */
function getDataGridRowIndex(rowData){
	return $('#selectDeptTargetDataGrid').datagrid('getRowIndex',rowData);
}
/**
 * 当用户点击部门节点时动态将部门及子部门自动添加至datagrid
 */
function displayAllDepts(data){
	if(typeof(data)!='undefined'){
		var users = data.allUsers ;
		var checkBoxChecked=data.checked ;
		if(checkBoxChecked=='true'){
			$.each(users,function(x,user){
				var insertIndex = getDataGridRowIndex(user.userId) ;
				if(insertIndex==-1){
					$('#selectDeptTargetDataGrid').datagrid('appendRow',user);
					$('#selectDeptTargetDataGrid').datagrid('checkRow',getDataGridRowIndex(user.userId));
					//$('#selectDeptTargetDataGrid').datagrid('selectRecord',user.userId);
				}
			});
		}else if(checkBoxChecked=='false'){
			$.each(users,function(y,user){
				var deleteIndex = getDataGridRowIndex(user.userId) ;
				if(deleteIndex !=-1){
					$('#selectDeptTargetDataGrid').datagrid('deleteRow',deleteIndex);
				}
			});
		}
		
	}
}
/**
 * 取得datagrid中已选择全部数据
 */
function getSelectedResultDataJson(){
	return $('#selectDeptTargetDataGrid').datagrid('getChecked');
}
/**
 * 取得datagrid中全部数据
 */
function getAllResultDataJson(){
	return $('#selectDeptTargetDataGrid').datagrid('getData').rows;
}
</script>
</style>
</head>
<body class="easyui-layout" fit="true">
<div region="west" style="width:250px;overflow: hidden;float:left;">
     <div id="toolbar" class="datagrid-toolbar" style="display: block;">
				<table class="tableForm" id="searchForm" width='100%'>
					<tr>
						<td width="220px;"><input id="deptQueryText" disabled="disabled"></input>  </td>
					</tr>
				</table>
	     </div>
	 <ul id="orgTree_dept"  style="width:auto;overflow: auto;"></ul>  
</div>
<div region="center" border="false"  id="ButtonDiv" style="width:150px;overflow: hidden;float:left;">
<!--  <img alt="选择移除" src="<%=request.getContextPath()%>/avicit/platform6/component/css/commonselection/icons_ext/partRemove.gif"  style="cursor:pointer;margin-left:7px"  onclick="removeAllSelectedDeptClick();" >
		<br />	
		<br />	
		<br />	
 <img alt="全部移除" src="<%=request.getContextPath()%>/avicit/platform6/component/css/commonselection/icons_ext/allRemove.gif"  style="cursor:pointer;margin-left:7px"  onclick="removeAllDeptClick();" >  -->
	<table border="0"  align="center">
		<tr>
			<td >
				<input type="button" value="<<移除"   style="cursor:pointer;width:65px;"  onclick="removeAllSelectedDeptClick();"/><br/>
				<br/>
				<input type="button" value="<<清空"  style="cursor:pointer;width:65px;"  onclick="removeAllDeptClick();"/><br/>
			</td>
		</tr>
	</table>
</div>
<div region="east" border="false" style="overflow: hidden;float:right;width:250px;">
		<div id="selectDeptTargetDataGrid"></div>
</div>
</body>
</html>
