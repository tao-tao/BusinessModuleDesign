/**
 * @author wangyi
 */

var messageView = $.extend({}, $.fn.datagrid.defaults.view, {    
	 /**  
     * 生成某一行数据  
     * @param  {DOM object} target   datagrid宿主table对应的DOM对象  
     * @param  {array} fields   datagrid的字段列表  
     * @param  {boolean} frozen   是否为冻结列  
     * @param  {number} rowIndex 行索引(从0开始)  
     * @param  {json object} rowData  某一行的数据  
     * @return {string}          单元格的拼接字符串  
     */  
    renderRow: function(target, fields, frozen, rowIndex, rowData) {   
        var opts = $.data(target, "datagrid").options;   
        //用于拼接字符串的数组   
        var cc = [];   
        if(frozen && opts.rownumbers) {   
            //rowIndex从0开始，而行号显示的时候是从1开始，所以这里要加1.   
            var rowNumber = rowIndex + 1;   
            //如果分页的话，根据页码和每页记录数重新设置行号   
            if(opts.pagination) {   
                rowNumber += (opts.pageNumber - 1) * opts.pageSize;   
            }   
            /**  
             * 先拼接行号列  
             * 注意DOM特征，用zenCoding可表达为"td.datagrid-td-rownumber>div.datagrid-cell-rownumber"  
             */  
            cc.push("<td class=\"datagrid-td-rownumber\"><div class=\"datagrid-cell-rownumber\">" + rowNumber + "</div></td>");   
        }   
        
       
        
        for(var i = 0; i < fields.length; i++) {   
            var field = fields[i];   
            var col = $(target).datagrid("getColumnOption", field);   
            if(col) {   
                var value = rowData[field];   
                //获取用户定义的单元格样式，入参包括：单元格值，当前行数据，当前行索引(从0开始)   
                var style = col.styler ? (col.styler(value, rowData, rowIndex) || "") : "";   
                //如果是隐藏列直接设置display为none，否则设置为用户想要的样式   
                var styler = col.hidden ? "style=\"display:none;" + style + "\"" : (style ? "style=\"" + style + "\"" : "");   
                cc.push("<td field=\"" + field + "\" " + styler + ">");   
                //如果当前列是datagrid组件保留的ck列时，则忽略掉用户定义的样式，即styler属性对datagrid自带的ck列是不起作用的。   
                if(col.checkbox) {   
                    var styler = "";   
                } else {   
                    var styler = "";   
                    //设置文字对齐属性   
                    if(col.align) {   
                        styler += "text-align:" + col.align + ";";   
                    }   
                    //设置文字超出td宽时是否自动换行(设置为自动换行的话会撑高单元格)   
                    if(!opts.nowrap) {   
                        styler += "white-space:normal;height:auto;";   
                    } else {   
                        /**  
                         * 并不是nowrap属性为true单元格就肯定不会被撑高，这还得看autoRowHeight属性的脸色  
                         * 当autoRowHeight属性为true的时候单元格的高度是根据单元格内容而定的，这种情况主要是用于表格里展示图片等媒体。  
                         */  
                        if(opts.autoRowHeight) {   
                            styler += "height:auto;";   
                        }   
                    }   
                }   
                //这个地方要特别注意，前面所拼接的styler属性并不是作用于td标签上，而是作用于td下的div标签上。   
                cc.push("<div style=\"" + styler + "\" ");   
                //如果是ck列，增加"datagrid-cell-check"样式类   
                if(col.checkbox) {   
                    cc.push("class=\"datagrid-cell-check ");   
                }   
                //如果是普通列，增加"datagrid-cell-check"样式类   
                else {   
                    cc.push("class=\"datagrid-cell " + col.cellClass);   
                }   
                cc.push("\">");   
                /**  
                 * ck列光设置class是不够的，当突然还得append一个input进去才是真正的checkbox。此处未设置input的id，只设置了name属性。  
                 * 我们注意到formatter属性对datagird自带的ck列同样不起作用。  
                 */  
                if(col.checkbox) {   
                    cc.push("<input type=\"checkbox\" name=\"" + field + "\" value=\"" + (value != undefined ? value : "") + "\"/>");   
                }   
                //普通列   
                else {   
                    /**  
                     * 如果单元格有formatter，则将formatter后生成的DOM放到td>div里面  
                     * 换句话说，td>div就是如来佛祖的五指山，而formatter只是孙猴子而已，猴子再怎么变化翻跟头，始终在佛祖手里。  
                     */  
                    if(col.formatter) {   
                        cc.push(col.formatter(value, rowData, rowIndex));   
                    }   
                    //操，这是最简单的简况了，将值直接放到td>div里面。   
                    else {
                    	
                    	if(field=='urlAddress' && value)
                    	{
                    		if(value.indexOf('javascript')==0)
                    			value="<a onclick='"+value+"' href='javascript: void(0);'>"+value+"</a>";
                    		else
                    			value="<a onclick=\"javascript: parent.addTab('"+rowData.title+"', '"+value+"');\" href='javascript: void(0);'>"+value+"</a>";
                    		
                    	}
                    	                    	
                        cc.push(value);   
                    }   
                }   
                cc.push("</div>");   
                cc.push("</td>");   
            }   
        }   
        
        
        //返回单元格字符串，注意这个函数内部并未把字符串放到文档流中。   
        
        //alert(cc.join(""));
        
        return cc.join("");   
    }
});   

