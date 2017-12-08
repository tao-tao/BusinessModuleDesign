package avicit.platform6.tools.codegeneration.wizard;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Driver;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.corext.util.JavaModelUtil;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.dialogs.TextFieldNavigationHandler;
import org.eclipse.jdt.internal.ui.refactoring.contentassist.ControlContentAssistHelper;
import org.eclipse.jdt.internal.ui.refactoring.contentassist.JavaPackageCompletionProcessor;
import org.eclipse.jdt.internal.ui.wizards.NewWizardMessages;
import org.eclipse.jdt.internal.ui.wizards.TypedViewerFilter;
import org.eclipse.jdt.internal.ui.wizards.buildpaths.FolderSelectionDialog;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IDialogFieldListener;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IStringButtonAdapter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.LayoutUtil;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringButtonStatusDialogField;
import org.eclipse.jdt.ui.IJavaElementSearchConstants;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.dialogs.SelectionDialog;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;



//import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;

import avicit.platform6.tools.codegeneration.core.DBConfig;
import avicit.platform6.tools.codegeneration.core.Resource;
import avicit.platform6.tools.codegeneration.core.common.TableUtil;
import avicit.platform6.tools.codegeneration.core.entity.FieldInfo;
import avicit.platform6.tools.codegeneration.core.entity.TableInfo;
import avicit.platform6.tools.codegeneration.define.ConstDefine;
import avicit.platform6.tools.codegeneration.log.AvicitLogger;
import avicit.platform6.tools.codegeneration.util.SvnFilenameFilter;
import avicit.platform6.tools.codegeneration.util.ZipUtil;

/**
 * 
 * 作者：dingrc 邮箱：dingrc@avicit.com 创建时间：2012-12-10
 * 
 * 类说明：选择业务模块的生成路径和生成模板 修改记录：
 */
@SuppressWarnings("restriction")
public class TempleteSelectPage extends BaseWizardPage {
	 

	public static String CREATE_DEFAULT_PATH = "avicit.platform6.modules.new";
	// 选择包控件
	
	public String webPathJsp ="";
	
	
	protected StringButtonStatusDialogField fPackageDialogField;

	StringButtonStatusDialogField getWebPath(){
		return webPath;
	}

	private String pathConstant;
	
	public void setPathConstant(String pc){
		this.pathConstant=pc;
	}
	
	public String getPathConstant(){
		return pathConstant;
	}
	
	private StringButtonStatusDialogField webPath;
	// 代码提示
	protected JavaPackageCompletionProcessor packageAssist;
	Text textTepletePath;
	/**
	 * 选择自定义模版
	 */
	private Button btnDefineCheck;
	private Button btnSelectTemplete;
	private Button btnExpMod1;
	
	Button btnLoadTable;
	
	private Combo comboTemplete;
	
	Group group_Templete;
	
	Label lbl_modeType;
	
	Composite compositeSubTable;
	
	Composite compositeTreeTable;
	private CodeGenerationWizard codeGenerationWizard;

	
	

	TreeComposite treeComposite;
	TreeViewer treeViewer;
	Menu me;
	private Tree tree;
	
	String ModelName = "";
	private boolean isJsp;
	/**
	 * 是否为主子表模版
	 */
	private boolean isMain;

	
	
	/**
	 * 是否为主子表模版
	 */
	private boolean isTree;
	
	
	public boolean isTree() {
		return isTree;
	}

	public void setTree(boolean isTree) {
		this.isTree = isTree;
	}

	/**
	 * 是否为主子表模版
	 * 
	 * @return
	 */
	public boolean getIsMain() {
		return isMain;
	}
	/**
	 * 是否为单表流程模板
	 */
	private boolean isSingleFlowTemplate;



	public boolean isSingleFlowTemplate() {
		return isSingleFlowTemplate;
	}

	public void setSingleFlowTemplate(boolean isSingleFlowTemplate) {
		this.isSingleFlowTemplate = isSingleFlowTemplate;
	}

	/**
	 * 子表信息group
	 */
	private Group grpSubTable;
	
	private Group grpSubTabletree;
	/**
	 * 子表过滤条件
	 */
	private Text txtFilter;

	/**
	 * 展现子表列表
	 */
	private TableViewer treeviewer;

	/**
	 * 子表选择列表
	 */
	private List<String> subTableNames;

	/**
	 * 主表字段
	 */
	private Combo comboMainField;
	/**
	 * 子表字段
	 */
	private Combo comboSubField;
	
