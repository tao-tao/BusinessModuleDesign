package com.tansun.data.db.validator;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.IPreferenceStore;

import com.tansun.data.db.DBPlugin;
import com.tansun.data.db.dialect.DialectProvider;
import com.tansun.data.db.dialect.IColumnType;
import com.tansun.data.db.dialect.IDialect;
import com.tansun.data.db.visual.model.AbstractDBConnectionModel;
import com.tansun.data.db.visual.model.AbstractDBEntityModel;
import com.tansun.data.db.visual.model.ColumnModel;
import com.tansun.data.db.visual.model.ForeignKeyMapping;
import com.tansun.data.db.visual.model.ForeignKeyModel;
import com.tansun.data.db.visual.model.RootModel;
import com.tansun.data.db.visual.model.TableModel;

/**
 * The ER-Diagram validator.
 * 
 * @author Naoki Takezoe
 * @since 1.0.5
 */
public class DiagramValidator {

	private RootModel model;

	private Set<String> tableNames = new HashSet<String>();
	private Set<String> logicalNames = new HashSet<String>();

	/**
	 * The constructor.
	 * 
	 * @param model
	 *            the model for validation
	 */
	public DiagramValidator(RootModel model) {
		this.model = model;
	}

	/**
	 * Executes validation.
	 * 
	 * @return validation errors
	 */
	public DiagramErrors doValidate() {
		DiagramErrors errors = new DiagramErrors();

		for (AbstractDBEntityModel entity : model.getChildren()) {
			if (entity instanceof TableModel) {
				TableModel table = (TableModel) entity;
				table.setError("");
				validateTable(errors, model, table);
			}
		}

		String dialectName = model.getDialectName();
		IDialect dialect = DialectProvider.getDialect(dialectName);
		dialect.validate(errors, model);
		loadValidators(model, errors);
		return errors;
	}

