package avicit.ui.runtime.core.subsystem;

import avicit.ui.runtime.core.INode;
import avicit.ui.view.module.IModelFactory;
import avicit.ui.view.module.ModelManagerImpl;

import com.tansun.runtime.resource.adapter.AbstractModelWorkbenchAdapter;

/**
 * @author Tao Tao
 *
 */
public class SubSystemWorkbenchAdapter extends AbstractModelWorkbenchAdapter
{

    private SubSystemWorkbenchAdapter()
    {
    }

    public static SubSystemWorkbenchAdapter getInstance()
    {
        return instance;
    }

    public String getLabel(Object object)
    {
    	SubSystemNode node = (SubSystemNode)object;
        return node.getDisplayName();
    }

    public Object[] getChildren(Object object)
    {
    	INode node = (INode)object;
        IModelFactory factory = ModelManagerImpl.getInstance().getModelFactory(SubSystemModelFactory.TYPE);;
        if(factory != null)
        	return factory.getModelParser(object, null).getChildren(node);
        return new Object[0];
    }

	public boolean hasChildren(Object element) {
		return true;
	}

    private static final SubSystemWorkbenchAdapter instance = new SubSystemWorkbenchAdapter();
}