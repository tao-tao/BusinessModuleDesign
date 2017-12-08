package com.tansun.runtime.resource.adapter;

import org.eclipse.jdt.internal.ui.navigator.JavaNavigatorContentProvider;

import avicit.ui.runtime.core.node.ProcessTemplateNode;

public class ProcessTemplateWorkbenchAdapter extends AbstractModelWorkbenchAdapter
{

    private ProcessTemplateWorkbenchAdapter()
    {
        javaContentProvider = new JavaNavigatorContentProvider();
    }

    public static ProcessTemplateWorkbenchAdapter getInstance()
    {
        return instance;
    }

    public String getLabel(Object object)
    {
    	ProcessTemplateNode node = (ProcessTemplateNode)object;
        return node.getDisplayName();
    }

    public Object[] getChildren(Object object)
    {
    	return new Object[0];
    }

    @Override
	public boolean hasChildren(Object element) {
		return false;
	}

	private static final ProcessTemplateWorkbenchAdapter instance = new ProcessTemplateWorkbenchAdapter();
    private transient JavaNavigatorContentProvider javaContentProvider;

}