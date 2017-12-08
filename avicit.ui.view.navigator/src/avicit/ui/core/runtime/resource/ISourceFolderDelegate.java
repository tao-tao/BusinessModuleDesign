package avicit.ui.core.runtime.resource;


public interface ISourceFolderDelegate
    extends IFolderDelegate
{

    public abstract IOutputFolderDelegate getOutputFolder();
}