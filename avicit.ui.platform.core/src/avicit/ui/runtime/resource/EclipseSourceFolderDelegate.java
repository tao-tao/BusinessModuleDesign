package avicit.ui.runtime.resource;

import org.eclipse.core.resources.IFolder;

import avicit.ui.runtime.resource.inf.IOutputFolderDelegate;
import avicit.ui.runtime.resource.inf.ISourceFolderDelegate;

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