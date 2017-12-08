/**
 * 初始化
 */
function initCronExprSelect(){
	//初始化月份选项
	changeRadioMonth();
	$("input[name='radioMonth']").bind('click', function(){
		changeRadioMonth();
	});
	
	//初始化星期选项
	changeUseWeek();
	$("#useWeek").bind('click', function(){
		changeUseWeek();
	});
	$("input[name='radioWeek']").bind('click', function(){
		changeRadioWeek();
	});
	
	//初始化天选项
	changeRadioDay();
	$("input[name='radioDay']").bind('click', function(){
		changeRadioDay();
	});
	
	//初始化小时选项
	changeRadioHour();
	$("input[name='radioHour']").bind('click', function(){
		changeRadioHour();
	});
	
	//初始化分钟选项
	changeRadioMinute();
	$("input[name='radioMinute']").bind('click', function(){
		changeRadioMinute();
	});
	//初始化秒选项
	changeRadioSecond();
	$("input[name='radioSecond']").bind('click', function(){
		changeRadioSecond();
	});
}

/**
 * 改变月份选项
 */
function changeRadioMonth(){
	var radioMonth = $("input[name='radioMonth']:checked").val();
	if(radioMonth == 'loop'){
		$("input[name='month']").attr('disabled', true);
	}else{
		$("input[name='month']").attr('disabled', false);
	}
}

/**
 * 改变是否使用星期
 */
function changeUseWeek(){
	var useWeek = $("#useWeek:checked").val();
	if(useWeek == 'yes'){
		$("input[name='radioWeek']").attr('disabled', false);
		 changeRadioWeek();
	}else{
		$("input[name='radioWeek']").attr('disabled', true);
		$("input[name='week']").attr('disabled', true);
	}
}

/**
 * 改变星期选项
 */
function changeRadioWeek(){
	var radioWeek = $("input[name='radioWeek']:checked").val();
	if(radioWeek == 'loop'){
		$("input[name='week']").attr('disabled', true);
	}else{
		$("input[name='week']").attr('disabled', false);
	}
}

/**
 * 改变天选项
 */
function changeRadioDay(){
	var radioDay = $("input[name='radioDay']:checked").val();
	if(radioDay == 'loop'){
		$("input[name='day']").attr('disabled', true);
	}else{
		$("input[name='day']").attr('disabled', false);
	}
}

/**
 * 改变小时选项
 */
function changeRadioHour(){
	var radioHour = $("input[name='radioHour']:checked").val();
	if(radioHour == 'loop'){
		$("input[name='hour']").attr('disabled', true);
	}else{
		$("input[name='hour']").attr('disabled', false);
	}
}

/**
 * 改变分钟选项
 */
function changeRadioMinute(){
	var radioMinute = $("input[name='radioMinute']:checked").val();
	if(radioMinute == 'loop'){
		$("input[name='minute']").attr('disabled', true);
		$("#beginMinute").attr('disabled', false);
		$("#sepMinute").attr('disabled', false);
	}else{
		$("input[name='minute']").attr('disabled', false);
		$("#beginMinute").attr('disabled', true);
		$("#sepMinute").attr('disabled', true);
	}
}

/**
 * 改变秒选项
 */
function changeRadioSecond(){
	var radioSecond = $("input[name='radioSecond']:checked").val();
	if(radioSecond == 'loop'){
		$("input[name='second']").attr('disabled', true);
		$("#beginSecond").attr('disabled', false);
		$("#sepSecond").attr('disabled', false);
	}else{
		$("input[name='second']").attr('disabled', false);
		$("#beginSecond").attr('disabled', true);
		$("#sepSecond").attr('disabled', true);
	}
}

/**
 * 获取Cron表达式和解析Cron表达式的类
 * @returns {CronExprObject}
 */
