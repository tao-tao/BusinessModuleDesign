package avicit.ui.view.navigator;


import java.util.Hashtable;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;

public class PooledImageDescriptor extends ImageDescriptor
{

    public static PooledImageDescriptor getImageDescriptor(ImageDescriptor r_ImageDescriptor)
    {
        if(r_ImageDescriptor == null)
            return null;
        ImageDescriptor t_RealImageDescriptor;
        for(t_RealImageDescriptor = r_ImageDescriptor; t_RealImageDescriptor instanceof PooledImageDescriptor; t_RealImageDescriptor = ((PooledImageDescriptor)t_RealImageDescriptor).imageDescriptor);
        Object t_Object = descriptorPools.get(t_RealImageDescriptor);
        if(t_Object != null)
        {
            return (PooledImageDescriptor)t_Object;
        } else
        {
            PooledImageDescriptor t_ImageDescriptor = new PooledImageDescriptor(t_RealImageDescriptor);
            descriptorPools.put(t_RealImageDescriptor, t_ImageDescriptor);
            return t_ImageDescriptor;
        }
    }

    @Override
    public Image createImage(boolean r_ReturnMissingImageOnError, Device r_Device)
    {
		Image image = super.createImage(r_ReturnMissingImageOnError, r_Device);
        return image;
    }

    @Override
    public ImageData getImageData()
    {
        return imageDescriptor.getImageData();
    }

    public int hashCode()
    {
        int result = 1;
        result = 31 * result + (imageDescriptor != null ? imageDescriptor.hashCode() : 0);
        return result;
    }

    public boolean equals(Object obj)
    {
        if(this == obj)
            return true;
        if(obj == null)
            return false;
        if(getClass() != obj.getClass())
        {
            return false;
        } else
        {
            PooledImageDescriptor other = (PooledImageDescriptor)obj;
            return other.imageDescriptor.equals(imageDescriptor);
        }
    }

  private PooledImageDescriptor(ImageDescriptor r_ImageDescriptor)
  {
      imageDescriptor = r_ImageDescriptor;
      Assert.isNotNull(r_ImageDescriptor, SwtMessages.IMAGE_DESCRIPTOR_CAN_NOT_BE_NULL);
  }

    private ImageDescriptor imageDescriptor;
    private static final Hashtable descriptorPools = new Hashtable();

}