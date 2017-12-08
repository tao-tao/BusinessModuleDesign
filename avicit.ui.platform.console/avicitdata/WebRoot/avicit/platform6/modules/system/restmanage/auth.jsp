<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>账号列表</title>
<base href="<%=ViewUtil.getRequestPath(request)%>">
<jsp:include page="/avicit/platform6/component/common/EasyUIJsInclude.jsp"></jsp:include>
<jsp:include page="/avicit/platform6/modules/system/commonpopup/commonSelectionHead.jsp"></jsp:include>
</head>
<body class="easyui-layout"  fit="true">

<div data-options="region:'west',split:true,title:''" style="width:250px;padding:0px;">
	<ul id="tt">正在加载数据...</ul>
</div>

<div data-options="region:'center',split:true,border:false" style="padding:0px;overflow:hidden;">
			<table id="rightdg"  class="easyui-datagrid"  url=""  fit="true"  toolbar="#righttoolbar" pagination="false" rownumbers="true" fitColumns="true" singleSelect="false" striped="true">
		    <thead>
				<tr>
					<th data-options="field:'id', halign:'center',checkbox:true" width="50">id</th>
					<th data-options="field:'restUrl',required:true,align:'center'"  width="100">URL地址</th>
					<th data-options="field:'urlDesc',required:true,align:'center'"  width="100">地址描述</th>
					<th data-options="field:'status',required:true,align:'center'"  width="50"  formatter="formatStatus">状态</th>
				</tr>
		    </thead>
	</table>
	
			<div id="righttoolbar">
			<table>
			<tr>
			<td><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="saveUrl()">保存</a></td>
				<td><label>所属单位:</label></td> 
				<td><select id="orgId" name="orgId" class="easyui-combobox"   data-options="valueField:'id', textField:'orgName', width:166,editable:false,panelHeight:'auto'"></select></td>
				<td><label>所属系统:</label></td> 
				<td><select id="systemId" name="systemId" class="easyui-combobox" data-options="valueField:'id', textField:'systemName', width:166,editable:false,panelHeight:'auto'"></select></td>
				<td><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="queryUrl()">查询</a></td>
				</tr>
				</table>
			</div>
			
	</div>
	<!-- right end-->
	
	<script type="text/javascript">
	var url;
	var org;
	var sys;
	
	$(function() { 
        $(document).ready(function() { 
            $.post('platform/restmanage/system/tree.json', {}, function(json) { 
                $("#tt").tree({ 
                    data : json, 
                    onClick : function(node) { 
                    	
                    	var parent = $('#tt').tree('getParent', node.target);
						if(parent!=null){
							org=parent.id;       
							sys=node.id;
						}else{
							org=node.id;       
							sys=null;
						}
						
                        $.post('platform/restmanage/system/tree.json', { 
                            "id" : node.id
                        }, function(json) { 
                        	var children = $('#tt').tree('getChildren', node.target);
                        	for (var i = 0; i < children.length; i++) {
                        		$('#tt').tree('remove', children[i].target);
                            }
                            $('#tt').tree('append', { 
                                parent : node.target, 
                                data : json
                            }); 
                        }, "json"); 
                        
                        if(sys!=null){
                        	  var systemId=$('#systemId').combobox('getValue');
              				url = 'platform/restmanage/resources/listAll.html';
              				$.post(url, {
              					systemId:systemId,
              					userId:sys
              				}, function(result) {
              					$('#rightdg').datagrid('loadData',result);
              				}, 'json');
                        	
                        }
                        
                    } 
                }); 
            }, "json"); 
        }); 
    }); 
	
	
	function formatStatus(val,row){    
	    if (val =='1'){    
	        return '有效';    
	    } else {    
	        return '无效';    
	    }    
	} 
	
    $(function(){  
    	$.post('platform/restmanage/org/listAll.json?&random='+Math.random(), {
		}, function(result) {
			$("#orgId").combobox("loadData", result).combobox('clear');
		}, 'json');
    	
        $('#orgId').combobox({  
            onSelect:function(record){  
                $.post('platform/restmanage/system/listByorg.html?orgId='+record.id+'&random='+Math.random(), {
    			}, function(result) {
    				$("#systemId").combobox("loadData", result).combobox('clear');
    			}, 'json');
            }  
        });  
        
        
        $('#rightdg').datagrid({
			onLoadSuccess:function(data){                   
	        	if(data){
	        		$.each(data.rows, function(index, item){
				        	if(item.checked){
				        		$('#rightdg').datagrid('checkRow', index);
				        	}
        			});
        		}
        	}  
		});
        
        
	});  
		
		
		function queryUrl() {
			var row =  $('#tt').tree('getSelected');
			if(row){
				var parent = $('#tt').tree('getParent', row.target);
				if(parent!=null){
					var systemId=$('#systemId').combobox('getValue');
					url = 'platform/restmanage/resources/listAll.html';
					$.post(url, {
						systemId:systemId,
						userId:row.id
					}, function(result) {
						$('#rightdg').datagrid('loadData',result);
					}, 'json');
				}else{
					$.messager.show({title:'info',msg:'请选择系统'});
				}
			}
		}
		
		function saveUrl() {
			url='platform/restmanage/auth/add.html';
			var checkedItems = $('#rightdg').datagrid('getChecked');
			var row =  $('#tt').tree('getSelected');
			
			var res = '';
			$.each(checkedItems, function(index, item){
				res=res+item.id+",";
			});         
			
			if(row){
				var parent = $('#tt').tree('getParent', row.target);
				if(parent!=null&&res!=''){
					
					$.post(url, {
						systemId:row.id,
						res:res
					}, function(result) {
						$.messager.show({title:'info',msg:'保存成功'});
					}, 'json');
				}
			}
		}
		
		
	</script>
	
	<style type="text/css">
#fm {
	margin: 0;
	padding: 10px 30px;
}

.ftitle {
	font-size: 14px;
	font-weight: bold;
	padding: 5px 0;
	margin-bottom: 10px;
	border-bottom: 1px solid #ccc;
}

.fitem {
	margin-bottom: 5px;
}

.fitem label {
	display: inline-block;
	width: 80px;
}

.fitem input {
	width: 160px;
}
</style>
	
</body>
</html>