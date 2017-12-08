package avicit.ui.runtime.core.action;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.SelectionListenerAction;

import avicit.ui.runtime.core.action.wizard.NewPackageWizard;
import avicit.ui.runtime.core.node.AbstractFolderNode;
import avicit.ui.runtime.core.node.PackageNode;
import avicit.ui.view.navigator.util.SwtResourceUtil;
import avicit.ui.view.navigator.views.AvicitProjectNavigator;
import avicit.ui.view.navigator.views.AvicitProjectViewer;

public class NewPackageAction extends SelectionListenerAction {

	public NewPackageAction(String text) {
		super(text);
	}

	public void run() {
		Object obj = getStructuredSelection().getFirstElement();
		if (obj == null)
			return;
		NewPackageWizard wizard = new NewPackageWizard();
		wizard.init(PlatformUI.getWorkbench(), new StructuredSelection(obj));
		WizardDialog dialog = new WizardDialog(Display.getCurrent().getActiveShell(), wizard);
		dialog.create();
		int result = dialog.open();
		if (result == Dialog.OK) {
			final AbstractFolderNode folderNode = (AbstractFolderNode)selection.getFirstElement();
			final PackageNode childNode = wizard.getChild();
			childNode.setType(folderNode.getType());
			childNode.setParent(folderNode);
			if(null == folderNode.getChildren()){
				folderNode.setChildren(new ArrayList());
			}

			folderNode.getChildren().add(childNode);
			final AvicitProjectViewer viewer = AvicitProjectNavigator.getViewer();
//			if (SwtResourceUtil.isValid(viewer.getTree())) {
			if(true){	
				Display display = Display.getDefault();
				if (display != null)
					display.asyncExec(new Runnable() {
						public void run() {
//							viewer.add(folderNode, childNode);
							List refreshList = new ArrayList();
							refreshList.add(folderNode.getResource().getResource());
//							refreshList.add(childNode.getResource().getResource());
							viewer.getResourceMapper().refresh(false, refreshList, refreshList, 500);
						}
					});
			}
		}
	}

	protected boolean updateSelection(IStructuredSelection selection) {
		boolean flag = super.updateSelection(selection);
		if (flag)
			flag = getStructuredSelection().toList().size() == 1;
		this.selection = selection;
		return flag;
	}

	IStructuredSelection selection;
}