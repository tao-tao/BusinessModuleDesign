package com.tansun.data.db.visual.editpart.tree;

import com.tansun.data.db.DBPlugin;
import com.tansun.data.db.visual.model.DommainModel;

public class DommainTreeEditPart extends AbstractDBTreeEditPart {

	protected void refreshVisuals() {
		DommainModel model = (DommainModel) getModel();
		
		StringBuilder sb = new StringBuilder();
		sb.append(model.getName()).append(" - ");
		sb.append(model.getType().getName());
		if(model.getType().supportSize()){
			sb.append("(").append(model.getSize()).append(")");
		}
		
		setWidgetText(sb.toString());
		setWidgetImage(DBPlugin.getImage(DBPlugin.ICON_DOMMAIN));
	}
	
	
}
