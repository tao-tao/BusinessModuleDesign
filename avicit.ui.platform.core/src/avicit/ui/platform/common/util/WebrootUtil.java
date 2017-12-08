package avicit.ui.platform.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipFile;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.core.runtime.content.IContentTypeManager;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.DocumentProviderRegistry;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.FileBufferModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import avicit.ui.platform.common.IPlatformConstants;
import avicit.ui.platform.common.data.XmlParsedFile;

/**
 * This utility class is used to make a bridge between the relative path from
 * webroot and physical path in the project.
 * 
 * @author Xiao-guang Zhang
 * 
 */
public class WebrootUtil {

	public static IProject getProject() {
		return ProjectFinder.getCurrentProjectByActiveEditor();
		// return ElementCreationFactory.getProject();
	}

	/**
	 * get the webpath for the project path. The project path is something like
	 * "/projectname/webroot/filename.jsp", or "/projectname/webroot/folder".
	 * The project information should be removed from project path, e.g,
	 * "/filename.jsp" or "/folder/*";
	 * 
	 * @param path
	 * @return the web path
	 */
	public static String getWebPath(IPath path) {
		String strWebrootPath = "";
		IProject project = WorkspaceUtil.getProjectFor(path);
		IPath webContentPath = getWebContentPath(project);
		if (webContentPath != null && webContentPath.isPrefixOf(path)) {
			int start = path.matchingFirstSegments(webContentPath);
			String[] segments = path.segments();
			for (int i = start, n = path.segmentCount(); i < n; i++) {
				strWebrootPath = strWebrootPath + IFileFolderConstants.PATH_SEPARATOR + segments[i];
			}
		}
		return strWebrootPath;
	}

	/**
	 * To see if a resource is under the webcontent folder.
	 * 
	 * @param resource
	 * @return true if resource is within the web content folder hierarchy
	 */
	public static boolean isUnderWebContentFolder(IResource resource) {
		IPath webContentPath = getWebContentPath(resource.getProject());
		if (webContentPath != null) {
			return webContentPath.isPrefixOf(resource.getFullPath());
		}
		return true;
	}

	/**
	 * @param project
	 * @return full path to web content folder
	 */
	public static IPath getWebContentPath(IProject project) {
		if (project != null) {
			return project.getFolder("WebRoot").getLocation();
		}
		return null;
	}

	/**
	 * Return the name of the web content folder. i.e, "WebContent"
	 * 
	 * @param project
	 * @return the web content folder name
	 */
	public static String getWebContentFolderName(IProject project) {
		IPath webContentPath = getWebContentPath(project);
		if (webContentPath != null)
			return webContentPath.lastSegment();
		return null;
	}

	/**
	 * @param project
	 * @return folder where for web content
	 */
	public static IFolder getWebContentFolder(IProject project) {
		IPath webContentPath = getWebContentPath(project);
		IFolder folder = null;
		if (webContentPath != null) {
			folder = project.getFolder(webContentPath.removeFirstSegments(webContentPath.segmentCount() - 1));

		}
		return folder;
	}

	/**
	 * return the depth of webcontent folder. For example, if the webcontent
	 * folder path is /projectname/webContent, then return 2, if it's
	 * /projectname/a/webContent, then return 3.
	 * 
	 * @param project
	 * @return the depth of webcontent folder
	 */
	public static int getWebContentFolderDepth(IProject project) {
		if (project != null) {
			IPath webContentPath = getWebContentPath(project);
			if (webContentPath != null) {
				return webContentPath.segmentCount();
			}
		}
		// default to 2
		return 2;
	}

	/**
	 * determine the path of web file is valid or not
	 * 
	 * @param path -
	 *            the path of web file
	 * @return - true - valid web file
	 */
	public static boolean isValidWebFile(IPath path) {
		String[] jspExtensions = getJSPFileExtensions();

		String extension = path.getFileExtension();
		if (extension != null && Arrays.asList(jspExtensions).contains(extension)) {
			return true;
		}

		return false;
	}

