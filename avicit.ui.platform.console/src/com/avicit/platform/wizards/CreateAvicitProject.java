package com.avicit.platform.wizards;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.ui.util.CoreUtility;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;

public class CreateAvicitProject extends Wizard implements INewWizard {

	
	private AvicitNewProjectWizard mainPage;
	
	private IWorkbench fWorkbench;
	
	IStructuredSelection selection;
	
	private String contentPath;
	
	public CreateAvicitProject(){
		mainPage = new AvicitNewProjectWizard("基础工程");
		
	}
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		setNeedsProgressMonitor(true);
		this.fWorkbench = workbench;
		this.selection = selection;
		setWindowTitle("新建工程");
	}

	public boolean performFinish() {
		final IProject project = mainPage.getProjectHandle();
		final IPath path = mainPage.getLocationPath();
		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor)
					throws InvocationTargetException {
				try {

					createProjet(project, path, monitor);

					project.close(monitor);
					project.open(monitor);
				} catch (Exception e) {
					e.printStackTrace();
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();

				}
			}

		};
		try {
			getContainer().run(true, false, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(), "Error",
					realException.getMessage());
			return false;
		}

		BasicNewResourceWizard.selectAndReveal(project.getProject(),
				fWorkbench.getActiveWorkbenchWindow());
		return true;
	}

	public void addPages() {
		
		mainPage.setTitle("金航数码");
		mainPage.setDescription("新建工程向导");
		addPage(mainPage);
	}

	private void createProjet(IProject project, IPath path,
			IProgressMonitor monitor) throws CoreException {
		// 建立项目
		monitor.beginTask("Creating " + project.getName(), 2);

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		final IProjectDescription description = workspace
				.newProjectDescription(project.getName());
		description.setLocation(project.getLocation());
		project.create(description, monitor);
		project.open(monitor);
		//2015-03-12
		monitor.beginTask("复制模板", 100);
		try {
			copyDemoTemplet(project, monitor);
		} catch (FileNotFoundException e1) {
			System.err.println("复制模板错误:" + e1.getMessage());
		}

		// addNature(project,new SubProgressMonitor(monitor, 10));
		monitor.beginTask("Creating .project config", 2);
		IFile pfile = project.getFile("/.project");
		try {
			InputStream stream = addProjectFile(project);
			if (pfile.exists()) {
				pfile.setContents(stream, true, true, monitor);
			} else {
				pfile.create(stream, true, monitor);
			}
			stream.close();
		} catch (IOException e) {
			System.err.println("create classespath error:" + e.getMessage());
		}
		IFile cfile;
		// 生成项目classpath文件
		monitor.beginTask("Creating classpath config", 2);
		cfile = project.getFile(".classpath");
		try {
			InputStream stream = openClassContentStream(project, "avicitdata");
			if (cfile.exists()) {
				cfile.setContents(stream, true, true, monitor);
			} else {
				cfile.create(stream, true, monitor);
			}
			stream.close();
		} catch (IOException e) {
			System.err.println("create classespath error:" + e.getMessage());
		}
	
		// 生成项目metadata
		monitor.beginTask("Creating .mymetadata config", 2);
		cfile = project.getFile("/.mymetadata");
		try {
			InputStream stream = addMetaDataFile(project);
			if (cfile.exists()) {
				cfile.setContents(stream, true, true, monitor);
			} else {
				cfile.create(stream, true, monitor);
			}
			stream.close();
		} catch (IOException e) {
			System.err.println("create classespath error:" + e.getMessage());
		}
		// 生成项目文件
		monitor.beginTask("Creating .pada config", 2);

		createData(project);
		//创建.classPath文件后，清空了class目录 2015-03-12
		//在创建了.classPath文件后再复制模板
		
		monitor.beginTask("复制模板", 100);
		try {
			//保证ResourceMapper删除完以后再执行，等待1000ms
			Thread.sleep(1000);	
			copyDemoTemplet(project, monitor);
		} catch (FileNotFoundException e1) {
			System.err.println("复制模板错误:" + e1.getMessage());
		} catch (InterruptedException e) {
			System.err.println("复制模板等待线程错误:" + e.getMessage());	
		}			

	}
	private InputStream addProjectFile(IProject project) {
		StringBuffer str = new StringBuffer();
		str.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<projectDescription>" + "<name>"
				+ project.getName()
				+ "</name>"
				+ "<comment></comment>"
				+ "<projects></projects>"
				+ "	<buildSpec>"
				+ "     <buildCommand>"
				+ "			<name>com.genuitec.eclipse.j2eedt.core.WebClasspathBuilder</name>"
				+ "			<arguments>"
				+ "			</arguments>"
				+ "		</buildCommand>"
				+ "		<buildCommand>"
				+ "			<name>org.eclipse.jdt.core.javabuilder</name>"
				+ "			<arguments>"
				+ "			</arguments>"
				+ "		</buildCommand>"
				+ "		<buildCommand>"
				+ "			<name>com.genuitec.eclipse.j2eedt.core.J2EEProjectValidator</name>"
				+ "			<arguments>"
				+ "			</arguments>"
				+ "		</buildCommand>"
				+ "		<buildCommand>"
				+ "			<name>com.genuitec.eclipse.j2eedt.core.DeploymentDescriptorValidator</name>"
				+ "			<arguments>"
				+ "			</arguments>"
				+ "		</buildCommand>"
				+ "     <buildCommand>"
				+ "          <name>com.tansun.ui.navigator.ecBuilder</name>"
				+ "          <arguments></arguments>"
				+ "     </buildCommand>"
				+ "	</buildSpec>"
				+ "	<natures>"
				+ "     <nature>com.avicit.ui.navigator.avicitNature</nature>"
				+ "		<nature>com.genuitec.eclipse.j2eedt.core.webnature</nature>"
				+ "		<nature>org.eclipse.jdt.core.javanature</nature>"
				+ "	</natures>" + "</projectDescription>");
		return new ByteArrayInputStream(str.toString().getBytes());
	}
	public InputStream addMetaDataFile(IProject project) {
		StringBuffer str = new StringBuffer();
		str.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<project-module type=\"WEB\"  name=\"" + project.getName()
				+ "\"" + "  id=\"myeclipse." + System.currentTimeMillis()
				+ "\" " + "  context-root=\"/" + project.getName() + "\""
				+ "  j2ee-spec=\"5.0\" " + "  archive=\"" + project.getName()
				+ ".war\">" + "  <attributes>"
				+ "    <attribute name=\"webrootdir\" value=\"/WebRoot\" />"
				+ "  </attributes>" + "</project-module>");
		return new ByteArrayInputStream(str.toString().getBytes());
	}

	public InputStream addMetaDataFileTxt(IProject project) {
		StringBuffer str = new StringBuffer();
		str.append("Manager Project");
		return new ByteArrayInputStream(str.toString().getBytes());
	}
	public void createData(IProject obj) {
		IProject project = (IProject) obj;
		IJavaProject jp = JavaCore.create(project);
		IFolder folder = project.getFolder("WebRoot");
		try {
			if (!folder.exists())
				folder.create(true, true, null);
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public InputStream openClassContentStream(IProject project, String path) {
		StringBuffer str = new StringBuffer();
		str.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		str.append("<classpath>\n");
		IPath ipath = formatPluginPath(PlatformPlugin.getPluginOSPath()).append(path);
		String s = ipath.toString();
//		s = s.substring(0, s.lastIndexOf('!')) + "file/"+path;
//		s = s.substring(s.indexOf('/')+1);
//		this.contentPath = s;
//		System.out.println("current project path is "+ s);
		File file = new File(s);
		String[] files = file.list();
		for (int i = 0; i < files.length; i++) {
			File dir = new File(s + "/" + files[i]);
			if (dir.isDirectory() && dir.getName().startsWith("src"))
				str.append("	<classpathentry kind=\"src\" path=\""
						+ dir.getName() + "\"/>\n");
		}

		str.append("	<classpathentry kind=\"con\" path=\"org.eclipse.jdt.launching.JRE_CONTAINER\"/>\n");
		str.append("	<classpathentry kind=\"con\" path=\"org.eclipse.jst.j2ee.internal.web.container\"/>\n");
		str.append("	<classpathentry kind=\"con\" path=\"org.eclipse.jst.j2ee.internal.module.container\"/>\n");
		str.append("	<classpathentry kind=\"con\" path=\"avicit.platform.config.COMP_CONTAINER/-\"/>\n");
//		str.append("	<classpathentry kind=\"con\" path=\"avicit.platform.config.COMP_CONTAINER\"/>\n");

		String[] lib = WebBuilder.getFileName(project.getLocation().append(
				"/WebRoot/WEB-INF/lib"));
		System.out.println("xxxxx  "+project.getLocation().append(
				"/WebRoot/WEB-INF/lib").toString());
		for (int i = 0; i < lib.length; i++) {
			if (lib[i].endsWith(".jar"))
				str.append("	<classpathentry kind=\"lib\" path=\"WebRoot/WEB-INF/lib/"
						+ lib[i] + "\"/>\n");
		}
		
		str.append("	<classpathentry kind=\"output\" path=\"WebRoot/WEB-INF/classes\"/>\n");

		str.append("</classpath>");
		return new ByteArrayInputStream(str.toString().getBytes());
	}
	public void copyDemoTemplet(IProject project,
			IProgressMonitor iprogressmonitor) throws CoreException,
			FileNotFoundException {
			IPath ipath = formatPluginPath(PlatformPlugin.getPluginOSPath()).append("avicitdata");
			String s = ipath.toString();
//			s = s.substring(0, s.lastIndexOf('!')) + "file/"+"avicitdata";
//			s = s.substring(s.indexOf('/')+1);
			this.contentPath = s;		
			copyDirData(project, getData(new Path(s)), iprogressmonitor, "avicitdata");
			
		

	}
	public void copyDirData(IProject project, String as[],
			IProgressMonitor iprogressmonitor, String path)
			throws CoreException {
		for (int i = 0; i < as.length; i++) {
			String s = as[i];
			//debug 2015
//			if(s.equals("D:/Work/OK_workspace/integrated_model/03Develope/businessModule_design/avicit.ui.platform.console/avicitdata/WebRoot/WEB-INF")){
//				System.out.println("Catched!UUUUUUUUUUUUUUUUUUUUUUUUUUUU");
//			}
			File tmpfile = new File(s);
			String s1 = tmpfile.getName();
			if (s1 != null && s1.lastIndexOf(".svn") > -1) {
				continue;
			}
			
			String sourcefolder = s.substring(s.indexOf(path) + path.length(),
					s.lastIndexOf("/"));

			if (tmpfile.isDirectory()) {
				IFolder ifolder = project.getProject().getFolder(sourcefolder + "/" + s1);
				CoreUtility.createFolder(ifolder, false, true,
						new SubProgressMonitor(iprogressmonitor, 10));

				IPath dir = formatPluginPath(PlatformPlugin.getPluginOSPath()).append(path)
						.append(sourcefolder + "/" + s1);

				copyDirData(project, getData(dir), iprogressmonitor, path);
			} else {
				IFile file ;
				if(!sourcefolder.equals("")){
					IPath targetDir = project.getFolder(sourcefolder).getFullPath();
					if (!fileExist(project, targetDir)) {
						IFolder f = null;
						if(!sourcefolder.equals("")){
						    f = project.getFolder(sourcefolder);
						}
						
						CoreUtility.createFolder(f, false, true,
								new SubProgressMonitor(iprogressmonitor, 10));
					}
					file = project.getFile(targetDir.removeFirstSegments(1)
							.append(s1));
				}else{
					file = project.getFile(s1);
				}
				
					writeNormal(file, iprogressmonitor, s);
				
			}
		}
	}
	public boolean fileExist(Object obj, Object obj1) {
		if (obj1 == null)
			return false;
		if (!(obj instanceof IProject))
			throw new IllegalArgumentException(
					"project must be an instance of org.eclipse.core.resources.IProjec");
		if (!(obj1 instanceof IPath))
			throw new IllegalArgumentException(
					"file must be an instance of org.eclipse.core.runtime.IPath");
		IProject iproject = (IProject) obj;
		Object obj2 = obj1;
		if (((IPath) (obj2)).isAbsolute())
			obj2 = new Path("./" + obj2);
		return iproject.exists(((IPath) (obj2)));
	}
	public void writeNormal(IFile file, IProgressMonitor iprogressmonitor,
			String s) {
		if (!file.exists()) {

			InputStream input;
			try {
				input = new FileInputStream(s);
				try {
					file.create(input, true, iprogressmonitor);
				} catch (CoreException e) {
					e.printStackTrace();
				}
				input.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
	
	public static IPath formatPluginPath (IPath pluginPath){
		String path = pluginPath.toString();
		if(null!=path && path.indexOf("file:")!=-1){
			path = path.substring(path.indexOf('/')+1);
		}
		IPath formatedPath = new Path(path);
		return formatedPath;
		
	}

	public String[] getData(IPath ipath) {
		String s = ipath.toString();
		File file = null;
		if (s != null && s.trim().length() > 0) {
			file = new File(s);
			if (!file.isDirectory())
				return new String[0];
			file = file.getAbsoluteFile();
		}
		String as[] = file.list();
		ArrayList arraylist = new ArrayList(as.length);
		for (int i = 0; i < as.length; i++) {
			IPath ipath1 = ipath.append(as[i]);
			arraylist.add(ipath1.toString());
		}

		return (String[]) arraylist.toArray(new String[as.length]);
	}

}
