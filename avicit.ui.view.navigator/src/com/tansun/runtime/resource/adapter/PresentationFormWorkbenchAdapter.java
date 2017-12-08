package com.tansun.runtime.resource.adapter;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Path;

import avicit.ui.runtime.core.node.PresentationFormNode;

public class PresentationFormWorkbenchAdapter extends AbstractModelWorkbenchAdapter
{

    private PresentationFormWorkbenchAdapter()
    {
    }

    public static PresentationFormWorkbenchAdapter getInstance()
    {
        return instance;
    }

    public String getLabel(Object object)
    {
    	PresentationFormNode node = (PresentationFormNode)object;
        return node.getDisplayName();
    }

    @Override
	public boolean hasChildren(Object element) {
    	PresentationFormNode node = (PresentationFormNode)element;
        IResource pagex = node.getFile().getResource();
        String fname = pagex.getName();
        if(fname.indexOf(".")>0)
        	fname = fname.substring(0,fname.indexOf("."));
        IFile javaFile = pagex.getParent().getFile(new Path(fname + ".java"));
        if(javaFile.exists())
        {
        	return true;
        }
    	return false;
	}

	public Object[] getChildren(Object object)
    {
//        List cats = new ArrayList();
		PresentationFormNode node = (PresentationFormNode)object;
        List presFiles = new ArrayList();
        IResource pagex = node.getFile().getResource();
        String fname = pagex.getName();
        if(fname.indexOf(".")>0)
        	fname = fname.substring(0,fname.indexOf("."));
        IFile javaFile = pagex.getParent().getFile(new Path(fname + ".java"));
        if(javaFile.exists())
        {
        	return new Object[]{javaFile};
        }
        return presFiles.toArray();
    }

	private static final PresentationFormWorkbenchAdapter instance = new PresentationFormWorkbenchAdapter();

}