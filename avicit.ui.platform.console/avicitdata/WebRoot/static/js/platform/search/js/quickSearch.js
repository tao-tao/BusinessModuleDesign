
/**
 * 主函数，绑定onkeyup事件。选择调用全文检索或敏捷查询
 */
var index = 0;            // 当前选中的敏捷搜索结果编号，初始化为第一条结果的前一条
var count = 0;           // 检索结果计数，即本次搜索结果条数
var hasResult = 0;   // 当前的查询是否有结果

function prepareToSearch(fulltextSearchPath, agileSearchPath) {
	
	var agileSearchBox = $('#subjectSearch');              // 获得敏捷搜索框
    var agileSearchTip = $('#agileSearchResult');        // 获得敏捷搜索提示框（div）
  
	agileSearchBox.bind('keyup', function(event) {       // 为敏捷搜索框绑定onkeyup事件

		if ("38" != event.keyCode && "40" != event.keyCode) { // 非上下键
			
			if ("13" != event.keyCode) {                     // 敲击按键（非回车）
				var keywords = $.trim(agileSearchBox.val()); // 获得查询关键词，去空格
				if ("" == keywords) {                        // 关键词为空，focus
					index = 0;
					agileSearchBox.focus();
					agileSearchTip.css('display', 'none');
				} else {                                     // 关键词不为空，使用timer执行敏捷检索
					index = 0;
					beforeAjax(agileSearchPath, keywords);
				}
	
			} else {                                         // 回车事件，在未选中某条结果时会调用全文检索
				agileSearchTip.css('display', 'none');       // 调用全文（敏捷）检索前，敏捷检索Tip变为不显示的状态。
				var keywords = $.trim(agileSearchBox.val()); // 获得查询关键词，去空格
				if ("" == keywords) {                        // 关键词为空，focus
					index = 0;
					agileSearchBox.focus();
					agileSearchTip.css('display', 'none');
				} else {                                     // 关键词不为空，执行全文检索（敏捷）操作
					if(index >= 1 && index <= count) {       // 有敏捷结果被选中，弹出对应结果的详情页面
						var currentResult = $('#agile_search_tip_header').find("tr:eq(" + index + ")");
						var title = currentResult.attr('id');
						var path = currentResult.attr('title');
						addTab(title, path, '', 'agileSearchResult', '0px 0px');
						index = 0;
					}else {                                  // 没有敏捷搜索结果被选中，调用全文检索
						index = 0;
						agileSearchTip.css('display', 'none');
						fullTextSearch(fulltextSearchPath, keywords);   
					}
				}
			}
			
		} else {                                             //上下键, 通过点击上下键选中一条结果
			 
			if(0 == hasResult){
				return;
			}
			
			var trs = $('#agile_search_tip_header').find("tr");
			trs.css('background', '');
			
		    if ($.trim($(this).val()).length == 0) {                // 搜索框为空，下拉提示框不显示；这句写的似乎有些重复；只是为了确保显示不出问题
		    	$('#agileSearchResult').css('display', 'none');
		        return;  
		    }else if(0 == hasResult){
		    	$('#agileSearchResult').css('display', 'none');
		    	return;
		    }
		    
		    var key = event.keyCode;                     // 获得按键编码
		    
		    // 计算"下标"的值
		    if (38 == key) {                             // 向上按钮  
		        index--;  
		        if (0 == index) index = count;
		    } else if (40 == key) {                      // 向下按钮  
		        index++;
		        if (count + 1 == index) index = 1;
		    } else {
		    	index = 0;
		    }
		    
			var trs = $('#agile_search_tip_header').find("tr:eq(0)");
			trs.css('background', '');
			if(index > 0 && index <= count) {
				var currentLink = $('#agile_search_tip_header').find("tr:eq(" + index + ")");
				currentLink.css('background', '#99ccff');
			}
		}
		
	});
}

/**
 * 直接调用后台全文搜索方法，从全部索引中搜索
 * 	addTab(this.innText, this.title, '', 'agileSearchResult', '0px 0px');
 */
function fullTextSearch(fulltextSearchPath, keywords) {
	keywords = encodeURIComponent(keywords);
	keywords = encodeURIComponent(keywords);
	//alert(keywords);
	//alert(keywords);
	addTab('搜索首页', fulltextSearchPath + '?keywords=' + keywords
			+ '&isFromAgile=1', '/images/search.png', 'sysSearchResult',
			'0px 0px');
}

/**
 * 延时的方法，而不是每次onkeyup都调用agileSearch方法
 */
