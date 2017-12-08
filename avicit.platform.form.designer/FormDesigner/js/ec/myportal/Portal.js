Ext.ns("divo")
Ext.ns("divo.data")
Ext.ns("divo.form")
Ext.ns("divo.grid")
Ext.ns("divo.layout")
Ext.ns("divo.panel")
Ext.ns("divo.menu")
Ext.ns("divo.utils")
Ext.ns("divo.tree")
Ext.ns("divo.misc")
Ext.ns("divo.admin.portal")

divo.getUserId = Ext.emptyFn

Ext.override(Ext.Component, {
	myId : function(id) {
		return this.id + "-" + id
	}
})

divo.StateProvider = function(config) {
	divo.StateProvider.superclass.constructor.call(this);
	Ext.apply(this, config);
};

Ext.extend(divo.StateProvider, Ext.state.Provider, {
	// private
	set : function(name, value) {
		if (name.length > 3 && name.substr(0, 3) == 'ext')
			return;

		if (value == undefined || value === null || value=='') {
			//this.clear(name);
			return;
		}
		
		divo.saveProfileAsync({
			userId : divo.getUserId(),
			msgCode : name,
			msgValue : this.encodeValue(value)
		})
		divo.StateProvider.superclass.set.call(this, name, value);
	},

//	// private
//	clear : function(name) {
//		divo.saveProfileAsync({
//			userId : divo.getUserId(),
//			msgCode : name,
//			msgValue : ''
//		})
//		divo.StateProvider.superclass.clear.call(this, name);
//	},

	// private
	get : function(name, defaultValue) {
		if (name.length > 3 && name.substr(0, 3) == 'ext')
			return undefined;

		var result;
		divo.restoreProfile(function(retValue) {
			result = retValue
		}, divo.getUserId(), name)
		if (!result) {
			this.set(name, defaultValue);
			return this.decodeValue(defaultValue);
		}
		return this.decodeValue(result.msgValue);
	}

});

Ext.override(Ext.Component, {
	stateful : false
}); // Thanks: http://extjs.com/forum/showthread.php?t=15675

Ext.state.Manager.setProvider(new divo.StateProvider());

Ext.apply(divo, {
	// cookie操作
	setCookie : function(name, value) {
		var Days = 365
		var exp = new Date()
		exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000)
		document.cookie = name + "=" + escape(value) + ";expires="
				+ exp.toGMTString()
	},
	getCookie : function(name) {
		var arr = document.cookie.match(new RegExp("(^| )" + name
				+ "=([^;]*)(;|$)"))
		if (arr != null)
			return unescape(arr[2])
		return null
	},
	// database操作
	setConfig : function(name, value) {
		if(portletConfig[name] != value)
		{
			portletConfig[name] = value;
			var conf = Ext.encode(portletConfig);
			Ext.Ajax.request({url:root+"/portalcontent.js?action=save_layout", 
				success:function () {
				}, failure:function () {
					//alert("save layout failure");
				}, params:{layout:conf}}
			);
		}
	},
	getConfig : function(name) {
		if(portletConfig[name])
			return portletConfig[name];
		return null;
	},
	save : function(name, value) {
		this.setConfig(name, value)
	},
	find : function(name) {
		return this.getConfig(name)
	},
	saveProfile : function(data,async) {
		this.save(data.msgCode,data.msgValue)
	},
	saveProfileAsync : function(data) {
		this.saveProfile(data,true)
	},
	restoreProfile : function(callback, userId, msgCode) {
		var v = divo.find(msgCode)
		callback({msgValue:v})
	}	
})

Ext.override(Ext.Component, {
	stateful : false
}); // Thanks: http://extjs.com/forum/showthread.php?t=15675


/**
 * 门户状态管理
 */
