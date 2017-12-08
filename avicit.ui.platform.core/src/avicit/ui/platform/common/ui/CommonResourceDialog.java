package avicit.ui.platform.common.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.model.WorkbenchLabelProvider;

import avicit.ui.platform.common.ui.TreeViewerSelectionDialog;

/**
 * This dialog shows IFile type resources within a IProject domain for
 * selection. The client can prvide the suffixs of files to filter when
 * candidates are shown on the tree.
 * 
 * The usage: Shell shell = new Shell(); IProject project = getProject();
 * CommonResourceDialog dlg = new CommonResourceDialog(shell, project);
 * dlg.setResourceDescription("image"); dlg.setSuffixs(new
 * String[]{"bmp","jpg","gif"}); if(dlg.open() == Window.OK) { IFile
 * selectedFile = (IFile)dlg.getResult()[0]; }
 * 
 * Note: In code above, what you get is an absolute resource path. You can use
 * <code>org.eclipse.wst.sse.core.util.PathHelper.convertToRelative(String input, String base)</code>
 * to convert a absolute resource path to a relative path based on one path.
 * 
 * @author Yangzhenhua
 */
public class CommonResourceDialog extends TreeViewerSelectionDialog {

	// private static final String STATUS_MESSAGE_0 = CommonPlugin
	// .getResourceString("Dialog.CommonResourceDialog.StatusMessage0");
	// //$NON-NLS-1$

	private IProject _project = null;

	private String _suffixs[] = null;

	private CommonResourceFilter _filter = null;

	// The resource type resourceDescription, such as "image", "jsp", "java
	// class" etc.
	private String _resourceDescription = null;

	private IFolder _folder;

	// The content provider
	class ProjectFileDialogContentProvider implements ITreeContentProvider {
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
			} else if (element instanceof IContainer) {
				IContainer container = (IContainer) element;
				if (container.isAccessible()) {
					try {
						return container.members();
					} catch (CoreException e) {
						System.out.println("Error.ProjectFileDialogContentProvider.0"); //$NON-NLS-1$
					}
				}

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

	// The default resource filter
	class CommonResourceFilter extends ViewerFilter {
		private List _suffixs = null;
		private boolean _includeFolder = false;

		/**
		 * @return Returns the _suffixs.
		 */
		public List getSuffixs() {
			return _suffixs;
		}

		/**
		 * @param _suffixs
		 *            The _suffixs to set.
		 */
		public void setSuffixs(String[] _suffixs) {
			this._suffixs = Arrays.asList(_suffixs);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers.Viewer,
		 *      java.lang.Object, java.lang.Object)
		 */
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			if (element instanceof IFile) {
				IFile file = (IFile) element;
				if (isSuffixBlank()) {
					return true;
				}
				String name = file.getName();
				int i = name.indexOf(".");
				if(i >0)
					name = name.substring(i+1);
				if (name != null) {
					if (_suffixs.contains(name)) {
						return true;
					}
				}
			} else if (element instanceof IContainer) {
				if (!((IContainer) element).isAccessible()) {
					return false;
				}
				if (element instanceof IProject) {
					return true;
				} else if (element instanceof IFolder) {
					IContainer container = (IContainer) element;
					try {
						IResource[] members = container.members();
						for (int i = 0; i < members.length; i++) {
							if (select(viewer, members[i].getParent(), members[i])) {
								return true;
							}
						}
					} catch (CoreException e) {
						System.out.println("Error.ProjectFileDialogContentProvider.0"); //$NON-NLS-1$
						return false;
					}
					if (_includeFolder)
						return true;
				}
			}
			return false;
		}

		public boolean isIncludeFolder() {
			return _includeFolder;
		}

		public void setIncludeFolder(boolean folder) {
			_includeFolder = folder;
		}

	}

	public CommonResourceDialog(Shell parentShell, IProject project) {
		this(parentShell, project, SWT.NONE, false);
	}

	public CommonResourceDialog(Shell parentShell, IFolder folder, int style) {
		this(parentShell, (folder == null ? null : folder.getProject()), style, false);
		_folder = folder;
	}
	
	public CommonResourceDialog(Shell parentShell, IProject project, int style)
	{
		this(parentShell,project,style,false);
	}
	
