package avicit.ui.runtime.core.requirement.analysis;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import avicit.ui.common.util.IPlatformConstants;
import avicit.ui.core.runtime.resource.EclipseFileDelegate;
import avicit.ui.runtime.core.INode;
import avicit.ui.runtime.core.node.CommonChildNode;
import avicit.ui.runtime.core.node.CommonNode;
import avicit.ui.view.module.IModelFactory;
import avicit.ui.view.module.ModelManagerImpl;

import com.tansun.runtime.resource.adapter.AbstractModelWorkbenchAdapter;

public class AnalysisWorkbenchAdapter extends AbstractModelWorkbenchAdapter
{

	@Override
	public boolean hasChildren(Object element) {
		return true;
	}

    public AnalysisWorkbenchAdapter()
    {
    }

    public static AnalysisWorkbenchAdapter getInstance()
    {
        return instance;
    }

    @Override
	public Object getParent(Object object) {
		return super.getParent(object);
	}

	public String getLabel(Object object)
    {
		AnalysisNode node = (AnalysisNode)object;
        return node.getDisplayName();
    }

    public Object[] getChildren(Object object)
    {
    	INode node = (INode)object;
        IModelFactory factory = ModelManagerImpl.getInstance().getModelFactory(AnalysisModelFactory.TYPE);;
        if(factory != null)
        	return factory.getModelParser(object, null).getChildren(node);
        return new Object[0];
    }

    private static final AnalysisWorkbenchAdapter instance = new AnalysisWorkbenchAdapter();

}