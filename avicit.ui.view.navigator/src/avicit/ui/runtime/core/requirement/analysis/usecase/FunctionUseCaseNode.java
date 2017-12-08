package avicit.ui.runtime.core.requirement.analysis.usecase;

import org.eclipse.core.internal.resources.Project;
import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;

import avicit.ui.core.runtime.resource.IFileDelegate;
import avicit.ui.core.runtime.resource.IResourceDelegate;
import avicit.ui.runtime.core.action.DeleteFileAction;
import avicit.ui.runtime.core.action.OpenJspAction;
import avicit.ui.runtime.core.node.AbstractFileNode;

public class FunctionUseCaseNode extends AbstractFileNode {

	public FunctionUseCaseNode(IFileDelegate file) {
		super(file);
	}

    @Override
	public String getDisplayName() {
    	String name = this.getFile().getName();

		return name;
	}
    
	public String getType() {
        return "fuc";
    }

	public Object getAdapter(Class adapter) {
        if(IResourceDelegate.class == adapter)
            return getResource();
        if(IProject.class == adapter) {
			try {
				return getResource().getProject().getAdapter(adapter);
			} catch (ResourceException e) {
				return null;
			}
        }if (IDoubleClickListener.class == adapter) {
			return new OpenJspAction();
		} 

        return super.getAdapter(adapter);
    }

    public Object getAdapter(Object adaptableObject, Class adapterType) {
    	return FunctionUseCaseNodeAdapter.getInstance();
    }

    public void createAction(IStructuredSelection selection,IMenuManager menu){
    	Project obj = (Project) ((AbstractFileNode)selection.getFirstElement()).getResource().getResource().getProject();

		DeleteFileAction delAction = new DeleteFileAction("删除功能建模");

		delAction.selectionChanged(selection);
		menu.appendToGroup("group.edit", delAction);		
    }
}
