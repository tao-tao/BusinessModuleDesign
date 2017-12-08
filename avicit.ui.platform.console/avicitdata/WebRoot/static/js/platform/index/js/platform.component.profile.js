/**
 * @author liuwei@avicit.com
 * @version 1.0 组件个性化设置
 */
var platform = function(){};
platform.ComponentProfile = function() {};
/**
 * @author tode.yu@bstek.com
 * @version 1.0 实现将客户端的DataGrid的自定义展现形式保存到服务端，再次打开页面是，加载之前设定的个性化的展现形式
 */
platform.ComponentProfile.DataGrid = function() {
	saveDataGridComponent = function(view, dataGridId) {
		var list = new Array();
		var columns = view.id(dataGridId).get("columns");
		for ( var i = 0; i < columns.size; i++) {
			var column = columns.get(i);
			if (column instanceof dorado.widget.grid.DataColumn) {
				buildDataColumnEntity(column, list, i);
			} else if (column instanceof dorado.widget.grid.ColumnGroup) {
				saveColumnGroup(column, list, i);
			}
		}
		var action = view.id("$ajaxActionDataGridComponentProfileSave");
		if (!action) {
			action = new dorado.widget.AjaxAction({
				service : "platform.dataGridProfileListener#saveComponent",
				id : "$ajaxActionDataGridComponentProfileSave",
				confirmMessage : "确定要保存当前自定义表格数据结构？",
				executeMessage : "保存数据表格结构中...",
				successMessage : "保存数据表格自定义结构成功",
				view : view
			});
			view.addChild(action);
		}
		action.set("parameter", {
			dataGridId : dataGridId,
			componentProfiles : list,
			viewId : view.get("name")
		});
		action.execute(list);
	};

	saveColumnGroup = function(column, list, order, parentControl) {
		var columns = column.get("columns");
		var c = null;
		for ( var i = 0; i < columns.size; i++) {
			c = columns.get(i);
			if (c instanceof dorado.widget.grid.ColumnGroup) {
				saveColumnGroup(c, list, i, column.get("name"));
			} else {
				buildDataColumnEntity(c, list, i, column.get("name"));
			}
		}
		buildGroupColumnEntity(column, list, order, parentControl);
	};

	buildDataColumnEntity = function(column, list, order, parentControl) {
		var entity = {
			controlId : column.get("name"),
			controlName : column.get("name"),
			caption : column.get("caption"),
			visible : column.get("visible"),
			order : order
		};
		if (parentControl) {
			entity.parentControl = parentControl;
		}
		if (column instanceof dorado.widget.grid.IndicatorColumn) {
			entity.controlType = "IndicatorColumn";
		} else if (column instanceof dorado.widget.grid.RowNumColumn) {
			entity.controlType = "RowNumColumn";
		} else if (column instanceof dorado.widget.grid.RowSelectorColumn) {
			entity.controlType = "RowSelectorColumn";
		} else {
			entity.controlType = "DataColumn";
			entity.width = column.get("width");
		}
		list.push(entity);
	};
	buildGroupColumnEntity = function(column, list, order, parentControl) {
		var entity = {
			controlId : column.get("name"),
			controlName : column.get("name"),
			controlType : "ColumnGroup",
			caption : column.get("caption"),
			visible : column.get("visible"),
			order : order
		};
		if (parentControl) {
			entity.parentControl = parentControl;
		}
		list.push(entity);
	};
};

/**
 * 保存当前Grid的设定
 * 
 * @param view
 *            Grid所在的视图
 * @param dataGridId
 *            Grid的Id
 */
platform.ComponentProfile.DataGrid.prototype.saveGridSet = function(view, dataGridId) {
	saveDataGridComponent(view, dataGridId);
};

/**
 * 重置Grid的设定，恢复到系统默认状态
 * 
 * @param view
 *            Grid所在的视图
 * @param dataGridId
 *            grid的Id
 */
platform.ComponentProfile.DataGrid.prototype.resetGrid = function(view, dataGridId) {
	var action = view.id("$ajaxActionResetDataGridComponentProfile");
	if (!action) {
		action = new dorado.widget.AjaxAction({
			service : "platform.dataGridProfileListener#doResetDataGrid",
			id : "$ajaxActionResetDataGridComponentProfile",
			confirmMessage : "确定要重置数据表格的自定义结构？",
			executeMessage : "重置表格默认展现形式...",
			successMessage : "重置成功",
			view : view
		});
		view.addChild(action);
	}
	action.set("parameter", {
		dataGridId : dataGridId,
		viewId : view.get("name")
	});
	action.execute();
};

