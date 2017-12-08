var prettySize = function(value) {
	var _format = function(value, unit) {
		return (value.toFixed(1) + ' ' + unit).replace('.0', '');
	};
	var K = 1024;
	var M = K * K;
	var G = M * K;
	var T = G * K;
	var dividers = [ T, G, M, K, 1 ];
	var units = [ 'TB', 'GB', 'MB', 'KB', 'B' ];
	if (value == 0) {
		return '0B';
	} else if (value < 0) {
		return 'Invalid size';
	}

	var result = '';
	var temp = 0;
	for ( var i = 0; i < dividers.length; i++) {
		var divider = dividers[i];
		if (value >= divider) {
			temp = value / divider;
			if (temp < 1.05) {
				result = _format(value,units[((i + 1) < units.length) ? (i + 1) : i]);
			} else {
				result = _format(temp, units[i]);
			}
			break;
		}
	}
	return result;
};
var fileExtClassName = function(fileName){
    var extFileName = /\.[^\.]+$/.exec(fileName);
    switch(extFileName[0]){
	    case '.xls' :    
	    	return 'nui-ico-file nui-ico-file-xls';
	    case '.xlsx' :
	    	return 'nui-ico-file nui-ico-file-xls';
	    case '.doc' :
	    	return 'nui-ico-file nui-ico-file-doc';
	    case '.docx' :
	    	return 'nui-ico-file nui-ico-file-doc';
	    case '.ppt' :
	    	return 'nui-ico-file nui-ico-file-ppt';
	    case '.pptx' :
	    	return 'nui-ico-file nui-ico-file-ppt';
	    case '.rar' :
	    	return 'nui-ico-file nui-ico-file-rar';
	    case '.html' :
	    	return 'nui-ico-file nui-ico-file-html';
	    case '.htm' :
	    	return 'nui-ico-file nui-ico-file-html';
	    case '.css' :
	    	return 'nui-ico-file nui-ico-file-css';
	    case '.xml' :
	    	return 'nui-ico-file nui-ico-file-xml';
	    case '.png' :
	    	return 'nui-ico-file nui-ico-file-png';
	    case '.js' :
	    	return 'nui-ico-file nui-ico-file-js';
	    case '.txt' :
	    	return 'nui-ico-file nui-ico-file-txt';
	    default : 
	    	return 'nui-ico-file nui-ico-file-default';
    }
};
/**
 * 从临时文件夹，迁移到指定文件夹或数据库中
 */
var postSysFileupload = function(fileuploadBusinessId,fileuploadBusinessTableName){
	var obj = $('.nui-bg-dark');
	var tmpHiddenValues = new Array();
	for(var i = 0 ; i < obj.length ; i++){
		var fileId = obj[i].id;
		var tmpHiddenValue = {
				tmpId : $(obj[i]).find('#tmpId_' + fileId).val(),
				isSaveToDatabase : $(obj[i]).find('#isSaveToDatabase_' + fileId).val()
		};
		tmpHiddenValues.push(tmpHiddenValue);
	}
	var datas = "fileuploadBusinessId=" + fileuploadBusinessId + "&fileuploadBusinessTableName=" + fileuploadBusinessTableName + "&tmpHiddenValues=" + JSON.stringify(tmpHiddenValues);
	ajaxRequest(datas);
};
function ajaxRequest(datas){
	$.ajax({
		type: "POST",
		url: getWebContentPath() + '/platform/controller/save',
		data: datas,
		success: function(msg){
			//alert( "Data Saved: " + msg );
		}
	});
}