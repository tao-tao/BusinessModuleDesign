package avicit.ui.runtime.core.node;

import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;

import avicit.ui.core.runtime.resource.IFolderDelegate;
import avicit.ui.core.runtime.resource.IResourceDelegate;
import avicit.ui.runtime.core.IPackageContainer;

public class ControllerNode extends AbstractFolderNode implements IPackageContainer
{

    public ControllerNode(IFolderDelegate folder)
    {
        super(folder);
        setOrder(2);
    }

    @Override
	public String getDisplayName() {
		return NavigatorMessages.CONTROLLER_NAME;
	}
    
//  @Override
	public String getType()
    {
        //return ControllerModelFactory.TYPE; update by lidong
        return "cont";
    }

	public String getConfigPath() {
		return this.getResource().getProjectRelativePath() + "/META-INF/control";
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
        }
		else if(IResource.class == adapter)
		{
            return this.getFolder().getFile(CONFIG_FILE).getResource();
		}
		else if(IPackageContainer.class == adapter)
		{
            return this;
		}
		return null;
    }
}