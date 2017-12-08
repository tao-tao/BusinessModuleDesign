/**
 * @author zhanglei
 */

/**
 * 扩展easyui-validationbox验证规则增加对日期的验证
 */
$.extend($.fn.validatebox.defaults.rules, {    
    past: {    
    		validator: function(value,param){
    			return compareDay(value) <= 0;
    		},
    		message :"日期不能大于今天！"
    }    
}); 

$.extend($.fn.validatebox.defaults.rules, {    
    def: {    
        validator: function(value,param){
        	if(typeof($.fn.validatebox.defaults.rules.unique[param[0]]) == 'undefined'){
        		return true;
        	}
        	if($.fn.validatebox.defaults.rules.unique[param[0]] == true){
        		return true;
        	}else{
        		$.fn.validatebox.defaults.rules.def.message = $.fn.validatebox.defaults.rules.unique[param[0]+'msg'];
        		return false;
        	}
        	   
        },
        message:''
    }    
});

/**
 * 扩展easyui-validationbox验证规则增加对唯一性的验证
 */
$.extend($.fn.validatebox.defaults.rules, {    
	unique: {    
        validator: function(value,param){ 
        	var r={};
        	//debugger;
        	$.ajax({
    			url : param[0],
    			data : {value : value},
    			type : 'GET',
    			async : false,
    			dataType : 'json',
    			success : function(result) {
    				r = result;
    			}
    		});
        	if(r.result == true){
        		$.fn.validatebox.defaults.rules.unique[param[1]]=true;
        		return true;
        	}else{
        		$.fn.validatebox.defaults.rules.unique[param[1]] = false;
        		if(r.msg){
        			$.fn.validatebox.defaults.rules.unique.message =r.msg;
        			$.fn.validatebox.defaults.rules.unique[param[1]+'msg']=r.msg;
        		}
        		return false;
        	}
        }
    }  
});  
/**
 * 扩展easyui-validationbox验证规则增加最小长度的验证
 */
$.extend($.fn.validatebox.defaults.rules, {    
    minLength: {    
        validator: function(value, param){    
            return value.length >= param[0];    
        },    
        message: 'Please enter at least {0} characters.'   
    }    
}); 
/**
 * 扩展easyui编辑器，增加对日期格式的编辑
 */
$.extend($.fn.datagrid.defaults.editors, {
	datebox : {
		init : function(container, options) {
			var input = $('<input />').appendTo(container);
			input.datebox(options);
			return input;
		},
		destroy : function(target) {
			$(target).datebox('destroy');
		},
		getValue : function(target) {
			return $(target).datebox('getValue');
		},
		setValue : function(target, value) {
			$(target).datebox('setValue', formatDatebox(value));
		},
		resize : function(target, width) {
			$(target).datebox('resize', width);
		}
	}
});
/**
 * 扩展easyui编辑器，增加对日期时间格式的编辑
 */
$.extend($.fn.datagrid.defaults.editors, {
    datetimebox: {//datetimebox就是你要自定义editor的名称
        init: function(container, options){
            var input = $('<input />').appendTo(container);
            input.datetimebox(options);
            return input.datetimebox({
                formatter:function(date){
                    return new Date(date).format("yyyy-MM-dd hh:mm:ss");
                }
            });
        },
        getValue: function(target){
            return $(target).parent().find('input.combo-value').val();
       },
        setValue: function(target, value){
            $(target).datetimebox("setValue",value);
        },
        resize: function(target, width){
           /*var input = $(target);
           if ($.boxModel == true){
                input.width(width - (input.outerWidth() - input.width()));
           } else {
                input.width(width);
            }*/
        	$(target).datetimebox('resize', width);
        }
    }
});


