var processSelectUserComponentEvent = {
		/**
		 * 树中数据用户节点选中到目标DataGrid
		 */
		selectedTreeUserNodeToTargetDataGrid : function(node,checked,branchNo){
			if(checked){//选中
				var record = {
			    		userId : node.id,
			    		userName : node.text,
			    		deptId : node.attributes.deptId,
			    		deptName : node.attributes.deptName
				};
				processSelectUserComponentEvent.eventAppendToSelectorTargetDataGrid(branchNo,record);
			} else {//反选
				var index = getRecordIndexInTargetDataGrid(node.id,branchNo);
				//增加判断 add by xingc
				if(index != -1){
					$('#selectorUserTabForTargetDataGrid_' + branchNo).datagrid('deleteRow', index);
				}
			}
		},
		
		/**
		 * 刷新目标DataGrid
		 * @param branchNo
		 */
		refreshSelectedTree: function (branchNo){
			var datas = reloadDataForDataGridOrderType($('#selectorUserTabForTargetDataGrid_' + branchNo).datagrid('getData'));
			$('#selectorUserTabForTargetDataGrid_' + branchNo).datagrid('loadData',datas);
		},
		
		/**
		 * 根据userId,校验是否在目标选择的dataGrid中存在
		 * @param branchNo
		 * @param userId
		 * @return true  : 存在
		 *         false : 不存在
		 */
		eventCheckIsHaveForTargetDataGrid : function(branchNo,userId){
			var datas = $('#selectorUserTabForTargetDataGrid_' + branchNo).datagrid('getData');
			if(datas.rows.length > 0){
				for(var i = 0 ; i < datas.rows.length ; i++){
					if(userId == datas.rows[i].userId){
						return true;
					}
				}
			}
			return false;
		},
		/**
		 * 向目标datagrid追加数据
		 * @param branchNo
		 * @param record
		 */
		eventAppendToSelectorTargetDataGrid : function(branchNo,record){
			if(conditions.getDealType(branchNo) == 1){//单人处理
				var datas = $('#selectorUserTabForTargetDataGrid_' + branchNo).datagrid('getData');
				if(datas.rows.length > 0){
					var isRecoverFlag = confirm('该节点为【单人处理】方式,确定要覆盖吗?');
					if(isRecoverFlag){
						//清空目标选择datagrid数据
						var data = {
								total : 0,
								rows : new Array()
						};
						$('#selectorUserTabForTargetDataGrid_' + branchNo).datagrid('loadData',data);
						
						//追加数据
						record.order = -2;
						record.orderIndex = 0;
						$('#selectorUserTabForTargetDataGrid_' + branchNo).datagrid('appendRow',record);
					}
					//清空select source tab标签页各控件的多余选择
//					var userIds = new Array();
//					userIds.push(record.userId);
//					unCheckedOfSelectorData(userIds,branchNo);
				} else {
					//追加数据
					record.order = -2;
					record.orderIndex = 0;
					$('#selectorUserTabForTargetDataGrid_' + branchNo).datagrid('appendRow',record);
				}
			} else {
				if (record != undefined && record.userId != undefined) {
					var rowIndex = $('#selectorUserTabForTargetDataGrid_' + branchNo).datagrid('getRowIndex', record.userId);
					//判断是否存在，不存在才增加
					if(rowIndex == -1){
						$('#selectorUserTabForTargetDataGrid_' + branchNo).datagrid('appendRow',record);
					}
				}
			}
		},
		/**
		 * 树的展开事件
		 * @param node
		 */
		eventSelectedTreeNodeExpand : function(object,node,branchNo){
			//判断该节点是否为单人处理,如果不是,搜行event事件 当设置节点审批人的时候不起效果dostepuserdefined
			if(conditions.getDealType(branchNo) != '1' && conditions.isUserSelectTypeAuto(branchNo) && type != 'dostepuserdefined'){
				var nodes = object.tree('getChildren',node.target);
				for(var i = 0 ; i < nodes.length ; i++){
					object.tree('check',nodes[i].target);
					if(nodes[i].attributes.nodeType == 'org' || nodes[i].attributes.nodeType == 'dept'){
						if(!isLeaf){
							if(nodes[i]){
								object.tree('expand',nodes[i].target);
							} else {
								object.tree('expand',nodes[i]);
							}
						}
					}
				}
			}
		},

		/**
		 * 根据"选人方式"进行操作
		 */
		eventOfUserSelectType : function(){
			var getDatas = new GetDatas();
			for(var branchNo = 0 ; branchNo < getDatas.getBranchLength() ;branchNo++){
				if(conditions.isUserSelectTypeAuto(branchNo) && type != 'dostepuserdefined' && type != 'doretreattodraft' && type != 'doretreattoprev' && type != 'dowithdraw'){//选人方式 是"自动"
					if(conditions.getDealType(branchNo) == '1'){//处理方式是"单人顺序"
						if(getDatas.getOrgList(branchNo).length == 0  
								&& getDatas.getDeptList(branchNo).length == 0 	
								&& getDatas.getPositionList(branchNo).length == 0 	
								&& getDatas.getGroupList(branchNo).length == 0 	
								&& getDatas.getRoleList(branchNo).length == 0 	
								&& getDatas.getUserList(branchNo).length == 1) {
							processSelectUserComponentEvent.eventLoadDataToSelectDataGrid(getDatas.getUserList(branchNo),branchNo);
						} else {
							var stepActivityName = getDatas.getNextActivityAlias(branchNo);
							$.messager.alert('提示','节点【' + stepActivityName + '】配置错误!','error');
						}
					} else {
						processSelectUserComponentEvent.eventLoadTabsDataToSelectDataGrid(branchNo);
						processSelectUserComponentEvent.eventLoadDataToSelectDataGrid(getDatas.getUserList(branchNo),branchNo);
					}
				}
				if(type == 'doretreattodraft' || type == 'doretreattoprev' || type == 'dowithdraw'){//退回拟稿人/退回上一步,自动选人
					processSelectUserComponentEvent.eventLoadDataToSelectDataGrid(getDatas.getUserList(branchNo),branchNo);
				}
			}
		},
		//加载指定数据到选中的DataGrid
		eventLoadDataToSelectDataGrid : function(datas,branchNo){
			//setTimeout(function(){
				for(var i = 0 ; i< datas.length ; i++){
					var record = {
							userId : datas[i].id,
							userName : datas[i].name,
							deptName : datas[i].deptName,
							deptId : datas[i].deptId
						};
					processSelectUserComponentEvent.eventAppendToSelectorTargetDataGrid(branchNo,record);
//					$("#selectorUserTabForTargetDataGrid_" + branchNo).datagrid('appendRow',);
				}
			//},1000);
		},
		//加载流程定义审批人时指定数据到选中的DataGrid
		eventLoadDataToSelectDataGridByUserDefined : function(datas,branchNo){
			//setTimeout(function(){
				for(var i = 0 ; i< datas.length ; i++){
					var record = {
							userId : datas[i].userId,
							userName : datas[i].userName,
							deptName : datas[i].deptName,
							deptId : datas[i].deptId
						};
					processSelectUserComponentEvent.eventAppendToSelectorTargetDataGrid(branchNo,record);
//					$("#selectorUserTabForTargetDataGrid_" + branchNo).datagrid('appendRow',);
				}
			//},1000);
		},
		/**
		 * 加载tab标签页的component的数据到选中的DataGrid
		 */
		eventLoadTabsDataToSelectDataGrid : function(branchNo){
			if($('#selectorUserTabs').find('#selectorUserTab_' + branchNo + '_Content').length == 0){//非分支
				var tabs = $('#selectorUserTabs').tabs('tabs');
				for(var i = 0 ; i < tabs.length ; i++){
					 var pp = $('#selectorUserTabs').tabs('getTab',i);
					 var tabId = pp.panel('options').id;
					 if (tabId != undefined && tabId.length > 5) {
						 var contentId = tabId.substring(5, tabId.length);
						 if (contentId.indexOf("Tree_") > -1) {
							 var roots = $('#' + contentId).tree('getRoots');
							 for(var j = 0 ; j < roots.length ; j++){
								 var root = roots[j];
								 $('#' + contentId).tree('select',root.target);
								 expandAllAndCheckTreeNode(contentId,root);
							 }
						 } else if (contentId.indexOf("Grid_") > -1) {
							 $('#' + contentId).datagrid('selectAll');
						 }
					 }
				}
			} else {//分支
				var tabs = $('#selectorUserTab_' + branchNo + '_Content').tabs('tabs');
				for(var i = 0 ; i < tabs.length ; i++){
					var pp = $('#selectorUserTab_' + branchNo + '_Content').tabs('getTab',i);
					 var tabId = pp.panel('options').id;
					 if (tabId != undefined && tabId.length > 5) {
						 var contentId = tabId.substring(5, tabId.length);
						 if (contentId.indexOf("Tree_") > -1) {
							 var roots = $('#' + contentId).tree('getRoots');
							 for(var j = 0 ; j < roots.length ; j++){
								 var root = roots[j];
								 $('#' + contentId).tree('select',root.target);
								 expandAllAndCheckTreeNode(contentId,root);
							 }
						 } else if (contentId.indexOf("Grid_") > -1) {
							 $('#' + contentId).datagrid('selectAll');
						 }
					 }
				}
			}
		},
		//是否显示选人框
		eventOfIsDisplayUserSelect : function(){
			var getDatas = new GetDatas();
			for(var branchNo = 0 ; branchNo < getDatas.getBranchLength() ;branchNo++){
				if(!conditions.isSelectUser(branchNo)){
					//parent.hideUserSelectArea();
				}
			}
		}
};
/**
 * 获取数据
 */
