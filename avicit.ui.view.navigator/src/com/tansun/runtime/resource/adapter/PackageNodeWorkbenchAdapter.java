package com.tansun.runtime.resource.adapter;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

import avicit.ui.common.util.FileUtil;
import avicit.ui.core.runtime.resource.EclipseFileDelegate;
import avicit.ui.runtime.core.INode;
import avicit.ui.runtime.core.develope.display.ViewNode;
import avicit.ui.runtime.core.develope.logic.LogicNode;
import avicit.ui.runtime.core.node.PackageNode;
import avicit.ui.runtime.core.requirement.analysis.usecase.FunctionUseCaseNode;
import avicit.ui.runtime.core.requirement.requirement.function.FunctionStructureNode;
import avicit.ui.runtime.core.usecase.common.UseCaseCommonNode;

public class PackageNodeWorkbenchAdapter extends AbstractModelWorkbenchAdapter
{

    private PackageNodeWorkbenchAdapter()
    {
    }

    public static PackageNodeWorkbenchAdapter getInstance()
    {
        return instance;
    }

    public String getLabel(Object object)
    {
    	PackageNode node = (PackageNode)object;
        return node.getDisplayName();
    }

//    public Object[] getChildren(Object object)
//    {
//    	PackageNode node = (PackageNode)object;
//        List presFiles = node.getChildren();
//        if(presFiles == null)
//        {	
//        	presFiles = new ArrayList();
//        	node.setChildren(presFiles);
//        }
//        return presFiles.toArray();
//    }
    public Object[] getChildren(Object object){

    	PackageNode node = (PackageNode) object;
    	String matchExtString = "";
    	if( node.getParent() instanceof FunctionStructureNode ){
    		matchExtString = ".function";
    	}else{
    		matchExtString = node.getParent() instanceof ViewNode ? ".jsp":".java";
    	}

		List children = node.getChildren();
		if(null == children)
			children = new ArrayList();
		if (true) {
			List presFiles = new ArrayList();
			IFolder folder = (IFolder) node.getResource().getResource();
			IResource[] resources = null;
			try {
				resources = ((IFolder) folder).members();
			} catch (CoreException e) {
				e.printStackTrace();
			}
			List files = new ArrayList();
			for (int i = 0; i < resources.length; i++) {
				if(resources[i] instanceof IFolder)
				{
					continue;
				}else {
					String ext = resources[i].getName();
					if (ext.endsWith(matchExtString)) {

						Object p = null;

						if (ext.endsWith(".function")) {
							p = new FunctionUseCaseNode(new EclipseFileDelegate((IFile) resources[i]));
//							((FunctionUseCaseNode)p).setParent(node);
						} else {
							p = new UseCaseCommonNode(new EclipseFileDelegate((IFile) resources[i]));
							((UseCaseCommonNode)p).setParent(node);
						}
						
						children.add(p);
					}
				}
			}
		}
		return children.toArray();
    }

    private static final PackageNodeWorkbenchAdapter instance = new PackageNodeWorkbenchAdapter();

}