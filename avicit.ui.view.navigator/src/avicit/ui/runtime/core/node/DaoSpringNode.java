package avicit.ui.runtime.core.node;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.w3c.dom.Node;

import avicit.ui.common.util.XmlHelp;
import avicit.ui.core.runtime.resource.IFileDelegate;
import avicit.ui.core.runtime.resource.IResourceDelegate;
import avicit.ui.platform.common.data.XmlParsedFile;
import avicit.ui.runtime.core.action.OpenServiceConfigAction;

public class DaoSpringNode extends AbstractFileNode
{
	
    public DaoSpringNode(IFileDelegate folder)
    {
        super(folder);
        setOrder(2);
    }

    public Object getModel()
    {
        return this.getFile();
    }

    public List getModelChildren()
    {
    	List all = new ArrayList();
    	XmlParsedFile internalFile = new XmlParsedFile((IFile) this.getResource().getResource());
    	if(internalFile != null)
    	{
	    	List children = XmlHelp.getChildElementsByName(internalFile.getEle(),"bean");
	    	for(int i=0; i<children.size(); i++)
	    	{
	    		org.w3c.dom.Node node = (Node) children.get(i);
	    		org.w3c.dom.Node att = XmlHelp.getNodeAttribute(node, "id");
	    		if(att != null)
	    		{
	    			JavaBeanNode bean = new JavaBeanNode();
	    			bean.setParent(this);
	    			bean.setType(this.getType());
	    			bean.setId(att.getNodeValue());
	    			att = XmlHelp.getNodeAttribute(node, "name");
	    			if(att != null)
	    				bean.setName(att.getNodeValue());
	    			att = XmlHelp.getNodeAttribute(node, "class");
	    			if(att != null)
	    				bean.setClazz(att.getNodeValue());
	//    			att = XmlHelp.getNodeAttribute(node, "id");
	    			bean.setInf(att.getNodeValue());
	    			all.add(bean);
	    		}
	    	}
    	}
    	internalFile.close();
    	return all;
    }

    @Override
	public String getDisplayName() {
    	String name = this.getFile().getName();
		int dotIndex = name.indexOf(".");
		if(dotIndex>0)
			name = name.substring(0,dotIndex);
		return name;
	}

	public String getType()
    {
        //return DaoModelFactory.TYPE; update by lidong
        return "dao";
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
        if(IDoubleClickListener.class == adapter)
        {
        	return new OpenServiceConfigAction();
        }
        else
		{
            return super.getAdapter(adapter);
		}
    }
}