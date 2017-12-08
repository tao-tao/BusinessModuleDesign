package avicit.ui.common.util;


import org.eclipse.core.runtime.IAdapterFactory;

import avicit.ui.core.runtime.resource.IMessageCaller;

public interface IValidator
    extends ICloneable
{

    public abstract boolean validate(Object obj, IAdapterFactory iadapterfactory, IMessageCaller imessagecaller);

    public static final IValidator NullValidators[] = new IValidator[0];

}