<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page import="avicit.platform6.modules.system.sysfulltextsearch.util.searchSysPath.SearchPathUtil" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@page import="avicit.platform6.core.api.system.syslocale.PlatformLocalesJSTL"%>

<html>
		<head>
		
					<style type="text/css" >
						　　<!--
						　　　.tnt {Writing-mode:tb-rl;Text-align:left;font-size:13px}
						　　-->
　　                 </style>
					<script type="text/javascript">
								<!--
								function reset() {
                                     document.segementForm.source.innerHTML = "";
									 document.segementForm.segement.innerHTML = "";
								}
								
				                function submit() {
							    		document.segementForm.submit();
				                }
								//-->
					</script>
		</head>
		<body><center>
								 <form id="segementForm" name="segementForm" action="<c:url value="<%= SearchPathUtil.LEXICON_SEGMENT_ACTION %>"/>" style="margin:0px;display: inline;">
								         <table bgcolor="#A3F1B2"><tr>
								                     <td>
															 	<table>		
															 	            <tr><td align="center"><span style="font-size:14px;"><%=PlatformLocalesJSTL.getBundleValueforSearch("search.lexicon.source")%></span></td></tr> <%--原文 --%>
															                <tr><td><textarea id="sourcePackage"  name="source" style="width:525px; height: 70px; overflow-x: auto; overflow-y: hidden; box-sizing: border-box;">${source}</textarea></td></tr>
															    </table>	
												    </td>
												    <td>
															     <table>	
															                <tr><td align="center"><span style="font-size:14px;"><%=PlatformLocalesJSTL.getBundleValueforSearch("search.lexicon.result")%></span></td></tr> <%--结果 --%>
																			<tr><td><textarea id="segementPackage" name="segement" disabled="disabled" style="width:525px; height: 70px;overflow-x: auto; overflow-y: hidden; box-sizing: border-box;">${segement}</textarea></td></tr>
																</table>	
													</td>
										</tr></table>
									</form>
									<table align="center"  style="padding: 0px;margin: 0px;"><tr align="center"><td align="center">
											<button onclick="submit();"><%=PlatformLocalesJSTL.getBundleValueforSearch("search.lexicon.segment")%><%-- 分词 --%></button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<button onclick="reset();"><%=PlatformLocalesJSTL.getBundleValueforSearch("search.lexicon.reload")%><%--重置 --%></button>
									</td></tr></table>											
		</center></body>
</html>