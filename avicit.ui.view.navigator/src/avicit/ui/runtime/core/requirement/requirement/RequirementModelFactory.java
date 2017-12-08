package avicit.ui.runtime.core.requirement.requirement;


import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;

import avicit.ui.core.runtime.resource.EclipseFolderDelegate;
import avicit.ui.core.runtime.resource.EclipseResourceDelegate;
import avicit.ui.core.runtime.resource.EclipseResourceManager;
import avicit.ui.core.runtime.resource.EclipseSourceFolderDelegate;
import avicit.ui.core.runtime.resource.IFileDelegate;
import avicit.ui.core.runtime.resource.IFolderDelegate;
import avicit.ui.core.runtime.resource.IResourceDelegate;
import avicit.ui.runtime.core.IModelCreator;
import avicit.ui.runtime.core.INode;
import avicit.ui.runtime.core.node.AbstractNode;
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
public class RequirementModelFactory extends AbstractModelFactory implements IModelCreator{

	public static String TYPE = "requirement";

	public RequirementModelFactory() {
		setPriority(17000);
		this.setType(TYPE);
	}

	@Override
	public boolean isAcceptable(IResourceDelegate ifiledelegate) {
//		if (ifiledelegate instanceof IFolderDelegate) {
//			IFolderDelegate folder = (IFolderDelegate) ifiledelegate;
//			ComponentNode model = (ComponentNode) ModelManagerImpl.getInstance().getPool().getModel(folder, ComponentModelFactory.TYPE, true, null);
//			String m = model.getIncludeModules();
//			if (m != null && m.contains("ywc"))
//				return true;
//		}
//		return false;
		return true;
	}

	@Override
	public IModelParser getModelParser(Object obj, IProgressMonitor iprogressmonitor) {
		return new ModelParser();
	}

	public class ModelParser extends AbstractModelFactory.ModelParser {

		@Override
		public Object createNewNode(IResourceDelegate ifiledelegate) throws ModelParseException {
			if (ifiledelegate instanceof IFolderDelegate){
				IFolder resource = getConfigFolder((IFolderDelegate) ifiledelegate);
				return new RequirementNode(new EclipseFolderDelegate(resource));
			}
				
			return null;
		}

		protected IFolder getConfigFolder(IResourceDelegate ifiledelegate){

			String path = ifiledelegate.getProjectRelativePath();
			int i = path.indexOf("META-INF");
			if(i>=0)
				path = path.substring(0, i);
					
				IFolder folder = null;
				try {
					folder = (IFolder)ifiledelegate.getProject().getFolder(path).getResource();
					try {
						if(!folder.exists())
							folder.create(true, true, null);
						folder = folder.getFolder(new Path("META-INF"));
						if(!folder.exists())
							folder.create(true, true, null);
						folder = folder.getFolder(new Path("requirement"));
						if(!folder.exists())
							folder.create(true, true, null);
					} catch (CoreException e) {
						e.printStackTrace();
					}
				} catch (ResourceException e1) {
			}
			return folder;
		}

		@Override
		public Object[] getChildren(INode object) {
			RequirementNode node = (RequirementNode) object;
			List chidren = node.getChildren();

			if (chidren == null) {
				List presFiles = new ArrayList();
				IModelFactory[] factories = ModelManagerImpl.getInstance().getAllModelFactories();

		        for(int i=0;i<factories.length; i++)
		        {
		        	String cat = factories[i].getCategory();

		        	if(cat.equals("subview1"))
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

	public AbstractNode getOrCreateNode(IFileDelegate file, boolean forceCreate) {
		EclipseSourceFolderDelegate sourceFolder = EclipseResourceManager.getSourceFolder((EclipseResourceDelegate) file);
		if(sourceFolder == null)
			return null;
		INode model = (INode) ModelManagerImpl.getInstance().getPool().getModel(sourceFolder, RequirementModelFactory.TYPE, true, null);
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
