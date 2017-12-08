package avicit.ui.runtime.core.action;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.SelectionListenerAction;

import avicit.ui.runtime.core.node.PackageNode;

public class NewAppFormAction extends SelectionListenerAction {

	public NewAppFormAction(String text) {
		super(text);
	}

	public void run() {/*
		Object obj = getStructuredSelection().getFirstElement();
		if (obj == null)
			return;
		if(obj instanceof IFile)
			try {
				((IFile)obj).refreshLocal(1, null);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		if(obj instanceof PackageNode)
		{
			obj = ((PackageNode)obj).getFolder().getResource();
		}
		//FormWizard wizard = new FormWizard();
		FormWizard wizard = new FormWizard();
		wizard.init(PlatformUI.getWorkbench(), new StructuredSelection(obj));
		WizardDialog dialog = new WizardDialog(Display.getCurrent().getActiveShell(), wizard);
		dialog.create();
		int result = dialog.open();
		if (result == Dialog.OK) {
//			PackageNode folderNode = (PackageNode) obj;
//			PresentationFormNode childNode = new PresentationFormNode(new EclipseFileDelegate((IFile) wizard.getNewFile()));
//			childNode.setParent(folderNode);
//			folderNode.getChildren().add(childNode);
//			ProjectViewer viewer = ProjectNavigator.getViewer();
//			if (SwtResourceUtil.isValid(viewer.getTree())) {
//				viewer.getResourceMapper().refreshModelNode(IResourceDelta.ADDED, folderNode, childNode);
//			}
		}
		super.run();
	*/}

	protected boolean updateSelection(IStructuredSelection selection) {
		boolean flag = super.updateSelection(selection);
		if (flag)
			flag = getStructuredSelection().toList().size() == 1;
		return flag;
	}

}