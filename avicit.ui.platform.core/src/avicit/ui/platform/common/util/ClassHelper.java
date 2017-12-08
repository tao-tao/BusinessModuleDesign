package avicit.ui.platform.common.util;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

public final class ClassHelper {

	static Map classMap = new HashMap();

	/**
	 * get the <code>ClassLoader</code> of java project specified.
	 * 
	 * @param project
	 *            <code>IJavaProject</code>
	 * @return <code>ClassLoader</code> of java project
	 * @throws CoreException
	 * @throws MalformedURLException
	 */
	public static ProjectClassLoader getProjectClassLoader(IJavaProject project)
			throws CoreException, MalformedURLException {
		ProjectClassLoader cla = null;//(ProjectClassLoader) classMap.get(project);
		if (cla != null)
			return cla;
		cla = new ProjectClassLoader(project);
//		classMap.put(project, cla);
		return cla;
	}

	public static ProjectClassLoader removeProjectClassLoader(IJavaProject project){
		return (ProjectClassLoader) classMap.remove(project);
	}

	/**
	 * load <code>Class</code> in java project
	 * 
	 * @param project
	 *            <code>IJavaProject</code>
	 * @param className
	 *            name of class to load
	 * @return <code>Class</code>
	 * @throws ClassNotFoundException
	 * @throws CoreException
	 * @throws MalformedURLException
	 */
	public static Class loadClass(IJavaProject project, String className)
			throws CoreException, ClassNotFoundException, MalformedURLException {
		ClassLoader loader = getProjectClassLoader(project);
		try{
			Class clazz = loader.loadClass(className);
			loader = null;
			return clazz;
		} catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * transform the <code>IType</code> to <code>Class</code>
	 * 
	 * @param type
	 *            <code>IType</code>
	 * @return <code>Class</code>
	 * @throws ClassNotFoundException
	 * @throws MalformedURLException
	 */
	public static Class typeToClass(IType type) throws CoreException,
			ClassNotFoundException, MalformedURLException {
		try {
			if (null != type && (type.isClass() || type.isInterface())) {
				String className = type.getFullyQualifiedName('$');
				return loadClass(type.getJavaProject(), className);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
