package avicit.ui.view.navigator;

import org.eclipse.core.resources.IResource;

public interface IResourceAnalyser
{

    public abstract IResource[] getRelevantResources(Object obj);
}
