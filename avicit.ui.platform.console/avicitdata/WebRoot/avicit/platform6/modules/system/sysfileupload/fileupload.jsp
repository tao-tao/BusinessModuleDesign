<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page language="java"  pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@page import="avicit.platform6.commons.utils.ViewUtil"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>SWFUpload Example - Multiple Uploaders</title>
<link rel="stylesheet" type="text/css" href="css/fileupload.css">
<script type="text/javascript" src="js/jquery-1.8.2.min.js"></script>
<script type="text/javascript" src="js/jquery.swfupload.js"></script>
<script type="text/javascript" src="js/swfupload.js"></script>
<script type="text/javascript">
	var baseUrl = "<%=ViewUtil.getRequestPath(request)%>";
	var fileuploadArg = {
		fileuploadNum : "",
		fileuploadType : "",
		fileuploadBusinessId : "abc",
		fileuploadBusinessTableName : "123",
		fileuploadIsSaveToDatabase : "",
		fileuploadPathImpl : ""
	};

</script>
<script type="text/javascript" src="js/fileupload.js"></script>
</head>
<body>
<table width="100%" border="1" >
  <tr>
    <td width="60px" align="right">附件:</td>
    <td align="left">
    	<div id="swfupload-control">
        	<div><span id="spanButtonPlaceHolder"></span></div>
        </div>
    	<div style="" class="X" id="divComposeAttachArea">
        	<div id="divComposeAttachContent" style="width: 100%;"></div>
        </div>
    </td>
  </tr>
</table>
</body>
</html>