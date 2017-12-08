package com.tansun.runtime.resource.adapter;

import avicit.ui.runtime.core.INode;
import avicit.ui.runtime.core.node.PresentationNode;
import avicit.ui.view.module.IModelFactory;
import avicit.ui.view.module.ModelManagerImpl;

public class PresentationWorkbenchAdapter extends AbstractModelWorkbenchAdapter
{

    private PresentationWorkbenchAdapter()
    {
    }

    public static PresentationWorkbenchAdapter getInstance()
    {
        return instance;
    }

    public String getLabel(Object object)
    {
    	PresentationNode node = (PresentationNode)object;
        return node.getDisplayName();
    }

//    public Object getParent(Object object)
//    {
//        JavaSourceNode node = (JavaSourceNode)object;
//        IFolderDelegate folder = (IFolderDelegate)node.getResource();
//        com.primeton.studio.runtime.core.IContribution contribution = RuntimeManager.createContribution(folder);
//        return new CategoryNode(folder, contribution, "bizlet");
//    }

    public Object[] getChildren(Object object)
    {
    	INode node = (INode)object;
        IModelFactory factory = ModelManagerImpl.getInstance().getModelFactory(/*"tansun.presentation", */"pres");
        if(factory != null)
        	return factory.getModelParser(object, null).getChildren(node);
        return new Object[0];
    }

    private static final PresentationWorkbenchAdapter instance = new PresentationWorkbenchAdapter();

}