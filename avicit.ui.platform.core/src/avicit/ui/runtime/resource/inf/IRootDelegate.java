package avicit.ui.runtime.resource.inf;

public interface IRootDelegate
    extends IFolderDelegate
{

    public abstract IProjectDelegate[] getProjects();

    public abstract IProjectDelegate[] getEOSProjects();

    public abstract IProjectDelegate getProject(String s);
}