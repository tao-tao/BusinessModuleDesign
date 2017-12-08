<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page import="avicit.platform6.modules.system.sysfulltextsearch.util.searchSysPath.SearchPathUtil" %>
<%@page import="avicit.platform6.commons.utils.ViewUtil"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@page import="avicit.platform6.core.api.system.syslocale.PlatformLocalesJSTL"%>

<html>
	<head>
	
		    <base href="<%=ViewUtil.getRequestPath(request)%>">                   <%-- 注意：移植的时候一起拿走 --%>
		    
		    <style type="text/css">                                                                       <%-- 为了在IE6下显示一样，特设的样式 --%>
						<!--
								button{  
  										overflow:visible;  
  										padding: 0 .5em;
  										margin: 0;
  								}
						-->
             </style>
             
			<script type="text/javascript">
			<!--
			
			function save() {
				var flag = confirm("确认保存当前结果么?") ;
				if(flag) {
						document.extWord.action="<%= SearchPathUtil.LEXICON_SAVE_EXTENDWORD_ACTION %>";
					    document.extWord.submit();
				}
			}
			
			function show() {
				confirm("确认重置页面么?未保存的结果将丢失。") ;
                init();
			}
			
			function init() {
				document.extWord.action="<%= SearchPathUtil.LEXICON_GET_EXTENDWORD_ACTION %>";
			    document.extWord.submit(); 
			}
			//-->
			</script>
	</head>
	
	<body  style="margin:1;padding:0;">     
			<form id="extWord" name="extWord" action="<c:url value="<%= SearchPathUtil.LEXICON_GET_EXTENDWORD_ACTION %>"/>" method="post">      
							<table align="center" bgcolor="#A3F1B2">
									<tr><td colspan="2" align="center"><span style="font-size: 13px;"><%=PlatformLocalesJSTL.getBundleValueforSearch("search.lexicon.extendwords")%></span></td></tr> <%--扩展词--%>
							        <tr><td colspan="2">
													<textarea id="extWordAsWhole" name="extWordAsWhole" title='<%=PlatformLocalesJSTL.getBundleValueforSearch("search.lexicon.extendwords")%>' cols="25" rows="15" style="font-size: medium;width: 245px;height: 245px;overflow-x: auto; overflow-y: auto; box-sizing: border-box;" dir="ltr">${returnExtWordAsWhole}</textarea>
									</td></tr>
									<tr>
												<td align="right"><button onclick="save();" ><%=PlatformLocalesJSTL.getBundleValueforSearch("search.lexicon.save")%></button>&nbsp;&nbsp;&nbsp;</td>   <%-- 保存 --%>
												<td align="left">&nbsp;&nbsp;&nbsp;<button onclick="return show();"><%=PlatformLocalesJSTL.getBundleValueforSearch("search.lexicon.reload")%></button>	</td> <%--重置 --%>
				               		</tr>	
						  </table>
			</form>
							
		        <c:if test="${init != 'alreadyInit'}">  
					<script type="text/javascript">
							<!--	
									init();
							//-->
					</script>
	            </c:if>
	            			
	</body>

</html>