	/**
	 * 
	 */
	private Combo comboMainFieldtree;
	
	
	/**
	 * 
	 */
	private Combo comboMainFieldtreeDisplay;
	/**
	 * 
	 */
	private Combo comboSubFieldtree;

	protected TempleteSelectPage(String pageName) {
		super(pageName);
		setTitle("模板和业务模块路径选择");
		setDescription("请选择生成模板和业务模块的生成路径");

	}

	public void createControl(Composite parent) {
		
		
		/**父界面***************************************************************************************/
		final Composite parent_ = new Composite(parent, SWT.NONE);
		setControl(parent_);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		parent_.setLayout(gridLayout);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		parent_.setLayoutData(data);
		/**父界面***************************************************************************************/
		/**树***************************************************************************************/
		initTree(parent_);
		/**树***************************************************************************************/
		Composite container = new Composite(parent_, SWT.NULL);
		
		container.setLayout(new FormLayout());
		/**选择模版***************************************************************************************/
		Composite compositeTemplete = new Composite(container, SWT.NONE);
		FormData fd_compositeTemplete = new FormData();
		fd_compositeTemplete.right = new FormAttachment(0, 550);
		fd_compositeTemplete.top = new FormAttachment(0, 3);
		fd_compositeTemplete.left = new FormAttachment(0, 3);
		compositeTemplete.setLayoutData(fd_compositeTemplete);
		compositeTemplete.setLayout(new FillLayout(SWT.HORIZONTAL));
		/**
		 * 树模板
		 */
		initTreeTable(container,compositeTemplete);
		
		//子表信息
		compositeSubTable = new Composite(container, SWT.NONE);
		compositeSubTable.setVisible(false);
		FormData fd_compositeSubTable = new FormData();
		fd_compositeSubTable.bottom = new FormAttachment(compositeTemplete,260, SWT.BOTTOM);
		fd_compositeSubTable.top = new FormAttachment(compositeTemplete,0, SWT.BOTTOM);
		fd_compositeSubTable.right = new FormAttachment(compositeTemplete,0, SWT.RIGHT);
		fd_compositeSubTable.left = new FormAttachment(compositeTemplete,0, SWT.LEFT);
		compositeSubTable.setLayoutData(fd_compositeSubTable);
		compositeSubTable.setLayout(new FillLayout(SWT.HORIZONTAL));
		//文件创建路径
		Composite compositeCreatePath = new Composite(container, SWT.NONE);
		FormData fd_compositeCreatePath = new FormData();
		fd_compositeCreatePath.bottom = new FormAttachment(compositeSubTable,100, SWT.BOTTOM);
		fd_compositeCreatePath.right = new FormAttachment(compositeSubTable,0, SWT.RIGHT);
		fd_compositeCreatePath.top = new FormAttachment(compositeSubTable,0, SWT.BOTTOM);
		fd_compositeCreatePath.left = new FormAttachment(compositeSubTable,0, SWT.LEFT);
		compositeCreatePath.setLayoutData(fd_compositeCreatePath);
		compositeCreatePath.setLayout(new FillLayout(SWT.HORIZONTAL));

		Group grpSheng = new Group(compositeCreatePath, SWT.NONE);
		grpSheng.setLayout(new GridLayout(4, false));
		grpSheng.setText("选择生成目录");
		createPackageControls(grpSheng, "", "业务模块路径：", 4,"请选择或输入业务模块的存储路径");
		
		createWebFolderControl(grpSheng);
		

		group_Templete = new Group(compositeTemplete, SWT.NONE);
		group_Templete.setText("模板");
		group_Templete.setLayout(null);
//		group_Templete.setVisible(false);

		// add by zl*************************************************
		

		grpSubTable = new Group(compositeSubTable, SWT.None);
		grpSubTable.setText("子表信息");
		grpSubTable.setEnabled(false);

		btnLoadTable = new Button(grpSubTable, SWT.TOGGLE);
		btnLoadTable.setBounds(424, 16, 80, 25);
		btnLoadTable.setText("载入子表");
		btnLoadTable.setEnabled(false);
		btnLoadTable.setToolTipText("点击按钮加载数据库表");
		btnLoadTable.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				CodeGenerationWizard wizard = getRealWizard();

				LoadSubTable();

				if (wizard.getMainTableNames() != null
						&& wizard.getAllTable().size() > 0) {
					subTableNames.remove(wizard.getMainTableNames().get(0));
				}
				treeviewer.setInput(subTableNames);
			}
		});
		final Label label = new Label(grpSubTable, SWT.NULL);
		label.setBounds(10, 20, 60, 25);
		label.setText("过滤条件：");

		txtFilter = new Text(grpSubTable, SWT.BORDER | SWT.SINGLE);
		txtFilter.setBounds(95, 20, 323, 20);
		txtFilter.setEnabled(false);
		txtFilter.setToolTipText("在完成加载表操作之后，可通过本输入框对结果进行过滤！");
		txtFilter.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent arg0) {
				List<String> filterdList = new ArrayList<String>();
				if (subTableNames == null)
					return;
				for (String table : subTableNames) {
					if (table.toLowerCase().indexOf(
							txtFilter.getText().toLowerCase()) > -1) {
						filterdList.add(table);
					}
				}
				treeviewer.setInput(filterdList);

			}
		});

		final Label label_1 = new Label(grpSubTable, SWT.NULL);
		label_1.setBounds(10, 52, 66, 22);
		label_1.setText(BusinessObjectConfigPage.getResourceBundle().getString(
				Resource.TABLESSUB));

		treeviewer = new TableViewer(grpSubTable, SWT.FULL_SELECTION
				| SWT.SINGLE);
		Table table_1 = treeviewer.getTable();
		table_1.setBounds(95, 45, 400, 122);
		table_1.setHeaderVisible(true);
		table_1.setLinesVisible(true);
		TableColumn column = new TableColumn(table_1, SWT.NONE);
		column.setText("业务子表名称");
		column.setWidth(380);
		treeviewer.setContentProvider(new IStructuredContentProvider() {
			@Override
			public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
				// TODO Auto-generated method stub
			}

			@Override
			public void dispose() {
				// TODO Auto-generated method stub
			}

			@Override
			public Object[] getElements(Object arg0) {
				if (arg0 instanceof List) {
					List list = (List) arg0;
					return list.toArray();
				}
				return new Object[0];
			}
		});
		treeviewer.setLabelProvider(new ITableLabelProvider() {
			public Image getColumnImage(Object element, int columnIndex) {
				return null;
			}

			public String getColumnText(Object element, int columnIndex) {
				if (element instanceof String) {
					if (columnIndex == 0)
						return (String) element;
				}
				return null;
			}

			public void addListener(ILabelProviderListener listener) {
			}

			public void dispose() {
			}

			public boolean isLabelProperty(Object element, String property) {

				return false;
			}

			public void removeListener(ILabelProviderListener listener) {
			}
		});
		treeviewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				setPageComplete(canFlipToNextPage());

			}
		});

		final Label label_2 = new Label(grpSubTable, SWT.NULL);
		label_2.setBounds(10, 182, 80, 34);
		label_2.setText(BusinessObjectConfigPage.getResourceBundle().getString(
				Resource.RELATIONMAIN));

		comboMainField = new Combo(grpSubTable, SWT.READ_ONLY);
		comboMainField.setBounds(110, 180, 150, 25);
		comboMainField.setEnabled(false);
		comboMainField.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				getRealWizard().setMainRelationField(comboMainField.getText());
				getRealWizard().getDoCreatPage().initListShowConfInfo();
			}
		});

		final Label label_3 = new Label(grpSubTable, SWT.NULL);
		label_3.setBounds(290, 182, 80, 34);
		label_3.setText(BusinessObjectConfigPage.getResourceBundle().getString(
				Resource.RELATIONSUB));

		comboSubField = new Combo(grpSubTable, SWT.READ_ONLY);
		comboSubField.setBounds(380, 180, 150, 25);
		comboSubField.setEnabled(false);
		comboSubField.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				getRealWizard().setSubRelationField(comboSubField.getText());
				getRealWizard().getDoCreatPage().initListShowConfInfo();
			}
		});

		// ***********************
		lbl_modeType = new Label(group_Templete, SWT.NONE);
		lbl_modeType.setBounds(10, 25, 1000, 17);
		lbl_modeType.setText("模板类型: "+ModelName);


		btnDefineCheck = new Button(group_Templete, SWT.CHECK);
		btnDefineCheck.setText("选择自定义模板");
		btnDefineCheck.setBounds(8, 52, 105, 17);
		btnDefineCheck.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (btnDefineCheck.getSelection()) {
					textTepletePath.setEnabled(true);
					btnSelectTemplete.setEnabled(true);
//					comboTemplete.setEnabled(false);
				} else {
					textTepletePath.setEnabled(false);
					btnSelectTemplete.setEnabled(false);
//					comboTemplete.setEnabled(true);
				}
			}
		});

		Label lblTempleteFile = new Label(group_Templete, SWT.NONE);
		lblTempleteFile.setBounds(10, 79, 35, 17);
		lblTempleteFile.setText(" 模 版:");

		textTepletePath = new Text(group_Templete, SWT.BORDER);
		textTepletePath.setBounds(51, 76, 447, 23);

		btnSelectTemplete = new Button(group_Templete, SWT.NONE);
		btnSelectTemplete.setBounds(504, 74, 36, 27);
		btnSelectTemplete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// 新建文件夹（目录）对话框
				DirectoryDialog folderdlg = new DirectoryDialog(new Shell());
				// 设置文件对话框的标题
				folderdlg.setText("文件夹选择");
				// 设置初始路径
				folderdlg.setFilterPath("SystemDrive");
				// 设置对话框提示文本信息
				folderdlg.setMessage("请选择相应的文件夹");
				// 打开文件对话框，返回选中文件夹目录
				String selecteddir = folderdlg.open();
				if (selecteddir == null) {
					return;
				} else {
					textTepletePath.setText(selecteddir);
				}

			}
		});

		btnSelectTemplete.setText("选择");
		textTepletePath.setEnabled(false);
		btnSelectTemplete.setEnabled(false);

		codeGenerationWizard = this.getRealWizard();
	}
	
	/**
	 * 
	 * 方法名称    :initTreeTable
	 * 功能描述    :
	@see
				   :
	@see
	 * 逻辑描述    :
	 * @param   :无
	 * @return  :void
	 * @throws  :无
	 * @since   :Ver 1.00
	 */
	private void initTreeTable(Composite container, Composite compositeTemplete) {
		compositeTreeTable = new Composite(container, SWT.NONE);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 4;
		compositeTreeTable.setLayout(gridLayout);
		compositeTreeTable.setVisible(false);
		
		grpSubTabletree = new Group(compositeTreeTable, SWT.None);
		grpSubTabletree.setText("树模板设置信息");
		grpSubTabletree.setEnabled(true);

		final Label label_1 = new Label(grpSubTabletree, SWT.NULL);
		label_1.setBounds(10, 120, 80, 34);
		label_1.setText("显示字段");
		
		comboMainFieldtreeDisplay = new Combo(grpSubTabletree, SWT.READ_ONLY);
		comboMainFieldtreeDisplay.setBounds(90, 120, 150, 25);
		comboMainFieldtreeDisplay.setEnabled(true);
		comboMainFieldtreeDisplay.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				getRealWizard().setTreeDisplayField(comboMainFieldtreeDisplay.getText());
				getRealWizard().getDoCreatPage().initListShowConfInfo();
				setPageComplete(canFlipToNextPage());
				
			}
		});
		

		final Label label_2 = new Label(grpSubTabletree, SWT.NULL);
		label_2.setBounds(10, 182, 80, 34);
		label_2.setText("主键ID");
		
		comboMainFieldtree = new Combo(grpSubTabletree, SWT.READ_ONLY);
		comboMainFieldtree.setBounds(90, 180, 150, 25);
		comboMainFieldtree.setEnabled(true);
		comboMainFieldtree.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				getRealWizard().setMainRelationField(comboMainFieldtree.getText());
				getRealWizard().getDoCreatPage().initListShowConfInfo();
				setPageComplete(canFlipToNextPage());
				
			}
		});
		
		

		final Label label_3 = new Label(grpSubTabletree, SWT.NULL);
		label_3.setBounds(290, 182, 80, 34);
		label_3.setText("关联父ID");

		comboSubFieldtree = new Combo(grpSubTabletree, SWT.READ_ONLY);
		comboSubFieldtree.setBounds(390, 180, 150, 25);
		comboSubFieldtree.setEnabled(true);
		comboSubFieldtree.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				getRealWizard().setTreeRelationField(comboSubFieldtree.getText());
				getRealWizard().getDoCreatPage().initListShowConfInfo();
				setPageComplete(canFlipToNextPage());
			}
		});
		
	}

	protected void initTree(Composite parent_){
		
		Composite testcomposite = new Composite(parent_, SWT.BORDER);
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		FillLayout layout = new FillLayout();
		testcomposite.setLayoutData(data);
		testcomposite.setLayout(layout);
		
		treeViewer = new TreeViewer(testcomposite, SWT.BORDER);
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
				TreeDataConfigObject selectTreeData = null;
				TreeItem[] select = tree.getSelection();
				if (select != null && select.length > 0) {
					TreeItem item = select[0];
					Object o = item.getData();
					if (o instanceof TreeDataConfigObject) {
						selectTreeData = (TreeDataConfigObject) o;
					}

				}
					/**
					 * 
					 * 
					 * 
					 */
				if (selectTreeData.getCode().equalsIgnoreCase(
						Resource.jspMainChild)) {
//					createControl(grpSubTable);
					isMain = true;
					isTree = false;
					compositeTreeTable.setVisible(false);
					compositeSubTable.setVisible(true);
					grpSubTable.setEnabled(true);
					btnLoadTable.setEnabled(true);
					comboMainField.setEnabled(true);
					comboSubField.setEnabled(true);
					txtFilter.setEnabled(true);
					ModelName = "主子表";
					LoadFieldToCombo();
					textTepletePath.setText(getRealWizard().getTempletPath()+File.separator+selectTreeData.getCode());
					/**
					 * 
					 * 
					 * 
					 */
				} else if (selectTreeData.getCode().equalsIgnoreCase(
						Resource.singleTabletree)) {

					compositeTreeTable.setVisible(true);
					isMain = false;
					isTree = true;
					compositeSubTable.setVisible(false);
					grpSubTable.setEnabled(false);
					btnLoadTable.setEnabled(false);
					comboMainField.setEnabled(false);
					comboMainField.setItems(new String[] {});
					comboSubField.setEnabled(false);
					comboSubField.setItems(new String[] {});
					txtFilter.setEnabled(false);
					txtFilter.setText("");
					treeviewer.setInput(null);
					ModelName = "单表树";
					textTepletePath.setText(getRealWizard().getTempletPath()+File.separator+selectTreeData.getCode());
					/**
					 * 
					 * 
					 * 
					 */
				} else if(!selectTreeData.getCode().equals("parent")){
					isTree = false;
					compositeTreeTable.setVisible(false);
					isMain = false;
					compositeSubTable.setVisible(false);
					grpSubTable.setEnabled(false);
					btnLoadTable.setEnabled(false);
					comboMainField.setEnabled(false);
					comboMainField.setItems(new String[] {});
					comboSubField.setEnabled(false);
					comboSubField.setItems(new String[] {});
					txtFilter.setEnabled(false);
					txtFilter.setText("");
					treeviewer.setInput(null);
					ModelName = "单表";
					textTepletePath.setText(getRealWizard().getTempletPath()+File.separator+selectTreeData.getCode());

				}
				
				setPageComplete(canFlipToNextPage());

			}
		});
		
		treeViewer.setContentProvider(new TreeContentProvider());
		treeViewer.setLabelProvider(new TreeLableProvider());
		treeViewer.setInput(new Object());//

	}
	/**
	 * 导入子表 addzl
	 */
	private void LoadSubTable() {
		CodeGenerationWizard wizard = getRealWizard();
		Properties props = wizard.getProps();
		this.getRealWizard().getTempletPath();
		try {
			Class driverClass = Class.forName(props.getProperty("driver"));
			Driver dirver = (Driver) driverClass.newInstance();
			DBConfig dBConfig = new DBConfig();
			// oracle 3
			int dbtype = 3;
			dBConfig.initDriverInfo(dirver, props.getProperty("url"),
					props.getProperty("driver"), props.getProperty("user"),
					props.getProperty("SchemaORCatlog"),
					props.getProperty("password"), dbtype);
			dBConfig.testConnection();
			java.util.List<String> list = dBConfig.getAllTable(
					props.getProperty("SchemaORCatlog"),
					props.getProperty("databaseName"));
			if (list != null && list.size() > 0) {
				subTableNames = null;
				subTableNames = list;
			} else {
				AvicitLogger.openMsg(null,
						"未查到任何表,请确认数据库名与Scheme/Catlog填写是否正确,或者此数据库下没有任何表。");
			}

		} catch (Exception e) {
			AvicitLogger.openError(e);
		}

	}

	// add zl

	void Refresh() {
		if (isMain) {
			this.LoadFieldToCombo();
			treeviewer.setInput(null);
			comboSubField.setItems(new String[] {});
		}else{
			this.LoadFieldToCombo();
		}
	}

	private void LoadFieldToCombo() {
		CodeGenerationWizard wizard = getRealWizard();
		String tablename = wizard.getMainTableNames().get(0).toString();
		this.LoadFieldToCombo(tablename, comboMainField);
	}

	/**
	 * 根据选择的数据表中的字段导入到combo里面
	 */
	private void LoadFieldToCombo(String tableName, Combo combo) {
		TableUtil tableUtil = new TableUtil();
		try {
			TableInfo tableInfo = tableUtil.getTableInfo(tableName);
			List<FieldInfo> fieldinfos = tableInfo.getFields();
			List<String> fields = new ArrayList<String>();// 读取 的字段
			// 获得主键
			if (tableInfo.getPrimaryField() != null) {
				fields.add(tableInfo.getPrimaryField().getFieldName());
			}
			int length = tableInfo.getFields().size();
			// 非主键字段
			for (int i = 0; i < length; i++) {
				fields.add(fieldinfos.get(i).getFieldName());
			}
			String[] items = new String[fields.size()];
			fields.toArray(items);
			combo.setItems(items);
			comboMainFieldtree.setItems(items);
			comboSubFieldtree.setItems(items);
			comboMainFieldtreeDisplay.setItems(items);
			if (items.length > 0) {
				combo.select(0);
			}

		} catch (Exception e) {
			AvicitLogger.openError(e.getMessage(), e);
			AvicitLogger.logError(e);
			e.printStackTrace();
		}

	}

	/*
	 * @Override public IWizardPage getNextPage() { IWizardPage next =
	 * super.getNextPage(); if (next instanceof BaseWizardPage) { BaseWizardPage
	 * base = (BaseWizardPage) next; if (base.packageAssist != null) {
	 * base.packageAssist.setPackageFragmentRoot(getPackageFragmentRoot()); } }
	 * if (packageAssist != null) {
	 * packageAssist.setPackageFragmentRoot(getPackageFragmentRoot()); } return
	 * super.getNextPage(); }
	 */

	protected void createPackageControls(Composite composite, String pkg,String lableText, int column, String tooltip) {
		TypeFieldsAdapter adapter = new TypeFieldsAdapter();
		fPackageDialogField = new StringButtonStatusDialogField(adapter);
		fPackageDialogField.setDialogFieldListener(adapter);
		fPackageDialogField.setLabelText(lableText);
		fPackageDialogField.setButtonLabel("请选择");
		fPackageDialogField.setStatusWidthHint(NewWizardMessages.NewTypeWizardPage_default);
		fPackageDialogField.setEnabled(false);
		fPackageDialogField.doFillIntoGrid(composite, column);
		fPackageDialogField.setText(CREATE_DEFAULT_PATH);
		final Text text = fPackageDialogField.getTextControl(null);
		
		
		text.setText(pkg);
		text.setToolTipText(tooltip);
		LayoutUtil.setWidthHint(text, getMaxFieldWidth());
		LayoutUtil.setHorizontalGrabbing(text);
		packageAssist = new JavaPackageCompletionProcessor();
		ControlContentAssistHelper.createTextContentAssistant(text,packageAssist);
		TextFieldNavigationHandler.install(text);
		text.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if(isJsp){
					//String aa = text.getText().replaceAll("\\.", "/");
					webPath.setText(pathConstant+ text.getText().replaceAll("\\.", "/"));
				}
				setPageComplete(canFlipToNextPage());
			}
		});
		text.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {

			}
		});
		text.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				if (packageAssist != null) {
					packageAssist.setPackageFragmentRoot(getPackageFragmentRoot());
				}
			}
		});
	}
	
	/**
	 * 生成jsp文件选择模版
	 * @param composite
	 */
	private void createWebFolderControl(Composite composite) {
		TypeFieldsAdapter adapter = new TypeFieldsAdapter();
		webPath = new StringButtonStatusDialogField(adapter);
		webPath.setDialogFieldListener(adapter);
		webPath.setLabelText("jsp文件路径：");
		webPath.setButtonLabel("请选择");
		webPath.setStatusWidthHint(NewWizardMessages.NewTypeWizardPage_default);
		webPath.setEnabled(false);
		webPath.doFillIntoGrid(composite, 4);
		
		final Text text = webPath.getTextControl(null);
		
		text.setText(getRealWizard().isManve()?"webapp":"web");
		//2015-03-13
		text.setText(getRealWizard().isManve()?"webapp":"WebRoot");
		text.setToolTipText("请选择或输入代码生成器最终生成的jsp文件路径");
		LayoutUtil.setWidthHint(text, getMaxFieldWidth());
		LayoutUtil.setHorizontalGrabbing(text);
		TextFieldNavigationHandler.install(text);
		text.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				webPath.setEnabled(false);
				setPageComplete(canFlipToNextPage());
			}
		});
	}

	/** ***************************************************** */
	private class TypeFieldsAdapter implements IStringButtonAdapter,IDialogFieldListener {
		// -------- IStringButtonAdapter
		public void changeControlPressed(DialogField field) {
			typePageChangeControlPressed(field);
		}

		// -------- IDialogFieldListener
		public void dialogFieldChanged(DialogField field) {
			typePageDialogFieldChanged(field);
		}
	}

	private void typePageChangeControlPressed(DialogField field) {
		if (field == fPackageDialogField) {
			IPackageFragment pack = choosePackage();
			if (pack != null) {
				fPackageDialogField.setText(pack.getElementName());
			}
			return;
		}
		ILabelProvider lp = new WorkbenchLabelProvider();
		ITreeContentProvider cp = new WorkbenchContentProvider();
		Class[] acceptedClasses = new Class[] { IFolder.class };
		ViewerFilter filter = new TypedViewerFilter(acceptedClasses);
		FolderSelectionDialog dialog = new FolderSelectionDialog(getShell(), lp, cp);
		dialog.setTitle(NewWizardMessages.BuildPathsBlock_ChooseOutputFolderDialog_title);
		dialog.setMessage(NewWizardMessages.BuildPathsBlock_ChooseOutputFolderDialog_description);
		dialog.addFilter(filter);
		dialog.setInput(getProject());
		dialog.setAllowMultiple(false);
		dialog.setDoubleClickSelects(true);
		if (dialog.open() == Window.OK) {
			Object obj = dialog.getFirstResult();
			if (obj instanceof IFolder) {
				IFolder f = (IFolder) obj;
				webPath.setText(f.getProjectRelativePath().toString());
			}
		}
	}

	protected IPackageFragmentRoot getPackageFragmentRoot() {
		IJavaProject javaPro = JavaCore.create(getProject());
		IPackageFragmentRoot initRoot = null;
		if (javaPro != null) {
			initRoot = JavaModelUtil.getPackageFragmentRoot(javaPro);
			try {
				if (initRoot == null
						|| initRoot.getKind() != IPackageFragmentRoot.K_SOURCE) {
					if (javaPro.exists()) {
						IPackageFragmentRoot[] roots = javaPro
								.getPackageFragmentRoots();
						for (int i = 0; i < roots.length; i++) {
							if (roots[i].getKind() == IPackageFragmentRoot.K_SOURCE) {
								initRoot = roots[i];
								break;
							}
						}
					}
					if (initRoot == null) {
						initRoot = javaPro.getPackageFragmentRoot(javaPro
								.getResource());
					}
				}
			} catch (JavaModelException e) {
				JavaPlugin.log(e);
			}
		}
		return initRoot;
	}

	private void typePageDialogFieldChanged(DialogField field) {
		String fieldName = null;
		if (field == fPackageDialogField) {
			// fPackageStatus= packageChanged();
			updatePackageStatusLabel();
			fieldName = "";
		}
		if (field == webPath) {
			String jspFolder = webPath.getText();
			if (jspFolder.length() == 0) {
				webPath.setStatus(NewWizardMessages.NewTypeWizardPage_default);
			} else {
				webPath.setStatus(""); //$NON-NLS-1$
			}
		}

	}

	private void updatePackageStatusLabel() {
		String packName = fPackageDialogField.getText();
		if (packName.length() == 0) {
			fPackageDialogField
					.setStatus(NewWizardMessages.NewTypeWizardPage_default);
		} else {
			fPackageDialogField.setStatus(""); //$NON-NLS-1$
		}
	}

	private IPackageFragment choosePackage() {
		SelectionDialog dialog = null;
		try {
			dialog = JavaUI.createPackageDialog(getShell(),
					JavaCore.create(getProject()),
					IJavaElementSearchConstants.CONSIDER_REQUIRED_PROJECTS);
			dialog.setHelpAvailable(false);
			dialog.setMessage(NewWizardMessages.NewTypeWizardPage_ChoosePackageDialog_description);
			dialog.setTitle(NewWizardMessages.NewTypeWizardPage_ChoosePackageDialog_title);
			if (dialog.open() == Window.OK) {
				Object[] objs = dialog.getResult();
				if (objs.length > 0) {
					return (IPackageFragment) objs[0];
				}
			}
		} catch (JavaModelException e) {
			AvicitLogger.openError(e);
		}
		return null;
	}
	
	protected void packageFieldChanged(String pkg) {
		try {
			IPath path = getJavaProject().getOutputLocation();
			String web = "web";
			if (path.segmentCount() > 1 && !path.segment(1).equals("bin")) {
				web = path.segment(1);
			}
			IFolder jspFolder = getProject().getFolder(web);
			if (!jspFolder.exists()) {
				jspFolder.create(true, true, null);
			}
			webPath.setText(jspFolder.getProjectRelativePath().toString() + "/"
					+ pkg.replace(".", "/"));
		} catch (CoreException e) {
			AvicitLogger.logError(e);
		}
	}

	@Override
	public boolean canFlipToNextPage() {

		if (isMain) {
			if (!treeviewer.getSelection().isEmpty()) {
				getRealWizard().setSubTableNames(new ArrayList<String>());

				for (int i = 0; i < treeviewer.getTable().getSelection().length; i++) {
					String t = treeviewer.getTable().getSelection()[i].getText();
					getRealWizard().getSubTableNames().add(t);
					LoadFieldToCombo(t, comboSubField);
				}
				if (comboMainField.getItemCount() == 0 || comboSubField.getItemCount() == 0) {
					return false;
				}
				getRealWizard().setMainRelationField(comboMainField.getText());
				getRealWizard().setSubRelationField(comboSubField.getText());
				
//				getRealWizard().gridpage.updateCols();
			} else {
				return false;
			}
		}

		if (isTree) {

			comboMainFieldtree.getItem(0);
			comboMainFieldtree.getSelection();
			comboMainFieldtree.getText();
			if (comboMainFieldtree.getItemCount() == 0
					|| comboSubFieldtree.getItemCount() == 0) {
				return false;
			}
			if(comboMainFieldtree.getText().equals("")||comboSubFieldtree.getText().equals("")){
				return false;
			}
			getRealWizard().setTreeRelationField(comboMainFieldtree.getText());
			getRealWizard().setTreeSubRelationField(comboSubFieldtree.getText());
			getRealWizard().setTreeDisplayField(comboMainFieldtreeDisplay.getText());
		}
		if (fPackageDialogField != null&& this.fPackageDialogField.getText() != null&& this.isAvicitPackageSpe(fPackageDialogField.getText())) {
			this.getRealWizard().setCreatePackage(this.fPackageDialogField.getText());
		} else {
			return false;
		}

		if (this.textTepletePath.getText() != null&& !this.textTepletePath.getText().equals("")) {
			this.getRealWizard().setTempletPath(this.textTepletePath.getText());
		} else if (!this.btnDefineCheck.getSelection()) {
			URL tplUrl = getBundle().getEntry(ConstDefine.TEMPLATE_BASE_PATH);
			try {
				tplUrl = FileLocator.toFileURL(tplUrl);
			} catch (IOException e) {
				e.printStackTrace();
			}

			this.getRealWizard().setTempletPath(
					tplUrl.getFile() + "");
		} else {
			return false;
		}

		//TODO dd
		Text text = webPath.getTextControl(null);
		webPathJsp = text.getText().replaceAll(pathConstant, "").replaceAll("\\.", "/");
				
		// 生成信息
		this.getRealWizard().getDoCreatPage().initListShowConfInfo();
		List<String> tableList = getRealWizard().getMainTableNames();
		if(tableList!=null&&tableList.size()>0){
//			getRealWizard().gridpage.updateCols(tableList.get(0));
			getRealWizard().gridpage.updateTreeData();
		}
		
		
		return true;

	}

	/**
	 * 监测包名是否符合avicit规范
	 * 
	 * @param strPackage
	 *            包名
	 * @return 返回值
	 */
	private boolean isAvicitPackageSpe(String strPackage) {
		if (strPackage.equals("")) {
//			return false;
			return true;
		}
		if (strPackage.indexOf(".") != -1) {
			String[] strField = strPackage.split("\\.");
			if (!strField[0].equals("avicit")) {
				//return false;
			}
			/*if (strPackage.indexOf("platform6") != -1) {
				return false;
			}*/
		}else{
			if (!strPackage.equals("avicit")) {
				return false;
			}
		}
		return true;
	}

	
	@Override
	public boolean isPageComplete() {
		if( fPackageDialogField.getTextControl(null).getText().trim().equals("") ){
			return true;
		}		
		return super.isPageComplete();
	}

	public boolean canFinish() {
		return false;
	}
	
	
}
