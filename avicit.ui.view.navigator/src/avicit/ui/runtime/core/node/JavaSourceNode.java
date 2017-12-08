package avicit.ui.runtime.core.node;

import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaElement;

import avicit.ui.core.runtime.resource.IFolderDelegate;
import avicit.ui.view.module.JavaModelFactory;

public class JavaSourceNode extends AbstractFolderNode
{
	IJavaElement internalElement;
	
    public JavaSourceNode(IFolderDelegate folder)
    {
        super(folder);
        setOrder(6);
    }

    public Object getModel()
    {
        return getJavaElement();
    }

    public String getType()
    {
        return JavaModelFactory.TYPE;
    }

	public void setJavaElement(IJavaElement f) {
		this.internalElement = f;
	}

    public IJavaElement getJavaElement()
    {
        return internalElement;
    }

    public Object getAdapter(Class adapter)
    {
        if(IResource.class == adapter)
		{
            return this.getFolder().getResource();
		}
        Object internal = internalElement.getAdapter(adapter);
        if(internal != null)
        	return internal;
        
		return super.getAdapter(adapter);
    }

}