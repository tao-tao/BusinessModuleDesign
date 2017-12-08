package avicit.ui.runtime.core.action;

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.internal.resources.File;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.SelectionListenerAction;
import org.eclipse.ui.ide.undo.DeleteResourcesOperation;
import org.eclipse.ui.ide.undo.WorkspaceUndoUtil;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;
import org.eclipse.ui.progress.WorkbenchJob;

import avicit.operation.EcOperationAdapater;
import avicit.ui.runtime.core.node.AbstractResourceNode;
import avicit.ui.runtime.core.node.PackageNode;

@SuppressWarnings("restriction")
public class DeleteResourceAction extends SelectionListenerAction {

	static class DeleteProjectDialog extends MessageDialog {

		static String getTitle(IResource projects[]) {
			if (projects.length == 1)
				return IDEWorkbenchMessages.DeleteResourceAction_titleProject1;
			else
				return IDEWorkbenchMessages.DeleteResourceAction_titleProjectN;
		}

		static String getMessage(IResource projects[]) {
			if (projects.length == 1) {
				IProject project = (IProject) projects[0];
				return NLS
						.bind(IDEWorkbenchMessages.DeleteResourceAction_confirmProject1,
								project.getName());
			} else {
				return NLS
						.bind(IDEWorkbenchMessages.DeleteResourceAction_confirmProjectN,
								new Integer(projects.length));
			}
		}

		protected void configureShell(Shell newShell) {
			super.configureShell(newShell);
			PlatformUI
					.getWorkbench()
					.getHelpSystem()
					.setHelp(newShell,
							"org.eclipse.ui.ide.delete_project_dialog_context");
		}

		protected Control createCustomArea(Composite parent) {
			Composite composite = new Composite(parent, 0);
			composite.setLayout(new GridLayout());
			radio1 = new Button(composite, 16);
			radio1.addSelectionListener(selectionListener);
			String text1;
			if (projects.length == 1) {
				IProject project = (IProject) projects[0];
				if (project == null || project.getLocation() == null)
					text1 = IDEWorkbenchMessages.DeleteResourceAction_deleteContentsN;
				else
					text1 = NLS
							.bind(IDEWorkbenchMessages.DeleteResourceAction_deleteContents1,
									project.getLocation().toOSString());
			} else {
				text1 = IDEWorkbenchMessages.DeleteResourceAction_deleteContentsN;
			}
			radio1.setText(text1);
			radio1.setFont(parent.getFont());
			Label detailsLabel = new Label(composite, 16384);
			detailsLabel
					.setText(IDEWorkbenchMessages.DeleteResourceAction_deleteContentsDetails);
			detailsLabel.setFont(parent.getFont());
			GC gc = new GC(detailsLabel);
			gc.setFont(detailsLabel.getParent().getFont());
			org.eclipse.swt.graphics.FontMetrics fontMetrics = gc
					.getFontMetrics();
			gc.dispose();
			GridData data = new GridData();
			data.horizontalIndent = Dialog.convertHorizontalDLUsToPixels(
					fontMetrics, 21);
			detailsLabel.setLayoutData(data);
			detailsLabel.addMouseListener(new MouseAdapter() {

				public void mouseUp(MouseEvent e) {
					deleteContent = true;
					radio1.setSelection(deleteContent);
					radio2.setSelection(!deleteContent);
				}

			});
			new Label(composite, 16384);
			radio2 = new Button(composite, 16);
			radio2.addSelectionListener(selectionListener);
			String text2 = IDEWorkbenchMessages.DeleteResourceAction_doNotDeleteContents;
			radio2.setText(text2);
			radio2.setFont(parent.getFont());
			radio1.setSelection(deleteContent);
			radio2.setSelection(!deleteContent);
			return composite;
		}

		boolean getDeleteContent() {
			return deleteContent;
		}

		public int open() {
			if (fIsTesting) {
				deleteContent = true;
				return 0;
			} else {
				return super.open();
			}
		}

		void setTestingMode(boolean t) {
			fIsTesting = t;
		}

		private IResource projects[];
		private boolean deleteContent;
		private boolean fIsTesting;
		private Button radio1;
		private Button radio2;
		private SelectionListener selectionListener;

