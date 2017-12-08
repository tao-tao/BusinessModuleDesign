package avicit.ui.runtime.resource;

import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IProject;

import avicit.ui.runtime.resource.inf.IProjectDelegate;
import avicit.ui.runtime.resource.inf.ISourceFolderDelegate;

public class EclipseProjectDelegate extends EclipseFolderDelegate
    implements IProjectDelegate
{

    public EclipseProjectDelegate(IProject project)
    {
        super(project);
    }

    public IProject getEclipseProject()
    {
        return (IProject)getResource();
    }

    public ISourceFolderDelegate[] getSourceFolders()
    {
        return EclipseResourceManager.getInstance().getSourceFolders(this);
    }

    public Object getAdapter(Class adapter)
    {
        if(IProject.class == adapter)
            return getEclipseProject();
        else
            return super.getAdapter(adapter);
    }

    public void delete()
        throws ResourceException
    {
        super.delete();
    }

    public boolean isOpen()
    {
        return getEclipseProject().isOpen();
    }

    public ISourceFolderDelegate getSourceFolder()
    {
        return null;
    }

    private transient long timestamp;
}