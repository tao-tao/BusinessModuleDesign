<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<html>
<head>

	<base href="<%=ViewUtil.getRequestPath(request)%>">
	<jsp:include page="/avicit/platform6/bpmclient/bpm/ProcessCommonJsInclude.jsp"></jsp:include>
	<title>添加移交项</title>
	<style>
		body td{
			font-family: Microsoft Yahei,sans-serif,Arial,Helvetica;
			font-size: 12px;
			padding-left: 0.5em;
		}
	</style>
	 <script type="text/javascript">
	 var baseurl = '<%=request.getContextPath()%>';
	   $(function(){
		   init();
		   //接受人绑定选人及部门事件注册
		   $("#receptUserName").bind("click",function(){
			   parent.selectUserDept();
		   });
	   });
       /**
        *初始化页面
        */
	   function init(){
		   ajaxRequest("POST","","platform/bpm/clientbpmWorkHandAction/addWorkHand","json","initWorkHand");
	   }
	   /**
        *初始化页面的回调函数
        */
	   function  initWorkHand(retValue){
	    	var workHandVo=retValue.workHand;
	    	$("#workOwnerId").attr("value",workHandVo.workOwnerId); 
	    	$("#workOwnerName").attr("value",workHandVo.workOwnerName); 
	    	$("#workOwnerDeptId").attr("value",workHandVo.workOwnerDeptId); 
	    	$("#workOwnerDeptName").attr("value",workHandVo.workOwnerDeptName); 
	    	$("#receptUserId").attr("value",workHandVo.receptUserId); 
	    	$("#receptDeptId").attr("value",workHandVo.receptDeptId); 
	    	$("#receptUserName").attr("value",workHandVo.receptUserName); 
	    	$("#receptDeptName").attr("value",workHandVo.receptDeptName); 
	    	$("#handDate").attr("value",(new Date()).Format("yyyy-MM-dd")); 
	    }
	  
		</script>
</head>

<body  >
       <form id="workhandForm" method="post">
        <table class="tableForm" width="100%" border=0>
				<tr>
					<td >移交人</td>
					<td><input name="workOwnerId"  id="workOwnerId" style="display:none;" /><input class="easyui-validatebox"  name="workOwnerName" id="workOwnerName" readonly="readonly"></input>
					</td>
					<td>移交部门</td>
					<td><input name="workOwnerDeptId" id="workOwnerDeptId" class="easyui-validatebox"  style="display:none;" /><input  class="easyui-validatebox"  name="workOwnerDeptName"  id="workOwnerDeptName" readonly="readonly" /></td>
				</tr>
				<tr>
					<td>接受人</td>
					<td><input name="receptUserId" id="receptUserId"  style="display:none;" /><input class="easyui-validatebox"  name="receptUserName"   id="receptUserName" required="true" ></input>
					<img  src="static/images/platform/bpm/client/images/addUserDept.gif" title="选择人员部门" onclick="parent.selectUserDept();" style="align:center;"/>
					</td>
					<td>接受部门</td>
					<td><input name="receptDeptId" id="receptDeptId" class="easyui-validatebox"  style="display:none;"/><input  class="easyui-validatebox"  id="receptDeptName"  name="receptDeptName" readonly="readonly" required="true" /></td>
				</tr>
				<tr>
				    <td>登记日期</td>
					<td><input name="handDate"  id="handDate" class="easyui-validatebox"  style="width: 150px;"  readonly="readonly"  /></td>
					<td>生效日期</td>
					<td><input name="handEffectiveDate" id="handEffectiveDate" class="easyui-datebox"  style="width: 150px;"  required="true" /></td>
				</tr>
				<tr >
					<td>预计返回日期</td>
					<td><input name="backDate"  id="backDate" class="easyui-datebox" style="width: 150px;" required="true" /></td>
				    <td>是否有效</td>
					<td><select name="validFlag" class="easyui-combobox" style="width:150px;" >
						  <option value="1">有效</option>
						  <option value="0">无效</option>
					</select></td>
				</tr>
				<tr>
					<td>移交原因</td>
					<td colspan="3"><textarea name="handReason" class="easyui-validatebox" style="height:80px;width:448px;"></textarea>
					</td>
				</tr>
			</table>
		</form>
</body>
</html>

