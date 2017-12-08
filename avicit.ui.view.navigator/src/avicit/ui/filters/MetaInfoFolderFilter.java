/*jadclipse*/// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.

package avicit.ui.filters;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.*;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
/**
 * 
 * @author lidong
 *
 */
public class MetaInfoFolderFilter extends ViewerFilter
{

    public MetaInfoFolderFilter()
    {
    }

    public boolean select(Viewer viewer, Object parent, Object element)
    {
    	/****todo****/
    	
        IFolder folder;
        IProject proj;
        if(element instanceof IFolder){
        	 folder = (IFolder)element;
        	 return false;
        }
        return true;
    }
}


