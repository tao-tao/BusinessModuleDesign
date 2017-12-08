var $_module = function(){
	return{
		getCommands: function(){
			return {name : 'auto',
			    	text : '自定义按钮条',
			    	iconCls : 'icon-plus',
					xtype : 'button'
					,replace_dt:'$_module.getCommands()'
					};
		},
		getQueryModel: function(){
			return {header:'自定义列模型',
					dataIndex:'ID', 
					width : 60
					,replace_dt:'$_module.getQueryModel()'
					};
		},
		getRowsPerPage: function(){
			return 10;
		},
		getRowActionFunc: function(){
			return $_module.getRowActionFunc;
		},
		getQueryFields: function(){
			return {
				xtype : 'textfield',
				name : 'name',
				fieldLabel : '自定义搜索条件'
				,replace_dt:'$_module.getQueryFields()'
			};
		},
		getRowAction: function(){
			return {
				 iconCls:'icon-edit-record'
				,qtip:'自定义Action'
				,replace_dt:'$_module.getRowAction()'
			};
		}
	}
}();

function QueryString() {
	var name, value, i;
	var str = location.href;
	var num = str.indexOf(":");
	if(num>=0)
	{
		this['schema'] = str.substr(0,num);
	}	
	num = str.indexOf("?")
	if(num>=0)
	{
		var parastr = str.substr(num + 1);
		str = str.substring(0,num);
		var arrtmp = parastr.split("&");
		for (i = 0; i < arrtmp.length; i++) {
			num = arrtmp[i].indexOf("=");
			if (num > 0) {
				name = arrtmp[i].substring(0, num);
				value = arrtmp[i].substr(num + 1);
				this[name] = value;
			}
		}
	}
	num = str.indexOf('/FormDesigner');
	if(num>=0)
		this['context'] = str.substr(0,num);
	
}
var Request = new QueryString();
var path = Request['path'];

Ext.form.SimpleCombo = Ext.extend(Ext.form.ComboBox, {
    mode           : 'local',
    triggerAction  : 'all',
    typeAhead      : true,
    valueField     : 'value',
    displayField   : 'name',
    forceSelection : true,
    editable       : true,
		minChars       : 0,
    initComponent  : function(){
        Ext.form.ComboBox.superclass.initComponent.call(this);
        if(!this.store && this.data){
            this.store = new Ext.data.SimpleStore({
                fields: ['value','name','cls'],
                data : this.data
            });
        }
		this.tpl = '<tpl for="."><div class="x-combo-list-item {cls}">{' + this.displayField + '}</div></tpl>';
    }

});
Ext.reg('simplecombo', Ext.form.SimpleCombo);

Main = function() {
};

