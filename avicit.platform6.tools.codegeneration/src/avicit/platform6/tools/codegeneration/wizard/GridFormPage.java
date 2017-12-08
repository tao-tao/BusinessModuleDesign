package avicit.platform6.tools.codegeneration.wizard;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.ui.IJavaElementSearchConstants;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.dialogs.SelectionDialog;

import avicit.platform6.tools.codegeneration.core.Resource;
import avicit.platform6.tools.codegeneration.core.common.TableUtil;
import avicit.platform6.tools.codegeneration.core.entity.FieldInfo;
import avicit.platform6.tools.codegeneration.core.entity.TableInfo;

public class GridFormPage extends BaseWizardPage implements IErrorHandler {
	private Text javaDataClassText;
	Button hbmBrowserButton;
	Button queryBroserButton;
	Composite composite_2;
	boolean firstFlag = true;
	
	TreeComposite treeComposite;
	
	ConcurrentHashMap<Integer,Boolean> loadMap = new ConcurrentHashMap<Integer,Boolean>();
	TreeViewer treeViewer;
	Menu me;
	Composite parenttemp;
	TabFolder folder1;
	TabFolder folder2;
	TabItem tabItem;
	TabItem tabItem1;
	TabItem tabItem2;
	private Tree tree;
	private Button btn_persist;
	private String javaDataClass;

	// private BooleanFieldEditor startLowerCase;
	protected IJavaProject project;
	private IFolder folder;
//	private ButtonFieldCpt buttonCpt;
	public ListDataFieldCpt columnCptForm;
	public ListDataFieldCpt columnCptTable;
	public ListDataFieldCpt columnCptSerach;
	
	public ListDataFieldCpt columnCptFormSub;
	public ListDataFieldCpt columnCptTableSub;
	public ListDataFieldCpt columnCptSerachSub;
	private Button btn_paging;
	private Text text_sql;
	private Button btn_hql;
	private Text text_order;

	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
	public GridFormPage() {
		super("wizardPage");
		setTitle("列表建模");
		setDescription("该向导将创建列表.");
		this.folder = folder;
//		project = JavaCore.create(folder.getProject());
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		
		final Composite parent_ = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 4;
		parent_.setLayout(gridLayout);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		parent_.setLayoutData(data);
		setControl(parent_);
		initTree(parent_);
		this.parenttemp = parent_;

		
		folder1 = new TabFolder(parent_, SWT.NONE);
		folder1.setLayoutData(new GridData(GridData.FILL_BOTH));
		tabItem = new TabItem(folder1, SWT.NONE);
		tabItem.setText("表单字段");
			columnCptForm = new ListDataFieldCpt(folder1, SWT.NONE);
			columnCptForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			tabItem.setControl(columnCptForm);
			
			
			
			 tabItem1 = new TabItem(folder1, SWT.NONE);
			tabItem1.setText("列表详细信息");
			
			columnCptTable = new ListDataFieldCpt(folder1, SWT.NONE);
			GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
			columnCptTable.setLayoutData(gd);
//			tabItem1.setControl(columnCptTable);
			
			 tabItem2 = new TabItem(folder1, SWT.NONE);
			tabItem2.setText("查询条件");

			columnCptSerach = new ListDataFieldCpt(folder1, SWT.NONE);
			GridData gd2 = new GridData(SWT.FILL, SWT.FILL, true, true);
			columnCptSerach.setLayoutData(gd2);
//			tabItem2.setControl(columnCptSerach);
		
	
		
	}

	private void initMainTabItem(Composite parent_) {

		if(columnCptForm==null){
			columnCptForm = new ListDataFieldCpt(folder1, SWT.NONE);
		}
		columnCptForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		tabItem.setControl(columnCptForm);

		
		if(columnCptTable==null){
			columnCptTable = new ListDataFieldCpt(folder1, SWT.NONE);
		}
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		columnCptTable.setLayoutData(gd);
		tabItem1.setControl(columnCptTable);
		

		if(columnCptSerach==null){
			columnCptSerach = new ListDataFieldCpt(folder1, SWT.NONE);
		}
		GridData gd2 = new GridData(SWT.FILL, SWT.FILL, true, true);
		columnCptSerach.setLayoutData(gd2);
		tabItem2.setControl(columnCptSerach);
		
	}

	private void initSubTabItem(Composite parent_) {

		if(columnCptFormSub==null){
			columnCptFormSub = new ListDataFieldCpt(folder1, SWT.NONE);
		}
		columnCptFormSub.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		tabItem.setControl(columnCptFormSub);

		
		if(columnCptTableSub==null){
			columnCptTableSub = new ListDataFieldCpt(folder1, SWT.NONE);
		}
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		columnCptTableSub.setLayoutData(gd);
		tabItem1.setControl(columnCptTableSub);
		

		if(columnCptSerachSub==null){
			columnCptSerachSub = new ListDataFieldCpt(folder1, SWT.NONE);
		}
		GridData gd2 = new GridData(SWT.FILL, SWT.FILL, true, true);
		columnCptSerachSub.setLayoutData(gd2);
		tabItem2.setControl(columnCptSerachSub);
	}
	
