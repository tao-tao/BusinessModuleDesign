package avicit.ui.runtime.core.requirement.requirement;

import avicit.ui.runtime.core.INode;
import avicit.ui.view.module.IModelFactory;
import avicit.ui.view.module.ModelManagerImpl;

import com.tansun.runtime.resource.adapter.AbstractModelWorkbenchAdapter;

/**
 * @author TaoTao
 *
 */
public class RequirementWorkbenchAdapter extends AbstractModelWorkbenchAdapter {

	@Override
	public boolean hasChildren(Object element) {
		return true;
	}

	public RequirementWorkbenchAdapter() {
		super();
	}

	public static RequirementWorkbenchAdapter getInstance(){
		return instance;
	}

	@Override
	public String getLabel(Object object) {
		RequirementNode node = (RequirementNode) object;
		return node.getDisplayName();
	}

	@Override
	public Object[] getChildren(Object object) {
    	INode node = (INode)object;
        IModelFactory factory = ModelManagerImpl.getInstance().getModelFactory(RequirementModelFactory.TYPE);;
        if(factory != null)
        	return factory.getModelParser(object, null).getChildren(node);
        return new Object[0];
	}

	private static final RequirementWorkbenchAdapter instance = new RequirementWorkbenchAdapter();
}
