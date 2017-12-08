package avicit.ui.view.module;

import avicit.ui.core.runtime.resource.IResourceDelegateDelta;



public class ModelChangeEvent
{

    public ModelChangeEvent(int type, Object source, IResourceDelegateDelta resourceDelta)
    {
        this.type = type;
        this.source = source;
    }

    public Object getSource()
    {
        return source;
    }

    public void setSource(Object source)
    {
        this.source = source;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public int getBuildKind()
    {
        return buildKind;
    }

    public void setBuildKind(int buildKind)
    {
        this.buildKind = buildKind;
    }

    public final IResourceDelegateDelta getResourceDelta()
    {
        return resourceDelta;
    }

    public final void setResourceDelta(IResourceDelegateDelta resourceDelta)
    {
        this.resourceDelta = resourceDelta;
    }

    public static final int UNKNOWN = 0;
    public static final int ADD = 1;
    public static final int REMOVE = 2;
    public static final int UPDATE = 4;
    private int type;
    private Object source;
    private IResourceDelegateDelta resourceDelta;
    private int buildKind;
}