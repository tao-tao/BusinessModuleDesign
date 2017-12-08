/**
 * 根据idField的值取得该行数据在datagrid中的rowIndex
 */
function getDataGridRowIndex(dataGridId,idFieldValue){
	var dataGridObj = $('#'+dataGridId);
	return dataGridObj.datagrid('getRowIndex',idFieldValue);
}
var functionAction = true ;
/**
 * 初始化tab
 */
function initTabContainer(dataGridHeight) {
	$('#comprehensiveTabControl')
			.tabs(
					{
						onSelect : function(title, index) {
							if (index == 0) {
								tabSelectedIndex = "0";
								loadRoleList();
							}
							if (index == 1) {
								tabSelectedIndex = "1";
								$('#userQueryText')
										.searchbox(
												{
													width : 200,
													searcher : function(
															value) {
														if (value == "请输入查询内容") {
															value = "";
														}
														  if(value.length==0){
															  functionAction = true ;
														  }else{
															  functionAction = false ;
														  }
														  searchData() ;
//														  var param="multipleOrg=n&selectType=user&userKeyWord="+value;
//												           ajaxRequest("POST",param,"platform/centralizedAuthorizationController/getOrgQueryResult","json","loadUserTreeQueryResult");
													},
													prompt : "请输入查询内容"
												});
								//$("#dgUser").css("height",dataGridHeight);
								$("#dgUser").datagrid("options").url ="platform/sysdept/sysDeptController/getUserDataByPage.json?_status=1";
								$("#dgUser").datagrid('reload',{id:'',type:''});
								
								$("#dgUser").datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
								
//								$("#orgTree_user").css("height",dataGridHeight - 84);
//								ajaxRequest(
//										"POST",
//										"selectType=user_Comprehensive",
//										"platform/centralizedAuthorizationController/getOrgDepts",
//										"json", "loadOrgTree");
							}
							if (index == 2) {
								tabSelectedIndex = "2";
								$('#deptQueryText').searchbox({
									width : 200,
									searcher : function(value) {
										if (value == "请输入查询内容") {
											value = "";
										}
										var searchData = {
												search_text:value
											
										} ;
										 var param="multipleOrg=n&selectType=dept&deptKeyWord="+value;
								           ajaxRequest("POST",searchData,"platform/sysdept/sysDeptController/searchDept?_status=1","json","loadDeptTreeByData");
										//停止datagrid的编辑.
									},
									prompt : "请输入查询内容"
								});
								loadDeptTree() ;
								$("#orgTree_dept").css("height",dataGridHeight - 84);
//								ajaxRequest(
//										"POST",
//										"multipleOrg=n&selectType=dept_Comprehensive",
//										"platform/sysdept/sysDeptController/getChildOrgDeptById.json",
//										"json", "loadDeptTree");
							}
							if (index == 3) {
								tabSelectedIndex = "3";
								$('#groupQueryText').searchbox({
									width : 200,
									searcher : function(value) {
										if (value == "请输入查询内容") {
											value = "";
										}
										searchGroupFun(value);
										//停止datagrid的编辑.
									},
									prompt : "请输入查询内容"
								});
								loadGroupList();
							}
							if (index == 4) {
								tabSelectedIndex = "4";
								$('#positionQueryText').searchbox({
									width : 200,
									searcher : function(value) {
										if (value == "请输入查询内容") {
											value = "";
										}
										searchPositionFun(value);
										//停止datagrid的编辑.
									},
									prompt : "请输入查询内容"
								});
								loadPositionList();
							}
						}
					});
	
//$('#comprehensiveTabControl').tabs("select","角色");
	loadRoleList();
}
/**
 * 加载组织树
 */
