package com.tansun.runtime.resource.adapter;

import org.eclipse.core.resources.ResourcesPlugin;

import avicit.ui.runtime.core.node.ProjectNode;
import avicit.ui.view.module.IModelFactory;
import avicit.ui.view.module.ModelManagerImpl;
import avicit.ui.view.module.ProjectModelFactory;

public class ProjectWorkbenchAdapter extends AbstractWorkbenchAdapter
{

    private ProjectWorkbenchAdapter()
    {}

    public static ProjectWorkbenchAdapter getInstance()
    {
        return instance;
    }

    public Object[] getChildren(Object object)
    {
    	ProjectNode node = (ProjectNode)object;
        IModelFactory factory = ModelManagerImpl.getInstance().getModelFactory(node.getResource(), ProjectModelFactory.TYPE);;
        if(factory != null)
        	return factory.getModelParser(object, null).getChildren(node);
        return new Object[0];
    }

    public Object getParent(Object object)
    {
        return ResourcesPlugin.getWorkspace().getRoot();
    }

    private static final ProjectWorkbenchAdapter instance = new ProjectWorkbenchAdapter();

}