//在layout的panle全局配置中,增加一个onCollapse处理title
$.extend($.fn.layout.paneldefaults, {
	onCollapse : function () {
		//获取layout容器
		var layout = $(this).parents("div.layout");
		//获取当前region的配置属性
		var opts = $(this).panel("options");
		//获取key
		var expandKey = "expand" + opts.region.substring(0, 1).toUpperCase() + opts.region.substring(1);
		
		if(layout.data("layout")){//如果布局中存在此属性
			//从layout的缓存对象中取得对应的收缩对象
			var expandPanel = layout.data("layout").panels[expandKey];
			//针对横向和竖向的不同处理方式
			if (opts.region == "west" || opts.region == "east") {
				//竖向的文字打竖,其实就是切割文字加br
				var split = [];
				for (var i = 0; i < opts.title.length; i++) {
					split.push(opts.title.substring(i, i + 1));
				}
				expandPanel.panel("body").addClass("panel-title").css("text-align", "center").html(split.join("<br>"));
			} else {
				expandPanel.panel("setTitle", "&nbsp;&nbsp;&nbsp;&nbsp;" + opts.title + "<div class=\"panel-icon "+ opts.iconCls +"\"/>");
			}
		}
		
	}
});

/**
 * 格式化日期 以供datagrid中日期控件使用
 * @param value 要格式化的日期
 * @returns 格式化完成的日期
 */
function formatDatebox(value) {
	if (value == null || value == '') {
		return '';
	}
	var dt;

	if (value instanceof Date) {
		dt = value;
	} else {
		//dt = new Date(Date.parse(value.replace(/-/g,"/")));
		if (!isNaN(value)){//正确的日期格式直接返回
			dt = new Date(value);
		}else{
			dt = new Date(Date.parse(value.replace(/-/g,"/")));
		}

		
	}
	return dt.format("yyyy-MM-dd"); //这里用到一个javascript的Date类型的拓展方法，这个是自己添加的拓展方法，在后面的步骤3定义
};

/**
 * 格式化日期 以供datagrid中日期控件使用
 * @param value 要格式化的日期
 * @returns 格式化完成的日期
 */
function formatDateTimebox(value) {
	if (value == null || value == '') {
		return '';
	}
	var dt;
	if (value instanceof Date) {
		dt = value;
	} else {
		//dt = new Date(Date.parse(value.replace(/-/g,"/")));
		if (!isNaN(value)){//正确的日期格式直接返回
			dt = new Date(value);
		}else{
			dt = new Date(Date.parse(value.replace(/-/g,"/")));
		}

	}
	return dt.format("yyyy-MM-dd hh:mm:ss"); //这里用到一个javascript的Date类型的拓展方法，这个是自己添加的拓展方法，在后面的步骤3定义
};
/**      
 * 对Date的扩展，将 Date 转化为指定格式的String      
 * 月(M)、日(d)、12小时(h)、24小时(H)、分(m)、秒(s)、周(E)、季度(q) 可以用 1-2 个占位符      
 * 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)      
 * eg:      
 * (new Date()).pattern("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423      
 * (new Date()).pattern("yyyy-MM-dd E HH:mm:ss") ==> 2009-03-10 二 20:09:04      
 * (new Date()).pattern("yyyy-MM-dd EE hh:mm:ss") ==> 2009-03-10 周二 08:09:04      
 * (new Date()).pattern("yyyy-MM-dd EEE hh:mm:ss") ==> 2009-03-10 星期二 08:09:04      
 * (new Date()).pattern("yyyy-M-d h:m:s.S") ==> 2006-7-2 8:9:4.18      
 */ 
 Date.prototype.pattern=function(fmt) {         
	    var o = {         
	    "M+" : this.getMonth()+1, //月份         
	    "d+" : this.getDate(), //日         
	    "h+" : this.getHours()%12 == 0 ? 12 : this.getHours()%12, //小时         
	    "H+" : this.getHours(), //小时         
	    "m+" : this.getMinutes(), //分         
	    "s+" : this.getSeconds(), //秒         
	    "q+" : Math.floor((this.getMonth()+3)/3), //季度         
	    "S" : this.getMilliseconds() //毫秒         
	    };         
	    var week = {         
	    "0" : "\u65e5",         
	    "1" : "\u4e00",         
	    "2" : "\u4e8c",         
	    "3" : "\u4e09",         
	    "4" : "\u56db",         
	    "5" : "\u4e94",         
	    "6" : "\u516d"        
	    };         
	    if(/(y+)/.test(fmt)){         
	        fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));         
	    }         
	    if(/(E+)/.test(fmt)){         
	        fmt=fmt.replace(RegExp.$1, ((RegExp.$1.length>1) ? (RegExp.$1.length>2 ? "\u661f\u671f" : "\u5468") : "")+week[this.getDay()+""]);         
	    }         
	    for(var k in o){         
	        if(new RegExp("("+ k +")").test(fmt)){         
	            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));         
	        }         
	    }         
	    return fmt;         
};

