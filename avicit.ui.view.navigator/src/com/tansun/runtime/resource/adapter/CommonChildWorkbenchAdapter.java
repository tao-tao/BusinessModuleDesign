package com.tansun.runtime.resource.adapter;

import org.eclipse.jface.resource.ImageDescriptor;

import avicit.ui.runtime.core.node.CommonChildNode;

public class CommonChildWorkbenchAdapter extends AbstractModelWorkbenchAdapter
{

    private CommonChildWorkbenchAdapter()
    {
    }

    public static CommonChildWorkbenchAdapter getInstance()
    {
        return instance;
    }

    @Override
    public ImageDescriptor getImageDescriptor(Object object)
    {
    	CommonChildNode node = (CommonChildNode)object;
        return node.getImage();
    }

    public String getLabel(Object object)
    {
    	CommonChildNode node = (CommonChildNode)object;
        return node.getDisplayName();
    }

    public Object[] getChildren(Object object)
    {
		return new Object[0];
    }

	public boolean hasChildren(Object element) {
		return false;
	}

    private static final CommonChildWorkbenchAdapter instance = new CommonChildWorkbenchAdapter();
}