	/**
	 * get the webpath for the project path. The project path is something like
	 * "/projectname/webroot/filename.jsp", or "/projectname/webroot/folder".
	 * The project information should be removed from project path, e.g,
	 * "/filename.jsp" or "/folder/*";
	 * 
	 * @param strPath -
	 *            the project path
	 * @return - web path remove from "/projectname/webroot"
	 * @deprecated use getWebPath(IPath path) instead.
	 */
	public static String getWebPath(String strPath) {
		String strWebrootPath = "";
		if (strPath != null) {
			IPath path = new Path(strPath);
			return getWebPath(path);
		}
		return strWebrootPath;
	}

	public static String getPageNameFromWebPath(String strWebPath) {
		String pageName = strWebPath;

		if (pageName.startsWith(IFileFolderConstants.PATH_SEPARATOR)) {
			pageName = pageName.substring(1);
		}

		String[] jspExtensions = getJSPFileExtensions();
		for (int i = 0, n = jspExtensions.length; i < n; i++) {
			String extension = IFileFolderConstants.DOT + jspExtensions[i];
			if (pageName.endsWith(extension)) {
				pageName = pageName.substring(0, pageName.length() - extension.length());
				break;
			}
		}

		return pageName;
	}

	/**
	 * Get the JSP file extension from Eclipse preference
	 * Windows->Preferences->General->Content Types
	 * 
	 * @return String Array for JSP file extensions
	 */
	public static String[] getJSPFileExtensions() {
		IContentTypeManager typeManager = Platform.getContentTypeManager();
		IContentType jspContentType = typeManager.getContentType("org.eclipse.jst.jsp.core.jspsource");
		if (jspContentType != null) {
			return jspContentType.getFileSpecs(IContentType.FILE_EXTENSION_SPEC);
		}
		return null;
	}

	public static class Field {
		public Field(String name, String type) {
			this.name = name;
			this.type = type;
		}

		@Override
		public String toString() {
			return "[" + this.name + "]" + this.type;
		}

		public String name;

		public String type;
	}

	public static class ObjectField {
		public ObjectField(String name, Object type) {
			this.name = name;
			this.type = type;
		}

		public String name;

		public Object type;
	}

	public static String[] getAllActionFiles(Element element, boolean isActionSet) {
		List commands = new ArrayList();

		if (element != null) {
			List list = XmlHelp.getChildElementsByName(element, "import");
			for (int i = 0; i < list.size(); i++) {
				org.w3c.dom.Node parentnode = (org.w3c.dom.Node) list.get(i);
				org.w3c.dom.Node attnode = parentnode.getAttributes().getNamedItem("resource");
				if (attnode != null) {
					String path = attnode.getNodeValue();
					if ((isActionSet && path.indexOf("actionset") > 0) || (!isActionSet && path.indexOf("actionset") < 0)) {
						commands.add(path);
					}
				}
			}
		}
		return (String[]) commands.toArray(new String[commands.size()]);
	}

	public static List getAllActions(XmlParsedFile file) {
		List commands = new ArrayList();

		if (file.getEle() != null) {
			Element element = file.getEle();
			List list = XmlHelp.getChildElementsByName(element, "Bean");
			for (int i = 0; i < list.size(); i++) {
				org.w3c.dom.Node parentnode = (org.w3c.dom.Node) list.get(i);
				org.w3c.dom.Node attnode = parentnode.getAttributes().getNamedItem("id");
				if (attnode != null) {
					String id = attnode.getNodeValue();
					ObjectField field = new ObjectField(id, parentnode);
					commands.add(field);
				}
			}
		}
		return commands;
	}

	public static List getAllEndPoints(XmlParsedFile file) {
		List commands = new ArrayList();

		if (file.getEle() != null) {
			Element element = file.getEle();
			List list = XmlHelp.getChildElementsByName(element, "endpoint");
			for (int i = 0; i < list.size(); i++) {
				org.w3c.dom.Node parentnode = (org.w3c.dom.Node) list.get(i);
				org.w3c.dom.Node attnode = parentnode.getAttributes().getNamedItem("id");
				if (attnode != null) {
					String id = attnode.getNodeValue();
					ObjectField field = new ObjectField(id, parentnode);
					commands.add(field);
				}
			}
		}
		return commands;
	}

