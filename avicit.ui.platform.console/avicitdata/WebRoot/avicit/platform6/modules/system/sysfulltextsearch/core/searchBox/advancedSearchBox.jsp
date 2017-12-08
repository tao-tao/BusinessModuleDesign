<%--<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd"> --%>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page import="avicit.platform6.modules.system.sysfulltextsearch.service.SearchServiceBase" %>
<%@ page import="avicit.platform6.modules.system.sysfulltextsearch.util.searchSysPath.SearchPathUtil" %>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<%@ page import="avicit.platform6.core.session.SessionHelper"%>
<%@ page import="avicit.platform6.core.api.unituser.sysuser.domain.SysUser"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<% 
     SysUser currUser = SessionHelper.getLoginSysUser();
	 String secretLevel = "1";   // 默认当前用户密 级为最低
     if(null != currUser)  
     	 secretLevel = currUser.getSecretLevel();
%>

<script type="text/javascript" src="avicit/platform6/modules/system/sysfulltextsearch/js/searchUtil.js"></script>
<script type="text/javascript" src="avicit/platform6/modules/system/sysfulltextsearch/js/advancedSearchFace.js"></script>

<script type="text/javascript">
function formatD(date){
	var year = date.getFullYear();
	var month = date.getMonth() + 1;
	var dateNo = date.getDate();  // 几号 What is the date today?
	//date.getDay(); 星期几 what day is it today?
	return year+"-"+month +"-"+dateNo;
}

function parserD(s){
	var t = Date.parse(s);
	if (!isNaN(t)){
		return new Date(t);
	} else {
		return new Date();
	}
}

$(document).ready(function(){
	
	$('#startTime').datebox({
		editable: false,
		formatter: formatD
		//,parser: parserD
	});
	
	$('#endTime').datebox({
		editable : false,
		formatter: formatD
		//,parser: parserD
	});
	
	var comboText  = $(".combo-text");
	for(var i = 0; i < comboText.length; i++) {
		comboText[i].style.backgroundColor = "#fff";
		comboText[i].style.border="1 solid #EEEEEE";
		comboText[i].style.borderRight="0";
	}

	var comboArrow  = $(".combo-arrow");
	for(var i = 0; i < comboArrow.length; i++) {
		comboArrow[i].style.paddingBottom = "1px";
		comboArrow[i].style.backgroundColor = "#fff";
		comboArrow[i].style.border="1 solid #EEEEEE";
		comboArrow[i].style.borderLeft="0";
	}

});
</script>

