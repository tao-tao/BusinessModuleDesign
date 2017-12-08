package avicit.ui.runtime.core.action.wizard;


import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.internal.core.JavaProject;
import org.eclipse.jdt.internal.core.PackageFragmentRoot;
import org.eclipse.jdt.internal.ui.wizards.NewElementWizard;
import org.eclipse.jdt.ui.wizards.NewClassWizardPage;
import org.eclipse.jdt.ui.wizards.NewInterfaceWizardPage;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import com.tansun.xpdl.model.pkg.ExternalPackage;

import avicit.ui.runtime.core.node.AbstractFolderNode;

public class NewClassWizard extends NewElementWizard{
	
	private NewClassWizardPage page;
//	private NewInterfaceWizardPage page;
	private ISelection selection;
	private AbstractFolderNode object;
	
	public NewClassWizard(AbstractFolderNode obj) {
		super();
		object = obj;
	}

	public void addPages() {
		super.addPages();
		page = new NewClassWizardPage();

//		IResource resource = object.getResource().getResource();
//		IPackageFragmentRoot root = (IPackageFragmentRoot) resource.getProjectRelativePath();
//
//		page.setPackageFragmentRoot(root, true);
//		page = new NewInterfaceWizardPage();
		page.init((IStructuredSelection) selection);
		addPage(page);
	}

	public boolean performFinish() {
		warnAboutTypeCommentDeprecation();
	    boolean res= super.performFinish();
	    if (res) {
	      IResource resource= page.getModifiedResource();
	      if (resource != null) {
	        selectAndReveal(resource);
//	        if (fOpenEditorOnFinish) {
	          openResource((IFile) resource);
//	        }
	      }
	    }
	    return res;
	}
	
	  @Override
	  protected boolean canRunForked() {
	    return !page.isEnclosingTypeSelected();
	  }

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		super.init(workbench, selection);
		this.selection = selection;
	}

	@Override
	protected void finishPage(IProgressMonitor monitor) throws InterruptedException, CoreException {
		// TODO Auto-generated method stub
		page.createType(monitor);
		
	}

	@Override
	public IJavaElement getCreatedElement() {
		// TODO Auto-generated method stub
		return page.getCreatedType();
	}
}
