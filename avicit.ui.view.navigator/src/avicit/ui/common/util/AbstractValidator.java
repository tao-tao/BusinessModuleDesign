package avicit.ui.common.util;


import org.eclipse.core.runtime.IAdapterFactory;

import avicit.ui.core.runtime.resource.IMessageCaller;

public abstract class AbstractValidator
    implements IValidator
{

    public AbstractValidator()
    {
        level = 2;
    }

    public AbstractValidator(String r_Message)
    {
        level = 2;
        message = r_Message;
    }

    public String getMessage()
    {
        return message;
    }

    public final void setMessage(String r_Message)
    {
        message = r_Message;
    }

    public int getLevel()
    {
        return level;
    }

    public void setLevel(int r_Level)
    {
        if(4 == r_Level || 16 == r_Level)
            level = r_Level;
        else
            level = 2;
    }

    public Object clone()
    {
        return this;
    }

    private void updateMessage(IMessageCaller r_MessageCaller)
    {
        if(r_MessageCaller == null)
            return;
        if(2 == getLevel())
        {
            r_MessageCaller.error(getMessage(), null);
            return;
        }
        if(4 == getLevel())
        {
            r_MessageCaller.warn(getMessage(), null);
            return;
        }
        if(16 == getLevel())
        {
            r_MessageCaller.info(getMessage(), null);
            return;
        } else
        {
            return;
        }
    }

    public boolean validate(Object r_Value, IAdapterFactory r_AdapterFactory, IMessageCaller r_MessageCaller)
    {
        if(doValidate(r_Value, r_AdapterFactory, r_MessageCaller))
            return true;
        updateMessage(r_MessageCaller);
        return 2 != getLevel();
    }

    protected abstract boolean doValidate(Object obj, IAdapterFactory iadapterfactory, IMessageCaller imessagecaller);

    private String message;
    private int level;
}