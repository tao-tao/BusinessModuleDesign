package com.tansun.runtime.resource.adapter;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdapterFactory;

import avicit.ui.core.runtime.resource.IResourceDelegate;
import avicit.ui.runtime.core.INode;
import avicit.ui.runtime.core.node.AbstractResourceNode;

public class ResourceAdapterFactory
    implements IAdapterFactory
{

    public ResourceAdapterFactory()
    {
    }

    public Object getAdapter(Object adaptableObject, Class adapterType)
    {
    	if(adaptableObject instanceof INode)
        {
        	INode element = (INode)adaptableObject;
            if(IResource.class.isAssignableFrom(adapterType))
            {
                IResourceDelegate resource = element.getResource();
                if(resource != null)
                    return resource.getAdapter(adapterType);
            }
        }
    	if(adaptableObject instanceof AbstractResourceNode)
        {
            AbstractResourceNode node = (AbstractResourceNode)adaptableObject;
            if(IResource.class.isAssignableFrom(adapterType))
                return node.getAdapter(adapterType);
        }
//    	if(adaptableObject instanceof PackageRoot)
//        {
//            AbstractResourceNode node = (AbstractResourceNode)adaptableObject;
//            if(IResource.class.isAssignableFrom(adapterType))
//                return node.getAdapter(adapterType);
//        }
        
        return null;
    }

    public Class[] getAdapterList()
    {
        return CLASSES;
    }

    private static final Class CLASSES[] = {
        IFile.class, IContainer.class, IFolder.class, IProject.class
    };

}