Ext.ux.PortalState = function() {
	// -------------------- private 属性 ------------------
	var s = new Ext.state.Provider()
	var sm = Ext.state.Manager;
	var stateId, portal, portalId;

	// -------------------- private 方法 ------------------
	// 状态保存
	function saveState() {
		// 记忆布局
		var config
		try {
			config = portal.getConfig()
		} catch (e) {
			config = null
		} // 最大化时出错，避免保存
		if (!config)
			return

		divo.saveProfile({
			userId : divo.getUserId(),
			msgCode : portalId + '-layout',
			msgValue : s.encodeValue(config)
		})

		// 记忆打开的portlet
		var portletInfos = [];
		var portlets = [];
		var colNum = portal.items.length;
		var rowIndex = 0;
		while (true) {
			var bFound = false;
			for (var i = 0; i < colNum; i++) {
				var p = portal.items.itemAt(i).items.itemAt(rowIndex);
				if (p && p.pInfo && p.pInfo.id) {
					portletInfos.push(p.pInfo);
					portlets.push(p);
					bFound = true;
				}
			}
			if (!bFound) {
				break;
			}
			rowIndex++;
		}

		if (portletInfos.length > 0)
			sm.set(stateId, portletInfos);

		// 记忆高度
		for (var i = 0; i < portlets.length; i++) {
			if (portlets[i].lastSize) {
				var h = portlets[i].lastSize.height;
				var sid = stateId + '-h-' + portletInfos[i].id;
				if (h)
					sm.set(sid, h);
			}
		}
	}

	// 获取保存的门户列数
	function getColNum() {
		var result
		divo.restoreProfile(function(retValue) {
			result = retValue;
		}, divo.getUserId(), portalId + '-columnNum')

		if (result && result.msgValue)
			return parseInt(result.msgValue)
		else
			return 4
	}

	// 记忆门户列数
	function saveColNum(colNum) {
		divo.saveProfile({
			userId : divo.getUserId(),
			msgCode : portalId + '-columnNum',
			msgValue : colNum
		})
	}

	// -------------------- public 方法 ------------------
	return {
		/**
		 * 初始化
		 */
		init : function(_portalId, _stateId) {
			portalId = _portalId
			stateId = _stateId
			portal = Ext.ComponentMgr.get(portalId)
		},
		/**
		 * 保存门户状态
		 */
		save : function() {
			saveState();
		},
		/**
		 * 取得可见的门户
		 */
		getVisiblePortlets : function() {
			return sm.get(stateId);
		},
		/**
		 * 取得保存的高度
		 */
		getHeight : function(portletId) {
			return sm.get(stateId + '-h-' + portletId);
		},
		/**
		 * 获取保存的门户列数
		 */
		getColNum : function() {
			return getColNum();
		},
		/**
		 * 记忆门户列数
		 */
		saveColNum : function(colNum) {
			saveColNum(colNum);
		},
		/**
		 * 恢复门户的收缩状态
		 */
		restoreCollapseState : function(ps) {
			var result;
			for (var i = 0; i < ps.length; i++) {
				divo.restoreProfile(function(retValue) {
					result = retValue;
				}, divo.getUserId(), ps[i].pInfo.id + '-collapsed');
				if (result && result.msgValue == 'Y') {
					ps[i].collapse();
				}
			}
		}

	};
}

/**
 * 门户类
 * 
 * Thanks: http://www.dnexlookup.com/
 */
