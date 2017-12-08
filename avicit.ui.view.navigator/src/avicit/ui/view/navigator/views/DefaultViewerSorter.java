package avicit.ui.view.navigator.views;

import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.ui.JavaElementSorter;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

import avicit.ui.core.runtime.resource.EclipseResourceManager;
import avicit.ui.runtime.core.IOrderable;


public class DefaultViewerSorter extends ViewerSorter
{

    public DefaultViewerSorter()
    {
        javaSorter = new JavaElementSorter();
    }

    public int compare(Viewer viewer, Object object1, Object object2)
    {
        if(object1 instanceof IOrderable)
        {
            if(object2 instanceof IOrderable)
            {
                IOrderable node1 = (IOrderable)object1;
                IOrderable node2 = (IOrderable)object2;
                if(node1.getOrder() != node2.getOrder())
                    return node1.getOrder() - node2.getOrder();
            } else
            {
                return -1;
            }
        } else
        if(object2 instanceof IOrderable)
            return ((IOrderable)object2).getOrder();
        IResource resource1 = EclipseResourceManager.getResource(object1);
        if(resource1 == null)
            return javaSorter.compare(viewer, object1, object2);
        IResource resource2 = EclipseResourceManager.getResource(object2);
        if(resource2 == null)
            return javaSorter.compare(viewer, object1, object2);
        if(resource1.getType() != resource2.getType())
            return resource2.getType() - resource1.getType();
        else
            return javaSorter.compare(viewer, object1, object2);
    }

    private JavaElementSorter javaSorter;
}