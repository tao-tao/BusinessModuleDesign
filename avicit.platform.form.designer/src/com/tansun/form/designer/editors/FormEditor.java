package com.tansun.form.designer.editors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URLEncoder;
import java.net.UnknownHostException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.Div;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.tansun.form.designer.Activator;

/**
 * 
 * Title		: FormEditor.java
 * 类描述       :xxxxxx 
 * 作者         : lidong@avicit.com  
 * 创建时间     : 2015-6-4 下午3:15:56
 * 版本         : 1.00
 * 
 * 修改记录: 
 * 版本            修改人          修改时间                  修改内容描述
 * ----------------------------------------
 * 1.00     xx          2015-6-4 下午3:15:56
 * ----------------------------------------
 */
public class FormEditor extends EditorPart {

	AdvancedBrowser browser = new AdvancedBrowser();
	static int port = getJettyPort();
	static int jspCount = 0;
	static org.eclipse.jetty.server.Server server = new org.eclipse.jetty.server.Server(port);
	
	public FormEditor() {
		super();
	}
	
	public void dispose() {
		super.dispose();
		IFileEditorInput file = (IFileEditorInput)this.getEditorInput();
		if(file != null)
		{
			IFile f = file.getFile();
			try {
				f.refreshLocal(1, null);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		jspCount--;
		stopJettyServer(server);
	}
	
	@Override
	public void doSave(IProgressMonitor arg0) {
	}
	@Override
	public void doSaveAs() {
		
	}
	
	/**
	 * 记录打开几个功能结构建模设计器
	 */
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		this.setInput(input);
		this.setSite(site);
		jspCount++;
	}
	
	@Override
	public boolean isDirty() {
		return false;
	}
	
	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}
	
	@Override
	public void createPartControl(Composite parent) {
		String pathStr = null;
		IFileEditorInput file = (IFileEditorInput) this.getEditorInput();
		if (file != null) {
			String ext = file.getName().substring(
					file.getName().lastIndexOf(".") + 1);
			IFile f = file.getFile();
			String moduleId = file.getName().substring(0, file.getName().lastIndexOf("."));
			Location location = Platform.getInstallLocation();
			String warpath = location.getURL().getPath()
					.concat("dropins/avicitproject");

			System.out.println("####" + server.isStarted());

			if (!server.isStarted())
				startJettyServer(server, warpath);
			if(ext.equalsIgnoreCase("function")){
				pathStr = "http://127.0.0.1:"
						+ port
						+ "/test/avicit/platform6/models/designer/function_module_designer/function_module_designer.jsp?id="+moduleId;
			}else if(ext.equalsIgnoreCase("businessflow")){
				pathStr = "http://127.0.0.1:"
						+ port
						+ "/test/avicit/platform6/models/designer/v2/business_flow_designer.jsp?id="+moduleId;
			}
			

		}
		this.setPartName(file.getName());
		browser.createContents(parent, pathStr);
	}
	@Override
	public void setFocus() {
		
	}
	
	/**
	 * 
	 * 方法名称    :startJettyServer
	 * 功能描述    :启动jetty服务器
	@see
				   :
	@see
	 * 逻辑描述    :
	 * @param   :无
	 * @return  :void
	 * @throws  :无
	 * @since   :Ver 1.00
	 */
	private void startJettyServer(org.eclipse.jetty.server.Server server, String webRoot)
	{
		WebAppContext context = new WebAppContext();
		HashLoginService dummyLoginService = new HashLoginService("TEST-SECURITY-REALM");
		context.getSecurityHandler().setLoginService(dummyLoginService);
		context.setDescriptor(webRoot+"/WEB-INF/web.xml");
		context.setResourceBase(webRoot);
		context.setContextPath("/test");
		server.setHandler(context);
		try {
			server.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @author lidong
	 * @param server
	 */
	private void stopJettyServer(org.eclipse.jetty.server.Server server)
	{
		/**
		 * 当关闭最后一个功能结构建模的时候关掉jetty服务器
		 * 
		 */
		if(!server.isStopped()&&jspCount==0)
			try {
				server.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	/**
	 * 
	 * 方法名称    :getJettyPort
	 * 功能描述    :设置端口
	@see
				   :
	@see
	 * 逻辑描述    :
	 * @param   :无
	 * @return  :int
	 * @throws  :无
	 * @since   :Ver 1.00
	 */
	private static int getJettyPort()
	{
		String ip = "127.0.0.1";
		int port = 10870;
		boolean isAvail = false;
		InetAddress address = null;
		try {
			address = InetAddress.getByName(ip);
		} catch (UnknownHostException e1) {
		}
		if(address!=null)
		{
			do{
				try {
					Socket socket = new Socket(address,port);
					isAvail = true;
					port++;
				} catch (IOException e) {
					isAvail = false;
				}
			} while(isAvail);
		}
		return port;
	}
	
	private String getFileContext(IFile file)
	{
		StringBuilder context = new StringBuilder();
		BufferedReader bis = null;
		try {
			bis = new BufferedReader(new InputStreamReader(new FileInputStream(new File(file.getLocationURI()))));
			String temp = "";
			while ((temp = bis.readLine()) != null) {
			     context.append(temp).append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(bis!=null)
				try {
					bis.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
		}
		return context.toString();
	}

	/*public static void main(String[] args) {//MessageDialog.openInformation(Display.getDefault().getActiveShell(), "提示框", webRoot);
		WebAppContext context = new WebAppContext();
		HashLoginService dummyLoginService = new HashLoginService("TEST-SECURITY-REALM");
		context.getSecurityHandler().setLoginService(dummyLoginService);
		context.setDescriptor("C:/Users/lidong/Desktop/bak/avicit/WEB-INF/web.xml");
		context.setResourceBase("C:/Users/lidong/Desktop/bak/avicit");
		context.setContextPath("/test");
		server.setHandler(context);
		try {
			server.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
}
