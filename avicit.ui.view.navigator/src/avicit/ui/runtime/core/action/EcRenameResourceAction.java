package avicit.ui.runtime.core.action;

import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourceAttributes;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.SelectionListenerAction;
import org.eclipse.ui.ide.undo.MoveResourcesOperation;
import org.eclipse.ui.ide.undo.ResourceDescription;
import org.eclipse.ui.ide.undo.WorkspaceUndoUtil;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.ui.internal.ide.StatusUtil;
import org.eclipse.ui.internal.progress.ProgressMonitorJobsDialog;

import avicit.operation.EcOperationAdapater;
import avicit.ui.common.util.HSUtil;
import avicit.ui.runtime.core.node.AbstractResourceNode;

public class EcRenameResourceAction extends SelectionListenerAction {

	private final IShellProvider shellProvider;

	public EcRenameResourceAction(final Shell shell) {
		super(IDEWorkbenchMessages.RenameResourceAction_text);
		Assert.isNotNull(shell);
		shellProvider = new IShellProvider() {
			public Shell getShell() {
				return shell;
			}
		};
		initAction();
	}

	private void initAction() {
		setToolTipText(IDEWorkbenchMessages.RenameResourceAction_toolTip);
		setId("org.eclipse.ui.RenameResourceAction");
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this, "org.eclipse.ui.ide.rename_resource_action_context");
	}

	void displayError(String message) {
		if (message == null)
			message = IDEWorkbenchMessages.WorkbenchAction_internalError;
		MessageDialog.openError(shellProvider.getShell(), getProblemsTitle(), message);
	}

	Shell getShell() {
		return shellProvider.getShell();
	}

	public void superrun() {
		IStatus errorStatus[] = new IStatus[1];
		try {
			(new ProgressMonitorJobsDialog(shellProvider.getShell())).run(true, true, createOperation(errorStatus));
		} catch (InterruptedException _ex) {
			return;
		} catch (InvocationTargetException e) {
			String msg = NLS.bind(IDEWorkbenchMessages.WorkspaceAction_logTitle, getClass().getName(), e.getTargetException());
			IDEWorkbenchPlugin.log(msg, StatusUtil.newStatus(4, msg, e.getTargetException()));
			displayError(e.getTargetException().getMessage());
		}
		if (errorStatus[0] != null && !errorStatus[0].isOK())
			ErrorDialog.openError(shellProvider.getShell(), getProblemsTitle(), null, errorStatus[0]);
	}

	protected boolean superupdateSelection(IStructuredSelection selection) {
		if (!super.updateSelection(selection) || selection.isEmpty())
			return false;
		for (Iterator i = getSelectedResources().iterator(); i.hasNext();) {
			IResource r = (IResource) i.next();
			if (!r.isAccessible())
				return false;
		}

		return true;
	}

	private boolean checkOverwrite(final Shell shell, final IResource destination) {
		final boolean result[] = new boolean[1];
		Runnable query = new Runnable() {
			public void run() {
				String pathName = destination.getFullPath().makeRelative().toString();
				String message = EcRenameResourceAction.RESOURCE_EXISTS_MESSAGE;
				String title = EcRenameResourceAction.RESOURCE_EXISTS_TITLE;
				if (destination.getType() == 4) {
					message = EcRenameResourceAction.PROJECT_EXISTS_MESSAGE;
					title = EcRenameResourceAction.PROJECT_EXISTS_TITLE;
				}
				result[0] = MessageDialog.openQuestion(shell, title, MessageFormat.format(message, new Object[] { pathName }));
			}
		};
		shell.getDisplay().syncExec(query);
		return result[0];
	}

	private boolean checkReadOnlyAndNull(IResource currentResource) {
		if (currentResource == null)
			return false;
		ResourceAttributes attributes = currentResource.getResourceAttributes();
		if (attributes != null && attributes.isReadOnly())
			return MessageDialog.openQuestion(getShell(), CHECK_RENAME_TITLE, MessageFormat.format(CHECK_RENAME_MESSAGE, new Object[] { currentResource.getName() }));
		else
			return true;
	}

	protected String getOperationMessage() {
		return IDEWorkbenchMessages.RenameResourceAction_progress;
	}

	protected String getProblemsMessage() {
		return IDEWorkbenchMessages.RenameResourceAction_problemMessage;
	}

	protected String getProblemsTitle() {
		return IDEWorkbenchMessages.RenameResourceAction_problemTitle;
	}

	protected String queryNewResourceName(final IResource resource) {
		final IWorkspace workspace = IDEWorkbenchPlugin.getPluginWorkspace();
		final IPath prefix = resource.getFullPath().removeLastSegments(1);
		IInputValidator validator = new IInputValidator() {
			public String isValid(String string) {
				if (resource.getName().equals(string))
					return IDEWorkbenchMessages.RenameResourceAction_nameMustBeDifferent;
				IStatus status = workspace.validateName(string, resource.getType());
				if (!status.isOK())
					return status.getMessage();
				if (workspace.getRoot().exists(prefix.append(string)))
					return IDEWorkbenchMessages.RenameResourceAction_nameExists;
				else
					return null;
			}
		};
		InputDialog dialog = new InputDialog(getShell(), IDEWorkbenchMessages.RenameResourceAction_inputDialogTitle, IDEWorkbenchMessages.RenameResourceAction_inputDialogMessage, resource.getName(), validator);
		dialog.setBlockOnOpen(true);
		int result = dialog.open();
		if (result == 0)
			return dialog.getValue();
		else
			return null;
	}

	public void run() {
		
		IResource currentResource = getCurrentResource();
		//ECproject.CloseAll(currentResource.getFullPath().toString());
		
		if (currentResource == null || !currentResource.exists())
			return;
//		boolean success = LTKLauncher.openRenameWizard(getStructuredSelection());
//		if(success)
//			return;
		if (!checkReadOnlyAndNull(currentResource))
			return;
		String newName = queryNewResourceName(currentResource);
		if (newName == null || newName.equals(""))
			return;
//		if (!newName.endsWith(".jsp")){
//			MessageDialog.openError(Display.getCurrent().getActiveShell(),
//					"警告", "文件后缀名必须是.jsp！");
//			return;
//		}
//		if (newName.equals(".jsp")){
//			MessageDialog.openError(Display.getCurrent().getActiveShell(),
//					"警告", "必须填写文件名！");
//			return;
//		}
		newPath = currentResource.getFullPath().removeLastSegments(1).append(newName);
		if(newPath.toString().indexOf("META-INF/jsp") > 0 && !newName.endsWith(".jsp")){
			HSUtil.showError("后缀名必须以.jsp结尾", Display.getCurrent().getActiveShell());
			return;
		}
		renameCascade();
		superrun();
		EcOperationAdapater reNameop = new EcOperationAdapater();
		reNameop.rename(currentResource.getProject(), currentResource.getName(),currentResource.getFullPath().toOSString(),newName);
		
	}
	private void renameCascade() {

		IStructuredSelection selection = this.getStructuredSelection();
		Iterator itor = selection.iterator();
		while (itor.hasNext()) {
			Object obj = itor.next();
			if (obj instanceof AbstractResourceNode)
				((AbstractResourceNode) obj).cascadeRename();

		}
	}

	private IResource getCurrentResource() {
		List resources = getSelectedResources();
		if (resources.size() == 1)
			return (IResource) resources.get(0);
		else
			return null;
	}

	protected void runWithNewPath(IPath path, IResource resource) {
		newPath = path;
		super.run();
	}

	protected boolean updateSelection(IStructuredSelection selection) {
		if (selection.size() > 1)
			return false;
		if (!superupdateSelection(selection))
			return false;
		IResource currentResource = getCurrentResource();
		return currentResource != null && currentResource.exists();
	}

	protected IRunnableWithProgress createOperation(final IStatus errorStatus[]) {
		return new IRunnableWithProgress() {

			public void run(IProgressMonitor monitor) {
				IResource resources[] = (IResource[]) getSelectedResources().toArray(new IResource[getSelectedResources().size()]);
				if (resources.length == 1) {
					IWorkspaceRoot workspaceRoot = resources[0].getWorkspace().getRoot();
					IResource newResource = workspaceRoot.findMember(newPath);
					boolean go = true;
					if (newResource != null)
						go = checkOverwrite(getShell(), newResource);
					if (go) {
						List<IResource> allResources = new ArrayList();
						List<IPath> allPaths = new ArrayList();
						fetchRelated(resources[0], allResources, allPaths);
						MoveResourcesOperation op = new EcMoveResourcesOperation(allResources.toArray(new IResource[allResources.size()]),
								allPaths.toArray(new IPath[allPaths.size()]), newPath, IDEWorkbenchMessages.RenameResourceAction_operationTitle);
						try {
							PlatformUI.getWorkbench().getOperationSupport().getOperationHistory().execute(op, monitor, WorkspaceUndoUtil.getUIInfoAdapter(getShell()));
						} catch (ExecutionException e) {
							if (e.getCause() instanceof CoreException)
								errorStatus[0] = ((CoreException) e.getCause()).getStatus();
							else
								errorStatus[0] = new Status(4, "org.eclipse.ui", getProblemsMessage(), e);
						}
					}
				}
			}
		};
	}
	
	private void fetchRelated(IResource newResource, List<IResource> allResources, List<IPath> allPaths){
		String name = newResource.getName();
		allResources.add(newResource);
		allPaths.add(newPath);
		if(name.endsWith(".spring.xml"))
		{
			String prefix = name.substring(0, name.length()-11);
			String newPrefix = newPath.lastSegment();
			if(newPrefix.endsWith(".spring.xml"))
				newPrefix = newPrefix.substring(0,newPrefix.length()-11);
			IFile f = newResource.getParent().getFile(new Path(prefix + ".att.xml"));
			if(f.exists())
			{
				allResources.add(f);
				IPath p = (IPath) newPath.clone();
				p = p.removeLastSegments(1);
				p = p.append(newPrefix+".att.xml");
				allPaths.add(p);
			}
			f = newResource.getParent().getFile(new Path(prefix + ".aop.xml"));
			if(f.exists())
			{
				allResources.add(f);
				IPath p = (IPath) newPath.clone();
				p = p.removeLastSegments(1);
				p = p.append(newPrefix+".aop.xml");
				allPaths.add(p);
			}
		}
		
	}

	public static final String ID = "org.eclipse.ui.EcRenameResourceAction";
	private IPath newPath;
	private static final String CHECK_RENAME_TITLE;
	private static final String CHECK_RENAME_MESSAGE;
	private static String RESOURCE_EXISTS_TITLE;
	private static String RESOURCE_EXISTS_MESSAGE;
	private static String PROJECT_EXISTS_MESSAGE;
	private static String PROJECT_EXISTS_TITLE;

	static {
		CHECK_RENAME_TITLE = IDEWorkbenchMessages.RenameResourceAction_checkTitle;
		CHECK_RENAME_MESSAGE = IDEWorkbenchMessages.RenameResourceAction_readOnlyCheck;
		RESOURCE_EXISTS_TITLE = IDEWorkbenchMessages.RenameResourceAction_resourceExists;
		RESOURCE_EXISTS_MESSAGE = IDEWorkbenchMessages.RenameResourceAction_overwriteQuestion;
		PROJECT_EXISTS_MESSAGE = IDEWorkbenchMessages.RenameResourceAction_overwriteProjectQuestion;
		PROJECT_EXISTS_TITLE = IDEWorkbenchMessages.RenameResourceAction_projectExists;
	}

	class EcMoveResourcesOperation extends MoveResourcesOperation {
		IPath originalDestinationPaths[];
		IResource originalResources[];
		IPath originalDestination;

		public EcMoveResourcesOperation(IResource[] resources, IPath[] destinationPaths, IPath destinationPath, String label) {
			super(resources, destinationPath, label);
			this.destinationPaths = destinationPaths;
			originalResources = resources;
			originalDestination = destination;
			originalDestinationPaths = destinationPaths;
		}

		protected void doUndo(IProgressMonitor monitor, IAdaptable uiInfo) throws CoreException {
			move(monitor, uiInfo);
			setTargetResources(originalResources);
			resourceDescriptions = new ResourceDescription[0];
			destination = originalDestination;
			destinationPaths = originalDestinationPaths;
		}

		@Override
		protected void doExecute(IProgressMonitor monitor, IAdaptable uiInfo) throws CoreException {
			try{
				super.doExecute(monitor, uiInfo);
//				ResourceMapper ;
			}catch(Throwable e){
				e.printStackTrace();
			}
		}
	}

}
