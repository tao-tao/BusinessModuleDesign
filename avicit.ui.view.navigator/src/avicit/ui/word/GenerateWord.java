package avicit.ui.word;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.dom4j.io.SAXReader;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.w3c.dom.Node;

import avicit.ui.common.util.FileUtil;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.rtf.RtfWriter2;

/**
 * @author Tao Tao
 *
 */
public class GenerateWord {
	private static String documentName = "组件需求规格说明书.doc";
	private Paragraph title1;
	private Paragraph title2;
	private Paragraph title3;
	private Paragraph context;
	private Table table;
	private Cell cell;

	private GenerateWord(){
	}

	public void generate(final IResource resource){
		if(resource instanceof IFolder){
			Display display = Display.getDefault();
			if (display != null) {
				display.asyncExec(new Runnable(){
					public void run(){
						Document document = new Document();
						IFolder destination = (IFolder) resource;
						ComponentModelInfo componentInfo = new ComponentModelInfo();
						File xml = new File(destination.getParent().getParent().getParent().getLocation().toOSString()+File.separator+"组件依赖描述文件.xml");
						parseComponentModel(xml, componentInfo);
						File file = new File(destination.getLocation().toOSString()+File.separator+"需求管理_"+"组件编号_"+componentInfo.getComponentID()+"_组件版本_"+componentInfo.getComponentVersion()+"_"+documentName);

						try{
							RtfWriter2.getInstance(document, new FileOutputStream(file));
							document.open();

							addTitle(document, title1, "1 组件名称", FontsAndColorsRepository.title1Font);
							addTitle(document, title2, "1.1 组件编号", FontsAndColorsRepository.title2Font);

							addContext(document, context, componentInfo.getComponentID(), FontsAndColorsRepository.font1);
//							addContext(document, context, "参见组件编号定义规则", FontsRepository.font1);
//							addContext(document, context, "工程研制系统：avicit.eds.子系统缩写.组件缩写", FontsRepository.font1);
//							addContext(document, context, "制造管理系统：avicit.mms.子系统缩写.组件缩写", FontsRepository.font1);
//							addContext(document, context, "客户服务系统：avicit.css.子系统缩写.组件缩写", FontsRepository.font1);
//							addContext(document, context, "项目管理系统：avicit.pms.子系统缩写.组件缩写", FontsRepository.font1);
//							addContext(document, context, "综合管理系统：avicit.ims.子系统缩写.组件缩写", FontsRepository.font1);
//							addContext(document, context, "平台：avicit.platform.子系统缩写.组件缩写", FontsRepository.font1);
							addTitle(document, title2, "1.2 组件版本", FontsAndColorsRepository.title2Font);
							addContext(document, context, componentInfo.getComponentVersion(), FontsAndColorsRepository.font1);
							addTitle(document, title2, "1.3 组件需求描述", FontsAndColorsRepository.title2Font);
							addContext(document, context, "通过业务场景来描述组件需求,简要说明此业务场景的作用范围及它的相关业务，以及受到此业务场景影响的任何其他事物。", FontsAndColorsRepository.font1);

							table = new Table(2, 7);
							table.setCellsFitPage(true);
							table.setTableFitsPage(true);
							table.setAlignment(Element.ALIGN_CENTER);
							table.setWidths(new int[] { 5, 15 });
							cell = new Cell(new Paragraph("场景名称", FontsAndColorsRepository.font2));
							cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							cell.setVerticalAlignment(Element.ALIGN_CENTER);
							table.addCell(cell, 0, 0);
							cell = new Cell(new Paragraph("参与者", FontsAndColorsRepository.font2));
							cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							cell.setVerticalAlignment(Element.ALIGN_CENTER);
							table.addCell(cell, 1, 0);
							cell = new Cell(new Paragraph("前提条件/假设", FontsAndColorsRepository.font2));
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							cell.setVerticalAlignment(Element.ALIGN_CENTER);
							cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
							table.addCell(cell, 2, 0);
							cell = new Cell(new Paragraph("典型场景描述", FontsAndColorsRepository.font2));
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							cell.setVerticalAlignment(Element.ALIGN_CENTER);
							cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
							table.addCell(cell, 3, 0);
							cell = new Cell(new Paragraph("结 果", FontsAndColorsRepository.font2));
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							cell.setVerticalAlignment(Element.ALIGN_CENTER);
							cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
							table.addCell(cell, 4, 0);
							cell = new Cell(new Paragraph("当前存在问题", FontsAndColorsRepository.font2));
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							cell.setVerticalAlignment(Element.ALIGN_CENTER);
							cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
							table.addCell(cell, 5, 0);
							cell = new Cell(new Paragraph("备 注", FontsAndColorsRepository.font2));
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							cell.setVerticalAlignment(Element.ALIGN_CENTER);
							cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
							table.addCell(cell, 6, 0);
							document.add(table);

							addContext(document, context, "[说明：", FontsAndColorsRepository.font1);
							addContext(document, context, "•	前提条件/假设：描述该典型场景发生的前提条件。", FontsAndColorsRepository.font1);
							addContext(document, context, "•	典型场景描述：结合典型案例把业务场景用一种故事的形式详细描述。", FontsAndColorsRepository.font1);
							addContext(document, context, "•	结果：描述场景得到的结果。", FontsAndColorsRepository.font1);
							addContext(document, context, "•	当前存在问题：提出以上描述中存在的问题和可能存在的问题。", FontsAndColorsRepository.font1);
							addContext(document, context, "]", FontsAndColorsRepository.font1);
							addContext(document, context, "", FontsAndColorsRepository.font1);
							addContext(document, context, "以下是示例，供您参考.", FontsAndColorsRepository.font1);

							table = new Table(2, 7);
							table.setCellsFitPage(true);
							table.setTableFitsPage(true);
							table.setWidths(new float[] {3, 8});
							table.setAlignment(Element.ALIGN_CENTER);
							cell = new Cell(new Paragraph("场景名称", FontsAndColorsRepository.font2));
							cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							cell.setVerticalAlignment(Element.ALIGN_CENTER);
							table.addCell(cell);
							cell = new Cell(new Paragraph("电话申请", FontsAndColorsRepository.font2));
							cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
							table.addCell(cell);
							cell = new Cell(new Paragraph("参与者", FontsAndColorsRepository.font2));
							cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							cell.setVerticalAlignment(Element.ALIGN_CENTER);
							table.addCell(cell);
							cell = new Cell(new Paragraph("一般用户", FontsAndColorsRepository.font2));
							cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
							table.addCell(cell);
							cell = new Cell(new Paragraph("前提条件/假设", FontsAndColorsRepository.font2));
							cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							cell.setVerticalAlignment(Element.ALIGN_CENTER);
							table.addCell(cell);
							cell = new Cell(new Paragraph("该用户具有较好的资信", FontsAndColorsRepository.font2));
							cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
							table.addCell(cell);
							cell = new Cell(new Paragraph("典型场景描述", FontsAndColorsRepository.font2));
							cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							cell.setVerticalAlignment(Element.ALIGN_CENTER);
							table.addCell(cell);
							cell = new Cell(new Paragraph("XX用户打电话给餐饮服务中介公司，要求他们给于定餐服务。中介公司询问了XX用户的具体地点、联系人、电话号码、一般的定餐需求，并直接在系统中作了记录。", FontsAndColorsRepository.font2));
							cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
							table.addCell(cell);
							cell = new Cell(new Paragraph("结果", FontsAndColorsRepository.font2));
							cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							cell.setVerticalAlignment(Element.ALIGN_CENTER);
							table.addCell(cell);
							cell = new Cell(new Paragraph("申请被接受，等待资信确认", FontsAndColorsRepository.font2));
							cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
							table.addCell(cell);
							cell = new Cell(new Paragraph("当前存在问题", FontsAndColorsRepository.font2));
							cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							cell.setVerticalAlignment(Element.ALIGN_CENTER);
							table.addCell(cell, 5, 0);
							cell = new Cell(new Paragraph("备注", FontsAndColorsRepository.font2));
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							cell.setVerticalAlignment(Element.ALIGN_CENTER);
							cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
							table.addCell(cell, 6, 0);
							document.add(table);
							addTitle(document, title2, "1.4 组件的用例图", FontsAndColorsRepository.title2Font);
							addContext(document, context, "从用户角度描述系统功能，并指各功能的操作者。使用用例图主要用于描述关键性的组件功能模块需求，也用于限定软件开发的范围。", FontsAndColorsRepository.font1);

							IFolder imagesFolder = destination.getParent().getFolder(new Path("usercase"));
							List<?> listImages = getImages(imagesFolder);

							if(!listImages.isEmpty()){
								addImages(document, listImages, 400, 300, Element.ALIGN_CENTER);
							}

							addTitle(document, title2, "1.5 组件的用例规约", FontsAndColorsRepository.title2Font);
							addContext(document, context, "[Use Case描述对Use Case图中相应的Use Case进行详细描述，其中包括该Use Case的前提条件、主导事件及其事后条件，替换事件及其事后条件。 ", FontsAndColorsRepository.font1);

							IFolder usercaseFolder = destination.getParent().getFolder(new Path("usercase"));
							List<?> files = getUcdFiles(usercaseFolder);
							UsecaseModelInfo info = new UsecaseModelInfo();
							int count = 1;

							for(Object resource : files){

								if(resource instanceof IFile){
									parseUsecaseModel(document, new File(((IFile) resource).getLocation().toOSString()), info, count++);
								}
							}

							addTitle(document, title1, "1.6 组件的活动图", FontsAndColorsRepository.title2Font);
							addContext(document, context, "使用活动图描述组件内涉及的业务流程。 ", FontsAndColorsRepository.font1);

							IFolder businessFolder = destination.getParent().getFolder(new Path("business"));
							listImages = getImages(businessFolder);

							if(!listImages.isEmpty()){
								addImages(document, listImages, 400, 300, Element.ALIGN_CENTER);
							}

							addTitle(document, title2, "1.7 组件对外提供服务描述", FontsAndColorsRepository.title2Font);
							addContext(document, context, "此处说明本组件向外部可提供的服务需求描述，确定可以保证本组件与其它系统中的组件正确连接的需求。", FontsAndColorsRepository.font1);
							addTableTitle(document, context, "外部接口列表", FontsAndColorsRepository.font6);

							table = new Table(2, 7);
							table.setCellsFitPage(true);
							table.setTableFitsPage(true);
							table.setWidths(new float[] { 4, 8 });
							table.setAlignment(Element.ALIGN_CENTER);
							cell = new Cell(new Paragraph("接口名称", FontsAndColorsRepository.font3));
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							cell.setVerticalAlignment(Element.ALIGN_CENTER);
							cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
							table.addCell(cell);
							cell = new Cell(new Paragraph("接口的需求描述", FontsAndColorsRepository.font3));
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							cell.setVerticalAlignment(Element.ALIGN_CENTER);
							cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
							table.addCell(cell);
							cell = new Cell(new Paragraph("消息显示接口", FontsAndColorsRepository.font4));
							cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
							cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
							table.addCell(cell);
							cell = new Cell(new Paragraph("通过该接口显示有必要向用户提示**子系统、操作系统的各种消息", FontsAndColorsRepository.font4));
							cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
							cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
							table.addCell(cell);
							cell = new Cell(new Paragraph("业务数据库接口", FontsAndColorsRepository.font4));
							cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
							cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
							table.addCell(cell);
							cell = new Cell(new Paragraph("存取**业务中产生的方案、指示、通知等相关文档和数据。", FontsAndColorsRepository.font4));
							cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
							cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
							table.addCell(cell);
							document.add(table);

							addTitle(document, title2, "1.8  组件数据需求描述", FontsAndColorsRepository.title2Font);
							addContext(document, context, "1、	对业务场景、业务功能需求中的输入和输出数据进行整理的过程。", FontsAndColorsRepository.font1);
							addContext(document, context, "2、	数据量估计（以5年为周期进行预估计）", FontsAndColorsRepository.font1);
							addContext(document, context, "3、	对用户需求进行综合、归纳与抽象，给出该组件对应的数据库概念模型，比如E-R（实体关系）图。", FontsAndColorsRepository.font1);
							File imageFile = new File(Platform.getInstallLocation().getURL().getPath()+"/dropins/avicit_platform/plugins/META-INF/config/example.jpeg");
							Image image = Image.getInstance(imageFile.getAbsolutePath());
							addImage(document, image, 350, 50, Element.ALIGN_CENTER);
							addContext(document, context, "4、	核心数据项以列表形式进行描述展示：", FontsAndColorsRepository.font1);

							table = new Table(6, 6);
							table.setCellsFitPage(true);
							table.setTableFitsPage(true);
							table.setAlignment(Element.ALIGN_CENTER);
							cell = new Cell(new Paragraph("序号", FontsAndColorsRepository.font2));
							cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							table.addCell(cell);
							cell = new Cell(new Paragraph("数据项名称", FontsAndColorsRepository.font2));
							cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							table.addCell(cell);
							cell = new Cell(new Paragraph("数据类型(长度)", FontsAndColorsRepository.font2));
							cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							table.addCell(cell);
							cell = new Cell(new Paragraph("业务规则", FontsAndColorsRepository.font2));
							cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							table.addCell(cell);
							cell = new Cell(new Paragraph("数据来源", FontsAndColorsRepository.font2));
							cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							table.addCell(cell);
							cell = new Cell(new Paragraph("备注", FontsAndColorsRepository.font2));
							cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							table.addCell(cell);
							cell = new Cell(new Paragraph("1", FontsAndColorsRepository.font4));
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
							table.addCell(cell);
							cell = new Cell(new Paragraph("出库单号", FontsAndColorsRepository.font4));
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
							table.addCell(cell);
							cell = new Cell(new Paragraph("字符（30）", FontsAndColorsRepository.font4));
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
							table.addCell(cell);
							cell = new Cell(new Paragraph("非空", FontsAndColorsRepository.font4));
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
							table.addCell(cell);
							cell = new Cell(new Paragraph("系统自动生成，编码规则：", FontsAndColorsRepository.font4));
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
							table.addCell(cell);
							cell = new Cell(new Paragraph("唯一标识", FontsAndColorsRepository.font4));
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
							table.addCell(cell);
							cell = new Cell(new Paragraph("2", FontsAndColorsRepository.font4));
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
							table.addCell(cell);
							cell = new Cell(new Paragraph("物料编码", FontsAndColorsRepository.font4));
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
							table.addCell(cell);
							cell = new Cell(new Paragraph("字符（30）", FontsAndColorsRepository.font4));
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
							table.addCell(cell);
							cell = new Cell(new Paragraph("非空", FontsAndColorsRepository.font4));
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
							table.addCell(cell);
							cell = new Cell(new Paragraph("按照用户出库权限，手工选择", FontsAndColorsRepository.font4));
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
							table.addCell(cell);
							cell = new Cell(new Paragraph("3", FontsAndColorsRepository.font4));
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
							table.addCell(cell, 3, 0);
							cell = new Cell(new Paragraph("出库数量", FontsAndColorsRepository.font4));
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
							table.addCell(cell);
							cell = new Cell(new Paragraph("数字", FontsAndColorsRepository.font4));
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
							table.addCell(cell);
							cell = new Cell(new Paragraph("<>0非空", FontsAndColorsRepository.font4));
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
							table.addCell(cell);
							cell = new Cell(new Paragraph("4", FontsAndColorsRepository.font4));
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
							table.addCell(cell, 4, 0);
							cell = new Cell(new Paragraph("库房号", FontsAndColorsRepository.font4));
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
							table.addCell(cell);
							cell = new Cell(new Paragraph("字符（30）", FontsAndColorsRepository.font4));
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
							table.addCell(cell);
							cell = new Cell(new Paragraph("手工选择物料所在库房", FontsAndColorsRepository.font4));
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
							table.addCell(cell, 4, 4);
							document.add(table);

							addTitle(document, title2, "1.9 组件验收准则", FontsAndColorsRepository.title2Font);
							addContext(document, context, "说明用于验证满足需求的验收准则。", FontsAndColorsRepository.font1);

							document.close();
						}catch(FileNotFoundException e){
							createErrorMessage(file, " 另一个程序正在使用此文件，请关闭相应进程再重新导出。");
							e.printStackTrace();
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				});
			}
		}
	}

	private void parseComponentModel(File file, ComponentModelInfo info){
		String element = null;

		try{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); 
			DocumentBuilder db = dbf.newDocumentBuilder();
			org.w3c.dom.Document document = db.parse(file);

			XPathFactory xpf = XPathFactory.newInstance();
			XPath xpath = xpf.newXPath();

			if(xpath.evaluate("/components/component/id", document) == ""){
				element = "id字段";
			}

			if(xpath.evaluate("/components/component/version", document) == ""){
				element = "version字段";
			}

			info.setComponentID(((Node)xpath.evaluate("/components/component/id", document, XPathConstants.NODE)).getTextContent());

			info.setComponentVersion(((Node)xpath.evaluate("/components/component/version", document, XPathConstants.NODE)).getTextContent());
		}catch(NullPointerException e){
			createErrorMessage(file, "文件"+element+"存放位置不合法或缺少，请修改文件并重新导出word。");
			e.printStackTrace();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	private void parseUsecaseModel(Document document, File file, UsecaseModelInfo info, int num){
		try{
			SAXReader reader = new SAXReader();

			org.dom4j.Document doc = reader.read(file);

			org.dom4j.Element root = doc.getRootElement();

			org.dom4j.Element child = root.element("children");

			List<?> results = new ArrayList();

			FileUtil.parseXML("dReg", child, results);

			if(!results.isEmpty()){
				for(Iterator<?> iterator = results.listIterator();iterator.hasNext();){
					org.dom4j.Element element = (org.dom4j.Element) iterator.next();

					element = element.getParent();

					if(element.element("id") != null){
						info.setUsecaseNo(element.element("id").getText());
					}else{
						info.setUsecaseNo(null);
					}

					if(element.element("name") != null){
						info.setUsecaseName(element.element("name").getText());
					}else{
						info.setUsecaseName(null);
					}

					if(element.element("softFlg") !=null){
						info.setUsecaseDescription(element.element("softFlg").getText());
					}else{
						info.setUsecaseDescription(null);
					}

					if(element.element("deException") != null){
						info.setAuthDescription(element.element("deException").getText());
					}else{
						info.setAuthDescription(null);
					}

					if(element.element("dReg") != null){
						info.setParticipant(element.element("dReg").getText());
					}else{
						info.setParticipant(null);
					}

					if(element.element("softGrade") != null){
						info.setStatus(element.element("softGrade").getText());
					}else{
						info.setStatus(null);
					}

					if(element.element("constri") != null){
						info.setPrecondition(element.element("constri").getText());
					}else{
						info.setPrecondition(null);
					}

					if(element.element("testPoint") != null){
						info.setPostcondition(element.element("testPoint").getText());
					}else{
						info.setPrecondition(null);
					}

					WorkFlow workflow = new WorkFlow();
					workflow.setSystemResponse("描述用户对系统的操作步骤"+"\r\n"+"例如："+"\r\n"+"1、XX用户点击“引入费用科目”按钮"+"\r\n"+"2、XX用户在弹出窗口中勾选费用项目后，点击“确定”按钮");
					workflow.setUsrOrsysExcitation("用户对系统操作后，系统的响应描述"+"\r\n"+"系统弹出“引入费用科目”窗口"+"\r\n"+"系统自动把勾选得到的费用科目增加到相应的\"资金来源\"或者\"资金分配\"页面中");

					info.setBasicworkingflow(workflow);

					if(element.element("applyre") != null){
						info.setExceptionOprFlow(element.element("applyre").getText());
					}else{
						info.setExceptionOprFlow(null);
					}

					if(element.element("noRequirement") != null){
						info.setNonFunction(element.element("noRequirement").getText());
					}else{
						info.setNonFunction(null);
					}

					Logging logging = new Logging();
					logging.setLogModifier("");
					logging.setLoggingTime("");
					logging.setModDescription("");

					info.setLogging(logging);

					addUsecaseField(document, context, title3, table, cell, num++, info);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private List<IResource> getUcdFiles(IFolder folder){
		List<IResource> files = new ArrayList<IResource>();

		try {
			IResource[] resources = folder.members();

			for (IResource resource : resources) {
				if (resource.getFileExtension().equals("ucd")) {
					files.add(resource);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return files;
	}

	private List<File> getImages(IFolder folder) {
		List<File> listFiles = new ArrayList<File>();

		try {
			IResource[] resources = folder.members();

			for (IResource resource : resources) {
				if (resource.getFileExtension().equals("jpeg")) {
					File file = resource.getLocation().toFile();
					listFiles.add(file);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return listFiles;
	}

	private void addImages(Document document, List<?> list, float x, float y, int alignment) {
		for (Object obj : list) {
			if (obj instanceof File) {
				String filePath = ((File) obj).getAbsolutePath();

				try {
					Image image = Image.getInstance(filePath);

					addImage(document, image, image.getAbsoluteX(), image.getAbsoluteY(), alignment);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void addImage(Document document, Image image, float x, float y, int alignment){
		try {
			image.scaleAbsolute(x, y);

			image.setAlignment(alignment);

			document.add(image);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addTitle(Document doc, Paragraph title, String titleStr, Font titleFont){
		title = new Paragraph(titleStr, titleFont);
		title.setSpacingAfter(6);
		title.setSpacingBefore(6);
		title.setFirstLineIndent(40);

		try {
			doc.add(title);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addContext(Document doc, Paragraph context, String contextStr, Font font){
		context = new Paragraph(contextStr, font);
		context.setAlignment(Element.ALIGN_LEFT);
		context.setFirstLineIndent(40);
		context.setSpacingBefore(3);
		context.setSpacingAfter(3);

		try {
			doc.add(context);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addTableTitle(Document doc, Paragraph context, String contextStr, Font font){
		context = new Paragraph(contextStr, font);
		context.setAlignment(Element.ALIGN_CENTER);
		context.setSpacingBefore(3);
		context.setSpacingAfter(3);

		try{
			doc.add(context);
		}catch (Exception e) {
			e.printStackTrace();
		} 
	}

	private void addUsecaseField(Document document, Paragraph context, Paragraph title3, Table table, Cell cell, int num, UsecaseModelInfo info){
		try {

			addTitle(document, title3, "1.5.1." + num + "<用例" + info.getUsecaseName() + ">", FontsAndColorsRepository.title3Font);
			addContext(document, context, "1、用例名称", FontsAndColorsRepository.font5);

			table = new Table(3, 16);
			table.setCellsFitPage(true);
			table.setTableFitsPage(true);
			table.setWidths(new float[] {3, 5, 5});
			table.setAlignment(Element.ALIGN_CENTER);
			cell = new Cell(new Paragraph("用例编号", FontsAndColorsRepository.font3));
			cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);
			cell = new Cell(new Paragraph(info.getUsecaseNo(), FontsAndColorsRepository.font4));
			cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
			cell.setColspan(2);
			table.addCell(cell);
			cell = new Cell(new Paragraph("用例名称", FontsAndColorsRepository.font3));
			cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);
			cell = new Cell(new Paragraph(info.getUsecaseName(), FontsAndColorsRepository.font4));
			cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
			cell.setColspan(2);
			table.addCell(cell);
			cell = new Cell(new Paragraph("用例描述", FontsAndColorsRepository.font3));
			cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new Cell(new Paragraph(info.getUsecaseDescription(), FontsAndColorsRepository.font4));
			cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
			cell.setColspan(2);
			table.addCell(cell);
			cell = new Cell(new Paragraph("权限描述", FontsAndColorsRepository.font3));
			cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new Cell(new Paragraph(info.getAuthDescription(), FontsAndColorsRepository.font4));
			cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
			cell.setColspan(2);
			table.addCell(cell);

			cell = new Cell(new Paragraph("参与者", FontsAndColorsRepository.font3));
			cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);
			cell = new Cell(new Paragraph(info.getParticipant(), FontsAndColorsRepository.font4));
			cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
			cell.setColspan(2);
			table.addCell(cell);

			cell = new Cell(new Paragraph("状态", FontsAndColorsRepository.font3));
			cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);
			cell = new Cell(new Paragraph(info.getStatus(), FontsAndColorsRepository.font4));
			cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
			cell.setColspan(2);
			table.addCell(cell);

			cell = new Cell(new Paragraph("前置条件", FontsAndColorsRepository.font3));
			cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new Cell(new Paragraph(info.getPrecondition(), FontsAndColorsRepository.font4));
			cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
			cell.setColspan(2);
			table.addCell(cell);

			cell = new Cell(new Paragraph("后置条件", FontsAndColorsRepository.font3));
			cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell, 7, 0);
			cell = new Cell(new Paragraph(info.getPostcondition(), FontsAndColorsRepository.font4));
			cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
			cell.setColspan(2);
			table.addCell(cell);

			cell = new Cell(new Paragraph("基本工作流程", FontsAndColorsRepository.font3));
			cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
			cell.setRowspan(2);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new Cell(new Paragraph("用户或系统外激励", FontsAndColorsRepository.font3));
			cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell, 8, 1);
			cell = new Cell(new Paragraph(info.getBasicworkingflow().getUsrOrsysExcitation(), FontsAndColorsRepository.font4));
			cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
			table.addCell(cell, 9, 1);
				
			cell = new Cell(new Paragraph("系统响应", FontsAndColorsRepository.font3));
			cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell, 8, 2);
			cell = new Cell(new Paragraph(info.getBasicworkingflow().getSystemResponse(), FontsAndColorsRepository.font4));
			cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
			table.addCell(cell, 9, 2);

			cell = new Cell(new Paragraph("异常操作流程", FontsAndColorsRepository.font3));
			cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new Cell(new Paragraph(info.getExceptionOprFlow(), FontsAndColorsRepository.font4));
			cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
			cell.setColspan(2);
			table.addCell(cell);

			cell = new Cell(new Paragraph("非功能需求", FontsAndColorsRepository.font3));
			cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new Cell(new Paragraph(info.getNonFunction(), FontsAndColorsRepository.font4));
			cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
			cell.setColspan(2);
			table.addCell(cell);

			cell = new Cell(new Paragraph("修改日志", FontsAndColorsRepository.font3));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setColspan(3);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
			table.addCell(cell);

			cell = new Cell(new Paragraph("修改人", FontsAndColorsRepository.font3));
			cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell, 13, 0);

			cell = new Cell(new Paragraph("修改日期", FontsAndColorsRepository.font3));
			cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell, 13, 1);

			cell = new Cell(new Paragraph("修改内容描述", FontsAndColorsRepository.font3));
			cell.setBackgroundColor(FontsAndColorsRepository.backgroundColor);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell, 13, 2);

			document.add(table);
		} catch (Exception e) {
			e.printStackTrace();
		}

		addContext(document, context, "2、"+info.getUsecaseName()+"界面示意图：", FontsAndColorsRepository.font5);
		addContext(document, context, "描述将要展示给用户的界面主要元素，功能按钮、基本布局等。如果包含多个子界面，应描述界面之间的迁移关系。可采用程序截图、电子表格等多种图示方式。（统一用Axure工具绘制）", FontsAndColorsRepository.font1);
		addContext(document, context, "界面1", FontsAndColorsRepository.font1);
		addContext(document, context, "界面1描述", FontsAndColorsRepository.font1);
		addContext(document, context, "界面2", FontsAndColorsRepository.font1);
		addContext(document, context, "界面2描述", FontsAndColorsRepository.font1);
		addContext(document, context, "…….", FontsAndColorsRepository.font1);
	}

	private void createErrorMessage(File file, String message){
		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		MessageDialog.openError(shell, "Error", file.getName()+message);
	}

	public static GenerateWord getInstance(){
		return instance;
	}

	private static GenerateWord instance = new GenerateWord();
}
