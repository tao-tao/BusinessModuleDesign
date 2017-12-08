package com.tansun.data.db.visual.editpart.tree;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;


import org.eclipse.gef.GraphicalViewer;

import com.tansun.data.db.DBPlugin;
import com.tansun.data.db.util.UIUtils;
import com.tansun.data.db.visual.action.DommainEditAction;
import com.tansun.data.db.visual.editor.VisualDBOutlinePage;
import com.tansun.data.db.visual.editpart.tree.FolderTreeEditPart.FolderModel;
import com.tansun.data.db.visual.model.RootModel;
import com.tansun.data.db.visual.model.TableModel;

public class RootTreeEditPart extends AbstractDBTreeEditPart {

	protected List<FolderModel> getModelChildren() {
		List<FolderModel> children = new ArrayList<FolderModel>();
		children.add(new FolderModel(DBPlugin.getResourceString("label.table"), (RootModel) getModel()){
			@Override public List<?> getChildren() {
				String filterText = VisualDBOutlinePage.getFilterText();
				if(filterText.length()==0){
					return root.getTables();
				}
				// filtering
				List<TableModel> result = new ArrayList<TableModel>();
				for(TableModel table: root.getTables()){
					if(table.getTableName().startsWith(filterText)){
						result.add(table);
					}
				}
				return result;
			}
			@Override public void doEdit() {
				// Nothing to do
			}
		});
		if(VisualDBOutlinePage.getFilterText().length() == 0){
			children.add(new FolderModel(DBPlugin.getResourceString("label.dommain"), (RootModel) getModel()){
				@Override public List<?> getChildren() {
					return root.getDommains();
				}
				@Override public void doEdit() {
					new DommainEditAction((GraphicalViewer)
							UIUtils.getActiveEditor().getAdapter(GraphicalViewer.class)).run();
				}
			});
		}
		return children;
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
        String propName = evt.getPropertyName();
		if(RootModel.P_CHILDREN.equals(propName) || RootModel.P_DOMMAINS.equals(propName)) {
			refreshChildren();
		}
	}
	
}
