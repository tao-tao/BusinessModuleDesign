package com.tansun.data.db.visual.action;

import java.util.List;


import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;

import com.tansun.data.db.DBPlugin;
import com.tansun.data.db.visual.model.DommainModel;
import com.tansun.data.db.visual.model.RootModel;

/**
 * Opens the DommainEditDialog.
 * 
 * @author Naoki Takezoe
 */
public class DommainEditAction extends Action {
	
	private GraphicalViewer viewer;
	private DommainModel editDommain;
	
	public DommainEditAction(GraphicalViewer viewer){
		super(DBPlugin.getResourceString("action.editDommain"));
		this.viewer = viewer;
	}

	public DommainEditAction(GraphicalViewer viewer, DommainModel editDommain){
		super(DBPlugin.getResourceString("action.editDommain"));
		this.viewer = viewer;
		this.editDommain = editDommain;
	}
	
	@Override public void run() {
		RootModel root = (RootModel) viewer.getContents().getModel();
		DommainEditDialog dialog = new DommainEditDialog(
				viewer.getControl().getShell(), root, editDommain);
		if(dialog.open() == DommainEditDialog.OK){
			viewer.getEditDomain().getCommandStack().execute(
					new DommainEditCommand(root, dialog.getResult(), root.getDommains()));
		}
	}
	
	private class DommainEditCommand extends Command {
		private RootModel rootModel;
		private List<DommainModel> newDommains;
		private List<DommainModel> oldDommains;
		
		public DommainEditCommand(RootModel rootModel, 
				List<DommainModel> newDommains, List<DommainModel> oldDommains){
			this.rootModel = rootModel;
			this.newDommains = newDommains;
			this.oldDommains = oldDommains;
		}
		
		public void execute() {
			this.rootModel.setDommains(newDommains);
		}

		public void undo() {
			this.rootModel.setDommains(oldDommains);
		}
	}
}
