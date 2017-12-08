package avicit.ui.core.runtime.resource;


public interface IProjectDelegate
    extends IFolderDelegate
{
    public abstract ISourceFolderDelegate[] getSourceFolders();

    public abstract boolean isOpen();
}