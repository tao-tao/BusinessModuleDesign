var TANGER_OCX_bDocOpen = false;
var TANGER_OCX_filename;
var TANGER_OCX_actionURL; //For auto generate form fiields
var TANGER_OCX_OBJ; //The Control
var TANGER_OCX_Username="Anonymous";
var SessionAuthKey = "bGFudG9wT0E6QXZpYzYwOG9h"	//如果是与AD集成，请输入basic验证的字符串

//以下为V1.7新增函数示例

//从本地增加图片到文档指定位置
function AddPictureFromLocal()
{
	if(TANGER_OCX_bDocOpen)
	{	
    TANGER_OCX_OBJ.AddPicFromLocal(
	"", //路径
	true,//是否提示选择文件
	true,//是否浮动图片
	100,//如果是浮动图片，相对于左边的Left 单位磅
	100); //如果是浮动图片，相对于当前段落Top
	};	
}

//从URL增加图片到文档指定位置
function AddPictureFromURL(URL)
{
	if(TANGER_OCX_bDocOpen)
	{
    TANGER_OCX_OBJ.AddPicFromURL(
	URL,//URL 注意；URL必须返回Word支持的图片类型。
	true,//是否浮动图片
	150,//如果是浮动图片，相对于左边的Left 单位磅
	150);//如果是浮动图片，相对于当前段落Top
	};
}

//从本地增加印章文档指定位置
function AddSignFromLocal(boolvalue)
{        
try{
   if(TANGER_OCX_bDocOpen)
   {
      TANGER_OCX_OBJ.AddSignFromLocal(
	"匿名用户",//当前登录用户
	"",//缺省文件
	true,//提示选择
	0,//left
	0,
	1,
	2,
	false,
	false,
	true)
   }
 }catch(err){
		
 }
}

//从URL增加印章文档指定位置
function AddSignFromURL(URL)
{
	try {
	   if(TANGER_OCX_bDocOpen)
	   {
	      TANGER_OCX_OBJ.AddSignFromURL(
		"匿名用户",//当前登录用户
		URL,//URL
		50,//left
		50)//top
	   }
	}catch(e){}
}

//从URL增加印章文档指定位置 Add by shixy 2010-04-21
function AddSecSignFromURL(URL)
{
	try {
	   if(TANGER_OCX_OBJ.doctype==1||TANGER_OCX_OBJ.doctype==2){
	   		TANGER_OCX_OBJ.AddSecSignFromURL("匿名用户",URL)
	   }else{
	   		alert("不能在该类型文档中使用安全签名印章.");
	   }
	}catch(e){}
}

//开始手写签名
function DoHandSign()
{
   if(TANGER_OCX_bDocOpen)
   {	
	TANGER_OCX_OBJ.DoHandSign2(
	TANGER_OCX_Username,//当前登录用户 必须
	"",0,0,0); //top//可选参数
	}
}
//开始手工绘图，可用于手工批示
function DoHandDraw()
{
	if(TANGER_OCX_bDocOpen)
	{	
	TANGER_OCX_OBJ.DoHandDraw2(
	0,0,0);//top optional
	}
}
//检查签名结果
function DoCheckSign()
{
	if(TANGER_OCX_bDocOpen)
	{		
	var ret = TANGER_OCX_OBJ.DoCheckSign
	(
	/*可选参数 IsSilent 缺省为FAlSE，表示弹出验证对话框,否则，只是返回验证结果到返回值*/
	);//返回值，验证结果字符串
	//alert(ret);
	}	
}
//此函数用来加入一个自定义的文件头部
function TANGER_OCX_AddDocHeader( strHeader )
{
	var i,cNum = 30;
	var lineStr = "";
	try
	{
		for(i=0;i<cNum;i++) lineStr += "_";  //生成下划线
		with(TANGER_OCX_OBJ.ActiveDocument.Application)
		{
			Selection.HomeKey(6,0); // go home
			Selection.TypeText(strHeader);
			Selection.TypeParagraph(); 	//换行
			Selection.TypeText(lineStr);  //插入下划线
			// Selection.InsertSymbol(95,"",true); //插入下划线
			Selection.TypeText("★");
			Selection.TypeText(lineStr);  //插入下划线
			Selection.TypeParagraph();
			//Selection.MoveUp(5, 2, 1); //上移两行，且按住Shift键，相当于选择两行
			Selection.HomeKey(6,1);  //选择到文件头部所有文本
			Selection.ParagraphFormat.Alignment = 1; //居中对齐
			with(Selection.Font)
			{
				NameFarEast = "宋体";
				Name = "宋体";
				Size = 12;
				Bold = false;
				Italic = false;
				Underline = 0;
				UnderlineColor = 0;
				StrikeThrough = false;
				DoubleStrikeThrough = false;
				Outline = false;
				Emboss = false;
				Shadow = false;
				Hidden = false;
				SmallCaps = false;
				AllCaps = false;
				Color = 255;
				Engrave = false;
				Superscript = false;
				Subscript = false;
				Spacing = 0;
				Scaling = 100;
				Position = 0;
				Kerning = 0;
				Animation = 0;
				DisableCharacterSpaceGrid = false;
				EmphasisMark = 0;
			}
			Selection.MoveDown(5, 3, 0); //下移3行
		}
	}
	catch(err){
		//alert("错误：" + err.number + ":" + err.description);
	}
	finally{
	}
}
function strtrim(value)
{
	return value.replace(/^\s+/,'').replace(/\s+$/,'');
}

