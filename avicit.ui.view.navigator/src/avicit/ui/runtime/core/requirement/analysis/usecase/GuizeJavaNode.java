package avicit.ui.runtime.core.requirement.analysis.usecase;

import avicit.ui.runtime.core.node.AbstractFileNode;
import avicit.ui.core.runtime.resource.IFileDelegate;

public class GuizeJavaNode extends AbstractFileNode
{

    public GuizeJavaNode(IFileDelegate folder)
    {
        super(folder);
        setOrder(2);
    }

    public Object getModel()
    {
        return this.getFile();
    }

    @Override
	public String getDisplayName() {
		return this.getFile().getName();
	}

	public String getType()
    {
        return "com.tansun.component.javaSource";
    }

}