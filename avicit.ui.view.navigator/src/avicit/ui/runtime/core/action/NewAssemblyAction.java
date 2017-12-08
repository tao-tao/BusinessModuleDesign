package avicit.ui.runtime.core.action;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.internal.resources.Project;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.util.CoreUtility;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.osgi.service.datalocation.Location;
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
import avicit.ui.common.util.FileUtil;
import avicit.ui.core.runtime.resource.EclipseFolderDelegate;
import avicit.ui.core.runtime.resource.EclipseProjectDelegate;
import avicit.ui.core.runtime.resource.IProjectDelegate;
import avicit.ui.core.runtime.resource.ISourceFolderDelegate;
import avicit.ui.runtime.core.INode;
import avicit.ui.runtime.core.node.AbstractFolderNode;
import avicit.ui.runtime.core.node.ProjectNode;
import avicit.ui.runtime.core.subsystem.SubSystemNode;
import avicit.ui.view.create.CustomDialog;
import avicit.ui.view.module.ModelManagerImpl;
import avicit.ui.view.module.ProjectModelFactory;
import avicit.ui.view.navigator.views.AvicitProjectNavigator;
import avicit.ui.view.navigator.views.AvicitProjectViewer;

import com.tansun.runtime.resource.adapter.WorkbenchAdapterFactory;

public class NewAssemblyAction extends SelectionListenerAction{
	
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

	public NewAssemblyAction(String text){
		super(text);
//		this.obj = obj;
	}
	
	public NewAssemblyAction(String text,IProject pro) {
		super(text);
		this.obj = (Project) pro;
		this.setDialogTitle("新建组件");
		this.setImages(new Image(Display.getCurrent(), getClass()
				.getResourceAsStream("/icons/eview16/component_dialog.png")));		
	}	
	public NewAssemblyAction(String text, Shell shell, int flag) {
		super(text);
		this.shell = shell;
		this.flag = flag;
		this.init();

	}

