package avicit.ui.runtime.core.requirement.designer.erm;




import com.tansun.runtime.resource.adapter.AbstractModelWorkbenchAdapter;

public class ErNodeAdaptper extends AbstractModelWorkbenchAdapter
{

    private ErNodeAdaptper()
    {
    }

    public static ErNodeAdaptper getInstance()
    {
        return instance;
    }

    public String getLabel(Object object)
    {
    	ErNode node = (ErNode)object;
        return node.getDisplayName();
    }

    public Object[] getChildren(Object object)
    {
    	return new Object[0];
    }

	public boolean hasChildren(Object element) {
		return false;
	}

    private static final ErNodeAdaptper instance = new ErNodeAdaptper();
}