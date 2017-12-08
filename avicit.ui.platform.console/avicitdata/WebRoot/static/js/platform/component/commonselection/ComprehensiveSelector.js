/**
 * 根据idField的值取得该行数据在datagrid中的rowIndex
 */
function getDataGridRowIndex(dataGridId,idFieldValue){
	var dataGridObj = $('#'+dataGridId);
	return dataGridObj.datagrid('getRowIndex',idFieldValue);
};
var functionAction = true;
var tabSelectedIndex = "";
/**
 * 初始化tab
 */
function initTabContainer(dataGridHeight,mappingConfig) {
	
	$('#comprehensiveTabControl')
			.tabs(
					{
						onSelect : function(title, index) {
							if (title == '用户') {
								tabSelectedIndex = "0";
								document.getElementById("buttonAddSelectedData").style.display="none";
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
														  var param="selectType=user&userKeyWord="+value;
														  if(multipleOrg_req != null && typeof(multipleOrg_req)!='undefined'){
						                            			param += "&multipleOrg="+multipleOrg_req ;
								                          }
														  //add zl
														  alert(123);
														  ajaxRequest("POST",param,"platform/commonSelection/commonSelectionController/getOrgQueryResult","json","loadUserTreeQueryResult");
													},
													prompt : "请输入查询内容"
												});
								$("#orgTree_user").css("height",dataGridHeight - 84);
										
								ajaxRequest(
										"POST",
										"selectType=user_Comprehensive&multipleOrg="+multipleOrg_req,
										"platform/commonSelection/commonSelectionController/getOrgDepts",
										"json", "loadOrgTree");
								     //window.setTimeout("expand()", 200);
								    // loadUserTargetDataGrid();
								    // loadDeptTargetDataGrid();
							}
							if (title == '部门') {
								tabSelectedIndex = "1";
								document.getElementById("buttonAddSelectedData").style.display="none";
								$('#deptQueryText').searchbox({
									width : 200,
									searcher : function(value) {
										if (value == "请输入查询内容") {
											value = "";
										}
										 var param="selectType=dept&deptKeyWord="+value;
										 if(multipleOrg_req != null && typeof(multipleOrg_req)!='undefined'){
		                            			param += "&multipleOrg="+multipleOrg_req ;
				                          }
								           ajaxRequest("POST",param,"platform/commonSelection/commonSelectionController/getOrgQueryResult","json","loadDeptTreeQueryResult");
										//停止datagrid的编辑.
									},
									prompt : "请输入查询内容"
								});
								//loadDeptTree() ;
								$("#orgTree_dept").css("height",dataGridHeight - 84);
								ajaxRequest(
										"POST",
										"selectType=dept_Comprehensive&multipleOrg="+multipleOrg_req ,
										"platform/commonSelection/commonSelectionController/getOrgDepts",
										"json", "loadDeptTree");
							}
							if (title == '角色') {
								tabSelectedIndex = "2";
								document.getElementById("buttonAddSelectedData").style.display="block";
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
								loadRoleList(parseComprehensiveMappingConfig("role",mappingConfig));
							}
							if (title == '群组') {
								tabSelectedIndex = "3";
								document.getElementById("buttonAddSelectedData").style.display="block";
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
								loadGroupList(parseComprehensiveMappingConfig("group",mappingConfig));
							}
							if (title == '岗位') {
								tabSelectedIndex = "4";
								document.getElementById("buttonAddSelectedData").style.display="block";
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
								loadPositionList(parseComprehensiveMappingConfig("position",mappingConfig));
							}
						}
					});
	$('#comprehensiveTabControl').tabs("select",0);
//	$('#comprehensiveTabControl').tabs("select","用户");
};
/**
 * 加载组织树
 */
