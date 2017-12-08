/**
 * sysLookUpType的子表操作
 * @author zhanglei
 */
function SysLookup(datagrid,url){
	if(!datagrid || typeof(datagrid)!=='string'&&datagrid.trim()!==''){
		throw new Error("datagrid不能为空！");
	}
    var	_url=url;
    this.getUrl = function(){
    	return _url;
    }
    this._datagridName=datagrid;
	this._datagridId="#"+datagrid;
	this._doc = document;
	this.editIndex = undefined;//当前正在编辑的行
	this._validation={};//有效标识
	var _s = this;
	var _validate=function(){
		if(!_s.endEditing()){
			$.messager.alert('提示','不能保存，请确保上一条数据填写完整','warning');
			return false;
		}
		
		var accessRows = _s._datagrid.datagrid('getChanges');
		
		var l = accessRows.length;
		var length=l;
		var row;
		for(;l--;){
			row =accessRows[l];
			if(!row.lookupName){
				$.messager.show({
					 title : '提示',
					 msg : '第'+(length-l)+'条数据中,系统代码名称不能为空!'
				 });
				return false;
			}
			if(!row.lookupCode){
				$.messager.show({
					 title : '提示',
					 msg : '第'+(length-l)+'条数据中,系统代码值不能为空!'
				 });
				return false;
			}
			if(!row.displayOrder){
				$.messager.show({
					 title : '提示',
					 msg : '第'+(l+1)+'条数据中,需要有排序号!'
				 });
				return false;
			}
		}
		return true;
	}
	this.validate=function(){
		return _validate;
	}
	
}
//初始化操作
SysLookup.prototype.init=function(pid){
	var _self = this;
	$.ajax({
		 url:'platform/syslookuptype/getLookUpCode/PLATFORM_VALID_FLAG',
		 type : 'get',
		 dataType : 'json',
		 success : function(r){
			 _self._validation=r;
		 }
	 });
	this._datagrid =$(this._datagridId).datagrid({
			onLoadSuccess: function(data){
				_self.endEditing();
				_self._datagrid.datagrid('clearChecked').datagrid('clearSelections');
			},
			onClickCell: function(index, field,value){
				if (_self.endEditing()){//.datagrid('selectRow', index)
					_self._datagrid.datagrid('editCell', {index:index,field:field,data:_self._validation});
					_self.editIndex = index;
					return true;
				}
				return false;
		},url : this.getUrl() + "/allSysLookup/" + pid
	})
	
}
//扩展easyui单元格编辑
$.extend($.fn.datagrid.methods, {
    editCell: function(jq,param){
        return jq.each(function(){
            var opts = $(this).datagrid('options');
            var fields = $(this).datagrid('getColumnFields',true).concat($(this).datagrid('getColumnFields'));
            for(var i=0; i<fields.length; i++){
                var col = $(this).datagrid('getColumnOption', fields[i]);
                col.editor1 = col.editor;
                if (fields[i] != param.field){
                    col.editor = null;
                }
            }
            $(this).datagrid('beginEdit', param.index);
            for(var i=0; i<fields.length; i++){
                var col = $(this).datagrid('getColumnOption', fields[i]);
                col.editor = col.editor1;
            }
            if(param.field ==='validFlag'){
            	var ed = $(this).datagrid('getEditor',{index:param.index,field: param.field });
     			$(ed.target).combobox( 'loadData' , param.data);//.combobox('select',0);
            }
        });
    }
});

/**
 * 根据父配置文件id加载配置文件属性值
 */
SysLookup.prototype.loadById=function(pid){
	this.endEditing();
	return this._datagrid ? this._datagrid.datagrid({url : this.getUrl() + "/allSysLookup/" + pid}).datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections')
						  :this.init(pid);
}

SysLookup.prototype.endEditing=function(){
    if (this.editIndex == undefined){return true}
    	this._datagrid.datagrid('endEdit', this.editIndex).datagrid('unselectRow',this.editIndex);
        this.editIndex = undefined;
        return true;
}
//******************************增删改操作  start ***********************************
//打开添加框
SysLookup.prototype.insert=function(){
	this.endEditing();
	this._datagrid.datagrid('insertRow',{
		index: 0,
		row:{id:"",validFlag:1}
	});
}
//保存功能
SysLookup.prototype.save=function(pid){
	if(!this.validate()()){
		return false;
	}
	var _self = this;
	var accessRows = this._datagrid.datagrid('getChanges');
	if(accessRows.length > 0){
		var reg =/\s/;
		for (var i=0;i<accessRows.length;i++){
			if(reg.test(accessRows[i].lookupName)){
				$.messager.alert('提示',"系统代码名称不能为空或含有空格字符，请检查！",'warning');
				return;
			}
			if(reg.test(accessRows[i].lookupCode)){
				$.messager.alert('提示',"系统代码值不能为空或含有空格字符，请检查！",'warning');
				return;
			}
		}
	}
	$.ajax({
		 url: _self.getUrl()+'/operation/saveSysLookup/'+pid,
		 data : {datas:JSON.stringify(accessRows)},
		 type : 'post',
		 dataType : 'json',
		 success : function(r){
			 if (r.flag == "success"){
				 _self.reLoad();
				 
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
SysLookup.prototype.del=function(){
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
				 url:'platform/syslookuptype/operation/lookup/delete',
				 data:	JSON.stringify(ids),
				 contentType : 'application/json',
				 type : 'post',
				 dataType : 'json',
				 success : function(r){
					 if (r.flag == "success") {
						 _self.reLoad();
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
/**
 * 关闭语言选择对话框
 */
SysLookup.prototype.closeLdialog=function(id){
	$(id).dialog('close');
}
SysLookup.prototype.openLanguageForm=function(){
	//this.endEditing();
	var rows = this._datagrid.datagrid('getChecked');
	if(rows.length !== 1){
		alert("请选择一条数据！");
		return false;
	}
	var index =this._datagrid.datagrid('getRowIndex',rows[0]);
	this._datagrid.datagrid('endEdit',index);
	var changeRows = this._datagrid.datagrid('getChanges');
	if(changeRows.length !==0){
		alert("请先保存第"+(index+1)+"条数据，再设置语言");
		return;
	}
	//getRowIndex
	var rows = this._datagrid.datagrid('getChecked');
	if(rows.length !== 1){
		alert("请选择一条数据！");
		return false;
	}
	this.cData = new CommonDialog("chooseL","700","400",this.getUrl()+'/chooseLanguage/2/'+rows[0].id,"多语言设置",false,true,false);
	this.cData.show();
}
//重载数据
SysLookup.prototype.reLoad=function(){
	this._datagrid.datagrid('load',{}).datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
}
//******************************增删该操作  end   ***********************************
//******************************格式化显示  start ***********************************
//格式化数据
SysLookup.prototype.formatValid=function(value){
	if(value === null || value === '') return '';
	var l=this._validation.length;
	for(;l--;){
		if(this._validation[l].lookupCode == value){
			return this._validation[l].lookupName;
		}
	}
}
//******************************格式化显示  end   ***********************************

