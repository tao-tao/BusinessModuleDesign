(function ($) {
	function init(target) {
		var opt = $.data(target, "toolbar").options;
		
		$.each($(target).children(), function () {
			var itemOpt = parseOptions($(this));
			opt.items.push(itemOpt);
		});
		
		if (!opt.items)
			return;
			
		$(target).empty();
		
		if (opt.randerTo) {
			$(target).appendTo(opt.randerTo);
		}
		
		$(target).css({
			height: 28,
			background: '#EFEFEF',
			padding: '3px 2px 1px 2px'
		});
		
		
		$.each(opt.items, function () {
			var items = this;
			if (typeof items[0] === 'string' && items[0] === "-") {
				$('<div/>').appendTo(target).css({
					height: 24,
					borderLeft: '1px solid #CCC',
					borderRight: '1px solid white',
					margin: '2px 1px'
				});
			}else{
				items.type = items.type || 'button';
				items = $.extend(items,{plain : true});
				var btn = $("<a/>");
				if(!items.href){
					items.href =  "javascript:void(0)";
				}
				btn.attr("href", "javascript:void(0)");
				
				if(items.onclick){
					items.href =  "javascript:void(0)";
					btn.attr("onclick", items.onclick);
				}else if(items.handler && typeof items.handler === 'function'){
					btn.click(items.handler);
				}
				
				btn.appendTo(target);
				
				if (items.type == 'button') {
					btn.linkbutton(items);
				} else if (items.type == 'menubutton') {
					btn.menubutton(items);
				}
			}
		});
		
		$(target).children().css("float","left");
		
		function parseOptions(t) {
			var opt = {
				id : t.attr("id"),
				disabled : (t.attr("disabled") ? true : undefined),
				plain : true,
				text : $.trim(t.html()),
				iconCls : (t.attr("icon") || t.attr("iconCls")),
				type : 'button',
				href:t.attr("href"),
				onclick:t.attr("onclick")
			};
			if (t.attr("type") && t.attr("type") != 'button' || t.attr("menu")) {
				opt = $.extend(opt, {
						menu : t.attr("menu"),
						duration : t.attr("duration"),
						type : 'menubutton'
					});
			}
			return opt;
		}
	}
	
	$.fn.toolbar = function (options, params) {
		if (typeof options === 'string') {
			return $(this).toolbar.methods[options].call(this,params);
		}
		
		options = options || {};
		return this.each(function () {
			var opt = $.data(this, "toolbar");
			if (opt) {
				$.extend(opt.options, options);
			} else {
				$.data(this, "toolbar", {
					options : $.extend({}, $.fn.toolbar.defaults, options)
				});
				init(this);
			}
		});
	};
	
	$.fn.toolbar.methods = {
		options : function () {
			return this.data().toolbar.options;
		},
		disabledALl:function(){
			return this.each(function(){
				var items = $(this).data().toolbar.options.items;
				var target = $(this);
				$.each(items,function(i,v){
						var ld = target.children().eq(i);
						if(v != "-"){
							if(v.type == 'menubutton'){
								ld.menubutton('disable');
							}else{
								ld.linkbutton('disable');
							}
							if(v.handler){
								ld.unbind('click');
							}
						}
				})
			});
		},
		enableAll:function(){
			return this.each(function(){
				var items = $(this).data().toolbar.options.items;
				var target = $(this);
				$.each(items,function(i,v){
						var ld = target.children().eq(i);
						if(v != "-"){
							if(v.type == 'menubutton'){
								ld.menubutton('enable');
							}else{
								ld.linkbutton('enable');
							}
							
							if(v.handler){
								ld.click(v.handler);
							}
						}
				})
			});
		},
		disabled:function(text){
			return this.each(function(){
				var items = $(this).data().toolbar.options.items;
				var target = $(this);
				$.each(items,function(i,v){
					if(v.text == text){
						var ld = target.children().eq(i);
						if(v.type == 'menubutton'){
							ld.menubutton('disable');
						}else{
							ld.linkbutton('disable');
						}
						if(v.handler){
							ld.unbind('click');
						}
					}
				})
			});
		},
		enable:function(text){
			return this.each(function(){
				var items = $(this).data().toolbar.options.items;
				var target = $(this);
				$.each(items,function(i,v){
					if(v.text == text){
						var ld = target.children().eq(i);
						if(v.type == 'menubutton'){
							ld.menubutton('enable');
						}else{
							ld.linkbutton('enable');
						}
						if(v.handler){
							ld.click(v.handler);
						}
					}
				})
			});
		}
	};
	
	$.fn.toolbar.defaults = {
		randerTo : null,
		items : []
	};
	
	if ($.parser) {
		$.parser.plugins.push('toolbar');
	}
})(jQuery);
