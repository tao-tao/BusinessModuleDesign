package avicit.platform6.tools.codegeneration.wizard;



import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.internal.ui.SharedImages;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;


public class ListDataFieldCpt extends Composite {

	private TableColumn newColumnTableColumn_6;
	private TableColumn newColumnTableColumn_5;
	private TableColumn newColumnTableColumn_4;
	private TableColumn newColumnTableColumn_3;
	private TableColumn newColumnTableColumn_2;
	private TableColumn newColumnTableColumn_1;

	// private TableColumn newColumnTableColumn;
	private Table table;

	public TableViewer tv;

	private Button button_1;

	private Button button;
	private String C_TYPE;
	private String C_NANME;
	private String C_VALUE;
	private String C_CLSTYPE;
	private String C_SCOPE;

	public ListDataFieldCpt(Composite parent, int style) {
		super(parent, style);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		setLayout(gridLayout);
//		Composite itemComp = new Composite(parent, style);
		tv = new TableViewer(this, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		tv.hashCode();
		table = tv.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		newColumnTableColumn_1 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_1.setWidth(60);
		newColumnTableColumn_1.setText("ID");

		newColumnTableColumn_2 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_2.setWidth(120);
		newColumnTableColumn_2.setText("标题");

		newColumnTableColumn_3 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_3.setWidth(100);
		newColumnTableColumn_3.setText("数据类型");
		 
		newColumnTableColumn_4 =  new TableColumn(table, SWT.NONE);
		newColumnTableColumn_4.setWidth(100);
		newColumnTableColumn_4.setText("是否隐藏");
		
	
		

		newColumnTableColumn_5 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_5.setWidth(160);
		newColumnTableColumn_5.setText("选择类型");
		
		this.tv.setContentProvider(new ArrayContentProvider());
		this.tv.setLabelProvider(new DFLabelProvider());

		final FCEditorModifier modifier = new FCEditorModifier();

		tv.setCellModifier(modifier);
		String[] cstrs = new String[] { _id, _header, _mapping, _hidden, _format,_search};
		tv.setColumnProperties(cstrs);
		tv.setCellEditors(new CellEditor[] {
				new TextCellEditor(this.table),
				new TextCellEditor(this.table),
				new TextCellEditor(this.table),
				new ComboBoxCellEditor(this.table, new String[] { "false", "true"}),
				new ComboBoxCellEditor(this.table, new String[] {"无",  "选人", "选部门","选角色","选群组","选岗位"}),
				new TextCellEditor(this.table)});
		
		Composite comp = new Composite(this, SWT.NONE);
		gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		comp.setLayout(gridLayout);
		comp.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, true));
		

	}

	protected void onBnClick_Remove() {
		UIHelper.removeTalbeSelection(this.tv);
	}

	protected void onBnClick_Add() {
		ListDataField fc = new ListDataField();
		fc.setDataIndex("");
		fc.setHeader("");
		fc.setDateformat("");
		fc.setMapping("");
		fc.setHidden(false);
		fc.setSearch(false);
		this.tv.add(fc);
	}

	@SuppressWarnings("unchecked")
	public void updateData(ListDataField[] objects) {
		this.tv.getInput();
		List<ListDataField> ds = UIHelper.getDatasetFromTableData(this.table);
		
		this.table.getParent();
		System.out.println(this.hashCode());
		List all = new ArrayList();
	
		/**
		 * 此处加版本号，来确定table是否改变
		 * 改变之后的不需要覆盖
		 */
		int j =0;
		for(int i=0; i<objects.length; i++)
		{
//			j++;
//			ListDataField test = new ListDataField();
//			test.setDataIndex("lidong"+j);
//			test.setMapping("lidong"+j);
//			test.setHeader("lidong"+j);
//			test.setVersion(0);
			if(ds.size()>0){
				if(ds.get(i).getVersion()>0){
					all.add(ds.get(i));
				}else{
					all.add(objects[i]);
				}
			}else{
				all.add(objects[i]);
			}
//			all.add(test);
		}
		this.tv.setInput(all.toArray(new ListDataField[all.size()]));
//		this.tv.setInput(objects);
	}

	public ListDataField[] fetchData() {
		return null;
	}

	private static final String _id = "_id";

	private static final String _header = "_header";

	private static final String _mapping = "_mapping";

	private static final String _hidden = "_hidden";
	
	private static final String _format = "_format";
	
	private static final String _search = "_search";

	private class FCEditorModifier implements ICellModifier {

		public boolean canModify(Object element, String property) {
			return true;
		}

		public Object getValue(Object element, String property) {
			ListDataField fc = (ListDataField) element;
			if (_id.equals(property)) {
				return fc.getDataIndex();
			} else if (_header.equals(property)) {
				return fc.getHeader();
			} else if (_mapping.equals(property)) {
				return fc.getMapping();
			} else if (_hidden.equals(property)) {
				return fc.getClstype();
			} else if (_format.equals(property)) {
				return fc.getSelecttype();
			} else if (_search.equals(property)){
				return Boolean.toString(fc.getSearch());
			}
			return null;
		}

		public void modify(Object element, String property, Object value) {
			if (element instanceof Item) {
				element = ((Item) element).getData();
			}
			ListDataField fc = (ListDataField) element;
			if (_id.equals(property)) {
				fc.setDataIndex((String) value);
			} else if (_header.equals(property)) {
				fc.setHeader((String) value);
			} else if (_mapping.equals(property)) {
				fc.setMapping((String)value);
			} else if (_hidden.equals(property)) {
				fc.setHidden("1".equals(value+""));
				fc.setVersion(fc.getVersion()+1);
			} else if (_format.equals(property)) {
				fc.setDateformat(SwitchValue((Integer)value));
				fc.setVersion(fc.getVersion()+1);
			} else if (_search.equals(property)){
				fc.setSearch("true".equals(value));
			}
			tv.update(fc, null);
		}

		private String SwitchValue(Integer value) {
//			int valueint = Integer.parseInt(value);
			switch (value) {
			case 0:
				return "无";
			case 1:
				return "U";
			case 2:
				return "D";
			case 3:
				return "R";
			case 4:
				return "P";
			case 5:
				return "G";

			default:
				break;
			}
			return null;
		}
	}

	private class DFLabelProvider implements ITableLabelProvider {
		public Image getColumnImage(Object element, int columnIndex) {
			if(columnIndex == 0)
				return SWTResourceManager.getImage(SharedImages.class, "page.gif");
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			ListDataField fc = (ListDataField) element;
			switch (columnIndex) {
			case 0:
				return fc.getDataIndex();
			case 1:
				return fc.getHeader();
			case 2:
				return fc.getMapping();
			case 3:
				return Boolean.toString(fc.getHidden());
			case 4:
				return fc.getDateformat();
			case 5:
				return Boolean.toString(fc.getSearch());
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
