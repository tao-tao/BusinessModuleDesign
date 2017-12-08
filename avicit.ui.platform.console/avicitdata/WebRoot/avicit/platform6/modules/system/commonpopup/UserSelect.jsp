<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<%@ taglib prefix="sec" uri="/WEB-INF/tags/shiro.tld" %>
<html>
<head>
<title>选择人员</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<base href="<%=ViewUtil.getRequestPath(request) %>">
<jsp:include page="/avicit/platform6/component/common/EasyUIJsInclude.jsp"></jsp:include>
<jsp:include page="/avicit/platform6/modules/system/commonpopup/commonSelectionHead.jsp"></jsp:include>
<script>
var orgDatajson = ${orgDatajson};
var historyData = ${historyData};
var singleSelect = ${singleSelect};
var multipleOrg = '${multipleOrg}' ;
var extParameter = '${extParameter}';
var displaySubDeptUser = '${displaySubDeptUser}';
var functionAction = true ;

$(function(){
	//ajaxRequest("POST","","platform/commonSelection/commonSelectionController/getOrg","json","loadOrgTree");
	$('#userQueryText').searchbox({
        width: 240,
        searcher: function (value) {
         //query();
        },
        prompt: "请输入查询内容"
    });
	loadOrgTree();
 //	window.setTimeout("expand()", 200);
 	var dataGridHeight = $(".easyui-layout").height();
	$("#orgTree_user").css("height",dataGridHeight-40);
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
	//查询框绑定keyup事件
	queryKeyUp();
 });
/**
 * 树查询回调函数（查询关键字为空调用）
 */
function loadUserTreeDefaultQueryResult(queryResult){
	 var queryJson =eval(queryResult);
	 $('#orgTree_user').tree("loadData",queryJson);
}
/**
 * 树查询回调函数
 */
function loadUserTreeQueryResult(queryResult){
	 var queryJson =eval(queryResult);
	 $('#orgTree_user').tree("loadData",queryJson);
	 $('#orgTree_user').tree('expandAll');
}
var loading = false;
/**
 * 加载组织树
 */
