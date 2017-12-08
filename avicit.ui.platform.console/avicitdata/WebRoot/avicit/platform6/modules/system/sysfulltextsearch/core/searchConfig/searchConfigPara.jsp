<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@page import="org.springframework.beans.factory.annotation.Autowired"%>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page import="avicit.platform6.modules.system.sysfulltextsearch.util.searchSysPath.SearchPathUtil" %>
<%@ page import="avicit.platform6.modules.system.sysfulltextsearch.action.SearchEngineConfigAction" %>
<%@ page import="avicit.platform6.core.spring.SpringFactory" %>
<%@ page import="org.springframework.web.servlet.ModelAndView" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
<%@ page import="org.springframework.web.servlet.ModelAndView" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="avicit.platform6.core.api.system.syslocale.PlatformLocalesJSTL"%>

<%
		SearchEngineConfigAction searchEngineConfigAction = SpringFactory.getBean("searchEngineConfigAction"); 
        // ModelAndView mav = searchEngineConfigAction.getAllPara();
        // Map<String, Object> modelMap =  mav.getModel();
        // HashMap<String, String> allParaMap = (HashMap<String, String>)modelMap.get("allParaMap");
%>

		<div class="title" align="center"   style="margin-top: 10px">
		   		   		 <%=PlatformLocalesJSTL.getBundleValueforSearch("search.config.parameter.caption")%> <%-- 维护搜索模块的配置信息，了解其含义后方可修改。其中参数修改，需重启系统后才会生效。--%> 
		</div>
		 
		<table width="100%" id="searchEngineConfigTable"  border="1" class="t1">
			 <tr bgcolor="#EFEFEF">
				    <td width="10%" bgcolor="#EFEFEF" align="center"  bordercolor="#9F9F9F"><%=PlatformLocalesJSTL.getBundleValueforSearch("search.config.parameter.parameterName")%></td> <%-- 参数名称--%> 
				    <td width="50%" bgcolor="#EFEFEF" align="center"  bordercolor="#9F9F9F"><%=PlatformLocalesJSTL.getBundleValueforSearch("search.config.parameter.parameterDescribe")%></td> <%-- 简要说明--%> 
				    <td width="10%" bgcolor="#EFEFEF" align="center"  bordercolor="#9F9F9F"><%=PlatformLocalesJSTL.getBundleValueforSearch("search.config.parameter.parameterValue")%></td> <%-- 参数取值--%> 
				    <td width="10%" bgcolor="#EFEFEF" align="center"  bordercolor="#9F9F9F"><%=PlatformLocalesJSTL.getBundleValueforSearch("search.config.parameter.parameterSave")%></td> <%-- 点击保存--%> 
			</tr>
		  
		   <tr>
			     <form id="#" name="#" method="post" action="<c:url value="<%= SearchPathUtil.SEARCH_ENGINE_SETPARA_ACTION %>"/>"  > 
						    <td width="10%">  <span>databaseSource</span> </td>
						    <td width="50%" > <span> <%=PlatformLocalesJSTL.getBundleValueforSearch("search.config.parameter.databaseSource")%></span> 
						    		<input id ="key" name="key" value="data.databaseSource" style="width: 0px;display: none;">       <!-- 传值用的隐藏控件 -->
						    </td>
						    
						    <td width="10%" align="center">
						    		<input id="text0" type="text" value="<%= searchEngineConfigAction.getPara1("data.databaseSource") %>" name="value" size="15"
												maxlength="63" align="left"
												style="border: 0; background: transparent;"
												onmouseover="showTextStyle('text0')"
												onmouseout="hideStyle('text0')"/>
							</td>
							
							<td width="10%" align="center">				
														<button id="para0" type="submit" id="#" name="#"
																style="border: 0; background: transparent;"
																onmouseover="showButtonStyle('para0')"
																onmouseout="hideStyle('para0')">
																<%=PlatformLocalesJSTL.getBundleValueforSearch("search.config.parameter.save")%> <%--保存修改--%>
														</button><br />		
						    </td>
					</form>		    
				</tr>
				
		   <tr>
			     <form id="#" name="#" method="post" action="<c:url value="<%= SearchPathUtil.SEARCH_ENGINE_SETPARA_ACTION %>"/>"  > 
						    <td width="10%">  <span>dataBasepath</span> </td>
						    <td width="50%" > <span><%=PlatformLocalesJSTL.getBundleValueforSearch("search.config.parameter.dataBasepath")%></span>  <%-- 数据文件存放位置: 可以写完整路径，如E:/search/data; 也可只写文件夹名字，如search_data，将存放在项目的WEB-INF下。 --%>
						    		<input id ="key" name="key" value="data.basepath" style="width: 0px;display: none;">
						    </td>
						    
						    <td width="10%" align="center">
						    		<input id="text1" type="text" value="<%= searchEngineConfigAction.getPara1("data.basepath") %>" name="value" size="15"
												maxlength="63" align="left"
												style="border: 0; background: transparent;"
												onmouseover="showTextStyle('text1')"
												onmouseout="hideStyle('text1')"/>
							</td>
							
							<td width="10%" align="center">				
														<button id="para1" type="submit" id="#" name="#"
																style="border: 0; background: transparent;"
																onmouseover="showButtonStyle('para1')"
																onmouseout="hideStyle('para1')">
																<%=PlatformLocalesJSTL.getBundleValueforSearch("search.config.parameter.save")%> <%--保存修改--%>
														</button><br />		
						    </td>
					</form>		    
				</tr>
					
		<tr>
		<form id="#" name="#" method="post" action="<c:url value="<%= SearchPathUtil.SEARCH_ENGINE_SETPARA_ACTION %>"/>" >
					<td width="10%">  <span>indexBasepath</span> </td>
				    <td width="50%" > <span><%=PlatformLocalesJSTL.getBundleValueforSearch("search.config.parameter.indexBasepath")%></span>  <%-- 索引存放位置, 可以写完整路径，如E:/search/index; 也可只写文件夹名字，如lucene_index，将存放在项目的WEB-INF下。 --%>
				    		<input id ="key" name="key" value="index.basepath" style="width: 0px;display: none;">
				    </td>
				    <td width="10%"  align="center">
				    		<input id="text2" type="text" value="<%=searchEngineConfigAction.getPara1("index.basepath") %>" name="value" size="15"
										maxlength="63" align="left"
										style="border: 0; background: transparent;"
										onmouseover="showTextStyle('text2')"
										onmouseout="hideStyle('text2')"/>
					</td>
					
				    <td width="10%" align="center">
												<button id="para2" type="submit" id="#" name="#"
														style="border: 0; background: transparent;"
														onmouseover="showButtonStyle('para2')"
														onmouseout="hideStyle('para2')">
														<%=PlatformLocalesJSTL.getBundleValueforSearch("search.config.parameter.save")%> <%--保存修改--%>
												</button><br />
				    </td>
				    </form>
		  </tr>
		  
		  <tr class="a1">
		  		<form id="#" name="#" method="post" action="<c:url value="<%= SearchPathUtil.SEARCH_ENGINE_SETPARA_ACTION %>"/>" >
				    <td width="10%">  <span>mergeFactor</span> </td>
				    <td width="50%" > <span><%=PlatformLocalesJSTL.getBundleValueforSearch("search.config.parameter.mergeFactor")%></span>  <%-- 合并因子：正整数，如10， 1000；内存越大，可以设置的越大，性能也会越好;  但设置太大将导致out of memory。 --%>
				    			<input id ="key" name="key" value="index.mergeFactor" style="width: 0px;display: none;">
				    </td>
				    <td width="10%"  align="center">
				    		<input id="text3" type="text" value="<%=searchEngineConfigAction.getPara1("index.mergeFactor") %>" name="value" size="15"
										maxlength="63" align="left"
										style="border: 0; background: transparent;"
										onmouseover="showTextStyle('text3')"
										onmouseout="hideStyle('text3')"/>
					</td>
				    <td width="10%" align="center">
												<button id="para3" type="submit" id="#" name="#"
														style="border: 0; background: transparent;"
														onmouseover="showButtonStyle('para3')"
														onmouseout="hideStyle('para3')">
														<span><%=PlatformLocalesJSTL.getBundleValueforSearch("search.config.parameter.save")%></span> <%--保存修改--%>
												</button><br />
				    </td>
				    </form>
		  </tr>
		  
		  <tr>
		   <form id="#" name="#" method="post" action="<c:url value="<%= SearchPathUtil.SEARCH_ENGINE_SETPARA_ACTION %>"/>" >
				   <td width="10%"> <span>maxBufferedDocs</span></td>
				    <td width="50%" ><span><%=PlatformLocalesJSTL.getBundleValueforSearch("search.config.parameter.maxBufferedDocs")%></span>  <%-- 内存中最大缓存文档数量，正整数，如100；部分搜索结果占用一部分内存，起缓存的作用，可用内存越大，可设置的值越大。 --%>
				    		<input id ="key" name="key" value="index.maxBufferedDocs" style="width: 0px;display: none;">
				    </td>
				    <td width="10%"  align="center">
				    			<input id="text4" type="text" value="<%=searchEngineConfigAction.getPara1("index.maxBufferedDocs") %>" name="value" size="15"
										maxlength="63" align="left"
										style="border: 0; background: transparent;"
										onmouseover="showTextStyle('text4')"
										onmouseout="hideStyle('text4')"/>
				    </td>
				    <td width="10%" align="center">			
								    			<button id="para4" type="submit" id="#" name="#"
														style="border: 0; background: transparent;"
														onmouseover="showButtonStyle('para4')"
														onmouseout="hideStyle('para4')">
														<span><%=PlatformLocalesJSTL.getBundleValueforSearch("search.config.parameter.save")%></span>  <%--保存修改--%>
												</button><br />
					</td>
					</form>
		  </tr>
		  
		  <tr class="a1">
		  <form id="#" name="#" method="post" action="<c:url value="<%= SearchPathUtil.SEARCH_ENGINE_SETPARA_ACTION %>"/>" >
				    <td width="10%"> <span>maxNumSegments</span></td>
				    <td width="50%" ><span><%=PlatformLocalesJSTL.getBundleValueforSearch("search.config.parameter.maxNumSegments")%></span> <%-- 索引中物理段的数目：正整数，如1，2，3；索引数据较少时，段的数目越小搜索速度越快，最小为1。一般数量级下，只用“1”。--%>
				    			<input id ="key" name="key" value="index.maxNumSegments" style="width: 0px;display: none;">
				    </td>
				    <td width="10%"  align="center">
	    					    			<input id="text5" type="text" value="<%=searchEngineConfigAction.getPara1("index.maxNumSegments") %>" name="value" size="15"
													maxlength="63" align="left"
													style="border: 0; background: transparent;"
													onmouseover="showTextStyle('text5')"
													onmouseout="hideStyle('text5')"/>
				    </td>
				    <td width="10%" align="center">
				    				
												<button id="para5" type="submit" id="#" name="#"
														style="border: 0; background: transparent;"
														onmouseover="showButtonStyle('para5')"
														onmouseout="hideStyle('para5')">
														<span><%=PlatformLocalesJSTL.getBundleValueforSearch("search.config.parameter.save")%></span> <%--保存修改--%>
												</button><br />
									
				    </td>
				    </form>
		  </tr>

		  <tr class="a1">
		  <form id="#" name="#" method="post" action="<c:url value="<%= SearchPathUtil.SEARCH_ENGINE_SETPARA_ACTION %>"/>" >
				    <td width="10%"><span>timeExpression</span></td>
				    <td width="50%" ><span><%=PlatformLocalesJSTL.getBundleValueforSearch("search.config.parameter.timeExpression")%></span>  
				                                                         <%-- 定时刷新索引时间：7部分，空格分开： 1.秒 0-59
                                                                                                        2.分钟 0-59
                                                                                                        3.小时 0-23
                                                                                                        4.月份中的日期 1-31
                                                                                                        5.月份 1-12
                                                                                                        6.星期日期 1-7
                                                                                                        7.年份（可省略）--%>
				    		<input id ="key" name="key" value="index.schedule.expression" style="width: 0px;display: none;">   
				    </td>
				    <td width="10%"  align="center">
	    					    			<input id="text6" type="text" value="<%=searchEngineConfigAction.getPara1("index.schedule.expression") %>" name="value" size="15"
													maxlength="63" align="left"
													style="border: 0; background: transparent;"
													onmouseover="showTextStyle('text6')"
													onmouseout="hideStyle('text6')"/>
				    </td>
				    <td width="10%" align="center">
												<button id="para6" type="submit" id="#" name="#"
														style="border: 0; background: transparent;"
														onmouseover="showButtonStyle('para6')"
														onmouseout="hideStyle('para6')">
														<span><%=PlatformLocalesJSTL.getBundleValueforSearch("search.config.parameter.save")%></span> <!--保存修改-->
												</button><br />
				    </td>
				      </form>
		  </tr>

		  		  <tr>
		  		  <form id="#" name="#" method="post" action="<c:url value="<%= SearchPathUtil.SEARCH_ENGINE_SETPARA_ACTION %>"/>" >
					    <td width="10%"><span>relation</span></td>
					    <td width="50%" ><span><%=PlatformLocalesJSTL.getBundleValueforSearch("search.config.parameter.relation")%></span>   <%-- 相关搜索关键字个数：非负整数；用于搜索相关结果的关键词个数。1，2，5等，设置太大将没有意义。 --%>
					    		<input id ="key" name="key" value="search.relation" style="width: 0px;display: none;">
					    </td>
					    <td width="10%"  align="center">
					    						   <input id="text7" type="text" value="<%=searchEngineConfigAction.getPara1("search.relation") %>" name="value" size="15"
														maxlength="63" align="left"
														style="border: 0; background: transparent;"
														onmouseover="showTextStyle('text7')"
														onmouseout="hideStyle('text7')"/>
					    </td>
					    <td width="10%" align="center">
										    				<button id="para7" type="submit" id="#" name="#"
																	style="border: 0; background: transparent;"
																	onmouseover="showButtonStyle('para7')"
																	onmouseout="hideStyle('para7')">
																	<span><%=PlatformLocalesJSTL.getBundleValueforSearch("search.config.parameter.save")%></span> <%--保存修改--%>
															</button><br />
					    </td>
					      </form>
		  </tr>
		  
		  <tr class="a1">
		  <form id="#" name="#" method="post" action="<c:url value="<%= SearchPathUtil.SEARCH_ENGINE_SETPARA_ACTION %>"/>" >
				    <td width="10%"><span>fragmenters</span></td>
				    <td width="50%" ><span><%=PlatformLocalesJSTL.getBundleValueforSearch("search.config.parameter.fragmenters")%></span>   <%-- 显示时读取的字符数，即搜索结果中包含搜索关键字的显示文本的长度。--%>
				    			<input id ="key" name="key" value="search.fragmenters" style="width: 0px;display: none;">
				    </td>
				    <td width="10%"  align="center">
				    							 <input id="text8" type="text" value="<%=searchEngineConfigAction.getPara1("search.fragmenters") %>" name="value" size="15"
														maxlength="63" align="left"
														style="border: 0; background: transparent;"
														onmouseover="showTextStyle('text8')"
														onmouseout="hideStyle('text8')"/>
				    </td>
				    <td width="10%" align="center">
				    									<button id="para8" type="submit" id="#" name="#"
																style="border: 0; background: transparent;"
																onmouseover="showButtonStyle('para8')"
																onmouseout="hideStyle('para8')">
																<span><%=PlatformLocalesJSTL.getBundleValueforSearch("search.config.parameter.save")%></span> <%--保存修改--%>
														</button><br />
				    </td>
				      </form>
		  </tr>
		  
		  <tr>
		  <form id="#" name="#" method="post" action="<c:url value="<%= SearchPathUtil.SEARCH_ENGINE_SETPARA_ACTION %>"/>" >
				    <td width="10%"><span>needHighLight</span></td>
				    <td width="50%" ><span><%=PlatformLocalesJSTL.getBundleValueforSearch("search.config.parameter.needHighLight")%></span>  <%-- 是否需要高亮显示：true , false；搜索结果中关键字是否需要高亮显示。--%>
				    			<input id ="key" name="key" value="search.needHighLight" style="width: 0px;display: none;">
				    </td>
				    <td width="10%"  align="center">
		    					    			<input id="text9" type="text" value="<%=searchEngineConfigAction.getPara1("search.needHighLight") %>" name="value" size="15"
														maxlength="63" align="left"
														style="border: 0; background: transparent;"
														onmouseover="showTextStyle('text9')"
														onmouseout="hideStyle('text9')"/>
				    </td>
				    <td width="10%" align="center">
						    					<button id="para9" type="submit" id="#" name="#"
														style="border: 0; background: transparent;"
														onmouseover="showButtonStyle('para9')"
														onmouseout="hideStyle('para9')">
														<span><%=PlatformLocalesJSTL.getBundleValueforSearch("search.config.parameter.save")%></span> <%--保存修改--%>
												</button><br />
				    </td>
				      </form>
		  </tr>
		  
		  <tr class="a1">
		  <form id="#" name="#" method="post" action="<c:url value="<%= SearchPathUtil.SEARCH_ENGINE_SETPARA_ACTION %>"/>" >
					    <td width="10%"><span>color</span></td>
					    <td width="50%" ><span><%=PlatformLocalesJSTL.getBundleValueforSearch("search.config.parameter.color")%></span>  <%-- 高亮显示字体颜色,可以填RGB值,也支持'red', 'blue'等常用颜色字符串 --%>
					    			<input id ="key" name="key" value="search.color" style="width: 0px;display: none;">
					    </td>
					    <td width="10%"  align="center">
					    						<input id="text10" type="text" value="<%=searchEngineConfigAction.getPara1("search.color") %>" name="value" size="15"
														maxlength="63" align="left"
														style="border: 0; background: transparent;"
														onmouseover="showTextStyle('text10')"
														onmouseout="hideStyle('text10')"/>
					    </td>
					    <td width="10%" align="center">
															<button id="para10" type="submit" id="#" name="#"
																	style="border: 0; background: transparent;"
																	onmouseover="showButtonStyle('para10')"
																	onmouseout="hideStyle('para10')">
																	<span><%=PlatformLocalesJSTL.getBundleValueforSearch("search.config.parameter.save")%></span>  <%--保存修改--%>
															</button><br />
			                                  
					    </td>
					   </form>
		  </tr>		  
		</table>
		
		<div class="STYLE1" align="center" style="margin-bottom: 10px;margin-top: 4px"> 
		   			<span>&nbsp;${tip}</span>
		</div>