var userLimitCombo=null;
var readCombo=null;
var readComboEx=null;


/**
 * 初始化combo数据
 */
$(function(){
	
	$.ajax({
		url: 'platform/sysUserRelationController/getComboData.json',
		data : {'lookupType' : 'PLATFORM_MESSAGE_READFLAG'},
		type :'POST',
		dataType :'json',
		success : function(r){
			if(r.lookup){
				readCombo=	r.lookup;
				readComboEx= [{lookupCode:'', lookupName:'所有'}].concat( readCombo );
				
				
				
			}
		}
	});
	
	$('#messageSearchDialog').dialog('close');
	$('#messageContentDialog').dialog("close");
	$('#sendMessageDialog').dialog('close');
	
	$('#recvUserName').attr('disabled', true);
   	var commonSelector = new CommonSelector("user","userSelectCommonDialog", "recvUser" ,"recvUserName",null,null);
   	commonSelector.init(null,null,'n'); //选择人员  回填部门 */
	
});

function dgMessageOnClickCell(rowIndex, field, value)
{
	if(field=='content')
	{
		$('#messageContentDialog').dialog("open");
		$('#messageContent').val(value);
	}
}


function formatDate(value)
{
	var newDate=new Date(value);
	return newDate.Format("yyyy-MM-dd hh:mm:ss");   
}

function formatRead(value)
{
	if(value ==null ||value == ''){
		return '';
	}
	for(var i =0 ,length = readCombo.length; i<length;i++){
		if(readCombo[i].lookupCode == value){
			return readCombo[i].lookupName;
		}
	}
}




function IsURL (str_url) { 
var strRegex = '^((https|http|ftp|rtsp|mms)?://)' 
+ '?(([0-9a-z_!~*\'().&=+$%-]+: )?[0-9a-z_!~*\'().&=+$%-]+@)?' //ftp的user@ 
+ '(([0-9]{1,3}.){3}[0-9]{1,3}' // IP形式的URL- 199.194.52.184 
+ '|' // 允许IP和DOMAIN（域名） 
+ '([0-9a-z_!~*\'()-]+.)*' // 域名- www. 
+ '([0-9a-z][0-9a-z-]{0,61})?[0-9a-z].' // 二级域名 
+ '[a-z]{2,6})' // first level domain- .com or .museum 
+ '(:[0-9]{1,4})?' // 端口- :80 
+ '((/?)|' // a slash isn't required if there is no file name 
+ '(/[0-9a-z_!~*\'().;?:@&=+$,%#-]+)+/?)$'; 
var re=new RegExp(strRegex); 
//re.test() 
if (re.test(str_url)) { 
return (true); 
} else { 
return (false); 
} 
} 


function saveData()
{
	var myDatagrid=$('#dgMessage');
	
	if( $('#title').val() == "" ||  $('#content').val() == "" || 
			$('#recvUser').val() == "" || $('#recvUserName').val() == "" )
	{
		$.messager.alert('提示','所有信息都需要填写，请填写完整','warning');
		return;
	}

	
	if($('#urlAddress').val() != "" && !IsURL($('#urlAddress').val())){
		$.messager.alert('提示','转向地址不是url地址','warning');
		return;
	}
	
	var title= $('#title').val();
	var content= $('#content').val();
	var urlAddress= $('#urlAddress').val();
	var recvUser= $('#recvUser').val();
	var recvUserName=$('#recvUserName').val();
	
	var isReaded = $('#isReaded').combobox('getValue');
	
	
	 $.ajax({
		 url:'platform/sysmessage/sysMessageController/sengMessage',
		 data : {title: title, content: content, urlAddress: urlAddress, recvUser: recvUser, recvUserName:recvUserName, isReaded: isReaded},
		 type : 'post',
		 dataType : 'json',
		 success : function(r){
			if(r.result==0){
				 
				 myDatagrid.datagrid('reload');
				 myDatagrid.datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
				 $.messager.show({
					 title : '提示',
					 msg : '发送成功'
				 });
				 
				 hideSendMessageDialog();
				 
			 }else{
				 $.messager.alert('提示','发送失败','warning');
			 } 
		 }
	 });
	  
}

