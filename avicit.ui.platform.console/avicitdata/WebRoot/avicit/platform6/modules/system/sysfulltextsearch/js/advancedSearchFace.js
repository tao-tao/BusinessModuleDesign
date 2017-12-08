/**
 * 高级检索需要的js, 高级检索(adSearch)搜索框和iframe中的搜索结果都需要引用该js
 * 需要引入和searchUtil.js 这里不重复引用了
 * 需要引入easyUI相关的js
 * 一些硬编码的写法实现了功能，性能也不差，但不很优雅，考虑是否优化。
 * @author wangjia
 */

/************************ 高级检索关键词输入框相关 start********************/
var adSearchKeywordTip = "--请输入--";
function adSearchKeywordFocus(obj) {
    if(obj.value == adSearchKeywordTip){
            obj.value = "";
     }
}

function adSearchKeywordBlur(obj) {
    if(obj.value == ""){
       obj.value = adSearchKeywordTip;
    }
}
/************************ 高级检索关键词输入框相关 end********************/

/************************ 增删高级检索条件相关 start********************/
// 清空当前条件
function cleanCondition(index) {
	var adKeyword  = document.getElementById("adKeyword" + index); if(null == adKeyword || 'undefined' == typeof(adKeyword)) return;
	adKeyword.value = "";
	
	var logic  = document.getElementById("logic" + index); if(null == adKeyword || 'undefined' == typeof(adKeyword)) return;
	logic.value = "OR";
	
	var field  = document.getElementById("field" + index); if(null == adKeyword || 'undefined' == typeof(adKeyword)) return;
	field.value = "ALL";
	
	var condition  = document.getElementById("condition" + index); if(null == adKeyword || 'undefined' == typeof(adKeyword)) return;
	condition.value = "IN";	
}

// 添加一条当前条件
function addAdSearchCondition(index) {
	var next = index + 1;
	showElement("complexCondition" + next);
	hideElement("conditionAdd" + index);
	hideElement("conditionDelete" + index);
}

//删除一条当前条件
function deleteAdSearchCondition(index) {
    var previous = index - 1;
    hideElement("complexCondition" + index);
    showElement("conditionAdd" + previous);
    showElement("conditionDelete" + previous);
    cleanCondition(index);
}
/************************ 增删高级检索条件相关 end********************/

/************************ 复制和提交检索条件相关 start********************/
// 复制全部检索条件 
function copyAdSearchCondition() {
        // TODO 这里先用笨方法写，以后再考虑是否优化
	    sysAdvancedSearchForm.field0.value = document.getElementById('field0').value;
	    sysAdvancedSearchForm.condition0.value = document.getElementById('condition0').value;
	    sysAdvancedSearchForm.keyword0.value = document.getElementById('adKeyword0').value;
	    if(adSearchKeywordTip == sysAdvancedSearchForm.keyword0.value) sysAdvancedSearchForm.keyword0.value = "";
	    
		sysAdvancedSearchForm.logic1.value =  document.getElementById('logic1').value;
		sysAdvancedSearchForm.field1.value =  document.getElementById('field1').value;
	    sysAdvancedSearchForm.condition1.value = document.getElementById('condition1').value;
	    sysAdvancedSearchForm.keyword1.value = document.getElementById('adKeyword1').value;
	    if(adSearchKeywordTip == sysAdvancedSearchForm.keyword1.value) sysAdvancedSearchForm.keyword1.value = "";
	    
	    sysAdvancedSearchForm.logic2.value =  document.getElementById('logic2').value;
		sysAdvancedSearchForm.field2.value =  document.getElementById('field2').value;
	    sysAdvancedSearchForm.condition2.value = document.getElementById('condition2').value;
	    sysAdvancedSearchForm.keyword2.value = document.getElementById('adKeyword2').value;
	    if(adSearchKeywordTip == sysAdvancedSearchForm.keyword2.value) sysAdvancedSearchForm.keyword2.value = "";
	    
		sysAdvancedSearchForm.logic3.value =  document.getElementById('logic3').value;
		sysAdvancedSearchForm.field3.value =  document.getElementById('field3').value;
	    sysAdvancedSearchForm.condition3.value = document.getElementById('condition3').value;
	    sysAdvancedSearchForm.keyword3.value = document.getElementById('adKeyword3').value;
	    if(adSearchKeywordTip == sysAdvancedSearchForm.keyword3.value) sysAdvancedSearchForm.keyword3.value = "";
	    
		sysAdvancedSearchForm.logic4.value =  document.getElementById('logic4').value;
		sysAdvancedSearchForm.field4.value =  document.getElementById('field4').value;
	    sysAdvancedSearchForm.condition4.value = document.getElementById('condition4').value;
	    sysAdvancedSearchForm.keyword4.value = document.getElementById('adKeyword4').value;
	    if(adSearchKeywordTip == sysAdvancedSearchForm.keyword4.value) sysAdvancedSearchForm.keyword4.value = "";
	    
	    sysAdvancedSearchForm.secretLevel.value = document.getElementById('secretLevelForAd').value; // 拷贝secret level
	    sysAdvancedSearchForm.time.value = document.getElementById('timeForAd').value; // 拷贝时间	
	    
	    sysAdvancedSearchForm.startTime.value = $('#startTime').datebox('getValue'); // 开始时间
	    sysAdvancedSearchForm.endTime.value = $('#endTime').datebox('getValue');     // 结束时间
	    
	    sysAdvancedSearchForm.pageSize.value = document.getElementById('pageSize').value;   
	    sysAdvancedSearchForm.pageIndex.value = document.getElementById('pageIndex').value; 	    
}

