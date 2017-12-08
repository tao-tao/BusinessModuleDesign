package avicit.ui.runtime.core.action.wizard;

import net.java.amateras.uml.UMLPlugin;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import avicit.ui.common.util.WorkbenchUtil;

public class NewFunctionStructureWizard extends Wizard implements
		INewWizard {

	private NewFunctionStructureWizardPage page;
	private IStructuredSelection selection;

	public NewFunctionStructureWizard() {
		super();
	}
	
	public void addPages() {
		page = new NewFunctionStructureWizardPage(selection);
		addPage(page);
	}

	public boolean performFinish() {
		IFile file = page.createNewFile();
		if(file==null){
			return false;
		}
		try {
			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			IDE.openEditor(page, file, true);
		} catch(Exception ex){
			UMLPlugin.logException(ex);
			return false;
		}
		return true;
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}
}