function GetDatas(){
	this.getBranchLength = function(){
		return dataJson.nextTask.length;
	};
	this.getOrgList = function(branchNo){
		if(dataJson.nextTask[branchNo].orgList == null){
			return new Array();
		}
		return dataJson.nextTask[branchNo].orgList;
	};
	this.getDeptList = function(branchNo){
		if(dataJson.nextTask[branchNo].deptList == null){
			return new Array();
		}
		return dataJson.nextTask[branchNo].deptList;
	};
	this.getUserList = function(branchNo){
		if(dataJson.nextTask[branchNo].userList == null){
			return new Array();
		}
		return dataJson.nextTask[branchNo].userList;
	};
	this.getGroupList = function(branchNo){
		if(dataJson.nextTask[branchNo].groupList == null){
			return new Array();
		}
		return dataJson.nextTask[branchNo].groupList;
	};
	this.getRoleList = function(branchNo){
		if(dataJson.nextTask[branchNo].roleList == null){
			return new Array();
		}
		return dataJson.nextTask[branchNo].roleList;
	};
	this.getPositionList = function(branchNo){
		if(dataJson.nextTask[branchNo].positionList == null){
			return new Array();
		}
		return dataJson.nextTask[branchNo].positionList;
	};
	this.getNextActivityAlias = function(branchNo){
		if(typeof(branchNo) == 'undefined'){
			branchNo = 0;
		}
		return dataJson.nextTask[branchNo].currentActivityAttr.activityAlias;
	};
	/**
	 *  从流程引擎中读取上次流经当前节点时的用户
	 */
	this.getAutoGetSysUsers = function(branchNo){
		$.ajax({
			   type: "GET",
			   url: getPath() + '/platform/user/bpmSelectUserAction/getAutoGetSysUsers',
			   async: false,
//			   data: 'userList=' + JSON.stringify(selectedUsers),
			   data: 'executionId=' + executionId + '&targetActivityName=' + targetActivityName,
			   dataType: 'json',
			   success: function(msg){
			      if(msg.userList.length > 0){
			    	  for(var i = 0 ; i< msg.userList.length ; i++){
			    		  var record = {
			    		    		userId : msg.userList[i].id,
			    		    		userName : msg.userList[i].name,
			    		    		deptId : msg.userList[i].deptId,
			    		    		deptName : msg.userList[i].deptName
			    				};
			    		 processSelectUserComponentEvent.eventAppendToSelectorTargetDataGrid(branchNo,record);
			    	  }
			      }
			   },
			   error : function(msg){
				   $.messager.alert('错误',msg,'error');
				   return ;
			   }
		});
	};
}
/**
 * create
 * @param branchNo
 * @returns
 */
