package avicit.ui.runtime.resource;

import avicit.ui.runtime.resource.inf.IResourceDelegate;

public abstract class AbstractResourceDelegate extends AbstractDataContainer
    implements IResourceDelegate
{

    public AbstractResourceDelegate()
    {
    }

    public boolean isArchive()
    {
        return false;
    }

    public boolean isEditable()
    {
        return false;
    }

    public boolean isBinary()
    {
        return true;
    }
}