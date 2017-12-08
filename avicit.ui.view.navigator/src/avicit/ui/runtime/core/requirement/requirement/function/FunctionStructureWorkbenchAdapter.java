package avicit.ui.runtime.core.requirement.requirement.function;

import avicit.ui.runtime.core.INode;
import avicit.ui.view.module.IModelFactory;
import avicit.ui.view.module.ModelManagerImpl;

import com.tansun.runtime.resource.adapter.AbstractModelWorkbenchAdapter;

/**
 * @author TaoTao
 *
 */
public class FunctionStructureWorkbenchAdapter extends AbstractModelWorkbenchAdapter {

	@Override
	public boolean hasChildren(Object element) {
		return true;
	}

	public FunctionStructureWorkbenchAdapter() {
		super();
	}

	public static FunctionStructureWorkbenchAdapter getInstance(){
		return instance;
	}

	@Override
	public String getLabel(Object object) {
		FunctionStructureNode node = (FunctionStructureNode) object;
		return node.getDisplayName();
	}

	@Override
	public Object[] getChildren(Object object) {
    	INode node = (INode)object;
        IModelFactory factory = ModelManagerImpl.getInstance().getModelFactory(FunctionStructureModelFactory.TYPE);;
        if(factory != null)
        	return factory.getModelParser(object, null).getChildren(node);
        return new Object[0];
	}

	private static final FunctionStructureWorkbenchAdapter instance = new FunctionStructureWorkbenchAdapter();
}
