package avicit.ui.view.module;


import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import avicit.ui.core.runtime.resource.IFolderDelegate;
import avicit.ui.core.runtime.resource.IResourceDelegate;
import avicit.ui.runtime.core.INode;
import avicit.ui.runtime.core.cluster.function.ComponentModelFactory;
import avicit.ui.runtime.core.cluster.function.ComponentNode;
import avicit.ui.runtime.core.node.ComponentLibNode;
import avicit.ui.view.exception.ModelParseException;

public class ComponentLibModelFactory extends AbstractModelFactory{

	public static String TYPE = "complib";
    
    public ComponentLibModelFactory()
    {
        setPriority(17000);
        this.setType(TYPE);
    }

	@Override
	public boolean isAcceptable(IResourceDelegate ifiledelegate) {
		if(ifiledelegate instanceof IFolderDelegate)
		{
			if(!ifiledelegate.getName().startsWith("."))
				return true;
		}
		return false;
	}

	@Override
	public IModelParser getModelParser(Object obj,
			IProgressMonitor iprogressmonitor) {
		return new ModelParser();
	}
	
    public class ModelParser extends AbstractModelFactory.ModelParser{

		@Override
		protected Object createNewNode(IResourceDelegate ifiledelegate) throws ModelParseException {
			return new ComponentLibNode((IFolderDelegate)ifiledelegate);
		}

		@Override
		public Object[] getChildren(INode object) {
			return createOrGetChildren(object, false);
		}
    }
    
    public static Object[] createOrGetChildren(INode object, boolean isNew){
        ComponentLibNode node = (ComponentLibNode)object;
        List cats = node.getChildren();
        if(cats == null)
        {
	        cats = new ArrayList();
	        IResourceDelegate[] vers = ((IFolderDelegate)node.getResource()).getChildren();
	        for(int i=0;i<vers.length; i++)
	        {
	        	if(vers[i] instanceof IFolderDelegate)
	        	{
		        	Object model =  ModelManagerImpl.getInstance().getPool().getModel(vers[i], ComponentModelFactory.TYPE, true, null);
		        	if(model instanceof ComponentNode)
		        	{
		        		((ComponentNode)model).setParent(node);
			        	cats.add(model);
		        	}
	        	}
	        }
	        node.setChildren(cats);
        }
        return null;
    }
}