	public NewAssemblyAction(String text, Project obj, int flag) {
		super(text);
		this.obj = obj;
		this.shell = shell;
		this.flag = flag;
		this.init();
		this.SetInfo();
		this.setDialogTitle("新建组件");
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
			IProjectDelegate projectDelegate = ((ProjectNode)projectNode).getProject();
			ISourceFolderDelegate[] sources = projectDelegate.getSourceFolders();			
			
			Object[] children = o.getChildren(projectNode);
			if(!this.checkPackageName(path)){
				dialog.setMessage("组件路径不合法,不能包括 [\\] [.] 等字符.", IMessageProvider.ERROR);
				return false;			
			}
			for(int i=0;i<sources.length;i++){
				if(sources[i].getFolder(path).exists()){
					dialog.setMessage("组件路径不能重复.", IMessageProvider.ERROR);
					return false;					
				}
			}
//			for (int i = 0; i < children.length; i++) {
//				if (children[i] instanceof ComponentNode) {
//					ComponentNode cn = (ComponentNode) children[i];
//					String id = cn.getProperty("component.id");
//					String name = cn.getProperty("component.name");
//					if(node.getFolder(path).exists()){
//						dialog.setMessage("组件路径不能重复.", IMessageProvider.ERROR);
//						return false;
//					}
//					if (id.equals(dialog.vo.id)) {
//						dialog.setMessage("组件ID不能重复.", IMessageProvider.ERROR);
//
//						return false;
//					} else if (name.equals(dialog.vo.name)) {
//						dialog.setMessage("组件名称不能重复.", IMessageProvider.ERROR);
//						return false;
//					}
//				}
//			}
		}
		return true;
	}

	private boolean checkPackageName(String path){
		boolean flag = false;
		flag = (path.indexOf('.')!=-1||path.indexOf('\\')!=-1) ? false : true;
		return flag;
	}
	public void run() {
//		Object obj = getStructuredSelection().getFirstElement();
//		if (obj == null)
//			return;
		node = null;
		dialog = new UpdateComponentDialog(shell, node, obj);
		dialog.create();
		int code = dialog.open();
		if (code == IDialogConstants.OK_ID) {
			ConfigVO vo = dialog.vo;
			if (obj instanceof Project) {
//				IProject project = ((AbstractFolderNode)obj).getResource().getResource().getProject();
				IProject project = obj;
//				SubSystemNode subNode = (SubSystemNode)obj;
//				String subSystemPath = subNode.getName();
//				IProject project = (IProject) obj;
				IJavaProject jp = JavaCore.create(project);

//				IFolder folder = project.getFolder(subSystemPath+"/"+path);
//				IFolder folder = project.getFolder("src-common/"+subSystemPath+"/"+path);
//				IFolder folder = project.getFolder("src-common/"+path);
				IFolder folder = project.getFolder(CodeGenerationActivator.SOURCE_FOLDER_PATH+"/"+path);
//				IFolder webFolder = project.getFolder("WebRoot").getFolder(subSystemPath).getFolder(path);
				IFolder webFolder = project.getFolder("WebRoot").getFolder(path);
				if (!(folder.exists())) {
					try {

//						IClasspathEntry[] path = jp.getRawClasspath();
//						IClasspathEntry[] pathadd = new IClasspathEntry[path.length + 1];
//						for (int i = 0; i < path.length; i++) {
//							pathadd[i] = path[i];
//
//						}
						CoreUtility.createFolder(folder, true, true, null);
						CoreUtility.createFolder(webFolder, true, true, null);
						// IClasspathEntry newEntry =
						// JavaCore.newSourceEntry("test");
//						IPath initialPath = new Path(project.getFullPath() +"/"+subSystemPath
//								+ "/" + this.getPath());
//						pathadd[pathadd.length - 1] = JavaCore.newSourceEntry(initialPath);
//						jp.setRawClasspath(pathadd, null);
					} catch (CoreException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} 

				IPackageFragmentRoot pr = jp.getPackageFragmentRoot(folder);

				if (true) {
//					IFolder metaf = folder.getFolder("META-INF");
					try {
//						if (!metaf.exists())
//							metaf.create(true, true, null);//该文件尚未创建，即调用了SubsystemNode.getChilren方法，并且开始新建ComponentNode，导致factory.isAccept为false，返回null引发后续异常（factory.isAccept返回true）；
						IFile file = folder.getFile(INode.SUBSYSTEM_DESC);
						if (!file.exists()) {
							file.create(new ByteArrayInputStream(createInitial(vo, this.type).getBytes("UTF-8")), true, null);

							Location location = Platform.getInstallLocation();
							File folder1 = new File(location.getURL().getPath().concat("/dropins/avicit_platform/plugins/META-INF/config"));
							File[] files = folder1.listFiles();

							for (File file1 : files) {
								IFile targetFile = folder.getFile(file1.getName());

								if (!targetFile.exists()) {
									FileInputStream fis = new FileInputStream(file1);
									targetFile.create(fis, true, null);
								}
							}
						}
//						if (this.type.equals("Comp")) {
//							/*CreateActionUtil.createXML(this.name,
//									(IProject) obj, this.path);*/
//							CreateActionUtil.createXML(this.id,this.name,
//									project, subSystemPath + "/" + this.path);
//							
//							CreateActionUtil.createComponentExtXML(this.id,
//									project, subSystemPath + "/" + this.path);
//						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				//2015-03-10
				final AbstractFolderNode folderNode = (AbstractFolderNode) ModelManagerImpl.getInstance().getPool().getModel(new EclipseProjectDelegate(project), ProjectModelFactory.TYPE, true, null);		
				SubSystemNode childNode = new SubSystemNode(new EclipseFolderDelegate(folder));
				childNode.setParent(folderNode);
				folderNode.getChildren().add(childNode);
				final AvicitProjectViewer viewer = AvicitProjectNavigator.getViewer();
//				if (SwtResourceUtil.isValid(viewer.getTree())) {
				if(true){	
					Display display = Display.getDefault();
					if (display != null)
						display.asyncExec(new Runnable() {
							public void run() {
//								viewer.add(folderNode, childNode);
								List refreshList = new ArrayList();
								refreshList.add(folderNode.getResource().getResource().getProject());
//								refreshList.add(childNode.getResource().getResource());
								viewer.getResourceMapper().refresh(false, refreshList, refreshList, 500);
							}
						});
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
		buffer.append("assembly.id=" + vo.id).append('\n');
		buffer.append("assembly.name=" + vo.name).append('\n');
		buffer.append("assembly.version=" + vo.version).append('\n');
		buffer.append("assembly.type=" + vo.type).append('\n');
		buffer.append("assembly.modules=" + vo.modules).append('\n');
		buffer.append("assembly.service=" + vo.service).append('\n');
		buffer.append("assembly.dao=" + vo.dao).append('\n');
		buffer.append("assembly.auto=" + vo.auto).append('\n');
//		if ("Comp".equals(type)) {
//			buffer.append("component.iscomp=" + true).append('\n');
//		} else {
//			buffer.append("component.iscomp=" + false).append('\n');
//		}

//		buffer.append("component.dtype=" + vo.dialogTitle).append('\n');
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
			text_path.setText("avicit/");


			namelabel = new Label(composite, SWT.NONE);
			namelabel.setText("组件名称: ");
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
			checkbox_input.setText("展示层");
			gd = new GridData();
			checkbox_input.setLayoutData(gd);
			checkbox_input.setSelection(true);
			checkbox_input.setEnabled(false);
			
			checkbox_pres = new Button(composite_1, SWT.CHECK);
			checkbox_pres.setText("业务对象层");
			gd = new GridData();
			checkbox_pres.setLayoutData(gd);
			checkbox_pres.setSelection(true);
			checkbox_pres.setEnabled(false);	

			checkbox_cont = new Button(composite_1, SWT.CHECK);
			checkbox_cont.setText("控制层");
			gd = new GridData();
			checkbox_cont.setLayoutData(gd);
			checkbox_cont.setSelection(true);
			checkbox_cont.setEnabled(false);	

			checkbox_serv = new Button(composite_1, SWT.CHECK);
			checkbox_serv.setText("逻辑层");
			gd = new GridData();
			checkbox_serv.setLayoutData(gd);
			checkbox_serv.setSelection(true);
			checkbox_serv.setEnabled(false);	

			checkbox_dao = new Button(composite_1, SWT.CHECK);
			checkbox_dao.setText("服务层");
			gd = new GridData();
			checkbox_dao.setLayoutData(gd);
			checkbox_dao.setSelection(true);
			checkbox_dao.setEnabled(false);	

			checkbox_desi = new Button(composite_1, SWT.CHECK);
			checkbox_desi.setText("数据访问层");
			gd = new GridData();
			checkbox_desi.setLayoutData(gd);
			checkbox_desi.setSelection(true);
			checkbox_desi.setEnabled(false);	

//			check(composite_1, checkbox_input, "input");
//			check(composite_1, checkbox_dao, "jcc");
//			check(composite_1, checkbox_serv, "ywc");
//			check(composite_1, checkbox_cont, "controller");
//			check(composite_1, checkbox_pres, "zhanshi");
//			check(composite_1, checkbox_data, "gzmb");
//			check(composite_1, checkbox_desi, "designer");
			
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
					this.setMessage("请输入组件名称.", IMessageProvider.ERROR);

					return;
				}
				if (text_path.getText().startsWith("avicit/") && text_path.getText().length()==7) {

					this.setMessage("请在路径avicit/后输入组件路径.", IMessageProvider.ERROR);
					return;
				}
				if (StringUtils.isEmpty(text_path.getText())) {

					this.setMessage("请输入组件路径.", IMessageProvider.ERROR);
					return;
				}
				vo.dialogTitle = DialogTitle;
				vo.enable = true;
//				vo.id = this.text_id.getText();
				vo.name = this.text_name.getText();
				// vo.service = this.text_service.getText();
				// vo.dao = this.text_dao.getText();
				StringBuffer m = new StringBuffer();

				m.append(checkbox_input.getSelection() ? "input," : "");
				m.append(checkbox_pres.getSelection() ? "zhanshi," : "");
				m.append(checkbox_cont.getSelection() ? "controller," : "");
				m.append(checkbox_serv.getSelection() ? "ywc," : "");
				m.append(checkbox_dao.getSelection() ? "jcc," : "");
				m.append(checkbox_desi.getSelection() ? "designer," : "");
				// m.append(checkbox_proc.getSelection() ? "process" : "");
				vo.modules = m.toString();
				vo.version = this.text_path.getText();
				vo.type = this.combo_type.getText();
				
				// vo.auto = this.checkbox_auto.getSelection();
				setPath(text_path.getText());
				setType(this.combo_type.getText());
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
