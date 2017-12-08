package avicit.ui.view.create;


import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.internal.resources.Project;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.util.CoreUtility;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.actions.SelectionListenerAction;
import org.eclipse.ui.model.IWorkbenchAdapter;

import avicit.ui.core.runtime.resource.EclipseProjectDelegate;
import avicit.ui.runtime.core.INode;
import avicit.ui.runtime.core.cluster.function.ComponentNode;
import avicit.ui.runtime.core.node.ProjectNode;
import avicit.ui.view.module.ModelManagerImpl;
import avicit.ui.view.module.ProjectModelFactory;

import com.tansun.runtime.resource.adapter.WorkbenchAdapterFactory;

public class ModulCreate extends SelectionListenerAction {
	private String path;
	private Project obj;
	private String type;
	public HashMap<String, String> map;
	UpdateComponentDialog dialog;
	ProjectNode node;
	private int flag;
	public static final int constru = 4;
	public static final int manager = 1;
	public static final int report = 2;
	public static final int trans = 3;
	public static final int modulesmanager = 5;
	public Image images;
	public Image getImages() {
		return images;
	}

	public void setImages(Image image) {
		this.images = image;
	}

	public String getType() {
		return type;

	}

	private String DialogTitle;

	public String getDialogTitle() {
		return DialogTitle;
	}

	public void setDialogTitle(String dialogTitle) {
		DialogTitle = dialogTitle;
	}

	public void addSupportItems(String str, String content) {
		map.put(str, content);

	}

	public void SetInfo() {/*
		Pmodel model = new Pmodel();
		String str = null;
		//switch (this.flag) {
		switch (4) {
		case constru:
			str = model.getMenConstru();
			break;
		case report:
			str = model.getMenReport();
			break;
		case manager:
			str = model.getMenManager();
			break;
		case trans:
			str = model.getMenTrans();
			break;
		}

		String[] strs = str.split(",");
		for (int i = 0; i < strs.length; i++) {
			String[] strsp = strs[i].split("[|]");
			this.addSupportItems(strsp[0], strsp[1]);
		}

	*/}

	public void init() {

		map = new HashMap<String, String>();

	}

