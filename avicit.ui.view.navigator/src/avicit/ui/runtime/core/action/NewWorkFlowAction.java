package avicit.ui.runtime.core.action;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.actions.SelectionListenerAction;


public class NewWorkFlowAction extends SelectionListenerAction {

	public NewWorkFlowAction(String text) {
		super(text);
	}

	public void run() {/*
		Object obj = getStructuredSelection().getFirstElement();
		if (obj == null)
			return;
		NewProcessDefinitionWizard wizard = new NewProcessDefinitionWizard();
		wizard.init(PlatformUI.getWorkbench(), new StructuredSelection(obj));
		WizardDialog dialog = new WizardDialog(Display.getCurrent().getActiveShell(), wizard);
		dialog.create();
		int result = dialog.open();
		if (result == Dialog.OK) {
//			PackageNode folderNode = (PackageNode) obj;
//			ProcessTemplateNode childNode = new ProcessTemplateNode(new EclipseFileDelegate((IFile) wizard.getNewFile()));
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