package avicit.ui.runtime.core.action.providers;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.NewProjectAction;
import org.eclipse.ui.navigator.CommonActionProvider;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.eclipse.ui.navigator.ICommonViewerWorkbenchSite;

import avicit.ui.runtime.core.IPackageContainer;
import avicit.ui.runtime.core.action.ExportAction;
import avicit.ui.runtime.core.action.ImportAction;
import avicit.ui.runtime.core.action.NewAppFormAction;
import avicit.ui.runtime.core.action.NewBizAction;
import avicit.ui.runtime.core.action.NewControllerAction;
import avicit.ui.runtime.core.action.NewHibernateMapAction;
import avicit.ui.runtime.core.action.NewPackageAction;
import avicit.ui.runtime.core.action.NewPresentationAction;
import avicit.ui.runtime.core.action.NewSQLMapAction;
import avicit.ui.runtime.core.action.NewServiceAction;
import avicit.ui.runtime.core.action.NewWorkFlowAction;
import avicit.ui.runtime.core.action.RefreshAction;
import avicit.ui.runtime.core.cluster.FunctionClusterNode;
import avicit.ui.runtime.core.cluster.function.ComponentNode;
import avicit.ui.runtime.core.node.AbstractFileNode;
import avicit.ui.runtime.core.node.AbstractFolderNode;
import avicit.ui.runtime.core.node.NavigatorMessages;
import avicit.ui.runtime.core.node.PackageNode;
import avicit.ui.runtime.core.requirement.requirement.RequirementNode;
import avicit.ui.runtime.core.requirement.requirement.business.BusinessNode;
import avicit.ui.runtime.core.requirement.requirement.document.DocumentNode;
import avicit.ui.runtime.core.requirement.requirement.function.FunctionStructureNode;
import avicit.ui.runtime.core.subsystem.SubSystemNode;
import avicit.ui.view.navigator.ImageRepository;

/**
 * @author Tao Tao
 *
 */
public class CreateSubSystemActionProvider extends CommonActionProvider {

	private NewPresentationAction presaction;
	private NewAppFormAction app;
	private NewControllerAction controlleraction;
	private NewPackageAction action;
	private NewWorkFlowAction wfaction;
	private NewServiceAction serviceaction;
	private NewServiceAction daoaction;
	private NewHibernateMapAction hibernateaction;
	private NewSQLMapAction sqlaction;
	private RefreshAction refreshaction;

	ExportAction export;
	ImportAction imp;
	private NewBizAction bizaction;

	public CreateSubSystemActionProvider() {
		action = new NewPackageAction(NavigatorMessages.NEW_PACKAGE);
		ImageRepository imageRepository = ImageRepository
				.getImageRepository("com.tansun.ui.navigator");
		action.setImageDescriptor(imageRepository
				.getImageDescriptor("avicit.ui.runtime.core.node.PackageNode"));

//		subaction = new NewComponentAction(NavigatorMessages.NEW_CONTROLLER);
//		subaction.setImageDescriptor(imageRepository
//				.getImageDescriptor("avicit.ui.runtime.core.node.PackageNode"));	
//		
		controlleraction = new NewControllerAction(
				NavigatorMessages.NEW_CONTROLLER);
		controlleraction
				.setImageDescriptor(imageRepository
						.getImageDescriptor("avicit.ui.runtime.core.node.ControllerPageXNode"));

		presaction = new NewPresentationAction(NavigatorMessages.NEW_FORM);
		presaction
				.setImageDescriptor(imageRepository
						.getImageDescriptor("avicit.ui.runtime.core.node.PresentationFormNode"));

		app = new NewAppFormAction(NavigatorMessages.NEW_APP);
		app.setImageDescriptor(imageRepository
				.getImageDescriptor("avicit.ui.runtime.core.node.PresentationFormNode"));

		serviceaction = new NewServiceAction("CreatePackageActionProvider65", 0);
		serviceaction.setImageDescriptor(imageRepository
				.getImageDescriptor("avicit.ui.runtime.core.node.SpringNode"));

		daoaction = new NewServiceAction(NavigatorMessages.NEW_DAOCONFIG, 1);
		daoaction
				.setImageDescriptor(imageRepository
						.getImageDescriptor("avicit.ui.runtime.core.node.DaoSpringNode"));

		wfaction = new NewWorkFlowAction(NavigatorMessages.NEW_WORKFLOW);
		wfaction.setImageDescriptor(imageRepository
				.getImageDescriptor("avicit.ui.runtime.core.node.ProcessTemplateNode"));

		hibernateaction = new NewHibernateMapAction(
				NavigatorMessages.NEW_MAPPING);
		hibernateaction
				.setImageDescriptor(imageRepository
						.getImageDescriptor("avicit.ui.runtime.core.node.DataMappingNode"));

		sqlaction = new NewSQLMapAction(NavigatorMessages.NEW_SQLMAP);
		sqlaction
				.setImageDescriptor(imageRepository
						.getImageDescriptor("avicit.ui.runtime.core.node.DataSQLNode"));

		bizaction = new NewBizAction(NavigatorMessages.NEW_BIZ);
		bizaction
				.setImageDescriptor(imageRepository
						.getImageDescriptor("avicit.ui.runtime.core.node.ControllerPageXNode"));

		refreshaction = new RefreshAction();
		refreshaction.setImageDescriptor(imageRepository
				.getImageDescriptor("refresh"));

		export = new ExportAction("Export");
		export.setImageDescriptor(imageRepository.getImageDescriptor("export"));

		imp = new ImportAction("Import");
		imp.setImageDescriptor(imageRepository.getImageDescriptor("export"));

//		new NewGuizeAction("CreatePackageActionProvider104");

//		new ResourcePropertyOpenAction(NavigatorMessages.RESOURCE_PROPERTY);
	}