	private void validateTable(DiagramErrors errors, RootModel root,
			TableModel table) {
		
		IPreferenceStore store = DBPlugin.getDefault().getPreferenceStore();

		// Validates TableModel
		String tableName = table.getTableName();
		if (tableName == null || tableName.length() == 0) {
			errors.addError(
					store.getString(DBPlugin.PREF_VALIDATE_PHYSICAL_TABLE_NAME_REQUIRED),
					table,
					DBPlugin.getResourceString("validation.error.tableName.required"));

		} else if (tableNames.contains(tableName.toUpperCase())) {
			errors.addError(
					store.getString(DBPlugin.PREF_VALIDATE_PHYSICAL_TABLE_NAME_DUPLICATED),
					table,
					DBPlugin.getResourceString("validation.error.tableName.duplicated"));
		} else {
			tableNames.add(tableName.toUpperCase());
		}

		String logicalName = table.getLogicalName();
		if (logicalName == null || logicalName.length() == 0) {
			errors.addError(
					store.getString(DBPlugin.PREF_VALIDATE_LOGICAL_TABLE_NAME_REQUIRED),
					table,
					DBPlugin.getResourceString("validation.error.logicalTableName.required"));

		} else if (logicalNames.contains(logicalName.toUpperCase())) {
			errors.addError(
					store.getString(DBPlugin.PREF_VALIDATE_LOGICAL_TABLE_NAME_DUPLICATED),
					table,
					DBPlugin.getResourceString("validation.error.logicalTableName.duplicated"));
		} else {
			logicalNames.add(logicalName.toUpperCase());
		}

		// Validates ColumnModels
		ColumnModel[] columns = table.getColumns();
		if (columns.length == 0) {
			errors.addError(store.getString(DBPlugin.PREF_VALIDATE_NO_COLUMNS),
					table,
					DBPlugin.getResourceString("validation.error.noColumns"));
		} else {
			Set<String> columnNames = new HashSet<String>();
			Set<String> logicalColumnNames = new HashSet<String>();
			boolean findPk = false;
			int i=0;
			for (ColumnModel column : columns) {
				if (column.isPrimaryKey()) {
					findPk = true;
				}
				if(column.getColumnType().getName().contains("LONG")){
					
					i++;
					if(i>1){
						errors.addError(
								store.getString(DBPlugin.PREF_VALIDATE_PHYSICAL_COLUMN_NAME_DUPLICATED),
								table,
								column,
								"LONG- Not allowed two!");
					}
				}
				String columnName = column.getColumnName();
				if (columnName == null || columnName.length() == 0) {
					errors.addError(
							store.getString(DBPlugin.PREF_VALIDATE_PHYSICAL_COLUMN_NAME_REQUIRED),
							table,
							column,
							DBPlugin.getResourceString("validation.error.columnName.required"));

				} else if (columnNames.contains(columnName.toUpperCase())) {
					errors.addError(
							store.getString(DBPlugin.PREF_VALIDATE_PHYSICAL_COLUMN_NAME_DUPLICATED),
							table,
							column,
							DBPlugin.getResourceString("validation.error.columnName.duplicated"));
				} else {
					columnNames.add(columnName.toUpperCase());
				}

				String logicalColumnName = column.getLogicalName();
				if (logicalColumnName == null
						|| logicalColumnName.length() == 0) {
					errors.addError(
							store.getString(DBPlugin.PREF_VALIDATE_LOGICAL_COLUMN_NAME_REQUIRED),
							table,
							column,
							DBPlugin.getResourceString("validation.error.logicalColumnName.required"));

				} else if (logicalColumnNames.contains(logicalColumnName.toUpperCase())) {
					errors.addError(
							store.getString(DBPlugin.PREF_VALIDATE_LOGICAL_COLUMN_NAME_DUPLICATED),
							table,
							column,
							DBPlugin.getResourceString("validation.error.logicalColumnName.duplicated"));
				} else {
					logicalColumnNames.add(logicalColumnName.toUpperCase());
				}
				// /
				int size = Integer.valueOf(column.getSize());
				if (column.getColumnType().supportSize()) {
					if (size < column.getColumnType().getMinSize()
							|| size > column.getColumnType().getMaxSize()) {
						errors.addError(
								store.getString(DBPlugin.PREF_VALIDATE_PHYSICAL_COLUMN_NAME_DUPLICATED),
								table, column, size+" Out of range("
										+ column.getColumnType().getMinSize()
										+ ","
										+ column.getColumnType().getMaxSize()+")");
						
					}
					if(column.getColumnType().getName().equals("NUMBER")){
						size=column.getDigits();
						//System.out.println(size+"  "+column.getColumnType().getDMinsize()+" ");
						if(size < column.getColumnType().getDMinsize()
								|| size > column.getColumnType().getDMaxsize()){
							errors.addError(
									store.getString(DBPlugin.PREF_VALIDATE_PHYSICAL_COLUMN_NAME_DUPLICATED),
									table, column, size+" Out of range("
											+ column.getColumnType().getDMinsize()
											+ ","
											+ column.getColumnType().getDMaxsize()+")");
							
						}
					}
				}

			}

			if (!findPk) {
				errors.addError(
						store.getString(DBPlugin.PREF_VALIDATE_PRIMARY_KEY),
						table,
						DBPlugin.getResourceString("validation.error.noPrimaryKey"));
			}

		}

		// Validates Relations
		for (AbstractDBConnectionModel conn : table.getModelSourceConnections()) {
			if (conn instanceof ForeignKeyModel) {
				ForeignKeyModel fk = (ForeignKeyModel) conn;
				for (ForeignKeyMapping mapping : fk.getMapping()) {
					ColumnModel referer = mapping.getRefer();
					ColumnModel target = mapping.getTarget();

					IColumnType refererType = referer.getColumnType();
					IColumnType targetType = target.getColumnType();

					if (!refererType.getName().equals(targetType.getName())) {
						errors.addError(
								store.getString(DBPlugin.PREF_VALIDATE_FOREIGN_KEY_COLUMN_TYPE),
								table,
								referer,
								DBPlugin.getResourceString("validation.error.foreignKey.columnType"));
						continue;
					}
					if (refererType.supportSize()
							&& !referer.getSize().equals(target.getSize())) {
						errors.addError(
								store.getString(DBPlugin.PREF_VALIDATE_FOREIGN_KEY_COLUMN_SIZE),
								table,
								referer,
								DBPlugin.getResourceString("validation.error.foreignKey.columnSize"));
						continue;
					}
				}

			}
		}
	}

	private void loadValidators(RootModel model, DiagramErrors error) {
		IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
		IExtensionPoint extensionPoint = extensionRegistry
				.getExtensionPoint("com.tansun.data.designer.validate");
		IExtension extensions[] = extensionPoint.getExtensions();
		for (int i = 0; i < extensions.length; i++) {
			IConfigurationElement elements[] = extensions[i]
					.getConfigurationElements();
			for (int j = 0; j < elements.length; j++) {
				IConfigurationElement element = elements[j];
				try {
					Object object = element.createExecutableExtension("class");
					/*
					 * String type = element.getAttribute("type"); String
					 * category = element.getAttribute("category"); String
					 * resource = element.getAttribute("resource");
					 */
					if (object instanceof Validator) {
						Validator vali = (Validator) object;
						vali.execute(model, error);
					}
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void doLoadModelFactory(IConfigurationElement element) {

	}
}
