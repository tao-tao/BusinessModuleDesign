package com.tansun.runtime.resource.adapter;

import org.eclipse.jdt.internal.ui.navigator.JavaNavigatorContentProvider;

import avicit.ui.runtime.core.INode;
import avicit.ui.runtime.core.node.JavaSourceNode;
import avicit.ui.view.module.IModelFactory;
import avicit.ui.view.module.ModelManagerImpl;

public class JavaSourceWorkbenchAdapter extends AbstractModelWorkbenchAdapter
{

    private JavaSourceWorkbenchAdapter()
    {
        javaContentProvider = new JavaNavigatorContentProvider(true);
        javaContentProvider.setIsFlatLayout(true);
    }

    public static JavaSourceWorkbenchAdapter getInstance()
    {
        return instance;
    }

    public String getLabel(Object object)
    {
    	JavaSourceNode node = (JavaSourceNode)object;
        return node.getDisplayName();
    }

    public Object[] getChildren(Object object)
    {
    	INode node = (INode)object;
        IModelFactory factory = ModelManagerImpl.getInstance().getModelFactory(/*"tansun.java",*/"java");
        if(factory != null)
        	return factory.getModelParser(object, null).getChildren(node);
        return new Object[0];
    }

    private static final JavaSourceWorkbenchAdapter instance = new JavaSourceWorkbenchAdapter();
    private transient JavaNavigatorContentProvider javaContentProvider;

}