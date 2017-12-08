<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<%
	String path = request.getContextPath();

String basePath = ViewUtil.getRequestPath(request);
%>  
<html>
<head>

	<title>组织机构图</title>

	<!-- 设置mxGraph的基础路径，其对应的css、images都需要被放置在此路径中 -->
	<script type="text/javascript">
		mxBasePath = '<%=path%>/avicit/platform6/bpm';
	</script>

	<!-- 加载并初始化mxGraph -->
	<script type="text/javascript" src="<%=path%>/avicit/platform6/bpm/js/mxClient.js"></script>

	<script type="text/javascript">
		/*
			为树节点创建一种自定义的图形（包括出去的上半部分）,图形如下：
			_____
		   |     |
		   -------
		      |
		*/
		function TreeNodeShape() { };

		TreeNodeShape.prototype = new mxCylinder();
		TreeNodeShape.prototype.constructor = TreeNodeShape;

		// 定义“树节点”伸出部分的长度
		TreeNodeShape.prototype.segment = 20;

		// 渲染时需要获取图形节点的状态（state）
		TreeNodeShape.prototype.apply = function(state)
		{
			mxCylinder.prototype.apply.apply(this, arguments);
			this.state = state;
		};
		
		TreeNodeShape.prototype.redrawPath = function(path, x, y, w, h, isForeground)
		{
                        //获得当前graph对象，通过view视图来获取
			var graph = this.state.view.graph;
                        //判断当前节点是否还有子节点
			var hasChildren = graph.model.getOutgoingEdges(this.state.cell).length > 0;
			
			if (isForeground)
			{
				if (hasChildren)
				{
					// Painting outside of vertex bounds is used here
					path.moveTo(w / 2, h + this.segment);
					path.lineTo(w / 2, h);   //在节点外部画一段延长线
					path.end();
				}	
			}
			else
			{
				path.moveTo(0, 0);
				path.lineTo(w, 0);
				path.lineTo(w, h);
				path.lineTo(0, h);
				path.close();
			}
		};
		
		mxCellRenderer.prototype.defaultShapes['treenode'] = TreeNodeShape;

		// 为树节点定义一种自定义边缘
		mxGraphView.prototype.updateFloatingTerminalPoint = function(edge, start, end, source)
		{
			var pt = null;
			
			if (source){
				pt = new mxPoint(start.x + start.width / 2,
						start.y + start.height + TreeNodeShape.prototype.segment);
			}else{
				pt = new mxPoint(start.x + start.width / 2, start.y);
			}

			edge.setAbsoluteTerminalPoint(pt, source);
		};
	</script>

	<!-- 画图代码 -->
	<script type="text/javascript">
		// 程序入口，本函数将在body的onload事件中被调用
		function main()
		{
			// 检测当前浏览器是否被mxGraph支持
			if (!mxClient.isBrowserSupported())
			{
				// 如果当前浏览器不支持，则显示错误信息
				mxUtils.error('Browser is not supported!', 200, false);
			}
			else
			{
				// 设置折叠和展开图标
				mxGraph.prototype.collapsedImage = new mxImage(mxClient.imageBasePath + '/collapsed.gif', 9, 9);
				mxGraph.prototype.expandedImage = new mxImage(mxClient.imageBasePath + '/expanded.gif', 9, 9);
				
				// Internet Explorer忽略了某些样式的解决方法
				var container = document.createElement('div');
				container.style.position = 'absolute';
				container.style.overflow = 'hidden';
				container.style.left = '0px';
				container.style.top = '0px';
				container.style.right = '0px';
				container.style.bottom = '0px';
				
				if (mxClient.IS_IE)
				{
					new mxDivResizer(container);
				}
				
				document.body.appendChild(container);
			
				// 在指定的容器中创建graph对象
				var graph = new mxGraph(container);

				// 指定显示的风格样式
				var style = graph.getStylesheet().getDefaultVertexStyle();
				style[mxConstants.STYLE_SHAPE] = 'treenode';
				style[mxConstants.STYLE_GRADIENTCOLOR] = 'white';
				style[mxConstants.STYLE_FONTSTYLE] = 1;
				style[mxConstants.STYLE_FILLCOLOR] = '#F9C735';
				style[mxConstants.STYLE_SHADOW] = true;
				
				style = graph.getStylesheet().getDefaultEdgeStyle();
				style[mxConstants.STYLE_EDGE] = mxEdgeStyle.TopToBottom;
				style[mxConstants.STYLE_ROUNDED] = true;
				
				
				style = [];
				//style[mxConstants.STYLE_SHAPE] = mxConstants.SHAPE_IMAGE;   //合并节点采用图
				style[mxConstants.STYLE_FONTSIZE] = '11';
				style[mxConstants.STYLE_FONTSTYLE] = 0;
				style[mxConstants.STYLE_FONTCOLOR] = 'black';
				style[mxConstants.STYLE_STROKECOLOR] = '#03689A';
				style[mxConstants.STYLE_FILLCOLOR] = '#B9D4D7';
				style[mxConstants.STYLE_GRADIENTCOLOR] = 'white';
				//style[mxConstants.STYLE_IMAGE]='<%=path%>/avicit/platform6/modules/system/sysorg/org.gif';
				style[mxConstants.STYLE_GRADIENT_DIRECTION] = mxConstants.DIRECTION_EAST;
				style[mxConstants.STYLE_ROUNDED] = true;
				style[mxConstants.STYLE_SHADOW] = true;
				graph.getStylesheet().putCellStyle('dept', style);
				
				// Enables automatic sizing for vertices after editing and
				// panning by using the left mouse button.
				graph.setAutoSizeCells(true);
				graph.setPanning(true);
				graph.panningHandler.useLeftButtonForPanning = true;

				// Stops editing on enter or escape keypress
				var keyHandler = new mxKeyHandler(graph);
				
				// Enables automatic layout on the graph and installs
				// a tree layout for all groups who's children are
				// being changed, added or removed.
				var layout = new mxCompactTreeLayout(graph, false);
				layout.useBoundingBox = false;
				layout.edgeRouting = false;
				layout.levelDistance = 30;
				layout.nodeDistance = 10;

				var layoutMgr = new mxLayoutManager(graph);
				
				layoutMgr.getLayout = function(cell)
				{
					if (cell.getChildCount() > 0)
					{
						return layout;
					}
				};

				// 图标不允许被选择
				graph.setCellsSelectable(false);

				// 定义显示折叠图标的条件
				graph.isCellFoldable = function(cell)
				{
					return this.model.getOutgoingEdges(cell).length > 0;
				};

				// 定义了折叠图标的位置
				graph.cellRenderer.getControlBounds = function(state)
				{
					if (state.control != null)
					{
						var oldScale = state.control.scale;
						var w = state.control.bounds.width / oldScale;
						var h = state.control.bounds.height / oldScale;
						var s = state.view.scale;			

						return new mxRectangle(state.x + state.width / 2 - w / 2 * s,
							state.y + state.height + TreeNodeShape.prototype.segment * s - h / 2 * s,
							w * s, h * s);
					}
					
					return null;
				};

				// 折叠图标上的点击事件
				graph.foldCells = function(collapse, recurse, cells)
				{
					this.model.beginUpdate();
					try
					{
						toggleSubtree(this, cells[0], !collapse);
						this.model.setCollapsed(cells[0], collapse);

						// Executes the layout for the new graph since
						// changes to visiblity and collapsed state do
						// not trigger a layout in the current manager.
						layout.execute(graph.getDefaultParent());
					}
					finally
					{
						this.model.endUpdate();
					}
				};
				
				// Gets the default parent for inserting new cells. This
				// is normally the first child of the root (ie. layer 0).
				var parent = graph.getDefaultParent();
								
				// 添加树的根顶点
				graph.getModel().beginUpdate();
				try
				{
					${modelContext}
				}
				finally
				{
					// 刷新显示
					graph.getModel().endUpdate();
				}
			}
		};

		// Updates the visible state of a given subtree taking into
		// account the collapsed state of the traversed branches
		//更新指定子树的可视化状态
		function toggleSubtree(graph, cell, show)
		{
			show = (show != null) ? show : true;
			var cells = [];
			
			graph.traverse(cell, true, function(vertex)
			{
				if (vertex != cell)
				{
					cells.push(vertex);
				}

				// 当折叠节点被看到时停止递归 
				return vertex == cell || !graph.isCellCollapsed(vertex);
			});

			graph.toggleCells(show, cells, true);
		};
	</script>
</head>

<!-- Calls the main function after the page has loaded. Container is dynamically created. -->
<body onload="main();">
</body>
</html>
	