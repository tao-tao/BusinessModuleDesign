package avicit.ui.runtime.core.requirement.requirement.function;


import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;

import avicit.ui.core.runtime.resource.IFolderDelegate;
import avicit.ui.core.runtime.resource.IResourceDelegate;
import avicit.ui.runtime.core.IPackageContainer;
import avicit.ui.runtime.core.action.NewFunctionStructureAction;
import avicit.ui.runtime.core.node.AbstractFolderNode;

/**
 * @author Tao Tao
 *
 */
@SuppressWarnings("restriction")
public class FunctionStructureNode extends AbstractFolderNode implements IPackageContainer
{

    public FunctionStructureNode(IFolderDelegate folder)
    {
        super(folder);
        setOrder(100);
    }

    @Override
	public String getDisplayName() {
		return "功能结构建模";
	}
    
//  @Override
	public String getType()
    {
        return FunctionStructureModelFactory.TYPE;
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
    	NewFunctionStructureAction requireAction = new NewFunctionStructureAction("新建功能结构");
    	requireAction.selectionChanged(selection);
		menu.appendToGroup("group.edit", requireAction);
    }

    public Object getAdapter(Object adaptableObject, Class adapterType) {
    	return FunctionStructureWorkbenchAdapter.getInstance();
    }
}