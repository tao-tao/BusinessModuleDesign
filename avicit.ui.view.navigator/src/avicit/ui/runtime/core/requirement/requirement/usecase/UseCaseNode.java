package avicit.ui.runtime.core.requirement.requirement.usecase;

import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;

import avicit.ui.core.runtime.resource.IFolderDelegate;
import avicit.ui.core.runtime.resource.IResourceDelegate;
import avicit.ui.runtime.core.IPackageContainer;
import avicit.ui.runtime.core.action.NewUseCaseAction;
import avicit.ui.runtime.core.node.AbstractFolderNode;
import avicit.ui.view.navigator.ImageRepository;

/**
 * @author Tao Tao
 *
 */
@SuppressWarnings("restriction")
public class UseCaseNode extends AbstractFolderNode implements IPackageContainer
{

    public UseCaseNode(IFolderDelegate folder)
    {
        super(folder);
        setOrder(10);
    }

    @Override
	public String getDisplayName() {
		return "用例建模";
	}

//  @Override
	public String getType()
    {
        return UseCaseModelFactory.TYPE;
    }

	public String getConfigPath() {
		return this.getResource().getProjectRelativePath() + "/META-INF/requirement/usecase";
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
    	NewUseCaseAction useAction = new NewUseCaseAction("新建用例建模");
    	useAction.selectionChanged(selection);
    	useAction.setImageDescriptor(ImageRepository
				.getImageRepository("avicit.ui.view.navigator")
				.getImageDescriptor("avicit.ui.runtime.core.requirement.analysis.usecase.UseCaseNode"));
		menu.appendToGroup("group.edit", useAction);
    }
    public Object getAdapter(Object adaptableObject, Class adapterType) {
    	return UseCaseWorkbenchAdapter.getInstance();
    }
}