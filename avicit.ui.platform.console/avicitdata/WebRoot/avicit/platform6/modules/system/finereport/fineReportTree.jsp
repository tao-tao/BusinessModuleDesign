<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<%@ page import="avicit.platform6.commons.utils.ViewUtil"%>
	<div id="toolbar" class="datagrid-toolbar">
	 	<table width="100%">
	 		<tr>
	 			<td width="100%"><input  type="text"  name="searchWord" id="searchWord"></input></td>
	 			<td align="right" width="100px;"><a class="easyui-linkbutton" iconCls="icon-reload" plain="true" onclick="refreshMenuSecurityCache();" style="width:120px;" href="javascript:void(0);">刷新授权缓存</a></td>
	 		</tr>
	 	</table>
	 </div>
	 <div style="overflow-x:hidden; overflow-y:auto; height:90%">
		<ul id="reportTree" class="easyui-tree" data-options="
				url:'platform/fineReportAuthorityController/getModuleTree.json',
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
	       		onClick: function(node){
					clickTreeRow(node);
				},
				onLoadSuccess: function(node, data){
					if (data.result){
						$('#reportTree').html(data.msg).css('color','red');
					}
				}
	       		
	       		">数据加载中...
	    </ul>
	</div>