// 条件为空，返回false
function validateNullCondition(keyword) {
	if(null == keyword || 'string' != typeof(keyword) || "" == keyword.Trim()) return false;
	return true;
}

// 条件为空，返回-1
// 条件不为空，为IN条件 返回1
// 条件不为空，为NOT条件，返回0
function validateNotCondition(keyword, condition) {
	if(!validateNullCondition(keyword)) return -1;
	if("IN" == condition) return 1;
	if("NOT" == condition) return 0;
}

// validate 检验高级检索条件是否符合要求
function checkAdCondition() {
	if (!validateNullCondition(sysAdvancedSearchForm.keyword0.value)
			&& !validateNullCondition(sysAdvancedSearchForm.keyword1.value)
			&& !validateNullCondition(sysAdvancedSearchForm.keyword2.value)
			&& !validateNullCondition(sysAdvancedSearchForm.keyword3.value)
			&& !validateNullCondition(sysAdvancedSearchForm.keyword4.value) )
		return "请输入查询条件";
    
	if (  
		 !(	1 == validateNotCondition(sysAdvancedSearchForm.keyword0.value, sysAdvancedSearchForm.condition0.value)
		  ||1 == validateNotCondition(sysAdvancedSearchForm.keyword1.value, sysAdvancedSearchForm.condition1.value)
			||1 == validateNotCondition(sysAdvancedSearchForm.keyword2.value, sysAdvancedSearchForm.condition2.value)
		  ||1 == validateNotCondition(sysAdvancedSearchForm.keyword3.value, sysAdvancedSearchForm.condition3.value)
			||1 == validateNotCondition(sysAdvancedSearchForm.keyword4.value, sysAdvancedSearchForm.condition4.value)
		  )
		)
		return "所有条件均为'不包含',将查不出任何结果";
	
	var startTime = sysAdvancedSearchForm.startTime.value;
	var endTime = sysAdvancedSearchForm.endTime.value;
	if(startTime && endTime && startTime > endTime) 
		return "开始时间必须早于或等于结束时间";
	
	// 这里可以添加其他验证条件
	
	return null;  // 通过验证 
	
}

// 复制检索条件，如果检索条件符合要求，则执行高级检索操作
function doAdvancedSearch() {
	if(!sysAdvancedSearchForm) {  // 防止没有加载完全，再次获取一次
		advancedSearchResult = document.getElementById("advancedSearchResult");
		advancedSearchResultWin = advancedSearchResult.contentWindow;
		advancedSearchForm = advancedSearchResultWin.document.getElementById("sysAdvancedSearchForm");
	}
	copyAdSearchCondition();
	var tip = checkAdCondition();
	if(null != tip) {
		try{      
			$.messager.alert('提示', tip, 'info');
		}catch(e) {
			alert(tip);
		}	
	} else sysAdvancedSearchForm.submit();
}

function doFirstAdSearch() {
	document.getElementById("pageIndex").value = 1;
	document.getElementById("pageSize").value = 5;
	doAdvancedSearch();
}
/************************ 复制和提交检索条件相关 end********************/