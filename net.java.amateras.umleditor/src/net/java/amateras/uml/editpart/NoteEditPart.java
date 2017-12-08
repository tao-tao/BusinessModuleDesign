/**
 * 
 */
package net.java.amateras.uml.editpart;

import net.java.amateras.uml.figure.NoteFigure;
import net.java.amateras.uml.model.NoteModel;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Text;

/**
 * @author Takahiro Shida.
 * 
 */
public class NoteEditPart extends AbstractUMLEntityEditPart {

	private DirectEditManager directManager;

	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE,
				new NoteDirectEditPolicy());
	}

	/*
	 * (�� Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure() {
		NoteFigure figure = new NoteFigure();
		return figure;
	}

	protected void refreshVisuals() {
		super.refreshVisuals();
		((NoteFigure) figure).setText(((NoteModel) getModel()).getContent());
	}

	public void performRequest(Request req) {
		if (req.getType().equals(RequestConstants.REQ_DIRECT_EDIT)
				|| req.getType().equals(RequestConstants.REQ_OPEN)) {
			performDirectEdit();
			return;
		}
		super.performRequest(req);
	}

	private void performDirectEdit() {
		if (directManager == null) {
			directManager = new NoteDirectEditManager();
		}
		directManager.show();
	}

	/**
	 * DirectEditManager
	 */
	private class NoteDirectEditManager extends DirectEditManager {

		public NoteDirectEditManager() {
			super(NoteEditPart.this, MultiLineCellEditor.class,
					new NoteCellEditorLocator());
		}

		protected void initCellEditor() {
			getCellEditor().setValue(((NoteModel) getModel()).getContent());
			Text text = (Text) getCellEditor().getControl();
			text.selectAll();
		}
	}

	/**
	 * CellEditorLocator
	 */
	private class NoteCellEditorLocator implements CellEditorLocator {
		public void relocate(CellEditor celleditor) {
			IFigure figure =  getFigure();
			Text text = (Text) celleditor.getControl();
			Rectangle rect = figure.getBounds().getCopy();
			figure.translateToAbsolute(rect);
			text.setBounds(rect.x + 5, rect.y + 5, rect.width - 5,
					rect.height - 5);
		}
	}

	/**
	 * DirectEditCommand
	 */
	private class DirectEditCommand extends Command {

		private String oldName;

		private String newName;

		public void execute() {
			NoteModel model = (NoteModel) getModel();
			oldName = model.getContent();
			model.setContent(newName);
		}

		public void setName(String name) {
			newName = name;
		}

		public void undo() {
			NoteModel model = (NoteModel) getModel();
			model.setContent(oldName);
		}
	}

	/**
	 * DirectEditPolicy
	 */
	private class NoteDirectEditPolicy extends DirectEditPolicy {

		protected Command getDirectEditCommand(DirectEditRequest request) {
			DirectEditCommand command = new DirectEditCommand();
			command.setName((String) request.getCellEditor().getValue());
			return command;
		}

		protected void showCurrentEditValue(DirectEditRequest request) {
		}
	}
}
