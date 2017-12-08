<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<%@ page import="java.util.*"%>
<%@ page import="avicit.platform6.api.syslookup.SysLookupAPI"%>
<%@ page import="avicit.platform6.api.syslookup.impl.SysLookupAPImpl"%>
<%@ page import="avicit.platform6.api.syslookup.dto.SysLookupSimpleVo"%>
<%@ page import="avicit.platform6.core.spring.SpringFactory"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="sec" uri="/WEB-INF/tags/shiro.tld"%>
<%
	SysLookupAPI upLoader = SpringFactory.getBean(SysLookupAPI.class);
	Collection<SysLookupSimpleVo> lookUpdeptType = upLoader.getLookUpListByType("PLATFORM_SYS_DEPT_TYPE");//修改平台，转换为通用代码，部门类型
	Collection<SysLookupSimpleVo> lookUpYesNoFlag = upLoader.getLookUpListByType("PLATFORM_YES_NO_FLAG");//修改平台，转换为通用代码，是否PMO
	Collection<SysLookupSimpleVo> lookUpValidFlag = upLoader.getLookUpListByType("PLATFORM_VALID_FLAG");//修改平台，转换为通用代码，是否有效
	
	
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>组织管理</title>
<base href="<%=ViewUtil.getRequestPath(request)%>">
<jsp:include page="/avicit/platform6/component/common/EasyUIJsInclude.jsp"></jsp:include>
<jsp:include page="/avicit/platform6/modules/system/commonpopup/commonSelectionHead.jsp"></jsp:include>
<script src="static/js/platform/component/common/CommonDialog.js" type="text/javascript"></script>
<link href="avicit/platform6/modules/system/sysdept/css/icon.css" type="text/css" rel="stylesheet">
<script type="text/javascript">
var path="<%=ViewUtil.getRequestPath(request)%>";
var currTreeNode;

$.extend($.fn.validatebox.defaults.rules,{
    //国内邮编验证
    zipcode: {
        validator: function (value) {
            var reg = /^[1-9]\d{5}$/;
            return reg.test(value);
        },
        message: "请输入正确的邮编格式"
    },
  //电话验证
    tel: {
        validator: function (value) {
        	var mobilereg = /^((13[0-9])|(15[^4,\D])|(18[0,5-9])|145|147)\d{8}$/;
            var phonereg = /^((0[0-9]{2,3})-)([0-9]{7,8})(-([0-9]{3,}))?$/;
            return mobilereg.test(value) || phonereg.test(value);
        },
        message: "请输入有效的电话号码"
    },
    fax :{
    	validator: function (value) {
        	var reg = /^[+]{0,1}(\d){1,3}[ ]?([-]?((\d)|[ ]){1,12})+$/;
            return reg.test(value);
        },
        message: "请输入有效的传真号码"
    }
});

$(document).ready(function(){ 
	var orgId='<%=request.getParameter("id")%>';
	loadOrgInfo(orgId);
	setFormReadOnly(false);
	//addOrg();
});

function setFormReadOnly(isReadOnly){
	
	if(!isReadOnly){
				
		$("form input").attr("disabled",false);
		$('form textarea').attr("disabled",false);
		//$("#btSaveDept").linkbutton('enable');
	}else{
		$("form input").attr("disabled",true);
		$('form textarea').attr("disabled",true);
	}
	
	$("form input[name='RESPONSIBLE_DEPT_NAME']").attr("disabled",true);
	
	//设置组织CODE为只读
	$("#ORG_CODE").attr("readonly", true);
}

function loadOrgInfo(orgId){
	$.ajax({
        cache: true,
        type: "POST",
        url:'platform/sysorg/sysOrgController/getSysOrgById.json?id='+orgId,
        dataType:"json",
        data:{id:orgId},
        error: function(request) {
        	$.messager.alert('提示','操作失败，服务请求状态：'+request.status+' '+request.statusText+' 请检查服务是否可用！','warning');
        },
        success: function(data) {
			if(data.result==0&&data.sysOrg){
				$('#orgEditForm').form('load',data.sysOrg);
				
			}else{
				$.messager.alert('提示',data.msg,'warning');
			}
        }
    }); 
}

function clearOrgForm(){
	$('#orgEditForm').form('load',{
		ID:'',
		
		PARENT_ORG_ID:'',
		ORG_CODE:'',
		ORG_NAME:'',
				
		POST:'',
		TEL:'',
		FAX:'',
		EMAIL:'',
		WORK_CALENDAR_ID:'',
		ORDER_BY:'',
		
		ORG_PLACE:'',
		RESPONSIBLE_DEPT_ID:'',
		RESPONSIBLE_DEPT_NAME:'',
		
		VALID_FLAG:'',
		ORG_DESC:''
	});
}

