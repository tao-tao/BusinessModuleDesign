package avicit.ui.runtime.core.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.internal.resources.Project;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.SelectionListenerAction;

import avicit.platform6.tools.codegeneration.wizard.CodeGenerationWizard;
import avicit.ui.runtime.core.action.NewComponentAction.UpdateComponentDialog;
import avicit.ui.runtime.core.cluster.FunctionClusterNode;
import avicit.ui.runtime.core.node.AbstractFolderNode;
import avicit.ui.runtime.core.node.ProjectNode;
import avicit.ui.runtime.core.subsystem.SubSystemNode;
import avicit.ui.view.navigator.views.AvicitProjectNavigator;
import avicit.ui.view.navigator.views.AvicitProjectViewer;

@SuppressWarnings("restriction")
public class NewCodeGenerationAction extends SelectionListenerAction {

	public NewCodeGenerationAction(String text) {
		super(text);
	}
	

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

	public void run() {
		Object obj = getStructuredSelection().getFirstElement();
		Object proObj = null;
		if (obj == null)
			return;
		if(obj instanceof SubSystemNode || obj instanceof FunctionClusterNode){
//			((SubSystemNode) obj).getParentNode();
			proObj = (Project) ((AbstractFolderNode)obj).getResource().getResource().getProject();	
		}
			
		CodeGenerationWizard wizard = new CodeGenerationWizard();
		if(obj instanceof SubSystemNode){
			wizard.setParentPackPath(((SubSystemNode)obj).getPath());
		}
		if(obj instanceof FunctionClusterNode){
			wizard.setParentPackPath(((FunctionClusterNode)obj).getPath());
		}
		wizard.init(PlatformUI.getWorkbench(), new StructuredSelection(obj));
		wizard.setSelectionForProject(new StructuredSelection(proObj));
		WizardDialog dialog = new WizardDialog(Display.getCurrent().getActiveShell(), wizard);
		dialog.create();
		int result = dialog.open();
		if (result == Dialog.OK) {
			final AbstractFolderNode folderNode = (AbstractFolderNode)obj;
//			final PackageNode childNode = wizard.getChild();
//			childNode.setType(folderNode.getType());
//			childNode.setParent(folderNode);
//			if(null == folderNode.getChildren()){
//				folderNode.setChildren(new ArrayList());
//			}
//			folderNode.getChildren().add(childNode);
			final AvicitProjectViewer viewer = AvicitProjectNavigator.getViewer();
//			try {
//				folderNode.getResource().getResource().getProject().refreshLocal(IResource.DEPTH_INFINITE, null);
//			} catch (CoreException e) {
//				e.printStackTrace();//日志待处理2015-03-07
//			}
//			if (SwtResourceUtil.isValid(viewer.getTree())) {
			if(true){	
				Display display = Display.getDefault();
				if (display != null)
					display.asyncExec(new Runnable() {
						public void run() {
//							viewer.add(folderNode, childNode);
							List refreshList = new ArrayList();
							refreshList.add(folderNode.getResource().getResource().getProject());
//							refreshList.add(childNode.getResource().getResource());
							viewer.getResourceMapper().refresh(false, refreshList, refreshList, 500);
						}
					});
			}
		}
	}

	protected boolean updateSelection(IStructuredSelection selection) {
		boolean flag = super.updateSelection(selection);
		if (flag)
			flag = getStructuredSelection().toList().size() == 1;
		return flag;
	}


	public void validate() {

	}
	public static String createInitial(String name, String type)
			throws JavaModelException {

		StringBuffer buffer = new StringBuffer();
//		buffer.append("component.id=" + vo.id).append('\n');
		buffer.append("component.name=" + name).append('\n');
//		buffer.append("component.version=" + vo.version).append('\n');
//		buffer.append("component.type=" + vo.type).append('\n');
//		buffer.append("component.modules=" + vo.modules).append('\n');
//		buffer.append("component.service=" + vo.service).append('\n');
//		buffer.append("component.dao=" + vo.dao).append('\n');
//		buffer.append("component.auto=" + vo.auto).append('\n');
		if ("Comp".equals(type)) {
			buffer.append("component.iscomp=" + true).append('\n');
		} else {
			buffer.append("component.iscomp=" + false).append('\n');
		}

//		buffer.append("component.dtype=" + vo.dialogTitle).append('\n');
		return buffer.toString();
	}

	public void showMessage(String content) {
		MessageDialog.openInformation(Display.getDefault().getActiveShell(),
				"信息", content);
	}
	

}
