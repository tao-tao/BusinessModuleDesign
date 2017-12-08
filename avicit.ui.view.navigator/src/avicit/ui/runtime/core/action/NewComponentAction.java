package avicit.ui.runtime.core.action;


import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.internal.resources.Project;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.util.CoreUtility;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
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

import avicit.platform6.tools.codegeneration.CodeGenerationActivator;
import avicit.ui.core.runtime.resource.EclipseProjectDelegate;
import avicit.ui.runtime.core.INode;
import avicit.ui.runtime.core.cluster.FunctionClusterNode;
import avicit.ui.runtime.core.cluster.function.ComponentNode;
import avicit.ui.runtime.core.node.AbstractFolderNode;
import avicit.ui.runtime.core.node.ProjectNode;
import avicit.ui.runtime.core.subsystem.SubSystemNode;
import avicit.ui.view.module.ModelManagerImpl;
import avicit.ui.view.module.ProjectModelFactory;
import avicit.ui.view.navigator.views.AvicitProjectNavigator;
import avicit.ui.view.navigator.views.AvicitProjectViewer;

import com.tansun.runtime.resource.adapter.WorkbenchAdapterFactory;

@SuppressWarnings("restriction")
public class NewComponentAction extends SelectionListenerAction {
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

	public NewComponentAction(String text){
		super(text);
//		this.obj = obj;
	}

	public NewComponentAction(String text, Shell shell, int flag) {
		super(text);
		this.shell = shell;
		this.flag = flag;
		this.init();
	}

	public NewComponentAction(String text, Project obj, int flag) {
		super(text);
		this.obj = obj;
		this.shell = shell;
		this.flag = flag;
		this.init();
		this.SetInfo();
		this.setDialogTitle("新建功能模块");
		this.setImages(new Image(Display.getCurrent(), getClass()
				.getResourceAsStream("/icons/eview16/component_dialog.png")));
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
			if(!this.checkPackageName(path)){
				dialog.setMessage("功能模块路径不合法,不能包括 [\\] [.] 等字符.", IMessageProvider.ERROR);
				return false;
			}

			Object[] children = o.getChildren(projectNode);
			for (int i = 0; i < children.length; i++) {
				if (children[i] instanceof ComponentNode) {
					ComponentNode cn = (ComponentNode) children[i];
					String id = cn.getProperty("component.id");
					String name = cn.getProperty("component.name");
					if(node.getFolder(path).exists()){
						dialog.setMessage("功能模块路径不能重复.", IMessageProvider.ERROR);
						return false;
					}

					if (id.equals(dialog.vo.id)) {
						dialog.setMessage("功能模块ID不能重复.", IMessageProvider.ERROR);

						return false;
					} else if (name.equals(dialog.vo.name)) {
						dialog.setMessage("功能模块名称不能重复.", IMessageProvider.ERROR);
						return false;
					}
				}
			}
		}

