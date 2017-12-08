package avicit.ui.runtime.core.subsystem;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.internal.ui.navigator.JavaNavigatorContentProvider;

import avicit.ui.common.util.FileUtil;
import avicit.ui.core.runtime.resource.IFolderDelegate;
import avicit.ui.core.runtime.resource.IResourceDelegate;
import avicit.ui.runtime.core.INode;
import avicit.ui.runtime.core.cluster.FunctionClusterModelFactory;
import avicit.ui.runtime.core.cluster.function.ComponentModelFactory;
import avicit.ui.runtime.core.node.AbstractFolderNode;
import avicit.ui.runtime.core.node.AbstractNode;
import avicit.ui.runtime.core.requirement.requirement.usecase.UseCaseModelFactory;
import avicit.ui.view.exception.ModelParseException;
import avicit.ui.view.module.AbstractModelFactory;
import avicit.ui.view.module.IModelFactory;
import avicit.ui.view.module.IModelParser;
import avicit.ui.view.module.ModelManagerImpl;

/**
 * @author Tao Tao
 *
 */
@SuppressWarnings("restriction")
public class SubSystemModelFactory extends AbstractModelFactory {

	public static String TYPE = "sub-system";

    private transient JavaNavigatorContentProvider javaContentProvider;

    public SubSystemModelFactory() {
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
			return new SubSystemNode((IFolderDelegate)ifiledelegate);
		}

		@Override
		public Object[] getChildren(INode object) {
			if( object instanceof SubSystemNode ){
				return createOrGetChildren(object);
			}

			return ((AbstractFolderNode)object).getChildren().toArray();
		}
    }

	private static Object[] createOrGetChildren(INode object) {
		SubSystemNode node = (SubSystemNode) object;

		List cats = new ArrayList();
		IModelFactory[] factories = ModelManagerImpl.getInstance().getAllModelFactories();

		for (int i = 0; i < factories.length; i++) {
			String cat = factories[i].getCategory();

			if (cat.equals("requirementview")) {

				if (factories[i].isAcceptable(node.getFolder())) {

					Object child = factories[i].getModelParser(node.getFolder(), null).parse(node.getFolder(), null);

					if (child instanceof INode)
						((INode) child).setParent(node);

					if (child != null) {
						cats.add(child);
					}
				}
			}
		}

		IFolderDelegate folderDelegate = node.getFolder();
		IResourceDelegate[] resources = folderDelegate.getChildren();
		Object model = null;

		for (IResourceDelegate resource : resources) {

			if(resource.getName().equals(".sub") || resource.getName().equals("META-INF")){
				continue;
			}
//			if (resource.getName().equals(".sub")){
//				model = ModelManagerImpl.getInstance().getPool().getModel(resource, UseCaseModelFactory.TYPE, true, null);
//				((INode)model).setParent(node);
//				continue;
//			}
//
//			if (resource.getName().equals("META-INF")){
//				model = ModelManagerImpl.getInstance().getPool().getModel(resource, UseCaseModelFactory.TYPE, true, null);
//				((INode)model).setParent(node);
//				continue;
//			}

			if(resource.getResource().getFileExtension() != null && resource.getResource().getFileExtension().equals("xml")){
				model = ModelManagerImpl.getInstance().getPool().getModel(resource, UseCaseModelFactory.TYPE, true, null);
				((INode) model).setParent(node);
				cats.add(model);
			}

			IFolder resourceFolder = null;

			if (resource instanceof IFolderDelegate) {
				resourceFolder = (IFolder) resource.getResource();

				List result1 = new ArrayList();
				List result2 = new ArrayList();

				if (resourceFolder.exists()) {
					FileUtil.listComponentNode(resourceFolder, result1, "", new String[] { ".ec" }, null);
					FileUtil.listFunctionClusterNode(resourceFolder, result2, "", new String[] { ".clu" }, null);

					if (!result2.isEmpty()) {
						AbstractNode folder = (AbstractNode) result2.get(0);
						model = ModelManagerImpl.getInstance().getPool().getModel(folder.getResource(), FunctionClusterModelFactory.TYPE, true, null);
					}

					if (!result1.isEmpty() && result2.isEmpty()) {
						AbstractNode folder = (AbstractNode) result1.get(0);
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