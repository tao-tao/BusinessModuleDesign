package avicit.ui.runtime.core.requirement.requirement.business;

import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.IDoubleClickListener;

import avicit.ui.core.runtime.resource.IFileDelegate;
import avicit.ui.core.runtime.resource.IResourceDelegate;
import avicit.ui.runtime.core.action.OpenJspAction;
import avicit.ui.runtime.core.node.AbstractFileNode;

/**
 * @author TaoTao
 *
 */
@SuppressWarnings("restriction")
public class BusinessUseCaseNode extends AbstractFileNode {

	public BusinessUseCaseNode(IFileDelegate file) {
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
    	return BusinessUseCaseNodeAdapter.getInstance();
    }
}
