<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>

<div id="toolbar" class="datagrid-toolbar">
	 	<table width="100%">
	 		<tr>
	 			<td width="100%"><input  type="text"  name="searchWord" id="searchWord"></input></td>
	 		</tr>
	 	</table>
	 </div>
	 <div style="overflow-x:hidden; overflow-y:auto; height:90%">
	<ul id="memuTree" class="easyui-tree"
			data-options="
			url:'platform/sysPermissionTreeController/listMemuTreeById.json',
			loadFilter: function(data){
	            if (data.data){	
	                return data.data;
	            } else {
	                return data;
	            }
       		},
       		lines:true,
       		dataType:'json',
       		animate:true,
       		onBeforeExpand:myOnBeforeExpand,
       		onClick:clickTreeRow">数据加载中...
	</ul>
	</div>