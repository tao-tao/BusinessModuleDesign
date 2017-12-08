package avicit.ui.runtime.core.node;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IDoubleClickListener;

import avicit.ui.core.runtime.resource.IFileDelegate;
import avicit.ui.core.runtime.resource.IResourceDelegate;
import avicit.ui.runtime.core.action.OpenFunctionNodeAction;

public class CommonChildNode extends AbstractFileNode
{

	String name;
	String tip;
	String editorID;
	String dbtype;
	ImageDescriptor image;
	
    public CommonChildNode(IFileDelegate file)
    {
        super(file);
        setOrder(2);
    }

    @Override
	public String getDisplayName() {
		return name;
	}
    
//    @Override
	public String getType()
    {
        return CommonNode.TYPE;
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
            return this.getFile().getResource();
		}
		else if(IDoubleClickListener.class == adapter)
		{
			return new OpenFunctionNodeAction();
		}
		return null;
    }

    public boolean equals(Object obj)
    {
        if(this == obj)
            return true;
        if(obj == null)
            return false;
        if(getClass() != obj.getClass())
            return false;
        CommonChildNode other = (CommonChildNode)obj;
        if(!ObjectUtils.equals(resource, other.resource))
            return false;
        String type = editorID;
        String otherType = other.getEditorID();
        return StringUtils.equals(type, otherType);
    }


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public ImageDescriptor getImage() {
		return image;
	}

	public void setImage(ImageDescriptor image) {
		this.image = image;
	}

	public String getEditorID() {
		return editorID;
	}

	public void setEditorID(String editorID) {
		this.editorID = editorID;
	}

	public String getDbtype() {
		return dbtype;
	}

	public void setDbtype(String dbtype) {
		this.dbtype = dbtype;
	}
}