package avicit.ui.platform.common.ui;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.ui.IJavaElementSearchConstants;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.SelectionDialog;

import avicit.ui.platform.common.util.ProjectFinder;

public class ChooseClassDialog {
	/**
	 * Logger for this class
	 */

	private Shell parent;
	private String typeName;
	private String message;
	private String title;

	public ChooseClassDialog(Shell parent, String typeName, String title, String message) {
		this.parent = parent;
		this.typeName = typeName;
		this.title = title;
		this.message = message;
	}

	public String openDialog() {
		Object result = null;
		try {
			SelectionDialog dialog = JavaUI.createTypeDialog(parent, new ProgressMonitorDialog(parent),
			/*
			 * (IJavaSearchScope)new JavaSearchScope(JavaSearchScope.SOURCES)
			 */
			SearchEngine.createJavaSearchScope(new IJavaElement[] { ProjectFinder.getCurrentJavaProject() }, IJavaSearchScope.SOURCES | IJavaSearchScope.APPLICATION_LIBRARIES),
					IJavaElementSearchConstants.CONSIDER_ALL_TYPES, false);
			dialog.setTitle(title);
			dialog.setMessage(message);
			if (dialog.open() != IDialogConstants.CANCEL_ID) {
				Object[] types = dialog.getResult();
				if (types != null && types.length != 0) {
					result = types[0];
				}
			}
		} catch (JavaModelException e) {
			throw new RuntimeException(e);
		}
		return result == null ? null : ((IType) result).getFullyQualifiedName();

	}

}
