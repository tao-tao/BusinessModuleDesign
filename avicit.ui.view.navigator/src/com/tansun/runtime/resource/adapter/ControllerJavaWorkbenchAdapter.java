package com.tansun.runtime.resource.adapter;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.ui.navigator.JavaNavigatorContentProvider;
import org.eclipse.ui.model.IWorkbenchAdapter;

import avicit.ui.common.util.AdapterUtil;
import avicit.ui.runtime.core.node.ControllerJavaNode;

public class ControllerJavaWorkbenchAdapter extends AbstractModelWorkbenchAdapter
{

    private ControllerJavaWorkbenchAdapter()
    {
        javaContentProvider = new JavaNavigatorContentProvider();
    }

    public static ControllerJavaWorkbenchAdapter getInstance()
    {
        return instance;
    }

    public String getLabel(Object object)
    {
    	ControllerJavaNode node = (ControllerJavaNode)object;
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
    	ControllerJavaNode node = (ControllerJavaNode)object;
    	IJavaElement javaElement = JavaCore.create(node.getFile().getResource());
    	IWorkbenchAdapter adapter = (IWorkbenchAdapter)ExpressionAdapterManager.getInstance().getAdapter(javaElement, IWorkbenchAdapter.class);
        if(adapter == null)
            adapter = (IWorkbenchAdapter)AdapterUtil.getAdapter(javaElement, IWorkbenchAdapter.class);
        Object[] childs =  adapter.getChildren(javaElement);
        return new Object[]{childs[2]};
    }

    private static final ControllerJavaWorkbenchAdapter instance = new ControllerJavaWorkbenchAdapter();
    private transient JavaNavigatorContentProvider javaContentProvider;

}