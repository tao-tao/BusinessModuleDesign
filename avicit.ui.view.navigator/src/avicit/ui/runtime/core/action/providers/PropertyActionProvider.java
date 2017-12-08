package avicit.ui.runtime.core.action.providers;

import org.eclipse.core.internal.resources.Project;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.navigator.CommonActionProvider;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;

import avicit.ui.runtime.core.INode;
import avicit.ui.runtime.core.action.PropertyAction;
import avicit.ui.runtime.core.node.ProjectNode;

@SuppressWarnings("restriction")
public class PropertyActionProvider extends CommonActionProvider {
    IStructuredSelection selection;

    public PropertyActionProvider()
    {
//        ImageRepository imageRepository = ImageRepository.getImageRepository("avicit.ui.view.navigator");
//        explorer.setImageDescriptor(imageRepository.getImageDescriptor("folder"));
    }

    public PropertyActionProvider(ISelectionProvider provider){
    	super();
    }

    @Override
	public void init(final ICommonActionExtensionSite aSite) {
		super.init(aSite);
	}

	public void fillContextMenu(IMenuManager menu)
    {
        super.fillContextMenu(menu);
        selection = (IStructuredSelection)getContext().getSelection();
        INode node = null;

        if( !(selection.getFirstElement() instanceof Project)){
            node = (INode)selection.getFirstElement();
        }

        if( node !=null && !(node instanceof ProjectNode)){
        	propertyAction = new PropertyAction("属性");
            propertyAction.selectionChanged(selection);
            menu.appendToGroup("group.properties", propertyAction);
        }
    }

    private PropertyAction propertyAction;
}
