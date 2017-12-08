package avicit.ui.platform.common.ui;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Yangzhenhua
 */

public class ProjectXMLFileSelectionDialog extends CommonResourceDialog {

	public ProjectXMLFileSelectionDialog(Shell parentShell, IProject project, String[] exts, int style, boolean includeFolder) {
		super(parentShell, project, style, includeFolder);
		init(exts);
	}

	private void init(String[] exts) {
		setResourceDescription("ѡ���ļ�");
		setSuffixs(exts);
	}

	public IFile getResultFile() {
		if(getResult() != null && getResult().length>0)
		{
			IFile selectedFile = (IFile) getResult()[0];
			return selectedFile;
		}
		return null;
	}
}