//选择部门
function mySelectDept(){
	
	var orgId=$('#ID').val();
	//alert(orgId);
	
	var deptSelector = new CommonDialog("deptSelectDialog","350", "300", path+"/avicit/platform6/modules/system/sysdept/selectDept2.jsp?id="+orgId+"&type=org","选择部门", null,null,false);
	deptSelector.show();
}

function selectDeptDialogCallBack(deptId, deptName)
{
	$('#RESPONSIBLE_DEPT_NAME').val(deptName);
	$('#RESPONSIBLE_DEPT_ID').val(deptId);
}


</script>

<style type="text/css">
.requiedbox {
	display: inline-block;
	white-space: nowrap;
	margin: 0;
	padding: 0;
	/* border-width: 1px;
	border-style: solid; */
	overflow: hidden;
}
.selectbox {
	display: inline-block; 
	white-space: nowrap;
	margin: 0;
	padding: 0;
	/* border-width: 1px;
	border-style: solid; */
	overflow: hidden;
	
}
.selectbox .selectbox-text {
	font-size: 12px;
	border: 0;
	margin: 0;
	padding: 0;
	line-height: 20px;
	height: 20px;
	 *margin-top: -1px;
	 *height: 18px;
	 *line-height: 18px;
	_height: 18px;
	_line-height: 18px;
	vertical-align: baseline;
}
.selectbox-button {
	width: 18px;
	height: 20px;
	overflow: hidden;
	display: inline-block;
	vertical-align: top;
	cursor: pointer;
	/* opacity: 0.6;
	filter: alpha(opacity=60); */
}
.selectbox-button-hover {
	/* opacity: 1.0;
	filter: alpha(opacity=100); */
}
.selectbox {
	border-color: #95B8E7;
	background-color: #fff;
}
.selectbox-button {
	background: url('avicit/platform6/component/css/commonselection/icons_ext/selectorCommon.gif') no-repeat center center;
}
.required-icon {
	maring:0px;
	padding:0px;
	width: 10px;
	height: 23px;
	overflow: hidden;
	display: inline-block;
	vertical-align: top;
	/* opacity: 0.6; */
/* 	filter: alpha(opacity=60); */
	background: url('static/css/platform/themes/default/public/images/required.gif') no-repeat center center;
}
.datagrid-toolbar-extend {
	height: auto;
	padding: 1px 0;
	border-width: 0 0 1px 0;
	border-style: solid;
	position: absolute;
	bottom: 0px;
	left: 0px;
	width: 100%;
}
.numberBox_extend{
	border:1px solid #0066FF;height:18px;
}
.inputbox{
	background-color: #fff;
	border: 1px solid #95b8e7;
	color: #000;
	height: 18px;
}
</style>

</head>
<body>

