package avicit.ui.runtime.core.action;

import java.util.HashMap;

import org.eclipse.core.internal.resources.Project;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.actions.SelectionListenerAction;

import avicit.ui.runtime.core.cluster.FunctionClusterNode;
import avicit.ui.runtime.core.cluster.function.ComponentNode;
import avicit.ui.runtime.core.node.AbstractNode;
import avicit.ui.runtime.core.node.ProjectNode;
import avicit.ui.runtime.core.subsystem.SubSystemNode;

/**
 * @author Tao Tao
 *
 */
@SuppressWarnings("restriction")
public class DeleteFolderAction extends SelectionListenerAction {
	private String path;
	private Project obj;
	private String type;
	public HashMap<String, String> map;
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

	public void SetInfo() {}

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

	public DeleteFolderAction(String text){
		super(text);
	}

	public DeleteFolderAction(String text, Shell shell, int flag) {
		super(text);
		this.shell = shell;
		this.flag = flag;
		this.init();
	}

	public DeleteFolderAction(String text, Project obj, int flag) {
		super(text);
		this.obj = obj;
		this.shell = shell;
		this.flag = flag;
		this.init();
	}

	public void run() {
		final Object obj = getStructuredSelection().getFirstElement();
		AbstractNode subNode;
		AbstractNode subSysNode;

		if (obj == null)
			return;

		if(obj instanceof SubSystemNode) {
			subNode = (SubSystemNode) obj;
			subSysNode = (AbstractNode) ((SubSystemNode)subNode).getParentNode();
			DeleteFolderJob deleteJob = new DeleteFolderJob(new String("Deleting SubSystemNode..."), this.obj, subNode, subSysNode);
			deleteJob.setUser(true);
			deleteJob.schedule();
		}

		if (obj instanceof ComponentNode) {
			subNode = (ComponentNode) obj;
			subSysNode = (AbstractNode) ((ComponentNode)subNode).getParentNode();
			DeleteFolderJob deleteJob = new DeleteFolderJob(new String("Deleting ComponentNode..."), this.obj, subNode, subSysNode);
			deleteJob.setUser(true);
			deleteJob.schedule();
		}

		if (obj instanceof FunctionClusterNode){
			subNode = (FunctionClusterNode) obj;
			subSysNode = (AbstractNode) ((FunctionClusterNode)subNode).getParentNode();
			DeleteFolderJob deleteJob = new DeleteFolderJob(new String("Deleting FunctionClusterNode..."), this.obj, subNode, subSysNode);
			deleteJob.setUser(true);
			deleteJob.schedule();
		}

		super.run();
	}

	class DeleteFolderJob extends Job {
		private AbstractNode subNode;
		private AbstractNode subParentNode;
		private IProject project;

		public DeleteFolderJob(String message, IProject project, AbstractNode subNode, AbstractNode subParentNode) {
			super(message);
			this.subNode = subNode;
			this.subParentNode = subParentNode;
			this.project = project;
		}

		// Modified by Tao Tao 删除节点时采用独立线程，并给删除节点增加Role，以防资源冲突。
		@Override
		protected IStatus run(IProgressMonitor monitor) {
			try {
				String nodePath = subNode.getResource().getProjectRelativePath();
				String parentNodePath = null;

				if(!(subParentNode instanceof ProjectNode)){
					parentNodePath = subParentNode.getResource().getProjectRelativePath().toString();
				}else{
					parentNodePath = project.getFolder("src").getFolder("avicit").getProjectRelativePath().toString();
				}

				IFolder folder1 = project.getFolder(new Path(nodePath));
				// Add by Tao Tao 增加删除节点树， 后期仍需考虑是否增加资源Rule，防止资源冲突。
				if(folder1.exists()){
					while (!folder1.getProjectRelativePath().toString().equals(parentNodePath)
							&& !(folder1.getParent() instanceof Project)) {
						folder1.delete(true, true, new NullProgressMonitor());

						folder1 = (IFolder) folder1.getParent();
					}
				}

				IFolder folder2 = project.getFolder("WebRoot").getFolder("avicit").getFolder(subNode.getResource().getProjectRelativePath().substring(11));
				IFolder parentFolder2 = null;

				if(!(subParentNode instanceof ProjectNode))
				{
					parentFolder2 = project.getFolder("WebRoot").getFolder(parentNodePath.substring(4));
				}else{
					parentFolder2 = project.getFolder("WebRoot").getFolder("avicit");
				}

				//删除WebRoot下普通项目文件
				if(folder2.exists()){
					while (!folder2.getProjectRelativePath().toString().equals(parentFolder2.getProjectRelativePath().toString())
							&& !(folder2.getParent() instanceof Project)) {
						folder2.delete(true, true, new NullProgressMonitor());

						folder2 = (IFolder) folder2.getParent();
					}
				}

				//删除WebRoot下Maven项目文件
				IFolder folder3 = project.getFolder("src").getFolder("main").getFolder("java").getFolder(nodePath.substring(4));
				IFolder parentFolder3 = null;

				if(!(subParentNode instanceof ProjectNode)){
					parentFolder3 = project.getFolder("src").getFolder("main").getFolder("java").getFolder(parentNodePath.substring(4));
				}else{
					parentFolder3 = project.getFolder("src").getFolder("main").getFolder("java").getFolder("avicit");
				}

				if(folder3.exists()){
					while (!folder3.getProjectRelativePath().toString().equals(parentFolder3.getProjectRelativePath().toString())
							&& !(folder3.getParent() instanceof Project)) {
						folder3.delete(true, true, new NullProgressMonitor());

						folder3 = (IFolder) folder3.getParent();
					}
				}

				//删除WebRoot下Maven项目文件
				IFolder folder4 = project.getFolder("src").getFolder("main").getFolder("resources").getFolder(nodePath.substring(4));
				IFolder parentFolder4 = null;

				if(!(subParentNode instanceof ProjectNode)){
					parentFolder4 = project.getFolder("src").getFolder("main").getFolder("resources").getFolder(parentNodePath.substring(4));
				}else{
					parentFolder4 = project.getFolder("src").getFolder("main").getFolder("resources").getFolder("avicit");
				}

				if(folder4.exists()){
					while (!folder4.getProjectRelativePath().toString().equals(parentFolder4.getProjectRelativePath().toString())
							&& !(folder4.getParent() instanceof Project)) {
						folder4.delete(true, true, new NullProgressMonitor());

						folder4 = (IFolder) folder4.getParent();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return Status.OK_STATUS;
		}
	}

	protected boolean updateSelection(IStructuredSelection selection) {
		boolean flag = super.updateSelection(selection);
		if (flag)
			flag = getStructuredSelection().toList().size() == 1;
		return flag;
	}

	@SuppressWarnings("unused")
	private boolean checkPackageName(String path){
		boolean flag = false;
		flag = (path.indexOf('.')!=-1||path.indexOf('\\')!=-1) ? false : true;
		return flag;
	}	

	public void validate() {

	}

	public void showMessage(String content) {
		MessageDialog.openInformation(Display.getDefault().getActiveShell(),
				"信息", content);
	}
}