package avicit.ui.runtime.core.develope.logic;

import avicit.ui.runtime.core.INode;
import avicit.ui.view.module.IModelFactory;
import avicit.ui.view.module.ModelManagerImpl;

import com.tansun.runtime.resource.adapter.AbstractModelWorkbenchAdapter;

public class LogicWorkbenchAdapter extends AbstractModelWorkbenchAdapter{


	@Override
	public boolean hasChildren(Object element) {
		return true;
	}

    public LogicWorkbenchAdapter()
    {
    }

    public static LogicWorkbenchAdapter getInstance()
    {
        return instance;
    }

    @Override
	public Object getParent(Object object) {
		return super.getParent(object);
	}

	public String getLabel(Object object)
    {
		LogicNode node = (LogicNode)object;
        return node.getDisplayName();
    }

    public Object[] getChildren(Object object)
    {
    	INode node = (INode)object;
        IModelFactory factory = ModelManagerImpl.getInstance().getModelFactory(LogicModelFactory.TYPE);
        if(factory != null)
        	return factory.getModelParser(object, null).getChildren(node);
        return new Object[0];
    }

    private static final LogicWorkbenchAdapter instance = new LogicWorkbenchAdapter();
}