function TANGER_OCX_doFormOnSubmit()
{
	var form = document.forms[0];
  	if (form.onsubmit)
	{
    	var retVal = form.onsubmit();
     	if (typeof retVal == "boolean" && retVal == false)
       	return false;
	}
	return true;
}

//允许或禁止显示修订工具栏和工具菜单（保护修订）
//Office2007不支持此方式　yanghf disable 20100329 
//function TANGER_OCX_EnableReviewBar(boolvalue)
//{
//	TANGER_OCX_OBJ.ActiveDocument.CommandBars("Reviewing").Enabled = boolvalue;
//	TANGER_OCX_OBJ.ActiveDocument.CommandBars("Track Changes").Enabled = boolvalue;
//	TANGER_OCX_OBJ.IsShowToolMenu = boolvalue;	//关闭或打开工具菜单
//}

//打开或者关闭修订模式
//需要用保护文档方式保护修订　yanghf disable 20100329 
//function TANGER_OCX_SetReviewMode(boolvalue)
//{
//	try {
//		TANGER_OCX_OBJ.ActiveDocument.TrackRevisions = boolvalue;
//	} catch(e){
//	}
//	
//}

//设置用户名
function TANGER_OCX_SetDocUser(cuser)
{
	with(TANGER_OCX_OBJ.ActiveDocument.Application)
	{
		UserName = cuser;
	}	
}

//进入或退出痕迹保留状态，调用上面的两个函数
//Office2007不支持此方式
//function TANGER_OCX_SetMarkModify(boolvalue)
//{
//	TANGER_OCX_SetReviewMode(boolvalue);
//	TANGER_OCX_EnableReviewBar(!boolvalue);
//}

//显示/不显示修订文字
function TANGER_OCX_ShowRevisions(boolvalue)
{
	TANGER_OCX_OBJ.ActiveDocument.ShowRevisions = boolvalue;
}

//打印/不打印修订文字
function TANGER_OCX_PrintRevisions(boolvalue)
{
	TANGER_OCX_OBJ.ActiveDocument.PrintRevisions = boolvalue;
}

function TANGER_OCX_SaveToServer()
{
	if(!TANGER_OCX_bDocOpen)
	{
		alert("没有打开的文档。");
		return;
	}
	
	TANGER_OCX_filename = prompt("附件另存为：","新文档.doc");
	if ( (!TANGER_OCX_filename))
	{
		TANGER_OCX_filename ="";
		return;
	}
	else if (strtrim(TANGER_OCX_filename)=="")
	{
		alert("您必须输入文件名！");
		return;
	}
	TANGER_OCX_SaveDoc();
}


//设置页面布局
function TANGER_OCX_ChgLayout()
{
 	try
	{
		TANGER_OCX_OBJ.showdialog(5); //设置页面布局
	}
	catch(err){
		alert("错误：" + err.number + ":" + err.description);
	}
	finally{
	}
}

//打印文档
function TANGER_OCX_PrintDoc()
{
	try
	{
		TANGER_OCX_OBJ.printout(true);
	}
	catch(err){
		alert("错误：" + err.number + ":" + err.description);
	}
	finally{
	}
}

function TANGER_OCX_SaveEditToServer()
{
	if(!TANGER_OCX_bDocOpen)
	{
		alert("没有打开的文档。");
		return;
	}
	
	TANGER_OCX_filename = document.all.item("filename").value;
	if ( (!TANGER_OCX_filename))
	{
		TANGER_OCX_filename ="";
		return;
	}
	else if (strtrim(TANGER_OCX_filename)=="")
	{
		alert("您必须输入文件名！");
		return;
	}
	//alert(TANGER_OCX_filename);
	TANGER_OCX_SaveDoc();
}

