package avicit.ui.runtime.core.node;

import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;

import com.tansun.runtime.resource.adapter.DevelopeWorkbenchAdapter;

import avicit.ui.runtime.core.IPackageContainer;
import avicit.ui.runtime.ec.jc.NewGuizeAction;
import avicit.ui.view.module.DevelopeModelFactory;
import avicit.ui.core.runtime.resource.IFolderDelegate;
import avicit.ui.core.runtime.resource.IResourceDelegate;

@SuppressWarnings("restriction")
public class DevelopNode extends AbstractFolderNode implements IPackageContainer
{

    public DevelopNode(IFolderDelegate folder)
    {
        super(folder);
        setOrder(4);
    }

    @Override
	public String getDisplayName() {
		return "开发";
	}
    
//  @Override
	public String getType()
    {
        return DevelopeModelFactory.TYPE;
    }

	public String getConfigPath() {
		return this.getResource().getProjectRelativePath() + "/META-INF/biz";
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

    public void createAction(IStructuredSelection selection,IMenuManager menu){
    	NewGuizeAction guizeAction = new NewGuizeAction("新建成服务立规则");
    	guizeAction.selectionChanged(selection);
		menu.appendToGroup("group.edit", guizeAction);
    }

    public Object getAdapter(Object adaptableObject, Class adapterType) {
    	return DevelopeWorkbenchAdapter.getInstance();
    }
}