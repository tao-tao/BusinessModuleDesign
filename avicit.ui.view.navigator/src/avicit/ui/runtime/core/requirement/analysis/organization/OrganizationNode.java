package avicit.ui.runtime.core.requirement.analysis.organization;


import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;

import avicit.ui.core.runtime.resource.IFolderDelegate;
import avicit.ui.core.runtime.resource.IResourceDelegate;
import avicit.ui.runtime.core.IPackageContainer;
import avicit.ui.runtime.core.node.AbstractFolderNode;
import avicit.ui.runtime.core.node.Action;
import avicit.ui.view.module.GuizeModelFactory;



@SuppressWarnings("restriction")
public class OrganizationNode extends AbstractFolderNode implements IPackageContainer
{

    public OrganizationNode(IFolderDelegate folder)
    {
        super(folder);
        setOrder(1000);
    }

    @Override
	public String getDisplayName() {
		return "组织机构建模";
	}
    
//  @Override
	public String getType()
    {
        return GuizeModelFactory.TYPE;
    }

	public String getConfigPath() {
		return this.getResource().getProjectRelativePath() + "/META-INF/analysis";
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
//		else if(IResource.class == adapter)
//		{
//            return this.getFolder().getFile(CONFIG_FILE).getResource();
//		}
		else if(IPackageContainer.class == adapter)
		{
            return this;
		}
		return null;
    }
    public void createAction(IStructuredSelection selection,IMenuManager menu){
    	Action guizeAction = new Action("组织机构建模");
    	guizeAction.selectionChanged(selection);
		menu.appendToGroup("group.edit", guizeAction);
    }
    public Object getAdapter(Object adaptableObject, Class adapterType) {
    	return OrganizationWorkbenchAdapter.getInstance();
    }
}