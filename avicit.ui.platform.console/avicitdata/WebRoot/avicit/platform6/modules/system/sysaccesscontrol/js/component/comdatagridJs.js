function ComDatagrid(datagrid){
	if(!datagrid || typeof(datagrid)!=='string'&&datagrid.trim()!==''){
		throw new Error("datagrid不能为空！");
	}
    this._datagridName=datagrid;
	this._datagridId="#"+datagrid;
	this._doc = document;
	this.editIndex = undefined;//当前正在编辑的行
	this.editColumn='';//当前正在编辑的列
	this.init.call(this);
	var _s = this;
	var _validate=function(){
		if(!_s.endEditing()){
			$.messager.alert('提示','不能保存，请确保上一条数据填写完整','warning');
			return false;
		}
		var accessRows = _s._datagrid.datagrid('getRows');
		var l = accessRows.length;
		var row;
		for(;l--;){
			row =accessRows[l];
			if(!row.key){
				$.messager.show({
					 title : '提示',
					 msg : '第'+(l+1)+'条数据中,组件名称和标识不能为空!'
				 });
				return false;
			}
			if(!row.value){
				$.messager.show({
					 title : '提示',
					 msg : '第'+(l+1)+'条数据中,参数值不能为空!'
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
ComDatagrid.prototype.init=function(){
	var _self = this;
	this._datagrid =$(this._datagridId).datagrid({
		onClickCell: function(index, field,value){
			if (_self.endEditing()){
				_self._datagrid.datagrid('selectRow', index).datagrid('editCell', {index:index,field:field});
				_self.editIndex = index;
				return true;
			}
			return false;
	}});
}
//扩展easyui单元格编辑
$.extend($.fn.datagrid.methods, {
    editCell: function(jq,param){
        jq.each(function(){
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
        });
        return true;
    }
});



ComDatagrid.prototype.endEditing=function(){
    if (this.editIndex == undefined){return true}
    //if ($('#dg').datagrid('validateRow', editIndex)){
    	this._datagrid.datagrid('endEdit', this.editIndex);
        this.editIndex = undefined;
        this.editColumn='';
        return true;
}


//******************************增删改操作  start ***********************************
//打开添加框
ComDatagrid.prototype.insert=function(){
	this.endEditing();
	this._datagrid.datagrid('insertRow',{
		index: 0,
		row:{id:"",type:"COMPONENT",sysApplicationId:"1"}
	});
}
//保存功能
ComDatagrid.prototype.save=function(parentId){
	if(!this.validate()()){
		return false;
	}
	var _self = this;
	//console.info(this._datagrid.datagrid('getChanges'));//getRows
	var accessRows = this._datagrid.datagrid('getRows');
	$.ajax({
		 url:'platform/componentManagerController/save/'+parentId+'.json',
		 data : {datas:JSON.stringify(accessRows)},
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
ComDatagrid.prototype.del=function(){
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
				 url:'platform/componentManagerController/delete.json',
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

//刷新缓存中组件资源的权限信息
ComDatagrid.prototype.refreshCom=function(){
	var _self = this;
	  $.messager.confirm('请确认','真的要刷新组件资源的权限信息吗？',function(b){
		 if(b){
			 $.ajax({
				 url:'platform/componentManagerController/refreshComponentAuthority.json',
				 type : 'post',
				 dataType : 'json',
				 success : function(r){
					 if (r.flag == "success") {
						 _self.reLoad();
						 _self._datagrid.datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
						 $.messager.show({
							 title : '提示',
							 msg : '刷新成功！'
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
  	
}


//刷新缓存中组件资源的权限信息
ComDatagrid.prototype.refreshComponent=function(){
	var _self = this;
	  $.messager.confirm('请确认','真的要刷新本页面的组件集合信息吗？',function(b){
		 if(b){
			 if (currentMenuId=="root"){
				 currentMenuId="1";
			 }
			 $.ajax({
					url: "platform/componentManagerController/refreshComponent4Page.json?resourceId="+currentMenuId,
					type: "POST",
					dataType: "json",
					data : currentMenuId,
					success: function(msg) {
					  if(msg.flag=='success'){
						  $.messager.show({
								title : '提示',
								msg : "重载页面组件成功。",
								timeout: 2000,  
					            showType:'slide'
							});  
					  }else if(msg.flag=='failure'){
						 if(msg.error){
							 $.messager.show({
									title : '提示',
									msg : msg.error,
									timeout: 2000,  
						            showType:'slide'
								});  
						 }
					  }
						
					},error: function(XMLHttpRequest, textStatus, errorThrown) {
//			            alert(XMLHttpRequest.status);
//			            alert(textStatus);
//			            alert(XMLHttpRequest.readyState);
			        }
				});
		 }
	  });
  	
}

//重载数据
ComDatagrid.prototype.reLoad=function(){
	this._datagrid.datagrid('load',{currentMenuId:currentMenuId});
}
//******************************增删该操作  end   ***********************************


