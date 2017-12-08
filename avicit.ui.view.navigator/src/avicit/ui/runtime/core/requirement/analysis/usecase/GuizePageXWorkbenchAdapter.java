package avicit.ui.runtime.core.requirement.analysis.usecase;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.internal.ui.navigator.JavaNavigatorContentProvider;

import com.tansun.runtime.resource.adapter.AbstractModelWorkbenchAdapter;

public class GuizePageXWorkbenchAdapter extends AbstractModelWorkbenchAdapter
{

    public GuizePageXWorkbenchAdapter()
    {
        javaContentProvider = new JavaNavigatorContentProvider(true);
    }

    public static GuizePageXWorkbenchAdapter getInstance()
    {
        return instance;
    }

    public String getLabel(Object object)
    {
    	GuizePageXNode node = (GuizePageXNode)object;
        return node.getDisplayName();
    }


    @Override
	public boolean hasChildren(Object element) {
		return false;
	}

	public Object[] getChildren(Object object)
    {
//        List cats = new ArrayList();
    	GuizePageXNode node = (GuizePageXNode)object;
        List presFiles = new ArrayList();
        IResource pagex = node.getFile().getResource();
        String fname = pagex.getName();
        if(fname.indexOf(".")>0)
        	fname = fname.substring(0,fname.indexOf("."));
        IFile javaFile = pagex.getParent().getFile(new Path(fname + ".java"));
        if(javaFile.exists())
        {
        	return new Object[]{javaFile};
//        	presFiles.add(new ControllerJavaNode(new EclipseFileDelegate(javaFile)));
//        	ControllerJavaNode node = (ControllerJavaNode)object;
/*        	IJavaElement javaElement = JavaCore.create(javaFile);
        	IWorkbenchAdapter adapter = (IWorkbenchAdapter)ExpressionAdapterManager.getInstance().getAdapter(javaElement, IWorkbenchAdapter.class);
            if(adapter == null)
                adapter = (IWorkbenchAdapter)AdapterUtil.getAdapter(javaElement, IWorkbenchAdapter.class);
            Object[] childs =  adapter.getChildren(javaElement);
            return new Object[]{childs[childs.length-1]};
*/
        	}
//        ComponentNode node = (ComponentNode)object;
//        cats.add(new PresentationNode(node.getFolder()));
//        cats.add(new ControllerNode(node.getFolder()));
//        cats.add(new ServiceNode(node.getFolder()));
//        cats.add(new DataModelNode(node.getFolder()));
//        cats.add(new JavaSourceNode(node.getFolder()));
        return presFiles.toArray();
//        if(navigator != null)
//            javaContentProvider.setIsFlatLayout(navigator.isFlatStyle());
//        return javaContentProvider.getChildren(node.getModel());
    }

    private static final GuizePageXWorkbenchAdapter instance = new GuizePageXWorkbenchAdapter();
    private transient JavaNavigatorContentProvider javaContentProvider;
}