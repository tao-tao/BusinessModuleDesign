package com.tansun.runtime.resource.adapter;

import org.eclipse.jdt.internal.ui.navigator.JavaNavigatorContentProvider;

import avicit.ui.runtime.core.node.DataMappingNode;

public class DataMappingWorkbenchAdapter extends AbstractModelWorkbenchAdapter
{

    private DataMappingWorkbenchAdapter()
    {
    }

    public static DataMappingWorkbenchAdapter getInstance()
    {
        return instance;
    }

    public String getLabel(Object object)
    {
    	DataMappingNode node = (DataMappingNode)object;
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
    	return new Object[0];
    }

	public boolean hasChildren(Object element) {
		return false;
	}

    private static final DataMappingWorkbenchAdapter instance = new DataMappingWorkbenchAdapter();
    private transient JavaNavigatorContentProvider javaContentProvider;
}