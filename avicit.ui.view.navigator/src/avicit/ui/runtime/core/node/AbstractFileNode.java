package avicit.ui.runtime.core.node;


import avicit.ui.core.runtime.resource.IFileDelegate;

public abstract class AbstractFileNode extends AbstractResourceNode
{

    public AbstractFileNode(IFileDelegate file)
    {
        super(file);
    }

    public IFileDelegate getFile()
    {
        return (IFileDelegate)getResource();
    }
}