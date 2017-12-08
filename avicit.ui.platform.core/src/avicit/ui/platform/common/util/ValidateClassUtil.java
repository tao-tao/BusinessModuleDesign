/**
 * ��Ŀ���: ���з�ƽ̨
 * ��������: 2013-1-22
 * ����ʱ��: ����10:40:40
 */ 
package avicit.ui.platform.common.util;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.JavaCore;

import avicit.ui.platform.common.util.ProjectClassLoader;
import avicit.ui.platform.common.util.ProjectFinder;

/** 
 * @�����: ValidateClassUtil.java  
 * @������: 
 * <p>
 * ��֤���Ƿ���ڵĹ���
 * </p>
 * @Methods:
 * @see class:
 * @exception class:
 * @����: user  user@tansun.com.cn  
 * @����ʱ��: 2013-1-22 ����10:40:40
 * @�汾: 1.00
 * 
 * @�޸ļ�¼: 
 * <p>
 * �汾            �޸���          �޸�ʱ��                  �޸���������<br>
 * ----------------------------------------<br>
 * 1.00     user          2013-1-22 ����10:40:40<br>
 * ----------------------------------------<br>
 * </p>
 */
public class ValidateClassUtil {

	/**
	 * ��Ŀ���������Map
	 */
	private static Map<String, ProjectClassLoader> projectClassLoaderMap = new HashMap<String, ProjectClassLoader>();
	
	/**
	 * ��Ŀ���������ˢ��ʱ��
	 */
	private static Map<String, Long> projectRefreshMap = new HashMap<String, Long>();
	
	/**
	 * 
	 * @�������: validateClass
	 * @��������: 
	 * @see class #method: 
	 * @�߼�����: 
	 * @param project
	 * @param className
	 * @return boolean
	 * @throws: ��
	 * @since: Ver 1.00
	 */
	public static boolean validateClass(IProject project, String className) {
		
		ClassLoader classLoader = getClassLoader(project);
		
		if (classLoader == null) {
			return false;
		}
		
		Class clz = null;
		try {
			clz = classLoader.loadClass(className);
			
		} catch (ClassNotFoundException e) {
		}
		
		return (clz != null);
	}
	
	/**
	 * 
	 * @�������: getClassLoader
	 * @��������: ������Ŀ�������
	 * @see class #method: 
	 * @�߼�����: 
	 * @param project
	 * @return ClassLoader
	 * @throws: ��
	 * @since: Ver 1.00
	 */
	private synchronized static ClassLoader getClassLoader(IProject project) {
		
		if (project == null) {
			return null;
		}
		
		boolean needRefresh = false;
		
		ProjectClassLoader pcl = null;
		if (projectClassLoaderMap.containsKey(project.getName())) {
			pcl = projectClassLoaderMap.get(project.getName());
			Long oldTime = projectRefreshMap.get(project.getName());
			Long currTime = System.currentTimeMillis();
			
			long dur = currTime.longValue() - oldTime.longValue();
			
			// ����10��������ˢ��һ��
			if (dur > (1000*60*10)) {
				needRefresh = true;
			}
		} else {
			needRefresh = true;
		}
		
		if (needRefresh) {
			ProjectClassLoader newPcl = null;
			try {
				newPcl = new ProjectClassLoader(
						JavaCore.create(project));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if (newPcl != null) {
				projectClassLoaderMap.put(project.getName(), newPcl);
				projectRefreshMap.put(project.getName(), System.currentTimeMillis());
				
				pcl = newPcl;
			}
		}
		
		return pcl;
	}
    public synchronized static ClassLoader forceGetClassLoader(IProject project) {
		
		if (project == null) {
			return null;
		}
		
		boolean needRefresh = false;
		
		ProjectClassLoader pcl = null;
		if (projectClassLoaderMap.containsKey(project.getName())) {
			pcl = projectClassLoaderMap.get(project.getName());
			Long oldTime = projectRefreshMap.get(project.getName());
			Long currTime = System.currentTimeMillis();
			
			long dur = currTime.longValue() - oldTime.longValue();
			
			// ����10��������ˢ��һ��
			if (dur > (1000*60*10)) {
				needRefresh = true;
			}
		} else {
			needRefresh = true;
		}
		
		if (true) {
			ProjectClassLoader newPcl = null;
			try {
				newPcl = new ProjectClassLoader(
						JavaCore.create(project));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if (newPcl != null) {
				projectClassLoaderMap.put(project.getName(), newPcl);
				projectRefreshMap.put(project.getName(), System.currentTimeMillis());
				
				pcl = newPcl;
			}
		}
		
		return pcl;
	}
    
    public synchronized static void closeProjectClassLoader(IProject project) {
    	ProjectClassLoader pcl = null;
		if (projectClassLoaderMap.containsKey(project.getName())) {
			pcl = projectClassLoaderMap.remove(project.getName());
			pcl.close();
		}
    }
}
