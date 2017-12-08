package com.tansun.runtime.resource.adapter;

import avicit.ui.runtime.core.node.DataSQLNode;

public class DataSQLWorkbenchAdapter extends AbstractModelWorkbenchAdapter
{

    private DataSQLWorkbenchAdapter()
    {
    }

    public static DataSQLWorkbenchAdapter getInstance()
    {
        return instance;
    }

    public String getLabel(Object object)
    {
    	DataSQLNode node = (DataSQLNode)object;
        return node.getDisplayName();
    }

    public Object[] getChildren(Object object)
    {
    	return new Object[0];
    }

	public boolean hasChildren(Object element) {
		return false;
	}

    private static final DataSQLWorkbenchAdapter instance = new DataSQLWorkbenchAdapter();
}