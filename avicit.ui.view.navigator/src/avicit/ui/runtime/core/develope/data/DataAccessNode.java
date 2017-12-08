package avicit.ui.runtime.core.develope.data;

import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;

import avicit.ui.core.runtime.resource.IFolderDelegate;
import avicit.ui.core.runtime.resource.IResourceDelegate;
import avicit.ui.runtime.core.IPackageContainer;
import avicit.ui.runtime.core.node.AbstractFolderNode;
import avicit.ui.runtime.core.requirement.designer.classm.ClassAction;
import avicit.ui.view.navigator.ImageRepository;

@SuppressWarnings("restriction")
public class DataAccessNode extends AbstractFolderNode implements IPackageContainer
{

	public DataAccessNode(IFolderDelegate folder)
    {
        super(folder);
        setOrder(98);
    }

    @Override
	public String getDisplayName() {
		return "数据访问层";
	}

	public String getType()
    {
        return DataAccessModelFactory.TYPE;
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
    	ClassAction guizeAction = new ClassAction("新建类");
    	guizeAction.setImageDescriptor(ImageRepository
				.getImageRepository("com.tansun.ui.navigator")
				.getImageDescriptor("com.tansun.ec.designer.classuml.ClassNode"));
    	guizeAction.selectionChanged(selection);
		menu.appendToGroup("group.edit", guizeAction);
    }
    public Object getAdapter(Object adaptableObject, Class adapterType) {
    	return DataAccessWorkbenchAdapter.getInstance();
    }
}