function makeRead(read)
{
	var myDatagrid=$('#dgMessage');
	
	var rows = myDatagrid.datagrid('getChecked');
	var ids = [];
	
	
	if (rows.length > 0) {
		$.messager.confirm('请确认', '您确定要标记当前所选的数据？', function(b) {
			if (b) {
				for(var i=0;i<rows.length;i++)
					ids.push(rows[i].id);
								
				$.ajax({
					url : 'platform/sysmessage/sysMessageController/markSysMessages',
					data : {
						ids : ids.join(","),
						mark: read
					},
					type : 'post',
					dataType : 'json',
					success : function(r) {
						
						if(r.result==0){
							myDatagrid.datagrid('reload');//刷新当前页
							myDatagrid.datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
							editIndex = -1;
							$.messager.show({
								title : '提示',
								msg : '标记成功！'
							});
						}
						else
						{
							$.messager.alert('提示','标记失败','warning');
						}
					}
				}); 
			}
		});
	} else {
		$.messager.alert('提示', '请选择要标记的记录！', 'warning');
	}
}


function deleteData()
{
	var myDatagrid=$('#dgMessage');
	
	var rows = myDatagrid.datagrid('getChecked');
	var ids=[];
	
	
	if (rows.length > 0) {
		$.messager.confirm('请确认', '您确定要删除当前所选的数据？', function(b) {
			if (b) {
				
				for(var i=0;i<rows.length;i++)
					ids.push(rows[i].id);
				
				$.ajax({
					url : 'platform/sysmessage/sysMessageController/deleteSysMessages',
					data : {
						ids : ids.join(",")
					},
					type : 'post',
					dataType : 'json',
					success : function(r) {
						
						if(r.result==0){
							myDatagrid.datagrid('reload');//刷新当前页
							myDatagrid.datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
							editIndex = -1;
							$.messager.show({
								title : '提示',
								msg : '删除成功！'
							});
						}
						else
						{
							$.messager.alert('提示','删除失败','warning');
						}
					}
				}); 
			}
		});
	} else {
		$.messager.alert('提示', '请选择要删除的记录！', 'warning');
	}
}

function showSearchDialog()
{
	$('#messageSearchDialog').dialog("open");
	
	$('#filter_EQ_m_is_readed').combobox('loadData', readComboEx);
	$('#filter_EQ_m_is_readed').combobox('setValue','');
	
}


function hideSearchDialog()
{
	$('#messageSearchDialog').dialog('close');
}

function searchData()
{
	
		
	var title= $('#filter_LIKE_m_title').val();
	var isReaded= $('#filter_EQ_m_is_readed').combobox('getValue');
	
	//alert(searchName+","+validFlag);
	
	loadSearchInfo(title, isReaded);
}

function clearSearchData()
{
	$('#filter_LIKE_m_title').val('');
	
	$('#filter_EQ_m_is_readed').combobox('setValue', '');
	
}

function loadSearchInfo(title, isReaded)
{
	
	$("#dgMessage").datagrid("options").url ="platform/sysmessage/sysMessageController/getSysMessageListByPage.json";
	$('#dgMessage').datagrid("reload", {filter_LIKE_m_title: title, filter_EQ_m_is_readed: isReaded, filter_EQ_m_recv_user: 'recv'});
	$("#dgMessage").datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');	
	
}

function showSendMessageDialog()
{
	$('#sendMessageDialog').dialog('open');
	
	$('#isReaded').combobox('loadData', readCombo );
	$('#isReaded').combobox('setValue', '0');
}

function hideSendMessageDialog()
{
	$('#sendMessageDialog').dialog('close');
}

function clearSendMessageData()
{
	$('#title').val('');
	$('#content').val('');
	$('#urlAddress').val('');
	$('#recvUser').val('');
	$('#recvUserName').val('');
	
	$('#isReaded').combobox('setValue', '0');
	
}






