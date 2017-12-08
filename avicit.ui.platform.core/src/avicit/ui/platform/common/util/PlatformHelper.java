package avicit.ui.platform.common.util;

import java.io.ByteArrayInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.DocumentProviderRegistry;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.wst.sse.core.internal.FileBufferModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.w3c.dom.Node;

import avicit.ui.platform.common.IPlatformConstants;
import avicit.ui.platform.common.data.SystemConfig;

public class PlatformHelper {

	private static final String UTF_8 = "UTF-8";

	private static final int MAXLength = 700;

	public static SystemConfig fetchSystemConfig(IDOMDocument doc) {

		SystemConfig sysConfig = new SystemConfig();
		Node root = doc.getDocumentElement();
		List list = XmlHelp.getChildElementsByName(root, "Bean");

		for (int i = 0; i < list.size(); i++) {
			org.w3c.dom.Node parentdnode = (org.w3c.dom.Node) list.get(i);
			org.w3c.dom.Node attNode = XmlHelp.getNodeAttribute(parentdnode, "id");
			if (attNode != null && "AppDataSource".equals(attNode.getNodeValue())) {
//				org.w3c.dom.Node childnode = XmlHelp.getChildElementByName(parentdnode, "property", "name", "driverClass");
//				if (childnode != null) {
//					childnode = XmlHelp.getChildElement(childnode, "value");
//					if (childnode != null)
						sysConfig.setDriver(XmlHelp.getSpringPropertyAttValue(parentdnode, "value", "driverClass"));
//				}
//				childnode = XmlHelp.getChildElementByName(parentdnode, "property", "name", "jdbcUrl");
//				childnode = XmlHelp.getChildElement(childnode, "value");
//				if (childnode != null) {
					sysConfig.setUrl(XmlHelp.getSpringPropertyAttValue(parentdnode, "value", "jdbcUrl"));
//				}
//				childnode = XmlHelp.getChildElementByName(parentdnode, "property", "name", "user");
//				childnode = XmlHelp.getChildElement(childnode, "value");
//				if (childnode != null) {
					sysConfig.setBizUsername(XmlHelp.getSpringPropertyAttValue(parentdnode, "value", "user"));
//				}
//				childnode = XmlHelp.getChildElementByName(parentdnode, "property", "name", "password");
//				childnode = XmlHelp.getChildElement(childnode, "value");
//				if (childnode != null) {
					sysConfig.setBizPassword(XmlHelp.getSpringPropertyAttValue(parentdnode, "value", "password"));
//				}
					break;
			}/* else if (attNode != null && "AppDataSource".equals(attNode.getNodeValue())) {
				org.w3c.dom.Node childnode = XmlHelp.getChildElementByName(parentdnode, "property", "name", "username");
				childnode = XmlHelp.getChildElement(childnode, "value");
				if (childnode != null) {
					sysConfig.setBizUsername(XmlHelp.getElementText(childnode));
				}
				childnode = XmlHelp.getChildElementByName(parentdnode, "property", "name", "password");
				childnode = XmlHelp.getChildElement(childnode, "value");
				if (childnode != null) {
					sysConfig.setBizPassword(XmlHelp.getElementText(childnode));
				}
			}*/
		}
		String webFolder = /* WebrootUtil.getWebContentFolderName(getProject()) */"WebRoot";
		sysConfig.setWebFolder(webFolder);
		sysConfig.setSpringFolder(webFolder + "/WEB-INF");
		sysConfig.setFacesFolder(webFolder + "/WEB-INF/faces-configs");
		sysConfig.setSrcFolder("/src-Application");
		sysConfig.setQueryFolder(webFolder + "/WEB-INF/query-configs");
		sysConfig.setProcessDir(webFolder + "/WEB-INF/wf-configs/processes");

		return sysConfig;

	}

