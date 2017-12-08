package avicit.ui.runtime.core.requirement.requirement.usecase;

import avicit.ui.runtime.core.INode;
import avicit.ui.view.module.IModelFactory;
import avicit.ui.view.module.ModelManagerImpl;

import com.tansun.runtime.resource.adapter.AbstractModelWorkbenchAdapter;

/**
 * @author TaoTao
 *
 */
public class UseCaseWorkbenchAdapter extends AbstractModelWorkbenchAdapter
{

	@Override
	public boolean hasChildren(Object element) {
		return true;
	}

    public UseCaseWorkbenchAdapter()
    {
    }

    public static UseCaseWorkbenchAdapter getInstance()
    {
        return instance;
    }

    @Override
	public Object getParent(Object object) {
		return super.getParent(object);
	}

	public String getLabel(Object object)
    {
		UseCaseNode node = (UseCaseNode)object;
        return node.getDisplayName();
    }

    public Object[] getChildren(Object object)
    {
    	INode node = (INode)object;
        IModelFactory factory = ModelManagerImpl.getInstance().getModelFactory(UseCaseModelFactory.TYPE);;
        if(factory != null)
        	return factory.getModelParser(object, null).getChildren(node);
        return new Object[0];
    }

    private static final UseCaseWorkbenchAdapter instance = new UseCaseWorkbenchAdapter();

}