package avicit.ui.runtime.core.action.wizard;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import avicit.ui.runtime.core.node.AbstractNode;
import avicit.ui.runtime.core.requirement.requirement.document.DocumentNode;

/**
 * @author Tao Tao
 *
 */
public class ExportDocumentWizard extends Wizard implements INewWizard {
	private ExportDocumentWizardPage page;
	private IStructuredSelection selection;

	@Override
	public void addPages() {
		IStructuredSelection selection1 = new StructuredSelection(((AbstractNode)selection.getFirstElement()).getResource().getResource());
		page = new ExportDocumentWizardPage("Export Document Wizard Page", selection1);
		addPage(page);
		DocumentNode node = (DocumentNode)selection.getFirstElement();
		IResource resource = node.getResource().getResource();
		IStructuredSelection selection2 = new StructuredSelection(resource);
//		WizardFileSystemResourceExportPage1 page1 = new WizardFileSystemResourceExportPage1(selection1);
//		WizardFileSystemResourceExportPage1 page2 = new WizardFileSystemResourceExportPage1(selection1);
//		addPage(page1);
//		addPage(page2);
		super.addPages();
	}

	@Override
	public boolean performFinish() {
		return false;
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection=selection;
	}
}