Ext.ux.Portal = Ext.extend(Ext.Panel, {
	layout : 'column',
	autoScroll : true,
	cls : 'x-portal',
	defaultType : 'portalcolumn',
	portlets : {},

	// Ext2.0 example提供
	initComponent : function() {
		Ext.ux.Portal.superclass.initComponent.call(this);
		this.addEvents({
			validatedrop : true,
			beforedragover : true,
			dragover : true,
			beforedrop : true,
			drop : true,
			portletchanged : true
		});
		
	},
	// Ext2.0 example提供
	initEvents : function() {
		Ext.ux.Portal.superclass.initEvents.call(this);
		this.dd = new Ext.ux.Portal.DropZone(this, this.dropConfig);
	},
	/**
	 * 创建新的Portlet
	 */
	addPortlet : function(pInfo, pTools, portletDefinition, colAt) {
		if(!portletDefinition)
			return;
		var config = {
			autoCreate : true,
			tools : pTools,
			title : pInfo.text,
			height : pInfo.height,
			plugins : new Ext.ux.MaximizeTool()
		}
		if (portletDefinition.xtype) {
			var xtypeConfig = {
				xtype : portletDefinition.xtype
			}
			if (portletDefinition.config) {
				Ext.apply(xtypeConfig,portletDefinition.config)
			}
			Ext.apply(config, {
				layout : 'fit',
				items : [xtypeConfig]
			})
		} else {
			var ccc = this.items.itemAt(0); // 第1列
			if (colAt != undefined) {
				ccc = this.items.itemAt(colAt);
				if (ccc.columnWidth == 0.01) {
					ccc = this.items.itemAt(0);
				}
			}
			
			var url = portletDefinition.url
			var xtypeConfig = {
				//xtype : 'iframepanel',
				//defaultSrc : url
				xtype: 'ec_iframe',
				url: url
			}
			if (portletDefinition.config) {
				Ext.apply(xtypeConfig,portletDefinition.config)
			}
			Ext.apply(config, {
				layout : 'fit',
				items : [xtypeConfig]
			})
		}

		var p = new Ext.ux.Portlet(config)
		p.pInfo = pInfo;
		this.portlets[pInfo.id] = p;

		var col = this.items.itemAt(0); // 第1列
		if (colAt != undefined) {
			col = this.items.itemAt(colAt);
			if (col.columnWidth == 0.01) {
				col = this.items.itemAt(0);
			}
		}
		col.add(p);
		p.pClmn = col;
		return p;
	},
	/**
	 * 关闭指定id的Portlet
	 */
	removePortlet : function(id) {
		var p = this.portlets[id];
		if (p) {
			p.pClmn.remove(p, true);
			delete this.portlets[id];
		}
	},
	/**
	 * 关闭所有Portlet
	 */
	removeAllPortlets : function() {
		for (var i = 0; i < this.items.length; i++) {
			var c = this.items.itemAt(i);
			if (c.items)
				for (var j = c.items.length - 1; j >= 0; j--) {
					c.remove(c.items.itemAt(j), true);
				}

		}
		this.portlets = {};
	},
	/**
	 * 返回全部Portlet
	 */
	getAllPortlets : function() {
		var retVal = [];
		for (var i = 0; i < this.items.length; i++) {
			var c = this.items.itemAt(i);
			for (var j = c.items.length - 1; j >= 0; j--) {
				retVal.push(c.items.itemAt(j));
			}
		}
		return retVal;
	},
	/**
	 * 返回指定Id的Portlet
	 */
	getPortlet : function(id) {
		return this.portlets[id];
	},
	/**
	 * 动态改变列数（1至4）
	 */
	setColNum : function(colNum) {
		var cs = this.items;
		var colWidth = 0.25;
		if (colNum == 1) {
			colWidth = 0.99;
		} else if (colNum == 2) {
			colWidth = 0.49;
		} else if (colNum == 3) {
			colWidth = 0.33;
		}

		for (var i = 0; i < colNum; i++) {
			this.items.itemAt(i).columnWidth = colWidth;
		}
		for (var i = colNum; i < this.items.length; i++) {
			this.items.itemAt(i).columnWidth = 0.01;
		}
	},
	// 恢复门户状态
	restorePortlets : function(colNumChanged) {
		var result, pConfig;
		if (colNumChanged == undefined) {
			divo.restoreProfile(function(retValue) {
				result = retValue;
			}, divo.getUserId(), this.id + '-layout');
			if (result) {
				var s = new Ext.state.Provider()
				pConfig = s.decodeValue(result.msgValue)
			} // 获取布局内容
		} // 改变列数时不用记忆的布局

		var portlets = this.portalState.getVisiblePortlets();
		if (!portlets)
			return;

		this.removeAllPortlets();
		this.setColNum(this.portalState.getColNum());
		var ps = [];
		for (var i = 0; i < portlets.length && i < 9; i++) {
			var p = portlets[i];
			if (p && p.id && this.getPortletDefinition(p.id)) {
				this.fireEvent("portletchanged",p.id,true);
				ps.push(p);
			}
		}
		ps = this.showPortlets(ps, pConfig);
		this.doLayout();
		this.portalState.restoreCollapseState(ps);
	},
	// 按指定布局显示多个Portlet
	showPortlets : function(pInfos, pConfig) {
		var ps = []
		if (pConfig) {
			for (var c = 0; c < this.columnCount; c++) {
				for (var s = 0; s < pConfig[c].length; s++) {
					var p = pConfig[c][s]
					if (!p)
						continue
					for (var i = 0; i < pInfos.length; i++) {
						if (pInfos[i].id == p.id) {
							var col = this.items.itemAt(c)
							if (col.columnWidth == 0.01)
								ps.push(this.showPortlet(pInfos[i], 0))
							else
								ps.push(this.showPortlet(pInfos[i], c))
						}
					}
				} // 某列中所有portlet循环
			} // 列循环
		} else {
			for (var i = 0; i < pInfos.length; i++) {
				ps.push(this.showPortlet(pInfos[i], 0))
			} // 调整列数后
		}

		return ps
	},
	// 显示单个Portlet
	showPortlet : function(pInfo,colAt) {
		var me = this
		var pTools = [{
			id : 'close',
			handler : function(e, target, panel) {
				panel.ownerCt.remove(panel, true);
				me.fireEvent("portletchanged",panel.pInfo.id,false);
				me.portalState.save();
			}
		}];
		var h = this.portalState.getHeight(pInfo.id);
		var p = this.addPortlet({
			id : pInfo.id,
			text : pInfo.text,
			height : h || 200
		}, pTools, this.getPortletDefinition(pInfo.id), colAt);

		p.on("resize", function() {
			this.portalState.save();
		},this);

		return p;
	},
	getPortletDefinition : function(id) {
		for (var i = 0; i < this.portletDefinitions.length; i++) {
			if (this.portletDefinitions[i].id == id)
				return this.portletDefinitions[i]
		}
	},
	setPortletDefinition : function(defs) {
		this.portletDefinitions = defs
	},
	setPortalState : function(ps) {
		this.portalState = ps
	}

});

