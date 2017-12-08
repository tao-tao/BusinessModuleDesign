package avicit.ui.runtime.core.node;

import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;

import avicit.ui.core.runtime.resource.IFolderDelegate;
import avicit.ui.core.runtime.resource.IResourceDelegate;
import avicit.ui.runtime.core.IPackageContainer;
import avicit.ui.view.module.ServiceCompositeModelFactory;

public class ServiceCompositeNode extends ServiceNode implements IPackageContainer
{

    public ServiceCompositeNode(IFolderDelegate folder)
    {
        super(folder);
        setOrder(3);
    }

    @Override
	public String getDisplayName() {
		return NavigatorMessages.COMPOSITESERVICE_NAME;
	}

	public String getType()
    {
        return ServiceCompositeModelFactory.TYPE;
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
            return this.getFolder().getResource();
		}
		else if(IPackageContainer.class == adapter)
		{
            return this;
		}
		return null;
    }

}