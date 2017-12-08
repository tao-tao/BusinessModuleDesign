package avicit.ui.runtime.resource.inf;

public interface IProjectDelegate
    extends IFolderDelegate
{
    public abstract ISourceFolderDelegate[] getSourceFolders();

    public abstract boolean isOpen();
}