function CreateComponent(branchNo){
	/**
	 *  创建树组件
	 * @param objectId tree id
	 * @param datas 表态数据
	 * @param treeUrl tree url 
	 * @param beforeExpandUrl tree展开前url
	 * @param tabNo 第二层页签no
	 */
	this.createTreeComponent = function(objectId,datas,treeUrl,beforeExpandUrl,tabNo,type){
		var object = $('#' + objectId);
		
		var baseUrl = '';
		if(treeUrl != ''){
			baseUrl = getPath() + treeUrl;
		}
		//改为一个初始化方法，多个会引起问题 modify by xingc
		object.tree({
			url : baseUrl,
			data : datas,
			checkbox : true,
		    lines : true,
		    method : 'post',
			onBeforeExpand:function(node,param){
				var para = node.attributes.para;
				if(typeof(para) == 'undefined'){
					para = '';
				}
				if(type == 'dept'){
					object.tree('options').url = getPath() + beforeExpandUrl + 'nodeType=' + node.attributes.nodeType +'&para=' + para;
				} else {
					object.tree('options').url = getPath() + beforeExpandUrl + 'nodeType=' + node.attributes.nodeType +'&para=' + para;
				}
		    },
		    onExpand : function(node){
		    	if(node.attributes.expandFlag == '' || node.attributes.expandFlag == ""){
		    		processSelectUserComponentEvent.eventSelectedTreeNodeExpand(object,node,branchNo);
		    		node.attributes.expandFlag = 'expanded';
		    	}
		    },
		    onCheck : function(node,checked){
		    	if(node.attributes.nodeType == 'user'){//判断是用户的节点
		    		processSelectUserComponentEvent.selectedTreeUserNodeToTargetDataGrid(node,checked,branchNo);
		    		processSelectUserComponentEvent.refreshSelectedTree(branchNo);
		    	} else if(node.attributes.nodeType == 'dept' || node.attributes.nodeType == 'role' || node.attributes.nodeType == 'position' || node.attributes.nodeType == 'group'){
		    		if(conditions.getDealType(branchNo) == 1){
		    			checked = false;
		    		}
		    		//第一次调用getChildren方法会报错，可能easyui的本身的bug modify by xingc
		    		var subNodes;
					try {
						subNodes = object.tree('getChildren', node.target);
					} catch (e) {
						// TODO: handle exception
					}
					if(subNodes){
						for(var i = 0 ; i < subNodes.length ; i++){
			    			var isLeaf = object.tree('isLeaf',subNodes[i].target);
		    	    		if(isLeaf){
		    	    			processSelectUserComponentEvent.selectedTreeUserNodeToTargetDataGrid(subNodes[i],checked,branchNo);
		    	    		}
			    		}
						processSelectUserComponentEvent.refreshSelectedTree(branchNo);
					}
		    	}
		    },
		    onBeforeCheck : function(node,checked){
		    	//选中之前,判断是否在target dataGrid中存在,如果存,不允许选择(return false)
		    	if(checked && processSelectUserComponentEvent.eventCheckIsHaveForTargetDataGrid(branchNo,node.id)){
		    		//alert('该用户【' + node.text + '】已经选择');
		    		return false;
		    	}
		    	if(checked && conditions.getDealType(branchNo) == 1){
		    		if(node.attributes.nodeType == 'user'){//判断是用户的节点
		    			var checkedDatas = object.tree('getChecked');
						if (checkedDatas.length > 0){
							for(var k = 0 ; k < checkedDatas.length ; k++){
								if(checkedDatas[k].id == node.id){
									object.tree('uncheck', checkedDatas[k].target);
								}
							}
						}
			    	} else if(node.attributes.nodeType == 'dept'){
			    		return false;
			    	}
		    	}
		    	//当树节点是关闭状态的时候才触发
		    	if(checked && node.state=='closed'){
		    		object.tree('expand',node.target);
		    	}
		    },
		    onLoadSuccess:function(node,data){
		    	//加载完树，展开根,默认只展开第一个页签的
		    	if(tabNo==0){
		    		var roots = object.tree('getRoots');
			    	for(i=0;i<roots.length;i++){
			    		var tempNode = roots[i];
			    		object.tree('expand',tempNode.target);
			    	}
		    	}
		    	//加载子节点后，如果父节点已选中，则选中所有的子节点
		    	if(node && node.checked){
		    		var nodes = object.tree('getChildren', node.target);
					for(var i = 0 ; i < nodes.length ; i++){
						if(nodes[i].checked == false){
						    object.tree('check', nodes[i].target);
					    }
					}
		    	}
		    }
		});
	};
	/**
	 * 创建搜索input
	 * @param objectId
	 */
	this.createSearchInputComponent = function(objectId,searchType,para){
		$('#' + objectId + "_content").layout('add', {region: 'north', height: 30, content: '<div class="searcher" id="searcher"><input class="searchInput" name="searcherInfo_' + objectId + '" id="searcherInfo_' + objectId + '" onblur="EventOnBlur(this)" onfocus="eventOnFocus(this)" value="请输入查询内容..." /><div class="searchOp" title="搜索">&nbsp;&nbsp;&nbsp;</div></div>' });
		$('#searcher #searcherInfo_' + objectId).liveSearch({url: getPath() + '/platform/user/bpmSelectUserAction/liveSearcher?searchType=' + searchType + '&para=' + para + '&branchNo=' + branchNo + '&searchInfo='});
	};
}
/**
 * 绘制
 * @returns
 */
