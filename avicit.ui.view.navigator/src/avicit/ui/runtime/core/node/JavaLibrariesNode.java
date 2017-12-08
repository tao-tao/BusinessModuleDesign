package avicit.ui.runtime.core.node;

import org.eclipse.core.resources.IResource;

import avicit.ui.core.runtime.resource.IProjectDelegate;

public class JavaLibrariesNode extends AbstractResourceNode
{

    public JavaLibrariesNode(IProjectDelegate project)
    {
        super(project);
        setOrder(100);
    }

    public IProjectDelegate getProject()
    {
        return (IProjectDelegate)getResource();
    }

    public String getType()
    {
        return "com.tansun.ui.navigator.javaLibrary";
    }

    public Object getAdapter(Class adapter)
    {
        if(IResource.class == adapter)
            return null;
        else
            return super.getAdapter(adapter);
    }
}