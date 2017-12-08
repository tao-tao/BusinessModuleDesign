package avicit.ui.runtime.util;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;

public class EclipseProjectExistValidator extends AbstractStringValidator
{

    public EclipseProjectExistValidator()
    {
    }

    public EclipseProjectExistValidator(String r_Message)
    {
        super(r_Message);
    }

    protected boolean onValidate(String r_Value)
    {
        IProject t_Project = ResourcesPlugin.getWorkspace().getRoot().getProject(r_Value);
        return t_Project.exists();
    }
}