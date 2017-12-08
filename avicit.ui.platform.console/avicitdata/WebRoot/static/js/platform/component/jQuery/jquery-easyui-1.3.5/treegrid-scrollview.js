/*
 * 为了处理浏览器兼容问题
 */
var s;
var Sys = {};
var isIE67 = false;
var ua = navigator.userAgent.toLowerCase();
(s = ua.match(/msie ([\d.]+)/)) ? Sys.ie = s[1] :
(s = ua.match(/firefox\/([\d.]+)/)) ? Sys.firefox = s[1] :
(s = ua.match(/chrome\/([\d.]+)/)) ? Sys.chrome = s[1] :
(s = ua.match(/opera.([\d.]+)/)) ? Sys.opera = s[1] :
(s = ua.match(/version\/([\d.]+).*safari/)) ? Sys.safari = s[1] : 0;
if(Sys.ie && (ua.indexOf('msie 6.0') > 1 ||  ua.indexOf('msie 7.0')>1)){
	isIE67 = true;
}
var rowIndex = 0; 
//记忆要滚动数据
var endResults = [];
//主滚动条目总数
var totals = 0;
var root = [];
var isAllExpand = false;
var historyRows = [];
var displayHeight = 0;
var firstDisplayHeight = 0;
var tempFlagNum = 0;
//js克隆
function clone(obj){  
    var o;  
    switch(typeof obj){  
    case 'undefined': break;  
    case 'string'   : o = obj + '';break;  
    case 'number'   : o = obj - 0;break;  
    case 'boolean'  : o = obj;break;  
    case 'object'   :          
    	if(obj === null){  
            o = null;  
        }else{  
            if(obj instanceof Array){  
                o = [];  
                for(var i = 0, len = obj.length; i < len; i++){  
                	o.push(clone(obj[i]));  
                }  
           }else{  
               	o = {};  
                for(var k in obj){  
                    o[k] = clone(obj[k]);  
                }  
           }  
        }  
        break;  
    default:          
        o = obj;break;  
    }  
    return o;     
}  


