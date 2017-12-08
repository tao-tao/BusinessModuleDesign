package avicit.ui.runtime.core.action;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.actions.SelectionListenerAction;

import avicit.ui.runtime.core.node.AbstractFolderNode;
import avicit.ui.runtime.core.node.AbstractResourceNode;
import avicit.ui.runtime.core.node.PackageNode;
import avicit.ui.view.navigator.views.AvicitProjectNavigator;
import avicit.ui.view.navigator.views.AvicitProjectViewer;

/**
 * @author Tao Tao
 *
 */
public class DeletePackageAction extends SelectionListenerAction {

	public DeletePackageAction(String text) {
		super(text);
	}

	public void run(){
		final Object obj = this.getStructuredSelection().getFirstElement();

		if(obj ==null)
			return;

		if(obj instanceof PackageNode){
			final IProject project = ((AbstractFolderNode)obj).getResource().getResource().getProject();
			final PackageNode node = (PackageNode)obj;

			final String nodePath = node.getResource().getProjectRelativePath();

			final Job job = new Job("Delete Packages"){

				@Override
				protected IStatus run(IProgressMonitor monitor) {
					try {
						IFolder folder = project.getFolder(nodePath);

						folder.delete(true, true, null);

						Display display = Display.getDefault();
						final AvicitProjectViewer viewer = AvicitProjectNavigator.getViewer();

						if (display != null)
							display.asyncExec(new Runnable() {
								public void run() {
									List<IResource> refreshList = new ArrayList<IResource>();
									IResource resource = ((AbstractResourceNode) node.getParent()).getResource().getResource();
									refreshList.add(resource);
									viewer.getResourceMapper().refresh(false, refreshList, refreshList, 500);
								}
							});
					} catch (Exception e) {
						e.printStackTrace();
					}

					return Status.OK_STATUS;
				}
			};
			job.setUser(true);
			job.schedule();
		}
	}
}
