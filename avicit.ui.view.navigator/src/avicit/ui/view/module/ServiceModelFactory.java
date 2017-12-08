package avicit.ui.view.module;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;

import avicit.ui.common.util.FileUtil;
import avicit.ui.core.runtime.resource.EclipseFileDelegate;
import avicit.ui.core.runtime.resource.EclipseFolderDelegate;
import avicit.ui.core.runtime.resource.IFileDelegate;
import avicit.ui.core.runtime.resource.IFolderDelegate;
import avicit.ui.core.runtime.resource.IResourceDelegate;
import avicit.ui.runtime.core.IModelCreator;
import avicit.ui.runtime.core.INode;
import avicit.ui.runtime.core.cluster.function.ComponentModelFactory;
import avicit.ui.runtime.core.cluster.function.ComponentNode;
import avicit.ui.runtime.core.node.AbstractFileNode;
import avicit.ui.runtime.core.node.AbstractNode;
import avicit.ui.runtime.core.node.PackageNode;
import avicit.ui.runtime.core.node.ServiceNode;
import avicit.ui.runtime.core.node.SpringNode;
import avicit.ui.view.exception.ModelParseException;

public class ServiceModelFactory extends AbstractModelFactory implements IModelCreator{

	public static String TYPE = "servl";
	public static String EXTENSION = ".spring.xml";

	public ServiceModelFactory() {
		setPriority(17000);
		this.setType(TYPE);
	}
	
	protected String getExtension(){
		return EXTENSION;
	}

	@Override
	public boolean isAcceptable(IResourceDelegate ifiledelegate) {
		if (ifiledelegate instanceof IFolderDelegate) {
			IFolderDelegate folder = (IFolderDelegate) ifiledelegate;
			ComponentNode model = (ComponentNode) ModelManagerImpl.getInstance().getPool().getModel(folder, ComponentModelFactory.TYPE, true, null);
			String m = model.getIncludeModules();
			if (m != null && m.contains("service"))
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
			{
				IFolder resource = getConfigFolder((IFolderDelegate) ifiledelegate);
				return new ServiceNode(new EclipseFolderDelegate(resource));
			}
			return null;
		}

		@Override
		public Object[] getChildren(INode object) {
			ServiceNode node = (ServiceNode) object;
			List chidren = node.getChildren();
			if (chidren == null) {
				List presFiles = new ArrayList();
				IFolderDelegate folder = null;
				try {
					folder = (IFolderDelegate) node.getFolder().getParent().getParent();
				} catch (ResourceException e) {
					e.printStackTrace();
				}
				ComponentNode model = (ComponentNode) ModelManagerImpl.getInstance().getPool().getModel(folder, ComponentModelFactory.TYPE, true, null);
				if (model != null) {
					String file = model.getProperty("component.service");
					if (file != null) {
						if(StringUtils.isEmpty(file))
							file = getExtension();
						String[] fs = file.split(";");

						FileUtil.listFiles((IFolder) node.getResource().getResource(), presFiles, "", fs, "META-INF");
						for (int i = 0; i < presFiles.size(); i++) {
							PackageNode f = (PackageNode) presFiles.get(i);
							f.setParent(node);
							f.setType(getType());
							List files = f.getChildren();
							if (files.size() > 0) {
								List pages = new ArrayList();
								for (int j = 0; j < files.size(); j++) {
									AbstractFileNode pagex = createNode((IFile) files.get(j));
									pagex.setParent(f);
									pages.add(pagex);
								}
								f.setChildren(pages);
							}
						}
//						for (int i = 0; i < fs.length; i++) {
//							IFileDelegate f = folder.getFile(fs[i]);
//							if (f != null && f.exists()) {
//								SpringNode snode = new SpringNode(f);
//								snode.setParent(this);
//								snode.setType(node.getType());
//								presFiles.add(snode);
//							}
//						}
					}
				}

				node.setChildren(presFiles);
				return presFiles.toArray();
			}
			return chidren.toArray();
		}
		
		protected AbstractFileNode createNode(IFile f){
			return new SpringNode(new EclipseFileDelegate(f));
		}
	}

	protected AbstractFileNode createNode(IFile f){
		return new SpringNode(new EclipseFileDelegate(f));
	}
	
	protected IFolder getConfigFolder(IResourceDelegate ifiledelegate){

		String path = ifiledelegate.getProjectRelativePath();
		int i = path.indexOf("META-INF");
		if(i>=0)
			path = path.substring(0, i);
				
//		if(ifiledelegate instanceof IFolder )
//		{
		IFolder folder = null;
		try {
			folder = (IFolder)ifiledelegate.getProject().getFolder(path).getResource();
			try {
				if(!folder.exists())
					folder.create(true, true, null);
				folder = folder.getFolder(new Path("META-INF"));
				if(!folder.exists())
					folder.create(true, true, null);
				folder = folder.getFolder(new Path("spring"));
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
//		}
	}

	public AbstractNode getOrCreateNode(IFileDelegate file, boolean forceCreate) {
		try {
			ComponentNode model = (ComponentNode) ModelManagerImpl.getInstance().getPool().getModel(file.getSourceFolder(), ComponentModelFactory.TYPE, false, null);
			if (model != null && model.getIncludeModules().indexOf("service")>=0) {
				String sfile = model.getProperty("component.service");
				
				if (sfile != null) {

					if(StringUtils.isEmpty(sfile))
						sfile = getExtension();
					
					String[] fs = sfile.split(";");
					boolean isMatch = false;
					for(int j=0; j<fs.length; j++)
						if(file.getName().endsWith(fs[j]))
							isMatch = true;
					if(!isMatch)
						return null;
					
					IFolder container = (IFolder) getConfigFolder(file);
					Object pmodel = ModelManagerImpl.getInstance().getPool().getModel(new EclipseFolderDelegate(container), this.getType(), false, null);
					if (pmodel != null) {
						ServiceNode node = (ServiceNode) pmodel;
						
						IFolder folder = (IFolder) node.getResource().getResource();
						
						if(!folder.getProjectRelativePath().isPrefixOf(file.getResource().getProjectRelativePath()))
							return null;
						
						List presFiles = node.getChildren();
						IFolderDelegate delegate = file.getParent();
						PackageNode findNode = null;
						AbstractFileNode childNode = null;
						if (presFiles != null) {
							for (int i = 0; i < presFiles.size(); i++) {
								PackageNode f = (PackageNode) presFiles.get(i);
								if (f.getResource().equals(delegate)) {
									findNode = f;
									break;
								}
							}
						}
						if (findNode == null && forceCreate) {
							String packName = delegate.getResource().getProjectRelativePath().removeFirstSegments(folder.getProjectRelativePath().segmentCount()).toString().replace("/", ".");
							findNode = new PackageNode(packName, (IFolder) delegate.getResource(), new ArrayList());
							findNode.setParent(node);
							findNode.setType(getType());
							childNode = createNode((IFile) file.getResource());
							childNode.setParent(findNode);
							findNode.setOrder(-1);
							findNode.getChildren().add(childNode);
						} else {
							List children = findNode.getChildren();
							for (int i = 0; i < children.size(); i++) {
								if (((AbstractFileNode) children.get(i)).getResource().equals(file))
									childNode = (AbstractFileNode) children.get(i);
							}
							if (childNode == null && forceCreate) {
								childNode = createNode((IFile) file.getResource());
								childNode.setParent(findNode);
								findNode.getChildren().add(childNode);
							}
						}
						return childNode;
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}

}
