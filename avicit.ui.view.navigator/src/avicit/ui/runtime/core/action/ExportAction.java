package avicit.ui.runtime.core.action;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.SelectionListenerAction;
import org.eclipse.ui.internal.dialogs.ExportWizard;

import avicit.ui.runtime.core.cluster.function.ComponentNode;

public class ExportAction extends SelectionListenerAction{

	public ExportAction(String text) {
		super(text);
	}

	public void run() {
		Object obj = getStructuredSelection().getFirstElement();
		if (obj == null)
			return;
		/*
		JarPackageWizard wizard = new JarPackageWizard();
		ComponentNode node = ((ComponentNode)obj);
		IFolder folder = (IFolder) node.getResource().getResource();
		JarPackageData jar = new JarPackageData();
		jar.setCompress(false);
		String version = node.getProperty("component.version");
		if(StringUtils.isEmpty(version))
			version = "1.0";
		jar.setManifestVersion(version);
		jar.setOverwrite(true);
		jar.setIncludeDirectoryEntries(true);
		jar.setElements(new Object[]{JavaCore.create(folder)});
		wizard.init(PlatformUI.getWorkbench(), jar);
		*/
		if(!(obj instanceof ComponentNode))
		{
			return;
		}
		ExportWizard wizard = new ExportWizard();
		
		wizard.init(PlatformUI.getWorkbench(), new StructuredSelection(obj));
		
		WizardDialog dialog = new WizardDialog(Display.getCurrent().getActiveShell(), wizard);
		dialog.create();
		int result = dialog.open();
		if (result == Dialog.OK) {
//			PackageNode folderNode = (PackageNode) obj;
//			PresentationFormNode childNode = new PresentationFormNode(new EclipseFileDelegate((IFile) wizard.getNewFile()));
//			childNode.setParent(folderNode);
//			folderNode.getChildren().add(childNode);
//			ProjectViewer viewer = ProjectNavigator.getViewer();
//			if (SwtResourceUtil.isValid(viewer.getTree())) {
//				viewer.getResourceMapper().refreshModelNode(IResourceDelta.ADDED, folderNode, childNode);
//			}
		}
		super.run();
	}

	protected boolean updateSelection(IStructuredSelection selection) {
		boolean flag = super.updateSelection(selection);
		if (flag)
			flag = getStructuredSelection().toList().size() == 1;
		return flag;
	}

}
