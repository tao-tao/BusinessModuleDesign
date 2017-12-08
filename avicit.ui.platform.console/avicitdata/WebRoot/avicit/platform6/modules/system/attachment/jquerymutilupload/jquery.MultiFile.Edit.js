/*
 ### jQuery Multiple File Upload Plugin v1.48 - 2012-07-19 ###
 * Home: http://www.fyneworks.com/jquery/multiple-file-upload/
 * Code: http://code.google.com/p/jquery-multifile-plugin/
 *
	* Licensed under http://en.wikipedia.org/wiki/MIT_License
 ###
*/
var closeImg = "images/file/action_delete.gif";
var baseUrl;
var fileuploadBusinessId;
var fileuploadBusinessTableName;
var openControl_flag = true;
var fileuploadIsSaveToDatabase = true;
var fieldArray = new Array();
var fileTypeArray = new Array();
var isDeleteFlagArray = new Array();
var isDownloadFlagArray = new Array();
var maxAttachCountArray = new Array();
var existsAttachCount = 0;
/*# AVOID COLLISIONS #*/
;if(window.jQuery) (function($){
/*# AVOID COLLISIONS #*/
	AttachFileEdit = function AttachFile(_baseUrl,_fileuploadBusinessId, _fileuploadBusinessTableName,_fileType,_openControl_flag,_isDelete_flag,_isDownload_flag,_fileuploadIsSaveToDatabase,_bindingTableFiled,_maxCount){
		baseUrl = _baseUrl;
		fileuploadBusinessId = _fileuploadBusinessId;
		fileuploadBusinessTableName =_fileuploadBusinessTableName;
		//fileType =  _fileType;
		fileTypeArray.push(_fileType);
		if(typeof(_isDelete_flag) != 'undefined' && (_isDelete_flag != null || _isDelete_flag !='' || _isDelete_flag !='null')){
			//isDelete_flag = _isDelete_flag;
			isDeleteFlagArray.push(_isDelete_flag);
		}
		if(typeof(_isDownload_flag) != 'undefined' && (_isDownload_flag != null || _isDownload_flag !='' || _isDownload_flag !='null')){
			//isDownload_flag = _isDownload_flag;
			isDownloadFlagArray.push(_isDownload_flag);
		}
		if(typeof(_fileuploadIsSaveToDatabase) != 'undefined' && (_fileuploadIsSaveToDatabase != null || _fileuploadIsSaveToDatabase !='' || _fileuploadIsSaveToDatabase !='null')){
			fileuploadIsSaveToDatabase = _fileuploadIsSaveToDatabase;
		}
		if(typeof(_bindingTableFiled) != 'undefined' && (_bindingTableFiled != null || _bindingTableFiled !='' || _bindingTableFiled !='null')){
			fieldArray.push(_bindingTableFiled);
		}
		if(typeof(_maxCount) != 'undefined' && (_maxCount != null || _maxCount !='' || _maxCount !='null')){
			maxAttachCountArray.push(_maxCount);
		}else{
			maxAttachCountArray.push(-1);
		}
	// plugin initialization
	$.fn.MultiFile = function(options){
		if(fieldArray.length==0) return this; // quick fail
		// Handle API methods
		if(typeof arguments[0]=='string'){
			// Perform API methods on individual elements
			if(this.length>1){
				var args = arguments;
				return this.each(function(){
					$.fn.MultiFile.apply($(this), args);
    });
			};
			// Invoke API method handler
			$.fn.MultiFile[arguments[0]].apply(this, $.makeArray(arguments).slice(1) || []);
			// Quick exit...
			return this;
		};
		
		// Initialize options for this call
		var options = $.extend(
			{}/* new object */,
			$.fn.MultiFile.options/* default options */,
			options || {} /* just-in-time options */
		);
		
		// Empty Element Fix!!!
		// this code will automatically intercept native form submissions
		// and disable empty file elements
		$('form')
		.not('MultiFile-intercepted')
		.addClass('MultiFile-intercepted')
		.submit($.fn.MultiFile.disableEmpty);
		
		//### http://plugins.jquery.com/node/1363
		// utility method to integrate this plugin with others...
		if($.fn.MultiFile.options.autoIntercept){
			$.fn.MultiFile.intercept( $.fn.MultiFile.options.autoIntercept /* array of methods to intercept */ );
			$.fn.MultiFile.options.autoIntercept = null; /* only run this once */
		};
		
		// loop through each matched element
		this
		 .not('.MultiFile-applied')
			.addClass('MultiFile-applied')
		.each(function(){
		    var fileType = '';
			if(fileTypeArray !=null){
				fileType = fileTypeArray[0];
				fileTypeArray.splice(0,1);
			}
			
			var maxAttachCount = -1;
			if(maxAttachCountArray !=null){
				maxAttachCount = maxAttachCountArray[0];
				maxAttachCountArray.splice(0,1);
			}
			
			var isDelete_flag = true;
				if(isDeleteFlagArray !=null){
					isDelete_flag = isDeleteFlagArray[0];
					isDeleteFlagArray.splice(0,1);
			}
			
			var isDownload_flag = true;
				if(isDownloadFlagArray !=null){
					isDownload_flag = isDownloadFlagArray[0];
					isDownloadFlagArray.splice(0,1);
			}
			//#####################################################################
			// MAIN PLUGIN FUNCTIONALITY - START
			//#####################################################################
			
       // BUG 1251 FIX: http://plugins.jquery.com/project/comments/add/1251
       // variable group_count would repeat itself on multiple calls to the plugin.
       // this would cause a conflict with multiple elements
       // changes scope of variable to global so id will be unique over n calls
       window.MultiFile = (window.MultiFile || 0) + 1;
       var group_count = window.MultiFile;
       
       // Copy parent attributes - Thanks to Jonas Wagner
       // we will use this one to create new input elements
       var MultiFile = {e:this, E:$(this), clone:$(this).clone()};
       
       //===
       
       //# USE CONFIGURATION
       if(typeof options=='number') options = {max:options};
       var o = $.extend({},
        $.fn.MultiFile.options,
        options || {},
   					($.metadata? MultiFile.E.metadata(): ($.meta?MultiFile.E.data():null)) || {}, /* metadata options */
								{} /* internals */
       );
       // limit number of files that can be selected?
    // 以下这段代码现在没有用。目前对原来的功能进行了扩展。20131005 /*IsNull(MultiFile.max)*/
       /*
       if(!(o.max>0) ){
        o.max = MultiFile.E.attr('maxlength');
       };
		if(!(o.max>0)){
			o.max = (String(MultiFile.e.className.match(/\b(max|limit)\-([0-9]+)\b/gi) || ['']).match(/[0-9]+/gi) || [''])[0];
			if(!(o.max>0)) o.max = -1;
			else  o.max = String(o.max).match(/[0-9]+/gi)[0];
		}
		*/
		// 以上这段代码现在没有用。目前对原来的功能进行了扩展。20131005			
       o.max = new Number(maxAttachCount);
       // limit extensions?
       o.accept = o.accept || MultiFile.E.attr('accept') || '';
       if(!o.accept){
        o.accept = (MultiFile.e.className.match(/\b(accept\-[\w\|]+)\b/gi)) || '';
        o.accept = new String(o.accept).replace(/^(accept|ext)\-/i,'');
       };
       
       //===
       
       // APPLY CONFIGURATION
							$.extend(MultiFile, o || {});
       MultiFile.STRING = $.extend({},$.fn.MultiFile.options.STRING,MultiFile.STRING);
       
       //===
       
       //#########################################
       // PRIVATE PROPERTIES/METHODS
       $.extend(MultiFile, {
        n: 0, // How many elements are currently selected?
        slaves: [], files: [],
        instanceKey: MultiFile.e.id || 'MultiFile'+String(group_count), // Instance Key?
        generateID: function(z){ return MultiFile.instanceKey + (z>0 ?'_F'+String(z):''); },
        trigger: function(event, element){
         var handler = MultiFile[event], value = $(element).attr('value');
         if(handler){
          var returnValue = handler(element, value, MultiFile);
          if( returnValue!=null ) return returnValue;
         }
         return true;
        }
       });
       
       //===
       
       // Setup dynamic regular expression for extension validation
       // - thanks to John-Paul Bader: http://smyck.de/2006/08/11/javascript-dynamic-regular-expresions/
      // alert("String(MultiFile.accept).length::"+String(MultiFile.accept).length);
      // if(String(MultiFile.accept).length>1){
	//							MultiFile.accept = MultiFile.accept.replace(/\W+/g,'|').replace(/^\W|\W$/g,'');
      //  MultiFile.rxAccept = new RegExp('\\.('+(MultiFile.accept?MultiFile.accept:'')+')$','gi');
     //  };
       
       //===
       
       // Create wrapper to hold our file list
       MultiFile.wrapID = MultiFile.instanceKey+'_wrap'; // Wrapper ID?
       MultiFile.E.wrap('<div class="MultiFile-wrap" id="'+MultiFile.wrapID+'"></div>');
       MultiFile.wrapper = $('#'+MultiFile.wrapID+'');
       
       //===
       
       // MultiFile MUST have a name - default: file1[], file2[], file3[]
       MultiFile.e.name = MultiFile.e.name || 'file'+ group_count +'[]';
       
       //===20131001修改，主要是增加多区域的附件，必须要把值放在数组中，只有当所有的htmlContainer的createDom结束后，才进入到该方法中，因此必须要这么处理才行。
       var bindingTableFiled = '';
		if(fieldArray!=null){
			bindingTableFiled = fieldArray[0];
			fieldArray.splice(0,1);
		}
		
		//==以上代码必须要放在这里进行执行才对。
		if(!MultiFile.list){
			var _drawAttachPanel = new drawAttachPanel(baseUrl);
						// Create a wrapper for the list
								// * OPERA BUG: NO_MODIFICATION_ALLOWED_ERR ('list' is a read-only property)
								// this change allows us to keep the files in the order they were selected
								MultiFile.wrapper.append( '<div class="MultiFile-list" id="'+MultiFile.wrapID+'_list"></div>' );//原来的版本，20131001 
								$(".MultiFile-list").append(_drawAttachPanel.getAttachPanel(MultiFile.wrapID,fileuploadBusinessId, fileuploadBusinessTableName,fileType,openControl_flag,isDelete_flag,isDownload_flag,fileuploadIsSaveToDatabase,bindingTableFiled));
								MultiFile.list = $('#'+MultiFile.wrapID+'_list');
		   };
           MultiFile.list = $(MultiFile.list);
       // Bind a new element
           MultiFile.addSlave = function( slave, slave_count ){
		//if(window.console) console.log('MultiFile.addSlave',slave_count);
        // Keep track of how many elements have been displayed
           MultiFile.n++;
        // Add reference to master element
           slave.MultiFile = MultiFile;
								
								// BUG FIX: http://plugins.jquery.com/node/1495
								// Clear identifying properties from clones
								if(slave_count>0) slave.id = slave.name = '';
								
        // Define element's ID and name (upload components need this!)
        //slave.id = slave.id || MultiFile.generateID(slave_count);
								if(slave_count>0) slave.id = MultiFile.generateID(slave_count);
								//FIX for: http://code.google.com/p/jquery-multifile-plugin/issues/detail?id=23
        
        // 2008-Apr-29: New customizable naming convention (see url below)
        // http://groups.google.com/group/jquery-dev/browse_frm/thread/765c73e41b34f924#
        slave.name = String(MultiFile.namePattern
         /*master name*/.replace(/\$name/gi,$(MultiFile.clone).attr('name'))
         /*master id  */.replace(/\$id/gi,  $(MultiFile.clone).attr('id'))
         /*group count*/.replace(/\$g/gi,   group_count)//(group_count>0?group_count:''))
         /*slave count*/.replace(/\$i/gi,   slave_count)//(slave_count>0?slave_count:''))
        );
        // If we've reached maximum number, disable input slave
       
        // old  20131007
        //if( (MultiFile.max > 0) && ((MultiFile.n + existsAttachCount+1) > (MultiFile.max-1)) )//{ // MultiFile.n Starts at 1, so subtract 1 to find true count
        	   //      slave.disabled = true;
        //};
        
        // Remember most recent slave
        MultiFile.current = MultiFile.slaves[slave_count] = slave;
        
								// We'll use jQuery from now on
								slave = $(slave);
        
        // Clear value
        slave.val('').attr('value','')[0].value = '';
        
								// Stop plugin initializing on slaves
								slave.addClass('MultiFile-applied');
								
        // Triggered when a file is selected
        slave.change(function(){
          //if(window.console) console.log('MultiFile.slave.change',slave_count);
 								 
          // Lose focus to stop IE7 firing onchange again
          $(this).blur();
          
          //# Trigger Event! onFileSelect
          if(!MultiFile.trigger('onFileSelect', this, MultiFile)) return false;
          //# End Event!
          
          //# Retrive value of selected file from element
          var ERROR = '', v = String(this.value || ''/*.attr('value)*/);
          
          // 刘伟20130830修改，主要是扩展上传文件类型的方法 -- start
          if(String(fileType).length>1){
        	  MultiFile.accept = fileType;
   			  MultiFile.accept = MultiFile.accept.replace(/\W+/g,'|').replace(/^\W|\W$/g,'');
   			  MultiFile.rxAccept = new RegExp('\\.('+(MultiFile.accept?MultiFile.accept:'')+')$','gi');
          };
         
          
          // check extension v.match(MultiFile.rxAccept) !=null &&
          // 刘伟，扩展只能上传某种类型的后处理方法 20130830
          if(MultiFile.accept && v && !v.match(MultiFile.rxAccept)){
            var temp = '';
             temp = MultiFile.STRING.denied.replace('$ext', String(v.match(/\.\w{1,4}$/gi)));
             ERROR = temp.replace('$doctype', String(fileType));
           };
           
           // 当上传个数大于最大设定值后，报错，一定要判断编辑时.ultiFile-label_save display为none的要去掉，不然算法有问题
           // 刘伟 20131007  extend
           if(MultiFile.max != -1){
        	   existsAttachCount = 0;
        	   $("#"+MultiFile.wrapID+"_list").find(".ultiFile-label_save").each(function (){
        		   if($(this).css("display")=="none"){
        		   }else{
        			   existsAttachCount++;
        		   }
        	   });
        	   MultiFile.n = $("#"+MultiFile.wrapID+"_list").find(".MultiFile-title").length;
        	   if((existsAttachCount + MultiFile.n + 1) > MultiFile.max){
        		   ERROR = "附件个数最多为"+MultiFile.max+"个!";
        	   }
            };
              
           // 扩展上传个数--end 20131007
           
          // Disallow duplicates
           // 处理文件是否存在，与添加不同的是，这个还需要与数据库里已经存在的文件名进行比较。start
         var vFullName = this.value;
         var insertFileName =MultiFile.STRING.file.replace('$file', vFullName.match(/[^\/\\]+$/gi)[0]);
     	 var stringFileNames = $jqeuryGetFileNames(fileuploadBusinessId,fileuploadBusinessTableName,bindingTableFiled);
     	 var arrayFileNames= new Array(); 
     	 arrayFileNames=stringFileNames.split(","); //字符分割      
     	 if($inArray(insertFileName,arrayFileNames)){
     		 ERROR = MultiFile.STRING.existsDuplicate.replace('$file', v.match(/[^\/\\]+$/gi));
     	 }
     	 
     	 
		for(var f in MultiFile.slaves)//{
           if(MultiFile.slaves[f] && MultiFile.slaves[f]!=this)//{
            if(MultiFile.slaves[f].value==v)//{
             ERROR = MultiFile.STRING.duplicate.replace('$file', v.match(/[^\/\\]+$/gi));
            //};
           //};
          //};
		// 处理文件是否存在，与添加不同的是，这个还需要与数据库里已经存在的文件名进行比较 end
		
          // Create a new file input element
          var newEle = $(MultiFile.clone).clone();// Copy parent attributes - Thanks to Jonas Wagner
          //# Let's remember which input we've generated so
          // we can disable the empty ones before submission
          // See: http://plugins.jquery.com/node/1495
          newEle.addClass('MultiFile');
          
          // Handle error
          if(ERROR!=''){
            // Handle error
            MultiFile.error(ERROR);
												
            // 2007-06-24: BUG FIX - Thanks to Adrian Wr骲el <adrian [dot] wrobel [at] gmail.com>
            // Ditch the trouble maker and add a fresh new element
            MultiFile.n--;
            MultiFile.addSlave(newEle[0], slave_count);
            slave.parent().prepend(newEle);
            slave.remove();
            return false;
          };
          
          // Hide this element (NB: display:none is evil!)
          $(this).css({ position:'absolute', top: '-3000px' });
          
          // Add new element to the form
          slave.after(newEle);
          
            // Bind functionality
          MultiFile.addSlave( newEle[0], slave_count+1 );
          
        
          //# Trigger Event! afterFileSelect
          if(!MultiFile.trigger('afterFileSelect', this, MultiFile)) return false;
          //# End Event!
          
            // Update list
          MultiFile.addToList( this, slave_count );
          
        }); // slave.change()
								
				// Save control to element
				$(slave).data('MultiFile', MultiFile);
								
							
								
       };// MultiFile.addSlave
       // Bind a new element
       
       
       // Add a new file to the list
       MultiFile.addToList = function(slave, slave_count ){
        //if(window.console) console.log('MultiFile.addToList',slave_count);
        //# Trigger Event! onFileAppend
        if(!MultiFile.trigger('onFileAppend', slave, MultiFile)) return false;
        //# End Event!
        var attach_util = new AttachUtil(baseUrl);
        r = $('<SPAN class="ultiFile-label">&nbsp;&nbsp;</SPAN>'),
        v = String(slave.value || ''/*.attr('value)*/),
        a = $('<span class="MultiFile-title" style="HEIGHT: 22px" title="'+MultiFile.STRING.selected.replace('$file', v)+'"><img align="absmiddle" src="'+attach_util.countImage(MultiFile.STRING.file.replace('$file', v.match(/[^\/\\]+$/gi)[0]))+'"/>'+MultiFile.STRING.file.replace('$file', v.match(/[^\/\\]+$/gi)[0])+'&nbsp;&nbsp;'+MultiFile.fileSize(slave)+'</span>'),
        b = $('<a class="MultiFile-remove" href="#'+MultiFile.wrapID+'"><img align="absmiddle" border="0" alt="单击删除该附件" src="'+baseUrl+closeImg+'"></a>');
      	if(isDelete_flag){
       		a.append(b);
        }else{
     	  
        }
       MultiFile.list.append(r.append(a));
        
        b.click(function(){
         
          //# Trigger Event! onFileRemove
          if(!MultiFile.trigger('onFileRemove', slave, MultiFile)) return false;
          //# End Event!
          
          MultiFile.n--;
          MultiFile.current.disabled = false;
          
          // Remove element, remove label, point to current
										MultiFile.slaves[slave_count] = null;
										$(slave).remove();
										$(this).parent().remove();
										
          // Show most current element again (move into view) and clear selection
          $(MultiFile.current).css({ position:'', top: '' });
										$(MultiFile.current).reset().val('').attr('value', '')[0].value = '';
          
          //# Trigger Event! afterFileRemove
          if(!MultiFile.trigger('afterFileRemove', slave, MultiFile)) return false;
          //# End Event!
										
          return false;
        });
        
        //# Trigger Event! afterFileAppend
        if(!MultiFile.trigger('afterFileAppend', slave, MultiFile)) return false;
        //# End Event!
        
       }; // MultiFile.addToList
       // Add element to selected files list
       
       
       
       // Bind functionality to the first element
       if(!MultiFile.MultiFile) MultiFile.addSlave(MultiFile.e, 0);
       
       // Increment control count
       //MultiFile.I++; // using window.MultiFile
       MultiFile.n++;
							
							// Save control to element
							MultiFile.E.data('MultiFile', MultiFile);
							
      
			//#####################################################################
			// MAIN PLUGIN FUNCTIONALITY - END
			//#####################################################################
		  /*计算文件大小 */
  MultiFile.fileSize = function(_file){
	var displayFileSize = 0;
  	var browserCfg = {};
  	var ua = window.navigator.userAgent;
  	if(ua.indexOf("MSIE")>=1){
  		browserCfg.ie=true;
  	}else if(ua.indexOf("Firefox")>=1){  	
    	browserCfg.firefox = true;
    }else if(ua.indexOf("Chrome")>=1){
      browserCfg.chrome = true;
    }
    var filesize = 0;
    try{
    	var obj_file = _file;//document.getElementById(_file);
    	if(obj_file==null || obj_file.value=="" || obj_file.value==null){
    		return;
    	}
    	if(browserCfg.firefox || browserCfg.chrome){
    		filesize = Math.round(obj_file.files[0].size*100/1024)/100;
    	}else if(browserCfg.ie){
    	   var filePath = obj_file.value;      
    	   var fileSystem = new ActiveXObject("Scripting.FileSystemObject");         
    	   var file = fileSystem.GetFile(filePath);      
    	   filesize = Math.round(file.Size*100/1024)/100;
    	}else{
    	  filesize = 0;//"浏览器不支持计算上传文件大小";
      }
    }catch(e){
      filesize = 0;//"计算上传文件大小失败"+e;
    }
    if(filesize>0 && filesize<1024){
    	displayFileSize = "["+filesize+"k]";
    }else{
    	if(filesize == 0){
    		displayFileSize = "";
    	}else{
    		displayFileSize = "["+Math.round(filesize*100/1024)/100+"M]";
    	}
    }
    return displayFileSize;
  };
}); // each element	
};
	
	/*--------------------------------------------------------*/
	
	/*
		### Core functionality and API ###
	*/
	$.extend($.fn.MultiFile, {
  /**
   * This method removes all selected files
   *
   * Returns a jQuery collection of all affected elements.
   *
   * @name reset
   * @type jQuery
   * @cat Plugins/MultiFile
   * @author Diego A. (http://www.fyneworks.com/)
   *
   * @example $.fn.MultiFile.reset();
   */
  reset: function(){
			var settings = $(this).data('MultiFile');
			//if(settings) settings.wrapper.find('a.MultiFile-remove').click();
			if(settings) settings.list.find('a.MultiFile-remove').click();
   return $(this);
  },
  
  
  /**
   * This utility makes it easy to disable all 'empty' file elements in the document before submitting a form.
   * It marks the affected elements so they can be easily re-enabled after the form submission or validation.
   *
   * Returns a jQuery collection of all affected elements.
   *
   * @name disableEmpty
   * @type jQuery
   * @cat Plugins/MultiFile
   * @author Diego A. (http://www.fyneworks.com/)
   *
   * @example $.fn.MultiFile.disableEmpty();
   * @param String class (optional) A string specifying a class to be applied to all affected elements - Default: 'mfD'.
   */
  disableEmpty: function(klass){ klass = (typeof(klass)=='string'?klass:'')||'mfD';
   var o = [];
   $('input:file.MultiFile').each(function(){ if($(this).val()=='') o[o.length] = this; });
   return $(o).each(function(){ this.disabled = true;}).addClass(klass);
  },
  
  
		/**
			* This method re-enables 'empty' file elements that were disabled (and marked) with the $.fn.MultiFile.disableEmpty method.
			*
			* Returns a jQuery collection of all affected elements.
			*
			* @name reEnableEmpty
			* @type jQuery
			* @cat Plugins/MultiFile
			* @author Diego A. (http://www.fyneworks.com/)
			*
			* @example $.fn.MultiFile.reEnableEmpty();
			* @param String klass (optional) A string specifying the class that was used to mark affected elements - Default: 'mfD'.
			*/
  reEnableEmpty: function(klass){ klass = (typeof(klass)=='string'?klass:'')||'mfD';
   return $('input:file.'+klass).removeClass(klass).each(function(){ this.disabled = false; });
  },
  
		/**
			* This method will intercept other jQuery plugins and disable empty file input elements prior to form submission
			*
	
			* @name intercept
			* @cat Plugins/MultiFile
			* @author Diego A. (http://www.fyneworks.com/)
			*
			* @example $.fn.MultiFile.intercept();
			* @param Array methods (optional) Array of method names to be intercepted
			*/
  intercepted: {},
  intercept: function(methods, context, args){
   var method, value; args = args || [];
   if(args.constructor.toString().indexOf("Array")<0) args = [ args ];
   if(typeof(methods)=='function'){
    $.fn.MultiFile.disableEmpty();
    value = methods.apply(context || window, args);
				//SEE-http://code.google.com/p/jquery-multifile-plugin/issues/detail?id=27
				setTimeout(function(){ $.fn.MultiFile.reEnableEmpty(); },1000);
    return value;
   };
   if(methods.constructor.toString().indexOf("Array")<0) methods = [methods];
   for(var i=0;i<methods.length;i++){
    method = methods[i]+''; // make sure that we have a STRING
    if(method) (function(method){ // make sure that method is ISOLATED for the interception
     $.fn.MultiFile.intercepted[method] = $.fn[method] || function(){};
     $.fn[method] = function(){
      $.fn.MultiFile.disableEmpty();
      if($.fn.MultiFile.intercepted[method] != null){
    	  value = $.fn.MultiFile.intercepted[method].apply(this, arguments);
			//SEE http://code.google.com/p/jquery-multifile-plugin/issues/detail?id=27
    	  setTimeout(function(){ $.fn.MultiFile.reEnableEmpty(); },1000);
    	  return value;
      }else{
    	  $.fn.MultiFile.reEnableEmpty();
    	  return '';
      }
     
     }; // interception
    })(method); // MAKE SURE THAT method IS ISOLATED for the interception
   };// for each method
  } // $.fn.MultiFile.intercept
		
 });
	
	/*--------------------------------------------------------*/
	
	/*
		### Default Settings ###
		eg.: You can override default control like this:
		$.fn.MultiFile.options.accept = 'gif|jpg';
	*/
	$.fn.MultiFile.options = { //$.extend($.fn.MultiFile, { options: {
		accept: '', // accepted file extensions
		max: -1,    // maximum number of selectable files
		
		// name to use for newly created elements
		namePattern: '$name', // same name by default (which creates an array)
         /*master name*/ // use $name
         /*master id  */ // use $id
         /*group count*/ // use $g
         /*slave count*/ // use $i
									/*other      */ // use any combination of he above, eg.: $name_file$i
		
		// STRING: collection lets you show messages in different languages
		STRING: {
			remove:'删除',
			denied:'不能上传 $ext类型的文件 ，只能上传$doctype类型的文件',
			file:'$file',
			selected:'已选文件: $file',
			existsDuplicate:'$file已经存在，请勿重复添加相同附件',
			duplicate:'您已选择了$file，请勿重复添加相同附件'
		},
		
		// name of methods that should be automcatically intercepted so the plugin can disable
		// extra file elements that are empty before execution and automatically re-enable them afterwards
  autoIntercept: [ 'submit', 'ajaxSubmit', 'ajaxForm', 'validate', 'valid' /* array of methods to intercept */ ],
		
		// error handling function
		error: function(s){
			/*
			ERROR! blockUI is not currently working in IE
			if($.blockUI){
				$.blockUI({
					message: s.replace(/\n/gi,'<br/>'),
					css: { 
						border:'none', padding:'15px', size:'12.0pt',
						backgroundColor:'#900', color:'#fff',
						opacity:'.8','-webkit-border-radius': '10px','-moz-border-radius': '10px'
					}
				});
				window.setTimeout($.unblockUI, 2000);
			}
			else//{// save a byte!
			*/
			 alert(s);
			//}// save a byte!
		}
 }; //} });
	
	/*--------------------------------------------------------*/
	
	/*
		### Additional Methods ###
		Required functionality outside the plugin's scope
	*/
	
	// Native input reset method - because this alone doesn't always work: $(element).val('').attr('value', '')[0].value = '';
	$.fn.reset = function(){ return this.each(function(){ try{ this.reset(); }catch(e){} }); };
	
	/*--------------------------------------------------------*/
	
	/*
		### Default implementation ###
		The plugin will attach itself to file inputs
		with the class 'multi' when the page loads
	*/
	$(function(){
		setTimeout(function(){
			$("input[type=file].multi").MultiFile();
		},10);
	});
 };	
/*# AVOID COLLISIONS #*/
})(jQuery);
/*# AVOID COLLISIONS #*/


