package avicit.ui.runtime.core.node;

import avicit.ui.core.runtime.resource.IFileDelegate;

public class ControllerJavaNode extends AbstractFileNode
{

    public ControllerJavaNode(IFileDelegate folder)
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