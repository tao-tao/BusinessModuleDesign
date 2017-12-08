package avicit.ui.runtime.resource;

import avicit.ui.runtime.resource.inf.IResourceDelegate;

public class ResourceHelper {

    public static boolean isValidResource(IResourceDelegate resourceDelegate)
    {
        return resourceDelegate != null && resourceDelegate.exists();
    }

}