		DeleteProjectDialog(Shell parentShell, IResource projects[]) {
			super(parentShell, getTitle(projects), null, getMessage(projects),
					3, new String[] { IDialogConstants.YES_LABEL,
							IDialogConstants.NO_LABEL }, 0);
			deleteContent = false;
			fIsTesting = false;
			selectionListener = new SelectionAdapter() {

				public void widgetSelected(SelectionEvent e) {
					Button button = (Button) e.widget;
					if (button.getSelection())
						deleteContent = button == radio1;
				}

			};
			this.projects = projects;
		}
	}

	public DeleteResourceAction(Shell shell) {
		super(IDEWorkbenchMessages.DeleteResourceAction_text);
		deleteContent = false;
		fTestingMode = false;
		setToolTipText(IDEWorkbenchMessages.DeleteResourceAction_toolTip);
		// PlatformUI.getWorkbench().getHelpSystem().setHelp(this,
		// "org.eclipse.ui.ide.delete_resource_action_context");
		// setId("org.eclipse.ui.DeleteResourceAction");
		if (shell == null) {
			throw new IllegalArgumentException();
		} else {
			this.shell = shell;
			return;
		}
	}

	private boolean canDelete(IResource resources[]) {
		if (!containsOnlyProjects(resources)
				&& !containsOnlyNonProjects(resources))
			return false;
		if (resources.length == 0)
			return false;
		for (int i = 0; i < resources.length; i++) {
			IResource resource = resources[i];
			if (resource.isPhantom())
				return false;
		}
		IStructuredSelection selection = this.getStructuredSelection();
		Iterator itor = selection.iterator();
		while (itor.hasNext()) {
			Object obj = itor.next();
			if (obj instanceof PackageNode)
				return false;
		}

		return true;
	}

	private boolean containsLinkedResource(IResource resources[]) {
		for (int i = 0; i < resources.length; i++) {
			IResource resource = resources[i];
			if (resource.isLinked())
				return true;
		}

		return false;
	}

	private boolean containsOnlyNonProjects(IResource resources[]) {
		int types = getSelectedResourceTypes(resources);
		if (types == 0)
			return false;
		return (types & 4) == 0;
	}

	private boolean containsOnlyProjects(IResource resources[]) {
		int types = getSelectedResourceTypes(resources);
		return types == 4;
	}

	private boolean confirmDelete(IResource resources[]) {
		if (containsOnlyProjects(resources))
			return confirmDeleteProjects(resources);
		else
			return confirmDeleteNonProjects(resources);
	}

	private boolean confirmDeleteNonProjects(IResource resources[]) {
		String title;
		String msg;
		if (resources.length == 1) {
			title = IDEWorkbenchMessages.DeleteResourceAction_title1;
			IResource resource = resources[0];
			if (resource.isLinked())
				msg = NLS
						.bind(IDEWorkbenchMessages.DeleteResourceAction_confirmLinkedResource1,
								resource.getName());
			else
				msg = NLS.bind(
						IDEWorkbenchMessages.DeleteResourceAction_confirm1,
						resource.getName());
		} else {
			title = IDEWorkbenchMessages.DeleteResourceAction_titleN;
			if (containsLinkedResource(resources))
				msg = NLS
						.bind(IDEWorkbenchMessages.DeleteResourceAction_confirmLinkedResourceN,
								new Integer(resources.length));
			else
				msg = NLS.bind(
						IDEWorkbenchMessages.DeleteResourceAction_confirmN,
						new Integer(resources.length));
		}
		return MessageDialog.openQuestion(shell, title, msg);
	}

	private boolean confirmDeleteProjects(IResource resources[]) {
		DeleteProjectDialog dialog = new DeleteProjectDialog(shell, resources);
		dialog.setTestingMode(fTestingMode);
		int code = dialog.open();
		deleteContent = dialog.getDeleteContent();
		return code == 0;
	}

	private IResource[] getSelectedResourcesArray() {
		List selection = getSelectedResources();
		//List temp = new ArrayList();
		
		
		IResource resources[] = new IResource[selection.size()];
		//IResource resources[] = new IResource[temp.size()];
		selection.toArray(resources);
		return resources;
	}