var scrollviews = $.extend({}, $.fn.treegrid.defaults.view, {
	render: function(target, container, frozen){
		var opts = $.data(target, 'treegrid').options;
		var fields = $(target).datagrid('getColumnFields', frozen);
		var rowIdPrefix = $.data(target, 'datagrid').rowIdPrefix;
		
		if (frozen){
			if (!(opts.rownumbers || (opts.frozenColumns && opts.frozenColumns.length))){
				return;
			}
		}
		var index = this.index;
		var view = this;
//		this.treeNodes = this.rows;
		if(this.rows){
			//克隆此对象
			var tempDatas = this.rows;
			//为了处理编号重复，由于js对象是引用类型，经过transfer后，会将children的值重新赋值
			for(var i=0;i<tempDatas.length;i++){
				var row = tempDatas[i];
				if(row.children){
					tempDatas[i].children = [];
					//如果导入的树不是全部展开的
					if(!isAllExpand){
						break;
					}
				}
			}
			var data = this.transfer(target, "", tempDatas);
			if(data.length!=0){
				var table = getTreeData(frozen, 0, data);
				$(container).html(table.join(''));
				if(!isAllExpand){
					if(data[0].PARENT_ID=='-1' || !data[0]._parentId){
						root = data.shift();
					}
				}
			}else{
				var tempRows = [];
				tempRows.push(root);
				tempRows[0].children = this.rows;
				var table = getTreeData(frozen, 0, tempRows,true);
				$(container).html(table.join(''));
			}
//			$(container).append(table.join(''));
		}else{
			if(this.treeNodes){
				var table = getTreeData(frozen, this.treeLevel, this.treeNodes);
				$(container).append(table.join(''));
			}
			
		}
		function getTreeData(frozen, depth, children,isLeaf){
			var table = ['<table class="datagrid-btable" cellspacing="0" cellpadding="0" border="0"><tbody>'];
			for(var i=0; i<children.length; i++){
				var row = children[i];
				if (row.state != 'open' && row.state != 'closed'){
					row.state = 'open';
				}
				var css = opts.rowStyler ? opts.rowStyler.call(target, row) : '';
				var classValue = '';
				var styleValue = '';
				if (typeof css == 'string'){
					styleValue = css;
				} else if (css){
					classValue = css['class'] || '';
					styleValue = css['style'] || '';
				}
				var cls = 'class="datagrid-row ' + (index % 2 && opts.striped ? 'datagrid-row-alt ' : ' ') + classValue + '"';
				var style = styleValue ? 'style="' + styleValue + '"' : '';
				
//				var cls = (index++ % 2 && opts.striped) ? 'class="datagrid-row datagrid-row-alt"' : 'class="datagrid-row"';
//				var styleValue = opts.rowStyler ? opts.rowStyler.call(target, row) : '';
//				var style = styleValue ? 'style="' + styleValue + '"' : '';
				var rowId = rowIdPrefix + '-' + (frozen?1:2) + '-' + row[opts.idField];
//				if(frozen){alert(children.length); alert(row + "=====" + rowId);}
				
				table.push('<tr id="' + rowId + '" node-id="' + row[opts.idField] + '" ' + cls + ' ' + style  + ' index-no="' + index + '">');
				//当只是叶子的时候才能渲染表格
				if(!isLeaf){
					table = table.concat(view.renderRow.call(view, target, fields, frozen, depth, row,index));	
				}
				table.push('</tr>');
				index++;
				if (row.children && row.children.length){
					var tt = getTreeData(frozen, depth+1, row.children,false);
					var v = row.state == 'closed' ? 'none' : 'block';
					table.push('<tr class="treegrid-tr-tree"><td style="border:0px" colspan=' + (fields.length + (opts.rownumbers?1:0)) + '><div style="display:' + v + '">');
					table = table.concat(tt);
					table.push('</div></td></tr>');
				}
				
			}
			table.push('</tbody></table>');
			return table;
		}
//		setRowNumbers(target);
//		function setRowNumbers(target){
//			var dc = $.data(target, 'datagrid').dc;
//			var opts = $.data(target, 'treegrid').options;
//			if (!opts.rownumbers) return;
//			var m = 0;
////			dc.body1.find('div.datagrid-cell-rownumber').each(function(i){
////				m = i;
////			});
//			dc.body1.find('div.datagrid-cell-rownumber').each(function(i){
////				$(this).html(index-m + i);
//				$(this).html(1 + i);
//			});
//		}
//		$("div.datagrid-mask-msg").remove();  
//		$("div.datagrid-mask").remove();  
	},
	renderRow: function(target, fields, frozen, depth, row,rowIndex){
		var opts = $.data(target, 'treegrid').options;
		var cc = [];
		
		if (frozen && opts.rownumbers){
			var rownumber = rowIndex + 1;
			cc.push('<td class="datagrid-td-rownumber"><div class="datagrid-cell-rownumber">'+rownumber+'</div></td>');
		}
		
//		if (frozen && opts.rownumbers){
//			cc.push('<td class="datagrid-td-rownumber"><div class="datagrid-cell-rownumber">' + rowIndex++ + '</div></td>'); 
//		}
		for(var i=0; i<fields.length; i++){
			var field = fields[i];
			var col = $(target).datagrid('getColumnOption', field);
			if (col){
				var css = col.styler ? (col.styler(row[field], row)||'') : '';
				var classValue = '';
				var styleValue = '';
				if (typeof css == 'string'){
					styleValue = css;
				} else if (cc){
					classValue = css['class'] || '';
					styleValue = css['style'] || '';
				}
				var cls = classValue ? 'class="' + classValue + '"' : '';
				var style = col.hidden ? 'style="display:none;' + styleValue + '"' : (styleValue ? 'style="' + styleValue + '"' : '');
				
				cc.push('<td field="' + field + '" ' + cls + ' ' + style + '>');
				
				if (col.checkbox){
					var style = '';
				} else {
					var style = styleValue;
					if (col.align){style += ';text-align:' + col.align + ';'}
					if (!opts.nowrap){
						style += ';white-space:normal;height:auto;';
					} else if (opts.autoRowHeight){
						style += ';height:auto;';
					}
				}
				cc.push('<div style="' + style + '" ');
				if (col.checkbox){
					cc.push('class="datagrid-cell-check ');
				} else {
					cc.push('class="datagrid-cell ' + col.cellClass);
				}
				cc.push('">');
				
				if (col.checkbox){
					if (row.checked){
						cc.push('<input type="checkbox" checked="checked"');
					} else {
						cc.push('<input type="checkbox"');
					}
					cc.push(' name="' + field + '" value="' + (row[field]!=undefined ? row[field] : '') + '"/>');
				} else {
					var val = null;
					if (col.formatter){
						val = col.formatter(row[field], row);
					} else {
						val = row[field];
//						val = row[field] || '&nbsp;';
					}
					if (field == opts.treeField){
						for(var j=0; j<depth; j++){
							cc.push('<span class="tree-indent"></span>');
						}
						if (row.state == 'closed'){
							cc.push('<span class="tree-hit tree-collapsed"></span>');
							cc.push('<span class="tree-icon tree-folder ' + (row.iconCls?row.iconCls:'') + '"></span>');
						} else {
							if (row.children && row.children.length){
								cc.push('<span class="tree-hit tree-expanded"></span>');
								cc.push('<span class="tree-icon tree-folder tree-folder-open ' + (row.iconCls?row.iconCls:'') + '"></span>');
							} else {
								cc.push('<span class="tree-indent"></span>');
								cc.push('<span class="tree-icon tree-file ' + (row.iconCls?row.iconCls:'') + '"></span>');
							}
						}
						cc.push('<span class="tree-title">' + val + '</span>');
					} else {
						cc.push(val);
					}
				}
				
				cc.push('</div>');
				cc.push('</td>');
			}
		}
		return cc.join('');
	},
	onBeforeRender: function(target, parentId, data){
		if ($.isArray(parentId)){
			data = {total:parentId.length, rows:parentId};
			parentId = null;
		}
		if (!data) return false;
		var state = $.data(target, 'treegrid');
		var opts = state.options;
		
		
		var temps = data.rows;
		if (data.length == undefined){
			if (data.footer){
				state.footer = data.footer;
			}
			if (data.total){
				state.total = data.total;
			}
			data = this.transfer(target, parentId, data.rows);
			if(!parentId){
				endResults = data;
			}
		} else {
			function setParent(children, parentId){
				for(var i=0; i<children.length; i++){
					var row = children[i];
					row._parentId = parentId;
					if (row.children && row.children.length){
						setParent(row.children, row[opts.idField]);
					}
				}
			}
			setParent(data, parentId);
		}
		var nodes = [];
		if(parentId){
			nodes = this.transfer(target, parentId, temps);
		}
		
		var node = find(target, parentId);
		if (node){
			if (node.children){
				node.children = node.children.concat(nodes);
			} else {
				node.children = nodes;
			}
		} else {
			state.data = state.data.concat(data);
		}
		if(parentId){
			this.treeNodes = nodes;
		}
		this.sort(target, data);
		this.treeLevel = $(target).treegrid('getLevel', parentId);
		//目前treeLevel只能支持2级
		function getDatas(treeNodes,treeLevel){
			--treeLevel;
			var results = [];
			if(treeLevel>=0){
				for(var i=0;i<treeNodes.length;i++){
					var row = treeNodes[i];
					results.push(row);
					if(row.children && row.children.length){
						var tt = getDatas(row.children,treeLevel);
						results = results.concat(tt);
					}
				}
			}
			return results;
		}
		var dc = state.dc;
		var view = this;
		this.rows = undefined;	// the rows to be rendered
		this.r1 = this.r2 = [];	// the first part and last part of rows
		var rootExpand = opts.rootExpand;
		if(opts.isAllExpand){
			isAllExpand = true;
		}
		if(!parentId){
			if(rootExpand){
				endResults = getDatas(endResults,2);
			}
			totals = endResults.length;
			init();
			createHeaderExpander();
		}
		
		if(!parentId){
			dc.body1.add(dc.body2).empty();
		}
		function find(target, idValue){
			var opts = $.data(target, 'treegrid').options;
			var data = $.data(target, 'treegrid').data;
			var cc = [data];
			while(cc.length){
				var c = cc.shift();
				for(var i=0; i<c.length; i++){
					var node = c[i];
					if (node[opts.idField] == idValue){
						return node;
					} else if (node['children']){
						cc.push(node['children']);
					}
				}
			}
			return null;
		}
		function init(){
			// erase the onLoadSuccess event, make sure it can't be triggered
			state.onLoadSuccess = opts.onLoadSuccess;
			opts.onLoadSuccess = function(){};
			setTimeout(function(){
				dc.body2.unbind('.treegrid').bind('scroll.treegrid', function(e){
					if (state.onLoadSuccess){
						opts.onLoadSuccess = state.onLoadSuccess;	// restore the onLoadSuccess event
						state.onLoadSuccess = undefined;
					}
					if (view.scrollTimer){
						clearTimeout(view.scrollTimer);
						view.scrollTimer = undefined;
					}
					view.scrollTimer = setTimeout(function(){
						scrolling.call(view);
					}, 300);
				});
				dc.body2.triggerHandler('scroll.treegrid');
			}, 0);
		}
		function scrolling(){
			if (dc.body2.is(':empty')){
				reload.call(this);
			} else {
				var firstTr = opts.finder.getTr(target, this.index, 'body',2,true);
				var lastTr = opts.finder.getTr(target, 0, 'last', 2);
				var headerHeight = dc.view2.children('div.datagrid-header').outerHeight();
				//没找到对应id的 tr
				if(firstTr.length==0){
					return;
				}
				var top = firstTr.position().top - headerHeight;
				var bottom = lastTr.position().top + lastTr.outerHeight() - headerHeight;
				if(tempFlagNum==0){
					firstDisplayHeight = lastTr.position().top - firstTr.position().top + lastTr.outerHeight();
				}else{
					displayHeight = lastTr.position().top - firstTr.position().top + lastTr.outerHeight();
				}
				if(isIE67){
					top = firstTr.position().top - headerHeight + (this.index)*25;
//					bottom = lastTr.position().top + lastTr.outerHeight() - headerHeight + (this.index)*25;
				}
				
				if (top > dc.body2.height() || bottom < 0){
					reload.call(this);
				} else if (top > 0){
					var page = Math.floor(this.index/opts.pageSize);
					this.getRows.call(this, target, page, function(rows){
						if(!this.r1.length){
							this.r1 = historyRows;
						}
						this.r2 = this.r1;
						this.r1 = rows;
						this.index = (page-1)*opts.pageSize;
						this.rows = this.r1.concat(this.r2);
						this.populate.call(this, target);
					});
				} else if (bottom < dc.body2.height()){
					var page = Math.floor(this.index/opts.pageSize)+2;
					tempFlagNum++;
					if(displayHeight>firstDisplayHeight){
						firstDisplayHeight = displayHeight-firstDisplayHeight;
					}
					if (this.r2.length){
						page++;
					}
					this.getRows.call(this, target, page, function(rows){
						if (!this.r2.length){
							this.r2 = rows;
							if(!this.r1.length){
								this.r1 = historyRows;
							}
						} else {
							this.r1 = this.r2;
							if(!this.r1.length){
								this.r1 = historyRows;
							}
							this.r2 = rows;
							//算如果随便拉滚动条时，进行定位
							if(this.index!=0 && this.index/opts.pageSize){
								this.index += opts.pageSize;
							}else{
								//有节点展开时 ，算index
								this.index += Math.round((displayHeight-firstDisplayHeight)/25);
							}
						}
//						this.index += opts.pageSize;
						if(isAllExpand){
							this.rows = rows;
						}else{
							this.rows = this.r1.concat(this.r2);
						}
						this.populate.call(this, target);
					});
				}
			}
			
			function reload(){
				var top = $(dc.body2).scrollTop();
				var index = Math.floor(top/25);
				var page = Math.floor(index/opts.pageSize) + 1;
				this.getRows.call(this, target, page, function(rows){
					this.index = (page-1)*opts.pageSize;
					this.rows = rows;
					this.r1 = rows;
					//将当前rows赋予 历史rows
					historyRows = rows;
					this.r2 = [];
					this.populate.call(this, target);
					dc.body2.triggerHandler('scroll.treegrid');
				});
			}
		}
		function createHeaderExpander(){
			if (!opts.detailFormatter){return}
			var t = $(target);
			var hasExpander = false;
			var fields = t.datagrid('getColumnFields',true).concat(t.datagrid('getColumnFields'));
			for(var i=0; i<fields.length; i++){
				var col = t.datagrid('getColumnOption', fields[i]);
				if (col.expander){
					hasExpander = true;
					break;
				}
			}
			if (!hasExpander){
				if (opts.frozenColumns && opts.frozenColumns.length){
					opts.frozenColumns[0].splice(0,0,{field:'_expander',expander:true,width:24,resizable:false,fixed:true});
				} else {
					opts.frozenColumns = [[{field:'_expander',expander:true,width:24,resizable:false,fixed:true}]];
				}
				
				var t = dc.view1.children('div.datagrid-header').find('table');
				var td = $('<td rowspan="'+opts.frozenColumns.length+'"><div class="datagrid-header-expander" style="width:24px;"></div></td>');
				if ($('tr',t).length == 0){
					td.wrap('<tr></tr>').parent().appendTo($('tbody',t));
				} else if (opts.rownumbers){
					td.insertAfter(t.find('td:has(div.datagrid-header-rownumber)'));
				} else {
					td.prependTo(t.find('tr:first'));
				}
			}
			
			setTimeout(function(){
				view.bindEvents(target);
			},0);
		}
	},	
	getRows: function(target, page, callback){
		var state = $.data(target, 'treegrid');
		var opts = state.options;
		var index = (page-1)*opts.pageSize;
		var rows = [];
		if(opts.rootExpand){
			var tempDatas = endResults;
			for(var i=0;i<tempDatas.length;i++){
				var row = tempDatas[i];
				if(row.children){
					tempDatas[i].children = [];
					//如果导入的树不是全部展开的
					if(!isAllExpand){
						break;
					}
				}
			}
//			if(index<tempDatas.length){
				if(isAllExpand){
					rows = tempDatas.slice(0, index+opts.pageSize);
				}else{
					rows = tempDatas.slice(index, index+opts.pageSize);
				}
//			}
		}else{
			rows = endResults.slice(index, index+opts.pageSize);
		}
		callback.call(this, rows);
//		var rows = state.data.firstRows.slice(0,50);
//		if (rows.length){//一次性读到数据时
//			
//		} else {//异步每次都加载时
//			var param = $.extend({}, opts.queryParams, {
//				page: page,
//				rows: opts.pageSize
//			});
//			if (opts.sortName){
//				$.extend(param, {
//					sort: opts.sortName,
//					order: opts.sortOrder
//				});
//			}
//			if (opts.onBeforeLoad.call(target, param) == false) return;
//			
//			$(target).treegrid('loading');
//			var result = opts.loader.call(target, param, function(data){
//				$(target).treegrid('loaded');
//				var data = opts.loadFilter.call(target, data);
//				if (data.rows && data.rows.length){
//					callback.call(opts.view, data.rows);
//				}
////				opts.onLoadSuccess.call(target, data);
//			}, function(){
//				$(target).treegrid('loaded');
//				opts.onLoadError.apply(target, arguments);
//			});
//			if (result == false){
//				$(target).treegrid('loaded');
//			}
//		}
	},
	
	populate: function(target){
		var state = $.data(target, 'treegrid');
		var opts = state.options;
		var dc = state.dc;
		dc.body1.add(dc.body2).empty();
		var rowHeight = 25;
		if (this.rows.length){
			opts.view.render.call(opts.view, target, dc.body2, false);
			opts.view.render.call(opts.view, target, dc.body1, true);
			if(isIE67){
				if(isAllExpand){
					dc.body1.add(dc.body2).children('table.datagrid-btable').css({
						marginTop: 0*rowHeight,
						marginBottom: totals*rowHeight - this.rows.length*rowHeight - this.index*rowHeight					
					});
				}else{
					dc.body1.add(dc.body2).children('table.datagrid-btable').css({
						marginTop: this.index*rowHeight,
						marginBottom: totals*rowHeight - this.rows.length*rowHeight - this.index*rowHeight
					});
//					var ptop = this.index*rowHeight;
//					alert(dc.body2.find('table.datagrid-btable').first().html());
//					var pboo = totals*rowHeight - this.rows.length*rowHeight - this.index*rowHeight;
//					dc.view2.children("div .datagrid-body").css("position","relative");
					
//					dc.body2.find('table.datagrid-btable').first().wrapAll("<div id='dfd' sizset='true'></div>");
//					dc.body2.find('table.datagrid-btable').first().css("position","absolute");  
					
//					$('#dfd').css("position","relative");
//					$('#dfd').css("position","absolute");
//					$('#dfd').css("top","0");
//					
//					$('#dfd').css("padding-top",this.index*rowHeight + "px");
//					$('#dfd').css("padding-bottom","10000px");  
//					$('#dfd').css("background","none"); 
//					$('#dfd').css("padding-bottom","10000px");
					
				} 
			}else{
				if(isAllExpand){
					dc.body1.add(dc.body2).children('table.datagrid-btable').css({
						paddingTop: 0*rowHeight,
						paddingBottom: totals*rowHeight - this.rows.length*rowHeight - this.index*rowHeight
					});
				}else{
					dc.body1.add(dc.body2).children('table.datagrid-btable').css({
						paddingTop: this.index*rowHeight,
						paddingBottom: totals*rowHeight - this.rows.length*rowHeight - this.index*rowHeight
					});
//					dc.body1.add(dc.body2).children('table.datagrid-btable').css("position","relative");
//					var s = totals*rowHeight - this.rows.length*rowHeight - this.index*rowHeight;
//					dc.body1.add(dc.body2).children('table.datagrid-btable').css("padding-bottom",s + "px");
//				dc.view2.children("div .datagrid-body").css("position","relative");
//					style="position:relative"
				}
//				alert(dc.body1.add(dc.body2).children('table.datagrid-btable').position().top);
				//alert(dc.body1.add(dc.body2).children('table.datagrid-btable').height());
			}
			var r = [];
			for(var i=0; i<this.index; i++){
				r.push({});
			}
			state.data.rows = r.concat(this.rows);
			opts.onLoadSuccess.call(target, {
				total: state.data.total,
				rows: this.rows
			});
		}
	}
	

});
