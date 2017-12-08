package avicit.ui.view.navigator;


import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import avicit.ui.view.exception.ExceptionUtil;

public class ImageRepository
{

    private static void loadImageRepositories()
    {
        IExtensionRegistry t_ExtensionRegistry = Platform.getExtensionRegistry();
        IExtensionPoint t_ExtensionPoint = t_ExtensionRegistry.getExtensionPoint("avicit.view.ui.imageRepository");
        if(t_ExtensionPoint != null)
        {
            IExtension t_Extensions[] = t_ExtensionPoint.getExtensions();
            for(int i = 0; i < t_Extensions.length; i++)
            {
                IExtension t_Extension = t_Extensions[i];
                IConfigurationElement t_ConfigurationElements[] = t_Extension.getConfigurationElements();
                for(int j = 0; j < t_ConfigurationElements.length; j++)
                {
                    IConfigurationElement t_ConfigurationElement = t_ConfigurationElements[j];
                    doLadImageRepository(t_ConfigurationElement);
                }

            }

        }
    }

    private static void doLadImageRepository(IConfigurationElement r_ConfigurationElement)
    {
        String t_Name = r_ConfigurationElement.getAttribute("name");
        String t_Image = r_ConfigurationElement.getAttribute("image");
        String t_BundleName = r_ConfigurationElement.getAttribute("bundleName");
        String t_PluginName = r_ConfigurationElement.getContributor().getName();
       if(StringUtils.isEmpty(t_BundleName) || "null".equals(t_BundleName))
            t_BundleName = t_PluginName;
        ImageDescriptor t_ImageDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin(t_PluginName, t_Image);
        if(t_ImageDescriptor != null)
        {
            ImageRepository t_Repository = getImageRepository(t_BundleName);
            t_Repository.setImageDescriptor(t_Name, t_ImageDescriptor);
        }
    }

    private ImageRepository()
    { }

    public static synchronized ImageRepository getImageRepository(String r_BundleName)
    {
        Object t_Object = repositories.get(r_BundleName);
        ImageRepository t_Repository;
        if(t_Object == null)
        {
            t_Repository = new ImageRepository();
            t_Repository.bundleName = r_BundleName;
            repositories.put(r_BundleName, t_Repository);
        } else
        {
            t_Repository = (ImageRepository)t_Object;
        }
        return t_Repository;
    }

    public static synchronized ImageRepository removeImageRepository(String r_BundleName)
    {
        return (ImageRepository)repositories.remove(r_BundleName);
    }

    public PooledImageDescriptor getImageDescriptor(String r_ImageName)
    {
        ImageDescriptor t_ImageDescriptor;
        t_ImageDescriptor = (ImageDescriptor)images.get(r_ImageName);
        if(t_ImageDescriptor == null)
        {
	        t_ImageDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin(bundleName, r_ImageName);
	        if(ImageDescriptor.getMissingImageDescriptor().equals(t_ImageDescriptor))
	            return null;
	        try
	        {
	            t_ImageDescriptor = PooledImageDescriptor.getImageDescriptor(t_ImageDescriptor);
	            images.put(r_ImageName, t_ImageDescriptor);
	        }
	        catch(Exception _ex)
	        {
	            return null;
	        }
        }
        return (PooledImageDescriptor)t_ImageDescriptor;
    }

    public void setImageDescriptor(String r_ImageName, ImageDescriptor r_ImageDescriptor)
    {
        if(r_ImageDescriptor != null && r_ImageName != null)
            if(r_ImageDescriptor instanceof PooledImageDescriptor)
                images.put(r_ImageName, r_ImageDescriptor);
            else
                images.put(r_ImageName, PooledImageDescriptor.getImageDescriptor(r_ImageDescriptor));
    }

    public String getBundleName()
    {
        return bundleName;
    }

    public static PooledImageDescriptor getImageDescriptor(String r_BundleName, String r_ImageName)
    {
        ImageRepository t_ImageRepository = getImageRepository(r_BundleName);
        return t_ImageRepository.getImageDescriptor(r_ImageName);
    }

    public static Image getImage(String r_BundleName, String r_ImageName)
    {
        ImageRepository t_ImageRepository = getImageRepository(r_BundleName);
        ImageDescriptor t_ImageDescriptor = t_ImageRepository.getImageDescriptor(r_ImageName);
        return t_ImageDescriptor.createImage();
    }

    public static final String EXTENSION_ID = "com.tansun.ui.imageRepository";
    private final Map images = new HashMap();
    private String bundleName;
    private static final Map repositories = new HashMap();

    static 
    {
        try
        {
            loadImageRepositories();
        }
        catch(Exception e)
        {
            ExceptionUtil.getInstance().logException(e);
        }
    }
}