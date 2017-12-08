package avicit.ui.core.runtime.resource;


import org.eclipse.core.resources.IFolder;

public class EclipseOutputFolderDelegate extends EclipseFolderDelegate
    implements IOutputFolderDelegate
{

    EclipseOutputFolderDelegate(IFolder folder)
    {
        super(folder);
    }
}