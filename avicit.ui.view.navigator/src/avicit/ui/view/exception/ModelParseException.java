package avicit.ui.view.exception;

import avicit.ui.core.runtime.resource.IResourceDelegate;



public final class ModelParseException extends RuntimeException
{

    public ModelParseException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public ModelParseException(String message)
    {
        super(message);
    }

    public ModelParseException(Throwable cause)
    {
        super(cause);
    }

    public ModelParseException(Throwable cause, IResourceDelegate resourceDelegate)
    {
        super(cause);
        this.resourceDelegate = resourceDelegate;
    }

    public ModelParseException(String message, Throwable cause, IResourceDelegate resourceDelegate)
    {
        super(message, cause);
        this.resourceDelegate = resourceDelegate;
    }

    public ModelParseException(String message, IResourceDelegate resourceDelegate)
    {
        super(message);
        this.resourceDelegate = resourceDelegate;
    }

    public IResourceDelegate getResourceDelegate()
    {
        return resourceDelegate;
    }

    private static final long serialVersionUID = 1L;
    private IResourceDelegate resourceDelegate;
}