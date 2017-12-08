package com.tansun.data.db.visual.editpart;


import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import com.tansun.data.db.visual.model.AnchorModel;
import com.tansun.data.db.visual.model.ForeignKeyModel;
import com.tansun.data.db.visual.model.NoteModel;
import com.tansun.data.db.visual.model.RootModel;
import com.tansun.data.db.visual.model.TableModel;
import com.tansun.data.newpackage.ViewEditPart;
import com.tansun.data.newpackage.ViewModel;

public class DBEditPartFactory implements EditPartFactory {

	public EditPart createEditPart(EditPart context, Object model) {
		EditPart part = null;
		
		if(model instanceof RootModel){
			part = new RootEditPart();
		} else if(model instanceof ViewModel){
			part = new ViewEditPart();
		} else if(model instanceof NoteModel){
			part = new NoteEditPart();
		} else if(model instanceof ForeignKeyModel){
			part = new ForeignKeyEditPart();
		} else if(model instanceof AnchorModel){
			part = new AnchorEditPart();
		}else if(model instanceof TableModel){
			part = new TableEditPart();
		}
		
		if(part!=null){
			part.setModel(model);
		}
		
		return part;
	}

}
