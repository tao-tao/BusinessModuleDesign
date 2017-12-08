$(function(){
	//下节点的属性值
	$('#workhandDataGrid').datagrid({
		fitColumns: true,
		nowrap:false,
		idField:'userId',
		loadMsg:"数据加载中.....",
		rownumbers:true,
		columns:[[
		    {field:'isBeginUser',title:'',width:40,align:'center',formatter:formatAction},
		    {field:'beginUserId',title:'移交人Id',width:120,align:'left',hidden : true},
			{field:'beginUserName',title:'移交人',width:120,align:'left'},
			{field:'beginDeptId',title:'移交人部门Id',width:120,align:'left',hidden : true},
			{field:'beginDeptName',title:'移交人部门',width:120,align:'left'},
			{field:'isEndUser',title:'',width:40,align:'center',formatter:formatAction},
			{field:'endUserId',title:'接受人Id',width:120,align:'left',hidden : true},
			{field:'endUserName',title:'接受人',width:120,align:'left'},
			{field:'endDeptId',title:'接受人部门Id',width:120,align:'left',hidden : true},
			{field:'endDeptName',title:'接受人部门',width:120,align:'left'}
		]]
	});
	function formatAction(value,row,index){
		if(value){
			var e =  "";
			var d = "<div class='check_yes' onClick=\"changeDataGridCheckBoxValue(" + value + ","+index+");return false;\"></div>";
			return e+d;
		} else {
			var e =  "";
			var d = "<div class='check_no' onClick=\"changeDataGridCheckBoxValue(" + value + ","+index+");return false;\"></div>";
			return e+d;
		}
	}
//	$('#workhandDataGrid').datagrid({
//	    onLoadSuccess: function(){
//	            function bindRowsEvent(){
//	                var panel = $('#workhandDataGrid').datagrid('getPanel');
//	                var rows = panel.find('tr[datagrid-row-index]');
//	                rows.unbind('click').bind('click',function(e){
//	                    return false;
//	                });
//	                rows.find('div.datagrid-cell-check input[type=checkbox]').unbind().bind('click', function(e){
//	                    var index = $(this).parent().parent().parent().attr('datagrid-row-index');
//	                    if ($(this).attr('checked')){
//	                        $('#workhandDataGrid').datagrid('selectRow', index);
//	                    } else {
//	                        $('#workhandDataGrid').datagrid('unselectRow', index);
//	                    }
//	                    e.stopPropagation();
//	                });
//	            }
//	            setTimeout(function(){
//	                bindRowsEvent();
//	            }, 10);   
//	    }
//	});
	$("#workhandDataGrid").datagrid('loadData',workhandJson);
});
function changeDataGridCheckBoxValue(value,index){
	var datas = $('#workhandDataGrid').datagrid('getData');
	if(datas.rows.length > 0){
		if(datas.rows[index].isBeginUser){
			$('#workhandDataGrid').datagrid('updateRow',{
				index: index,
				row: {
					isBeginUser: false,
					isEndUser: true
				}
			});
		} else if(!datas.rows[index].isBeginUser){
			$('#workhandDataGrid').datagrid('updateRow',{
				index: index,
				row: {
					isBeginUser: true,
					isEndUser: false
				}
			});
		} else if(datas.rows[index].isEndUser){
			$('#workhandDataGrid').datagrid('updateRow',{
				index: index,
				row: {
					isBeginUser: true,
					isEndUser: false
				}
			});
		} else if(!datas.rows[index].isEndUser){
			$('#workhandDataGrid').datagrid('updateRow',{
				index: index,
				row: {
					isBeginUser: false,
					isEndUser: true
				}
			});
		}
	}
}

