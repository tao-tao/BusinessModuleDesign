package avicit.ui.runtime.core.node;


import java.io.ByteArrayInputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;

import avicit.ui.common.util.WorkbenchUtil;
import avicit.ui.view.navigator.Activator;



public class NewPageXWizard extends Wizard implements INewWizard {

	private IStructuredSelection selection;
	private NewPageXWizardPage page;
	private IFile newFile;

	public NewPageXWizard() {
		setWindowTitle("创建功能模块.");
		try {
			URL prefix = new URL(Activator.getDefault().getDescriptor().getInstallURL(), "icons/wizban/");
			ImageDescriptor id = ImageDescriptor.createFromURL(new URL(prefix, "controller.gif"));
			setDefaultPageImageDescriptor(id);
		}catch (MalformedURLException e) {}
	}

	public void init(IWorkbench workbench, IStructuredSelection currentSelection) {
		this.selection = currentSelection;
	}

	public void addPages() {
		page = new NewPageXWizardPage();
		addPage(page);
		page.init(selection);
	}

	public boolean performFinish() {
		try {
			newFile = page.getProcessFile();
			String fileName = newFile.getName();
			String processName = page.getProcessName();
			newFile.create(new ByteArrayInputStream(DesignerContentProvider.createInitialPageX(fileName, processName).getBytes()), true, null);

			IDE.openEditor(getActivePage(), newFile, WorkbenchUtil.EC_CONTROLLEREDITOR_ID);
			BasicNewResourceWizard.selectAndReveal(newFile, getActiveWorkbenchWindow());
			return true;
		} catch (CoreException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unused")
	private void openPropertiesView() {
		try {
			if (getActivePage().findView("org.eclipse.ui.views.PropertySheet") == null) {
				getActivePage().showView("org.eclipse.ui.views.PropertySheet");
			}
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}

	private IWorkbenchPage getActivePage() {
		return getActiveWorkbenchWindow().getActivePage();
	}

	private IWorkbenchWindow getActiveWorkbenchWindow() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow();
	}

	public IFile getNewFile() {
		return newFile;
	}

	public void setNewFile(IFile newFile) {
		this.newFile = newFile;
	}
}
