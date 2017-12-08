package avicit.ui.core.runtime.resource;


import java.io.File;

import org.apache.commons.lang.ObjectUtils;
import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourceAttributes;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.QualifiedName;

@SuppressWarnings("restriction")
public abstract class EclipseResourceDelegate extends AbstractResourceDelegate
    implements IResourceDelegate
{

    public EclipseResourceDelegate(IResource resource)
    {
        this.resource = resource;
    }

    public IResource getResource()
    {
        return resource;
    }

    public void delete()
        throws ResourceException
    {
        try
        {
            resource.delete(false, null);
        }
        catch(CoreException e)
        {
            throw new ResourceException((IStatus) e);
        }
    }

    public boolean exists()
    {
        return resource != null && resource.exists();
    }

    public String getFullPath()
    {
        return resource.getFullPath().toString();
    }

    public String getName()
    {
        return resource.getName();
    }

    public IFolderDelegate getParent()
        throws ResourceException
    {
        return EclipseResourceManager.getInstance().getParent(this);
    }

    public IProjectDelegate getProject()
        throws ResourceException
    {
        return EclipseResourceManager.getInstance().getProject(this);
    }

    public String getProjectRelativePath()
    {
        return getResource().getProjectRelativePath().toString();
    }

    public final IRootDelegate getRoot()
        throws ResourceException
    {
        return EclipseRootDelegate.getInstance();
    }

    public ISourceFolderDelegate getSourceFolder()
    {
        return EclipseResourceManager.getInstance().getSourceFolder(this);
    }

    public String getSourceRelativePath()
    {
        return EclipseResourceManager.getInstance().getSourceRelativePath(this);
    }

    public int getType()
        throws ResourceException
    {
        return resource.getType();
    }

    public int hashCode()
    {
        int t_Result = 1;
        t_Result = 31 * t_Result + (resource != null ? resource.hashCode() : 0);
        return t_Result;
    }

    public long getLastModified()
    {
        return resource.getLocalTimeStamp();
    }

    public boolean equals(Object obj)
    {
        if(this == obj)
            return true;
        if(resource == null)
            return false;
        if(obj == null)
            return false;
        if(getClass() != obj.getClass())
        {
            return false;
        } else
        {
            EclipseResourceDelegate other = (EclipseResourceDelegate)obj;
            return ObjectUtils.equals(resource, other.resource);
        }
    }

    public Object getAdapter(Class adapter)
    {
        if(IResource.class == adapter)
            return getResource();
        if(IProject.class == adapter)
            return resource.getProject();
        else
            return super.getAdapter(adapter);
    }

    public String toString()
    {
        return resource.toString();
    }

    public File getFile()
    {
        if(resource == null)
            return null;
        IPath path = resource.getLocation();
        if(path == null)
            return null;
        else
            return path.toFile();
    }

    public boolean isArchive()
    {
        return false;
    }

    public boolean isEditable()
    {
        if(!resource.isAccessible())
            return false;
        if(resource.isLinked())
            return false;
        ResourceAttributes attributes = resource.getResourceAttributes();
        if(attributes == null)
            return false;
        else
            return attributes.isReadOnly();
    }

    public boolean isBinary()
    {
        return false;
    }

    public String getProtocol()
    {
        return "eclipse";
    }

    public String getPersistentProperty(String key)
    {
        if(!resource.exists())
            return null;
        try {
			return resource.getPersistentProperty(new QualifiedName("com.primeton.studio.runtime", key));
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return null;
    }

    public void setPersistentProperty(String key, String value)
    {
        try
        {
            QualifiedName name = new QualifiedName("com.primeton.studio.runtime", key);
            resource.setPersistentProperty(name, value);
        }
        catch(CoreException e)
        {
            e.printStackTrace();
        }
    }

    public static final String ECLIPSE = "eclipse";
    private IResource resource;
}