/**
 * @author zhanglei
 */
function SysApplication(id,datagrid,searchForm,url){
	if(!id ||typeof(id)!=='string'&&id.trim()!==''){
		 throw new Error("默认引用id不能为空！");
	}
	if(!datagrid || typeof(datagrid)!=='string'&&datagrid.trim()!==''){
		throw new Error("datagrid不能为空！");
	}
	if(!searchForm || typeof(searchForm)!=='string'&&searchForm.trim()!==''){
		throw new Error("查询id不能为空！");
	}
	this._id = id;
	this._datagridId="#"+datagrid;
	this._searchId="#"+searchForm;
	this._doc = document;
	this._url=url;
	this.comboData ={};
	//正在编辑的行
	this._indexEditing=-1;
	//是否可以结束编辑
	this._isEndEdit=true;
	this.init.call(this);
}
/**
 * 初始化
 */
SysApplication.prototype.init=function(){
	var _self = this;
	this._datagrid=$(this._datagridId).datagrid({
		url:this._url,
		onAfterEdit :function(rowIndex, rowData, changes){
			var rows = dg.datagrid('getRows');
			var l = rows.length;
			_self._isEndEdit=true;
			for(;l--;){
				var row = rows[l];
				if(row.applicationCode==rowData.applicationCode &&dg.datagrid('getRowIndex',row)!==rowIndex){
					alert("应用编码必须唯一！");
					dg.datagrid('selectRow',rowIndex).datagrid('beginEdit',rowIndex);
					var ed = dg.datagrid('getEditor',{index:rowIndex,field: 'runState' });
					$(ed.target).combobox( 'loadData' , _self.comboData);//.combobox('select',0);
					_self._isEndEdit=false;
					break;
				}
				if(row.applicationName.length>100){
					alert("应用名称太长！");
					dg.datagrid('selectRow',rowIndex).datagrid('beginEdit',rowIndex);
					var ed = dg.datagrid('getEditor',{index:rowIndex,field: 'runState' });
					$(ed.target).combobox( 'loadData' , _self.comboData);//.combobox('select',0);
					_self._isEndEdit=false;
					break;
				}
				if(row.applicationCode.length>100){
					alert("应用编码太长！");
					dg.datagrid('selectRow',rowIndex).datagrid('beginEdit',rowIndex);
					var ed = dg.datagrid('getEditor',{index:rowIndex,field: 'runState' });
					$(ed.target).combobox( 'loadData' , _self.comboData);//.combobox('select',0);
					_self._isEndEdit=false;
					break;
				}
			}
		},
		onClickRow :function(rowIndex, rowData){
			if(_self._indexEditing != rowIndex &&_self.endEdit())
				_self._indexEditing=-1;
		}
	});
	var dg =this._datagrid;
	this._search$= $(this._searchId).form();
	this._search$.find('input').on('keyup',function(e){
		if(e.keyCode == 13){
			_self.search();
		}
	});
	$.ajax({
		url: 'platform/syslookuptype/getLookUpCode/PLATFORM_APPLICATION_STATE.json',
		type :'get',
		async:false,
		dataType :'json',
		success : function(r){
			if(r){
				_self.comboData =r;
			}
		}
	});
	/*$.ajax({
		url: 'platform/sysApps/getLookUpCode.json',
		data :{'code':'PLATFORM_APPLICATION_STATE'},
		type :'get',
		dataType :'json',
		success : function(r){
			if(r.json){
				_self.comboData =r.json;
			}
		}
	});*/
};
//查询
SysApplication.prototype.search=function(){
	this._datagrid.datagrid('load',{ param : JSON.stringify(serializeObject(this._search$))});
};
//重置查询条件
SysApplication.prototype.reset=function(){
	/*$.each(this._search$.serializeArray(),function(index){
			$('#'+this['name']).val('');
		}
	);*/
	this._search$.find('input').val('');
	this.search();
};
//添加
SysApplication.prototype.insert=function(){
	if(!this.endEdit()){
		$.messager.alert('提示','不能编辑，请确保上一条数据填写完整','warning');
		return false;
	}
	var temp = this._datagrid;
	temp.datagrid('insertRow',{
		index: 0,
		row:{id:""}
	});
	temp.datagrid('selectRow', 0).datagrid('beginEdit',0);
	this._indexEditing=0;
	//获得编辑器
	var ed = temp.datagrid('getEditor',{index:0,field: 'runState' });
	$(ed.target).combobox( 'loadData' , this.comboData).combobox('select',0);
};
//保存
SysApplication.prototype.save=function(){
	if(!this.endEdit()){
		$.messager.alert('提示','不能保存，请确保上一条数据填写完整','warning');
		return false;
	}
	var rows = this._datagrid.datagrid('getChanges');
	var data =JSON.stringify(rows);
	var _self = this;
	if(rows.length > 0){
		 _self._indexEditing=-1;
		 $.ajax({
			 url:'platform/sysApps/save.json',
			 data : {datas : data},
			 type : 'post',
			 dataType : 'json',
			 success : function(r){
				 if (r.flag == "success"){
					 _self._datagrid.datagrid('reload',{});//刷新当前页
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
	 }else{
		 $.messager.show({
			 title : '提示',
			 msg : '没有要提交的数据！'
		});
	 } 
};
//编辑
SysApplication.prototype.edit=function(){
	var temp = this._datagrid;
	var rows = temp.datagrid('getChecked');
	var index = temp.datagrid('getRowIndex',rows[0]);
	//编辑正在编辑的数据
	if(this._indexEditing===index)	return true;

	if(!this.endEdit()){
		$.messager.alert('提示','不能编辑，请确保上一条数据填写完整','warning');
		return false;
	}
	var l=rows.length;
	if(l !==1){
		$.messager.alert('提示','请选择一条数据！','warning');
		return false;
	}
	temp.datagrid('beginEdit',index);
	this._indexEditing=index;
	//获得编辑器
	var ed = temp.datagrid('getEditor',{index:index,field: 'runState' });
	$(ed.target).combobox( 'loadData' , this.comboData);
};
//删除
SysApplication.prototype.del=function(){
	var rows = this._datagrid.datagrid('getChecked');
	var _self = this;
	var ids = [];
	var l =rows.length;
  	if(l > 0){
	  $.messager.confirm('请确认','您确定要删除当前所选的数据？',function(b){
		 if(b){
			 _self._indexEditing=-1;
			 for(;l--;){
				 if(rows[l].id ==_self._id){
					 alert("默认应用不能删除");
					 continue;
				 }
				 ids.push(rows[l].id);
			 }
			 if(ids.length ==0) return;
			 $.ajax({
				 url:'platform/sysApps/delete.json',
				 data:{
					 ids : ids.join(',')
				 },
				 type : 'post',
				 dataType : 'json',
				 success : function(r){
					 if (r.flag == "success") {
						 _self._datagrid.datagrid('reload',{});//刷新当前页
						 _self._datagrid.datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
						 $.messager.show({
							 title : '提示',
							 msg : '删除成功！'
						});
					}else{
						$.messager.show({
							 title : '提示',
							 msg : r.er
						});
					}
				 }
			 });
		 } 
	  });
  	}else{
	  $.messager.alert('提示','请选择要删除的记录！','warning');
  	}
};
//结束当前编辑行
SysApplication.prototype.endEdit=function(){
	//debugger;
	if(this._indexEditing === -1) return true;
	this._isEndEdit=false;
	this._datagrid.datagrid('endEdit',this._indexEditing);
	return this._isEndEdit;
};
//格式化
SysApplication.prototype.format=function(value){
	if(value ==null ||value == '') return '';
	var l=this.comboData.length;
	for(;l--;){
		if(this.comboData[l].lookupCode == value){
			return this.comboData[l].lookupName;
		}
	}
};