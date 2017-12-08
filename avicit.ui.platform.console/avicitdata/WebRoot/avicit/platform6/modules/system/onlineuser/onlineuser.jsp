<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<%@ page import="java.util.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="sec" uri="/WEB-INF/tags/shiro.tld"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>在线用户</title>
<base href="<%=ViewUtil.getRequestPath(request)%>">
<jsp:include page="/avicit/platform6/component/common/EasyUIJsInclude.jsp"></jsp:include>
<jsp:include page="/avicit/platform6/modules/system/commonpopup/commonSelectionHead.jsp"></jsp:include>
<script type="text/javascript">

var onlineView = $.extend({}, $.fn.datagrid.defaults.view, {    
	 /**  
    * 生成某一行数据  
    * @param  {DOM object} target   datagrid宿主table对应的DOM对象  
    * @param  {array} fields   datagrid的字段列表  
    * @param  {boolean} frozen   是否为冻结列  
    * @param  {number} rowIndex 行索引(从0开始)  
    * @param  {json object} rowData  某一行的数据  
    * @return {string}          单元格的拼接字符串  
    */  
   renderRow: function(target, fields, frozen, rowIndex, rowData) {   
       var opts = $.data(target, "datagrid").options;   
       //用于拼接字符串的数组   
       var cc = [];   
       if(frozen && opts.rownumbers) {   
           //rowIndex从0开始，而行号显示的时候是从1开始，所以这里要加1.   
           var rowNumber = rowIndex + 1;   
           //如果分页的话，根据页码和每页记录数重新设置行号   
           if(opts.pagination) {   
               rowNumber += (opts.pageNumber - 1) * opts.pageSize;   
           }   
           /**  
            * 先拼接行号列  
            * 注意DOM特征，用zenCoding可表达为"td.datagrid-td-rownumber>div.datagrid-cell-rownumber"  
            */  
           cc.push("<td class=\"datagrid-td-rownumber\"><div class=\"datagrid-cell-rownumber\">" + rowNumber + "</div></td>");   
       }   
       
      
       
       for(var i = 0; i < fields.length; i++) {   
           var field = fields[i];   
           var col = $(target).datagrid("getColumnOption", field);   
           if(col) {   
               var value = rowData[field];   
               //获取用户定义的单元格样式，入参包括：单元格值，当前行数据，当前行索引(从0开始)   
               var style = col.styler ? (col.styler(value, rowData, rowIndex) || "") : "";   
               //如果是隐藏列直接设置display为none，否则设置为用户想要的样式   
               var styler = col.hidden ? "style=\"display:none;" + style + "\"" : (style ? "style=\"" + style + "\"" : "");   
               cc.push("<td field=\"" + field + "\" " + styler + ">");   
               //如果当前列是datagrid组件保留的ck列时，则忽略掉用户定义的样式，即styler属性对datagrid自带的ck列是不起作用的。   
               if(col.checkbox) {   
                   var styler = "";   
               } else {   
                   var styler = "";   
                   //设置文字对齐属性   
                   if(col.align) {   
                       styler += "text-align:" + col.align + ";";   
                   }   
                   //设置文字超出td宽时是否自动换行(设置为自动换行的话会撑高单元格)   
                   if(!opts.nowrap) {   
                       styler += "white-space:normal;height:auto;";   
                   } else {   
                       /**  
                        * 并不是nowrap属性为true单元格就肯定不会被撑高，这还得看autoRowHeight属性的脸色  
                        * 当autoRowHeight属性为true的时候单元格的高度是根据单元格内容而定的，这种情况主要是用于表格里展示图片等媒体。  
                        */  
                       if(opts.autoRowHeight) {   
                           styler += "height:auto;";   
                       }   
                   }   
               }   
               //这个地方要特别注意，前面所拼接的styler属性并不是作用于td标签上，而是作用于td下的div标签上。   
               cc.push("<div style=\"" + styler + "\" ");   
               //如果是ck列，增加"datagrid-cell-check"样式类   
               if(col.checkbox) {   
                   cc.push("class=\"datagrid-cell-check ");   
               }   
               //如果是普通列，增加"datagrid-cell-check"样式类   
               else {   
                   cc.push("class=\"datagrid-cell " + col.cellClass);   
               }   
               cc.push("\">");   
               /**  
                * ck列光设置class是不够的，当突然还得append一个input进去才是真正的checkbox。此处未设置input的id，只设置了name属性。  
                * 我们注意到formatter属性对datagird自带的ck列同样不起作用。  
                */  
               if(col.checkbox) {   
                   cc.push("<input type=\"checkbox\" name=\"" + field + "\" value=\"" + (value != undefined ? value : "") + "\"/>");   
               }   
               //普通列   
               else {   
                   /**  
                    * 如果单元格有formatter，则将formatter后生成的DOM放到td>div里面  
                    * 换句话说，td>div就是如来佛祖的五指山，而formatter只是孙猴子而已，猴子再怎么变化翻跟头，始终在佛祖手里。  
                    */  
                   if(col.formatter) {   
                       cc.push(col.formatter(value, rowData, rowIndex));   
                   }   
                   //操，这是最简单的简况了，将值直接放到td>div里面。   
                   else {
                   	
	                   	if(field=='name')
	                   	{
	                   		//value="<a href='platform/sysuser/toEditJsp?sysUserId="+ rowData.userId +"'>"+value+"</a>";
	                   	//	value="<a onclick='showUserDetail(\""+rowData.userId+"\")' href='javascript: void(0);'>"+value+"</a>";
	                   	}
                   	
                   	
                       cc.push(value);   
                   }   
               }   
               cc.push("</div>");   
               cc.push("</td>");   
           }   
       }   
       
       
       //返回单元格字符串，注意这个函数内部并未把字符串放到文档流中。   
       
       //alert(cc.join(""));
       
       return cc.join("");   
   }
});   


