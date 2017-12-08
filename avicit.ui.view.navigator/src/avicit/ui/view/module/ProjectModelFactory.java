package avicit.ui.view.module;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

import avicit.ui.common.util.FileUtil;
import avicit.ui.core.runtime.resource.IFolderDelegate;
import avicit.ui.core.runtime.resource.IProjectDelegate;
import avicit.ui.core.runtime.resource.IResourceDelegate;
import avicit.ui.core.runtime.resource.ISourceFolderDelegate;
import avicit.ui.runtime.core.INode;
import avicit.ui.runtime.core.cluster.function.ComponentModelFactory;
import avicit.ui.runtime.core.cluster.function.ComponentNode;
import avicit.ui.runtime.core.node.AbstractFolderNode;
import avicit.ui.runtime.core.node.JavaLibrariesNode;
import avicit.ui.runtime.core.node.ProjectNode;
import avicit.ui.runtime.core.subsystem.SubSystemNode;
import avicit.ui.view.exception.ModelParseException;
import avicit.ui.view.navigator.builder.AvicitLibNature;

@SuppressWarnings("restriction")
public class ProjectModelFactory extends AbstractModelFactory {

	public static String TYPE = "proj";

	public ProjectModelFactory() {
		setPriority(17000);
		this.setType(TYPE);
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
		protected Object createNewNode(IResourceDelegate ifiledelegate) throws ModelParseException {
			if (ifiledelegate instanceof IProjectDelegate)
				return new ProjectNode((IProjectDelegate) ifiledelegate);
			return null;
		}
		
		private  IFolder getConfigFolder(AbstractFolderNode node){
			IProject proj=null;
			IFolder folder=null;
			try {
				proj =(IProject) (node.getResource().getProject().getResource());
				folder=proj.getFolder(new Path(""));
			} catch (ResourceException e) {
				e.printStackTrace();
			}
			return folder;
		}

		@Override
		public Object[] getChildren(INode object) {
			ProjectNode node = (ProjectNode) object;
			List chidren = node.getChildren();
			if (true) {
				List list = new ArrayList();
				IProjectDelegate projectDelegate = node.getProject();
				if(AvicitLibNature.isEcProject((IProject) projectDelegate.getResource()))
				{
					IResourceDelegate[] modules = projectDelegate.getChildren();
					for (int i = 0; i < modules.length; i++) {
						if(modules[i] instanceof IFolderDelegate)
						{
							Object model =  ModelManagerImpl.getInstance().getPool().getModel(((IFolderDelegate)modules[i]), ComponentLibModelFactory.TYPE, true, null);
							if(model != null)
								list.add(model);
						}
					}
					node.setChildren(list);
				}
				else
				{
					//2015-03-09 
					//projectDelegate.getFolders避免
					IResourceDelegate iResource[] = projectDelegate.getChildren();
					for(int i=0;i<iResource.length;i++){
						if(iResource[i] instanceof IFolderDelegate){
							System.out.println("iResource: " + iResource.toString());
						}
					}
					ISourceFolderDelegate[] sources = projectDelegate.getSourceFolders();
					Set resourceset = new HashSet();
	
					for (int i = 0; i < sources.length; i++) {
						Object model = null;
						try {
//							if(sources[i].getParent().getName().startsWith("sub-system")){
							IResourceDelegate[] iResourceDelegate = sources[i].getChildren();
							for(int j =0;j<iResourceDelegate.length;j++){
								if(iResourceDelegate[j].getName().equals("META-INF") || iResourceDelegate[j].getName().equals("i18n"))
									continue;
								//if(iResourceDelegate[j] instanceof IFolderDelegate && ((IFolderDelegate)iResourceDelegate[j]).getFile(SubSystemNode.SUBSYSTEM_DESC).exists()){
								if(iResourceDelegate[j] instanceof IFolderDelegate ){			
									List presFiles = new ArrayList();
									FileUtil.listSubSystemNode((IFolder) iResourceDelegate[j].getResource(), presFiles, "", new String[]{".sub"}, null);
									for(int k=0;k<presFiles.size();k++){
										System.out.println("XXXXXXXXXXXXXX" + presFiles.get(k).toString());
//										model =  ModelManagerImpl.getInstance().getPool().getModel((IResource)presFiles.get(k),SubSystemModelFactory.TYPE, true, null);
										SubSystemNode subNode = (SubSystemNode)presFiles.get(k);
										subNode.setParent(object);
										list.add(subNode);
									}
								}else if(iResourceDelegate[j] instanceof IFolderDelegate && ((IFolderDelegate)iResourceDelegate[j]).getFile(INode.CONFIG_FILE).exists())
									model =  ModelManagerImpl.getInstance().getPool().getModel(iResourceDelegate[j], ComponentModelFactory.TYPE, true, null);

								if(model instanceof ComponentNode)
								{
									((ComponentNode)model).setParent(object);
								}
								else if(model instanceof SubSystemNode){
									((SubSystemNode)model).setParent(object);	
								}
								else if(model == null)
								{
									model =  ModelManagerImpl.getInstance().getPool().getModel(sources[i], null, true, null);
								}
								if (model != null)
								{
									list.add(model);
									resourceset.add(sources[i].getResource());
								}
							}
							list.add(new JavaLibrariesNode(projectDelegate));
//							if(sources[i].getParent().getFile(".sub").exists()){
//								model =  ModelManagerImpl.getInstance().getPool().getModel(sources[i].getParent(),SubSystemModelFactory.TYPE, true, null);
//								if(null!=model)
//									((SubSystemNode)model).setChildren(null);//运行至此，说明resourceChanged，并且已经新建了projectNode，需要重新获取子节点
////							SubSystemNode subNode = new SubSystemNode(node.getFolder());
//							}
//							else
//								model =  ModelManagerImpl.getInstance().getPool().getModel(sources[i], ComponentModelFactory.TYPE, true, null);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
//						if(model instanceof ComponentNode)
//						{
//							((ComponentNode)model).setParent(object);
//						}
//						else if(model instanceof SubSystemNode){
//							((SubSystemNode)model).setParent(object);	
//						}
//						else if(model == null)
//						{
//							model =  ModelManagerImpl.getInstance().getPool().getModel(sources[i], null, true, null);
//						}
//						if (model != null)
//						{
//							list.add(model);
//							resourceset.add(sources[i].getResource());
//						}
					}
					
//					List presFiles = new ArrayList();
//					AbstractFolderNode node1 = (AbstractFolderNode)node;
//					FileUtil.listFiles((IFolder)  getConfigFolder(node), presFiles, "", new String[]{".acd"}, null);	
//					FileUtil.listFiles((IFolder)( node.getResource().getResource()1), presFiles, "", new String[]{".erd"}, null);
//					IFolder iFolder;
//					try {
////						iFolder = (IFolder) node.getResource().getProject().getFolder(node.getConfigPath());
//						String path = node.getResource().getResource().getFullPath().toString();
//						Path folderPath = new Path(path);
//						iFolder = (IFolder) node.getResource().getProject().getFolder(node.getResource().getResource().getFullPath().toString());
//						FileUtil.listFiles(iFolder, presFiles, "", new String[]{".acd"}, null);
//					} catch (ResourceException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
//					this.pro = (IProject) ((INode)obj1).getResource().getProject();	
					//2015-03-14 隐藏不必要的节点
//					CommonNode comm = new CommonNode(node.getFolder());
//					list.add(comm);
//					
//					avicit.ui.runtime.core.requirement.designer.GuizeNode desNode = new avicit.ui.runtime.core.requirement.designer.GuizeNode(node.getFolder());
//					list.add(desNode);
//					
//					AnalysisNode analysisNode = new AnalysisNode(node.getFolder());
//					list.add(analysisNode);
					
					IJavaProject javaproject = (IJavaProject) JavaCore.create(projectDelegate.getResource());
			        if(javaproject != null)
			        {
						Object resources[];
						try {
							resources = javaproject.getNonJavaResources();
					        for(int i = 0; i < resources.length; i++)
					        {
					        	if(!resourceset.contains(resources[i]))
					        		list.add(resources[i]);
					        }
						} catch (JavaModelException e) {
							e.printStackTrace();
						}
			        }
			        resourceset.clear();
			        resourceset = null;
					node.setChildren(list);
				}
				
				return list.toArray();
			}
			return chidren.toArray();
		}
	}
}
