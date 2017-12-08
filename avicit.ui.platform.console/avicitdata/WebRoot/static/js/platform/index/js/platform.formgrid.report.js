var platform = function(){};
platform.Report=function(){};

platform.Report.prototype.showFormReportDialog=function(view,formId,initFormData,showPdfButton,showExcelButton){
	this.view=view;
	this.showPdfButton=showPdfButton;
	this.showExcelButton=showExcelButton;
	var tabControl = new dorado.widget.TabControl();
	
	var basicSettingForm = _createTitleInfoForm(initFormData);
	var basicTab = new dorado.widget.tab.ControlTab({
		caption :"标题属性设置",
		control : basicSettingForm
	});
	tabControl.addTab(basicTab);

	var formStyleFormSetting = _createFormStyleForm(initFormData);
	var formStyleTab = new dorado.widget.tab.ControlTab({
		caption :"表单属性设置",
		control : formStyleFormSetting
	});
//	tabControl.addTab(formStyleTab);

	var obj=this;
	var buttons=[];
	if(this.showPdfButton){
		var pdfButton=new dorado.widget.Button({
			caption:"导出PDF",
			icon:">avicit/platform6/modules/system/css/images/folder_table.png",
			listener:{
				onClick:function(){
					var basicInfo = basicSettingForm.get("entity");
					var formConfig = formStyleFormSetting.get("entity");
					var config = {
							showTitle : basicInfo.showTitle,
							title : basicInfo.title,
							fontColor : basicInfo.fontColor,
							fontSize : basicInfo.fontSize,
							bgColor : basicInfo.bgColor,
							labelAlign : formConfig.labelAlign,
							dataAlign : formConfig.dataAlign,
							dataStyle : formConfig.dataStyle
					};
					obj.generatePdfFormReport(view,formId, config,false);
				}
			}
			
		});
		buttons.push(pdfButton);
	}
	if(this.showExcelButton){
		var excelButton=new dorado.widget.Button({
			caption:"导出Excel",
			icon:">avicit/platform6/modules/system/css/images/page_excel.png",
			listener:{
				onClick:function(){
					var basicInfo = basicSettingForm.get("entity");
					var formConfig = formStyleFormSetting.get("entity");
					var config = {
							showTitle : basicInfo.showTitle,
							title : basicInfo.title,
							fontColor : basicInfo.fontColor,
							fontSize : basicInfo.fontSize,
							bgColor : basicInfo.bgColor,
							labelAlign : formConfig.labelAlign,
							dataAlign : formConfig.dataAlign,
							dataStyle : formConfig.dataStyle
					};					
					obj.generateExcelFormReport(view,formId, config);
				}
			}
			
		});
		buttons.push(excelButton);
	}
	var cancelButton=new dorado.widget.Button({
		caption:"取消",
		icon:" url(>skin>common/icons.gif) -220px -240px"
	});
	buttons.push(cancelButton);
	var dialog=new dorado.widget.Dialog({
		width:500,
		height:350,
		center:true,
		modal:true,
		closeAction:"close",
		caption:"导出报表参数设置",
		buttons:buttons
	});
	cancelButton.addListener("onClick",function(){
		dialog.hide();
	});
	dialog.addChild(tabControl);
	dialog.show();
};

platform.Report.prototype.showGridReportDialog=function(view,tableIds,initFormData,showPdfButton,showExcelButton,dataScope,maxSize){
	this.view=view;
	this.showPdfButton=showPdfButton;
	this.showExcelButton=showExcelButton;
	
	var tabControl=new dorado.widget.TabControl();
	
	var basicSettingForm = _createTitleInfoForm(initFormData);
	var basicTab=new dorado.widget.tab.ControlTab({
			caption:"标题属性设置",
			control:basicSettingForm
		});
	tabControl.addTab(basicTab);

	var advanceSettingForm = _createAdvanceSettingForm(initFormData);
	var advanceTab=new dorado.widget.tab.ControlTab({
			caption:"表格属性设置",
			control:advanceSettingForm
		});

	//tabControl.addTab(advanceTab);
	var obj=this;	
	var buttons=[];	
	if(this.showPdfButton){
		var pdfButton=new dorado.widget.Button({
			caption:"导出PDF",
			icon:">avicit/platform6/modules/system/css/images/folder_table.png",
			listener:{
				onClick:function(){
					var basicInfo = basicSettingForm.get("entity");
					var dataStyle = advanceSettingForm.get("entity");
					var config = {
						titleInfos : {
							showTitle : basicInfo.showTitle,
							title : basicInfo.title,
							fontColor : basicInfo.fontColor,
							fontSize : basicInfo.fontSize,
							bgColor : basicInfo.bgColor
						},
						columnHeaderStyle : {
							headerBgColor : dataStyle.headerBgColor,
							headerFontColor : dataStyle.headerFontColor,
							headerFontSize : dataStyle.headerFontSize,
							headerAlign : dataStyle.headerAlign								
						},
						gridDataStyle : {
							bgColor : dataStyle.contentBgColor,
							fontColor : dataStyle.contentFontColor,
							fontSize : dataStyle.contentFontSize,
							fontAlign : dataStyle.contentFontAlign	
						}
					};
					obj.generatePdfGridReport(view,tableIds,config,false,dataScope,maxSize);
				}
			}
			
		});
		buttons.push(pdfButton);
	}
	if(this.showExcelButton){
		var excelButton=new dorado.widget.Button({
			caption:"导出Excel",
			icon:">avicit/platform6/modules/system/css/images/page_excel.png",
			listener:{
				onClick:function(){
					var basicInfo = basicSettingForm.get("entity");
					var dataStyle = advanceSettingForm.get("entity");
					var config = {
						titleInfos : {
							showTitle : basicInfo.showTitle,
							title : basicInfo.title,
							fontColor : basicInfo.fontColor,
							fontSize : basicInfo.fontSize,
							bgColor : basicInfo.bgColor
						},
						columnHeaderStyle : {
							headerBgColor : dataStyle.headerBgColor,
							headerFontColor : dataStyle.headerFontColor,
							headerFontSize : dataStyle.headerFontSize,
							headerAlign : dataStyle.headerAlign								
						},
						gridDataStyle : {
							bgColor : dataStyle.contentBgColor,
							fontColor : dataStyle.contentFontColor,
							fontSize : dataStyle.contentFontSize,
							fontAlign : dataStyle.contentFontAlign	
						}
					};
					obj.generateExcelGridReport(view,tableIds, config,dataScope,maxSize);
				}
			}
			
		});
		buttons.push(excelButton);
	}
	var cancelButton=new dorado.widget.Button({
		caption:"取消",
		icon:" url(>skin>common/icons.gif) -220px -240px"
	});
	buttons.push(cancelButton);
	var dialog=new dorado.widget.Dialog({
		width:500,
		height:220,
		center:true,
		modal:true,
		closeAction:"close",
		caption:"导出报表",
		buttons:buttons
	});
	cancelButton.addListener("onClick",function(){
		dialog.hide();
	});
	dialog.addChild(tabControl);
	dialog.show();
};