function showUserDetail(sysUserId)
{
	var path="platform/sysuser/toEditJsp?sysUserId="+sysUserId;
	var usd = new CommonDialog("editUserDialog","700","435",path,"用户信息",false,true,false);
	usd.show();
}

var sessionView = $.extend({}, $.fn.datagrid.defaults.view, {    
	 /**  
    * 生成某一行数据  
    * @param  {DOM object} target   datagrid宿主table对应的DOM对象  
    * @param  {array} fields   datagrid的字段列表  
    * @param  {boolean} frozen   是否为冻结列  
    * @param  {number} rowIndex 行索引(从0开始)  
    * @param  {json object} rowData  某一行的数据  
    * @return {string}          单元格的拼接字符串  
    */  
   renderRow: function(target, fields, frozen, rowIndex, rowData) {   
       var opts = $.data(target, "datagrid").options;   
       //用于拼接字符串的数组   
       var cc = [];   
       if(frozen && opts.rownumbers) {   
           //rowIndex从0开始，而行号显示的时候是从1开始，所以这里要加1.   
           var rowNumber = rowIndex + 1;   
           //如果分页的话，根据页码和每页记录数重新设置行号   
           if(opts.pagination) {   
               rowNumber += (opts.pageNumber - 1) * opts.pageSize;   
           }   
           /**  
            * 先拼接行号列  
            * 注意DOM特征，用zenCoding可表达为"td.datagrid-td-rownumber>div.datagrid-cell-rownumber"  
            */  
           cc.push("<td class=\"datagrid-td-rownumber\"><div class=\"datagrid-cell-rownumber\">" + rowNumber + "</div></td>");   
       }   
       
      
       
       for(var i = 0; i < fields.length; i++) {   
           var field = fields[i];   
           var col = $(target).datagrid("getColumnOption", field);   
           if(col) {   
               var value = rowData[field];   
               //获取用户定义的单元格样式，入参包括：单元格值，当前行数据，当前行索引(从0开始)   
               var style = col.styler ? (col.styler(value, rowData, rowIndex) || "") : "";   
               //如果是隐藏列直接设置display为none，否则设置为用户想要的样式   
               var styler = col.hidden ? "style=\"display:none;" + style + "\"" : (style ? "style=\"" + style + "\"" : "");   
               cc.push("<td field=\"" + field + "\" " + styler + ">");   
               //如果当前列是datagrid组件保留的ck列时，则忽略掉用户定义的样式，即styler属性对datagrid自带的ck列是不起作用的。   
               if(col.checkbox) {   
                   var styler = "";   
               } else {   
                   var styler = "";   
                   //设置文字对齐属性   
                   if(col.align) {   
                       styler += "text-align:" + col.align + ";";   
                   }   
                   //设置文字超出td宽时是否自动换行(设置为自动换行的话会撑高单元格)   
                   if(!opts.nowrap) {   
                       styler += "white-space:normal;height:auto;";   
                   } else {   
                       /**  
                        * 并不是nowrap属性为true单元格就肯定不会被撑高，这还得看autoRowHeight属性的脸色  
                        * 当autoRowHeight属性为true的时候单元格的高度是根据单元格内容而定的，这种情况主要是用于表格里展示图片等媒体。  
                        */  
                       if(opts.autoRowHeight) {   
                           styler += "height:auto;";   
                       }   
                   }   
               }   
               //这个地方要特别注意，前面所拼接的styler属性并不是作用于td标签上，而是作用于td下的div标签上。   
               cc.push("<div style=\"" + styler + "\" ");   
               //如果是ck列，增加"datagrid-cell-check"样式类   
               if(col.checkbox) {   
                   cc.push("class=\"datagrid-cell-check ");   
               }   
               //如果是普通列，增加"datagrid-cell-check"样式类   
               else {   
                   cc.push("class=\"datagrid-cell " + col.cellClass);   
               }   
               cc.push("\">");   
               /**  
                * ck列光设置class是不够的，当突然还得append一个input进去才是真正的checkbox。此处未设置input的id，只设置了name属性。  
                * 我们注意到formatter属性对datagird自带的ck列同样不起作用。  
                */  
               if(col.checkbox) {   
                   cc.push("<input type=\"checkbox\" name=\"" + field + "\" value=\"" + (value != undefined ? value : "") + "\"/>");   
               }   
               //普通列   
               else {   
                   /**  
                    * 如果单元格有formatter，则将formatter后生成的DOM放到td>div里面  
                    * 换句话说，td>div就是如来佛祖的五指山，而formatter只是孙猴子而已，猴子再怎么变化翻跟头，始终在佛祖手里。  
                    */  
                   if(col.formatter) {   
                       cc.push(col.formatter(value, rowData, rowIndex));   
                   }   
                   //操，这是最简单的简况了，将值直接放到td>div里面。   
                   else {
                   	   
                	   if(field=="extra")
                	   {
                		   value="<a onclick='deleteSession(\""+rowData.sessionId+"\")' href='javascript: void(0);'>踢出</a>";
                	   }
                	   
                       cc.push(value);   
                   }   
               }   
               cc.push("</div>");   
               cc.push("</td>");   
           }   
       }   
       
       
       //返回单元格字符串，注意这个函数内部并未把字符串放到文档流中。   
       
       //alert(cc.join(""));
       
       return cc.join("");   
   }
});   

