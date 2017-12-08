package avicit.ui.core.runtime.resource;


import org.eclipse.core.resources.IFolder;

public class EclipseSourceFolderDelegate extends EclipseFolderDelegate
    implements ISourceFolderDelegate
{

    public EclipseSourceFolderDelegate(IFolder folder)
    {
        super(folder);
    }

    public IOutputFolderDelegate getOutputFolder()
    {
        return EclipseResourceManager.getInstance().getOutputFolder(this);
    }
}