<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page import="avicit.platform6.modules.system.sysfulltextsearch.util.searchSysPath.SearchPathUtil" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>

	<title>词典维护</title>
	<style type="text/css">
		<!--
		body,div,span{
			background:#fff;
		}
		
		#searchLexicon {
			width: 100%;
			height: 300px;
			/*border: 1px solid;*/
		}
	
		#searchSegmentation{
			margin-top: 5px;
			width: 100%;
			/*border: 1px solid;*/	
		}
			
		.wordEditor{
			margin-left: 50px;
		    margin-right: 50px;
		    font-size:12px;
		    text-align:left;		    
		}
		
		#segementPackage{
			margin-top: 10px;
		}
		// -->
	</style>
	
	<script type="text/javascript" language="javascript"> 
		<!--
					function iFrameHeight() { 
			                try{
										var ifm= document.getElementById("iframepage"); 
										var subWeb = document.frames ? document.frames["iframepage"].document : ifm.contentDocument; 
										if(ifm != null && subWeb != null) { 
												ifm.height = subWeb.body.scrollHeight; 
										} 
							}catch(e) {
								      // do nothing,  just for IE6
							}
					}
		// -->
	</script>

</head>
<!--  <body bgcolor="#C7EDCC"> -->
<body>
		<div id="searchLexicon" align="center">
		        <table>
					        <tr>
								        <td width="270"> 
								        		<iframe src="../core/searchLexicon/stopWord.jsp" width="270" height="320" frameborder="0"  scrolling="no"  width="100%" height="100%" onLoad="iFrameHeight()">	</iframe>
										</td>
										
										<td width="270">
												<iframe src="../core/searchLexicon/extendWord.jsp" width="270" height="320" frameborder="0"  scrolling="no"  width="100%" height="100%" onLoad="iFrameHeight()">	</iframe>
										</td>
										
										<td width="270">
												<iframe src="../core/searchLexicon/hotWord.jsp" width="270" height="320" frameborder="0"  scrolling="no"  width="100%" height="100%" onLoad="iFrameHeight()">	</iframe>
										</td>
										
										<td width="270">
												<iframe src="../core/searchLexicon/hotInput.jsp" width="270" height="320" frameborder="0"  scrolling="no"  width="100%" height="100%" onLoad="iFrameHeight()">	</iframe>
										</td>
							</tr>			
				</table>
		</div>
		
		<div  id="searchSegmentation" align="center" style="margin-top: 30px;">
		            <hr width="83%">
					<iframe src="../core/searchLexicon/segement.jsp"  width="1090px" height="160" frameborder="0"  scrolling="no"  width="100%" height="100%" onLoad="iFrameHeight()" >	</iframe>
		</div>
		
</body>
</html>