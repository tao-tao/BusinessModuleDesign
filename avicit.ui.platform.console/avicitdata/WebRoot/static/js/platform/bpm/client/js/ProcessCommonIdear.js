$(function(){
	//下节点的属性值
	$('#commonIdearDataGrid').datagrid({
		fitColumns: true,
		nowrap:false,
		idField:'id',
		loadMsg:"数据加载中.....",
		rownumbers:true,
		frozenColumns:[[
            {field:'ck',checkbox:true}
		]],
		columns:[[
		    {field:'id',title:'意见ID',width:40,align:'left',hidden:true},
		    {field:'value',title:'意见',width:400,align:'left'},
		    {field:'orgId',title:'orgId',width:40,align:'left',hidden:true},
		    {field:'userId',title:'userId',width:40,align:'left',hidden:true},
		    {field:'key',title:'key',width:40,align:'left',hidden:true},
		    {field:'isMulti',title:'isMulti',width:40,align:'left',hidden:true},
		    {field:'isDefault',title:'isDefault',width:40,align:'left',hidden:true}
		]]
	});
	$('#commonIdearDataGrid').datagrid('loadData',commonIdearDataJson);
});
function getCommonIdearSelected(){
	var values = [];
	var rows = $('#commonIdearDataGrid').datagrid('getSelections');
	for(var i = 0 ; i < rows.length ; i++){
		values.push(rows[i].value);
	}
	if(values.length == 0){
		return '';
	}
	return values.join('\n');
}