platform.Report.prototype.showGridReportDialogSupportI18n=function(view,tableIds,initFormData,showPdfButton,showExcelButton,dataScope,maxSize,i18n){
	this.view=view;
	this.showPdfButton=showPdfButton;
	this.showExcelButton=showExcelButton;
	
	var tabControl=new dorado.widget.TabControl();
	
	var basicSettingForm = _createTitleInfoFormI18n(initFormData, i18n);
	var basicTab=new dorado.widget.tab.ControlTab({
			caption: i18n.SetCaptionProperty,
			control:basicSettingForm
		});
	tabControl.addTab(basicTab);

	var advanceSettingForm = _createAdvanceSettingForm(initFormData);
	var advanceTab=new dorado.widget.tab.ControlTab({
			caption: i18n.SetTableProperty,
			control:advanceSettingForm
		});

	//tabControl.addTab(advanceTab);
	var obj=this;	
	var buttons=[];	
	if(this.showPdfButton){
		var pdfButton=new dorado.widget.Button({
			caption: i18n.ExportPDF,
			icon:">avicit/platform6/modules/system/css/images/folder_table.png",
			listener:{
				onClick:function(){
					var basicInfo = basicSettingForm.get("entity");
					var dataStyle = advanceSettingForm.get("entity");
					var config = {
						titleInfos : {
							showTitle : basicInfo.showTitle,
							title : basicInfo.title,
							fontColor : basicInfo.fontColor,
							fontSize : basicInfo.fontSize,
							bgColor : basicInfo.bgColor
						},
						columnHeaderStyle : {
							headerBgColor : dataStyle.headerBgColor,
							headerFontColor : dataStyle.headerFontColor,
							headerFontSize : dataStyle.headerFontSize,
							headerAlign : dataStyle.headerAlign								
						},
						gridDataStyle : {
							bgColor : dataStyle.contentBgColor,
							fontColor : dataStyle.contentFontColor,
							fontSize : dataStyle.contentFontSize,
							fontAlign : dataStyle.contentFontAlign	
						}
					};
					obj.generatePdfGridReport(view,tableIds,config,false,dataScope,maxSize);
				}
			}
			
		});
		buttons.push(pdfButton);
	}
	if(this.showExcelButton){
		var excelButton=new dorado.widget.Button({
			caption:i18n.ExportExcel,
			icon:">avicit/platform6/modules/system/css/images/page_excel.png",
			listener:{
				onClick:function(){
					var basicInfo = basicSettingForm.get("entity");
					var dataStyle = advanceSettingForm.get("entity");
					var config = {
						titleInfos : {
							showTitle : basicInfo.showTitle,
							title : basicInfo.title,
							fontColor : basicInfo.fontColor,
							fontSize : basicInfo.fontSize,
							bgColor : basicInfo.bgColor
						},
						columnHeaderStyle : {
							headerBgColor : dataStyle.headerBgColor,
							headerFontColor : dataStyle.headerFontColor,
							headerFontSize : dataStyle.headerFontSize,
							headerAlign : dataStyle.headerAlign								
						},
						gridDataStyle : {
							bgColor : dataStyle.contentBgColor,
							fontColor : dataStyle.contentFontColor,
							fontSize : dataStyle.contentFontSize,
							fontAlign : dataStyle.contentFontAlign	
						}
					};
					obj.generateExcelGridReport(view,tableIds, config,dataScope,maxSize);
				}
			}
			
		});
		buttons.push(excelButton);
	}
	var cancelButton=new dorado.widget.Button({
		caption:i18n.Cancel,
		icon:" url(>skin>common/icons.gif) -220px -240px"
	});
	buttons.push(cancelButton);
	var dialog=new dorado.widget.Dialog({
		width:500,
		height:220,
		center:true,
		modal:true,
		closeAction:"close",
		caption:i18n.ExportReport,
		buttons:buttons
	});
	cancelButton.addListener("onClick",function(){
		dialog.hide();
	});
	dialog.addChild(tabControl);
	dialog.show();
};

