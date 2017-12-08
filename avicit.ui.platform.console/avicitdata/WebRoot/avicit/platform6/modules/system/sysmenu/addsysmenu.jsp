<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>添加菜单</title>
<base href="<%=ViewUtil.getRequestPath(request)%>">
<jsp:include page="/avicit/platform6/component/common/EasyUIJsInclude.jsp"></jsp:include>
<style type="text/css">
.table tr input{
	width: 95%;
	margin-left: 5px;
}
.table tr th{
	font-size: 12px;
	font-family: "Microsoft YaHei";
	color: #444;
	line-height:20px;
}
.table label{
	font-size: 12px;
	font-family: "Microsoft YaHei";
	color: #444;
	line-height:20px;
}
.fieldset{
	font-size: 12px;
	font-family: "Microsoft YaHei";
	color: #444;
	line-height:20px;
	width: 95%;
	display:none;
/* 	margine-bottom: 20px; */
}
.numberBox_extend{
	border:1px solid #0066FF;height:18px;
}
</style>
<!--[if IE 8]> 
	<style type="text/css">
		.form{padding-bottom:36px;}
	</style>
<![endif]-->
</head>
<body class="easyui-layout" fit="true">
	<div data-options="region:'center',split:true,border:false" style="padding-bottom:32px;overflow: auto;">
		<form id='form' class='form'> 
		<input type="hidden" name='id' id='id' value=''/>
		<input type="hidden" name='tlId' id='tlId' value=''/>
		<table class="table" width="100%" style="padding-top: 4px;">
			<tr>
				<th align="right" width="85px">菜单名称</th>
				<!-- <td><input title="菜单名称" class="inputbox" type="text" name="name" id="name" /></td> -->
				<td>
					<span style="padding:0px;margin: 0px;">
						<span style="padding:0px;margin: 0px;width: 5px;display: inline-block;">
							<span class="required-icon"></span>
						</span>
					  	<input title="菜单名称" class="inputbox" style="width: 535px;" type="text" name="name" id="name"/>
					</span>
				</td>
			</tr >
			<tr>
				<th align="right">菜单代码</th>
				<!-- <td><input title="菜单代码" class="inputbox" type="text" name="code" id="code" /></td> -->
				<td>
					<span style="padding:0px;margin: 0px;">
						<span style="padding:0px;margin: 0px;width: 5px;display: inline-block;">
							<span class="required-icon"></span>
						</span>
					  	<input title="菜单代码" class="inputbox" style="width: 535px;" type="text" name="code" id="code"/>
					</span>
				</td>
			</tr>
			<tr>
				<th align="right">图标引用地址</th>
				<td style="padding-left:5px;"><input title="图标引用地址" class="inputbox" data-options="width:546,editable:false,panelHeight:'auto'" type="text" name="image" id="image" /></td>
			</tr>
			<tr>
				<th align="right">菜单路径</th>
				<td><input title="菜单路径" class="inputbox" type="text" name="url" id="url" /></td>
			</tr>
			<tr>
				<th align="right">排序编号</th>
				<td><input title="排序编号" class="easyui-numberbox numberBox_extend" data-options="min:0,precision:0" type="text" name="orderBy" id="orderBy" /></td>
			</tr>
			<tr>
				<th align="right">菜单组</th>
				<!-- <td><input title="菜单组" class="inputbox" type="text" name="menuGroupName" id="menuGroupName" /></td> -->
				<td style="padding-left:5px;"><select name="menuGroup" class="easyui-combobox" data-options="width:546,editable:false,panelHeight:'auto'">
							<c:forEach items="${menuGroup}" var="menuGroup">
								<option value="${menuGroup.lookupCode}">${menuGroup.lookupName}</option>
							</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<th align="right">菜单类型</th>
				<td>
					<input style="width: 25px;" title="功能菜单" type="radio" name="type" id="type1" value='1' checked/><label for="type1">功能菜单</label>
					<input style="width: 25px;" title="门户小页" type="radio" name="type" id="type2" value='2'/><label for="type2">门户小页</label>
				</td>
			</tr>
			<tr>
				<th align="right">菜单状态</th>
				<td><input style="width: 25px;" title="启用" type="radio" name="status" id="status1" value='1' checked/><label for="status1">启用</label>
					<input style="width: 25px;" title="禁用" type="radio" name="status" id="status2" value='0'/><label for="status2">禁用</label>
				</td>
			</tr>
			<tr>
				<th align="right">是否Robbin</th>
				<td><input style="width: 25px;" title="是" type="radio" name="isRobbin" id="isRobbin1" value='1'/><label for="isRobbin1">是</label>
					<input style="width: 25px;" title="否" type="radio" name="isRobbin" id="isRobbin2" value='0' checked/><label for="isRobbin2">否</label>
				</td>
			</tr>
			<tr>
				<th align="right">菜单视图</th>
				<!-- <td><input title="配置文件代码" class="inputbox" type="text" name="profileOptionCode" id="profileOptionCode"/></td> -->
				<!-- <td><textarea title='菜单描述' class="inputbox scrollbar" style="height:50px;width:95%;margin-left: 5px;" id="comments" name="comments" ></textarea></td> -->
				<td><input title="菜单视图文件路径" class="inputbox" type="text" name="menuView" id="menuView" /></td>
			</tr>
			<tr>
				<th align="right">菜单描述</th>
				<td><textarea title='菜单描述' class="inputbox scrollbar" style="height:50px;width:95%;margin-left: 5px;" id="comments" name="comments" ></textarea></td>
			</tr>
		</table>
		<fieldset id='fieldset' class="fieldset">
			<legend>门户小页</legend>
			<table class="table" width="100%">
			<tr>
				<th align="right" width="80px">滚动条</th>
				<td>
					<input style="width: 25px;" title="有" type="radio" name="scroll" id="scroll1" value='1'/><label for="scroll1">有</label>
					<input style="width: 25px;" title="无" type="radio" name="scroll" id="scroll2" value='0' checked/><label for="scroll2">无</label>
				</td>
			</tr >
			<tr>
				<th align="right" width="80px">关闭按钮</th>
				<td>
					<input style="width: 25px;" title="有" type="radio" name="isclose" id="closeble1" value='1'/><label for="closeble1">有</label>
					<input style="width: 25px;" title="无" type="radio" name="isclose" id="closeble2" value='0' checked/><label for="closeble2">无</label>
				</td>
			</tr>
			<tr>
				<th align="right" width="80px">是否显示标题</th>
				<td>
					<input style="width: 25px;" title="显示" type="radio" name="isShowTitle" id="titleble1" value='1' checked/><label for="titleble1">显示</label>
					<input style="width: 25px;" title="隐藏" type="radio" name="isShowTitle" id="titleble2" value='0'/><label for="titleble2">隐藏</label>
				</td>
			</tr>
			<!-- <tr>
				<th align="right" width="80px">是否允许拖拽</th>
				<td>
					<input style="width: 25px;" title="允许" type="radio" name="isdrag" id="dragble1" value='1' /><label for="dragble1">允许</label>
					<input style="width: 25px;" title="不允许" type="radio" name="isdrag" id="dragble2" value='0' /><label for="dragble2">不允许</label>
				</td>
			</tr> -->
			<tr>
				<th align="right" width="80px">其它链接地址</th>
				<td><input title="其它链接地址 " class="inputbox" type="text" name="moreurl" id="moreurl" /></td>
			</tr>
			<tr>
				<th align="right" width="80px">高度</th>
				<td><input title="高度 " class="inputbox" type="text" name="height" id="height" /></td>
			</tr>
			<tr>
				<th align="right" width="80px">刷新频率</th>
				<td><input title="刷新频率" class="inputbox" type="text" name="refresh" id="refresh" /></td>
			</tr>
			<tr>
				<th align="right" width="80px">样式</th>
				<!-- <td><input title="配置文件代码" class="inputbox" type="text" name="profileOptionCode" id="profileOptionCode"/></td> -->
				<td><textarea title='样式' class="inputbox scrollbar" style="height:50px;width:95%;margin-left: 5px;" id="css" name="css" ></textarea></td>
			</tr>
		</table>
		</fieldset>
	</form>
		<div id="toolbar" class="datagrid-toolbar datagrid-toolbar-extend">
			<table class="tableForm" border="0" cellspacing="1" width='100%'>
				<tr>	
					<td width="60%" align="right"><a title="保存" id="saveButton"  class="easyui-linkbutton" plain="false" onclick="saveForm();" href="javascript:void(0);">保存</a>
					<a title="返回" id="returnButton"  class="easyui-linkbutton" plain="false" onclick="closeForm();" href="javascript:void(0);">返回</a>
					</td>
				</tr>
			</table>
		</div>
	</div>
	<script type="text/javascript">
		var path;
		function getOffset(evt) {
			var target = evt.target;
			if (target.offsetLeft == undefined) {
				target = target.parentNode;
			}
			var pageCoord = getPageCoord(target);
			var eventCoord = {
				x : window.pageXOffset + evt.clientX,
				y : window.pageYOffset + evt.clientY
			};
			var offset = {
				offsetX : eventCoord.x - pageCoord.x,
				offsetY : eventCoord.y - pageCoord.y
			};
			return offset;
		}
	
		function getPageCoord(element) {
			var coord = {
				x : 0,
				y : 0
			};
			while (element) {
				coord.x += element.offsetLeft;
				coord.y += element.offsetTop;
				element = element.offsetParent;
			}
			return coord;
		}
		function setIconIndex(event, target) {
			var offset = event;
			if (!offset.offsetX) {
				offset = getOffset(event);
			}
			var offsetX = floorNumber(offset.offsetX, 20);
			var offsetY = floorNumber(offset.offsetY, 20);
	
			var iconPath = "static/images/platform/sysmenu/icons.gif -" + offsetX + "px -"
					+ offsetY + "px";
			temp =iconPath;
		}
	
		function floorNumber(num, divisor) {
			return Math.floor(num / divisor) * divisor;
		}
	
		function setDivLocation(id, x, y) {
			var css = document.getElementById(id).style;
			css.left = x + "px";
			css.top = y + "px";
		}
		
		function hidePanel(){
			$('#image').combobox('setValue', temp);
		}
		function formatItem(){
			var s='<img src="static/images/platform/sysmenu/icons.gif" onClick="setIconIndex(event, \'selectIcon\')"/>';
			return s;
		}
		$(function(){
			$('#image').combobox({    
				valueField: 'id',
				textField: 'text',
				data: [{    
				    "id":'',    
				    "text":''   
				}],
				formatter: formatItem,
				onHidePanel:hidePanel
			}); 
			
			$('#type1').change(function(){
				$('#fieldset').css('display','none');
			});
			$('#type2').change(function(){
				$('#fieldset').css('display','block');
			});
		});
		
		function closeForm(){
			parent.sysMenu.closeDialog("#insert");
		}
		function saveForm(){
			var name =$('#name').val();
			if(name.length ===  0){
				alert("菜单名称不能为空！");
				return;
			}
			if(name.length >50){
				alert("菜单名称不能超过50个字！");
				return;
			}
			var code = $('#code').val();
			if(code.length ===  0){
				alert("菜单代码不能为空！");
				return;
			}
			if(code.length >50){
				alert("菜单代码不能超过50个字符！");
				return;
			}
			var order =$('#orderBy').val();
			if(order>65535){
				alert("排序号不能超过65535");
				$('#orderBy').numberbox('setValue', 5000);
				return;
			}
			parent.sysMenu.save(serializeObject($('#form')),'${url}','#insert','${id}');
		}
	</script>
</body>
</html>