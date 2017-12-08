package avicit.ui.runtime.core.requirement.analysis;


import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;

import avicit.ui.core.runtime.resource.EclipseFileDelegate;
import avicit.ui.core.runtime.resource.EclipseResourceDelegate;
import avicit.ui.core.runtime.resource.EclipseResourceManager;
import avicit.ui.core.runtime.resource.EclipseSourceFolderDelegate;
import avicit.ui.core.runtime.resource.IFileDelegate;
import avicit.ui.core.runtime.resource.IFolderDelegate;
import avicit.ui.core.runtime.resource.IResourceDelegate;
import avicit.ui.runtime.core.IModelCreator;
import avicit.ui.runtime.core.INode;
import avicit.ui.runtime.core.cluster.function.ComponentModelFactory;
import avicit.ui.runtime.core.cluster.function.ComponentNode;
import avicit.ui.runtime.core.node.AbstractNode;
import avicit.ui.runtime.core.requirement.analysis.epc.EpcNode;
import avicit.ui.view.exception.ModelParseException;
import avicit.ui.view.module.AbstractModelFactory;
import avicit.ui.view.module.IModelFactory;
import avicit.ui.view.module.IModelParser;
import avicit.ui.view.module.ModelManagerImpl;

public class AnalysisModelFactory extends AbstractModelFactory implements IModelCreator{

	public static String TYPE = "analysis";

	public AnalysisModelFactory() {
		setPriority(17000);
		this.setType(TYPE);
	}

	@Override
	public boolean isAcceptable(IResourceDelegate ifiledelegate) {
		if (ifiledelegate instanceof IFolderDelegate) {
			IFolderDelegate folder = (IFolderDelegate) ifiledelegate;
			ComponentNode model = (ComponentNode) ModelManagerImpl.getInstance().getPool().getModel(folder, ComponentModelFactory.TYPE, true, null);
			String m = model.getIncludeModules();
			if (m != null && m.contains("analysis"))
				return true;
		}
		return false;
	}

	@Override
	public IModelParser getModelParser(Object obj, IProgressMonitor iprogressmonitor) {
		return new ModelParser();
	}

	public class ModelParser extends AbstractModelFactory.ModelParser {

		@Override
		public Object createNewNode(IResourceDelegate ifiledelegate) throws ModelParseException {
			if (ifiledelegate instanceof IFolderDelegate)
				return new AnalysisNode((IFolderDelegate) ifiledelegate);
			return null;
		}

		@Override
		public Object[] getChildren(INode object) {
			AnalysisNode node = (AnalysisNode) object;
			List chidren = node.getChildren();
			if (chidren == null) {
				List presFiles = new ArrayList();
				IModelFactory[] factories = ModelManagerImpl.getInstance().getAllModelFactories();
		        for(int i=0;i<factories.length; i++)
		        {
		        	String cat = factories[i].getCategory();
		        	if(cat.equals("analysis"))
		        	{
		        		AbstractNode child = (AbstractNode) factories[i].getModelParser(node.getFolder(), null).parse(node.getFolder(), null);
		        		if(child != null)
		        		{
		        			child.setParent(node);
		        			presFiles.add(child);
		        		}
		        	}
		        }
		        String fileID = "WebRoot/WEB-INF/_platform/applicationContext-common.xml";
		        IFile file = node.getResource().getResource().getProject().getFile(new Path(fileID));
		        EpcNode child = new EpcNode(new EclipseFileDelegate(file));
		        child.setEditorID("avicit.ui.model.editor.ModelEditor");
		        presFiles.add(child);
				node.setChildren(presFiles);
				return presFiles.toArray();
			}
			return chidren.toArray();
		}
	}
	
	public AbstractNode getOrCreateNode(IFileDelegate file, boolean forceCreate) {
		EclipseSourceFolderDelegate sourceFolder = EclipseResourceManager.getSourceFolder((EclipseResourceDelegate) file);
		if(sourceFolder == null)
			return null;
		INode model = (INode) ModelManagerImpl.getInstance().getPool().getModel(sourceFolder, AnalysisModelFactory.TYPE, true, null);
		if(model != null)
		{
			this.getModelParser(model, null).getChildren(model);
			IModelFactory[] factories = ModelManagerImpl.getInstance().getAllModelFactories();
	        for(int i=0;i<factories.length; i++)
	        {
	        	String cat = factories[i].getCategory();
	        	if(cat.equals("goujian") && (factories[i] instanceof IModelCreator))
	        	{
	        		AbstractNode child = ((IModelCreator)factories[i]).getOrCreateNode(file, forceCreate);
	        		if(child != null)
	        			return child;
	        	}
	        }
		}
		return null;
	}

}
