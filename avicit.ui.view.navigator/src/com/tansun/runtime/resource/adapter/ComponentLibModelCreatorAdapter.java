package com.tansun.runtime.resource.adapter;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

import avicit.ui.core.runtime.resource.EclipseResourceDelegate;
import avicit.ui.core.runtime.resource.EclipseResourceManager;
import avicit.ui.core.runtime.resource.EclipseSourceFolderDelegate;
import avicit.ui.core.runtime.resource.IFileDelegate;
import avicit.ui.core.runtime.resource.IFolderDelegate;
import avicit.ui.core.runtime.resource.IResourceDelegate;
import avicit.ui.runtime.core.IModelCreator;
import avicit.ui.runtime.core.cluster.function.ComponentModelFactory;
import avicit.ui.runtime.core.cluster.function.ComponentNode;
import avicit.ui.runtime.core.node.AbstractNode;
import avicit.ui.runtime.core.node.ComponentLibNode;
import avicit.ui.view.module.IModelFactory;
import avicit.ui.view.module.ModelManagerImpl;

public class ComponentLibModelCreatorAdapter extends AbstractModelWorkbenchAdapter implements IModelCreator
{

    private ComponentLibModelCreatorAdapter()
    {
    }

    public static ComponentLibModelCreatorAdapter getInstance()
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
        ComponentLibNode node = (ComponentLibNode)object;
        List cats = node.getChildren();
        if(cats == null)
        {
	        cats = new ArrayList();
	        IResourceDelegate[] vers = ((IFolderDelegate)node.getResource()).getChildren();
	        for(int i=0;i<vers.length; i++)
	        {
	        	Object model =  ModelManagerImpl.getInstance().getPool().getModel(vers[i], ComponentModelFactory.TYPE, true, null);
	        	if(model instanceof ComponentNode)
	        	{
		        	cats.add(model);
	        	}
	        }
	        node.setChildren(cats);
        }
        return cats.toArray();
    }

    private static final ComponentLibModelCreatorAdapter instance = new ComponentLibModelCreatorAdapter();

}