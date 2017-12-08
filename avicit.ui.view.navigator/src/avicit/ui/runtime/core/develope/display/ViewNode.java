package avicit.ui.runtime.core.develope.display;

import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;

import avicit.ui.core.runtime.resource.IFolderDelegate;
import avicit.ui.core.runtime.resource.IResourceDelegate;
import avicit.ui.runtime.core.IPackageContainer;
import avicit.ui.runtime.core.action.NewJspAction;
import avicit.ui.runtime.core.node.AbstractFolderNode;
import avicit.ui.view.navigator.ImageRepository;

@SuppressWarnings("restriction")
public class ViewNode extends AbstractFolderNode implements IPackageContainer
{

	public ViewNode(IFolderDelegate folder)
    {
        super(folder);
        setOrder(94);
    }

    @Override
	public String getDisplayName() {
		return "展示层";
	}

	public String getType()
    {
        return ViewModelFactory.TYPE;
    }

	public String getConfigPath() {
		return this.getResource().getProjectRelativePath();
	}

    @SuppressWarnings("rawtypes")
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
    	NewJspAction guizeAction = new NewJspAction("新建JSP");
    	guizeAction.setImageDescriptor(ImageRepository
				.getImageRepository("com.tansun.ui.navigator")
				.getImageDescriptor("com.tansun.ec.component.jtyscomponent.XmlNode"));
    	guizeAction.selectionChanged(selection);
		menu.appendToGroup("group.edit", guizeAction);
    }

    @SuppressWarnings("rawtypes")
	public Object getAdapter(Object adaptableObject, Class adapterType) {
    	return ViewWorkbenchAdapter.getInstance();
    }
}