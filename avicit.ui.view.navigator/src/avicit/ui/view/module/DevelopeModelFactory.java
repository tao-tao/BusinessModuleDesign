package avicit.ui.view.module;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;

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
import avicit.ui.runtime.core.node.DevelopNode;
import avicit.ui.view.exception.ModelParseException;

/**
 * @author Tao Tao
 *
 */
public class DevelopeModelFactory extends AbstractModelFactory implements IModelCreator{

	public static String TYPE = "jcc";

	public DevelopeModelFactory() {
		setPriority(17000);
		this.setType(TYPE);
	}

	@Override
	public boolean isAcceptable(IResourceDelegate ifiledelegate) {
		if (ifiledelegate instanceof IFolderDelegate) {
			IFolderDelegate folder = (IFolderDelegate) ifiledelegate;
			ComponentNode model = (ComponentNode) ModelManagerImpl.getInstance().getPool().getModel(folder, ComponentModelFactory.TYPE, true, null);
			String m = model.getIncludeModules();
			if (m != null && m.contains("jcc"))
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
				return new DevelopNode((IFolderDelegate) ifiledelegate);
			return null;
		}

		@Override
		public Object[] getChildren(INode object) {
			DevelopNode node = (DevelopNode) object;
			List chidren = node.getChildren();
			if (chidren == null) {
				List presFiles = new ArrayList();
				IModelFactory[] factories = ModelManagerImpl.getInstance().getAllModelFactories();
		        for(int i=0;i<factories.length; i++)
		        {
		        	String cat = factories[i].getCategory();
		        	if(cat.equals("subview"))
		        	{
		        		AbstractNode child = (AbstractNode) factories[i].getModelParser(node.getFolder(), null).parse(node.getFolder(), null);
		        		if(child != null)
		        		{
		        			child.setParent(node);
		        			presFiles.add(child);
		        		}
		        	}
		        }
				node.setChildren(presFiles);
				return presFiles.toArray();
			}
			return chidren.toArray();
		}
	}
	
	private IFolder getConfigFolder(DevelopNode node){
		IFolder folder =  (IFolder)node.getResource().getResource();
		try {
			if(!folder.exists())
				folder.create(true, true, null);
			folder = folder.getFolder(new Path("META-INF"));
			if(!folder.exists())
				folder.create(true, true, null);
			folder = folder.getFolder(new Path("spring"));
			if(!folder.exists())
				folder.create(true, true, null);
//			folder = folder.getFolder(new Path("service"));
//			if(!folder.exists())
//				folder.create(true, true, null);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return folder;
	}

	public AbstractNode getOrCreateNode(IFileDelegate file, boolean forceCreate) {
		EclipseSourceFolderDelegate sourceFolder = EclipseResourceManager.getSourceFolder((EclipseResourceDelegate) file);
		if(sourceFolder == null)
			return null;
		INode model = (INode) ModelManagerImpl.getInstance().getPool().getModel(sourceFolder, DevelopeModelFactory.TYPE, true, null);
		if(model != null)
		{
			this.getModelParser(model, null).getChildren(model);
			IModelFactory[] factories = ModelManagerImpl.getInstance().getAllModelFactories();
	        for(int i=0;i<factories.length; i++)
	        {
	        	String cat = factories[i].getCategory();
	        	if(cat.equals("jicheng") && (factories[i] instanceof IModelCreator))
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
