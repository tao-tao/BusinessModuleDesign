package avicit.ui.runtime.core.requirement.requirement.document;

import avicit.ui.runtime.core.INode;
import avicit.ui.view.module.IModelFactory;
import avicit.ui.view.module.ModelManagerImpl;

import com.tansun.runtime.resource.adapter.AbstractModelWorkbenchAdapter;

/**
 * @author TaoTao
 *
 */
public class DocumentWorkbenchAdapter extends AbstractModelWorkbenchAdapter {

	@Override
	public boolean hasChildren(Object element) {
		return false;
	}

	public DocumentWorkbenchAdapter() {
		super();
	}

	public static DocumentWorkbenchAdapter getInstance(){
		return instance;
	}

	@Override
	public String getLabel(Object object) {
		DocumentNode node = (DocumentNode) object;
		return node.getDisplayName();
	}

	@Override
	public Object[] getChildren(Object object) {
    	INode node = (INode)object;
        IModelFactory factory = ModelManagerImpl.getInstance().getModelFactory(DocumentModelFactory.TYPE);;
        if(factory != null)
        	return factory.getModelParser(object, null).getChildren(node);
        return new Object[0];
	}

	private static final DocumentWorkbenchAdapter instance = new DocumentWorkbenchAdapter();
}