	public static List getAllScripts(IDOMDocument domDocument) {
		List commands = new ArrayList();

		if (domDocument != null) {
			Element element = domDocument.getDocumentElement();
			List list = XmlHelp.getChildElementsByName(element, "script");
			for (int i = 0; i < list.size(); i++) {
				org.w3c.dom.Node parentnode = (org.w3c.dom.Node) list.get(i);
				org.w3c.dom.Node attnode = parentnode.getAttributes().getNamedItem("id");
				if (attnode != null) {
					String id = attnode.getNodeValue();
					ObjectField field = new ObjectField(id, parentnode);
					commands.add(field);
				}
			}
		}
		return commands;
	}

	public static List getAllQuerys(IFile file) {
		List commands = new ArrayList();

		// IFile file =
		// inputfile.getProject().getFile("/WebRoot/WEB-INF/wf-configs/TransitionScript.xml");
		if (file.exists()) {
			IEditorInput editFile = new FileEditorInput(file);
			IDocumentProvider provider = DocumentProviderRegistry.getDefault()
					.getDocumentProvider(editFile);
			try {
				provider.connect(editFile);
			} catch (CoreException e) {
				e.printStackTrace();
			}
			IDocument newDocument = provider.getDocument(editFile);// FileBufferModelManager.getInstance().getModel(buttonFile);
			IStructuredModel model = null;
			if (newDocument instanceof IStructuredDocument) {
				model = StructuredModelManager.getModelManager()
						.getModelForEdit((IStructuredDocument) newDocument);
			}
			IDOMDocument ele = ((IDOMModel) model).getDocument();
			Element element = ele.getDocumentElement();
			List list = XmlHelp.getChildElementsByName(element, "Query");
			for (int i = 0; i < list.size(); i++) {
				org.w3c.dom.Node parentnode = (org.w3c.dom.Node) list.get(i);
				Node childnode = XmlHelp.getChildElement(parentnode, "QueryID");
				String id = XmlHelp.getElementText(childnode);
				if (id != null && id.length() > 0) {
					ObjectField field = new ObjectField(id, parentnode);
					commands.add(field);
				}
			}
		}
		return commands;
	}

