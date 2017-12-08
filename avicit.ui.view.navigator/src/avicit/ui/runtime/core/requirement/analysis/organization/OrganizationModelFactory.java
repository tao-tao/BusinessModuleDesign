package avicit.ui.runtime.core.requirement.analysis.organization;


import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;

import avicit.ui.common.util.FileUtil;
import avicit.ui.core.runtime.resource.EclipseFileDelegate;
import avicit.ui.core.runtime.resource.EclipseFolderDelegate;
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
import avicit.ui.runtime.core.node.PackageNode;
import avicit.ui.runtime.core.requirement.designer.erm.ErNode;
import avicit.ui.view.exception.ModelParseException;
import avicit.ui.view.module.AbstractModelFactory;
import avicit.ui.view.module.IModelFactory;
import avicit.ui.view.module.IModelParser;
import avicit.ui.view.module.ModelManagerImpl;

public class OrganizationModelFactory extends AbstractModelFactory implements IModelCreator{

	public static String TYPE = "organization";

	public OrganizationModelFactory() {
		setPriority(17000);
		this.setType(TYPE);
	}

	@Override
	public boolean isAcceptable(IResourceDelegate ifiledelegate) {
		if (ifiledelegate instanceof IFolderDelegate) {
			IFolderDelegate folder = (IFolderDelegate) ifiledelegate;
			ComponentNode model = (ComponentNode) ModelManagerImpl.getInstance().getPool().getModel(folder, ComponentModelFactory.TYPE, true, null);
			String m = model.getIncludeModules();
			if (m != null && m.contains("organization"))
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
			if (ifiledelegate instanceof IFolderDelegate){
				IFolder resource = getConfigFolder((IFolderDelegate) ifiledelegate);
				return new OrganizationNode(new EclipseFolderDelegate(resource));
			}
			return null;
		}

		@Override
		public Object[] getChildren(INode object) {
			OrganizationNode node = (OrganizationNode) object;
			List chidren = node.getChildren();
			if (true) {
				List presFiles = new ArrayList();
				FileUtil.listFiles((IFolder) node.getResource().getResource(), presFiles, "", new String[]{".erd"}, null);
				for (int i = 0; i < presFiles.size(); i++) {
					PackageNode f = (PackageNode) presFiles.get(i);
					f.setParent(node);
					f.setType(TYPE);
					List files = f.getChildren();
					if (files.size() > 0) {
						List pages = new ArrayList();
						for (int j = 0; j < files.size(); j++) {
							IFile file = (IFile) files.get(j);
							INode p = null;
							
							if(file.getName().endsWith("erd"))
								p = new ErNode(new EclipseFileDelegate(file));
							p.setParent(f);
							pages.add(p);
						}
						f.setChildren(pages);
					}
				}
				node.setChildren(presFiles);
				return presFiles.toArray();
			}
			return chidren.toArray();
		}
	}
	
	private IFolder getConfigFolder(IResourceDelegate ifiledelegate){
		IProject project = null;		
		IFolder folder = null;
		try {
			project = (IProject)ifiledelegate.getProject().getResource();
			try {
				folder = project.getFolder(new Path("META-INF"));
				if(!folder.exists())
					folder.create(true, true, null);
				folder = folder.getFolder(new Path("analysis"));
				if(!folder.exists())
					folder.create(true, true, null);
				folder = folder.getFolder(new Path("organization"));
				if(!folder.exists())
					folder.create(true, true, null);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		} catch (ResourceException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return folder;
	}
	
	public AbstractNode getOrCreateNode(IFileDelegate file, boolean forceCreate) {
		EclipseSourceFolderDelegate sourceFolder = EclipseResourceManager.getSourceFolder((EclipseResourceDelegate) file);
		if(sourceFolder == null)
			return null;
		INode model = (INode) ModelManagerImpl.getInstance().getPool().getModel(sourceFolder, OrganizationModelFactory.TYPE, true, null);
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
