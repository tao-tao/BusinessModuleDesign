package net.java.amateras.uml.usecasediagram.action;

import java.util.ArrayList;
import java.util.List;

import net.java.amateras.uml.UMLPlugin;
import net.java.amateras.uml.usecasediagram.edit.UsecaseEditPart;
import net.java.amateras.uml.usecasediagram.model.UsecaseModel;

import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

//import com.tansun.form.designer.editors.FormEditor;

/**
 * The abstract class for copy actions in the diagram.
 *
 * @author Naoki Takezoe
 * @since 1.2.3
 */
public  class OpenSourceAction extends SelectionAction {

	//private AbstractPasteAction pasteAction;

	private List<Class<?>> allowModelTypes = new ArrayList<Class<?>>();

	public OpenSourceAction(IWorkbenchPart part) {
		super(part);
		setId("opens");
		setActionDefinitionId("opens");
		setText("��");
		ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
		//setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_COPY));
		//setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_COPY_DISABLED));

		
	}

	protected void registerAllowType(Class<?> type) {
		allowModelTypes.add(type);
	}

	public void run() {
		Object obj =((StructuredSelection) getSelection()).getFirstElement();
		if (obj instanceof UsecaseEditPart){
			UsecaseEditPart edit = (UsecaseEditPart) obj;
			UsecaseModel model = (UsecaseModel)edit.getModel();
			if (model.getResource() != null && model.getFileResource().exists()) {
				IWorkbenchWindow window = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow();
				if (window != null) {
					IWorkbenchPage activePage = window.getActivePage();
					
					try {
						if(model.getFileResource().getProjectRelativePath().toString().endsWith(".jsp")){
//							IDE.openEditor(activePage, model.getFileResource(),FormEditor.class.getName());
						}else{
							IDE.openEditor(activePage, model.getFileResource());
						}
						
					
					} catch (PartInitException e) {
						MessageDialog.openError(
								window.getShell(),
								UMLPlugin.getDefault().getResourceString(
										"open.resource.title"),
								UMLPlugin.getDefault().getResourceString(
										"open.resource.message"));
					}
				}
			}
		}else{
			return;
		}
			return;
		
		
		
	}

	protected boolean calculateEnabled() {
		List<Object> selected = getSelectedObjects();
		if (selected.isEmpty()) {
			return true;
		}
		if(selected.size()>1){
			
			return true;
		}
		if (!(selected.get(0) instanceof UsecaseEditPart)) {
			return false;
		}else{
			return true;
		}
		
		
	}

	private boolean isAllowType(Class<?> type) {
		return true;
	}

}
