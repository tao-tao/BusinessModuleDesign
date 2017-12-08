package avicit.ui.runtime.core.usecase.common;

import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;

import avicit.ui.core.runtime.resource.IFileDelegate;
import avicit.ui.core.runtime.resource.IResourceDelegate;
import avicit.ui.runtime.core.action.DeleteFileAction;
import avicit.ui.runtime.core.node.AbstractFileNode;

/**
 * @author Tao Tao
 *
 */
@SuppressWarnings("restriction")
public class UseCaseCommonNode extends AbstractFileNode
{

    public UseCaseCommonNode(IFileDelegate file)
    {
    	 super(file);
         setOrder(60);
    }

    @Override
	public String getDisplayName() {
    	String name = this.getFile().getName();
//		int dotIndex = name.indexOf(".");
//		if(dotIndex>0)
//			name = name.substring(0,dotIndex);
		return name;
	}

	public String getType() {
        return "ucd";
    }

	public Object getAdapter(Class adapter) {
        if(IResourceDelegate.class == adapter)
            return getResource();
        if(IProject.class == adapter) {
			try {
				return getResource().getProject().getAdapter(adapter);
			} catch (ResourceException e) {
				return null;
			}
        }
//        else if(IResource.class == adapter)
//		{
//			return this.getResource();
//		}
        return super.getAdapter(adapter);
    }

    public Object getAdapter(Object adaptableObject, Class adapterType) {
    	return UseCaseCommonNodeAdapter.getInstance();
    }

    public void createAction(IStructuredSelection selection,IMenuManager menu){

    	Object obj = selection.getFirstElement();

		DeleteFileAction delAction = null;

    	if( obj instanceof UseCaseCommonNode ){
    		if(((UseCaseCommonNode) obj).getDisplayName().endsWith(".java")){
    			delAction = new DeleteFileAction("删除类");
    		} else if(((UseCaseCommonNode) obj).getDisplayName().endsWith(".ucd")){
    			delAction = new DeleteFileAction("删除用例建模");
    		} else if (((UseCaseCommonNode) obj).getDisplayName().endsWith(".jsp") ||
    				((UseCaseCommonNode) obj).getDisplayName().endsWith(".js")){
    			delAction = new DeleteFileAction("删除JSP");
    		}
    	}

    	if( delAction != null ){
    		delAction.selectionChanged(selection);
    		menu.appendToGroup("group.edit", delAction);
    	}
    }
}