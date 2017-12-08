package avicit.ui.runtime.core.action.providers;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.navigator.CommonActionProvider;

import avicit.ui.runtime.core.action.ExploreFileActionDelegate;
import avicit.ui.view.navigator.ImageRepository;

public class OpenExplorerActionProvider extends CommonActionProvider
{

    public OpenExplorerActionProvider()
    {
    	explorer = new ExploreFileActionDelegate("打开资源位置");
        ImageRepository imageRepository = ImageRepository.getImageRepository("avicit.ui.view.navigator");
        explorer.setImageDescriptor(imageRepository.getImageDescriptor("folder"));
    }

    public void fillContextMenu(IMenuManager menu)
    {
        super.fillContextMenu(menu);
        IStructuredSelection selection = (IStructuredSelection)getContext().getSelection();
        explorer.selectionChanged(selection);
		menu.appendToGroup("group.open", explorer);
    }

    private ExploreFileActionDelegate explorer;
}