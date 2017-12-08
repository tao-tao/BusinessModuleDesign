package avicit.ui.runtime.core.action;


//CreatePackageActionProvider
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.SelectionListenerAction;

import avicit.ui.runtime.core.action.wizard.NewJspWizard;
import avicit.ui.runtime.core.node.AbstractFolderNode;
import avicit.ui.view.navigator.views.AvicitProjectNavigator;
import avicit.ui.view.navigator.views.AvicitProjectViewer;

public class NewJspAction extends SelectionListenerAction {

	public NewJspAction(String text) {
		super(text);
	}

	public void run() {
		Object obj = getStructuredSelection().getFirstElement();
		if (obj == null)
			return;
		final AbstractFolderNode folderNode = (AbstractFolderNode) obj;
		// NewElementWizard wizard = new NewElementWizard();
		NewJspWizard wizard = new NewJspWizard();
		wizard.init(PlatformUI.getWorkbench(), new StructuredSelection(obj));
		WizardDialog dialog = new WizardDialog(Display.getCurrent().getActiveShell(), wizard);
		dialog.create();
		int result = dialog.open();
//		if (result == Dialog.OK) {
//
//		}
//		
		super.run();

		final AvicitProjectViewer viewer = AvicitProjectNavigator.getViewer();
		// if (SwtResourceUtil.isValid(viewer.getTree())) {
		Display display = Display.getDefault();

		if (display != null)
			display.asyncExec(new Runnable() {
				public void run() {
					// viewer.add(folderNode, childNode);
					List<IResource> refreshList = new ArrayList<IResource>();
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