	public static void saveSysDatasourceConfig(IDOMDocument doc, SystemConfig sysConfig) {
		Node root = doc.getDocumentElement();
		List list = XmlHelp.getChildElementsByName(root, "Bean");

		for (int i = 0; i < list.size(); i++) {
			org.w3c.dom.Node parentdnode = (org.w3c.dom.Node) list.get(i);
			org.w3c.dom.Node attNode = XmlHelp.getNodeAttribute(parentdnode, "id");
			if (attNode != null && "AppDataSource".equals(attNode.getNodeValue())) {
//				org.w3c.dom.Node childnode = XmlHelp.getChildElementByName(parentdnode, "property", "name", "driverClassName");
//				if (childnode != null) {
//					childnode = XmlHelp.createOrGetChildElement(childnode, "value");
//					if (childnode != null)
//						XmlHelp.setElementText(childnode, sysConfig.getDriver());
//				}
				XmlHelp.setSpringPropertyAttValue(parentdnode, "driverClass", "value", sysConfig.getDriver());
				XmlHelp.setSpringPropertyAttValue(parentdnode, "jdbcUrl", "value", sysConfig.getUrl());
				XmlHelp.setSpringPropertyAttValue(parentdnode, "user", "value", sysConfig.getBizUsername());
				XmlHelp.setSpringPropertyAttValue(parentdnode, "password", "value", sysConfig.getBizPassword());
//				childnode = XmlHelp.getChildElementByName(parentdnode, "property", "name", "url");
//				childnode = XmlHelp.getChildElement(childnode, "value");
//				if (childnode != null) {
//					XmlHelp.setElementText(childnode, sysConfig.getUrl());
//				}
//				childnode = XmlHelp.getChildElementByName(parentdnode, "property", "name", "username");
//				childnode = XmlHelp.getChildElement(childnode, "value");
//				if (childnode != null) {
//					XmlHelp.setElementText(childnode, sysConfig.getBizUsername());
//				}
//				childnode = XmlHelp.getChildElementByName(parentdnode, "property", "name", "password");
//				childnode = XmlHelp.getChildElement(childnode, "value");
//				if (childnode != null) {
//					XmlHelp.setElementText(childnode, sysConfig.getBizPassword());
//				}
			}/* else if (attNode != null && "AppDataSource".equals(attNode.getNodeValue())) {
				org.w3c.dom.Node childnode = XmlHelp.getChildElementByName(parentdnode, "property", "name", "driverClassName");
				if (childnode != null) {
					childnode = XmlHelp.createOrGetChildElement(childnode, "value");
					if (childnode != null)
						XmlHelp.setElementText(childnode, sysConfig.getDriver());
				}
				childnode = XmlHelp.getChildElementByName(parentdnode, "property", "name", "url");
				childnode = XmlHelp.getChildElement(childnode, "value");
				if (childnode != null) {
					XmlHelp.setElementText(childnode, sysConfig.getUrl());
				}
				childnode = XmlHelp.getChildElementByName(parentdnode, "property", "name", "username");
				childnode = XmlHelp.getChildElement(childnode, "value");
				if (childnode != null) {
					XmlHelp.setElementText(childnode, sysConfig.getBizUsername());
				}
				childnode = XmlHelp.getChildElementByName(parentdnode, "property", "name", "password");
				childnode = XmlHelp.getChildElement(childnode, "value");
				if (childnode != null) {
					XmlHelp.setElementText(childnode, sysConfig.getBizPassword());
				}
			}*/
		}
	}

	public static void openError(Shell shell, String message, String ermsg) {

		if (ermsg != null && ermsg.length() > MAXLength) {
			ermsg = ermsg.substring(0, MAXLength);
		}

		ErrorDialog.openError(shell, "ƽ̨�����쳣", message, new Status(IStatus.ERROR, IPlatformConstants.PluginID_Platform, 0, ermsg == null ? "" : ermsg, null), IStatus.ERROR);
	}

	public static void openError(Shell shell, String message, Throwable ex) {

		String ermsg = null;
		if (null != ex) {
			StringWriter sw = new StringWriter();
			ex.printStackTrace(new PrintWriter(sw));
			ermsg = sw.toString();
		}

		if (ermsg != null && ermsg.length() > MAXLength) {
			ermsg = ermsg.substring(0, MAXLength);
		}

		ErrorDialog.openError(shell, "ƽ̨�����쳣", message, new Status(IStatus.ERROR, IPlatformConstants.PluginID_Platform, 0, ermsg == null ? "" : ermsg, ex), IStatus.ERROR);
	}

	// public static void openError2(Shell shell, String message, Throwable ex)
	// {
	// // ed=new ExceptionDetailsDialog(shell,"ƽ̨�����쳣",null,message,ex,)
	//		
	// }

