package avicit.ui.runtime.core.node;


import org.apache.commons.lang.ArrayUtils;
import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;

import avicit.ui.core.runtime.resource.IFileDelegate;
import avicit.ui.core.runtime.resource.IFolderDelegate;
import avicit.ui.core.runtime.resource.IResourceDelegate;
import avicit.ui.runtime.core.IModelCreator;
import avicit.ui.runtime.core.IPackageContainer;
import avicit.ui.view.module.GuizeModelFactory;
import avicit.ui.view.module.IModelFactory;
import avicit.ui.view.module.ModelManagerImpl;



public class GuizeNode extends AbstractFolderNode implements IPackageContainer
{

    public GuizeNode(IFolderDelegate folder)
    {
        super(folder);
        setOrder(0);
    }

    @Override
	public String getDisplayName() {
		return "测试";
	}
    
//  @Override
	public String getType()
    {
        return GuizeModelFactory.TYPE;
    }

	public String getConfigPath() {
		return this.getResource().getProjectRelativePath() + "/META-INF/biz";
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
		else if(IResource.class == adapter)
		{
            return this.getFolder().getResource();
		}
		else if(IPackageContainer.class == adapter)
		{
            return this;
		}
		return null;
    }
    public void createAction(IStructuredSelection selection,IMenuManager menu){
    	Action guizeAction = new Action("ҵ���߼�");
    	guizeAction.selectionChanged(selection);
		menu.appendToGroup("group.edit", guizeAction);
    }
    public Object getAdapter(Object adaptableObject, Class adapterType) {
    	return GuizeWorkbenchAdapter.getInstance();
    }
    
    public AbstractNode getOrCreateNode(IFileDelegate file, boolean forceCreate) {
      	IFolderDelegate sourceFolder=null;
  	
  			try {
  				sourceFolder = file.getParent().getParent();
  			} catch (ResourceException e) {
  				// TODO Auto-generated catch block
  				e.printStackTrace();
  			}
  		
  		if(sourceFolder == null)
  			return null;
  		
  		Object model = ModelManagerImpl.getInstance().getPool().getModel(sourceFolder, GuizeModelFactory.TYPE, true, null);
  		if(model instanceof GuizeNode)
  		{
  			IModelFactory[] factories = ModelManagerImpl.getInstance().getAllModelFactories();
  			String ext = file.getName();
  			int j = ext.indexOf("."); 
  			if(j>0)
  				ext = ext.substring(j+1);
  	        for(int i=0;i<factories.length; i++)
  	        {
  	        	String cat = factories[i].getCategory();
  	        	String[] exts = factories[i].getExtensionNames();
  	        	if(cat.equals("topdesigner"))
  	        	{
  	        		if(ArrayUtils.contains(exts, ext))
  	        		{
  	        			Object pmodel = ModelManagerImpl.getInstance().getPool().getModel(sourceFolder, factories[i].getType(), forceCreate, null);
  	        			if(pmodel != null && factories[i] instanceof IModelCreator)
  	        			{
  	        				return ((IModelCreator)factories[i]).getOrCreateNode(file, forceCreate);
  	        			}
  	        		}
//  	        		if(factories[i].isAcceptable(node.getFolder()))
//  	        		{
//  		        		cats.add(factories[i].getModelParser(node.getFolder(), null).parse(node.getFolder(), null));
//  	        		}
  	        	}
  	        }
  		}
  		return null;
  	}    
}