var timer;
var actInput; // keywords
var actPath;  // agileSearchPath
function beforeAjax(agileSearchPath, keywords) {
	index = 0;
	actInput = keywords;
	actPath = agileSearchPath;
	if (timer)  window.clearTimeout(timer);
	timer = window.setTimeout("agileSearch(actPath, actInput)", 500);    // 这个延时参数放到配置文件里?																	
}

/**
 * Ajax调用敏捷检索方法
 */
function agileSearch(agileSearchPath, keywords) {
	
	var agileSearchTip = $('#agileSearchResult');                 // 获得敏捷搜索提示框（div）
	agileSearchTip.css('display', 'none');                        // 执行敏捷查询之前，先把上一次的结果作为不显示的状态
	index = 0;                                                   // 重置当前选中的敏捷检索结果编号
	
	$.ajax({
		type : "POST",                                            // 使用post方法访问后台
		dataType : "json",                                        // 返回json格式的数据
		// url: agileSearchPath,                                  // 要访问的后台地址
		url : agileSearchPath + ".json",                          // 要访问的后台地址，转为json格式，这里返回的结果和上面的一样
		data : "keywords=" + keywords,
		complete : function() {
		}, 
		success : function(result) {                              // result为返回的数据，在这里由SpringMVC返回包含结果集合的hashMap和搜索条件searchCondition

			if(result != null && "undefined" != typeof(result) && null != result.hashMap &&  "undefined" != typeof(result.hashMap)) {
				hasResult = 1;
				showTip(result);                                  // 解析结果，并放到敏捷提示栏中
			}else {
				hasResult = 0;
			}
		}
	});
}

/**
 * 解析返回的数据，以适当的格式放到前端页面敏捷提示栏中
 */
