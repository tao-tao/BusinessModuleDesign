package avicit.ui.runtime.core.action.wizard;

import java.io.ByteArrayInputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.internal.resources.Project;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.internal.ui.util.CoreUtility;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import avicit.platform6.tools.codegeneration.CodeGenerationActivator;
import avicit.ui.core.runtime.resource.EclipseFolderDelegate;
import avicit.ui.core.runtime.resource.IFolderDelegate;
import avicit.ui.runtime.core.INode;
import avicit.ui.runtime.core.action.NewSubSystemAction;
import avicit.ui.runtime.core.node.AbstractFolderNode;
import avicit.ui.runtime.core.subsystem.SubSystemNode;
import avicit.ui.view.navigator.Activator;


public class NewSubSystemWizard extends Wizard implements INewWizard {

	private NewSubSystemWizardPage newItemWizardPage;

	private IStructuredSelection selection;
	AbstractFolderNode node;
	Project obj;
	SubSystemNode child;

	public NewSubSystemWizard() {
		super();
		setNeedsProgressMonitor(true);
		try {
			URL prefix = new URL(Activator.getDefault().getDescriptor().getInstallURL(), "icons/wizban/");
			ImageDescriptor id = ImageDescriptor.createFromURL(new URL(prefix, "package.gif"));
			setDefaultPageImageDescriptor(id);
		}catch (MalformedURLException e) {}
	}

	public void init(IWorkbench arg0,Project obj) {
		this.selection = selection;
		node = (AbstractFolderNode) selection;
		this.obj = obj;
		if(null==obj){
			System.out.println("========================NNNNNNNNNN");
		}
	}

	@Override
	public void addPages() {
		newItemWizardPage = new NewSubSystemWizardPage(selection,obj);
		addPage(newItemWizardPage);
	}

	@Override
	public boolean performFinish() {
		String packName = newItemWizardPage.fetchData();
		packName = packName.trim();
		String pathName = packName.replace(".", "/");
		IFolderDelegate folder;
		try {
//			folder = node.getResource().getProject().getFolder(node.getConfigPath() + "/" + pathName);
//			if (!folder.exists()) {
//				folder.create();
//			}
			if(obj instanceof IProject){

				IProject project = (IProject) obj;
//				IJavaProject jp = JavaCore.create(project);
				//2013-03-09
//				IFolder ifolder = project.getFolder("src-common").getFolder(pathName);
				IFolder ifolder = project.getFolder(CodeGenerationActivator.SOURCE_FOLDER_PATH).getFolder(pathName);
				IFolder webFolder = project.getFolder("WebRoot").getFolder(pathName);
				if (!(ifolder.exists())) {
					try {
						CoreUtility.createFolder(ifolder, true, true, null);
						CoreUtility.createFolder(webFolder, true, true, null);	
					}catch(CoreException e){
						e.printStackTrace();
						}
					}
				
				IFile file = ifolder.getFile(INode.SUBSYSTEM_DESC);
				if (!file.exists()) {
					file.create(
							new ByteArrayInputStream(NewSubSystemAction.createInitial(pathName,
									"subsystem").getBytes("UTF-8")),
							true, null);
					file.setCharset("UTF-8", null);
				}
//				IFolder folder = ifolder.getFolder("META-INF");
//				IFolder metaf = ifolder.getFolder("META-INF");
//				if (!metaf.exists())
//					metaf.create(true, true, null);	
				
				child = new SubSystemNode(new EclipseFolderDelegate(ifolder));
			}
//			child = new PackageNode(packName, (IFolder) folder.getResource(), new ArrayList());

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "异常", "出现异常" + e.getClass().getSimpleName() + ":\n详细信息:" + e.getLocalizedMessage());
		}
		return false;
	}

	public SubSystemNode getChild() {
		return child;
	}

	@Override
	public void init(IWorkbench arg0, IStructuredSelection selection) {
		this.selection = selection;
//		node = (AbstractFolderNode) selection.getFirstElement();
		this.obj = (Project) selection.getFirstElement();
		this.obj = obj;
		if(null==obj){
		}	
		
	}
}