function Drawer(branchNo){
	var createComponent = new CreateComponent(branchNo);
	/**
	 * 创建tab
	 * @param tabs
	 * @param tabId
	 * @param tabTitle
	 * @param contentId
	 * @param iconCls
	 */
	this.drawTab = function(tabs,tabId,tabTitle,contentId,iconCls){
		var contentStr = 
			'<div id="' + contentId + '_content" class="easyui-layout" fit="true">' +
               '<div region="center" style="overflow: hidden;">' +
		         '<div id="' + contentId + '" branchNo="' + branchNo + '" style="width: 100%; height: 100%;"></div>' +
		       '</div>' +
		    '</div>';
		tabs.tabs('add',{  
			id : tabId,
		    title: tabTitle,
		    fit: true,
		    closable:false,
		    content : contentStr,
		    iconCls : iconCls
		});
	};
	/**
	 * 绘制组织标签
	 */
	this.drawOrgTabContent = function(data,objectId,tabNo){
		var datas = [];
		var ids = '';
		for(var i = 0 ; i< data.length ; i++){
			var jsonData = {
					id : data[i].id,
					text: data[i].name,
					state: 'closed',
					nodeType : 'org',
					iconCls : 'icon-org',
					attributes : {
						nodeType : 'org',
						expandFlag : ''
					}
			};
			ids += data[i].id + ',';
			datas[i] = jsonData;
		}
		var beforeExpandUrl = '/platform/user/bpmSelectUserAction/dept?';
		createComponent.createTreeComponent(objectId,datas,'',beforeExpandUrl,tabNo,'org');
		//绘制搜索input
		createComponent.createSearchInputComponent(objectId,'org',ids,branchNo);
	};
	/**
	 * 绘制部门标签
	 */
	this.drawDepartmentTabContent = function(data,objectId,tabNo){
		var ids = '';
		for(var i = 0 ; i< data.length ; i++){
			if(i < data.length - 1){
				ids += data[i].id + ',';
			}else{
				ids += data[i].id;
			}
		}
		var beforeExpandUrl = '/platform/user/bpmSelectUserAction/dept?';
		var treeUrl =  beforeExpandUrl + 'nodeType=root&id=&para=' + ids;
		var object = $('#' + objectId);
		createComponent.createTreeComponent(objectId,null,treeUrl,beforeExpandUrl,tabNo,'dept');
		//绘制搜索input
		createComponent.createSearchInputComponent(objectId,'dept',ids,branchNo);
	};
	/**
	 * 绘制用户标签内容
	 */
	this.drawUserTabContent = function(data,objectId,tabNo){
		var object = $('#' + objectId);
		object.datagrid({
			fit: true,
			fitColumns: true,
			nowrap:false,
			idField:'userId',
			rownumbers:true,
			frozenColumns:[[
	            {field:'ck',checkbox:true}
			]],
			columns:[[
				{field:'id',title:'用户ID',width:120,align:'center',hidden : true },
				{field:'name',title:'姓名',width:120,align:'left'},
				{field:'deptName',title:'部门',width:120,align:'left'},
				{field:'deptId',title:'部门ID',width:120,align:'center',hidden : true},
			]],
			onCheck : function(rowIndex,rowData){
				if(getRecordIndexInTargetDataGrid(rowData.id,branchNo) == -1){//目标dataGrid 重复判断
	    			var record = {
	    		    		userId : rowData.id,
	    		    		userName : rowData.name,
	    		    		deptId : rowData.deptId,
	    		    		deptName : rowData.deptName
	    				};
	    			processSelectUserComponentEvent.eventAppendToSelectorTargetDataGrid(branchNo,record);
//	    			$('#selectorUserTabForTargetDataGrid_' + branchNo).datagrid('appendRow',record);	    		
	    		}
			},
			onUncheck : function(rowIndex,rowData){
				var index = getRecordIndexInTargetDataGrid(rowData.id,branchNo);
				$('#selectorUserTabForTargetDataGrid_' + branchNo).datagrid('deleteRow', index);
			},
			onSelectAll : function(rows){
				if(rows.length > 0){
					for(var i = 0 ; i < rows.length; i++){
						var record = {
		    		    		userId : rows[i].id,
		    		    		userName : rows[i].name,
		    		    		deptId : rows[i].deptId,
		    		    		deptName : rows[i].deptName
		    			};
						processSelectUserComponentEvent.eventAppendToSelectorTargetDataGrid(branchNo,record);
					}
				}
			},
			onUnselectAll : function(rows){
				if(rows.length > 0){
					for(var i = 0 ; i < rows.length; i++){
						var index = getRecordIndexInTargetDataGrid(rows[i].id,branchNo);
						$('#selectorUserTabForTargetDataGrid_' + branchNo).datagrid('deleteRow', index);
					}
				}
			}
		});
		//为用户 dataGrid追加数据
		for(var i = 0 ; i< data.length ; i++){
			object.datagrid('appendRow',{
				id : data[i].id,
				name : data[i].name,
				deptName : data[i].deptName,
				deptId : data[i].deptId
			});
		}
	};
	/**
	 * 绘制角色标签页
	 */
	this.drawRoleTabContent = function(data,objectId,tabNo){
		var datas = [];
		var ids = '';
		for(var i = 0 ; i< data.length ; i++){
			var jsonData = {
					id : data[i].id,
					text: data[i].name,
					state: 'closed',
					nodeType : 'role',
					iconCls : 'icon-role',
					attributes : {
						nodeType : 'role',
						expandFlag : ''
					}
			};
			datas[i] = jsonData;
			ids += data[i].id + ',';
		}
		var beforeExpandUrl = '/platform/user/bpmSelectUserAction/role?';
		createComponent.createTreeComponent(objectId,datas,'',beforeExpandUrl,tabNo,'role');
		//绘制搜索input
		createComponent.createSearchInputComponent(objectId,'role',ids,branchNo);
	};
	/**
	 * 绘制岗位标签页
	 */
	this.drawPositionTabContent = function(data,objectId,tabNo){
		var datas = [];
		var ids = '';
		for(var i = 0 ; i< data.length ; i++){
			var jsonData = {
					id : data[i].id,
					text: data[i].name,
					state: 'closed',
					nodeType : 'position',
					iconCls : 'icon-position',
					attributes : {
						nodeType : 'position',
						expandFlag : ''
					}
			};
			datas[i] = jsonData;
			ids += data[i].id + ',';
		}
		var beforeExpandUrl = '/platform/user/bpmSelectUserAction/position?';
		createComponent.createTreeComponent(objectId,datas,'',beforeExpandUrl,tabNo,'position');
		//绘制搜索input
		createComponent.createSearchInputComponent(objectId,'position' , ids,branchNo);
	};
	/**
	 * 绘制群组标签内容
	 */
	this.drawGroupTabContent = function(data,objectId,tabNo){
		var datas = [];
		var ids = '';
		for(var i = 0 ; i< data.length ; i++){
			var jsonData = {
					id : data[i].id,
					text: data[i].name,
					state: 'closed',
					nodeType : 'group',
					iconCls : 'icon-group',
					attributes : {
						nodeType : 'group',
						expandFlag : ''
					}
			};
			datas[i] = jsonData;
			ids += data[i].id + ',';
		}
		var beforeExpandUrl = '/platform/user/bpmSelectUserAction/group?';
		createComponent.createTreeComponent(objectId,datas,'',beforeExpandUrl,tabNo,'group');
		//绘制搜索input
		createComponent.createSearchInputComponent(objectId,'group',ids,branchNo);
	};
}
/**
 * 同步树及表格下所有选中的数据为uncheck状态
 * @param userIds Array
 * @param branchNo 分支序号
 */
