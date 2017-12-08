package avicit.ui.runtime.core.requirement.requirement;

import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;

import avicit.ui.core.runtime.resource.IFolderDelegate;
import avicit.ui.core.runtime.resource.IResourceDelegate;
import avicit.ui.runtime.core.IPackageContainer;
import avicit.ui.runtime.core.action.ExportDocumentAction;
import avicit.ui.runtime.core.node.AbstractFolderNode;
import avicit.ui.runtime.core.node.Action;
import avicit.ui.view.create.ModulCreate;

/**
 * @author Tao Tao
 *
 */
@SuppressWarnings("restriction")
public class RequirementNode extends AbstractFolderNode implements IPackageContainer
{

    public RequirementNode(IFolderDelegate folder)
    {
        super(folder);
        setOrder(1);
    }

    @Override
	public String getDisplayName() {
		return "需求分析";
	}
    
//  @Override
	public String getType()
    {
        return RequirementModelFactory.TYPE;
    }

	public String getConfigPath() {
		return this.getResource().getProjectRelativePath() + "/META-INF/requirement";
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
    	ExportDocumentAction requireAction = new ExportDocumentAction("导出需求文档", selection, ModulCreate.constru);
    	requireAction.selectionChanged(selection);
		menu.appendToGroup("group.edit", requireAction);
    }

    public Object getAdapter(Object adaptableObject, Class adapterType) {
    	return RequirementWorkbenchAdapter.getInstance();
    }
}