<%-- 高级检索框外层，点击搜索按钮后，通过js传值给真正的form(隐藏)，然后提交给后台 start--%>
<div id="advancedSearchDiv">

      <%-- 高级检索框 start--%>
	  <div class="gjjs" style="background:url(avicit/platform6/modules/system/sysfulltextsearch/images/searchBox/sousbg.png) no-repeat;">
	  
	            <%-- 高级检索框头，可切换到统一搜索，可保存检索条件 start--%>
			    <div class="jstab" style="background:url(avicit/platform6/modules/system/sysfulltextsearch/images/searchBox/sstitbg.png) left bottom repeat-x">
				      <div class="jstabb"><a href="javascript:void(0);">高级检索</a></div>
			    </div>
			    <%--高级检索框头，可切换到统一搜索，可保存检索条件 end--%>
			    
			    <%-- 高级检索框条件部分 start--%>
			    <div class="jsmain">
			       <table cellpadding="0" cellspacing="0" border="0" width="100%">
			       
			        <tr id="complexCondition0" style="display:block;">
			          	<td width="90"></td>
			          	<td width="115"><select id="field0" class="w105"><option value ="ALL">全部</option><option value ="<%=SearchServiceBase.specialFields[0]%>"><%=SearchServiceBase.specialFieldNames[0]%></option><option value ="<%=SearchServiceBase.specialFields[1]%>"><%=SearchServiceBase.specialFieldNames[1]%></option><option value ="<%=SearchServiceBase.specialFields[2]%>"><%=SearchServiceBase.specialFieldNames[2]%></option> </select></td>
			          	<td width="90"><select id="condition0" class="w68"><option value ="IN">包含</option><option value ="NOT">不包含</option></select></td>
			          	<td><input id ="adKeyword0" type="text" value="--请输入--" class="w330" onfocus="adSearchKeywordFocus(this)" onblur="adSearchKeywordBlur(this)"/></td>
			          	<td width="80" nowrap="nowrap">
			          		<a id="conditionAdd0" href="javascript:void(0);" onclick="addAdSearchCondition(0)"><img src="avicit/platform6/modules/system/sysfulltextsearch/images/searchBox/ssadd.png" /></a><div class="clear"></div>
			          	</td>
			        </tr>
			        
			        <tr id="complexCondition1" style="display:none;">
			          	<td width="90"><select id="logic1" class="w72"><option value ="OR">或者</option><option value ="AND">并且</option></select></td>
			          	<td width="115"><select id="field1" class="w105"><option value ="ALL">全部</option><option value ="<%=SearchServiceBase.specialFields[0]%>"><%=SearchServiceBase.specialFieldNames[0]%></option><option value ="<%=SearchServiceBase.specialFields[1]%>"><%=SearchServiceBase.specialFieldNames[1]%></option><option value ="<%=SearchServiceBase.specialFields[2]%>"><%=SearchServiceBase.specialFieldNames[2]%></option> </select></td>
			          	<td width="90"><select id="condition1" class="w68"><option value ="IN">包含</option><option value ="NOT">不包含</option></select></td>
			          	<td><input id ="adKeyword1" type="text" value="--请输入--" class="w330"  onfocus="adSearchKeywordFocus(this)" onblur="adSearchKeywordBlur(this)"/></td>
			          	<td width="80" nowrap="nowrap">
			          		<a id="conditionDelete1" href="javascript:void(0);" onclick="deleteAdSearchCondition(1)"><img src="avicit/platform6/modules/system/sysfulltextsearch/images/searchBox/ssdelete.png" /></a>
			          		<a id="conditionAdd1" href="javascript:void(0);" onclick="addAdSearchCondition(1)"><img src="avicit/platform6/modules/system/sysfulltextsearch/images/searchBox/ssadd.png" /></a> <div class="clear"></div>
			          	</td>
			        </tr>
			        
			        <tr id="complexCondition2" style="display:none;">
			          	<td width="90"><select id="logic2" class="w72"><option value ="OR">或者</option><option value ="AND">并且</option></select></td>
			          	<td width="115"><select id="field2" class="w105"><option value ="ALL">全部</option><option value ="<%=SearchServiceBase.specialFields[0]%>"><%=SearchServiceBase.specialFieldNames[0]%></option><option value ="<%=SearchServiceBase.specialFields[1]%>"><%=SearchServiceBase.specialFieldNames[1]%></option><option value ="<%=SearchServiceBase.specialFields[2]%>"><%=SearchServiceBase.specialFieldNames[2]%></option> </select></td>
			          	<td width="90"><select id="condition2" class="w68"><option value ="IN">包含</option><option value ="NOT">不包含</option></select></td>
			          	<td><input id ="adKeyword2"  type="text" value="--请输入--" class="w330" onfocus="adSearchKeywordFocus(this)" onblur="adSearchKeywordBlur(this)"/></td>
			          	<td nowrap="nowrap" width="80">
			          		<a id="conditionDelete2"  href="javascript:void(0);" onclick="deleteAdSearchCondition(2)"><img src="avicit/platform6/modules/system/sysfulltextsearch/images/searchBox/ssdelete.png" /></a>
			          		<a id="conditionAdd2"  href="javascript:void(0);" onclick="addAdSearchCondition(2)"><img src="avicit/platform6/modules/system/sysfulltextsearch/images/searchBox/ssadd.png" /></a><div class="clear"></div>
			          	</td>
			        </tr>
			        
			        <tr id="complexCondition3" style="display:none;">
			          	<td width="90"><select id="logic3" class="w72"><option value ="OR">或者</option><option value ="AND">并且</option></select></td>
			          	<td width="115"><select id="field3" class="w105"><option value ="ALL">全部</option><option value ="<%=SearchServiceBase.specialFields[0]%>"><%=SearchServiceBase.specialFieldNames[0]%></option><option value ="<%=SearchServiceBase.specialFields[1]%>"><%=SearchServiceBase.specialFieldNames[1]%></option><option value ="<%=SearchServiceBase.specialFields[2]%>"><%=SearchServiceBase.specialFieldNames[2]%></option> </select></td>
			          	<td width="90"><select id="condition3" class="w68"><option value ="IN">包含</option><option value ="NOT">不包含</option></select></td>
			          	<td><input id ="adKeyword3" type="text" value="--请输入--" class="w330" onfocus="adSearchKeywordFocus(this)" onblur="adSearchKeywordBlur(this)"/></td>
			          	<td nowrap="nowrap" width="80">
			          		<a id="conditionDelete3"  href="javascript:void(0);" onclick="deleteAdSearchCondition(3)"><img src="avicit/platform6/modules/system/sysfulltextsearch/images/searchBox/ssdelete.png" /></a>
			          		<a id="conditionAdd3"  href="javascript:void(0);" onclick="addAdSearchCondition(3)"><img src="avicit/platform6/modules/system/sysfulltextsearch/images/searchBox/ssadd.png" /></a><div class="clear"></div>
			          	</td>
			        </tr>
			        
			        <tr id="complexCondition4" style="display:none;">
			          	<td width="90"><select id="logic4" class="w72"><option value ="OR">或者</option><option value ="AND">并且</option></select></td>
			          	<td width="115"><select id="field4" class="w105"><option value ="ALL">全部</option><option value ="<%=SearchServiceBase.specialFields[0]%>"><%=SearchServiceBase.specialFieldNames[0]%></option><option value ="<%=SearchServiceBase.specialFields[1]%>"><%=SearchServiceBase.specialFieldNames[1]%></option><option value ="<%=SearchServiceBase.specialFields[2]%>"><%=SearchServiceBase.specialFieldNames[2]%></option> </select></td>
			          	<td width="90"><select id="condition4" class="w68"><option value ="IN">包含</option><option value ="NOT">不包含</option></select></td>
			          	<td><input id ="adKeyword4" type="text" value="--请输入--" class="w330" onfocus="adSearchKeywordFocus(this)" onblur="adSearchKeywordBlur(this)"/></td>
			          	<td nowrap="nowrap" width="80">
			          		<a id="conditionDelete4" href="javascript:void(0);" onclick="deleteAdSearchCondition(4)"><img src="avicit/platform6/modules/system/sysfulltextsearch/images/searchBox/ssdelete.png" /></a>
			          		<div class="clear"></div>
			          	</td>
			        </tr>
			      </table>
			      
			      <div class="jsmline"></div> <%-- 页面上的分割线 --%>
			      
			      <table cellpadding="0" cellspacing="0" border="0" width="100%">
			        <tr>
			          <%--
			          <th width="140">发布日期</th>
			          <td width="150"><select id="timeForAd" class="w140" style="margin-left: 5px"><option value ="ALL">全部时段</option><option value ="DAY"> 本 日</option><option value ="MONTH"> 本 月</option><option value ="YEAR"> 本 年</option><option value ="YEAR_BEFORE">本年之前</option></select></td>
			          --%>
			          <th width="140"></th>
			          <td width="150"><select style="display: none;" id="timeForAd" class="w140" style="margin-left: 5px"><option value ="ALL">全部时段</option><option value ="DAY"> 本 日</option><option value ="MONTH"> 本 月</option><option value ="YEAR"> 本 年</option><option value ="YEAR_BEFORE">本年之前</option></select></td>
			          <th width="140">密级</th>
			          <td>
			              <select id="secretLevelForAd" class="w78"  style="margin-left: 4px;"> 
			              <%-- <select id="secretLevelForAd" class="w140"> --%>
			                    <% if(secretLevel.compareTo("2") >= 0){%>
			          				<option value ="ALL">全 部</option>
			          			<% }%>	
			          					          			
			          			<option value ="1">内 部</option>
			          			
			          			<% if(secretLevel.compareTo("2") >= 0){%>
			          				<option value ="2">秘 密</option>
			          			<% }%>
			          			
			          			<% if(secretLevel.compareTo("3") >= 0){%>
			          				<option value ="3">机 密</option>
			          			<% }%>
			          			
			          			<% if(secretLevel.compareTo("4") >= 0){%>
			          				<option value ="4">绝 密</option>
			          			<% }%>			          			
			              </select>
			          </td>
			        </tr>
			        
			     </table>
			     
			     <%--
			     <table cellpadding="0" cellspacing="0" border="0" width="100%" style="display:none;">
			        <tr>
			          <th width="140">开始时间</th>
			          <td width="150">
			          		<input id="startTime" type="text">
			          </td>
			          
			          <th width="140">结束时间</th>
			          <td>
			          		<input id="endTime" type="text">
			          </td>
			        </tr>     
			     </table>	
			     --%>	      
			      <input type="text" id="pageIndex" value="1" style="width:0;display:none;"/>  <%-- 请求的分页序号，默认为1  --%>
			      <input type="text" id="pageSize" value="5"  style="width:0;display:none;"/>  <%-- 页的大小 ，默认为5，没必要时不变--%>
			      
			      <div class="jsmline"></div> <%-- 页面上的分割线 --%>
			      
			      <div class="jsbutton"><a href="javascript:void(0);"><img src="avicit/platform6/modules/system/sysfulltextsearch/images/searchBox/zssearch.png" onclick="doFirstAdSearch()"/></a></div>
			      
		    </div>   
	        <%-- 高级检索框条件部分 end--%>
	        
	  </div>
	  <%--  高级检索框 end--%>
	  
</div>
<%--  高级检索框外层，点击搜索按钮后，通过js传值给真正的form(隐藏)，然后提交给后台 end--%>
