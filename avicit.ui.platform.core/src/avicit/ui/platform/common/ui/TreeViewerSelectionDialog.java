package avicit.ui.platform.common.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.SelectionDialog;
import org.eclipse.ui.part.DrillDownComposite;

/**
 * This is a base dialog that uses TreeViewer to show selections, subclass needs
 * to provide IContentProvider, ILabelProvider and ViewerFilter for the
 * TreeViewer. Subclass needs to implement isValidSelection(), which valids the
 * selection, and findInputElement() which provides the root element of the
 * tree. Besides, subclass might need to implement getResult() to return a
 * customized result.
 * 
 * @author Yangzhenhua
 * 
 */
public abstract class TreeViewerSelectionDialog extends SelectionDialog {
	private static final String DEFAULT_TITLE = "Dialog.TreeViewerSelectionDialog.DefaultTitle"; //$NON-NLS-1$

	/** Used to tag the image type */
	public static final int STYLE_NONE = 0;

	public static final int STYLE_INFORMATION = 1;

	public static final int STYLE_ERROR = 2;

	public static final int STYLE_WARNING = 3;

	/** Sizi of the TreeViewer composite */
	private static final int SIZING_SELECTION_PANE_HEIGHT = 300;

	private static final int SIZING_SELECTION_PANE_WIDTH = 320;

	private String _title = DEFAULT_TITLE;

	// the seleciton on the treeviewer.
	private static Object[] _selection;

	// providers
	private ITreeContentProvider _contentProvider;

	private ILabelProvider _labelProvider;

	private ViewerFilter _filter;

	/** The validation image */
	private Label _statusImage;

	/** The validation message */
	private Label _statusLabel;

	private String _statusMessage;

	/** The selection tree */
	protected TreeViewer _treeViewer;

	private int _style;

	private ViewerSorter _viewerSorter = null;

	protected IDoubleClickListener _doubleClickListener;

	/**
	 * @param parentShell
	 */
	public TreeViewerSelectionDialog(Shell parentShell, String statusMessage,
			int style) {
		super(parentShell);
		_statusMessage = statusMessage;
		_style = style;
		setShellStyle(SWT.CLOSE | SWT.TITLE | SWT.BORDER
				| SWT.APPLICATION_MODAL | SWT.RESIZE);
	}

	public TreeViewerSelectionDialog(Shell parentShell, String statusMessage) {
		this(parentShell, statusMessage, SWT.NONE);
	}

	public void setTitle(String title) {
		super.setTitle(title);
		_title = title;
	}

	/**
	 * Returns a new drill down viewer for this dialog.
	 * 
	 * @param parent
	 */
	protected void createTreeViewer(Composite parent) {
		// Create drill down
		DrillDownComposite drillDown = new DrillDownComposite(parent,
				SWT.BORDER);
		GridData spec = new GridData(GridData.FILL_BOTH);
		spec.widthHint = SIZING_SELECTION_PANE_WIDTH;
		spec.heightHint = SIZING_SELECTION_PANE_HEIGHT;
		drillDown.setLayoutData(spec);
		_treeViewer = new TreeViewer(drillDown, _style);
		drillDown.setChildTree(_treeViewer);
	}