platform.Report.prototype.showFormGridReportDialog = function(view, formIds,
		tableIds, initFormData, showPdfButton, showExcelButton,dataScope,maxSize) {
	this.view = view;
	this.showPdfButton = showPdfButton;
	this.showExcelButton = showExcelButton;
	
	var tabControl = new dorado.widget.TabControl();
	
	var basicSettingForm = _createTitleInfoForm(initFormData);
	var basicTab = new dorado.widget.tab.ControlTab({
		caption : $resource("basicTabCaption"),
		control : basicSettingForm
	});
	tabControl.addTab(basicTab);

	var advanceSettingForm = _createAdvanceSettingForm(initFormData);
	var advanceTab = new dorado.widget.tab.ControlTab({
		caption : $resource("advanceTabCaption"),
		control : advanceSettingForm
	});
	//tabControl.addTab(advanceTab);

	var formStyleFormSetting = _createFormStyleForm(initFormData);
	var formStyleTab = new dorado.widget.tab.ControlTab({
		caption : $resource("formStyleTabCaption"),
		control : formStyleFormSetting
	});
	//tabControl.addTab(formStyleTab);
	
	var obj = this;
	var buttons = [];
	if (this.showPdfButton) {
		var pdfButton = new dorado.widget.Button(
				{
					caption : $resource("pdfButtonCaption"),
					icon : ">avicit/platform6/modules/system/css/images/folder_table.png",
					listener : {
						onClick : function() {
							var basicInfo = basicSettingForm.get("entity");
							var formConfig = formStyleFormSetting.get("entity");
							var dataStyle = advanceSettingForm.get("entity");
							var config = {
								titleInfos : {
									showTitle : basicInfo.showTitle,
									title : basicInfo.title,
									fontColor : basicInfo.fontColor,
									fontSize : basicInfo.fontSize,
									bgColor : basicInfo.bgColor
								},
								columnHeaderStyle : {
									headerBgColor : dataStyle.headerBgColor,
									headerFontColor : dataStyle.headerFontColor,
									headerFontSize : dataStyle.headerFontSize,
									headerAlign : dataStyle.headerAlign								
								},
								gridDataStyle : {
									bgColor : dataStyle.contentBgColor,
									fontColor : dataStyle.contentFontColor,
									fontSize : dataStyle.contentFontSize,
									fontAlign : dataStyle.contentFontAlign	
								},
								formStyle : {
									labelAlign : formConfig.labelAlign,
									dataAlign : formConfig.dataAlign,
									dataStyle : formConfig.dataStyle
								}
							};
							obj.generatePdfFormGridReport(view, formIds,tableIds, config,false,dataScope,maxSize);
						}
					}

				});
		buttons.push(pdfButton);
	}
	if (this.showExcelButton) {
		var excelButton = new dorado.widget.Button(
				{
					caption : $resource("excelButtonCaption"),
					icon : ">avicit/platform6/modules/system/css/images/page_excel.png",
					listener : {
						onClick : function() {
							var basicInfo = basicSettingForm.get("entity");
							var formConfig = formStyleFormSetting.get("entity");
							var dataStyle = advanceSettingForm.get("entity");
							var config = {
								titleInfos : {
									showTitle : basicInfo.showTitle,
									title : basicInfo.title,
									fontColor : basicInfo.fontColor,
									fontSize : basicInfo.fontSize,
									bgColor : basicInfo.bgColor
								},
								columnHeaderStyle : {
									headerBgColor : dataStyle.headerBgColor,
									headerFontColor : dataStyle.headerFontColor,
									headerFontSize : dataStyle.headerFontSize,
									headerAlign : dataStyle.headerAlign								
								},
								gridDataStyle : {
									bgColor : dataStyle.contentBgColor,
									fontColor : dataStyle.contentFontColor,
									fontSize : dataStyle.contentFontSize,
									fontAlign : dataStyle.contentFontAlign	
								},
								formStyle : {
									labelAlign : formConfig.labelAlign,
									dataAlign : formConfig.dataAlign,
									dataStyle : formConfig.dataStyle
								}
							};
							obj.generateExcelFormGridReport(view, formIds,
									tableIds, config,dataScope,maxSize);
						}
					}

				});
		buttons.push(excelButton);
	}
	var cancelButton = new dorado.widget.Button({
		caption : $resource("excelButtonCaption"),
		icon : " url(>skin>common/icons.gif) -220px -240px"
	});
	buttons.push(cancelButton);
	var dialog = new dorado.widget.Dialog({
		width : 450,
		height : 300,
		center : true,
		modal : true,
		closeAction : "close",
		caption : $resource("exportDialogCaption"),
		buttons : buttons
	});
	cancelButton.addListener("onClick", function() {
		dialog.hide();
	});
	dialog.addChild(tabControl);
	dialog.show();
};

// form 表单时  excel
platform.Report.prototype.generateExcelFormReport=function(view,formId,config){
	var form = this.buildExcelFormData(view, formId, config);
	var parameter = {
			titleInfos : config,
			form : form
	};
	var action=new dorado.widget.AjaxAction();
	action.set("service","excelReportAction#buildExcelFormReportData");
	action.set("parameter",parameter);
	action.set("executingMessage","报表生成中，请稍后...");
	action.execute(function(fileName){
		var url = $url(">/platform/file/downformgridfile?fileName="+fileName+"&type=xls");
		window.location.href = url;
	});
};

