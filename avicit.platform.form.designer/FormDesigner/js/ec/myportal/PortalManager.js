/**
 * 门户管理器
 */
divo.admin.portal.PortalManager = function(_portletPre) {
	// ----------------- 私有属性 ----------------------
/*
	var portletDefinitions = [{
		id : 'google',
		text : 'Google Home',
		url : "http://www.google.cn/"
	}];
*/

	var portletPre = _portletPre
	var colNumCombo, portletMenu
	var portalId, portalState, portal
//	var portletDefinitions = _portletDefinitions;
	
	// ----------------- 私有方法 ----------------------
	// 在portletDefinitions中添加前缀
	function adjustPortletDefinitions() {
		for (var i = 0; i < portletDefinitions.length; i++) {
			portletDefinitions[i].id = portletPre + portletDefinitions[i].id
		}
	}

	// 创建门户管理用工具条控件
	function createToolbar() {
		var tb = []
		tb.push('->')

		portletMenu = new Ext.ux.ColumnMenu({
        	columnHeight: 100,
        	columnWidth: 148,
			id : portletPre + 'menu',
			items : getPortletMenuItems()
		})
		tb.push({
			text : 'Select Content...',
			iconCls : 'bmenu',
			menu : portletMenu
		})
		tb.push({
			text : 'Columns'
		})

		colNumCombo = new Ext.form.ComboBox({
			store : [[1, '1'], [2, '2'], [3, '3'], [4, '4']],
			typeAhead : true,
			width : 40,
			triggerAction : 'all',
			editable : false,
			selectOnFocus : true
		})
		colNumCombo.on("select",onColNumChanged,this)

		tb.push(colNumCombo)
		return tb
	}

	// 生成内容菜单项
	function getPortletMenuItems() {
		var items = []
		for (var i = 0; i < portletDefinitions.length; i++) {
			items.push({
				text : portletDefinitions[i].text,
				hideOnClick:false,
				checked : false,
				checkHandler : onItemCheck
			})
		}
		return items
	}

	// 选择内容菜单项时
	function onItemCheck(item, checked) {
		var portletId = getPortletId(item.text)
		if (!checked) {
			portal.removePortlet(portletId);
			portal.doLayout();
		} else {
			var p = portal.showPortlet({
				text : item.text,
				id : portletId,
				checked : true
			});
			portal.doLayout();
			portalState.restoreCollapseState([p]);
		}
		portalState.save();
	}

	// 门户窗口关闭时
	function onPortletChanged(portletId, checked) {
		var index = getItemIndex(portletId);
		if(index)
		{
			portletMenu.items.itemAt(index).setChecked(checked, true); //第2个参数表示suppressEvent
		}
	}

	// 栏目数改变时
	function onColNumChanged(o) {
		var colNum = o.getValue()
		if (colNum != portalState.getColNum()) {
			portalState.saveColNum(colNum)
			portal.restorePortlets(true)
		}
	}

	//通过名称获取id
	function getPortletId(text) {
		for (var i = 0; i < portletDefinitions.length; i++) {
			if (portletDefinitions[i].text == text)
				return portletDefinitions[i].id
		}
	}
	
	//通过id获得索引号
	function getItemIndex(id) {
		for (var i = 0; i < portletDefinitions.length; i++) {
			if (portletDefinitions[i].id == id)
				return [i]
		}
	}

	// -------------------- public方法 -----------------------
	return {
		/**
		 * 初始化
		 */
		init : function(_portalId, _portalState) {
			portalId = _portalId
			adjustPortletDefinitions()

			portalState = _portalState;

			portal = Ext.ComponentMgr.get(portalId)
			portal.setPortletDefinition(portletDefinitions)
			portal.setPortalState(portalState)

			portal.on('portletchanged', onPortletChanged, this)
			
			colNumCombo.setValue(portalState.getColNum())
			portal.restorePortlets();
		},
		getTbar : function() {
			return createToolbar()
		}

	};
}