Main.prototype = {

	init : function() {

		Ext.QuickTips.init();

		this.idCounter = 0;
		this.autoUpdate = true;

		this.cookies = new Ext.state.CookieProvider();

		this.initResizeLayer();
		this.initUndoHistory();
		this.initTreePanel();
		this.initEditPanel();
		this.initComponentsPanel();

		this.viewport = new Ext.Viewport({
			layout : 'border',
			items : [{
				region : 'north',
				// title : '表单编辑器',
				height : 28,
				tbar : [ {
							text : '打开',
							iconCls : 'icon-reset',
							scope : this,
							handler : function() {
								this.openFile();
							}
						}, {
							text : '保存',
							iconCls : 'icon-reset',
							scope : this,
							handler : function() {
								this.saveFile();
							}
						}, {
							text : '编辑源码',
							iconCls : 'icon-editEl',
							scope : this,
							handler : this.editConfig
						}, {
							text : '编辑文本',
							iconCls : 'icon-editEl',
							scope : this,
							handler : this.editText
						}, '-', {
							text : '清空',
							iconCls : 'icon-reset',
							scope : this,
							handler : function() {
								this.markUndo("Reset All");
								this.resetAll();
							}
						},  {
							iconCls : 'icon-update',
							text : '更新',
							tooltip : 'Apply changes',
							scope : this,
							handler : function() {
								this.updateForm(true);
							}
						}, '-', {
							iconCls : 'icon-time',
							text : '显示时间 : <i>unknown</i>',
							scope : this,
							id : 'FBRenderingTimeBtn',
							tooltip : 'Click to update form and display rendering time',
							handler : function() {
								this.updateForm(true);
							}
						}, '-', {
							id : 'FBUndoBtn',
							iconCls : 'icon-undo',
							text : '恢复',
							disabled : true,
							tooltip : "Undo last change",
							handler : this.undo,
							scope : this
						}, '->', {
							text : '工具栏',
							iconCls : 'icon-editEl',
							scope : this,
							handler : this.showTool
						}, {
							text : '帮助',
							iconCls : 'icon-help',
							handler : function() {
								Ext.Msg.alert('信息',
										"<b>本编辑器属于光大平台的一部分");
							}
						}]
			}, {
				region : 'center',
				layout : 'fit',
				border : false,
				bodyBorder : false,
				style : 'padding:3px 5px;',
				items : {
					border : false,
					bodyBorder : false,
					bodyStyle : 'background:#dddddd;border:dashed green 1px;',
					layout : 'fit',
					id : 'FBBuilderPanel',
					autoScroll: true
				}
			}, {
				region : 'east',
				border : false,
				title : '属性',
				width : 200,
				split : true,
				xtype : 'panel',
				layout : 'border',
				collapsible : true,
//				collapsed : true,
				items : [this.treePanel, this.editPanel]
			}]
		});
		this.builderPanel = Ext.ComponentMgr.get('FBBuilderPanel');
		var root = this.treePanel.root;
		root.fEl = this.builderPanel;
		root.elConfig = this.builderPanel.initialConfig;
		this.builderPanel._node = root;

		var drop = new Ext.dd.DropZone(main.builderPanel.el, {
					ddGroup : 'component',
					notifyOver : function(src, e, data) {
						var node = main.getNodeForEl(e.getTarget());
						if (node) {
							data.node = node;
							node.select();
							this.highlightElement(node.fEl.el);
							if (this.canAppend({}, node) === true) {
								return true;
							} else {
								data.node = null;
								return false;
							}
						} else {
							data.node = null;
							return false;
						}
					}.createDelegate(this),
					notifyDrop : function(src, e, data) {
						if (!data.node || !data.compData) {
							return;
						}
						var c = data.compData.config;
						if (typeof c == 'function') {
							c.call(this, function(config) {
										var n = this.appendConfig(config,
												data.node, true, true);
									}, data.node.elConfig);
						} else {
							var n = this.appendConfig(this
											.cloneConfig(data.compData.config),
									data.node, true, true);
							
						}
						this.updateForm(true);
						if(n)
							this.setCurrentNode(n, true);
						return true;
					}.createDelegate(this),
					notifyOut : function(src, e, data) {
						data.node = null;
					}
				});
		main.builderPanel.drop = drop;

		this.treePanel.el.on('contextmenu', function(e) {
					e.preventDefault();
				});

		if (!this.loadConfig(path)) {
			this.resetAll();
		}

		// select elements on form with single click
		this.builderPanel.el.on('click', function(e, el) {
					e.preventDefault();
					var node = this.getNodeForEl(el);
					if (!node) {
						node = this.treePanel.root;
					}
					this.highlightElement(node.fEl.el);
					this.setCurrentNode(node, true);
				}, this);
		// menu on form elements
		this.builderPanel.el.on('contextmenu', function(e, el) {
					e.preventDefault();
					var node = this.getNodeForEl(el);
					if (!node) {
						return;
					}
					this.highlightElement(node.fEl.el);
					this.setCurrentNode(node, true);
					this.contextMenu.node = node;
					this.contextMenu.showAt(e.getXY());
				}, this);
	},

	// the tree panel, listing elements
	initTreePanel : function() {
		var tree = new Ext.tree.TreePanel({
					region : 'north',
					// title : "组件树",
					iconCls : "icon-el",
					collapsible : true,
					floatable : false,
					autoScroll : true,
					height : 200,
					split : true,
					animate : false,
					enableDD : true,
					ddGroup : 'component',
					containerScroll : true,
					selModel : new Ext.tree.DefaultSelectionModel(),
					notools : [{
								id : 'expandall',
								text : '展开全部',
								tooltip : 'Expand all elements',
								scope : this,
								handler : function() {
									this.treePanel.expandAll();
								}
							}, {
								id : 'collapseall',
								text : '折叠全部',
								tooltip : 'Collapse all elements',
								scope : this,
								handler : function() {
									this.treePanel.collapseAll();
								}
							}]
				});

		var root = new Ext.tree.TreeNode({
					text : '元素',
					id : this.getNewId(),
					draggable : false
				});
		tree.setRootNode(root);

		tree.on('click', function(node, e) {
					e.preventDefault();
					if (!node.fEl || !node.fEl.el) 
					{
						return;
					}
					this.highlightElement(node.fEl.el);
					this.setCurrentNode(node);
					this.visualResize(node);
					//window.node = node; // debug
				}, this);

		// clone a node
		var cloneNode = function(node) {
			var config = Ext.apply({}, node.elConfig);
			delete config.id;
			var newNode = new Ext.tree.TreeNode({
						id : this.getNewId(),
						text : this.configToText(config)
					});
			newNode.elConfig = config;

			// clone children
			for (var i = 0; i < node.childNodes.length; i++) {
				n = node.childNodes[i];
				if (n) {
					newNode.appendChild(cloneNode(n));
				}
			}

			return newNode;

		}.createDelegate(this);

		// assert node drop
		tree.on('nodedragover', function(de) {
					var p = de.point, t = de.target;
					if (p == "above" || t == "below") {
						t = t.parentNode;
					}
					if (!t) {
						return false;
					}
					this.highlightElement(t.fEl.el);
					return (this.canAppend({}, t) === true);
				}, this);

		// copy node on 'ctrl key' drop
		tree.on('beforenodedrop', function(de) {
					if (!de.rawEvent.ctrlKey) {
						this.markUndo("Moved " + de.dropNode.text);
						return true;
					}
					this.markUndo("Copied " + de.dropNode.text);
					var ns = de.dropNode, p = de.point, t = de.target;
					if (!(ns instanceof Array)) {
						ns = [ns];
					}
					var n;
					for (var i = 0, len = ns.length; i < len; i++) {
						n = cloneNode(ns[i]);
						if (p == "above") {
							t.parentNode.insertBefore(n, t);
						} else if (p == "below") {
							t.parentNode.insertBefore(n, t.nextSibling);
						} else {
							t.appendChild(n);
						}
					}
					n.ui.focus();
					if (de.tree.hlDrop) {
						n.ui.highlight();
					}
					t.ui.endDrop();
					de.tree.fireEvent("nodedrop", de);
					return false;
				}, this);

		// update on node drop
		tree.on('nodedrop', function(de) {
					var node = de.target;
					if (de.point != 'above' && de.point != 'below') {
						node = node.parentNode || node;
					}
					// this.updateForm(false, node);
					this.updateForm(true);
				}, this, {
					buffer : 100
				});

		// get first selected node
		tree.getSelectedNode = function() {
			return this.selModel.getSelectedNode();
		};

		// context menu to delete / duplicate...
		var contextMenu = new Ext.menu.Menu({
			items : [{
						text : 'Delete this element',
						iconCls : 'icon-deleteEl',
						scope : this,
						handler : function(item) {
							this.removeNode(contextMenu.node);
						}
					}, {
						text : 'Add new element as child',
						iconCls : 'icon-addEl',
						scope : this,
						handler : function(item) {
							var node = this.appendConfig({}, contextMenu.node,
									true, true);
							this.treePanel.expandPath(node.getPath());
							this.updateForm(true);
						}
					}, {
						text : 'Add new element under',
						iconCls : 'icon-addEl',
						scope : this,
						handler : function(item) {
							var node = this.appendConfig({},
									contextMenu.node.parentNode, true, true);
							this.treePanel.expandPath(node.getPath());
							this.updateForm(true);
						}
					}, {
						text : 'Duplicate this element',
						iconCls : 'icon-dupEl',
						scope : this,
						handler : function(item) {
							var node = contextMenu.node;
							this.markUndo("Duplicate " + node.text);
							var newNode = cloneNode(node);
							if (node.isLast()) {
								node.parentNode.appendChild(newNode);
							} else {
								node.parentNode.insertBefore(newNode,
										node.nextSibling);
							}
							// this.updateForm(false, node.parentNode);
							this.updateForm(true);
						}
					}, {
						text : 'Visual resize / move',
						tooltip : 'Visual resize the element.<br/>You can move it too if in an <b>absolute</b> layout',
						iconCls : 'icon-resize',
						scope : this,
						handler : function(item) {
							this.visualResize(contextMenu.node);
						}
					}]
		});
		tree.on('contextmenu', function(node, e) {
					e.preventDefault();
					if (node != this.treePanel.root) {
						contextMenu.node = node;
						contextMenu.showAt(e.getXY());
					}
				}, this);
		this.contextMenu = contextMenu;

		this.treePanel = tree;
		
//		this.initEditPanel();
		
//		this.propertyPanel = new Ext.Window({
//			region : 'west',
//			border : false,
//			title : '属性',
//			width : 200,
//			height: 430,
///			//autoHeight: true,
//			split : true,
//			xtype : 'panel',
//			layout : 'border',
//			collapsible : true,
//			items : [this.treePanel, this.editPanel],
//			closeAction: 'hide'
//		});
		
//		this.propertyPanel.show();
//		this.propertyPanel.alignTo(document, 'tr', [-180,30]);
	},

	// layer used for selection resize
	initResizeLayer : function() {

		this.resizeLayer = new Ext.Layer({
					cls : 'resizeLayer',
					html : 'Resize me'
				});
		this.resizeLayer.setOpacity(0.5);
		this.resizeLayer.resizer = new Ext.Resizable(this.resizeLayer, {
					handles : 'all',
					draggable : true,
					dynamic : true
				});
		this.resizeLayer.resizer.dd.lock();
		this.resizeLayer.resizer.on('resize', function(r, w, h) {
					var n = this.editPanel.currentNode;
					if (!n || !n.elConfig) {
						return false;
					}
					this.markUndo("Resize element to " + w + "x" + h);
					var s = n.fEl.el.getSize();
					if (s.width != w) {
						n.elConfig.width = w;
						if (n.parentNode.elConfig.layout == 'column') {
							delete n.elConfig.columnWidth;
						}
					}
					if (s.height != h) {
						n.elConfig.height = h;
						delete n.elConfig.autoHeight;
					}
					// this.updateForm(true, n.parentNode);
					this.updateForm(true);
					this.setCurrentNode(n);
					this.highlightElement(n.fEl.el);
				}, this);
		this.resizeLayer.resizer.dd.endDrag = function(e) {
			var n = this.editPanel.currentNode;
			if (!n || !n.elConfig) {
				return false;
			}
			var pos = this.resizeLayer.getXY();
			var pPos = n.parentNode.fEl.body.getXY();
			var x = pos[0] - pPos[0];
			var y = pos[1] - pPos[1];
			this.markUndo("Move element to " + x + "x" + y);
			n.elConfig.x = x;
			n.elConfig.y = y;
			this.updateForm(true, n.parentNode);
			this.setCurrentNode(n);
			this.highlightElement(n.fEl.el);
		}.createDelegate(this);

	},

	// customized property grid for attributes
	initEditPanel : function() {

		var fields = [];
		for (var i in Main.FIELDS) {
			fields.push([i, i]);
		}
		var newPropertyField = new Ext.form.ComboBox({
					mode : 'local',
					valueField : 'value',
					displayField : 'name',
					store : new Ext.data.SimpleStore({
								sortInfo : {
									field : 'name',
									order : 'ASC'
								},
								fields : ['value', 'name'],
								data : fields
							})
				});
		newPropertyField.on('specialkey', function(tf, e) {
					var name = tf.getValue();
					var ds = this.editPanel.store;
					if (e.getKey() == e.ENTER && name != ''
							&& !ds.getById(name)) {
						var defaultVal = "";
						if (this.attrType(name) == 'object') {
							defaultVal = "{}";
						}
						if (this.attrType(name) == 'number') {
							defaultVal = 0;
						}
						ds.add(new Ext.grid.PropertyRecord({
									name : name,
									value : defaultVal
								}, name));
						this.editPanel.startEditing(ds.getCount() - 1, 1);
						tf.setValue('');
					}
				}, this);

		var grid = new Ext.grid.PropertyGrid({
					// title : '属性',
					height : 300,
					split : true,
					region : 'center',
					source : {},
					bbar : ['增加属性 :', newPropertyField],
					customEditors : Main.getCustomEditors(),
					newPropertyField : newPropertyField
				});

		var valueRenderer = function(value, p, r) {
			if (typeof value == 'boolean') {
				p.css = (value ? "typeBoolTrue" : "typeBoolFalse");
				return (value ? "True" : "False");
			} else if (this.attrType(r.id) == 'object') {
				p.css = "typeObject";
				return value;
			} else {
				return value;
			}
		}.createDelegate(this);
		var propertyRenderer = function(value, p) {
			var t = Main.FIELDS[value];
			qtip = (t ? t.desc : '');
			p.attr = 'qtip="' + qtip.replace(/"/g, '&quot;') + '"';
			return value;
		};
		grid.colModel.getRenderer = function(col) {
			return (col == 0 ? propertyRenderer : valueRenderer);
		};

		var contextMenu = new Ext.menu.Menu({
					items : [{
						id : 'FBMenuPropertyDelete',
						iconCls : 'icon-delete',
						text : 'Delete this property',
						scope : this,
						handler : function(item, e) {
							this.markUndo('Delete property <i>'
									+ item.record.id + '</i>');
							var ds = grid.store;
							delete grid.getSource()[item.record.id];
							if(grid.getSource().initialConfig)
							{
								delete grid.getSource().initialConfig[item.record.id];
							}
							ds.remove(item.record);
							delete item.record;
							this.updateNode(grid.currentNode);
							var node = grid.currentNode.parentNode
									|| grid.currentNode;
							// this.updateForm.defer(200, this, [false, node]);
							this.updateForm.defer(200, this, [true]);
						}
					}]
				});

		// property grid contextMenu
		grid.on('rowcontextmenu', function(g, idx, e) {
					e.stopEvent();
					var r = this.store.getAt(idx);
					if (!r) {
						return false;
					}
					var i = contextMenu.items.get('FBMenuPropertyDelete');
					i.setText('Delete property "' + r.id + '"');
					i.record = r;
					contextMenu.showAt(e.getXY());
				}, grid);

		// update node text & id
		grid.store.on('update', function(s, r, t) {
					if (t == Ext.data.Record.EDIT) {
						this.markUndo('Set <i>'
								+ r.id
								+ '</i> to "'
								+ Ext.util.Format.ellipsis(
										(String)(r.data.value), 20) + '"');
						var node = grid.currentNode;
						if(grid.getSource().initialConfig)
						{
							grid.getSource().initialConfig[r.id] = r.value;
						}
						this.updateNode(grid.currentNode);
						// this.updateForm(false, node.parentNode || node);
						this.updateForm(true); // update by yangzhenhua.
					}
				}, this, {
					buffer : 100
				});

		this.editPanel = grid;

	},

	// Components panel
	initComponentsPanel : function() {

		// components; config is either an object, or a function called with the
		// adding function and parent config
		var data = getComponents();// Components.getComponents();
		var ds = new Ext.data.SimpleStore({
					fields : ['category', 'name', 'description', 'config'],
					data : data
				});
		var tpl = new Ext.XTemplate('<ul id="FormBuilderComponentSelector">',
				'<tpl for=".">',
				'<li class="component" qtip="{description}">{name}</li>',
				'</tpl>', '<div class="x-clear"></div>', '</ul>');
		var view = new Ext.DataView({
					store : ds,
					tpl : tpl,
					overClass : 'over',
					selectedClass : 'selected',
					singleSelect : true,
					itemSelector : '.component'
				});
		view.on('dblclick', function(v, idx, node, e) {
					e.preventDefault();
					var n = this.editPanel.currentNode;
					if (!n) {
						return false;
					}
					var c = view.store.getAt(idx).data.config;
					if (!c) {
						return false;
					}
					if (typeof c == 'function') {
						c.call(this, function(config) {
									var newNode = this.appendConfig(config, n,
											true, true);
								}, n.elConfig);
					} else {
						var newNode = this.appendConfig(this.cloneConfig(c), n,
								true, true);
					}
				}, this);
		view.on('render', function() {
					var d = new Ext.dd.DragZone(view.el, {
								ddGroup : 'component',
								containerScroll : true,
								getDragData : function(e) {
									view.onClick(e);
									var r = view.getSelectedRecords();
									if (r.length == 0) {
										return false;
									}
									var el = e.getTarget('.component');
									if (el) {
										return {
											ddel : el,
											compData : r[0].data
										};
									}
								},
								getTreeNode : function(data, targetNode) {
									if (!data.compData) {
										return null;
									}

									var c = data.compData.config;
									if (typeof c == 'function') {
										c.call(this, function(config) {
													var n = this.appendConfig(
															config, targetNode,
															true, true);
													this
															.setCurrentNode(n,
																	true);
												}, targetNode.elConfig);
									} else {
										var n = this
												.appendConfig(
														this
																.cloneConfig(data.compData.config),
														targetNode, true, true);
										this.setCurrentNode(n, true);
										return n;
									}
									return null;

								}.createDelegate(this)
							});
					view.dragZone = d;
				}, this);

		var filter = function(b) {
			ds.filter('category', new RegExp(b.text));
		};
		var tb = ['<b>分类 : </b>', 
			  /*
			   {
					text : '所有',
					toggleGroup : 'categories',
					enableToggle : true,
					pressed : true,
					scope : ds,
					handler : ds.clearFilter
				},
			   */ '-'];
		var cats = [];
		ds.each(function(r) {
					var tokens = r.data.category.split(",");
					Ext.each(tokens, function(token) {
								if (cats.indexOf(token) == -1) {
									cats.push(token);
								}
							});
				});
		Ext.each(cats, function(v) {
					tb.push({
								text : v,
								toggleGroup : 'categories',
								enableToggle : true,
								handler : filter
							});
				});

		var panel = new Ext.Window({
					region : 'south',
					title : '工具',
					//height : 300,
					layout : 'fit',
					width  : 250,
					autoHeight: true,
					autoScroll : true,
					items : [view],
					tbar : tb,
					closeAction: 'hide'
				});

		panel.view = view;
		this.componentsPanel = panel;
		if(cats.length>0)
			filter({text:cats[0]});
	},

	// Undo history
	initUndoHistory : function() {
		this.undoHistoryMax = 20;
		this.undoHistory = [];
	},

	// add current config to undo
	markUndo : function(text) {
		this.undoHistory.push({
					text : text,
					config : this.getTreeConfig()
				});
		if (this.undoHistory.length > this.undoHistoryMax) {
			this.undoHistory.remove(this.undoHistory[0]);
		}
		this.updateUndoBtn();
	},

	// update undo button according to undo history
	updateUndoBtn : function() {
		if (this.undoHistory.length == 0) {
			Ext.ComponentMgr.get('FBUndoBtn').disable().setText('恢复');
		} else {
			Ext.ComponentMgr.get('FBUndoBtn').enable().setText('<b>恢复</b> : '
					+ this.undoHistory[this.undoHistory.length - 1].text);
		}
	},

	// undo last change
	undo : function() {
		var undo = this.undoHistory.pop();
		this.updateUndoBtn();
		if (!undo || !undo.config) {
			return false;
		}
		this.setConfig(undo.config);
		return true;
	},

	// return the node corresponding to an element (search upward)
	getNodeForEl : function(el) {
		var search = 0;
		var target = null;
		while (search < 20) {
			target = Ext.ComponentMgr.get(el.id);
			if (target && target._node) {
				return target._node;
			}
			el = el.parentNode;
			if (!el) {
				break;
			}
			search++;
		}
		return null;
	},

	// show the layer to visually resize / move element
	visualResize : function(node) {
		if (node == this.treePanel.root || !node || !node.fEl) {
			return;
		}
		if (node.parentNode && node.parentNode.elConfig
				&& node.parentNode.elConfig.layout == 'fit') {
			if(node.parentNode.elConfig.id != 'FBBuilderPanel')
				Ext.Msg.alert("提示","当上级容器的布局是fit时，大小不能被调整.");
		} else {
			if (node.parentNode && node.parentNode.elConfig
					&& node.parentNode.elConfig.layout == 'absolute') {
				this.resizeLayer.resizer.dd.unlock();
				this.resizeLayer.resizer.dd
						.constrainTo(node.parentNode.fEl.body);
			} else {
				this.resizeLayer.resizer.dd.lock();
			}
			this.resizeLayer.setBox(node.fEl.el.getBox());
			this.resizeLayer.show();
		}
	},

	// hide select layers (e is click event)
	hideHighligt : function(e) {
		if (e) {
			e.preventDefault();
		}
		this.builderPanel.el.select('.selectedElement')
				.removeClass('selectedElement');
		this.builderPanel.el.select('.selectedElementParent')
				.removeClass('selectedElementParent');
	},

	// set current editing node
	setCurrentNode : function(node, select) {
		if(!node)
			return;
		var p = this.editPanel;
		p.enable();
		if (!node || !node.elConfig) {
			p.currentNode = null;
			p.setSource({});
			p.disable();
		} else {
			config = node.elConfig;
			for (k in config) {
				if (this.attrType(k) == 'object'
						&& typeof config[k] == 'object') {
					try {
						var ec = Main.JSON.encode(config[k], 0, true);//Ext.encode(config[k], 0, true);
						config[k] = ec;
					} catch (e) {
					}
				}
			}
			p.setSource(config);
			p.currentNode = node;
			if (node.fEl == this.builderPanel) {
				p.disable();
			}
		}
		if (select) {
			this.treePanel.expandPath(node.getPath());
			node.select();
			this.visualResize(node);
		}
	},

	// update node text & id (if necessary)
	updateNode : function(node) {
		if (!node) {
			return;
		}
		node.setText(this.configToText(node.elConfig));
		if (node.elConfig.id && node.elConfig.id != node.id) {
			// node.getOwnerTree().unregisterNode(node);
			node.id = node.elConfig.id;
			// node.getOwnerTree().registerNode(node);
		}
	},

	// update the form at the specified node (if force or autoUpdate is true)
	updateForm : function(force, node) {
		try{
		node = node || this.treePanel.root;
		var updateTime = (node == this.treePanel.root);
		var time = null;

		// search container to update, upwards
		node = this.searchForContainerToUpdate(node);

		if (force === true || this.autoUpdate) {
			var config = this.getTreeConfig(node, true);
			time = this.setFormConfig(config, node.fEl);
			this.updateTreeEls(node.fEl);
			this.hideHighligt();

			// save into cookies
			//this.cookies.set('formbuilderconfig', Main.JSON.encode(this.getTreeConfig()));
		}

		if (time && updateTime) {
			Ext.ComponentMgr.get('FBRenderingTimeBtn').setText('显示时间 : <i>'
							+ time + 'ms</i>');
		}
		}catch(e)
		{}
	},

	// load from cookies if present
	loadConfig : function(filename, type) {
		if (filename) {
			this.file = filename;
			this.filetype = type || 'local';
			if(this.filetype == 'remote')
			{
				var r = {url:Request['context'] + '/FormDesign.jsp?cmd=get_content&filename='+filename};
				r.success = function(response, o) {
					var text = response.responseText;
					this.textConfig = text;
					eval(text);
					var item = form || tree;
					if (item) {
						try {
							var c = {
								items : [item]
							};
							this.setConfig(c);
						} catch (e) {
							return false;
						}
						return true;
					} else {
						return false;
					}
				}
				r.failure = function(response, o) {
					alert('failure');
				};
				r.scope = this;
				var temp = Ext.lib.Ajax.defaultPostHeader;
				Ext.lib.Ajax.defaultPostHeader += '; charset=GBK';
				Ext.Ajax.request(r);
				Ext.lib.Ajax.defaultPostHeader = temp;
			}
			else if(this.filetype == 'remotedb')
			{
				var r = {url:Request['context'] + '/FormDesign.jsp?cmd=get_dbcontent&filename='+filename};
				r.success = function(response, o) {
					var text = response.responseText;
					this.textConfig = text;
					eval(text);
					var item = form || tree;
					if (item) {
						try {
							var c = {
								items : [item]
							};
							this.setConfig(c);
						} catch (e) {
							return false;
						}
						return true;
					} else {
						return false;
					}
				}
				r.failure = function(response, o) {
					alert('failure');
				};
				r.scope = this;
				var temp = Ext.lib.Ajax.defaultPostHeader;
				Ext.lib.Ajax.defaultPostHeader += '; charset=GBK';
				Ext.Ajax.request(r);
				Ext.lib.Ajax.defaultPostHeader = temp;
			}
			else
			{
				var fso = new ActiveXObject('Scripting.FileSystemObject');
				filename = decodeURIComponent(filename);
				var f1 = fso.OpenTextFile(filename, 1, false, -2);
				var text = f1.ReadAll();
				f1.Close();
				var form, tree;
				this.textConfig = text;
				eval(text);
				var item = form || tree;
				if (item) {
					try {
						var c = {
							items : [item]
						};
						this.setConfig(c);
					} catch (e) {
						return false;
					}
					return true;
				} else {
					return false;
				}
			}
		} else {
			var c = this.cookies.get('formbuilderconfig');
			if (c) {
				try {
					this.setConfig(c);
				} catch (e) {
					return false;
				}
				return true;
			} else {
				return false;
			}
		}

	},
	openFile : function() {
		if (!this.openWindow) {
			var items = [];
			var forms;
			var dbforms;
			if(Request['schema'] == 'http' && Request['context'])
			{
				forms = new Ec.ComboBox({
					filetype:'remote',
					forceSelection: true,
				    triggerAction: 'all',
				    fieldLabel: '表单列表',
					url: Request['context'] + '/FormDesign.jsp?cmd=get_files',
					selectone: 'forms'
				});
				dbforms = new Ec.ComboBox({
					filetype:'remotedb',
					forceSelection: false,
				    triggerAction: 'all',
				    fieldLabel: '自定义表单列表',
					url: Request['context'] + '/FormDesign.jsp?cmd=get_dbfiles',
					selectone: 'forms'
				});
				items.push(forms);
				items.push(dbforms);
			}
			else
			{
				forms = new Ec.FileUploadField({filetype:'local', fieldLabel:'本地文件'});
				items.push(forms);
			}
			this.openWindow = new Ext.Window({
						title : "Open Config",
						width : 400,
						autoHeight : 'auto',
						layout : 'form',
						items : items,
						modal : true,
						frame: true,
						closeAction : 'hide'
					});
			this.openWindow.addButton({
						text : "关闭",
						scope : this,
						handler : function() {
							this.openWindow.hide();
						}
					});
			this.openWindow.addButton({
						text : "打开",
						scope : this,
						handler : function() {
							var config = forms.getValue();
							var type = forms.filetype;
							if (config == "") {
								config = dbforms.getValue();
								type = dbforms.filetype;
							}
							if(config == "")
							{
								this.openWindow.el.unmask();
								Ext.Msg.alert("Error", "Config seems invalid");
								return;
							} else {
								this.loadConfig(config,type);
							}
							this.openWindow.el.unmask();
							this.openWindow.hide();
						}
					});
		}
		this.openWindow.show();
	},
	saveFile : function() {
		if (this.file) {
			Ext.Msg.confirm("Info", "确认覆盖原文件?", function(b) {
						if (b == 'yes') {
							if(this.filetype == 'remote' || this.filetype == 'remotedb')
							{
								var r = {url:Request['context'] + '/FormDesign.jsp'};
								r.success = function(response, o) {
									var text = response.responseText;
									var msg = eval('('+text+')');
									Ext.Msg.alert('成功',msg.message);
								}
								r.failure = function(response, o) {
									Ext.Msg.alert('失败','保存失败');
								};
								r.scope = this;
								//r.method = 'POST';
								var cleanConfig = this.getTreeConfig();
									cleanConfig = (cleanConfig.items
											? cleanConfig.items[0] || {}
											: {});
								cleanConfig = Main.JSON.encode(cleanConfig, 0, true);
								this.textConfig = "var form = " + cleanConfig;
								
								r.params = {
									cmd: this.filetype == 'remote' ?'save_changes':'save_dbchanges',
									filename: this.file,
									content: encodeURI(cleanConfig)
								};
								
								var temp = Ext.lib.Ajax.defaultPostHeader;
								Ext.lib.Ajax.defaultPostHeader += '; charset=GBK';
								Ext.Ajax.request(r);
								Ext.lib.Ajax.defaultPostHeader = temp;
							}
							else
							{
								var fso = new ActiveXObject('Scripting.FileSystemObject');
								var f = fso.OpenTextFile(this.file, 2, true);
								if (f) {
									var cleanConfig = this.getTreeConfig();
									cleanConfig = (cleanConfig.items
											? cleanConfig.items[0] || {}
											: {});
									cleanConfig = "var form = " + Main.JSON.encode(cleanConfig, 0, true);
									this.textConfig = cleanConfig;
									
									f.Write(cleanConfig);
									f.Close();
								} else {
									Ext.Msg.alert("Error", "文件路径不合法.");
								}
							}
						}
					}, this);
			
		} else {
			Ext.Msg.confirm("Error", "没有打开文件." + '需要另存为文件 ?', function(b) {
						if (b == 'yes') {
							this.saveAsFile();
						}
					}, this);
		}
	},
	saveAsFile : function() {
		if (!this.saveWindow) {
			var tf;
			if(Request['schema'] == 'http' && Request['context'])
			{
				tf = new Ext.form.TextField({
				    xtype:'textfield',
				    fieldLabel:'表单名称',
				    name:'textvalue',
				    fileType: 'remotedb'
				  });
			}
			else
			{
				tf = new Ext.form.FileUploadField({
						readOnly : false,
						fileType: 'local'
					});
			}
			
			this.saveWindow = new Ext.Window({
						title : "Open Config",
						width : 400,
						autoHeight : 'auto',
						layout : 'fit',
						items : [tf],
						modal : true,
						closeAction : 'hide'
					});
			this.saveWindow.addButton({
						text : "关闭",
						scope : this,
						handler : function() {
							this.saveWindow.hide();
						}
					});
			this.saveWindow.addButton({
						text : "选择",
						scope : this,
						handler : function() {
							var config = tf.getValue();
							var type = tf.fileType;
							if (config != "") {
								this.file = config;
								this.filetype  = type;
								this.saveFile();
								this.saveWindow.hide();
							}
						}
					});
		}
		this.saveWindow.show();
	},
	// search upware for a container to update
	searchForContainerToUpdate : function(node) {

		// search for a parent with border or column layout
		var found = null;
		var root = this.treePanel.root;
		var n = node;
		while (n != root) {
			if (n
					&& n.elConfig
					&& (n.elConfig.layout == 'border'
							|| n.elConfig.layout == 'table' || n.elConfig.layout == 'column')) {
				found = n;
			}
			n = n.parentNode;
		}
		if (found !== null) {
			return found.parentNode;
		}

		// no column parent, search for first container with items
		n = node;
		while (n != root) {
			if (!n.fEl || !n.fEl.items) {
				n = n.parentNode;
			} else {
				break;
			}
		}
		return n;
	},

	// hilight an element
	highlightElement : function(el) {
		this.resizeLayer.hide();
		if (el == this.builderPanel.el) {
			return;
		}
		if (el) {
			var elParent = el.findParent('.x-form-element', 5, true);
			this.hideHighligt();
			if (elParent) {
				elParent.addClass("selectedElementParent");
			}
			el.addClass("selectedElement");
		}
	},
	// update node.fEl._node associations
	updateTreeEls : function(el) {
		if (!el) {
			el = this.builderPanel;
		}
		if (el._node) {
			el._node.fEl = el;
			// workaround for fieldsets
			if (el.xtype == 'fieldset') {
				el.el.dom.id = el.id;
			} else if (el.xtype == 'ec_grid') {
				if(el.searchBar)
				{
					el.searchBar._node.fEl = el.searchBar.form;
					el.searchBar.form.items.each(function(i) {
						this.updateTreeEls(i);
					}, this);
				}
				if(el.Model)
				{
					var model = el.Model;
					for(var i=0;i<model.length;i++)  
         			{
         				if(model[i]._node)
         					model[i]._node.fEl = el;         					
         			}
				}				
			}
			if(el.elements && el.elements.indexOf('tbar')>=0)
			{
				var titems = el.topToolbar.items.items;
				for(var i=0; i<titems.length; i++)
				{
					if(titems[i]._node)
						titems[i]._node.fEl = titems[i];
					else if(titems[i].initialConfig && titems[i].initialConfig._node)
						titems[i].initialConfig._node.fEl = titems[i];
				}
			}
			if(el.elements && el.elements.indexOf('bbar')>=0)
			{
				var titems = el.bottomToolbar.items.items;
				for(var i=0; i<titems.length; i++)
				{
					if(titems[i]._node)
						titems[i]._node.fEl = titems[i];
					else if(titems[i].initialConfig && titems[i].initialConfig._node)
						titems[i].initialConfig._node.fEl = titems[i];
				}
			}
			if(el.buttons)
			{
				var titems = el.buttons;
				for(var i=0; i<titems.length; i++)
				{
					if(titems[i]._node)
						titems[i]._node.fEl = titems[i];
					else if(titems[i].initialConfig && titems[i].initialConfig._node)
						titems[i].initialConfig._node.fEl = titems[i];
				}
			}
		}
		if (!el.items) {
			return;
		}
		try {
			el.items.each(function(i) {
						this.updateTreeEls(i);
					}, this);
		} catch (e) {
		}
	},

	// return type of attribute
	attrType : function(name) {
		if (!Main.FIELDS[name]) {
			return 'unknown';
		}
		return Main.FIELDS[name].type;
	},

	// return a cloned config
	cloneConfig : function(config, top) {
		if (!config) {
			return null;
		}
		var newConfig = {};
		var i;
		for (i in config) {
//			if(!config[i])
//				continue;
			var is = config[i];
			if (is instanceof Array) {
				var items = new Array();
				for(var j=0; j<is.length; j++)
				{
				   var item = this.cloneConfig(is[j]);
				   if(item)
				   	items.push(item);
				}
				newConfig[i] = items;
			}else if (typeof config[i] == 'object') {
				newConfig[i] = this.cloneConfig(config[i]);
			}else if (typeof config[i] != 'function') {
				newConfig[i] = config[i];
			}
		}
		return newConfig;
	},

	// erase all
	resetAll : function() {
		var w = this.viewport.layout.center.getSize().width - 50;
		var node = this.setConfig({
					items : []
				});
		this.setCurrentNode(node, true);
	},

	// get a new ID
	getNewId : function() {
		return "form-gen-" + (this.idCounter++);
	},

	// return true if config can be added to node, or an error message if it
	// cannot
	canAppend : function(config, node) {
		if (node == this.treePanel.root && this.treePanel.root.hasChildNodes()) {
			return "Only one element can be directly under the GUI Builder";
		}
		var xtype = node.elConfig.xtype;
		if (xtype
				&& ['panel', 'viewport', 'form', 'ec_form', 'window',
						'tabpanel', 'toolbar', 'fieldset', 'radiogroup','ec_radiogroup','ec_checkboxgroup',
						'ec_grid', 'ec_tree'].indexOf(xtype) == -1) {
			return 'You cannot add element under xtype "' + xtype + '"';
		}
		
		if(config.formConfig)
		{
			var exitSearch = false;
			node.eachChild(function(n){
				if(n.elConfig.formConfig)
					exitSearch = true;
			});
			if(exitSearch)
				return 'Only one searchbar can be hold.';
		}
		return true;
	},
// get the tree config at the specified node
	getTreeConfig : function(node, addNodeInfos) {
		if (!node) {
			node = this.treePanel.root;
		}
		var config = Ext.apply({}, node.elConfig);
		if (!config.id && addNodeInfos) {
			config.id = node.id;
		}
		for (k in config) {
			if (this.attrType(k) == 'object') {
				try {
					config[k] = Ext.decode(config[k]);
				} catch (e) {
				}
			}
		}
		if (addNodeInfos) {
			config._node = node;
		}
		var items = [];

		node.eachChild(function(n) {
			if(n.elConfig.topbar || n.elConfig.bottombar || n.elConfig.buttonbar)
				return;
			if (n.elConfig && n.elConfig.xtype != 'ec_grid') {
				var nconfig = this.getTreeConfig(n, addNodeInfos);
				this.specialConfig(nconfig,n);
				items.push(nconfig);
			}
			else if (n.elConfig.xtype == 'ec_grid') 
			{
//				n.elConfig.deferRowRender = false;
				if (addNodeInfos) 
				{
					n.elConfig._node = n;
				}
				var gconfig = Ext.apply({}, n.elConfig);
//				if(gconfig.view)
//				{
//					gconfig.view.destroy();
//					gconfig.view = null;
//				}
				for (k in gconfig) {
					if (this.attrType(k) == 'object') {
						try {
							gconfig[k] = Ext.decode(gconfig[k]);
						} catch (e) {
						}
					}
				}
				//if (gconfig.searchFormCfg) 
				var searchChild;
				n.eachChild(function(n1) {
					if(n1.elConfig.formConfig)
					{
						searchChild = n1;
						gconfig.searchFormCfg = n1.elConfig;
						for (k in gconfig.searchFormCfg) {
							if (this.attrType(k) == 'object') {
								try {
									gconfig.searchFormCfg[k] = Ext.decode(gconfig.searchFormCfg[k]);
								} catch (e) {
								}
							}
						}
					}
				}, this);
				if (searchChild) {
					delete gconfig.searchFormCfg.searchFields;
					gconfig.searchFormCfg.searchFields = [];
					searchChild.eachChild(function(n1) {
						if(n1.elConfig.xtype)
						{
							var mconfig = Ext.apply({}, n1.elConfig);
							for (k in mconfig) {
								if (this.attrType(k) == 'object') {
									try {
										mconfig[k] = Ext.decode(mconfig[k]);
									} catch (e) {
									}
								}
							}
							n1.eachChild(function(n2){
								if(n2.elConfig.xtype)
								{
									var mconfig2 = Ext.apply({}, n2.elConfig);
									for (k in mconfig2) {
										if (this.attrType(k) == 'object') {
											try {
												mconfig2[k] = Ext.decode(mconfig2[k]);
											} catch (e) {
											}
										}
									}
									if(!mconfig.items)
										mconfig.items = [];
									mconfig.items.push(mconfig2);
								}
							},this);
							gconfig.searchFormCfg.searchFields.push(mconfig);
						}
					},this);
				}
				if (gconfig.Model) {
					delete gconfig.Model;
					gconfig.Model = [];
					n.eachChild(function(n1) {
						if(n1.elConfig.header)
						{
							var mconfig = Ext.apply({}, n1.elConfig);
							for (k in mconfig) {
								if (this.attrType(k) == 'object') {
									try {
										mconfig[k] = Ext.decode(mconfig[k]);
									} catch (e) {
									}
								}
							}
							gconfig.Model.push(mconfig);
						}
					},this);
				}
				
				this.specialConfig(gconfig, n);
				//if(gconfig.tbar && gconfig.tbar.length>=2 && searchChild)
				//	gconfig.tbar = gconfig.tbar.slice(0,gconfig.tbar.length-2);
				items.push(gconfig);
			}
		}, this);
		if (items.length > 0) {
			config.items = items;
		} else if (config.xtype == 'form') {
			config.items = {};
		} else if (config.xtype == 'ec_form') {
			config.items = {};
		} else {
			if (config.xtype == 'ec_grid') {
				if(!config.Model)
				{
					config.Model = [];
				}
			}
			delete config.items;
		}
		return config;
	},
	specialConfig: function(gconfig, node){
		node.eachChild(function(n1) {
			if(n1.elConfig.topbar)
			{
				gconfig.tbar = [];
				n1.eachChild(function(n2) {
					if(n2.elConfig.name)
					{
						gconfig.tbar.push(n2.elConfig);
					}
				}, this)
			}
			if(n1.elConfig.bottombar)
			{
				gconfig.bbar = [];
				n1.eachChild(function(n2) {
					if(n2.elConfig.name)
					{
						gconfig.bbar.push(n2.elConfig);
					}
				}, this)
			}
			if(n1.elConfig.buttonbar)
			{
				gconfig.buttons = [];
				n1.eachChild(function(n2) {
					if(n2.elConfig.name)
					{
						gconfig.buttons.push(n2.elConfig);
					}
				}, this)
			}
		}, this);

	},
	// node text created from config of el
	configToText : function(c) {
		var txt = [];
		c = c || {};
		if (c.xtype) {
			txt.push(c.xtype);
		}
		if (c.fieldLabel) {
			txt.push('[' + c.fieldLabel + ']');
		}
		if (c.boxLabel) {
			txt.push('[' + c.boxLabel + ']');
		}
		if (c.layout) {
			txt.push('<i>' + c.layout + '</i>');
		}
		if (c.title) {
			txt.push('<b>' + c.title + '</b>');
		}
		if (c.text) {
			txt.push('<b>' + c.text + '</b>');
		}
		if (c.region) {
			txt.push('<b>(' + c.region + ')</b>');
		}
		if (c.header) {
			txt.push('<b>列(' + c.header + ')</b>');
		} else if(c.dataIndex){
			txt.push('<b>列(' + c.dataIndex + ')</b>');
		}
		if (c.formConfig) {
			txt.push('<b>searchBar</b>');
		}
		if (c.topbar) {
			txt.push('<b>topbar</b>');
		}
		if (c.bottombar) {
			txt.push('<b>bottombar</b>');
		}
		if (c.buttonbar) {
			txt.push('<b>buttonbar</b>');
		}
		return (txt.length == 0 ? "Element" : txt.join(" "));
	},
	lookforNode: function(node, config)
	{
		var retV;
		if(config.type_dt)
		{
			node.eachChild(function(n){
				if(n.elConfig && n.elConfig[config.type_dt])
					retV = n;
			});
			if(!retV)
			{
				var nc = {};
				nc[config.type_dt] = true;
				retV = this.appendConfig(nc, node, false);
			}
		}
		else if(node.elConfig.xtype=='ec_grid')
		{
			if(config.xtype)
			{
				node.eachChild(function(n){
					if(n.elConfig && n.elConfig.formConfig)
						retV = n;
				});
			}
			else if(config.formConfig)
			{
				node.eachChild(function(n){
					if(n.elConfig.formConfig)
						retV = n;
				});
				if(retV)
					retV = null;
				else
					retV = node;
			}
			else 
			{
				retV = node;
			}
		}
		else if(node.elConfig.xtype !='ec_grid' && (config.formConfig || config.header))
		{
			retV = null;
		}
		else
		{
			retV = node;
		}
		return retV;
	},
	specialAppend: function(newNode, config)
	{
		//if(config.xtype == 'ec_grid')
		//{
		var Model = config.Model;
		 if (Model && Model.length) {
			for (var i = 0; i < Model.length; i++) {
				this.appendConfig(Model[i], newNode, false);
			}
		}
		
		var searchFormCfg = config.searchFormCfg;
		if (searchFormCfg) {
			var sConfig = searchFormCfg; 
			var sNode = this.appendConfig(sConfig, newNode, false);
		}
		//}
		//else for searchbar 
		//{
		if(config.searchFields != undefined)
		{
			for(var n in config.searchFields){ 
				if(config.searchFields[n].xtype)
					this.appendConfig(config.searchFields[n], newNode, false); 
			} 
		}
		//}
		if(config.tbar != undefined)
		{
			var tNode = this.appendConfig({topbar:true}, newNode, false); 
			for(var n in config.tbar){ 
				if(config.tbar[n].name){
					var ntNode = this.appendConfig(config.tbar[n], tNode, false);
					config.tbar[n]._node = ntNode;
					if(config.tbar[n].initialConfig)
						config.tbar[n].initialConfig._node = ntNode;
				}
			} 
		}
		if(config.bbar != undefined)
		{
			var bNode = this.appendConfig({bottombar:true}, newNode, false); 
			for(var n in config.bbar){ 
				if(config.bbar[n].name)
				{
					var ntNode = this.appendConfig(config.bbar[n], bNode, false);
					config.bbar[n]._node = ntNode;
				}
			} 
		}
		if(config.buttons != undefined)
		{
			var bNode = this.appendConfig({buttonbar:true}, newNode, false); 
			for(var n in config.buttons){ 
				if(config.buttons[n].name)
				{
					var ntNode = this.appendConfig(config.buttons[n], bNode, false);
					config.buttons[n]._node = ntNode;
				}
			} 
		}
	},
	// add a config to the tree
	appendConfig : function(config, appendTo, doUpdate, markUndo) {

		if (!appendTo) {
			appendTo = this.treePanel.getSelectedNode() || this.treePanel.root;
		}
		var canAppend = this.canAppend(config, appendTo);
		if (canAppend !== true) {
			Ext.Msg.alert("Unable to add element", canAppend);
			return false;
		}
		appendTo = this.lookforNode(appendTo, config);
		if(!appendTo)
			return null;
		
		var items = config.items;
		delete config.items;
		var id = config.id
				|| (config._node ? config._node.id : this.getNewId());
		var newNode = new Ext.tree.TreeNode({
					id : id,
					text : this.configToText(config)
				});
		for (var k in config) {
			if (config[k] === null) {
				delete config[k];
			}
		}
		
		newNode.elConfig = config;
		config._node = newNode;
		this.specialAppend(newNode, config);
		
		if (markUndo === true) {
			this.markUndo("Add " + newNode.text);
		}

		appendTo.appendChild(newNode);
		if (items && items.length) {
			for (var i = 0; i < items.length; i++) {
				this.appendConfig(items[i], newNode, false);
			}
		}
		if (doUpdate !== false) {
			this.updateForm(false, newNode);
		}
		return newNode;

	},

	// remove a node
	removeNode : function(node) {
		if (!node || node == this.treePanel.root) {
			return;
		}
		this.markUndo("Remove " + node.text);
		var nextNode = node.nextSibling || node.parentNode;
		var pNode = node.parentNode;
		pNode.removeChild(node);
		this.setCurrentNode(nextNode, true);
		// this.updateForm(false, pNode);
		this.updateForm(true);
	},

	// update the form
	setFormConfig : function(config, el) {

		el = el || this.builderPanel;
		
		// empty the form
		if (el.items) {
			while (el.items.first()) {
				if(el.remove)
				{
					var it = el.items.first();
					el.remove(it, true);
					if(it.view)
					{
						delete it.view;
						it.view = null;
					}
				}
			}
		}
		if (el.getLayoutTarget) {
			el.getLayoutTarget().update();
		} else {
			el.update();
		}
		
		// adding items
		var start = new Date().getTime();
		if (config.items) {
			for (var i = 0; i < config.items.length; i++) {
//				if(config.items[i].initialConfig)
//					el.add(config.items[i].initialConfig);
//				else
					el.add(config.items[i]);
			}
		}
		el.doLayout();
		var time = new Date().getTime() - start;
		return time;

	},

	// show a window with the json config
	editText : function() {
		var size = this.viewport.getSize();
		if (!this.textWindow) {
			var tf = new Ext.form.TextArea();
			this.textWindow = new Ext.Window({
						title : "Form Config",
						width : 400,
						height : size.height - 50,
						autoScroll : true,
						layout : 'fit',
						items : [tf],
						modal : true,
						closeAction : 'hide'
					});
			this.textWindow.tf = tf;

			this.textWindow.addButton({
						text : "Close",
						scope : this.textWindow,
						handler : function() {
							this.hide();
						}
					});
			this.textWindow.addButton({
				text : "Apply",
				scope : this,
				handler : function() {
					var config = null;
					try {
						this.textWindow.el.mask("Please wait...");
						this.textConfig = tf.getValue();
						var value = this.textConfig;
						eval(value);
						//this.storeConfig = Ext.decode(value);
						config = form||tree;//Ext.decode(value);
					} catch (e) {
						config = null;
						this.textWindow.el.unmask();
						Ext.Msg.alert("Error", "JSON is invalid : " + e.name
										+ "<br/>" + e.message);
						return;
					}
					if (!config) {
						this.textWindow.el.unmask();
						Ext.Msg.alert("Error", "Config seems invalid");
						return;
					} else {
						this.markUndo("JSON edit");
						try {
							var c = {items : [config]};
							//this.storeConfig = this.cloneConfig(c);
							this.setConfig(c);
						} catch (e) {
							this.textWindow.el.unmask();
							Ext.Msg.confirm("Error",
								"Error while adding : "
										+ e.name
										+ "<br/>"
										+ e.message
										+ '<br/>'
										+ 'Do you wish to revert to previous ?',
								function(b) {
									if (b == 'yes') {
										this.undo();
									}
								}, this);
						}
					}
					this.textWindow.el.unmask();
					this.textWindow.hide();
				}
			});
		}
		var cleanConfig = this.textConfig;
		this.textWindow.tf.setValue(cleanConfig);
		this.textWindow.show();
	},
	
	// show a window with the json config
	editConfig : function() {
		var size = this.viewport.getSize();
		if (!this.jsonWindow) {
			var tf = new Ext.form.TextArea();
			this.jsonWindow = new Ext.Window({
						title : "Form Config",
						width : 400,
						height : size.height - 50,
						autoScroll : true,
						layout : 'fit',
						items : [tf],
						modal : true,
						closeAction : 'hide'
					});
			this.jsonWindow.tf = tf;

			this.jsonWindow.addButton({
						text : "Close",
						scope : this.jsonWindow,
						handler : function() {
							this.hide();
						}
					});
			this.jsonWindow.addButton({
				text : "Apply",
				scope : this,
				handler : function() {
					var config = null;
					try {
						this.jsonWindow.el.mask("Please wait...");
						var value = tf.getValue();
						//this.storeConfig = Ext.decode(value);
						config = Ext.decode(value);
					} catch (e) {
						config = null;
						this.jsonWindow.el.unmask();
						Ext.Msg.alert("Error", "JSON is invalid : " + e.name
										+ "<br/>" + e.message);
						return;
					}
					if (!config) {
						this.jsonWindow.el.unmask();
						Ext.Msg.alert("Error", "Config seems invalid");
						return;
					} else {
						this.markUndo("JSON edit");
						try {
							var c = {items : [config]};
							//this.storeConfig = this.cloneConfig(c);
							this.setConfig(c);
						} catch (e) {
							this.jsonWindow.el.unmask();
							Ext.Msg.confirm("Error",
								"Error while adding : "
										+ e.name
										+ "<br/>"
										+ e.message
										+ '<br/>'
										+ 'Do you wish to revert to previous ?',
								function(b) {
									if (b == 'yes') {
										this.undo();
									}
								}, this);
						}
					}
					this.jsonWindow.el.unmask();
					this.jsonWindow.hide();
				}
			});
		}
		var cleanConfig = this.getTreeConfig();//this.getStoreTreeConfig() || {};
		cleanConfig = cleanConfig.items ? (cleanConfig.items[0] || {}) : {};
		cleanConfig = Main.JSON.encode(cleanConfig, 0 , true);
		this.jsonWindow.tf.setValue(cleanConfig);
		this.jsonWindow.show();
	},
	// remove all nodes
	showTool : function() {
		this.componentsPanel.show();
	},
	// remove all nodes
	removeAll : function() {
		var root = this.treePanel.root;
		while (root.firstChild) {
			root.removeChild(root.firstChild);
		}
	},

	// set config (remove then append a whole new config)
	setConfig : function(config) {
		if (!config || !config.items) {
			return false;
		}
		// delete all items
		this.removeAll();
		this.setFormConfig({items:[]});
		// add all items
		var root = this.treePanel.root;
		var node = null;
		for (var i = 0; i < config.items.length; i++) {
			try {
				node = this.appendConfig(config.items[i], root);
			} catch (e) {
				Ext.Msg.alert("Error", "Error while adding : " + e.name
								+ "<br/>" + e.message);
			}
		}
		this.updateForm(true, root);
		return node || root;
	}
};

Array.prototype.indexOf = function(value) {
	for (var i = 0, l = this.length; i < l; i++)
		if(this[i] == value)
			return i;
	return -1;
}

// modified Ext.util.JSON to display a readable config
Main.JSON = new (function() {
	var useHasOwn = {}.hasOwnProperty ? true : false;
	var pad = function(n) {
		return n < 10 ? "0" + n : n;
	};
	var m = {
		"\b" : '\\b',
		"\t" : '\\t',
		"\n" : '\\n',
		"\f" : '\\f',
		"\r" : '\\r',
		'"' : '\\"',
		'\'' : '\\\'',
		"\\" : '\\\\'
	};
	var encodeString = function(s) {
		if (/["\\\x00-\x1f]/.test(s)) {
			return '\'' + s.replace(/([\x00-\x1f\\"])/g, function(a, b) {
						var c = m[b];
						if (c) {
							return c;
						}
						c = b.charCodeAt();
						return "\\u00" + Math.floor(c / 16).toString(16)
								+ (c % 16).toString(16);
					}) + '\'';
		}
		return '\'' + s + '\'';
	};

	var indentStr = function(n) {
		var str = "", i = 0;
		while (i < n) {
			str += "  ";
			i++;
		}
		return str;
	};

	var encodeArray = function(o, indent) {
		indent = indent || 0;
		var a = ["["], b, i, l = o.length, v;
		for (i = 0; i < l; i += 1) {
			v = o[i];
			switch (typeof v) {
				case "undefined" :
				case "function" :
				case "unknown" :
					break;
				default :
					if (b) {
						a.push(',');
					}
					a.push(v === null ? "null" : Main.JSON.encode(v, indent + 1));
					b = true;
			}
		}
		a.push("]");
		return a.join("");
	};

	var encodeDate = function(o) {
		return '"' + o.getFullYear() + "-" + pad(o.getMonth() + 1) + "-"
				+ pad(o.getDate()) + "T" + pad(o.getHours()) + ":"
				+ pad(o.getMinutes()) + ":" + pad(o.getSeconds()) + '"';
	};

	this.encode = function(o, indent, top) {
		indent = indent || 0;
		if(top)
			this.objs = new Array();
		if (typeof o == "undefined" || o === null) {
			return "null";
		} else if (o instanceof Array) {
			return encodeArray(o, indent);
		} else if (o instanceof Date) {
			return encodeDate(o);
		} else if (typeof o == "string") {
			return encodeString(o);
		} else if (typeof o == "number") {
			return isFinite(o) ? String(o) : "null";
		} else if (typeof o == "boolean") {
			return String(o);
		} else if (o.initialConfig) {
			return this.encode(o.initialConfig, indent + 1);
		} else {
			if(typeof o == 'object')
			{
				if(this.objs.indexOf(o) != -1)
					return;
				else
					this.objs.push(o);
			}
			var a = ["{\n"], b, i, v, vs;
			if (o.items instanceof Array) {
				var items = o.items;
				delete o.items;
				o.items = items;
			}
			if (o.id && /^internal_/.test(o.id))
				return;
				
			if(o.replace_dt)
			{
				return o.replace_dt;
			}
			
			for (i in o) {
				if (i === "_node") {
					continue;
				}
				
				if (/^_dt$/.test(i)) {
					continue;
				}
				if (i === "type_dt") {
					continue;
				}
				if (i === "id" && /^form-gen-/.test(o[i])) {
					continue;
				}
				if (i === "id" && /^ext-comp-/.test(o[i])) {
					continue;
				}
				if (i === "id" && /^internal_/.test(o[i])) {
					continue;
				}
				if (!useHasOwn || o.hasOwnProperty(i)) {
					if(o[i+'_dt'])
						v = o[i+'_dt'];
					else
						v = o[i];
					
					v = o[i];
					switch (typeof v) {
						case "undefined" :
						case "unknown" :
							break;
//						case "function" :
						default :
							if (b) {
								a.push(',\n');
							}
							if(typeof v != 'function')
							{
								 if( i=='editor' )
								 	vs = '"'+v+'"';
								 else
								 	vs = this.encode(v, indent + 1);
							}
							else
								vs = v;
							a.push(indentStr(indent), i, ":", 
									vs === null? "null": vs);
							b = true;
					}
				}
			}
			a.push("\n" + indentStr(indent - 1) + "}");
			if(top)
				delete this.objs;
			return a.join("");
		}
	};

})();

// parse DocRefs
var fields = {};
var fileName;
var infos;
var type;
var desc;
for (fileName in DocRefs) {
	for (key in DocRefs[fileName]) {
		infos = DocRefs[fileName][key];
		if (infos.type == "Function") {
			continue;
		}
		desc = "<i>" + fileName + "</i><br/><b>" + infos.type + "</b> "
				+ infos.desc;
		if (!fields[key]) {
			fields[key] = {
				desc : desc
			};
			if (infos.type == "Boolean") {
				type = "boolean";
			} else if (infos.type == "Number") {
				type = "number";
			} else if (infos.type.match(/Object/)) {
				type = "object";
			} else {
				type = "string";
			}
			fields[key].type = type;
		} else {
			fields[key].desc += "<hr/>" + desc;
		}
	}
}
Ext.apply(fields, {
	xtype : {
		desc : "",
		type : "string",
		values : 'component box viewport panel window dataview colorpalette datepicker tabpanel button splitbutton cycle toolbar tbitem tbseparator tbspacer tbfill tbtext tbbutton tbsplit paging editor treepanel field textfield trigger textarea numberfield datefield combo checkbox radio hidden form fieldset htmleditor timefield grid editorgrid progress ec_form ec_grid ec_combo'
				.split(' ')
	},
	region : {
		desc : "",
		type : "string",
		values : 'center west north south east'.split(' ')
	},
	hideMode : {
		desc : "",
		type : "string",
		values : 'visibility display offsets'.split(' ')
	},
	msgTarget : {
		desc : "",
		type : "string",
		values : 'qtip title under side'.split(' ')
	},
	shadow : {
		desc : "",
		type : "string",
		values : 'sides frame drop'.split(' ')
	},
	tabPosition : {
		desc : "",
		type : "string",
		values : 'top bottom'.split(' ')
	},
	columnWidth : {
		desc : "Size of column (0 to 1 for percentage, >1 for fixed width",
		type : "number"
	},
	fieldLabel : {
		desc : "Label of the field",
		type : "string"
	},
	x : {
		desc : "X position in pixels (for absolute layouts",
		type : "number"
	},
	y : {
		desc : "Y position in pixels (for absolute layouts",
		type : "number"
	},
	anchor : {
		desc : "Anchor size (width) in %",
		type : "string"
	},
	colspan : {
		desc : "Column span in tableformLayout.",
		type : "number"
	},
	rowspan : {
		desc : "Row span in tableformLayout.",
		type : "number"
	}
});
fields.layout.values = [];
for (i in Ext.Container.LAYOUTS) {
	fields.layout.values.push(i);
}
fields.vtype.values = [];
for (i in Ext.form.VTypes) {
	fields.vtype.values.push(i);
}
fields.defaultType.values = fields.defaults.xtype;
Main.FIELDS = fields;

// custom editors for attributes
Main.getCustomEditors = function() {
	var g = Ext.grid;
	var f = Ext.form;
	var cmEditors = new g.PropertyColumnModel().editors;
	var eds = {};
	var fields = Main.FIELDS;
	for (i in fields) {
		if (fields[i].values) {
			var values = fields[i].values;
			var data = [];
			for (j = 0; j < values.length; j++) {
				data.push([values[j], values[j]]);
			}
			eds[i] = new g.GridEditor(new f.SimpleCombo({
						forceSelection : false,
						data : data,
						editable : true
					}));
		} else if (fields[i].type == "boolean") {
			eds[i] = cmEditors['boolean'];
		} else if (fields[i].type == "number") {
			eds[i] = cmEditors['number'];
		} else if (fields[i].type == "string") {
			eds[i] = cmEditors['string'];
		} else if (fields[i].type == "object") {
			eds[i] = cmEditors['string'];
		}
	}
	return eds;
};

main = new Main();
Ext.onReady(main.init, main);
