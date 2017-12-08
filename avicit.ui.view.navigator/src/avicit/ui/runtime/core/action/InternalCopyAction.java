package avicit.ui.runtime.core.action;

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.SelectionListenerAction;
import org.eclipse.ui.part.ResourceTransfer;

public class InternalCopyAction extends SelectionListenerAction
{

    public InternalCopyAction(Shell shell, Clipboard clipboard)
    {
        super("Copy");
        Assert.isNotNull(shell);
        Assert.isNotNull(clipboard);
        this.shell = shell;
        this.clipboard = clipboard;
        setToolTipText("Copy Tooltip");
//        setId("org.eclipse.ui.CopyAction");
//        PlatformUI.getWorkbench().getHelpSystem().setHelp(this, "CopyHelpId");
    }

    public InternalCopyAction(Shell shell, Clipboard clipboard, InternalPasteAction pasteAction)
    {
        this(shell, clipboard);
        this.pasteAction = pasteAction;
    }

    public void run()
    {
        List selectedResources = getSelectedResources();
        IResource resources[] = (IResource[])selectedResources.toArray(new IResource[selectedResources.size()]);
        int length = resources.length;
        int actualLength = 0;
        String fileNames[] = new String[length];
        StringBuffer buf = new StringBuffer();
        for(int i = 0; i < length; i++)
        {
            IPath location = resources[i].getLocation();
            if(location != null)
                fileNames[actualLength++] = location.toOSString();
            if(i > 0)
                buf.append("\n");
            buf.append(resources[i].getName());
        }

        if(actualLength < length)
        {
            String tempFileNames[] = fileNames;
            fileNames = new String[actualLength];
            for(int i = 0; i < actualLength; i++)
                fileNames[i] = tempFileNames[i];

        }
        setClipboard(resources, fileNames, buf.toString());
        if(pasteAction != null && pasteAction.getStructuredSelection() != null)
            pasteAction.selectionChanged(pasteAction.getStructuredSelection());
    }

    private void setClipboard(IResource resources[], String fileNames[], String names)
    {
        try
        {
            if(fileNames.length > 0)
                clipboard.setContents(new Object[] {
                    resources, fileNames, names
                }, new Transfer[] {
                    ResourceTransfer.getInstance(), FileTransfer.getInstance(), TextTransfer.getInstance()
                });
            else
                clipboard.setContents(new Object[] {
                    resources, names
                }, new Transfer[] {
                    ResourceTransfer.getInstance(), TextTransfer.getInstance()
                });
        }
        catch(SWTError e)
        {
            if(e.code != 2002)
                throw e;
            if(MessageDialog.openQuestion(shell, "Problem with copy title", "Problem with copy."))
                setClipboard(resources, fileNames, names);
        }
    }

    protected boolean updateSelection(IStructuredSelection selection)
    {
        if(!super.updateSelection(selection))
            return false;
        if(getSelectedNonResources().size() > 0)
            return false;
        List selectedResources = getSelectedResources();
        if(selectedResources.size() == 0)
            return false;
        boolean projSelected = selectionIsOfType(4);
        boolean fileFoldersSelected = selectionIsOfType(3);
        if(!projSelected && !fileFoldersSelected)
            return false;
        if(projSelected && fileFoldersSelected)
            return false;
        IContainer firstParent = ((IResource)selectedResources.get(0)).getParent();
        if(firstParent == null)
            return false;
        for(Iterator resourcesEnum = selectedResources.iterator(); resourcesEnum.hasNext();)
        {
            IResource currentResource = (IResource)resourcesEnum.next();
            if(!currentResource.getParent().equals(firstParent))
                return false;
            if(currentResource.getLocation() == null)
                return false;
        }

        return true;
    }

    private Shell shell;
    private Clipboard clipboard;
    private InternalPasteAction pasteAction;
}