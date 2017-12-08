package avicit.ui.runtime.core.requirement.requirement.function;


import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
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
import avicit.ui.runtime.core.node.AbstractNode;
import avicit.ui.runtime.core.node.PackageNode;
import avicit.ui.runtime.core.requirement.analysis.usecase.FunctionUseCaseNode;
import avicit.ui.view.exception.ModelParseException;
import avicit.ui.view.module.AbstractModelFactory;
import avicit.ui.view.module.IModelFactory;
import avicit.ui.view.module.IModelParser;
import avicit.ui.view.module.ModelManagerImpl;

/**
 * @author TaoTao
 *
 */
public class FunctionStructureModelFactory extends AbstractModelFactory implements IModelCreator{

	public static String TYPE = "functionStructure";

	public FunctionStructureModelFactory() {
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
				return new FunctionStructureNode(new EclipseFolderDelegate(resource));
			}

			return null;
		}

		protected IFolder getConfigFolder(IResourceDelegate ifiledelegate){

			String path = ifiledelegate.getProjectRelativePath();

			IFolder folder = null;
			try {
				folder = (IFolder) ifiledelegate.getProject().getFolder(path).getResource();

				if (!folder.exists())
					folder.create(true, true, null);

				folder = folder.getFolder(new Path("functionStructure"));

				if (!folder.exists())
					folder.create(true, true, null);

			} catch (Exception e) {
				e.printStackTrace();
			}

				return folder;
		}

		@Override
		public Object[] getChildren(INode object) {
			FunctionStructureNode node = (FunctionStructureNode) object;
			List children = node.getChildren();

			List childList = new ArrayList();
			
			List presFiles = new ArrayList();
			FileUtil.listFiles((IFolder) node.getResource().getResource(), presFiles, "", new String[]{".function"}, null);
			PackageNode pn = null;

			if (presFiles.size() > 0) {
				pn = (PackageNode) presFiles.remove(presFiles.size() - 1);
				List fs = pn.getChildren();
				if (fs.size() > 0) {
					for (int j = 0; j < fs.size(); j++) {
						IFile file = (IFile) fs.get(j);
						INode p = null;
						p = new FunctionUseCaseNode(new EclipseFileDelegate(file));
						p.setParent(node);
						childList.add(p);
					}
				}

				for (int i = 0; i < presFiles.size(); i++) {
					PackageNode f = (PackageNode) presFiles.get(i);
					childList.add(f);
					f.setParent(node);
					f.setType(TYPE);
					List files = f.getChildren();
					if (files.size() > 0) {
						List pages = new ArrayList();
						for (int j = 0; j < files.size(); j++) {
							IFile file = (IFile) files.get(j);
							INode p = null;
							p = new FunctionUseCaseNode(new EclipseFileDelegate(file));
							p.setParent(f);
							pages.add(p);
						}
						f.setChildren(pages);
					}
				}
			}

			node.setChildren(childList);
			return childList.toArray();
		}
	}

	public AbstractNode getOrCreateNode(IFileDelegate file, boolean forceCreate) {
		EclipseSourceFolderDelegate sourceFolder = EclipseResourceManager.getSourceFolder((EclipseResourceDelegate) file);
		if(sourceFolder == null)
			return null;
		INode model = (INode) ModelManagerImpl.getInstance().getPool().getModel(sourceFolder, FunctionStructureModelFactory.TYPE, true, null);
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