function loadOrgTree(orgDatajson){
	    var orgData =eval(orgDatajson.orgDatajson);  
		 $('#orgTree_user').tree({
	 		 //checkbox : true,
	 		 lines : true,
	 		 method : 'post',
	 		data: orgData,
	 		onCheck :function(node, checked){
	 			
	 		},
	 		 onBeforeExpand:function(node,param){
	 			var beforeExpandUrl_ = 'platform/centralizedAuthorizationController/getDepts.json';
				var paraM = node.attributes.para;
				if(typeof(paraM) == 'undefined'){
					paraM = '';
				}
				$('#orgTree_user').tree('options').url = beforeExpandUrl_ + '?nodeType=' + node.attributes.nodeType +'&deptId='+node.id+'&selectType=user&param=' + paraM;
		        
	 		 },onLoadSuccess:function(node,data){
	 		 }
	 	 });
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
				var insertIndex = getDataGridRowIndex("selectTargetDataGrid",user.userId) ;
				if(insertIndex==-1){
				 	var selectionData = {"id":user.userId,"name":user.userName,type:"user",typeName:"用户"};
					$('#selectTargetDataGrid').datagrid('appendRow',selectionData);
					$('#selectTargetDataGrid').datagrid('checkRow',getDataGridRowIndex("selectTargetDataGrid",user.userId));
					//$('#selectUserTargetDataGrid').datagrid('selectRecord',user.userId);
				}
			});
		}else if(checkBoxChecked=='false'){
			$.each(users,function(y,user){
				var deleteIndex = getDataGridRowIndex("selectTargetDataGrid",user.userId) ;
				if(deleteIndex !=-1){
					$('#selectTargetDataGrid').datagrid('deleteRow',deleteIndex);
				}
			});
		}
	}
}
//展开树
function expand(root) {
	$('#orgTree_user').tree('expand',root.target);
}
/**
 * 加载组织树
 */
function loadDeptTree(){
//	    var orgData =   eval(orgDatajson.data);
	    var orgTree_dept =  $('#orgTree_dept') ;
//	    if(orgData.length==1){
	    	orgTree_dept.tree({
		 		 //checkbox : true,
		 		 lines : true,
		 		 method : 'post',
//		 		data: orgData,
		 		url:'platform/sysdept/sysDeptController/getChildOrgDeptById.json?_status=1',
		 		onCheck :function(node, checked){
		 			
		 		},onBeforeExpand:function(node,param){
		 			 if(node){
		 				var beforeExpandUrl = 'platform/sysdept/sysDeptController/getChildOrgDeptById.json';
						
						orgTree_dept.tree('options').url = beforeExpandUrl + '?_status=1&type='+node.attributes.type +'&id='+node.id;
						
		 			 }
		 		 },
		 		loadFilter: function(data){
		            if (data.data){	
		                return data.data;
		            } else {
		                return data;
		            }
	       		}
		 	 });
//	    }
}
/**
 * 加载组织树
 */
function loadDeptTreeByData(orgDatajson){
	    var orgData =   eval(orgDatajson.data);
	    var orgTree_dept =  $('#orgTree_dept') ;
	    if(orgData.length==1){
	    	orgTree_dept.tree({
		 		 //checkbox : true,
		 		 lines : true,
		 		data: orgData,
		 		url:null,
		 		onCheck :function(node, checked){
		 			
		 		},onBeforeExpand:function(node,param){
		 		 }
		 	 });
	    }
}

/**
 * 加载角色数据
 */
function loadRoleList(){
	$('#roleQueryText').searchbox({
		width : 200,
		searcher : function(value) {
			if (value == "请输入查询内容") {
				value = "";
			}
			searchRoleFun(value);
			//停止datagrid的编辑.
		},
		prompt : "请输入查询内容"
	});
//	var dataGridHeight = $(".easyui-layout").height()-73;
	$('#roleList').datagrid({
		fit: true,
	    url:'platform/centralizedAuthorizationController/getSysRole.json?type=role',
		idField:'id',
		//width: '100%',
		singleSelect: true,
	    nowrap: false,
	    striped: true,
	    autoRowHeight:false,
	    checkOnSelect:true,
	    remoteSort : false,
		//height: dataGridHeight,
		fitColumns: true,
		pagination:true,
		pageSize:dataOptions.pageSize,
		pageList:dataOptions.pageList,
		columns:[[
                    {field:'id',title:'角色ID',width:25,hidden:'true',fit:true},
					{field:'roleName',title:'角色名称',width:75,align:'left',fit:true},
					{field:'roleDesc',title:'角色描述',width:60,align:'left',sortable:true,fit:true}
				]],
				toolbar: '#RoleToolbar'
	});
}

/**
 * 加载群组数据
 */