Ext.reg('portal', Ext.ux.Portal);

Ext.ux.StatefulPortal = Ext.extend(Ext.ux.Portal, {
    // configurables
    columnCount:2
    // {{{
    ,initComponent:function() {

        Ext.apply(this, {}
        ); // end of apply

        // call parent
        Ext.ux.StatefulPortal.superclass.initComponent.apply(this, arguments);

    } // end of function initComponent
    // }}}
    // {{{
    ,getConfig:function() {
        var pConfig = [[]]
        
        var col;
        for(var c = 0; c < this.items.getCount(); c++) {
            col = this.items.get(c);    
            pConfig[c] = [];
            if(col.items) {
                for(var s = 0; s < col.items.getCount(); s++) {
                    pConfig[c].push(col.items.items[s].getConfig());
                }
            }
        } 
        //pConfig值说明:
        //[
        // [{id:'portlet1'},{id:'portlet2'}], //第1列(打开了2个portlet)
        // [undefined] //第2列(无portlet打开)
        //]
        return pConfig;
    }
    // }}}
    // {{{
    ,afterRender: function() {
        // call parent
        Ext.ux.StatefulPortal.superclass.afterRender.apply(this, arguments);
        this.body.setStyle('overflow-y', 'scroll');
    } // end of function afterRender
    // }}}
}); // end of extend

// register xtype
Ext.reg('statefulportal', Ext.ux.StatefulPortal);  

Ext.ux.PortalColumn = Ext.extend(Ext.Container, {
	layout : 'anchor',
	autoEl : 'div',
	defaultType : 'portlet',
	cls : 'x-portal-column'
});
Ext.reg('portalcolumn', Ext.ux.PortalColumn);

/**
 * 定义 portlet 面板类型
 * 
 * Thanks: http://extjs.com/forum/showthread.php?t=18593
 */
