package com.tansun.runtime.resource.adapter;

import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.model.IWorkbenchAdapter;

import avicit.ui.runtime.core.INode;

public class AbstractModelWorkbenchAdapter extends AbstractWorkbenchAdapter
{

    public AbstractModelWorkbenchAdapter()
    {}

    public Object getParent(Object object)
    {
    	if(object instanceof INode)
    		return ((INode)object).getParent();
    	return null;
    }

    public Object[] getChildren(Object object)
    {
        INode node = (INode)object;
        IWorkbenchAdapter adapter = (IWorkbenchAdapter)Platform.getAdapterManager().getAdapter(node.getModel(), IWorkbenchAdapter.class);
        if(adapter != null)
            return adapter.getChildren(node.getModel());
        else
            return NO_CHILDREN;
    }
}