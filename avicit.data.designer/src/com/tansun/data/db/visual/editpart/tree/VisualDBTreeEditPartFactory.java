package com.tansun.data.db.visual.editpart.tree;


import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import com.tansun.data.db.visual.editpart.tree.FolderTreeEditPart.FolderModel;
import com.tansun.data.db.visual.model.ColumnModel;
import com.tansun.data.db.visual.model.DommainModel;
import com.tansun.data.db.visual.model.IndexModel;
import com.tansun.data.db.visual.model.RootModel;
import com.tansun.data.db.visual.model.TableModel;

public class VisualDBTreeEditPartFactory implements EditPartFactory {

	public EditPart createEditPart(EditPart context, Object model) {
        EditPart part = null;
        if(model instanceof RootModel){
        	part = new RootTreeEditPart();
        } else if(model instanceof TableModel){
			part = new TableTreeEditPart();
		} else if(model instanceof ColumnModel){
			part = new ColumnTreeEditPart();
		} else if(model instanceof FolderModel){
			part = new FolderTreeEditPart();
		} else if(model instanceof DommainModel){
			part = new DommainTreeEditPart();
		} else if(model instanceof IndexModel){
			part = new IndexTreeEditPart();
		}
		if(part != null){
			part.setModel(model);
		}
		return part;
	}

}
