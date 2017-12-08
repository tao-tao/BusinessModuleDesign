function loadRole(comId){
	$("#roleList").datagrid('load',{currentComId:comId});
	$("#roleList").datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
}
function loadUser(comId){
	$("#dgUser").datagrid('load',{currentComId:comId});
	$("#dgUser").datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
}
function loadGroup(comId){
	$("#groupList").datagrid('load',{currentComId:comId});
	$("#groupList").datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
}
function loadPosition(comId){
	$("#positionList").datagrid('load',{currentComId:comId});
	$("#positionList").datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
}
function loadDept(comId){
	$("#orgTree_dept").datagrid('load',{currentComId:comId});
	$("#orgTree_dept").datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
}

function clickComponentCell(rowIndex, field, value){
	switch(field){
	 	case 'ACCESSIBILITY':
	 		if(value == null || typeof(value) =='undefined'||value.length==0){
	 			setComponentAuth('1','1',rowIndex);
	 		}else if(value == '1'){
	 			setComponentAuth('0','',rowIndex);
	 		}else if(value == '0'){
	 			setComponentAuth('1','',rowIndex);
	 		}
	 		
	 		break ;
	 	case 'OPERABILITY':
	 		if(value == null || typeof(value) =='undefined'||value.length==0){
	 			setComponentAuth('1','1',rowIndex);
	 		}else if(value == '1'){
	 			setComponentAuth('','0',rowIndex);
	 		}else if(value == '0'){
	 			setComponentAuth('','1',rowIndex);
	 		}
	 		break ;
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
	    url:'platform/sysAccessControlController/getSysAccesscontrolData/R.json',
		idField:'ID',
		//width: '100%',
		singleSelect: true,
	    nowrap: false,
	    striped: true,
	    autoRowHeight:false,
	    checkOnSelect:true,
	    selectOnCheck: false,
	    remoteSort : false,
		//height: dataGridHeight,
		fitColumns: true,
		queryParams: {currentComId: currentMenuId},
		onClickCell:clickComponentCell,
		columns:[[
                    {field:'ID',title:'权限ID',width:25,checkbox:true,fit:true},
					{field:'ROLE_NAME',title:'角色名称',width:75,align:'left',fit:true},
					{field:'ROLE_DESC',title:'角色描述',width:60,align:'left',sortable:true,fit:true},
					{field:'ACCESSIBILITY',title:'访问权限',width:60,align:'left',sortable:false,fit:true,formatter:comReadAuthFormat},
					{field:'OPERABILITY',title:'编辑权限',width:60,align:'left',sortable:false,fit:true,formatter:comWriteAuthFormat}
				]],
				toolbar: '#RoleToolbar'
	});
}

/**
 * 加载群组数据
 */
