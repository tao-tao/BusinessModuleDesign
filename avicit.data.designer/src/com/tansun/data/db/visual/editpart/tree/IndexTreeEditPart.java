package com.tansun.data.db.visual.editpart.tree;

import com.tansun.data.db.DBPlugin;
import com.tansun.data.db.visual.model.IndexModel;

public class IndexTreeEditPart extends AbstractDBTreeEditPart {
	
	protected void refreshVisuals() {
		IndexModel model = (IndexModel) getModel();
		setWidgetText(model.toString());
		setWidgetImage(DBPlugin.getImage(DBPlugin.ICON_INDEX));
	}
	
}
