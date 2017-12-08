package avicit.ui.runtime.core.cluster;

import java.io.InputStreamReader;
import java.util.Properties;

import org.eclipse.core.internal.resources.Project;
import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;

import avicit.ui.core.runtime.resource.EclipseFileDelegate;
import avicit.ui.core.runtime.resource.IFileDelegate;
import avicit.ui.core.runtime.resource.IFolderDelegate;
import avicit.ui.core.runtime.resource.IResourceDelegate;
import avicit.ui.runtime.core.IModelCreator;
import avicit.ui.runtime.core.IPackageContainer;
import avicit.ui.runtime.core.action.DeleteFolderAction;
import avicit.ui.runtime.core.action.NewCodeGenerationAction;
import avicit.ui.runtime.core.action.NewComponentAction;
import avicit.ui.runtime.core.cluster.function.ComponentNode;
import avicit.ui.runtime.core.node.AbstractFolderNode;
import avicit.ui.runtime.core.node.AbstractNode;
import avicit.ui.runtime.core.node.ProjectNode;
import avicit.ui.view.create.ModulCreate;

/**
 * @author Tao Tao
 *
 */
@SuppressWarnings("restriction")
public class FunctionClusterNode extends AbstractFolderNode implements IModelCreator {

	Properties p;
	IFileDelegate clusterFile;
	long loadTime = 0;
	boolean needReload = false;

	public boolean isNeedReload() {
		return needReload;
	}

	public void setNeedReload(boolean needReload) {
		this.needReload = needReload;
	}

	public FunctionClusterNode(IFolderDelegate folder,String s){
		super(folder);
	}

    public FunctionClusterNode(IFolderDelegate folder)
    {
        super(folder);
        setOrder(50);
        p = new Properties();
        //2015-03-14
    	IFile file = (IFile) folder.getFile(CLUSTER_FILE).getResource();
    	clusterFile = new EclipseFileDelegate(file);
    	reloadProperties();
    	initFolder(folder);
    }

    public void reloadProperties(){
    	InputStreamReader in = null;
        try {
        	clusterFile.getResource().refreshLocal(IResource.DEPTH_INFINITE, null); 	
        	IFile file = (IFile) clusterFile.getResource();
        	long lastModified = file.getModificationStamp();
        	if(lastModified > loadTime)
        	{
        		in = new InputStreamReader(file.getContents(),"UTF-8");
            	file.setCharset("UTF-8", null);
        		p.load(in);
        		loadTime = lastModified;
        		this.needReload = false;
			}
		} catch (Exception e) {
			this.needReload = true;
			e.printStackTrace();
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
	}

	public String getDisplayName() {
		String name = p.getProperty("component.name");

		if (this.parent instanceof ComponentNode) {
			name = this.getFolder().getName();
		}

		if (name != null)
			return name;

		return "功能集";
	}

	public void initFolder(IFolderDelegate folders) {
		IFolder folder = (IFolder) folders.getResource();

		try {
			if (!folder.exists())
				folder.create(true, true, null);

			if (!this.needReload)// 2015-03-07 防止重写reloadProperties方法中已经设置的标志位
				this.needReload = false;
		} catch (Exception e) {
			this.needReload = true;
			e.printStackTrace();
		}
	}

	@Override
	public Object getParent() {
		if (parent instanceof ProjectNode)
			return ((ProjectNode) parent).getProject().getResource();
		return parent;
	}

    public Object getParentNode()
    {
    	return parent;
    }

	@Override
	public String getType() {
		return FunctionClusterModelFactory.TYPE;
	}

    public void createAction(IStructuredSelection selection,IMenuManager menu){
       	Project obj = (Project) ((AbstractFolderNode)selection.getFirstElement()).getResource().getResource().getProject();

       	NewComponentAction requireAction = new NewComponentAction("新建功能模块",obj,ModulCreate.constru);
    	requireAction.selectionChanged(selection);
		menu.appendToGroup("group.edit", requireAction);

		DeleteFolderAction delAction = new DeleteFolderAction("删除功能集",obj,ModulCreate.constru);
		delAction.selectionChanged(selection);
		menu.appendToGroup("group.edit", delAction);

		NewCodeGenerationAction codeGenerationAction = new NewCodeGenerationAction("自动生成代码");
		codeGenerationAction.selectionChanged(selection);
		menu.appendToGroup("group.edit", codeGenerationAction);
    }

    public Object getAdapter(Object adaptableObject, Class adapterType) {
    	return FunctionClusterWorkbenchAdapter.getInstance();
    }

    public Object getAdapter(Class adapter)
    {
        if(IResourceDelegate.class == adapter)
            return getResource();
        if(IProject.class == adapter)
        {
			try {
				return getResource().getProject().getAdapter(adapter);
			} catch (ResourceException e) {
				return null;
			}
        }
		else if(IResource.class == adapter)
		{
            return this.getFolder().getFile(CLUSTER_FILE).getResource();
		}
		else if(IPackageContainer.class == adapter)
		{
            return this;
		}
		return null;
    }

	@Override
	public AbstractNode getOrCreateNode(IFileDelegate file, boolean forceCreate) {
		return FunctionClusterWorkbenchAdapter.getInstance().getOrCreateNode(file, forceCreate);
	}

    public String getProperty(String name){
    	String ret = p.getProperty(name);
    	if(ret != null)
    		return ret;
    	return "";
    }

	public String getPath() {
    	return this.getFolder().getSourceRelativePath();
	}
}