//附件工具类
function AttachUtil(baseUrl){
	//定义文件图标
	this.wrdImg = baseUrl+"images/file/word.jpg";
	this.xlsImg = baseUrl+"images/file/excel.jpg";
	this.zipImg = baseUrl+"images/file/zip.jpg";
	this.rarImg = baseUrl+"images/file/rar.jpg";
	this.exeImg = baseUrl+"images/file/exe.jpg";
	this.pptImg = baseUrl+"images/file/ppt.jpg";
	this.txtImg = baseUrl+"images/file/txt.jpg";
	this.bmpImg = baseUrl+"images/file/bmp.jpg";
	this.normalImg = baseUrl+"images/file/normal.jpg";
	this.htmlImg = baseUrl+"images/file/html.jpg";
	this.pdfImg = baseUrl+"images/file/pdf.jpg";
	this.jpgImg = baseUrl+"images/file/jpg.jpg";
	this.gifImg = baseUrl+"images/file/gif.jpg";
	this.aviImg = baseUrl+"images/file/avi.jpg";
	
	
	this.countImage = function(str){
		str = str.substr(str.lastIndexOf(".")+1);
		switch(str){
			case "xls":
				return this.xlsImg;
			case "xlsx":
				return this.xlsImg;
			case "doc":
				return this.wrdImg;
			case "docx":
				return this.wrdImg;
			case "txt":
				return this.txtImg;
			case "jpg":
				return this.jpgImg;
			case "gif":
				return this.gifImg;
			case "png":
				return this.jpgImg;
			case "pdf":
				return this.pdfImg;
			case "rar":
				return this.rarImg;
			case "zip":
				return this.zipImg;
			case "ppt":
				return this.pptImg;
			case "pptx":
				return this.pptImg;
			case "swf":
				return this.aviImg;
			case "bmp":
				return this.bmpImg;
			case "c":
				return this.normalImg;
			case "cpp":
				return this.normalImg;
			case "class":
				return this.normalImg;
			case "html":
				return this.htmlImg;
			case "htm":
				return this.htmlImg;
			default:
				return this.normalImg;
		}
	};
}

