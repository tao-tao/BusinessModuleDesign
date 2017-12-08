package avicit.ui.runtime.core.action;

import java.util.List;

import org.eclipse.core.internal.resources.File;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.actions.CopyFilesAndFoldersOperation;
import org.eclipse.ui.actions.CopyProjectOperation;
import org.eclipse.ui.actions.SelectionListenerAction;
import org.eclipse.ui.part.ResourceTransfer;

import avicit.operation.EcOperationAdapater;

public class InternalPasteAction extends SelectionListenerAction
{

    public InternalPasteAction(Shell shell, Clipboard clipboard)
    {
        super("Paste");
        Assert.isNotNull(shell);
        Assert.isNotNull(clipboard);
        this.shell = shell;
        this.clipboard = clipboard;
        setToolTipText("Paste ToolTip");
//        setId("org.eclipse.ui.PasteAction");
//        PlatformUI.getWorkbench().getHelpSystem().setHelp(this, "HelpId");
    }

    private IResource getTarget()
    {
        List selectedResources = getSelectedResources();
        for(int i = 0; i < selectedResources.size(); i++)
        {
            IResource resource = (IResource)selectedResources.get(i);
            if((resource instanceof IProject) && !((IProject)resource).isOpen())
                return null;
            if(resource.getType() == 1)
                resource = resource.getParent();
            if(resource != null)
                return resource;
        }

        return null;
    }
    
    

    @Override
	protected List getSelectedResources() {
		return super.getSelectedResources();
	}

	private boolean isLinked(IResource resources[])
    {
        for(int i = 0; i < resources.length; i++)
            if(resources[i].isLinked())
                return true;

        return false;
    }

    public void run()
    {
        ResourceTransfer resTransfer = ResourceTransfer.getInstance();
        IResource resourceData[] = (IResource[])clipboard.getContents(resTransfer);
        if(resourceData != null && resourceData.length > 0)
        {
            if(resourceData[0].getType() == 4)
            {
                for(int i = 0; i < resourceData.length; i++)
                {
                    CopyProjectOperation operation = new CopyProjectOperation(shell);
                    operation.copyProject((IProject)resourceData[i]);
                }

            } else
            {
                IContainer container = getContainer();
                CopyFilesAndFoldersOperation operation = new CopyFilesAndFoldersOperation(shell);
                operation.copyResources(resourceData, container);
                for(int i=0;i<resourceData.length;i++){
                	if(resourceData[i] instanceof File){
                		File file =(File)resourceData[i];
    					EcOperationAdapater reNameop = new EcOperationAdapater();
    					reNameop.copy(file.getProject(), file.getFullPath().toOSString(),container.getFullPath().toOSString());
                	}
                }
            }
            return;
        }
        FileTransfer fileTransfer = FileTransfer.getInstance();
        String fileData[] = (String[])clipboard.getContents(fileTransfer);
        if(fileData != null)
        {
            IContainer container = getContainer();
            CopyFilesAndFoldersOperation operation = new CopyFilesAndFoldersOperation(shell);
            operation.copyFiles(fileData, container);
        }
    }

    private IContainer getContainer()
    {
        List selection = getSelectedResources();
        if(selection.get(0) instanceof IFile)
            return ((IFile)selection.get(0)).getParent();
        else
            return (IContainer)selection.get(0);
    }

    protected boolean updateSelection(IStructuredSelection selection)
    {
        if(!super.updateSelection(selection))
            return false;
        final IResource clipboardData[][] = new IResource[1][];
        shell.getDisplay().syncExec(new Runnable() {

            public void run()
            {
                ResourceTransfer resTransfer = ResourceTransfer.getInstance();
                clipboardData[0] = (IResource[])clipboard.getContents(resTransfer);
            }

        });
        IResource resourceData[] = clipboardData[0];
        boolean isProjectRes = resourceData != null && resourceData.length > 0 && resourceData[0].getType() == 4;
        if(isProjectRes)
        {
            for(int i = 0; i < resourceData.length; i++)
                if(resourceData[i].getType() != 4 || !((IProject)resourceData[i]).isOpen())
                    return false;

            return true;
        }
        if(getSelectedNonResources().size() > 0)
            return false;
        IResource targetResource = getTarget();
        if(targetResource == null)
            return false;
        List selectedResources = getSelectedResources();
        if(selectedResources.size() > 1)
        {
            for(int i = 0; i < selectedResources.size(); i++)
            {
                IResource resource = (IResource)selectedResources.get(i);
                if(resource.getType() != 1)
                    return false;
                if(!targetResource.equals(resource.getParent()))
                    return false;
            }

        }
        if(resourceData != null)
        {
            if(isLinked(resourceData) && targetResource.getType() != 4)
                return false;
            if(targetResource.getType() == 2)
            {
                for(int i = 0; i < resourceData.length; i++)
                    if(targetResource.equals(resourceData[i]))
                        return false;

            }
            return true;
        }
        org.eclipse.swt.dnd.TransferData transfers[] = clipboard.getAvailableTypes();
        FileTransfer fileTransfer = FileTransfer.getInstance();
        for(int i = 0; i < transfers.length; i++)
            if(fileTransfer.isSupportedType(transfers[i]))
                return true;

        return false;
    }

    private Shell shell;
    private Clipboard clipboard;

}