//允许或禁止文件－>新建菜单
function TANGER_OCX_EnableFileNewMenu(boolvalue)
{
	TANGER_OCX_OBJ.FileNew = boolvalue;
}
//允许或禁止文件－>打开菜单
function TANGER_OCX_EnableFileOpenMenu(boolvalue)
{
	TANGER_OCX_OBJ.FileOpen = boolvalue;
}
//允许或禁止文件－>关闭菜单
function TANGER_OCX_EnableFileCloseMenu(boolvalue)
{
	TANGER_OCX_OBJ.FileClose = boolvalue;
}
//允许或禁止文件－>保存菜单
function TANGER_OCX_EnableFileSaveMenu(boolvalue)
{
	TANGER_OCX_OBJ.FileSave = boolvalue;
}
//允许或禁止文件－>另存为菜单
function TANGER_OCX_EnableFileSaveAsMenu(boolvalue)
{
	TANGER_OCX_OBJ.FileSaveAs = boolvalue;
}
//允许或禁止文件－>打印菜单
function TANGER_OCX_EnableFilePrintMenu(boolvalue)
{
	TANGER_OCX_OBJ.FilePrint = boolvalue;
}
//允许或禁止文件－>打印预览菜单
function TANGER_OCX_EnableFilePrintPreviewMenu(boolvalue)
{
	TANGER_OCX_OBJ.FilePrintPreview = boolvalue;
}

//允许或禁止文件－>页面设置菜单
function TANGER_OCX_EnableFilePageSetupMenu(boolvalue)
{
	TANGER_OCX_OBJ.FilePageSetup = boolvalue;
}

//允许或禁止文件－>属性菜单
function TANGER_OCX_EnableFilePropertiesMenu(boolvalue)
{
	TANGER_OCX_OBJ.FileProperties=boolvalue;
}

function TANGER_OCX_OpenDoc()
{
	TANGER_OCX_OBJ = document.all.item("TANGER_OCX");

	TANGER_OCX_OBJ.CreateNew("Word.Document");
}

function TANGER_OCX_OnDocumentOpened(str, obj)
{
	TANGER_OCX_bDocOpen = true;		
}

function TANGER_OCX_OnDocumentClosed()
{
   TANGER_OCX_bDocOpen = false;
}

function TANGER_OCX_SaveDoc(strAuthType)
{
	var newwin,newdoc;

	if(!TANGER_OCX_bDocOpen)
	{
		alert("没有打开的文档。");
		return;
	}

	try
	{
	 	if(!TANGER_OCX_doFormOnSubmit())return; //如果存在，则执行表单的onsubmit函数。
		//如果文档未保护，在保存时设置文档保护 
		if (TANGER_OCX_OBJ.ActiveDocument.TrackRevisions == false){doProtect(0);}
		//保存时设置AD集成验证的口令
		strAuthType = (strAuthType==null) ? "" : strAuthType.toLowerCase();
		if (strAuthType == "negotiate" || strAuthType == "basic"){
			TANGER_OCX_OBJ.AddHTTPHeader("Authorization: Basic " + SessionAuthKey);
		}
	 	//调用控件的SaveToURL函数
		var retHTML = TANGER_OCX_OBJ.SaveToURL
		(
			document.forms[0].action,  //此处为uploadedit.asp
			"wordBody",	//文件输入域名称,可任选,不与其他<input type=file name=..>的name部分重复即可
			"", //可选的其他自定义数据－值对，以&分隔。如：myname=tanger&hisname=tom,一般为空
			"wordName", //文件名,此处从表单输入获取，也可自定义
			"myForm" //控件的智能提交功能可以允许同时提交选定的表单的所有数据.此处可使用id或者序号
		); //此函数会读取从服务器上返回的信息并保存到返回值中。
		//打开一个新窗口显示返回数据
        return retHTML;
	}
	
	
	catch(err){
		alert("不能保存到URL：" + err.number + ":" + err.description);
	}
	
}

