package com.tansun.data.db.visual.action;

import java.util.ArrayList;
import java.util.List;


import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.actions.Clipboard;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

import com.tansun.data.db.DBPlugin;
import com.tansun.data.db.visual.editor.VisualDBEditor;
import com.tansun.data.db.visual.model.ICloneableModel;

/**
 * Copy selected entity models in the diagram editor.
 *
 * @author Naoki Takezoe
 * @since 1.0.4
 */
public class CopyAction extends SelectionAction {

	private PasteAction pasteAction;

	public CopyAction(VisualDBEditor editor, PasteAction pasteAction) {
		super(editor);

		setId(ActionFactory.COPY.getId());
		//setActionDefinitionId(ActionFactory.COPY.getId());
		setText(DBPlugin.getResourceString("action.copy"));
		ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
		setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_COPY));
		setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_COPY_DISABLED));

		this.pasteAction = pasteAction;
	}

	@SuppressWarnings("unchecked")
	public void run() {
		List<EditPart> selection = getSelectedObjects();
		List<ICloneableModel> copied = new ArrayList<ICloneableModel>();
		for (int i = 0; i < selection.size(); i++) {
			EditPart editPart = (EditPart) selection.get(i);
			ICloneableModel cloneable = (ICloneableModel) editPart.getModel();
			copied.add(cloneable.clone());
		}
		Clipboard.getDefault().setContents(copied);

		pasteAction.update();
	}

	@SuppressWarnings("unchecked")
	protected boolean calculateEnabled() {
		List<Object> selected = getSelectedObjects();
		if (selected.isEmpty()) {
			return true;
		}
		for (int i = 0; i < selected.size(); i++) {
			if (!(selected.get(i) instanceof EditPart)) {
				return false;
			}
			EditPart editPart = (EditPart) selected.get(i);
			if (ICloneableModel.class.isAssignableFrom(editPart.getModel().getClass())) {
				return true;
			}
		}
		return false;
	}

}
