package avicit.ui.runtime.core.requirement.requirement.usecase;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
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
import avicit.ui.runtime.core.usecase.common.UseCaseCommonNode;
import avicit.ui.view.exception.ModelParseException;
import avicit.ui.view.module.AbstractModelFactory;
import avicit.ui.view.module.IModelParser;
import avicit.ui.view.module.ModelManagerImpl;

/**
 * @author Tao Tao
 *
 */
public class UseCaseModelFactory extends AbstractModelFactory implements IModelCreator{

	public static String TYPE = "ucd";
	public static String EXTENSION = ".ucd";

	public UseCaseModelFactory() {
		setPriority(17000);
		this.setType(TYPE);
	}
	
	protected String getExtension(){
		return EXTENSION;
	}

	@Override
	public boolean isAcceptable(IResourceDelegate ifiledelegate) {
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
				return new UseCaseNode(new EclipseFolderDelegate(resource));
			}
			return null;
		}

		@Override
		public Object[] getChildren(INode object) {
			UseCaseNode node = (UseCaseNode) object;
			List chidren = node.getChildren();
			List pages = new ArrayList();
			if (true) {
				List presFiles = new ArrayList();
				FileUtil.listFiles((IFolder) node.getResource().getResource(), presFiles, "", new String[]{".ucd"}, null);
				for (int i = 0; i < presFiles.size(); i++) {
					PackageNode f = (PackageNode) presFiles.get(i);
//					f.setParent(node);
//					f.setType(TYPE);
					List files = f.getChildren();
					if (files.size() > 0) {
						for (int j = 0; j < files.size(); j++) {
							AbstractFileNode pagex = createNode((IFile) files.get(j));
							pagex.setParent(f);
							pages.add(pagex);
						}
//						f.setChildren(pages);
					}
				}
				node.setChildren(pages);
				return pages.toArray();
			}
			return chidren.toArray();
		}
	}

	@Override
	protected AbstractFileNode createNode(IFile f){
		return new UseCaseCommonNode(new EclipseFileDelegate(f));
	}

	private IFolder getConfigFolder(IResourceDelegate ifiledelegate){

		String path = ifiledelegate.getProjectRelativePath();

		IFolder folder = null;
		try {
			folder = (IFolder) ifiledelegate.getProject().getFolder(path).getResource();

			if (!folder.exists())
				folder.create(true, true, null);

			folder = folder.getFolder(new Path("usercase"));

			if (!folder.exists())
				folder.create(true, true, null);

		} catch (Exception e) {
			e.printStackTrace();
		}

			return folder;
	}

	public AbstractNode getOrCreateNode(IFileDelegate file, boolean forceCreate) {
		try {
			ComponentNode model = (ComponentNode) ModelManagerImpl.getInstance().getPool().getModel(file.getSourceFolder(), ComponentModelFactory.TYPE, false, null);
			if (model != null && model.getIncludeModules().indexOf("usercase")>=0) {
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
						UseCaseNode node = (UseCaseNode) pmodel;
						
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
