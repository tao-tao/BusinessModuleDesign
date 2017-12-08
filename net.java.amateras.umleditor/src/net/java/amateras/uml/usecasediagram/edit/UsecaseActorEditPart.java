/**
 * 
 */
package net.java.amateras.uml.usecasediagram.edit;

import java.beans.PropertyChangeEvent;

import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import net.java.amateras.uml.editpart.NamedEntityEditPart;
import net.java.amateras.uml.figure.EntityFigure;
import net.java.amateras.uml.usecasediagram.edit.UsecaseEditPart.SetValueCommand;
import net.java.amateras.uml.usecasediagram.figure.UsecaseActorFigure;
import net.java.amateras.uml.usecasediagram.model.UsecaseActorModel;
import net.java.amateras.uml.usecasediagram.model.UsecaseModel;
import net.java.amateras.uml.usecasediagram.property.UseCaseAcDialog;
import net.java.amateras.uml.usecasediagram.property.UseCaseDialog;

/**
 * @author Takahiro Shida.
 * 
 */
public class UsecaseActorEditPart extends NamedEntityEditPart {

	protected EntityFigure createEntityFigure() {
		UsecaseActorModel model = (UsecaseActorModel) getModel();
		return new UsecaseActorFigure(model);
	}

	public void propertyChange(PropertyChangeEvent evt) {
		super.propertyChange(evt);
		if (evt.getPropertyName().equals(UsecaseActorModel.P_IMAGE)) {
			refleshImage();
		}
		if (evt.getPropertyName().equals(UsecaseActorModel.P_DESC)) {

			refleshDesc((String) evt.getNewValue());
		}
	}

	private void refleshImage() {
		UsecaseActorModel model = (UsecaseActorModel) getModel();
		UsecaseActorFigure figure = (UsecaseActorFigure) getFigure();
		figure.setImage(model);
	}

	private void refleshDesc(String str) {
		// UsecaseActorModel model = (UsecaseActorModel) getModel();
		// model.setDesc(str);
	}

	//
	public void performRequest(Request request) {
		if (request.getType() == RequestConstants.REQ_OPEN) {
			onOpenRequest(this.getModel(), this.getViewer().getControl()
					.getShell());
		} else {
			super.performRequest(request);
		}
	}

	public void onOpenRequest(Object model, Shell shell) {

		

		UseCaseAcDialog dialog = new UseCaseAcDialog(shell,
				(UsecaseActorModel) model);
		if (dialog.open() == Dialog.OK) {
			UsecaseActorModel newmodel = (UsecaseActorModel) model;
			newmodel = (UsecaseActorModel) newmodel.clone();
			this.getViewer()
					.getEditDomain()
					.getCommandStack()
					.execute(
							new SetValueCommand(newmodel,
									(UsecaseActorModel) model));
		}

	}

	class SetValueCommand extends Command {
		UsecaseActorModel newmodel;
		UsecaseActorModel model;

		public SetValueCommand(UsecaseActorModel newmodel,
				UsecaseActorModel model) {
			this.newmodel = newmodel;
			this.model = model;
		}

		@Override
		public void execute() {
			model.setName(newmodel.getName());
		}

	}

}