function loadOrgTree(orgDatajson){
	    multipleOrg_req = orgDatajson.multipleOrg;
	    var orgData =eval(orgDatajson.orgDatajson);  
		 $('#orgTree_user').tree({
	 		 checkbox : true,
	 		 lines : true,
	 		 method : 'post',
	 		data: orgData,
	 		onCheck :function(node, checked){
	 			var nodeType = node.attributes.nodeType ;
	 			if(nodeType=='user'){
	 				var currNodeName = removeNodeNameParent(node.text) ;
		 			var selectionData = {"id":node.id,"name":currNodeName,type:"user",typeName:"用户"};
	 				if(checked==true){
	 					var rowIndex_ = getDataGridRowIndex("selectTargetDataGrid",node.id);
	 					if(rowIndex_==-1){
	 						$('#selectTargetDataGrid').datagrid('appendRow',selectionData);
	 					}
	 				//	$('#selectTargetDataGrid').datagrid('selectRecord',node.id);
	 					var rowIndex = getDataGridRowIndex("selectTargetDataGrid",node.id);
			 		     $('#selectTargetDataGrid').datagrid('checkRow',rowIndex);
	 				}else if(checked==false){
	 					var rowIndexD = getDataGridRowIndex("selectTargetDataGrid",node.id);
	 					if(rowIndexD!=-1){
	 						$('#selectTargetDataGrid').datagrid('deleteRow',rowIndexD);
	 					}
	 				}
	 			}else if(nodeType=='dept' && functionAction == true){
	 				var param = "deptId="+node.id+"&checked="+checked ;
	 				var displaySubDeptUser = '<%=displaySubDeptUser%>';
	 				if(displaySubDeptUser != null && typeof(displaySubDeptUser)!='undefined' && displaySubDeptUser !='null'){
 		            	param +="&displaySubDeptUser="+displaySubDeptUser;
 		            }
 					ajaxRequest("POST",param,"platform/commonSelection/commonSelectionController/getAllSysUsersByDeptId","json","displayAllUsers");
	 			}else if(nodeType=='dept' && functionAction == false){
	 				var  childNodes = $('#orgTree_user').tree("getChildren",node.target);
	 				jQuery.each(childNodes,function(i,currNode){
	 					var currNodeName_ = removeNodeNameParent(currNode.text) ;
			 			var currSelectionData = {"id":currNode.id,"name":currNodeName_,type:"user",typeName:"用户"};
	 					if(checked){
	 						var rowIndexQ = $('#selectTargetDataGrid').datagrid('getRowIndex',currNode.id);
		 					if(rowIndexQ==-1){
		 						$('#selectTargetDataGrid').datagrid('appendRow',currSelectionData);
		 					}
		 					var rowIndexQ_ = $('#selectTargetDataGrid').datagrid('getRowIndex',currNode.id);
				 		     $('#selectTargetDataGrid').datagrid('checkRow',rowIndexQ_);
		 				}else{
		 					var rowIndexQD = $('#selectTargetDataGrid').datagrid('getRowIndex',currNode.id);
		 					if(rowIndexQD!=-1){
		 						$('#selectTargetDataGrid').datagrid('deleteRow',rowIndexQD);
		 					}
		 				}
	 				});
			    }else{
	 				if(nodeType=='org' && checked){
	 					$('#orgTree_user').tree('uncheck',node.target) ;
	 				}
	 			}
	 		},
	 		 onBeforeExpand:function(node,param){
	 			var beforeExpandUrl_ = 'platform/commonSelection/commonSelectionController/getDepts';
				var paraM = node.attributes.para;
				if(typeof(paraM) == 'undefined'){
					paraM = '';
				}
				$('#orgTree_user').tree('options').url = beforeExpandUrl_ + '?nodeType=' + node.attributes.nodeType +'&deptId='+node.id+'&selectType=user&param=' + paraM;
		        
	 		 },onLoadSuccess:function(node,data){
	 		 },onDblClick:function(node){
		 			var nodeType = node.attributes.nodeType ;
		 			var userDataGrid = $('#selectTargetDataGrid') ;
		 			if(nodeType=='user'){
		 				var currNodeName = removeNodeNameParent(node.text) ;
			 		   	var selectionData = {"id":node.id,"name":currNodeName,type:"user",typeName:"用户"};
		 				var rowIndex_ = getDataGridRowIndex("selectTargetDataGrid",node.id);
		 				if(rowIndex_==-1){
		 					userDataGrid.datagrid('appendRow',selectionData);
		 				}
		 				userDataGrid.datagrid('selectRecord',node.id);
		 				$('#orgTree_user').tree('check',node.target) ;
		 			}
		 		 }
	 	 });
};
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
					//$('#selectTargetDataGrid').datagrid('selectRecord',user.userId);
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
};
//展开树
function expand(root) {
	$('#orgTree_user').tree('expand',root.target);
};
/**
 * 加载组织树
 */
