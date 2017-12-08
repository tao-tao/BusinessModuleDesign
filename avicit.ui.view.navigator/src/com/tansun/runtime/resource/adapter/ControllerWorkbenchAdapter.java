package com.tansun.runtime.resource.adapter;

import avicit.ui.runtime.core.INode;
import avicit.ui.runtime.core.node.ControllerNode;
import avicit.ui.view.module.IModelFactory;
import avicit.ui.view.module.ModelManagerImpl;

public class ControllerWorkbenchAdapter extends AbstractModelWorkbenchAdapter
{

	@Override
	public boolean hasChildren(Object element) {
		return true;
	}

    private ControllerWorkbenchAdapter()
    {
    }

    public static ControllerWorkbenchAdapter getInstance()
    {
        return instance;
    }

    @Override
	public Object getParent(Object object) {
		return super.getParent(object);
	}

	public String getLabel(Object object)
    {
    	ControllerNode node = (ControllerNode)object;
        return node.getDisplayName();
    }

    public Object[] getChildren(Object object)
    {
    	INode node = (INode)object;
      //  IModelFactory factory = ModelManagerImpl.getInstance().getModelFactory(/*"tansun.controller", */ControllerModelFactory.TYPE);;
        IModelFactory factory = ModelManagerImpl.getInstance().getModelFactory(/*"tansun.controller", */"cont");
        if(factory != null)
        	return factory.getModelParser(object, null).getChildren(node);
        return new Object[0];
    }

    private static final ControllerWorkbenchAdapter instance = new ControllerWorkbenchAdapter();

}