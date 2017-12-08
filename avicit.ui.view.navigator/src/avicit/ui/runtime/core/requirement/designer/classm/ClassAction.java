package avicit.ui.runtime.core.requirement.designer.classm;


//CreatePackageActionProvider
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.internal.ui.wizards.NewClassCreationWizard;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.SelectionListenerAction;

import avicit.ui.runtime.core.node.AbstractFolderNode;
import avicit.ui.view.navigator.views.AvicitProjectNavigator;
import avicit.ui.view.navigator.views.AvicitProjectViewer;

/**
 * @author Tao Tao
 *
 */
@SuppressWarnings("restriction")
public class ClassAction extends SelectionListenerAction {

	public ClassAction(String text) {
		super(text);
	}

	public void run() {
		Object obj = getStructuredSelection().getFirstElement();

		if (obj == null)
			return;

		NewClassCreationWizard wizard = new NewClassCreationWizard();
		List<?> selections = getSelectedResources();
		wizard.init(PlatformUI.getWorkbench(), new StructuredSelection(selections));
		WizardDialog dialog = new WizardDialog(Display.getCurrent().getActiveShell(), wizard);
		dialog.create();
		int result = dialog.open();
		// if (result == Dialog.OK) {
		// }
		super.run();
		final AvicitProjectViewer viewer = AvicitProjectNavigator.getViewer();
		// if (SwtResourceUtil.isValid(viewer.getTree())) {

		Display display = Display.getDefault();
		final AbstractFolderNode folderNode = (AbstractFolderNode) obj;

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