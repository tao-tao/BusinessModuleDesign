/**
 * @author zhanglei
 */
function SysLookupType(datagrid,url,searchD,form){
	if(!datagrid || typeof(datagrid)!=='string'&&datagrid.trim()!==''){
		throw new Error("datagrid不能为空！");
	}
	var _selectId='';
    var	_url=url;
    this._editIndex=0;//用于编辑保存后
    this._nowPageNumber=1;
    this._oldPageNumber=0;
    this.validation={};//有效标识
    this.level={};//级别
    this.modifier={};//使用级别
    this.getUrl = function(){
    	return _url;
    }
    var _onSelectRow=function(){};//对象化封装，防止别人篡改
    this.getOnSelectRow=function(){
    	return _onSelectRow;
    }
    this.setOnSelectRow=function(f){
    	_onSelectRow = f;
    }
    var _onLoadSuccess=function(){};//数据导入成功回调
    this.getOnLoadSuccess=function(){
    	return _onLoadSuccess;
    }
    this.setOnLoadSuccess=function(f){
    	_onLoadSuccess=f;
    }
	this._datagridId="#"+datagrid;
	this._doc = document;
	this._formId="#"+form;
	this._searchDiaId ="#"+searchD;
}
//初始化操作
SysLookupType.prototype.init=function(){
	var _self = this;
	this.searchDialog =$(this._searchDiaId).css('display','block').dialog({
		title:'查询'
	});
	this.searchForm = $(this._formId).form();
	this.searchForm.find('input').on('keyup',function(e){
		if(e.keyCode == 13){
			_self.searchData();
		}
	});
	this.searchDialog.dialog('close',true);
	$.ajax({
		url: 'platform/syslookuptype/getLookUpCode/PLATFORM_VALID_FLAG',
		type :'get',
		async:false,
		dataType :'json',
		success : function(r){
			if(r){
				_self.validation =r;
			}
		}
	});
	$.ajax({
		url: 'platform/syslookuptype/getLookUpCode/PLATFORM_SYS_USER_FLAG',
		type :'get',
		async:false,
		dataType :'json',
		success : function(r){
			if(r){
				_self.level =r;
			}
		}
	});
	
	$.ajax({
		url: 'platform/syslookuptype/getLookUpCode/PLATFORM_USAGE_MODIFIER',
		type :'get',
		async:false,
		dataType :'json',
		success : function(r){
			if(r){
				_self.modifier =r;
			}
		}
	});
	this._datagrid=$(this._datagridId).datagrid({
		url:this.getUrl()+"/allSysLookupType.json",
		onSelect: function(rowIndex, rowData){
				_self._selectId=rowData.id;
				_self.getOnSelectRow()(rowIndex, rowData,rowData.id);
			},
		onLoadSuccess :function(data){
			_self._datagrid.datagrid('clearChecked').datagrid('clearSelections');
			_self._datagrid.datagrid('doCellTip',   
					{   
						onlyShowInterrupt:true,   
						position:'bottom',
						tipStyler:{'backgroundColor':'#FFFFFF',boxShadow:'1px 1px 3px #292929'}
					}); 
			_self.getOnLoadSuccess()();
			_self._nowPageNumber = data.page.page;
			if(data.total>0){
				if (_self._oldPageNumber == 0){
					_self._oldPageNumber = data.page.page;
				}
				if (_self._oldPageNumber == _self._nowPageNumber){
					_self._datagrid.datagrid('selectRow',_self._editIndex);
				}else{
					_self._datagrid.datagrid('selectRow',0);
				}
			}else{
				_self.getOnSelectRow()(null, null,-1);
			}
		}
		});
}
//***************格式化显示********************
//格式化
SysLookupType.prototype.formatValid=function(value){
	if(value ==null ||value == '') return '';
	var l=this.validation.length;
	for(;l--;){
		if(this.validation[l].lookupCode == value){
			return this.validation[l].lookupName;
		}
	}
}
//格式化
SysLookupType.prototype.formatLevel=function(value){
	if(value ==null ||value == '') return '';
	var l=this.level.length;
	for(;l--;){
		if(this.level[l].lookupCode == value){
			return this.level[l].lookupName;
		}
	}
}

