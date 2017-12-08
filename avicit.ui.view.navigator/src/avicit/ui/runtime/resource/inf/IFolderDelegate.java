package avicit.ui.runtime.resource.inf;

import avicit.ui.core.runtime.resource.IFileDelegate;
import avicit.ui.core.runtime.resource.IResourceDelegate;

public interface IFolderDelegate
    extends IResourceDelegate
{

    public abstract IResourceDelegate[] getChildren();

    public abstract IFileDelegate getFile(String s);

    public abstract IFolderDelegate getFolder(String s);

    public abstract boolean isPrefixOf(IResourceDelegate iresourcedelegate);
}