function loadDeptTree(orgDatajson){
	    multipleOrg_req = orgDatajson.multipleOrg;
	    var orgData =   eval(orgDatajson.orgDatajson);
		 $('#orgTree_dept').tree({
	 		 checkbox : true,
	 		 lines : true,
	 		 method : 'post',
	 		data: orgData,
	 		onCheck :function(node, checked){
	 			var nodeType = node.attributes.nodeType ;
	 			var dataGridObj = $('#selectTargetDataGrid') ;
	 			if(nodeType=='dept'){
	 				var currNodeName = removeNodeNameParent(node.text) ;
		 			var selectionData = {"id":node.id,"name":currNodeName,type:"dept",typeName:"部门"};
	 				if(checked){
	 					var rowIndex_ = getDataGridRowIndex("selectTargetDataGrid",node.id);
	 					if(rowIndex_==-1){
	 						dataGridObj.datagrid('appendRow',selectionData);
	 					}
	 				//	$('#selectTargetDataGrid').datagrid('selectRecord',node.id);
	 					var rowIndex = getDataGridRowIndex("selectTargetDataGrid",node.id);
	 					dataGridObj.datagrid('checkRow',rowIndex);
	 				}else{
	 					var rowIndex = getDataGridRowIndex("selectTargetDataGrid",node.id);
	 					if(rowIndex!=-1){
	 						dataGridObj.datagrid('deleteRow',rowIndex);
	 					}
	 				}
	 			}else{
	 				/**
	 				if(nodeType=='org' && checked){
	 					$('#orgTree_dept').tree('uncheck',node.target) ;
	 				}**/
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
	 			var deptGridObj = $('#selectTargetDataGrid') ;
	 			if(nodeType=='dept'){
	 				var currNodeName = removeNodeNameParent(node.text) ;
		 			var selectionData = {"id":node.id,"name":currNodeName,type:"dept",typeName:"部门"};
		 			var rowIndex_ = getDataGridRowIndex("selectTargetDataGrid",node.id);
 					if(rowIndex_==-1){
 						deptGridObj.datagrid('appendRow',selectionData);
 					}
 					deptGridObj.datagrid('selectRecord',node.id);
 					$('#orgTree_dept').tree('check',node.target) ;
	 			}
	 		 }
	 	 });
};
/**
 * 加载角色数据
 */
function loadRoleList(historyIdArr){
	var dataGridHeight = $(".easyui-layout").height()-73;
	$('#roleList').datagrid({
	    url:'platform/commonSelection/commonSelectionController/getSysRole?type=role',
		idField:'id',
		width: '100%',
	    nowrap: false,
	    striped: true,
	    autoRowHeight:false,
	    checkOnSelect:true,
	    remoteSort : false,
		height: dataGridHeight,
		fitColumns: true,
		frozenColumns:[[
		                {field:'ck',checkbox:true}
		 ]],
		columns:[[
                     {field:'id',title:'角色ID',width:25,hidden:'true'},
					{field:'roleName',title:'角色名称',width:25,align:'left'},
					{field:'desc',title:'角色描述',width:80,align:'left',sortable:true}
				]],
		onDblClickRow:function(rowIndex, rowData){
			            var rowIndexV=getDataGridRowIndex("selectTargetDataGrid",rowData.id);
			            if(rowIndexV == -1){
			            	    var selectionData = {"id":rowData.id,"name":rowData.roleName,type:"role",typeName:"角色"};
				 				$('#selectTargetDataGrid').datagrid('appendRow',selectionData);
				 				$('#selectTargetDataGrid').datagrid('selectRow',getDataGridRowIndex("selectTargetDataGrid",rowData.id));
			            }
		 },onLoadSuccess:function(data){
			 if(data.rows.length>0){
				 checkSelectedData(historyIdArr,"roleList");
			}
		 }
	});
};

/**
 * 加载群组数据
 */
function loadGroupList(historyIdArr){
	var dataGridHeight = $(".easyui-layout").height()-73;
	$('#groupList').datagrid({
		url:'platform/commonSelection/commonSelectionController/getSysGroup?type=group',
		idField:'id',
		width: '100%',
	    nowrap: false,
	    striped: true,
	    autoRowHeight:false,
	    checkOnSelect:true,
	    remoteSort : false,
		height: dataGridHeight,
		fitColumns: true,
		frozenColumns:[[
		                {field:'ck',checkbox:true}
		 ]],
		columns:[[
		    {field:'id',title:'群组ID',width:25,hidden:'true'},
			{field:'groupName',title:'群组名称',width:25,align:'left'},
			{field:'groupDesc',title:'群组描述',width:80,align:'left',sortable:true}
		]],onLoadSuccess:function(data){
			if(data.rows.length>0){
				checkSelectedData(historyIdArr,"groupList");
			}
		},onDblClickRow:function(rowIndex, rowData){
			 var rowIndexV=getDataGridRowIndex("selectTargetDataGrid",rowData.id);
	            if(rowIndexV == -1){
	            	var selectionData = {"id":rowData.id,"name":rowData.groupName,type:"group",typeName:"群组"};
	            	var groupTargetDataGrid = $('#selectTargetDataGrid') ;
	            	 groupTargetDataGrid.datagrid('appendRow',selectionData);
	            	 groupTargetDataGrid.datagrid('selectRow',getDataGridRowIndex("selectTargetDataGrid",rowData.id));
	           }
        }
	});
};
function loadPositionList(historyIdArr){
	var dataGridHeight = $(".easyui-layout").height()-73;
	$('#positionList').datagrid({
		url:'platform/commonSelection/commonSelectionController/getSysPosition?type=position',
		idField:'id',
		width: '100%',
	    nowrap: false,
	    striped: true,
	    autoRowHeight:false,
	    checkOnSelect:true,
	    remoteSort : false,
		height: dataGridHeight,
		fitColumns: true,
		frozenColumns:[[
		                {field:'ck',checkbox:true}
		 ]],
		columns:[[
            {field:'id',title:'岗位ID',width:25,hidden:'true'},
			{field:'positionName',title:'岗位名称',width:25,align:'left'},
			{field:'positionDesc',title:'岗位描述',width:80,align:'left',sortable:true}
		]],onLoadSuccess:function(data){
			if(data.rows.length>0){
				checkSelectedData(historyIdArr,"positionList");
			}
		},onDblClickRow:function(rowIndex, rowData){
            var rowIndexV=getDataGridRowIndex("selectTargetDataGrid",rowData.id);
            if(rowIndexV == -1){
            	var selectionData = {"id":rowData.id,"name":rowData.positionName,type:"position",typeName:"岗位"};
            	var positionTargetDataGrid = $('#selectTargetDataGrid') ;
            	 positionTargetDataGrid.datagrid('appendRow',selectionData);
            	 positionTargetDataGrid.datagrid('selectRow',getDataGridRowIndex("selectTargetDataGrid",rowData.id));
            }
       }
	});
};
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
};

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
};
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
};
function loadTargetDataGridTable(dataGridHeight){
	$('#selectTargetDataGrid').datagrid({
		fitColumns: true,
		remoteSort: false,
		nowrap:false,
		idField:'id',
	//	pagination:true,
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
		columns:[[
			{field:'id',title:'ID',width:20,align:'center' ,hidden : true},
			{field:'type',title:'type',width:10,align:'center',hidden : true},
			{field:'typeName',title:'类型',width:150,align:'center'},
			{field:'name',title:'名称',width:150,align:'center'}
		]],onLoadSuccess:function(data){
			//$('#selectTargetDataGrid').datagrid("checkAll");
		},onDblClickRow:function(rowIndex, rowData){
			$('#selectTargetDataGrid').datagrid('deleteRow',rowIndex);
			var type = rowData.type ;
			if(type=='user'){
				var nodes = $('#orgTree_user').tree('getChecked');
				if(null != nodes && typeof(nodes)!='undefined' && nodes.length>0){
					for(var j = 0; j< nodes.length; j++){
						if(nodes[j].id==rowData.id){
							$('#orgTree_user').tree('uncheck',nodes[j].target); 
							break ;
						}
					}
				}
			}else if(type=='dept'){
				var deptNodes = $('#orgTree_dept').tree('getChecked');
				if(null != deptNodes && typeof(deptNodes)!='undefined' && deptNodes.length>0){
				for(var j = 0; j< deptNodes.length; j++){
					if(deptNodes[j].id==rowData.id){
						$('#orgTree_dept').tree('uncheck',deptNodes[j].target); 
						break ;
					}
				}
			  }
			}else if(type=='role'){
				var roleList = $('#roleList').datagrid('getChecked');
				if(null != roleList && typeof(roleList)!='undefined' && roleList.length>0){
				for(var j = 0; j< roleList.length; j++){
					if(roleList[j].id==rowData.id){
						$('#roleList').datagrid('uncheckRow',getDataGridRowIndex("roleList",rowData.id)); 
						break ;
					}
				}
			  }
			}else if(type=='group'){
				var roleList = $('#groupList').datagrid('getChecked');
				if(null != roleList && typeof(roleList)!='undefined' && roleList.length>0){
				for(var j = 0; j< roleList.length; j++){
					if(roleList[j].id==rowData.id){
						$('#groupList').datagrid('uncheckRow',getDataGridRowIndex("groupList",rowData.id)); 
						break ;
					}
				}
			  }
			}else if(type=='position'){
				var roleList = $('#positionList').datagrid('getChecked');
				if(null != roleList && typeof(roleList)!='undefined' && roleList.length>0){
				for(var j = 0; j< roleList.length; j++){
					if(roleList[j].id==rowData.id){
						$('#positionList').datagrid('uncheckRow',getDataGridRowIndex("positionList",rowData.id)); 
						break ;
					}
				}
			  }
			}
		}
	});
};
/**
 * 初始化datagrid
 */
