package com.tansun.data.db.visual.action;

import java.util.List;


import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.dialogs.MessageDialog;

import com.tansun.data.db.DBPlugin;
import com.tansun.data.db.util.NameConverter;
import com.tansun.data.db.util.UIUtils;
import com.tansun.data.db.visual.editor.VisualDBEditor;
import com.tansun.data.db.visual.editpart.TableEditPart;
import com.tansun.data.db.visual.model.ColumnModel;
import com.tansun.data.db.visual.model.TableModel;

public class Logical2PhysicalAction extends SelectionAction {

	public Logical2PhysicalAction(VisualDBEditor editor) {
		super(editor);

		setId(Logical2PhysicalAction.class.getName());
		//setActionDefinitionId(Logical2PhysicalAction.class.getName());
		setText(DBPlugin.getResourceString("action.logical2physical"));
	}

	@Override
	@SuppressWarnings("unchecked")
	public void run() {
		if(!MessageDialog.openConfirm(getWorkbenchPart().getSite().getShell(),
				DBPlugin.getResourceString("dialog.confirm.title"),
				DBPlugin.getResourceString("action.logical2physical.confirm"))){
			return;
		}

		List<EditPart> selection = getSelectedObjects();

		for (int i = 0; i < selection.size(); i++) {
			EditPart editPart = (EditPart) selection.get(i);
			if(editPart instanceof TableEditPart){
				TableModel table = (TableModel) editPart.getModel();

				for(ColumnModel column: table.getColumns()){
					String logical = column.getLogicalName();
					if(logical != null && logical.length() != 0){
						column.setColumnName(NameConverter.logical2physical(logical));
					}
				}

				String logical = table.getTableName();
				if(logical != null && logical.length() != 0){
					table.setTableName(NameConverter.logical2physical(logical));
				}
			}
		}

		UIUtils.getActiveEditor().doSave(new NullProgressMonitor());
	}


	@Override
	@SuppressWarnings("unchecked")
	protected boolean calculateEnabled() {
		List<Object> selection = getSelectedObjects();
		for (int i = 0; i < selection.size(); i++) {
			Object obj = selection.get(i);
			if(obj instanceof TableEditPart){
				return true;
			}
		}
		return false;
	}
}
