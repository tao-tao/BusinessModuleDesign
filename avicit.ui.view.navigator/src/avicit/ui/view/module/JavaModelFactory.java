package avicit.ui.view.module;


import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.ui.packageview.PackageExplorerContentProvider;

import avicit.ui.core.runtime.resource.IFolderDelegate;
import avicit.ui.core.runtime.resource.IResourceDelegate;
import avicit.ui.runtime.core.INode;
import avicit.ui.runtime.core.node.JavaSourceNode;
import avicit.ui.view.exception.ModelParseException;

public class JavaModelFactory extends AbstractModelFactory{

	public static String TYPE = "java";
    private transient PackageExplorerContentProvider javaContentProvider;
    
    public JavaModelFactory()
    {
        setPriority(17000);
        javaContentProvider = new PackageExplorerContentProvider(true);
        javaContentProvider.setIsFlatLayout(true);
        javaContentProvider.setShowLibrariesNode(true);
        this.setType(TYPE);
    }

	@Override
	public boolean isAcceptable(IResourceDelegate ifiledelegate) {
		if(ifiledelegate instanceof IFolderDelegate)
		{
//			IFolderDelegate folder = (IFolderDelegate) ifiledelegate;
//			ComponentNode model = (ComponentNode)ModelManagerImpl.getInstance().getPool().getModel(folder, true, null);
//			String m = model.getIncludeModules();
//			if(m != null && m.contains("java"))
				return true;
		}
		return false;
	}

	@Override
	public IModelParser getModelParser(Object obj,
			IProgressMonitor iprogressmonitor) {
		return new ModelParser();
	}
	
    public class ModelParser extends AbstractModelFactory.ModelParser{

		@Override
		protected Object createNewNode(IResourceDelegate ifiledelegate) throws ModelParseException {
//			if(ifiledelegate instanceof IFolderDelegate)
//			{
//				IJavaElement f = JavaCore.create((IFolder)((IFolderDelegate)ifiledelegate).getResource());
//				if(f != null)
//				{
//					JavaSourceNode node = new JavaSourceNode((IFolderDelegate)ifiledelegate);
//					node.setJavaElement(f);
//					getChildren(node);
//					return node;
//				}
//			}
			return null;
		}

		@Override
		public Object[] getChildren(INode object) {
			JavaSourceNode node = (JavaSourceNode)object;
	    	IJavaElement javaElement = node.getJavaElement();
	    	
	    	Object[] all = javaContentProvider.getChildren(javaElement);
//	    	if(object.getResource() instanceof IFolderDelegate)
//	    	{
//	    		IFolderDelegate mfolder = ((IFolderDelegate)object.getResource()).getFolder("META-INF");
//	    		if(mfolder.exists())
//	    		{
//			    	Object[] newall = new Object[all.length + 1];
//			    	System.arraycopy(all, 0, newall, 0, all.length);
//			    	newall[all.length] = mfolder.getResource();
//			    	return newall;
//	    		}
//	    	}
	    	return all;
		}
    }
}