function loadOrgTree(){
	    var orgData =eval(orgDatajson);  // eval(result.orgDatajson);
		 $('#orgTree_user').tree({
	 		 checkbox : true,
	 		 lines : true,
	 		 method : 'post',
	 		data: orgData,
	 		onCheck :function(node, checked){
	 			if(loading){
	 				return;
	 			}
	 			var nodeType = node.attributes.nodeType ;
	 			if(nodeType=='user'){
	 				var parentNode = $('#orgTree_user').tree('getParent',node.target) ;
	 				var currNodeName = removeNodeNameParent(node.text) ;
	 				var userNo = node.attributes.userNo ;
		 			var selectionData = {"userId":node.id,"userName":currNodeName,"deptId":parentNode.id,"deptName":parentNode.text,"userNo":userNo};
	 				if(checked){
	 					var rowIndex_ = getDataGridRowIndex(node.id);
	 					if(rowIndex_==-1){
	 						$('#selectUserTargetDataGrid').datagrid('appendRow',selectionData);
	 					}
	 				//	$('#selectUserTargetDataGrid').datagrid('selectRecord',node.id);
	 					var rowIndex = getDataGridRowIndex(node.id);
			 		     $('#selectUserTargetDataGrid').datagrid('checkRow',rowIndex);
	 				}else{
	 					var rowIndex = getDataGridRowIndex(node.id);
	 					if(rowIndex!=-1){
	 						$('#selectUserTargetDataGrid').datagrid('deleteRow',rowIndex);
	 					}
	 				}
	 			}else if(nodeType=='dept' && functionAction == true){
		 					var param = "deptId="+node.id+"&checked="+checked ;
		 					if(displaySubDeptUser != null && typeof(displaySubDeptUser)!='undefined' && displaySubDeptUser !='null'){
		 		            	param +="&displaySubDeptUser="+displaySubDeptUser;
		 		            }
		 					ajaxRequest("POST",param,"platform/commonSelection/commonSelectionController/getAllSysUsersByDeptId","json","displayAllUsers");
	 			}else if(nodeType=='dept' && functionAction == false){
	 				var  childNodes = $('#orgTree_user').tree("getChildren",node.target);
	 				$.each(childNodes,function(i,currNode){
	 					var currNodeName_ = removeNodeNameParent(currNode.text) ;
	 					var currSelectionData = {"userId":currNode.id,"userName":currNodeName_,"deptId":node.id,"deptName":node.text};
	 					if(checked){
		 					var rowIndex_ = getDataGridRowIndex(currNode.id);
		 					if(rowIndex_==-1){
		 						$('#selectUserTargetDataGrid').datagrid('appendRow',currSelectionData);
		 					}
		 					var rowIndex = getDataGridRowIndex(currNode.id);
				 		     $('#selectUserTargetDataGrid').datagrid('checkRow',rowIndex);
		 				}else{
		 					var rowIndex = getDataGridRowIndex(currNode.id);
		 					if(rowIndex!=-1){
		 						$('#selectUserTargetDataGrid').datagrid('deleteRow',rowIndex);
		 					}
		 				}
	 				});
			    }else if(nodeType=='org' && (checked==false || checked==true)){
	 			//	if(nodeType=='org' && ){
	 					//$('#orgTree_user').tree('uncheck',node.target) ;
	 					node.checked = false ;
	 			//	}
	 			}
	 		},
	 		 onBeforeExpand:function(node,param){
	 			var beforeExpandUrl = 'platform/commonSelection/commonSelectionController/getDepts';
	 			var paramReq = '?nodeType=' + node.attributes.nodeType 
				+ '&deptId='+node.id
				//+ '&orgId='+node.id
				+ '&selectType=user';
	 			var para = node.attributes.para;
				if(para != null && typeof(para) != 'undefined'){
					paramReq +="&param=" + para ;
				}
	 			var checkedFlag =  node.attributes.checkedFlag ;
	 			if( checkedFlag != null && typeof(checkedFlag) != 'undefined' && checkedFlag ){
					paramReq += "&checkedFlag=true";
				}
				if( extParameter != null && typeof(extParameter) != 'undefined' ){
					paramReq += "&extParameter=" + extParameter ;
				}
				$('#orgTree_user').tree('options').url = beforeExpandUrl + paramReq;
		        
	 		 },onLoadSuccess:function(node,data){
	 			 
	 		 },onBeforeLoad:function(node, param){
	 		    loading=true;
			 },onLoadSuccess:function(node, data){
	 		    loading=false;
	 		 },onDblClick:function(node){
	 			 debugger;
	 			var nodeType = node.attributes.nodeType ;
	 			if(nodeType=='dept'){
	 				node.attributes.checkedFlag = true; 
	 				$('#orgTree_user').tree('expand',node.target);
	 			}
	 			if(nodeType=='user'){
		 			var parentNode = $('#orgTree_user').tree('getParent',node.target) ;
		 			var currNodeName = removeNodeNameParent(node.text) ;
		 		   	var selectionData = {"userId":node.id,"userName":currNodeName,"deptId":parentNode.id,"deptName":parentNode.text};
	 				var rowIndex_ = getDataGridRowIndex(node.id);
	 				if(rowIndex_==-1){
	 					$('#selectUserTargetDataGrid').datagrid('appendRow',selectionData);
	 				}
	 				$('#selectUserTargetDataGrid').datagrid('selectRecord',node.id);
	 				$('#orgTree_user').tree('check',node.target) ;
	 			}
	 		 }
	 	 });
}
/**
 * 初始化datagrid
 */
