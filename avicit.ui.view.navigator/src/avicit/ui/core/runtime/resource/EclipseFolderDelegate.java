package avicit.ui.core.runtime.resource;


import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

public class EclipseFolderDelegate extends EclipseResourceDelegate
    implements IFolderDelegate
{

    public EclipseFolderDelegate(IContainer folder)
    {
        super(folder);
    }

    protected EclipseFolderDelegate(IResource resource)
    {
        super(resource);
    }

    public IContainer getEclipseFolder()
    {
        return (IContainer)getResource();
    }

    public IResourceDelegate[] getChildren()
    {
    	try{
	        if(exists())
	            return EclipseResourceManager.getInstance().getChildren(this);
    	}catch(Exception e){}
        return NO_RESOURCES;
    }

    public IFileDelegate getFile(String path)
    {
        return EclipseResourceManager.getInstance().getFile(this, path);
    }

    public IFolderDelegate getFolder(String path)
    {
        return EclipseResourceManager.getInstance().getFolder(this, path);
    }

    public void create()
    {
        IContainer container;
        container = getEclipseFolder();
		if(container instanceof IFolder)
		{
		    EclipseResourceManager.forceCreateFolder((IFolder)container);
		    return;
		}

        if(container instanceof IProject)
        {
            try {
				((IProject)container).create(null);
			} catch (CoreException e) {
				e.printStackTrace();
			}
            return;
        }
    }

    public Object getAdapter(Class adapter)
    {
        if(IFolder.class == adapter || IContainer.class == adapter)
            return getEclipseFolder();
        else
            return super.getAdapter(adapter);
    }

    public boolean isPrefixOf(IResourceDelegate resourceDelegate)
    {
        if(resourceDelegate == null)
            return false;
        Assert.isTrue(resourceDelegate instanceof EclipseResourceDelegate, "The resource must be eclipse resource");
        IResource resource = ((EclipseResourceDelegate)resourceDelegate).getResource();
        if(resource == null)
        {
            return false;
        } else
        {
            IPath path = resource.getFullPath();
            return getResource().getFullPath().isPrefixOf(path);
        }
    }
}
