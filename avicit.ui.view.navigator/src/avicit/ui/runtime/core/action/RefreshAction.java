package avicit.ui.runtime.core.action;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.filesystem.IFileInfo;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceRuleFactory;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.MultiRule;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;
import org.eclipse.ui.internal.ide.dialogs.IDEResourceInfoUtils;

import avicit.operation.DiagramErrors;
import avicit.operation.EcOperation;
import avicit.operation.Manager;

public class RefreshAction extends WorkspaceAction implements EcOperation {
	
	public RefreshAction() {
		super(IDEWorkbenchMessages.RefreshAction_text);
		setToolTipText(IDEWorkbenchMessages.RefreshAction_toolTip);
		setId("org.eclipse.ui.RefreshAction");
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this, "org.eclipse.ui.ide.refresh_action_context");
	
	}
	public void add(){
		Manager.getInstance().addr(this);
	}

	void checkLocationDeleted(IProject project) throws CoreException {
		if (!project.exists())
			return;
		IFileInfo location = IDEResourceInfoUtils.getFileInfo(project.getLocationURI());
		/*if (!location.exists()) {
			String message = NLS.bind(IDEWorkbenchMessages.RefreshAction_locationDeletedMessage, project.getName(), location.toString());
			final MessageDialog dialog = new MessageDialog(getShell(), IDEWorkbenchMessages.RefreshAction_dialogTitle, null, message, 3, new String[] { IDialogConstants.YES_LABEL, IDialogConstants.NO_LABEL }, 0);
			getShell().getDisplay().syncExec(new Runnable() {
				public void run() {
					dialog.open();
				}
			});
			if (dialog.getReturnCode() == 0)
				project.delete(true, true, null);
		}*/
	}

	protected String getOperationMessage() {
		return IDEWorkbenchMessages.RefreshAction_progressMessage;
	}

	protected String getProblemsMessage() {
		return IDEWorkbenchMessages.RefreshAction_problemMessage;
	}

	protected String getProblemsTitle() {
		return IDEWorkbenchMessages.RefreshAction_problemTitle;
	}

	protected List getSelectedResources() {
		List resources = super.getSelectedResources();
		if (resources.isEmpty()) {
			resources = new ArrayList();
			resources.add(ResourcesPlugin.getWorkspace().getRoot());
		}
		return resources;
	}

	protected void invokeOperation(IResource resource, IProgressMonitor monitor) throws CoreException {
		if (resource.getType() == 4)
			checkLocationDeleted((IProject) resource);
		else if (resource.getType() == 8) {
			IProject projects[] = ((IWorkspaceRoot) resource).getProjects();
			for (int i = 0; i < projects.length; i++)
				checkLocationDeleted(projects[i]);
		}
		resource.refreshLocal(2, monitor);
	}

	protected boolean updateSelection(IStructuredSelection s) {
        return (super.updateSelection(s) || s.isEmpty()) && getSelectedNonResources().size() == 0;
	}

	public void handleKeyReleased(KeyEvent event) {
		if (event.keyCode == 0x100000e && event.stateMask == 0)
			refreshAll();
	}

	public void refreshAll() {
		IStructuredSelection currentSelection = getStructuredSelection();
		selectionChanged(StructuredSelection.EMPTY);
		run();
		selectionChanged(currentSelection);
	}

	public void run() {/*
		org.eclipse.core.runtime.jobs.ISchedulingRule rule = null;
		IResourceRuleFactory factory = ResourcesPlugin.getWorkspace().getRuleFactory();
		for (Iterator resources = getSelectedResources().iterator(); resources.hasNext();)
			rule = MultiRule.combine(rule, factory.refreshRule((IResource) resources.next()));
		runInBackground(rule);
		final ProjectViewer viewer = ProjectNavigator.getViewer();
		if (SwtResourceUtil.isValid(viewer.getTree())) {
			Display display = Display.getDefault();
			if (display != null)
				display.asyncExec(new Runnable() {
					public void run() {
						if(getSelectedResources().size()>0)
						{
							List refreshList = new ArrayList();
							refreshList.addAll(getSelectedResources());
							viewer.getResourceMapper().refresh(false, refreshList, refreshList, 0);
						}
					}
				});
		}
	*/}

	@Override
	protected void invokeOperation(IStructuredSelection selection, IProgressMonitor iprogressmonitor) throws CoreException {
		if(selection==null){
			return;
		}
		Iterator itor = selection.iterator();
		while(itor.hasNext())
		{
			Object obj = itor.next();
			
		}
	}

	@Override
	public boolean delete(IProject project, String filePath) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean rename(IProject project, String fileName, String filePath,
			String newName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean copy(IProject project, String filePath, String newPackage) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void validator(DiagramErrors errors, IFile file) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		this.refreshAll();
		this.run();
	}

}