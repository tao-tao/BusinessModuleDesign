package com.tansun.runtime.resource.adapter;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdapterFactory;

import avicit.ui.runtime.core.node.AbstractNode;

public class NodeAdapterFactory implements IAdapterFactory {

	public NodeAdapterFactory() {
	}

	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adaptableObject instanceof AbstractNode) {
			return ((AbstractNode) adaptableObject).getAdapter(adapterType);
		}
		return null;
	}

	public Class[] getAdapterList() {
		return CLASSES;
	}

	private static final Class CLASSES[] = { IFile.class, IContainer.class, IFolder.class, IProject.class };

}