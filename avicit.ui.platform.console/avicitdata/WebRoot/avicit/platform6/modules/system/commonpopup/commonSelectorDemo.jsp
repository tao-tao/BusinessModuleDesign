<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<%@ taglib prefix="sec" uri="/WEB-INF/tags/shiro.tld" %>
<html>
<head>
	<title>通用选择DEMO</title>
	<base href="<%=ViewUtil.getRequestPath(request) %>">
	<jsp:include page="/avicit/platform6/component/common/EasyUIJsInclude.jsp"></jsp:include>
	<jsp:include page="/avicit/platform6/modules/system/commonpopup/commonSelectionHead.jsp"></jsp:include>
	<style>
		body td{
			font-family: Microsoft Yahei,sans-serif,Arial,Helvetica;
			font-size: 12px;
			padding-left: 0.5em;
		}
	</style>
	 <script type="text/javascript">
	 var baseurl = '<%=request.getContextPath()%>';
	 //调用方法展示
    jQuery(function(){
    	  //选择人员
		   var commonSelector = new CommonSelector("user","userSelectCommonDialog","receptUserId","receptUserName","receptDeptId","receptDeptName","{\"secretL\":1}",2);
		   commonSelector.init(null,null,'n'); //选择人员  回填部门
		 //选择部门
		   var deptCommonSelector = new CommonSelector("dept","deptSelectCommonDialog","selectDeptId","selectDeptName");
		   deptCommonSelector.init(); //选择部门
		   //选择角色
		   var roleCommonSelector = new CommonSelector("role","roleSelectCommonDialog","selectRoleId","selectRoleName",null,null,false);
		   roleCommonSelector.init(); //选择角色
		   //选择群组
		   var groupCommonSelector = new CommonSelector("group","groupSelectCommonDialog","selectGroupId","selectGroupName",null,null,false);
		   groupCommonSelector.init(); //选择群组
		   //选择岗位
		   var positionCommonSelector = new CommonSelector("position","positionSelectCommonDialog","selectPositionId","selectPositionName");
		   positionCommonSelector.init(); //选择岗位
		   //综合选择
		   var param = {splitChar:",","user":{"userId":"receptUserId1","userName":"receptUserName1","selectCount":"-1","tabShow":"1"},"dept":{"deptId":"selectDeptId1","deptName":"selectDeptName1","selectCount":"2","tabShow":"1"},"role":{"roleId":"selectRoleId1","roleName":"selectRoleName1","selectCount":"3","tabShow":"1"},"group":{"groupId":"selectGroupId1","groupName":"selectGroupName1","tabShow":"0"},"position":{"positionId":"selectPositionId1","positionName":"selectPositionName1","tabShow":"0"}};
		    var comprehensiveSelectorSelector = new ComprehensiveSelector("receptUserName1",param);
		    comprehensiveSelectorSelector.init(null,null,'n'); //自动回填选择的数据  第三个参数如果不传或填除'n'以外的值 默认取当前登录人所属组织   填写'n'代表显示多根组织的数据据
		    // comprehensiveSelectorSelector.init(null,"getSelectedData",'n'); //开发人员自定义函数回填选择的数据  第三个参数如果不传或填除'n'以外的值 默认取当前登录人所属组织  填写'n'代表显示多根组织的数据据
		    
		   
	   });
	 
	 function getSelectedData(selectedData){
		 if(selectedData != null && selectedData!="undefined"){
			 var user = selectedData["user"] ;
			 if(user != null && user!="undefined"){
				 $("#receptUserId1").val(user.userId);
				 $("#receptUserName1").val(user.userName);
			 }
			 var dept = selectedData["dept"] ;
            if(dept != null && dept!="undefined"){
            	$("#selectDeptId1").val(dept.deptId);
				 $("#selectDeptName1").val(dept.deptName);
			 }
			 var role= selectedData["role"] ;
             if(role != null && role!="undefined"){
            	 $("#selectRoleId1").val(role.roleId);
				 $("#selectRoleName1").val(role.roleName);
			 }
			 var group = selectedData["group"] ;
             if(group != null && group!="undefined"){
            	 $("#selectGroupId1").val(group.groupId);
				 $("#selectGroupName1").val(group.groupName);
			 }
			 var position = selectedData["position"] ;
             if(position != null && position!="undefined"){
            	 $("#selectPositionId1").val(position.positionId);
				 $("#selectPositionName1").val(position.positionName);
			 }
		 }
	 }
		</script>
</head>

