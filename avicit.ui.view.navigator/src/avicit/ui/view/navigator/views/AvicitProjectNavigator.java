package avicit.ui.view.navigator.views;


import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.navigator.CommonViewer;
import org.eclipse.ui.part.*;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.jface.action.*;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.*;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.SWT;

import avicit.ui.common.util.AdapterUtil;
import avicit.ui.common.util.WorkbenchUtil;
import avicit.ui.runtime.core.IModelLocator;
import avicit.ui.runtime.core.INode;

import com.tansun.runtime.resource.adapter.ExpressionAdapterManager;


/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */

public class AvicitProjectNavigator extends CommonNavigator {


	private static AvicitProjectNavigator instance;
	private AvicitProjectViewer projectViewer;
	private IMemento memento;

	public AvicitProjectNavigator() {
		instance = this;
	}

//	public void createPartControl(Composite aParent) {
//		super.createPartControl(aParent);
//		getCommonViewer().setSorter(new DefaultViewerSorter());
//	}

	protected CommonViewer createCommonViewer(Composite parent) {
		projectViewer = new AvicitProjectViewer(this, getViewSite().getId(), parent, 770);
		initListeners(projectViewer);
		projectViewer.getNavigatorContentService().restoreState(memento);
		getSite().setSelectionProvider(projectViewer);
		configViewer(parent, projectViewer);
		return projectViewer;
	}

	protected void configViewer(Composite parent, AvicitProjectViewer viewer) {
		ResourcesPlugin.getWorkspace().addResourceChangeListener(viewer.getResourceMapper(), IResourceChangeEvent.PRE_CLOSE | IResourceChangeEvent.PRE_DELETE | IResourceChangeEvent.POST_CHANGE);
		// JavaCore.addElementChangedListener(viewer.getResourceMapper());
	}

	 protected void updateExtensions(CommonViewer viewer) {
		viewer.getNavigatorContentService().getActivationService().activateExtensions(new String[] { "com.tansun.ui.navigator.projectContent" //$NON-NLS-1$
				}, true);
	}

	@Override
	public void dispose() {
//		ResourcesPlugin.getWorkspace().removeResourceChangeListener(projectViewer.getResourceMapper());
		super.dispose();
	}

	protected void handleDoubleClick(DoubleClickEvent anEvent) {
		IStructuredSelection selection = (IStructuredSelection) anEvent.getSelection();
		List list = selection.toList();
		if (list.size() == 1)
			handleSingleSelection(list, anEvent);
		else
			handleMultiSelection(list, anEvent);
		super.handleDoubleClick(anEvent);
	}

	private void handleSingleSelection(List list, DoubleClickEvent anEvent) {
		Object object = list.get(0);
		IDoubleClickListener listener = (IDoubleClickListener) ExpressionAdapterManager.getInstance().getAdapter(object, IDoubleClickListener.class);
		if (listener == null)
			handleMultiSelection(list, anEvent);
		else
			listener.doubleClick(anEvent);
	}

	private void handleMultiSelection(List list, DoubleClickEvent anEvent) {
		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			Object object = iterator.next();
			Object adapter = null;
			if (object instanceof IJavaElement)
				try {
					IEditorPart editorPart = JavaUI.openInEditor((IJavaElement) object);
					JavaUI.revealInEditor(editorPart, (IJavaElement) object);
					return;
				} catch (JavaModelException _ex) {
				} catch (PartInitException _ex) {
				}
			if (object instanceof INode) {
				INode model = (INode) object;
				if (model.getResource() != null)
					adapter = model.getResource().getAdapter(IFile.class);
			} else {
				adapter = AdapterUtil.getAdapter(object, IFile.class);
			}
			if (!(adapter instanceof IFile))
				continue;
			IFile file = (IFile) adapter;
			IEditorPart editorPart = WorkbenchUtil.openFile(file);
			if (editorPart == null)
				continue;
			if (!(object instanceof INode))
				continue;
			IModelLocator locator = getModelLocator(editorPart);
			if (locator == null)
				continue;
			locator.locate(null, (INode) object);
			break;
		}
	}

	private IModelLocator getModelLocator(IEditorPart editorPart) {
		Object locatorAdapter = editorPart.getAdapter(IModelLocator.class);
		if (locatorAdapter instanceof IModelLocator)
			return (IModelLocator) locatorAdapter;
		if (editorPart instanceof IModelLocator)
			return (IModelLocator) editorPart;
		else
			return null;
	}

	public static AvicitProjectNavigator getViewInstance() {
		return instance;
	}

	public static AvicitProjectViewer getViewer() {
		return instance.projectViewer;
	}

	@Override
	public void selectReveal(ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			if (structuredSelection.size() == 1) {
				Object object = structuredSelection.getFirstElement();
				if (object instanceof INode) {
					super.selectReveal(structuredSelection);
					return;
				}
			}
		}
		super.selectReveal(selection);
	}

	
	public void createPartControl(Composite aParent) {
		super.createPartControl(aParent);
		getCommonViewer().setSorter(new DefaultViewerSorter());
	}
	

}