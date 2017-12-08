var IdeaManage = {};
IdeaManage.IdeaDiv = null;
IdeaManage.getIdea = function (entryId){
	
	var url = "platform/bpm/ideaaction/getIdea.json";
	var contextPath = getPath();
	var urltranslated = contextPath + "/" + url;

	if(entryId == 'undefined' || entryId == null){
		return ;
	}
	var _ideaDiv = document.getElementById("idea");
	if(_ideaDiv==null||_ideaDiv=="undefined"){
		return;
	}
	jQuery.ajax({
        type:"POST",
		data:"processInstanceId="+entryId,
        url: urltranslated,  
        dataType:"json",
		context: document.body, 
        success: function(msg){
        	IdeaManage.drawIdea(msg);
		},
		error: function(XMLHttpRequest, textStatus, errorThrown){
			window.alert("调用Ajax操作发生异常，地址为：" + urltranslated + "，异常信息为：" + errorThrown);
		}
	}); 
};

IdeaManage.drawIdea = function (msg){
	
	if(msg == null){
		return ;
	}
	var codes = msg.ideaCode;
	var titles = msg.titleMap;
	var ideas = msg.ideaMap;
	
	if(codes == null || titles == null || ideas==null){
		return ;
	}
	 
	IdeaManage.IdeaDiv = document.getElementById("idea");
	var cdiv = document.createElement("div");
	var ideaContent = "<table class='idea-table'>";
	//debugger;
	for (var code in codes){
		var title = "";
		var idea = "";
		for (var t in titles){
			if(codes[code] == t){
				title = titles[t];
				break;
			}
		}
		for (var i in ideas){
			if(codes[code] == i){
				idea = ideas[i];
				break;
			}
		}
		if(title == null || title == ""){
			continue;
		}
		ideaContent +="<tr>";
		ideaContent +="<td class='idea-table-title'>";
		ideaContent +=title;
		ideaContent +="</td>";
		ideaContent +="<td class='idea-table-idea'>";
		ideaContent +="<table border='0'><tr><td >";
		for (var i=0;i<idea.length;i++){
			ideaContent +=idea[i];
			ideaContent +="</br>";
		}
		ideaContent +="</td></tr></table>";
		ideaContent +="</td>";
		ideaContent +="</tr>";
	}
	ideaContent +="</table>";
	cdiv.innerHTML = ideaContent;
	IdeaManage.IdeaDiv.appendChild(cdiv); 
};
