package avicit.ui.runtime.core.node;

import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;

import avicit.ui.common.util.PlatformHelper;
import avicit.ui.core.runtime.resource.IFolderDelegate;
import avicit.ui.core.runtime.resource.IResourceDelegate;
import avicit.ui.runtime.core.IPackageContainer;

public class CommonNode extends AbstractFolderNode implements IPackageContainer
{
	static String TYPE = "comm";
    public CommonNode(IFolderDelegate folder)
    {
        super(folder);
        setOrder(100);
    }

    @Override
	public String getDisplayName() {
		return "公共元素";
	}
    
//    @Override
	public String getType()
    {
        return TYPE;
    }

	public IFile getCommonFile()
    {
		IProject jp = this.getResource().getResource().getProject();
		IFile file = PlatformHelper.getAppSpringConfigFile(jp);
		if(file == null || !file.exists()) {
			file = PlatformHelper.getPlatformSpringConfigFile(jp);
		}
		return file;
    }

    public Object getAdapter(Class adapter)
    {
        if(IResourceDelegate.class == adapter)
            return getResource();
        if(IProject.class == adapter)
        {
			try {
				return getResource().getProject().getAdapter(adapter);
			} catch (ResourceException e) {
				return null;
			}
        }/*
        else if(IDoubleClickListener.class == adapter)
		{
			return new OpenCommonNodeAction();
		}*/
		return null;
    }
}