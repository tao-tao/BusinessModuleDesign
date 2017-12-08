package avicit.ui.runtime.core.requirement.designer.classm;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
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
import avicit.ui.view.exception.ModelParseException;
import avicit.ui.view.module.AbstractModelFactory;
import avicit.ui.view.module.IModelParser;
import avicit.ui.view.module.ModelManagerImpl;

public class GuizeModelFactory extends AbstractModelFactory implements IModelCreator{

	public static String TYPE = "cld";
	public static String EXTENSION = ".cld";

	public GuizeModelFactory() {
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
			if (m != null && m.contains("designer"))
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
				return new GuizeNode(new EclipseFolderDelegate(resource));
			}
			return null;
		}

		@Override
		public Object[] getChildren(INode object) {
			GuizeNode node = (GuizeNode) object;
			List chidren = node.getChildren();
			if (true) {
				List presFiles = new ArrayList();
				FileUtil.listFiles((IFolder) node.getResource().getResource(), presFiles, "", new String[]{".cld"}, null);
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
							
								p = new ClassNode(new EclipseFileDelegate(file));
								
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
		
		protected AbstractFileNode createNode(IFile f){/*
			return new ClassNode(new EclipseFileDelegate(f));
		*/return null;}
	}

	protected AbstractFileNode createNode(IFile f){
		//return new ClassNode(new EclipseFileDelegate(f));
		return null;
	}
	
	protected IFolder getConfigFolder(IResourceDelegate ifiledelegate){

		IProject project = null;		
		IFolder folder = null;
		try {
			project = (IProject)ifiledelegate.getProject().getResource();
			try {
				folder = project.getFolder(new Path("META-INF"));
				if(!folder.exists())
					folder.create(true, true, null);
				folder = folder.getFolder(new Path("design"));
				if(!folder.exists())
					folder.create(true, true, null);
				folder = folder.getFolder(new Path("classm"));
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
		try {
			ComponentNode model = (ComponentNode) ModelManagerImpl.getInstance().getPool().getModel(file.getSourceFolder(), ComponentModelFactory.TYPE, false, null);
			if (model != null && model.getIncludeModules().indexOf("designer")>=0) {
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
						GuizeNode node = (GuizeNode) pmodel;
						
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