/*为password字段添加一个editor
	$("#gridId").datagrid('addEditor', { 
             field : 'password', 
             editor : { 
                 type : 'validatebox', 
                 options : { 
                     required : true 
                 } 
             } 
         }); 
	删除password的editor
	$("#gridid").datagrid('removeEditor', 'password'); */
/**
 * 扩展easyui-datagrid组件方法，增加添加编辑框方法
 */
$.extend($.fn.datagrid.methods, { 
    addEditor : function(jq, param) { 
        if (param instanceof Array) { 
            $.each(param, function(index, item) { 
                var e = $(jq).datagrid('getColumnOption', item.field); 
                e.editor = item.editor; 
            }); 
        } else { 
            var e = $(jq).datagrid('getColumnOption', param.field); 
            e.editor = param.editor; 
        } 
    }, 
    removeEditor : function(jq, param) { 
        if (param instanceof Array) { 
            $.each(param, function(index, item) { 
                var e = $(jq).datagrid('getColumnOption', item); 
                e.editor = {}; 
            }); 
        } else { 
            var e = $(jq).datagrid('getColumnOption', param); 
            e.editor = {}; 
        } 
    } 
});
/**
 *  比较2个日期，格式必须为yyyy-MM-dd,如果第二个参数不存在默认比较对象为今天
 * @param date1
 * @param data2
 * @returns {Number}
 */
function compareDay(date1,data2){
	var comparedDay;
	if(!data2){
		comparedDay =  new Date();
	}else{
		comparedDay = new Date(Date.parse(date1.replace(/-/g,"/")));
	}
	var compareDay = new Date(Date.parse(date1.replace(/-/g,"/")));
	
	return compareDay.getTime() - comparedDay.getTime();
};
/*动态加载javascrpt函数*/
function loadScript(url, callback){
	var doc = document;
    var script = doc.createElement("script")
    script.type = "text/javascript";
    if (script.readyState){  //IE
        script.onreadystatechange = function(){
            if (script.readyState == "loaded" || script.readyState == "complete"){
                script.onreadystatechange = null;
                callback();
            }
        };
    } else {  //Others
        script.onload = function(){
            callback();
        };
    }
    script.src = url;
    doc.getElementsByTagName("head")[0].appendChild(script);
};

/**
 * 拓展数组方法 删除
 * @param dx
 * @returns {Boolean}
 */
Array.prototype.remove=function(dx){
	if(isNaN(dx)||dx>this.length){
		return false;
	}
	for ( var i = 0, n = 0; i < this.length; i++) {
		if (this[i] != this[dx]) {
			this[n++] = this[i]
		}
	}
	this.length -= 1
};
/**
 * @param obj 数组类型，实现datagrid分页后，将选中的所有条目放入obj内，以便分页选中后，提交 
 * 取消点击行,就选中一行记录，而是选中checkbox时，才选中一行
 */
function bindRowsEvent(obj){
    var panel = myDatagrid.datagrid('getPanel');
    var rows = panel.find('tr[datagrid-row-index]');
    rows.unbind('click').bind('click',function(e){
        return false;
    });
    rows.find('div.datagrid-cell-check input[type=checkbox]').unbind().bind('click', function(e){
        var index = $(this).parent().parent().parent().attr('datagrid-row-index');
        if ($(this).attr('checked')){
        	myDatagrid.datagrid('selectRow', index);
        	if(obj instanceof Array){
        		var row = myDatagrid.datagrid('getRows')[index];
        		obj.push(row);
        	}
        } else {
        	myDatagrid.datagrid('unselectRow', index);
        	if(obj instanceof Array){
        		var row = myDatagrid.datagrid('getRows')[index];
        		if(obj.length>0){
        			for(var i=0;i<obj.length;i++){
            			if(obj[i]['id'] == row['id']){
            				obj.remove(i);
            			}
            		}
        		}
        	}
        }
        e.stopPropagation();
    });
};
/**
 * datagrid根据id影藏checkbox
 * @param id
 */
