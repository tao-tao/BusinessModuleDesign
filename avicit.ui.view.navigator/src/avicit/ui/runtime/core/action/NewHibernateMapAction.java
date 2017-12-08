package avicit.ui.runtime.core.action;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.SelectionListenerAction;

import avicit.ui.core.runtime.resource.EclipseFileDelegate;
import avicit.ui.core.runtime.resource.IFileDelegate;

public class NewHibernateMapAction extends SelectionListenerAction {

	public NewHibernateMapAction(String text) {
		super(text);
	}

	public void run() {/*
		Object obj = getStructuredSelection().getFirstElement();
		if (obj == null)
			return;
		MappingWizard wizard = new MappingWizard();
		wizard.init(PlatformUI.getWorkbench(), new StructuredSelection(obj));
		WizardDialog dialog = new WizardDialog(Display.getCurrent().getActiveShell(), wizard);
		dialog.create();
		int result = dialog.open();
		if (result == Dialog.OK) {
			IFileDelegate delegate = new EclipseFileDelegate((IFile) wizard.getNewFile());
			if(delegate.exists())
			{
//				PackageNode folderNode = (PackageNode)obj;
//				DataMappingNode childNode = new DataMappingNode(delegate);
//				childNode.setParent(folderNode);
//	  		  	folderNode.getChildren().add(childNode);
//				ProjectViewer viewer = ProjectNavigator.getViewer();
//				if (SwtResourceUtil.isValid(viewer.getTree())) {
//					viewer.getResourceMapper().refreshModelNode(IResourceDelta.ADDED, folderNode, childNode);
//				}
			}
		}
	*/}

	protected boolean updateSelection(IStructuredSelection selection) {
		boolean flag = super.updateSelection(selection);
		if (flag)
			flag = getStructuredSelection().toList().size() == 1;
		return flag;
	}

}