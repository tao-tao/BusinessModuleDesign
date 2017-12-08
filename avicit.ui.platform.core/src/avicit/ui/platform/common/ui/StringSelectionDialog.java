package avicit.ui.platform.common.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Yangzhenhua
 */
public class StringSelectionDialog extends TableSelectionDialog {

	private IProject _project = null;

	private Object input = null;

	// The content provider
	class StringContentProvider implements ITreeContentProvider {
		/**
		 * The visual part that is using this content provider is about to be
		 * disposed. Deallocate all allocated SWT resources.
		 */
		public void dispose() {
			// nothing to dispose
		}

		/**
		 * @see ITreeContentProvider#getChildren
		 */
		public Object[] getChildren(Object element) {
			if (element instanceof Object[]) {
				return (Object[]) element;
			}
			return new Object[0];
		}

		/**
		 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(Object)
		 */
		public Object[] getElements(Object element) {
			return getChildren(element);
		}

		/**
		 * @see ITreeContentProvider#getParent
		 */
		public Object getParent(Object element) {
			if (element instanceof IResource) {
				return ((IResource) element).getParent();
			}
			return null;
		}

		/**
		 * @see ITreeContentProvider#hasChildren
		 */
		public boolean hasChildren(Object element) {
			return getChildren(element).length > 0;
		}

		/**
		 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(Viewer,
		 *      Object, Object)
		 */
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// no viewer change support required
		}

	}

	class StringLabelProvider implements ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			
			return element.toString();
		}

		public void addListener(ILabelProviderListener listener) {
		}

		public void dispose() {
		}

		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		public void removeListener(ILabelProviderListener listener) {
		}

	}
	
	/**
	 * This is a dialog for common resource selection, the resouce supported
	 * include IFolder, IProject, IFile, user can provide
	 * 
	 * @param parentShell
	 * @param project
	 */
	public StringSelectionDialog(Shell parentShell, String title, Object[] inputs, int style) {
		super(parentShell, title, style);
		this.setTitle(title);
		_contentProvider = new StringContentProvider();
		_labelProvider = new StringLabelProvider();
		this.input = inputs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jst.pagedesigner.ui.common.SelectionTreeViewerDialog#findInputElement()
	 */
	protected Object findInputElement() {
		return this.input;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.dialogs.SelectionDialog#getResult()
	 */
	public Object[] getResult() {
		Object[] objects = super.getResult();
		if (objects == null || objects.length == 0) {
			return null;
		}
		List list = new ArrayList();
		for (int i = 0; i < objects.length; i++) {
			if (objects[i] instanceof String) {
				list.add(objects[i]);
			}
		}
		return list.toArray();
	}

	@Override
	protected boolean isValidSelection(Object selection) {
		return true;
	}
}
