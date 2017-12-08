package avicit.ui.view.module;


import org.eclipse.core.runtime.IProgressMonitor;

import avicit.ui.core.runtime.resource.IMessageCaller;
import avicit.ui.core.runtime.resource.IResourceDelegate;


public interface IModelCompiler
{

    public abstract IResourceDelegate[] compile(IResourceDelegate ifiledelegate, int i, int j, IProgressMonitor iprogressmonitor, IMessageCaller imessagecaller);

    public abstract IResourceDelegate[] compile(IResourceDelegate ifiledelegate, int i, Object obj, int j, IProgressMonitor iprogressmonitor, IMessageCaller imessagecaller);

    public abstract IResourceDelegate[] getTargetResources(IResourceDelegate ifiledelegate);

    public static final int UNKNOW = 0;
    public static final int SUCCESS = 2;
    public static final int ERROR = 4;

}