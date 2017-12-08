package avicit.ui.view.module;


import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;

import avicit.ui.core.runtime.resource.IResourceDelegate;


public interface IModelFactory extends IAdaptable, IPriority
{

    public abstract String getBundleName();

    public abstract IModelCompiler getCompiler(Object obj, IProgressMonitor iprogressmonitor);

    public abstract IModelValidator getValidator(Object obj, IProgressMonitor iprogressmonitor);

    public abstract IModelParser getModelParser(Object obj, IProgressMonitor iprogressmonitor);

    public abstract void setPriority(int i);

    public abstract String[] getExtensionNames();
    
    public abstract void setExtensionNames(String[] exts);

    public abstract String getCategory();
    
    public abstract void setCategory(String cat);
    
    public int getResourceType();
    
    public void setResourceType(int resource);

    public abstract String getVersion();

    public abstract String getType();
    
    public abstract boolean isAcceptable(IResourceDelegate ifiledelegate);

	public abstract boolean supportIdenticalExtension();
}