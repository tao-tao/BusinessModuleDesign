/**
 * 
 */
package net.java.amateras.uml.usecasediagram.edit;

import java.beans.PropertyChangeEvent;

import net.java.amateras.uml.UMLPlugin;
import net.java.amateras.uml.editpart.NamedEntityEditPart;
import net.java.amateras.uml.figure.EntityFigure;
import net.java.amateras.uml.usecasediagram.figure.UsecaseFigure;
import net.java.amateras.uml.usecasediagram.figure.UsecaseFigureFactory;
import net.java.amateras.uml.usecasediagram.model.UsecaseModel;
import net.java.amateras.uml.usecasediagram.property.UseCaseDialog;

import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

/**
 * @author shida
 * 
 */
public class UsecaseEditPart extends NamedEntityEditPart {

	protected EntityFigure createEntityFigure() {
		return UsecaseFigureFactory.getUsecaseFigure((UsecaseModel) getModel());
	}

	public void propertyChange(PropertyChangeEvent evt) {
		super.propertyChange(evt);
		if (evt.getPropertyName().equals(UsecaseModel.P_RESOURCE)) {
			UsecaseModel model = (UsecaseModel) getModel();
			UsecaseFigure figure = (UsecaseFigure) getFigure();
			figure.setLink(model.getResource() != null&&model.getResource().trim().length()>0
					&& model.getFileResource().exists());
		} else if (evt.getPropertyName().equals(UsecaseModel.P_INPUT)) {
			UsecaseModel model = (UsecaseModel) getModel();
			UsecaseFigure figure = (UsecaseFigure) getFigure();
			this.refreshVisuals();
		}
	}

	public void performRequest(Request request) {
		if (request.getType() == RequestConstants.REQ_OPEN) {
			onOpenRequest(this.getModel(), this.getViewer().getControl()
					.getShell());
		} else {
			super.performRequest(request);
		}
	}

	public void onOpenRequest(Object model, Shell shell) {

		UseCaseDialog dialog = new UseCaseDialog(shell, (UsecaseModel) model);
		if (dialog.open() == Dialog.OK) {
			UsecaseModel newmodel = (UsecaseModel) model;
			newmodel = (UsecaseModel) newmodel.clone();
			this.getViewer()
					.getEditDomain()
					.getCommandStack()
					.execute(
							new SetValueCommand(newmodel, (UsecaseModel) model));
		}

	}

	class SetValueCommand extends Command {
		UsecaseModel newmodel;
		UsecaseModel model;

		public SetValueCommand(UsecaseModel newmodel, UsecaseModel model) {
			this.newmodel = newmodel;
			this.model = model;
		}

		@Override
		public void execute() {
			// model.setInput(newmodel.getInput());
			// model.setName(newmodel.getName());
			// model.setDeDeal(newmodel)
		}

	}

	private boolean openEditor() {
		UsecaseModel model = (UsecaseModel) getModel();
		if (model.getResource() != null && model.getFileResource().exists()) {
			IWorkbenchWindow window = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow();
			if (window != null) {
				IWorkbenchPage activePage = window.getActivePage();
				try {
					IDE.openEditor(activePage, model.getFileResource());
					return true;
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
		return false;
	}
}