//附件显示内容
function createAttahContent(){
	 this.draw = function(attachId,bussinessId,businessTableName,IsSaveToDatabase,bindingTableFiled,addEnableFlag,attachNumber){
		 var attachNumberContent = "";
		 if(typeof(attachNumber)=='undefined' || attachNumber == -1 || attachNumber == 'undefined'){
			 
		 }else{
			 attachNumberContent = "限制上传"+attachNumber+"个附件";
		 }
		var addFileFlag='';
		var divTranstrant='';
		if(addEnableFlag != null && (addEnableFlag == "true" || addEnableFlag == true)){
			addFileFlag = "block";
		}else{
			addFileFlag = "none";
			divTranstrant = "filter:Alpha(opacity=0);opacity:0.0;";
		}
		var content = "<form id='"
		+attachId
		+"' action='' enctype='multipart/form-data'  method='POST' name='attach' style='margin:5px 20px'>"
		+" <input type='text' name='formId' id='formId' value='"
		+bussinessId
		+"' style='display:none'/>"
	    +" <input type = 'text' name='formCode' id='formCode' value='"
		+businessTableName
		+"' style='display:none'/>"
		+" <input type = 'text' name='saveType' id='saveType' value='"
		+IsSaveToDatabase
		+"' style='display:none'/>"
		+" <input type = 'text' name='formField' id='formField' value='"
		+bindingTableFiled
		+"' style='display:none'/>"
		+"  <div style='margin:5xp;'>"
		+"<div class='addAttache' style='display:block;"
		+divTranstrant
		+"'><FONT color=red style='position:absolute;margin-left:170px;'>"+attachNumberContent+"</FONT></div> "
		//+"<div style=' z-index: 1; top: 10px;position: absolute;'><input type='button' onclick='$('input[type=file]').last().click()' id='mybutton' style='display:block;' value='添加附件'/></div>"  filter:Alpha(opacity=0);opacity:0.0;
		+"<div style=' z-index: 2; top: 10px;position: absolute;'><input type='file'  name='files'  class='multi' style='display:"
		+addFileFlag
		+";width:60px;padding:0px 15px;magin:5px 30px;height:40px;filter:Alpha(opacity=0);opacity:0.0;'/></div>"
		+"   </div>"
		+"  </form>";
		return content;
	 };
}

