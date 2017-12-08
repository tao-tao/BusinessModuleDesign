package avicit.platform6.tools.codegeneration.core.common;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeInstance;
import org.apache.velocity.runtime.RuntimeSingleton;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceRuleFactory;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.core.runtime.jobs.MultiRule;
import org.eclipse.jdt.core.JavaModelException;

import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;

//import com.sun.xml.internal.fastinfoset.util.ContiguousCharArrayArray;


import avicit.platform6.tools.codegeneration.CodeGenerationActivator;
import avicit.platform6.tools.codegeneration.core.Resource;
import avicit.platform6.tools.codegeneration.core.entity.EntityParamInfo;
import avicit.platform6.tools.codegeneration.core.entity.Property;
import avicit.platform6.tools.codegeneration.core.entity.RelationTable;
import avicit.platform6.tools.codegeneration.core.entity.TableBusinessObject;
import avicit.platform6.tools.codegeneration.core.entity.TableInfo;
import avicit.platform6.tools.codegeneration.core.entity.Templete;
import avicit.platform6.tools.codegeneration.core.entity.TempleteFile;
import avicit.platform6.tools.codegeneration.core.exception.TemplateFileNotFoundException;
import avicit.platform6.tools.codegeneration.core.util.TempleteConfig;
import avicit.platform6.tools.codegeneration.core.util.id.IdentityFactory;
import avicit.platform6.tools.codegeneration.log.AvicitLogger;
import avicit.platform6.tools.codegeneration.wizard.CodeGenerationWizard;

/**
 * <p>
 * 金航数码科技有限责任公司
 * </p>
 * <p>
 * 作者：dingrc
 * </p>
 * <p>
 * 邮箱：dingrc@avicit.com
 * </p>
 * <p>
 * 创建时间：2012-12-10
 * </p>
 * 
 * <p>
 * 类说明：代码生成核心
 * </p>
 * <p>
 * 修改记录：
 * </p>
 */
public class CreateCode {

	public class ThreadStatus {

		public static final String Run = "0";
		public static final String Pause = "1";
		public static final String Stop = "2";
	}
	private CodeGenerationWizard codewizard;
	private String modeName;
	private final List templateList = new ArrayList();
	private final List taskList = new ArrayList();
	private static final String VARCHAR2_ORCL = "VARCHAR2";
	private static final String VARCHAR = "VARCHAR";
	private static final String NUMBER_ORCL = "NUMBER";
	private static final String DATE_ORCL = "DATE";
	private static final String TIME_ORCL = "TIMESTAMP";
	private static final String DECIMAL = "DECIMAL";
	/**
	 * 表名称的集合
	 */
	private List<String> tableNames;
	private String srcPath;
	private String webPath;
	private String pagePath;
	private String templatePath;
	private int totalFileCount;
	private int createdFilesCount;
	private String threadStatus;
	private boolean completeStatus;
	private TableBusinessObject tableBusinessObject;
	/**
	 * 是否为主子表
	 */
	private boolean isMainSub;
	
	private boolean isTree;
	
	private boolean isSingleFlowTemplate;

	/**
	 * 主表关联字段
	 */
	private String mainKey;
	/**
	 * 子表关联字段
	 */
	private String subKey;

	private String mainTableName;

	private String subTableName;
	private String webJsp;
	private String partPackName;
	private String dtoPackName;
	
	/**
	 * 子表关联字段
	 */
	private String treeRelationField;
	
	/**
	 * 子表关联字段
	 */
	private String treeSubRelationField;
	
	private String treeDisplayField;

	public String getDtoPackName() {
		return dtoPackName;
	}

	public void setDtoPackName(String dtoPackName) {
		this.dtoPackName = dtoPackName;
	}

	public String getPartPackName() {
		return partPackName;
	}

	public void setPartPackName(String partPackName) {
		this.partPackName = partPackName;
	}

	public void setMainSub(boolean isMainSub) {
		this.isMainSub = isMainSub;
	}

	public void setMainKey(String mainKey) {
		this.mainKey = mainKey;
	}
	
	public boolean isSingleFlowTemplate() {
		return isSingleFlowTemplate;
	}

	public void setSingleFlowTemplate(boolean isSingleFlowTemplate) {
		this.isSingleFlowTemplate = isSingleFlowTemplate;
	}
	
	public void setSubKey(String subKey) {
		this.subKey = subKey;
	}

	public void setMainTableName(String mainTableName) {
		this.mainTableName = mainTableName;
	}

	public void setSubTableName(String subTableName) {
		this.subTableName = subTableName;
	}

	public TableBusinessObject getTableBusinessObject() {
		return tableBusinessObject;
	}

	public void setTableBusinessObject(TableBusinessObject tableBusinessObject) {
		this.tableBusinessObject = tableBusinessObject;
	}

	public CreateCode(String projectPath,String webPath, File templatePath, String modeName,List<String> tableNames, boolean isMaven,String webJsp) throws Exception {
		AvicitLogger.logInfo("begin CreateCode======================= ");
		this.srcPath = projectPath;
		this.webPath = projectPath + File.separator+webPath.replaceAll("/", "\\\\");
		this.webJsp = webJsp;
		this.templatePath = templatePath.getPath();
		totalFileCount = 0;
		createdFilesCount = 0;
		threadStatus = ThreadStatus.Stop;
		completeStatus = false;

		if (projectPath == null || projectPath.equals("")) {
			throw new Exception("没有项目路径!??");
		}

		if (templatePath == null || templatePath.equals("")) {
			throw new Exception("没有模板路径!??");
		}
		this.templatePath = (new StringBuilder(String.valueOf(templatePath)))
				.append(File.separator).toString();
		// if (modeName == null || modeName.equals("")) {
		// throw new Exception("模板名称为空!??");
		// }

		if (tableNames == null || tableNames.size() < 1) {
			throw new Exception("数据表为空!??");
		}

		TempleteConfig.getTempleteConfig().loadTempleteDef(templatePath);
		this.modeName = modeName;
		this.tableNames = tableNames;
		TempleteFile tf = TempleteConfig.getTempleteConfig().getConfig();
		if (isMaven) {
			this.srcPath = projectPath + File.separator + tf.getBase_src()+ File.separator + "main" + File.separator + "java"+ File.separator;
		} else {
//			this.srcPath = projectPath + File.separator + tf.getBase_src()+ File.separator;
//			this.srcPath = projectPath + File.separator;
			//2015-03-09
//			this.srcPath = projectPath + File.separator + "src-common" + File.separator;
			this.srcPath = projectPath + File.separator + CodeGenerationActivator.SOURCE_FOLDER_PATH + File.separator;
		}

		this.pagePath = projectPath + File.separator + tf.getBase_page()+ File.separator + modeName + File.separator;

		// 初始化包路径
		initPackage();
	}

	private void initPackage() {
		TempleteFile tf = TempleteConfig.getTempleteConfig().getConfig();
		for (int i = 0; i < tf.getTempletes().size(); i++) {
			Templete templete = tf.getTempletes().get(i);
			templete.setPack_name(templete.getPack_name().replaceAll("［基础包名］", tf.getBase_package()).replaceAll("［模块名］", modeName));
			templete.setDir_type(templete.getDir_type().replaceAll("［基础包名］", tf.getBase_package()).replaceAll("［模块名］", modeName));
			templete.setFile_name(templete.getFile_name().replaceAll("［基础包名］", tf.getBase_package()).replaceAll("［模块名］", modeName));
			templete.setFile_postfix(templete.getFile_postfix().replaceAll("［基础包名］", tf.getBase_package()).replaceAll("［模块名］", modeName));
			templete.setFile_prefix(templete.getFile_prefix().replaceAll("［基础包名］", tf.getBase_package()).replaceAll("［模块名］", modeName));
		}
	}

