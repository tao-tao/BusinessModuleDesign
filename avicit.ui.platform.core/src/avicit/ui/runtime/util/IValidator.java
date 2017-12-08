package avicit.ui.runtime.util;

import org.eclipse.core.runtime.IAdapterFactory;

public interface IValidator
    extends ICloneable
{

    public abstract boolean validate(Object obj, IAdapterFactory iadapterfactory, IMessageCaller imessagecaller);

    public static final IValidator NullValidators[] = new IValidator[0];

}