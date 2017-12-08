package com.tansun.data.db.visual.editor;


import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;

import com.tansun.data.db.DBPlugin;
import com.tansun.data.db.util.UIUtils;
import com.tansun.data.db.visual.action.DommainEditAction;
import com.tansun.data.db.visual.editpart.TableEditPart;
import com.tansun.data.db.visual.editpart.tree.FolderTreeEditPart.FolderModel;
import com.tansun.data.db.visual.model.AbstractDBEntityModel;
import com.tansun.data.db.visual.model.ColumnModel;
import com.tansun.data.db.visual.model.DommainModel;
import com.tansun.data.db.visual.model.IndexModel;
import com.tansun.data.db.visual.model.RootModel;
import com.tansun.data.db.visual.model.TableModel;

public class ModelEditor {

	private GraphicalViewer viewer;
	private boolean editTable;

	public ModelEditor(GraphicalViewer viewer, boolean editTable){
		this.viewer = viewer;
		this.editTable = editTable;
	}

	public void editModel(Object model){
		EditPart rootEditPart = viewer.getContents();
		RootModel rootModel = (RootModel) rootEditPart.getModel();

		if(model instanceof TableModel){
			if(editTable){
				TableModel table = (TableModel) model;
				if(table.isLinkedTable()){
					UIUtils.openAlertDialog(DBPlugin.getResourceString("error.edit.linkedTable"));
					return;
				}
				TableEditPart.openTableEditDialog(viewer, table, rootModel);

			} else {
				EditPart select = getSelectEditPart(rootEditPart, model);
				if(select != null){
					viewer.select(select);
					viewer.reveal(select);
				}
			}

		} else if(model instanceof DommainModel){
			new DommainEditAction((GraphicalViewer)
					UIUtils.getActiveEditor().getAdapter(GraphicalViewer.class),
					(DommainModel) model).run();

		} else if(model instanceof ColumnModel){
			TableModel parent = null;
			LOOP: for(AbstractDBEntityModel entity: rootModel.getChildren()){
				if(entity instanceof TableModel){
					TableModel table = (TableModel) entity;
					for(ColumnModel column: table.getColumns()){
						if(column == model){
							parent = table;
							break LOOP;
						}
					}
				}
			}
			if(parent != null){
				if(parent.isLinkedTable()){
					UIUtils.openAlertDialog(DBPlugin.getResourceString("error.edit.linkedTable"));
					return;
				}
				TableEditPart.openTableEditDialog(
						viewer, parent, rootModel, (ColumnModel) model);
			}

		} else if(model instanceof FolderModel){
			((FolderModel) model).doEdit();

		} else if(model instanceof IndexModel){
			TableModel parent = null;
			LOOP: for(AbstractDBEntityModel entity: rootModel.getChildren()){
				if(entity instanceof TableModel){
					TableModel table = (TableModel) entity;
					for(IndexModel index: table.getIndices()){
						if(index == model){
							parent = table;
							break LOOP;
						}
					}
				}
			}
			if(parent != null){
				TableEditPart.openTableEditDialog(
						viewer, parent, rootModel, (IndexModel) model);
			}
		}

	}


	private EditPart getSelectEditPart(EditPart part, Object model){
		if(part.getModel() == model){
			return part;
		}
		for(Object child: part.getChildren()){
			EditPart result = getSelectEditPart((EditPart) child, model);
			if(result != null){
				return result;
			}
		}
		return null;
	}


}
