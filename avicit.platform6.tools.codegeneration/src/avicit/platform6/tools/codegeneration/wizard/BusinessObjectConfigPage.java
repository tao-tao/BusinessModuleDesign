package avicit.platform6.tools.codegeneration.wizard;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jdt.internal.ui.util.SWTUtil;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringButtonStatusDialogField;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

import avicit.platform6.tools.codegeneration.core.DBConfig;
import avicit.platform6.tools.codegeneration.core.Resource;
import avicit.platform6.tools.codegeneration.log.AvicitLogger;
//import avicit.ui.runtime.core.node.SubSystemNode;


/**
 * 
 * 作者：dingrc 邮箱：dingrc
 * 
 * @avicit.com 创建时间：2012-12-10 上午09:12:34 类说明：配置数据库 修改记录：
 */
public class BusinessObjectConfigPage extends BaseWizardPage {
	public static final String WORKFLOW_ID = "avicit.platform6.tools.bpm.designer";

	public static final String KEY_SCHEMA = "SchemeORCatlog";
	public static final String KEY_DRIVER = "driver";
	public static final String KEY_URL = "url";
	public static final String KEY_USERNAME = "user";
	public static final String KEY_PASSWORD = "password";
	public static final String KEY_DSNAME = "databaseName";

	public static String DEFAULT_SCHEMA = "PT6";
	public static String DEFAULT_USERNAME = "pt6";
	public static String DEFAULT_PASSWORD = "cape";
	public static String DEFAULT_URL = "jdbc:oracle:thin:@10.216.37.73:1521:mes6";
	public static String DEFAULT_DRIVER = "oracle.jdbc.driver.OracleDriver";
	public static String DEFAULT_DSNAME = "PT6";

	public static final String PROPERTY_USERID = "database_userid";
	public static final String PROPERTY_URL = "database_url";
	public static final String PROPERTY_PASSWORD = "database_password";
	public static final String PROPERTY_SCHEMA = "database_schema";
	public static final String PROPERTY_CATALOG = "database_catalog";
	public static final String PROPERTY_VIEW = "database_view";
	public static final String PROPERTY_DRIVER = "database_driver";
	public static final String PROPERTY_LIBDIR = "lib_dir";
	public static final String PROPERTY_BASEDIR = "basedir";
	public static final String PROPERTY_PLUGIN = "plugin";
	public static final String PROPERTY_PACKAGE = "package";
	public static final String PROPERTY_OUTPUTDIR = "dest_dir";
	public static final String PROPERTY_TABLE = "table";
	public static final String PROPERTY_HBM = "hbm";

	public static final String DATABASE_TYPE = "$DATABASE_TYPE";
	public static final String CLASSNAME_KEY = "$CLASSNAME_KEY";
	public static final String USERNAME_KEY = "$USERNAME_KEY";
	public static final String PASSWORD_KEY = "$PASSWORD_KEY";
	public static final String URL_KEY = "$URL_KEY";

	private Text txtDriver;
	private Text txtURL;
	private Text txtUser;
	private Text txtPassword;
	private Text txtFilter;
	private Button btnLoad;
	private TableViewer tableViewer;
	private List<String> tableList = new ArrayList<String>();
	private IStructuredSelection selection;
	private IProject project;
	private Properties properties = new Properties();
	private static ResourceBundle rb = ResourceBundle
			.getBundle("avicit.platform6.tools.codegeneration.core.ResourceMessages");

	public static ResourceBundle getResourceBundle() {
		return rb;
	}

	// 工作流首选项
	private IPreferenceStore workflowStore;
	// 模块文件夹
	private IFolder folderModule;
	// 是否同步现有模块
	private Button btnSynch;
	// 模块文件选择控件
	protected StringButtonStatusDialogField fieldModule;

	private String fPackageDialogFieldForCodePage;
	private Label lblSchemeORCatlog;
	private Composite container_1;
	private Text txtSchemeORCatlog;
	private Composite container_Tabs;
	private Label label_1;
	private Button btnCheckDB;
	private Text txtDatabaseName;

