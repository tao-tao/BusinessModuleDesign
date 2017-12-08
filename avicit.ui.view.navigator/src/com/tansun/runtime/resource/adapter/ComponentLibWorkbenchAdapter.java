package com.tansun.runtime.resource.adapter;

import avicit.ui.runtime.core.node.ComponentLibNode;
import avicit.ui.view.module.ComponentLibModelFactory;
import avicit.ui.view.module.IModelFactory;
import avicit.ui.view.module.ModelManagerImpl;

public class ComponentLibWorkbenchAdapter extends AbstractModelWorkbenchAdapter
{

    private ComponentLibWorkbenchAdapter()
    {}

    public static ComponentLibWorkbenchAdapter getInstance()
    {
        return instance;
    }

    public String getLabel(Object object)
    {
    	ComponentLibNode node = (ComponentLibNode)object;
        return node.getDisplayName();
    }

    public Object[] getChildren(Object object)
    {
    	ComponentLibNode node = (ComponentLibNode)object;
        IModelFactory factory = ModelManagerImpl.getInstance().getModelFactory(node.getResource(), ComponentLibModelFactory.TYPE);
        if(factory != null)
        	return factory.getModelParser(object, null).getChildren(node);
        return new Object[0];
    }

    private static final ComponentLibWorkbenchAdapter instance = new ComponentLibWorkbenchAdapter();

}