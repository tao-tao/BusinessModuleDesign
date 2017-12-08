package com.tansun.runtime.resource.adapter;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.ui.model.WorkbenchAdapter;

import avicit.ui.view.navigator.ImageRepository;

public abstract class AbstractWorkbenchAdapter extends WorkbenchAdapter
{

    protected AbstractWorkbenchAdapter()
    {
    }

    public ImageDescriptor getImageDescriptor(Object object)
    {
        String name = object.getClass().getName();
        ImageRepository imageRepository = ImageRepository.getImageRepository("avicit.ui.view.navigator");
        return decorateImage(imageRepository.getImageDescriptor(name), object);
    }

    protected ImageDescriptor decorateImage(ImageDescriptor input, Object element)
    {
    	IResource resource = null;
    	if(element instanceof IAdaptable){
    		resource = (IResource) ((IAdaptable)element).getAdapter(IResource.class);
    	}
        if(resource != null){
            try {
                int findMaxProblemSeverity = resource.findMaxProblemSeverity(IMarker.PROBLEM, true, IResource.DEPTH_INFINITE);
                if(findMaxProblemSeverity == IMarker.SEVERITY_ERROR){
                	ImageRepository imageRepository = ImageRepository.getImageRepository("avicit.ui.view.navigator");
                    ImageDescriptor overlayImage = imageRepository.getImageDescriptor(("error"));//AbstractUIPlugin.imageDescriptorFromPlugin("com.tansun.ui.navigator", "icons/decorator/error.gif");//
//                    Image i = overlayImage.createImage();
                    DecorationOverlayIcon newIcon = new DecorationOverlayIcon(input.createImage(), overlayImage, IDecoration.BOTTOM_LEFT);
                    newIcon.createImage();
                    return newIcon;
                }
            } catch (CoreException e) {
                e.printStackTrace();
            }
        }
        return input;
    }

	public boolean hasChildren(Object element) {
		return true;
	}


}