function deleteSession(sessionId)
{
	$.messager.confirm('请确认','确定禁止本session？',
		function(b){
			if(b){
				
												
				$.ajax({
					 url:'platform/onlineuser/onlineUserController/deleteSession.json',
					 data : {sessionId : sessionId},
					 type : 'post',
					 dataType : 'json',
					 success : function(data){
						 if(0==data.result){
							 $.messager.show({title : '提示',msg : '踢出成功'});
						 }else{
							 $.messager.alert('提示',data.msg,'warning');
						 }
						 
						
					 }
				 });
						
			}
		});
}


var path="<%=ViewUtil.getRequestPath(request)%>";
var timer;

$(document).ready(function(){ 
	
	loadOnlineInfo();
	$('#searchUserDialog').dialog('close');
	$('#timelyRefreshDialog').dialog("close");
	
	$('#searchUserForm').find('input').on('keyup',function(e){
		if(e.keyCode == 13){
			searchUser();
		}
	});
	
});



/**
 * 加载群组信息
 */
function loadOnlineInfo(){
	
	$("#dgOnline").datagrid("options").url ="platform/onlineuser/onlineUserController/getOnlineUserListByPage.json";
	$('#dgOnline').datagrid("reload", {});
			
};

function loadSearchOnlineInfo( userName, deptName, startTime, endTime)
{
	$("#dgOnline").datagrid("options").url ="platform/onlineuser/onlineUserController/getOnlineUserListByPage.json";
	$('#dgOnline').datagrid("reload", 
			{
				userName: userName, 
				deptName: deptName,
				startTime: startTime,
				endTime: endTime
			}
	);
	
};


function dgOnlineOnClickRow(index,rowData)
{
	// 获取群组中的用户列表
    loadSessionInfo(rowData.loginName);
	    
};



function dgOnlineOnLoadSuccess(data)
{
	//alert(data.total);
	
	$('#totalNum').text(data.total);
};

function refresh()
{
	loadOnlineInfo();
	loadSessionInfo(null);
};




/*增加一条记录*/


function searchPosition()
{
	
	var positionName= $('#filter_LIKE_positionName').val();
	var positionCode= $('#filter_LIKE_positionCode').val();
	
	expSearchParams={positionName: positionName, positionCode: positionCode};
	
	loadSearchPositionInfo( positionName, positionCode);
};

function clearPosition()
{
	$('#filter_LIKE_positionName').val('');
	$('#filter_LIKE_positionCode').val('');
	
};




