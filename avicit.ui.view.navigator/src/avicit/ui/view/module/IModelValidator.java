package avicit.ui.view.module;


import org.eclipse.core.runtime.IProgressMonitor;

import avicit.ui.core.runtime.resource.IMessageCaller;
import avicit.ui.core.runtime.resource.IResourceDelegate;


public interface IModelValidator
{

    public abstract boolean validate(IResourceDelegate ifiledelegate, ModelChangeEvent modelchangeevent, IMessageCaller imessagecaller, IProgressMonitor iprogressmonitor);

    public abstract boolean validate(IResourceDelegate ifiledelegate, Object obj, ModelChangeEvent modelchangeevent, IMessageCaller imessagecaller, IProgressMonitor iprogressmonitor);
}