<div region="center" border="false" style="overflow: hidden;">
  
			<form id="orgEditForm" name="orgEditForm" method="post">
				
				<input type="hidden"  name="ID" id="ID"></input>
				<input type="hidden"  name="PARENT_ORG_ID" id="PARENT_ORG_ID"></input>
				<input type="hidden"  name="RESPONSIBLE_DEPT_ID" id="RESPONSIBLE_DEPT_ID" >
				
				<div class="formExtendBase">
					<sec:accesscontrollist hasPermission="3" domainObject="system_sysorg_orgAddForm_ORG_CODE">
						<div class="formUnit column2">
							<label class="labelbg">组织代码：</label>
							<div class="inputContainer">
								<input class="easyui-validatebox"  data-options="required:true,validType:length[0,100]" type="text" name="ORG_CODE" id="ORG_CODE" ></input>
							</div>
						</div>
					</sec:accesscontrollist>
					
					<sec:accesscontrollist hasPermission="3" domainObject="system_sysorg_orgAddForm_ORG_NAME">
						<div class="formUnit column2">
							<label class="labelbg">组织名称：</label>
							<div class="inputContainer">
								<input class="easyui-validatebox" data-options="required:true,validType:length[0,100]" type="text" name="ORG_NAME" id="ORG_NAME" ></input>
							</div>
						</div>
					</sec:accesscontrollist>
					
										
										
					<sec:accesscontrollist hasPermission="3" domainObject="system_sysorg_orgAddForm_ORDER_BY">
						<div class="formUnit column2">
							<label>排序：</label>
							<div class="inputContainer">
								<input class="easyui-numberbox" data-options="required:true, max:9999999999" type="text" name="ORDER_BY" id="ORDER_BY" ></input>
							</div>
						</div>
					</sec:accesscontrollist>
					
					<sec:accesscontrollist hasPermission="3" domainObject="system_sysorg_orgAddForm_POST">
						<div class="formUnit column2">
							<label>邮编：</label>
							<div class="inputContainer">
								<input class="easyui-validatebox" data-options="validType:'zipcode'" name="POST" id="POST" ></input>
							</div>
						</div>
					</sec:accesscontrollist>
					
					<sec:accesscontrollist hasPermission="3" domainObject="system_sysorg_orgAddForm_TEL">
						<div class="formUnit column2">
							<label >电话：</label>
							<div class="inputContainer">
								<input class="easyui-validatebox" data-options="validType:'tel'" name="TEL" id="TEL" ></input>
							</div>
						</div>
					</sec:accesscontrollist>
					
					<sec:accesscontrollist hasPermission="3" domainObject="system_sysorg_orgAddForm_FAX">
						<div class="formUnit column2">
							<label >传真：</label>
							<div class="inputContainer">
								<input class="easyui-validatebox" data-options="validType:'fax'" name="FAX" id="FAX" ></input>
							</div>
						</div>
					</sec:accesscontrollist>
					
					<sec:accesscontrollist hasPermission="3" domainObject="system_sysorg_orgAddForm_EMAIL">
						<div class="formUnit column2">
							<label >邮箱：</label>
							<div class="inputContainer">
								<input class="easyui-validatebox" data-options="validType:'email'"  name="EMAIL" id="EMAIL" ></input>
							</div>
						</div>
					</sec:accesscontrollist>
					
					<sec:accesscontrollist hasPermission="3" domainObject="system_sysorg_orgAddForm_RESPONSIBLE_DEPT_NAME">
						<div class="formUnit column2" >
							<label >接口部门：</label>
							<div class="inputContainer">
								<!-- <input class="easyui-validatebox" disabled="disabled" type="text" name="RESPONSIBLE_DEPT_NAME" id="RESPONSIBLE_DEPT_NAME" ></input> -->
																
								
									
									<span class="selectbox" style="width: 100%" >
										<input type="text" id="RESPONSIBLE_DEPT_NAME" class="easyui-validatebox"  name="RESPONSIBLE_DEPT_NAME" readonly="readonly"  />
										<span>
											<span class="selectbox-button" onclick="mySelectDept();" style=" height: 20px;"></span>
										</span>
									</span>
								
								
							</div>
						</div>
					</sec:accesscontrollist>
					
					<sec:accesscontrollist hasPermission="3" domainObject="system_sysorg_orgAddForm_WORK_CALENDAR_ID">
						<div class="formUnit column2">
							<label>工作日历：</label>
							<div class="inputContainer">
								<input class="easyui-datebox" name="WORK_CALENDAR_ID" id="WORK_CALENDAR_ID" ></input>
							</div>
						</div>
					</sec:accesscontrollist>
					
					<sec:accesscontrollist hasPermission="3" domainObject="system_sysorg_orgAddForm_VALID_FLAG">
						<div class="formUnit column2">
							<label>是否有效：</label>
							<div class="inputContainer">
								<c:forEach items="<%=lookUpValidFlag%>" var="data">
									<input type="radio" name="VALID_FLAG" id="VALID_FLAG" value="${data.lookupCode}" style="width: 20px"/>${data.lookupName}
								</c:forEach>
							</div>
						</div>
					</sec:accesscontrollist>
					
					
					
					<sec:accesscontrollist hasPermission="3" domainObject="system_sysorg_orgAddForm_ORG_PLACE">
						<div class="formUnit column1">
							<label>办公地点：</label>
							<div class="inputContainer">
								<input class="easyui-validatebox" data-options="validType:length[0,100]"  name="ORG_PLACE" id="ORG_PLACE" ></input>
							</div>
						</div>
					</sec:accesscontrollist>
					
					
					
					<sec:accesscontrollist hasPermission="3" domainObject="system_sysorg_orgAddForm_ORG_DESC">
						<div class="formUnit column1" style="height:80px">
							<label>描述 ：</label>
							<div class="inputContainer">
								<textarea  style="height:60px;width: 90%" class="easyui-validatebox" data-options="validType:length[0,240]"  name="ORG_DESC" id="ORG_DESC"></textarea>
							</div>
						</div>
					</sec:accesscontrollist>
					
					
				</div>
			</form> 
	 
</div>
</body>
</html>