	/**
	 * This is a dialog for common resource selection, the resouce supported
	 * include IFolder, IProject, IFile, user can provide
	 * 
	 * @param parentShell
	 * @param project
	 */
	public CommonResourceDialog(Shell parentShell, IProject project, int style, boolean includeFolder) {
		super(parentShell, "ѡ����Դ", style);
		if (project == null) {
			throw new IllegalArgumentException(
					"Argument(project) cannot be null");
		}
		_project = project;
		setContentProvider(new ProjectFileDialogContentProvider());
		setLabelProvider(WorkbenchLabelProvider
				.getDecoratingWorkbenchLabelProvider());
		_filter = new CommonResourceFilter();
		_filter.setIncludeFolder(includeFolder);
		setFilter(_filter);
		_project = project;
		setStatusMessage(getStatusMessage());
	}

	public CommonResourceDialog(Shell parentShell, IProject project, boolean includeFolder) {
		this(parentShell, project, SWT.NONE, includeFolder);
	}

	/**
	 * This is a dialog for common resource selection, the resouce supported
	 * include IFolder, IProject, IFile, user can provide
	 * 
	 * @param parentShell
	 * @param folder
	 * @param style
	 */
	public CommonResourceDialog(Shell parentShell, IFolder folder, int style, boolean includeFolder) {
		this(parentShell, (folder == null ? null : folder.getProject()), style, includeFolder);

		_folder = folder;

	}

	protected String getStatusMessage() {
		if (_resourceDescription == null) {
			return "";
		}
		return _resourceDescription;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jst.pagedesigner.ui.common.SelectionTreeViewerDialog#findInputElement()
	 */
	protected Object findInputElement() {
		if (_folder != null) {
			return new Object[] { _folder, };
		}
		return new Object[] { _project, };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jst.pagedesigner.ui.common.SelectionTreeViewerDialog#isValidSelection(java.lang.Object)
	 */
	protected boolean isValidSelection(Object selection) {
		if (selection instanceof Object[]) {
			for (int i = 0, n = ((Object[]) selection).length; i < n; i++) {
				if (isValidElement(((Object[]) selection)[i]) == true) {
					return true;
				}
			}
			return false;
		}
		return isValidElement(selection);
	}

	private boolean isValidElement(Object selection) {
		if ((selection instanceof IFile)) {
			// Null means no filter is set
			if (isSuffixBlank()) {
				return true;
			}
			// The extension is supported?
//			else if (_suffixs != null && Arrays.asList(_suffixs).contains(((IFile) selection).getFileExtension().toLowerCase())) {
				return true;
//			}
		} 
//		else if (selection instanceof IFolder)
//			return true;
		// None of above conditions, invalid.
		return false;
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
			if (objects[i] instanceof IResource) {
				list.add(objects[i]);
			}
		}
		return list.toArray();
	}

	/**
	 * @param suffixs
	 *            The suffixs to set.
	 */
	public void setSuffixs(String[] suffixs) {
		this._suffixs = convertTolowercase(suffixs);
		_filter.setSuffixs(_suffixs);
		setStatusMessage(getStatusMessage());
	}

	private String[] convertTolowercase(String[] suffixs) {
		if (suffixs != null) {
			String[] newSuffixs = new String[suffixs.length];
			for (int i = 0; i < suffixs.length; i++) {
				newSuffixs[i] = suffixs[i].toLowerCase();
			}
			return newSuffixs;
		}
		return null;
	}

	public void setFilter(String[] suffixs, String sourceDescription) {
		setSuffixs(suffixs);
		setResourceDescription(sourceDescription);
	}

	/**
	 * @return Returns the sourceDescription.
	 */
	public String getResourceDescription() {
		return _resourceDescription;
	}

	/**
	 * @param sourceDescription
	 *            The sourceDescription to set.
	 */
	public void setResourceDescription(String sourceDescription) {
		this._resourceDescription = sourceDescription;
		setStatusMessage(getStatusMessage());
	}

	private boolean isSuffixBlank() {
		boolean isSuffixBlank = false;
		if (_suffixs == null) {
			isSuffixBlank = true;
		} else {
			int count = 0;
			for (int i = 0, size = _suffixs.length; i < size; i++) {
				if (_suffixs[i] != null && !"".equals(_suffixs[i])) {
					count++;
					break;
				}
			}
			if (count == 0) {
				isSuffixBlank = true;
			}
		}
		return isSuffixBlank;
	}
}
