package avicit.ui.common.util;


import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;

public class AdapterUtil {

    public AdapterUtil()
    {
    }

    public static Object getAdapter(Object r_Element, Class r_AdapterType)
    {
        if(r_Element == null)
            return null;
        Object adapter = Platform.getAdapterManager().getAdapter(r_Element, r_AdapterType);
        if(adapter != null)
        	return adapter;
        if(r_Element instanceof IAdaptable)
            return ((IAdaptable)r_Element).getAdapter(r_AdapterType);
        return null;
    }

}
