package avicit.ui.core.runtime.resource;


import java.io.InputStream;

import org.eclipse.core.internal.resources.ResourceException;

public interface IFileDelegate
    extends IResourceDelegate
{

    public abstract String getExtension();

    public abstract void setContents(InputStream inputstream);

    public abstract void create(InputStream inputstream);

    public abstract InputStream getContents()
        throws ResourceException;
}