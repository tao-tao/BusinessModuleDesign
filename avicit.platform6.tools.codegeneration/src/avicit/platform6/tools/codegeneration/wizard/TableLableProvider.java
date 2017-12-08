package avicit.platform6.tools.codegeneration.wizard;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

public class TableLableProvider  implements ILabelProvider {

	CodeGenerationWizard wizard;
	
	private boolean isMain;

	
	
	/**
	 * 是否为主子表模版
	 */
	private boolean isTree;

	
	public TableLableProvider(CodeGenerationWizard wizard){
		this.wizard = wizard;
	}
	  /**
     * 显示什么图片
     * @param 结点
     * @return 可以为null值
     */
    public Image getImage(Object element) {
        ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
        return sharedImages.getImage(ISharedImages.IMG_OBJ_ELEMENT);
       // return ((TreeDataConfigObject) element).getImage();
    }

    /**
     * 显示什么文字
     * @param 结点
     * @return 不能为null值 
     */
    public String getText(Object element) {
        return ((TableDataConfigObject) element).getName();

    }

    public void addListener(ILabelProviderListener listener) {}

    public void dispose() {}

    public boolean isLabelProperty(Object element, String property) {
        return false;
    }

    public void removeListener(ILabelProviderListener listener) {}
}


