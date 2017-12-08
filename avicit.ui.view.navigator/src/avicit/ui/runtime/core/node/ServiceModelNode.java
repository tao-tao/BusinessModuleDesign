package avicit.ui.runtime.core.node;

import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;

import avicit.ui.core.runtime.resource.IFolderDelegate;
import avicit.ui.core.runtime.resource.IResourceDelegate;
import avicit.ui.runtime.core.IPackageContainer;

public class ServiceModelNode extends AbstractFolderNode implements IPackageContainer
{

    public ServiceModelNode(IFolderDelegate folder)
    {
        super(folder);
        setOrder(3);
    }

    @Override
	public String getDisplayName() {
		return NavigatorMessages.SERVICEMODEL_NAME;
	}

	public String getType()
    {
        //return ServiceModelModelFactory.TYPE; update by lidong
        return "servmodel";
    }

	public String getConfigPath() {
		return this.getResource().getProjectRelativePath() + "/META-INF/spring";
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