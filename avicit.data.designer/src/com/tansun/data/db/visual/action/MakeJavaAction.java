package com.tansun.data.db.visual.action;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.swt.graphics.RGB;

import avicit.ui.platform.db.Container;
import avicit.ui.platform.db.DBColumn;
import avicit.ui.platform.db.DBTable;

import com.tansun.data.db.DBPlugin;
import com.tansun.data.db.visual.editor.VisualDBEditor;
import com.tansun.data.db.visual.editpart.TableEditPart;
import com.tansun.data.db.visual.model.ColumnModel;
import com.tansun.data.db.visual.model.TableModel;

public class MakeJavaAction extends SelectionAction {
	private List<TableEditPart> ls = new ArrayList<TableEditPart>();
	private List<DBTable> lt = new ArrayList<DBTable>();
	private VisualDBEditor editor;
	public MakeJavaAction(VisualDBEditor editor) {
		super(editor);
		this.editor=editor;
		// setAccelerator(SWT.CTRL | 'm');
		setId(MakeJavaAction.class.getName());
		// setActionDefinitionId(MakeJavaAction.class.getName());
		setText(DBPlugin.getResourceString("action.MakeJava"));
		
	}

	class MyCommand extends Command {
		private TableModel model;
		private RGB r;

		MyCommand(RGB r, TableModel model) {
			this.model = model;
			this.r = r;
		}

		public void execute() {
			model.setBackgroundColor(r);
		}
	}

	public List<DBTable> changeToTopTable(List<DBTable> t) {
		ls.clear();
		lt.clear();
		Container con = new Container();
		List<Object> selection = getSelectedObjects();
		for (int i = 0; i < selection.size(); i++) {
			Object obj = selection.get(i);
			if (obj instanceof TableEditPart) {
				
				TableModel tmodel = (TableModel) ((TableEditPart) obj)
						.getModel();
				ls.add((TableEditPart) obj);
				ColumnModel[] cmodels = tmodel.getColumns();
				DBTable table = new DBTable(con, tmodel.getTableName());
				for (int p = 0; p < cmodels.length; p++) {
					ColumnModel model = cmodels[p];
					DBColumn newColumn = new DBColumn(table, model
							.getColumnName(), model.getColumnType().getName(),
							Integer.valueOf(model.getSize()), model.getDigits(), model
									.isNotNull() ? 1 : 0);
					table.notifyColumn(newColumn);
					if(model.isPrimaryKey()){
						table.notifyPrimaryKey(newColumn.getName());
					}
				}
				t.add(table);
			}

		}

		return t;
		// con.setTables(tables)
	}

	@Override
	public void run() {/*
		try {
			changeToTopTable(lt);
			//MappingWizard wizard = new MappingWizard(null);
			
			wizard.setFileName(editor.getTitleToolTip().split("[.]")[0]);
			
			wizard.setTableList(changeToTopTable(lt));
			wizard.init(PlatformUI.getWorkbench(), new StructuredSelection(
					ProjectFinder.getCurrentProject()));
			WizardDialog dialog = new WizardDialog(Display.getDefault()
					.getActiveShell(), wizard);
			dialog.create();
			dialog.open();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		editor.doSaveAs();
		

	*/}

	
	@SuppressWarnings("unchecked")
	protected boolean calculateEnabled() {
		List<Object> selection = getSelectedObjects();
		for (int i = 0; i < selection.size(); i++) {
			Object obj = selection.get(i);
			if (obj instanceof TableEditPart) {
				return true;
			}
		}
		return false;
	}
}
