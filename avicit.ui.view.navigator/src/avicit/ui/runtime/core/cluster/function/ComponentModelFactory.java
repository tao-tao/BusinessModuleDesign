package avicit.ui.runtime.core.cluster.function;


import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.internal.ui.navigator.JavaNavigatorContentProvider;

import avicit.ui.core.runtime.resource.IFileDelegate;
import avicit.ui.core.runtime.resource.IFolderDelegate;
import avicit.ui.core.runtime.resource.IResourceDelegate;
import avicit.ui.runtime.core.INode;
import avicit.ui.runtime.core.subsystem.SubSystemNode;
import avicit.ui.view.exception.ModelParseException;
import avicit.ui.view.module.AbstractModelFactory;
import avicit.ui.view.module.IModelFactory;
import avicit.ui.view.module.IModelParser;
import avicit.ui.view.module.ModelManagerImpl;

/**
 * @author Tao Tao
 *
 */
@SuppressWarnings("restriction")
public class ComponentModelFactory extends AbstractModelFactory{

	public static String TYPE = "comp";
	
    private transient JavaNavigatorContentProvider javaContentProvider;
    
    public ComponentModelFactory()
    {
        setPriority(17000);
        javaContentProvider = new JavaNavigatorContentProvider(true);
        javaContentProvider.setIsFlatLayout(true);
        this.setType(TYPE);
    }

	@Override
	public boolean isAcceptable(IResourceDelegate ifiledelegate) {
		if(ifiledelegate instanceof IFolderDelegate)
		{
			IFolderDelegate folder = (IFolderDelegate) ifiledelegate;
			IFileDelegate f = folder.getFile(INode.CONFIG_FILE);
//			if(f.exists())
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
			return new ComponentNode((IFolderDelegate)ifiledelegate);
		}

		@Override
		public Object[] getChildren(INode object) {
			return createOrGetChildren(object, false);
		}
    }

    public static Object[] createOrGetChildren(INode object, boolean isNew){
    	if(object instanceof SubSystemNode)
    		return new ArrayList().toArray();

    	ComponentNode node = (ComponentNode)object;
        List cats = node.getChildren();

        if(cats == null || isNew)
        {
	        cats = new ArrayList();
	        IModelFactory[] factories = ModelManagerImpl.getInstance().getAllModelFactories();

	        for(int i=0;i<factories.length; i++)
	        {
	        	String cat = factories[i].getCategory();

	        	if(cat.equals("subview"))
	        	{
	        		if(factories[i].isAcceptable(node.getFolder()))
	        		{
	        			Object child = factories[i].getModelParser(node.getFolder(), null).parse(node.getFolder(), null);

	        			if(child instanceof INode)
	        				((INode)child).setParent(node);

	        			if(child != null){
	        				cats.add(child);
	        			}
	        		}
	        	}
	        }

	        if(!isNew)
	        	node.setChildren(cats);
        }

        return cats.toArray();
    }
}