	@SuppressWarnings(value = { "deprecation" })
	public BusinessObjectConfigPage(String name ,IStructuredSelection selection) {
		super(name);
		setTitle("生成业务对象");
		setDescription("请配置数据库并选择生成业务对象");
		workflowStore = new ScopedPreferenceStore(new InstanceScope(),WORKFLOW_ID);
		String temp =workflowStore.getString(URL_KEY);
		if(!"".equals(temp)){
			DEFAULT_URL = workflowStore.getString(URL_KEY);
		}
		temp =workflowStore.getString(CLASSNAME_KEY);
		if(!"".equals(temp)){
			DEFAULT_DRIVER = workflowStore.getString(CLASSNAME_KEY);
		}
		temp =workflowStore.getString(USERNAME_KEY);
		if(!"".equals(temp)){
			DEFAULT_USERNAME = workflowStore.getString(USERNAME_KEY);
		}
		temp =workflowStore.getString(PASSWORD_KEY);
		if(!"".equals(temp)){
			DEFAULT_PASSWORD = workflowStore.getString(PASSWORD_KEY);
		}
		
		DEFAULT_SCHEMA = DEFAULT_USERNAME.toUpperCase();
		DEFAULT_DSNAME = DEFAULT_USERNAME.toUpperCase();
		
		this.selection = selection;
//		Object obj = selection.getFirstElement();
//		if(obj instanceof SubSystemNode){
//			this.project = ((SubSystemNode)obj).getResource().getResource().getProject();		
//		}
//		this.getRealWizard().setProject(project);
		

	}

	// add by zl
	// private Group groupMainTabs;

