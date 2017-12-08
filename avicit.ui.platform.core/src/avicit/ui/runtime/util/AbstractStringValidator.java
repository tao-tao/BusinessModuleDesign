package avicit.ui.runtime.util;

import org.eclipse.core.runtime.IAdapterFactory;

public abstract class AbstractStringValidator extends AbstractValidator
{

    public AbstractStringValidator()
    {
    }

    public AbstractStringValidator(String r_Message)
    {
        super(r_Message);
    }

    protected boolean doValidate(Object r_Value, IAdapterFactory r_AdapterFactory, IMessageCaller r_MessageCaller)
    {
        if(r_Value instanceof String)
            return onValidate((String)r_Value);
        if(r_Value == null)
            return onValidate((String)r_Value);
        if(r_AdapterFactory != null)
        {
            Object t_Object = r_AdapterFactory.getAdapter(r_Value, String.class);
            if(t_Object instanceof String)
                return onValidate((String)t_Object);
        }
        return false;
    }

    protected abstract boolean onValidate(String s);
}