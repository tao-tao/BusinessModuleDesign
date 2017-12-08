package avicit.platform6.tools.codegeneration.wizard;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.util.CoreUtility;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.List;

import avicit.platform6.tools.codegeneration.core.MainCreatCode;
import avicit.platform6.tools.codegeneration.core.Resource;
import avicit.platform6.tools.codegeneration.log.AvicitLogger;

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
 * 类说明：生成代码的向导页
 * </p>
 * <p>
 * 修改记录：
 * </p>
 */
public class DoCreatPage extends BaseWizardPage {

	public static String CREATE_CODE_COMPLETE_MSG = "代码生成完成!";

	private List listShowConfInfo;
	private List listShowDoInfo;
	private List listShowMavenInfo;
	private Button btnDoCreate;
	private String moduleName;
	private String moduleGroupName;

	public String getSubSystemName() {
		return moduleGroupName;
	}

	public void setSubSystemName(String subSystemName) {
		this.moduleGroupName = subSystemName;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public List getListShowDoInfo() {
		return listShowDoInfo;
	}

	public void setListShowDoInfo(List listShowDoInfo) {
		this.listShowDoInfo = listShowDoInfo;
	}

	private CodeGenerationWizard codeGenerationWizard;

	protected DoCreatPage(String pageName) {
		super(pageName);
		setTitle("生成代码");
		setDescription("根据配置信息点生成按钮生成相应代码。");
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		Group groupShowInfo = new Group(container, SWT.NONE);
		groupShowInfo.setText("代码生成配置信息");
		groupShowInfo.setBounds(10, 10, 554, 144);

		listShowConfInfo = new List(groupShowInfo, SWT.BORDER | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.MULTI);
		listShowConfInfo.setBounds(10, 22, 534, 82);

		Group groupShowDoInfo = new Group(container, SWT.NONE);
		groupShowDoInfo.setText("代码生成信息");
		groupShowDoInfo.setBounds(10, 159, 554, 144);

		btnDoCreate = new Button(groupShowDoInfo, SWT.TOGGLE);
		btnDoCreate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (btnDoCreate.getSelection()) {
					if (MessageDialog.openConfirm(null, "提示", "确认生成代码？")) {
						btnDoCreate.setEnabled(false);

						try {
							OutputStream fos = new FileOutputStream(codeGenerationWizard.getFile());
							codeGenerationWizard.getProps().store(fos,"更新配置====");
							fos.close();

						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						IProject project = codeGenerationWizard.getProject();

						String projectPath = project.getLocation().makeAbsolute().toFile().getAbsolutePath();
						//Modified by Tao Tao, get projectName.
						String projectName = project.getName();
						String templatePath = codeGenerationWizard.getTempletPath();
//						String modeName = codeGenerationWizard.getCreatePackage();
						String modeName = DoCreatPage.this.moduleGroupName;
						java.util.List<String> tables = codeGenerationWizard.getMainTableNames();
						if( tables.size()>0){
							DoCreatPage.this.moduleName = tables.get(0);
						}

						MainCreatCode mainCreatCode = new MainCreatCode();
						mainCreatCode.setSingleFlowTemplate(codeGenerationWizard.templeteSelectPage.isSingleFlowTemplate());

						String webJsp = codeGenerationWizard.templeteSelectPage.getPathConstant();

						mainCreatCode.setCodeGenerationWizard(codeGenerationWizard);
						String webPath =codeGenerationWizard.templeteSelectPage.getWebPath().getText();
						webJsp =webPath.replaceAll(webJsp, "");

						//Modified by Tao Tao, add projectName as a parameter to generatorCode.
						mainCreatCode.generatorCode(projectName, projectPath, templatePath,modeName, tables,codeGenerationWizard.templeteSelectPage.getIsMain(),codeGenerationWizard.templeteSelectPage.isTree(),webPath,webJsp);

						listShowDoInfo.getDisplay().syncExec(mainCreatCode.getCreateThread());
					} else {
						resumCreatButtion();
					}
				}

			}
		});
		btnDoCreate.setBounds(464, 10, 80, 27);
		btnDoCreate.setText("生成");

		listShowDoInfo = new List(groupShowDoInfo, SWT.BORDER | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.MULTI);
		listShowDoInfo.setBounds(10, 43, 534, 91);

		setControl(container);

