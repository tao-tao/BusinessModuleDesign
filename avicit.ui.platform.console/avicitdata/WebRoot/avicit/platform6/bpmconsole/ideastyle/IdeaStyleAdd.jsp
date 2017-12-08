<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<html>
<head>
	<%
		String pdId = request.getParameter("pdId");
	%>
	
	<base href="<%=ViewUtil.getRequestPath(request)%>">
	<jsp:include page="/avicit/platform6/bpmclient/bpm/ProcessCommonJsInclude.jsp"></jsp:include>
	<title>添加流程意见样式</title>
	<style>
		body td{
			font-family: Microsoft Yahei,sans-serif,Arial,Helvetica;
			font-size: 12px;
			padding-left: 0.5em;
		}
	</style>
	<script type="text/javascript">
	function selectProcessPosition(){
		var usd = new UserSelectDialog('userSelectCommonDialog', 700, 400,
				getPath() + '/platform/user/bpmSelectUserAction/positionSelectCommon?isMultiple=true',
				'选择岗位');
		var buttons = [ {
			text : '确定',
			id : 'processSubimt',
			//iconCls : 'icon-submit',
			handler : function() {
				var frmId = $('#userSelectCommonDialog iframe').attr('id');
				var frm = document.getElementById(frmId).contentWindow;
				var resultDatas = frm.getSelectedResultDataJson();
				var ids = "";
				var names = "";
				for ( var i = 0; i < resultDatas.length; i++) {
					var resultData = resultDatas[i];
					ids = ids + resultData.positionCode + ",";
					names = names + resultData.positionName + ",";
				}
				if(ids!=null&&ids!=""){
					ids = ids.substring(0,ids.length-1);
					names = names.substring(0,names.length-1);
				}else{
					$.messager.alert('提示', "请选择岗位");
					return ;
				}
				$("#position").val(ids);
				$("#positionName").val(names);
				usd.close();
			}
		} ];
		usd.createButtonsInDialog(buttons);
		usd.show();
	}
	
	
	function selectProcessActivity(){
		var usd = new UserSelectDialog('processActivityDialog', 700, 400,
				getPath() + '/avicit/platform6/bpmconsole/ideastyle/ProcessActivityList.jsp?pdId=<%=pdId%>',
				'选择流程节点');
		var buttons = [ {
			text : '确定',
			id : 'processSubimt',
			//iconCls : 'icon-submit',
			handler : function() {
				var frmId = $('#processActivityDialog iframe').attr('id');
				var frm = document.getElementById(frmId).contentWindow;
				var resultDatas = frm.getSelectedResultDataJson();

				var selfActName = '';
				var selfAlias = '';
				var bpmStyleIds = '';
				var relName = '';
				var processName = '';
				for ( var i = 0; i < resultDatas.length; i++) {
					var bool = false;
					var resultData = resultDatas[i];
					if (typeof(resultData.processType)!= 'undefined') {
						if(resultData.processType == "本流程"){
							bool = true;
						}
					}
					
					if(bool){ //本流程
						if (typeof(resultData.activityName) != 'undefined') {
							selfActName += resultData.activityName + ',';
						}
						if (typeof(resultData.activityAlias) != 'undefined') {
							selfAlias += resultData.activityAlias + ',';
						}
						if (typeof(resultData.processName) != 'undefined') {
							processName = resultData.processName;
						}
						
					}else{
						//父子流程
						if (typeof(resultData.bpmStyleId) != 'undefined') {
							bpmStyleIds += resultData.bpmStyleId + ',';
						}
						
						if (typeof(resultData.activityAlias) != 'undefined') {
							relName += resultData.activityAlias + ',';
						}
					}
				}

				
				selfAlias=selfAlias.substring(0,selfAlias.length-1);
				selfActName=selfActName.substring(0,selfActName.length-1);
				relName=relName.substring(0,relName.length-1);
				bpmStyleIds=bpmStyleIds.substring(0,bpmStyleIds.length-1);
				
				var activityAlias = "";
				//没有引用流程节点
				if(bpmStyleIds == "" && selfAlias !=""){
					activityAlias = processName+"["+selfAlias+"]";
				}
				
				//没有本流程节点
				if(bpmStyleIds != "" && selfAlias == ""){
					activityAlias = relName;
				}
				
				if(bpmStyleIds != "" && selfAlias != ""){
					activityAlias = processName+"["+selfAlias+"],"+relName;
				}
				$("#activityName").val(selfActName);
				$("#activityAlias").val(activityAlias);
				$("#quoteId").val(bpmStyleIds);
				usd.close();
			}
		} ];
		usd.createButtonsInDialog(buttons);
		usd.show();
	}
	
	</script>
</head>

<body class="easyui-layout" fit="true">
	<div region="center" border="false" style="overflow: hidden;">
		<form id="form1" method="post">
        	<table class="tableForm" width="100%" border=0>
				<tr>
					<td >名称</td>
					<td colspan="3">
					<input id="quoteId"  name="quoteId" style="display:none;" />
					<input id="processDefinitionId"  name="processDefinitionId" style="display:none;" value="<%=pdId%>"/>
					<input id="title"  name="title" required="true" class="easyui-validatebox" style="width:470px"/><font color="red">*</font>
					</td>
					
				</tr>
				<tr>
					
					<td >代码</td>
					<td colspan="3"><input id="code"  name="code" required="true" class="easyui-validatebox" style="width:470px"/><font color="red">*</font></td>
				</tr>
				<tr>
					<td >类型</td>
					<td colspan="3">
						<select id="type" name="type" class="easyui-combobox"  style="width:470px">
						  <option value="0">自用类型</option>
						  <option value="1">引用类型</option>
						</select><font color="red">*</font>
					</td>
				
				</tr>
				<tr>

					<td >排序</td>
					<td colspan="3"><input id="orderBy"  name="orderBy" required="true" class="easyui-validatebox" style="width:470px"/><font color="red">*</font></td>
				</tr>
				<tr>
					<td >节点</td>
					<td colspan="3">
					<input id="activityName"  name="activityName" style="display:none;"/>
					<input id="activityAlias"  name="activityAlias" required="true" readonly="readonly" class="easyui-validatebox" style="width:470px"/><font color="red">*</font>
					<img  src="static/images/platform/bpm/client/images/button/tiaozhuan.png" title="选择流程节点" onclick="selectProcessActivity();" style="align:center;cursor:pointer;"/>

					</td>
				</tr>
				<tr>
					<td >岗位</td>
					<td colspan="3">
					<input id="position"  name="position" style="display:none;"/>
					<input id="positionName"  name="positionName" readonly="readonly" style="width:470px"/>
					<img  src="static/images/platform/bpm/client/images/button/renyuan.png" title="选择岗位" onclick="selectProcessPosition();" style="align:center;cursor:pointer;"/>
					</td>
				</tr>
				<tr>
					<td >样式</td>
					<td colspan="3"><input id="styleName"  name="styleName"  value="idea,user,day" style="width:470px"/></td>
				</tr>
				<tr>
					<td colspan="4">格式：user,dept,day,time,idea的任意组合，用,分割。<br/>比如：idea,user,day 显示格式为：意见，用户，日期</td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>

