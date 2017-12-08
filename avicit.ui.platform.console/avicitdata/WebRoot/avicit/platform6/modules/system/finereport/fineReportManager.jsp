<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>报表模版授权管理</title>
		<base href="<%=ViewUtil.getRequestPath(request) %>">
		<jsp:include page="/avicit/platform6/component/common/EasyUIJsInclude.jsp"></jsp:include>
		<jsp:include page="/avicit/platform6/modules/system/commonpopup/commonSelectionHead.jsp"></jsp:include>
		<script src="avicit/platform6/modules/system/finereport/js/center.js" type="text/javascript"></script>
		<script src="avicit/platform6/modules/system/finereport/js/menutree.js" type="text/javascript"></script>
		<script type="text/javascript" charset="utf-8">
		var currentReportId="none";
		var curComId="none";
		var currentReportTreeId="none";
		var comDatagrid;
		var currTabIndex=0;
		var TARGET_TYPE = "R"; // 目标类型； 角色(R)，用户(U)，部门(D)，群组(G)，岗位(P)。
		window.TARGET_ID = ""; // 目标ID
		$(function(){
			initMemuSearch();
			initTabContainer();
		});
		
		function myOnBeforeExpand(row) {
			$("#memuTree").tree("options").url = "platform/sysPermissionTreeController/listMemuTreeById.json?id=" + row.id;
			return true;
		}
		function clickTreeRow(row){
			var urlHead = "ReportServer?formlet=";
			currentReportTreeId = row.id
			if (row.attributes.value.indexOf(".cpt")!=-1){
				urlHead = "ReportServer?reportlet=";
			}
			currentReportId = urlHead + encodeURIComponent(row.attributes.path);
			reloadTabData(currTabIndex);
		}
		

		function reloadTabData(index){
			if(index==0){
				loadRole(currentReportId);
			}else if(index==1){
				loadUser(currentReportId);
			}else if(index==2){
				loadDept(currentReportId);
			}else if(index==3){
				loadGroup(currentReportId);
			}else if(index==4){
				loadPosition(currentReportId);
			}
		} 
		
		/**
		 *菜单查询
		 **/
		function initMemuSearch() {
			$('#searchWord').searchbox({
				width : 200,
				searcher : function(value) {
					$("#reportTree").tree("search", value);
					currentReportId = "none";
					reloadTabData(currTabIndex);
				},
				prompt : "请输入菜单名称！"
			});
		}
		
		(function($) {    
		      
		    $.extend($.fn.tree.methods, {  
		        /**  
		         * 扩展easyui tree的搜索方法  
		         * @param tree easyui tree的根DOM节点(UL节点)的jQuery对象  
		         * @param searchText 检索的文本  
		         * @param this-context easyui tree的tree对象  
		         */  
		        search: function(jqTree, searchText) {  
		            //easyui tree的tree对象。可以通过tree.methodName(jqTree)方式调用easyui tree的方法  
		            var tree = this;  
		              
		            //获取所有的树节点  
		            var nodeList = getAllNodes(jqTree, tree);  
		              
		            //如果没有搜索条件，则展示所有树节点  
		            searchText = $.trim(searchText);  
		            if (searchText == "") {  
		                for (var i=0; i<nodeList.length; i++) {   
		                    $(nodeList[i].target).show();  
		                }  
		                //展开已选择的节点（如果之前选择了）  
		                var selectedNode = tree.getSelected(jqTree);  
		                if (selectedNode) {  
		                    tree.expandTo(jqTree, selectedNode.target);  
		                }  
		                return;  
		            }  
		              
		            //搜索匹配的节点
		            var matchedNodeList = [];  
		            if (nodeList && nodeList.length>0) {  
		                var node = null;  
		                for (var i=0; i<nodeList.length; i++) {  
		                    node = nodeList[i];  
		                    if (isMatch(searchText, node.text)) { 
		                        matchedNodeList.push(node);  
		                    }  
		                }  
		                  
		                //隐藏所有节点  
		                for (var i=0; i<nodeList.length; i++) {   
		                    $(nodeList[i].target).hide();  
		                }             
		                  
		                //折叠所有节点  
		                tree.collapseAll(jqTree);  
		                  
		                //展示所有匹配的节点以及父节点              
		                for (var i=0; i<matchedNodeList.length; i++) {  
		                    showMatchedNode(jqTree, tree, matchedNodeList[i]);  
		                }  
		            }      
		        },  
		          
		        /**  
		         * 展示节点的子节点（子节点有可能在搜索的过程中被隐藏了）  
		         * @param node easyui tree节点  
		         */  
		        showChildren: function(jqTree, node) {  
		            //easyui tree的tree对象。可以通过tree.methodName(jqTree)方式调用easyui tree的方法  
		            var tree = this;  
		              
		            //展示子节点  
		            if (!tree.isLeaf(jqTree, node.target)) {  
		                var children = tree.getChildren(jqTree, node.target);  
		                if (children && children.length>0) {  
		                    for (var i=0; i<children.length; i++) {  
		                        if ($(children[i].target).is(":hidden")) {  
		                            $(children[i].target).show();  
		                        }  
		                    }  
		                }  
		            }     
		        },  
		          
		        /**  
		         * 将滚动条滚动到指定的节点位置，使该节点可见（如果有滚动条才滚动，没有滚动条就不滚动）  
		         * @param param {  
		         *    treeContainer: easyui tree的容器（即存在滚动条的树容器）。如果为null，则取easyui tree的根UL节点的父节点。  
		         *    targetNode:  将要滚动到的easyui tree节点。如果targetNode为空，则默认滚动到当前已选中的节点，如果没有选中的节点，则不滚动  
		         * }   
		         */  
		        scrollTo: function(jqTree, param) {  
		            //easyui tree的tree对象。可以通过tree.methodName(jqTree)方式调用easyui tree的方法  
		            var tree = this;  
		              
		            //如果node为空，则获取当前选中的node  
		            var targetNode = param && param.targetNode ? param.targetNode : tree.getSelected(jqTree);  
		              
		            if (targetNode != null) {  
		                //判断节点是否在可视区域                 
		                var root = tree.getRoot(jqTree);  
		                var $targetNode = $(targetNode.target);  
		                var container = param && param.treeContainer ? param.treeContainer : jqTree.parent();  
		                var containerH = container.height();  
		                var nodeOffsetHeight = $targetNode.offset().top - container.offset().top;  
		                if (nodeOffsetHeight > (containerH - 30)) {  
		                    var scrollHeight = container.scrollTop() + nodeOffsetHeight - containerH + 30;  
		                    container.scrollTop(scrollHeight);  
		                }                             
		            }  
		        }  
		    });  
		      
		      
		      
		      
		    /**  
		     * 展示搜索匹配的节点  
		     */  
		    function showMatchedNode(jqTree, tree, node) {  
		        //展示所有父节点  
		        $(node.target).show();  
		        var pNode = node;  
		        while ((pNode = tree.getParent(jqTree, pNode.target))) {  
		            $(pNode.target).show();               
		        }  
		        //展开到该节点  
		        tree.expandTo(jqTree, node.target);  
		        //如果是非叶子节点，需折叠该节点的所有子节点  
		        if (!tree.isLeaf(jqTree, node.target)) {  
		            tree.collapse(jqTree, node.target);  
		        }  
		    }      
		      
		    /**  
		     * 判断searchText是否与targetText匹配  
		     * @param searchText 检索的文本  
		     * @param targetText 目标文本  
		     * @return true-检索的文本与目标文本匹配；否则为false.  
		     */  
		    function isMatch(searchText, targetText) {  
		        return $.trim(targetText)!="" && targetText.indexOf(searchText)!=-1 && targetText.indexOf(".")!=-1;  
		    }  
		      
		    /**  
		     * 获取easyui tree的所有node节点  
		     */  
		    function getAllNodes(jqTree, tree) {  
		        var allNodeList = jqTree.data("allNodeList");  
		        if (!allNodeList) {  
		            var roots = tree.getRoots(jqTree);  
		            allNodeList = getChildNodeList(jqTree, tree, roots);  
		            jqTree.data("allNodeList", allNodeList);  
		        }  
		        return allNodeList;  
		    }  
		      
		    /**  
		     * 定义获取easyui tree的子节点的递归算法  
		     */  
		    function getChildNodeList(jqTree, tree, nodes) {  
		        var childNodeList = [];  
		        if (nodes && nodes.length>0) {             
		            var node = null;  
		            for (var i=0; i<nodes.length; i++) {  
		                node = nodes[i];  
		                childNodeList.push(node);  
		                if (!tree.isLeaf(jqTree, node.target)) {  
		                    var children = tree.getChildren(jqTree, node.target);  
		                    childNodeList = childNodeList.concat(getChildNodeList(jqTree, tree, children));  
		                }  
		            }  
		        }  
		        return childNodeList;  
		    }  
		})(jQuery);  
		
		</script>
	</head>
	
	<body class="easyui-layout" fit="true">
		<div data-options="region:'west',title:'报表模版授权信息',split:true,iconCls:'icon-save'" style="width:400px;background:#f5fafe;overflow-y:hidden;">
				<%@ include file ="fineReportTree.jsp"%>
		</div> 
	
		<div data-options="region:'center',iconCls:'icon-search',title:'权限设置'" style="background:#ffffff; height:0; overflow:hidden; font-size:0;">
			 <%@ include file ="center.jsp"%>
		</div>
		
	
	</body>
</html>