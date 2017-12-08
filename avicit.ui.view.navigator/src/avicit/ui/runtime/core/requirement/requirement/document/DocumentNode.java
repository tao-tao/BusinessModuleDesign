package avicit.ui.runtime.core.requirement.requirement.document;


import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;

import avicit.ui.core.runtime.resource.IFolderDelegate;
import avicit.ui.core.runtime.resource.IResourceDelegate;
import avicit.ui.runtime.core.IPackageContainer;
import avicit.ui.runtime.core.node.AbstractFolderNode;
import avicit.ui.runtime.core.requirement.requirement.RequirementWorkbenchAdapter;

/**
 * @author Tao Tao
 *
 */
@SuppressWarnings("restriction")
public class DocumentNode extends AbstractFolderNode implements IPackageContainer
{

    public DocumentNode(IFolderDelegate folder)
    {
        super(folder);
        setOrder(300);
    }

    @Override
	public String getDisplayName() {
		return "需求文档";
	}

//  @Override
	public String getType()
    {
        return DocumentModelFactory.TYPE;
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
    }

    public Object getAdapter(Object adaptableObject, Class adapterType) {
    	return RequirementWorkbenchAdapter.getInstance();
    }
}