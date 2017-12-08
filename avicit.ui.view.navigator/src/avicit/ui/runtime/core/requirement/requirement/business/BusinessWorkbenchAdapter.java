package avicit.ui.runtime.core.requirement.requirement.business;

import avicit.ui.runtime.core.INode;
import avicit.ui.view.module.IModelFactory;
import avicit.ui.view.module.ModelManagerImpl;

import com.tansun.runtime.resource.adapter.AbstractModelWorkbenchAdapter;

/**
 * @author TaoTao
 *
 */
public class BusinessWorkbenchAdapter extends AbstractModelWorkbenchAdapter {

	@Override
	public boolean hasChildren(Object element) {
		return true;
	}

	public BusinessWorkbenchAdapter() {
		super();
	}

	public static BusinessWorkbenchAdapter getInstance(){
		return instance;
	}

	@Override
	public String getLabel(Object object) {
		BusinessNode node = (BusinessNode) object;
		return node.getDisplayName();
	}

	@Override
	public Object[] getChildren(Object object) {
    	INode node = (INode)object;
        IModelFactory factory = ModelManagerImpl.getInstance().getModelFactory(BusinessModelFactory.TYPE);;
        if(factory != null)
        	return factory.getModelParser(object, null).getChildren(node);
        return new Object[0];
	}

	private static final BusinessWorkbenchAdapter instance = new BusinessWorkbenchAdapter();
}
