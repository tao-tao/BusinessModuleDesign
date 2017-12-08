package avicit.ui.runtime.core.cluster;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.internal.ui.navigator.JavaNavigatorContentProvider;

import avicit.ui.common.util.FileUtil;
import avicit.ui.core.runtime.resource.IFolderDelegate;
import avicit.ui.core.runtime.resource.IResourceDelegate;
import avicit.ui.runtime.core.INode;
import avicit.ui.runtime.core.cluster.function.ComponentModelFactory;
import avicit.ui.runtime.core.node.AbstractNode;
import avicit.ui.view.exception.ModelParseException;
import avicit.ui.view.module.AbstractModelFactory;
import avicit.ui.view.module.IModelParser;
import avicit.ui.view.module.ModelManagerImpl;

/**
 * @author Tao Tao
 *
 */
@SuppressWarnings("restriction")
public class FunctionClusterModelFactory extends AbstractModelFactory {

	public static String TYPE = "funclu";

    private transient JavaNavigatorContentProvider javaContentProvider;

    public FunctionClusterModelFactory() {
        setPriority(17000);
        javaContentProvider = new JavaNavigatorContentProvider(true);
        javaContentProvider.setIsFlatLayout(true);
        this.setType(TYPE);
	}

	@Override
	public boolean isAcceptable(IResourceDelegate ifiledelegate) {
		if(ifiledelegate instanceof IFolderDelegate)
		{
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
			return new FunctionClusterNode((IFolderDelegate)ifiledelegate);
		}

		@Override
		public Object[] getChildren(INode object) {
			return createOrGetChildren(object);
		}
    }

	public static Object[] createOrGetChildren(INode object) {
		FunctionClusterNode node = (FunctionClusterNode) object;

		List cats = new ArrayList();

		IFolderDelegate folderDelegate = node.getFolder();
		IResourceDelegate[] resources = folderDelegate.getChildren();
		Object model = null;

		for (IResourceDelegate resource : resources) {

			if (resource.getName().equals(".clu"))
				continue;

			if (resource instanceof IFolderDelegate) {
				IFolder resourceFolder = (IFolder) resource.getResource();

				List result = new ArrayList();

				if (resourceFolder.exists()) {
					FileUtil.listComponentNode(resourceFolder, result, "", new String[] { ".ec" }, null);

					if (!result.isEmpty()) {
						AbstractNode folder = (AbstractNode) result.get(0);
						model = ModelManagerImpl.getInstance().getPool().getModel(folder.getResource(), ComponentModelFactory.TYPE, true, null);
					}

					if (model instanceof INode) {
						((INode) model).setParent(node);
					}

					if (model != null) {
						cats.add(model);
					}
				}
			}
		}

		return cats.toArray();
	}
}
