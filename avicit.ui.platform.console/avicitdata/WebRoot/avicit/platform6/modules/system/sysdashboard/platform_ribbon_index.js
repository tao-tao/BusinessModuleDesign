var doradoFlag = false;
	var baseUrl = "<%=ViewUtil.getRequestPath(request)%>";
	var messageCount = "0";
	var controlMessageDialog = "false";
	var messageIntervalTime = "30000";
	if (typeof (messageIntervalTime) == 'undefined') {
		messageIntervalTime = 30000;
	}
	var fulltextSearchPath = "platform/search/search.html";
	var agileSearchPath = "platform/sysAgileSearch/agileSeracherAction/agileSearchTip";
	function loadMessageData() {
		if (controlMessageDialog != "" && controlMessageDialog == "true") {
			loadMessage(baseUrl, messageIntervalTime, doradoFlag);
		} else {

		}
	}

	function openLayoutPage() {
		var dialogId = "layoutDialog";
		var usd = new CommonDialog(
				dialogId,
				"450",
				"450",
				baseUrl
						+ 'avicit/platform6/modules/system/sysdashboard/indexPortletConfig.jsp?isgloable=false&dialogId='
						+ dialogId, "设置页面布局", false, true, false);
		usd.show();
	}
	function openPortletAdd() {
		var dialogId = "portletAddDialog";
		var usd = new CommonDialog(
				dialogId,
				"450",
				"450",
				baseUrl
						+ 'avicit/platform6/modules/system/sysdashboard/indexMpmPortletAdd.jsp?isgloable=false&dialogId='
						+ dialogId, "添加首页应用", false, true, false);
		usd.show();
	}

	function savePortlet() {
		var portlet = "";
		var iframeBody = $("#iframeBody")[0].contentWindow;
		var portletRow = iframeBody.$("#portalContent .ui-portlet");
		var layout = iframeBody.$("#layout").val();
		$.each(portletRow, function(k, v) {
			var indexs = iframeBody.getPortletInfo(v.id);
			$.each(indexs, function(k1, v1) {
				portlet = portlet + v.id + ";" + k1 + ";" + v1.x + ":" + v1.y
						+ "@";
			});
		});

		if (portlet != null && portlet != "") {
			portlet = portlet.substring(0, portlet.length - 1);
			$.ajax({
				url : baseUrl
						+ 'platform/IndexPortalController/saveIndexPortlet',
				async : false,
				type : "POST",
				data : 'isgloable=false&portlet=' + portlet + '&layout='
						+ layout,
				success : function() {
					//获取当前操作的数据行记录
					$.messager.alert("操作提示", "保存页面设置成功!!","info");
					hideDialog();
					window.location.reload();
					//refreshPortlet();	
				},
				error : function() {
					//alert('portlet配置信息保存失败!');
				}
			});
		}

	}
	
	
	function showDialogAddColumn(){
		var usd = new CommonDialog("showDialogMenu","800","465","avicit/platform6/modules/system/sysdashboard/platform_ribbon_mutil_select.jsp","配置快捷菜单",true,true,false,true);
		usd.show();
		/**
		 * 回调方法
		 */
		saveMenuBack = function(personalMenu) {
			$("#ribbonButtomMenu").html(personalMenu);
		};
		
		/**
		 * 关闭方法
		 */
		closeDailogMenu = function() {
			usd.close();
		};
		
	};
	
	
	//弹出系统版本页面
	function showVersionDialog() {
		$('#versionDialog').dialog({
		    title: '系统版本',
		    width: 400,
		    height: 200,
		    loadMsg:'数据加载中...',
		    closed: false,
		    cache: false,
		    modal: true
		});
		$('#versionDialog').dialog('refresh', 'avicit/cape/mpmversion/mpmVersionDialog.jsp');
	} 