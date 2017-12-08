package avicit.platform6.tools.codegeneration.core;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import avicit.platform6.tools.codegeneration.core.common.CreateCode;
import avicit.platform6.tools.codegeneration.log.AvicitLogger;
import avicit.platform6.tools.codegeneration.wizard.CodeGenerationWizard;

public class MainCreatCode{
	private CodeGenerationWizard codeGenerationWizard;

	private boolean isMainSub;
	
	private boolean isTree;
	
	private boolean isSingleFlowTemplate;

	public boolean isSingleFlowTemplate() {
		return isSingleFlowTemplate;
	}

	public void setSingleFlowTemplate(boolean isSingleFlowTemplate) {
		this.isSingleFlowTemplate = isSingleFlowTemplate;
	}

	/**
	 * 生成代码
	 */

	public void generatorCode(String projectName, String projectPath, String templatePath,String modeName, List<String> tables, boolean isMainSub,boolean isTree,String webPath,String webJsp) {




		this.isMainSub = isMainSub;

		this.isTree = isTree;
		
		boolean isReady = checkFilePath();

		if (!isReady) {
			return;
		}
		createCodeService(projectName, projectPath,webPath, templatePath, modeName, tables,webJsp);
	}

	public CodeGenerationWizard getCodeGenerationWizard() {
		return codeGenerationWizard;
	}

	public void setCodeGenerationWizard(CodeGenerationWizard codeGenerationWizard) {
		this.codeGenerationWizard = codeGenerationWizard;
		
	}

	public void initDBConifg(List jarFileList, String driverUrl,
			String driverClassName, String driverUser, String driverSchema,
			String driverPassword, int datatype) {
		dBConfig = new DBConfig();
		dBConfig.initDriverInfo(jarFileList, driverUrl, driverClassName,
				driverUser, driverSchema, driverPassword, datatype);

	}

	/**
	 * 检测是否参数都选择全了
	 */
	private boolean checkFilePath() {
		// String projectPath = projectPathText.getText();
		// String templatePath = templatePathText.getText();
		// String modeName = modeNameText.getText();
		String projectPath = "";
		String templatePath = "";
		String modeName = "";
		File projectFile = new File(projectPath);
		File templateFile = new File(templatePath);
		tableNames = null;
		// if (!projectFile.exists()) {
		// JOptionPane.showMessageDialog(null, "请正确选择项目路径!", "错误", 0);
		// projectPathButton.requestFocus();
		// return false;
		// }
		// if (!templateFile.exists()) {
		// JOptionPane.showMessageDialog(null, "请正确选择模板路径!", "错误", 0);
		// templatePathButton.requestFocus();
		// return false;
		// }
		// if (modeName == null || modeName.equals("")) {
		// JOptionPane.showMessageDialog(null, "请输入模块名称!", "错误", 0);
		// modeNameText.requestFocus();
		// return false;
		// }
		// if (tableNames == null || tableNames.size() < 1) {
		// JOptionPane.showMessageDialog(null, "请完成数据库相关配置并选择要操作的Table!", "错误",
		// 0);
		// databaseConfigButton.requestFocus();
		// return false;
		// } else {
		// return true;
		// }
		return true;
	}

	/**
	 * 开始运行生成代码的动作
	 */
	private void start(final String projectName, final String projectPath,final String webPath, final String templatePath,final String modeName, final List<String> tables,final String webJsp) {
		createThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// String driverSchema="PT6";
					// String databaseName="PT6";

					// tableNames=dBConfig.getAllTable(driverSchema,
					// databaseName);
					//

					createCode = new CreateCode(projectPath, webPath,new File(templatePath), modeName, tables,codeGenerationWizard.isManve(),webJsp);
					createCode.setSingleFlowTemplate(isSingleFlowTemplate);
					createCode.setCodewizard(codeGenerationWizard);
					if(isMainSub){
						createCode.setMainSub(isMainSub);
						createCode.setMainTableName(codeGenerationWizard.getMainTableNames().get(0));
						createCode.setMainKey(codeGenerationWizard.getMainRelationField());
						createCode.setSubTableName(codeGenerationWizard.getSubTableNames().get(0));
						createCode.setSubKey(codeGenerationWizard.getSubRelationField());
					}
					
					if(isTree){
						createCode.setTree(isTree);
						createCode.setTreeRelationField(codeGenerationWizard.getTreeRelationField());
						createCode.setTreeSubRelationField(codeGenerationWizard.getTreeSubRelationField());
						createCode.setTreeDisplayField(codeGenerationWizard.getTreeDisplayField());
					}
				
					
					createCode.setTableBusinessObject(codeGenerationWizard.getTableBusinessObject());

					createCode.setThreadStatus(CreateCode.ThreadStatus.Run);

					createCode.createServcie(projectName, !codeGenerationWizard.isCodeGenerationWithHbm());
				} catch (Exception e) {
					AvicitLogger.logError(e);
					AvicitLogger.openError(e.getMessage(), e);
					codeGenerationWizard.getDoCreatPage().resumCreatButtion();
					return;
				}
				if (createCode.isCompleteStatus()) {
					codeGenerationWizard.getDoCreatPage().addCreatOkMsg();
					codeGenerationWizard.getDoCreatPage().resumCreatButtion();
					codeGenerationWizard.alertOkMsg();
				}
			}
		});

	}

	private void createCodeService(String projectName, String projectPath,String webPath, String templatePath,String modeName, List<String> tables,String webJsp) {
		// resumeRunButton();

		start(projectName, projectPath, webPath,templatePath, modeName, tables,webJsp);
		// resumeReadyButton();
	}

	private Thread createThread;

	public Thread getCreateThread() {
		return createThread;
	}

	public void setCreateThread(Thread createThread) {
		this.createThread = createThread;
	}

	private java.util.List<String> tableNames = new ArrayList<String>();
	private CreateCode createCode;

	public CreateCode getCreateCode() {
		return createCode;
	}

	public void setCreateCode(CreateCode createCode) {
		this.createCode = createCode;
	}

	private DBConfig dBConfig = null;

	public static void main(String[] args) {
		MainCreatCode mainCreatCode = new MainCreatCode();
		List jarFileList = new ArrayList();
		jarFileList.add("C:\\Users\\Administrator\\Desktop\\ojdbc14_g.jar");
		// oracle 3
		int dbtype = 3;
		mainCreatCode
				.initDBConifg(jarFileList,
						"jdbc:oracle:thin:@10.216.37.222:1521:ptdev",
						"oracle.jdbc.driver.OracleDriver", "pt6", "PT6",
						"cape", dbtype);
		try {
			mainCreatCode.dBConfig.testConnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mainCreatCode.tableNames.add("sys_sample_project_info");
		//mainCreatCode.start("D:\\javawork\\AVICIT_PLATFORM\\platform_v6\\AVICIT_BPM6","","E:\\Workspaces\\Workspace_Platform_V6\\avicit.platform6.tools.codegeneration\\templete\\vm_avicit_v6","avicit.platform6.modules.ac", mainCreatCode.tableNames);

		// mainCreatCode.start("D:\\javawork\\AVICIT_PLATFORM\\platform_v6\\AVICIT_BPM6",
		// "E:\\Workspaces\\Workspace_Platform_V6\\codeGenaration\\src\\templete\\singleTable",
		// "",mainCreatCode.tableNames);
		mainCreatCode.getCreateThread().start();
	}
}