	public void setType(String type) {
		this.type = type;

	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private String id;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private String name;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	private Shell shell;

	public ModulCreate(String text, Shell shell, int flag) {
		super(text);
		this.shell = shell;
		this.flag = flag;
		this.init();

	}

	public ModulCreate(String text, Project obj, int flag) {
		super(text);
		this.obj = obj;
		this.shell = shell;
		this.flag = flag;
		this.init();
		this.SetInfo();

	}

	public boolean validateComponentNode(Project node,String path) {

		if (node != null) {
			Object projectNode = ModelManagerImpl
					.getInstance()
					.getPool()
					.getModel(new EclipseProjectDelegate((IProject) node),
							ProjectModelFactory.TYPE, true, null);

			IWorkbenchAdapter o = (IWorkbenchAdapter) WorkbenchAdapterFactory
					.getInstance().getAdapter(projectNode,
							IWorkbenchAdapter.class);
			Object[] children = o.getChildren(projectNode);
			for (int i = 0; i < children.length; i++) {
				if (children[i] instanceof ComponentNode) {
					ComponentNode cn = (ComponentNode) children[i];
					String id = cn.getProperty("component.id");
					String name = cn.getProperty("component.name");
					if(node.getFolder(path).exists()){
						dialog.setMessage("模块路径不能重复.", IMessageProvider.ERROR);
						return false;
					}
					if (id.equals(dialog.vo.id)) {
						dialog.setMessage("模块ID不能重复.", IMessageProvider.ERROR);

						return false;
					} else if (name.equals(dialog.vo.name)) {
						dialog.setMessage("模块名称不能重复.", IMessageProvider.ERROR);
						return false;
					}
				}
			}

		}
		return true;
	}

	public void run() {

		node = null;

		dialog = new UpdateComponentDialog(shell, node, obj);
		dialog.create();
		int code = dialog.open();
		if (code == IDialogConstants.OK_ID) {
			ConfigVO vo = dialog.vo;
			if (obj instanceof IProject) {
				IProject project = (IProject) obj;
				IJavaProject jp = JavaCore.create(project);

				IFolder folder = project.getFolder(path);

				if (!(folder.exists())) {
					try {

						IClasspathEntry[] path = jp.getRawClasspath();
						IClasspathEntry[] pathadd = new IClasspathEntry[path.length + 1];
						for (int i = 0; i < path.length; i++) {
							pathadd[i] = path[i];

						}
						CoreUtility.createFolder(folder, true, true, null);
						// IClasspathEntry newEntry =
						// JavaCore.newSourceEntry("test");
						IPath initialPath = new Path(project.getFullPath()
								+ "/" + this.getPath());
						pathadd[pathadd.length - 1] = JavaCore
								.newSourceEntry(initialPath);
						jp.setRawClasspath(pathadd, null);
					} catch (CoreException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} 

				IPackageFragmentRoot pr = jp.getPackageFragmentRoot(folder);

				if (true) {
					IFolder metaf = folder.getFolder("META-INF");
					try {
						if (!metaf.exists())
							metaf.create(true, true, null);
						IFile file = folder.getFile(INode.CONFIG_FILE);
						if (!file.exists()) {
							file.create(
									new ByteArrayInputStream(createInitial(vo,
											this.type).getBytes("UTF-8")),
									true, null);
							file.setCharset("UTF-8", null);
						}
						if (this.type.equals("Comp")) {
							/*CreateActionUtil.createXML(this.name,
									(IProject) obj, this.path);*/
							CreateActionUtil.createXML(this.id,this.name,
									(IProject) obj, this.path);
							
							CreateActionUtil.createComponentExtXML(this.id,
									(IProject) obj, this.path);
						}
					} catch (JavaModelException e) {
						e.printStackTrace();
					} catch (CoreException e) {
						e.printStackTrace();
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}

				}

			}
		}
		super.run();
	}

	protected boolean updateSelection(IStructuredSelection selection) {
		boolean flag = super.updateSelection(selection);
		if (flag)
			flag = getStructuredSelection().toList().size() == 1;
		return flag;
	}

	public static String createInitial(ConfigVO vo, String type)
			throws JavaModelException {

		StringBuffer buffer = new StringBuffer();
		buffer.append("component.id=" + vo.id).append('\n');
		buffer.append("component.name=" + vo.name).append('\n');
		buffer.append("component.version=" + vo.version).append('\n');
		buffer.append("component.type=" + vo.type).append('\n');
		buffer.append("component.modules=" + vo.modules).append('\n');
		buffer.append("component.service=" + vo.service).append('\n');
		buffer.append("component.dao=" + vo.dao).append('\n');
		buffer.append("component.auto=" + vo.auto).append('\n');
		if ("Comp".equals(type)) {
			buffer.append("component.iscomp=" + true).append('\n');
		} else {
			buffer.append("component.iscomp=" + false).append('\n');
		}

		buffer.append("component.dtype=" + vo.dialogTitle).append('\n');
		return buffer.toString();
	}

	class UpdateComponentDialog extends CustomDialog {

		// private Button checkbox_enable;
		private Button checkbox_input;
		private Button checkbox_pres;
		private Button checkbox_cont;
		private Button checkbox_serv;
		private Button checkbox_dao;
		private Button checkbox_data;
		private Button checkbox_desi;
		// private Button checkbox_proc;
		private Text text_version;
		private Text text_id;
		private Text text_name;
		private Text text_path;
		private Text text_service;
		private Text text_dao;
		private Combo combo_type;
		ProjectNode node;
		Object selection;
		private Button checkbox_auto;

		UpdateComponentDialog(Shell parentShell, ProjectNode node,
				Object selection) {
			super(shell);
			this.node = node;
			this.selection = selection;
			this.setInitMessage(DialogTitle);
			this.setBeforeMessage(DialogTitle);
			this.setDialogName(DialogTitle);
			this.setTitleName(DialogTitle);
			this.setImage(getImages());
		}

		protected Control createCustomArea(Composite parent) {
			Composite composite = new Composite(parent, SWT.NONE);
			GridLayout layout = new GridLayout();
			layout.numColumns = 2;
			composite.setLayout(layout);

			Label namelabel = new Label(composite, SWT.NONE);
			namelabel.setText("路径: ");
			GridData gd = new GridData();
			gd.widthHint = convertWidthInCharsToPixels(15);
			namelabel.setLayoutData(gd);
			text_path = new Text(composite, SWT.BORDER);
			gd = new GridData(GridData.FILL_HORIZONTAL);
			text_path.setLayoutData(gd);
			text_path.setEditable(true);

			namelabel = new Label(composite, SWT.NONE);
			namelabel.setText("模块id: ");
			gd = new GridData();
			gd.widthHint = convertWidthInCharsToPixels(15);
			namelabel.setLayoutData(gd);
			text_id = new Text(composite, SWT.BORDER);
			gd = new GridData(GridData.FILL_HORIZONTAL);
			text_id.setLayoutData(gd);

			namelabel = new Label(composite, SWT.NONE);
			namelabel.setText("模块名称: ");
			gd = new GridData();
			gd.widthHint = convertWidthInCharsToPixels(15);
			namelabel.setLayoutData(gd);
			text_name = new Text(composite, SWT.BORDER);
			gd = new GridData(GridData.FILL_HORIZONTAL);
			text_name.setLayoutData(gd);

			namelabel = new Label(composite, SWT.NONE);
			namelabel.setText("类型: ");
			gd = new GridData();
			gd.widthHint = convertWidthInCharsToPixels(15);
			namelabel.setLayoutData(gd);
			combo_type = new Combo(composite, SWT.DROP_DOWN|SWT.READ_ONLY);
			gd = new GridData(GridData.FILL_HORIZONTAL);
			combo_type.setLayoutData(gd);
			combo_type.setItems(new String[] { "Web", "Comp" });
			if (flag == constru) {
				combo_type.select(1);
			} else {
				combo_type.select(0);
			}
			combo_type.setEnabled(false);
			namelabel = new Label(composite, SWT.NONE);
			namelabel.setText("模块特性: ");
			gd = new GridData();
			gd.widthHint = convertWidthInCharsToPixels(15);
			namelabel.setLayoutData(gd);
			Composite composite_1 = new Composite(composite, SWT.NONE);
			final GridLayout gridLayout_1 = new GridLayout();
			gridLayout_1.numColumns = 5;
			composite_1.setLayout(gridLayout_1);
			gd = new GridData(GridData.FILL_HORIZONTAL);
			composite_1.setLayoutData(gd);

			checkbox_input = new Button(composite_1, SWT.CHECK);
			checkbox_input.setText("接入层");
			gd = new GridData();
			checkbox_input.setLayoutData(gd);

			checkbox_pres = new Button(composite_1, SWT.CHECK);
			checkbox_pres.setText("展示层");
			gd = new GridData();
			checkbox_pres.setLayoutData(gd);

			checkbox_cont = new Button(composite_1, SWT.CHECK);
			checkbox_cont.setText("控制层");
			gd = new GridData();
			checkbox_cont.setLayoutData(gd);

			checkbox_serv = new Button(composite_1, SWT.CHECK);
			checkbox_serv.setText("业务逻辑层");
			gd = new GridData();
			checkbox_serv.setLayoutData(gd);

			checkbox_dao = new Button(composite_1, SWT.CHECK);
			checkbox_dao.setText("集成层");
			gd = new GridData();
			checkbox_dao.setLayoutData(gd);

			checkbox_data = new Button(composite_1, SWT.CHECK);
			checkbox_data.setText("规则模版");
			gd = new GridData();
			checkbox_data.setLayoutData(gd);
			
			checkbox_desi = new Button(composite_1, SWT.CHECK);
			checkbox_desi.setText("设计层");
			gd = new GridData();
			checkbox_desi.setLayoutData(gd);

			check(composite_1, checkbox_input, "input");
			check(composite_1, checkbox_dao, "jcc");
			check(composite_1, checkbox_serv, "ywc");
			check(composite_1, checkbox_cont, "controller");
			check(composite_1, checkbox_pres, "zhanshi");
			check(composite_1, checkbox_data, "gzmb");
			check(composite_1, checkbox_desi, "designer");
			
			namelabel = new Label(composite, SWT.NONE);
			namelabel.setText("版本号: ");
			namelabel.setVisible(false);
			gd = new GridData();
			gd.widthHint = convertWidthInCharsToPixels(15);
			namelabel.setLayoutData(gd);
			text_version = new Text(composite, SWT.BORDER);
			gd = new GridData(GridData.FILL_HORIZONTAL);
			text_version.setLayoutData(gd);
			text_version.setVisible(false);
			return composite;
		}

		public void check(Composite parent, Button b, String str) {
			if (b == null)
				return;
			boolean is = true;
			if (map.containsKey(str)) {
				is = true;
			}
			b.setSelection(is);
			b.setEnabled(is);
		}
	
		
		protected void okPressed() {
			
				if (StringUtils.isEmpty(text_name.getText())) {
					this.setMessage("请输入模块名称.", IMessageProvider.ERROR);

					return;
				}
				if (StringUtils.isEmpty(text_id.getText())) {

					this.setMessage("请输入模块ID名称.", IMessageProvider.ERROR);
					return;
				}
				if (StringUtils.isEmpty(text_path.getText())) {

					this.setMessage("请输入模块路径.", IMessageProvider.ERROR);
					return;
				}
				vo.dialogTitle = DialogTitle;
				vo.enable = true;
				vo.id = this.text_id.getText();
				vo.name = this.text_name.getText();
				// vo.service = this.text_service.getText();
				// vo.dao = this.text_dao.getText();
				StringBuffer m = new StringBuffer();

				m.append(checkbox_input.getSelection() ? "input," : "");
				m.append(checkbox_pres.getSelection() ? "zhanshi," : "");
				m.append(checkbox_cont.getSelection() ? "controller," : "");
				m.append(checkbox_serv.getSelection() ? "ywc," : "");
				m.append(checkbox_dao.getSelection() ? "jcc," : "");
				m.append(checkbox_data.getSelection() ? "gzmb," : "");
				m.append(checkbox_desi.getSelection() ? "designer," : "");
				// m.append(checkbox_proc.getSelection() ? "process" : "");
				vo.modules = m.toString();
				vo.version = this.text_version.getText();
				vo.type = this.combo_type.getText();
				
				// vo.auto = this.checkbox_auto.getSelection();
				setPath(text_path.getText());
				setType(this.combo_type.getText());
				setName(this.text_name.getText());
				setId(this.text_id.getText());
				if (!validateComponentNode(obj,text_path.getText()))
					return;
				super.okPressed();

			
		}

		public String deal(Button b, String str) {
			if (b == null) {
				return "";
			} else {
				return b.getSelection() ? str + "," : "";
			}
		}

		public String getName() {
			return text_path.getText();
		}

		public ConfigVO vo = new ConfigVO();

		@Override
		public void crateArea(Composite parent) {
			// TODO Auto-generated method stub
			createCustomArea(parent);
		}
	}

	public void validate() {

	}

	static class ConfigVO {
		public ConfigVO() {
		}

		public String dialogTitle;
		public String id;
		public boolean enable;
		public String name;
		public String modules;
		public String version;
		public String type;
		public String service = "";
		public String dao = "";
		public boolean auto = false;
	}

	public void showMessage(String content) {
		MessageDialog.openInformation(Display.getDefault().getActiveShell(),
				"信息", content);
	}
}