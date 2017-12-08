package avicit.ui.runtime.core.requirement.designer.logicflow;


import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;

import avicit.ui.core.runtime.resource.IFolderDelegate;
import avicit.ui.core.runtime.resource.IResourceDelegate;
import avicit.ui.runtime.core.IPackageContainer;
import avicit.ui.runtime.core.node.AbstractFolderNode;
import avicit.ui.runtime.core.requirement.designer.logicflow.GuizeWorkbenchAdapter;
import avicit.ui.view.navigator.ImageRepository;

public class GuizeNode extends AbstractFolderNode implements IPackageContainer
{

    public GuizeNode(IFolderDelegate folder)
    {
        super(folder);
        setOrder(2);
    }

    @Override
	public String getDisplayName() {
		return "逻辑流建模";
	}
    
//  @Override
	public String getType()
    {
        return GuizeModelFactory.TYPE;
    }

	public String getConfigPath() {
		return this.getResource().getProjectRelativePath();
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
    	LogicFlowAction guizeAction = new LogicFlowAction("新建逻辑流模型");
    	guizeAction.setImageDescriptor(ImageRepository
				.getImageRepository("avicit.ui.view.navigator")
				.getImageDescriptor("avicit.ui.runtime.core.requirement.designer.erm.ErNode"));
    	guizeAction.selectionChanged(selection);

		menu.appendToGroup("group.edit", guizeAction);
    }
    public Object getAdapter(Object adaptableObject, Class adapterType) {
    	return GuizeWorkbenchAdapter.getInstance();
    }
}