function unCheckedOfSelectorData(userIds,branchNo){
	//1,先获取tab页
	//2,获取tab页的ui元素
	//3,判断ui元素的类型
	//4,获取ui元素的选中的数据
	//5,使该数据为uncheck状态
	var panels = $('.panel .panel-body');
	for(var i = 0 ; i < panels.length ; i++){
		if(panels[i].id.indexOf('tabs') != -1){
			var subPanels = $('#' + panels[i].id + ' div');
			for(var j = 0 ; j < subPanels.length ; j++){
				if(typeof(subPanels[j].id) != 'undefined' && subPanels[j].id != ''){
					if($('#' + subPanels[j].id).attr('branchNo') == branchNo){
						var checkedDatas;
						try{
							if((subPanels[j].id).indexOf('Tree')!=-1){
								// tree 操作
								checkedDatas = $('#' + subPanels[j].id).tree('getChecked');
								if (checkedDatas.length > 0){
									for(var k = 0 ; k < checkedDatas.length ; k++){
										for(var m = 0 ; m < userIds.length;m++){
											if(checkedDatas[k].id == userIds[m]){
												$('#' + subPanels[j].id).tree('uncheck', checkedDatas[k].target);
											}
										}
									}
								}
							}
							if((subPanels[j].id).indexOf('Grid')!=-1){
								// dataGrid 操作
								checkedDatas = $('#' + subPanels[j].id).datagrid('getChecked');
								if (checkedDatas.length > 0){
									for(var k = 0 ; k < checkedDatas.length ; k++){
										for(var m = 0 ; m < userIds.length;m++){
											if(checkedDatas[k].id == userIds[m]){
												var index = $('#' + subPanels[j].id).datagrid('getRowIndex', checkedDatas[k]);
												$('#' + subPanels[j].id).datagrid('uncheckRow', index);
											}
										}
									}
								}
							}
						}catch(e){}
					}
				}
			}
		}
	}
}

