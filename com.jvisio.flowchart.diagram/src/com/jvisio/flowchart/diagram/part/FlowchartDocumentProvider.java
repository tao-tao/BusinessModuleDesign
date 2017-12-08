package com.jvisio.flowchart.diagram.part;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;



import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.MultiRule;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.DemultiplexingListener;
import org.eclipse.emf.transaction.NotificationFilter;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.workspace.util.WorkspaceSynchronizer;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.gef.editparts.ScalableRootEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.actions.PromptingDeleteFromModelAction;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.render.editparts.RenderedDiagramRootEditPart;
import org.eclipse.gmf.runtime.diagram.ui.render.internal.dialogs.CopyToImageDialog;
import org.eclipse.gmf.runtime.diagram.ui.render.internal.l10n.DiagramUIRenderMessages;
import org.eclipse.gmf.runtime.diagram.ui.render.util.CopyToHTMLImageUtil;
import org.eclipse.gmf.runtime.diagram.ui.render.util.CopyToImageUtil;
import org.eclipse.gmf.runtime.diagram.ui.render.util.DiagramImageUtils;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.DiagramDocument;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.DiagramModificationListener;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.IDiagramDocument;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.IDocument;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.document.FileDiagramDocumentProvider;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.document.FileDiagramModificationListener;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

/**
 * @generated
 */
public class FlowchartDocumentProvider extends FileDiagramDocumentProvider {

	
	private CopyToImageDialog dialog = null;
	
	private GraphicalViewer viewer;
	/**
	 * @generated
	 */
	protected void saveDocumentToFile(IDocument document, IFile file,
			boolean overwrite, IProgressMonitor monitor) throws CoreException {
		IPath path = file.getFullPath();
		String fileName = file.getLocation().removeFileExtension()
				.lastSegment();
		Diagram diagram = (Diagram) document.getContent();
		Resource diagramResource = diagram.eResource();
		IDiagramDocument diagramDocument = (IDiagramDocument) document;
		TransactionalEditingDomain domain = diagramDocument.getEditingDomain();
		List resources = domain.getResourceSet().getResources();

		monitor.beginTask("Saving diagram", resources.size() + 1); //$NON-NLS-1$
		super.saveDocumentToFile(document, file, overwrite,
				new SubProgressMonitor(monitor, 1));
		for (Iterator it = resources.iterator(); it.hasNext();) {
			Resource nextResource = (Resource) it.next();
			monitor.setTaskName("Saving " + nextResource.getURI()); //$NON-NLS-1$
			if (nextResource != diagramResource && nextResource.isLoaded()) {
				try {
					nextResource.save(Collections.EMPTY_MAP);
					doSave(monitor);
				} catch (IOException e) {
					FlowchartDiagramEditorPlugin
							.getInstance()
							.logError(
									"Unable to save resource: " + nextResource.getURI(), e); //$NON-NLS-1$
				}
			}
			//createRunnable(monitor, path, fileName);
			
			monitor.worked(1);
		}
		monitor.done();
	}
	/**
	 * 
	 * 方法名称    :createRunnable
	 * 功能描述    :保存图片到磁盘
	@see
				   :
	@see
	 * 逻辑描述    :
	 * @param   :无
	 * @return  :void
	 * @throws  :无
	 * @since   :Ver 1.00
	 */
	@Deprecated
	private void createRunnable(IProgressMonitor monitor,IPath path,String fileName) {
		IWorkbenchPage workbenchPage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		SaveImageAction action = new SaveImageAction(workbenchPage);
		dialog = new CopyToImageDialog(Display.getCurrent().getActiveShell(),
				path, fileName);
				try {
					List editparts = action.getOperationSetTemp();

					CopyToImageUtil copyToImageUtil = null;
					
					copyToImageUtil = new CopyToImageUtil();
					
					if (editparts.size() == 1
						&& editparts.get(0) instanceof DiagramEditPart) {
						monitor.beginTask("", 6); //$NON-NLS-1$
						monitor.worked(1);
						monitor
							.setTaskName(NLS
								.bind(
									DiagramUIRenderMessages.CopyToImageAction_copyingDiagramToImageFileMessage,
								""));
						copyToImageUtil.copyToImage(
							(DiagramEditPart) editparts.get(0), dialog
								.getDestination(), dialog.getImageFormat(),
							monitor);
					} else {
						DiagramImageUtils
								.zOrderSort(
										editparts,
										LayerManager.Helper
												.find(action.getDiagramEditPart())
												.getLayer(
														LayerConstants.PRINTABLE_LAYERS));
						monitor.beginTask("", 6); //$NON-NLS-1$
						monitor.worked(1);
						monitor
							.setTaskName(NLS
								.bind(
									DiagramUIRenderMessages.CopyToImageAction_copyingSelectedElementsToImageFileMessage,
									dialog.getDestination().toOSString()));
						copyToImageUtil.copyToImage(action.getDiagramEditPart(),
							editparts, dialog.getDestination(), dialog
								.getImageFormat(), monitor);
					}
				} catch (CoreException e) {
					
				} finally {
					monitor.done();
				}
			
		
	}
	public void doSave(IProgressMonitor monitor) {
		try {
			IEditorPart editorPart = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage().getActiveEditor();
			DiagramEditor diagramEditor = (DiagramEditor) editorPart;

			IDiagramGraphicalViewer viewer = diagramEditor
				.getDiagramGraphicalViewer();
			viewer.getRootEditPart();
			//ScalableRootEditPart rootEditPart = (ScalableRootEditPart)getViewer().getRootEditPart();
			RenderedDiagramRootEditPart rootEditPart = (RenderedDiagramRootEditPart)viewer.getRootEditPart();
			double zoom = rootEditPart.getZoomManager().getZoom();
			
			IEditorInput editor =PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
					.getActiveEditor().getEditorInput();
			IFileEditorInput input = (IFileEditorInput) editor;
			IFile umlFile =input.getFile();
			try {
				String file = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage()
						.getActiveEditor().getTitle();
				int index_str = file.lastIndexOf(".");
				file = file.substring(0, index_str);
				IFigure figure = rootEditPart
						.getLayer(LayerConstants.PRINTABLE_LAYERS);

				Rectangle rectangle = figure.getBounds();

				Image image = new Image(Display.getDefault(),
						rectangle.width + 50, rectangle.height + 50);
				GC gc = new GC(image);
				SWTGraphics graphics = new SWTGraphics(gc);
				figure.paint(graphics);

				ImageLoader loader = new ImageLoader();
				loader.data = new ImageData[] { image.getImageData() };
				String umlPath = umlFile.getLocation().toOSString();
				String imageName = file;
				String uml = umlPath
						.substring(0, umlPath.lastIndexOf("\\") + 1)
						+ imageName + ".jpeg";
				loader.save(uml, SWT.IMAGE_JPEG);
				IFolder folder = (IFolder) umlFile.getParent();
				folder.refreshLocal(IResource.FOLDER, new NullProgressMonitor());
				image.dispose();
				gc.dispose();
			} catch(Exception ex){
			//	ex.printStackTrace();
			} finally {
				rootEditPart.getZoomManager().setZoom(zoom);
			}
		
		} catch(Exception ex){
			throw new RuntimeException(ex);
		}
		
	}
	/**
	 * @generated
	 */
	protected ISchedulingRule getSaveRule(Object element) {
		IDiagramDocument diagramDocument = getDiagramDocument(element);
		if (diagramDocument != null) {
			Diagram diagram = diagramDocument.getDiagram();
			if (diagram != null) {
				Collection rules = new ArrayList();
				for (Iterator it = diagramDocument.getEditingDomain()
						.getResourceSet().getResources().iterator(); it
						.hasNext();) {
					IFile nextFile = WorkspaceSynchronizer
							.getFile((Resource) it.next());
					if (nextFile != null) {
						rules.add(computeSaveSchedulingRule(nextFile));
					}
				}
				return new MultiRule((ISchedulingRule[]) rules
						.toArray(new ISchedulingRule[rules.size()]));
			}
		}
		return super.getSaveRule(element);
	}