CronExprObject = function() {
	var records = new Array();
	/**
	 * 获取设置的表达式数据
	 */
	this.cronExpression = function(){
		_addSecondLoop();
		_addSecondFix();
		_addMinuteLoop();
		_addMinuteFix();
		_addHourLoop();
		_addHourFix();
		_addDayLoop();
		_addDayFix();
		_addMonthLoop();
		_addMonthFix();
		_addWeekLoop();
		_addWeekFix();
		_addWeekEnabled();
		return records;
	};
	
	_addResultRecord = function(type, value, enabled) {
		records.push({
			"type_" : type,
			"value_" : value,
			"enabled_" : enabled
		});
	};
	
	_isCheckableControlChecked = function(radio) {
		var checked = false;
		if (radio.attr('checked') == true|| radio.attr('checked') ==  "checked") {
			checked = true;
		}
		return checked;
	};
	
	_getInputControlValues = function(controlName) {
		var controlArray = $("input[name='"+ controlName +"']:checked");
		var valueString = "";
		for ( var i = 0; i < controlArray.length; i++) {
			var input = controlArray[i];
			valueString += $(input).val() + ",";
		}
		return valueString;
	};

	_addSecondLoop = function() {
		var beginSecond = $("#beginSecond");
		var sepSecond = $("#sepSecond");
		var control = $("#secondLoop");
		var enabled = _isCheckableControlChecked(control);
		_addResultRecord("01", beginSecond.val() + "," + sepSecond.val(), enabled);
	};
	
	_addSecondFix = function() {
		var valueString = _getInputControlValues("second");
		var control = $("#secondFix");
		var enabled = _isCheckableControlChecked(control);
		_addResultRecord("02", valueString, enabled);
	};
	
	_addMinuteLoop = function() {
		var beginMinute = $("#beginMinute");
		var sepMinute = $("#sepMinute");
		var control = $("#minuteLoop");
		var enabled = _isCheckableControlChecked(control);
		_addResultRecord("11", beginMinute.val() + "," + sepMinute.val(),enabled);
	};
	
	_addMinuteFix = function() {
		var valueString = _getInputControlValues("minute");
		var control = $("#minuteFix");
		var enabled = _isCheckableControlChecked(control);
		_addResultRecord("12", valueString, enabled);
	};
	
	_addHourLoop = function() {
		var control = $("#hourLoop");
		var enabled = _isCheckableControlChecked(control);
		_addResultRecord("21", "", enabled);
	};
	
	_addHourFix = function() {
		var valueString = _getInputControlValues("hour");
		var control = $("#hourFix");
		var enabled = _isCheckableControlChecked(control);
		_addResultRecord("22", valueString, enabled);
	};
	
	_addDayLoop = function() {
		var control = $("#dayLoop");
		var enabled = _isCheckableControlChecked(control);
		_addResultRecord("31", "", enabled);
	};
	
	_addDayFix = function() {
		var valueString = _getInputControlValues("day");
		var control = $("#dayFix");
		var enabled = _isCheckableControlChecked(control);
		_addResultRecord("32", valueString, enabled);
	};
	
	_addMonthLoop = function() {
		var control = $("#monthLoop");
		var enabled = _isCheckableControlChecked(control);
		_addResultRecord("41", "", enabled);
	};
	
	_addMonthFix = function() {
		var valueString = _getInputControlValues("month");
		var control = $("#monthFix");
		var enabled = _isCheckableControlChecked(control);
		_addResultRecord("42", valueString, enabled);
	};
	
	_addWeekLoop = function() {
		var control = $("#weekLoop");
		var enabled = _isCheckableControlChecked(control);
		_addResultRecord("51", "", enabled);
	};
	
	_addWeekFix = function() {
		var valueString = _getInputControlValues("week");
		var control = $("#weekFix");
		var enabled = _isCheckableControlChecked(control);
		_addResultRecord("52", valueString, enabled);
	};
	
	_addWeekEnabled = function() {
		var weekEnabledCheckbox = $("#useWeek");
		var enabled = _isCheckableControlChecked(weekEnabledCheckbox);
		_addResultRecord("50", "", enabled);
	};
	
	var $currentSecondString = '';
	var $currentMinuteString = '';
	var $currentHourString = '';
	var $currentDayString = '';
	var $currentMonthString = '';
	var $currentWeekString = '';
	
	/**
	 * 解析表达式
	 */
	this.parseCronToUI = function(cronExpr) {
		var partArray = cronExpr.split(" ");
		if (6 == partArray.length) {
			var secondString = partArray[0];
			var minuteString = partArray[1];
			var hourString = partArray[2];
			var dayString = partArray[3];
			var monthString = partArray[4];
			var weekString = partArray[5];
			
			$currentSecondString = secondString;
			$currentMinuteString = minuteString;
			$currentHourString = hourString;
			$currentDayString = dayString;
			$currentMonthString = monthString;			
			$currentWeekString = weekString;

			return true;
		}else{
			return false;
		}
	};
	
	/**
	 * 设置时间指定的选项
	 */
	_setInputControlValues = function(controlName, timeValues) {
		var controlsArray = $("input[name='"+ controlName +"']");
		//先将所有选项置为未选中
		for ( var j = 0; j < controlsArray.length; j++) {
			var control = controlsArray[j];
			$(control).attr('checked', false);
			control.checked = false;
		}
		//解析表达式并选中
		var timeValueArray = timeValues.split(",");
		for ( var i = 0; i < timeValueArray.length; i++) {
			var timeValue = timeValueArray[i];
			for ( var j = 0; j < controlsArray.length; j++) {
				var control = controlsArray[j];
				if ($(control).val() == timeValue) {
					$(control).attr('checked', true);
				}
			}
		}
	};
	
	/**
	 * 解析秒
	 */
	this.parseSecondToUI = function(secondString) {
		if(secondString == undefined || secondString == ""){
			secondString = $currentSecondString;
		}

		if ("*" == secondString) {
			// format:*
			var beginSecond = 0;
			var endSecond = 1;
			
			$("#beginSecond").val(beginSecond);
			$("#sepSecond").val(endSecond);
			$("#secondLoop").attr("checked", true);
			changeRadioSecond();
		} else if (-1 != secondString.indexOf("/")) {
			// format:0/5
			var secondStringArray = secondString.split("/");
			
			if (2 != secondStringArray.length) {
				alert("解释[秒]字段错误");
			}
		 
			if(isNaN(secondStringArray[0]) || isNaN(secondStringArray[1]))
			{
				alert("[秒]中含有非数字字符");
			}
			var beginSecond = parseInt(secondStringArray[0]);
			var endSecond = parseInt(secondStringArray[1]);
			$("#beginSecond").val(beginSecond);
			$("#sepSecond").val(endSecond);
			$("#secondLoop").attr("checked", true);
			changeRadioSecond();
		} else {
			// format:1,2,3
			_setInputControlValues("second", secondString);
			$("#secondFix").attr("checked", true);
			changeRadioSecond();
		}
	};
	
	/**
	 * 解析分钟
	 */
	this.parseMinuteToUI = function(minuteString) {
		if(minuteString == undefined || minuteString == ""){
			minuteString = $currentMinuteString;
		}
		// 处理分
		if ("*" == minuteString) {
			// format:*
			var beginMinute = 0;
			var endMinute = 1;
			$("#beginMinute").val(beginMinute);		
			$("#sepMinute").val(endMinute);
			$("#minuteLoop").attr("checked", true);
			changeRadioMinute();
		} else if (-1 != minuteString.indexOf("/")) {
			// format:0/5
			var minuteStringArray = minuteString.split("/");
			if (2 != minuteStringArray.length) {
				alert("解释[分]字段错误");
			}
			if(isNaN(minuteStringArray[0]) || isNaN(minuteStringArray[1])){
				alert("[分]中含有非数字字符");
			}
			var beginMinute = parseInt(minuteStringArray[0]);
			var endMinute = parseInt(minuteStringArray[1]);
			$("#beginMinute").val(beginMinute);		
			$("#sepMinute").val(endMinute);
			$("#minuteLoop").attr("checked", true);
			changeRadioMinute();
		} else {
			// format:1,2,3
			_setInputControlValues("minute", minuteString);
			$("#minuteFix").attr("checked", true);
			changeRadioMinute();
		}
	};
	
	/**
	 * 解析小时
	 */
	this.parseHourToUI = function(hourString) {
		if(hourString == undefined || hourString == ""){
			hourString = $currentHourString;
		}
		// 处理小时
		if ("*" == hourString) {
			$("#hourLoop").attr("checked", true);
			changeRadioHour();
		} else if (-1 != hourString.indexOf("/")) {
			// format:0/5
			var hourStringArray = hourString.split("/");
			if (2 != hourStringArray.length) {
				alert("解释[小时]字段错误");
			}
			if(isNaN(hourStringArray[0]) || isNaN(hourStringArray[1])){
				alert("[小时]中含有非数字字符");
			}
			var beginHour = parseInt(hourStringArray[0]);
			var endHour = parseInt(hourStringArray[1]);
			hourString = "";
			for (var i = 0; i < 24; i++) {
				if (0 == (i - beginHour) % endHour) {
					hourString += i + ",";
				}
			}
			_setInputControlValues("hour", hourString);
			$("#hourFix").attr("checked", true);
			changeRadioHour();
		} else {
			// format:1,2,3
			_setInputControlValues("hour", hourString);
			$("#hourFix").attr("checked", true);
			changeRadioHour();
		}
	};
	
	/**
	 * 解析天
	 */
	this.parseDayToUI = function(dayString) {
		if(dayString == undefined || dayString == ""){
			dayString = $currentDayString;
		}
		// 处理天
		if ("*" == dayString) {
			$("#dayLoop").attr("checked", true);
			changeRadioDay();
		} else if (-1 != dayString.indexOf("/")) {
			// format:0/5
			var dayStringArray = dayString.split("/");
			if (2 != dayStringArray.length) {
				alert("解释[小时]字段错误");
			}
			var beginDay = parseInt(dayStringArray[0]);
			var endDay = parseInt(dayStringArray[1]);
			dayString = "";
			for ( var i = 1; i < 32; i++) {
				if (0 == (i - beginDay) % endDay) {
					dayString += i + ",";
				}
			}
			_setInputControlValues("day", dayString);
			$("#dayFix").attr("checked", true);
			changeRadioDay();
		} else if ("?" == dayString) {
			// 原来不处理，现在还是初始化下
			$("#dayLoop").attr("checked", true);
			_setInputControlValues("day", "");
			changeRadioDay();
		} else {
			// format:1,2,3
			_setInputControlValues("day", dayString);
			$("#dayFix").attr("checked", true);
			changeRadioDay();
		}
	};
	
	/**
	 * 解析月
	 */
	this.parseMonthToUI = function(monthString) {
		if(monthString == undefined || monthString == ""){
			monthString = $currentMonthString;
		}
		 
		// 处理月
		if ("*" == monthString) {
			$("#monthLoop").attr("checked", true);
			changeRadioMonth();
		} else if (-1 != monthString.indexOf("/")) {
			// format:0/5
			var monthStringArray = monthString.split("/");
			if (2 != monthStringArray.length) {
				alert("解释[月]字段错误");
			}
			var beginMonth = parseInt(monthStringArray[0]);
			var endMonth = parseInt(monthStringArray[1]);
			monthString = "";
			for ( var i = 1; i < 13; i++) {
				if (0 == (i - beginMonth) % endMonth) {
					monthString += i + ",";
				}
			}
			_setInputControlValues("month", monthString);
			$("#monthFix").attr("checked", true);
			changeRadioMonth();
		} else {
			// format:1,2,3
			_setInputControlValues("month", monthString);
			$("#monthFix").attr("checked", true);
			changeRadioMonth();
		}
	};
	
	/**
	 * 解析星期
	 */
	this.parseWeekToUI = function(weekString) {
		if(weekString == undefined || weekString == ""){
			weekString = $currentWeekString;
		}
		// 处理星期
		if ("?" == weekString) {
			// 原来什么都不做，现在还是初始化下
			$("#useWeek").attr("checked", false);
			_setInputControlValues("week", "");
			changeUseWeek();
			return;
		}
		$("#useWeek").attr("checked", true);
		changeUseWeek();
		if ("*" == weekString) {
			$("#weekLoop").attr("checked", true);
			changeRadioWeek();
		} else if (-1 != weekString.indexOf("/")) {
			// format:0/5
			var weekStringArray = weekString.split("/");
			if (2 != weekStringArray.length) {
				alert("解释[小时]字段错误");
			}
			var beginWeek = parseInt(weekStringArray[0]);
			var endWeek = parseInt(weekStringArray[1]);
			weekString = "";
			for ( var i = 1; i < 13; i++) {
				if (0 == (i - beginWeek) % endWeek) {
					weekString += i + ",";
				}
			}
			_setInputControlValues("week", weekString);
			$("#weekFix").attr("checked", true);
			changeRadioWeek();
		} else {
			// format:1,2,3
			_setInputControlValues("week", weekString);
			$("#weekFix").attr("checked", true);
			changeRadioWeek();
		}
	};
};