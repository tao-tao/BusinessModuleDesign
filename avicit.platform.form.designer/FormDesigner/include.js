function encodeURIComponent(s) {
	return escape(s);
}
function decodeURIComponent(s) {
	return unescape(s);
}

if (typeof console == 'undefined') {
    console = {
			log : function(arg1, arg2, etc){}
		};
}

function helpIcon(msg) {
	return '<span class="helpIcon" qtip="'+Ext.util.Format.htmlEncode(msg)+'">&nbsp;</span>';
}
