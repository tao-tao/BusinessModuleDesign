package avicit.ui.runtime.core.action;

import org.eclipse.core.resources.IFolder;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.SelectionListenerAction;
import org.eclipse.ui.internal.dialogs.ImportWizard;

import avicit.ui.runtime.core.cluster.function.ComponentNode;

public class ImportAction extends SelectionListenerAction{

	public ImportAction(String text) {
		super(text);
	}

	public void run() {
		Object obj = getStructuredSelection().getFirstElement();
		if (obj == null)
			return;
		/*
		ZipFileImportWizard wizard = new ZipFileImportWizard();
		*/
		ImportWizard wizard = new ImportWizard();
		ComponentNode node = ((ComponentNode)obj);
		IFolder folder = (IFolder) node.getResource().getResource();
		wizard.init(PlatformUI.getWorkbench(), new StructuredSelection(folder));
		WizardDialog dialog = new WizardDialog(Display.getCurrent().getActiveShell(), wizard);
		dialog.create();
		int result = dialog.open();
		if (result == Dialog.OK) {
			
		}
		super.run();
	}

	protected boolean updateSelection(IStructuredSelection selection) {
		boolean flag = super.updateSelection(selection);
		if (flag)
			flag = getStructuredSelection().toList().size() == 1;
		return flag;
	}

}
