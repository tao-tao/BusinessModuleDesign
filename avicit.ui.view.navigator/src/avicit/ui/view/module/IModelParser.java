package avicit.ui.view.module;


import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.runtime.IProgressMonitor;

import avicit.ui.core.runtime.resource.IResourceDelegate;
import avicit.ui.runtime.core.INode;
import avicit.ui.view.exception.ModelParseException;

public interface IModelParser {

	public abstract void save(IResourceDelegate ifiledelegate, Object obj)
			throws IOException;

	public abstract Object parse(IResourceDelegate ifiledelegate,
			IProgressMonitor iprogressmonitor) throws ModelParseException;

	public abstract Object parse(InputStream inputstream,
			IProgressMonitor iprogressmonitor) throws ModelParseException;

	public abstract Object[] getChildren(INode node);
	
	public abstract boolean hasChildren(INode node);

	public abstract boolean supportMetadataPersistence();
}