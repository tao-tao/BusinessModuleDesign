package avicit.ui.runtime.core.node;


import java.util.List;

import org.eclipse.core.internal.resources.File;
import org.eclipse.core.internal.resources.Project;
import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;

import avicit.ui.common.util.StringUtil;
import avicit.ui.core.runtime.resource.EclipseFolderDelegate;
import avicit.ui.core.runtime.resource.IResourceDelegate;
import avicit.ui.runtime.core.IPackageContainer;
import avicit.ui.runtime.core.action.DeleteFolderAction;
import avicit.ui.runtime.core.action.DeletePackageAction;
import avicit.ui.view.create.ModulCreate;

public class PackageNode extends AbstractFolderNode
{
	public String packName;
	public IFolder parent;
	public String type;

    public PackageNode(String pack, IFolder folder, List f)
    {
        super(new EclipseFolderDelegate(folder));
        this.packName = pack;
        this.setChildren(f);
        setOrder(1);
    }

    public PackageNode(String pack, IFolder folder, IFolder parent, List f)
    {
        super(new EclipseFolderDelegate(folder));
        this.parent = parent;
        this.packName = pack;
        this.setChildren(f);
        setOrder(1);
    }

    public void setType(String type) {
		this.type = type;
	}

	public Object getModel()
    {
        return this.getFolder();
    }

    @Override
	public String getDisplayName() {
    	if(StringUtil.isEmpty(this.packName))
    		return "(Default Package)";
		return this.packName;
	}

	public String getType()
    {
        return this.type;
    }

	public Object getAdapter(Class adapter) {

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
    	Project obj = (Project) ((AbstractFolderNode)selection.getFirstElement()).getResource().getResource().getProject();

		DeletePackageAction delAction = new DeletePackageAction("删除包");

		delAction.selectionChanged(selection);
		menu.appendToGroup("group.edit", delAction);
    }
}