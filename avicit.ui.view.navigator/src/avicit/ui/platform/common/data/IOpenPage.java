package avicit.ui.platform.common.data;


/**
 * Editors should implement this if they can handle reseting to a page based on
 * the class defined for that page.
 * 
 * @author yangzhenhua
 */
public interface IOpenPage {
	public void setActiveEditorPage(String name, String pageID, String fileID, String type) throws Exception;
//	public Object getSystemBean(String beanName) throws TansunException;
}