package avicit.ui.core.runtime.resource;


import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;

public class EclipseRootDelegate extends EclipseFolderDelegate
    implements IRootDelegate
{

    private EclipseRootDelegate()
    {
        super(ResourcesPlugin.getWorkspace().getRoot());
    }

    public static EclipseRootDelegate getInstance()
    {
        return instance;
    }

    public IProjectDelegate getProject(String name)
    {
        return EclipseResourceManager.getInstance().getProject(name);
    }

    public IProjectDelegate[] getProjects()
    {
        return EclipseResourceManager.getInstance().getProjects();
    }

    public IProjectDelegate[] getEOSProjects()
    {
//        IEOSProject projects[] = RuntimePlugin.getEOSProjects();
        List list = new ArrayList();
//        for(int i = 0; i < projects.length; i++)
//        {
//            IProject project = (IProject)projects[i].getAdapter(IResource.class);
//            if(project.isOpen())
//                list.add(new EclipseProjectDelegate(project));
//        }

        IProjectDelegate results[] = new IProjectDelegate[list.size()];
        list.toArray(results);
        return results;
    }

    public Object getAdapter(Class adapter)
    {
        if(IWorkspaceRoot.class == adapter)
            return ResourcesPlugin.getWorkspace().getRoot();
        else
            return super.getAdapter(adapter);
    }

    private static final EclipseRootDelegate instance = new EclipseRootDelegate();

}