//grid 时  excel
platform.Report.prototype.generateExcelGridReport=function(view,gridIds,configData,dataScope,maxSize){
	if(!configData)configData={};
	var action=new dorado.widget.AjaxAction();
	action.set("service","excelReportAction#buildExcelGridReportData");
	action.set("executingMessage","报表生成中，请稍后...");
	var grid = this.buildExcelGridData(view, gridIds, configData,dataScope,maxSize);
	var parameter = {
			titleInfos : configData.titleInfos,
			grid : grid
	};
	action.set("parameter",parameter);
	action.execute(function(fileName){
		var url = $url(">/platform/file/downformgridfile?fileName="+fileName+"&type=xls");
		window.location.href=url;
	});
	
};

platform.Report.prototype.generateExcelFormGridReport = function(view, formIds, gridIds, config,dataScope,maxSize) {
	var action = new dorado.widget.AjaxAction();
	action.set("service","excelReportAction#buildFormGridReportData");
	action.set("executingMessage",$resource("executingMessage"));
	var grid = this.buildExcelGridData(view,gridIds, config,dataScope,maxSize);
	var form = this.buildExcelFormData(view, formIds,config.formStyle);
	var parameter = {
		grid : grid,
		form : form,
		titleInfos : config.titleInfos
	};

	action.set("parameter", parameter);
	action.execute(function(fileName) {
		var url = $url(">/platform/file/downformgridfile?fileName="+fileName+"&type=xls");
		window.location.href = url;
	});
};

platform.Report.prototype.buildExcelGridData = function(view, gridIds,config,dataScope,maxSize) {
	var gridInfos = new Array();
	this.view = view;
	if (gridIds instanceof Array) {
		for ( var i = 0; i < gridIds.length; i++) {
			var gridId=gridIds[i];
			var grid=this.view.id(gridId);
			var dataPath=grid.get("dataPath");
			var ds=grid.get("dataSet");
			var dataType=ds.get("data").elementDataType;
			if(dataPath){
				dataType=ds.getData(dataPath).elementDataType;
			}
			debugger;
			this.buildSingleGridData(view,gridId,dataType,gridInfos,config,dataScope,maxSize);
		}
	} else {
		var grid=this.view.id(gridIds);
		var dataPath=grid.get("dataPath");
		var ds=grid.get("dataSet");
		var dataType=ds.get("data").elementDataType;
		if(dataPath){
			dataType=ds.getData(dataPath).elementDataType;
		}
		this.buildSingleGridData(view,gridIds,dataType,gridInfos,config,dataScope,maxSize);
	}
	var grid = {
		gridInfos : gridInfos
	};
	return grid;
};

platform.Report.prototype.buildSingleGridData = function(view,gridId,dataType,gridInfos,config,dataScope,maxSize) {
	if(dataScope && dataScope!="currentPage" && dataScope!="serverAll"){
		throw new dorado.Exception("Don't support the scope:"+dataScope);
	}
	var grid = view.id(gridId);
	var dataPath=grid.get("dataPath");
	var columns = grid.get("columns");
	var ds = grid.get("dataSet");
	var resultList = new dorado.util.KeyedList();
	_buildColumnsInfo(columns,dataType,null, resultList, config.columnHeaderStyle,dataScope);
	var result = new dorado.util.Map();
	result.put("columnInfos", resultList.toArray());
	result.put("dataScope", dataScope);		
	result.put("maxSize",maxSize);	
	if(dorado.widget.DataTreeGrid){
		if(grid instanceof dorado.widget.DataTreeGrid){
			var treeColumn=grid.get("treeColumn");
			result.put("treeColumn",treeColumn);
		}	
	}
	if(!dataScope || dataScope=="currentPage"){
		// retrive current page data
		var resultArray=[],data = grid.get("itemModel").toArray();
		if (data instanceof Array) {
			for (var j = 0; j < data.length; j++) {
				var entity = data[j];
				if ( entity instanceof dorado.Entity) {
					var json=_retrieveComplexObjectValue(entity.toJSON(),entity,grid);
					resultArray.push(json);
				}
			}
		}
		result.put("data", resultArray);		
	}else{
		// retrive server side all data
		var dataType,retriveResult=_retriveReferenceParameter(ds,dataPath);
		if(retriveResult.isRef){
			result.put("dataProviderParameter",retriveResult.parameter);
			result.put("pageSize",retriveResult.pageSize);
			result.put("dataProviderId",retriveResult.propertyDef.get("dataProvider.id"));
			dataType=retriveResult.propertyDef.get("dataProvider").dataType;
		}else{
			result.put("dataProviderId", ds.get("dataProvider").name);		
			result.put("dataProviderParameter",ds.get("parameter"));
			result.put("pageSize", ds.get("pageSize") || 0);	
			dataType=ds.get("dataProvider").dataType;
		}
		if(dataType){
			if (dataType instanceof dorado.DataType) dataType = dataType.get("id");
			else if (typeof dataType == "string") dataType = dataType;
			else dataType = dataType.id;
			result.put("resultDataType",dataType);
		}
		result.put("sysParameter", ds._sysParameter ? ds._sysParameter.toJSON() : undefined);
	}
	result.put("gridDataStyle",config.gridDataStyle);
	gridInfos.push(result);
};

