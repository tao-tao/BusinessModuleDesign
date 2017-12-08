package avicit.ui.runtime.core.develope.data;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
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
import avicit.ui.runtime.core.usecase.common.UseCaseCommonNode;
import avicit.ui.view.exception.ModelParseException;
import avicit.ui.view.module.AbstractModelFactory;
import avicit.ui.view.module.GuizeModelFactory;
import avicit.ui.view.module.IModelFactory;
import avicit.ui.view.module.IModelParser;
import avicit.ui.view.module.ModelManagerImpl;

public class DataAccessModelFactory  extends AbstractModelFactory implements IModelCreator{
	
	public static String TYPE = "dao";

	public DataAccessModelFactory() {
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
				return new DataAccessNode(new EclipseFolderDelegate(resource));
			}
			return null;
		}

		@Override
		public Object[] getChildren(INode object) {
			DataAccessNode node = (DataAccessNode) object;
			List chidren = node.getChildren();
			List childList = new ArrayList();
			if (true) {
				List presFiles = new ArrayList();
				FileUtil.listFiles((IFolder) node.getResource().getResource(), presFiles, "", new String[]{".java",".xml"}, null);
				PackageNode pn = null;
				//先判断存在defaultnode,递归规则保证了最后一个node是defaultnode
				if(FileUtil.existDefaultPackage((IFolder) node.getResource().getResource())){
					if(presFiles.size() >0){
						pn = (PackageNode)presFiles.remove(presFiles.size()-1);
						List fs = pn.getChildren();
						if (fs.size() > 0) {
							for (int j = 0; j < fs.size(); j++) {
								IFile file = (IFile) fs.get(j);
								INode p = null;
								p = new UseCaseCommonNode(new EclipseFileDelegate(file));
								p.setParent(node);
								childList.add(p);
							}
						}		
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
							p = new UseCaseCommonNode(new EclipseFileDelegate(file));
							p.setParent(f);
							pages.add(p);
						}
						f.setChildren(pages);
					}
				}
				node.setChildren(childList);
				return childList.toArray();
			}
			return chidren.toArray();
					
		}
	}
	
	private IFolder getConfigFolder(IResourceDelegate ifiledelegate){

		String path = ifiledelegate.getProjectRelativePath();
		int i = path.indexOf("META-INF");
		if(i>=0)
			path = path.substring(0, i);
				
//		if(ifiledelegate instanceof IFolder )
//		{
		IFolder folder = null;
		IFolder daFolder = null;
		try {
			folder = (IFolder)ifiledelegate.getProject().getFolder(path).getResource();
			try {
				if(!folder.exists())
					folder.create(true, true, null);
				daFolder = folder.getFolder(new Path("dao"));
				if(!daFolder.exists()){
					daFolder.create(true, true, null);
				}	
				folder = folder.getFolder(new Path("META-INF"));
				if(!folder.exists())
					folder.create(true, true, null);
//				folder = folder.getFolder(new Path("dao"));
//				if(!folder.exists())
//					folder.create(true, true, null);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		} catch (ResourceException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return daFolder;
//		}
	}

	public AbstractNode getOrCreateNode(IFileDelegate file, boolean forceCreate) {
		EclipseSourceFolderDelegate sourceFolder = EclipseResourceManager.getSourceFolder((EclipseResourceDelegate) file);
		if(sourceFolder == null)
			return null;
		INode model = (INode) ModelManagerImpl.getInstance().getPool().getModel(sourceFolder, GuizeModelFactory.TYPE, true, null);
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
