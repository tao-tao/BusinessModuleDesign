package net.java.amateras.uml.usecasediagram.property;

import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;


public class ProcessDataFieldCpt extends Composite {

	private TableColumn newColumnTableColumn_9;
	private TableColumn newColumnTableColumn_8;
	private TableColumn newColumnTableColumn_7;
	private TableColumn newColumnTableColumn_6;
	private TableColumn newColumnTableColumn_5;
	private TableColumn newColumnTableColumn_4;
	private TableColumn newColumnTableColumn_3;
	private TableColumn newColumnTableColumn_2;
	private TableColumn newColumnTableColumn_1;

	// private TableColumn newColumnTableColumn;
	private Table table;

	private TableViewer tv;

	private Button button_1;

	private Button button;

	public ProcessDataFieldCpt(Composite parent, int style) {
		super(parent, style);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		setLayout(gridLayout);

		button = new Button(this, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				onBnClick_Add();
			}
		});
		button.setText("增加");

		button_1 = new Button(this, SWT.NONE);
		button_1.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				onBnClick_Remove();
			}
		});
		
		button_1.setLayoutData(new GridData());
		button_1.setText("删除");

		tv = new TableViewer(this, SWT.BORDER | SWT.FULL_SELECTION);
		table = tv.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

		newColumnTableColumn_1 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_1.setWidth(80);
		newColumnTableColumn_1.setText("字段名称");

		newColumnTableColumn_2 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_2.setWidth(100);
		newColumnTableColumn_2.setText("字段长度和类型");

		newColumnTableColumn_3 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_3.setWidth(80);
		newColumnTableColumn_3.setText("字段单位");
		 
		newColumnTableColumn_4 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_4.setWidth(100);
		newColumnTableColumn_4.setText("输入/输出属性");

		newColumnTableColumn_5 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_5.setWidth(80);
		newColumnTableColumn_5.setText("字段值来源");
		
		newColumnTableColumn_6 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_6.setWidth(90);
		newColumnTableColumn_6.setText("是否必须输入");
		
		newColumnTableColumn_7 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_7.setWidth(80);
		newColumnTableColumn_7.setText("字段初始值");
		
		newColumnTableColumn_8 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_8.setWidth(100);
		newColumnTableColumn_8.setText("字段值范围和列表");
		
		newColumnTableColumn_9 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_9.setWidth(80);
		newColumnTableColumn_9.setText("输出目的地");
		
		this.tv.setContentProvider(new ArrayContentProvider());
		this.tv.setLabelProvider(new DFLabelProvider());

		final FCEditorModifier modifier = new FCEditorModifier();

		tv.setCellModifier(modifier);
		String[] cstrs = new String[] { _name, _type, _unit, _property, _fieldSource,_isNeed,_initialValue,_fieldScope,_OutDestination};
		tv.setColumnProperties(cstrs);
		tv.setCellEditors(new CellEditor[] {
				new TextCellEditor(this.table),
				new TextCellEditor(this.table),
				new TextCellEditor(this.table),
				new ComboBoxCellEditor(this.table,new String[]{"输入","输出"}),
				new ComboBoxCellEditor(this.table,new String[]{"手工输入","联动查询返回","交易成功返回"}),
				new ComboBoxCellEditor(this.table,new String[]{"是","否"}),
				new TextCellEditor(this.table),
				new TextCellEditor(this.table),
				new ComboBoxCellEditor(this.table,new String[]{"打印机","文件","屏幕"})});
	}

	protected void onBnClick_Remove() {
		StructuredSelection ss = (StructuredSelection) tv.getSelection();
		if (ss != null && !ss.isEmpty()) {
			tv.remove(ss.getFirstElement());
		}
	}

	protected void onBnClick_Add() {

		DataField df = new DataField();
		df.setName("");
		df.setType("");
		df.setUnit("");
		df.setProperty(0);
		df.setFieldSource(0);
		df.setIsNeed(0);
		df.setInitialValue("");
		df.setFieldScope("");
		df.setOutDestination(0);
		this.tv.add(df);
	}

	@SuppressWarnings("unchecked")
	public void updateData(Object[] objects) {
		this.tv.setInput(objects);
	}

	public DataField[] fetchData() {
		Set ds = getDatasetFromTableData(this.table);
		return (DataField[]) ds.toArray(new DataField[ds.size()]);
	}
	
	public static Set getDatasetFromTableData(Table table) {
		Set data = new LinkedHashSet();
		TableItem[] its = table.getItems();

		for (int i = 0; i < its.length; i++) {
			Object mp = its[i].getData();
			data.add(mp);
		}
		return data;
	}
	private static final String _name = "name";

	private static final String _type = "type";

	private static final String _unit = "unit";

	private static final String _property = "property";

	private static final String _fieldSource = "fieldSource";
	
	private static final String _isNeed = "isNeed";
	
	private static final String _initialValue = "initialValue";
	
	private static final String _fieldScope = "fieldScope";
	
	private static final String _OutDestination = "OutDestination";

	private class FCEditorModifier implements ICellModifier {

		public boolean canModify(Object element, String property) {
			return true;
		}


		
		public Object getValue(Object element, String property) {
			DataField fc = (DataField) element;
			if (_name.equals(property)) {
				return fc.getName();
			} else if (_type.equals(property)) {
				return fc.getType();
			} else if (_unit.equals(property)) {
				return fc.getUnit();
			} else if (_property.equals(property)) {
				return fc.getProperty();
			} else if (_fieldSource.equals(property)) {
				return fc.getFieldSource();
			} else if (_isNeed.equals(property)){
				return fc.getIsNeed();
			} else if (_initialValue.equals(property)){
				return fc.getInitialValue();
			} else if (_fieldScope.equals(property)){
				return fc.getFieldScope();
			} else if (_OutDestination.equals(property)){
				return fc.getOutDestination();
			}
			return null;
		}

		public void modify(Object element, String property, Object value) {
			if (element instanceof Item) {
				element = ((Item) element).getData();
			}
			
			DataField fc = (DataField) element;
			if (_name.equals(property)) {
				fc.setName((String)value);
			} else if (_type.equals(property)) {
				fc.setType((String)value);
			} else if (_unit.equals(property)) {
				fc.setUnit((String)value);
			} else if (_property.equals(property)) {
				fc.setProperty((Integer)value);
			} else if (_fieldSource.equals(property)) {
				fc.setFieldSource((Integer)value);
			} else if (_isNeed.equals(property)){
				fc.setIsNeed((Integer)value);
			} else if (_initialValue.equals(property)){
				fc.setInitialValue((String)value);
			} else if (_fieldScope.equals(property)){
				fc.setFieldScope((String)value);
			} else if (_OutDestination.equals(property)){
				fc.setOutDestination((Integer)value);
			}
			
			tv.update(fc, null);
		}

	
	}

	private class DFLabelProvider implements ITableLabelProvider {
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}
//		df.setName(getName());
//		df.setType(getType());
//		df.setUnit(getUnit());
//		df.setProperty(isProperty());
//		df.setFieldSource(getFieldSource());
//		df.setNeed(isNeed());
//		df.setInitialValue(getInitialValue());
//		df.setFieldScope(getFieldScope());
//		df.setOutDestination(getOutDestination());
		//_name, _type, _unit, _property, _fieldSource,_isNeed,_initialValue,_fieldScope,_OutDestination
		public String getColumnText(Object element, int columnIndex) {
			DataField fc = (DataField) element;
			switch (columnIndex) {
			case 0:
				return fc.getName();
			case 1:
				return fc.getType();
			case 2:
				return fc.getUnit();
			case 3:
				if(fc.getProperty()==0){
					return 	"输入";
				}else {
					return 	"输出";
				}
				
			case 4:
				if(fc.getFieldSource() == 0){
					return "手工输入";
				}else if(fc.getFieldSource() == 1){
					return "联动查询返回";
				}else{
					return "交易成功返回";
				}
			case 5:
				if(fc.getIsNeed() == 0){
					return "是";
				}else{
					return "否";
				}
			case 6:
				return fc.getInitialValue();
			case 7:
				return fc.getFieldScope();
			case 8:
				if(fc.getOutDestination() == 0){
					return "打印机";
				}else if(fc.getOutDestination() == 1){
					return "文件";
				}else{
					return "屏幕";
				}
			}
			return null;
		}

		public void addListener(ILabelProviderListener listener) {
		}

		public void dispose() {
		}

		public boolean isLabelProperty(Object element, String property) {
			return true;
		}

		public void removeListener(ILabelProviderListener listener) {
		}
	}
}