function drawAttachPanel(baseUrl){
	
	this.getAttachPanel = function(attahDivId,fileuploadBusinessId, fileuploadBusinessTableName,fileType,openControl_flag,isDelete_flag,isDownload_flag,fileuploadIsSaveToDatabase,bindingTableFiled){
		var datas = "fileuploadBusinessId=" + fileuploadBusinessId 
					+ "&fileuploadBusinessTableName=" + fileuploadBusinessTableName 
					+ "&fileuploadIsSaveToDatabase=" + fileuploadIsSaveToDatabase 
					+ "&openControl_flag=" + openControl_flag 
					+"&isDelete_flag=" + isDelete_flag
					+"&isDownload_flag=" + isDownload_flag
					+"&webPath="+baseUrl
					+"&bindingTableFiled="+bindingTableFiled;
			$.ajax({
			type:"POST",
			url:  baseUrl+'platform/jqueryMutilFile/getContent',
			data: datas,
			dataType: 'text',
			success: function(msg){
				$("#"+attahDivId+"_list").append(msg);
			},
			error : function(msg){
				//$(".MultiFile-list").append(msg);
				$("#"+attahDivId+"_list").append(msg);
			}});
	};
	
	$jqeuryDeleteFile = function(fileName,fileuploadBusinessId,fileuploadBusinessTableName,fileuploadIsSaveToDatabase,isDelete_flag,pkId){
		if(confirm('确定要删除【'+ fileName +'】附件文件吗？')){
			var datas = "" 
			+"&fileuploadBusinessId=" + fileuploadBusinessId 
			+"&fileuploadBusinessTableName=" + fileuploadBusinessTableName 
			+"&fileName=" + fileName 
			+"&fileuploadIsSaveToDatabase=" + fileuploadIsSaveToDatabase 
			+"&isDelete_flag=" + isDelete_flag
			+"&pkId=" + pkId;
			$.ajax({
				type:"POST",
				url:  baseUrl+'platform/jqueryMutilFile/deletefile',
				data: datas,
				dataType: 'text',
				success: function(msg){
					$("#"+msg).css("display","none");
				},
				error : function(msg){
				}});
		}
		};
		
		$jqeuryGetFileNames = function(fileuploadBusinessId,fileuploadBusinessTableName,bindingTableFiled){
			var datas = "fileuploadBusinessId=" + fileuploadBusinessId 
			+"&fileuploadBusinessTableName=" + fileuploadBusinessTableName
			+"&bindingTableFiled=" + bindingTableFiled;
			var $fileNames="";
		    $.ajax({
				type:"POST",
				async : false,
				url:  baseUrl+'platform/jqueryMutilFile/getFileNames',
				data: datas,
				dataType: 'text',
				success: function(msg){
					$fileNames = msg;
				},
				error : function(msg){
					
				}});
		    return $fileNames;
		};
		
		$inArray = function (value,array){    
		    if(typeof value=="string"||typeof value=="number"){    
		        for(var i in array){    
		            if(value===array[i]){    
		                return true;    
		            }    
		        }    
		        return false;    
		    }    
		};   
}
