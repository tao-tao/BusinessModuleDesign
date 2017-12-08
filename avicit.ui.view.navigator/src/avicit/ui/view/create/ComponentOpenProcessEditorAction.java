package avicit.ui.view.create;


import org.eclipse.core.internal.resources.Project;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class ComponentOpenProcessEditorAction implements IObjectActionDelegate {
	private IWorkbenchPart part;
	private ISelection selection;
	private ComponentManagerCreateAction componentManagerCreateAction;

	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		this.part = targetPart;
	}

	public void run(IAction action) {
		IWorkbenchWindow w = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (w == null){
			return;
		}
		IWorkbenchPage page = w.getActivePage();
		if (page == null){
			return;
		}
		Object obj = null;
		IProject jp = null;
		if(selection instanceof TreeSelection){
			obj = ((TreeSelection)selection).getFirstElement();
		}
		componentManagerCreateAction = new ComponentManagerCreateAction("createAction",(Project)obj,ModulCreate.constru);
		componentManagerCreateAction.run();
	}

	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}
	
}
