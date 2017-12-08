package avicit.ui.runtime.core.action;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.SelectionListenerAction;

import com.jvisio.flowchart.diagram.part.FlowchartCreationWizard;

import avicit.ui.runtime.core.action.wizard.NewFunctionStructureWizard;
import avicit.ui.runtime.core.node.AbstractFolderNode;
import avicit.ui.view.navigator.views.AvicitProjectNavigator;
import avicit.ui.view.navigator.views.AvicitProjectViewer;

/**
 * @author Tao Tao
 *
 */
public class NewFunctionStructureAction extends SelectionListenerAction {

	public NewFunctionStructureAction(String text) {
		super(text);
	}

	public void run() {
		Object obj = getStructuredSelection().getFirstElement();
		if (obj == null)
			return;

		//NewFunctionStructureWizard wizard = new NewFunctionStructureWizard();
		FlowchartCreationWizard wizard = new FlowchartCreationWizard();
		wizard.init(PlatformUI.getWorkbench(), new StructuredSelection(obj));
		WizardDialog dialog = new WizardDialog(Display.getCurrent().getActiveShell(), wizard);
		dialog.create();
		dialog.open();
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