	public static IWorkbenchPage getActivePage() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
	}

	public static void refreshCurrentFile() {
		FileEditorInput fei = (FileEditorInput) PlatformHelper.getActiveEditor().getEditorInput();
		try {
			fei.getFile().refreshLocal(IResource.DEPTH_ZERO, null);
		} catch (CoreException e) {
			throw new RuntimeException(e);
		}
	}

	public static void saveCurrentFile() {
		FileEditorInput fei = (FileEditorInput) PlatformHelper.getActiveEditor().getEditorInput();
		IDocumentProvider provider = DocumentProviderRegistry.getDefault().getDocumentProvider(fei);
		IDocument doc = provider.getDocument(fei);
		try {
			provider.saveDocument(null, fei, doc, true);
		} catch (CoreException e) {
			throw new RuntimeException(e);
		}
	}

	public static IEditorPart getActiveEditor() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
	}

	public static IFile getCurrentFileByActiveEditor() {
		IFile file = null;
		try {
			file = ((IFileEditorInput) getActiveEditor().getEditorInput()).getFile();
		} catch (Exception e) {
			ProjectFinder.logger.info("getCurrentFile() - e=" + e); //$NON-NLS-1$
		}
		return file;
	}

	public static IFile getClassFile(String className) {
		if (className == null)
			return null;
		IJavaProject project = ProjectFinder.getCurrentJavaProject();
		IPackageFragmentRoot[] roots;
		try {
			roots = project.getAllPackageFragmentRoots();
			for (int i = 0; i < roots.length; i++) {
				if (roots[i].isArchive())
					continue;
				IPath path = roots[i].getPath();
				String rootName = path.removeFirstSegments(1).toPortableString();
				String classPathName = className;
				classPathName = classPathName.replace('.', '/');
				classPathName = rootName + "/" + classPathName + ".java";
				IFile file = WebrootUtil.getProject().getFile(classPathName);
				if (file.exists())
					return file;
			}
		} catch (JavaModelException e) {
			ProjectFinder.logger.info("getClassFile(String) - e=" + e); //$NON-NLS-1$
		}
		return null;
	}
	
	public static IFile getWebFile(String pageName) {
		if (pageName == null)
			return null;
		IProject project = ProjectFinder.getCurrentProjectByActiveEditor();
		IPath parent = new Path("WebRoot");
		parent = parent.append(pageName);
		return project.getFile(parent);
	}
	
	public static IFolder getPackageFolder(String packName) {
		if (packName == null)
			return null;
		IJavaProject project = ProjectFinder.getCurrentJavaProject();
		IPackageFragmentRoot[] roots;
		try {
			roots = project.getAllPackageFragmentRoots();
			for (int i = 0; i < roots.length; i++) {
				if (roots[i].isArchive())
					continue;
				IPath path = roots[i].getPath();
				String rootName = path.removeFirstSegments(1).toPortableString();
				String classPathName = packName;
				classPathName = classPathName.replace('.', '/');
				classPathName = rootName + "/" + classPathName;
				IFolder file = WebrootUtil.getProject().getFolder(classPathName);
				if (file.exists())
					return file;
			}
		} catch (JavaModelException e) {
			ProjectFinder.logger.info("getClassFile(String) - e=" + e); //$NON-NLS-1$
		}
		return null;
	}
	
	public static IPackageFragment getPackageFragment(String packName) {
		if (packName == null)
			return null;
		IJavaProject project = ProjectFinder.getCurrentJavaProject();
		IPackageFragmentRoot[] roots;
		try {
			roots = project.getAllPackageFragmentRoots();
			for (int i = 0; i < roots.length; i++) {
				if (roots[i].isArchive())
					continue;
				IPackageFragment ret = roots[i].getPackageFragment(packName);
				if (ret != null && ret.exists())
					return ret;
			}
		} catch (JavaModelException e) {
			ProjectFinder.logger.info("getClassFile(String) - e=" + e); //$NON-NLS-1$
		}
		return null;
	}
	
	public static IPackageFragment getPackageFragment(IJavaProject project , String packName) {
		if (packName == null)
			return null;
		IPackageFragmentRoot[] roots;
		try {
			roots = project.getAllPackageFragmentRoots();
			for (int i = 0; i < roots.length; i++) {
				if (roots[i].isArchive())
					continue;
				IPackageFragment ret = roots[i].getPackageFragment(packName);
				if (ret != null && ret.exists())
					return ret;
			}
		} catch (JavaModelException e) {
			ProjectFinder.logger.info("getClassFile(String) - e=" + e); //$NON-NLS-1$
		}
		return null;
	}
	
	public static IFile getFile(String packName) {
		if (packName == null)
			return null;
		IJavaProject project = ProjectFinder.getCurrentJavaProject();
		IPackageFragmentRoot[] roots;
		try {
			roots = project.getAllPackageFragmentRoots();
			for (int i = 0; i < roots.length; i++) {
				if (roots[i].isArchive())
					continue;
				IPath path = roots[i].getPath();
				String rootName = path.removeFirstSegments(1).toPortableString();
				String classPathName = packName;
//				classPathName = classPathName.replace('.', '/');
				classPathName = rootName + "/" + classPathName;
				IFile file = WebrootUtil.getProject().getFile(classPathName);
				if (file.exists())
					return file;
			}
		} catch (JavaModelException e) {
			ProjectFinder.logger.info("getClassFile(String) - e=" + e); //$NON-NLS-1$
		}
		return null;
	}

	public static IFile getPlatformSpringConfigFile(IProject jp) {
		IFile file = jp.getFile(IPlatformConstants.SpringFile_Platform);
		return file;
	}

	public static IFile getAppSpringConfigFile(IProject jp) {
		IFile file = jp.getFile(IPlatformConstants.SpringFile_App);
		if(!file.exists())
			file = jp.getFile(IPlatformConstants.SpringFile_App1);
		return file;
	}

	public static IFile getAppBatchConfigFile(IProject jp) {
		IFile file = jp.getFile(IPlatformConstants.SpringFile_Batch);
		return file;
	}

	public static IDOMDocument createDOMDocument(IFile file) {
		IStructuredModel model = FileBufferModelManager.getInstance().getModel(file);
		IDOMDocument doc = ((IDOMModel) model).getDocument();
		return doc;
	}

	public static void saveSysDatasourceConfigToPojoListXML(SystemConfig sc) {

		String path = IPlatformConstants.FORM_LIST_XML;

		IFile file = ProjectFinder.getCurrentProjectByActiveEditor().getFile(path);
		String xmlstr = IOHelper.getFileString(file.getLocation().toFile(), UTF_8);

		Document doc = null;
		try {
			doc = DocumentHelper.parseText(xmlstr);
		} catch (DocumentException e) {
			AT.throwe(e);
		}

		org.dom4j.Node dbdriver = (org.dom4j.Node) doc.selectNodes("//" + "dbDriver").get(0);
		org.dom4j.Node dbURL = (org.dom4j.Node) doc.selectNodes("//" + "dbURL").get(0);
		org.dom4j.Node dbUser = (org.dom4j.Node) doc.selectNodes("//" + "dbUser").get(0);
		org.dom4j.Node password = (org.dom4j.Node) doc.selectNodes("//" + "password").get(0);

		dbdriver.setText(sc.getDriver());
		dbURL.setText(sc.getUrl());
		dbUser.setText(sc.getBizUsername());
		password.setText(sc.getBizPassword());

		xmlstr = doc.asXML();

		try {
			file.setContents(new ByteArrayInputStream(xmlstr.getBytes(UTF_8)), true, false, null);
			file.refreshLocal(1, null);
		} catch (Exception e) {
			AT.throwe(e);
		}

	}

	public static IPackageFragment createPackageFragment(IJavaProject project, String packageName, boolean b, IProgressMonitor object, Shell shell) {
		try {
			IPackageFragmentRoot root = HSUtil.selectProjectRoot(project, shell);
			if(root != null)
				root.createPackageFragment(packageName, b, (IProgressMonitor) object);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static IPackageFragment createPackageFragment(String packageName, boolean b, IProgressMonitor object, Shell shell) {
		IJavaProject project = ProjectFinder.getCurrentJavaProject();
		try {
			IPackageFragmentRoot root = HSUtil.selectProjectRoot(project, shell);
			if(root != null)
				root.createPackageFragment(packageName, b, (IProgressMonitor) object);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return null;
	}

}
