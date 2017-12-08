package com.tansun.runtime.resource.adapter;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;

import avicit.ui.core.runtime.resource.EclipseResourceDelegate;
import avicit.ui.core.runtime.resource.IResourceDelegate;
import avicit.ui.runtime.core.INode;
import avicit.ui.runtime.core.IResourceNode;

public class JavaElementAdapterFactory implements IAdapterFactory {

	public JavaElementAdapterFactory() {
	}

	public Object getAdapter(Object adaptableObject, Class adapterType) {
		IResourceDelegate resourceDelegate = null;
		if (adaptableObject instanceof INode) {
			INode element = (INode) adaptableObject;
			resourceDelegate = element.getResource();
		}
		if (adaptableObject instanceof IResourceNode) {
			IResourceNode element = (IResourceNode) adaptableObject;
			resourceDelegate = element.getResource();
		}
		if (resourceDelegate instanceof EclipseResourceDelegate) {
			EclipseResourceDelegate eclipseResource = (EclipseResourceDelegate) resourceDelegate;
			org.eclipse.core.resources.IResource resource = eclipseResource.getResource();
			return JavaCore.create(resource);
		} else {
			return null;
		}
	}

	public Class[] getAdapterList() {
		return CLASSES;
	}

	private static final Class CLASSES[] = { IJavaElement.class };

}
