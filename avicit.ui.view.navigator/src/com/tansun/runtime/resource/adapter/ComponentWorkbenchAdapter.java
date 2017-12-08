package com.tansun.runtime.resource.adapter;

import avicit.ui.runtime.core.cluster.function.ComponentModelFactory;
import avicit.ui.runtime.core.cluster.function.ComponentNode;
import avicit.ui.view.module.IModelFactory;
import avicit.ui.view.module.ModelManagerImpl;

public class ComponentWorkbenchAdapter extends AbstractModelWorkbenchAdapter
{

    private ComponentWorkbenchAdapter()
    {
    }

    public static ComponentWorkbenchAdapter getInstance()
    {
        return instance;
    }

    public String getLabel(Object object)
    {
    	ComponentNode node = (ComponentNode)object;
        return node.getDisplayName();
    }

    public Object[] getChildren(Object object)
    {
    	ComponentNode node = (ComponentNode)object;
        IModelFactory factory = ModelManagerImpl.getInstance().getModelFactory(node.getResource(), ComponentModelFactory.TYPE);
        if(factory != null)
        	return factory.getModelParser(object, null).getChildren(node);
        return new Object[0];
    }

	public boolean hasChildren(Object element) {
		return true;
	}

    private static final ComponentWorkbenchAdapter instance = new ComponentWorkbenchAdapter();

}