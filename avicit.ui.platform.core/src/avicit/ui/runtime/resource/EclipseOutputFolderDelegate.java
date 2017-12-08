package avicit.ui.runtime.resource;

import org.eclipse.core.resources.IFolder;

import avicit.ui.runtime.resource.inf.IOutputFolderDelegate;

public class EclipseOutputFolderDelegate extends EclipseFolderDelegate
    implements IOutputFolderDelegate
{

    EclipseOutputFolderDelegate(IFolder folder)
    {
        super(folder);
    }
}