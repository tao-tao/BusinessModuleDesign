package avicit.platform6.tools.codegeneration.wizard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import net.sf.hibernate.tool.hbm2java.CodeGenerator;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.eclipse.core.internal.resources.Project;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.ui.dialogs.StatusInfo;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IStringButtonAdapter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.LayoutUtil;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringButtonDialogField;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.views.navigator.ResourceComparator;

import avicit.platform6.tools.codegeneration.core.entity.BusinessObjectProperty;
import avicit.platform6.tools.codegeneration.log.AvicitLogger;
import avicit.platform6.tools.codegeneration.util.HbmParser;
import avicit.platform6.tools.codegeneration.util.MyEntityResolver;

/**
 * 
 * 作者：dingrc 邮箱：dingrc@avicit.com 创建时间：2012-12-10 上午09:12:34
 * 
 * 类说明：选择需要运行代码生成向导的工程 修改记录：
 */
@SuppressWarnings("restriction")
public class ProjectSelectPage extends BaseWizardPage {
	// 初始selection
	private IStructuredSelection selection;
	// 初始化selection中选中的包
	private IPackageFragment currPackage;
	// 可选工程列表
	private List<IProject> projects;
	// 选中的工程
	private IProject project;
	// 工程树
	private TreeViewer treeProjects;
	// 是否生成hbm文件
	private Button chkHbm;
	// hbm文件选择控件
	protected StringButtonDialogField fieldHbm;
	// hbm文件
	private IFile fileHbm;

	public ProjectSelectPage(String name, IStructuredSelection selection) {
		super(name);
		this.selection = selection;
		setTitle("工程选择");
		setDescription("请选择需要运行代码生成向导的工程");
		// 初始化所有工程
		projects = loadProjects();
	}

	@Override
	public boolean canFlipToNextPage() {
		this.getRealWizard().setProject(project);
		
		return project != null;

	}

	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		container.setLayout(layout);

		createProjectsViewer(container);

//		createOtherControls(container);

		Group mavenGroup = new Group(container, SWT.NONE);
		GridLayout gdLayout = new GridLayout(2, true);
		gdLayout.marginHeight = 20;
		mavenGroup.setLayout(gdLayout);
		mavenGroup.setText("是否为Maven项目?");
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.verticalSpan = 2;
//		mavenGroup.setBounds(10, 309, 554, 100);
		mavenGroup.setLayoutData(gd);