//正文模板的在线编辑不需要对文档进行保护控制
function TANGER_OCX_SaveDocUnprotect(strAuthType)
{
	var newwin,newdoc;

	if(!TANGER_OCX_bDocOpen)
	{
		alert("没有打开的文档。");
		return;
	}

	try
	{
	 	if(!TANGER_OCX_doFormOnSubmit())return; //如果存在，则执行表单的onsubmit函数。

		//保存时设置AD集成验证的口令
		strAuthType = (strAuthType==null) ? "" : strAuthType.toLowerCase();
		if (strAuthType == "negotiate" || strAuthType == "basic"){
			TANGER_OCX_OBJ.AddHTTPHeader("Authorization: Basic " + SessionAuthKey);
		}
	 	//调用控件的SaveToURL函数
		var retHTML = TANGER_OCX_OBJ.SaveToURL
		(
			document.forms[0].action,  //此处为uploadedit.asp
			"wordBody",	//文件输入域名称,可任选,不与其他<input type=file name=..>的name部分重复即可
			"", //可选的其他自定义数据－值对，以&分隔。如：myname=tanger&hisname=tom,一般为空
			"wordName", //文件名,此处从表单输入获取，也可自定义
			"myForm" //控件的智能提交功能可以允许同时提交选定的表单的所有数据.此处可使用id或者序号
		); //此函数会读取从服务器上返回的信息并保存到返回值中。
		//打开一个新窗口显示返回数据
        return retHTML;
	}

	catch(err){
		alert("不能保存到URL：" + err.number + ":" + err.description);
	}
	
}

function insertRedHeadFromUrl(headFileURL,strAuthType)
{
	try
	{
		var wOldType = doUnProtect();	//先解除文档保护
		if(TANGER_OCX_OBJ.doctype!=1)//TANGER_OCX_OBJ.doctype=1为word文档
		{return;}
		if (TANGER_OCX_OBJ.ActiveDocument.Bookmarks.Exists("zhengwen")){
			TANGER_OCX_OBJ.ActiveDocument.Bookmarks("zhengwen").Select();
			TANGER_OCX_OBJ.ActiveDocument.Application.Selection.Delete();		//删除书签
			//TANGER_OCX_OBJ.ActiveDocument.Application.Selection.Delete(1,1);	//删除段落
		}else{
			TANGER_OCX_OBJ.ActiveDocument.Application.Selection.HomeKey(6);
		}
	
		//设置AD集成验证的口令
		strAuthType = (strAuthType==null) ? "" : strAuthType.toLowerCase();
		if (strAuthType == "negotiate" || strAuthType == "basic"){
			TANGER_OCX_OBJ.AddHTTPHeader("Authorization: Basic " + SessionAuthKey);
			alert("inserRedHead");
		}
		TANGER_OCX_OBJ.addtemplatefromurl(headFileURL);//在光标位置插入红头文档
		doProtect(wOldType);			//最后加文档保护
	}
	catch(err){
		alert("不能进行套红操作：" + err.number + ":" + err.description);
	}
}


function TANGER_OCX_SaveAsHTML()
{
	var newwin,newdoc;

	if(!TANGER_OCX_bDocOpen)
	{
		alert("没有打开的文档。");
		return;
	}
	try
	{
		//调用控件的PublishAsHTMLToURL函数
		var retHTML = TANGER_OCX_OBJ.PublishAsHTMLToURL
			(
				"uploadhtmls.jsp",
				"HTMLFILES", //文件输入域名称,可任选,所有相关文件都以此域上传
				"",
				document.forms[0].htmlfile.value
				//此处省略了第5个参数，HTML FORM得索引或者id.这样,不会提交表单数据
				//只提交所有得html文件相关得文件
			);
		newwin = window.open("","_blank","left=200,top=200,width=400,height=300,status=0,toolbar=0,menubar=0,location=0,scrollbars=1,resizable=1",false);
		newdoc = newwin.document;
		newdoc.open();
		newdoc.write("<center><hr>"+retHTML+"<hr><input type=button VALUE='关闭窗口' onclick='window.close()'></center>");
		newdoc.close();	
		newwin.focus();
		if(window.opener) //如果父窗口存在，刷新并关闭当前窗口
		{
			window.opener.location.reload();
		}
	}
	catch(err){
		alert("不能保存到URL：" + err.number + ":" + err.description);
	}
	finally{
	}
}
 //域同步
