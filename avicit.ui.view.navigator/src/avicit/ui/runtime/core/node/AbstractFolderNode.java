package avicit.ui.runtime.core.node;

import java.util.List;

import avicit.ui.core.runtime.resource.IFolderDelegate;

public abstract class AbstractFolderNode extends AbstractResourceNode
{
	public List<?> children /*= new ArrayList()*/;

    public AbstractFolderNode(IFolderDelegate folder)
    {
        super(folder);
    }

    public IFolderDelegate getFolder()
    {
        return (IFolderDelegate)getResource();
    }

	public List getChildren() {
		return children;
	}

	public void setChildren(List<?> children) {
		this.children = children;
	}
}