function loadSelectedTable(dataGridHeight){
	 $('#selectUserTargetDataGrid').datagrid({
			fitColumns: true,
			remoteSort: false,
			nowrap:false,
			idField:'userId',
		//	pagination:true,
			loadMsg:"数据加载中.....",
			singleSelect:singleSelect,
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
			columns:[[
				{field:'userId',title:'用户ID',width:120,align:'center' ,hidden : true},
				{field:'userNo',title:'用户编号',width:120,align:'center' ,hidden : true},
				{field:'userName',title:'用户名',width:150,align:'center'},
				{field:'deptId',title:'部门ID',width:120,align:'center',hidden : true},
				{field:'deptName',title:'部门',width:150,align:'center'}
			]],onLoadSuccess:function(data){
				if(data.rows.length>0){
					$('#selectUserTargetDataGrid').datagrid("checkAll");		
				}
			},onDblClickRow:function(rowIndex, rowData){
				$('#selectUserTargetDataGrid').datagrid('deleteRow',rowIndex);
				var nodes = $('#orgTree_user').tree('getChecked');
				if(null != nodes && typeof(nodes)!='undefined' && nodes.length>0){
					for(var j = 0; j< nodes.length; j++){
						if(nodes[j].id==rowData.userId){
							$('#orgTree_user').tree('uncheck',nodes[j].target); 
							break ;
						}
					}
				}
			}
		});
	 /**
	//设置分页控件   
		var p = $('#selectUserTargetDataGrid').datagrid('getPager');
		$(p).pagination({
		    pageSize: 10,//每页显示的记录条数，默认为10
		    pageList: [10,15,20,25,30],//可以设置每页记录条数的列表
		    beforePageText: '第',//页数文本框前显示的汉字
		    afterPageText: '页    共 {pages} 页',
		    displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
		});**/
}
/**
 * 打开页面时将已选择的数据带回datagrid显示
 */
function loadSelectGridDatas(datas){
	$('#selectUserTargetDataGrid').datagrid('loadData',datas);
}
//展开树
function expand(dataGridHeight) {
		var node = $('#orgTree_user').tree('getSelected');
		if(node){
			$('#orgTree_user').tree('expand',node.target);
		}else{
			$('#orgTree_user').tree('expandAll');
		}
}

/**
 * 移除全部选择的用户
 */
function removeAllSelectedUsersClick(){
	    removeGridAllSelectedData();
	    var nodes = $('#orgTree_user').tree('getChecked');
		for(var i = 0; i < nodes.length; i++){
			if(getRecordIndexInTargetDataGrid(nodes[i].id) == -1 && nodes[i].iconCls == "icon-user"){
				$('#orgTree_user').tree('uncheck',nodes[i].target); 		
			}
		}
}
/**
 * 移除dataGrid所有选择的数据
 */
function removeGridAllSelectedData(){
	var dataGridData = $('#selectUserTargetDataGrid').datagrid('getChecked');
	var checkDataSize=dataGridData.length;
	if(checkDataSize>0){
		$('#selectUserTargetDataGrid').datagrid('deleteRow',getRecordIndexInTargetDataGrid(dataGridData[0].userId));
		var currDataGridData=$('#selectUserTargetDataGrid').datagrid('getChecked');
		if(currDataGridData.length>=1){
			removeGridAllSelectedData();
		}
	}
}
/**
 * 移除全部用户
 */
