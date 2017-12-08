<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.List, 
                 java.util.Map, 
                 org.springframework.web.servlet.ModelAndView, 
                 org.springframework.ui.ModelMap,
                 avicit.platform6.modules.system.sysfulltextsearch.domain.SearchResult,
                 avicit.platform6.commons.utils.ViewUtil"
%>
<%@ page import="avicit.platform6.modules.system.sysfulltextsearch.util.searchSysPath.SearchPathUtil" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="avicit.platform6.core.api.system.syslocale.PlatformLocalesJSTL"%>

	    <%-- 搜索结果 start --%>
	    <div id="searchResult">                                            <!-- searchResult start 展示搜索结果和相关信息 -->
			<%!	int pageNum = 0; int pageCount = 0; int footsize = 7; String keywords = "www.avicit.com";%>
			<%String hasResult = (String)request.getParameter("hasResult"); %>
			
			<%if(hasResult == null){ %>                                  <!-- 用户还没开始搜 -->
			     <p class="STYLE0" ><%=PlatformLocalesJSTL.getBundleValueforSearch("search.result.noKeyWord")%></p>    <%-- 请在上面输入查询关键词--%>
			<%}else if(hasResult.startsWith("0")) { %>      <!-- 用户搜了，但是没有结果（也可能有结果，但密级或权限不够） -->
				 <jsp:include page="/avicit/platform6/modules/system/sysfulltextsearch/accessory/noResultTip.jsp" flush="true" />
			<%}else if(hasResult.startsWith("1")) { %>      <!-- 有可展现的结果  -->
				 <span id="countAndTime" class="STYLE2" >	
				 	<%=PlatformLocalesJSTL.getBundleValueforSearch("search.result.resultCount1")%>&nbsp;${searchResultTotal}&nbsp;<%=PlatformLocalesJSTL.getBundleValueforSearch("search.result.resultCount2")%> &nbsp;&nbsp;&nbsp;&nbsp;   <%-- 本次查询结果有约m条--%>
					<%=PlatformLocalesJSTL.getBundleValueforSearch("search.result.costTime1")%>&nbsp;${searchResultTime}&nbsp;<%=PlatformLocalesJSTL.getBundleValueforSearch("search.result.costTime2")%>                            <%-- 本次后台查询时间n毫秒--%>
				 </span>
				<% int resultIndex = 0; %>
				<div id="resultList" style="margin-top: 15px">        <!-- resultList start 搜索结果列表  -->
					<c:forEach items="${resultList}" var="resultItem">	
								<% resultIndex++; %>
								<table class="result" cellspacing="0" cellpadding="0">    <!-- 表格, 单条搜索结果  用这个表格来容纳该条结果的各个属性 start -->
									<tr>                                  
									<td class="f">
											<c:if test="${resultItem.FILTER_FLAG == 'TRUE'}">  <!-- 通过了页面过滤器，比如权限等 -->
												        <input id ="path" name="path" value="${resultItem.FILE_PATH}"  style="width: 0px;display:none;">        <!--  传值用的隐藏控件,nuy -->
														<h3 class="t">                                                                                <!-- 搜索结果的标题，链接到详情页或进行文件下载,  一般需要高亮 -->
																	<span id="index" style="width: 0px;display:none;"><%= resultIndex %></span>                                         <!-- index of search result, hidden unless the user change the configuration of view-->
																	<c:if test="${resultItem.INDEX_TYPE == 'DATABASE_TABLE'}">
																				<a  onclick="showResultOnTip( '<%=PlatformLocalesJSTL.getBundleValueforSearch("search.result.resultCaption")%>', '<%=ViewUtil.getRequestPath(request)%>${resultItem.SEARCH_RESULT_URL}')"  href="javascript:void(0);" > 
																				                                              <%--搜索结果 --%>
																				<font color="#0000CC">
																							${resultItem.CAPE_AVICIT_SEARCH_TITLE}                    <!--  database type search source -->
																				</font></a>
					                                                </c:if>
					                                                
					                                                <c:if test="${resultItem.INDEX_TYPE == 'DISK_FILE'}">
																				<a target="_blank" href="${resultItem.SEARCH_RESULT_URL}" > <font color="#0000CC">
																							${resultItem.FILE_NAME}                                                     <!--  disk file type search source -->
																				</font></a>
					                                                </c:if>
														</h3>
														
														<font size="-1">                                                                            <!-- 搜索结果的正文, 必然需要高亮 -->
															${resultItem.FILE_CONTENT}                                               <!--  disk file type search source-->                        <!-- 搜索结果的文件正文(摘要)  -->
															${resultItem.CAPE_AVICIT_SEARCH_CONTENT}            <!--  database type search source-->                     <!-- 搜索结果的正文  -->
														<br/>
														
														<span class="g">                                                                          <!-- 搜索结果的补充信息(细节), 肯定需要高亮 -->
														    ${resultItem.NAME}  :                                                             <!-- all type search source-->                                  <!-- 搜索名称 -->
															${resultItem.FILE_NAME}                                                      <!-- disk file  type search source-->                       <!-- 搜索结果的文件名称  -->
														    ${resultItem.FILE_PATH}                                                        <!-- disk file  type search source-->                       <!-- 搜索结果的文件路径  -->
														    ${resultItem.CAPE_AVICIT_SEARCH_DETAIL}                  <!-- database type search source-->                     <!-- 搜索细节 -->
														</span> 
														
														<a class="m" target="_blank" > 
															    ${resultItem.INDEX_TYPE}
																${resultItem.SEARCH_RESULT_NAME} :                         <!-- all types of data source -->                               <!-- 搜索结果的类别名, 不用高亮  -->
																${resultItem.SEARCH_RESULT_TYPE}                              <!-- all type search source-->                                   <!-- 搜索结果的系统类别  -->
														</a>
														<br/>
														
														<span>
														<c:if test="${!empty resultItem.CAPE_AVICIT_SEARCH_ATTACHMENT}">
																	<form action="<c:url value="<%= SearchPathUtil.SEARCH_DOWNLOAD_BLOBFILE %>"/>" method="post">
																			<!--  <button type="submit" style="border: 0; background: transparent;"><u> <font color="blue">相关附件:</font></u></button> --> <!--  权限要求: 这里附件不能提供下载, 封掉下载按钮 --> 
																			<%=PlatformLocalesJSTL.getBundleValueforSearch("search.result.relatedAccessories")%>:<%--相关附件 --%>
																			 ${resultItem.CAPE_AVICIT_SEARCH_ATTACHMENT}               
																			<input name="fileName" value="${resultItem.CAPE_AVICIT_SEARCH_ATTACHMENT_FILENAME}"  style="visibility:collapse;width: 0px;height:0px" >   
																			<input name="sql" value="${resultItem.CAPE_AVICIT_SEARCH_ATTACHMENT_SQL}"  style="visibility:collapse;width: 0px;height:0px">  
																			<input name="columnName" value=" ${resultItem.CAPE_AVICIT_SEARCH_ATTACHMENT_COLUMNNAME}"  style="visibility:collapse;width: 0px;height:0px">  
											                        </form>
														</c:if>
														</span>
			
														<br>
														</font>	
											</c:if>		
											
											<c:if test="${resultItem.FILTER_FLAG == 'FALSE'}">    <!-- 被页面过滤器拦截，不能显示该条数据 -->
												<span  class="pageFilteredResult"> <h3 class="t"><font color="gray" >
													 <br/><br/> <%=PlatformLocalesJSTL.getBundleValueforSearch("search.result.limitsOfAuthority")%><br/><br/> <%--由于权限等原因,  您不能查看该条数据的具体内容。--%>
												</font></h3></span>
											</c:if>
											
									</td>
									</tr>
								</table>              <!-- 表格, 单条搜索结果  用这个表格来容纳该条结果的各个属性 end -->
						<br>
						</c:forEach>
				</div>                       <!-- resultList end 搜索结果列表  -->
				
				
			<%}%>
			
		</div> <!-- searchResult end -->
		
		<!--  相关搜索部分 start -->
		<%-- 设计一种合适而简单的算法，实现相关搜索的功能  --%>
		<%String hasRelationResult = (String)request.getParameter("hasRelationResult"); %>
		<% if(hasRelationResult != null){ %>			
			<% if(hasRelationResult.startsWith("1")){ %>
				<div id="rs">
					<table cellpadding="0">
						<tr>
							<th class="tt" rowspan="2">相关搜索</th>
							<th><a href="#">相关搜索</a></th>
						</tr>
					</table>
				</div>
			<%}
		}
		%>
		<!--  相关搜索部分 end -->
		
        <jsp:include page="/avicit/platform6/modules/system/sysfulltextsearch/core/searchResult/searchPagination.jsp" flush="true" />