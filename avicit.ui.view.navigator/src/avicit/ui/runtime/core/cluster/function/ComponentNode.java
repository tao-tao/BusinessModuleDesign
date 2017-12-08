package avicit.ui.runtime.core.cluster.function;

import java.io.InputStreamReader;
import java.util.Properties;

import org.eclipse.core.internal.resources.Project;
import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;

import avicit.ui.core.runtime.resource.EclipseFileDelegate;
import avicit.ui.core.runtime.resource.IFileDelegate;
import avicit.ui.core.runtime.resource.IFolderDelegate;
import avicit.ui.core.runtime.resource.IResourceDelegate;
import avicit.ui.runtime.core.IPackageContainer;
import avicit.ui.runtime.core.action.DeleteFolderAction;
import avicit.ui.runtime.core.node.AbstractFolderNode;
import avicit.ui.runtime.core.node.AbstractNode;
import avicit.ui.runtime.core.node.ProjectNode;
import avicit.ui.view.create.ModulCreate;

import com.tansun.runtime.resource.adapter.ComponentModelCreatorAdapter;
import com.tansun.runtime.resource.adapter.ComponentWorkbenchAdapter;

/**
 * @author Tao Tao
 *
 */
@SuppressWarnings("restriction")
public class ComponentNode extends AbstractFolderNode implements IPackageContainer
{
	Properties p;
	IFileDelegate ecfile;
	long loadTime = 0;
	boolean needReload = false;

	public boolean isNeedReload() {
		return needReload;
	}

	public void setNeedReload(boolean needReload) {
		this.needReload = needReload;
	}

    public ComponentNode(IFolderDelegate folder)
    {
        super(folder);
        setOrder(40);
        p = new Properties();
        //2015-03-14
    	IFile file = (IFile) folder.getFile(CONFIG_FILE).getResource();
    	ecfile = new EclipseFileDelegate(file);
    	reloadProperties();
    	initFolder(folder);
    }

	@Override
	public String getDisplayName() {
		String name = p.getProperty("component.name");

		if (this.parent instanceof ComponentNode) {
			name = this.getFolder().getName();
		}

		if (name != null)
			return name;

		return "功能结构建模";
	}

//  @Override
	public String getType()
    {
        return ComponentModelFactory.TYPE;
    }

	public String getConfigPath() {
		return this.getResource().getProjectRelativePath();
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
            return this.getFolder().getFile(CONFIG_FILE).getResource();
		}
		else if(IPackageContainer.class == adapter)
		{
            return this;
		}
		return null;
    }

    public void reloadProperties(){
    	InputStreamReader in = null;
        try {
        	ecfile.getResource().refreshLocal(IResource.DEPTH_INFINITE, null); 	
        	IFile file = (IFile) ecfile.getResource();
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

	private String createInitial(String name, String type) throws JavaModelException {
		StringBuffer buffer = new StringBuffer();
		buffer.append("component.name=" + name).append('\n');
		buffer.append("component.modules=" + "input,zhanshi,controller,ywc,jcc,gzmb,designer,").append('\n');
		if ("Comp".equals(type)) {
			buffer.append("component.iscomp=" + true).append('\n');
		} else {
			buffer.append("component.iscomp=" + false).append('\n');
		}

		return buffer.toString();
	}

    public void initFolder(IFolderDelegate folders){
    	IFolder folder=	(IFolder)folders.getResource();
    	
		try {
			if(!folder.exists())
				folder.create(true, true, null);
			IFolder	 folderm = folder.getFolder(new Path("META-INF"));
			if(!folderm.exists())
				folderm.create(true, true, null);
//			IFile ecFile = folderm.getFile(".ec");
//			if(!ecFile.exists()){
//				ecFile.create(
//						new ByteArrayInputStream(createInitial(folder.getName(),"Comp").getBytes("UTF-8")),
//						true, null);
////				ecFile.setCharset("UTF-8", null);	
//			}
//			folder = folderm.getFolder(new Path("exception"));
//			if(!folder.exists()){
//
//				folder.create(true, true, null);
//				IFile f=folder.getFile(new Path(this.getProperty("component.id")+"_exception.xml"));
//				if(!f.exists()){
//					try {
//						//update by lidong
//						StringBuffer buffer = new StringBuffer();
//						buffer.append("<?xml version=\"1.0\" encoding=\"gbk\"?>");
//						buffer.append("\n");
//						buffer.append("<exceptions>");
//						buffer.append("\n");
//						buffer.append("</exceptions>");
//						f.create(new ByteArrayInputStream(buffer.toString().getBytes("UTF-8")), true, null);
//					} catch (UnsupportedEncodingException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					f.setCharset("UTF-8", null);
//				}
//				
//				
//			
//			}
				
//			folder = folderm.getFolder(new Path("message"));
//			if(!folder.exists()){
//				folder.create(true, true, null);
//				IFile f=folder.getFile(new Path(this.getProperty("component.id")+"_message.properties"));
//				if(!f.exists()){
//					try {
//						f.create(new ByteArrayInputStream("".getBytes("UTF-8")), true, null);
//					} catch (UnsupportedEncodingException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					f.setCharset("UTF-8", null);
//				}
//				IFile fcn=folder.getFile(new Path(this.getProperty("component.id")+"_message_en.properties"));
//				if(!fcn.exists()){
//					try {
//						fcn.create(new ByteArrayInputStream("".getBytes("UTF-8")), true, null);
//					} catch (UnsupportedEncodingException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					fcn.setCharset("UTF-8", null);
//				}
//				
//				IFile fzh=folder.getFile(new Path(this.getProperty("component.id")+"_message_zh.properties"));
//				if(!fzh.exists()){
//					try {
//						fzh.create(new ByteArrayInputStream("".getBytes("UTF-8")), true, null);
//					} catch (UnsupportedEncodingException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					fcn.setCharset("UTF-8", null);
//				}
//				
//			}
			if(!this.needReload)//2015-03-07 防止重写reloadProperties方法中已经设置的标志位	
				this.needReload = false;
		} catch (Exception e) {
			this.needReload = true;
			e.printStackTrace();
		}
    }

    public String getId(){
    	return p.getProperty("component.id");
    }

    public String getIncludeModules(){
    	return p.getProperty("component.modules");
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

	public AbstractNode getOrCreateNode(IFileDelegate file, boolean forceCreate) {
		return ComponentModelCreatorAdapter.getInstance().getOrCreateNode(file, forceCreate);
	}

    public void createAction(IStructuredSelection selection,IMenuManager menu){
    	Project obj = (Project) ((AbstractFolderNode)selection.getFirstElement()).getResource().getResource().getProject();

		DeleteFolderAction delAction = new DeleteFolderAction("删除功能模块",obj,ModulCreate.constru);
		delAction.selectionChanged(selection);
		menu.appendToGroup("group.edit", delAction);		
    }

    public Object getAdapter(Object adaptableObject, Class adapterType) {
    	return ComponentWorkbenchAdapter.getInstance();
    }

    public String getProperty(String name){
    	String ret = p.getProperty(name);
    	if(ret != null)
    		return ret;
    	return "";
    }
}