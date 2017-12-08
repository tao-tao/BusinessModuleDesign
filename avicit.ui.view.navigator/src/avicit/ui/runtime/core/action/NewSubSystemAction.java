package avicit.ui.runtime.core.action;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.internal.resources.Project;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.SelectionListenerAction;

import avicit.ui.core.runtime.resource.EclipseProjectDelegate;
import avicit.ui.runtime.core.action.wizard.NewSubSystemWizard;
import avicit.ui.runtime.core.node.AbstractFolderNode;
import avicit.ui.runtime.core.subsystem.SubSystemNode;
import avicit.ui.view.module.ModelManagerImpl;
import avicit.ui.view.module.ProjectModelFactory;
import avicit.ui.view.navigator.views.AvicitProjectNavigator;
import avicit.ui.view.navigator.views.AvicitProjectViewer;

@SuppressWarnings("restriction")
public class NewSubSystemAction extends SelectionListenerAction {
	private IProject pro;

	public NewSubSystemAction(String text,IProject pro) {
		super(text);
		this.pro = pro;
	}

	public void run() {
//		Object obj = getStructuredSelection().getFirstElement();
//		if (obj == null){
//			System.out.println("XXXXX");
//			return;
//		}
		NewSubSystemWizard wizard = new NewSubSystemWizard();
//		wizard.init(PlatformUI.getWorkbench(), new StructuredSelection(obj));
		wizard.init(PlatformUI.getWorkbench(), (Project)this.pro);
		WizardDialog dialog = new WizardDialog(Display.getCurrent().getActiveShell(), wizard);
		dialog.create();
		int result = dialog.open();
		if (result == Dialog.OK) {
//			final AbstractFolderNode folderNode = (AbstractFolderNode)selection.getFirstElement();
			final AbstractFolderNode folderNode = (AbstractFolderNode) ModelManagerImpl.getInstance().getPool().getModel(new EclipseProjectDelegate(pro), ProjectModelFactory.TYPE, true, null);		
			final SubSystemNode childNode = (SubSystemNode)wizard.getChild();
			childNode.setType(folderNode.getType());
			childNode.setParent(folderNode);

			final AvicitProjectViewer viewer = AvicitProjectNavigator.getViewer();
//			if (SwtResourceUtil.isValid(viewer.getTree())) {
			if(true){	
				Display display = Display.getDefault();
				if (display != null)
					display.asyncExec(new Runnable() {
						public void run() {
//							viewer.add(folderNode, childNode);
							List refreshList = new ArrayList();
							refreshList.add(folderNode.getResource().getResource().getProject());
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
	public static String createInitial(String name, String type)
			throws JavaModelException {

		StringBuffer buffer = new StringBuffer();
//		buffer.append("component.id=" + vo.id).append('\n');
		buffer.append("component.name=" + name).append('\n');
//		buffer.append("component.version=" + vo.version).append('\n');
//		buffer.append("component.type=" + vo.type).append('\n');
//		buffer.append("component.modules=" + vo.modules).append('\n');
//		buffer.append("component.service=" + vo.service).append('\n');
//		buffer.append("component.dao=" + vo.dao).append('\n');
//		buffer.append("component.auto=" + vo.auto).append('\n');
		if ("Comp".equals(type)) {
			buffer.append("component.iscomp=" + true).append('\n');
		} else {
			buffer.append("component.iscomp=" + false).append('\n');
		}

//		buffer.append("component.dtype=" + vo.dialogTitle).append('\n');
		return buffer.toString();
	}
	IStructuredSelection selection;
}