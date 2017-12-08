package avicit.ui.runtime.core.action;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.SelectionListenerAction;

import avicit.ui.view.navigator.util.DesktopUtil;

public class ExploreFileActionDelegate extends SelectionListenerAction
{

    public ExploreFileActionDelegate(String text)
    {
    	super(text);
        selectedFile = null;
    }

    public void setActivePart(IAction iaction, IWorkbenchPart iworkbenchpart)
    {
    }

    @Override
    public void run()
    {
        DesktopUtil.explore(selectedFile);
    }

	protected boolean updateSelection(IStructuredSelection selection) {
        if(!(selection instanceof StructuredSelection))
        {
            selectedFile = null;
            return false;
        }
        StructuredSelection sel = (StructuredSelection)selection;
        if(sel.size() != 1)
        {
            selectedFile = null;
            return false;
        }
        Object object = sel.getFirstElement();
        IResource resource = null;
        if(object instanceof IResource)
            resource = (IResource)object;
        else
        if(object instanceof IJavaElement)
            resource = ((IJavaElement)object).getResource();
        else
        if(object instanceof IAdaptable)
        {
            IAdaptable adaptable = (IAdaptable)object;
            resource = (IResource)adaptable.getAdapter(IResource.class);
            if(resource instanceof IFile)
            {
                selectedFile = ((IFile)resource).getLocation().toFile();
                return true;
            }
        }
        if(resource == null)
        {
            return false;
        }
        if(resource.getLocation() != null)
        {
            selectedFile = resource.getLocation().toFile();
            return true;
        } else
        {
            return false;
        }
	}

    public void dispose()
    {
    }

    public void init(IWorkbenchWindow window)
    {
        this.window = window;
    }

    private File selectedFile;
    private IWorkbenchWindow window;
}