var eventToolbar = {
		getBranchLength : function(){
			if(dataJson.nextTask == null){
				return 0;
			}
			return dataJson.nextTask.length;
		},
		//为toolbar添加强制表态radio
		eventOfToolbarAppendIdeaCompelMannerRadio : function(){
			if(conditions.isIdeaCompelManner() && type == 'dosubmit'){
				$("#toolbar").toolbar('enable','强制表态');
				$("#ideaCompelMannerToolbar").css("display",'');
				$('#ideaCompelMannerToolbar .icon-ideas-lock').html('<div style="float:left;">强制表态</div><div style=\"display:block;float:left;border:0px solid red;margin-top:-3px;\"><label><input type=\"radio\" name=\"ideaCompelMannerRadioGroup\" value=\"yes\" >同意</label> <label><input type=\"radio\" name=\"ideaCompelMannerRadioGroup\" value=\"no\">不同意</label></div>');
			} else {
				$("#toolbar").toolbar('disabled','强制表态');
				$("#ideaCompelMannerToolbar").css("display",'none');
			}
		}
};
/**
 * 绘制idea Toolbar
 */
function drawIdeasToolbar(){
	$('#toolbar').toolbar({
		items:[{
			id : 'selectCommonIdearToolbar',
			iconCls : 'icon-ideas',
			disabled : false,
			text : '常用意见'
		},"-",{
			id : 'saveCommonIdear',
			text : '保存到常用意见',
			iconCls : 'icon-save',
			handler : function(){
				var ideasValue = $("#textAreaIdeas").val();
				if(ideasValue != ''){
					saveIdeas(ideasValue);
				} else {
					$.messager.alert('提示','意见为空!','error');
				}
			}
		},"-",{
			id : 'ideaCompelMannerToolbar',
			iconCls : 'icon-ideas-lock',
			disabled : false,
			text : '强制表态'
		}]
	});
}
/**
 * 绘制强制表态
 * @param branchNo
 */
function drawIdeaCompelManner(branchNo){
	if(dataJson.nextTask[branchNo].currentActivityAttr && dataJson.nextTask[branchNo].currentActivityAttr.userSelectType && dataJson.nextTask[branchNo].currentActivityAttr.ideaCompelManner == 'yes'){
		$('#ideaCompelMannerRadioGroup').appendTo($('#toolbar'));//动态追加"是否强制表态" radio group 
	}
}

/**
 * 保存意见
 */
function saveIdeas(ideasValue){
	$.ajax({
		   type: "POST",
		   url: getPath() + '/platform/user/bpmSelectUserAction/savePersonCommonIdear',
		   async: false,
		   data: 'idears=' + ideasValue,
		   dataType: 'text',
		   success: function(msg){
			   $.messager.show({
					title:'提示',
					msg:'保存成功!',
					timeout:1000,
					showType:'slide'
				}); 
		   },
		   error : function(msg){
			   $.messager.alert('错误',msg,'error');
		   }
	});
}
$(function(){
	drawIdeasToolbar();
//	从cookie中获取数据到指定的component中
	setTimeout(function(){
		//加载历史意见记录
		try{
			var idearHistoryKey = settings.saveToCookieKey;
			var idearHistoryVal = $.cookie(idearHistoryKey);
			$('#textAreaIdeas').val(idearHistoryVal);
		}catch(e){
			
		}
		// 为常用意见button绑定menu
		$('#selectCommonIdearToolbar .l-btn-left').attr('class','easyui-splitbutton').menubutton({});
		$('#selectCommonIdearToolbar .l-btn-left').click(function(){
			getCommonIdear();
			var height = $(this).height();
			var top = $(this).offset().top;
			var left = $(this).offset().left + ($(this).width() /2) - ($('#shareit-box').width() / 2);		
			$('#shareit-header').height('1');
			$('#shareit-box').show();
			$('#shareit-box').css({'top':top, 'left':'13'});
		});
	}, 300);
	
	eventToolbar.eventOfToolbarAppendIdeaCompelMannerRadio();
	
	$('#shareit-box').mouseleave(function(){
		$(this).hide();
	});
	
	$("input[name='ideaCompelMannerRadioGroup']").click(function(){
		$("#ideaCompelManner").val(this.value);
	});
});
function setTextareaVal(value){
	//定义常用意见menu
	$("#textAreaIdeas").val(value);
	$('#shareit-box').hide();
}
//从服务端获取常用意见
function getCommonIdear(){
	$.ajax({
		   type: "GET",
		   url: getPath() + '/platform/user/bpmSelectUserAction/getPersonCommonIdear?j=' + Math.random(),
		   async: false,
		   dataType: 'json',
		   success: function(msg){
			   $("#shareit-url").html('');
			   var commonIdearList = msg.customedImplList;
			   if(typeof(commonIdearList) != 'undefined' && commonIdearList.length > 0){
				   for(var i = 0 ; i < commonIdearList.length ; i++){
//					   $('#commonIdearsMenus').menu('appendItem', {
//							text: commonIdearList[i].value
//					   });
					   if(commonIdearList[i].value != null){
						   $("#shareit-url").append("<li onclick=\"setTextareaVal('" + commonIdearList[i].value + "')\">" + commonIdearList[i].value + "</li>");
					   }
				   }
			   } else {
				   $('#commonIdearsMenus').menu('appendItem', {
						text: '空'
					});
			   } 
		   },
		   error : function(msg){
//			   $.messager.alert('错误','获取常用意见列表失败!','error');
		   }
	});
}
function saveToCookieTip(){
	$.messager.show({
		title:'提示',
		msg:'保存成功!',
		timeout:1000,
		showType:'slide'
	});
}



