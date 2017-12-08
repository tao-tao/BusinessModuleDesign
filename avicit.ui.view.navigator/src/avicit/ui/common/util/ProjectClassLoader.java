package avicit.ui.common.util;


import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarFile;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.launching.JavaRuntime;

public class ProjectClassLoader extends URLClassLoader {

	public ProjectClassLoader(IJavaProject project) throws MalformedURLException, CoreException {
		super(getURLSFromProject(project, null), Thread.currentThread().getContextClassLoader());
	}

	ProjectClassLoader(IJavaProject project, URL[] extraUrls) throws MalformedURLException, CoreException {
		super(getURLSFromProject(project, extraUrls), Thread.currentThread().getContextClassLoader());
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		return super.findClass(name);
	}

	public static URL[] getURLSFromProject(IJavaProject project, URL[] extraUrls) throws CoreException, MalformedURLException {
		String[] classPaths = JavaRuntime.computeDefaultRuntimeClassPath(project);

		URL[] urls = new URL[classPaths.length];
		for (int i = 0; i < classPaths.length; i++) {
			urls[i] = computeForURLClassLoader(classPaths[i]);
		}
		return urls;
		/*
		 * List list = new ArrayList(); if (null != extraUrls) { for (int i = 0;
		 * i < extraUrls.length; i++) { list.add(extraUrls[i]); } }
		 * 
		 * IClasspathEntry[] entries = project.getRawClasspath();
		 * entries[0].getPath();
		 * 
		 * IPackageFragmentRoot[] roots = project.getAllPackageFragmentRoots();
		 * String installLoc =
		 * ResourcesPlugin.getWorkspace().getRoot().getLocation().toFile().getAbsolutePath();
		 * installLoc = installLoc.replace('\\', '/'); if
		 * (installLoc.endsWith("/")) installLoc = installLoc.substring(0,
		 * installLoc.length() - 1);
		 * 
		 * for (int i = 0; i < roots.length; i++) { try { if
		 * (roots[i].isArchive()) { File f = new
		 * File(FileLocator.resolve(roots[i].getPath().makeAbsolute().toFile().toURL()).getFile());
		 * if (f.exists()) {
		 * list.add(FileLocator.resolve(roots[i].getPath().makeAbsolute().toFile().toURL())); }
		 * else { String s = roots[i].getPath().toOSString().replace('\\', '/');
		 * if (!s.startsWith("/")) s = "/" + s; f = new File(installLoc + s); if
		 * (f.exists()) { list.add(f.toURL()); } else { f = new File("c:" +
		 * installLoc + s); if (f.exists()) { list.add(f.toURL()); } else { f =
		 * new File("d:" + installLoc + s); if (f.exists()) {
		 * list.add(f.toURL()); } } } } } else { IPath path =
		 * roots[i].getJavaProject().getOutputLocation(); if
		 * (path.segmentCount() > 1) { IWorkspaceRoot root =
		 * ResourcesPlugin.getWorkspace().getRoot(); path =
		 * root.getFolder(path).getLocation(); if
		 * (!list.contains(path.toFile().toURL()))
		 * list.add(path.toFile().toURL()); } else { path =
		 * roots[i].getJavaProject().getProject().getLocation(); if
		 * (!list.contains(path.toFile().toURL()))
		 * list.add(path.toFile().toURL()); } } } catch (Exception e) { } }
		 * 
		 * URL[] urls = new URL[list.size()]; int index = 0; for (Iterator i =
		 * list.iterator(); i.hasNext(); index++) { urls[index] = (URL)
		 * i.next(); } return urls;
		 */
	}

//	private static final String PROTOCAL_PREFIX = "file:////";

	private static URL computeForURLClassLoader(String classpath) {
		if (!classpath.endsWith("\\")) {
			File file = new File(classpath);
			if (file.exists() && file.isDirectory()) {
				classpath = classpath.concat("\\");
			}
			try {
				return file.toURL();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public void close() {
		URL[] urlList = this.getURLs();
		
		if (urlList == null || urlList.length == 0) {
			return;
		}
		
		try {
			// 查找URLClassLoader中的URLClassPath对象ucp
			Object ucpObj = null;
			Field ucpField = URLClassLoader.class.getDeclaredField("ucp");
			ucpField.setAccessible(true);
			ucpObj = ucpField.get(this);
			for (int i = 0; i < urlList.length; i++) {
				URL url = urlList[i];
				// 获取ucp内部的jarLoader
				Method m = ucpObj.getClass().getDeclaredMethod("getLoader", int.class);
				m.setAccessible(true);
				Object jarLoader = m.invoke(ucpObj, i);
				String clsName = jarLoader.getClass().getName();
				if (clsName.indexOf("JarLoader") != -1) {
					m = jarLoader.getClass().getDeclaredMethod("ensureOpen");
					m.setAccessible(true);
					m.invoke(jarLoader);
					m = jarLoader.getClass().getDeclaredMethod("getJarFile");
					m.setAccessible(true);
					JarFile jf = (JarFile)m.invoke(jarLoader);
					// 释放jarLoader中的jarFile
					jf.close();
					//System.out.println("release jar:" + jf.getName());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}