function removeAllUserClick(){
	$('#selectUserTargetDataGrid').datagrid('clearChecked');
	$('#selectUserTargetDataGrid').datagrid('loadData',[]);
	var nodes = $('#orgTree_user').tree('getChecked');
	for(var j = 0; j< nodes.length; j++){
		$('#orgTree_user').tree('uncheck',nodes[j].target); 		
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
	return $('#selectUserTargetDataGrid').datagrid('getRowIndex',rowData);
}
/**
 * 当用户点击部门节点时动态将部门人员及子部门人员自动添加至datagrid
 */
function displayAllUsers(data){
	if(typeof(data)!='undefined'){
		var users = data.allUsers ;
		var checkBoxChecked=data.checked ;
		if(checkBoxChecked=='true'){
			$.each(users,function(x,user){
				var insertIndex = getDataGridRowIndex(user.userId) ;
				if(insertIndex==-1){
					$('#selectUserTargetDataGrid').datagrid('appendRow',user);
					$('#selectUserTargetDataGrid').datagrid('checkRow',getDataGridRowIndex(user.userId));
					//$('#selectUserTargetDataGrid').datagrid('selectRecord',user.userId);
				}
			});
		}else if(checkBoxChecked=='false'){
			$.each(users,function(y,user){
				var deleteIndex = getDataGridRowIndex(user.userId) ;
				if(deleteIndex !=-1){
					$('#selectUserTargetDataGrid').datagrid('deleteRow',deleteIndex);
				}
			});
		}
		
	}
}
/**
 * 取得datagrid中已选择全部数据
 */
function getSelectedResultDataJson(){
	return $('#selectUserTargetDataGrid').datagrid('getChecked');
}
/**
 * 取得datagrid中全部数据
 */
function getAllResultDataJson(){
	return $('#selectUserTargetDataGrid').datagrid('getData').rows;
}

/**
 * 查询事件
 */
 function query(value){
	  if(undefined == value || null == value){
		  value = '';
	  }
	  
	  if (value == "请输入查询内容") {
          value = "";
      }
      var param="selectType=user&userKeyWord="+value;
      if(multipleOrg != null && typeof(multipleOrg)!='undefined'){
      	param +="&multipleOrg="+multipleOrg;
      }
      
      if(value.length==0){
      	functionAction = true ;
      	ajaxRequest("POST",param,"platform/commonSelection/commonSelectionController/getOrgQueryResult","json","loadUserTreeDefaultQueryResult");
      }else{
      	functionAction = false ;
      	ajaxRequest("POST",param,"platform/commonSelection/commonSelectionController/getOrgQueryResult","json","loadUserTreeQueryResult");
      }
}

/**
 * 绑定查询框键盘事件
 */
 //查询框绑定keyup事件
 function queryKeyUp(){
	//定义查询框setTimeout执行方法
	 var TimeFnKeyUp = null;
	//查询框绑定事件
	$('#toolbar input').bind('keyup', function () {
		var value=this.value;
		//清除定时任务
		clearTimeout(TimeFnKeyUp);
		//执行延时处理
		TimeFnKeyUp= setTimeout(function(){
			//执行查询事件
			query(value);
		},300);
	});
 }
 
</script>

</head>
<body class="easyui-layout" fit="true">

<div region="west" style="width:250px;overflow: hidden;float:left;">
     <div id="toolbar" class="datagrid-toolbar" style="display: block;">
				<table class="tableForm" id="searchForm" width='100%'>
					<tr>
						<td width="220px;"><input id="userQueryText" disabled="disabled"></input>  </td>
					</tr>
				</table>
	     </div>
	 <ul id="orgTree_user"  style="width:auto;overflow: auto;"></ul>  
	
</div>

<div region="center" border="false"  id="ButtonDiv" style="overflow: hidden;float:left;">
<!-- <img alt="选择移除" src="<%=request.getContextPath()%>/avicit/platform6/component/css/commonselection/icons_ext/partRemove.gif"  style="cursor:pointer;margin-left:7px"  onclick="removeAllSelectedUsersClick();" >
		<br />	
		<br />	<br />	
 <img alt="全部移除" src="<%=request.getContextPath()%>/avicit/platform6/component/css/commonselection/icons_ext/allRemove.gif"  style="cursor:pointer;margin-left:7px"  onclick="removeAllUserClick();" >  
 	 -->
 	<table border="0"  align="center">
		<tr>
			<td >
				<input type="button" value="<<移除"   style="cursor:pointer;width:65px;"  onclick="removeAllSelectedUsersClick();"/><br/>
				<br/>
				<input type="button" value="<<清空"  style="cursor:pointer;width:65px;"  onclick="removeAllUserClick();"/><br/>
			</td>
		</tr>
	</table>
</div>
<div region="east" border="false" style="overflow: hidden;float:right;width:250px;">
<div id="selectUserTargetDataGrid"></div>
	
</div>
</body>
</html>