	protected void initTree(Composite parent_){
		
		Composite testcomposite = new Composite(parent_, SWT.BORDER);
		GridLayout gl = new GridLayout();
		gl.numColumns = 1;
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		FillLayout layout = new FillLayout();
		testcomposite.setLayoutData(data);
		testcomposite.setLayout(layout);
		
		treeViewer = new TreeViewer(testcomposite, SWT.BORDER);
		treeViewer.setAutoExpandLevel(0);
		tree = treeViewer.getTree();

		org.eclipse.jface.action.MenuManager ma = new org.eclipse.jface.action.MenuManager();
		
		ma.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager arg0) {
				arg0.removeAll();
				TreeItem[] items = tree.getSelection();
				if (items != null && items.length > 0) {
					Object o = items[0].getData();
					
				}
			}
		});
		
		me = ma.createContextMenu(tree);
		tree.setMenu(me);

		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				onTreeSelected();
			}
		});

	}
	
	
	protected void onTreeSelected() {
		TableDataConfigObject selectTreeData = null;
		TreeItem[] select = tree.getSelection();
		if (select != null && select.length > 0) {
			TreeItem item = select[0];
			Object o = item.getData();
			if (o instanceof TableDataConfigObject) {
				selectTreeData = (TableDataConfigObject) o;
				if(selectTreeData.getName().equalsIgnoreCase(getRealWizard().getMainTableNames().get(0)))
				{
//					if(getRealWizard().templeteSelectPage.getIsMain())
					initMainTabItem(this.parenttemp);
					this.updateColsMain(selectTreeData.getName());
				}
//					else{
//					initSubTabItem(this.parenttemp);
//					this.updateColsSub(selectTreeData.getName());
//				}
				
			}

		}
		
		setPageComplete(canFlipToNextPage());

	}
	public void updateTreeData(){
		treeViewer.setContentProvider(new TableContentProvider(getRealWizard()));
		treeViewer.setLabelProvider(new TableLableProvider(getRealWizard()));
		treeViewer.setInput(new Object());//
	}
	public void updateColsMain(String tableName) {

		List fields = new ArrayList();
		TableUtil tableUtil = new TableUtil();
		
		TableInfo tableInfo = null;
		try {
			tableInfo = tableUtil.getTableInfo(tableName);
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<FieldInfo> tablefields = tableInfo.getFields();
		if (tablefields != null ) {
			try {
				
				for (FieldInfo info:tablefields) {
					ListDataField field = new ListDataField();
					field.setDataIndex(info.getFieldName());
					field.setMapping(info.getFieldTypeName());
					field.setHeader(info.getComment());
					field.setHiddenArray(new String[]{"false","true"});
					field.setVersion(0);
					fields.add(field);
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

		if(columnCptForm!=null){
			columnCptForm.updateData((ListDataField[]) fields
					.toArray(new ListDataField[fields.size()]));
			columnCptTable.updateData((ListDataField[]) fields
					.toArray(new ListDataField[fields.size()]));
			columnCptSerach.updateData((ListDataField[]) fields
					.toArray(new ListDataField[fields.size()]));
			
		}
		
		
		
	

	}
	public void updateColsSub(String tableName) {

		List fields = new ArrayList();
		TableUtil tableUtil = new TableUtil();
		
		TableInfo tableInfo = null;
		try {
			tableInfo = tableUtil.getTableInfo(tableName);
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<FieldInfo> tablefields = tableInfo.getFields();
		if (tablefields != null ) {
			try {
				
				for (FieldInfo info:tablefields) {
					ListDataField field = new ListDataField();
					field.setDataIndex(info.getFieldName());
					field.setMapping(info.getFieldTypeName());
					field.setHeader(info.getComment());
					field.setHiddenArray(new String[]{"false","true"});
					field.setVersion(0);
					fields.add(field);
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

		
		if(columnCptFormSub!=null){
			columnCptFormSub.updateData((ListDataField[]) fields
					.toArray(new ListDataField[fields.size()]));
			columnCptTableSub.updateData((ListDataField[]) fields
					.toArray(new ListDataField[fields.size()]));
			columnCptSerachSub.updateData((ListDataField[]) fields
					.toArray(new ListDataField[fields.size()]));
		}
		
		
	

	}
	
	private void loadTable(List fields) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Ensures that both text fields are set.
	 */
	public void dialogChanged() {
		updateStatus(null);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public String getFromDB() {
		return Boolean.toString(this.btn_persist.getSelection());
	}

	public String getPaging() {
		return Boolean.toString(btn_paging.getSelection());
	}

	public String getHQL() {
		return Boolean.toString(btn_hql.getSelection());
	}
	
	public String getJavaDataClass() {
		return javaDataClass;
	}

	public String getSQL() {
		return this.text_sql.getText();
	}

	public String getOrder() {
		return this.text_order.getText();
	}
	
	public void onError(String message, Throwable exception) {
		if (null == message && null != exception)
			message = exception.getMessage();
		if (null == message && null != exception)
			message = exception.getClass().getName();
		MessageDialog.openError(getShell(), "An error has occured", message);
	}


	public List fetchCols() {
		List buttons = new ArrayList();
		buttons.addAll(Arrays.asList(columnCptForm.fetchData()));
		return buttons;
	}
	
	public List fetchButtons() {
		List buttons = new ArrayList();
		buttons.addAll(Arrays.asList());
		return buttons;
	}
}