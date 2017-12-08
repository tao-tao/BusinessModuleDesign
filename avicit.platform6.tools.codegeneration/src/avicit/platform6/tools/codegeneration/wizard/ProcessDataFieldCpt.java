//package avicit.platform6.tools.codegeneration.wizard;
//
//
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Set;
//
//import org.eclipse.jface.viewers.ArrayContentProvider;
//import org.eclipse.jface.viewers.CellEditor;
//import org.eclipse.jface.viewers.ICellModifier;
//import org.eclipse.jface.viewers.ILabelProviderListener;
//import org.eclipse.jface.viewers.ITableLabelProvider;
//import org.eclipse.jface.viewers.TableViewer;
//import org.eclipse.jface.viewers.TextCellEditor;
//import org.eclipse.swt.SWT;
//import org.eclipse.swt.events.SelectionAdapter;
//import org.eclipse.swt.events.SelectionEvent;
//import org.eclipse.swt.graphics.Image;
//import org.eclipse.swt.layout.GridData;
//import org.eclipse.swt.layout.GridLayout;
//import org.eclipse.swt.widgets.Button;
//import org.eclipse.swt.widgets.Composite;
//import org.eclipse.swt.widgets.Item;
//import org.eclipse.swt.widgets.Table;
//import org.eclipse.swt.widgets.TableColumn;
//
//
//
//public class ProcessDataFieldCpt extends Composite {
//
//	private TableColumn newColumnTableColumn_6;
//	private TableColumn newColumnTableColumn_5;
//	private TableColumn newColumnTableColumn_4;
//	private TableColumn newColumnTableColumn_3;
//	private TableColumn newColumnTableColumn_2;
//	private TableColumn newColumnTableColumn_1;
//
//	// private TableColumn newColumnTableColumn;
//	private Table table;
//
//	private TableViewer tv;
//
//	private Button button_1;
//
//	private Button button;
//
//	public ProcessDataFieldCpt(Composite parent, int style, boolean compact) {
//		super(parent, style);
//		GridLayout gridLayout = new GridLayout();
//		gridLayout.numColumns = 2;
//		setLayout(gridLayout);
//
//		tv = new TableViewer(this, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
//		table = tv.getTable();
//		table.setHeaderVisible(true);
//		table.setLinesVisible(true);
//		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
//
//		newColumnTableColumn_1 = new TableColumn(table, SWT.NONE);
//		newColumnTableColumn_1.setWidth(!compact?80:200);
//		newColumnTableColumn_1.setText("名称");
//
//		newColumnTableColumn_2 = new TableColumn(table, SWT.NONE);
//		newColumnTableColumn_2.setWidth(!compact?100:200);
//		newColumnTableColumn_2.setText("描述");
//
//		if(!compact)
//		{
//			newColumnTableColumn_3 = new TableColumn(table, SWT.NONE);
//			newColumnTableColumn_3.setWidth(120);
//			newColumnTableColumn_3.setText("数据类型");
//			 
//			newColumnTableColumn_4 = new TableColumn(table, SWT.NONE);
//			newColumnTableColumn_4.setWidth(100);
//			newColumnTableColumn_4.setText("控件类型");
//	
//			newColumnTableColumn_5 = new TableColumn(table, SWT.NONE);
//			newColumnTableColumn_5.setWidth(100);
//			newColumnTableColumn_5.setText("参数数据");
//			
//			newColumnTableColumn_6 = new TableColumn(table, SWT.NONE);
//			newColumnTableColumn_6.setWidth(100);
//			newColumnTableColumn_6.setText("是否隐藏");
//		}		
//		this.tv.setContentProvider(new ArrayContentProvider());
//		this.tv.setLabelProvider(new DFLabelProvider());
//
//		final FCEditorModifier modifier = new FCEditorModifier();
//
//		tv.setCellModifier(modifier);
//		String[] cstrs = new String[] { _name, _desc, _type, _xtype, _data,_hidden};
//		tv.setColumnProperties(cstrs);
//		tv.setCellEditors(new CellEditor[] {
//				new TextCellEditor(this.table),
//				new TextCellEditor(this.table),
//				new TextCellEditor(this.table),
//				new TextCellEditor(this.table),
//				new TextCellEditor(this.table),
//				new TextCellEditor(this.table)});
//
//		Composite comp = new Composite(this, SWT.NONE);
//		gridLayout = new GridLayout();
//		gridLayout.numColumns = 1;
//		comp.setLayout(gridLayout);
//		comp.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, true));
//		button = new Button(comp, SWT.NONE);
//		button.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(SelectionEvent e) {
//				onBnClick_Add();
//			}
//		});
//		button.setText("增加");
//
//		button_1 = new Button(comp, SWT.NONE);
//		button_1.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(SelectionEvent e) {
//				onBnClick_Remove();
//			}
//		});
//		button_1.setLayoutData(new GridData());
//		button_1.setText("删除");
//
//	}
//
//	protected void onBnClick_Remove() {
//		UIHelper.removeTalbeSelection(this.tv);
//	}
//
//	protected void onBnClick_Add() {
//		DataField fc = new DataField();
//		fc.setName("");
//		fc.setData("");
//		fc.setDataType("java.lang.String");
//		fc.setFieldLabel("");
//		fc.setXtype("textfield");
//		fc.setHidden(false);
//		this.tv.add(fc);
//	}
//
//	@SuppressWarnings("unchecked")
//	public void updateData(Object[] objects) {
//		List all = new ArrayList();
//		Set ds = UIHelper.getDatasetFromTableData(this.table);
//		
//		for(int i=0; i<objects.length; i++)
//		{
//			if(!ds.contains(objects[i]))
//					all.add(objects[i]);
//		}
//		this.tv.setInput(all.toArray(new DataField[all.size()]));
//	}
//
//	public DataField[] fetchData() {
//		Set ds = UIHelper.getDatasetFromTableData(this.table);
//		return (DataField[]) ds.toArray(new DataField[ds.size()]);
//	}
//
//	private static final String _name = "_name";
//
//	private static final String _desc = "_desc";
//
//	private static final String _type = "_type";
//
//	private static final String _xtype = "_xtype";
//	
//	private static final String _data = "_data";
//	
//	private static final String _hidden = "_hidden";
//
//
//	private class FCEditorModifier implements ICellModifier {
//
//		public boolean canModify(Object element, String property) {
//			return true;
//		}
//
//		public Object getValue(Object element, String property) {
//			DataField fc = (DataField) element;
//
//			if (_name.equals(property)) {
//				return fc.getName();
//			} else if (_desc.equals(property)) {
//				return fc.getFieldLabel();
//			} else if (_type.equals(property)) {
//				return fc.getDataType()==null?"java.lang.String":fc.getDataType();
//			} else if (_data.equals(property)) {
//				return fc.getData();
//			} else if (_xtype.equals(property)) {
//				return fc.getXtype();
//			} else if (_hidden.equals(property)) {
//				return Boolean.toString(fc.getHidden());
//			}
//			return null;
//		}
//
//		public void modify(Object element, String property, Object value) {
//			if (element instanceof Item) {
//				element = ((Item) element).getData();
//			}
//			DataField fc = (DataField) element;
//			if (_name.equals(property)) {
//				fc.setName((String) value);
//			} else if (_desc.equals(property)) {
//				fc.setFieldLabel((String) value);
//			} else if (_type.equals(property)) {
//				fc.setDataType((String)value);
//			} else if (_data.equals(property)) {
//				fc.setData((String) value);
//			} else if (_xtype.equals(property)) {
//				fc.setXtype((String) value);
//			} else if (_hidden.equals(property)) {
//				fc.setHidden("true".equals(value));
//			}
//			tv.update(fc, null);
//		}
//	}
//
//	private class DFLabelProvider implements ITableLabelProvider {
//		public Image getColumnImage(Object element, int columnIndex) {
//			if(columnIndex == 0)
//				return SWTResourceManager.getImage(SharedImages.class, "page.gif");
//			return null;
//		}
//
//		public String getColumnText(Object element, int columnIndex) {
//			DataField fc = (DataField) element;
//			switch (columnIndex) {
//			case 0:
//				return fc.getName();
//			case 1:
//				return fc.getFieldLabel();
//			case 2:
//				return fc.getDataType() == null?"":fc.getDataType();
//			case 3:
//				return fc.getXtype();
//			case 4:
//				return fc.getData();
//			case 5:
//				return Boolean.toString(fc.getHidden());
//			}
//			return null;
//		}
//
//		public void addListener(ILabelProviderListener listener) {
//		}
//
//		public void dispose() {
//		}
//
//		public boolean isLabelProperty(Object element, String property) {
//			return true;
//		}
//
//		public void removeListener(ILabelProviderListener listener) {
//		}
//	}
//}