function TextToBookMark(sKeepValue, sWordFieldName, sFieldValue){
	try {
		if (sKeepValue == 1 || sKeepValue == true || sKeepValue == 'true') {
			if (null != sWordFieldName && "" != sWordFieldName) {
				if (null != sFieldValue && "" != sFieldValue) {
					var sFNs = sWordFieldName.split(",");
					var sFVs = sFieldValue.split("@@XY@@");
					if (sFNs.length == sFVs.length) {
						var wOldType = doUnProtect(); //首先解除文档保护
						for (var i = 0; i < sFNs.length; i++) {
							TANGER_OCX_OBJ.SetBookmarkValue(sFNs[i], sFVs[i]);
						}
						doProtect(wOldType); //最后增加文档保护	 
					}
				}
			}
		}
	}
	catch(err){
	}
	finally{
	}
}
//接受所有修订
function TANGER_OCX_AcceptAllRevisions()
{
	try{
		var wOldType = doUnProtect();	//首先解除文档保护
		TANGER_OCX_OBJ.ActiveDocument.AcceptAllRevisions();
		doProtect(wOldType);		//最后增加文档保护
	}catch(ex){}
}
//拒绝所有修订
function TANGER_OCX_RejectAllRevisions()
{
	try{
		var wOldType = doUnProtect();	//首先解除文档保护
		TANGER_OCX_OBJ.ActiveDocument. RejectAllRevisions ();
		doProtect(wOldType);		//最后增加文档保护
	}catch(ex){}
}

//痕迹保留 以跟踪文档中所做的修订。 yanghf modify 20100329
function TrackRevisions(boolvalue){
	
	try {
		if (boolvalue){
			TANGER_OCX_OBJ.ActiveDocument.TrackRevisions = true;	//只用于记录状态：是否保留痕迹
			doProtect(0);
		} else {
			TANGER_OCX_OBJ.ActiveDocument.TrackRevisions = false;	//只用于记录状态：是否保留痕迹
			doUnProtect();
		}
	} catch(w){
	}
	
}


function addWaterMark(text){
	try{
		var wOldType = doUnProtect();	//首先解除文档保护 
		var ActiveDocument = TANGER_OCX_OBJ.ActiveDocument;
		ActiveDocument.Sections(1).Range.Select();
		ActiveDocument.ActiveWindow.ActivePane.View.SeekView= 9; //wdSeekCurrentPageHeader
		var Selection = ActiveDocument.Application.Selection;
		Selection.HeaderFooter.Shapes.AddTextEffect(0,text, "宋体", 1, false, false, 0, 0).Select();
		Selection.ShapeRange.TextEffect.NormalizedHeight= false;
		Selection.ShapeRange.Line.Visible =false;
		Selection.ShapeRange.Fill.Visible =true;
		Selection.ShapeRange.Fill.Solid();
		Selection.ShapeRange.Fill.ForeColor.RGB= 12345;
		Selection.ShapeRange.Fill.Transparency= 0.5;
		Selection.ShapeRange.Rotation = 315;
		Selection.ShapeRange.LockAspectRatio= true;
		Selection.ShapeRange.Height = ActiveDocument.Application.CentimetersToPoints(4.13);
		Selection.ShapeRange.Width = ActiveDocument.Application.CentimetersToPoints(16.52);
		Selection.ShapeRange.WrapFormat.AllowOverlap= true;
		Selection.ShapeRange.WrapFormat.Side= 3;//wdWrapNone
		Selection.ShapeRange.WrapFormat.Type= 3;
		Selection.ShapeRange.RelativeHorizontalPosition= 0;//wdRelativeVerticalPositionMargin
		Selection.ShapeRange.RelativeVerticalPosition= 0; //wdRelativeVerticalPositionMargin
		Selection.ShapeRange.Left = -999995;//wdShapeCenter
		Selection.ShapeRange.Top = -999995;//wdShapeCenter
		ActiveDocument.ActiveWindow.ActivePane.View.SeekView= 0;//wdSeekMainDocument
		doProtect(wOldType);		//最后增加文档保护
	} catch(err){
		alert("addWaterMark errir:"+ err.number + ":" + err.description);
	}
}

//***************************************************
//yanghf add 20100322
//解除文档保护，并返回解除保护之前的文档保护方式
function doUnProtect(){
try{
    //首先记录原来的文档保护方式
    var wOldType = TANGER_OCX_OBJ.ActiveDocument.ProtectionType;

    //如果原来的保护方式不是wdNoProtection 就解除文档保护
    if (wOldType != -1) {
       TANGER_OCX_OBJ.ActiveDocument.UnProtect("word&7t8eb5!");
    }
    //返回原来的文档保护方式
    return wOldType
}catch(e){}
}

//设置文档保护，参数为文档保护方式
function doProtect(nProtectionType){
try{
    //如果nProtectionType保护方式不是wdNoProtection 就设置成nProtectionType保护方式
    if (nProtectionType != -1) {
       TANGER_OCX_OBJ.ActiveDocument.Protect(nProtectionType, true, "word&7t8eb5!");
    }
}catch(e){}
}
//***************************************************
