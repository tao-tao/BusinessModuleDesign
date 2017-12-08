package avicit.ui.runtime.core.requirement.designer.bpmdesigner;




import com.tansun.runtime.resource.adapter.AbstractModelWorkbenchAdapter;

public class BpmDesignerNodeAdaptper extends AbstractModelWorkbenchAdapter
{

    private BpmDesignerNodeAdaptper()
    {
    }

    public static BpmDesignerNodeAdaptper getInstance()
    {
        return instance;
    }

    public String getLabel(Object object)
    {
    	BpmDesignerNode node = (BpmDesignerNode)object;
        return node.getDisplayName();
    }

    public Object[] getChildren(Object object)
    {
    	return new Object[0];
    }

	public boolean hasChildren(Object element) {
		return false;
	}

    private static final BpmDesignerNodeAdaptper instance = new BpmDesignerNodeAdaptper();
}