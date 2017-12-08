package avicit.ui.view.module;


import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;

import avicit.ui.core.runtime.resource.EclipseFileDelegate;
import avicit.ui.core.runtime.resource.EclipseFolderDelegate;
import avicit.ui.core.runtime.resource.IFolderDelegate;
import avicit.ui.core.runtime.resource.IResourceDelegate;
import avicit.ui.runtime.core.IModelCreator;
import avicit.ui.runtime.core.node.AbstractFileNode;
import avicit.ui.runtime.core.node.BpelNode;
import avicit.ui.runtime.core.node.ServiceCompositeNode;
import avicit.ui.view.exception.ModelParseException;

public class ServiceCompositeModelFactory extends ServiceModelFactory implements IModelCreator{

	public static String TYPE = "servb";
	public static String EXTENSION = ".biz.xml";

	public ServiceCompositeModelFactory() {
		super();
		setType(TYPE);
	}

	protected String getExtension(){
		return EXTENSION;
	}

	@Override
	public IModelParser getModelParser(Object obj, IProgressMonitor iprogressmonitor) {
		return new ModelParser();
	}

	protected IFolder getConfigFolder(IResourceDelegate ifiledelegate){

		String path = ifiledelegate.getProjectRelativePath();
		int i = path.indexOf("/META-INF");
		if(i>=0)
			path = path.substring(0, i);
				
		IFolder folder = null;
		try {
			folder = (IFolder)ifiledelegate.getProject().getFolder(path).getResource();
			try {
				if(!folder.exists())
					folder.create(true, true, null);
				folder = folder.getFolder(new Path("META-INF"));
				if(!folder.exists())
					folder.create(true, true, null);
				folder = folder.getFolder(new Path("bpel"));
				if(!folder.exists())
					folder.create(true, true, null);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		} catch (ResourceException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return folder;
	}

	public class ModelParser extends ServiceModelFactory.ModelParser {

		@Override
		public Object createNewNode(IResourceDelegate ifiledelegate) throws ModelParseException {
			if (ifiledelegate instanceof IFolderDelegate)
			{
				IFolder resource = getConfigFolder((IFolderDelegate) ifiledelegate);
				//return new ServiceCompositeNode(new EclipseFolderDelegate(resource));
				return null;
			}
			return null;
		}
		
		protected AbstractFileNode createNode(IFile f){
			return new BpelNode(new EclipseFileDelegate(f));
		}
	}

	protected AbstractFileNode createNode(IFile f){
		return new BpelNode(new EclipseFileDelegate(f));
	}
	
}
