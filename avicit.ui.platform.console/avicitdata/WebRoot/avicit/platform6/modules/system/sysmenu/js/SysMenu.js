function SysMenu(tree,url,searchId,form){
	if(!tree || typeof(tree)!=='string'&&tree.trim()!==''){
		throw new Error("tree不能为空！");
	}
	var _selectId='';
    var	_url=url;
    this.level={};//级别
    this.getUrl = function(){
    	return _url;
    };
    this._searchId = "#"+searchId;
	this._treeId="#"+tree;
	this._tree={};
	this._doc = document;
	this._formId='#'+form;
	this._rootId='';
	//当前选中的node
	this._selectNode={};
	this._iDg={};//添加菜单窗口对象
	this._eDg={};//编辑菜单窗口对象
	/***********************封装各种事件***********/
	var _onLoadSuccess=function(){};//加载数据和查找数据成功后的回调
	this.getOnLoadSuccess=function(){
		return _onLoadSuccess;
	};
	this.setOnLoadSuccess=function(func){
		_onLoadSuccess=func;
	};
	var _onSelect=function(){};//选中node事件
	this.getOnSelect=function(){
		return _onSelect;
	};
	this.setOnSelect=function(func){
		_onSelect=func;
	};
	var _onClick=function(){};//单击node事件
	this.getOnClick=function(){
		return _onClick;
	};
	this.setOnClick=function(func){
		_onClick=func;
	};
	
	
	this.init.call(this);
};

//初始化操作
SysMenu.prototype.init=function(){
	var _self = this;
	$(this._searchId).searchbox({
	 	width: 200,
        searcher: function (value) {
        	if(value==null||value==""){
        		_self._tree.tree('reload');
        		return;
        	}
        	$.ajax({
                cache: true,
                type: "post",
                url:_self.getUrl()+"/search",
                dataType:"json",
                data:{search_text:value},
                async: false,
                error: function(request) {
                	throw new Error('操作失败，服务请求状态：'+request.status+' '+request.statusText+' 请检查服务是否可用！');
                },
                success: function(data) {
					if(data){
						_self._tree.tree('loadData',data);
						_self.selectRootNode(_self);
					}else{
						_self._tree.tree('loadData',{});
					}
                }
            });
        },
        prompt: "请输入菜单名称！"
    });
	this.setOnLoadSuccess(this.selectRootNode);
	this.setOnSelect(this.loadMenuById);
	this._tree=$(this._treeId).tree({    
		url:this.getUrl()+"/sysmenutree/2",
		formatter:function(node){
			if(node._parentId ==="-1"){
				_self._rootId=node.id;
			}
			if(node.attributes && node.attributes.s){
				return '<a style="color:#fff;font-weight:normal;background:#3399ff;padding:0 4px;">' + node.text + '</a>';
			}else{
				return node.text;
			}
		},
		onSelect:function(node){
			_self._selectNode=node;
			_self.getOnSelect()(_self,node);
			},
		onClick:function(node){
			_self.getOnClick()(_self,node);
			},
		onLoadSuccess:function(node, data){
			if(!node){
				_self.getOnLoadSuccess()(_self,node,data);
			}
		}
	});
};
//选择根节点
SysMenu.prototype.selectRootNode=function(self){
	var node = self._tree.tree('find', self._rootId);
	if(node){
		self._tree.tree('select', node.target);
	}else{
		throw new Error('根节点丢失');
	}
};
SysMenu.prototype.loadMenuById=function(_self,node){
	$.ajax({
        cache: true,
        type: "get",
        url:_self.getUrl()+"/sysmenu/"+node.id,
        dataType:"json",
        async: false,
        error: function(request) {
        	throw new Error('操作失败，服务请求状态：'+request.status+' '+request.statusText+' 请检查服务是否可用！');
        },
        success: function(data) {
			$(_self._formId).form('load',data);
        }
    });
};
//添加平级菜单
SysMenu.prototype.insert=function(){
	if(this._selectNode._parentId=== '-1'){
		alert('不能添加与'+this._selectNode.text+'平级的菜单');
		return;
	}
	this._iDg = new CommonDialog("insert","700","400",this.getUrl()+'/operation/add/'+this._selectNode._parentId,"添加菜单",false,true,false);
	this._iDg.show();
};
//添加子菜单
SysMenu.prototype.insertsub=function(){
	this._iDg=new CommonDialog("insert","700","400",this.getUrl()+'/operation/add/'+this._selectNode.id,"添加菜单",false,true,false);
	this._iDg.show();
};

//添加子菜单
SysMenu.prototype.modify=function(){
	this._eDg=new CommonDialog("modify","700","400",this.getUrl()+'/operation/modify/'+this._selectNode.id,"编辑菜单",false,true,false);
	this._eDg.show();
};

SysMenu.prototype.save=function(form,url,dialog,id){
	var _self = this;
	$.ajax({
		 url:url,
		 data : JSON.stringify(form),
		 type : 'post',
		 contentType : 'application/json',
		 dataType : 'json',
		 async : false,
		 success : function(r){
			 if (r.flag == "success"){
				 if(r.type =='1'){//新建
					 var selected = _self._tree.tree('find',r.pid);
					 _self._tree.tree('append', {
					 	parent: selected.target,
					 	data: [r.data]
					 });
					 selected.state='open';
				 }else if(r.type =='2'){//修改
					 form.menuGroupName=r.menuGroupName;
					 $(_self._formId).form('load',form);
					 _self._tree.tree('update',{
						 				target:_self._selectNode.target,
						 				text:form.name
					 				  });
					 
				 }
				 $(dialog).dialog('close');
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
};
SysMenu.prototype.del=function(){
	if(this._selectNode.attributes.count > 0){
		//alert("当前选中菜单含有子菜单项，请先删除子菜单！");
		 $.messager.show({
			 title : '提示',
			 msg : '当前选中菜单含有子菜单项，请先删除子菜单！'
		});
		return;
	}
	var _self = this;
	var ids = [];
	ids.push(this._selectNode.id);
	  $.messager.confirm('请确认','您确定要删除当前节点？',function(b){
		 if(b){
			 $.ajax({
				 url:'platform/sysmenu/operation/sysmenu/delete',
				 data:	JSON.stringify(ids),
				 contentType : 'application/json',
				 type : 'post',
				 dataType : 'json',
				 success : function(r){
					 if (r.flag == "success") {
						 _self._tree.tree('remove',_self._selectNode.target);
						 $.messager.show({
							 title : '提示',
							 msg : '删除成功！'
						 });
						 //删除当前节点，选择父节点
						 var node =_self._tree.tree('find',_self._selectNode._parentId);
						 node.attributes.count--;
						 _self._tree.tree('select', node.target);
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
};
//关闭对话框
SysMenu.prototype.closeDialog=function(id){
	$(id).dialog('close');
};

