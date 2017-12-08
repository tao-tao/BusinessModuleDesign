package avicit.ui.runtime.core.action.wizard;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.eclipse.core.resources.IFolder;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import avicit.ui.core.runtime.resource.IFolderDelegate;
import avicit.ui.runtime.core.node.AbstractFolderNode;
import avicit.ui.runtime.core.node.PackageNode;
import avicit.ui.view.navigator.Activator;


public class NewPackageWizard extends Wizard implements INewWizard {

	private NewPackageWizardPage newItemWizardPage;

	private IStructuredSelection selection;
	AbstractFolderNode node;
	
	PackageNode child;

	public NewPackageWizard() {
		super();
		setNeedsProgressMonitor(true);
		try {
			URL prefix = new URL(Activator.getDefault().getDescriptor().getInstallURL(), "icons/wizban/");
			ImageDescriptor id = ImageDescriptor.createFromURL(new URL(prefix, "package.gif"));
			setDefaultPageImageDescriptor(id);
		}catch (MalformedURLException e) {}
	}

	public void init(IWorkbench arg0, IStructuredSelection selection) {
		this.selection = selection;
		node = (AbstractFolderNode) selection.getFirstElement();
	}

	@Override
	public void addPages() {
		newItemWizardPage = new NewPackageWizardPage(selection);
		addPage(newItemWizardPage);
	}

	@Override
	public boolean performFinish() {
		String packName = newItemWizardPage.fetchData();
		packName = packName.trim();
		String pathName = packName.replace(".", "/");
		IFolderDelegate folder;
		try {
			folder = node.getResource().getProject().getFolder(node.getConfigPath() + "/" + pathName);
			if (!folder.exists()) {
				folder.create();
			}
			child = new PackageNode(packName, (IFolder) folder.getResource(), new ArrayList());

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "异常", "出现异常" + e.getClass().getSimpleName() + ":\n详细信息:" + e.getLocalizedMessage());
		}
		return false;
	}

	public PackageNode getChild() {
		return child;
	}
}
