package avicit.ui.runtime.resource;

import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;

import avicit.ui.runtime.util.IMessageCaller;

public class EclipseMarkerMessageCaller
    implements IMessageCaller
{

    public EclipseMarkerMessageCaller(IResource r_Resource)
    {
        Assert.isNotNull(r_Resource, "the resource can't be null,please check it.");
        resource = r_Resource;
    }

    public void clear()
    {
        try
        {
            resource.deleteMarkers("org.eclipse.core.resources.problemmarker", false, 0);
            resource.deleteMarkers("org.eclipse.core.resources.textmarker", false, 0);
        }
        catch(CoreException _ex) { }
    }

    public void error(String r_Message, Properties r_Properties)
    {
        try
        {
            IMarker t_Marker = resource.createMarker("org.eclipse.core.resources.problemmarker");
            setAttributes(r_Message, r_Properties, t_Marker);
            t_Marker.setAttribute("location", resource.getFullPath().toOSString());
            t_Marker.setAttribute("priority", 2);
            t_Marker.setAttribute("severity", 2);
        }
        catch(CoreException _ex) { }
    }

    private void setAttributes(String r_Message, Properties r_Properties, IMarker t_Marker)
        throws CoreException
    {
        t_Marker.setAttribute("message", r_Message);
        if(r_Properties != null)
        {
            for(Iterator t_Iterator = r_Properties.keySet().iterator(); t_Iterator.hasNext();)
            {
                String t_Key = (String)t_Iterator.next();
                Object t_Value = r_Properties.get(t_Key);
                if(t_Value != null && ((t_Value instanceof String) || NumberUtils.isNumber(t_Value.toString())))
                    t_Marker.setAttribute(t_Key, t_Value);
            }

        }
    }

    public boolean hasError()
    {
		try {
	        IMarker t_Markers[] = resource.findMarkers("org.eclipse.core.resources.problemmarker", false, 2);
	        if(ArrayUtils.isEmpty(t_Markers))
	            return false;
	        for(int i = 0;i<t_Markers.length;i++)
	        {
		        IMarker t_Marker = t_Markers[i];
		        if(t_Marker.getAttribute("severity", 0) == 2)
		            return true;
	        }
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return false;
    }

    public void info(String r_Message, Properties r_Properties)
    {
        try
        {
            IMarker t_Marker = resource.createMarker("org.eclipse.core.resources.textmarker");
            setAttributes(r_Message, r_Properties, t_Marker);
            t_Marker.setAttribute("priority", 1);
            t_Marker.setAttribute("severity", 0);
        }
        catch(CoreException _ex) { }
    }

    public void warn(String r_Message, Properties r_Properties)
    {
        try
        {
            IMarker t_Marker = resource.createMarker("org.eclipse.core.resources.problemmarker");
            setAttributes(r_Message, r_Properties, t_Marker);
            t_Marker.setAttribute("priority", 1);
            t_Marker.setAttribute("severity", 1);
        }
        catch(CoreException _ex) { }
    }

    private IResource resource;
}