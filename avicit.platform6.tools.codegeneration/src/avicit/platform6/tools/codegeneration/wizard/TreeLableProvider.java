package avicit.platform6.tools.codegeneration.wizard;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

public class TreeLableProvider  implements ILabelProvider {

	
	  /**
     * 显示什么图片
     * @param 结点
     * @return 可以为null值
     */
    public Image getImage(Object element) {
        ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
        
        return sharedImages.getImage(ISharedImages.IMG_ETOOL_HOME_NAV);
       // return ((TreeDataConfigObject) element).getImage();
    }

    /**
     * 显示什么文字
     * @param 结点
     * @return 不能为null值 
     */
    public String getText(Object element) {
        return ((TreeDataConfigObject) element).getName();

    }

    public void addListener(ILabelProviderListener listener) {}

    public void dispose() {}

    public boolean isLabelProperty(Object element, String property) {
        return false;
    }

    public void removeListener(ILabelProviderListener listener) {}
}