/**
 * 循环调用,展开树的节点
 */
function expandAndCheckTreeNode(treeId,node){
	$('#' + treeId).tree('expand',node.target);
	$('#' + treeId).tree('check',node.target);
}

function expandAllAndCheckTreeNode(treeId,node){
	$('#' + treeId).tree('expandAll',node.target);
	$('#' + treeId).tree('check',node.target);
}
/**
 * 绘制第二层
 */
function drawSecondLayerTabs(data,tabs,branchNo){
	var drawer = new Drawer(branchNo);
	var tabNo = 0;
	if(data.deptList != null && (data.deptList.length > 0 || data.orgList.length > 0)){//绘制部门标签页
		drawer.drawTab(tabs,'tabs_departmentTree_' + branchNo , '部门' ,'departmentTree_' + branchNo , 'icon-department');
		if(data.orgList.length > 0){
			drawer.drawOrgTabContent(data.orgList,'departmentTree_' + branchNo,tabNo++);//绘制部门标签页内容
		} else if(data.deptList.length > 0){
			drawer.drawDepartmentTabContent(data.deptList,'departmentTree_' + branchNo,branchNo);//绘制部门标签页内容
		}
		
	}
	if(data.userList != null && data.userList.length > 0){//绘制用户标签页
		drawer.drawTab(tabs,'tabs_userDataGrid_' + branchNo , '用户' ,'userDataGrid_' + branchNo , 'icon-users');
		drawer.drawUserTabContent(data.userList,'userDataGrid_' + branchNo,tabNo++);//绘制用户标签页内容
//		loadDataForTargetDataGrid_UserList(branchNo,data.userList);//如果是自动选人方式,加载用户dataGrid数据到target DataGrid
	}
	if(data.roleList != null && data.roleList.length > 0){//绘制角色标签页
		drawer.drawTab(tabs,'tabs_roleTree_' + branchNo , '角色' ,'roleTree_' + branchNo , 'icon-role');
		drawer.drawRoleTabContent(data.roleList,'roleTree_' + branchNo,tabNo++);//绘制角色标签页内容
	}
	if(data.positionList != null && data.positionList.length > 0){//绘制岗位标签页
		drawer.drawTab(tabs,'tabs_positionTree_' + branchNo , '岗位' ,'positionTree_' + branchNo , 'icon-position');
		drawer.drawPositionTabContent(data.positionList,'positionTree_' + branchNo,tabNo++);//绘制角色标签页内容
	}
	if(data.groupList != null && data.groupList.length > 0){//绘制群组标签页
		drawer.drawTab(tabs,'tabs_groupTree_' + branchNo , '群组' ,'groupTree_' + branchNo , 'icon-group');
		drawer.drawGroupTabContent(data.groupList,'groupTree_' + branchNo,tabNo++);//绘制群组标签页内容
	}
	//选中第一标签页
	tabs.tabs('select',0);
}
/**
 * 绘制目标选人dataGird
 */
