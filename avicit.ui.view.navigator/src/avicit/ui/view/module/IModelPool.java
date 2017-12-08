package avicit.ui.view.module;


import org.eclipse.core.runtime.IProgressMonitor;

import avicit.ui.core.runtime.resource.IResourceDelegate;

public interface IModelPool
{

    public abstract void attachModel(Object obj, IResourceDelegate resource);

    public abstract void detachModel(IResourceDelegate resource, String type);

    public abstract Object getModel(IResourceDelegate resource, String type, boolean flag, IProgressMonitor iprogressmonitor);

    public abstract boolean isInPool(IResourceDelegate resource, String type);

    public abstract boolean isValid(IResourceDelegate resource, String type);

    public abstract boolean refresh(IResourceDelegate resource, String type);

    public abstract boolean isEnable();

    public abstract void setEnable(boolean flag);

    public abstract void setMaxModelPoolCount(int i);

    public abstract int getMaxModelPoolCount();

    public static final int DefaultMaxModelPoolCount = 1500;
}