function showTip(result) {

	if (null == result || "undefined" == typeof(result) || null == result.hashMap ||  "undefined" == typeof(result.hashMap)) {
		return;
	}
	
	/*极其繁琐的拼html，维护修改时小心不要出错哦！  开头部分  start*/
	var html2 =  
        "<div id='agile_search_tip_container' style='width:250px;border:0;'>"
	    +     "<div id='agile_search_tip_header' style='width:100%;border:0;overflow-y:auto;overflow-x:hidden;'>"
	    +		  "<table id='agile_search_tip_table' border='0' width='100%'>"
	/*极其繁琐的拼html，维护修改时小心不要出错哦！  开头部分  end*/
	    
	count = 0;                                              //计数器置0
	/*极其繁琐的拼html，维护修改时小心不要出错哦！  中间部分  end*/
	$.each(result.hashMap, function(key, values) {
		var key = key;                                      //看起来清楚一点
		html2 += "<tr>";
//		html2 += 	  "<td width='10px' bgcolor='#F1F5FB'>"
//			    +     	   "<span class='verticalStyle'><font size='2'>" + key + "</font></span>"
//			    +	  "</td>"
		html2   +=     "<td>"
			    +           "<table class='warp_table' border='0' width='100%'>";
		$.each(values, function(pos, value){
			count ++;                                           //出一条结果，计数器加一
			var title; var path; var content; var detail;
			if(null != value) {
				title = value.title;      if(null == title)    title = '';
				path = value.path;        if(null == path)     path = '';
				content = value.content;  if(null == content)  content = '';
				detail = value.detail;    if(null == detail)   detail = '';
			}   
			html2 +=                "<tr id='" + title + "' title='" + path +"' style='cursor:pointer;' onmouseover=\"$onMouserOver(this);\" onmouseout=\"$onMouserOut(this);\" onclick = \"addTab('" + title + "','" + path + "', '', 'agileSearchResult', '0px 0px');\"><td valign='top' class='searchContextIcon'>&nbsp;&nbsp;&nbsp;</td><td nowrap ><font size='2'>";
			html2 +=                cut_str(title, 10);
			html2 +=                "</font><br />";
			html2 +=                "<font size='1'>";
			html2 +=                cut_str(content, 21);
			html2 +=                "</font></tr></td>";
		});
		html2 +=            "</table>";
		html2 +=      "</td>";
		html2 += "</tr>";
		
	});
	/*极其繁琐的拼html，维护修改时小心不要出错哦！  中间部分  end*/
	
	/*极其繁琐的拼html，维护修改时小心不要出错哦！ 结尾部分  start*/
    html2 += 
    	          "</table>"
	    +     "</div>"
	    +     "<div id='agile_search_tip_footer' class='agile_search_tip_footer'>"
	    +		   "<span id='full_text_search_link'>&nbsp;&nbsp;&nbsp;&nbsp;更多</span>"
        +     "</div>"
        +"</div>"
    /*极其繁琐的拼html，维护修改时小心不要出错哦！  结尾部分  end*/
        
	// 填入提示信息
	var agileSearchResult = $("#agileSearchResult").empty().append(html2);
	
    // 计算Tip的位置
	var searchBox = $("#subjectSearch");
	var left = searchBox.position().left + searchBox.width() - agileSearchResult.width() + 5;
	var top = searchBox.position().top + searchBox.height() + 62;
	agileSearchResult.css("left", left);
	agileSearchResult.css("top", top);
	
	var keywords = $.trim($('#subjectSearch').val());    // 这两句修正个一小bug: 若空，则不显示。（延时器会有滞后，快速退格，可能显示出刚才输入文字的搜索结果）
	if("" ==keywords) return false;
	
	if(1 == hasResult) {
		agileSearchResult.show();                        // 让提示框为可见
		agileSearchResult.css('display', 'block');
	}
	/* 
	 * 若结果条数大于这个数字，则把显示框高度设定，滚动条生效。考虑到IE6下的兼容性，没有采用在SCC中设定Max-Height属性的方法。
	 * 
	 * 由于检索出的各条数据内容多少不同，单条数据占据的高度不定：
	 * 若count的限定太大，有可能出现搜索出很多条结果（单条结果平均高度又比较大），而没有启用高度限定，导致提示框占据空间太大的情况；
	 * 若提示框高度限定太大，在单条结果平均高度又比较小的情况下，则可能出现搜索框下面有一片空白的情况；
	 * 保险起见，count和 高度框设定都小一些，而这样的话，搜索框通常会显示的偏小一些。
	 * 
	 * TODO 下面这些数字需要做成可配置的?根据具体情况来调整。
	 */
	/*
	if(count >= 4) {
		var agileSearchResult = $('#agileSearchResult');
		agileSearchResult.css('height','210px');
		var agile_search_tip_container = $('#agile_search_tip_container');
		agile_search_tip_container.css('height','210px');
		var agile_search_tip_header = $('#agile_search_tip_header');
		agile_search_tip_header.css('height','190px');
	}
	*/
	
	// 点击“更多”，实际是进入全文检索，这个时间的绑定必须在显示tip的div填入html之后，如果在一开始就加上这段代码，会取不到页面元素，从而失效。
    var full_text_search_link = $('#full_text_search_link');         
    full_text_search_link.click(function() {
    	var agileSearchBox = $('#subjectSearch');        // 获得敏捷搜索框
		var keywords = $.trim(agileSearchBox.val());     // 获得查询关键词，去空格
		if ("" == keywords) {                            // 关键词为空，focus
			agileSearchBox.focus();
		} else {                                         // 关键词不为空，执行全文检索操作
			fullTextSearch(fulltextSearchPath, keywords);
		}
    });
    
    /*
     * 绑定“点击鼠标，tip框消失”事件。 
     * 这里用mouseout不起作用（但div里面只放"hello world"的时候是好使的, 换agile_search_tip_container也不行）。
     */
    var agileSearchTip = $('#agileSearchResult');                      // 获得敏捷搜索提示框（div）
    agileSearchTip.click(function() {
    	agileSearchTip.css('display', 'none');
    });
}

/**
 *  摘要： 截取字符串的前一段
 */
function cut_str(sourcestr, cutlength) {
	var returnstr = '';
	if(null != sourcestr && "undefined" != typeof(sourcestr)) {
			if(sourcestr.length > cutlength)
				returnstr = sourcestr.substring(0, cutlength-6) + "......";
			else
				returnstr = sourcestr;
	}
	return returnstr;
}

/**
 * 鼠标经过时改变背景色 
 */
$onMouserOver = function(obj){
	obj.style.background = "#99ccff";
}
$onMouserOut = function(obj){
	obj.style.background = "";
}


/**
 * 主函数, 包含该文件即执行, 即拥有快速搜索功能, 
 * 不需要在主页上另行修改, 快速搜索和主页完全分离
 */
var fulltextSearchPath = "platform/search/search.html";
var agileSearchPath = "platform/sysAgileSearch/agileSeracherAction/agileSearchTip";

function mainOfQuickSearch() {  //敏捷搜索框进入可搜索模式，暂时不需要做成开关。
		prepareToSearch(fulltextSearchPath, agileSearchPath);
}

// 执行,这里执行很慢, 优先级降低, 不如依然加在body的onload事件里
//mainOfQuickSearch();