divo.admin.portal.MainPanel = Ext.extend(Ext.Panel, {

	initComponent : function() {
		this.portId = this.portId || 'admin-portal';
		this.portalMgr = new divo.admin.portal.PortalManager(this.portId)
		
		Ext.apply(this, {
			layout : 'fit',
			width:700,
			height:500,
			items : [{
				region : 'center',
				layout : 'fit',
				autoScroll : true,
				tbar : this.portalMgr.getTbar(),
				//ctCls : "j-toolbar-dark",
				items : [{
					xtype : 'statefulportal',
					id : this.portId,
					items : [{
						columnWidth : .25
						//,style : 'padding:10px 0 10px 10px'
					}, {
						columnWidth : .25
						//,style : 'padding:10px 0 10px 10px'
					}, {
						columnWidth : .25
						//,style : 'padding:10px 0 10px 10px'
					}, {
						columnWidth : .25
						//,style : 'padding:10px 0 10px 10px'
					}]
				}]
			}]
		})

		divo.admin.portal.MainPanel.superclass.initComponent.call(this)
	},
	onRender : function(){
		this.initPortal();
        divo.admin.portal.MainPanel.superclass.onRender.apply(this, arguments);
	},
	initPortal : function() {
		this.portalState = new Ext.ux.PortalState()
		this.portalState.init(this.portId,this.myId("state"))
		
		this.portalMgr.init(this.portId, this.portalState)
	
		Ext.ComponentMgr.get(this.portId).on("drop", function() {
			this.portalState.save()
		},this)
	}
})

Ext.reg('portalpanel', divo.admin.portal.MainPanel);

Ext.ux.ColumnMenu = function(config) {
	Ext.apply(this,config);
    Ext.ux.ColumnMenu.superclass.constructor.call(this, config);
};

Ext.extend( Ext.ux.ColumnMenu , Ext.menu.Menu , {
    /**
     * @cfg {Number} columnHeight The max height of a menu column in pixels (defaults to 300)
     */
	columnHeight: 300,
    /**
     * @cfg {Number} columnWidth The width of a menu column in pixels (defaults to 180)
     */
	columnWidth: 180,
	
	// private
    render: function() {
        if ( this.el ) {
            return;
        }
        var el = this.el = this.createEl();

        if ( !this.keyNav ) {
            this.keyNav = new Ext.menu.MenuNav( this );
        }
        
        if ( this.plain ) {
            el.addClass("x-menu-plain");
        }
        if ( this.cls ) {
            el.addClass( this.cls );
        }

        var focusEl = this.focusEl = el.createChild({
            cls: "x-menu-focus",
            href: "#",
            onclick: "return false;",
            tabIndex:"-1",
            tag: "a"
        });
        
        el.setStyle({
			'background': '',
			'margin': '0',
        	'padding': '0'
        });
        
        var containerEl = this.containerEl = el.createChild({
            cls: "x-column-menu",
            tag: "div"
        });

		var columnEl = null;
        var ul = null;
        var li = null;

        this.items.each(function( item , index , length ) {
            if ( ul === null || ul.getHeight() >= this.columnHeight ) {
	            columnEl = containerEl.createChild({
	                cls: "x-menu-list",
	                tag: "div"
	            });
	            
	            ul = columnEl.createChild({
	                style: "width: " + this.columnWidth + "px;",
	                tag: "ul"
	            });
	            
	            ul.on("click", this.onClick, this);
	            ul.on("mouseover", this.onMouseOver, this);
	            ul.on("mouseout", this.onMouseOut, this);
	            
	            this.ul = ul;
			}
            
            li = document.createElement("li");
            li.className = "x-menu-list-item";
            
            ul.dom.appendChild( li );
            
            item.render( li , this );
		}.createDelegate( this ));
		
		containerEl.child('.x-menu-list:last').setHeight( containerEl.child('.x-menu-list:first').getComputedHeight() );
    }
});
// EOP

