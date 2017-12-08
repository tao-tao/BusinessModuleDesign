package avicit.platform6.tools.codegeneration.wizard;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import sun.org.mozilla.javascript.internal.FunctionNode;

import avicit.platform6.tools.codegeneration.core.entity.TableBusinessObject;


/**
 * 
 * 作者：dingrc 
 * 邮箱：dingrc@avicit.com
 * 创建时间：2012-12-10 上午09:12:34
 *
 * 类说明：代码生成向导
 * 修改记录：
 */
public class CodeGenerationWizard extends Wizard implements INewWizard {
	public DoCreatPage getDoCreatPage() {
		return doCreatPage;
	}

	public void setDoCreatPage(DoCreatPage doCreatPage) {
		this.doCreatPage = doCreatPage;
	}
	
	public final String strpro ="/avicit/platform6/tools/codegeneration/core/dbconfig.properties"; 
	private IFolder folder;
	IWorkbench workbench;
	IStructuredSelection selection;
	ProjectSelectPage pageProjectSelect;
	NewUnitCreationWizardPage selectFiledPage;
	public TempleteSelectPage templeteSelectPage;
	BusinessObjectConfigPage businessObjectConfigPage;
	DoCreatPage doCreatPage;
	public GridFormPage gridpage;
	IStructuredSelection selectionForProject;
	private String parentPackPath = null;
	
	public String getParentPackPath() {
		return parentPackPath;
	}

	public void setParentPackPath(String parentPackPath) {
		this.parentPackPath = parentPackPath;
	}

	public IStructuredSelection getSelectionForProject() {
		return selectionForProject;
	}

	public void setSelectionForProject(IStructuredSelection selectionForProject) {
		this.selectionForProject = selectionForProject;
	}

	boolean codeGenerationWithHbm =false;
	
	public boolean isCodeGenerationWithHbm() {
		return codeGenerationWithHbm;
	}

	public void setCodeGenerationWithHbm(boolean codeGenerationWithHbm) {
		this.codeGenerationWithHbm = codeGenerationWithHbm;
	}

	boolean flag = false;
	//IPath path;
	String ownerName;
	String bizObject;
	private String groupName; 
	
	private IProject project;
	private String jspPath;
	
	/**
	 * 是否为maven工程
	 */
	private boolean isManve =false;
	
	
	public boolean isManve() {
		return isManve;
	}

	void setManve(boolean isManve) {
		this.isManve = isManve;
	}

	boolean getManve() {
		return this.isManve;
	}

	private List<String> mainTableNames = null;
	private List<String> subTableNames = null;
	
	/**
	 * 主表关联字段
	 */
	private String mainRelationField;
	/**
	 * 子表关联字段
	 */
	private String subRelationField;
	
	
	/**
	 * 子表关联字段
	 */
	private String treeRelationField;
	
	/**
	 * 子表关联字段
	 */
	private String treeSubRelationField;
	
	private String treeDisplayField;
	
	
	public String getTreeDisplayField() {
		return treeDisplayField;
	}

	public void setTreeDisplayField(String treeDisplayField) {
		this.treeDisplayField = treeDisplayField;
	}

	public String getTreeRelationField() {
		return treeRelationField;
	}

	public void setTreeRelationField(String treeRelationField) {
		this.treeRelationField = treeRelationField;
	}

	public String getMainRelationField() {
		return mainRelationField;
	}

	public void setMainRelationField(String mainRelationField) {
		this.mainRelationField = mainRelationField;
	}

	public String getSubRelationField() {
		return subRelationField;
	}

	public void setSubRelationField(String subRelationField) {
		this.subRelationField = subRelationField;
	}

	public List<String> getSubTableNames() {
		return subTableNames;
	}

	public void setSubTableNames(List<String> subTableNames) {
		this.subTableNames = subTableNames;
	}

	private TableBusinessObject tableBusinessObject=new TableBusinessObject();
	
	private List<String> allTable = null;
	
	
	/**
	 * 获得载入的所有表名称
	 * @return
	 */
	public List<String> getAllTable() {
		return allTable;
	}
	/**
	 * 设置载入数据库中所有表名称
	 * @param allTable
	 */
	public void setAllTable(List<String> allTable) {
		this.allTable = allTable;
	}

	public List<String> getMainTableNames() {
		return mainTableNames;
	}
	public void setMainTableNames(List<String> mainTableNames) {
		this.mainTableNames = mainTableNames;
	}

	public TableBusinessObject getTableBusinessObject() {
		return tableBusinessObject;
	}

	public void setTableBusinessObject(TableBusinessObject tableBusinessObject) {
		this.tableBusinessObject = tableBusinessObject;
	}



