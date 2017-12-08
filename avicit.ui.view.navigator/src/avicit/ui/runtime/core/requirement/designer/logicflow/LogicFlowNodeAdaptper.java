package avicit.ui.runtime.core.requirement.designer.logicflow;




import com.tansun.runtime.resource.adapter.AbstractModelWorkbenchAdapter;

public class LogicFlowNodeAdaptper extends AbstractModelWorkbenchAdapter
{

    private LogicFlowNodeAdaptper()
    {
    }

    public static LogicFlowNodeAdaptper getInstance()
    {
        return instance;
    }

    public String getLabel(Object object)
    {
    	LogicFlowNode node = (LogicFlowNode)object;
        return node.getDisplayName();
    }

    public Object[] getChildren(Object object)
    {
    	return new Object[0];
    }

	public boolean hasChildren(Object element) {
		return false;
	}

    private static final LogicFlowNodeAdaptper instance = new LogicFlowNodeAdaptper();
}