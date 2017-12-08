package avicit.ui.runtime.core.node;

import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.IDoubleClickListener;

import avicit.ui.core.runtime.resource.IFolderDelegate;
import avicit.ui.core.runtime.resource.IResourceDelegate;
import avicit.ui.runtime.core.IPackageContainer;
import avicit.ui.runtime.core.action.OpenDataModelAction;

public class DaoModelNode extends AbstractFolderNode implements IPackageContainer
{

    public DaoModelNode(IFolderDelegate folder)
    {
        super(folder);
        setOrder(4);
    }

    @Override
	public String getDisplayName() {
		return NavigatorMessages.DAO_NAME;
	}

	public String getType()
    {
      //  return DaoModelFactory.TYPE; update by lidong
        return "dao";
    }

	public String getConfigPath() {
		return this.getResource().getProjectRelativePath() + "/META-INF/spring";
	}
	
    public Object getAdapter(Class adapter)
    {
        if(IResourceDelegate.class == adapter)
            return getResource();
        else if(IProject.class == adapter)
        {
			try {
				return getResource().getProject().getAdapter(adapter);
			} catch (ResourceException e) {
				return null;
			}
        }
        else if(IDoubleClickListener.class == adapter)
        {
        	return new OpenDataModelAction();
        }
		else if(IResource.class == adapter)
		{
            return this.getFolder().getFile(CONFIG_FILE).getResource();
		}
		else if(IPackageContainer.class == adapter)
		{
            return this;
		}
		else
		{
            return super.getAdapter(adapter);
		}
    }
}