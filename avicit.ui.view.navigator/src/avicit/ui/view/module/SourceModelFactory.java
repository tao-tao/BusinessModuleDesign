package avicit.ui.view.module;


import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;

import avicit.ui.core.runtime.resource.IFolderDelegate;
import avicit.ui.core.runtime.resource.IResourceDelegate;
import avicit.ui.runtime.core.INode;
import avicit.ui.view.exception.ModelParseException;

public class SourceModelFactory extends AbstractModelFactory{

	public static String TYPE = "sour";
    
    public SourceModelFactory()
    {
        setPriority(17000);
        this.setType(TYPE);
    }

	@Override
	public boolean isAcceptable(IResourceDelegate ifiledelegate) {
		if(ifiledelegate instanceof IFolderDelegate)
		{
			IJavaElement f = JavaCore.create((IFolder)((IFolderDelegate)ifiledelegate).getResource());
			if(f == null)
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
			if(ifiledelegate instanceof IFolderDelegate)
			{
				return ((IFolderDelegate)ifiledelegate).getResource();
			}
			return null;
		}

		@Override
		public Object[] getChildren(INode object) {
	    	
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
	    	return new Object[0];
		}
    }
}
