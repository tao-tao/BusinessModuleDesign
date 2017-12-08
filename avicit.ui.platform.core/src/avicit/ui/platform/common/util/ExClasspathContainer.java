package avicit.ui.platform.common.util;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

import avicit.ui.platform.common.IPlatformConstants;

//import com.tansun.platform.prefs.ExInstallation;
//import com.tansun.platform.ui.PluginConstants;

public class ExClasspathContainer implements IClasspathContainer,
		IPlatformConstants {

	IClasspathEntry[] exLibraryEntries;
	ExInstallation exInstallation;

	IJavaProject javaProject = null;

	public ExClasspathContainer(IJavaProject javaProject,
			ExInstallation jbpmInstallation) {
		this.javaProject = javaProject;
		this.exInstallation = jbpmInstallation;

	}

	public IClasspathEntry[] getClasspathEntries() {
		if (exLibraryEntries == null) {
			exLibraryEntries = createJbpmLibraryEntries(javaProject);
		}
		return exLibraryEntries;
	}

	public String getDescription() {
		return "Tansun Workflow Library [" + exInstallation.name + "]";
	}

	public int getKind() {
		return IClasspathContainer.K_APPLICATION;
	}

	public IPath getPath() {
		return new Path("Ex/" + exInstallation.name);
	}

	private IClasspathEntry[] createJbpmLibraryEntries(IJavaProject project) {
		Map jarNames = getJarNames();
		ArrayList entries = new ArrayList();
		Iterator iterator = jarNames.keySet().iterator();
		while (iterator.hasNext()) {
			IPath jarPath = (IPath) iterator.next();
			IPath srcPath = (IPath) jarNames.get(jarPath);
			IPath srcRoot = null;
			entries.add(JavaCore.newLibraryEntry(jarPath, srcPath, srcRoot));
		}
		return (IClasspathEntry[]) entries.toArray(new IClasspathEntry[entries
				.size()]);
	}

	private Map getJarNames() {
		HashMap result = new HashMap();
		Path locationPath = new Path(exInstallation.location);
		try {
			Document document = new SAXReader().read(locationPath.append(
					"src/resources/gpd/version.info.xml").toFile());
			XPath xpath = document
					.createXPath("/jbpm-version-info/classpathentry");
			List list = xpath.selectNodes(document);
			for (int i = 0; i < list.size(); i++) {
				Element entry = (Element) list.get(i);
				IPath sourcePath = null;
				if (entry.attribute("src") != null) {
					sourcePath = locationPath.append((String) entry.attribute(
							"src").getData());
				}
				result.put(locationPath.append((String) entry.attribute("path")
						.getData()), sourcePath);
			}
		} catch (DocumentException e) {
		}
		return result;
	}

}
