package avicit.ui.runtime.util;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;

public class EclipseDirectoryExistValidator extends AbstractStringValidator
{

    public EclipseDirectoryExistValidator()
    {
    }

    public EclipseDirectoryExistValidator(String r_Message)
    {
        super(r_Message);
    }

    protected boolean onValidate(String r_Value)
    {
        Path t_Path;
        String t_Segments[];
        t_Path = new Path(r_Value);
        t_Segments = t_Path.segments();
        if(t_Segments.length == 0)
            return false;
        IProject t_Project;
        if(1 != t_Segments.length)
        {
	        String t_Segment = t_Segments[0];
	        t_Project = ResourcesPlugin.getWorkspace().getRoot().getProject(t_Segment);
	        return t_Project.exists();
        }else{
	        IFolder t_Folder = ResourcesPlugin.getWorkspace().getRoot().getFolder(t_Path);
	        return t_Folder.exists();
        }
    }
}