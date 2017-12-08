package avicit.ui.runtime.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public final class UrlUtil
{

    private UrlUtil()
    {
    }

    public static URL getURL(String resourceLocation)
        throws RuntimeException
    {
        return getURL(resourceLocation, null);
    }

    public static URL getURL(String resourceLocation, ClassLoader loader)
        throws RuntimeException
    {
        if(resourceLocation.startsWith("file:"))
            resourceLocation = resourceLocation.substring("file:".length());
        File file = new File(resourceLocation);
        if(!file.exists())
        {
			try {
				return new URL((new StringBuilder()).append("file:").append(resourceLocation).toString());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
        }
        URL url = null;
        if(loader != null)
            url = loader.getResource(resourceLocation);
        if(url == null)
        {
            url = Thread.currentThread().getContextClassLoader().getResource(resourceLocation);
            if(url == null)
            {
                url = UrlUtil.class.getClassLoader().getResource(resourceLocation);
                if(url == null)
                    throw new RuntimeException((new StringBuilder()).append("Class path resource [").append(resourceLocation).append("] cannot be resolved to URL because it does not exist.").toString());
            }
        }
        return url;
    }

    public static final String FILE_URL_PREFIX = "file:";
}