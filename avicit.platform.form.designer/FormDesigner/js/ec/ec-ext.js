function alertError(msg) {
	Ext.Msg.show({title : '错误', msg : msg, icon : Ext.Msg.ERROR, buttons : Ext.Msg.OK});
}

if ((typeof Range !== "undefined") && !Range.prototype.createContextualFragment) {  
          Range.prototype.createContextualFragment = function (html) {  
          var frag = document.createDocumentFragment(),  
          div = document.createElement("div");  
          frag.appendChild(div);  
          div.outerHTML = html;  
          return frag;  
      };  
  }  

String.prototype.len = function() {
	//return this.replace(/[^x00-xff]/g,"aa").length;
		var len = 0;
		for (var i=0; i<this.length; i++) {
			if (this.charCodeAt(i)>127 || this.charCodeAt(i)==94) {
				len += 2;
			} else {
				len ++;
			}
		}
		return len;
};

String.prototype.existsGB = function() {
	for (var i=0; i<this.length; i++) {
		if (this.charCodeAt(i)>127 || this.charCodeAt(i)==94) {
			return true;
		}
	}

	return false;
}

String.prototype.toNumber = function() {
	
	var ret = this.replace(/[^0-9\.]/g, "");
	var firstIndex = ret.indexOf(".");
	
	if (firstIndex != -1) {
		var str1 = ret.substring(0, firstIndex);
		var str2 = ret.substring(firstIndex);
		str2 = str2.replace(/\./g, "");
		ret = str1 + "." + str2;
	}

	return ret;
}

/**
 * 日期格式转换
 * @param {} mask
 * @return {}
 */
Date.prototype.format = function(mask) {

    var d = this;
    var zeroize = function (value, length) {
        if (!length) length = 2;
        value = String(value);
        for (var i = 0, zeros = ''; i < (length - value.length); i++) {
            zeros += '0';
        }
        return zeros + value;
    };
    return mask.replace(/"[^"]*"|'[^']*'|\b(?:d{1,4}|m{1,4}|yy(?:yy)?|([hHMstT])\1?|[lLZ])\b/g, function($0) {
        switch($0) {
            case 'd':   return d.getDate();
            case 'dd': return zeroize(d.getDate());
            case 'ddd': return ['Sun','Mon','Tue','Wed','Thr','Fri','Sat'][d.getDay()];
            case 'dddd':    return ['Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday'][d.getDay()];
            case 'M':   return d.getMonth() + 1;
            case 'MM': return zeroize(d.getMonth() + 1);
            case 'MMM': return ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'][d.getMonth()];
            case 'MMMM':    return ['January','February','March','April','May','June','July','August','September','October','November','December'][d.getMonth()];
            case 'yy': return String(d.getFullYear()).substr(2);
            case 'yyyy':    return d.getFullYear();
            case 'h':   return d.getHours() % 12 || 12;
            case 'hh': return zeroize(d.getHours() % 12 || 12);
            case 'H':   return d.getHours();
            case 'HH': return zeroize(d.getHours());
            case 'm':   return d.getMinutes();
            case 'mm': return zeroize(d.getMinutes());
            case 's':   return d.getSeconds();
            case 'ss': return zeroize(d.getSeconds());
            case 'l':   return zeroize(d.getMilliseconds(), 3);
            case 'L':   var m = d.getMilliseconds();
                    if (m > 99) m = Math.round(m / 10);
                    return zeroize(m);
            case 'tt': return d.getHours() < 12 ? 'am' : 'pm';
            case 'TT': return d.getHours() < 12 ? 'AM' : 'PM';
            case 'Z':   return d.toUTCString().match(/[A-Z]+$/);
            // Return quoted strings with the surrounding quotes removed
            default:    return $0.substr(1, $0.length - 2);
        }
    });
};

Ext.QuickTips.init();
var EcCurrComp;
var root = root || '..';
Ext.BLANK_IMAGE_URL = root+'/js/ext/resources/images/default/s.gif';
Ext.form.Field.prototype.msgTarget = 'side';

Ec = { version : '1.1.0' };

Ext.apply(Ext.form.VTypes, {
	nowTimeV : function(val, datefield){
		if(datefield.compareTo){
			var other = Ext.get(datefield.compareTo).getValue();
		}
	},
	dateGT : function(val, datefield) {
		if (datefield.compareTo) {
			var other = Ext.get(datefield.compareTo).getValue();
			if (other) {
				if (val >= Ext.get(datefield.compareTo).getValue()) {
					Ext.getCmp(datefield.compareTo).clearInvalid();
					return true;
				} else {
					return false;
				}
			}
		}
		Ext.getCmp(datefield.compareTo).clearInvalid();
		return true;
	},
	dateLT : function(val, datefield) {
		if (datefield.compareTo) {
			var other = Ext.get(datefield.compareTo).getValue();
			if (other) {
				if (val <= Ext.get(datefield.compareTo).getValue()) {
					Ext.getCmp(datefield.compareTo).clearInvalid();
					return true;
				} else {
					return false;
				}
			}
		}
		Ext.getCmp(datefield.compareTo).clearInvalid();
		return true;
	}
});

