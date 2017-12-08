package avicit.ui.view.navigator.views;


import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jdt.internal.ui.navigator.JavaNavigatorLabelProvider;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.model.IWorkbenchAdapter;
import org.eclipse.ui.model.IWorkbenchAdapter2;
import org.eclipse.ui.navigator.ICommonContentExtensionSite;

import avicit.ui.common.util.AdapterUtil;
import avicit.ui.runtime.core.INode;

import com.tansun.runtime.resource.adapter.ExpressionAdapterManager;



public class NavigatorLabelProvider extends JavaNavigatorLabelProvider
/*	implements IColorProvider, IFontProvider, ICommonLabelProvider*/{

    public NavigatorLabelProvider()
    {
    	super();
    }

    @Override
	public StyledString getStyledText(Object element) {
    	String label = null;
        IWorkbenchAdapter adapter = getAdapter(element);
        if(adapter != null)
             label = adapter.getLabel(element);
        StyledString str = null;
        if(label != null)
        	str = new StyledString(label);
    	if(str == null)
    		str = super.getStyledText(element);
		 return str;
	}

	@Override
    public String getText(Object element)
    {
        IWorkbenchAdapter adapter = getAdapter(element);
        if(adapter != null)
            return adapter.getLabel(element);
        return super.getText(element);
    }

    public Image getImage(Object element)
    {
    	Image image = super.getImage(element);
    	if(image != null)
    		return image;
        return doGetImage(element);
    }

    @Override
    public String getDescription(Object element)
    {/*
        if(element instanceof INamingElement)
            return ((INamingElement)element).getDisplayName();
        if(element instanceof IResource)
            return ((IResource)element).getFullPath().makeRelative().toString();
        else
            return super.getDescription(element);
    */
    	return "avicit";}

    @Override
    public void init(ICommonContentExtensionSite icommoncontentextensionsite)
    {
    	super.init(icommoncontentextensionsite);
    	super.setIsFlatLayout(true);
    }

    @Override
    public Color getForeground(Object element)
    {
    	Color color = this.getForeground(element);
    	if(color != null)
    		return color;
        return getColor(element, true);
    }

    @Override
    public Color getBackground(Object element)
    {
    	Color color = this.getBackground(element);
    	if(color != null)
    		return color;
        return getColor(element, false);
    }

    private ImageDescriptor decorateImage(ImageDescriptor input, Object element)
    {
    	IResource resource = null;
    	if(element instanceof IAdaptable){
    		resource = (IResource) ((IAdaptable)element).getAdapter(IResource.class);
    	}
        if(resource != null && resource.exists()){
            try {
                int findMaxProblemSeverity = resource.findMaxProblemSeverity(IMarker.PROBLEM, true, IResource.DEPTH_INFINITE);
                if(findMaxProblemSeverity == IMarker.SEVERITY_ERROR){
                	//ImageRepository imageRepository = ImageRepository.getImageRepository("com.tansun.ui.navigator");
                    //ImageDescriptor overlayImage = imageRepository.getImageDescriptor(("error"));//AbstractUIPlugin.imageDescriptorFromPlugin("com.tansun.ui.navigator", "icons/decorator/error.gif");//
//                    Image i = overlayImage.createImage();
                    DecorationOverlayIcon newIcon = new DecorationOverlayIcon(input.createImage(), null, IDecoration.BOTTOM_LEFT);
                    newIcon.createImage();
                    return newIcon;
                }
            } catch (CoreException e) {
                e.printStackTrace();
            }
        }
        return input;
    }

    private Image doGetImage(Object element)
    {
        IWorkbenchAdapter adapter = getAdapter(element);
        if(adapter == null)
            return null;
        else
        {
        	ImageDescriptor desc = adapter.getImageDescriptor(element);
        	if(element instanceof INode)
        		desc = decorateImage(desc, element);
            if(desc != null)
            	return desc.createImage();
        }
        return null;
    }

    private Color getColor(Object element, boolean forground)
    {
        IWorkbenchAdapter2 adapter = getAdapter2(element);
        if(adapter == null)
            return null;
        org.eclipse.swt.graphics.RGB descriptor = forground ? adapter.getForeground(element) : adapter.getBackground(element);
        if(descriptor == null)
            return null;
        Color color = new Color(Display.getCurrent(), descriptor);
        return color;
    }

    private Font getFont(Object element)
    {
        IWorkbenchAdapter2 adapter = getAdapter2(element);
        if(adapter == null)
            return null;
        org.eclipse.swt.graphics.FontData descriptor = adapter.getFont(element);
        if(descriptor == null)
            return null;
        Font font = new Font(Display.getCurrent(), descriptor);
        return font;
    }

    protected IWorkbenchAdapter getAdapter(Object element)
    {
       IWorkbenchAdapter adapter = (IWorkbenchAdapter)ExpressionAdapterManager.getInstance().getAdapter(element, IWorkbenchAdapter.class);
        if(adapter == null)
            adapter = (IWorkbenchAdapter)AdapterUtil.getAdapter(element, IWorkbenchAdapter.class);
        if(adapter == null)
            //return InternalWorkbenchAdapter.getInstance();
        	return null;
        else
            return adapter;
    }

    protected final IWorkbenchAdapter2 getAdapter2(Object o)
    {
    	
    	return null;
    	/*
        IWorkbenchAdapter2 adapter = (IWorkbenchAdapter2)AdapterUtil.getAdapter(o, IWorkbenchAdapter2.class);
        if(adapter == null)
            return InternalWorkbenchAdapter.getInstance();
        else
            return adapter;
    */}

}