function loadSessionInfo(loginName)
{
	//filter_EQ_ug_groupId
	
	if(loginName==null)
	{
		$('#dgSession').datagrid("loadData", []);
	}
	else
	{
		$("#dgSession").datagrid("options").url ="platform/onlineuser/onlineUserController/getPrinciplSessions.json";
		$('#dgSession').datagrid("reload", {loginName: loginName});
	}
	
};





function showSearchUser()
{
	$('#searchUserDialog').dialog("open");
};


function hideSearchUser()
{
	$('#searchUserDialog').dialog('close');
};

function searchUser()
{
		
	var userName= $('#userName').val();
	var deptName= $('#deptName').val();
	var startTime=$('#startTime').datetimebox('getValue');
	var endTime=$('#endTime').datetimebox('getValue');
	//alert(userName+","+deptName+","+startTime+","+endTime);
	
	if(startTime!="" && endTime!="" && startTime>endTime)
	{
		$.messager.alert('提示','起始时间大于终止时间','warning');
		return;
	}
	
	loadSearchOnlineInfo(userName, deptName, startTime, endTime);
};

function clearUser()
{
	$('#userName').val('');
	$('#deptName').val('');
	$('#startTime').datetimebox('setValue', '');
	$('#endTime').datetimebox('setValue', '');
};

function showTimelyRefresh()
{
	$('#timelyRefreshDialog').dialog("open");
};

function hideTimelyRefresh()
{
	$('#timelyRefreshDialog').dialog("close");
};

function setRefresh()
{
	var value=$('#refreshTime').val();
	if(timer)
		clearTimeout(timer);
	
	timer=setInterval(refresh, value*1000);
	hideTimelyRefresh();
};


function cancelTimelyRefresh()
{
	if(timer)
		clearTimeout(timer);
	
	$.messager.alert('提示','定时刷新已取消','warning');
};



</script>
</head>

