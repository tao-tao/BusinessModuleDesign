package avicit.ui.core.runtime.resource;



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