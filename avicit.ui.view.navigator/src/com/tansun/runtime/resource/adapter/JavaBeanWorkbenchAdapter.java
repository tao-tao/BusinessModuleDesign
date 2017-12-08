package com.tansun.runtime.resource.adapter;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jface.resource.ImageDescriptor;

import avicit.ui.runtime.core.node.JavaBeanNode;
import avicit.ui.view.navigator.ImageRepository;

public class JavaBeanWorkbenchAdapter extends AbstractModelWorkbenchAdapter
{

    private JavaBeanWorkbenchAdapter()
    {
    }

    public static JavaBeanWorkbenchAdapter getInstance()
    {
        return instance;
    }

    @Override
    public ImageDescriptor getImageDescriptor(Object object)
    {
    	JavaBeanNode node = (JavaBeanNode)object;
    	ICompilationUnit cu = node.getCU();
    	String name = object.getClass().getName();
    	if(cu != null && cu.exists())
    		name = "source";
        ImageRepository imageRepository = ImageRepository.getImageRepository("com.tansun.ui.navigator");
        return imageRepository.getImageDescriptor(name);//decorateImage(imageRepository.getImageDescriptor(name), object);
    }

    public String getLabel(Object object)
    {
    	JavaBeanNode node = (JavaBeanNode)object;
        return node.getDisplayName();
    }

    public Object[] getChildren(Object object)
    {
    	return new Object[0];
    }

	public boolean hasChildren(Object element) {
		return false;
	}

    private static final JavaBeanWorkbenchAdapter instance = new JavaBeanWorkbenchAdapter();
}