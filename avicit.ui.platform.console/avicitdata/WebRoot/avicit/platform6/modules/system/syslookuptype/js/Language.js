/**
 * sysLookUpType的子表操作
 * @author zhanglei
 */
function Language(datagrid,url){
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
	this._language={};//有效标识

	var saveCallBack=function(){};
	this.setSaveCallBack=function(callBack){
		saveCallBack=callBack;
	}
	this.getSaveCallBack=function(){
		return saveCallBack;
	}
	
	var _s = this;
	var _validate=function(){
		if(!_s.endEditing()){
			$.messager.alert('提示','不能保存，请确保上一条数据填写完整','warning');
			return false;
		}
		var accessRows = _s._datagrid.datagrid('getChanges');
		var l = accessRows.length;
		var row;
		for(;l--;){
			row =accessRows[l];
			if(!row.sysLanguageCode){
				$.messager.show({
					 title : '提示',
					 msg : '第'+(l+1)+'条数据中,语言名称不能为空!'
				 });
				return false;
			}
		}
		return true;
	}
	this.validate=function(){
		return _validate;
	}
	this.init.call(this);
	
}
//初始化操作
Language.prototype.init=function(){
	var _self = this;
	$.ajax({
		 url:'platform/syslookuptype/getAllLanguage',
		 type : 'get',
		 dataType : 'json',
		 success : function(r){
			 _self._language=r;
		 }
	 });
	this._datagrid =$(this._datagridId).datagrid({
			onClickCell: function(index, field,value){
				if (_self.endEditing()){//.datagrid('selectRow', index)
					var language=_self._language;
					var i =language.length;
					var rows =_self._datagrid.datagrid('getRows');
					var j =0;
					var temp=[];
					var name='';
					var flag =true;
					for(;i--;){
						for(j =rows.length;j--;){
							if(language[i].code ==rows[j].sysLanguageCode){
								flag=false;
								if(j == index){//把自己添加进去
									temp.push({'code':language[i].code,'name':language[i].name});
								}
								break;
							}
						}
						if(flag){
							temp.push({'code':language[i].code,'name':language[i].name});
						}
						flag =true;
					}
					_self._datagrid.datagrid('editCell', {index:index,field:field,data:temp});
					_self.editIndex = index;
					return true;
				}
				return false;
		},url : this.getUrl() + "/allData/"
	});
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
            if(param.field ==='sysLanguageCode'){
            	var ed = $(this).datagrid('getEditor',{index:param.index,field: param.field });
            	$(ed.target).combobox( 'loadData' , param.data);//.combobox('select',0);
            }
        });
    }
});

/**
 * 根据父配置文件id加载配置文件属性值
 */
Language.prototype.loadById=function(pid){
	return this._datagrid ? this._datagrid.datagrid({url : this.getUrl() + "/allSysLookup/" + pid}):this.init(pid);
}

Language.prototype.endEditing=function(){
    if (this.editIndex == undefined){return true}
	this._datagrid.datagrid('endEdit', this.editIndex).datagrid('unselectRow',this.editIndex);
    this.editIndex = undefined;
    return true;
}
//******************************增删改操作  start ***********************************
//打开添加框
Language.prototype.insert=function(){
	this.endEditing();
	if(this._datagrid.datagrid('getRows').length >=this._language.length){
		alert("不能超过语言数！");
		return;
	}
	this._datagrid.datagrid('insertRow',{
		index: 0,
		row:{id:""}
	});
}
//保存功能
Language.prototype.save=function(pid){
	if(!this.validate()()){
		return false;
	}
	var _self = this;
	var accessRows = this._datagrid.datagrid('getChanges');
	$.ajax({
		 url:_self.getUrl() + "/save/",
		 data : {datas:JSON.stringify(accessRows)},
		 type : 'post',
		 dataType : 'json',
		 success : function(r){
			 if (r.flag == "success"){
				 _self.reLoad();
				 _self._datagrid.datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
				 _self.getSaveCallBack()();
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
Language.prototype.del=function(){
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
				 url:_self.getUrl() + "/delete/",
				 data:	JSON.stringify(ids),
				 contentType : 'application/json',
				 type : 'post',
				 dataType : 'json',
				 success : function(r){
					 if (r.flag == "success") {
						 _self.reLoad();
						 _self._datagrid.datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
						 _self.getSaveCallBack()();
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
Language.prototype.reLoad=function(){
	this._datagrid.datagrid('load',{});
}
//******************************增删该操作  end   ***********************************
//******************************格式化显示  start ***********************************
//格式化数据
Language.prototype.formateLanguage=function(value){
	if(value === null || value === '') return '';
	var l=this._language.length;
	for(;l--;){
		if(this._language[l].code == value){
			return this._language[l].name;
		}
	}
}
//******************************格式化显示  end   ***********************************
