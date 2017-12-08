package avicit.ui.view.create;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import avicit.ui.runtime.core.action.NewAssemblyAction;
import avicit.ui.runtime.core.action.NewSubSystemAction;

public class AssemblyManagerAction implements IObjectActionDelegate{

	private IWorkbenchPart part;
	private ISelection selection;
	private NewAssemblyAction componentManagerCreateAction;
	private IProject pro;

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
//		componentManagerCreateAction = new NewComponentAction("createSubSystemAction",(Project) this.pro);
		componentManagerCreateAction = new NewAssemblyAction("createSubSystemAction",this.pro);	
		componentManagerCreateAction.run();
	}

	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
		if(null!=this.selection){
			System.out.println("CCCCCCCCCCC");
		}
		if(selection instanceof TreeSelection){
			Object obj1 = ((TreeSelection)selection).getFirstElement();
			if(null!=obj1){
				this.pro = (IProject) obj1;
//				try {
////					this.pro = (IProject) ((INode)obj1).getResource().getProject();
//				} catch (ResourceException e) {
//					e.printStackTrace();
//					System.out.println("ERROR_ERROR_ERROR");
//				}
			}
		}		
	}
	
	

}
