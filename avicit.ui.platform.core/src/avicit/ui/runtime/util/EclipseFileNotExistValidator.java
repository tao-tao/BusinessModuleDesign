package avicit.ui.runtime.util;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;

public final class EclipseFileNotExistValidator extends AbstractStringCaseValidator
{

    public EclipseFileNotExistValidator()
    {
    }

    public EclipseFileNotExistValidator(String r_Message)
    {
        super(r_Message);
    }

    public EclipseFileNotExistValidator(String r_Message, boolean r_IgnoreCase)
    {
        super(r_Message, r_IgnoreCase);
    }

    protected boolean onValidate(String r_Value)
    {
        if(StringUtils.isEmpty(r_Value))
            return true;
        IFile t_File;
        Path t_Path = new Path(r_Value);
        t_File = ResourcesPlugin.getWorkspace().getRoot().getFile(t_Path);
        if(t_File.exists())
            return false;
        IResource t_Children[];
        IContainer t_Folder = t_File.getParent();
        try {
			t_Children = t_Folder.members();
			 for(int i =0; i<t_Children.length; i++)
		        {
			        if((t_Children[i] instanceof IFile) && isEquals(t_File.getName(), t_Children[i].getName()))
			            return false;
		        }
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
        return true;
    }
}