function drawSecondLayerTabsForTargetDataGrid(objId,branchNo){
	$('#' + objId).datagrid({
		fit: true,
		fitColumns: true,
		remoteSort: false,
		nowrap:false,
		idField:'userId',
		rownumbers:true,
		toolbar:[{
			id:'btnadd',
			text:'移除全部',
			iconCls:'icon-users-remove',
			handler:function(){
				var datas = $('#' + objId).datagrid('getData');
				if (datas.rows.length > 0){
					var userIds = new Array();
					for(var i = 0 ; i < datas.rows.length ; i++){
						//同步树及表格下所有选中的数据为uncheck状态
						userIds.push(datas.rows[i].userId);
					}
					unCheckedOfSelectorData(userIds,branchNo);
				}
				
				var data = {
						total : 0,
						rows : new Array()
				};
				$('#' + objId).datagrid('loadData',data);
			} 
		}],
		columns:[[
			{field:'userId',title:'用户ID',width:120,align:'left' ,hidden : true},
			{field:'userName',title:'用户名',width:150,align:'left',sortable:true},
			{field:'deptId',title:'部门ID',width:120,align:'left',hidden : true},
			{field:'deptName',title:'部门',width:150,align:'left'},
			{field:'selectedType',title:'当前选中的是用户还是部门',width:120,align:'left',hidden : true},
			{field:'removeOp',title:'',width:32,align:'center',sortable:'true',formatter:function(value,row,index){
				var e =  "";
				var d = "<span class=\"user-remove\" onclick=\"removeOp('" + objId + "','" + row.userId + "'," + branchNo + ");return false;\">&nbsp;&nbsp;&nbsp;&nbsp;</span>";
				return e+d;
			}},
			{field:'orderIndex',title:'排序值',width:20,align:'left',hidden : true},
			{field:'order',title:'',width:60,align:'center',sortable:'true',formatter:function(value,row,index){
				var e =  "";
				var d = "";
				if(value == -1){//first
					d = "<span class=\"user-order-desc\" onclick=\"orderDownOp(" + value + "," + index + "," + branchNo + ")\">&nbsp;&nbsp;&nbsp;&nbsp;</span>";
				} else if(value == 0){// middle
					d = "<span class=\"user-order-asc\" onclick=\"orderUpOp(" + value + "," + index + "," + branchNo + ")\">&nbsp;&nbsp;&nbsp;&nbsp;</span><span class=\"user-order-desc\" onclick=\"orderDownOp(" + value + "," + index + "," + branchNo + ")\">&nbsp;&nbsp;&nbsp;&nbsp;</span>";
				} else if(value == 1){//last
					d = "<span class=\"user-order-asc\" onclick=\"orderUpOp(" + value + "," + index + "," + branchNo + ")\">&nbsp;&nbsp;&nbsp;&nbsp;</span>";
				}
				return e+d;
			}}
		]]
	});
}
/**
 * 向上交换排序
 * @param value
 * @param index
 * @param branchNo
 */