function loadGroupList(){
	$('#groupList').datagrid({
		fit: true,
		url:'platform/sysAccessControlController/getSysAccesscontrolData/G.json',
		idField:'ID',
		singleSelect: true,
	    nowrap: false,
	    striped: true,
	    autoRowHeight:false,
	    checkOnSelect:true,
	    selectOnCheck: false,
	    remoteSort : false,
	    queryParams: {currentComId: currentMenuId},
		fitColumns: true,
		onClickCell:clickComponentCell,
		columns:[[
		    {field:'ID',title:'权限ID',width:25,checkbox:true,fit:true},
			{field:'GROUP_NAME',title:'群组名称',width:25,align:'left',fit:true},
			{field:'GROUP_DESC',title:'群组描述',width:80,align:'left',sortable:true,fit:true},
			{field:'ACCESSIBILITY',title:'访问权限',width:60,align:'left',sortable:false,fit:true,formatter:comReadAuthFormat},
			{field:'OPERABILITY',title:'编辑权限',width:60,align:'left',sortable:false,fit:true,formatter:comWriteAuthFormat}
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
		url:'platform/sysAccessControlController/getSysAccesscontrolData/P.json',
		idField:'ID',
		//width: '100%',
	    nowrap: false,
	    striped: true,
	    singleSelect: true,
	    autoRowHeight:false,
	    selectOnCheck: false,
	    checkOnSelect:true,
	    remoteSort : false,
	    queryParams: {currentComId: currentMenuId},
		//height: dataGridHeight,
		fitColumns: true,
		onClickCell:clickComponentCell,
		columns:[[
            {field:'ID',title:'权限ID',width:25,checkbox:true,fit:true},
			{field:'POSITION_NAME',title:'岗位名称',width:25,align:'left',fit:true},
			{field:'ACCESSIBILITY',title:'访问权限',width:10,align:'left',sortable:false,fit:true,formatter:comReadAuthFormat},
			{field:'OPERABILITY',title:'编辑权限',width:10,align:'left',sortable:false,fit:true,formatter:comWriteAuthFormat}
		]],onLoadSuccess:function(data){
			if(data.rows.length>0){
				
			}
		},
		toolbar: '#positionToolbar'
	});
}

/**
 * 加载组织树
 */
function loadDeptTree(){
	
	    $('#orgTree_dept').datagrid({
			fit: true,
			url:'platform/sysAccessControlController/getSysAccesscontrolData/D.json',
			idField:'ID',
			//width: '100%',
		    nowrap: false,
		    striped: true,
		    singleSelect: true,
		    autoRowHeight:false,
		    selectOnCheck: false,
		    checkOnSelect:true,
		    remoteSort : false,
		    queryParams: {currentComId: currentMenuId},
			//height: dataGridHeight,
			fitColumns: true,
			onClickCell:clickComponentCell,
			columns:[[
	            {field:'ID',title:'权限ID',width:25,checkbox:true,fit:true},
				{field:'DEPT_NAME',title:'部门名称',width:25,align:'left',fit:true},
				{field:'DEPT_CODE',title:'部门编号',width:25,align:'left',fit:true},
				{field:'ACCESSIBILITY',title:'访问权限',width:10,align:'left',sortable:false,fit:true,formatter:comReadAuthFormat},
				{field:'OPERABILITY',title:'编辑权限',width:10,align:'left',sortable:false,fit:true,formatter:comWriteAuthFormat}
			]],onLoadSuccess:function(data){
				if(data.rows.length>0){
					
				}
			},
			toolbar: '#deptToolbar'
		});
}

function comReadAuthFormat(value, rowData, rowIndex) {
	if("1" == value)   
		return "<img src='avicit/platform6/modules/system/sysaccesscontrol/images/ok.png' title='允许访问' alt='允许访问' >";
	if("0" == value) 
		return "<img src='avicit/platform6/modules/system/sysaccesscontrol/images/no.gif' title='禁止访问' alt='禁止访问' >";
	
	return "<img src='avicit/platform6/modules/system/sysaccesscontrol/images/untitled.png' title='未设置权限' alt='未设置权限' >";	
}
//编辑权限formatter
function comWriteAuthFormat(value, rowData, rowIndex) {
	if("1" == value)   
		return "<img src='avicit/platform6/modules/system/sysaccesscontrol/images/ok.png' title='允许编辑' alt='允许编辑' >";
	if("0" == value) 
		return "<img src='avicit/platform6/modules/system/sysaccesscontrol/images/no.gif' title='禁止编辑' alt='禁止编辑' >";
	
	return "<img src='avicit/platform6/modules/system/sysaccesscontrol/images/untitled.png' title='未设置权限' alt='未设置权限' >";	
}

function initTabContainer() {
	$('#comprehensiveTabControl')
			.tabs(
					{
						onSelect : function(title, index) {
							if (index == 0) {
								currTabIndex = "0";
								loadRoleList();
							}
							if (index == 1) {
								currTabIndex = "1";
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
													},
													prompt : "请输入查询内容"
												});
								$("#dgUser").datagrid("options").url='platform/sysAccessControlController/getSysAccesscontrolData/U.json';
								loadUser(currentMenuId);
							
							}
							if (index == 2) {
								currTabIndex = "2";
								$('#deptQueryText').searchbox({
									width : 200,
									searcher : function(value) {
										if (value == "请输入查询内容") {
											value = "";
										}
										searchDeptFun(value);
										//停止datagrid的编辑.
									},
									prompt : "请输入查询内容"
								});
								loadDeptTree();
							}
							if (index == 3) {
								currTabIndex = "3";
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
								currTabIndex = "4";
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
	
	loadRoleList();
}

//查询
function searchRoleFun(queryKeyWord){
	if(queryKeyWord==null){
		$.messager.alert("操作提示", "请输入查询条件！","info");
		return ;
    }
	var queryParams = $('#roleList').datagrid('options').queryParams; 
	var searchData = {
			"filter-LIKE-ROLE_NAME":queryKeyWord
	} ;
	queryParams.param=JSON.stringify(searchData);
	
	
   $('#roleList').datagrid('options').queryParams=queryParams;        
    $("#roleList").datagrid('load'); 
}

function searchDeptFun(queryKeyWord){
	if(queryKeyWord==null){
		$.messager.alert("操作提示", "请输入查询条件！","info");
		return ;
    }
	var queryParams = $('#orgTree_dept').datagrid('options').queryParams; 
	var searchData = {
			"filter-LIKE-DEPT_NAME":queryKeyWord
	} ;
	queryParams.param=JSON.stringify(searchData);
	
	
   $('#orgTree_dept').datagrid('options').queryParams=queryParams;        
    $("#orgTree_dept").datagrid('load'); 
}

//查询
function searchPositionFun(queryKeyWord){
	if(queryKeyWord==null){
		$.messager.alert("操作提示", "请输入查询条件！","info");
		return ;
	}
	var queryParams = $('#positionList').datagrid('options').queryParams;  
	var searchData = {
			"filter-LIKE-POSITION_NAME":queryKeyWord
	} ;
	queryParams.param=JSON.stringify(searchData);
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
	var searchData = {
			"filter-LIKE-GROUP_NAME":queryKeyWord
	} ;
	queryParams.param=JSON.stringify(searchData);
    $('#groupList').datagrid('options').queryParams=queryParams;        
    $("#groupList").datagrid('load'); 
}


/**
 * 用户tab输入查询内容后查询数据
 * @returns
 */
function searchData(){
	var depeId = $("#authSelectedDeptId").val();
	var type = "" ;
	if(depeId!= null && depeId.length>0){
		type="dept";
	}
	var queryKeyWord = $('#userQueryText').searchbox("getValue");
	var queryParams = $('#dgUser').datagrid('options').queryParams;  
	var searchData = {
			'filter-LIKE-LOGIN_NAME':queryKeyWord
	} ;
	queryParams.param=JSON.stringify(searchData);
	$('#dgUser').datagrid('options').queryParams=queryParams;   
	$('#dgUser').datagrid('load');
	$("#dgUser").datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
}


function delResources(){
	var rows = getTabRows('getChecked');
	var ids = [];
	var l=rows.length;
	if (rows.length > 0) {
		$.messager.confirm('请确认','您确定要删除当前所选的数据？',
			function(b){
				if(b){
					for (;l--;) {
						ids.push(rows[l].ID);
					}
					$.ajax({
						url : 'platform/sysAccessControlController/deleteSysAccesscontrol.json',
						data:	JSON.stringify(ids),
						contentType : 'application/json',
						type : 'post',
						dataType : 'json',
						success : function(result) {
							if (result.flag == "success") {
									reloadTabData(currTabIndex);
									$.messager.show({
										title : '提示',
										msg : '操作成功！',
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


function setComponentAuth(readStatus,writeStatus,rowIndex){
	var rows;
	if (rowIndex!=null){
		rows = getTabRows('getRows');
	}else{
		rows = getTabRows('getChecked');
	}
	
	var ids = [];
	var l=rows.length;
	if (rows.length > 0) {
		$.messager.confirm('请确认','您确定要修改当前所选的数据权限？',
			function(b){
				if(b){
					if(rowIndex!=null){
						ids.push(rows[rowIndex].ID);
					}else{
						for (;l--;) {
							ids.push(rows[l].ID);
						}
					}
					$.ajax({
						url : 'platform/sysAccessControlController/updateComponentResourceAuth.json',
						data:	{ids:ids.join(','),readStatus:readStatus,writeStatus:writeStatus},
						type : 'post',
						dataType : 'json',
						success : function(result) {
							if (result.flag == "success") {
									reloadTabData(currTabIndex);
									$.messager.show({
										title : '提示',
										msg : '操作成功！',
										timeout:2000,  
								        showType:'slide'  
									});
								
							}
						}
					});
				}
			});
	} else {
		$.messager.alert('提示', '请选择要修改权限的记录！', 'warning');
	}
}


function getTabRows(chooseType){
	var rows;
	if(currTabIndex==0){
		rows = $('#roleList').datagrid(chooseType);
	}else if(currTabIndex==1){
		rows = $('#dgUser').datagrid(chooseType);
	}else if(currTabIndex==2){
		rows = $('#orgTree_dept').datagrid(chooseType);
	}else if(currTabIndex==3){
		rows = $('#groupList').datagrid(chooseType);
	}else{
		rows = $('#positionList').datagrid(chooseType);
	}
	return rows;
}

/**
 * 添加用户
 */
function insertUser(){
	//var path="platform/sysAccessControlController/toAddUserJsp?type=menu";
	//var usd = new CommonDialog("insertUserDialog","700","435",path,"添加用户",false,true,false);
	//usd.show();
	
	var comSelector = new CommonSelector("user","userSelectCommonDialog","userId","userName",null,null,null,false,null,null,600,400);
	comSelector.init(false,'selectUserDialogCallBack','n'); //选择人员  回填部门 */
}

function selectUserDialogCallBack(data){
	var ids = [];
	var l =data.length;
	if (l>0){
	for(;l--;){
		 ids.push(data[l].userId);
	 }
	 $.ajax({
		 url:'platform/sysAccessControlController/insertComponentResourceAuth/U/'+currentMenuId+'.json',
		 data:	JSON.stringify(ids),
		 contentType : 'application/json',
		 type : 'post',
		 dataType : 'json',
		 success : function(r){
			 if (r.flag == "success") {
				 reloadTabData(currTabIndex);
				 $.messager.show({
					 title : '提示',
					 msg : '操作成功！'
				});
			}else{
				$.messager.show({
					 title : '提示',
					 msg : r.msg
				});
			}
		 }
	 });
	}
}

/**
 * 添加角色
 */
function insertRole(){
	var path="platform/sysAccessControlController/toAddRoleJsp?type=menu";
	var usd = new CommonDialog("insertRoleDialog","700","435",path,"添加角色",false,true,false);
	usd.show();
}


/**
 * 添加群组
 */
function insertGroup(){
	var path="platform/sysAccessControlController/toAddGroupJsp?type=menu";
	var usd = new CommonDialog("insertGroupDialog","700","435",path,"添加群组",false,true,false);
	usd.show();
}


/**
 * 添加岗位
 */
function insertPosition(){
	var path="platform/sysAccessControlController/toAddPositionJsp?type=menu";
	var usd = new CommonDialog("insertPositionDialog","700","435",path,"添加岗位",false,true,false);
	usd.show();
}

//选择部门
function mySelectDept(){
	var deptSelector = new CommonSelector("dept","deptSelectCommonDialog","deptId","deptName",null,null,null,false,null,null,600,400);
	deptSelector.init(false,"selectDeptDialogCallBack",'n');
}
//部门选择回调
function selectDeptDialogCallBack(data){
	var ids = [];
	var l =data.length;
	if (l>0){
	for(;l--;){
		 ids.push(data[l].deptId);
	 }
	$.ajax({
		 url:'platform/sysAccessControlController/insertComponentResourceAuth/D/'+currentMenuId+'.json',
		 data:	JSON.stringify(ids),
		 contentType : 'application/json',
		 type : 'post',
		 dataType : 'json',
		 success : function(r){
			 if (r.flag == "success") {
				 reloadTabData(currTabIndex);
				 $.messager.show({
					 title : '提示',
					 msg : '操作成功！'
				});
			}else{
				$.messager.show({
					 title : '提示',
					 msg : r.msg
				});
			}
		 }
	 });
	}else{
		 $.messager.show({
			 title : '提示',
			 msg : '操作失败，请选择部门！'
		});
	}
}

$closeAddUserDialog = function(){
	 $("#insertUserDialog").dialog("close");
};

$closeAddRoleDialog = function(){
	 $("#insertRoleDialog").dialog("close");
};

$closeAddGroupDialog = function(){
	 $("#insertGroupDialog").dialog("close");
};


$closeAddPositionDialog = function(){
	 $("#insertPositionDialog").dialog("close");
};