	public void createServcie(String projectName, boolean isFromDB)throws TemplateFileNotFoundException, Exception {
		create(projectName, isFromDB);
	}

	private String repalaceVariable(String str, TableInfo tableInfo) {
		String tmpStr = str;
		if(null == tmpStr)
			System.out.println("***********tempStr");
		if(null == tableInfo)
			System.out.println("************tableInfo"+tmpStr);
		if(null == tableInfo.getEntityBean())
			System.out.println("***********Entity"+tableInfo.getTableName());
		tmpStr = tmpStr.replaceAll("［实体名］", tableInfo.getEntityBean().getStandName());
		tmpStr = tmpStr.replaceAll("［小写实体名］", tableInfo.getEntityBean().getLowerAllCharName());
		tmpStr = tmpStr.replaceAll("［大写实体名］", tableInfo.getEntityBean().getUppderAllCharName());
		return tmpStr;
	}

	/**
	 * 运行创建代码的主进程
	 */
	private void create(String projectName, boolean isFromDB) throws Exception,TemplateFileNotFoundException {
		try {
			// 加载Velocity模板
			VelocityEngine velocity = new VelocityEngine();
			velocity.setProperty("file.resource.loader.path", templatePath);
			// Velocity.FILE_RESOURCE_LOADER_CACHE=templatePath;

			velocity.init();

			List<String> tableList = tableNames;
			List<TableInfo> tableInfoList = new ArrayList<TableInfo>();
			if (tableList == null || tableList.size() == 0) {
				throw new Exception("无法创建Javabean");
			}
			completeStatus = false;
			
			TempleteFile tf = TempleteConfig.getTempleteConfig().getConfig();
			int taskSize = tf.getTempletes().size();
			
			if (isFromDB) {
				TableUtil tableUtil = new TableUtil();

				if (isMainSub) {
					TableInfo tableInfoM = tableUtil.getTableInfo(mainTableName,getCodewizard());
					tableInfoList.add(tableInfoM);
					TableInfo tableInfoS = tableUtil.getTableInfo(subTableName,getCodewizard());
					tableInfoList.add(tableInfoS);
					
					tableNames.clear();
					tableNames.add(mainTableName);
					tableNames.add(subTableName);
					
					tableUtil.buildForeignKeyForTablesByFields(tableInfoM,tableInfoS, mainKey, subKey);
					
				} else if (isTree) {
					TableInfo tableInfo = null;
					for (int bk = 0; bk < tableList.size(); bk++) {
						tableInfo = tableUtil.getTableInfo((String) tableList.get(bk),getCodewizard());
						tableInfoList.add(tableInfo);
					}
					
					tableUtil.buildTreekeyForTablesByFields(tableInfo, treeRelationField, treeSubRelationField,treeDisplayField);
					
				} else {
					/**
					 * 获得所有的表模型及对应Bean模型
					 */
					for (int bk = 0; bk < tableList.size(); bk++) {
						TableInfo tableInfo = tableUtil.getTableInfo((String) tableList.get(bk),getCodewizard());
						tableInfoList.add(tableInfo);
					}
				}

			} else {
				BOP2TableInfoUtil bOP2TableInfoUtil = new BOP2TableInfoUtil();
				TableInfo tableInfo = bOP2TableInfoUtil.Bo2TableInfo(this.tableBusinessObject.getBoList(),this.tableBusinessObject.getTableName());
				tableInfoList.add(tableInfo);

			}
			
			int tableListSize = tableList.size();
			totalFileCount = tableListSize * taskSize;
			TableInfo singleTable = new TableInfo();
			if(tableListSize == 1)
				singleTable = tableInfoList.get(0);
			String mapperDir = null;
			String standardName = "";
			String parentPack ="";
			String basePackName = null;
			parentPack = modeName.lastIndexOf('.')!=-1 ? modeName.substring(modeName.lastIndexOf('.')+1):modeName;
			for (int k = 0; k < taskSize; k++) {
				if (threadStatus.equals(ThreadStatus.Stop)) { // 如果点击停止按钮了，则退出
					break;
				}
				for (; threadStatus.equals(ThreadStatus.Pause); 
						Thread.sleep(100L)) { // 点击暂停按钮了
				}

				for (int i = 0; i < tableListSize; i++) {
					if (threadStatus.equals(ThreadStatus.Stop)) { // 如果点击停止按钮了，则退出
						break;
					}
					for (; threadStatus.equals(ThreadStatus.Pause); 
							Thread.sleep(100L)) { // 点击暂停按钮了
					}

					VelocityContext context = new VelocityContext();
					TableInfo tableInfo = (TableInfo) tableInfoList.get(i);
					
					//System.out.println(tableInfoList.size());
					
					// add by zl 如果有父表，也就是自己是别人的子表 不做任何模版
					if (tableInfo.getParentTables().size() != 0) {
						//System.out.println("only one");
						Templete current_template = tf.getTempletes().get(k);
//						if(current_template.getFile_postfix().contains("Controller"))
//						continue;
					}

					String directoryName = "";
					String directoryPageName = "";
					String fileName = "";
					// 替换路径中的变量
					Templete current_templete = tf.getTempletes().get(k);
					directoryName = splitDirectory(current_templete.getPack_name(), false);
					directoryPageName = splitJspDirectory(current_templete.getPack_name(), false);
//					directoryName = repalaceVariable(directoryName, tableInfo); // .replaceAll("［实体名］",
					directoryName = repalaceVariable(directoryName, tableInfoList.get(0));
																				// tableInfo.getEntityBean().getLowerAllCharName());
					directoryPageName = repalaceVariable(directoryPageName,tableInfo); // directoryPageName.replaceAll("［实体名］",
										// tableInfo.getEntityBean().getLowerAllCharName());
					standardName = tableInfo.getEntityBean().getStandName();
					context.put("tableInfoList", tableInfoList);
					context.put("tableInfo", tableInfo);
					context.put("modeName", replaceSlash(modeName));
					String pack = repalaceVariable(tf.getBase_package(), tableInfo);
					context.put("pack",pack);
					context.put("company", tf.getCompany());
					context.put("author", tf.getAuthor());
					context.put("copyright", tf.getCopyright());
					context.put("date",new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
					context.put("name", tableInfo.getEntityBean().getStandName());
					if(current_templete.getFile_name().equals("dto.vm")){
						dtoPackName = dtoPackName == null ? repalaceVariable(current_templete.getPack_name(),tableInfo) : dtoPackName;
						context.put("packageName",dtoPackName.replace('/', '.'));
					}else
						context.put("packageName",repalaceVariable(current_templete.getPack_name(),tableInfo).replace('/', '.'));
//					context.put("packageName",dtoPackName);
					String preUrl = (String)context.get("packageName");
					if(preUrl.lastIndexOf('.')!=-1){
						preUrl = preUrl.substring(0,preUrl.lastIndexOf('.'));
						preUrl = preUrl.replace('.', '/') + "/";
					}
					context.put("preurl", preUrl);
					context.put("mappingPath", preUrl.substring(preUrl.indexOf("/")+1));
					context.put("subkey", TableUtil.buildStandParamName(subKey));
					context.put("parentpack", parentPack);
					context.put("webPath",repalaceVariable(current_templete.getPack_name(),tableInfo).replace(".", "/"));
					String path ="";
					if(pack == ""){
						path =modeName;
					}else{
						path = pack + "/"+modeName;
					}

					context.put("webPathJsp",webJsp);
					context.put("IdentityUtil", IdentityFactory.getInstance());

					// 添加附加属性值
					if (tf.getProperties() != null&& tf.getProperties().size() > 0) {
						List<Property> properties = tf.getProperties();
						for (int pk = 0; pk < properties.size(); pk++) {
							context.put(properties.get(pk).getKey(), properties.get(pk).getValue());
						}
					}

					Template template = null;
					template = velocity.getTemplate(current_templete.getFile_name(), "UTF-8");
					if (current_templete.getDir_type().trim().equals("code")) {
						fileName = (new StringBuilder(String.valueOf(directoryName))).toString();
						if(fileName.endsWith("dao"))
							mapperDir = fileName;
					} else if (current_templete.getDir_type().trim().equals("page")) {
						fileName = (new StringBuilder(String.valueOf(directoryPageName))).toString();
					}else if(current_templete.getDir_type().trim().equals("jsp")){
						if(current_templete.getFile_postfix().trim().endsWith("js"))
							fileName = (new StringBuilder(this.webPath).append(File.separator + parentPack + File.separator).append(File.separator+tableInfo.getEntityBean().getStandName().toLowerCase()).append(File.separator+"js")).toString();
//						fileName = (new StringBuilder(this.webPath).append(File.separator+tableInfo.getEntityBean().getStandName())).toString();
						else
							fileName = (new StringBuilder(this.webPath).append(File.separator + parentPack + File.separator).append(File.separator+tableInfo.getEntityBean().getStandName().toLowerCase())).toString();
					}
					
					if (tf.getDynafilename() != null && tf.getDynafilename()) { // 动态名称，系统自动计算
						/*List<String> filePath = new ArrayList<String>();
						filePath.add(fileName);
						String tempFile = repalaceVariable(current_templete.getFile_prefix(),tableInfo);
						if(!"".equals(tempFile)){
							filePath.add(repalaceVariable(current_templete.getFile_prefix(),tableInfo));
						}
						tempFile = repalaceVariable(current_templete.getFile_postfix(),tableInfo);
						if(!"".equals(tempFile)){
							filePath.add(repalaceVariable(current_templete.getFile_postfix(),tableInfo));
						}
						
						fileName = StringUtils.join(filePath, File.separator);*/
						fileName = fileName+ File.separator+ repalaceVariable(current_templete.getFile_prefix(),tableInfo) +repalaceVariable(current_templete.getFile_postfix(),tableInfo);
					} else { // 缺省用实体的名称
						fileName = (new StringBuilder(fileName))
								.append(File.separator)
								.append(current_templete.getFile_prefix())
								.append(tableInfo.getEntityBean()
										.getStandName())
								.append(tf.getTempletes().get(k)
										.getFile_postfix()).toString();
					}

					File directoryFile = null;
					if (current_templete.getDir_type().trim().equals("code")) {
						directoryFile = new File(directoryName);
					} else if (current_templete.getDir_type().trim().equals("page")) {
						directoryFile = new File(directoryPageName);
					}else if (current_templete.getDir_type().trim().equals("jsp")) {
						if(current_templete.getFile_postfix().trim().endsWith("js"))
							directoryFile = new File(this.webPath+File.separator+parentPack+File.separator+tableInfo.getEntityBean().getStandName().toLowerCase()+File.separator+"js"+File.separator+ repalaceVariable(current_templete.getFile_prefix(),tableInfo));	
						else
							directoryFile = new File(this.webPath+File.separator+parentPack+File.separator+tableInfo.getEntityBean().getStandName().toLowerCase()+File.separator+ repalaceVariable(current_templete.getFile_prefix(),tableInfo));
//						directoryFile = new File(this.webPath+File.separator+tableInfo.getEntityBean().getStandName()+File.separator+ repalaceVariable(current_templete.getFile_prefix(),tableInfo));
					}

					if (!directoryFile.exists()) {
						directoryFile.mkdirs();
					}
					String encoding = null;
					if (current_templete.getEncoding() == null) {
						encoding = "UTF-8";
					} else {
						encoding = current_templete.getEncoding();
					}
					//受限于原有设计的模板在外数据表在内的嵌套循环结构，对于主子表结构，此处需加如下条件判断,避免子表数据覆盖主表数据
//					if(!current_templete.getFile_postfix().contains("Controller") || i==0){//此判断保证不会为子表创建controller
					if(isWritable(current_templete.getFile_name()) || i==0){//此判断保证不会为子表创建controller
						if(i==0 && isMainSub && isWriteSubItem(current_templete.getFile_name())){
							fileName = formatFileName(fileName,tableInfo,current_templete);
						}
						Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), encoding));
						// 模板输出
						if (template != null) {
							template.merge(context, writer);
						}
						writer.flush();
						writer.close();		
					}

				}

			}

