package avicit.ui.view.module;

import avicit.ui.core.runtime.resource.IFolderDelegate;
import avicit.ui.core.runtime.resource.IResourceDelegate;



public interface IModelManager
{

    public abstract IModelFactory getModelFactory(IResourceDelegate ifiledelegate, String type);

    public abstract IModelFactory getModelFactory(/*String type, */String modeltype);

    public abstract IModelFactory[] getAllModelFactories();

    public abstract IModelPool getPool();

    public abstract String[] getAllTypes();

    public abstract void addModelChangeListener(IModelChangeListener imodelchangelistener);

    public abstract void removeModelChangeListener(IModelChangeListener imodelchangelistener);

    public abstract IModelChangeListener[] getModelChangeListeners();

    public abstract void fireModelChanged(ModelChangeEvent modelchangeevent);

    public abstract long getLastModified(IFolderDelegate ifolderdelegate);
}