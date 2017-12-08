package avicit.ui.runtime.core.action.wizard;

import java.io.InputStream;

import net.java.amateras.uml.UMLPlugin;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

public class NewFunctionStructureWizardPage extends
		WizardNewFileCreationPage {

	public NewFunctionStructureWizardPage(IStructuredSelection selection)
	{
		super("wizardPage", selection);
		setTitle(UMLPlugin.getDefault().getResourceString("wizard.newFunctionStructure.title"));
		setDescription(UMLPlugin.getDefault().getResourceString("wizard.newFunctionStructure.description"));
	}

	public void createControl(Composite parent) {
		super.createControl(parent);
		this.setFileName("newFile.fcd");
	}

	protected InputStream getInitialContents() {
		return null;
	}
}