	public void createControl(Composite parent) {
		Composite container_a = new Composite(parent, SWT.NONE);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		container_a.setFont(parent.getFont());
		container_a.setLayout(new GridLayout());
		container_1 = new Composite(container_a, SWT.NONE);
		
		 gd.grabExcessHorizontalSpace = true;
		  gd.horizontalAlignment = GridData.FILL;
		FormData fd_container_1 = new FormData();
		fd_container_1.right = new FormAttachment(0, 525);
		fd_container_1.top = new FormAttachment(0, 3);
		fd_container_1.left = new FormAttachment(0, 3);
		container_1.setLayoutData(gd);
		container_1.setFont(container_a.getFont());

		// 设置布局
		GridLayout gl_container_1 = new GridLayout();
		container_1.setLayout(gl_container_1);
		gl_container_1.numColumns = 8;
		gl_container_1.verticalSpacing = 9;

		createDbConfigControl(container_1);

		createListConfigControl(container_1);

		setPageComplete(false);
		setControl(container_a);

		container_Tabs = new Composite(container_a, SWT.NONE);
		FormData fd_container_Tabs = new FormData();
		fd_container_Tabs.top = new FormAttachment(0, 171);
		fd_container_Tabs.left = new FormAttachment(0, 3);
		container_Tabs.setLayoutData(gd);
		container_Tabs.setLayout(null);
		Group groupMainTabs = new Group(container_Tabs, SWT.NONE);
		groupMainTabs.setText("表选择");
		groupMainTabs.setBounds(0, 0, 622, 177);
		groupMainTabs.setLayout(null);
		// TODO 为“加载”按钮增加回车事件
		// TODO 增加一个可输入过滤条件的过滤框
		Label label = new Label(groupMainTabs, SWT.NULL);
		label.setBounds(10, 22, 60, 17);
		label.setText("过滤条件：");
		txtFilter = new Text(groupMainTabs, SWT.BORDER | SWT.SINGLE);
		txtFilter.setBounds(95, 19, 503, 20);
		txtFilter.setToolTipText("在完成加载表操作之后，可通过本输入框对结果进行过滤！");
		txtFilter.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				List<String> filterdList = new ArrayList<String>();
				for (String table : tableList) {
					if (table.toLowerCase().indexOf(
							txtFilter.getText().toLowerCase()) > -1) {
						filterdList.add(table);
					}
				}
				tableViewer.setInput(filterdList);
			}
		});

		label_1 = new Label(groupMainTabs, SWT.NULL);
		label_1.setBounds(10, 52, 66, 84);
		label_1.setText(rb.getString(Resource.TABLES));
		tableViewer = new TableViewer(groupMainTabs, SWT.FULL_SELECTION
				| SWT.SINGLE);
		Table table_1 = tableViewer.getTable();
		table_1.setBounds(95, 45, 503, 122);
		table_1.setHeaderVisible(true);
		table_1.setLinesVisible(true);
		TableColumn column = new TableColumn(table_1, SWT.NONE);
		column.setText("业务表名称");
		column.setWidth(300);
		tableViewer.setContentProvider(new IStructuredContentProvider() {
			@SuppressWarnings("unchecked")
			public Object[] getElements(Object inputElement) {

				if (inputElement instanceof List) {
					List list = (List) inputElement;
					return list.toArray();
				}
				return new Object[0];
			}

			public void dispose() {
			}

			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
			}
		});
		tableViewer.setLabelProvider(new ITableLabelProvider() {
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
		tableViewer.setInput(tableList);
		tableViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {
					public void selectionChanged(SelectionChangedEvent event) {
						setPageComplete(canFlipToNextPage());

					}
				});
	}

	/**
	 * @param container
	 */
	private void createListConfigControl(Composite container) {
	}

	/**
	 * 创建驱动信息页面
	 * 
	 * @param container
	 */
	private void createDbConfigControl(Composite container) {
		// TODO 驱动类可从databaseURL.properties中读取，并且让用户选择
		// TODO 考虑如果没有对应的驱动类，需要用户可选中jar，可参考myeclipse相关界面
		// TODO 对于数据库配置，应该默认去取和工作流相同的配置，并且可保存
		Label label = new Label(container, SWT.NULL);
		label.setText(rb.getString(Resource.DATABASE_DRIVER));
		txtDriver = new Text(container, SWT.BORDER | SWT.SINGLE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 6;
		txtDriver.setLayoutData(gd);
		txtDriver.setText(getDefaultValue(KEY_DRIVER, DEFAULT_DRIVER));
		new Label(container, SWT.NULL);

		label = new Label(container, SWT.NULL);
		label.setText(rb.getString(Resource.DATABASE_URI));
		txtURL = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 6;
		txtURL.setLayoutData(gd);
		txtURL.setText(getDefaultValue(KEY_URL, DEFAULT_URL));
		new Label(container, SWT.NULL);

		lblSchemeORCatlog = new Label(container_1, SWT.NONE);
		lblSchemeORCatlog.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		lblSchemeORCatlog.setText("Scheme/Catlog");

		txtSchemeORCatlog = new Text(container_1, SWT.BORDER);
		txtSchemeORCatlog.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 4, 1));
		txtSchemeORCatlog.setText(getDefaultValue(KEY_SCHEMA, DEFAULT_SCHEMA));
		txtSchemeORCatlog.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				txtDatabaseName.setText(txtSchemeORCatlog.getText());
			}

		});

		btnCheckDB = new Button(container_1, SWT.CHECK);
		btnCheckDB.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (btnCheckDB.getSelection()) {
					txtDatabaseName.setEditable(true);
				} else {
					txtDatabaseName.setEditable(false);
				}
			}
		});
		btnCheckDB.setText("数据库名与Scheme/Catlog不同");

		txtDatabaseName = new Text(container_1, SWT.BORDER);
		txtDatabaseName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		txtDatabaseName.setText(getDefaultValue(KEY_DSNAME, DEFAULT_DSNAME));
		txtDatabaseName.setEditable(false);
		new Label(container_1, SWT.NONE);

		if( getDefaultValue("btnCheckStatus","false").equals("true"))
		{
			btnCheckDB.setSelection(true);
			txtDatabaseName.setEditable(true);
		}

		label = new Label(container, SWT.NULL);
		label.setText(rb.getString(Resource.DATABASE_USER));
		txtUser = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 6;
		txtUser.setLayoutData(gd);
		txtUser.setText(getDefaultValue(KEY_USERNAME, DEFAULT_USERNAME));
		new Label(container, SWT.NULL);
		txtUser.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent arg0) {
				txtSchemeORCatlog.setText(txtUser.getText().toUpperCase());
			}
		});

		label = new Label(container, SWT.NULL);
		label.setText(rb.getString(Resource.DATABASE_PASSWORD));
		txtPassword = new Text(container, SWT.BORDER | SWT.PASSWORD);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 6;
		txtPassword.setLayoutData(gd);
		txtPassword.setText(getDefaultValue(KEY_PASSWORD, DEFAULT_PASSWORD));
		// new Label(container, SWT.NULL);

		btnLoad = new Button(container, SWT.PUSH);
		btnLoad.setText(rb.getString(Resource.LOADTABLE));
		btnLoad.setToolTipText("点击按钮加载数据库表");
		btnLoad.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				try {
					Location location = Platform.getInstallLocation();
					File file = new File(location.getURL().getPath().concat("dropins/avicit_platform/plugins/META-INF/conf"));

					if(!file.exists())
					{
						file.createNewFile();
					}

					properties.load(new FileInputStream(file));
					properties.setProperty(KEY_DRIVER, txtDriver.getText());
					properties.setProperty(KEY_URL, txtURL.getText());
					properties.setProperty(KEY_SCHEMA, txtSchemeORCatlog.getText());
					properties.setProperty(KEY_USERNAME, txtUser.getText());
					properties.setProperty(KEY_PASSWORD, txtPassword.getText());

					if(txtDatabaseName.getEditable())
					{
						properties.setProperty(KEY_DSNAME, txtDatabaseName.getText());
						properties.setProperty("btnCheckStatus", "true");
					}
					else{
						properties.setProperty("btnCheckStatus", "false");
					}

					properties.store(new FileOutputStream(file),null);
				} catch (Exception e1){
					e1.printStackTrace();
				}

				CodeGenerationWizard wizard = (CodeGenerationWizard) getWizard();

				Properties props = wizard.getProps();
				props.put("user", txtUser.getText());
				props.put("password", txtPassword.getText());
				props.put("url", txtURL.getText());
				props.put("driver", txtDriver.getText());
				props.put("SchemaORCatlog", txtSchemeORCatlog.getText());
				props.put("databaseName", txtDatabaseName.getText());
				// groupMainTabs.setText("选择主表：");
				loadTable();
			}
		});
		gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = false;
		gd.horizontalSpan = 1;
		gd.widthHint = SWTUtil.getButtonWidthHint(btnLoad);
		btnLoad.setLayoutData(gd);
	}

	private String getDefaultValue(String key, String defaultValue) {
		/*
		 * Properties props = getSingletonProp(); String value =
		 * props.getProperty(key); return (value == null) ? defaultValue :
		 * value;
		 */
		loadDBConfig();
		return properties.getProperty(key, defaultValue);
	}

	private void loadDBConfig() {
		try {
			Location location = Platform.getInstallLocation();
			File file = new File(location.getURL().getPath().concat("dropins/avicit_platform/plugins/META-INF/conf"));

			if(file.exists())
			{
				properties.load(new FileInputStream(file));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean isOnce = true;

	private Properties getSingletonProp() {
		CodeGenerationWizard wizard = (CodeGenerationWizard) getWizard();
		Properties props = wizard.getProps();
		if (isOnce) {
			InputStream in = null;
			try {
				in = new FileInputStream(wizard.getFile());
				props.load(in);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					if (in != null)
						in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			isOnce = false;
			return props;
		}
		return props;

	}

	@Override
	public boolean canFlipToNextPage() {
		if (!tableViewer.getSelection().isEmpty()) {
			this.getRealWizard().setMainTableNames(new ArrayList<String>());

			for (int i = 0; i < tableViewer.getTable().getSelection().length; i++) {
				String t = tableViewer.getTable().getSelection()[i].getText();
				this.getRealWizard().getMainTableNames().add(t);
			}

			getRealWizard().templeteSelectPage.Refresh();

		}
		return !tableViewer.getSelection().isEmpty();
	}

	public boolean onFinish() {
		boolean canFinish = true;
		return canFinish;
	}

	private Map<String, String> getLables() {
		Map<String, String> labels = new HashMap<String, String>();
		Connection con = null;
		try {
			Class.forName(properties.getProperty(PROPERTY_DRIVER));
			con = DriverManager.getConnection(
					properties.getProperty(PROPERTY_URL),
					properties.getProperty(PROPERTY_USERID),
					properties.getProperty(PROPERTY_PASSWORD));
			String sql = "select * from user_col_comments t where t.table_name = ?";
			String tableName = ((IStructuredSelection) tableViewer
					.getSelection()).getFirstElement().toString();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, tableName);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String column = rs.getString("column_name");
				String label = rs.getString("comments");
				if (StringUtils.isEmpty(label)) {
					label = column;
				}
				String[] tmps = label.split("[:：]");
				labels.put(column, tmps[0]);
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			AvicitLogger.logError(e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					AvicitLogger.logError(e);
				}
			}
		}
		return labels;
	}

	private void loadTable() {
		this.getRealWizard().getTempletPath();
		try {
			tableList.clear();
			tableViewer.setInput(tableList);
			Class<?> driverClass = Class.forName(txtDriver.getText());
			Driver dirver = (Driver) driverClass.newInstance();
			DBConfig dBConfig = new DBConfig();
			// oracle 3
			//sql server 2
			final int dbtype = 3;
			dBConfig.initDriverInfo(dirver, txtURL.getText(),
					txtDriver.getText(), txtUser.getText(),
					txtSchemeORCatlog.getText(), txtPassword.getText(), dbtype);
			dBConfig.testConnection();

			java.util.List<String> list = dBConfig.getAllTable(
					txtSchemeORCatlog.getText(), txtDatabaseName.getText());
			if (list != null && list.size() > 0) {
				tableList.addAll(list);
				tableViewer.setInput(tableList);
				this.getRealWizard().setAllTable(null);
				this.getRealWizard().setAllTable(list);
			} else {
				AvicitLogger.openMsg(null,
						"未查到任何表,请确认数据库名与Scheme/Catlog填写是否正确,或者此数据库下没有任何表。");
			}

		} catch (Exception e) {
			AvicitLogger.openError(e);
		}

	}

}