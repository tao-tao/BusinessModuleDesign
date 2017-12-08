package avicit.ui.platform.common.ui;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Yangzhenhua
 */

public class WebPageSelectionDialog extends CommonResourceDialog {

	public static final String[] WebPageSuffixs = new String[] { "jsp", "jspx", "jsf", "html", "htm" };
	public static final String[] XmlSuffixs = new String[] { "xml" };

	public WebPageSelectionDialog(Shell parentShell, IProject project, int style) {
		super(parentShell, project, style);
		init();
	}

	public WebPageSelectionDialog(Shell parentShell, IProject project) {
		super(parentShell, project);
		init();
	}

	public WebPageSelectionDialog(Shell parentShell, IFolder folder, int style) {
		super(parentShell, folder, style);
		init();
	}

	private void init() {
		//xyz update
		setResourceDescription("��ѡ����ȷ����Դ����");
		setSuffixs(WebPageSuffixs);
	}

	public IFile getResultFile() {
		IFile selectedFile = (IFile) getResult()[0];
		return selectedFile;
	}
}
