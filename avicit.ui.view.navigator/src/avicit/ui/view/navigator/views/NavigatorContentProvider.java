package avicit.ui.view.navigator.views;

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.ui.navigator.JavaNavigatorContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.model.IWorkbenchAdapter;

import com.tansun.runtime.resource.adapter.AbstractWorkbenchAdapter;
import com.tansun.runtime.resource.adapter.ExpressionAdapterManager;
import com.tansun.runtime.resource.adapter.ProjectWorkbenchAdapter;

import avicit.ui.common.util.AdapterUtil;
import avicit.ui.core.runtime.resource.EclipseProjectDelegate;
import avicit.ui.runtime.core.INode;
import avicit.ui.view.module.ModelManagerImpl;
import avicit.ui.view.module.ProjectModelFactory;
import avicit.ui.view.navigator.builder.AvicitNature;

public class NavigatorContentProvider extends JavaNavigatorContentProvider{
	
	
	public NavigatorContentProvider(){
		super(false);
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		IProject project;
		if (parentElement == null)
			return NavigatorContentProvider.NO_CHILDREN;
		if (parentElement instanceof IProject) {
			project = (IProject) parentElement;
			if (project.isOpen())
			{
				if (AvicitNature.isEcProject(project))
					return doGetProjectChildren(project);
			}
		} else if (parentElement instanceof INode) {
//			return null;
			return internalGetChildren(parentElement);
		}
		return super.getChildren(parentElement);
	}

	@Override
	public Object[] getElements(Object inputElement) {
        if(inputElement instanceof IWorkspaceRoot)
        {
            IWorkspaceRoot root = (IWorkspaceRoot)inputElement;
            return root.getProjects();
        }
        if(inputElement instanceof IJavaModel)
            return ((IJavaModel)inputElement).getWorkspace().getRoot().getProjects();
        if(inputElement instanceof IProject)
            return super.getElements(JavaCore.create((IProject)inputElement));
        else
            return super.getElements(inputElement);
    }
	protected Object[] doGetProjectChildren(IProject project) {
		Object model = ModelManagerImpl.getInstance().getPool().getModel(new EclipseProjectDelegate(project), ProjectModelFactory.TYPE, true, null);
		return ProjectWorkbenchAdapter.getInstance().getChildren(model);
	}

	public Object[] internalGetChildren(Object element) {
//		if (element instanceof IJavaElement) {
//			super.setIsFlatLayout(true);
//			Object children[] = super.getChildren(element);
//			if (!ArrayUtils.isEmpty(children)) {
//				if (children[0] instanceof IPackageDeclaration)
//					children = new Object[] { children[children.length - 1] };
//				return children;
//			}
//		}
		IWorkbenchAdapter adapter = getAdapter(element);
		if (adapter instanceof AbstractWorkbenchAdapter)
			return ((AbstractWorkbenchAdapter) adapter).getChildren(element);
		if (adapter != null)
			return adapter.getChildren(element);
		else
			return NavigatorContentProvider.NO_CHILDREN;
	}	

	public boolean hasChildren(Object element) {
		if (element instanceof IJavaElement)
			return super.hasChildren(element);
		IWorkbenchAdapter adapter = getAdapter(element);
		if (adapter instanceof AbstractWorkbenchAdapter) {
			AbstractWorkbenchAdapter newAdapter = (AbstractWorkbenchAdapter) adapter;
			return newAdapter.hasChildren(element);
		}
		if (adapter != null)
			return !ArrayUtils.isEmpty(adapter.getChildren(element));
		else
			return super.hasChildren(element);
	}
	
	public IWorkbenchAdapter getAdapter(Object element) {
		IWorkbenchAdapter adapter = (IWorkbenchAdapter) ExpressionAdapterManager.getInstance().getAdapter(element, IWorkbenchAdapter.class);
		if (adapter == null)
			adapter = (IWorkbenchAdapter) AdapterUtil.getAdapter(element, IWorkbenchAdapter.class);
		if (adapter == null)
			//return InternalWorkbenchAdapter.getInstance();
			return null;
		else
			return adapter;
	}
	public void inputChanged(Viewer aViewer, Object oldInput, Object newInput) {
		super.inputChanged(aViewer, oldInput, newInput);
	}

}
