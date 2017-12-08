package com.tansun.runtime.resource.adapter;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.packageview.ClassPathContainer;

import avicit.ui.common.util.CollectionUtil;
import avicit.ui.runtime.core.node.JavaLibrariesNode;
import avicit.ui.runtime.core.node.NavigatorMessages;

public class JavaLibrariesWorkbenchAdapter extends AbstractWorkbenchAdapter
{

    private JavaLibrariesWorkbenchAdapter()
    {
    }

    public Object[] getChildren(Object object)
    {
        JavaLibrariesNode node = (JavaLibrariesNode)object;
        IProject project = (IProject)node.getProject().getAdapter(IResource.class);
        IJavaProject javaProject = JavaCore.create(project);
        List list = new ArrayList();
		try {
			IClasspathEntry entries[] = javaProject.getRawClasspath();
	        for(int i = 0; i < entries.length; i++)
	        {
	            IClasspathEntry entry = entries[i];
	            if(entry.getEntryKind() == 5)
	            {
	                ClassPathContainer container = new ClassPathContainer(javaProject, entry);
	                list.add(container);
	            }
	            if(entry.getEntryKind() == 1)
	            {
	                org.eclipse.jdt.core.IPackageFragmentRoot roots[] = javaProject.findPackageFragmentRoots(entry);
	                CollectionUtil.addAllQuietly(list, roots);
	            }
	            if(entry.getEntryKind() == 4)
	            {
	                org.eclipse.jdt.core.IPackageFragmentRoot roots[] = javaProject.findPackageFragmentRoots(entry);
	                CollectionUtil.addAllQuietly(list, roots);
	            }
	        }
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return list.toArray();
    }

    public static JavaLibrariesWorkbenchAdapter getInstance()
    {
        return instance;
    }

    @Override
    public String getLabel(Object object)
    {
        return NavigatorMessages.JAVA_LIBRARY;
    }

    private static final JavaLibrariesWorkbenchAdapter instance = new JavaLibrariesWorkbenchAdapter();

}