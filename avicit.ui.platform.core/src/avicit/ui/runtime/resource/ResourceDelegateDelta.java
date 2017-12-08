package avicit.ui.runtime.resource;

import avicit.ui.runtime.resource.inf.IResourceDelegate;
import avicit.ui.runtime.resource.inf.IResourceDelegateDelta;

public class ResourceDelegateDelta
    implements IResourceDelegateDelta
{

    public ResourceDelegateDelta(IResourceDelegate resourceDelegate)
    {
        this.resourceDelegate = resourceDelegate;
    }

    public IResourceDelegate getResource()
    {
        return resourceDelegate;
    }

    private IResourceDelegate resourceDelegate;
}