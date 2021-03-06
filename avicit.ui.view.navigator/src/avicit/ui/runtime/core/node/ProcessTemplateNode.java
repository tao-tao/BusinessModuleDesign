package avicit.ui.runtime.core.node;

import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.IDoubleClickListener;

import avicit.ui.core.runtime.resource.IFileDelegate;
import avicit.ui.core.runtime.resource.IResourceDelegate;
import avicit.ui.runtime.core.action.OpenProcessAction;

public class ProcessTemplateNode extends AbstractFileNode
{

    public ProcessTemplateNode(IFileDelegate file)
    {
        super(file);
        setOrder(2);
    }

    public Object getModel()
    {
        return this.getFile();
    }

    @Override
	public String getDisplayName() {
		String name = this.getFile().getName();
		int dotIndex = name.indexOf(".");
		if(dotIndex>0)
			name = name.substring(0,dotIndex);
		return name;
	}

	public String getType()
    {
        return "process";
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
        	return new OpenProcessAction();
        }
		else
		{
            return super.getAdapter(adapter);
		}
    }
}