	private Properties props = new Properties();
	
	private File file ;

	public File getFile(){
		return file;
	}
	
	private String templetPath ;
	
	public String getTempletPath() {
		return templetPath;
	}

	public void setTempletPath(String templetPath) {
		this.templetPath = templetPath;
	}

	private String createPackage;
	
	public String getCreatePackage() {
		return createPackage;
	}

	public void setCreatePackage(String createPackage) {
		this.createPackage = createPackage;
	}

	public CodeGenerationWizard() {
		super();
		
		mainTableNames=new ArrayList<String>();
		subTableNames=new ArrayList<String>();
		URL root = this.getClass().getResource("/");
		
		try {
			root = FileLocator.toFileURL(root);
		} catch (IOException e) {
			e.printStackTrace();
		}
		file = new File((root.toString()+this.strpro).substring(6));
		
	}

	/**辅助建模设计器插件 */
	public CodeGenerationWizard(boolean flag, IPath path, String tableName, String ownerName) {
		super();
		this.flag = flag;
		//this.path = path;
		mainTableNames=new ArrayList<String>();
		subTableNames=new ArrayList<String>();
		mainTableNames.add(tableName);
		this.ownerName = ownerName;
		
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.workbench = workbench;
		this.selection = selection;
		
		
		/*
		IPreferenceNode[] dd = workbench.getPreferenceManager().getRootSubNodes();
		IPreferenceNode ipn = workbench.getPreferenceManager().find("avicit.platform6.tools.bpm.designer.common");
		
		System.out.println(ipn.getSubNodes());
		 PreferenceNode  pn = ( PreferenceNode )ipn;
		 
		 System.out
				.println(workbench.getPreferenceStore().getString("dd"));
		System.out
				.println(workbench.getPreferenceStore().getString("$DATABASE_TYPE"));*/
		this.selection = selection;
		Object obj = this.selection.getFirstElement();
		//this.groupName = selection.toString().startsWith("[")?selection.toString().replace("[", "").replace("]", ""):"功能模块集";
		this.groupName = this.parentPackPath;
		System.out.println("===================XXXXXXXXXXXXXXXX"+groupName);
//		this.doCreatPage.setSubSystemName(groupName);
//		node = (AbstractFolderNode) selection.getFirstElement();		
//		this.doCreatPage.setModuleName(moduleName);
		
		workbench.getPreferenceManager().getRootSubNodes();
		setWindowTitle("代码生成器");
		setNeedsProgressMonitor(true);
	}

	@Override
	public void addPages() {
		super.addPages();
		//工程选择页
		pageProjectSelect = new ProjectSelectPage("proSelectPage", this.selectionForProject);
		pageProjectSelect.setWizard(this);
		addPage(pageProjectSelect);
//		//业务对象页
//		businessObjectConfigPage = new BusinessObjectConfigPage("businessObjectConfigPage",selection);
//		addPage(businessObjectConfigPage);
//		//模版选择页
//		templeteSelectPage=new TempleteSelectPage("TempleteSelectPage");
//		addPage(templeteSelectPage);
		gridpage = new GridFormPage();
		addPage(gridpage);
		//代码生成页
		doCreatPage = new DoCreatPage("doCreatPage");
		doCreatPage.setSubSystemName(groupName);
		addPage(doCreatPage);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		return true;
	}
	
	@Override
	public boolean canFinish() {
		
		if (pageProjectSelect.isPageComplete()
//			&&	templeteSelectPage.isPageComplete()
//			&&	businessObjectConfigPage.isPageComplete()
			&&	doCreatPage.isPageComplete()
				) {
			return true;
		}else{
			return false;
		}
	}
	public String getBizObject() {
		return bizObject;
	}

	public void setBizObject(String bizPackage) {
		this.bizObject = bizPackage;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	/**
	 * @return the ownerName
	 */
	public String getOwnerName() {
		return ownerName;
	}

	/**
	 * @param ownerName the ownerName to set
	 */
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public Properties getProps() {
		return props;
	}

	public IProject getProject() {
		return project;
	}
	
	public String getJspPath(){
		return jspPath;
	}
	public void setJspPath(String jspPath){
		this.jspPath = jspPath;
	}

	public void setProject(IProject project) {
		this.project = project;
	}

	public void alertOkMsg(){
		MessageBox mb=new MessageBox(this.getShell(),SWT.OK);
    	mb.setText("提示");
    	mb.setMessage("代码生成完成。");
//    	mb.open();
	}

	public String getTreeSubRelationField() {
		return treeSubRelationField;
	}

	public void setTreeSubRelationField(String treeSubRelationField) {
		this.treeSubRelationField = treeSubRelationField;
	}
	
	
}