		final CodeGenerationWizard codeGenerationWizard = getRealWizard();
		final Button[] buttons = new Button[2];
		buttons[0] = new Button(mavenGroup, SWT.RADIO);
		buttons[0].setText("是");
		GridData data = new GridData(SWT.BEGINNING, SWT.CENTER, true, true);
		buttons[0].setSize(200, 200);
		buttons[0].setLayoutData(data);
		buttons[0].setSelection(false);
		buttons[0].addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if(buttons[0].getSelection()){
					codeGenerationWizard.setManve(true);
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		buttons[1] = new Button(mavenGroup, SWT.RADIO);
		buttons[1].setText("否");
		data = new GridData(SWT.BEGINNING, SWT.CENTER, true, true);
		buttons[1].setSize(200, 200);
		buttons[1].setLayoutData(data);
		buttons[1].setSelection(true);
		buttons[1].addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if(buttons[1].getSelection()){
					codeGenerationWizard.setManve(false);
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		initialize();

		setControl(container);
	}

	/**
	 * @param container
	 */
	private void createOtherControls(Composite container) {
//		chkHbm = new Button(container, SWT.CHECK);
//		chkHbm.setText("根据现有hbm文件新建模块");
//		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
//		gd.horizontalSpan = 3;
//		chkHbm.setLayoutData(gd);
//		chkHbm.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				resetDialogField();
//				setPageComplete(canFlipToNextPage());
//			}
//		});

		fieldHbm = new StringButtonDialogField(createHbmAdapter());
		fieldHbm.setLabelText("hbm文件：");
		fieldHbm.setButtonLabel("请选择");
		fieldHbm.setEnabled(false);
		fieldHbm.doFillIntoGrid(container, 3);
		Text txtHbm = fieldHbm.getTextControl(null);
		txtHbm.setEditable(false);
		LayoutUtil.setWidthHint(txtHbm, getMaxFieldWidth());
		LayoutUtil.setHorizontalGrabbing(txtHbm);
	}

	/**
	 * @param container
	 */
	private void createProjectsViewer(Composite container) {
//		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
//		parent_.setLayoutData(data);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.horizontalSpan = 3;
		Group group = new Group(container, SWT.NONE);
		group.setText("工程列表");
		group.setLayoutData(gd);
		group.setLayout(new FillLayout());
		treeProjects = new TreeViewer(group, SWT.NULL);
		// listViewer.setLabelProvider(WorkbenchLabelProvider.getDecoratingWorkbenchLabelProvider());
		treeProjects.setLabelProvider(createTreeLabelProvider());
		treeProjects.setContentProvider(createTreeContentProvider());
		treeProjects.setInput(projects);
		treeProjects
				.addSelectionChangedListener(new ISelectionChangedListener() {
					public void selectionChanged(SelectionChangedEvent event) {
						ISelection selection = event.getSelection();
						if (!selection.isEmpty()) {
							IStructuredSelection sel = (IStructuredSelection) selection;
							project = (IProject) sel.getFirstElement();
						}

						setPageComplete(canFlipToNextPage());
					}
				});
	}

	private ILabelProvider createTreeLabelProvider() {
		return new LabelProvider() {
			@Override
			public String getText(Object element) {
				// IWorkbenchAdapter adapter = (IWorkbenchAdapter) Platform
				// .getAdapterManager().getAdapter(element,
				// IWorkbenchAdapter.class);
				// return adapter.getLabel(element);
				if (element instanceof IProject) {
					IProject pro = (IProject) element;
					return pro.getName();
				}
				return super.getText(element);
			}

			@Override
			public Image getImage(Object element) {
				// JavaElementImageProvider.getDecoratedImage(JavaPluginImages.DESC_MISC_PUBLIC,
				// 0, JavaElementImageProvider.SMALL_SIZE);
				// JavaUI.getSharedImages().getImage(org.eclipse.jdt.ui.ISharedImages.IMG_OBJS_JAR);
				Image image = PlatformUI
						.getWorkbench()
						.getSharedImages()
						.getImage(
								org.eclipse.ui.ide.IDE.SharedImages.IMG_OBJ_PROJECT);
				return image;
			}
		};
	}

	private IContentProvider createTreeContentProvider() {
		return new WorkbenchContentProvider() {
			@SuppressWarnings("unchecked")
			@Override
			public Object[] getElements(Object inputElement) {
				if (inputElement instanceof List) {
					return ((List) inputElement).toArray();
				}
				return new Object[0];
			}

			@Override
			public boolean hasChildren(Object element) {
				return false;
			}
		};
	}
	
	/**
	 * 获得工作空间中的所有工程列表
	 * @return
	 */
	protected List<IProject> loadProjects() {
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot()
				.getProjects();
		List<IProject> pros = new ArrayList<IProject>();
		for (IProject pro : projects) {
			if (pro.isAccessible()) {
				pros.add(pro);
			}
		}
		return pros;
	}

	private void initialize() {
		if (selection != null && !selection.isEmpty()) {
			Object obj = selection.getFirstElement();
//			obj = (Project) ((AbstractFolderNode)obj).getResource().getResource().getProject();	
//			AbstractFolderNode ls = null;
			if (obj instanceof IJavaProject) {
				IJavaProject pro = (IJavaProject) obj;
				project = pro.getProject();
			} else if(obj instanceof Project){
				this.project = (IProject) obj;
			}else {
				IResource resource = null;
				if (obj instanceof IResource) {
					resource = (IResource) obj;
				} else if (obj instanceof IJavaElement) {
					resource = ((IJavaElement) obj).getResource();
				} else if (obj instanceof IAdaptable) {
					IAdaptable adaptable = (IAdaptable) obj;
					resource = (IResource) adaptable
							.getAdapter(org.eclipse.core.resources.IResource.class);
				} 
				if (resource != null) {
					project = resource.getProject();
				}
			}

			if (project != null) {
				if (project.isOpen()) {
					treeProjects.getTree().setSelection(
							treeProjects.getTree().getItem(
									projects.indexOf(project)));
				} else {
					project = null;
				}

			}
			// 如果用户在运行向导之前鼠标已经选中某个包，则把这个包找出来
			IJavaElement elem = getInitialJavaElement(selection);
			if (elem != null) {
				// evaluate the enclosing type
				currPackage = (IPackageFragment) elem
						.getAncestor(IJavaElement.PACKAGE_FRAGMENT);
			}
		} else {
			// 如果只有一个工程，则默认选中；如果是多个工程，交由用户自己去选择
			if (projects.size() == 1) {
				treeProjects.getTree().setSelection(
						treeProjects.getTree().getItem(0));

			}
		}
		// 临终前触发一下选中事件
		treeProjects.getTree().notifyListeners(SWT.Selection, new Event());
	}

	/**
	 * Utility method to inspect a selection to find a Java element.
	 * 
	 * @param selection
	 *            the selection to be inspected
	 * @return a Java element to be used as the initial selection, or
	 *         <code>null</code>, if no Java element exists in the given
	 *         selection
	 */
	protected IJavaElement getInitialJavaElement(IStructuredSelection selection) {
		IJavaElement jelem = null;
		if (selection != null && !selection.isEmpty()) {
			Object selectedElement = selection.getFirstElement();
			if (selectedElement instanceof IAdaptable) {
				IAdaptable adaptable = (IAdaptable) selectedElement;

				jelem = (IJavaElement) adaptable.getAdapter(IJavaElement.class);
				if (jelem == null) {
					IResource resource = (IResource) adaptable
							.getAdapter(IResource.class);
					if (resource != null
							&& resource.getType() != IResource.ROOT) {
						while (jelem == null
								&& resource.getType() != IResource.PROJECT) {
							resource = resource.getParent();
							jelem = (IJavaElement) resource
									.getAdapter(IJavaElement.class);
						}
						if (jelem == null) {
							jelem = JavaCore.create(resource); // java project
						}
					}
				}
			}
		}
		/*
		 * if (jelem == null) { IWorkbenchPart part =
		 * JavaPlugin.getActivePage().getActivePart(); if (part instanceof
		 * ContentOutline) { part =
		 * JavaPlugin.getActivePage().getActiveEditor(); }
		 * 
		 * if (part instanceof IViewPartInputProvider) { Object elem =
		 * ((IViewPartInputProvider) part) .getViewPartInput(); if (elem
		 * instanceof IJavaElement) { jelem = (IJavaElement) elem; } } }
		 * 
		 * if (jelem == null || jelem.getElementType() ==
		 * IJavaElement.JAVA_MODEL) { try { IJavaProject[] projects =
		 * JavaCore.create( ResourcesPlugin.getWorkspace().getRoot())
		 * .getJavaProjects(); if (projects.length == 1) { jelem = projects[0];
		 * } } catch (JavaModelException e) { JavaPlugin.log(e); } }
		 */
		return jelem;
	}

	public IPackageFragment getCurrPackage() {

		return currPackage;
	}

	private void resetDialogField() {
		fieldHbm.setEnabled(chkHbm.getSelection());
		if (!fieldHbm.isEnabled()) {
			fieldHbm.setText("");
			fileHbm = null;
		}
	}

	private IStringButtonAdapter createHbmAdapter() {
		return new IStringButtonAdapter() {
			public void changeControlPressed(DialogField field) {
				// FileFolderSelectionDialog FilteredResourcesSelectionDialog
				// ResourceListSelectionDialog
				ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(
						getShell(), new WorkbenchLabelProvider(),
						new WorkbenchContentProvider());
				dialog.setTitle("文件选择");
				dialog.setMessage("请选择hbm文件");
				dialog.setAllowMultiple(false);
				dialog.addFilter(new ViewerFilter() {
					@Override
					public boolean select(Viewer viewer, Object parentElement,
							Object element) {
						if (element instanceof IFile) {
							IFile file = (IFile) element;
							return file.getName().endsWith(".hbm.xml");
						}
						return true;
					}
				});
				dialog.setComparator(new ResourceComparator(
						ResourceComparator.NAME));

				dialog.setValidator(new ISelectionStatusValidator() {
					public IStatus validate(Object[] selection) {
						int nSelected = selection.length;
						if (nSelected == 1) {
							Object obj = selection[0];
							if (obj instanceof IFile) {
								return Status.OK_STATUS;
							}
						}
						return new StatusInfo(IStatus.ERROR, null);
					}
				});
				dialog.setInput(project);
				if (dialog.open() == Window.OK) {
					fileHbm = (IFile) dialog.getFirstResult();
					fieldHbm.setText(fileHbm.getFullPath().toString());
				}
				setPageComplete(canFlipToNextPage());
			}
		};
	}

	/**
	 * @return
	 */
	private IWizardPage parseHbm4Next() {
		try {
			SAXReader reader = new SAXReader();
			reader.setEntityResolver(new MyEntityResolver());
			// 在读文件时把 dtd 验证去掉 ,不仅使消除异常,而且功能运行时间从十几分钟缩短至 几秒.
			reader.setFeature(
					"http://apache.org/xml/features/nonvalidating/load-external-dtd",
					false);
			Document doc = reader.read(fileHbm.getContents());
			Element rootElement = doc.getRootElement();
			Element classElement = rootElement.element("class");
			CodeGenerationWizard wizard = getRealWizard();
			java.util.List tables = new ArrayList();
			tables.add(classElement.attributeValue("table"));
			wizard.setMainTableNames(tables);
			String className = classElement.attributeValue("name");
			wizard.setBizObject(className);
			HbmParser parser = new HbmParser();

			// 先判断同级目录是否有java文件，没有则生成
			IPath output = fileHbm.getLocation().removeLastSegments(
					className.split("\\.").length);
			IFolder javaFolder = getWorkspaceRoot().getFolder(
					fileHbm.getFullPath().removeLastSegments(1));
			String javaFilename = fileHbm.getName().replace("hbm.xml", "java");
			if (!javaFolder.getFile(javaFilename).exists()) {
				List<String> args = new ArrayList<String>();
				args.add("--output=" + output.toOSString());
				args.add(fileHbm.getLocation().toOSString());
				CodeGenerator.main((String[]) args.toArray(new String[args
						.size()]));
				// 刷新工程
				javaFolder.refreshLocal(IResource.DEPTH_INFINITE, null);
				parser.parseJava(javaFolder.getFile(javaFilename));
			}

			List<BusinessObjectProperty> boList = new ArrayList<BusinessObjectProperty>();
			parser.parseHbm(boList, doc, new HashMap<String, String>());
			wizard.getTableBusinessObject().setBoList(boList);
			wizard.getTableBusinessObject().setTableName(
					classElement.attributeValue("table"));
			return wizard.templeteSelectPage;
		} catch (Exception e) {
			AvicitLogger.openError("解析hbm文件出错！", e);
		}
		return null;
	}

	@SuppressWarnings("restriction")
	@Override
	public IWizardPage getNextPage() {
		CodeGenerationWizard wizard = getRealWizard();
		wizard.setProject(project);
		// 如果是根据现有hibernate配置文件生成模块
		if (chkHbm !=null && chkHbm.getSelection()) {
			wizard.setCodeGenerationWithHbm(true);
			return parseHbm4Next();
		} else {
			wizard.setCodeGenerationWithHbm(false);
		}
		//IProject ip = wizard.getProject();
		
		// 是否maven工程
		try {
			IProjectDescription ipd = project.getDescription();
			java.util.List<String> projectType = Arrays.asList(ipd.getNatureIds());
//			if (projectType.contains(BusinessObjectConfigPage.getResourceBundle().getString(Resource.MAVEN))) {
//				wizard.setManve(true);
			if(wizard.getManve()){
//				wizard.templeteSelectPage.setPathConstant("src/main/resources/");
//				wizard.templeteSelectPage.getWebPath().setText("src/main/resources/");
			}else{
//				wizard.templeteSelectPage.setPathConstant("web/");
//				wizard.templeteSelectPage.getWebPath().setText("web/");
				// 2015-03-13
//				wizard.templeteSelectPage.setPathConstant("WebRoot/");
//				wizard.templeteSelectPage.getWebPath().setText("WebRoot/");	
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return wizard.gridpage;
	}
}
