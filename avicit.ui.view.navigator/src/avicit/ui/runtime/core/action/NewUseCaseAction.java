package avicit.ui.runtime.core.action;


import java.util.ArrayList;
import java.util.List;

import net.java.amateras.uml.usecasediagram.wizard.NewUsecaseDiagramWizard;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.SelectionListenerAction;

import avicit.ui.runtime.core.node.AbstractFolderNode;
import avicit.ui.view.navigator.views.AvicitProjectNavigator;
import avicit.ui.view.navigator.views.AvicitProjectViewer;

public class NewUseCaseAction extends SelectionListenerAction{

	public NewUseCaseAction(String text) {
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
//		if (result == Dialog.OK) {
//			
//		}
		super.run();

		final AvicitProjectViewer viewer = AvicitProjectNavigator.getViewer();
//		if (SwtResourceUtil.isValid(viewer.getTree())) {
		final AbstractFolderNode folderNode = (AbstractFolderNode) obj;
		Display display = Display.getDefault();

		if (display != null)
			display.asyncExec(new Runnable() {
				public void run() {
					// viewer.add(folderNode, childNode);
					List refreshList = new ArrayList();
					refreshList.add(folderNode.getResource().getResource());
					// refreshList.add(childNode.getResource().getResource());
					viewer.getResourceMapper().refresh(false, refreshList, refreshList, 500);
				}
			});
	}

	protected boolean updateSelection(IStructuredSelection selection) {
		boolean flag = super.updateSelection(selection);
		if (flag)
			flag = getStructuredSelection().toList().size() == 1;
		return flag;
	}

}
