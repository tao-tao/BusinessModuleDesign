package avicit.ui.runtime.core.action;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.actions.SelectionListenerAction;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.ui.internal.ide.StatusUtil;
import org.eclipse.ui.internal.progress.ProgressMonitorJobsDialog;


public abstract class WorkspaceAction extends SelectionListenerAction
{

    public WorkspaceAction(String text)
    {
        super(text);
        shell = Display.getCurrent().getActiveShell();
    }

    void displayError(String message)
    {
        if(message == null)
            message = IDEWorkbenchMessages.WorkbenchAction_internalError;
        MessageDialog.openError(shell, getProblemsTitle(), message);
    }

    final IStatus execute(List resources, IProgressMonitor monitor)
    {
        MultiStatus errors;
        Iterator resourcesEnum;
        errors = null;
        if(shouldPerformResourcePruning())
            resources = pruneResources(resources);
        monitor.beginTask("", resources.size() * 1000);
        monitor.setTaskName(getOperationMessage());
        resourcesEnum = resources.iterator();
        Object obj;
        while(resourcesEnum.hasNext()) 
        {
            IResource resource = (IResource)resourcesEnum.next();
            try
            {
                invokeOperation(resource, new SubProgressMonitor(monitor, 1000));
            }
            catch(CoreException e)
            {
                errors = recordError(errors, e);
            }
            if(monitor.isCanceled())
                throw new OperationCanceledException();
        }
        try
        {
        	invokeOperation(selection, new SubProgressMonitor(monitor, 1000));
        }
        catch(CoreException e)
        {
            errors = recordError(errors, e);
        }
        
        obj = errors != null ? ((Object) (errors)) : ((Object) (Status.OK_STATUS));
        monitor.done();
        return ((IStatus) (obj));
    }

    protected abstract String getOperationMessage();

    protected String getProblemsMessage()
    {
        return IDEWorkbenchMessages.WorkbenchAction_problemsMessage;
    }

    protected String getProblemsTitle()
    {
        return IDEWorkbenchMessages.WorkspaceAction_problemsTitle;
    }

    Shell getShell()
    {
        return shell;
    }

    protected abstract void invokeOperation(IStructuredSelection selection, IProgressMonitor iprogressmonitor)
        throws CoreException;
    
    protected abstract void invokeOperation(IResource iresource, IProgressMonitor iprogressmonitor)
        throws CoreException;

    boolean isDescendent(List resources, IResource child)
    {
        IResource parent = child.getParent();
        return parent != null && (resources.contains(parent) || isDescendent(resources, parent));
    }

    List pruneResources(List resourceCollection)
    {
        List prunedList = new ArrayList(resourceCollection);
        for(Iterator elementsEnum = prunedList.iterator(); elementsEnum.hasNext();)
        {
            IResource currentResource = (IResource)elementsEnum.next();
            if(isDescendent(prunedList, currentResource))
                elementsEnum.remove();
        }

        return prunedList;
    }

    MultiStatus recordError(MultiStatus errors, CoreException error)
    {
        if(errors == null)
            errors = new MultiStatus("org.eclipse.ui.ide", 4, getProblemsMessage(), null);
        errors.merge(error.getStatus());
        return errors;
    }

    public void run()
    {
        IStatus errorStatus[] = new IStatus[1];
        try
        {
            (new ProgressMonitorJobsDialog(shell)).run(true, true, createOperation(errorStatus));
        }
        catch(InterruptedException _ex)
        {
            return;
        }
        catch(InvocationTargetException e)
        {
            String msg = NLS.bind(IDEWorkbenchMessages.WorkspaceAction_logTitle, getClass().getName(), e.getTargetException());
            IDEWorkbenchPlugin.log(msg, StatusUtil.newStatus(4, msg, e.getTargetException()));
            displayError(e.getTargetException().getMessage());
        }
        if(errorStatus[0] != null && !errorStatus[0].isOK())
            ErrorDialog.openError(shell, getProblemsTitle(), null, errorStatus[0]);
    }

    protected boolean shouldPerformResourcePruning()
    {
        return true;
    }

    protected boolean updateSelection(IStructuredSelection selection)
    {
        if(!super.updateSelection(selection) || selection.isEmpty())
            return false;
        for(Iterator i = getSelectedResources().iterator(); i.hasNext();)
        {
            IResource r = (IResource)i.next();
            if(!r.isAccessible())
                return false;
        }

        this.selection = selection; 
        return true;
    }

    protected List getActionResources()
    {
        return getSelectedResources();
    }

    public void runInBackground(ISchedulingRule rule)
    {
        runInBackground(rule, ((Object []) (null)));
    }

    public void runInBackground(ISchedulingRule rule, Object jobFamily)
    {
        if(jobFamily == null)
            runInBackground(rule, ((Object []) (null)));
        else
            runInBackground(rule, new Object[] {
                jobFamily
            });
    }

    public void runInBackground(ISchedulingRule rule,final Object jobFamilies[])
    {
        final List resources = new ArrayList(getActionResources());
        
        Job job = new WorkspaceJob("RefreshJob") {
            public boolean belongsTo(Object family)
            {
                if(jobFamilies == null || family == null)
                    return false;
                for(int i = 0; i < jobFamilies.length; i++)
                    if(family.equals(jobFamilies[i]))
                        return true;
                return false;
            }
            public IStatus runInWorkspace(IProgressMonitor monitor)
            {
                return execute(resources, monitor);
            }
        };
        if(rule != null)
            job.setRule(rule);
        job.setUser(true);
        job.schedule();
    }

    protected IRunnableWithProgress createOperation(final IStatus errorStatus[])
    {
        return new WorkspaceModifyOperation() {
            public void execute(IProgressMonitor monitor)
            {
                errorStatus[0] = WorkspaceAction.this.execute(getActionResources(), monitor);
            }

        };
    }
    private Shell shell;
    IStructuredSelection selection;
}