function orderUpOp(value,index,branchNo){
	var datas = $('#selectorUserTabForTargetDataGrid_' + branchNo).datagrid('getData');
	var changeData = datas.rows[index];
	datas.rows[index] = datas.rows[index - 1];
	datas.rows[index - 1] = changeData;
	datas = reloadDataForDataGridOrderType(datas);
	$('#selectorUserTabForTargetDataGrid_' + branchNo).datagrid('loadData',datas);
	return;
}
/**
 * 向下交换排序
 * @param value
 * @param index
 * @param branchNo
 */
function orderDownOp(value,index,branchNo){
	var datas = $('#selectorUserTabForTargetDataGrid_' + branchNo).datagrid('getData');
	var changeData = datas.rows[index];
	datas.rows[index] = datas.rows[index + 1];
	datas.rows[index + 1] = changeData;
	datas = reloadDataForDataGridOrderType(datas);
	$('#selectorUserTabForTargetDataGrid_' + branchNo).datagrid('loadData',datas);
	return;
}
/**
 * 移除操作
 * @param objId
 * @param userId
 * @param branchNo
 * @returns {Boolean}
 */
function removeOp(objId,userId,branchNo){
	var index = getRecordIndexInTargetDataGrid(userId,branchNo);
	//$('#' + objId).datagrid('deleteRow', index);
	var userIds = new Array();
	userIds.push(userId);
	unCheckedOfSelectorData(userIds,branchNo);
	var datas = reloadDataForDataGridOrderType($('#selectorUserTabForTargetDataGrid_' + branchNo).datagrid('getData'));
	$('#selectorUserTabForTargetDataGrid_' + branchNo).datagrid('loadData',datas);
	return false;
}
function reloadDataForDataGridOrderType(datas){
	if(datas.rows.length == 1){
		datas.rows[0].order = 2;
	} else {
		for(var i = 0 ; i < datas.rows.length ; i++){
			if(i == 0){
				datas.rows[i].order = -1;
			} else if(i == datas.rows.length - 1){
				datas.rows[i].order = 1;
			} else {
				datas.rows[i].order = 0;
			}
		}
	}
	return datas;
}
function eventOnFocus(obj){
    if(obj.value=="请输入查询内容..."){
    	obj.style.color="#000000";
    	obj.value="";
     }
 }
function EventOnBlur(obj){
    if(obj.value==""){
    	obj.style.color="";
    	obj.value="请输入查询内容...";
    }
}
/**
 * search event
 * @param obj
 * @param userId
 * @param userName
 * @param deptId
 * @param deptName
 */
function eventSearcherCheckBox(obj,userId,userName,deptId,deptName,branchNo){
	if(obj.checked){
		if(getRecordIndexInTargetDataGrid(userId,branchNo) == -1){
			var record = {
		    		userId : userId,
		    		userName : userName,
		    		deptId : deptId,
		    		deptName : deptName
				};
			processSelectUserComponentEvent.eventAppendToSelectorTargetDataGrid(branchNo,record);
		}
	}
	return;
}
/**
 * 获取数据记录在目标数据表格中的索引值
 */
function getRecordIndexInTargetDataGrid(userId,branchNo){
	var datas = $('#selectorUserTabForTargetDataGrid_' + branchNo).datagrid('getData');
	if(datas.total == 0){
		return -1;
	} else {
		for( var i = 0 ; i < datas.rows.length ; i++){
			if(userId == datas.rows[i].userId){
				return i;
			}
		}
	}
	return -1;
}
