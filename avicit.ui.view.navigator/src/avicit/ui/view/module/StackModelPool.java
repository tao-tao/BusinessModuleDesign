package avicit.ui.view.module;


import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.internal.corext.util.LRUMap;

import avicit.ui.core.runtime.resource.IResourceDelegate;
import avicit.ui.core.runtime.resource.IResourceDelegateDelta;
import avicit.ui.core.runtime.resource.ResourceDelegateDelta;
import avicit.ui.runtime.core.INode;

class StackModelPool implements IModelPool
{
    private class ModelCache
    {
        Object model;
        long timestamp;
        final StackModelPool modelPool;

        public ModelCache(Object model, long timestamp)
        {
        	modelPool = StackModelPool.this;
            this.model = model;
            this.timestamp = timestamp;
        }
    }

    StackModelPool()
    {
        maxModelPoolCount = IModelPool.DefaultMaxModelPoolCount;
        modelPool = new LRUMap(maxModelPoolCount);
        lockedModelPool = new HashMap();
        lockedEosModelPool = new HashMap();
        enable = true;
    }

    public boolean isEnable()
    {
        return enable;
    }

    public synchronized void setEnable(boolean enable)
    {
        if(this.enable == enable)
            return;
        this.enable = enable;
        if(this.enable)
        {
            modelPool.clear();
            modelPool.putAll(lockedModelPool);
            lockedModelPool.clear();
            lockedEosModelPool.clear();
        } else
        {
            lockedModelPool.putAll(modelPool);
        }
    }

    public int getMaxModelPoolCount()
    {
        return maxModelPoolCount;
    }

    public void setMaxModelPoolCount(int count)
    {
        count = Math.max(IModelPool.DefaultMaxModelPoolCount, count);
        LRUMap newMap = new LRUMap(count);
        newMap.putAll(modelPool);
        modelPool = newMap;
        maxModelPoolCount = count;
    }

    public boolean isInPool(IResourceDelegate fileDelegate, String type)
    {
        if(fileDelegate == null)
            return false;
        return doGetModel(type, fileDelegate) != null;
    }

    public boolean isValid(IResourceDelegate fileDelegate, String type)
    {
    	String key = fileDelegate.getResource().hashCode() + type;
        ModelCache modelCache = (ModelCache)modelPool.get(key);
        return modelCache != null && modelCache.timestamp == fileDelegate.getLastModified();
    }

    public boolean refresh(IResourceDelegate fileDelegate, String type)
    {
    	if(!fileDelegate.exists())
    		return false;
    	String key = fileDelegate.getResource().hashCode() + type;
        ModelCache modelCache = (ModelCache)modelPool.get(key);
        if(modelCache == null)
            return false;
        if(modelCache.timestamp == fileDelegate.getLastModified())
        {
            return false;
        } else
        {
            detachModel(fileDelegate, type);
            return true;
        }
    }

    private ModelCache doGetModel(String type, IResourceDelegate fileDelegate)
    {
    	if(fileDelegate.getResource() == null)
    		return null;
    	String key = fileDelegate.getResource().hashCode() + type;
        ModelCache modelCache = (ModelCache)modelPool.get(key);
        if(modelCache == null && !isEnable())
            modelCache = (ModelCache)lockedModelPool.get(key);
        return modelCache;
    }

//    private void detachModel(IFolderDelegate folderDelegate)
//    {
//        List fileList = new ArrayList();
//        for(Iterator iter = modelPool.keySet().iterator(); iter.hasNext();)
//        {
//            IResourceDelegate fileDelegate = (IResourceDelegate)iter.next();
//            if((fileDelegate instanceof EclipseResourceDelegate) && folderDelegate.isPrefixOf(fileDelegate))
//                fileList.add(fileDelegate);
//        }
//
//        IResourceDelegate fileDelegate;
//        for(Iterator iter = fileList.iterator(); iter.hasNext(); detachModel(fileDelegate))
//        {
//            fileDelegate = (IResourceDelegate)iter.next();
//        }
//    }

    public void detachModel(IResourceDelegate fileDelegate, String type)
    {
        if(fileDelegate == null)
            return;
        String key = fileDelegate.getResource().hashCode() + type;
        ModelCache modelCache = (ModelCache)modelPool.get(key);
        if(modelCache != null)
        {
            modelPool.remove(key);
            ModelChangeEvent event = new ModelChangeEvent(2, this, (IResourceDelegateDelta) new ResourceDelegateDelta(fileDelegate));
            ModelManagerImpl.getInstance().fireModelChanged(event);
        }
    }

    public synchronized void attachModel(Object model, IResourceDelegate fileDelegate)
    {
        if(model == null || fileDelegate == null)
            return;
        ModelCache modelCache = null;
        String type = null;
        if(model instanceof INode)
        {
        	type = ((INode)model).getType();
        	modelCache = doGetModel(type, fileDelegate);
        }
        else
        	modelCache = doGetModel(null, fileDelegate);
        
        ModelChangeEvent event;
        if(modelCache != null)
        {
            if(model.equals(modelCache.model) && modelCache.timestamp == fileDelegate.getLastModified())
                return;
            event = new ModelChangeEvent(4, this, (IResourceDelegateDelta) new ResourceDelegateDelta(fileDelegate));
            modelCache.model = model;
            modelCache.timestamp = fileDelegate.getLastModified();
        } else
        {
        	String key = fileDelegate.getResource().hashCode() + type;
            modelCache = new ModelCache(model, fileDelegate.getLastModified());
            modelPool.put(key, modelCache);
            event = new ModelChangeEvent(1, this, (IResourceDelegateDelta) new ResourceDelegateDelta(fileDelegate));
        }
        if(event != null)
            ModelManagerImpl.getInstance().fireModelChanged(event);
    }

    public Object getModel(IResourceDelegate fileDelegate, String type, boolean forceLoad, IProgressMonitor monitor)
    {
        if(fileDelegate == null)
            return null;
        ModelCache modelCache = doGetModel(type, fileDelegate);
        if(modelCache != null)
        {
            if(modelCache.timestamp == fileDelegate.getLastModified())
            {
                return modelCache.model;
            }
            if(forceLoad)
                return attachModel(fileDelegate, type, monitor);
        }
        if(forceLoad)
        	return attachModel(fileDelegate, type, monitor);
        return null;
    }

    private Object attachModel(IResourceDelegate fileDelegate, String type, IProgressMonitor monitor)
    {
        Object model = null;
        IModelFactory factory = ModelManagerImpl.getInstance().getModelFactory(fileDelegate, type);
        if(factory != null && factory.isAcceptable(fileDelegate))
        {
            IModelParser parser = ModelManagerImpl.getInstance().getModelParser(fileDelegate, type);
            model = parser.parse(fileDelegate, monitor);
        }
        if(model != null)
            attachModel(model, fileDelegate);
        return model;
    }

    private int maxModelPoolCount;
    private LRUMap modelPool;
    private Map lockedModelPool;
    private Map lockedEosModelPool;
    private boolean enable;
}