	/**
	 * @generated
	 */
	protected FileInfo createFileInfo(IDocument document,
			FileSynchronizer synchronizer, IFileEditorInput input) {
		assert document instanceof DiagramDocument;

		DiagramModificationListener diagramListener = new CustomModificationListener(
				this, (DiagramDocument) document, input);
		DiagramFileInfo info = new DiagramFileInfo(document, synchronizer,
				diagramListener);

		diagramListener.startListening();
		return info;
	}

	/**
	 * @generated
	 */
	private ISchedulingRule computeSaveSchedulingRule(IResource toCreateOrModify) {
		if (toCreateOrModify.exists()
				&& toCreateOrModify.isSynchronized(IResource.DEPTH_ZERO))
			return fResourceRuleFactory.modifyRule(toCreateOrModify);

		IResource parent = toCreateOrModify;
		do {
			/*
			 * XXX This is a workaround for https://bugs.eclipse.org/bugs/show_bug.cgi?id=67601
			 * IResourceRuleFactory.createRule should iterate the hierarchy itself.
			 */
			toCreateOrModify = parent;
			parent = toCreateOrModify.getParent();
		} while (parent != null && !parent.exists()
				&& !parent.isSynchronized(IResource.DEPTH_ZERO));

		return fResourceRuleFactory.createRule(toCreateOrModify);
	}

	/**
	 * @generated
	 */
	private class CustomModificationListener extends
			FileDiagramModificationListener {

		/**
		 * @generated
		 */
		private DemultiplexingListener myListener = null;

		/**
		 * @generated
		 */
		public CustomModificationListener(
				FlowchartDocumentProvider documentProviderParameter,
				DiagramDocument documentParameter,
				IFileEditorInput inputParameter) {
			super(documentProviderParameter, documentParameter, inputParameter);
			final DiagramDocument document = documentParameter;
			NotificationFilter diagramResourceModifiedFilter = NotificationFilter
					.createEventTypeFilter(Notification.SET);
			myListener = new DemultiplexingListener(
					diagramResourceModifiedFilter) {
				protected void handleNotification(
						TransactionalEditingDomain domain,
						Notification notification) {
					if (notification.getNotifier() instanceof EObject) {
						Resource modifiedResource = ((EObject) notification
								.getNotifier()).eResource();
						if (modifiedResource != document.getDiagram()
								.eResource()) {
							document.setContent(document.getContent());
						}
					}

				}
			};
		}

		/**
		 * @generated
		 */
		public void startListening() {
			super.startListening();
			getEditingDomain().addResourceSetListener(myListener);
		}

		/**
		 * @generated
		 */
		public void stopListening() {
			getEditingDomain().removeResourceSetListener(myListener);
			super.stopListening();
		}

	}
	
	protected GraphicalViewer getViewer(){
		return viewer;
	}

}
