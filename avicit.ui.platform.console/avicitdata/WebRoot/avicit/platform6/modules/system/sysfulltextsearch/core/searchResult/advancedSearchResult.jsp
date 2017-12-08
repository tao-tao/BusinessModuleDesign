<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd"> 
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.List, 
                 java.util.Map, 
                 org.springframework.web.servlet.ModelAndView, 
                 org.springframework.ui.ModelMap,
                 avicit.platform6.modules.system.sysfulltextsearch.domain.SearchResult,
                 avicit.platform6.commons.utils.ViewUtil,
                 avicit.platform6.core.session.SessionHelper,
                 avicit.platform6.core.api.unituser.sysuser.domain.SysUser,
                 avicit.platform6.core.api.system.syslocale.PlatformLocalesJSTL,
                 avicit.platform6.modules.system.sysfulltextsearch.util.searchSysPath.SearchPathUtil"           
%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<% 
     SysUser currUser = SessionHelper.getLoginSysUser();
     String SecretLevel = "1";
     if(null != currUser) currUser.getSecretLevel();
%>

<% 
     String advancedSearch1 = "/platform/advancedSearchAction/advancedSearch.do";
     String advancedSearch2 = SearchPathUtil.deletePrefix(advancedSearch1);
%>

<html>
	  <head>
	  
	    	<title>高级检索</title>
	    	<base href="<%=ViewUtil.getRequestPath(request)%>">                  
	    	<jsp:include page="/avicit/platform6/component/common/EasyUIJsInclude.jsp"></jsp:include>
	    	
	    	<link href="avicit/platform6/modules/system/sysfulltextsearch/css/advancedSearchResult.css" rel="stylesheet" type="text/css" />       <%-- 搜索结果的样式 --%>                    
	    	<link href="avicit/platform6/modules/system/sysfulltextsearch/css/advancedSearchPagination.css" rel="stylesheet" type="text/css" />   <%-- 分页按钮相关的样式 --%>
	       	
	        <script type="text/javascript" src="avicit/platform6/modules/system/sysfulltextsearch/js/searchUtil.js"></script>    
			<script type="text/javascript" src="avicit/platform6/modules/system/sysfulltextsearch/js/advancedSearchFace.js"></script>
			<script type="text/javascript" src="avicit/platform6/modules/system/sysfulltextsearch/js/advancedSearchCommon.js"></script>
	  </head>
	  
	  <body>
            <%-- 高级检索条件为空 start--%>	
	        <c:if test="${!empty empty_adCondition}"> 
	              <center><font color="red">${empty_adCondition}</font></center>
	        </c:if> <%-- 高级检索条件为空 end--%>	
	        
	        <font size="0.1" color="white">	        
		        <%-- 还没建立好索引 start--%>	
		        <c:if test="${!empty indexed}">
		        	<center>${indexed}</center>
		        </c:if> <%-- 还没建立好索引 end--%>
		        <c:if test="${isHasIndex == false}">
					<center>no search index</center>
				</c:if>
	        </font>
	        
	        <%-- 高级检索请求提交的form, 页面上不显示，js填入值后提交 start--%>
	        <!--
	        <form id="sysAdvancedSearchForm" name="sysAdvancedSearchForm" method="post" action="<c:url value='<%=advancedSearch1%>'/>">
               <input id="field0"     type="text"  name="field0" value="${field0}" />
               <input id="condition0" type="text"  name="condition0"  value="${condition0}" />
               <input id="keyword0"   type="text"  name="keyword0"   value="${keyword0}" /> 
               
               <input id="logic1"     type="text"  name="logic1"  value="${logic1}" />       <%-- 逻辑关联词，或且非--%>	
               <input id="field1"     type="text"  name="field1"  value="${field1}" />       <%-- 搜索域，“标题”，“正文”，“发布人”--%>
               <input id="condition1" type="text"  name="condition1"  value="${condition1}" />   <%-- 逻辑条件，“包含”，不包含 --%>
               <input id="keyword1"   type="text"  name="keyword1"  value="${keyword1}" />     <%-- 检索关键词--%>
               
               <input id="logic2"     type="text"  name="logic2"  value="${logic2}" />
               <input id="field2"     type="text"  name="field2"  value="${field2}" />
               <input id="condition2" type="text"  name="condition2"  value="${condition2}" />
               <input id="keyword2"   type="text"  name="keyword2"  value="${keyword2}" />
                    
               <input id="logic3"     type="text"  name="logic3"  value="${logic3}" />
               <input id="field3"     type="text"  name="field3"  value="${field3}" />
               <input id="condition3" type="text"  name="condition3"  value="${condition3}" />
               <input id="keyword3"   type="text"  name="keyword3"  value="${keyword3}" />
               
               <input id="logic4"     type="text"  name="logic4"  value="${logic4}" />
               <input id="field4"     type="text"  name="field4"  value="${field4}" />
               <input id="condition4" type="text"  name="condition4"  value="${condition4}" />
               <input id="keyword4"   type="text"  name="keyword4"  value="${keyword4}" />
               
               <input id="logic5"     type="text"  name="logic5"  value="${logic5}" />
               <input id="field5"     type="text"  name="field5"  value="${field5}" />
               <input id="condition5" type="text"  name="condition5"  value="${condition5}" />
               <input id="keyword5"   type="text"  name="keyword5"  value="${keyword5}" />
               
               <input id="logic6"     type="text"  name="logic6"  value="${logic6}" />
               <input id="field6"     type="text"  name="field6"  value="${field6}" />
               <input id="condition6" type="text"  name="condition6"  value="${condition6}" />
               <input id="keyword6"   type="text"  name="keyword6"  value="${keyword6}" />
               
               <input id="logic7"     type="text"  name="logic7"  value="${logic7}" />
               <input id="field7"     type="text"  name="field7"  value="${field7}" />
               <input id="condition7" type="text"  name="condition7"  value="${condition7}" />
               <input id="keyword7"   type="text"  name="keyword7"  value="${keyword7}" />
               
               <input id="logic8"     type="text"  name="logic8"  value="${logic8}" />
               <input id="field8"     type="text"  name="field8"  value="${field8}" />
               <input id="condition8" type="text"  name="condition8"  value="${condition8}" />
               <input id="keyword8"   type="text"  name="keyword8"  value="${keyword8}" />
               
               <input id="logic9"     type="text"  name="logic9"  value="${logic9}" />
               <input id="field9"     type="text"  name="field9"  value="${field9}" />
               <input id="condition9" type="text"  name="condition9"  value="${condition9}" />
               <input id="keyword9"   type="text"  name="keyword9"  value="${keyword9}" />
               
               <input id="templateCategorie"   type="text"  name="templateCategorie"  value="${templateCategorie}" />
               <input id="secretLevel"   type="text"  name="secretLevel"  value="${secretLevel}" />
               <input id="time"   type="text"  name="time"  value="${time}" />
               
               <input id="startTime" type="text"  name="startTime"  value="${startTime}" />
               <input id="endTime"   type="text"  name="endTime"  value="${endTime}"   />
               
               <input id ="pageSize"  name="pageSize"  value="5"             />
               <input id ="pageIndex" name="pageIndex" value="${pageIndex}"  />        <%--传值用的隐藏控件--%>
                 
               <input type="submit" value="搜索" >
            </form>
            -->
            
	        <form id="sysAdvancedSearchForm" name="sysAdvancedSearchForm" method="post" action="<c:url value='<%=advancedSearch1%>'/>" style="width:0;height:0;">
               <input id="field0"     type="text"  name="field0" value="${field0}" style="width: 0px;display:none;"/>
               <input id="condition0" type="text"  name="condition0"  value="${condition0}" style="width: 0px;display:none;"/>
               <input id="keyword0"   type="text"  name="keyword0"   value="${keyword0}" style="width: 0px;display:none;"/> 
               
               <input id="logic1"     type="text"  name="logic1"  value="${logic1}" style="width: 0px;display:none;"/>       <%-- 逻辑关联词，或且非--%>	
               <input id="field1"     type="text"  name="field1"  value="${field1}" style="width: 0px;display:none;"/>       <%-- 搜索域，“标题”，“正文”，“发布人”--%>
               <input id="condition1" type="text"  name="condition1"  value="${condition1}" style="width: 0px;display:none;"/>   <%-- 逻辑条件，“包含”，不包含 --%>
               <input id="keyword1"   type="text"  name="keyword1"  value="${keyword1}" style="width: 0px;display:none;"/>     <%-- 检索关键词--%>
               
               <input id="logic2"     type="text"  name="logic2"  value="${logic2}" style="width: 0px;display:none;"/>
               <input id="field2"     type="text"  name="field2"  value="${field2}" style="width: 0px;display:none;"/>
               <input id="condition2" type="text"  name="condition2"  value="${condition2}" style="width: 0px;display:none;"/>
               <input id="keyword2"   type="text"  name="keyword2"  value="${keyword2}" style="width: 0px;display:none;"/>
                    
               <input id="logic3"     type="text"  name="logic3"  value="${logic3}" style="width: 0px;display:none;"/>
               <input id="field3"     type="text"  name="field3"  value="${field3}" style="width: 0px;display:none;"/>
               <input id="condition3" type="text"  name="condition3"  value="${condition3}" style="width: 0px;display:none;"/>
               <input id="keyword3"   type="text"  name="keyword3"  value="${keyword3}" style="width: 0px;display:none;"/>
               
               <input id="logic4"     type="text"  name="logic4"  value="${logic4}" style="width: 0px;display:none;"/>
               <input id="field4"     type="text"  name="field4"  value="${field4}" style="width: 0px;display:none;"/>
               <input id="condition4" type="text"  name="condition4"  value="${condition4}" style="width: 0px;display:none;"/>
               <input id="keyword4"   type="text"  name="keyword4"  value="${keyword4}" style="width: 0px;display:none;"/>
               
               <input id="logic5"     type="text"  name="logic5"  value="${logic5}" style="width: 0px;display:none;"/>
               <input id="field5"     type="text"  name="field5"  value="${field5}" style="width: 0px;display:none;"/>
               <input id="condition5" type="text"  name="condition5"  value="${condition5}" style="width: 0px;display:none;"/>
               <input id="keyword5"   type="text"  name="keyword5"  value="${keyword5}" style="width: 0px;display:none;"/>
               
               <input id="logic6"     type="text"  name="logic6"  value="${logic6}" style="width: 0px;display:none;"/>
               <input id="field6"     type="text"  name="field6"  value="${field6}" style="width: 0px;display:none;"/>
               <input id="condition6" type="text"  name="condition6"  value="${condition6}" style="width: 0px;display:none;"/>
               <input id="keyword6"   type="text"  name="keyword6"  value="${keyword6}" style="width: 0px;display:none;"/>
               
               <input id="logic7"     type="text"  name="logic7"  value="${logic7}" style="width: 0px;display:none;"/>
               <input id="field7"     type="text"  name="field7"  value="${field7}" style="width: 0px;display:none;"/>
               <input id="condition7" type="text"  name="condition7"  value="${condition7}" style="width: 0px;display:none;"/>
               <input id="keyword7"   type="text"  name="keyword7"  value="${keyword7}" style="width: 0px;display:none;"/>
               
               <input id="logic8"     type="text"  name="logic8"  value="${logic8}" style="width: 0px;display:none;"/>
               <input id="field8"     type="text"  name="field8"  value="${field8}" style="width: 0px;display:none;"/>
               <input id="condition8" type="text"  name="condition8"  value="${condition8}" style="width: 0px;display:none;"/>
               <input id="keyword8"   type="text"  name="keyword8"  value="${keyword8}" style="width: 0px;display:none;"/>
               
               <input id="logic9"     type="text"  name="logic9"  value="${logic9}" style="width: 0px;display:none;"/>
               <input id="field9"     type="text"  name="field9"  value="${field9}" style="width: 0px;display:none;"/>
               <input id="condition9" type="text"  name="condition9"  value="${condition9}" style="width: 0px;display:none;"/>
               <input id="keyword9"   type="text"  name="keyword9"  value="${keyword9}" style="width: 0px;display:none;"/>
               
               <input id="templateCategorie"   type="text"  name="templateCategorie"  value="${templateCategorie}" style="width: 0px;display:none;"/>
               <input id="secretLevel"   type="text"  name="secretLevel"  value="${secretLevel}" style="width: 0px;display:none;"/>
               <input id="time"   type="text"  name="time"  value="${time}" style="width: 0px;display:none;"/>
               
               <input id="startTime" type="text"  name="startTime"  value="${startTime}" style="width: 0px;display:none;"/>
               <input id="endTime"   type="text"  name="endTime"  value="${endTime}"   style="width: 0px;display:none;"/>
               
               <input id ="pageSize"  name="pageSize"  value="5"             style="width: 0px;display:none;"/>
               <input id ="pageIndex" name="pageIndex" value="${pageIndex}"  style="width: 0px;display:none;"/>        <%--传值用的隐藏控件--%>
                 
               <input type="submit" value="搜索" style="width: 0px;display:none;">
            </form>
           
        <%-- 搜索结果 start --%>
	    <div id="searchResult">                                            <!-- searchResult start 展示搜索结果和相关信息 -->
			<%!	int pageNum = 0; int pageCount = 0; int footsize = 7; String keywords = "www.avicit.com";%>
			<% int resultIndex = 0; %>
				<div id="resultList">        <!-- resultList start 搜索结果列表  -->
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
			
		</div> <!-- searchResult end -->
		
        <%@include file="advancedSearchPagination.jsp" %>
	    </body>
</html>