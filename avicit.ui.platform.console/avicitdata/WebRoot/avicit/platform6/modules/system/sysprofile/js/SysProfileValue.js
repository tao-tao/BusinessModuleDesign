/**
 * @author zhanglei
 */
function SysProfileValue(datagrid,url){
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
	this.editColumn='';//当前正在编辑的列
	//this.init.call(this);//注释掉防止首次加载请求后台两次
	this._scope={};//格式化显示level
	this._appDialog={};//选择应用对话框
	this._sqlDialog={};//选择sql值对话框
	this._siteDialog={};//选择site对话框
	this._sql=null;//配置文件中的sql
	var _sqlData={};//配置文件中sql的格式化值
	this.getSqlData=function(){
		return _sqlData;
	}
	this.setSqlData=function(data){
		_sqlData = data;
	}
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
			if(!row.profileLevelCode){
				$.messager.show({
					 title : '提示',
					 msg : '第'+(length-l)+'条数据中,级别不能为空!'
				 });
				return false;
			}
			if(!row.levelValueName){
				$.messager.show({
					 title : '提示',
					 msg : '第'+(length-l)+'条数据中,级别值不能为空!'
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
SysProfileValue.prototype.init=function(pid){
	var _self = this;
	this._datagrid =$(this._datagridId).datagrid({
			onLoadSuccess: function(data){
				_self._datagrid.datagrid('clearChecked').datagrid('clearSelections');
			},
			onClickCell: function(index, field,value){
					_self.editColumn=field;
					if(field==='levelValueName'){//需要根据不同scope进行选择
						_self.openSelector(index, field, value);
						return true;
					}
					if(field==='profileOptionValue' && _self._sql && _self.endEditing()){//点击配置文件值，并且配置文件有sql语句
						//if(_self.endEditing()){
						_self.openSqlSelector(index, field, value);
						//}
						return true;
					}
					if (_self.endEditing()){
						_self._datagrid.datagrid('selectRow', index).datagrid('editCell', {index:index,field:field,data:_self._scope});
						_self.editIndex = index;
						return true;
					}
					return false;
			},url : this.getUrl() + "/" + pid+".json"
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
            if(param.field ==='profileLevelCode'){
            	var ed = $(this).datagrid('getEditor',{index:param.index,field: param.field });
     			$(ed.target).combobox( 'loadData' , param.data);//.combobox('select',0);
            }
        });
    }
});

/**
 * 根据父配置文件id加载配置文件属性值
 */
SysProfileValue.prototype.loadById=function(pid){
	this.endEditing();
	var _self = this;
	$.ajax({
		 url:this.getUrl() + "/moreinfo/" + pid+".json",
		 type : 'get',
		 async:false,
		 dataType : 'json',
		 success : function(r){
			 _self._scope=r.scope;
			 _self._sql =r.sql;
			 _self.setSqlData(r.sqlData);
		 }
	 });
	return this._datagrid ? this._datagrid.datagrid({url : this.getUrl() + "/" + pid+".json"}):this.init(pid);
}

SysProfileValue.prototype.endEditing=function(){
    if (this.editIndex == undefined){return true}
    //if ($('#dg').datagrid('validateRow', editIndex)){
    	this._datagrid.datagrid('endEdit', this.editIndex).datagrid('unselectRow',this.editIndex);
    	if(this.editColumn ==='profileLevelCode'){
    		this._datagrid.datagrid('updateRow',{
    			index: this.editIndex,
    			row: {
    				levelValue: '-1',
    				levelValueName:''
    			}
    		});
    	}
        this.editIndex = undefined;
        this.editColumn='';
        return true;
}
//打开选择sql值对话框
SysProfileValue.prototype.openSqlSelector=function(index,field,value){
	var nowRow=this._datagrid.datagrid('getRows')[index];
	/*if(!nowRow.id){
		nowRow.id='-1';
	}*/
	//this._sqlDialog = new CommonDialog("sql","700","400",'platform/sysprofile/operation/choose/sql/'+index+'/'+nowRow.id,"选择页面",false,true,false);
	this._sqlDialog = new CommonDialog("sql","700","400",'platform/sysprofile/operation/choose/sql/'+index,"选择页面",false,true,false);
	this._sqlDialog.show()
}
//打开scope选择框
SysProfileValue.prototype.openSelector=function(index, field, value){
	this.endEditing();
	var _self = this;
	var nowRow=this._datagrid.datagrid('getRows')[index];
	var levelCode =nowRow.profileLevelCode;
	var comSelect;
	if(!levelCode){
		 $.messager.show({
			 title : '提示',
			 msg : '请选择当前行的级别值！'
		});
		return true;
	}
	if(levelCode == 1){//地点
		this._siteDialog = new CommonDialog("site","700","400",'platform/sysprofile/operation/choose/site/'+index+'/'+nowRow.levelValue,"地点选择页面",false,true,false);
		this._siteDialog.show()
		return true;
	}else if(levelCode== 2){//应用
		this._appDialog = new CommonDialog("app","700","400",'platform/sysprofile/operation/choose/app/'+index+'/'+nowRow.levelValue,"应用选择页面",false,true,false);
		this._appDialog.show()
		return true;
	}else if(levelCode ==3){//用户
		comSelect = new GridCommonSelector("user",'dgProValue',index,"levelValue",{targetId:'levelValue'},function(rowIndex,resultData){
			_self._datagrid.datagrid('updateRow',{
					index: index,
					row: {
						levelValue: resultData.userId,
						levelValueName:resultData.userName
					}
				});
			},null,null,null,1);
		comSelect.init(nowRow);
		return true;
	}else if(levelCode ==4){//角色
		comSelect = new GridCommonSelector("role",'dgProValue',index,"levelValue",{targetId:'levelValue'},function(rowIndex,resultData){
			if(resultData.length !==1)
				return false;
			_self._datagrid.datagrid('updateRow',{
					index: index,
					row: {
						levelValue: resultData[0].id,
						levelValueName:resultData[0].roleName
					}
				});
			},null,null,null,1);
		comSelect.init(nowRow);
		return true;
	}else if(levelCode ==5){//部门
		comSelect = new GridCommonSelector("dept",'dgProValue',index,"levelValue",{targetId:'levelValue'},function(rowIndex,resultData){
			_self._datagrid.datagrid('updateRow',{
					index: index,
					row: {
						levelValue: resultData.deptId,
						levelValueName:resultData.deptName
					}
				});
			},null,null,null,1);//
		comSelect.init(nowRow);
		return true;
	}else{
		throw new Error('没有级别');
	}
}
//******************************增删改操作  start ***********************************
//打开添加框
SysProfileValue.prototype.insert=function(){
	this.endEditing();
	this._datagrid.datagrid('insertRow',{
		index: 0,
		row:{id:""}
	});
}
//保存功能
SysProfileValue.prototype.save=function(pid){
	if(!this.validate()()){
		return false;
	}
	var _self = this;
	var accessRows = this._datagrid.datagrid('getRows');
	if(accessRows.length > 0){
		var reg =/\s/;
		for (var i=0;i<accessRows.length;i++){
			if(reg.test(accessRows[i].profileOptionValue)){
				$.messager.alert('提示',"配置文件值不能为空或含有空格字符，请检查！",'warning');
				return;
			}
		}
	}
	$.ajax({
		 url:'platform/sysprofile/operation/save/profileValue.json',
		 data : {datas:JSON.stringify(accessRows),pid:pid},
		 type : 'post',
		 dataType : 'json',
		 success : function(r){
			 if (r.flag == "success"){
				 _self.reLoad();
				 _self._datagrid.datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
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
SysProfileValue.prototype.del=function(){
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
				 url:'platform/sysprofile/operation/delete/profileValue.json',
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
SysProfileValue.prototype.reLoad=function(){
	this._datagrid.datagrid('load',{});
}
SysProfileValue.prototype.change=function(){
	//alert(12);
	/*this._datagrid.datagrid('updateRow',{
		index: this.editIndex,
		row: {
			levelValue: '',
			levelValueName:''
		}
	});*/
}
//******************************增删该操作  end   ***********************************
//******************************格式化显示  start ***********************************
//格式化数据
SysProfileValue.prototype.formatLevelCode=function(value){
	if(value ===null ||value === '') return '';
	var l=this._scope.length;
	for(;l--;){
		if(this._scope[l].key == value){
			return this._scope[l].value;
		}
	}
}
//格式化显示样式，如果配置文件有sql则通过弹出对话框选值，否则直接编辑
SysProfileValue.prototype.styleOptionValue=function(){
	if(this._sql){
		return 'cursor:pointer;';
	}else{
		return '';
	}
}
//格式化数据格式，如果配置文件有sql则匹配通用代码格式化，否则直接显示
SysProfileValue.prototype.formateOptionValue=function(value){
	if(this._sql){
		if(value ===null ||value === '') return '';
		var temp =this.getSqlData();
		var l=temp.length;
		for(;l--;){
			if(temp[l].code == value){
				return temp[l].name;
			}
		}
	}else{
		return value;
	}
}
//******************************格式化显示  end   ***********************************
//******************************选择应用操作   ***************************************
//app回调
SysProfileValue.prototype.sureApp=function(index,rowData){
	if (rowData != undefined && rowData!=null){
		this._datagrid.datagrid('updateRow',{
			index: index,
			row: {
				levelValue: rowData.id,
				levelValueName:rowData.applicationName
			}
		});
	this.closeApp("#app");
	}else{
		 $.messager.alert('提示','请先选择一条记录！','warning')
	}
}
SysProfileValue.prototype.closeApp=function(id){
	$(id).dialog('close');
}
//site回调
SysProfileValue.prototype.sureSite=function(index,rowData){
	if (rowData != undefined && rowData!=null){
		this._datagrid.datagrid('updateRow',{
			index: index,
			row: {
				levelValue: rowData.id,
				levelValueName:rowData.name
			}
		});
		this.closeApp("#site");
	}else{
		 $.messager.alert('提示','请先选择一条记录！','warning')
	}
}
SysProfileValue.prototype.closeSite=function(id){
	$(id).dialog('close');
}
//sql回调
SysProfileValue.prototype.sureSql=function(index,rowData){
	if (rowData != undefined && rowData!=null){
		this._datagrid.datagrid('updateRow',{
			index: index,
			row: {
				profileOptionValue: rowData.code
			}
		});
		this.closeSql("#sql");
	}else{
		 $.messager.alert('提示','请先选择一条记录！','warning')
	}
}
SysProfileValue.prototype.closeSql=function(id){
	$(id).dialog('close');
}

