package avicit.ui.runtime.core.action.providers;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.internal.core.CompilationUnit;
import org.eclipse.jdt.internal.core.PackageFragment;
import org.eclipse.jdt.internal.core.PackageFragmentRoot;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionGroup;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;

//import com.tansun.ec.presentation.display.GuizePageXNode;
//import com.tansun.ec.presentation.js.ExportAction;
//import com.tansun.ec.presentation.js.GuizeNode;
//import com.tansun.ec.presentation.js.ImportAction;
import avicit.ui.runtime.core.action.DeleteResourceAction;
import avicit.ui.runtime.core.action.EcRenameResourceAction;
import avicit.ui.runtime.core.action.InternalCopyAction;
import avicit.ui.runtime.core.action.InternalPasteAction;
//import com.tansun.runtime.core.node.AbstractFileNode;
//import com.tansun.runtime.core.node.AbstractFolderNode;
//import com.tansun.runtime.core.node.JavaSourceNode;


public class EditActionGroup extends ActionGroup
{

    public EditActionGroup(ICommonActionExtensionSite anActionSite)
    {
        this.anActionSite = anActionSite;
        makeActions();
    }

    public void dispose()
    {
        if(clipboard != null)
        {
            clipboard.dispose();
            clipboard = null;
        }
        super.dispose();
    }

    public void fillContextMenu(IMenuManager menu)
    {
        IStructuredSelection selection = (IStructuredSelection)getContext().getSelection();
//        boolean anyResourceSelected = !selection.isEmpty() && ResourceSelectionUtil.allResourcesAreOfType(selection, 7);
        Object ele = selection.getFirstElement();
//        if(ele instanceof GuizeNode){
//        	menu.appendToGroup("group.edit", jsexaction);
//        	menu.appendToGroup("group.edit", jsimaction);
//        	 jsexaction.selectionChanged(selection);
//        	 jsimaction.selectionChanged(selection);
//        }
//        if(ele instanceof AbstractFolderNode)
        {
//        	if(!(ele instanceof JavaSourceNode)){
//        		menu.appendToGroup("group.edit", copyAction);
//        		menu.appendToGroup("group.edit", pasteAction);
//        	}
        }
//        else if(!(ele instanceof IProject))
//        {
//        	
//        	if(!(ele instanceof CompilationUnit) && !(ele instanceof PackageFragment)){
//        		menu.appendToGroup("group.edit", renameAction);
//            	menu.appendToGroup("group.edit", copyAction);
//            	menu.appendToGroup("group.edit", pasteAction);
//        	}
//        	if((ele instanceof AbstractFileNode)){
//        		
//            	menu.appendToGroup("group.edit", deleteAction);
//        	}
//        	
//        	
//        } 
//        menu.appendToGroup("group.edit", openFileAction);
//        if(anyResourceSelected)
    }

    public void fillActionBars(IActionBars actionBars)
    {
//        if(textActionHandler == null)
//            textActionHandler = new TextActionHandler(actionBars);
//        textActionHandler.setCopyAction(copyAction);
//        textActionHandler.setPasteAction(pasteAction);
//        textActionHandler.setDeleteAction(deleteAction);
        updateActionBars();
//        textActionHandler.updateActionBars();
    }

    public void handleKeyPressed(KeyEvent event)
    {
        if(event.character == '\177' && event.stateMask == 0)
        {
            if(deleteAction.isEnabled())
                deleteAction.run();
            event.doit = false;
        }
    }

    protected void makeActions()
    {
    	Shell shell = anActionSite.getViewSite().getShell();
        clipboard = new Clipboard(shell.getDisplay());
        pasteAction = new InternalPasteAction(shell, clipboard);
        ISharedImages images = PlatformUI.getWorkbench().getSharedImages();
        pasteAction.setDisabledImageDescriptor(images.getImageDescriptor("IMG_TOOL_PASTE_DISABLED"));
        pasteAction.setImageDescriptor(images.getImageDescriptor("IMG_TOOL_PASTE"));
//      pasteAction.setActionDefinitionId("org.eclipse.ui.edit.paste");
        copyAction = new InternalCopyAction(shell, clipboard, pasteAction);
        copyAction.setDisabledImageDescriptor(images.getImageDescriptor("IMG_TOOL_COPY_DISABLED"));
        copyAction.setImageDescriptor(images.getImageDescriptor("IMG_TOOL_COPY"));
//      copyAction.setActionDefinitionId("org.eclipse.ui.edit.copy");
        deleteAction = new DeleteResourceAction(shell);
        deleteAction.setDisabledImageDescriptor(images.getImageDescriptor("IMG_TOOL_DELETE_DISABLED"));
        deleteAction.setImageDescriptor(images.getImageDescriptor("IMG_TOOL_DELETE"));
//      deleteAction.setActionDefinitionId("org.eclipse.ui.edit.delete");
        renameAction = new EcRenameResourceAction(shell);
//      renameAction.setActionDefinitionId("org.eclipse.ui.edit.rename");
        pasteAction.setText("Poin paste");
        copyAction.setText("Poin copy");
        deleteAction.setText("Poin delete");
        renameAction.setText("Poin rename");
//      ImageRepository imageRepository = ImageRepository.getImageRepository("com.tansun.ui.navigator");
//      openFileAction = new ExploreFileActionDelegate("Explorer");
//      openFileAction.setImageDescriptor(imageRepository.getImageDescriptor("folder"));
//        jsexaction=new ExportAction("����");
//        jsimaction=new ImportAction("����");
    }

    public void updateActionBars()
    {
        IStructuredSelection selection = (IStructuredSelection)getContext().getSelection();
        copyAction.selectionChanged(selection);
        pasteAction.selectionChanged(selection);
        deleteAction.selectionChanged(selection);
    	renameAction.selectionChanged(selection);
//        openFileAction.selectionChanged(selection);
    }

    private Clipboard clipboard;
    private InternalCopyAction copyAction;
    private DeleteResourceAction deleteAction;
    private InternalPasteAction pasteAction;
    private EcRenameResourceAction renameAction;
//    ExploreFileActionDelegate openFileAction;
//    private TextActionHandler textActionHandler;
    ICommonActionExtensionSite anActionSite;
//    private ExportAction jsexaction;
//    private ImportAction jsimaction;
}