	public void init(ICommonActionExtensionSite extensionSite) {
		if (extensionSite.getViewSite() instanceof ICommonViewerWorkbenchSite) {
			org.eclipse.ui.IWorkbenchWindow window = ((ICommonViewerWorkbenchSite) extensionSite
					.getViewSite()).getWorkbenchWindow();
			ActionFactory.NEW.create(window);
			new NewProjectAction(
					((ICommonViewerWorkbenchSite) extensionSite.getViewSite())
							.getWorkbenchWindow());
		}
		// try{
		// editGroup = new EditActionGroup(extensionSite);
		// }catch(Exception e)
		// {
		// e.printStackTrace();
		// }
		super.init(extensionSite);
	}

	public void fillContextMenu(IMenuManager menu) {
		super.fillContextMenu(menu);
		IStructuredSelection selection = (IStructuredSelection) getContext()
				.getSelection();
		Object node = selection.getFirstElement();
		if (node instanceof IPackageContainer) {
			if (node instanceof SubSystemNode ||
				node instanceof avicit.ui.runtime.core.node.DevelopNode  ||
				node instanceof avicit.ui.runtime.core.requirement.designer.GuizeNode  ||
				node instanceof avicit.ui.runtime.core.requirement.requirement.RequirementNode ||
				node instanceof avicit.ui.runtime.core.requirement.requirement.function.FunctionStructureNode ||
				node instanceof avicit.ui.runtime.core.requirement.requirement.document.DocumentNode ||
				node instanceof avicit.ui.runtime.core.requirement.requirement.business.BusinessNode ||
				node instanceof avicit.ui.runtime.core.develope.bo.BoNode ||
				node instanceof avicit.ui.runtime.core.develope.display.ViewNode ||
				node instanceof avicit.ui.runtime.core.develope.logic.LogicNode ||
				node instanceof avicit.ui.runtime.core.develope.control.ControlNode ||
				node instanceof avicit.ui.runtime.core.develope.data.DataAccessNode ||
				node instanceof avicit.ui.runtime.core.develope.service.ServiceNode||
				node instanceof avicit.ui.runtime.core.requirement.requirement.usecase.UseCaseNode ||
				node instanceof avicit.ui.runtime.core.cluster.function.ComponentNode) {
			} else {
				action.selectionChanged(selection);
				menu.appendToGroup("group.edit", action);
			}
		}

		if (node instanceof PackageNode) {

			if (((PackageNode) node).getParent() instanceof AbstractFolderNode) {
				AbstractFolderNode anode = (AbstractFolderNode) ((PackageNode) node)
						.getParent();
				anode.createAction(selection, menu);

				((PackageNode) node).createAction(selection, menu);
			}
		}

		if(node instanceof RequirementNode){
			((RequirementNode) node).createAction(selection, menu);
		}

		if (node instanceof ComponentNode) {
			((ComponentNode) node).createAction(selection, menu);
		}

		if(node instanceof SubSystemNode){
			((SubSystemNode) node).createAction(selection, menu);
		}

		if(node instanceof FunctionClusterNode){
			((FunctionClusterNode)node).createAction(selection, menu);
		}

//		if (node instanceof JavaSourceNode) {
//		} else {
//			imp.selectionChanged(selection);
//
//			if (node instanceof AbstractFolderNode) {
//				refreshaction.selectionChanged(selection);
//				menu.appendToGroup("group.refactor", refreshaction);
//				// menu.appendToGroup("group editor", propertyAction);
//			}
//		}

        if(node instanceof FunctionStructureNode ||
           node instanceof DocumentNode ||
           node instanceof BusinessNode ||
           node instanceof avicit.ui.runtime.core.requirement.requirement.usecase.UseCaseNode
           )
        {
			((AbstractFolderNode) node).createAction(selection, menu);
        }

        if(node instanceof AbstractFileNode )
        {
        	((AbstractFileNode) node).createAction(selection, menu);
        }
	}
}