function hiddenCheckBox(id){
	var a = $('div .datagrid-cell-check');
	$('div .datagrid-cell-check').children("input[name='id']").each(function (){
		var currentId = this.value; 
		if(currentId == id){
			$(this).attr("disabled","disabled");
			$(this).css("display", "none");
		}
	})
};
/**
 * datagrid单元格增加tooltip
 */
$.extend($.fn.datagrid.methods, {      
    /** 
     * 开打提示功能    
     * @param {} jq    
     * @param {} params 提示消息框的样式    
    * @return {}    
     */     
    doCellTip:function (jq, params) {      
        function showTip(showParams, td, e, dg) {      
            //无文本，不提示。      
            if ($(td).text() == "") return;      
               
           params = params || {};   
          var options = dg.data('datagrid');      
            showParams.content = '<div class="tipcontent">' + showParams.content + '</div>';      
            $(td).tooltip({      
                content:showParams.content,      
                trackMouse:true,      
                position:params.position,      
                onHide:function () {      
                   $(this).tooltip('destroy');      
             },      
               onShow:function () {      
                    var tip = $(this).tooltip('tip');      
                    if(showParams.tipStyler){      
                        tip.css(showParams.tipStyler);      
                   }      
                  if(showParams.contentStyler){      
                       tip.find('div.tipcontent').css(showParams.contentStyler);      
                   }   
              }      
           }).tooltip('show');      
    
       };      
        return jq.each(function () {      
            var grid = $(this);      
           var options = $(this).data('datagrid');      
            if (!options.tooltip) {      
                var panel = grid.datagrid('getPanel').panel('panel');      
                panel.find('.datagrid-body').each(function () {      
                  var delegateEle = $(this).find('> div.datagrid-body-inner').length ? $(this).find('> div.datagrid-body-inner')[0] : this;      
                    $(delegateEle).undelegate('td', 'mouseover').undelegate('td', 'mouseout').undelegate('td', 'mousemove').delegate('td[field]', {      
                      'mouseover':function (e) {   
                            //if($(this).attr('field')===undefined) return;      
                          var that = this;   
                            var setField = null;   
                            if(params.specialShowFields && params.specialShowFields.sort){   
                                for(var i=0; i<params.specialShowFields.length; i++){   
                                    if(params.specialShowFields[i].field == $(this).attr('field')){   
                                        setField = params.specialShowFields[i];   
                                   }   
                               }   
                            }   
                           if(setField==null){   
                                options.factContent = $(this).find('>div').clone().css({'margin-left':'-5000px', 'width':'auto', 'display':'inline', 'position':'absolute'}).appendTo('body');      
                                var factContentWidth = options.factContent.width();      
                               params.content = $(this).text();      
                                if (params.onlyShowInterrupt) {      
                                    if (factContentWidth > $(this).width()) {      
                                       showTip(params, this, e, grid);      
                                    }      
                               } else {      
                                    showTip(params, this, e, grid);      
                                }    
                            }else{   
                               panel.find('.datagrid-body').each(function(){   
                                    var trs = $(this).find('tr[datagrid-row-index="' + $(that).parent().attr('datagrid-row-index') + '"]');   
                                    trs.each(function(){   
                                       var td = $(this).find('> td[field="' + setField.showField + '"]');   
                                        if(td.length){   
                                            params.content = td.text();   
                                       }   
                                    });   
                               });   
                               showTip(params, this, e, grid);   
                          }   
                       },      
                       'mouseout':function (e) {      
                            if (options.factContent) {      
                                options.factContent.remove();      
                                options.factContent = null;      
                            }      
                       }      
                    });      
                });      
            }      
        });      
    },      
    /** 
     * 关闭消息提示功能    
    * @param {} jq    
     * @return {}    
     */     
    cancelCellTip:function (jq) {      
        return jq.each(function () {      
            var data = $(this).data('datagrid');      
            if (data.factContent) {      
                data.factContent.remove();      
                data.factContent = null;      
            }      
            var panel = $(this).datagrid('getPanel').panel('panel');      
           panel.find('.datagrid-body').undelegate('td', 'mouseover').undelegate('td', 'mouseout').undelegate('td', 'mousemove')      
        });      
    }      
});