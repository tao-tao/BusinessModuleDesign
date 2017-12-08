package avicit.ui.runtime.core.cluster;

import avicit.ui.core.runtime.resource.IFileDelegate;
import avicit.ui.runtime.core.node.AbstractNode;
import avicit.ui.view.module.IModelFactory;
import avicit.ui.view.module.ModelManagerImpl;

import com.tansun.runtime.resource.adapter.AbstractModelWorkbenchAdapter;

/**
 * @author Tao Tao
 *
 */
public class FunctionClusterWorkbenchAdapter extends
		AbstractModelWorkbenchAdapter {

	private FunctionClusterWorkbenchAdapter() {
	}

	public static FunctionClusterWorkbenchAdapter getInstance() {
		return instance;
	}

    public String getLabel(Object object)
    {
    	FunctionClusterNode node = (FunctionClusterNode)object;
        return node.getDisplayName();
    }

    public Object[] getChildren(Object object)
    {
    	FunctionClusterNode node = (FunctionClusterNode)object;
        IModelFactory factory = ModelManagerImpl.getInstance().getModelFactory(node.getResource(), FunctionClusterModelFactory.TYPE);

        if(factory != null)
        	return factory.getModelParser(object, null).getChildren(node);
        return new Object[0];
    }

	public AbstractNode getOrCreateNode(IFileDelegate file, boolean forceCreate) {
		return null;
	}
//
//	public boolean hasChildren(Object element) {
//		return true;
//	}

	private static FunctionClusterWorkbenchAdapter instance = new FunctionClusterWorkbenchAdapter();
}
