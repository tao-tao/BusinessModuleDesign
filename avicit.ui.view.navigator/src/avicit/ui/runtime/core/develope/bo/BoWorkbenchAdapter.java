package avicit.ui.runtime.core.develope.bo;

import avicit.ui.runtime.core.INode;
import avicit.ui.view.module.IModelFactory;
import avicit.ui.view.module.ModelManagerImpl;

import com.tansun.runtime.resource.adapter.AbstractModelWorkbenchAdapter;

public class BoWorkbenchAdapter extends AbstractModelWorkbenchAdapter{


	@Override
	public boolean hasChildren(Object element) {
		return true;
	}

    public BoWorkbenchAdapter()
    {
    }

    public static BoWorkbenchAdapter getInstance()
    {
        return instance;
    }

    @Override
	public Object getParent(Object object) {
		return super.getParent(object);
	}

	public String getLabel(Object object)
    {
		BoNode node = (BoNode)object;
        return node.getDisplayName();
    }

    public Object[] getChildren(Object object)
    {
    	INode node = (INode)object;
        IModelFactory factory = ModelManagerImpl.getInstance().getModelFactory(/*"tansun.controller", */BoModelFactory.TYPE);
//        IModelFactory factory = ModelManagerImpl.getInstance().getModelFactory(/*"tansun.controller", */GuizeModelFactory.TYPE);
        if(factory != null)
        	return factory.getModelParser(object, null).getChildren(node);
        return new Object[0];
    }

    private static final BoWorkbenchAdapter instance = new BoWorkbenchAdapter();
}