	private int getSelectedResourceTypes(IResource resources[]) {
		int types = 0;
		for (int i = 0; i < resources.length; i++)
			types |= resources[i].getType();

		return types;
	}

	public void run() {
		final IResource resources[] = getSelectedResourcesArray();
		if (!confirmDelete(resources)) {
			return;
		} else {
			Job deletionCheckJob = new Job("deletionCheck") {
				protected IStatus run(IProgressMonitor monitor) {
					if (resources.length == 0) {
						return Status.CANCEL_STATUS;
					} else {
						scheduleDeleteJob(resources);
						return Status.OK_STATUS;
					}
				}

				public boolean belongsTo(Object family) {
					if (IDEWorkbenchMessages.DeleteResourceAction_jobName
							.equals(family))
						return true;
					else
						return super.belongsTo(family);
				}

			};
			deletionCheckJob.schedule();
			for(int i=0;i<resources.length;i++){
				if(resources[i] instanceof File ){
					File file =(File)resources[i];
					EcOperationAdapater reNameop = new EcOperationAdapater();
					reNameop.delete(file.getProject(), file.getFullPath().toOSString());
					//temp.add(f);
				}
				//temp.add(selection.get(i));
			}
			return;
		}
	}

	private void delteCascade() {

		IStructuredSelection selection = this.getStructuredSelection();
		Iterator itor = selection.iterator();
		while (itor.hasNext()) {
			Object obj = itor.next();
			if (obj instanceof AbstractResourceNode)
				((AbstractResourceNode) obj).cascadeDelete();

		}
	}

	private void scheduleDeleteJob(final IResource resourcesToDelete[]) {
		Job deleteJob = new Job("deleteJob") {

			public IStatus run(IProgressMonitor monitor) {
				WorkbenchJob statusJob;
				final DeleteResourcesOperation op = new DeleteResourcesOperation(
						resourcesToDelete,
						IDEWorkbenchMessages.DeleteResourceAction_operationLabel,
						deleteContent);
				op.setModelProviderIds(getModelProviderIds());
				if (!deleteContent || !containsOnlyProjects(resourcesToDelete))
					try {
						statusJob = new WorkbenchJob("") {

							public IStatus runInUIThread(
									IProgressMonitor monitor) {
								return op.computeExecutionStatus(monitor);
							}

						};
						statusJob.setSystem(true);
						statusJob.schedule();
						try {
							statusJob.join();
						} catch (InterruptedException _ex) {
						}
						if (statusJob.getResult().isOK()){
							delteCascade();
							return op.execute(monitor,
									WorkspaceUndoUtil.getUIInfoAdapter(shell));
						}
							
						return statusJob.getResult();

					} catch (Exception e) {
						if (e.getCause() instanceof CoreException)
							return ((CoreException) e.getCause()).getStatus();
						else
							return new Status(4, "org.eclipse.ui.ide",
									e.getMessage(), e);
					}
				try {
					return PlatformUI
							.getWorkbench()
							.getOperationSupport()
							.getOperationHistory()
							.execute(op, monitor,
									WorkspaceUndoUtil.getUIInfoAdapter(shell));
				} catch (ExecutionException e) {
				}
				return new Status(4, "org.eclipse.ui.ide", "");
			}

			public boolean belongsTo(Object family) {
				if (IDEWorkbenchMessages.DeleteResourceAction_jobName
						.equals(family))
					return true;
				else
					return super.belongsTo(family);
			}

		};
		deleteJob.setUser(true);
		deleteJob.schedule();
	}

	protected boolean updateSelection(IStructuredSelection selection) {
		return super.updateSelection(selection)
				&& canDelete(getSelectedResourcesArray());
	}

	public String[] getModelProviderIds() {
		return modelProviderIds;
	}

	public void setModelProviderIds(String modelProviderIds[]) {
		this.modelProviderIds = modelProviderIds;
	}

	public static final String ID = "org.eclipse.ui.DeleteResourceAction";
	public static final String DJPATH="WebRoot/eform/eformsys/fceform/djfile/";
	private Shell shell;
	private boolean deleteContent;
	protected boolean fTestingMode;
	private String modelProviderIds[];

}