function _retriveReferenceParameter(dataSet,dataPath){
	var supported=false, propertyDef=null, parameter={},pageSize = 0;
	if (dataPath && dataPath.match(/\.[\#\w]*$/)) {
		var i = dataPath.lastIndexOf('.');
		var parentDataPath = dataPath.substring(0, i), subProperty = dataPath.substring(i + 1);
		if (subProperty.charAt(0) == '#') subProperty = subProperty.substring(1);		
		var parentEntity = dataSet.getData(parentDataPath);
		if (parentEntity && parentEntity instanceof dorado.Entity) {
			var parentDataType = parentEntity.dataType;
			if (parentDataType && parentDataType instanceof dorado.EntityDataType) {
				propertyDef = parentDataType.getPropertyDef(subProperty);
				if (propertyDef && propertyDef instanceof dorado.Reference) {
					dorado.$this = parentEntity;
					parameter = dorado.JSON.evaluate(propertyDef.get("parameter"));
					pageSize = propertyDef.get("pageSize");
					supported = true;
				}
			}
		}
	}
	if(!supported && dataPath){
		throw new dorado.Exception($resource("supportExceptionPrefix")+dataPath+$resource("supportExceptionSuffix"));
	}
	return {isRef:supported,propertyDef:propertyDef,parameter:parameter,pageSize: pageSize || 0};
}
function _retrieveDataTreeGridBindingConfigs(grid){
	var configs=[];
	var bindingConfigs=grid.get("bindingConfigs");
	recurBindingConfigs(bindingConfigs,configs);
	function recurBindingConfigs(bindingConfigs,result){
		bindingConfigs.each(function(bindingConfig){
			if(bindingConfig.childrenProperty){
				result.push(bindingConfig.childrenProperty);
			}
			if(bindingConfig instanceof Array){				
				recurBindingConfigs(bindingConfig.get("bindingConfigs"),result);
			}
			
		});
	}
	return configs; 
}

function _retrieveComplexObjectValue(json,entity,grid){
	var mapSub = new dorado.util.Map();
	for(var propertyName in json){
		var dataSub=entity.get(propertyName);
		mapSub.put(propertyName, dataSub);
		var iscomplex=false;
		if(dataSub!=null  && !(dataSub instanceof dorado.EntityList)){
		    var dt=dataSub.dataType;
		    if(dt){
		    	var displayProperty=dt.get("defaultDisplayProperty");
		    	if(displayProperty){
		    		mapSub.put(propertyName, dataSub.get(displayProperty));
		    		iscomplex=true;
		    	}
		    }
		}else if(grid instanceof dorado.widget.DataTreeGrid&&dataSub&&(dataSub instanceof dorado.EntityList)&&dataSub.entityCount>0){
			var iterData = dataSub.iterator({
				currentPage : true
			});
			iterData.first();
			var resultDataArray = new Array();
			while (iterData.hasNext()) {
				var e=iterData.next();
				var jsonData=_retrieveComplexObjectValue(e.toJSON(),e,grid);;
				resultDataArray.push(jsonData);
			    
			}
			var configs=_retrieveDataTreeGridBindingConfigs(grid);
			configs.each(function(config){
				if(config==propertyName){
					mapSub.remove(propertyName);
					mapSub.put("children", resultDataArray);
				}
			});
		}
		if(!iscomplex){
			var dt=entity.dataType;
			if(dt){
				dt.get("propertyDefs").each(function(propertyDef){
					var go=true;
					var name=propertyDef.get("name");
					if(name==propertyName){
						var mappings=propertyDef.get("mapping");
						if(mappings){
							for(var i=0;i<mappings.length;i++){
								var mapping=mappings[i];
								var mappingKey=mapping.key;
								if(dataSub!=null && (mappingKey==dataSub.toString())){
									dataSub=mapping.value;
									go=false;
									break;
								}
							}
						}
					}
					return go;
				});
			}
			mapSub.put(propertyName, dataSub);
		}
	}
	return mapSub.toJSON();
};

function _generateHeaderLevel(col, level) {
	var parentColumn = col.get("parent");
	if (parentColumn instanceof dorado.widget.DataGrid) {
		return level;
	}
	if (parentColumn instanceof dorado.widget.grid.ColumnGroup) {
		level++;
		return _generateHeaderLevel(parentColumn, level);
	}
	return level;
}

function _buildColumnsInfo(childrenColumns,dataType,parentMap, resultList, columnHeaderStyle,dataScope) {
	var columns = childrenColumns.toArray();
	for ( var i = 0; i < columns.length; i++) {
		var col = columns[i];
		if (col instanceof dorado.widget.grid.IndicatorColumn) {
			continue;
		}
		if (col instanceof dorado.widget.grid.RowNumColumn) {
			continue;
		}
		if (col instanceof dorado.widget.grid.RowSelectorColumn) {
			continue;
		}
		if (!col.get("visible")) {
			continue;
		}
		var columnName;
		if (col instanceof dorado.widget.grid.ColumnGroup) {
			columnName = col.get("name");
		} else {
			columnName = col.get("property");
		}

		var columnCaption = col.get("caption");
		var width = col._realWidth;
		if (width == undefined) {
			width = 0;
		}
		var level = _generateHeaderLevel(col, 1);
		var map = new dorado.util.Map();
		map.put("columnName", columnName);
		map.put("level", level);
		map.put("label", columnCaption);
		map.put("width", width);
		var mapping=_retriveGridColumnMapping(columnName,dataType);
		if(mapping){
			map.put(columnName+"_mapping",mapping);
		}
		if(dataScope && (dataScope=="serverAll")){
			var displayProperty=_retriveEntityDisplayProperty(columnName,dataType);
			if(displayProperty){
				map.put(columnName+"_displayProperty",displayProperty);
			}			
		}
		
		if (columnHeaderStyle) {
			map.put("bgColor", columnHeaderStyle.headerBgColor);
			map.put("fontColor", columnHeaderStyle.headerFontColor);
			map.put("fontSize", columnHeaderStyle.headerFontSize);
			map.put("align", columnHeaderStyle.headerAlign);
		}
		
		if (col instanceof dorado.widget.grid.ColumnGroup) {
			if (col.get("columns").toArray().length > 0) {
				_buildColumnsInfo(col.get("columns"),dataType,map,
						new dorado.util.KeyedList(), columnHeaderStyle,dataScope);
			}
		}
		resultList.insert(map.toJSON());
	}
	if (parentMap != null) {
		parentMap.put("children", resultList.toArray());
	}
};

function _retriveEntityDisplayProperty(columnName,dataType){
	var displayProperty=null;
	if(!(dataType instanceof dorado.EntityDataType)){
		return displayProperty;
	}
	var propertyDefs=dataType.get("propertyDefs");
	if(propertyDefs instanceof dorado.util.KeyedArray){
		propertyDefs.each(function(propertyDef){
			if(propertyDef.get("name")==columnName){
				var dt=propertyDef.get("dataType");
				if(dt && (dt instanceof dorado.EntityDataType)){
					displayProperty=dt.get("defaultDisplayProperty");					
				}
				return false;
			}
		});
	}	
	return displayProperty;
}

function _retriveGridColumnMapping(columnName,dataType){
	var mapping=null;
	if(!(dataType instanceof dorado.EntityDataType)){
		return mapping;
	}
	var propertyDefs=dataType.get("propertyDefs");
	if(propertyDefs instanceof dorado.util.KeyedArray){
		propertyDefs.each(function(propertyDef){
			if(propertyDef.get("name")==columnName){
				mapping=propertyDef.get("mapping");
				return false;
			}
		});
	}
	return mapping;
};

platform.Report.prototype.buildExcelFormData = function(view, formIds,formStyle,dataScope) {
	var formInfos = new Array();
	if (formIds instanceof Array) {
		for ( var i = 0; i < formIds.length; i++) {
			this.generateExcelFormGridData(view, formIds[i], formInfos);
		}
	} else {
		this.generateExcelFormGridData(view, formIds, formInfos);
	}
	var form = {
		formInfos : formInfos,
		formStyle : formStyle
	};
	return form;
};

platform.Report.prototype.generateExcelFormGridData = function(view, formId,formInfos) {
	var form = view.id(formId);
	var config = {};
	var cols = form.get("cols");
	var count = 2;
	if (cols) {
		count = cols.split(",").length;
	}

	config.columnCount = count;
	var keyedArray = form.get("elements");
	var resultList = new dorado.util.KeyedArray();
	var ds = form.get("dataSet");
	var dataType=null;
	if (ds) {
		var retriveResult=_retriveReferenceParameter(ds,form.get("dataPath"));
		if(retriveResult.isRef){
			dataType=retriveResult.propertyDef.get("dataType");
			if(dataType instanceof dorado.AggregationDataType){
				dataType=dataType.elementDataType;
			}
		}else{
			dataType = ds.getDataType();		
		}
	}
	keyedArray.each(function(element) {
		if (element instanceof dorado.widget.autoform.AutoFormElement && element.get("editor") instanceof dorado.widget.AbstractEditor) {
			var colSpan = 1;
			var rowSpan = 1;
			var layoutConstraint = element.get("layoutConstraint");
			if (layoutConstraint && layoutConstraint.colSpan) {
				colSpan = layoutConstraint.colSpan;
			}
			if (layoutConstraint && layoutConstraint.rowSpan) {
				rowSpan = layoutConstraint.rowSpan;
			}
			var label = element.get("label");
			if (!label && dataType) {
				var propertyName = element.get("property");
				label = dataType.getPropertyDef(propertyName).get("label");
			}
			var data = {
				label : label,
				value : element.get("value"),
				colSpan : colSpan,
				rowSpan : rowSpan
			};
			resultList.append(data);
		}
	});
	config.datas = resultList.toArray();
	formInfos.push(config);
};

// form表单的pdf 
platform.Report.prototype.generatePdfFormReport=function(view,formIds,config,onlineView){
	if(!formIds)
		return;
	var form = this.buildExcelFormData(view, formIds,config);
	var parameter = {
			titleInfos : config,
			form : form
	};
	var action=new dorado.widget.AjaxAction();
	action.set("parameter",parameter);
	action.set("executingMessage","报表生成中，请稍后...");
	action.set("service","pdfReportAction#buildPdfFormReportData");
	
	action.execute(function(fileName){
		var url = $url(">/platform/file/downformgridfile?fileName="+fileName+"&type=pdf");
		window.location.href=url;
	});
	
};

// grid表格  pdf 
platform.Report.prototype.generatePdfGridReport=function(view,gridIds,configData,onlineView,dataScope,maxSize){
	if(!configData)configData={};
	var grid = this.buildExcelGridData(view, gridIds, configData,dataScope,maxSize);
	var parameter = {
			titleInfos : configData.titleInfos,
			grid : grid
	};
	var action=new dorado.widget.AjaxAction();
	action.set("parameter",parameter);
	action.set("executingMessage","报表生成中，请稍后...");
	action.set("service","pdfReportAction#buildPdfGridReportData");
	action.execute(function(fileName){
		var url = $url(">/platform/file/downformgridfile?fileName="+fileName+"&type=pdf");
		window.location.href=url;
	});
};

platform.Report.prototype.generatePdfFormGridReport=function(view,formIds,gridIds,config,onlineView,dataScope,maxSize){
	if(!config)config={};
	var form = this.buildExcelFormData(view, formIds,config.formStyle);
	var grid = this.buildExcelGridData(view, gridIds, config,dataScope,maxSize);
	var parameter = {
		titleInfos : config.titleInfos,
		grid : grid,
		form : form
	};
	var action=new dorado.widget.AjaxAction();
	action.set("parameter",parameter);
	action.set("executingMessage","报表生成中，请稍后...");
	action.set("service","pdfReportAction#buildFormGridReportData");

	action.execute(function(fileName){
		var url = $url(">/platform/file/downformgridfile?fileName="+fileName+"&type=pdf");
		if(onlineView){
			var dialog=new dorado.widget.Dialog({
				width:750,
				height:600,
				center:true,
				modal:true,
				maximizeable:true,
				closeAction:"close",
				children:[{
					$type:"HtmlContainer",
					contentOverflow:"hidden",
					content:"<div id='_swfReportViewerDiv'>"
				}]
			});
			dialog.show();
			var viewer=new bdf.SwfViewer();
			var config={
				swf:$url(">pdfReportAction.generateSwf.c?sourcePdf="+fileName),
				divId:"_swfReportViewerDiv",
				isJasperReports:false,
				downloadReportUrl:url
			};
			viewer.createView(config);	
		}else{
			window.location.href=url;
		}
			
	});
};

function _createAdvanceSettingForm(initFormData){
	var advanceSettingForm=new dorado.widget.AutoForm({
		cols:"*,*",
		labelAlign:"right",
		labelWidth:100
	});
	advanceSettingForm.addElement({
			property:"headerBgColor",
			label:"列头背景色",
			labelWidth:170,
			layoutConstraint:{colSpan:2}
			});
	advanceSettingForm.addElement({
			property:"headerFontColor",
			label:"列头字体颜色",
			labelWidth:170,
			layoutConstraint:{colSpan:2}
			});
	advanceSettingForm.addElement({
				property:"headerFontSize",
				label:"列头字体大小",
				labelWidth:170,
				editor:{
					$type:"NumberSpinner",
					min:10,
					max:100,
					editable:false,
					tags:"reportTitleTag"
				}
			});
	advanceSettingForm.addElement({
			property:"headerAlign",
			label:"列头对齐方式",
			labelWidth:170,
			editor:{
				$type:"TextEditor",
				mapping:[
					{
						key:0,
						value:"左对齐"
					},
					{
						key:1,
						value:"居中对齐"
					},
					{
						key:2,
						value:"右对齐"
					}
				],
				trigger:"autoMappingDropDown1"
			}
		});

	advanceSettingForm.addElement({
				property:"contentBgColor",
				label:"内容背景色",
				labelWidth:170,
				layoutConstraint:{colSpan:2}
			});
	advanceSettingForm.addElement({
				property:"contentFontColor",
				label:"内容字体颜色",
				labelWidth:170,
				layoutConstraint:{colSpan:2}
			});
	advanceSettingForm.addElement({
			property:"contentFontSize",
			label:"内容字体颜色",
			labelWidth:170,
			editor:{
				$type:"NumberSpinner",
				min:10,
				max:100,
				editable:false,
				tags:"reportTitleTag"
			}
		});
	advanceSettingForm.addElement({
			property:"contentFontAlign",
			label:"内容对齐方式",
			labelWidth:170,
			editor:{
				$type:"TextEditor",
				mapping:[
					{
						key:0,
						value:"左对齐"
					},
					{
						key:1,
						value:"居中对齐"
					},
					{
						key:2,
						value:"右对齐"
					}
				],
				trigger:"autoMappingDropDown1"
			}
		});


	var advanceFormData={
		headerBgColor:"233,233,233",
		headerFontColor:"0,0,0",
		headerFontSize:9,
		headerAlign:1,
		contentBgColor:"255,255,255",
		contentFontColor:"0,0,0",
		contentFontSize:9,
		contentFontAlign:1
	};
	if(initFormData){
		dorado.Object.apply(advanceFormData,initFormData);
	}
	advanceSettingForm.set("entity",advanceFormData);
	return advanceSettingForm;
}

function _createFormStyleForm(initFormData2){
	var basicSettingForm2 = new dorado.widget.AutoForm({
		cols : "*,*",
		labelAlign : "right",
		labelWidth : 90
	});
	basicSettingForm2.addElement({
		name : "labelAlign",
		label : "标签对齐方式",
		layoutConstraint : {
			colSpan : 3
		},
		editor : {
			$type : "TextEditor",
			mapping : [ {
				key : 0,
				value : "左对齐"
			}, {
				key : 1,
				value : "居中对齐"
			}, {
				key : 2,
				value : "右对齐"
			} ],
			trigger : "autoMappingDropDown1"
		}
	});
	basicSettingForm2.addElement({
		name : "dataAlign",
		label : "内容对齐方式",
		layoutConstraint : {
			colSpan : 3
		},
		editor : {
			$type : "TextEditor",
			mapping : [ {
				key : 0,
				value : "左对齐"
			}, {
				key : 1,
				value : "居中对齐"
			}, {
				key : 2,
				value : "右对齐"
			} ],
			trigger : "autoMappingDropDown1"
		}
	});
	basicSettingForm2.addElement({
		name : "dataStyle",
		label : "内容字体样式",
		layoutConstraint : {
			colSpan : 3
		},
		editor : {
			$type : "RadioGroup",
			layout : "flow",
			radioButtons : [ {
				text : "正常",
				value : 0
			}, {
				text : "下划线",
				value : 4
			}, {
				text : "倾斜",
				value : 2
			}, {
				text : "加粗",
				value : 1
			} ]
		}
	});

	var initConfigData2 = {
		labelAlign : 2,
		dataAlign : 0,
		dataStyle : 0,
		showPageNo : true,
		showBorder : true
	};
	var initFormData2 = null;
	if (initFormData2) {
		dorado.Object.apply(initConfigData2, initFormData2);
	}
	basicSettingForm2.set("entity", initConfigData2);
	return basicSettingForm2;

}

function _createTitleInfoForm(initFormData){
	var basicSettingForm = new dorado.widget.AutoForm({
		cols : "*,*",
		labelAlign : "right",
		labelWidth : 100
	});
	basicSettingForm.addElement({
		property : "showPageNo",
		label : "显示页号",
		editor : {
			$type : "CheckBox",
			checked : true
		}
	});

	basicSettingForm.addElement({
		property : "showTitle",
		label : "显示标题",
		editor : {
			$type : "CheckBox",
			onValueChange : function(self, arg) {
				if (self.get("value")) {
					this.tag("reportTitleTag").set("readOnly", false);
				} else {
					this.tag("reportTitleTag").set("readOnly", true);
				}
			}
		}
	});

	basicSettingForm.addElement({
		property : "title",
		readOnly : true,
		tags : "reportTitleTag",
		layoutConstraint : {
			colSpan : 2
		},
		label : "报表标题"
	});

	basicSettingForm.addElement({
		property : "fontSize",
		readOnly : true,
		tags : "reportTitleTag",
		layoutConstraint : {
			colSpan : 2
		},
		label : "标题文字大小",
		editor : {
			$type : "NumberSpinner",
			min : 10,
			max : 100,
			editable : false,
			tags : "reportTitleTag"
		}
	});
//	basicSettingForm.addElement({
//		property : "fontColor",
//		readOnly : true,
//		tags : "reportTitleTag",
//		layoutConstraint : {
//			colSpan : 2
//		},
//		label : "标题颜色"
//	});
//	basicSettingForm.addElement({
//		property : "bgColor",
//		readOnly : true,
//		tags : "reportTitleTag",
//		layoutConstraint : {
//			colSpan : 2
//		},
//		label : "标题背景颜色 "
//	});
	var basicFormInitData = {
		fileName : "report.pdf",
		showPageNo : true,
		showTitle : false,
		title : "",
		fontSize : 18,
		fontColor : "0,0,0",
		bgColor : "255,255,255"
	};
	if (initFormData) {
		dorado.Object.apply(basicFormInitData, initFormData);
	}
	basicSettingForm.set("entity", basicFormInitData);
	return basicSettingForm;
}

function _createTitleInfoFormI18n(initFormData, i18n){
	var basicSettingForm = new dorado.widget.AutoForm({
		cols : "*,*",
		labelAlign : "right",
		labelWidth : 100
	});
	basicSettingForm.addElement({
		property : "showPageNo",
		label : i18n.DisplayPageNumber,
		editor : {
			$type : "CheckBox",
			checked : true
		}
	});

	basicSettingForm.addElement({
		property : "showTitle",
		label : i18n.DisplayCaption,
		editor : {
			$type : "CheckBox",
			onValueChange : function(self, arg) {
				if (self.get("value")) {
					this.tag("reportTitleTag").set("readOnly", false);
				} else {
					this.tag("reportTitleTag").set("readOnly", true);
				}
			}
		}
	});

	basicSettingForm.addElement({
		property : "title",
		readOnly : true,
		tags : "reportTitleTag",
		layoutConstraint : {
			colSpan : 2
		},
		label : i18n.ReportTitle
	});

	basicSettingForm.addElement({
		property : "fontSize",
		readOnly : true,
		tags : "reportTitleTag",
		layoutConstraint : {
			colSpan : 2
		},
		label : i18n.TitleFontSize,
		editor : {
			$type : "NumberSpinner",
			min : 10,
			max : 100,
			editable : false,
			tags : "reportTitleTag"
		}
	});
//	basicSettingForm.addElement({
//		property : "fontColor",
//		readOnly : true,
//		tags : "reportTitleTag",
//		layoutConstraint : {
//			colSpan : 2
//		},
//		label : "标题颜色"
//	});
//	basicSettingForm.addElement({
//		property : "bgColor",
//		readOnly : true,
//		tags : "reportTitleTag",
//		layoutConstraint : {
//			colSpan : 2
//		},
//		label : "标题背景颜色 "
//	});
	var basicFormInitData = {
		fileName : "report.pdf",
		showPageNo : true,
		showTitle : false,
		title : "",
		fontSize : 18,
		fontColor : "0,0,0",
		bgColor : "255,255,255"
	};
	if (initFormData) {
		dorado.Object.apply(basicFormInitData, initFormData);
	}
	basicSettingForm.set("entity", basicFormInitData);
	return basicSettingForm;
}