package avicit.ui.runtime.core.requirement.designer.logicflow;


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
import avicit.ui.core.runtime.resource.IFileDelegate;
import avicit.ui.core.runtime.resource.IFolderDelegate;
import avicit.ui.core.runtime.resource.IResourceDelegate;
import avicit.ui.runtime.core.IModelCreator;
import avicit.ui.runtime.core.INode;
import avicit.ui.runtime.core.node.AbstractNode;
import avicit.ui.runtime.core.node.PackageNode;
import avicit.ui.view.exception.ModelParseException;
import avicit.ui.view.module.AbstractModelFactory;
import avicit.ui.view.module.IModelParser;

public class GuizeModelFactory extends AbstractModelFactory implements IModelCreator{

	public static String TYPE = "lf";

	public GuizeModelFactory() {
		setPriority(17000);
		setType(TYPE);
	}

//	@Override
	public boolean isAcceptable(IResourceDelegate ifiledelegate) {
		/*if (ifiledelegate instanceof IFolderDelegate) {
			IFolderDelegate folder = (IFolderDelegate) ifiledelegate;
			ComponentNode model = (ComponentNode) ModelManagerImpl.getInstance().getPool().getModel(folder, ComponentModelFactory.TYPE, true, null);
			String m = model.getIncludeModules();
			if (m != null && m.contains("topdesigner"))
				return true;
		}*/
		return true;
	}

	@Override
	public IModelParser getModelParser(Object obj, IProgressMonitor iprogressmonitor) {
		return new ModelParser();
	}

	public class ModelParser extends AbstractModelFactory.ModelParser {

		@Override
		protected Object createNewNode(IResourceDelegate ifiledelegate) throws ModelParseException {
			if (ifiledelegate instanceof IFolderDelegate){
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
				FileUtil.listFiles((IFolder) node.getResource().getResource(), presFiles, "", new String[]{".lf"}, null);
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
							
							if(file.getName().endsWith("lf"))
								p = new LogicFlowNode(new EclipseFileDelegate(file));
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
				folder = folder.getFolder(new Path("logicflow"));
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

//	@Override
	public AbstractNode getOrCreateNode(IFileDelegate file, boolean forceCreate) {/*
		try {
			if(!file.getName().endsWith(".erd")){
				return null;
			}
			Object pmodel = ModelManagerImpl.getInstance().getPool().getModel(file.getParent().getParent(), this.getType(), false, null);
			if(pmodel != null)
			{
				AbstractFolderNode node = (AbstractFolderNode)pmodel;
				System.out.println(node.getResource().getFullPath()+"{{");
				IFolder folder = (IFolder)node.getResource().getResource();
				if(!folder.getProjectRelativePath().isPrefixOf(file.getResource().getProjectRelativePath()))
					return null;
				
				List presFiles = node.getChildren();
				IFolderDelegate delegate = file.getParent();
				PackageNode findNode = null;
				AbstractNode childNode = null;
				if(presFiles != null)
				{
					for(int i=0; i<presFiles.size(); i++)
			        {
						PackageNode f = (PackageNode) presFiles.get(i);
						if(f.getResource().equals(delegate))
						{
							findNode = f;
							break;
						}
			        }
				}
				if(findNode == null && forceCreate)
				{
					String packName = delegate.getFullPath().replace("/", ".");
					String[] str=packName.split("[.]");
					String pname=null;
					if(str.length>3){
						pname=str[2]+"."+str[3];
					}else{
						pname=str[2];
					}
					findNode = new PackageNode(pname, (IFolder) delegate.getResource(), new ArrayList());
					findNode.setParent(node);
					findNode.setType(TYPE);
					if(file.getName().endsWith("erd"))
						childNode = new ErNode(file);
					if(childNode==null){
						return null;
					}
					childNode.setParent(findNode);
					findNode.setOrder(-1);
					findNode.getChildren().add(childNode);
				}
				else
				{
					List children = findNode.getChildren();
					for(int i=0; i<children.size(); i++)
					{
						if(((AbstractFileNode)children.get(i)).getResource().equals(file))
							childNode =  (AbstractNode) children.get(i);
					}
					if(childNode == null && forceCreate)
					{
						
						if(file.getName().endsWith("erd"))
							childNode = new ErNode(file);
						if(childNode==null){
							return null;
						}
						childNode.setParent(findNode);
					}
					findNode.getChildren().add(childNode);
				}
				return childNode;
			}
		} catch (ResourceException e) {
		}
		return null;
	*/return null;}
}
