package avicit.ui.runtime.core.develope.service;

import avicit.ui.runtime.core.INode;
import avicit.ui.runtime.core.node.GuizeNode;
import avicit.ui.runtime.core.node.GuizeWorkbenchAdapter;
import avicit.ui.view.module.GuizeModelFactory;
import avicit.ui.view.module.IModelFactory;
import avicit.ui.view.module.ModelManagerImpl;

import com.tansun.runtime.resource.adapter.AbstractModelWorkbenchAdapter;

public class ServiceWorkbenchAdapter extends AbstractModelWorkbenchAdapter{


	@Override
	public boolean hasChildren(Object element) {
		return true;
	}

    public ServiceWorkbenchAdapter()
    {
    }

    public static ServiceWorkbenchAdapter getInstance()
    {
        return instance;
    }

    @Override
	public Object getParent(Object object) {
		return super.getParent(object);
	}

	public String getLabel(Object object)
    {
		ServiceNode node = (ServiceNode)object;
        return node.getDisplayName();
    }

    public Object[] getChildren(Object object)
    {
    	INode node = (INode)object;
        IModelFactory factory = ModelManagerImpl.getInstance().getModelFactory(/*"tansun.controller", */ServiceModelFactory.TYPE);
//        IModelFactory factory = ModelManagerImpl.getInstance().getModelFactory(/*"tansun.controller", */GuizeModelFactory.TYPE);
        if(factory != null)
        	return factory.getModelParser(object, null).getChildren(node);
        return new Object[0];
    }

    private static final ServiceWorkbenchAdapter instance = new ServiceWorkbenchAdapter();


}
