
Ec.WrapDataView = Ext.extend(Ext.Panel, {
	paging: false,
			initComponent : function(panel) {
				if (!this.fields) {
					this.fields = [{
								name : 'id',
								type : 'string'
							}];
				}
				this.store = new Ext.data.Store({
					proxy : new Ext.data.HttpProxy({
								url : '',
								method : 'post'
							}),
					reader : new Ec.ArrayReader({
								totalProperty : 'totalCount',
								root : 'list'
							}, Ext.data.Record.create(this.fields))
				});
				if(this.paging)
				{
					this.pagingConfig = {
						xtype : 'paging',
						pageSize : this.pageSize || 10,
						displayInfo : true,
						displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
						emptyMsg : '<b>0</b> 条记录'
					};
					this.pagingConfig.store = this.store;
					this.bbar = new Ext.PagingToolbar(this.pagingConfig); 
				}
				this.viewConfig.store = this.store;
				this.items = this.viewConfig;
				Ec.WrapDataView.superclass.initComponent.call(this);
			},
//			afterRender: function(){
//				Ec.WrapDataView.superclass.afterRender.call(this);
//				this.view = this.items.items[0];
//				if(this.view)
//					this.view.addClass('overflow');
//			},
			setURI : function(uri, params, ref, target) {
				this._uri = uri;
				// this.store.proxy = new Ext.data.HttpProxy({url: this._uri});
				// this.store.baseParams = {data_event:'paging', this.name,
				// params)
				this.store.proxy = new Ext.data.HttpProxy({
							url : this._uri
									+ '?'
									+ Ec.Request.DataEventParams('paging',
											this.name, params)
						});
				if (ref) {
					if (ref.length == 1 && ref[0] == '')
						return;
					this.ref = ref;
					this.target = target;
					this.store.on('beforeload', this.getReference, this);
				}
			},
			setValue : function(values) {
				if (!values)
					return;
				var list = values.list ? values.list : values;
				if (values.selList)
					this.recordIds = values.selList;
				this.store.loadData(values);
				this.store.addListener('loadexception', function() {
							Ec.Utils.msg('Error', 'Load grid data error.');
						});
			},
			getReference : function(store, options) {
				if (this.ref) {
					options = options || {};
					options.params = options.params || {};
					for (var i = 0; i < this.ref.length; i++) {
						var refField = this.ref[i];
						var refParam;
						if (refField.indexOf(':') > 0) {
							refParam = refField.substr(refField.indexOf(':')
									+ 1);
							refField = refField
									.substr(0, refField.indexOf(':'));
						}

						var fields = Ec.Utils.find(refField, this.target);
						if (fields.length > 0) {
							for (var j = 0; j < fields.length; j++) {
								options.params[refField] = Ec.Utils
										.getValueString(fields[j], refParam);// .getValue(refParam);
							}
						} else if (refField != '') {
							var comp = Ext.getCmp(refField);
							if (comp && comp.getValue)
								options.params[refField] = comp
										.getValue(refParam);
						}
					}
				}
			}
			
		});
Ext.reg('ec_wrapdataview', Ec.WrapDataView);