		return true;
	}

	public void run() {
		Object obj = getStructuredSelection().getFirstElement();

		if (obj == null)
			return;
		node = null;
		dialog = new UpdateComponentDialog(shell, node, obj);
		dialog.create();
		int code = dialog.open();

		if (code == IDialogConstants.OK_ID) {
			ConfigVO vo = dialog.vo;

			IProject project = ((AbstractFolderNode)obj).getResource().getResource().getProject();
			AbstractFolderNode subNode = null;

			if (obj instanceof FunctionClusterNode) {
				subNode = (FunctionClusterNode)obj;
			}

			if (obj instanceof SubSystemNode) {
				subNode = (SubSystemNode)obj;
			}

			// 2015-03-16
			// String subSystemPath = "avicit"+ subNode.getParent().
			String configPath = subNode.getConfigPath();
			String subSystemPath = configPath.substring(4);

			IJavaProject jp = JavaCore.create(project);

			// IFolder folder = project.getFolder(subSystemPath+"/"+path);
			// IFolder folder =
			// project.getFolder("src-common/"+subSystemPath+"/"+path);
			IFolder folder = project.getFolder(CodeGenerationActivator.SOURCE_FOLDER_PATH+"/"+subSystemPath + "/" + path);
			IFolder webFolder = project.getFolder("WebRoot").getFolder(subSystemPath).getFolder(path);

			if (!(folder.exists())) {
				try {
					// IClasspathEntry[] path = jp.getRawClasspath();
					// IClasspathEntry[] pathadd = new
					// IClasspathEntry[path.length + 1];
					// for (int i = 0; i < path.length; i++) {
					// pathadd[i] = path[i];
					//
					// }
					CoreUtility.createFolder(folder, true, true, null);
					CoreUtility.createFolder(webFolder, true, true, null);
					// IClasspathEntry newEntry =
					// JavaCore.newSourceEntry("test");
					// IPath initialPath = new Path(project.getFullPath()
					// +"/"+subSystemPath
					// + "/" + this.getPath());
					// pathadd[pathadd.length - 1] =
					// JavaCore.newSourceEntry(initialPath);
					// jp.setRawClasspath(pathadd, null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			IPackageFragmentRoot pr = jp.getPackageFragmentRoot(folder);

			IFolder metaf = folder.getFolder("META-INF");

			try {
				if (!metaf.exists())
					metaf.create(true, true, null);// 该文件尚未创建，即调用了SubsystemNode.getChilren方法，并且开始新建ComponentNode，导致factory.isAccept为false，返回null引发后续异常（factory.isAccept返回true）；
				IFile file = folder.getFile(INode.CONFIG_FILE);

				if (!file.exists()) {
					file.create(new ByteArrayInputStream(createInitial(vo, this.type).getBytes("UTF-8")), true, null);
					file.setCharset("UTF-8", null);
				}

//				if (this.type.equals("Comp")) {
					// CreateActionUtil.createXML(this.id,this.name,
					// project, subSystemPath + "/" + this.path);
					// CreateActionUtil.createComponentExtXML(this.id,
					// project, subSystemPath + "/" + this.path);
//				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			// 2015-03-10
			final AvicitProjectViewer viewer = AvicitProjectNavigator.getViewer();
			// if (SwtResourceUtil.isValid(viewer.getTree())) {
			final AbstractFolderNode folderNode = (AbstractFolderNode) ModelManagerImpl.getInstance().getPool().getModel(new EclipseProjectDelegate(project), ProjectModelFactory.TYPE, true, null);

			Display display = Display.getDefault();

			if (display != null)
				display.asyncExec(new Runnable() {
					public void run() {
						// viewer.add(folderNode, childNode);
						List refreshList = new ArrayList();
						refreshList.add(folderNode.getResource().getResource().getProject());
						// refreshList.add(childNode.getResource().getResource());
						viewer.getResourceMapper().refresh(false, refreshList, refreshList, 500);
					}
				});
		}

		super.run();
	}

	protected boolean updateSelection(IStructuredSelection selection) {
		boolean flag = super.updateSelection(selection);
		if (flag)
			flag = getStructuredSelection().toList().size() == 1;
		return flag;
	}
	
	private boolean checkPackageName(String path){
		boolean flag = false;
		flag = (path.indexOf('.')!=-1||path.indexOf('\\')!=-1) ? false : true;
		return flag;
	}	

	public static String createInitial(ConfigVO vo, String type)
			throws JavaModelException {

		StringBuffer buffer = new StringBuffer();
		buffer.append("component.id=" + vo.id).append('\n');
		buffer.append("component.path=" + vo.path).append('\n');
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

//		buffer.append("component.dtype=" + vo.dialogTitle).append('\n');
		return buffer.toString();
	}

	class UpdateComponentDialog extends TitleAreaDialog {

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
		private String initMessage;

		public void setInitMessage(String initMessage) {
			this.initMessage = initMessage;
		}

		public String getInitMessage() {
			return initMessage;
		}

		UpdateComponentDialog(Shell parentShell, ProjectNode node,
				Object selection) {
			super(shell);
			this.node = node;
			this.selection = selection;
			this.setInitMessage(DialogTitle);
		}

		protected Control createContents(Composite parent) {
			super.createContents(parent);
			this.getShell().setText(DialogTitle);
			this.setTitle(DialogTitle);
			this.setMessage(this.getInitMessage(), IMessageProvider.INFORMATION);// ���ó�ʼ���Ի������ʾ��Ϣ
			if(getImages()!=null)
			this.setTitleImage(getImages());
			return parent;
		}

		protected Control createDialogArea(Composite composite) {
			this.crateArea(composite);
			return composite;
		}

		protected Control createCustomArea(Composite parent) {
			Composite area = (Composite) super.createDialogArea(parent);
			Composite composite = new Composite(area, SWT.NONE);
			GridLayout layout = new GridLayout(2, false);
			composite.setLayout(layout);
			composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

			Label namelabel = new Label(composite, SWT.NONE);
			namelabel.setText("相对于组件(或功能集)的路径: ");
			GridData gd = new GridData();
		    gd.grabExcessHorizontalSpace = true;
		    gd.horizontalAlignment = GridData.FILL;
			text_path = new Text(composite, SWT.BORDER);
			text_path.setLayoutData(gd);
			text_path.setEditable(true);

			namelabel = new Label(composite, SWT.NONE);
			namelabel.setText("功能模块名称: ");
			text_name = new Text(composite, SWT.BORDER);
			text_name.setLayoutData(gd);
			text_name.setEditable(true);

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
	
		public void crateArea(Composite parent) {
			createCustomArea(parent);
		}

		protected void okPressed() {
			
				if (StringUtils.isEmpty(text_name.getText())) {
					this.setMessage("请输入模块名称.", IMessageProvider.ERROR);

					return;
				}
//				if (StringUtils.isEmpty(text_id.getText())) {
//
//					this.setMessage("请输入模块ID名称.", IMessageProvider.ERROR);
//					return;
//				}
				if (StringUtils.isEmpty(text_path.getText())) {

					this.setMessage("请输入模块路径.", IMessageProvider.ERROR);
					return;
				}
				vo.dialogTitle = DialogTitle;
				vo.enable = true;
//				vo.id = this.text_id.getText();
				vo.name = this.text_name.getText();
				// vo.service = this.text_service.getText();
				// vo.dao = this.text_dao.getText();
				StringBuffer m = new StringBuffer();

//				m.append(checkbox_input.getSelection() ? "input," : "");
//				m.append(checkbox_pres.getSelection() ? "zhanshi," : "");
//				m.append(checkbox_cont.getSelection() ? "controller," : "");
//				m.append(checkbox_serv.getSelection() ? "ywc," : "");
//				m.append(checkbox_dao.getSelection() ? "jcc," : "");
//				m.append(checkbox_data.getSelection() ? "gzmb," : "");
//				m.append(checkbox_desi.getSelection() ? "designer," : "");
				// m.append(checkbox_proc.getSelection() ? "process" : "");
				vo.modules = m.toString();
//				vo.version = this.text_version.getText();
//				vo.type = this.combo_type.getText();
				vo.path = this.text_path.getText();

				// vo.auto = this.checkbox_auto.getSelection();
				setPath(text_path.getText());
//				setType(this.combo_type.getText());
				setName(this.text_name.getText());
//				setId(this.text_id.getText());//
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
	}

	public void validate() {

	}

	static class ConfigVO {
		public ConfigVO() {
		}

		public String dialogTitle;
		public String id;
		public String path;
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