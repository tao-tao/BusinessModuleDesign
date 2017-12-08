package avicit.ui.runtime.core.cluster.function;


import avicit.ui.runtime.core.INode;
import avicit.ui.view.module.IModelFactory;
import avicit.ui.view.module.ModelManagerImpl;

import com.tansun.runtime.resource.adapter.AbstractModelWorkbenchAdapter;

public class ComponentWorkbenchAdapter extends AbstractModelWorkbenchAdapter
{

	@Override
	public boolean hasChildren(Object element) {
		return true;
	}

    public ComponentWorkbenchAdapter()
    {
    }

    public static ComponentWorkbenchAdapter getInstance()
    {
        return instance;
    }

    @Override
	public Object getParent(Object object) {
		return super.getParent(object);
	}

	public String getLabel(Object object)
    {
		ComponentNode node = (ComponentNode)object;
        return node.getDisplayName();
    }

    public Object[] getChildren(Object object)
    {
    	INode node = (INode)object;
        IModelFactory factory = ModelManagerImpl.getInstance().getModelFactory(ComponentModelFactory.TYPE);;
        if(factory != null)
        	return factory.getModelParser(object, null).getChildren(node);
        return new Object[0];
    }

    private static final ComponentWorkbenchAdapter instance = new ComponentWorkbenchAdapter();
}