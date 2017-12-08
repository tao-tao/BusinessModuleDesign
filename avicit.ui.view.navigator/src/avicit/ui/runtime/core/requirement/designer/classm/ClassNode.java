package avicit.ui.runtime.core.requirement.designer.classm;


import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IProject;

import avicit.ui.core.runtime.resource.IFileDelegate;
import avicit.ui.core.runtime.resource.IResourceDelegate;
import avicit.ui.runtime.core.node.AbstractFileNode;

public class ClassNode extends AbstractFileNode
{

    public ClassNode(IFileDelegate file)
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
        return "cld";
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
//        if(IDoubleClickListener.class == adapter)
//        {
//        	return new OpenPageXAction();
//        }
//        else
		{
            return super.getAdapter(adapter);
		}
    }
    public Object getAdapter(Object adaptableObject, Class adapterType) {
    	return ClassNodeAdaptper.getInstance();
    }
}