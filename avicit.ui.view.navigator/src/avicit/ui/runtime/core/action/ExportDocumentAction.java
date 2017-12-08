package avicit.ui.runtime.core.action;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.SelectionListenerAction;

import avicit.ui.core.runtime.resource.EclipseFolderDelegate;
import avicit.ui.core.runtime.resource.IResourceDelegate;
import avicit.ui.runtime.core.action.wizard.ExportDocumentWizard;
import avicit.ui.runtime.core.node.AbstractNode;
import avicit.ui.word.GenerateWord;

public class ExportDocumentAction extends SelectionListenerAction {

	IStructuredSelection selection;

	public ExportDocumentAction(String text, IStructuredSelection selection, int flag) {
		super(text);
		this.selection = selection;
	}

	@Override
	public void run() {
		Object obj = selection.getFirstElement();
		if (obj == null)
			return;

		createWordDocument();
//		ExportDocumentWizard wizard = new ExportDocumentWizard();
//		wizard.init(PlatformUI.getWorkbench(), new StructuredSelection(obj));
//		WizardDialog dialog = new WizardDialog(Display.getCurrent().getActiveShell(), wizard);
//		dialog.create();
//		dialog.open();

		super.run();
	}

	private void createWordDocument(){
		IResourceDelegate delegate = ((AbstractNode)selection.getFirstElement()).getResource();

		if(delegate instanceof EclipseFolderDelegate){
			EclipseFolderDelegate folderDelegate = (EclipseFolderDelegate) delegate;

			GenerateWord generate = GenerateWord.getInstance();
			generate.generate(folderDelegate.getFolder("document").getResource());
		}
	}

	protected boolean updateSelection(IStructuredSelection selection) {
		boolean flag = super.updateSelection(selection);
		if (flag)
			flag = getStructuredSelection().toList().size() == 1;
		return flag;
	}
}
