package avicit.ui.runtime.core.requirement.designer.classm;




import com.tansun.runtime.resource.adapter.AbstractModelWorkbenchAdapter;

public class ClassNodeAdaptper extends AbstractModelWorkbenchAdapter
{

    private ClassNodeAdaptper()
    {
    }

    public static ClassNodeAdaptper getInstance()
    {
        return instance;
    }

    public String getLabel(Object object)
    {
    	ClassNode node = (ClassNode)object;
        return node.getDisplayName();
    }

    public Object[] getChildren(Object object)
    {
    	return new Object[0];
    }

	public boolean hasChildren(Object element) {
		return false;
	}

    private static final ClassNodeAdaptper instance = new ClassNodeAdaptper();
}