package avicit.ui.runtime.core.node;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;

import avicit.ui.core.runtime.resource.EclipseResourceDelegate;
import avicit.ui.core.runtime.resource.IResourceDelegate;
import avicit.ui.runtime.core.IResourceNode;

public abstract class AbstractResourceNode extends AbstractNode implements
		IResourceNode {

	public AbstractResourceNode(IResourceDelegate resource) {
		setResource(resource);
		Assert.isNotNull(this.resource, "The resource should not be null.");
	}

	protected void setResource(IResourceDelegate resource) {
		this.resource = resource;
	}

	public IResourceDelegate getResource() {
		return resource;
	}

	public Object getModel() {
		return resource;
	}

	public boolean isVirtual() {
		return false;
	}

	public Object getAdapter(Class adapter) {
		if (IJavaElement.class == adapter) {
			EclipseResourceDelegate resourceDelegate = (EclipseResourceDelegate) getResource();
			IResource eclipseResource = resourceDelegate.getResource();
			return JavaCore.create(eclipseResource);
		}
		if (IResourceDelegate.class == adapter)
			return getResource();
		if (IResource.class.isAssignableFrom(adapter))
			return getResource().getAdapter(adapter);
		else
			return super.getAdapter(adapter);
	}

	public String toString() {
		return resource.getName();
	}

	public String getName() {
		return getResource().getName();
	}

	public int hashCode() {
		Object model = getModel();
		String type = getType();
		int result = 1;
		result = 31 * result + (model != null ? model.hashCode() : 0);
		result = 31 * result + (resource != null ? resource.hashCode() : 0);
		result = 31 * result + (type != null ? type.hashCode() : 0);
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractResourceNode other = (AbstractResourceNode) obj;
		if (!ObjectUtils.equals(resource, other.resource))
			return false;
		String type = getType();
		String otherType = other.getType();
		return StringUtils.equals(type, otherType);
	}

	public Object getParent() {
		return parent;
	}

	public void setParent(Object parent) {
		this.parent = parent;
	}

	public void setType(String type) {
	}

	public void createAction(IStructuredSelection selection, IMenuManager menu) {

	}

	public Object getAdapter(Object adaptableObject, Class adapterType) {
		return null;
	}

	public boolean cascadeDelete() {

		return false;
	}

	public boolean cascadeRename() {
		
		return false;
	}

	protected Object parent;
	protected IResourceDelegate resource;
}