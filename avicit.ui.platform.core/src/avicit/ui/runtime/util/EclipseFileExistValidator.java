package avicit.ui.runtime.util;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;

public final class EclipseFileExistValidator extends AbstractStringCaseValidator
{

    public EclipseFileExistValidator()
    {
    }

    public EclipseFileExistValidator(String r_Message)
    {
        super(r_Message);
    }

    public EclipseFileExistValidator(String r_Message, boolean r_IgnoreCase)
    {
        super(r_Message, r_IgnoreCase);
    }

    protected boolean onValidate(String r_Value)
    {
        Path t_Path;
        if(StringUtils.isEmpty(r_Value))
            return false;
        t_Path = new Path(r_Value);
        IFile t_File = ResourcesPlugin.getWorkspace().getRoot().getFile(t_Path);
        if(t_File.exists())
            return true;
        IResource t_Children[];
        IContainer t_Folder = t_File.getParent();
        try {
			t_Children = t_Folder.members();
	        for(int i=0; i< t_Children.length; i++)
	        {
		        if((t_Children[i] instanceof IFile) && isEquals(t_File.getName(), t_Children[i].getName()))
		            return true;
	        }
		} catch (CoreException e) {
			e.printStackTrace();
		}
        return false;
    }
}