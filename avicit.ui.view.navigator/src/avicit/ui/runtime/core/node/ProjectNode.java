package avicit.ui.runtime.core.node;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.w3c.dom.Node;

import avicit.ui.common.util.XmlHelp;
import avicit.ui.core.runtime.resource.IProjectDelegate;
import avicit.ui.core.runtime.resource.IResourceDelegate;
import avicit.ui.platform.common.data.XmlParsedFile;
import avicit.ui.runtime.core.action.NewComponentAction;
import avicit.ui.view.module.ProjectModelFactory;

@SuppressWarnings({ "unused", "restriction" })
public class ProjectNode extends AbstractFolderNode
{

	public static String TRANSACTIONATT = "org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource";
	
	IProjectDelegate project;
	
    public ProjectNode(IProjectDelegate project)
    {
        super(project);
        this.project = project;
        setOrder(5);
    }

    @Override
	public String getDisplayName() {
		return "��Ŀ";
	}

	public String getType()
    {
        return ProjectModelFactory.TYPE;
    }
	
    public Object getAdapter(Class adapter)
    {
        if(IResourceDelegate.class == adapter)
            return getResource();
        else if(IProject.class == adapter)
        {
			try {
				return getResource().getProject().getAdapter(adapter);
			} catch (ResourceException e) {
				return null;
			}
        }
		else
		{
            return super.getAdapter(adapter);
		}
    }

	public IProjectDelegate getProject() {
		return project;
	}

	public void setProject(IProjectDelegate project) {
		this.project = project;
	}
	
	public String getConfigPath() {
		return this.getResource().getProjectRelativePath();
	}	
	
   public void createAction(IStructuredSelection selection,IMenuManager menu){
//	   NewSubSystemAction guizeAction = new NewSubSystemAction("新建子系统");
//    	guizeAction.selectionChanged(selection);
//		menu.appendToGroup("group.edit", guizeAction);
    }
   
	public List getBeanFromCommonByType(String cls){
		List all = new ArrayList();
		if(cls != null)
		{
			IProject pro = (IProject) this.getResource().getResource();
			IFile file = pro.getFile(new Path("src-framework/META-INF/spring/common.spring.xml"));
			XmlParsedFile internalFile = new XmlParsedFile(file);
	    	if(internalFile != null)
	    	{
		    	List children = XmlHelp.getChildElementsByName(internalFile.getEle(),"bean");
		    	for(int i=0; i<children.size(); i++)
		    	{
		    		org.w3c.dom.Node node = (Node) children.get(i);
		    		org.w3c.dom.Node idatt = XmlHelp.getNodeAttribute(node, "id");
		    		if(idatt != null)
		    		{
		    			org.w3c.dom.Node att = XmlHelp.getNodeAttribute(node, "class");
		    			if(att != null && cls.equals(att.getNodeValue()))
		    			{
		    				JavaBeanNode bean = new JavaBeanNode();
		    				bean.setId(idatt.getNodeValue());
			    			bean.setClazz(att.getNodeValue());
			    			bean.setParent(this);
			    			bean.setType(this.getType());
			    			att = XmlHelp.getNodeAttribute(node, "name");
			    			if(att != null)
			    				bean.setName(att.getNodeValue());
			    			all.add(bean);
		    			}
		    		}
		    	}
	    	}
	    	internalFile.close();
		}
    	return all;
	}
}