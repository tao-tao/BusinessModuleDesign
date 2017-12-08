package avicit.ui.core.runtime.resource;



public class ResourceHelper {

    public static boolean isValidResource(IResourceDelegate resourceDelegate)
    {
        return resourceDelegate != null && resourceDelegate.exists();
    }

}