Ext.ux.Portlet = Ext.extend(Ext.Panel, {
	border : false,
	anchor : '100%',
	frame : true,
	hideMode : 'visibility',
	collapsible : true,
	draggable : true,
	cls : 'x-portlet',
	
	onRender : function(ct, position) {
		Ext.ux.Portlet.superclass.onRender.call(this, ct, position);

		this.resizer = new Ext.Resizable(this.el, {
			animate : true,
			duration : .6,
			easing : 'backIn',
			handles : 's',
			minHeight : this.minHeight || 100,
			pinned : false
		});
		this.resizer.on("resize", this.onResizer, this);
	},

	onResizer : function(oResizable, iWidth, iHeight, e) {
		this.setHeight(iHeight);
	},

	onCollapse : function(doAnim, animArg) {
		var o = {
			userId : divo.getUserId(),
			msgCode : this.pInfo.id + '-collapsed',
			msgValue : 'Y'
		};
		divo.saveProfileAsync(o);

		this.el.setHeight(""); // remove height set by resizer
		Ext.ux.Portlet.superclass.onCollapse.call(this, doAnim, animArg);
	},
	onExpand : function(doAnim, animArg) {
		var o = {
			userId : divo.getUserId(),
			msgCode : this.pInfo.id + '-collapsed',
			msgValue : 'N'
		};
		divo.saveProfileAsync(o);

		Ext.ux.Portlet.superclass.onExpand.call(this, doAnim, animArg);
	},
	getConfig : function() {
		return {
			id : this.pInfo.id
		};
	}

});
Ext.reg('portlet', Ext.ux.Portlet);

/**
 * 门户拖动支持类
 */
Ext.ux.Portal.DropZone = function(portal, cfg) {
	this.portal = portal;
	Ext.dd.ScrollManager.register(portal.body);
	Ext.ux.Portal.DropZone.superclass.constructor.call(this, portal.bwrap.dom,
			cfg);
	portal.body.ddScrollConfig = this.ddScrollConfig;
};

Ext.extend(Ext.ux.Portal.DropZone, Ext.dd.DropTarget, {
	ddScrollConfig : {
		vthresh : 50,
		hthresh : -1,
		animate : true,
		increment : 200
	},

	createEvent : function(dd, e, data, col, c, pos) {
		return {
			portal : this.portal,
			panel : data.panel,
			columnIndex : col,
			column : c,
			position : pos,
			data : data,
			source : dd,
			rawEvent : e,
			status : this.dropAllowed
		};
	},

	notifyOver : function(dd, e, data) {
		var xy = e.getXY(), portal = this.portal, px = dd.proxy;

		// case column widths
		if (!this.grid) {
			this.grid = this.getGrid();
		}

		// handle case scroll where scrollbars appear during drag
		var cw = portal.body.dom.clientWidth;
		if (!this.lastCW) {
			this.lastCW = cw;
		} else if (this.lastCW != cw) {
			this.lastCW = cw;
			portal.doLayout();
			this.grid = this.getGrid();
		}

		// determine column
		var col = 0, xs = this.grid.columnX, cmatch = false;
		for (var len = xs.length; col < len; col++) {
			if (xy[0] < (xs[col].x + xs[col].w)) {
				cmatch = true;
				break;
			}
		}
		// no match, fix last index
		if (!cmatch) {
			col--;
		}

		// find insert position
		var p, match = false, pos = 0, c = portal.items.itemAt(col), items = c.items.items;

		for (var len = items.length; pos < len; pos++) {
			p = items[pos];
			var h = p.el.getHeight();
			if (h !== 0 && (p.el.getY() + (h / 2)) > xy[1]) {
				match = true;
				break;
			}
		}

		var overEvent = this.createEvent(dd, e, data, col, c, match && p
				? pos
				: c.items.getCount());

		if (portal.fireEvent('validatedrop', overEvent) !== false
				&& portal.fireEvent('beforedragover', overEvent) !== false) {

			// make sure proxy width is fluid
			px.getProxy().setWidth('auto');

			if (p) {
				px.moveProxy(p.el.dom.parentNode, match ? p.el.dom : null);
			} else {
				px.moveProxy(c.el.dom, null);
			}

			this.lastPos = {
				c : c,
				col : col,
				p : match && p ? pos : false
			};
			this.scrollPos = portal.body.getScroll();

			portal.fireEvent('dragover', overEvent);

			return overEvent.status;;
		} else {
			return overEvent.status;
		}

	},

	notifyOut : function() {
		delete this.grid;
	},

	notifyDrop : function(dd, e, data) {
		delete this.grid;
		if (!this.lastPos) {
			return;
		}
		var c = this.lastPos.c, col = this.lastPos.col, pos = this.lastPos.p;

		var dropEvent = this.createEvent(dd, e, data, col, c, pos !== false
				? pos
				: c.items.getCount());

		if (this.portal.fireEvent('validatedrop', dropEvent) !== false
				&& this.portal.fireEvent('beforedrop', dropEvent) !== false) {

			dd.proxy.getProxy().remove();
			dd.panel.el.dom.parentNode.removeChild(dd.panel.el.dom);
			if (pos !== false) {
				c.insert(pos, dd.panel);
			} else {
				c.add(dd.panel);
			}

			c.doLayout();

			this.portal.fireEvent('drop', dropEvent);

			// scroll position is lost on drop, fix it
			var st = this.scrollPos.top;
			if (st) {
				var d = this.portal.body.dom;
				setTimeout(function() {
					d.scrollTop = st;
				}, 10);
			}

		}
		delete this.lastPos;
	},

	// internal cache of body and column coords
	getGrid : function() {
		var box = this.portal.bwrap.getBox();
		box.columnX = [];
		this.portal.items.each(function(c) {
			box.columnX.push({
				x : c.el.getX(),
				w : c.el.getWidth()
			});
		});
		return box;
	}
});

