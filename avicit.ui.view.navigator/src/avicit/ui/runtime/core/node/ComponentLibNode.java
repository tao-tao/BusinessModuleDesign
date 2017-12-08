package avicit.ui.runtime.core.node;

import com.tansun.runtime.resource.adapter.ComponentModelCreatorAdapter;

import avicit.ui.core.runtime.resource.IFileDelegate;
import avicit.ui.core.runtime.resource.IFolderDelegate;
import avicit.ui.runtime.core.IModelCreator;
import avicit.ui.view.module.ComponentLibModelFactory;

public class ComponentLibNode extends AbstractFolderNode implements IModelCreator
{
	
    public ComponentLibNode(IFolderDelegate folder)
    {
        super(folder);
        setOrder(1);
    }
    
	public String getType()
    {
        return ComponentLibModelFactory.TYPE;
    }

    public Object getParent()
    {
    	if(parent instanceof ProjectNode)
    		return ((ProjectNode)parent).getProject().getResource();
    	return parent;
    }

    public Object getParentNode()
    {
    	return parent;
    }

//	@Override
	public AbstractNode getOrCreateNode(IFileDelegate file, boolean forceCreate) {
		return ComponentModelCreatorAdapter.getInstance().getOrCreateNode(file, forceCreate);
	}

}