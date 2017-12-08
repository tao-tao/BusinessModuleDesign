package avicit.ui.platform.common;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.graphics.Color;

public interface IPlatformConstants {

	/**
	 * ȱʡ��ť�����ļ�·��
	 */
	public static final String ACTIONSET_BUTTON_XML = "WebRoot/WEB-INF/springxml/applicationContext-button.xml";
	public static final String ACTIONSET_BUTTON_JAR = "WebRoot/WEB-INF/comp/platform/framework.jar";
	public static final String ACTIONSET_BUTTON_XML1 = "META-INF/spring/button.spring.xml";
	public static final String ACTIONSET_BUTTON_XML2 = "WebRoot/WEB-INF/springxml/button.spring.xml";
	public static final String ACTIONSET_SERVICE_XML = "WebRoot/WEB-INF/springxml/applicationContext-service.xml";
	public static final String WORKFLOW_SCRIPT_XML = "WebRoot/WEB-INF/wf-configs/TransitionScript.xml";
	public static final String BEAN_CONFIG_TYPE = "Bean";
	public static final String FormManagerID = "com.tansun.easycare.editor.MainEditor";
	public static final String BizObjectManagerID = "com.tansun.easycare.editor.BOEditor";
	public static final String COMPATIBILITY_CONFIG_TYPE = "Compatibility";
	public static final String CONSTRUCTOR_CONFIG_TYPE = "Constructor";
	public static final String DEFAULT_EC_NAME = "Easycare1.0";
	public static final String DesignerEditorID = "com.tansun.workflow.designer.editor.DesignerEditor";
	public static final String DIR_QUERY_CONFIGS = "WebRoot/WEB-INF/query-configs";
	public static final String DIR_NETWORK_CONFIGS = "WebRoot/WEB-INF/inter-configs";
	public static final String DIR_QUERYMODULE_CONFIGS = "WebRoot/WEB-INF/customquery-sources";
	public static final String EC_NAME = "easycare-name";
	public static final String EcActionEditorID = "com.tansun.platform.ui.EcActionEditor"; // TODO
	public static final String EcQueryEditorID = "com.tansun.platform.ui.EcQueryEditor";
	public static final String EcQuerymoduleEditorID = "com.tansun.platform.ui.EcQueryModuleEditor";	
	public static final String EcRuleEditorID = "com.tansun.platform.ui.EcScriptEditor";
	public static final String FIELD_CONFIG_TYPE = "Field";
	/**
	 * ȱʡ�?�ļ�·�����Ŀ¼
	 */
	public static final IPath Path_WebPage = new Path("WebRoot");
	public static final String ID_WebTreeEditor = "com.tansun.ui.model.tree.editor.WebTreeEditor"; //$NON-NLS-1$
	public static final String IntroductionPageConfigID = "com.tansun.platform.ui.introductionPage";
	public static final String OrganizationEditorID = "com.tansun.platform.ui.OrganizationEditor";
	public static final String ID_WebMenuEditor = "com.tansun.ui.model.menu.editor.WebMenuEditor";
	
	public static final String PageName_WebTreeEditor = "Web������";

	public static final String ID_PlatformConfigEditor = "com.tansun.platform.ui.PlatformConfigEditor";
	
	public static final String ID_JobEditor = "com.tansun.job.designer.editor.DesignerEditor";

	public static final String PluginID_Platform = "com.tansun.platform.ui";

	/**
	 * ȱʡת��script�ļ�·��
	 */
	public static final String Path_TransitionScript = "WebRoot/WEB-INF/wf-configs/TransitionScript.xml";

	public static final String ID_SourceEditor = "com.tansun.platform.ui.SourceEditor";

	/**
	 * ȱʡspring�����ļ�·��
	 */
	public static final String SpringFile_Platform = "WebRoot/WEB-INF/_platform/applicationContext-common.xml";

	public static final Color veryLightBlue = new Color(null, 224, 224, 255);
	public static final Color veryLightGray = new Color(null, 224, 224, 224);
	/**
	 * ȱʡ�����������ļ�Ŀ¼
	 */
	public static final String Path_WorkflowConfig = "WebRoot/WEB-INF/wf-configs/processes";
	/**
	 * ȱʡ�����������ļ�Ŀ¼
	 */
	public static final String WorkflowConfigWorkingPath = "WebRoot/WEB-INF/wf-configs";
	public static final String WorkflowConfigWorkingProcessesPath = "WebRoot/WEB-INF/wf-configs/processes";
	public static final String ID_WorkflowManagerEditor = "com.tansun.platform.ui.WorkflowEditor";
	public static final String ID_EcPlatformEditor = "com.tansun.platform.ui.EcPlatformEditor"; //$NON-NLS-1$
	public static final String SpringFile_App = "WebRoot/WEB-INF/springxml/applicationContext-common.xml";
	public static final String SpringFile_Batch = "WebRoot/WEB-INF/springxml/batch.xml";
	public static final String SpringFile_App1 = "WebRoot/WEB-INF/applicationContext-common.xml";
	public static final String FORM_LIST_XML = "WebRoot/WEB-INF/formList.xml";
	
	/**
	 * ȱʡͼƬ·��
	 */
	public static final IPath Path_WebImg = new Path("WebRoot/images");

}