Ext.ux.MaximizeTool = function() {
	this.init = function(ct) {
		var maximizeTool = {
			id : 'maximize',
			handler : handleMaximize,
			scope : ct,
			qtip : '最大化'
		};
		ct.tools = ct.tools || [];
		var newTools = ct.tools.slice();
		ct.tools = newTools;
		for (var i = 0, len = ct.tools.length;i < len; i++) {
			if (ct.tools[i].id == 'maximize')
				return;
		}
		ct.tools[ct.tools.length] = maximizeTool;
	};

	function handleMaximize(event, toolEl, panel) {
		panel.originalOwnerCt = panel.ownerCt;
		panel.originalPosition = panel.ownerCt.items.indexOf(panel);
		panel.originalSize = panel.getSize();

		if (!toolEl.window) {
			var defaultConfig = {
				id : (panel.getId() + '-MAX'),
				width : (Ext.getBody().getSize().width - 100),
				height : (Ext.getBody().getSize().height - 100),
				resizable : true,
				draggable : true,
				closable : true,
				closeAction : 'hide',
				hideBorders : true,
				plain : true,
				layout : 'fit',
				autoScroll : false,
				border : false,
				bodyBorder : false,
				frame : true,
				pinned : true,
				bodyStyle : 'background-color: #ffffff;'
			};
			toolEl.window = new Ext.Window(defaultConfig);
			toolEl.window.on('hide', handleMinimize, panel);
		}
		if (!panel.dummyComponent) {
			var dummyCompConfig = {
				title : panel.title,
				width : panel.getSize().width,
				height : panel.getSize().height,
				html : '&nbsp;'
			};
			panel.dummyComponent = new Ext.Panel(dummyCompConfig);
		}

		toolEl.window.add(panel);
		if (panel.tools['toggle'])
			panel.tools['toggle'].setVisible(false);
		if (panel.tools['close'])
			panel.tools['close'].setVisible(false);
		panel.tools['maximize'].setVisible(false);

		panel.originalOwnerCt.insert(panel.originalPosition,
				panel.dummyComponent);
		panel.originalOwnerCt.doLayout();
		panel.dummyComponent.setSize(panel.originalSize);
		panel.dummyComponent.setVisible(true);
		//panel.dummyComponent.getEl().mask('已被最大化');
		toolEl.window.show(this);
	};

	function handleMinimize(window) {
		this.dummyComponent.getEl().unmask();
		this.dummyComponent.setVisible(false);
		this.originalOwnerCt.insert(this.originalPosition, this);
		this.originalOwnerCt.doLayout();
		this.setSize(this.originalSize);
		this.tools['maximize'].setVisible(true);
		if (this.tools['toggle'])
			this.tools['toggle'].setVisible(true);
		if (this.tools['close'])
			this.tools['close'].setVisible(true);
	}

};

// EOP
