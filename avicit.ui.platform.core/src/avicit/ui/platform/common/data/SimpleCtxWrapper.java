package avicit.ui.platform.common.data;

import java.io.File;
import java.util.Hashtable;

import org.springframework.context.support.FileSystemXmlApplicationContext;

import avicit.ui.platform.common.exception.MsgException;
import avicit.ui.platform.common.exception.TansunException;
import avicit.ui.platform.common.util.AT;

import com.tansun.easycare.common.db.DbHandleDao;
import com.tansun.workflow.engine.WorkflowEngine;
import com.tansun.workflow.util.CommonConfig;

public class SimpleCtxWrapper {

	private static final String WF_ENGINE = "wfEngine";

	private static final String ECDAO = "ECDAO";

	private static final String DAO = "DAO";

	private static Hashtable wrappers = new Hashtable();

	public static SimpleCtxWrapper getCurrent(String name) {
		AT.isNotNull(name);
		Object wrapper = wrappers.get(name);
		if (null == wrapper) {
			SimpleCtxWrapper wr = new SimpleCtxWrapper(name);
			wrappers.put(name, wr);
			return wr;
		} else {
			return (SimpleCtxWrapper) wrapper;
		}
	}

	public static void setCurrent(SimpleCtxWrapper current) {
		wrappers.put(current.name, current);
	}
	
	public static SimpleCtxWrapper removeCurrent(String name) {
		return (SimpleCtxWrapper)wrappers.remove(name);
	}

	private DbHandleDao dao;

	private DbHandleDao ecDao;

	private FileSystemXmlApplicationContext fsac;

	private boolean isInitialized;

	private SystemConfig sysConfig;
	private WorkflowEngine wfEngine;

	private String name;

	private File config;

	private File workflow;

	private File spring;


	public SimpleCtxWrapper(String name) {
		super();
		this.name = name;
	}

	public void dispose() {
		if(fsac != null)
		{
			fsac.destroy();
			fsac.close();
			fsac = null;
		}
	}

	private Object getBean(String beanName) {

		Object result = null;
		try {
			initPlatformSpringCtx(spring,workflow,config);

			if (fsac != null) {
				result = fsac.getBean(beanName);
			} else{
				throw new TansunException("The config file is invalid.");
			}

		} catch (TansunException e) {
			throw new RuntimeException(e);
		} catch (Throwable ee) {
			AT.throwe(ee);
		} finally {
		}

		return result;
	}

	public DbHandleDao getDao() {
		if (dao == null )
			dao = (DbHandleDao) getSystemBean(DAO);
		return dao;
	}

	public DbHandleDao getEcDao() {

		if (ecDao == null)
			ecDao = (DbHandleDao) getSystemBean(ECDAO);
		return ecDao;
	}


	public SystemConfig getSysConfig() {

//		if (null == sysConfig) {
//			sysConfig = new SystemConfig();
//			Document doc = getModel();
//			Node root = doc.getDocumentElement();
//			List list = XmlHelp.getChildElementsByName(root, "Bean");
//
//			for (int i = 0; i < list.size(); i++) {
//				org.w3c.dom.Node parentdnode = (org.w3c.dom.Node) list.get(i);
//				org.w3c.dom.Node attNode = XmlHelp.getNodeAttribute(
//						parentdnode, "id");
//				if (attNode != null
//						&& "orgDataSource".equals(attNode.getNodeValue())) {
//					org.w3c.dom.Node childnode = XmlHelp.getChildElementByName(
//							parentdnode, "property", "name", "driverClassName");
//					if (childnode != null) {
//						childnode = XmlHelp.getChildElement(childnode, "value");
//						if (childnode != null)
//							sysConfig.setDriver(XmlHelp
//									.getElementText(childnode));
//					}
//					childnode = XmlHelp.getChildElementByName(parentdnode,
//							"property", "name", "url");
//					childnode = XmlHelp.getChildElement(childnode, "value");
//					if (childnode != null) {
//						sysConfig.setUrl(XmlHelp.getElementText(childnode));
//					}
//					childnode = XmlHelp.getChildElementByName(parentdnode,
//							"property", "name", "username");
//					childnode = XmlHelp.getChildElement(childnode, "value");
//					if (childnode != null) {
//						sysConfig
//								.setUsername(XmlHelp.getElementText(childnode));
//					}
//					childnode = XmlHelp.getChildElementByName(parentdnode,
//							"property", "name", "password");
//					childnode = XmlHelp.getChildElement(childnode, "value");
//					if (childnode != null) {
//						sysConfig
//								.setPassword(XmlHelp.getElementText(childnode));
//					}
//				} else if (attNode != null
//						&& "AppDataSource".equals(attNode.getNodeValue())) {
//					org.w3c.dom.Node childnode = XmlHelp.getChildElementByName(
//							parentdnode, "property", "name", "username");
//					childnode = XmlHelp.getChildElement(childnode, "value");
//					if (childnode != null) {
//						sysConfig.setBizUsername(XmlHelp
//								.getElementText(childnode));
//					}
//					childnode = XmlHelp.getChildElementByName(parentdnode,
//							"property", "name", "password");
//					childnode = XmlHelp.getChildElement(childnode, "value");
//					if (childnode != null) {
//						sysConfig.setBizPassword(XmlHelp
//								.getElementText(childnode));
//					}
//				}
//			}
//			String webFolder = WebrootUtil
//					.getWebContentFolderName(getProject());
//			sysConfig.setWebFolder(webFolder);
//			sysConfig.setSpringFolder(webFolder + "/WEB-INF");
//			sysConfig.setFacesFolder(webFolder + "/WEB-INF/faces-configs");
//			sysConfig.setSrcFolder("/src-Application");
//			sysConfig.setQueryFolder(webFolder + "/WEB-INF/query-configs");
//			sysConfig
//					.setProcessDir(webFolder + "/WEB-INF/wf-configs/processes");
//
//		}
		return sysConfig;
	}

	private Object getSystemBean(final String beanName) {
				Object result = getBean( beanName);

		if (result != null) {
			return result;
		} else {
			throw new MsgException("The Bean:" + beanName + " is not found.");
		}
	}

	public WorkflowEngine getWfEngine() {
		if (wfEngine == null)
			wfEngine = (WorkflowEngine) getSystemBean(WF_ENGINE);
		return wfEngine;
	}



	private void initPlatformSpringCtx(File springfile,File actionworkflow,File configpath) {
		if (!isInitialized) {
			try {
				CommonConfig.initialize(configpath.getAbsolutePath());
				String common = springfile.getAbsolutePath();
				 String workflow = actionworkflow.getAbsolutePath();
				FileSystemXmlApplicationContext fsac1 = new FileSystemXmlApplicationContext(
						new String[] { common  ,workflow }, false);
				fsac1.refresh();
				fsac = fsac1;
				isInitialized = true;
			} catch (Exception e) {
				if (fsac != null) {
					fsac.close();
					fsac = null;
				}
				AT.throwe(e);
			}
		}
	}

	public void setDao(DbHandleDao dao) {
		this.dao = dao;
	}

	public void setEcDao(DbHandleDao dao) {
		this.ecDao = dao;
	}

	public void setSysConfig(SystemConfig sysConfig) {
		this.sysConfig = sysConfig;
	}

	public void setWfEngine(WorkflowEngine wfEngine) {
		this.wfEngine = wfEngine;
	}
}
