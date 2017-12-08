package avicit.ui.view.create;


import java.io.ByteArrayInputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

public class CreateActionUtil {
	
	public static void DeleteXML(String compName,IProject project,String path){
		IFolder folder = project.getFolder(path);
		//IFolder metaf = folder.getFolder("META-INF");
		try {
			IFile file = folder.getFile("META-INF/component-config.xml");
			IFile file2 = folder.getFile("META-INF/component-ext-config.xml");
			if (file != null&&file.exists()) {
				file.delete(true, null);
			}
			if(file2 != null&&file2.exists()){
				file2.delete(true, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 
	 * @方法名称: createXML
	 * @功能描述: 创建component-config.xml文件
	 * @逻辑描述: 
	 * @param compName 构件名称，project 当前工程，path 当前模块路径
	 * @return 无
	 * @throws: IOException
	 * @since: Ver 1.00
	 */
	public static void createXML(String compName,String desc,IProject project,String path){
		IFolder folder = project.getFolder(path);
		IFolder metaf = folder.getFolder("META-INF");
		try {
		
			IFile file = folder.getFile("META-INF/component-config.xml");
			if (!file.exists()) {
				file.create(
						new ByteArrayInputStream(appendStr(compName,desc).getBytes("UTF-8")), true, null);
				file.setCharset("UTF-8", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @方法名称: createComponentExtXML
	 * @功能描述: 创建component-ext-config.xml文件
	 * @逻辑描述: 
	 * @param compName 构件名称，project 当前工程，path 当前模块路径
	 * @return 无
	 * @throws: IOException
	 * @since: Ver 1.00
	 */
	public static void createComponentExtXML(String compName,IProject project,String path){
		IFolder folder = project.getFolder(path);
		//IFolder metaf = folder.getFolder("META-INF");
		try {
		
			IFile file = folder.getFile("META-INF/component-ext-config.xml");
			if (!file.exists()) {
				file.create(
						new ByteArrayInputStream(initComponentExtXML(compName).getBytes("UTF-8")), true, null);
				file.setCharset("UTF-8", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @方法名称: appendStr
	 * @功能描述：初始化component-config.xml文件
	 * @逻辑描述: 
	 * @param compName 构件名称
	 * @return String
	 * @throws: 无
	 * @since: Ver 1.00
	 */
	private static String appendStr(String comName,String desc){
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append("\n");
		sb.append("<component desc=\"").append(desc).append("\" name=\"");
		sb.append(comName);
		sb.append("\" type=\"3\" version=\"1.0\"/>");
		
		return sb.toString();
	}
	/**
	 * 
	 * @方法名称: initComponentExtXML
	 * @功能描述：初始化component-ext-config.xml文件
	 * @逻辑描述: 
	 * @param compName 构件名称
	 * @return String
	 * @throws: 无
	 * @since: Ver 1.00
	 */
	private static String initComponentExtXML(String comName){
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append("\n");
		sb.append("<component name=\"");
		sb.append(comName);
		sb.append("\">\n");
		sb.append("<!-- 页面流控制类替换与页面标签显隐 -->\n");
		sb.append("<!-- path是页面流的访问路径，如果有替换的action类，写在actionclass中-->\n");
		sb.append("<!-- \n");
		sb.append("<pagex path=\"\" actionclass=\"\"> \n");
		sb.append("		fieldcontrol是页面控件控制，每个节点一个，nodeId是环节ID \n");
		sb.append("		<fieldcontrol nodeId=\"\"> \n");
		sb.append("			每个field对应界面一个空间，name是控件名称，" +
				"visiable是是否显示disabled是否可用，readonly是否只读\n");
		sb.append("			<field name=\"\" visiable=\"\" disabled=\"\" readonly=\"\"></field>\n");
		sb.append("		</fieldcontrol>\n");
		sb.append("</pagex>\n");
		sb.append("-->\n");
		
		sb.append("<!-- biz环节filter和预留空环节 -->\n");
		sb.append("<!-- path是biz流的访问路径-->\n");
		sb.append("<!--\n");
		sb.append("<biz path=\"\">\n");
		sb.append("		bizfilters每个环节一个，nodeId是环节ID\n");
		sb.append("		<bizfilters nodeId=\"\">\n");
		sb.append("		filter具有执行顺序，因此用filterlist\n");
		sb.append("		<filterlist>\n");
		sb.append("			每个filter一个，filterclass是类名\n");
		sb.append("			<filter filterclass=\"\">\n");
		sb.append("			</filter>\n");
		sb.append("		</filterlist>\n");
		sb.append("		</bizfilters>\n");
		sb.append("   biz预留空环节指定实现规定接口的类的方法\n");
		sb.append("		<biznode nodeId=\"\" beanId=\"\">\n");
		sb.append("		</biznode>\n");
		sb.append("</biz>\n");
		sb.append("-->\n");
		sb.append("</component>");
		
		return sb.toString();
	}
}
