<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd"> 
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page import="avicit.platform6.modules.system.sysfulltextsearch.util.searchSysPath.SearchPathUtil" %>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="avicit.platform6.core.api.system.syslocale.PlatformLocalesJSTL"%>

<script type="text/javascript" src="avicit/platform6/modules/system/sysfulltextsearch/js/searchUtil.js"></script>

<%-- 热输入提示用的js TODO--%>  
<script type="text/javascript">
<!-- 
var baseUrl = "<%=ViewUtil.getRequestPath(request)%>"; /*使url可用*/
var fulltextSuggestPath = baseUrl + "platform/sysFullTextSearch/LexiconAction/fulltextSearchSuggest";
//-->
</script>
<script type="text/javascript" src="avicit/platform6/modules/system/sysfulltextsearch/js/fulltextSearchSuggest.js"></script>

<%-- 搜索主体功能用的js--%>
<script type="text/javascript" src="avicit/platform6/modules/system/sysfulltextsearch/js/searchCommon.js"></script>

<%-- 搜索选项用的js--%>
<script type="text/javascript" src="avicit/platform6/modules/system/sysfulltextsearch/js/searchChoice.js"></script>

               <div align="left">
				<%-- 可被别的页面包含，引用；任何jsp页面include本页面后修改一下路径， 便具备了全文搜索功能   --%>
				<form name="sysFullTextSearchForm" method="post" action="<c:url value='<%= SearchPathUtil.SEARCH_SEARCH_ALL_INDEX_BYPAGE_ACTION %>'/>" onsubmit="return checkKeyWordsInput()">  
					<span class="s_ipt_wr">
						<input id="keywords" type="text" value="${searchKeywords}" name="keywords" maxlength="100" class="s_ipt" autocomplete="off"/>
					</span>
					<span class="s_btn_wr">
						<input type="submit" value='<%=PlatformLocalesJSTL.getBundleValueforSearch("search.button.text")%>' id="su" style="background: url(avicit/platform6/modules/system/sysfulltextsearch/images/searchBox/sousbg.png/i-1.0.0.png) repeat scroll 0 0 #DDDDDD;" class="s_btn"/> <%--搜索--%>
					</span>
				</form>
				
			   <%-- 热输入的提示 --%>
			   <ul id="suggest_ul"></ul>
               </div>
               
               <%--放在后面,确保加载完毕再执行, 不需要热输入提示功能时, 注释掉这段即可 start--%>
               <script type="text/javascript">
               <!--                          
               			prepareToShowSuggest() ;
                        var suggestFrontList = new Array();
                        
                        // 获取指定名称(searchHotInput)的cookie的值,  即当前浏览器里存的热输入。
                        var hotInputInCookies = getCookie('searchHotInput');
                        if(null != hotInputInCookies && 'undefined' != typeof(hotInputInCookies) )
                        		suggestFrontList = hotInputInCookies.split('*');
                         
               // -->
               </script>
               <%--放在后面,确保加载完毕再执行, 不需要热输入提示功能时, 注释掉这段即可 end--%>
               