SysLookupType.prototype.formatModifier=function(value){
	if(value ==null ||value == '') return '';
	var l=this.modifier.length;
	for(;l--;){
		if(this.modifier[l].lookupCode == value){
			return this.modifier[l].lookupName;
		}
	}
}
//******************************查询方法  start***********************************
//打开查询框
SysLookupType.prototype.openSearchForm =function(){
	this.searchDialog.dialog('open',true);
}
//去后台查询
SysLookupType.prototype.searchData =function(){
	this._datagrid.datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
	this._datagrid.datagrid('load',{ param : JSON.stringify(serializeObject(this.searchForm))});
}
//隐藏查询框
SysLookupType.prototype.hideSearchForm =function(){
	this.searchDialog.dialog('close',true);
}
/*清空查询条件*/
SysLookupType.prototype.clearData =function(){
	this.searchForm.find('input').val('');
	this.searchData();
};
//******************************查询方法  end  ***********************************
//******************************增删改操作  start ***********************************
//打开添加框
SysLookupType.prototype.insert=function(){
	this.nData = new CommonDialog("insert","700","400",this.getUrl()+'/operation/add/null',"添加",false,true,false);
	this.nData.show();
}
//关闭对话框
SysLookupType.prototype.closeDialog=function(id){
	$(id).dialog('close');
}
//打开编辑框
SysLookupType.prototype.modify=function(){
	var rows = this._datagrid.datagrid('getChecked');
	this._editIndex = this._datagrid.datagrid('getRowIndex',rows[0]);
	var pageopt = this._datagrid.datagrid('getPager').data("pagination").options;
	this._oldPageNumber = pageopt.pageNumber;
	if(rows.length !== 1){
		$.messager.alert('提示','请选择一条数据编辑！','warning');
		return false;
	}
	this.eData = new CommonDialog("edit","700","400",this.getUrl()+'/operation/modify/'+rows[0].id,"编辑",false,true,false);
	this.eData.show();
}
/**
 * 关闭语言选择对话框
 */
SysLookupType.prototype.closeLdialog=function(id){
	$(id).dialog('close');
}
//打开语言选择
SysLookupType.prototype.openLanguageForm=function(){
	var rows = this._datagrid.datagrid('getChecked');
	if(rows.length !== 1){
		alert("请选择一条数据！");
		return false;
	}
	this.cData = new CommonDialog("chooseL","700","400",this.getUrl()+'/chooseLanguage/1/'+rows[0].id,"多语言设置",false,true,false);
	this.cData.show();
}
//保存功能
SysLookupType.prototype.save=function(form,url,id){
	var _self = this;
	$.ajax({
		 url:url,
		 data : JSON.stringify(form),
		 type : 'post',
		 contentType : 'application/json',
		 dataType : 'json',
		 success : function(r){
			 if (r.flag == "success"){
				 _self.reLoad();
				 _self._datagrid.datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
				 $(id).dialog('close');
				$.messager.show({
					 title : '提示',
					 msg : '保存成功！'
				 });
			 }else{
				 $.messager.show({
					 title : '提示',
					 msg : r.error
				});
			 } 
		 }
	 });
}
//删除
SysLookupType.prototype.del=function(){
	var rows = this._datagrid.datagrid('getChecked');
	var _self = this;
	var ids = [];
	var l =rows.length;
	if(l > 0){
	  $.messager.confirm('请确认','您确定要删除当前所选的数据？',function(b){
		 if(b){
			 for(;l--;){
				 ids.push(rows[l].id);
			 }
			 $.ajax({
				 url:'platform/syslookuptype/operation/lookuptype/delete',
				 data:	JSON.stringify(ids),
				 contentType : 'application/json',
				 type : 'post',
				 dataType : 'json',
				 success : function(r){
					 if (r.flag == "success") {
						 _self.reLoad();
						 _self._datagrid.datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
						 $.messager.show({
							 title : '提示',
							 msg : '删除成功！'
						});
					}else{
						$.messager.show({
							 title : '提示',
							 msg : r.error
						});
					}
				 }
			 });
		 } 
	  });
	}else{
	  $.messager.alert('提示','请选择要删除的记录！','warning');
	}
}
//重载数据
SysLookupType.prototype.reLoad=function(){
	this._datagrid.datagrid('reload',{});
}