<body  >
       <form id="commonSelectionForm" method="post">
         <div class="formExtendBase">
       	<fieldset>
				<legend>选择人员和部门</legend>
				  <table class="tableForm" width="100%" border=0>
				<tr>
					<td >人员ID</td>
					<td><input name="receptUserId" id="receptUserId"  style="display:block;" readOnly="readOnly"  /><input class="easyui-validatebox"  name="UserName"   id="UserName"  style="display:none;" ></input>
					
					</td>
					<td>人员姓名</td>
					<td><div class="formUnit column"><div class="inputContainer"><input class="easyui-validatebox"  name="receptUserName"   id="receptUserName"  readOnly="readOnly" style="width:200px" ></input> </div></div>
					</td>
				</tr>
				<tr>
					<td>人员归属部门ID</td>
					<td><input name="receptDeptId" id="receptDeptId"  style="display:block;" readOnly="readOnly"  /></input>
					</td>
					<td>人员归属部门名称</td>
					<td><div class="formUnit column"><div class="inputContainer"><input class="easyui-validatebox"  name="receptDeptName"   id="receptDeptName"  readOnly="readOnly" style="width:200px;" ></input></div></div>
					   <!-- <img  src="<%=request.getContextPath()%>/avicit/platform6/component/js/jQuery/jquery-easyui-1.3.4/themes/icons_ext/addUserDept.gif " title="选择人员部门" onclick=" selectUserClick();" style="align:center;"/> --> 
					</td>
				</tr>
				
			</table>
			</fieldset>
          	<fieldset>
				<legend>选择部门</legend>
				  <table class="tableForm" width="100%" border=0>
				<tr>
				<tr>
					<td>部门ID</td>
					<td><input name="selectDeptId" id="selectDeptId"  style="display:block;" readOnly="readOnly"   /></input>
					</td>
					<td>部门名称</td>
					<td><div class="formUnit column"><div class="inputContainer"><input class="easyui-validatebox"  name="selectDeptName"   id="selectDeptName" readOnly="readOnly"  style="width:200px;" ></input></div></div>
					</td>
				</tr>
				
			</table>
			</fieldset>
			<fieldset>
				<legend>选择角色</legend>
				  <table class="tableForm" width="100%" border=0>
				<tr>
				<tr>
					<td>角色ID</td>
					<td><input name="selectRoleId" id="selectRoleId"  style="display:block;"  readOnly="readOnly"  /></input>
					</td>
					<td>角色名称</td>
					<td><div class="formUnit column"><div class="inputContainer"><input class="easyui-validatebox"  name="selectRoleName"   id="selectRoleName"  readOnly="readOnly" style="width:200px;" ></input></div></div>
					</td>
				</tr>
				
			</table>
			</fieldset>
			<fieldset>
				<legend>选择群组</legend>
				  <table class="tableForm" width="100%" border=0>
				<tr>
				<tr>
					<td>群组ID</td>
					<td><input name="selectGroupId" id="selectGroupId"  style="display:block;" readOnly="readOnly"  /></input>
					</td>
					<td>群组名称</td>
					<td><div class="formUnit column"><div class="inputContainer"><input class="easyui-validatebox"  name="selectGroupName"   id="selectGroupName"  readOnly="readOnly" style="width:200px;" ></input></div></div>
					</td>
				</tr>
				
			</table>
			</fieldset>
			<fieldset>
				<legend>选择岗位</legend>
				  <table class="tableForm" width="100%" border=0>
				<tr>
				<tr>
					<td>岗位ID</td>
					<td><input name="selectPositionId" id="selectPositionId"  style="display:block;" readOnly="readOnly"  /></input>
					</td>
					<td>岗位名称</td>
					<td><div class="formUnit column"><div class="inputContainer"><input class="easyui-validatebox"  name="selectPositionName"   id="selectPositionName"  readOnly="readOnly" style="width:200px;" ></input></div></div>
					</td>
				</tr>
				
			</table>
			</fieldset>
			<fieldset>
				<legend>综合选择</legend>
				  <table class="tableForm" width="100%" border=0>
				 <tr>
					<td >人员ID</td>
					<td><input name="receptUserId1" id="receptUserId1"  style="display:block;" readOnly="readOnly" /></input>
					
					</td>
					<td>人员姓名</td>
					<td><div class="formUnit column"><div class="inputContainer"><input class="easyui-validatebox"  name="receptUserName1"   id="receptUserName1"  readOnly="readOnly" style="width:200px;" ></input></div></div>
					</td>
				</tr>
				<tr>
					<td>部门ID</td>
					<td><input name="selectDeptId1" id="selectDeptId1" style="display:block;"  readOnly="readOnly" /></input>
					</td>
					<td>部门名称</td>
					<td><div class="formUnit column"><div class="inputContainer"><input class="easyui-validatebox"  name="selectDeptName1"   id="selectDeptName1"  readOnly="readOnly" style="width:200px;" ></input></div></div>
					</td>
				</tr>
					<tr>
					<td>角色ID</td>
					<td><input name="selectRoleId1" id="selectRoleId1"  style="display:block;"  readOnly="readOnly"  /></input>
					</td>
					<td>角色名称</td>
					<td><div class="formUnit column"><div class="inputContainer"><input class="easyui-validatebox"  name="selectRoleName1"   id="selectRoleName1"  readOnly="readOnly" style="width:200px;" ></input></div></div>
					</td>
				</tr>
				<tr>
					<td>群组ID</td>
					<td><input name="selectGroupId1" id="selectGroupId1"  style="display:block;"  readOnly="readOnly"  /></input>
					</td>
					<td>群组名称</td>
					<td><div class="formUnit column"><div class="inputContainer"><input class="easyui-validatebox"  name="selectGroupName1"   id="selectGroupName1"  readOnly="readOnly" style="width:200px;" ></input></div></div>
					</td>
				</tr>
				<tr>
					<td>岗位ID</td>
					<td><input name="selectPositionId1" id="selectPositionId1"  style="display:block;"  readOnly="readOnly" /></input>
					</td>
					<td>岗位名称</td>
					<td><div class="formUnit column"><div class="inputContainer"><input class="easyui-validatebox"  name="selectPositionName1"   id="selectPositionName1"  readOnly="readOnly" style="width:200px;"></input></div></div>
					</td>
				</tr>
				
			</table>
			</fieldset>
		 </div>
		</form>
</body>
</html>