<body class="easyui-layout" fit="true">

	
		<div data-options="region:'north',split:true,title:''" style="height: 350px; padding:0px;">
		
			<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'north',split:true,title:''" style="height: 50px; padding:0px;">
					<table>
						<tr>
							<td width='15%'><span style="color: blue">当前在线人数</span></td>
							<td width='10%'><span id='totalNum' style="color: blue">0</span></td>
							<td><span style="color: red">注意, 由于存在用户网络突然中断或用户不注销直接关闭浏览器等特殊情况，当前在线人数只是一个概数，可能不准确，仅供参考</span></td>
						</tr>
					</table>
				</div>
				<div data-options="region:'center',split:true,title:''" style="padding:0px;">	
					<div id="toolbarOnline" class="datagrid-toolbar">
						<sec:accesscontrollist   hasPermission="3" domainObject="pmTaskInfo_tabPower_button_btRefresh" >
							<a id="btRefresh"  class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="refresh();" href="javascript:void(0);">刷新</a>
						</sec:accesscontrollist>
						
						<%-- 
						<sec:accesscontrollist   hasPermission="3" domainObject="pmTaskInfo_tabPower_button_btTimelyRefresh" >
							<a id="btTimelyRefresh"  class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="showTimelyRefresh();" href="javascript:void(0);">定时刷新</a>
						</sec:accesscontrollist>
						
						<sec:accesscontrollist   hasPermission="3" domainObject="pmTaskInfo_tabPower_button_btCancelTimelyRefresh" >
							<a id="btCancelTimelyRefresh" class="easyui-linkbutton" iconCls="icon-remove"  plain="true" onclick="cancelTimelyRefresh();" href="javascript:void(0);">取消定时</a>
						</sec:accesscontrollist>
						 --%>
						<sec:accesscontrollist   hasPermission="3" domainObject="pmTaskInfo_tabPower_button_btSearchOnline" >
							<a id="btSearchOnline" class="easyui-linkbutton" iconCls="icon-search"  plain="true" onclick="showSearchUser();" href="javascript:void(0);">查询</a>
						</sec:accesscontrollist>
						
											
					</div>
					
					
							
					<table id="dgOnline" class="easyui-datagrid"
						data-options=" 
							fit: true,
							border: false,
							rownumbers: true,
							animate: true,
							collapsible: false,
							fitColumns: true,
							autoRowHeight: false,
							toolbar:'#toolbarOnline',
							idField :'id',
							singleSelect: true,
							checkOnSelect: true,
							selectOnCheck: true,
							
							pagination:true,
							pageSize:10,
							pageList:dataOptions.pageList,
							
							striped:true,
							
							view: onlineView,
							
							onClickRow: dgOnlineOnClickRow,
							onLoadSuccess: dgOnlineOnLoadSuccess
							">
						<thead>
							<tr>
								
								<th data-options="field:'name', halign:'center',align:'center'" editor="{type:'text'}" width="220">用户名</th>
								<th data-options="field:'loginName', halign:'center',align:'center'" editor="{type:'text'}" width="220">登录名</th>
								<th data-options="field:'deptName', halign:'center',align:'center'" editor="{type:'text'}" width="220">部门名称</th>
								<th data-options="field:'ip', halign:'center',align:'left'" editor="{type:'text'}" width="220">网络地址</th>
								<th data-options="field:'loginTime', halign:'center',align:'center'" editor="{type:'text'}" width="220">登录时间</th>
								<th data-options="field:'onlineTime', halign:'center',align:'center'" editor="{type:'text'}" width="220">在线时间</th>
								<!-- <th data-options="field:'sessionNum', halign:'center',align:'center'" editor="{type:'text'}" width="220">session实例数量</th> -->
								
							</tr>
						</thead>
					</table>		
						
				</div>	
				
				<div id="searchUserDialog" class="easyui-dialog" data-options="iconCls:'icon-search',resizable:true,modal:false,buttons:'#searchUserBtns'" 
					style="width: 600px;height:150px; visible: hidden" title="搜索在线用户">
					<form id="searchUserForm">
			    		<table>
			    			<tr>
			    				<td>用户名:</td><td><input type='text' name="userName" id="userName"/></td>
			    				<td>部门名称:</td><td><input type='text' name="deptName" id="deptName"/></td>
			    			</tr>
			    			<tr>
			    				<td>登录时间(起):</td><td><input class="easyui-datetimebox" data-options="{editable: false}" name="startTime" id="startTime"/></td>
			    				<td>登录时间(止):</td><td><input class="easyui-datetimebox" data-options="{editable: false}" name="endTime" id="endTime"/></td>
			    			</tr>
			    		</table>
			    	</form>
			    	<div id="searchUserBtns">
			    		<a class="easyui-linkbutton" plain="false" onclick="searchUser();" href="javascript:void(0);">查询</a>
			    		<a class="easyui-linkbutton" plain="false" onclick="clearUser();" href="javascript:void(0);">清空</a>
			    		<a class="easyui-linkbutton" plain="false" onclick="hideSearchUser();" href="javascript:void(0);">返回</a>
			    	</div>
			    </div>
			    
			    <div id="timelyRefreshDialog" class="easyui-dialog" data-options="iconCls:'icon-search',resizable:true,modal:false,buttons:'#searchUserBtns'" 
					style="width: 600px;height:100px; visible: hidden" title="搜索在线用户">
					<form id="searchUserForm">
			    		<table>
			    			<tr>
			    				<td>刷新时间（秒）:</td><td><input class="easyui-numberbox" name="refreshTime" id="refreshTime" value="20"/></td>
			    				
			    			</tr>
			    			
			    		</table>
			    	</form>
			    	<div id="searchUserBtns">
			    		<a class="easyui-linkbutton" iconCls="icon-setting" plain="true" onclick="setRefresh();" href="javascript:void(0);">确认</a>
			    		<a class="easyui-linkbutton" iconCls="icon-cancel" plain="true" onclick="cancelTimelyRefresh();" href="javascript:void(0);">重置</a>
			    		<a class="easyui-linkbutton" iconCls="icon-back" plain="true" onclick="hideTimelyRefresh();" href="javascript:void(0);">返回</a>
			    	</div>
			    </div>
			    
			    
			</div>
		</div>
		
		<div data-options="region:'center',split:true,title:''" style="padding:0px;">	
		
			
					
			<table id="dgSession" class="easyui-datagrid"
				data-options=" 
					fit: true,
					border: false,
					rownumbers: true,
					animate: true,
					collapsible: false,
					fitColumns: true,
					autoRowHeight: false,
					
					idField :'id',
					singleSelect: false,
					checkOnSelect: false,
					selectOnCheck: true,
					
										
					striped:true,
					view: sessionView
							
					">
				<thead>
					<tr>
						
						<th data-options="field:'lastRequest',required:true,halign:'center',align:'center'" editor="{type:'text'}" width="220">最近请求时间</th>
						<th data-options="field:'sessionId',required:true,halign:'center',align:'center'" editor="{type:'text'}" width="220">Session ID</th>
						<th data-options="field:'expired',required:true,halign:'center',align:'center'" editor="{type:'text'}" width="220">是否超时</th>
						<th data-options="field:'extra',halign:'center',align:'left'" editor="{type:'text'}"  width="220">操作</th>
						
						
						
					</tr>
				</thead>
			</table>	
		</div>
		
		
	    
	</div>
</body>
</html>