	private void setTreeViewerProviders() {
		_treeViewer.setContentProvider(_contentProvider);
		_treeViewer.setLabelProvider(_labelProvider);
		if (_viewerSorter == null) {
			_viewerSorter = new ViewerSorter();
		}
		_treeViewer.setSorter(_viewerSorter);
		_treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
					public void selectionChanged(SelectionChangedEvent event) {
						_selection = getSelectedElements((IStructuredSelection) event
								.getSelection());
						updateStatus();
					}
				});
		
		
		_doubleClickListener=new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				ISelection selection = event.getSelection();
				if (selection instanceof IStructuredSelection) {
					Object item = ((IStructuredSelection) selection)
							.getFirstElement();
					if (item instanceof IFile) {
						okPressed();
					} else if (_treeViewer.getExpandedState(item)) {
						_treeViewer.collapseToLevel(item, 1);
					} else {
						_treeViewer.expandToLevel(item, 1);
					}
				}
			}
		};
		_treeViewer.addDoubleClickListener(_doubleClickListener);
		_treeViewer.setInput(findInputElement());

		if (_filter != null) {
			_treeViewer.addFilter(_filter);
		}
	}

	/**
	 * Creates the contents of the composite.
	 */
	public void createTreeViewerComposite(Composite parent) {
		Composite treeViewerComposite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		treeViewerComposite.setLayout(layout);

		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 2;
		treeViewerComposite.setLayoutData(gridData);
		Label label = new Label(treeViewerComposite, SWT.WRAP);
		label.setText(_title);
		label.setFont(treeViewerComposite.getFont());
		createTreeViewer(treeViewerComposite);
		Dialog.applyDialogFont(treeViewerComposite);
	}

	/**
	 * Sets the selected existing container.
	 * 
	 * @param selection
	 */
	public void setSelectedElement(Object[] selection) {
		// Expand to and select the specified container
		if (selection == null) {
			return;
		}
		this._selection = selection;
		for (int i = 0; i < _selection.length; i++) {
			if (_selection[i] != null) {
				_treeViewer.expandToLevel(_selection[i], 1);

			}
		}
		_treeViewer.setSelection(new StructuredSelection(_selection), true);
	}

	/**
	 * Sets the selected existing container.
	 * 
	 * @param selection
	 */
	public void setSelection(Object[] selection) {
		this._selection = selection;
	}

	/*
	 * (non-Javadoc) Method declared on Dialog.
	 */
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		area.setLayout(gridLayout);

		// Container treeviewer composite
		createTreeViewerComposite(area);

		_statusImage = createLabel(area);
		_statusImage.setImage(getMessageImage(STYLE_ERROR));
		_statusLabel = createLabel(area);
		// Link to model
		setTreeViewerProviders();

		return dialogArea;
	}

	private Label createLabel(Composite parent) {
		Label label = new Label(parent, SWT.LEFT);
		GridData data = new GridData();
		data.horizontalSpan = 1;
		data.horizontalAlignment = GridData.FILL;
		label.setLayoutData(data);
		label.setText(_statusMessage == null ? "" : _statusMessage); //$NON-NLS-1$
		return label;
	}

	private Object[] getSelectedElements(IStructuredSelection selection) {
		return selection.toArray();
	}

	/**
	 * @param provider
	 *            The _contentProvider to set.
	 */
	public void setContentProvider(ITreeContentProvider provider) {
		_contentProvider = provider;
	}

	/**
	 * @param provider
	 *            The _labelProvider to set.
	 */
	public void setLabelProvider(ILabelProvider provider) {
		_labelProvider = provider;
	}

	/**
	 * @param filter
	 *            The _filter to set.
	 */
	public void setFilter(ViewerFilter filter) {
		this._filter = filter;
	}

	/**
	 * @param sorter
	 *            The _viewerSorter to set.
	 */
	public void setViewerSorter(ViewerSorter sorter) {
		_viewerSorter = sorter;
	}

	public void setStatusMessage(String message) {
		_statusMessage = message;
	}

	/**
	 * Update the status message
	 */
	private void updateStatus() {
		Object selection = _selection;
		if (_selection != null && _selection.length == 1) {
			selection = _selection[0];
		}
		if (isValidSelection(selection)) {
			_statusImage.setVisible(false);
			_statusLabel.setText(getStatusMessage()); //$NON-NLS-1$
			if (getOkButton() != null)
				getOkButton().setEnabled(true);
		} else {
			_statusImage.setVisible(true);
			_statusImage.setImage(getMessageImage(STYLE_ERROR));
			_statusImage.redraw();
			_statusLabel.setText(_statusMessage);
			getOkButton().setEnabled(false);
		}

	}

	protected String getStatusMessage() {
		return "";
	}

	/**
	 * Get the different message according the message type.
	 * 
	 * @return Image - the message image
	 */
	protected Image getMessageImage(int imageType) {
		switch (imageType) {
		case STYLE_ERROR:
			return JFaceResources.getImage(Dialog.DLG_IMG_MESSAGE_ERROR);
		case STYLE_WARNING:
			return JFaceResources.getImage(Dialog.DLG_IMG_MESSAGE_WARNING);
		case STYLE_INFORMATION:
			return JFaceResources.getImage(Dialog.DLG_IMG_MESSAGE_INFO);
		default:
			return null;
		}
	}

	/**
	 * The <code>ContainerSelectionDialog</code> implementation of this
	 * <code>Dialog</code> method builds a list of the selected resource
	 * containers for later retrieval by the client and closes this dialog.
	 */
	protected void okPressed() {
		List chosenContainerPathList = new ArrayList();
		if (_selection != null) {
			chosenContainerPathList.addAll(Arrays.asList(_selection));
		}
		setResult(chosenContainerPathList);
		super.okPressed();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.window.Window#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createContents(Composite parent) {
		Control control = super.createContents(parent);
		if (_selection != null) {
			this.setSelectedElement(_selection);
		}
		return control;
	}

	protected abstract boolean isValidSelection(Object selection);

	protected abstract Object findInputElement();

	public TreeViewer getTreeViewer() {
		return _treeViewer;
	}

	public void setTreeViewer(TreeViewer viewer) {
		_treeViewer = viewer;
	}
}
