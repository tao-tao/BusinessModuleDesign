package com.tansun.data.newpackage;

import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import avicit.ui.data.inter.MakeJava;
import avicit.ui.data.inter.MakeJavaEvent;
import avicit.ui.data.inter.Manager;

import com.tansun.data.db.DBPlugin;
import com.tansun.data.db.util.UIUtils;
import com.tansun.data.db.visual.editpart.AbstractDBEntityEditPart;
import com.tansun.data.db.visual.editpart.ColumnFigure;
import com.tansun.data.db.visual.model.AbstractDBConnectionModel;
import com.tansun.data.db.visual.model.ColumnModel;
import com.tansun.data.db.visual.model.ForeignKeyMapping;
import com.tansun.data.db.visual.model.ForeignKeyModel;
import com.tansun.data.db.visual.model.IndexModel;
import com.tansun.data.db.visual.model.RootModel;

public class ViewEditPart extends AbstractDBEntityEditPart implements
		NodeEditPart, MakeJava {

	private Font font;

	/**
	 * Creates a {@link CreateTableConnectionCommand} instance as the connection
	 * creation command.
	 */
	@Override
	protected CreateConnectionCommand newCreateConnectionCommand() {
		return new CreateTableConnectionCommand();
	}

	protected IFigure createFigure() {
		ViewFigure figure = new ViewFigure();
		updateFigure(figure);
		return figure;
	}

	private void updateFigure(ViewFigure figure) {
		ViewModel model = (ViewModel) getModel();
		RootModel root = (RootModel) getParent().getModel();

		if (font != null) {
			font.dispose();
		}

		FontData[] fontData = root.getFontData();
		font = new Font(Display.getDefault(), fontData);
		figure.setFont(font);

		if (root.getLogicalMode()) {
			figure.setTableName(model.getLogicalName());
		} else {
			figure.setTableName(model.getTableName());
		}
		figure.setErrorMessage(model.getError());
		figure.setBorders(Integer.valueOf(model.getBorder()));
		figure.removeAllColumns();
		figure.setLinkedTable(model.isLinkedTable());
		figure.setBackgroundColor(DBPlugin.getDefault().getColor(
				model.getBackgroundColor()));
		;
		ColumnModel[] columns = model.getColumns();
		for (int i = 0; i < columns.length; i++) {
			ColumnFigure[] figures = createColumnFigure(root, model, columns[i]);
			figure.add(figures[0]);
			figure.add(figures[1]);
			figure.add(figures[2]);
		}
	}

	@Override
	public void deactivate() {
		super.deactivate();
		if (font != null) {
			font.dispose();
			Manager.getInstance().remove(this);
		}
	}

	@Override
	public void activate() {
		// TODO Auto-generated method stub
		super.activate();
		Manager.getInstance().add(this);
	}

	private ColumnFigure[] createColumnFigure(RootModel root, ViewModel table,
			ColumnModel model) {
		StringBuffer sb = new StringBuffer();
		if (root.getLogicalMode()) {
			sb.append(model.getColumnType().getLogicalName());
		} else {
			sb.append(model.getColumnType().getName());
		}
		if (model.getColumnType().supportSize() && model.getSize().length() > 0) {
			sb.append("(");
			sb.append(model.getSize());
			sb.append(")");
		}
		ColumnFigure label1 = new ColumnFigure();
		ColumnFigure label2 = new ColumnFigure();
		ColumnFigure label3 = new ColumnFigure();
		if (root.getLogicalMode()) {
			label1.setText(model.getLogicalName());
		} else {
			label1.setText(model.getColumnName());
		}
		label1.setUnderline(model.isPrimaryKey());
		label2.setText(sb.toString());
		label2.setUnderline(model.isPrimaryKey());

		List<AbstractDBConnectionModel> connections = table
				.getModelSourceConnections();
		LOOP: for (int i = 0; i < connections.size(); i++) {
			AbstractDBConnectionModel obj = connections.get(i);
			if (obj instanceof ForeignKeyModel) {
				ForeignKeyMapping[] mappings = ((ForeignKeyModel) obj)
						.getMapping();
				for (int j = 0; j < mappings.length; j++) {
					if (mappings[j].getRefer() != null
							&& mappings[j].getRefer().getColumnName().equals(
									model.getColumnName())) {
						label1.setText(label1.getText() + "(FK)");
						break LOOP;
					}
				}
			}
		}

		LOOP: for (IndexModel index : table.getIndices()) {
			if (index.getIndexType().getName().equals("UNIQUE")) {
				for (String columnName : index.getColumns()) {
					if (columnName.equals(model.getColumnName())) {
						label1.setText(label1.getText() + "(UQ)");
						break LOOP;
					}
				}
			}
		}

		if (model.isNotNull()
				&& DBPlugin.getDefault().getPreferenceStore().getBoolean(
						DBPlugin.PREF_SHOW_NOT_NULL)) {
			if (root.getLogicalMode()) {
				label3.setText(DBPlugin.getResourceString("label.notNull"));
			} else {
				label3.setText("NOT NULL");
			}
		}

		return new ColumnFigure[] { label1, label2, label3 };
	}

	protected void refreshVisuals() {
		super.refreshVisuals();
		updateFigure((ViewFigure) getFigure());
		refreshChildren();
	}

	public void doubleClicked() {
		final ViewModel model = (ViewModel) getModel();
		if (model.isLinkedTable()) {
			UIUtils.openAlertDialog(DBPlugin
					.getResourceString("error.edit.linkedTable"));
		} else {
			RootModel root = (RootModel) getParent().getModel();
			openTableEditDialog(getViewer(), model, root);
		}
	}

	/**
	 * Opens the {@link ViewtDialog}.
	 * 
	 * @param viewer
	 *            the viewer
	 * @param model
	 *            the table model
	 * @param root
	 *            the root model
	 */
	public static void openTableEditDialog(EditPartViewer viewer,
			final ViewModel model, RootModel root) {
		openTableEditDialog(viewer, model, root, (ColumnModel) null);
	}

	/**
	 * Opens the {@link ViewtDialog} to edit a given column.
	 * 
	 * @param viewer
	 *            the viewer
	 * @param model
	 *            the table model
	 * @param root
	 *            the root model
	 * @param editColumn
	 *            the editing target column model
	 */
	public static void openTableEditDialog(EditPartViewer viewer,
			final ViewModel model, RootModel root, ColumnModel editColumn) {

		ViewtDialog dialog = new ViewtDialog(viewer.getControl()
				.getShell(), root, model.getTableName(),
				model.getLogicalName(), model.getDescription(), model
						.getColumns(), editColumn, model.getIndices(), false,
				null, model.getSql());

		if (dialog.open() == Dialog.OK) {
			List<ColumnModel> columns = dialog.getResultColumns();
			List<IndexModel> indices = dialog.getResultIncices();

			viewer.getEditDomain().getCommandStack().execute(
					new TableEditCommand(model, dialog.getTableName(), dialog
							.getTableLogicalName(), dialog
							.getTableDescription(), columns
							.toArray(new ColumnModel[columns.size()]), indices
							.toArray(new IndexModel[indices.size()]), dialog
							.getSql()));
		}
	}

	/**
	 * Opens the {@link ViewtDialog} to edit a given index.
	 * 
	 * @param viewer
	 *            the viewer
	 * @param model
	 *            the table model
	 * @param root
	 *            the root model
	 * @param editIndex
	 *            the editing target index model
	 */
	public static void openTableEditDialog(EditPartViewer viewer,
			final ViewModel model, RootModel root, IndexModel editIndex) {

		ViewtDialog dialog = new ViewtDialog(viewer.getControl()
				.getShell(), root, model.getTableName(),
				model.getLogicalName(), model.getDescription(), model
						.getColumns(), null, model.getIndices(), true,
				editIndex, model.getSql());

		if (dialog.open() == Dialog.OK) {
			List<ColumnModel> columns = dialog.getResultColumns();
			List<IndexModel> indices = dialog.getResultIncices();

			viewer.getEditDomain().getCommandStack().execute(
					new TableEditCommand(model, dialog.getTableName(), dialog
							.getTableLogicalName(), dialog
							.getTableDescription(), columns
							.toArray(new ColumnModel[columns.size()]), indices
							.toArray(new IndexModel[indices.size()]), dialog
							.getSql()));
		}
	}

	private static class TableEditCommand extends Command {

		private ViewModel model;
		private String oldTableName;
		private String newTableName;
		private String oldTableLogicalName;
		private String newTableLogicalName;
		private String oldTableDescription;
		private String newTableDescription;
		private ColumnModel[] oldColumns;
		private ColumnModel[] newColumns;
		private IndexModel[] oldIndices;
		private IndexModel[] newIndices;
		private String oldSql;
		private String newSql;

		public TableEditCommand(ViewModel model, String newTableName,
				String newTableLogicalName, String newTableDescription,
				ColumnModel[] newColumns, IndexModel[] newIndices, String sql) {
			this.model = model;
			this.oldTableName = model.getTableName();
			this.newTableName = newTableName;
			this.oldTableLogicalName = model.getLogicalName();
			this.newTableLogicalName = newTableLogicalName;
			this.oldTableDescription = model.getDescription();
			this.newTableDescription = newTableDescription;
			this.oldColumns = model.getColumns();
			this.newColumns = newColumns;
			this.oldIndices = model.getIndices();
			this.newIndices = newIndices;
			this.oldSql = model.getSql();
			this.newSql = sql;
		}

		public void execute() {
			this.model.setTableName(newTableName);
			this.model.setLogicalName(newTableLogicalName);
			this.model.setDescription(newTableDescription);
			this.model.setColumns(newColumns);
			this.model.setIndices(newIndices);
			this.model.setSql(newSql);
		}

		public void undo() {
			this.model.setTableName(oldTableName);
			this.model.setLogicalName(oldTableLogicalName);
			this.model.setDescription(oldTableDescription);
			this.model.setColumns(oldColumns);
			this.model.setIndices(oldIndices);
			this.model.setSql(oldSql);
		}
	}

	protected static class CreateTableConnectionCommand extends
			CreateConnectionCommand {

		protected ColumnModel[] oldColumns;

		public void execute() {
			if (connection instanceof ForeignKeyModel) {
				ViewModel table = (ViewModel) getModel();
				List<AbstractDBConnectionModel> sources = table
						.getModelSourceConnections();
				String fkName = table.getTableName() + "_FK_";
				int count = 1;
				while (true) {
					for (int i = 0; i < sources.size(); i++) {
						AbstractDBConnectionModel obj = sources.get(i);
						if (obj instanceof ForeignKeyModel) {
							if (((ForeignKeyModel) obj).getForeignKeyName()
									.equals(fkName + count)) {
								count++;
								break;
							}
						}
					}
					fkName = fkName + count;
					break;
				}
				((ForeignKeyModel) connection).setForeignKeyName(fkName);

				ForeignKeyMapping[] mappings = ((ForeignKeyModel) connection)
						.getMapping();
				for (ForeignKeyMapping mapping : mappings) {
					if (mapping.getRefer() == null) {
						ColumnModel targetColumn = mapping.getTarget();
						ColumnModel referColumn = new ColumnModel();
						referColumn.setColumnName(targetColumn.getColumnName());
						referColumn.setLogicalName(targetColumn
								.getLogicalName());
						referColumn.setColumnType(targetColumn.getColumnType());
						referColumn.setDommain(targetColumn.getDommain());
						referColumn.setSize(targetColumn.getSize());
						referColumn.setDescription(targetColumn
								.getDescription());

						oldColumns = table.getColumns();
						ColumnModel[] newArray = new ColumnModel[oldColumns.length + 1];
						System.arraycopy(oldColumns, 0, newArray, 0,
								oldColumns.length);
						newArray[oldColumns.length] = referColumn;

						table.setColumns(newArray);
					}
				}
			}

			super.execute();
		}

		public void undo() {
			if (oldColumns != null) {
				ViewModel table = (ViewModel) getModel();
				table.setColumns(oldColumns);
			}
			super.undo();
		}
	}

	public void finish(MakeJavaEvent name) {
		// TODO Auto-generated method stub
		if (name.getName().equals(((ViewModel) getModel()).getTableName())) {
			getViewer().getEditDomain().getCommandStack().execute(
					new MyCommand(new RGB(255, 255, 0), this,name));
		}
	}

	class MyCommand extends Command {
		private ViewEditPart model;
		private RGB r;
		private MakeJavaEvent event;

		MyCommand(RGB r, ViewEditPart model,MakeJavaEvent event) {
			this.model = model;
			this.r = r;
			this.event=event;
		}

		public void execute() {
			
			
			
			
			
			ViewModel models=((ViewModel) model.getModel());
			//models.setBackgroundColor(r);
			models.setPath(event.getPath());
			models.setProjectName(event.getProjectPath());
			models.setHbmPath(event.getHbmPath());
			models.setJavaName(event.getJavaPath());
			models.setError("finish project"+event.getProjectPath());
			//models.setp
			models.setBorder("1");
			System.out.println("---------------------------------------");
			System.out.println(models.getProjectName());
			System.out.println(models.getHbmPath());
			
		}
	}
}
