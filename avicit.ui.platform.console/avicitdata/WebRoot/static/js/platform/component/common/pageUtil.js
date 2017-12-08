//对Date的扩展，将 Date 转化为指定格式的String 
// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符， 
// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字) 
// 例子： 
// (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423 
// (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18 
Date.prototype.Format = function(fmt) 
{ //author: meizz 
	  var o = {
		"M+" : this.getMonth() + 1, //月份 
		"d+" : this.getDate(), //日 
		"h+" : this.getHours(), //小时 
		"m+" : this.getMinutes(), //分 
		"s+" : this.getSeconds(), //秒 
		"q+" : Math.floor((this.getMonth() + 3) / 3), //季度 
		"S" : this.getMilliseconds()
	//毫秒 
	};
	if (/(y+)/.test(fmt))
		fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(fmt))
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k])
					: (("00" + o[k]).substr(("" + o[k]).length)));
	return fmt; 
};

/**
 * 将表单对象转为json对象
 * @param formValues
 * @returns 
 */
function convertToJson(formValues) {
		var result = {};
			for ( var formValue, j = 0; j < formValues.length; j++) {
				formValue = formValues[j];
				var name = formValue.name;
				var value = formValue.value;
				if (name.indexOf('.') < 0) {
					result[name] = value;
					continue;
				} else {
					var simpleNames = name.split('.');
					// 构建命名空间
					var obj = result;
					for ( var i = 0; i < simpleNames.length - 1; i++) {
						var simpleName = simpleNames[i];
						if (simpleName.indexOf('[') < 0) {
							if (obj[simpleName] == null) {
								obj[simpleName] = {};
							}
							obj = obj[simpleName];
						} else { // 数组
							// 分隔
							var arrNames = simpleName.split('[');
							var arrName = arrNames[0];
							var arrIndex = parseInt(arrNames[1]);
							if (obj[arrName] == null) {
								obj[arrName] = []; // new Array();
							}
							obj = obj[arrName];
							multiChooseArray = result[arrName];
							if (obj[arrIndex] == null) {
								obj[arrIndex] = {}; // new Object();
							}
							obj = obj[arrIndex];
						}
					}

					if (obj[simpleNames[simpleNames.length - 1]]) {
						var temp = obj[simpleNames[simpleNames.length - 1]];
						obj[simpleNames[simpleNames.length - 1]] = temp;
					} else {
						obj[simpleNames[simpleNames.length - 1]] = value;
					}

				}
			}
			return result;
 };