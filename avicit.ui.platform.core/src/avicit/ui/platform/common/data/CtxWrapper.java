package avicit.ui.platform.common.data;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import avicit.ui.platform.common.IPlatformConstants;
import avicit.ui.platform.common.exception.MsgException;
import avicit.ui.platform.common.exception.TansunException;
import avicit.ui.platform.common.util.AT;
import avicit.ui.platform.common.util.PlatformHelper;
import avicit.ui.platform.db.MetaDataRetriever;

import com.tansun.easycare.common.db.DbHandleDao;
import com.tansun.workflow.engine.WorkflowEngine;
import com.tansun.workflow.util.CommonConfig;

public class CtxWrapper {

	private static final String WF_ENGINE = "wfEngine";

	private static final String ECDAO = "ECDAO";

	private static final String DAO = "DAO";

	private static Hashtable wrappers = new Hashtable();

	public static CtxWrapper getCurrent(IProject project) {
		AT.isNotNull(project);
		Object wrapper = wrappers.get(project.getName());
		if (null == wrapper) {
			CtxWrapper wr = new CtxWrapper(project);
			wrappers.put(project.getName(), wr);
			return wr;
		} else {
			return (CtxWrapper) wrapper;
		}
	}

	public static void setCurrent(CtxWrapper current) {
		wrappers.put(current.getProject().getName(), current);
	}

	public static CtxWrapper removeCurrent(IProject project) {
		return (CtxWrapper) wrappers.remove(project.getName());
	}

	private MetaDataRetriever dataRetriever;

	private FileSystemXmlApplicationContext fsac;

	private boolean isInitialized;
	
	private long initTime;

	private IProject project;

	private SystemConfig sysConfig;

	private Map cacheData;

	public CtxWrapper(IProject project) {
		super();
		this.project = project;
	}

	public void dispose() {
//		if (dataRetriever != null && dataRetriever.getConnection() != null) {
//			dataRetriever.getSession().close();
//		}
		if (fsac != null) {
			fsac.destroy();
			fsac.close();
			fsac = null;
		}
	}

	private Object getBean(String beanName) {

		Object result = null;
		try {
			// monitor.beginTask("��ʼ��ȡ�����ļ���������ݿ�", 100);
			// monitor.worked(30);
			initPlatformSpringCtx();
			// if (monitor.isCanceled())
			// throw new InterruptedException("User cancel the progress.");

			if (fsac != null) {
				result = fsac.getBean(beanName);
				// monitor.subTask("��ʼ�������,���Ժ�.");
				// monitor.worked(70);
			} else {
				throw new TansunException("The config file is invalid.");
			}

		} catch (TansunException e) {
			throw new RuntimeException(e);
		} catch (Throwable ee) {
			// ee.printStackTrace();
			AT.throwe(ee);
		} finally {
			// monitor.done();
		}

		return result;
	}

	public DbHandleDao getDao() {
		return (DbHandleDao) getSystemBean(DAO);
	}

	public DbHandleDao getEcDao() {
		return (DbHandleDao) getSystemBean(ECDAO);
	}

	// private IDOMDocument getModel() {
	//
	// FileEditorInput fei = new FileEditorInput(this.project
	// .getFile(new Path(IPlatformConstants.SpringFile_App)));
	// IDocumentProvider provider = DocumentProviderRegistry.getDefault()
	// .getDocumentProvider(fei);
	// IDocument doc = provider.getDocument(fei);
	// return (IDOMDocument) doc;
	// }

	public IProject getProject() {
		return project;
	}

