package avicit.ui.runtime.core.node;

import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jface.viewers.IDoubleClickListener;

import avicit.ui.common.util.JdtUtil;
import avicit.ui.core.runtime.resource.IResourceDelegate;
import avicit.ui.runtime.core.action.OpenJavaBeanAction;

public class SysSpringNode extends AbstractVirtualNode{
	
	protected Object parent;
	protected String type;
	
	private String id = "";
	private String name = "";
	private String clazz = "";
	private String inf = "";

	ICompilationUnit cu;
	
//	@Override
	public Object getModel() {
		return null;
	}
	
//	@Override
	public Object getParent() {
		return parent;
	}
	
//	@Override
	public IResourceDelegate getResource() {
		if(parent != null && parent instanceof SpringNode)
		{
			return ((SpringNode)parent).getResource();
		}
		else if(parent != null && parent instanceof DaoSpringNode)
		{
			return ((DaoSpringNode)parent).getResource();
		}else if(parent != null && parent instanceof XmlNode)
		{
			return ((XmlNode)parent).getResource();
		}
		return null;
	}
	
//	@Override
	public String getType() {
		return this.type;
	}
	
//	@Override
	public void setParent(Object parent) {
		this.parent = parent; 
	}
	
//	@Override
	public void setType(String type) {
		this.type = type;
	}
	
//	@Override
	public String getName() {
		return id;
	}

	public boolean equals(Object arg0) {
		if(!(arg0 instanceof SysSpringNode))
			return false;
		if(this.id != null)
			return id.equals(((SysSpringNode)arg0).id);
		return super.equals(arg0);
	}

    public Object getAdapter(Class adapter)
    {
        if(IResourceDelegate.class == adapter)
            return getResource();
        if(IProject.class == adapter)
        {
			try {
				return getResource().getProject().getAdapter(adapter);
			} catch (ResourceException e) {
				return null;
			}
        }
        if(IDoubleClickListener.class == adapter)
        {
        	return new OpenJavaBeanAction();
        }
        else
		{
            return super.getAdapter(adapter);
		}
    }
    
    public ICompilationUnit getCU(){
    	if(cu == null)
    	{
			try {
				cu = JdtUtil.getCompilationUnit((IProject) this.getResource().getProject().getResource(), clazz);
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
		return cu;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
		getCU();
		if(cu != null && cu.exists())
		{
			this.setOrder(1);
		}
	}

	public String getInf() {
		return inf;
	}

	public void setInf(String inf) {
		this.inf = inf;
	}

	public void setName(String name) {
		this.name = name;
	}
    
}
