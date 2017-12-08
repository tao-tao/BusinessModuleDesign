package net.java.amateras.uml.usecasediagram.property;

import java.util.LinkedHashSet;
import java.util.Set;

import net.java.amateras.uml.usecasediagram.model.DroolData;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
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
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;

public class DroolDataFieldCpt extends Composite {

	private TableColumn newColumnTableColumn_1;

	// private TableColumn newColumnTableColumn;
	private Table table;

	private TableViewer tv;

	private Button button_1;

	private Button button;

	private WebPageSelectionDialog dialog;

	public DroolDataFieldCpt(Composite parent, int style) {
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
		button.setText("规则");

		button_1 = new Button(this, SWT.NONE);
		button_1.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				onBnClick_Remove();
			}
		});

		button_1.setLayoutData(new GridData());
		button_1.setText("规则");

		tv = new TableViewer(this, SWT.BORDER | SWT.FULL_SELECTION);
		table = tv.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

		newColumnTableColumn_1 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_1.setWidth(800);
		newColumnTableColumn_1.setText("�����ļ�·��");

		this.tv.setContentProvider(new ArrayContentProvider());
		this.tv.setLabelProvider(new DFLabelProvider());

		final FCEditorModifier modifier = new FCEditorModifier();

		tv.setCellModifier(modifier);
		String[] cstrs = new String[] { _path };
		tv.setColumnProperties(cstrs);
		tv.setCellEditors(new CellEditor[] { new TextCellEditor(this.table) });
	}

	protected void onBnClick_Remove() {
		StructuredSelection ss = (StructuredSelection) tv.getSelection();
		if (ss != null && !ss.isEmpty()) {
			tv.remove(ss.getFirstElement());
		}
	}

	protected void onBnClick_Add() {
		DroolData fc = new DroolData();
		dialog = new WebPageSelectionDialog(getShell(), getCurrentProject(),
				SWT.NONE);

		if (dialog.open() == Dialog.OK) {
			Object[] files = (Object[]) dialog.getResult();
			if (files.length > 0) {
				IResource path = (IResource) files[0];
				String pathStr = path.getProjectRelativePath().toString();
				fc.setPath(pathStr);
			}
		}

		this.tv.add(fc);
	}

	public IEditorPart getActiveEditor() {
		return (IEditorPart) PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActiveEditor();
	}

	public IFile getCurrentFile() {
		return ((IFileEditorInput) getActiveEditor().getEditorInput())
				.getFile();
	}

	public IProject getCurrentProject() {
		return getCurrentFile().getProject().getProject();
	}

	@SuppressWarnings("unchecked")
	public void updateData(Object[] objects) {
		this.tv.setInput(objects);
	}

	public DroolData[] fetchData() {
		Set ds = getDatasetFromTableData(this.table);
		return (DroolData[]) ds.toArray(new DroolData[ds.size()]);
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

	private static final String _path = "path";

	private class FCEditorModifier implements ICellModifier {

		public boolean canModify(Object element, String property) {
			return true;
		}

		public Object getValue(Object element, String property) {
			DroolData fc = (DroolData) element;
			if (_path.equals(property)) {
				return fc.getPath();
			}
			return null;
		}

		public void modify(Object element, String property, Object value) {
			if (element instanceof Item) {
				element = ((Item) element).getData();
			}
			DroolData fc = (DroolData) element;
			if (_path.equals(property)) {
				fc.setPath((String) value);
			}

			tv.update(fc, null);
		}

	}

	private class DFLabelProvider implements ITableLabelProvider {
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			DroolData fc = (DroolData) element;
			switch (columnIndex) {
			case 0:
				return fc.getPath();

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
