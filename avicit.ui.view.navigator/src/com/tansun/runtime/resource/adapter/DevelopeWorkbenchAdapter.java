package com.tansun.runtime.resource.adapter;

import avicit.ui.runtime.core.INode;
import avicit.ui.runtime.core.node.DevelopNode;
import avicit.ui.view.module.DevelopeModelFactory;
import avicit.ui.view.module.IModelFactory;
import avicit.ui.view.module.ModelManagerImpl;


public class DevelopeWorkbenchAdapter extends AbstractModelWorkbenchAdapter
{

	@Override
	public boolean hasChildren(Object element) {
		return true;
	}

    public DevelopeWorkbenchAdapter()
    {
    }

    public static DevelopeWorkbenchAdapter getInstance()
    {
        return instance;
    }

    @Override
	public Object getParent(Object object) {
		return super.getParent(object);
	}

	public String getLabel(Object object)
    {
		DevelopNode node = (DevelopNode)object;
        return node.getDisplayName();
    }

    public Object[] getChildren(Object object)
    {
    	INode node = (INode)object;
        IModelFactory factory = ModelManagerImpl.getInstance().getModelFactory(/*"tansun.controller", */DevelopeModelFactory.TYPE);;
        if(factory != null)
        	return factory.getModelParser(object, null).getChildren(node);
        return new Object[0];
    }

    private static final DevelopeWorkbenchAdapter instance = new DevelopeWorkbenchAdapter();

}