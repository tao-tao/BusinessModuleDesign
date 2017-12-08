package avicit.ui.runtime.core.requirement.analysis.usecase;

import net.java.amateras.uml.usecasediagram.wizard.NewUsecaseDiagramWizard;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.SelectionListenerAction;

public class FunctionUseCaseAction extends SelectionListenerAction {

	protected FunctionUseCaseAction(String text) {
		super(text);
	}

	public void run() {
		Object obj = getStructuredSelection().getFirstElement();
		if (obj == null)
			return;
		NewUsecaseDiagramWizard wizard = new NewUsecaseDiagramWizard();
		wizard.init(PlatformUI.getWorkbench(), new StructuredSelection(obj));
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
