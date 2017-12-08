package avicit.ui.core.runtime.resource;


public interface IRootDelegate
    extends IFolderDelegate
{

    public abstract IProjectDelegate[] getProjects();

    public abstract IProjectDelegate[] getEOSProjects();

    public abstract IProjectDelegate getProject(String s);
}