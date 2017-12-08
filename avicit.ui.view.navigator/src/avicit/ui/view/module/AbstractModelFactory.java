package avicit.ui.view.module;


import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.IProgressMonitor;

import avicit.ui.core.runtime.resource.IFileDelegate;
import avicit.ui.core.runtime.resource.IFolderDelegate;
import avicit.ui.core.runtime.resource.IMessageCaller;
import avicit.ui.core.runtime.resource.IResourceDelegate;
import avicit.ui.runtime.core.INode;
import avicit.ui.runtime.core.node.AbstractFileNode;
import avicit.ui.view.exception.ModelParseException;

public class AbstractModelFactory implements IModelFactory{

	public String type;
	int priority;
	String category;
	int resource;
	String[] exts;
	
    public AbstractModelFactory()
    {
        setPriority(17000);
    }

    public IModelParser getModelParser(IResourceDelegate fileDelegate, IProgressMonitor progressMonitor)
    {
        return new ModelParser();
    }

    public String getBundleName()
    {
        return "com.tansun.ui.navigator";
    }

    public IModelCompiler getCompiler(Object model, IProgressMonitor progressMonitor)
    {
        return new Compliler();
    }

    public IModelValidator getValidator(Object model, IProgressMonitor progressMonitor)
    {
        return new Validator();
    }
    
    ////@Override
    public String getCategory()
    {
        return category;
    }

	//@Override
	public void setCategory(String cat) {
		this.category = cat;
	}

	public int getResourceType() {
		return resource;
	}

	public void setResourceType(int resource) {
		this.resource = resource;
	}

	//@Override
	public void setExtensionNames(String[] exts) {
		this.exts = exts;
	}

	//@Override
	public String[] getExtensionNames() {
		return this.exts;
	}

	//@Override
	public String getVersion() {
		return "4.0";
	}

	//@Override
	public boolean isAcceptable(IResourceDelegate ifiledelegate) {
		return false;
	}

	//@Override
	public void setPriority(int i) {
		priority=i;
	}

	//@Override
	public Object getAdapter(Class arg0) {
		return null;
	}

	//@Override
	public int getPriority() {
		return priority;
	}

	//@Override
	public IModelParser getModelParser(Object obj, IProgressMonitor iprogressmonitor) {
		return new ModelParser();
	}

	//@Override
	public boolean supportIdenticalExtension() {
		return true;
	}
	
    public class ModelParser implements IModelParser{

		//@Override
		public boolean hasChildren(INode IEcModel) {
			return false;
		}

		//@Override
		public Object parse(IResourceDelegate ifiledelegate,
				IProgressMonitor iprogressmonitor) throws ModelParseException {

			if(ifiledelegate instanceof IFolderDelegate)
			{
				//Modified by Tao Tao, 屏蔽使用Model Pool，为了防止因为Pool造成的节点显示不正常，另外Pool在实际中只作用于当project关闭重新打开时，作用不是很大。
				IModelPool pool = ModelManagerImpl.getInstance().getPool();
//				Object node =  pool.getModel(ifiledelegate, getType(), false, null);
//				if(node == null)
//				{
					Object node = createNewNode(ifiledelegate);
//					IResourceDelegate res = ifiledelegate;
//					if(node instanceof INode)
//					{
//						res = ((INode)node).getResource();
//					}
//					pool.attachModel(node, res);
//				}
				return node;
			}

			if(ifiledelegate instanceof IFileDelegate){
				Object node = createNode((IFile) ifiledelegate.getResource());
				return node;
			}

			return null;
		}

		protected Object createNewNode(IResourceDelegate ifiledelegate){
			return null;
		}

		//@Override
		public Object[] getChildren(INode object) {
	        return new Object[0];
		}
		
		//@Override
		public Object parse(InputStream inputstream,
				IProgressMonitor iprogressmonitor) throws ModelParseException {
			return null;
		}

		//@Override
		public void save(IResourceDelegate ifiledelegate, Object obj)
				throws IOException {
		}

		//@Override
		public boolean supportMetadataPersistence() {
			return false;
		}
    }
    public class Validator implements IModelValidator{

		//@Override
		public boolean validate(IResourceDelegate ifiledelegate,
				ModelChangeEvent modelchangeevent,
				IMessageCaller imessagecaller, IProgressMonitor iprogressmonitor) {
			return this.validate(ifiledelegate, null, modelchangeevent, imessagecaller, iprogressmonitor); 
		}

		//@Override
		public boolean validate(IResourceDelegate ifiledelegate, Object obj,
				ModelChangeEvent modelchangeevent,
				IMessageCaller imessagecaller, IProgressMonitor iprogressmonitor) {
			switch (modelchangeevent.getBuildKind())
			{
				case IResourceDelta.ADDED:
					
					break;
				case IResourceDelta.REMOVED:
					ModelManagerImpl.getInstance().getPool().detachModel(ifiledelegate, getType());
					break;
				case IResourceDelta.CHANGED:
					
					break;
			}
			return false;
		}
    	
    }
    public class Compliler implements IModelCompiler{

		//@Override
		public IResourceDelegate[] compile(IResourceDelegate ifiledelegate, int i,
				int j, IProgressMonitor iprogressmonitor,
				IMessageCaller imessagecaller) {
			
			return null;
		}

		//@Override
		public IResourceDelegate[] compile(IResourceDelegate ifiledelegate, int i,
				Object obj, int j, IProgressMonitor iprogressmonitor,
				IMessageCaller imessagecaller) {
			return null;
		}

		//@Override
		public IResourceDelegate[] getTargetResources(
				IResourceDelegate ifiledelegate) {
			return null;
		}
    	
    }
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	protected AbstractFileNode createNode(IFile f) {
		return null;
	}
}