Ec.Utils = function(){
	var msgCt;
	var tipmode = false;

	function createBox(t, s) {
		return [
				'<div class="msg">',
				'<div class="x-box-tl"><div class="x-box-tr"><div class="x-box-tc"></div></div></div>',
				'<div class="x-box-ml"><div class="x-box-mr"><div class="x-box-mc"><h3>',
				t,
				'</h3>',
				s,
				'</div></div></div>',
				'<div class="x-box-bl"><div class="x-box-br"><div class="x-box-bc"></div></div></div>',
				'</div>'].join('');
	}
	return {
		getItem : function(c) {
			if (c.comp) {
				return c.comp.items.items[0];
			}
		},
		msg : function(title, format, fn) {
			if(tipmode)
			{
				if (!msgCt) {
					msgCt = Ext.DomHelper.insertFirst(document.body, {
						id : 'ec-tip-msg'
					}, true);
				}
				msgCt.alignTo(document, 'c-c');
				var s = String.format.apply(String, Array.prototype.slice.call(
						arguments, 1));
				var m = Ext.DomHelper.append(msgCt, {
					html : createBox(title, s)
				}, true);
				m.slideIn('t').pause(2).ghost("t", {
					remove : true
				});
			}
			else
				Ext.MessageBox.alert(title,format, fn);
			},
		// 查找功能
		// fieldName是查找的属性值
		// c是查找的目标容器
		// propName是fieldName对应组件中的属性值
		find : function(fieldName, c, propName) {

			if(typeof c == 'string')
			{
				c = Ec.FormManager.get(c);
			}

			if(!c)
				return [];

			var name = propName || 'name';
			var curForm = c.win ? c.comp : Ec.Utils.getItem(c);
			// 如果要查找的就是容器本身
			if(!curForm)
				curForm = c;
			if(curForm[name] == fieldName)
				return [curForm];

			var fields = Ec.Utils.iterateFind(curForm, name, fieldName);
			return fields;
		},
		// 递归查找
		// c是查找的容器
		// propName是查找的属性名称
		// fieldName是查找的属性值
		iterateFind : function(c, propName, fieldName) {
			var name = propName;
		    var fields = [];
		    // 先查找items
		    if (c.items && c.items.items) {
		        var cs = c.items.items;
		        for(var i = 0; i < cs.length; i++){
		            if(cs[i][name] == fieldName) {
		                fields.push(cs[i]);
		                // return fields;
		            }
		           fields = fields.concat(Ec.Utils.iterateFind(cs[i], propName, fieldName));
		        }
		    }
		    // 查找buttons
		    if (fields.length == 0 && c.buttons) {
		        for (var i = 0;i < c.buttons.length; i++) {
		            if (c.buttons[i][name] == fieldName) {
		                fields.push(c.buttons[i]);
		                // return fields;
		            }
		            fields = fields.concat(Ec.Utils.iterateFind(c.buttons[i], propName, fieldName));
		        }
		    }
		    // 查找tbar
		    if(fields.length == 0 && c.topToolbar && c.topToolbar.items){
		        var cs = c.topToolbar.items.items;
		        for(var i = 0; i < cs.length; i++){
		            if(cs[i][name] == fieldName) {
		                fields.push(cs[i]);
		                // return fields;
		            }
		            fields = fields.concat(Ec.Utils.iterateFind(cs[i], propName, fieldName));
		        }
		    }


		    // 查找bbar
		    if(fields.length == 0  && c.bottomToolbar && c.bottomToolbar.items){
		        var cs = c.bottomToolbar.items.items;
		        for(var i = 0; i < cs.length; i++){
		            if(cs[i][name] == fieldName) {
		                fields.push(cs[i]);
		                // return fields;
		            }
		            fields = fields.concat(Ec.Utils.iterateFind(cs[i], propName, fieldName));
		       }
		    }
		    // 查找自定义的searchBar
		    if(fields.length == 0 && c.searchBar && c.searchBar.form) {
		    	if(c.searchBar.form[name] == fieldName)
		    		fields.push(c.searchBar.form);

		    	fields = fields.concat(Ec.Utils.iterateFind(c.searchBar.form, propName, fieldName));
		    }

		    // 如果是tab页中的grid的tbar，按钮属性是直接放在c.topToolbar中
		    if(fields.length == 0 && c.topToolbar && c.topToolbar.length > 0){
		        var cs = c.topToolbar;
		        for(var i = 0; i < cs.length; i++){
		        	if (fieldName == 'detailBtn' && c.name == 'sysparamHistoryList') {
		        		 if(cs[i][name] == fieldName) {
			                fields.push(cs[i]);
			                // return fields;
			            }
		   			}

		            fields = fields.concat(Ec.Utils.iterateFind(cs[i], propName, fieldName));
		        }
		    }

		    return fields;
		},
		findNotEmpty : function(c, propName) {
			if(!propName)
				return;
			if(typeof c == 'string')
			{
				c = Ec.FormManager.get(c);
			}

			if(!c)
				return [];

			var curForm = c.win ? c.comp : Ec.Utils.getItem(c);
			// 如果要查找的就是容器本身
			if(!curForm)
				curForm = c;
			if(curForm[propName])
				return [curForm];

			var fields = Ec.Utils.iterateFindNotEmpty(curForm, propName);
			return fields;
		},
		iterateFindNotEmpty : function(c, propName) {
			var name = propName;
		    var fields = [];
		    // 先查找items
		    if (c.items && c.items.items) {
		        var cs = c.items.items;
		        for(var i = 0; i < cs.length; i++){
		            if(cs[i][name]) {
		                fields.push(cs[i]);
		                // return fields;
		            }
		           fields = fields.concat(Ec.Utils.iterateFindNotEmpty(cs[i], propName));
		        }
		    }
		    // 查找buttons
		    if (fields.length == 0 && c.buttons) {
		        for (var i = 0;i < c.buttons.length; i++) {
		            if (c.buttons[i][name]) {
		                fields.push(c.buttons[i]);
		                // return fields;
		            }
		            fields = fields.concat(Ec.Utils.iterateFindNotEmpty(c.buttons[i], propName));
		        }
		    }
		    // 查找tbar
		    if(fields.length == 0 && c.topToolbar && c.topToolbar.items){
		        var cs = c.topToolbar.items.items;
		        for(var i = 0; i < cs.length; i++){
		            if(cs[i][name]) {
		                fields.push(cs[i]);
		                // return fields;
		            }
		            fields = fields.concat(Ec.Utils.iterateFindNotEmpty(cs[i], propName));
		        }
		    }
		    // 查找bbar
		    if(fields.length == 0  && c.bottomToolbar && c.bottomToolbar.items){
		        var cs = c.bottomToolbar.items.items;
		        for(var i = 0; i < cs.length; i++){
		            if(cs[i][name]) {
		                fields.push(cs[i]);
		                // return fields;
		            }
		            fields = fields.concat(Ec.Utils.iterateFindNotEmpty(cs[i], propName));
		       }
		    }
		    // 查找自定义的searchBar
		    if(fields.length == 0 && c.searchBar && c.searchBar.form) {
		    	if(c.searchBar.form[name])
		    		fields.push(c.searchBar.form);

		    	fields = fields.concat(Ec.Utils.iterateFindNotEmpty(c.searchBar.form, propName));
		    }
		    return fields;
		},
		op : function(fieldName, compname, op, params) {

			if(fieldName.constructor == Array)
			{
				for(var i=0;i<fieldName.length;i++)
					Ec.Utils.op(fieldName[i],compname,op,params);
			}
			else
			{
				var c = compname;
				if(typeof c == 'string')
					c = Ec.FormManager.get(compname);
				if(!c)
					c = EcCurrComp;
				if(c)
				{
					var fields = Ec.Utils.find(fieldName, c);
					for(var i = 0, len = fields.length; i < len; i++){
						if(typeof op =='function')
						{
							op.call(fields[i],params);
						}
						else if(typeof op == 'string')
						{
			                if(typeof fields[i][op] == 'function') {
			                	if (op == 'hide' && fields[i].needHideLabel) {
			                		var el = fields[i].getEl();
			                		var parent = el.parent();
			                		while(!parent.hasClass("x-form-item")) {
			                			parent = parent.parent();
			                		}
			                		parent.dom.style.display = 'none';
			                	}

			                	if (op == 'show' && fields[i].needHideLabel) {
			                		var el = fields[i].getEl();
			                		var parent = el.parent();
			                		while(!parent.hasClass("x-form-item")) {
			                			parent = parent.parent();
			                		}
			                		//el.parent().parent().dom.style.display = 'undefined';
			                		if (parent.dom.style.display) {
			                			parent.dom.style.display = 'undefined';
			                		}
			                	}
			                	
			                	fields[i][op](params);
			                }
						}
		            }
		            if(c.win)
		            	c.comp.syncShadow();
				}
			}
		},
		getValueString : function(field,param) {
			if(field.getSubmitValue)
				return field.getSubmitValue(param);
			if(field.isFormField)
				return field.getRawValue(param);
			else
				return field.getValue(param);
		},
 iterateParam : function (params, items) {
	if (params && items && items.items/*&& Ext.type(items) == 'nodelist'*/) {
		//items.each(function(f) {
		Ext.each(items.items, function(f, i, allItems) {
			var id = f.name || f.id;
			var p = params;
			var index = id.indexOf('.');
			if( index > 0 )
			{
				var sub = id.substr(0,index);
				id = id.substr(index+1);
				p = params[sub];
				if(!p)
				{
					params[sub] = {};
					p = params[sub];
				}
			}
			if (f.inputType == 'radio' && !f.checked) {
				return;
			}

			if (f.inputType == 'checkbox' && !f.checked) {
				return;
			}

			if (f.inputType == 'checkbox' && f.checked) {
				var v = p[id];

				value = Ec.Utils.getValueString(f);
				if (!v) {
					v =  value;
				} else {
					if (v.length > 0) {
						v += ',';
					}
					v += value;
				}
				p[id] = v;
			}

	        value = Ec.Utils.getValueString(f);
	        if (value) {
	        	p[id] = value;
	        }

	        Ec.Utils.iterateParam(params, f.items);
		});
	}
},
    setValues : function(form, values, prefix){
        if(Ext.isArray(values)){ // array of objects
            for(var i = 0, len = values.length; i < len; i++){
                var v = values[i];
                var fid = v.id;
            	if(prefix)
            		fid = prefix + '.' + id;
                var f = form.findField(fid);
                if(f){
                    f.setValue(v.value);
                    if(form.trackResetOnLoad){
                        f.originalValue = f.getValue();
                    }
                }
            }
        }else{ // object hash
            var field, id;
            for(id in values){
                if(typeof values[id] != 'function')
                {
                	var fid = id;
                	if(prefix)
                		fid = prefix + '.' + id;
                	if (field = form.findField(fid)){
	                    field.setValue(values[id]);
	                    if(form.trackResetOnLoad){
	                        field.originalValue = field.getValue();
	                    }
	                }
	                if(typeof values[id] == 'object')
	                {
	                	Ec.Utils.setValues(form, values[id], fid);
                	}
                }
            }
        }
        return form;
    },

		json2xml : function(o, tab) {
		   var toXml = function(v, name, ind) {
		      var xml = "";
		      if (v instanceof Array) {
		         for (var i=0, n=v.length; i<n; i++)
		            xml += ind + toXml(v[i], name, ind+"\t") + "\n";
		      }
		      else if (typeof(v) == "object") {
		         var hasChild = false;
		         xml += ind + "<" + name;
		         for (var m in v) {
		            if (m.charAt(0) == "@")
		               xml += " " + m.substr(1) + "=\"" + v[m].toString() + "\"";
		            else
		               hasChild = true;
		         }
		         xml += hasChild ? ">" : "/>";
		         if (hasChild) {
		            for (var m in v) {
		               if (m == "#text")
		                  xml += v[m];
		               else if (m == "#cdata")
		                  xml += "<![CDATA[" + v[m] + "]]>";
		               else if (m.charAt(0) != "@")
		                  xml += toXml(v[m], m, ind+"\t");
		            }
		            xml += (xml.charAt(xml.length-1)=="\n"?ind:"") + "</" + name + ">";
		         }
		      }
		      else {
		         xml += ind + "<" + name + ">" + v.toString() +  "</" + name + ">";
		      }
		      return xml;
		   }, xml="";
		   for (var m in o)
		      xml += toXml(o[m], m, "");
		   return tab ? xml.replace(/\t/g, tab) : xml.replace(/\t|\n/g, "");
		},
		xml2json: function(xml, tab) {
		   var X = {
		      toObj: function(xml) {
		         var o = {};
		         if (xml.nodeType==1) {   // element node ..
		            if (xml.attributes.length)   // element with attributes  ..
		               for (var i=0; i<xml.attributes.length; i++)
		                  o["@"+xml.attributes[i].nodeName] = (xml.attributes[i].nodeValue||"").toString();
		            if (xml.firstChild) { // element has child nodes ..
		               var textChild=0, cdataChild=0, hasElementChild=false;
		               for (var n=xml.firstChild; n; n=n.nextSibling) {
		                  if (n.nodeType==1) hasElementChild = true;
		                  else if (n.nodeType==3 && n.nodeValue.match(/[^ \f\n\r\t\v]/)) textChild++; // non-whitespace text
		                  else if (n.nodeType==4) cdataChild++; // cdata section node
		               }
		               if (hasElementChild) {
		                  if (textChild < 2 && cdataChild < 2) { // structured element with evtl. a single text or/and cdata node ..
		                     X.removeWhite(xml);
		                     for (var n=xml.firstChild; n; n=n.nextSibling) {
		                        if (n.nodeType == 3)  // text node
		                           o["#text"] = X.escape(n.nodeValue);
		                        else if (n.nodeType == 4)  // cdata node
		                           o["#cdata"] = X.escape(n.nodeValue);
		                        else if (o[n.nodeName]) {  // multiple occurence of element ..
		                           if (o[n.nodeName] instanceof Array)
		                              o[n.nodeName][o[n.nodeName].length] = X.toObj(n);
		                           else
		                              o[n.nodeName] = [o[n.nodeName], X.toObj(n)];
		                        }
		                        else  // first occurence of element..
		                           o[n.nodeName] = X.toObj(n);
		                     }
		                  }
		                  else { // mixed content
		                     if (!xml.attributes.length)
		                        o = X.escape(X.innerXml(xml));
		                     else
		                        o["#text"] = X.escape(X.innerXml(xml));
		                  }
		               }
		               else if (textChild) { // pure text
		                  if (!xml.attributes.length)
		                     o = X.escape(X.innerXml(xml));
		                  else
		                     o["#text"] = X.escape(X.innerXml(xml));
		               }
		               else if (cdataChild) { // cdata
		                  if (cdataChild > 1)
		                     o = X.escape(X.innerXml(xml));
		                  else
		                     for (var n=xml.firstChild; n; n=n.nextSibling)
		                        o["#cdata"] = X.escape(n.nodeValue);
		               }
		            }
		            if (!xml.attributes.length && !xml.firstChild) o = null;
		         }
		         else if (xml.nodeType==9) { // document.node
		            o = X.toObj(xml.documentElement);
		         }
		         else
		            alert("unhandled node type: " + xml.nodeType);
		         return o;
		      },
		      toJson: function(o, name, ind) {
		         var json = name ? ("\""+name+"\"") : "";
		         if (o instanceof Array) {
		            for (var i=0,n=o.length; i<n; i++)
		               o[i] = X.toJson(o[i], "", ind+"\t");
		            json += (name?":[":"[") + (o.length > 1 ? ("\n"+ind+"\t"+o.join(",\n"+ind+"\t")+"\n"+ind) : o.join("")) + "]";
		         }
		         else if (o == null)
		            json += (name&&":") + "null";
		         else if (typeof(o) == "object") {
		            var arr = [];
		            for (var m in o)
		               arr[arr.length] = X.toJson(o[m], m, ind+"\t");
		            json += (name?":{":"{") + (arr.length > 1 ? ("\n"+ind+"\t"+arr.join(",\n"+ind+"\t")+"\n"+ind) : arr.join("")) + "}";
		         }
		         else if (typeof(o) == "string")
		            json += (name&&":") + "\"" + o.toString() + "\"";
		         else
		            json += (name&&":") + o.toString();
		         return json;
		      },
		      innerXml: function(node) {
		         var s = ""
		         if ("innerHTML" in node)
		            s = node.innerHTML;
		         else {
		            var asXml = function(n) {
		               var s = "";
		               if (n.nodeType == 1) {
		                  s += "<" + n.nodeName;
		                  for (var i=0; i<n.attributes.length;i++)
		                     s += " " + n.attributes[i].nodeName + "=\"" + (n.attributes[i].nodeValue||"").toString() + "\"";
		                  if (n.firstChild) {
		                     s += ">";
		                     for (var c=n.firstChild; c; c=c.nextSibling)
		                        s += asXml(c);
		                     s += "</"+n.nodeName+">";
		                  }
		                  else
		                     s += "/>";
		               }
		               else if (n.nodeType == 3)
		                  s += n.nodeValue;
		               else if (n.nodeType == 4)
		                  s += "<![CDATA[" + n.nodeValue + "]]>";
		               return s;
		            };
		            for (var c=node.firstChild; c; c=c.nextSibling)
		               s += asXml(c);
		         }
		         return s;
		      },
		      escape: function(txt) {
		         return txt.replace(/[\\]/g, "\\\\")
		                   .replace(/[\"]/g, '\\"')
		                   .replace(/[\n]/g, '\\n')
		                   .replace(/[\r]/g, '\\r');
		      },
		      removeWhite: function(e) {
		         e.normalize();
		         for (var n = e.firstChild; n; ) {
		            if (n.nodeType == 3) {  // text node
		               if (!n.nodeValue.match(/[^ \f\n\r\t\v]/)) { // pure whitespace text node
		                  var nxt = n.nextSibling;
		                  e.removeChild(n);
		                  n = nxt;
		               }
		               else
		                  n = n.nextSibling;
		            }
		            else if (n.nodeType == 1) {  // element node
		               X.removeWhite(n);
		               n = n.nextSibling;
		            }
		            else                      // any other node
		               n = n.nextSibling;
		         }
		         return e;
		      }
		   };
		   xml = this.loadXML(xml);
		   if (xml.nodeType == 9) // document node
		      xml = xml.documentElement;
		   var json = X.toJson(X.toObj(X.removeWhite(xml)), xml.nodeName, "\t");
		   return "{\n" + tab + (tab ? json.replace(/\t/g, tab) : json.replace(/\t|\n/g, "")) + "\n}";
		},
		loadXML: function(xml)
		{
		    var xmlDoc;
		    if(window.ActiveXObject)
		    {
		        xmlDoc    = new ActiveXObject('Microsoft.XMLDOM');
		        xmlDoc.async    = false;
		        xmlDoc.loadXML(xml);
		    }
		    else if (document.implementation&&document.implementation.createDocument)
		    {
		        //xmlDoc    = document.implementation.createDocument('', '', null);
		        //xmlDoc.loadXML(xml);
		    	var parser = new DOMParser();
		    	xmlDoc = parser.parseFromString(xml, "text/xml");
		    }
		    else
		    {
		        return null;
		    }

		    return xmlDoc;
		}

	}
}();

Ec.Request = function() {
	var requestHandler = function(response, o) {
		var c = this;
		clearWaitMsg(c,'');
		var doc = response.responseXML;

		var txt = response.responseText;
		if (txt === 'noright!') {
			window.location.href=root + '/noright.html';
			return;
		}
		if (txt && txt.indexOf("nologin")!=-1) {
			window.top.location.href = root + '/login.html';
			return;
		}

		if(doc == undefined && o && o.response)
		{
			doc = o.response.responseXML;
		}
		if(!doc && c && c.comp && c.comp.el)
		{
			c.comp.el.dom.innerHTML = response.responseText;
			return;
		}
		if(doc == undefined)
			return;
		var result = doc.documentElement || doc;
		var code = Ext.DomQuery.selectValue('/@code', result, '');
		if (code == '202') {
			window.top.location.href = root + '/login.jsp';
			return;
		}
		if (code != '200') {
			showErrorMsg(code, result);
			return;
		}

		var type = Ext.DomQuery.selectValue('/@type', result, '');
		var uri = Ext.DomQuery.selectValue('/@uri', result, '');
		var reshandler = Ec.HandlerManager.getReponseHandler(type);
		if(reshandler)
			reshandler.handler(c, result, uri);
		else
			alert('No proper handler.');
			
	};
	var requestFailure = function(response, o) {
		clearWaitMsg(this);
		Ec.Utils.msg(null, getMsg(response, this), response.responseText);
	};
	var getMsg = function(response, c) {
		if (typeof response == 'undefined') {
			return "server";
		} else {
			if (response.status == '404') {
				return 'Resource not exist.[' + response.statusText + ']';
			} else if (response.status == '0') {
				if(c && c.c)
				{
					var updater = Ec.Utils.findNotEmpty(c.c, 'updatetimer');
					for(var i=0; i< updater.length; i++)
					{
						Ext.TaskMgr.stop(updater[i].updatetimer);
					}
					return "网络中断";
				}
				return '网络中断.';
			} else
				return response.statusText + '[' + response.status + ']';
		}
	};
	var showErrorMsg = function(code, result) {
		var msg = Ext.DomQuery.selectValue('/@message', result, '');
		Ec.Utils.msg(code, msg);
	};
	var waitMsg = function(c, msg) {
		var item;

		if(c.c)
			item = Ec.Utils.getItem(c.c);
		else
			item = Ec.Utils.getItem(c);
		if (msg && item) {
			var t = item.waitMsgTarget;
			if (t === true) {
				item.el.mask(msg, 'x-mask-loading');
			} else if (t) {
				t = Ext.get(t);
				t.mask(msg, 'x-mask-loading');
			} else {
				item.msgBox = Ext.MessageBox.wait(msg, item.waitTitle || 'Please Wait...');
			}
		}
	};
	var clearWaitMsg = function(c, msg) {
		//var item = Ec.Utils.getItem(c);
		var item;

		if(c.c)
			item = Ec.Utils.getItem(c.c);
		else
			item = Ec.Utils.getItem(c);

		if (item) {
			var t = item.waitMsgTarget;
			if (t === true && item.el) {
				item.el.unmask();
			} else if (t) {
				t.unmask();
			} else {
				if (item.msgBox) {
					//Ext.MessageBox.updateProgress(1);
					//Ext.MessageBox.hide();
					item.msgBox.updateProgress(1);
					item.msgBox.hide();
					delete item.msgBox;
				}
			}
		}
	};
	var request = function(r, c, msg) {
		waitMsg(c, msg);
		//
		r.success = c.requestHandler || requestHandler;
		r.failure = c.requestFailure || requestFailure;
		r.scope = c;
		if(!root)
		{
			alert('root is null.');
			return;
		}
		if(r.url.indexOf(root) < 0 && r.url.charAt(0)=='/')
		{
			r.url = root + r.url;
		}
		if(r.form)
		{
			// r.url = r.url + '?' + Ext.encode(r.params);
			var pStr = '';
			
			for (var fieldName in r.params) {
				pStr = pStr + '&' + fieldName + '=' + r.params[fieldName];
			}
			r.url = r.url + '?' + pStr;
			r.isUpload = true;
			r.form.form.submit(r);
		}
		else
		{
			var temp = Ext.lib.Ajax.defaultPostHeader;
			//Ext.lib.Ajax.defaultPostHeader += '; charset=GBK';
			Ext.Ajax.request(r);
			Ext.lib.Ajax.defaultPostHeader = temp;
		}
	};
	var show = function(c, url, waitMsg) {
		var r = {
			url : url
		};
		r.params = c.conf?c.conf.params||{}:{};
		r.params._comp = c.renderTo;
		r.params._type = Ec.Request.SHOW;

		request(r, c, waitMsg);
	};
	return {
		SHOW : 'show',
		HANDLE : 'handle',
		DATA_EVENT : 'data_event',
		FIELD_EVENT : 'field_event',
		NULL : 'null',
		DataEventParams : function(event, field, params) {
			if(params)
			{
				var pStr = '';
				for (var fieldName in params) {
					pStr = pStr + '&' + fieldName + '=' + params[fieldName];
				}
				return '_type=' + Ec.Request.DATA_EVENT + '&_event=' + event + '&_field=' + field + pStr;
			}
			else
				return '_type=' + Ec.Request.DATA_EVENT + '&_event=' + event + '&_field=' + field;
		},
		panel : function(renderTo, url, conf, waitMsg) {
			var c = {
				conf : conf,
				win : false,
				renderTo : renderTo
			};
			show(c, url, waitMsg);
		},
		window : function(renderTo, url, conf, waitMsg) {

			var c = {
				conf : conf,
				win : true,
				renderTo : renderTo
			};
			show(c, url, waitMsg);
		},
		render : function(renderTo, url, data, other) {
			var c = {
				conf: data,
				win : false,
				renderTo : renderTo
			};
			var doc = Ec.Utils.loadXML(other);
			var result = doc.documentElement || doc;
			var code = Ext.DomQuery.selectValue('/@code', result, '');
			if (code == '202') {
				window.top.location.href = root + '/login.jsp';
				return;
			}
			if (code != '200') {
				showErrorMsg(code, result);
				return;
			}

			var type = Ext.DomQuery.selectValue('/@type', result, '');
			var uri = Ext.DomQuery.selectValue('/@uri', result, '');
			var reshandler = Ec.HandlerManager.getReponseHandler(type);
			if(reshandler)
				reshandler.handler(c, result, uri);;
		},
		close : function(renderTo, confirmMsg) {
			Ec.FormManager.close(renderTo);
		},
		fireFieldEvent : function(c, params, component, form) {
			var target = c.c;
			if (!params)
				params = {};
			if(target.basicParams)
				Ext.applyIf(params, target.basicParams);

			for (var i = 0;i < c.ref.length; i++) {
				var refField = c.ref[i];
				var refParam;
				if(refField.indexOf(':')>0)
				{
					refParam = refField.substr(refField.indexOf(':')+1);
					refField = refField.substr(0,refField.indexOf(':'));
				}

				var fields = Ec.Utils.find(refField,target);
				if(fields.length>0)
				{
					for (var j = 0;j < fields.length; j++) {
						params[refField] = Ec.Utils.getValueString(fields[j],refParam);// .getValue(refParam);
					}
				}
				else if(refField != '')
				{
					var comp = Ext.getCmp(refField);
					if(comp && comp.getValue)
						params[refField] = comp.getValue(refParam);
					else
					{
						var field = Ext.get(refField);
						if(field)
						{
							if(field.dom.type!='checkbox' || field.dom.checked)
							    params[refField] = field.getValue();
						}
					}
				}
			}
			var r = {
				url : target.uri
			};
			r.params = params;
			if(r.params._type == Ec.Request.SHOW)
				r.params._type = Ec.Request.FIELD_EVENT;
			else
				r.params._type = params._type || Ec.Request.FIELD_EVENT;
			r.params._event = c.event;
			r.params._field = c.field;
			r.params._comp = target.renderTo;
			r.timeout = component?(component.timeout?component.timeout:30000):30000;
			if(form) {
				r.form = form;
			}
			request(r, c, (component?component.waitMsg:undefined));
		},
	    load : function(src) {
	        var headerDom = document.getElementsByTagName('head').item(0);
	        var jsDom = document.createElement('script');
	        jsDom.type = 'text/javascript';
	        jsDom.scr = src;
	        headerDom.appendChild(jsDom);
	    },
	    ajaxLoad : function(src) {
	        Ext.Ajax.request({
	            method:'GET',
	            url: src,
	            scope: this,
	            success: function(response){
	                var module = eval(response.responseText);
	            }
	        });
	    }
	};
}();

Ec.Response = function() {
	var fieldValid = function(tip, comp) {
		var fields = Ec.Utils.find(tip.field,comp);
		for (var i = 0;i < fields.length; i++) {
			var field = fields[i];
			if (tip.message == '') {
				field.suspendEvents();
				field.clearInvalid();
				field.resumeEvents();
			} else {
				field.markInvalid(tip.message);
			}
		}
	};
	return {
		initData : function(node, c) {
			this.initEvent(node, c);
			var obj = {};
			var json = Ext.DomQuery.select('/d', node);
			if (json.length != 0) {
				for(var i=0;i<json.length;i++)
				{
					var type = Ext.DomQuery.selectValue('/@type', json[i]);
					var jsonText = json[i].text || json[i].textContent;
					var isXml = false;
					if(type == 'xml')
					{
						isXml = true;
						//jsonText = Ec.Utils.xml2json(jsonText,'');
					}
					obj = Ext.util.JSON.decode(jsonText);
					var newc = c;
					var isNewTarget = false;
					var target = Ext.DomQuery.selectValue('/@target', json[i]);
					if(target && target != 'self')
					{
						newc = Ec.FormManager.get(target);
						isNewTarget = true;
					}
					if(!newc)
						newc = c;
					this.initScriptBefore(node, newc);
					this.initLocalData(newc, obj, isNewTarget, isXml);
				}
			}
			else if(c.conf && c.conf.initData && !c.conf.initData.loaded)
			{
				
				this.initScriptBefore(node, c);
				if(c.conf.initData.data)
					this.initLocalData(c,c.conf.initData.data);
				else
					this.initLocalData(c,c.conf.initData);
				c.conf.initData.loaded = true;
			}
			this.initScript(node, c);
			this.initVis(node, c);
			this.initButtons(node, c);

		},
		initLocalData : function(newc, obj, target, isXml) {
			if(!obj)
			   return;

			for (var fieldName in obj) {
				if (fieldName == '_valid') {
					var list = obj[fieldName];
					for (var i = 0;i < list.length; i++) {
						fieldValid(list[i], newc.comp);
					}
					continue;
				}else if (fieldName == '_message') {
					var list = obj[fieldName];
					var txt = [];
					var ops = [];
					for (var i = 0;i < list.length; i++) {
						if(list[i].title == 'script')
							ops.push(list[i].message);
						else
							txt.push('<h3>'+ list[i].title + '</h3>' + list[i].message);
					}
					if(ops.length>0 && txt.length>0)
					{
						Ec.Utils.msg('提示消息', txt.join('<br>'),function(){eval(ops.join(';'));});
					}
					else if(txt.length >0)
					{
						Ec.Utils.msg('提示消息', txt.join('<br>'));
					}
					else if(ops.length >0)
					{
						eval(ops.join(';'));
					}
					continue;
				}else{
					var fields = Ec.Utils.find(fieldName, newc);
					if(fields.length > 0)
					{
						for (var i = 0;i < fields.length; i++) {
							var field = fields[i];
							// field.suspendEvents();
							if(field.setURI)
								field.setURI(newc.uri,newc.basicParams);
							else
								field._uri = newc.uri;
							var fieldv = obj[fieldName];
							if(isXml && fieldv.substr(0,3) == '<v>')
							{
								fieldv = Ec.Utils.xml2json(fieldv,'');
								fieldv = Ext.util.JSON.decode(fieldv);
								fieldv = fieldv.v;
							}
							var old_disabled = field.disabled;
							field.disabled = true;
							if(field.type == 'jsontext')
								field.setValue(Ext.util.JSON.encode(fieldv));
							else if(field.setValue)
								field.setValue(fieldv);
							else if(field.getForm)
								Ec.Utils.setValues(field.getForm(),fieldv);
							else if(field.setText)
								field.setText(fieldv, false);
							// field.resumeEvents();
							field.disabled = old_disabled;
						}
					}
					else
					{
						var field = Ext.get(fieldName);
						if(field)
						{
							if(field.dom.type == 'checkbox')
							    field.dom.checked = obj[fieldName];
							else
								field.dom.value = obj[fieldName];
						}
					}
				}
			}
		},
		initScriptBefore : function(node, c) {
			var obj = {};
			EcCurrComp = c;
			var json = Ext.DomQuery.select('/b', node);
			if (json.length != 0) {
				var jsonText = json[0].text || json[0].textContent;
				try{
					eval(jsonText);
				}catch(e){alert('script is error: ' + jsonText);}
			}
		},initScript : function(node, c) {
			var obj = {};
			EcCurrComp = c;
			var json = Ext.DomQuery.select('/s', node);
			if (json.length != 0) {
				var jsonText = json[0].text || json[0].textContent;
				try{
					eval(jsonText);
				}catch(e){alert('script is error: ' + jsonText);}
			}
		},
		initEvent : function(root, c) {
			var obj = {};
			var json = Ext.DomQuery.select('/e', root);
			if (json.length != 0) {
				var jsonText = json[0].text || json[0].textContent;
				obj = Ext.util.JSON.decode(jsonText);
			}
			for (var fieldName in obj) {
				for (var eventName in obj[fieldName]) {
					var reftarget = obj[fieldName][eventName][0];
					var ref = reftarget.ref;
					var target = reftarget.target;
					var newc;
					for(var ii=0;ii<target.length;ii++)
					{
						if(target[ii] == 'self')
							newc = c;
						else
							newc = Ec.FormManager.get(target[ii]);
						if(newc)
						{
							var fields = Ec.Utils.find(fieldName, newc);
							for (var i = 0;i < fields.length; i++) {
								var scope = {
									c : newc,
									event : eventName,
									field : fieldName,
									ref : ref
								};
								if(fields[i].setURI)
										fields[i].setURI(c.uri,c.basicParams, ref, newc);
									else
										fields[i]._uri = c.uri;
								var handler = Ec.HandlerManager.getEventHandler(fields[i], eventName);
								if (handler) {
									fields[i].on(eventName, handler, scope);
								} else {
									fields[i].on(eventName, Ec.HandlerManager.eventHandler, scope);
								}
							}
						}
					}
				}
			}
		},
		initVis : function(root, c) {
			var obj = {};
			var json = Ext.DomQuery.select('/v', root);
			var formControlled = '0';
			if (json.length != 0) {
				var jsonText = json[0].text || json[0].textContent;
				obj = Ext.util.JSON.decode(jsonText);
				formControlled = Ext.DomQuery.selectValue('/@fc', json[0], '0');
			}
			if(obj)
			{
				var visFields = {};

				for (var fieldName in obj) {
					var index = fieldName.indexOf(':');
					var target = fieldName.substr(0,index);
					var field = fieldName.substr(index+1);
					var newc;
					if(target == 'self' || target == '')
						newc = c;
					else
						newc = Ec.FormManager.get(target);
					Ec.Utils.op(field,newc,obj[fieldName]);
					/*visFields[field] = 1;*/
				}

				if(formControlled == '3')
				{
					var curForm = Ec.Utils.getItem(c);
					if(curForm && curForm.cascade)
					{
						curForm.cascade(function(c1){
				            if(!visFields[c1.name]) {
				            	if( typeof c1.hide == 'function' && c1.isFormField)
									c1.hide();
							}
				        });
					}
				}
				else if(formControlled == '1')
				{
					var curForm = Ec.Utils.getItem(c);
					if(curForm && curForm.cascade)
					{
						curForm.cascade(function(c1){
				            if(!visFields[c1.name]) {
				            	if( typeof c1.hide == 'function' && c1.isFormField) {
									c1.disable();
				            	}
							}
				        });
					}
				}
				else if(formControlled == '2')
				{
					var curForm = Ec.Utils.getItem(c);
					if(curForm && curForm.cascade)
					{
						curForm.cascade(function(c1){
				            if(!visFields[c1.name]) {
				            	if( typeof c1.hide == 'function' && c1.isFormField)
									c1.enable();
							}
				        });
					}
				}
			}
		},
		initButtons: function (node, c){
			var curForm = c.win ? c.comp : Ec.Utils.getItem(c);
			curForm = Ec.Utils.find('ec_form',c,'xtype');
			for(var i=0; i<curForm.length; i++)
			{
				var form = curForm[i];
				var buttons = form.buttons;
				if(!buttons)
					continue;

				var eventName = 'click';
				for (var i = 0;i < buttons.length; i++) {
        			if(typeof buttons[i].events[eventName] == "boolean" && !buttons[i].initialConfig.handler){
						var scope = {
							c : c,
							event : eventName,
							field : buttons[i].name,
							ref : buttons[i].ref || ''
						};
						if(buttons[i].setURI)
							buttons[i].setURI(c.uri,c.basicParams);
						else
							buttons[i]._uri = c.uri;
						var handler = Ec.HandlerManager.getEventHandler(buttons[i], eventName);
						if (handler) {
							buttons[i].on(eventName, handler, scope);
						} else {
							buttons[i].on(eventName, Ec.HandlerManager.eventHandler, scope);
						}
        			}
				}
			}
		}
	}
}();

Ec.HandlerManager = function() {
	var reps = {};
	var eventHandlers = {};

	var validateForm = function(_form) {
		var msgs = [];
		var fs = [];
		_form.items.each(function(_f) {
			if(_f.hidden==true)
				return;
			var owner = _f.ownerCt;
			while(owner)
			{
				if(owner.hidden==true)
					return;
				 if(owner.checkbox)
				 {
					if(!owner.checkbox.dom.checked)
						return;
					owner = owner.ownerCt;
				 }
				 else
				 	owner = null;
			}
			_f.on('invalid', function(__f, msg) {
				if (fs) {
					for (var n = 0; n < fs.length; n++) {
						if (fs[n] == __f.name) {
							return;
						}
					}
					fs.push(__f.name);
					msgs.push((__f.fieldLabel ? __f.fieldLabel : __f.name) + " : " + msg );
				}
			}, _f);
		});
		_form.isValid();
		
		if(msgs.length >0) {
			Ext.Msg.alert('验证失败', msgs.join('<br>'));
			
			fs = null;
			msgs = null;
			return false;
		}

		return true;
	};

	var sendRequest = function(scope, component, c){
		// 增加触发事件之前的判断，可在控件中增加beforeRequest方法，返回true，继续提交，否则不提交
		if (component.beforeRequest && typeof component.beforeRequest == 'function') {
			if (!component.beforeRequest()) {
				return;
			}
		}
		
		if (component.type && component.type == 'submit') {
				var item = Ec.Utils.getItem(c);
				var params = {};
				var id,value;
				// item.getForm().items.each(function(f){
		        // id = f.name || f.id;
		        // value = Ec.Utils.getValueString(f);
		        // params[id] = value;
		        // });
		        if (!validateForm(item.getForm())) {
					return;
				}
		        Ec.Utils.iterateParam(params, item.getForm().items);
				Ec.Request.fireFieldEvent(scope, params, component);
			} else if(component.type && component.type == 'submit_no_validate') {
				var item = Ec.Utils.getItem(c);
				var params = {};
				var id,value;
		        Ec.Utils.iterateParam(params, item.getForm().items);
				Ec.Request.fireFieldEvent(scope, params, component);
			} else if (component.type && component.type == 'submit_obj') {
				var item = Ec.Utils.getItem(c);
				var values = {};
				var id,value;

				if(typeof item.getForm == 'function') {
					if (!validateForm(item.getForm())) {
						return;
					}
					item.fileUpload = false;
					Ec.Utils.iterateParam(values, item.getForm().items);
				}
				else
				{
					var form = Ec.Utils.find('ec_form',c,'xtype');
					if(form.length>0) {
						form = form[0];
						if (!validateForm(form[0])) {
							return;
						}
						form.fileUpload = false;
						Ec.Utils.iterateParam(values, form.items);
					}
				}
				Ec.Request.fireFieldEvent(scope, {data:Ext.encode(values)}, component);
			} else if (component.type && component.type == 'submit_noval') {
				var item = Ec.Utils.getItem(c);
				var values = {};
				var id,value;

				if(typeof item.getForm == 'function') {
					item.fileUpload = false;
					Ec.Utils.iterateParam(values, item.getForm().items);
				}
				else
				{
					var form = Ec.Utils.find('ec_form',c,'xtype');
					if(form.length>0) {
						form = form[0];
						form.fileUpload = false;
						Ec.Utils.iterateParam(values, form.items);
					}
				}
				Ec.Request.fireFieldEvent(scope, {data:Ext.encode(values)}, component);
			} else if (component.type && component.type == 'submit_file') {
				var form = Ec.Utils.find('ec_form',c,'xtype');
				if(form.length>0)
				{
					form = form[0];
					form.fileUpload = true;
					var params = {};
					var item = Ec.Utils.getItem(c);
					if (!validateForm(item.getForm())) {
						return;
					}
					Ec.Utils.iterateParam(params, item.getForm().items); // {data:Ext.encode(params)}
					Ec.Request.fireFieldEvent(scope, {data:Ext.encode(params)}, component, form);
				}
				else
				{
					Ec.Utils.msg('Info', 'The page should include form.');
				}
			} else if (component.type && component.type == 'submit_xml') {
				var item = Ec.Utils.getItem(c);
				var values = {};
				var id,value;

				if(typeof item.getForm == 'function') {
					if (!validateForm(item.getForm())) {
						return;
					}
					Ec.Utils.iterateParam(values, item.getForm().items);
				}
				else
				{
					var form = Ec.Utils.find('ec_form',c,'xtype');
					if(form) {
						if (!validateForm(form)) {
							return;
						}
						Ec.Utils.iterateParam(values, form.items);
					}
				}
				Ec.Request.fireFieldEvent(scope, {data:Ec.Utils.json2xml(values)}, component);
			} else {
				var params = {};
				if(component && scope.field) {
					params[scope.field] = component.getValue ? component.getValue() : '';
				}
				Ec.Request.fireFieldEvent(scope, params, component);
			}
	};

	return {
		eventHandler : function(component) {
			var c = this.c;
			if(component.confirmFn)
			{
				var rs = component.confirmFn.call(this);
				if('yes' != rs) {
					return;
				}
			}
			if(component.confirmMsg){
				Ext.Msg.show({
					 title : component.confirmTitle || ''
					,msg :component.confirmMsg
					,icon : Ext.Msg.QUESTION
					,buttons : Ext.Msg.YESNO
					,scope : this
					,fn:function(response) {
						if('yes' == response) {
							sendRequest(this, component,c);
						}
					}
				});
			}
			else
				sendRequest(this, component,c);
		},
		getEventHandler : function(c, event) {
			var xtypes = c.getXTypes().split('/');
			for (var i = xtypes.length - 1;i >= 0; i--) {
				var xtype = xtypes[i];
				var o = eventHandlers[xtype];
				if (o) {
					if (o[event])
						return o[event];
				}
			}
			return;
		},
		regEventHandler : function(xtype, event, handler) {
			var o = eventHandlers[xtype];
			if (o) {
				o[event] = handler;
			} else {
				eventHandlers[xtype] = {};
				eventHandlers[xtype][event] = handler;
			}
		},
		regReponseHandler : function(type, obj) {
			reps[type] = obj;
		},
		getReponseHandler : function(type) {
			return reps[type];
		},
		formValid : function(form) {
			return validateForm(form);
		}
	};
}();

Ec.FormManager = function() {
	var containers = {};
	return {
		put: function(renderTo, c) {
			containers[renderTo] = c;
		},
		close: function(renderTo,autoDestroy) {
			var c = containers[renderTo];
			if (c != null && c && c.comp) {
				if(!autoDestroy)
					c.comp.destroy();
				containers[renderTo] = null;
			}
		},
		get: function(renderTo) {
			return containers[renderTo];
		}
	};
}();

Ec.Response.ShowForm = function() {
	return {
		handler : function(c, result, uri) {
			var json = Ext.DomQuery.select('/f', result);
			var newTarget = c.renderTo;
			var target = Ext.DomQuery.select('/target', result);
			if (target.length != 0) {
				newTarget = target[0].text || target[0].textContent;
			}
			if(newTarget == '')
				newTarget = c.renderTo;
			else if(newTarget == '_blank')
			{
				newTarget = Ext.id();
				c.renderTo = newTarget;
			}
			var formObj;
			if(c.conf && c.conf.formObj)
				formObj = c.conf.formObj;
			if (!formObj && json.length != 0) {
				var jsonText = json[0].text || json[0].textContent;
				try {
					formObj = Ext.util.JSON.decode(jsonText);
				} catch (e) {
					alert("form json text error." + e);
				}
			}
			if(formObj)
			{
				Ec.FormManager.close(newTarget);
				formObj.renderTo = '';

				// added for enhancement
				if(c.conf && c.conf.formfilter)
				    formObj = c.conf.formfilter.call(c.conf.scope||c, formObj,c.conf);
//				 alert(formObj.xPos);
//				 alert(formObj.yPos);
				// end added.
				if(formObj.basicParams)
				{
					if(!c.basicParams)
						c.basicParams = {};
				    Ext.apply(c.basicParams, formObj.basicParams);
				    delete formObj.basicParams;
				}
				if(c.conf && c.conf.params)
				{
					if(!c.basicParams)
						c.basicParams = {};
				    Ext.apply(c.basicParams, c.conf.params);
				    delete c.conf.params;
				}
				if (!formObj.xtype) {
					formObj.xtype = 'ec_form'
				};
				var panel = null;
				var targetDom = Ext.getDom(newTarget);
				if(!targetDom)
				{
					Ext.DomHelper.append(document.body, {
						id : newTarget,
						name: newTarget
					}, true);
				}
				if(formObj.buttons)
				{
					for(var i=0; i<formObj.buttons.length; i++)
					{
						var bt = formObj.buttons[i];
						if(bt && bt.handler && typeof bt.handler == 'string')
						{
							try{
								bt.handler = eval("({handler:" + bt.handler + "})");
								bt.handler = bt.handler.handler;
							}catch(e){alert(e);}
						}
					}
				}
				if (c.win === false) {
					var winconf = {
//						title : title,
						renderTo : newTarget,
						layout : 'fit',
						width : formObj.width,
						autoHeight : true,
						autoScroll:true,
						bodyBorder : true,
						border : true,
						frame: false,
						items : formObj
					};
					if(formObj.showconf)
						Ext.apply(winconf, formObj.showconf);
					panel = new Ext.Panel( winconf );
					panel.on('resize', function() {
						this.items.items[0].setSize(this.getSize());
					}, this);
				} else {
					var title = formObj.title;
					delete formObj.title;
					var buttons = formObj.buttons;
					formObj.buttons = '';
					var w = formObj.width;
					formObj.autoWidth = true;
					var modal = !c.conf || !(!c.conf || (c.conf && c.conf.model==false));
					var winconf = {
						title : title,
						x: formObj.xPos,
						y: formObj.yPos,
						modal : modal,
						layout : 'fit',
						width : parseInt(w) == 'NaN' ?  400 : w+20,
						height : formObj.height,
						autoHeight : formObj.height?false: true,
						autoScroll : true,
						bodyBorder : false,
						border : false,
						plain : true,
						// frame: false,
						buttons : buttons,
						items : formObj
					};
					if(c.conf && c.conf.showconf)
					{
						Ext.apply(winconf, c.conf.showconf);
						delete c.conf.showconf;
					}

					if(formObj.showconf)
					{
						Ext.apply(winconf, formObj.showconf);
						delete formObj.showconf;
					}
					panel = new Ext.Window(winconf);
					panel.show();
					panel.on('close', function() {
						Ec.FormManager.close(c.renderTo, true);
						if(c.conf && c.conf.submit)
							c.conf.submit();
					});
					panel.el.disableShadow();
				}
				c.comp = panel;
				c.uri = uri;
				Ec.FormManager.put(newTarget,c);

				Ec.Response.initData(result, c);

				if(c.conf && c.conf.onFormCreated)
				{
					c.conf.onFormCreated();
					if(c.conf.single)
						delete c.conf.onFormCreated;
				}
				if (typeof formObj.onFormCreated == 'function'){
				 	formObj.onFormCreated(panel);
				}
			} else
				Ec.Utils.msg('', 'No form found.');
		}
	};
}();

Ec.HandlerManager.regReponseHandler(Ec.Request.SHOW, Ec.Response.ShowForm);

Ec.Response.LoadData = function() {
	return {
		handler : function(c, result, uri) {
			Ec.Response.initData(result, c.c);
		}
	};
}();
Ec.HandlerManager.regReponseHandler(Ec.Request.DATA_EVENT, Ec.Response.LoadData);

Ec.HandlerManager.regReponseHandler(Ec.Request.FIELD_EVENT, Ec.Response.LoadData);

Ec.Response.Null = {
	handler : function() {
	}
};
Ec.HandlerManager.regReponseHandler(Ec.Request.NULL, Ec.Response.Null);

Ec.Grid = Ext.extend(Ext.grid.EditorGridPanel, {
	paging : false,
	height : 300,
	pageSize : 20,
	remoteSort : false,
	hasStore : false,
	keepSelectedOnPaging: true,
	submitAllSel : false,
	clicksToEdit: 1,
	rowNumber : false,
	checkBox: false,
	viewConfig:{
         forceFit: true
    },
	pagingConfig:{
        pageSize: 15,
        store: this.store,
        displayInfo: true,
        displayMsg: "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
        emptyMsg: '<b>0</b> 条记录',
        onLoad : function(A, C, F) {
				if (!this.rendered) {
					this.dsLoaded = [A, C, F];
					return
				}
				this.cursor = F.params ? (F.params[this.paramNames.start] ? F.params[this.paramNames.start] : 0) : 0;
				var E = this.getPageData(), B = E.activePage, D = E.pages;
				this.afterTextEl.el.innerHTML = String.format(
						this.afterPageText, E.pages);
				this.field.dom.value = B;
				this.first.setDisabled(B == 1);
				this.prev.setDisabled(B == 1);
				this.next.setDisabled(B == D);
				this.last.setDisabled(B == D);
				this.loading.enable();
				this.updateInfo();
				this.fireEvent("change", this, E)
			}
     },
	initComponent : function() {
        if(this.view)
        	delete this.view;

		 if(this.Model){
             this.initModel();
         }
         if(this.paging){
             this.initPagingBar();
         }
         if(this.keepSelectedOnPaging){
             this.initOnPaging();
         }
		if(!this.plugins)
			this.plugins = [];

//		if(this.searchFormCfg && this.searchFormCfg.searchFields
//			&& this.searchFormCfg.searchFields.length>0)
//		{
			this.plugins.push(new Ec.Grid.SearchForm(this.searchFormCfg));
			this.header = false;
			this.border = true;
			this.bodyBorder = true;
//		}

		if(this.rowActions)
		{
			this.plugins.push(this.rowActions);
		}

		if(this.recordForm)
		{
			this.recordForm = new Ext.ux.grid.RecordForm({
				 title:this.title || ' '
				,iconCls:'icon-edit-record'
				,columnCount:2
				,ignoreFields:{compID:true}
				,readonlyFields:{action1:true}
				,disabledFields:{qtip1:true}
				,formConfig:{
					 labelWidth:80
					,buttonAlign:'right'
//					,bodyStyle:'padding-top:10px'
				}
			});
			this.plugins.push(this.recordForm);
		}

		if(this.rows)
		{
			this.plugins.push(new Ext.ux.plugins.GroupHeaderGrid());
		}
		//if (!this.paging && this.selectFunc && this.getSelectionModel()) {
		//this.getSelectionModel().on('rowselect', this.selectFunc, this);
		//}
		
		// 关于全选的，如果取消一行，就把全选标志去了
		if(this.checkBox && !this.singleSelect && !this.searchFormCfg){
			this.selModel.on('rowdeselect', function() {
				var checker = Ext.fly(this.getView().innerHd).child(".x-grid3-hd-inner");
		      	if (checker.hasClass("x-grid3-hd-checker-on")) { 
		       		checker.removeClass("x-grid3-hd-checker-on"); 
		      	} 
			}, this);
		
			this.store.on('load', function() {
				var checker = Ext.fly(this.getView().innerHd).child(".x-grid3-hd-inner"); 
		      	if (checker.hasClass("x-grid3-hd-checker-on")) { 
		       		checker.removeClass("x-grid3-hd-checker-on"); 
		      	} 
			},this);
		}
		
		if (this.computeWhenSelect && typeof this.selectInfo == 'function' && typeof this.deSelectInfo == 'function') {
			this.selModel.on('rowdeselect', this.deSelectInfo, this);
			this.selModel.on('rowselect', this.selectInfo, this);
		}
		
		if (this.needSelectEvent) {
			this.addEvents('selectrow');
			this.selModel.on('rowdeselect', this.selectRow, this);
			this.selModel.on('rowselect', this.selectRow, this);
		}
		
		if (this.selectFunc && Ext.type(this.selectFunc) == 'function' && this.getSelectionModel()) {
			this.getSelectionModel().on('rowselect', function(sm, index, record) {
				this.selectFunc(record.data);
			}, this);
		}
		this.recordIds = [];
		Ec.Grid.superclass.initComponent.apply(this, arguments);
	},
	initModel: function(){
         var gCm = new Array();
         var gRecord = new Array();
         if(this.rowNumber){
             gCm[gCm.length] = new Ext.grid.RowNumberer();
         }
         if(this.checkBox){
             var sm = new Ec.CheckboxSelectionModel({singleSelect : this.singleSelect || false});
             gCm[gCm.length] = sm;
             this.selModel = sm;
         }else
         {
			if(!this.selModel)
				this.selModel = new Ext.grid.RowSelectionModel( {singleSelect : this.singleSelect || false});
         }
         
         // 动态计算gird宽度，防止每列太挤了
         var w = 0;
         for(var i=0;i<this.Model.length;i++)
         {
             var g = this.Model[i];
             if (!g.width) {
             	g.width = 80;
             }
             
             if(g.visiable || g.visiable=='undefined' || g.visiable==null){
             	 w += g.width;
             	
             	 if(g.editor && (typeof g.editor == 'string'))
             	 {
             	 	g.editor_dt = g.editor;
             	 	g.editor = eval('('+g.editor+')');
             	 	if(g.editor_dt.indexOf('xtype')>=0)
             	 	{
             	 		g.editor = Ext.ComponentMgr.create(g.editor);
             	 	}
             	 }
             	 else if(g.editor)
             	 {
             	 	if(!g.editor.isFormField)
             	 	{
             	 		g.editor = Ext.ComponentMgr.create(g.editor);
             	 	}
             	 }
             	 if(g.editor)
             	 {
             	 	if(!g.renderer)
             	 		g.renderero = Ext.grid.ColumnModel.defaultRenderer;
             	 	else
             	 		g.renderero = g.renderer;
             	 	g.renderer = this.customRenderer.createDelegate(g);
             	 	g.editor.parentComp = this;
             	 }
                 gCm[gCm.length] = g;
             }
             if(g.dataIndex)
             {
	             gRecord[gRecord.length]=Ext.apply(g,{
	                 name: g.dataIndex,
	                 type: g.type || 'string',
	                 defaultValue: g.defaultValue
	             });
             }
         }

         // 动态计算gird宽度，防止每列太挤了
         //this.bodywidth = w;
         
         this.on("beforeedit", this.beforeEdit, this);
         this.on("afteredit", this.afterEdit, this);

         if(this.rowActions)
         {
         	if(this.rowActions.xtype)
         		this.rowActions = Ext.ComponentMgr.create(this.rowActions);

         	gCm[gCm.length] = this.rowActions;
         }
         if(this.tbar)
         {
	         for(var i=0;i<this.tbar.length;i++)
	         {
	             var g = this.tbar[i];
	             if(g.xtype)
	             {
	             	this.tbar[i] = Ext.ComponentMgr.create(g);
	             }
	         }
         }

         // create grid columnModel
         this.cm = new Ext.grid.ColumnModel(gCm);
         if(this.rows)
		 {
			this.cm.rows = this.rows;
		 }
		 
		 this.gCm = gCm;
		 this.gRecord = gRecord;
//         this.cm.defaultSortable = true;
         // create a jsonStore

         this.store = new Ext.data.Store({
         	 remoteSort : this.remoteSort,
             proxy: new Ext.data.HttpProxy({url: '',method: 'post'}),
             reader:new Ec.ArrayReader({
                 totalProperty: 'totalCount',
                 root: 'list'
             },
             Ext.data.Record.create(gRecord)
             )
         });
         this.store.grid = this;
         this.pagingConfig.store = this.store;
     },
     customRenderer : function(v, p, record, rowIndex){
     	var v = this.renderero(v, p, record, rowIndex);

        return '<div class="x-editable-cell">' + v +'</div>';
     },
     beforeEdit: function(e){
     	  this.editRecord = e.record;
     },
     afterEdit: function(e){
     	  this.editRecord = null;
     },
     initPagingBar: function(){
     	if(this.pageSize)
     		this.pagingConfig.pageSize = this.pageSize;
         var bbar = new Ext.PagingToolbar(this.pagingConfig);
         this.bbar = bbar;
     },
     initOnPaging: function(){
		 if(!this.idColName)
         	this.idColName = this.Model[0].dataIndex ;
         this.selModel.on('rowdeselect',function(selMdl,rowIndex,rec){
         	if(this.idColName)
             for(var i=0;i<this.recordIds.length;i++)
             {
                 if(rec.data[this.idColName] == this.recordIds[i][[this.idColName]]){
                     this.recordIds.splice(i,1);
                     return;
                 }
             }
        },this);
        this.selModel.on('rowselect',function(selMdl,rowIndex,rec){
        	if(this.idColName)
             if(this.hasElement(this.recordIds)){
                 for(var i=0;i<this.recordIds.length;i++){
                     if(rec.data[this.idColName] == this.recordIds[i][this.idColName]){
                         return;
                     }
                 }
             }
             this.recordIds.unshift(rec.data);
        },this);
        this.store.on('load',this.reselect,this);
        
        var g = this;
        this.store.on('load', function(s, records) {
        	if (g.el) {
			    g.el.select("table[class=x-grid3-row-table]").each(function(x) {  
			        x.addClass('x-grid3-cell-text-visible');  
			    });  
        	}
		    
		    /*// autoHeight时，如果高度太小，x-small-editor的height为auto时，editor的textarea显示不出来，最小高度设为50就可以了
		    var v = g.getView();
		    var h = records.length > 0 ? records.length * 30 : 0;
		    if (h > 0) {
		    	g.setHeight(h);
		    	g.autoHeight = false;
		    }*/
		    
		}); 
    },
     hasElement : function(recIds){
         if(recIds.length > 0)
             return true;
         else
             return false;
     },
	onRender : function() {
		Ec.Grid.superclass.onRender.apply(this, arguments);
	},
	reselect:function(){
        (function(){
		if(this.hasElement(this.recordIds) && this.idColName){
             this.store.each(function(rec){
                 Ext.each(this.recordIds,function(item,index,allItems){
                 	 if(rec.data[this.idColName] == item[this.idColName]){
                 	 	 if (this.needSelectEvent) {
                 	 	 	this.selModel.suspendEvents();
                 	 	 }
                         this.selModel.selectRecords([rec],true);
                          if (this.needSelectEvent) {
                 	 	 	this.selModel.resumeEvents();
                 	 	 }
                         return false;
                     }
                 },this);
             },this);
         }}).createDelegate(this).defer(100,this);
         
         // 解决IE浏览器下滚动条挡住记录的问题
         if (Ext.isIE && this.changeWidth) {
         	 var existsButtons = (this.buttons && this.buttons.length);
         	 
        	 //var h = this.getGridEl().getHeight();
         	 var viewHeader = this.getView().scroller.prev();
         	 
         	 var h = viewHeader.getHeight() + this.getView().scroller.getHeight();
        	 if (!existsButtons && !this.bbar) {
        		this.getGridEl().setHeight(h + 15);
        	 } else if (!existsButtons && this.bbar) {
        	 	var bbarEl = this.getGridEl().next();
        	 	if (!this.bbarHeight) {
        	 		this.bbarHeight = bbarEl.getHeight();
        	 	}
        	 	bbarEl.setHeight(this.bbarHeight + 17);
        	 } else {
        	 	var buttonsEl;
        		if (this.bbar) {
        	 		buttonsEl = this.getGridEl().next().next();
        	 	} else {
        	 		buttonsEl = this.getGridEl().next();
        	 	}
        	 	if (!this.buttonsHeight) {
        	 		this.buttonsHeight = buttonsEl.getHeight();
        	 	}
        	 	buttonsEl.setHeight(this.buttonsHeight + 15);
        	 }
         	
        	 this.addHeight = true;
         }
         
    },
	setURI : function(uri,params,ref, target){
		this._uri = uri;
		// this.store.proxy = new Ext.data.HttpProxy({url: this._uri});
		// this.store.baseParams = {data_event:'paging', this.name, params)
		this.store.proxy = new Ext.data.HttpProxy({url: this._uri + '?' + Ec.Request.DataEventParams('paging', this.name, params)});
		if(ref)
		{
			if(ref.length ==1 && ref[0]=='')
				return;
			this.ref = ref;
			this.target = target;
			this.store.on('beforeload',this.getReference,this);
		}
	},
	getReference: function(store, options){
		if(this.ref)
		{
			options = options || {};
			options.params = options.params || {};
			for (var i = 0;i < this.ref.length; i++) {
				var refField = this.ref[i];
				var refParam;
				if(refField.indexOf(':')>0)
				{
					refParam = refField.substr(refField.indexOf(':')+1);
					refField = refField.substr(0,refField.indexOf(':'));
				}

				var fields = Ec.Utils.find(refField,this.target);
				if(fields.length>0)
				{
					for (var j = 0;j < fields.length; j++) {
						options.params[refField] = Ec.Utils.getValueString(fields[j],refParam);// .getValue(refParam);
					}
				}
				else if(refField != '')
				{
					var comp = Ext.getCmp(refField);
					if(comp && comp.getValue)
						options.params[refField] = comp.getValue(refParam);
				}
			}
		}
	},
	setValue : function(values) {
		if(!values)
			return;
		if(values.selList) {
			this.recordIds = values.selList;
		} else {
			if (this.clearRecordIdsWhenLoad) {
				this.recordIds = [];
				if (this.computeWhenSelect && this.computeMethod 
					&& this[this.computeMethod] && typeof (this[this.computeMethod]) == 'function') {
					this[this.computeMethod]();
				}
			}
		}
		var list = values.list ? values.list : values;
		this.store.loadData(values);
		this.store.addListener('loadexception', function() {
			Ec.Utils.msg('Error', 'Load grid data error.');
		});
	},
	getFieldByName : function(prop, value){
        var m = this.findBy(function(c){
            return c[prop] === value;
        });
        if(m.length == 0 && this.searchBar && this.searchBar.form)
        {
        	m = m.concat(this.searchBar.form.find(prop,value));
        	this.searchBar.form.cascade(function(c1){
	            if (c1.buttons) {
					for (var i = 0;i < c1.buttons.length; i++) {
						if (c1.buttons[i].name == value) {
							m.push(c1.buttons[i]);
						}
					}
				}
	        });
        }
        return m;
    },
	getValueObject : function(record){
		var returndata = {};
		if(record)
	    {
			var datafields = this.store.fields.items;
			for(fi=0,flen=datafields.length;fi<flen;fi++)
			{
				if(datafields[fi].type == 'float' || datafields[fi].type == 'int')
					returndata[datafields[fi].name] = datafields[fi].convert(record.data[datafields[fi].name]);
				else if(datafields[fi].type == 'date' && datafields[fi].dateFormat!=null)
					returndata[datafields[fi].name] = Ext.util.Format.date(record.data[datafields[fi].name],datafields[fi].dateFormat);
				else
				    returndata[datafields[fi].name] = record.data[datafields[fi].name];
			}
		}
		return returndata;
	},
	getValue : function(refParam){
		var datas;
		if(!refParam)
			return;
		if(refParam == 'sel')
		{
			if (this.submitAllSel == false) {
				datas = this.getSelections();
			} else {
				datas = this.getSelectValue();
				return Ext.encode(datas);
			}
		}
		else if(refParam == 'dirty')
		{
			var records = this.store.data; // Get the Record
			datas = [];
			Ext.each(records.items, function(r, i) {
				var o = r.modified;
				if(o) {
					datas.push(r);
				}
			}, this);
		}
		else if(refParam == 'all')
		{
			var records = this.store.data; // Get the Record
			datas = [];
			Ext.each(records.items, function(r, i) {
				datas.push(r);
			}, this);
		}
		var i;
		var data;
		var recordlen=datas.length;
		var fi,flen;
		var submitdata;
		submitdata = [];
		for(i=0;i<recordlen;i++)
	    {
	    	data = datas[i];
	    	submitdata[i] = this.getValueObject(data);
	    }
		return Ext.encode(submitdata);
	},
	refresh : function() {
		this.store.reload();
	},
	getCursor : function() {
		var cursor = 0;
		if (this.paging) {
			cursor = this.getBottomToolbar().cursor;
		}
		return cursor;
	},
    getSelectValue: function(idField){
    	if (this.keepSelectedOnPaging) {
    		return this.recordIds;
    	} else {
    		var v = [];
    		if (this.singleSelect) {
    			var rec = this.selModel.getSelected();
    			if (rec) {
    				v.push(rec.data);
    			}
    		} else {
    			var recs = this.selModel.getSelections();
    			if (recs) {
    				for (var i = 0; i < recs.length; i++) {
    					v.push(recs[i].data);
    				}
    			}
    		}

    		return v;
    	}
    },
    addChildren : function(children) {
    	var store = this.store;
		if(store.recordType) {
			for(var i=0; i<children.length; i++)
			{
				var child = children[i];
				var rec = new store.recordType({newRecord:true});
				rec.fields.each(function(f) {
					rec.data[f.name] = child[f.name];
				});
				store.add(rec);
			}
		}
    },
    insertChildren : function(children, index) {
    	var store = this.store;
		if(store.recordType) {
			for(var i=0; i<children.length; i++)
			{
				var child = children[i];
				var rec = new store.recordType({newRecord:true});
				rec.fields.each(function(f) {
					rec.data[f.name] = child[f.name];
				});
				store.insert(index, rec);
			}
		}
    },
    updateChildren : function(children) {
    	if (!this.idColName) {
    		return;
    	}

    	var records = this.store.getRange();
    	for (var i = 0; i < records.length; i++) {
    		var rec = records[i];
    		for (var j = 0; j < children.length; j++) {
    			if (rec.data[this.idColName] == children[j][this.idColName]) {
    				rec.fields.each(function(f) {
						rec.data[f.name] = children[j][f.name];
					});
					
					rec.commit();
    			}
    		}
    	}
    },

    removeNode : function(node) {
    	var store = this.store;
    	store.remove(node);
    },
    removeSelection: function() {
    	var store = this.store;
		var datas = this.getSelectionModel().getSelections();
		for(i=0;i<datas.length;i++)
	    {
	    	store.remove(datas[i]);
	    }
		return true;
	},
    removeAll: function() {
    	var store = this.store;
    	store.removeAll();
		return true;
	},
	setSelectedData : function(datas) {
		if (datas && datas instanceof Array) {
			this.recordIds = datas;
			this.reselect();
		}
	},
	selectRow : function(grid) {
		if (this.selModel.rangeSelectLock) {
			return;
		}
		var selectRecords = grid.getSelections();
		var data = [];
		for(i=0;i<selectRecords.length;i++)
	    {
	    	record = selectRecords[i];
	    	data[i] = this.getValueObject(record);
	    }
		this.fireEvent('selectrow', grid, Ext.encode(data));
	}
});
Ext.reg('ec_grid', Ec.Grid);

Ec.ArrayReader = Ext.extend(Ext.data.JsonReader, {
	onMetaChange : function(meta, recordType, o){

    },
	readRecords : function(o){
    	this.jsonData = o;
        if(o.metaData){
            delete this.ef;
            this.meta = o.metaData;
            this.recordType = Ext.data.Record.create(o.metaData.fields);
            this.onMetaChange(this.meta, this.recordType, o);
        }
        var s = this.meta, Record = this.recordType,
            f = Record.prototype.fields, fi = f.items, fl = f.length;

// Generate extraction functions for the totalProperty, the root, the id, and
// for each field
        if (!this.ef) {
            if(s.totalProperty) {
	            this.getTotal = this.getJsonAccessor(s.totalProperty);
	        }
	        if(s.successProperty) {
	            this.getSuccess = this.getJsonAccessor(s.successProperty);
	        }
	        this.getRoot = s.root ? this.getJsonAccessor(s.root) : function(p){return p;};
	        if (s.id) {
	        	var g = this.getJsonAccessor(s.id);
	        	this.getId = function(rec) {
	        		var r = g(rec);
		        	return (r === undefined || r === "") ? null : r;
	        	};
	        } else {
	        	this.getId = function(){return null;};
	        }
            this.ef = [];
            for(var i = 0; i < fl; i++){
                f = fi[i];
                var map = (f.mapping !== undefined && f.mapping !== null) ? f.mapping : i;
                this.ef[i] = this.getJsonAccessor(map);
            }
        }

    	var root = this.getRoot(o), c = root.length, totalRecords = c, success = true;
    	if(s.totalProperty){
            var v = parseInt(this.getTotal(o), 10);
            if(!isNaN(v)){
                totalRecords = v;
            }
        }
        if(s.successProperty){
            var v = this.getSuccess(o);
            if(v === false || v === 'false'){
                success = false;
            }
        }
        var records = [];
	    for(var i = 0; i < c; i++){
		    var n = root[i];
	        var values = {};
	        var id = this.getId(n);
	        for(var j = 0; j < fl; j++){
	            f = fi[j];
                var v = this.ef[j](n);
                values[f.name] = f.convert((v !== undefined) ? v : f.defaultValue, n);
	        }
	        var record = new Record(values, id);
	        record.json = n;
	        records[i] = record;
	    }
	    return {
	        success : success,
	        records : records,
	        totalRecords : totalRecords
	    };
    }
});

Ec.CheckboxSelectionModel = Ext.extend(Ext.grid.RowSelectionModel, {
    header: '<div class="x-grid3-hd-checker">&#160;</div>',
    width: 20,
    sortable: false,

    // private
    menuDisabled:true,
    fixed:true,
    dataIndex: '',
    id: 'checker',

    // private
    initEvents : function(){
        Ec.CheckboxSelectionModel.superclass.initEvents.call(this);
        this.grid.on('render', function(){
            var view = this.grid.getView();
            view.mainBody.on('mousedown', this.onMouseDown, this);
            Ext.fly(view.innerHd).on('mousedown', this.onHdMouseDown, this);

        }, this);
        if(this.grid.singleSelect)
        	this.header = '';
    },

    // private
    onMouseDown : function(e, t){
        if(e.button === 0 && t.className.indexOf('ec-sel-column')>=0){ // Only fire if left-click
            e.stopEvent();
            var row = e.getTarget('.x-grid3-row');
            if(row){
                var index = row.rowIndex;
                if(this.isSelected(index)){
                    this.deselectRow(index);
                }else{
                    this.selectRow(index, true);
                }
            }
        }
    },

    // private
    onHdMouseDown : function(e, t){
        if(t.className == 'x-grid3-hd-checker'){
            e.stopEvent();
            var hd = Ext.fly(t.parentNode);
            var isChecked = hd.hasClass('x-grid3-hd-checker-on');
            if(isChecked){
                hd.removeClass('x-grid3-hd-checker-on');
                this.clearSelections();
            }else{
                hd.addClass('x-grid3-hd-checker-on');
                this.selectAll();
            }
        }
    },

    // private
    renderer : function(v, p, record, rowIndex){
        return '<div class="ec-sel-column' + (record.get('ec_selectable')==true? '': record.store.grid.singleSelect? ' x-grid3-row-radiochecker' :' x-grid3-row-checker') + '">&#160;</div>';

    },
    
    // 修改selectAll方法，在调用时设置一个变量，在selectRow时不触发与后台的交互
    selectAll : function() {
		if (this.locked) {
			return
		}
		this.selections.clear();
		this.rangeSelectLock = true;
		for (var B = 0, A = this.grid.store.getCount(); B < A; B++) {
			this.selectRow(B, true)
		}
		this.rangeSelectLock = false;
		this.grid.selectRow(this.grid);
	},
	// 取消全选也一样
	clearSelections : function(A) {
		if (this.locked) {
			return
		}
		if (A !== true) {
			var C = this.grid.store;
			var B = this.selections;
			this.rangeSelectLock = true;
			B.each(function(D) {
						this.deselectRow(C.indexOfId(D.id))
					}, this);
			B.clear();
			this.rangeSelectLock = false;
			this.grid.selectRow(this.grid);
		} else {
			this.selections.clear()
		}
		this.last = false
	}
});

Ec.Grid.RowActions = function(config) {
	Ext.apply(this, config);
	this.addEvents(
		 'beforeaction'
		,'action'
		,'beforegroupaction'
		,'groupaction'
	);
	Ec.Grid.RowActions.superclass.constructor.call(this);
};

Ext.extend(Ec.Grid.RowActions, Ext.util.Observable, {
	 actionEvent:'click'
	,autoWidth:true
	,dataIndex:''
	,header:''
	,menuDisabled:true
	,sortable:false
	,tplGroup:
		 '<tpl for="actions">'
		+'<div class="ux-grow-action-item<tpl if="\'right\'===align"> ux-action-right</tpl> '
		+'{cls}" style="{style}" qtip="{qtip}">{text}</div>'
		+'</tpl>'
	,tplRow:
		 '<div class="ux-row-action">'
		+'<tpl for="actions">'
		+'<div class="ux-row-action-item {cls} <tpl if="text">'
		+'ux-row-action-text</tpl>" style="{hide}{style}" qtip="{qtip}">'
		+'<tpl if="text"><span qtip="{qtip}">{text}</span></tpl></div>'
		+'</tpl>'
		+'</div>'
	,hideMode:'visiblity'
	,widthIntercept:4
	,widthSlope:21
	,init:function(grid) {
		this.grid = grid;
		if(!this.tpl) {
			this.tpl = this.processActions(this.actions);
		}
		// calculate width
		if(this.autoWidth) {
			this.width =  this.widthSlope * this.actions.length + this.widthIntercept;
			this.fixed = true;
		}
		var view = grid.getView();
		var cfg = {scope:this};
		cfg[this.actionEvent] = this.onClick;
		grid.afterRender = grid.afterRender.createSequence(function() {
			view.mainBody.on(cfg);
			if(this.actions)
				this.on('action', this.grid.onRowAction, grid);
			if(this.groupActions)
				this.on('groupaction', this.grid.onGroupAction, grid);
		}, this);

		if(!this.renderer) {
			this.renderer = function(value, cell, record, row, col, store) {
				cell.css += (cell.css ? ' ' : '') + 'ux-row-action-cell';
				return this.tpl.apply(this.getData(value, cell, record, row, col, store));
			}.createDelegate(this);
		}

		if(view.groupTextTpl && this.groupActions) {
			view.interceptMouse = view.interceptMouse.createInterceptor(function(e) {
				if(e.getTarget('.ux-grow-action-item')) {
					return false;
				}
			});
			view.groupTextTpl =
				 '<div class="ux-grow-action-text">' + view.groupTextTpl +'</div>'
				+this.processActions(this.groupActions, this.tplGroup).apply()
			;
		}
	}
	,getData:function(value, cell, record, row, col, store) {
		return record.data || {};
	}
	,processActions:function(actions, template) {
		var acts = [];
		Ext.each(actions, function(a, i) {
			if(a.iconCls && 'function' === typeof (a.callback || a.cb)) {
				this.callbacks = this.callbacks || {};
				this.callbacks[a.iconCls] = a.callback || a.cb;
			}
			var o = {
				 cls:a.iconIndex ? '{' + a.iconIndex + '}' : (a.iconCls ? a.iconCls : '')
				,qtip:a.qtipIndex ? '{' + a.qtipIndex + '}' : (a.tooltip || a.qtip ? a.tooltip || a.qtip : '')
				,text:a.textIndex ? '{' + a.textIndex + '}' : (a.text ? a.text : '')
				,hide:a.hideIndex
					? '<tpl if="' + a.hideIndex + '">'
						+ ('display' === this.hideMode ? 'display:none' :'visibility:hidden') + ';</tpl>'
					: (a.hide ? ('display' === this.hideMode ? 'display:none' :'visibility:hidden;') : '')
				,align:a.align || 'right'
				,style:a.style ? a.style : ''
			};
			acts.push(o);
		}, this);
		var xt = new Ext.XTemplate(template || this.tplRow);
		return new Ext.XTemplate(xt.apply({actions:acts}));
	}
	,onClick:function(e, target) {
		var view = this.grid.getView();
		var action = false;
		var row = e.getTarget('.x-grid3-row');
		var col = view.findCellIndex(target.parentNode.parentNode);
		var t = e.getTarget('.ux-row-action-item');
		if(t) {
			action = t.className.replace(/ux-row-action-item /, '');
			if(action) {
				action = action.replace(/ ux-row-action-text/, '');
				action = action.trim();
			}
		}
		if(false !== row && false !== col && false !== action) {
			var record = this.grid.store.getAt(row.rowIndex);
			if(this.callbacks && 'function' === typeof this.callbacks[action]) {
				this.callbacks[action](this.grid, record, action, row.rowIndex, col);
			}
			if(true !== this.eventsSuspended && false === this.fireEvent('beforeaction', this.grid, record, action, row.rowIndex, col)) {
				return;
			}
			else if(true !== this.eventsSuspended) {
				this.fireEvent('action', this.grid, record, action, row.rowIndex, col);
			}
		}
		t = e.getTarget('.ux-grow-action-item');
		if(t) {
			var group = view.findGroup(target);
			var groupId = group ? group.id.replace(/ext-gen[0-9]+-gp-/, '') : null;
			var records;
			if(groupId) {
				var re = new RegExp(Ext.escapeRe(groupId));
				records = this.grid.store.queryBy(function(r) {
					return r._groupId.match(re);
				});
				records = records ? records.items : [];
			}
			action = t.className.replace(/ux-grow-action-item (ux-action-right )*/, '');
			if(this.callbacks && 'function' === typeof this.callbacks[action]) {
				this.callbacks[action](this.grid, records, action, groupId);
			}
			else if(true !== this.eventsSuspended && false === this.fireEvent('beforegroupaction', this.grid, records, action, groupId)) {
				return false;
			}
			else if(true !== this.eventsSuspended) {
				var ids = groupId.split('-');
				this.fireEvent('groupaction', this.grid, records, action, ids[0], ids[1]);
			}
		}
	}
});
Ext.reg('ec_rowactions', Ec.Grid.RowActions);


Ec.Grid.SearchForm = function(config) {
	Ext.apply(this, config);
	Ec.Grid.SearchForm.superclass.constructor.call(this);
};

var searchBarHeader1 = '<table width="100%" border="0" cellspacing="0" cellpadding="0" class="mod_box">'+
			  '<tr>'+
			    '<td>' +
			    '<table width="100%" style="table-layout:auto;" border="0" cellspacing="0" cellpadding="0">'+
			      '<tr>'+			
			        '<td class="x-searchbar-left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>'+
			        '<td class="x-searchbar-middle" nowrap>';
var searchBarHeader2 = '</td>'+
			        '<td class="x-searchbar-right" width="100%" align="right">';
var searBarMiddle = '</td>'+
			      '</tr>'+
			    '</table>' +
			    '</td>'+
			  '</tr>' +
			  '<tr>'+
			  '<td><table width="100%" border="0" cellpadding="0" cellspacing="0" class="x-searchbar_table">'+
		       '<tr><td>';

var searchBarBottom = '</td></tr></table></td></tr></table>';

Ext.extend(Ec.Grid.SearchForm, Ext.util.Observable, {
	 searchIconCls:'icon-search'
	,searchText:'搜索条件'
	,columnCount:1
	,cancelIconCls:'icon-cross'
	,cancelText:'关闭'

	,init:function(grid) {
		grid.afterRender = grid.afterRender.createSequence(function() {
				var isTitle = false;
				
				if(this.searchable)
				{
					var btn = new Ext.Button({iconCls:this.searchIconCls,text:'搜索',scope:this, handler:this.onSearch, id: this.grid.id + 'internal_expand'});
			        btn.ownerCt = grid;
			        if(!this.buttons)
			        	this.buttons = [];
			        this.buttons.push(btn);
			        isTitle = true;
				}
				
				if(this.title)
					isTitle = true;
				
				if(isTitle === true)
				{
					this.formCt = this.grid.id + '_searchBar';
					this.buttonCt = this.grid.id + '_buttonCt';
					if(!this.title)
						this.title = this.searchText;
					var ct = this.grid.bwrap.createChild({cn: searchBarHeader1 + this.title + searchBarHeader2 + '<div id="' + this.buttonCt + '"></div>'+ searBarMiddle + '<div id="' + this.formCt + '"></div>' + searchBarBottom},this.grid.bwrap.dom.firstChild, true);
				
					 if(this.buttons && this.buttons.length > 0){
			            // tables are required to maintain order and for correct IE layout
					 	this.buttonCt = Ext.fly(this.buttonCt);
			            var tb = this.buttonCt.createChild({cn: {
//			                cls:"x-searchbar-right",
			                html:'<table cellspacing="0"><tbody><tr></tr></tbody></table><div class="x-clear"></div>'
			            }}, null, true);
			            var tr = tb.getElementsByTagName('tr')[0];
			            for(var i = 0, len = this.buttons.length; i < len; i++) {
			                var b = this.buttons[i];
			                var td = document.createElement('td');
			                td.className = 'x-panel-btn-td';
			                b.render(tr.appendChild(td));
			            }
			        }
				}
	            if(this.searchable)
				{
			        if(!this.window) {
						this.window = this.getPanel();
					}
				}
		}, this);
		
		this.grid = grid;
		grid.searchBar = this;
		
		if(grid.topToolbar && grid.topToolbar.length==0)
			delete grid.topToolbar;
		
		if(this.righttop)
		{
			this.buttons = grid.buttons;
			
			grid.buttons = null;
		}
		
		this.title = this.grid.title;

		if(this.searchFields)
		{
			if(this.formConfig.bodyStyle)
				delete this.formConfig.bodyStyle;
        	this.searchable = true;
		}
	}
	,onDestroy:function() {
		if(this.window) {
			this.window.destroy();
			this.window = null;
			this.form = null;
		}
		else if(this.form) {
			if('function' === typeof this.form.destroy) {
				this.form.destroy();
			}
			this.form = null;
		}
	},
	refreshParams: function(){
		var items = this.form.items;
		var values = {};

		items.each(function(f){
            if(f.isFormField) {
//            	if (f.isXType("datefield")) {
//            		v = f.getValue();
//            	}
            	var v = Ec.Utils.getValueString(f);
            	values[f.name] = v;
            }
        });
		values.fromsearch = '1';
		if(this.grid.store.baseParams)
			delete this.grid.store.baseParams;

		this.grid.store.baseParams = values;
		this.grid.store.search = true;
	}
	,onSearch:function() {
		// 先检查搜索条件是否必输的都满足了
		var items = this.form.items;
		var r = true;
		items.each(function(f){
			if (!f.validate()) {
				r = false;
			}
        });

        if (!r) {
        	return;
        }

		this.refreshParams();
		this.grid.store.load();
		
		// 可能根据搜索条件要进行一些处理，例如隐藏列等
		if (this.grid.afterSearchClick && typeof this.grid.afterSearchClick == 'function') {
			this.grid.afterSearchClick();
		}
	}
	,onCancel:function() {
		if(!this.window) {
			this.window = this.getPanel();
		}
		if(this.window.collapsed)
		{
			this.window.expand();
//			this.window.show();
//			this.window.collapsed = false;
			Ext.fly(this.formCt).show();
		}
		else
		{
			this.window.collapse();
			Ext.fly(this.formCt).hide();
//			this.window.hide();
//			this.window.collapsed = true;
			/*if(this.grid.store.search)
			{
				this.grid.store.baseParams = {};
				this.grid.store.load();
				delete this.grid.store.search;
			}*/
		}
	}
	,getPanel:function() {
		if(this.formCt) {
			var panel = Ext.getCmp(this.formCt);
			if(panel) {
				panel.add(this.form);
				panel.doLayout();
			}
			else {
				panel = Ext.fly(this.formCt);
				if(panel) {
					var cfg =Ext.apply(
						{
//						 title: this.title
//						,collapsible:true
//						,collapsed:this.collapsed //|| true
//						,
						width:'auto'
						,layout:'fit'
						,border:false
//						,collapsedCls:this.searchIconCls
						,renderTo:panel,
//			            title:'搜索条件',
						autoHeight: true,
			            layout:'tableform'
			            ,columns:this.columnCount
						,width:'auto',
						defaults : {width : 160},
						labelWidth : 120  // 默认label宽度
						,items: this.searchFields
						,cls : 'search-class'
//						,buttons:[{
//								 text:this.searchText
//								,name: 'grid_search_button'
//								,scope:this
//								,handler:this.onSearch
//								,iconCls:this.searchIconCls
//							},{
//								 text:this.cancelText
//								,iconCls:this.cancelIconCls
//								,scope:this
//								,handler:this.onCancel
//								,formBind:true
//							}]
					}, this.formConfig);
					panel = new Ext.Panel(cfg);
					panel.ownerCt = this.grid;
				}
			}
			Ext.fly(this.formCt).setVisibilityMode(Ext.Element.DISPLAY);
			if(panel.collapsed)
				Ext.fly(this.formCt).hide();
		}
		this.form = panel;//.items.itemAt(0);
		return panel;
	}
	,show:function(record, animEl) {
		if(!this.window) {
			this.window = this.getPanel();
		}
		this.window.show(animEl);
		this.record = record;
	}
});


Ec.TreeNodeUI = Ext.extend(Ext.tree.TreeNodeUI, {
    focus: Ext.emptyFn, // prevent odd scrolling behavior
    onOver : function(A) {
    	if (!Ext.get(this.elNode).hasClass("tree_obj")) {
    		this.addClass("x-tree-node-over")
    	} else {
    		this.addClass("tree_obj_over");
    	}
    },
    onOut : function(A) {
		if (!Ext.get(this.elNode).hasClass("tree_obj")) {
    		this.removeClass("x-tree-node-over")
    	} else {
    		this.removeClass("tree_obj_over");
    	}
	},
	toggleCheck : function(B) {
		var A = this.checkbox;
		var t = this.node.getOwnerTree();
		if (A && t.singleSelect == false) {
			A.checked = (B === undefined ? !A.checked : B);
			this.onCheckChange()
		}
	},
    renderElements : function(n, a, targetNode, bulkRender){
    	var t = n.getOwnerTree();
    	if(!t)
    		return;
    	if (t.isCanSelect) {
    		t.isCanSelect(n, a);
    	}

    	var cols = t.columns;
         if(cols)
         {
	        n.cols = new Array();
			var text = n.text || (c.renderer ? c.renderer(a[c.dataIndex], n, a) : a[c.dataIndex]);
			n.cols[cols[0].dataIndex] = text;

	        this.indentMarkup = n.parentNode ? n.parentNode.ui.getChildIndent() : '';
			var cb = typeof a.checkable=="boolean"?a.checkable:typeof a.checked=="boolean";

	        var bw = t.borderWidth;
	        var c = cols[0];
	        var buf = [
	             '<li class="x-tree-node"><div ext:tree-node-id="',n.id,'" class="x-tree-node-el x-tree-node-leaf ', a.cls,'">',
	                '<div class="x-tree-col ',(c.cls?c.cls:''),'" style="width:',c.width-bw,'px;">',
	                    '<span class="x-tree-node-indent">',this.indentMarkup,"</span>",
	                    '<img src="', this.emptyIcon, '" class="x-tree-ec-icon x-tree-elbow">',
	                    '<img src="', a.icon || this.emptyIcon, '" class="x-tree-node-icon',t.hideIcon?" x-hidden":"" ,(a.icon ? " x-tree-node-inline-icon" : ""),(a.iconCls ? " "+a.iconCls : ""),'" unselectable="on">',
	                    cb ? ('<input class="x-tree-node-cb" type="' + (t.singleSelect?'radio" name="dddttt"':'checkbox') + '" ' + (a.checked ? 'checked="checked" />' : '/>')) : '',
	            		'<a hidefocus="on" class="x-tree-node-anchor" href="',a.href ? a.href.indexOf("http://")>=0? a.href : root + a.href : "#",'" tabIndex="1" ',
	                    a.hrefTarget ? ' target="'+a.hrefTarget+'"' : "", '>',
	                    '<span unselectable="on">', c.dataIndex ? (c.renderer ? c.renderer(a[c.dataIndex], n, a) : a[c.dataIndex]?a[c.dataIndex]:'&#160;') : n.text,"</span></a>",
	                "</div>"];
	         for(var i = 1, len = cols.length; i < len; i++){
	             c = cols[i];
	             n.cols[cols[i].dataIndex] = text;
	             buf.push('<div class="x-tree-col ',(c.cls?c.cls:''),'" style="width:',c.width-bw,'px;">',
	                        '<div class="x-tree-col-text">',(c.renderer ? c.renderer(a[c.dataIndex], n, a) :  a[c.dataIndex]?a[c.dataIndex]:'&#160;'),"</div>",
	                      "</div>");
	         }
	         buf.push(
	            '<div class="x-clear"></div></div>',
	            '<ul class="x-tree-node-ct" style="display:none;"></ul>',
	            "</li>");
	        if(bulkRender !== true && n.nextSibling && n.nextSibling.ui.getEl()){
	            this.wrap = Ext.DomHelper.insertHtml("beforeBegin", n.nextSibling.ui.getEl(), buf.join(""));
	        }else{
	            this.wrap = Ext.DomHelper.insertHtml("beforeEnd", targetNode, buf.join(""));
	        }
	        this.elNode = this.wrap.childNodes[0];
	        this.ctNode = this.wrap.childNodes[1];
	        var cs = this.elNode.firstChild.childNodes;
	        this.indentNode = cs[0];
	        this.ecNode = cs[1];
	        this.iconNode = cs[2];
	        var index = 3;
	        if(cb){
	            this.checkbox = cs[3];
				// fix for IE6
				this.checkbox.defaultChecked = this.checkbox.checked;
	            index++;
	        }
	        this.anchor = cs[index];
	        this.textNode = cs[index].firstChild;
        }
        else
        {
        	//Ec.TreeNodeUI.superclass.renderElements.apply(this, arguments);
        	var D = n;
        	var I = a;
        	var H = targetNode;
        	var J = bulkRender;
        	I.checked = I.originalChecked;
        	this.indentMarkup=D.parentNode?D.parentNode.ui.getChildIndent():"";var E=typeof a.checkable=="boolean"?a.checkable:typeof I.checked=="boolean";var B=I.href?I.href:Ext.isGecko?"":"#";var C=["<li class=\"x-tree-node\"><div ext:tree-node-id=\"",D.id,"\" class=\"x-tree-node-el x-tree-node-leaf x-unselectable ",I.cls,"\" unselectable=\"on\"" + ((Ext.isIE && I.cls == 'tree_I') ? "style=\"padding-top:5px;\" " : "") + ">","<span class=\"x-tree-node-indent\">",this.indentMarkup,"</span>","<img src=\"",this.emptyIcon,"\" class=\"x-tree-ec-icon x-tree-elbow\" />","<img src=\"",I.icon||this.emptyIcon,"\" class=\"x-tree-node-icon",(I.icon?" x-tree-node-inline-icon":""),(I.iconCls?" "+I.iconCls:""),"\" unselectable=\"on\" />",E?("<input class=\"x-tree-node-cb\" type=\"" + (t.singleSelect?"radio\" name=\"dddttt\"" : "checkbox") + "\" "+(I.checked?"checked=\"checked\" />":"/>")):"","<a hidefocus=\"on\" class=\"x-tree-node-anchor\" href=\"",B.indexOf("http://")>=0? a.href : root + B,"\" tabIndex=\"1\" ",I.hrefTarget?" target=\""+I.hrefTarget+"\"":"","><span unselectable=\"on\">",D.text,"</span></a></div>","<ul class=\"x-tree-node-ct\" style=\"display:none;\"></ul>","</li>"].join("");var A;if(J!==true&&D.nextSibling&&(A=D.nextSibling.ui.getEl())){this.wrap=Ext.DomHelper.insertHtml("beforeBegin",A,C)}else{this.wrap=Ext.DomHelper.insertHtml("beforeEnd",H,C)}this.elNode=this.wrap.childNodes[0];this.ctNode=this.wrap.childNodes[1];var G=this.elNode.childNodes;this.indentNode=G[0];this.ecNode=G[1];this.iconNode=G[2];var F=3;if(E){this.checkbox=G[3];this.checkbox.defaultChecked=this.checkbox.checked;F++}this.anchor=G[F];this.textNode=G[F].firstChild;
        }
    }
});
Ec.ColumnTreeEditor = function(tree, config){
    config = config || {};
    var field = config.events ? config : new Ext.form.TextField(config);
    Ext.tree.TreeEditor.superclass.constructor.call(this, field);

    this.tree = tree;

    if(!tree.rendered){
        tree.on('render', this.initEditor, this);
    }else{
        this.initEditor(tree);
    }
};

Ext.extend(Ec.ColumnTreeEditor, Ext.Editor, {
    alignment: "l-l",
    autoSize: false,
    hideEl : false,
    cls: "x-small-editor x-tree-editor",
    shim:false,
    shadow:"frame",
//    maxWidth: 250,
    editDelay: 0,
    initEditor : function(tree){
        tree.on('beforeclick', this.beforeNodeClick, this);
        this.on('complete', this.updateNode, this);
        this.on('beforestartedit', this.fitToTree, this);
        this.on('startedit', this.bindScroll, this, {delay:10});
        this.on('specialkey', this.onSpecialKey, this);
    },
   fitToTree : function(ed, el){
        var td = this.tree.getTreeEl().dom, nd = el.dom;
        if(td.scrollLeft >  nd.offsetLeft){
        	td.scrollLeft = nd.offsetLeft;
        }
        var w = nd.style.width;
//        		Math.min(
//                this.maxWidth,
//                nd.offsetWidth);
                //(td.clientWidth > 20 ? td.clientWidth : td.offsetWidth) - Math.max(0, nd.offsetLeft-td.scrollLeft) - 5);
        this.setSize(w, '');
    },
    triggerEdit : function(node, e){
        var obj1;
		if (Ext.select(".x-tree-node-anchor", false, e.target).getCount() == 1) {
			obj1 = Ext.select(".x-tree-node-anchor", false, e.target).elements[0].firstChild;
		} else if (e.target.nodeName == 'SPAN' || e.target.nodeName == 'DIV'){
			obj1 = e.target;
		} else {
			return false;
		}
		var colIndex = 0;
		for (var i in node.cols) {
			if (node.cols[i] == obj1.innerHTML) {
				colIndex = i;
			}
		}
		if(this.tree.editAttributeFilter(node, colIndex))
		{
			this.editNode = node;
			this.editCol = obj1;
			this.editColIndex = colIndex;
			try{
				this.completeEdit();
			}catch(e){alert(e)};

			this.startEdit(obj1);
			if (obj1.nodeName == 'DIV') {
				var width = obj1.offsetWidth;
				this.setSize(width);
			}
		}
    },

    bindScroll : function(){
        this.tree.getTreeEl().on('scroll', this.cancelEdit, this);
    },

    beforeNodeClick : function(node, e){
        var sinceLast = (this.lastClick ? this.lastClick.getElapsed() : 0);
        this.lastClick = new Date();
        if(sinceLast > this.editDelay && this.tree.getSelectionModel().isSelected(node)){
            e.stopEvent();
            this.triggerEdit(node, e);
            return false;
        } else {
			this.completeEdit();
		}
    },
    updateNode : function(ed, value){
        this.tree.getTreeEl().un('scroll', this.cancelEdit, this);
      	this.editNode.cols[this.editColIndex] = value; //for internal use only
		this.editNode.attributes[this.editColIndex] = value;//duplicate into array of node attributes
		this.editCol.innerHTML = value;
    },
    onHide : function(){
        Ext.tree.TreeEditor.superclass.onHide.call(this);
        if(this.editNode){
            this.editNode.ui.focus();
        }
    },
    onSpecialKey : function(field, e){
        var k = e.getKey();
        if(k == e.ESC){
            e.stopEvent();
            this.cancelEdit();
        }else if(k == e.ENTER && !e.hasModifier()){
            e.stopEvent();
            this.completeEdit();
        }
    }
});

Ec.Tree = Ext.extend(Ext.tree.TreePanel, {
    lines:false,
    autoScroll:true,
    borderWidth: Ext.isBorderBox ? 0 : 2,
    singleSelect : true,
	recordIds: [],
//	idColName: undefined,
	checkBox: false,
	autocheckparent: true,
	autocheckchild: true,
	autoExpand : 1,
	dragSort : false,
	needAutoSize : false,
	autoSizeId : undefined,
	autosize : function() {
    	var m = document.getElementById(this.autoSizeId);
    	this.height = m.offsetHeight - 100;
    	this.width = m.offsetWidth - 60;
    },
	_iterateCreateNode : function(ld, jsonNode, extNode) {
		var expand = false;
		if (jsonNode.children && jsonNode.children.length > 0) {
			for (var nn = 0; nn < jsonNode.children.length; nn++) {
				var jn = jsonNode.children[nn];
				var en = ld.createNode(jn);
				var cexpand = this._iterateCreateNode(ld, jn, en);
				expand = expand || cexpand;
				extNode.appendChild(en);
			}
		}
		if(extNode.attributes.originalChecked)
		{
			this.recordIds.push(extNode.attributes);
			expand = true;
		}
		if(expand)
			extNode.attributes.expand = true;
		return expand;
	},
	// cls:'x-column-tree',
	beforeMove: function(tree, node, oldParent, newParent, index) {
		if (node.myInsert) {
			delete node.myInsert;
			return true;
		}
		var nextNode = newParent.item(index);
		Ext.MessageBox.confirm("提示", "您确定改变节点位置么？", function(btnId) {
			if (btnId == 'yes') {
				node.myInsert = true;
				newParent.insertBefore(node, nextNode);
				//_tree.fireEvent('aftermovenode', this, node.id, oldParent.id, newParent.id, index);
				_tree.fireEvent('movenode', this, node, oldParent, newParent, index);
			}
		});
		return false;
	},
	initComponent : function() {
		this.initModel();
		
		if (this.needAutoSize) {
			this.autosize();
		}
		
		if (this.dragSort) {
			var _tree = this;
			this.addEvents( 'aftermovenode' );
			this.on('beforemovenode', this.beforeMove, this);
        }

//        if(this.searchFormCfg)
		{
			if(!this.plugins)
				this.plugins = [];

//			this.tbar = this.tbar || [];
			this.plugins.push(new Ec.Tree.SearchForm(this.searchFormCfg));
			this.header = false;
		}

		Ec.Tree.superclass.initComponent.apply(this, arguments);
	},
	initModel: function(){
		this.rootVisible = false;
		this.children = [];
		this.recordIds = [];
		if(!this.loader)
		{
			this.loader = new Ext.tree.TreeLoader( {
				dataUrl : '',
				requestMethod : 'GET',
				uiProviders: {
					'sys':Ext.tree.TreeNodeUI,
					'def': Ec.TreeNodeUI
				},
				//----chengw add getParams start---
				getParams: function(node){
			        var buf = [], bp = this.baseParams;
			        for(var key in bp){
			            if(typeof bp[key] != "function"){
			                buf.push(encodeURIComponent(key), "=", encodeURIComponent(bp[key]), "&");
			            }
			        }
			        node.attributes.loader="";
			        var vStr = [];
			        vStr.push(Ext.encode(node.attributes));
			        vStr = vStr.join(",");
				    var nodeall="[" + vStr + "]";
				    
			        buf.push("node=", encodeURIComponent(node.id));
			        buf.push("&nodeall=", nodeall);
			        //alert(buf);
			        return buf.join("");
			    }
			    //----chengw add getParams end---
			})
		}

		if(!this.root)
		{
			this.root = new Ext.tree.AsyncTreeNode( {
				id : 'root-id',
				text : 'root'
			})
		}

	    if(this.checkBox)
	    {
	    	this.selModel = new Ext.tree.MultiSelectionModel();
	    	this.on('load',function(node){
	        	if(this.checkBox)
	        	{
		        	 var cs = node.childNodes;
		             for(var i = 0, len = cs.length; i < len; i++) {
			            	var child = cs[i];
			           	if(this.getElement(child.attributes[this.idColName])>-1)
			           		child.attributes.checked = true;
			           	else
			           		child.attributes.checked = false;
			         }
		        }
	        },this);
			this.on('checkchange', function(node,state){
				var value = node.attributes;
				if(!value)
					return;
				if(this.singleSelect)
				{
					this.recordIds.splice(0,this.recordIds.length,value);
					return;
				}
				else
				{
					var idColName = this.idColName || 'id';
					if(!value[idColName])
						value[idColName] = node.id;
					var i = this.getElement(value[idColName]);
	                //if(state && (i==-1))
	                var isChecked = state;//node.attributes.checked;
	                if(isChecked && i==-1) {
	                	this.recordIds.push(value);
	                }
	                else if(!isChecked && i>-1) {
	                	this.recordIds.splice(i,1);
	                }

	                // 判断其父节点是否选中，没有的话选中其父节点
	                if (isChecked) {

		                if(this.autocheckparent)
		                if (node.parentNode && typeof node.parentNode.attributes.checkable == 'boolean' && !node.parentNode.attributes.checked) {
		                	node.parentNode.checkbychild = true;
		                	node.parentNode.getUI().toggleCheck(true);
		                }

		                this.selModel.select(node);

		                // 如果是被其子节点造成的关联选中，下面不执行
		                if (node.checkbychild) {
		                	delete node.checkbychild;
		                	return;
		                }
	                }
	                else
	                	this.selModel.unselect(node);

	                if(this.autocheckchild)
	                {
		                // 判断如果选中当前node，将其子node都设为选中
		                if (!node.childNodes || node.childNodes.length == 0) {
		                	return;
		                }

		                if (isChecked) {
		                	var _theTree = this;
		                	Ext.each(node.childNodes, function(i) {
		                		this.getUI().toggleCheck(true);
		                	});
		                } else {
		                	var _theTree = this;
		                	Ext.each(node.childNodes, function(i) {
		                		this.getUI().toggleCheck(false);
		                	});
		                }
	                }
				}
	         }, this);
//	         this.getSelectionModel().on('selectionchange', function(sm, node, last){
//				node.toggleCheck(true);
//	         }, this);
	    }
		else
		{
	        this.getSelectionModel().on('selectionchange', function(sm, node, last){
				var value = node ? node.attributes : (last ? last.attributes : null);
				if(this.checkBox)
				{
					return;
				}
				if(!value)
					return;
				if(this.singleSelect)
				{
					this.recordIds.splice(0,this.recordIds.length,value);
					return;
				}
				else
				{
					var idColName = this.idColName || 'id';
					var i = this.getElement(value[idColName]);
	                //if(state && (i==-1))
	                if(i==-1)
	                	this.recordIds.push(value);
	                else if(i>-1)
	                	this.recordIds.splice(i,1);
				}
	         }, this);
	    }

     },
     getElement : function(recId){
     	var id = this.idColName || 'id';
		for(var i=0;i<this.recordIds.length;i++)
		{
			if(this.recordIds[i][id] == recId)
				return i;
		}
		return -1;
    },
	onRender : function(){
        Ec.Tree.superclass.onRender.apply(this, arguments);
        var cols = this.columns;
        this.recordIds = [];
        if(cols)
        {
//        	this.marker = this.body.createChild({cls:'x-tree-resize-marker'},this.innerCt.dom);
	        this.headers = this.body.createChild({cls:'x-tree-headers'},this.innerCt.dom);

	        var c;
	        var totalWidth = 0;
	        for(var i = 0, len = cols.length; i < len; i++){
	             c = cols[i];
	             totalWidth += c.width? c.width:0;
	             this.headers.createChild({
	                 cls:'x-tree-hd ' + (c.cls?c.cls+'-hd':''),
	                 cn: {
	                     cls:'x-tree-hd-text',
	                     html: c.header
	                 },
	                 style: c.width? 'width:'+(c.width-this.borderWidth)+'px;':''
	             });
	        }
	        this.headers.createChild({cls:'x-clear'});
	        // prevent floats from wrapping when clipped
	        if(totalWidth != 0)
	        {
		        this.headers.setWidth(totalWidth);
		        this.innerCt.setWidth(totalWidth);
	        }
        }
        if(this.autoExpand >= 0)
        {
        	this.root.expand();
        }
        else if(this.autoExpand == 0)
        {
        	this.root.expandChildNodes(true);
        }
        else if(this.autoExpand == -2)
        {
        	this.expandFirstNode(this.root);
        }
        if(this.editable)
        {
        	this.editor = new Ec.ColumnTreeEditor(this,{
                completeOnEnter: true,
                autosize: true,
                ignoreNoChange: true
            });
        }
    },
    setURI: function(uri, params, ref, target){
    	this._uri = uri;
    	this.loader.dataUrl = this._uri + '?' + Ec.Request.DataEventParams('nodeExpand', this.name, params);
    	if(ref)
		{
			if(ref.length ==1 && ref[0]=='')
				return;
			this.ref = ref;
			this.target = target;
			this.loader.on('beforeload',this.getReference,this);
		}
    	if(this.autoExpand > 0)
        	this.root.expandChildNodes();
    	else if(this.autoExpand == 0)
    		this.root.expandChildNodes(true);
    	else if(this.autoExpand == -2)
        	this.expandFirstNode(this.root);
    },
    getReference: function(store, options){
		if(this.ref)
		{
			options = options || {};
			options.params = options.params || {};
			for (var i = 0;i < this.ref.length; i++) {
				var refField = this.ref[i];
				var refParam;
				if(refField.indexOf(':')>0)
				{
					refParam = refField.substr(refField.indexOf(':')+1);
					refField = refField.substr(0,refField.indexOf(':'));
				}

				var fields = Ec.Utils.find(refField,this.target);
				if(fields.length>0)
				{
					for (var j = 0;j < fields.length; j++) {
						options.params[refField] = Ec.Utils.getValueString(fields[j],refParam);// .getValue(refParam);
					}
				}
				else if(refField != '')
				{
					var comp = Ext.getCmp(refField);
					if(comp && comp.getValue)
						options.params[refField] = comp.getValue(refParam);
				}
			}
		}
	},
	iterateNode : function(node, fn) {
		for (var nn = 0; nn < node.childNodes.length; nn++) {
			var jn = node.childNodes[nn];
			this.iterateNode(jn,fn);
		}
		fn(node);
	},
	setSelectedValue : function(values) {
		this.recordIds = values || [];
//		var pcheck = this.autocheckparent;
		var ccheck = this.autocheckchild;
//		this.autocheckparent = false;
		this.autocheckchild = false;
		this.iterateNode(this.root, function(node){
			var att = node.attributes;
			var idColName = this.idColName || 'id';
			if(this.getElement(att[idColName])==-1)
				node.getUI().toggleCheck(false);
			else
				node.getUI().toggleCheck(true);
		}.createDelegate(this));
//		this.autocheckparent = pcheck;
		this.autocheckchild = ccheck;
	},
	setValue : function(values) {
		if(!values)
			return;

		var node = this.getRootNode();
		for (var i = 0;i < this.children.length; i++) {
			this.children[i].remove();
		}
		var t = values.constructor;
		var att;
		if (values.constructor == Array) {
			for (var i = 0;i < values.length; i++) {
				att = values[i];
				if(att.children && att.children.length == 0)
					delete att.children;
				var en = this.loader.createNode(att);
				this._iterateCreateNode(this.loader, att, en)
				this.children.push(en);
			}
		} else {
			att = values;
			if(att.children && att.children.length == 0)
				delete att.children;

			var en = this.loader.createNode(att)
			this._iterateCreateNode(this.loader, att, en)
			this.children[0] = en;// new Ext.tree.AsyncTreeNode(values);
		}
		node.appendChild(this.children);
    	if(this.autoExpand == -2)
    		this.expandFirstNode(node);
		else if(this.autoExpand == 0)
			node.expandChildNodes(true);
		else
			node.expand();

		node.attributes.expand = true;
		this.expandTheNodes(node);

	},
	expandFirstNode: function(node){
		node.expand();
		if(node.childNodes && node.childNodes.length>0)
		{
			this.expandFirstNode(node.childNodes[0]);
		}
    },
    expandTheNodes : function(node) {
    	if (node.attributes.expand) {
    		node.expand();
    		if (node.childNodes && node.childNodes.length>0) {
    			for (var i = 0; i < node.childNodes.length; i++) {
    				this.expandTheNodes(node.childNodes[i]);
    			}
    		}
    	}
    },
    expandNode : function(nodeId) {
    	var nodeToExpand = this.root.findChild('id', nodeId);
    	if(nodeToExpand) {
    		nodeToExpand.expand(true);
//    		children = nodeToExpand.childNodes;
//    		if(children && children.length > 0) {
//    			for(var child in children) {
//    				this.expandNode(child.id);
//    			}
//    		}
    	}
    },
	getSelectValue: function(idField){
    	return this.recordIds;
    },
    attributeFilter : function(key, value){
		if(key == 'loader'|| key == 'uiProvider' || key == 'children')
			return false;
		return true;
    },
    editAttributeFilter : function(node){
		if(node == 'id'|| node == 'uiProvider')
			return false;
		return true;
    },
    encodeNodeValue: function(value){
	   var c = false, result = "{";
	
	   for(var key in value) {
		   	if (!this.attributeFilter || this.attributeFilter(key, value[key])) {
	           if (c) result += ',';
	           result += '"' + key + '":"' + value[key] + '"';
	           c = true;
	       }
	   }
	   result += "}";
	   return result;
    },
	getValue: function(param){
		if(param == 'all')
		{
			var node = this.getRootNode();
			return node.toJsonString(this.nodefilter, this.attributeFilter);
		}
		else if(param == 'sel')
		{
			var node = this.getSelectValue();//this.getSelectionModel().getSelectedNode();

			if(node.length >0)
			{
				var vStr = [];
				for (var i = 0; i < node.length; i++) {
	    			//vStr.push(Ext.encode(node[i]));
					//var v = {};	
					//v = node[i].attributes ? node[i].attributes : node[i];
					//----chengw modfiety start
					//v.loader='';
					//----chengw modfiety end
					//vStr.push(Ext.encode(v));
					vStr.push(this.encodeNodeValue(node[i]));
	    		}
				vStr = vStr.join(",");
				return "[" + vStr + "]";
			}
			else if(this.getSelectionModel())
			{
//				if(this.getSelectionModel().getSelectedNodes)
//				{
//					node = this.getSelectionModel().getSelectedNodes();
//					return node.toJsonString(this.nodefilter, this.attributeFilter);
//				}
//				else
				if(this.getSelectionModel().getSelectedNode)
				{
					node = this.getSelectionModel().getSelectedNode();
					if (node) {
						return node.toJsonString(this.nodefilter, this.attributeFilter);
					}
				}
			}
			return "[]";
		}
		else
    		return Ext.encode(this.recordIds);
    },
    setNodeText : function(id, text) {
    	var node = this.getNodeById(id);
    	if(node) {
    		node.setText(text);
    	}
    },
    loadChildNode : function(id) {
    	var node = this.getNodeById(id);
    	if (node) {
    		this.getLoader().load(node, function() {
    			node.expand();
    		});
    	}
    },
    addChildren : function(parent, children) {
    	var node = null;

    	if (parent) {
    		node = this.getNodeById(parent.id);
    	} else {
    		node = this.root;
    	}

    	if (node && children && children.length) {
    		node.leaf = false;
    		var firstChild;
    		for (var i = 0; i < children.length; i++) {
    			var en = this.loader.createNode(children[i]);

    			this._iterateCreateNode(this.loader, children[i], en);
    			if(i==0)
    				firstChild = en;
    			node.appendChild(en);
    		}
    		var tt = this;
    		if(firstChild)
    		{
	    		node.expand(null, null, function() {
	    			tt.getNodeById(firstChild.id).select();
	    		});
    		}
    	}
    },
    /**
     * please set parent node Id in text field before in this method
     */
    /**
     * 2010-03-02 zhangtao modify
     * 2010-03-02 is remove parent when children is empty
     * @param {} children
     * @param {} isRemoveParent
     */
    removeNodes : function(children, isRemoveParent){
    	var node = null;
    	if(children && children.length){
    		for(var i = 0; i < children.length; i++){
    			if(children[i].text == "-1"){
    				node = this.root;
    			}else{
    				node = this.getNodeById(children[i].text);
    			}
    			var dd = this.getNodeById(children[i].id);
    			if(dd && node){
    				node.removeChild(dd);
    				// zhangtao modify
    				if(!node.hasChildNodes()) {// if doesn't have children
    					if(isRemoveParent) {// if reove parent
    						var parent = node.parentNode;
    						if(parent) {// if parent
    							parent.removeChild(node);
    						}
    					}
    				}
    			}
    		}
    	}
    },
    removeNode : function(nodeId) {
    	var node = this.getNodeById(nodeId);
    	if (node) {
    		var parent = node.parentNode;
    		parent.removeChild(node);
    	}
    },
    replaceNode : function(nodeId, newNode){
    	var node = this.getNodeById(nodeId);
    	if(node){
    		var parent = node.parentNode;
    		var nextSibling = node.nextSibling;
    		var newNode = this.loader.createNode(newNode);
    		parent.removeChild(node);
    		parent.insertBefore(newNode, nextSibling);
    	}

    },
    removeAllNode : function(nodeId){
    	var root = this.getRootNode();
    	var parent = this.getNodeById(nodeId);
    	root.removeChild(parent);

    	if(parent && parent.childNodes.length){
    		var child = parent.childNodes;
    		for(var ii = 0; ii < child.length; ii){
    			parent.removeChild(child[0]);

    		}
    	}
    },
    deleteItem : function(){
		var selectedItem = this.getSelectionModel().getSelectedNode();
		if (!selectedItem) {
			Ext.Msg.alert('Warning', 'Please select an Item to delete.');
			return false;
		}
		selectedItem.remove();
	},
    addItem : function(values){
		var selectedItem = this.getSelectionModel().getSelectedNode();
		if (!selectedItem) {
			selectedItem = this.getRootNode();
		}
		if (values.constructor == Array) {
			for (var i = 0;i < values.length; i++) {
				var att = values[i];
				if(att.children && att.children.length == 0)
					delete att.children;
				var newNode = this.loader.createNode(att);
				if(selectedItem.isLeaf()) {
					selectedItem.parentNode.insertBefore(newNode, selectedItem.nextSibling);
				} else {
					selectedItem.insertBefore(newNode, selectedItem.firstChild);
				}
			}
		}
		else
		{
			var newNode = this.loader.createNode(values);
			if(selectedItem.isLeaf()) {
				selectedItem.parentNode.insertBefore(newNode, selectedItem.nextSibling);
			} else {
				selectedItem.insertBefore(newNode, selectedItem.firstChild);
			}
		}
	},
    addSearchCond : function(conds){
    	this.loader.baseParams = this.loader.baseParams || {};
    	Ext.apply(this.loader.baseParams, conds);
	}
});
Ext.reg('ec_tree', Ec.Tree);


Ec.Tree.SearchForm = function(config) {
	Ext.apply(this, config);
	Ec.Tree.SearchForm.superclass.constructor.call(this);
};

Ext.extend(Ec.Tree.SearchForm, Ec.Grid.SearchForm, {
	
	expandChild : false

	,onSearch:function() {
		var items = this.form.items;
		var values = {};
		items.each(function(f){
            if(f.isFormField) {
            	var v = f.getValue();
            	v = Ec.Utils.getValueString(f);
            	if (f.isXType("datefield")) {
            		if (f.value) {
            			v = f.value;
            		}
            	}
            	v = encodeURI(v);
            	values[f.name] = v;
            }
        });
        values['fromTree'] = 1;
		if(this.tree.loader.baseParams)
			delete this.tree.loader.baseParams;

		this.tree.loader.baseParams = values;
		var node = this.tree.getRootNode();
//		if(node.childNodes)
//		{
//	        this.tree.loader.load(node.childNodes[0]);
//	        for(var i = node.childNodes.length; i > 0; i--){
//	            this.tree.loader.load(node.childNodes[i-1]);
//	        }
//        }

		while(node.firstChild){
            node.removeChild(node.firstChild);
        }
        node.loading = false;
        node.reload(function(){node.expand();});
//		node.collapse(false, false);
//
//        node.childrenRendered = false;
//        node.loaded = false;
//        node = new Ext.tree.AsyncTreeNode( {
//				id : 'root-id',
//				text : 'root'
//			});
//		this.tree.setRootNode(node);
//		node = this.tree.getRootNode();
//		node.expand();
//        node.expanded = false;
//		node.reload(function(){node.expand(true);});
        this.tree.loader.search = '1';
        if(this.expandChild) {// if expand child
        	node.expand(true);
        }
	}
	,onCancel:function() {
		if(!this.window) {
			this.window = this.getPanel();
		}
		if(this.tree.loader.search)
		{
			this.tree.loader.baseParams = {};
			var node = this.tree.getRootNode();
			if(node.childNodes)
			{
		        for(var i = node.childNodes.length; i > 0; i--){
		            node.removeChild(node.childNodes[i-1]);
		        }
	        }
	        this.tree.loader.load(node);
			delete this.tree.loader.search;
		}
	}
});

Ext.tree.TreePanel.prototype.toJsonString = function(nodeFilter, attributeFilter, attributeMapping){
	return this.getRootNode().toJsonString(nodeFilter, attributeFilter, attributeMapping);
};
Ext.tree.TreeNode.prototype.toJsonString = function(nodeFilter, attributeFilter, attributeMapping){
   if (nodeFilter && (nodeFilter(this) == false)) {
       return '';
   }
   var c = false, result = "{";

   if (!attributeFilter || attributeFilter("id", this.id)) {
       result += 'id:"' + this.id + '"';
       c = true;
   }

//    Add all user-added attributes unless rejected by the attributeFilter.
   for(var key in this.attributes) {
	   	if ((key != 'id') && (!attributeFilter || attributeFilter(key, this.attributes[key]))) {
           if (c) result += ',';
           if (attributeMapping && attributeMapping[key]) {
               thisKey = attributeMapping[key];
           } else {
               thisKey = key;
           }
           result += '"' + thisKey + '":"' + this.attributes[key] + '"';
           c = true;
       }
   }

//    Add child nodes if any
	var children = this.childNodes;
	var clen = children.length;
	if(clen != 0){
	    if (c) result += ',';
	    result += '"children":['
	    for(var i = 0; i < clen; i++){
	        if (i > 0) result += ',';
	        result += children[i].toJsonString(nodeFilter, attributeFilter, attributeMapping);
	    }
	    result += ']';
	}

   return result + "}";
};

Ext.tree.TreePanel.prototype.toXmlString = function(nodeFilter, attributeFilter, attributeMapping){
   return '\u003C?xml version="1.0"?>\u003Ctree>' +
      this.getRootNode().toXmlString(nodeFilter, attributeFilter, attributeMapping) +
       '\u003C/tree>';
};

Ext.tree.TreeNode.prototype.toXmlString = function(nodeFilter, attributeFilter, attributeMapping){
//    Exclude nodes based on caller-supplied filtering function
   if (nodeFilter && (nodeFilter(this) == false)) {
       return '';
   }
   var result = '\u003Cnode';

//    Add the id attribute unless the attribute filter rejects it.
   if (!attributeFilter || attributeFilter("id", this.id)) {
       result += ' id="' + this.id + '"';
  }

//    Add all user-added attributes unless rejected by the attributeFilter.
   for(var key in this.attributes) {
       if ((key != 'id') && (!attributeFilter || attributeFilter(key, this.attributes[key]))) {
           if (attributeMapping && attributeMapping[key]) {
               thisKey = attributeMapping[key];
           } else {
               thisKey = key;
       }
           result += ' ' + thisKey + '="' + this.attributes[key] + '"';
       }
   }

//    Add child nodes if any
   var children = this.childNodes;
   var clen = children.length;
   if(clen == 0){
       result += '/>';
   }else{
       result += '>';
       for(var i = 0; i < clen; i++){
           result += children[i].toXmlString(nodeFilter, attributeFilter, attributeMapping);
       }
       result += '\u003C/node>';
   }
   return result;
};

Ec.Form = Ext.extend(Ext.form.FormPanel, {
    lines:false,
    autoScroll:true,
    borderWidth: Ext.isBorderBox ? 0 : 2,
    singleSelect : true,
	recordIds:[],
	idColName:null,
	checkBox: false,
	layout: 'tableform',
	columns: 1,

	initComponent : function() {
		
		if(!this.plugins)
			this.plugins = [];

		this.plugins.push(new Ec.Grid.SearchForm(this.searchFormCfg));
		this.header = false;
		
		Ec.Form.superclass.initComponent.apply(this, arguments);
	},
	afterRender : function(){
        Ec.Form.superclass.afterRender.apply(this, arguments);
        if(this.recordIds.length>0)
        	this.getForm().setValues(this.recordIds[0]);
    },
    getSelectValue: function(idField){
    	return [this.getForm().getValues()];
    }
});
Ext.reg('ec_form', Ec.Form);

Ec.Wizard = Ext.extend(Ext.Panel, {
	confirmTitle:'提示',
	confirmMsg:'确认关闭?',
	defaults:{autoScroll: true},
	clearButtons:true,
    initComponent : function() {
		this.prev = new Ext.Button({
					 text:'Prev'
					,name:'t_wizard_prev'
					,scope:this
					,handler:this.onPrev
					,formBind:true
				});
		this.next = new Ext.Button({
					 text:'Next'
					,name:'t_wizard_next'
					,scope:this
					,handler:this.onNext
					,formBind:true
				});
		this.finish = new Ext.Button({
					 text:'Finish'
					,name:'t_wizard_finish'
					,scope:this
					,handler:this.onFinish
					,formBind:true
				});
		this.close = new Ext.Button({
					 text:'Close'
					,name:'t_wizard_close'
					,scope:this
					,handler:this.onClose
					,formBind:true
				});
		if(!this.buttons)
			this.buttons = [];
		this.buttons = this.buttons.concat([this.prev, this.next, this.finish, this.close]);
		this.addEvents(
            'beforepagechange',
            'pagechange',
            "finish",
            'close'
        );
		delete this.layout;
        this.setLayout(new Ext.layout.CardLayout({
            deferredRender: this.deferredRender
        }));
        Ec.Wizard.superclass.initComponent.apply(this, arguments);
	},
	afterRender : function(){
        Ec.Wizard.superclass.afterRender.apply(this, arguments);

        this.activeIndex = 0;
		this.setActivePage(this.activeIndex);
        this.refreshButtons();
        this.getLayout().setActiveItem(this.items.items[this.activeIndex]);
    },

    setActivePage : function(index){
    	this.activeIndex = index;
    	var item = this.items.items[index];
        item = this.getComponent(item);
        if(!this.rendered){
            this.activeTab = item;
            return;
        }
    	item = this.getComponent(item);
    	if(item.pageLoad)
        {
        	Ec.Request.panel(item.target,item.pageLoad,
	    		{
		        	formfilter: this.formfilter,
		        	scope: this,
		        	buttons:[],
		        	item: item
		        }
        	);
        	delete item.pageLoad;
        }
        this.layout.setActiveItem(item);
        if(item.doLayout){
            item.doLayout();
        }
        this.refreshButtons();
        this.fireEvent('pagechange', this, item);
    },
    formfilter: function(formObj,opts){
    	if(formObj)
    	{
    		delete formObj.id;
    		opts.item.title = formObj.title;
    		delete formObj.title;
    		formObj.width = this.body.dom.offsetWidth-14;
    		formObj.height = this.body.dom.offsetHeight-14;
    		if(this.clearButtons && opts && opts.buttons)
    		{
    			delete formObj.buttons;
    			formObj.buttons = opts.buttons;
    		}
    		return formObj;
    	}
    	else if(opts)
    	{
    		alert('no form found in the page.');
    	}
    },
    onPrev: function(){
    	var item = this.items.items[this.activeIndex];
    	var i, len = this.activeIndex;
    	var values = {};
    	for(i=0;i<len;i++)
    	{
    		var target = this.items.items[i].target;
    		values[target] = this.getCurrItemValue(this.items.items[i]);
    	}
    	values = Ext.encode(values);

    	var previtem = this.items.items[this.activeIndex+1];
    	if(previtem)
    	{
	    	previtem = this.getComponent(previtem);
	    	if(previtem.pageLoad)
	        {
	        	Ec.Request.panel(previtem.target,previtem.pageLoad,
		    		{
			        	formfilter: this.formfilter,
			        	scope: this,
			        	buttons:[],
			        	item: previtem
			        }
	        	);
	        	delete previtem.pageLoad;
	        }
	        if(item.doLayout){
	            item.doLayout();
	        }
    	}
    	this.fireEvent('beforepagechange', this, {action:'prev',target:item.target, value:this.getCurrItemValue(item), values: values}, this.activeIndex);
    },
    onClose: function(){
    	if(this.confirmMsg)
		{
			Ext.Msg.show({
				 title : this.confirmTitle || ''
				,msg :this.confirmMsg
				,icon : Ext.Msg.QUESTION
				,buttons : Ext.Msg.YESNO
				,scope : this
				,fn:function(response) {
					if('yes' == response) {
    					this.fireEvent('close', this, {action:'close'}, this.activeIndex);
						return;
					}
				}
			});
		}
    },
    onNext: function(){
    	var item = this.items.items[this.activeIndex];
    	var i, len = this.activeIndex;
    	var values = {};
    	for(i=0;i<len;i++)
    	{
    		var target = this.items.items[i].target;
    		values[target] = this.getCurrItemValue(this.items.items[i]);
    	}
    	values = Ext.encode(values);

    	var nextitem = this.items.items[this.activeIndex+1];
    	if(nextitem)
    	{
	    	nextitem = this.getComponent(nextitem);
	    	if(nextitem.pageLoad)
	        {
	        	Ec.Request.panel(nextitem.target,nextitem.pageLoad,
		    		{
			        	formfilter: this.formfilter,
			        	scope: this,
			        	buttons:[],
			        	item: nextitem
			        }
	        	);
	        	delete nextitem.pageLoad;
	        }
	        if(item.doLayout){
	            item.doLayout();
	        }
    	}
    	this.fireEvent('beforepagechange', this, {action:'next',target:item.target, value:this.getCurrItemValue(item), values: values}, this.activeIndex);
    },
    getCurrItemValue: function(item){
        item = this.getComponent(item);
        var values={};
        if(item.target)
        {
        	var c = Ec.FormManager.get(item.target);
        	if(c && c.comp)
        	{
        		var form = c.comp.items.items[0];
        		if(form.getForm)
        		{
        			Ec.Utils.iterateParam(values, form.getForm().items);
//	        		form.getForm().items.each(function(f){
//			            id = f.name || f.id;
//			            values[id] = f.getValue();
//			        });
        		}
        		else if(form.getValue)
			    {
			    	values[form.name||form.id] = form.getValue();
			    }

        	}
        	else if(item.getForm)
        	{
        		Ec.Utils.iterateParam(values, item.getForm().items);
//        		item.getForm().items.each(function(f){
//			            id = f.name || f.id;
//			            values[id] = f.getValue();
//			        });
        	}
        	else if(item.getValue)
		    {
		    	values[item.name||item.id] = item.getValue();
		    }
        }
        values =Ext.encode(values);
        return values;
    },
    onFinish: function(){
    	var item = this.items.items[this.activeIndex];
    	var i, len = this.items.items.length;
    	var values = {};
    	for(i=0;i<len;i++)
    	{
    		var target = this.items.items[i].target;
    		values[target] = this.getCurrItemValue(this.items.items[i]);
    	}
    	values = Ext.encode(values);
    	this.fireEvent('finish', this, {action:'finish',target:'', value:this.getCurrItemValue(item), values: values}, this.activeIndex);
    },
    refreshButtons: function(){
    	if(this.activeIndex==0)
    		this.prev.hide();
    	else
    		this.prev.show();
    	if(this.activeIndex==this.items.items.length-1)
    	{
    		this.next.hide();
    		this.finish.show();
    	}
    	else
    	{
    		this.next.show();
    		this.finish.hide();
    	}
    	this.close.show();
    },
    setValue:function(values){
    	if(typeof values == 'number')
    	{
    		this.setActivePage(values);
    	}
    }
});
Ext.reg('ec_wizard', Ec.Wizard);

Ec.BrowserField = Ext.extend(Ext.form.TextField,  {
	selectUrl: '',
	valueField : 'id',
	multiSelect: false,
    buttonText: '',
    iconCls:'icon-open-popup',
    cancelText: '',
    cancelIconCls: 'icon-cross',
    buttonOnly: false,
    buttonOffset: 3,
    // private
    readOnly: true,
    fireSelectOnInit : true,

    autoSize: Ext.emptyFn,

    initComponent: function(){
    	this.winTarget = Ext.id();
    	this.values = [];
        Ec.BrowserField.superclass.initComponent.call(this);
        this.addEvents(
            'valueselected',
            'valuecleared',
            'setselecturl'
        );
    },

    onRender : function(ct, position){
        Ec.BrowserField.superclass.onRender.call(this, ct, position);

        this.wrap = this.el.wrap({cls:'x-form-field-wrap'});
        this.el.setVisibilityMode(Ext.Element.DISPLAY);
        this.el.setSize(0);
        this.el.hide();
        var m = ['<table cellspacing="0" class="noboder" style="border:0px;">',
                '<tr><td style="margin-left:0px;border:0" align="left" class="x-select-left"></td><td class="x-select-middle" style="margin-left:0px;border:0" align="left"></td><td class="x-select-right" align="left"></td></tr></table>'];
        var el = document.createElement("div");
        el.innerHTML = m.join("");

        this.wrap.insertFirst(el);
        this.displayInput = new Ext.form.TextField({
            renderTo: this.wrap.child("td.x-select-left", true),
            readOnly: this.readOnly
        });

        var btnCfg = Ext.applyIf(this.buttonCfg || {}, {
            text: this.buttonText,
            iconCls: this.iconCls
        });
        this.button = new Ext.Button(Ext.apply(btnCfg, {
            renderTo: this.wrap.child("td.x-select-middle", true),
            // cls: 'x-btn-icon',
            scope:this,
            handler: function(){
            	var ps = {};
            	var str = this.selectUrl;
            	var num = str.indexOf("?");
				if(num>=0)
				{
					var parastr = str.substr(num + 1);
					str = str.substring(0,num);
					var arrtmp = parastr.split("&");
					for (i = 0; i < arrtmp.length; i++) {
						num = arrtmp[i].indexOf("=");
						if (num > 0) {
							var name = arrtmp[i].substring(0, num);
							var value = arrtmp[i].substr(num + 1);
							ps[name] = value;
						}
					}
				}
            	if(this.params)
            	{
            		for (var fieldName in this.params) {
            			var refField = fieldName;
						var refParam;
						if(refField.indexOf(':')>0)
						{
							refParam = refField.substr(refField.indexOf(':')+1);
							refField = refField.substr(0,refField.indexOf(':'));
						}
            			var fields = Ec.Utils.find(refField, this.params[fieldName]);

            			if(fields.length>0)
            				ps[refField] = Ec.Utils.getValueString(fields[0],refParam);
            		}
            	}

		        Ec.Request.window(this.winTarget,this.selectUrl,
	        	{
	        		params: ps,
		        	formfilter: this.formfilter,
		        	scope: this,
		        	buttons:[{text:'选择',handler:this.selectFromPopup,iconCls:'icon-select',scope: this},
		        	{text:'关闭',handler:function(){Ec.FormManager.close(this.winTarget);},iconCls:'icon-cross',scope: this}]}
	        	);
		    }
        }));
        this.clearbutton = new Ext.Button({
        	text: this.cancelText,
            iconCls: this.cancelIconCls,
            renderTo: this.wrap.child("td.x-select-right", true),
            // cls: 'x-btn-icon',
            scope:this,
            handler: function(){
            	this.setValue([], true, true);
		    }
        });

        if(this.buttonOnly){
            this.el.hide();
            this.wrap.setWidth(this.button.getEl().getWidth());
        }
    },
    disable : function() {
    	this.displayInput.disable();
    	this.button.hide();
    	this.clearbutton.hide();
    },
    enable : function() {
    	this.displayInput.enable();
    	this.button.show();
    	this.clearbutton.show();
    },
    formfilter: function(formObj,opts){
    	if(formObj.xtype
				&& (formObj.xtype.indexOf('ec_grid')>=0
				|| formObj.xtype.indexOf('ec_tree')>=0
				|| formObj.xtype.indexOf('ec_form')>=0))
			{
				grid = formObj;
			}
    	if(!grid)
    	{
	    	var items = formObj.items;
	    	if(!items)
	    		return;
	    	var i;
	    	var grid;

	    	for(i=0; i<items.length; i++){
	    		grid = this.formfilter(items[i], opts);
	    		if(grid)
	    		{
	    			return grid;
	    		}
	    		if(items[i].xtype
	    		&& (items[i].xtype.indexOf('ec_grid')>=0
	    		|| items[i].xtype.indexOf('ec_tree')>=0
	    		|| items[i].xtype.indexOf('ec_form')>=0))
	    		{
	    			grid = items[i];
	    			break;
	    		}
	    	}
    	}
    	if(grid)
    	{
    		delete grid.sm;
    		delete grid.selModel;
    		if(this.multiSelect)
    		{
    			grid.singleSelect = false;
    			grid.checkBox = true;
    		}
    		else
    		{
    			grid.singleSelect = true;
    			grid.checkBox = true;
    		}

    		grid.keepSelectedOnPaging = false;

    		delete grid.id;
    		if(grid.tbar)
    			grid.tbar = [];
    		if(grid.topToolbar)
    			grid.topToolbar = [];
    		delete grid.rowActions;

    		this.winFormName = grid.name;
    		grid.idColName = this.valueField;
    		grid.bodyStyle = 'padding:6px';
    		grid.recordIds = this.getRecords();
    		if(grid != formObj)
    		{
    			delete formObj.items;
    			formObj.items = [];
    			formObj.items.push(grid);
    		}
    		if(opts && opts.buttons)
    		{
    			delete formObj.buttons;
    			formObj.buttons = opts.buttons;
    		}
    		
    		if(!formObj.xPos)
    			formObj.xPos = this.clearbutton.el.getX();
    		if(!formObj.yPos)
    			formObj.yPos = this.clearbutton.el.getY();
    		
    		return formObj;
    	}
    	else if(opts)
    	{
    		alert('no grid found in the page.');
    	}
    },
	selectFromPopup : function(btn,e){
		var c = Ec.FormManager.get(this.winTarget);
		this.values = [];
		if(c)
		{
			var grid = Ec.Utils.find(this.winFormName,c);
			if(grid.length>0)
			{
				if(grid[0].getSelectValue)
				{
					var records = grid[0].getSelectValue();
					var v = [];
					for(var i = 0, len = records.length; i < len; i++){
						var data = {id: records[i][this.valueField], name:records[i][this.displayField||this.valueField]};
						v.push(data);
					}
					// 第二个参数说明是否触发valueselected事件
					/**
					 * 为空判断
					 */
					if(v.toString() == ""){
						Ext.Msg.show({ title:'提示',msg: '请选择记录',icon : Ext.Msg.INFO,buttons: Ext.Msg.OK});
						return ;
					}
					this.setValue(v, true);

					// this.fireEvent('valueselected', this, Ext.encode(v));
			    }
				Ec.FormManager.close(this.winTarget);
			}

		}
    },
    getRecords: function() {
    	var datas = new Array();
		for(i=0;i<this.values.length;i++)
		{
			var data = {};
			data[this.valueField] = this.values[i].id;
			data[this.displayField] = this.values[i].name;
			datas.push(data);
		}
		return datas;
	},
	// 第二个参数是判断是否触发valueselected事件
    setValue : function(value, isFireEvent, isClearEvent) {
		if(Ext.isArray(value))
		{
			this.values = [].concat(value);
    	}else
    	{
			if(typeof value != 'object')
			{
				if(this.values && this.values.length==0)
				{
					value = {id:value, name:value};
					this.values = [];
					this.values.push(value);
				}
			}
			else
			{
				this.values = [];
				this.values.push(value);
			}
		}

		//this.fireEvent('valueselected', this, Ext.encode(value));
		if(isClearEvent) {
			this.fireEvent('valuecleared', this, Ext.encode(this.values));
		} else {
			if (isFireEvent) {
				this.fireEvent('valueselected', this, Ext.encode(this.values));
			} else {
				// 到这里应该是初始化赋值会触发
				// 如果fireSelectOnInit为true，触发valueselected事件
				if (this.fireSelectOnInit) {
					this.fireEvent('valueselected', this, Ext.encode(this.values));
				}
			}
		}
		
		this.refreshDisplay();
		
		this.validate();
	},
	// 用于设置新URL
	setSelecturl :function(newurl) {
		this.selectUrl = newurl;
	},
	refreshDisplay : function(){
		var values = [];
		var names = [];
		var i;
		for(i=0;i<this.values.length;i++)
		{
			values.push(this.values[i].id);
			names.push(this.values[i].name);
		}
		this.el.dom.value = values.join(',');
		this.displayInput.el.dom.value = names.join(',');
	},
	getRawValue: function(){
		return this.getValue();
	},
	getValue: function(){
		var names = [];
		for(i=0;i<this.values.length;i++)
		{
			names.push(this.values[i].name);
		}
		var display = names.join(',');

		// if(this.display != this.displayInput.el.dom.value)
		// return this.displayInput.el.dom.value;
		// else
		return this.el.dom.value;
	},
    onResize : function(w, h){
        Ec.BrowserField.superclass.onResize.call(this, w, h);
        this.wrap.setWidth(w);

        if(!this.buttonOnly){
            var w = this.wrap.getWidth() - this.button.getEl().getWidth() - this.buttonOffset;
            this.el.setWidth(w);
        }
    },
    preFocus : Ext.emptyFn,
    getResizeEl : function(){
        return this.wrap;
    },
    getPositionEl : function(){
        return this.wrap;
    },
    alignErrorIcon : function(){
        this.errorIcon.alignTo(this.wrap, 'tl-tr', [2, 0]);
    }
});
Ext.reg('ec_browserfield', Ec.BrowserField);

Ec.ComboBox = Ext.extend(Ext.form.ComboBox, {
// selectone : false,
// field : 'undefined',
// fields : ['id', 'name'],
// displayField : 'name',
// valueField : 'id'

	editable : false,
	matchWhenLoad : true,

	initComponent : function() {
		if(this.data)
		{
			this.mode = 'local',
			this.fields = this.fields || ['id', 'name'];
			this.displayField = this.displayField || 'name';
			this.valueField = this.valueField || 'id';
			this.store = new Ext.data.SimpleStore({
		        fields: ['id', 'name'],
		        displayField:'name',
		        valueField : 'id',
		        data : this.data
		    });
		}
		else if(this.selectone)
		{
			this.mode = 'remote',
			this.fields = ['id', 'name'];
			this.displayField = 'name';
			this.valueField = 'id';
			this.forceSelection = this.forceSelection || true;
			this.triggerAction = this.triggerAction || 'all';
			this.fireSelectOnInit = this.fireSelectOnInit === undefined ? true : this.fireSelectOnInit;
			this.store = new Ext.data.SimpleStore( {
				url : this.url || (root + '/dictionaryItem.mo?' + Ec.Request.DataEventParams('load', 'dictionary', {key: this.selectone, fieldname:this.name})),
				fields : this.fields
			});
			if(this.params)
				this.store.on('beforeload',this.getReference,this);
		}
		else
		{
			this.fireSelectOnInit = this.fireSelectOnInit === undefined ? true : this.fireSelectOnInit;
			this.store = new Ext.data.JsonStore( {
				url : this.url || '',
				fields : this.fields
			});
		}
		Ec.ComboBox.superclass.initComponent.apply(this, arguments);
	},
	setURI: function(uri, params, ref, target){
		if(!this.selectone)
		{
			this._uri = uri;
			this.store.proxy = new Ext.data.HttpProxy({url: this._uri + '?' + Ec.Request.DataEventParams('load', this.name, params)});
			if(ref)
			{
				this.params = ref;
				this.target = target;
				this.store.on('beforeload',this.getReference,this);
			}
		}
	},
	getReference: function(store, options){
		if(this.params)
		{
			options = options || {};
			options.params = options.params || {};
			for (var i = 0;i < this.params.length; i++) {
				var refField = this.params[i];
				var refParam;
				if(refField.indexOf(':')>0)
				{
					refParam = refField.substr(refField.indexOf(':')+1);
					refField = refField.substr(0,refField.indexOf(':'));
				}

				var fields = Ec.Utils.find(refField,this.target);
				if(fields.length>0)
				{
					for (var j = 0;j < fields.length; j++) {
						options.params[refField] = Ec.Utils.getValueString(fields[j],refParam);// .getValue(refParam);
					}
				}
				else if(refField != '')
				{
					var comp = Ext.getCmp(refField);
					if(comp && comp.getValue)
						options.params[refField] = comp.getValue(refParam);
				}
			}
			if(this.parentComp && this.parentComp.editRecord)
			{
				Ext.apply(options.params, this.parentComp.editRecord.data);
			}
		}
	},
	setValue : function(values) {
		if(values == null)
			return;
		if (values.constructor == Array) {
			var oldValue = this.getValue();
			this.clearValue();
			this.store.loadData(values);
			
			if (!this.matchWhenLoad) {
				this.oldValue = undefined;
			}
			
			var isSet = false;
			for (var i = 0; i < values.length; i++) {
				if (values[i] && values[i].selected) {
					var showValue = this.valueField || this.displayField;
					Ec.ComboBox.superclass.setValue.call(this, values[i][showValue]);
					if(values[i][showValue]!=oldValue)
					{
						var record = this.findRecord(this.valueField, values[i][showValue]);
						if(record && this.fireSelectOnInit)
							this.fireEvent('select', this, record);
					}
					isSet = true;
					this.oldValue = values[i][showValue];
					//this.setValue(record[this.valueField]);
					break;
				}
			}
			if(!isSet)
			{
				//Ec.ComboBox.superclass.setValue.call(this, oldValue);
				if (!this.matchWhenLoad) {
					return;
				}
				
				var record = this.findRecord(this.valueField, oldValue);
				if(record) {
					Ec.ComboBox.superclass.setValue.call(this, oldValue);
					if (this.fireSelectOnInit) {
						this.fireEvent('select', this, record);
					}
					this.oldValue = oldValue;
				} else {
					if (this.fireSelectOnInit) {
						this.fireEvent('select', this, null);
					}
					this.oldValue = undefined;
				}
			}
		} else {
			if (this.mode == 'remote' && this.store.getCount() == 0) {
				this.store.on("load", function() {
					Ec.ComboBox.superclass.setValue.call(this, values);
					var record = this.findRecord(this.valueField, values);
					if(record) {
						if (this.fireSelectOnInit) {
							this.fireEvent('select', this, record);
						}
						this.oldValue = values;
					} else {
						this.oldValue = undefined;
					}
				}, this, {
					single : true
				})
				this.doQuery(this.allQuery, true);
			} else {
				Ec.ComboBox.superclass.setValue.call(this, values);
				this.oldValue = values;
			}
		}
	},
	getSubmitValue: function(){
		var v;
		if(this.forceSelection == false)
		{
			v = this.el.dom.value;
			var index = this.store.find(this.displayField, v);
			if(index != -1)
			{
				v = this.getValue();
			}
			if(v == this.emptyText)
				v = null;
		}
		else
		    v = this.getValue();
		return v;
	},
	onSelect : function(A, B) {
		if (this.fireEvent("beforeselect", this, A, B) !== false) {
			var value = A.data[this.valueField || this.displayField]
			var fireSelect = false;
			if (this.oldValue !== value) {
				fireSelect = true;
			}
			this.setValue(value);
			this.collapse();
			if (fireSelect) {
				this.fireEvent("select", this, A, B);
			}
			this.oldValue = value;
		}
	},
	afterRender : function(){
        Ext.form.TriggerField.superclass.afterRender.call(this);
        var y;
        if(Ext.isIE && !this.hideTrigger && this.el.getY() != (y = this.trigger.getY())){
            this.el.position();
            this.el.setY(y);
        }
    }
});

Ext.reg('ec_combo', Ec.ComboBox);

Ec.UploadServlet = 'Ec.FileUploadServlet';
Ec.FileField = Ext.extend(Ext.form.Field,{
	maxLength : 16,
	emptyText : 'No files',
	deleteText : 'Delete file',
	reallyWantText : '确实需要删除文件? <b>{0}</b> ?',
	downloadText : 'click download file {0}',
	removeText : 'click delete file {0}',
	addText : 'click add file',
	uploadText : 'Upload file for ',
	selectedClass : 'ec-file-item-selected',
	fileCls : 'file',
	pathext: '*',
	vertical:false,
	downloadable:true,
	fields : [ {
		name : 'fileId',
		mapping : 'fileId'
	}, {
		name : 'fileName',
		mapping : 'text'
	}, {
		name : 'shortName',
		mapping : 'shortName'
	}, {
		name : 'fileCls',
		mapping : 'iconCls'
	}],
	defaultAutoCreate : {
		tag : 'input',
		type : 'hidden'
	},
	getFileCls : function(name) {
		var atmp = name.split('.');
		if (1 === atmp.length) {
			return this.fileCls;
		} else {
			return this.fileCls + '-' + atmp.pop().toLowerCase();
		}
	},
	setValue : function(values) {
		this.path = values;
		this.store.proxy = new Ext.data.HttpProxy({url: Ec.UploadServlet + '?cmd=getdb&path=' + this.path});
		this.view.setStore(this.store);
		this.store.reload();
	},
	getValue : function() {
		return this.path;
	},
	getRemoveQtip : function(values) {
		return String.format(this.removeText, values.fileName);
	},
	getDownloadQtip : function(values) {
		return String.format(this.downloadText, values.fileName);
	},
	initComponent : function() {
		var autoLoad = false;
		if (this.path) {
			autoLoad = true;
		}
		this.store = new Ext.data.JsonStore( {
			autoLoad : autoLoad,
			url : Ec.UploadServlet + '?cmd=getdb&path=' + this.path,
			root : '',
			fields : this.fields
		});

		var viewTpl = '<input type="hidden" name="{[this.scope.name]}" value="{[this.scope.path]}" />'+
				'<tpl for=".">'
				+ '<div class="ec-file-item"' + (this.vertical ? ' style="width:100%;">' : '>')
				+ '<div class="ec-file-icon {fileCls}" >&#160;&#160;&#160;&#160;</div>'
				+ '<div class="ec-file-name" qtip="{[this.scope.getDownloadQtip(values)]}">{shortName}</div>'
				+ (this.downloadable ? '<div id="remove" class="ec-file-delete" qtip="{[this.scope.getRemoveQtip(values)]}">&#160;&#160;&#160;&#160;</div>' : '')
				+ '</div>'
				+ '</tpl>';
		this.view = new Ext.DataView( {
			itemSelector : 'div.ec-file-item',
			store : this.store,
			autoHeight: true,
			selectedClass : this.selectedClass,
			singleSelect : true,
			loadingText : 'loading',
			deferEmptyText : false,
			emptyText : this.emptyText,
			cls : 'ec-file-name',
			tpl : new Ext.XTemplate(
				viewTpl
				, {scope : this}
			),
			listeners : {
				click : {
					scope : this,
					fn : this.onViewClick
				}
			}
		});
		Ec.FileField.superclass.initComponent.call(this,arguments)
	},
	onRender : function(ct, position) {
		Ec.FileField.superclass.onRender.call(this, ct, position);
		this.ct = ct;
		ct.last().remove();
		this.bt = Ext.DomHelper.append(this.ct, '<div class="ec-file-add"' + 'qtip="' + this.addText + '"></div>', true);
		this.bt.on('click', this.onUpload, this);
		if(this.newType)
		{
			this.btn = Ext.DomHelper.append(this.ct, '<div class="ec-file-new"' + 'qtip="新建"></div>', true);
			this.btn.on('click', this.onNewDoc, this);
		}
		Ext.DomHelper.append(this.ct,'<br><div style="width:100%;"></div>',true);
		this.view.render(ct);
	},
	hide : function() {
		this.view.hide();
		this.bt.hide();
	},
	show : function() {
		this.view.show();
		this.bt.show();
	},
	onViewClick : function(view, index, node, e) {
		var t = e.getTarget('div.ec-file-delete');
		var file = this.store.getAt(index);
		var fileName = this.path + '/' + file.get('fileName');
		var fileId = file.get('fileId');
		var shortName = file.get('shortName');
		if (t) {
			Ext.Msg.show( {
				title : this.deleteText,
				msg : String.format(this.reallyWantText, file.get('fileName')),
				icon : Ext.Msg.WARNING,
				buttons : Ext.Msg.YESNO,
				scope : this,
				fn : function(response) {
					if ('yes' !== response) {
						this.getEl().dom.focus();
						return;
					}
					Ext.Ajax.request( {
						url : Ec.UploadServlet,
						success : this.refresh,
						scope : this,
						params : {
							cmd : 'deletedb',
							id : fileId
						}
					});
				}
			});
			return;
		}
		this.downloadFile(fileId, shortName);
	},
	refresh : function() {
		this.store.load();
	},
	onUpload : function(e, node) {
		if(!this.path || this.path=='')
		{
			Ext.MessageBox.alert("系统提示", '需要先保存,以获取路径.');
			return;
		}
		var win = Ext.WindowMgr.get(this.getName());
		if (win) {
			return;
		}
		win = new Ext.Window( {
			id : this.getName()+'_window',
			x : e.getPageX(),
			y : e.getPageY(),
			constrain : true,
			width : 220,
			minWidth : 165,
			height : 220,
			minHeight : 200,
			layout : 'fit',
			border : false,
			closable : true,
			title : this.uploadText + this.fieldLabel,
			iconCls : 'icon-upload',
			items : [{
				xtype : 'uploadpanel',
				buttonsAt : 'tbar',
				id : 'uppanel',
				url : Ec.UploadServlet,
				path : this.path,
				pathext: this.pathext,
				maxFileSize : 1048576,
				enableProgress : false,
				listeners : {
					allfinished : {
						scope : this,
						fn : this.refresh
					}
				}
			 	,singleUpload:this.singleUpload || false
			}]
		});
		win.show();
	},
	onNewDoc : function(e, node) {
		if(!this.path || this.path=='')
		{
			Ext.MessageBox.alert("系统提示", '需要先保存,以获取路径.');
			return;
		}
		if (document.getElementById("fileDown1") == undefined) {
		     var div = document.createElement("div");
		     div.className = "x-hidden";
		     div.id = 'fileDown1';
		     div.innerHTML = "<object id =\"filedownload\"  height ='0' width ='0' classid=\"clsid:f820e117-a6db-4d81-bb45-8259d89b3af0\" ></object>";// CODEBASE=\"" + root + "/pages/dsofra.CAB#version=1,0,0,2\"
		     document.body.appendChild(div);
	    }

		    var fp = new Ext.FormPanel({
		        frame: true,
		        labelWidth: 110,
		        width: 340,
		        bodyStyle: 'padding:0 10px 0;',
		        defaultType: 'radio',  // each item will be a radio button
		        items: [{
		            checked: true,
		            fieldLabel: '可选类型',
		            boxLabel: '.doc',
		            name: 'fav-color',
		            inputValue: '.doc'
		        },{
		            fieldLabel: '',
		            labelSeparator: '',
		            boxLabel: '.vsd',
		            name: 'fav-color',
		            inputValue: '.vsd'
		        },{
		            fieldLabel: '',
		            labelSeparator: '',
		            boxLabel: '.xls',
		            name: 'fav-color',
		            inputValue: '.xls'
		        }],
		        buttons: [{
		            text: '选择',
		            scope: this,
		            handler: function(){
		          		var selectedType = fp.form.findField("fav-color").getGroupValue();
		          		this.createNewFile(selectedType);
		          		promptWin.close();
		            }
		        }]
	 		});
 	 	//这里需要弹出menu供用户选择是新建何种文档
	    var promptWin = new Ext.Window({
		     id : 'promptForInput',
		     title : "选择要新建的文件类型",
		     maximizable : false,
		     modal : true,
		     width : 350,
		     items:[
		     	fp
		     ]
	    });

		promptWin.show();
		return;
	},
	createNewFile:function(selectedType){

		var htm = "<div>";
		htm += "<object id =\"dsoDocument\" classid=\"clsid:00460182-9E5E-11D5-B7C8-B8269041DD57\"  style=\"HEIGHT: 480px; WIDTH: 680px\"></object>";//CODEBASE=\"" + root + "/pages/dsofra.CAB#version=2,2,1,3\"
	    htm += "</div>";

	    this.newType = selectedType;

	    var shortName = new Date().format("mm-d-H-m-s") + selectedType;
	    var path = this.path + '/' + shortName;
	    var filename = this.getSystemFileTemp() + "\\" + shortName;
	    var pa = this;
	    this.win = new Ext.Window({
		     id : 'dso',
		     title : "新建文档",
		     maximizable : false,
		     modal : true,
		     width : 700,
		     height : 500,
		     html : htm,
		     close: function(){
		     	Ext.Msg.show( {
					title : this.deleteText,
					msg : String.format('确实需要保存文件? <b>{0}</b> ?', shortName),
					icon : Ext.Msg.WARNING,
					buttons : Ext.Msg.YESNO,
					scope : pa,
					fn : function(response) {
						if ('yes' == response) {
							var typeInt = pa.newType==".doc"?"11":(pa.newType==".xls"?"12":pa.newType==".ppt"?"13":"10");
//							alert(typeInt)
							dsoDocument.saveAs(filename,typeInt);
							if(dsoDocument)
				     			dsoDocument.close();

				     		var url = location.href;
							var num = url.lastIndexOf("/");
							if(num>=0)
							{
								url = url.substr(0,num+1);
							}
							var uploadurl = url + Ec.UploadServlet + '?t=ocx&cmd=uploaddb&path=' + path;
							try {
						    	filedownload.UploadFile(filename, uploadurl);
						    	pa.refresh();
						    } catch (e) {
							    Ext.MessageBox.alert("系统提示", "上传文件时出错:" + e.description);
							    return;
						    }
						}
						else
						{
							try{
								if(dsoDocument){
				     				dsoDocument.close();
								}
							}catch(e){

							}
						}
			     		pa.win.destroy();
					}
				});
	    	}
	    });
	    this.win.on('resize',function(panel, w, h) {
		    Ext.get('dsoDocument').setWidth(w-20);
		    Ext.get('dsoDocument').setHeight(h-30);
		});
		this.win.show();
		dsoDocument.CreateNew(this.getType(selectedType));
	}
	,
	getSystemFileTemp: function() {
		var fso;
		var f;
		try{
			fso = new ActiveXObject("Scripting.FileSystemObject");
			f = fso.GetSpecialFolder(2);
		} catch (e) {
		  	Ext.MessageBox.alert("系统提示", e);
		}
		return f;
	},
	downloadFile : function(fileId, shortName) {

		var type = this.getType(shortName);
		if(this.downloadtype==1 && Ext.isIE)
		{
			if (document.getElementById("fileDown1") == undefined) {
			     var div = document.createElement("div");
			     div.className = "x-hidden";
			     div.id = 'fileDown1';
			     //div.innerHTML = "<object id =\"filedownload\"  height ='0' width ='0' classid=\"clsid:f820e117-a6db-4d81-bb45-8259d89b3af0\" CODEBASE=\"dsofra.cab#version=1,0,0,2\"></object>";
			     div.innerHTML = "<object id =\"filedownload\"  height ='0' width ='0' classid=\"clsid:f820e117-a6db-4d81-bb45-8259d89b3af0\" CODEBASE=\"" + root + "/pages/dsofra.CAB#version=1,0,0,2\" ></object>";
			     document.body.appendChild(div);
		    }
		    var url = location.href;
			var num = url.lastIndexOf("/");
			if(num>=0)
			{
				url = url.substr(0,num+1);
			}
		    var downloadurl = url + Ec.UploadServlet + '?cmd=downloaddb&id=' + fileId;

    		var downloadfilename = this.getSystemFileTemp() + "\\" + shortName;
    		var downladresult = false;
			try {
		    	downladresult = filedownload.DownloadFile(downloadurl, downloadfilename);
		    } catch (e) {
			    Ext.MessageBox.alert("系统提示", "下载文件时出错:" + e.description);
			    return;
		    }
		    if(downladresult)
		    {
			    var htm = "<div>";
	//    		htm += "<object id =\"dsoDocument\" classid=\"clsid:00460182-9E5E-11D5-B7C8-B8269041DD57\" CODEBASE=\"dsofra.cab#version=2,2,1,3\" style=\"HEIGHT: 500px; WIDTH: 800px\"></object>";
				htm += "<object id =\"dsoDocument\" classid=\"clsid:00460182-9E5E-11D5-B7C8-B8269041DD57\" CODEBASE=\"" + root + "/pages/dsofra.CAB#version=2,2,1,3\" style=\"HEIGHT: 480px; WIDTH: 680px\"></object>";
			    htm += "</div>";
			    var win = new Ext.Window({
				     id : 'dso',
				     title : "编辑文档",
				     maximizable : false,
				     modal : true,
//				     resizable : false,
//				     draggable : false,
				     width : 700,
				     height : 500,
				     html : htm,
				     close: function(){
				     	Ext.Msg.show( {
							title : this.deleteText,
							msg : String.format('确实需要保存文件? <b>{0}</b> ?', shortName),
							icon : Ext.Msg.WARNING,
							buttons : Ext.Msg.YESNO,
							scope : this,
							fn : function(response) {
								if ('yes' == response) {
									dsoDocument.save();
									if(dsoDocument)
						     			dsoDocument.close();
									var uploadurl = url + Ec.UploadServlet + '?t=ocx&cmd=uploaddb&id=' + fileId;
									try {
								    	filedownload.UploadFile(downloadfilename, uploadurl);
								    } catch (e) {
									    Ext.MessageBox.alert("系统提示", "上传文件时出错:" + e.description);
									    return;
								    }
								}
								else
								{
									if(dsoDocument)
						     			dsoDocument.close();
								}
					     		win.destroy();
							}
						});
			    	}
			    });
			    win.on('resize',function(panel, w, h) {
				    Ext.get('dsoDocument').setWidth(w-20);
				    Ext.get('dsoDocument').setHeight(h-30);
				});
				win.show();
				dsoDocument.open(downloadfilename, false, type ,"","");
		    }
			return;
		}else if(this.downloadtype==2 && Ext.isIE){
			try{
				this.showPicInWindow(fileId);
			}catch(e){
				alert(e.message);
			}
			return;
		}

		// create hidden target iframe
		var id = Ext.id();
		var frame = document.createElement('iframe');
		frame.id = id;
		frame.name = id;
		frame.className = 'x-hidden';
		if (Ext.isIE) {
			frame.src = Ext.SSL_SECURE_URL;
		}

		document.body.appendChild(frame);

		if (Ext.isIE) {
			document.frames[id].name = id;
		}

		var form = Ext.DomHelper.append(document.body, {
			tag : 'form',
			method : 'post',
			action : Ec.UploadServlet,
			target : id
		});

		document.body.appendChild(form);

		var hidden;

		// append cmd to form
		hidden = document.createElement('input');
		hidden.type = 'hidden';
		hidden.name = 'cmd';
		hidden.value = 'downloaddb';
		form.appendChild(hidden);

		// append path to form
		hidden = document.createElement('input');
		hidden.type = 'hidden';
		hidden.name = 'id';
		hidden.value = fileId;
		form.appendChild(hidden);

		var callback = function() {
			Ext.EventManager.removeListener(frame, 'load', callback, this);
			setTimeout(function() {
				document.body.removeChild(form);
			}, 100);
			setTimeout(function() {
				document.body.removeChild(frame);
			}, 110);
		};
		Ext.EventManager.on(frame, 'load', callback, this);
		form.submit();
	},
	getType : function(path){
	    if(path.indexOf(".xls")>=0)
	    {
	     	return "Excel.Sheet";
	    }
		else if(path.indexOf(".ppt")>=0)
		{
	      return "PowerPoint.Show";
		}
		else if(path.indexOf(".vsd")>=0)
		{
	      return "Visio.Drawing";
		}
		else if(path.indexOf(".doc")>=0)
		{
	      return "Word.Document";
		}
	},
	showPicInWindow:function(fileId){
		var url = location.href;
		var num = url.lastIndexOf("/");
		if(num>=0){
			url = url.substr(0,num+1);
		}
		var downloadurl = url + Ec.UploadServlet + '?cmd=downloaddb&id=' + fileId;
		//alert(downloadurl);
		var win = new Ext.Window( {
			id : fileId+'_window',
			//x : e.getPageX(),
			//y : e.getPageY(),
			constrain : true,
			width : 450,
			minWidth : 165,
			height : 350,
			minHeight : 200,
			layout : 'fit',
			border : false,
			closable : true,
			//title : this.uploadText + this.fieldLabel,
			title:'',
			iconCls : 'icon-upload',
			items : [
				new Ext.Panel({html:'<img src="' + downloadurl + '"></img>'})
			]
		});
		win.show();
	}
});
Ext.reg('ec_filefield', Ec.FileField);

Ec.HiddenField = Ext.extend(Ext.form.Hidden, {
	updatefrequent : false,
	updatetime     : undefined,

	initComponent  : function(){
        Ec.HiddenField.superclass.initComponent.call(this);
    	this.addEvents(
            'refreshvalue'
        );
	},
	afterRender : function(){
        Ec.HiddenField.superclass.afterRender.apply(this, arguments);
		if(this.updatefrequent)
        {
         	this.updatetimer = Ext.TaskMgr.start({
	            interval: this.updatetime || 2000, //runs every 1 sec
	            run: function() {
	            	var options = {};
	            	options.lastx = this.value;
	            	this.fireEvent('refreshvalue', this, options);
	            }.createDelegate(this)
	        });
         }
    },
	setValue : function(value) {
		this.value = value;
	},
	getRawValue : function(value) {
		return this.value;
	}
});

Ext.reg('ec_hiddenfield', Ec.HiddenField);

Ec.FieldSet = Ext.extend(Ext.form.FieldSet, {
	setValue: function(value){
		if(this.checkbox)
		{
			if(value == false)
				this.collapse();
			else
				this.expand();
		}
	},
	getValue : function(value) {
		if(this.checkbox)
			return this.checkbox.dom.checked;
	}
	,onHide: function(){
		Ec.FieldSet.superclass.onHide.call(this);
		//this.el.hide();
	}
});

Ext.reg('ec_fieldset', Ec.FieldSet);

Ec.RadioGroup = Ext.extend(Ext.form.RadioGroup, {
	getName : function(){
		return this.name;
	},
	setValue : function(value) {
		if(this.items)
		{
			this.items.each(function(f) {
				f.setValue(value);
			});
		}
	}
});

Ext.reg('ec_radiogroup', Ec.RadioGroup);

Ext.apply(Ext.layout.FormLayout.prototype, {

	renderItem : function(c, position, target){
			if(c && !c.rendered && c.isFormField && c.inputType != 'hidden'){
					var args = [
								 c.id, c.fieldLabel,
								 c.labelStyle||this.labelStyle||'',
								 this.elementStyle||'',
								 typeof c.labelSeparator == 'undefined' ? this.labelSeparator : c.labelSeparator,
								 (c.itemCls||this.container.itemCls||'') + (c.hideLabel ? ' x-hide-label' : ''),
								 c.clearCls || 'x-form-clear-left'
					];
					if(typeof position == 'number'){
							position = target.dom.childNodes[position] || null;
					}
					if(position){
							this.fieldTpl.insertBefore(position, args);
					}else{
							this.fieldTpl.append(target, args);
					}
					var fEl = Ext.get('x-form-el-'+c.id);
					c.render(fEl);
					if (c.prefix) { fEl.insertHtml('afterBegin',  c.prefix+'&nbsp;'); }
					if (c.suffix) { fEl.insertHtml('beforeEnd', '&nbsp;'+c.suffix); }
			}else {
					Ext.layout.FormLayout.superclass.renderItem.apply(this, arguments);
			}
	}

});

// some extensions
Ec.SimpleCombo = Ext.extend(Ext.form.ComboBox, {
    mode           : 'local',
    triggerAction  : 'all',
    typeAhead      : true,
    valueField     : 'value',
    displayField   : 'name',
    forceSelection : true,
    editable       : false,
		minChars       : 0,
    initComponent  : function(){
        Ext.form.ComboBox.superclass.initComponent.call(this);
        if(!this.store && this.data){
            this.store = new Ext.data.SimpleStore({
                fields: ['value','name','cls'],
                data : this.data
            });
        }
		this.tpl = '<tpl for="."><div class="x-combo-list-item {cls}">{' + this.displayField + '}</div></tpl>';
    },
	getRawValue: function(){
		return this.getValue();
	}
});
Ext.reg('ec_simplecombo', Ec.SimpleCombo);

Ec.PostalCodeField = Ext.extend(Ext.form.TextField, {
    maskRe         : /[0-9]/,
		acceptDep      : false,
		width          : 50,

		initComponent  : function() {
			if (this.acceptDep) {
				this.regex = /^\d{2}$|^\d{5}$/;
			} else {
				this.regex = /^\d{5}$/;
			}
		}
});
Ext.reg('ec_postalcodefield', Ec.PostalCodeField);

Ec.StaticText = Ext.extend(Ext.BoxComponent, {
	encode: false,
    onRender : function(ct, position){
        if(!this.el){
            this.el = document.createElement('div');
            this.el.id = this.getId();
            this.el.innerHTML = this.text ? Ext.util.Format.htmlEncode(this.text) : (this.html || '');
        }
        Ec.StaticText.superclass.onRender.call(this, ct, position);
    },
    setText: function(t, encode){
        this.text = t;
        if(this.rendered){
            this.el.dom.innerHTML = encode !== false ? Ext.util.Format.htmlEncode(t) : t;
        }
        return this;
    }
});
Ext.reg('ec_statictext', Ec.StaticText);

Ec.MultiCheckbox = Ext.extend(Ext.form.Field, {
	columns:1,
	typetag : 'checkbox',
	blankText : "You must select at least one item in this group",
	data : [], // ['a','Aaa'],['b','Bbb']
	allowBlank: true,
	initComponent  : function(){
        Ec.MultiCheckbox.superclass.initComponent.call(this);
    	this.addEvents(
            'checkchange'
        );
	},
	onRender : function(ct, position){
		this.el = ct.createChild({tag:'div',cls:'x-form-field ',id:this.id||Ext.id()});
		this.genNode();
	},
	genNode: function(){
		var first = this.el.first();
		if(first) first.remove();

		this.el.dom.value = [];
		var m = ['<table cellspacing="0" class="noboder" style="border:0px;" width="100%">',
                '<tr align="left">'];
        var el = document.createElement("div");
        var dataLength = this.data.length ;
        for (var i=0;i<dataLength;i++) {
        	if(i%this.columns == 0 && i > 0){
        		m.push('</tr><tr align="left">');
        	}
			var id = Ext.id();
			m.push('<td align="left">');
			m.push("<label style='font-size:12px; white-space:nowrap; text-align:left;width:100%;'>" );
			m.push("<input type='" + this.typetag + "' id='" + id + "' name='" + this.name + "' value='" + this.data[i][0] + "'/>");
			m.push(this.data[i][1] + "</label>");
			m.push('</td>');
		}
		m.push("</tr></table>");
		el.innerHTML = m.join("");
		this.el.insertFirst(el);
		this.el.select('input').each(function(i) {
			// 增加checked属性
			i.dom.setAttribute('checked', false);
			i.dom.checked = false;
			i.on({'click' : {
				        fn: this.onClick,
				        scope: this,
				        delay: 100
				    }
			});
		},this);
		if(this.value != undefined)
			this.setValue(this.value);
	},
	getName : function(){
		return this.name;
	},
	onClick : function(e, v){
		var values = this.getValue();
		var value = v.checked;
		// 将状态赋给dom的属性，可以在移动位置后保持选中状态
		//v.attributes['checked'].nodeValue = value;

		// 如果是单选，先把所有的checked去掉
		if (this.typetag == 'radio') {
			this.el.select('input').each(function(node) {
				if (node.dom.id != v.id) {
					node.dom.attributes['checked'].nodeValue = false;
					node.dom.setAttribute('checked', false);
				}
			});
		}
		
		if (v.attributes['checked']) {
			v.attributes['checked'].nodeValue = true;
		} else {
			v.setAttribute('checked', value);
		}
		var target = v.value;
		this.fireEvent('checkchange', this, values, target, value);
	},
	getValue : function() {
		var value = [];
		this.el.select('input').each(function(i) {
			if (i.dom.checked) {
				value.push(i.dom.value);
			}
		});
		if(this.typetag == 'radio')
		{
			if(value.length>0)
				return value[0];
			else
				return null;
		}
		else
			return value;
	},
	getRawValue : function() {
		return this.getValue();
	},
	setValue : function(value) {
		if(value.data)
		{
			this.data = value.data;
			this.genNode();
			value = value.value;
		}
		if (value != undefined) {
			 if(!(value instanceof Array))
			 {
				var vs = [];
				vs.push(value);
				value = vs;
			 }
		} else {
			value = [];
		}
		var sel = this.el;
		
		this.el.select('input').each(function(node) {
			node.dom.attributes['checked'].nodeValue = false;
			node.dom.setAttribute('checked', false);
		});
		
		this.el.select('input').each(function(node) {
				for(var i=0; i<value.length;i++)
				{
					if(value[i] == node.dom.value)
					{
						node.dom.checked = true;
						// 将状态赋给dom的属性，可以在移动位置后保持选中状态
						if (node.dom.attributes['checked']) {
							node.dom.attributes['checked'].nodeValue = true;
						} else {
							node.dom.setAttribute('checked', true);
						}
					}
				}
		});
	},
	setEnable : function(items, value) {
		if (items != undefined) {
			 if(!(items instanceof Array))
			 {
				var vs = [];
				vs.push(items);
				items = vs;
			 }
		}
		this.el.select('input').each(function(node) {
				for(var i=0; i<items.length;i++)
				{
					if(items[i] == node.dom.value)
					{
						node.dom.disabled = value;
						break;
					}
				}
			});
	},
	setRawValue : function(value) {
		this.setValue(value);
	},
	validateValue : function(A) {
		if (!this.allowBlank) {
			var B = false;

			var v = this.getValue();
			if(this.typetag == 'radio') {
				if (v === null) {
					B = true;
				}
			} else {
				if (v.length == 0) {
					B = true;
				}
			}

			if (B) {
				this.markInvalid(this.blankText);
				return false
			}
		}
		return true
	}
});
Ext.reg('ec_checkboxgroup', Ec.MultiCheckbox);

Ec.FileUploadField = Ext.extend(Ext.form.TextField,  {
    buttonText: 'Browse...',
    buttonOnly: false,
    buttonOffset: 3,

    // private
    readOnly: true,

    autoSize: Ext.emptyFn,

    // private
    initComponent: function(){
        Ec.FileUploadField.superclass.initComponent.call(this);
        this.addEvents(
            'fileselected'
        );
    },

    // private
    onRender : function(ct, position){
        Ec.FileUploadField.superclass.onRender.call(this, ct, position);

        this.wrap = this.el.wrap({cls:'x-form-field-wrap x-form-file-wrap'});
        this.el.addClass('x-form-file-text');
        this.el.dom.removeAttribute('name');

        this.fileInput = this.wrap.createChild({
            id: this.getFileInputId(),
            name: this.name||this.getId(),
            cls: 'x-form-file',
            tag: 'input',
            type: 'file',
            size: 1
        });

        var btnCfg = Ext.applyIf(this.buttonCfg || {}, {
            text: this.buttonText
        });
        this.button = new Ext.Button(Ext.apply(btnCfg, {
            renderTo: this.wrap,
            cls: 'x-form-file-btn' + (btnCfg.iconCls ? ' x-btn-icon' : '')
        }));

        if(this.buttonOnly){
            this.el.hide();
            this.wrap.setWidth(this.button.getEl().getWidth());
        }

        this.fileInput.on('change', function(){
            var v = this.fileInput.dom.value;
            this.setValue(v);
            this.fireEvent('fileselected', this, v);
        }, this);
    },

    // private
    getFileInputId: function(){
        return this.id+'-file';
    },

    // private
    onResize : function(w, h){
        Ec.FileUploadField.superclass.onResize.call(this, w, h);

        this.wrap.setWidth(w);

        if(!this.buttonOnly){
            var w = this.wrap.getWidth() - this.button.getEl().getWidth() - this.buttonOffset;
            this.el.setWidth(w);
        }
    },

    // private
    preFocus : Ext.emptyFn,

    // private
    getResizeEl : function(){
        return this.wrap;
    },

    // private
    getPositionEl : function(){
        return this.wrap;
    },

    // private
    alignErrorIcon : function(){
        this.errorIcon.alignTo(this.wrap, 'tl-tr', [2, 0]);
    }

});
Ext.reg('ec_fileuploadfield', Ec.FileUploadField);

Ec.IFrameComponent = Ext.extend(Ext.BoxComponent, {

	initComponent : function() {
		if(this.url != "")
		{
			if (this.url.indexOf("?") < 0) {
				this.url += "?";
			} else {
				this.url += "&";
			}
			this.url += "parentFrameId=" + this.id;
		}
		Ec.IFrameComponent.superclass.initComponent.apply(this, arguments);
	},

    url : "",

    onRender : function(ct, position){
        var url = this.url;
        // url += (url.indexOf('?') != -1 ? '&' : '?') + '_dc=' + (new
		// Date().getTime());
        if(url != "")
        	this.el = ct.createChild({tag: 'iframe', id: this.id, name: this.id, frameborder: this.frameborder || 0, src: url, allowTransparency: true});
        else
        	this.el = ct.createChild({tag: 'iframe', id: this.id, name: this.id, frameborder: this.frameborder || 0, allowTransparency: true});
        ct.appendChild(this.el);
    },
    setValue: function(v){
    	if(v!='' && this.el)
    		this.el.dom.src = v;
    	else
    		this.url = v;
    }

});
Ext.reg('ec_iframe', Ec.IFrameComponent);

Ec.Label = Ext.extend(Ext.form.Label, {
	isFormField: true,
	getName: function(){
		return this.name;
	},
	setValue: function(value){
		this.setText(value);
	},
	validate: function(){
		return true;
	},
	getRawValue: function(){
		return this.text;
	}
});

Ext.reg('ec_label', Ec.Label);

Ec.Button = Ext.extend(Ext.Button, {
	initComponent : function() {
		if(typeof this.handler == 'string')
		{
			this.handler = eval("(" + this.handler + ")");
		}
		Ec.Button.superclass.initComponent.apply(this, arguments);
	}
});

Ext.reg('ec_button', Ec.Button);

Ec.SelectButton = Ext.extend(Ext.Button, {
	selectUrl: '',
	multiSelect: false,
    buttonText: '',
    iconCls:'icon-open-popup',
    cancelText: '',
    cancelIconCls: 'icon-cross',
    buttonOnly: false,
    buttonOffset: 3,
    windowType:'window',
    panelDiv:'',
    // private
    readOnly: true,
    values:[],

    autoSize: Ext.emptyFn,
    initComponent: function(){
    	this.setHandler( function(){
    		var ps = {};
    		var str = this.selectUrl;
    		var num = str.indexOf("?");
			if(num>=0)
			{
				var parastr = str.substr(num + 1);
				str = str.substring(0,num);
				var arrtmp = parastr.split("&");
				for (i = 0; i < arrtmp.length; i++) {
					num = arrtmp[i].indexOf("=");
					if (num > 0) {
						var name = arrtmp[i].substring(0, num);
						var value = arrtmp[i].substr(num + 1);
						if(value.indexOf('@') != -1) {
							var componentId = value.substring(1);
							var component = Ext.getCmp(componentId);
							if(component) {// 如果组件存在
								value = component.getValue();
							} else {// 不存在则不处理当前值
								continue;
							}
							this.selectUrl = this.selectUrl.replace("@"+componentId,value);
						}
						ps[name] = value;
					}
				}
			}
        	if(this.params)
        	{
        		for (var fieldName in this.params) {
        			var refField = fieldName;
					var refParam;
					if(refField.indexOf(':')>0)
					{
						refParam = refField.substr(refField.indexOf(':')+1);
						refField = refField.substr(0,refField.indexOf(':'));
					}
        			var fields = Ec.Utils.find(refField, this.params[fieldName]);

        			if(fields.length>0)
        				ps[refField] = Ec.Utils.getValueString(fields[0],refParam);
        		}
            }
	        this.winTarget = Ext.id();
    		if(this.windowType == 'panel'){
	        	if(this.panelDiv){
	        		this.winTarget = this.panelDiv;
        		}
	    		Ec.Request.panel(this.winTarget,this.selectUrl,
	        	{
	        		params: ps,
		        	formfilter: this.formfilter,
		        	scope: this,
		        	frame: false,
		        	buttons:[{text:'选择',handler:this.selectFromPopup, scope: this},
		        	{text:'关闭',handler:function(){Ec.FormManager.close(this.winTarget);},scope: this}]}
	        	);
        	}else{
	    		Ec.Request.window(this.winTarget,this.selectUrl,
	        	{
	        		params: ps,
		        	frame: false,
		        	formfilter: this.formfilter,
		        	scope: this,
		        	buttons:[{text:'选择',handler:this.selectFromPopup, scope: this},
		        	{text:'关闭',handler:function(){Ec.FormManager.close(this.winTarget);},scope: this}]}
	        	);
        	}
	    }.createDelegate(this),this);
        Ec.SelectButton.superclass.initComponent.call(this);
        this.addEvents(
            'valueselected'
        );
    },
    formfilter: function(formObj,opts){
    	if(formObj.xtype
				&& (formObj.xtype.indexOf('ec_grid')>=0
				|| formObj.xtype.indexOf('ec_tree')>=0
				|| formObj.xtype.indexOf('ec_form')>=0))
		{
			grid = formObj;
		}
    	if(!grid)
    	{
	    	var items = formObj.items;
	    	if(!items)
	    		return;
	    	var i;
	    	var grid;

	    	for(i=0; i<items.length; i++){
	    		grid = this.formfilter(items[i], opts);
	    		if(grid)
	    		{
	    			return grid;
	    		}
	    		if(items[i].xtype
	    		&& (items[i].xtype.indexOf('ec_grid')>=0
	    		|| items[i].xtype.indexOf('ec_tree')>=0
	    		|| items[i].xtype.indexOf('ec_form')>=0))
	    		{
	    			grid = items[i];
	    			break;
	    		}
	    	}
    	}
    	if(grid)
    	{
    		delete grid.sm;
    		delete grid.selModel;
    		if(this.multiSelect)
    		{
    			grid.singleSelect = false;
    			grid.checkBox = true;
    		}
    		else
    		{
    			grid.singleSelect = true;
    			grid.checkBox = true;
    		}

    		grid.keepSelectedOnPaging = false;

    		delete grid.id;
    		if(grid.tbar)
    			grid.tbar = [];
    		if(grid.topToolbar)
    			grid.topToolbar = [];
    		delete grid.rowActions;
			if(grid.Model)
			{
				for(i=0; i<grid.Model.length; i++){
					delete grid.Model[i].editor;
				}
			}
    		this.winFormName = grid.name;
    		grid.idColName = this.valueField;
//    		grid.bodyStyle = 'padding:6px';
    		if(grid != formObj)
    		{
    			delete formObj.items;
    			formObj.items = [];
    			formObj.items.push(grid);
    		}
    		if(opts && opts.buttons)
    		{
    			delete formObj.buttons;
    			formObj.buttons = opts.buttons;
    		}
    		formObj.recordIds = [];
    		
    		if(!formObj.xPos)
    			formObj.xPos = this.el.getX();
    		if(!formObj.yPos)
    			formObj.yPos = this.el.getY();
    			
    		return formObj;
    	}
    	else if(opts)
    	{
    		alert('no grid found in the page.');
    	}
    },
	selectFromPopup : function(btn,e){
		var c = Ec.FormManager.get(this.winTarget);
		var values = [];
		if(c)
		{
			var grid = Ec.Utils.find(this.winFormName,c);
			if(grid.length>0)
			{
				if(grid[0].getSelectValue)
				{
					var records = grid[0].getSelectValue();
					if (records.length == 0) {
						Ext.Msg.alert('错误', '请选择记录');
						return;
					}
					
					for(var i = 0, len = records.length; i < len; i++){
						// var data = {id: records[i][this.valueField],
						// name:records[i][this.displayField||this.valueField]};
						if(this.valueField)
							values.push(records[i][this.valueField]);
						else
							values.push(records[i]);

					}
			    }
			    delete grid[0].recordIds;
				Ec.FormManager.close(this.winTarget);
			}
			this.fireEvent('valueselected', this, Ext.encode(values));
		}
    }
});

Ext.reg('ec_selectbutton', Ec.SelectButton);

Ec.DateTime = Ext.extend(Ext.form.Field, {
	/**
	 * @cfg {String/Object} defaultAutoCreate DomHelper element spec
	 * Let superclass to create hidden field instead of textbox. Hidden will be submittend to server
	 */
	 defaultAutoCreate:{tag:'input', type:'hidden'}
	/**
	 * @cfg {Number} timeWidth Width of time field in pixels (defaults to 100)
	 */
	,timeWidth:100
	/**
	 * @cfg {String} dtSeparator Date - Time separator. Used to split date and time (defaults to ' ' (space))
	 */
	,dtSeparator:' '
	/**
	 * @cfg {String} hiddenFormat Format of datetime used to store value in hidden field
	 * and submitted to server (defaults to 'Y-m-d H:i:s' that is mysql format)
	 */
	,hiddenFormat:'Y-m-d H:i:s'
	/**
	 * @cfg {Boolean} otherToNow Set other field to now() if not explicly filled in (defaults to true)
	 */
	,otherToNow:true
	/**
	 * @cfg {Boolean} emptyToNow Set field value to now on attempt to set empty value.
	 * If it is true then setValue() sets value of field to current date and time (defaults to false)
	 */
	/**
	 * @cfg {String} timePosition Where the time field should be rendered. 'right' is suitable for forms
	 * and 'below' is suitable if the field is used as the grid editor (defaults to 'right')
	 */
	,timePosition:'right' // valid values:'below', 'right'
	/**
	 * @cfg {String} dateFormat Format of DateField. Can be localized. (defaults to 'm/y/d')
	 */
	,dateFormat:'Y-m-d'
	/**
	 * @cfg {String} timeFormat Format of TimeField. Can be localized. (defaults to 'g:i A')
	 */
	,timeFormat:'H:i:s'
	/**
	 * @cfg {Object} dateConfig Config for DateField constructor.
	 */
	/**
	 * @cfg {Object} timeConfig Config for TimeField constructor.
	 */

	/**
	 * private
	 * creates DateField and TimeField and installs the necessary event handlers
	 */
	,initComponent:function() {
		// call parent initComponent
		Ec.DateTime.superclass.initComponent.call(this);

		// create DateField
		var dateConfig = Ext.apply({}, {
			 id:this.id + '-date'
			,format:this.dateFormat || Ext.form.DateField.prototype.format
			,width:this.timeWidth
			,selectOnFocus:this.selectOnFocus
			,listeners:{
				  blur:{scope:this, fn:this.onBlur}
				 ,focus:{scope:this, fn:this.onFocus}
				 ,change:{scope:this, fn:this.onChange}
			}
		}, this.dateConfig);
		this.df = new Ext.form.DateField(dateConfig);
		this.df.ownerCt = this;
		this.df.afterMethod('setValue', this.updateValue, this);
		delete(this.dateFormat);


		// create TimeField
		var timeConfig = Ext.apply({}, {
			 id:this.id + '-time'
			,format:this.timeFormat || Ext.form.TimeField.prototype.format
			,width:this.timeWidth
			,selectOnFocus:this.selectOnFocus
			,listeners:{
				  blur:{scope:this, fn:this.onBlur}
				 ,focus:{scope:this, fn:this.onFocus}
			}
		}, this.timeConfig);
		this.tf = new Ext.form.TimeField(timeConfig);
		this.tf.ownerCt = this;
		this.tf.afterMethod('setValue', this.updateValue, this);
		delete(this.timeFormat);

		// relay events
		this.relayEvents(this.df, ['focus', 'specialkey', 'invalid', 'valid']);
		this.relayEvents(this.tf, ['focus', 'specialkey', 'invalid', 'valid']);

	}
	,onChange:function() {
	}
	/**
	 * private
	 * Renders underlying DateField and TimeField and provides a workaround for side error icon bug
	 */
	,onRender:function(ct, position) {
		// don't run more than once
		if(this.isRendered) {
			return;
		}

		// render underlying hidden field
		Ec.DateTime.superclass.onRender.call(this, ct, position);

		// render DateField and TimeField
		// create bounding table
		var t;
		if('below' === this.timePosition || 'bellow' === this.timePosition) {
			t = Ext.DomHelper.append(ct, {tag:'table',style:'border-collapse:collapse',children:[
				 {tag:'tr',children:[{tag:'td', style:'padding-bottom:1px', cls:'ux-datetime-date'}]}
				,{tag:'tr',children:[{tag:'td', cls:'ux-datetime-time'}]}
			]}, true);
		}
		else {
			t = Ext.DomHelper.append(ct, {tag:'table',style:'border-collapse:collapse',children:[
				{tag:'tr',children:[
					{tag:'td',style:'padding-right:4px', cls:'ux-datetime-date'},{tag:'td', cls:'ux-datetime-time'}
				]}
			]}, true);
		}

		this.tableEl = t;
//		this.wrap = t.wrap({cls:'x-form-field-wrap'});
		this.wrap = t.wrap();
        this.wrap.on("mousedown", this.onMouseDown, this, {delay:10});

		// render DateField & TimeField
		this.df.render(t.child('td.ux-datetime-date'));
		this.tf.render(t.child('td.ux-datetime-time'));

		// workaround for IE trigger misalignment bug
		if(Ext.isIE && Ext.isStrict) {
			t.select('input').applyStyles({top:0});
		}

		this.on('specialkey', this.onSpecialKey, this);
		this.df.el.swallowEvent(['keydown', 'keypress']);
		this.tf.el.swallowEvent(['keydown', 'keypress']);

		// create icon for side invalid errorIcon
		if('side' === this.msgTarget) {
			var elp = this.el.findParent('.x-form-element', 10, true);
			this.errorIcon = elp.createChild({cls:'x-form-invalid-icon'});

			this.df.errorIcon = this.errorIcon;
			this.tf.errorIcon = this.errorIcon;
		}

		// setup name for submit
		this.el.dom.name = this.hiddenName || this.name || this.id;

		// prevent helper fields from being submitted
		this.df.el.dom.removeAttribute("name");
		this.tf.el.dom.removeAttribute("name");

		// we're rendered flag
		this.isRendered = true;

		// update hidden field
		this.updateHidden();

	}
	/**
	 * private
	 */
    ,adjustSize:Ext.BoxComponent.prototype.adjustSize
	/**
	 * private
	 */
	,alignErrorIcon:function() {
        this.errorIcon.alignTo(this.tableEl, 'tl-tr', [2, 0]);
	}
	/**
	 * private initializes internal dateValue
	 */
	,initDateValue:function() {
		this.dateValue = this.otherToNow ? new Date() : new Date(1970, 0, 1, 0, 0, 0);
	}
	// }}}
	// {{{
    /**
     * Calls clearInvalid on the DateField and TimeField
     */
    ,clearInvalid:function(){
        this.df.clearInvalid();
        this.tf.clearInvalid();
    }
	/**
	 * @private
	 * called from Component::destroy.
	 * Destroys all elements and removes all listeners we've created.
	 */
	,beforeDestroy:function() {
		if(this.isRendered) {
//			this.removeAllListeners();
			this.wrap.removeAllListeners();
			this.wrap.remove();
			this.tableEl.remove();
			this.df.destroy();
			this.tf.destroy();
		}
	}
    /**
     * Disable this component.
     * @return {Ext.Component} this
     */
    ,disable:function() {
		if(this.isRendered) {
			this.df.disabled = this.disabled;
			this.df.onDisable();
			this.tf.onDisable();
		}
		this.disabled = true;
		this.df.disabled = true;
		this.tf.disabled = true;
        this.fireEvent("disable", this);
        return this;
    }
    /**
     * Enable this component.
     * @return {Ext.Component} this
     */
    ,enable:function() {
        if(this.rendered){
			this.df.onEnable();
			this.tf.onEnable();
        }
        this.disabled = false;
		this.df.disabled = false;
		this.tf.disabled = false;
        this.fireEvent("enable", this);
        return this;
    }
	/**
	 * private Focus date filed
	 */
	,focus:function() {
		this.df.focus();
	}
	/**
	 * private
	 */
	,getPositionEl:function() {
		return this.wrap;
	}
	/**
	 * private
	 */
	,getResizeEl:function() {
		return this.wrap;
	}
	/**
	 * @return {Date/String} Returns value of this field
	 */
	,getValue:function() {
		// create new instance of date
		return this.dateValue ? new Date(this.dateValue) : '';
	}
	/**
	 * @return {Boolean} true = valid, false = invalid
	 * private Calls isValid methods of underlying DateField and TimeField and returns the result
	 */
	,isValid:function() {
		return this.df.isValid() && this.tf.isValid();
	}
    /**
     * Returns true if this component is visible
     * @return {boolean}
     */
    ,isVisible : function(){
        return this.df.rendered && this.df.getActionEl().isVisible();
    }
	/**
	 * private Handles blur event
	 */
	,onBlur:function(f) {
		// called by both DateField and TimeField blur events

		// revert focus to previous field if clicked in between
		if(this.wrapClick) {
			f.focus();
			this.wrapClick = false;
		}

		// update underlying value
		if(f === this.df) {
			this.updateDate();
		}
		else {
			this.updateTime();
		}
		this.updateHidden();

		// fire events later
//		(function() {
//			if(!this.df.hasFocus && !this.tf.hasFocus) {

//				alert(this.dateValue);
//				var v = this.getValue();
//				if(String(v) !== String(this.startValue)) {
//					this.fireEvent("change", this, v, this.startValue);
//				}
//				this.hasFocus = false;
//				this.fireEvent('blur', this);
//			}
//		}).call(this);
	}
	/**
	 * private Handles focus event
	 */
	,onFocus:function() {
        if(!this.hasFocus){
            this.hasFocus = true;
            this.startValue = this.getValue();
            this.fireEvent("focus", this);
        }
	}
	/**
	 * private Just to prevent blur event when clicked in the middle of fields
	 */
	,onMouseDown:function(e) {
		if(!this.disabled) {
			this.wrapClick = 'td' === e.target.nodeName.toLowerCase();
		}
	}
	/**
	 * private
	 * Handles Tab and Shift-Tab events
	 */
	,onSpecialKey:function(t, e) {
		var key = e.getKey();
		if(key === e.TAB) {
			if(t === this.df && !e.shiftKey) {
				e.stopEvent();
				this.tf.focus();
			}
			if(t === this.tf && e.shiftKey) {
				e.stopEvent();
				this.df.focus();
			}
		}
		// otherwise it misbehaves in editor grid
		if(key === e.ENTER) {
			this.updateValue();
		}
	}
	/**
	 * private Sets the value of DateField
	 */
	,setDate:function(date) {
		this.df.setValue(date);
	}
	/**
	 * private Sets the value of TimeField
	 */
	,setTime:function(date) {
		this.tf.setValue(date);
	}
	/**
	 * private
	 * Sets correct sizes of underlying DateField and TimeField
	 * With workarounds for IE bugs
	 */
	,setSize:function(w, h) {
		if(!w) {
			return;
		}
		if('below' === this.timePosition) {
			this.df.setSize(w, h);
			this.tf.setSize(w, h);
			if(Ext.isIE) {
				this.df.el.up('td').setWidth(w);
				this.tf.el.up('td').setWidth(w);
			}
		}
		else {
			this.df.setSize(w - this.timeWidth - 4, h);
			this.tf.setSize(this.timeWidth, h);

			if(Ext.isIE) {
				this.df.el.up('td').setWidth(w - this.timeWidth - 4);
				this.tf.el.up('td').setWidth(this.timeWidth);
			}
		}
	}
	/**
	 * @param {Mixed} val Value to set
	 * Sets the value of this field
	 */
	,setValue:function(val) {
		if(!val && true === this.emptyToNow) {
			this.setValue(new Date());
			return;
		}
		else if(!val) {
			this.setDate('');
			this.setTime('');
			this.updateValue();
			return;
		}
        if ('number' === typeof val) {
          val = new Date(val);
        }
		val = val ? val : new Date(1970, 0 ,1, 0, 0, 0);
		var da, time;
		if(val instanceof Date) {
			this.setDate(val);
			this.setTime(val);
			this.dateValue = new Date(val);
		}
		else {
			da = val.split(this.dtSeparator);
			this.setDate(da[0]);
			if(da[1]) {
				if(da[2]) {
					da[1] += da[2];
				}
				this.setTime(da[1]);
			}
		}
		this.updateValue();
	}
	/**
     * Hide or show this component by boolean
     * @return {Ext.Component} this
     */
    ,setVisible: function(visible){
        if(visible) {
            this.df.show();
            this.tf.show();
        }else{
            this.df.hide();
            this.tf.hide();
        }
        return this;
    }
	,show:function() {
		return this.setVisible(true);
	}
	,hide:function() {
		return this.setVisible(false);
	}
	/**
	 * private Updates the date part
	 */
	,updateDate:function() {

		var d = this.df.getValue();
		if(d) {
			if(!(this.dateValue instanceof Date)) {
				this.initDateValue();
				if(!this.tf.getValue()) {
					this.setTime(this.dateValue);
				}
			}
			this.dateValue.setMonth(0); // because of leap years
			this.dateValue.setFullYear(d.getFullYear());
			this.dateValue.setMonth(d.getMonth());
			this.dateValue.setDate(d.getDate());
		}
		else {
			this.dateValue = '';
			//this.setTime('');
		}
	}
	/**
	 * private
	 * Updates the time part
	 */
	,updateTime:function() {
		var t = this.tf.getValue();
		if(t && !(t instanceof Date)) {
			t = Date.parseDate(t, this.tf.format);
		}
		if(t && !this.df.getValue()) {
			this.initDateValue();
			this.setDate(this.dateValue);
		}
		if(this.dateValue instanceof Date) {
			if(t) {
				this.dateValue.setHours(t.getHours());
				this.dateValue.setMinutes(t.getMinutes());
				this.dateValue.setSeconds(t.getSeconds());
			}
			else {
				this.dateValue.setHours(0);
				this.dateValue.setMinutes(0);
				this.dateValue.setSeconds(0);
			}
		}
	}
	/**
	 * private Updates the underlying hidden field value
	 */
	,updateHidden:function() {
		if(this.isRendered) {
			var value = this.dateValue instanceof Date ? this.dateValue.format(this.hiddenFormat) : '';
			this.el.dom.value = value;
		}
	}
	/**
	 * private Updates all of Date, Time and Hidden
	 */
	,updateValue:function() {
//		if(this.validateValues()){
			this.updateDate();
			this.updateTime();
			this.updateHidden();
//		}
		return;
	}
	,validateValues:function(){
		alert(this.width);
		if(this.width == 220){
			this.width = this.width+1;
			this.df.markInvalid('11');
			return false;
		}
//		alert('zheli' + this.width);
		this.clearInvalid();
//		if(this.vtype){
//            var vt = Ext.form.VTypes;
//            alert(vt[this.vtype](value, this));
//            if(!vt[this.vtype](value, this)){
//
//                this.markInvalid(this.vtypeText || vt[this.vtype +'Text']);
//                return false;
//            }
//        }
        return true;
	}
	/**
	 * @return {Boolean} true = valid, false = invalid
	 * callse validate methods of DateField and TimeField
	 */
	,validate:function() {
		return this.df.validate() && this.tf.validate();
	}
	/**
	 * Returns renderer suitable to render this field
	 * @param {Object} Column model config
	 */
	,renderer: function(field) {
		var format = field.editor.dateFormat || Ec.DateTime.prototype.dateFormat;
		format += ' ' + (field.editor.timeFormat || Ec.DateTime.prototype.timeFormat);
		var renderer = function(val) {
			var retval = Ext.util.Format.date(val, format);
			return retval;
		};
		return renderer;
	}
});

Ext.reg('ec_datetime', Ec.DateTime);

Ec.EventHandler = {};

Ec.EventHandler.boxMove = function(com, x, y) {
	Ec.Request.fireFieldEvent(this, {
		x : x,
		y : y
	});
}
Ec.EventHandler.boxResize = function(com, adjWidth, adjHeight, rawWidth,
		rawHeight) {
	Ec.Request.fireFieldEvent(this, {
		adjWidth : adjWidth,
		adjHeight : adjHeight,
		rawWidth : rawWidth,
		rawHeight : rawHeight
	});
}

Ec.HandlerManager.regEventHandler('box', 'move', Ec.EventHandler.boxMove);
Ec.HandlerManager.regEventHandler('box', 'resize',
		Ec.EventHandler.boxResize);

Ec.EventHandler.fieldChange = function(com, newValue, oldValue) {
	Ec.Request.fireFieldEvent(this, {
		newValue : newValue,
		oldValue : oldValue
	});
}
Ec.EventHandler.fieldInvalid = function(com, msg) {
	Ec.Request.fireFieldEvent(this, {
		msg : msg
	});
}
Ec.EventHandler.fieldSpecialkey = function(com, event) {
	Ec.Request.fireFieldEvent(this, {
		key : event.getKey(),
		charCode : event.getCharCode()
	});
}
Ec.HandlerManager.regEventHandler('field', 'change',
		Ec.EventHandler.fieldChange);
Ec.HandlerManager.regEventHandler('field', 'invalid',
		Ec.EventHandler.fieldInvalid);
Ec.HandlerManager.regEventHandler('field', 'specialkey',
		Ec.EventHandler.fieldSpecialkey);

Ec.EventHandler.checkboxCheck = function(com, check) {
	Ec.Request.fireFieldEvent(this, {
		check : check
	});
}
Ec.HandlerManager.regEventHandler('checkbox', 'check',
		Ec.EventHandler.checkboxCheck);

Ec.EventHandler.buttonToggle = function(com, pressed) {
	Ec.Request.fireFieldEvent(this, {
		pressed : pressed
	});
}
Ec.HandlerManager.regEventHandler('button', 'toggle',
		Ec.EventHandler.buttonToggle);

Ec.EventHandler.colorpaletteSelecte = function(com, color) {
	Ec.Request.fireFieldEvent(this, {
		color : color
	});
}
Ec.HandlerManager.regEventHandler('colorpalette', 'select',
		Ec.EventHandler.colorpaletteSelecte);

Ec.EventHandler.datepickerSelecte = function(com, date) {
	Ec.Request.fireFieldEvent(this, {
		date : date
	});
}
Ec.HandlerManager.regEventHandler('datepicker', 'select',
		Ec.EventHandler.datepickerSelecte);

Ec.EventHandler.comboSelecte = function(combo, record, index) {
	var value = null;
	if (record) {
		value = record.get(combo.valueField);
	}

	Ec.Request.fireFieldEvent(this, {
		index : index,
		value : value
	});
}
Ec.HandlerManager.regEventHandler('combo', 'select',
		Ec.EventHandler.comboSelecte);

Ec.EventHandler.treeNodeEvent = function(node) {
	Ec.Request.fireFieldEvent(this, {
		node : node.id,
		nodeText : node.text
	});
}
Ec.EventHandler.treeTextChange = function(node, text, oldText) {
	Ec.Request.fireFieldEvent(this, {
		node : node.id,
		nodeText : text,
		oldText : oldText
	});
}
Ec.EventHandler.treeInsert = function(tree, parent, node, refNode) {
	Ec.Request.fireFieldEvent(this, {
		parent : parent.id,
		parentText : parent.text,
		node : node.id,
		nodeText : node.text,
		refNode : refNode.id
	});
}
Ec.EventHandler.treeAppend = function(tree, parent, node, index) {
	Ec.Request.fireFieldEvent(this, {
		parent : parent.id,
		parentText : parent.text,
		node : node.id,
		nodeText : node.text,
		index : index
	});
}
Ec.EventHandler.treeRemove = function(tree, parent, node) {
	Ec.Request.fireFieldEvent(this, {
		parent : parent.id,
		parentText : parent.text,
		node : node.id,
		nodeText : node.text
	});
}
Ec.EventHandler.treeMoveNode = function(tree, node, oldParent, newParent,
		index) {
	Ec.Request.fireFieldEvent(this, {
		oldParent : oldParent.id,
		oldParentText : oldParent.text,
		newParent : newParent.id,
		newParentText : newParent.text,
		node : node.id,
		nodeText : node.text,
		index : index
	});
}
Ec.EventHandler.treeCheckchange = function(node, check) {
	Ec.Request.fireFieldEvent(this, {
		node : node.id,
		nodeText : node.text,
		check : check,
		parentNode : node.parentNode ? node.parentNode.id : null,
		parentNodeText : node.parentNode ? node.parentNode.text : null
	});
}
Ec.HandlerManager.regEventHandler('treepanel', 'click',
		Ec.EventHandler.treeNodeEvent);
Ec.HandlerManager.regEventHandler('treepanel', 'dblclick',
		Ec.EventHandler.treeNodeEvent);
Ec.HandlerManager.regEventHandler('treepanel', 'textchange',
		Ec.EventHandler.treeTextChange);
Ec.HandlerManager.regEventHandler('treepanel', 'movenode',
		Ec.EventHandler.treeMoveNode);
Ec.HandlerManager.regEventHandler('treepanel', 'append',
		Ec.EventHandler.treeAppend);
Ec.HandlerManager.regEventHandler('treepanel', 'remove',
		Ec.EventHandler.treeRemove);
Ec.HandlerManager.regEventHandler('treepanel', 'insert',
		Ec.EventHandler.treeInsert);
Ec.HandlerManager.regEventHandler('treepanel', 'checkchange',
		Ec.EventHandler.treeCheckchange);

Ec.EventHandler.gridRowClick = function(grid, rowIndex) {
	var record = grid.getStore().getAt(rowIndex); // Get the Record
	Ec.Request.fireFieldEvent(this, {
		rowIndex : rowIndex,
		cursor : grid.getCursor(),
		data: Ext.encode(record.data)
	});
};

Ec.EventHandler.gridCellClick = function(grid, rowIndex, columnIndex, e) {
	var record = grid.getStore().getAt(rowIndex); // Get the Record
	var columnName = grid.getColumnModel().getDataIndex(columnIndex);
	var cellData = record.get(columnName);
	Ec.Request.fireFieldEvent(this, {
		rowIndex : rowIndex,
		columnIndex : columnIndex,
		cursor : grid.getCursor(),
		columnName : columnName,
		cellData : cellData
	});
};

Ec.EventHandler.searchClick = function(grid, value) {
	value._type = Ec.Request.DATA_EVENT;
	Ec.Request.fireFieldEvent(this, value);
};

Ec.HandlerManager.regEventHandler('ec_grid', 'search',
		Ec.EventHandler.searchClick);
Ec.HandlerManager.regEventHandler('grid', 'rowclick',
		Ec.EventHandler.gridRowClick);
Ec.HandlerManager.regEventHandler('grid', 'rowdblclick',
		Ec.EventHandler.gridRowClick);
Ec.HandlerManager.regEventHandler('grid', 'cellclick',
		Ec.EventHandler.gridCellClick);
Ec.HandlerManager.regEventHandler('grid', 'celldblclick',
		Ec.EventHandler.gridCellClick);

Ec.EventHandler.wizardSelected = function(wizard, value, index) {
	Ec.Request.fireFieldEvent(this, {
		index : index,
		action : value.action,
		target : value.target,
		value : value.value,
		values: value.values
	});
}
Ec.HandlerManager.regEventHandler('ec_wizard', 'beforepagechange', Ec.EventHandler.wizardSelected);
Ec.HandlerManager.regEventHandler('ec_wizard', 'finish', Ec.EventHandler.wizardSelected);
Ec.HandlerManager.regEventHandler('ec_wizard', 'close', Ec.EventHandler.wizardSelected);

Ec.EventHandler.ButtonSelected = function(button, value) {
	Ec.Request.fireFieldEvent(this, {
		action : 'selected',
		value : value
	});
}
Ec.HandlerManager.regEventHandler('ec_selectbutton', 'valueselected', Ec.EventHandler.ButtonSelected);

Ec.EventHandler.BrowserSelected = function(button, value) {
	Ec.Request.fireFieldEvent(this, {
		action : 'selected',
		value : value
	});
}
Ec.HandlerManager.regEventHandler('ec_browserfield', 'valueselected', Ec.EventHandler.BrowserSelected);

Ec.EventHandler.FrequencyRefresh = function(chart, value) {
	value = value || {};
	value.action =  'refreshvalue';
	Ec.Request.fireFieldEvent(this, value);
}
Ec.HandlerManager.regEventHandler('ec_hiddenfield', 'refreshvalue', Ec.EventHandler.FrequencyRefresh);

Ec.EventHandler.CheckBoxGroupCheck = function(group, values, target, value) {
	var v = {};
	v.values = Ext.encode(values);
	v.value = target;
	v.action =  'checkchange';
	Ec.Request.fireFieldEvent(this, v);
}
Ec.HandlerManager.regEventHandler('ec_checkboxgroup', 'checkchange', Ec.EventHandler.CheckBoxGroupCheck);

Ec.TableFormLayout = Ext.extend(Ext.layout.ContainerLayout, {
	labelSeparator : ':',

	columns: 1,
    // private
    monitorResize:false,

    // private
    setContainer : function(ct){
        Ec.TableFormLayout.superclass.setContainer.call(this, ct);
        this.columns = ct.columns || this.columns;
        this.currentRow = 0;
        this.currentColumn = 0;
        this.cells = [];
        this.allTD = [],
    	this.allTR = [],

        this.labelAlign = ct.labelAlign || 'right';

        if(ct.labelAlign){
            ct.addClass('x-form-label-'+ct.labelAlign);
        }
        if(ct.hideLabels){
            this.labelStyle = "display:none";
            this.elementStyle = "padding-left:0;";
            this.labelAdjust = 0;
        }else{
            this.labelSeparator = ct.labelSeparator || this.labelSeparator;
            ct.labelWidth = ct.labelWidth || 100;
            if(typeof ct.labelWidth == 'number'){
                this.labelAdjust = ct.labelWidth;
                this.labelStyle = "width:"+ct.labelWidth+"px;";
                this.elementStyle = "padding-left:0";
            }
            ct.labelWidth = ct.labelWidth;
            if(ct.labelAlign == 'top'){
                this.labelStyle = "width:auto;";
                this.labelAdjust = 0;
                this.elementStyle = "padding-left:0;";
            }
        }
        if(!this.fieldTpll){
            // the default field template used by all form layouts
            var tl = new Ext.Template(
                '<label for="{0}" style="{2}" class="x-form-item-label">{1}{4}</label>'
            );
             var te = new Ext.Template(
                '<div class="x-form-item {5}" tabIndex="-1">',
                    '<div class="x-form-element" id="x-form-el-{0}" style="float:left;{3}"></div>{6}<div style="font-weight:bold;color:red;">{7}</div>',
                '</div>'
            );
            tl.disableFormats = true;
            tl.compile();
            te.disableFormats = true;
            te.compile();
            Ec.TableFormLayout.prototype.fieldTpll = tl;
            Ec.TableFormLayout.prototype.fieldTple = te;
        }
    },
    // private
    onLayout : function(ct, target){
        var cs = ct.items.items, len = cs.length, c, i;
        if(!this.table){
            target.addClass('x-table-layout-ct');
            this.table = target.createChild(
                {tag:'table', cls:'x-table-layout', cellspacing: 0, width:'100%', cn: {tag: 'tbody'}}, null, true);

            // 临时隐藏的table，用来放隐藏td的
            this.tempTable = target.createChild(
                {tag:'table', cls:'x-hide-display', cellspacing: 0, width:'100%', cn: {tag: 'tbody'}}, null, true);
            this.tempRow = document.createElement('tr');
            this.tempTable.tBodies[0].appendChild(this.tempRow);
            var tempCell = document.createElement('td');
            this.tempRow.appendChild(tempCell);

            this.renderAll(ct, target);
        }
    },
    // private
    getRow : function(index){
        var row = this.table.tBodies[0].childNodes[index];
        if(!row){
            row = document.createElement('tr');
            this.table.tBodies[0].appendChild(row);

            // 设置行的索引值
            row.indexValue = this.allTR.length;
            this.allTR.push(row);
        }
        return row;
    },
    // private
	getNextCell : function(c){
		var cell = this.getNextNonSpan(this.currentColumn, this.currentRow);
		var curCol = this.currentColumn = cell[0], curRow = this.currentRow = cell[1];
		if(c.fieldLabel!=null && c.fieldLabel!='')
		{
			var colspan = (c.colspan || 1)*2-1;
			for(var rowIndex = curRow; rowIndex < curRow + (c.rowspan || 1); rowIndex++){
				if(!this.cells[rowIndex]){
					this.cells[rowIndex] = [];
				}
				for(var colIndex = curCol; colIndex < curCol + colspan; colIndex++){
					this.cells[rowIndex][colIndex] = true;
				}
			}
			var tdl = document.createElement('td');
			var clsl = 'x-table-layout-label';
			if(c.cellCls){
				clsl += ' ' + c.cellCls;
			}
			tdl.className = clsl;
			if(this.labelAdjust)
				tdl.style.width = this.labelAdjust;
			if(this.labelAlign)
				tdl.align = this.labelAlign;

			var td = document.createElement('td');
			if(c.cellId){
				td.id = c.cellId;
			}
			var cls = 'x-table-layout-cell';
			if(c.cellCls){
				cls += ' ' + c.cellCls;
			}
			td.className = cls;
			if(c.colspan){
				td.colSpan = colspan;
			}

			if(c.rowspan){
				td.rowSpan = c.rowspan;
			}
			this.getRow(curRow).appendChild(tdl);
			this.getRow(curRow).appendChild(td);

			// 设置td的索引值
			tdl.indexValue = this.allTD.length;
			this.allTD.push(tdl);
			td.indexValue = this.allTD.length;
			this.allTD.push(td);

			return [tdl,td];
		}
		else
		{
			for(var rowIndex = curRow; rowIndex < curRow + (c.rowspan || 1); rowIndex++){
				if(!this.cells[rowIndex]){
					this.cells[rowIndex] = [];
				}
				for(var colIndex = curCol; colIndex < curCol + (c.colspan || 1); colIndex++){
					this.cells[rowIndex][colIndex] = true;
				}
			}

			var td = document.createElement('td');
			if(c.cellId){
				td.id = c.cellId;
			}
			var cls = 'x-table-layout-cell';
			if(c.cellCls){
				cls += ' ' + c.cellCls;
			}
			td.className = cls;
			var colspan = c.colspan || 1;
			if(c.colspan){
				td.colSpan = colspan;
			}
			if(c.rowspan){
				td.rowSpan = c.rowspan;
			}
			this.getRow(curRow).appendChild(td);
			return td;
		}
	},
    // private
	getNextNonSpan: function(colIndex, rowIndex){
		var cols = this.columns;
		while((cols && colIndex >= cols) || (this.cells[rowIndex] && this.cells[rowIndex][colIndex])) {
			if(cols && colIndex >= cols){
				rowIndex++;
				colIndex = 0;
			}else{
				colIndex++;
			}
		}
		return [colIndex, rowIndex];
	},
	hideItem: function(c){
		if(!c)
			return;
    	if(c.ownerDom)
    	{
    		var p = Ext.get(c.ownerDom);
    		p.enableDisplayMode();
    		p.hide();

    		var tr = Ext.get(c.ownerDom.parentNode.parentNode);
    		tr.enableDisplayMode();

    		var labeltd = Ext.get(c.ownerDom.parentNode.previousSibling);

    		var label = labeltd.select(".x-form-item-label", false);
    		for(var i=0; i<label.elements.length; i++)
    		{
    			Ext.get(label.elements[i]).hide();
    		}
    		/*
    		var childs = tr.select("td .x-form-item", false);

    		var shouldhide = true;
    		for(var i=0; i<childs.elements.length; i++)
    		{

    			if(!(childs.elements[i].style["visibility"] == "hidden" || childs.elements[i].style["display"] == "none"))
    			{
    				shouldhide = false;
    				break;
    			}
    		}
    		if(shouldhide)
    		{
    			//Ext.get(tr).hide();
    		}
    		*/
    		if (true) {
    			// 每行的size
				var rowSize = this.columns * 2;

        		var tr = c.ownerDom.parentNode.parentNode;
				var labelTD = c.ownerDom.parentNode.previousSibling;
				var selfTD = c.ownerDom.parentNode;

				if (labelTD.showFlag == false) {
					// 说明已经隐藏了，不管了
					return;
				}
				// 设置这两个td为隐藏
				labelTD.showFlag = false;
				selfTD.showFlag = false;
				// 将这两个TD移到临时表去
				tr.removeChild(labelTD);
				tr.removeChild(selfTD);
				this.tempRow.appendChild(labelTD);
				this.tempRow.appendChild(selfTD);

				// 将后面的TD都往前挪
				var nextLabelTD;
				var nextItemTD;

				// spanSize是看一共占多少的TD
				var spanSize = 0;
				if (labelTD.attributes.colSpan) {
					spanSize += parseInt(labelTD.attributes.colSpan.nodeValue);
				} else {
					spanSize += 1;
				}
				if (selfTD.attributes.colSpan) {
					spanSize +=  parseInt(selfTD.attributes.colSpan.nodeValue);
				} else {
					spanSize += 1;
				}
				var currentTR = tr;
				var nextSpanSize = 0; // 下一行空地大小

				var tempTDArray = [];
				for (var k = selfTD.indexValue + 1; k < this.allTD.length; k++) {
					if (this.allTD[k].showFlag) {
						tempTDArray.push(this.allTD[k]);
					}
				}

				for (var i = 0; i < tempTDArray.length - 1;) {
					// 没空位退出
					if (spanSize == 0 && nextSpanSize == 0) {
						break;
					}

					nextLabelTD = tempTDArray[i];
					nextItemTD = tempTDArray[i+1];

					// 如果与隐藏td是同行的，不处理，因为会自动提前
					var tmpTR = nextLabelTD.parentNode;
					if (currentTR == tmpTR) {
						i += 2;
						continue;
					}

					// 非同行的如果空位够，将后面的TD挪到前面来
					var labelSize = nextLabelTD.attributes.colSpan ? parseInt(nextLabelTD.attributes.colSpan.nodeValue) : 1;
					var itemSize = nextItemTD.attributes.colSpan ? parseInt(nextItemTD.attributes.colSpan.nodeValue) : 1;
					if (spanSize >= labelSize + itemSize) {
						
						tmpTR.removeChild(nextLabelTD);
						tmpTR.removeChild(nextItemTD);
						currentTR.appendChild(nextLabelTD);
						currentTR.appendChild(nextItemTD);
						
						var inputs = Ext.get(nextItemTD).select("input");
						if (inputs) {
							inputs.each(function(node) {
								if (node.dom.type == 'text') {
									if(Ext.isIE) {
										if (node.dom.getAttribute("readonly") == true) {
											node.repaint();
										}
									}
								}
							});
						}
						
						// 挪了之后空位减少
						spanSize = spanSize - labelSize - itemSize;
						i += 2;

						nextSpanSize = labelSize + itemSize;
					} else {

						// 将空出来的设为空td

						/*var needFill = rowSize - spanSize;
						if (needFill > 0) {
							for (var k = 0; k < needFill; k++) {
								var td = document.createElement("TD");
								td.innerHTML = '&nbsp';
								td.isTempFill = true;
								currentTR.appendChild(td);
							}
						}*/


						// 如果空位不够，并且下一行也有空位了，换行，没有空位就结束了
						if (nextSpanSize == 0) {
							break;
						}

						if (currentTR.indexValue < this.allTR.length - 1) {
							currentTR = this.allTR[currentTR.indexValue + 1];
							/*while (!currentTR.hasChildNodes()) {
								if (currentTR.indexValue < this.allTR.length - 1) {
									currentTR = this.allTR[currentTR.indexValue + 1];
								} else {
									currentTR = null;
									break;
								}
							}*/

							if (currentTR == null) {
								break;
							}
							spanSize = nextSpanSize;
						} else {
							break;
						}
					}
				}
    		}
    	}
    },
    showItem: function(c){
    	if(!c)
    		return;
    	if(c.ownerDom)
    	{
    		var p = Ext.get(c.ownerDom);
    		p.show();

    		var labeltd = Ext.get(c.ownerDom.parentNode.previousSibling);
    		var label = labeltd.select(".x-form-item-label", false);
    		for(var i=0; i<label.elements.length; i++)
    		{
    			Ext.get(label.elements[i]).show();
    		}

    		var tr = Ext.get(c.ownerDom.parentNode.parentNode);
    		// tr.enableDisplayMode();
    		/*
    		var childs = tr.select(".x-form-item", false);

    		var shouldshow = false;
    		for(var i=0; i<childs.elements.length; i++)
    		{
    			if(!childs.elements[i].isVisible || !childs.elements[i].isVisible())
    			{
    				shouldshow = true;
    				break;
    			}
    		}
    		if(shouldshow)
    		{
    			Ext.get(tr).show();
    		}
    		*/
    		//var itemtd = Ext.get(c.ownerDom.parentNode);
    		//labeltd.show();
    		//itemtd.show();

    		if (true) {
    			var tr = c.ownerDom.parentNode.parentNode;
				var labelTD = c.ownerDom.parentNode.previousSibling;
				var selfTD = c.ownerDom.parentNode;

				if (labelTD.showFlag == true) {
					// 说明已经显示了，不管了
					return;
				}

				// 每行的size
				var rowSize = this.columns * 2;
				// 要将这两个TD插到自己的位置

				var preTD;
				var nextTD;

				// 找到td前没隐藏的td
				for (var i = labelTD.indexValue-1; i >= 0; i--) {
					if (this.allTD[i].showFlag == true) {
						preTD = this.allTD[i];
						break;
					}
				}

				// 找到td后没隐藏的td
				for (var i = labelTD.indexValue + 1; i < this.allTD.length; i++) {
					if (this.allTD[i].showFlag == true) {
						nextTD = this.allTD[i];
						break;
					}
				}

				var labelSize = labelTD.attributes.colSpan ? parseInt(labelTD.attributes.colSpan.nodeValue) : 1;
				var itemSize = selfTD.attributes.colSpan ? parseInt(selfTD.attributes.colSpan.nodeValue) : 1;
				var needSize = labelSize + itemSize;

				labelTD.showFlag = true;
				selfTD.showFlag = true;

				// 先判断前面td所在的tr还有没有位置
				if (preTD) {
					var preTR = preTD.parentNode;
					if (preTR.hasChildNodes()) {
						var size = 0;
						for (var j = 0; j < preTR.childNodes.length; j++) {
							var tmpTD = preTR.childNodes[j];
							var tmpSize = tmpTD.attributes.colSpan ? parseInt(tmpTD.attributes.colSpan.nodeValue) : 1;
							size += tmpSize;
						}

						if (rowSize - size >= needSize) {
							// 如果剩余空间够放置
							// 将这两个TD从临时表移除
							this.tempRow.removeChild(labelTD);
							this.tempRow.removeChild(selfTD);
							preTR.appendChild(labelTD);
							preTR.appendChild(selfTD);

							// 返回不继续执行
							return;
						}
					}
				}

				// 前面TD所在的行没地方了，只能放在后面行了
				if (nextTD) {
					var nextTR = nextTD.parentNode;
					var needMoveTD = [];
					needMoveTD.push(labelTD);
					needMoveTD.push(selfTD);

					while (nextTR && needMoveTD.length > 0) {
					// 直接插在后面TD之前，再判断TR的长度是否超出，超出就继续往后挪
						if (nextTD) {
							for (var k = 0; k < needMoveTD.length; k++) {
								nextTR.insertBefore(needMoveTD[k], nextTD);
							}
						} else {
							for (var k = 0; k < needMoveTD.length; k++) {
								nextTR.appendChild(needMoveTD[k]);
							}
						}
						//nextTR.insertBefore(labelTD, nextTD);
						//nextTR.insertBefore(selfTD, nextTD);

						// 计算TR中现在的长度是否超出
						var tds = nextTR.childNodes;
						var size = 0;
						var i = 0;
						for (i = 0; i < tds.length;) {
							var tmpTD = tds[i];
							var tmpLabelSize = tmpTD.attributes.colSpan ? parseInt(tmpTD.attributes.colSpan.nodeValue) : 1;
							var tmpItem = tds[i+1];
							var tmpItemSize = tmpItem.attributes.colSpan ? parseInt(tmpItem.attributes.colSpan.nodeValue) : 1;

							size += tmpLabelSize + tmpItemSize;

							if (size > rowSize) {
								break;
							}
							i += 2;
						}

						if (size <= rowSize) {
							// 说明TR长度每超出，应该是发生在最后一行
							return;
						}

						// 将超出的TD全往下一行挪
						needMoveTD = [];
						for (;i < tds.length; i++) {
							needMoveTD.push(tds[i]);
						}

						nextTR = nextTR.nextSibling;
						if (nextTR) {
							nextTD = nextTR.firstChild;
						}
					}
				} else {
					var nextTR;
					if (preTD) {
						nextTR = preTD.parentNode.nextSibling;
					} else {
						nextTR = this.allTR[0];
					}
					this.tempRow.removeChild(labelTD);
					this.tempRow.removeChild(selfTD);
					nextTR.appendChild(labelTD);
					nextTR.appendChild(selfTD);
				}
    		}
    	}
    },
    // private
    renderItem : function(c, position, target){
        if(c && !c.rendered && c.inputType != 'hidden'){
        	if(c.fieldLabel!=null && c.fieldLabel!='')
        	{
	        	target = this.getNextCell(c);

	        	target[0].showFlag = true;
	        	target[1].showFlag = true;

	        	if(c.align)
	        		target[1].align = c.align;
	        	var args = [
	                   c.id, c.fieldLabel,
	                   c.labelStyle||this.labelStyle||'',
	                   this.elementStyle||'',
	                   typeof c.labelSeparator == 'undefined' ? this.labelSeparator : c.labelSeparator,
	                   (c.itemCls||this.container.itemCls||'') + (c.hideLabel ? ' x-hide-label' : ''),
	                   c.postLabel?'<div style="float:left;vertical-align:bottom"><span style="vertical-align:bottom">'+c.postLabel+'</span></div>':'', c.allowBlank==false?'*':''
	            ];
	            var dl = this.fieldTpll.append(target[0], args);
	            var d = this.fieldTple.append(target[1], args);
	            c.render('x-form-el-'+c.id);
	            c.ownerDom = d;
	            c.on('hide',this.hideItem, this);
		        c.on('show',this.showItem,this);
        	}
        	else if(c.inTable)
        	{

        		target = this.getNextCell(c);

	        	target.showFlag = true;

	        	if(c.align)
	        		target.align = c.align;
	        	var args = [
	                   c.id, '',
	                   '',
	                   this.elementStyle||'',
	                   '',
	                   (c.itemCls||this.container.itemCls||'') + (c.hideLabel ? ' x-hide-label' : ''),
	                   c.postLabel?'<div style="float:left;">'+c.postLabel+'</div>':'', c.allowBlank==false?'*':''
	            ];
	            var d = this.fieldTple.append(target, args);
	            c.render('x-form-el-'+c.id);
	            c.ownerDom = d;
	            c.on('hide',this.hideItem, this);
		        c.on('show',this.showItem,this);
        	}
        	else
        	{
        		Ec.TableFormLayout.superclass.renderItem.apply(this, arguments);
        	}
        }
        else if(c.hidden)
        {

        }
        else
        {
        	c.render(this.table.parentNode);
        }

    },
    // private
    isValidParent : function(c, target){
        return true;
    }
});

Ext.Container.LAYOUTS['tableform'] = Ec.TableFormLayout;

Ext.grid.GroupSummary = function(config){
    Ext.apply(this, config);
};

Ext.extend(Ext.grid.GroupSummary, Ext.util.Observable, {
    init : function(grid){
        this.grid = grid;
        this.cm = grid.getColumnModel();
        this.view = grid.getView();

        var v = this.view;
        v.doGroupEnd = this.doGroupEnd.createDelegate(this);

        v.afterMethod('onColumnWidthUpdated', this.doWidth, this);
        v.afterMethod('onAllColumnWidthsUpdated', this.doAllWidths, this);
        v.afterMethod('onColumnHiddenUpdated', this.doHidden, this);
        v.afterMethod('onUpdate', this.doUpdate, this);
        v.afterMethod('onRemove', this.doRemove, this);

        if(!this.rowTpl){
            this.rowTpl = new Ext.Template(
                '<div class="x-grid3-summary-row" style="{tstyle}">',
                '<table class="x-grid3-summary-table" border="0" cellspacing="0" cellpadding="0" style="{tstyle}">',
                    '<tbody><tr>{cells}</tr></tbody>',
                '</table></div>'
            );
            this.rowTpl.disableFormats = true;
        }
        this.rowTpl.compile();

        if(!this.cellTpl){
            this.cellTpl = new Ext.Template(
                '<td class="x-grid3-col x-grid3-cell x-grid3-td-{id} {css}" style="{style}">',
                '<div class="x-grid3-cell-inner x-grid3-col-{id}" unselectable="on">{value}</div>',
                "</td>"
            );
            this.cellTpl.disableFormats = true;
        }
        this.cellTpl.compile();
    },

    toggleSummaries : function(visible){
        var el = this.grid.getGridEl();
        if(el){
            if(visible === undefined){
                visible = el.hasClass('x-grid-hide-summary');
            }
            el[visible ? 'removeClass' : 'addClass']('x-grid-hide-summary');
        }
    },

    renderSummary : function(o, cs){
        cs = cs || this.view.getColumnData();
        var cfg = this.cm.config;

        var buf = [], c, p = {}, cf, last = cs.length-1;
        for(var i = 0, len = cs.length; i < len; i++){
            c = cs[i];
            cf = cfg[i];
            p.id = c.id;
            p.style = c.style;
            p.css = i == 0 ? 'x-grid3-cell-first ' : (i == last ? 'x-grid3-cell-last ' : '');
            if(cf.summaryType || cf.summaryRenderer){
                p.value = (cf.summaryRenderer || c.renderer)(o.data[c.name], p, o);
            }else{
                p.value = '';
            }
            if(p.value == undefined || p.value === "") p.value = "&#160;";
            buf[buf.length] = this.cellTpl.apply(p);
        }

        return this.rowTpl.apply({
            tstyle: 'width:'+this.view.getTotalWidth()+';',
            cells: buf.join('')
        });
    },

    calculate : function(rs, cs){
        var data = {}, r, c, cfg = this.cm.config, cf;
        for(var j = 0, jlen = rs.length; j < jlen; j++){
            r = rs[j];
            for(var i = 0, len = cs.length; i < len; i++){
                c = cs[i];
                cf = cfg[i];
                if(cf.summaryType){
                    data[c.name] = Ext.grid.GroupSummary.Calculations[cf.summaryType](data[c.name] || 0, r, c.name, data);
                }
            }
        }
        return data;
    },

    doGroupEnd : function(buf, g, cs, ds, colCount){
        var data = this.calculate(g.rs, cs);
        buf.push('</div>', this.renderSummary({data: data}, cs), '</div>');
    },

    doWidth : function(col, w, tw){
        var gs = this.view.getGroups(), s;
        for(var i = 0, len = gs.length; i < len; i++){
            s = gs[i].childNodes[2];
            s.style.width = tw;
            s.firstChild.style.width = tw;
            s.firstChild.rows[0].childNodes[col].style.width = w;
        }
    },

    doAllWidths : function(ws, tw){
        var gs = this.view.getGroups(), s, cells, wlen = ws.length;
        for(var i = 0, len = gs.length; i < len; i++){
            s = gs[i].childNodes[2];
            s.style.width = tw;
            s.firstChild.style.width = tw;
            cells = s.firstChild.rows[0].childNodes;
            for(var j = 0; j < wlen; j++){
                cells[j].style.width = ws[j];
            }
        }
    },

    doHidden : function(col, hidden, tw){
        var gs = this.view.getGroups(), s, display = hidden ? 'none' : '';
        for(var i = 0, len = gs.length; i < len; i++){
            s = gs[i].childNodes[2];
            s.style.width = tw;
            s.firstChild.style.width = tw;
            s.firstChild.rows[0].childNodes[col].style.display = display;
        }
    },

    // Note: requires that all (or the first) record in the
    // group share the same group value. Returns false if the group
    // could not be found.
    refreshSummary : function(groupValue){
        return this.refreshSummaryById(this.view.getGroupId(groupValue));
    },

    getSummaryNode : function(gid){
        var g = Ext.fly(gid, '_gsummary');
        if(g){
            return g.down('.x-grid3-summary-row', true);
        }
        return null;
    },

    refreshSummaryById : function(gid){
        var g = document.getElementById(gid);
        if(!g){
            return false;
        }
        var rs = [];
        this.grid.store.each(function(r){
            if(r._groupId == gid){
                rs[rs.length] = r;
            }
        });
        var cs = this.view.getColumnData();
        var data = this.calculate(rs, cs);
        var markup = this.renderSummary({data: data}, cs);

        var existing = this.getSummaryNode(gid);
        if(existing){
            g.removeChild(existing);
        }
        Ext.DomHelper.append(g, markup);
        return true;
    },

    doUpdate : function(ds, record){
        this.refreshSummaryById(record._groupId);
    },

    doRemove : function(ds, record, index, isUpdate){
        if(!isUpdate){
            this.refreshSummaryById(record._groupId);
        }
    },

    showSummaryMsg : function(groupValue, msg){
        var gid = this.view.getGroupId(groupValue);
        var node = this.getSummaryNode(gid);
        if(node){
            node.innerHTML = '<div class="x-grid3-summary-msg">' + msg + '</div>';
        }
    }
});

Ext.grid.GroupSummary.Calculations = {
    'sum' : function(v, record, field){
        return v + parseInt((record.data[field]||0));
    },

    'count' : function(v, record, field, data){
        return data[field+'count'] ? ++data[field+'count'] : (data[field+'count'] = 1);
    },

    'max' : function(v, record, field, data){
        var v = record.data[field];
        var max = data[field+'max'] === undefined ? (data[field+'max'] = v) : data[field+'max'];
        return v > max ? (data[field+'max'] = v) : max;
    },

    'min' : function(v, record, field, data){
        var v = record.data[field];
        var min = data[field+'min'] === undefined ? (data[field+'min'] = v) : data[field+'min'];
        return v < min ? (data[field+'min'] = v) : min;
    },

    'average' : function(v, record, field, data){
        var c = data[field+'count'] ? ++data[field+'count'] : (data[field+'count'] = 1);
        var t = (data[field+'total'] = ((data[field+'total']||0) + (record.data[field]||0)));
        return t === 0 ? 0 : t / c;
    }
}

Ext.grid.HybridSummary = Ext.extend(Ext.grid.GroupSummary, {
    calculate : function(rs, cs){
        var gcol = this.view.getGroupField();
        var gvalue = rs[0].data[gcol];
        var gdata = this.getSummaryData(gvalue);
        return gdata || Ext.grid.HybridSummary.superclass.calculate.call(this, rs, cs);
    },

    updateSummaryData : function(groupValue, data, skipRefresh){
        var json = this.grid.store.reader.jsonData;
        if(!json.summaryData){
            json.summaryData = {};
        }
        json.summaryData[groupValue] = data;
        if(!skipRefresh){
            this.refreshSummary(groupValue);
        }
    },

    getSummaryData : function(groupValue){
        var json = this.grid.store.reader.jsonData;
        if(json && json.summaryData){
            return json.summaryData[groupValue];
        }
        return null;
    }
});

Ec.GroupGrid = Ext.extend(Ec.Grid, {

	enableGroupingMenu : false,
	hideGroupedColumn : false,
	groupTextTpl: '{text} ({[values.rs.length]} {[values.rs.length > 1 ? "Items" : "Item"]})',
	
	initModel: function(){
		
		Ec.GroupGrid.superclass.initModel.apply(this);
		
		 this.view = new Ext.grid.GroupingView({
	        forceFit:true,
	        groupTextTpl: this.groupTextTpl,
	        hideGroupedColumn : this.hideGroupedColumn,
	        enableGroupingMenu : this.enableGroupingMenu
	     });
		 this.plugins = [new Ext.grid.GroupSummary()];

         this.cm.defaultSortable = true;
         // create a jsonStore
         this.store = new Ext.data.GroupingStore({
             proxy: new Ext.data.HttpProxy({
                 url: '',
                 method: 'post'
             }),
             reader:new Ec.ArrayReader({
                 totalProperty: 'totalCount',
                 root: 'list'
             },
             Ext.data.Record.create(this.gRecord)
             ),
             sortInfo: this.sortInfo,
             groupField: this.groupField
         });
         this.store.grid = this;
         this.pagingConfig.store = this.store;
     }
});

Ext.reg('ec_groupgrid', Ec.GroupGrid);


Ec.EventHandler.MoveNode = function(tree, node, oldParent, newParent, index) {
	Ec.Request.fireFieldEvent(this, {
		node : node,
		oldParent : oldParent,
		newParent : newParent,
		index : index
	});
}

Ec.HandlerManager.regEventHandler('ec_tree', 'aftermovenode', Ec.EventHandler.MoveNode);

Ec.EventHandler.SelectRow = function(grid, v) {
	Ec.Request.fireFieldEvent(this, {
		value : v
	});
}

Ec.HandlerManager.regEventHandler('ec_grid', 'selectrow', Ec.EventHandler.SelectRow);

Ec.RenderableTextArea = Ext.extend(Ext.form.TextArea, {
	originalValue : '',
	initComponent : function() {
		this.on('disable', function(comp) {
			comp.el.dom.value = comp.valueDecode(comp.originalValue);
		}, this);
		Ec.RenderableTextArea.superclass.initComponent.apply(this, arguments);
	},
	valueDecode : function(value) {
		if (this.disabled) {
			value = value.replace("@#_", "\n                                                                 ");
			value = value.replace("@#_", " ");
		} else {
			var index = value.indexOf("@#_");
			if (index != -1) {
				value = value.substring(0, index);
			}
		}
		return value;
	},
	valueEncode : function(value) {
		return value;
	},
	setValue : function(value) {
		this.originalValue = value;
		this.el.dom.value = this.valueDecode(value);
	},
	getValue : function() {
		if (this.disabled) {
			return this.originalValue;
		} else {
			var retV = Ec.RenderableTextArea.superclass.getValue.apply(this, arguments);
			return this.valueEncode(retV);
		}
	},
	getRawValue : function() {
		if (this.disabled) {
			return this.originalValue;
		} else {
			var retV = Ec.RenderableTextArea.superclass.getValue.apply(this, arguments);
			return this.valueEncode(retV);
		}
	}
});

Ext.reg('ec_renderabletextarea', Ec.RenderableTextArea);

Ec.Myimg = Ext.extend(Ext.BoxComponent, {
    onRender : function(ct, position){
        if(!this.el){
            this.el = document.createElement('img');
           	this.el.src = this.src || Ext.BLANK_IMAGE_URL;
        }
        Ec.Myimg.superclass.onRender.call(this, ct, position);
        this.setValue(this);
    },
    setValue : function(v){
    	if(typeof v == 'string')
    		this.el.dom.src = v;
    	else
    	{
    		if(v.hrefLink)
    		{
    			if(!this.refLink)
    			{
    				this.refLink = this.container.createChild({
		                tag: 'a'
		            });
    			}
    			this.container.appendChild(this.refLink);
    			this.refLink.dom.href = v.hrefLink;
    			this.refLink.appendChild(this.el);
    		}
    		if(this.el.dom)
    		{
	    		if(v.src)
	    		{
	    			this.el.dom.src = v.src;
	    		}
	    		if(v.width)
	    		{
	    			this.el.setWidth(v.width);
	    		}
	    		if(v.height)
	    		{
	    			this.el.setHeight(v.height);
	    		}
    		}
    	}
    }
});

Ext.reg('ec_img', Ec.Myimg);


Ext.apply(Ext.form.VTypes, {
	passconfirm : function(val, textfield) {
		if (textfield.confirmTo) {
			return val == Ext.get(textfield.confirmTo).getValue();
		}
		return true;
	},
	nowTimeV : function(val, datefield){
		alert(1);
		if(datefield.compareTo){
			var other = Ext.get(datefield.compareTo).getValue();
			alert(other);
		}
	},
	dateGT : function(val, datefield) {
		if (datefield.compareTo) {
			var other = Ext.get(datefield.compareTo).getValue();
			if (other) {
				if (val >= Ext.get(datefield.compareTo).getValue()) {
					Ext.getCmp(datefield.compareTo).clearInvalid();
					return true;
				} else {
					return false;
				}
			}
		}
		Ext.getCmp(datefield.compareTo).clearInvalid();
		return true;
	},
	dateLT : function(val, datefield) {
		if (datefield.compareTo) {
			var other = Ext.get(datefield.compareTo).getValue();
			if (other) {
				if (val <= Ext.get(datefield.compareTo).getValue()) {
					Ext.getCmp(datefield.compareTo).clearInvalid();
					return true;
				} else {
					return false;
				}
			}
		}
		Ext.getCmp(datefield.compareTo).clearInvalid();
		return true;
	},
    IPAddress:  function(v) {
        return /^\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}$/.test(v);
    },
    //简体中文
    chinese: function(v){
        var r =  /^[\u0391-\uFFE5]+$/;
        return r.test(v);
    },
    //非中文
    noChinese: function(v)
    {
        var r = /^^[\u0391-\uFFE5]+$/;
        return r.test(v);
    },
	chineseText:'无效的中文字符',
	noChineseText:'不能输入中文字符',
    IPAddressText: '无效的IP地址！',
    //日期格式校验
	VerifyDate: function(v){
		var ft = /^(?:(?:(?:(?:(?:1[6-9]|[2-9]\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00)))(\/|-)(?:0?2\1(?:29)))|(?:(?:(?:1[6-9]|[2-9]\d)?\d{2})(\/|-)(?:(?:(?:0?[13578]|1[02])\2(?:31))|(?:(?:0?[1,3-9]|1[0-2])\2(29|30))|(?:(?:0?[1-9])|(?:1[0-2]))\2(?:0?[1-9]|1\d|2[0-8]))))$/.test(v);
		var st = /^([0-9]{4})(-|\/)([0-9]{2})(-|\/)([0-9]{2})$/.test(v);
		return ft&st;
	},
	VerifyDateText:'日期格式必须为yyyy-mm-dd'

});


/**
 * 下载文件
 * @param {} url	下载文件的路径
 * @param {} params 下载时所带的参数
 */
function downloadFile(url, params) {

	// create hidden target iframe
	var id = Ext.id();
	var frame = document.createElement('iframe');
	frame.id = id;
	frame.name = id;
	frame.className = 'x-hidden';
	if (Ext.isIE) {
		frame.src = Ext.SSL_SECURE_URL;
	}

	document.body.appendChild(frame);

	if (Ext.isIE) {
		document.frames[id].name = id;
	}

	var form = Ext.DomHelper.append(document.body, {
		tag : 'form',
		method : 'post',
		action : url,
		target : id
	});

	document.body.appendChild(form);

	var hidden;

	if (params) {
		for (var k in params) {
			hidden = document.createElement('input');
			hidden.type = 'hidden';
			hidden.name = k;
			hidden.value = params[k];
			form.appendChild(hidden);
		}
	}

	// append cmd to form
	hidden = document.createElement('input');
	hidden.type = 'hidden';
	hidden.name = 'cmd';
	hidden.value = 'download';
	form.appendChild(hidden);


	var callback = function() {
		Ext.EventManager.removeListener(frame, 'load', callback, this);
		setTimeout(function() {
			document.body.removeChild(form);
		}, 100);
		setTimeout(function() {
			document.body.removeChild(frame);
		}, 110);
	};
	Ext.EventManager.on(frame, 'load', callback, this);
	form.submit();
}


function usMoney(v, ccyCode, scale, needRound) {
	var base = 1;

	// 默认保留2位小数
	if (!scale) {
		scale = 2;
	}
	
	if (!needRound) {
		v = parseFloat(v);
		// 找到小数点的索引值
		v = (v + '');
		var index = v.indexOf('.');
		if (index == -1) {
			// 整数的话后面直接补0
			var s = ".";
			for (var i = 0; i < scale; i++) {
				s += "0";
			}
			v = v + s;
		} else {
			// 小数的话看是需要补还是需要截取
			var vlength = v.length;
			if (index + scale < vlength - 1) {
				// 需要截取
				v = v.substring(0, index + scale + 1);
			} else {
				// 需要补充
				for (var i = 0; i < index + scale - vlength + 1; i++) {
					v = v + '0';
				}
			}
		}
	} else {
		for (var i = 0; i < scale; i++) {
			base = base * 10;
		}
	
		v = (Math.round((v - 0) * base)) / base;
		
		// 如果v是整数，后面补上scale个0
		if (v == Math.floor(v)) {
			var s = ".";
			for (var i = 0; i < scale; i++) {
				s += "0";
			}
			v = v + s;
		} else {
			var b = 1;
			for (var i = 1; i < scale; i++) {
				b = b * 10;
				if (Math.abs((v * b) - (Math.floor(v * b))) < 0.000000001) {
					var s = "";
					for (var j = i; j < scale; j++) {
						s += "0";
					}
					v = v + s;
					break;
				}
			}
		}
	}

	v = String(v);
	var ps = v.split(".");
	var whole = ps[0];
	var sub = ps[1] ? "." + ps[1] : ".0000";
	var r = /(\d+)(\d{3})/;
	while (r.test(whole)) {
		whole = whole.replace(r, "$1" + "," + "$2");
	}
	v = whole + sub;

	var ret = v;

	if (v.charAt(0) == "-") {
		ret = "-" + v.substr(1);
	}
	
	if (ccyCode) {
		ret = ccyCode + " " + ret;
	}

	return ret;
	
	/*
	

	//v = (v == Math.floor(v)) ? v + ".00" : ((v * 10 == Math.floor(v
	//		* 10)) ? v + "0" : v);

	v = String(v);
	var ps = v.split(".");
	var whole = ps[0];
	var sub = ps[1] ? "." + ps[1] : ".0000";
	var r = /(\d+)(\d{3})/;
	while (r.test(whole)) {
		whole = whole.replace(r, "$1" + "," + "$2");
	}
	v = whole + sub;

	var ret = v;

	if (v.charAt(0) == "-") {
		ret = "-" + v.substr(1);
	}
	*/
	
}

function usMoneyRound(v, ccyCode, scale) {
	var base = 1;

	// 默认保留2位小数
	if (!scale) {
		scale = 2;
	}
	
	for (var i = 0; i < scale; i++) {
		base = base * 10;
	}

	v = (Math.round((v - 0) * base)) / base;
	
	// 如果v是整数，后面补上scale个0
	if (v == Math.floor(v)) {
		var s = ".";
		for (var i = 0; i < scale; i++) {
			s += "0";
		}
		v = v + s;
	} else {
		var b = 1;
		for (var i = 1; i < scale; i++) {
			b = b * 10;
			if (Math.abs((v * b) - (Math.floor(v * b))) < 0.000000001) {
				var s = "";
				for (var j = i; j < scale; j++) {
					s += "0";
				}
				v = v + s;
				break;
			}
		}
	}
	

	v = String(v);
	var ps = v.split(".");
	var whole = ps[0];
	var sub = ps[1] ? "." + ps[1] : ".0000";
	var r = /(\d+)(\d{3})/;
	while (r.test(whole)) {
		whole = whole.replace(r, "$1" + "," + "$2");
	}
	v = whole + sub;

	var ret = v;

	if (v.charAt(0) == "-") {
		ret = "-" + v.substr(1);
	}
	
	if (ccyCode) {
		ret = ccyCode + " " + ret;
	}

	return ret;
}

Ec.MoneyField = Ext.extend(Ext.form.TextField, {
	setUsMoney : true,
	style : 'text-align:right',
	letterRe : /^[a-zA-Z]+$/g,
	defaultValue : 0,
	msgAlert : false,

	initComponent  : function(){
        Ec.MoneyField.superclass.initComponent.call(this);

        if (this.ccyCode) {
        	// 币种必须是字母
        	var s = this.ccyCode.replace(this.letterRe,"");
        	if (s.length > 0) {
        		this.ccyCode = undefined;
        	}
        }

    	this.on('blur', function(field) {
			var v = this.el.getValue();
			if (!v) {
				v = this.defaultValue + '';
			}
			//v = v.replace(/[^0-9\.]/g, "");
			
			v = v.toNumber();
			
			if (v) {
				var splits = ("" + v).split('.');
				if (splits && splits.length > 0) {
					if (splits[0].length > 12) {
						v = 0;
//						Ext.Msg.alert("错误", "整数位长度不能超过12位");
						Ext.Msg.show({title:'提示',msg:'整数位长度不能超过12位!',icon: Ext.Msg.INFO,buttons: Ext.Msg.OK});
					}
				}
			}
		
			this.setValue(v);
			
			if (this.afterBlur && typeof this.afterBlur == 'function') {
				this.afterBlur();
			}
			
		}, this);

	},

	setValue : function(value) {
		if (!value) {
			value = this.defaultValue + '';
		} else {
			value = value + '';
		}

		// 过滤非数字
		//value = value.replace(/[^0-9\.]/g, "");
		value = value.toNumber();
		
		if (this.setUsMoney) {
			Ec.MoneyField.superclass.setValue.call(this, usMoney(value, this.ccyCode, this.scale));
		} else {
			Ec.MoneyField.superclass.setValue.call(this, value);
		}
	},
	getRawValue : function() {
		var v =  Ec.MoneyField.superclass.getRawValue.call(this);

		if (v) {
			//v = v.replace(/[^0-9\.]/g, "");
			v = v.toNumber();
		}

		return v;
	},
	getValue : function() {
		var v = Ec.MoneyField.superclass.getValue.call(this);
		if (v) {
			//v = v.replace(/[^0-9\.]/g, "");
			v = v.toNumber();
			
			if (v) {
				var splits = ("" + v).split('.');
				if (splits && splits.length > 0) {
					if (splits[0].length > 12) {
						v = 0;
					}
				}
			}
		}
		return v;
	},

	markInvalid : function(msg){
        Ec.MoneyField.superclass.markInvalid.call(this, msg);
    },
    
    validateValue : function(value) {
		return Ec.MoneyField.superclass.validateValue.call(this, value);
	}
    
});

Ext.reg('ec_moneyfield', Ec.MoneyField);


Ec.PercentField = Ext.extend(Ext.form.TextField, {
	decimalPrecision : 2,
	maskRe : /[\d\.]+/,
	allowNegative : false,
	minValue : Number.NEGATIVE_INFINITY,
	maxValue : 100,
	allowZero : true,
	defaultNull : false,
	style : 'text-align:right;',
	
	initComponent  : function(){
		
		if (this.allowNegative) {
			this.maskRe =  /[\d\.-]+/;
		}
		
		if (this.defaultValue !== undefined) {
			this.value = this.defaultValue;
			this.emptyText = this.defaultValue;
		}
		
		Ec.PercentField.superclass.initComponent.call(this);
		
		this.on('blur', function(field) {
			var ov = this.el.getValue();
			
			if (!ov) {
				ov = this.defaultValue + '';
			}
			//v = v.replace(/[^0-9\.]/g, "");
			
			ov = ov.toNumber();
			
			ov = usMoney(ov);
			
			this.validateRateValue(ov);
				//var v = this.getValue();
				//v = parseFloat((parseFloat(v)*100)).toFixed(2);
			Ec.PercentField.superclass.setValue.call(this, ov);
		});
		
		
	},

	setValue : function(value) {
		if ( this.defaultNull == true && (!value && value!=0 || value == '') ) {
			this.el.dom.value = '';
			this.defaultValue = undefined;
			return;
		} else if (!value) {
			this.el.dom.value = '0.00';
			this.defaultValue = undefined;
			return;
		}
		
		if (value === this.emptyText && value === this.defaultValue) {
			this.el.dom.value = value;
			this.defaultValue = undefined;
			return;
		}
		if (isNaN(value)) {
			Ec.PercentField.superclass.setValue.call(this, value);
		} else {
			var v = parseFloat((parseFloat(value)*100)).toFixed(2);
			Ec.PercentField.superclass.setValue.call(this, v);
		}
		
		if (this.el.dom.value === this.emptyText) {
			return;
		}

	},
	getRawValue : function() {
		var v = this.el.getValue();
		if (!v) {
			v = 0;
		}
		if (v) {
			//v = v.replace(/[^0-9\.]/g, "");
			v = v.toNumber();
		}
		
		if (!isNaN(v)) {
			v = parseFloat((parseFloat(v)/100)).toFixed(4);
		} else {
			v = 0;
		}
		return v;
	},
	getValue : function() {
		var v = this.el.getValue();
		
		if (!v) {
			v = 0;
		} else {
			//v = (v + '').replace(/[^0-9\.]/g, "");
			v = (v + '').toNumber();
		}
		
		if (!isNaN(v)) {
			v = parseFloat((parseFloat(v)/100)).toFixed(4);
		} else {
			v = 0;
		}
		return v;
	},
	validateRateValue : function(value){
		
		if (value) {
        	//value = value.replace(/[^0-9\.]/g, "");
			value = value.toNumber();
        }
		
        if(isNaN(value)){
            this.markInvalid(String.format("{0} 不是有效的数字", value));
            return false;
        }
        var num = parseFloat(value);
        if(num < this.minValue){
            this.markInvalid(String.format("输入数字不能小于 {0}", this.minValue));
            return false;
        }
        if(num > this.maxValue){
            this.markInvalid(String.format("输入数字不能大于 {0}", this.maxValue));
            return false;
        }
        return true;
    },
    validateValue : function(value){
        if(!Ec.PercentField.superclass.validateValue.call(this, value)){
            return false;
        }
        
        if (value) {
        	//value = value.replace(/[^0-9\.]/g, "");
        	value = value.toNumber();
        }
        
        if(value.length < 1){ // if it's blank and textfield didn't flag it then it's valid
             return true;
        }
        
        if(isNaN(value)){
            this.markInvalid(String.format("{0} 不是有效的数字", value));
            return false;
        }
        
        if (!this.allowZero && parseFloat(value) === 0) {
        	this.markInvalid(String.format("0不是有效的数字", value));
            return false;
        }
        
        var num = parseFloat(value) * 100;
        
        if(num < this.minValue){
            this.markInvalid(String.format("输入数字不能小于 {0}", this.minValue));
            return false;
        }
        if(num > this.maxValue){
            this.markInvalid(String.format("输入数字不能大于 {0}", this.maxValue));
            return false;
        }
        return true;
    }
});

Ext.reg('ec_percentfield', Ec.PercentField);


// edi  EdiPercentField  not format it   .
Ec.EdiPercentField = Ext.extend(Ext.form.TextField, {
	decimalPrecision : 0,
	maskRe : /[\d]+/,
	allowNegative : false,
	minValue : 0,
	maxValue : 100,
	postLabel : '%', 
	allowBlank : true,
	style : 'text-align:right',
	initComponent  : function(){
		if (this.allowNegative) {
			this.maskRe =  /[\d\-]+/;
		}
		Ec.EdiPercentField.superclass.initComponent.call(this);
	},
	setValue : function(value) {
		Ec.EdiPercentField.superclass.setValue.call(this, value);
	},
	getRawValue : function() {
		var v = this.el.getValue();
		if (isNaN(v)) { 
			v = "";
		}
		return v;
	},
	getValue : function() {
		var v = this.el.getValue();
		if (isNaN(v)) { 
			v = "";
		}
		return v;
	},
	
    validateValue : function(value){
    	value = this.el.getValue();
    	if (this.allowBlank  && "" == value) {
			return true;
		}
        if(isNaN(value)){
            this.markInvalid(String.format("{0} 不是有效的数字", value));
            return false;
        }
        if(!Ec.EdiPercentField.superclass.validateValue.call(this, value)){
            return false;
        }
        var num = parseInt(value);
        if(num < this.minValue){
            this.markInvalid(String.format("输入数字不能小于 {0}", this.minValue));
            return false;
        }
        if(num > this.maxValue){
            this.markInvalid(String.format("输入数字不能大于 {0}", this.maxValue));
            return false;
        }
        return true;
    }
});
Ext.reg('ec_edipercentfield', Ec.EdiPercentField);

// ErRlValField
Ec.ErRlValField = Ext.extend(Ext.form.TextField, {
	decimalPrecision : 5,
	maskRe : /[\d\.]+/,
	allowNegative : false,
	minValue : Number.NEGATIVE_INFINITY,
	maxValue : Number.MAX_VALUE,
	allowZero : true,
	defaultNull : false,
	style : 'text-align:right;',
	
	initComponent  : function(){
		
		if (this.allowNegative) {
			this.maskRe =  /[\d\.-]+/;
		}
		
		if (this.defaultValue !== undefined) {
			this.value = this.defaultValue;
			this.emptyText = this.defaultValue;
		}
		
		Ec.ErRlValField.superclass.initComponent.call(this);
		
		this.on('blur', function(field) {
			var ov = this.el.getValue();
			
			if (!ov) {
				ov = this.defaultValue + '';
			}
			//v = v.replace(/[^0-9\.]/g, "");
			
			ov = ov.toNumber();
			
			ov = usMoney(ov, null, 5);
			
			this.validateRateValue(ov);
				//var v = this.getValue();
				//v = parseFloat((parseFloat(v)*100)).toFixed(2);
			Ec.ErRlValField.superclass.setValue.call(this, ov);
		});
		
		
	},

	setValue : function(value) {
		if ( this.defaultNull == true && (!value && value!=0 || value == '') ) {
			this.el.dom.value = '';
			this.defaultValue = undefined;
			return;
		} else if (!value) {
			this.el.dom.value = '0.00000';
			this.defaultValue = undefined;
			return;
		}
		
		if (value === this.emptyText && value === this.defaultValue) {
			this.el.dom.value = value;
			this.defaultValue = undefined;
			return;
		}
		if (isNaN(value)) {
			Ec.ErRlValField.superclass.setValue.call(this, value);
		} else {
			var v = parseFloat(parseFloat(value)).toFixed(5);
			Ec.ErRlValField.superclass.setValue.call(this, v);
		}
		
		if (this.el.dom.value === this.emptyText) {
			return;
		}

	},
	getRawValue : function() {
		var v = this.el.getValue();
		if (!v) {
			v = 0;
		}
		if (v) {
			//v = v.replace(/[^0-9\.]/g, "");
			v = v.toNumber();
		}
		
		if (!isNaN(v)) {
			v = parseFloat(parseFloat(v)).toFixed(5);
		} else {
			v = 0;
		}
		return v;
	},
	getValue : function() {
		var v = this.el.getValue();
		
		if (!v) {
			v = 0;
		} else {
			//v = (v + '').replace(/[^0-9\.]/g, "");
			v = (v + '').toNumber();
		}
		
		if (!isNaN(v)) {
			v = parseFloat(parseFloat(v)).toFixed(5);
		} else {
			v = 0;
		}
		return v;
	},
	validateRateValue : function(value){
		
		if (value) {
        	//value = value.replace(/[^0-9\.]/g, "");
			value = value.toNumber();
        }
		
        if(isNaN(value)){
            this.markInvalid(String.format("{0} 不是有效的数字", value));
            return false;
        }
        var num = parseFloat(value);
        if(num < this.minValue){
            this.markInvalid(String.format("输入数字不能小于 {0}", this.minValue));
            return false;
        }
        if(num > this.maxValue){
            this.markInvalid(String.format("输入数字不能大于 {0}", this.maxValue));
            return false;
        }
        return true;
    },
    validateValue : function(value){
        if(!Ec.ErRlValField.superclass.validateValue.call(this, value)){
            return false;
        }
        
        if (value) {
        	//value = value.replace(/[^0-9\.]/g, "");
        	value = value.toNumber();
        }
        
        if(value.length < 1){ // if it's blank and textfield didn't flag it then it's valid
             return true;
        }
        
        if(isNaN(value)){
            this.markInvalid(String.format("{0} 不是有效的数字", value));
            return false;
        }
        
        if (!this.allowZero && parseFloat(value) === 0) {
        	this.markInvalid(String.format("0不是有效的数字", value));
            return false;
        }
        
        var num = parseFloat(value);
        
        if(num < this.minValue){
            this.markInvalid(String.format("输入数字不能小于 {0}", this.minValue));
            return false;
        }
        if(num > this.maxValue){
            this.markInvalid(String.format("输入数字不能大于 {0}", this.maxValue));
            return false;
        }
        return true;
    }
});

Ext.reg('ec_erRlValfield', Ec.ErRlValField);



Ec.BlankField = Ext.extend(Ext.form.TextField, {
	labelStyle : 'display:none',
	style : 'display:none',
	fieldLabel : ' '
});

Ext.reg('ec_blankfield', Ec.BlankField);


function usRate(value) {
	if (!value || value == 0) {
		return '0.0000';
	}
	var v = parseFloat((parseFloat(value)*100)).toFixed(4);
	return v + '';
}

var keepRefresh = function(cmpId, time) {
	var cmp = Ext.getCmp(cmpId);

	var _func = function() {
		cmp.store.reload();
		cmp.keepR = setTimeout(_func, time);
	}

	if (cmp.store) {
		cmp.keepR = setTimeout(_func, time);
	}
}

/**
 * 设置控件disabled样式
 */
Ext.apply(Ext.form.TextField.prototype, {
      disabledClass : "disabled_input"
});

Ext.apply(Ext.form.TextArea.prototype, {
      disabledClass : "disabled_textarea"
});

Ext.apply(Ext.form.ComboBox.prototype, {
      disabledClass : "x-item-disabled"
});

/*
Ext.apply(Ext.grid.GridView.prototype, {
	focusRow : function(A) {
		//this.focusCell(A, 0, false)
	},layout : function() {
		if (!this.mainBody) {
			return
		}
		var C = this.grid;
		var F = C.getGridEl();
		var A = F.getSize(true);
		var B = A.width;
		if (B < 20 || A.height < 20) {
			return
		}
		if (C.autoHeight) {
			this.scroller.dom.style['overflow-x'] = "visible";
			this.scroller.dom.style.overflow = "visible"
		} else {
			this.el.setSize(A.width, A.height);
			var E = this.mainHd.getHeight();
			var D = A.height - (E);
			this.scroller.setSize(B, D);
			if (this.innerHd) {
				this.innerHd.style.width = (B) + "px"
			}
		}
		if (this.forceFit) {
			if (this.lastViewWidth != B) {
				this.fitColumns(false, false);
				this.lastViewWidth = B
			}
		} else {
			this.autoExpand();
			this.syncHeaderScroll()
		}
		this.onLayout(B, D)
	},fitColumns : function(D, G, E) {
		var F = this.cm, S, L, O;
		var R = F.getTotalWidth(false);
		
		// 修改宽度
		var oldWidth = this.grid.getInnerWidth();
		if (oldWidth < this.grid.bodywidth) {
			this.grid.changeWidth = true;
			var gridel = this.grid.getGridEl();
			
			var gridparent = gridel.parent();
			gridparent.setStyle('width', oldWidth);
			if (gridel.prev()) {
				gridel.prev().setStyle('width', this.grid.bodywidth);
			}
			gridel.setStyle('width', this.grid.bodywidth);
			if (this.grid.bbar) {
				gridel.next().setStyle('width', this.grid.bodywidth);
			}
		} else {
			this.grid.changeWidth = false;
		}
			
		var J = this.grid.getGridEl().getWidth(true) - this.scrollOffset;
		//this.grid.oldWidth = this.grid.getGridEl().getWidth(true);
		//var J = this.grid.bodywidth - this.scrollOffset;
		
		if (J < 20) {
			return
		}
		var B = J - R;
		if (B === 0) {
			return false
		}
		var A = F.getColumnCount(true);
		var P = A - (typeof E == "number" ? 1 : 0);
		if (P === 0) {
			P = 1;
			E = undefined
		}
		var K = F.getColumnCount();
		var I = [];
		var N = 0;
		var M = 0;
		var H;
		for (O = 0; O < K; O++) {
			if (!F.isHidden(O) && !F.isFixed(O) && O !== E) {
				H = F.getColumnWidth(O);
				I.push(O);
				N = O;
				I.push(H);
				M += H
			}
		}
		var C = (J - F.getTotalWidth()) / M;
		while (I.length) {
			H = I.pop();
			O = I.pop();
			F.setColumnWidth(O, Math.max(this.grid.minColumnWidth, Math.floor(H
									+ H * C)), true)
		}
		if ((R = F.getTotalWidth(false)) > J) {
			var Q = P != A ? E : N;
			F.setColumnWidth(Q, Math.max(1, F.getColumnWidth(Q) - (R - J)),
					true)
		}
		if (D !== true) {
			this.updateAllColumnWidths()
		}
		return true
	}
});


Ext.apply(Ext.grid.RowSelectionModel.prototype, {
		handleMouseDown : function(D, F, E) {
				if (E.button !== 0 || this.isLocked()) {
					return
				}
				var A = this.grid.getView();
				if (E.shiftKey && this.last !== false) {
					var C = this.last;
					this.selectRange(C, F, E.ctrlKey);
					this.last = C;
					A.focusRow(F)
				} else {
					var B = this.isSelected(F);
					if (E.ctrlKey && B) {
						this.deselectRow(F)
					} else {
						if (!B || this.getCount() > 1) {
//							var selectflag = true;
//							if (!this.grid.singleSelect) {
//								if (this.getCount() > 0) {
//									selectflag = (E.ctrlKey || E.shiftKey) ;
//								}
//							}
//							
//							if (selectflag) {
//								this.selectRow(F, E.ctrlKey || E.shiftKey);
//							}
							this.selectRow(F, true);
							A.focusRow(F)
						}
					}
				}
			}
});
*/

// add by fuhaining 2010.12.21 start 
Ec.MoneyLabel = Ext.extend(Ext.form.Label, {
	setUsMoney : true,
	style : 'text-align:left',
	letterRe : /^[a-zA-Z]+$/g,
	defaultValue : 0,
	msgAlert : false,
	isFormField: true,
	
	initComponent  : function(){
        Ec.MoneyLabel.superclass.initComponent.call(this);

        if (this.ccyCode) {
        	// 币种必须是字母
        	var s = this.ccyCode.replace(this.letterRe,"");
        	if (s.length > 0) {
        		this.ccyCode = undefined;
        	}
        }
        
        if (this.value) {
        	this.setValue(this.value);
        }
	},
	getName: function(){
		return this.name;
	},
	setValue : function(value) {
		if (!value) {
			value = this.defaultValue + '';
		} else {
			value = value + '';
		}

		// 过滤非数字
		value = value.replace(/[^0-9\.]/g, "");
		
		if (this.setUsMoney) {
			this.setText(usMoney(value, this.ccyCode, this.scale));
		} else {
			this.setText(value);
		}
	},
	getRawValue : function() {
		var v =  this.text;

		if (v) {
			v = v.replace(/[^0-9\.]/g, "");
		}

		return v;
	},
	validate: function(){
		return true;
	}
});
Ext.reg('ec_moneylabel', Ec.MoneyLabel);

Ec.PercentLabel = Ext.extend(Ext.form.Label, {
//	style : 'text-align:right',
	decimalPrecision : 2,
	maskRe : /[\d\.]+/,
	allowNegative : true,
	minValue : Number.NEGATIVE_INFINITY,
	maxValue : Number.MAX_VALUE,
	allowZero : true,
	
	initComponent  : function(){
		
		if (this.allowNegative) {
			this.maskRe =  /[\d\.-]+/;
		}
		
		if (this.defaultValue !== undefined) {
			this.value = this.defaultValue;
			this.emptyText = this.defaultValue;
		}
		Ec.PercentLabel.superclass.initComponent.call(this);
		
		if (this.value) {
        	this.setValue(this.value);
        }
	},

	setValue : function(value) {
		if (value === this.emptyText && value === this.defaultValue) {
			this.el.dom.value = value;
			this.defaultValue = undefined;
			return;
		}
		
		if (value) {
			value = (value+'').replace(/[^0-9\.-]/g, "");
		}
		
		if (isNaN(value)) {
			this.setText(value);
//			Ec.PercentLabel.superclass.setValue.call(this, value);
		} else {
			var v = parseFloat(value)*100;
			v = usMoney(usMoneyRound(v));
			//var v = parseFloat((parseFloat(value)*100)).toFixed(2);
			this.setText(v);
//			Ec.PercentLabel.superclass.setValue.call(this, v);
		}
		if (this.el.dom.value === this.emptyText) {
			return;
		}

	},
	getRawValue : function() {
		var v = this.getEl().dom.innerHTML;
		
		if (v) {
			v = v.replace(/[^0-9\.]/g, "");
		}
		
		if (!isNaN(v)) {
			v = parseFloat((parseFloat(v)/100)).toFixed(4);
		} else {
			v = 0;
		}
		return v;
	},
	getValue : function() {
		var v = this.getEl().dom.innerHTML;
		if (v) {
			v = v.replace(/[^0-9\.]/g, "");
		}
		
		if (!isNaN(v)) {
			v = parseFloat((parseFloat(v)/100)).toFixed(4);
		} else {
			v = 0;
		}
		return v;
	},
    validate: function(){
		return true;
	}
});

Ext.reg('ec_percentlabel', Ec.PercentLabel);


Ec.LocalComboBox = Ext.extend(Ext.form.ComboBox, {
	fireSelectOnInit : false,
	mode : 'local',
	fields : ['strVal', 'name'],
	displayField : 'name',
	valueField : 'strVal',
	triggerAction : 'all',
	editable : false,
	matchWhenLoad : true,

	initComponent : function() {
		var initData = [];
		if (this.data && this.data.constructor == Array) {
			initData = this.data;
		}
		this.store = new Ext.data.JsonStore({
			url : this.url || '',
			fields : this.fields,
			data : initData
		});
		
		Ec.ComboBox.superclass.initComponent.apply(this, arguments);
	},
	setValue : function(values) {
		if(values == null)
			return;
		if (values.constructor == Array) {
			var oldValue = this.getValue();
			this.clearValue();
			this.store.loadData(values);
			
			if (!this.matchWhenLoad) {
				this.oldValue = undefined;
			}
			
			var isSet = false;
			for (var i = 0; i < values.length; i++) {
				if (values[i] && values[i].selected) {
					var showValue = this.valueField || this.displayField;
					Ec.ComboBox.superclass.setValue.call(this, values[i][showValue]);
					if(values[i][showValue]!=oldValue)
					{
						var record = this.findRecord(this.valueField, values[i][showValue]);
						if(record) {
							if (this.fireSelectOnInit) {
								this.fireEvent('select', this, record);
							}
						}
					}
					isSet = true;
					this.oldValue = values[i][showValue];
					//this.setValue(record[this.valueField]);
					break;
				}
			}
			if(!isSet)
			{
				if (!this.matchWhenLoad) {
					return;
				}
				//Ec.ComboBox.superclass.setValue.call(this, oldValue);
				var record = this.findRecord(this.valueField, oldValue);
				if(record) {
					Ec.ComboBox.superclass.setValue.call(this, oldValue);
					if (this.fireSelectOnInit) {
						this.fireEvent('select', this, record);
					}
					this.oldValue = oldValue;
				} else {
					if (this.fireSelectOnInit) {
						this.fireEvent('select', this, null);
					}
					this.oldValue = undefined;
				}
			}
		} else {
			Ec.ComboBox.superclass.setValue.call(this, values);
			this.oldValue = values;
		}
	},
	onSelect : function(A, B) {
		if (this.fireEvent("beforeselect", this, A, B) !== false) {
			var value = A.data[this.valueField || this.displayField]
			var fireSelect = false;
			if (this.oldValue !== value) {
				fireSelect = true;
			}
			this.setValue(value);
			this.collapse();
			if (fireSelect) {
				this.fireEvent("select", this, A, B);
			}
			this.oldValue = value;
		}
	},
	getSubmitValue: function(){
		var v = this.getValue();
		return v;
	},
	validateValue : function() {
		if (!this.allowBlank) {
			var v = this.getValue() || -1;
			if (v == -1) {
				this.markInvalid(this.blankText);
				return false
			}
		}
		return true
	}
});
Ext.reg('ec_localcombo', Ec.LocalComboBox);

Ec.LocalMultiCheckbox = Ext.extend(Ext.form.Field, {
	columns:2,
	typetag : 'checkbox',
	blankText : "You must select at least one item in this group",
	data : [],
	initComponent  : function(){
        Ec.LocalMultiCheckbox.superclass.initComponent.call(this);
    	this.addEvents(
            'checkchange'
        );
	},
	onRender : function(ct, position){
		this.el = ct.createChild({tag:'div',cls:'x-form-field x-form-multicheckbox',id:this.id||Ext.id()});
		this.el.dom.value = [];
	},
	genNode: function(){
		var first = this.el.first();
		if(first) first.remove();

		this.el.dom.value = [];
		var m = ['<table cellspacing="0" class="noboder" style="border:0px;" width="100%">',
                '<tr align="left">'];
        var el = document.createElement("div");
        var dataLength = this.data.length ;
        for (var i=0;i<dataLength;i++) {
        	if(i%this.columns == 0 && i > 0){
        		m.push('</tr><tr align="left">');
        	}
			var id = Ext.id();
			m.push('<td align="left">');
			m.push("<label style='font-size:12px; white-space:nowrap; text-align:left;width:100%;'>" );
			m.push("<input type='" + this.typetag + "' id='" + id + "' name='" + this.name + "' value='" + this.data[i].strVal + "'");
			if (this.data[i].selected == true) {
				m.push(' checked="true" ');
			}
			m.push("/>");
			m.push(this.data[i].name + "</label>");
			m.push('</td>');
		}
		m.push("</tr></table>");
		el.innerHTML = m.join("");
		this.el.insertFirst(el);
		
		this.el.select('input').each(function(i) {
			i.on({'click' : {
				        fn: this.onClick,
				        scope: this,
				        delay: 100
				    }
			});
		},this);
	},
	onClick : function(e, v){
		var values = this.getValue();
		var value = v.checked;
		var target = v.value;
		this.fireEvent('checkchange', this, values, target, value);
	},
	getName : function(){
		return this.name;
	},
	getValue : function() {
		var value = [];
		this.el.select('input').each(function(i) {
			if (i.dom.checked) {
				value.push(i.dom.value);
			}
		});
		if(this.typetag == 'radio')
		{
			if(value.length>0)
				return value[0];
			else
				return null;
		}
		else
			return value;
	},
	getRawValue : function() {
		return this.getValue();
	},
	setValue : function(value) {
		this.data = value || [];
		if (this.data.length > 0) {
			this.genNode();
		}
	},
	setEnable : function(items, value) {
		if (items != undefined) {
			 if(!(items instanceof Array))
			 {
				var vs = [];
				vs.push(items);
				items = vs;
			 }
		}
		this.el.select('input').each(function(node) {
				for(var i=0; i<items.length;i++)
				{
					if(items[i] == node.dom.value)
					{
						node.dom.disabled = value;
						break;
					}
				}
			});
	},
	setRawValue : function(value) {
		this.setValue(value);
	},
	validateValue : function(A) {
		if (!this.allowBlank) {
			var B = false;

			var v = this.getValue();
			if(this.typetag == 'radio') {
				if (v === null) {
					B = true;
				}
			} else {
				if (v.length == 0) {
					B = true;
				}
			}

			if (B) {
				this.markInvalid(this.blankText);
				return false
			}
		}
		return true
	}
});
Ext.reg('ec_localcheckboxgroup', Ec.LocalMultiCheckbox);


Ec.GrowTextarea = Ext.extend(Ext.form.TextArea, {
	grow: true,
	growMin : 20,
	enableKeyEvents: true,
	style : 'text-align:left'
});
Ext.reg('ec_growtextarea', Ec.GrowTextarea);

// add by fuhaining 2010.12.21 end


// add by zhenfangyong 2011.4.15
Ec.GrowMsgTextarea = Ext.extend(Ext.form.TextArea, {
	grow: true,
	growMin : 60,
	growMax : 100,
	maxLength: 500,
	enableKeyEvents: true,
	style : 'text-align:left',
    validator : function(value) {
		if (value.existsGB()) {
			return "不能输入中文";
		}
		return true
	}
});
Ext.reg('ec_growmsgtextarea', Ec.GrowMsgTextarea);

Ec.MsgTextField = Ext.extend(Ext.form.TextField, {
	style : 'text-align:left',
    validator : function(value) {
		if (value.existsGB()) {
			return "不能输入中文";
		}
		return true
	}
});
Ext.reg('ec_msgtextfield', Ec.MsgTextField);
// and by zhenfangyong 2011.4.15 end

/**
 * 币种Code控件，初始化时未ccyItemIds赋值，值为用到此币种Code的控件ID数组
 * 当此控件值改变时，将注册的控件币种Code同时改变
 * @class Ec.CcyCodeField
 * @extends Ext.form.Hidden
 */
Ec.CcyCodeField = Ext.extend(Ext.form.Hidden, {
	ccyItemIds : [],

	initComponent  : function(){
        Ec.CcyCodeField.superclass.initComponent.call(this);
	},
	setValue : function(value) {
		this.value = value;
		if (this.ccyItemIds && this.ccyItemIds.length > 0) {
			for ( var i = 0; i < this.ccyItemIds.length; i++) {
				var item = Ext.getCmp(this.ccyItemIds[i]);
				item.ccyCode = value;
				item.setValue(item.getValue());
			}
		}
	},
	getRawValue : function(value) {
		return this.value;
	}
});

Ext.reg('ec_ccycodefield', Ec.CcyCodeField);


/**
 * grid标题居中问题
 */
Ext.apply(Ext.grid.GridView.prototype, {
	getColumnStyle : function(A, C) {
		var B = !C ? (this.cm.config[A].css || "") : "";
		B += "width:" + this.getColumnWidth(A) + ";";
		if (this.cm.isHidden(A)) {
			B += "display:none;"
		}
		var D = this.cm.config[A].align;
		if (!C) {
			B += "text-align:" + (D ? D : "center") + ";"
		}
		
		var E = this.cm.config[A].headerAlign;
		if (C) {
			B += "text-align:" + (E ? E : "center") + ";"
		}
		return B
	}
});

/**
 * 汇率label，保留5位小数
 * @class Ec.RateLabel
 * @extends Ext.form.Label
 */
Ec.RateLabel = Ext.extend(Ext.form.Label, {
	isFormField: true,
	style : 'text-align:left',
	getName: function(){
		return this.name;
	},
	setValue: function(value){
		if (value != undefined) {
			value = usMoney(value, null, 5);
			value = value.toNumber();
		}
		this.setText(value);
	},
	validate: function(){
		return true;
	},
	getRawValue: function(){
		return this.text;
	}
});

Ext.reg('ec_ratelabel', Ec.RateLabel);



Ext.apply(Ext.grid.GridEditor.prototype, {
	autoSize : true,
	hideEl : false
});

Ec.NumberField = Ext.extend(Ext.form.NumberField, {
	
	allowZero : true,
	
    validateValue : function(value){
        if(!Ec.NumberField.superclass.validateValue.call(this, value)){
            return false;
        }
        
        if (!this.allowZero && parseFloat(value) === 0) {
        	this.markInvalid(String.format("0不是有效的数字", value));
            return false;
        }
        
        return true;
    }
});

Ext.reg('ec_numberfield', Ec.NumberField);