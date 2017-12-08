package com.jvisio.flowchart.diagram.part;

import org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.wizards.EditorCreationWizard;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;

/**
 * @generated
 */
public class FlowchartCreationWizard extends EditorCreationWizard {

	/**
	 * @generated
	 */
	public void addPages() {
		super.addPages();
		if (page == null) {
			page = new FlowchartCreationWizardPage(getWorkbench(),
					getSelection());
		}
		addPage(page);
	}

	/**
	 * @generated
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		super.init(workbench, selection);
		setWindowTitle("New Flowchart Diagram"); //$NON-NLS-1$
		setDefaultPageImageDescriptor(FlowchartDiagramEditorPlugin
				.getBundledImageDescriptor("icons/wizban/NewFlowchartWizard.gif")); //$NON-NLS-1$
		setNeedsProgressMonitor(true);
	}
}
