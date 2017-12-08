package avicit.ui.view.module;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

import avicit.ui.common.util.PropertyComparator;
import avicit.ui.core.runtime.resource.IFileDelegate;
import avicit.ui.core.runtime.resource.IFolderDelegate;
import avicit.ui.core.runtime.resource.IProjectDelegate;
import avicit.ui.core.runtime.resource.IResourceDelegate;
import avicit.ui.core.runtime.resource.ResourceHelper;
import avicit.ui.view.exception.ExceptionUtil;

public class ModelManagerImpl
    implements IModelManager, IResourceChangeListener, IResourceDeltaVisitor
{

	static int RESOURCE_FOLDER = IResource.FOLDER;
	static int RESOURCE_FILE = IResource.FILE;
	static int RESOURCE_PROJECT = IResource.PROJECT;
	
    private ModelManagerImpl()
    {
        listeners = new HashSet();
        timestamps = new HashMap();
		modelFactories = new HashMap();
        factories = new TreeSet(new PropertyComparator("priority"));
        pool = new StackModelPool();
        loadModelFactories();
    }

    private void loadModelFactories()
    {
        IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
        IExtensionPoint extensionPoint = extensionRegistry.getExtensionPoint("avicit.ui.view.navigator.modelFactory");
        IExtension extensions[] = extensionPoint.getExtensions();
        for(int i = 0; i < extensions.length; i++)
        {
            IConfigurationElement elements[] = extensions[i].getConfigurationElements();
            for(int j = 0; j < elements.length; j++)
            {
                IConfigurationElement element = elements[j];
                doLoadModelFactory(element);
            }
        }
    }

    private void doLoadModelFactory(IConfigurationElement element)
    {
        try
        {
            Object object = element.createExecutableExtension("class");
            String type = element.getAttribute("type");
            String category = element.getAttribute("category");
            String resource = element.getAttribute("resource");
            if(object instanceof IModelFactory)
            {
                IModelFactory factory = (IModelFactory)object;
                if(category != null)
                	factory.setCategory(category);
                if(resource != null)
                {
                	
                	factory.setResourceType("folder".equals(resource)?RESOURCE_FOLDER:"project".equals(resource)?RESOURCE_PROJECT:RESOURCE_FILE);
                	String exts = element.getAttribute("extensions");
                	if(exts != null)
                		factory.setExtensionNames(exts.split(";"));
                }
                registerFileFactory(/*type, */factory);
            }
        }
        catch(CoreException e)
        {e.printStackTrace();}
    }

    public synchronized  void registerFileFactory(/*String type, */IModelFactory factory)
    {
//        Assert.isLegal(StringUtils.isNotEmpty(type), "The type cannot be null");
        Assert.isNotNull(factory, "The factory cannot be null");
    	String key = factory.getType();
        if(key!=null)
        {
            IModelFactory oldFactory = (IModelFactory)modelFactories.get(key);
           // System.out.println(key+"   "+factory.getClass()+" "+oldFactory);
            if(oldFactory == null || factory.getPriority() > oldFactory.getPriority())
            {
            	 
                modelFactories.put(key, factory);
            }
        } 
//        else
        {
            factories.add(factory);
        }
    }

    public static ModelManagerImpl getInstance()
    {
        return instance;
    }

    public synchronized IModelFactory[] getAllModelFactories()
    {
        Set set = new HashSet();
        set.addAll(modelFactories.values());
        set.addAll(factories);
        return (IModelFactory[])set.toArray(new IModelFactory[set.size()]);
    }

//	@Override
	public IModelFactory getModelFactory(String modeltype) {
		String key = modeltype; 
		return modelFactories.get(key);
	}

    public IModelFactory getModelFactory(IResourceDelegate resourceDelegate, String type)
    {
    	IModelFactory modelFactory = null;
        if(type != null)
        {
        	 modelFactory = getModelFactory(type);
             if(modelFactory != null)
             	return modelFactory;
        }
       
    	boolean isFile = false;
    	boolean isProject = false;
    	boolean isFolder = true;
    	if(resourceDelegate instanceof IProjectDelegate)
    	{
    		isProject = true;
    		isFolder = false;
    	}
    	else if(resourceDelegate instanceof IFileDelegate)
    	{
    		isFile = true;
    		isFolder = false;
    		IFileDelegate fileDelegate = (IFileDelegate)resourceDelegate;
	        String extension = fileDelegate.getExtension();
	        if(extension == null)
	            return null;
	        extension = fileDelegate.getName();
	        int index = extension.indexOf(".");
	        if(index>0)
	        	extension = extension.substring(index+1);

	        for(Iterator iterator = modelFactories.values().iterator(); iterator.hasNext();)
	        {
	        	IModelFactory mf = (IModelFactory)iterator.next();
	        	if(ArrayUtils.contains(mf.getExtensionNames(), extension))
	        	{
	        		modelFactory = mf;
	        		break;
	        	}
	        }
    	}
    	
        if(modelFactory != null)
            return modelFactory;

        for(Iterator iterator = factories.iterator(); iterator.hasNext();)
        {
            modelFactory = (IModelFactory)iterator.next();
            if((isFile && modelFactory.getResourceType() == RESOURCE_FILE)
            	|| (isFolder && modelFactory.getResourceType() == RESOURCE_FOLDER)
            	|| (isProject && modelFactory.getResourceType() == RESOURCE_PROJECT))
            {
            	if(modelFactory.isAcceptable(resourceDelegate))
            		return modelFactory;
            }
        }
        return null;
    }

	public IModelParser getModelParser(IResourceDelegate file ,String type) {
		IModelFactory factory = getModelFactory(file, type);
		if(factory != null && factory.isAcceptable(file))
			return factory.getModelParser(file, null);
		return null;
	}

	public IModelValidator getModelValidator(IResourceDelegate file, String type) {
		IModelFactory factory = getModelFactory(file, type);
		if(factory != null)
			return factory.getValidator(file, null);
		return null;
	}

	public IModelCompiler getModelCompiler(IResourceDelegate file, String type) {
		IModelFactory factory = getModelFactory(file, type);
		if(factory != null && factory.isAcceptable(file))
			return factory.getCompiler(file, null);
		return null;
	}

	public boolean support(IFileDelegate file, String type) {
		IModelFactory factory = getModelFactory(file, type);
		if(factory != null)
			return factory.isAcceptable(file);
		return false;
	}
    
    public synchronized IModelPool getPool()
    {
        return pool;
    }

    public String[] getAllTypes()
    {
        String types[] = new String[modelFactories.size()];
        modelFactories.keySet().toArray(types);
        return types;
    }

    public void addModelChangeListener(IModelChangeListener listener)
    {
        if(listener != null)
            listeners.add(listener);
    }

    public void fireModelChanged(ModelChangeEvent event)
    {
        for(Iterator iterator = listeners.iterator(); iterator.hasNext();)
        {
            IModelChangeListener listener = (IModelChangeListener)iterator.next();
            try
            {
                listener.modelChanged(event);
            }
            catch(Exception e)
            {
                ExceptionUtil.getInstance().logException(e);
            }
        }
    }

    public IModelChangeListener[] getModelChangeListeners()
    {
        IModelChangeListener results[] = new IModelChangeListener[listeners.size()];
        listeners.toArray(results);
        return results;
    }

    public void removeModelChangeListener(IModelChangeListener listener)
    {
        if(listener != null)
            listeners.remove(listener);
    }

    public long getLastModified(IFolderDelegate folderDelegate)
    {
        if(!ResourceHelper.isValidResource(folderDelegate))
            return 0L;
        Long oldTimestamp = (Long)timestamps.get(folderDelegate.getFullPath());
        if(oldTimestamp == null)
            return folderDelegate.getLastModified();
        else
            return oldTimestamp.longValue();
    }

    public void resourceChanged(IResourceChangeEvent event)
    {
        if(event.getDelta() == null)
            return;
        try
        {
            event.getDelta().accept(this);
        }
        catch(CoreException e)
        {
            e.printStackTrace();
        }
    }

    public boolean visit(IResourceDelta delta)
        throws CoreException
    {
        if(delta == null)
            return true;
        IResource resource = delta.getResource();
        int kind = delta.getKind();
        if(resource.getType() == 1 && (".project".equals(resource.getName()) || ".classpath".equals(resource.getName()) || ".library".equals(resource.getName())))
        {
            updateParent(resource, false);
            return true;
        }
        if(1 == kind)
        {
            updateParent(resource, true);
            return false;
        }
        if(2 == kind)
        {
            updateParent(resource, false);
            return true;
        } else
        {
            return true;
        }
    }

    private void updateParent(IResource resource, boolean add)
    {
        IContainer container = resource.getParent();
        String fullPath = container.getFullPath().toString();
        Long currentTimestamp = (Long)timestamps.remove(fullPath);
        if(currentTimestamp == null)
            currentTimestamp = Long.valueOf(container.getLocalTimeStamp());
        currentTimestamp = Long.valueOf(container.getLocalTimeStamp() + 100L);
        timestamps.put(fullPath, currentTimestamp);
    }

    private static final ModelManagerImpl instance = new ModelManagerImpl();
    private Map<String, IModelFactory> modelFactories;
    private Set factories;
    private IModelPool pool;
    private Set listeners;
    private Map timestamps;
}