function loadUserTargetDataGrid(){
	 $('#selectTargetDataGrid').datagrid({
			fitColumns: true,
			remoteSort: false,
			nowrap:false,
			idField:'userId',
		//	pagination:true,
			loadMsg:"数据加载中.....",
		//	singleSelect:singleSelect,
			rownumbers:true,
		 //   height : dataGridHeight,
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
				{field:'userName',title:'用户名',width:150,align:'center'}
			]],onLoadSuccess:function(data){
				//$('#selectTargetDataGrid').datagrid("checkAll");
			},onDblClickRow:function(rowIndex, rowData){
				$('#selectTargetDataGrid').datagrid('deleteRow',rowIndex);
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
};
/**
 * 初始化datagrid
 */
function loadDeptTargetDataGrid(){
	 $('#selectTargetDataGrid').datagrid({
			fitColumns: true,
			remoteSort: false,
			nowrap:false,
			idField:'deptId',
			loadMsg:"数据加载中.....",
		//	singleSelect:singleSelect,
			rownumbers:true,
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
				//$('#selectTargetDataGrid').datagrid("checkAll");
			},onDblClickRow:function(rowIndex, rowData){
				$('#selectTargetDataGrid').datagrid('deleteRow',rowIndex);
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
};
/**
 * 
 */
function addSelectDataToGrid(){
//	var tabContainer=$('#comprehensiveTabControl');
//	var currTab= tabContainer.tabs('getSelected'); 
//	var index = tabContainer.tabs('getTabIndex',currTab);
	if(tabSelectedIndex=='2'){ //角色
		addSelectedDataByTab("roleList","selectTargetDataGrid",'role');
	}else if(tabSelectedIndex=='3'){//群组
		addSelectedDataByTab("groupList","selectTargetDataGrid",'group');
	}else if(tabSelectedIndex=='4'){//岗位
		addSelectedDataByTab("positionList","selectTargetDataGrid",'position');
	}
};
/**
 * 将 tab中datagrid中已选中的数据添加到目标datagrid
 * @param sourceGridId  tab中源datagrid的ID
 * @param targetGridId  目标datagrid的ID
 */
function addSelectedDataByTab(sourceGridId,targetGridId,type){
	var sourceGridObj = $('#'+sourceGridId) ;
	var allSelectedData = sourceGridObj.datagrid('getChecked');
	if(null!=allSelectedData && allSelectedData.length>0){
		var targetGridObj = $('#'+targetGridId) ;
		for(var j = 0; j< allSelectedData.length; j++){
			var currId = allSelectedData[j].id;
			var currName = "" ;
			var typeName = "" ;
			if(type=='role'){
				currName = allSelectedData[j].roleName;
				typeName = "角色" ;
			}else if(type=='group'){
				currName = allSelectedData[j].groupName;
				typeName = "群组" ;
			}else if(type=='position'){
				currName = allSelectedData[j].positionName;
				typeName = "岗位" ;
			}
			var selectData = {id:currId,name:currName,type:type,typeName:typeName};
			if(getDataGridRowIndex(targetGridId,currId)==-1){
				targetGridObj.datagrid('appendRow',selectData); 		
				targetGridObj.datagrid('checkRow',getDataGridRowIndex(targetGridId,currId)); 		
			}
		}
	}
};
/**
 * 清除目标datagrid中已选中的数据
 * @param sourceGridId  源datagrid的ID
 * @param targetGridId  目标datagrid的ID
 */
function removeAllDataGridData(sourceGridId,targetGridId){
	var sourceGridObj = $('#'+sourceGridId) ;
	sourceGridObj.datagrid('clearChecked');
	sourceGridObj.datagrid('loadData',{total : 0,rows : []});
	if(targetGridId !=null && typeof(targetGridId)!='undefined'){
		var idArr = targetGridId.split(",");
		for(var i = 0 ;i < idArr.length ; i++){
			$('#'+idArr[i]).datagrid('uncheckAll'); 		
		}
	}
};

/**
 * 移除dataGrid所有选择的数据
 */
function removeGridAllSelectedData(sourceGridId){
	var dataGridData = $('#'+sourceGridId).datagrid('getChecked');
	var checkDataSize=dataGridData.length;
	if(checkDataSize>0){
		if(dataGridData[0] != null && typeof(dataGridData[0])!='undefined'){
			$('#'+sourceGridId).datagrid('deleteRow',getDataGridRowIndex(sourceGridId,dataGridData[0].id));
			var selectType = dataGridData[0].type ;
			if(selectType!=null && typeof(selectType)!='undefined'){
				if(selectType == 'user'){
					var userTree = $('#orgTree_user');
					var userNode = userTree.tree('find', dataGridData[0].id);
					if(userNode != null && typeof(userNode)!='undefined'){
						userTree.tree("uncheck",userNode.target);
		        	}
				}
		        if(selectType == 'dept'){
		        	var deptTree = $('#orgTree_dept');
		        	var deptNode = deptTree.tree('find', dataGridData[0].id);
		        	if(deptNode != null && typeof(deptNode)!='undefined'){
		        		deptTree.tree("uncheck",deptNode.target);
		        	}
				}
		        if(selectType == 'role'){
		        	$('#roleList').datagrid('uncheckRow',getDataGridRowIndex(sourceGridId,dataGridData[0].id));
		        	
				}
		        if(selectType == 'group'){
		        	$('#groupList').datagrid('uncheckRow',getDataGridRowIndex(sourceGridId,dataGridData[0].id));
				}
		        if(selectType == 'position'){
		        	$('#positionList').datagrid('uncheckRow',getDataGridRowIndex(sourceGridId,dataGridData[0].id));
				}
			}
		}
		var currDataGridData=$('#'+sourceGridId).datagrid('getChecked');
		if(currDataGridData.length>=1){
			removeGridAllSelectedData(sourceGridId);
		}
	}
};
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
	 
};
/**
 * 部门查询回调函数
 * @param queryResult
 */
function loadDeptTreeQueryResult(queryResult){
	 var queryJson =eval(queryResult);
	 $('#orgTree_dept').tree("loadData",queryJson);
	 $('#orgTree_dept').tree('expandAll');
};
/**
 * 综合选择页面初始化时回填数据
 * @param mappingConfigJson
 */
function displayParentPageSelectedData(mappingConfigJson){
	var allRows = [] ;
	if(mappingConfigJson!=null && typeof(mappingConfigJson)!='undefined'){
		var  typeName =  "" ;
		var idArr = [] ;
		var nameArr =[] ;
		var splitChar = "," ;
		if(mappingConfigJson.splitChar != null && typeof(mappingConfigJson.splitChar)!='undefined' ){
			splitChar = mappingConfigJson.splitChar ;
		}
		
        if(mappingConfigJson.user != null && mappingConfigJson.user.tabShow=='1'){
        	typeName = "用户" ;
        	idArr = (mappingConfigJson.user.userId).split(splitChar);
    		nameArr = (mappingConfigJson.user.userName).split(splitChar);
    		if( idArr.length > 0 && nameArr.length > 0 ){
    			for(var i = 0 ; i< idArr.length ;i++){
    				var userId = parent.$("#"+idArr[i]).val();
    			    var userName =  parent.$("#"+nameArr[i]).val();
    			    if(userId != null && userId.length>0 && userName!= null){
    			    		var userIdValArr = userId.split(splitChar);
    			    		var userNameValArr = userName.split(splitChar);
    			    		if(userIdValArr != null && typeof(userIdValArr)!='undefined' && userIdValArr.length>0 && userNameValArr.length>0){
    			    		   for(var j = 0 ; j<userIdValArr.length ; j++){
    			    			   allRows.push({"id":userIdValArr[j],"type":"user","typeName":typeName,"name":userNameValArr[j]}) ;
    			    		   }
    			    		}
    			     }
    			 }
    		 }	
    	}
        if(mappingConfigJson.dept != null && mappingConfigJson.user.tabShow=='1'){
        	typeName = "部门" ;
        	idArr = mappingConfigJson.dept.deptId.split(splitChar);
    		nameArr = mappingConfigJson.dept.deptName.split(splitChar);
    		if( idArr.length > 0 && nameArr.length > 0 ){
    			
    			for(var i = 0 ; i< idArr.length ;i++){
    				var deptId =  parent.$("#"+idArr[i]).val();
    			    var deptName =  parent.$("#"+nameArr[i]).val();
    			    if(deptId != null && deptId.length>0 && deptName!= null){
    			    	var deptIdValArr = deptId.split(splitChar);
			    		var deptNameValArr = deptName.split(splitChar);
			    		if(deptIdValArr != null && typeof(deptIdValArr)!='undefined' && deptIdValArr.length>0 && deptNameValArr.length>0){
			    		   for(var k = 0 ; k<deptIdValArr.length ; k++){
			    			   allRows.push({"id":deptIdValArr[k],"type":"dept","typeName":typeName,"name":deptNameValArr[k]}) ;
			    		   }
			    		}
    				  
    			    }
    			}	
    		}
		}
        if(mappingConfigJson.role != null && mappingConfigJson.user.tabShow=='1'){
        	typeName = "角色" ;
        	idArr = mappingConfigJson.role.roleId.split(splitChar);
    		nameArr = mappingConfigJson.role.roleName.split(splitChar);
    		if( idArr.length > 0 && nameArr.length > 0 ){
    			
    			for(var i = 0 ; i< idArr.length ;i++){
    				var roleId =  parent.$("#"+idArr[i]).val();
    			    var roleName =  parent.$("#"+nameArr[i]).val();
    			    if(roleId != null && roleId.length>0 && roleName!= null){
    			    	var roleIdValArr = roleId.split(splitChar);
			    		var roleNameValArr = roleName.split(splitChar);
			    		if(roleIdValArr != null && typeof(roleIdValArr)!='undefined' && roleIdValArr.length>0 && roleNameValArr.length>0){
			    		   for(var m = 0 ; m<roleIdValArr.length ; m++){
			    			   allRows.push({"id":roleIdValArr[m],"type":"role","typeName":typeName,"name":roleNameValArr[m]}) ;
			    		   }
			    		}
    			    }
    			}	
    		}
		}
        if(mappingConfigJson.group != null && mappingConfigJson.user.tabShow=='1'){
        	typeName = "群组" ;
        	idArr = mappingConfigJson.group.groupId.split(splitChar);
    		nameArr = mappingConfigJson.group.groupName.split(splitChar);
    		if( idArr.length > 0 && nameArr.length > 0 ){
    			
    			for(var i = 0 ; i< idArr.length ;i++){
    				var groupId =  parent.$("#"+idArr[i]).val();
    			    var groupName =  parent.$("#"+nameArr[i]).val();
    			    if(groupId != null && groupId.length>0 && groupName!= null){
    			    	var groupIdValArr = groupId.split(splitChar);
			    		var groupNameValArr = groupName.split(splitChar);
			    		if(groupIdValArr != null && typeof(groupIdValArr)!='undefined' && groupIdValArr.length>0 && groupNameValArr.length>0){
			    		   for(var n = 0 ; n<groupIdValArr.length ; n++){
			    			   allRows.push({"id":groupIdValArr[n],"type":"group","typeName":typeName,"name":groupNameValArr[n]}) ;
			    		   }
			    		}
    				 
    			    }
    			}	
    		}
		}
        if(mappingConfigJson.position != null && mappingConfigJson.user.tabShow=='1'){
        	typeName = "岗位" ;
        	idArr = mappingConfigJson.position.positionId.split(splitChar);
    		nameArr = mappingConfigJson.position.positionName.split(splitChar);
    		if( idArr.length > 0 && nameArr.length > 0 ){
    			for(var i = 0 ; i< idArr.length ;i++){
    				var positionId =  parent.$("#"+idArr[i]).val();
    			    var positionName =  parent.$("#"+nameArr[i]).val();
    			    if(positionId != null && positionId.length>0 && positionName!= null){
    			    	var positionIdValArr = positionId.split(splitChar);
			    		var positionNameValArr = positionName.split(splitChar);
			    		if(positionIdValArr != null && typeof(positionIdValArr)!='undefined' && positionIdValArr.length>0 && positionNameValArr.length>0){
			    		   for(var p = 0 ; p<positionIdValArr.length ; p++){
			    			   allRows.push({"id":positionIdValArr[p],"type":"position","typeName":typeName,"name":positionNameValArr[p]}) ; 
			    		   }
			    		}
    				   
    			    }
    			}	
    		}
		}
	}
	$("#selectTargetDataGrid").datagrid("loadData",{"total":allRows.length,"rows":allRows});
	$("#selectTargetDataGrid").datagrid("checkAll");
};
/**
 * 解析综合选择群组 角色  岗位 映射配置 将对应id解析出来放入数组返回
 * @param type
 * @param mappingConfig
 * @returns {Array}
 */
function parseComprehensiveMappingConfig(type,mappingConfig){
	var idArr = new Array();
	if(mappingConfig != null && typeof(mappingConfig) != 'undefined'){
		var splitChar = mappingConfig.splitChar ;
		if( splitChar == null || typeof(splitChar) == 'undefined' || splitChar.length==0){
			splitChar = ',' ;
		}
		if(type == 'role'){
			var roleIdStr = mappingConfig.role.roleId ;
			if(roleIdStr != null && typeof(roleIdStr) != 'undefined'){
				var selectedRoleId = parent.$("#"+roleIdStr).val();
				if(selectedRoleId != null && typeof(selectedRoleId) != 'undefined' && selectedRoleId.length>0){
					idArr = selectedRoleId.split(splitChar);
				}
			}
		}
		if(type == 'group'){
			var groupIdStr = mappingConfig.group.groupId ;
			if(groupIdStr != null && typeof(groupIdStr) != 'undefined'){
				var selectedGroupId = parent.$("#"+groupIdStr).val();
				if(selectedGroupId != null && typeof(selectedGroupId) != 'undefined' && selectedGroupId.length>0){
					idArr = selectedGroupId.split(splitChar);
				}
			}
		}
		if(type == 'position'){
			var positionIdStr = mappingConfig.position.positionId ;
			if(positionIdStr != null && typeof(positionIdStr) != 'undefined'){
				var selectedPositionId = parent.$("#"+positionIdStr).val();
				if(selectedPositionId != null && typeof(selectedPositionId) != 'undefined' && selectedPositionId.length>0){
					idArr = selectedPositionId.split(splitChar);
				}
			}
		}
	}
	return idArr ;
};
/**
 * 根据已知id值选中datagrid数据
 * @param historyId     带有分隔符号的id的字符串     必填
 * @param dataGridId                          必填
 */
function checkSelectedData(historyIdArr,dataGridId){
	if(historyIdArr != null && typeof(historyIdArr)!='undefined' && historyIdArr.length>0){
			jQuery.each(historyIdArr,function(x,currentId){
				var rowIndex = jQuery('#'+dataGridId).datagrid('getRowIndex',currentId);
				if(rowIndex!=-1){
					jQuery('#'+dataGridId).datagrid('checkRow',rowIndex);
				}
			});
	}
};