platform.ComponentProfile.AutoForm = function() {
	this.view = null;
	this.autoFormId = null;
	this.autoForm = null;
	this.config = null;
	this.cols = null;
	this.autoFormConfigProfileCols = null;
	this.ajaxActionSaveAutoFormProfile = null;
	this.ajaxActionResetAutoFormProfile = null;
	this.dialogSubAutoFormProfile = null;
};
platform.ComponentProfile.AutoForm.prototype.saveAutoFormSet = function(view,
		autoFormId, config) {
	this.autoForm = view.id(autoFormId);
	if (!this.autoForm) {
		dorado.MessageBox.alert("未在当前视图中找到AutoForm[" + autoFormId + "]");
		return;
	}
	this.view = view;
	this.autoFormId = autoFormId;
	this.config = config;
	var caption = "AutoForm[" + this.autoForm.get("id") + "]表格显示设定";
	var buttons = this.createDialogButtons();
	var grid = this.createMemberConfigGrid();
	processDialog = new dorado.widget.Dialog({
		width : 600,
		height : 300,
		center : true,
		closeAction : "close",
		status : "hidden",
		maximizeable : true,
		modal : true,
		caption : caption,
		modal : true,
		center : true,
		status : "hidden",
		buttonAlign : "center",
		resizable : true,
		view : view,
		buttons : buttons
	});
	this.dialog = processDialog;
	processDialog.addChild(grid);
	processDialog.show();
};
platform.ComponentProfile.AutoForm.prototype.resetAutoForm = function(view,
		autoFormId) {
	this.view = view;
	this.autoForm = view.id(autoFormId);
	this.autoFormId = autoFormId;
	this.gridAutoFormProfile = null;
	this.dialog = null;
	this.resetAutoFormConfig();
};

platform.ComponentProfile.AutoForm.prototype.parseAutoForm = function() {
	var results = new Array();
	var elements = this.autoForm.get("elements");
	var element = null;
	var colSpan = 1;
	var rowSpan = 1;
	var dataType = null;
	if (this.autoForm.get("dataSet")) {
		dataType = this.autoForm.get("dataSet").get("dataType");
	} else if (this.autoForm.get("dataType")) {
		dataType = this.autoForm.get("dataType");
	}
	var dataTypes = null;
	var propertyDef = null;
	if (dataType) {
		var propertyDefs = dataType.get("propertyDefs");
		dataTypes = new dorado.util.Map();
		for ( var i = 0; i < propertyDefs.size; i++) {
			dataTypes.put(propertyDefs.get(i).get("name"), propertyDefs.get(i));
		}
	}
	for ( var i = 0; i < elements.size; i++) {
		var label = null;
		element = elements.get(i);
		if (element.get("layoutConstraint")) {
			colSpan = element.get("layoutConstraint").colSpan;
		}
		if (element.get("layoutConstraint")) {
			rowSpan = element.get("layoutConstraint").rowSpan;
		}
		if (isNaN(colSpan)) {
			colSpan = 1;
		}
		if (isNaN(rowSpan)) {
			rowSpan = 1;
		}
		if (!label) {
			if (element.get("property")) {
				propertyDef = dataTypes.get(element.get("property"));
				if (propertyDef) {
					label = propertyDef.get("label");
				} else {
					label = "";
				}
			} else {
				label = "";
			}
		}
		results.push({
			name : element.get("name"),
			label : label,
			colSpan : colSpan,
			rowSpan : rowSpan,
			visible : element.get("visible")
		});
	}
	return results;
};
platform.ComponentProfile.AutoForm.prototype.getAjaxActionSaveAutoFormProfile = function() {
	this.ajaxActionSaveAutoFormProfile = new dorado.widget.AjaxAction({
		service : "platform.autoFormProfileListener#saveAutoFormProfile",
		confirmMessage : "确定要保存当前表单自定义设定?",
		successMessage : "保存表单自定义设定成功",
		executeMessage : "保存表单自定义设定中..."
	});
	return this.ajaxActionSaveAutoFormProfile;
};
platform.ComponentProfile.AutoForm.prototype.getAjaxActionResetAutoFormProfile = function() {
	this.ajaxActionResetAutoFormProfile = new dorado.widget.AjaxAction({
		service : "platform.autoFormProfileListener#doResetAutoFormProfile",
		confirmMessage : "确定要重置当前设定的表单显示配置，使用系统默认显示?",
		successMessage : "重置表单默认设定成功",
		executeMessage : "重置表单默认设定中..."
	});
	return this.ajaxActionResetAutoFormProfile;
};
platform.ComponentProfile.AutoForm.prototype.createDialogButtons = function() {
	var buttons = new Array();
	var caller = this;
	var button = new dorado.widget.Button({
		caption : "设定表单列数",
		icon : ">avicit/platform6/modules/system/css/images/accept.png",
		listener : {
			onClick : function(self, arg) {
				caller.invokeSubDialog();
			}
		}
	});
	buttons.push(button);

	button = new dorado.widget.Button(
			{
				caption : "使用默认配置",
				icon : ">avicit/platform6/modules/system/css/images/arrow_rotate_clockwise.png",
				listener : {
					onClick : function(self, arg) {
						caller.resetAutoFormConfig();
					}
				}
			});
	buttons.push(button);

	var button = new dorado.widget.Button({
		caption : "确定",
		icon : ">avicit/platform6/modules/system/css/images/accept.png",
		listener : {
			onClick : function(self, arg) {
				caller.saveAutoFormconfig();
			}
		}
	});
	buttons.push(button);

	button = new dorado.widget.Button({
		caption : "取消",
		icon : ">avicit/platform6/modules/system/css/images/cancel.png",
		listener : {
			onClick : function(self, arg) {
				caller.dialog.hide();
			}
		}
	});
	buttons.push(button);

	return buttons;
};
platform.ComponentProfile.AutoForm.prototype.createMemberConfigGrid = function() {
	var grid = new dorado.widget.Grid({
		draggable : true,
		droppable : true,
		dragMode : "item",
		view : this.view,
		dropMode : "onOrInsertItems",
		dragTags : "form",
		droppableTags : "form",
		layoutConstraint : {
			type : "center"
		},
		columns : [ {
			$type : "RowNum",
			name : "rowNum"
		}, {
			name : "name",
			property : "name",
			caption : "元素名称",
			readOnly : true
		}, {
			name : "label",
			property : "label",
			caption : "元素标题"
		}, {
			name : "colSpan",
			property : "colSpan",
			dataType : "int",
			width : 60,
			caption : "所占列数"
		}, {
			name : "rowSpan",
			property : "rowSpan",
			dataType : "int",
			width : 60,
			caption : "所占行数"
		}, {
			name : "visible",
			property : "visible",
			dataType : "boolean",
			width : 60,
			caption : "是否显示"
		} ]
	});
	this.gridAutoFormProfile = grid;
	var datas = this.parseAutoForm();
	grid.set("items", datas);
	return grid;
};
platform.ComponentProfile.AutoForm.prototype.createAutoFormConfig = function() {
	this.autoFormConfigProfileCols = new dorado.widget.AutoForm({
		cols : "*",
		labelAlign : "right",
		labelWidth : 40,
		layoutConstraint : {
			type : "top"
		}
	});
	this.autoFormConfigProfileCols.addElement({
		name : "cols",
		label : "列数",
		layoutConstraint : {
			colSpan : 1
		},
		text : this.autoForm.get("cols"),
		editor : {
			$type : "TextEditor",
			value : this.autoForm.get("cols")
		}
	});
	return this.autoFormConfigProfileCols ;
};
platform.ComponentProfile.AutoForm.prototype.invokeSubDialog = function() {
	var autoFormConfig = this.createAutoFormConfig();
	this.dialogSubAutoFormProfile = new dorado.widget.Dialog({
		width : 300,
		height : 150,
		center : true,
		closeAction : "hide",
		status : "hidden",
		maximizeable : true,
		modal : true,
		caption : "AutoForm表格列数设定",
		modal : true,
		center : true,
		status : "hidden",
		buttonAlign : "center",
		resizable : true
	});

	var caller = this;
	var buttons = new Array();
	var button = new dorado.widget.Button({
		caption : "确定",
		icon : ">avicit/platform6/modules/system/css/images/accept.png",
		listener : {
			onClick : function(self, arg) {
				var form = autoFormConfig;
				caller.cols = form.get("entity").cols;
				caller.dialogSubAutoFormProfile.hide();
			}
		}
	});
	buttons.push(button);
	button = new dorado.widget.Button({
		caption : "取消",
		icon : ">avicit/platform6/modules/system/css/images/cancel.png",
		listener : {
			onClick : function(self, arg) {
				caller.dialogSubAutoFormProfile.hide();
			}
		}
	});
	buttons.push(button);
	this.dialogSubAutoFormProfile.set("buttons", buttons);
	this.dialogSubAutoFormProfile.addChild(autoFormConfig);
	this.dialogSubAutoFormProfile.show();
};

