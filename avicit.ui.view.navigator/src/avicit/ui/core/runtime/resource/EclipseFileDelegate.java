package avicit.ui.core.runtime.resource;


import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;

@SuppressWarnings("restriction")
public class EclipseFileDelegate extends EclipseResourceDelegate
    implements IFileDelegate
{

    public EclipseFileDelegate(IFile file)
    {
        super(file);
    }

    public InputStream getContents() throws ResourceException
    {
    	try{
    		return getEclipseFile().getContents(true);
    	}catch(CoreException e){
        	throw new ResourceException((IStatus) e);
        }
    }

    public IFile getEclipseFile()
    {
        return (IFile)getResource();
    }

    public String getExtension()
    {
        return getEclipseFile().getFileExtension();
    }

    public void create(InputStream inputStream)
    {
        try
        {
            IFile file = getEclipseFile();
            IContainer container = file.getParent();
            if(container.getType() == 2)
                EclipseResourceManager.forceCreateFolder((IFolder)container);
            getEclipseFile().create(inputStream, true, null);
        }
        catch(CoreException e)
        {
            //throw new ResourceException((IStatus) e);
        }
    }

    public void setContents(InputStream inputStream)
    {
        try
        {
            if(getEclipseFile().exists())
                getEclipseFile().setContents(inputStream, true, true, null);
            else
                create(inputStream);
        }
        catch(CoreException e)
        {
            //throw new ResourceException(e);
        }
    }

    public void create()
    {
        create(((InputStream) (new ByteArrayInputStream(new byte[0]))));
    }

    public Object getAdapter(Class adapter)
    {
        if(IFile.class == adapter)
            return getEclipseFile();
        else
            return super.getAdapter(adapter);
    }
}