package avicit.ui.runtime.core.action;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.actions.SelectionListenerAction;

import avicit.ui.runtime.core.node.AbstractFileNode;
import avicit.ui.runtime.core.node.AbstractFolderNode;
import avicit.ui.view.navigator.views.AvicitProjectNavigator;
import avicit.ui.view.navigator.views.AvicitProjectViewer;

/**
 * @author Tao Tao
 *
 */
public class DeleteFileAction extends SelectionListenerAction {

	public DeleteFileAction(String text) {
		super(text);
	}

	public void run(){
		final Object obj = this.getStructuredSelection().getFirstElement();
		if(obj ==null)
			return;
		if(obj instanceof AbstractFileNode){
			final IProject project = ((AbstractFileNode)obj).getResource().getResource().getProject();
			final AbstractFileNode node = (AbstractFileNode)obj;

			final String nodePath = node.getResource().getProjectRelativePath();

			final IResource parentNode = ((AbstractFolderNode)node.getParent()).getResource().getResource();

			final Job job = new Job("Delete Files"){

				@Override
				protected IStatus run(IProgressMonitor monitor) {
					try{
						IFile file = project.getFile(nodePath);

						file.delete(true, true, null);

						Display display = Display.getDefault();
						final AvicitProjectViewer viewer = AvicitProjectNavigator.getViewer();

						if (display != null)
							display.asyncExec(new Runnable() {
								public void run() {
									List refreshList = new ArrayList();
									refreshList.add(parentNode);
									viewer.getResourceMapper().refresh(false, refreshList, refreshList, 500);
								}
							});
						}catch( Exception e ){
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