	public static List getAllButtons() {
		List commands = new ArrayList();
		InputStream input = null;
		IFile buttonFile = getProject().getFile(
				IPlatformConstants.ACTIONSET_BUTTON_XML);
		JarFile jarFile = null;
		
		try {
			if (!buttonFile.exists()) {
				buttonFile = getProject().getFile(
						IPlatformConstants.ACTIONSET_BUTTON_XML2);
			}
			if (!buttonFile.exists()) {
				try {
					jarFile = new JarFile(getProject()
							.getFile(IPlatformConstants.ACTIONSET_BUTTON_JAR)
							.getLocation().toString());
					JarEntry jarEntry = jarFile
							.getJarEntry(IPlatformConstants.ACTIONSET_BUTTON_XML1);
					if (jarEntry != null && jarFile != null) {
						input = jarFile.getInputStream(jarEntry);
					}
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

			Map buttonMap = new HashMap();
			Element element = null;
			if (buttonFile.exists()) {
				IStructuredModel model = FileBufferModelManager.getInstance()
						.getModel(buttonFile);
				IDOMDocument ele = ((IDOMModel) model).getDocument();
				element = ele.getDocumentElement();
			} else {
				Document doc = XmlHelp.openXmlConfig(input);
				element = doc.getDocumentElement();
			}
			List list = XmlHelp.getChildElementsByName(element, "Bean");
			for (int i = 0; i < list.size(); i++) {
				org.w3c.dom.Node parentdnode = (org.w3c.dom.Node) list.get(i);
				Node classatt = parentdnode.getAttributes().getNamedItem("class");
				if (classatt == null)
					continue;
				if (!"com.ec.api.ServiceDef".equals(classatt.getNodeValue()))
					continue;
				org.w3c.dom.Node childnode = XmlHelp.getNodeAttribute(parentdnode,
						"id");// XmlHelp.getChildElementByName(parentdnode,
								// "property",
								// "name",
								// "field");
				String id = null;
				String name = null;
				if (childnode != null) {
					// childnode = XmlHelp.getNodeAttribute(childnode, "value");
					// if (childnode != null)
					id = childnode.getNodeValue();// XmlHelp.getElementText(childnode);
				}
				childnode = XmlHelp.getChildElementByName(parentdnode, "property",
						"name", "dispName");
				childnode = XmlHelp.getNodeAttribute(childnode, "value");
				if (childnode != null) {
					name = childnode.getNodeValue();
				}
				if (id != null && name != null) {
					buttonMap.put(id, name);
				}
			}
			org.w3c.dom.Node node = XmlHelp.getChildElementByName(element, "Bean",
					"id", "CommonServices");
			node = XmlHelp.getChildElement(node, "property");
			node = XmlHelp.getChildElement(node, "map");
			list = XmlHelp.getChildElementsByName(node, "entry");
			for (int i = 0; i < list.size(); i++) {
				org.w3c.dom.Node childNode = (org.w3c.dom.Node) list.get(i);
				org.w3c.dom.Node attNode = childNode.getAttributes().getNamedItem(
						"key");
				org.w3c.dom.Node refNode = XmlHelp
						.getChildElement(childNode, "ref");
				if (attNode != null && refNode != null) {
					String name = attNode.getNodeValue();
					attNode = refNode.getAttributes().getNamedItem("bean");
					if (attNode != null) {
						String bean = attNode.getNodeValue();
						String beanName = (String) buttonMap.get(bean);
						if (name != null && beanName != null)
							commands.add(new Field(name, beanName));
					}
				}
			}
		} catch (DOMException e) {
			
			e.printStackTrace();
		}finally{
			if(jarFile != null){
				try {
					jarFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
		return commands;
	}

	public static List getAllServices() {
		List commands = new ArrayList();
		IFile buttonFile = getProject().getFile(IPlatformConstants.ACTIONSET_SERVICE_XML);
		if (buttonFile.exists()) {
			IStructuredModel model = FileBufferModelManager.getInstance().getModel(buttonFile);
			IDOMDocument ele = ((IDOMModel) model).getDocument();
			Element element = ele.getDocumentElement();
			List list = XmlHelp.getChildElementsByName(element, "Bean");
			for (int i = 0; i < list.size(); i++) {
				org.w3c.dom.Node parentnode = (org.w3c.dom.Node) list.get(i);
				org.w3c.dom.Node attnode = parentnode.getAttributes().getNamedItem("id");
				if (attnode != null) {
					String id = attnode.getNodeValue();
					commands.add(id);
				}
			}
		}
		return commands;
	}

	public static List getServiceMethods(String serviceid) {
		List commands = new ArrayList();
		if (serviceid != null) {
			IFile buttonFile = getProject().getFile(IPlatformConstants.ACTIONSET_SERVICE_XML);
			if (buttonFile.exists()) {
				IStructuredModel model = FileBufferModelManager.getInstance().getModel(buttonFile);
				IDOMDocument ele = ((IDOMModel) model).getDocument();
				Element element = ele.getDocumentElement();
				org.w3c.dom.Node findNode = null;
				List list = XmlHelp.getChildElementsByName(element, "Bean");
				for (int i = 0; i < list.size(); i++) {
					org.w3c.dom.Node parentnode = (org.w3c.dom.Node) list.get(i);
					org.w3c.dom.Node attnode = parentnode.getAttributes().getNamedItem("id");
					if (attnode != null) {
						String id = attnode.getNodeValue();
						if (serviceid.equals(id)) {
							findNode = parentnode;
							break;
						}
					}
				}
				if (findNode != null) {
					org.w3c.dom.Node attnode = findNode.getAttributes().getNamedItem("class");
					if (attnode != null) {
						try {
							Class cls = ClassHelper.loadClass(ProjectFinder.getCurrentJavaProject(), attnode.getNodeValue());
							Method[] ms = cls.getMethods();
							for (int i = 0; i < ms.length; i++) {
								commands.add(ms[i].getName());
							}
						} catch (Exception e) {
						}
					}
				}
			}
		}
		return commands;
	}

	public static ObjectField[] getButtonDefinitions(IDOMDocument ele) {
		List commands = new ArrayList();
		Map buttonMap = new HashMap();
		Element element = ele.getDocumentElement();
		List list = XmlHelp.getChildElementsByName(element, "Bean");
		for (int i = 0; i < list.size(); i++) {
			org.w3c.dom.Node parentdnode = (org.w3c.dom.Node) list.get(i);
			Node classatt = parentdnode.getAttributes().getNamedItem("class");
			if (classatt == null)
				continue;
			if (!"com.ec.api.ServiceDef".equals(classatt.getNodeValue()))
				continue;
			org.w3c.dom.Node childnode = XmlHelp.getNodeAttribute(parentdnode, "id");// XmlHelp.getChildElementByName(parentdnode,
																						// "property",
																						// "name",
																						// "field");
			String id = null;
			if (childnode != null) {
				id = childnode.getNodeValue();// XmlHelp.getElementText(childnode);
			}
			if (id != null) {
				buttonMap.put(id, parentdnode);
			}
		}
		org.w3c.dom.Node node = XmlHelp.getChildElementByName(element, "Bean", "id", "CommonServices");
		node = XmlHelp.getChildElement(node, "property");
		node = XmlHelp.getChildElement(node, "map");
		list = XmlHelp.getChildElementsByName(node, "entry");
		for (int i = 0; i < list.size(); i++) {
			org.w3c.dom.Node childNode = (org.w3c.dom.Node) list.get(i);
			org.w3c.dom.Node attNode = childNode.getAttributes().getNamedItem("key");
			org.w3c.dom.Node refNode = XmlHelp.getChildElement(childNode, "ref");
			if (attNode != null && refNode != null) {
				String name = attNode.getNodeValue();
				attNode = refNode.getAttributes().getNamedItem("bean");
				if (attNode != null) {
					String bean = attNode.getNodeValue();
					Node beanNode = (Node) buttonMap.get(bean);
					if (name != null && beanNode != null)
						commands.add(new ObjectField(name, new org.w3c.dom.Node[] { childNode, beanNode }));
				}
			}
		}
		return (ObjectField[]) commands.toArray(new ObjectField[commands.size()]);
	}

	public static ObjectField[] getEndPoints(IDOMDocument ele) {
		List commands = new ArrayList();
		Element element = ele.getDocumentElement();
		List list = XmlHelp.getChildElementsByName(element, "jaxws:endpoint");
		for (int i = 0; i < list.size(); i++) {
			org.w3c.dom.Node parentdnode = (org.w3c.dom.Node) list.get(i);
			org.w3c.dom.Node childnode = XmlHelp.getNodeAttribute(parentdnode, "id");
			String id = null;
			if (childnode != null) {
				id = childnode.getNodeValue();// XmlHelp.getElementText(childnode);
			}
			childnode = XmlHelp.getNodeAttribute(parentdnode, "implementor");
			if (childnode != null) {
				String beanid = childnode.getNodeValue();
				if (beanid.startsWith("#"))
					beanid = beanid.substring(1);
				
				org.w3c.dom.Node txnode = XmlHelp.getChildElementByName(element, "Bean", "id", beanid);
				org.w3c.dom.Node node = null;
				String target = XmlHelp.getSpringPropertyRefValue(txnode, "target");
				if(!StringUtils.isEmpty(target))
					node = XmlHelp.getChildElementByName(element, "Bean", "id", target);
				else
				{
					node = txnode;
					txnode = null;
				}
				
				if (id != null && node != null)
					commands.add(new ObjectField(id, new org.w3c.dom.Node[] { parentdnode, txnode, node }));
			}
		}
		return (ObjectField[]) commands.toArray(new ObjectField[commands.size()]);
	}
	public static IFile getButtonXML(InputStream input){
//		IFile ifle = new File("button.spring.xml");
		return null;
	}
}
