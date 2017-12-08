(function($){

	/**
	 * 用class名称加载
	 */
	$(function(){
		if($('.easyui-subwindow').length > 0){
			$('.easyui-subwindow').subwindow({});
		}
	});
	
	/**
	 * 初始化
	 */
	function init(target){
		var t = $(target);
		var style = t.attr('style');
		t.addClass('subwindow-f').hide();
		
		var span = $(
				'<span class="combo">' +
				'<input type="text" class="combo-text" autocomplete="off" readonly="readonly" style="'+ style +'" value="'+ t.val() +'">' +
				'<span><span class="subwindow-arrow"></span></span>' +
				'<input type="hidden" class="combo-value" value="'+ t.val() +'">' +
				'</span>'
				).insertAfter(target);
		
		
		//替换input
		var name = $(target).attr('name');
		if (name){
			span.find('input.combo-value').attr('name', name);
			$(target).removeAttr('name').attr('comboName', name);
		}

		return {
			subwindow: span
		};
	}
	
	/**
	 * 修改大小
	 */
	function setSize(target){
		var state = $.data(target, 'subwindow');
		var subwindow = state.subwindow;
		
		var c = $(target).clone();
		c.css('visibility','hidden');
		c.appendTo('body');
		var width = c.outerWidth();
		var height = c.outerHeight();
		c.remove();

		var input = subwindow.find('input.combo-text');
		var arrow = subwindow.find('.subwindow-arrow');
		var arrowWidth = arrow._outerWidth();
		
		subwindow._outerWidth(width)._outerHeight(height);
		input._outerWidth(subwindow.width() - arrowWidth);
		input.css({
			height: subwindow.height()+'px',
			lineHeight: subwindow.height()+'px'
		});
		arrow._outerHeight(subwindow.height());
		subwindow.insertAfter(target);
	}
	
	/**
	 * 绑定事件
	 */
	function bindEvents(target){
		var state = $.data(target, 'subwindow');
		var opts = state.options;
		var subwindow = state.subwindow;
		var arrow = subwindow.find('.subwindow-arrow');

		arrow.bind('click.combo', function(){
			opts.onClick.call(this);
		}).bind('mouseenter.combo', function(){
			$(this).addClass('subwindow-arrow-hover');
		}).bind('mouseleave.combo', function(){
			$(this).removeClass('subwindow-arrow-hover');
		});
	}
	
	/**
	 * 构造方法
	 */
	$.fn.subwindow = function(options, param){
		if (typeof options == 'string'){
			return $.fn.subwindow.methods[options](this, param);
		}
		options = options || {};
		return this.each(function(){
			var state = $.data(this, 'subwindow');
			if (state){
				$.extend(state.options, options);
			} else {
				var r = init(this);
				state = $.data(this, 'subwindow', {
					options: $.extend({}, $.fn.subwindow.defaults, $.fn.subwindow.parseOptions(this), options),
					subwindow: r.subwindow,
					previousValue: null
				});
				$(this).removeAttr('disabled');
			}
			setSize(this);
			bindEvents(this);
		});
	};
	
	/**
	 * 方法
	 */
	$.fn.subwindow.methods = {
		options: function(jq){
			return $.data(jq[0], 'subwindow').options;
		},
		clear: function(jq){
			return jq.each(function(){
				clear(this);
			});
		},
		getValue: function(jq){
			return getValue(jq[0]);
		},
		setValue: function(jq, value){
			return jq.each(function(){
				setValue(this, value);
			});
		},
		destroy: function(jq){
			return jq.each(function(){
				destroy(this);
			});
		}
	};
	
	/**
	 * 获取参数
	 */
	$.fn.subwindow.parseOptions = function(target){
		return $.extend({}, $.parser.parseOptions(target, []));
	};

	/**
	 * 默认参数
	 */
	$.fn.subwindow.defaults = {
		onClick: function(){}
	};
	
	/**
	 * 获取值
	 */
	function getValue(target){
		var state = $.data(target, 'subwindow');
		var subwindow = state.subwindow;
		return subwindow.find('input.combo-value').val();
	}
	
	/**
	 * 设置值
	 */
	function setValue(target, value){
		var state = $.data(target, 'subwindow');
		var subwindow = state.subwindow;
		subwindow.find('input.combo-value').val(value);
		subwindow.find('input.combo-text').val(value);
	}
	
	/**
	 * 清除内容
	 */
	function clear(target){
		var state = $.data(target, 'subwindow');
		var subwindow = state.subwindow;
		subwindow.find('input.combo-value').val('');
		subwindow.find('input.combo-text').val('');
	}
	
	/**
	 * 摧毁方法
	 */
	function destroy(target){
		var state = $.data(target, 'subwindow');
		var input = state.subwindow.find('input.combo-text');
		input.validatebox('destroy');
		state.panel.panel('destroy');
		state.subwindow.remove();
		$(target).remove();
	}
})(jQuery);
