package net.java.amateras.uml.action;

import net.java.amateras.uml.UMLPlugin;

import org.eclipse.gef.GraphicalViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class OpenOutlineViewAction extends AbstractUMLEditorAction {
	
	public OpenOutlineViewAction(GraphicalViewer viewer){
		super(UMLPlugin.getDefault().getResourceString("menu.openOutlineView"), viewer);
		setImageDescriptor(UMLPlugin.getImageDescriptor("icons/view_outline.gif"));
	}
	
	public void update(IStructuredSelection sel){
	}

	public void run(){
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		try {
			window.getActivePage().showView("org.eclipse.ui.views.ContentOutline");
		} catch(Exception ex){
			ex.printStackTrace();
		}
	}

}