		codeGenerationWizard = this.getRealWizard();
		this.setPageComplete(false);
	}

	@Override
	public boolean canFlipToNextPage() {
//		if (listShowDoInfo.getItemCount() > 0&& listShowDoInfo.getItem(listShowDoInfo.getItemCount() - 1).equals(CREATE_CODE_COMPLETE_MSG)) {
//			return true;
//		} else {
//			return false;
//		}
		return false;
	}

	public void initListShowConfInfo() {
		listShowConfInfo.removeAll();
		codeGenerationWizard = this.getRealWizard();
		/*IProject ip = codeGenerationWizard.getProject();
		// 是否maven工程
		try {
			IProjectDescription ipd = ip.getDescription();
			java.util.List<String> projectType = Arrays.asList(ipd.getNatureIds());
			if (projectType.contains(BusinessObjectConfigPage.getResourceBundle().getString(Resource.MAVEN))) {
				codeGenerationWizard.setManve(true);
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}*/
		if (codeGenerationWizard != null) {
			String pType = "工程类型:";
			if (codeGenerationWizard.isManve()) {
				pType += "Maven工程";
			} else {
				pType += "j2ee工程";
			}
			listShowConfInfo.add("生成模块所在项目:"
					+ codeGenerationWizard.getProject() + "    " + pType);
			//TODO 删除
			listShowConfInfo.add("生成模块路径:"+ codeGenerationWizard.getCreatePackage());
			/*
			 * listShowConfInfo.add("生成模块的模板路径:"+
			 * codeGenerationWizard.getTempletPath());
			 */

			if (codeGenerationWizard.templeteSelectPage.getIsMain()) {

				listShowConfInfo.add("数据库主表："
						+ codeGenerationWizard.getMainTableNames().get(0)
						+ "数据库子表："
						+ codeGenerationWizard.getSubTableNames().get(0));
				listShowConfInfo.add("主表关联字段："
						+ codeGenerationWizard.getMainRelationField()
						+ "      " + "子表关联字段："
						+ codeGenerationWizard.getSubRelationField());

			} else {
				String tabs = "";
				for (Object s : codeGenerationWizard.getMainTableNames()) {
					tabs = tabs + "," + s;
				}
				if(tabs.length()>0){
					listShowConfInfo.add("数据库表：" + tabs.substring(1));
				}
				
			}

		}
	}
	
	private String getModuleNameByTable(java.util.List<String> tables){
		if(codeGenerationWizard.templeteSelectPage.getIsMain() && tables.size()>0){
			this.moduleName = tables.get(0);
		}
		return this.moduleName;
	}

	private void setNewClassPath(IProject project,String moduleName){
		moduleName = moduleName.replace("_", "");
		moduleName = moduleName.toLowerCase();
		IPath initialPath = new Path(project.getFullPath()+"/"+this.moduleGroupName+"/" + moduleName);
		IJavaProject jp = JavaCore.create(project);
		IClasspathEntry[] path;
		try {
			path = jp.getRawClasspath();
			IClasspathEntry[] pathadd = new IClasspathEntry[path.length + 1];
			for (int i = 0; i < path.length; i++) {
				pathadd[i] = path[i];
			}
//			IFolder folder = project.getFolder("/sub-system007"+"/" + moduleName);
//			CoreUtility.createFolder(folder, true, true, null);
			pathadd[pathadd.length - 1] = JavaCore.newSourceEntry(initialPath);
			jp.setRawClasspath(pathadd, null);	
		} catch (JavaModelException e) {
			e.printStackTrace();
		} catch (CoreException e) {
			e.printStackTrace();
		}
		
	}
	public void resumCreatButtion() {
		btnDoCreate.setSelection(false);
		btnDoCreate.setEnabled(true);
		this.setPageComplete(true);
	}

	public void addCreatOkMsg() {
		this.listShowDoInfo.add(CREATE_CODE_COMPLETE_MSG);
		try {
			//2015-03-10 取消将功能模块作为源码包
			//this.setNewClassPath(codeGenerationWizard.getProject(), this.moduleName);
			codeGenerationWizard.getProject().refreshLocal(
					IResource.DEPTH_INFINITE, null);
		} catch (CoreException e) {
			e.printStackTrace();
			AvicitLogger.logInfo("刷新工程失败！");
		}
		this.setPageComplete(true);
	}

}
