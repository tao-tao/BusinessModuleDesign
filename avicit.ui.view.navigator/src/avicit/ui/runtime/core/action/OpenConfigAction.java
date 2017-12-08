package avicit.ui.runtime.core.action;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.SelectionListenerAction;

public class OpenConfigAction extends SelectionListenerAction {

	public OpenConfigAction(String text) {
		super(text);
	}

	private IWorkbenchWindow window;

	private ISelection selection;

	/*
	 * (non-Javadoc) Method declared on IWorkbenchActionDelegate
	 */
	public void dispose() {
		// do nothing
	}

	/*
	 * (non-Javadoc) Method declared on IWorkbenchActionDelegate
	 */
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}

	/**
	 * The <code>WindowActionDelegate</code> implementation of this
	 * <code>IActionDelegate</code> method launches a stand-alone dialog that
	 * contains a list of sections for the selected readme file in the
	 * navigator.
	 */
	public void run() {
		IWorkbenchPage page = window.getActivePage();
		if (page == null)
			return;

		IProject jp = null;
		if (this.selection instanceof TreeSelection) {/*
			Object obj = ((TreeSelection) selection).getFirstElement();
			ProjectManager manager=new ProjectManager();
			manager.init(PlatformUI.getWorkbench(), new StructuredSelection(obj));
			WizardDialog dialog = new WizardDialog(Display.getCurrent().getActiveShell(), manager);
			dialog.create();
			int result = dialog.open();
			if (result == Dialog.OK) {
				
			}
			super.run();
		*/}
	}

	/**
	 * The <code>WindowActionDelegate</code> implementation of this
	 * <code>IActionDelegate</code> method does nothing - we will let simple
	 * rules in the XML config file react to selections.
	 */
	protected boolean updateSelection(IStructuredSelection selection) {
		super.updateSelection(selection);
		this.selection = selection;
		return true;
	}
}