platform.ComponentProfile.AutoForm.prototype.saveAutoFormconfig = function() {
	var dialog = this.dialog;
	var grid = this.gridAutoFormProfile;
	var items = grid.get("items");
	var updateDatas = new Array();
	for ( var i = 0; i < items.length; i++) {
		if (items[i].colSpan < 1 || items[i].rowSpan < 1) {
			dorado.MessageBox.alert("表单元素的所占行和所占列不能是小于1的数");
			return;
		}
		updateDatas.push({
			order : i,
			controlName : items[i].name,
			caption : items[i].label,
			colSpan : parseInt(items[i].colSpan),
			rowSpan : parseInt(items[i].rowSpan),
			visible : items[i].visible
		});
	}
	var action = this.getAjaxActionSaveAutoFormProfile();
	var hideMode = "visibility";
	if (this.config && this.config.hideMode) {
		hideMode = this.config.hideMode;
	}

	var cols = this.autoForm.get("cols");
	if (!cols) {
		cols = "*,*";
	}
	if (this.cols) {
		cols = this.cols;
	}
	action.set("parameter", {
		autoFormElements : updateDatas,
		viewId : this.view.get("name"),
		autoFormId : this.autoFormId,
		cols : cols,
		hideMode : hideMode
	});
	action.execute(function() {
		dialog.hide();
	});
};
platform.ComponentProfile.AutoForm.prototype.resetAutoFormConfig = function() {
	var dialog = this.dialog;
	var action = this.getAjaxActionResetAutoFormProfile();
	action.set("parameter", {
		viewId : this.view.get("name"),
		autoFormId : this.autoFormId
	});
	action.execute(function() {
		dialog.hide();
	});
};