function loadGroupList(){
	//var dataGridHeight = $(".easyui-layout").height()-73;
	$('#groupList').datagrid({
		fit: true,
		url:'platform/centralizedAuthorizationController/getSysGroup.json?type=group',
		idField:'id',
		//width: '100%',
		singleSelect: true,
	    nowrap: false,
	    striped: true,
	    autoRowHeight:false,
	    checkOnSelect:true,
	    pagination:true,
		pageSize:dataOptions.pageSize,
		pageList:dataOptions.pageList,
	    remoteSort : false,
		//height: dataGridHeight,
		fitColumns: true,
		columns:[[
		    {field:'id',title:'群组ID',width:25,hidden:'true',fit:true},
			{field:'groupName',title:'群组名称',width:25,align:'left',fit:true},
			{field:'groupDesc',title:'群组描述',width:80,align:'left',sortable:true,fit:true}
		]],onLoadSuccess:function(data){
			if(data.rows.length>0){
			}
		},
		toolbar: '#groupToolbar'
	});
}
function loadPositionList(){
	//var dataGridHeight = $(".easyui-layout").height()-73;
	$('#positionList').datagrid({
		fit: true,
		url:'platform/centralizedAuthorizationController/getSysPosition.json?type=position',
		idField:'id',
		//width: '100%',
	    nowrap: false,
	    striped: true,
	    singleSelect: true,
	    autoRowHeight:false,
	    checkOnSelect:true,
	    remoteSort : false,
	    pagination:true,
		pageSize:dataOptions.pageSize,
		pageList:dataOptions.pageList,
		//height: dataGridHeight,
		fitColumns: true,
		columns:[[
            {field:'id',title:'岗位ID',width:25,hidden:'true',fit:true},
			{field:'positionName',title:'岗位名称',width:25,align:'left',fit:true},
			{field:'positionDesc',title:'岗位描述',width:80,align:'left',sortable:true,fit:true}
		]],onLoadSuccess:function(data){
			if(data.rows.length>0){
				
			}
		},
		toolbar: '#positionToolbar'
	});
}
//查询
function searchRoleFun(queryKeyWord){
	if(queryKeyWord==null){
		$.messager.alert("操作提示", "请输入查询条件！","info");
		return ;
    }
	var queryParams = $('#roleList').datagrid('options').queryParams;  
    queryParams.roleQueryKey =queryKeyWord;
    $('#roleList').datagrid('options').queryParams=queryParams;        
    $("#roleList").datagrid('load'); 
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



/**
 * 用户查询回调函数
 * @param queryResult
 */
function loadUserTreeQueryResult(queryResult){
	 var queryJson =eval(queryResult);
	 $('#orgTree_user').tree("loadData",queryJson);
	 if(functionAction==false){
		 $('#orgTree_user').tree('expandAll'); 
	 }
	 
}
/**
 * 部门查询回调函数
 * @param queryResult
 */
function loadDeptTreeQueryResult(queryResult){
	 var queryJson =eval(queryResult);
	 $('#orgTree_dept').tree("loadData",queryJson);
	 $('#orgTree_dept').tree('expandAll');
}

/*当前类型start 角色(R)，用户(U)，部门(D)，群组(G)，岗位(P)*/
$(function(){
	$(".tabs").find("li").click(function(){
		window.TARGET_TYPE = "R";    // 根据刚加载的位置，默认为“角色”
		
		var targetName = $(this).text();
		switch(targetName){
			case "角色": window.TARGET_TYPE = "R"; window.loadAuthInfoData();break;
			case "用户": window.TARGET_TYPE = "U"; window.loadAuthInfoData();break;
			case "部门": window.TARGET_TYPE = "D"; window.loadAuthInfoData();break;
			case "群组": window.TARGET_TYPE = "G"; window.loadAuthInfoData();break;
			case "岗位": window.TARGET_TYPE = "P"; window.loadAuthInfoData();break;
			default : window.TARGET_TYPE = "R"; 
		}
	});	
});
/*当前类型end*/

/*当前ID 角色(R)，用户(U)，部门(D)，群组(G)，岗位(P)*/
$(function(){	        
	        function settargetId() {
				var selections;
				switch(window.TARGET_TYPE){
					case "R": selections = $('#roleList').datagrid('getSelections'); if(!selections || !selections[0]) break;
					          window.TARGET_ID = selections[0].id;
					          window.loadAuthInfoData(); 
					          break;
					
					case "U": selections = $('#dgUser').datagrid('getSelections'); if(!selections || !selections[0]) break;
					          window.TARGET_ID = selections[0].ID;
					          window.loadAuthInfoData(); 
					          break;
					
					case "D": selections = $('#orgTree_dept').tree('getSelected');  if(!selections) break;     
					          window.TARGET_ID = selections.id;
					          window.loadAuthInfoData();
					          break;
					
					case "G": selections = $('#groupList').datagrid('getSelections'); if(!selections || !selections[0]) break;   
			                  window.TARGET_ID = selections[0].id;
			                  window.loadAuthInfoData();
			                  break;
					
					case "P": selections = $('#positionList').datagrid('getSelections'); if(!selections || !selections[0]) break;
			          		  window.TARGET_ID = selections[0].id;
			          		  window.loadAuthInfoData();
			                  break;
					
					default : selections = null; 
				}
				
				$(".searchbox-text").val("");
	        };
	        
			$('#roleList').datagrid({
		        onClickRow : function(rowIndex, rowData) {
		        	settargetId();
		        }
	        });
			
			$('#dgUser').datagrid({
		        onClickRow : function(rowIndex, rowData) {
		        	settargetId();
		        }
	        });
			
			$('#orgTree_dept').tree({
				onSelect : function() {
					settargetId();
	            }
            });
			
			$('#groupList').datagrid({
		        onClickRow : function(rowIndex, rowData) {
		        	settargetId();
		        }
	        });
			
			$('#positionList').datagrid({
		        onClickRow : function(rowIndex, rowData) {
		        	settargetId();
		        }
	        });	
});
/*当前ID end*/

/**
 * 展开部门选择对话框
 */
function authSelectDept(){
	//var usd = new CommonDialog("authSelectDeptDialog","350","400","avicit/platform6/modules/system/sysaccesscontrol/content/authSelectDept.jsp","选择部门",null,null,false);		
	//usd.show();
	
	var deptSelector = new CommonSelector("dept","deptSelectCommonDialog","authSelectedDeptId",null,null,null,null,null,null,null,600,400);
	deptSelector.init(false,"selectDeptDialogCallBack",'n');
}
//部门选择回调
function selectDeptDialogCallBack(data){
	$("#authSelectedDeptId").val(data[0].deptId);
	searchDeptData() ;
}
/**
 * 确定后保存选择的部门ID到页面指定位置
 * @param deptId
 */
function saveSelectedDeptId(deptId){
	$("#authSelectedDeptId").val(deptId);
	searchDeptData() ;
	$("#authSelectDeptDialog").dialog('close');
}

/**
 * 用户tab输入查询内容后查询数据
 * @returns
 */
function searchData(){
	$("#dgUser").datagrid("options").url="platform/sysdept/sysDeptController/getUserDataByPage.json?_status=1";
	//var depeId = $("#authSelectedDeptId").val();
//	var type = "" ;
//	if(depeId!= null && depeId.length>0){
//		type="dept";
//	}
	var queryKeyWord = $('#userQueryText').searchbox("getValue");
	var searchData = {
			search_LOGIN_NAME:queryKeyWord,
			search_USER_NAME:queryKeyWord
	} ;
//	searchData.id=depeId;
//	searchData.type=type;
	$('#dgUser').datagrid('load',searchData);
	$("#dgUser").datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
}

function searchDeptData(){
	$("#dgUser").datagrid("options").url="platform/sysdept/sysDeptController/getUserDataByPage.json?_status=1";
	var depeId = $("#authSelectedDeptId").val();
	var type = "" ;
	if(depeId!= null && depeId.length>0){
		type="dept";
	}
	var queryKeyWord = $('#userQueryText').searchbox("getValue");
	var searchData = {
			search_LOGIN_NAME:queryKeyWord
	} ;
	searchData.id=depeId;
	searchData.type=type;
	$('#dgUser').datagrid('load',searchData);
	$("#dgUser").datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
}