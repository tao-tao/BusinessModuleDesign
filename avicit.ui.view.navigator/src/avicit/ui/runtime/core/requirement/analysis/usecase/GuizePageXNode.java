package avicit.ui.runtime.core.requirement.analysis.usecase;

import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.IDoubleClickListener;

import avicit.ui.runtime.core.action.OpenPageXAction;
import avicit.ui.runtime.core.node.AbstractFileNode;
import avicit.ui.runtime.core.requirement.requirement.usecase.UseCaseModelFactory;
import avicit.ui.core.runtime.resource.IFileDelegate;
import avicit.ui.core.runtime.resource.IResourceDelegate;

@SuppressWarnings("restriction")
public class GuizePageXNode extends AbstractFileNode
{

    public GuizePageXNode(IFileDelegate folder)
    {
        super(folder);
        setOrder(2);
    }

    public Object getModel()
    {
        return this.getFile();
    }

    @Override
	public String getDisplayName() {
		return this.getFile().getName();
	}

	public String getType()
    {
        return UseCaseModelFactory.TYPE;
    }
	 public Object getAdapter(Object adaptableObject, Class adapterType) {
	    	return GuizePageXWorkbenchAdapter.getInstance();
	    }
    public Object getAdapter(Class adapter)
    {
        if(IResourceDelegate.class == adapter)
            return getResource();
        if(IProject.class == adapter)
        {
			try {
				return getResource().getProject().getAdapter(adapter);
			} catch (ResourceException e) {
				return null;
			}
        }
        if(IDoubleClickListener.class == adapter)
        {
        	return new OpenPageXAction();
        }
        else
		{
            return super.getAdapter(adapter);
		}
    }
}