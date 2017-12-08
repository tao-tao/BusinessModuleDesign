package avicit.ui.runtime.core;


import org.eclipse.core.runtime.IAdaptable;

import avicit.ui.core.runtime.resource.IResourceDelegate;

public interface INode
    extends IAdaptable, IOrderable, INamingElement
{

	public static String CONFIG_FILE = "META-INF/.ec";
	
	public static String CLUSTER_FILE = ".clu";

	public static String SUBSYSTEM_DESC = ".sub";

	public static String MODULE_DESC = ".ec";

	public abstract Object getParent();

	public abstract void setParent(Object parent);

	public abstract IResourceDelegate getResource();

    public abstract Object getModel();

    public abstract boolean isVirtual();

    public abstract String getType();
    
    public abstract void setType(String type);

    public static final Object NO_CHILDREN[] = new Object[0];
}