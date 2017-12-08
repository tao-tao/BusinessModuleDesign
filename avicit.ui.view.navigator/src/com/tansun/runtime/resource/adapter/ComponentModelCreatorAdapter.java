package com.tansun.runtime.resource.adapter;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

import avicit.ui.core.runtime.resource.EclipseResourceDelegate;
import avicit.ui.core.runtime.resource.EclipseResourceManager;
import avicit.ui.core.runtime.resource.EclipseSourceFolderDelegate;
import avicit.ui.core.runtime.resource.IFileDelegate;
import avicit.ui.runtime.core.IModelCreator;
import avicit.ui.runtime.core.cluster.function.ComponentModelFactory;
import avicit.ui.runtime.core.cluster.function.ComponentNode;
import avicit.ui.runtime.core.node.AbstractNode;
import avicit.ui.view.module.IModelFactory;
import avicit.ui.view.module.ModelManagerImpl;

public class ComponentModelCreatorAdapter implements IModelCreator
{

    private ComponentModelCreatorAdapter()
    {
    }

    public static ComponentModelCreatorAdapter getInstance()
    {
        return instance;
    }
    
//	@Override
	public AbstractNode getOrCreateNode(IFileDelegate file, boolean forceCreate) {
		EclipseSourceFolderDelegate sourceFolder = EclipseResourceManager.getSourceFolder((EclipseResourceDelegate) file);
		if(sourceFolder == null)
			return null;
		
		Object model = ModelManagerImpl.getInstance().getPool().getModel(sourceFolder, ComponentModelFactory.TYPE, false, null);
		if(model instanceof ComponentNode)
		{
			IModelFactory[] factories = ModelManagerImpl.getInstance().getAllModelFactories();
			String ext = file.getName();
			int j = ext.indexOf("."); 
			if(j>0)
				ext = ext.substring(j+1);
	        for(int i=0;i<factories.length; i++)
	        {
	        	String cat = factories[i].getCategory();
	        	String[] exts = factories[i].getExtensionNames();
	        	if(cat.equals("view"))
	        	{
	        		if(ArrayUtils.contains(exts, ext))
	        		{
	        			Object pmodel = ModelManagerImpl.getInstance().getPool().getModel(sourceFolder, factories[i].getType(), forceCreate, null);
	        			if(pmodel != null && factories[i] instanceof IModelCreator)
	        			{
	        				return ((IModelCreator)factories[i]).getOrCreateNode(file, forceCreate);
	        			}
	        		}
//	        		if(factories[i].isAcceptable(node.getFolder()))
//	        		{
//		        		cats.add(factories[i].getModelParser(node.getFolder(), null).parse(node.getFolder(), null));
//	        		}
	        	}
	        }
		}
		return null;
	}

    public Object[] getChildren(Object object)
    {
        ComponentNode node = (ComponentNode)object;
        List cats = node.getChildren();
        if(cats == null)
        {
	        cats = new ArrayList();
	        IModelFactory[] factories = ModelManagerImpl.getInstance().getAllModelFactories();
	        for(int i=0;i<factories.length; i++)
	        {
	        	String cat = factories[i].getCategory();
	        	if(cat.equals("view"))
	        	{
	        		if(factories[i].isAcceptable(node.getFolder()))
	        		{
		        		cats.add(factories[i].getModelParser(node.getFolder(), null).parse(node.getFolder(), null));
	        		}
	        	}
	        }
	        node.setChildren(cats);
        }
        return cats.toArray();
    }

    private static final ComponentModelCreatorAdapter instance = new ComponentModelCreatorAdapter();

}