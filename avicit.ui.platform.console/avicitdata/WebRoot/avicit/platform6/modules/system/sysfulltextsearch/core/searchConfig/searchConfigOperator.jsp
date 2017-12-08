<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page import="avicit.platform6.modules.system.sysfulltextsearch.util.searchSysPath.SearchPathUtil" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="avicit.platform6.core.api.system.syslocale.PlatformLocalesJSTL"%>

		<div class="title" align="center"  style="margin-top: 3px">
					<%=PlatformLocalesJSTL.getBundleValueforSearch("search.config.operator.caption")%> <%-- 手动执行搜索引擎的各项操作，了解其含义后方可执行操作。--%>
		</div>
		 
		<table width="100%" id="searchEngineOperateTable"  border="1" class="t1" height="5px">
					   <tr bgcolor="#EFEFEF">
							    <td width="10%" bgcolor="#EFEFEF" align="center"  bordercolor="#9F9F9F"><%=PlatformLocalesJSTL.getBundleValueforSearch("search.config.operator.operatorName")%></td> <%-- 操作名称--%>
							    <td width="50%" class="noline1" bgcolor="#EFEFEF" align="center"  bordercolor="#EFEFEF"><%=PlatformLocalesJSTL.getBundleValueforSearch("search.config.operator.operatorDescribe")%></td> <%-- 简要说明--%>
							    <td width="10%" class="noline2" bgcolor="#EFEFEF"align="center"  bordercolor="#EFEFEF"></td> 
							    <td width="10%" bgcolor="#EFEFEF" align="center"  bordercolor="#9F9F9F"><%=PlatformLocalesJSTL.getBundleValueforSearch("search.config.operator.operatorExecute")%></td> <%-- 点击执行--%>
						</tr>
			
						   <tr class="a1">
									    <td width="10%"><%=PlatformLocalesJSTL.getBundleValueforSearch("search.config.operator.rebuildIndex.caption")%></td> <%-- 手动重建索引 --%>
									    <td width="50%"  class="noline1"><%=PlatformLocalesJSTL.getBundleValueforSearch("search.config.operator.rebuildIndex.describe")%></td> <%-- 重建系统内所有的索引，包括文档索引、菜单索引、数据索引等--%>
									    <td width="10%"  class="noline2"></td>
									    <td width="10%"  align="center">
									    				<form id="rebuildIndexForm" name="rebuildIndexForm" method="post" action="<c:url value="<%= SearchPathUtil.SEARCH_REBUILD_ALL_INDEX_ACTION %>"/>" style="display:inline;">
																	<button type="submit" id="operator1" name="rebuildIndex"
																		style="border: 0; background: transparent"
																		onmouseover="showButtonStyle('operator1')"
																		onmouseout="hideStyle('operator1')"
																		onclick="return confirmOperator('操作将改变索引文件, 执行过程中搜索功能将不可用, 您确认要进行此项操作吗？')">
																				<%=PlatformLocalesJSTL.getBundleValueforSearch("search.config.operator.execute")%> <%-- 执行操作 --%>
																		</button><br />
													  </form>
									    </td>
						  </tr>
						  
				  		   <tr>
								    <td width="10%"><%=PlatformLocalesJSTL.getBundleValueforSearch("search.config.operator.appendIndex.caption")%></td> <%-- 构建增量索引 --%>
								    <td width="50%"  class="noline1"><%=PlatformLocalesJSTL.getBundleValueforSearch("search.config.operator.appendIndex.describe")%></td> <%-- 构建增量索引，新增索引内容，部分修改原索引 --%>
								    <td width="10%"  class="noline2" ></td>
								    <td width="10%"  align="center">
								    				<form id="rebuildIndexForm" name="rebuildIndexForm" method="post" action="<c:url value="<%= SearchPathUtil.SEARCH_BUILD_INCREMENTAL_INDEX_ACTION %>"/> " style="display:inline;">
																<button type="submit" id="operator2" name="rebuildIndex"
																	style="border: 0; background: transparent;"
																	onmouseover="showButtonStyle('operator2');"
																	onmouseout="hideStyle('operator2')"
																	onclick="return confirmOperator('操作将改变索引文件, 执行过程中搜索功能将不可用, 您确认要进行此项操作吗？')">
																			<%=PlatformLocalesJSTL.getBundleValueforSearch("search.config.operator.execute")%><%-- 执行操作 --%>
																</button><br/>
													</form>
								    </td>
				         </tr>
		                 
		                <%--
				  		<tr class="a1">
							    <td width="10%">优化索引结构</td>
							    <td width="50%"  class="noline1">对索引结构进行优化，由于数据量和软硬件配置不同，本操作时间可能会较长</td>
							    <td width="10%"  class="noline2" ></td>
							    <td width="10%"  align="center">
											<form id="rebuildIndexForm" name="rebuildIndexForm" method="post" action="<c:url value="<%= SearchPathUtil.SEARCH_REBUILD_ALL_INDEX_ACTION %>"/>" style="display:inline;">
															<button type="submit" id="operator3" name="rebuildIndex"
																style="border: 0; background: transparent;"
																onmouseover="showButtonStyle('operator3');"
																onmouseout="hideStyle('operator3')">
																		执行操作
															</button>
										    </form>
							    </td>
				      </tr>
				      --%>
				     
				     <tr>
							    <td width="10%"><%=PlatformLocalesJSTL.getBundleValueforSearch("search.config.operator.deleteIndex.caption")%></td> <%-- 删除索引文件 --%>
							    <td width="50%"  class="noline1"><%=PlatformLocalesJSTL.getBundleValueforSearch("search.config.operator.deleteIndex.describe")%></td> <%-- 强行删除所有的索引文件：用于处理异常的索引文件，先删除后重建。通常不推荐使用，仅适于用多次建立索引依然无法成功的情况。 --%>
							    <td width="10%"  class="noline2" ></td>
							    <td width="10%"  align="center">
												 <form id="rebuildIndexForm" name="rebuildIndexForm" method="post" action="<c:url value="<%= SearchPathUtil.SEARCH_DELETE_ALL_INDEX_FILE_ACTION%>"/>" style="display:inline;">
															<button type="submit" id="operator4" name="rebuildIndex"
																style="border: 0; background: transparent;"
																onmouseover="showButtonStyle('operator4');"
																onmouseout="hideStyle('operator4')"
																onclick="return confirmOperator('操作将删除索引文件, 删除后搜索请求将不能返回任何结果, 您确认要进行此项操作吗？')">
																		<%=PlatformLocalesJSTL.getBundleValueforSearch("search.config.operator.execute")%><%-- 执行操作 --%>
																</button><br />
										     </form>
							    </td>
				     </tr>
		</table>
		
		<div class="STYLE1" align="center" style="margin-bottom: 12px;margin-top: 4px"> 
	   				<span>&nbsp;${indexMessage}</span>
		</div>