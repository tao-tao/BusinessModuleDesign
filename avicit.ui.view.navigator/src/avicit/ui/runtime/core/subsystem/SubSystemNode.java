package avicit.ui.runtime.core.subsystem;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.internal.resources.Project;
import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;

import avicit.ui.core.runtime.resource.EclipseFileDelegate;
import avicit.ui.core.runtime.resource.IFileDelegate;
import avicit.ui.core.runtime.resource.IFolderDelegate;
import avicit.ui.core.runtime.resource.IResourceDelegate;
import avicit.ui.runtime.core.INode;
import avicit.ui.runtime.core.IPackageContainer;
import avicit.ui.runtime.core.action.DeleteFolderAction;
import avicit.ui.runtime.core.action.NewCodeGenerationAction;
import avicit.ui.runtime.core.action.NewComponentAction;
import avicit.ui.runtime.core.action.NewFunctionClusterAction;
import avicit.ui.runtime.core.action.OpenEpcNodeAction;
import avicit.ui.runtime.core.node.AbstractFolderNode;
import avicit.ui.runtime.core.node.ProjectNode;
import avicit.ui.view.create.ModulCreate;

/**
 * @author Tao Tao
 *
 */
@SuppressWarnings("restriction")
public class SubSystemNode extends AbstractFolderNode implements IPackageContainer
{
	Properties p;
	IFileDelegate ecfile;
	long loadTime = 0;

    public SubSystemNode(IFolderDelegate folder)
    {
        super(folder);
        setOrder(90);
        p = new Properties();
    	IFile file = (IFile) folder.getFile(INode.SUBSYSTEM_DESC).getResource();
    	ecfile = new EclipseFileDelegate(file);
    	reloadProperties();
    }

    public String getId(){
    	return p.getProperty("component.id");
    }

    public String getIncludeModules(){
    	return p.getProperty("component.modules");
    }

    public String getPath(){
    	return p.getProperty("assembly.version");
    }

    @Override
	public String getDisplayName() {
    	String name = p.getProperty("assembly.name");
//    	if(this.parent instanceof ComponentLibNode)
//    	{
//    		name = this.getFolder().getName();
//    	}
    	if(name != null)
    		return name;
    	return "组件";
	}

    public String getProperty(String name){
    	String ret = p.getProperty(name);
    	if(ret != null)
    		return ret;
    	return "";
    }

	public String getType()
    {
        return SubSystemModelFactory.TYPE;
    }

    public Object getParent()
    {
    	if(parent instanceof ProjectNode)
    		return ((ProjectNode)parent).getProject().getResource();
    	return parent;
    }

    public Object getParentNode()
    {
    	return parent;
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
//            return this.getFile().getResource();
		}
		else if(IDoubleClickListener.class == adapter)
		{
			return new OpenEpcNodeAction();
		}
		return null;
    }

    public Object getAdapter(Object adaptableObject, Class adapterType) {
    	return SubSystemWorkbenchAdapter.getInstance();
    }

    public void reloadProperties(){
    	InputStreamReader in = null;

        try {
        	IFile file = (IFile) ecfile.getResource();
        	long lastModified = file.getModificationStamp();

        	if(lastModified > loadTime)
        	{
        		in = new InputStreamReader(file.getContents(),"UTF-8");
//            	file.setCharset("UTF-8", null);
        		p.load(in);
        		loadTime = lastModified;
        	}
		} catch ( Exception e) {
			e.printStackTrace();
		}

		finally{
			if(in != null)
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
//	@Override
//	public AbstractNode getOrCreateNode(IFileDelegate file, boolean forceCreate) {
//		return ComponentModelCreatorAdapter.getInstance().getOrCreateNode(file, forceCreate);
//	}

//	public IFileDelegate getEcfile() {
//		return ecfile;
//	}
//
//	public void setEcfile(IFileDelegate ecfile) {
//		this.ecfile = ecfile;
//	}
	public String getConfigPath() {
		return this.getResource().getProjectRelativePath();
	}

    public List getChildren() {
		return children;
	}


//	@Override
//	public AbstractNode getOrCreateNode(IFileDelegate file, boolean forceCreate) {
//		return SubSystemNodeAdapter.getInstance().getOrCreateNode(file, forceCreate);
//	}

    public void createAction(IStructuredSelection selection,IMenuManager menu){
    	Project obj;
		obj = (Project) ((AbstractFolderNode)selection.getFirstElement()).getResource().getResource().getProject();

		NewComponentAction componentAction = new NewComponentAction("新建功能模块",obj,ModulCreate.constru);
		componentAction.selectionChanged(selection);
		menu.appendToGroup("group.edit", componentAction);

		NewFunctionClusterAction guizeAction = new NewFunctionClusterAction("创建功能集",obj,ModulCreate.constru);
		guizeAction.selectionChanged(selection);
		menu.appendToGroup("group.edit", guizeAction);

		DeleteFolderAction delAction = new DeleteFolderAction("删除组件",obj,ModulCreate.constru);
		delAction.selectionChanged(selection);
		menu.appendToGroup("group.edit", delAction);

		NewCodeGenerationAction codeAction = new NewCodeGenerationAction("自动生成代码");
		codeAction.selectionChanged(selection);
		menu.appendToGroup("group.edit", codeAction);
    }
}