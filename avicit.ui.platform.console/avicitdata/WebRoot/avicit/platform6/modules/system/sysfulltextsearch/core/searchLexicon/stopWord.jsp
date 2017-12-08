<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page import="avicit.platform6.modules.system.sysfulltextsearch.util.searchSysPath.SearchPathUtil" %>
<%@page import="avicit.platform6.commons.utils.ViewUtil"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@page import="avicit.platform6.core.api.system.syslocale.PlatformLocalesJSTL"%>

<html >
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
								var flag = confirm("确认保存当前结果么?");
								if (flag) {
									document.stopWord.action = "<%= SearchPathUtil.LEXICON_SAVE_STOPWORD_ACTION %>";
								    document.stopWord.submit();
							    }
						}
						
						function show() {
							confirm("确认重置页面么?未保存的结果将丢失。") ;
			                init();
						}
						
						function init() {
							document.stopWord.action="<%= SearchPathUtil.LEXICON_GET_STOPWORD_ACTION %>";
						    document.stopWord.submit(); 
						}
						//-->
			</script>
	</head>
	
	<body  style="margin:1;padding:0;">
	            <form id="stopWord" name="stopWord" action="<c:url value="<%= SearchPathUtil.LEXICON_GET_STOPWORD_ACTION %>"/>" method="post" style="margin:0px;display: inline;"> 
						<table align="center" bgcolor="#A3F1B2" >
								<tr><td  colspan="2" align="center"><span style="font-size: 13px;"><%=PlatformLocalesJSTL.getBundleValueforSearch("search.lexicon.stopwords")%></span></td></tr><%--停用词--%>
						        <tr><td  colspan="2" align="center">					     
													<textarea id="stopWordAsWhole" name="stopWordAsWhole" title='<%=PlatformLocalesJSTL.getBundleValueforSearch("search.lexicon.stopwords")%>' cols="25" rows="15" style="font-size: medium;width: 245px;height: 245px;overflow-x: auto; overflow-y: auto; box-sizing: border-box;" dir="ltr">${returnStopWordAsWhole}</textarea>	
								</td></tr>								
								<tr>
										<td align="right" ><button onclick="save();	"><span><%=PlatformLocalesJSTL.getBundleValueforSearch("search.lexicon.save")%></span></button>&nbsp;&nbsp;&nbsp;</td> <%--保存 --%>
										<td  align="left" >&nbsp;&nbsp;&nbsp;<button onclick="return show();"><span><%=PlatformLocalesJSTL.getBundleValueforSearch("search.lexicon.reload")%></span></button></td> <%--重置 --%>
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