package avicit.ui.runtime.core.requirement.analysis.epc;


import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;

import avicit.ui.core.runtime.resource.IFileDelegate;
import avicit.ui.core.runtime.resource.IFolderDelegate;
import avicit.ui.core.runtime.resource.IResourceDelegate;
import avicit.ui.runtime.core.IPackageContainer;
import avicit.ui.runtime.core.action.OpenEpcNodeAction;
import avicit.ui.runtime.core.action.OpenFunctionNodeAction;
import avicit.ui.runtime.core.node.AbstractFileNode;
import avicit.ui.runtime.core.node.AbstractFolderNode;
import avicit.ui.runtime.core.node.Action;
import avicit.ui.view.module.GuizeModelFactory;



@SuppressWarnings("restriction")
public class EpcNode extends AbstractFileNode
{
	String editorID;
	public static String MODLE_EDITOR_URL = "http://localhost:8087/Platform_V6/engine/designer/v2/business_flow_designer.jsp?id=4028801a4738cbf8014738d8ae7d0003";
    public EpcNode(IFileDelegate file)
    {
        super(file);
        setOrder(1000);
    }

    @Override
	public String getDisplayName() {
		return "业务活动建模";
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
		else if(IResource.class == adapter)
		{
            return this.getFile().getResource();
		}
		else if(IDoubleClickListener.class == adapter)
		{
			return new OpenEpcNodeAction();
		}
		return null;
    }	
	
    
    public void createAction(IStructuredSelection selection,IMenuManager menu){
    	Action guizeAction = new Action("EPC建模");
    	guizeAction.selectionChanged(selection);
		menu.appendToGroup("group.edit", guizeAction);
    }
    public Object getAdapter(Object adaptableObject, Class adapterType) {
    	return EpcWorkbenchAdapter.getInstance();
    }
	public String getEditorID() {
		return editorID;
	}

	public void setEditorID(String editorID) {
		this.editorID = editorID;
	}   
}