			createEcFile(projectName, this.srcPath);
			for(TableInfo tfo : tableInfoList){
				basePackName = basePackName == null ?  repalaceVariable(tf.getBase_package(), tfo):basePackName;
//				String pack = repalaceVariable(tf.getBase_package(), tfo);123
				File outFile  = new File(mapperDir + File.separator+tfo.getEntityBean().getStandName()+"Mapper.xml");
				createMapperFile(outFile,basePackName,modeName,tfo);
			}
			if (threadStatus.equals(ThreadStatus.Run)) {
				completeStatus = true;
			}
		} catch (Exception e) {
			AvicitLogger.openError(e.getMessage(), e);
			AvicitLogger.logError(e);
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}

	private void createEcFile(String projectName, String srcPath) {
		String moduleName = "";
		if (this.tableNames.size() > 0) {
			moduleName = tableNames.get(0);
			moduleName = moduleName.replace("_", "");
			moduleName = moduleName.toLowerCase();
		}
		// File ecFile = new File(srcPath + File.separator + this.modeName +
		// File.separator +moduleName + File.separator + File.separator +
		// ".ec");
		// File ecDir = new File(srcPath + File.separator + this.modeName +
		// File.separator + moduleName + File.separator );
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		final IProject project = workspace.getRoot().getProject(projectName);
		final IPath folderPath = new Path(File.separator + "src" + File.separator
				+ this.modeName + File.separator + moduleName + File.separator
				+ "META-INF" + File.separator);
		final IPath ecFilePath = new Path(File.separator + "src"
				+ File.separator + this.modeName + File.separator + moduleName
				+ File.separator + "META-INF" + File.separator + ".ec");
		final IFile ecFile = project.getFile(ecFilePath);
		// final File ecFile = new File(srcPath + File.separator + this.modeName
		// + File.separator +moduleName + File.separator +"META-INF" +
		// File.separator + ".ec");
		// File ecDir = new File(srcPath + File.separator + this.modeName +
		// File.separator + moduleName + File.separator +"META-INF");
		if (!ecFile.exists()) {
			try {
				final byte[] buf = createInitial(moduleName, "Comp").getBytes("UTF-8");

				Job job = new Job("creating Ecfile.") {
					@Override
					protected IStatus run(IProgressMonitor monitor) {
						IContainer container = project;
						String[] elements = ecFilePath.segments();
						String[] folders = folderPath.segments();

						ISchedulingRule rule = createEcFileRules(elements, container);

						Platform.getJobManager().beginRule(rule, monitor);

						try {
							for (int i = 0; i < folders.length; i++) {
								IPath segmentPath = new Path(folders[i]);
								container = container.getFolder(segmentPath);
								if (!container.exists()) {
									((IFolder) container).create(true, true, new NullProgressMonitor());
								}
							}

							IFile file = container.getFile(new Path(elements[elements.length - 1]));
							file.create(new ByteArrayInputStream(buf), false, new NullProgressMonitor());
						} catch (Exception e) {
							e.printStackTrace();
							return Status.CANCEL_STATUS;
						} finally {
							Platform.getJobManager().endRule(rule);
						}

						return Status.OK_STATUS;
					}
				};
				job.setUser(true);
				job.addJobChangeListener(new JobChangeAdapter() {
					public void done(IJobChangeEvent event) {
						if (event.getResult().isOK())
							System.out.println("EcFile created Successfully.");
						else
							System.out.println("EcFile creating failed.");
					}
				});
				job.schedule();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private ISchedulingRule createEcFileRules(String[] elements,
			IContainer container) {
		ISchedulingRule combinedRule = null;
		if (!(elements == null)) {
			IResourceRuleFactory ruleFactory = ResourcesPlugin.getWorkspace().getRuleFactory();

			for (int i = 0; i < elements.length - 1; i++) {
				IPath segmentPath = new Path(elements[i]);

					container = container.getFolder(segmentPath);
					ISchedulingRule rule = ruleFactory.createRule(container);
					combinedRule = MultiRule.combine(rule, combinedRule);;
				}

			IFile ecFile = container.getFile(new Path(elements[elements.length - 1]));
			ISchedulingRule rule = ruleFactory.createRule(ecFile);
			combinedRule = MultiRule.combine(rule, combinedRule);
		}

		return combinedRule;
	}

	private String createInitial(String name, String type)
			throws JavaModelException {
		StringBuffer buffer = new StringBuffer();
//		buffer.append("component.id=" + vo.id).append('\n');
		buffer.append("component.name=" + name).append('\n');
//		buffer.append("component.version=" + vo.version).append('\n');
//		buffer.append("component.type=" + vo.type).append('\n');
		buffer.append("component.modules=" + "input,zhanshi,controller,ywc,jcc,gzmb,designer,").append('\n');
//		buffer.append("component.service=" + vo.service).append('\n');
//		buffer.append("component.dao=" + vo.dao).append('\n');
//		buffer.append("component.auto=" + vo.auto).append('\n');
		if ("Comp".equals(type)) {
			buffer.append("component.iscomp=" + true).append('\n');
		} else {
			buffer.append("component.iscomp=" + false).append('\n');
		}
//		buffer.append("component.dtype=" + vo.dialogTitle).append('\n');
		return buffer.toString();
	}	
	/**
	 * 
	 * @param templateAttribute
	 * @return
	 */
	private boolean isWritable(String templateAttribute){
		boolean flag = false;
		if(!templateAttribute.equals("controller.vm")&&!templateAttribute.contains("mainaddjsp.vm")&&
		   !templateAttribute.equals("maindetailjsp.vm")&&!templateAttribute.contains("mainjs.vm")&&
		   !templateAttribute.equals("mainmanagejsp.vm")&&!templateAttribute.contains("maineditjsp.vm")&&
		   !templateAttribute.equals("mainservice.vm")&&!templateAttribute.equals("subservice.vm")&&
		   !templateAttribute.equals("subaddjsp.vm")&&!templateAttribute.equals("subeditjsp.vm")&&
		   !templateAttribute.equals("subjs.vm")&&!templateAttribute.equals("subdao.vm")&&
		   !templateAttribute.equals("rest.vm")){
			flag = true;
		}
		return flag;
	}
	
	private boolean isWriteSubItem(String templateAttribute){
		boolean flag = false;
		if(templateAttribute.equals("subaddjsp.vm") || templateAttribute.equals("subeditjsp.vm")||
		   templateAttribute.equals("subservice.vm")|| templateAttribute.equals("subjs.vm")||
		   templateAttribute.equals("subdao.vm")){
			flag = true;
		}
		return flag;
		
	}
	
	private String formatFileName(String fileName,TableInfo tableInfo,Templete template){
		RelationTable  rt = tableInfo.getChildTables().get(0);
//		String prefix = template.getFile_prefix();
		String postfix = template.getFile_postfix();
		String standName = rt.getChildTable().getEntityBean().getStandName();
		String newName = postfix.replace("［实体名］", standName);
		fileName = fileName.substring(0,fileName.lastIndexOf('\\'));
		fileName = fileName + File.separator + newName;
		return fileName;
	}

	/**
	 * 创建mybatis映射文件
	 * @param mapperFile
	 * @param pack
	 * @param modeName
	 * @param tableInfo
	 * @throws IOException 
	 */
	private void createMapperFile(File mapperFile,String pack,String modeName,TableInfo tableInfo) throws IOException{
		partPackName = partPackName == null ?  tableInfo.getEntityBean().getLowerAllCharName() : partPackName;
		StringBuilder sb = new StringBuilder();
//		String nameSpace = pack + this.modeName + "." + tableInfo.getEntityBean().getLowerAllCharName()+".dao."+tableInfo.getEntityBean().getStandName()+"Dao";
		String nameSpace = pack + replaceSlash(this.modeName) + "." + partPackName+".dao."+tableInfo.getEntityBean().getStandName()+"Dao";		
//		String prePackName = pack + this.modeName + "." + tableInfo.getEntityBean().getLowerAllCharName();//avicit.platform6.demo.demobusinesstrip
		String prePackName = pack + replaceSlash(this.modeName) + "." + partPackName;//avicit.platform6.demo.demobusinesstrip	
		String standardName = tableInfo.getEntityBean().getStandName();//DemoBusinessTrip
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
		sb.append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\" >\n");
		sb.append("<mapper namespace=\""+nameSpace+"\">\n\n");
//		partPackName = partPackName == null ?  tableInfo.getEntityBean().getLowerAllCharName() : partPackName;
//		sb.append("<resultMap id=\""+tableInfo.getEntityBean().getStandName()+"DTOMap\""+" type=\""+pack + this.modeName + "." + tableInfo.getEntityBean().getLowerAllCharName()+".dto."+tableInfo.getEntityBean().getStandName()+"DTO"+"\">\n");
		sb.append("<resultMap id=\""+tableInfo.getEntityBean().getStandName()+"DTOMap\""+" type=\""+pack + replaceSlash(this.modeName) + "." + partPackName+".dto."+tableInfo.getEntityBean().getStandName()+"DTO"+"\">\n");
		String rstMapId = standardName+"DTOMap";
		String rstMapType = prePackName+".dto."+standardName+"DTO";
		EntityParamInfo  paramInfo = null;
		String rstProperty = null;
		String rstColumn = null;
		String rstJdbcType = null;
		for(Iterator iter = tableInfo.getEntityBean().getParams().iterator();iter.hasNext();){
			paramInfo = (EntityParamInfo)iter.next();
			rstProperty = paramInfo.getStandName();
			rstColumn = paramInfo.getField().getFieldName();
			rstJdbcType = paramInfo.getField().getFieldTypeName();
			rstJdbcType = transferJdbcTypeForDate(rstJdbcType);
			sb.append("\t").append("\t");
			sb.append("<result property=\""+rstProperty+"\" " + "column=\""+rstColumn+"\" " +"jdbcType=\""+rstJdbcType+"\" />\n" );
		}
		//此处判断是流程表单，然后继续append操作 0331
		if(isSingleFlowTemplate){
			sb.append("\t").append("\t");
			sb.append("<result property=\""+"activityalias_"+"\" " + "column=\""+"activityalias_"+"\" " +"jdbcType=\""+"VARCHAR"+"\" />\n" );
			sb.append("\t").append("\t");
			sb.append("<result property=\""+"businessstate_"+"\" " + "column=\""+"businessstate_"+"\" " +"jdbcType=\""+"VARCHAR"+"\" />\n" );	
		}
		sb.append("</resultMap>\n");//构造<resultMap>
		if(tableInfo.getParentTables().size() > 0){//对于子表，需创建findDemoBusinessTripSubByPid片段
			String searchByPidContent = createSearchByPidContent(standardName, modeName, tableInfo);
			sb.append(searchByPidContent);
		}
		String searchByPageContent = this.createSearchByPageContent(standardName, modeName, tableInfo);
		sb.append(searchByPageContent);
		String searchListContent = this.createSearchContent(standardName, modeName, tableInfo);
		sb.append(searchListContent);
		String searchByIdContent = this.createSearchByKeyContent(standardName, modeName, tableInfo);
		sb.append(searchByIdContent);
		String insertOjectContent = this.createInsertObjectContent(standardName, modeName, tableInfo, prePackName);
		sb.append(insertOjectContent);
		String updateObjectContent = this.updateObjectContent(standardName, modeName, tableInfo, prePackName);
		sb.append(updateObjectContent);
		String updateAllContent = this.updateObjectAllContent(standardName, modeName, tableInfo, prePackName);
		sb.append(updateAllContent);
		String deleteContent = this.deleteObjectContent(standardName, modeName, tableInfo, prePackName);
		sb.append(deleteContent);
		sb.append("</mapper>");
		try {
			ByteArrayInputStream bs = new ByteArrayInputStream(sb.toString().getBytes("utf-8"));
			OutputStream os = new FileOutputStream(mapperFile);
			int readLen =0;
			byte[] buffer = new byte[2048];
			while((readLen=(bs.read(buffer)))!=-1){
				os.write(buffer,0,readLen);
			}
			bs.close();
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw e;//抛出异常，统一在create方法中处理异常信息，记录日志
		} catch (IOException e) {
			e.printStackTrace();
			throw e;//抛出异常，统一在create方法中处理异常信息，记录日志
		}
		
	}

	/**
	 * 分页查询配置片段
	 * @param standardName
	 * @param modeName
	 * @param tableInfo
	 * @return
	 */
	private String createSearchByPageContent(String standardName,String modeName,TableInfo tableInfo){
		StringBuilder sb = new StringBuilder();
		sb.append("<!-- 分页查询 "+tableInfo.getTableName()+" -->\n");
		String id = "search"+standardName+"ByPage";
		String parameterType = "java.util.Map";
		String resultMap = standardName+"DTOMap";
		sb.append("<select id=\""+id+"\" "+"parameterType=\""+parameterType+"\" "+"resultMap=\""+resultMap+"\">\n");
		EntityParamInfo  paramInfo = null;
		String lowerFieldName = "";
		sb.append("\t").append("select").append("\n");
		//此处判断是流程模板，继续append，注意对“,\n”的处理
		if(isSingleFlowTemplate){
			sb.append("\t").append("\t").append("v.activityalias_");
			formatSpace(sb,"activityalias_");
			sb.append(",\n");
			sb.append("\t").append("\t").append("v.businessstate_");
			formatSpace(sb,"businessstate_");
			sb.append(",\n");		
		}
		List<EntityParamInfo> paramInfoList = tableInfo.getEntityBean().getParams();
		for(int i =0; i<paramInfoList.size();i++){
			paramInfo = (EntityParamInfo)paramInfoList.get(i);
			lowerFieldName = paramInfo.getField().getLowerFildName();
			sb.append("\t").append("\t").append("t1."+lowerFieldName);
			formatSpace(sb,lowerFieldName);
			if(i == paramInfoList.size()-1)
				sb.append("\n");
			else
				sb.append(",\n");
		}
		//此处判断是流程模板，append左连接
		if(isSingleFlowTemplate){
			sb.append("\t").append("from "+tableInfo.getLowerTableName()+" 	t1 left join BPM_CLIENT_HIST_PROCINST_V v on t1.id = v.formid_\n");	
		}else
			sb.append("\t").append("from "+tableInfo.getLowerTableName()+" 	t1\n");
		sb.append("\t").append("\t").append("<where>\n");
		String upperColumnName = "";
		String firstLowerName = "";
		for(Iterator iter = tableInfo.getEntityBean().getParams().iterator();iter.hasNext();){
			paramInfo = (EntityParamInfo)iter.next();
			upperColumnName = paramInfo.getField().getFieldName();
			firstLowerName = paramInfo.getStandName();
			sb.append("\t").append("\t").append("\t");
			sb.append("<if test=\""+firstLowerName+" != null and "+firstLowerName+" != ''\">\n");
			sb.append("\t").append("\t").append("\t").append("\t");
			sb.append("and t1.").append(upperColumnName).append(" like '%'||").append("#{"+firstLowerName+"}").append("||'%'\n");
			sb.append("\t").append("\t").append("\t");
			sb.append("</if>\n");
		}
		//此处判断是流程模板，继续append
		if(isSingleFlowTemplate){
			appendForSingleFlow(sb);
		}
		sb.append("\t").append("\t").append("</where>\n");
		sb.append("\t").append("\t").append("\t").append("order by t1.creation_date desc\n");//create_date作为规范
		sb.append("</select>\n");
		return sb.toString();
	}
	
	
	private void appendForSingleFlow(StringBuilder sb){
		sb.append("\t").append("\t").append("\t");
		sb.append("<if test=\""+"bpmType == 'my'"+"\">\n");
		sb.append("\t").append("\t").append("\t").append("\t");
		sb.append("and exists (select 1 from BPM_CLIENT_HIST_TASK_V os where os.task_b_id_ = t1.id and os.assignee_ = #{currUserId})\n");
		sb.append("\t").append("\t").append("\t");
		sb.append("</if>\n");	
		
		sb.append("\t").append("\t").append("\t");
		sb.append("<if test=\""+"bpmState "+"!= null and "+"bpmState"+" != 'all'\">\n");	
		sb.append("\t").append("\t").append("\t").append("\t");
		sb.append("and v.").append("businessstate_").append(" = #{bpmState}\n");	
		sb.append("\t").append("\t").append("\t");
		sb.append("</if>\n");		
	}
	
	/**
	 * 查询列表配置片段
	 * @param standardName
	 * @param modeName
	 * @param tableInfo
	 * @return
	 */
	private String createSearchContent(String standardName,String modeName,TableInfo tableInfo){
		StringBuilder sb = new StringBuilder();
		sb.append("<!-- 查询列表 "+tableInfo.getTableName()+" -->\n");
		String id = "search"+standardName;
		String parameterType = "java.util.Map";
		String resultMap = standardName+"DTOMap";
		sb.append("<select id=\""+id+"\" "+"parameterType=\""+parameterType+"\" "+"resultMap=\""+resultMap+"\">\n");
		EntityParamInfo  paramInfo = null;
		String lowerFieldName = "";
		sb.append("\t").append("select").append("\n");
		List<EntityParamInfo> paramInfoList = tableInfo.getEntityBean().getParams();
		for(int i =0; i<paramInfoList.size();i++){
			paramInfo = (EntityParamInfo)paramInfoList.get(i);
			lowerFieldName = paramInfo.getField().getLowerFildName();
			sb.append("\t").append("\t").append("t1."+lowerFieldName);
			formatSpace(sb,lowerFieldName);
			if(i == paramInfoList.size()-1)
				sb.append("\n");
			else
				sb.append(",\n");
		}
		sb.append("\t").append("from "+tableInfo.getLowerTableName()+" 	t1\n");
		sb.append("\t").append("\t").append("<where>\n");
		String upperColumnName = "";
		String firstLowerName = "";
		for(Iterator iter = tableInfo.getEntityBean().getParams().iterator();iter.hasNext();){
			paramInfo = (EntityParamInfo)iter.next();
			upperColumnName = paramInfo.getField().getFieldName();
			firstLowerName = paramInfo.getStandName();
			sb.append("\t").append("\t").append("\t");
			sb.append("<if test=\""+firstLowerName+" != null and "+firstLowerName+" != ''\">\n");
			sb.append("\t").append("\t").append("\t").append("\t");
			sb.append("and t1.").append(upperColumnName).append(" =#{").append(firstLowerName).append("}\n");
			sb.append("\t").append("\t").append("\t");
			sb.append("</if>\n");
		}
		sb.append("\t").append("\t").append("</where>\n");
		sb.append("</select>\n");
		return sb.toString();
	}
	/**
	 * 主键查询配置片段
	 * @param standardName
	 * @param modeName
	 * @param tableInfo
	 * @return
	 */
	private String createSearchByKeyContent(String standardName,String modeName,TableInfo tableInfo){

		StringBuilder sb = new StringBuilder();
		sb.append("<!-- 主键查询对象 "+tableInfo.getTableName()+" -->\n");
		String id = "find"+standardName+"ById";
		String parameterType = "java.util.Map";
		String resultMap = standardName+"DTOMap";
		sb.append("<select id=\""+id+"\" "+"parameterType=\""+parameterType+"\" "+"resultMap=\""+resultMap+"\">\n");
		EntityParamInfo  paramInfo = null;
		String lowerFieldName = "";
		sb.append("\t").append("select").append("\n");
		List<EntityParamInfo> paramInfoList = tableInfo.getEntityBean().getParams();
		String primaryKey = "";
		for(int i =0; i<paramInfoList.size();i++){
			paramInfo = (EntityParamInfo)paramInfoList.get(i);
			lowerFieldName = paramInfo.getField().getLowerFildName();
			if(paramInfo.getField().getFieldName().equals(tableInfo.getPrimaryField().getFieldName())){
				primaryKey = lowerFieldName;
			}
			sb.append("\t").append("\t").append("t1."+lowerFieldName);
			formatSpace(sb,lowerFieldName);
			if(i == paramInfoList.size()-1)
				sb.append("\n");
			else
				sb.append(",\n");
		}
		sb.append("\t").append("from "+tableInfo.getLowerTableName()+" 	t1\n");
		sb.append("\t").append("\t").append("where ").append("t1."+primaryKey).append(" = #{").append(primaryKey).append("}\n");
		sb.append("</select>\n");
		return sb.toString();
	}
	/**
	 * pid查询配置片段
	 * @param standardName
	 * @param modeName
	 * @param tableInfo
	 * @return
	 */
	private String createSearchByPidContent(String standardName,String modeName,TableInfo tableInfo){

		StringBuilder sb = new StringBuilder();
		sb.append("<!-- 根据pid查询对象 "+tableInfo.getTableName()+" -->\n");
		String id = "find"+standardName+"ByPid";
		String parameterType = "java.util.Map";
		String resultMap = standardName+"DTOMap";
		sb.append("<select id=\""+id+"\" "+"parameterType=\""+parameterType+"\" "+"resultMap=\""+resultMap+"\">\n");
		EntityParamInfo  paramInfo = null;
		String lowerFieldName = "";
		sb.append("\t").append("select").append("\n");
		List<EntityParamInfo> paramInfoList = tableInfo.getEntityBean().getParams();
		String primaryKey = "";
		String pKey = subKey.toLowerCase();
		for(int i =0; i<paramInfoList.size();i++){
			paramInfo = (EntityParamInfo)paramInfoList.get(i);
			lowerFieldName = paramInfo.getField().getLowerFildName();
			if(paramInfo.getField().getFieldName().equals(tableInfo.getPrimaryField().getFieldName())){
				primaryKey = lowerFieldName;
			}
			sb.append("\t").append("\t").append("t1."+lowerFieldName);
			formatSpace(sb,lowerFieldName);
			if(i == paramInfoList.size()-1)
				sb.append("\n");
			else
				sb.append(",\n");
		}
		sb.append("\t").append("from "+tableInfo.getLowerTableName()+" 	t1\n");
		sb.append("\t").append("\t").append("where ").append("t1."+pKey).append(" = #{").append("pid").append("}\n");
		sb.append("</select>\n");
		return sb.toString();
	}
	
	/**
	 * 新增对象配置片段
	 * @param standardName
	 * @param modeName
	 * @param tableInfo
	 * @return
	 */
	private String createInsertObjectContent(String standardName,String modeName,TableInfo tableInfo,String pack){
		partPackName = partPackName == null ?  tableInfo.getEntityBean().getLowerAllCharName() : partPackName;
		StringBuilder sb = new StringBuilder();
		sb.append("<!-- 新增对象 "+tableInfo.getTableName()+" -->\n");
		String id = "insert"+standardName;
		String parameterType = pack + ".dto."+tableInfo.getEntityBean().getStandName()+"DTO";
		sb.append("<insert id=\""+id+"\" "+"parameterType=\""+parameterType+"\">\n");
		sb.append("\t").append("insert into ").append(tableInfo.getLowerTableName()).append("\n");
		sb.append("\t").append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">\n");
		List<EntityParamInfo> paramInfoList = tableInfo.getEntityBean().getParams();
		EntityParamInfo  paramInfo = null;
		String lowerFieldName = "";
		String firstLowerName = "";
		for(int i =0; i<paramInfoList.size();i++){
			paramInfo = (EntityParamInfo)paramInfoList.get(i);
			firstLowerName = paramInfo.getStandName();
			lowerFieldName = paramInfo.getField().getLowerFildName();
			sb.append("\t").append("\t").append("\t");
			sb.append("<if test=\""+firstLowerName+" != null\"").append(">\n");
			sb.append("\t").append("\t").append("\t").append("\t");	
			sb.append(lowerFieldName).append(",").append("\n");
			sb.append("\t").append("\t").append("\t").append("</if>\n");
		}
		sb.append("\t").append("</trim>\n");
		sb.append("\t").append("<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\">\n");
		String rstJdbcType = "";
		for(Iterator iter = tableInfo.getEntityBean().getParams().iterator();iter.hasNext();){
			paramInfo = (EntityParamInfo)iter.next();
			firstLowerName = paramInfo.getStandName();
			rstJdbcType = paramInfo.getField().getFieldTypeName();
			rstJdbcType = transferJdbcType(rstJdbcType);
			sb.append("\t").append("\t").append("\t");
			sb.append("<if test=\""+firstLowerName+" != null\"").append(">\n");
			if(firstLowerName.equals("creationDate") || firstLowerName.equals("lastUpdateDate")){
				sb.append("\t").append("\t").append("\t").append("\t").append("#{").append(firstLowerName).append("},\n");	
			}else
				sb.append("\t").append("\t").append("\t").append("\t").append("#{").append(firstLowerName).append(",").append("jdbcType=").append(rstJdbcType).append("},\n");
			sb.append("\t").append("\t").append("\t").append("</if>\n");
		}
		sb.append("\t").append("</trim>\n");
		sb.append("</insert>\n");
		return sb.toString();
	}
	
	/**
	 * 更新对象(Sensitive)配置片段
	 * @param standardName
	 * @param modeName
	 * @param tableInfo
	 * @param pack
	 * @return
	 */
	private String updateObjectContent(String standardName,String modeName,TableInfo tableInfo,String pack){
		StringBuilder sb = new StringBuilder();
		sb.append("<!-- 更新对象 "+tableInfo.getTableName()+" -->\n");
		String id = "update"+standardName+"Sensitive";
		String parameterType = pack +".dto."+tableInfo.getEntityBean().getStandName()+"DTO";
		sb.append("<update id=\""+id+"\" ").append("parameterType=\"").append(parameterType).append("\">\n");
		sb.append("\t").append("update ").append(tableInfo.getLowerTableName()).append(" t1\n");
		sb.append("\t").append("<set>\n");

		List<EntityParamInfo> paramInfoList = tableInfo.getEntityBean().getParams();
		EntityParamInfo  paramInfo = null;
		String lowerFieldName = "";
		String firstLowerName = "";
		String paramJdbcType = "";
		String primaryKey = "";
		for(Iterator iter = tableInfo.getEntityBean().getParams().iterator();iter.hasNext();){
			paramInfo = (EntityParamInfo)iter.next();
			lowerFieldName = paramInfo.getField().getLowerFildName();
			if(paramInfo.getField().getFieldName().equals(tableInfo.getPrimaryField().getFieldName()))
				primaryKey = lowerFieldName;
			paramJdbcType = paramInfo.getField().getFieldTypeName();
			paramJdbcType = transferJdbcType(paramJdbcType);
			firstLowerName = paramInfo.getStandName();
			sb.append("\t").append("\t").append("\t");
			sb.append("<if test=\""+firstLowerName+" != null\"").append(">\n");
			sb.append("\t").append("\t").append("\t").append("\t");	
			sb.append("t1.").append(lowerFieldName);
			this.formatSpace(sb, lowerFieldName);
			if(firstLowerName.equals("creationDate") || firstLowerName.equals("lastUpdateDate")){
				sb.append("=#{").append(firstLowerName).append("},\n");	
			}else if(firstLowerName.equals("version")){
				sb.append("=t1.version+1,\n");	
			}else{
				sb.append("=#{").append(firstLowerName).append(",").append("jdbcType=").append(paramJdbcType).append("},\n");
			}
			sb.append("\t").append("\t").append("\t").append("</if>\n");
		}
		sb.append("\t").append("</set>\n");
		sb.append("\t").append("\t").append("where t1.").append(primaryKey).append(" = #{").append(primaryKey).append("}")
		.append(" and t1.version =#{version}\n");		
		sb.append("</update>\n");
		return sb.toString();
			
	}	
	
	/**
	 * 更新对象(All)配置片段
	 * @param standardName
	 * @param modeName
	 * @param tableInfo
	 * @param pack
	 * @return
	 */
	private String updateObjectAllContent(String standardName,String modeName,TableInfo tableInfo,String pack){
		StringBuilder sb = new StringBuilder();
		sb.append("<!-- 更新对象 "+tableInfo.getTableName()+" -->\n");
		String id = "update"+standardName+"All";
		String parameterType = pack + ".dto."+tableInfo.getEntityBean().getStandName()+"DTO";
		sb.append("<update id=\""+id+"\" ").append("parameterType=\"").append(parameterType).append("\">\n");
		sb.append("\t").append("update ").append(tableInfo.getLowerTableName()).append(" t1\n");
		sb.append("\t").append("\t").append("set\n");

		List<EntityParamInfo> paramInfoList = tableInfo.getEntityBean().getParams();
		EntityParamInfo  paramInfo = null;
		String lowerFieldName = "";
		String firstLowerName = "";
		String paramJdbcType = "";
		String primaryKey = "";
		for(Iterator iter = tableInfo.getEntityBean().getParams().iterator();iter.hasNext();){
			paramInfo = (EntityParamInfo)iter.next();
			lowerFieldName = paramInfo.getField().getLowerFildName();
			if(paramInfo.getField().getFieldName().equals(tableInfo.getPrimaryField().getFieldName())){
				primaryKey = lowerFieldName;
			}
			paramJdbcType = paramInfo.getField().getFieldTypeName();
			paramJdbcType = transferJdbcType(paramJdbcType);
			firstLowerName = paramInfo.getStandName();
			sb.append("\t").append("\t").append("\t");
			sb.append("t1.").append(lowerFieldName);
			this.formatSpace(sb, lowerFieldName);
			if(firstLowerName.equals("creationDate") || firstLowerName.equals("lastUpdateDate")){
				sb.append("=#{dto.").append(firstLowerName).append("},\n");	
			}else if(firstLowerName.equals("version")){
				sb.append("=t1.version+1,\n");	
			}else{
				sb.append("=#{dto.").append(firstLowerName).append(",").append("jdbcType=").append(paramJdbcType).append("},\n");
			}
		}
		sb.append("\t").append("where t1.").append(primaryKey).append(" = #{dto.").append(primaryKey).append("}")
		.append(" and t1.version =#{version}\n");		
		sb.append("</update>\n");
		return sb.toString();
	}
	
	/**
	 * 创建删除对象配置片段
	 * @param standardName
	 * @param modeName
	 * @param tableInfo
	 * @param pack
	 */
	private String deleteObjectContent(String standardName,String modeName,TableInfo tableInfo,String pack){
		StringBuilder sb = new StringBuilder();
		sb.append("<!-- 主键删除 "+tableInfo.getTableName()+" -->\n");
		String id = "delete"+standardName+"ById";
		String parameterType = "java.lang.String";
		String primaryKey = "";
		String lowerFieldName = "";
		String firstLowerName = "";
		EntityParamInfo  paramInfo = null;
		for(Iterator iter = tableInfo.getEntityBean().getParams().iterator();iter.hasNext();){
			paramInfo = (EntityParamInfo)iter.next();
			lowerFieldName = paramInfo.getField().getLowerFildName();
			if(paramInfo.getField().getFieldName().equals(tableInfo.getPrimaryField().getFieldName())){
				primaryKey = lowerFieldName;
			}
		}
		sb.append("<delete id=\"" + id +"\" "+"parameterType=\"" + parameterType +"\">\n");
		sb.append("\t").append("\t").append("delete from ").append(tableInfo.getLowerTableName()).append(" t1 where t1.").append(primaryKey).append(" = #{").append(primaryKey).append("}\n");
		sb.append("</delete>\n");
		return sb.toString();
	}
	
	/**
	 * 格式化空格字符串
	 * @param stringBuilder
	 * @param fieldName
	 */
	private void formatSpace(StringBuilder stringBuilder,String fieldName){
		int length = 31;
		int fieldLength = fieldName.length();
		int spaceNum = length - fieldLength;
		for(int i=0;i<spaceNum;i++){
			stringBuilder.append(" ");
		}
		
	}
	
	/**
	 * 将varchar2转为varchar
	 * 将number转为decimal
	 * @param typeName
	 * @return
	 */
	private String transferJdbcType(String typeName){
		String myBatisTypeName = typeName;
		if(typeName.equals(VARCHAR2_ORCL))
			myBatisTypeName = VARCHAR;
		else if(typeName.equals(NUMBER_ORCL))
			myBatisTypeName = DECIMAL;
		return myBatisTypeName;
		
	}
	
	private String transferJdbcTypeForDate(String typeName){
		String myBatisTypeName = typeName;
		if(typeName.equals(VARCHAR2_ORCL))
			myBatisTypeName = VARCHAR;
		else if(typeName.equals(NUMBER_ORCL))
			myBatisTypeName = DECIMAL;
		else if(typeName.equals(DATE_ORCL))
			myBatisTypeName = TIME_ORCL;
		return myBatisTypeName;
		
	}	
	public String splitDirectory(String packageName, boolean flag) {
		TempleteFile tf = TempleteConfig.getTempleteConfig().getConfig();
		String splitDirectory[] = packageName.split("\\.");
		String directoryName = "";
		if (splitDirectory.length == 0) {
			if (flag) {
				directoryName = (new StringBuilder(tf.getBase_src()))
						.append(File.separator).append(packageName).toString();
			} else {
				directoryName = (new StringBuilder(String.valueOf(srcPath))).append(packageName).toString();
			}
		} else {
			if (flag) {
				directoryName = (new StringBuilder(tf.getBase_src())).append(
						File.separator).toString();
			} else {
				directoryName = srcPath;
			}
			for (int j = 0; j < splitDirectory.length; j++) {
				if (j == splitDirectory.length - 1) {
					directoryName = (new StringBuilder(String.valueOf(directoryName))).append(splitDirectory[j]).toString();
					break;
				}
				directoryName = (new StringBuilder(String.valueOf(directoryName))).append(splitDirectory[j]).append(File.separator).toString();
			}
		}
		return directoryName;
	}

	public String splitJspDirectory(String packageName, boolean flag) {
		TempleteFile tf = TempleteConfig.getTempleteConfig().getConfig();
		String splitDirectory[] = packageName.split("\\.");
		String directoryName = "";
		if (splitDirectory.length == 0) {
			if (flag) {
				directoryName = (new StringBuilder(tf.getBase_page()))
						.append(File.separator).append(modeName)
						.append(File.separator).append(packageName).toString();
			} else {
				directoryName = (new StringBuilder(String.valueOf(pagePath)))
						.append(packageName).toString();
			}
		} else {
			if (flag) {
				directoryName = (new StringBuilder(tf.getBase_page()))
						.append(File.separator).append(modeName)
						.append(File.separator).toString();
			} else {
				directoryName = pagePath;
			}
			for (int j = 0; j < splitDirectory.length; j++) {
				if (j == splitDirectory.length - 1) {
					directoryName = (new StringBuilder(
							String.valueOf(directoryName))).append(
							splitDirectory[j]).toString();
					break;
				}
				directoryName = (new StringBuilder(
						String.valueOf(directoryName)))
						.append(splitDirectory[j]).append(File.separator)
						.toString();
			}

		}
		return directoryName;
	}

	public void deleteCreatedFiles() {
		createdFilesCount = countCreatedFiles();
		deleteAllFiles();
	}
	
	private String replaceSlash(String todoString){
		String rstString = null;
		if(null!=todoString)
			rstString = todoString.replace('/', '.');
		return rstString;
	}

	private int countCreatedFiles() {
		int tempFilesCount = 0;
		int taskSize = taskList.size() - 1;
		for (int k = 0; k < taskSize; k++) {
			String directoryName = "";
			directoryName = splitDirectory(((String[]) taskList.get(k))[1],
					false);
			File directoryFile = new File(directoryName);
			if (directoryFile.exists()) {
				File files[] = directoryFile.listFiles();
				if (files != null) {
					tempFilesCount += files.length;
				}
			}
		}

		return tempFilesCount;
	}

	private void deleteAllFiles() {
		int taskSize = taskList.size() - 1;
		int deletedCount = 0;
		// out(outPanel, "开始删除文件.................\n");
		for (int k = 0; k < taskSize; k++) {
			String directoryName = "";
			directoryName = splitDirectory(((String[]) taskList.get(k))[1],
					false);
			File directoryFile = new File(directoryName);
			if (directoryFile.exists()) {
				File files[] = directoryFile.listFiles();
				if (files != null) {
					for (int i = 0; i < files.length; i++) {
						deletedCount++;
						// out(outPanel, (new
						// StringBuilder("删除文件:......")).append(files[i].getPath()).append("\n").toString());
						files[i].delete();
						// bar.setValue((1000 * deletedCount) /
						// createdFilesCount);
					}
				}
				deletedCount++;
				// out(outPanel, (new
				// StringBuilder("删除目录:......")).append(directoryFile.getPath()).append("\n").toString());
				directoryFile.delete();
				// bar.setValue((1000 * deletedCount) / createdFilesCount);
			}
		}

		String directoryName = splitDirectory((new StringBuilder(TempleteConfig.getTempleteConfig().getConfig().getBase_package())).append(modeName).toString(), false);
		File directoryFile = new File(directoryName);
		if (directoryFile.exists()) {
			File files[] = directoryFile.listFiles();
			if (files == null) {
				directoryFile.delete();
			}
		}
	}

	public void checkTemplateFiles() throws TemplateFileNotFoundException {
		File file = new File(templatePath);
		if (!file.exists()) {
			throw new TemplateFileNotFoundException("模板所在目录无法找到", templatePath);
		}
		for (Iterator iterator = templateList.iterator(); iterator.hasNext();) {
			String filename = (String) iterator.next();
			File tempFile = new File((new StringBuilder(String.valueOf(templatePath))).append(File.separator).append(filename).toString());
			if (!tempFile.exists()) {
				throw new TemplateFileNotFoundException("模板文件未找到", filename);
			}
		}

	}


	public void setModeName(String modeName) {
		this.modeName = modeName;
	}

	public String getThreadStatus() {
		return threadStatus;
	}

	public void setThreadStatus(String threadStatus) {
		this.threadStatus = threadStatus;
	}

	public boolean isCompleteStatus() {
		return completeStatus;
	}

	public CodeGenerationWizard getCodewizard() {
		return codewizard;
	}

	public void setCodewizard(CodeGenerationWizard codewizard) {
		this.codewizard = codewizard;
	}

	public boolean isTree() {
		return isTree;
	}

	public void setTree(boolean isTree) {
		this.isTree = isTree;
	}

	public String getTreeRelationField() {
		return treeRelationField;
	}

	public void setTreeRelationField(String treeRelationField) {
		this.treeRelationField = treeRelationField;
	}

	public String getTreeSubRelationField() {
		return treeSubRelationField;
	}

	public void setTreeSubRelationField(String treeSubRelationField) {
		this.treeSubRelationField = treeSubRelationField;
	}

	public String getTreeDisplayField() {
		return treeDisplayField;
	}

	public void setTreeDisplayField(String treeDisplayField) {
		this.treeDisplayField = treeDisplayField;
	}
	
}
