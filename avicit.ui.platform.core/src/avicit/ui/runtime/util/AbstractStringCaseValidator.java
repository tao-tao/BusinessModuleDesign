package avicit.ui.runtime.util;

public abstract class AbstractStringCaseValidator extends AbstractStringValidator
{

    public AbstractStringCaseValidator()
    {
    }

    public AbstractStringCaseValidator(String r_Message)
    {
        super(r_Message);
    }

    public AbstractStringCaseValidator(String r_Message, boolean r_IgnoreCase)
    {
        super(r_Message);
        ignoreCase = r_IgnoreCase;
    }

    public boolean isIgnoreCase()
    {
        return ignoreCase;
    }

    public void setIgnoreCase(boolean r_IgnoreCase)
    {
        ignoreCase = r_IgnoreCase;
    }

    protected final boolean isEquals(String r_SourceName, String r_TargetName)
    {
        if(isIgnoreCase())
            return r_SourceName.equalsIgnoreCase(r_TargetName);
        else
            return r_SourceName.equals(r_TargetName);
    }

    private boolean ignoreCase;
}