<%--  搜索选项  start--%>
<table cellspacing="0" cellpadding="0" align="right" width="20%">
		<tr>
			<td align="left" style="padding-right:10px">
				<div style="border-left:1px solid #e1e1e1;padding-left:10px;word-wrap:break-word;overflow:hidden;">
						<div id="ec_im_container">
								<div style="line-height:24px;margin:0 0 4px 0;">
											<a href="javascript:void(0);" onclick="showOrHideChoicePannel();">
															<font color="#666666" style="text-decoration:underline"><span><%=PlatformLocalesJSTL.getBundleValueforSearch("search.box.root")%></span></font>   <%--检索选项--%>
											</a>
											 <div id="choicePannel" class="EC_fr EC_PP" style="display:none;"> 
															<table border="0">
																<%--==========================页面显示控制（不涉及后台）===========start============== --%>
																<tr height="30">
																	    <td><span style="display:inline-block"><%=PlatformLocalesJSTL.getBundleValueforSearch("search.box.filteredInfo")%>&nbsp;&nbsp;</span></td> <%--过滤信息: --%>
																		<td width="50">
																		         <button id="searchChoiceButton1" 
																								style="border: 0; background: transparent;"
																                                onmouseover="showButtonStyle('searchChoiceButton1')"
																                                onmouseout="hideStyle('searchChoiceButton1')"
																		                         onclick="hideFilteredResult ()"> <font color="blue"><u><%=PlatformLocalesJSTL.getBundleValueforSearch("search.box.hide")%></u></font> <%--隐藏  --%>
																		         </button>
																		</td> 
																		<td width="50">
																				 <button id="searchChoiceButton2" 
																								style="border: 0; background: transparent;"
																                                onmouseover="showButtonStyle('searchChoiceButton2')"
																                                onmouseout="hideStyle('searchChoiceButton2')"
																		                        onclick="showFilteredResult ()"> <font color="blue"><u><%=PlatformLocalesJSTL.getBundleValueforSearch("search.box.show")%></u></font> <%--显示  --%>
																		         </button>
																		</td>
																</tr>
																
																<tr  height="30">
																	    <td><%=PlatformLocalesJSTL.getBundleValueforSearch("search.box.quantitAndTime")%>&nbsp;&nbsp;</td> <%-- 数目与时间: --%>
																		<td  width="50">
																		         <button id="searchChoiceButton3" 
																								style="border: 0; background: transparent;"
																                                onmouseover="showButtonStyle('searchChoiceButton3')"
																                                onmouseout="hideStyle('searchChoiceButton3')"
																		                         onclick="hideElement ('countAndTime')"> <font color="blue"><u><%=PlatformLocalesJSTL.getBundleValueforSearch("search.box.hide")%></u></font> <%-- 隐藏 --%>
																		          </button>
																		</td> 
																		<td width="50">
																				 <button id="searchChoiceButton4" 
																								style="border: 0; background: transparent;"
																                                onmouseover="showButtonStyle('searchChoiceButton4')"
																                                onmouseout="hideStyle('searchChoiceButton4')"
																		                        onclick="showElement ('countAndTime')"> <font color="blue"><u><%=PlatformLocalesJSTL.getBundleValueforSearch("search.box.show")%></u></font> <%--显示  --%>
																		         </button>
																		</td>
																</tr>
																<%--==========================页面显示控制（不涉及后台）==============end===============--%>
																
    														    <tr>    <%--空白行，仅样式需要--%>
																	    <td colspan="3">&nbsp;</td> 
																</tr>	
																
																<%--==========================搜索选项（需要将请求传到后台）==========start===============--%>
														        <form id="compositeSearchForm" name="compositeSearchForm" method="post" action="<c:url value="<%= SearchPathUtil.SEARCH_COMPOSITE_SEARCH_ACTION %>"/>" onsubmit="return checkKeyWordsInputInChoice('newKeywords')">
															        <input type="text" value="${searchKeywords}" id="oldKeywords" name="oldkeywords" style="display: none;"/>   <%-- 上次搜索的关键词，隐藏 --%>
															        <input type="text" value="" id="newKeywords" name="keywords" style="display: none;"/>   <%-- 本次搜索的关键词，隐藏--%>
	                                                                <tr>   <%-- 检索全部   --%>
																		    <td colspan="2">
																		            <div id="tip0" style="width:120px;height:80px;padding:2em;display:none">
	                                                                                     <%=PlatformLocalesJSTL.getBundleValueforSearch("search.box.searchAllInOnePageTip")%> <%-- 将所有相关结果全部检索出来，放在一页中。数据量较大时，可能需要较长响应时间。 --%>
	                                                                                </div>
	                                                                                
																				    <span style="line-height: 160%" onmouseover="showTip(0, event)" onmouseout="hiddenTip(0, event)">
																				    	<%=PlatformLocalesJSTL.getBundleValueforSearch("search.box.searchAllInOnePage")%>  <%-- 检索所有结果 --%>
																				    </span>       
	                                                                        </td>
	                                                                        <td align="center"> 
				 															        <input type="checkbox" name="searchAllInOnePage" title="<%=PlatformLocalesJSTL.getBundleValueforSearch("search.box.searchAllInOnePage")%>"/> <%-- 检索所有结果 --%>
																		    </td>
																	</tr>	
																	
																	<tr>   <%-- 二次检索（类型A）   --%>
																		    <td colspan="2">
																		            <div id="tip1" style="width:120px;height:80px;padding:2em;display:none">
	                                                                                     <%=PlatformLocalesJSTL.getBundleValueforSearch("search.box.searchInResultTip")%> <%-- 在上次检索的结果中，进行进一步的检索。 --%>
	                                                                                </div>
	                                                                                
																				    <span style="line-height: 160%" onmouseover="showTip(1, event)" onmouseout="hiddenTip(1, event)">
																				    	<%=PlatformLocalesJSTL.getBundleValueforSearch("search.box.searchInResult")%>  <%-- 二次检索 --%>
																				    </span>               
	                                                                        </td>
	                                                                        <td align="center">        
	                                                                                <input type="checkbox" name="searchInResult" title="<%=PlatformLocalesJSTL.getBundleValueforSearch("search.box.searchInResult")%>"/> <%-- 检索所有结果 --%>
																		    </td>
																	</tr>
																	
																	<tr>  <%--空白行，仅样式需要--%>					
																	    <td colspan="3">&nbsp;</td> 
																    </tr>
																
																	<tr>
																	        <td colspan="3" align="right">
																					<input type="submit" value='<%=PlatformLocalesJSTL.getBundleValueforSearch("search.button.text")%>'/> 
																					&nbsp;&nbsp;&nbsp;&nbsp;
																			</td>
																	</tr> 
															    </form>										
																<%--========================页搜索选项（需要将请求传到后台）=============end============ --%>															
															</table>
											</div>
											<br>
								</div>
					</div>
		       </div>
		       
           </td>
         </tr>
</table>
<%--  搜索选项  end--%>