	public SystemConfig getSysConfig() {

		// if (null == sysConfig) {
		// sysConfig = new SystemConfig();
		// Document doc = getModel();
		// Node root = doc.getDocumentElement();
		// List list = XmlHelp.getChildElementsByName(root, "Bean");
		//
		// for (int i = 0; i < list.size(); i++) {
		// org.w3c.dom.Node parentdnode = (org.w3c.dom.Node) list.get(i);
		// org.w3c.dom.Node attNode = XmlHelp.getNodeAttribute(
		// parentdnode, "id");
		// if (attNode != null
		// && "orgDataSource".equals(attNode.getNodeValue())) {
		// org.w3c.dom.Node childnode = XmlHelp.getChildElementByName(
		// parentdnode, "property", "name", "driverClassName");
		// if (childnode != null) {
		// childnode = XmlHelp.getChildElement(childnode, "value");
		// if (childnode != null)
		// sysConfig.setDriver(XmlHelp
		// .getElementText(childnode));
		// }
		// childnode = XmlHelp.getChildElementByName(parentdnode,
		// "property", "name", "url");
		// childnode = XmlHelp.getChildElement(childnode, "value");
		// if (childnode != null) {
		// sysConfig.setUrl(XmlHelp.getElementText(childnode));
		// }
		// childnode = XmlHelp.getChildElementByName(parentdnode,
		// "property", "name", "username");
		// childnode = XmlHelp.getChildElement(childnode, "value");
		// if (childnode != null) {
		// sysConfig
		// .setUsername(XmlHelp.getElementText(childnode));
		// }
		// childnode = XmlHelp.getChildElementByName(parentdnode,
		// "property", "name", "password");
		// childnode = XmlHelp.getChildElement(childnode, "value");
		// if (childnode != null) {
		// sysConfig
		// .setPassword(XmlHelp.getElementText(childnode));
		// }
		// } else if (attNode != null
		// && "AppDataSource".equals(attNode.getNodeValue())) {
		// org.w3c.dom.Node childnode = XmlHelp.getChildElementByName(
		// parentdnode, "property", "name", "username");
		// childnode = XmlHelp.getChildElement(childnode, "value");
		// if (childnode != null) {
		// sysConfig.setBizUsername(XmlHelp
		// .getElementText(childnode));
		// }
		// childnode = XmlHelp.getChildElementByName(parentdnode,
		// "property", "name", "password");
		// childnode = XmlHelp.getChildElement(childnode, "value");
		// if (childnode != null) {
		// sysConfig.setBizPassword(XmlHelp
		// .getElementText(childnode));
		// }
		// }
		// }
		// String webFolder = WebrootUtil
		// .getWebContentFolderName(getProject());
		// sysConfig.setWebFolder(webFolder);
		// sysConfig.setSpringFolder(webFolder + "/WEB-INF");
		// sysConfig.setFacesFolder(webFolder + "/WEB-INF/faces-configs");
		// sysConfig.setSrcFolder("/src-Application");
		// sysConfig.setQueryFolder(webFolder + "/WEB-INF/query-configs");
		// sysConfig
		// .setProcessDir(webFolder + "/WEB-INF/wf-configs/processes");
		//
		// }
		return sysConfig;
	}

	private Object getSystemBean(final String beanName) {
		/*
		 * Shell shell = null; if(Display.getDefault() != null) shell =
		 * Display.getDefault().getActiveShell(); else shell = new Shell();
		 * ProgressMonitorJobsDialog dialog = new
		 * ProgressMonitorJobsDialog(shell); IRunnableWithProgress runnable =
		 * new IRunnableWithProgress() { public void run(IProgressMonitor
		 * monitor) throws InvocationTargetException, InterruptedException {
		 *  } }; try { dialog.setBlockOnOpen(false); dialog.run(true, true,
		 * runnable); dialog.close(); } catch (InvocationTargetException e) {
		 * throw new TansunException(e); } catch (InterruptedException e) {
		 * throw new TansunException("user interrupt."); }
		 */
		Object result = getBean(beanName);
		if (result != null) {
			return result;
		} else {
			throw new MsgException("The Bean:" + beanName + " is not found.");
		}
	}

	public WorkflowEngine getWfEngine() {
		return (WorkflowEngine) getSystemBean(WF_ENGINE);
	}

	private void initPlatformSpringCtx() {
		IFile file = PlatformHelper.getPlatformSpringConfigFile(this.project);
		if (!isInitialized || (initTime<file.getModificationStamp())) {
			try {
				IFolder dir = project.getFolder(IPlatformConstants.WorkflowConfigWorkingPath);
				CommonConfig.initialize(dir.getRawLocation().toOSString()); // ???
				String common = file.getRawLocation().toOSString();
				if(fsac != null)
				{
					fsac.close();
					fsac.destroy();
				}
				FileSystemXmlApplicationContext fsac1 = new FileSystemXmlApplicationContext(new String[] { common /* ,workflow */}, false);
				fsac1.refresh();
				fsac = fsac1;
				isInitialized = true;
				if(dataRetriever != null)
				{
					try{
						dataRetriever.getConnection().close();
					}catch(Throwable e){
						
					}
					dataRetriever = null;
				}
				initTime = file.getModificationStamp();
			} catch (Exception e) {
				if (fsac != null) {
					fsac.close();
					fsac = null;
				}
				AT.throwe(e);
			}
		}
	}

	public void setSysConfig(SystemConfig sysConfig) {
		this.sysConfig = sysConfig;
	}

	public MetaDataRetriever getDataRetriever() {
		initPlatformSpringCtx();
		if (dataRetriever == null)
			dataRetriever = new MetaDataRetriever(this);
		return dataRetriever;
	}

	public void setDataRetriever(MetaDataRetriever dataRetriever) {
		this.dataRetriever = dataRetriever;
	}

	public Object getCacheData(Object key) {
		if (this.cacheData != null)
			return cacheData.get(key);
		return null;
	}

	public void setCacheData(Object key, Object cacheV) {
		if (this.cacheData == null)
			cacheData = new HashMap();
		this.cacheData.put(key, cacheV);
	}
}
